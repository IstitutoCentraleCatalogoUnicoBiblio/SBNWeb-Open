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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoValida;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
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
 * Classe FondeLuogo
 * Gestisce la fusione o lo spostamento dei legami con titoli tra due luoghi
 *
 * Valida la richiesta:
 * . i due luoghi dPartenza e idArrivo devono esistere in tb_luogo, e devono
 * essere in forma accettata.
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_luo con bid=spostaId e
 * lid=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_luo con bid=spostaId e lid=idArrivo, se esiste
 * interrompe il servizio e segnala diagnostico 'legame già esistente' comunicando
 * spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_luo con vid= idArrivo, modifica
 * tb_titolo.ts_var e ute_var, aggiorna fl_allinea = 'S' per tutte le occorrenze
 * di tr_tit_bib (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami tr_tit_luo legati
 * a idPartenza, si  verifica che il numero di legami non sia maggiore di un
 * valore massimo gestito in un property di sistema, altrimenti restituisce un
 * diagnostico: 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame tr_tit_luo: verifica esistenza tr_tit_luo con bid=bid e
 * lid=idArrivo, se esiste interrompe il servizio e segnala diagnostico 'legame
 * già esistente' comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami tr_tit_luo si spostano i legami tr_luo_luo
 * da idPartenza a idArrivo (update di lid sui legami) verificanco che non ci
 * siano già su idArrivo nel qual caso non si esegue nessuna operazione.
 * Al termine dell'elaborazione sui legami si cancella il luogo idPartenza.
 */
public class FondeLuogo extends FondeElementoAutFoctoring {
	private FondeType 		fondeType;
	private String 		_id_arrivo;
	private String 		_id_partenza;
	private String[] 		_spostaId;
	private SbnUserType 	_sbnUser;
	private int 			maxSpostamenti;

	private String _spostaIdAttuale;

	private Tb_luogo luogoFuso;

   public FondeLuogo(SBNMarc input_root_object){
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
            if (_spostaIdAttuale != null)
                ecc.appendMessaggio("spostaId " + _spostaIdAttuale);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, _sbnUser);
            throw ecc;
        }
    }

	private void executeFonde() throws IllegalArgumentException, InvocationTargetException, Exception {
		LuogoValida luogoValida =  new LuogoValida();
		luogoValida.validaPerFonde(getCdUtente(), _id_partenza, _id_arrivo);
		if (_spostaId.length > 0)
			spostamentoLegami();
		else fusion();
		LuogoLuogo luogoLuogo = new LuogoLuogo();
		luogoLuogo.spostaLegami(_id_partenza,_id_arrivo,getCdUtente());
		Luogo luogo = new Luogo();
		luogoFuso = luogo.cercaLuogoPerID(_id_arrivo);
		object_response = formattaOutput();
	}

	/**
	 * Method fusion.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void fusion() throws IllegalArgumentException, InvocationTargetException, Exception {
		TitoloLuogo titoloLuogo = new TitoloLuogo();
		TableDao tavola = titoloLuogo.cercaLegamiLuogoTitolo(_id_partenza);
        List vettoreRisultati = tavola.getElencoRisultati();

		if (vettoreRisultati.size() > maxSpostamenti)
			throw new EccezioneSbnDiagnostico(3141,"Troppi identificativi comunicati: spezzare la richiesta in parti");
		Tr_tit_luo tr_tit_luo;
		for (int i=0; i<vettoreRisultati.size();i++){
			tr_tit_luo = (Tr_tit_luo) vettoreRisultati.get(i);
			_spostaIdAttuale = tr_tit_luo.getBID();
			titoloLuogo.cancellaLegameTitoloLuogo(_id_partenza,tr_tit_luo.getBID(),getCdUtente());
            tr_tit_luo.setLID(_id_arrivo);
            titoloLuogo.inserisci(tr_tit_luo);
		}
		//alla fine cancello l'autore idPartenza (attenzione a non cancellare i tr_aut_bib)
		Tb_luogo luogoDaCancellare = new Tb_luogo();
        Luogo luogo = new Luogo();
        luogoDaCancellare = luogo.cercaLuogoPerID(_id_partenza);
		TimestampHash timeHash = new TimestampHash();
		timeHash.putTimestamp("Tb_luogo", luogoDaCancellare.getLID(),
				new SbnDatavar( luogoDaCancellare.getTS_VAR()));
		luogo.cancellaLuogo(luogoDaCancellare,getCdUtente(),timeHash);

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
			TitoloLuogo titoloLuogo = new TitoloLuogo();
			if (titoloLuogo.estraiTitoloLuogo(_spostaId[i].toString(),_id_partenza,null) == null)
				throw new EccezioneSbnDiagnostico(3030,"legame inesistente");
			if (titoloLuogo.estraiTitoloLuogo(_spostaId[i].toString(),_id_arrivo,null) != null)
				throw new EccezioneSbnDiagnostico(3029,"legame già esistente");
			titoloLuogo.updateTitoliLuogo(_id_partenza,_id_arrivo,getCdUtente(),_sbnUser,_spostaIdAttuale);
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
        datiResp.setT001(luogoFuso.getLID());
        SbnDatavar data = new SbnDatavar(luogoFuso.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(luogoFuso.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.LU);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_LUOGO_1274;
    }

}
