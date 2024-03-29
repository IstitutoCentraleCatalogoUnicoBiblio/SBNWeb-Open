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
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.domain.servizi.ServiziCommon;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.utils.isbd.IsbdTokenizer;
import it.iccu.sbn.ejb.utils.isbd.IsbdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SchedaInventarioInputVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloBollettinoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.documentofisico.NotaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionesemantica.CountLegamiSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBollettinoDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBollettinoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBuoniCaricoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroConservazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroIngressoDettaglioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroIngressoLogVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroTopograficoCollocazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroTopograficoInventarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroTopograficoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStatisticheRegistroDettaglioAcqVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStatisticheRegistroDettaglioMatVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStatisticheRegistroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_utenti_professionali_webDao;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_default_inven_schedeDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_nota_invDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_provenienza_inventarioDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_serie_inventarialeDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_sezione_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbf_modelli_stampeDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_formati_sezioniDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_poss_prov_inventariDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.periodici.PeriodiciDAO;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.polo.orm.bibliografica.Tb_autore;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_default_inven_schede;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_nota_inv;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.TitoloHibernate;
import it.iccu.sbn.util.Constants.DocFisico;
import it.iccu.sbn.util.jasper.BatchCollectionSerializer;
import it.iccu.sbn.util.validation.ValidazioniDocumentoFisico;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="InventarioBMT" description="A session bean named InventarioBMT"
 *           display-name="InventarioBMT" jndi-name="sbnWeb/InventarioBMT"
 *           type="Stateless" transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class InventarioBMTBean extends TicketChecker implements InventarioBMT {

	private static final long serialVersionUID = 4743067947902216854L;

	private ServiziBibliografici bibliografica;
	private ServiziCommon servizi;
	private Acquisizioni acquisizioni;
	private DocumentoFisicoCommon dfCommon;
	private Codici codici;
	private Semantica semantica;

	private Tbf_bibliotecaDao daoBiblio;

	private Tbf_biblioteca_in_poloDao daoBib;

	private Tbc_sezione_collocazioneDao daoSez;

	private Tbc_serie_inventarialeDao daoSerie;

	private Tbc_inventarioDao daoInv;

	private Tbc_collocazioneDao daoColl;

	private Tbc_provenienza_inventarioDao daoProven;

	private Tbf_modelli_stampeDao daoModello;

	private Tbc_default_inven_schedeDao daoModelloDefault;

	private GenericJDBCAcquisizioniDAO daoAcquisizioni;

	private Trc_poss_prov_inventariDao daoPri;

	private ValidazioniDocumentoFisico valida;

	private Trc_formati_sezioniDao daoForSez;

	private Tbc_nota_invDao daoNotaInv;

	private SemanticaDAO daoSemantica;

	Connection connection;

	private SessionContext context;

	private static Logger log = Logger.getLogger(InventarioBMTBean.class);


	public InventarioBMTBean() {
		valida = new ValidazioniDocumentoFisico();
		connection = null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
	}

	public void setSessionContext(SessionContext context) throws EJBException,
	RemoteException {
		daoInv = new Tbc_inventarioDao();
		daoSerie = new Tbc_serie_inventarialeDao();
		daoColl = new Tbc_collocazioneDao();
		daoProven = new Tbc_provenienza_inventarioDao();
		daoAcquisizioni = new GenericJDBCAcquisizioniDAO();
		daoBib = new Tbf_biblioteca_in_poloDao();
		daoBiblio = new Tbf_bibliotecaDao();

		this.context = context;
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			bibliografica = factory.getSrvBibliografica();
			servizi = factory.getServiziCommon();
			acquisizioni = factory.getAcquisizioni();
			dfCommon = factory.getDocumentoFisicoCommon();
			codici = factory.getCodici();
			semantica = factory.getSemantica();
		} catch (NamingException e) {

		} catch (CreateException e) {

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
	public List getListaInventariTitolo(String codPolo, String codBib,
			String bid, String ticket) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		checkTicket(ticket);
		List listaInventariTitolo = null;
		List listaTit = new ArrayList();
		Tbc_collocazione collocazione = null;
		try {
			InventarioVO inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setBid(bid);
			valida.validaCodPoloCodBibBid(codPolo, codBib, bid);
			Tb_titolo titolo = daoInv.getTitoloDF1(bid);
			if (titolo == null) {
				throw new DataException("titoloNonTrovatoSuBDPolo");
			}
			List<Tbc_inventario> listaInv = new ArrayList<Tbc_inventario>();
			InventarioTitoloVO rec = null;
			daoInv =  new Tbc_inventarioDao();
			if (codBib.equals("")) {
				listaInv = daoInv.getListaInventariTitolo(bid);
			} else {
				listaInv = daoInv.getListaInventariTitolo(codPolo, codBib, bid);
			}
			if (listaInv.size() > 0) {
				listaInventariTitolo = new ArrayList();
				int countInv = 0;
				for (int index = 0; index < listaInv.size(); index++) {
					rec = new InventarioTitoloVO();
					Tbc_inventario recResult = listaInv.get(index);
					rec.setPrg(++countInv);
					//almaviva5_20101025 gest. periodici
					rec.setCodPolo(recResult.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
					rec.setCodBibO(recResult.getCd_bib_ord());
					rec.setAnnoOrd(recResult.getAnno_ord() != null ? recResult.getAnno_ord().toString() : null);
					rec.setCodOrd(recResult.getCd_ord() != null ? recResult.getCd_ord().toString() : null);
					rec.setCodTipoOrd(recResult.getCd_tip_ord() != null ? recResult.getCd_tip_ord().toString() : null);
					//
					rec.setCodBib(recResult.getCd_serie().getCd_polo()
							.getCd_biblioteca());
					rec.setCodSerie(recResult.getCd_serie().getCd_serie());
					rec.setCodInvent(recResult.getCd_inven());
					if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_PRECISATO_CHR) {
						rec.setSitAmm("precisato");
					}else if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
						rec.setSitAmm("collocato");
					}else{
						countInv--;
						continue;
						//						rec.setSitAmm("dismesso");
					}
					//					rec.setCodSit(String.valueOf(recResult.getCd_sit()));
					rec.setSeqColl(recResult.getSeq_coll());
					if (recResult.getPrecis_inv().trim().length() > 80){
						rec.setPrecInv(recResult.getPrecis_inv().substring(0, 80).trim());
					}else{
						if (recResult.getPrecis_inv().trim().equals("$")){
							rec.setPrecInv("");
						}else{
							rec.setPrecInv(recResult.getPrecis_inv().trim());
						}
					}
					rec.setCodFrui(recResult.getCd_frui());
					rec.setCodNoDisp(recResult.getCd_no_disp().trim());
					if (recResult.getKey_loc() != null) {
						if (recResult.getKey_loc().getCd_biblioteca_doc() != null
								&& recResult.getKey_loc().getCd_biblioteca_doc().getFl_canc() != 'S'){
							rec.setFlEsempl("E");
						}else{
							rec.setFlEsempl("");
						}
						rec.setKeyLoc(recResult.getKey_loc().getKey_loc());
						rec.setCodSez(recResult.getKey_loc().getCd_sez()
								.getCd_sez());
						rec.setCodLoc(recResult.getKey_loc().getCd_loc());
						rec.setSpecLoc(recResult.getKey_loc().getSpec_loc());
						if (recResult.getKey_loc().getConsis() == null ||
								(recResult.getKey_loc().getConsis() != null && recResult.getKey_loc().getConsis().trim().equals("$"))){
							rec.setConsistenza("");
						}else{
							rec.setConsistenza(recResult.getKey_loc().getConsis());
						}
					} else {
						rec.setKeyLoc(0);
						rec.setCodSez("");
						rec.setCodLoc("");
						rec.setSpecLoc("");
						rec.setConsistenza("");
					}
					rec.setBid(recResult.getB().getBid());
					rec.setSezOld(recResult.getSez_old());
					rec.setLocOld(recResult.getLoc_old());
					rec.setSpecOld(recResult.getSpec_old());
					//almaviva5_20101021 gest. periodici
					rec.setAnnoAbb(recResult.getAnno_abb() != null ? recResult.getAnno_abb().toString() : null);
					if (recResult.getCd_no_disp().trim().equals("")){
						rec.setDisponibilita("");
					}else{
						rec.setDisponibilita(codici.cercaDescrizioneCodice(String.valueOf(recResult.getCd_no_disp().trim()),
								CodiciType.CODICE_NON_DISPONIBILITA,
								CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					if (recResult.getCd_frui().trim().equals("")){
						rec.setFruizione("");
					}else{
						rec.setFruizione(codici.cercaDescrizioneCodice(String.valueOf(recResult.getCd_frui().trim()),
								CodiciType.CODICE_CATEGORIA_FRUIZIONE,
								CodiciRicercaType.RICERCA_CODICE_SBN));
					}
					listaInventariTitolo.add(rec);
				}

			}else{
				throw new DataException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO, "noInv");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInventariTitolo;
	}

	public List getListaInventariTitolo(String codPolo, List<String> listaBiblio,
			String bid, String ticket) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		checkTicket(ticket);
		List listaInventariTitolo = null;
		List listaTit = new ArrayList();
		Tbc_collocazione collocazione = null;
		try {
			valida.validaCodPoloListaBilioBid(codPolo, listaBiblio, bid);
			Tb_titolo titolo = daoInv.getTitoloDF1(bid);
			if (titolo == null) {
				throw new DataException("titoloNonTrovatoSuBDPolo");
			}
			List listaInv = new ArrayList();
			InventarioTitoloVO rec = null;
			if (listaBiblio.size() > 0) {
				int countInv = 0;
				listaInventariTitolo = new ArrayList();
				for (int index1 = 0; index1 < listaBiblio.size(); index1++) {
					String codBib = listaBiblio.get(index1);
					daoInv =  new Tbc_inventarioDao();
					listaInv = daoInv.getListaInventariTitoloPerServizi(codPolo, codBib, bid);
					if (listaInv.size() > 0) {
						for (int index = 0; index < listaInv.size(); index++) {
							rec = new InventarioTitoloVO();
							Tbc_inventario recResult = (Tbc_inventario)listaInv.get(index);
							rec.setPrg(++countInv);
							rec.setCodBib(recResult.getCd_serie().getCd_polo()
									.getCd_biblioteca());
							rec.setCodSerie(recResult.getCd_serie().getCd_serie());
							rec.setCodInvent(recResult.getCd_inven());
							if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_PRECISATO_CHR) {
								rec.setSitAmm("precisato");
							}else if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
								rec.setSitAmm("collocato");
							}else{
								countInv--;
								continue;
								//						rec.setSitAmm("dismesso");
							}
							//					rec.setCodSit(String.valueOf(recResult.getCd_sit()));
							rec.setSeqColl(recResult.getSeq_coll());
							if (recResult.getPrecis_inv().trim().length() > 80){
								rec.setPrecInv(recResult.getPrecis_inv().substring(0, 80).trim());
							}else{
								if (recResult.getPrecis_inv().trim().equals("$")){
									rec.setPrecInv("");
								}else{
									rec.setPrecInv(recResult.getPrecis_inv().trim());
								}
							}
							rec.setCodFrui(recResult.getCd_frui());
							rec.setCodNoDisp(recResult.getCd_no_disp().trim());
							if (recResult.getKey_loc() != null) {
								if (recResult.getKey_loc().getCd_biblioteca_doc() != null
										&& recResult.getKey_loc().getCd_biblioteca_doc().getFl_canc() != 'S'){
									rec.setFlEsempl("E");
								}else{
									rec.setFlEsempl("");
								}
								rec.setKeyLoc(recResult.getKey_loc().getKey_loc());
								rec.setCodSez(recResult.getKey_loc().getCd_sez()
										.getCd_sez());
								rec.setCodLoc(recResult.getKey_loc().getCd_loc());
								rec.setSpecLoc(recResult.getKey_loc().getSpec_loc());
								if (recResult.getKey_loc().getConsis() == null ||
										(recResult.getKey_loc().getConsis() != null && recResult.getKey_loc().getConsis().trim().equals("$"))){
									rec.setConsistenza("");
								}else{
									rec.setConsistenza(recResult.getKey_loc().getConsis());
								}
							} else {
								rec.setKeyLoc(0);
								rec.setCodSez("");
								rec.setCodLoc("");
								rec.setSpecLoc("");
								rec.setConsistenza("");
							}
							rec.setBid(recResult.getB().getBid());
							rec.setSezOld(recResult.getSez_old());
							rec.setLocOld(recResult.getLoc_old());
							rec.setSpecOld(recResult.getSpec_old());
							if (recResult.getCd_no_disp().trim().equals("")){
								rec.setDisponibilita("");
							}else{
								rec.setDisponibilita(codici.cercaDescrizioneCodice(String.valueOf(recResult.getCd_no_disp().trim()),
										CodiciType.CODICE_NON_DISPONIBILITA,
										CodiciRicercaType.RICERCA_CODICE_SBN));
							}
							if (recResult.getCd_frui().trim().equals("")){
								rec.setFruizione("");
							}else{
								rec.setFruizione(codici.cercaDescrizioneCodice(String.valueOf(recResult.getCd_frui().trim()),
										CodiciType.CODICE_CATEGORIA_FRUIZIONE,
										CodiciRicercaType.RICERCA_CODICE_SBN));
							}
							listaInventariTitolo.add(rec);
						}
					}
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		if (listaInventariTitolo != null && listaInventariTitolo.size() > 0){
			return listaInventariTitolo;
		}else{
			throw new DataException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO, "noInv");
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
	public List getListaInventariCollocati(String codPolo, String codBib,
			String bid, String ticket) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		checkTicket(ticket);
		List listaInventariTitolo = null;
		try {
			InventarioVO inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setBid(bid);
			valida.validaCodPoloCodBibBid(codPolo, codBib, bid);
			Tb_titolo titolo = daoInv.getTitoloDF1(bid);
			if (titolo == null) {
				throw new DataException("titoloNonTrovatoSuBDPolo");
			}
			List listaInv = new ArrayList();
			InventarioTitoloVO rec = null;
			daoInv =  new Tbc_inventarioDao();
			listaInv = daoInv.getListaInventari(codPolo, codBib, bid);
			if (listaInv.size() > 0) {
				listaInventariTitolo = new ArrayList();
				for (int index = 0; index < listaInv.size(); index++) {
					rec = new InventarioTitoloVO();
					Tbc_inventario recResult = (Tbc_inventario)listaInv.get(index);
					if (recResult.getKey_loc() != null) {
						rec.setPrg(index + 1);
						rec.setCodBib(recResult.getCd_serie().getCd_polo()
								.getCd_biblioteca());
						rec.setCodSerie(recResult.getCd_serie().getCd_serie());
						rec.setCodInvent(recResult.getCd_inven());
						rec.setSeqColl(recResult.getSeq_coll());
						rec.setPrecInv(recResult.getPrecis_inv());
						rec.setCodFrui(recResult.getCd_frui());
						rec.setCodNoDisp(recResult.getCd_no_disp().trim());
						rec.setKeyLoc(recResult.getKey_loc().getKey_loc());
						rec.setCodSez(recResult.getKey_loc().getCd_sez()
								.getCd_sez());
						rec.setCodLoc(recResult.getKey_loc().getCd_loc());
						rec.setSpecLoc(recResult.getKey_loc().getSpec_loc());
						if (recResult.getKey_loc().getConsis() == null ||
								(recResult.getKey_loc().getConsis() != null && recResult.getKey_loc().getConsis().trim().equals("$"))){
							rec.setConsistenza("");
						}else{
							rec.setConsistenza(recResult.getKey_loc().getConsis());
						}
						rec.setBid(recResult.getB().getBid());
						rec.setSezOld(recResult.getSez_old());
						rec.setLocOld(recResult.getLoc_old());
						rec.setSpecOld(recResult.getSpec_old());
						listaInventariTitolo.add(rec);
					}else{
						continue;
					}
				}
			}else{
				throw new DataException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO, "noInv");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInventariTitolo;
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
	public List getListaInventariPossessori(String codPolo, String codBib,
			String bid, String ticket, EsameCollocRicercaVO paramRicerca) throws RemoteException,
			ApplicationException, DataException, ValidationException {
		checkTicket(ticket);
		List listaInventariPossessori = null;
		InventarioListeVO rec = null;
		Integer numPoss = null;
		String ultCodSerie = null;
		String ultCodInv = null;
		String giriSuccessivi = null;
		if (paramRicerca.getUltLoc() != null && paramRicerca.getUltSpec() != null){
			//ultCodSerie e ultCodInv
			giriSuccessivi = "SI";
			log.info("ultimo codLoc:" + paramRicerca.getUltLoc());

			log.info("ultimo codSpec:" + paramRicerca.getUltSpec());
		}
		List<Trc_poss_prov_inventari> listaInvPoss = null;
		try {
			InventarioVO inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setBid(bid);
			valida.validaInventario(inv, null, 0);
			daoPri = new Trc_poss_prov_inventariDao();
			if (codBib.equals("")) {
				if (giriSuccessivi == null){
					numPoss = daoPri.countInventariPerPossessore(bid);
				}else{
					numPoss = daoPri.countInventariPerPossessore(bid, paramRicerca.getUltLoc(), paramRicerca.getUltSpec());
				}
			} else {
				if (giriSuccessivi == null){
					numPoss = daoPri.countInventariPerPossessoreBib(codPolo, codBib, bid);
				}else{
					numPoss = daoPri.countInventariPerPossessoreBib(codPolo, codBib, bid, paramRicerca.getUltLoc(), paramRicerca.getUltSpec());
				}
			}
			if (numPoss != null && numPoss > 0){
				daoInv = new Tbc_inventarioDao();
				if (codBib.equals("")) {
					if (giriSuccessivi == null){
						listaInvPoss = daoPri.getListaInventariPossessori(bid, paramRicerca.getNuovoLimiteRicerca());
					}else{
						listaInvPoss = daoPri.getListaInventariPossessori(bid, paramRicerca.getNuovoLimiteRicerca(),
								paramRicerca.getUltLoc(), paramRicerca.getUltSpec());
					}
				} else {
					if (giriSuccessivi == null){
						listaInvPoss = daoPri.getListaInventariPossessoriBib(codPolo, codBib, bid, paramRicerca.getNuovoLimiteRicerca());
					}else{
						listaInvPoss = daoPri.getListaInventariPossessoriBib(codPolo, codBib, bid,
								paramRicerca.getNuovoLimiteRicerca(), paramRicerca.getUltLoc(), paramRicerca.getUltSpec());
					}
				}
				if (listaInvPoss.size() > 0) {
					listaInventariPossessori = new ArrayList();
					for (int index = 0; index < listaInvPoss.size(); index++) {
						Trc_poss_prov_inventari recResult = listaInvPoss.get(index);
						rec = new InventarioListeVO();

						rec.setPrg(index + 1);
						//						Tbc_inventario inven = recResult.getCd_polo();
						//						Tbc_serie_inventariale cdSerie = inven.getCd_serie();
						//						if (!codBib.equals("")) {
						//							Tbf_biblioteca_in_polo bib = cdSerie.getCd_polo();
						//							rec.setCodBib(bib.getCd_biblioteca());
						//						}
						//						rec.setCodSerie(cdSerie.getCd_serie());
						//						rec.setCodInvent(inven.getCd_inven());
						rec.setCodSerie(recResult.getCd_polo().getCd_serie().getCd_serie());
						rec.setCodInvent(recResult.getCd_polo().getCd_inven());
						if (recResult.getCd_polo().getCd_sit() == DocFisico.Inventari.INVENTARIO_PRECISATO_CHR) {
							rec.setSitAmm("precisato");
						}else if (recResult.getCd_polo().getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
							rec.setSitAmm("collocato");
						}else{
							rec.setSitAmm("dismesso");
						}
						Tbc_collocazione coll = recResult.getCd_polo().getKey_loc();
						if (coll != null) {
							rec.setKeyLoc(coll.getKey_loc());
							rec.setCodSez(coll.getCd_sez().getCd_sez());
							rec.setCodLoc(coll.getCd_loc());
							rec.setCodSpec(coll.getSpec_loc());
							rec.setNotaColl(recResult.getNota());
							if(recResult.getCd_legame() ==('R')){
								rec.setLegame("Provenienza");
							}else if(recResult.getCd_legame() ==('P')){
								rec.setLegame("Possessore");
							}
						} else {
							rec.setCodSez("");
							rec.setCodLoc("");
							rec.setCodSpec("");
							rec.setCodColl("");
							rec.setNotaColl("");
						}
						rec.setSeqColl(recResult.getCd_polo().getSeq_coll());
						rec.setPrecInv(recResult.getCd_polo().getPrecis_inv());
						Tb_titolo titolo = recResult.getCd_polo().getB();
						if (titolo != null){
							rec.setBid(titolo.getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitoloHib(titolo, ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO tit = (TitoloVO)titoli.get(0);
									rec.setDescr(tit.getIsbd());
								}
							}else{
								rec.setDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
						rec.setNatura(""+titolo.getCd_natura());
						//
						//
						listaInventariPossessori.add(rec);
						if (listaInvPoss.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
							ultCodSerie = recResult.getCd_polo().getCd_serie().getCd_serie();
							log.info("ultimo codSerie:" + ultCodSerie);
							ultCodInv = String.valueOf(recResult.getCd_polo().getCd_inven());
							log.info("ultimo codInv:" + ultCodInv);
						}
					}
				}

				if (listaInventariPossessori != null){
					if (numPoss > paramRicerca.getNuovoLimiteRicerca()){
						CodiceVO estremiListaCollIncompleta = new CodiceVO();
						estremiListaCollIncompleta.setNumero(numPoss);
						estremiListaCollIncompleta.setCodice(ultCodSerie);
						estremiListaCollIncompleta.setTerzo(ultCodInv);
						listaInventariPossessori.add(estremiListaCollIncompleta);
					}else{
						return listaInventariPossessori;
					}
				}
			}else{
				throw new DataException(SbnErrorTypes.GDF_INVENTARIO_NON_TROVATO, "noInv");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInventariPossessori;
	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaSerie(String codPolo, String codiceBiblioteca,
			String ticket) throws ResourceNotFoundException,
			ApplicationException, ValidationException, RemoteException {
		checkTicket(ticket);
		List listaSerie = null;
		try {
			CodiceVO serie = new CodiceVO();
			serie.setCodice(codPolo);
			serie.setDescrizione(codiceBiblioteca);
			valida.validaPoloBib(serie);
			List listaSer = new ArrayList();
			SerieVO rec = null;
			daoSerie = new Tbc_serie_inventarialeDao();
			listaSer = daoSerie.getListaSerie(codPolo, codiceBiblioteca);
			if (listaSer.size() > 0) {
				listaSerie = new ArrayList();
				for (int index = 0; index < listaSer.size(); index++) {
					Tbc_serie_inventariale recResult = (Tbc_serie_inventariale)listaSer.get(index);
					rec = new SerieVO();
					rec.setPrg(index + 1);
					rec.setCodSerie(recResult.getCd_serie());
					rec.setProgAssInv(String.valueOf(recResult.getPrg_inv_corrente()));
					rec.setPregrAssInv(String.valueOf(recResult.getPrg_inv_pregresso()));
					if (recResult.getDescr().trim().equals("$")) {
						rec.setDescrSerie("");
					} else {
						rec.setDescrSerie(recResult.getDescr());
					}
					rec.setNumMan(String.valueOf(recResult.getNum_man()));
					listaSerie.add(rec);
				}

			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaSerie;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaSerieDate(String codPolo, String codiceBiblioteca,
			String ticket) throws ResourceNotFoundException,
			ApplicationException, ValidationException, RemoteException {
		checkTicket(ticket);
		List listaSerie = null;
		try {
			CodiceVO serie = new CodiceVO();
			serie.setCodice(codPolo);
			serie.setDescrizione(codiceBiblioteca);
			valida.validaPoloBib(serie);
			List listaSer = new ArrayList();
			SerieVO rec = null;
			daoSerie = new Tbc_serie_inventarialeDao();
			listaSer = daoSerie.getListaSerie(codPolo, codiceBiblioteca);
			if (listaSer.size() > 0) {
				listaSerie = new ArrayList();
				for (int index = 0; index < listaSer.size(); index++) {
					Tbc_serie_inventariale recResult = (Tbc_serie_inventariale) listaSer.get(index);
					rec = new SerieVO();
					rec.setCodSerie(recResult.getCd_serie());
					rec.setProgAssInv(String.valueOf(recResult.getPrg_inv_corrente()));
					//
					rec.setNumMan(String.valueOf(recResult.getNum_man()));
					if (recResult.getDt_ingr_inv_man() != null){
						rec.setDataIngrMan(DateUtil.formattaData(recResult.getDt_ingr_inv_man()));
					}else{
						rec.setDataIngrMan(null);
					}
					rec.setInizioMan1(String.valueOf(recResult.getInizio_man()));
					//almaviva5_20081126
					rec.setInizioMan2(recResult.getInizio_man2() != null ? String.valueOf(recResult.getInizio_man2()) : "0");
					rec.setInizioMan3(recResult.getInizio_man3() != null ? String.valueOf(recResult.getInizio_man3()) : "0");
					rec.setInizioMan4(recResult.getInizio_man4() != null ? String.valueOf(recResult.getInizio_man4()) : "0");
					rec.setFineMan1(String.valueOf(recResult.getFine_man()));
					rec.setFineMan2(recResult.getFine_man2() != null ? String.valueOf(recResult.getFine_man2()) : "0");
					rec.setFineMan3(recResult.getFine_man3() != null ? String.valueOf(recResult.getFine_man3()) : "0");
					rec.setFineMan4(recResult.getFine_man4() != null ? String.valueOf(recResult.getFine_man4()) : "0");
					if (recResult.getDt_ingr_inv_man() != null){
						rec.setDataIngrMan(DateUtil.formattaData(recResult.getDt_ingr_inv_man()));
					}else{
						rec.setDataIngrMan(null);
					}
					if (recResult.getDt_ingr_inv_ris1() != null){
						rec.setDataIngrRis1(DateUtil.formattaData(recResult.getDt_ingr_inv_ris1()));
					}else{
						rec.setDataIngrRis1(null);
					}
					if (recResult.getDt_ingr_inv_ris2() != null){
						rec.setDataIngrRis2(DateUtil.formattaData(recResult.getDt_ingr_inv_ris2()));
					}else{
						rec.setDataIngrRis2(null);
					}
					if (recResult.getDt_ingr_inv_ris3() != null){
						rec.setDataIngrRis3(DateUtil.formattaData(recResult.getDt_ingr_inv_ris3()));
					}else{
						rec.setDataIngrRis3(null);
					}
					if (recResult.getDt_ingr_inv_ris4() != null){
						rec.setDataIngrRis4(DateUtil.formattaData(recResult.getDt_ingr_inv_ris4()));
					}else{
						rec.setDataIngrRis4(null);
					}

					rec.setPregrAssInv(String.valueOf(recResult.getPrg_inv_pregresso()));
					if (recResult.getDt_ingr_inv_preg() != null){
						rec.setDataIngrPregr(DateUtil.formattaData(recResult.getDt_ingr_inv_preg()));
					}else{
						rec.setDataIngrPregr(null);
					}

					if (recResult.getDescr().trim().equals("$")) {
						rec.setDescrSerie("");
					} else {
						rec.setDescrSerie(recResult.getDescr());
					}
					rec.setNumMan(String.valueOf(recResult.getNum_man()));
					listaSerie.add(rec);
				}

			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaSerie;
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public SerieVO getSerieDettaglio(String codPolo, String codBib,
			String codSerie, String ticket) throws DataException,
			ApplicationException, ValidationException {
		checkTicket(ticket);

		SerieVO dettaglio = null;
		Tbc_serie_inventariale rec = null;

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			DaoManager.begin(tx);
			log.debug("getSerieDettaglio(): Inizio transazione");

			valida.validaCodPoloCodBibSerie(codPolo, codBib, codSerie);
			rec = daoSerie.getSerie(codPolo, codBib, codSerie.toUpperCase());
			if (rec != null) {
				dettaglio = new SerieVO();
				dettaglio.setCodPolo(rec.getCd_polo().getCd_polo().getCd_polo());
				dettaglio.setCodBib(rec.getCd_polo().getCd_biblioteca());
				dettaglio.setCodSerie(rec.getCd_serie());
				if (rec.getDescr().trim().equals("$")) {
					dettaglio.setDescrSerie("");
				} else {
					dettaglio.setDescrSerie(rec.getDescr().trim());
				}
				if (rec.getDt_ingr_inv_preg() != null){
					dettaglio.setDataIngrPregr(DateUtil.formattaData(rec.getDt_ingr_inv_preg()));
				}else{
					dettaglio.setDataIngrPregr("");
				}
				if (rec.getDt_ingr_inv_man() != null){
					dettaglio.setDataIngrMan(DateUtil.formattaData(rec.getDt_ingr_inv_man()));
				}else{
					dettaglio.setDataIngrMan("");
				}
				dettaglio.setFlDefault(String.valueOf(rec.getFl_default()));
				if (rec.getInizio_man() > 0){
					dettaglio.setInizioMan1(String.valueOf(rec.getInizio_man()));
				}else{
					dettaglio.setInizioMan1(String.valueOf(0));
				}
				if (rec.getFine_man() > 0){
					dettaglio.setFineMan1(String.valueOf(rec.getFine_man()));
				}else{
					dettaglio.setFineMan1(String.valueOf(0));
				}
				if (rec.getDt_ingr_inv_ris1() != null){
					dettaglio.setDataIngrRis1(DateUtil.formattaData(rec.getDt_ingr_inv_ris1()));
				}else{
					dettaglio.setDataIngrRis1("");
				}
				if (rec.getInizio_man2() != null){
					dettaglio.setInizioMan2(String.valueOf(rec.getInizio_man2()));
				}else{
					dettaglio.setInizioMan2(String.valueOf(0));
				}
				if (rec.getFine_man2() != null){
					dettaglio.setFineMan2(String.valueOf(rec.getFine_man2()));
				}else{
					dettaglio.setFineMan2(String.valueOf(0));
				}
				if (rec.getDt_ingr_inv_ris2() != null){
					dettaglio.setDataIngrRis2(DateUtil.formattaData(rec.getDt_ingr_inv_ris2()));
				}else{
					dettaglio.setDataIngrRis2("");
				}
				if (rec.getInizio_man3()  != null){
					dettaglio.setInizioMan3(String.valueOf(rec.getInizio_man3()));
				}else{
					dettaglio.setInizioMan3(String.valueOf(0));
				}
				if (rec.getFine_man3()  != null){
					dettaglio.setFineMan3(String.valueOf(rec.getFine_man3()));
				}else{
					dettaglio.setFineMan3(String.valueOf(0));
				}

				if (rec.getDt_ingr_inv_ris3() != null){
					dettaglio.setDataIngrRis3(DateUtil.formattaData(rec.getDt_ingr_inv_ris3()));
				}else{
					dettaglio.setDataIngrRis3("");
				}
				if (rec.getInizio_man4()  != null){
					dettaglio.setInizioMan4(String.valueOf(rec.getInizio_man4()));
				}else{
					dettaglio.setInizioMan4(String.valueOf(0));
				}
				if (rec.getFine_man4()  != null){
					dettaglio.setFineMan4(String.valueOf(rec.getFine_man4()));
				}else{
					dettaglio.setFineMan4(String.valueOf(0));
				}

				if (rec.getDt_ingr_inv_ris4() != null){
					dettaglio.setDataIngrRis4(DateUtil.formattaData(rec.getDt_ingr_inv_ris4()));
				}else{
					dettaglio.setDataIngrRis4("");
				}
				dettaglio.setFlChiusa(String.valueOf(rec.getFlg_chiusa()));
				dettaglio.setProgAssInv(String.valueOf(rec.getPrg_inv_corrente()));
				dettaglio.setPregrAssInv(String.valueOf(rec.getPrg_inv_pregresso()));
				dettaglio.setNumMan(String.valueOf(rec.getNum_man()));
				dettaglio.setInizioMan1(String.valueOf(rec.getInizio_man()));
				dettaglio.setFineMan1(String.valueOf(rec.getFine_man()));
				dettaglio.setUteIns(rec.getUte_ins());
				dettaglio.setDataIns(DateUtil.formattaDataCompletaTimestamp(rec.getTs_ins()));
				dettaglio.setUteAgg(rec.getUte_var());
				dettaglio.setDataAgg(DateUtil.formattaDataCompletaTimestamp(rec.getTs_var()));

				tx.commit();
				ok = true;
				log.debug("getSerieDettaglio(): Commit transazione");

			} else {
				throw new ValidationException("recInesistente");
			}

		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
			if (!ok)
				log.error("getSerieDettaglio(): Rollback transazione");
		}

		return dettaglio;
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean insertSerie(SerieVO serie, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);

		boolean ret = false;
		Tbc_serie_inventariale recSerie = null;

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			DaoManager.begin(tx);
			log.debug("insertSerie(): Inizio transazione");

			valida.validaSerie(serie);
			recSerie = daoSerie.getSerie(serie.getCodPolo(),
					serie.getCodBib(), ValidazioneDati.trimOrEmpty(serie.getCodSerie()).toUpperCase());
			if (recSerie != null) {
				throw new ValidationException("recEsistente");
			} else {
				Timestamp ts = DaoManager.now();
				recSerie = new Tbc_serie_inventariale();

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(serie.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(serie.getCodBib());
				bib.setCd_polo(polo);

				recSerie.setCd_polo(bib);
				recSerie.setCd_serie(ValidazioneDati.trimOrEmpty(serie.getCodSerie()).toUpperCase());
				if (serie.getDescrSerie().trim().equals("")){
					recSerie.setDescr("$");
				}else{
					recSerie.setDescr(serie.getDescrSerie());
				}
				recSerie.setPrg_inv_corrente(Integer.parseInt(serie.getProgAssInv()));
				recSerie.setPrg_inv_pregresso(Integer.parseInt(serie.getPregrAssInv()));
				recSerie.setDt_ingr_inv_preg(DateUtil.toDate(serie.getDataIngrPregr()));
				recSerie.setNum_man(Integer.parseInt(serie.getNumMan()));
				recSerie.setDt_ingr_inv_man(DateUtil.toDate(serie.getDataIngrMan()));
				recSerie.setFlg_chiusa((serie.getFlChiusa().charAt(0)));
				recSerie.setFl_default((serie.getFlDefault().charAt(0)));
				//
				recSerie.setInizio_man(Integer.parseInt(serie.getInizioMan1()));
				recSerie.setFine_man(Integer.parseInt(serie.getFineMan1()));
				recSerie.setDt_ingr_inv_ris1(DateUtil.toDate(serie.getDataIngrRis1()));
				recSerie.setInizio_man2(Integer.parseInt(serie.getInizioMan2()));
				recSerie.setFine_man2(Integer.parseInt(serie.getFineMan2()));
				recSerie.setDt_ingr_inv_ris2(DateUtil.toDate(serie.getDataIngrRis2()));
				recSerie.setInizio_man3(Integer.parseInt(serie.getInizioMan3()));
				recSerie.setFine_man3(Integer.parseInt(serie.getFineMan3()));
				recSerie.setDt_ingr_inv_ris3(DateUtil.toDate(serie.getDataIngrRis3()));
				recSerie.setInizio_man4(Integer.parseInt(serie.getInizioMan4()));
				recSerie.setFine_man4(Integer.parseInt(serie.getFineMan4()));
				recSerie.setDt_ingr_inv_ris4(DateUtil.toDate(serie.getDataIngrRis4()));
				recSerie.setTs_ins(ts);
				recSerie.setTs_var(ts);
				recSerie.setUte_ins(serie.getUteIns());
				recSerie.setUte_var(serie.getUteAgg());
				recSerie.setFl_canc('N');
				daoSerie = new Tbc_serie_inventarialeDao();
				ret = daoSerie.inserimentoSerie(recSerie);

				tx.commit();
				ok = true;
				log.debug("insertSerie(): Commit transazione");

				return ret;
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
			if (!ok)
				log.error("insertSerie(): Rollback transazione");
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean updateSerie(SerieVO serie, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		try {
			valida.validaSerie(serie);
			Tbc_serie_inventariale recSerie = daoSerie.getSerie(serie
					.getCodPolo(), serie.getCodBib(), serie.getCodSerie());
			if (recSerie != null) {
				if (!serie.getDataAgg().equals(DateUtil.formattaDataCompletaTimestamp(recSerie.getTs_var()))){
					throw new ValidationException("erroreSerieModDaAltroUtente",
							ValidationException.errore);
				}else{
					if (serie.getDescrSerie().trim().equals("")) {
						recSerie.setDescr("$");
					} else {
						recSerie.setDescr(serie.getDescrSerie());
					}
					//					if (Integer.parseInt(serie.getProgAssInv()) < recSerie.getPrg_inv_corrente()
					//							|| Integer.parseInt(serie.getPregrAssInv()) < recSerie.getPrg_inv_pregresso()) {
					//						throw new ValidationException("progrPregrNonPuoEssereMinoreDellUltimoAssegnato",
					//								ValidationException.errore);
					//					} else {
					if(Integer.parseInt(serie.getProgAssInv()) < recSerie.getPrg_inv_corrente()){
						throw new ValidationException("progrAutNonPuoEssereMinoreDellUltimoAssegnato");
					}
					//progressivo automatico pregresso
					if (serie.getPregrAssInv() != null && recSerie.getPrg_inv_pregresso() > 0){
						if(Integer.parseInt(serie.getPregrAssInv() ) < recSerie.getPrg_inv_pregresso()){
							throw new DataException("progrPregrNonPuoEssereMinoreDellUltimoAssegnato");
						}
					}
					recSerie.setPrg_inv_corrente(Integer.parseInt(serie.getProgAssInv()));
					recSerie.setPrg_inv_pregresso(Integer.parseInt(serie.getPregrAssInv()));
					recSerie.setDt_ingr_inv_preg(DateUtil.toDate(serie.getDataIngrPregr()));
					recSerie.setNum_man(Integer.parseInt(serie.getNumMan()));
					recSerie.setDt_ingr_inv_man(DateUtil.toDate(serie.getDataIngrMan()));
					recSerie.setFlg_chiusa((serie.getFlChiusa().charAt(0)));
					recSerie.setFl_default((serie.getFlDefault().charAt(0)));
					//
					recSerie.setInizio_man(Integer.parseInt(serie.getInizioMan1()));
					recSerie.setFine_man(Integer.parseInt(serie.getFineMan1()));
					recSerie.setDt_ingr_inv_ris1(DateUtil.toDate(serie.getDataIngrRis1()));
					recSerie.setInizio_man2(Integer.parseInt(serie.getInizioMan2()));
					recSerie.setFine_man2(Integer.parseInt(serie.getFineMan2()));
					recSerie.setDt_ingr_inv_ris2(DateUtil.toDate(serie.getDataIngrRis2()));
					recSerie.setInizio_man3(Integer.parseInt(serie.getInizioMan3()));
					recSerie.setFine_man3(Integer.parseInt(serie.getFineMan3()));
					recSerie.setDt_ingr_inv_ris3(DateUtil.toDate(serie.getDataIngrRis3()));
					recSerie.setInizio_man4(Integer.parseInt(serie.getInizioMan4()));
					recSerie.setFine_man4(Integer.parseInt(serie.getFineMan4()));
					recSerie.setDt_ingr_inv_ris4(DateUtil.toDate(serie.getDataIngrRis4()));
					recSerie.setUte_var(serie.getUteAgg());
					return ret = daoSerie.modificaSerie(recSerie);
					//					}
				}
			} else {
				throw new DataException("recInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List insertInventario(InventarioVO inventario,
			String tipoOperazione, int nInv, Locale locale, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);

		valida.validaInventario(inventario, tipoOperazione, nInv);
		List listaInvAss = null;

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			boolean tipoOperazionePrevista = false;

			DaoManager.begin(tx);
			log.debug("insertInventario(): Inizio transazione");

			// int numInvAss = 0;
			if (tipoOperazione.equals("O")) {
				tipoOperazione = "C";
			}
			if (tipoOperazione.equals("N") || tipoOperazione.equals("S")) {
				tipoOperazionePrevista = true;
				listaInvAss = controllaInvManuale(inventario,
						tipoOperazione, nInv, locale, ticket);
			}
			if (tipoOperazione.equals("C") || tipoOperazione.equals("P")) {
				tipoOperazionePrevista = true;
				listaInvAss = assegnaNumInv(inventario, tipoOperazione,
						nInv, locale);
			}
			if (tipoOperazione.equals("T")) {
				tipoOperazionePrevista = true;
				listaInvAss = modificaTitoloRecuperaInvCanc(inventario,
						tipoOperazione, nInv, locale, ticket);
			}

			if (!tipoOperazionePrevista){
				throw new ValidationException("invTipoOpNonPrev",
						ValidationException.errore);
			}

			tx.commit();
			ok = true;
			log.debug("insertInventario(): Commit transazione");

			return listaInvAss;

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
			if (!ok)
				log.error("insertInventario(): Rollback transazione");
		}
	}

	private List assegnaNumInv(InventarioVO inventario,
			String tipoOperazione, int nInv, Locale locale)
	throws DataException, ApplicationException, ValidationException {
		List listaInv = null;
		try {
			int numInv = 0;
			int numInvAss = 0;
			int numPrgInv = 0;
			int prgInv = 0;
			InventarioVO inv = null;

			boolean ret = false;
			valida.validaInventario(inventario, tipoOperazione, nInv);
			Tbc_serie_inventariale recSerie = daoSerie.getSerieForUpdate(inventario.getCodPolo(), inventario
					.getCodBib(), inventario.getCodSerie());
			if (recSerie != null) {
				if (tipoOperazione.equals("C")
						&& recSerie.getFlg_chiusa() == '1') {
					// serie chiusa
					throw new ValidationException("serieChiusa", ValidationException.errore);
				}
				// serie aperta
				if (tipoOperazione.equals("C")) {
					prgInv = recSerie.getPrg_inv_corrente();
					//almaviva5_20101227 #4109
					if (recSerie.getPrg_inv_pregresso() > 0)
						if ( (prgInv + 1) >= DocumentoFisicoCostant.MIN_PROGRESSIVO_INV_PREGRESSO )
							throw new ValidationException("calcoloCodInvKOMax", ValidationException.errore);
					//
				} else {
					prgInv = recSerie.getPrg_inv_pregresso();
				}
				prgInv++;
				if (tipoOperazione.equals("C")) {
					for (prgInv = prgInv; prgInv < Integer.MAX_VALUE; prgInv++) {
						if (checkIntervalloEsterni(prgInv, recSerie.getInizio_man(), recSerie.getFine_man() )
								&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man2(), recSerie.getFine_man2())
								&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man3(), recSerie.getFine_man3())
								&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man4(), recSerie.getFine_man4()) ) {
							// tutto ok
							break;
						}
					}
				}
				if (prgInv == 0)
					// invNumInvProgressivo
					throw new ValidationException("calcoloCodInvKO", ValidationException.errore);


				//almaviva5_20101217 #4109 superato limite massimo
				if (prgInv > DocumentoFisicoCostant.MAX_PROGRESSIVO_INVENTARIO)
					throw new ValidationException("calcoloCodInvKOMax", ValidationException.errore);

				numPrgInv++;
				// cicla sul numero inventari da creare
				listaInv = creaInv(inventario, recSerie, numPrgInv, prgInv,	tipoOperazione, nInv, locale);

				ret = ValidazioneDati.isFilled(listaInv);

			}else{
				throw new DataException("serieNonEsistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInv;
	}

	private static boolean checkIntervalloEsterni(int prgInv, Integer inizioMan, Integer fineMan) {
		int inizio = inizioMan != null ? inizioMan.intValue() : 0;
		int fine   = fineMan != null ? fineMan.intValue() : 0;
		if (inizio == fine)
			return true;
		return (prgInv < inizio) || (prgInv > fine);
	}

	private List creaInv(InventarioVO recInv,
			Tbc_serie_inventariale recSerie, int numPrgInv, int prgInv,
			String tipoOperazione, int nInv, Locale locale) throws DataException,
			ApplicationException, ValidationException, DaoManagerException {
		List listaInv = new ArrayList();
		InventarioVO inv = null;
		boolean ret = false;
		try {
			for (int index = 0; index < nInv; index++) {

				//almaviva5_20101217 #4109 superato limite massimo
				if (prgInv > DocumentoFisicoCostant.MAX_PROGRESSIVO_INVENTARIO)
					throw new DataException("calcoloCodInvKOMax");

				Timestamp ts = DaoManager.now();
				Tbc_inventario inventario = new Tbc_inventario();
				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(recInv.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(recInv.getCodBib());
				bib.setCd_polo(polo);
				//				Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
				//				serie.setCd_polo(bib);
				//				serie.setCd_serie(recInv.getCodSerie());
				Tb_titolo tit = new Tb_titolo();
				tit.setBid(recInv.getBid());
				Tbc_provenienza_inventario proven = null;
				if (recInv.getCodProven() != null) {
					proven = new Tbc_provenienza_inventario();
					proven.setCd_polo(bib);
					proven.setCd_proven(recInv.getCodProven());
					//				} else {
					//					proven.setCd_proven(null);
				}
				inventario.setCd_proven(proven);
				//				inventario.setCd_serie(serie);
				inventario.setCd_serie(recSerie);
				inventario.setTs_ins(ts);
				inventario.setTs_var(ts);
				inventario.setCd_inven(prgInv);
				if (recInv.getBid().trim().equals("")) {
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_CANCELLATO_CHR);
					tit.setBid("");
					inventario.setB(tit);
					//					inventario.getB().setBid("");
				} else {
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_PRECISATO_CHR);
					inventario.setB(tit);
				}
				if (recInv.getPrecInv().trim().equals("")) {
					inventario.setPrecis_inv("$");
				} else {
					inventario.setPrecis_inv(recInv.getPrecInv());
				}
				inventario.setValore(BigDecimal.valueOf(0L));
				inventario.setImporto(BigDecimal.valueOf(0L));
				inventario.setNum_vol(0);
				inventario.setTot_loc(0);
				inventario.setTot_inter(0);
				inventario.setAnno_abb(0);
				inventario.setAnno_fattura(null);
				inventario.setPrg_fattura(null);
				inventario.setRiga_fattura(null);
				inventario.setCd_bib_fatt("");
				inventario.setKey_loc(null);
				inventario.setKey_loc_old(0);
				inventario.setNum_carico(0);
				inventario.setNum_scarico(0);
				inventario.setData_carico(Date.valueOf("9999-12-31"));
				inventario.setData_scarico(Date.valueOf("9999-12-31"));
				inventario.setData_delibera(Date.valueOf("9999-12-31"));
				inventario.setSeq_coll(ValidazioneDati.trimOrEmpty(recInv.getSeqColl()));
				inventario.setFlg_disp(' ');
				inventario.setFlg_nuovo_usato(' ');
				inventario.setStato_con(" ");
				inventario.setCd_bib_scar(" ");
				inventario.setCd_polo_scar(" ");
				inventario.setId_bib_scar(null);
				inventario.setCd_mat_inv(" ");
				// dati ordine
				if (recInv.getCodBibO() == null) {
					inventario.setCd_bib_ord(null);
					inventario.setAnno_ord(null);
					inventario.setCd_ord(null);
					inventario.setCd_fornitore(0);
					inventario.setCd_tip_ord(' ');
					inventario.setTipo_acquisizione(' ');
				} else {
					inventario.setCd_bib_ord(recInv.getCodBibO());
					inventario.setAnno_ord(Integer.parseInt(recInv.getAnnoOrd()));
					inventario.setCd_ord(Integer.parseInt(recInv.getCodOrd()));
					inventario.setCd_fornitore(Integer.parseInt(recInv.getCodFornitore()));
					inventario.setCd_tip_ord(recInv.getCodTipoOrd().charAt(0));
					inventario.setTipo_acquisizione(recInv.getTipoAcquisizione().charAt(0));
				}
				inventario.setCd_no_disp(" ");
				inventario.setCd_frui(" ");
				inventario.setCd_riproducibilita(" ");
				inventario.setSupporto_copia(" ");

				inventario.setDigitalizzazione(' ');
				inventario.setDisp_copia_digitale(" ");
				inventario.setRif_teca_digitale("    ");
				inventario.setId_accesso_remoto("");

				inventario.setCd_carico(' ');
				inventario.setCd_scarico(' ');
				inventario.setDelibera_scar("");
				inventario.setSez_old("");
				inventario.setLoc_old("");
				inventario.setSpec_old("");
				inventario.setCd_supporto("  ");
				if (recInv.getDataIngresso() != null){
					inventario.setData_ingresso(DateUtil.toDate(recInv.getDataIngresso()));
				}else{
					inventario.setData_ingresso(null);
				}
				inventario.setTs_ins(ts);
				inventario.setTs_var(ts);
				if (recInv.getDataInsPrimaColl() != null) {
					inventario.setTs_ins_prima_coll(ts);
				} else {
					inventario.setTs_ins_prima_coll(null);
				}
				inventario.setUte_ins_prima_coll("");
				inventario.setUte_ins(recInv.getUteIns());
				inventario.setUte_var(recInv.getUteIns());
				inventario.setFl_canc(' ');
				daoInv = new Tbc_inventarioDao();
				if (daoInv.inserimentoInventario(inventario)) {
					inv = new InventarioVO();
					inv = trasformaInv1HibVO(inventario, locale);
					log.debug("Inserimento inventario: " + inv);
					listaInv.add(inv);
					if (++numPrgInv > nInv) {
						if (tipoOperazione.equals("C")) {
							recSerie.setPrg_inv_corrente(prgInv);
							recSerie.setUte_var(recInv.getUteIns());
							if (!daoSerie.modificaSerie(recSerie)) {
								throw new DataException(
								"erroreUpdateSerieProgrAss");
							}
							ret = true;
						} else if (tipoOperazione.equals("P")){
							recSerie.setPrg_inv_pregresso(prgInv);
							recSerie.setUte_var(recInv.getUteIns());
							if (!daoSerie.modificaSerie(recSerie)) {
								throw new DataException(
								"erroreUpdateSeriePregrAss");
							}
							ret = true;
						}
						return listaInv;
					}
					prgInv++;
					if (tipoOperazione.equals("C")) {
						for (prgInv = prgInv; prgInv < Integer.MAX_VALUE; prgInv++) {
							if (checkIntervalloEsterni(prgInv, recSerie.getInizio_man(), recSerie.getFine_man() )
									&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man2(), recSerie.getFine_man2())
									&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man3(), recSerie.getFine_man3())
									&& checkIntervalloEsterni(prgInv, recSerie.getInizio_man4(), recSerie.getFine_man4()) ) {
								// tutto ok
								break;
							}
						}
					}
				} else {
					throw new DataException("erroreInvInsert");
				}
			}

		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInv;
	}

	private List controllaInvManuale(InventarioVO inventario,
			String tipoOperazione, int nInv, Locale locale, String ticket)
	throws DataException, ApplicationException, ValidationException,
	DaoManagerException {
		checkTicket(ticket);
		int codRet = 0;
		boolean ret = false;
		List listaInvCreati = null;
		Tbc_serie_inventariale recSerie = null;
		InventarioVO inv = null;
		try {
			// valida.validaInventario(inventario, tipoOperazione, nInv);
			recSerie = daoSerie.getSerieForUpdate(inventario.getCodPolo(), inventario.getCodBib(), inventario.getCodSerie());
			if (recSerie != null) {
				if (tipoOperazione.equals("S")) {
					inv = controllaEsistenza(inventario, tipoOperazione, recSerie, nInv, locale, ticket);
					if (inv != null) {
						listaInvCreati = new ArrayList();
						listaInvCreati.add(inv);
						return listaInvCreati;
					} else {
						throw new ValidationException("inventarioInesistente",
								ValidationException.errore);
					}
				}
				// tipo operazione = N (non presente, crea inventario manuale)
				int inizio_man2 = ValidazioneDati.coalesce(recSerie.getInizio_man2(), 0);
				int fine_man2 = ValidazioneDati.coalesce(recSerie.getFine_man2(), 0);
				int inizio_man3 = ValidazioneDati.coalesce(recSerie.getInizio_man3(), 0);
				int fine_man3 = ValidazioneDati.coalesce(recSerie.getFine_man3(), 0);
				int inizio_man4 = ValidazioneDati.coalesce(recSerie.getInizio_man4(), 0);
				int fine_man4 = ValidazioneDati.coalesce(recSerie.getFine_man4(), 0);
				if ((recSerie.getFine_man() == 0 && recSerie.getInizio_man() == 0)
						&& (fine_man2 == 0 && inizio_man2 == 0)
						&& (fine_man3 == 0 && inizio_man3 == 0)
						&& (fine_man4 == 0 && inizio_man4 == 0)) {
					// vado a fare solo il controllo esistenza
					if (inventario.getCodInvent() > recSerie.getPrg_inv_corrente()) {
						throw new ValidationException("invNumSuperioreProgressivoAutomatico", ValidationException.errore);
					}
					inv = controllaEsistenza(inventario, tipoOperazione, recSerie, nInv, locale, ticket);
					if (inv != null) {
						listaInvCreati = new ArrayList();
						listaInvCreati.add(inv);
						return listaInvCreati;
					}
				}
				if (this.checkIntervalloInterni(inventario.getCodInvent(), recSerie.getInizio_man(), recSerie.getFine_man())
						|| checkIntervalloInterni(inventario.getCodInvent(), recSerie.getInizio_man2(), recSerie.getFine_man2())
						|| checkIntervalloInterni(inventario.getCodInvent(), recSerie.getInizio_man3(), recSerie.getFine_man3())
						|| checkIntervalloInterni(inventario.getCodInvent(), recSerie.getInizio_man4(), recSerie.getFine_man4())){
					//codInv ricade in uno degli intervalli riservati --> va bene
				}else{
					if (inventario.getCodInvent() > recSerie.getPrg_inv_corrente()) {
						// errore numero superiore a progressivo
						throw new ValidationException("invNumSuperioreProgressivoAutomatico", ValidationException.errore);
					}
				}
				//((inventario.getCodInvent() >= recSerie.getFine_man()
				//|| inventario.getCodInvent() <= recSerie.getInizio_man())) {
				inv = controllaEsistenza(inventario, tipoOperazione,
						recSerie, nInv, locale, ticket);
				if (inv != null) {
					listaInvCreati = new ArrayList();
					listaInvCreati.add(inv);
					return listaInvCreati;
				}
			} else {
				throw new DataException("recInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvCreati;
	}

	private static final boolean checkIntervalloInterni(int numInv, int inizioMan, int fineMan) {
		if (inizioMan <= numInv  && numInv <= fineMan){
			return true;
		}else{
			return false;
		}
	}
	private InventarioVO controllaEsistenza(InventarioVO inventario,
			String tipoOperazione, Tbc_serie_inventariale recSerie, int nInv, Locale locale,
			String ticket) throws DataException, ApplicationException,
			ValidationException, DaoManagerException {
		checkTicket(ticket);
		InventarioVO inv = null;
		List listaInv = new ArrayList();
		//		List listaTit = null;
		//		TitoloVO recTit = null;
		try {
			// valida.validaInventario(inventario, tipoOperazione, nInv);
			Tbc_inventario recInv = daoInv.getInventario(inventario
					.getCodPolo(), inventario.getCodBib(), inventario
					.getCodSerie(), inventario.getCodInvent());
			if (recInv == null) {
				// inventario inesistente
				if (tipoOperazione.equals("S")) {
					return inv;
				}
				if (tipoOperazione.equals("N")) {
					int prgInv = inventario.getCodInvent();
					int numPrgInv = 1;
					listaInv = creaInv(inventario, recSerie, numPrgInv, prgInv,
							tipoOperazione, nInv, locale);
					if (listaInv.size() > 0) {
						return inv = (InventarioVO)listaInv.get(0);
					} else {
						throw new ValidationException("errCreaInv",
								ValidationException.errore);
					}
				}
			} else {
				if (recInv.getFl_canc() == 'S') {
					if (tipoOperazione.equals("N")) {
						throw new ValidationException("invCancellato",
								ValidationException.errore);
					}else if (tipoOperazione.equals("S")){
						throw new ValidationException("msgCancellato",
								ValidationExceptionCodici.errore, "msgC");
					}
				}
				if (tipoOperazione.equals("N")) {
					throw new ValidationException("invEsistente",
							ValidationException.errore);
				}
				if (recInv.getKey_loc() != null) {
					// inventario già collocato
					throw new ValidationException("invGiaCollocato",
							ValidationException.errore);
				}
				if (recInv.getB().getBid().trim().equals("")) {
					recInv.getB().setBid(inventario.getBid());
					recInv.setCd_sit(DocFisico.Inventari.INVENTARIO_PRECISATO_CHR);
					recInv.setUte_var(inventario.getUteAgg());
					if (!daoInv.modificaInventario(recInv)) {
						throw new ValidationException("erroreUpdateInventario",
								ValidationExceptionCodici.errore);
					}
				} else if (!recInv.getB().getBid().trim().equals(
						inventario.getBid())) {
					inv = new InventarioVO();
					daoInv = new Tbc_inventarioDao();
					Tb_titolo titolo = daoInv.getTitoloDF1(inventario.getBid());
					if (titolo != null) {
						inv.setCambioIsbn(titolo.getIsbd());
						throw new ValidationException("msgConfermaSpostamento1",
								ValidationException.errore, "msgS", recInv.getB().getBid().trim(), recInv.getB().getIsbd().trim(), recInv.getPrecis_inv());
					}
					inv.setCambioIsbn("Titolo non trovato in Polo");
				} else {
					throw new ValidationException("invGiaPresente",
							ValidationExceptionCodici.errore, "msgGiaPresente");
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return inv;
	}

	private List modificaTitoloRecuperaInvCanc(InventarioVO inventario,
			String tipoOperazione, int nInv, Locale locale, String ticket)
	throws DataException, ApplicationException, ValidationException,
	DaoManagerException {
		checkTicket(ticket);
		List listaInvCreati = null;
		//		List listaTit = null;
		try {
			// valida.validaInventario(inventario, tipoOperazione, nInv);
			Tbc_inventario recInv = daoInv.getInventario(inventario
					.getCodPolo(), inventario.getCodBib(), inventario
					.getCodSerie(), inventario.getCodInvent());
			if (recInv != null) {
				// modifica bid e indicatore di cancellazione
				recInv.setUte_var(inventario.getUteAgg());
				//				recInv.getB().setBid(inventario.getBid());
				//fare getTitolo
				if (inventario.getBid() != null) {
					Tb_titolo titolo = daoInv.getTitoloDF1(inventario.getBid());
					if (titolo != null){
						recInv.setB(titolo);
					}else{
						throw new DataException("TitoloNonTrovatoInPolo");
					}
				} else {
					//per inventario cancellato non si dovrebbe verificare mai!
					recInv.getB().setBid(inventario.getBid());
				}
				if (inventario.getPrecInv().trim().equals("")) {
					recInv.setPrecis_inv("$");
				} else {
					recInv.setPrecis_inv(inventario.getPrecInv());
				}
				recInv.setCd_sit(DocFisico.Inventari.INVENTARIO_PRECISATO_CHR);
				recInv.setFl_canc(' ');
				if (!daoInv.modificaInventario(recInv)) {
					throw new DataException("erroreUpdateInventario");
				}
				listaInvCreati = new ArrayList();
				listaInvCreati.add(inventario);
			}
			//		} catch (ValidationException e) {
			//			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvCreati;
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public InventarioVO getInventario(String codPolo, String codBib,
			String codSerie, int codInv, Locale locale, String ticket) throws DataException,
			ApplicationException, ValidationException, DaoManagerException {
		checkTicket(ticket);

		InventarioVO inv = null;

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			DaoManager.begin(tx);
			log.debug("getInventario(): Inizio transazione");

			inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setCodSerie(codSerie);
			inv.setCodInvent(codInv);
			valida.validaInventarioKey(inv);
			Tbc_inventario recInv = daoInv.getInventario(codPolo, codBib,
					codSerie, codInv);
			if (recInv == null) {
				throw new DataException("invNonEsistente");
			}
			inv = this.trasformaInv1HibVO(recInv, locale);
			leggiNoteInventario(inv, recInv);
			if (recInv.getCd_sit() == DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
				if (recInv.getId_bib_scar() != null && recInv.getId_bib_scar()> 0){
					daoBiblio = new Tbf_bibliotecaDao();
					Tbf_biblioteca biblioteca = daoBiblio.getBiblioteca(Integer.valueOf(recInv.getId_bib_scar()));
					if (biblioteca != null){
						inv.setVersoBibDescr(biblioteca.getNom_biblioteca());
					}else{
						throw new ValidationException("Biblioteca non presente in anagrafe",
								ValidationException.errore);
					}
				}else{
					inv.setVersoBibDescr("");

				}
			}

			tx.commit();
			ok = true;
			log.debug("getInventario(): Commit transazione");

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
			if (!ok)
				log.error("getInventario(): Rollback transazione");
		}
		return inv;
	}

	/**
	 * @param inv
	 * @param recInv
	 * @throws DaoManagerException
	 */
	private void leggiNoteInventario(InventarioVO inv, Tbc_inventario recInv)
	throws DaoManagerException {
		NotaInventarioVO nota = null;
		List<NotaInventarioVO> listaNote = null;
		daoNotaInv = new Tbc_nota_invDao();
		List<Tbc_nota_inv> lista = daoNotaInv.getNote(recInv);
		if (lista.size() > 0){
			listaNote = new ArrayList<NotaInventarioVO>();
			for (int index = 0; index < lista.size(); index++) {
				Tbc_nota_inv recResult = lista.get(index);
				nota = new NotaInventarioVO();
				nota.setPrg(index + 1);
				nota.setCodPolo(recResult.getCd_polo().getCd_serie().getCd_polo().getCd_polo().getCd_polo());
				nota.setCodBib(recResult.getCd_polo().getCd_serie().getCd_polo().getCd_biblioteca());
				nota.setCodSerie(recResult.getCd_polo().getCd_serie().getCd_serie());
				nota.setCodInvent(recResult.getCd_polo().getCd_inven());
				nota.setCodNota(recResult.getCd_nota());
				nota.setDescrNota(recResult.getDs_nota_libera());
				nota.setDataIns(DateUtil.formattaData(recResult.getTs_ins().getTime()));
				nota.setDataAgg(DateUtil.formattaData(recResult.getTs_var().getTime()));
				nota.setUteIns(recResult.getUte_ins());
				nota.setUteAgg(recResult.getUte_var());
				listaNote.add(nota);
			}
			inv.setListaNote(listaNote);
		}else{
			inv.setListaNote(null);
		}
	}

	private InventarioDettaglioVO trasformaInvHibVO(Tbc_inventario inventario, Locale locale)
	throws DataException {
		InventarioDettaglioVO inv = null;
		try {
			inv = new InventarioDettaglioVO();
			inv.setLocale(locale);
			inv.setCodPolo(inventario.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
			inv.setCodBib(inventario.getCd_serie().getCd_polo().getCd_biblioteca());
			inv.setCodSerie(inventario.getCd_serie().getCd_serie());
			inv.setCodInvent(inventario.getCd_inven());
			if (inventario.getCd_proven() != null) {
				inv.setCodProven(inventario.getCd_proven().getCd_proven());
				inv.setCodPoloProven(inventario.getCd_proven().getCd_polo().getCd_polo().getCd_polo());
				inv.setCodBibProven(inventario.getCd_proven().getCd_polo().getCd_biblioteca());
			}
			if (inventario.getKey_loc() == null) {
				inv.setKeyLoc(0);
			} else {
				inv.setKeyLoc(inventario.getKey_loc().getKey_loc());
			}
			if (inventario.getB() != null){
				inv.setBid(inventario.getB().getBid());
				inv.setNatura(String.valueOf(inventario.getB().getCd_natura()));
			}else{
				inv.setBid("");
				inv.setNatura("");
			}
			inv.setValoreDouble((inventario.getValore()).doubleValue());
			inv.setImportoDouble(inventario.getImporto().doubleValue());
			if (inventario.getNum_vol() != null){
				inv.setNumVol(inventario.getNum_vol().toString());
			}else{
				inv.setNumVol(String.valueOf(0));
			}
			if (inventario.getTot_loc() != null){
				inv.setTotLoc(inventario.getTot_loc().toString());
			}else{
				inv.setTotLoc(String.valueOf(0));
			}
			if (inventario.getTot_inter() != null){
				inv.setTotInter(inventario.getTot_inter().toString());
			}else{
				inv.setTotInter(String.valueOf(0));
			}
			inv.setSeqColl(inventario.getSeq_coll().trim());
			if (inventario.getPrecis_inv() != null){
				if (inventario.getPrecis_inv().equals("$")) {
					inv.setPrecInv(" ");
				} else {
					inv.setPrecInv(inventario.getPrecis_inv());
				}
			}else{
				inv.setPrecInv("");
			}
			if (inventario.getAnno_abb() != null){
				inv.setAnnoAbb(inventario.getAnno_abb().toString());
			}else{
				inv.setAnnoAbb(String.valueOf(0));
			}
			inv.setFlagDisp(String.valueOf(inventario.getFlg_disp()));
			inv.setFlagNU(String.valueOf(inventario.getFlg_nuovo_usato()));
			inv.setStatoConser(inventario.getStato_con());

			if (inventario.getCd_fornitore() != null){
				inv.setCodFornitore(inventario.getCd_fornitore().toString());
			}else{
				inv.setCodFornitore("");
			}
			inv.setCodMatInv(inventario.getCd_mat_inv());
			inv.setCodSit(String.valueOf(inventario.getCd_sit()));
			if (inventario.getCd_no_disp().trim() != null){
				inv.setCodNoDisp(inventario.getCd_no_disp().trim());
			}else{
				inv.setCodNoDisp("");
			}
			if (inventario.getCd_frui().trim().trim() != null){
				inv.setCodFrui(inventario.getCd_frui());
			}else{
				inv.setCodFrui("");
			}
			//
			if (inventario.getCd_riproducibilita() == null){
				inv.setCodRiproducibilita("");
			}else{
				inv.setCodRiproducibilita(inventario.getCd_riproducibilita().toString());
			}
			if (inventario.getSupporto_copia() == null){
				inv.setSupportoCopia("");
			}else{
				inv.setSupportoCopia(inventario.getSupporto_copia());
			}
			//
			if (inventario.getDigitalizzazione() == null){
				inv.setDigitalizzazione("");
			}else{
				inv.setDigitalizzazione(inventario.getDigitalizzazione().toString());
			}
			if (inventario.getDisp_copia_digitale() == null){
				inv.setDispDaRemoto("");
				inv.setDescrDispDaRemoto("");
			}else{
				inv.setDispDaRemoto(inventario.getDisp_copia_digitale().toString());
				inv.setDescrDispDaRemoto(codici.cercaDescrizioneCodice(String.valueOf(inventario.getDisp_copia_digitale()),
						CodiciType.CODICE_DISP_ACCESSO_REMOTO,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}
			if (inventario.getRif_teca_digitale() == null){
				inv.setRifTecaDigitale("");
				inv.setDescrRifTecaDigitale("");
			}else{
				inv.setRifTecaDigitale(inventario.getRif_teca_digitale().toString());
				inv.setDescrRifTecaDigitale(codici.cercaDescrizioneCodice(String.valueOf(inventario.getRif_teca_digitale()),
						CodiciType.CODICE_TECHE_DIGITALI,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}






			//			if (inventario.getDisp_copia_digitale() == null){
			//				inv.setDispDaRemoto("");
			//			}else{
			//				inv.setDispDaRemoto(inventario.getDisp_copia_digitale().toString());
			//			}
			//			if (inventario.getRif_teca_digitale() == null){
			//				inv.setRifTecaDigitale("");
			//			}else{
			//				inv.setRifTecaDigitale(inventario.getRif_teca_digitale());
			//			}
			if (inventario.getId_accesso_remoto() == null){
				inv.setIdAccessoRemoto("");
			}else{
				inv.setIdAccessoRemoto(inventario.getId_accesso_remoto());
			}
			//
			if (inventario.getCd_ord() == null || inventario.getCd_ord() == 0 ) {
				inv.setCodBibO("");
				inv.setAnnoOrd("");
				inv.setCodOrd("");
				inv.setRigaOrd("");
				if (inventario.getCd_tip_ord()!=null){
					inv.setCodTipoOrd(inventario.getCd_tip_ord().toString());
					if (inventario.getTipo_acquisizione() != null){
						inv.setTipoAcquisizione(inventario.getTipo_acquisizione().toString());
					}else{
						inv.setTipoAcquisizione("");
					}
				}else{
					inv.setCodTipoOrd("");
					inv.setTipoAcquisizione("");
				}
			} else {
				inv.setCodBibO(inventario.getCd_bib_ord());
				inv.setAnnoOrd(inventario.getAnno_ord().toString());
				inv.setCodOrd(inventario.getCd_ord().toString());
				inv.setCodTipoOrd(inventario.getCd_tip_ord().toString());
				if (inventario.getTipo_acquisizione() != null){
					inv.setTipoAcquisizione(inventario.getTipo_acquisizione().toString());
				}else{
					inv.setTipoAcquisizione("");
				}
			}
			if (inventario.getCd_bib_fatt() == null ) {
				inv.setCodBibF("");
				inv.setAnnoFattura("");
				inv.setProgrFattura("");
				inv.setRigaFattura("");
			}else{
				if (inventario.getCd_bib_fatt().trim().equals("")) {
					inv.setCodBibF(inventario.getCd_bib_fatt());
					inv.setAnnoFattura("");
					inv.setProgrFattura("");
					inv.setRigaFattura("");
				} else {
					inv.setCodBibF(inventario.getCd_bib_fatt());
					inv.setAnnoFattura(inventario.getAnno_fattura().toString());
					inv.setProgrFattura(inventario.getPrg_fattura().toString());
					inv.setRigaFattura(inventario.getRiga_fattura().toString());
				}
			}
			if (inventario.getCd_no_disp().trim() != null){
				inv.setCodNoDisp(inventario.getCd_no_disp().trim());
			}else{
				inv.setCodNoDisp("");
			}
			if (inventario.getCd_frui().trim() != null){
				inv.setCodFrui(inventario.getCd_frui());
			}else{
				inv.setCodFrui("");
			}
			if (inventario.getCd_polo_scar() != null){
				inv.setCodPoloScar(inventario.getCd_polo_scar());
			}else{
				inv.setCodPoloScar(null);
			}
			if (inventario.getCd_bib_scar() != null){
				inv.setCodBibS(inventario.getCd_bib_scar());
			}else{
				inv.setCodBibS("");
			}
			if (inventario.getId_bib_scar() != null){
				inv.setIdBibScar(inventario.getId_bib_scar());
			}else{
				inv.setIdBibScar(0);
			}
			if (inventario.getCd_scarico() != null){
				inv.setCodScarico(inventario.getCd_scarico().toString());
			}else{
				inv.setCodScarico("");
			}
			if (inventario.getNum_scarico() != null){
				inv.setNumScarico(inventario.getNum_scarico().toString());
			}else{
				inv.setNumScarico("");
			}
			if (inventario.getCd_carico() != null){
				inv.setCodCarico(inventario.getCd_carico().toString());
			}else{
				inv.setCodCarico("");
			}

			if (inventario.getNum_carico() != null){
				inv.setNumCarico(inventario.getNum_carico().toString());
			}else{
				inv.setNumCarico("");
			}
			if (inventario.getData_carico() != null){
				if (inventario.getData_carico().equals("9999-12-31")) {
					inv.setDataCarico("00/00/0000");
				} else {
					inv.setDataCarico(DateUtil.formattaData(inventario.getData_carico().getTime()));
				}
			}else{
				inv.setDataCarico("00/00/0000");
			}
			if (inventario.getDelibera_scar() != null){
				inv.setDeliberaScarico(inventario.getDelibera_scar());
			}else{
				inv.setDeliberaScarico("");
			}
			if (inventario.getData_redisp_prev() != null){
				if (inventario.getData_redisp_prev().equals("")) {
					inv.setDataRedisp("");
				} else {
					inv.setDataRedisp(DateUtil.formattaData(inventario.getData_redisp_prev().getTime()));
				}
			}else{
				inv.setDataRedisp("");
			}
			if (inventario.getData_scarico() != null){
				if (inventario.getData_scarico().equals("9999-12-31")) {
					inv.setDataScarico("00/00/0000");
				} else {
					inv.setDataScarico(DateUtil.formattaData(inventario.getData_scarico().getTime()));
				}
			}else{
				inv.setDataScarico("00/00/0000");
			}
			if (inventario.getData_delibera() != null){
				if (inventario.getData_delibera().equals("9999-12-31")) {
					inv.setDataDelibScar("00/00/0000");
				} else {
					inv.setDataDelibScar(DateUtil.formattaData(inventario.getData_delibera().getTime()));
				}
			}else{
				inv.setDataDelibScar("00/00/0000");
			}
			if (inventario.getSez_old() != null){
				inv.setSezOld(inventario.getSez_old().trim());
			}else{
				inv.setSezOld("");
			}
			if (inventario.getLoc_old() != null){
				inv.setLocOld(inventario.getLoc_old());
			}else{
				inv.setLocOld("");
			}
			if (inventario.getSpec_old() != null){
				inv.setSpecOld(inventario.getSpec_old());
			}else{
				inv.setSpecOld("");
			}
			if (inventario.getKey_loc_old() != null){
				inv.setKeyLocOld(inventario.getKey_loc_old());
			}else{
				inv.setKeyLocOld(0);
			}
			inv.setCancDB2i(String.valueOf(inventario.getFl_canc()));
			if (inventario.getCd_supporto() != null){
				inv.setCodSupporto(inventario.getCd_supporto());
			}else{
				inv.setCodSupporto("");
			}
			if (inventario.getData_ingresso() == null) {
				inv.setDataIngresso(null);
			} else {
				inv.setDataIngresso(DateUtil.formattaData(inventario.getData_ingresso().getTime()));
			}
			if (inventario.getTs_ins_prima_coll() == null) {
				inv.setDataInsPrimaColl("");
			} else {
				inv.setDataInsPrimaColl(DateUtil.formattaData(inventario.getTs_ins_prima_coll().getTime()));
			}
			if (inventario.getUte_ins_prima_coll() == null) {
				inv.setUteInsPrimaColl("");
			} else {
				inv.setUteInsPrimaColl(inventario.getUte_ins_prima_coll().toString());
			}
			if (inventario.getTs_ins() != null){
				inv.setDataIns(DateUtil.formattaData(inventario.getTs_ins().getTime()));
			}else{
				inv.setDataIns("");
			}
			if (inventario.getTs_var() != null){
				inv.setDataAgg(DateUtil.formattaData(inventario.getTs_var().getTime()));
			}else{
				inv.setDataAgg("");
			}
			if (inventario.getUte_ins() != null){
				inv.setUteIns(inventario.getUte_ins());
			}else{
				inv.setUteIns("");
			}
			if (inventario.getUte_var() != null){
				inv.setUteAgg(inventario.getUte_var());
			}else{
				inv.setUteAgg("");
			}
		} catch (Exception e) {

			throw new DataException(e);
		}
		return inv;
	}

	private void trasformaInvVOHib(InventarioVO recInvColl,	Tbc_inventario inventario)
	throws DataException {
		try {
			inventario.setCd_sit(recInvColl.getCodSit().charAt(0));
			inventario.setPrecis_inv(recInvColl.getPrecInv().trim());
			inventario.setValore(new BigDecimal(recInvColl.getValoreDouble()));
			inventario.setImporto(new BigDecimal(recInvColl.getImportoDouble()));
			if (recInvColl.getNumVol().trim().equals("")){
				inventario.setNum_vol(0);
			}else{
				inventario.setNum_vol(Integer.parseInt(recInvColl.getNumVol()));
			}
			inventario.setTot_loc(Integer.parseInt(recInvColl.getTotLoc()));
			inventario.setTot_inter(Integer.parseInt(recInvColl.getTotInter()));
			inventario.setAnno_abb(Integer.parseInt(recInvColl.getAnnoAbb()));
			if (recInvColl.getNumFattura() != null && !recInvColl.getNumFattura().trim().equals("")) {
				//				inventario.setAnno_fattura(Integer.parseInt(recInvColl.getDataFattura().substring(6)));
				inventario.setAnno_fattura(Integer.parseInt(recInvColl.getAnnoFattura()));
				inventario.setCd_bib_fatt(recInvColl.getCodBibF());
				if (!recInvColl.getProgrFattura().equals("") && !recInvColl.getRigaFattura().equals("")){
					inventario.setPrg_fattura(Integer.parseInt(recInvColl.getProgrFattura()));
					inventario.setRiga_fattura(Integer.parseInt(recInvColl.getRigaFattura()));
				}
			} else {
				inventario.setAnno_fattura(null);
				inventario.setPrg_fattura(null);
				inventario.setRiga_fattura(null);
				inventario.setCd_bib_fatt("");
			}
			inventario.setKey_loc_old(recInvColl.getKeyLocOld());
			inventario.setNum_carico(Integer.parseInt(recInvColl.getNumCarico()));
			inventario.setNum_scarico(Integer.parseInt(recInvColl.getNumScarico()));
			if (recInvColl.getDataCarico().equals("00/00/0000")) {
				inventario.setData_carico(Date.valueOf("9999-12-31"));
			} else {
				inventario.setData_carico(DateUtil.toDate(recInvColl.getDataCarico()));
			}
			if (recInvColl.getDataScarico().equals("00/00/0000")) {
				inventario.setData_scarico(Date.valueOf("9999-12-31"));
			} else {
				inventario.setData_scarico(DateUtil.toDate(recInvColl.getDataScarico()));
			}
			if (recInvColl.getDataDelibScar().equals("00/00/0000")) {
				inventario.setData_delibera(Date.valueOf("9999-12-31"));
			} else {
				inventario.setData_delibera(DateUtil.toDate(recInvColl.getDataDelibScar()));
			}
			inventario.setSeq_coll(recInvColl.getSeqColl().trim());
			inventario.setFlg_disp(recInvColl.getFlagDisp().charAt(0));
			inventario.setFlg_nuovo_usato(recInvColl.getFlagNU().charAt(0));
			inventario.setStato_con(recInvColl.getStatoConser());
			inventario.setCd_bib_scar(recInvColl.getCodBibS());
			inventario.setCd_polo_scar(recInvColl.getCodPoloScar());
			if (recInvColl.getIdBibScar() == 0){
				inventario.setId_bib_scar(null);
			}else{
				inventario.setId_bib_scar(recInvColl.getIdBibScar());
			}
			inventario.setCd_mat_inv(recInvColl.getCodMatInv());
			//
			if (recInvColl.getDataIngresso() != null && recInvColl.getDataIngresso().equals("")) {
				inventario.setData_ingresso(null);
			} else {
				inventario.setData_ingresso(DateUtil.toTimestamp(recInvColl.getDataIngresso()));
			}
			if (!recInvColl.getCodProven().equals("")) {
				daoProven = new Tbc_provenienza_inventarioDao();
				Tbc_provenienza_inventario proven = new Tbc_provenienza_inventario();
				proven = daoProven.getProvenienza(recInvColl.getCodPoloProven(), recInvColl
						.getCodBibProven(), recInvColl.getCodProven());
				if (proven != null) {
					inventario.setCd_proven(proven);
				} else {
					inventario.setCd_proven(null);
				}
			} else
				inventario.setCd_proven(null);

			inventario.setCd_tip_ord(ValidazioneDati.isFilled(recInvColl.getCodTipoOrd()) ? recInvColl.getCodTipoOrd().charAt(0) : ' ');
			if (recInvColl.getTipoAcquisizione().trim() != null && !recInvColl.getTipoAcquisizione().trim().equals("")){
				inventario.setTipo_acquisizione(recInvColl.getTipoAcquisizione().charAt(0));
			}else{
				inventario.setTipo_acquisizione(' ');
			}
			if (recInvColl.getCodBibO().equals("")) {
				inventario.setCd_bib_ord(null);
				//				inventario.setCd_tip_ord(null);
				inventario.setAnno_ord(null);
				inventario.setCd_ord(null);
				inventario.setCd_fornitore(0);
			} else {
				inventario.setAnno_ord(Integer.parseInt(recInvColl.getAnnoOrd()));
				inventario.setCd_bib_ord(recInvColl.getCodBibO());
				//				inventario.setCd_tip_ord((recInvColl.getCodTipoOrd()).charAt(0));
				inventario.setCd_ord(Integer.parseInt(recInvColl.getCodOrd()));
				inventario.setCd_fornitore(Integer.parseInt(recInvColl.getCodFornitore()));
			}
			inventario.setCd_no_disp(recInvColl.getCodNoDisp().trim());
			inventario.setCd_frui(recInvColl.getCodFrui().trim());
			//
			inventario.setCd_riproducibilita(recInvColl.getCodRiproducibilita().trim());
			inventario.setSupporto_copia(recInvColl.getSupportoCopia().trim());
			//
			if (recInvColl.getDigitalizzazione().length() != 0){
				inventario.setDigitalizzazione(recInvColl.getDigitalizzazione().charAt(0));
			}else{
				inventario.setDigitalizzazione(' ');
			}
			inventario.setDisp_copia_digitale(recInvColl.getDispDaRemoto().trim());
			inventario.setRif_teca_digitale(recInvColl.getRifTecaDigitale().trim());
			inventario.setId_accesso_remoto(recInvColl.getIdAccessoRemoto().trim());
			//
			if (recInvColl.getCodCarico().length() != 0){
				inventario.setCd_carico(recInvColl.getCodCarico().charAt(0));
			}else{
				inventario.setCd_carico(' ');
			}
			if (recInvColl.getCodScarico().length() != 0){
				inventario.setCd_scarico(recInvColl.getCodScarico().charAt(0));
			}else{
				inventario.setCd_scarico(' ');
			}
			inventario.setDelibera_scar(recInvColl.getDeliberaScarico().trim());
			inventario.setSez_old(recInvColl.getSezOld().trim());
			inventario.setLoc_old(recInvColl.getLocOld().trim());
			inventario.setSpec_old(recInvColl.getSpecOld().trim());
			// inventario.setTs_var(Timestamp.valueOf(recInvColl.getDataAgg()));
			// inventario.setTs_ins(Timestamp.valueOf(recInvColl.getDataIns()));
			inventario.setUte_var(recInvColl.getUteAgg().trim());
			inventario.setUte_ins(recInvColl.getUteIns().trim());

			if (ValidazioneDati.isFilled(recInvColl.getDataInsPrimaColl()) ) {
				inventario.setTs_ins_prima_coll(DateUtil.toTimestamp(recInvColl.getDataInsPrimaColl()));
			}

		} catch (Exception e) {

			throw new DataException(e);
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
	public InventarioDettaglioVO getInventarioDettaglio(String codPolo,
			String codBib, String codSerie, int codInv, Locale locale, String ticket)
	throws ResourceNotFoundException, ApplicationException,
	ValidationException, DataException {
		checkTicket(ticket);
		// InventarioVO recInv = null;
		InventarioDettaglioVO rec = null;
		//		List listaTit = new ArrayList();
		//		TitoloVO recTit = null;
		InventarioVO inv = new InventarioVO();
		inv.setCodPolo(codPolo);
		inv.setCodBib(codBib);
		inv.setCodSerie(codSerie);
		inv.setCodInvent(codInv);
		valida.validaInventarioKey(inv);
		try {

			daoInv = new Tbc_inventarioDao();
			Tbc_inventario inventario = daoInv.getInventario(codPolo, codBib,
					codSerie, codInv);
			// recInv = (InventarioVO)this.getInventario(codPolo, codBib,
			// codSerie, codInv, ticket);
			if (inventario != null) {
				if (inventario.getFl_canc() == 'S') {
					throw new ValidationException("invCancellato",
							ValidationException.errore);
				}
				rec = trasformaInvHibVO(inventario, locale);
				if (inventario.getB() != null) {
					//trattamento per sip2
					List titoli = null;
					try {
						titoli = dfCommon.getTitoloHib(inventario.getB(), ticket);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block

					}
					if (titoli != null){
						if (titoli.size() > 0 && titoli.size() == 1){
							TitoloVO titolo = (TitoloVO)titoli.get(0);
							rec.setTitIsbdTrattato(titolo.getIsbd());
						}
					}else{
						rec.setTitIsbdTrattato("Titolo non trovato in Polo");
					}
					//fine trattamento per sip2
					rec.setTitIsbd(inventario.getB().getIsbd());
					rec.setTitNatura(String.valueOf(inventario.getB().getCd_natura()));
				} else {
					rec.setTitIsbd("Titolo non trovato in Polo");
				}
				if (inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
					if (inventario.getId_bib_scar() != null && inventario.getId_bib_scar()> 0){
						daoBiblio = new Tbf_bibliotecaDao();
						Tbf_biblioteca biblioteca = daoBiblio.getBiblioteca(Integer.valueOf(inventario.getId_bib_scar()));
						if (biblioteca != null){
							rec.setVersoBibDescr(biblioteca.getNom_biblioteca());
						}else{
							throw new ValidationException("Biblioteca non presente in anagrafe",
									ValidationException.errore);
						}
					}else{
						rec.setVersoBibDescr("");

					}
					rec.setMotivoScaricoDescr(codici.cercaDescrizioneCodice(String.valueOf(inventario.getCd_scarico()),
							CodiciType.CODICE_MOTIVI_DI_SCARICO_INVENTARIALE,
							CodiciRicercaType.RICERCA_CODICE_SBN));
					rec.setDataScarico(DateUtil.formattaData(inventario.getData_scarico().getTime()));
				}
				if (inventario.getKey_loc() != null) {
					rec.setCollCodSez(inventario.getKey_loc().getCd_sez().getCd_sez().trim());
					rec.setCollCodLoc(inventario.getKey_loc().getCd_loc().trim());
					rec.setCollSpecLoc(inventario.getKey_loc().getSpec_loc().trim());
					rec.setSeqColl(inventario.getSeq_coll().trim());
					if (inventario.getKey_loc().getConsis().equals("$")) {
						rec.setCollConsis("");
					} else {
						rec.setCollConsis(inventario.getKey_loc().getConsis());
					}
					rec.setCollBidLoc(inventario.getKey_loc().getB().getBid());
				}
				if (inventario.getKey_loc_old() != null && inventario.getKey_loc_old() > 0) {
					Tbc_collocazione collocazione = daoColl.getCollocazione(inventario.getKey_loc_old());
					if  (collocazione != null ){
						if (collocazione.getFl_canc() == 'S'){
							rec.setSezOld(collocazione.getCd_sez().getCd_sez());
							rec.setLocOld(collocazione.getCd_loc());
							rec.setSpecOld(collocazione.getSpec_loc());
						}else{
							//in presenza di keyLocOld se la collocazione relativa non è cancellata, è un errore
							//si riportano ugualmente i dati della collocazione con un'indicazione che riporterà
							//una segnalazione al web
							if ((inventario.getKey_loc().getTot_inv() > 0) || (inventario.getKey_loc().getTot_inv() > 0
									&& inventario.getKey_loc().getTot_inv_prov().intValue() > 0)) {//rp bug 0004161 collaudo
								rec.setSezOld(collocazione.getCd_sez().getCd_sez());
							}else{
								rec.setSezOld(collocazione.getCd_sez().getCd_sez() + "Squadratura");
							}
							rec.setLocOld(collocazione.getCd_loc());
							rec.setSpecOld(collocazione.getSpec_loc());
						}
					}else{
						throw new DataException("collocazioneDefinitivaInesistente");

					}
				}
				if (inventario.getCd_proven() != null) {
					rec.setDescrProven(inventario.getCd_proven().getDescr());
				} else {
					rec.setDescrProven("");
				}
				//				if (inventario.getKey_loc_old() != null	&& inventario.getKey_loc_old() > 0) {
				//					Tbc_collocazione collocazione = daoColl.getCollocazione(inventario.getKey_loc_old());
				//					if (collocazione != null && collocazione.getFl_canc() != 'S') {
				//						rec.setSezOld(collocazione.getCd_sez().getCd_sez());
				//						rec.setLocOld(collocazione.getCd_loc());
				//						rec.setSpecOld(collocazione.getSpec_loc());
				//					} else {
				//						//potrebbe esser andato male Spostamento Collocazioni
				//						rec.setSezOld("SQUADRADB");
				//						rec.setLocOld("Squadratura: Collocazione non esistente o cancellata");
				//					}
				//				}
				//almaviva5_20090807 errore da migrazione
				Integer cd_ord = inventario.getCd_ord();
				if (cd_ord != null && cd_ord > 0) {
					rec.setCodBibO(inventario.getCd_bib_ord());
					rec.setCodTipoOrd(""+inventario.getCd_tip_ord());
					rec.setAnnoOrd(String.valueOf(inventario.getAnno_ord()));
					rec.setCodOrd(String.valueOf(cd_ord));
					//servizio fornitore
					//almaviva5_20090807 errore da migrazione
					Integer cd_fornitore = inventario.getCd_fornitore();
					if (cd_fornitore != null && cd_fornitore > 0) {
						ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
						ricercaFornitori.setCodFornitore(String.valueOf(cd_fornitore));
						ricercaFornitori.setLocale("0");
						try{
							List listaFornitori = acquisizioni.getRicercaListaFornitori(ricercaFornitori);
							if (listaFornitori.size() > 0 && listaFornitori.size() == 1) {
								FornitoreVO fornitore = (FornitoreVO) listaFornitori.get(0);
								rec.setDescrFornitore(fornitore.getNomeFornitore());
							} else {
								rec.setDescrFornitore("");
							}
						} catch (ValidationException e) {
							if (e.getMessage().equals("assenzaRisultati")){
								throw new ValidationException("fornitoreNonTrovato");
							}
						}
					}
				}
				rec.setImportoDouble(inventario.getImporto().doubleValue());
				rec.setValoreDouble(inventario.getValore().doubleValue());
			} else {
				throw new ValidationException("invNonEsistente",
						ValidationException.errore);
			}
			leggiNoteInventario(rec, inventario);
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return rec;
	}

	private ProvenienzaInventarioVO getProvenienza(String codPolo,
			String codBib, String codProven) throws DataException,
			ValidationException, DaoManagerException {
		ProvenienzaInventarioVO recProven = null;
		try {
			ProvenienzaInventarioVO rec = new ProvenienzaInventarioVO();
			rec.setCodPolo(codPolo);
			rec.setCodBib(codBib);
			rec.setCodProvenienza(codProven);
			valida.validaProvenienzaInventario(rec);
			Tbc_provenienza_inventario provenienza = daoProven.getProvenienza(
					codPolo, codBib, codProven);
			if (provenienza == null) {
				throw new DataException("invNonEsistente");
			}
			recProven = new ProvenienzaInventarioVO();
			recProven.setCodPolo(provenienza.getCd_polo().getCd_polo()
					.getCd_polo());
			recProven.setCodBib(provenienza.getCd_polo().getCd_biblioteca());
			recProven.setCodProvenienza(provenienza.getCd_proven());
			recProven.setDescrProvenienza(provenienza.getDescr());
			recProven.setDataIns(provenienza.getTs_ins().toString());
			recProven.setDataAgg(provenienza.getTs_var().toString());
			recProven.setUteIns(provenienza.getUte_ins());
			recProven.setUteAgg(provenienza.getUte_var());
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return recProven;
	}

	public ProvenienzaInventarioVO getProvenienza(String codPolo,
			String codBib, String codProven, String ticket) throws DataException, ApplicationException,
			ValidationException {
		checkTicket(ticket);
		ProvenienzaInventarioVO recProven = null;
		try {
			recProven = this.getProvenienza(codPolo, codBib, codProven);
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return recProven;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public int updateInvColl(InventarioVO recInvColl,
			CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		checkTicket(ticket);
		// this.ValidaInventarioDettaglioVO(inventario, tipoOperazione);
		//		boolean ret = false;
		int keyLoc = 0;

		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;

		try {
			DaoManager.begin(tx);
			log.debug("updateInvColl(): Inizio transazione");

			if (tipoOperazione.equals("M_INV")) {
				keyLoc = modificaInvColl(recInvColl, recColl, tipoOperazione);
			}
			//			if (tipoOperazione.equals("S") || tipoOperazione.equals("N")) {
			//				return keyLoc = gestioneColl(recInvColl, recColl, tipoOperazione);
			//			}
			else {
				throw new ValidationException("invTipoOpNonPrev",
						ValidationException.errore);
			}

			tx.commit();
			ok = true;
			log.debug("updateInvColl(): Commit transazione");

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
			if (!ok)
				log.error("updateInvColl(): Rollback transazione");
		}
		return keyLoc;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean updateCollocazioneScolloca(InventarioVO recInvColl,
			CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		checkTicket(ticket);
		// this.ValidaInventarioDettaglioVO(inventario, tipoOperazione);
		//		boolean ret = false;
		boolean ret = false;
		try {
			if (tipoOperazione.equals("S") || tipoOperazione.equals("N") || tipoOperazione.equals("scarico")) {
				return ret = gestioneColl(recInvColl, recColl, tipoOperazione, ticket);
			} else {
				throw new ValidationException("invTipoOpNonPrev",
						ValidationException.errore);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		}
		// return ret;
	}
	private int modificaInvColl(InventarioVO recInvColl, CollocazioneVO recColl, String tipoOperazione)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		//modifica inventario
		//crea collocazione
		Timestamp ts = DaoManager.now();
		boolean ret = false;
		String oper = null;
		// InventarioVO recInv = null;
		Tbc_inventario inventario = null;
		Tbc_collocazione collocazione = null;
		int keyLoc = -1;
		valida.validaInventario(recInvColl);
		try {
			daoInv = new Tbc_inventarioDao();
			inventario = daoInv.getInventario(recInvColl.getCodPolo(), recInvColl.getCodBib(),
					recInvColl.getCodSerie(), recInvColl.getCodInvent());
			if (inventario != null) {
				trattamentoInsModNoteInventario(recInvColl, ts, inventario);
				boolean fatturaPresenteDB = (ValidazioneDati.isFilled(inventario.getCd_bib_fatt() ) &&
						inventario.getAnno_fattura() != null && inventario.getAnno_fattura().intValue() > 0
						&& inventario.getPrg_fattura() != null && inventario.getPrg_fattura().intValue() > 0
						&& inventario.getRiga_fattura() != null && inventario.getRiga_fattura().intValue() > 0);

				if (!tipoOperazione.equals(DocumentoFisicoCostant.S_SCOLLOCAZIONE_INV)
						&& !tipoOperazione.equals(DocumentoFisicoCostant.C_CANCELLA_INV)) {
					if (!recInvColl.getDataAgg().equals(DateUtil.formattaData(inventario.getTs_var()))) {
						throw new DataException("invInvModAltroUt");
					}
					if (inventario.getFl_canc() == 'S') {
						throw new DataException("invCancellato");
					}
				}
				trasformaInvVOHib(recInvColl, inventario);
				// preparara record da aggiornare
				if (recInvColl.getPrecInv().trim().equals("")) {
					inventario.setPrecis_inv("$");
				}
				if (recInvColl.getDataCarico().trim().equals("")
						|| recInvColl.getDataCarico().trim().equals("0000-00-00")) {
//					inventario.setData_carico(Timestamp.valueOf("9999-12-31"));
					inventario.setData_carico(DateUtil.toTimestamp("9999-12-31"));

				}
				if (recInvColl.getDataScarico().trim().equals("")
						|| recInvColl.getDataScarico().trim().equals("0000-00-00")) {
					inventario.setData_scarico(Timestamp.valueOf("9999-12-31"));
				}
				if (recInvColl.getDataDelibScar().trim().equals("")
						|| recInvColl.getDataDelibScar().trim()
						.equals("0000-00-00")) {
					inventario.setData_delibera(Timestamp.valueOf("9999-12-31"));
				}
				// perform fattura
				// trattamento fattura
				boolean fatturaVeloce = !recInvColl.getNumFattura().trim().equals("")
				&& (Integer.parseInt(recInvColl.getProgrFattura()) == 0)
				&&  (Integer.parseInt(recInvColl.getRigaFattura()) == 0);

				if (fatturaVeloce && !fatturaPresenteDB) {
					valida.validaInvFattura(recInvColl);
					ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();

					ricercaFatture.setCodPolo(recInvColl.getCodPolo());

					ricercaFatture.setCodBibl(recInvColl.getCodBib());

					ricercaFatture.setChiamante("");

					ricercaFatture.setFornitore(new StrutturaCombo(recInvColl.getCodFornitore(), ""));

					ricercaFatture.setOrdine(new StrutturaTerna(recInvColl.getCodTipoOrd(),
							recInvColl.getAnnoOrd(), recInvColl.getCodOrd()));

					ricercaFatture.setDataFattura(recInvColl.getDataFattura());
					//					ricercaFatture.setAnnoFattura(recInvColl.getDataFattura().substring(6));
					ricercaFatture.setAnnoFattura(recInvColl.getAnnoFattura());
					ricercaFatture.setImportoFattura(recInvColl.getImportoDouble());

					ricercaFatture.setNumFattura(recInvColl.getNumFattura());

					ricercaFatture.setTipoFattura("F");


					ricercaFatture.setUtente(recInvColl.getUteAgg());

					ListaSuppFatturaVO fattura = acquisizioni.gestioneFatturaDaDocFisico(ricercaFatture);

					if (fattura.getSelezioniChiamato() == null){
						throw new DataException("erroreInserimentoFattura");
					}else{
						FatturaVO fatturaCreata = fattura.getSelezioniChiamato().get(0);
						inventario.setCd_bib_fatt(fatturaCreata.getCodBibl());
						inventario.setAnno_fattura(Integer.parseInt(fatturaCreata.getAnnoFattura()));
						inventario.setPrg_fattura(Integer.parseInt(fatturaCreata.getProgrFattura()));
						int numRiga = fatturaCreata.getRigheDettaglioFattura().size();
						StrutturaFatturaVO riga = (fatturaCreata.getRigheDettaglioFattura().get(numRiga - 1));
						inventario.setRiga_fattura(riga.getRigaFattura());
					}
				}
				if (recColl != null){
					if (inventario.getKey_loc() != null) {
						if (inventario.getKey_loc().getKey_loc() == recColl.getKeyColloc()) {
							// la collocazione non è cambiata: aggiorna solo la consistenza
							oper = "=";
							recColl.setKeyColloc(inventario.getKey_loc().getKey_loc());
							//prepara Collocazione
							preparaCollocazione(inventario, recColl, tipoOperazione, oper);
							//finisce di preparare inventario e aggiorna collocazione e inventario
							ret = aggiornaInventario(inventario, recColl, oper,	tipoOperazione);
							if (!ret) {
								//								return false;
								return keyLoc = -1;
							}else{
								if (inventario.getKey_loc() != null){
									keyLoc = inventario.getKey_loc().getKey_loc();
								}else{
									keyLoc = 0;//inventario non collocato
								}
							}
							return keyLoc;
						}
					}
					if (recColl.getKeyColloc() == 0) {
						if (recColl.getCodBibSez() == null || recColl.getCodBibSez().trim().equals("")) {
							//
							//						if (recColl.getCodSez().trim().equals("")) {
							//							return aggiornaInventario(inventario, recColl, oper, tipoOperazione);
							ret = aggiornaInventario(inventario, recColl, oper, tipoOperazione);
							if (!ret) {
								return keyLoc = -1;
							}else{
								if (inventario.getKey_loc() != null){
									keyLoc = inventario.getKey_loc().getKey_loc();
								}else{
									keyLoc = 0;//inventario non collocato
								}
							}
							return keyLoc;
						}else{
							// provengo da gestioneColl ed ho codSez, codLoc... ma
							// non ho la keyLoc
							// getSezione
							// updateSezione
							// insertCollocazione
							collocazione = creaCollocazione(inventario, recColl, tipoOperazione);
							//if ret{
							//
							if (collocazione == null) {
								//è andata male la creazione della collocazione
								return keyLoc = -1;
							} else {
								inventario.setKey_loc(collocazione);
								//								return aggiornaInventario(inventario, recColl, oper, tipoOperazione);
								ret = aggiornaInventario(inventario, recColl, oper, tipoOperazione);
								if (!ret) {
									//è andato male l'aggiornamento dell'inventario con i dati della collocazione
									keyLoc = -2;
								}else{
									if (inventario.getKey_loc() != null){
										keyLoc = inventario.getKey_loc().getKey_loc();
									}else{
										keyLoc = 0;//inventario non collocato
									}
								}
								return keyLoc;
							}
						}
					}
					if (inventario.getKey_loc() != null) {
						// la collocazione è diversa scolloca dalla vecchia

						oper = "-";
						preparaCollocazione(inventario, recColl, tipoOperazione, oper);
						daoColl.modificaCollocazione(inventario.getKey_loc());
						if (!ret) {
							keyLoc = -1;
						}else{
							if (inventario.getKey_loc() != null){
								keyLoc = inventario.getKey_loc().getKey_loc();
							}else{
								keyLoc = 0;//inventario non collocato
							}
						}
						return keyLoc;
					}
					ret = false;
					oper = "+";
					preparaCollocazione(inventario, recColl, tipoOperazione, oper);
					ret = aggiornaInventario(inventario, recColl, oper, tipoOperazione);
					if (!ret) {
						keyLoc = -1;
					}else{
						if (inventario.getKey_loc() != null){
							keyLoc = inventario.getKey_loc().getKey_loc();
						}else{
							keyLoc = 0;//inventario non collocato
						}
					}
					return keyLoc;
				}else{
					ret = aggiornaInventario(inventario, recColl, oper, tipoOperazione);
					if (!ret) {
						keyLoc = -1;
					}else{
						if (inventario.getKey_loc() != null){
							keyLoc = inventario.getKey_loc().getKey_loc();
						}else{
							keyLoc = 0;//inventario non collocato
						}
					}
					return keyLoc;
				}
			} else {
				throw new DataException("invNonEsistente");
			}

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		//		if (ret){
		//			return keyLoc = collocazione.getKey_loc();
		//		}else{
		//			return keyLoc = 0;//per esempl.
		//		}
		//		return ret;
	}

	/**
	 * @param recInvColl
	 * @param ts
	 * @param inventario
	 * @throws DaoManagerException
	 */
	private void trattamentoInsModNoteInventario(InventarioVO recInvColl,
			Timestamp ts, Tbc_inventario inventario) throws DaoManagerException {
		//trattamento note
		//
		cancellaNoteInventario(recInvColl, ts, inventario);
		//scorre la lista delle note provenienti da input e controllo di ognuna l'esistenza sul db
		//senza considerare fl_canc, se è presente recupero l'occorrenza e l'aggiorno con i dati di input
		if (recInvColl.getListaNote() != null && recInvColl.getListaNote().size() > 0){
			daoNotaInv = new Tbc_nota_invDao();
			for (int index = 0; index < recInvColl.getListaNote().size(); index++) {
				NotaInventarioVO notaInput = recInvColl.getListaNote().get(index);
				Tbc_nota_inv notaDB = daoNotaInv.getNotaNoFlCanc(inventario, notaInput.getCodNota());
				if (notaDB == null){
					//inserisco la nuova nota
					Tbc_nota_inv nota = new Tbc_nota_inv();
					//
					Tbc_inventario inv =  new Tbc_inventario();
					inv.setCd_serie(inventario.getCd_serie());
					inv.setCd_inven(inventario.getCd_inven());
					//
					nota.setCd_polo(inv);
					nota.setCd_nota(notaInput.getCodNota());
					nota.setDs_nota_libera(notaInput.getDescrNota());
					nota.setFl_canc('N');
					nota.setTs_ins(ts);
					nota.setUte_ins(notaInput.getUteAgg());
					nota.setTs_var(ts);
					nota.setUte_var(notaInput.getUteAgg());
					daoNotaInv = new Tbc_nota_invDao();
					Boolean retInsNota = daoNotaInv.inserimentoNota(nota);
				}else{
					//aggiorno la nota esistente anche se cancellata logicamente
					notaDB.setDs_nota_libera(notaInput.getDescrNota());
					notaDB.setFl_canc('N');
					notaDB.setTs_var(ts);
					notaDB.setUte_var(notaInput.getUteAgg());
					daoNotaInv = new Tbc_nota_invDao();
					Boolean retModNota = daoNotaInv.modificaNota(notaDB);
				}
			}
			return;
		}
		//fine trattamento note
	}

	/**
	 * @param recInvColl
	 * @param ts
	 * @param inventario
	 * @throws DaoManagerException
	 */
	private void cancellaNoteInventario(InventarioVO recInvColl, Timestamp ts,
			Tbc_inventario inventario) throws DaoManagerException {
		daoNotaInv = new Tbc_nota_invDao();
		List listaNoteDB = new ArrayList<Tbc_nota_inv>();
		listaNoteDB = daoNotaInv.getNote(inventario);
		//cancella sempre logicamente le note esistenti anche se la lista che arriva da input è vuota
		if (listaNoteDB.size() >= 1){
			boolean retNota = false;
			for (int index = 0; index < listaNoteDB.size(); index++) {
				Tbc_nota_inv recResult = (Tbc_nota_inv)listaNoteDB.get(index);
				recResult.setFl_canc('S');
				recResult.setUte_var(recInvColl.getUteAgg());
				recResult.setTs_var(ts);
				if (retNota = daoNotaInv.modificaNota(recResult));
			}
		}
	}

	private boolean gestioneColl(InventarioVO recInvColl,
			CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		String oper = null;
		Tbc_inventario inventario = null;
		try {
			valida.validaInventarioKey(recInvColl);
			daoInv = new Tbc_inventarioDao();
			inventario = daoInv.getInventario(recInvColl.getCodPolo(),
					recInvColl.getCodBib(), recInvColl.getCodSerie(),
					recInvColl.getCodInvent());
			if (inventario != null) {
				if (tipoOperazione.equals("S")) {
					oper = "-";
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_PRECISATO_CHR);
					inventario.setUte_var(DaoManager.getFirmaUtente(ticket));
				} else if (tipoOperazione.equals("N")) {
					oper = "+";
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR);
					inventario.setUte_var(DaoManager.getFirmaUtente(ticket));
				}else if (tipoOperazione.equals("scarico")){
					oper = "-";
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_DISMESSO_CHR);
					inventario.setUte_var(recInvColl.getUteAgg());

					if (recInvColl.getCodScarico().equals("T")){
						inventario.setId_bib_scar(recInvColl.getIdBibScar());
						inventario.setCd_bib_scar(recInvColl.getCodBibS().toUpperCase());
						inventario.setCd_polo_scar(recInvColl.getCodPoloScar().toUpperCase());
					}else{
						inventario.setId_bib_scar(null);
						inventario.setCd_bib_scar(" ");
						inventario.setCd_polo_scar(" ");
					}
					inventario.setCd_scarico(recInvColl.getCodScarico().charAt(0));
					inventario.setNum_scarico(Integer.valueOf(recInvColl.getNumScarico()));
					inventario.setDelibera_scar(recInvColl.getDeliberaScarico());
					//
					inventario.setData_delibera(DateUtil.toTimestamp(recInvColl.getDataDelibScar()));
					inventario.setData_scarico(DateUtil.toTimestamp(recInvColl.getDataScarico()));
					//
					inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_DISMESSO_CHR);//dismesso
					inventario.setFlg_disp('N');
					//
					inventario.setUte_var(recInvColl.getUteAgg());
				}
				preparaCollocazione(inventario, recColl, tipoOperazione, oper);
				if (tipoOperazione.equals("S") || tipoOperazione.equals("scarico")) {
					cancellaEsemplare(inventario, ticket);
					inventario.setKey_loc(null);
				}
				daoInv = new Tbc_inventarioDao();
				if (daoInv.modificaInventario(inventario)) {
					ret = true;
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	private void preparaCollocazione(Tbc_inventario inventario,
			CollocazioneVO recColl,  String tipoOperazione, String oper )
	throws ValidationException, DataException {
		Tbc_collocazione collocazione = null;
		try {
			Timestamp ts = DaoManager.now();

			//			if (inventario.getKey_loc() == null) {
			//				if (oper.equals("+")) {
			//					throw new DataException("collCollocazioneInesistente");
			//				}else{
			//					inventario.setKey_loc(collocazione);
			//				}
			//			}
			if (inventario.getKey_loc() == null) {
				daoColl = new Tbc_collocazioneDao();
				collocazione = daoColl.getCollocazione(recColl.getKeyColloc());
				if (collocazione != null) {
					inventario.setKey_loc(collocazione);
				}else{
					if (oper.equals("+")) {
						throw new DataException("collCollocazioneInesistente");
					}
				}
			}
			if (!oper.equals("=")) {
				int tot_inv = inventario.getKey_loc().getTot_inv();
				if (oper.equals("+")) {
					inventario.getKey_loc().setTot_inv(++tot_inv);
				} else {
					inventario.getKey_loc().setTot_inv(--tot_inv);
				}
				Integer tot_inv_prov = inventario.getKey_loc().getTot_inv_prov();
				if (tot_inv_prov == null)
					tot_inv_prov = 0;
				if (tot_inv < 1
						&& tot_inv_prov.intValue() < 1) {
					//cancella la collocazione rimasta vuota
					inventario.getKey_loc().setFl_canc('S');
				} else {
					if (recColl.getConsistenza() != null && recColl.getConsistenza().trim().equals("")) {
						// aggiorna la consistenza
						inventario.getKey_loc().setConsis("$");
					} else {
						if (!tipoOperazione.equals("C_INV")) {
							inventario.getKey_loc().setConsis(recColl.getConsistenza());
						}
					}
				}
			}
			// aggiorna il contatore degli inventari sulla sezione
			if (oper.equals("=")) {
				if (recColl.getConsistenza().trim().equals("")) {
					// aggiorna la consistenza
					inventario.getKey_loc().setConsis("$");
				} else {
					if (!tipoOperazione.equals("C_INV")) {
						inventario.getKey_loc().setConsis(recColl.getConsistenza());
					}
				}
				return;
			}
			int totInvSez = inventario.getKey_loc().getCd_sez().getTot_inv();
			if (oper.equals("+")) {
				totInvSez++;
			} else if (oper.equals("-")){
				totInvSez--;
			}
			inventario.getKey_loc().setTs_var(ts);
			inventario.getKey_loc().setUte_var(recColl.getUteAgg());
			inventario.getKey_loc().getCd_sez().setTot_inv(totInvSez);
			inventario.getKey_loc().getCd_sez().setUte_var(recColl.getUteAgg());
			inventario.getKey_loc().getCd_sez().setTs_var(ts);

		} catch (DataException e) {
			throw e;
			//		} catch (DaoManagerException e) {
			//
			//			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
	}

	private void cancellaEsemplare(Tbc_inventario recInvColl, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		Timestamp ts = null;
		try {
			// recupero dati per la ricerca dell'esemplare
			if (recInvColl.getKey_loc() != null){
				daoColl = new Tbc_collocazioneDao();
				Tbc_collocazione collocazione = daoColl.getCollocazione(recInvColl.getKey_loc().getKey_loc());
				if (collocazione == null)
					return;

				if (collocazione.getFl_canc() != 'S')
					return;

				Tbc_esemplare_titolo esemplare = collocazione.getCd_biblioteca_doc();
				if (esemplare == null || esemplare.getFl_canc() == 'S')
					// non esiste esemplare per la collocazione
					return;

				// cancellazione: deve verificare se ci sono altre
				// collocazioni collegate
				daoColl = new Tbc_collocazioneDao();
				int listaCollocazioni = daoColl.countAltreCollocazioniPerEsemplare(collocazione);
				if (listaCollocazioni > 0)
					// non è possibile cancellare l'esemplare perchè legato
					// ad altre collocazioni
					return;

				// setto dataAgg e fl_canc='S' su
				// tbc_inventario.tbc_collocazione.tbc_esemplare
				ts = DaoManager.now();
				recInvColl.getKey_loc().getCd_biblioteca_doc().setTs_var(ts);
				recInvColl.getKey_loc().getCd_biblioteca_doc().setUte_var(DaoManager.getFirmaUtente(ticket));
				recInvColl.getKey_loc().getCd_biblioteca_doc().setFl_canc('S');

			}
			// return ret;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
	}

	private boolean aggiornaInventario(Tbc_inventario inventario,
			CollocazioneVO recColl, String oper, String tipoOperazione)
	throws ResourceNotFoundException, DataException,
	ValidationException, DaoManagerException {
		boolean ret = false;
		try {
			if (!inventario.getCd_scarico().equals(Character.valueOf(' '))) {
				// scarico inventariale deve scollocare l'inventario
				inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_DISMESSO_CHR);
				inventario.setFlg_disp('N');
				if (inventario.getKey_loc() != null) {
					oper = "-";
					preparaCollocazione(inventario, recColl, tipoOperazione, oper);
					inventario.setKey_loc(null);
				}
			}
			if (inventario.getKey_loc() != null) {
				if (inventario.getTs_ins_prima_coll() == null ) {
					inventario.setUte_ins_prima_coll(recColl.getUteAgg());
					inventario.setTs_ins_prima_coll(inventario.getKey_loc().getTs_ins());
				}else{
					inventario.setUte_ins_prima_coll(recColl.getUteAgg());
				}
				inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR);
			}
			daoInv = new Tbc_inventarioDao();
			ret = daoInv.modificaInventario(inventario);
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	public Tbc_collocazione creaCollocazione(Tbc_inventario inventario,
			CollocazioneVO recColl, String tipoOperazione)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		boolean ret = false;
		String oper = null;
		Timestamp ts = DaoManager.now();
		Tbc_sezione_collocazione sezione = null;
		Tbc_collocazione collocazione = null;
		int serie = 0;
		int num = 0;
		try {
			SezioneCollocazioneVO recSez = new SezioneCollocazioneVO();
			recSez.setCodPolo(recColl.getCodPoloSez());
			recSez.setCodBib(recColl.getCodBibSez());
			recSez.setCodSezione(recColl.getCodSez());
			valida.validaSezione(recSez);
			daoSez = new Tbc_sezione_collocazioneDao();
			sezione = daoSez.getSezione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez());
			if (sezione != null) {
				//trattamento di calcolo serie/progressivo per formatiSezione per assegnare il codColloc e il codSpec
				//è posizionato qui perchè mi serve il tipo coll di sezione
				if (recColl.getRecFS() != null){
					//					dfCommon.assegnaSerieProgr(recColl, ts, sezione, serie, num);
					if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO.charAt(0)
							|| sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_CONTINUAZIONE).charAt(0)
							|| sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO).charAt(0)){
						if (recColl.getRecFS() != null){
							//trattamento di calcolo serie/progressivo per formatiSezione per assegnare il codColloc e il codSpec
							//è posizionato qui perchè mi serve il tipo coll di sezione
							dfCommon.assegnaSerieProgr(recColl, ts, sezione, serie, num, inventario.getCd_mat_inv());
							daoColl =  new Tbc_collocazioneDao();
							if (sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO.charAt(0))
									//									|| sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_CONTINUAZIONE).charAt(0)
									//							c'è la necessità di assegnare la stessa collocazione a titoli diversi nel
									//							caso di cambiamento di titolo della rivista bug #0002965 esercizio
							){
								if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
										(recColl.getRecFS().getCodFormato()+String.valueOf(recColl.getRecFS().getProgSerie())), String.valueOf(recColl.getRecFS().getProgNum()))){
									throw new ValidationException("msgCodLocEsistente",
											ValidationException.errore, "msgCodLocEsistente", (recColl.getRecFS().getCodFormato()+String.valueOf(recColl.getRecFS().getProgSerie())), String.valueOf(recColl.getRecFS().getProgNum()));
								}
							}else{
								if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
										(String.valueOf(recColl.getRecFS().getProgNum())), "")){
									throw new ValidationException("msgCodLocEsistente",
											ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getRecFS().getProgNum())), "");
								}
							}
							//fine trattamento di calcolo serie/progressivo per formatiSezione
						}
					}
				}
				//fine trattamento di calcolo serie/progressivo per formatiSezione
				if (inventario.getKey_loc() != null) {
					// aggiorna la collocazione precedente
					oper = "-";
					preparaCollocazione(inventario, recColl, tipoOperazione, oper);
				}
				collocazione = preparazioneInserimentoCollocazione(sezione, recColl,tipoOperazione);
				if (collocazione != null) {
					// prelevo l'oggetto sezione
					// modifico il record con +1
					// inserisco l'oggetto collocazione
					// salvo la sezione
					if (oper != null && oper.equals("-")){
					}else{
						sezione.setTot_inv(sezione.getTot_inv() + 1);
					}
					sezione.setUte_var(recColl.getUteAgg());
					sezione.setTs_var(ts);
					sezione.getTbc_collocazione().add(collocazione);
					daoSez = new Tbc_sezione_collocazioneDao();
					//modifico la sezione
					ret = daoSez.saveUpdateSezione(sezione);
					if (ret) {
						return  collocazione;
					}
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return collocazione;
	}


	private Tbc_collocazione preparazioneInserimentoCollocazione(
			Tbc_sezione_collocazione sezione, CollocazioneVO recColl,
			String tipoOperazione) throws DataException, ApplicationException,
			ValidationException {

		Tbc_collocazione collocazione = null;
		String locNormalizzata = null;
		String specNormalizzata = null;
		Timestamp ts = DaoManager.now();
		try {
			valida.validaCollocazione(recColl);
			// campi ordinamento collocazione
			// tipoElab = "G";
			if (ValidazioneDati.isFilled(recColl.getCodColloc())) {
				String locDaNormalizzare = recColl.getCodColloc();
				try {
					if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE.charAt(0)){
						locNormalizzata = (OrdinamentoCollocazione2.normalizza(locDaNormalizzare, true));
					}else{
						locNormalizzata = (OrdinamentoCollocazione2.normalizza(locDaNormalizzare));
					}
				} catch (Exception e) {

				}
			} else {
				locNormalizzata = ValidazioneDati.fillRight("", ' ', 80);
			}
			if (ValidazioneDati.isFilled(recColl.getSpecColloc())) {
				String specDaNormalizzare = recColl.getSpecColloc();
				if (sezione.getCd_colloc() == DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE.charAt(0)){
					specNormalizzata = (OrdinamentoCollocazione2.normalizza(specDaNormalizzare, true));
				}else{
					specNormalizzata = (OrdinamentoCollocazione2.normalizza(specDaNormalizzare));
				}
			} else {
				specNormalizzata = ValidazioneDati.fillRight("", ' ', 40);
			}
			collocazione = new Tbc_collocazione();
			collocazione.setCd_sez(sezione);
			if (recColl.getCodSistema() != null) {
				collocazione.getCd_sistema().setCd_sistema(recColl.getCodSistema());
				collocazione.getCd_sistema().setCd_edizione(recColl.getCodEdizione());
				collocazione.getCd_sistema().setClasse(recColl.getClasse());
			} else {
				collocazione.setCd_sistema(null);
			}
			if (recColl.getCodBibDoc() != null) {
				collocazione.getCd_biblioteca_doc().getCd_polo().getCd_polo().setCd_polo(recColl.getCodPoloDoc());
				collocazione.getCd_biblioteca_doc().getCd_polo().setCd_biblioteca(recColl.getCodBibDoc());
				collocazione.getCd_biblioteca_doc().getB().setBid(recColl.getBid());
				collocazione.getCd_biblioteca_doc().setCd_doc(recColl.getCodDoc().intValue());
			} else {
				collocazione.setCd_biblioteca_doc(null);
			}
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(recColl.getBid());
			collocazione.setB(titolo);
			collocazione.setCd_loc(recColl.getCodColloc());
			collocazione.setSpec_loc(recColl.getSpecColloc());
			if (recColl.getConsistenza().trim().equals("")) {
				collocazione.setConsis("$");
			}else{
				collocazione.setConsis(recColl.getConsistenza());
			}
			collocazione.setTot_inv(1);
			if (recColl.getIndice() != null) {
				collocazione.setIndice(recColl.getIndice());
			} else {
				collocazione.setIndice(null);
			}
			collocazione.setOrd_loc(locNormalizzata);
			collocazione.setOrd_spec(specNormalizzata);
			if (recColl.getTotInvProv() != null) {
				collocazione.setTot_inv_prov(recColl.getTotInvProv());
			} else {
				collocazione.setTot_inv_prov(0);
			}
			collocazione.setTs_ins(ts);
			collocazione.setTs_var(ts);
			collocazione.setUte_ins(recColl.getUteIns());
			collocazione.setUte_var(recColl.getUteAgg());
			collocazione.setFl_canc(' ');
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		}
		return collocazione;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean deleteInventario(InventarioVO recInvColl,
			CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		Timestamp ts = null;
		String oper = null;
		Tbc_inventario inventario = null;
		Tbc_collocazione collocazione = null;
		int keyLoc = 0;
		valida.validaInventarioKey(recInvColl);
		try {
			daoInv = new Tbc_inventarioDao();
			inventario = daoInv.getInventario(recInvColl.getCodPolo(),
					recInvColl.getCodBib(), recInvColl.getCodSerie(),
					recInvColl.getCodInvent());
			// inventario = this.getInventario(recInvColl);
			if (inventario != null) {
				// controllo se è in prestito
				// richiama un ejb di servizi che conta i movimenti aperti
				//				int movA = contaMovimentiAperti(inventario.getCd_serie()
				//						.getCd_polo().getCd_polo().getCd_polo(), inventario
				//						.getCd_serie().getCd_polo().getCd_biblioteca(),
				//						inventario.getCd_serie().getCd_serie(), inventario
				//								.getCd_inven());
				int movA = servizi.getNumeroMovimentiAttivi(inventario.getCd_serie()
						.getCd_polo().getCd_polo().getCd_polo(), inventario
						.getCd_serie().getCd_polo().getCd_biblioteca(),
						inventario.getCd_serie().getCd_serie(), inventario
						.getCd_inven());
				if (movA > 0) {
					// inventario in prestito
					throw new ValidationException("invInPrestito",
							ValidationException.errore);
				}

				//almaviva5_20100217 controllo esistenza legami con possessori
				Trc_poss_prov_inventariDao possDao = new Trc_poss_prov_inventariDao();
				int legPoss = possDao.countLegamiPerInventario(inventario.getCd_serie()
						.getCd_polo().getCd_polo().getCd_polo(), inventario
						.getCd_serie().getCd_polo().getCd_biblioteca(),
						inventario.getCd_serie().getCd_serie(), inventario
						.getCd_inven());
				if (legPoss > 0) {
					// inventario legato a possessore
					if (!possDao.deleteLegamiPerInventario(inventario.getCd_serie()
							.getCd_polo().getCd_polo().getCd_polo(), inventario
							.getCd_serie().getCd_polo().getCd_biblioteca(),
							inventario.getCd_serie().getCd_serie(), inventario
							.getCd_inven(), recInvColl.getUteAgg() ) )
						throw new ValidationException("invLegamePossessore",
								ValidationException.errore);
				}
				//

				//almaviva5_20101026 cancellazione legami a fascicoli (solo se natura='S')
				if (ValidazioneDati.equals(inventario.getB().getCd_natura(), 'S')) {
					PeriodiciDAO pDao = new PeriodiciDAO();
					pDao.deleteLegamiEsemplariFascicolo(inventario, recInvColl.getUteAgg());
				}
				//

				//cancellazione note
				cancellaNoteInventario(recInvColl, ts, inventario);
				//
				if (inventario.getKey_loc() != null) {
					oper = "-";
					// inventario collocato: deve aggiornare il totale sul
					// record di collocazione
					preparaCollocazione(inventario, recColl,
							tipoOperazione, oper);
					cancellaEsemplare(inventario, ticket);
				}
				// aggiorna soltanto tbc_inventario
				ts = DaoManager.now();
				// collocazione = inventario.getKey_loc();
				// collocazione.getTbc_inventario().remove(inventario);
				if (inventario.getKey_loc() != null) {
					daoColl = new Tbc_collocazioneDao();
					daoColl.modificaCollocazione(inventario.getKey_loc());
				}
				inventario.setKey_loc(null);
				inventario.setFl_canc('S');
				inventario.setTot_loc(0);
				inventario.setTot_inter(0);
				inventario.setSeq_coll("");
				inventario.setCd_proven(null);
				inventario.setCd_fornitore(0);
				inventario.setB(null);
				inventario.setCd_bib_ord(null);
				inventario.setCd_tip_ord(null);
				inventario.setAnno_ord(null);
				inventario.setCd_ord(null);
				inventario.setCd_bib_fatt(null);
				inventario.setAnno_fattura(null);
				inventario.setPrg_fattura(null);
				inventario.setRiga_fattura(null);
				inventario.setCd_sit(DocFisico.Inventari.INVENTARIO_CANCELLATO_CHR);
				inventario.setTs_var(ts);
				inventario.setUte_var(recInvColl.getUteAgg());
				daoInv = new Tbc_inventarioDao();
				if (daoInv.modificaInventario(inventario)) {
					ret = true;
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean aggiornaInventarioFattura(InventarioVO recInv, String tipoOp, String ticket)
	throws ResourceNotFoundException, DataException,
	ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		Timestamp ts = null;
		String oper = null;
		Tbc_inventario inventario = null;
		Tbc_collocazione collocazione = null;
		int keyLoc = 0;
		valida.validaInventarioKey(recInv);
		try {
			daoInv = new Tbc_inventarioDao();
			inventario = daoInv.getInventario(recInv.getCodPolo(),
					recInv.getCodBib(), recInv.getCodSerie(),
					recInv.getCodInvent());
			if (inventario != null) {
				ts = DaoManager.now();
				if (tipoOp.equals("C")){////agg. inv canc. solo dati fattura
					inventario.setCd_bib_fatt("");
					inventario.setAnno_fattura(null);
					inventario.setPrg_fattura(null);
					inventario.setRiga_fattura(null);
				} else if (tipoOp.equals("I")){//agg. inv con soli dati fattura + prezzo
					inventario.setImporto(new BigDecimal(recInv.getImportoDouble()));
					inventario.setCd_bib_fatt(recInv.getCodBibF());
					this.controlloDatiFattura(recInv, inventario);
				} else if (tipoOp.equals("R")){//agg. inv con soli dati fattura
					inventario.setCd_bib_fatt(recInv.getCodBibF());
					this.controlloDatiFattura(recInv, inventario);
				}
				inventario.setTs_var(ts);
				inventario.setUte_var(recInv.getUteAgg());
				daoInv = new Tbc_inventarioDao();
				if (daoInv.modificaInventario(inventario)) {
					ret = true;
				}
			}else{
				throw new DataException("invNonEsistente");
			}
			//		} catch (ValidationException e) {
			//			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	/**
	 * @param recInv
	 * @param inventario
	 * @throws DataException
	 */
	private void controlloDatiFattura(InventarioVO recInv,
			Tbc_inventario inventario) throws DataException {
		if ((recInv.getAnnoFattura() != null && ValidazioneDati.strIsNumeric(recInv.getAnnoFattura()) && Integer.valueOf(recInv.getAnnoFattura()) > 0) &&
				(ValidazioneDati.strIsNumeric(recInv.getProgrFattura()) && Integer.valueOf(recInv.getProgrFattura()) > 0) &&
				(ValidazioneDati.strIsNumeric(recInv.getRigaFattura()) && Integer.valueOf(recInv.getRigaFattura()) > 0)){
			inventario.setAnno_fattura((Integer.valueOf(recInv.getAnnoFattura())));
			inventario.setPrg_fattura((Integer.valueOf(recInv.getProgrFattura())));
			inventario.setRiga_fattura((Integer.valueOf(recInv.getRigaFattura())));
		}else{
			throw new DataException("erroreValidazioneDatiFattura");
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaProvenienze(String codPolo, String codBib,
			String ticket, String filtroProvenienza) throws ResourceNotFoundException,
			ApplicationException, ValidationException, RemoteException {
		List listaProvenienze = null;
		try {
			checkTicket(ticket);
			ProvenienzaInventarioVO proven = new ProvenienzaInventarioVO();
			proven.setCodPolo(codPolo);
			proven.setCodBib(codBib);
			valida.validaProvenienzaInventarioLista(proven);
			List listaProv = new ArrayList();
			daoProven = new Tbc_provenienza_inventarioDao();
			listaProv = daoProven.getListaProvenienze(codPolo, codBib, filtroProvenienza);
			if (listaProv.size() > 0) {
				listaProvenienze = new ArrayList();
				ProvenienzaInventarioVO rec = null;
				for (int index = 0; index < listaProv.size(); index++) {
					Tbc_provenienza_inventario recResult = (Tbc_provenienza_inventario)listaProv.get(index);
					rec = new ProvenienzaInventarioVO();
					rec.setPrg(index + 1);
					rec.setCodProvenienza(recResult.getCd_proven());
					rec.setDescrProvenienza(String
							.valueOf(recResult.getDescr()));
					rec.setDataIns(String.valueOf(recResult.getTs_ins()));
					rec.setDataAgg(String.valueOf(recResult.getTs_var()));
					rec.setUteAgg(recResult.getUte_var());
					rec.setUteIns(recResult.getUte_ins());
					rec.setCodBib(recResult.getCd_polo().getCd_biblioteca());
					rec.setCodPolo(recResult.getCd_polo().getCd_polo()
							.getCd_polo());
					listaProvenienze.add(rec);
				}

			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaProvenienze;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean insertProvenienza(ProvenienzaInventarioVO recProven,
			String ticket) throws DataException, ApplicationException,
			ValidationException {
		checkTicket(ticket);
		Tbc_provenienza_inventario provenienza = null;
		boolean ret = false;
		try {
			valida.validaProvenienzaInventario(recProven);
			daoProven = new Tbc_provenienza_inventarioDao();
			provenienza = daoProven.getProvenienza(recProven.getCodPolo(),
					recProven.getCodBib(), recProven.getCodProvenienza());
			if (provenienza == null) {
				daoProven = new Tbc_provenienza_inventarioDao();
				ret = daoProven.inserimentoProvenienza(recProven);
			} else {
				throw new DataException("recEsistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean updateProvenienza(ProvenienzaInventarioVO recProven,
			String ticket) throws DataException, ApplicationException,
			ValidationException {
		checkTicket(ticket);
		/*
		 * public boolean updateFormati(FormatiVO formato, String ticket) throws
		 * DataException, ApplicationException, ValidationException { boolean
		 * ret = false; try{ valida.validaFormato(formato);
		 * Tbc_formato_collocazione recFor =
		 * daoFor.getFormato(formato.getCodPolo(), formato.getCodBib(),
		 * formato.getCodFormato()); if (recFor != null){
		 * if(!recFor.getTs_var().equals(formato.getDataAgg())){ throw new
		 * ValidationException("erroreFormatoModDaAltroUtente",
		 * ValidationException.errore); }else{ //modifica formatiSezione ret =
		 * daoFor.modificaFormato(formato); } }else{ throw new
		 * DataException("recInesistente"); } }catch (ValidationException e) {
		 * //logger.error("Errore in getBibliteche",e); throw e; } catch
		 * (DataException e) { // TODO Auto-generated catch block
		 *  } catch (Exception e) { // TODO Auto-generated
		 * catch block  } return ret; }
		 */
		boolean ret = false;
		try {
			valida.validaProvenienzaInventario(recProven);
			Tbc_provenienza_inventario provenienza = daoProven.getProvenienza(
					recProven.getCodPolo(), recProven.getCodBib(), recProven
					.getCodProvenienza());
			if (provenienza != null) {
				Timestamp ts = DaoManager.now();
				provenienza.setDescr(recProven.getDescrProvenienza());
				provenienza.setTs_var(ts);
				provenienza.setUte_var(recProven.getUteAgg());
				provenienza.setFl_canc('N');
				ret = daoProven.modificaProvenienza(provenienza);
			} else {
				throw new DataException("recInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws xception
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariNonCollocati(
			EsameCollocRicercaVO paramRicerca, String ticket)
	throws ApplicationException, ValidationException, DataException,
	RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List result = new ArrayList();
		List listaInvNonColl = null;
		InventarioListeVO rec = null;
		List listaTit = new ArrayList();
		try {
			valida.validaParamRicercaPerInvNonColl(paramRicerca);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventari(paramRicerca);
			if (result.size() > 0) {
				listaInvNonColl = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = (Tbc_inventario)result.get(index);
					rec = new InventarioListeVO();
					rec.setPrg(index + 1);
					rec.setCodSerie(recResult.getCd_serie().getCd_serie());
					rec.setCodInvent(recResult.getCd_inven());
					if (recResult.getB() != null){
						rec.setBid(recResult.getB().getBid());
						List titoli = null;
						try {
							titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block

						}
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								rec.setDescr(titolo.getIsbd());
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
					}else{
						rec.setDescr("Titolo non trovato in Polo");
					}
					//					rec.setBid(recResult.getB().getBid());
					//					rec.setDescr(recResult.getB().getIsbd());//ha sostituito la getTitolo()
					if (recResult.getPrecis_inv().equals("$")) {
						rec.setPrecInv("");
					} else {
						rec.setPrecInv(recResult.getPrecis_inv());
					}
					//					listaTit = getTitolo(recResult.getB().getBid(), ticket);
					//					if (listaTit != null) {
					//						recTit = (TitoloVO) (TitoloVO) listaTit.get(0);
					//					}
					//					if (listaTit != null) {
					//						rec.setDescr(recTit.getIsbd());
					//					} else {
					//						rec.setDescr("Titolo non trovato in Polo");
					//					}
					listaInvNonColl.add(rec);
				}

			} else {
				throw new DataException("inventariNotFound");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvNonColl;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariDiCollocazione(EsameCollocRicercaVO paramRicerca, String ticket)
	throws ApplicationException, ValidationException, DataException,
	RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List<Tbc_inventario> result = new ArrayList();
		List listaInvDiColloc = null;
		InventarioListeVO rec = null;
		List listaTit = new ArrayList();
		try {
			valida.validaParamRicercaPerInvDiColloc(paramRicerca);
			Tbc_collocazione collocazione = daoColl.getCollocazione(paramRicerca.getKeyLoc());
			if  (collocazione != null){
				daoInv = new Tbc_inventarioDao();
				result = daoInv.getListaInventari(collocazione, paramRicerca.getOrdLst());

				if (result.size() > 0) {
					listaInvDiColloc = new ArrayList();
					int countInv = 0;
					for (int index = 0; index < result.size(); index++) {
						Tbc_inventario recResult = result.get(index);
						rec = new InventarioListeVO();
						rec.setPrg(++countInv);
						//almaviva5_20100923 campi mancanti per gest.periodici
						rec.setCodPolo(recResult.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
						Integer anno_abb = recResult.getAnno_abb();
						if (anno_abb != null && anno_abb > 0)
							rec.setAnnoAbb(anno_abb.toString());
						//
						rec.setCodBib(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
						rec.setCodSerie(recResult.getCd_serie().getCd_serie());
						rec.setCodInvent(recResult.getCd_inven());
						if (recResult.getB() != null){
							rec.setBid(recResult.getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setDescr(titolo.getIsbd());
								}
							}else{
								rec.setDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
						//						rec.setBid(recResult.getB().getBid());
						//						rec.setDescr(recResult.getB().getIsbd());//ha sostituito la getTitolo()
						rec.setCodSit((String.valueOf(recResult.getCd_sit())));
						if (rec.isPrecisato() ) {
							rec.setSitAmm("precisato");
						}else if (rec.isCollocato() ) {
							rec.setSitAmm("collocato");
						}else{
							countInv--;
							continue;
						}
						rec.setSeqColl(recResult.getSeq_coll().trim());
						if (recResult.getPrecis_inv().equals("$")) {
							rec.setPrecInv("");
						} else {
							rec.setPrecInv(recResult.getPrecis_inv());
						}
						rec.setKeyLoc(recResult.getKey_loc().getKey_loc());
						rec.setKeyLocOld(recResult.getKey_loc_old());
						rec.setSezOld(recResult.getSez_old());
						rec.setLocOld(recResult.getLoc_old());
						rec.setSpecOld(recResult.getSpec_old());
						//almaviva5_20100924 aggiunti dati ordine per periodici
						if (ValidazioneDati.isFilled(recResult.getCd_bib_ord())) {
							rec.setCodBibO(recResult.getCd_bib_ord());
							rec.setAnnoOrd(recResult.getAnno_ord().toString());
							rec.setCodTipoOrd(recResult.getCd_tip_ord().toString());
							rec.setCodOrd(recResult.getCd_ord().toString());
						}
						listaInvDiColloc.add(rec);
					}
				} else {
					throw new DataException("inventariNotFound");
				}
			} else {
				throw new DataException("collocazioniNotFound");
			}

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvDiColloc;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariOrdine(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale,
			String ticket, int nRec)
	throws ApplicationException, ValidationException, DataException, RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List listaTit = new ArrayList();
		List result = new ArrayList();
		List listaInvOrdine = null;
		InventarioListeVO rec = null;
		try {
			InventarioVO recValid = new InventarioVO();
			recValid.setCodPolo(codPolo);
			recValid.setCodBib(codBib);
			recValid.setCodTipoOrd(codTipOrd);
			recValid.setAnnoOrd(String.valueOf(annoOrd));
			recValid.setCodOrd(String.valueOf(codOrd));
			recValid.setCodBibF(codBibF);
			valida.validaListaInventariOrdine(recValid);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventari(codPolo, codBib, codBibO, codTipOrd,
					annoOrd, codOrd, codBibF);
			if (result.size() > 0) {
				listaInvOrdine = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = (Tbc_inventario) result.get(index);
					if (recResult.getCd_sit() != DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
						rec = new InventarioListeVO();
						rec.setLocale(locale);
						rec.setPrg(index + 1);
						//almaviva5_20100923 campi mancanti per gest.periodici
						rec.setCodPolo(recResult.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBib(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
						rec.setCodTipoOrd(recResult.getCd_tip_ord().toString());
						rec.setAnnoOrd(recResult.getAnno_ord().toString());
						rec.setCodOrd(recResult.getCd_ord().toString());
						Tbc_collocazione coll = recResult.getKey_loc();
						if (coll != null) {
							rec.setCodSez(coll.getCd_sez().getCd_sez());
							rec.setCodLoc(coll.getCd_loc());
							rec.setCodSpec(coll.getSpec_loc());
							rec.setKeyLoc(coll.getKey_loc());
						}
						Integer anno_abb = recResult.getAnno_abb();
						if (anno_abb != null && anno_abb > 0)
							rec.setAnnoAbb(anno_abb.toString());
						//
						rec.setCodBibO(recResult.getCd_bib_ord());
						rec.setCodSerie(recResult.getCd_serie().getCd_serie());
						rec.setCodInvent(recResult.getCd_inven());
						rec.setSeqColl(recResult.getSeq_coll());
						if (recResult.getB() != null){
							rec.setBid(recResult.getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setDescr(titolo.getIsbd());
								}
							}else{
								rec.setDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
						//					rec.setBid(recResult.getB().getBid());
						//					rec.setDescr(recResult.getB().getIsbd());//ha sostituito la getTitolo()
						if (recResult.getPrecis_inv().equals("$")) {
							rec.setPrecInv("");
						} else {
							rec.setPrecInv(recResult.getPrecis_inv());
						}
						rec.setSeparatore("//");
						rec.setValoreDouble(recResult.getValore().doubleValue());
						rec.setImportoDouble(recResult.getImporto().doubleValue());
						//					listaTit = getTitolo(recResult.getB().getBid(), ticket);
						//					if (listaTit != null) {
						//						recTit = (TitoloVO) (TitoloVO) listaTit.get(0);
						//					}
						//					if (listaTit != null) {
						//						rec.setDescr(recTit.getIsbd());
						//					} else {
						//						rec.setDescr("Titolo non trovato in Polo");
					}
					//					}
					listaInvOrdine.add(rec);
				}


			} else {
				throw new DataException("inventariOrdineNonTrovato");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
			//		} catch (DaoManagerException e) {
			//
			//			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvOrdine;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariOrdini(List listaOrdini, Locale locale,
			String ticket, int nRec)
	throws ApplicationException, ValidationException, DataException, RemoteException {
		checkTicket(ticket);

		InventarioListeVO rec = null;
		List listaInvOrd = null;
		List listaInventariOutput = new ArrayList();
		try{

			for (int index = 0; index < listaOrdini.size(); index++) {
				StrutturaQuinquies recResult = (StrutturaQuinquies) listaOrdini.get(index);
				ListaSuppOrdiniVO ricercaOrdine = new ListaSuppOrdiniVO();
				ricercaOrdine.setCodPolo(recResult.getCodice4());
				ricercaOrdine.setCodBibl(recResult.getCodice5());
				ricercaOrdine.setTipoOrdine(recResult.getCodice1());
				ricercaOrdine.setAnnoOrdine(recResult.getCodice2());
				ricercaOrdine.setCodOrdine(recResult.getCodice3());
				ricercaOrdine.setTicket("");
				daoAcquisizioni = new GenericJDBCAcquisizioniDAO();
				List lista = daoAcquisizioni.getRicercaListaOrdini(ricercaOrdine);
				if (lista != null && lista.size() == 1)	{
					OrdiniVO recOrdine = (OrdiniVO) lista.get(0);
					daoInv = new Tbc_inventarioDao();
					listaInvOrd = daoInv.getListaInventari(recOrdine.getCodPolo(), recOrdine.getCodBibl(), recOrdine.getCodBibl(),
							recOrdine.getTipoOrdine(), Integer.valueOf(recOrdine.getAnnoOrdine()), Integer.valueOf(recOrdine.getCodOrdine()), "");
					if (listaInvOrd != null && listaInvOrd.size() > 0) {
						for (int index1 = 0; index1 < listaInvOrd.size(); index1++) {
							Tbc_inventario inv = (Tbc_inventario) listaInvOrd.get(index1);
							if (inv.getCd_sit() != DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
								rec = new InventarioListeVO();
								rec.setLocale(locale);
								rec.setPrg(index + 1);
								rec.setCodTipoOrd(recOrdine.getTipoOrdine());
								rec.setAnnoOrd(recOrdine.getAnnoOrdine());
								rec.setCodOrd(recOrdine.getCodOrdine());
								if (recOrdine.getTitolo() != null){
									rec.setBidOrd(recOrdine.getTitolo().getCodice());
									List titoli = null;
									try {
										titoli = dfCommon.getTitolo(recOrdine.getTitolo().getCodice(), ticket);
									} catch (RemoteException e) {
										// TODO Auto-generated catch block

									}
									if (titoli != null){
										if (titoli.size() > 0 && titoli.size() == 1){
											TitoloVO titolo = (TitoloVO)titoli.get(0);
											rec.setIsbdOrd(titolo.getIsbd());
										}
									}else{
										rec.setIsbdOrd("Titolo non trovato in Polo");
									}
								}else{
									rec.setIsbdOrd("Titolo non trovato in Polo");
								}

								rec.setCodSerie(inv.getCd_serie().getCd_serie());
								rec.setCodInvent(inv.getCd_inven());
								rec.setSeqColl(inv.getSeq_coll());
								//almaviva5_20100928 campi mancanti per periodici
								rec.setCodPolo(inv.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
								rec.setCodBib(inv.getCd_serie().getCd_polo().getCd_biblioteca());
								rec.setCodBibO(inv.getCd_bib_ord());
								rec.setAnnoOrd(inv.getAnno_ord().toString());
								rec.setCodTipoOrd(inv.getCd_tip_ord().toString());
								rec.setCodOrd(inv.getCd_ord().toString());
								Tbc_collocazione coll = inv.getKey_loc();
								if (coll != null) {
									rec.setCodSez(coll.getCd_sez().getCd_sez());
									rec.setCodLoc(coll.getCd_loc());
									rec.setCodSpec(coll.getSpec_loc());
									rec.setKeyLoc(coll.getKey_loc());
								}
								//
								if (inv.getB() != null) {
									//almaviva5_20100928 imposto titolo inv. solo se diverso da quello dell'ordine
									if (!inv.getB().getBid().equals(recOrdine.getTitolo().getCodice())) {
										rec.setBid(inv.getB().getBid());
										List titoli = null;
										try {
											titoli = dfCommon.getTitoloHib(
													inv.getB(), ticket);
										} catch (RemoteException e) {
											// TODO Auto-generated catch block

										}
										if (titoli != null) {
											if (titoli.size() > 0
													&& titoli.size() == 1) {
												TitoloVO titolo = (TitoloVO) titoli
												.get(0);
												rec.setDescr(titolo.getIsbd());
											}
										} else {
											rec.setDescr("Titolo non trovato in Polo");
										}
									}
								}else{
									rec.setDescr("Titolo non trovato in Polo");
								}
								if (inv.getPrecis_inv().equals("$")) {
									rec.setPrecInv("");
								} else {
									rec.setPrecInv(inv.getPrecis_inv());
								}
								if (ValidazioneDati.isFilled(rec.getPrecInv()))
									rec.setSeparatore("//");
								rec.setValoreDouble(inv.getValore().doubleValue());
								rec.setImportoDouble(inv.getImporto().doubleValue());
							}
							listaInventariOutput.add(rec);
						}
						//listaInventariOutput.add(rec);
					} else {
						rec = new InventarioListeVO();
						rec.setLocale(locale);
						rec.setPrg(index + 1);
						rec.setCodBibO(recOrdine.getCodBibl());
						rec.setCodTipoOrd(recOrdine.getTipoOrdine());
						rec.setAnnoOrd(recOrdine.getAnnoOrdine());
						rec.setCodOrd(recOrdine.getCodOrdine());
						if (recOrdine.getTitolo() != null){
							rec.setBidOrd(recOrdine.getTitolo().getCodice());
							List titoli = null;
							try {
								titoli = dfCommon.getTitolo(recOrdine.getTitolo().getCodice(), ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setIsbdOrd(titolo.getIsbd());
								}
							}else{
								rec.setIsbdOrd("Titolo non trovato in Polo");
							}
						}else{
							rec.setIsbdOrd("Titolo non trovato in Polo");
						}

						rec.setCodSerie("");
						rec.setCodInvent(0);
						rec.setSeqColl("");
						rec.setBid("");
						rec.setDescr("Nessun inventario legato all'ordine");
						rec.setPrecInv("");
						rec.setSeparatore("");
						rec.setValoreDouble(0);
						rec.setImportoDouble(0);
						listaInventariOutput.add(rec);
						continue;
					}
				} else {
					throw new DataException("listaOrdiniRestituisceRisultatoNonUnivoco");
				}
			}

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		}
		//ordinamento discendente ordini
		Collections.sort(listaInventariOutput, ValidazioneDati.invertiComparatore(InventarioListeVO.ORDINAMENTO_ORDINE_INVENTARIO));
		return listaInventariOutput;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariOrdiniNonFatturati(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale,
			String ticket, int nRec)
	throws ApplicationException, ValidationException, DataException, RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List listaTit = new ArrayList();
		List result = new ArrayList();
		List listaInvOrdine = null;
		InventarioListeVO rec = null;
		try {
			InventarioVO recValid = new InventarioVO();
			recValid.setCodPolo(codPolo);
			recValid.setCodBib(codBib);
			recValid.setCodTipoOrd(codTipOrd);
			recValid.setAnnoOrd(String.valueOf(annoOrd));
			recValid.setCodOrd(String.valueOf(codOrd));
			recValid.setCodBibF(codBibF);
			valida.validaListaInventariOrdine(recValid);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventariNonFatturati(codPolo, codBib, codBibO, codTipOrd,
					annoOrd, codOrd, codBibF);
			if (result.size() > 0) {
				listaInvOrdine = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = (Tbc_inventario) result.get(index);
					rec = new InventarioListeVO();
					rec.setLocale(locale);
					rec.setPrg(index + 1);
					rec.setCodBibO(recResult.getCd_bib_ord());
					rec.setCodSerie(recResult.getCd_serie().getCd_serie());
					rec.setCodInvent(recResult.getCd_inven());
					rec.setSeqColl(recResult.getSeq_coll());
					if (recResult.getB() != null){
						rec.setBid(recResult.getB().getBid());
						List titoli = null;
						try {
							titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block

						}
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								rec.setDescr(titolo.getIsbd());
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
					}else{
						rec.setDescr("Titolo non trovato in Polo");
					}
					//					rec.setBid(recResult.getB().getBid());
					//					rec.setDescr(recResult.getB().getIsbd());//ha sostituito la getTitolo()
					if (recResult.getPrecis_inv().equals("$")) {
						rec.setPrecInv("");
					} else {
						rec.setPrecInv(recResult.getPrecis_inv());
					}
					rec.setSeparatore("//");
					rec.setValoreDouble(recResult.getValore().doubleValue());
					rec.setImportoDouble(recResult.getImporto().doubleValue());
					//					listaTit = getTitolo(recResult.getB().getBid(), ticket);
					//					if (listaTit != null) {
					//						recTit = (TitoloVO) (TitoloVO) listaTit.get(0);
					//					}
					//					if (listaTit != null) {
					//						rec.setDescr(recTit.getIsbd());
					//					} else {
					//						rec.setDescr("Titolo non trovato in Polo");
					//					}
					listaInvOrdine.add(rec);
				}

			} else {
				throw new DataException("inventariOrdineNonTrovato");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
			//		} catch (DaoManagerException e) {
			//
			//			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvOrdine;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariFattura(String codPolo, String codBib,
			int annoFattura, int progrFattura, Locale locale,
			String ticket) throws ApplicationException,
			ValidationException, DataException, RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List listaTit = new ArrayList();
		List result = new ArrayList();
		List listaInvFattura = null;
		InventarioListeVO rec = null;
		try {
			InventarioVO recValid = new InventarioVO();
			recValid.setCodPolo(codPolo);
			recValid.setCodBib(codBib);
			recValid.setAnnoFattura(String.valueOf(annoFattura));
			recValid.setProgrFattura(String.valueOf(progrFattura));
			valida.validaListaInventariOrdine(recValid);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventari(codPolo, codBib,
					annoFattura, progrFattura);
			if (result.size() > 0) {
				listaInvFattura = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = (Tbc_inventario)result.get(index);
					rec = new InventarioListeVO();
					rec.setLocale(locale);
					rec.setPrg(index + 1);
					rec.setCodSerie(recResult.getCd_serie().getCd_serie());
					rec.setCodInvent(recResult.getCd_inven());
					if (recResult.getB() != null){
						rec.setBid(recResult.getB().getBid());
						List titoli = null;
						try {
							titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block

						}
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								rec.setDescr(titolo.getIsbd());
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
					}else{
						rec.setDescr("Titolo non trovato in Polo");
					}
					rec.setSeqColl(recResult.getSeq_coll());
					rec.setRigaFattura(String.valueOf(recResult.getRiga_fattura()));
					if (recResult.getPrecis_inv().equals("$")) {
						rec.setPrecInv("" + "//");
					} else {
						rec.setPrecInv(recResult.getPrecis_inv() + "//");
					}
					listaInvFattura.add(rec);
				}

			} else {
				throw new DataException("inventariFatturaNonTrovato");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
			//		} catch (DaoManagerException e) {
			//
			//			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvFattura;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaInventariRigaFattura(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF,
			Locale locale, String ticket)
	throws ApplicationException, ValidationException, DataException, RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List listaTit = new ArrayList();
		List result = new ArrayList();
		List listaInvFattura = null;
		InventarioListeVO rec = null;
		try {
			InventarioVO recValid = new InventarioVO();
			recValid.setCodPolo(codPolo);
			recValid.setCodBib(codBib);
			recValid.setCodTipoOrd(codTipOrd);
			recValid.setAnnoOrd(String.valueOf(annoOrd));
			recValid.setCodOrd(String.valueOf(codOrd));
			recValid.setCodBibF(codBibF);
			recValid.setAnnoFattura(String.valueOf(annoF));
			recValid.setProgrFattura(String.valueOf(prgF));
			recValid.setRigaFattura(String.valueOf(rigaF));
			valida.validaListaInventariRigaFattura(recValid);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventari(codPolo, codBib, codBibO,
					codTipOrd, annoOrd, codOrd, codBibF, annoF, prgF, rigaF);
			if (result.size() > 0) {
				listaInvFattura = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = (Tbc_inventario)result.get(index);
					rec = new InventarioListeVO();
					rec.setLocale(locale);
					rec.setPrg(index + 1);
					rec.setCodBibO(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
					rec.setCodSerie(recResult.getCd_serie().getCd_serie());
					rec.setCodInvent(recResult.getCd_inven());
					if (recResult.getB() != null){
						rec.setBid(recResult.getB().getBid());
						List titoli = null;
						try {
							titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block

						}
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								rec.setDescr(titolo.getIsbd());
							}
						}else{
							rec.setDescr("Titolo non trovato in Polo");
						}
					}else{
						rec.setDescr("Titolo non trovato in Polo");
					}
					rec.setSeqColl(recResult.getSeq_coll());
					if (recResult.getPrecis_inv().equals("$")) {
						rec.setPrecInv("" + "//");
					} else {
						rec.setPrecInv(recResult.getPrecis_inv() + "//");
					}
					rec.setImportoDouble(recResult.getImporto().doubleValue());
					listaInvFattura.add(rec);
				}

			} else {
				throw new DataException("inventariRigaFatturaNonTrovato");
				//				return new ArrayList();
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
			//		} catch (DaoManagerException e) {
			//
			//			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvFattura;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public InventarioListeVO getInventarioRigaFattura(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF,
			Locale locale, String ticket)
	throws ValidationException, RemoteException {
		checkTicket(ticket);
		TitoloVO recTit = null;
		List listaTit = new ArrayList();
		List result = new ArrayList();
		List listaInvFattura = null;
		InventarioListeVO rec = null;
		try {
			InventarioVO recValid = new InventarioVO();
			recValid.setCodPolo(codPolo);
			recValid.setCodBib(codBib);
			recValid.setCodTipoOrd(codTipOrd);
			recValid.setAnnoOrd(String.valueOf(annoOrd));
			recValid.setCodOrd(String.valueOf(codOrd));
			recValid.setCodBibF(codBibF);
			recValid.setAnnoFattura(String.valueOf(annoF));
			recValid.setProgrFattura(String.valueOf(prgF));
			recValid.setRigaFattura(String.valueOf(rigaF));
			valida.validaListaInventariRigaFattura(recValid);
			daoInv = new Tbc_inventarioDao();
			result = daoInv.getListaInventari(codPolo, codBib, codBibO,
					codTipOrd, annoOrd, codOrd, codBibF, annoF, prgF, rigaF);
			if (result.size() > 0 && result.size() == 1) {
				Tbc_inventario recResult = (Tbc_inventario)result.get(0);
				rec = new InventarioListeVO();
				rec.setLocale(locale);
				rec.setCodSerie(recResult.getCd_serie().getCd_serie());
				rec.setCodInvent(recResult.getCd_inven());
				if (recResult.getB() != null){
					rec.setBid(recResult.getB().getBid());
					List titoli = null;
					try {
						titoli = dfCommon.getTitoloHib(recResult.getB(), ticket);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block

					}
					if (titoli != null){
						if (titoli.size() > 0 && titoli.size() == 1){
							TitoloVO titolo = (TitoloVO)titoli.get(0);
							rec.setDescr(titolo.getIsbd());
						}
					}else{
						rec.setDescr("Titolo non trovato in Polo");
					}
				}else{
					rec.setDescr("Titolo non trovato in Polo");
				}
				rec.setSeqColl(recResult.getSeq_coll());
				if (recResult.getPrecis_inv().equals("$")) {
					rec.setPrecInv("");
				} else {
					rec.setPrecInv(recResult.getPrecis_inv());
				}
				rec.setImportoDouble(recResult.getImporto().doubleValue());

			} else {
				return rec = null;
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

		} catch (Exception e) {

		}
		return rec;
	}
	//	private List getTitolo(String bid, String ticket)
	//			throws DataException, ApplicationException, ValidationException {
	//checkTicket(ticket);
	//		TitoloVO recTit = new TitoloVO();
	//		List lista = new ArrayList();
	//		try {
	//			AreaDatiServizioInterrTitoloCusVO risposta = bibliografica.servizioDatiTitoloPerBid(bid, ticket);
	//			recTit.setBid(risposta.getBid());
	//			recTit.setIsbd(risposta.getTitoloResponsabilita());
	//			recTit.setNatura(risposta.getNatura());
	//			if (risposta.getCodErr().equals("0000")) {
	//				// trovato
	//				lista.add(recTit);
	//			} else {
	//				return null;
	//			}
	//		} catch (RemoteException e1) {
	//			e1.printStackTrace();
	//			throw new DataException(e1);
	//		} catch (DAOException e1) {
	//			e1.printStackTrace();
	//			throw new DataException(e1);
	//		}
	//		return lista;
	//	}

	private int contaMovimentiAperti(String codPolo, String codBib,
			String codSerie, int codInvent) throws DataException,
			ApplicationException, ValidationException {
		int movA = 0;
		// l'EJB di servizi dovrà fare la select che segue

		// ("select count(*) " +
		// " from tbl_movimento_servizi " +
		// " where cod_bib= '" +recInv.getCodBib() + "' " +
		// "and cod_serie = '"+recInv.getCodSerie()+ "' " +
		// "and cod_inven = '"+recInv.getCodInvent()+ "' " +
		// "and stato_mov = 'A'");
		return movA;
	}

	private InventarioVO trasformaInv1HibVO(Tbc_inventario recInv, Locale locale)
	throws DataException {
		InventarioVO inv = null;
		try {
			inv = new InventarioVO();
			inv.setLocale(locale);
			inv.setCodPolo(recInv.getCd_serie().getCd_polo().getCd_polo()
					.getCd_polo());
			inv.setCodBib(recInv.getCd_serie().getCd_polo().getCd_biblioteca());
			inv.setCodSerie(recInv.getCd_serie().getCd_serie());
			inv.setCodInvent(recInv.getCd_inven());
			if (recInv.getCd_proven() != null) {
				inv.setCodProven(recInv.getCd_proven().getCd_proven());
				inv.setDescrProven(recInv.getCd_proven().getDescr());
				inv.setCodPoloProven(recInv.getCd_proven().getCd_polo().getCd_polo().getCd_polo());
				inv.setCodBibProven(recInv.getCd_proven().getCd_polo().getCd_biblioteca());
			} else {
				inv.setCodProven("");
				inv.setCodPoloProven("");
				inv.setCodBibProven("");
			}
			if (recInv.getKey_loc() == null) {
				inv.setKeyLoc(0);
			} else {
				inv.setKeyLoc(recInv.getKey_loc().getKey_loc());
			}
			if (recInv.getB() != null){
				inv.setBid(recInv.getB().getBid());
				inv.setNatura(String.valueOf(recInv.getB().getCd_natura()));
			}else{
				inv.setBid("");
				inv.setNatura("");
			}
			inv.setValoreDouble((recInv.getValore()).doubleValue());
			inv.setImportoDouble(recInv.getImporto().doubleValue());
			if (recInv.getNum_vol() != null){
				inv.setNumVol(recInv.getNum_vol().toString());
			}else{
				inv.setNumVol(String.valueOf(0));
			}
			if (recInv.getTot_loc() != null){
				inv.setTotLoc(recInv.getTot_loc().toString());
			}else{
				inv.setTotLoc(String.valueOf(0));
			}
			if (recInv.getTot_inter() != null){
				inv.setTotInter(recInv.getTot_inter().toString());
			}else{
				inv.setTotInter(String.valueOf(0));
			}
			inv.setSeqColl(recInv.getSeq_coll().trim());
			if (recInv.getPrecis_inv() != null){
				if (recInv.getPrecis_inv().equals("$")) {
					inv.setPrecInv(" ");
				} else {
					inv.setPrecInv(recInv.getPrecis_inv());
				}
			}else{
				inv.setPrecInv("");
			}
			if (recInv.getAnno_abb() != null){
				inv.setAnnoAbb(recInv.getAnno_abb().toString());
			}else{
				inv.setAnnoAbb(String.valueOf(0));
			}
			inv.setFlagDisp(String.valueOf(recInv.getFlg_disp()));
			inv.setFlagNU(String.valueOf(recInv.getFlg_nuovo_usato()));
			inv.setStatoConser(recInv.getStato_con());

			if (recInv.getCd_fornitore() != null){
				inv.setCodFornitore(recInv.getCd_fornitore().toString());
			}else{
				inv.setCodFornitore("");
			}
			inv.setCodMatInv(recInv.getCd_mat_inv());
			inv.setCodSit(String.valueOf(recInv.getCd_sit()));
			if (recInv.getCd_no_disp().trim() != null){
				inv.setCodNoDisp(recInv.getCd_no_disp().trim());
			}else{
				inv.setCodNoDisp("");
			}
			if (recInv.getCd_frui().trim().trim() != null){
				inv.setCodFrui(recInv.getCd_frui());
			}else{
				inv.setCodFrui("");
			}
			//
			//
			if (recInv.getCd_riproducibilita() == null){
				inv.setCodRiproducibilita("");
			}else{
				inv.setCodRiproducibilita(recInv.getCd_riproducibilita().toString());
			}
			if (recInv.getSupporto_copia() == null){
				inv.setSupportoCopia("");
			}else{
				inv.setSupportoCopia(recInv.getSupporto_copia());
			}
			//
			if (recInv.getDigitalizzazione() == null){
				inv.setDigitalizzazione("");
			}else{
				inv.setDigitalizzazione(recInv.getDigitalizzazione().toString());
			}
			if (recInv.getDisp_copia_digitale() == null ||
					(recInv.getDisp_copia_digitale() != null && recInv.getDisp_copia_digitale().trim().equals(""))){
				inv.setDispDaRemoto("");
				inv.setDescrDispDaRemoto("");
			}else{
				inv.setDispDaRemoto(recInv.getDisp_copia_digitale().toString());
				inv.setDescrDispDaRemoto(codici.cercaDescrizioneCodice(String.valueOf(recInv.getDisp_copia_digitale().trim()),
						CodiciType.CODICE_DISP_ACCESSO_REMOTO,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}
			if (recInv.getRif_teca_digitale() == null ||
					(recInv.getRif_teca_digitale() != null && recInv.getRif_teca_digitale().trim().equals(""))){
				inv.setRifTecaDigitale("");
				inv.setDescrRifTecaDigitale("");
			}else{
				inv.setRifTecaDigitale(recInv.getRif_teca_digitale());
				inv.setDescrRifTecaDigitale(codici.cercaDescrizioneCodice(String.valueOf(recInv.getRif_teca_digitale().trim()),
						CodiciType.CODICE_TECHE_DIGITALI,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}
			if (recInv.getId_accesso_remoto() == null){
				inv.setIdAccessoRemoto("");
			}else{
				inv.setIdAccessoRemoto(recInv.getId_accesso_remoto());
			}
			//23/04/2009
			//rosacreainv			inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
			if (recInv.getCd_ord() == null || recInv.getCd_ord() == 0 ) {
				inv.setCodBibO("");
				inv.setAnnoOrd("");
				inv.setCodOrd("");
				inv.setRigaOrd("");
				//rosacreainv				inv.setCodTipoOrd("");
				if (recInv.getCd_tip_ord()!=null){
					inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
					if (recInv.getTipo_acquisizione() != null){
						inv.setTipoAcquisizione(recInv.getTipo_acquisizione().toString());
					}else{
						inv.setTipoAcquisizione("");
					}
				}else{
					inv.setCodTipoOrd("");
					inv.setTipoAcquisizione("");
				}
			} else {
				inv.setCodBibO(recInv.getCd_bib_ord());
				inv.setAnnoOrd(recInv.getAnno_ord().toString());
				inv.setCodOrd(recInv.getCd_ord().toString());
				//rosacreainv				inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
				inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
				if (recInv.getTipo_acquisizione() != null){
					inv.setTipoAcquisizione(recInv.getTipo_acquisizione().toString());
				}else{
					inv.setTipoAcquisizione("");
				}
			}
			if (recInv.getCd_bib_fatt() == null) {
				inv.setCodBibF("");
				inv.setAnnoFattura("");
				inv.setProgrFattura("");
				inv.setRigaFattura("");
			}else{
				if (recInv.getCd_bib_fatt().trim().equals("")) {
					inv.setCodBibF(recInv.getCd_bib_fatt());
					inv.setAnnoFattura("");
					inv.setProgrFattura("");
					inv.setRigaFattura("");
				} else {
					inv.setCodBibF(recInv.getCd_bib_fatt());
					inv.setAnnoFattura(recInv.getAnno_fattura().toString());
					inv.setProgrFattura(recInv.getPrg_fattura().toString());
					inv.setRigaFattura(recInv.getRiga_fattura().toString());
				}
			}
			if (recInv.getCd_polo_scar() != null){
				inv.setCodPoloScar(recInv.getCd_polo_scar());
			}else{
				inv.setCodPoloScar(null);
			}
			if (recInv.getCd_bib_scar() != null){
				inv.setCodBibS(recInv.getCd_bib_scar());
			}else{
				inv.setCodBibS("");
			}
			if (recInv.getId_bib_scar() != null){
				inv.setIdBibScar(recInv.getId_bib_scar());
			}else{
				inv.setIdBibScar(0);
			}
			if (recInv.getCd_scarico() != null){
				inv.setCodScarico(recInv.getCd_scarico().toString());
			}else{
				inv.setCodScarico("");
			}
			if (recInv.getNum_scarico() != null){
				inv.setNumScarico(recInv.getNum_scarico().toString());
			}else{
				inv.setNumScarico("");
			}
			if (recInv.getCd_carico() != null){
				inv.setCodCarico(recInv.getCd_carico().toString());
			}else{
				inv.setCodCarico("");
			}
			if (recInv.getNum_carico() != null){
				inv.setNumCarico(recInv.getNum_carico().toString());
			}else{
				inv.setNumCarico("");
			}
			if (recInv.getData_redisp_prev() != null){
				if (recInv.getData_redisp_prev().equals("")) {
					inv.setDataRedisp("");
				} else {
					inv.setDataRedisp(DateUtil.formattaData(recInv.getData_redisp_prev().getTime()));
				}
			}else{
				inv.setDataRedisp("");
			}

			if (recInv.getData_carico() != null){
				if (recInv.getData_carico().equals("9999-12-31")) {
					inv.setDataCarico("00/00/0000");
				} else {
					inv.setDataCarico(DateUtil.formattaData(recInv.getData_carico().getTime()));
				}
			}else{
				inv.setDataCarico("00/00/0000");
			}
			if (recInv.getDelibera_scar() != null){
				inv.setDeliberaScarico(recInv.getDelibera_scar());
			}else{
				inv.setDeliberaScarico("");
			}
			if (recInv.getData_scarico() != null){
				if (recInv.getData_scarico().equals("9999-12-31")) {
					inv.setDataScarico("00/00/0000");
				} else {
					inv.setDataScarico(DateUtil.formattaData(recInv.getData_scarico().getTime()));
				}
			}else{
				inv.setDataScarico("00/00/0000");
			}
			if (recInv.getData_delibera() != null){
				if (recInv.getData_delibera().equals("9999-12-31")) {
					inv.setDataDelibScar("00/00/0000");
				} else {
					inv.setDataDelibScar(DateUtil.formattaData(recInv.getData_delibera().getTime()));
				}
			}else{
				inv.setDataDelibScar("00/00/0000");
			}
			if (recInv.getSez_old() != null){
				inv.setSezOld(recInv.getSez_old().trim());
			}else{
				inv.setSezOld("");
			}
			if (recInv.getLoc_old() != null){
				inv.setLocOld(recInv.getLoc_old());
			}else{
				inv.setLocOld("");
			}
			if (recInv.getSpec_old() != null){
				inv.setSpecOld(recInv.getSpec_old());
			}else{
				inv.setSpecOld("");
			}
			if (recInv.getKey_loc_old() != null){
				inv.setKeyLocOld(recInv.getKey_loc_old());
			}else{
				inv.setKeyLocOld(0);
			}
			inv.setCancDB2i(String.valueOf(recInv.getFl_canc()));
			if (recInv.getCd_supporto() != null){
				inv.setCodSupporto(recInv.getCd_supporto());
			}else{
				inv.setCodSupporto("");
			}
			if (recInv.getData_ingresso() == null) {
				inv.setDataIngresso(null);
			} else {
				inv.setDataIngresso(DateUtil.formattaData(recInv.getData_ingresso().getTime()));
			}
			if (recInv.getTs_ins_prima_coll() == null) {
				inv.setDataInsPrimaColl("");
			} else {
				inv.setDataInsPrimaColl(DateUtil.formattaData(recInv.getTs_ins_prima_coll().getTime()));
			}
			if (recInv.getUte_ins_prima_coll() == null) {
				inv.setUteInsPrimaColl("");
			} else {
				inv.setUteInsPrimaColl(recInv.getUte_ins_prima_coll().toString());
			}
			if (recInv.getTs_ins() != null){
				inv.setDataIns(DateUtil.formattaData(recInv.getTs_ins().getTime()));
			}else{
				inv.setDataIns("");
			}
			if (recInv.getTs_var() != null){
				inv.setDataAgg(DateUtil.formattaData(recInv.getTs_var().getTime()));
			}else{
				inv.setDataAgg("");
			}
			if (recInv.getUte_ins() != null){
				inv.setUteIns(recInv.getUte_ins());
			}else{
				inv.setUteIns("");
			}
			if (recInv.getUte_var() != null){
				inv.setUteAgg(recInv.getUte_var());
			}else{
				inv.setUteAgg("");
			}
		} catch (Exception e) {

			throw new DataException(e);
		}
		return inv;
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public String getChiaviTitoloAutore(String tipo, String bidVid, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		String chiave = null;
		List listaTit = null;
		Tb_titolo titolo = null;
		Tb_autore autore = null;
		try {
			if (tipo != null && bidVid != null){
				if (tipo.equals("T")){
					titolo = daoInv.getTitoloDF(bidVid);
					if (titolo != null) {
						chiave = titolo.getKy_clet1_t() + titolo.getKy_clet2_t();
					}else{
						throw new DataException("disallineamentoTraDbDiPoloEDbDiIndice");
					}
				}else if (tipo.equals("A")){
					autore = daoInv.getAutoreDF(bidVid);
					if (autore != null) {
						chiave = autore.getKy_cautun();
					}else{
						throw new DataException("disallineamentoTraDbDiPoloEDbDiIndice");
					}
				}
			}
			return chiave;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public StampaRegistroVO getRegistroIngresso(StampaRegistroVO input, String ticket, BatchLogWriter logg)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		StampaRegistroVO outputReg = null;
		Tbc_serie_inventariale serie = null;
		Tbf_biblioteca_in_polo biblioteca = null;
		Timestamp ts = DaoManager.now();
		Tb_titolo sup = null;
		int numInvMinMax = -1;
		List listaInvDaNumANum = null;
		int numInvMeno1 = 0;
		String descrBib = null;
		String invDaNonValorizzato = null;
		int sizeInv = 0;
		int sizeLog = 0;
		Logger logger = logg.getLogger();

		try {
			StampaRegistroVO input1 = (input);
			if (input1 instanceof StampaRegistroVO){
				valida.validaRegistroIngresso(input1);
			}

			daoBib = new Tbf_biblioteca_in_poloDao();
			biblioteca = daoBib.select(input.getCodPolo(), input.getCodeBiblioteca());
			if (biblioteca != null){
				descrBib = biblioteca.getDs_biblioteca();
			}else{
				descrBib = "Descrizione biblioteca mancante";
			}

			input.setDataDa("01/01/1901");

			daoSerie = new Tbc_serie_inventarialeDao();
			serie = daoSerie.getSerie(input.getCodPolo(), input.getCodeBiblioteca(),
					input.getSerieInv().toUpperCase());
			if (serie == null) {
				throw new DataException("serieNonEsistente");
			}
			//trattamento campo inventario Da
			invDaNonValorizzato = input.getCodeInvDa();
			if (input.getCodeInvDa() != null && input.getCodeInvDa().equals("")){
				input.setCodeInvDa("0");
				//non ho il codice inventario impostato quindi cerco il numero minimo
				daoInv = new Tbc_inventarioDao();
				numInvMinMax = daoInv.getMinInventario(input.getCodPolo(), input.getCodeBiblioteca(), input.getSerieInv(), input.getDataDa());
				if (numInvMinMax > 0){
					input.setCodeInvDa(String.valueOf(numInvMinMax));
				}else{
					throw new DataException("erroreNumInvMinMax");
				}
			}
			numInvMinMax = -1;
			//trattamento campo inventario A
			input.setDataA(DateUtil.formattaData(ts.getTime()));
			//non ho il codice inventario impostato quindi cerco il numero massimo
			if (input.getCodeInvA() != null && input.getCodeInvA().equals("")){
				daoInv = new Tbc_inventarioDao();
				numInvMinMax = daoInv.getMaxInventario(input.getCodPolo(), input.getCodeBiblioteca(), input.getSerieInv(), input.getDataA());
				if (numInvMinMax > 0){
					input.setCodeInvA(String.valueOf(numInvMinMax));
				}else{
					throw new DataException("erroreNumInvMinMax");
				}
			}
			//
			outputReg = new StampaRegistroVO(input);
			outputReg.setCodPolo(input.getCodPolo());
			outputReg.setCodeBiblioteca(input.getCodeBiblioteca());
			outputReg.setSerieInv(input.getSerieInv());
			if (input.getCodeInvDa() != null && (input.getCodeInvDa().equals("") || input.getCodeInvDa().equals("0"))){
				outputReg.setCodeInvDa(invDaNonValorizzato);
			}else{
				outputReg.setCodeInvDa(input.getCodeInvDa());
			}
			outputReg.setCodeInvA(input.getCodeInvA());
			outputReg.setDataDa(input.getDataDa());
			outputReg.setDataA(input.getDataA());
			outputReg.setDescrBib(descrBib);
			outputReg.setData(DateUtil.formattaData(DaoManager.now()));
			//
			BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch(input);
			SubReportVO subInv = bcs.startReport("INVENTARI");
			SubReportVO subLog = bcs.startReport("LOG");
			outputReg.setRecInventario(subInv);
			outputReg.setRecLog(subLog);
			daoInv = new Tbc_inventarioDao();
			ScrollableResults cursor_Inv =  daoInv.getListaInventariDaARegIng(input.getCodPolo(), input.getCodeBiblioteca(),
					input.getSerieInv(), input.getCodeInvDa(), input.getCodeInvA());
			int invDa = Integer.valueOf(input.getCodeInvDa());
			while (cursor_Inv.next()) {
				Tbc_inventario inventario = (Tbc_inventario) cursor_Inv.get(0);
				if (inventario != null) {
					if (inventario.getCd_inven() != Integer.valueOf(invDa)){
						List listaInv = new ArrayList();
						int differenza  = (inventario.getCd_inven() - invDa) + 1;
						if (differenza != 0 && differenza > 0){
							//scrivo soltanto il log
							//StampaRegistroIngressoLogVO outputLog = new StampaRegistroIngressoLogVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
							StampaRegistroIngressoLogVO outputLog = new StampaRegistroIngressoLogVO(outputReg);
							if (differenza == 2){
								outputLog.setNroInvLog(String.valueOf(invDa) + " - " + String.valueOf(invDa));
								//									outputDettaglio.setTitolo("Numero inventario non presente");
								outputLog.setChkTitolo("N");
							}else if (differenza > 2){
								outputLog.setNroInvLog(invDa + " - " + (inventario.getCd_inven() -1));
								//									outputDettaglio.setTitolo("Numeri inventari non presenti");
								outputLog.setChkTitolo("N");
							}
							outputLog.setChkDataInventario("N");
							outputLog.setChkValore("N");
							outputLog.setChkBid("N");
							outputLog.setChkCodeInventario("N");
							outputLog.setChkPrecisazioni("N");
							outputLog.setChkCodeFornitore("N");
							outputLog.setChkCodeMateriale("N");
							outputLog.setChkImporto("N");//o prezzo
							outputLog.setChkCodBib("N");
							outputLog.setChkCodSerie("N");
							outputLog.setChkProvenienza("N");
							outputLog.setChkTAcq("N");
							bcs.writeVO("LOG", outputLog);
							//							}
						}else{
							//è incrementato soltanto per gli inventari validi
							sizeInv++;

						}
					}
					//StampaRegistroIngressoLogVO outputLog = new StampaRegistroIngressoLogVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					//StampaRegistroIngressoDettaglioVO outputDettaglio = new StampaRegistroIngressoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					StampaRegistroIngressoLogVO outputLog = new StampaRegistroIngressoLogVO(outputReg);
					StampaRegistroIngressoDettaglioVO outputDettaglio = new StampaRegistroIngressoDettaglioVO(outputReg);
					sizeInv++;
					if (inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_PRECISATO_CHR) {
						outputDettaglio.setSitAmm("precisato");
					}else if (inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
						outputDettaglio.setSitAmm("collocato");
					}else{
						outputDettaglio.setSitAmm("dismesso");
					}

					if (inventario.getCd_serie() != null){
						if (inventario.getCd_inven() > 0){
							outputDettaglio.setNroInvReg(String.valueOf(inventario.getCd_inven()));
							outputLog.setNroInvLog(String.valueOf(inventario.getCd_inven()));
							outputLog.setChkCodeInventario("0");
						}else{
							outputDettaglio.setNroInvReg("");
							outputLog.setNroInvLog("");
							//cod Inventario log
							outputLog.setChkCodeInventario("N");
						}
						outputLog.setChkCodSerie("0");
					}else{//serie == null
						if (inventario.getCd_inven() > 0){
							outputDettaglio.setNroInvReg(String.valueOf(inventario.getCd_inven()));
							outputLog.setNroInvLog(String.valueOf(inventario.getCd_inven()));
							outputLog.setChkCodeInventario("0");
						}else{
							//cod Inventario log
							outputDettaglio.setNroInvReg("");
							outputLog.setNroInvLog("");
							outputLog.setChkCodeInventario("N");
						}
						//cod serie log
						outputLog.setChkCodSerie("N");
					}
					//data ins
					//bug 0004012 collaudo - rendere modificabile il formato cella nel report .xls
					if(inventario.getData_ingresso() != null){
						if (!inventario.getData_ingresso().equals("")){
							//							outputDettaglio.setDataIns(DateUtil.formattaData(inventario.getData_ingresso().getTime()));
							outputDettaglio.setDataInsDate((inventario.getData_ingresso()));
						}else{
							//							outputDettaglio.setDataIns("");
							outputDettaglio.setDataInsDate(inventario.getTs_ins());
						}
						outputLog.setChkDataInventario("0");
					}else{
						//data ins log
						//						outputDettaglio.setDataIns("");
						outputDettaglio.setDataInsDate(inventario.getTs_ins());
						outputLog.setChkDataInventario("N");
					}
					//cod bib log
					if(inventario.getCd_serie().getCd_polo().getCd_biblioteca() != null){
						if (inventario.getCd_serie().getCd_polo().getCd_biblioteca().trim().equals("")){
							outputLog.setChkCodBib("N");
						}else{
							outputLog.setChkCodBib("0");
						}
					}else{
						outputLog.setChkCodBib("N");
					}
					//fornitore
					String descrFornitore = null;
					outputLog.setChkProvenienza("0");
					if (inventario.getCd_bib_ord() != null){//se non c'è l'ordine non si deve controllare il fornitore
						if(inventario.getCd_fornitore().equals(0)){
							controllaProvenienza(outputDettaglio, outputLog, inventario);
						}else{
							//servizio fornitore
							ListaSuppFornitoreVO ricercaFornitori = new ListaSuppFornitoreVO();
							ricercaFornitori.setCodFornitore(String.valueOf(inventario.getCd_fornitore()));
							ricercaFornitori.setLocale("0");
							List listaFornitori = null;
							try{
								listaFornitori = acquisizioni.getRicercaListaFornitori(ricercaFornitori);
								if (listaFornitori != null && listaFornitori.size() > 0 && listaFornitori.size() == 1){
									FornitoreVO fornitore = (FornitoreVO) listaFornitori.get(0);
									descrFornitore = fornitore.getNomeFornitore();
									//fornitore log
									outputLog.setChkCodeFornitore("0");
									outputDettaglio.setDescrProven(descrFornitore);
								}else{
									controllaProvenienza(outputDettaglio, outputLog, inventario);
								}
							} catch (ValidationException e) {
								outputLog.setChkCodeFornitore("0");
								if (e.getMessage().equals("assenzaRisultati")){
									outputDettaglio.setDescrProven("ERRORE: fornitore "+inventario.getCd_fornitore() +" presente su inventario ma non in anagrafe");
								}else{
									outputDettaglio.setDescrProven("ERRORE: altro errore su fornitori");
								}
							}
						}
					}else{
						//l'inventario non è legato ad ordine
						controllaProvenienza(outputDettaglio, outputLog, inventario);
					}
					//tipo acquisizione
					if(inventario.getTipo_acquisizione() != (null)){
						if(inventario.getTipo_acquisizione().equals(' ')){
							outputDettaglio.setCodTipoAcq("");
							//tipo acquisizione log
							outputLog.setChkTAcq("N");
						}else{
							outputDettaglio.setCodTipoAcq(inventario.getTipo_acquisizione().toString());
							//tipo acquisizione log
							outputLog.setChkTAcq("0");
						}
					}else{
						outputDettaglio.setCodTipoAcq("");
						//tipo acquisizione log
						outputLog.setChkTAcq("N");
					}
					//tipo materiale
					if(inventario.getCd_mat_inv() != (null)){
						if(!inventario.getCd_mat_inv().trim().equals("")){
							outputDettaglio.setCodTipoMat(inventario.getCd_mat_inv());
							//tipo materiale log
							outputLog.setChkCodeMateriale("0");
						}else{
							outputDettaglio.setCodTipoMat("");
							//tipo materiale log
							outputLog.setChkCodeMateriale("N");
						}
					}else{
						outputDettaglio.setCodTipoMat("");
						//tipo materiale log
						outputLog.setChkCodeMateriale("N");
					}
					//bid
					Tb_titolo tit = inventario.getB();
					if(tit != null){
						if(tit.getBid().equals("")){
							outputDettaglio.setBid("");
							//titolo log
							outputLog.setChkBid("N");
						}else{
							outputDettaglio.setBid(tit.getBid());
							//titolo log
							outputLog.setChkBid("0");
						}
					}else{
						outputDettaglio.setBid("");
						//titolo log
						outputLog.setChkBid("N");
					}
					//titolo
					if (tit != (null)){
						List titoli = dfCommon.getTitoloHib(tit, ticket);
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								if (!titolo.getNatura().equals("W")){
									//no titolo superiore
									outputDettaglio.setTitolo(titolo.getIsbd());
									outputLog.setChkTitolo("0");
								}else{
									TitoloDAO dao = new TitoloDAO();
									sup = dao.getMonografiaSuperiore(tit);
									if (sup != null) {
										IsbdVO tokens = null;
										try {
											tokens = IsbdTokenizer.tokenize(sup.getIsbd(), sup.getIndice_isbd());
											String tit$a = tokens.getT200$a_areaTitolo();
											if (ValidazioneDati.isFilled(tit$a)) {
												StringBuilder tmp = new StringBuilder();
												tmp.append(tit$a).append(". ").append(titolo.getIsbd());
												String isbd = tmp.toString();
												//
												outputLog.setChkTitolo("0");
												outputDettaglio.setTitolo(isbd);
											}else{
												outputDettaglio.setTitolo("Errore nel Titolo superiore");
												//nel report l'intestazione di colonna cambia nome nel log
												outputLog.setChkTitolo("N");
											}
										} catch (Exception e) {
											log.error("bid: " + sup.getBid(), e);
											logger.error(sup.getBid()+" "+"Errore nel Titolo superiore");
											outputDettaglio.setTitolo(sup.getBid()+" "+"Errore nel Titolo superiore");
											//nel report l'intestazione di colonna cambia nome nel log
											outputLog.setChkTitolo("N");
										}
									}else{
										outputDettaglio.setTitolo("Titolo superiore non presente. "+ titolo.getIsbd());
										//nel report l'intestazione di colonna cambia nome nel log
										outputLog.setChkTitolo("N");
									}
								}
							}else{
								outputDettaglio.setTitolo("");
								//titolo log
								outputLog.setChkTitolo("N");
							}
						}else{
							outputDettaglio.setTitolo("");
							//titolo log
							outputLog.setChkTitolo("N");
						}
					}else{
						outputDettaglio.setTitolo("");
						//titolo log
						outputLog.setChkTitolo("N");
					}
					//precisazione log
					if (inventario.getPrecis_inv() != null) {
						if (inventario.getPrecis_inv().equals("$")) {
							outputDettaglio.setPrecisazione("");
							outputLog.setChkPrecisazioni("0");
						} else {
							outputDettaglio.setPrecisazione(inventario.getPrecis_inv());
							outputLog.setChkPrecisazioni("0");
						}
					}else{
						outputLog.setChkPrecisazioni("N");
					}
					if (outputDettaglio.getSitAmm() != null && outputDettaglio.getSitAmm().equals("dismesso")){
						outputDettaglio.setPrecisazione(outputDettaglio.getSitAmm());//situazione amministrativa
					}
					//fattura
					if (inventario.getCd_bib_fatt() != null) {
						if (!inventario.getCd_bib_fatt().trim().equals("")){
							ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();
							ricercaFatture.setCodBibl(inventario.getCd_bib_fatt());
							ricercaFatture.setProgrFattura(String.valueOf(inventario.getPrg_fattura()));
							ricercaFatture.setAnnoFattura(String.valueOf(inventario.getAnno_fattura()));
							ricercaFatture.setProvenienza("registroIngressoBatch");
							ricercaFatture.setTipoFattura("F");
							ricercaFatture.setTicket("");
							List listaFattura = null;
							try{
								listaFattura = daoAcquisizioni.getRicercaListaFatture(ricercaFatture);
								if (listaFattura.size() > 0){
									FatturaVO fattura = (FatturaVO)listaFattura.get(0);
									outputDettaglio.setDataFattura(String.valueOf(fattura.getDataFattura().trim()));
									outputDettaglio.setNumFattura(String.valueOf(fattura.getNumFattura()).trim());
									outputDettaglio.setDescrProven(fattura.getFornitoreFattura().getDescrizione().trim());
								}else{
									outputDettaglio.setNumFattura("");
								}
							} catch (ValidationException e) {
								outputLog.setChkCodeFornitore("N");
								if (e.getMessage().equals("assenzaRisultati")){
									outputDettaglio.setNumFattura("ERRORE: fattura "+inventario.getAnno_fattura() +" "+ inventario.getPrg_fattura() +" presente su inventario ma non in anagrafe");
								}else{
									outputDettaglio.setNumFattura("ERRORE: altro errore su fattura");
								}
							}
						}
					}
					//valore
					if(inventario.getValore().equals(0)){
						outputDettaglio.setValore("");
						//valore log
						outputLog.setChkValore("N");
					}else{
						outputDettaglio.setValoreDouble(inventario.getValore().doubleValue());
						outputDettaglio.setValore(outputDettaglio.getValoreDouble(), outputDettaglio.getNumberFormat(), outputDettaglio.getLocale());
						//valore log
						outputLog.setChkValore("0");
					}
					//importo
					if(inventario.getImporto().equals(0)){
						outputDettaglio.setPrezzo("");
						//valore log
						outputLog.setChkImporto("N");
					}else{
						outputDettaglio.setPrezzoDouble(inventario.getImporto().doubleValue());
						outputDettaglio.setPrezzo(outputDettaglio.getPrezzoDouble(), outputDettaglio.getNumberFormat(), outputDettaglio.getLocale());
						//valore log
						outputLog.setChkImporto("0");
					}
					//collocazione
					Tbc_collocazione key_loc = inventario.getKey_loc();
					if (key_loc != null) {
						outputDettaglio.setCodCollocazione(key_loc.getCd_sez().getCd_sez().trim() + " " + key_loc.getCd_loc().trim() +  " " + key_loc.getSpec_loc().trim() +  " " + inventario.getSeq_coll().trim());
						daoInv.getCurrentSession().evict(key_loc);
					}else{
						outputDettaglio.setCodCollocazione("");
					}
					invDa = (inventario.getCd_inven() + 1);

					//almaviva5_20100701 libera memoria
					daoInv.getCurrentSession().evict(inventario);
					daoInv.getCurrentSession().evict(tit);
					//
					if((outputLog.getChkCodeFornitore() != null && outputLog.getChkCodeFornitore().equals("0"))//chkCodeFornitore--
							&& (outputLog.getChkDataInventario() != null && outputLog.getChkDataInventario().equals("0"))//chkDataInventario
							&& (outputLog.getChkCodBib() != null && outputLog.getChkCodBib().equals("0"))//chkCodBib
							&& (outputLog.getChkCodSerie() != null && outputLog.getChkCodSerie().equals("0"))//chkCodSerie
							&& (outputLog.getChkCodeInventario() != null && outputLog.getChkCodeInventario().equals("0"))//chkCodeInventario
							&& (outputLog.getChkProvenienza() != null && outputLog.getChkProvenienza().equals("0"))//chkProvenienza
							&& (outputLog.getChkTAcq() != null && outputLog.getChkTAcq().equals("0"))//chkTAcq
							&& (outputLog.getChkBid() != null && outputLog.getChkBid().equals("0"))//chkBid
							&& (outputLog.getChkTitolo() != null && outputLog.getChkTitolo().equals("0"))//chkTitolo
							&& (outputLog.getChkCodeMateriale() != null && outputLog.getChkCodeMateriale().equals("0"))//chkCodeMateriale
							&& (outputLog.getChkValore() != null && outputLog.getChkValore().equals("0"))//chkValore
							&& (outputLog.getChkPrecisazioni() != null && outputLog.getChkPrecisazioni().equals("0"))//chkPrecisazioni
							&& (outputLog.getChkImporto() != null && outputLog.getChkImporto().equals("0"))){//chkImporto
					}else{
						sizeLog++;
						bcs.writeVO("LOG", outputLog);
					}
					bcs.writeVO("INVENTARI", outputDettaglio);
				}
			}
			outputReg.setSizeInv(sizeInv);
			outputReg.setSizeLog(sizeLog);
			cursor_Inv.close();
			if (sizeInv > 0){
				bcs.endReport("INVENTARI");
				bcs.endReport("LOG");
				return outputReg;
			}
			//nessun inventario trovato - calcola inventari mancanti per log
			int invA = Integer.valueOf(input.getCodeInvA());
			List listaInv = new ArrayList();
			StampaRegistroIngressoDettaglioVO outputDettaglio1 = new StampaRegistroIngressoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			StampaRegistroIngressoLogVO outputLog1 = new StampaRegistroIngressoLogVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			outputDettaglio1.setNroInvReg(invDa + " - " + invA);
			outputLog1.setNroInvLog(invDa + " - " + invA);
			outputDettaglio1.setTitolo("Non sono presenti inventari per l'intervallo selezionato");
			//
			outputLog1.setChkDataInventario("N");
			outputLog1.setChkValore("N");
			outputLog1.setChkBid("N");
			outputLog1.setChkCodeInventario("N");
			outputLog1.setChkPrecisazioni("N");
			outputLog1.setChkCodeFornitore("N");
			outputLog1.setChkCodeMateriale("N");
			outputLog1.setChkImporto("N");//o prezzo
			outputLog1.setChkCodBib("N");
			outputLog1.setChkCodSerie("N");
			outputLog1.setChkProvenienza("N");
			outputLog1.setChkTAcq("N");
			//			outputDettaglio1.setDataIns("");
			outputDettaglio1.setDataInsDate(new java.util.Date());
			outputDettaglio1.setDescrProven("");
			outputDettaglio1.setCodTipoAcq("");
			outputDettaglio1.setBid("");
			outputDettaglio1.setCodTipoMat("");
			outputDettaglio1.setValore("");
			outputDettaglio1.setPrecisazione("");
			outputDettaglio1.setPrezzo("");//o importo
			outputDettaglio1.setNumFattura("");
			outputDettaglio1.setDataFattura("");
			outputDettaglio1.setCodCollocazione("");
			outputDettaglio1.setSitAmm("assente");
			outputDettaglio1.setData("");
			outputDettaglio1.setDescrBib(descrBib);
			//
			outputReg.setSizeInv(1);
			outputReg.setSizeLog(1);
			bcs.writeVO("INVENTARI", outputDettaglio1);
			bcs.writeVO("LOG", outputLog1);
			bcs.endReport("INVENTARI");
			bcs.endReport("LOG");
			return outputReg;
			//				}
			//			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (StringIndexOutOfBoundsException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		}
	}

	/**
	 * @param output
	 * @param recResult
	 */
	private void controllaProvenienza(StampaRegistroIngressoDettaglioVO outputDettaglio, StampaRegistroIngressoLogVO outputLog,
			Tbc_inventario recResult) {
		if (recResult.getCd_proven() != null &&
				(recResult.getCd_proven().getCd_proven() != null && !recResult.getCd_proven().getCd_proven().equals(""))) {
			outputDettaglio.setDescrProven(recResult.getCd_proven().getDescr());
			//provenienza log
			outputLog.setChkCodeFornitore("0");
		}else{
			outputDettaglio.setDescrProven("");
			//provenienza log
			outputLog.setChkCodeFornitore("N");
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public StampaStatisticheRegistroVO getStatisticheRegistroIngresso(StampaRegistroVO input, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		Locale locale = Locale.getDefault();
		StampaStatisticheRegistroVO rec = null;
		Tbc_serie_inventariale serie = null;
		Tbf_biblioteca_in_polo biblioteca = null;
		Timestamp ts = DaoManager.now();
		int numInvMinMax = -1;
		List listaCodAcqCodMat = null;
		List listaCodAcqCodMatGroupBy = null;
		List listaDettaglio = null;
		List statisticheRegistroIngresso = null;
		int numInvMeno1 = 0;
		String descrBib = null;
		String invDaNonValorizzato = null;

		try {
			StampaRegistroVO input1 = (input);
			if (input1 instanceof StampaRegistroVO){
				valida.validaRegistroIngresso(input1);
			}
			if (input.getDataDa() != null && (input.getDataDa().trim().equals("") || input.getDataDa().equals("00/00/0000"))){
				input.setDataDa("01/01/1901");
			}
			if (input.getDataA() != null && (input.getDataA().trim().equals("") || input.getDataDa().equals("00/00/0000"))){
				input.setDataA(DateUtil.formattaData(ts.getTime()));
			}
			daoBib = new Tbf_biblioteca_in_poloDao();
			biblioteca = daoBib.select(input.getCodPolo(), input.getCodeBiblioteca());
			if (biblioteca != null){
				descrBib = biblioteca.getDs_biblioteca();
			}else{
				descrBib = "Descrizione biblioteca mancante";
			}
			input.setDescrBib(descrBib);

			daoSerie = new Tbc_serie_inventarialeDao();
			serie = daoSerie.getSerie(input.getCodPolo(), input.getCodeBiblioteca(),
					input.getSerieInv().toUpperCase());
			if (serie == null) {
				throw new DataException("serieNonEsistente");
			}
			//trattamento campo inventario Da
			if (input.getCodeInvDa() != null && (input.getCodeInvDa().equals("") || input.getCodeInvDa().equals("0"))){
				input.setCodeInvDa("0");
				//non ho il codice inventario impostato quindi cerco il numero minimo
				daoInv = new Tbc_inventarioDao();
				numInvMinMax = daoInv.getMinInventario(input.getCodPolo(), input.getCodeBiblioteca(), serie.getCd_serie(), input.getDataDa());
				if (numInvMinMax > 0){
					input.setCodeInvDa(String.valueOf(numInvMinMax));
				}else{
					input.setCodeInvDa(String.valueOf(numInvMinMax));
					//						throw new DataException("erroreNumInvMinMax");
				}
			}
			numInvMinMax = -1;
			//trattamento campo inventario A
			//non ho il codice inventario impostato quindi cerco il numero massimo
			if (input.getCodeInvA() != null && (input.getCodeInvA().equals("") || input.getCodeInvA().equals("0"))){
				daoInv = new Tbc_inventarioDao();
				numInvMinMax = daoInv.getMaxInventario(input.getCodPolo(), input.getCodeBiblioteca(), serie.getCd_serie(), input.getDataA());
				if (numInvMinMax > 0){
					input.setCodeInvA(String.valueOf(numInvMinMax));
				}else{
					throw new DataException("erroreNumInvMinMax");
				}
			}
			//			}
			String codTipOrd = null;
			daoInv = new Tbc_inventarioDao();
			if (input.getCodeTipoOrdine() != null){
				codTipOrd = input.getCodeTipoOrdine();
			}
			daoInv = new Tbc_inventarioDao();
			StampaRegistroVO recAcq = null;
			StampaStatisticheRegistroDettaglioMatVO recMat = null;
			statisticheRegistroIngresso = new ArrayList();
			rec = new StampaStatisticheRegistroVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
			rec.setCodPolo(input.getCodPolo());
			rec.setCodBib(input.getCodeBiblioteca());
			rec.setCodeBiblioteca(input.getCodeBiblioteca());
			rec.setSerieInv(input.getSerieInv());
			rec.setCodeInvDa(input.getCodeInvDa());
			rec.setCodeInvA(input.getCodeInvA());
			rec.setDataDa(input.getDataDa());
			rec.setDataA(input.getDataA());
			rec.setDescrBib(descrBib);
			rec.setCodeTipoOrdine(input.getCodeTipoOrdine());
			rec.setData(DateUtil.formattaDataCompletaTimestamp(ts));
			rec.setCodAttivita(input.getCodAttivita());
			rec.setUser(ticket.substring(8, 13));
			if (serie == null){
				listaCodAcqCodMatGroupBy = daoInv.getListaCodAcqCodMatGroupBy(input.getCodPolo(), input.getCodeBiblioteca(), null,
						null, null, input.getDataDa().trim(), input.getDataA().trim(), codTipOrd);
			}else{
				listaCodAcqCodMatGroupBy = daoInv.getListaCodAcqCodMatGroupBy(input.getCodPolo(), input.getCodeBiblioteca(), serie.getCd_serie(),
						input.getCodeInvDa(), input.getCodeInvA(), input.getDataDa().trim(), input.getDataA().trim(), codTipOrd);
			}
			if (listaCodAcqCodMatGroupBy != null && listaCodAcqCodMatGroupBy.size() > 0){
				String tipoAcq = null;
				Set tipiAcq = new HashSet<String>();
				Long totaleInv = null;
				BigDecimal totaleVal = null;
				StampaStatisticheRegistroDettaglioAcqVO acqCorrente = null;
				for (int index1 = 0; index1 < listaCodAcqCodMatGroupBy.size(); index1++) {
					Object[] recResult1 = (Object[]) listaCodAcqCodMatGroupBy.get(index1);
					tipoAcq = String.valueOf(recResult1[0]);
					if (!tipiAcq.contains(tipoAcq)){
						acqCorrente = new StampaStatisticheRegistroDettaglioAcqVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
						totaleInv = new Long(0);
						totaleVal = new BigDecimal(0);
						acqCorrente.setCodiceTipoAcq(String.valueOf(recResult1[0]).trim());
						if (acqCorrente.getCodiceTipoAcq() != null && !acqCorrente.getCodiceTipoAcq().trim().equals("")){
							acqCorrente.setDescrTipoAcq(codici.cercaDescrizioneCodice(String.valueOf(recResult1[0]).trim(),
									CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,
									CodiciRicercaType.RICERCA_CODICE_SBN));
							if (acqCorrente.getDescrTipoAcq() != null && acqCorrente.getDescrTipoAcq().trim().equals("")){
								acqCorrente.setDescrTipoAcq("Manca descrizione per codice acq = "+acqCorrente.getCodiceTipoAcq());
							}
						}else{
							acqCorrente.setDescrTipoAcq("");
						}
						tipiAcq.add(tipoAcq);
						rec.getListaTipoAcq().add(acqCorrente);
					}
					recMat = new StampaStatisticheRegistroDettaglioMatVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
					recMat.setCodiceTipoMat(String.valueOf(recResult1[1]).trim());
					if (recMat.getCodiceTipoMat() != null && !recMat.getCodiceTipoMat().trim().equals("")){
						recMat.setDescrTipoMat(codici.cercaDescrizioneCodice(String.valueOf(recResult1[1]).trim(),
								CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE,
								CodiciRicercaType.RICERCA_CODICE_SBN));
						if (recMat.getDescrTipoMat() != null && recMat.getDescrTipoMat().trim().equals("")){
							recMat.setDescrTipoMat("Manca descrizione per codice mat = "+recMat.getCodiceTipoMat());
						}
					}else{
						recMat.setDescrTipoMat("");
					}

					totaleInv = totaleInv + (Integer) recResult1[2];
					totaleVal = totaleVal.add((BigDecimal)recResult1[3]);
					recMat.setTotInvMat(String.valueOf(recResult1[2]));
					recMat.setTotValMat(((BigDecimal)recResult1[3]).doubleValue(), input.getNumberFormat(), input.getLocale());
					acqCorrente.setTotInvAcq(String.valueOf(totaleInv));
					acqCorrente.setTotValAcq(totaleVal.doubleValue(), input.getNumberFormat(), input.getLocale());
					acqCorrente.getListaMat().add(recMat);
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {

			throw new DataException(e);
		}
		return rec;
	}

	private List getListaInventariDaNumInvANumInv(String codPolo, String codBib, String codSerie, String codTipOrd,
			String daNum, String aNum, String dataDa, String dataA, String tipoOperazione)
	throws ResourceNotFoundException, DataException, ValidationException {
		List listaInv = null;
		List listaOutput = null;
		Tbc_inventario rec = null;
		Tbc_inventario inventario = null;
		try {
			daoInv = new Tbc_inventarioDao();
			listaInv = daoInv.getListaInventariDaA(codPolo, codBib, codSerie, daNum, aNum);
			if (listaInv.size() > 0){
				if (tipoOperazione != null && tipoOperazione.equals("V")){//elaborazione sintetica registro ingresso
					//sintetica
					listaOutput = new ArrayList();
					for (int index = 0; index < listaInv.size(); index++) {
						Tbc_inventario recResult = (Tbc_inventario) listaInv.get(index);
						rec = new Tbc_inventario();
						Timestamp dataDaString = (DateUtil.toTimestamp(dataDa));
						Timestamp dataAString = (DateUtil.toTimestamp(dataA));
						//						if (recResult.getTs_ins().before((dataDaString)) ||
						//									recResult.getTs_ins().after((dataAString))){
						//						}else{
						//
						listaOutput.add(recResult);
						//						}
					}
				}else if (tipoOperazione != null && tipoOperazione.equals("C")){//elaborazione analitica registro ingresso
					//analitica
					listaOutput = new ArrayList();
					for (int index = 0; index < listaInv.size(); index++) {
						Tbc_inventario recResult = (Tbc_inventario) listaInv.get(index);
						rec = new Tbc_inventario();
						if (codTipOrd != null){
							if (!recResult.getCd_tip_ord().equals(codTipOrd)){
								String dataDaString = String.valueOf(DateUtil.toTimestamp(dataDa));
								String dataAString = String.valueOf(DateUtil.toTimestamp(dataA));
								if (recResult.getTs_ins().before(Timestamp.valueOf(dataDaString)) ||
										recResult.getTs_ins().after(Timestamp.valueOf(dataAString))){
								}else{
									listaOutput.add(rec);
								}
							}
						}
						//il codTipOrd nel caso di Analitica non può essere null
						throw new DataException("tipoAcquisizioneNull");
					}
				}else{
					//tipoOperazione != da V e C
					throw new DataException("tipoElaborazioneDiversoDaSinteticaOAnalitica");
				}
			}

		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaOutput;
	}
	private List calcolaInvMancanti(String serie, int codInvDa, int codInvA)
	throws DataException, ApplicationException, ValidationException {
		List lista = new ArrayList();
		return lista;
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
	public List getEtichette(StampaEtichetteVO input, String tipoOperazione, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		checkTicket(ticket);
		List lista = new ArrayList();
		StampaEtichetteVO output = new StampaEtichetteVO(input);
		List listaEtichette = new ArrayList();
		String locNormalizzata = null;
		String aLocNormalizzata = null;
		String specNormalizzata = null;
		String aSpecNormalizzata = null;
		Tbc_sezione_collocazione sezioneDa = null;
		Tbc_sezione_collocazione sezioneA = null;
		Timestamp ts = DaoManager.now();
		try{
			if (tipoOperazione.equals("R")){
				output = this.controllaInput(input, output);
				daoInv = new Tbc_inventarioDao();
				List listaInvDaA = daoInv.getListaInventariDaA(input.getCodPolo(), input.getCodBib(),
						input.getSerie(), input.getStartInventario(), input.getEndInventario());
				if (listaInvDaA.size() > 0){
					for (int index = 0; index < listaInvDaA.size(); index++) {
						Tbc_inventario inventario = (Tbc_inventario) listaInvDaA.get(index);
						if (inventario != null){
							if (inventario.getKey_loc() != null){
								if (inventario.getKey_loc().getKey_loc() > 0){
									if (inventario.getKey_loc() != null && inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
										dfCommon.scriviDatiEtichetta(listaEtichette, inventario, tipoOperazione, input.getDescrBibEtichetta());
										if (listaEtichette != null || listaEtichette.size() > 0){
										}
									}else{
										continue;
									}
								}else{
									continue;
								}
							}else{
								continue;
							}

						}
					}
				}else{
					output.getErrori().add("ERRORE: tipo operazione "+ tipoOperazione +" e lista inventari vuota");
				}
			}else if (tipoOperazione.equals("N")){
				output = this.controllaInput(input, output);
				List listaInventariInput = input.getListaInventari();
				if (input.getSelezione() == null && !input.getSelezione().equals("A")){
					Collections.sort(listaInventariInput);
				}
				if (listaInventariInput != null){
					if (listaInventariInput.size() > 0){
						for (int y=0; y<listaInventariInput.size(); y++) {
							CodiceVO elem = (CodiceVO) listaInventariInput.get(y);
							daoInv = new Tbc_inventarioDao();
							Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), elem.getCodice(), Integer.valueOf(elem.getDescrizione()));
							if (inventario != null){
								if (inventario.getFl_canc() != 'S'){
									if (inventario.getKey_loc() != null){
										if (inventario.getKey_loc().getKey_loc() > 0){
											if (inventario.getKey_loc() != null && inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
												dfCommon.scriviDatiEtichetta(listaEtichette, inventario, tipoOperazione, input.getDescrBibEtichetta());
												if (listaEtichette != null || listaEtichette.size() > 0){
												}
											}else{
												continue;
											}
										}else{
											continue;
										}
									}else{
										continue;
									}
								}
							}else{
								output.getErrori().add("ATTENZIONE: Inventario "+elem.getCodice()+" "+ elem.getDescrizione()+" non esistente o cancellato");
							}
						}
					}else{
						output.getErrori().add("ERRORE: tipo operazione "+ tipoOperazione +" e lista inventari vuota");
					}
				}
			}else if (tipoOperazione.equals("S")){
				output = this.controllaInput(input, output);
				List listaInvInput = new ArrayList();
				daoSez = new Tbc_sezione_collocazioneDao();
				Tbc_sezione_collocazione sezione = daoSez.getSezione(input.getCodPolo(), input.getCodBib(), input.getSezione());
				if (sezione != null){
//					CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
//							false, input.getDallaSpecificazione(), input.getAllaSpecificazione(), false, "");
					CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
							input.getDallaSpecificazione(), input.getAllaSpecificazione(), "");
					daoColl = new Tbc_collocazioneDao();
					List listaCollocazioni = daoColl.getCollocazioniPerEtichette(input.getCodPolo(),input.getCodBib(), sezione.getCd_sez(),
							collSpec.getDaColl(), collSpec.getAColl(), collSpec.getDaSpec(), collSpec.getASpec());
					if (listaCollocazioni.size() > 0){
						for (int y=0; y<listaCollocazioni.size(); y++) {
							Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioni.get(y);
							daoInv = new Tbc_inventarioDao();
							List<Tbc_inventario> listaInv = daoInv.getListaInventari(collocazione, "I");//I = ordina per inv.
							if (listaInv.size() > 0) {
								for (int index = 0; index < listaInv.size(); index++) {
									Tbc_inventario inventario = listaInv.get(index);
									if (inventario != null){
										if (inventario.getKey_loc() != null){
											if (inventario.getKey_loc().getKey_loc() > 0){
												if (inventario.getKey_loc() != null && inventario.getCd_sit() == DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR) {
													dfCommon.scriviDatiEtichetta(listaEtichette, inventario, tipoOperazione, input.getDescrBibEtichetta());
													if (listaEtichette != null || listaEtichette.size() > 0){
													}
												}else{
													continue;
												}
											}else{
												continue;
											}
										}else{
											continue;
										}
									}
								}
							}else{
								output.getErrori().add("ERRORE: tipo operazione "+ tipoOperazione +" e lista inventari vuota");
							}
						}
					}
				}
			}
		}catch (ValidationException e) {

			throw new DataException (e);
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return listaEtichette;
	}

	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	public StampaEtichetteVO controllaInput(StampaEtichetteVO input, StampaEtichetteVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
		try{
			input.validate(input.getErrori());
		}catch (ValidationException e) {

			throw e;
		} catch (Exception e) {

			throw new DataException (e);
		}
		return output;
	}

	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	public AreaParametriStampaSchedeVo controllaInputSchede(AreaParametriStampaSchedeVo input, AreaParametriStampaSchedeVo output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
		try{
			input.validate(input.getErrori());
		}catch (ValidationException e) {

			throw e;
		} catch (Exception e) {

			throw new DataException (e);
		}
		return output;
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List getListaModelli(String codPolo, String codiceBiblioteca, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		List lista = null;
		ModelloEtichetteVO rec = null;
		try {
			//SerieVO mi serve soltanto da appoggio per fare la validazione
			CodiceVO modello = new CodiceVO();
			modello.setCodice(codPolo);
			modello.setDescrizione(codiceBiblioteca);
			valida.validaPoloBib(modello);
			List listaModelli = new ArrayList();
			daoModello = new Tbf_modelli_stampeDao();
			listaModelli = daoModello.getListaModelli(codPolo, codiceBiblioteca);
			if (listaModelli.size() > 0) {
				lista = new ArrayList();
				for (int index = 0; index < listaModelli.size(); index++) {
					Tbf_modelli_stampe recResult = (Tbf_modelli_stampe)listaModelli.get(index);
					rec = new ModelloEtichetteVO();
					rec.setPrg(index + 1);
					rec.setCodModello(recResult.getModello());
					rec.setDescrModello(recResult.getDescr());
					lista.add(rec);
				}

			}else{
				throw new DataException("listaModelliVuota");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return lista;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public ModelloDefaultVO getModelloDefault(String codPolo, String codiceBiblioteca,
			String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		ModelloDefaultVO modelloStampa = null;
		try {
			CodiceVO recModello = new CodiceVO();
			recModello.setCodice(codPolo);
			recModello.setDescrizione(codiceBiblioteca);
			valida.validaPoloBib(recModello);
			daoModelloDefault = new Tbc_default_inven_schedeDao();
			Tbc_default_inven_schede modelloDefault = daoModelloDefault.getModelloDefault(codPolo, codiceBiblioteca);
			if (modelloDefault != null && modelloDefault.getId_modello().getModello() != null) {
				modelloStampa = new ModelloDefaultVO();
				modelloStampa.setCodPolo(modelloDefault.getCd_biblioteca().getCd_polo().getCd_polo());
				modelloStampa.setCodBib(modelloDefault.getCd_biblioteca().getCd_biblioteca());
				modelloStampa.setCodModello(modelloDefault.getId_modello().getModello());
				modelloStampa.setDescrBibModello(modelloDefault.getId_modello().getDescr_bib());
				modelloStampa.setFormatoStampa(modelloDefault.getFormato_stampa());
				modelloStampa.setDescrBibModello(modelloDefault.getId_modello().getDescr_bib());
				//
				modelloStampa.setN_copie_tit(String.valueOf(modelloDefault.getN_copie_tit()));
				modelloStampa.setN_copie_edi(String.valueOf(modelloDefault.getN_copie_edi()));
				modelloStampa.setN_copie_poss(String.valueOf(modelloDefault.getN_copie_poss()));
				modelloStampa.setN_copie_richiamo(String.valueOf(modelloDefault.getN_copie_richiamo()));
				modelloStampa.setSch_autori(String.valueOf(modelloDefault.getSch_autori()));
				modelloStampa.setFl_coll_aut(String.valueOf(modelloDefault.getFl_coll_aut()));
				modelloStampa.setFl_tipo_leg(String.valueOf(modelloDefault.getFl_tipo_leg()));
				modelloStampa.setSch_topog(String.valueOf(modelloDefault.getSch_topog()));
				modelloStampa.setFl_coll_top(String.valueOf(modelloDefault.getFl_coll_top()));
				modelloStampa.setSch_soggetti(String.valueOf(modelloDefault.getSch_soggetti()));
				modelloStampa.setFl_coll_sog(String.valueOf(modelloDefault.getFl_coll_sog()));
				modelloStampa.setSch_classi(String.valueOf(modelloDefault.getSch_classi()));
				modelloStampa.setFl_coll_cla(String.valueOf(modelloDefault.getFl_coll_cla()));
				modelloStampa.setSch_titoli(String.valueOf(modelloDefault.getSch_titoli()));
				modelloStampa.setFl_coll_tit(String.valueOf(modelloDefault.getFl_coll_tit()));
				modelloStampa.setSch_edit(String.valueOf(modelloDefault.getSch_edit()));
				modelloStampa.setFl_coll_edi(String.valueOf(modelloDefault.getFl_coll_edi()));
				modelloStampa.setSch_poss(String.valueOf(modelloDefault.getSch_poss()));
				modelloStampa.setFl_coll_poss(String.valueOf(modelloDefault.getFl_coll_poss()));
				modelloStampa.setFlg_coll_richiamo(String.valueOf(modelloDefault.getFlg_coll_richiamo()));
				modelloStampa.setFl_non_inv(String.valueOf(modelloDefault.getFl_non_inv()));
				modelloStampa.setN_copie_aut(String.valueOf(modelloDefault.getN_copie_aut()));
				modelloStampa.setN_copie_top(String.valueOf(modelloDefault.getN_copie_top()));
				modelloStampa.setN_copie_sog(String.valueOf(modelloDefault.getN_copie_sog()));
				modelloStampa.setN_copie_tit(String.valueOf(modelloDefault.getN_copie_tit()));
				modelloStampa.setN_copie_edi(String.valueOf(modelloDefault.getN_copie_edi()));
				modelloStampa.setN_copie_poss(String.valueOf(modelloDefault.getN_copie_poss()));
				modelloStampa.setN_copie_cla(String.valueOf(modelloDefault.getN_copie_cla()));
				modelloStampa.setN_copie_richiamo(String.valueOf(modelloDefault.getN_copie_richiamo()));
				//
				modelloStampa.setCodScarico(modelloDefault.getCd_unimarc().trim());
				modelloStampa.setN_serie(String.valueOf(modelloDefault.getN_serie()));
				//
			}else{
				throw new DataException("recModelloDefaultInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return modelloStampa;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public ModelloEtichetteVO getModello(String codPolo, String codiceBiblioteca, String codModello,
			String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		ModelloEtichetteVO modelloStampa = null;
		try {
			ModelloEtichetteVO rec = new ModelloEtichetteVO();
			rec.setCodPolo(codPolo);
			rec.setCodBib(codiceBiblioteca);
			rec.setCodModello(codModello);
			valida.validaModelloKey(rec);
			daoModello = new Tbf_modelli_stampeDao();
			Tbf_modelli_stampe recModello = daoModello.getModello(codPolo, codiceBiblioteca, codModello);
			if (recModello != null) {
				modelloStampa = new ModelloEtichetteVO();
				modelloStampa.setCodPolo(recModello.getCd_bib().getCd_polo().getCd_polo());
				modelloStampa.setCodBib(recModello.getCd_bib().getCd_biblioteca());
				modelloStampa.setDescrBib(recModello.getDescr_bib());
				modelloStampa.setCodModello(recModello.getModello());
				modelloStampa.setDescrModello(recModello.getDescr());
				modelloStampa.setDatiForm(recModello.getCampi_valori_del_form());
			}else{
				throw new DataException("recInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return modelloStampa;
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean insertModelloDefault(ModelloDefaultVO modello, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		try {
			valida.validaModelloDefault(modello);
			daoModello = new Tbf_modelli_stampeDao();
			Tbf_modelli_stampe recModello = daoModello.getModello(modello.getCodPolo(), modello.getCodBib(), modello.getCodModello());
			if (recModello == null) {
				throw new ValidationException("recModelliEtichetteNonEsistente");
			} else {
				Timestamp ts = DaoManager.now();
				Tbc_default_inven_schede modelloDefault = null;

				Set recModelloDefault = recModello.getTbc_default_inven_schede();
				if (recModelloDefault.size() == 0){
					daoModelloDefault = new Tbc_default_inven_schedeDao();
					modelloDefault = daoModelloDefault.getModelloDefault(modello.getCodPolo(), modello.getCodBib());
					if (modelloDefault == null){
						//inserimento
						modelloDefault = new Tbc_default_inven_schede();
						modelloDefault.setCd_biblioteca(recModello.getCd_bib());
						modelloDefault.setCd_unimarc(modello.getCodScarico().trim());
						modelloDefault.setTipo("");
						modelloDefault.setId_modello(recModello);
						modelloDefault.setFormato_stampa(modello.getFormatoStampa());
						//
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						modelloDefault.setSch_autori(modello.getSch_autori().charAt(0));
						modelloDefault.setFl_coll_aut(modello.getFl_coll_aut().charAt(0));
						modelloDefault.setFl_tipo_leg(modello.getFl_tipo_leg().charAt(0));
						modelloDefault.setSch_topog(modello.getSch_topog().charAt(0));
						modelloDefault.setFl_coll_top(modello.getFl_coll_top().charAt(0));
						modelloDefault.setSch_soggetti(modello.getSch_soggetti().charAt(0));
						modelloDefault.setFl_coll_sog(modello.getFl_coll_sog().charAt(0));
						modelloDefault.setSch_classi(modello.getSch_classi().charAt(0));
						modelloDefault.setFl_coll_cla(modello.getFl_coll_cla().charAt(0));
						modelloDefault.setSch_titoli(modello.getSch_titoli().charAt(0));
						modelloDefault.setFl_coll_tit(modello.getFl_coll_tit().charAt(0));
						modelloDefault.setSch_edit(modello.getSch_edit().charAt(0));
						modelloDefault.setFl_coll_edi(modello.getFl_coll_edi().charAt(0));
						modelloDefault.setSch_poss(modello.getSch_poss().charAt(0));
						modelloDefault.setFl_coll_poss(modello.getFl_coll_poss().charAt(0));
						modelloDefault.setFlg_coll_richiamo(modello.getFlg_coll_richiamo().charAt(0));
						modelloDefault.setFl_non_inv(modello.getFl_non_inv().charAt(0));
						modelloDefault.setN_serie(Integer.parseInt(modello.getN_serie()));
						modelloDefault.setN_piste(0);
						modelloDefault.setN_copie(0);
						modelloDefault.setN_copie_aut(Integer.parseInt(modello.getN_copie_aut()));
						modelloDefault.setN_copie_top(Integer.parseInt(modello.getN_copie_top()));
						modelloDefault.setN_copie_sog(Integer.parseInt(modello.getN_copie_sog()));
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_cla(Integer.parseInt(modello.getN_copie_cla()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						//
						modelloDefault.setUte_ins(modello.getUteAgg());
						modelloDefault.setUte_var(modello.getUteAgg());
						modelloDefault.setTs_ins(ts);
						modelloDefault.setTs_var(ts);
						modelloDefault.setFl_canc('N');
					}else{
						//aggiornamento
						//
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						modelloDefault.setSch_autori(modello.getSch_autori().charAt(0));
						modelloDefault.setFl_coll_aut(modello.getFl_coll_aut().charAt(0));
						modelloDefault.setFl_tipo_leg(modello.getFl_tipo_leg().charAt(0));
						modelloDefault.setSch_topog(modello.getSch_topog().charAt(0));
						modelloDefault.setFl_coll_top(modello.getFl_coll_top().charAt(0));
						modelloDefault.setSch_soggetti(modello.getSch_soggetti().charAt(0));
						modelloDefault.setFl_coll_sog(modello.getFl_coll_sog().charAt(0));
						modelloDefault.setSch_classi(modello.getSch_classi().charAt(0));
						modelloDefault.setFl_coll_cla(modello.getFl_coll_cla().charAt(0));
						modelloDefault.setSch_titoli(modello.getSch_titoli().charAt(0));
						modelloDefault.setFl_coll_tit(modello.getFl_coll_tit().charAt(0));
						modelloDefault.setSch_edit(modello.getSch_edit().charAt(0));
						modelloDefault.setFl_coll_edi(modello.getFl_coll_edi().charAt(0));
						modelloDefault.setSch_poss(modello.getSch_poss().charAt(0));
						modelloDefault.setFl_coll_poss(modello.getFl_coll_poss().charAt(0));
						modelloDefault.setFlg_coll_richiamo(modello.getFlg_coll_richiamo().charAt(0));
						modelloDefault.setFl_non_inv(modello.getFl_non_inv().charAt(0));
						modelloDefault.setN_serie(Integer.parseInt(modello.getN_serie()));
						modelloDefault.setN_piste(0);
						modelloDefault.setN_copie(0);
						modelloDefault.setN_copie_aut(Integer.parseInt(modello.getN_copie_aut()));
						modelloDefault.setN_copie_top(Integer.parseInt(modello.getN_copie_top()));
						modelloDefault.setN_copie_sog(Integer.parseInt(modello.getN_copie_sog()));
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_cla(Integer.parseInt(modello.getN_copie_cla()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						//
						modelloDefault.setCd_unimarc(modello.getCodScarico().trim());
						modelloDefault.setId_modello(recModello);
						modelloDefault.setFormato_stampa(modello.getFormatoStampa());
						modelloDefault.setUte_var(modello.getUteAgg());
						modelloDefault.setTs_var(ts);
					}
				}else{
					Iterator iter = recModelloDefault.iterator();
					while (iter.hasNext()){
						modelloDefault = (Tbc_default_inven_schede)iter.next();
						//modifica
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						modelloDefault.setSch_autori(modello.getSch_autori().charAt(0));
						modelloDefault.setFl_coll_aut(modello.getFl_coll_aut().charAt(0));
						modelloDefault.setFl_tipo_leg(modello.getFl_tipo_leg().charAt(0));
						modelloDefault.setSch_topog(modello.getSch_topog().charAt(0));
						modelloDefault.setFl_coll_top(modello.getFl_coll_top().charAt(0));
						modelloDefault.setSch_soggetti(modello.getSch_soggetti().charAt(0));
						modelloDefault.setFl_coll_sog(modello.getFl_coll_sog().charAt(0));
						modelloDefault.setSch_classi(modello.getSch_classi().charAt(0));
						modelloDefault.setFl_coll_cla(modello.getFl_coll_cla().charAt(0));
						modelloDefault.setSch_titoli(modello.getSch_titoli().charAt(0));
						modelloDefault.setFl_coll_tit(modello.getFl_coll_tit().charAt(0));
						modelloDefault.setSch_edit(modello.getSch_edit().charAt(0));
						modelloDefault.setFl_coll_edi(modello.getFl_coll_edi().charAt(0));
						modelloDefault.setSch_poss(modello.getSch_poss().charAt(0));
						modelloDefault.setFl_coll_poss(modello.getFl_coll_poss().charAt(0));
						modelloDefault.setFlg_coll_richiamo(modello.getFlg_coll_richiamo().charAt(0));
						modelloDefault.setFl_non_inv(modello.getFl_non_inv().charAt(0));
						modelloDefault.setN_serie(Integer.parseInt(modello.getN_serie()));
						modelloDefault.setN_piste(0);
						modelloDefault.setN_copie(0);
						modelloDefault.setN_copie_aut(Integer.parseInt(modello.getN_copie_aut()));
						modelloDefault.setN_copie_top(Integer.parseInt(modello.getN_copie_top()));
						modelloDefault.setN_copie_sog(Integer.parseInt(modello.getN_copie_sog()));
						modelloDefault.setN_copie_tit(Integer.parseInt(modello.getN_copie_tit()));
						modelloDefault.setN_copie_edi(Integer.parseInt(modello.getN_copie_edi()));
						modelloDefault.setN_copie_poss(Integer.parseInt(modello.getN_copie_poss()));
						modelloDefault.setN_copie_cla(Integer.parseInt(modello.getN_copie_cla()));
						modelloDefault.setN_copie_richiamo(Integer.parseInt(modello.getN_copie_richiamo()));
						//
						modelloDefault.setCd_unimarc(modello.getCodScarico().trim());
						modelloDefault.setId_modello(recModello);
						modelloDefault.setFormato_stampa(modello.getFormatoStampa());
						modelloDefault.setUte_var(modello.getUteAgg());
						modelloDefault.setTs_var(ts);
					}
				}
				recModello.getTbc_default_inven_schede().add(modelloDefault);
				return ret = daoModello.saveUpdateModello(recModello);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean insertModello(ModelloEtichetteVO modello, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		Tbf_modelli_stampe rec =null;
		try {
			valida.validaModello(modello);
			daoModello = new Tbf_modelli_stampeDao();
			Tbf_modelli_stampe recModello = daoModello.getModelloN(modello.getCodPolo(), modello.getCodBib(), modello.getCodModello());
			if (recModello == null) {
				Timestamp ts = DaoManager.now();
				rec = new Tbf_modelli_stampe();
				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(modello.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(modello.getCodBib());
				bib.setCd_polo(polo);
				rec.setCd_bib(bib);
				rec.setDescr_bib(modello.getDescrBib().trim());
				rec.setModello(modello.getCodModello().trim());
				rec.setDescr(modello.getDescrModello().trim());
				rec.setCampi_valori_del_form(modello.getDatiForm());
				rec.setTipo_modello(modello.getTipoModello().charAt(0));
				rec.setTbc_default_inven_schede(null);
				rec.setUte_ins(modello.getUteAgg());
				rec.setUte_var(modello.getUteAgg());
				rec.setTs_ins(ts);
				rec.setTs_var(ts);
				rec.setFl_canc('N');
				return ret = daoModello.saveUpdateModello(rec);
			} else {
				Timestamp ts = DaoManager.now();
				recModello.setDescr_bib(modello.getDescrBib().trim());
				recModello.setModello(modello.getCodModello().trim());
				recModello.setDescr(modello.getDescrModello().trim());
				recModello.setCampi_valori_del_form(modello.getDatiForm());
				recModello.setUte_var(modello.getUteAgg());
				recModello.setFl_canc('N');
				recModello.setTs_var(ts);
				return ret = daoModello.saveUpdateModello(recModello);
				//				throw new ValidationException("recEsistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean updateModello(ModelloEtichetteVO modello, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		Tbf_modelli_stampe rec =null;
		try {
			valida.validaModello(modello);
			daoModello = new Tbf_modelli_stampeDao();
			Tbf_modelli_stampe recModello = daoModello.getModello(modello.getCodPolo(), modello.getCodBib(), modello.getCodModello());
			if (recModello != null) {
				Timestamp ts = DaoManager.now();
				recModello.setDescr_bib(modello.getDescrBib().trim());
				recModello.setDescr(modello.getDescrModello().trim());
				recModello.setCampi_valori_del_form(modello.getDatiForm());
				recModello.setUte_var(modello.getUteAgg());
				recModello.setTs_var(ts);
				return ret = daoModello.saveUpdateModello(recModello);
			} else {
				throw new ValidationException("recNonEsistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
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
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean  deleteModello(ModelloEtichetteVO recModello, String ticket)
	throws DataException, ApplicationException, ValidationException {
		checkTicket(ticket);
		boolean ret = false;
		try{
			ModelloEtichetteVO rec = new ModelloEtichetteVO();
			rec.setCodPolo(recModello.getCodPolo());
			rec.setCodBib(recModello.getCodBib());
			rec.setCodModello(recModello.getCodModello());
			valida.validaModelloKey(rec);
			daoModello =  new Tbf_modelli_stampeDao();
			Tbf_modelli_stampe modello = daoModello.getModello(recModello.getCodPolo(), recModello.getCodBib(), recModello.getCodModello());
			if (modello != null){
				Timestamp ts = DaoManager.now();
				modello.setFl_canc('S');
				modello.setTs_var(ts);
				modello.setUte_var(recModello.getUteAgg());
				daoModello = new Tbf_modelli_stampeDao();
				ret = daoModello.saveUpdateModello(modello);
				if (!ret){
					throw new DataException("erroreCancModello");
				}
			}else{
				throw new DataException("erroreModelloInesistente");
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
	}


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws RemoteException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public boolean aggiornaInventarioDisponibilita(AggDispVO aggDispVO, String ticket)
	throws DataException, ValidationException{
		checkTicket(ticket);
		boolean ret = false;
		Timestamp ts = null;
		String oper = null;
		Tbc_collocazione collocazione = null;
		int keyLoc = 0;
		try {
			valida.validaInputAggiornamentoDisponibilitaCMT(aggDispVO);
			List listaInventariInput = aggDispVO.getListaInventari();
			if (listaInventariInput != null){
				if (listaInventariInput.size() > 0){
					for (int y=0; y<listaInventariInput.size(); y++) {
						StrutturaQuater elem = (StrutturaQuater) listaInventariInput.get(y);
						if (elem.getCodice1() != null && elem.getCodice2() != null && elem.getCodice3() != null){
							daoInv = new Tbc_inventarioDao();
							Tbc_inventario inventario = daoInv.getInventario(aggDispVO.getCodPolo(), aggDispVO.getCodBib(), elem.getCodice1(), Integer.valueOf(elem.getCodice2()));
							if (inventario != null){
								if (inventario.getCd_sit() != DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
									//predisposizione per agg inv
									if (elem.getCodice3() != null && !elem.getCodice3().equals("")){//data_redisp_prev
										inventario.setData_redisp_prev(DateUtil.toTimestamp(elem.getCodice3()));
										inventario.setCd_no_disp("R");
									}else{
										inventario.setData_redisp_prev(null);
										inventario.setCd_no_disp("R");
									}
									if (elem.getCodice4() != null && !elem.getCodice4().equals("")){//data rientro presente
										inventario.setCd_no_disp(" ");
										inventario.setData_redisp_prev(null);
									}
									inventario.setUte_var((DaoManager.getFirmaUtente(ticket)));
									inventario.setTs_var(DaoManager.now());
									daoInv = new Tbc_inventarioDao();
									if(daoInv.modificaInventario(inventario)){
										ret = true;
									}
								}else{
									throw new DataException("inventarioDismesso");
								}
							}
						}
					}
				}
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return ret;
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
	 * @generated //TODO: Must provide implementation for bean create stub
	 */
	public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List parametri) throws RemoteException, ApplicationException,
			DataException, ValidationException {
		checkTicket(ticket);

		StampeOnlineVO stampeOnline = new StampeOnlineVO();
		stampeOnline.setParametriInput(parametri);

		switch (tipoStampa) {
		case STAMPA_REGISTRO_CONSERVAZIONE:
			return eseguiStampaRegistroConservazione(stampeOnline);
		case STAMPA_REGISTRO_TOPOGRAFICO:
			return eseguiStampaRegistroTopografico(stampeOnline);
		}

		return stampeOnline;
	}

	private StampeOnlineVO eseguiStampaRegistroTopografico(
			StampeOnlineVO stampeOnline) {
		List nomiCampi = new ArrayList();
		nomiCampi.add("polo");
		nomiCampi.add("biblioteca");
		nomiCampi.add("sezione");
		nomiCampi.add("collocazione");
		nomiCampi.add("specificazione");
		nomiCampi.add("totale inventari");
		nomiCampi.add("isbd");
		nomiCampi.add("n. inv.");

		stampeOnline.setDescrizioneCampi(nomiCampi);

		List righeDati = new ArrayList();

		List riga1 = new ArrayList();
		riga1.add("CSW");
		riga1.add(" FI");
		riga1.add("111");
		riga1.add("prima collocazione");
		riga1.add("prima spec");
		riga1.add("1");

		riga1.add("Il nome della rosa");
		riga1.add("3");

		righeDati.add(riga1);

		// List subRiga = new ArrayList();
		// subRiga.add(getTitoloInventario("Il nome della rosa", "3"));
		// riga1.add(subRiga);
		// righeDati.add(riga1);

		List riga2 = new ArrayList();
		riga2.add("CSW");
		riga2.add(" FI");
		riga2.add("111");
		riga2.add("seconda collocazione");
		riga2.add("seconda spec");
		riga2.add("2");
		riga2.add("Il nome della violetta");
		riga2.add("2223");

		righeDati.add(riga2);

		List riga3 = new ArrayList();
		riga3.add("CSW");
		riga3.add(" FI");
		riga3.add("111");
		riga3.add("seconda collocazione");
		riga3.add("seconda spec");
		riga3.add("2");
		riga3
		.add("Il nome della dalia ncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndnncdlcvnhdfvnnhdflnvnlacdn vnlclrvjncndn");
		riga3.add("1113");

		righeDati.add(riga3);
		stampeOnline.setRigheDatiDB(righeDati);
		return stampeOnline;
	}

	private StampeOnlineVO eseguiStampaRegistroConservazione(
			StampeOnlineVO stampeOnline) {
		Tbc_inventarioDao daoInv = new Tbc_inventarioDao();
		EsameCollocRicercaVO parametriRicerca = (EsameCollocRicercaVO) stampeOnline
		.getParametriInput().get(0);
		try {
			stampeOnline.setRigheDatiDB(daoInv
					.getListaInventari(parametriRicerca));
		} catch (DaoManagerException e) {
			// TODO Auto-generated catch block

		}
		return stampeOnline;
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
	public StampaRegistroTopograficoVO getRegistroTopografico(StampaRegistroTopograficoVO input, String ticket, BatchLogWriter batchLog)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		checkTicket(ticket);
		StampaRegistroTopograficoVO output = new StampaRegistroTopograficoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
		Tbf_biblioteca_in_polo biblioteca = null;
		String locNormalizzata = null;
		String aLocNormalizzata = null;
		String specNormalizzata = null;
		String aSpecNormalizzata = null;
		String descrBib = null;
		// fare getBiblioteca
		TitoloVO rec = null;
		Timestamp ts = DaoManager.now();
		int invCnt = 0;
		int sizeColl = 0;
		Logger _log = batchLog.getLogger();

		try{
			output = this.controllaInputStRegTop(input, output);

			daoBib = new Tbf_biblioteca_in_poloDao();
			daoSez = new Tbc_sezione_collocazioneDao();
			daoColl = new Tbc_collocazioneDao();
			daoInv = new Tbc_inventarioDao();

			biblioteca = daoBib.select(input.getCodPolo(), input.getCodBib());
			if (biblioteca != null){
				descrBib = biblioteca.getDs_biblioteca();
			}else{
				descrBib = "Descrizione biblioteca mancante";
			}
			if (output != null) {
				output.setCodPolo(input.getCodPolo());
				output.setCodBib(input.getCodBib());
				output.setSezione(input.getSezione());
				output.setDallaCollocazione(input.getDallaCollocazione());
				output.setDallaSpecificazione(input.getDallaSpecificazione());
				output.setAllaCollocazione(input.getAllaCollocazione());
				output.setAllaSpecificazione(input.getAllaSpecificazione());
				output.setDescrBib(descrBib);
				output.setDataDiElaborazione(DateUtil.formattaDataOra(DaoManager.now()));

				Tbc_sezione_collocazione sezione = daoSez.getSezione(input.getCodPolo(), input.getCodBib(), input.getSezione());
				if (sezione != null) {
//					CodiciNormalizzatiVO collSpecA = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
//							false, input.getDallaSpecificazione(), input.getAllaSpecificazione(), false, "");
					CodiciNormalizzatiVO collSpecA = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(Character.toString(sezione.getCd_colloc()), input.getDallaCollocazione(), input.getAllaCollocazione(),
							input.getDallaSpecificazione(), input.getAllaSpecificazione(), "");
					List listaInvInput = new ArrayList();
					//
					BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch(input);
					SubReportVO subColl = bcs.startReport("COLLOCAZIONI");
					output.setRecCollocazione(subColl);
					ScrollableResults cursor_Coll =  daoColl.getCollocazioniPerTopografico(input.getCodPolo(),input.getCodBib(), sezione.getCd_sez(),
							collSpecA.getDaColl(), collSpecA.getAColl(), collSpecA.getDaSpec(), collSpecA.getASpec());
					while (cursor_Coll.next()) {
						sizeColl++;
						Tbc_collocazione collocazione = (Tbc_collocazione) cursor_Coll.get(0);

						if (collocazione != null) {
							StampaRegistroTopograficoCollocazioneVO rtCollocazione = new StampaRegistroTopograficoCollocazioneVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
							rtCollocazione.setOrdLoc(collocazione.getOrd_loc());
							rtCollocazione.setKeyLoc(String.valueOf(collocazione.getKey_loc()));
							rtCollocazione.setCollocazione(collocazione.getCd_loc());
							rtCollocazione.setSpecificazione(collocazione.getSpec_loc());
							rtCollocazione.setTotInventari(String.valueOf(collocazione.getTot_inv()));
							rtCollocazione.setConsistenzaColl(collocazione.getConsis());

							ScrollableResults cursor_Inv = daoInv.getCursorInventari(collocazione, "S");
							int sizeInv = 0;
							StampaRegistroTopograficoInventarioVO rtInventario = null;
							SubReportVO subInv = bcs.startReport("INVENTARI");
							rtCollocazione.setRecInventario(subInv);
							while (cursor_Inv.next() ) {
								List<StampaRegistroTopograficoInventarioVO> inventari = new ArrayList<StampaRegistroTopograficoInventarioVO>();
								Tbc_inventario inventario = (Tbc_inventario) cursor_Inv.get(0);
								if (inventario != null) {
									if ((++invCnt % 500) == 0)
										_log.debug(invCnt + " inventari trattati");

									//almaviva5_20100630
									if ((invCnt % 50) == 0)
										BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
								}
								//
								rtInventario = new StampaRegistroTopograficoInventarioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
								Tb_titolo titolo = inventario.getB();
								List<String> errori = rtInventario.getErrori();
								Tbc_esemplare_titolo esemplare = collocazione.getCd_biblioteca_doc();
								if (titolo != null && titolo.getFl_canc() != 'S' ) {
									if (titolo.getCd_natura() != ('W')) {
										if (titolo.getCd_natura() == ('M')) {
											CodiceVO trTitTit = daoInv.getTitTitDF(titolo.getBid());
											if (trTitTit != null) {
												rtInventario.setBidKyCles2t(trTitTit.getCodice());
												rtInventario.setBidDescrSup(trTitTit.getDescrizione());
											}
											rtInventario.setBidInv(titolo.getBid());
											//tratta titolo per isbd
											List titoli = null;
											try {
												titoli = TitoloHibernate.getTitoloHib(inventario.getB(), ticket);
											} catch (RemoteException e) {

											}
											if (ValidazioneDati.isFilled(titoli) ) {
												TitoloVO tit = (TitoloVO)titoli.get(0);
												rtInventario.setBidDescr(tit.getIsbd());
											}else{
												rtInventario.setBidDescr("Titolo non trovato in Polo");
											}
											//															rtDettaglio.setBidDescr(inventario.getB().getIsbd());
										}else{
											//no titolo superiore
											rtInventario.setBidInv(titolo.getBid());
											//tratta titolo per isbd
											List titoli = null;
											try {
												titoli = TitoloHibernate.getTitoloHib(inventario.getKey_loc().getB(), ticket);
											} catch (RemoteException e) {

											}
											if (ValidazioneDati.isFilled(titoli) ) {
												TitoloVO tit = (TitoloVO)titoli.get(0);
												rtInventario.setBidDescr(tit.getIsbd());
											} else {
												rtInventario.setBidDescr("Titolo non trovato in Polo");
											}
										}

									}else{
										//natura W
										CodiceVO trTitTit = daoInv.getTitTitDF(titolo.getBid());
										if (trTitTit == null){
											errori.add("ERRORE: Natura "+titolo.getCd_natura()+" e Titolo superiore non presente");
										}else{
											rtInventario.setBidKyCles2t(trTitTit.getCodice());
											rtInventario.setBidDescrSup(trTitTit.getDescrizione());
										}
									}
									rtInventario.setBidInv(titolo.getBid());
									if (!ValidazioneDati.isFilled(rtInventario.getBidDescr())) {
										//tratta titolo per isbd
										List titoli = null;
										try {
											//											titoli = TitoloHibernate.getTitoloHib(inventario.getKey_loc().getB(), ticket);
											titoli = TitoloHibernate.getTitoloHib(inventario.getB(), ticket);
										} catch (RemoteException e) {

										}
										if (ValidazioneDati.isFilled(titoli) ) {
											TitoloVO tit = (TitoloVO)titoli.get(0);
											rtInventario.setBidDescr(tit.getIsbd());
										}else{
											rtInventario.setBidDescr("Titolo non trovato in Polo");
										}
									}
									//
									if (titolo.getCd_natura() == 'S' && esemplare != null){
										if (esemplare.getCons_doc().trim().length() > 0 &&
												!esemplare.getCons_doc().trim().equals("$")){
											rtInventario.setConsistenzaDoc(esemplare.getCons_doc().trim());
										}
									}
								}
								else{
									errori.add("ERRORE: Inventario = "+ inventario.getCd_serie().getCd_serie()+ inventario.getCd_inven() + " : titolo " + titolo.getBid() +" non presente in polo");
									continue;
								}
								if (esemplare != null){
									rtInventario.setConsistenzaDoc(esemplare.getCons_doc());
								}else{
									rtInventario.setConsistenzaDoc("");
								}
								rtInventario.setSerie(inventario.getCd_serie().getCd_serie());
								rtInventario.setNumInventario(String.valueOf(inventario.getCd_inven()));
								rtInventario.setSeqColl(inventario.getSeq_coll());
								rtCollocazione.setRecInventario(subInv);
								bcs.writeVO("INVENTARI", rtInventario);
								//almaviva5_20100630 elimino l'oggetto dalla sessione per liberare memoria
								daoInv.getCurrentSession().evict(inventario);
								daoInv.getCurrentSession().evict(titolo);
								daoInv.getCurrentSession().evict(esemplare);
							}
							bcs.endReport("INVENTARI");
							cursor_Inv.close();
							bcs.writeVO("COLLOCAZIONI", rtCollocazione);
							daoInv.getCurrentSession().evict(collocazione);
						}

						bcs.endReport("COLLOCAZIONE");
					}
					cursor_Coll.close();
					daoColl.getCurrentSession().clear();
				}
			}
		}catch (ValidationException e) {
			_log.error("", e);
			throw new DataException (e);
		}catch (DaoManagerException e) {
			_log.error("", e);
			throw new DataException (e);
		} catch (Exception e) {
			_log.error("", e);
			throw new DataException (e);
		}
		output.setCollocazioniCount(sizeColl);
		return output;
	}

	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	public StampaRegistroTopograficoVO controllaInputStRegTop(StampaRegistroTopograficoVO input, StampaRegistroTopograficoVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
		try{
			input.validate(input.getErrori());
		}catch (ValidationException e) {

			throw e;
		} catch (Exception e) {

			throw new DataException (e);
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
	public StampaBollettinoVO getBollettino(StampaBollettinoVO input, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		checkTicket(ticket);
		StampaBollettinoVO output = new StampaBollettinoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
		Tbf_biblioteca_in_polo biblioteca = null;
		String descrBib = null;
		// fare getBiblioteca
		TitoloVO rec = null;
		Timestamp ts = DaoManager.now();
		StampaBollettinoDettaglioVO rcDettaglio = null;
		List listaInv = new ArrayList();
		String ordinaPer = null;
		try{
			output = this.controllaInputBollettino(input, output);
			String dataA = null;
			if (input.getDataA() != null && (input.getDataA().trim().equals("") || input.getDataA().equals("00/00/0000"))){
				dataA = DateUtil.formattaData(ts.getTime());
				input.setDataA(dataA);
			}else{
				dataA = input.getDataA();
			}
			daoBib = new Tbf_biblioteca_in_poloDao();
			biblioteca = daoBib.select(input.getCodPolo(), input.getCodBib());
			if (biblioteca != null){
				descrBib = biblioteca.getDs_biblioteca();
			}else{
				descrBib = "Descrizione biblioteca mancante";
			}
			if (output != null){
				output.setCodPolo(input.getCodPolo());
				output.setCodBib(input.getCodBib());
				output.setDataDa(input.getDataDa());
				output.setDataA(input.getDataA());
				output.setDescrBib(descrBib);
				if (input.getOrdinaPer() != null){
					if (input.getOrdinaPer().startsWith("C")){
						if (input.getOrdinaPer().endsWith("A")){
							output.setOrdinaPer("Data prima collocazione, serie, inventario Asc");
						}else if (input.getOrdinaPer().endsWith("D")){
							output.setOrdinaPer("Data prima collocazione, serie, inventario Desc");
						}
					}else if (input.getOrdinaPer().startsWith("T")){
						if (input.getOrdinaPer().endsWith("A")){
							output.setOrdinaPer("Titolo Asc");
						}else if (input.getOrdinaPer().endsWith("D")){
							output.setOrdinaPer("Titolo Desc");
						}
					}
				}else{
					output.getErrori().add("ERRORE: Ordinamento non valorizzato");
				}
				output.setDataDiElaborazione(DateUtil.formattaDataOra(DaoManager.now()));
				rcDettaglio = new StampaBollettinoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				daoInv = new Tbc_inventarioDao();
				String tipoLista = null;
				if (input.isNuoviEsemplari()){
					listaInv = daoInv.getListaInventariNuoviEsemplari(input.getCodPolo(), input.getCodBib(),
							DateUtil.toTimestamp(input.getDataDa()), DateUtil.toTimestampA(dataA), input.getOrdinaPer());
					tipoLista = "nuoviEsemplari";
				}else{
					listaInv = daoInv.getListaInventariNuoviTitoli(input.getCodPolo(), input.getCodBib(),
							DateUtil.toTimestamp(input.getDataDa()), DateUtil.toTimestampA(dataA), input.getOrdinaPer());
					tipoLista = "nuoviTitoli";
				}
				if (listaInv.size() > 0) {
					if (input.getOrdinaPer() != null && input.getOrdinaPer().equals("A")){

					}else if (input.getOrdinaPer() != null && input.getOrdinaPer().equals("C")){

					}else if (input.getOrdinaPer() != null && input.getOrdinaPer().equals("A")){

					}else{

					}
					trattamentoDatiPerBollettino(input, output, listaInv, tipoLista, ticket);
				}else{
					output.getErrori().add("ERRORE: Non sono presenti dati che soddisfano la richiesta");
				}
			}
		}catch (ValidationException e) {

			throw new DataException (e);
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return output;
	}

	private void trattamentoDatiPerBollettino(StampaBollettinoVO input,
			StampaBollettinoVO output, List listaInv, String tipoLista, String ticket)
	throws RemoteException, DaoManagerException, Exception {
		checkTicket(ticket);
		BigDecimal valore = null;
		StampaBollettinoDettaglioVO bollettinoDettaglio;
		if (tipoLista != null && tipoLista.equals("nuoviEsemplari")){
			for (int index = 0; index < listaInv.size(); index++) {
				BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
				bollettinoDettaglio = new StampaBollettinoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				Tbc_inventario inventario = (Tbc_inventario) listaInv.get(index);
				if (inventario != null){
					if (inventario.getKey_loc() != null){
						if (inventario.getKey_loc().getB() != null){
							bollettinoDettaglio.setBid(inventario.getKey_loc().getB().getBid());
							//tratta titolo per isbd
							List titoli = null;
							try {
								titoli = dfCommon.getTitoloHib(inventario.getKey_loc().getB(), ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO tit = (TitoloVO)titoli.get(0);
									bollettinoDettaglio.setIsbd(tit.getIsbd());
								}
							}else{
								bollettinoDettaglio.setIsbd("Titolo non trovato in Polo");
							}
							//							bollettinoDettaglio.setIsbd(inventario.getKey_loc().getB().getIsbd());
							bollettinoDettaglio.setNatura(""+inventario.getKey_loc().getB().getCd_natura());
						}else{
							bollettinoDettaglio.getErrori().add("ERRORE: Inventario = "+ inventario.getCd_serie().getCd_serie()+ inventario.getCd_inven() + " collocato e titolo di collocazione non presente in polo");
							continue;
						}
						bollettinoDettaglio.setDescrCatFrui(codici.cercaDescrizioneCodice(inventario.getCd_frui(),
								CodiciType.CODICE_CATEGORIA_FRUIZIONE,
								CodiciRicercaType.RICERCA_CODICE_SBN));
						String seqColl = null;
						if (inventario.getSeq_coll() != null && !inventario.getSeq_coll().trim().equals("")){
							seqColl = " / " + inventario.getSeq_coll().trim();
						}else{
							seqColl = inventario.getSeq_coll().trim();
						}

						bollettinoDettaglio.setInventario(inventario.getCd_serie().getCd_serie() + " " + inventario.getCd_inven());
						bollettinoDettaglio.setCollocazione(inventario.getKey_loc().getCd_sez().getCd_sez().trim() + " "
								+ inventario.getKey_loc().getCd_loc().trim() + " "
								+ inventario.getKey_loc().getSpec_loc().trim() + seqColl);
						//soggetti e classi
						CatSemSoggettoVO soggettario = null;
						CatSemClassificazioneVO classificazione = null;
						daoSemantica = new SemanticaDAO();
						CountLegamiSemanticaVO numLegami = daoSemantica.countLegamiSemantica(inventario.getKey_loc().getB().getBid(), "D");
						if (numLegami != null){
							//soggetti
							if (numLegami.getCountLegamiSoggetto() > 0){
								soggettario = trattaSoggettario(ticket,
										bollettinoDettaglio, soggettario,
										numLegami);
							}
							//classi
							if (numLegami.getCountLegamiClasse() > 0){
								classificazione = trattaClasificazione(ticket,
										bollettinoDettaglio, classificazione,
										numLegami);
							}
						}
					}else{
						bollettinoDettaglio.getErrori().add("ERRORE: Inventario = "+ inventario.getCd_serie().getCd_serie()+ inventario.getCd_inven() + " non collocato e data prima collocazione impostata");
						continue;
					}

				}
				output.getLista().add(bollettinoDettaglio);
			}
		}else if (tipoLista != null && tipoLista.equals("nuoviTitoli")){
			for (int index = 0; index < listaInv.size(); index++) {
				BatchManager.getBatchManagerInstance().checkForInterruption(input.getIdBatch());
				bollettinoDettaglio = new StampaBollettinoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				InventarioTitoloBollettinoVO inventarioBoll = (InventarioTitoloBollettinoVO) listaInv.get(index);
				daoInv = new Tbc_inventarioDao();
				Tbc_inventario inventario = daoInv.getInventario(input.getCodPolo(), input.getCodBib(), inventarioBoll.getCd_serie(), inventarioBoll.getCd_inven());
				if (inventario != null){
					if (inventario.getKey_loc() != null){
						if (inventario.getKey_loc().getB() != null){
							bollettinoDettaglio.setBid(inventario.getKey_loc().getB().getBid());
							//tratta titolo per isbd
							List titoli = null;
							try {
								titoli = dfCommon.getTitoloHib(inventario.getKey_loc().getB(), ticket);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO tit = (TitoloVO)titoli.get(0);
									bollettinoDettaglio.setIsbd(tit.getIsbd());
								}
							}else{
								bollettinoDettaglio.setIsbd("Titolo non trovato in Polo");
							}
							//							bollettinoDettaglio.setIsbd(inventario.getKey_loc().getB().getIsbd());
							bollettinoDettaglio.setNatura(""+inventario.getKey_loc().getB().getCd_natura());
						}else{
							bollettinoDettaglio.getErrori().add("ERRORE: Inventario = "+ inventario.getCd_serie().getCd_serie()+ inventario.getCd_inven() + " collocato e titolo di collocazione non presente in polo");
							continue;
						}
						bollettinoDettaglio.setDescrCatFrui(codici.cercaDescrizioneCodice(inventario.getCd_frui(),
								CodiciType.CODICE_CATEGORIA_FRUIZIONE,
								CodiciRicercaType.RICERCA_CODICE_SBN));
						String seqColl = null;
						if (inventario.getSeq_coll() != null && !inventario.getSeq_coll().trim().equals("")){
							seqColl = " / " + inventario.getSeq_coll().trim();
						}else{
							seqColl = inventario.getSeq_coll().trim();
						}

						bollettinoDettaglio.setInventario(inventario.getCd_serie().getCd_serie() + " " + inventario.getCd_inven());
						bollettinoDettaglio.setCollocazione(inventario.getKey_loc().getCd_sez().getCd_sez().trim() + " "
								+ inventario.getKey_loc().getCd_loc().trim() + " "
								+ inventario.getKey_loc().getSpec_loc().trim() + seqColl);
						//soggetti e classi
						CatSemSoggettoVO soggettario = null;
						CatSemClassificazioneVO classificazione = null;
						daoSemantica = new SemanticaDAO();
						CountLegamiSemanticaVO numLegami = daoSemantica.countLegamiSemantica(inventario.getKey_loc().getB().getBid(), "D");
						if (numLegami != null){
							//soggetti
							if (numLegami.getCountLegamiSoggetto() > 0){
								soggettario = trattaSoggettario(ticket,
										bollettinoDettaglio, soggettario,
										numLegami);
							}
							//classi
							if (numLegami.getCountLegamiClasse() > 0){
								classificazione = trattaClasificazione(ticket,
										bollettinoDettaglio, classificazione,
										numLegami);
							}
						}
						//						bollettinoDettaglio.setSoggetto(" ");//manca servizio di semantica
						//						bollettinoDettaglio.setIndiceClassificazione(" ");//manca servizio di semantica
					}else{
						bollettinoDettaglio.getErrori().add("ERRORE: Inventario = "+ inventario.getCd_serie().getCd_serie()+ inventario.getCd_inven() + " non collocato e data prima collocazione impostata");
						continue;
					}

				}
				output.getLista().add(bollettinoDettaglio);
			}
		}
	}

	/**
	 * @param ticket
	 * @param bollettinoDettaglio
	 * @param classificazione
	 * @param numLegami
	 * @return
	 */
	private CatSemClassificazioneVO trattaClasificazione(String ticket,
			StampaBollettinoDettaglioVO bollettinoDettaglio,
			CatSemClassificazioneVO classificazione,
			CountLegamiSemanticaVO numLegami) {
		checkTicket(ticket);
		try{
			classificazione = semantica.ricercaClassiPerBidCollegato(true, numLegami.getBid(), "", "", ticket, 0);
		} catch (DAOException e) {
			// TODO Auto-generated catch block

		} catch (RemoteException e) {
			// TODO Auto-generated catch block

		}

		if (classificazione != null && classificazione.getListaClassi().size() > 0){
			CodiceVO rec = null;
			for (int index2 = 0; index2 < classificazione.getListaClassi().size(); index2++) {
				SinteticaClasseVO classif = classificazione.getListaClassi().get(index2);
				rec = new CodiceVO();
				rec.setCodice("");
				rec.setDescrizione(classif.getIdentificativoClasse());
				bollettinoDettaglio.getIndiciClassificazione().add(rec);
			}
		}else{
			bollettinoDettaglio.setIndiciClassificazione(null);
		}
		return classificazione;
	}

	/**
	 * @param ticket
	 * @param bollettinoDettaglio
	 * @param soggettario
	 * @param numLegami
	 * @return
	 */
	private CatSemSoggettoVO trattaSoggettario(String ticket,
			StampaBollettinoDettaglioVO bollettinoDettaglio,
			CatSemSoggettoVO soggettario, CountLegamiSemanticaVO numLegami) {
		try{
			checkTicket(ticket);
			soggettario = semantica.ricercaSoggettiPerBidCollegato2(true, numLegami.getBid(),
					null, ticket, 0);
		} catch (DAOException e) {
			// TODO Auto-generated catch block

		} catch (RemoteException e) {
			// TODO Auto-generated catch block

		}

		if (soggettario != null && soggettario.getListaSoggetti().size() > 0){
			CodiceVO rec = null;
			for (int index1 = 0; index1 < soggettario.getListaSoggetti().size(); index1++) {
				ElementoSinteticaSoggettoVO soggetto = soggettario.getListaSoggetti().get(index1);
				rec = new CodiceVO();
				rec.setCodice("");
				rec.setDescrizione(soggetto.getTesto());
				bollettinoDettaglio.getSoggetti().add(rec);
			}
		}else{
			bollettinoDettaglio.setSoggetti(null);
		}
		return soggettario;
	}

	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	public StampaRegistroConservazioneVO controllaInputStRegCons(StampaRegistroConservazioneVO input, StampaRegistroConservazioneVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
		try{
			input.validate(input.getErrori());
		} catch (Exception e) {

			throw new DataException (e);
		}
		return output;
	}
	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
	public StampaBollettinoVO controllaInputBollettino(StampaBollettinoVO input, StampaBollettinoVO output)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
		Timestamp ts = DaoManager.now();
		try{
			input.validate(input.getErrori());
			if (input.getDataDa()!=null && input.getDataDa().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(input.getDataDa());
				if (codRitorno != ValidazioneDati.DATA_OK)
					output.getErrori().add("ERRORE: data Da Errata");
			}
			if (input.getDataA()!=null && input.getDataA().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(input.getDataA());
				if (codRitorno != ValidazioneDati.DATA_OK)
					output.getErrori().add("ERRORE: data A Errata");
			}
			if (!input.getDataDa().trim().equals("") && !input.getDataDa().equals("00/00/0000")){
				if (input.getDataDa().compareTo(input.getDataA()) < 0){
					output.getErrori().add("ERRORE: data da " + input.getDataDa() + " < di data a "+ input.getDataA());
				}
			}
			if (!input.getDataA().trim().equals("") && !input.getDataA().equals("00/00/0000")){
				if (input.getDataDa().compareTo(input.getDataA()) == 0){
					output.getErrori().add("ERRORE: data A " + input.getDataDa() + " > di data da "+ input.getDataA());
				}
			}
			if (input.getOrdinaPer() == null ){
				output.getErrori().add("ERRORE: ordinamento non impostato");
			}
		} catch (Exception e) {

			throw new DataException (e);
		}
		return output;
	}
	/**
	 * @param input
	 * @param sezioneDa
	 * @throws DataException
	 */
//	public StampaBuoniCaricoVO controllaInputBuoniCarico(StampaBuoniCaricoVO input, StampaBuoniCaricoVO output)
//	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException,RemoteException	{
//		Timestamp ts = DaoManager.now();
//		try{
//			input.validate(input.getErrori());
//			if (input.getTipoOperazione() != null && input.getTipoOperazione().equals("B")){
//				if (!ValidazioneDati.strIsNull(input.getNumeroBuono())) {
//					if (!ValidazioneDati.strIsNumeric(input.getNumeroBuono())) {
//						output.getErrori().add("ERRORE: numero buono deve essere numerico");
//					}
//					if (input.getNumeroBuono().length() > 9) {
//						output.getErrori().add("ERRORE: numero buono max 9 caratteri numerici");
//					}
//				}else{
//					output.getErrori().add("ERRORE: numero buono obbligatorio");
//				}
//			}
//			if (input.getTipoOperazione() != null && input.getTipoOperazione().equals("F")){
//				if (!ValidazioneDati.strIsNull(input.getAaFattura())) {
//					if (!ValidazioneDati.strIsNumeric(input.getAaFattura())) {
//						output.getErrori().add("ERRORE: anno fattura deve essere numerico");
//					}
//					if (input.getAaFattura().length() > 4) {
//						output.getErrori().add("ERRORE: anno fattura max 4 caratteri numerici");
//					}
//				}else{
//					output.getErrori().add("ERRORE: anno fattura obbligatorio");
//				}
////				if (!ValidazioneDati.strIsNull(input.getNumFattura())) {
////					if (input.getNumFattura().length() > 20) {
////						output.getErrori().add("ERRORE: numero fattura max 20 caratteri numerici");
////					}
////				}else{
////					output.getErrori().add("ERRORE: numero fattura obbligatorio");
////				}
//			}
//		} catch (Exception e) {
//
//			throw new DataException (e);
//		}
//		return output;
//	}
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
	 * //TODO: Must provide implementation for bean create stub
	 */
	public SchedaInventarioVO inserimentoInventarioOrdine(SchedaInventarioInputVO schedaInventarioInput, String ticket)
	throws DataException, ApplicationException, ValidationException {
		SchedaInventarioVO inventarioOrdine = null;
		List<InventarioVO> listaInvAss = null;
		int numInv = 1;
		Timestamp ts = DaoManager.now();
		Locale locale = Locale.getDefault();
		UserTransaction tx = context.getUserTransaction();
		boolean ok = false;
		try {
			DaoManager.begin(tx);
			//almaviva5_20141205 #5672
			checkTicket(ticket);
			String user = DaoManager.getFirmaUtente(ticket);
			valida.validaCodPoloCodBibSerie(schedaInventarioInput.getCodPolo(), schedaInventarioInput.getCodBib(), schedaInventarioInput.getSerie());
			// int numInvAss = 0;
			String tipoOperazione = "C";
			InventarioVO inventario = new InventarioVO();
			inventario.setLocale(locale);
			inventario.setNumberFormat(ServiziConstant.VALORE_IMPORTO_NUMBER_FORMAT_CONVERTER);
			//campi obbligatori per l'inserimento dell'inventario base
			inventario.setCodPolo(schedaInventarioInput.getCodPolo());
			inventario.setCodBib(schedaInventarioInput.getCodBib());
			inventario.setCodSerie(schedaInventarioInput.getSerie());
			//bid
			if (schedaInventarioInput.getBidInv() !=null &&  schedaInventarioInput.getBidInv().length()!=0)	{
				if (schedaInventarioInput.getBidInv().trim().length()>10)	{
					throw new ValidationException("Bid eccedente");
				}
				inventario.setBid(schedaInventarioInput.getBidInv());
			}else{
				throw new ValidationException("Bid = Null");
			}
			//codBibO
			if (schedaInventarioInput.getCodBibO() !=null &&  schedaInventarioInput.getCodBibO().length()!=0)	{
				if (schedaInventarioInput.getCodBibO().trim().length()>3)	{
					throw new ValidationException("Codice biblioteca dell'ordine eccedente");
				}
				inventario.setCodBibO(schedaInventarioInput.getCodBibO());
			}else{
				throw new ValidationException("Codice biblioteca dell'ordine obbligatorio");
			}
			//annoOrd
			if (!ValidazioneDati.strIsNull(schedaInventarioInput.getAnnoOrd())) {
				if (schedaInventarioInput.getAnnoOrd().length()>9) {
					throw new ValidationException("Anno ordine eccedente");
				}
				if (ValidazioneDati.strIsNumeric(schedaInventarioInput.getAnnoOrd())){
					inventario.setAnnoOrd(schedaInventarioInput.getAnnoOrd());
				}else{
					throw new ValidationException("Anno ordine deve essere numerico");
				}
			}else{
				throw new ValidationException("Anno ordine obbligatorio");
			}
			//codOrd
			if (!ValidazioneDati.strIsNull(schedaInventarioInput.getCodOrd())) {
				if (schedaInventarioInput.getCodOrd().length()>9) {
					throw new ValidationException("Codice ordine eccedente");
				}
				if (ValidazioneDati.strIsNumeric(schedaInventarioInput.getCodOrd())){
					inventario.setCodOrd(schedaInventarioInput.getCodOrd());
				}else{
					throw new ValidationException("Codice ordine deve essere numerico");
				}
			}else{
				throw new ValidationException("Codice ordine obbligatorio");
			}
			//idFornitore
			if (!ValidazioneDati.strIsNull((schedaInventarioInput.getIdFornitore()))) {
				if ((schedaInventarioInput.getIdFornitore()).length()>9) {
					throw new ValidationException("Id fornitore eccedente");
				}
				if (ValidazioneDati.strIsNumeric(schedaInventarioInput.getIdFornitore())){
					inventario.setCodFornitore(schedaInventarioInput.getIdFornitore());
				}else{
					throw new ValidationException("Id fornitore deve essere numerico");
				}
			}else{
				throw new ValidationException("Id fornitore obbligatorio");
			}
			//codTipoOrd
			if (schedaInventarioInput.getCodTipoOrd() !=null &&  schedaInventarioInput.getCodTipoOrd().length()!=0)	{
				if (schedaInventarioInput.getCodTipoOrd().trim().length()>1)	{
					throw new ValidationException("Codice tipo ordine eccedente");
				}
				inventario.setCodTipoOrd(schedaInventarioInput.getCodTipoOrd());
				inventario.setTipoAcquisizione(schedaInventarioInput.getCodTipoOrd());
			}else{
				throw new ValidationException("Codice tipo ordine obbligatorio");
			}
			//precisazione
			if (schedaInventarioInput.getPrecis() !=null &&  schedaInventarioInput.getPrecis().length()!=0)	{
				if (schedaInventarioInput.getPrecis().trim().length()>160)	{
					throw new ValidationException("Precisazione inventario eccedente");
				}
				inventario.setPrecInv(schedaInventarioInput.getPrecis());
			}else{
				throw new ValidationException("Precisazione inventario = Null");
			}
			inventario.setCodProven(null);
			inventario.setDataIngresso(DateUtil.formattaData(ts));
			inventario.setDataInsPrimaColl("");
			inventario.setUteIns(user);
			inventario.setUteAgg(user);

			int nInv = 1;
			listaInvAss = assegnaNumInv(inventario, tipoOperazione,	nInv, locale);
			if (listaInvAss != null && listaInvAss.size() == 1){
				InventarioVO inventarioCreato = listaInvAss.get(0);
				daoInv = new Tbc_inventarioDao();
				Tbc_inventario inv = daoInv.getInventario(inventarioCreato.getCodPolo(), inventarioCreato.getCodBib(),
						inventarioCreato.getCodSerie(), inventarioCreato.getCodInvent());
				//validazione ed aggionamento inventario con i valori obbligatori in modifica
				//anno abbonamento
				if (!ValidazioneDati.strIsNull(schedaInventarioInput.getKanno())) {
					if (schedaInventarioInput.getKanno().length()>9) {
						throw new ValidationException("Anno abbonamento eccedente");
					}
					if (ValidazioneDati.strIsNumeric(schedaInventarioInput.getKanno())){
						inv.setAnno_abb(Integer.parseInt(schedaInventarioInput.getKanno()));
					}else{
						throw new ValidationException("Anno abbonamento deve essere numerico");
					}
				}else{
					throw new ValidationException("Anno abbonamento obbligatorio");
				}
				//codice tipo materiale
				if (schedaInventarioInput.getTipoMat()!=null && schedaInventarioInput.getTipoMat().trim().length()!=0){
					if (schedaInventarioInput.getTipoMat().trim().length()>2)	{
						throw new ValidationException("Tipo materiale eccedente");
					}
					if (ValidazioneDati.strIsAlfabetic(schedaInventarioInput.getTipoMat())){
						inv.setCd_mat_inv(schedaInventarioInput.getTipoMat()) ;
					}else{
						throw new ValidationException("Codice tipo materiale deve essere alfabetico");
					}
				}else{
					throw new ValidationException("Codice tipo materiale obbligatorio");
				}
				//codice tipo circ (fruizione)
				if (schedaInventarioInput.getTipoCirc()!=null && schedaInventarioInput.getTipoCirc().trim().length()!=0){
					if (schedaInventarioInput.getTipoCirc().trim().length()>2)	{
						throw new ValidationException("Codice tipo fruizione eccedente");
					}
					if (ValidazioneDati.strIsAlfabetic(schedaInventarioInput.getTipoCirc())){
						inv.setCd_frui(schedaInventarioInput.getTipoCirc());
					}else{
						throw new ValidationException("Codice tipo fruizione deve essere alfabetico");
					}
				}else{
					throw new ValidationException("Codice tipo fruizione obbligatorio");
				}
				//valore
				if (schedaInventarioInput.getValore() != null && !schedaInventarioInput.getValore().equals("")){
					if (schedaInventarioInput.getValore().length()>14) {
						throw new ValidationException("Valore eccedente");
					}
					inv.setValore(BigDecimal.valueOf(NumberFormat.getInstance(locale).parse(schedaInventarioInput.getValore()).doubleValue()));
				}else{
					throw new ValidationException("Valore obbligatorio");
				}

				//prezzoBil (importo)
				if (!ValidazioneDati.strIsNull((schedaInventarioInput.getPrezzoBil()))) {
					if ((schedaInventarioInput.getPrezzoBil()).length()>14) {
						throw new ValidationException("Prezzo bilancio eccedente");
					}
					inv.setImporto(BigDecimal.valueOf(NumberFormat.getInstance(locale).parse(schedaInventarioInput.getPrezzoBil()).doubleValue()));
				}else{
					inv.setImporto(BigDecimal.valueOf(NumberFormat.getInstance(locale).parse("0").doubleValue()));
				}
				//sequenza
				if (!ValidazioneDati.strIsNull((schedaInventarioInput.getSequenza()))) {
					if ((schedaInventarioInput.getSequenza()).length()>20) {
						throw new ValidationException("Sequenza eccedente");
					}
					inv.setSeq_coll(schedaInventarioInput.getSequenza());
				}

				//
				daoInv = new Tbc_inventarioDao();
				if (!daoInv.modificaInventario(inv)) {
					throw new DataException("Errore in fase di modifica inventario1");
				}else{
					if (schedaInventarioInput.getKeyLoc() != null && !schedaInventarioInput.getKeyLoc().trim().equals("")
							&& !schedaInventarioInput.getKeyLoc().equals("0")){
						daoColl = new Tbc_collocazioneDao();
						Tbc_collocazione collocazione = daoColl.getCollocazione(Integer.parseInt(schedaInventarioInput.getKeyLoc()));
						if (collocazione != null){
							//identificata dal key loc e la collocazione va aggiornata nei campi tot_inv, consis_doc
							//se la collocazione ha un esemplare, aggiornare anche la consis di esemplare.
							int totInv = 0;
							collocazione.setTot_inv(collocazione.getTot_inv() + 1);
							collocazione.setConsis(schedaInventarioInput.getConsisDoc());
							if (collocazione.getCd_biblioteca_doc() != null){
								collocazione.getCd_biblioteca_doc().setCons_doc(schedaInventarioInput.getConsisDoc());
								collocazione.getCd_biblioteca_doc().setTs_var(ts);
								collocazione.getCd_biblioteca_doc().setUte_var(user);
							}else{
								throw new DataException("Collocazione priva di esemplare titolo");
							}
							collocazione.setTs_var(ts);
							collocazione.setUte_var(user);
							//
							inv.setCd_sit(DocFisico.Inventari.INVENTARIO_COLLOCATO_CHR);
							inv.setKey_loc(collocazione);
							inv.setTs_var(ts);
							inv.setUte_var(user);
							daoInv = new Tbc_inventarioDao();
							if (!daoInv.modificaInventario(inv)) {
								throw new DataException("Errore in fase di modifica inventario2");
							}else{
								inventarioOrdine = new SchedaInventarioVO();
								inventarioOrdine.setKbibl(inv.getCd_serie().getCd_polo().getCd_biblioteca());

								// modifiche per PICOS 08.11.2010 rox
								// inventarioOrdine.setNinvent(inv.getCd_serie().getCd_serie().trim()+(String.valueOf(inv.getCd_inven())));
								inventarioOrdine.setKserie(inv.getCd_serie().getCd_serie().trim());
								inventarioOrdine.setNinvent(String.valueOf(inv.getCd_inven()));
								//
								inventarioOrdine.setSezione(inv.getKey_loc().getCd_sez().getCd_polo().getCd_biblioteca().trim() + inv.getKey_loc().getCd_sez().getCd_sez().trim());
								inventarioOrdine.setColloc(inv.getKey_loc().getCd_loc().trim());
								inventarioOrdine.setSpecific(inv.getKey_loc().getSpec_loc().trim());
								inventarioOrdine.setSequenza(inv.getSeq_coll().trim());
								inventarioOrdine.setData(DateUtil.formattaDataPicos(inv.getTs_ins()).trim());
								if (inv.getCd_proven() != null){
									inventarioOrdine.setTipoprov(inv.getCd_proven().getCd_proven().trim());
								}else{
									inventarioOrdine.setTipoprov("");
								}
								inventarioOrdine.setTipomat(inv.getCd_mat_inv().trim());
								inventarioOrdine.setTipocirc(inv.getCd_frui().trim());
								inventarioOrdine.setPrecis(inv.getPrecis_inv().trim());
								inventarioOrdine.setValore(NumberFormat.getInstance(locale).format(inv.getValore()));
							}
						}else{
							throw new DataException("Collocazione inesistente");
						}
					}else{
						inventarioOrdine = new SchedaInventarioVO();
						inventarioOrdine.setKbibl(inv.getCd_serie().getCd_polo().getCd_biblioteca());

						// modifiche per PICOS 08.11.2010 rox
						//inventarioOrdine.setNinvent(inv.getCd_serie().getCd_serie().trim()+(String.valueOf(inv.getCd_inven())));
						inventarioOrdine.setKserie(inv.getCd_serie().getCd_serie().trim());
						inventarioOrdine.setNinvent(String.valueOf(inv.getCd_inven()));
						//

						inventarioOrdine.setSezione("");
						inventarioOrdine.setColloc("");
						inventarioOrdine.setSpecific("");
						inventarioOrdine.setSequenza("");
						inventarioOrdine.setData(DateUtil.formattaDataPicos(inv.getTs_ins()).trim());
						if (inv.getCd_proven() != null){
							inventarioOrdine.setTipoprov(inv.getCd_proven().getCd_proven().trim());
						}else{
							inventarioOrdine.setTipoprov("");
						}
						inventarioOrdine.setTipomat(inv.getCd_mat_inv().trim());
						inventarioOrdine.setTipocirc(inv.getCd_frui().trim());
						inventarioOrdine.setPrecis(inv.getPrecis_inv().trim());
						inventarioOrdine.setValore(NumberFormat.getInstance(locale).format(inv.getValore()));
					}
				}
			}
			ok = true;

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {
			throw new DataException(e);
		} finally {
			DaoManager.endTransaction(tx, ok);
		}
		return inventarioOrdine;
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
//	public StampaBuoniCaricoVO getBuoniCarico(StampaBuoniCaricoVO input, String ticket)
//	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
	//checkTicket(ticket);
//		StampaBuoniCaricoVO output = new StampaBuoniCaricoVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
//		Tbf_biblioteca_in_polo biblioteca = null;
//		String descrBib = null;
//		// fare getBiblioteca
//		TitoloVO rec = null;
//		Timestamp ts = DaoManager.now();
//		StampaBuoniCaricoDettaglioVO bcDettaglio = null;
//		List listaInv = new ArrayList();
//		try{
//			output = this.controllaInputBuoniCarico(input, output);
//			daoBib = new Tbf_biblioteca_in_poloDao();
//			biblioteca = daoBib.select(input.getCodPolo(), input.getCodBib());
//			if (biblioteca != null){
//				descrBib = biblioteca.getDs_biblioteca();
//			}else{
//				descrBib = "Descrizione biblioteca mancante";
//			}
//			if (output != null){
//				output.setCodPolo(input.getCodPolo());
//				output.setCodBib(input.getCodBib());
//				output.setDescrBib(descrBib);
//				output.setDataDiElaborazione(DateUtil.formattaDataOra(DaoManager.now()));
//				if (input.getTipoOperazione().equals("F")){
//					//con il numero fattura si reperisce il progressivo fattura
//					int prgFattura = 0;
//					if (input.getAnnoFattura().trim().length() > 0){
//						ListaSuppFatturaVO ricercaFatture=new ListaSuppFatturaVO();
//						ricercaFatture.setCodBibl(input.getCodBib());
////						ricercaFatture.setNumFattura(input.getNumFattura());
//						ricercaFatture.setAnnoFattura(input.getAnnoFattura());
//						ricercaFatture.setTipoFattura("F");
//						ricercaFatture.setTicket(ticket);
//						List listaFattura = acquisizioni.getRicercaListaFatture(ricercaFatture);
//						if (listaFattura.size() > 0){
//							FatturaVO fattura = (FatturaVO)listaFattura.get(0);
//							prgFattura = Integer.parseInt(fattura.getProgrFattura());
//						}else{
//							output.getErrori().add("ERRORE: fatturaNonTrovata");
//						}
//						daoInv = new Tbc_inventarioDao();
//						listaInv = daoInv.getListaInventariBuoniCaricoAnnoFattura(input.getCodPolo(), input.getCodBib(),
//								input.getAnnoFattura(), prgFattura);
//						if (listaInv.size() > 0){
//							BigDecimal totaleValore = new BigDecimal(0);
//							for (int index = 0; index < listaInv.size(); index++) {
//								Tbc_inventario recResult = (Tbc_inventario) listaInv.get(index);
//								Tbc_serie_inventariale serie = recResult.getCd_serie();
//								bcDettaglio = new StampaBuoniCaricoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
//								int buonoCarico = 0;
//								if (input.getNumeroBuono().trim().length() > 0 && Integer.parseInt(input.getNumeroBuono().trim()) > 0){
//									buonoCarico = Integer.parseInt(input.getNumeroBuono().trim());
//								}else{
//									if (serie.getBuono_carico() == Integer.parseInt(input.getNumeroBuono())){
//									}else{
//										buonoCarico = serie.getBuono_carico() + 1;
//										daoSerie = new Tbc_serie_inventarialeDao();
//										serie.setBuono_carico(buonoCarico);
//										serie.setTs_var(ts);
//										serie.setUte_var((DaoManager.getFirmaUtente(ticket)));
//									}
//								}
//								//con il numero fattura si reperisce il progressivo fattura
//								String descrFornitore = null;
//								String dataFattura = null;
//								if (input.getAnnoFattura().trim().length() > 0){
//									ricercaFatture=new ListaSuppFatturaVO();
//									ricercaFatture.setCodBibl(input.getCodBib());
////									ricercaFatture.setNumFattura(input.getNumFattura());
//									ricercaFatture.setAnnoFattura(input.getAnnoFattura());
//									ricercaFatture.setTipoFattura("F");
//									ricercaFatture.setTicket(ticket);
//									listaFattura = acquisizioni.getRicercaListaFatture(ricercaFatture);
//									if (listaFattura.size() > 0){
//										FatturaVO fattura = (FatturaVO)listaFattura.get(0);
//										descrFornitore = fattura.getAnagFornitore().getNomeFornitore();
//										dataFattura = fattura.getDataFattura();
//										//
//										bcDettaglio.setFornitore(descrFornitore);
//										bcDettaglio.setAnnoFattura(fattura.getAnnoFattura());
//										bcDettaglio.setProgrFattura(fattura.getProgrFattura());
//										bcDettaglio.setDataFattura(dataFattura);
//
//									}else{
//										output.getErrori().add("ERRORE: fatturaNonTrovata");
//									}
//								}else{
//									bcDettaglio.setFornitore("");
//									bcDettaglio.setAnnoFattura("");
//									bcDettaglio.setProgrFattura("");
//									bcDettaglio.setDataFattura("");
//								}
//								Tbc_inventario inventario = new Tbc_inventario();
//								inventario = recResult;
//								inventario.setCd_serie(serie);
//								inventario.setData_carico(ts);
//								inventario.setNum_carico(buonoCarico);
//								inventario.setCd_carico('A');
//								inventario.setTs_var(ts);
//								inventario.setUte_var((DaoManager.getFirmaUtente(ticket)));
//								daoInv = new Tbc_inventarioDao();
//								if (!daoInv.modificaInventario(inventario)) {
//									output.getErrori().add("ERRORE: errore di fase di aggiornamento dell'inventario");
//								}else{
//									bcDettaglio.setBuonoCarico(String.valueOf(buonoCarico));
//
//									bcDettaglio.setCodSerie(recResult.getCd_serie().getCd_serie());
//									bcDettaglio.setCodInv(String.valueOf(recResult.getCd_inven()));
//									bcDettaglio.setBid(recResult.getB().getBid());
//									bcDettaglio.setIsbd(recResult.getB().getIsbd());
//									bcDettaglio.setCodTipOrdDescr(codici.cercaDescrizioneCodice(recResult.getTipo_acquisizione().toString(),
//											CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,
//											CodiciRicercaType.RICERCA_CODICE_SBN));
//									bcDettaglio.setValoreDouble((recResult.getValore()).doubleValue());
//
//									totaleValore = totaleValore.add((BigDecimal)recResult.getValore());
//									bcDettaglio.setTotValore(((BigDecimal)recResult.getValore()).doubleValue(), input.getNumberFormat(), input.getLocale());
//									bcDettaglio.setPrecInv(recResult.getPrecis_inv());
//								}
//								output.getLista().add(bcDettaglio);
//							}
//						}else{
//							output.getErrori().add("ERRORE: i criteri indicati per la produzione dela stampa non hanno individuato alcun inventario");
//						}
//					}else{
//						output.getErrori().add("ERRORE: Anno fattura obbligatorio in presenza di Progr. fattura");
//					}
//				}else if (input.getTipoOperazione().equals("R")){
//					//input.getNumeroBuono() diverso da 0
//					daoInv = new Tbc_inventarioDao();
//					listaInv = daoInv.getListaInventariBuoniCaricoCodSerieInvDaInvA(input.getCodPolo(), input.getCodBib(),
//							input.getSerie(), input.getStartInventario(), input.getEndInventario());
//					if (listaInv.size() > 0){
//						ListaSuppFatturaVO ricercaFatture;
//						List listaFattura;
//						BigDecimal totaleValore = new BigDecimal(0);
//						for (int index = 0; index < listaInv.size(); index++) {
//							Tbc_inventario recResult = (Tbc_inventario) listaInv.get(index);
//							Tbc_serie_inventariale serie = recResult.getCd_serie();
//							int buonoCarico = 0;
//							if (input.getNumeroBuono() != null && Integer.parseInt(input.getNumeroBuono().trim()) > 0){
//								buonoCarico = Integer.parseInt(input.getNumeroBuono().trim());
//							}else{
//								if (input.getNumeroBuono() == null){
//									input.setNumeroBuono("0");
//								}
//								if (serie.getBuono_carico() == Integer.parseInt(input.getNumeroBuono())){
//									//								}else{
//									buonoCarico = (serie.getBuono_carico() + 1);
//									daoSerie = new Tbc_serie_inventarialeDao();
//									serie.setBuono_carico(buonoCarico);
//									serie.setTs_var(ts);
//									serie.setUte_var((DaoManager.getFirmaUtente(ticket)));
//								}
//							}
//							bcDettaglio = new StampaBuoniCaricoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
//							String descrFornitore = null;
//							String dataFattura = null;
//							String numFattura = null;
//							if (input.getAnnoFattura() != null && input.getNumFattura() != null){
//								//con il numero fattura si reperisce il progressivo fattura
//								if (input.getAnnoFattura().trim().length() > 0 && input.getNumFattura().trim().length() > 0){
//									ricercaFatture=new ListaSuppFatturaVO();
//									ricercaFatture.setCodBibl(input.getCodBib());
//									ricercaFatture.setNumFattura(input.getNumFattura());
//									ricercaFatture.setAnnoFattura(input.getAnnoFattura());
//									ricercaFatture.setTipoFattura("F");
//									ricercaFatture.setTicket(ticket);
//									listaFattura = acquisizioni.getRicercaListaFatture(ricercaFatture);
//									if (listaFattura.size() > 0){
//										FatturaVO fattura = (FatturaVO)listaFattura.get(0);
//										descrFornitore = fattura.getAnagFornitore().getNomeFornitore();
//										dataFattura = fattura.getDataFattura();
//										numFattura = fattura.getNumFattura();
//									}else{
//										output.getErrori().add("ERRORE: fatturaNonTrovata");
//									}
//								}
//							}else{
//								descrFornitore = "";
//								dataFattura = "";
//								numFattura = "";
//							}
//							bcDettaglio.setNumeroBuono(String.valueOf(buonoCarico));
//							bcDettaglio.setCodPolo(recResult.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
//							bcDettaglio.setCodBib(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
//							bcDettaglio.setCodInv(String.valueOf(recResult.getCd_inven()));
//							bcDettaglio.setBid(recResult.getB().getBid());
//							bcDettaglio.setIsbd(recResult.getB().getIsbd());
//							//solo per quando testo sulla 34
//							if (recResult.getTipo_acquisizione() == null){
//								recResult.setTipo_acquisizione(recResult.getCd_tip_ord());
//							}
//
//							bcDettaglio.setCodTipOrdDescr(codici.cercaDescrizioneCodice(recResult.getTipo_acquisizione().toString(),
//									CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,
//									CodiciRicercaType.RICERCA_CODICE_SBN));
//							bcDettaglio.setValoreDouble((recResult.getValore()).doubleValue());
//
//							totaleValore = totaleValore.add((BigDecimal)recResult.getValore());
//							bcDettaglio.setTotValore(((BigDecimal)recResult.getValore()).doubleValue(), input.getNumberFormat(), input.getLocale());
//							bcDettaglio.setPrecInv(recResult.getPrecis_inv());
//							//
//							recResult.setCd_serie(serie);
//							recResult.setData_carico(ts);
//							recResult.setNum_carico(buonoCarico);
//							recResult.setData_carico(serie.getTs_var());
//							recResult.setCd_carico('A');
//							recResult.setTs_var(ts);
//							recResult.setUte_var((DaoManager.getFirmaUtente(ticket)));
//							daoInv = new Tbc_inventarioDao();
//							if (!daoInv.modificaInventario(recResult)) {
//								output.getErrori().add("ERRORE: errore di fase di aggiornamento dell'inventario");
//								return output;
//							}
//							output.getLista().add(bcDettaglio);
//						}
//					}
//				}else if (input.getTipoOperazione().equals("B")){
//					//input.getNumeroBuono() == 0
//					daoInv = new Tbc_inventarioDao();
//					listaInv = daoInv.getListaInventariBuoniCaricoCodSerieNumCarico(input.getCodPolo(), input.getCodBib(),
//							input.getSerie(), input.getNumeroBuono());
//					if (listaInv.size() > 0){
//						ListaSuppFatturaVO ricercaFatture;
//						List listaFattura;
//						BigDecimal totaleValore = new BigDecimal(0);
//						for (int index = 0; index < listaInv.size(); index++) {
//							Tbc_inventario recResult = (Tbc_inventario) listaInv.get(index);
//							Tbc_serie_inventariale serie = recResult.getCd_serie();
//							bcDettaglio = new StampaBuoniCaricoDettaglioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
//							int buonoCarico = 0;
//							if (input.getNumeroBuono().trim().length() > 0 && Integer.parseInt(input.getNumeroBuono().trim()) > 0){
//								buonoCarico = Integer.parseInt(input.getNumeroBuono().trim());
//							}else{
//								if (serie.getBuono_carico() == Integer.parseInt(input.getNumeroBuono())){
//								}else{
//									buonoCarico = serie.getBuono_carico() + 1;
//									daoSerie = new Tbc_serie_inventarialeDao();
//									serie.setBuono_carico(buonoCarico);
//									serie.setTs_var(ts);
//									serie.setUte_var((DaoManager.getFirmaUtente(ticket)));
//								}
//							}
//							//con il numero fattura si reperisce il progressivo fattura
//							String descrFornitore = null;
//							String dataFattura = null;
//							if (input.getAnnoFattura().trim().length() > 0){
//								ricercaFatture=new ListaSuppFatturaVO();
//								ricercaFatture.setCodBibl(input.getCodBib());
////								ricercaFatture.setNumFattura(input.getNumFattura());
//								ricercaFatture.setAnnoFattura(input.getAnnoFattura());
//								ricercaFatture.setTipoFattura("F");
//								ricercaFatture.setTicket(ticket);
//								listaFattura = acquisizioni.getRicercaListaFatture(ricercaFatture);
//								if (listaFattura.size() > 0){
//									FatturaVO fattura = (FatturaVO)listaFattura.get(0);
//									descrFornitore = fattura.getAnagFornitore().getNomeFornitore();
//									dataFattura = fattura.getDataFattura();
//								}else{
//									output.getErrori().add("ERRORE: fatturaNonTrovata");
//								}
//							}
//							Tbc_inventario inventario = new Tbc_inventario();
//							inventario = recResult;
//							inventario.setCd_serie(serie);
//							inventario.setData_carico(ts);
//							inventario.setNum_carico(buonoCarico);
//							inventario.setCd_carico('A');
//							inventario.setTs_var(ts);
//							inventario.setUte_var((DaoManager.getFirmaUtente(ticket)));
//							daoInv = new Tbc_inventarioDao();
//							if (!daoInv.modificaInventario(inventario)) {
//								output.getErrori().add("ERRORE: errore di fase di aggiornamento dell'inventario");
//							}else{
//								bcDettaglio.setNumeroBuono(String.valueOf(buonoCarico));
//								bcDettaglio.setFornitore(descrFornitore);
//								bcDettaglio.setCodPolo(recResult.getCd_serie().getCd_polo().getCd_polo().getCd_polo());
//								bcDettaglio.setCodBib(recResult.getCd_serie().getCd_polo().getCd_biblioteca());
//								bcDettaglio.setCodInv(String.valueOf(recResult.getCd_inven()));
//								bcDettaglio.setBid(recResult.getB().getBid());
//								bcDettaglio.setIsbd(recResult.getB().getIsbd());
//								bcDettaglio.setCodTipOrdDescr(codici.cercaDescrizioneCodice(recResult.getTipo_acquisizione().toString(),
//										CodiciType.CODICE_TIPO_ACQUISIZIONE_MATERIALE,
//										CodiciRicercaType.RICERCA_CODICE_SBN));
//								bcDettaglio.setValoreDouble((recResult.getValore()).doubleValue());
//
//								totaleValore = totaleValore.add((BigDecimal)recResult.getValore());
//								bcDettaglio.setTotValore(((BigDecimal)recResult.getValore()).doubleValue(), input.getNumberFormat(), input.getLocale());
//								bcDettaglio.setPrecInv(recResult.getPrecis_inv());
//							}
//							output.getLista().add(bcDettaglio);
//						}
//					}
//				}
//
//			}
//
//		}catch (ValidationException e) {
//
//			throw new DataException (e);
//		}catch (DaoManagerException e) {
//
//			throw new DataException (e);
//		} catch (Exception e) {
//
//			throw new DataException (e);
//		}
//		return output;
//	}

	/**
	 * @param input
	 * @param ticket
	 * @param output
	 * @param ts
	 * @param listaInv
	 * @throws DaoManagerException
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws ValidationException
	 * @throws RemoteException
	 */
	private void trattamentoInventarioBuonoCarico(
			StampaBuoniCaricoVO input, String ticket,
			StampaBuoniCaricoVO output, Timestamp ts, List listaInv)
	throws DaoManagerException, ResourceNotFoundException,
	ApplicationException, ValidationException, RemoteException {
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @throws xception
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public List<CodiceVO> getListaInventariCollocatiDa(String codPolo, String codBib, String codUtente, String dataDa, String dataA, String ticket)
	throws ApplicationException, ValidationException, DataException, RemoteException {
		checkTicket(ticket);
		List listaInvCollDa = null;
		CodiceVO rec = null;
		try {
			valida.validaParamRicercaPerInvCollDa(codPolo, codBib, codUtente, dataDa, dataA);
			Tbf_utenti_professionali_webDao daoUteProfWeb = new Tbf_utenti_professionali_webDao();
			Tbf_utenti_professionali_web uteWeb = daoUteProfWeb.select(codUtente);
			if (uteWeb == null){
				throw new DataException("utenteNotFound");
			}
			Tbf_anagrafe_utenti_professionali id_utente_professionale = uteWeb.getId_utente_professionale();
			Trf_utente_professionale_biblioteca bibUteProf = null;
			Set<?> utentiProf = id_utente_professionale.getTrf_utente_professionale_biblioteca();
			if (utentiProf != null) {
				Iterator<?> bibUte = utentiProf.iterator();

				if (bibUte.hasNext()) {
					//tolti set password e ticket
					bibUteProf = (Trf_utente_professionale_biblioteca) bibUte.next();
				}else{
					throw new DataException("utenteNotFound");
				}
			}else{
				throw new DataException("utenteNotFound");
			}
			if (bibUteProf != null){
				if (bibUteProf.getCd_polo().getCd_polo().getCd_polo().equals(codPolo)){
					if (bibUteProf.getCd_polo().getCd_biblioteca().equals(codBib)){
						if (!bibUteProf.getId_utente_professionale().equals(uteWeb.getId_utente_professionale())){
							throw new DataException("utenteNotFound");
						}
					}
					else{
						throw new DataException("utenteNotFound");
					}
				}else{
					throw new DataException("utenteNotFound");
				}
			}else{
				throw new DataException("utenteNotFound");
			}
			daoInv = new Tbc_inventarioDao();
			List<Tbc_inventario> result = daoInv.getListaInventariDa(codPolo, codBib, codUtente, dataDa, dataA);
			if (result.size() > 0) {
				listaInvCollDa = new ArrayList();
				for (int index = 0; index < result.size(); index++) {
					Tbc_inventario recResult = result.get(index);
					rec = new CodiceVO();
					rec.setCodice(recResult.getCd_serie().getCd_serie());
					rec.setDescrizione(String.valueOf(recResult.getCd_inven()));
					listaInvCollDa.add(rec);
				}

			} else {
				throw new DataException("inventariNotFound");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaInvCollDa;
	}
}

