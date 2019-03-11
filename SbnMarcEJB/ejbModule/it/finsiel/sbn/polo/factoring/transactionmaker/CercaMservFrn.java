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
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.Ana;
import it.iccu.sbn.ejb.model.unimarcmodel.AnagraficheFrn;
import it.iccu.sbn.ejb.model.unimarcmodel.AskFrn;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiAnaType;
import it.iccu.sbn.ejb.model.unimarcmodel.Frn;
import it.iccu.sbn.ejb.model.unimarcmodel.IndirType;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.OperazioneType;
import it.iccu.sbn.periodici.exception.PicosException;
import it.iccu.sbn.periodici.vo.PicosAskFrnOperazione;
import it.iccu.sbn.periodici.vo.SchedaFornitoreVO;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.types.Date;

public class CercaMservFrn extends MServDocFactoring {

	private static Log log = LogFactory.getLog("iccu.serversbnmarc.CercaMservFrn");
	protected int maxRighe;

	protected List<SchedaFornitoreVO> listaFornitori;

	private String _msgErr = null;

	private String _kbibl;
	private Date _dataInizio;
	private Date _dataFine;
	private OperazioneType _operazione;
	private String[] _tipoFrn;

	public CercaMservFrn(SBNMarc input_root_object) {
		super(input_root_object);

		AskFrn _askFrn = input_root_object.getSbnMessage().getMServDoc().getAskfrn();

		if (_askFrn.getKbibl() != null)
			_kbibl = _askFrn.getKbibl();
		if (_askFrn.getDataInizio() != null)
			_dataInizio = _askFrn.getDataInizio();
		if (_askFrn.getDataFine() != null)
			_dataFine = _askFrn.getDataFine();
		if (_askFrn.getOperazione() != null)
			_operazione = _askFrn.getOperazione();
		if (_askFrn.getTipoFrn() != null)
			_tipoFrn = _askFrn.getTipoFrn();
	}

	protected void executeCerca() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		int counter = 0;

		if (_dataInizio != null)
			counter++;
		if (_dataFine != null)
			counter++;
		if (_operazione != null)
			counter++;

		if (counter < 2)
			throw new EccezioneSbnDiagnostico(3039,
					"comunicare uno e un solo canale di ricerca");
		cercaAskFrn(_dataInizio, _dataFine, _operazione, _tipoFrn);

	}

	private void cercaAskFrn(Date inizio, Date fine,
			OperazioneType tipoOp, String[] frn) throws IllegalArgumentException,
			InvocationTargetException, Exception {

		try {
			String _kpolo = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date from = df.parse(_dataInizio.toString() );

			//imposto la data fine intervallo all'orario 23:59:59
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(df.parse(_dataFine.toString() ));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			java.util.Date to = calendar.getTime();

			if (from.after(to)) {
				Errore errore = Decodificatore.getErrore(3323); // errore intervallo date (3323)
				_msgErr = errore.getDescrizione();
				return;
			}

			PicosAskFrnOperazione op = PicosAskFrnOperazione.T;
			if (tipoOp == OperazioneType.M)
				op = PicosAskFrnOperazione.M;
			if (tipoOp == OperazioneType.C)
				op = PicosAskFrnOperazione.C;

			if (!ValidazioneDati.isFilled(_kbibl)) {
				_kbibl = null;
				_kpolo = null;
			}	else
				_kpolo = user_object.getBiblioteca().substring(0, 3);

			if (!ValidazioneDati.isFilled(_tipoFrn))
				_tipoFrn = null;

			listaFornitori = getPeriodici().askFrn(_kpolo, _kbibl, from, to, op, _tipoFrn);

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

		if (listaFornitori.size() > 0) {
			AnagraficheFrn fornitori = formattaFornitore(listaFornitori);
			servDoc.setAnagrafiche(fornitori);
		} else {
			Errore errore = Decodificatore.getErrore(3001); // Nessun Elemento trovato (3001)
			servDoc.setMsgErr(errore.getDescrizione() );
		}

		return sbnmarc;
	}

	private AnagraficheFrn formattaFornitore(List<SchedaFornitoreVO> listaFrn)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		AnagraficheFrn anaFrn = new AnagraficheFrn();
		Ana ana = new Ana();
		anaFrn.setAna(ana);

		for (SchedaFornitoreVO f : listaFrn) {
			Frn frn = new Frn();
			frn.setCdFrn(ValidazioneDati.fillLeft(f.getCodFornitore(), '0', 9));
			frn.setCdTpFrn(f.getTipoPartner());
			frn.setCFPiva(f.getPartitaIva());
			frn.setEmail(f.getEmail());
			frn.setNote(f.getNote());

			frn.setDatiAna(formattaDatiAna(f));
			frn.setIndirizzo(formattaIndirizzo(f));

			ana.addFrn(frn);
		}

		return anaFrn;
	}

	private DatiAnaType formattaDatiAna(SchedaFornitoreVO f) {

		DatiAnaType ana = new DatiAnaType();
		ana.setNome(f.getNomeFornitore());
		ana.setTpPersona("G");//String.valueOf(f.getTipo_partner()));
		ana.setTpRecPft("1");
		return ana;
	}

	private IndirType formattaIndirizzo(SchedaFornitoreVO f) {

		IndirType ind = new IndirType();
		ind.setCap(f.getCap());
		ind.setCdPaese(f.getPaese());
		ind.setCitta(f.getCitta());
		ind.setFax(f.getFax());
		ind.setSiglaProv(f.getProvincia());
		ind.setTel(f.getTelefono());
		ind.setVia1(f.getIndirizzo());
		return ind;
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
