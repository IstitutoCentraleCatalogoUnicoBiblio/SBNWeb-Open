/**
 *
 */
package it.iccu.sbn.ejb.domain.documentofisico;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.utils.AnaliticaCollocazione;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneReticoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneUltKeyLocVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioniUltCollSpecVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareListaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_esemplare_titoloDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_sezione_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_formati_sezioniDao;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_poss_prov_inventariDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Trc_formati_sezioni;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.matchers.DocFisico;
import it.iccu.sbn.util.validation.ValidazioniDocumentoFisico;
import it.iccu.sbn.vo.custom.documentofisico.InventarioPossedutoVO;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.custom.periodici.KardexPeriodicoOpacVO;
import it.iccu.sbn.vo.xml.binding.sbnwebws.CollocazioneType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DisponibilitaType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DocumentoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EsitoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.NonCollocatoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.PossedutoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;



/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Collocazione" description="A session bean named Collocazione"
 * 			display-name="Collocazione"
 *          jndi-name="sbnWeb/Collocazione" type="Stateless"
 *          transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */
public abstract class CollocazioneBean extends TicketChecker implements Collocazione {

	private static final long serialVersionUID = -5995637006811645405L;

	private static Log log = LogFactory.getLog(CollocazioneBean.class);

	private Interrogazione interrogazione;
	private Servizi servizi;
	private DocumentoFisicoCommon dfCommon;


	private Tbc_inventarioDao daoInv;
	private Tbc_sezione_collocazioneDao daoSez;
	private Trc_formati_sezioniDao daoForSez;
	private Tbc_collocazioneDao daoColl;
	private Tbc_esemplare_titoloDao daoEsempl;
	private ValidazioniDocumentoFisico valida = new ValidazioniDocumentoFisico();
	Connection connection = null;
	private PeriodiciSBN periodici;

