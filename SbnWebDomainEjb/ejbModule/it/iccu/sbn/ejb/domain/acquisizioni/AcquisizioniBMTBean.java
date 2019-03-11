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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.acquisizioni.OrdiniUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.AdeguamentoCalcoliVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DatiFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SchedaInventarioAcqVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SchedaInventarioInputVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_fattureDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.amministrazione.ContatoriDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_righe_fatture;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.vo.validators.acquisizioni.Validazione;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;


/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="ServiziDocumentoFisico" description="A session bean named
 *           ServiziDocumentoFisico" display-name="ServiziDocumentoFisico"
 *           jndi-name="sbnWeb/ServiziDocumentoFisico" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class AcquisizioniBMTBean extends TicketChecker implements AcquisizioniBMT {


	private static final long serialVersionUID = 7915309276263638347L;

	private static Logger log = Logger.getLogger(AcquisizioniBMT.class);

	private GenericJDBCAcquisizioniDAO dao;
	private SessionContext ctx;

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 */
	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext context) throws EJBException,
	RemoteException {
		this.ctx = context;
		dao = new GenericJDBCAcquisizioniDAO();
	}

	public  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
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
     * //
     */
	public SchedaInventarioVO picosRinnovoOrdine(OperazioneSuOrdiniMassivaVO richiesta, SchedaInventarioInputVO passaggioDati, String ticket) throws DataException, ApplicationException , ValidationException
	{
		SchedaInventarioVO schedaInv = new SchedaInventarioVO();
		UserTransaction tx = ctx.getUserTransaction();
		if (passaggioDati != null)
			try {
				richiesta.setPassaggioDati(passaggioDati);
				DaoManager.begin(tx);
				richiesta = this.gestioneDifferitaOperazioniSuOrdineMassiva(richiesta);
				ClonePool.copyCommonProperties(schedaInv, richiesta.getScheda());
				DaoManager.commit(tx);

			} catch (Exception e) {
				DaoManager.rollback(tx);
				throw new ValidationException(e);
			}


		return schedaInv;
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
     *
     */
	public OperazioneSuOrdiniMassivaVO gestioneDifferitaOperazioniSuOrdineMassiva(OperazioneSuOrdiniMassivaVO richiesta) throws DataException, ApplicationException , ValidationException
	{
		UserTransaction tx = ctx.getUserTransaction();

		OperazioneSuOrdiniMassivaVO output = null;
		boolean ok = false;
		try {
			output = richiesta.copy();
			// lettura globale
			int numRecDaElabor = 0;

			String tipoOperazione = richiesta.getTipoOperazione();
			if (ValidazioneDati.in(tipoOperazione, "A", "C", "R") )
			{
				List<OrdiniVO> ordini = null;

				ListaSuppOrdiniVO ricercaOrdini = new ListaSuppOrdiniVO();
				ricercaOrdini.setCodBibl(richiesta.getCodBib());
				ricercaOrdini.setCodPolo(richiesta.getCodPolo());
				ricercaOrdini.setIdOrdList(richiesta.getDatiInput());
				ricercaOrdini.setOrdinamento("");
				ricercaOrdini.setChiamante("");
				ricercaOrdini.setElemXBlocchi(0);


				try {
					ordini = dao.getRicercaListaOrdini(ricercaOrdini);

				} catch (Exception e) {
					log.error("", e);
				}
				int size = ValidazioneDati.size(ordini);
				numRecDaElabor = size;

			}
			if (tipoOperazione.equals("S")  ) {
				List<DocumentoVO> ordInEsamelista=null;

				ListaSuppDocumentoVO eleRicerca=new ListaSuppDocumentoVO();
				eleRicerca.setCodBibl(richiesta.getCodBib());
				eleRicerca.setCodPolo(richiesta.getCodPolo());
				eleRicerca.setTipoDocLet("S");
				eleRicerca.setIdDocList(richiesta.getDatiInput());
				eleRicerca.setOrdinamento("");
				eleRicerca.setChiamante("");
				eleRicerca.setElemXBlocchi(0);

				try {
					ordInEsamelista=dao.getRicercaListaDocumenti(eleRicerca);

				} catch (Exception e) {
					log.error("", e);
				}
				if (ordInEsamelista!=null && ordInEsamelista.size()>0)
				{
					numRecDaElabor=ordInEsamelista.size();
				}
			}

			if (ValidazioneDati.in(tipoOperazione, "V", "F")  )	{
				List<SuggerimentoVO> ordInEsamelista=null;

				ListaSuppSuggerimentoVO eleRicerca=new ListaSuppSuggerimentoVO();
				eleRicerca.setCodBibl(richiesta.getCodBib());
				eleRicerca.setCodPolo(richiesta.getCodPolo());
				//eleRicerca.setTipoDocLet("S");
				eleRicerca.setIdSugList(richiesta.getDatiInput());
				eleRicerca.setOrdinamento("");
				eleRicerca.setChiamante("");
				eleRicerca.setElemXBlocchi(0);

				try {
					ordInEsamelista=dao.getRicercaListaSuggerimenti(eleRicerca);

				} catch (Exception e) {
					log.error("", e);
				}
				if (ordInEsamelista!=null && ordInEsamelista.size()>0)
				{
					numRecDaElabor=ordInEsamelista.size();
				}
			}


			List<String> errori = output.getErrori();
			if (tipoOperazione.equals("A"))
			{
				// annullamento
				errori.add("OPERAZIONE DI ANNULLAMENTO");
			}
			else if (tipoOperazione.equals("C"))
			{
				// chiusura
				errori.add("OPERAZIONE DI CHIUSURA");
				errori.add("Adeguamento del prezzo previsto al prezzo reale: "
								+ (richiesta.isAdeguamentoPrezzo() ? "Sì" : "No"));
			}
			else if (tipoOperazione.equals("R"))
			{
				// rinnovo
				errori.add("OPERAZIONE DI RINNOVO");
			}
			else if (tipoOperazione.equals("S"))
			{
				// rifiuto suggerimenti
				errori.add("RIFIUTO SUGGERIMENTI LETTORE");
			}
			else if (tipoOperazione.equals("V"))
			{
				// accetta suggerimenti
				errori.add("ACCETTA SUGGERIMENTI BIBLIOTECARIO");
			}
			else if (tipoOperazione.equals("F"))
			{
				// rifiuta suggerimenti bibliotecario
				errori.add("RIFIUTA SUGGERIMENTI BIBLIOTECARIO");
			}

			//if (operaz.getDatiInput()!=null && operaz.getDatiInput().size()>0)
			if (numRecDaElabor>0)
			{
				errori.add("Numero record da elaborare = " + numRecDaElabor);
			}
			else
			{
				errori.add("ERRORE: assenza di record da elaborare");
			}


			if (tipoOperazione.equals("A")) {

				// annullamento
				//output.getErrori().add("OPERAZIONE DI ANNULLAMENTO");
				for (int i = 0; i < richiesta.getDatiInput().size(); i++) {

					//UserTransaction ut = session.getUserTransaction();
					//ut.begin();

/*					try {
						output=dao.gestioneDifferitaOperazioniSuOrdineMassivaFase1( operaz,  output, i );
						//ut.commit();

					} catch (Exception e) {
						log.error("", e);
						//ut.rollback();

					}
*/
// commentato il 22.10.08
/*
 					Boolean procedi=false;
					//Lettura singola
					ListaSuppOrdiniVO ricercaOrdiniSingle=new ListaSuppOrdiniVO();
					ricercaOrdiniSingle.setCodBibl(operaz.getCodBib());
					ricercaOrdiniSingle.setCodPolo(operaz.getCodPolo());
					ricercaOrdiniSingle.setIDOrd(operaz.getDatiInput().get(i));
					ricercaOrdiniSingle.setOrdinamento("");
					ricercaOrdiniSingle.setChiamante("");
					ricercaOrdiniSingle.setElemXBlocchi(0);
					List<OrdiniVO> ordSingle=null;
					try {
						ordSingle=dao.getRicercaListaOrdini(ricercaOrdiniSingle);
					} catch (Exception e) {
						log.error("", e);
					}
					// test esistenza di un unico risultato
					if (ordSingle.size()==1)
					{
						// controllo stato
						if (ordSingle.get(0).getStatoOrdine().equals("C"))
						{
							output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " Ordine già chiuso");
							//break;
						}
						else if (ordSingle.get(0).getStatoOrdine().equals("N"))
						{
							output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " Ordine già annullato");
							//break;

						}
						else
						{
							if (!ordSingle.get(0).getTipoOrdine().equals("R")) //non ordine di rilegatura
							{
								// controllo esistenza inventari associati
						          List listaInv=null;

						          String codPolo=ordSingle.get(0).getCodPolo();
						          String codBib=ordSingle.get(0).getCodBibl();
						          String codBibO=ordSingle.get(0).getCodBibl();
						          String codTipOrd=ordSingle.get(0).getTipoOrdine();
						          String codBibF="";
						          String ticket=operaz.getTicket(); //  VA MESSO OBBLIGATORIAMENTE ?????
						          int   annoOrd=Integer.valueOf(ordSingle.get(0).getAnnoOrdine());
						          int   codOrd=Integer.valueOf(ordSingle.get(0).getCodOrdine());
						          int   nRec=999;
						          Locale loc=Locale.getDefault();
								  try {
									  listaInv= inventario.getListaInventariOrdine(codPolo,  codBib,  codBibO, codTipOrd,  annoOrd,  codOrd,  codBibF, loc, ticket,  nRec);
									} catch (Exception e) {
										log.error("", e);
									}

						          //  esistono inventari assoc
						          if (listaInv!=null && listaInv.size()>0)
						          {
										output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " annullamento impossibile perchè esistono inventari associati");
										//break;
						          }
						          else
						          {
						        	  procedi=true;
						          }


							}
							// modifica dello stato
							if (procedi)
							{
								ordSingle.get(0).setStatoOrdine("N");
								ordSingle.get(0).setUtente(operaz.getUser());
								try {
									Boolean risultato=dao.modificaOrdine(ordSingle.get(0));
									if (risultato)
									{
										output.getErrori().add("OK: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " Ordine annullato");

									}
									else
									{
										output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " annullamento impossibile perchè la modifica dello stato non è andata a buon fine");
										//break;

									}

								} catch (Exception e) {
									log.error("", e);
									output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " annullamento impossibile perchè si è verificata una eccezione");
									//break;

								}
							}

						}
					}
*///fine  commento il 22.10.08
					Boolean procedi = false;
					// Lettura singola
					ListaSuppOrdiniVO ricercaOrdiniSingle = new ListaSuppOrdiniVO();
					ricercaOrdiniSingle.setCodBibl(richiesta.getCodBib());
					ricercaOrdiniSingle.setCodPolo(richiesta.getCodPolo());
					ricercaOrdiniSingle.setIDOrd(richiesta.getDatiInput().get(i));
					ricercaOrdiniSingle.setOrdinamento("");
					ricercaOrdiniSingle.setChiamante("");
					ricercaOrdiniSingle.setElemXBlocchi(0);
					List<OrdiniVO> ordSingle = null;
					try {
						ordSingle=dao.getRicercaListaOrdini(ricercaOrdiniSingle);
					} catch (Exception e) {
						log.error("", e);
					}
					// test esistenza di un unico risultato
					if (ordSingle.size()==1)
					{
						// controllo stato
						OrdiniVO o = ordSingle.get(0);
						if (o.getStatoOrdine().equals("C"))
						{
							errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine gi\u00E0 chiuso");
							//break;
						}
						else if (o.getStatoOrdine().equals("N"))
						{
							errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine gi\u00E0 annullato");
							//break;

						}
						else
						{
							if (!o.getTipoOrdine().equals("R")) //non ordine di rilegatura
							{
								// controllo esistenza inventari associati

						          List<?> listaInv = null;

						          String codPolo=o.getCodPolo();
						          String codBib=o.getCodBibl();
						          String codBibO=o.getCodBibl();
						          String codTipOrd=o.getTipoOrdine();
						          String codBibF="";
						          String ticket=richiesta.getTicket(); //  VA MESSO OBBLIGATORIAMENTE ?????
						          int   annoOrd=Integer.valueOf(o.getAnnoOrdine());
						          int   codOrd=Integer.valueOf(o.getCodOrdine());
						          int   nRec=999;
						          Locale loc=Locale.getDefault();
								  try {
									listaInv = DomainEJBFactory.getInstance().getInventario()
											.getListaInventariOrdine(codPolo,
													codBib, codBibO, codTipOrd,
													annoOrd, codOrd, codBibF,
													loc, ticket, nRec);
									} catch (Exception e) {
										log.error("", e);
									}

						          //  esistono inventari assoc
						          if (listaInv!=null && listaInv.size()>0)
						          {
										errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " annullamento impossibile perch\u00E8 esistono inventari associati");
										//break;
						          }
						          else
						          {
						        	  procedi=true;
						          }

/*						          if (ordSingle.get(0).getEsistenzaInventariLegati()!=null && ordSingle.get(0).getEsistenzaInventariLegati().equals("1"))
						          {
										output.getErrori().add("ERRORE: " + ordSingle.get(0).getAnnoOrdine() + " - "  + ordSingle.get(0).getTipoOrdine() + " - " +  ordSingle.get(0).getCodOrdine() + " annullamento impossibile perchè esistono inventari associati");
										//break;
						          }
						          else
						          {
						        	  procedi=true;
						          }
*/
						          int righeFatturaOrdine = DomainEJBFactory.getInstance().getAcquisizioni().countRigheFatturaOrdine(o);
						          if (righeFatturaOrdine > 0) {
						        	  errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " annullamento impossibile perch\u00E8 esistono righe di fattura associate");
						        	  procedi = false;
						          }
							}
							// modifica dello stato
							if (procedi)
							{
								DaoManager.begin(tx);
								//log.debug("Inizio transazione");
								//this.scriviLog(operaz, output, numRecSpostati);
								o.setStatoOrdine("N");
								o.setUtente(DaoManager.getFirmaUtente(richiesta.getTicket()));
								try {
									Boolean risultato=dao.modificaOrdine(o);
									if (risultato)
									{
										errori.add("OK: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine annullato");
										DaoManager.commit(tx);

									}
									else
									{
										errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " annullamento impossibile perch\u00E8 la modifica dello stato non \u00E8 andata a buon fine");
										//break;
										DaoManager.rollback(tx);

									}

								} catch (Exception e) {
									log.error("", e);
									errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " annullamento impossibile perch\u00E8 si \u00E8 verificata una eccezione");
									//break;
									DaoManager.rollback(tx);

								}
							}

						}
					}

				}
			}
			else if (tipoOperazione.equals("C")) {
				chiusuraOrdini(richiesta, tx, errori);

			}
			else if (tipoOperazione.equals("R")) {
				rinnovoOrdini(richiesta, tx, output, errori);

			}
			else if (tipoOperazione.equals("S"))
			{
				// operazione di rifiuto suggerimenti
				for (int i=0; i<richiesta.getDatiInput().size(); i++)
				{
					Boolean procedi=false;
					//Lettura singola

					ListaSuppDocumentoVO eleRicercaSingle=new ListaSuppDocumentoVO();
					eleRicercaSingle.setCodBibl(richiesta.getCodBib());
					eleRicercaSingle.setCodPolo(richiesta.getCodPolo());
					eleRicercaSingle.setTipoDocLet("S");

					List<Integer> eleSingle=new ArrayList<Integer>();
					eleSingle.add(richiesta.getDatiInput().get(i));

					eleRicercaSingle.setIdDocList(eleSingle);
					eleRicercaSingle.setOrdinamento("");
					eleRicercaSingle.setChiamante("");
					eleRicercaSingle.setElemXBlocchi(0);

					List<DocumentoVO> elementoSingle=null;
					try {
						elementoSingle=dao.getRicercaListaDocumenti(eleRicercaSingle);
					} catch (Exception e) {
						log.error("", e);
					}
					// test esistenza di un unico risultato
					if (elementoSingle.size()==1)
					{
						// controllo stato
						if (elementoSingle.get(0).getStatoSuggerimentoDocumento().equals("A")) // accettato
						{
							errori.add("ERRORE: " + elementoSingle.get(0).getCodDocumento() + " Suggerimento gi\u00E0 accettato");
							//break;
						}
						if (elementoSingle.get(0).getStatoSuggerimentoDocumento().equals("R")) // rifiutato
						{
							errori.add("ERRORE: " + elementoSingle.get(0).getCodDocumento() + " Suggerimento gi\u00E0 rifiutato");
							//break;
						}
						if (elementoSingle.get(0).getStatoSuggerimentoDocumento().equals("O")) // ordinato
						{
							errori.add("ERRORE: " + elementoSingle.get(0).getCodDocumento() + " Suggerimento gi\u00E0 ordinato");
							//break;
						}
						if (elementoSingle.get(0).getStatoSuggerimentoDocumento().equals("W")) // in attesa di risposta
						{
							procedi=true;
						}

						// modifica dello stato
						if (procedi)
							{
							DaoManager.begin(tx);
							elementoSingle.get(0).setUtenteCod(richiesta.getUser());
							elementoSingle.get(0).setStatoSuggerimentoDocumento("R");
							try {
								Boolean risultato=dao.modificaDocumento(elementoSingle.get(0));
								if (risultato)
								{
									errori.add("OK: " + elementoSingle.get(0).getCodDocumento() + " Suggerimento rifiutato");
									DaoManager.commit(tx);

								}
								else
								{
									errori.add("ERRORE: " + elementoSingle.get(0).getCodDocumento() + " rifiuto impossibile perch\u00E8 la modifica dello stato non \u00E8 andata a buon fine");
									//break;
									DaoManager.rollback(tx);

								}

							} catch (Exception e) {
								log.error("", e);
								errori.add("ERRORE: " + elementoSingle.get(0).getCodDocumento() + " rifiuto impossibile perch\u00E8 si \u00E8 verificata una eccezione");
								//break;
								DaoManager.rollback(tx);

							}

						}

					}

				}

			}
			else if (tipoOperazione.equals("V")) // accetta sugg bibl
			{
				// operazione di accettazione dei suggerimenti del bibliotecario
				Connection c = null;
				try {
					c = dao.getConnection();
					for (int i = 0; i < richiesta.getDatiInput().size(); i++) {
						Boolean procedi = false;
						//Lettura singola

						ListaSuppSuggerimentoVO eleRicercaSingle = new ListaSuppSuggerimentoVO();
						eleRicercaSingle.setCodBibl(richiesta.getCodBib());
						eleRicercaSingle.setCodPolo(richiesta.getCodPolo());
						//eleRicercaSingle.setTipoDocLet("S");

						List<Integer> eleSingle = new ArrayList<Integer>();
						eleSingle.add(richiesta.getDatiInput().get(i));

						eleRicercaSingle.setIdSugList(eleSingle);
						eleRicercaSingle.setOrdinamento("");
						eleRicercaSingle.setChiamante("");
						eleRicercaSingle.setElemXBlocchi(0);

						List<SuggerimentoVO> elementoSingle = null;
						try {
							elementoSingle = dao.getRicercaListaSuggerimenti(c, eleRicercaSingle);
						} catch (Exception e) {
							log.error("", e);
						}
						// test esistenza di un unico risultato
						if (elementoSingle.size() == 1) {
							// controllo stato
							SuggerimentoVO sugg = ValidazioneDati.first(elementoSingle);
							if (sugg.getStatoSuggerimento().equals("A")) // accettato
							{
								errori
										.add("ERRORE: "
												+ sugg.getCodiceSuggerimento()
												+ " Suggerimento gia' accettato");
								//break;
							}
							if (sugg.getStatoSuggerimento().equals("R")) // rifiutato
							{
								errori
										.add("ERRORE: "
												+ sugg.getCodiceSuggerimento()
												+ " Suggerimento gia' rifiutato");
								//break;
							}
							if (sugg.getStatoSuggerimento().equals("O")) // ordinato
							{
								errori
										.add("ERRORE: "
												+ sugg.getCodiceSuggerimento()
												+ " Suggerimento gia' ordinato");
								//break;
							}
							if (sugg.getStatoSuggerimento().equals("W")) // in attesa di risposta
								procedi = true;

							// modifica dello stato
							if (procedi) {
								DaoManager.begin(tx);
								sugg.setUtente(richiesta.getUser());
								sugg.setStatoSuggerimento("A");
								try {
									Boolean risultato = dao.modificaSuggerimento(c, sugg);
									if (risultato) {
										errori
												.add("OK: "
														+ sugg.getCodiceSuggerimento()
														+ " Suggerimento accettato");
										DaoManager.commit(tx);

									} else {
										errori
												.add("ERRORE: "
														+ sugg.getCodiceSuggerimento()
														+ " accettazione impossibile perch\u00E8 la modifica dello stato non \u00E8 andata a buon fine");
										//break;
										DaoManager.rollback(tx);

									}

								} catch (Exception e) {
									log.error("", e);
									errori
											.add("ERRORE: "
													+ sugg.getCodiceSuggerimento()
													+ " accettazione impossibile perch\u00E8 si \u00E8 verificata una eccezione");
									//break;
									DaoManager.rollback(tx);
								}

							}

						}

					}
				} finally {
					//almaviva5_20130509 #4861
					dao.close(c);
				}

			}
			else if (tipoOperazione.equals("F")) // rifiuto sugg bibl
			{
				// operazione di rifiuto dei suggerimenti del bibliotecario
				Connection c = dao.getConnection();
				try {
					for (int i = 0; i < richiesta.getDatiInput().size(); i++) {
						Boolean procedi = false;
						//Lettura singola

						ListaSuppSuggerimentoVO eleRicercaSingle = new ListaSuppSuggerimentoVO();
						eleRicercaSingle.setCodBibl(richiesta.getCodBib());
						eleRicercaSingle.setCodPolo(richiesta.getCodPolo());
						//eleRicercaSingle.setTipoDocLet("S");

						List<Integer> eleSingle = new ArrayList<Integer>();
						eleSingle.add(richiesta.getDatiInput().get(i));

						eleRicercaSingle.setIdSugList(eleSingle);
						eleRicercaSingle.setOrdinamento("");
						eleRicercaSingle.setChiamante("");
						eleRicercaSingle.setElemXBlocchi(0);

						List<SuggerimentoVO> elementoSingle = null;
						try {
							elementoSingle = dao.getRicercaListaSuggerimenti(c, eleRicercaSingle);
						} catch (Exception e) {
							log.error("", e);
						}
						// test esistenza di un unico risultato
						if (elementoSingle.size() == 1) {
							// controllo stato
							SuggerimentoVO sugg = elementoSingle.get(0);
							if (sugg.getStatoSuggerimento().equals("A")) // accettato
							{
								errori
										.add("ERRORE: "
												+ sugg
														.getCodiceSuggerimento()
												+ " Suggerimento gia' accettato");
								//break;
							}
							if (sugg.getStatoSuggerimento()
									.equals("R")) // rifiutato
							{
								errori
										.add("ERRORE: "
												+ sugg
														.getCodiceSuggerimento()
												+ " Suggerimento gia' rifiutato");
								//break;
							}
							if (sugg.getStatoSuggerimento()
									.equals("O")) // ordinato
							{
								errori
										.add("ERRORE: "
												+ sugg
														.getCodiceSuggerimento()
												+ " Suggerimento gia' ordinato");
								//break;
							}
							if (sugg.getStatoSuggerimento()
									.equals("W")) // in attesa di risposta
							{
								procedi = true;
							}

							// modifica dello stato
							if (procedi) {
								DaoManager.begin(tx);
								sugg.setUtente(richiesta.getUser());
								sugg.setStatoSuggerimento("R");
								try {
									Boolean risultato = dao.modificaSuggerimento(c, sugg);
									if (risultato) {
										errori
												.add("OK: "
														+ sugg
																.getCodiceSuggerimento()
														+ " Suggerimento rifiutato");
										DaoManager.commit(tx);

									} else {
										errori
												.add("ERRORE: "
														+ sugg
																.getCodiceSuggerimento()
														+ " riifuto impossibile perch\u00E8 la modifica dello stato non \u00E8 andata a buon fine");
										//break;
										DaoManager.rollback(tx);

									}

								} catch (Exception e) {
									log.error("", e);
									errori
											.add("ERRORE: "
													+ sugg
															.getCodiceSuggerimento()
													+ " rifiuto impossibile perch\u00E8 si \u00E8 verificata una eccezione");
									//break;
									DaoManager.rollback(tx);

								}

							}

						}

					}
				} finally {
					//almaviva5_20130509 #4861
					dao.close(c);
				}
			}




