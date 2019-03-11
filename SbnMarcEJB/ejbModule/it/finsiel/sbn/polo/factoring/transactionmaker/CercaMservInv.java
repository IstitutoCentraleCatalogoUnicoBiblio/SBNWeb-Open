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

import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.util.Errore;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.AskInv;
import it.iccu.sbn.ejb.model.unimarcmodel.Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.periodici.exception.PicosException;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contiene le funzionalità di ricerca per l'entità Abstract restituisce la
 * lista sintetica o analitica
 *
 * Possibili parametri di ricerca: Identificativo: T001 Descrizione Abs esatto:
 * stringa esatta Descrizione Abs parte iniziale: stringaLike
 *
 * Filtri di ricerca: livello di autorità: tipoAuthority intervallo di data di
 * aggiornamento: T005_Range
 *
 * Parametrizzazioni di output: tipoOrd: ordinamento richiesto, può essere su
 * identificativo , sulla descrizione, sul timestamp + identificativo tipoOut:
 * analitico o sintetico
 *
 * Passi da eseguire: esegue la ricerca secondo i parametri ricevuti; prepara
 * l'output secondo le indicazioni ricevute in analitica se il descrittore è di
 * rinvio si prepara l'esame della forma accettata se non ok ritorna il msg
 * response di diagnostica
 *
 * @author
 * @version 13/01/2007
 */
public class CercaMservInv extends MServDocFactoring {

	private static Log log = LogFactory.getLog("iccu.serversbnmarc.MservAskInv");
	protected int maxRighe;

	private String _Kbibl;
	private String _Kordi;

	// opzionali
	private String _kAnno;
	private String _tipoMat;
	private String _tipoCirc;
	private String _precis;
	private String _valore;
	private Boolean _flaColl;
	private String _consisDoc;
	private int _annoBill;
	private String _tipoPrezzo;
	private String _prezzo;
//	private String _Kinv;
	protected List<SchedaInventarioVO> inventarioVO;

	private String _msgErr = null;
	private String _kserie;

	public CercaMservInv(SBNMarc input_root_object) {
		super(input_root_object);
		AskInv _askInv;
		_askInv = input_root_object.getSbnMessage().getMServDoc().getAskinv();

		if (_askInv.getKbibl() != null)
			_Kbibl = _askInv.getKbibl();
		if (_askInv.getKordi() != null)
			_Kordi = _askInv.getKordi();

		if (_askInv.getKanno() != null)
			_kAnno = _askInv.getKanno();
		if (_askInv.getTipomat() != null)
			_tipoMat = _askInv.getTipomat();
		if (_askInv.getTipocirc() != null)
			_tipoCirc = _askInv.getTipocirc();
		if (_askInv.getPrecis() != null)
			_precis = _askInv.getPrecis();
		if (_askInv.getValore() != null)
			_valore = _askInv.getValore();

		if (_askInv.getFlacoll())
			_flaColl = true;
		else
			_flaColl = false;

		if (_askInv.getConsisDoc() != null)
			_consisDoc = _askInv.getConsisDoc();

		if (String.valueOf(_askInv.getAnnoBil()) != null)
			_annoBill = _askInv.getAnnoBil();
		if (_askInv.getTipoPrezzo() != null)
			_tipoPrezzo = _askInv.getTipoPrezzo();
		if (_askInv.getPrezzo() != null)
			_prezzo = _askInv.getPrezzo();

        //almaviva5_20100616 aggiunto cod serie
        if (ValidazioneDati.isFilled(_askInv.getKserie()) )
        	_kserie = _askInv.getKserie();
        else {
        	_kserie = ResourceLoader.getPropertyString("SERIE_SPAZIO");
        	log.warn("kserie non specificato, impostata serie a spazio");
        }

	}

	protected void executeCerca() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		int counter = 0;

		if (_Kbibl != null)
			counter++;
		if (_Kordi != null)
			counter++;
    	//almaviva5_20100616 aggiunto cod serie
    	if(_kserie != null)
    		counter ++;

		if (counter != 3)
			throw new EccezioneSbnDiagnostico(3039,
					"comunicare uno e un solo canale di ricerca");

		if ((_Kbibl != null) && (_Kordi != null))
			cercaAskInv(_Kbibl, _kserie, _Kordi, _kAnno, _tipoMat, _tipoCirc, _precis,
					_valore, _flaColl, _consisDoc, _annoBill, _tipoPrezzo,
					_prezzo);

	}

	/**
	 * Method cercaRepertorioPerMarca.
	 *
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaAskInv(String kbibl, String kserie, String kordi, String kanno,
			String tipomat, String tipocirc, String precis, String valore,
			Boolean flacoll, String consisDoc, int annoBil, String tipoPrezzo,
			String prezzoBil) throws IllegalArgumentException,
			InvocationTargetException, Exception {

		try {
			SbnUserType user = getCurrentUser();
	        cd_utente = user.getBiblioteca() + (user.getUserId() == null ? "      " : user.getUserId());

			inventarioVO = getPeriodici().askInv(kbibl, kserie, kordi, kanno, tipomat,
					tipocirc, precis, valore, flacoll, consisDoc, annoBil,
					tipoPrezzo, prezzoBil, cd_utente);

		} catch (PicosException e) {
			_msgErr = e.getMessage();
		}

	}

	public void eseguiTransazione() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		executeCerca();
	}

	protected SBNMarc preparaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		SBNMarc risultato = formattaOutput();
		object_response = risultato;
		return risultato;
	}

	private SBNMarc formattaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {

		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		MServDoc servDoc = new MServDoc();

		sbnmarc.setSbnUser(user_object);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setMServDoc(servDoc);
		sbnmarc.setSbnMessage(message);

		// errore picos da PeriodiciBean
		if (_msgErr != null) {
			servDoc.setMsgErr(_msgErr);
			return sbnmarc;
		}

		if (inventarioVO.size() > 0) {
			Inventario inventario = formattaInventario(inventarioVO.get(0));
			servDoc.setAnsinv(inventario);
		} else {
			Errore errore = Decodificatore.getErrore(3001); // Nessun Elemento trovato (3001)
			servDoc.setMsgErr(errore.getDescrizione() );
		}

		return sbnmarc;
	}

	/**
	 * Metodo per il controllo delle autorizzazioni
	 */
	public void verificaAbilitazioni() throws EccezioneAbilitazioni {
		if (!validator.verificaAttivitaDesc(getCdUtente(), "Cerca"))
			throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
	}

	public String getIdAttivitaSt() {
		return CodiciAttivita.getIstance().CERCA_DOCUMENTO_1002;
	}

}
