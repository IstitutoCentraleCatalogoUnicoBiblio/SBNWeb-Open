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
import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.util.Errore;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.Abbonamento;
import it.iccu.sbn.ejb.model.unimarcmodel.AnsDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.AskDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Abbonamento;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SchedaPeriodico;
import it.iccu.sbn.ejb.model.unimarcmodel.Sintesi;
import it.iccu.sbn.periodici.vo.SchedaAbbonamentoVO;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;
import it.iccu.sbn.periodici.vo.SchedaPeriodicoVO;
import it.iccu.sbn.periodici.vo.SchedaSintesiVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

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
public class CercaMservDoc extends MServDocFactoring {

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.MservAskDoc");

	RicTit4Inventario ricTit4Inventario;
	RicTit4Abbonamento ricTit4Abbonamento;
	private String _xdoc;
	private RicTit4Inventario _xinv;
	private RicTit4Abbonamento _xabb;
	private String _Kbibl;
	private String _Kinv;
	private String _Kordi;
	private String _Kanno;
	// Mantiene il resultSet che deve essere spacchettato
	protected TableDao tavola_response = null;
	protected List<SchedaPeriodicoVO> periodicoVO;
	protected boolean ricercaXdoc = false;

	private String _kserie;


	public CercaMservDoc(SBNMarc input_root_object) {
		super(input_root_object);

		AskDoc _askDoc;
		_askDoc = input_root_object.getSbnMessage().getMServDoc().getAskdoc();
		if (_askDoc.getXdoc() != null)
			_xdoc = _askDoc.getXdoc();

		if (_askDoc.getXinv() != null) {
			if (_askDoc.getXinv().getKinv() != null)
				_Kinv = _askDoc.getXinv().getKinv();
			if (_askDoc.getXinv().getKbibl() != null)
				_Kbibl = _askDoc.getXinv().getKbibl();
	        //almaviva5_20100616 aggiunto cod serie
	        if (ValidazioneDati.isFilled(_askDoc.getXinv().getKserie()) )
	        	_kserie = _askDoc.getXinv().getKserie();
	        else {
	        	_kserie = ResourceLoader.getPropertyString("SERIE_SPAZIO");
	        	log.warn("kserie non specificato, impostata serie a spazio");
	        }
		}

		if (_askDoc.getXabb() != null) {
			if (_askDoc.getXabb().getKbibl() != null)
				_Kbibl = _askDoc.getXabb().getKbibl();
			if (_askDoc.getXabb().getKordi() != null)
				_Kordi = _askDoc.getXabb().getKordi();
			if (_askDoc.getXabb().getKanno() != null)
				_Kanno = _askDoc.getXabb().getKanno();
		}

	}