/*		}catch (ValidationException e) {
	      	  //logger.error("Errore in getBibliteche",e);
	      	  //throw e;
	            //throw new EJBException(e);
			log.error("", e);
			throw e;*/

		} catch (Exception e) {
			//
			//log.error("", e);
        	if (richiesta.isServizioPicos())
			{

    			throw new ValidationException(e);
			}
		} finally {
			DaoManager.endTransaction(tx, true);
		}

		return output;
	}

	private void rinnovoOrdini(OperazioneSuOrdiniMassivaVO richiesta,
			UserTransaction tx, OperazioneSuOrdiniMassivaVO output,
			List<String> errori) throws Exception {
		// rinnovo

		DaoManager.begin(tx);
		for (int i = 0; i < richiesta.getDatiInput().size(); i++) {
			Boolean procedi = false;
			// Lettura singola
			ListaSuppOrdiniVO ricercaOrdiniSingle = new ListaSuppOrdiniVO();
			ricercaOrdiniSingle.setCodBibl(richiesta.getCodBib());
			ricercaOrdiniSingle.setCodPolo(richiesta.getCodPolo());
			ricercaOrdiniSingle.setIDOrd(richiesta.getDatiInput().get(i));
			ricercaOrdiniSingle.setOrdinamento("");
			ricercaOrdiniSingle.setChiamante("");
			ricercaOrdiniSingle.setElemXBlocchi(0);
			List<OrdiniVO> ordSingle = null;
			try {
				ordSingle=dao.getRicercaListaOrdini(ricercaOrdiniSingle);
			} catch (Exception e) {
				log.error("", e);
			}
			// test esistenza di un unico risultato
			if (ordSingle.size()==1)
			{
				// controllo stato

				OrdiniVO o = ordSingle.get(0);
				if (!o.isContinuativo())
				{
					// solo ordini continuativi
					errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine non continuativo");
					//break;
				}

				if (o.isContinuativo() && o.equals("C") && o.isRinnovato())
				{
					// se già chiusi, non devono essere già stati rinnovati
					errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine chiuso gi\u00E0 rinnovato");
					//break;
				}
				if (o.isContinuativo())
				{
					// va modificato lo stato dell'ordine originario a rinnovato
					// va creato il nuovo ordine opportunamente preparato con gli aumenti degli anni e la memorizzazione del padre origine dei rinnovi
					if (o.getTipoOrdine()!=null &&  o.getTipoOrdine().equals("R")) //non ordine di rilegatura
					{
						errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine di rilegatura non rinnovabile");
						//break;
					}
					else
					{
						procedi=true;
					}
					// in caso di ordine rinnovo picos in cui va creato solo l'inventari e non creato l'ordine
					// rossana 01.04.09
					if (richiesta.isServizioPicos() && richiesta.isSoloCreazioneInventari())
					{
						procedi=false;
						try
						{
							DaoManager.begin(tx);
							//Inizio transazione
							SchedaInventarioVO schedaInv = DomainEJBFactory.getInstance().getInventarioBMT().inserimentoInventarioOrdine(richiesta.getPassaggioDati(), richiesta.getTicket() );
							output.setScheda(new SchedaInventarioAcqVO(schedaInv));
							DaoManager.commit(tx);
						} catch (Exception e) {
							//log.error("", e);
							DaoManager.rollback(tx);
							throw e;
						}
					}

					// modifica dello stato
					if (procedi)
					{
						o.setUtente(DaoManager.getFirmaUtente(richiesta.getTicket()));
						o.setRinnovato(true);
						try {
							DaoManager.begin(tx);
							//Inizio transazione
							try {
								if (richiesta.isServizioPicos() && (o.getPeriodoValAbbOrdine()==null || (o.getPeriodoValAbbOrdine()!=null && o.getPeriodoValAbbOrdine().trim().length()==0) ))
								{
									o.setPeriodoValAbbOrdine("1");
								}
								dao.modificaOrdine(o);
							} catch (Exception e) {
								errori.add("Exception - modifica ordine : " + e.getMessage());
								log.error("", e);
							}

							String codOrdPrec="";
							String annoOrdPrec="";
							codOrdPrec=o.getCodOrdine();
							annoOrdPrec=o.getAnnoOrdine();
							if (o.isRinnovato() && o.getCodPrimoOrdine()!=null  &&  o.getCodPrimoOrdine().trim().length()>0 && !o.getCodPrimoOrdine().trim().equals("0") && o.getAnnoPrimoOrdine()!=null && o.getAnnoPrimoOrdine().trim().length()>0 && !o.getAnnoPrimoOrdine().trim().equals("0"))
							{
								// viene salvaguardato sempre l'ordine originario
								codOrdPrec=o.getCodPrimoOrdine();
								annoOrdPrec=o.getAnnoPrimoOrdine();
							}
							else
							{
								//
								codOrdPrec=o.getCodOrdine();
								annoOrdPrec=o.getAnnoOrdine();
							}

							OrdiniVO ordineClonato = (OrdiniVO) o.clone();
							ordineClonato.setCodOrdine("");
							ordineClonato.setRinnovato(false);
							if (richiesta.isServizioPicos() && !richiesta.isAggiornamentoRinnovo())
							{
								// caso di buchi di catena per cui il rinnovo creato non sarà l'ultimo elemento della catena dei rinnovi
								ordineClonato.setRinnovato(true);
							}
							ordineClonato.setStatoOrdine("A");
							ordineClonato.setCodPrimoOrdine(codOrdPrec);
							ordineClonato.setAnnoPrimoOrdine(annoOrdPrec);

							Calendar c = Calendar.getInstance();
						 	int anno=c.get(Calendar.YEAR);
						 	String annoAttuale="";
						 	annoAttuale=Integer.valueOf(anno).toString();
							String annoOrd2=annoAttuale;

							// ASSEGNAZIONE DELLA data di sistema
							Date dataodierna=new Date(); // DateUtil.getDate();
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
							String dataOdiernaStr =formato.format(dataodierna);

							ordineClonato.setAnnoOrdine(annoOrd2);
							ordineClonato.setDataOrdine(dataOdiernaStr);
							// vedere tck 2335: SOLO per i periodici in abbonamento (cioè nature S & ordini continuativi & impegno 2) il rinnovo prevede anche l'aggiornamento della data inizio e data fine abbonamento, del volume e dell'anno di abbonamento
							boolean periodicoInAbbonamento=false;
							if (o.isContinuativo() && o.getNaturaOrdine()!=null && o.getNaturaOrdine().equals("S") && o.getStatoOrdine()!=null  && o.getStatoOrdine().equals("A") )
							{
								if (o.isGestBil() && o.getBilancio()!=null && o.getBilancio().getCodice3()!=null &&  o.getBilancio().getCodice3().trim().length()>0 && o.getBilancio().getCodice3().trim().equals("2"))
								{
									periodicoInAbbonamento=true;
								}
								else if ( o.getTipoOrdine().equals("L") || o.getTipoOrdine().equals("D") || o.getTipoOrdine().equals("C"))
								{
									periodicoInAbbonamento=true;
								}
							}
							if (periodicoInAbbonamento)
							{
								if (o.getNumVolAbbOrdine()>0)
								{
									ordineClonato.setNumVolAbbOrdine(o.getNumVolAbbOrdine()+1);
								}
/*										if (ordSingle.get(0).getAnnataAbbOrdine()!=null &&  ordSingle.get(0).getAnnataAbbOrdine().trim().length()>0 && strIsNumeric(ordSingle.get(0).getAnnataAbbOrdine().trim()))
								{
									ordineClonato.setAnnataAbbOrdine(String.valueOf(Integer.valueOf(ordSingle.get(0).getAnnataAbbOrdine().trim())+1));
								}
*/
								int incremento=1;

								if (o.getPeriodoValAbbOrdine()!=null && o.getPeriodoValAbbOrdine().trim().length()>0 )
								{
									if (o.getPeriodoValAbbOrdine().equals("2"))
									{
										incremento=2;
									}
									if (o.getPeriodoValAbbOrdine().equals("3"))
									{
										incremento=3;

									}
								}
								else
								{
									if ( richiesta.isServizioPicos() && (o.getPeriodoValAbbOrdine()==null || (o.getPeriodoValAbbOrdine()!=null && o.getPeriodoValAbbOrdine().trim().length()==0) ))
									{
										ordineClonato.setPeriodoValAbbOrdine("1");
									}
								}
								String dataAnnoIncrementato="";
								// 14.07.09 assoggettare anche annoabbordine all'incremento periodale (Rossana)
								if (o.getAnnoAbbOrdine()!=null &&  o.getAnnoAbbOrdine().trim().length()>0 && strIsNumeric(o.getAnnoAbbOrdine().trim()))
								{
									ordineClonato.setAnnoAbbOrdine(String.valueOf(Integer.valueOf(o.getAnnoAbbOrdine().trim())+incremento));
								}
								if (ordineClonato.getDataFineAbbOrdine()!=null && ordineClonato.getDataFineAbbOrdine().trim().length()>0)
								{
									//eleOrdineAttivo.setDataPubblFascicoloAbbOrdine();
									try {
										formato.setLenient(false); // Date date =
										// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
										formato.parse(ordineClonato.getDataFineAbbOrdine().trim());

										String dataParteAnteriore=ordineClonato.getDataFineAbbOrdine().substring(0,6);
										int dataParteAnno=Integer.valueOf(ordineClonato.getDataFineAbbOrdine().substring(6,10))+ incremento;
										dataAnnoIncrementato=dataParteAnteriore+ String.valueOf(dataParteAnno); //+ annoAttuale
										ordineClonato.setDataFineAbbOrdine(dataAnnoIncrementato);
									} catch (Exception e) {
										log.error("", e);
									}
								}
								if (ordineClonato.getDataPubblFascicoloAbbOrdine()!=null && ordineClonato.getDataPubblFascicoloAbbOrdine().trim().length()>0)
								{
									//eleOrdineAttivo.setDataPubblFascicoloAbbOrdine();
									try {
										formato.setLenient(false); // Date date =
										// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
										formato.parse(ordineClonato.getDataPubblFascicoloAbbOrdine().trim());

										String dataParteAnteriore=ordineClonato.getDataPubblFascicoloAbbOrdine().substring(0,6);
										//String dataAnnoIncrementato=dataParteAnteriore+annoAttuale;
										int dataParteAnno=Integer.valueOf(ordineClonato.getDataPubblFascicoloAbbOrdine().substring(6,10))+incremento;
										dataAnnoIncrementato=dataParteAnteriore+ String.valueOf(dataParteAnno); //+ annoAttuale
										ordineClonato.setDataPubblFascicoloAbbOrdine(dataAnnoIncrementato);
									} catch (Exception e) {
										log.error("", e);
									}

								}
								// ricoprimento dei dati precedentemente valorizzati con parametri Picos
								if (richiesta.isServizioPicos())
								{
									//operazioni su rinnovo PICOS
									if (richiesta.isServizioPicos() && richiesta.getKanno()!=null && richiesta.getKanno().trim().length()==4)
									{
										ordineClonato.setAnnoAbbOrdine(richiesta.getKanno());
										ordineClonato.setDataFineAbbOrdine("31/12/"+richiesta.getKanno()); // data fine
										ordineClonato.setDataPubblFascicoloAbbOrdine("01/01/"+richiesta.getKanno()); // data inizio
									}
									if (richiesta.isServizioPicos() && richiesta.getPrezzoBil()>0)
									{

										ordineClonato.setPrezzoEuroOrdine(richiesta.getPrezzoBil());
									}

									//fine operazioni su rinnovo PICOS
								}
							}



							if (o.isGestBil() && o.getBilancio()!=null && o.getBilancio().getCodice1()!=null &&  o.getBilancio().getCodice1().trim().length()>0 &&  strIsNumeric(o.getBilancio().getCodice1().trim()))
							{
					        	ListaSuppBilancioVO ricercaBilanci=new ListaSuppBilancioVO();
					        	ricercaBilanci.setCodPolo(o.getCodPolo());
					        	ricercaBilanci.setCodBibl(o.getCodBibl());
					        	ricercaBilanci.setEsercizio(String.valueOf(Integer.valueOf(o.getBilancio().getCodice1().trim())+1));
					        	ricercaBilanci.setCapitolo(o.getBilancio().getCodice2());

					        	// ricoprimento dei dati precedentemente valorizzati con parametri Picos
					        	if (richiesta.isServizioPicos())
								{
						        	if (richiesta.isServizioPicos() && richiesta.getAnnoBil()!=0)
									{
										// su rinnovo PICOS
										ricercaBilanci.setEsercizio(String.valueOf(richiesta.getAnnoBil()));
						        		if (richiesta.isServizioPicos() && richiesta.getCapitoloBil()!=0)
										{
											ricercaBilanci.setCapitolo(String.valueOf(richiesta.getCapitoloBil()));
										}
									}
								}
					        	ricercaBilanci.setImpegno(o.getBilancio().getCodice3());

					        	List<BilancioVO> elenco=null;
								try {
									elenco= dao.getRicercaListaBilanci(ricercaBilanci);
								} catch (Exception e) {
									errori.add("Exception - ricerca bilanci : " + e.getMessage());
									log.error("", e);
								}

					        	if (elenco!=null && elenco.size()==1)
					        	{
					        		ordineClonato.getBilancio().setCodice1(String.valueOf(Integer.valueOf(o.getBilancio().getCodice1().trim())+1));

					        		if (richiesta.isServizioPicos() && richiesta.getAnnoBil()!=0)
									{
										// su rinnovo PICOS
						        		ordineClonato.getBilancio().setCodice1(String.valueOf(richiesta.getAnnoBil()));
						        		if (richiesta.isServizioPicos() && richiesta.getCapitoloBil()!=0)
										{
							        		ordineClonato.getBilancio().setCodice2(String.valueOf(richiesta.getCapitoloBil()));
										}
									}
					        	}
							}

							Boolean risultatoIns=false;
							try {
								// errore introdotto per test
/*										if (ordineClonato.getIDOrd()==6)
								{
									ordineClonato.getFornitore().setCodice("12");
								}
*/
								risultatoIns=dao.inserisciOrdine(ordineClonato);
							} catch (Exception e) {
								errori.add("Exception - inserimento ordine : " + e.getMessage());
								log.error("", e);
							}

							if (risultatoIns)
							{
								DaoManager.begin(tx);
					        	if (!richiesta.isServizioPicos())
								{
					        		errori.add("OK: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine rinnovato");
					        		// recuperare nuove coordinate del record inserito magari con una lettura
									//StrutturaTerna ordNuovo=dao.gestioneRinnovato( ordSingle.get(0).getIDOrd(), ordSingle.get(0).getCodPrimoOrdine(),ordSingle.get(0).getAnnoPrimoOrdine(), "S");
									StrutturaTerna ordNuovo=new StrutturaTerna(ordineClonato.getTipoOrdine(), ordineClonato.getAnnoOrdine(), ordineClonato.getCodOrdine());
									errori.add("NUOVO ORDINE : " + ordNuovo.getCodice2() + " - "  + ordNuovo.getCodice1() + " - " +  ordNuovo.getCodice3());
								}
					        	if (richiesta.isServizioPicos())
								{
					        		StrutturaTerna ordNuovo=new StrutturaTerna(ordineClonato.getTipoOrdine(), ordineClonato.getAnnoOrdine(), ordineClonato.getCodOrdine());
									output.setOrdineNuovo(ordNuovo);
									output.setEsitoRinnovoPicos(true);
									if (output.getOrdineNuovo()!=null)
									{
										richiesta.getPassaggioDati().setCodTipoOrd(output.getOrdineNuovo().getCodice1());
										richiesta.getPassaggioDati().setAnnoOrd(output.getOrdineNuovo().getCodice2());
										richiesta.getPassaggioDati().setCodOrd(output.getOrdineNuovo().getCodice3());
									}
									SchedaInventarioVO schedaInv = DomainEJBFactory
											.getInstance()
											.getInventarioBMT()
											.inserimentoInventarioOrdine(richiesta.getPassaggioDati(), richiesta.getTicket());
									output.setScheda(new SchedaInventarioAcqVO(schedaInv));

								}

								DaoManager.commit(tx);

							}
							else
							{
					        	if (!richiesta.isServizioPicos())
								{
									errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Rinnovo impossibile perch\u00E8 si \u00E8 verificata una eccezione");
								}
								DaoManager.rollback(tx);
							}


						} catch (Exception e) {
							//log.error("", e);
							DaoManager.rollback(tx);
							if (!richiesta.isServizioPicos())
							{
				        		log.error("", e);
				        		errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Rinnovo impossibile perch\u00E8 si \u00E8 verificata una eccezione");
							}
				        	if (richiesta.isServizioPicos())
							{
				  	      	  throw e;
							}
							//break;
						}

					}


				}

			}

		} //FINE FOR
	}

	private void chiusuraOrdini(OperazioneSuOrdiniMassivaVO richiesta,
			UserTransaction tx, List<String> errori)
			throws Exception, DaoManagerException {
		Tba_ordiniDao odao = new Tba_ordiniDao();
		boolean ok = false;
		Set<StrutturaTerna> bilanci = new HashSet<StrutturaTerna>();
		Set<Integer> sezioni = new HashSet<Integer>();

		String firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());
		List<Integer> datiInput = richiesta.getDatiInput();
		int size = ValidazioneDati.size(datiInput);
		for (int i = 0; i < size; i++) {
			ok = false;
			//Lettura singola
			ListaSuppOrdiniVO ricercaOrdiniSingle = new ListaSuppOrdiniVO();
			ricercaOrdiniSingle.setCodBibl(richiesta.getCodBib());
			ricercaOrdiniSingle.setCodPolo(richiesta.getCodPolo());
			ricercaOrdiniSingle.setIDOrd(datiInput.get(i));
			ricercaOrdiniSingle.setOrdinamento("");
			ricercaOrdiniSingle.setChiamante("");
			ricercaOrdiniSingle.setElemXBlocchi(0);
			List<OrdiniVO> ordSingle = null;
			try {
				ordSingle = dao.getRicercaListaOrdini(ricercaOrdiniSingle);
			} catch (Exception e) {
				log.error("", e);
			}
			// test esistenza di un unico risultato
			if (ordSingle.size() == 1) {
				// controllo stato
				OrdiniVO o = ordSingle.get(0);
				if (o.getStatoOrdine().equals("C"))
				{
					errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine gi\u00E0 chiuso");
					//break;
				}
				else
				{
					Double totImportInv=0.00;
					if (!o.getTipoOrdine().equals("R")) //non ordine di rilegatura
					{
						// controllo esistenza inventari associati
						// almaviva5_20140618 #4967
						DaoManager.begin(tx);
						int cntInv = odao.countInventariOrdine(o.getCodPolo(),
								o.getCodBibl(), o.getTipoOrdine(),
								Integer.valueOf(o.getAnnoOrdine()),
								Integer.valueOf(o.getCodOrdine()));

				          // non esistono inventari associati
				          if (cntInv == 0) {
								errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " chiusura impossibile perch\u00E8 non esistono inventari associati");
				          }
				          else
				        	  ok = true;

					}
					// modifica dello stato
					if (ok) {
						DaoManager.begin(tx);
						o.setUtente(firmaUtente);
						o.setStatoOrdine("C");
						if (richiesta.isAdeguamentoPrezzo()) {// dipende dalla configurazione
							// almaviva5_20140618 #4967
							if (ValidazioneDati.in(o.getTipoOrdine(), "A", "V") ) {
								totImportInv = odao.importoInventariOrdine(o.getCodPolo(),
										o.getCodBibl(), o.getTipoOrdine(),
										Integer.valueOf(o.getAnnoOrdine()),
										Integer.valueOf(o.getCodOrdine())).doubleValue();
								o.setPrezzoEuroOrdine(totImportInv);

							}
						}

						ok = false;
						try {
							ok = dao.modificaOrdine(o);
							if (ok) {
								errori.add("OK: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " Ordine chiuso");
								if (o.isGestBil())
									bilanci.add(o.getBilancio());
								if (o.isGestSez())
									sezioni.add(o.getIDSez());
							} else
								errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " chiusura impossibile perch\u00E8 la modifica dello stato non \u00E8 andata a buon fine");

						} catch (Exception e) {
							log.error("", e);
							errori.add("ERRORE: " + o.getAnnoOrdine() + " - "  + o.getTipoOrdine() + " - " +  o.getCodOrdine() + " chiusura impossibile perch\u00E8 si \u00E8 verificata una eccezione");
						} finally {
							DaoManager.endTransaction(tx, ok);
						}
					}
				}
			}
		}

		//almaviva5_20140624 #4631 aggiornamento bilanci e sezioni
		//BILANCI
		if (ValidazioneDati.isFilled(bilanci))
			for (StrutturaTerna b : bilanci) {
				ListaSuppBilancioVO ricercaBilanci = new ListaSuppBilancioVO();
				ricercaBilanci.setCodPolo(richiesta.getCodPolo());
				ricercaBilanci.setCodBibl(richiesta.getCodBib());
				ricercaBilanci.setEsercizio(b.getCodice1());
				ricercaBilanci.setCapitolo(b.getCodice2());
				String cod_mat = ValidazioneDati.trimOrNull(b.getCodice3());
				ricercaBilanci.setImpegno(cod_mat);
				try {
					DaoManager.begin(tx);
					ok = false;
					List<BilancioVO> elenco = dao.getRicercaListaBilanci(ricercaBilanci);
					if (ValidazioneDati.size(elenco) == 1) {
						BilancioVO bilInOggetto = elenco.get(0);
						bilInOggetto.setUtente(firmaUtente);
						ok = dao.modificaBilancio(bilInOggetto, cod_mat);
					}
				} catch (Exception e) {
					// l'errore capita in questo punto
					log.error("", e);
				} finally {
					DaoManager.endTransaction(tx, ok);
				}
			}


		//SEZIONI
		if (ValidazioneDati.isFilled(sezioni))
			for (Integer idSez : sezioni) {
				ListaSuppSezioneVO ricercaSezioni = new ListaSuppSezioneVO();
				ricercaSezioni.setCodPolo(richiesta.getCodPolo());
				ricercaSezioni.setCodBibl(richiesta.getCodBib());
				ricercaSezioni.setIdSezione(idSez);
				try {
					DaoManager.begin(tx);
					ok = false;
					List<SezioneVO> elenco = dao.getRicercaListaSezioni(ricercaSezioni);
					if (ValidazioneDati.size(elenco) == 1) {
						SezioneVO sezInOggetto = elenco.get(0);
						sezInOggetto.setUtente(firmaUtente);
						ok = dao.modificaSezione(sezInOggetto);
					}
				} catch (Exception e) {
					// l'errore capita in questo punto
					log.error("", e);
				} finally {
					DaoManager.endTransaction(tx, ok);
				}
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
	 * @generated //
	 */
	public boolean getFusioneCollane(String bidOld, String bidNew, String firmaUtente)
			throws DataException, ApplicationException, ValidationException {

		boolean ok = false;

		UserTransaction tx = ctx.getUserTransaction();

		try {
			DaoManager.begin(tx);
			log.debug("Inizio transazione");

			// ordini
			ListaSuppOrdiniVO critOrd=new ListaSuppOrdiniVO();
			List<String> arrBidOrd=new ArrayList<String>();
			arrBidOrd.add(bidOld);
			critOrd.setBidList(arrBidOrd);
			// devo considerare tutti gli ordini anche quelli annullati
			String[] statoOrdArr = new String[] {"A", "C", "N" };
			critOrd.setStatoOrdineArr(statoOrdArr);

			List<OrdiniVO> listaOrd = null;
			try {
				listaOrd = dao.getRicercaListaOrdini(critOrd);
			} catch (Exception e) {
				log.error("", e);
			}


			if (listaOrd!=null &&  listaOrd.size() > 0) {
				for (int t=0; t<listaOrd.size(); t++)
				{
					OrdiniVO eleOrd=listaOrd.get(t);
					eleOrd.getTitolo().setCodice(bidNew);
					eleOrd.setUtente(firmaUtente);
					if (!dao.modificaOrdiniXFusione(eleOrd)) {
						ok = false;
						throw new DataException("erroreUpdateOrdini");
					}
				}
				ok = true;
			} else {
				ok = true;
			}
			// gare

			ListaSuppGaraVO critGare=new ListaSuppGaraVO();
			critGare.setBid(new StrutturaCombo(bidOld, ""));
			//critGare.getBid().setCodice(bidOld);

			List<GaraVO> listaGare = null;
			try {
				listaGare = dao.getRicercaListaGare(critGare);
			} catch (Exception e) {
				log.error("", e);
			}

			if (listaGare!=null &&  listaGare.size() > 0) {
				for (int x=0; x<listaGare.size(); x++)
				{
					GaraVO eleGara=listaGare.get(x);
					eleGara.getBid().setCodice(bidNew);
					eleGara.setUtente(firmaUtente);
					if (!dao.modificaGaraXFusione(eleGara)) {
						ok = false;
						throw new DataException("erroreUpdateGare");
					}
				}
				ok = true;
			} else {
				ok = true;
			}
			// suggerimenti bibliotecario

			ListaSuppSuggerimentoVO critSuggBibl=new ListaSuppSuggerimentoVO();
			critSuggBibl.setTitolo(new StrutturaCombo(bidOld, ""));
			//critSuggBibl.getTitolo().setCodice(bidOld);

			List<SuggerimentoVO> listaSuggBibl = null;
			try {
				listaSuggBibl = dao.getRicercaListaSuggerimenti(critSuggBibl);
			} catch (Exception e) {
				log.error("", e);
			}


			if (listaSuggBibl!=null &&  listaSuggBibl.size() > 0) {
				for (int y=0; y<listaSuggBibl.size(); y++)
				{
					SuggerimentoVO eleSuggBibl=listaSuggBibl.get(y);
					eleSuggBibl.getTitolo().setCodice(bidNew);
					eleSuggBibl.setUtente(firmaUtente);
					if (!dao.modificaSuggerimentoXFusione(eleSuggBibl)) {
						ok = false;
						throw new DataException("erroreUpdateSuggBibl");
					}
				}
				ok = true;
			} else {
				ok = true;
			}

			// suggerimenti bibliotecario

			ListaSuppDocumentoVO critSuggLett=new ListaSuppDocumentoVO();
			critSuggLett.setTitolo(new StrutturaCombo(bidOld, ""));
			//critSuggLett.getTitolo().setCodice(bidOld);

			List<DocumentoVO> listaSuggLett = null;
			try {
				listaSuggLett = dao.getRicercaListaDocumenti(critSuggLett);
			} catch (Exception e) {
				log.error("", e);
			}


			if (listaSuggLett!=null &&  listaSuggLett.size() > 0) {
				for (int z=0; z<listaSuggLett.size(); z++)
				{
					DocumentoVO eleSuggLett=listaSuggLett.get(z);
					eleSuggLett.getTitolo().setCodice(bidNew);
					eleSuggLett.setUtenteCod(firmaUtente);
					if (!dao.modificaDocumentoXFusione(eleSuggLett)) {
						ok = false;
						throw new DataException("erroreUpdateSuggBibl");
					}
				}
				ok = true;
			} else {
				ok = true;
			}
			DaoManager.commit(tx);
			log.debug("Commit transazione");
			return ok;

		} catch (ValidationException e) {
			throw e;
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
     * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
     * @generated
     *
     * //
     */
	public AdeguamentoCalcoliVO adeguaCalcoli (AdeguamentoCalcoliVO adeguamentoCalcoliVO ) throws DataException, ApplicationException , ValidationException
	{


		// ADEGUAMENTO SEZIONI
		AdeguamentoCalcoliVO output = null;
		UserTransaction tx = ctx.getUserTransaction();
		try {
			output=new AdeguamentoCalcoliVO();

			ListaSuppSezioneVO critSez = new ListaSuppSezioneVO();
			List<SezioneVO> listaSez = null;
			critSez.setCodPolo(adeguamentoCalcoliVO.getCodPolo());
			critSez.setCodBibl(adeguamentoCalcoliVO.getCodBib());

			try
			{
				listaSez= dao.getRicercaListaSezioni(critSez);
			} catch (Exception e) {
				log.error("", e);
			}

			if (listaSez!=null &&  listaSez.size() > 0)
			{
				for (int y=0; y<listaSez.size(); y++)
				{
					if (listaSez.get(y).getIdSezione()>0)
					{
						SezioneVO sezioneInOggetto=listaSez.get(y);
						sezioneInOggetto.setUtente(adeguamentoCalcoliVO.getUser());

//						arrIdSez.add(listaSez.get(y).getIdSezione());
//						// calcolo disponibilità
//						CalcoliVO risult=new CalcoliVO();
//						risult=dao.Calcola(adeguamentoCalcoliVO.getCodPolo(),adeguamentoCalcoliVO.getCodBib(), listaSez.get(y).getIdSezione(),listaSez.get(y),0,null,0,null,null, null, loc);
//						double disp=sezioneInOggetto.getBudgetSezione();
//						if (risult!=null && risult.getOrdinato()!=0)
//						{
//							disp=sezioneInOggetto.getBudgetSezione() - risult.getOrdinato();
//						}
//						//sezioneInOggetto.setSommaDispSezione(disp);
//						if (sezioneInOggetto.getSommaDispSezione()==disp)
//						{
//							output.getErrori().add("OK sezione: " + listaSez.get(y).getCodiceSezione() + "la disponibilità del db:" + String.valueOf(sezioneInOggetto.getSommaDispSezione()) + " coincide con quella calcolata:" + String.valueOf(disp));
//						}
//						else
//						{
//							output.getErrori().add("OK sezione: " + listaSez.get(y).getCodiceSezione() + "la disponibilità del db:" + String.valueOf(sezioneInOggetto.getSommaDispSezione()) + " NON coincide con quella calcolata:" + String.valueOf(disp));
//						}
						//transaction.begin();
						//log.debug("Inizio transazione");

						// modifica della sezione (CON ADEGUAMENTO CALCOLI AUTOMATICO) passando sezioneInOggetto
						Boolean risultato=false;
						DaoManager.begin(tx);
						log.debug("Inizio transazione sezioni");
						try {
							try {
								risultato=dao.modificaSezione(sezioneInOggetto);

							} catch (Exception e) {
								log.error("", e);
							}
							if (risultato)
							{
								DaoManager.commit(tx);
								log.debug("Commit transazione");
								output.getErrori().add("OK - sezione: " + listaSez.get(y).getCodiceSezione() + " Sezione modificata");

							}
							else
							{
								DaoManager.rollback(tx);
								log.error("Rollback transazione");
								output.getErrori().add("ERRORE - sezione: " + listaSez.get(y).getCodiceSezione() + " la modifica della sezione non \u00E8 andata a buon fine");

							}

						} catch (Exception e) {
							log.error("", e);
							output.getErrori().add("ERRORE - sezione: " + listaSez.get(y).getCodiceSezione() + " modifica della sezione impossibile perch\u00E8 si \u00E8 verificata una eccezione");
							//break;
							DaoManager.rollback(tx);
							log.error("Rollback transazione");
						}

					}
				}

			}
			//********************
			// ADEGUAMENTO BILANCI
        	ListaSuppBilancioVO ricercaBilanci=new ListaSuppBilancioVO();
        	ricercaBilanci.setCodPolo(adeguamentoCalcoliVO.getCodPolo());
        	ricercaBilanci.setCodBibl(adeguamentoCalcoliVO.getCodBib());
			List<BilancioVO> listaBil =null;
			//Locale loc=Locale.getDefault();
			try
			{
				listaBil= dao.getRicercaListaBilanci(ricercaBilanci);
			} catch (Exception e) {
				log.error("", e);
			}
			if (listaBil!=null &&  listaBil.size() > 0)
			{
				for (int z=0; z<listaBil.size(); z++)
				{
					BilancioVO bilancioInOggetto=listaBil.get(z);
					bilancioInOggetto.setUtente(adeguamentoCalcoliVO.getUser());
//					if (bilancioInOggetto.getDettagliBilancio()!=null && bilancioInOggetto.getDettagliBilancio().size() >0 )
//					{
//						for (int i=0; i<bilancioInOggetto.getDettagliBilancio().size(); i++)
//						{
//							CalcoliVO risult=new CalcoliVO();
//							BilancioDettVO 	oggettoDettVO=bilancioInOggetto.getDettagliBilancio().get(i);
//							String impegno=oggettoDettVO.getImpegno();
//							try
//							{
//								risult=dao.Calcola(bilancioInOggetto.getCodPolo(), bilancioInOggetto.getCodBibl(),0,null, bilancioInOggetto.getIDBil(),oggettoDettVO.getImpegno(),0,null,null,null ,loc);
//							} catch (Exception e) {
//
//								// l'errore capita in questo punto
//								log.error("", e);
//							}
//							double imp=0.00;
//							double fatt=0.00;
//							double pag=0.00;
//							double acq=0.00;
//
//							if (risult!=null )
//							{
//								if (risult.getOrdinato()!=0)
//								{
//									imp=risult.getOrdinato();
//								}
//								if (risult.getFatturato()!=0)
//								{
//									fatt=risult.getFatturato();
//								}
//								if (risult.getPagato()!=0)
//								{
//									pag=risult.getPagato();
//								}
//								if (risult.getAcquisito()!=0)
//								{
//									acq=risult.getAcquisito();
//								}
//
//							}
//							oggettoDettVO.setImpegnato(imp);
//							oggettoDettVO.setFatturato(fatt);
//							oggettoDettVO.setPagato(pag);
//							oggettoDettVO.setAcquisito(acq);
//							if (risult!=null && risult.getOrdinato()!=0)
//							{
//								if (oggettoDettVO.getImpegnato()==imp)
//								{
//									output.getErrori().add("OK id bil- impegno : (" + String.valueOf(bilancioInOggetto.getIDBil()) +"," + oggettoDettVO.getImpegno() + ") l'impegnato del db:" + String.valueOf(oggettoDettVO.getImpegnato()) + " coincide con quello calcolato:" + String.valueOf(imp));
//								}
//								else
//								{
//									output.getErrori().add("KO id bil- impegno : (" + String.valueOf(bilancioInOggetto.getIDBil()) +"," + oggettoDettVO.getImpegno() + ") l'impegnato del db:" + String.valueOf(oggettoDettVO.getImpegnato()) + " NON coincide con quello calcolato:" + String.valueOf(imp));
//								}
//							}
//							//TODO: aggiungere i confronti con fatturato, pagato e acquisito
//						}
//					}
					//TODO: modifica del bilancio (CON ADEGUAMENTO CALCOLI AUTOMATICO) passando bilInOggetto
					DaoManager.begin(tx);
					log.debug("Inizio transazione bilanci");
					Boolean risultatoBil=false;

					try {
						try {
							risultatoBil=dao.modificaBilancio(bilancioInOggetto, null);

						} catch (Exception e) {
							log.error("", e);
						}
						if (risultatoBil)
						{
							//output.getErrori().add("OK - id sezione: " + listaSez.get(y).getIdSezione() + " Sezione modificata");
							output.getErrori().add("OK - bilancio: " + listaBil.get(z).getIDBil() + " Bilancio modificato");
							DaoManager.commit(tx);
							log.debug("Commit transazione");

						}
						else
						{
							//output.getErrori().add("ERRORE- id sezione: " + listaSez.get(y).getIdSezione()  + " la modifica della sezione non è andata a buon fine");
							//break;
							output.getErrori().add("ERRORE - bilancio: " + listaBil.get(z).getIDBil() + " la modifica del bilancio non \u00E8 andata a buon fine");
							DaoManager.rollback(tx);
							log.error("Rollback transazione");
						}

					} catch (Exception e) {
						log.error("", e);
						output.getErrori().add("ERRORE - bilancio: " + listaBil.get(z).getIDBil() + " modifica del bilancio impossibile perch\u00E8 si \u00E8 verificata una eccezione");
						//break;
						DaoManager.rollback(tx);
						log.error("Rollback transazione");

					}

				}

			}
			//transaction.commit();
			//log.debug("Commit transazione");

		} catch (Exception e) {
/*			try {
				trans.rollback();
				//log.error("Rollback transazione");
			} catch (Exception e1) {
				log.error("", e1);
			}
*/			//log.error("Rollback transazione");
			throw new DataException(e);
		}

		return output;
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
     * //TODO: Must provide implementation for bean create stub
     */
	public List getRicercaListaFatture(ListaSuppFatturaVO ricercaFatture) throws ResourceNotFoundException, ApplicationException, ValidationException
	{

		Boolean testxHib=false;
		//testxHib=true;
        List listaFatture = new ArrayList();
		List results = null;

		if (!testxHib)
		{
			return dao.getRicercaListaFatture(ricercaFatture) ;
		}
		else
		{
	        try
	        {
	        	Tba_fattureDao fattureDao = new Tba_fattureDao();
	        	Tba_ordiniDao ordiniDao = new Tba_ordiniDao();
	        	Validazione.ValidaListaSuppFatturaVO(ricercaFatture);
	            results = fattureDao.getRicercaListaFattureHib(ricercaFatture);
				int totFatt=0;
	            if(results != null && results.size() > 0)
	            {
					totFatt=results.size();
	            	ConfigurazioneORDVO apConfORDNew = new ConfigurazioneORDVO();
	                apConfORDNew.setCodPolo(ricercaFatture.getCodPolo());
	                apConfORDNew.setCodBibl(ricercaFatture.getCodBibl());
	                Boolean gestBil = true;
	                try
	                {
	                    apConfORDNew = ordiniDao.loadConfigurazioneOrdiniHib(apConfORDNew);
	                    if(apConfORDNew != null && !apConfORDNew.isGestioneBilancio())
	                    {
	                    	gestBil = false;
	                    }
	                }
	                catch(Exception e)
	                {
	                    log.error("", e);
	                }

					int numRighe=0;
					int progrRighe=1; // inizializzato e si incrementa solo quando si fa add all'array

	                for(int i = 0; i < results.size(); i++)
	                {
	                    numRighe++;
	                    FatturaVO rec = new FatturaVO();
	                    Tba_fatture oggFATT = (Tba_fatture)results.get(i);
	                    // inizio
						// configurazione
						rec.setGestBil(true);
						if (!gestBil)
						{
							rec.setGestBil(false);
						}
						rec.setIDFatt(oggFATT.getId_fattura());
		                Tbf_biblioteca_in_polo appoBib = oggFATT.getCd_bib();
		                rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
		                rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setDenoBibl(appoBib.getDs_biblioteca());
						//rec.setProgressivo(numRighe);
						rec.setProgressivo(progrRighe);
						rec.setAnnoFattura(oggFATT.getAnno_fattura().toString());
						rec.setProgrFattura(String.valueOf(oggFATT.getProgr_fattura()));
						rec.setNumFattura(oggFATT.getNum_fattura());

						SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
						//format1.format(oggORD.getData_ord());

						String dataFormattata = "";
						try {
							dataFormattata = format1.format(oggFATT.getData_fattura());
						} catch (Exception e) {
							//log.error("", e);
						}
						rec.setDataFattura(dataFormattata);

						dataFormattata = "";
						try {
							dataFormattata = format1.format(oggFATT.getData_reg());
						} catch (Exception e) {
							//log.error("", e);
						}
						rec.setDataRegFattura(dataFormattata);

						rec.setImportoFattura(oggFATT.getImporto().doubleValue());
						rec.setValutaFattura(oggFATT.getValuta());
						rec.setCambioFattura(oggFATT.getCambio().doubleValue());
						rec.setStatoFattura(String.valueOf(oggFATT.getStato_fattura()));
						rec.setDenoStatoFattura("");
						if (rec.getStatoFattura()!=null && rec.getStatoFattura().trim().length()>0)
						{
							if ( rec.getStatoFattura().trim().equals("1"))
							{
								rec.setDenoStatoFattura("Registrata");
							}
							else if (rec.getStatoFattura().trim().equals("2"))
							{
								rec.setDenoStatoFattura("Controllata");
							}
							else if (rec.getStatoFattura().trim().equals("3"))
							{
								rec.setDenoStatoFattura("Ordine di pagamento emesso");
							}
							else if (rec.getStatoFattura().trim().equals("4"))
							{
								rec.setDenoStatoFattura("Contabilizzata");
							}
						}

						rec.setTipoFattura(String.valueOf(oggFATT.getTipo_fattura()));
						rec.setFornitoreFattura(new StrutturaCombo("",""));
						rec.getFornitoreFattura().setCodice(String.valueOf(oggFATT.getCod_fornitore().getCod_fornitore()));
						rec.getFornitoreFattura().setDescrizione(oggFATT.getCod_fornitore().getNom_fornitore());

						// dati fornitore per stampe
						rec.setAnagFornitore(new FornitoreVO());
						rec.getAnagFornitore().setCodFornitore(String.valueOf(oggFATT.getCod_fornitore().getCod_fornitore()));
						rec.getAnagFornitore().setNomeFornitore(oggFATT.getCod_fornitore().getNom_fornitore());
						if (oggFATT.getCod_fornitore().getIndirizzo()!=null && oggFATT.getCod_fornitore().getIndirizzo().trim().length()>0 )
						{
							rec.getAnagFornitore().setIndirizzo(oggFATT.getCod_fornitore().getIndirizzo());
						}
						if (oggFATT.getCod_fornitore().getCitta()!=null && oggFATT.getCod_fornitore().getCitta().trim().length()>0 )
						{
							rec.getAnagFornitore().setCitta(oggFATT.getCod_fornitore().getCitta());
						}
						if (rec.getAnagFornitore().getCap()!=null && rec.getAnagFornitore().getCap().trim().length()>0 )
						{
							rec.getAnagFornitore().setCap(oggFATT.getCod_fornitore().getCap());
						}
						if (rec.getAnagFornitore().getPaese()!=null && rec.getAnagFornitore().getPaese().trim().length()>0 )
						{
							rec.getAnagFornitore().setPaese(oggFATT.getCod_fornitore().getPaese());
						}

						if (rec.getAnagFornitore().getPartitaIva()!=null && rec.getAnagFornitore().getPartitaIva().trim().length()>0 )
						{
							rec.getAnagFornitore().setPartitaIva(oggFATT.getCod_fornitore().getP_iva());
						}
						if (rec.getAnagFornitore().getCodiceFiscale()!=null && rec.getAnagFornitore().getCodiceFiscale().trim().length()>0 )
						{
							rec.getAnagFornitore().setCodiceFiscale(oggFATT.getCod_fornitore().getCod_fiscale());
						}
						if (rec.getAnagFornitore().getFax()!=null && rec.getAnagFornitore().getFax().trim().length()>0 )
						{
							rec.getAnagFornitore().setFax(oggFATT.getCod_fornitore().getFax());
						}
						if (rec.getAnagFornitore().getTelefono()!=null && rec.getAnagFornitore().getTelefono().trim().length()>0 )
						{
							rec.getAnagFornitore().setTelefono(oggFATT.getCod_fornitore().getTelefono());
						}
						rec.getAnagFornitore().setFornitoreBibl(new DatiFornitoreVO());


						rec.setScontoFattura(oggFATT.getSconto().doubleValue());
						rec.setDataUpd(oggFATT.getTs_var());
						List<StrutturaFatturaVO> arrayFattDett = new ArrayList<StrutturaFatturaVO >();

						// recupero le righe di dettaglio della fattura
		                if(oggFATT.getTba_righe_fatture()!= null)
		                {
    				    try
		                    {
    						Object[] elencoFattDett=oggFATT.getTba_righe_fatture().toArray();
    						for (int j = 0; j<elencoFattDett.length; j++) {
    							Tba_righe_fatture eleFattDett = (Tba_righe_fatture) elencoFattDett[j];
    							StrutturaFatturaVO newEleFattDett = new StrutturaFatturaVO();

    							appoBib = eleFattDett.getCd_biblioteca();
    							newEleFattDett.setCodBibl(appoBib.getCd_biblioteca());
    							newEleFattDett.setCodPolo(appoBib.getCd_polo().getCd_polo());
    							newEleFattDett.setIDFatt(eleFattDett.getId_fattura().getId_fattura());
    							newEleFattDett.setProgrRigaFattura(j+1); //
    							newEleFattDett.setRigaFattura(eleFattDett.getRiga_fattura());


    							newEleFattDett.setIDOrd(eleFattDett.getId_ordine().getId_ordine());
    							if (eleFattDett.getCod_mat()!=null && eleFattDett.getCod_mat().getId_capitoli_bilanci()!=null)
    							{
    								newEleFattDett.setIDBil(eleFattDett.getCod_mat().getId_capitoli_bilanci().getId_capitoli_bilanci());
    							}


    							newEleFattDett.setImportoRigaFattura(eleFattDett.getImporto_riga().doubleValue());
    							newEleFattDett.setSconto1RigaFattura(eleFattDett.getSconto_1().doubleValue());
    							newEleFattDett.setSconto2RigaFattura(eleFattDett.getSconto_2().doubleValue());
    							newEleFattDett.setCodIvaRigaFattura(eleFattDett.getCod_iva());


    							String isbd="";
    							String bid="";

    							// solo per la prima fattura dell'elenco ricavo il titolo (mi occorre solo in esamina e non sulla sintetica)

    							if (numRighe==1 && totFatt==1)
    							{
    								if (eleFattDett.getId_ordine().getBid()!=null &&eleFattDett.getId_ordine().getBid().trim().length()!=0)
    								{
    									bid=eleFattDett.getId_ordine().getBid();

    									try {
    										TitoloACQVO recTit=null;
    										recTit = dao.getTitoloOrdineTer(eleFattDett.getId_ordine().getBid().trim());
    										if (recTit!=null && recTit.getIsbd()!=null) {
    											bid=eleFattDett.getId_ordine().getBid().trim();
    											isbd=recTit.getIsbd();
    										}
    										if (recTit==null) {
    											isbd=" ";
    											bid=eleFattDett.getId_ordine().getBid();
    										}
    									} catch (Exception e) {
    											isbd=" ";
    											bid=eleFattDett.getId_ordine().getBid();
    									}
    								}
    							}
    							//

    							newEleFattDett.setTitOrdine(isbd);
    							newEleFattDett.setBidOrdine(bid);
    							newEleFattDett.setInvRigaFatt("");



    							// recupero inventario associato alla riga di fattura
    							if (numRighe==1 && totFatt==1)
    							{
    								InventarioListeVO listaInv=null;
    								try
    								{
    							         String codPolo=rec.getCodPolo();
    							         String codBib=rec.getCodBibl();
    							         String codBibO=rec.getCodBibl();
    							         String codTipOrd=String.valueOf(eleFattDett.getId_ordine().getCod_tip_ord());
    							         int annoOrd=eleFattDett.getId_ordine().getAnno_ord().intValue();
    							         int codOrd=eleFattDett.getId_ordine().getId_ordine();
    							         String codBibF=rec.getCodBibl();
    							         int annoF=Integer.valueOf(rec.getAnnoFattura());
    							         int progF=Integer.valueOf(rec.getProgrFattura());
    							         int rigaF=newEleFattDett.getRigaFattura();
    							         //Locale locale=ricercaFatture.;
    							         Locale locale=Locale.getDefault(); // aggiornare con quella locale se necessario
    							         String ticket=ricercaFatture.getTicket();
    							         //nRec =;
//    		 modifica del 30/10/08 per eliminare  il data exception sollevato da 	getListaInventarioRigaFattura
    		/*							listaInv= inventario.getListaInventarioRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket);
    									if (listaInv!=null)
    									{
    										InventarioListeVO ele=(InventarioListeVO)listaInv;
    										String inv=ele.getCodSerie()+" "+ String.valueOf(ele.getCodInvent());
    										recDett.setInvRigaFatt(inv.trim());
    									}
    		*/
//    									listaInv= this.getInventario().getInventarioRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket);
    									listaInv=  DomainEJBFactory.getInstance().getInventario().getInventarioRigaFattura(codPolo, codBib,codBibO, codTipOrd,  annoOrd,  codOrd,codBibF, annoF, progF,  rigaF, locale, ticket);
    									if (listaInv!=null)
    									{
    										InventarioListeVO ele=listaInv;
    										String inv=ele.getCodSerie()+" "+ String.valueOf(ele.getCodInvent());
    										newEleFattDett.setInvRigaFatt(inv.trim());
    									}
    								} catch (Exception e) {
    									//
    									// l'errore capita in questo punto
    									log.error("", e);
    								}

    							}
    							newEleFattDett.setOrdine(new StrutturaTerna("","",""));
    							if (!String.valueOf(newEleFattDett.getIDOrd()).equals("null") && newEleFattDett.getIDOrd()>0)
    							{
    								newEleFattDett.getOrdine().setCodice1(String.valueOf(eleFattDett.getId_ordine().getCod_tip_ord()));
    								newEleFattDett.getOrdine().setCodice2(String.valueOf(eleFattDett.getId_ordine().getAnno_ord()));
    								newEleFattDett.getOrdine().setCodice3(String.valueOf(eleFattDett.getId_ordine().getCod_ord()));
    								newEleFattDett.setPrezzoOrdine(eleFattDett.getId_ordine().getPrezzo_lire().doubleValue());
    							}


    							newEleFattDett.setBilancio(new StrutturaTerna("","",""));
    							if (!String.valueOf(newEleFattDett.getIDBil()).equals("null") && newEleFattDett.getIDBil()>0)
    							{
    								newEleFattDett.getBilancio().setCodice1(String.valueOf(eleFattDett.getCod_mat().getId_capitoli_bilanci().getEsercizio()));
    								newEleFattDett.getBilancio().setCodice2(String.valueOf(eleFattDett.getCod_mat().getId_capitoli_bilanci().getCapitolo()));
    								newEleFattDett.getBilancio().setCodice3(String.valueOf(eleFattDett.getCod_mat().getCod_mat()));
    								// per gestire la visualizzazione della riga altre spese nella jsp
    								if (newEleFattDett.getBilancio().getCodice3().trim().equals("4"))
    								{
    									newEleFattDett.setCodPolo("*");

    								}

    							}

    							newEleFattDett.setNoteRigaFattura(eleFattDett.getNote());
    							newEleFattDett.setFattura(new StrutturaTerna("","",""));

    							// gestione note di credito
    							if (eleFattDett.getId_fattura().getTipo_fattura()=='N' && eleFattDett.getId_fattura_in_credito()>0)
    							{

    								ListaSuppFatturaVO ricercaFattureNC=new ListaSuppFatturaVO();
    								ricercaFattureNC.setIDFatt(eleFattDett.getId_fattura_in_credito());
    								ricercaFattureNC.setFlag_canc(false);
    								List resultFattNC = fattureDao.getRicercaListaFattureHib(ricercaFattureNC);
    				                for(int b = 0; b < resultFattNC.size(); b++)
    				                {
    				                    Tba_fatture oggFATTNC = (Tba_fatture)resultFattNC.get(b);
    				                	if (oggFATTNC.getAnno_fattura().doubleValue()>0 )
    									{
    				                		newEleFattDett.getFattura().setCodice1(String.valueOf(oggFATTNC.getAnno_fattura().doubleValue()));
    									}
    									if (oggFATTNC.getProgr_fattura()>0 )
    									{
    										newEleFattDett.getFattura().setCodice2(String.valueOf(oggFATTNC.getProgr_fattura()));
    									}
    									if (!String.valueOf(oggFATTNC.getId_fattura()).equals("null") && oggFATTNC.getId_fattura()>0)
    									{
    										newEleFattDett.setIDFattNC(oggFATTNC.getId_fattura());
    									}
    				                }

    								if (eleFattDett.getId_fattura_in_credito()>0)
    								{
    									newEleFattDett.getFattura().setCodice3(String.valueOf(eleFattDett.getId_fattura_in_credito()));
    								}
    							}
    							arrayFattDett.add(newEleFattDett);
    		                }
		                    }
		                    catch(Exception e)
		                    {
		                        log.error("", e);
		                    }
		                }

						// elimina le fatture senza righe
						if (ricercaFatture.isRicercaOrd() && arrayFattDett.size()>0)
						{
							rec.setRigheDettaglioFattura(arrayFattDett);
							listaFatture.add(rec);
							progrRighe=progrRighe+1;
						}

						if (!ricercaFatture.isRicercaOrd())
						{
							rec.setRigheDettaglioFattura(arrayFattDett);
							listaFatture.add(rec);
							progrRighe=progrRighe+1;
						}
						// fine
	                    //listaFatture.add(rec);
	                }
	            }

	        }
	        catch(ValidationException e)
	        {
	            throw e;
	        }
	        catch(Exception e)
	        {
	            log.error("", e);
	        }
		}
        Validazione.ValidaRicercaFatture(listaFatture);
        return listaFatture;

	}

	public String calcolaCartName(String ticket, String codPolo, String codBib, String cd_bib_google) throws ApplicationException, EJBException {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);
			int next = ContatoriDAO.getNextValue(ticket, codPolo, codBib, Constants.PROGRESSIVO_CART_NAME_GOOGLE, 0, null, true, 999);
			String cartName = OrdiniUtil.formattaCartName(cd_bib_google, next);
			DaoManager.commit(tx);

			return cartName;

		} catch (Exception e) {
			DaoManager.rollback(tx);
			throw new ApplicationException(SbnErrorTypes.ERROR_DB_READ_SEQUENCE);
		}
	}
}


