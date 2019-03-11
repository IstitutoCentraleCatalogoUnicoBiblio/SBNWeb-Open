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
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
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
 * Classe FondeSoggetto
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due soggetti
 *
 * Valida la richiesta:
 * . i due soggetti dPartenza e idArrivo devono esistere in tb_soggetto
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_sog con bid=spostaId e
 * cid=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_sog con bid=spostaId e cid=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_sog con cid= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze
 * di tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami tr_tit_sog legati
 * a idPartenza, si  verifica che il numero di legami non sia maggiore di un
 * valore massimo gestito in un property di sistema, altrimenti restituisce un
 * diagnostico: 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame tr_tit_sog: verifica esistenza tr_tit_sog con bid=bid e
 * cid=idArrivo, se esiste interrompe il servizio e segnala diagnostico 'legame
 * già esistente' comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami tr_tit_sog si cancella il soggetto
 * idPartenza.
 */
public class FondeSoggetto extends FondeElementoAutFoctoring {
	private FondeType 		fondeType;
	private String 		_id_arrivo;
	private String 		_id_partenza;
	private String[] 		_spostaId;
	private SbnUserType 	_sbnUser;
	private int 			maxSpostamenti;
	private String 		_spostaIdAttuale;
	private Tb_soggetto	soggettoArrivo;
	private TimestampHash	_timeHash = new TimestampHash();
	private boolean isFusione;

   public FondeSoggetto(SBNMarc input_root_object){
		super(input_root_object);
		fondeType = input_root_object.getSbnMessage().getSbnRequest().getFonde();
		_id_arrivo = fondeType.getIdArrivo();
		_id_partenza = fondeType.getIdPartenza();
		_spostaId = fondeType.getSpostaID();
		//almaviva5_20091117
		isFusione = (_spostaId.length == 0);
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
		SoggettoValida soggettoValida =  new SoggettoValida();
		soggettoValida.validaPerFonde(getCdUtente(), _id_partenza, _id_arrivo, isFusione);

		//almaviva5_20140221
		Soggetto soggetto = new Soggetto();
		soggettoArrivo = soggetto.cercaSoggettoPerId(_id_arrivo);

		if (isFusione)
			eseguiFusione();
		else
			eseguiSpostamentoLegami();

		object_response = formattaOutput();
	}


	/**
	 * Method eseguiFusione.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws InfrastructureException
	 * @throws IllegalArgumentException
	 */

	private void eseguiFusione() throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception {
		SoggettoTitolo soggettoTitolo = new SoggettoTitolo();
		TableDao tavola = soggettoTitolo.cercaLegamiSoggettoTitolo(_id_partenza);
        List<Tr_tit_sog_bib> vettoreRisultati = tavola.getElencoRisultati();

		int size = ValidazioneDati.size(vettoreRisultati);
		if (size > maxSpostamenti)
			throw new EccezioneSbnDiagnostico(3141, "Troppi identificativi comunicati: spezzare la richiesta in parti");

		for (int i = 0; i < size; i++) {
			Tr_tit_sog_bib tr_tit_sog = vettoreRisultati.get(i);
			_timeHash.putTimestamp("Tr_tit_sog_bib",
					tr_tit_sog.getBID()	+ tr_tit_sog.getCID(),
					new SbnDatavar( tr_tit_sog.getTS_VAR()));
			_spostaIdAttuale = new String(tr_tit_sog.getBID());
			Tr_tit_sog_bib legameArrivo = soggettoTitolo.estraiTitoloSoggetto(tr_tit_sog.getBID(), _id_arrivo);
			if (legameArrivo == null)
				//sposta legame
				soggettoTitolo.updateTitoliSoggetto(_id_partenza, soggettoArrivo, getCdUtente(), tr_tit_sog.getBID(), _timeHash);
			else {
				soggettoTitolo.cancellaLegameTitoloSoggetto(_id_partenza, tr_tit_sog.getBID(), getCdUtente());
				//almaviva5_20151201 #6046 se il legame arrivo è disattivato va riattivato
				if (ValidazioneDati.in(legameArrivo.getFL_CANC(), "s", "S") ) {
					legameArrivo.setFL_CANC(" ");
					soggettoTitolo.update(legameArrivo);
				}
			}
		}

		Tb_soggetto soggettoDaCancellare = new Tb_soggetto();
        Soggetto soggetto = new Soggetto();
        soggettoDaCancellare = soggetto.cercaSoggettoPerId(_id_partenza);
		soggetto.cancellaSoggetto(soggettoDaCancellare, getCdUtente());
	}


	/**
	 * Method eseguiSpostamentoLegami.
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
	private void eseguiSpostamentoLegami() throws IllegalArgumentException, InvocationTargetException, Exception {

		if (_spostaId.length > maxSpostamenti)
			throw new EccezioneSbnDiagnostico(3140, "Troppi identificativi comunicati: spezzare la richiesta in parti");

		for (int i = 0; i < _spostaId.length; i++) {
			_spostaIdAttuale = new String(_spostaId[i]);
			SoggettoTitolo soggettoTitolo = new SoggettoTitolo();
			Tr_tit_sog_bib tr_tit_sog = soggettoTitolo.estraiTitoloSoggetto(_spostaId[i], _id_partenza);
			if (tr_tit_sog == null)
				throw new EccezioneSbnDiagnostico(3029, "legame inesistente");
			else
				_timeHash.putTimestamp("Tr_tit_sog_bib",
						tr_tit_sog.getBID()	+ tr_tit_sog.getCID(),
						new SbnDatavar( tr_tit_sog.getTS_VAR()));

			Tr_tit_sog_bib legameArrivo = soggettoTitolo.estraiTitoloSoggetto(_spostaId[i], _id_arrivo);
			if (legameArrivo == null)
				//sposta legame
				soggettoTitolo.updateTitoliSoggetto(_id_partenza, soggettoArrivo, getCdUtente(), _spostaId[i], _timeHash);
			else {
				//almaviva5_20151201 #6046 se il legame arrivo è disattivato va riattivato
				if (ValidazioneDati.in(legameArrivo.getFL_CANC(), "s", "S") ) {
					legameArrivo.setFL_CANC(" ");
					soggettoTitolo.update(legameArrivo);
					//disattiva legame partenza
					soggettoTitolo.cancellaLegameTitoloSoggetto(_id_partenza, _spostaIdAttuale, getCdUtente());
				} else
					throw new EccezioneSbnDiagnostico(3030, "legame già esistente");
			}

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
        datiResp.setT001(soggettoArrivo.getCID());
        SbnDatavar data = new SbnDatavar(soggettoArrivo.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(soggettoArrivo.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.SO);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_SOGGETTO_1272;
    }

}
