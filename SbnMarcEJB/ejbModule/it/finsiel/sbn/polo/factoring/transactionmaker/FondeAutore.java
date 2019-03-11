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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.AllineamentoAutore;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreAutore;
import it.finsiel.sbn.polo.oggetti.AutoreBiblioteca;
import it.finsiel.sbn.polo.oggetti.AutoreMarca;
import it.finsiel.sbn.polo.oggetti.RepertorioAutore;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
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
 * Classe FondeAutore
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due autori
 *
 * Valida la richiesta:
 * . i due autori idPartenza e idArrivo devono esistere in tb_autore, e devono
 * essere in forma accettata.
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_aut con bid=spostaId e
 * vid=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_aut con bid=spostaId e vid=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_aut con vid= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze
 * di tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami tr_tit_aut legati
 * a idPartenza, si  verifica che il numero di legami non sia maggiore di un
 * valore massimo gestito in un property di sistema, altrimenti restituisce un
 * diagnostico: 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame tr_tit_aut: verifica esistenza tr_tit_aut con bid=bid e
 * vid=idArrivo, se esiste interrompe il servizio e segnala diagnostico 'legame
 * già esistente' comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami tr_tit_aut si spostano i legami tr_aut_aut
 * da idPartenza a idArrivo (update di vid sui legami), si spostano tr_rep_aut e
 * tr_mar_aut.
 * Per tutti e tre i tipi legame si controllo che non esista già la stessa
 * relazione con idArrivo, nel qual caso non si esegue nessuna operazione.
 * Al termine dell'elaborazione sui legami si cancella l'autore idPartenza e i
 * suoi legami ad eccezione dei legami tr_aut_bib.
 */
public class FondeAutore extends FondeElementoAutFoctoring {

    private SbnUserType _sbnUser;
    private Tb_autore autoreFuso;
    private FondeType fondeType;
    private String _id_arrivo;
    private String _id_partenza;
    private String[] _spostaId;
    private static int maxSpostamenti =
        Integer.parseInt(ResourceLoader.getPropertyString("NRO_MAX_SPOSTAMENTO_LEGAMI"));
    private String _spostaIdAttuale;
    private TimestampHash _timeHash = new TimestampHash();

    public FondeAutore(SBNMarc input_root_object) {
        super(input_root_object);
        fondeType = input_root_object.getSbnMessage().getSbnRequest().getFonde();
        _id_arrivo = fondeType.getIdArrivo();
        _id_partenza = fondeType.getIdPartenza();
        _spostaId = fondeType.getSpostaID();
        _sbnUser = input_root_object.getSbnUser();
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

    protected void verificaLocalizzazioniCancellazione(
        String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }
        AutoreBiblioteca autBib = new AutoreBiblioteca();
        if (!autBib.verificaEsistenzaTr_aut_bib(_id_partenza, utente.substring(0, 3)))
            //Da confrontare con titolofonde
            throw new EccezioneSbnDiagnostico(3115, "Autore non localizzato nel polo");
    }

    protected void fondeConLink(String user)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autoreDB = new Autore();
        Tb_autore aut1 = autoreDB.estraiAutorePerID(_id_partenza);
        Tb_autore aut2 = autoreDB.estraiAutorePerID(_id_arrivo);

