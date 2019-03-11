/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.finsiel.sbn.polo.factoring.transactionmaker;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.estensione.ClasseValida;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloClasse;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe FondeClasse
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due classe
 *
 * Valida la richiesta:
 * . i due simboli idPartenza e idArrivo devono esistere in tb_classe
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_cla con bid=spostaId e id
 * classe=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_cla con bid=spostaId e id classe=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_cla con id classe= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze
 * di tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 */
public class FondeClasse extends FondeElementoAutFoctoring {
    private FondeType fondeType;
    private String _id_arrivo;
    private String _id_partenza;
    private String[] _spostaId;
    private SbnUserType _sbnUser;
    private int maxSpostamenti;
    private String _spostaIdAttuale;
    private Tb_classe classeFusa;
    private TimestampHash _timeHash = new TimestampHash();

    public FondeClasse(SBNMarc input_root_object) {
        super(input_root_object);
        fondeType = input_root_object.getSbnMessage().getSbnRequest().getFonde();
        _id_arrivo = fondeType.getIdArrivo();
        _id_partenza = fondeType.getIdPartenza();
        _spostaId = fondeType.getSpostaID();
        _sbnUser = input_root_object.getSbnUser();
        maxSpostamenti = Integer.parseInt(ResourceLoader.getPropertyString("NRO_MAX_SPOSTAMENTO_LEGAMI"));
    }

    /**
     * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void eseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        try {
            verificaAbilitazioni();
            executeFonde();

        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            ecc.appendMessaggio("spostaId " + _spostaIdAttuale);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, _sbnUser);
            throw ecc;
        }
    }

    /**
     * controllo se si tratta di spostamento di legami o di fusione e lancio
     * il metodo adeguato
     * si spostano tutti i legami delle:
     * - tr_aut_aut
     * - tr_rep_aut
     * - tr_mar_aut
     * Method executeFonde.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void executeFonde() throws IllegalArgumentException, InvocationTargetException, Exception {

        ClasseValida classeValida = new ClasseValida();
        classeValida.validaPerFonde(_id_partenza, _id_arrivo, getCdUtente());
        //almaviva5_20100722 controllo sistemi abilitati in biblioteca
    	classeValida.controllaVettoreParametriSemantici(_id_partenza, this._sbnUser);
    	classeValida.controllaVettoreParametriSemantici(_id_arrivo, this._sbnUser);

        if (_spostaId.length > 0)
            spostamentoLegami();
        else
         fusion();
        Classe classe = new Classe();
        classeFusa = classe.cercaClassePerID(_id_arrivo);
        object_response = formattaOutput();
    }


    /**
     * Method fusion.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws InfrastructureException
     * @throws IllegalArgumentException
     */
    private void fusion() throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception {
        TitoloClasse titoloClasse = new TitoloClasse();
        List vettoreRisultati = titoloClasse.estraiLegamiTitoloClasse(_id_partenza);
        if (vettoreRisultati.size() > maxSpostamenti)
            throw new EccezioneSbnDiagnostico(3141,"Troppi identificativi comunicati: spezzare la richiesta in parti");
        Tr_tit_cla tr_tit_cla;
        for (int i=0; i<vettoreRisultati.size();i++){
            tr_tit_cla = (Tr_tit_cla) vettoreRisultati.get(i);
            _timeHash.putTimestamp("Tr_tit_cla", tr_tit_cla.getBID()
            + tr_tit_cla.getCD_SISTEMA()
            + tr_tit_cla.getCD_EDIZIONE()
            + tr_tit_cla.getCLASSE(),new SbnDatavar( tr_tit_cla.getTS_VAR()));
            _spostaIdAttuale = new String(tr_tit_cla.getBID());
            if (titoloClasse.estraiTitoloClasse(tr_tit_cla.getBID(),_id_arrivo) == null){
                titoloClasse.updateTitoliClasse(tr_tit_cla,_id_partenza,_id_arrivo,getCdUtente(),tr_tit_cla.getBID(),_timeHash);
            }else titoloClasse.cancellaLegame(_id_partenza,tr_tit_cla.getBID(),getCdUtente());

        }
        Tb_classe classeDaCancellare = new Tb_classe();
        Classe classe= new Classe();
        classeDaCancellare = classe.cercaClassePerID(_id_partenza);
        //almaviva5_20090414
        _timeHash.putTimestamp("Tb_classe", classeDaCancellare.getCD_SISTEMA()
                + classeDaCancellare.getCD_EDIZIONE()
                + classeDaCancellare.getCLASSE(),
                new SbnDatavar( classeDaCancellare.getTS_VAR()));

        classe.cancellaClasse(classeDaCancellare,getCdUtente(),_timeHash);


    }


    private void spostamentoLegami() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (_spostaId.length > maxSpostamenti)
            throw new EccezioneSbnDiagnostico(
                3140,
                "Troppi identificativi comunicati: spezzare la richiesta in parti");
        for (int i = 0; i < _spostaId.length; i++) {
            _spostaIdAttuale = new String(_spostaId[i]);
            TitoloClasse titoloClasse = new TitoloClasse();
            Tr_tit_cla tr_tit_cla = new Tr_tit_cla();
            tr_tit_cla = titoloClasse.estraiTitoloClasse(_spostaId[i].toString(), _id_partenza);
            if (tr_tit_cla == null)
                throw new EccezioneSbnDiagnostico(3029, "legame inesistente");
            _timeHash.putTimestamp(
                "Tr_tit_cla",
                tr_tit_cla.getBID()
                    + tr_tit_cla.getCD_SISTEMA()
                    + tr_tit_cla.getCD_EDIZIONE()
                    + tr_tit_cla.getCLASSE(),
                new SbnDatavar( tr_tit_cla.getTS_VAR()));
            if (titoloClasse.estraiTitoloClasse(_spostaId[i].toString(), _id_arrivo) != null)
                throw new EccezioneSbnDiagnostico(3030, "legame già esistente");
            titoloClasse.updateTitoliClasse(
                tr_tit_cla,
                _id_partenza,
                _id_arrivo,
                getCdUtente(),
                _spostaId[i].toString(),
                _timeHash);
        }
    }

    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiElementoType datiResp = new DatiElementoType();
//        datiResp.setT001(classeFusa.getCD_SISTEMA().trim() +
//            Decodificatore.getCd_unimarc("Tb_classe","cd_edizione",classeFusa.getCD_EDIZIONE())
//            + classeFusa.getCLASSE());
        datiResp.setT001(classeFusa.getCD_SISTEMA()+
                classeFusa.getCD_EDIZIONE()
                + classeFusa.getCLASSE().trim());

        SbnDatavar data = new SbnDatavar(classeFusa.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(classeFusa.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.CL);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_CLASSE_1273;
    }
}
