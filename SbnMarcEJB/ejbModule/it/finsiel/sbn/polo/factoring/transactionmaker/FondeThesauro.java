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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.ThesauroTitolo;
import it.finsiel.sbn.polo.oggetti.estensione.TermineValida;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
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
import java.util.ArrayList;
import java.util.List;



/**
 * Classe FondeThesauro
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due thesauro
 *
 * Valida la richiesta:
 * . i due thesauri dPartenza e idArrivo devono esistere in tb_thesauro
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza Trs_termini_titoli_biblioteche con bid=spostaId e
 * cid=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza Trs_termini_titoli_biblioteche con bid=spostaId e cid=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica Trs_termini_titoli_biblioteche con did= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze
 * di tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami Trs_termini_titoli_biblioteche legati
 * a idPartenza, si  verifica che il numero di legami non sia maggiore di un
 * valore massimo gestito in un property di sistema, altrimenti restituisce un
 * diagnostico: 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame Trs_termini_titoli_biblioteche: verifica esistenza Trs_termini_titoli_biblioteche con bid=bid e
 * cid=idArrivo, se esiste interrompe il servizio e segnala diagnostico 'legame
 * già esistente' comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami Trs_termini_titoli_biblioteche si cancella il Thesauro
 * idPartenza.
 */
public class FondeThesauro extends FondeElementoAutFoctoring {
	private FondeType 		fondeType;
	private String 		_id_arrivo;
	private String 		_id_partenza;
	private String[] 		_spostaId;
	private SbnUserType 	_sbnUser;
	private int 			maxSpostamenti;
	private String 		_spostaIdAttuale;
	private Tb_termine_thesauro	termineFuso;
	private TimestampHash	_timeHash = new TimestampHash();

   public FondeThesauro(SBNMarc input_root_object){
		super(input_root_object);
		fondeType = input_root_object.getSbnMessage().getSbnRequest().getFonde();
		_id_arrivo = fondeType.getIdArrivo();
		_id_partenza = fondeType.getIdPartenza();
		_spostaId = fondeType.getSpostaID();
		_sbnUser = input_root_object.getSbnUser();
		maxSpostamenti = Integer.parseInt(ResourceLoader.getPropertyString("NRO_MAX_SPOSTAMENTO_LEGAMI")) ;
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
            if (!(_spostaIdAttuale == null))
	            ecc.appendMessaggio("Id = " + _spostaIdAttuale);
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
		Tb_termine_thesauro termineDaCancellare = new Tb_termine_thesauro();
		TermineValida termineValida =  new TermineValida();
		termineValida.validaPerFonde(getCdUtente(),_id_partenza, _id_arrivo);
		if (_spostaId.length > 0)
			spostamentoLegami();
		else fusion();
		TermineThesauro termine = new TermineThesauro();
		termineFuso = termine.cercaTerminePerId(_id_arrivo);
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
		ThesauroTitolo thesauroTitolo = new ThesauroTitolo();
		TableDao tavola = thesauroTitolo.cercaLegamiThesauroTitolo(_id_partenza);
        List vettoreRisultati = new ArrayList();
		vettoreRisultati = tavola.getElencoRisultati();

		if (vettoreRisultati.size() > maxSpostamenti)
			throw new EccezioneSbnDiagnostico(3141,"Troppi identificativi comunicati: spezzare la richiesta in parti");
		Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche;
		for (int i=0; i<vettoreRisultati.size();i++){
			trs_termini_titoli_biblioteche = (Trs_termini_titoli_biblioteche) vettoreRisultati.get(i);
			_timeHash.putTimestamp("Trs_termini_titoli_biblioteche",
					trs_termini_titoli_biblioteche.getBID()
							+ trs_termini_titoli_biblioteche.getDID(),
					new SbnDatavar( trs_termini_titoli_biblioteche.getTS_VAR()));
			_spostaIdAttuale = new String(trs_termini_titoli_biblioteche.getBID());
			if (thesauroTitolo.estraiTitoloThesauro(trs_termini_titoli_biblioteche.getBID(),_id_arrivo) == null){
				thesauroTitolo.updateTitoliThesauro(_id_partenza,_id_arrivo,getCdUtente(),trs_termini_titoli_biblioteche.getBID(),_timeHash);
			}else thesauroTitolo.cancellaLegameTitoloThesauro(_id_partenza,trs_termini_titoli_biblioteche.getBID(),getCdUtente());

		}
		Tb_termine_thesauro termineDaCancellare = new Tb_termine_thesauro();
		TermineThesauro termineThesauro = new TermineThesauro();
        termineDaCancellare = termineThesauro.cercaTerminePerId(_id_partenza);
        termineThesauro.cancellaTermine_thesauro(termineDaCancellare,getCdUtente());


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
			throw new EccezioneSbnDiagnostico(3140,"Troppi identificativi comunicati: spezzare la richiesta in parti");
		for (int i=0;i<_spostaId.length;i++){
			_spostaIdAttuale = new String(_spostaId[i]);
			ThesauroTitolo thesauroTitolo = new ThesauroTitolo();
			Trs_termini_titoli_biblioteche trs_termini_titoli_biblioteche = null;
			trs_termini_titoli_biblioteche = thesauroTitolo.estraiTitoloThesauro(_spostaId[i].toString(),_id_partenza);
			if (trs_termini_titoli_biblioteche == null)
				throw new EccezioneSbnDiagnostico(3029,"legame inesistente");
			else
				_timeHash.putTimestamp("Trs_termini_titoli_biblioteche",
						trs_termini_titoli_biblioteche.getBID()
								+ trs_termini_titoli_biblioteche.getDID(),
						new SbnDatavar( trs_termini_titoli_biblioteche.getTS_VAR()));
			if (thesauroTitolo.estraiTitoloThesauro(_spostaId[i].toString(),_id_arrivo) != null)
				throw new EccezioneSbnDiagnostico(3030,"legame già esistente");
			thesauroTitolo.updateTitoliThesauro(_id_partenza,_id_arrivo,getCdUtente(),_spostaId[i].toString(),_timeHash);
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
        datiResp.setT001(termineFuso.getDID());
        SbnDatavar data = new SbnDatavar(termineFuso.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(termineFuso.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.TH);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_THESAURO_1300;
        // TODO INSERIRE CODICI THESAURO
    }

}