        aut2.setUTE_VAR(user);
        aut2.setVID_LINK(aut2.getVID());
        aut2.setVID(aut1.getVID());
        aut2.setTS_VAR(aut1.getTS_VAR());
        new Autore().eseguiUpdate(aut2);
        AllineamentoAutore allineamentoAutore = new AllineamentoAutore(aut1);
        allineamentoAutore.setTb_autore(true);
        AutoreAllineamento titoloG = new AutoreAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoAutore, user);

        allineamentoAutore = new AllineamentoAutore(aut2);
        allineamentoAutore.setTr_aut_aut(true);
        titoloG.aggiornaFlagAllineamento(allineamentoAutore, user);
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
        String user = getCdUtente();
        verificaLocalizzazioniCancellazione(user);
        AutoreValida autoreValida = new AutoreValida();
        autoreValida.validaPerFonde(user, _id_partenza, _id_arrivo);
        boolean fondeConLink = false;
        if (_spostaId.length > 0)
            spostamentoLegami();
        else {
            if (!ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_1027)) {
                if (!ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_AUTORE_1270)) {
                    if (ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202)) {
                        fondeConLink = true;
                    } else {
                        throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
                    }
                }
            }
            if (fondeConLink)
                fondeConLink(user);
            else {
                fusion();
             }
        }
        Autore autore = new Autore();
        if (fondeConLink) {
            autoreFuso = autore.estraiAutorePerID(_id_partenza);
        } else {
            autoreFuso = autore.estraiAutorePerID(_id_arrivo);
        }
        object_response = formattaOutput();
    }

    /**
     * Method fusion.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void fusion() throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloAutore titoloAutore = new TitoloAutore();
        TableDao tavola = titoloAutore.cercaLegamiAutoreTitolo(_id_partenza);
        List<Tr_tit_aut> vettoreRisultati = tavola.getElencoRisultati();

        if (vettoreRisultati.size() > maxSpostamenti)
            throw new EccezioneSbnDiagnostico(
                3141,
                "Troppi identificativi comunicati: spezzare la richiesta in parti");
        Tr_tit_aut tr_tit_aut;

        // Bug esercizio 6443: almaviva2 17/07/2017 in caso di fusione è necessario impostare i campi di relazione/responsabilità
        // quando si salva il legame sulla tabella dei  _timeHash
        for (int i = 0; i < vettoreRisultati.size(); i++) {
            tr_tit_aut = vettoreRisultati.get(i);
            _timeHash.putTimestamp("Tr_tit_aut",
                tr_tit_aut.getBID() + tr_tit_aut.getVID() +
                tr_tit_aut.getCD_RELAZIONE() + tr_tit_aut.getTP_RESPONSABILITA(),
                new SbnDatavar( tr_tit_aut.getTS_VAR()));
            _spostaIdAttuale = new String(tr_tit_aut.getBID());

            // bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
            if (titoloAutore.estraiTitoloAutore(tr_tit_aut.getBID(), _id_arrivo) == null) {
                titoloAutore.updateTitoliAutore(
                    _id_partenza,
                    _id_arrivo,
                    tr_tit_aut.getCD_RELAZIONE(),
		            tr_tit_aut.getTP_RESPONSABILITA(),
                    getCdUtente(),
                    tr_tit_aut.getBID(),
                    _timeHash);
            } else
                titoloAutore.cancellaLegameTitoloAutore(_id_partenza, tr_tit_aut.getBID(), getCdUtente());

        }
        AutoreAutore autoreAutore = new AutoreAutore();
        autoreAutore.spostaLegami(_id_partenza, _id_arrivo, getCdUtente());

        RepertorioAutore repertorioAutore = new RepertorioAutore();
        repertorioAutore.spostaLegami(_id_partenza, _id_arrivo, getCdUtente() );

        AutoreMarca autoreMarca = new AutoreMarca();
        autoreMarca.spostaLegamiAutore(_id_partenza, _id_arrivo, getCdUtente());


        //alla fine cancello l'autore idPartenza (attenzione a non cancellare i tr_aut_bib)

        //Sulla tr_aut_bib con il vid dell'autore cancellato deve essere impostato
        //fl_allinea con lo stesso valore della modifica dati.
        Tb_autore autoreDaCancellare = new Tb_autore();
        Autore autore = new Autore();
        autoreDaCancellare = autore.estraiAutorePerID(_id_partenza);

        autoreDaCancellare.setFL_CANC("S");
        AllineamentoAutore allineamentoAutore = new AllineamentoAutore(autoreDaCancellare);
        allineamentoAutore.setTb_autore(true);
        AutoreAllineamento titoloG = new AutoreAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoAutore, getCdUtente());

        Tb_autore autArrivo = autore.estraiAutorePerID(_id_arrivo);
        allineamentoAutore = new AllineamentoAutore(autArrivo);
        allineamentoAutore.setTr_aut_aut(true);
        titoloG.aggiornaFlagAllineamento(allineamentoAutore, getCdUtente());

        autoreAutore.cancellaLegami(autoreDaCancellare.getVID(), getCdUtente(), autoreDaCancellare.getTP_FORMA_AUT());
        autore.cancellaAutoreFuso(autoreDaCancellare,_id_arrivo ,getCdUtente());
        new AutoreBiblioteca().aggiornaFlagCancCancellazione(_id_partenza,getCdUtente());
    }

    /**
     * Method spostamentoLegami.
     * - verifica che il numero di elementi non sia maggiore di un valore massimo
     *   gestito in un properties di sistema altrimenti restituisce un diagnostico
     *   "troppi identificativi comunicati: spezzare la richiesta in parti"
     * - per ogni spostaId verifica esistenza tr_tit_aut con bid = spostaId e
     *   vid = idPartenza, se non esiste segnala diagnostico "legame inesistente"
     *   seguito da _spostaId
     * - se controlli sono ok modifica tr_tit_aut con vid = id_arrivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    private void spostamentoLegami() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (_spostaId.length > maxSpostamenti)
            throw new EccezioneSbnDiagnostico(
                3140,
                "Troppi identificativi comunicati: spezzare la richiesta in parti");
        for (int i = 0; i < _spostaId.length; i++) {
            _spostaIdAttuale = new String(_spostaId[i]);
            TitoloAutore titoloAutore = new TitoloAutore();
            Tr_tit_aut tr_tit_aut = new Tr_tit_aut();
            tr_tit_aut = titoloAutore.estraiTitoloAutore(_spostaId[i].toString(), _id_partenza);
            if (tr_tit_aut == null)
                throw new EccezioneSbnDiagnostico(3029, "legame inesistente");
            else

                // Bug esercizio 6443: almaviva2 17/07/2017 in caso di fusione è necessario impostare i campi di relazione/responsabilità
                // quando si salva il legame sulla tabella dei  _timeHash
                _timeHash.putTimestamp(
                    "Tr_tit_aut",
                    tr_tit_aut.getBID() + tr_tit_aut.getVID() +
                    tr_tit_aut.getCD_RELAZIONE() + tr_tit_aut.getTP_RESPONSABILITA(),
                    new SbnDatavar( tr_tit_aut.getTS_VAR()));
            if (titoloAutore.estraiTitoloAutore(_spostaId[i].toString(), _id_arrivo, tr_tit_aut.getCD_RELAZIONE(),tr_tit_aut.getTP_RESPONSABILITA()) != null)
                throw new EccezioneSbnDiagnostico(3030, "legame già esistente");
            // bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
            titoloAutore.updateTitoliAutore(
                _id_partenza,
                _id_arrivo,
                tr_tit_aut.getCD_RELAZIONE(),
	            tr_tit_aut.getTP_RESPONSABILITA(),
                getCdUtente(),
                _spostaIdAttuale,
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
        datiResp.setT001(autoreFuso.getVID());
        SbnDatavar data = new SbnDatavar(autoreFuso.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(autoreFuso.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.AU);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_AUTORE_1270;
    }
}