	private SessionContext ctx;

//	public CollocazioneBean() {
//		super();
//	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public void ejbCreate() {
	}

	public void setSessionContext(SessionContext ctx) throws EJBException,
	RemoteException {
		this.ctx = ctx;
		daoSez = new Tbc_sezione_collocazioneDao();
		daoForSez = new Trc_formati_sezioniDao();
		daoColl = new Tbc_collocazioneDao();
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			this.servizi = DomainEJBFactory.getInstance().getServizi();
			this.interrogazione = factory.getInterrogazione();
			this.dfCommon = factory.getDocumentoFisicoCommon();
			this.periodici = factory.getPeriodici();

			} catch (NamingException e) {

			} catch (CreateException e) {

			}
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
	 *
	 */
	public List getListaSezioni() {
		Tbc_sezione_collocazioneDao dao = new Tbc_sezione_collocazioneDao();
		try {
			dao.selectAll();

		} catch (DaoManagerException e) {


		}
		return null;
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 * @throws RemoteException
	 *
	 * @throws ResourceNotFoundException
	 * @throws RemoteException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getListaSezioni(String codPolo, String codBib, String ticket)
	throws RemoteException,	DataException, ValidationException{
		List<SezioneCollocazioneVO> listaSezioni = null;
		try {
			CodiceVO sezione = new CodiceVO();
			sezione.setCodice(codPolo);
			sezione.setDescrizione(codBib);
			valida.validaPoloBib(sezione);

			List<Tbc_sezione_collocazione> listaSez = daoSez.getListaSezioni(codPolo, codBib);
			if (listaSez.size() > 0) {
				listaSezioni = new ArrayList<SezioneCollocazioneVO>();
				for (int index = 0; index < listaSez.size(); index++) {
					Tbc_sezione_collocazione recResult = listaSez.get(index);
					SezioneCollocazioneVO rec = ConversioneHibernateVO.toWeb().sezioneCollocazione(recResult);
					rec.setPrg(index + 1);
					listaSezioni.add(rec);
				}

			}
		} catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
		return listaSezioni;
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
	 *
	 */
	public boolean insertSezione(SezioneCollocazioneVO sezione, String ticket)
	throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		Timestamp ts = DaoManager.now();
		try{
			valida.validaSezione(sezione);
			Tbc_sezione_collocazione recSez = daoSez.getSezione(sezione.getCodPolo(), sezione.getCodBib(),
					sezione.getCodSezione().trim());
			if (recSez != null){
				if (recSez.getFl_canc()=='N'){
					throw new DataException("recEsistente");
				}else{
					if (!sezione.getFlRecSez().equals(SezioneCollocazioneVO.SEZRECUP)){
						sezione.setFlRecSez(SezioneCollocazioneVO.SEZRECUP);
						throw new ValidationException("msgConfermaRecuperoNomeSez",
								ValidationException.errore, "msgSez", sezione.getFlRecSez() , null);
					}
				}
				if (sezione.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					daoForSez = new Trc_formati_sezioniDao();
					String codFor00 = "00";
					List listaForSez = daoForSez.getListaFormatiSezioni00(sezione.getCodPolo(),
							sezione.getCodBib(), sezione.getCodSezione(), codFor00);
					if (listaForSez.size() > 0){
						recSez.setFl_canc('N');
						recSez.setDescr(sezione.getDescrSezione());
						recSez.setTipo_sez(sezione.getTipoSezione().charAt(0));
						recSez.setCd_colloc(sezione.getTipoColloc().charAt(0));
						recSez.setUte_ins(sezione.getUteAgg());
						recSez.setTs_ins(ts);
						//aggiorno la sezione
						return ret = daoSez.saveUpdateSezione(recSez);
					}else{
//						Integer maxPrgSerie = daoForSez.getMaxPrgSerie(sezione.getCodPolo(),
//						sezione.getCodBib(), sezione.getCodSezione(), codFor00);
//						if (maxPrgSerie == null){
//						maxPrgSerie = 0;
//						maxPrgSerie++;
//						}else if (maxPrgSerie >= 0){
//						maxPrgSerie++;
//						}
						Trc_formati_sezioni forSez = new Trc_formati_sezioni();
						Tbf_polo polo = new Tbf_polo();
						polo.setCd_polo(sezione.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_polo(polo);
						bib.setCd_biblioteca(sezione.getCodBib());
						Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
						sez.setCd_polo(bib);
						sez.setCd_sez(sezione.getCodSezione());

						forSez.setCd_formato("00");
						forSez.setCd_sezione(sez);
						forSez.setProg_serie(0);
						forSez.setDescr(sezione.getDescrSezione());
						forSez.setProg_num(sezione.getProgNum());
						forSez.setUte_var(sezione.getUteAgg());
						forSez.setUte_ins(sezione.getUteIns());
						forSez.setTs_ins(ts);
						forSez.setTs_var(ts);
						forSez.setFl_canc('N');
						recSez.setFl_canc('N');
						recSez.setUte_ins(sezione.getUteAgg());
						recSez.setTs_ins(ts);
						//aggiorno la sezione
						daoSez.saveUpdateSezione(recSez);
						forSez.setCd_sezione(recSez);
						//inserisco in formato sezione
						daoForSez = new Trc_formati_sezioniDao();
						return ret = daoForSez.inserimentoFormatiSezioni(forSez);
					}
				}else{
					//recupero sezione cancellata
					recSez.setUte_ins(sezione.getUteAgg());
					recSez.setTs_ins(ts);
					this.trasformaSezVOHib(sezione, recSez);
					//inserisco la sezione
					daoSez = new Tbc_sezione_collocazioneDao();
					return ret = daoSez.saveUpdateSezione(recSez);
				}
			}else{
				if (sezione.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					//inserisce sezione e formato sezione
					Trc_formati_sezioni forSez = new Trc_formati_sezioni();
					Tbf_polo polo = new Tbf_polo();
					polo.setCd_polo(sezione.getCodPolo());
					Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
					bib.setCd_polo(polo);
					bib.setCd_biblioteca(sezione.getCodBib());
					Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
					sez.setCd_polo(bib);
					sez.setCd_sez(sezione.getCodSezione());

					forSez.setCd_formato("00");
					forSez.setCd_sezione(sez);
					forSez.setProg_serie(0);
					forSez.setDescr(sezione.getDescrSezione());
					forSez.setProg_num(sezione.getProgNum());
					forSez.setUte_var(sezione.getUteAgg());
					forSez.setUte_ins(sezione.getUteIns());
					forSez.setTs_ins(ts);
					forSez.setTs_var(ts);
					forSez.setFl_canc('N');

					recSez = new Tbc_sezione_collocazione();
					recSez.setCd_polo(bib);
					recSez.setCd_sez(sezione.getCodSezione().trim());
					this.trasformaSezVOHib(sezione, recSez);
					//inserisco la sezione
					recSez.setFl_canc('N');
					recSez.setUte_ins(sezione.getUteAgg());
					recSez.setTs_ins(ts);
					daoSez.saveUpdateSezione(recSez);
					forSez.setCd_sezione(recSez);
					//inserisco in formato sezione
					daoForSez = new Trc_formati_sezioniDao();
					return ret = daoForSez.inserimentoFormatiSezioni(forSez);
				} else {
					//inserisce solo sezione
					recSez =  new Tbc_sezione_collocazione();
					Tbf_polo polo = new Tbf_polo();
					polo.setCd_polo(sezione.getCodPolo());
					Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
					bib.setCd_polo(polo);
					bib.setCd_biblioteca(sezione.getCodBib());
					Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
					sez.setCd_polo(bib);
					sez.setCd_sez(sezione.getCodSezione());
					recSez.setCd_polo(bib);
					recSez.setCd_sez(sezione.getCodSezione());
					recSez.setUte_ins(sezione.getUteAgg());
					recSez.setTs_ins(ts);
					this.trasformaSezVOHib(sezione, recSez);
					//inserisco la sezione
					daoSez = new Tbc_sezione_collocazioneDao();
					return ret = daoSez.saveUpdateSezione(recSez);
				}
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}
//		return ret;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws Exception
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public boolean insertSezione4Import(SezioneCollocazioneVO sezione, String ticket) throws Exception {
		try {
			return insertSezione(sezione, ticket);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
	 *
	 */
	public SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket)
			throws DataException, ApplicationException, ValidationException {
		SezioneCollocazioneVO dettaglio = null;
		Tbc_sezione_collocazione sezione = null;
		boolean sezioneSpazio = !ValidazioneDati.isFilled(codSez);
		try {
			SezioneCollocazioneVO recVal = new SezioneCollocazioneVO();
			recVal.setCodPolo(codPolo);
			recVal.setCodBib(codBib);
			recVal.setCodSezione(codSez.trim());
			valida.validaSezione(recVal);
			daoSez = new Tbc_sezione_collocazioneDao();
			sezione = daoSez.getSezione(codPolo, codBib, codSez.trim());
			if (sezione != null) {
				if (sezione.getFl_canc() == 'N') {
					dettaglio = ConversioneHibernateVO.toWeb().sezioneCollocazione(sezione);
				} else {
					throw new DataException("recSezioneInesistente");
				}
			} else {
				throw new DataException("recSezioneInesistente");
			}
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			if (sezioneSpazio)
				e = new DataException(SbnErrorTypes.GDF_SEZIONE_SPAZIO_NON_DEFINITA, "recSezioneInesistente");
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {
			throw new DataException(e);
		}
		return dettaglio;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws Exception
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public SezioneCollocazioneVO getSezioneDettaglio4Import(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		try {
			return getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
	 *
	 */
	public boolean  deleteSezione(SezioneCollocazioneVO recSezione, String ticket)
	throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		try{
			SezioneCollocazioneVO rec = new SezioneCollocazioneVO();
			rec.setCodPolo(recSezione.getCodPolo());
			rec.setCodBib(recSezione.getCodBib());
			rec.setCodSezione(recSezione.getCodSezione());
			valida.validaSezione(rec);
			Tbc_sezione_collocazione sezione = daoSez.getSezione(recSezione.getCodPolo(), recSezione.getCodBib(), recSezione.getCodSezione().trim());
			if (sezione != null){
				if (sezione.getTot_inv()>0){
					throw new ValidationException("erroreSezioneNonCancellabile",
							ValidationException.errore);
				}
				Timestamp ts = DaoManager.now();
				if (recSezione.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					daoForSez = new Trc_formati_sezioniDao();
					Trc_formati_sezioni forSez = daoForSez.getFormatiSezioni00(recSezione.getCodPolo(), recSezione.getCodBib(), recSezione.getCodSezione());
					if (forSez != null){
						forSez.setUte_var(recSezione.getUteAgg());
						forSez.setFl_canc('S');
						forSez.setTs_var(ts);
						sezione.setFl_canc('S');
						sezione.setTs_var(ts);
						sezione.setUte_var(recSezione.getUteAgg());
						daoForSez = new Trc_formati_sezioniDao();
//						ret = daoForSez.cancellaFormatiSezioni(recSezione.getCodPolo(), recSezione.getCodBib(), recSezione.getCodSezione(), recSezione.getUteAgg());
						ret = daoSez.saveUpdateSezione(sezione);
						ret = daoForSez.cancellaFormatiSezioni(forSez);
						if (!ret){
							throw new ValidationException("erroreCancFormatoSezione", ValidationException.errore);
						}else{
							return ret;
						}
					}
				}
				ret = false;
				sezione.setFl_canc('S');
				sezione.setTs_var(ts);
				sezione.setUte_var(recSezione.getUteAgg());
				if (sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO).charAt(0)
						|| sezione.getCd_colloc() == (DocumentoFisicoCostant.COD_CONTINUAZIONE).charAt(0)){
					Set forSez = sezione.getTrc_formati_sezioni();
					if (forSez.size() > 0){
						Iterator iter = forSez.iterator();
						while (iter.hasNext()){
							Trc_formati_sezioni recForSez = (Trc_formati_sezioni)iter.next();
							daoForSez = new Trc_formati_sezioniDao();
							Trc_formati_sezioni recForSez1 = daoForSez.getFormatiSezioni(recForSez.getCd_sezione().getCd_polo().getCd_polo().getCd_polo(),
									recForSez.getCd_sezione().getCd_polo().getCd_biblioteca(),
									recForSez.getCd_sezione().getCd_sez(), recForSez.getCd_formato());
							if (recForSez.getFl_canc() == 'N'){
							//modifica
							recForSez.setUte_var(recSezione.getUteAgg());
							recForSez.setFl_canc('S');
							recForSez.setTs_var(ts);
							sezione.getTrc_formati_sezioni().add(recForSez);
							}
						}
					}
				}
				daoSez = new Tbc_sezione_collocazioneDao();
				ret = daoSez.saveUpdateSezione(sezione);
				if (!ret){
					throw new ValidationException("erroreCancSezione",
							ValidationException.errore);
				}
			}else{
				throw new ValidationException("erroreSezioneInesistente",
						ValidationException.errore);
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
	 * @throws ResourceNotFoundException
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public boolean  updateSezione(SezioneCollocazioneVO sezione, String ticket)
	throws DataException, ApplicationException, ValidationException	{

		boolean ret = false;
		Tbc_sezione_collocazione sez = null;
		Trc_formati_sezioni forSez = null;
		Timestamp ts = DaoManager.now();
		try{
			valida.validaSezione(sezione);
			Tbc_sezione_collocazione recSez = daoSez.getSezione(sezione.getCodPolo(), sezione.getCodBib(), sezione.getCodSezione());
			if (recSez != null){
				if (sezione.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
					daoForSez = new Trc_formati_sezioniDao();
					String codFor00 = "00";
					List listaForSez = daoForSez.getListaFormatiSezioni00(sezione.getCodPolo(),
							sezione.getCodBib(), sezione.getCodSezione(), codFor00);
					if (listaForSez.size() > 0){
					}else{
						//inserimento
						forSez = new Trc_formati_sezioni();
						Tbf_polo polo = new Tbf_polo();
						polo.setCd_polo(sezione.getCodPolo());
						Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
						bib.setCd_polo(polo);
						bib.setCd_biblioteca(sezione.getCodBib());
						sez = new Tbc_sezione_collocazione();
						sez.setCd_polo(bib);
						sez.setCd_sez(sezione.getCodSezione());

						forSez.setCd_formato("00");
						forSez.setCd_sezione(sez);
						forSez.setProg_serie(0);
						forSez.setDescr(sezione.getDescrSezione());
						forSez.setProg_num(sezione.getProgNum());
						forSez.setUte_var(sezione.getUteAgg());
						forSez.setUte_ins(sezione.getUteIns());
						forSez.setTs_ins(ts);
						forSez.setTs_var(ts);
						forSez.setFl_canc('N');
						recSez.setFl_canc('N');
						recSez.setUte_ins(sezione.getUteAgg());
						recSez.setTs_ins(ts);
						//aggiorno la sezione
						this.trasformaSezVOHib(sezione, recSez);
						daoSez.saveUpdateSezione(recSez);
						forSez.setCd_sezione(recSez);
						//inserisco in formato sezione
						daoForSez = new Trc_formati_sezioniDao();
						return ret = daoForSez.inserimentoFormatiSezioni(forSez);
					}
				}
				//inserimento solo sezione
				this.trasformaSezVOHib(sezione, recSez);
				ret = daoSez.saveUpdateSezione(recSez);
				return ret = daoSez.saveUpdateSezione(recSez);
//				Tbf_polo polo = new Tbf_polo();
//				polo.setCd_polo(sezione.getCodPolo());
//				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
//				bib.setCd_polo(polo);
//				bib.setCd_biblioteca(sezione.getCodBib());
//				sez = new Tbc_sezione_collocazione();
//				sez.setCd_polo(bib);
//				sez.setCd_sez(sezione.getCodSezione());
//				recSez.setCd_polo(bib);
//				recSez.setCd_sez(sezione.getCodSezione());
//				recSez.setUte_ins(sezione.getUteAgg());
//				recSez.setTs_ins(ts);
//				this.trasformaSezVOHib(sezione, recSez);
//				//inserimento la sezione
//				daoSez = new Tbc_sezione_collocazioneDao();
//				return ret = daoSez.saveUpdateSezione(recSez);
		}else{
			throw new DataException("recInesistente");
		}
		}catch (ValidationException e) {
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
	 * @throws RemoteException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getListaFormatiSezioni(String codPolo, String codBib, String ticket)
	throws RemoteException, DataException, ValidationException {
		List lista = null;
		FormatiSezioniVO rec = null;
		try {
			valida.validaFormatiSezioni(codPolo, codBib);
			daoForSez = new Trc_formati_sezioniDao();
			List listaForSez =  daoForSez.getListaFormatiSezioni(codPolo, codBib);
			if (listaForSez.size() > 0){
				lista = new ArrayList();
				for (int index = 0; index < listaForSez.size(); index++) {
					Trc_formati_sezioni recResult = (Trc_formati_sezioni)listaForSez.get(index);
					rec = new FormatiSezioniVO();
					rec.setPrg(index + 1);
					rec.setDataIns(String.valueOf(recResult.getTs_ins()));
					rec.setDataAgg(String.valueOf(recResult.getTs_var()));
					rec.setCodFormato(recResult.getCd_formato());
					rec.setCodPolo(recResult.getCd_sezione().getCd_polo().getCd_polo().getCd_polo());
					rec.setCodBib(recResult.getCd_sezione().getCd_polo().getCd_biblioteca());
					rec.setCodSez(recResult.getCd_sezione().getCd_sez());
					rec.setDescrFor(recResult.getDescr());
					rec.setProgSerie(recResult.getProg_serie());
					rec.setProgNum(recResult.getProg_num());
					rec.setNumeroPezzi(recResult.getN_pezzi());
					rec.setNumeroPezziMisc(recResult.getN_pezzi_misc());
					rec.setProgSerieNum1Misc(recResult.getProg_serie_num1_misc());
					rec.setDalProgrMisc(recResult.getProgr_num1_misc());
					rec.setProgSerieNum2Misc(recResult.getProg_serie_num2_misc());
					rec.setAlProgrMisc(recResult.getProgr_num2_misc());
					lista.add(rec);
				}
			}
		}catch (ValidationException e) {
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
	 * @throws RemoteException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getListaFormatiSezioni(String codPolo, String codBib, String codSez, String ticket)
	throws RemoteException, DataException, ValidationException {
		List lista = null;
		FormatiSezioniVO rec = null;
		try {
			valida.validaFormatiSezioni(codPolo, codBib, codSez);
			daoForSez = new Trc_formati_sezioniDao();
			List listaForSez =  daoForSez.getListaFormatiSezioni(codPolo, codBib, codSez);
			if (listaForSez.size() > 0){
				lista = new ArrayList();
				for (int index = 0; index < listaForSez.size(); index++) {
					Trc_formati_sezioni recResult = (Trc_formati_sezioni)listaForSez.get(index);
					rec = new FormatiSezioniVO();
					rec.setPrg(index + 1);
					rec.setDataIns(String.valueOf(recResult.getTs_ins()));
					rec.setDataAgg(String.valueOf(recResult.getTs_var()));
					rec.setCodFormato(recResult.getCd_formato());
					rec.setCodPolo(recResult.getCd_sezione().getCd_polo().getCd_polo().getCd_polo());
					rec.setCodBib(recResult.getCd_sezione().getCd_polo().getCd_biblioteca());
					rec.setCodSez(recResult.getCd_sezione().getCd_sez());
					rec.setDescrFor(recResult.getDescr().trim());
					rec.setProgSerie(recResult.getProg_serie());
					rec.setProgNum(recResult.getProg_num());
					rec.setNumeroPezzi(recResult.getN_pezzi());
					rec.setNumeroPezziMisc(recResult.getN_pezzi_misc());
					rec.setProgSerieNum1Misc(recResult.getProg_serie_num1_misc());
					rec.setDalProgrMisc(recResult.getProgr_num1_misc());
					rec.setProgSerieNum2Misc(recResult.getProg_serie_num2_misc());
					rec.setAlProgrMisc(recResult.getProgr_num2_misc());
					lista.add(rec);
				}
			}
		}catch (ValidationException e) {
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
	 * @throws RemoteException
	 * @throws ValidationException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getListaFormatiBib(String codPolo, String codBib, String codSez, String ticket)
	throws RemoteException, DataException, ValidationException {
		List lista = null;
		FormatiSezioniVO rec = null;
		try {
			valida.validaFormatiSezioni(codPolo, codBib, codSez);
			daoForSez = new Trc_formati_sezioniDao();
			List listaForSez =  daoForSez.getListaFormatiBib(codPolo, codBib, codSez);
			if (listaForSez.size() > 0){
				lista = new ArrayList();
				for (int index = 0; index < listaForSez.size(); index++) {
					Trc_formati_sezioni recResult = (Trc_formati_sezioni)listaForSez.get(index);
					rec = new FormatiSezioniVO();
					rec.setPrg(index + 1);
					rec.setDataIns(String.valueOf(recResult.getTs_ins()));
					rec.setDataAgg(String.valueOf(recResult.getTs_var()));
					rec.setCodFormato(recResult.getCd_formato());
					rec.setCodPolo(recResult.getCd_sezione().getCd_polo().getCd_polo().getCd_polo());
					rec.setCodBib(recResult.getCd_sezione().getCd_polo().getCd_biblioteca());
					rec.setCodSez(recResult.getCd_sezione().getCd_sez());
					rec.setDescrFor(recResult.getDescr());
					rec.setProgSerie(recResult.getProg_serie());
					rec.setProgNum(recResult.getProg_num());
					rec.setNumeroPezzi(recResult.getN_pezzi());
					//
					rec.setNumeroPezziMisc(recResult.getN_pezzi_misc());
					rec.setProgSerieNum1Misc(recResult.getProg_serie_num1_misc());
					rec.setDalProgrMisc(recResult.getProgr_num1_misc());
					rec.setProgSerieNum2Misc(recResult.getProg_serie_num2_misc());
					rec.setAlProgrMisc(recResult.getProgr_num2_misc());
					if (String.valueOf(recResult.getCd_sezione().getCd_colloc()).equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| String.valueOf(recResult.getCd_sezione().getCd_colloc()).equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
					lista.add(rec);
					}
				}
			}
		}catch (ValidationException e) {
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
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public FormatiSezioniVO getFormatiSezioniDettaglio(String codPolo, String codBib, String codSez,
			String codFor, String ticket)
	throws DataException, ApplicationException, ValidationException {
		FormatiSezioniVO recForSez = null;
		try{
			FormatiSezioniVO forSez = new FormatiSezioniVO();
			forSez.setCodPolo(codPolo);
			forSez.setCodBib(codBib);
			forSez.setCodSez(codSez);
			forSez.setCodSez(codFor);
			valida.validaFormatiSezioni(forSez);
			daoForSez = new Trc_formati_sezioniDao();
			Trc_formati_sezioni formatoSezione = daoForSez.getFormatiSezioni(codPolo, codBib, codSez, codFor);
			if (formatoSezione != null){
				recForSez = new FormatiSezioniVO();
				recForSez.setCodPolo(formatoSezione.getCd_sezione().getCd_polo().getCd_polo().getCd_polo());
				recForSez.setCodBib(formatoSezione.getCd_sezione().getCd_polo().getCd_biblioteca());
				recForSez.setCodSez(formatoSezione.getCd_sezione().getCd_sez());
				recForSez.setCodFormato(formatoSezione.getCd_formato());
				recForSez.setProgSerie(formatoSezione.getProg_serie());
				recForSez.setProgNum(formatoSezione.getProg_num());
				recForSez.setDataIns(String.valueOf(formatoSezione.getTs_ins()));
				recForSez.setDataAgg(String.valueOf(formatoSezione.getTs_var()));
				recForSez.setUteIns(formatoSezione.getUte_ins());
				recForSez.setUteAgg(formatoSezione.getUte_var());
				recForSez.setNumeroPezzi(formatoSezione.getN_pezzi());
				recForSez.setDescrFor(formatoSezione.getDescr().trim());
				recForSez.setNumeroPezziMisc(formatoSezione.getN_pezzi_misc());
				recForSez.setProgSerieNum1Misc(formatoSezione.getProg_serie_num1_misc());
				recForSez.setDalProgrMisc(formatoSezione.getProgr_num1_misc());
				recForSez.setProgSerieNum2Misc(formatoSezione.getProg_serie_num2_misc());
				recForSez.setAlProgrMisc(formatoSezione.getProgr_num2_misc());

				return recForSez;
			}else{
				throw new DataException("recInesistente");
			}
		}catch (ValidationException e) {
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
	 * @throws ResourceNotFoundException
	 * @throws RemoteException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public boolean insertFormatiSezioni(FormatiSezioniVO forSez, String ticket)
	throws ResourceNotFoundException, ApplicationException, RemoteException {
		Trc_formati_sezioni recForSez = null;

		Timestamp ts = DaoManager.now();
		boolean ret = false;
		try{
			valida.validaFormatiSezioni(forSez);
			daoForSez = new Trc_formati_sezioniDao();
			recForSez = daoForSez.getFormatiSezioni(forSez.getCodPolo(), forSez.getCodBib(), forSez.getCodSez().trim(), forSez.getCodFormato());
			Trc_formati_sezioni rec = new Trc_formati_sezioni();
			if (recForSez == null){
				//insertFormatiSezioni
				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(forSez.getCodPolo());
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(forSez.getCodBib());
				bib.setCd_polo(polo);
				Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
				sez.setCd_polo(bib);
				sez.setCd_sez(forSez.getCodSez().toUpperCase());


				rec.setCd_sezione(sez);
				rec.setCd_formato(forSez.getCodFormato());
				rec.setDescr(forSez.getDescrFor());
				rec.setN_pezzi(forSez.getNumeroPezzi());
				rec.setProg_serie(forSez.getProgSerie());
				rec.setProg_num(forSez.getProgNum());
				rec.setN_pezzi_misc(forSez.getNumeroPezziMisc());
				rec.setProg_serie_num1_misc(forSez.getProgSerieNum1Misc());
				rec.setProgr_num1_misc(forSez.getDalProgrMisc());
				rec.setProg_serie_num2_misc(forSez.getProgSerieNum2Misc());
				rec.setProgr_num2_misc(forSez.getAlProgrMisc());

				rec.setTs_ins(ts);
				rec.setTs_var(ts);
				rec.setUte_ins(forSez.getUteAgg());
				rec.setUte_var(forSez.getUteAgg());
				rec.setFl_canc('N');
				ret = daoForSez.inserimentoFormatiSezioni(rec);
			}else{
				throw new DataException("recEsistente");
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return ret;
	}
	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws RemoteException
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public boolean updateFormatiSezioni(FormatiSezioniVO forSez, String ticket)
	throws DataException, ApplicationException, ValidationException	{

		boolean ret = false;
		try{
			valida.validaFormatiSezioni(forSez);
			daoForSez = new Trc_formati_sezioniDao();
			Trc_formati_sezioni recForSez = daoForSez.getFormatiSezioni(forSez.getCodPolo(),forSez.getCodBib(),
					forSez.getCodSez(), forSez.getCodFormato());
			if (recForSez != null){
				if (!forSez.getDataAgg().equals(recForSez.getTs_var().toString())){
					throw new ValidationException("erroreFormatoModDaAltroUtente",
							ValidationException.errore);
				}else{
					//modifica formatiSezione
					boolean changePrgSerie = (forSez.getProgSerie() != recForSez.getProg_serie() );
					if (changePrgSerie && forSez.getProgSerie() < recForSez.getProg_serie())
						throw new ValidationException("prgSerieNonPuoEssereMinoreDelValorePrec");
					//almaviva5_20140911 #5638
					if (!changePrgSerie && forSez.getProgNum() < recForSez.getProg_num())
						throw new ValidationException("prgAssNonPuoEssereMinoreDelValorePrec");

					recForSez.setTs_var(DaoManager.now());
					recForSez.setProg_serie(forSez.getProgSerie());
					recForSez.setProg_num(forSez.getProgNum());
					recForSez.setDescr(forSez.getDescrFor().trim());
					recForSez.setUte_var(forSez.getUteAgg());
					recForSez.setN_pezzi_misc(forSez.getNumeroPezziMisc());
					recForSez.setFl_canc('N');
					ret = daoForSez.modificaFormatiSezioni(recForSez);
				}
			}else{
				throw new DataException("recInesistente");
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
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
	 * @throws DAOException
	 * @throws RemoteException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String bibliotecaOperante, String ticket)
	throws DAOException, RemoteException {

		// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
		// operante che nel caso di centro Sistema non coincidono
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass =
			new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(DaoManager.codPoloFromTicket(ticket) + DaoManager.codBibFromTicket(ticket));
//		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
		// Fine Modifica almaviva2 16.07.2010


		areaDatiPass.setBidRicerca(bid);
		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(true);
		areaDatiPass.setCodiceBiblioSbn(bibliotecaOperante);

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO returnVO = null;
		try {
			returnVO = interrogazione.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass , ticket);
			// gestire errore sbnmarc

			DatiBibliograficiCollocazioneVO datiBibliografici = AnaliticaCollocazione.getDatiBibliografici(returnVO.getTreeElementViewTitoli());
			// coll.getListaTitoliCollocazione(newAnalitica) // restituisce array bid/titolo/natura adatti a collocazione
			// coll.getTitoloGenerale(newAnalitica) // restituisce la monografia superiore del bid radice dell'albero
			// o se stesso.
			return datiBibliografici;
		} catch (DAOException e) {
			throw e;
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
	 *
	 */
	public CollocazioneVO getCollocazione(int keyLoc, String ticket)
	throws DataException, ApplicationException, ValidationException {

		CollocazioneVO coll = null;
		Tbc_collocazione collocazione = null;

		try{
			if (keyLoc == 0)
				throw new ValidationException("validaCollKeyCollocObbligatorio", ValidationException.errore);

			daoColl = new Tbc_collocazioneDao();

			collocazione = daoColl.getCollocazione(keyLoc);
			if (collocazione != null)
				coll = ConversioneHibernateVO.toWeb().collocazione(collocazione);

		}catch (ValidationException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return coll;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws Exception
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public CollocazioneVO getCollocazione4Import(int keyLoc, String ticket) throws Exception {
		try {
			return getCollocazione(keyLoc, ticket);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
	 *
	 */
	public CollocazioneVO getCollocazione(CollocazioneVO recColl, String ticket)
//	throws ValidationException, DaoManagerException, DataException {
	throws DataException, ApplicationException, ValidationException {
		CollocazioneVO coll = null;
		boolean ret = false;
		String locNormalizzata = null;
		String specNormalizzata = null;
		try{
			valida.validaCollocazione(recColl);
			daoColl = new Tbc_collocazioneDao();
			daoSez = new Tbc_sezione_collocazioneDao();
			Tbc_sezione_collocazione sezione = daoSez.getSezione(recColl.getCodPoloSez(),
					recColl.getCodBibSez(), recColl.getCodSez());
			if (sezione != null){
				//almaviva5_20121019 #5153
				String cdColloc = String.valueOf(sezione.getCd_colloc());

				if (!recColl.getCodColloc().trim().equals("")){
					String locDaNormalizzare = recColl.getCodColloc();
					if (cdColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
						locNormalizzata = OrdinamentoCollocazione2.normalizza(locDaNormalizzare, true);
					}else{
						locNormalizzata = OrdinamentoCollocazione2.normalizza(locDaNormalizzare);
					}
				}else{
					locNormalizzata = ValidazioneDati.fillRight("", ' ', 80);
				}
				if(!recColl.getSpecColloc().trim().equals("")){
					String specDaNormalizzare = recColl.getSpecColloc();
					if (cdColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
						specNormalizzata = OrdinamentoCollocazione2.normalizza(specDaNormalizzare, true);
					}else{
						specNormalizzata = OrdinamentoCollocazione2.normalizza(specDaNormalizzare);
					}
				}else{
					specNormalizzata = ValidazioneDati.fillRight("", ' ', 40);
				}

//				if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
//						recColl.getCodColloc(), recColl.getSpecColloc())){
					if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
							locNormalizzata, specNormalizzata)){
					throw new ValidationException("msgCodLocEsistente",
							ValidationException.errore, "msgCodLoc", recColl.getCodColloc(), recColl.getSpecColloc());
				}else{
					coll = null;
				}
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return coll;
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
	public CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket)
	throws DataException, ApplicationException, ValidationException {

		CollocazioneDettaglioVO coll = null;
		Tbc_collocazione collocazione = null;

		try{
			if (keyLoc == 0)
				throw new ValidationException("validaCollKeyCollocObbligatorio", ValidationException.errore);

			daoColl = new Tbc_collocazioneDao();

			collocazione = daoColl.getCollocazione(keyLoc);
			if (collocazione != null)
				coll = ConversioneHibernateVO.toWeb().collocazioneDettaglio(collocazione);

		}catch (ValidationException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return coll;
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
//	public CollocazioneVO getCollocazione(int keyLoc, String ticket)
//	throws DataException, ApplicationException, ValidationException {
//
//		CollocazioneVO coll = null;
//		try{
//			daoColl = new Tbc_collocazioneDao();
//			Tbc_collocazione rec = daoColl.getCollocazione(keyLoc);
//			if (coll != null){
//				coll = new CollocazioneVO();
//				coll.setDataIns(rs.getString("data_ins"));
//				coll.setDataAgg(rs.getString("data_agg"));
//				coll.setKeyColloc(rs.getInt("key_loc"));
//				coll.setCodColloc(rs.getString("cod_loc"));
//				coll.setSpecColloc(rs.getString("spec_loc"));
//				coll.setConsistenza(rs.getString("consis"));
//				coll.setTotInv(rs.getInt("tot_inv"));
//				coll.setCodBibDoc(rs.getString("cod_bib_doc"));
//				coll.setBidDoc(rs.getString("bid_doc"));
//				coll.setCodDoc(rs.getInt("cod_doc"));
//				coll.setBid(rs.getString("bid"));
////				rec.setCodCla(rs.getString("cod_cla"));
//				coll.setIndice(rs.getString("indice"));
//				coll.setCodBib(rs.getString("cod_bib"));
//				coll.setCodSez(rs.getString("cod_sez"));
//				coll.setOrdLoc(rs.getString("ord1_loc"));
//				coll.setOrdSpec(rs.getString("ord2_loc"));
////				rec.setOrd3Colloc(rs.getString("ord3_loc"));
////				rec.setOrd4Colloc(rs.getString("ord4_loc"));
//				coll.setTotInvProv(rs.getInt("tot_inv_prov"));
//				coll.setCancDb2i(rs.getString("canc_db2i"));
//			}else{
//				coll = null;
//			}
//		} catch (DaoManagerException e) {
//
//			throw new ValidationException(e);
//		} catch (ValidationException e) {
//
//			throw e;
//		}
//		return coll;
//	}
//
//
//
//		Connection connection = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List lista = new ArrayList();
//		CollocazioneVO rec =null;
//		try {
//			connection = this.connection;
//			pstmt = connection.prepareStatement("select * from tbc_localizzazione where key_loc = "+keyLoc+
//					" and canc_db2i <> 'S';");
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				rec = new CollocazioneVO();
//				rec.setDataIns(rs.getString("data_ins"));
//				rec.setDataAgg(rs.getString("data_agg"));
//				rec.setKeyColloc(rs.getInt("key_loc"));
//				rec.setCodColloc(rs.getString("cod_loc"));
//				rec.setSpecColloc(rs.getString("spec_loc"));
//				rec.setConsistenza(rs.getString("consis"));
//				rec.setTotInv(rs.getInt("tot_inv"));
//				rec.setCodBibDoc(rs.getString("cod_bib_doc"));
//				rec.setBidDoc(rs.getString("bid_doc"));
//				rec.setCodDoc(rs.getInt("cod_doc"));
//				rec.setBid(rs.getString("bid"));
////				rec.setCodCla(rs.getString("cod_cla"));
//				rec.setIndice(rs.getString("indice"));
//				rec.setCodBib(rs.getString("cod_bib"));
//				rec.setCodSez(rs.getString("cod_sez"));
//				rec.setOrdLoc(rs.getString("ord1_loc"));
//				rec.setOrdSpec(rs.getString("ord2_loc"));
////				rec.setOrd3Colloc(rs.getString("ord3_loc"));
////				rec.setOrd4Colloc(rs.getString("ord4_loc"));
//				rec.setTotInvProv(rs.getInt("tot_inv_prov"));
//				rec.setCancDb2i(rs.getString("canc_db2i"));
//				lista.add(rec);
//			} // End while
//			rs.close();
//			//connection.close();
//			if (lista.size()>0){
//				if (lista.size()==1){
//					//trovato
//				}else{
//					throw new ValidationException("trovatoRecordNonUnivoco",
//							ValidationExceptionCodici.errore);
//				}
//			}else{
//				throw new ValidationException("collCollocazioneInesistente",
//						ValidationException.errore);
//			}
//		} catch (ValidationException e) {
//			throw e;
//		} catch (DataException e) {
//			throw e;
//		} catch (Exception e) {
//
//		}
//		return rec;
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
	 *
	 */
	public boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean datiMappaModificati, boolean confermaEsistenza, String ticket)
	throws DataException, ValidationException {

		boolean ret = false;
		String locNormalizzata = null;
		String specNormalizzata = null;
		Timestamp ts = DaoManager.now();

		int serie = 0;
		int num = 0;

		try {
			valida.validaCollocazione(recColl);
			daoColl = new Tbc_collocazioneDao();
			Tbc_collocazione collocazione = daoColl.getCollocazione(recColl.getKeyColloc());
			if (collocazione != null){
				if (!recColl.getDataAgg().equals(collocazione.getTs_var().toString())){
					throw new DataException("collModAltroUtente");
				}
				if (ValidazioneDati.in(collocazione.getFl_canc(), 'S', 's'))
					throw new DataException("collCancellata");

				if (!recColl.getCodColloc().trim().equals("")){
					String locDaNormalizzare = recColl.getCodColloc();
					if (recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
						locNormalizzata = OrdinamentoCollocazione2.normalizza(locDaNormalizzare, true);
					}else{
						locNormalizzata = OrdinamentoCollocazione2.normalizza(locDaNormalizzare);
					}
				}else{
					locNormalizzata = ValidazioneDati.fillRight("", ' ', 80);
				}
				if(!recColl.getSpecColloc().trim().equals("")){
					String specDaNormalizzare = recColl.getSpecColloc();
					if (recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)){
						specNormalizzata = OrdinamentoCollocazione2.normalizza(specDaNormalizzare, true);
					}else{
						specNormalizzata = OrdinamentoCollocazione2.normalizza(specDaNormalizzare);
					}
				}else{
					specNormalizzata = ValidazioneDati.fillRight("", ' ', 40);
				}
///

				//controllo se è cambiata la sezione
				if (!collocazione.getCd_sez().getCd_sez().trim().equals(recColl.getCodSez().trim()) &&
						collocazione.getCd_sez().getCd_polo().getCd_biblioteca().equals(recColl.getCodBibSez()) &&
						collocazione.getCd_sez().getCd_polo().getCd_polo().getCd_polo().equals(recColl.getCodPoloSez())){

					//aggiorno la vecchia sezione
					collocazione.getCd_sez().setTot_inv(collocazione.getCd_sez().getTot_inv() - recColl.getTotInv());

					//aggiorno la nuova sezione
					daoSez = new Tbc_sezione_collocazioneDao();
					Tbc_sezione_collocazione sezione = daoSez.getSezione(recColl.getCodPoloSez(),
							recColl.getCodBibSez(), recColl.getCodSez());
					if (sezione != null) {
						//almaviva5_20141028 #5662
						if (ValidazioneDati.in(sezione.getFl_canc(), 'S', 's'))
							throw new DataException("erroreSezioneInesistente");

						if (recColl.getTipoColloc() != null && recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
								|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
								|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
							if (recColl.getRecFS() != null){
								//trattamento di calcolo serie/progressivo per formatiSezione per assegnare il codColloc e il codSpec
								//è posizionato qui perchè mi serve il tipo coll di sezione
								dfCommon.assegnaSerieProgr(recColl, ts, sezione, serie, num, null);
								locNormalizzata = OrdinamentoCollocazione2.normalizza(recColl.getCodColloc());
								specNormalizzata = OrdinamentoCollocazione2.normalizza(recColl.getSpecColloc());
								if (specNormalizzata == null){
									specNormalizzata="";
								}
								daoColl =  new Tbc_collocazioneDao();
								if (recColl.getTipoColloc() != null && recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
										|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
									if (datiMappaModificati){
										if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
												locNormalizzata, specNormalizzata, recColl.getKeyColloc())){
											if (!confermaEsistenza){
												throw new ValidationException("msgCodLocEsistente",
														ValidationException.errore, "msgCodLocEsistente", (recColl.getRecFS().getCodFormato()+String.valueOf(recColl.getRecFS().getProgSerie())), String.valueOf(recColl.getRecFS().getProgNum()));
											}
										}
									}
								}else{
									//non a formato
									if (datiMappaModificati){
										if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
												locNormalizzata, "", recColl.getKeyColloc())){
											if (!confermaEsistenza){
												throw new ValidationException("msgCodLocEsistente",
														ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getRecFS().getProgNum())), "");
											}
										}
									}
								}
								//fine trattamento di calcolo serie/progressivo per formatiSezione
							}
						}else{//bug 0004731 esercizio
							if (datiMappaModificati){
								if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
										locNormalizzata, specNormalizzata, recColl.getKeyColloc())){
									if (!confermaEsistenza){
										throw new ValidationException("msgCodLocEsistente",
												ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getCodColloc())), String.valueOf(recColl.getSpecColloc()));
									}
								}
							}
						}
						sezione.setTot_inv(collocazione.getTot_inv() + sezione.getTot_inv());
						sezione.setUte_var(recColl.getUteAgg());
						sezione.setTs_var(ts);
						collocazione.setCd_sez(sezione);
					}else{
						throw new DataException("sezNonEsistente");
					}
				}else{
					//la sezione non è cambiata
					if (recColl.getTipoColloc() != null && recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)
							|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO)){
						if (recColl.getRecFS() != null){
							//trattamento di calcolo serie/progressivo per formatiSezione per assegnare il codColloc e il codSpec
							//è posizionato qui perchè mi serve il tipo coll di sezione
							dfCommon.assegnaSerieProgr(recColl, ts, collocazione.getCd_sez(), serie, num, null);
							locNormalizzata = OrdinamentoCollocazione2.normalizza(recColl.getCodColloc());
							specNormalizzata = OrdinamentoCollocazione2.normalizza(recColl.getSpecColloc());
							if (specNormalizzata == null){
								specNormalizzata="";
							}
							daoColl =  new Tbc_collocazioneDao();
							if (recColl.getTipoColloc() != null && recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
									|| recColl.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
								if (datiMappaModificati){
									if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
											locNormalizzata, specNormalizzata, recColl.getKeyColloc())){
										if (!confermaEsistenza){
											throw new ValidationException("msgCodLocEsistente",
													ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getCodColloc())), String.valueOf(recColl.getSpecColloc()));
										}
									}
								}
							}else{
								//non a formato
								if (datiMappaModificati){
									if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
											locNormalizzata, "", recColl.getKeyColloc())){
										if (!confermaEsistenza){
											throw new ValidationException("msgCodLocEsistente",
													ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getRecFS().getProgNum())), "");
										}
									}
								}
							}
							//fine trattamento di calcolo serie/progressivo per formatiSezione
						}
					}else{//bug 0004731 esercizio
						if (datiMappaModificati){
							if (daoColl.getCollocazione(recColl.getCodPoloSez(), recColl.getCodBibSez(), recColl.getCodSez(),
									locNormalizzata, specNormalizzata, recColl.getKeyColloc())){
								//la coll già esiste
								if (!confermaEsistenza){
									throw new ValidationException("msgCodLocEsistente",
											ValidationException.errore, "msgCodLocEsistente", (String.valueOf(recColl.getCodColloc())), String.valueOf(recColl.getSpecColloc()));
								}
							}
						}
					}
				}
				collocazione.setCd_loc(recColl.getCodColloc());
				collocazione.setSpec_loc(recColl.getSpecColloc());
				if (recColl.getConsistenza().trim().equals("")){
					collocazione.setConsis("$");
				}else{
					collocazione.setConsis(recColl.getConsistenza().trim());
				}
				collocazione.setTot_inv(recColl.getTotInv());
				if (recColl.getCodDoc().equals(0)){
					collocazione.setCd_biblioteca_doc(null);
				}else{
					collocazione.getCd_biblioteca_doc().getCd_polo().getCd_polo().setCd_polo(recColl.getCodPoloDoc());
					collocazione.getCd_biblioteca_doc().getCd_polo().setCd_biblioteca(recColl.getCodBibDoc());
					collocazione.getCd_biblioteca_doc().getB().setBid(recColl.getBidDoc());
					collocazione.getCd_biblioteca_doc().setCd_doc((recColl.getCodDoc()));
				}
				collocazione.getB().setBid(recColl.getBid());
				collocazione.setIndice(recColl.getIndice());
				collocazione.setTot_inv_prov(recColl.getTotInvProv());
				collocazione.setOrd_loc(locNormalizzata);
				collocazione.setOrd_spec(specNormalizzata);
				collocazione.setTs_var(ts);
				collocazione.setUte_var(recColl.getUteAgg());

				daoInv = new Tbc_inventarioDao();
				if (daoInv.modificaInventario(recColl.getKeyColloc(), recColl.getUteAgg())){
					daoColl.modificaCollocazione(collocazione);
					ret = true;
				}
			}else{
				throw new DataException("collCollocazioneInesistente");
			}

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
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
	 * @throws DataException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getListaCollocazioniReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo,
			String tipoOperazione, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_collocazione> listaCollocazioni = null;
		List lista = new ArrayList();
		CollocazioneTitoloVO recRet = null;
		CollocazioneReticoloVO rec = null;
		int prg = 0;

		try{
			for (int i = 0; i < reticolo.length; i++) {
				recRet = reticolo[i];
				if (codBib.equals("")) {
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioni(recRet.getBid());
				}else{
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioni(codPolo, codBib,	recRet.getBid());
				}
				if (listaCollocazioni.size() > 0){
					for (int y=0; y<listaCollocazioni.size(); y++) { //if (collocazione != null){
						Tbc_collocazione collocazione = listaCollocazioni.get(y);
						rec = new CollocazioneReticoloVO();
						rec.setPrg(prg + (y + 1));
						rec.setCodBib("");
						rec.setBid(collocazione.getB().getBid());
						rec.setCodColloc(collocazione.getCd_loc());
						rec.setSpecColloc(collocazione.getSpec_loc());
						rec.setTotInv(collocazione.getTot_inv());
						if (collocazione.getConsis().equals("$")){
							rec.setConsistenza("");
						}else{
							rec.setConsistenza(collocazione.getConsis());
						}
						rec.setOrdLoc(collocazione.getOrd_loc());
						rec.setOrdSpec(collocazione.getSpec_loc());
						rec.setKeyColloc(collocazione.getKey_loc());
						if (collocazione.getCd_biblioteca_doc() != null){
							rec.setBidDoc(collocazione.getCd_biblioteca_doc().getB().getBid());
							rec.setCodDoc(collocazione.getCd_biblioteca_doc().getCd_doc());
						}else{
							rec.setBidDoc("");
							rec.setCodDoc(0);
						}
						rec.setCodPoloSez(collocazione.getCd_sez().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBibSez(collocazione.getCd_sez().getCd_polo().getCd_biblioteca());
						rec.setCodSez(collocazione.getCd_sez().getCd_sez());
						rec.setDescrBid(collocazione.getB().getIsbd());

//						if (rec!=null){
//						listaTit=(List)this.getTitolo(rec.getBid(), ticket);
//						if (listaTit!=null){
////						recTit = (TitoloVO)listaTit.get(0);
//						if ((TitoloVO)listaTit.get(0)!=null){
////						if (recTit != null){
//						recTit = (TitoloVO)listaTit.get(0);
//						rec.setDescrBid(recTit.getIsbd());
//						}else{
//						rec.setDescrBid("Titolo non trovato in Polo");
//						}
//						}
						lista.add(rec);
					}
					prg = rec.getPrg();
				}else{
//					throw new DataException("NonEsistonoCollocazioniNelReticolo");
				}
			}
			if (lista.size()>0){
				//trovato
			}else{
				throw new DataException(SbnErrorTypes.GDF_COLLOCAZIONI_RETICOLO_NON_TROVATE, "NonEsistonoCollocazioniNelReticolo");
			}
		}catch (DataException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return lista;
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
	public List getListaCollocazioniSezione(EsameCollocRicercaVO paramRicerca, String tipoRichiesta,
			String ticket)
	throws DataException, ValidationException {
		List collocazioni = null;
		List listaCollocazioni = null;
		int numColl = 0;
		int keyLoc = 0;
		String ord = null;
		String ultLoc = null;
		String ultOrdLoc = null;
		String ultSpec = null;
		String ultKeyLoc = null;
		String primoCodLoc = null;
		try{
			valida.validaParamRicercaDaSezioni(paramRicerca);

			if (paramRicerca.getOrdLst().equals("CA")){
				ord="ord_loc asc";
			}else if ( paramRicerca.getOrdLst().equals("CD")){
				ord="ord_loc desc";
			}else{
				ord = "";
			}
			CodiciNormalizzatiVO collSpec = null;
			if (paramRicerca.getTipoRicerca().equals("0") || paramRicerca.getTipoRicerca().equals("1")
					|| paramRicerca.getTipoRicerca().equals("2") || paramRicerca.getTipoRicerca().equals("3")
					|| paramRicerca.getTipoRicerca().equals("4") || paramRicerca.getTipoRicerca().equals("5")){
				if (paramRicerca.getTipoRicerca().equals("0")
						|| paramRicerca.getTipoRicerca().equals("2")
						|| paramRicerca.getTipoRicerca().equals("4")){
					paramRicerca.setUltKeyLoc(0);
				}
				//la ricerca deve ricominciare dall'ultima collocazione / specificazione letta
				if (paramRicerca.getUltLoc() != null && !paramRicerca.getUltLoc().trim().equals("")){
					//solo se ultLoc è valorizzato
					paramRicerca.setCodLoc(paramRicerca.getUltLoc());
					log.info("ultimo codLoc:" + paramRicerca.getUltLoc());

					paramRicerca.setCodSpec(paramRicerca.getUltSpec());
					log.info("ultimo codSpec:" + paramRicerca.getUltSpec());
					log.info("ultimo keyLoc:" + paramRicerca.getUltKeyLoc());
				}
				if (ord != ""){
					if (paramRicerca.getTipoRicerca().equals("1")
							|| paramRicerca.getTipoRicerca().equals("3")
							|| paramRicerca.getTipoRicerca().equals("5")){
						if (ord.equals("ord_loc desc")){
							//controllare in debug se conservo il primissimo codLoc altrimenti devo
							//passare alla query di 1, 5, 7 getListaCollocazioni anche PrimoCodLoc
							//
							//						paramRicerca.setPrimoCodLoc(paramRicerca.getCodLoc());
							//						log.info("primo codLoc:" + paramRicerca.getPrimoCodLoc());
//							paramRicerca.setUltOrdLoc(paramRicerca.getUltLoc());
							log.info("ultimo ordLoc:" + paramRicerca.getUltLoc());
							daoSez = new Tbc_sezione_collocazioneDao();
							Tbc_sezione_collocazione sezione = daoSez.getSezione(paramRicerca.getCodPolo(), paramRicerca.getCodBib(), paramRicerca.getCodSez());
							if (sezione == null){
								throw new DataException("recSezioneInesistente");
							}
							collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), paramRicerca.getPrimoCodLoc(), paramRicerca.getUltLoc(), paramRicerca.isEsattoColl(),
									paramRicerca.getCodSpec(), paramRicerca.getASpec(), paramRicerca.isEsattoSpec(), paramRicerca.getOrdLst());
							primoCodLoc = collSpec.getDaColl();
//							ultOrdLoc = collSpec.getAColl();
							ultOrdLoc = paramRicerca.getUltOrdLoc();

						}else{
							daoSez = new Tbc_sezione_collocazioneDao();
							Tbc_sezione_collocazione sezione = daoSez.getSezione(paramRicerca.getCodPolo(), paramRicerca.getCodBib(), paramRicerca.getCodSez());
							if (sezione == null){
								throw new DataException("recSezioneInesistente");
							}
							collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), paramRicerca.getCodLoc(), paramRicerca.getALoc(), paramRicerca.isEsattoColl(),
									paramRicerca.getCodSpec(), paramRicerca.getASpec(), paramRicerca.isEsattoSpec(), paramRicerca.getOrdLst());
							//
							paramRicerca.setPrimoCodLoc(paramRicerca.getCodLoc());
							log.info("primo codLoc:" + paramRicerca.getPrimoCodLoc());
						}
					}else{

						//almaviva2 agosto 2009
						//				CodiciNormalizzatiVO collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(paramRicerca.getCodLoc(), null, paramRicerca.isEsattoColl(),
						//						paramRicerca.getCodSpec(), null, paramRicerca.isEsattoSpec());
						//
						daoSez = new Tbc_sezione_collocazioneDao();
						Tbc_sezione_collocazione sezione = daoSez.getSezione(paramRicerca.getCodPolo(), paramRicerca.getCodBib(), paramRicerca.getCodSez());
						if (sezione == null){
							throw new DataException("recSezioneInesistente");
						}
						collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), paramRicerca.getCodLoc(), paramRicerca.getALoc(), paramRicerca.isEsattoColl(),
								paramRicerca.getCodSpec(), paramRicerca.getASpec(), paramRicerca.isEsattoSpec(), paramRicerca.getOrdLst());
						//
						paramRicerca.setPrimoCodLoc(paramRicerca.getCodLoc());
						log.info("primo codLoc:" + paramRicerca.getPrimoCodLoc());
						//				paramRicerca.setUltOrdLoc(collSpec.getDaColl());
						//				log.info("ultimo ordLoc:" + paramRicerca.getUltLoc());
					}
				}
				//
				if(paramRicerca.getTipoRicerca().equals("0")){
					daoColl = new Tbc_collocazioneDao();
					numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
							paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
							collSpec.getDaSpec(), collSpec.getASpec(), 0, null, null, null);
					if (numColl < 1){
						throw new DataException("collNonTrovate");
					}else {
						collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
								collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), 0, null, null);
						if (ValidazioneDati.isFilled(collocazioni) ) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){

									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}
							}
						}else{
							throw new DataException("collNonTrovate");
						}
					}
				}
				if(paramRicerca.getTipoRicerca().equals("1")){
					keyLoc = paramRicerca.getUltKeyLoc();//ultKeyLoc trattata
					daoColl = new Tbc_collocazioneDao();
					if (!ord.equals("")){
						if (ord.equals("ord_loc desc")){
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, ord, primoCodLoc, ultOrdLoc);
						}else{
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null, ultOrdLoc);
						}
					}
					if (numColl < 1){
						throw new DataException("collNonTrovate");
					}else {
						if (!ord.equals("")){
							if (ord.equals("ord_loc desc")){
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, primoCodLoc, ultOrdLoc);
							}else{
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, null, null);
							}
						}
						if (ValidazioneDati.isFilled(collocazioni)) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
									//ultOrdLoc = recResult.getOrd_loc();
									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}else{
									ultLoc = null;
									ultOrdLoc = null;
									ultSpec = null;
									ultKeyLoc = null;
								}
							}
						}else{
							throw new DataException("collNonTrovate");
						}
					}
				}
				if(paramRicerca.getTipoRicerca().equals("2")) {
					paramRicerca.setUltLoc("");
					paramRicerca.setUltOrdLoc("");
					daoColl = new Tbc_collocazioneDao();
					numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
							paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
							collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null, null);
					if (numColl < 1){
						throw new DataException("collNonTrovate");
					}else{
						collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
								collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, null, null);
						if (ValidazioneDati.isFilled(collocazioni)) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
									//								paramRicerca.setUltSpec(recResult.getOrd_spec());
									//								paramRicerca.setUltSpec(recResult.getOrd_loc());
									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}
							}
						}else{
							throw new DataException("collNonTrovate");
						}
					}
				}
				if(paramRicerca.getTipoRicerca().equals("3")) {
					/*
					keyLoc = paramRicerca.getUltKeyLoc();//ultKeyLoc trattata
					daoColl = new Tbc_collocazioneDao();
					if (!ord.equals("")){
						if (ord.equals("ord_loc desc")){
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, ord, primoCodLoc);
						}else{
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null);
						}
					}
					 */
					keyLoc = paramRicerca.getUltKeyLoc();//ultKeyLoc trattata
					daoColl = new Tbc_collocazioneDao();
					if (!ord.equals("")){
						if (ord.equals("ord_loc desc")){
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, ord, primoCodLoc, ultOrdLoc);
						}else{
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null, null);
						}
					}
					if (numColl < 1){
						throw new DataException("collNonTrovate");
					}else {
						if (!ord.equals("")){
							if (ord.equals("ord_loc desc")){
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, primoCodLoc, ultOrdLoc);
							}else{
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, null, null);
							}
						}
						if (ValidazioneDati.isFilled(collocazioni)) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
									//									ultOrdLoc = recResult.getOrd_loc();
									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}else{
									ultLoc = null;
									ultOrdLoc = null;
									ultSpec = null;
									ultKeyLoc = null;
								}
							}
						}else{
							throw new DataException("collNonTrovate");
						}
					}
				}
				if(paramRicerca.getTipoRicerca().equals("4")) {
					paramRicerca.setUltLoc("");
					paramRicerca.setUltOrdLoc("");
					daoColl = new Tbc_collocazioneDao();
					numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
							paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
							collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null, null);
					if (numColl < 1) {
						throw new DataException("collNonTrovate");
					}else{

						collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
								collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, null, null);
						if (ValidazioneDati.isFilled(collocazioni)) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}
							}
						}
					}
				}
				if(paramRicerca.getTipoRicerca().equals("5")) {
					/*
					keyLoc = paramRicerca.getUltKeyLoc();//ultKeyLoc trattata
					daoColl = new Tbc_collocazioneDao();
					if (!ord.equals("")){
						if (ord.equals("ord_loc desc")){
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, ord, primoCodLoc);
						}else{
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null);
						}
					}
					 */
					keyLoc = paramRicerca.getUltKeyLoc();//ultKeyLoc trattata
					daoColl = new Tbc_collocazioneDao();
					if (!ord.equals("")){
						if (ord.equals("ord_loc desc")){
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, ord, primoCodLoc, ultOrdLoc);
						}else{
							numColl = daoColl.getCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
									collSpec.getDaSpec(), collSpec.getASpec(), keyLoc, null, null, null);
						}
					}
					if (numColl < 1){
						throw new DataException("collNonTrovate");
					}else {
						if (!ord.equals("")){
							if (ord.equals("ord_loc desc")){
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, primoCodLoc, ultOrdLoc);
							}else{
								collocazioni = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
										paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(),  collSpec.getAColl(),
										collSpec.getDaSpec(), collSpec.getASpec(), ord, paramRicerca.getNuovoLimiteRicerca(), keyLoc, null, null);
							}
						}
						if (ValidazioneDati.isFilled(collocazioni)) {
							int size = collocazioni.size();
							listaCollocazioni = new ArrayList(size);
							for (int index = 0; index < size; index++) {
								Tbc_collocazione recResult = (Tbc_collocazione)collocazioni.get(index);
								listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
								//impostazione di ultKeyLoc
								if (size == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
									//									ultOrdLoc = recResult.getOrd_loc();
									ultLoc = recResult.getCd_loc();
									log.info("ultimo codLoc:" + ultLoc);
									ultOrdLoc = recResult.getOrd_loc();
									log.info("ultimo ordLoc:" + ultOrdLoc);
									ultSpec = recResult.getSpec_loc();
									log.info("ultimo specLoc:" + ultSpec);
									ultKeyLoc = String.valueOf(recResult.getKey_loc());
									log.info("ultimo keyLoc:" + ultKeyLoc);
								}else{
									ultLoc = null;
									ultOrdLoc = null;
									ultSpec = null;
									ultKeyLoc = null;
								}
							}
						}else{
							throw new DataException("collNonTrovate");
						}
					}
				}
			}else if (paramRicerca.getTipoRicerca().equals("6") || paramRicerca.getTipoRicerca().equals("7")
					|| paramRicerca.getTipoRicerca().equals("8") || paramRicerca.getTipoRicerca().equals("9")){
				//tipoRichiesta = 6 o 8 (6 lente collocazioni, 8 lente specificazioni)
				//tipoRichiesta = 7 o 9 (7 segue lente collocazioni, 9 segue lente specificazioni)
				if (paramRicerca.getTipoRicerca().equals("6")
						|| paramRicerca.getTipoRicerca().equals("8")){
					if (paramRicerca.getTipoRicerca().equals("8")){
						if (paramRicerca.getALoc() == null){
							paramRicerca.setALoc(paramRicerca.getCodLoc());
							log.info("alla collocazione aLoc:" + paramRicerca.getALoc());
							paramRicerca.setASpec(paramRicerca.getCodSpec());
							log.info("alla specificazione aSpec:" + paramRicerca.getASpec());
						}
					}else{
						//almaviva2 agosto 2009
						//paramRicerca.setALoc("");
						paramRicerca.setALoc(paramRicerca.getALoc()+"");
						log.info("alla collocazione aLoc:" + paramRicerca.getALoc());
					}
				}
				if (paramRicerca.getTipoRicerca().equals("7")
						|| paramRicerca.getTipoRicerca().equals("9")){
					//la ricerca deve ricominciare dall'ultima collocazione / specificazione letta
					paramRicerca.setCodLoc(paramRicerca.getUltLoc());
					log.info("ultimo codLoc:" + paramRicerca.getUltLoc());
					paramRicerca.setUltOrdLoc(paramRicerca.getUltOrdLoc());
					log.info("ultimo ultOrdLoc:" + paramRicerca.getUltOrdLoc());
					paramRicerca.setCodSpec(paramRicerca.getUltSpec());
					log.info("ultimo codSpec:" + paramRicerca.getUltSpec());
				}
				if (paramRicerca.getTipoRicerca().equals("8") || paramRicerca.getTipoRicerca().equals("9")){
					paramRicerca.setEsattoColl(true);
				}
				daoSez = new Tbc_sezione_collocazioneDao();
				Tbc_sezione_collocazione sezione = daoSez.getSezione(paramRicerca.getCodPolo(), paramRicerca.getCodBib(), paramRicerca.getCodSez());
				collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), paramRicerca.getCodLoc(), paramRicerca.getALoc(), paramRicerca.isEsattoColl(),
						paramRicerca.getCodSpec(), paramRicerca.getASpec(), paramRicerca.isEsattoSpec(), paramRicerca.getOrdLst());
				if (collSpec != null){
					log.info("ultima loc normalizzata:" + collSpec.getDaColl());
					if(paramRicerca.getTipoRicerca().equals("6")) {
						numColl = daoColl.countGroupCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(), collSpec.getAColl());
						if (numColl < 1) {
							throw new DataException("collNonTrovate");
						}else {
							if (paramRicerca.getUltLoc() != null){
								collSpec.setDaColl(paramRicerca.getUltLoc().trim());
								collSpec.setAColl(paramRicerca.getUltOrdLoc().trim());
							}
							//lista Collocazioni da lente
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase().trim(), collSpec.getDaColl(), collSpec.getAColl(), paramRicerca.getNuovoLimiteRicerca());
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaColl", index);
									listaCollocazioni.add(rec);
									//impostazione di ultKeyLoc
									if (listaLente.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
										ultLoc = rec.getCodColloc();
										ultOrdLoc = rec.getOrdLoc();
										ultSpec = rec.getSpecColloc();
										ultKeyLoc = String.valueOf(rec.getKeyColloc());
									}
								}
							}else{
								throw new DataException("collNonTrovate");
							}
						}
					} else	if(paramRicerca.getTipoRicerca().equals("7")){
						keyLoc = paramRicerca.getUltKeyLoc();
						daoColl = new Tbc_collocazioneDao();
						numColl = daoColl.countGroupCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(), collSpec.getAColl(), true);
						if (numColl < 1) {
							throw new DataException("collNonTrovate");
						}else {
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase().trim(), collSpec.getDaColl(), collSpec.getAColl(),
									paramRicerca.getNuovoLimiteRicerca(), true);
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaColl", index);
									listaCollocazioni.add(rec);
									if (listaLente.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
										ultLoc = rec.getCodColloc();
										ultOrdLoc = rec.getOrdLoc();
										ultSpec = rec.getSpecColloc();
										ultKeyLoc = String.valueOf(rec.getKeyColloc());
									}else{
										ultLoc = null;
										ultOrdLoc = null;
										ultSpec = null;
										ultKeyLoc = null;
									}
								}
							}else{
								throw new DataException("collNonTrovate");
							}
						}
					} else if(paramRicerca.getTipoRicerca().equals("8")){
						numColl = daoColl.countGroupSpecificazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(), collSpec.getDaSpec(), collSpec.getASpec());
						if (numColl < 1){
							throw new DataException("collNonTrovate");
						}else {
							if (paramRicerca.getUltLoc() != null){
								collSpec.setDaColl(paramRicerca.getUltLoc().trim());
								collSpec.setAColl(paramRicerca.getUltOrdLoc().trim());
							}
							//lista specificazioni da lente
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaSpecificazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(), collSpec.getDaSpec(), collSpec.getASpec(),  paramRicerca.getNuovoLimiteRicerca());
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaSpec", index);
									listaCollocazioni.add(rec);
									if (listaLente.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
										ultLoc = rec.getCodColloc();
										ultOrdLoc = rec.getOrdLoc();
										ultSpec = rec.getSpecColloc();
										ultKeyLoc = String.valueOf(rec.getKeyColloc());
									}
								}
							}
						}
					} else	if(paramRicerca.getTipoRicerca().equals("9")){
						keyLoc = paramRicerca.getUltKeyLoc();
						daoColl = new Tbc_collocazioneDao();
						numColl = daoColl.countGroupSpecificazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
								paramRicerca.getCodSez().toUpperCase(), collSpec.getDaColl(), collSpec.getDaSpec(), collSpec.getASpec(), true);
						if (numColl < 1){
							throw new DataException("collNonTrovate");
						}else {
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaSpecificazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase().trim(), collSpec.getDaColl(), collSpec.getDaSpec(), collSpec.getASpec(),
									paramRicerca.getNuovoLimiteRicerca(), true);
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaSpec", index);
									listaCollocazioni.add(rec);
									if (listaLente.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
										ultLoc = rec.getCodColloc();
										ultOrdLoc = rec.getOrdLoc();
										ultSpec = rec.getSpecColloc();
										ultKeyLoc = String.valueOf(rec.getKeyColloc());
									}else{
										ultLoc = null;
										ultOrdLoc = null;
										ultSpec = null;
										ultKeyLoc = null;
									}
								}
							}else{
								throw new DataException("collNonTrovate");
							}
						}
					}
				}
			}else{
				if(paramRicerca.getTipoRicerca().equals("10") || paramRicerca.getTipoRicerca().equals("11")){
					daoSez = new Tbc_sezione_collocazioneDao();
					Tbc_sezione_collocazione sezione = daoSez.getSezione(paramRicerca.getCodPolo(), paramRicerca.getCodBib(), paramRicerca.getCodSez());
					collSpec = NormalizzaRangeCollocazioni.normalizzaCollSpec(Character.toString(sezione.getCd_colloc()), paramRicerca.getCodLoc(), paramRicerca.getALoc(), paramRicerca.isEsattoColl(),
							paramRicerca.getCodSpec(), paramRicerca.getCodSpec(), paramRicerca.isEsattoSpec(), paramRicerca.getOrdLst());
					if (collSpec != null){
						if(paramRicerca.getTipoRicerca().equals("10")){
							//lista ult coll
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaUltCollocazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase().trim(), collSpec.getDaColl(), collSpec.getAColl());
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaUltColl", index);
									listaCollocazioni.add(rec);
									//impostazione di ultKeyLoc
									if (listaLente.size() == DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC && (index == DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC - 1)){
										paramRicerca.setUltLoc(rec.getCodColloc());//controllare queste impostazioni
									}
								}
							}else{
								throw new DataException("collNonTrovate");
							}
						} else	if(paramRicerca.getTipoRicerca().equals("11")){
							if (collSpec.getDaColl().trim().equals("")){
								collSpec.setAColl(collSpec.getDaColl());
							}
							//lista ult coll
							daoColl = new Tbc_collocazioneDao();
							List listaLente = daoColl.getListaUltSpecificazioni(paramRicerca.getCodPoloSez(), paramRicerca.getCodBibSez(),
									paramRicerca.getCodSez().toUpperCase().trim(), collSpec.getDaColl(), collSpec.getDaColl());
							if (listaLente.size() > 0){
								listaCollocazioni = new ArrayList();
								for (int index = 0; index < listaLente.size(); index++) {
									CollocazioniUltCollSpecVO rec = this.creaElementoListaUltCollSpec((Object[]) listaLente.get(index), "listaUltSpec", index);
									listaCollocazioni.add(rec);
									//impostazione di ultKeyLoc
									if (listaLente.size() == DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC && (index == DocumentoFisicoCostant.NUM_REC_ULTCOLLSPEC - 1)){
										paramRicerca.setUltLoc(rec.getCodColloc());//controllare queste impostazioni
									}
								}
							}else{
								throw new DataException("collocazioneNonPresenteNellaSezione");
							}
						}
					}
				}else	if(tipoRichiesta.equals("12")){
					//lista ult coll
					daoColl = new Tbc_collocazioneDao();
					List listaLente = daoColl.getListaCollocazioniDiEsemplare(paramRicerca.getCodPoloDoc(), paramRicerca.getCodBibDoc(), paramRicerca.getBidDoc(), paramRicerca.getCodDoc());
					if (listaLente.size() > 0){
						listaCollocazioni = new ArrayList();
						for (int index = 0; index < listaLente.size(); index++) {
							Tbc_collocazione recResult = (Tbc_collocazione)listaLente.get(index);
							listaCollocazioni.add(new CollocazioneUltKeyLocVO(recResult.getKey_loc(), index));
							//impostazione di ultKeyLoc
							if (listaLente.size() == paramRicerca.getNuovoLimiteRicerca() && index == paramRicerca.getNuovoLimiteRicerca() - 1){
								paramRicerca.setUltLoc(recResult.getOrd_loc());
							}
						}
					}else{
						throw new DataException("collNonTrovate");
					}
				}
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}

		if (listaCollocazioni != null){
			if (numColl > paramRicerca.getNuovoLimiteRicerca()){
				CodiceVO estremiListaCollIncompleta = new CodiceVO();
//				if (paramRicerca.getTipoRicerca().equals("0")){//aggiungere altre
					estremiListaCollIncompleta.setNumero(numColl);
//				}
				estremiListaCollIncompleta.setCodice(ultLoc);
				estremiListaCollIncompleta.setTerzo(ultSpec);
				estremiListaCollIncompleta.setQuarto(ultOrdLoc);
				estremiListaCollIncompleta.setDescrizione(ultKeyLoc);
				listaCollocazioni.add(estremiListaCollIncompleta);
//
//				throw new ValidationException("collListaIncompleta", ValidationException.errore, "msgS", listaCollocazioni);
			}else{
				return listaCollocazioni;
			}
		}
		return listaCollocazioni;

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
	public EsemplareDettaglioVO getEsemplareDettaglio(String codPolo, String codBib, String bid, int codDoc, String ticket)
	throws DataException, ApplicationException, ValidationException {

		EsemplareDettaglioVO rec =null;

		try {
			EsemplareVO esempl = new EsemplareVO();
			esempl.setCodPolo(codPolo);
			esempl.setCodBib(codBib);
			esempl.setBid(bid);
			esempl.setCodDoc(codDoc);
			valida.validaEsemplare(esempl);
			daoEsempl = new Tbc_esemplare_titoloDao();
			Tbc_esemplare_titolo esemplare = daoEsempl.getEsemplare(codPolo, codBib, bid, codDoc);
			if (esemplare!=null){
					rec = new EsemplareDettaglioVO();
					rec.setCodPolo(esemplare.getCd_polo().getCd_polo().getCd_polo());
					rec.setCodBib(esemplare.getCd_polo().getCd_biblioteca());
					if (esemplare.getB() != null){
						rec.setBid(esemplare.getB().getBid());
						List titoli = null;
						try {
							titoli = dfCommon.getTitolo(esemplare.getB().getBid(), ticket);
						} catch (RemoteException e) {


						}
						if (titoli != null){
							if (titoli.size() > 0 && titoli.size() == 1){
								TitoloVO titolo = (TitoloVO)titoli.get(0);
								rec.setBidDescr(titolo.getIsbd());
							}
						}else{
							rec.setBidDescr("Titolo non trovato in Polo");
						}
					}else{
						rec.setBidDescr("Titolo non trovato in Polo");
					}

//					if (esemplare.getTb_titolo().getIsbd() != null){
//						rec.setBidDescr(esemplare.getTb_titolo().getIsbd());
//					}else{
//						rec.setBidDescr("titolo doc non trovato sul db locale");
//					}
					rec.setCodDoc(esemplare.getCd_doc());
					if (esemplare.getCons_doc().equals("$")){
						rec.setConsDoc("");
					}else{
						rec.setConsDoc(esemplare.getCons_doc());
					}
					rec.setUteIns(esemplare.getUte_ins());
					rec.setDataIns(String.valueOf(esemplare.getTs_ins()));
					rec.setUteAgg(esemplare.getUte_var());
					rec.setDataAgg(String.valueOf(esemplare.getTs_var()));
					rec.setCancDb2i(String.valueOf(esemplare.getFl_canc()));
			}else{
				throw new DataException("NonEsistente");
			}
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return rec;
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
	public List getListaEsemplariReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_esemplare_titolo> listaEsemplari = null;
		List lista = new ArrayList();
		CollocazioneTitoloVO recRet = null;
		EsemplareListaVO rec = null;
		int prg = 0;

		try{
			for (int i = 0; i < reticolo.length; i++) {
				recRet = reticolo[i];
				if (codBib.equals("")) {
					daoEsempl = new Tbc_esemplare_titoloDao();
					listaEsemplari = daoEsempl.getListaEsemplari(recRet.getBid());
				}else{
					daoEsempl = new Tbc_esemplare_titoloDao();
					listaEsemplari = daoEsempl.getListaEsemplari(codPolo, codBib, recRet.getBid());
				}
				if (listaEsemplari.size() > 0){
					for (int y=0; y<listaEsemplari.size(); y++) { //if (collocazione != null){
						Tbc_esemplare_titolo esemplare = listaEsemplari.get(y);
						rec = new EsemplareListaVO();
						rec.setPrg(prg + (y + 1));
						rec.setCodPolo(esemplare.getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBib(esemplare.getCd_polo().getCd_biblioteca());
						if (esemplare.getB() != null){
							rec.setBid(esemplare.getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitolo(esemplare.getB().getBid(), ticket);
							} catch (RemoteException e) {


							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setBidDescr(titolo.getIsbd());
								}
							}else{
								rec.setBidDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setBidDescr("Titolo non trovato in Polo");
						}
//						rec.setBid(esemplare.getB().getBid());
//						rec.setBidDescr(esemplare.getB().getIsbd());
						rec.setCodDoc(esemplare.getCd_doc());
						if (esemplare.getCons_doc().equals("$")){
							rec.setConsDoc("");
						}else{
							rec.setConsDoc(esemplare.getCons_doc());
						}
						lista.add(rec);
					}
					prg = rec.getPrg();
				}
			}
			if (lista.size()>0){

			}
//			}else{
//				throw new DataException("esemplNonEsistente");
//			}
//		}catch (ValidationException e) {
//			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return lista;
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
	public List getListaCollocazioniDiEsemplare(String codPoloSez, String codBibSez, String codSez,
			String codPoloDoc, String codBibDoc, String bidDoc, int codDoc,
			String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List listaCollocazioniDiEsemplare = null;
		List lista = new ArrayList();
		CollocazioneUltKeyLocVO rec = null;

		try{
			CollocazioneVO coll = new CollocazioneVO();
			coll.setCodPolo(codPoloSez);
			coll.setCodBib(codBibSez);
			coll.setCodPoloSez(codPoloSez);
			coll.setCodBibSez(codBibSez);
			coll.setCodPoloDoc(codPoloDoc);
			coll.setCodBibDoc(codBibDoc);
			coll.setBidDoc(bidDoc);
			coll.setCodDoc(codDoc);
			valida.validaCollocazionePerListaCollocazioniDiEsemplare(coll);
			daoColl = new Tbc_collocazioneDao();
			listaCollocazioniDiEsemplare = daoColl.getListaCollocazioniDiEsemplare(codPoloDoc, codBibDoc, bidDoc, codDoc);
				if (listaCollocazioniDiEsemplare.size() > 0){
					for (int i=0; i<listaCollocazioniDiEsemplare.size(); i++) { //if (collocazione != null){
						Tbc_collocazione collocazione = (Tbc_collocazione) listaCollocazioniDiEsemplare.get(i);
						rec = new CollocazioneUltKeyLocVO();
						rec.setKeyColloc(collocazione.getKey_loc());
						rec.setPrg(i + 1);
						rec.setCodPolo(collocazione.getCd_sez().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBib(collocazione.getCd_sez().getCd_polo().getCd_biblioteca());
						rec.setCodSez(collocazione.getCd_sez().getCd_sez());
						rec.setBid(collocazione.getB().getBid());
						rec.setTitolo(collocazione.getB().getIsbd());
						if (collocazione.getCd_biblioteca_doc() != null){
							rec.setFlgEsemplare("E");
						}
						rec.setCodPoloDoc(collocazione.getCd_biblioteca_doc().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBibDoc(collocazione.getCd_biblioteca_doc().getCd_polo().getCd_biblioteca());
						if (collocazione.getCd_biblioteca_doc().getB() != null){
							rec.setBidDoc(collocazione.getCd_biblioteca_doc().getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitolo(collocazione.getCd_biblioteca_doc().getB().getBid(), ticket);
							} catch (RemoteException e) {


							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setTitoloDoc(titolo.getIsbd());
								}
							}else{
								rec.setTitoloDoc("Titolo non trovato in Polo");
							}
						}else{
							rec.setTitoloDoc("Titolo non trovato in Polo");
						}
//						rec.setBidDoc(collocazione.getCd_biblioteca_doc().getB().getBid());
//						rec.setTitoloDoc(collocazione.getCd_biblioteca_doc().getB().getIsbd());
						rec.setCodDoc(collocazione.getCd_biblioteca_doc().getCd_doc());
						rec.setCodColloc(collocazione.getCd_loc());
						rec.setSpecColloc(collocazione.getSpec_loc());
						rec.setTotInv(collocazione.getCd_biblioteca_doc().getCd_doc());
						if (collocazione.getConsis().equals("$")){
							rec.setConsistenza("");
						}else{
							rec.setConsistenza(collocazione.getConsis());
						}
						lista.add(rec);
					}
				}
			if (lista.size()>0){

			}
//			}else{
//				throw new DataException("esemplNonEsistente");
//			}
//		}catch (ValidationException e) {
//			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return lista;
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
	public boolean insertEsemplare(EsemplareVO recEsempl, int keyLoc, String ticket)
	throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		int codDoc = 0;
		Timestamp ts = DaoManager.now();
		try {
			valida.validaEsemplare(recEsempl);
			daoEsempl = new Tbc_esemplare_titoloDao();
			Integer esemplareCodDocMax = daoEsempl.getMaxEsemplare(recEsempl.getCodPolo(), recEsempl.getCodBib(),
					recEsempl.getBid());
			Tbc_esemplare_titolo esemplare = new Tbc_esemplare_titolo();
			if (esemplareCodDocMax == null){
				codDoc++;
			}else{
				codDoc = esemplareCodDocMax;
				codDoc++;
			}

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(recEsempl.getCodPolo());
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(recEsempl.getCodBib());
			bib.setCd_polo(polo);
			esemplare.setCd_polo(bib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(recEsempl.getBid());
			esemplare.setB(titolo);
			esemplare.setCd_doc(codDoc);
			if (recEsempl.getConsDoc().trim().equals("")){
				esemplare.setCons_doc("$");
			}else{
				esemplare.setCons_doc(recEsempl.getConsDoc().trim());
			}
			esemplare.setTs_ins(ts);
			esemplare.setTs_var(ts);
			esemplare.setUte_ins(recEsempl.getUteAgg());
			esemplare.setUte_var(recEsempl.getUteAgg());
			esemplare.setFl_canc('N');
			if (keyLoc > 0){
				Tbc_collocazione collocazione = this.aggiornaColloc(keyLoc, esemplare, null);
				if (collocazione != null){
					esemplare.getTbc_collocazione().add(collocazione);
				}
			}

			daoEsempl = new Tbc_esemplare_titoloDao();
			ret = daoEsempl.inserimentoEsemplare(esemplare);
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return ret;
	}

	private Tbc_collocazione aggiornaColloc(int keyLoc, Tbc_esemplare_titolo recEsempl, String tipoOper)
	throws DataException, ApplicationException, ValidationException {

		Timestamp ts = DaoManager.now();
		Tbc_collocazione collocazione = null;
		try {
			daoColl = new Tbc_collocazioneDao();
			collocazione = daoColl.getCollocazione(keyLoc);
			if (collocazione != null){
				if(collocazione.getCd_biblioteca_doc() != null){
					if ((collocazione.getCd_biblioteca_doc().getCd_doc() != (recEsempl.getCd_doc())) ||
							(collocazione.getCd_biblioteca_doc().getCd_polo().getCd_polo().getCd_polo() != recEsempl.getCd_polo().getCd_polo().getCd_polo()) ||
									(collocazione.getCd_biblioteca_doc().getCd_polo().getCd_biblioteca() != recEsempl.getCd_polo().getCd_biblioteca())){
						//si tratta di spostamento di esemplare: controlla se può cancellare il vecchio esemplare
						daoColl = new Tbc_collocazioneDao();
						List collocazioni = daoColl.getListaCollocazioni(
								collocazione.getCd_biblioteca_doc().getCd_polo().getCd_polo().getCd_polo(),
								collocazione.getCd_biblioteca_doc().getCd_polo().getCd_biblioteca(),
								collocazione.getCd_biblioteca_doc().getB().getBid(),
								collocazione.getCd_biblioteca_doc().getCd_doc(),
								collocazione.getKey_loc());
						if (collocazioni.size() == 0){
//							recEsempl.setTs_var(ts);
//							recEsempl.setFl_canc('S');
							collocazione.getCd_biblioteca_doc().setTs_var(ts);
							collocazione.getCd_biblioteca_doc().setFl_canc('S');
						}
					}
				}
				if (tipoOper != null && tipoOper == DocumentoFisicoCostant.C_CANCELLA_ESEMPL){
//					if (tipoOper == DocumentoFisicoCostant.C_CANCELLA_ESEMPL){
					collocazione.setCd_biblioteca_doc(null);
				}else{
					collocazione.setCd_biblioteca_doc(recEsempl);
				}
				collocazione.setTs_var(ts);
				if (daoColl.modificaCollocazione(collocazione)){
					return collocazione;
				}
			}else{
				throw new DataException("collCollocazioneInesistente");
			}
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return collocazione;
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
	public boolean updateEsemplare(EsemplareVO recEsempl, int keyLoc, String tipoOper, String ticket)
	throws DataException, ApplicationException, ValidationException {
		boolean ret = false;
		Timestamp ts = DaoManager.now();
		List collocazioni = null;
			try {
			valida.validaEsemplare(recEsempl);
			daoEsempl = new Tbc_esemplare_titoloDao();
			Tbc_esemplare_titolo esemplare = daoEsempl.getEsemplare(recEsempl.getCodPolo(), recEsempl.getCodBib(),
					recEsempl.getBid(), recEsempl.getCodDoc());
			if (esemplare != null){
				if (!recEsempl.getDataAgg().equals(esemplare.getTs_var().toString())){
					throw new ValidationException("esemplModAltroUt", ValidationExceptionCodici.errore);
				}
			}else{
				throw new DataException("esemplareNonEsistente");
			}
			if (tipoOper != DocumentoFisicoCostant.C_CANCELLA_ESEMPL){
			}else{
				//cancellazione: deve verificare se ci sono collocazioni collegate
				daoColl = new Tbc_collocazioneDao();
				collocazioni = daoColl.getListaCollocazioni(esemplare.getCd_polo().getCd_polo().getCd_polo(),
																		esemplare.getCd_polo().getCd_biblioteca(),
																		esemplare.getB().getBid(),
																		esemplare.getCd_doc(), keyLoc);
				if (collocazioni.size() == 0){
					esemplare.setFl_canc('S');
				}else{
					esemplare.setFl_canc(' ');
				}
//				esemplare.setTs_var(ts);
			}
			if (recEsempl.getConsDoc().trim().equals("")){
				esemplare.setCons_doc("$");
			}else{
				esemplare.setCons_doc(recEsempl.getConsDoc());
			}
			esemplare.setTs_var(ts);
			esemplare.setUte_var(recEsempl.getUteAgg());
//			daoEsempl = new Tbc_esemplare_titoloDao();
//			ret = daoEsempl.modificaEsemplare(esemplare);
			if (keyLoc > 0){
//				this.aggiornaColloc(keyLoc, esemplare, tipoOper);
				Tbc_collocazione collocazione = this.aggiornaColloc(keyLoc, esemplare, tipoOper);
//				if (collocazione != null){
//					esemplare.getTbc_collocazione().add(collocazione);
//				}
			}
//				if (tipoOper.equals("C")){
//					esemplare.setFl_canc('S');
//				}
			if (tipoOper.equals("C")){
				//almaviva2 bug 4450 esercizio
				if (collocazioni.size() == 0){
					esemplare.setFl_canc('S');
				}else{
					esemplare.setFl_canc(' ');
				}
			}
			daoEsempl = new Tbc_esemplare_titoloDao();
			ret = daoEsempl.modificaEsemplare(esemplare);
		}catch (ValidationException e) {
			throw e;
		}catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return ret;
	}


	private CollocazioniUltCollSpecVO creaElementoListaUltCollSpec(Object[] object, String lista, int i) {
		CollocazioniUltCollSpecVO rec = new CollocazioniUltCollSpecVO();
		if (lista != null){
			rec.setPrg(i + 1);
			if (lista.equals("listaSpec")){
				rec.setCodColloc((String) object[3]);
				rec.setSpecColloc((String) object[5]);
				rec.setNumOccorrenze((Integer) object[0]);
				rec.setTotInventari((Integer) object[1]);
			}else if (lista.equals("listaColl")){
				rec.setCodColloc((String) object[2]);
				rec.setNumOccorrenze((Integer) object[0]);
				rec.setTotInventari((Integer) object[1]);
			}else if (lista.equals("listaUltColl")){
				rec.setCodColloc((String) object[2]);
				rec.setNumOccorrenze((Integer) object[0]);
			}else if (lista.equals("listaUltSpec")){
				rec.setSpecColloc((String) object[4]);
				rec.setNumOccorrenze((Integer) object[0]);
			}
		}
		return rec;
	}

	public CollocazioneUltKeyLocVO creaElementoListaCollocazioni(int key_loc, String ticket, int pos)
			throws DataException, ApplicationException, ValidationException {
		Tbc_collocazione c;
		try {
			c = daoColl.getCollocazione(key_loc);

			CollocazioneUltKeyLocVO rec = new CollocazioneUltKeyLocVO();
			rec.setKeyColloc(key_loc);
			rec.setPrg(pos + 1);
			rec.setCodBib(c.getCd_sez().getCd_polo().getCd_biblioteca());
			rec.setCodSez(c.getCd_sez().getCd_sez().trim());
			if (c.getB() != null) {
				rec.setBid(c.getB().getBid());
				List titoli = null;
				try {
					// titoli = dfCommon.getTitolo(recResult.getB().getBid(),
					// ticket);
					titoli = dfCommon.getTitoloHib(c.getB(), ticket);
				} catch (RemoteException e) {


				}
				if (titoli != null) {
					if (titoli.size() > 0 && titoli.size() == 1) {
						TitoloVO titolo = (TitoloVO) titoli.get(0);
						rec.setTitolo(titolo.getIsbd());
					}
				} else {
					rec.setTitolo("Titolo non trovato in Polo");
				}
			} else {
				rec.setTitolo("Titolo non trovato in Polo");
			}
			rec.setCodColloc(c.getCd_loc());
			rec.setSpecColloc(c.getSpec_loc());
			rec.setTotInv(c.getTot_inv());
			rec.setOrdLoc(c.getOrd_loc());
			rec.setOrdSpec(c.getOrd_spec());
			rec.setKeyColloc(c.getKey_loc());
			if (c.getCd_biblioteca_doc() != null) {
				rec.setBidDoc(c.getCd_biblioteca_doc().getB().getBid());
				rec.setCodDoc(c.getCd_biblioteca_doc().getCd_doc());
				rec.setFlgEsemplare("E");
			} else {
				rec.setBidDoc(null);
				rec.setCodDoc(null);
			}
			if (c.getConsis().equals("$")) {
				rec.setConsistenza("");
			} else {
				rec.setConsistenza(c.getConsis());
			}

			return rec;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private void trasformaSezVOHib(SezioneCollocazioneVO sezione, Tbc_sezione_collocazione recSez){

		Timestamp ts = DaoManager.now();

		if (sezione.getNoteSezione().trim().equals("")){
			recSez.setNote_sez("$");
		}else{
			recSez.setNote_sez(sezione.getNoteSezione());
		}
		recSez.setTot_inv(sezione.getInventariCollocati());
		if (sezione.getDescrSezione().trim().equals("")){
			recSez.setDescr("$");
		}else{
			recSez.setDescr(sezione.getDescrSezione());
		}
		recSez.setCd_colloc(sezione.getTipoColloc().charAt(0));
		recSez.setTipo_sez(sezione.getTipoSezione().charAt(0));
		if (sezione.getClassific().trim().equals("")){
			recSez.setCd_cla("$");
		}else{
			recSez.setCd_cla(sezione.getClassific());
		}
		recSez.setTot_inv_max(sezione.getInventariPrevisti());
		recSez.setTs_var(ts);
		recSez.setUte_var(sezione.getUteAgg());
		recSez.setFl_canc('N');
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
	 *
	 */
	public boolean [] getDatiPosseduto(String codPolo, String codBib, String bid, String ticket)
	throws DataException, ApplicationException, ValidationException {
		boolean [] ret = {false, false, false, false, false, false};

		try{
			InventarioVO inv = new InventarioVO();
			inv.setCodPolo(codPolo);
			inv.setCodBib(codBib);
			inv.setBid(bid);
			valida.validaInventario(inv, null, 0);
			//inventari
			daoInv = new Tbc_inventarioDao();
			int countInventariPerPosseduto = daoInv.countInventariPerPosseduto(codPolo, codBib, bid);
			ret[0] = (countInventariPerPosseduto > 0);

			//collocazioni
			daoColl = new Tbc_collocazioneDao();
			int countCollocazioniPerPosseduto = daoColl.countCollocazioniPerPosseduto(codPolo, codBib, bid);
			ret[1] = (countCollocazioniPerPosseduto > 0);

			//esemplare
			daoEsempl = new Tbc_esemplare_titoloDao();
			int countEsemplariPerPosseduto = daoEsempl.countEsemplariPerPosseduto(codPolo, codBib, bid);
			ret[2] = (countEsemplariPerPosseduto > 0);

			// altreBiblioteche
			// inventari
			int countInventariPerPossedutoAltreBib = daoInv.countInventariPerPossedutoAltreBib(codPolo, codBib, bid);
			ret[3] = (countInventariPerPossedutoAltreBib > 0);

			// altre bib del polo
			int countCollocazioniPerPossedutoAltreBib = daoColl.countCollocazioniPerPossedutoAltreBib(codPolo, codBib, bid);
			ret[4] = (countCollocazioniPerPossedutoAltreBib > 0);

			// esemplare
			int countEsemplariPerPossedutoAltreBib = daoEsempl.countEsemplariPerPossedutoAltreBib(codPolo, codBib, bid);
			ret[5] = (countEsemplariPerPossedutoAltreBib > 0);

		}catch (ValidationException e) {
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
	 * @throws DataException
	 * @throws ValidationException
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 *
	 */
	public List getAltreBib(String bid, String codBib, String ticket)
	throws DataException, ApplicationException, ValidationException {
		Set<Tbf_biblioteca> biblioteche = new HashSet<Tbf_biblioteca>();
		List listaAltreBib = new ArrayList();
		CodiceVO rec = null;
		Timestamp ts = DaoManager.now();

		try{
			CodiceVO rec1 = new CodiceVO();
			rec1.setCodice(bid);
			rec1.setDescrizione(codBib);
			valida.validaBidBib(rec1);
			//inventari
			daoInv = new Tbc_inventarioDao();
			List altreBibInv = null;
			if (codBib == null){
				altreBibInv = daoInv.getAltreBibInv(bid);
			}else{
				altreBibInv = daoInv.getAltreBibInv(bid, codBib);
			}
			if (altreBibInv.size() > 0){
				for (int y=0; y<altreBibInv.size(); y++) {
					Tbf_biblioteca bibInv = (Tbf_biblioteca) altreBibInv.get(y);
					biblioteche.add(bibInv);
				}
			}
			//collocazioni
			daoColl = new Tbc_collocazioneDao();
			List altreBibColl = null;
			if (codBib == null){
				altreBibColl = daoColl.getAltreBibColl(bid);
			}else{
				altreBibColl = daoColl.getAltreBibColl(bid, codBib);
			}
			if (altreBibColl.size() > 0){
				for (int y=0; y<altreBibColl.size(); y++) {
					Tbf_biblioteca bibColl = (Tbf_biblioteca) altreBibColl.get(y);
					biblioteche.add(bibColl);
				}
			}
			//esemplare
			daoEsempl = new Tbc_esemplare_titoloDao();
			List altreBibEse = null;
			if (codBib == null){
				altreBibEse = daoEsempl.getAltreBibEsempl(bid);
			}else{
				altreBibEse = daoEsempl.getAltreBibEsempl(bid, codBib);
			}
			if (altreBibEse.size() > 0){
				for (int y=0; y<altreBibEse.size(); y++) {
					Tbf_biblioteca bibEse = (Tbf_biblioteca) altreBibEse.get(y);
					biblioteche.add(bibEse);
				}
			}
			if (biblioteche.size() > 0){
				Iterator i = biblioteche.iterator();
				while (i.hasNext()){
					Tbf_biblioteca bib = (Tbf_biblioteca) i.next();
					rec = new CodiceVO();
					rec.setCodice(bib.getCd_bib());
					rec.setDescrizione(bib.getNom_biblioteca());
					listaAltreBib.add(rec);
				}
			}

		}catch (ValidationException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new DataException(e);
		} catch (Exception e) {

			throw new DataException(e);
		}

		Collections.sort(listaAltreBib);
		return listaAltreBib;
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
	 *
	 */
	public List getListaEsemplariDiCollocazione(String codPolo, String codBib, String bid, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_esemplare_titolo> listaEsemplari = null;
		List lista = new ArrayList();
		EsemplareListaVO rec = null;

		try{
				if (codBib.equals("")) {
					daoColl = new Tbc_collocazioneDao();
					listaEsemplari = daoEsempl.getListaEsemplari(bid);
				}else{
					daoColl = new Tbc_collocazioneDao();
					listaEsemplari = daoEsempl.getListaEsemplari(codPolo, codBib, bid);
				}
				if (listaEsemplari != null){
					for (int y=0; y<listaEsemplari.size(); y++) {
						Tbc_esemplare_titolo esemplare = listaEsemplari.get(y);
						rec = new EsemplareListaVO();
						rec.setPrg(y + 1);
						rec.setCodPolo(esemplare.getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBib(esemplare.getCd_polo().getCd_biblioteca());
						if (esemplare.getB() != null){
							rec.setBid(esemplare.getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitolo(esemplare.getB().getBid(), ticket);
							} catch (RemoteException e) {


							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setBidDescr(titolo.getIsbd());
								}
							}else{
								rec.setBidDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setBidDescr("Titolo non trovato in Polo");
						}
//						rec.setBid(esemplare.getB().getBid());
//						rec.setBidDescr(esemplare.getB().getIsbd());
						rec.setCodDoc(esemplare.getCd_doc());
						if (esemplare.getCons_doc().equals("$")){
							rec.setConsDoc("");
						}else{
							rec.setConsDoc(esemplare.getCons_doc());
						}
					}
				}else{
					throw new DataException("NonEsistonoEsemplari");
				}
			if (lista.size()>0){
				//trovato
			}else{
				throw new DataException("NonEsistonoEsemplari");
			}
		}catch (DataException e) {
			throw e;
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
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
	 *
	 */
	public List getListaCollocazioniTitolo(String codPolo, String codBib, String bid, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_collocazione> listaCollocazioni = null;
		List lista = new ArrayList();
		CollocazioneReticoloVO rec = null;

		try{
				if (codBib.equals("")) {
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioniTitolo(bid);
				}else{
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioniTitolo(codPolo, codBib, bid);
				}
				if (listaCollocazioni.size() > 0){
					for (int y=0; y<listaCollocazioni.size(); y++) { //if (collocazione != null){
						Tbc_collocazione collocazione = listaCollocazioni.get(y);
						rec = new CollocazioneReticoloVO();
						rec.setPrg(y + 1);
						rec.setCodBib("");
						rec.setBid(collocazione.getB().getBid());
						rec.setCodColloc(collocazione.getCd_loc());
						rec.setSpecColloc(collocazione.getSpec_loc());
						rec.setTotInv(collocazione.getTot_inv());
						if (collocazione.getConsis().equals("$")){
							rec.setConsistenza("");
						}else{
							rec.setConsistenza(collocazione.getConsis());
						}
						rec.setOrdLoc(collocazione.getOrd_loc());
						rec.setOrdSpec(collocazione.getSpec_loc());
						rec.setKeyColloc(collocazione.getKey_loc());
						if (collocazione.getCd_biblioteca_doc() != null){
							rec.setBidDoc(collocazione.getCd_biblioteca_doc().getB().getBid());
							rec.setCodDoc(collocazione.getCd_biblioteca_doc().getCd_doc());
							rec.setFlgEsemplare("E");
						}else{
							rec.setBidDoc("");
							rec.setCodDoc(0);
						}
						rec.setCodPoloSez(collocazione.getCd_sez().getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBibSez(collocazione.getCd_sez().getCd_polo().getCd_biblioteca());
						rec.setCodSez(collocazione.getCd_sez().getCd_sez());
						rec.setDescrBid(collocazione.getB().getIsbd());

//						if (rec!=null){
//						listaTit=(List)this.getTitolo(rec.getBid(), ticket);
//						if (listaTit!=null){
////						recTit = (TitoloVO)listaTit.get(0);
//						if ((TitoloVO)listaTit.get(0)!=null){
////						if (recTit != null){
//						recTit = (TitoloVO)listaTit.get(0);
//						rec.setDescrBid(recTit.getIsbd());
//						}else{
//						rec.setDescrBid("Titolo non trovato in Polo");
//						}
//						}
						lista.add(rec);
					}
				}

		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
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
	 *
	 */
	public List getListaCollocazioniTitoloSimple(String codPolo, String codBib, String bid, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_collocazione> listaCollocazioni = null;
		List lista = new ArrayList();
		CollocazioneReticoloVO rec = null;

		try{
				if (codBib.equals("")) {
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioniTitolo(bid);
				}else{
					daoColl = new Tbc_collocazioneDao();
					listaCollocazioni = daoColl.getListaCollocazioniTitolo(codPolo, codBib, bid);
				}
				if (listaCollocazioni.size() > 0){
					for (int y=0; y<listaCollocazioni.size(); y++) { //if (collocazione != null){
						Tbc_collocazione collocazione = listaCollocazioni.get(y);
						lista.add(collocazione);
					}
				}

		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
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
	 *
	 */
	public List getListaEsemplariTitolo(String codPolo, String codBib, String bid, String ticket)
	throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
		List<Tbc_esemplare_titolo> listaEsemplari = null;
		List lista = new ArrayList();
		EsemplareListaVO rec = null;

		try{
				if (codBib.equals("")) {
					daoColl = new Tbc_collocazioneDao();
					listaEsemplari = daoEsempl.getListaEsemplari(bid);
				}else{
					daoColl = new Tbc_collocazioneDao();
					listaEsemplari = daoEsempl.getListaEsemplari(codPolo, codBib,	bid);
				}
				if (listaEsemplari != null){
					for (int y=0; y<listaEsemplari.size(); y++) {
						Tbc_esemplare_titolo esemplare = listaEsemplari.get(y);
						rec = new EsemplareListaVO();
						rec.setPrg(y + 1);
						rec.setCodPolo(esemplare.getCd_polo().getCd_polo().getCd_polo());
						rec.setCodBib(esemplare.getCd_polo().getCd_biblioteca());
						if (esemplare.getB() != null){
							rec.setBid(esemplare.getB().getBid());
							List titoli = null;
							try {
								titoli = dfCommon.getTitolo(esemplare.getB().getBid(), ticket);
							} catch (RemoteException e) {

							}
							if (titoli != null){
								if (titoli.size() > 0 && titoli.size() == 1){
									TitoloVO titolo = (TitoloVO)titoli.get(0);
									rec.setBidDescr(titolo.getIsbd());
								}
							}else{
								rec.setBidDescr("Titolo non trovato in Polo");
							}
						}else{
							rec.setBidDescr("Titolo non trovato in Polo");
						}
//						rec.setBid(esemplare.getB().getBid());
//						rec.setBidDescr(esemplare.getB().getIsbd());
						rec.setCodDoc(esemplare.getCd_doc());
						if (esemplare.getCons_doc().equals("$")){
							rec.setConsDoc("");
						}else{
							rec.setConsDoc(esemplare.getCons_doc());
						}
						lista.add(rec);
					}
				}
		}catch (DaoManagerException e) {

			throw new DataException (e);
		} catch (Exception e) {

			throw new DataException (e);
		}
		return lista;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			String ticket = command.getTicket();
			checkTicket(ticket);

			Serializable[] params = command.getParams();

			switch (command.getCommand() ) {
			case GDF_POSSEDUTO_DOCUMENTO_WS:
				return wsGetPossedutoDocumento(command);

			}

			return null;

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode());
		} catch (Exception e) {
			log.error("", e);
			ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

	private CommandResultVO wsGetPossedutoDocumento(CommandInvokeVO command) throws Exception {
		Serializable[] params = command.getParams();

		String codPolo = (String) params[0];
		String codBib = (String) params[1];
		String bid = (String) params[2];
		boolean withDisponibilita = (Boolean) params[3];
		boolean withFascicoli = (Boolean) params[4];
		Timestamp now = DaoManager.now();
		String ticket = command.getTicket();

		TitoloDAO tdao = new TitoloDAO();
		Tb_titolo t = tdao.getTitoloLazy(bid);
		if (t == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		boolean isCollana = (t.getCd_natura() == 'C');

		SbnwebType sbn = new SbnwebType();
		DocumentoType doc = new DocumentoType();
		doc.setBid(bid);
		doc.setIsbd(t.getIsbd());
		sbn.setDocumento(doc);

		Tbc_collocazioneDao cdao = new Tbc_collocazioneDao();
		List<InventarioPossedutoVO> inventariPosseduti = cdao.getInventariPosseduti(codPolo, codBib, bid);

		PossedutoType p = new PossedutoType();
		EsitoType esito = new EsitoType();
		esito.setReturnCode(0);
		esito.setMessage("OK");
		esito.setNumeroRighe(ValidazioneDati.size(inventariPosseduti));
		sbn.setEsito(esito);

		NonCollocatoType nonCollocati = new NonCollocatoType();

		Map<String, List<InventarioPossedutoVO>> segnature = Stream.of(inventariPosseduti).collect(Collectors.groupingBy(DocFisico.Inventari.groupBySegnatura()));
		Iterator<String> keys = new TreeSet<String>(segnature.keySet()).iterator();
		while (keys.hasNext() ) {
			List<InventarioPossedutoVO> grp = segnature.get(keys.next());
			InventarioPossedutoVO ip = grp.get(0);
			CollocazioneType ct = null;
			if (ip.isCollocato() ) {
				ct = ConversioneHibernateVO.toXML().collocazione(ip);

				//se il titolo di coll è diverso da quello della richiesta va riportato nella risposta
				String bidColl = ip.getBid_coll();
				if (!ValidazioneDati.equals(bid, bidColl)) {
					Tb_titolo tcoll = tdao.getTitoloLazy(bidColl);
					if (tcoll == null)
						throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

					DocumentoType docColl = new DocumentoType();
					docColl.setBid(bidColl);
					docColl.setIsbd(tcoll.getIsbd());
					ct.setDocumento(docColl);
				}
			}

			if (!isCollana) {	//le collane non riportano gli inventari
				Tb_titolo tinv = t;	//titolo inventario come titolo collocazione
				for (InventarioPossedutoVO inv : grp ) {
					InventarioType it = ConversioneHibernateVO.toXML().inventarioPosseduto(inv);
					if (inv.hasPossessori()) {
						//recupero legami poss/prov
						Trc_poss_prov_inventariDao ppDao = new Trc_poss_prov_inventariDao();
						List<Object[]> possessori = ppDao.getListaPossessoriDiInventario(codPolo, codBib, inv.getCd_serie(), inv.getCd_inven());
						for (Object[] o : possessori) {
							Trc_poss_prov_inventari ppi = (Trc_poss_prov_inventari) o[0];
							Tbc_possessore_provenienza pp = (Tbc_possessore_provenienza) o[1];
							it.getPossessore().add(ConversioneHibernateVO.toXML().possessore(pp, ppi));
						}
					}

					String bidInv = inv.getBid_inv();
					if (!ValidazioneDati.equals(bid, bidInv)) {
						String isbd = inv.getIsbd_inv();

						DocumentoType docInv = new DocumentoType();
						docInv.setBid(bidInv);
						docInv.setIsbd(isbd);
						it.setDocumento(docInv);
					}

					if (inv.isCollocato()) {
						ct.getInventario().add(it);
						//calcolo disponibilità
						if (withDisponibilita) {
							MovimentoVO mov = new MovimentoVO();
							mov.setCodPolo(codPolo);
							mov.setCodBibOperante(codBib);
							mov.setCodBibInv(codBib);
							mov.setCodSerieInv(inv.getCd_serie());
							mov.setCodInvenInv(inv.getCd_inven() + "");
							mov.setDataInizioPrev(now);

							ControlloDisponibilitaVO cdvo = new ControlloDisponibilitaVO(mov, false);
							cdvo.setNoPrenotazione(true);

							CommandInvokeVO cmd = CommandInvokeVO.build(ticket, CommandType.SRV_CONTROLLO_DISPONIBILITA_WS, cdvo, false);
							CommandResultVO response = servizi.invoke(cmd);
							response.throwError();

							DisponibilitaType dtype = (DisponibilitaType) response.getResult();
							it.setDisponibilita(dtype);
						}

						//kardex
						if (withFascicoli && tinv.getCd_natura() == 'S') {
							BibliotecaVO bib = new BibliotecaVO();
							bib.setCod_bib(codBib);
							bib.setCod_polo(codPolo);
							InventarioVO inv2 = new InventarioVO();
							inv2.setCodBib(codBib);
							inv2.setCodSerie(inv.getCd_serie());
							inv2.setCodInvent(inv.getCd_inven());

							RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoVO<FascicoloVO>();
							richiesta.setBiblioteca(bib);
							SerieInventarioVO si = new SerieInventarioVO(inv2);
							si.setBid(inv.getBid_inv());
							richiesta.setOggettoRicerca(si);
							richiesta.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

							CommandInvokeVO cmd = CommandInvokeVO.build(ticket, CommandType.PER_KARDEX_PERIODICO_OPAC, richiesta);
							CommandResultVO result = periodici.invoke(cmd);
							result.throwError();

							KardexPeriodicoOpacVO kpo = (KardexPeriodicoOpacVO) result.getResult();
							List<FascicoloVO> fascicoli = ValidazioneDati.coalesce(kpo.getFascicoli(), ValidazioneDati.emptyList(FascicoloVO.class) );

							for (FascicoloVO f : fascicoli)
								it.getFascicolo().add(ConversioneHibernateVO.toXML().fascicolo((FascicoloDecorator)f));

						}
					} else
						nonCollocati.getInventario().add(it);
				}
			}
			if (ct != null)
				p.getCollocazione().add(ct);
		}
/*
		for (Tbc_collocazione c : collocazioni) {
			CollocazioneType ct = ConversioneHibernateVO.toXML().collocazione(c);
			List<Tbc_inventario> inventari = idao.getListaInventari(c, null);
			for (Tbc_inventario i : inventari) {
				InventarioType it = ConversioneHibernateVO.toXML().inventario(i);
				ct.getInventario().add(it);
			}
			p.getCollocazione().add(ct);
		}
*/
		if (ValidazioneDati.isFilled(nonCollocati.getInventario()))
			p.setNonCollocato(nonCollocati);

		sbn.setPosseduto(p);

		return CommandResultVO.build(command, sbn);
	}

}
