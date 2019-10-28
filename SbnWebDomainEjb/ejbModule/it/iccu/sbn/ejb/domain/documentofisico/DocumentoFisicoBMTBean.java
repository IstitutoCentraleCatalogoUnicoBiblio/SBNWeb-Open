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
package it.iccu.sbn.ejb.domain.documentofisico;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale;
import it.iccu.sbn.ejb.domain.servizi.ServiziCommon;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareListaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ScaricoInventarialeVO;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionestampe.BidInventarioSegnaturaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.EtichettaDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.SchedaVO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_esemplare_titoloDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_sezione_collocazioneDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.periodici.PeriodiciDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.Constants.DocFisico;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.validation.ValidazioniDocumentoFisico;
import it.iccu.sbn.vo.custom.esporta.QueryData;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="DocumentoFisicoBMT" description="A session bean named
 *           DocumentoFisicoBMT" display-name="DocumentoFisicoBMT"
 *           jndi-name="sbnWeb/DocumentoFisicoBMT" type="Stateless"
 *           transaction-type="Bean" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class DocumentoFisicoBMTBean extends TicketChecker implements DocumentoFisicoBMT {

	/**
	 *
	 */
	private static final long serialVersionUID = 7915309276263638347L;

	private static Logger log = Logger.getLogger(DocumentoFisicoBMT.class);

	private Tbc_inventarioDao daoInv;
	private Tbc_collocazioneDao daoColl;
	private Tbc_esemplare_titoloDao daoEsempl;
	private Tbc_sezione_collocazioneDao daoSez;
	private DocumentoFisicoCommon dfCommon;
	private Tbf_bibliotecaDao daoBiblio;
	private ServiziCommon servizi;

	Connection connection;

	private Inventario inventario;
	private Collocazione collocazione;
	private ValidazioniDocumentoFisico valida;
	private SessionContext context;

//	private Tbc_serie_inventarialeDao daoSerie;
//	private Trc_formati_sezioniDao daoForSez;

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext context) throws EJBException,
	RemoteException {
		this.context = context;
		try {
			daoInv = new Tbc_inventarioDao();
			daoColl = new Tbc_collocazioneDao();
			daoEsempl = new Tbc_esemplare_titoloDao();
			daoSez = new Tbc_sezione_collocazioneDao();
			valida = new ValidazioniDocumentoFisico();
			daoBiblio = new Tbf_bibliotecaDao();

			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			this.inventario = factory.getInventario();
			this.collocazione = factory.getCollocazione();
			dfCommon = factory.getDocumentoFisicoCommon();
			servizi = factory.getServiziCommon();

		} catch (NamingException e) {
			log.error("", e);
		} catch (CreateException e) {
			log.error("", e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public boolean getFusioneTitoli(String bidOld, String bidNew, String firmaUtente)
			throws DataException, ApplicationException, ValidationException {

		boolean ok = false;
		Timestamp ts = DaoManager.now();
		Integer esemplDocMax = 0;
		Map<Tbf_biblioteca_in_polo, Integer> bibCdDoc = new HashMap<Tbf_biblioteca_in_polo, Integer>();
		UserTransaction tx = context.getUserTransaction();

		try {
			InventarioVO inv = new InventarioVO();
			inv.setBid(bidOld);
			valida.validaBid(inv);
			InventarioVO inv1 = new InventarioVO();
			inv1.setBid(bidNew);
			valida.validaBid(inv1);

			DaoManager.begin(tx);

			//inventari
			daoInv = new Tbc_inventarioDao();
			List<Tbc_inventario> listaInv = daoInv.getListaInventari(bidOld);
			if (ValidazioneDati.isFilled(listaInv) ) {
				for (Tbc_inventario inventario : listaInv) {
					inventario.setTs_var(ts);
					Tb_titolo titolo = new Tb_titolo();
					titolo.setBid(bidNew);
					inventario.setB(titolo);
					inventario.setUte_var(firmaUtente);
					if (!daoInv.modificaInventario(inventario)) {
						ok = false;
						throw new DataException("erroreUpdateInventario");
					}
				}
			}

			// collocazioni
			daoColl = new Tbc_collocazioneDao();
			List<Tbc_collocazione> listaColl = daoColl.getListaCollocazioni(bidOld);
			if (ValidazioneDati.isFilled(listaColl) ) {
				for (Tbc_collocazione collocazione : listaColl) {
					collocazione.setTs_var(ts);
					Tb_titolo titolo = new Tb_titolo();
					titolo.setBid(bidNew);
					collocazione.setB(titolo);
					collocazione.setUte_var(firmaUtente);
					if (!daoColl.modificaCollocazione(collocazione)) {
						ok = false;
						throw new DataException("erroreUpdateCollocazione");
					}
				}
			}

			// esemplare
			daoEsempl = new Tbc_esemplare_titoloDao();
			List<Tbc_esemplare_titolo> listaEsempl = daoEsempl.getListaEsemplari(bidOld);
			if (ValidazioneDati.isFilled(listaEsempl) ) {
				for (Tbc_esemplare_titolo esemplare : listaEsempl) {
					// cancellare l'esemplare con il bidOld
					esemplare.setUte_var(firmaUtente);
					esemplare.setTs_var(ts);
					esemplare.setFl_canc('S');
					if (!daoEsempl.modificaEsemplare(esemplare)) {
						ok = false;
						throw new DataException("erroreDeleteEsemplare");
					}
					// verificare che non esista già un doc con lo stesso
					// progressivo per il nuovo titolo
					Tbf_biblioteca_in_polo bib = esemplare.getCd_polo();
					Tbc_esemplare_titolo esemplDoc = daoEsempl.getEsemplare(
							bib.getCd_polo().getCd_polo(),
							bib.getCd_biblioteca(), bidNew,
							esemplare.getCd_doc());
					if (esemplDoc != null) {
						Integer esemplareCodDocMax = daoEsempl.getEsemplare(bib.getCd_polo().getCd_polo(),
								bib.getCd_biblioteca(),
								bidNew);
						//progressivo da max (se esistente), oppure 0
						esemplDocMax = esemplareCodDocMax != null ? esemplareCodDocMax : ValidazioneDati.coalesce(bibCdDoc.get(bib), 0);
					} else {
						// primo esemplare su nuovo bid
						esemplDocMax = ValidazioneDati.coalesce(bibCdDoc.get(bib), 0);
					}
					bibCdDoc.put(bib, ++esemplDocMax);
					// inserire un nuovo esemplare con il bidNew
					Tbc_esemplare_titolo nuovoEsemplare = new Tbc_esemplare_titolo();
					nuovoEsemplare.setCd_polo(bib);
					nuovoEsemplare.setCd_doc(esemplDocMax);
					Tb_titolo titolo = new Tb_titolo();
					titolo.setBid(bidNew);
					nuovoEsemplare.setB(titolo);
					nuovoEsemplare.setTbc_collocazione(null);
					nuovoEsemplare.setCons_doc(esemplare.getCons_doc());
					nuovoEsemplare.setTs_ins(ts);
					nuovoEsemplare.setTs_var(ts);
					nuovoEsemplare.setUte_ins(firmaUtente);
					nuovoEsemplare.setUte_var(firmaUtente);
					nuovoEsemplare.setFl_canc('N');
					if (!daoEsempl.modificaEsemplare(nuovoEsemplare)) {
						ok = false;
						throw new DataException("erroreUpdateEsemplare");
					}
					// deve aggiornare il bid_loc su tbc_collocazione
					daoColl = new Tbc_collocazioneDao();
					List<Tbc_collocazione> listaCollDoc = daoColl.getListaCollocDoc(
							bib.getCd_polo().getCd_polo(),
							bib.getCd_biblioteca(), bidOld,
							esemplare.getCd_doc());
					for (Tbc_collocazione collDoc : listaCollDoc) {
						collDoc.setB(titolo);
						collDoc.setCd_biblioteca_doc(nuovoEsemplare);
						collDoc.setUte_var(firmaUtente);
						collDoc.setTs_var(ts);
						if (!daoColl.modificaCollocazione(collDoc)) {
							ok = false;
							throw new DataException("erroreUpdateCollocazione");
						}
					}
				}
			}

			ok = true;
			return ok;

		} catch (ValidationException e) {
			throw e;

		} catch (DaoManagerException e) {
			throw new DataException(e);

		} catch (DataException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
			throw new DataException(e);

		} finally {
			DaoManager.endTransaction(tx, ok);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public ScaricoInventarialeVO getScaricoInventariale(ScaricoInventarialeVO input,  BatchLogWriter blw)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		ScaricoInventarialeVO output = new ScaricoInventarialeVO(input);
		ScrollableResults listaInv = null;
		int numRecScaricati = 0;
		int numInvMinMax = -1;
		int numRecTrattati = 0;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		UserTransaction tx = context.getUserTransaction();
		 Logger logger = blw.getLogger();
		try{
			DaoManager.begin(tx);
			logger.debug("Inizio transazione scarico");
			this.scriviLogScarInv(input, output, numRecScaricati);
			output = this.controllaInputScarInv(input, output);

			if (input.getTipoOperazione().equals("R")){
				//trattamento campo inventario Da
				//trattamento campo inventario A
				//non ho il codice inventario impostato quindi cerco il numero massimo
				if (input.getEndInventario().equals("") || input.getEndInventario().trim().equals("0")){
					daoInv = new Tbc_inventarioDao();
					numInvMinMax = daoInv.getMaxInventario(input.getCodPolo(), input.getCodBib(), input.getSerie(), ts.toString());
					if (numInvMinMax > 0){
						input.setEndInventario(String.valueOf(numInvMinMax));
					}else{
						throw new DataException("erroreNumInvMinMax");
					}
				}
				Object obj = input;
				daoInv = new Tbc_inventarioDao();
				listaInv = daoInv.getListaInventariScrollableDaADaScaricare(input.getCodPolo(), input.getCodBib(),
						input.getSerie(), input.getStartInventario(), input.getEndInventario());
				List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
				DaoManager.commit(tx);
				//trattamento con ciclo
				numRecScaricati = trattamentoScaricaInv(input, output, listaInventari, numRecScaricati, logger);
				output.getErrori().add("Numero record scaricati: " + (numRecScaricati));
			}else if (input.getTipoOperazione().equals("N")){
				List listaInventariInput = input.getListaInventari();
				if (listaInventariInput != null){
					numRecTrattati = listaInventariInput.size();
					daoInv = new Tbc_inventarioDao();
					if (listaInventariInput.size() > 0){
						for (int y=0; y<listaInventariInput.size(); y++) {
							CodiceVO elem = (CodiceVO) listaInventariInput.get(y);
							Tbc_inventario inventario = daoInv.getInventarioDaScaricare(input.getCodPolo(), input.getCodBib(),
									elem.getCodice(), Integer.parseInt(elem.getDescrizione()));
							if (inventario != null){
								// controllo se è in prestito
								// richiama un ejb di servizi che conta i movimenti aperti
								int movA = servizi.getNumeroMovimentiAttivi(inventario.getCd_serie()
										.getCd_polo().getCd_polo().getCd_polo(), inventario
										.getCd_serie().getCd_polo().getCd_biblioteca(),
										inventario.getCd_serie().getCd_serie(), inventario
										.getCd_inven());
								if (movA > 0) {
									// inventario in prestito
									output.getErrori().add("Inventario '" + inventario.getCd_serie().getCd_serie() + inventario
											.getCd_inven() + "' con movimenti aperti non scaricato");
									continue;
								}
								//almaviva5_20101026 cancellazione legami a fascicoli (solo se natura='S')
								String uteIns = input.getCodPolo()+input.getCodBib()+input.getUser();
								if (ValidazioneDati.equals(inventario.getB().getCd_natura(), 'S')) {
									PeriodiciDAO pDao = new PeriodiciDAO();
									pDao.deleteLegamiEsemplariFascicolo(inventario, uteIns);
								}
								//
								//trattamento senza ciclo
								numRecScaricati = trattamentoScaricaInventario(input, output, inventario, numRecScaricati);
							}
//							else{
//								output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e inventario " + elem.getCodice() + elem.getDescrizione() + " inesistente o già scaricato");
//							}
						}
						output.getErrori().add("Numero record scaricati: " + (numRecScaricati));
						output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
						output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
					}else{
						output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e lista inventari vuota");
					}
				}else{
					output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e lista inventari non presente");
				}
			}
			DaoManager.commit(tx);

		} catch (Exception e) {
			output.getErrori().add("Numero record scaricati: " + (numRecScaricati));
			output.setStato(ConstantsJMS.STATO_ERROR);
			output.getErrori().add("ECCEZIONE = " + e);
			try {
				DaoManager.rollback(tx);
				logger.error("Rollback transazione");
				return output;
			} catch (Exception e1) {
				logger.error(e1);
			}
			return output;
		}
		return output;
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public SpostamentoCollocazioniVO getSpostamentoCollocazioni(SpostamentoCollocazioniVO input,  BatchLogWriter blw)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		SpostamentoCollocazioniVO output = new SpostamentoCollocazioniVO(input);
		ScrollableResults listaInv = null;
		int numRecSpostati = 0;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		UserTransaction tx = context.getUserTransaction();
		 Logger logger = blw.getLogger();
		try{
			DaoManager.begin(tx);
			logger.debug("Inizio transazione spostamento");
			this.scriviLogSpostColl(input, output, numRecSpostati);
			output = this.controllaInputSpostColl(input, output);
			logger.debug("Inizio validazione input");
			blw.logWriteCollection(input.getErrori());
			Object obj = input;
				if (input.getTipoOperazione().equals("R")){
					daoInv = new Tbc_inventarioDao();
					listaInv = daoInv.getListaInventariScrollableDaASpostColl(input.getCodPolo(), input.getCodBib(),
						input.getSerie(), input.getStartInventario(), input.getEndInventario());
					List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
					DaoManager.commit(tx);
					numRecSpostati = this.scorriListaInventariPerSpostamentoCollocazioni(input, output, listaInventari, numRecSpostati, ts, logger);
				}else{
					if (input.getAzione() != null){
						//tipoOperazione = "S"
						//manca implementazione di collocazione a formato
						ScrollableResults listaCollocazioniSezP = null;
						if (input.getSezioneP() != null){
//							CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(input.getSezioneP().getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
//									false, input.getDallaSpecificazione(), input.getAllaSpecificazione(), false, "");
							CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(input.getSezioneP().getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
									 input.getDallaSpecificazione(), input.getAllaSpecificazione(),  "");
							if (collSpec != null){
//								if (collSpec.getDaColl().compareTo(collSpec.getAColl()) <= 0){ //bug 0004699 esercizio rp
									if ( (collSpec.getDaColl() + collSpec.getDaSpec()).compareTo( (collSpec.getAColl() + collSpec.getASpec()) ) <= 0) {
										// la prima minore uguale della seconda
//									if (collSpec.getDaSpec().compareTo(collSpec.getASpec()) <= 0){//bug 0004699 esercizio rp
										daoInv = new Tbc_inventarioDao();
										listaInv = daoInv.getListaInventariScrollable(input.getCodPoloSez(),input.getCodBibSez(),
												input.getSezione().trim(), collSpec.getDaColl(), collSpec.getAColl(), false, collSpec.getDaSpec(), collSpec.getASpec(), false, 0, null, 0);
										List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
										DaoManager.commit(tx);
										numRecSpostati = this.scorriListaInventariPerSpostamentoCollocazioni(input, output, listaInventari, numRecSpostati, ts, logger);
//								}else{//bug 0004699 esercizio rp
//										output.getErrori().add("ERRORE: tipo operazione S e dalla specificazione > di alla specificazione");
//									}
								}else{
									output.getErrori().add("ERRORE: tipo operazione S e dalla collocazione > di alla collocazione");
								}
							}else{
								output.getErrori().add("ERRORE: tipo operazione S errore in fase di normalizzazione di collocazioni e specificazioni");
							}
						}else{
							listaCollocazioniSezP.close();
							output.getErrori().add("ERRORE: sezione di partenza non presente sul DB");
						}
					}else{
						output.getErrori().add("Azione non impostata");
					}
				}
				output.getErrori().add("Numero record spostati: " + (numRecSpostati));


				DaoManager.commit(tx);

			return output;
		} catch (Exception e) {
			output.getErrori().add("Numero record spostati: " + (numRecSpostati));
			output.setStato(ConstantsJMS.STATO_ERROR);
			output.getErrori().add("ECCEZIONE = " + e);
			try {
				DaoManager.rollback(tx);
				logger.error("Rollback transazione");
				return output;
			} catch (Exception e1) {
				logger.error("", e1);
			}
			return output;
		}
}

	private void scriviLogSpostColl(SpostamentoCollocazioniVO input,
			SpostamentoCollocazioniVO output, int numRecSpostati) {
		output.getErrori().add("Utente = " + input.getUser() + "Data e Ora di inizio = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		output.getErrori().add("");
		output.getErrori().add("SPOSTAMENTO COLLOCAZIONI");
		output.getErrori().add("");
		output.getErrori().add("Tipo Richiesta = " + input.getTipoOperazione());
		output.getErrori().add("");
		//Ricerca per collocazioni - tipoOperazione = S
		if (input.getTipoOperazione().equals("S")){
//			output.getErrori().add("Numero record trattati = " + listaCollocazioni.size());
			output.getErrori().add("Codice Polo sezione Partenza = " + input.getCodPoloSez());
			output.getErrori().add("Codice Bib sezione Partenza = " + input.getCodBibSez());
			output.getErrori().add("Codice sezione Partenza = " + input.getSezione());
			output.getErrori().add("Dalla collocazione = " + input.getDallaCollocazione());
			output.getErrori().add("Specificazione = " + input.getDallaSpecificazione());
			output.getErrori().add("Alla Collocazione = " + input.getAllaCollocazione());
			output.getErrori().add("Specificazione = " + input.getAllaSpecificazione());
			if (input.getCodSezArr() != null){
				output.getErrori().add("Codice Polo sezione Arrivo = " + input.getCodPoloSezArr());
				output.getErrori().add("Codice Bib sezione Arrivo = " + input.getCodBibSezArr());
				output.getErrori().add("Codice sezione Arrivo = " + input.getCodSezArr());
				output.getErrori().add("Collocazione Provvisoria = " + input.getCollProvv());
				output.getErrori().add("Specificazione Provvisoria = " + input.getSpecProvv());
			}else{
				output.getErrori().add("Codice Polo sezione Arrivo = Non indicata");
				output.getErrori().add("Codice Bib sezione Arrivo = Non indicata");
				output.getErrori().add("Codice sezione Arrivo = Non indicata");
				output.getErrori().add("Collocazione Provvisoria = Non indicata");
				output.getErrori().add("Specificazione Provvisoria = Non indicata");
			}
			output.getErrori().add("");
		}else{

			//Ricerca per range di inventari - tipoOperazione = I
//				output.getErrori().add("Numero record trattati = " + listaInv.size());
			output.getErrori().add("Codice Serie = " + input.getSerie());
			output.getErrori().add("Da Numero Inventario = " + input.getStartInventario());
			output.getErrori().add("A Numero Inventario = " + input.getEndInventario());
			output.getErrori().add("");
			if (input.getCodSezArr() != null){
				output.getErrori().add("Codice Polo sezione Arrivo = " + input.getCodPoloSezArr());
				output.getErrori().add("Codice Bib sezione Arrivo = " + input.getCodBibSezArr());
				output.getErrori().add("Codice sezione Arrivo = " + input.getCodSezArr());
				output.getErrori().add("Collocazione Provvisoria = " + input.getCollProvv());
				output.getErrori().add("Specificazione Provvisoria = " + input.getSpecProvv());
			}else{
				output.getErrori().add("Codice Polo sezione Arrivo = Non indicata");
				output.getErrori().add("Codice Bib sezione Arrivo = Non indicata");
				output.getErrori().add("Codice sezione Arrivo = Non indicata");
				output.getErrori().add("Collocazione Provvisoria = Non indicata");
				output.getErrori().add("Specificazione Provvisoria = Non indicata");
			}
		}
//		output.getErrori().add("Sezione di arrivo: " + input.getCodSezArr()+ " " + input.getCodBibSezArr() + " " + input.getCodSezArr() + " " );
//		output.getErrori().add("");
//		output.getErrori().add("Numero record spostati: " + (numRecSpostati));
	}

	private void scriviLogAggDisp(AggDispVO input,
			AggDispVO output, int numRecAggiornati) {
		output.getErrori().add("AGGIORNAMENTO DISPONIBILITA'");
		output.getErrori().add("");
		output.getErrori().add("Utente = " + input.getUser());
		output.getErrori().add("Ora inizio				= " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		output.getErrori().add("");
		output.getErrori().add("Tipo Richiesta = " + input.getTipoOperazione());
		output.getErrori().add("");
		output.getErrori().add("Tipo fruizione = " + input.getCodTipoFruizione());
		output.getErrori().add("");
		output.getErrori().add("Non disponibile per = " + input.getCodNoDispo());
		output.getErrori().add("");
		output.getErrori().add("Stato di conservazione = " + input.getCodiceStatoConservazione());
		output.getErrori().add("");
		output.getErrori().add("Tipo digitalizzazione = " + input.getCodDigitalizzazione());
		output.getErrori().add("");
		output.getErrori().add("Tipo di riproduzione ammessi = " + input.getCodRip());
		output.getErrori().add("");
		output.getErrori().add("Filtro sul titolo per Livello Autorità = " + input.getFiltroLivAut());
		output.getErrori().add("");
		output.getErrori().add("Filtro sul titolo per Natura = " + input.getFiltroCodNatura());
		output.getErrori().add("");

		// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
		output.getErrori().add("Filtro sul titolo per Tipo data pubblicazione= " + input.getTipoDataPubb());
		output.getErrori().add("");
		output.getErrori().add("Filtro sul titolo per intervallo Date di pubblicazione da = " + input.getAaPubbFrom() + " a " + input.getAaPubbTo());
		output.getErrori().add("");

		output.getErrori().add("Filtro su data di ingresso = " + input.getFiltroDataIngressoDa());
		output.getErrori().add("");
		output.getErrori().add("Filtro su tipo fruizione = " + input.getFiltroCodTipoFruizione());
		output.getErrori().add("");
		output.getErrori().add("Filtro su stato di conservazone = " + input.getFiltroStatoConservazione());
		output.getErrori().add("");
		output.getErrori().add("Filtro su motivo di non disponibilità = " + input.getFiltroCodNoDisp());
		output.getErrori().add("");
		output.getErrori().add("Filtro su riproducibilità = " + input.getFiltroCodRip());
		output.getErrori().add("");
		//Ricerca per collocazioni - tipoOperazione = S
		if (input.getTipoOperazione().equals("S")){
			output.getErrori().add("Codice Polo sezione = " + input.getCodPoloSez());
			output.getErrori().add("Codice Bib sezione = " + input.getCodBibSez());
			output.getErrori().add("Codice sezione = " + input.getSezione());
			output.getErrori().add("Dalla collocazione = " + input.getDallaCollocazione());
			output.getErrori().add("Specificazione = " + input.getDallaSpecificazione());
			output.getErrori().add("Alla Collocazione = " + input.getAllaCollocazione());
			output.getErrori().add("Specificazione = " + input.getAllaSpecificazione());
		}else if (input.getTipoOperazione().equals("R")){
			//canale = I
			output.getErrori().add("Codice Serie			= " + input.getSerie());
			output.getErrori().add("Da Numero Inventario 	= " + input.getStartInventario() + "	A Numero Inventario 	= " + input.getEndInventario());
		}else if  (input.getTipoOperazione().equals("N")){
				if (input.getSelezione() != null && input.getSelezione().equals("F")){
					output.getErrori().add("Lista Inventari			= " + input.getNomeFileAppoggioInv());
					output.getErrori().add("contenente 	N° inventari = " + input.getListaInventari().size());
				}else{
					output.getErrori().add("Lista Inventari	contenente 	N° inventari = " + input.getListaInventari().size());
				}
		}else if (input.getTipoOperazione().equals("D")){
			//canale = D data d'ingresso
		}else{
			output.getErrori().add("Errore nel tipo operazione");
		}
	}
	private void scriviLogScarInv(ScaricoInventarialeVO input,
			ScaricoInventarialeVO output, int numRecScaricati) {

		output.getErrori().add("SCARICO INVENTARIALE'");
		output.getErrori().add("");
		output.getErrori().add("Utente					= " + input.getUser());
		output.getErrori().add("Ora inizio				= " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		output.getErrori().add("");
		if (input.getTipoOperazione().equals("N")){
			if (input.getSelezione() != null && input.getSelezione().equals("F")){
				output.getErrori().add("Lista Inventari			= " + input.getNomeFileAppoggioInv());
				output.getErrori().add("contenente 	N° inventari = " + input.getListaInventari().size());
			}else{
				output.getErrori().add("Lista Inventari	contenente 	N° inventari = " + input.getListaInventari().size());
			}
		}else{
			output.getErrori().add("Codice Serie			= " + input.getSerie());
			output.getErrori().add("Da Numero Inventario 	= " + input.getStartInventario() + "	A Numero Inventario 	= " + input.getEndInventario());
		}
		output.getErrori().add("");
		output.getErrori().add("verso la biblioteca		= " + input.getPolo() + " " + input.getVersoBibliotecaDescr());
		output.getErrori().add("");
		output.getErrori().add("Motivo dello scarico	= " + input.getMotivoDelloScarico());
		output.getErrori().add("");
		output.getErrori().add("Numero buono scarico	= " + input.getNumBuonoScarico());
		output.getErrori().add("");
		output.getErrori().add("Data scarico			= " + input.getDataScarico());
		output.getErrori().add("");
		output.getErrori().add("Testo della delibera	= " + input.getTestoDelibera());
		output.getErrori().add("");
		output.getErrori().add("Data delibera			= " + input.getDataDelibera());
		output.getErrori().add("");
	}
	/**
	 * @param input
	 * @param output
	 * @param listaInv
	 * @param numRecSpostati
	 * @param collocazione
	 * @param ts
	 * @param totInvColl
	 * @param totInvSez
	 * @return
	 * @throws DaoManagerException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @throws RemoteException
	 */
	private int scorriListaInventariPerSpostamentoCollocazioni(SpostamentoCollocazioniVO input, SpostamentoCollocazioniVO output,
			List<CodiceVO> listaInventari, int numRecSpostati, Timestamp ts, Logger logger)
	throws DaoManagerException, ApplicationException, DataException, ValidationException, RemoteException {

		UserTransaction tx = context.getUserTransaction();
		Logger _log = logger;

		int loop = 0;
		int totInv = 0;

		daoInv = new Tbc_inventarioDao();
		SpostamentoCollocazioniVO input2 = input.copy();
		for (int i = 0; i < listaInventari.size(); i++) {
			CodiceVO stringaInv = listaInventari.get(i);
			try{
				BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
				Tbc_inventarioDao.begin(tx);

				Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), stringaInv.getCodice(), Integer.valueOf(stringaInv.getDescrizione()));
				if (inventario != null){
					// controllo se è in prestito
					// richiama un ejb di servizi che conta i movimenti aperti
					int movA = servizi.getNumeroMovimentiAttivi(inventario.getCd_serie()
							.getCd_polo().getCd_polo().getCd_polo(), inventario
							.getCd_serie().getCd_polo().getCd_biblioteca(),
							inventario.getCd_serie().getCd_serie(), inventario
							.getCd_inven());
					if (movA > 0) {
						if (input.getPrestito() == null){
							// inventario in prestito senza richiesta di spostamento
							output.getErrori().add("Inventario '" + inventario.getCd_serie().getCd_serie() + inventario
									.getCd_inven() + "' con "+movA+" movimenti aperti non spostato");
							continue;
						}else{
							// inventario in prestito con richiesta di spostamento
							output.getErrori().add("Inventario '" + inventario.getCd_serie().getCd_serie() + inventario
									.getCd_inven() + "' con "+movA+" movimenti aperti");
						}
					}

					Tb_titolo titolo = daoInv.getTitoloDF1(inventario.getB().getBid());
					if (titolo == null) {
						output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
						output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven() + "	titolo Non Trovato Su BD Polo");
						continue;
					}else{
						if (inventario.getKey_loc() == null){
							output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
							output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven() + "	non collocato");
							output.getErrori().add("titolo		" +  titolo.getIsbd());
							continue;
						}else{
							if (input2.getTipoOperazione().equals("R")){
								//deve essere definita l'azione in base alla sezione di partenza e di arrivo
								input2.setCodPoloSez(inventario.getKey_loc().getCd_sez().getCd_polo().getCd_polo().getCd_polo());
								input2.setCodBibSez(inventario.getKey_loc().getCd_sez().getCd_polo().getCd_biblioteca());
								input2.setSezione(inventario.getKey_loc().getCd_sez().getCd_sez());
								output = controllaSezionePartenza(input2, output);
							}
							if (input2.getAzione() != null){
								if (input2.getSezioneA() == null && input2.getAzione().equals("ricollocazione")){
									if (!ValidazioneDati.isFilled(inventario.getKey_loc_old()) ) {
										continue;
									}
									//scolloco dalla sezione di partenza
									int totInvCollP = inventario.getKey_loc().getTot_inv() - 1;
									inventario.getKey_loc().setTot_inv(totInvCollP);

									int totInvSezP = inventario.getKey_loc().getCd_sez().getTot_inv() - 1;
									inventario.getKey_loc().getCd_sez().setTot_inv(totInvSezP);

									if ((inventario.getKey_loc().getTot_inv() < 1) || (inventario.getKey_loc().getTot_inv() < 1
											&& inventario.getKey_loc().getTot_inv_prov().intValue() < 1)) {
										//cancella la collocazione rimasta vuota
										inventario.getKey_loc().setTs_var(ts);
										inventario.getKey_loc().setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
										inventario.getKey_loc().setFl_canc('S');
									}
									//
									Tbc_sezione_collocazione  appoKeyLocPCodSez = inventario.getKey_loc().getCd_sez();
									String appoKeyLocPCodColl = inventario.getKey_loc().getCd_loc();
									String appoKeyLocPCodSpec = inventario.getKey_loc().getSpec_loc();

									inventario.setKey_loc(null);
									inventario.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
									inventario.setTs_var(ts);
									if(daoInv.modificaInventario(inventario)){
										int totInvColl = 0;
										int totInvSez = 0;
										//l'inventario viene recuperato da una collocazione provvisoria
										//aggiorna i contatori sulla collocazione provvisoria e
										//ripristino della collocazione definitiva
										Tbc_collocazione collocazioneOld = daoColl.getCollocazione(inventario.getKey_loc_old());
										if (collocazioneOld != null){
											//colloco alla sezione di collocazioneOld di arrivo
											totInvColl = collocazioneOld.getTot_inv() + 1;
											collocazioneOld.setTot_inv(totInvColl);
											totInvSez = collocazioneOld.getCd_sez().getTot_inv() + 1;
											collocazioneOld.setTot_inv(totInvSez);

											int totInvProv = collocazioneOld.getTot_inv_prov() - 1;
											collocazioneOld.setTot_inv_prov(totInvProv);

											//dati collocazione per aggiornamento
											collocazioneOld.setTot_inv(totInvColl);
											collocazioneOld.setTs_var(ts);
											collocazioneOld.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
											//dati sezione per aggiornamento
											collocazioneOld.getCd_sez().setTot_inv(totInvSez);
											collocazioneOld.getCd_sez().setTs_var(ts);
											collocazioneOld.getCd_sez().setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
											if (collocazioneOld.getFl_canc() == 'S')
												collocazioneOld.setFl_canc('N');


											inventario.setKey_loc(collocazioneOld);
											totInv = collocazioneOld.getTot_inv();
											output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
											output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven());
											output.getErrori().add("titolo		" +  titolo.getIsbd());
											output.getErrori().add("RICOLLOCAZIONE dalla sezione	" + appoKeyLocPCodSez.getCd_sez().trim() + "	codLoc: " + appoKeyLocPCodColl.trim() + " specLoc: " + appoKeyLocPCodSpec.trim());
											output.getErrori().add("				alla sezione	" + collocazioneOld.getCd_sez().getCd_sez().trim() + "	codLoc: " + collocazioneOld.getCd_loc().trim()+ " specLoc: " + collocazioneOld.getSpec_loc().trim());
										}else{
											output.getErrori().add("ERRORE: inventario"+  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven() + " e collocazione_old = "+inventario.getKey_loc_old()+" non presente sul DB");
											continue;
										}
										//devo cancellare la collocazione temporanea ossia scollocare?
										inventario.setKey_loc_old(0);
										inventario.setSez_old("");
										inventario.setLoc_old("");
										inventario.setSpec_old("");

									}
								}else if (input2.getSezioneA() != null && !input2.getAzione().equals("ricollocazione")){

									//colloco a sezioneA
									if (input2.getAzione().equals("spostamentoTemporaneo")){
										//appoggio i dati da scollocare
										inventario.setKey_loc_old(inventario.getKey_loc().getKey_loc());
										inventario.setSez_old(inventario.getKey_loc().getCd_sez().getCd_sez());
										inventario.setLoc_old(inventario.getKey_loc().getCd_loc());
										inventario.setSpec_old(inventario.getKey_loc().getSpec_loc());
										//non vario i contatori del numero di inventari della sezione e della collocazione
										//perchè ciò avviene in crea collocazione


										//scolloco dalla sezione di partenza
										int totInvCollP = inventario.getKey_loc().getTot_inv() - 1;
										inventario.getKey_loc().setTot_inv(totInvCollP);
										//
										int totInvSezP = inventario.getKey_loc().getCd_sez().getTot_inv() - 1;
										inventario.getKey_loc().getCd_sez().setTot_inv(totInvSezP);
										//
										if ((inventario.getKey_loc().getTot_inv() < 1) || (inventario.getKey_loc().getTot_inv() < 1
												&& inventario.getKey_loc().getTot_inv_prov().intValue() < 1)) {
											//cancella la collocazione rimasta vuota
											inventario.getKey_loc().setTs_var(ts);
											inventario.getKey_loc().setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
											inventario.getKey_loc().setFl_canc('S');
										}

										inventario.getKey_loc().getCd_sez().setTot_inv(totInvSezP);
										inventario.getKey_loc().getCd_sez().setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
										inventario.getKey_loc().getCd_sez().setTs_var(ts);

										//

										inventario.setKey_loc(null);
										inventario.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
										inventario.setTs_var(ts);
										if(daoInv.modificaInventario(inventario)){
											CodiciNormalizzatiVO collSpecA = null;
											String collProvv = null;
											String specProvv = null;
											if (input2.getCollProvv() != null || input2.getSpecProvv() != null){
												//prendo i codici collProvv e specProvv di input
												collProvv = input2.getCollProvv();
												specProvv = input2.getSpecProvv();
											}else{
												//prendo i codici collProvv e specProvv della keyLocOld
												collProvv = inventario.getLoc_old();
												specProvv = inventario.getSpec_old();
											}
											collSpecA = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(input2.getSezioneA().getCd_colloc()), collProvv, null,
													false, specProvv, null, false, "");
											if (collSpecA != null){
												if (collSpecA.getDaColl() == null || collSpecA.getDaColl() == null){
													output.getErrori().add("ERRORE: errore in fase di normalizzazione di collocazioni e specificazioni");
												}
											}else{
												output.getErrori().add("ERRORE: errore in fase di normalizzazione di collocazioni e specificazioni");
											}
											CollocazioneVO collocazioneProvvisoria = new CollocazioneVO();
											collocazioneProvvisoria.setCodPolo(input2.getCodPolo());
											collocazioneProvvisoria.setCodBib(input2.getCodBib());
											collocazioneProvvisoria.setCodPoloSez(input2.getSezioneA().getCd_polo().getCd_polo().getCd_polo());
											collocazioneProvvisoria.setCodBibSez(input2.getSezioneA().getCd_polo().getCd_biblioteca());
											collocazioneProvvisoria.setCodSez(input2.getSezioneA().getCd_sez());
											collocazioneProvvisoria.setCodColloc(collProvv.trim());
											collocazioneProvvisoria.setSpecColloc(specProvv.trim());
											collocazioneProvvisoria.setOrdLoc(collSpecA.getDaColl().trim());
											collocazioneProvvisoria.setOrdSpec(collSpecA.getDaSpec().trim());
											collocazioneProvvisoria.setConsistenza("$");
											collocazioneProvvisoria.setBid(inventario.getB().getBid());
											collocazioneProvvisoria.setUteAgg(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
											collocazioneProvvisoria.setUteIns(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
											Tbc_collocazione coll = this.inventario.creaCollocazione(inventario,collocazioneProvvisoria,"M_INV");
											//verificare M_INV
											if (coll == null){
												//errore in output
												output.getErrori().add("ERRORE: spostamento temporaneo e problemi in fase di creazione collocazione");
												//return output;
											}
											int totInvProv = coll.getTot_inv_prov() + 1;
											coll.setTot_inv_prov(totInvProv);
											inventario.setKey_loc(coll);
											totInv = coll.getTot_inv();
											output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
											output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven());
											output.getErrori().add("titolo		" +  titolo.getIsbd());
											output.getErrori().add("spostamento TEMPORANEO dalla sezione " + inventario.getSez_old() + " codLocOld: " + inventario.getLoc_old()+ " specLocOld: " + inventario.getSpec_old());
											output.getErrori().add("						alla sezione " + coll.getCd_sez().getCd_sez().trim() + " codLoc: " + coll.getCd_loc().trim()+ " specLoc: " + coll.getSpec_loc().trim());
										}
									}else{
										if (input2.getAzione().equals("spostamentoDefinitivo")){
											boolean coll = false;
											//appoggio i dati da scollocare
											//scolloco dalla sezione di partenza
											//sottraggo 1 a totInv di sezione di partenza e aggiorno la sezione della collocazione
											//lascio la stessa collocazione modificandone soltanto la sezione con la sezione di arrivo
											//
											Tbc_collocazione keyLoc = inventario.getKey_loc();
											Tbc_sezione_collocazione codSezP = keyLoc.getCd_sez();

											String codSezPartenza = codSezP.getCd_sez();
											String codSezArr = input2.getCodSezArr().trim();

											if (input2.getTipoOperazione().equals("S") ||
													(input2.getTipoOperazione().equals("R") && keyLoc.getTot_inv() == 1)){
												CollocazioneDettaglioVO collocazioneDuplicata = new CollocazioneDettaglioVO();
												collocazioneDuplicata.setCodPolo(input2.getCodPolo());
												collocazioneDuplicata.setCodBib(input2.getCodBib());
												collocazioneDuplicata.setCodPoloSez(input2.getCodPoloSezArr());
												collocazioneDuplicata.setCodBibSez(input2.getCodBibSezArr());
												collocazioneDuplicata.setCodSez(input2.getCodSezArr().trim());
												collocazioneDuplicata.setKeyColloc(keyLoc.getKey_loc());
												collocazioneDuplicata.setCodColloc(keyLoc.getCd_loc().trim());
												collocazioneDuplicata.setSpecColloc(keyLoc.getSpec_loc().trim());
												collocazioneDuplicata.setOrdLoc(keyLoc.getOrd_loc().trim());
												collocazioneDuplicata.setOrdSpec(keyLoc.getOrd_spec().trim());
												collocazioneDuplicata.setConsistenza(keyLoc.getConsis().trim());
												collocazioneDuplicata.setBid(keyLoc.getB().getBid());
												collocazioneDuplicata.setTotInv(keyLoc.getTot_inv());
												collocazioneDuplicata.setTotInvProv(keyLoc.getTot_inv_prov());
												collocazioneDuplicata.setTipoColloc(keyLoc.getCd_sez().getCd_colloc()+ "");
												collocazioneDuplicata.setDataAgg(keyLoc.getTs_var().toString());
												collocazioneDuplicata.setUteAgg(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
												collocazioneDuplicata.setUteIns(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
												coll = this.collocazione.updateCollocazione(collocazioneDuplicata, true, true, "");
												if (coll){
													//int totInvProv = keyLoc.getTot_inv_prov() + 1;
													//keyLoc.setTot_inv_prov(totInvProv);
													inventario.setKey_loc(keyLoc);
													totInv = keyLoc.getTot_inv();
													output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
													output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven());
													output.getErrori().add("titolo		" +  titolo.getIsbd());
													output.getErrori().add("spostamento DEFINITIVO dalla sezione " + codSezPartenza + " codLoc: " + keyLoc.getCd_loc().trim() + " specLoc: " + keyLoc.getSpec_loc().trim() );
													output.getErrori().add("						alla sezione " + codSezArr + " codLoc: " +keyLoc.getCd_loc().trim()+ " specLoc: " + keyLoc.getSpec_loc().trim());
//													output.getErrori().add("						   di numero " + totInv + " inventari.");
												}else{
													//errore in output
													output.getErrori().add("ERRORE: spostamento definitivo e problemi in modifica collocazione in presenza di un solo inventario legato alla stessa collocazione");
												}
											}else if (input2.getTipoOperazione().equals("R") && keyLoc.getTot_inv() > 1){
												//descrmento i totInv della sezione di partenza
												int totInvCollP = keyLoc.getTot_inv() - 1;
												keyLoc.setTot_inv(totInvCollP);
												//
												int totInvSezP = inventario.getKey_loc().getCd_sez().getTot_inv() - 1;
												keyLoc.getCd_sez().setTot_inv(totInvSezP);
												//
												if ((keyLoc.getTot_inv() < 1) || (keyLoc.getTot_inv() < 1
														&& keyLoc.getTot_inv_prov().intValue() < 1)) {
													//cancella la collocazione rimasta vuota
													keyLoc.setTs_var(ts);
													keyLoc.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
													keyLoc.setFl_canc('S');
												}

												keyLoc.getCd_sez().setTot_inv(totInvSezP);
												keyLoc.getCd_sez().setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
												keyLoc.getCd_sez().setTs_var(ts);
												//
												inventario.setKey_loc(null);
												inventario.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
												inventario.setTs_var(ts);
												if(daoInv.modificaInventario(inventario)){
													CollocazioneDettaglioVO collocazioneDuplicata = new CollocazioneDettaglioVO();
													//non setto nè keyLoc nè totInv
													collocazioneDuplicata.setCodPolo(input2.getCodPolo());
													collocazioneDuplicata.setCodBib(input2.getCodBib());
													collocazioneDuplicata.setCodPoloSez(input2.getCodPoloSezArr());
													collocazioneDuplicata.setCodBibSez(input2.getCodBibSezArr());
													collocazioneDuplicata.setCodSez(input2.getCodSezArr().trim());
													collocazioneDuplicata.setCodColloc(keyLoc.getCd_loc().trim());
													collocazioneDuplicata.setSpecColloc(keyLoc.getSpec_loc().trim());
													collocazioneDuplicata.setOrdLoc(keyLoc.getOrd_loc().trim());
													collocazioneDuplicata.setOrdSpec(keyLoc.getOrd_spec().trim());
													collocazioneDuplicata.setConsistenza(keyLoc.getConsis().trim());
													collocazioneDuplicata.setBid(keyLoc.getB().getBid());
													collocazioneDuplicata.setTipoColloc(keyLoc.getCd_sez().getCd_colloc()+ "");
													collocazioneDuplicata.setDataAgg(keyLoc.getTs_var().toString());
													collocazioneDuplicata.setUteAgg(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
													collocazioneDuplicata.setUteIns(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
													Tbc_collocazione collocazione = this.inventario.creaCollocazione(inventario,collocazioneDuplicata,"M_INV");
													if (collocazione != null){
														inventario.setKey_loc(collocazione);
														totInv = collocazione.getTot_inv();
														output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
														output.getErrori().add("inventario	" +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven());
														output.getErrori().add("titolo		" +  titolo.getIsbd());
														output.getErrori().add("spostamento DEFINITIVO dalla sezione " + codSezPartenza + " codLoc: " + keyLoc.getCd_loc() + " specLoc: " + keyLoc.getSpec_loc() );
														output.getErrori().add("						alla sezione " + codSezArr + " codLoc: " +keyLoc.getCd_loc().trim()+ " specLoc: " + keyLoc.getSpec_loc().trim());
//														output.getErrori().add("						   di numero " + totInv + " inventari.");
													}else{
														//errore in output
														output.getErrori().add("ERRORE: spostamento definitivo e problemi in fase di creazione di collocazione in presenza di più inventari legati alla stessa collocazione");
														//return output;
													}
												}
											}
										}
									}
								}else{
									output.getErrori().add("ERRORE: Sezione di arrivo null e Azione diversa da ricollocazione");
									continue;
								}
							}else{
								output.getErrori().add("ERRORE: Per l'inventario " +  inventario.getCd_serie().getCd_serie() +"	"+  inventario.getCd_inven()+" l'azione non puo' essere impostata per richiesta incongruente");
								continue;
							}
						}

						if (inventario.getKey_loc().getTot_inv() < 1 && inventario.getKey_loc().getTot_inv_prov() < 1){
							if (inventario.getKey_loc().getTot_inv() < 1 && inventario.getKey_loc().getTot_inv_prov() < 1){
								inventario.getKey_loc().setFl_canc('S');
								inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_PRECISATO_CHR);
							}else{
								inventario.getKey_loc().setFl_canc(' ');
							}
						}
						inventario.setUte_var(input2.getCodPolo()+input2.getCodBib()+input2.getUser());
						inventario.setTs_var(ts);
						if(daoInv.modificaInventario(inventario)){
							numRecSpostati += totInv;
							DaoManager.commit(tx);
							//
							if (input2.getEtichette() != null && input2.getEtichette().equals(DocumentoFisicoCostant.ETICHETTE)){
								dfCommon.scriviDatiEtichetta(input2.getListaEtichette(), inventario, input2.getTipoOperazione(), input2.getDescrBibEtichetta());
								output.setListaEtichette(input2.getListaEtichette());
								output.setStampaDiffEtichette(input2.getStampaDiffEtichette());
								output.getErrori().add("scritta etichetta per inventario" +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven());
							}
						}
					}
					if ((numRecSpostati % 500) == 0){
						_log.debug("Finora " + ts + "sono stati spostati "+ numRecSpostati + " records");
						_log.debug("-- ultimo inventario spostato: " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven());
						continue;
					}
					//
				}else{
					output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
					output.getErrori().add("inventario " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven() + " e inventario " + inventario.getCd_serie().getCd_serie() + " " + inventario.getCd_inven() + " = null");
					continue;
				}


			} catch (Exception e) {
				_log.error("", e);
				output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
				output.getErrori().add("ECCEZIONE: " + e.getMessage());
			}
		}
		output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
		output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		return numRecSpostati;
	}

	private void aggiornaCollocazioneDaDuplicare(SpostamentoCollocazioniVO output,
			SpostamentoCollocazioniVO input2, Tbc_inventario inventario)
			throws DataException, ValidationException, RemoteException {
	}


	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	private SpostamentoCollocazioniVO controllaInputSpostColl(SpostamentoCollocazioniVO input, SpostamentoCollocazioniVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		Tbc_sezione_collocazione sezioneP = null;
		Tbc_sezione_collocazione sezioneA = null;


		try{
				input.validate(input.getErrori());
				if ((input.getCodPoloSez() != null && !input.getCodPoloSez().equals(""))
						&& (input.getCodBibSez() != null && !input.getCodBibSez().equals(""))){
					output = controllaSezionePartenza(input, output);
				}else if (input.getTipoOperazione().equals("R")){
					input.validate(input.getErrori());
				}
		} catch (Exception e) {
			log.error("", e);
		}
		return output;
	}

	private AggDispVO controllaInputAggDisp(AggDispVO input, AggDispVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{

		try{
			input.validate(input.getErrori());
		} catch (Exception e) {
			log.error("", e);
		}
		return output;
	}

	private ScaricoInventarialeVO controllaInputScarInv(ScaricoInventarialeVO input, ScaricoInventarialeVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{

			try{
				input.validate(input.getErrori());
		} catch (Exception e) {
			log.error("", e);
		}
		return output;
	}
	private SpostamentoCollocazioniVO controllaSezionePartenza(SpostamentoCollocazioniVO input,
			SpostamentoCollocazioniVO output) throws DaoManagerException {
		Tbc_sezione_collocazione sezioneP = null;
		Tbc_sezione_collocazione sezioneA = null;
		daoSez = new Tbc_sezione_collocazioneDao();
		sezioneP = daoSez.getSezione(input.getCodPoloSez(), input.getCodBibSez(), input.getSezione().trim());
		if (sezioneP == null){
			output.getErrori().add("ERRORE: Sezione di Partenza inesistente");
			return output;
		}else{
			input.setSezioneP(sezioneP);
			if (sezioneP.getTipo_sez() == 'T'){
				if ((input.getCodPoloSezArr() != null && !input.getCodPoloSezArr().equals(""))
						&& (input.getCodBibSezArr() != null && !input.getCodBibSezArr().equals(""))){
					//sezione di arrivo di input presente
					output.getErrori().add("ERRORE: La sezione di partenza è temporanea --> la sezione di arrivo non deve essere indicata");
					return output;
				}else{
					//sezione di arrivo di input non presente
					input.setCodPoloSezArr(null);
					input.setCodBibSezArr(null);
					input.setSezioneA(null);
					input.setAzione("ricollocazione");
				}
			}else{
				//				if ((input.getCodPoloSezArr() != null && input.getCodPoloSezArr().equals(""))
				//						&& (input.getCodBibSezArr() != null && input.getCodBibSezArr().equals(""))){
				if ((input.getCodPoloSezArr() == null)
						&& (input.getCodBibSezArr() == null)){
					//sezione di arrivo di input non presente
					output.getErrori().add("ERRORE: La sezione di partenza è definitiva --> la sezione di arrivo è obbligatoria");
					return output;
				}else{
					//sezione di arrivo di input presente
					daoSez = new Tbc_sezione_collocazioneDao();
					sezioneA = daoSez.getSezione(input.getCodPoloSezArr(), input.getCodBibSezArr(), input.getCodSezArr().trim());
					if (sezioneA == null){
						output.getErrori().add("ERRORE: Sezione di Arrivo inesistente sul DB");
						return output;
					}else{
						input.setSezioneA(sezioneA);
						if (sezioneA.getTipo_sez() == 'T'){
							input.setAzione("spostamentoTemporaneo");
						}else{
							//le sezioni non sono temporanee, il tipo collocazione di partenza e arrivo
							//devono essere deve coincidere
							if (sezioneP.getCd_colloc() != sezioneA.getCd_colloc()){
								if (sezioneA.getCd_colloc() != 'M'){
									output.getErrori().add("ERRORE: Il tipo di collocazione delle sezioni di collocazione di partenza e di arrivo non coincidono");
									return output;
								}
								//							}else{
								//								input.setAzione("spostamentoDefinitivo");
								//							}
							}
							input.setAzione("spostamentoDefinitivo");
						}
					}
				}
			}
			if (sezioneA != null){
				if (sezioneP.getCd_sez().trim().equals(sezioneA.getCd_sez().trim())){
					output.getErrori().add("ERRORE: La sezione di partenza coincide con la sezione di arrivo");
					return output;
				}
			}
		}
		return output;
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public AggDispVO getAggiornamentoDisponibilita(AggDispVO input,  BatchLogWriter blw)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{

		AggDispVO output = new AggDispVO(input);
		ScrollableResults listaInv = null;
		int numRecAggiornati = 0;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		UserTransaction tx = context.getUserTransaction();
		 Logger logger = blw.getLogger();
		try{
			DaoManager.begin(tx);
			logger.debug("Inizio transazione aggiornamento");
			this.scriviLogAggDisp(input, output, numRecAggiornati);
			output = this.controllaInputAggDisp(input, output);
			logger.debug("Inizio validazione input");
			blw.logWriteCollection(input.getErrori());


			Timestamp dataIngressoDa = null;
			Timestamp dataIngressoA = null;
			int numInvMinMax = -1;

			int numRecTrattati = 0;
			if (input.getFiltroDataIngressoDa() != null &&
					(!input.getFiltroDataIngressoDa().trim().equals("") && !input.getFiltroDataIngressoDa().equals("00/00/0000"))){
				dataIngressoDa = (DateUtil.toTimestamp(input.getFiltroDataIngressoDa()));
				if (!input.getFiltroDataIngressoA().trim().equals("") && !input.getFiltroDataIngressoA().equals("00/00/0000")){
					dataIngressoA = (DateUtil.toTimestampA(input.getFiltroDataIngressoA()));
				}else{
					if (input.getFiltroDataIngressoA().trim().equals("") || input.getFiltroDataIngressoA().equals("00/00/0000")){
						dataIngressoA = (ts);
					}
				}
			}
			Object obj = input;
//			if (input.getTipoOperazione().equals("D")){
//				daoInv = new Tbc_inventarioDao();
//				listaInv = daoInv.getListaInventariScrollableDataDaAAggDisp(input.getCodPolo(), input.getCodBib(), null, 0,
//						dataIngressoDa, dataIngressoA, input.getFiltroCodTipoFruizione(), input.getFiltroCodRip(),
//						input.getFiltroCodNoDisp(), input.getFiltroStatoConservazione());
//				List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
//				DaoManager.commit(tx);
//				numRecAggiornati = trattamentoModificaInventari(input, output, listaInventari, dataIngressoDa, dataIngressoA,
//						numRecAggiornati, logger);
//			}else
				if (input.getTipoOperazione().equals("R")){
				//trattamento campo inventario Da
				//trattamento campo inventario A
				//non ho il codice inventario impostato quindi cerco il numero massimo
				if (input.getEndInventario().equals("") || input.getEndInventario().trim().equals("0")){
					daoInv = new Tbc_inventarioDao();
					numInvMinMax = daoInv.getMaxInventario(input.getCodPolo(), input.getCodBib(), input.getSerie(), ts.toString());
					if (numInvMinMax > 0){
						input.setEndInventario(String.valueOf(numInvMinMax));
					}else{
						throw new DataException("erroreNumInvMinMax");
					}
				}
				daoInv = new Tbc_inventarioDao();
				listaInv = daoInv.getListaInventariScrollableDaAAggDisp(input.getCodPolo(), input.getCodBib(),
						input.getSerie(), input.getStartInventario(), input.getEndInventario(),
						dataIngressoDa, dataIngressoA,
						input.getFiltroCodTipoFruizione(), input.getFiltroCodRip(),
						input.getFiltroCodNoDisp(), input.getFiltroStatoConservazione());
				List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
				numRecAggiornati = trattamentoModificaInventari(input, output, listaInventari,
						numRecAggiornati, logger);
			}else if (input.getTipoOperazione().equals("N")){

				List listaInventariInput = input.getListaInventari();
				List listaInv1 = new ArrayList();

				if (listaInventariInput != null){
					numRecTrattati = listaInventariInput.size();
					daoInv = new Tbc_inventarioDao();
					if (listaInventariInput.size() > 0){
						for (int y=0; y<listaInventariInput.size(); y++) {
							CodiceVO elem = (CodiceVO) listaInventariInput.get(y);
							listaInv = daoInv.getListaInventariScrollableDaAAggDisp(input.getCodPolo(), input.getCodBib(),
									elem.getCodice(), elem.getDescrizione(), null,
									dataIngressoDa, dataIngressoA,
									input.getFiltroCodTipoFruizione(), input.getFiltroCodRip(),
									input.getFiltroCodNoDisp(), input.getFiltroStatoConservazione());

							List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
							if (listaInventari.size() > 0) {
								//prendo l'inventario e lo aggiungo alla lista, ciclo sui successivi se presenti
								//alla fine passo l'array degli inventari trovati a al trattamento
								for (int i=0; i<listaInventari.size(); i++) {
									CodiceVO inventario = listaInventari.get(i);
									if (inventario != null){
										listaInv1.add(inventario);
									}
								}
							}else{
								output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e inventario: "+elem.getCodice()+" "+elem.getDescrizione()+" non trovato");
								continue;
							}
						}

						numRecAggiornati = trattamentoModificaInventari(input, output, listaInv1,
								numRecAggiornati, logger);
					}else{
						output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e lista inventari vuota");
					}
					output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
					output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
					output.getErrori().add("Numero record aggiornati = " + numRecAggiornati);
				}else{
					output.getErrori().add("ERRORE: tipo operazione '"+input.getTipoOperazione()+"' e lista inventari non presente");
				}
			}else if (input.getTipoOperazione().equals("S")){
				ScrollableResults listaCollocazioniSezP = null;
				daoSez = new Tbc_sezione_collocazioneDao();
				Tbc_sezione_collocazione sezione = daoSez.getSezione(input.getCodPolo(), input.getCodBib(), input.getSezione());
//				CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
//						false, input.getDallaSpecificazione(), input.getAllaSpecificazione(), false, "");
				CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
						 input.getDallaSpecificazione(), input.getAllaSpecificazione(),  "");
				if (collSpec != null){
					if (collSpec.getDaColl().compareTo(collSpec.getAColl()) >  0){
						output.getErrori().add("ERRORE: tipo operazione S e dalla collocazione > di alla collocazione");
					}
					if (collSpec.getDaSpec().compareTo(collSpec.getASpec()) >  0){
						output.getErrori().add("ERRORE: tipo operazione S e dalla specificazione > di alla specificazione");
					}
				}else{
					output.getErrori().add("ERRORE: tipo operazione S errore in fase di normalizzazione di collocazioni e specificazioni");
				}
				daoInv = new Tbc_inventarioDao();
				listaInv = daoInv.getListaInventariScrollableAggDisp(input.getCodPoloSez(),input.getCodBibSez(),
						input.getSezione().trim(), collSpec.getDaColl(), collSpec.getAColl(), false, collSpec.getDaSpec(), collSpec.getASpec(), false, 0, null, 0,
						dataIngressoDa, dataIngressoA,
						input.getFiltroCodTipoFruizione(), input.getFiltroCodRip(),
						input.getFiltroCodNoDisp(), input.getFiltroStatoConservazione());
				List<CodiceVO> listaInventari = creaFileSequenziale(obj, listaInv);
				numRecAggiornati = trattamentoModificaInventari(input, output, listaInventari,
						numRecAggiornati, logger);
			}
			DaoManager.commit(tx);
		} catch (Exception e) {
			output.getErrori().add("Numero record aggiornati: " + (numRecAggiornati));
			output.setStato(ConstantsJMS.STATO_ERROR);
			output.getErrori().add("ECCEZIONE = " + e);
			try {
				DaoManager.rollback(tx);
				logger.error("Rollback transazione");
				return output;
			} catch (Exception e1) {
				logger.error("", e1);
			}
			return output;
		}

		return output;
	}

	/**
	 * @param input
	 * @param listaInv
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ValidationException
	 * @throws Exception
	 * @throws DataException
	 */
	private List<CodiceVO> creaFileSequenziale(Object obj,
			ScrollableResults listaInv) throws IOException,
			FileNotFoundException, ValidationException, Exception,
			DataException {
		File f = null;
		if (obj instanceof AggDispVO){
			AggDispVO input = (AggDispVO)obj;
			f = File.createTempFile("aggDisp_" + input.getIdBatch(), ".dat");
		}else if (obj instanceof SpostamentoCollocazioniVO){
			SpostamentoCollocazioniVO input = (SpostamentoCollocazioniVO)obj;
			f = File.createTempFile("spostColl_" + input.getIdBatch(), ".dat");
		}else if (obj instanceof ScaricoInventarialeVO){
			ScaricoInventarialeVO input = (ScaricoInventarialeVO)obj;
			f = File.createTempFile("scarInv_" + input.getIdBatch(), ".dat");
		}
		BufferedOutputStream streamOut = new BufferedOutputStream(new FileOutputStream(f));
		int loop = 0;
		Session session = (new DaoManager()).getCurrentSession();
		while (listaInv.next()) {
			Tbc_inventario inv = (Tbc_inventario) listaInv.get(0);
			streamOut.write((formattaChiaveInv(inv) + "\n").getBytes());
			if ((++loop % 100) == 0) {	// pulisco session hibernate
				session.flush();
				session.clear();
			}
		}
		streamOut.close();

		InputStream is = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		if (br == null) {
			throw new ValidationException("erroreAcquisizioneInventariDaFile");
		}
		List<CodiceVO> listaInventari = new ArrayList<CodiceVO>();

		String inv = null;
		while ( (inv = br.readLine() ) != null ){
			CodiceVO serieInv = (ValidazioneDati.leggiInventario(inv));
			if (serieInv != null){
				listaInventari.add(serieInv);
			}else{
				throw new DataException("contenutoFileNonValido");
			}
		}
		br.close();
		f.delete();
		return listaInventari;
	}

	private String formattaChiaveInv(Tbc_inventario inv) {
		return inv.getCd_serie().getCd_serie() +
		String.format("%09d", inv.getCd_inven() );
	}

	/**
	 * @param input
	 * @param output
	 * @param dataIngressoDa
	 * @param dataIngressoA
	 * @param numRecAggiornati
	 * @param listaInvData
	 * @return
	 * @throws DaoManagerException
	 */
	private int trattamentoModificaInventari(AggDispVO input,
			AggDispVO output, List<CodiceVO> listaInventari,
//			Timestamp dataIngressoDa,
//			Timestamp dataIngressoA,
			int numRecAggiornati, Logger logger) throws DaoManagerException {
		UserTransaction tx = context.getUserTransaction();


		Timestamp ts = DaoManager.now();
		Logger logg = logger;
		int loop = 0;
		CodiceVO ultKeyInv = null;
		daoInv = new Tbc_inventarioDao();

		for (int i = 0; i < ValidazioneDati.size(listaInventari); i++) {
			CodiceVO stringaInv = listaInventari.get(i);
			try {
				if ((i % 100) == 0) {
					BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
				}	
				DaoManager.begin(tx);

				Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), stringaInv.getCodice(), Integer.valueOf(stringaInv.getDescrizione()));
				if (inventario != null) {
						//predisposizione per agg inv
						Tb_titolo tit = inventario.getB();
						if (tit != null) {

							// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
//							if ((input.getFiltroCodNatura() != null && !input.getFiltroCodNatura().trim().equals(""))
//									|| (input.getFiltroLivAut() != null && !input.getFiltroLivAut().trim().equals(""))){
							if ((input.getFiltroCodNatura() != null && !input.getFiltroCodNatura().trim().equals(""))
									|| (input.getFiltroLivAut() != null && !input.getFiltroLivAut().trim().equals(""))
									|| (input.getTipoDataPubb() != null && !input.getTipoDataPubb().trim().equals(""))
									|| (input.getAaPubbFrom() != null && !input.getAaPubbFrom().trim().equals(""))){
								if (!input.getFiltroCodNatura().trim().equals("")){
									//inserire modifica su natura w e m
									//se la natura di input è 'w' o 's' il record deve essere aggiornato ugualmente
									if (input.getFiltroCodNatura().equals("W")){
										//deve aggiornare anche 'M'
										if ((tit.getCd_natura() == input.getFiltroCodNatura().trim().toUpperCase().codePointAt(0)) ||
												tit.getCd_natura() == 'M'){
											//aggiorna
										}
									}else if (input.getFiltroCodNatura().equals("M")){
										//deve aggiornare anche 'W'
										if ((tit.getCd_natura() == input.getFiltroCodNatura().trim().toUpperCase().codePointAt(0)) ||
												tit.getCd_natura() == 'W'){
											//aggiorna
										}
									}else if (input.getFiltroCodNatura().equals("S")){
										if (tit.getCd_natura() != input.getFiltroCodNatura().trim().toUpperCase().codePointAt(0)){
//											output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
//											output.getErrori().add("natura del bid dell'inventario " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven() + " diversa da natura di input");
											continue;
										}
											//altrimenti aggiorna
									}else{
										continue;
									}

								}
								if (!input.getFiltroLivAut().trim().equals("")){
									if (!tit.getCd_livello().equals(input.getFiltroLivAut().toUpperCase().trim())){
//										output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
//										output.getErrori().add("livello autorità del bid dell'inventario " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven() + " e diverso da livello di autorità di input");
										continue;
									}//altrimenti aggiorna
								}

								if (!input.getTipoDataPubb().trim().equals("")) {
									if (tit.getTp_aa_pubb() != input.getTipoDataPubb().toUpperCase().charAt(0)){
										continue;
									}//altrimenti aggiorna
								}

								if (!input.getAaPubbFrom().trim().equals("")) {
									if (tit.getAa_pubb_1() == null) {
										continue;
									} else if (tit.getAa_pubb_1().endsWith("..")) {
										if (Integer.valueOf(tit.getAa_pubb_1().substring(0,2)) < Integer.valueOf(input.getAaPubbFrom().substring(0,2))
											|| Integer.valueOf(tit.getAa_pubb_1().substring(0,2)) > Integer.valueOf(input.getAaPubbTo().substring(0,2))) {
											continue;
										}
									}  else if (tit.getAa_pubb_1().endsWith(".")) {
										if (Integer.valueOf(tit.getAa_pubb_1().substring(0,3)) < Integer.valueOf(input.getAaPubbFrom().substring(0,3))
												|| Integer.valueOf(tit.getAa_pubb_1().substring(0,3)) > Integer.valueOf(input.getAaPubbTo().substring(0,3))) {
												continue;
										}
									}  else if (Integer.valueOf(tit.getAa_pubb_1()) < Integer.valueOf(input.getAaPubbFrom())
											|| Integer.valueOf(tit.getAa_pubb_1()) > Integer.valueOf(input.getAaPubbTo())) {
										continue;
									} //altrimenti aggiorna
								}


//								if (!input.getAaPubbFrom().trim().equals("")) {
//									if ((inventario.getB().getAa_pubb_1() == null) || Integer.valueOf(inventario.getB().getAa_pubb_1()) < Integer.valueOf(input.getAaPubbFrom())
//											|| Integer.valueOf(inventario.getB().getAa_pubb_1()) > Integer.valueOf(input.getAaPubbTo())){
//										continue;
//									}//altrimenti aggiorna
//								}


							}
							numRecAggiornati = aggiornaDisponibilitaInventario(
									input, output, numRecAggiornati, inventario);
							DaoManager.commit(tx);
							//
							if ((numRecAggiornati % 500) == 0){
								logg.debug("Finora " + ts + "sono stati aggiornati "+ numRecAggiornati + " records");
								logg.debug("-- ultimo inventario aggiornato: " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven());
								continue;
							}
						}else{
							output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
							output.getErrori().add("inventario " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven() + " con bid " + tit.getBid() + " non presente sul DB di Polo");
							continue;
						}
				}else{
					output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
					output.getErrori().add("inventario = null");
					continue;
				}
			} catch (Exception e) {
				log.error("", e);
				output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
				output.getErrori().add("ECCEZIONE: " + e.getMessage());
			}
		}
		output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
		output.getErrori().add("Numero record aggiornati = " + numRecAggiornati);
		output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		return numRecAggiornati;
	}

	/**
	 * @param input
	 * @param output
	 * @param numRecAggiornati
	 * @param inventario
	 * @return
	 * @throws DaoManagerException
	 */
	private int aggiornaDisponibilitaInventario(AggDispVO input,
			AggDispVO output, int numRecAggiornati, Tbc_inventario inventario)
			throws DaoManagerException {
		inventario.setUte_var(input.getCodPolo()+input.getCodBib()+input.getUser());
		inventario.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
		if (input.getCodNoDispo() != null && !input.getCodNoDispo().trim().equals("")){
			if (input.getCodNoDispo() != null && input.getCodNoDispo().trim().equals("Z")){
				inventario.setCd_no_disp(" ");
				inventario.setData_redisp_prev(null);
			}else{
				inventario.setCd_no_disp(input.getCodNoDispo().trim().toUpperCase());
			}
		}
		if (input.getCodTipoFruizione() != null && !input.getCodTipoFruizione().trim().equals("")){
			inventario.setCd_frui(input.getCodTipoFruizione().trim().toUpperCase());
		}
		if (input.getCodRip() != null && !input.getCodRip().trim().equals("")){
			if (input.getCodRip() != null && input.getCodRip().trim().equals("Z")){
				inventario.setCd_riproducibilita(" ");
			}else{
				inventario.setCd_riproducibilita(input.getCodRip().trim().toUpperCase());
			}
		}
		if (input.getCodiceStatoConservazione() != null && !input.getCodiceStatoConservazione().trim().equals("")){
			inventario.setStato_con(input.getCodiceStatoConservazione().trim().toUpperCase());
		}
		if (input.getCodDigitalizzazione() != null && !input.getCodDigitalizzazione().trim().equals("")){
			inventario.setDigitalizzazione(input.getCodDigitalizzazione().charAt(0));
		}
		daoInv = new Tbc_inventarioDao();
		if(daoInv.modificaInventario(inventario)){
			output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
			output.getErrori().add("aggiornato inventario " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven());
			numRecAggiornati++;
		}
		return numRecAggiornati;
	}

	/**
	 * @param input
	 * @param output
	 * @param dataIngressoDa
	 * @param dataIngressoA
	 * @param numRecAggiornati
	 * @param listaInvData
	 * @return
	 * @throws DaoManagerException
	 */
	private int trattamentoScaricaInv(ScaricoInventarialeVO input,
			ScaricoInventarialeVO output, List<CodiceVO> listaInventari, int numRecScaricati, Logger logger) throws DaoManagerException {
		UserTransaction tx = context.getUserTransaction();

		Timestamp ts = DaoManager.now();
		Logger logg = logger;
		int loop = 0;
		CodiceVO ultKeyInv = null;
		daoInv = new Tbc_inventarioDao();

		for (int i = 0; i < listaInventari.size(); i++) {
			CodiceVO stringaInv = listaInventari.get(i);
			try{
				BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
				DaoManager.begin(tx);

				Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), stringaInv.getCodice(), Integer.valueOf(stringaInv.getDescrizione()));
				if (inventario != null){
					// controllo se è in prestito
					// richiama un ejb di servizi che conta i movimenti aperti
					int movA = servizi.getNumeroMovimentiAttivi(inventario.getCd_serie()
							.getCd_polo().getCd_polo().getCd_polo(), inventario
							.getCd_serie().getCd_polo().getCd_biblioteca(),
							inventario.getCd_serie().getCd_serie(), inventario
							.getCd_inven());
					if (movA > 0) {
						// inventario in prestito
						output.getErrori().add("Inventario '" + inventario.getCd_serie().getCd_serie() + inventario
								.getCd_inven() + "' con movimenti aperti non scaricato");
						continue;
					}
					//almaviva5_20101026 cancellazione legami a fascicoli (solo se natura='S')
					String uteIns = input.getCodPolo()+input.getCodBib()+input.getUser();
					if (ValidazioneDati.equals(inventario.getB().getCd_natura(), 'S')) {
						PeriodiciDAO pDao = new PeriodiciDAO();
						pDao.deleteLegamiEsemplariFascicolo(inventario, uteIns);
					}
					//
					boolean scolloca = false;
					if (inventario.getKey_loc() != null){
						numRecScaricati = trattamentoScaricaInvConKeyLoc(input,
								output, numRecScaricati, inventario);
					}else{
						numRecScaricati = trattamentoScaricaInvSenzaKeyLoc(
								input, output, inventario, numRecScaricati);
					}
					if ((numRecScaricati % 500) == 0){
						logg.debug("Finora " + ts + "sono stati aggiornati "+ numRecScaricati + " records");
						logg.debug("-- ultimo inventario aggiornato: " +  inventario.getCd_serie().getCd_serie() +  inventario.getCd_inven());
						continue;
					}
				}
			} catch (Exception e) {
				log.error("", e);
				output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
				output.getErrori().add("ECCEZIONE: " + e.getMessage());
			}
		}
		output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
		output.getErrori().add("Ora fine = " + DateUtil.formattaDataOra(new java.sql.Timestamp(System.currentTimeMillis())));
		return numRecScaricati;
	}

	/**
	 * @param input
	 * @param output
	 * @param numRecScaricati
	 * @param inventario
	 * @return
	 * @throws DaoManagerException
	 * @throws ResourceNotFoundException
	 * @throws DataException
	 * @throws ValidationException
	 * @throws RemoteException
	 */
	private int trattamentoScaricaInvConKeyLoc(ScaricoInventarialeVO input,
			ScaricoInventarialeVO output, int numRecScaricati,
			Tbc_inventario inventario) throws DaoManagerException,
			ResourceNotFoundException, DataException, ValidationException,
			RemoteException {
		boolean scolloca;
		//si scolloca
		InventarioVO recInv = new InventarioVO();
		recInv.setCodPolo(inventario.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
		recInv.setCodBib(inventario.getCd_serie().getCd_polo().getCd_biblioteca());
		recInv.setCodSerie(inventario.getCd_serie().getCd_serie());
		recInv.setCodInvent(inventario.getCd_inven());

		if (input.getMotivoDelloScarico().trim().equals("T")){
			if (Integer.valueOf(input.getVersoBiblioteca()) > 0){
				daoBiblio = new Tbf_bibliotecaDao();
				Tbf_biblioteca biblioteca = daoBiblio.getBiblioteca(Integer.valueOf(input.getVersoBiblioteca()));
				if (biblioteca != null){
					recInv.setIdBibScar(biblioteca.getId_biblioteca());
					recInv.setCodBibS(biblioteca.getCd_bib().toUpperCase());
					if (biblioteca.getCd_polo() != null){
						recInv.setCodPoloScar(biblioteca.getCd_polo().trim().toUpperCase());
					}
				}else{
					output.getErrori().add("La biblioteca verso cui effettuare il trasferimento contiene un valore non valido");
				}
			}else{
				output.getErrori().add("La biblioteca verso cui effettuare il trasferimento contiene un valore non valido");
			}
		}else{
			recInv.setIdBibScar(0);
			recInv.setCodBibS("");
			recInv.setCodPoloScar("");
		}
		recInv.setCodScarico(input.getMotivoDelloScarico());
		recInv.setNumScarico(String.valueOf(input.getNumBuonoScarico()));
		recInv.setDeliberaScarico(input.getTestoDelibera());
		//
		recInv.setDataScarico(input.getDataScarico());
		//
		if (input.getDataDelibera().trim().equals("")
				|| input.getDataDelibera().trim().equals("00/00/0000")){
			recInv.setDataDelibScar("9999-12-31");
		}else{
			recInv.setDataDelibScar(input.getDataDelibera());
		}
		recInv.setCodSit(DocFisico.Inventari.INVENTARIO_DISMESSO);//dismesso
		recInv.setFlagDisp("N");
		//
		recInv.setUteAgg(input.getCodPolo()+input.getCodBib()+input.getUser());

		CollocazioneVO recColl = new CollocazioneVO();
		recColl.setKeyColloc(inventario.getKey_loc().getKey_loc());
		recColl.setConsistenza(inventario.getKey_loc().getConsis().trim());
		recColl.setUteAgg(input.getCodPolo()+input.getCodBib()+input.getUser());
		scolloca = this.inventario.updateCollocazioneScolloca(recInv, recColl, "scarico", input.getTicket());
		if (scolloca)
			numRecScaricati++;
		output.getErrori().add("Inventario "+inventario.getCd_serie().getCd_serie()+inventario.getCd_inven()+" scaricato");
		return numRecScaricati;
	}


	/**
	 * @param input
	 * @param output
	 * @param dataIngressoDa
	 * @param dataIngressoA
	 * @param numRecAggiornati
	 * @param listaInvData
	 * @return
	 * @throws DaoManagerException
	 */
	private int trattamentoScaricaInventario(ScaricoInventarialeVO input,
			ScaricoInventarialeVO output, Tbc_inventario inventario, int numRecScaricati) throws DaoManagerException {
		try{
			if (inventario != null){

				boolean scolloca = false;
				if (inventario.getKey_loc() != null){
					numRecScaricati = trattamentoScaricaInvConKeyLoc(input,
							output, numRecScaricati, inventario);

				}else{
					numRecScaricati = trattamentoScaricaInvSenzaKeyLoc(input,
							output, inventario, numRecScaricati);
				}

			}else{
				output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
				output.getErrori().add("Inventario "+inventario.getCd_serie().getCd_serie()+inventario.getCd_inven()+" non presente in base dati");
			}
		} catch (Exception e) {
			log.error("", e);
			output.getErrori().add("------------------------------------------------------------------------------------------------------------------------");
			output.getErrori().add("ECCEZIONE: " + e.getMessage());
		}
		return numRecScaricati;
	}

	/**
	 * @param input
	 * @param output
	 * @param inventario
	 * @param numRecScaricati
	 * @return
	 * @throws DaoManagerException
	 */
	private int trattamentoScaricaInvSenzaKeyLoc(ScaricoInventarialeVO input,
			ScaricoInventarialeVO output, Tbc_inventario inventario,
			int numRecScaricati) throws DaoManagerException {
		//
		if (input.getMotivoDelloScarico().trim().equals("T")){
			if (Integer.valueOf(input.getVersoBiblioteca()) > 0){
				daoBiblio = new Tbf_bibliotecaDao();
				Tbf_biblioteca biblioteca = daoBiblio.getBiblioteca(Integer.valueOf(input.getVersoBiblioteca()));
				if (biblioteca != null){
					inventario.setId_bib_scar(biblioteca.getId_biblioteca());
					inventario.setCd_bib_scar(biblioteca.getCd_bib().toUpperCase());
					if (biblioteca.getCd_polo() != null){
						inventario.setCd_polo_scar(biblioteca.getCd_polo().trim().toUpperCase());
					}
				}else{
					output.getErrori().add("La biblioteca verso cui effettuare il trasferimento contiene un valore non valido");
				}
			}else{
				output.getErrori().add("La biblioteca verso cui effettuare il trasferimento contiene un valore non valido");
			}
		}else{
			inventario.setId_bib_scar(null);
			inventario.setCd_bib_scar(" ");
			inventario.setCd_polo_scar(" ");
		}
		inventario.setCd_scarico(input.getMotivoDelloScarico().charAt(0));
		inventario.setNum_scarico(input.getNumBuonoScarico());
		inventario.setDelibera_scar(input.getTestoDelibera());
		//
		inventario.setData_scarico(DateUtil.toDate(input.getDataScarico()));
		//
		if (input.getDataDelibera().trim().equals("")
				|| input.getDataDelibera().trim().equals("00/00/0000")){
			inventario.setData_delibera(Date.valueOf("9999-12-31"));
		}
		inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_DISMESSO_CHR);//dismesso
		inventario.setFlg_disp('N');
		//
		inventario.setUte_var(input.getCodPolo()+input.getCodBib()+input.getUser());
		inventario.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
		if(daoInv.modificaInventario(inventario)){
			numRecScaricati++;
			output.getErrori().add("Inventario "+inventario.getCd_serie().getCd_serie()+inventario.getCd_inven()+" scaricato");
		}
		return numRecScaricati;
	}

	/**
	 * @param input
	 * @param ts
	 * @throws DaoManagerException
	 * @throws DataException
	 */
	private void calcolaInvA(AggDispVO input, Timestamp ts) throws DaoManagerException, DataException {
		if (input.getEndInventario().trim().equals("") || input.getEndInventario().equals("00/00/0000")){
			input.setEndInventario(DateUtil.formattaData(ts.getTime()));
		}
		//non ho il codice inventario impostato quindi cerco il numero massimo
		if (input.getEndInventario().equals("")){
			daoInv = new Tbc_inventarioDao();
			int numInvMinMax = daoInv.getMaxInventario(input.getCodPolo(), input.getCodBib(), input.getSerie(), input.getFiltroDataIngressoA());
			if (numInvMinMax > 0){
				input.setFiltroDataIngressoA(String.valueOf(numInvMinMax));
			}else{
				throw new DataException("erroreNumInvMinMax");
			}
		}
	}

	/**
	 * @param input
	 * @throws DaoManagerException
	 * @throws DataException
	 */
	private void calcolaInvDa(AggDispVO input) throws DaoManagerException, DataException {
		if (input.getStartInventario().equals("")){
			input.setStartInventario("0");
			//non ho il codice inventario impostato quindi cerco il numero minimo
			daoInv = new Tbc_inventarioDao();
			int numInvMinMax = daoInv.getMinInventario(input.getCodPolo(), input.getCodBib(), input.getSerie(), input.getFiltroDataIngressoDa());
			if (numInvMinMax > 0){
				input.setFiltroDataIngressoDa(String.valueOf(numInvMinMax));
			}else{
				throw new DataException("erroreNumInvMinMax");
			}
		}
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	//per stampa schede

	public List getBid(AreaParametriStampaSchedeVo input, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{

		List<CodiceVO> output = new ArrayList<CodiceVO>();
		AreaParametriStampaSchedeVo outputSt = new AreaParametriStampaSchedeVo(input);
		HashSet<CodiceVO> hm = new HashSet<CodiceVO>();
		List listaBid = new ArrayList();
		//		CodiceVO outputDettaglio = null;
		String locNormalizzata = null;
		String aLocNormalizzata = null;
		String specNormalizzata = null;
		String aSpecNormalizzata = null;
		Tbc_sezione_collocazione sezioneDa = null;
		Tbc_sezione_collocazione sezioneA = null;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		Logger log1 = Logger.getLogger(input.getFirmaBatch());

		UserTransaction tx = context.getUserTransaction();
		try{
			DaoManager.begin(tx);
			if (tipoOperazione.equals("R")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				if (outputSt != null){
					daoInv = new Tbc_inventarioDao();
					List listaInvDaA = daoInv.getListaInventariDaA(input.getCodPolo(), input.getCodBib(),
							input.getSerie(), input.getStartInventario(), input.getEndInventario());
					if (listaInvDaA.size() > 0){
						for (int index = 0; index < listaInvDaA.size(); index++) {
							Tbc_inventario inventario = (Tbc_inventario) listaInvDaA.get(index);
							if (inventario != null){
								hm.add(new CodiceVO(inventario.getB().getBid()));
							}
						}
						output.addAll(hm);
					}else{
						log1.debug("ERRORE: tipo operazione N e lista inventari vuota");
					}
				}else{
					log1.debug("ERRORE: tipo operazione I - errore in fase di validazione dati di input");
				}
			}else if (tipoOperazione.equals("N")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				List listaInventariInput = input.getListaInventari();
				if (listaInventariInput != null && listaInventariInput.size() > 0){
					for (int y=0; y<listaInventariInput.size(); y++) {
						CodiceVO elem = (CodiceVO) listaInventariInput.get(y);
						daoInv = new Tbc_inventarioDao();
						Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), elem.getCodice(), Integer.valueOf(elem.getDescrizione()));
						if (inventario != null){
							if (inventario.getFl_canc() != 'S'){
								hm.add(new CodiceVO(inventario.getB().getBid()));
							}
						}else{
							log1.debug("ATTENZIONE: Inventario "+elem.getCodice()+" "+ elem.getDescrizione()+" non esistente o cancellato");
						}
						//							}
					}
				}else{
					log1.debug("ERRORE: tipo operazione N e lista inventari vuota");
				}
				output.addAll(hm);
			}else if (tipoOperazione.equals("S")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				List listaInvInput = new ArrayList();
				daoSez = new Tbc_sezione_collocazioneDao();
				Tbc_sezione_collocazione sezione = daoSez.getSezione(input.getCodPolo(), input.getCodBib(), input.getSezione());
				if (sezione != null){
//					CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
//							false, input.getDallaSpecificazione(), input.getAllaSpecificazione(), false, "");
					CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
							 input.getDallaSpecificazione(), input.getAllaSpecificazione(),  "");
					daoColl = new Tbc_collocazioneDao();
					//almaviva5_20090708
					List listaCollocazioni = daoColl.getCollocazioniPerEtichette(input.getCodPolo(),input.getCodBib(), sezione.getCd_sez(),
							collSpec.getDaColl(), collSpec.getAColl(), collSpec.getDaSpec(), collSpec.getASpec() );
					if (listaCollocazioni.size() > 0){
						for (int y=0; y<listaCollocazioni.size(); y++) {
							Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioni.get(y);
							daoInv = new Tbc_inventarioDao();
							List<Tbc_inventario> listaInv = daoInv.getListaInventari(collocazione, "I");//I=ordinamento per inv
							if (listaInv.size() > 0) {
								//								outputDettaglio = new CodiceVO();
								for (int index = 0; index < listaInv.size(); index++) {
									Tbc_inventario inventario = listaInv.get(index);
									if (inventario != null){
										hm.add(new CodiceVO(inventario.getB().getBid()));
									}
								}
							}else{
								log1.debug("ERRORE: tipo operazione S e lista inventari vuota");
							}
						}
					}else{
						log1.debug("ERRORE: tipo operazione S e lista collocazioni vuota");
					}
				}else{
					log1.debug("ERRORE: tipo operazione S e sezione non presente nel DB");
				}
				output.addAll(hm);
			}else{
				log1.debug("ERRORE: tipo operazione non valido");
			}
			DaoManager.commit(tx);
			log.debug("Commit transazione");
		}catch (ValidationException e) {
			log.error("", e);
			throw new DataException (e);
		}catch (DaoManagerException e) {
			log.error("", e);
			throw new DataException (e);
		} catch (Exception e) {
			log.error("", e);
			throw new DataException (e);
		}
		listaBid.addAll(output);
		return listaBid;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	//per stampa schede

	public List getBid2(AreaParametriStampaSchedeVo input, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{

		List<BidInventarioSegnaturaVO> output = new ArrayList<BidInventarioSegnaturaVO>();
		AreaParametriStampaSchedeVo outputSt = new AreaParametriStampaSchedeVo(input);
		HashSet<BidInventarioSegnaturaVO> hm = new HashSet<BidInventarioSegnaturaVO>();
		List listaBid = new ArrayList();
		//		CodiceVO outputDettaglio = null;
		String locNormalizzata = null;
		String aLocNormalizzata = null;
		String specNormalizzata = null;
		String aSpecNormalizzata = null;
		Tbc_sezione_collocazione sezioneDa = null;
		Tbc_sezione_collocazione sezioneA = null;
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		Logger log1 = Logger.getLogger(input.getFirmaBatch());
		List listaInvDaA = new ArrayList();

		UserTransaction tx = context.getUserTransaction();
		try{
			DaoManager.begin(tx);
			if (tipoOperazione.equals("R")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				if (outputSt != null){
					daoInv = new Tbc_inventarioDao();
					listaInvDaA = daoInv.getListaInventariDaA(input.getCodPolo(), input.getCodBib(),
							input.getSerie(), input.getStartInventario(), input.getEndInventario());
					if (listaInvDaA.size() > 0){
						output = new ArrayList();
						trattamentoDatiInventario(output, listaInvDaA);
						output.addAll(hm);
					}else{
						log1.debug("ERRORE: tipo operazione N e lista inventari vuota");
					}
				}else{
					log1.debug("ERRORE: tipo operazione I - errore in fase di validazione dati di input");
				}
			}else if (tipoOperazione.equals("N")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				List listaInventariInput = input.getListaInventari();
				if (listaInventariInput != null && listaInventariInput.size() > 0){
					output = new ArrayList();
					for (int y=0; y<listaInventariInput.size(); y++) {
						CodiceVO elem = (CodiceVO) listaInventariInput.get(y);
						daoInv = new Tbc_inventarioDao();
						Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), elem.getCodice(), Integer.valueOf(elem.getDescrizione()));
						if (inventario != null){
							listaInvDaA = new ArrayList();
							listaInvDaA.add(inventario);
							trattamentoDatiInventario(output, listaInvDaA);
						}else{
							log1.debug("ATTENZIONE: Inventario "+elem.getCodice()+" "+ elem.getDescrizione()+" non esistente o cancellato");
						}
						//							}
					}
				}else{
					log1.debug("ERRORE: tipo operazione N e lista inventari vuota");
				}
				output.addAll(hm);
			}else if (tipoOperazione.equals("S")){
				outputSt = this.inventario.controllaInputSchede(input, outputSt);
				List listaInvInput = new ArrayList();
				daoSez = new Tbc_sezione_collocazioneDao();
				Tbc_sezione_collocazione sezione = daoSez.getSezione(input.getCodPolo(), input.getCodBib(), input.getSezione());
				if (sezione != null){
					CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
							input.getDallaSpecificazione(), input.getAllaSpecificazione(), "");
					daoColl = new Tbc_collocazioneDao();
					//almaviva5_20090708
					List listaCollocazioni = daoColl.getCollocazioniPerEtichette(input.getCodPolo(),input.getCodBib(), sezione.getCd_sez(),
							collSpec.getDaColl(), collSpec.getAColl(), collSpec.getDaSpec(), collSpec.getASpec() );
					if (listaCollocazioni.size() > 0){
						output = new ArrayList();//almaviva2 2/9/2010
						for (int y=0; y<listaCollocazioni.size(); y++) {
							Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioni.get(y);
							daoInv = new Tbc_inventarioDao();
							List<Tbc_inventario> listaInv = daoInv.getListaInventari(collocazione, "I");//I=ordinamento per inv
							if (listaInv.size() > 0) {
										trattamentoDatiInventario(output, listaInv);
							}else{
								log1.debug("ERRORE: tipo operazione S e lista inventari vuota");
							}
						}
					}else{
						log1.debug("ERRORE: tipo operazione S e lista collocazioni vuota");
					}
				}else{
					log1.debug("ERRORE: tipo operazione S e sezione non presente nel DB");
				}
				output.addAll(hm);
			}else{
				log1.debug("ERRORE: tipo operazione non valido");
			}
			DaoManager.commit(tx);
			log.debug("Commit transazione");
		}catch (ValidationException e) {
			log.error("", e);
			throw new DataException (e);
		}catch (DaoManagerException e) {
			log.error("", e);
			throw new DataException (e);
		} catch (Exception e) {
			log.error("", e);
			throw new DataException (e);
		}
		listaBid.addAll(output);
		return listaBid;
	}

	/**
	 * @param output
	 * @param listaInv
	 */
	private void trattamentoDatiInventario(List<BidInventarioSegnaturaVO> output,
			List<Tbc_inventario> listaInv) {
		for (int index = 0; index < listaInv.size(); index++) {
			BidInventarioSegnaturaVO rec = new BidInventarioSegnaturaVO();
			Tbc_inventario recResult = listaInv.get(index);
			if (recResult.getFl_canc() != 'S'){
				rec.setBid(recResult.getB().getBid());
				rec.setBiblioteca(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
				rec.setSerie(recResult.getCd_serie().getCd_serie());
				rec.setInventario(String.valueOf(recResult.getCd_inven()));
				rec.setSequenza(recResult.getSeq_coll());
				rec.setCodSit(""+recResult.getCd_sit());
				if (recResult.getPrecis_inv().equals("$")){
					rec.setPrecisazione("");
				}else{
					rec.setPrecisazione(recResult.getPrecis_inv());
				}
				if (recResult.getKey_loc() != null) {
					rec.setSezione(recResult.getKey_loc().getCd_sez().getCd_sez());
					rec.setCollocazione(recResult.getKey_loc().getCd_loc());
					rec.setSpecificazione(recResult.getKey_loc().getSpec_loc());
				} else {
					rec.setSezione("");
					rec.setCollocazione("");
					rec.setSpecificazione("");
				}
				output.add(rec);
			}
		}
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws RemoteException
	 * @throws ApplicationException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getSegnature(String codPolo, String codBib,
			String bid, String ticket) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		List listaOutput = new ArrayList();
		List listaOutputInvDiColl = null;
		List listaOutputInvDiEsempl = null;
		List listaInv = new ArrayList();
		UserTransaction tx = context.getUserTransaction();
		try {
			InventarioVO inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setBid(bid);
			valida.validaCodPoloCodBibBid(codPolo, codBib, bid);
			daoInv = new Tbc_inventarioDao();

			DaoManager.begin(tx);
			log.debug("Inizio transazione");

			EtichettaDettaglioVO rec = null;


			boolean [] posseduto = this.collocazione.getDatiPosseduto(codPolo, codBib, bid, ticket);
			//
			List<Tbc_inventario> listaInventariPosseduto = null;
			List listaCollocazioniPosseduto = null;
			if (posseduto[0] != false){
				//tab1 attivo
				daoInv = new Tbc_inventarioDao();
				listaInventariPosseduto = daoInv.getListaInventariTitolo(codPolo, codBib, bid);
				if (listaInventariPosseduto.size() > 0) {
					listaOutput = popolaListaOutputConInv(listaInventariPosseduto);
				}
			}else if (posseduto[1] != false){
				listaCollocazioniPosseduto = this.collocazione.getListaCollocazioniTitoloSimple(codPolo, codBib, bid, ticket);
				if (listaCollocazioniPosseduto.size() > 0)  {
					for (int y=0; y<listaCollocazioniPosseduto.size(); y++) {
						Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioniPosseduto.get(y);
						daoInv = new Tbc_inventarioDao();
						listaInventariPosseduto = daoInv.getListaInventari(collocazione, "I");
						if (listaInventariPosseduto.size() > 0) {
//							for (int index = 0; index < listaInv.size(); index++) {
//								listaOutputInvDiColl = popolaListaOutputConInv(listaInventariPosseduto);
								listaOutput = popolaListaOutputConInv(listaInventariPosseduto);
//							}
						}
//						listaOutput.add(listaOutputInvDiColl);
					}
				}
			}else if (posseduto[2] != false){
				List listaEsemplariPosseduto = this.collocazione.getListaEsemplariTitolo(codPolo, codBib, bid, ticket);
				if (listaEsemplariPosseduto.size() > 0)  {
					for (int y=0; y<listaEsemplariPosseduto.size(); y++) {
//						Tbc_esemplare_titolo esemplare = (Tbc_esemplare_titolo) listaEsemplariPosseduto.get(y);
						EsemplareListaVO esemplare = (EsemplareListaVO) listaEsemplariPosseduto.get(y);

						daoColl = new Tbc_collocazioneDao();
//						listaCollocazioniPosseduto = daoColl.getListaCollocazioniDiEsemplare(esemplare.getCd_polo().getCd_polo().getCd_polo(),
//								esemplare.getCd_polo().getCd_biblioteca(), esemplare.getB().getBid(), esemplare.getCd_doc());
						listaCollocazioniPosseduto = daoColl.getListaCollocazioniDiEsemplare(esemplare.getCodPolo(),
								esemplare.getCodBib(), esemplare.getBid(), esemplare.getCodDoc());
						if (listaCollocazioniPosseduto.size() > 0)  {
							for (int yy=0; yy<listaCollocazioniPosseduto.size(); yy++) {
								Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioniPosseduto.get(yy);
								daoInv = new Tbc_inventarioDao();
								listaInventariPosseduto = daoInv.getListaInventari(collocazione, "I");//I = ordina per inv.
								if (listaInventariPosseduto.size() > 0) {
									for (int index = 0; index < listaInv.size(); index++) {
//										listaOutputInvDiEsempl = popolaListaOutputConInv(listaInventariPosseduto);
										listaOutput = popolaListaOutputConInv(listaInventariPosseduto);
									}
								}
							}
						}
//						listaOutput.add(listaOutputInvDiEsempl);
					}
				}
			}
			if (!posseduto[0] &&
					!posseduto[1] &&
					!posseduto[2] ){
				DaoManager.commit(tx);
				log.debug("Commit transazione");
				return listaOutput = null;
			}else{
				DaoManager.commit(tx);
				log.debug("Commit transazione");
				return listaOutput;
			}

//			if (posseduto[0] &&
//					posseduto[1] &&
//					posseduto[2] ){
//			}else{
//				listaOutput = null;
//			}
//			DaoManager.commit(tx);
//			log.debug("Commit transazione");
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new DataException(e);
		} catch (Exception e) {
			try {
				DaoManager.rollback(tx);
				log.error("Rollback transazione");
			} catch (Exception e1) {
				log.error("", e1);
			}

			log.error("", e);
			throw new DataException(e);
		}
//		return listaOutput;
	}

	/**
	 * @param listaInventariPosseduto
	 * @return
	 */
	private List popolaListaOutputConInv(List<Tbc_inventario> listaInventariPosseduto) {
		List listaOutput;
		SchedaVO rec;
		listaOutput = new ArrayList();
		for (int index = 0; index < listaInventariPosseduto.size(); index++) {
			rec = new SchedaVO();
			Tbc_inventario recResult = listaInventariPosseduto.get(index);
			if (recResult.getFl_canc() != 'S'){
				rec.setBiblioteca(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
				rec.setSerie(recResult.getCd_serie().getCd_serie());
				rec.setInventario(String.valueOf(recResult.getCd_inven()));
				rec.setBidInventario(recResult.getB().getBid());
				rec.setSequenza(recResult.getSeq_coll());
				rec.setCodSit(""+recResult.getCd_sit());
				if (recResult.getPrecis_inv().equals("$")){
					rec.setPrecisazione("");
				}else{
					rec.setPrecisazione(recResult.getPrecis_inv());
				}
				if (recResult.getKey_loc() != null) {
					rec.setSezione(recResult.getKey_loc().getCd_sez().getCd_sez());
					rec.setCollocazione(recResult.getKey_loc().getCd_loc());
					rec.setSpecificazione(recResult.getKey_loc().getSpec_loc());
					rec.setBidCollocazione(recResult.getKey_loc().getB().getBid());
					rec.setOrd_loc(recResult.getKey_loc().getOrd_loc());
					rec.setOrd_spec(recResult.getKey_loc().getSpec_loc());
					rec.setBidCollocazione(recResult.getKey_loc().getB().getBid());
					if (recResult.getKey_loc().getCd_biblioteca_doc() != null){
						rec.setBidEsemplare(recResult.getKey_loc().getCd_biblioteca_doc().getB().getBid());
//						rec.setCodDoc(recResult.getKey_loc().getCd_biblioteca_doc().getCd_doc());
					}
				} else {
					rec.setSezione("");
					rec.setCollocazione("");
					rec.setSpecificazione("");
				}
				listaOutput.add(rec);
			}
		}
		return listaOutput;
	}

//	/**
//	 *
//	 * <!-- begin-xdoclet-definition -->
//	 *
//	 * @throws RemoteException
//	 * @throws ApplicationException
//	 * @throws DataException
//	 * @throws ValidationException
//	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
//	 * @generated
//	 *
//	 * //TODO: Must provide implementation for bean create stub
//	 */
//	public List getSegnature(String codPolo, String codBib,
//			String bid, String ticket) throws RemoteException,
//			ApplicationException, DataException, ValidationException {
//		List listaOutput = null;
//		List listaInv = new ArrayList();
//		UserTransaction transaction = context.getUserTransaction();
//		try {
//			InventarioVO inv = new InventarioVO();
//			inv.setCodPolo(codPolo);
//			inv.setCodBib(codBib);
//			inv.setBid(bid);
//			valida.validaCodPoloCodBibBid(codPolo, codBib, bid);
//			daoInv = new Tbc_inventarioDao();
//
//			DaoManager.begin(tx);
//			log.debug("Inizio transazione");
//
//			EtichettaDettaglioVO rec = null;
//				listaInv = daoInv.getListaInventari(codPolo, codBib, bid);
//			if (listaInv.size() > 0) {
//				listaOutput = new ArrayList();
//				for (int index = 0; index < listaInv.size(); index++) {
//					rec = new EtichettaDettaglioVO();
//					Tbc_inventario recResult = (Tbc_inventario)listaInv.get(index);
//					if (recResult.getFl_canc() != 'S'){
//						rec.setBiblioteca(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
//						rec.setSerie(recResult.getCd_serie().getCd_serie());
//						rec.setInventario(String.valueOf(recResult.getCd_inven()));
//						rec.setSequenza(recResult.getSeq_coll());
//						if (recResult.getPrecis_inv().equals("$")){
//							rec.setPrecisazione("");
//						}else{
//							rec.setPrecisazione(recResult.getPrecis_inv());
//						}
//						if (recResult.getKey_loc() != null) {
//							rec.setSezione(recResult.getKey_loc().getCd_sez().getCd_sez());
//							rec.setCollocazione(recResult.getKey_loc().getCd_loc());
//							rec.setSpecificazione(recResult.getKey_loc().getSpec_loc());
//						} else {
//							rec.setSezione("");
//							rec.setCollocazione("");
//							rec.setSpecificazione("");
//						}
//						listaOutput.add(rec);
//					}
//				}
//			}
////			else{//almaviva2 20091111
////				throw new DataException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO, "noInv");
////			}
//			DaoManager.commit(tx);
//			log.debug("Commit transazione");
//		} catch (ValidationException e) {
//			throw e;
////		} catch (DataException e) {
////			try {
////				DaoManager.rollback(tx);
////				log.error("Rollback transazione");
////			} catch (Exception e1) {
////				e1.printStackTrace();
////			}
////			throw e;
//		} catch (DaoManagerException e) {
//			log.error("", e);
//			throw new DataException(e);
//		} catch (Exception e) {
//			try {
//				DaoManager.rollback();
//				log.error("Rollback transazione");
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//
//			log.error("", e);
//			throw new DataException(e);
//		}
//		return listaOutput;
//	}


	/**
	 * Esegue insert e update su db.
	 *
	 * @param query query da eseguire
	 * @param param parametri da impostare nella query (null se inesistenti)
	 * @param count nr. di esecuzioni (commit transaction ogni 50)
	 * @throws DaoManagerException
	 */
	public void executeInsertUpdateImport(List<QueryData> queryData) throws EJBException {

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			DaoManager.begin(tx);

			Session session = daoBiblio.getCurrentSession();

			for (QueryData qd : queryData) {
				SQLQuery sqlQuery = session.createSQLQuery(qd.query());
				String param = qd.param();
				if (ValidazioneDati.isFilled(param))
					sqlQuery.setParameter("param", param);

				sqlQuery.executeUpdate();
			}
			session.flush();
			session.clear();
			ok = true;

		} catch (Exception e) {
			throw new EJBException(e);

		} finally {
			DaoManager.endTransaction(tx, ok);
		}
	}

	public ElaborazioniDifferiteOutputVo acquisizioneUriCopiaDigitale(
			AcquisizioneUriCopiaDigitaleVO richiesta, BatchLogWriter blog)
			throws ApplicationException, ValidationException, EJBException {

		AcquisizioneUriCopiaDigitale batch = new AcquisizioneUriCopiaDigitale(richiesta, blog);
		UserTransaction tx = context.getUserTransaction();
		return batch.execute(tx);

	}
}
