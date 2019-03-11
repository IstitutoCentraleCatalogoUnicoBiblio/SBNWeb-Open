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
import it.finsiel.sbn.polo.oggetti.AutoreMarca;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.RepertorioMarca;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.oggetti.estensione.MarcaValida;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
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
 * Classe FondeMarca
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due marche
 *
 * Valida la richiesta:
 * . le due marchei idPartenza e idArrivo devono esistere in tb_marca.
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo gestito
 *  in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_mar con bid=spostaId e
 * mid=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_mar con bid=spostaId e mid=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_mar con mid= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze di
 * tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami tr_tit_mar legati a
 * idPartenza, si  verifica che il numero di legami non sia maggiore di un valore
 * massimo gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame tr_tit_mar: verifica esistenza tr_tit_mar con bid=bid e mid=idArrivo,
 * se esiste interrompe il servizio e segnala diagnostico 'legame già esistente'
 * comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami tr_tit_mar si spostano i legami tr_rep_mar e
 * tr_mar_aut da idPartenza a idArrivo (update di mid sui legami), controllando
 * preventivamente  che non esista già la stessa relazione con idArrivo, nel qual
 * caso non si esegue nessuna operazione.
 * Al termine dell'elaborazione sui legami si cancella la marca idPartenza e i suoi
 * legami ad eccezione dei legami tr_mar_bib.
 *
 */


public class FondeMarca extends FondeElementoAutFoctoring {
	private FondeType 		fondeType;
	private String 		_id_arrivo;
	private String 		_id_partenza;
	private String[] 		_spostaId;
	private SbnUserType 	_sbnUser;
	private int 			maxSpostamenti;
	private String 		_spostaIdAttuale;
	private Tb_marca		marcaFusa;
	private TimestampHash	_timeHash = new TimestampHash();

	public FondeMarca(SBNMarc input_root_object) {
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
            ecc.appendMessaggio("spostaId " + _spostaIdAttuale);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, _sbnUser);
            throw ecc;
        }
    }

	private void executeFonde() throws IllegalArgumentException, InvocationTargetException, Exception {
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007
// START LOCALIZZAZIONE MARCA
//		if (validator.isPolo(getCdUtente())){
//			MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
//			if (marcaBiblioteca.verificaEsistenzaTr_mar_bib(_id_partenza,_sbnUser.getBiblioteca().substring(0,3)) == null)
//				throw new EccezioneSbnDiagnostico(3210,"Marca non localizzata nel polo");
//		}
// END LOCALIZZA MARCA
		MarcaValida marcaValida =  new MarcaValida();
		marcaValida.validaPerFonde(getCdUtente(),_id_partenza, _id_arrivo,_timeHash);
		if (_spostaId.length > 0)
			spostamentoLegami();
		else fusion();
		Marca marca = new Marca();
		TableDao tavola;
		tavola = marca.cercaMarcaPerID(_id_arrivo);
        List temp;
		temp = tavola.getElencoRisultati();

		if (temp.size() >0)
			marcaFusa = (Tb_marca)temp.get(0);
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
		TitoloMarca titoloMarca = new TitoloMarca();
		TableDao tavola = titoloMarca.cercaLegamiMarcaTitolo(_id_partenza);
        List vettoreRisultati = tavola.getElencoRisultati();

		if (vettoreRisultati.size() > maxSpostamenti)
			throw new EccezioneSbnDiagnostico(3141,"Troppi identificativi comunicati: spezzare la richiesta in parti");
		Tr_tit_mar tr_tit_mar;
		for (int i=0; i<vettoreRisultati.size();i++){
			tr_tit_mar = (Tr_tit_mar) vettoreRisultati.get(i);
			_spostaIdAttuale = new String(tr_tit_mar.getBID());
			if (titoloMarca.estraiTitoloMarca(_id_arrivo,tr_tit_mar.getBID()) == null)
				titoloMarca.updateTitoliMarca(_id_partenza,_id_arrivo,getCdUtente(),_sbnUser,tr_tit_mar.getBID());
			else titoloMarca.cancellaLegameTitoloMarca(_id_partenza,tr_tit_mar.getBID(),getCdUtente());
		}
		RepertorioMarca repertorioMarca = new RepertorioMarca();
        if (repertorioMarca.contaTotaleLegami(_id_partenza,_id_arrivo)>3)
            throw new EccezioneSbnDiagnostico(3247,"Le citazioni totali sono più di tre");
		repertorioMarca.spostaLegami(_id_partenza,_id_arrivo,getCdUtente());
		AutoreMarca autoreMarca = new AutoreMarca();
		autoreMarca.spostaLegamiMarca(_id_partenza, _id_arrivo,getCdUtente());

		//alla fine cancello la marca con idPartenza (attenzione a non cancellare i tr_mar_bib)
		Tb_marca marcaDaCancellare = new Tb_marca();
        Marca marca = new Marca();
        marcaDaCancellare = marca.estraiMarcaPerID(_id_partenza);
		marca.cancellaMarca(marcaDaCancellare,getCdUtente(),_timeHash);

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
			TitoloMarca titoloMarca = new TitoloMarca();
			if (titoloMarca.estraiTitoloMarca(_id_partenza,_spostaId[i].toString()) == null)
				throw new EccezioneSbnDiagnostico(3030,"legame inesistente");
			if (titoloMarca.estraiTitoloMarca(_id_arrivo,_spostaId[i].toString()) != null)
				throw new EccezioneSbnDiagnostico(3029,"legame già esistente");
			titoloMarca.updateTitoliMarca(_id_partenza,_id_arrivo,getCdUtente(),_sbnUser,_spostaIdAttuale);
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
        datiResp.setT001(marcaFusa.getMID());
        SbnDatavar data = new SbnDatavar(marcaFusa.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(marcaFusa.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.MA);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_MARCA_1271;
    }
}
