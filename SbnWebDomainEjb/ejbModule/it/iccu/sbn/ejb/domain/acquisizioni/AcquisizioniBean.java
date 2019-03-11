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
package it.iccu.sbn.ejb.domain.acquisizioni;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CalcoliVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheOrdiniStampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQAreeIsbdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.persistence.dao.acquisizioni.AcquisizioniBaseDAO.ChiaveOrdine;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniEditoreDAO;
import it.iccu.sbn.persistence.dao.acquisizioni.Picos;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_buono_ordineDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_cambi_ufficialiDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_fattureDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_fornitoriDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_profili_acquistoDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_richieste_offertaDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_sez_acquis_bibliograficheDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_suggerimenti_bibliograficiDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tbb_bilanciDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tbl_documenti_lettoriAcqDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tra_messaggiDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.periodici.PeriodiciDAO;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_buono_ordine;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_richieste_offerta;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_righe_fatture;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_suggerimenti_bibliografici;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori_biblioteche;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_elementi_buono_ordine;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_fornitori_offerte;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_messaggi;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordini_biblioteche;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_sez_acq_storico;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_sez_acquisizione_fornitori;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.vo.validators.acquisizioni.Validazione;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Acquisizioni" description="A session bean named
 *           Acquisizioni" display-name="Acquisizioni"
 *           jndi-name="sbnWeb/Acquisizioni" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class AcquisizioniBean extends TicketChecker implements	Acquisizioni {

	private static final long serialVersionUID = -6291747816913216450L;

	private static Logger log = Logger.getLogger(Acquisizioni.class);

	private GenericJDBCAcquisizioniDAO dao;
	private GenericJDBCAcquisizioniEditoreDAO daoEditore;
	private Tba_fornitoriDao daoFornitore;
	private Inventario inventario;
	private PeriodiciSBN periodici;

	SessionContext ctx;

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext context) throws EJBException,
			RemoteException {
		this.ctx = context;
		dao = new GenericJDBCAcquisizioniDAO();
		daoEditore = new GenericJDBCAcquisizioniEditoreDAO();
	}

	private Inventario getInventario() throws Exception {
		if (inventario == null) {
			inventario = DomainEJBFactory.getInstance().getInventario();
		}
		return inventario;
	}

	private PeriodiciSBN getPeriodici() throws Exception {
		if (periodici == null) {
			periodici = DomainEJBFactory.getInstance().getPeriodici();
		}

		return periodici;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaFatture(ListaSuppFatturaVO ricercaFatture)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {

		Boolean testxHib = false;
		List listaFatture = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaFatture(ricercaFatture);
		} else {
			try {
				Tba_fattureDao fattureDao = new Tba_fattureDao();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				ValidaListaSuppFatturaVO(ricercaFatture);
				results = fattureDao.getRicercaListaFattureHib(ricercaFatture);
				int totFatt = 0;
				if (results != null && results.size() > 0) {
					totFatt = results.size();
					ConfigurazioneORDVO apConfORDNew = new ConfigurazioneORDVO();
					apConfORDNew.setCodPolo(ricercaFatture.getCodPolo());
					apConfORDNew.setCodBibl(ricercaFatture.getCodBibl());
					Boolean gestBil = true;
					try {
						apConfORDNew = ordiniDao
								.loadConfigurazioneOrdiniHib(apConfORDNew);
						if (apConfORDNew != null
								&& !apConfORDNew.isGestioneBilancio()) {
							gestBil = false;
						}
					} catch (Exception e) {
						log.error("", e);
					}

					int numRighe = 0;
					int progrRighe = 1; // inizializzato e si incrementa solo
					// quando si fa add all'array

					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						FatturaVO rec = new FatturaVO();
						Tba_fatture oggFATT = (Tba_fatture) results.get(i);
						// inizio
						// configurazione
						rec.setGestBil(true);
						if (!gestBil) {
							rec.setGestBil(false);
						}
						rec.setIDFatt(oggFATT.getId_fattura());
						Tbf_biblioteca_in_polo appoBib = oggFATT.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setDenoBibl(appoBib.getDs_biblioteca());
						// rec.setProgressivo(numRighe);
						rec.setProgressivo(progrRighe);
						rec
								.setAnnoFattura(oggFATT.getAnno_fattura()
										.toString());
						rec.setProgrFattura(String.valueOf(oggFATT
								.getProgr_fattura()));
						rec.setNumFattura(oggFATT.getNum_fattura());

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggFATT
									.getData_fattura());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataFattura(dataFormattata);

						dataFormattata = "";
						try {
							dataFormattata = format1.format(oggFATT
									.getData_reg());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataRegFattura(dataFormattata);

						rec.setImportoFattura(oggFATT.getImporto()
								.doubleValue());
						rec.setValutaFattura(oggFATT.getValuta());
						rec.setCambioFattura(oggFATT.getCambio().doubleValue());
						rec.setStatoFattura(String.valueOf(oggFATT
								.getStato_fattura()));
						rec.setDenoStatoFattura("");
						if (rec.getStatoFattura() != null
								&& rec.getStatoFattura().trim().length() > 0) {
							if (rec.getStatoFattura().trim().equals("1")) {
								rec.setDenoStatoFattura("Registrata");
							} else if (rec.getStatoFattura().trim().equals("2")) {
								rec.setDenoStatoFattura("Controllata");
							} else if (rec.getStatoFattura().trim().equals("3")) {
								rec
										.setDenoStatoFattura("Ordine di pagamento emesso");
							} else if (rec.getStatoFattura().trim().equals("4")) {
								rec.setDenoStatoFattura("Contabilizzata");
							}
						}

						rec.setTipoFattura(String.valueOf(oggFATT
								.getTipo_fattura()));
						rec.setFornitoreFattura(new StrutturaCombo("", ""));
						rec.getFornitoreFattura().setCodice(
								String.valueOf(oggFATT.getCod_fornitore()
										.getCod_fornitore()));
						rec.getFornitoreFattura().setDescrizione(
								oggFATT.getCod_fornitore().getNom_fornitore());

						// dati fornitore per stampe
						rec.setAnagFornitore(new FornitoreVO());
						rec.getAnagFornitore().setCodFornitore(
								String.valueOf(oggFATT.getCod_fornitore()
										.getCod_fornitore()));
						rec.getAnagFornitore().setNomeFornitore(
								oggFATT.getCod_fornitore().getNom_fornitore());
						if (oggFATT.getCod_fornitore().getIndirizzo() != null
								&& oggFATT.getCod_fornitore().getIndirizzo()
										.trim().length() > 0) {
							rec.getAnagFornitore().setIndirizzo(
									oggFATT.getCod_fornitore().getIndirizzo());
						}
						if (oggFATT.getCod_fornitore().getCitta() != null
								&& oggFATT.getCod_fornitore().getCitta().trim()
										.length() > 0) {
							rec.getAnagFornitore().setCitta(
									oggFATT.getCod_fornitore().getCitta());
						}
						if (rec.getAnagFornitore().getCap() != null
								&& rec.getAnagFornitore().getCap().trim()
										.length() > 0) {
							rec.getAnagFornitore().setCap(
									oggFATT.getCod_fornitore().getCap());
						}
						if (rec.getAnagFornitore().getPaese() != null
								&& rec.getAnagFornitore().getPaese().trim()
										.length() > 0) {
							rec.getAnagFornitore().setPaese(
									oggFATT.getCod_fornitore().getPaese());
						}

						if (rec.getAnagFornitore().getPartitaIva() != null
								&& rec.getAnagFornitore().getPartitaIva()
										.trim().length() > 0) {
							rec.getAnagFornitore().setPartitaIva(
									oggFATT.getCod_fornitore().getP_iva());
						}
						if (rec.getAnagFornitore().getCodiceFiscale() != null
								&& rec.getAnagFornitore().getCodiceFiscale()
										.trim().length() > 0) {
							rec.getAnagFornitore()
									.setCodiceFiscale(
											oggFATT.getCod_fornitore()
													.getCod_fiscale());
						}
						if (rec.getAnagFornitore().getFax() != null
								&& rec.getAnagFornitore().getFax().trim()
										.length() > 0) {
							rec.getAnagFornitore().setFax(
									oggFATT.getCod_fornitore().getFax());
						}
						if (rec.getAnagFornitore().getTelefono() != null
								&& rec.getAnagFornitore().getTelefono().trim()
										.length() > 0) {
							rec.getAnagFornitore().setTelefono(
									oggFATT.getCod_fornitore().getTelefono());
						}
						rec.getAnagFornitore().setFornitoreBibl(
								new DatiFornitoreVO());

						rec.setScontoFattura(oggFATT.getSconto().doubleValue());
						rec.setDataUpd(oggFATT.getTs_var());
						List<StrutturaFatturaVO> arrayFattDett = new ArrayList<StrutturaFatturaVO>();

						// recupero le righe di dettaglio della fattura
						if (oggFATT.getTba_righe_fatture() != null) {
							try {
								Object[] elencoFattDett = oggFATT
										.getTba_righe_fatture().toArray();
								for (int j = 0; j < elencoFattDett.length; j++) {
									Tba_righe_fatture eleFattDett = (Tba_righe_fatture) elencoFattDett[j];
									StrutturaFatturaVO newEleFattDett = new StrutturaFatturaVO();

									appoBib = eleFattDett.getCd_biblioteca();
									newEleFattDett.setCodBibl(appoBib
											.getCd_biblioteca());
									newEleFattDett.setCodPolo(appoBib
											.getCd_polo().getCd_polo());
									newEleFattDett.setIDFatt(eleFattDett
											.getId_fattura().getId_fattura());
									newEleFattDett.setProgrRigaFattura(j + 1); //
									newEleFattDett.setRigaFattura(eleFattDett
											.getRiga_fattura());

									newEleFattDett.setIDOrd(eleFattDett
											.getId_ordine().getId_ordine());
									if (eleFattDett.getCod_mat() != null
											&& eleFattDett.getCod_mat()
													.getId_capitoli_bilanci() != null) {
										newEleFattDett.setIDBil(eleFattDett
												.getCod_mat()
												.getId_capitoli_bilanci()
												.getId_capitoli_bilanci());
									}

									newEleFattDett
											.setImportoRigaFattura(eleFattDett
													.getImporto_riga()
													.doubleValue());
									newEleFattDett
											.setSconto1RigaFattura(eleFattDett
													.getSconto_1()
													.doubleValue());
									newEleFattDett
											.setSconto2RigaFattura(eleFattDett
													.getSconto_2()
													.doubleValue());
									newEleFattDett
											.setCodIvaRigaFattura(eleFattDett
													.getCod_iva());

									String isbd = "";
									String bid = "";

									// solo per la prima fattura dell'elenco
									// ricavo il titolo (mi occorre solo in
									// esamina e non sulla sintetica)

									if (numRighe == 1 && totFatt == 1) {
										if (eleFattDett.getId_ordine().getBid() != null
												&& eleFattDett.getId_ordine()
														.getBid().trim()
														.length() != 0) {
											bid = eleFattDett.getId_ordine()
													.getBid();

											try {
												TitoloACQVO recTit = null;
												recTit = dao
														.getTitoloOrdineTer(eleFattDett
																.getId_ordine()
																.getBid()
																.trim());
												if (recTit != null
														&& recTit.getIsbd() != null) {
													bid = eleFattDett
															.getId_ordine()
															.getBid().trim();
													isbd = recTit.getIsbd();
												}
												if (recTit == null) {
													isbd = " ";
													bid = eleFattDett
															.getId_ordine()
															.getBid();
												}
											} catch (Exception e) {
												isbd = " ";
												bid = eleFattDett
														.getId_ordine()
														.getBid();
											}
										}
									}
									//

									newEleFattDett.setTitOrdine(isbd);
									newEleFattDett.setBidOrdine(bid);
									newEleFattDett.setInvRigaFatt("");

									// recupero inventario associato alla riga
									// di fattura
									if (numRighe == 1 && totFatt == 1) {
										InventarioListeVO listaInv = null;
										try {
											String codPolo = rec.getCodPolo();
											String codBib = rec.getCodBibl();
											String codBibO = rec.getCodBibl();
											String codTipOrd = String
													.valueOf(eleFattDett
															.getId_ordine()
															.getCod_tip_ord());
											int annoOrd = eleFattDett
													.getId_ordine()
													.getAnno_ord().intValue();
											int codOrd = eleFattDett
													.getId_ordine()
													.getId_ordine();
											String codBibF = rec.getCodBibl();
											int annoF = Integer.valueOf(rec
													.getAnnoFattura());
											int progF = Integer.valueOf(rec
													.getProgrFattura());
											int rigaF = newEleFattDett
													.getRigaFattura();
											// Locale locale=ricercaFatture.;
											Locale locale = Locale.getDefault(); // aggiornare
											// con
											// quella
											// locale
											// se
											// necessario
											String ticket = ricercaFatture
													.getTicket();
											listaInv = this.getInventario()
													.getInventarioRigaFattura(
															codPolo, codBib,
															codBibO, codTipOrd,
															annoOrd, codOrd,
															codBibF, annoF,
															progF, rigaF,
															locale, ticket);
											if (listaInv != null) {
												InventarioListeVO ele = listaInv;
												String inv = ele.getCodSerie()
														+ " "
														+ String
																.valueOf(ele
																		.getCodInvent());
												newEleFattDett
														.setInvRigaFatt(inv
																.trim());
											}
										} catch (Exception e) {

											// l'errore capita in questo punto
											log.error("", e);
										}

									}
									newEleFattDett
											.setOrdine(new StrutturaTerna("",
													"", ""));
									if (!String.valueOf(
											newEleFattDett.getIDOrd()).equals(
											"null")
											&& newEleFattDett.getIDOrd() > 0) {
										newEleFattDett.getOrdine().setCodice1(
												String.valueOf(eleFattDett
														.getId_ordine()
														.getCod_tip_ord()));
										newEleFattDett.getOrdine().setCodice2(
												String.valueOf(eleFattDett
														.getId_ordine()
														.getAnno_ord()));
										newEleFattDett.getOrdine().setCodice3(
												String.valueOf(eleFattDett
														.getId_ordine()
														.getCod_ord()));
										newEleFattDett
												.setPrezzoOrdine(eleFattDett
														.getId_ordine()
														.getPrezzo_lire()
														.doubleValue());
									}

									newEleFattDett
											.setBilancio(new StrutturaTerna("",
													"", ""));
									if (!String.valueOf(
											newEleFattDett.getIDBil()).equals(
											"null")
											&& newEleFattDett.getIDBil() > 0) {
										newEleFattDett
												.getBilancio()
												.setCodice1(
														String
																.valueOf(eleFattDett
																		.getCod_mat()
																		.getId_capitoli_bilanci()
																		.getEsercizio()));
										newEleFattDett
												.getBilancio()
												.setCodice2(
														String
																.valueOf(eleFattDett
																		.getCod_mat()
																		.getId_capitoli_bilanci()
																		.getCapitolo()));
										newEleFattDett
												.getBilancio()
												.setCodice3(
														String
																.valueOf(eleFattDett
																		.getCod_mat()
																		.getCod_mat()));
										// per gestire la visualizzazione della
										// riga altre spese nella jsp
										if (newEleFattDett.getBilancio()
												.getCodice3().trim()
												.equals("4")) {
											newEleFattDett.setCodPolo("*");

										}

									}

									newEleFattDett
											.setNoteRigaFattura(eleFattDett
													.getNote());
									newEleFattDett
											.setFattura(new StrutturaTerna("",
													"", ""));

									// gestione note di credito
									if (eleFattDett.getId_fattura()
											.getTipo_fattura() == 'N'
											&& eleFattDett
													.getId_fattura_in_credito() > 0) {

										ListaSuppFatturaVO ricercaFattureNC = new ListaSuppFatturaVO();
										ricercaFattureNC.setIDFatt(eleFattDett
												.getId_fattura_in_credito());
										ricercaFattureNC.setFlag_canc(false);
										List resultFattNC = fattureDao
												.getRicercaListaFattureHib(ricercaFattureNC);
										for (int b = 0; b < resultFattNC.size(); b++) {
											Tba_fatture oggFATTNC = (Tba_fatture) resultFattNC
													.get(b);
											if (oggFATTNC.getAnno_fattura()
													.doubleValue() > 0) {
												newEleFattDett
														.getFattura()
														.setCodice1(
																String
																		.valueOf(oggFATTNC
																				.getAnno_fattura()
																				.doubleValue()));
											}
											if (oggFATTNC.getProgr_fattura() > 0) {
												newEleFattDett
														.getFattura()
														.setCodice2(
																String
																		.valueOf(oggFATTNC
																				.getProgr_fattura()));
											}
											if (!String.valueOf(
													oggFATTNC.getId_fattura())
													.equals("null")
													&& oggFATTNC
															.getId_fattura() > 0) {
												newEleFattDett
														.setIDFattNC(oggFATTNC
																.getId_fattura());
											}
										}

										if (eleFattDett
												.getId_fattura_in_credito() > 0) {
											newEleFattDett
													.getFattura()
													.setCodice3(
															String
																	.valueOf(eleFattDett
																			.getId_fattura_in_credito()));
										}
									}
									arrayFattDett.add(newEleFattDett);
								}
							} catch (Exception e) {
								log.error("", e);
							}
						}

						// elimina le fatture senza righe
						if (ricercaFatture.isRicercaOrd()
								&& arrayFattDett.size() > 0) {
							rec.setRigheDettaglioFattura(arrayFattDett);
							listaFatture.add(rec);
							progrRighe = progrRighe + 1;
						}

						if (!ricercaFatture.isRicercaOrd()) {
							rec.setRigheDettaglioFattura(arrayFattDett);
							listaFatture.add(rec);
							progrRighe = progrRighe + 1;
						}
						// fine
						// listaFatture.add(rec);
					}
				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaFatture(listaFatture);
		return listaFatture;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaProfili(ListaSuppProfiloVO ricercaProfili)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaProfili = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaProfili(ricercaProfili);
		} else {
			try {
				Tba_profili_acquistoDao profiliDao = new Tba_profili_acquistoDao();
				Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
				ValidaListaSuppProfiloVO(ricercaProfili);
				results = profiliDao.getRicercaListaProfiliHib(ricercaProfili);
				if (results != null && results.size() > 0) {
					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						StrutturaProfiloVO rec = new StrutturaProfiloVO();
						Tba_profili_acquisto oggPROF = (Tba_profili_acquisto) results
								.get(i);
						if (oggPROF.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						rec.setProgressivo(numRighe);
						rec.setCodPolo(ricercaProfili.getCodPolo());
						rec.setCodBibl(ricercaProfili.getCodBibl());
						rec.setIDSez(oggPROF.getId_sez_acquis_bibliografiche()
								.getId_sez_acquis_bibliografiche());
						rec.setSezione(new StrutturaCombo("", ""));
						rec.getSezione().setCodice(
								oggPROF.getId_sez_acquis_bibliografiche()
										.getCod_sezione());
						rec.setProfilo(new StrutturaCombo("", ""));
						rec.getProfilo().setCodice(
								String.valueOf(oggPROF.getCod_prac())); // 24.07.09
						// oggPROF.getCod_prac().toString()
						rec.getProfilo().setDescrizione(
								oggPROF.getDescr().trim());
						rec.setPaese(new StrutturaCombo("", ""));
						rec.getPaese().setCodice(oggPROF.getPaese());
						rec.setLingua(new StrutturaCombo("", ""));
						rec.getLingua().setCodice(oggPROF.getLingua());
						rec.setDataUpd(oggPROF.getTs_var());

						// ricerca dei fornitori (SOLO DI BIBLIOTECA) associati
						// al profilo se esistenti
						ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
						if (rec != null && rec.getProfilo() != null
								&& rec.getProfilo().getCodice() != null) {
							ricercaFornitori.setCodPolo(rec.getCodPolo());
							ricercaFornitori.setCodBibl(rec.getCodBibl());
							ricercaFornitori.setCodProfiloAcq(rec.getProfilo()
									.getCodice());
							ricercaFornitori.setCodSezione(rec.getSezione()
									.getCodice());
							List<Tbr_fornitori> resultsForn = null;
							try {
								resultsForn = fornitoriDao
										.getRicercaListaFornitoriHib(ricercaFornitori);
							} catch (Exception e) {
								log.error("", e);
							}

							if (resultsForn != null && resultsForn.size() > 0) {
								List<StrutturaTerna> listForn = new ArrayList();
								int numRiga = 0;
								for (int t = 0; t < resultsForn.size(); t++) {
									numRiga = numRiga + 1;
									StrutturaTerna eleForn = new StrutturaTerna(
											"", "", "");
									eleForn.setCodice1(String.valueOf(numRiga));
									eleForn.setCodice2(String
											.valueOf(resultsForn.get(t)
													.getCod_fornitore()));
									eleForn.setCodice3(resultsForn.get(t)
											.getNom_fornitore());
									listForn.add(eleForn);
								}
								rec.setListaFornitori(listForn);

							}

						}

						listaProfili.add(rec);
					}
				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaProfili(listaProfili);
		return listaProfili;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaComunicazioni(
			ListaSuppComunicazioneVO ricercaComunicazioni)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaCom = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaComunicazioni(ricercaComunicazioni);
		} else {
			try {
				Tra_messaggiDao comDao = new Tra_messaggiDao();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();

				ValidaListaSuppComunicazioneVO(ricercaComunicazioni);
				results = comDao
						.getRicercaListaComunicazioniHib(ricercaComunicazioni);
				if (results != null && results.size() > 0) {
					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						ComunicazioneVO rec = new ComunicazioneVO();
						Tra_messaggi oggCom = (Tra_messaggi) results.get(i);
						if (oggCom.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						Tbf_biblioteca_in_polo appoBib = oggCom.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						// inizio
						rec.setProgressivo(numRighe);
						rec.setDenoBibl(appoBib.getDs_biblioteca());
						rec.setCodiceMessaggio(String.valueOf(oggCom
								.getCod_msg()));

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());
						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggCom
									.getData_msg());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataComunicazione(dataFormattata);

						rec.setNoteComunicazione(oggCom.getNote());
						rec.setStatoComunicazione(String.valueOf(oggCom
								.getStato_msg()));
						rec.setDataUpd(oggCom.getTs_var());

						rec.setMittenteCap(appoBib.getId_biblioteca().getCap());
						rec.setMittenteCitta(appoBib.getId_biblioteca()
								.getLocalita());
						rec.setMittenteFax(appoBib.getId_biblioteca().getFax());
						rec.setMittenteIndirizzo(appoBib.getId_biblioteca()
								.getIndirizzo());
						rec.setMittenteTelefono(appoBib.getId_biblioteca()
								.getTelefono());
						rec.setCodCliFornitore("");

						// ricerca su fornbibl
						rec.setCodCliFornitore("");
						try {
							rec.setCodCliFornitore(fornitoriDao
									.getCodClienteDaTbr_fornitori_biblioteche(
											String.valueOf(oggCom
													.getCod_fornitore()
													.getCod_fornitore()), rec
													.getCodBibl(), rec
													.getCodPolo()));
						} catch (Exception e) {
							// log.error("", e);
						}

						if (rec.getStatoComunicazione().equals("1")) {
							rec.setDesStato("RICEVUTO");
						} else if (rec.getStatoComunicazione().equals("2")) {
							rec.setDesStato("SPEDITO");
						} else if (rec.getStatoComunicazione().equals("3")) {
							rec.setDesStato("NON SPEDITO");
						} else {
							rec.setDesStato("");
						}

						rec.setTipoMessaggio(oggCom.getTipo_msg());

						// ricerca su tb_codici
						String kCdMsg = rec.getTipoMessaggio().trim();
						if (rec.getTipoMessaggio() != null
								&& rec.getTipoMessaggio().trim().length() > 0) {
							if (rec.getCodiceMessaggio().length() < 2) {
								// aggiunta di zeri iniziali
								String cbibl = "00" + rec.getTipoMessaggio();
								int posizStart = cbibl.length() - 2;
								cbibl = cbibl.substring(posizStart,
										posizStart + 2);
								// String
								// cbibl=kbibl.substring(kbibl.length()-3,
								// kbibl.length());
								kCdMsg = cbibl;
							}
						}
						rec.setDesMessaggio("");
						try {
							rec.setDesMessaggio(ordiniDao.getCodiciDaTbCodici(
									"ATME", kCdMsg));
						} catch (Exception e) {
							// log.error("", e);
						}

						rec.setTipoInvioComunicazione(String.valueOf(oggCom
								.getTipo_invio()));

						if (rec.getTipoInvioComunicazione().equals("F")) {
							rec.setTipoInvioDes("fax");
						} else if (rec.getTipoInvioComunicazione().equals("P")) {
							rec.setTipoInvioDes("posta");
						} else if (rec.getTipoInvioComunicazione().equals("S")) {
							rec.setTipoInvioDes("stampa");
						} else {
							rec.setTipoInvioDes("");
						}

						rec.setFornitore(new StrutturaCombo("", ""));
						rec.getFornitore().setCodice(
								String.valueOf(oggCom.getCod_fornitore()
										.getCod_fornitore()));
						rec.getFornitore().setDescrizione(
								oggCom.getCod_fornitore().getNom_fornitore());

						// dati fornitore per stampe
						rec.setAnagFornitore(ConversioneHibernateVO.toWeb()
								.fornitore(oggCom.getCod_fornitore()));
						rec.getAnagFornitore().setFornitoreBibl(
								new DatiFornitoreVO());

						rec.setIdDocumento(new StrutturaTerna("", "", ""));

						if (String.valueOf(oggCom.getCod_tip_ord()) != null
								&& String.valueOf(oggCom.getCod_tip_ord())
										.trim().length() != 0)
						// if
						// (ricercaComunicazioni.getTipoDocumento().equals("F")
						// )
						{
							rec.setTipoDocumento("O");
							rec.getIdDocumento().setCodice1(
									String.valueOf(oggCom.getCod_tip_ord())); // cod_tip_ord
							rec.getIdDocumento().setCodice2(
									String.valueOf(oggCom.getAnno_ord())); // anno_ord
							rec.getIdDocumento().setCodice3(
									String.valueOf(oggCom.getCod_ord())); // cod_ord
						}
						// if
						// (ricercaComunicazioni.getTipoDocumento().equals("O")
						// )
						else {
							rec.setTipoDocumento("F");
							rec.getIdDocumento().setCodice2(
									String.valueOf(oggCom.getAnno_fattura())); // anno_fattura
							rec.getIdDocumento().setCodice3(
									String.valueOf(oggCom.getProgr_fattura())); // progr_fattura
						}

						rec.setDirezioneComunicazione("A"); // assumo un valore
						// di default
						rec.setDirezioneComunicazioneLabel("a");
						if (String.valueOf(oggCom.getStato_msg()).equals("1")) {
							rec.setDirezioneComunicazione("D");
							rec.setDirezioneComunicazioneLabel("da");
						}

						// dati di dettaglio dell'ordine tck 2560 (solo in
						// esamina)

						rec.setDocORDINE(new StrutturaQuinquies("", "", "", "",
								"")); // anno_abb, data_fasc_str ,
						// data_fine_str, titolo, rinnovo

						if (rec.getTipoDocumento() != null
								&& rec.getTipoDocumento().equals("O")
								&& numRighe == 1) {
							// ricerca ordine
							int numOrd = 0;
							String bidOrd = "";
							String isbd = "";
							String annoAbb = "";
							String dataFascAbb = "";
							String dataFineAbb = "";
							String continuativo = "0";
							String natura = "";
							String rinnovo = "";

							ListaSuppOrdiniVO ricercaOrdiniCom = new ListaSuppOrdiniVO();
							ricercaOrdiniCom.setCodBibl(rec.getCodBibl());
							ricercaOrdiniCom.setCodPolo(rec.getCodPolo());
							if (rec.getIdDocumento() != null
									&& rec.getIdDocumento().getCodice1() != null
									&& rec.getIdDocumento().getCodice1().trim()
											.length() > 0) {
								// sqlOrdine=sqlOrdine +
								// " and ord.cod_tip_ord='" +
								// rec.getIdDocumento().getCodice1().trim() +
								// "'";
								// ricercaOrdiniCom.setTipoOrdine(oggCom.getCod_tip_ord().charAt(0));
								ricercaOrdiniCom.setTipoOrdine(rec
										.getIdDocumento().getCodice1());
							}
							if (rec.getIdDocumento() != null
									&& rec.getIdDocumento().getCodice2() != null
									&& rec.getIdDocumento().getCodice2().trim()
											.length() > 0) {
								// sqlOrdine=sqlOrdine + " and ord.anno_ord='" +
								// rec.getIdDocumento().getCodice2().trim() +
								// "'";
								ricercaOrdiniCom.setAnnoOrdine(rec
										.getIdDocumento().getCodice2());
							}
							if (rec.getIdDocumento() != null
									&& rec.getIdDocumento().getCodice3() != null
									&& rec.getIdDocumento().getCodice3().trim()
											.length() > 0) {
								// sqlOrdine=sqlOrdine + " and ord.cod_ord="+
								// rec.getIdDocumento().getCodice3().trim();
								ricercaOrdiniCom.setCodOrdine(rec
										.getIdDocumento().getCodice3());
							}

							List<Tba_ordini> resultsCom = null;
							try {
								resultsCom = ordiniDao
										.getRicercaListaOrdiniHib(ricercaOrdiniCom);

							} catch (Exception e) {

								// l'errore capita in questo punto
								log.error("", e);
							}
							if (resultsCom != null && resultsCom.size() > 0) {
								for (int h = 0; h < resultsCom.size(); h++) {
									numOrd = numOrd + 1;
									bidOrd = resultsCom.get(h).getBid();
									if (String.valueOf(resultsCom.get(h)
											.getAnno_abb()) != "null"
											&& resultsCom.get(h).getAnno_abb()
													.intValue() > 0) {
										annoAbb = String.valueOf(resultsCom
												.get(h).getAnno_abb()
												.intValue());
									}
									dataFormattata = "";
									try {
										dataFormattata = format1
												.format(resultsCom.get(h)
														.getData_fasc());
									} catch (Exception e) {
										// log.error("", e);
									}
									dataFascAbb = dataFormattata;

									dataFormattata = "";
									try {
										dataFormattata = format1
												.format(resultsCom.get(h)
														.getData_fine());
									} catch (Exception e) {
										// log.error("", e);
									}
									dataFineAbb = dataFormattata;

									if (String.valueOf(resultsCom.get(h)
											.getContinuativo()) != "null"
											&& String.valueOf(
													resultsCom.get(h)
															.getContinuativo())
													.equals("1")) {
										continuativo = "1";
									}
									if (String.valueOf(resultsCom.get(h)
											.getNatura()) != null) {
										natura = String.valueOf(resultsCom.get(
												h).getNatura());
									}
									if (resultsCom.get(h).getAnno_1ord()
											.intValue() > 0
											&& resultsCom.get(h).getCod_1ord() > 0) {
										rinnovo = resultsCom.get(h)
												.getCod_tip_ord()
												+ "-"
												+ String.valueOf(resultsCom
														.get(h).getAnno_1ord()
														.intValue())
												+ "-"
												+ String.valueOf(resultsCom
														.get(h).getCod_1ord());
									}

								}
							}

							String nStandard = "";
							List<StrutturaTerna> nStandardArr = null;

							if (bidOrd != null && bidOrd.trim().length() > 0
									&& numOrd == 1) {
								try {
									TitoloACQVO recTit = null;
									recTit = dao.getTitoloOrdineTer(bidOrd);
									if (recTit != null
											&& recTit.getIsbd() != null) {
										isbd = recTit.getIsbd();
									}
									if (recTit != null
											&& recTit.getNumStandard() != null) {
										nStandard = recTit.getNumStandard();
									}
									if (recTit != null
											&& recTit.getNumStandardArr() != null
											&& recTit.getNumStandardArr()
													.size() > 0) {
										nStandardArr = recTit
												.getNumStandardArr();
									}

								} catch (Exception e) {
									log.error("", e);
								}
							}

							if (nStandardArr != null && nStandardArr.size() > 0) {
								rec.setNumStandardArr(nStandardArr);
								rec.leggiNumSdt();

							}

							rec.getDocORDINE().setCodice1(annoAbb);
							rec.getDocORDINE().setCodice2(dataFascAbb);
							rec.getDocORDINE().setCodice3(dataFineAbb);
							rec.getDocORDINE().setCodice4(isbd);
						}
						// fine
						listaCom.add(rec);
					} // fine for
				} // fine if
			} // fine try
			catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaComunicazioni(listaCom);
		return listaCom;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciCambio(CambioVO cambio) throws DataException,
			ApplicationException, ValidationException {
		return dao.inserisciCambio(cambio);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaCambio(CambioVO cambio) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaCambio(cambio);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaCambio(CambioVO cambio) throws DataException,
			ApplicationException {
		return dao.cancellaCambio(cambio);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciProfilo(StrutturaProfiloVO profilo)
			throws DataException, ApplicationException, ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaProfili = new ArrayList();
		List results = null;
		boolean ret = false;
		boolean retPrimario = false;
		boolean retSecondario = false;

		if (!testxHib) {
			ret = dao.inserisciProfilo(profilo);
		} else {
			try {
				Tba_profili_acquistoDao profiliDao = new Tba_profili_acquistoDao();
				this.ValidaStrutturaProfiloVO(profilo);
				// effettuare prima la ricerca: se esiste non effettuare
				// l'inserimento
				ListaSuppProfiloVO listaSupp = this
						.trasformaProfiloInlistaSupp(profilo);
				if (listaSupp != null) {
					List elenco = null;
					elenco = profiliDao.getRicercaListaProfiliHib(listaSupp);
					if (elenco == null) {
						// inserimento
						Tba_profili_acquisto pro = new Tba_profili_acquisto();

						Tbf_polo polo = new Tbf_polo();
						// polo.setCd_polo(ricercaCambi.getCodPolo());
						polo.setCd_polo(profilo.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_biblioteca(profilo.getCodBibl());
						bib.setCd_polo(polo);
						// DATA di sistema
						Timestamp ts = new java.sql.Timestamp(System
								.currentTimeMillis());
						// pro.setCd_bib(bib);

						// lettura x acquisire sezione
						Tba_sez_acquis_bibliografiche sez = new Tba_sez_acquis_bibliografiche();
						int numRigheSez = 0;
						if (profilo.getSezione() != null
								&& profilo.getSezione().getCodice() != null
								&& profilo.getSezione().getCodice().trim()
										.length() > 0) {
							ListaSuppSezioneVO ricercaSezioni = new ListaSuppSezioneVO();
							List<Tba_sez_acquis_bibliografiche> resultsSez = null;
							ricercaSezioni.setCodBibl(profilo.getCodBibl());
							ricercaSezioni.setCodPolo(profilo.getCodPolo());
							ricercaSezioni.setCodiceSezione(profilo
									.getSezione().getCodice().trim()
									.toUpperCase());
							Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
							resultsSez = sezioniDao
									.getRicercaListaSezioniHib(ricercaSezioni);
							if (resultsSez != null) {
								numRigheSez = resultsSez.size();
							}
							if (numRigheSez == 1) {
								sez = resultsSez.get(0);
							} else {
								throw new ValidationException(
										"ordineIncongruenzaSezioneInesistente",
										ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
							}
						}
						// inserimento profilo
						// pro.setCod_prac(Integer.valueOf("11"));
						pro.setDescr(profilo.getProfilo().getDescrizione());
						pro.setPaese(profilo.getPaese().getCodice());
						pro.setLingua(profilo.getLingua().getCodice());
						pro.setId_sez_acquis_bibliografiche(sez);
						pro.setTs_ins(ts);
						pro.setTs_var(ts);
						pro.setFl_canc('N');
						pro.setUte_ins(profilo.getUtente());
						pro.setUte_var(profilo.getUtente());
						// Tra_sez_acquisizione_fornitori sezForn = new
						// Tra_sez_acquisizione_fornitori();
						// sezForn.setTba_profili_acquisto()

						retPrimario = profiliDao.inserisciProfiloHib(pro);

						// rilettura profilo per acquisire l'id attribuito
						elenco = profiliDao
								.getRicercaListaProfiliHib(listaSupp);
						int idProf = 0;
						if (elenco != null && elenco.size() == 1) {
							idProf = ((List<Tba_profili_acquisto>) elenco)
									.get(0).getCod_prac();
							profilo.getProfilo().setCodice(
									String.valueOf(idProf)); // per aggiornare i
							// dati della
							// action

						} else {
							// lanciare eccezione
						}

						if (profilo.getListaFornitori() != null
								&& profilo.getListaFornitori().size() > 0) {
							// valRitornoINSLOOP=false;
							for (int i = 0; i < profilo.getListaFornitori()
									.size(); i++) {
								StrutturaTerna oggettoDettVO = (StrutturaTerna) profilo
										.getListaFornitori().get(i);
								// lettura fornitore
								ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
								ricercaFornitori.setCodFornitore(oggettoDettVO
										.getCodice2());
								ricercaFornitori.setCodBibl(profilo
										.getCodBibl());
								ricercaFornitori.setCodPolo(profilo
										.getCodPolo());
								// ricercaFornitori.setLocale("1");
								Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
								List<Tbr_fornitori> resultsForn = fornitoriDao
										.getRicercaListaFornitoriHib(ricercaFornitori);
								int numRigheForn = 0;
								if (resultsForn != null) {
									numRigheForn = resultsForn.size();
								}
								if (numRigheForn != 1) {
									throw new ValidationException(
											"ordineIncongruenzaFornitoreInesistente",
											ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
								}
								// controllo duplicazione fornitore
								int codForn = resultsForn.get(0)
										.getCod_fornitore();
								if (i > 0) {
									for (int j = 0; j < i; j++) {
										if (String.valueOf(codForn).equals(
												((StrutturaTerna) profilo
														.getListaFornitori()
														.get(j)).getCodice2())) {
											throw new ValidationException(
													"profilierroreFornitoreRipetuto",
													ValidationExceptionCodici.profilierroreFornitoreRipetuto);
										}
									}
								}

								// il fornitore è locale?
								Boolean fornLoc = false;
								if (resultsForn.get(0)
										.getTbr_fornitori_biblioteche() != null) {
									Object[] elencoFornBibl = resultsForn
											.get(0)
											.getTbr_fornitori_biblioteche()
											.toArray();
									if (elencoFornBibl.length > 0) {
										fornLoc = true;

									}
								}

								// se il fornitore non è locale prima di
								// inserirlo fra quelli del profilo devo
								// localizzarlo
								if (!fornLoc) {
									Tbr_fornitori_biblioteche fornBibl = new Tbr_fornitori_biblioteche();
									fornBibl.setCd_biblioteca(bib);
									fornBibl.setCod_fornitore(resultsForn
											.get(0).getCod_fornitore()); // cod_fornitore
									fornBibl.setTipo_pagamento(""); // tipo_pagamento
									fornBibl.setCod_cliente(""); // cod_cliente
									fornBibl.setNom_contatto(""); // nom_contatto
									fornBibl.setTel_contatto(""); // tel_contatto
									fornBibl.setFax_contatto(""); // fax_contatto
									fornBibl.setValuta("EUR"); // valuta
									fornBibl.setCod_polo(profilo.getCodPolo()); // cod_polo
									fornBibl.setAllinea(' '); // allinea
									fornBibl.setUte_ins(profilo.getUtente());
									fornBibl.setTs_ins(ts);
									fornBibl.setUte_var(profilo.getUtente());
									fornBibl.setTs_var(ts);
									fornBibl.setFl_canc('N');
									Set listaFornBib = new HashSet<Tbr_fornitori_biblioteche>();
									listaFornBib.add(fornBibl);
									resultsForn.get(0)
											.setTbr_fornitori_biblioteche(
													listaFornBib);
									retSecondario = fornitoriDao
											.modificaFornitoreHib(resultsForn
													.get(0));
									if (!retSecondario) {
										throw new ValidationException(
												"ordineIncongruenzaFornitoreInesistente",
												ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
									}

								}
								// inserisco il fornitore del profilo
								Tra_sez_acquisizione_fornitori fornProf = new Tra_sez_acquisizione_fornitori();
								fornProf.setCd_biblioteca(bib);

								fornProf.setCod_prac(BigDecimal.valueOf(Double
										.valueOf(String.valueOf(idProf))));
								fornProf.setCod_fornitore(resultsForn.get(0)
										.getORMID());
								fornProf.setUte_ins(profilo.getUtente());
								fornProf.setTs_ins(ts);
								fornProf.setUte_var(profilo.getUtente());
								fornProf.setTs_var(ts);
								fornProf.setFl_canc('N');
								// fornProf.setTba_profili_acquisto();

								retSecondario = profiliDao
										.inserisciFornProfiloHib(fornProf);

							}
						}
					} else {
						throw new ValidationException(
								"cambierroreInserimentoEsistenzaRecord",
								ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
					}
				}
				if (retPrimario && retSecondario) {
					ret = true;
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaProfilo(StrutturaProfiloVO profilo)
			throws DataException, ApplicationException, ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaProfili = new ArrayList();
		List results = null;
		boolean ret = false;
		boolean retPrimario = false;
		boolean retSecondario = false;
		boolean controlloCONGR = false;

		if (!testxHib) {
			ret = dao.modificaProfilo(profilo);
		} else {
			try {
				Tbf_polo polo = new Tbf_polo();
				// polo.setCd_polo(ricercaCambi.getCodPolo());
				polo.setCd_polo(profilo.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(profilo.getCodBibl());
				bib.setCd_polo(polo);
				// DATA di sistema
				Timestamp ts = new java.sql.Timestamp(System
						.currentTimeMillis());

				// controlli preventivi
				// lettura x acquisire sezione e controllo esistenza E SCADENZA
				Tba_sez_acquis_bibliografiche sez = new Tba_sez_acquis_bibliografiche();
				int numRigheSez = 0;
				if (profilo.getSezione() != null
						&& profilo.getSezione().getCodice() != null
						&& profilo.getSezione().getCodice().trim().length() > 0) {
					ListaSuppSezioneVO ricercaSezioni = new ListaSuppSezioneVO();
					List<Tba_sez_acquis_bibliografiche> resultsSez = null;
					ricercaSezioni.setCodBibl(profilo.getCodBibl());
					ricercaSezioni.setCodPolo(profilo.getCodPolo());
					ricercaSezioni.setCodiceSezione(profilo.getSezione()
							.getCodice().trim().toUpperCase());
					Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
					resultsSez = sezioniDao
							.getRicercaListaSezioniHib(ricercaSezioni);
					if (resultsSez != null) {
						numRigheSez = resultsSez.size();
					}
					if (numRigheSez == 1) {
						sez = resultsSez.get(0);
						controlloCONGR = true;
					} else {
						controlloCONGR = false;
						throw new ValidationException(
								"ordineIncongruenzaSezioneInesistente",
								ValidationExceptionCodici.ordineIncongruenzaSezioneInesistente);
					}
				}

				// ciclo sui fornitori, verifico esistenza e lancio eccezione e
				// in caso localizzo
				if (profilo.getListaFornitori() != null
						&& profilo.getListaFornitori().size() > 0) {
					for (int i = 0; i < profilo.getListaFornitori().size(); i++) {
						StrutturaTerna oggettoDettVO = (StrutturaTerna) profilo
								.getListaFornitori().get(i);
						// controllo esistenza fornitore
						// lettura fornitore
						ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
						ricercaFornitori.setCodFornitore(oggettoDettVO
								.getCodice2());
						ricercaFornitori.setCodBibl(profilo.getCodBibl());
						ricercaFornitori.setCodPolo(profilo.getCodPolo());
						// ricercaFornitori.setLocale("1");
						Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
						List<Tbr_fornitori> resultsForn = fornitoriDao
								.getRicercaListaFornitoriHib(ricercaFornitori);
						int numRigheForn = 0;
						if (resultsForn != null) {
							numRigheForn = resultsForn.size();
							controlloCONGR = true;
						}
						if (numRigheForn != 1) {
							controlloCONGR = false;
							throw new ValidationException(
									"ordineIncongruenzaFornitoreInesistente",
									ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
						}
						// il fornitore è locale?
						Boolean fornLoc = false;
						if (resultsForn.get(0).getTbr_fornitori_biblioteche() != null) {
							Object[] elencoFornBibl = resultsForn.get(0)
									.getTbr_fornitori_biblioteche().toArray();
							if (elencoFornBibl.length > 0) {
								fornLoc = true;

							}
						}

						// esiste il fornitore in anagrafica e non esiste fra
						// quelli di biblioteca
						// se il fornitore non è locale prima di inserirlo fra
						// quelli del profilo devo localizzarlo
						if (!fornLoc) {
							Tbr_fornitori_biblioteche fornBibl = new Tbr_fornitori_biblioteche();
							fornBibl.setCd_biblioteca(bib);
							fornBibl.setCod_fornitore(resultsForn.get(0)
									.getCod_fornitore()); // cod_fornitore
							fornBibl.setTipo_pagamento(""); // tipo_pagamento
							fornBibl.setCod_cliente(""); // cod_cliente
							fornBibl.setNom_contatto(""); // nom_contatto
							fornBibl.setTel_contatto(""); // tel_contatto
							fornBibl.setFax_contatto(""); // fax_contatto
							fornBibl.setValuta("EUR"); // valuta
							fornBibl.setCod_polo(profilo.getCodPolo()); // cod_polo
							fornBibl.setAllinea(' '); // allinea
							fornBibl.setUte_ins(profilo.getUtente());
							fornBibl.setTs_ins(ts);
							fornBibl.setUte_var(profilo.getUtente());
							fornBibl.setTs_var(ts);
							fornBibl.setFl_canc('N');
							Set listaFornBib = new HashSet<Tbr_fornitori_biblioteche>();
							listaFornBib.add(fornBibl);
							resultsForn.get(0).setTbr_fornitori_biblioteche(
									listaFornBib);
							retSecondario = fornitoriDao
									.modificaFornitoreHib(resultsForn.get(0));
							controlloCONGR = true;
							if (!retSecondario) {
								controlloCONGR = false;
								throw new ValidationException(
										"ordineIncongruenzaFornitoreInesistente",
										ValidationExceptionCodici.ordineIncongruenzaFornitoreInesistente);
							}

						}
						// controllo duplicazione fornitore
						int codForn = resultsForn.get(0).getCod_fornitore();
						if (i > 0) {
							controlloCONGR = true;
							for (int j = 0; j < i; j++) {
								if (String.valueOf(codForn).equals(
										((StrutturaTerna) profilo
												.getListaFornitori().get(j))
												.getCodice2())) {
									controlloCONGR = false;
									throw new ValidationException(
											"profilierroreFornitoreRipetuto",
											ValidationExceptionCodici.profilierroreFornitoreRipetuto);
								}
							}

						}
					}
				}

				// se tutti i controlli ok
				if (controlloCONGR) {
					// lettura profilo (SE PIù DI UNO ECCEZIONE)
					Tba_profili_acquistoDao profiliDao = new Tba_profili_acquistoDao();
					this.ValidaStrutturaProfiloVO(profilo);
					// effettuare prima la ricerca: se esiste non effettuare
					// l'inserimento
					ListaSuppProfiloVO listaSupp = this
							.trasformaProfiloInlistaSupp(profilo);
					if (listaSupp != null) {
						List elenco = null;
						elenco = profiliDao
								.getRicercaListaProfiliHib(listaSupp);
						if (elenco != null && elenco.size() == 1) {
							// modifica
							// aggiornamento
							// CONTROLLO DELLA CONCORRENZA
							Tba_profili_acquisto pro = ((List<Tba_profili_acquisto>) elenco)
									.get(0);

							if (!pro.getTs_var().equals(profilo.getDataUpd())) {
								throw new ValidationException(
										"operazioneInConcorrenza",
										ValidationExceptionCodici.operazioneInConcorrenza);
							}
							pro.setDescr(profilo.getProfilo().getDescrizione());
							pro.setPaese(profilo.getPaese().getCodice());
							pro.setLingua(profilo.getLingua().getCodice());
							pro.setId_sez_acquis_bibliografiche(sez);
							pro.setTs_ins(ts);
							pro.setTs_var(ts);
							pro.setFl_canc('N');
							pro.setUte_ins(profilo.getUtente());
							pro.setUte_var(profilo.getUtente());
							ret = profiliDao.modificaProfiloHib(pro);
						}
					}
					if (ret) {
						// SU tra_sez_acquisizione_fornitori CANCELLAZIONE
						// LOGICA DEI FORNITORI LEGATI AL PROFILO
						// E POI IN CICLO CANCELLAZIONE FISICA E INSERIMENTO
						// CICLANDO SUI FORNITORI
						// lettura SU tra_sez_acquisizione_fornitori
						List elencoFornProf = null;
						elencoFornProf = profiliDao
								.getRicercaListaFornitoriProfiloHib(listaSupp);
						boolean retCancFornProf = false;
						if (elencoFornProf != null && elencoFornProf.size() > 0) {
							Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
							for (int i = 0; i < elencoFornProf.size(); i++) {
								// Cancellazione SU
								// tra_sez_acquisizione_fornitori di tutti i
								// fornitori del profilo preesistenti
								Tra_sez_acquisizione_fornitori eleElencoFornProf = (Tra_sez_acquisizione_fornitori) elencoFornProf
										.get(i);
								retCancFornProf = fornitoriDao
										.cancellaProfFornitoreHib(eleElencoFornProf);
								if (!retCancFornProf) {
									throw new ValidationException(
											"errors.acquisizioni.erroreModifica");
								}
							}
						}
						// inserimento SU tra_sez_acquisizione_fornitori di
						// tutti i nuovi fornitori del profilo
						if (profilo.getListaFornitori() != null
								&& profilo.getListaFornitori().size() > 0) {
							for (int i = 0; i < profilo.getListaFornitori()
									.size(); i++) {
								StrutturaTerna oggettoDettVO = (StrutturaTerna) profilo
										.getListaFornitori().get(i);
								// inserisco il fornitore del profilo
								Tra_sez_acquisizione_fornitori fornProf = new Tra_sez_acquisizione_fornitori();
								fornProf.setCd_biblioteca(bib);

								fornProf.setCod_prac(BigDecimal.valueOf(Double
										.valueOf(profilo.getProfilo()
												.getCodice())));
								fornProf.setCod_fornitore(Integer
										.parseInt(oggettoDettVO.getCodice2()));
								fornProf.setUte_ins(profilo.getUtente());
								fornProf.setTs_ins(ts);
								fornProf.setUte_var(profilo.getUtente());
								fornProf.setTs_var(ts);
								fornProf.setFl_canc('N');
								// fornProf.setTba_profili_acquisto();
								retSecondario = profiliDao
										.inserisciFornProfiloHib(fornProf);
								if (!retSecondario) {
									throw new ValidationException(
											"errors.acquisizioni.erroreModifica");
								}
							}

						}
					}

				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaProfilo(StrutturaProfiloVO profilo)
			throws DataException, ApplicationException, ValidationException {
		return dao.cancellaProfilo(profilo);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaCambi(ListaSuppCambioVO ricercaCambi)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return dao.getRicercaListaCambi(ricercaCambi);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppCambioVO(ListaSuppCambioVO prova)
			throws ValidationException {
		Validazione.ValidaListaSuppCambioVO(prova);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaCambioVO(CambioVO prova) throws ValidationException {
		Validazione.ValidaCambioVO(prova);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicerca(List listaCambi) throws ValidationException {
		Validazione.ValidaRicerca(listaCambi);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getListaOrdini() throws ResourceNotFoundException,
			ApplicationException {
		return dao.getListaOrdini();
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaOrdini(ListaSuppOrdiniVO ricercaOrdini)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		if (!testxHib) {
			return dao.getRicercaListaOrdini(ricercaOrdini);
		} else {
			List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO>();
			List<Tba_ordini> results = null;
			try {
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				this.ValidaListaSuppOrdiniVO(ricercaOrdini);
				results = ordiniDao.getRicercaListaOrdiniHib(ricercaOrdini);
				if (results != null && results.size() > 0) {
					if (results.size() > 1000) {
						throw new ValidationException(
								"ricercaDaRaffinareTroppi",
								ValidationExceptionCodici.ricercaDaRaffinareTroppi);
					}
					ConfigurazioneBOVO apConfBONew = new ConfigurazioneBOVO();
					apConfBONew.setCodPolo(ricercaOrdini.getCodPolo());
					apConfBONew.setCodBibl(ricercaOrdini.getCodBibl());
					String tipoRinnovoConfigurato = "";
					try {
						apConfBONew = ordiniDao
								.loadConfigurazioneHib(apConfBONew);
						tipoRinnovoConfigurato = apConfBONew.getTipoRinnovo();
					} catch (Exception e) {
						log.error("", e);
					}
					ConfigurazioneORDVO apConfORDNew = new ConfigurazioneORDVO();
					apConfORDNew.setCodPolo(ricercaOrdini.getCodPolo());
					apConfORDNew.setCodBibl(ricercaOrdini.getCodBibl());
					Boolean gestBil = true;
					Boolean gestSez = true;
					Boolean gestProf = true;
					try {
						apConfORDNew = ordiniDao
								.loadConfigurazioneOrdiniHib(apConfORDNew);
						if (apConfORDNew != null
								&& !apConfORDNew.isGestioneBilancio()) {
							gestBil = false;
						}
						if (apConfORDNew != null
								&& !apConfORDNew.isGestioneSezione()) {
							gestSez = false;
						}
						if (apConfORDNew != null
								&& !apConfORDNew.isGestioneProfilo()) {
							gestProf = false;
						}
					} catch (Exception e) {
						log.error("", e);
					}

					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe = numRighe + 1;
						OrdiniVO rec = new OrdiniVO();
						Tba_ordini oggORD = results.get(i);
						// configurazione
						rec.setGestBil(true);
						rec.setGestSez(true);
						rec.setGestProf(true);

						if (!gestBil) {
							rec.setGestBil(false);
						}
						if (!gestSez) {
							rec.setGestSez(false);
						}
						if (!gestProf) {
							rec.setGestProf(false);
						}
						// TODO lettura configurazione ordini per la bibioteca
						// dell'ordine (se non obbligatorio mettere a false)
						// TODO NON IMPOSTARE I VALORI DI DEFAULT SE SPECIFICATI
						// e' importante leggere la configurazione per imporre
						// il valore false, altrimenti per default è true

						if (oggORD.getFl_canc() == 'S') {
							rec.setFlag_canc(true);
						}
						Tbf_biblioteca_in_polo appoBib = oggORD.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());

						rec.setCodOrdine(String.valueOf(oggORD.getCod_ord()));
						rec.setAnnoOrdine(String.valueOf(oggORD.getAnno_ord()));
						rec.setTipoOrdine(String.valueOf(oggORD
								.getCod_tip_ord()));

						rec.setProgressivo(i);
						rec.setIDOrd(oggORD.getId_ordine());

						rec.setIDOrd(oggORD.getId_ordine());
						if (oggORD.getTbb_bilancicod_mat() != null
								&& oggORD.getTbb_bilancicod_mat()
										.getId_capitoli_bilanci() != null) {
							rec.setIDBil(oggORD.getTbb_bilancicod_mat()
									.getId_capitoli_bilanci()
									.getId_capitoli_bilanci());
						}
						if (oggORD.getId_sez_acquis_bibliografiche() != null) {
							rec.setIDSez(oggORD
									.getId_sez_acquis_bibliografiche()
									.getId_sez_acquis_bibliografiche());
						}
						if (oggORD.getId_valuta() != null) {
							rec.setIDVal(oggORD.getId_valuta().getId_valuta());
						}
						rec.setDataUpd(oggORD.getTs_var());
						// valorizzazione del campo datastampaordine per
						// identificare gli ordini stampati in uno stesso buono
						rec.setDataStampaOrdine("");
						if (!oggORD.getData_agg().equals(oggORD.getTs_ins())) {
							rec.setDataStampaOrdine(oggORD.getData_agg()
									.toString());
						}

						// ordine.setValutaOrdine(rsSub.getString("valuta"));

						// prova di antonio
						// GregorianCalendar g = new GregorianCalendar();
						// g.setTime(new
						// java.util.Date(System.currentTimeMillis()));
						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggORD
									.getData_ord());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataOrdine(dataFormattata);

						rec.setNoteOrdine(oggORD.getNote());
						rec.setNumCopieOrdine(oggORD.getNum_copie());
						if (oggORD.getContinuativo() == '1') {
							rec.setContinuativo(true);
						} else {
							rec.setContinuativo(false);
						}
						rec.setStatoOrdine(String.valueOf(oggORD
								.getStato_ordine()));
						// if (rs.getInt("cod_doc_lett")>0)
						// provenienza sugg lett
						if (oggORD.getCod_doc_lett() == null) {
							rec.setCodDocOrdine("");
							rec.setCodTipoDocOrdine("");

						} else if (oggORD.getCod_doc_lett() > 0) {
							rec.setCodDocOrdine(String.valueOf(oggORD
									.getCod_doc_lett()));
							rec.setCodTipoDocOrdine(String.valueOf(oggORD
									.getTipo_doc_lett()));
						}
						// provenienza sugg bibl
						if (oggORD.getCod_sugg_bibl() == null) {
							rec.setCodSuggBiblOrdine("");

						} else if (oggORD.getCod_sugg_bibl().intValue() > 0) {
							rec.setCodSuggBiblOrdine(String.valueOf(oggORD
									.getCod_sugg_bibl()));
						}

						rec.setCodUrgenzaOrdine(String.valueOf(oggORD
								.getTipo_urgenza()));
						// provenienza gare
						if (oggORD.getCod_rich_off() == null) {
							rec.setCodRicOffertaOrdine("");

						} else if (oggORD.getCod_rich_off().intValue() > 0) {
							rec.setCodRicOffertaOrdine(String.valueOf(oggORD
									.getCod_rich_off()));
						}
						// provenienza offerte fornitore
						if (oggORD.getBid_p() == null) {
							rec.setIdOffertaFornOrdine("");
						} else {
							rec.setIdOffertaFornOrdine(oggORD.getBid_p());
						}

						try {
							rec.setFornitore(new StrutturaCombo("", ""));

						} catch (Exception e) {
							// log.error("", e);
						}
						if (oggORD.getCod_fornitore() != null) {
							rec.getFornitore().setCodice(
									String.valueOf(oggORD.getCod_fornitore()
											.getCod_fornitore()));
							rec.getFornitore().setDescrizione(
									oggORD.getCod_fornitore()
											.getNom_fornitore());

							if (rec.getFornitore().getCodice() == null
									|| (rec.getFornitore().getCodice() != null && rec
											.getFornitore().getCodice().trim()
											.length() == 0)
									|| rec.getFornitore().getDescrizione() == null
									|| (rec.getFornitore().getDescrizione() != null && rec
											.getFornitore().getDescrizione()
											.trim().length() == 0)) {
								rec.getFornitore().setDescrizione(
										"fornitore non presente su base dati");
							}
							// dati fornitore per stampe

							rec.setAnagFornitore(new FornitoreVO());
							rec.getAnagFornitore().setCodFornitore(
									String.valueOf(oggORD.getCod_fornitore()
											.getCod_fornitore()));
							rec.getAnagFornitore().setNomeFornitore(
									oggORD.getCod_fornitore()
											.getNom_fornitore());
							if (rec.getFornitore().getCodice() == null
									|| (rec.getFornitore().getCodice() != null && rec
											.getFornitore().getCodice().trim()
											.length() == 0)
									|| rec.getFornitore().getDescrizione() == null
									|| (rec.getFornitore().getDescrizione() != null && rec
											.getFornitore().getDescrizione()
											.trim().length() == 0)) {
								rec.getAnagFornitore().setNomeFornitore(
										"fornitore non presente su base dati");
							}
							if (oggORD.getCod_fornitore().getIndirizzo() != null
									&& oggORD.getCod_fornitore().getIndirizzo()
											.trim().length() > 0) {
								rec.getAnagFornitore().setIndirizzo(
										oggORD.getCod_fornitore()
												.getIndirizzo());
							}
							if (oggORD.getCod_fornitore().getCitta() != null
									&& oggORD.getCod_fornitore().getCitta()
											.trim().length() > 0) {
								rec.getAnagFornitore().setCitta(
										oggORD.getCod_fornitore().getCitta());
							}
							if (oggORD.getCod_fornitore().getCap() != null
									&& oggORD.getCod_fornitore().getCap()
											.trim().length() > 0) {
								rec.getAnagFornitore().setCap(
										oggORD.getCod_fornitore().getCap());
							}
							if (oggORD.getCod_fornitore().getPaese() != null
									&& oggORD.getCod_fornitore().getPaese()
											.trim().length() > 0) {
								rec.getAnagFornitore().setPaese(
										oggORD.getCod_fornitore().getPaese());
							}
							if (oggORD.getCod_fornitore().getCod_fiscale() != null
									&& oggORD.getCod_fornitore()
											.getCod_fiscale().trim().length() > 0) {
								rec.getAnagFornitore().setCodiceFiscale(
										oggORD.getCod_fornitore()
												.getCod_fiscale());
							}
							if (oggORD.getCod_fornitore().getFax() != null
									&& oggORD.getCod_fornitore().getFax()
											.trim().length() > 0) {
								rec.getAnagFornitore().setFax(
										oggORD.getCod_fornitore().getFax());
							}
							if (oggORD.getCod_fornitore().getTelefono() != null
									&& oggORD.getCod_fornitore().getTelefono()
											.trim().length() > 0) {
								rec.getAnagFornitore()
										.setTelefono(
												oggORD.getCod_fornitore()
														.getTelefono());
							}
							rec.getAnagFornitore().setFornitoreBibl(
									new DatiFornitoreVO());

						}

						rec.setNoteFornitore(oggORD.getNote_forn());
						rec.setTipoInvioOrdine(String.valueOf(oggORD
								.getTipo_invio()));
						try {

							rec.setBilancio(new StrutturaTerna("", "", ""));

						} catch (Exception e) {
							// log.error("", e);
						}
						if (oggORD.getTbb_bilancicod_mat() != null
								&& oggORD.getTbb_bilancicod_mat()
										.getId_capitoli_bilanci() != null) {
							if (gestBil
									&& (oggORD.getCod_tip_ord() == 'A'
											|| oggORD.getCod_tip_ord() == 'V' || oggORD
											.getCod_tip_ord() == 'R')
									&& oggORD.getTbb_bilancicod_mat()
											.getId_capitoli_bilanci()
											.getEsercizio().intValue() > 0) {
								try {
									rec.setBilancio(new StrutturaTerna(String
											.valueOf(oggORD
													.getTbb_bilancicod_mat()
													.getId_capitoli_bilanci()
													.getEsercizio()), String
											.valueOf(oggORD
													.getTbb_bilancicod_mat()
													.getId_capitoli_bilanci()
													.getCapitolo()), String
											.valueOf(oggORD
													.getTbb_bilancicod_mat()
													.getCod_mat())));

								} catch (Exception e) {
									// log.error("", e);
								}

							}
						}

						if (oggORD.getCod_1ord() == null
								|| (oggORD.getCod_1ord() != null && oggORD
										.getCod_1ord().equals(""))) {
							rec.setCodPrimoOrdine("");
						} else {
							rec.setCodPrimoOrdine(String.valueOf(oggORD
									.getCod_1ord()));
						}
						if (oggORD.getAnno_1ord() == null
								|| (oggORD.getAnno_1ord() != null && oggORD
										.getAnno_1ord().equals(""))) {
							rec.setAnnoPrimoOrdine("");
						} else {
							rec.setAnnoPrimoOrdine(String.valueOf(oggORD
									.getAnno_1ord()));
						}

						// rec.setValutaOrdine(rs.getString("valuta"));
						if (oggORD.getId_valuta() != null) {
							rec.setValutaOrdine(oggORD.getId_valuta()
									.getValuta());
						}

						// BigDecimal bd=cu.getCambio();
						rec.setPrezzoOrdine(oggORD.getPrezzo().doubleValue());
						rec.setPrezzoEuroOrdine(oggORD.getPrezzo_lire()
								.doubleValue());
						rec.setPaeseOrdine(oggORD.getPaese());
						if (gestSez
								&& (oggORD.getCod_tip_ord() == 'A' || oggORD
										.getCod_tip_ord() == 'V')) {
							if (oggORD.getId_sez_acquis_bibliografiche() != null) {
								rec.setSezioneAcqOrdine(oggORD
										.getId_sez_acquis_bibliografiche()
										.getCod_sezione());
							}

						} else {
							rec.setSezioneAcqOrdine("");
						}

						rec
								.setCodBibliotecaSuggOrdine(oggORD
										.getCod_bib_sugg());
						String isbd = "";
						String bid = "";
						String nStandard = "";
						List<StrutturaTerna> nStandardArr = null;
						// try {

						/*
						 * // solo per test if (rec.getIDOrd()==4) { String
						 * passa="si"; }
						 */
						if (oggORD.getBid() != null
								&& oggORD.getBid().trim().length() != 0) {
							// solo per test

							/*
							 * bid=rs.getString("bid"); isbd="titolo di test";
							 */
							bid = oggORD.getBid();
							try {
								TitoloACQVO recTit = null;
								// recTit =
								// this.getTitoloRox(rs.getString("bid"));
								Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
								// List risposta =
								// cambiDao.getTitoloOrdine(rs.getString("bid"));
								// recTit=
								// cambiDao.getTitoloOrdineBis(rs.getString("bid"));
								recTit = ordiniDao.getTitoloOrdineTerHib(oggORD
										.getBid());
								if (recTit != null && recTit.getIsbd() != null) {
									isbd = recTit.getIsbd();
								}
								if (recTit != null
										&& recTit.getNumStandard() != null) {
									nStandard = recTit.getNumStandard();
								}
								if (recTit != null
										&& recTit.getNumStandardArr() != null
										&& recTit.getNumStandardArr().size() > 0) {
									nStandardArr = recTit.getNumStandardArr();
								}

							} catch (Exception e) {
								isbd = "titolo non trovato";
							}
						}
						try {

							rec.setTitolo(new StrutturaCombo(bid, isbd));

						} catch (Exception e) {
							// log.error("", e);
						}

						rec.setTitoloIsbn("");
						rec.setTitoloIssn("");

						if (nStandardArr != null && nStandardArr.size() > 0) {
							rec.setNumStandardArr(nStandardArr);
						}

						rec.setStatoAbbOrdine(String.valueOf(oggORD
								.getStato_abb()));
						rec.setPeriodoValAbbOrdine(String.valueOf(oggORD
								.getCod_per_abb()));
						if (oggORD.getAnno_abb() == null
								|| (oggORD.getAnno_abb() != null && oggORD
										.getAnno_abb().equals(""))
								|| (oggORD.getAnno_abb() != null && oggORD
										.getAnno_abb().intValue() == 0)) {
							rec.setAnnoAbbOrdine("");
						} else {
							rec.setAnnoAbbOrdine(String.valueOf(oggORD
									.getAnno_abb()));
						}

						rec.setNumFascicoloAbbOrdine(oggORD.getNum_fasc());

						String data_fasc_Formattata = "";
						try {
							data_fasc_Formattata = format1.format(oggORD
									.getData_fasc());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec
								.setDataPubblFascicoloAbbOrdine(data_fasc_Formattata);

						rec.setAnnataAbbOrdine(oggORD.getAnnata());
						rec.setNumVolAbbOrdine(oggORD.getNum_vol_abb());

						String data_fine_Formattata = "";
						try {
							data_fine_Formattata = format1.format(oggORD
									.getData_fine());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataFineAbbOrdine(data_fine_Formattata);

						String data_chiusura_ord_Formattata = "";
						try {
							data_chiusura_ord_Formattata = format1
									.format(oggORD.getData_chiusura_ord());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataChiusura(data_chiusura_ord_Formattata);

						rec.setRegTribOrdine(oggORD.getReg_trib());
						rec.setNaturaOrdine(String.valueOf(oggORD.getNatura()));
						rec.setStampato(oggORD.getStampato());
						rec.setRinnovato(oggORD.getRinnovato());

						try {

							rec
									.setRinnovoOrigine(new StrutturaTerna("",
											"", "")); // accoglie l'ordine
							// originario o
							// precedente a seconda
							// della configurazione

						} catch (Exception e) {
							// log.error("", e);
						}
						// per la stampa

						if (rec.isContinuativo()
								&& rec.getCodPrimoOrdine() != null
								&& !rec.getCodPrimoOrdine().trim().equals("0")
								&& rec.getCodPrimoOrdine().trim().length() > 0
								&& rec.getAnnoPrimoOrdine() != null
								&& !rec.getAnnoPrimoOrdine().trim().equals("0")
								&& rec.getAnnoPrimoOrdine().trim().length() > 0) {
							rec.setRinnovoOrigine(dao.gestioneRinnovato(rec
									.getIDOrd(),
									rec.getCodPrimoOrdine().trim(), rec
											.getAnnoPrimoOrdine().trim(),
									tipoRinnovoConfigurato)); //
							// COSTRUZIONE DELLA CATENA DEI RINNOVI
						}
						//
						if (rec.getCodDocOrdine() != null
								&& rec.getCodDocOrdine().trim().length() > 0
								&& !rec.getCodDocOrdine().trim().equals("0")) {
							rec.setProvenienza(rec.getCodDocOrdine().trim()
									+ "-Sugg. lett.");
						} else if (rec.getCodSuggBiblOrdine() != null
								&& rec.getCodSuggBiblOrdine().trim().length() > 0
								&& !rec.getCodSuggBiblOrdine().trim().equals(
										"0")) {
							rec.setProvenienza(rec.getCodSuggBiblOrdine()
									.trim()
									+ "-Sugg. bibliotec.");
						} else if (rec.getIdOffertaFornOrdine() != null
								&& rec.getIdOffertaFornOrdine().trim().length() > 0
								&& !rec.getIdOffertaFornOrdine().trim().equals(
										"0")) {
							rec.setProvenienza(rec.getIdOffertaFornOrdine()
									.trim()
									+ "-Offerte fornitore");
						} else if (rec.getCodRicOffertaOrdine() != null
								&& rec.getCodRicOffertaOrdine().trim().length() > 0
								&& !rec.getCodRicOffertaOrdine().trim().equals(
										"0")) {
							rec.setProvenienza(rec.getCodRicOffertaOrdine()
									.trim()
									+ "-Gare");
						} else {
							rec.setProvenienza("");
						}

						// solo per ordini di rilegatura

						if (rec.getTipoOrdine()!=null && rec.getTipoOrdine().equals("R") && numRighe==1)
						{

							if (oggORD.getTra_ordine_inventari()!=null)
							{
								//oggORD.getId_ordine().getTra_ordine_inventari();
								//ordInv.setId_ordine(oggORD); []
								try {
									//List<Tra_ordine_inventari> elencoAtt = new ArrayList<Tra_ordine_inventari >(oggORD.getTra_ordine_inventari());
									Object[] elencoInv=oggORD.getTra_ordine_inventari().toArray();
									// vanno prima adeguati tra_ordine_inventari in StrutturaInventariOrdVO prima del set successivo
									//rec.setRigheInventariRilegatura(elencoAtt);
									List<StrutturaInventariOrdVO> arrayInv = new ArrayList<StrutturaInventariOrdVO >();
									for (int j = 0; j<elencoInv.length; j++) {
										Tra_ordine_inventari eleInv = (Tra_ordine_inventari)elencoInv[j];
										StrutturaInventariOrdVO newEleInv = new StrutturaInventariOrdVO(rec.getCodPolo(), rec.getCodBibl());
										//newEleInv.setCodPolo(eleInv.getCd_polo().);
										//newEleInv.setCodBibl(eleInv.getCd_polo().getCd_bib_ord());

										newEleInv.setBid(eleInv.getId_ordine().getBid());
										newEleInv.setIDOrd(eleInv.getId_ordine().getId_ordine());
										if (eleInv.getData_rientro()!=null)
										{
											newEleInv.setDataRientro(eleInv.getData_rientro().toString());
										}
										if (eleInv.getData_rientro_presunta() != null) {
											newEleInv
													.setDataRientroPresunta(eleInv
															.getData_rientro_presunta()
															.toString());
										}
										if (eleInv.getData_uscita() != null) {
											newEleInv.setDataUscita(eleInv
													.getData_uscita()
													.toString());
										}
										if (eleInv.getCd_polo().getCd_serie()
												.getCd_serie() != null) {
											newEleInv.setSerie(eleInv
													.getCd_polo().getCd_serie()
													.getCd_serie());
										}
										newEleInv.setNumero(String
												.valueOf(eleInv.getCd_polo()
														.getCd_inven()));
										if (eleInv.getOta_fornitore() != null) {
											newEleInv.setNote(eleInv
													.getOta_fornitore());
										}

										// session.save(newpar);
										arrayInv.add(newEleInv);
									}
									rec.setRigheInventariRilegatura(arrayInv);

								} catch (Exception e) {
									log.error("", e);
								}
							}
						}

						listaOrdini.add(rec);
					} // End for
				} // fine if results

			} catch (ValidationException e) {
				throw e;

			} catch (Exception e) {

				log.error("", e);

			}
			return listaOrdini;
		} // fine if (testxHib)

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciOrdine(OrdiniVO ordine) throws DataException,
			ApplicationException, ValidationException {
		// return dao.inserisciOrdine( ordine);
		boolean ok = false;

		try {
			// return dao.modificaOrdine(ordine);
			try {
				ok = dao.inserisciOrdine(ordine);
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {

				// l'errore capita in questo punto
				log.error("", e);
			}
			// si procede con l'aggiornamento del bilancio per i dati calcolati
			if (ok
					&& ordine.isGestBil()
					&& ordine.getTipoOrdine() != null
					&& (ordine.getTipoOrdine().equals("A")
							|| ordine.getTipoOrdine().equals("V") || ordine
							.getTipoOrdine().equals("R"))) {
				ListaSuppBilancioVO ricercaBilanci = new ListaSuppBilancioVO();
				ricercaBilanci.setCodPolo(ordine.getCodPolo());
				ricercaBilanci.setCodBibl(ordine.getCodBibl());
				ricercaBilanci.setEsercizio(ordine.getBilancio().getCodice1());
				ricercaBilanci.setCapitolo(ordine.getBilancio().getCodice2());
				String cod_mat = ValidazioneDati.trimOrNull(ordine.getBilancio().getCodice3());
				ricercaBilanci.setImpegno(cod_mat);

				List<BilancioVO> elenco = dao.getRicercaListaBilanci(ricercaBilanci);
				if (elenco != null && elenco.size() == 1) {
					BilancioVO bilInOggetto = elenco.get(0);
					bilInOggetto.setUtente(ordine.getUtente());
					try {
						ok = false;
						ok = dao.modificaBilancio(bilInOggetto, cod_mat);

					} catch (Exception e) {

						// l'errore capita in questo punto
						log.error("", e);
					}
				} else {
					// mancanza di intercettazione dell'eccezione dalla ricerca
					ok = false;
				}

			}
			// si procede con l'aggiornamento della sezione per i dati calcolati
			if (ok
					&& ordine.isGestSez()
					&& ordine.getTipoOrdine() != null
					&& (ordine.getTipoOrdine().equals("A") || ordine
							.getTipoOrdine().equals("V"))) {
				ListaSuppSezioneVO ricercaSezioni = new ListaSuppSezioneVO();
				ricercaSezioni.setCodPolo(ordine.getCodPolo());
				ricercaSezioni.setCodBibl(ordine.getCodBibl());
				ricercaSezioni.setIdSezione(ordine.getIDSez());

				List<SezioneVO> elenco = dao.getRicercaListaSezioni(ricercaSezioni);
				if (elenco != null && elenco.size() == 1) {
					SezioneVO sezInOggetto = elenco.get(0);
					sezInOggetto.setUtente(ordine.getUtente());
					try {
						ok = false;
						ok = dao.modificaSezione(sezInOggetto);

					} catch (Exception e) {

						// l'errore capita in questo punto
						log.error("", e);
					}
				} else {
					// mancanza di intercettazione dell'eccezione dalla
					// getricerca
					ok = false;
				}

			}

		} catch (ValidationException e) {
			// throw new ValidationException(e.getMessage(),
			// ValidationException.errore);
			throw e;

		} catch (Exception e) {

			log.error("", e);

		}
		if (!ok)
			ctx.setRollbackOnly();

		return ok;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciOrdineBiblHib(OrdiniVO ordine,
			List listaBiblAff) throws DataException, ApplicationException {
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
		Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
		Tra_ordini_biblioteche oggBibl = new Tra_ordini_biblioteche();

		Tbf_polo polo = new Tbf_polo();
		if (ordine.getCodPolo() != null) {
			polo.setCd_polo(ordine.getCodPolo());
		}
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		oggBibl.setCod_bib_ord(ordine.getCodBibl()); // codice identificativo
		// della biblioteca che
		// effettua l'ordine
		oggBibl.setCod_tip_ord(ordine.getTipoOrdine().charAt(0));
		oggBibl.setAnno_ord(BigDecimal.valueOf(Double.valueOf(ordine
				.getAnnoOrdine())));
		oggBibl.setCod_ord(Integer.parseInt(ordine.getCodOrdine()));
		oggBibl.setFl_canc('N');
		oggBibl.setTs_var(ts);
		oggBibl.setUte_var(ordine.getUtente());
		oggBibl.setTs_ins(ts);
		oggBibl.setUte_ins(ordine.getUtente());
		boolean output = true;
		try {
			for (int j = 0; j < listaBiblAff.size(); j++) {
				if (listaBiblAff.get(j) != null) {
					bib.setCd_biblioteca((String) listaBiblAff.get(j));
					if (polo != null) {
						bib.setCd_polo(polo);
					}
				}
				oggBibl.setCd_bib(bib);
				boolean ret = ordiniDao.inserisciOrdineBiblHib(oggBibl);
				if (!ret) {
					output = false;
				}
			}
		} catch (DaoManagerException e) {
			output = false;
			throw new ApplicationException(e);
		}
		return true;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaOrdine(OrdiniVO ordine) throws DataException,
			ApplicationException, ValidationException {
		boolean ok = false;
		try {
			try {
				ok = dao.modificaOrdine(ordine);

				// almaviva5_20121121 evolutive google
				OrdineCarrelloSpedizioneVO ocs = ordine.getOrdineCarrelloSpedizione();
				if (ok && ocs != null) {
					Tba_ordiniDao odao = new Tba_ordiniDao();
					Tra_ordine_carrello_spedizione ordine_carrello_spedizione = ConversioneHibernateVO
							.toHibernate().ordineCarrelloSpedizione(ocs);
					odao.salvaOrdineCarrelloSpedizione(ordine_carrello_spedizione);
				}
			} catch (ApplicationException e) {
				throw e;
			} catch (ValidationException e) {
				throw e;

			} catch (DAOConcurrentException e) {
				throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);

			} catch (Exception e) {

				// l'errore capita in questo punto
				log.error("", e);
			}
			// si procede con l'aggiornamento del bilancio per i dati calcolati
			String tipoOrdine = ordine.getTipoOrdine();
			if (ok	&& ordine.isGestBil()
					&& ValidazioneDati.in(tipoOrdine, "A", "V", "R")) {
				ListaSuppBilancioVO ricercaBilanci = new ListaSuppBilancioVO();
				ricercaBilanci.setCodPolo(ordine.getCodPolo());
				ricercaBilanci.setCodBibl(ordine.getCodBibl());
				ricercaBilanci.setEsercizio(ordine.getBilancio().getCodice1());
				ricercaBilanci.setCapitolo(ordine.getBilancio().getCodice2());
				String cod_mat = ValidazioneDati.trimOrNull(ordine.getBilancio().getCodice3());
				ricercaBilanci.setImpegno(cod_mat);

				List<BilancioVO> elenco = dao.getRicercaListaBilanci(ricercaBilanci);
				if (elenco != null && elenco.size() == 1) {
					BilancioVO bilInOggetto = elenco.get(0);
					bilInOggetto.setUtente(ordine.getUtente());
					try {
						ok = false;
						ok = dao.modificaBilancio(bilInOggetto, cod_mat);

					} catch (Exception e) {

						// l'errore capita in questo punto
						log.error("", e);
					}
				} else {
					// mancanza di intercettazione dell'eccezione dalla ricerca
					ok = false;
				}
			}
			// si procede con l'aggiornamento della sezione per i dati calcolati
			if (ok	&& ordine.isGestSez()
					&& ValidazioneDati.in(tipoOrdine, "A", "V")) {
				ListaSuppSezioneVO ricercaSezioni = new ListaSuppSezioneVO();
				ricercaSezioni.setCodPolo(ordine.getCodPolo());
				ricercaSezioni.setCodBibl(ordine.getCodBibl());
				ricercaSezioni.setIdSezione(ordine.getIDSez());

				List<SezioneVO> elenco = dao.getRicercaListaSezioni(ricercaSezioni);
				if (elenco != null && elenco.size() == 1) {
					SezioneVO sezInOggetto = elenco.get(0);
					sezInOggetto.setUtente(ordine.getUtente());
					try {
						ok = false;
						ok = dao.modificaSezione(sezInOggetto);

					} catch (Exception e) {

						// l'errore capita in questo punto
						log.error("", e);
					}
				} else {
					// mancanza di intercettazione dell'eccezione dalla ricerca
					ok = false;
				}
			}

		} catch (ValidationException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
		}

		if (!ok)
			ctx.setRollbackOnly();

		return ok;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaOrdine(OrdiniVO ordine) throws DataException,
			ApplicationException, ValidationException {
		PeriodiciDAO pdao = new PeriodiciDAO();
		// almaviva5_20110715 #4570 esercizio
		try {
			if (pdao.countEsemplariFascicoloOrdine(ordine.getIDOrd()) > 0)
				throw new ApplicationException(
						SbnErrorTypes.PER_ERRORE_ESISTE_FASCICOLO_LEGATO_ORDINE);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		}

		return dao.cancellaOrdine(ordine);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaBiblAffiliate(String codiceBiblioteca,
			String codiceAttivita) throws ResourceNotFoundException,
			ApplicationException, ValidationException {
		return dao.getRicercaBiblAffiliate(codiceBiblioteca, codiceAttivita);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaFornitori(
			ListaSuppFornitoreVO ricercaFornitori)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaFornitori = new ArrayList();
		List listaFornitoriAggiornata = new ArrayList();
		List results = null;

		if (!testxHib) {
			listaFornitori = dao.getRicercaListaFornitori(ricercaFornitori);
			if (listaFornitori.size() > 0) {
				FornitoreVO fornitore;

				for (int i = 0; i < listaFornitori.size(); i++) {
					fornitore = (FornitoreVO) listaFornitori.get(i);
					boolean ret = false;
					ret = daoEditore.gestioneEditoreNumStandard(fornitore, "I");
					listaFornitoriAggiornata.add(fornitore);
					if (!ret) {
						return listaFornitori;
					}
				}
				return listaFornitoriAggiornata;
			} else {
				return listaFornitori;
			}
		} else {
			try {
				Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
				ValidaListaSuppFornitoreVO(ricercaFornitori);
				results = fornitoriDao
						.getRicercaListaFornitoriHib(ricercaFornitori);
				if (results != null && results.size() > 0) {
					if (results.size() > 1000
							&& ricercaFornitori.getTipoOperazionePicos() == null
							&& !ricercaFornitori.isStampaForn()) {
						throw new ValidationException(
								"ricercaDaRaffinareTroppi",
								ValidationExceptionCodici.ricercaDaRaffinareTroppi);
					}
					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						FornitoreVO rec = new FornitoreVO();
						Tbr_fornitori oggFORN = (Tbr_fornitori) results.get(i);

						// vanno prima adeguati tra_ordine_inventari in
						// StrutturaInventariOrdVO prima del set successivo
						// rec.setRigheInventariRilegatura(elencoAtt);

						rec.setProgressivo(numRighe);
						rec.setCodPolo(ricercaFornitori.getCodPolo());
						// rec.setCodBibl();
						if (ricercaFornitori.getCodBibl() != null
								&& ricercaFornitori.getCodBibl().length() != 0) {
							rec.setCodBibl(ricercaFornitori.getCodBibl());
						} else {
							rec.setCodBibl("");
						}
						rec.setCodFornitore(String.valueOf(oggFORN
								.getCod_fornitore()));
						rec.setNomeFornitore(oggFORN.getNom_fornitore().trim());
						rec.setUnitaOrg(oggFORN.getUnit_org().trim());
						rec.setIndirizzo(oggFORN.getIndirizzo().trim());
						rec.setIndirizzoComposto("");
						if (oggFORN.getPaese() != null
								&& oggFORN.getPaese().trim().length() > 0) {
							String appoInd = rec.getIndirizzoComposto();
							rec.setIndirizzoComposto(appoInd + " "
									+ oggFORN.getPaese().trim());
						}
						if (oggFORN.getProvincia() != null
								&& oggFORN.getProvincia().trim().length() > 0) {
							String appoInd = rec.getIndirizzoComposto();
							rec.setIndirizzoComposto(appoInd + " "
									+ oggFORN.getProvincia().trim());
						}
						if (oggFORN.getCap() != null
								&& oggFORN.getCap().trim().length() > 0) {
							String appoInd = rec.getIndirizzoComposto();
							rec.setIndirizzoComposto(appoInd + " "
									+ oggFORN.getCap().trim());
						}
						if (oggFORN.getCitta() != null
								&& oggFORN.getCitta().trim().length() > 0) {
							String appoInd = rec.getIndirizzoComposto();
							rec.setIndirizzoComposto(appoInd + " "
									+ oggFORN.getCitta().trim());
						}
						if (oggFORN.getIndirizzo() != null
								&& oggFORN.getIndirizzo().trim().length() > 0) {
							String appoInd = rec.getIndirizzoComposto();
							rec.setIndirizzoComposto(appoInd + " "
									+ oggFORN.getIndirizzo().trim());
						}

						rec.setCasellaPostale(oggFORN.getCpostale().trim());
						rec.setCitta(oggFORN.getCitta().trim());
						rec.setCap(oggFORN.getCap().trim());
						rec.setTelefono(oggFORN.getTelefono().trim());
						rec.setFax(oggFORN.getFax().trim());
						rec.setNote(oggFORN.getNote().trim());
						rec.setPartitaIva(oggFORN.getP_iva().trim());
						rec.setCodiceFiscale(oggFORN.getCod_fiscale().trim());
						rec.setEmail(oggFORN.getE_mail().trim());
						rec.setPaese(oggFORN.getPaese());
						rec.setTipoPartner(String.valueOf(oggFORN
								.getTipo_partner()));
						rec.setProvincia(oggFORN.getProvincia());
						rec.setBibliotecaFornitore(oggFORN.getCod_bib());
						rec.setBibliotecaFornitoreCodPolo(oggFORN
								.getCod_polo_bib());
						rec.setDataUpd(oggFORN.getTs_var());

						rec.setFornitoreBibl(null);
						List<DatiFornitoreVO> arrayFornBibl = new ArrayList<DatiFornitoreVO>();

						if (oggFORN.getTbr_fornitori_biblioteche() != null) {
							try {
								Object[] elencoFornBibl = oggFORN
										.getTbr_fornitori_biblioteche()
										.toArray();
								for (int j = 0; j < elencoFornBibl.length; j++) {
									Tbr_fornitori_biblioteche eleFornBibl = (Tbr_fornitori_biblioteche) elencoFornBibl[j];
									DatiFornitoreVO newEleFornBibl = new DatiFornitoreVO();

									Tbf_biblioteca_in_polo appoBib = eleFornBibl
											.getCd_biblioteca();
									newEleFornBibl.setCodBibl(appoBib
											.getCd_biblioteca());
									newEleFornBibl.setCodPolo(appoBib
											.getCd_polo().getCd_polo());
									if (eleFornBibl.getCod_fornitore() > 0) {
										newEleFornBibl.setCodFornitore(String
												.valueOf(eleFornBibl
														.getCod_fornitore()));
									}
									if (eleFornBibl.getTipo_pagamento() != null) {
										newEleFornBibl
												.setTipoPagamento(eleFornBibl
														.getTipo_pagamento());
									}
									if (eleFornBibl.getCod_cliente() != null) {
										newEleFornBibl
												.setCodCliente(eleFornBibl
														.getCod_cliente());
									}
									if (eleFornBibl.getNom_contatto() != null) {
										newEleFornBibl
												.setNomContatto(eleFornBibl
														.getNom_contatto());
									}
									if (eleFornBibl.getTel_contatto() != null) {
										newEleFornBibl
												.setTelContatto(eleFornBibl
														.getTel_contatto());
									}
									if (eleFornBibl.getFax_contatto() != null) {
										newEleFornBibl
												.setFaxContatto(eleFornBibl
														.getFax_contatto());
									}
									if (eleFornBibl.getValuta() != null) {
										newEleFornBibl.setValuta(eleFornBibl
												.getValuta());
									}
									arrayFornBibl.add(newEleFornBibl);
								}
							} catch (Exception e) {
								log.error("", e);
							}
						}

						if (arrayFornBibl != null && arrayFornBibl.size() > 0) {
							rec.setFornitoreBibl(arrayFornBibl.get(0));
						}
						// lettura profili legati ad un fornitore
						ListaSuppFornitoreVO ricercaProfFornitori = new ListaSuppFornitoreVO();
						if (rec.getFornitoreBibl() != null) {
							ricercaProfFornitori.setCodFornitore(rec
									.getFornitoreBibl().getCodFornitore());
							ricercaProfFornitori.setCodPolo(rec
									.getFornitoreBibl().getCodPolo());
							ricercaProfFornitori.setCodBibl(rec
									.getFornitoreBibl().getCodBibl());

							List<Tba_profili_acquisto> resultsProf = null;
							try {
								resultsProf = fornitoriDao
										.getProfiliFornitoreHib(ricercaProfFornitori);
							} catch (Exception e) {
								log.error("", e);
							}

							if (resultsProf != null && resultsProf.size() > 0) {
								List<StrutturaProfiloVO> listaProfili = new ArrayList<StrutturaProfiloVO>();
								for (int t = 0; t < resultsProf.size(); t++) {
									StrutturaProfiloVO prof = new StrutturaProfiloVO(
											"",
											ricercaProfFornitori.getCodBibl(),
											new StrutturaCombo(String
													.valueOf(resultsProf.get(t)
															.getCod_prac()), ""),
											new StrutturaCombo("", ""),
											new StrutturaCombo("", ""),
											new StrutturaCombo("", ""), null,
											""); // 24.07.09
									// resultsProf.get(t).getCod_prac().toString()
									listaProfili.add(prof);
								} //
								rec.getFornitoreBibl().setProfiliAcq(
										listaProfili);

							}

						}
						listaFornitori.add(rec);
					}
				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaFornitori(listaFornitori);
		return listaFornitori;

	}

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la ricerca dei titoli collegati in modo
	// esplicito o implicito all'editore selezionato
	public List getRicercaTitCollEditori(
			RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return daoEditore.getRicercaTitCollEditori(ricercaTitCollEditoriVO,
				ticket);
	}

	public List getRicercaEditCollTitolo(
			RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return daoEditore.getRicercaEditCollTitolo(ricercaTitCollEditoriVO,
				ticket);
	}

	public List getAreeIsbdListaBid(List listaBid)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		try {
			return daoEditore.getAreeIsbdListaBid(listaBid);
		} catch (DaoManagerException e) {

			log.error("", e);
		}
		return null;
	}

	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// inserimento occorrenza nella tabella tr_editore_titolo che contiene i
	// legami espliciti fra titolo e fornitore/editore
	public AreaDatiVariazioneReturnVO gestioneLegameTitEdit(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket)
			throws DataException, ApplicationException, ValidationException {
		Boolean testxHib = false;
		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();
		if (!testxHib) {
			areaDatiVariazioneReturnVO = daoEditore.gestioneLegameTitEdit(
					areaDatiPass, ticket);
		}
		return areaDatiVariazioneReturnVO;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciFornitore(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List results = null;
		boolean ret = false;
		boolean retPrimario = false;
		boolean retSecondario = false;
		boolean retTer = false;
		boolean datiFornBiblAssenti = false;

		if (!testxHib) {
			ret = dao.inserisciFornitore(fornitore);
			if (ret) {
				ret = daoEditore.gestioneEditoreNumStandard(fornitore, "G");
			}
		} else {
			try {
				Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
				this.ValidaFornitoreVO(fornitore);
				// effettuare prima la ricerca: se esiste non effettuare
				// l'inserimento
				ListaSuppFornitoreVO listaSupp = this
						.trasformaFornitoreInlistaSupp(fornitore);
				if (listaSupp != null) {
					List elenco = null;
					elenco = fornitoriDao
							.getRicercaListaFornitoriHib(listaSupp);
					if (elenco == null) {
						// inserimento

						Tbr_fornitori forn = new Tbr_fornitori();

						Tbf_polo polo = new Tbf_polo();
						// polo.setCd_polo(ricercaCambi.getCodPolo());
						polo.setCd_polo(fornitore.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_biblioteca(fornitore.getCodBibl());
						bib.setCd_polo(polo);
						// DATA di sistema
						Timestamp ts = new java.sql.Timestamp(System
								.currentTimeMillis());
						// pro.setCd_bib(bib);

						forn.setNom_fornitore(fornitore.getNomeFornitore()
								.trim().replace("'", "''"));
						forn.setUnit_org(fornitore.getUnitaOrg());
						forn.setIndirizzo(fornitore.getIndirizzo());

						forn.setCpostale(fornitore.getCasellaPostale()); // cpostale
						forn.setCitta(fornitore.getCitta()); // citta
						forn.setCap(fornitore.getCap()); // cap
						forn.setTelefono(fornitore.getTelefono()); // telefono
						forn.setFax(fornitore.getFax()); // fax
						forn.setNote(fornitore.getNote().trim().replace("'",
								"''")); // note
						forn.setP_iva(fornitore.getPartitaIva()); // p_iva
						forn.setCod_fiscale(fornitore.getCodiceFiscale()); // cod_fiscale
						forn.setE_mail(fornitore.getEmail()); // e_mail
						forn.setPaese(fornitore.getPaese()); // paese
						forn.setTipo_partner(fornitore.getTipoPartner().charAt(
								0)); // tipo_partner
						forn.setProvincia(fornitore.getProvincia()); // provincia
						if (!fornitore.getTipoPartner().equals("B")) {
							fornitore.setBibliotecaFornitore("");
							fornitore.setBibliotecaFornitoreCodPolo("");
						} else // biblioteca come forn
						{
							if (fornitore.getBibliotecaFornitore() == null
									|| fornitore
											.getBibliotecaFornitoreCodPolo() == null
									|| fornitore.getBibliotecaFornitore()
											.length() != 3
									|| fornitore
											.getBibliotecaFornitoreCodPolo()
											.length() != 3) {
								fornitore.setBibliotecaFornitore("");
								fornitore.setBibliotecaFornitoreCodPolo("");
							}
						}

						forn.setCod_bib(fornitore.getBibliotecaFornitore()); // cd_bib
						forn.setChiave_for(fornitore.getChiaveFor()); // chiave_for
						forn.setCod_polo_bib(fornitore
								.getBibliotecaFornitoreCodPolo()); // cod_polo_bib

						forn.setUte_ins(fornitore.getUtente());
						forn.setTs_ins(ts);
						forn.setUte_var(fornitore.getUtente());
						forn.setTs_var(ts);
						forn.setFl_canc('N');


						retPrimario = fornitoriDao.inserisciFornitoreHib(forn);

						// rilettura fornitore per acquisire l'id attribuito
						listaSupp.setLocale("0");
						elenco = fornitoriDao
								.getRicercaListaFornitoriHib(listaSupp);
						;
						int idForn = 0;
						Set listaFornBib = null;
						if (elenco != null && elenco.size() == 1) {
							idForn = ((List<Tbr_fornitori>) elenco).get(0)
									.getCod_fornitore();
							fornitore.setCodFornitore(String.valueOf(idForn));
							listaFornBib = ((List<Tbr_fornitori>) elenco)
									.get(0).getTbr_fornitori_biblioteche();
						} else {
							// lanciare eccezione
						}

						if (fornitore.getFornitoreBibl() != null) {
							if ((fornitore.getFornitoreBibl()
									.getTipoPagamento() == null || fornitore
									.getFornitoreBibl().getTipoPagamento()
									.trim().length() == 0)
									&& (fornitore.getFornitoreBibl()
											.getCodCliente() == null || fornitore
											.getFornitoreBibl().getCodCliente()
											.trim().length() == 0)
									&& (fornitore.getFornitoreBibl()
											.getNomContatto() == null || fornitore
											.getFornitoreBibl()
											.getNomContatto().trim().length() == 0)
									&& (fornitore.getFornitoreBibl()
											.getTelContatto() == null || fornitore
											.getFornitoreBibl()
											.getTelContatto().trim().length() == 0)
									&& (fornitore.getFornitoreBibl()
											.getFaxContatto() == null || fornitore
											.getFornitoreBibl()
											.getFaxContatto().trim().length() == 0)
									// considerando la valuta i dati della
									// biblioteca operante anche se nulli
									// vengono registrati su sottotabella
									&& (fornitore.getFornitoreBibl()
											.getValuta() == null || fornitore
											.getFornitoreBibl().getValuta()
											.length() == 0)

							) {
								datiFornBiblAssenti = true;
							}
						}
						if (!datiFornBiblAssenti && retPrimario
								&& fornitore.getFornitoreBibl() != null) {
							// inserimento fornitore biblioteca
							Tbr_fornitori_biblioteche fornBibl = new Tbr_fornitori_biblioteche();
							fornBibl.setCd_biblioteca(bib);
							fornBibl.setCod_fornitore(idForn); // cod_fornitore
							fornBibl.setTipo_pagamento(fornitore
									.getFornitoreBibl().getTipoPagamento()
									.trim()); // tipo_pagamento
							fornBibl.setCod_cliente(fornitore
									.getFornitoreBibl().getCodCliente().trim()); // cod_cliente
							fornBibl.setNom_contatto(fornitore
									.getFornitoreBibl().getNomContatto().trim()
									.replace("'", "''")); // nom_contatto
							fornBibl
									.setTel_contatto(fornitore
											.getFornitoreBibl()
											.getTelContatto().trim()); // tel_contatto
							fornBibl
									.setFax_contatto(fornitore
											.getFornitoreBibl()
											.getFaxContatto().trim()); // fax_contatto
							fornBibl.setValuta(fornitore.getFornitoreBibl()
									.getValuta()); // valuta
							fornBibl.setCod_polo(fornitore.getFornitoreBibl()
									.getCodPolo()); // cod_polo
							fornBibl.setAllinea(' '); // allinea
							fornBibl.setUte_ins(fornitore.getUtente());
							fornBibl.setTs_ins(ts);
							fornBibl.setUte_var(fornitore.getUtente());
							fornBibl.setTs_var(ts);
							fornBibl.setFl_canc('N');
							// ASSOCIAZIONE PROFILI ACQUISTO AL FORNITORE solo
							// se sono fornitori di biblioteca
							if (fornitore.getFornitoreBibl() != null
									&& fornitore.getFornitoreBibl()
											.getProfiliAcq() != null
									&& fornitore.getFornitoreBibl()
											.getProfiliAcq().size() > 0) {
								Set listaProfiliForn = new HashSet<Tra_sez_acquisizione_fornitori>();
								for (int i = 0; i < fornitore
										.getFornitoreBibl().getProfiliAcq()
										.size(); i++) {
									StrutturaProfiloVO oggettoDettProfilo = fornitore
											.getFornitoreBibl().getProfiliAcq()
											.get(i);
									String oggettoDettProfiloCod = oggettoDettProfilo
											.getProfilo().getCodice().trim();

									Tra_sez_acquisizione_fornitori profForn = new Tra_sez_acquisizione_fornitori();

									profForn.setCd_biblioteca(bib);
									profForn
											.setCod_prac(BigDecimal
													.valueOf(Double
															.valueOf(oggettoDettProfiloCod)));
									profForn.setCod_fornitore(idForn);
									profForn.setUte_ins(fornitore.getUtente());
									profForn.setTs_ins(ts);
									profForn.setUte_var(fornitore.getUtente());
									profForn.setTs_var(ts);
									profForn.setFl_canc('N');

									listaProfiliForn.add(profForn);
								}
								fornBibl
										.setTra_sez_acquisizione_fornitori(listaProfiliForn);

							}

							Set listaFornBib2 = new HashSet<Tbr_fornitori_biblioteche>();
							listaFornBib2.add(fornBibl);

							forn.setTbr_fornitori_biblioteche(listaFornBib2);
							// retSecondario=fornitoriDao.inserisciFornitoreBibliotecaHib(fornBibl);

							retSecondario = fornitoriDao
									.modificaFornitoreHib(forn);
						}

					}
				}
				if (retPrimario && retSecondario) {
					ret = true;
				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public boolean modificaFornitore(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaProfili = new ArrayList();
		List results = null;
		boolean ret = false;
		boolean retTer = false;
		boolean datiFornBiblAssenti = false;

		if (!testxHib) {
			ret = dao.modificaFornitore(fornitore);
			if (ret) {
				ret = daoEditore.gestioneEditoreNumStandard(fornitore, "G");
			}

		} else {
			try {
				Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
				this.ValidaFornitoreVO(fornitore);
				ListaSuppFornitoreVO listaSupp = this
						.trasformaFornitoreInlistaSupp(fornitore);
				// effettuare prima la ricerca: se esiste effettuare la modifica
				if (listaSupp != null) {
					if (fornitore.getFornitoreBibl() != null) {
						if ((fornitore.getFornitoreBibl().getTipoPagamento() == null || fornitore
								.getFornitoreBibl().getTipoPagamento().trim()
								.length() == 0)
								&& (fornitore.getFornitoreBibl()
										.getCodCliente() == null || fornitore
										.getFornitoreBibl().getCodCliente()
										.trim().length() == 0)
								&& (fornitore.getFornitoreBibl()
										.getNomContatto() == null || fornitore
										.getFornitoreBibl().getNomContatto()
										.trim().length() == 0)
								&& (fornitore.getFornitoreBibl()
										.getTelContatto() == null || fornitore
										.getFornitoreBibl().getTelContatto()
										.trim().length() == 0)
								&& (fornitore.getFornitoreBibl()
										.getFaxContatto() == null || fornitore
										.getFornitoreBibl().getFaxContatto()
										.trim().length() == 0)
								// considerando la valuta i dati della
								// biblioteca operante anche se nulli vengono
								// registrati su sottotabella
								&& (fornitore.getFornitoreBibl().getValuta() == null || fornitore
										.getFornitoreBibl().getValuta()
										.length() == 0)
						// && (fornitore.getFornitoreBibl().getCodPolo()==null
						// ||
						// fornitore.getFornitoreBibl().getCodPolo().length()==0)
						) {
							datiFornBiblAssenti = true;
						}
					}

					List elenco = null;
					elenco = fornitoriDao
							.getRicercaListaFornitoriHib(listaSupp);
					if (elenco != null && elenco.size() == 1) {
						// modifica
						Tbr_fornitori forn = ((List<Tbr_fornitori>) elenco)
								.get(0);

						if (!forn.getTs_var().equals(fornitore.getDataUpd())) {
							throw new ValidationException(
									"operazioneInConcorrenza",
									ValidationExceptionCodici.operazioneInConcorrenza);
						}

						Tra_sez_acquisizione_fornitori[] oldLegamiProfForn = null;
						Object[] elencoFornBibl = forn
								.getTbr_fornitori_biblioteche().toArray();
						if (elencoFornBibl != null && elencoFornBibl.length > 0) {
							Tbr_fornitori_biblioteche eleFornBibl = (Tbr_fornitori_biblioteche) elencoFornBibl[0];
							Object[] elencoProfFornBibl = eleFornBibl
									.getTra_sez_acquisizione_fornitori()
									.toArray();
							if (elencoProfFornBibl != null
									&& elencoProfFornBibl.length > 0) {
								// // individuazione dei precedenti profili
								// acquisto associati al fornitore
								oldLegamiProfForn = new Tra_sez_acquisizione_fornitori[elencoProfFornBibl.length];
								for (int j = 0; j < elencoProfFornBibl.length; j++) {
									oldLegamiProfForn[j] = (Tra_sez_acquisizione_fornitori) elencoProfFornBibl[j];
								}
								// cancellazione fisica di tutti i precedenti
								// profili acquisto associati al fornitore e
								// rimozione delle associazioni dalle collezioni
								for (int y = 0; y < oldLegamiProfForn.length; y++) {
									Tbr_fornitori_biblioteche pippo = (Tbr_fornitori_biblioteche) forn
											.getTbr_fornitori_biblioteche()
											.iterator().next();
									pippo
											.getTra_sez_acquisizione_fornitori()
											.remove(
													oldLegamiProfForn[y]);
									retTer = fornitoriDao
											.cancellaProfFornitoreHib(oldLegamiProfForn[y]);
								}
							}
						}

						Tbf_polo polo = new Tbf_polo();
						// polo.setCd_polo(ricercaCambi.getCodPolo());
						polo.setCd_polo(fornitore.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_biblioteca(fornitore.getCodBibl());
						bib.setCd_polo(polo);
						// DATA di sistema
						Timestamp ts = new java.sql.Timestamp(System
								.currentTimeMillis());

						forn.setNom_fornitore(fornitore.getNomeFornitore()
								.trim().replace("'", "''"));
						forn.setUnit_org(fornitore.getUnitaOrg().trim());
						forn.setIndirizzo(fornitore.getIndirizzo().trim()
								.replace("'", "''"));
						forn.setCpostale(fornitore.getCasellaPostale().trim()); // cpostale
						forn.setCitta(fornitore.getCitta().trim()); // citta
						forn.setCap(fornitore.getCap().trim()); // cap
						forn.setTelefono(fornitore.getTelefono().trim()); // telefono
						forn.setFax(fornitore.getFax().trim()); // fax
						forn.setNote(fornitore.getNote().trim().replace("'",
								"''")); // note
						forn.setP_iva(fornitore.getPartitaIva().trim()); // p_iva
						forn.setCod_fiscale(fornitore.getCodiceFiscale()); // cod_fiscale
						forn.setE_mail(fornitore.getEmail().trim()); // e_mail
						forn.setPaese(fornitore.getPaese()); // paese
						forn.setTipo_partner(fornitore.getTipoPartner().charAt(
								0)); // tipo_partner
						forn.setProvincia(fornitore.getProvincia()); // provincia
						if (!fornitore.getTipoPartner().equals("B")) {
							fornitore.setBibliotecaFornitore("");
							fornitore.setBibliotecaFornitoreCodPolo("");
						} else // biblioteca come forn
						{
							if (fornitore.getBibliotecaFornitore() == null
									|| fornitore
											.getBibliotecaFornitoreCodPolo() == null
									|| fornitore.getBibliotecaFornitore()
											.length() != 3
									|| fornitore
											.getBibliotecaFornitoreCodPolo()
											.length() != 3) {
								fornitore.setBibliotecaFornitore("");
								fornitore.setBibliotecaFornitoreCodPolo("");
							}
						}

						forn.setCod_bib(fornitore.getBibliotecaFornitore()); // cd_bib
						forn.setChiave_for(fornitore.getChiaveFor()); // chiave_for
						forn.setCod_polo_bib(fornitore
								.getBibliotecaFornitoreCodPolo()); // cod_polo_bib
						forn.setUte_ins(fornitore.getUtente());
						forn.setTs_ins(ts);
						forn.setUte_var(fornitore.getUtente());
						forn.setTs_var(ts);
						forn.setFl_canc('N');
						int idForn = forn.getCod_fornitore();

						if (!datiFornBiblAssenti
								&& fornitore.getFornitoreBibl() != null) {
							// inserimento fornitore biblioteca
							Tbr_fornitori_biblioteche fornBibl = new Tbr_fornitori_biblioteche();
							fornBibl.setCd_biblioteca(bib);
							fornBibl.setCod_fornitore(idForn); // cod_fornitore
							fornBibl.setTipo_pagamento(fornitore
									.getFornitoreBibl().getTipoPagamento()
									.trim()); // tipo_pagamento
							fornBibl.setCod_cliente(fornitore
									.getFornitoreBibl().getCodCliente().trim()); // cod_cliente
							fornBibl.setNom_contatto(fornitore
									.getFornitoreBibl().getNomContatto().trim()
									.replace("'", "''")); // nom_contatto
							fornBibl
									.setTel_contatto(fornitore
											.getFornitoreBibl()
											.getTelContatto().trim()); // tel_contatto
							fornBibl
									.setFax_contatto(fornitore
											.getFornitoreBibl()
											.getFaxContatto().trim()); // fax_contatto
							fornBibl.setValuta(fornitore.getFornitoreBibl()
									.getValuta()); // valuta
							fornBibl.setCod_polo(fornitore.getFornitoreBibl()
									.getCodPolo()); // cod_polo
							fornBibl.setAllinea(' '); // allinea
							fornBibl.setUte_ins(fornitore.getUtente());
							fornBibl.setTs_ins(ts);
							fornBibl.setUte_var(fornitore.getUtente());
							fornBibl.setTs_var(ts);
							fornBibl.setFl_canc('N');
							// ASSOCIAZIONE PROFILI ACQUISTO AL FORNITORE solo
							// se sono fornitori di biblioteca
							if (fornitore.getFornitoreBibl() != null
									&& fornitore.getFornitoreBibl()
											.getProfiliAcq() != null
									&& fornitore.getFornitoreBibl()
											.getProfiliAcq().size() > 0) {
								Set listaProfiliForn = new HashSet<Tra_sez_acquisizione_fornitori>();
								for (int i = 0; i < fornitore
										.getFornitoreBibl().getProfiliAcq()
										.size(); i++) {
									StrutturaProfiloVO oggettoDettProfilo = fornitore
											.getFornitoreBibl().getProfiliAcq()
											.get(i);
									String oggettoDettProfiloCod = oggettoDettProfilo
											.getProfilo().getCodice().trim();

									Tra_sez_acquisizione_fornitori profForn = new Tra_sez_acquisizione_fornitori();

									profForn.setCd_biblioteca(bib);
									profForn
											.setCod_prac(BigDecimal
													.valueOf(Double
															.valueOf(oggettoDettProfiloCod)));
									profForn.setCod_fornitore(idForn);
									profForn.setUte_ins(fornitore.getUtente());
									profForn.setTs_ins(ts);
									profForn.setUte_var(fornitore.getUtente());
									profForn.setTs_var(ts);
									profForn.setFl_canc('N');

									listaProfiliForn.add(profForn);
								}
								fornBibl
										.setTra_sez_acquisizione_fornitori(listaProfiliForn);
							} else {

							}
							Set listaFornBib2 = new HashSet<Tbr_fornitori_biblioteche>();
							listaFornBib2.add(fornBibl);
							forn.setTbr_fornitori_biblioteche(listaFornBib2);
							ret = fornitoriDao.modificaFornitoreHib(forn);

						}
					}
				}

			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */


	public List getRicercaListaBilanci(ListaSuppBilancioVO ricercaBilanci)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {

		Boolean testxHib = false;
		// testxHib=true;
		List listaBilanci = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaBilanci(ricercaBilanci);
		} else {
			try {
				Tbb_bilanciDao bilanciDao = new Tbb_bilanciDao();
				ValidaListaSuppBilancioVO(ricercaBilanci);
				results = bilanciDao.getRicercaListaBilanciHib(ricercaBilanci);
				if (results != null && results.size() > 0) {
					int numRighe = 0;
					int progrRighe = 1;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						BilancioVO rec = new BilancioVO();
						Tbb_capitoli_bilanci oggBIL = (Tbb_capitoli_bilanci) results
								.get(i);
						if (oggBIL.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						Tbf_biblioteca_in_polo appoBib = oggBIL.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setProgressivo(Integer.valueOf(progrRighe));
						rec.setIDBil(oggBIL.getId_capitoli_bilanci());
						rec.setEsercizio(String.valueOf(oggBIL.getEsercizio()));
						rec.setCapitolo(String.valueOf(oggBIL.getCapitolo()));
						rec.setBudgetDiCapitolo(oggBIL.getBudget()
								.doubleValue());
						rec.setDataUpd(oggBIL.getTs_var());
						boolean cercaImpegno = false;
						if (oggBIL.getTbb_bilanci() != null)
							try {
								Object elencoImp[] = oggBIL.getTbb_bilanci()
										.toArray();
								List arrayImp = new ArrayList();
								for (int j = 0; j < elencoImp.length; j++) {
									Tbb_bilanci eleImp = (Tbb_bilanci) elencoImp[j];
									BilancioDettVO newEleImp = new BilancioDettVO();
									newEleImp.setImpegno(String.valueOf(eleImp
											.getCod_mat()));
									if (ricercaBilanci.getImpegno() != null
											&& ricercaBilanci.getImpegno()
													.length() != 0
											&& ricercaBilanci
													.getImpegno()
													.trim()
													.equals(
															newEleImp
																	.getImpegno()))
										cercaImpegno = true;
									newEleImp.setBudget(eleImp.getBudget()
											.doubleValue());
									BigDecimal bd = eleImp.getBudget();
									bd.setScale(2, 4);
									newEleImp.setBudgetStr(String.valueOf(bd
											.doubleValue()));
									newEleImp.setImpegnato(eleImp.getOrdinato()
											.doubleValue());
									newEleImp.setFatturato(eleImp
											.getFatturato().doubleValue());
									newEleImp.setPagato(eleImp.getPagato()
											.doubleValue());
									newEleImp.setAcquisito(0.0D);
									Double acqBil = Double.valueOf(eleImp
											.getAcquisito().doubleValue());
									if (acqBil != null
											&& acqBil.doubleValue() > 0)
										newEleImp.setAcquisito(acqBil
												.doubleValue());
									newEleImp.setImpFatt(newEleImp
											.getImpegnato()
											- newEleImp.getFatturato());
									newEleImp.setDispCassa(newEleImp
											.getBudget()
											- newEleImp.getPagato());
									newEleImp.setDispCompetenza(newEleImp
											.getBudget()
											- newEleImp.getImpegnato());
									newEleImp.setDispCompetenzaAcq(newEleImp
											.getBudget()
											- newEleImp.getAcquisito());
									arrayImp.add(newEleImp);
								}

								rec.setDettagliBilancio(arrayImp);
							} catch (Exception e) {
								log.error("", e);
							}
						if (ricercaBilanci.getImpegno() != null
								&& ricercaBilanci.getImpegno().length() != 0) {
							if (cercaImpegno) {
								listaBilanci.add(rec);
								progrRighe++;
							}
						} else {
							listaBilanci.add(rec);
							progrRighe++;
						}
					}

				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}

		}
		ValidaRicercaBilanci(listaBilanci);
		return listaBilanci;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciFattura(FatturaVO fattura) throws DataException,
			ApplicationException, ValidationException {
		return dao.inserisciFattura(fattura);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciComunicazione(ComunicazioneVO com)
			throws DataException, ApplicationException, ValidationException {
		boolean ok = dao.inserisciComunicazione(com);
		if (ok && ValidazioneDati.isFilled(com.getFascicoli())) { // periodici
			try {
				getPeriodici().inserisciMessaggiFascicolo(com);
			} catch (Exception e) {
				return false;
			}

		}
		return ok;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public boolean inserisciBilancio(BilancioVO bilancio) throws DataException,
			ApplicationException, ValidationException {
		return dao.inserisciBilancio(bilancio);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public boolean modificaBilancio(BilancioVO bilancio) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaBilancio(bilancio, null);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaFattura(FatturaVO fattura) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaFattura(fattura);


	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaComunicazione(ComunicazioneVO com)
			throws DataException, ApplicationException, ValidationException {
		boolean ok = dao.modificaComunicazione(com);
		if (ok && ValidazioneDati.isFilled(com.getFascicoli())) { // periodici
			try {
				getPeriodici().inserisciMessaggiFascicolo(com);
			} catch (Exception e) {
				return false;
			}

		}
		return ok;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public List getRicercaListaSezioni(ListaSuppSezioneVO ricercaSezioni)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List<Tba_sez_acquis_bibliografiche> results = null;
		List<SezioneVO> listaSezioni = null;

		if (!testxHib) {
			return dao.getRicercaListaSezioni(ricercaSezioni);
		} else {
			Tba_sez_acquis_bibliografiche sa = null;
			SezioneVO rec = null;

			try {
				Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
				this.ValidaListaSuppSezioneVO(ricercaSezioni);
				results = sezioniDao.getRicercaListaSezioniHib(ricercaSezioni);
				this.ValidaRicercaSezioni(results);
				SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

				// ciclo di riempimento
				for (int index = 0; index < results.size(); index++) {
					sa = results.get(index);
					rec = new SezioneVO();
					// carica il resultset
					Tbf_polo polo = new Tbf_polo();
					polo.setCd_polo(ricercaSezioni.getCodPolo());
					Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
					bib.setCd_biblioteca(ricercaSezioni.getCodBibl());
					bib.setCd_polo(polo);

					rec.setProgressivo(index + 1);
					rec.setCodPolo(polo.getCd_polo());
					rec.setCodBibl(bib.getCd_biblioteca());
					rec
							.setDescrizioneSezione(sa.getNome().trim()
									.toUpperCase());
					rec.setCodiceSezione(sa.getCod_sezione().trim());

					BigDecimal bd = sa.getSomma_disp();
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					rec.setSommaDispSezione(bd.doubleValue());
					rec.setNoteSezione(sa.getNote());
					rec.setAnnoValiditaSezione(sa.getAnno_val().toString());

					String dataFormattata = "";
					try {
						dataFormattata = format1.format(sa.getTs_var());
					} catch (Exception e) {
						// log.error("", e);
					}
					rec.setDataAgg(dataFormattata);

					bd = sa.getBudget();
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					rec.setBudgetSezione(bd.doubleValue()); // tramite vo
					// riempie anche il
					// campo
					// importodelta
					rec.setBudgetLetto(bd.doubleValue());
					rec.setIdSezione(sa.getId_sez_acquis_bibliografiche());
					rec.setDataUpd(sa.getTs_var());

					dataFormattata = "";
					try {
						dataFormattata = format1.format(sa.getData_val());
					} catch (Exception e) {
						// log.error("", e);
					}
					rec.setDataVal(dataFormattata); // tramite vo riempie anche
					// il boolean chiusa

					// rec.setBudgetDiCapitolo(Double.valueOf(rs.getString("budgetCap")));
					// INIZIALIZZAZIONE
					if (listaSezioni == null) {
						listaSezioni = new ArrayList();
					}
					listaSezioni.add(rec);
				}
				// inizio
				int esercizio = 0;
				if (ricercaSezioni.getEsercizio() != null
						&& ricercaSezioni.getEsercizio().trim().length() != 0) {
					esercizio = Integer.valueOf(ricercaSezioni.getEsercizio());
				}

				// 09/01/09 i dati calcolati sono prelevati da db ma solo da
				// elaboraspesa può essere attivata la procedura calcola
				if (listaSezioni.size() == 1
						&& (esercizio > 0
								|| (ricercaSezioni.getDataOrdineDa() != null && ricercaSezioni
										.getDataOrdineDa().trim().length() > 0) || (ricercaSezioni
								.getDataOrdineA() != null && ricercaSezioni
								.getDataOrdineA().trim().length() > 0))) {
					CalcoliVO risult = new CalcoliVO();
					risult = dao.calcola(listaSezioni.get(0)
							.getCodPolo(), listaSezioni.get(0)
							.getCodBibl(), listaSezioni.get(0)
							.getIdSezione(), listaSezioni.get(0),
							0, null, esercizio, ricercaSezioni
									.getDataOrdineDa(), ricercaSezioni
									.getDataOrdineA(), ricercaSezioni
									.getTicket(), ricercaSezioni.getLoc());
					SezioneVO sezioneInOggetto = listaSezioni
							.get(0);
					double disp = sezioneInOggetto.getBudgetSezione();
					// String
					// dispStr=Pulisci.VisualizzaImporto(listaSezioni.get(i).getBudgetSezione());
					if (risult != null && risult.getOrdinato() != 0) {
						disp = sezioneInOggetto.getBudgetSezione()
								- risult.getOrdinato();
						// dispStr=Pulisci.VisualizzaImporto(disp);
					}
					sezioneInOggetto.setSommaDispSezione(disp);
				}

				if (listaSezioni.size() == 1 && ricercaSezioni.isStoria()) {

					List<Tra_sez_acq_storico> resultsSub = sezioniDao.getRicercaListaSezioneStoriaHib(ricercaSezioni);


					int numRighe2 = 0;
					List<StrutturaQuinquies> righeEsameStoria = new ArrayList<StrutturaQuinquies>();
					StrutturaQuinquies rigaEsameStoria;
					for (int index = 0; index < resultsSub.size(); index++) {
						Tra_sez_acq_storico subEle = resultsSub.get(index);
						rigaEsameStoria = new StrutturaQuinquies();

						// format1.format(oggORD.getData_ord());
						String dataFormattata = "";
						try {
							dataFormattata = format1.format(subEle
									.getData_var_bdg());
						} catch (Exception e) {
							// log.error("", e);
						}
						rigaEsameStoria.setCodice1(dataFormattata);

						String valoreStr = "0.00";
						rigaEsameStoria.setCodice2(valoreStr);
						if (subEle.getImporto_diff() != null) {
							DecimalFormatSymbols dfs = new DecimalFormatSymbols();
							dfs.setGroupingSeparator(',');
							dfs.setDecimalSeparator('.');
							// controllo formattazione con virgola separatore
							// dei decimali
							try {
								DecimalFormat df = new DecimalFormat(
										"#,##0.00", dfs);
								// importo
								String stringa = df.format(subEle
										.getImporto_diff().doubleValue());
								valoreStr = stringa;
							} catch (Exception e) {
								log.error("", e);
							}
						}
						if (valoreStr != null && !valoreStr.equals("0.00")) {
							rigaEsameStoria.setCodice2(valoreStr);
						}

						String bdgOldStr = "0.00";
						rigaEsameStoria.setCodice3(bdgOldStr);
						if (subEle.getBudget_old() != null) {
							DecimalFormatSymbols dfs = new DecimalFormatSymbols();
							dfs.setGroupingSeparator(',');
							dfs.setDecimalSeparator('.');
							// controllo formattazione con virgola separatore
							// dei decimali
							try {
								DecimalFormat df = new DecimalFormat(
										"#,##0.00", dfs);
								// importo
								String stringa = df.format(subEle
										.getBudget_old());
								bdgOldStr = stringa;
							} catch (Exception e) {
								log.error("", e);
							}
						}
						if (bdgOldStr != null && !bdgOldStr.equals("0.00")) {
							rigaEsameStoria.setCodice3(bdgOldStr);
						}

						righeEsameStoria.add(rigaEsameStoria);

					}
					if (righeEsameStoria != null && righeEsameStoria.size() > 0) // listaSezioni.size()==1
					{
						listaSezioni.get(0).setRigheEsameStoria(
								righeEsameStoria);
					}
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		this.ValidaRicercaSezioni(listaSezioni);
		return listaSezioni;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciSezione(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		return dao.inserisciSezione(sezione);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaSezione(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaSezione(sezione);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaSezione(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		return dao.cancellaSezione(sezione);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaBuoniOrd(
			ListaSuppBuoniOrdineVO ricercaBuoniOrd)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaBuoni = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaBuoniOrd(ricercaBuoniOrd);
		} else {
			try {
				Tba_buono_ordineDao buoniDao = new Tba_buono_ordineDao();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();

				ValidaListaSuppBuoniOrdineVO(ricercaBuoniOrd);
				results = buoniDao.getRicercaListaBuoniOrdHib(ricercaBuoniOrd);
				if (results != null && results.size() > 0) {
					if (results.size() > 1000) {
						throw new ValidationException(
								"ricercaDaRaffinareTroppi",
								ValidationExceptionCodici.ricercaDaRaffinareTroppi);
					}
					ConfigurazioneBOVO apConfBONew = new ConfigurazioneBOVO();
					apConfBONew.setCodPolo(ricercaBuoniOrd.getCodPolo());
					apConfBONew.setCodBibl(ricercaBuoniOrd.getCodBibl());
					String tipoRinnovoConfigurato = "";
					try {
						apConfBONew = ordiniDao
								.loadConfigurazioneHib(apConfBONew);
						tipoRinnovoConfigurato = apConfBONew.getTipoRinnovo();
					} catch (Exception e) {
						log.error("", e);
					}
					ConfigurazioneORDVO apConfORDNew = new ConfigurazioneORDVO();
					apConfORDNew.setCodPolo(ricercaBuoniOrd.getCodPolo());
					apConfORDNew.setCodBibl(ricercaBuoniOrd.getCodBibl());
					Boolean gestBil = true;
					try {
						apConfORDNew = ordiniDao
								.loadConfigurazioneOrdiniHib(apConfORDNew);
						if (apConfORDNew != null
								&& !apConfORDNew.isGestioneBilancio()) {
							gestBil = false;
						}
					} catch (Exception e) {
						log.error("", e);
					}
					int numRighe = 0;
					int progrRighe = 1;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						BuoniOrdineVO rec = new BuoniOrdineVO();
						Tba_buono_ordine oggBUO = (Tba_buono_ordine) results
								.get(i);

						// inizio
						rec.setGestBil(true);

						if (!gestBil) {
							rec.setGestBil(false);
						}

						rec.setProgressivo(progrRighe);
						rec.setIDBuonoOrd(oggBUO.getId_buono_ordine());
						if (oggBUO.getCod_mat() != null
								&& oggBUO.getCod_mat().getId_capitoli_bilanci() != null) {
							rec.setIDBil(oggBUO.getCod_mat()
									.getId_capitoli_bilanci()
									.getId_capitoli_bilanci());
						}
						Tbf_biblioteca_in_polo appoBib = oggBUO.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());

						rec.setNumBuonoOrdine(oggBUO.getBuono_ord());

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggBUO
									.getData_buono());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataBuonoOrdine(dataFormattata);

						rec.setStatoBuonoOrdine(String.valueOf(oggBUO
								.getStato_buono()));
						rec.setBilancio(new StrutturaTerna("", "", ""));

						if (oggBUO.getCod_mat() != null
								&& oggBUO.getCod_mat().getId_capitoli_bilanci() != null) {
							rec.getBilancio().setCodice1(
									String.valueOf(oggBUO.getCod_mat()
											.getId_capitoli_bilanci()
											.getEsercizio()));
							rec.getBilancio().setCodice2(
									String.valueOf(oggBUO.getCod_mat()
											.getId_capitoli_bilanci()
											.getCapitolo()));
							rec.getBilancio().setCodice3(
									String.valueOf(oggBUO.getCod_mat()
											.getCod_mat()));
						}
						rec.setFornitore(new StrutturaCombo("", ""));
						rec.getFornitore().setCodice(
								String.valueOf(oggBUO.getCod_fornitore()));
						rec.getFornitore().setDescrizione(
								oggBUO.getCod_fornitore().getNom_fornitore());
						rec.setDataUpd(oggBUO.getTs_var());

						// dati biblioteca per stampe
						rec.setDenoBibl(appoBib.getDs_biblioteca());

						// dati fornitore per stampe
						rec.setAnagFornitore(new FornitoreVO());
						rec.getAnagFornitore().setCodFornitore(
								String.valueOf(oggBUO.getCod_fornitore()));
						rec.getAnagFornitore().setNomeFornitore(
								oggBUO.getCod_fornitore().getNom_fornitore());

						if (oggBUO.getCod_fornitore().getIndirizzo() != null
								&& oggBUO.getCod_fornitore().getIndirizzo()
										.trim().length() > 0) {
							rec.getAnagFornitore().setIndirizzo(
									oggBUO.getCod_fornitore().getIndirizzo());
						}
						if (oggBUO.getCod_fornitore().getCitta() != null
								&& oggBUO.getCod_fornitore().getCitta().trim()
										.length() > 0) {
							rec.getAnagFornitore().setCitta(
									oggBUO.getCod_fornitore().getCitta());
						}
						if (oggBUO.getCod_fornitore().getCap() != null
								&& oggBUO.getCod_fornitore().getCap().trim()
										.length() > 0) {
							rec.getAnagFornitore().setCap(
									oggBUO.getCod_fornitore().getCap());
						}
						if (oggBUO.getCod_fornitore().getPaese() != null
								&& oggBUO.getCod_fornitore().getPaese().trim()
										.length() > 0) {
							rec.getAnagFornitore().setPaese(
									oggBUO.getCod_fornitore().getPaese());
						}
						if (oggBUO.getCod_fornitore().getCod_fiscale() != null
								&& oggBUO.getCod_fornitore().getCod_fiscale()
										.trim().length() > 0) {
							rec.getAnagFornitore().setCodiceFiscale(
									oggBUO.getCod_fornitore().getCod_fiscale());
						}
						if (oggBUO.getCod_fornitore().getFax() != null
								&& oggBUO.getCod_fornitore().getFax().trim()
										.length() > 0) {
							rec.getAnagFornitore().setFax(
									oggBUO.getCod_fornitore().getFax());
						}
						if (oggBUO.getCod_fornitore().getTelefono() != null
								&& oggBUO.getCod_fornitore().getTelefono()
										.trim().length() > 0) {
							rec.getAnagFornitore().setTelefono(
									oggBUO.getCod_fornitore().getTelefono());
						}

						rec.getAnagFornitore().setFornitoreBibl(
								new DatiFornitoreVO());

						// recupero le righe di dettaglio del buono
						if (oggBUO.getTra_elementi_buono_ordine() != null) {
							try {
								Object[] elencoOrdBuo = oggBUO
										.getTra_elementi_buono_ordine()
										.toArray();
								List<OrdiniVO> arrayBuoDett = new ArrayList<OrdiniVO>();
								double impParziale = 0;
								for (int j = 0; j < elencoOrdBuo.length; j++) {
									Tra_elementi_buono_ordine singleOgg = (Tra_elementi_buono_ordine) elencoOrdBuo[j];
									// ricerca ordine
									ListaSuppOrdiniVO ricercaOrdineAss = new ListaSuppOrdiniVO();
									ricercaOrdineAss
											.setTipoOrdine(String
													.valueOf(singleOgg
															.getCod_tip_ord()));
									ricercaOrdineAss.setAnnoOrdine(String
											.valueOf(singleOgg.getAnno_ord()));
									ricercaOrdineAss.setCodOrdine(String
											.valueOf(singleOgg.getCod_ord()));
									ricercaOrdineAss.setCodPolo(rec
											.getCodPolo());
									ricercaOrdineAss.setCodBibl(rec
											.getCodBibl());

									ricercaOrdineAss.setFlag_canc(false);
									List resultOrdineAss = null;
									try {
										resultOrdineAss = this
												.getRicercaListaOrdini(ricercaOrdineAss);
									} catch (Exception e) {
										// log.error("", e);
									}

									if (resultOrdineAss != null
											&& resultOrdineAss.size() == 1) {
										OrdiniVO eleBuoDett = (OrdiniVO) resultOrdineAss
												.get(0);
										// OrdiniVO newEleBuoDett = new
										// OrdiniVO();
										arrayBuoDett.add(eleBuoDett);
										// fare la somma dei prezzi per desumere
										// l'importo del buono
										impParziale = impParziale
												+ eleBuoDett
														.getPrezzoEuroOrdine();
									}
								}
								rec.setImporto(impParziale);
								rec.setListaOrdiniBuono(arrayBuoDett);

							} catch (Exception e) {
								log.error("", e);
							}
						}
						// fine
						// se legami con ordini inesistenti per le condizioni
						// immesse il buono non deve essere aggiunto
						if (rec.getListaOrdiniBuono() != null
								&& rec.getListaOrdiniBuono().size() > 0) {
							listaBuoni.add(rec);
							progrRighe++;
						}
					}
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaBuoniOrd(listaBuoni);
		return listaBuoni;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciBuonoOrd(BuoniOrdineVO buonoOrd)
			throws DataException, ApplicationException, ValidationException {
		return dao.inserisciBuonoOrd(buonoOrd);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaBuonoOrd(BuoniOrdineVO buonoOrd)
			throws DataException, ApplicationException, ValidationException {
		return dao.modificaBuonoOrd(buonoOrd);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaBuonoOrd(BuoniOrdineVO buonoOrd)
			throws DataException, ApplicationException, ValidationException {
		return dao.cancellaBuonoOrd(buonoOrd);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaFattura(FatturaVO fattura) throws DataException,
			ApplicationException, ValidationException {
		return dao.cancellaFattura(fattura);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaBilancio(BilancioVO bilancio) throws DataException,
			ApplicationException, ValidationException {
		return dao.cancellaBilancio(bilancio);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaComunicazione(ComunicazioneVO com)
			throws DataException, ApplicationException, ValidationException {
		boolean ok = dao.cancellaComunicazione(com);
		if (ok) { // periodici
			try {
				getPeriodici().cancellaMessaggiFascicolo(com);
			} catch (Exception e) {
				return false;
			}

		}
		return ok;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaBuoniOrd(List listaBuoniOrd)
			throws ValidationException {
		Validazione.ValidaRicercaBuoniOrd(listaBuoniOrd);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppBuoniOrdineVO(ListaSuppBuoniOrdineVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppBuoniOrdineVO(dao, oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaBuoniOrdineVO(BuoniOrdineVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaBuoniOrdineVO(dao, oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaSezioni(List listaSezioni)
			throws ValidationException {
		Validazione.ValidaRicercaSezioni(listaSezioni);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppSezioneVO(ListaSuppSezioneVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppSezioneVO(oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaSezioneVO(SezioneVO oggettoVO) throws ValidationException {
		Validazione.ValidaSezioneVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaProfili(List listaProfili)
			throws ValidationException {
		Validazione.ValidaRicercaProfili(listaProfili);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaComunicazioni(List listaComunicazioni)
			throws ValidationException {
		Validazione.ValidaRicercaComunicazioni(listaComunicazioni);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaFatture(List listaFatture)
			throws ValidationException {
		Validazione.ValidaRicercaFatture(listaFatture);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaBilanci(List listaBilanci)
			throws ValidationException {
		Validazione.ValidaRicercaBilanci(listaBilanci);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppProfiloVO(ListaSuppProfiloVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppProfiloVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppComunicazioneVO(
			ListaSuppComunicazioneVO oggettoLista) throws ValidationException {
		Validazione.ValidaListaSuppComunicazioneVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppFatturaVO(ListaSuppFatturaVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppFatturaVO(oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public void ValidaListaSuppBilancioVO(ListaSuppBilancioVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppBilancioVO(oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaBilancioVO(BilancioVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaBilancioVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaComunicazioneVO(ComunicazioneVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaComunicazioneVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaStrutturaProfiloVO(StrutturaProfiloVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaStrutturaProfiloVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaFatturaVO(FatturaVO oggettoVO) throws ValidationException {
		Validazione.ValidaFatturaVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public void ValidaRicercaFornitori(List listaFornitori)
			throws ValidationException {
		Validazione.ValidaRicercaFornitori(listaFornitori);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppOrdiniVO(ListaSuppOrdiniVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppOrdiniVO(oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public void ValidaRicercaOrdini(List listaOrdini)
			throws ValidationException {
		Validazione.ValidaRicercaOrdini(listaOrdini);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaOrdiniVO(OrdiniVO oggettoVO) throws ValidationException {
		Validazione.ValidaOrdiniVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public void ValidaFornitoreVO(FornitoreVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaFornitoreVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppFornitoreVO(ListaSuppFornitoreVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppFornitoreVO(oggettoLista);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public String struttura(String sqlString) {
		return dao.struttura(sqlString);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean strIsAlfabetic(String data) {
		return dao.strIsAlfabetic(data);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean strIsNumeric(String data) {
		return dao.strIsNumeric(data);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public int validaDataPassata(String data) {
		return Validazione.validaDataPassata(data);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getTitolo(List listaBid, String ticket)
			throws ValidationException, DataException, ApplicationException {
		return dao.getTitolo(listaBid, ticket);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getTitoloOrdine(String bidPassato) throws DataException,
			ApplicationException, DaoManagerException {
		Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
		return cambiDao.getTitoloOrdine(bidPassato);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public TitoloACQVO getTitoloOrdineBis(String bidPassato)
			throws DataException, ApplicationException, DaoManagerException {
		Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
		return cambiDao.getTitoloOrdineBis(bidPassato);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public TitoloACQAreeIsbdVO getAreeIsbdTitolo(String bidPassato)
			throws DataException, ApplicationException, DaoManagerException {
		return dao.getAreeIsbdTitolo(bidPassato);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws DaoManagerException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public TitoloACQVO getTitoloRox(String bidPassato) throws DataException,
			ApplicationException, DaoManagerException, ValidationException {
		return dao.getTitoloRox(bidPassato);
	}



	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaSuggerimenti(
			ListaSuppSuggerimentoVO ricercaSuggerimenti)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaSuggBibl = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaSuggerimenti(ricercaSuggerimenti);
		} else {
			try {
				Tba_suggerimenti_bibliograficiDao suggBiblDao = new Tba_suggerimenti_bibliograficiDao();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				ValidaListaSuppSuggerimentoVO(ricercaSuggerimenti);
				results = suggBiblDao
						.getRicercaListaSuggerimentiHib(ricercaSuggerimenti);
				if (results != null && results.size() > 0) {
					if (results.size() > 1000) {
						throw new ValidationException(
								"ricercaDaRaffinareTroppi",
								ValidationExceptionCodici.ricercaDaRaffinareTroppi);
					}
					int numRighe = 0;
					int progrRighe = 1;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						SuggerimentoVO rec = new SuggerimentoVO();
						Tba_suggerimenti_bibliografici oggSUGGBIBL = (Tba_suggerimenti_bibliografici) results
								.get(i);
						if (oggSUGGBIBL.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						Tbf_biblioteca_in_polo appoBib = oggSUGGBIBL
								.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setProgressivo(Integer.valueOf(numRighe));

						rec.setCodiceSuggerimento(oggSUGGBIBL
								.getCod_sugg_bibl().toString());

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggSUGGBIBL
									.getData_sugg_bibl());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataSuggerimento(dataFormattata);

						rec.setNoteSuggerimento(oggSUGGBIBL.getNote());
						rec.setNoteBibliotecario(oggSUGGBIBL.getMsg_per_bibl());
						rec.setNoteFornitore(oggSUGGBIBL.getNote_forn());
						rec.setStatoSuggerimento(String.valueOf(oggSUGGBIBL
								.getStato_sugg()));

						// ricerca su tb_codici

						rec.setDenoStatoSuggerimento("");
						try {
							rec.setDenoStatoSuggerimento(ordiniDao
									.getCodiciDaTbCodici("ASTS", String
											.valueOf(oggSUGGBIBL
													.getStato_sugg())));
						} catch (Exception e) {
							// log.error("", e);
						}

						rec.setBibliotecario(new StrutturaCombo("", ""));

						if (oggSUGGBIBL.getCod_bibliotecario() != null
								&& oggSUGGBIBL.getCod_bibliotecario()
										.getTbf_utenti_professionali_web() != null
								&& oggSUGGBIBL.getCod_bibliotecario()
										.getTbf_utenti_professionali_web()
										.getUserid() != null) {
							rec.getBibliotecario().setCodice(
									oggSUGGBIBL.getCod_bibliotecario()
											.getTbf_utenti_professionali_web()
											.getUserid());
						}
						if (!rec.getBibliotecario().getCodice().equals("0")) {
							if (oggSUGGBIBL.getCod_bibliotecario() != null
									&& oggSUGGBIBL.getCod_bibliotecario()
											.getCognome() != null
									&& oggSUGGBIBL.getCod_bibliotecario()
											.getCognome().trim().length() > 0) {
								rec.setNominativoBibliotecario(oggSUGGBIBL
										.getCod_bibliotecario().getCognome()
										.trim());
								if (oggSUGGBIBL.getCod_bibliotecario() != null
										&& oggSUGGBIBL.getCod_bibliotecario()
												.getNome() != null
										&& oggSUGGBIBL.getCod_bibliotecario()
												.getNome().trim().length() > 0) {
									String appo = rec
											.getNominativoBibliotecario();
									rec.setNominativoBibliotecario(appo
											+ " - "
											+ oggSUGGBIBL
													.getCod_bibliotecario()
													.getNome().trim());
								}
							}
						}

						// temporaneamente
						if (oggSUGGBIBL.getCod_bibliotecario() != null
								&& oggSUGGBIBL.getCod_bibliotecario()
										.getTbf_utenti_professionali_web() != null
								&& oggSUGGBIBL.getCod_bibliotecario()
										.getTbf_utenti_professionali_web()
										.getUserid() != null) {
							rec.getBibliotecario().setDescrizione(
									oggSUGGBIBL.getCod_bibliotecario()
											.getTbf_utenti_professionali_web()
											.getUserid());
						}
						rec.setSezione(new StrutturaCombo("", ""));
						if (oggSUGGBIBL.getId_sez_acquis_bibliografiche() != null
								&& oggSUGGBIBL
										.getId_sez_acquis_bibliografiche()
										.getCod_sezione() != null) {
							rec.getSezione().setCodice(
									oggSUGGBIBL
											.getId_sez_acquis_bibliografiche()
											.getCod_sezione());
						}
						if (oggSUGGBIBL.getId_sez_acquis_bibliografiche() != null) {
							rec.setIDSez(oggSUGGBIBL
									.getId_sez_acquis_bibliografiche()
									.getId_sez_acquis_bibliografiche());
						}
						if (oggSUGGBIBL.getId_sez_acquis_bibliografiche() != null
								&& oggSUGGBIBL
										.getId_sez_acquis_bibliografiche()
										.getNome() != null) {
							rec.getSezione().setDescrizione(
									ValidazioneDati.trimOrEmpty(oggSUGGBIBL
											.getId_sez_acquis_bibliografiche()
											.getNome()));
						}
						rec.setDataUpd(oggSUGGBIBL.getTs_var());

						String isbd = "";
						String bid = "";
						String naturaBid = "";
						if (oggSUGGBIBL.getBid() != null
								&& oggSUGGBIBL.getBid().trim().length() != 0) {
							try {
								bid = oggSUGGBIBL.getBid();

								TitoloACQVO recTit = null;
								recTit = dao.getTitoloOrdineTer(oggSUGGBIBL
										.getBid().trim());
								if (recTit != null && recTit.getIsbd() != null) {
									bid = oggSUGGBIBL.getBid().trim();
									isbd = recTit.getIsbd();
									naturaBid = recTit.getNatura();
								}
								if (recTit == null) {
									isbd = "titolo non trovato";
									bid = oggSUGGBIBL.getBid();
								}

							} catch (Exception e) {
								isbd = "titolo non trovato";
								bid = oggSUGGBIBL.getBid();
							}
						}
						rec.setTitolo(new StrutturaCombo(bid, isbd));
						rec.setNaturaBid(naturaBid);

						listaSuggBibl.add(rec);
					} // fine for
				} // fine if
			} // fine try
			catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaSuggerimenti(listaSuggBibl);
		return listaSuggBibl;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppSuggerimentoVO(
			ListaSuppSuggerimentoVO oggettoLista) throws ValidationException {
		Validazione.ValidaListaSuppSuggerimentoVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaSuggerimentoVO(SuggerimentoVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaSuggerimentoVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaSuggerimenti(List listaSuggerimenti)
			throws ValidationException {
		Validazione.ValidaRicercaSuggerimenti(listaSuggerimenti);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciSuggerimento(SuggerimentoVO suggerimento)
			throws DataException, ApplicationException, ValidationException {
		return dao.inserisciSuggerimento(suggerimento);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaSuggerimento(SuggerimentoVO suggerimento)
			throws DataException, ApplicationException, ValidationException {
		return dao.modificaSuggerimento(suggerimento);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaSuggerimento(SuggerimentoVO suggerimento)
			throws DataException, ApplicationException, ValidationException {
		return dao.cancellaSuggerimento(suggerimento);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaConfigurazione(ConfigurazioneBOVO configurazione)
			throws DataException, ApplicationException, ValidationException {
		return dao.modificaConfigurazione(configurazione);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public ConfigurazioneBOVO loadConfigurazione(
			ConfigurazioneBOVO configurazione)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {

		Boolean testxHib = false;
		// testxHib=true;
		if (!testxHib) {
			return dao.loadConfigurazione(configurazione);
		} else {
			ConfigurazioneBOVO result = new ConfigurazioneBOVO();
			try {
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				result = ordiniDao.loadConfigurazioneHib(configurazione);
			} catch (ValidationException e) {
				// throw new ValidationException(e.getMessage(),
				// ValidationException.errore);
				throw e;

			} catch (Exception e) {

				log.error("", e);

			}
			return result;
		}

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public ConfigurazioneORDVO loadConfigurazioneOrdini(
			ConfigurazioneORDVO configurazioneORD)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {

		Boolean testxHib = false;
		// testxHib=true;
		if (!testxHib) {
			return dao.loadConfigurazioneOrdini(configurazioneORD);
		} else {
			ConfigurazioneORDVO result = null;
			try {
				result = new ConfigurazioneORDVO();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				result = ordiniDao
						.loadConfigurazioneOrdiniHib(configurazioneORD);
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
			return result;
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaConfigurazioneOrdini(
			ConfigurazioneORDVO configurazioneORD) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaConfigurazioneOrdini(configurazioneORD);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaDocumenti(
			ListaSuppDocumentoVO ricercaDocumenti)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaSuggLett = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaDocumenti(ricercaDocumenti);
		} else {
			try {
				Tbl_documenti_lettoriAcqDao suggLettDao = new Tbl_documenti_lettoriAcqDao();
				Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
				ValidaListaSuppDocumentoVO(ricercaDocumenti);
				results = suggLettDao
						.getRicercaListaDocumentiHib(ricercaDocumenti);
				if (results != null && results.size() > 0) {
					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						DocumentoVO rec = new DocumentoVO();
						Tbl_documenti_lettori oggSUGGLETT = (Tbl_documenti_lettori) results
								.get(i);
						if (oggSUGGLETT.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						Tbf_biblioteca_in_polo appoBib = oggSUGGLETT
								.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setProgressivo(Integer.valueOf(numRighe));

						rec.setIDDoc(oggSUGGLETT.getId_documenti_lettore());
						// rec.setUtenteCod(ricercaDocumenti.getUtente());

						rec.setCodDocumento(String.valueOf(oggSUGGLETT
								.getCod_doc_lett()));

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggSUGGLETT
									.getTs_var());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataAgg(dataFormattata);

						dataFormattata = "";
						try {
							dataFormattata = format1.format(oggSUGGLETT
									.getTs_ins());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataIns(dataFormattata);

						rec.setStatoSuggerimentoDocumento(String
								.valueOf(oggSUGGLETT.getStato_sugg()));

						// ricerca su tb_codici

						rec.setDenoStatoSuggerimento("");
						try {
							rec.setDenoStatoSuggerimento(ordiniDao
									.getCodiciDaTbCodici("ASTS", String
											.valueOf(oggSUGGLETT
													.getStato_sugg())));
						} catch (Exception e) {
							// log.error("", e);
						}

						rec.setUtente(new StrutturaTerna("", "", ""));
						if (oggSUGGLETT.getId_utenti() != null) {
							rec.getUtente().setCodice1(
									String.valueOf(oggSUGGLETT.getId_utenti()
											.getId_utenti()));
							rec.getUtente().setCodice2(
									oggSUGGLETT.getId_utenti().getCod_utente());
							rec.getUtente().setCodice3(
									oggSUGGLETT.getId_utenti().getCognome()
											.trim()
											+ "-"
											+ oggSUGGLETT.getId_utenti()
													.getNome().trim());
						}

						rec.setTitolo(new StrutturaCombo("", ""));
						rec.getTitolo().setCodice(oggSUGGLETT.getBid());
						rec.getTitolo().setDescrizione(oggSUGGLETT.getTitolo());

						rec.setPrimoAutore(oggSUGGLETT.getAutore());
						rec.setEditore(oggSUGGLETT.getEditore());
						rec.setLuogoEdizione(oggSUGGLETT.getLuogo_edizione());

						rec.setPaese(new StrutturaCombo("", ""));
						rec.getPaese().setCodice(oggSUGGLETT.getPaese());

						rec.setLingua(new StrutturaCombo("", ""));
						rec.getLingua().setCodice(oggSUGGLETT.getLingua());

						rec.setLuogoEdizione(oggSUGGLETT.getLuogo_edizione());
						if (oggSUGGLETT.getAnno_edizione() != null) {
							rec.setAnnoEdizione(String.valueOf(oggSUGGLETT
									.getAnno_edizione().intValue()));
						}
						rec.setNoteDocumento(oggSUGGLETT.getNote());
						rec.setMsgPerLettore(oggSUGGLETT.getMsg_lettore());

						rec.setTipoVariazione("");

						String isbd = "";
						String bid = "";
						String naturaBid = "";
						isbd = oggSUGGLETT.getTitolo();

						if (oggSUGGLETT.getBid() != null
								&& oggSUGGLETT.getBid().trim().length() != 0) {
							bid = oggSUGGLETT.getBid();

							try {
								TitoloACQVO recTit = null;
								recTit = dao.getTitoloOrdineTer(oggSUGGLETT
										.getBid().trim());
								if (recTit != null && recTit.getIsbd() != null) {
									bid = oggSUGGLETT.getBid().trim();
									isbd = recTit.getIsbd();
									naturaBid = recTit.getNatura();
								}
								if (recTit == null) {
									isbd = isbd + " (ERRORE: titolo-bid)";
									bid = oggSUGGLETT.getBid();
									naturaBid = "";
								}
							} catch (Exception e) {
								isbd = isbd + " (ERRORE: titolo-bid)";
								bid = oggSUGGLETT.getBid();
								naturaBid = "";
							}
						}

						rec.setTitolo(new StrutturaCombo(bid, isbd));
						rec.setNaturaBid(naturaBid);
						listaSuggLett.add(rec);
					} // fine for
				} // fine if
			} // fine try
			catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaDocumenti(listaSuggLett);
		return listaSuggLett;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppDocumentoVO(ListaSuppDocumentoVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppDocumentoVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaDocumentoVO(DocumentoVO oggettoVO)
			throws ValidationException {
		Validazione.ValidaDocumentoVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaDocumenti(List listaDocumenti)
			throws ValidationException {
		Validazione.ValidaRicercaDocumenti(listaDocumenti);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaDocumento(DocumentoVO documento)
			throws DataException, ApplicationException, ValidationException {
		return dao.modificaDocumento(documento);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaGare(ListaSuppGaraVO ricercaGare)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		Boolean testxHib = false;
		// testxHib=true;
		List listaGare = new ArrayList();
		List results = null;

		if (!testxHib) {
			return dao.getRicercaListaGare(ricercaGare);
		} else {
			try {
				Tba_richieste_offertaDao gareDao = new Tba_richieste_offertaDao();
				ValidaListaSuppGaraVO(ricercaGare);
				results = gareDao.getRicercaListaGareHib(ricercaGare);
				if (results != null && results.size() > 0) {
					int numRighe = 0;
					for (int i = 0; i < results.size(); i++) {
						numRighe++;
						GaraVO rec = new GaraVO();
						Tba_richieste_offerta oggGARA = (Tba_richieste_offerta) results
								.get(i);
						if (oggGARA.getFl_canc() != 'S')
							;
						rec.setFlag_canc(true);
						Tbf_biblioteca_in_polo appoBib = oggGARA.getCd_bib();
						rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
						rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setProgressivo(numRighe);
						rec.setCodRicOfferta(String.valueOf(oggGARA
								.getCod_rich_off()));

						SimpleDateFormat format1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						// format1.format(oggORD.getData_ord());
						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggGARA
									.getData_rich_off());
						} catch (Exception e) {
							// log.error("", e);
						}
						rec.setDataRicOfferta(dataFormattata);

						// rec.setPrezzoIndGara(String.valueOf(rs.getFloat("prezzo_rich")));
						rec.setPrezzoIndGara(oggGARA.getPrezzo_rich()
								.doubleValue());
						rec.setNumCopieRicAcq(oggGARA.getNum_copie());
						rec.setNoteOrdine(oggGARA.getNote());
						rec.setStatoRicOfferta(String.valueOf(oggGARA
								.getStato_rich_off()));
						rec.setDataUpd(oggGARA.getTs_var());

						/*
						 * if (rec.getStatoRicOfferta()==null ||
						 * (ricercaGare.getStatoRicOfferta()!=null &&
						 * ricercaGare.getStatoRicOfferta().equal(""))) {
						 * rec.setDesStatoRicOfferta(""); } else
						 */
						if (rec.getStatoRicOfferta().equals("1")) {
							rec.setDesStatoRicOfferta("Aperta");
						} else if (rec.getStatoRicOfferta().equals("2")) {
							rec.setDesStatoRicOfferta("Chiusa");
						} else if (rec.getStatoRicOfferta().equals("3")) {
							rec.setDesStatoRicOfferta("Annullata");
						} else if (rec.getStatoRicOfferta().equals("4")) {
							rec.setDesStatoRicOfferta("Ordinata");
						}

						String naturaBid = "";
						String isbd = "";
						String bid = "";

						if (oggGARA.getBid().getBid() != null
								&& oggGARA.getBid() != null
								&& oggGARA.getBid().getBid().trim().length() != 0) {
							bid = oggGARA.getBid().getBid();

							try {
								TitoloACQVO recTit = null;
								recTit = dao.getTitoloOrdineTer(oggGARA
										.getBid().getBid().trim());
								if (recTit != null && recTit.getIsbd() != null) {
									bid = oggGARA.getBid().getBid().trim();
									isbd = recTit.getIsbd();
									naturaBid = recTit.getNatura();
								}
								if (recTit == null) {
									isbd = "titolo non trovato";
									bid = oggGARA.getBid().getBid();
									naturaBid = "natura non trovata";
								}
							} catch (Exception e) {
								isbd = "titolo non trovato";
								bid = oggGARA.getBid().getBid();
								naturaBid = "natura non trovata";
							}
						}
						rec.setBid(new StrutturaCombo(bid, isbd));
						rec.setNaturaBid(naturaBid);

						// if rs.getString("lingua")!=null ciclo per riempire
						// l'arraylist

						// ricerca dei partecipanti associati alla gara se
						// esistenti

						if (oggGARA.getTra_fornitori_offerte() != null) {
							try {
								Object elencoGaraDett[] = oggGARA
										.getTra_fornitori_offerte().toArray();
								List arrayGaraDett = new ArrayList();
								for (int j = 0; j < elencoGaraDett.length; j++) {
									Tra_fornitori_offerte eleGaraDett = (Tra_fornitori_offerte) elencoGaraDett[j];
									PartecipantiGaraVO newEleGaraDett = new PartecipantiGaraVO();

									newEleGaraDett.setCodBibl(rec.getCodBibl());
									newEleGaraDett.setCodPolo(rec.getCodPolo());
									newEleGaraDett.setCodRicOfferta(String
											.valueOf(eleGaraDett
													.getCod_rich_off()));
									newEleGaraDett.setCodtipoInvio(String
											.valueOf(eleGaraDett
													.getTipo_invio()));

									dataFormattata = "";
									try {
										dataFormattata = format1
												.format(eleGaraDett
														.getData_invio());
									} catch (Exception e) {
										// log.error("", e);
									}
									newEleGaraDett
											.setDataInvioAlFornRicOfferta(dataFormattata);

									newEleGaraDett
											.setFornitore(new StrutturaCombo(
													"", ""));
									if (eleGaraDett.getCod_fornitore() != null) {
										newEleGaraDett
												.getFornitore()
												.setCodice(
														String
																.valueOf(eleGaraDett
																		.getCod_fornitore()
																		.getCod_fornitore()));
										newEleGaraDett
												.getFornitore()
												.setDescrizione(
														eleGaraDett
																.getCod_fornitore()
																.getNom_fornitore());
									}

									newEleGaraDett
											.setMsgRispDaFornAGara(eleGaraDett
													.getRisp_da_risp());
									newEleGaraDett
											.setNoteAlFornitore(eleGaraDett
													.getNote());
									newEleGaraDett.setStatoPartecipante(String
											.valueOf(eleGaraDett
													.getStato_gara()));

									arrayGaraDett.add(newEleGaraDett);
								}

								rec.setDettaglioPartecipantiGara(arrayGaraDett);
							} catch (Exception e) {
								log.error("", e);
							}
						}
						listaGare.add(rec);
					}
				}
			} catch (ValidationException e) {
				throw e;
			} catch (Exception e) {
				log.error("", e);
			}
		}
		ValidaRicercaGare(listaGare);
		return listaGare;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppGaraVO(ListaSuppGaraVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppGaraVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaGaraVO(GaraVO oggettoVO) throws ValidationException {
		Validazione.ValidaGaraVO(oggettoVO);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaGare(List listaGare)
			throws ValidationException {
		Validazione.ValidaRicercaGare(listaGare);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaGara(GaraVO gara) throws DataException,
			ApplicationException, ValidationException {
		return dao.modificaGara(gara);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean gestioneStampaOrdini(List listaOggetti,
			List idList, String tipoOggetti, String utente, String bo)
			throws DataException, ApplicationException, ValidationException {
		return dao.gestioneStampaOrdini(listaOggetti, idList, tipoOggetti,
				utente, bo);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List gestioneStampato(List listaOggetti,
			String tipoOggetti, String bo) throws DataException,
			ApplicationException, ValidationException {
		return dao.gestioneStampato(listaOggetti, tipoOggetti, bo);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List costruisciCatenaRinnovati(StrutturaTerna ordRinn,
			String codBibl) throws DataException, ApplicationException,
			ValidationException {
		List<ChiaveOrdine> rinnovi = dao.costruisciCatenaRinnovati(ordRinn, codBibl);
		List<StrutturaQuinquies> listaOrdiniCatena = new ArrayList<StrutturaQuinquies>();
		for (ChiaveOrdine r : rinnovi) {
			StrutturaQuinquies eleLista = new StrutturaQuinquies();
			eleLista.setCodice1(r.getTipo());
			eleLista.setCodice2(r.getAnno());
			eleLista.setCodice3(r.getCod());
			listaOrdiniCatena.add(eleLista);
		}
		return listaOrdiniCatena;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciGara(GaraVO gara) throws DataException,
			ApplicationException, ValidationException {
		return dao.inserisciGara(gara);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaGara(GaraVO gara) throws DataException,
			ApplicationException, ValidationException {
		return dao.cancellaGara(gara);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaFornitore(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		return dao.cancellaFornitore(fornitore);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaOfferte(
			ListaSuppOffertaFornitoreVO ricercaOfferte)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return dao.getRicercaListaOfferte(ricercaOfferte);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaListaSuppOffertaFornitoreVO(
			ListaSuppOffertaFornitoreVO oggettoLista)
			throws ValidationException {
		Validazione.ValidaListaSuppOffertaFornitoreVO(oggettoLista);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void ValidaRicercaOfferte(List listaOfferte)
			throws ValidationException {
		Validazione.ValidaRicercaOfferte(listaOfferte);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 *
	 */
	public List getRicercaListaCambiHib(ListaSuppCambioVO ricercaCambi)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		List results = null;
		List listaCambi = null;
		Tba_cambi_ufficiali cu = null;
		Object[] arrivo = null;
		CambioVO rec = null;

		try {
			Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
			this.ValidaListaSuppCambioVO(ricercaCambi);
			results = cambiDao.getRicercaListaCambiHib(ricercaCambi);
			// ciclo di riempimento
			if (results != null && results.size() > 0) {
				for (int index = 0; index < results.size(); index++) {
					// Criteria cr1 =
					// session.createCriteria(CodiciType.TAB_CODICE_VALUTA.class);
					// cr1.add(Expression.eq("TP_TABELLA",
					// results.get(index).getCD_TABELLA().trim()));
					// cr1.list();
					// cu = (Tba_cambi_ufficiali) results.get(index);
					arrivo = (Object[]) results.get(index);
					cu = (Tba_cambi_ufficiali) arrivo[0];

					rec = new CambioVO();

					// carica il resultset
					rec.setDesValuta((String) arrivo[1]);

					rec.setIDCambio(cu.getId_valuta());
					rec.setCodValuta((cu.getValuta().trim()
							.toUpperCase()));

					Tbf_polo polo = new Tbf_polo();
					polo.setCd_polo(ricercaCambi.getCodPolo());
					Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
					bib.setCd_biblioteca(ricercaCambi.getCodBibl());
					bib.setCd_polo(polo);
					rec.setCodPolo(polo.getCd_polo());
					rec.setCodBibl(bib.getCd_biblioteca());

					BigDecimal bd = cu.getCambio();
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					rec.setTassoCambio(bd.doubleValue());
					rec.setDataVariazione(String.valueOf(cu.getData_var()));

					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					String dataTest = formato.format(cu.getData_var());

					if (dataTest.equals("31/12/9999")) {
						rec.setValRifer(true);
					}
					rec.setProgressivo(index + 1);
					rec.setDataUpd(cu.getTs_var());


					if (listaCambi == null) {
						listaCambi = new ArrayList();
					}
					listaCambi.add(rec);

					}

			}

		} catch (ValidationException e) {
			// logger.error("Errore in getBibliteche",e);
			throw e;
			// throw new EJBException(e);
		} catch (RemoteException e) {
			// logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
			/*
			 * } catch (DaoManagerException e) { log.error("", e); throw e;
			 */
		} catch (Exception e) {

			log.error("", e);
		}
		this.ValidaRicerca(listaCambi);
		return listaCambi;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciCambioHib(CambioVO cambio) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
			// ret=cambiDao.inserisciCambioHib(cambio) ;
			this.ValidaCambioVO(cambio);
			if (cambio.getTassoCambio() == 0) {
				// tasso inserimento a zero
				throw new ValidationException("cambierroreInserimentoTasso",
						ValidationExceptionCodici.cambierroreInserimentoTasso);
			}
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			ListaSuppCambioVO listaSupp = this.trasformaCambioInlistaSupp(cambio);
			if (listaSupp != null) {
				List elenco = null;

				// *** controllo esistenza check di riferimento
				ListaSuppCambioVO listaSuppEuro = new ListaSuppCambioVO();
				listaSuppEuro.setEsisteRiferimento(true);
				listaSuppEuro.setCodBibl(cambio.getCodBibl());
				listaSuppEuro.setCodPolo(cambio.getCodPolo());
				List elencoRif = null;
				elencoRif = cambiDao.getRicercaListaCambiHib(listaSuppEuro);
				if (ValidazioneDati.isFilled(elencoRif)	&& cambio.isValRifer()) {
					throw new ValidationException(
							"cambierrorePresenzaValutaRiferimento",
							ValidationExceptionCodici.cambierrorePresenzaValutaRiferimento);
				}
				// *** fine controllo esistenza check di riferimento

				elenco = cambiDao.getRicercaListaCambiHib(listaSupp);
				if (elenco == null) {
					// controllo che non sia stato cancellato logicamente: in
					// tal caso
					// faccio update anche del flag e non insert
					listaSupp.setFlag_canc(true); // IMP
					List elencoCanc = null;
					elencoCanc = cambiDao.getRicercaListaCambiHib(listaSupp);
					if (elencoCanc != null && elencoCanc.size() == 1) {
						// update di tutti campi (esclusa la chiave) e anche del
						// flag
						Timestamp ts = new java.sql.Timestamp(System
								.currentTimeMillis());

						Object[] arrivo = null;
						arrivo = (Object[]) elencoCanc.get(0);
						Tba_cambi_ufficiali cu = (Tba_cambi_ufficiali) arrivo[0];
						// Tba_cambi_ufficiali cu = (Tba_cambi_ufficiali)
						// elencoCanc.get(0);

						cu.setFl_canc('N'); // IMP
						cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
						// DATA di sistema
						Date dataodierna = new Date();
						if (cambio.isValRifer()) {
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
							try {
								dataodierna = formato.parse("31/12/9999");
							} catch (ParseException e) {
								log.error("", e);
							}
						}
						cu.setData_var(dataodierna);
						cu.setUte_var(cambio.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
						cu.setTs_var(ts);
						cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
						ret = cambiDao.modificaCambioHib(cu);
					}
					if (elencoCanc == null) {
						Tba_cambi_ufficiali cu = new Tba_cambi_ufficiali();

						Tbf_polo polo = new Tbf_polo();
						// polo.setCd_polo(ricercaCambi.getCodPolo());
						polo.setCd_polo(cambio.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_biblioteca(cambio.getCodBibl());
						bib.setCd_polo(polo);
						// DATA di sistema
						Timestamp ts = DaoManager.now();
						cu.setCd_bib(bib);
						cu.setValuta(cambio.getCodValuta());
						cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
						Date dataodierna = new Date();
						if (cambio.isValRifer()) {
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
							try {
								dataodierna = formato.parse("31/12/9999");
							} catch (ParseException e) {
								log.error("", e);
							}
						}
						cu.setData_var(dataodierna);
						cu.setUte_ins(cambio.getUtente());
						cu.setTs_ins(ts);
						cu.setUte_var(cambio.getUtente());
						cu.setTs_var(ts);
						cu.setFl_canc('N');
						ret = cambiDao.inserisciCambioHib(cu);
					}
				} else {
					throw new ValidationException(
							"cambierroreInserimentoEsistenzaRecord",
							ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
				}

			}
		} catch (ValidationException e) {
			// logger.error("Errore in getBibliteche",e);
			throw e;
			// throw new EJBException(e);
		} catch (RemoteException e) {
			// logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
			/*
			 * } catch (DaoManagerException e) { log.error("", e); throw e;
			 */
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;
	}

	public ListaSuppCambioVO trasformaCambioInlistaSupp(CambioVO cambio)
			throws EJBException {
		ListaSuppCambioVO listaSupp = null;
		try {
			// sufficiente impostare la chiave
			listaSupp = new ListaSuppCambioVO();
			listaSupp.setCodBibl(cambio.getCodBibl());
			listaSupp.setCodPolo(cambio.getCodPolo());
			listaSupp.setCodValuta(cambio.getCodValuta());
		} catch (EJBException e) {

			log.error("", e);
		}
		return listaSupp;
	}

	public ListaSuppProfiloVO trasformaProfiloInlistaSupp(
			StrutturaProfiloVO profilo) throws EJBException {
		ListaSuppProfiloVO listaSupp = null;
		try {
			// sufficiente impostare la chiave
			listaSupp = new ListaSuppProfiloVO();
			listaSupp.setCodBibl(profilo.getCodBibl());
			listaSupp.setCodPolo(profilo.getCodPolo());
			if (profilo.getProfilo() != null) {
				listaSupp.setProfilo(profilo.getProfilo());
			}
			if (listaSupp.getProfilo() != null
					&& listaSupp.getProfilo().getDescrizione() != null
					&& listaSupp.getProfilo().getDescrizione().trim().length() > 0) {
				listaSupp.getProfilo().setDescrizione(
						listaSupp.getProfilo().getDescrizione().trim()
								.toUpperCase());
			}
			// solo se non è presente il codice profilo impongo le altre
			// condizioni
			if (profilo.getSezione() != null
					&& (listaSupp.getProfilo() == null || (listaSupp
							.getProfilo() != null
							&& listaSupp.getProfilo().getCodice() != null && listaSupp
							.getProfilo().getCodice().trim().length() == 0))) {
				listaSupp.setSezione(profilo.getSezione());
			}

		} catch (EJBException e) {

			log.error("", e);
		}
		return listaSupp;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaCambioHib(CambioVO cambio) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			Tba_cambi_ufficialiDao cambiDao = new Tba_cambi_ufficialiDao();
			// ret=cambiDao.modificaCambioHib(cambio) ;
			this.ValidaCambioVO(cambio);
			if (cambio.getTassoCambio() == 0) {
				// tasso inserimento a zero
				throw new ValidationException("cambierroreInserimentoTasso",
						ValidationExceptionCodici.cambierroreInserimentoTasso);
			}
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			if (this.trasformaCambioInlistaSupp(cambio) != null) {

				List elenco = null;
				elenco = cambiDao.getRicercaListaCambiHib(this
						.trasformaCambioInlistaSupp(cambio));
				if (elenco != null && elenco.size() == 1) {
					// *** controllo esistenza check di riferimento
					ListaSuppCambioVO listaSuppEuro = new ListaSuppCambioVO();
					listaSuppEuro.setEsisteRiferimento(true);
					listaSuppEuro.setCodBibl(cambio.getCodBibl());
					listaSuppEuro.setCodPolo(cambio.getCodPolo());
					List elencoRif = null;
					elencoRif = cambiDao.getRicercaListaCambiHib(listaSuppEuro);
					if (elencoRif != null && elencoRif.size() > 0
							&& cambio.isValRifer()) {
						throw new ValidationException(
								"cambierrorePresenzaValutaRiferimento",
								ValidationExceptionCodici.cambierrorePresenzaValutaRiferimento);
					}
					// *** fine controllo esistenza check di riferimento

					Timestamp ts = new java.sql.Timestamp(System
							.currentTimeMillis());

					Object[] arrivo = null;
					arrivo = (Object[]) elenco.get(0);
					Tba_cambi_ufficiali cu = (Tba_cambi_ufficiali) arrivo[0];
					if (!cu.getTs_var().equals(cambio.getDataUpd())) {
						throw new ValidationException(
								"operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);
					}

					// Tba_cambi_ufficiali cu = (Tba_cambi_ufficiali)
					// elenco.get(0);
					cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					// DATA di sistema
					Date dataodierna = new Date();
					// aggiunta il 20.01.09
					if (cambio.isValRifer()) {
						SimpleDateFormat formato = new SimpleDateFormat(
								"dd/MM/yyyy");
						try {
							dataodierna = formato.parse("31/12/9999");
						} catch (ParseException e) {

							log.error("", e);
						}
					}
					cu.setData_var(dataodierna);
					cu.setUte_var(cambio.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
					cu.setTs_var(ts);
					cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));

					ret = cambiDao.modificaCambioHib(cu);

				} else {
					if (elenco != null && elenco.size() > 1) {
						throw new ValidationException(
								"cambierroreModificaRecordNonUnivoco",
								ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
					}
				}
			}
		} catch (ValidationException e) {
			throw e;

		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaCambioHib(CambioVO cambio) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			Tba_cambi_ufficialiDao dao = new Tba_cambi_ufficialiDao();
			// ret=cambiDao.modificaCambioHib(cambio) ;
			this.ValidaCambioVO(cambio);
			if (cambio.getTassoCambio() == 0) {
				// tasso inserimento a zero
				throw new ValidationException("cambierroreInserimentoTasso",
						ValidationExceptionCodici.cambierroreInserimentoTasso);
			}
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			ListaSuppCambioVO lsc = this.trasformaCambioInlistaSupp(cambio);
			if (lsc != null) {
				List elenco = dao.getRicercaListaCambiHib(lsc);
				if (ValidazioneDati.size(elenco) == 1) {
					// controlla esistenza con legami ad altri oggetti ed inibizione
					List elenco2 = dao.getRicercaListaCambiDaCancHib(lsc);
					if (ValidazioneDati.size(elenco2) == 1) {
						// vuol dire che ha legami, quindi non è cancellabile
						elenco = null;
						throw new ValidationException("erroreCancellaCambio",
								ValidationExceptionCodici.erroreCancellaCambio);

					}

					//almaviva5_20140617 #5078
					if (cambio.isValRifer() && dao.countValuteNonRiferimento(cambio.getCodPolo(), cambio.getCodBibl()) > 0)
						throw new ValidationException(SbnErrorTypes.ACQ_VALUTA_RIFERIMENTO_NON_CANCELLABILE);

					// metto i valori da non sovrascrivere
					Object[] arrivo = (Object[]) elenco.get(0);
					Tba_cambi_ufficiali cu = (Tba_cambi_ufficiali) arrivo[0];

					// va cancellata la riga sottostante ?????
					cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					// DATA di sistema
					cu.setData_var(new Date());
					cu.setUte_var(cambio.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
					cu.setTs_var(DaoManager.now());
					cu.setFl_canc('S');
					ret = dao.cancellaCambioHib(cu);
				} else {
					if (ValidazioneDati.size(elenco) > 1) {
						throw new ValidationException(
								"cambierroreModificaRecordNonUnivoco",
								ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
					}
				}
			}

		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */

	public List getRicercaListaSezioniHib(ListaSuppSezioneVO ricercaSezioni)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		List<Tba_sez_acquis_bibliografiche> results = null;
		List<SezioneVO> listaSezioni = null;
		Tba_sez_acquis_bibliografiche sa = null;
		SezioneVO rec = null;
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
			this.ValidaListaSuppSezioneVO(ricercaSezioni);
			results = sezioniDao.getRicercaListaSezioniHib(ricercaSezioni);
			this.ValidaRicercaSezioni(results);

			// ciclo di riempimento
			for (int index = 0; index < results.size(); index++) {
				sa = results.get(index);
				rec = new SezioneVO();
				// carica il resultset
				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(ricercaSezioni.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(ricercaSezioni.getCodBibl());
				bib.setCd_polo(polo);

				rec.setProgressivo(index + 1);
				rec.setCodPolo(polo.getCd_polo());
				rec.setCodBibl(bib.getCd_biblioteca());
				rec.setDescrizioneSezione(sa.getNome().trim().toUpperCase());
				rec.setCodiceSezione(sa.getCod_sezione().trim());

				BigDecimal bd = sa.getSomma_disp();
				bd.setScale(2, BigDecimal.ROUND_HALF_UP);
				rec.setSommaDispSezione(bd.doubleValue());
				rec.setNoteSezione(sa.getNote());
				rec.setAnnoValiditaSezione(sa.getAnno_val().toString());

				bd = sa.getBudget();
				bd.setScale(2, BigDecimal.ROUND_HALF_UP);
				rec.setBudgetSezione(bd.doubleValue()); // tramite vo riempie
				// anche il campo
				// importodelta
				rec.setIdSezione(sa.getId_sez_acquis_bibliografiche());
				rec.setDataUpd(sa.getTs_var());

				rec.setBudgetLetto(bd.doubleValue());

				String dataFormattata = "";
				try {
					dataFormattata = format1.format(sa.getData_val());
				} catch (Exception e) {
					// log.error("", e);
				}
				rec.setDataVal(dataFormattata); // tramite vo riempie anche il
				// boolean chiusa

				// rec.setBudgetDiCapitolo(Double.valueOf(rs.getString("budgetCap")));
				// INIZIALIZZAZIONE
				if (listaSezioni == null) {
					listaSezioni = new ArrayList();
				}
				listaSezioni.add(rec);
			}
			// inizio
			int esercizio = 0;
			if (ricercaSezioni.getEsercizio() != null
					&& ricercaSezioni.getEsercizio().trim().length() != 0) {
				esercizio = Integer.valueOf(ricercaSezioni.getEsercizio());
			}

			// 09/01/09 i dati calcolati sono prelevati da db ma solo da
			// elaboraspesa può essere attivata la procedura calcola
			if (listaSezioni.size() == 1
					&& (esercizio > 0
							|| (ricercaSezioni.getDataOrdineDa() != null && ricercaSezioni
									.getDataOrdineDa().trim().length() > 0) || (ricercaSezioni
							.getDataOrdineA() != null && ricercaSezioni
							.getDataOrdineA().trim().length() > 0))) {
				CalcoliVO risult = new CalcoliVO();
				risult = dao.calcola(listaSezioni.get(0)
						.getCodPolo(), listaSezioni.get(0)
						.getCodBibl(), listaSezioni.get(0)
						.getIdSezione(), listaSezioni.get(0), 0,
						null, esercizio, ricercaSezioni.getDataOrdineDa(),
						ricercaSezioni.getDataOrdineA(), ricercaSezioni
								.getTicket(), ricercaSezioni.getLoc());
				SezioneVO sezioneInOggetto = listaSezioni.get(0);
				double disp = sezioneInOggetto.getBudgetSezione();
				// String
				// dispStr=Pulisci.VisualizzaImporto(listaSezioni.get(i).getBudgetSezione());
				if (risult != null && risult.getOrdinato() != 0) {
					disp = sezioneInOggetto.getBudgetSezione()
							- risult.getOrdinato();
					// dispStr=Pulisci.VisualizzaImporto(disp);
				}
				sezioneInOggetto.setSommaDispSezione(disp);
			}

			if (listaSezioni.size() == 1 && ricercaSezioni.isStoria()) {

				List<Tra_sez_acq_storico> resultsSub = sezioniDao
						.getRicercaListaSezioneStoriaHib(ricercaSezioni);

				int numRighe2 = 0;
				List<StrutturaQuinquies> righeEsameStoria = new ArrayList<StrutturaQuinquies>();
				StrutturaQuinquies rigaEsameStoria;
				for (int index = 0; index < resultsSub.size(); index++) {
					Tra_sez_acq_storico subEle = resultsSub.get(index);
					rigaEsameStoria = new StrutturaQuinquies();

					// format1.format(oggORD.getData_ord());
					String dataFormattata = "";
					try {
						dataFormattata = format1.format(subEle
								.getData_var_bdg());
					} catch (Exception e) {
						// log.error("", e);
					}
					rigaEsameStoria.setCodice1(dataFormattata);

					String valoreStr = "0.00";
					rigaEsameStoria.setCodice2(valoreStr);
					if (subEle.getImporto_diff() != null) {
						DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						dfs.setGroupingSeparator(',');
						dfs.setDecimalSeparator('.');
						// controllo formattazione con virgola separatore dei
						// decimali
						try {
							DecimalFormat df = new DecimalFormat("#,##0.00",
									dfs);
							// importo
							String stringa = df.format(subEle.getImporto_diff()
									.doubleValue());
							valoreStr = stringa;
						} catch (Exception e) {
							log.error("", e);
						}
					}
					if (valoreStr != null && !valoreStr.equals("0.00")) {
						rigaEsameStoria.setCodice2(valoreStr);
					}

					String bdgOldStr = "0.00";
					rigaEsameStoria.setCodice3(bdgOldStr);
					if (subEle.getBudget_old() != null) {
						DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						dfs.setGroupingSeparator(',');
						dfs.setDecimalSeparator('.');
						// controllo formattazione con virgola separatore dei
						// decimali
						try {
							DecimalFormat df = new DecimalFormat("#,##0.00",
									dfs);
							// importo
							String stringa = df.format(subEle.getBudget_old());
							bdgOldStr = stringa;
						} catch (Exception e) {
							log.error("", e);
						}
					}
					if (bdgOldStr != null && !bdgOldStr.equals("0.00")) {
						rigaEsameStoria.setCodice3(bdgOldStr);
					}

					righeEsameStoria.add(rigaEsameStoria);

				}
				if (righeEsameStoria != null && righeEsameStoria.size() > 0) // listaSezioni.size()==1
				{
					listaSezioni.get(0).setRigheEsameStoria(righeEsameStoria);
				}
			}

			// fine

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {

			log.error("", e);
		}
		this.ValidaRicercaSezioni(listaSezioni);
		return listaSezioni;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaSezioneHib(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			this.ValidaSezioneVO(sezione);
			Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			if (this.trasformaSezioneInlistaSupp(sezione) != null) {
				List elenco = null;
				elenco = sezioniDao.getRicercaListaSezioniHib(this
						.trasformaSezioneInlistaSupp(sezione));
				// this.ValidaRicercaSezioni(elenco);
				if (elenco != null && elenco.size() == 1) {
					Timestamp ts = new java.sql.Timestamp(System
							.currentTimeMillis());
					Tba_sez_acquis_bibliografiche sa = (Tba_sez_acquis_bibliografiche) elenco
							.get(0);

					if (!sa.getTs_var().equals(sezione.getDataUpd())) {
						throw new ValidationException(
								"operazioneInConcorrenza",
								ValidationExceptionCodici.operazioneInConcorrenza);
					}

					// DATA di sistema
					Date dataodierna = new Date();

					sa.setUte_var(sezione.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
					sa.setTs_var(ts);
					sa.setNote(sezione.getNoteSezione());
					sa.setNome(sezione.getDescrizioneSezione().trim()
							.toUpperCase());
					BigDecimal bd = BigDecimal.valueOf(sezione
							.getBudgetSezione());
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					sa.setBudget(bd);

					// CALCOLO DISPONIBILITA
					// 09.01.09 calcolo disponibilità spostato su db
					Locale locale = Locale.getDefault(); // aggiornare con
					// quella locale se
					// necessario
					// calcolo disponibilità
					CalcoliVO risult = new CalcoliVO();
					try {
						risult = dao.calcola(sezione.getCodPolo(), sezione
								.getCodBibl(), sezione.getIdSezione(), sezione,
								0, null, 0, null, null, null, locale);
					} catch (Exception e) {

						// l'errore capita in questo punto
						log.error("", e);
					}
					double speso = 0.00; // TODO
					// double disp=sezione.getBudgetSezione()-speso;
					double disp = sezione.getBudgetSezione();
					if (risult != null && risult.getOrdinato() != 0) {
						disp = disp - risult.getOrdinato();
					}
					sezione.setSommaDispSezione(disp);
					// bd=BigDecimal.valueOf(sezione.getSommaDispSezione());
					bd = BigDecimal.valueOf(disp);
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					sa.setSomma_disp(bd);

					bd = new BigDecimal(sezione.getAnnoValiditaSezione());
					bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					sa.setAnno_val(bd);

					// cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));
					// cu.setCambio(BigDecimal.valueOf(cambio.getTassoCambio()));

					sa.setData_val(null);
					if (sezione.getDataVal() != null
							&& sezione.getDataVal().trim().length() > 0) {
						SimpleDateFormat formato = new SimpleDateFormat(
								"dd/MM/yyyy");
						try {
							Date data_validita = formato.parse(sezione
									.getDataVal());
							sa.setData_val(data_validita);
						} catch (ParseException e) {

							log.error("", e);
						}
					}

					ret = sezioniDao.modificaSezioneHib(sa);

					// caso variazione di budget (registrare il record su
					// storico)
					if (sezione.getBudgetSezione() > 0
							&& sezione.getBudgetLetto() > 0
							&& sezione.getBudgetSezione() != sezione
									.getBudgetLetto()) {
						Tra_sez_acq_storico ss = new Tra_sez_acq_storico();
						ss.setId_sez_acquis_bibliografiche(sa);
						ss.setTs_ins(ts);

						ss.setData_var_bdg(new Date());

						bd = BigDecimal.valueOf(sezione.getBudgetLetto());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						ss.setBudget_old(bd);

						bd = BigDecimal.valueOf(sezione.getImportoDelta());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						ss.setImporto_diff(bd);

						ss.setUte_ins(sezione.getUtente());
						ss.setUte_var(sezione.getUtente());
						ss.setTs_var(ts);
						ss.setFl_canc('N');
						sezioniDao.inserisciSezioneStoriaHib(ss);
					}

				} else {
					if (elenco != null && elenco.size() > 1) {
						throw new ValidationException(
								"cambierroreModificaRecordNonUnivoco",
								ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
					}
				}
			}
		} catch (ValidationException e) {
			throw e;

		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaSezioneHib(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			this.ValidaSezioneVO(sezione);
			Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			if (this.trasformaSezioneInlistaSupp(sezione) != null) {
				List elenco = null;

				// controlla esistenza sezione
				elenco = sezioniDao.getRicercaListaSezioniHib(this
						.trasformaSezioneInlistaSupp(sezione));
				if (elenco != null && elenco.size() == 1) {
					// controlla esistenza con legami ad altri oggetti ed
					// inibizione

					List elenco2 = null;
					elenco2 = sezioniDao.getRicercaListaSezioniDaCancHib(this
							.trasformaSezioneInlistaSupp(sezione));
					if (elenco2 != null && elenco2.size() == 1) {
						Tba_sez_acquis_bibliografiche sa = (Tba_sez_acquis_bibliografiche) elenco2
								.get(0);
						Object[] su = sa
								.getTba_suggerimenti_bibliografici().toArray();
						Object[] ordX = sa.getTba_ordini().toArray();
						if (ordX.length == 0) // non esistono legami con ordini
						// devo indagare sui sugg
						{
							if (su.length > 0) // esistono legami con sugg
							{
								Boolean suggRifiutati = true;
								for (int p = 0; p < su.length; p++) {
									Tba_suggerimenti_bibliografici objSugg = (Tba_suggerimenti_bibliografici) su[p];
									if (objSugg.getFl_canc() != 'S') // non
									// cancellati
									{
										if (objSugg.getStato_sugg() != 'R') // contains(String
										// stato_sugg.equals("R")))
										{
											suggRifiutati = false;
											break;
										}
									}
								}
								// verifico l'ulteriore condizione di esistenza
								// di suggerimenti non rifiutati
								if (!suggRifiutati) {
									elenco = null;
									throw new ValidationException(
											"erroreCancellaSez",
											ValidationExceptionCodici.erroreCancellaSez);
								}
							} else {
								// vuol dire che non ha legamicon ord nè sugg,
								// quindi è cancellabile

							}

						} else {
							// vuol dire che ha legami CON ORDINI, quindi non è
							// cancellabile A MENO CHE SIANO CON fl_canc='S'

							Boolean ordCancellati = true;
							for (int h = 0; h < ordX.length; h++) {
								Tba_ordini objOrd = (Tba_ordini) ordX[h];
								if (objOrd.getFl_canc() != 'S') // non
								// cancellati
								{
									ordCancellati = false;
									break;
								}
							}
							// verifico l'ulteriore condizione di esistenza di
							// suggerimenti non rifiutati
							if (!ordCancellati) {
								elenco = null;
								throw new ValidationException(
										"erroreCancellaSez",
										ValidationExceptionCodici.erroreCancellaSez);
							}

						}

					}
				}

				// this.ValidaRicercaSezioni(elenco);
				if (elenco != null && elenco.size() == 1) {
					Timestamp ts = new java.sql.Timestamp(System
							.currentTimeMillis());
					Tba_sez_acquis_bibliografiche sa = (Tba_sez_acquis_bibliografiche) elenco
							.get(0);

					sa.setUte_var(sezione.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
					sa.setTs_var(ts);
					sa.setFl_canc('S');
					ret = sezioniDao.cancellaSezioneHib(sa);
				} else {
					if (elenco != null && elenco.size() > 1) {
						throw new ValidationException(
								"cambierroreModificaRecordNonUnivoco",
								ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
					}
					if (elenco == null) {
						throw new ValidationException("erroreCancella",
								ValidationExceptionCodici.erroreCancella);
					}

				}
			}
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciSezioneHib(SezioneVO sezione) throws DataException,
			ApplicationException, ValidationException {
		boolean ret = false;
		try {
			this.ValidaSezioneVO(sezione);
			Tba_sez_acquis_bibliograficheDao sezioniDao = new Tba_sez_acquis_bibliograficheDao();
			// ret=sezioniDao.inserisciSezioneHib(sezione) ;
			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			ListaSuppSezioneVO listaSupp = this
					.trasformaSezioneInlistaSupp(sezione);
			if (listaSupp != null) {
				List elenco = null;
				elenco = sezioniDao.getRicercaListaSezioniHib(listaSupp);
				// this.ValidaRicercaSezioni(elenco);
				if (elenco == null) {
					Timestamp ts = new java.sql.Timestamp(System
							.currentTimeMillis());
					// controllo che non sia stato cancellato logicamente: in
					// tal caso
					// faccio update anche del flag e non insert
					listaSupp.setFlag_canc(true); // IMP
					List elencoCanc = null;
					elencoCanc = sezioniDao
							.getRicercaListaSezioniHib(listaSupp);
					if (elencoCanc != null && elencoCanc.size() == 1) {
						// update di tutti campi (esclusa la chiave) e anche del
						// flag
						Tba_sez_acquis_bibliografiche sa = (Tba_sez_acquis_bibliografiche) elencoCanc
								.get(0);
						sa.setFl_canc('N'); // IMP
						sa.setUte_var(sezione.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
						sa.setTs_var(ts);
						sa.setNome(sezione.getDescrizioneSezione().trim()
								.toUpperCase());
						sa.setNote(sezione.getNoteSezione());

						BigDecimal bd = BigDecimal.valueOf(sezione
								.getBudgetSezione());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setBudget(bd);

						// CALCOLO DISPONIBILITA
						double speso = 0.00; // TODO
						double disp = sezione.getBudgetSezione() - speso;
						// bd=BigDecimal.valueOf(sezione.getSommaDispSezione());
						bd = BigDecimal.valueOf(disp);
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setSomma_disp(bd);

						bd = new BigDecimal(sezione.getAnnoValiditaSezione());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setAnno_val(bd);

						sa.setData_val(null);
						if (sezione.getDataVal() != null
								&& sezione.getDataVal().trim().length() > 0) {
							SimpleDateFormat formato = new SimpleDateFormat(
									"dd/MM/yyyy");
							try {
								Date data_validita = formato
										.parse(sezione.getDataVal());
								sa.setData_val(data_validita);
							} catch (ParseException e) {

								log.error("", e);
							}
						}

						ret = sezioniDao.modificaSezioneHib(sa);
					}
					if (elencoCanc == null) {
						// inserimento
						Tba_sez_acquis_bibliografiche sa = new Tba_sez_acquis_bibliografiche();
						Tbf_polo polo = new Tbf_polo();
						// polo.setCd_polo(ricercaCambi.getCodPolo());
						polo.setCd_polo(sezione.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_biblioteca(sezione.getCodBibl());
						bib.setCd_polo(polo);
						// DATA di sistema
						sa.setCd_bib(bib);

						sa.setCod_sezione(sezione.getCodiceSezione().trim()
								.toUpperCase()); // cod_sezione
						sa.setNome(sezione.getDescrizioneSezione().trim()
								.toUpperCase());
						sa.setNote(sezione.getNoteSezione());

						BigDecimal bd = BigDecimal.valueOf(sezione
								.getBudgetSezione());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setBudget(bd);

						// CALCOLO DISPONIBILITA
						double speso = 0.00;
						double disp = sezione.getBudgetSezione() - speso;
						// bd=BigDecimal.valueOf(sezione.getSommaDispSezione());
						bd = BigDecimal.valueOf(disp);
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setSomma_disp(bd);

						bd = new BigDecimal(sezione.getAnnoValiditaSezione());
						bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						sa.setAnno_val(bd);
						sa.setUte_ins(sezione.getUtente());
						sa.setTs_ins(ts);
						sa.setUte_var(sezione.getUtente());
						sa.setTs_var(ts);
						sa.setFl_canc('N');

						sa.setData_val(null);
						if (sezione.getDataVal() != null
								&& sezione.getDataVal().trim().length() > 0) {
							SimpleDateFormat formato = new SimpleDateFormat(
									"dd/MM/yyyy");
							try {
								Date data_validita = formato
										.parse(sezione.getDataVal());
								sa.setData_val(data_validita);
							} catch (ParseException e) {

								log.error("", e);
							}
						}
						ret = sezioniDao.inserisciSezioneHib(sa);
					}

				} else {
					throw new ValidationException(
							"cambierroreInserimentoEsistenzaRecord",
							ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
				}

			}

		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	public ListaSuppSezioneVO trasformaSezioneInlistaSupp(SezioneVO sezione)
			throws EJBException {
		ListaSuppSezioneVO listaSupp = null;
		try {
			// sufficiente impostare la chiave
			listaSupp = new ListaSuppSezioneVO();
			listaSupp.setCodBibl(sezione.getCodBibl());
			listaSupp.setCodPolo(sezione.getCodPolo());
			listaSupp.setCodiceSezione(sezione.getCodiceSezione());
			listaSupp.setChiusura(sezione.isChiusa());
		} catch (EJBException e) {

			log.error("", e);
		}
		return listaSupp;

	}

	public ListaSuppFornitoreVO trasformaFornitoreInlistaSupp(
			FornitoreVO fornitore) throws EJBException {
		ListaSuppFornitoreVO listaSupp = null;
		try {
			// sufficiente impostare la chiave
			listaSupp = new ListaSuppFornitoreVO();
			listaSupp.setCodPolo(fornitore.getCodPolo());
			if (fornitore.getCodFornitore() != null
					&& fornitore.getCodFornitore().trim().length() > 0) {
				listaSupp.setCodFornitore(fornitore.getCodFornitore());
			}

			if (fornitore.getFornitoreBibl() != null
					&& fornitore.getFornitoreBibl().getCodBibl() != null
					&& fornitore.getFornitoreBibl().getCodBibl().trim()
							.length() > 0) {
				listaSupp.setCodBibl(fornitore.getFornitoreBibl().getCodBibl());
				listaSupp.setLocale("1");
			} else {
				listaSupp.setCodBibl(fornitore.getCodBibl());
				listaSupp.setLocale("0");
			}
			if (fornitore.getNomeFornitore() != null
					&& fornitore.getNomeFornitore().trim().length() > 0) {
				listaSupp.setNomeFornitore(fornitore.getNomeFornitore().trim());
			}
			if (fornitore.getTipoPartner() != null
					&& fornitore.getTipoPartner().trim().length() > 0) {
				listaSupp.setTipoPartner(fornitore.getTipoPartner());
			}
			if (fornitore.getPaese() != null
					&& fornitore.getPaese().trim().length() > 0) {
				listaSupp.setPaese(fornitore.getPaese());
			}
			if (fornitore.getProvincia() != null
					&& fornitore.getProvincia().trim().length() > 0) {
				listaSupp.setProvincia(fornitore.getProvincia());
			}
			if (fornitore.getChiaveFor() != null
					&& fornitore.getChiaveFor().trim().length() > 0) {
				listaSupp.setChiaveFor(fornitore.getChiaveFor());
			}

		} catch (EJBException e) {

			log.error("", e);
		}
		return listaSupp;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getRicercaListaFornitoriHib(
			ListaSuppFornitoreVO ricercaFornitori)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		List results = null;
		try {
			Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
			Tbr_fornitori fo = new Tbr_fornitori();
			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche();
			results = fornitoriDao
					.getRicercaListaFornitoriHib(ricercaFornitori);
			this.ValidaRicercaFornitori(results);
			return results;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {

			log.error("", e);
		}
		return results;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean modificaFornitoreHib(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		try {
			// this.ValidaFornitoreVO(fornitore);
			Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
			Tbr_fornitori fo = new Tbr_fornitori();
			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche();
			ret = fornitoriDao.modificaFornitoreHib(fo);
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean cancellaFornitoreHib(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		try {
			this.ValidaFornitoreVO(fornitore);
			Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
			Tbr_fornitori fo = new Tbr_fornitori();
			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche();

			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			if (this.trasformaFornitoreInlistaSupp(fornitore) != null) {
				List elenco = null;

				// controlla esistenza sezione
				elenco = fornitoriDao
						.getRicercaListaFornitoriHib(trasformaFornitoreInlistaSupp(fornitore));
				if (elenco != null && elenco.size() == 1) {
					// controlla esistenza con legami ad altri oggetti ed
					// inibizione
					List elenco2 = null;
					// elenco2 =
					// fornitoriDao.getRicercaListaFornitoriDaCancHib(trasformaFornitoreInlistaSupp(fornitore));
					if (elenco2 != null && elenco2.size() == 1) {
						// vuol dire che ha legami, quindi non è cancellabile
						elenco = null;
						throw new ValidationException("erroreCancellaSez",
								ValidationExceptionCodici.erroreCancellaSez);

					}
				}

				// this.ValidaRicercaSezioni(elenco);
				if (elenco != null && elenco.size() == 1) {
					Timestamp ts = new java.sql.Timestamp(System
							.currentTimeMillis());
					Tbr_fornitori forn = (Tbr_fornitori) elenco.get(0);

					forn.setUte_var(fornitore.getUtente()); // hibernateDAO.getFirmaUtente(ticket)
					forn.setTs_var(ts);
					forn.setFl_canc('S');
					// ret=fornitoriDao.cancellaFornitoreHib(fornitore) ;

				} else {
					if (elenco != null && elenco.size() > 1) {
						throw new ValidationException(
								"cambierroreModificaRecordNonUnivoco",
								ValidationExceptionCodici.cambierroreModificaRecordNonUnivoco);
					}
					if (elenco == null) {
						throw new ValidationException("erroreCancella",
								ValidationExceptionCodici.erroreCancella);
					}

				}
			}
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public boolean inserisciFornitoreHib(FornitoreVO fornitore)
			throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		try {
			this.ValidaFornitoreVO(fornitore);
			Tba_fornitoriDao fornitoriDao = new Tba_fornitoriDao();
			Tbr_fornitori fo = new Tbr_fornitori();
			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche();
			ret = fornitoriDao.inserisciFornitoreHib(fo);
		} catch (Exception e) {

			log.error("", e);
		}
		return ret;

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public ListaSuppFatturaVO gestioneFatturaDaDocFisico(
			ListaSuppFatturaVO ricercaFatture) throws DataException,
			ApplicationException, ValidationException {
		ricercaFatture.setRicercaOrd(false);
		boolean ret = false;
		try {

			// effettuare prima la ricerca: se esiste non effettuare
			// l'inserimento
			if (ricercaFatture != null)

			{

				List<OrdiniVO> ordInEsamelista = null;
				try {
					// controllo esistenza ordine
					ListaSuppOrdiniVO ricercaOrdini = new ListaSuppOrdiniVO();
					ricercaOrdini.setCodBibl(ricercaFatture.getCodBibl());
					ricercaOrdini.setCodPolo(ricercaFatture.getCodPolo());
					if (ricercaFatture.getOrdine() != null
							&& ricercaFatture.getOrdine().getCodice1() != null
							&& ricercaFatture.getOrdine().getCodice1().trim()
									.length() > 0) {
						ricercaOrdini.setTipoOrdine(ricercaFatture.getOrdine()
								.getCodice1());
					}

					if (ricercaFatture.getOrdine() != null
							&& ricercaFatture.getOrdine().getCodice2() != null
							&& ricercaFatture.getOrdine().getCodice2().trim()
									.length() > 0) {
						ricercaOrdini.setAnnoOrdine(ricercaFatture.getOrdine()
								.getCodice2());
					}
					if (ricercaFatture.getOrdine() != null
							&& ricercaFatture.getOrdine().getCodice3() != null
							&& ricercaFatture.getOrdine().getCodice3().trim()
									.length() > 0) {
						ricercaOrdini.setCodOrdine(ricercaFatture.getOrdine()
								.getCodice3());
					}
					ricercaOrdini.setOrdinamento("");
					ricercaOrdini.setChiamante("");
					ricercaOrdini.setElemXBlocchi(0);

					ordInEsamelista = dao.getRicercaListaOrdini(ricercaOrdini);
				} catch (ValidationException e) {
					log.error("", e);
					throw new ValidationException("ordineNONtrovato",
							ValidationExceptionCodici.ordineNONtrovato);
				} catch (Exception e) {

					log.error("", e);

				}

				ricercaFatture.setSelezioniChiamato(null);
				List elenco = null;
				if (ordInEsamelista != null && ordInEsamelista.size() == 1) {
					try {
						OrdiniVO ordInEsame = ordInEsamelista.get(0);
						// implemento i criteri di ricerca con i dati
						// dell'ordine trovato
						StrutturaTerna bilancio = new StrutturaTerna("", "", "");
						if (ordInEsame.getBilancio() != null) {
							bilancio = ordInEsame.getBilancio();
						}
						ricercaFatture.setBilancio(bilancio);
						elenco = dao.getRicercaListaFatture(ricercaFatture);
					} catch (ValidationException e) {
						log.error("", e);

					} catch (Exception e) {

						log.error("", e);
					}

					FatturaVO fattura = null;

					if (elenco == null) {

						fattura = new FatturaVO();
						// riempire con i dati passati
						// trasforma listasupporto in fattura
						// chiave ordine - bibl - tipo - anno - progr
						// num fatt, data fatt., fornitore

						// configurazione
						ConfigurazioneORDVO configurazioneORD = new ConfigurazioneORDVO();
						configurazioneORD.setCodBibl(ricercaFatture
								.getCodBibl());
						configurazioneORD.setCodPolo(ricercaFatture
								.getCodPolo());
						ConfigurazioneORDVO configurazioneORDLetta = new ConfigurazioneORDVO();
						Boolean gestBil = true;

						try {
							configurazioneORDLetta = dao
									.loadConfigurazioneOrdini(configurazioneORD);
							if (configurazioneORDLetta != null
									&& !configurazioneORDLetta
											.isGestioneBilancio()) {
								fattura.setGestBil(false);
							}

						} catch (Exception e) {

							// l'errore capita in questo punto
							log.error("", e);
						}
						fattura.setCodPolo(ricercaFatture.getCodPolo());
						fattura.setCodBibl(ricercaFatture.getCodBibl());
						fattura.setAnnoFattura(ricercaFatture.getAnnoFattura());
						fattura.setDataFattura(ricercaFatture.getDataFattura());
						fattura.setNumFattura(ricercaFatture.getNumFattura());
						fattura.setFornitoreFattura(ricercaFatture
								.getFornitore());
						fattura
								.setRigheDettaglioFattura(new ArrayList<StrutturaFatturaVO>());
						fattura.setTipoFattura(ricercaFatture.getTipoFattura());
						fattura.setStatoFattura("1");
						fattura.setCambioFattura(1.00);
						fattura.setValutaFattura("EUR");
						fattura.setUtente(ricercaFatture.getUtente());
						fattura.setTicket(ricercaFatture.getTicket());
						fattura.setImportoFattura(ricercaFatture
								.getImportoFattura());
						fattura.setScontoFattura(0.00);

						StrutturaFatturaVO rigaFat = new StrutturaFatturaVO();
						rigaFat.setRigaFattura(1);
						rigaFat.setBilancio(ricercaFatture.getBilancio());
						rigaFat.setOrdine(ricercaFatture.getOrdine());
						rigaFat.setImportoRigaFattura(ricercaFatture
								.getImportoFattura());
						rigaFat.setSconto1RigaFattura(0.00);
						rigaFat.setSconto2RigaFattura(0.00);
						rigaFat.setCodIvaRigaFattura("00");
						fattura.getRigheDettaglioFattura().add(rigaFat);
						fattura.setFatturaVeloce(true);

						// try {
						ret = dao.inserisciFattura(fattura);

						if (ret) {
							List<FatturaVO> arrFatt = new ArrayList<FatturaVO>();
							arrFatt.add(fattura);
							// aggiornamento della lista di supporto
							ricercaFatture.setSelezioniChiamato(arrFatt);
						} else {
							// errore in inserimento
						}

					} else {
						if (elenco.size() == 1) {
							fattura = (FatturaVO) elenco.get(0);
							// controllo lo stato della fattura che non deve
							// essere 3- ordine di pagamento emesso o
							// 4-contabilizzato
							if (!fattura.getStatoFattura().equals("3")
									&& !fattura.getStatoFattura().equals("4")) {
								// Quando si modifica una fattura (già
								// controllata) e non si sta pagando occorre far
								// regredire lo stato a registrata
								// già gestito nel metodo di modifica
								fattura
										.setUtente(ricercaFatture.getUtente());
								fattura.setTicket(ricercaFatture.getTicket());
								StrutturaFatturaVO rigaFat = new StrutturaFatturaVO();
								rigaFat.setRigaFattura(fattura
										.getRigheDettaglioFattura().size() + 1);
								rigaFat.setBilancio(ricercaFatture
										.getBilancio());
								rigaFat.setOrdine(ricercaFatture.getOrdine());
								rigaFat.setImportoRigaFattura(ricercaFatture
										.getImportoFattura());
								rigaFat.setSconto1RigaFattura(0.00);
								rigaFat.setSconto2RigaFattura(0.00);
								rigaFat.setCodIvaRigaFattura("00");
								fattura.getRigheDettaglioFattura().add(rigaFat);
								double sommaImportoOrdineAggiunto = fattura
										.getImportoFattura();
								fattura
										.setImportoFattura(sommaImportoOrdineAggiunto
												+ ricercaFatture
														.getImportoFattura());
								fattura.setFatturaVeloce(true);

								// try {
								ret = dao.modificaFattura(fattura);
								if (ret) {
									List<FatturaVO> arrFatt = new ArrayList<FatturaVO>();
									arrFatt.add(fattura);
									// aggiornamento della lista di supporto
									ricercaFatture
											.setSelezioniChiamato(arrFatt);
								} else {
									// errore in modifica
								}
							} else {
								// fattura esistente già pagata o contabilizzata
								// (non modificabile)
							}

						} else {
							// troppi risultati
							throw new ValidationException(
									"cambierroreInserimentoEsistenzaRecord",
									ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
						}

					}
					// prepara l'output per documento fisico

				} else // if (ordInEsamelista!=null &&
				// ordInEsamelista.size()==1)
				{

				}

			} else {
				throw new ValidationException(
						"cambierroreInserimentoEsistenzaRecord",
						ValidationExceptionCodici.cambierroreInserimentoEsistenzaRecord);
			}

		} catch (ValidationException e) {
			// logger.error("Errore in getBibliteche",e);
			// throw e;
			// throw new EJBException(e);
			log.error("", e);
			throw e;

		} catch (Exception e) {

			log.error("", e);

		}
		return ricercaFatture;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List getInventariOrdineRilegatura(
			ListaSuppOrdiniVO ricercaInvOrd) throws ResourceNotFoundException,
			ApplicationException, ValidationException {
		return dao.getInventariOrdineRilegatura(ricercaInvOrd);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List ripartoSpese(ListaSuppSpeseVO criteriRiparto)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return dao.ripartoSpese(criteriRiparto);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List statisticheTempi(ListaSuppSpeseVO criteriRiparto)
			throws ResourceNotFoundException, ApplicationException,
			ValidationException {
		return dao.statisticheTempi(criteriRiparto);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DataException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List<StampaBuoniVO> impostaBuoniOrdineDaStampare(
			ConfigurazioneBOVO configurazione, List listaOggetti,
			String tipoOggetti, Boolean ristampa, String ticket, String utente,
			String denoBibl) throws DataException, ApplicationException,
			ValidationException {
		List<StampaBuoniVO> output = new ArrayList<StampaBuoniVO>();
		try {
			List listaOggettiDaStampare = null;
			List<RigheOrdiniStampaBuoniVO> listaOrdiniDaStampare;
			// RigheOrdiniStampaBuoniVO
			if (listaOggetti != null && listaOggetti.size() > 0) {
				// oggetti di tipo ORDINE
				if (tipoOggetti != null && tipoOggetti.trim().length() > 0
						&& tipoOggetti.trim().equals("ORD")) {
					// vanno suddivisi per fornitore-tipoOrdine
					OrdiniVO eleLista = new OrdiniVO();
					String chiaveDiRottura = "";
					if (ristampa) {
						chiaveDiRottura = ((OrdiniVO) listaOggetti.get(0))
								.getFornitore().getCodice().trim()
								+ ((OrdiniVO) listaOggetti.get(0))
										.getTipoOrdine().trim()
								+ ((OrdiniVO) listaOggetti.get(0))
										.getDataOrdine()
								+ ((OrdiniVO) listaOggetti.get(0))
										.getDataStampaOrdine();
					} else {
						chiaveDiRottura = ((OrdiniVO) listaOggetti.get(0))
								.getFornitore().getCodice().trim()
								+ ((OrdiniVO) listaOggetti.get(0))
										.getTipoOrdine().trim();
					}

					StampaBuoniVO eleStampa = new StampaBuoniVO();

					// riempire eleStampa con i dati univoci
					// eleStampa.
					// .....
					eleStampa.setCodBibl(configurazione.getCodBibl());
					eleStampa.setCodPolo(configurazione.getCodPolo());
					// la denominazione della biblioteca è stata sostituita con
					// la prima riga dei dati di intestazione 06.07.09
					// if (denoBibl!=null && (eleStampa.getDenoBib()==null ||
					// (eleStampa.getDenoBib()!=null &&
					// eleStampa.getDenoBib().trim().length()==0 )))
					// {
					// eleStampa.setDenoBib(denoBibl);
					// }

					eleStampa.setTicket(ticket);
					eleStampa.setUtente(utente);
					eleStampa.setPresenzaLogoImg(configurazione
							.isPresenzaLogoImg());

					eleStampa.setNomeLogoImg(StampeUtil.getBatchFilesPath()
							+ File.separator + configurazione.getNomeLogoImg());
					// eleStampa.setNomeLogoImg(configurazione.getNomeLogoImg());
					// // path fisico
					// eleStampa.setNomeLogoImg("C:"+ File.separator +
					// "bdi_l.jpg"); // aggiunto per test
					String lingua = "";
					if (configurazione.isLinguaIT()) {
						lingua = "ITA";
						List<String> intest = new ArrayList<String>();
						String intestStr = "";
						for (int j = 0; configurazione
								.getListaDatiIntestazione() != null
								&& j < configurazione
										.getListaDatiIntestazione().length; j++) {
							if (j == 0) {
								eleStampa.setDenoBib(configurazione
										.getListaDatiIntestazione()[0]
										.getCodice2());
							}
							if (j > 0) {
								intest.add(configurazione
										.getListaDatiIntestazione()[j]
										.getCodice2());
								if (j > 1) {
									intestStr = intestStr + "\n"; // salto riga
								}
								intestStr = intestStr
										+ configurazione
												.getListaDatiIntestazione()[j]
												.getCodice2();
							}
						}
						eleStampa.setListaDatiIntestazione(intestStr);
					} else {
						lingua = "ENG";
						List<String> intest = new ArrayList<String>();
						String intestStr = "";
						for (int j = 0; configurazione
								.getListaDatiIntestazioneEng() != null
								&& j < configurazione
										.getListaDatiIntestazioneEng().length; j++) {
							if (j == 0) {
								eleStampa.setDenoBib(configurazione
										.getListaDatiIntestazioneEng()[0]
										.getCodice2());
							}
							if (j > 0) {
								intest.add(configurazione
										.getListaDatiIntestazioneEng()[j]
										.getCodice2());
								if (j > 1) {
									intestStr = intestStr + "\n"; // salto riga
								}
								intestStr = intestStr
										+ configurazione
												.getListaDatiIntestazioneEng()[j]
												.getCodice2();
							}

						}
						eleStampa.setListaDatiIntestazione(intestStr);
					}
					eleStampa.setEtichettaProtocollo(configurazione
							.isEtichettaProtocollo());
					eleStampa.setEtichettaDataProtocollo(configurazione
							.isEtichettaDataProtocollo());

					eleStampa.setNumProtocollo("");
					eleStampa.setNumBuonoOrdine("");

					eleStampa.setAnagFornitore(((OrdiniVO) listaOggetti.get(0))
							.getAnagFornitore());

					eleStampa.setTestoOggetto(configurazione.leggiOgg(lingua,
							((OrdiniVO) listaOggetti.get(0)).getTipoOrdine()));
					eleStampa.setTestoIntroduttivo(configurazione.leggiIntro(
							lingua, ((OrdiniVO) listaOggetti.get(0))
									.getTipoOrdine(), ((OrdiniVO) listaOggetti
									.get(0)).getCd_tipo_lav()));

					eleStampa.setPresenzaPrezzo(configurazione
							.isPresenzaPrezzo());

					if (configurazione.isLinguaIT()) {
						List<String> fineStp = new ArrayList<String>();
						String fineStpStr = "";

						for (int j = 0; configurazione.getListaDatiFineStampa() != null
								&& j < configurazione.getListaDatiFineStampa().length; j++) {
							fineStp
									.add(configurazione
											.getListaDatiFineStampa()[j]
											.getCodice2());
							if (j > 0) {
								fineStpStr = fineStpStr + "\n"; // salto riga
							}
							fineStpStr = fineStpStr
									+ configurazione.getListaDatiFineStampa()[j]
											.getCodice2();
						}
						eleStampa.setListaDatiFineStampa(fineStpStr);
					} else {
						List<String> fineStp = new ArrayList<String>();
						String fineStpStr = "";
						for (int j = 0; configurazione
								.getListaDatiFineStampaEng() != null
								&& j < configurazione
										.getListaDatiFineStampaEng().length; j++) {
							fineStp.add(configurazione
									.getListaDatiFineStampaEng()[j]
									.getCodice2());
							if (j > 0) {
								fineStpStr = fineStpStr + "\n"; // salto riga
							}
							fineStpStr = fineStpStr
									+ configurazione
											.getListaDatiFineStampaEng()[j]
											.getCodice2();
						}
						eleStampa.setListaDatiFineStampa(fineStpStr);
					}
					eleStampa.setPresenzaFirmaImg(configurazione
							.isPresenzaFirmaImg());
					// eleStampa.setNomeFirmaImg(configurazione.getNomeFirmaImg());
					eleStampa
							.setNomeFirmaImg(StampeUtil.getBatchFilesPath()
									+ File.separator
									+ configurazione.getNomeFirmaImg());

					eleStampa.setIndicaRistampa(configurazione
							.isIndicaRistampa());
					if (ristampa) {
						eleStampa
								.setDataStampa(((OrdiniVO) listaOggetti.get(0))
										.getDataOrdine()); // attribuzione della

					}

					double totImpSingoloBO = 0.00;
					double totImpSingoloEurBO = 0.00;
					RigheOrdiniStampaBuoniVO ordineDaStampare = new RigheOrdiniStampaBuoniVO();
					for (int i = 0; listaOggetti != null
							&& i < listaOggetti.size(); i++) {
						eleLista = ((OrdiniVO) listaOggetti.get(i));

						String confronto = "";
						if (ristampa) {
							// confronto=eleLista.getFornitore().getCodice().trim()+eleLista.getTipoOrdine().trim()
							// +eleLista.getAnnoOrdine();
							confronto = eleLista.getFornitore().getCodice()
									.trim()
									+ eleLista.getTipoOrdine().trim()
									+ eleLista.getDataOrdine()
									+ eleLista.getDataStampaOrdine();
						} else {
							confronto = eleLista.getFornitore().getCodice()
									.trim()
									+ eleLista.getTipoOrdine().trim();
						}

						if (chiaveDiRottura.equals(confronto)) {
							totImpSingoloBO = totImpSingoloBO
									+ eleLista.getPrezzoOrdine();
							totImpSingoloEurBO = totImpSingoloEurBO
									+ eleLista.getPrezzoEuroOrdine();
							// totImpSingoloBO=totImpSingoloBO +
							// eleLista.getPrezzoEuroOrdine();//!!!
							// riempire eleStampa con i dati molteplici

							// riempire l'elemento di riga di eleStampa ad es.
							// ordineDaStampare.set
							// ordineDaStampare.set;
							// .....
							ordineDaStampare = new RigheOrdiniStampaBuoniVO();
							ordineDaStampare.setNumOrdine(eleLista
									.getTipoOrdine()
									+ " "
									+ eleLista.getAnnoOrdine()
									+ " "
									+ eleLista.getCodOrdine());
							ordineDaStampare.setTipoOrdine(eleLista
									.getTipoOrdine());
							if (eleLista.getTipoOrdine() != null
									&& eleLista.getTipoOrdine().equals("R")
									&& eleLista.getRigheInventariRilegatura() != null
									&& eleLista.getRigheInventariRilegatura()
											.size() > 0) {
								ordineDaStampare
										.setRigheInventariRilegatura(eleLista
												.getRigheInventariRilegatura());
							}
							ordineDaStampare.setRinnovato(eleLista
									.isRinnovato());
							ordineDaStampare
									.setRinnovoOrigine(eleLista
											.getRinnovoOrigine().getCodice1()
											+ " "
											+ eleLista.getRinnovoOrigine()
													.getCodice2()
											+ " "
											+ eleLista.getRinnovoOrigine()
													.getCodice3());
							ordineDaStampare.setAbbonamento(false);

							// gestione ristampa
							if (ristampa) {
								eleStampa.setDataStampa(eleLista
										.getDataOrdine()); // attribuzione della
								// data del primo
								// ordine (come data
								// di ristampa del
								// bo )
							}

							if (eleStampa.isIndicaRistampa()
									&& eleLista.isStampato()) {
								eleStampa.setEtichettaRistampa("(ristampa)");
							}

							if (eleLista.isContinuativo()
									&& eleLista.getNaturaOrdine().equals("S")
									&& eleLista.getStatoOrdine().equals("A")) {

								if (eleLista.getTipoOrdine() != null
										&& (eleLista.getTipoOrdine()
												.equals("A") || eleLista
												.getTipoOrdine().equals("V"))) {
									ordineDaStampare.setAbbonamento(true);
									// ordineDaStampare.setAnnataAbbOrdine(eleLista.getAnnataAbbOrdine());
									ordineDaStampare
											.setAnnataAbbOrdine(eleLista
													.getAnnoAbbOrdine());
									ordineDaStampare.setDataPubblDa(eleLista
											.getDataPubblFascicoloAbbOrdine());
									ordineDaStampare.setDataPubbA(eleLista
											.getDataFineAbbOrdine());
								}
							}
							if (eleLista.getNumStandardArr() != null
									&& eleLista.getNumStandardArr().size() > 0) {
								// ordineDaStampare.set
								for (int x = 0; x < eleLista
										.getNumStandardArr().size(); x++) {
									// tck 2602 numeri standard non più
									// configurabili
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("I")) // configurazione.leggiAree("T")
									// &&
									{
										ordineDaStampare
												.setEtichetta_ISBN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("J")) {
										ordineDaStampare
												.setEtichetta_ISSN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("M")) {
										ordineDaStampare
												.setEtichetta_ISMN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("E")) // configurazione.leggiAree("E")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMEROEDITORIALE(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("L")) // configurazione.leggiAree("N")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMERODILASTRA(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("G")) // configurazione.leggiAree("P")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NPUBBLICAZIONEGOVERNATIVA(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
								}
							}

							ordineDaStampare.setTitolo(eleLista.getTitolo()
									.getDescrizione());
							// configurazione dell'isbd tck 2602
							String isbdConfigurato = "";
							String separatoreIsbd = ". - ";
							if (eleLista.getTitolo() != null
									&& eleLista.getTitolo().getCodice() != null
									&& eleLista.getTitolo().getCodice().trim()
											.length() > 0) {
								TitoloACQAreeIsbdVO isbdWithAree = dao
										.getAreeIsbdTitolo(eleLista.getTitolo()
												.getCodice());
								if (isbdWithAree != null) {
									if (configurazione.leggiAree("T")
											&& isbdWithAree != null
											&& isbdWithAree.getArea200Titolo() != null
											&& isbdWithAree.getArea200Titolo()
													.trim().length() > 0) {
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea200Titolo()
														.trim();
									}
									if (configurazione.leggiAree("E")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea205Edizione() != null
											&& isbdWithAree
													.getArea205Edizione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea205Edizione()
														.trim();
									}
									if (configurazione.leggiAree("N")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea207Numerazione() != null
											&& isbdWithAree
													.getArea207Numerazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea207Numerazione()
														.trim();
									}
									if (configurazione.leggiAree("P")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea210Pubblicazione() != null
											&& isbdWithAree
													.getArea210Pubblicazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea210Pubblicazione()
														.trim();
									}
									if (isbdWithAree != null
											&& isbdWithAree.getCollezione() != null
											&& isbdWithAree.getCollezione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree.getCollezione()
														.trim();
									}

								}
							}
							if (isbdConfigurato.trim().length() > 0) {
								ordineDaStampare.setTitolo(isbdConfigurato);
							}
							// fine configurazione dell'isbd

							if (eleStampa.isPresenzaPrezzo()) {
								ordineDaStampare.setPrezzo(eleLista
										.getPrezzoEuroOrdineStr());
							} else {
								ordineDaStampare.setPrezzo(null);
							}

							ordineDaStampare.setNote(eleLista
									.getNoteFornitore());
							// scarto degli ordini che non hanno titolo tck 2604
							// if (eleLista.getTipoOrdine()!=null &&
							// !eleLista.getTipoOrdine().equals("R") &&
							// ordineDaStampare.getTitolo()!=null &&
							// ordineDaStampare.getTitolo().trim().length()>0)

							if ((ordineDaStampare.getTitolo() != null && ordineDaStampare
									.getTitolo().trim().length() > 0)
									|| (eleLista.getTipoOrdine() != null && eleLista
											.getTipoOrdine().equals("R"))) {
								eleStampa.getListaOrdiniDaStampare().add(
										ordineDaStampare);
							} else {
								String appoReport = eleStampa.getReportErrori();
								eleStampa.setReportErrori(appoReport
										+ "Non è stato stampato l'ordine: "
										+ ordineDaStampare.getNumOrdine()
										+ ", per dati mancanti" + "\n");
							}

							if (i == (listaOggetti.size() - 1)) {
								DecimalFormatSymbols dfs = new DecimalFormatSymbols();
								dfs.setGroupingSeparator('.');
								dfs.setDecimalSeparator(',');
								// controllo formattazione con virgola
								// separatore dei decimali
								DecimalFormat df = new DecimalFormat(
										"#,##0.00", dfs);
								// importo

								String stringa = df.format(totImpSingoloBO);
								NumberFormat formatIT = NumberFormat
										.getCurrencyInstance();
								String strCurrency = "\u20AC ";
								strCurrency = strCurrency + stringa;
								// Number imp=formatIT.parse(strCurrency); // va
								// in errore se non è riconosciuto come formato
								// euro
								String totImpSingoloBOStr = formatIT
										.format(totImpSingoloBO);
								totImpSingoloBOStr = totImpSingoloBOStr
										.substring(2); // elimina il simbolo

								eleStampa.setValuta(eleLista.getValutaOrdine());

								String stringaEur = df
										.format(totImpSingoloEurBO);
								NumberFormat formatITEur = NumberFormat
										.getCurrencyInstance();
								String strCurrencyEur = "\u20AC ";
								strCurrencyEur = strCurrencyEur + stringa;
								// Number imp=formatIT.parse(strCurrency); // va
								// in errore se non è riconosciuto come formato
								// euro
								String totImpSingoloBOEurStr = formatIT
										.format(totImpSingoloEurBO);
								totImpSingoloBOEurStr = totImpSingoloBOEurStr
										.substring(2); // elimina il simbolo

								eleStampa
										.setImportoFornitura(totImpSingoloBOStr);

								eleStampa
										.setImportoFornituraEur(totImpSingoloBOEurStr);

								output.add(eleStampa);
							}
						} else {
							if (ristampa) {
								chiaveDiRottura = ((OrdiniVO) listaOggetti
										.get(i)).getFornitore().getCodice()
										.trim()
										+ ((OrdiniVO) listaOggetti.get(i))
												.getTipoOrdine().trim()
										+ ((OrdiniVO) listaOggetti.get(i))
												.getDataOrdine()
										+ ((OrdiniVO) listaOggetti.get(i))
												.getDataStampaOrdine();

							} else {
								chiaveDiRottura = ((OrdiniVO) listaOggetti
										.get(i)).getFornitore().getCodice()
										.trim()
										+ ((OrdiniVO) listaOggetti.get(i))
												.getTipoOrdine().trim();
							}

							DecimalFormatSymbols dfs = new DecimalFormatSymbols();
							dfs.setGroupingSeparator('.');
							dfs.setDecimalSeparator(',');
							// controllo formattazione con virgola separatore
							// dei decimali
							DecimalFormat df = new DecimalFormat("#,##0.00",
									dfs);
							// importo

							String stringa = df.format(totImpSingoloBO);
							NumberFormat formatIT = NumberFormat
									.getCurrencyInstance();
							String strCurrency = "\u20AC ";
							strCurrency = strCurrency + stringa;
							// Number imp=formatIT.parse(strCurrency); // va in
							// errore se non è riconosciuto come formato euro
							String totImpSingoloBOStr = formatIT
									.format(totImpSingoloBO);
							totImpSingoloBOStr = totImpSingoloBOStr
									.substring(2); // elimina il simbolo

							eleStampa.setImportoFornitura(totImpSingoloBOStr);
							// eleStampa.getListaOrdiniDaStampare().add(ordineDaStampare);
							output.add(eleStampa);
							// eleStampa=new StampaBuoniVO();
							totImpSingoloBO = 0.00;
							// utilizzo il vecchio eleStampa ripulendo alcuni
							// campi reset
							StampaBuoniVO eleStampa2 = new StampaBuoniVO();
							eleStampa2 = (StampaBuoniVO) eleStampa.clone();
							eleStampa2.setImportoFornitura("");
							eleStampa2
									.setListaOrdiniDaStampare(new ArrayList<RigheOrdiniStampaBuoniVO>());
							eleStampa2.setTestoOggetto(configurazione.leggiOgg(
									lingua, ((OrdiniVO) listaOggetti.get(i))
											.getTipoOrdine()));
							eleStampa2.setTestoIntroduttivo(configurazione
									.leggiIntro(lingua,
											((OrdiniVO) listaOggetti.get(i))
													.getTipoOrdine(),
											((OrdiniVO) listaOggetti.get(i))
													.getCd_tipo_lav()));
							eleStampa2
									.setAnagFornitore(((OrdiniVO) listaOggetti
											.get(i)).getAnagFornitore());
							eleStampa = eleStampa2;
							// ******
							// riempire eleStampa con i dati molteplici

							ordineDaStampare = new RigheOrdiniStampaBuoniVO();
							// riempire l'elemento di riga di eleStampa ad es.
							// ordineDaStampare.set
							// ordineDaStampare.set;
							// ..... elemento in considerazione, caricato
							eleLista = ((OrdiniVO) listaOggetti.get(i));
							ordineDaStampare.setNumOrdine(eleLista
									.getTipoOrdine()
									+ " "
									+ eleLista.getAnnoOrdine()
									+ " "
									+ eleLista.getCodOrdine());
							ordineDaStampare.setTipoOrdine(eleLista
									.getTipoOrdine());
							if (eleLista.getTipoOrdine() != null
									&& eleLista.getTipoOrdine().equals("R")
									&& eleLista.getRigheInventariRilegatura() != null
									&& eleLista.getRigheInventariRilegatura()
											.size() > 0) {
								ordineDaStampare
										.setRigheInventariRilegatura(eleLista
												.getRigheInventariRilegatura());
							}
							ordineDaStampare.setRinnovato(eleLista
									.isRinnovato());
							ordineDaStampare
									.setRinnovoOrigine(eleLista
											.getRinnovoOrigine().getCodice1()
											+ " "
											+ eleLista.getRinnovoOrigine()
													.getCodice2()
											+ " "
											+ eleLista.getRinnovoOrigine()
													.getCodice3());
							ordineDaStampare.setAbbonamento(false);
							// gestione ristampa
							if (ristampa) {
								eleStampa.setDataStampa(eleLista
										.getDataOrdine()); // attribuzione della
								// data del primo
								// ordine (come data
								// di ristampa del
								// bo )
							}

							if (eleStampa.isIndicaRistampa()
									&& eleLista.isStampato()) {
								eleStampa.setEtichettaRistampa("(ristampa)");
							}
							if (eleLista.isContinuativo()
									&& eleLista.getNaturaOrdine().equals("S")
									&& eleLista.getStatoOrdine().equals("A")) {

								if (eleLista.getTipoOrdine() != null
										&& (eleLista.getTipoOrdine()
												.equals("A") || eleLista
												.getTipoOrdine().equals("V"))) {
									ordineDaStampare.setAbbonamento(true);
									// ordineDaStampare.setAnnataAbbOrdine(eleLista.getAnnataAbbOrdine());
									ordineDaStampare
											.setAnnataAbbOrdine(eleLista
													.getAnnoAbbOrdine());
									ordineDaStampare.setDataPubblDa(eleLista
											.getDataPubblFascicoloAbbOrdine());
									ordineDaStampare.setDataPubbA(eleLista
											.getDataFineAbbOrdine());
								}
							}
							if (eleLista.getNumStandardArr() != null
									&& eleLista.getNumStandardArr().size() > 0) {
								// ordineDaStampare.set
								for (int x = 0; x < eleLista
										.getNumStandardArr().size(); x++) {
									// tck 2602 numeri standard non più
									// configurabili
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("I")) // configurazione.leggiAree("T")
									// &&
									{
										ordineDaStampare
												.setEtichetta_ISBN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("J")) {
										ordineDaStampare
												.setEtichetta_ISSN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("M")) {
										ordineDaStampare
												.setEtichetta_ISMN(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("E")) // configurazione.leggiAree("E")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMEROEDITORIALE(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("L")) // configurazione.leggiAree("N")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMERODILASTRA(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleLista.getNumStandardArr().get(x)
											.getCodice1().equals("G")) // configurazione.leggiAree("P")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NPUBBLICAZIONEGOVERNATIVA(eleLista
														.getNumStandardArr()
														.get(x).getCodice2());
									}
								}
							}

							ordineDaStampare.setTitolo(eleLista.getTitolo()
									.getDescrizione());

							// configurazione dell'isbd tck 2602
							String isbdConfigurato = "";
							String separatoreIsbd = ". - ";
							if (eleLista.getTitolo() != null
									&& eleLista.getTitolo().getCodice() != null
									&& eleLista.getTitolo().getCodice().trim()
											.length() > 0) {
								TitoloACQAreeIsbdVO isbdWithAree = dao
										.getAreeIsbdTitolo(eleLista.getTitolo()
												.getCodice());
								if (isbdWithAree != null) {
									if (configurazione.leggiAree("T")
											&& isbdWithAree != null
											&& isbdWithAree.getArea200Titolo() != null
											&& isbdWithAree.getArea200Titolo()
													.trim().length() > 0) {
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea200Titolo()
														.trim();
									}
									if (configurazione.leggiAree("E")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea205Edizione() != null
											&& isbdWithAree
													.getArea205Edizione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea205Edizione()
														.trim();
									}
									if (configurazione.leggiAree("N")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea207Numerazione() != null
											&& isbdWithAree
													.getArea207Numerazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea207Numerazione()
														.trim();
									}
									if (configurazione.leggiAree("P")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea210Pubblicazione() != null
											&& isbdWithAree
													.getArea210Pubblicazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea210Pubblicazione()
														.trim();
									}
									if (isbdWithAree != null
											&& isbdWithAree.getCollezione() != null
											&& isbdWithAree.getCollezione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree.getCollezione()
														.trim();
									}

								}

							}
							if (isbdConfigurato.trim().length() > 0) {
								ordineDaStampare.setTitolo(isbdConfigurato);
							}
							// fine configurazione dell'isbd

							if (eleStampa.isPresenzaPrezzo()) {
								ordineDaStampare.setPrezzo(eleLista
										.getPrezzoEuroOrdineStr());
							} else {
								ordineDaStampare.setPrezzo(null);
							}

							ordineDaStampare.setNote(eleLista
									.getNoteFornitore());

							// totImpSingoloBO=0.00;
							totImpSingoloBO = totImpSingoloBO
									+ eleLista.getPrezzoEuroOrdine();

							// scarto degli ordini che non hanno titolo tck 2604
							// if (eleLista.getTipoOrdine()!=null &&
							// !eleLista.getTipoOrdine().equals("R") &&
							// ordineDaStampare.getTitolo()!=null &&
							// ordineDaStampare.getTitolo().trim().length()>0)

							if ((ordineDaStampare.getTitolo() != null && ordineDaStampare
									.getTitolo().trim().length() > 0)
									|| (eleLista.getTipoOrdine() != null && eleLista
											.getTipoOrdine().equals("R"))) {
								eleStampa.getListaOrdiniDaStampare().add(
										ordineDaStampare);
							} else {
								String appoReport = eleStampa.getReportErrori();
								eleStampa.setReportErrori(appoReport
										+ "Non è stato stampato l'ordine: "
										+ ordineDaStampare.getNumOrdine()
										+ ", per dati mancanti" + "\n");
							}

							if (i == (listaOggetti.size() - 1)) {
								DecimalFormatSymbols dfs2 = new DecimalFormatSymbols();
								dfs2.setGroupingSeparator('.');
								dfs2.setDecimalSeparator(',');
								// controllo formattazione con virgola
								// separatore dei decimali
								DecimalFormat df2 = new DecimalFormat(
										"#,##0.00", dfs2);
								// importo
								String stringa2 = df2.format(totImpSingoloBO);
								NumberFormat formatIT2 = NumberFormat
										.getCurrencyInstance();
								String strCurrency2 = "\u20AC ";
								strCurrency2 = strCurrency2 + stringa2;
								// Number imp=formatIT.parse(strCurrency); // va
								// in errore se non è riconosciuto come formato
								// euro
								String totImpSingoloBOStr2 = formatIT2
										.format(totImpSingoloBO);
								totImpSingoloBOStr2 = totImpSingoloBOStr2
										.substring(2); // elimina il simbolo
								eleStampa
										.setImportoFornitura(totImpSingoloBOStr2);
								output.add(eleStampa);
							}

						}

					}// fine for
					if (output != null && output.size() > 0) {
						output.get(0).setListaOggDaStampare(listaOggetti); // memorizzo
						// solo
						// nel
						// primo
						// tutti
						// gli
						// id
						// da
						// stampare
						// per
						// cambiargli
						// stato
					}
				}
				// oggetti di tipo BUONI ORDINE
				if (tipoOggetti != null && tipoOggetti.trim().length() > 0
						&& tipoOggetti.trim().equals("BUO")) {
					BuoniOrdineVO eleLista = new BuoniOrdineVO();
					StampaBuoniVO eleStampa = new StampaBuoniVO();

					// riempire eleStampa con i dati univoci
					// eleStampa.
					// .....
					eleStampa.setCodBibl(configurazione.getCodBibl());
					eleStampa.setCodPolo(configurazione.getCodPolo());
					eleStampa.setDenoBib(denoBibl);

					eleStampa.setTicket(ticket);
					eleStampa.setUtente(utente);
					eleStampa.setPresenzaLogoImg(configurazione
							.isPresenzaLogoImg());
					eleStampa.setNomeLogoImg(StampeUtil.getBatchFilesPath()
							+ File.separator + configurazione.getNomeLogoImg());
					// eleStampa.setNomeLogoImg(configurazione.getNomeLogoImg());
					// // path fisico
					// eleStampa.setNomeLogoImg("C:"+ File.separator +
					// "bdi_l.jpg"); // aggiunto per test
					String lingua = "";
					if (configurazione.isLinguaIT()) {
						lingua = "ITA";
						List<String> intest = new ArrayList<String>();
						String intestStr = "";
						for (int j = 0; configurazione
								.getListaDatiIntestazione() != null
								&& j < configurazione
										.getListaDatiIntestazione().length; j++) {
							if (j == 0) {
								eleStampa.setDenoBib(configurazione
										.getListaDatiIntestazione()[0]
										.getCodice2());
							}
							if (j > 0) {
								intest.add(configurazione
										.getListaDatiIntestazione()[j]
										.getCodice2());
								if (j > 1) {
									intestStr = intestStr + "\n"; // salto riga
								}
								intestStr = intestStr
										+ configurazione
												.getListaDatiIntestazione()[j]
												.getCodice2();
							}

						}
						eleStampa.setListaDatiIntestazione(intestStr);
					} else {
						lingua = "ENG";
						List<String> intest = new ArrayList<String>();
						String intestStr = "";
						for (int j = 0; configurazione
								.getListaDatiIntestazioneEng() != null
								&& j < configurazione
										.getListaDatiIntestazioneEng().length; j++) {
							if (j == 0) {
								eleStampa.setDenoBib(configurazione
										.getListaDatiIntestazioneEng()[0]
										.getCodice2());
							}
							if (j > 0) {
								intest.add(configurazione
										.getListaDatiIntestazioneEng()[j]
										.getCodice2());
								if (j > 1) {
									intestStr = intestStr + "\n"; // salto riga
								}
								intestStr = intestStr
										+ configurazione
												.getListaDatiIntestazioneEng()[j]
												.getCodice2();
							}
						}
						eleStampa.setListaDatiIntestazione(intestStr);
					}
					eleStampa.setEtichettaProtocollo(configurazione
							.isEtichettaProtocollo());
					eleStampa.setEtichettaDataProtocollo(configurazione
							.isEtichettaDataProtocollo());

					eleStampa.setNumProtocollo("");

					eleStampa.setPresenzaPrezzo(configurazione
							.isPresenzaPrezzo());

					if (configurazione.isLinguaIT()) {
						List<String> fineStp = new ArrayList<String>();
						String fineStpStr = "";

						for (int j = 0; configurazione.getListaDatiFineStampa() != null
								&& j < configurazione.getListaDatiFineStampa().length; j++) {
							fineStp
									.add(configurazione
											.getListaDatiFineStampa()[j]
											.getCodice2());
							if (j > 0) {
								fineStpStr = fineStpStr + "\n"; // salto riga
							}
							fineStpStr = fineStpStr
									+ configurazione.getListaDatiFineStampa()[j]
											.getCodice2();
						}
						eleStampa.setListaDatiFineStampa(fineStpStr);
					} else {
						List<String> fineStp = new ArrayList<String>();
						String fineStpStr = "";
						for (int j = 0; configurazione
								.getListaDatiFineStampaEng() != null
								&& j < configurazione
										.getListaDatiFineStampaEng().length; j++) {
							fineStp.add(configurazione
									.getListaDatiFineStampaEng()[j]
									.getCodice2());
							if (j > 0) {
								fineStpStr = fineStpStr + "\n"; // salto riga
							}
							fineStpStr = fineStpStr
									+ configurazione
											.getListaDatiFineStampaEng()[j]
											.getCodice2();
						}
						eleStampa.setListaDatiFineStampa(fineStpStr);
					}
					eleStampa.setPresenzaFirmaImg(configurazione
							.isPresenzaFirmaImg());
					// eleStampa.setNomeFirmaImg(configurazione.getNomeFirmaImg());
					eleStampa
							.setNomeFirmaImg(StampeUtil.getBatchFilesPath()
									+ File.separator
									+ configurazione.getNomeFirmaImg());
					eleStampa.setIndicaRistampa(configurazione
							.isIndicaRistampa());
					OrdiniVO eleListaOrd = new OrdiniVO();
					double totImpSingoloBO = 0.00;
					RigheOrdiniStampaBuoniVO ordineDaStampare = new RigheOrdiniStampaBuoniVO();
					for (int i = 0; listaOggetti != null
							&& i < listaOggetti.size(); i++) {
						eleLista = ((BuoniOrdineVO) listaOggetti.get(i));
						// se il buono d'ordine è già stampato allora siamo in
						// ristampa
						ristampa = false;
						if (eleLista.getStatoBuonoOrdine() != null
								&& eleLista.getStatoBuonoOrdine().equals("S")) {
							ristampa = true;
						}
						eleStampa.setNumBuonoOrdine(eleLista
								.getNumBuonoOrdine());
						eleStampa
								.setAnagFornitore(((BuoniOrdineVO) listaOggetti
										.get(0)).getAnagFornitore());

						for (int j = 0; eleLista.getListaOrdiniBuono() != null
								&& j < eleLista.getListaOrdiniBuono().size(); j++) {
							eleListaOrd = (eleLista
									.getListaOrdiniBuono().get(j));
							if (j == 0) {
								eleStampa.setTestoOggetto(configurazione
										.leggiOgg(lingua, eleListaOrd
												.getTipoOrdine()));
								eleStampa.setTestoIntroduttivo(configurazione
										.leggiIntro(lingua, eleListaOrd
												.getTipoOrdine(), eleListaOrd
												.getCd_tipo_lav()));
							}
							totImpSingoloBO = totImpSingoloBO
									+ eleListaOrd.getPrezzoEuroOrdine();
							// riempire eleStampa con i dati molteplici

							// riempire l'elemento di riga di eleStampa ad es.
							// ordineDaStampare.set
							// ordineDaStampare.set;
							// .....
							ordineDaStampare = new RigheOrdiniStampaBuoniVO();
							ordineDaStampare.setNumOrdine(eleListaOrd
									.getTipoOrdine()
									+ " "
									+ eleListaOrd.getAnnoOrdine()
									+ " "
									+ eleListaOrd.getCodOrdine());
							ordineDaStampare.setTipoOrdine(eleListaOrd
									.getTipoOrdine());
							if (eleListaOrd.getTipoOrdine() != null
									&& eleListaOrd.getTipoOrdine().equals("R")
									&& eleListaOrd
											.getRigheInventariRilegatura() != null
									&& eleListaOrd
											.getRigheInventariRilegatura()
											.size() > 0) {
								ordineDaStampare
										.setRigheInventariRilegatura(eleListaOrd
												.getRigheInventariRilegatura());
							}

							ordineDaStampare.setRinnovato(eleListaOrd
									.isRinnovato());
							ordineDaStampare.setRinnovoOrigine(eleListaOrd
									.getRinnovoOrigine().getCodice1()
									+ " "
									+ eleListaOrd.getRinnovoOrigine()
											.getCodice2()
									+ " "
									+ eleListaOrd.getRinnovoOrigine()
											.getCodice3());
							ordineDaStampare.setAbbonamento(false);
							if (eleListaOrd.isContinuativo()
									&& eleListaOrd.getNaturaOrdine() != null
									&& eleListaOrd.getNaturaOrdine()
											.equals("S")
									&& eleListaOrd.getStatoOrdine() != null
									&& eleListaOrd.getStatoOrdine().equals("A")) {

								if (eleListaOrd.getTipoOrdine() != null
										&& (eleListaOrd.getTipoOrdine().equals(
												"A") || eleListaOrd
												.getTipoOrdine().equals("V"))) {
									ordineDaStampare.setAbbonamento(true);
									// ordineDaStampare.setAnnataAbbOrdine(eleListaOrd.getAnnataAbbOrdine());
									ordineDaStampare
											.setAnnataAbbOrdine(eleListaOrd
													.getAnnoAbbOrdine());
									ordineDaStampare.setDataPubblDa(eleListaOrd
											.getDataPubblFascicoloAbbOrdine());
									ordineDaStampare.setDataPubbA(eleListaOrd
											.getDataFineAbbOrdine());
								}
							}
							// gestione prima stampa: si lascia vuoto il campo
							// data di stampa perchè provvede jasper che in caso
							// di assenza attribuisce la data odierna della
							// stampa fisica
							// gestione ristampa: si assume come data di stampa
							// quella memorizzata come data del buono (piuttosto
							// che degli ordini associati IDEM) che viene
							// aggiornata con la prima stampa
							if (ristampa) {
								eleStampa.setDataStampa(eleLista
										.getDataBuonoOrdine());
							}

							if (eleStampa.isIndicaRistampa()
									&& eleLista.getStatoBuonoOrdine().equals(
											"S")) // && eleListaOrd.isStampato()
							{
								eleStampa.setEtichettaRistampa("(ristampa)");
							}

							if (eleListaOrd.getNumStandardArr() != null
									&& eleListaOrd.getNumStandardArr().size() > 0) {
								// ordineDaStampare.set
								for (int x = 0; x < eleListaOrd
										.getNumStandardArr().size(); x++) {
									// tck 2602 numeri standard non più
									// configurabili
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("I")) // configurazione.leggiAree("T")
									// &&
									{
										ordineDaStampare
												.setEtichetta_ISBN(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("J")) {
										ordineDaStampare
												.setEtichetta_ISSN(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("M")) {
										ordineDaStampare
												.setEtichetta_ISMN(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("E")) // configurazione.leggiAree("E")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMEROEDITORIALE(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("L")) // configurazione.leggiAree("N")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NUMERODILASTRA(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
									if (eleListaOrd.getNumStandardArr().get(x)
											.getCodice1().equals("G")) // configurazione.leggiAree("P")
									// &&
									{
										ordineDaStampare
												.setEtichetta_NPUBBLICAZIONEGOVERNATIVA(eleListaOrd
														.getNumStandardArr()
														.get(x).getCodice2());
									}
								}
							}

							ordineDaStampare.setTitolo(eleListaOrd.getTitolo()
									.getDescrizione());

							// configurazione dell'isbd tck 2602
							String isbdConfigurato = "";
							String separatoreIsbd = ". - ";
							if (eleListaOrd.getTitolo() != null
									&& eleListaOrd.getTitolo().getCodice() != null
									&& eleListaOrd.getTitolo().getCodice()
											.trim().length() > 0) {
								TitoloACQAreeIsbdVO isbdWithAree = null;
								try {
									isbdWithAree = dao
											.getAreeIsbdTitolo(eleListaOrd
													.getTitolo().getCodice());
								} catch (Exception e) {

									log.error("", e);
								}

								if (isbdWithAree != null) {
									if (configurazione.leggiAree("T")
											&& isbdWithAree != null
											&& isbdWithAree.getArea200Titolo() != null
											&& isbdWithAree.getArea200Titolo()
													.trim().length() > 0) {
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea200Titolo()
														.trim();
									}
									if (configurazione.leggiAree("E")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea205Edizione() != null
											&& isbdWithAree
													.getArea205Edizione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea205Edizione()
														.trim();
									}
									if (configurazione.leggiAree("N")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea207Numerazione() != null
											&& isbdWithAree
													.getArea207Numerazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea207Numerazione()
														.trim();
									}
									if (configurazione.leggiAree("P")
											&& isbdWithAree != null
											&& isbdWithAree
													.getArea210Pubblicazione() != null
											&& isbdWithAree
													.getArea210Pubblicazione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree
														.getArea210Pubblicazione()
														.trim();
									}
									if (isbdWithAree != null
											&& isbdWithAree.getCollezione() != null
											&& isbdWithAree.getCollezione()
													.trim().length() > 0) {
										if (!isbdConfigurato.equals("")) {
											isbdConfigurato = isbdConfigurato
													+ separatoreIsbd;
										}
										isbdConfigurato = isbdConfigurato
												+ isbdWithAree.getCollezione()
														.trim();
									}

								}

							}
							if (isbdConfigurato.trim().length() > 0) {
								ordineDaStampare.setTitolo(isbdConfigurato);
							}
							// fine configurazione dell'isbd

							if (eleStampa.isPresenzaPrezzo()) {
								ordineDaStampare.setPrezzo(eleListaOrd
										.getPrezzoEuroOrdineStr());
							} else {
								ordineDaStampare.setPrezzo(null);
							}

							ordineDaStampare.setNote(eleListaOrd
									.getNoteFornitore());

							// scarto degli ordini che non hanno titolo (non non
							// sono di rilegatura) tck 2604
							// if (eleListaOrd.getTipoOrdine()!=null &&
							// !eleListaOrd.getTipoOrdine().equals("R") &&
							// ordineDaStampare.getTitolo()!=null &&
							// ordineDaStampare.getTitolo().trim().length()>0)
							if ((ordineDaStampare.getTitolo() != null && ordineDaStampare
									.getTitolo().trim().length() > 0)
									|| (eleListaOrd.getTipoOrdine() != null && eleListaOrd
											.getTipoOrdine().equals("R"))) {
								eleStampa.getListaOrdiniDaStampare().add(
										ordineDaStampare);
							} else {
								String appoReport = eleStampa.getReportErrori();
								eleStampa.setReportErrori(appoReport
										+ "Non è stato stampato l'ordine: "
										+ ordineDaStampare.getNumOrdine()
										+ ", per dati mancanti" + "\n");
							}

						}
						DecimalFormatSymbols dfs = new DecimalFormatSymbols();
						dfs.setGroupingSeparator('.');
						dfs.setDecimalSeparator(',');
						// controllo formattazione con virgola separatore dei
						// decimali
						DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
						// importo
						String stringa = df.format(totImpSingoloBO);
						NumberFormat formatIT = NumberFormat
								.getCurrencyInstance();
						String strCurrency = "\u20AC ";
						strCurrency = strCurrency + stringa;
						// Number imp=formatIT.parse(strCurrency); // va in
						// errore se non è riconosciuto come formato euro
						String totImpSingoloBOStr = formatIT
								.format(totImpSingoloBO);
						totImpSingoloBOStr = totImpSingoloBOStr.substring(2); // elimina
						// il
						// simbolo
						eleStampa.setImportoFornitura(totImpSingoloBOStr);

						output.add(eleStampa);
					}
					if (output != null && output.size() > 0) {
						output.get(0).setListaOggDaStampare(listaOggetti); // memorizzo
						// solo
						// nel
						// primo
						// tutti
						// gli
						// id
						// da
						// stampare
						// per
						// cambiargli
						// stato
					}
				}

			}
		} catch (Exception e) {

			log.error("", e);

		}
		return output;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List picosPeriodici(String kbibl, String kordi, String kanno)
			throws ResourceNotFoundException, ValidationException,
			ApplicationException {
		Picos picos = new Picos();
		return picos.picosPeriodici(kbibl, kordi, kanno);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public List picosPeriodiciInv(String kbibl, String kinv,
			String kserie) throws ResourceNotFoundException,
			ValidationException, ApplicationException {
		Picos picos = new Picos();
		return picos.picosPeriodiciInv(kbibl, kinv, kserie);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @param ticket
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public java.util.List picosPeriodiciAskInv(String kbibl,
			String kordi, String kanno, String tipomat, String tipocirc,
			String precis, String valore, Boolean flacoll, String consisDoc,
			int annoBil, String tipoPrezzo, String prezzoBil, String kserie, String ticket)
			throws ResourceNotFoundException, ValidationException,
			ApplicationException {

		List results = null;
		try {
			Picos picos = new Picos();
			results = picos.picosPeriodiciAskInv(kbibl, kordi, kanno, tipomat,
					tipocirc, precis, valore, flacoll, consisDoc, annoBil,
					tipoPrezzo, prezzoBil, kserie, ticket);
		} catch (DataException e) {

			log.error("", e);
		} catch (ApplicationException e) {

			log.error("", e);
		} catch (ValidationException e) {

			// log.error("", e);
			throw e;
		} catch (Exception e) {

			// throw e;
			log.error("", e);
		}
		return results;
		// return dao.elaboraPeriodiciAskInv( kbibl, kordi, kanno, tipomat,
		// tipocirc, precis, valore, flacoll, consisDoc, annoBil, tipoPrezzo,
		// prezzoBil) ;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public int ValidaPreRicercaOrdini(ListaSuppOrdiniVO ricercaOrdini)
			throws ValidationException {
		return Validazione.ValidaPreRicercaOrdini(dao, ricercaOrdini);

	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *            //TODO: Must provide implementation for bean create stub
	 */
	public void migrazioneCStoSBNWEBcateneRinnoviBis()
			throws ValidationException {
		try {
			dao.migrazioneCStoSBNWEBcateneRinnoviBis();
		} catch (DataException e) {

			log.error("", e);
		} catch (ApplicationException e) {

			log.error("", e);
		} catch (ValidationException e) {

			// log.error("", e);
			throw e;
		} catch (Exception e) {

			log.error("", e);
		}
	}

	public CommandResultVO invoke(CommandInvokeVO command)
			throws ValidationException, ApplicationException {
		if (command == null)
			throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

		command.validate();

		String ticket = command.getTicket();
		checkTicket(ticket);
		AcquisizioniInvokeHandler handler = new AcquisizioniInvokeHandler(this,	dao);

		return handler.invoke(command);
	}

	public FornitoreVO getFornitore(String codPolo, String codBib,
			String codFornitore, String descrForn, String ticket)
			throws DataException, ApplicationException, ValidationException,
			it.iccu.sbn.ejb.exception.DataException {
		FornitoreVO fornVO = null;
		Tbr_fornitori fornDB = null;
		try {
			FornitoreVO recVal = new FornitoreVO();
			recVal.setCodPolo(codPolo);
			recVal.setCodBibl(codBib);
			if (codFornitore != null) {
				recVal.setCodFornitore(codFornitore.trim());
			}
			if (descrForn != null) {
				recVal.setNomeFornitore(descrForn.trim());
			}
			// this.ValidaFornitoreVO(recVal);
			daoFornitore = new Tba_fornitoriDao();
			if (codFornitore != null) {
				if (descrForn != null) {
					fornDB = daoFornitore.getFornitore(codPolo, codBib,
							codFornitore.trim(), descrForn.trim());
				} else {
					fornDB = daoFornitore.getFornitore(codPolo, codBib,
							codFornitore.trim(), null);
				}
			} else {
				if (descrForn != null) {
					fornDB = daoFornitore.getFornitore(codPolo, codBib, null,
							descrForn.trim());
				} else {
					throw new it.iccu.sbn.ejb.exception.DataException(
							"mancanoEstremiPerRicercaFornitore");
				}
			}
			if (fornDB != null) {
				fornVO = new FornitoreVO();
				fornVO.setCodFornitore(String
						.valueOf(fornDB.getCod_fornitore()));
				fornVO.setNomeFornitore(fornDB.getNom_fornitore().trim());
				if (fornDB.getRegione() != null) {
					fornVO.setRegione(fornDB.getRegione().trim());
				} else {
					fornVO.setRegione("");
				}
				fornVO.setIsbnEditore("");
			} else {
				throw new it.iccu.sbn.ejb.exception.DataException(
						"recSezioneInesistente");
			}
			// }catch (ValidationException e) {
			// throw e;
		} catch (DataException e) {
			log.error("", e);
			throw e;
		} catch (DaoManagerException e) {
			throw new it.iccu.sbn.ejb.exception.DataException(e);
		} catch (Exception e) {

			log.error("", e);
			throw new it.iccu.sbn.ejb.exception.DataException(e);
		}
		return fornVO;
	}

	public int countRigheFatturaOrdine(OrdiniVO ordine) throws SbnBaseException {
		Tba_fattureDao dao = new Tba_fattureDao();
		try {
			if (ordine != null) {
				log.debug(String.format("controllo righe fattura per ordine '%s'", ordine.getChiaveOrdine()) );
				return dao.countRigheFatturaOrdine(ordine.getIDOrd());
			}

			return 0;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		}
	}

}
