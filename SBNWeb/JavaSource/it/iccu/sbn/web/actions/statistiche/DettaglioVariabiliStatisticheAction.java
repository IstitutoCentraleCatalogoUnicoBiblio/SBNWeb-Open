package it.iccu.sbn.web.actions.statistiche;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotechePoloVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.stampe.StampaDiffVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.statistiche.DettaglioVariabiliStatisticheForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.statistiche.StatisticheDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

public final class DettaglioVariabiliStatisticheAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static final String LISTA_BIBLIOTECHE = "sif.bib.export.unimarc";

	private static Logger log = Logger.getLogger(DettaglioVariabiliStatisticheAction.class);
	private CaricamentoCombo caricaCombo = new CaricamentoCombo();
	
	private StatisticheDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new StatisticheDelegate(FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}
	
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.blocco", "blocco");
		map.put("button.conferma", "conferma");
		map.put("button.indietro", "indietro");
		map.put("button.tutteLeBiblio", "tutteLeBiblio");
		map.put("button.cercabiblioteche", "listaSupportoBib");// metodi per cerca (lente)
		return map;
	}

	class ordinaCodiceAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaCodiceDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getCdTabella() != null)
				sa = gp.getCdTabella();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getCdTabella() != null)
				sb = gp.getCdTabella();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloAsc implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sa).compareTo(sb)); // Ascending

		} // end compare

	} // end class StringComparator

	class ordinaTitoloDec implements Comparator<CodiceConfigVO> {
		public final int compare(CodiceConfigVO a, CodiceConfigVO b) {

			boolean x = false;
			boolean y = false;
			CodiceConfigVO gp = a;
			String sa = "";
			if (gp.getDescrizione() != null)
				sa = gp.getDescrizione();
			else
				x = true;
			String sb = "";
			gp = b;
			if (gp.getDescrizione() != null)
				sb = gp.getDescrizione();
			else
				y = true;
			if (x && !y)
				return 1;
			if (!x && y)
				return -1;
			if (x && y)
				return 0;
			return ((sb).compareTo(sa)); // Ascending

		} // end compare

	} // end class StringComparator

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioVariabiliStatisticheForm myForm = (DettaglioVariabiliStatisticheForm) form;
		ActionMessages errors = new ActionMessages();

		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}
		try {

			DettaglioVariabiliStatisticheForm currentForm = (DettaglioVariabiliStatisticheForm) form;

			String codBib = (Navigation.getInstance(request).getUtente().getCodBib());
			String codPolo = Navigation.getInstance(request).getUtente().getCodPolo();
			String ticket = Navigation.getInstance(request).getUtente().getTicket();

			log.debug("unspecified()");

			List<BibliotecaVO> sifBiblio = (List<BibliotecaVO>) request.getAttribute(LISTA_BIBLIOTECHE);
			if (ValidazioneDati.isFilled(sifBiblio)) {
				myForm.setListaBib(sifBiblio);
				StringBuilder buf = new StringBuilder();
				Iterator<BibliotecaVO> i = sifBiblio.iterator();
				for (;;) {
					buf.append(i.next().getCod_bib());
					if (i.hasNext())
						buf.append(", ");
					else
						break;
				}

				currentForm.setElencoBiblio(buf.toString());

			}

			if (request.getAttribute("dettaglioVariabili") != null) {
				StatisticaVO statistica = (StatisticaVO)request.getAttribute("dettaglioVariabili");
				Navigation navigation = Navigation.getInstance(request); 
				navigation.setDescrizioneX("Dettaglio variabili di input alla query");
				navigation.setTesto("Dettaglio variabili");
				StatisticheDelegate delegate = getDelegate(request);
				List<DettVarStatisticaVO> listaVar = delegate.getDettVarStatistica(Integer.valueOf(statistica.getIdConfig()), ticket);
				if ( listaVar == null || (listaVar != null && listaVar.size() == 0)){
					if (statistica.getTipoQuery() != null && !statistica.getTipoQuery().equals("0")){
						errors.add("generico", new ActionMessage("error.statistiche.statisticaSelezionataNonEsistente"));
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}
				}
				myForm.setStatistica(statistica);

				if (myForm.getStatistica().getTipoQuery() != null && myForm.getStatistica().getTipoQuery().equals("0")){
					if (myForm.getStatistica().getElencoVariabili() != null && myForm.getStatistica().getElencoVariabili().size() > 0){
						myForm.getStatistica().getElencoVariabili().clear();
					}
					myForm.getStatistica().setElencoVariabili(null);
					this.conferma(mapping, currentForm, request, response);
					if (myForm.getStatistica().getTipoQuery() != null && myForm.getStatistica().getTipoQuery().equals("0")){
						request.setAttribute("disableConferma", "disableConferma");
						return Navigation.getInstance(request).goBack(true);
					}
				}else{
					myForm.getStatistica().getElencoVariabili().clear();
					DettVarStatisticaVO riga = new DettVarStatisticaVO();
					DettVarStatisticaVO rec = null;
					for (int index = 0; index < listaVar.size(); index++) {
						rec = listaVar.get(index);
						riga = new DettVarStatisticaVO();
						riga.setIdStatistica(rec.getIdStatistica());
						riga.setEtichettaNomeVar(rec.getEtichettaNomeVar());
						riga.setValore(rec.getValore());
						riga.setNomeVar(rec.getNomeVar());
						riga.setObbligatorio(rec.getObbligatorio());
						if (rec.getObbligatorio() != null && rec.getObbligatorio().equals("S")){
							myForm.setObbligatorio("S");
						}
						if (rec.getTipoVar() != null && rec.getTipoVar().equals("combo")){
							if (rec.getValore() != null){
								String  valori [] = rec.getValore().split("\\|");
								if (valori.length > 1){
									List<CodiceVO> codiciDescrizioni = null;
									if (valori[0].length() >= 4){ //sono stati splittati 4 campi
										//select dinamica da tabella generica con eventuale join su tb_codici
										//i parametri passati in questa query sono quelli presenti nella tabella
										//tb_stat_parameter, colonna valore per i tipo combo
										String valori4 = codBib;
										codiciDescrizioni = getListaCodDescrDaTabGenerica(codPolo, valori[0]/*codBib*/, valori[1]/*tabella*/,
												valori[2]/*codice*/, valori[3]/*descrizione*/, valori4/*valore codBib*/, ticket);
										codiciDescrizioni.add(0, new CodiceVO("", ""));
									}
									if (codiciDescrizioni == null || codiciDescrizioni.size() < 1){
										errors = new ActionMessages();
										errors.add("generico", new ActionMessage("error.documentofisico.listaVuota"));
										this.saveErrors(request, errors);
										return mapping.getInputForward();
									} else {
										riga.setListaCodiciVO((this.caricaCombo.loadCodiceDesc(codiciDescrizioni)));
									}
									riga.setValore("");
								}else if (valori.length == 1){
									//select da tabella codici
									CodiciType tipo = CodiciType.fromString(valori[0]);
									FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
									riga.setListaCodiciVO((caricaCombo.loadCodiceDesc(factory.getCodici().getCodici(tipo,true))));
									riga.setValore("");
								}

							}
						}else if (rec.getTipoVar() != null && rec.getTipoVar().equals("comboLibera")){
							if (rec.getValore() != null){
								String  valori [] = rec.getValore().split("\\,");
								if (valori.length > 0){
									List<CodiceVO> codiciDescrizioni = new ArrayList<CodiceVO>();
									for (int i = 0; i < valori.length; i++) {
										String val [] = valori[i].split("\\-");
										if (val.length > 0){
											codiciDescrizioni.add(0, new CodiceVO(val[0], val[1]));
										}
									}
									codiciDescrizioni.add(0, new CodiceVO("", ""));
									if (codiciDescrizioni == null || codiciDescrizioni.size() < 1){
										errors = new ActionMessages();
										errors.add("generico", new ActionMessage("error.documentofisico.listaVuota"));
										this.saveErrors(request, errors);
										return mapping.getInputForward();
									} else {
										riga.setListaCodiciVO((this.caricaCombo.loadCodiceDesc(codiciDescrizioni)));
									}
									riga.setValore("");
								}
							}
						}
//						if (myForm.getStatistica().getFlPoloBiblio() != null && !myForm.getStatistica().getFlPoloBiblio().equals("P")){
							if (ValidazioneDati.in(rec.getTipoVar(), "filtroListaBib", "filtroListaBibNoSplit") ){
								if (myForm.getListaBib() == null || myForm.getListaBib() != null && myForm.getListaBib().size() == 0){
									riga.setValore(codBib);
									myForm.setElencoBiblio(codBib);
								}
							if (riga.getValore() != null && (!riga.getValore().equals("") || riga.getValore().equals(codBib))){
								currentForm.setTastoCancBib(true);//attivo
							}else{
								currentForm.setTastoCancBib(false);//non attivo
							}
						}
						riga.setTipoVar(rec.getTipoVar());
						myForm.getStatistica().getElencoVariabili().add(riga);
					}
				}
			}else{
				DettVarStatisticaVO rec = null;
				if (myForm.getStatistica().getElencoVariabili() != null && 
						myForm.getStatistica().getElencoVariabili().size() > 0){
					for (int index = 0; index < myForm.getStatistica().getElencoVariabili().size(); index++) {
						rec = myForm.getStatistica().getElencoVariabili().get(index);
						if (rec.getEtichettaNomeVar() != null && ValidazioneDati.in(rec.getTipoVar(), "filtroListaBib", "filtroListaBibNoSplit")) {
							rec.setValore(myForm.getElencoBiblio());
							if (rec.getValore() != null && !rec.getValore().equals("")){
								currentForm.setTastoCancBib(true);//attivo
							}else{
								currentForm.setTastoCancBib(false);//non attivo
							}
						}
					}
				}
			}
		} catch (ValidationException ve) {
			errors.add("generico", new ActionMessage("error.statistiche."+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (DataException ve) {
			errors.add("generico", new ActionMessage("error.statistiche."+ ve.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			errors.add("generico", new ActionMessage("error.statistiche."+ e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();

	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DettaglioVariabiliStatisticheForm myForm = (DettaglioVariabiliStatisticheForm) form;
		ActionMessages errors = new ActionMessages();
		  long longData1 = 0;
		  long longData2 = 0;
		  Date data1 = null;
		  Date data2 = null;
		  
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
//			if (myForm.getStatistica().getFlPoloBiblio() != null && myForm.getStatistica().getFlPoloBiblio().equals("B")){
//				if (ValidazioneDati.size(myForm.getListaBib()) == 0) {
//					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.esporta.selezionare.bib"));
//					this.saveErrors(request, errors);
//					return mapping.getInputForward();
//				}
//			}

			if (myForm.getStatistica().getElencoVariabili() == null){
			}else{
				//scorro tabella di input e per ogni riga controllo se 'valore' � stato valorizzato
				// ed eseguo i controlli formali
				DettVarStatisticaVO riga = null;
				for (int index = 0; index < myForm.getStatistica().getElencoVariabili().size(); index++) {
					riga = myForm.getStatistica().getElencoVariabili().get(index);
					//data
					if (riga.getTipoVar().equals("data")){
						if (ValidazioneDati.isFilled(riga.getValore().trim())) {
							if (ValidazioneDati.validaData_1(riga.getValore().trim()) != ValidazioneDati.DATA_OK){
								myForm.setErrore(new String(riga.getEtichettaNomeVar()));
								throw new ValidationException(("dataErrata")) ;
							}
						}
						try {
							if (riga.getNomeVar().endsWith("D")){
								if (ValidazioneDati.isFilled(riga.getValore().trim())){
									data1 = simpleDateFormat.parse(riga.getValore().trim());
								}else{
									if (riga.getValore() != null){
										if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
											myForm.setErrore(new String(riga.getEtichettaNomeVar()));
											throw new ValidationException(("campoDataDaObbligatorio"));
										}
										riga.setValore(null);
										data1 = null;
									}
								}
							}else if (riga.getNomeVar().endsWith("A")){
								if (riga.getValore() != null && !riga.getValore().trim().equals("")){
									data2 = simpleDateFormat.parse(riga.getValore().trim());
								}else{
									//forzata data odierna quindi la data A non necessita controllo obbligatoriet� 
									Date dataOdierna = new Date();
									riga.setValore(DateUtil.formattaData(dataOdierna));
									data2 = simpleDateFormat.parse(riga.getValore().trim());
								}
							}
						} catch (ParseException e) {
							errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("error.documentofisico.erroreParse"));
							this.saveErrors(request, errors);
							resetToken(request);
							return mapping.getInputForward();
						}
					}
					//stringa
					if (riga.getTipoVar().equals("string")){
						if (riga.getValore() == null) {
							myForm.setErrore(new String(riga.getEtichettaNomeVar()));
							throw new ValidationException(("stringErrata"));
						}else if (riga.getValore().trim().equals("")){
							if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
								myForm.setErrore(new String(riga.getEtichettaNomeVar()));
								throw new ValidationException(("campoTestoObbligatorio"));
							}else{
								riga.setValore(null);//richiesta per trattare l'anno definendolo stringa
							}
						}
					}
					//int
					if (riga.getTipoVar().equals("int4")){
						if (riga.getValore() == null || (ValidazioneDati.strIsAlfabetic(riga.getValore().trim()))){
							myForm.setErrore(new String(riga.getEtichettaNomeVar()));
							throw new ValidationException(("intErrato"));
						}else{
							if (!riga.getValore().trim().equals("")){
								if (riga.getValore().trim().length()!= 4){
									myForm.setErrore(new String(riga.getEtichettaNomeVar()));
									throw new ValidationException(("lunghezzaInt4"));
								}
							}else{
								if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
									myForm.setErrore(new String(riga.getEtichettaNomeVar()));
									throw new ValidationException(("campoNumericoDaObbligatorio"));
								}
								riga.setValore(null);
							}
						}
					}
					//stringa combo
					if (riga.getTipoVar().equals("combo")){
						if (riga.getValore() == null) {
							myForm.setErrore(new String(riga.getEtichettaNomeVar()));
							throw new ValidationException(("stringaComboNull"));
						}else{
							if (riga.getValore() != null && riga.getValore().equals("")){
								if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
									myForm.setErrore(new String(riga.getEtichettaNomeVar()));
									throw new ValidationException(("campoComboObbligatorio"));
								}
							}
						}
					}

					//stringa combo
					if (riga.getTipoVar().equals("comboLibera")){
						if (riga.getValore() == null) {
							myForm.setErrore(new String(riga.getEtichettaNomeVar()));
							throw new ValidationException(("stringaComboLiberaNull"));
						}else{
							if (riga.getValore() != null && riga.getValore().equals("")){
								if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
									myForm.setErrore(new String(riga.getEtichettaNomeVar()));
									throw new ValidationException(("campoComboLiberaObbligatorio"));
								}
							}
						}
					}

					if (ValidazioneDati.in(riga.getTipoVar(), "filtroListaBib", "filtroListaBibNoSplit") ){
						if (myForm.getStatistica().getFlPoloBiblio() != null && myForm.getStatistica().getFlPoloBiblio().equals("P")){
							//livello polo, quindi potenzialmente tutte le biblio
							if (myForm.getElencoBiblio() != null){
								if (!myForm.getElencoBiblio().trim().equals("")){
									//non � stato selezionato nulla dalla lista bib
									riga.setValore(myForm.getElencoBiblio());
								}else{
									if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
										//errore: biblioteca obbligatorio
										myForm.setErrore(new String(riga.getEtichettaNomeVar()));
										throw new ValidationException(("campoBibliotecaObbligatorio"));
									}
								}
							}else{
								if (riga.getObbligatorio() != null && riga.getObbligatorio().equals("S")){
									//errore: biblioteca obbligatorio
									myForm.setErrore(new String(riga.getEtichettaNomeVar()));
									throw new ValidationException(("campoBibliotecaObbligatorio"));
								}
								riga.setValore(myForm.getElencoBiblio());
							}
						}else{
							//livello biblio
							//si passa quello che c'� in valore  
						}
				}
					

					//... un controllo per ogni filtroLista

					if (data1 != null && data2 != null){
						longData1 = data1.getTime();
						longData2 = data2.getTime();
						if ((longData2 - longData1) < 0) {
							errors = new ActionMessages();
							errors.add("Attenzione", new ActionMessage("error.documentofisico.dataDaAErrata"));
							this.saveErrors(request, errors);
							resetToken(request);
							return mapping.getInputForward();
						}
					}
				}
			}
				
			List<StatisticaVO> inputForStampeService = new ArrayList<StatisticaVO>();
			inputForStampeService.add(myForm.getStatistica());

			String tipoFormato="";

			request.setAttribute("DatiVo", inputForStampeService);
			String fileJrxml = null;
			request.setAttribute("TipoFormato", tipoFormato);

			if (myForm.getStatistica().getFlTxt() != null && myForm.getStatistica().getFlTxt().equals("S")){
				String[] nome_composto = myForm.getStatistica().getParExc().getQuery().trim().split("\\s+");
				String nomeConUnderscore="";
				for (int x=0; x< nome_composto.length; x++) {
					nomeConUnderscore=nomeConUnderscore+nome_composto[x]+"_";
				}
				myForm.getStatistica().setNomeStatisticaPerLog(nomeConUnderscore);

				fileJrxml = myForm.getStatistica().getNomeStatisticaPerLog().trim()+".csv";
			}else{
				fileJrxml = "statistiche"+".jrxml";
			}
//			String basePath=this.servlet.getServletContext().getRealPath(File.separator);

			String pathJrxml = null;
			//percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathDownload = StampeUtil.getBatchFilesPath();

			String basePath=this.servlet.getServletContext().getRealPath(File.separator);
			pathJrxml=basePath+File.separator+"jrxml"+File.separator+File.separator+ fileJrxml;
			String downloadPath = StampeUtil.getBatchFilesPath();
			log.info("download path: " + downloadPath);
			String downloadLinkPath = "/";

			
			//codice standard inserimento messaggio di richiesta stampa differita
			StampaDiffVO stam = new StampaDiffVO();
			stam.setBasePath(basePath);
			stam.setDownloadPath(downloadPath);
			stam.setDownloadLinkPath(downloadLinkPath);
			stam.setTipoStampa(tipoFormato);
			stam.setUser(Navigation.getInstance(request).getUtente().getUserId());
			stam.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			stam.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			stam.setParametri(inputForStampeService);
			stam.setTemplate(pathJrxml);
			stam.setDownload(pathDownload);
//			stam.setDownloadLinkPath("/");		
			stam.setCodAttivita(CodiciAttivita.getIstance().STATISTICHE);//momentaneo
			stam.setTicket(Navigation.getInstance(request).getUtente().getTicket());
			UtilityCastor util= new UtilityCastor();
			String dataCorr = util.getCurrentDate();
			stam.setData(dataCorr);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			String idMessaggio = null;
			stam.setTipoOperazione(myForm.getStatistica().getCodiceAttivita());///////simone
			idMessaggio = factory.getStampeOnline().elaboraStatistica(stam);

			if (idMessaggio != null) {
				idMessaggio = "ID Messaggio: "+ idMessaggio;//prelevo l'identificativo del messaggio da OutputStampaVO 
				errors.add("Avviso", new ActionMessage("errors.finestampa" , idMessaggio));
				this.saveErrors(request, errors);
			}else{
				errors.add("Attenzione", new ActionMessage("error.documentofisico.prenotStampaNonEffettuata"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
			myForm.setDisable(true);
		} catch (ValidationException ve) {
			errors.add("generico", new ActionMessage("error.statistiche."+ ve.getMessage(), myForm.getErrore()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			errors.add("generico", new ActionMessage("error.statistiche."+ e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (myForm.getStatistica().getTipoQuery() != null && myForm.getStatistica().getTipoQuery().equals("0")){
			return Navigation.getInstance(request).goBack(true);
		}else{
			return mapping.getInputForward();
		}
	}
	
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		
		if (idCheck.equals("STATISTICHE")) {
			boolean auth;
			try {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().STATISTICHE);
				auth = true;
			} catch (UtenteNotAuthorizedException e) {
				auth = false;
			} catch (RemoteException e) {
				log.error("", e);
				return false;
			}

			Navigation navi = Navigation.getInstance(request);
			UserVO utente = navi.getUtente();
			return (auth && utente.isRoot());
		}

		return true;
	}
	private List<CodiceVO> getListaCodDescrDaTabGenerica(String codPolo, String valori0/*codBib*/, String valori1/*tabella*/,
			 String  valori2/*codice*/,  String valori3/*descrizione*/, String valori4/*valore codBib*/, String ticket) throws Exception {
		List<CodiceVO> lista;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();		
		lista = factory.getStatistiche().getListaCodDescrDaTabGenerica(codPolo, valori0/*codBib*/, valori1/*tabella*/,
				  valori2/*codice*/, valori3/*descrizione*/, valori4/*valore codBib*/, ticket);
		return lista;

	}
	
	
	public List<ComboCodDescVO> loadComboBiblioteche() {
		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		ComboCodDescVO biblioteca = new ComboCodDescVO();
		biblioteca.setCodice("");
		biblioteca.setDescrizione("");
		lista.add(biblioteca);
		biblioteca = new ComboCodDescVO();
		biblioteca.setCodice("Tutte");
		biblioteca.setDescrizione("Tutte");
		lista.add(biblioteca);
		return lista;

	}

	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);

			UserVO utente = Navigation.getInstance(request).getUtente();
			SIFListaBibliotechePoloVO richiesta = new SIFListaBibliotechePoloVO(
					utente.getCodPolo(), utente.getCodBib(), true, Integer
							.valueOf(ConstantDefault.ELEMENTI_BLOCCHI
									.getDefault()), LISTA_BIBLIOTECHE);

			return biblio.getSIFListaBibliotechePolo(richiesta);
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.errore." + e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward tutteLeBiblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioVariabiliStatisticheForm currentForm = (DettaglioVariabiliStatisticheForm) form;
		
		DettVarStatisticaVO rec = null;
		for (int index = 0; index < currentForm.getStatistica().getElencoVariabili().size(); index++) {
			rec = currentForm.getStatistica().getElencoVariabili().get(index);
			if (rec.getEtichettaNomeVar() != null && ValidazioneDati.in(rec.getTipoVar(), "filtroListaBib", "filtroListaBibNoSplit") ){
				if (rec.getValore() != null && !rec.getValore().equals(""))
					rec.setValore(currentForm.getElencoBiblio());
				currentForm.setTastoCancBib(false);//non attivo
				rec.setValore("");
				currentForm.setListaBib(null);
				currentForm.setElencoBiblio("");
			}else{
				currentForm.setTastoCancBib(true);// attivo
			}
		}
		currentForm.setTastoCancBib(false);
		return mapping.getInputForward();
	}



}