	protected void executeCerca() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		int counter = 0;
		if (_xdoc != null)
			counter++;
		if ((_Kinv != null) && (_Kbibl != null) && (_kserie != null))
			counter++;
		if ((_Kbibl != null) && (_Kordi != null) && (_Kanno != null))
			counter++;
		if (counter != 1)
			throw new EccezioneSbnDiagnostico(3039,	"comunicare uno e un solo canale di ricerca");
		if (_xdoc != null)
			cercaXdoc();
		if ((_Kinv != null) && (_Kbibl != null) && (_kserie != null))
			cercaXinv(_Kinv, _Kbibl, _kserie);
		if ((_Kbibl != null) && (_Kordi != null) && (_Kanno != null))
			cercaXabb(_Kbibl, _Kordi, _Kanno);

	}

	private void cercaXdoc() throws EccezioneSbnDiagnostico, EccezioneDB,
			InfrastructureException {
		TitoloCerca titolo = new TitoloCerca();
		String id = Formattazione.formatta(_xdoc);
		tavola_response = titolo.cercaTitoloPerID(id);
		ricercaXdoc = true;
	}

	private void cercaXabb(String _Kbibl, String _Kordi, String _Kanno)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		periodicoVO = getPeriodici().xabb(_Kbibl, _Kordi, _Kanno);
	}

	private void cercaXinv(String _Kinv, String _Kbibl, String kserie)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		periodicoVO = getPeriodici().xinv(_Kbibl, _Kinv, kserie);
	}

	public String cercaNumeroPerBid(String bid)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		NumeroStd numeroStd = new NumeroStd();
		List v = numeroStd.cercaNumeroPerBid(bid);
		Tb_numero_std tb_numero_std;
		// DA VEDERE
		int size = v.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				tb_numero_std = (Tb_numero_std) v.get(i);
				if (tb_numero_std.getTP_NUMERO_STD().trim().equalsIgnoreCase("j")) {
					return tb_numero_std.getNUMERO_STD();
				}
			}
		} else {
			return "";
		}
		return "";
	}

	/**
	 * Method cercaRepertorioPerMarca.
	 *
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */

	public void eseguiTransazione() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		executeCerca();
	}

	protected SBNMarc preparaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		SBNMarc risultato = null;
		if (ricercaXdoc) {
			List response = tavola_response.getElencoRisultati();
			risultato = formattaOutput(response);
			object_response = risultato;
			return risultato;
		} else {
			risultato = formattaPeriodicoTot(periodicoVO);
			object_response = risultato;
			return risultato;
		}

	}

	private SBNMarc formattaOutput(List TableDao_response)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		MServDoc servDoc = new MServDoc();
		AnsDoc ansDoc = new AnsDoc();
		SchedaPeriodico schedaPeriodico = new SchedaPeriodico();
		List list = tavola_response.getResponse();
		if (ValidazioneDati.isFilled(list) ) {
			Tb_titolo tb_titolo = (Tb_titolo) TableDao_response.get(0);
			schedaPeriodico.setKnide(tb_titolo.getBID());
			if (tb_titolo.getBID_LINK() != null)
				schedaPeriodico.setBidsbn(tb_titolo.getBID_LINK());

			schedaPeriodico.setBidsbn(tb_titolo.getBID());
			schedaPeriodico.setNatura(tb_titolo.getCD_NATURA());
			String n_std = cercaNumeroPerBid(tb_titolo.getBID());
			schedaPeriodico.setIssn(n_std);
			schedaPeriodico.setLingua(tb_titolo.getCD_LINGUA_1());
			schedaPeriodico.setPaese(tb_titolo.getCD_PAESE());
			schedaPeriodico.setTipod(tb_titolo.getTP_AA_PUBB());

			//almaviva5_20150209 segnalazione CFI: la data pubb. può contenere caratteri non numerici
			String aa_pubb_1 = tb_titolo.getAA_PUBB_1();
			if (ValidazioneDati.isFilled(aa_pubb_1))
				schedaPeriodico.setAnno(aa_pubb_1);
			else
				schedaPeriodico.setAnno("0");

			String aa_pubb_2 = tb_titolo.getAA_PUBB_2();
			if (ValidazioneDati.isFilled(aa_pubb_2))
				schedaPeriodico.setAnno2(aa_pubb_2);
			else
				schedaPeriodico.setAnno2("0");

			schedaPeriodico.setGenere(tb_titolo.getCD_GENERE_1());
			schedaPeriodico.setPeriodic(tb_titolo.getCD_PERIODICITA());

			schedaPeriodico.setIsbd(tb_titolo.getISBD());
			schedaPeriodico.setCles(tb_titolo.getKY_CLES1_T() + tb_titolo.getKY_CLES2_T());

			ansDoc.addSchedaperiodico(schedaPeriodico);
			ansDoc.setSchedaperiodico(0, schedaPeriodico);
			servDoc.setAnsdoc(ansDoc);

		} else {
			// Nessun Elemento trovato
			Errore errore = Decodificatore.getErrore(3001);
			servDoc.setMsgErr(errore.getDescrizione());
		}

		sbnmarc.setSbnUser(user_object);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setMServDoc(servDoc);
		sbnmarc.setSbnMessage(message);

		return sbnmarc;
	}

	private SBNMarc formattaPeriodicoTot(
			List<SchedaPeriodicoVO> listaPeriodici)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		MServDoc servDoc = new MServDoc();
		AnsDoc ansDoc = new AnsDoc();
		SchedaPeriodico schedaPeriodico = new SchedaPeriodico();

		if (ValidazioneDati.isFilled(listaPeriodici) )
			for (SchedaPeriodicoVO p1 : listaPeriodici) {
				schedaPeriodico = formattaPeriodico(p1);

				List<SchedaInventarioVO> listaInventari = p1.getSchedaInventario();
				if (ValidazioneDati.isFilled(listaInventari) )
					for (SchedaInventarioVO i1 : listaInventari ) {
						Inventario inv1 = formattaInventario(i1);
						schedaPeriodico.addInventario(inv1);
					}


				List<SchedaSintesiVO> listaSintesi = p1.getSchedaSintesi();
				if (ValidazioneDati.isFilled(listaSintesi) )
					for (SchedaSintesiVO s1 : listaSintesi ) {
						Sintesi sint1 = formattaSintesi(s1);
						schedaPeriodico.addSintesi(sint1);
					}


				List<SchedaAbbonamentoVO> listaAbbonamenti = p1.getSchedaAbbonamento();
				if (ValidazioneDati.isFilled(listaAbbonamenti) )
					for (SchedaAbbonamentoVO a1 : listaAbbonamenti) {
						Abbonamento abb1 = formattaAbbonamento(a1);
						schedaPeriodico.addAbbonamento(abb1);
					}

				ansDoc.addSchedaperiodico(schedaPeriodico);
				//ansDoc.setSchedaperiodico(p, schedaPeriodico);

		} else {
			// Nessun Elemento trovato
			Errore errore = Decodificatore.getErrore(3001);
			servDoc.setMsgErr(errore.getDescrizione());
		}

		servDoc.setAnsdoc(ansDoc);
		sbnmarc.setSbnUser(user_object);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setMServDoc(servDoc);
		sbnmarc.setSbnMessage(message);
		return sbnmarc;
	}

	private SchedaPeriodico formattaPeriodico(SchedaPeriodicoVO periodicoVo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		SchedaPeriodico schedaPeriodico = new SchedaPeriodico();
		schedaPeriodico.setKnide(periodicoVo.getKnide());
		schedaPeriodico.setBidsbn(periodicoVo.getBidsbn());
		schedaPeriodico.setNatura(periodicoVo.getNatura());
		String n_std = cercaNumeroPerBid(periodicoVo.getKnide());
		schedaPeriodico.setIssn(n_std);
		schedaPeriodico.setLingua(periodicoVo.getLingua());
		schedaPeriodico.setPaese(periodicoVo.getPaese());
		schedaPeriodico.setTipod(periodicoVo.getTipod());

		if (periodicoVo.getAnno() != null)
			schedaPeriodico.setAnno(periodicoVo.getAnno());
		else
			schedaPeriodico.setAnno("0");

		if (periodicoVo.getAnno2() != null)
			schedaPeriodico.setAnno2(periodicoVo.getAnno2());
		else
			schedaPeriodico.setAnno2("0");

		schedaPeriodico.setGenere(periodicoVo.getGenere());
		schedaPeriodico.setPeriodic(periodicoVo.getPeriodic());
		schedaPeriodico.setIsbd(periodicoVo.getIsbd());
		schedaPeriodico.setCles(periodicoVo.getCles());

		return schedaPeriodico;
	}

	private Sintesi formattaSintesi(SchedaSintesiVO sintesiVo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Sintesi sintesi = new Sintesi();
		sintesi.setKbibl(sintesiVo.getKbibl());
		sintesi.setKsint(sintesiVo.getKsint());
		sintesi.setSegnatura(sintesiVo.getSegnatura());
		sintesi.setDescr(sintesiVo.getDescr());

		return sintesi;
	}

	private Abbonamento formattaAbbonamento(SchedaAbbonamentoVO abbonamentoVo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Abbonamento abbonamento = new Abbonamento();
		abbonamento.setKbibl(abbonamentoVo.getKbibl());
		abbonamento.setKordi(abbonamentoVo.getKordi());
		abbonamento.setKanno(abbonamentoVo.getKanno());
		abbonamento.setKforn(abbonamentoVo.getKforn());
		abbonamento.setLivello(abbonamentoVo.getLivello());
		return abbonamento;
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
