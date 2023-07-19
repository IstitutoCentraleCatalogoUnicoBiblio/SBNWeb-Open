/**
 * Jun 26, 2007 it.iccu.sbn.web.actions.servizi.util
 */
package it.iccu.sbn.util.ConvertiVo;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoCommon;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.MovimentoPerStampaServCorrVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.BiblioPoloVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.DocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.IndirizzoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ProfessioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ResidenzaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.IterServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.MaterieDAO;
import it.iccu.sbn.persistence.dao.servizi.ModalitaErogazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.OccupazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.persistence.dao.servizi.SegnatureDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziDAO;
import it.iccu.sbn.persistence.dao.servizi.SupportiBibliotecaDAO;
import it.iccu.sbn.persistence.dao.servizi.TabelleRelazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_documenti_lettoriDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_modalita_erogazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.TipoServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.TitoliDiStudioDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_default;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.servizi.Tbl_controllo_iter;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_disponibilita_precatalogati;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_materie;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_penalita_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio_web_dati_richiesti;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;
import it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio;
import it.iccu.sbn.polo.orm.servizi.Tbl_storico_richieste_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_materie_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_relazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_supporti_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Trl_tariffe_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.util.TitoloHibernate;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.ServiziConstant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.annimon.stream.Optional;


// ########## FUNZIONI DI UTILITA'
// ################################################
public final class ServiziConversioneVO extends DataBindingConverter {

	private static final long serialVersionUID = -7317431238182498429L;

	private static DocumentoFisicoCommon dfCommon = null;
	private static final char NULL_CHAR = 0;

	private static final ServiziConversioneVO instance = new ServiziConversioneVO();

	private final RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
	private final AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
	private final MaterieDAO matDAO = new MaterieDAO();
	private final UtilitaDAO utilDAO = new UtilitaDAO();
	private final IterServizioDAO iterDAO = new IterServizioDAO();
	private final UtentiDAO utentiDAO = new UtentiDAO();
	private final Tbc_inventarioDao invDAO = new Tbc_inventarioDao();
	private final ServiziDAO srvDAO = new ServiziDAO();
	private final SupportiBibliotecaDAO suppDAO = new SupportiBibliotecaDAO();
	private final Tbl_documenti_lettoriDAO docDAO = new Tbl_documenti_lettoriDAO();
	private final OccupazioniDAO occDAO = new OccupazioniDAO();
	private final SegnatureDAO segnDAO = new SegnatureDAO();
	private final TipoServizioDAO tipoSrvDAO = new TipoServizioDAO();
	private final TitoliDiStudioDAO titStudioDAO = new TitoliDiStudioDAO();
	private final TabelleRelazioneDAO relDAO = new TabelleRelazioneDAO();
	private final ModalitaErogazioneDAO modErogDAO = new ModalitaErogazioneDAO();
	private final SaleDAO saleDAO = new SaleDAO();


	private static DocumentoFisicoCommon getDfCommon() {
		if (dfCommon != null)
			return dfCommon;
		try {
			dfCommon = DomainEJBFactory.getInstance().getDocumentoFisicoCommon();
			return dfCommon;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static final ServizioVO daWebVOAWebDirittoUtente(ElementoSinteticaServizioVO diritto)  {
		ServizioVO servizio = new ServizioVO();
		servizio.setCodPolo(diritto.getCodPolo());
		servizio.setCodBib(diritto.getCodBiblioteca());
		servizio.setCodPoloUte(diritto.getCodPolo()); //?
		servizio.setCodBibUte(diritto.getCodBiblioteca()); //?
		servizio.setCodUte(""); //?
		servizio.setCodice(diritto.getTipServizio());
		servizio.setServizio(diritto.getCodServizio());
		servizio.setAutorizzazione("");

		servizio.setNote(""); //?
		servizio.setDataInizio(null); //?
		servizio.setDataFine(null); // ?
		servizio.setSospDataInizio(null);
		servizio.setSospDataFine(null);
		servizio.setStato(ServizioVO.OLD);
		servizio.setUteIns(diritto.getUteIns());
		servizio.setUteVar(diritto.getUteVar());
		servizio.setTsIns(diritto.getTsIns());
		servizio.setTsVar(diritto.getTsVar());
		servizio.setFlCanc(String.valueOf(diritto.getFlCanc()));
		servizio.setDescrizione(diritto.getDesServizio()); //?
		servizio.setIdServizio(diritto.getIdServizio()); //?
		servizio.setIdUtente(0); //?
		return servizio;
	}

	private static final ServizioVO daHibernateAWebDirittoUtente(Trl_diritti_utente diritto) throws Exception {
		ServizioVO servizio = new ServizioVO();
		Tbl_servizio srv = diritto.getId_servizio();
		Tbl_tipo_servizio tipoSrv = srv.getId_tipo_servizio();
		Tbf_biblioteca_in_polo bib = tipoSrv.getCd_bib();

		servizio.setCodPolo(bib.getCd_polo().getCd_polo());
		servizio.setCodBib(bib.getCd_biblioteca());
		servizio.setCodPoloUte(diritto.getId_utenti().getCd_bib().getCd_polo().getCd_polo());
		servizio.setCodBibUte(diritto.getId_utenti().getCd_bib().getCd_biblioteca());
		servizio.setCodUte(String.valueOf(diritto.getId_utenti().getCod_utente()));

		servizio.setCodice(tipoSrv.getCod_tipo_serv());
		servizio.setServizio(srv.getCod_servizio());
		servizio.setAutorizzazione("");

		servizio.setNote(trimString(diritto.getNote()));
		servizio.setDataInizio(dateToString(diritto.getData_inizio_serv()));
		servizio.setDataFine(dateToString(diritto.getData_fine_serv()));
		servizio.setSospDataInizio(dateToString(diritto.getData_inizio_sosp()));
		servizio.setSospDataFine(dateToString(diritto.getData_fine_sosp()));
		servizio.setStato(ServizioVO.OLD);
		servizio.setUteIns(diritto.getUte_ins());
		servizio.setUteVar(diritto.getUte_var());
		servizio.setTsIns(diritto.getTs_ins());
		servizio.setTsVar(diritto.getTs_var());
		servizio.setFlCanc(String.valueOf(diritto.getFl_canc()));
		servizio.setDescrizione(srv.getDescr());
		servizio.setFlag_aut_ereditato(diritto.getCod_tipo_aut()); // 20.01.10

		servizio.setIdServizio(srv.getId_servizio());
		servizio.setIdUtente(diritto.getId_utenti().getId_utenti());

		servizio.setDescrizioneTipoServizio(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, servizio.getCodice()));

		return servizio;
	}


	private static final String dateToString(java.util.Date data) {
		if (data == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String value = format.format(data);

		return value;
	}


	private static final java.util.Date stringToDate(java.lang.String data) {
		if (data == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date value;
		try {
			value = format.parse(data);
		} catch (ParseException e) {
			return null;
		}

		return value;
	}


	private static final String trimString(String value) {
		if (value == null)
			return null;
		return value.trim();
	}

	public static final UtenteBibliotecaVO daBibliotecaVOAUtenteBibliotecaVO(BibliotecaVO bib) {
		UtenteBibliotecaVO utente = new UtenteBibliotecaVO();

		utente.getBibliopolo().setCodPoloXUteBib(bib.getCod_polo());
		utente.getBibliopolo().setCodBibXUteBib(bib.getCod_bib());
		utente.getBibliopolo().setCodiceAnagrafe(bib.getCd_ana_biblioteca());
		utente.setCognome(bib.getNom_biblioteca());
		utente.setNome(" ");
		utente.setFlCanc("N");
		utente.setFlCancAna("N"); // 16.02.2010
		utente.getAnagrafe().setProvenienza(bib.getChiave_ente());
		utente.setNuovoUte(true);
//		utente.setPassword();

		utente.getProfessione().setPersonaGiuridica('S');

		utente.getAnagrafe().setCodFiscale( ValidazioneDati.strIsNull(bib.getCod_fiscale()) ? bib.getP_iva() : bib.getCod_fiscale() );
		utente.getAnagrafe().setDataNascita("00/00/0000");
		utente.getAnagrafe().setLuogoNascita(" ");
		utente.getAnagrafe().setNazionalita(bib.getPaese());
		utente.getAnagrafe().setPostaElettronica("");
		if (bib.getE_mail()!=null){
			utente.getAnagrafe().setPostaElettronica(bib.getE_mail());
		}

		utente.getAnagrafe().setProvenienza(" ");
		utente.getAnagrafe().setSesso(" ");

		utente.getAnagrafe().getDomicilio().setCap(bib.getCap());
		utente.getAnagrafe().getDomicilio().setCitta(bib.getLocalita());
		utente.getAnagrafe().getDomicilio().setFax(bib.getFax());
		utente.getAnagrafe().getDomicilio().setProvincia(bib.getProvincia());
		utente.getAnagrafe().getDomicilio().setTelefono(bib.getTelefono());
		utente.getAnagrafe().getDomicilio().setVia(bib.getIndirizzo());

		utente.getAnagrafe().getResidenza().setCap(bib.getCap());
		utente.getAnagrafe().getResidenza().setCitta(bib.getLocalita());
		utente.getAnagrafe().getResidenza().setFax(bib.getFax());
		utente.getAnagrafe().getResidenza().setProvincia(bib.getProvincia());
		utente.getAnagrafe().getResidenza().setTelefono(bib.getTelefono());
		utente.getAnagrafe().getResidenza().setVia(bib.getIndirizzo());
		utente.getAnagrafe().getResidenza().setNazionalita(bib.getPaese());

		utente.setTipoUtente(Servizi.Utenti.UTENTE_TIPO_SBNWEB);

		return utente;
	}


	public static final ElementoSinteticaServizioVO daHibernateAWebAnagServizio(Tbl_servizio hibernateVO,
			int progressivo) {
		ElementoSinteticaServizioVO servizio = new ElementoSinteticaServizioVO();
		servizio.setProgressivo(progressivo);
		servizio.setCodBiblioteca(hibernateVO.getId_tipo_servizio().getCd_bib()
				.getCd_biblioteca());
		servizio.setCodPolo(hibernateVO.getId_tipo_servizio().getCd_bib()
				.getCd_polo().getCd_polo());
		servizio.setTipServizio(hibernateVO.getId_tipo_servizio().getCod_tipo_serv());
		servizio.setCodServizio(hibernateVO.getCod_servizio());
		servizio.setDesServizio(trimString(hibernateVO.getDescr()));
		servizio.setIdServizio(hibernateVO.getId_servizio());
		servizio.setCancella("");
		servizio.setStato(ElementoSinteticaServizioVO.NEW);
		servizio.setUteIns(hibernateVO.getUte_ins());
		servizio.setUteVar(hibernateVO.getUte_var());
		servizio.setTsIns(hibernateVO.getTs_ins());
		servizio.setTsVar(hibernateVO.getTs_var());
		servizio.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));
		return servizio;
	}

	public static final FaseControlloIterVO daHibernateAWebControlloIter(Tbl_controllo_iter controlloIter) {
		FaseControlloIterVO controlloIterVO = new FaseControlloIterVO();

		controlloIterVO.setCodAttivita(controlloIter.getId_iter_servizio().getCod_attivita());
		controlloIterVO.setCodBib(controlloIter.getId_iter_servizio().getId_tipo_servizio().getCd_bib().getCd_biblioteca());
		controlloIterVO.setCodControllo(controlloIter.getCod_controllo());
		controlloIterVO.setCodTipoServ(controlloIter.getId_iter_servizio().getId_tipo_servizio().getCod_tipo_serv());
		controlloIterVO.setDataAgg(new Date(controlloIter.getTs_var().getTime()));
		controlloIterVO.setDataIns(new Date(controlloIter.getTs_ins().getTime()));
		controlloIterVO.setFlagBloc(controlloIter.getFl_bloccante()=='S' ? true : false);
		controlloIterVO.setMessaggio(controlloIter.getMessaggio());
		controlloIterVO.setProgrFase(controlloIter.getProgr_fase());
		controlloIterVO.setUteIns(controlloIter.getUte_ins());
		controlloIterVO.setUteVar(controlloIter.getUte_var());
		controlloIterVO.setFlCanc(controlloIter.getFl_canc()+"");
		return controlloIterVO;
	}


	public static final MateriaVO daHibernateAWebMateria(Tbl_materie hibernateVO,
			int progressivo) throws Exception {
		MateriaVO webVO = new MateriaVO();

		webVO.setProgressivo(progressivo);
		webVO.setCodBib(hibernateVO.getCd_biblioteca().getCd_biblioteca());
		webVO.setCodPolo(hibernateVO.getCd_biblioteca().getCd_polo().getCd_polo());
		webVO.setCodice(hibernateVO.getCod_mat());
		webVO.setDescrizione(trimString(hibernateVO.getDescr()));
		webVO.setSelezionato("");
		webVO.setStato(MateriaVO.INIZIALE_NON_SELEZIONATO);
		webVO.setIdMateria(hibernateVO.getId_materia());
		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));

		return webVO;
	}


	public static final MateriaVO daHibernateAWebMateriaUtente(Trl_materie_utenti hibernateVO)
			throws Exception {
		MateriaVO webVO = new MateriaVO();
		Tbl_materie materia = hibernateVO.getId_materia();

		webVO.setCodBib(materia.getCd_biblioteca().getCd_biblioteca());
		webVO.setCodPolo(materia.getCd_biblioteca().getCd_polo().getCd_polo());
		webVO.setCodice(materia.getCod_mat());
		webVO.setDescrizione(trimString(materia.getDescr()));
		webVO.setSelezionato("S");
		webVO.setStato(MateriaVO.INIZIALE_SELEZIONATO);
		webVO.setIdMateria(materia.getId_materia());
		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));

		return webVO;
	}


	public static final MovimentoVO daHibernateAWebMovimento(
			Tbl_richiesta_servizio hVO, Locale locale) throws DaoManagerException {

		MovimentoVO webVO = new MovimentoVO();

		webVO.setCodRichServ(Long.toString(hVO.getCod_rich_serv()));
		webVO.setIdRichiesta(hVO.getCod_rich_serv());

		if (hVO.getAnno_period() != null)
			webVO.setAnnoPeriodico(hVO.getAnno_period().toString());

		webVO.setBid(hVO.getBid());
		webVO.setCod_erog(hVO.getCod_erog());
		webVO.setCodAttivita(hVO.getCod_attivita());
		webVO.setCodBibDest(hVO.getCod_bib_dest());

		Tbl_esemplare_documento_lettore id_esemplare = hVO.getId_esemplare_documenti_lettore();
		if (id_esemplare != null) {
			webVO.setProgrEsempDocLet(id_esemplare.getPrg_esemplare() + "");

			Tbl_documenti_lettori documento = id_esemplare.getId_documenti_lettore();
			webVO.setIdDocumento(documento.getId_documenti_lettore());
			webVO.setCodBibDocLett(documento.getCd_bib().getCd_biblioteca());
			webVO.setTipoDocLett(documento.getTipo_doc_lett() + "");
			webVO.setCodDocLet(documento.getCod_doc_lett() + "");
			Tbf_biblioteca_in_polo bib = documento.getCd_bib();
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBibOperante(bib.getCd_biblioteca());
		}

		/*
		Tbl_documenti_lettori documento = hVO.getId_documenti_lettore();
		if (documento != null) {
			webVO.setIdDocumento(documento.getId_documenti_lettore());
			webVO.setCodBibDocLett(documento.getCd_bib().getCd_biblioteca());
			webVO.setTipoDocLett(documento.getTipo_doc_lett() + "");
			webVO.setCodDocLet(documento.getCod_doc_lett() + "");
			Tbl_esemplare_documento_lettore id_esemplare = hVO.getId_esemplare_documenti_lettore();
			if (id_esemplare != null)
				webVO.setProgrEsempDocLet(id_esemplare.getPrg_esemplare() + "");
		}
		*/
		Tbc_inventario inventario = hVO.getCd_polo_inv();
		if (inventario != null) {
			Tbc_serie_inventariale cd_serie = inventario.getCd_serie();
			Tbf_biblioteca_in_polo bib = cd_serie.getCd_polo();
			webVO.setCodBibInv(bib.getCd_biblioteca());
			webVO.setCodSerieInv(cd_serie.getCd_serie());
			webVO.setCodInvenInv(inventario.getCd_inven() + "");
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBibOperante(bib.getCd_biblioteca());
		}

		webVO.setCodBibPrelievo(hVO.getCod_bib_prelievo());
		webVO.setCodBibRestituzione(hVO.getCod_bib_restituzione());

		Trl_utenti_biblioteca id_utenti_biblioteca = hVO.getId_utenti_biblioteca();
		//movimento.setCodBibUte(id_utenti_biblioteca.getCd_biblioteca().getCd_biblioteca());
		webVO.setCodBibUte(id_utenti_biblioteca.getId_utenti().getCd_bib().getCd_biblioteca());
		webVO.setCodUte(id_utenti_biblioteca.getId_utenti().getCod_utente());
		webVO.setIdUtenteBib(id_utenti_biblioteca.getId_utenti_biblioteca());
		webVO.setEmail(ConversioneHibernateVO.toWeb().getEmailUtente(id_utenti_biblioteca.getId_utenti()));

		if (hVO.getId_modalita_pagamento() != null)
			webVO.setCodModPag(hVO.getId_modalita_pagamento().getCod_mod_pag() + "");

		if (hVO.getCod_risp() != null)
			webVO.setCodRisp(hVO.getCod_risp().toString());

		Tbl_servizio diritto = hVO.getId_servizio();
		webVO.setIdServizio(diritto.getId_servizio());
		webVO.setCodServ(diritto.getCod_servizio());
		webVO.setCodStatoMov(String.valueOf(hVO.getCod_stato_mov()));
		webVO.setCodStatoRic(String.valueOf(hVO.getCod_stato_rich()));

		if (hVO.getId_supporti_biblioteca() != null)
			webVO.setCodSupporto(hVO.getId_supporti_biblioteca().getCod_supporto());

		webVO.setCodTipoServ(diritto.getId_tipo_servizio().getCod_tipo_serv());
		webVO.setCodTipoServRich(hVO.getCod_tipo_serv_rich());


		if (hVO.getCopyright() != null)
			webVO.setCopyright(hVO.getCopyright() + "");

		if (hVO.getCosto_servizio() != null)
			webVO.setCostoServizio(hVO.getCosto_servizio()
					.doubleValue(), ServiziConstant.NUMBER_FORMAT_CONVERTER,
					locale);

		webVO.setDataFineEff(hVO.getData_fine_eff());
		webVO.setDataFinePrev(hVO.getData_fine_prev());
		webVO.setDataInizioEff(hVO.getData_in_eff());
		webVO.setDataInizioPrev(hVO.getData_in_prev());

		if (hVO.getData_massima() != null)
			webVO.setDataMax(new java.sql.Date(hVO.getData_massima().getTime()));

		if (hVO.getData_proroga() != null)
			webVO.setDataProroga(new java.sql.Date(hVO.getData_proroga().getTime()));

		webVO.setDataRichiesta(hVO.getData_richiesta());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		if (hVO.getFl_condiz() != null)
			webVO.setFlCondiz(hVO.getFl_condiz().toString());

		webVO.setFlSvolg(String.valueOf(hVO.getFl_svolg()));
		webVO.setFlTipoRec(RichiestaRecordType.fromChar(hVO.getFl_tipo_rec()) );
		webVO.setNoteBibliotecario(hVO.getNote_bibliotecario());
		webVO.setNoteUtente(hVO.getNote_ut());
		webVO.setNumFascicolo(hVO.getNum_fasc());

		Tbl_prenotazione_posto pp = ValidazioneDati.first(hVO.getPrenotazioni_posti());
		webVO.setPrenotazionePosto(ConvertToWeb.Sale.prenotazione(pp, webVO));

		if (hVO.getNum_pezzi() != null)
			webVO.setNumPezzi(Short.toString(hVO.getNum_pezzi()));

		if (hVO.getNum_rinnovi() != null)
			webVO.setNumRinnovi(hVO.getNum_rinnovi());

		webVO.setNumVolume((hVO.getNum_volume()));

		if (hVO.getPrezzo_max() != null)
			webVO.setPrezzoMax(hVO.getPrezzo_max().doubleValue(),
					ServiziConstant.NUMBER_FORMAT_CONVERTER, locale);

		webVO.setProgrIter(hVO.getId_iter_servizio().getProgr_iter() + "");

		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());

		//almaviva5_20150127 servizi ill;
		Tbl_dati_richiesta_ill dati_ill = hVO.getDati_richiesta_ill();
		if (dati_ill != null && !ValidazioneDati.in(dati_ill.getFl_canc(), 's', 'S')) {
			DatiRichiestaILLVO datiILL = ConversioneHibernateVO.toWeb().datiRichiestaILL(dati_ill, null);
			datiILL.setMovimento(webVO);
			datiILL.setCod_rich_serv(webVO.getIdRichiesta());
			webVO.setDatiILL(datiILL);
		}

		webVO.setIntervalloCopia(hVO.getIntervallo_copia());

		return webVO;

	}

	public static final MovimentoPerStampaServCorrVO daHibernateAWebMovimentoPerStampa(
			Serializable entity, Locale locale, String ticket) throws Exception {

		MovimentoPerStampaServCorrVO mov = new MovimentoPerStampaServCorrVO();
		TitoloDAO dao = new TitoloDAO();
		if (entity instanceof Tbl_richiesta_servizio) {
			Tbl_richiesta_servizio req = (Tbl_richiesta_servizio)entity;

			mov.setCodRichServ(Long.toString(req.getCod_rich_serv()));
			mov.setIdRichiesta(req.getCod_rich_serv());
			if (req.getAnno_period() != null) {
				mov.setAnnoPeriodico(req.getAnno_period().toString());
			} else {
				mov.setAnnoPeriodico("0");
			}
			mov.setBid(req.getBid());
			mov.setCod_erog(req.getCod_erog());
			mov.setCodAttivita(req.getCod_attivita());
			mov.setCodBibDest(req.getCod_bib_dest());

			Tbl_documenti_lettori doc = req.getId_documenti_lettore();
			if (doc != null) {
				mov.setCodBibDocLett(doc.getCd_bib().getCd_biblioteca());
				mov.setTipoDocLett(doc.getTipo_doc_lett() + "");
				mov.setCodDocLet(doc.getCod_doc_lett() + "");
				Tbl_esemplare_documento_lettore id_esemplare = req.getId_esemplare_documenti_lettore();
				if (id_esemplare != null) {
					mov.setProgrEsempDocLet(id_esemplare.getPrg_esemplare() + "");
					dao.evict(id_esemplare);
				}

				//documento lettore
				List titoli = null;
				String bid = doc.getBid();
				if (ValidazioneDati.isFilled(bid)) {
					//almaviva5_20130307 #5268 check su esistenza bid in base dati

					Tb_titolo t = dao.getTitoloLazy(bid);
					if (t != null && !ValidazioneDati.in(t.getFl_canc(), 's', 'S') ) {
						try {
							titoli = getDfCommon().getTitolo(bid, ticket);
						} catch (RemoteException e) {}
						dao.evict(t);
					}
					if (ValidazioneDati.isFilled(titoli) ) {
						TitoloVO tit = (TitoloVO)ValidazioneDati.first(titoli);
						mov.setTitolo(tit.getIsbd());
					} else
						mov.setTitolo("Titolo non trovato in Polo");

				} else {
					// non c'è il bid, prendo il titolo
					if (doc.getTitolo() != null) {
						mov.setTitolo(doc.getTitolo());
					} else {
						mov.setTitolo("Titolo non trovato in Polo");
					}
				}
				mov.setSegnatura(doc.getSegnatura());

				dao.evict(doc);

			} else {
				mov.setCodBibDocLett("");
				mov.setTipoDocLett("");
				mov.setCodDocLet("");
				mov.setProgrEsempDocLet("");
			}

			Tbc_inventario inventario = req.getCd_polo_inv();
			if (inventario != null) {
				Tbc_serie_inventariale cd_serie = inventario.getCd_serie();
				mov.setCodBibInv(cd_serie.getCd_polo().getCd_biblioteca());
				mov.setCodSerieInv(cd_serie.getCd_serie());
				mov.setCodInvenInv(inventario.getCd_inven() + "");
				mov.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(req));
				//
				List<TitoloVO> titoli = null;
				Tb_titolo t = inventario.getB();
				//almaviva5_20120116 #4829
				if (t != null) {
					try {
						//titoli = getDfCommon().getTitolo(t.getBid(), ticket);
						titoli = TitoloHibernate.getTitolo(t.getBid(), ticket);
					} catch (RemoteException e) {
						mov.setTitolo("Titolo non trovato in Polo");
					}
					if (ValidazioneDati.size(titoli) == 1) {
						TitoloVO tit = titoli.get(0);
						mov.setTitolo(tit.getIsbd());

					} else {
						mov.setTitolo(t.getIsbd());
						mov.setTitolo("Titolo non trovato in Polo");
					}
					dao.evict(t);
				} else
					mov.setTitolo("Titolo non trovato in Polo");

				dao.evict(inventario.getKey_loc());
				dao.evict(inventario);
			}

			mov.setCodBibPrelievo(req.getCod_bib_prelievo());
			mov.setCodBibRestituzione(req.getCod_bib_restituzione());

			Trl_utenti_biblioteca id_utenti_biblioteca = req.getId_utenti_biblioteca();
			mov.setCodBibUte(id_utenti_biblioteca.getId_utenti().getCd_bib().getCd_biblioteca());
			mov.setCodUte(id_utenti_biblioteca.getId_utenti().getCod_utente().trim());
			mov.setNominativo(id_utenti_biblioteca.getId_utenti().getNome().trim()+" "+id_utenti_biblioteca.getId_utenti().getCognome().trim());
			Tbl_occupazioni occupazioni = id_utenti_biblioteca.getId_occupazioni();
			if (occupazioni != null) {
				mov.setProfessione(String.valueOf(id_utenti_biblioteca.getId_occupazioni().getProfessione()));
			} else {
				mov.setProfessione("");
			}
			Tbl_utenti utenti = id_utenti_biblioteca.getId_utenti();
			if (utenti != null) {
				if (utenti.getTit_studio() != null) {
					mov.setTitoloStudio(String.valueOf(id_utenti_biblioteca.getId_utenti().getTit_studio()));
				}
			} else {
				mov.setTitoloStudio("");
			}

			if (req.getId_modalita_pagamento() != null)
				mov.setCodModPag(req.getId_modalita_pagamento().getCod_mod_pag() + "");

			if (req.getCod_risp() != null)
				mov.setCodRisp(req.getCod_risp().toString());

			mov.setCodServ(req.getId_servizio().getCod_servizio());
			mov.setCodStatoMov(String.valueOf(req.getCod_stato_mov()));
			mov.setCodStatoRic(String.valueOf(req.getCod_stato_rich()));

			if (req.getId_supporti_biblioteca() != null)
				mov.setCodSupporto(req.getId_supporti_biblioteca().getCod_supporto());

			mov.setCodBibTipoServ(req.getId_servizio().getId_tipo_servizio().getCd_bib().getCd_biblioteca());
			mov.setDescrBibTipoServ(req.getId_servizio().getId_tipo_servizio().getCd_bib().getDs_biblioteca());
			mov.setCodTipoServ(req.getId_servizio().getId_tipo_servizio().getCod_tipo_serv());
			mov.setCodTipoServRich(req.getCod_tipo_serv_rich());

			dao.evict(id_utenti_biblioteca);
			dao.evict(utenti);

			if (req.getCopyright() != null)
				mov.setCopyright(req.getCopyright() + "");

			if (req.getCosto_servizio() != null)
				mov.setCostoServizio(req.getCosto_servizio()
						.doubleValue(), ServiziConstant.NUMBER_FORMAT_CONVERTER,
						locale);

			mov.setDataFineEff(req.getData_fine_eff());
			mov.setDataFinePrev(req.getData_fine_prev());
			mov.setDataInizioEff(req.getData_in_eff());
			mov.setDataInizioPrev(req.getData_in_prev());

			if (req.getData_massima() != null)
				mov.setDataMax(new java.sql.Date(req.getData_massima().getTime()));

			if (req.getData_proroga() != null)
				mov.setDataProroga(new java.sql.Date(req.getData_proroga().getTime()));

			mov.setDataRichiesta(req.getData_richiesta());
			mov.setFlCanc(String.valueOf(req.getFl_canc()));

			if (req.getFl_condiz() != null)
				mov.setFlCondiz(req.getFl_condiz().toString());

			mov.setFlSvolg(String.valueOf(req.getFl_svolg()));
			mov.setFlTipoRec(RichiestaRecordType.fromChar(req.getFl_tipo_rec()) );
			mov.setNoteBibliotecario(req.getNote_bibliotecario());
			mov.setNoteUtente(req.getNote_ut());
			mov.setNumFascicolo(req.getNum_fasc());

			if (req.getNum_pezzi() != null)
				mov.setNumPezzi(Short.toString(req.getNum_pezzi()));

			if (req.getNum_rinnovi() != null)
				mov.setNumRinnovi(req.getNum_rinnovi());

			mov.setNumVolume(req.getNum_volume());

			if (req.getPrezzo_max() != null)
				mov.setPrezzoMax(req.getPrezzo_max().doubleValue(),
						ServiziConstant.NUMBER_FORMAT_CONVERTER, locale);

			mov.setProgrIter(req.getId_iter_servizio().getProgr_iter() + "");
			mov.setTsIns(req.getTs_ins());
			mov.setTsVar(req.getTs_var());
			mov.setUteIns(req.getUte_ins());
			mov.setUteVar(req.getUte_var());

		} else
			if (entity instanceof Tbl_storico_richieste_servizio) {
			Tbl_storico_richieste_servizio req = (Tbl_storico_richieste_servizio)entity;
			/*campi tabella
			  x"cd_polo" CHAR(3) NOT NULL,
			  x"cd_bib" CHAR(3) NOT NULL,
			  x"cod_rich_serv" NUMERIC(12,0) NOT NULL,
			  x"cod_bib_ut" CHAR(3),
			  x"cod_utente" NUMERIC(10,0),
			  x"cod_tipo_serv" CHAR(2),
			  x"data_richiesta" DATE,
			  x"num_volume" SMALLINT,
			  x"num_fasc" CHAR(15),
			  x"num_pezzi" SMALLINT,
			  x"note_ut" VARCHAR(255),
			  x"prezzo_max" NUMERIC(12,3),
			  x"costo_servizio" NUMERIC(12,3),
			  x"data_massima" DATE,
			  x"note_bibl" VARCHAR(255),
			  x"data_proroga" DATE,
			  x"data_in_prev" DATE,
			  x"data_fine_prev" DATE,
			  x"flag_svolg" CHAR(1),
			  x"copyright" CHAR(1),
			  x"descr_erog" VARCHAR(255),
			  x"descr_stato_ric" VARCHAR(255),
			  x"descr_supporto" VARCHAR(255),
			  x"descr_risp" VARCHAR(255),
			  x"descr_mod_pag" VARCHAR(255),
			  x"flag_pren" CHAR(1),
			  x"tipo_doc_lett" CHAR(1),
			  x"cod_doc_lett" NUMERIC(10,0),
			  x"prg_esemplare" SMALLINT,
			  x"cod_serie" CHAR(3),
			  x"cod_inven" INTEGER,
			  x"flag_condiz" CHAR(1),
			  x"cod_tipo_serv_alt" CHAR(2),
			  x"descr_erog_alt" VARCHAR(255),
			  x"cod_bib_dest" CHAR(3),
			  x"titolo" VARCHAR(240),
			  x"autore" VARCHAR(160),
			  x"editore" CHAR(50),
			  x"anno_edizione" CHAR(4),
			  x"luogo_edizione" CHAR(30),
			  x"annata" CHAR(10),
			  x"num_vol_mon" SMALLINT,
			  x"data_in_eff" DATE,
			  x"data_fine_eff" DATE,
			  x"num_rinnovi" SMALLINT,
			  x"note_bibliotecario" VARCHAR(255),
			  x"descr_stato_mov" VARCHAR(255),
			  x"flag_tipo_serv" CHAR(1),
			  x"num_solleciti" SMALLINT,
			  x"data_ult_soll" DATE,
			 */
			//cd_polo non serve
			mov.setCodPolo(req.getCd_polo());
			mov.setCodBibDocLett(req.getCd_bib());
			mov.setCodRichServ(req.getCod_rich_serv().toString());
			mov.setCodBibUte(req.getCod_bib_ut());
			mov.setCodUte(req.getCod_utente().toString());
			mov.setCodTipoServ(req.getCod_tipo_serv());
			mov.setDescrTipoServ(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, req.getCod_tipo_serv()));
//			movimento.setDataRichiesta(DateUtil.toTimestamp(richiesta.getData_richiesta().toString()));
			mov.setDataRichiesta(new Timestamp(req.getData_richiesta().getTime()));
			mov.setNumVolume(Short.toString(req.getNum_volume()));
			if (req.getNum_fasc() != null){
				mov.setNumFascicolo(req.getNum_fasc());
			}else{
				mov.setNumFascicolo("");
			}
			mov.setNumPezzi(Short.toString(req.getNum_pezzi()));
			mov.setNoteUtente(req.getNote_ut());
			if (req.getPrezzo_max() != null){
				mov.setPrezzoMax(req.getPrezzo_max().doubleValue(),
						ServiziConstant.NUMBER_FORMAT_CONVERTER, locale);
			}
			if (req.getCosto_servizio() != null)
				mov.setCostoServizio(req.getCosto_servizio()
						.doubleValue(), ServiziConstant.NUMBER_FORMAT_CONVERTER,
						locale);
			if (req.getData_massima() != null)
				mov.setDataMax(new java.sql.Date(req.getData_massima().getTime()));
			mov.setNoteBibl(req.getNote_bibl());
			if (req.getData_proroga() != null)
				mov.setDataProroga(new java.sql.Date(req.getData_proroga().getTime()));

			mov.setDataInizioPrev(new Timestamp(req.getData_in_prev().getTime()));
			mov.setDataFinePrev(new Timestamp(req.getData_fine_prev().getTime()));
			mov.setFlSvolg(req.getFlag_svolg()+"");
			if (req.getCopyright() != null)
				mov.setCopyright(req.getCopyright() + "");
			mov.setModErog(req.getDescr_erog());
			mov.setStatoRichiesta(req.getDescr_stato_ric());
			mov.setSupporto(req.getDescr_supporto());
			mov.setRisposta(req.getDescr_risp());
			mov.setModPagamento(req.getDescr_mod_pag());
			//richiesta.getFlag_pren() non in output c/s
			mov.setPrenotazione(req.getFlag_pren()+"");
			if (req.getTipo_doc_lett() != null){
				mov.setTipoDocLett(req.getTipo_doc_lett() + "");
			}else{
				mov.setTipoDocLett("");
			}
			if (req.getCod_doc_lett() != null){
				mov.setCodDocLet(req.getCod_doc_lett() + "");
			}else{
				mov.setCodDocLet("");
			}
				mov.setProgrEsempDocLet(req.getPrg_esemplare() + "");

			//almaviva5_20110113 #3988
			mov.setCodBibInv(req.getCod_bib_dest());
			mov.setTitolo(ValidazioneDati.isFilled(req.getTitolo()) ? req.getTitolo() : ServiziConstant.MSG_INVENTARIO_CANCELLATO);
			//
			mov.setCodSerieInv(req.getCod_serie());
			mov.setCodInvenInv(isFilled(req.getCod_inven()) ? req.getCod_inven() + "" : "");
			mov.setFlCondiz(req.getFlag_condiz()+"");
			mov.setServIllAltern(req.getDescr_erog_alt());
			mov.setCodBibDest(req.getCod_bib_dest());
			mov.setAutore(req.getAutore());
			mov.setEditore(req.getEditore());
			mov.setAnnoEdizione(req.getAnno_edizione());
			mov.setLuogoEdizione(req.getLuogo_edizione());
			if (req.getAnnata() != null){
				mov.setAnnoPeriodico(req.getAnnata().toString());
			}else{
				mov.setAnnoPeriodico("0");
			}
			mov.setNumVolumeMon(Short.toString(req.getNum_vol_mon()));
			if (req.getData_in_eff() != null)
			mov.setDataInizioEff(new Timestamp(req.getData_in_eff().getTime()));
			if (req.getData_fine_eff() != null)
			mov.setDataFineEff(new Timestamp(req.getData_fine_eff().getTime()));
			if (req.getNum_rinnovi() > 0)
				mov.setNumRinnovi(req.getNum_rinnovi());
			mov.setDescrStatoMov(String.valueOf(req.getDescr_stato_mov()));
			mov.setNumSolleciti(String.valueOf(req.getNum_solleciti()));
			if (req.getData_ult_soll() != null)
				mov.setDataUltSoll(new java.sql.Date(req.getData_ult_soll().getTime()));

			mov.setTsIns(new java.sql.Timestamp(req.getTs_ins().getTime()));
			mov.setTsVar(new java.sql.Timestamp(req.getTs_var().getTime()));
			mov.setUteIns(req.getUte_ins());
			mov.setUteVar(req.getUte_var());

			//almaviva5_20120906 #4829
			BigDecimal cod_utente = req.getCod_utente();
			if (ValidazioneDati.isFilled(cod_utente)) {
				try {
					Trl_utenti_biblioteca id_utenti_biblioteca = instance.utentiDAO.getUtenteBibliotecaById(cod_utente.intValue());
					if (id_utenti_biblioteca != null) {
						Tbl_utenti id_utenti = id_utenti_biblioteca.getId_utenti();
						mov.setCodBibUte(id_utenti.getCd_bib().getCd_biblioteca());
						mov.setCodUte(id_utenti.getCod_utente().trim());
						mov.setNominativo(id_utenti.getNome().trim()+" "+id_utenti.getCognome().trim());
					}
				} catch (Exception e) {
					//almaviva5_20181107 #6774
					mov.setNominativo(ServiziConstant.MSG_UTENTE_NON_TROVATO);
				}
			}

			try {
				if (mov.isRichiestaSuInventario() ) {
					Tbc_inventario inv = instance.invDAO.getInventario(
							mov.getCodPolo(), mov.getCodBibInv(),
							mov.getCodSerieInv(), req.getCod_inven());
					if (inv != null)
						mov.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(inv, null));
				} else {
					//almaviva5_20181107 #6774
					Optional<BigDecimal> codDocLett = Optional.ofNullable(req.getCod_doc_lett());
					Tbl_documenti_lettori doc = instance.docDAO
							.getDocumentoLettore(mov.getCodPolo(),
									mov.getCodBibDocLett(), mov.getTipoDocLett(),
									codDocLett.orElse(BigDecimal.ZERO).longValue());
					if (doc != null)
						mov.setSegnatura(doc.getSegnatura());
				}
			} catch (Exception e) {
				//almaviva5_20181107 #6774
				mov.setSegnatura(ServiziConstant.MSG_INVENTARIO_CANCELLATO);
			}

		}

		return mov;
	}


	public static final MovimentoListaVO daHibernateAWebMovimentoLista(
			Tbl_richiesta_servizio hVO, Locale locale, Timestamp now)
			throws DaoManagerException {

		MovimentoVO movimento = daHibernateAWebMovimento(hVO, locale);

		MovimentoListaVO webVO = new MovimentoListaVO(movimento);
		Tbl_utenti id_utenti = hVO.getId_utenti_biblioteca().getId_utenti();
		webVO.setCognomeNome(trimOrEmpty(id_utenti.getCognome()) + " " + trimOrEmpty(id_utenti.getNome()));

		Tbl_solleciti sollecito = instance.richiesteDAO.getUltimoSollecito(hVO);
		if (sollecito != null) {
			webVO.setNumSolleciti(sollecito.getProgr_sollecito() );
			java.util.Date dataInvio = sollecito.getData_invio();
			webVO.setDataUltimoSollecito(dateToString(dataInvio));
			webVO.setData_invio_soll(dataInvio);
		}
		else {
			short giorniTolleranza = hVO.getId_servizio().getMax_gg_cons();
			Timestamp dtFinePrev = hVO.getData_fine_prev();
			if (dtFinePrev != null) {
				Timestamp fineRichiesta = DateUtil.addDay(dtFinePrev, giorniTolleranza);
				webVO.setScaduto(fineRichiesta.before(now));
			}
		}

		webVO.setRinnovabile(hVO.getId_iter_servizio().getFlg_rinn() == 'S');

		return webVO;
	}


	public static final OccupazioneVO daHibernateAWebOccupazione(Tbl_occupazioni hibernateVO,
			int progressivo) {

		OccupazioneVO webVO = new OccupazioneVO();
		webVO.setProgressivo(progressivo);
		webVO.setCodBiblioteca(hibernateVO.getCd_biblioteca().getCd_biblioteca());
		webVO.setCodPolo(hibernateVO.getCd_biblioteca().getCd_polo().getCd_polo());

		webVO.setIdOccupazioni(hibernateVO.getId_occupazioni());
		webVO.setCodOccupazione(String.valueOf(hibernateVO.getOccupazione()));
		//webVO.setCodOccupazione(hibernateVO.getOccupazione()); // 03.02.10

		webVO.setDesOccupazione(hibernateVO.getDescr());
		webVO.setProfessione(String.valueOf(hibernateVO.getProfessione()));

		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));

		return webVO;
	}



	public static final PenalitaServizioVO daHibernateAWebPenalitaServizio(Tbl_penalita_servizio penalita) {
		PenalitaServizioVO penalitaVO = null;

		if (penalita!=null) {
			penalitaVO = new PenalitaServizioVO();
			penalitaVO.setCodBib(penalita.getId_servizio().getId_tipo_servizio().getCd_bib().getCd_biblioteca());
			penalitaVO.setCodServ(penalita.getId_servizio().getCod_servizio());
			penalitaVO.setCodTipoServ(penalita.getId_servizio().getId_tipo_servizio().getCod_tipo_serv());
			penalitaVO.setCoeffSosp(penalita.getCoeff_sosp().longValueExact());
			penalitaVO.setFlCanc(penalita.getFl_canc()+"");
			penalitaVO.setGgSosp(penalita.getGg_sosp());
			penalitaVO.setTsIns(penalita.getTs_ins());
			penalitaVO.setTsVar(penalita.getTs_var());
			penalitaVO.setUteIns(penalita.getUte_ins());
			penalitaVO.setUteVar(penalita.getUte_var());
			penalitaVO.setGgTolleranza(penalita.getId_servizio().getMax_gg_cons());
		}

		return penalitaVO;
	}

	public static final RangeSegnatureVO daHibernateAWebSegnatura(Tbl_disponibilita_precatalogati segnatura) {
		RangeSegnatureVO segnaturaVO = new RangeSegnatureVO(segnatura.getId_disponibilita_precatalogati(),
									segnatura.getCod_segn(), segnatura.getSegn_inizio(),
									segnatura.getSegn_fine(), segnatura.getSegn_da(), segnatura.getSegn_a(),
									segnatura.getCod_frui().trim(), segnatura.getCod_no_disp().trim());
		segnaturaVO.setCodBiblioteca(segnatura.getCd_bib().getCd_biblioteca());
		segnaturaVO.setCodPolo(segnatura.getCd_bib().getCd_polo().getCd_polo());
		segnaturaVO.setNewSegnatura(false);
		segnaturaVO.setTsVar(segnatura.getTs_var());
		segnaturaVO.setTsIns(segnatura.getTs_ins());

		return segnaturaVO;
	}

	public static final ServizioBibliotecaVO daHibernateAWebServizio(Tbl_servizio servizio) {
		if (servizio == null)
			return null;
		ServizioBibliotecaVO servizioVO = new ServizioBibliotecaVO();

		servizioVO.setIdServizio(servizio.getId_servizio());
		servizioVO.setCodBib(servizio.getId_tipo_servizio().getCd_bib().getCd_biblioteca());
		servizioVO.setCodPolo(servizio.getId_tipo_servizio().getCd_bib().getCd_polo().getCd_polo());
		servizioVO.setCodServ(servizio.getCod_servizio());
		servizioVO.setCodTipoServ(servizio.getId_tipo_servizio().getCod_tipo_serv());
		servizioVO.setDescr(servizio.getDescr());
		servizioVO.setDurMaxRinn1(servizio.getDur_max_rinn1());
		servizioVO.setDurMaxRinn2(servizio.getDur_max_rinn2());
		servizioVO.setDurMaxRinn3(servizio.getDur_max_rinn3());
		servizioVO.setDurMov(servizio.getDur_mov());
		servizioVO.setFlCanc(""+servizio.getFl_canc());
		servizioVO.setMaxGgAnt(servizio.getMax_gg_ant());
		servizioVO.setMaxGgCons(servizio.getMax_gg_cons());
		servizioVO.setMaxGgDep(servizio.getMax_gg_dep());
		servizioVO.setNMaxGgvalPren(servizio.getN_max_ggval_pren().longValueExact());
		servizioVO.setNMaxPren(servizio.getN_max_pren().longValueExact());
		servizioVO.setNumMaxMov(servizio.getNum_max_mov());
		servizioVO.setNumMaxRich(servizio.getNum_max_rich());
		servizioVO.setNumMaxRiprod(servizio.getNum_max_riprod());
		servizioVO.setNumMaxPrenPosto(servizio.getN_max_pren_posto());
		servizioVO.setNumGgPrepSupp(servizio.getN_gg_prep_supp());
		servizioVO.setOrarioLimitePrepSupp(servizio.getTs_orario_limite_pren());
		servizioVO.setTsIns(servizio.getTs_ins());
		servizioVO.setTsVar(servizio.getTs_var());
		servizioVO.setUteIns(servizio.getUte_ins());
		servizioVO.setUteVar(servizio.getUte_var());

		servizioVO.setGiorniRestituzioneRichiedente(ValidazioneDati.coalesce(servizio.getN_gg_rest_ill(), (short)0));

		PenalitaServizioVO p = new PenalitaServizioVO();
		Tbl_penalita_servizio penalita = servizio.getTbl_penalita_servizio();
		if (penalita != null) {
			p.setCoeffSosp(new Long(penalita.getCoeff_sosp().longValueExact()));
			p.setGgSosp(penalita.getGg_sosp());
			p.setCodBib(servizio.getId_tipo_servizio().getCd_bib().getCd_biblioteca());
			p.setCodServ(servizio.getCod_servizio());
			p.setCodTipoServ(servizio.getId_tipo_servizio().getCod_tipo_serv());
			p.setFlCanc("" + penalita.getFl_canc());
			p.setTsIns(penalita.getTs_ins());
			p.setTsVar(penalita.getTs_var());
			p.setUteIns(penalita.getUte_ins());
			p.setUteVar(penalita.getUte_var());
		}

		servizioVO.setPenalita(p);

		servizioVO.setCodServizioILL(servizio.getId_tipo_servizio().getCd_iso_ill());

		try {
			servizioVO.setDescrTipoServ(CodiciProvider.cercaDescrizioneCodice(servizioVO.getCodTipoServ(),
					CodiciType.CODICE_TIPO_SERVIZIO,
					CodiciRicercaType.RICERCA_CODICE_SBN));
		} catch (Exception e) {}

		return servizioVO;
	}

	public static final ElementoSinteticaServizioVO daHibernateAWebServizioAutorizzazione(Trl_autorizzazioni_servizi hVO, int progressivo) {
		ElementoSinteticaServizioVO servizio = new ElementoSinteticaServizioVO();
		servizio.setProgressivo(progressivo);
		Tbl_servizio srv = hVO.getId_servizio();
		Tbl_tipo_servizio tipoSrv = srv.getId_tipo_servizio();
		Tbf_biblioteca_in_polo bib = tipoSrv.getCd_bib();

		servizio.setCodBiblioteca(bib.getCd_biblioteca());
		servizio.setCodPolo(bib.getCd_polo().getCd_polo());
		servizio.setTipServizio(tipoSrv.getCod_tipo_serv());
		servizio.setCodServizio(srv.getCod_servizio());
		servizio.setDesServizio(trimString(srv.getDescr()));
		servizio.setIdServizio(srv.getId_servizio());
		servizio.setCancella("");
		servizio.setStato(ElementoSinteticaServizioVO.NEW);
		servizio.setUteIns(hVO.getUte_ins());
		servizio.setUteVar(hVO.getUte_var());
		servizio.setTsIns(hVO.getTs_ins());
		servizio.setTsVar(hVO.getTs_var());
		servizio.setFlCanc(String.valueOf(hVO.getFl_canc()));
		servizio.setCodAut(hVO.getId_tipi_autorizzazione().getCod_tipo_aut());
		servizio.setDescrizioneTipoServizio(hVO.getId_tipi_autorizzazione().getDescr());
		return servizio;
	}

	// ########## DA WEB A HIBERNATE
	// ################################################

	public static final SollecitiVO daHibernateAWebSolleciti(Tbl_solleciti sollecito) {
		SollecitiVO webVO = new SollecitiVO();

		webVO.setProgrSollecito(sollecito.getProgr_sollecito());
		webVO.setCodRichServ(sollecito.getCod_rich_serv().getCod_rich_serv() );
		webVO.setDataInvio(sollecito.getData_invio());
		webVO.setFlCanc(String.valueOf(sollecito.getFl_canc()));
		webVO.setNote(sollecito.getNote());
		webVO.setTipoInvio(String.valueOf(sollecito.getTipo_invio()));
		webVO.setEsito(String.valueOf(sollecito.getEsito()));
		webVO.setTsIns(sollecito.getTs_ins());
		webVO.setTsVar(sollecito.getTs_var());
		webVO.setUteIns(sollecito.getUte_ins());
		webVO.setUteVar(sollecito.getUte_var());

		return webVO;
	}

	public static final SupportoBibliotecaVO daHibernateAWebSupportoBiblioteca(Tbl_supporti_biblioteca supporto)
	{
		SupportoBibliotecaVO supportoVO = new SupportoBibliotecaVO();
		supportoVO.setCd_bib(supporto.getCd_bib().getCd_biblioteca());
		supportoVO.setCd_polo(supporto.getCd_bib().getCd_polo().getCd_polo());
		supportoVO.setCodSupporto(supporto.getCod_supporto());
		supportoVO.setFlCanc(supporto.getFl_canc()+"");
		supportoVO.setId(supporto.getId_supporti_biblioteca());
		supportoVO.setImportoUnitario(supporto.getImp_unita().doubleValue());
		supportoVO.setCostoFisso(supporto.getCosto_fisso().doubleValue());
		supportoVO.setTsIns(supporto.getTs_ins());
		supportoVO.setTsVar(supporto.getTs_var());
		supportoVO.setUteIns(supporto.getUte_ins());
		supportoVO.setUteVar(supporto.getUte_var());

		//almaviva5_20161018 servizi ILL
		supportoVO.setFlSvolg(ValidazioneDati.coalesce(supporto.getFl_svolg(), 'L').toString());

		return supportoVO;
	}

	public static final RelazioniVO daHibernateAWebTabellaRelazioni(Trl_relazioni_servizi relazioneHib) {
		RelazioniVO relazioneVO = null;

		if (relazioneHib!=null) {
			relazioneVO = new RelazioniVO();
			relazioneVO.setId(relazioneHib.getId());
			relazioneVO.setCodBib(relazioneHib.getCd_bib().getCd_biblioteca());
			relazioneVO.setCodPolo(relazioneHib.getCd_bib().getCd_polo().getCd_polo());
			relazioneVO.setCodRelazione(relazioneHib.getCd_relazione());
			relazioneVO.setFlCanc(String.valueOf(relazioneHib.getFl_canc()));
			relazioneVO.setIdTabellaCodici(relazioneHib.getId_relazione_tb_codici());
			relazioneVO.setTabellaCodici(relazioneHib.getTabella_tb_codici());
			relazioneVO.setIdTabellaRelazione(relazioneHib.getId_relazione_tabella_di_relazione());
			relazioneVO.setTabellaRelazione(relazioneHib.getTabella_di_relazione());
			relazioneVO.setTsIns(relazioneHib.getTs_ins());
			relazioneVO.setTsVar(relazioneHib.getTs_var());
			relazioneVO.setUteIns(relazioneHib.getUte_ins());
			relazioneVO.setUteVar(relazioneHib.getUte_var());
		}

		return relazioneVO;
	}

	public static final TariffeModalitaErogazioneVO daHibernateAWebTariffeModalitaErogazione(Tbl_modalita_erogazione modalitaErogazione_tbl, Trl_tariffe_modalita_erogazione modalitaErogazione_trl) {
		TariffeModalitaErogazioneVO modalitaErogazioneVO = new TariffeModalitaErogazioneVO();

		modalitaErogazioneVO.setCodPolo(modalitaErogazione_trl.getId_tipo_servizio().getCd_bib().getCd_polo().getCd_polo());
		modalitaErogazioneVO.setCodBib(modalitaErogazione_trl.getId_tipo_servizio().getCd_bib().getCd_biblioteca());
		modalitaErogazioneVO.setCodErog(String.valueOf(modalitaErogazione_trl.getCod_erog()));
		modalitaErogazioneVO.setCodTipoServ(modalitaErogazione_trl.getId_tipo_servizio().getCod_tipo_serv());
		modalitaErogazioneVO.setFlCanc(String.valueOf(modalitaErogazione_trl.getFl_canc()));
		modalitaErogazioneVO.setTarBaseDouble(Double.valueOf(modalitaErogazione_tbl.getTar_base().doubleValue()));
		modalitaErogazioneVO.setCostoUnitarioDouble(Double.valueOf(modalitaErogazione_tbl.getCosto_unitario().doubleValue()));
		modalitaErogazioneVO.setTsIns(modalitaErogazione_trl.getTs_ins());
		modalitaErogazioneVO.setTsVar(modalitaErogazione_trl.getTs_var());
		modalitaErogazioneVO.setUteIns(modalitaErogazione_trl.getUte_ins());
		modalitaErogazioneVO.setUteVar(modalitaErogazione_trl.getUte_var());

		//almaviva5_20161018 servizi ILL
		modalitaErogazioneVO.setFlSvolg(ValidazioneDati.coalesce(modalitaErogazione_tbl.getFl_svolg(), 'L').toString());

		return modalitaErogazioneVO;
	}

	public static final SupportiModalitaErogazioneVO daHibernateAWebSupportiModalitaErogazione(Tbl_modalita_erogazione modalitaErogazione_tbl, Trl_supporti_modalita_erogazione modalitaErogazione_trl) {
		SupportiModalitaErogazioneVO modalitaErogazioneVO = new SupportiModalitaErogazioneVO();

		Tbl_supporti_biblioteca supporto = modalitaErogazione_trl.getId_supporti_biblioteca();
		modalitaErogazioneVO.setCodPolo(supporto.getCd_bib().getCd_polo().getCd_polo());
		modalitaErogazioneVO.setCodBib(supporto.getCd_bib().getCd_biblioteca());
		modalitaErogazioneVO.setCodErog(modalitaErogazione_trl.getCod_erog());
		modalitaErogazioneVO.setCodSupporto(supporto.getCod_supporto());
		modalitaErogazioneVO.setFlCanc(String.valueOf(modalitaErogazione_trl.getFl_canc()));
		modalitaErogazioneVO.setTarBaseDouble(Double.valueOf(modalitaErogazione_tbl.getTar_base().doubleValue()));
		modalitaErogazioneVO.setCostoUnitarioDouble(Double.valueOf(modalitaErogazione_tbl.getCosto_unitario().doubleValue()));
		modalitaErogazioneVO.setTsIns(modalitaErogazione_trl.getTs_ins());
		modalitaErogazioneVO.setTsVar(modalitaErogazione_trl.getTs_var());
		modalitaErogazioneVO.setUteIns(modalitaErogazione_trl.getUte_ins());
		modalitaErogazioneVO.setUteVar(modalitaErogazione_trl.getUte_var());

		//almaviva5_20161018 servizi ILL
		modalitaErogazioneVO.setFlSvolg(ValidazioneDati.coalesce(supporto.getFl_svolg(), 'L').toString());

		return modalitaErogazioneVO;
	}

	public static final ModalitaErogazioneVO daHibernateAWebModalitaErogazione(Tbl_modalita_erogazione modalitaErogazione) {
		ModalitaErogazioneVO modalitaErogazioneVO = new ModalitaErogazioneVO();

		modalitaErogazioneVO.setCodPolo(modalitaErogazione.getCd_bib().getCd_polo().getCd_polo());
		modalitaErogazioneVO.setCodBib(modalitaErogazione.getCd_bib().getCd_biblioteca());
		modalitaErogazioneVO.setCodErog(modalitaErogazione.getCod_erog());
		modalitaErogazioneVO.setFlCanc(String.valueOf(modalitaErogazione.getFl_canc()));
		modalitaErogazioneVO.setTarBaseDouble(Double.valueOf(modalitaErogazione.getTar_base().doubleValue()));
		modalitaErogazioneVO.setCostoUnitarioDouble(Double.valueOf(modalitaErogazione.getCosto_unitario().doubleValue()));
		modalitaErogazioneVO.setTsIns(modalitaErogazione.getTs_ins());
		modalitaErogazioneVO.setTsVar(modalitaErogazione.getTs_var());
		modalitaErogazioneVO.setUteIns(modalitaErogazione.getUte_ins());
		modalitaErogazioneVO.setUteVar(modalitaErogazione.getUte_var());

		//almaviva5_20161018 servizi ILL
		modalitaErogazioneVO.setFlSvolg(ValidazioneDati.coalesce(modalitaErogazione.getFl_svolg(), 'L').toString());

		return modalitaErogazioneVO;
	}

	public static final AutorizzazioniVO daHibernateAWebTipiAut(
			Tbl_tipi_autorizzazioni hibernateVO) throws Exception {
		AutorizzazioniVO webVO = new AutorizzazioniVO();
		// webVO.setProgressivo(progressivo);
		webVO.setAutorizzazione(hibernateVO.getDescr());
		webVO.setCodTipoAutor(hibernateVO.getCod_tipo_aut());
		webVO
				.setCodPoloAutor(hibernateVO.getCd_bib().getCd_polo()
						.getCd_polo());
		webVO.setCodBibAutor(hibernateVO.getCd_bib().getCd_biblioteca());
		webVO.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));
		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		return webVO;
	}

	public static final AutorizzazioneVO daHibernateAWebTipoAutorizzazione(
			Tbl_tipi_autorizzazioni hVO, int progressivo) {

		if (hVO == null)
			return null;

		AutorizzazioneVO webVO = new AutorizzazioneVO();
		webVO.setProgressivo(progressivo);
		webVO.setCodBiblioteca(hVO.getCd_bib().getCd_biblioteca());
		webVO.setCodPolo(hVO.getCd_bib().getCd_polo().getCd_polo());
		webVO.setIdAutorizzazione(hVO.getId_tipi_autorizzazione());
		webVO.setAutomaticoPer(String.valueOf(hVO.getFlag_aut()));
		webVO.setCodAutorizzazione(hVO.getCod_tipo_aut());
		webVO.setDesAutorizzazione(hVO.getDescr());

		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		//Imposto i servizi associati all'autorizzazione
		Set<Trl_autorizzazioni_servizi> listaServizi = hVO.getTrl_autorizzazioni_servizi();
		if (ValidazioneDati.isFilled(listaServizi) ) {
			Iterator<Trl_autorizzazioni_servizi> i = listaServizi.iterator();

			List<ElementoSinteticaServizioVO> listaServiziVO = new ArrayList<ElementoSinteticaServizioVO>();

			while (i.hasNext()) {
				Trl_autorizzazioni_servizi servizio = i.next();
				ElementoSinteticaServizioVO servizioVO = daHibernateAWebServizioAutorizzazione(servizio, 0);
				if (servizio.getFl_canc() != 'S') {
					listaServiziVO.add(servizioVO);
					try {
						String tipoSrv = servizioVO.getTipServizio();
						CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv, CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
						if (ts.isILL())
							webVO.setFl_svolg("I");
					} catch (Exception e) {}

				}

			}
			BaseVO.sortAndEnumerate(listaServiziVO, ElementoSinteticaServizioVO.ORDINAMENTO_PER_TIPO_SRV_DIRITTO);
			webVO.setListaServizi(listaServiziVO);
		}

		return webVO;
	}

	public static final ServizioWebDatiRichiestiVO daHibernateAWebModuloRichiesta(Tbl_servizio_web_dati_richiesti servizioWebDatiRichiesti) {
		ServizioWebDatiRichiestiVO servizioWebDatiRichiestiVO = new ServizioWebDatiRichiestiVO();

		if (servizioWebDatiRichiesti!=null) {

			Tbf_biblioteca_in_polo bib = servizioWebDatiRichiesti.getId_tipo_servizio().getCd_bib();
			servizioWebDatiRichiestiVO.setCodPolo(bib.getCd_polo().getCd_polo());
			servizioWebDatiRichiestiVO.setCodBib(bib.getCd_biblioteca());
			servizioWebDatiRichiestiVO.setCodiceTipoServizio(servizioWebDatiRichiesti.getId_tipo_servizio().getCod_tipo_serv());
			servizioWebDatiRichiestiVO.setCampoRichiesta(servizioWebDatiRichiesti.getCampo_richiesta());
			servizioWebDatiRichiestiVO.setObbligatorio(servizioWebDatiRichiesti.getObbligatorio()=='S' ? true : false );

			servizioWebDatiRichiestiVO.setTsIns(servizioWebDatiRichiesti.getTs_ins());
			servizioWebDatiRichiestiVO.setTsVar(servizioWebDatiRichiesti.getTs_var());
			servizioWebDatiRichiestiVO.setUteIns(servizioWebDatiRichiesti.getUte_ins());
			servizioWebDatiRichiestiVO.setUteVar(servizioWebDatiRichiesti.getUte_var());
			servizioWebDatiRichiestiVO.setFlCanc(""+servizioWebDatiRichiesti.getFl_canc());

			servizioWebDatiRichiestiVO.setUtilizzato(servizioWebDatiRichiesti.getFl_canc() == 'N');
		}
		return servizioWebDatiRichiestiVO;
	}


	public static final TipoServizioVO daHibernateAWebTipoServizio(Tbl_tipo_servizio hVO) {
		TipoServizioVO webVO = new TipoServizioVO();

		if (hVO != null) {
			webVO.setIdTipoServizio(hVO.getId_tipo_servizio());
			// tipoServizioVO.setCodaRichieste(tipoServizio.getCoda_richieste());
			webVO.setCodaRichieste((hVO.getCoda_richieste() == 'S'));

			webVO.setCodBib(hVO.getCd_bib().getCd_biblioteca());
			webVO.setCodiceTipoServizio(hVO.getCod_tipo_serv());
			webVO.setCodPolo(hVO.getCd_bib().getCd_polo().getCd_polo());

			webVO.setNumMaxMov(hVO.getNum_max_mov());

			if (hVO.getOre_ridis() < 23) {
				webVO.setOreRidis(hVO.getOre_ridis());
				webVO.setGgRidis(0);
			} else {
				webVO.setOreRidis(0);
				webVO.setGgRidis(hVO.getOre_ridis() / 24);
			}

			webVO.setPenalita((hVO.getPenalita() == 'S'));

			webVO.setIns_richieste_utente((hVO.getIns_richieste_utente() == 'S'));
			webVO.setAnche_da_remoto((hVO.getAnche_da_remoto() == 'S'));

			fillBaseWeb(hVO, webVO);

			//almaviva5_20180914 #6685
			webVO.setCodServizioILL(hVO.getCd_iso_ill());
		}

		return webVO;
	}

	public static final SpecTitoloStudioVO daHibernateAWebTitoloStudio(
			Tbl_specificita_titoli_studio hibernateVO, int progressivo) {

		SpecTitoloStudioVO webVO = new SpecTitoloStudioVO();
		webVO.setProgressivo(progressivo);
		webVO.setCodBiblioteca(hibernateVO.getCd_biblioteca()
				.getCd_biblioteca());
		webVO.setCodPolo(hibernateVO.getCd_biblioteca().getCd_polo()
				.getCd_polo());

		webVO.setIdTitoloStudio(hibernateVO.getId_specificita_titoli_studio());
		//webVO.setCodSpecialita(String.valueOf(hibernateVO.getSpecif_tit())); 22.12.09
		webVO.setCodSpecialita(hibernateVO.getSpecif_tit());
		webVO.setDesSpecialita(hibernateVO.getDescr());
		webVO.setTitoloStudio(String.valueOf(hibernateVO.getTit_studio()));

		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hibernateVO.getFl_canc()));

		return webVO;
	}

	public static final UtenteBibliotecaVO daHibernateAWebUteDettaglio(
			Trl_utenti_biblioteca utenteBib, List<Trl_diritti_utente> diritti,
			List<Trl_materie_utenti> materie, String numberFormat, Locale locale) throws Exception {
		UtenteBibliotecaVO webVO = new UtenteBibliotecaVO();
		Tbl_utenti utenteAna = utenteBib.getId_utenti();
		webVO.setIdUtente(String.valueOf(utenteAna.getId_utenti()));
		webVO.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));
		// polo/biblioteca utente
		webVO.setCodPolo(utenteAna.getCd_bib().getCd_polo().getCd_polo());
		webVO.setCodiceBiblioteca(utenteAna.getCd_bib().getCd_biblioteca());
		webVO.setCodiceUtente(utenteAna.getCod_utente());

		//19.01.10
		webVO.setChiaveUte(utenteAna.getChiave_ute());

		// polo/biblioteca servizio
		webVO.setCodBibSer(utenteBib.getCd_biblioteca().getCd_biblioteca());
		webVO.setCodPoloSer(utenteBib.getCd_biblioteca().getCd_polo().getCd_polo());

		AnagrafeVO anag = webVO.getAnagrafe();
		anag.setDataNascita(dateToString(utenteAna.getData_nascita()));
		anag.setCodFiscale(utenteAna.getCod_fiscale());

		//almaviva5_20150428
		anag.setPostaElettronica(ValidazioneDati.trimOrNull(utenteAna.getInd_posta_elettr()));
		anag.setPostaElettronica2(ValidazioneDati.trimOrNull(utenteAna.getInd_posta_elettr2()));

		anag.setNazionalita(utenteAna.getPaese_citt());
		ResidenzaVO residenza = anag.getResidenza();
		residenza.setProvincia(trimString(utenteAna.getProv_res()));

		webVO.setCognome(trimString(utenteAna.getCognome()));
		webVO.setNome(trimString(utenteAna.getNome()));

		webVO.setPassword(utenteAna.getPassword());
		webVO.setLastAccess(utenteAna.getLast_access());
		//almaviva5_20101203 #4036
		webVO.setChangePassword(String.valueOf(utenteAna.getChange_password()));
		//

		anag.setLuogoNascita(trimString(utenteAna.getLuogo_nascita()));
		if (ValidazioneDati.isFilled(utenteAna.getCod_proven()) )
			anag.setProvenienza(utenteAna.getCod_proven().toString());
		anag.setSesso(String.valueOf(utenteAna.getSesso()));

		String[] tipDoc = new String[4];
		if (ValidazioneDati.isFilled(utenteAna.getTipo_docum1()))
			tipDoc[0] = utenteAna.getTipo_docum1().toString();

		if (ValidazioneDati.isFilled(utenteAna.getTipo_docum2()))
			tipDoc[1] = utenteAna.getTipo_docum2().toString();

		if (ValidazioneDati.isFilled(utenteAna.getTipo_docum3()))
			tipDoc[2] = utenteAna.getTipo_docum3().toString();

		if (ValidazioneDati.isFilled(utenteAna.getTipo_docum4()))
			tipDoc[3] = utenteAna.getTipo_docum4().toString();

		String[] numDoc = new String[4];
		if (!ValidazioneDati.strIsNull(utenteAna.getNum_docum1())) {
			numDoc[0] = utenteAna.getNum_docum1();
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getNum_docum2())) {
			numDoc[1] = utenteAna.getNum_docum2();
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getNum_docum3())) {
			numDoc[2] = utenteAna.getNum_docum3();
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getNum_docum4())) {
			numDoc[3] = utenteAna.getNum_docum4();
		}

		String[] autRil = new String[4];
		if (!ValidazioneDati.strIsNull(utenteAna.getAut_ril_docum1())) {
			autRil[0] = trimString(utenteAna.getAut_ril_docum1());
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getAut_ril_docum2())) {
			autRil[1] = trimString(utenteAna.getAut_ril_docum2());
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getAut_ril_docum3())) {
			autRil[2] = trimString(utenteAna.getAut_ril_docum3());
		}
		if (!ValidazioneDati.strIsNull(utenteAna.getAut_ril_docum4())) {
			autRil[3] = trimString(utenteAna.getAut_ril_docum4());
		}
		List<DocumentoVO> documenti = new ArrayList<DocumentoVO>();
		for (int ind = 0; ind < 4; ind++)
			documenti.add(new DocumentoVO(tipDoc[ind], numDoc[ind], autRil[ind]));

		webVO.setDocumento(documenti);
		webVO.setTipoSMS(utenteAna.getAllinea() != null ? utenteAna.getAllinea() : UtenteBibliotecaVO.NO_SMS);
		IndirizzoVO domicilio = anag.getDomicilio();
		domicilio.setCap(utenteAna.getCap_dom());
		domicilio.setCitta(trimString(utenteAna.getCitta_dom()));
		domicilio.setFax(trimString(utenteAna.getFax_dom()));
		domicilio.setTelefono(trimString(utenteAna.getTel_dom()));
		domicilio.setVia(trimString(utenteAna.getIndirizzo_dom()));
		domicilio.setProvincia(utenteAna.getProv_dom());

		residenza.setCap(utenteAna.getCap_res());
		residenza.setCitta(trimString(utenteAna.getCitta_res()));
		residenza.setFax(trimString(utenteAna.getFax_res()));
		residenza.setTelefono(trimString(utenteAna.getTel_res()));
		residenza.setVia(trimString(utenteAna.getIndirizzo_res()));
		residenza.setProvincia(utenteAna.getProv_res());
		residenza.setNazionalita(utenteAna.getPaese_res());

		ProfessioniVO professione = webVO.getProfessione();
		professione.setAteneo(trimString(utenteAna.getCod_ateneo()));
		professione.setCorsoLaurea(trimString(utenteAna.getCorso_laurea()));
		professione.setMatricola(trimString(utenteAna.getCod_matricola()));
		professione.setReferente(trimString(utenteAna.getNome_referente()));
		professione.setPersonaGiuridica(utenteAna.getPersona_giuridica());

		if (ValidazioneDati.isFilled(utenteAna.getTipo_pers_giur()))
			professione.setTipoPersona(String.valueOf(utenteAna.getTipo_pers_giur()));

		BiblioPoloVO bp = webVO.getBibliopolo();
		if (utenteAna.getCredito_polo() != null)
			bp.setPoloCredito(utenteAna.getCredito_polo().doubleValue(), numberFormat, locale);

		if (utenteAna.getData_reg() != null)
			bp.setPoloDataRegistrazione(dateToString(utenteAna.getData_reg()));

		bp.setPoloInfrazioni(trimString(utenteAna.getNote()));
		bp.setPoloNote(trimString(utenteAna.getNote_polo()));
		bp.setCodBibXUteBib(utenteAna.getCod_bib());
		bp.setCodPoloXUteBib(utenteAna.getCod_polo_bib());

		//almaviva5_20110428 #4401
		bp.setCodiceAnagrafe(utenteAna.getCodice_anagrafe());

		AutorizzazioniVO autorizzazioni = webVO.getAutorizzazioni();
		autorizzazioni.setCodTipoAutor(utenteBib.getCod_tipo_aut());
		//  rox 15.01.10 inizio
		Tbl_tipi_autorizzazioni aut = new Tbl_tipi_autorizzazioni();
		if (!ValidazioneDati.strIsNull(autorizzazioni.getCodTipoAutor())) {
			aut = instance.autDAO.getTipoAutorizzazione(
					utenteBib.getCd_biblioteca().getCd_polo().getCd_polo(),
					utenteBib.getCd_biblioteca().getCd_biblioteca(),
					utenteBib.getCod_tipo_aut());
			if (aut != null) {
				autorizzazioni.setCodTipoAutor(aut.getCod_tipo_aut().trim());
				autorizzazioni.setAutorizzazione(aut.getDescr().trim());
				autorizzazioni.setIdAutor(aut.getId_tipi_autorizzazione());
				autorizzazioni.setCodBibAutor(utenteBib.getCd_biblioteca().getCd_polo().getCd_polo());
				autorizzazioni.setCodPoloAutor(utenteBib.getCd_biblioteca().getCd_biblioteca());
			}
		}
		//	  rox 15.01.10 fine
		if (utenteBib.getCredito_bib() != null)
			bp.setBiblioCredito(utenteBib.getCredito_bib().doubleValue(), numberFormat, locale);
		bp
				.setBiblioNote(trimString(utenteBib.getNote_bib()));
		if (utenteBib.getData_inizio_aut() != null) {
			bp.setInizioAuto(
					dateToString(utenteBib.getData_inizio_aut()));
		}
		if (utenteBib.getData_fine_aut() != null) {
			bp.setFineAuto(
					dateToString(utenteBib.getData_fine_aut()));
		}
		if (utenteBib.getData_inizio_sosp() != null) {
			bp.setInizioSosp(
					dateToString(utenteBib.getData_inizio_sosp()));
		}
		if (utenteBib.getData_fine_sosp() != null) {
			bp.setFineSosp(
					dateToString(utenteBib.getData_fine_sosp()));
		}
		if (utenteBib.getTs_ins() != null) {
			bp.setDataPrimaRegistr(dateToString(new Date(utenteBib.getTs_ins().getTime())));
		}

		// esame esistenza della sola professione 18.01.10
		if (ValidazioneDati.isFilled(utenteAna.getProfessione())) {
			professione.setProfessione(String.valueOf(utenteAna.getProfessione()));
		}
		// esame eseistenza solo titolo di studio 18.01.10
		if (ValidazioneDati.isFilled(utenteAna.getTit_studio())) {
			professione.setTitoloStudio(String.valueOf(utenteAna.getTit_studio()));
		}


		Tbl_occupazioni id_occupazioni = utenteBib.getId_occupazioni();

		if (id_occupazioni != null)
			professione.setIdOccupazione(String.valueOf(id_occupazioni.getId_occupazioni()));
		else
			professione.setIdOccupazione("0");
		//Tbl_utenti utenteAna = utenteBib.getId_utenti();
		Tbl_specificita_titoli_studio id_specificita_titoli_studio = utenteBib.getId_specificita_titoli_studio();
		if (id_specificita_titoli_studio != null)
			professione.setIdSpecTitoloStudio(
					String.valueOf(id_specificita_titoli_studio.getId_specificita_titoli_studio()));
		else
			professione.setIdSpecTitoloStudio("0");

		webVO.setUteIns(utenteBib.getUte_ins());
		webVO.setUteVar(utenteBib.getUte_var());
		webVO.setTsIns(utenteBib.getTs_ins());
		webVO.setTsVar(utenteBib.getTs_var());
		webVO.setFlCanc(String.valueOf(utenteBib.getFl_canc()));

		webVO.setUteInsAna(utenteAna.getUte_ins());
		webVO.setUteVarAna(utenteAna.getUte_var());
		webVO.setTsInsAna(utenteAna.getTs_ins());
		webVO.setTsVarAna(utenteAna.getTs_var());
		webVO.setFlCancAna(String.valueOf(utenteAna.getFl_canc()));

		if (diritti != null && diritti.size() > 0) {
			int p = 0;
			for (Trl_diritti_utente d : diritti) {
				ServizioVO servizio = daHibernateAWebDirittoUtente(d);
				servizio.setProgressivo(++p);
				autorizzazioni.getServizi().add(servizio);
			}
		}

		if (materie != null && materie.size() > 0) {
			int p = 0;
			for (Trl_materie_utenti m : materie) {
				MateriaVO materia = daHibernateAWebMateriaUtente(m);
				materia.setProgressivo(++p);
				professione.getMaterie().add(materia);
			}
		}

		Character cd_tipo_ute = utenteAna.getCd_tipo_ute();
		webVO.setTipoUtente(cd_tipo_ute == null ? Servizi.Utenti.UTENTE_TIPO_SBNWEB : cd_tipo_ute.toString() );

		return webVO;
	}

	public static final UtenteBaseVO daHibernateAWebUtente(Tbl_utenti hVO) {
		if (hVO == null)
			return null;

		UtenteBaseVO webVO = new UtenteBaseVO();
		Tbf_biblioteca_in_polo bib = hVO.getCd_bib();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());
		webVO.setCodiceFiscale(hVO.getCod_fiscale());
		webVO.setCodUtente(hVO.getCod_utente());
		webVO.setCognome(hVO.getCognome());
		webVO.setDataNascita(hVO.getData_nascita());
		webVO.setLuogoNascita(hVO.getLuogo_nascita());
		webVO.setNome(hVO.getNome());
		webVO.setSesso(hVO.getSesso());

		//almaviva5_20150430
		webVO.setMail1(hVO.getInd_posta_elettr());
		webVO.setMail2(hVO.getInd_posta_elettr2());

		webVO.setIdUtente(hVO.getId_utenti());
		webVO.setPassword(hVO.getPassword());
		webVO.setPersonaGiuridica(Optional.ofNullable(hVO.getPersona_giuridica()).orElse(' ').toString());
		webVO.setTipoEnte(Optional.ofNullable(hVO.getTipo_pers_giur()).orElse(' ').toString());

		webVO.setOrdUtente(hVO.getChiave_ute());

		Character cd_tipo_ute = hVO.getCd_tipo_ute();
		webVO.setTipoUtente(cd_tipo_ute == null ? Servizi.Utenti.UTENTE_TIPO_SBNWEB : cd_tipo_ute.toString() );

		return webVO;
	}

	public static final UtenteBibliotecaVO daHibernateAWebUtenteBase(
			Tbl_utenti hVO, List<Trl_diritti_utente> diritti,
			List<Trl_materie_utenti> materie,String format, Locale locale) throws Exception {
		if (hVO == null)
			return null;

		UtenteBibliotecaVO webVO = new UtenteBibliotecaVO();
		// da impostare da chi riceve
		//webVO.setCodBibSer();
		//webVO.setCodPoloSer();
		webVO.setIdUtente(String.valueOf(hVO.getId_utenti()));
		//webVO.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));
		// polo/biblioteca utente
		Tbf_biblioteca_in_polo bib = hVO.getCd_bib();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodiceBiblioteca(bib.getCd_biblioteca());
		webVO.setCodiceUtente(hVO.getCod_utente());

		// polo/biblioteca servizio
		//webVO.setCodBibSer(utenteBib.getCd_biblioteca().getCd_biblioteca());
		//webVO.setCodPoloSer(utenteBib.getCd_biblioteca().getCd_polo().getCd_polo());

		AnagrafeVO anag = webVO.getAnagrafe();
		anag.setDataNascita(dateToString(hVO.getData_nascita()));
		anag.setCodFiscale(hVO.getCod_fiscale());

		anag.setPostaElettronica("");
		//almaviva5_20150428
		anag.setPostaElettronica(ValidazioneDati.trimOrNull(hVO.getInd_posta_elettr()));
		anag.setPostaElettronica2(ValidazioneDati.trimOrNull(hVO.getInd_posta_elettr2()));

		//webVO.getAnagrafe().setPostaElettronica(trimString(hibernateVO.getInd_posta_elettr()));
		anag.setNazionalita(hVO.getPaese_citt());
		anag.getResidenza().setProvincia(trimString(hVO.getProv_res()));

		webVO.setCognome(trimString(hVO.getCognome()));
		webVO.setNome(trimString(hVO.getNome()));

		webVO.setPassword(hVO.getPassword());

		anag.setCodFiscale(hVO.getCod_fiscale());
		anag.setLuogoNascita(trimString(hVO.getLuogo_nascita()));
		if (ValidazioneDati.isFilled(hVO.getCod_proven()) )
			anag.setProvenienza(hVO.getCod_proven().toString());
		anag.setSesso(String.valueOf(hVO.getSesso()));

		String[] tipDoc = new String[4];
		if (ValidazioneDati.isFilled(hVO.getTipo_docum1()))
			tipDoc[0] = hVO.getTipo_docum1().toString();

		if (ValidazioneDati.isFilled(hVO.getTipo_docum2()))
			tipDoc[1] = hVO.getTipo_docum2().toString();

		if (ValidazioneDati.isFilled(hVO.getTipo_docum3()))
			tipDoc[2] = hVO.getTipo_docum3().toString();

		if (ValidazioneDati.isFilled(hVO.getTipo_docum4()))
			tipDoc[3] = hVO.getTipo_docum4().toString();

		String[] numDoc = new String[4];
		if (!ValidazioneDati.strIsNull(hVO.getNum_docum1())) {
			numDoc[0] = hVO.getNum_docum1();
		}
		if (!ValidazioneDati.strIsNull(hVO.getNum_docum2())) {
			numDoc[1] = hVO.getNum_docum2();
		}
		if (!ValidazioneDati.strIsNull(hVO.getNum_docum3())) {
			numDoc[2] = hVO.getNum_docum3();
		}
		if (!ValidazioneDati.strIsNull(hVO.getNum_docum4())) {
			numDoc[3] = hVO.getNum_docum4();
		}

		String[] autRil = new String[4];
		if (!ValidazioneDati.strIsNull(hVO.getAut_ril_docum1())) {
			autRil[0] = trimString(hVO.getAut_ril_docum1());
		}
		if (!ValidazioneDati.strIsNull(hVO.getAut_ril_docum2())) {
			autRil[1] = trimString(hVO.getAut_ril_docum2());
		}
		if (!ValidazioneDati.strIsNull(hVO.getAut_ril_docum3())) {
			autRil[2] = trimString(hVO.getAut_ril_docum3());
		}
		if (!ValidazioneDati.strIsNull(hVO.getAut_ril_docum4())) {
			autRil[3] = trimString(hVO.getAut_ril_docum4());
		}
		List<DocumentoVO> documenti = new ArrayList<DocumentoVO>();
		for (int ind = 0; ind < 4; ind++) {
			documenti.add(new DocumentoVO(tipDoc[ind], numDoc[ind], autRil[ind]));
		}
		webVO.setDocumento(documenti);
		webVO.setTipoSMS(hVO.getAllinea()!=null ? hVO.getAllinea() : UtenteBibliotecaVO.NO_SMS);
		anag.getDomicilio().setCap(hVO.getCap_dom());
		anag.getDomicilio().setCitta(trimString(hVO.getCitta_dom()));
		anag.getDomicilio().setFax(hVO.getFax_dom());
		anag.getDomicilio().setTelefono(hVO.getTel_dom());
		anag.getDomicilio().setVia(trimString(hVO.getIndirizzo_dom()));
		anag.getDomicilio().setProvincia(hVO.getProv_dom());
		anag.getResidenza().setCap(hVO.getCap_res());
		anag.getResidenza().setCitta(trimString(hVO.getCitta_res()));
		anag.getResidenza().setFax(hVO.getFax_res());
		anag.getResidenza().setTelefono(hVO.getTel_res());
		anag.getResidenza().setVia(trimString(hVO.getIndirizzo_res()));
		anag.getResidenza().setProvincia(hVO.getProv_res());
		anag.getResidenza().setNazionalita(hVO.getPaese_res());

		// esame esistenza della sola professione 18.01.10
		if (ValidazioneDati.isFilled(hVO.getProfessione())) {
			webVO.getProfessione().setProfessione(String.valueOf(hVO.getProfessione()));
		}
		// esame esistenza solo titolo di studio 18.01.10
		if (ValidazioneDati.isFilled(hVO.getTit_studio())) {
			webVO.getProfessione().setTitoloStudio(String.valueOf(hVO.getTit_studio()));
		}
		webVO.getProfessione().setAteneo(trimString(hVO.getCod_ateneo()));
		webVO.getProfessione().setCorsoLaurea(hVO.getCorso_laurea());
		webVO.getProfessione().setMatricola(hVO.getCod_matricola());
		webVO.getProfessione().setReferente(trimString(hVO.getNome_referente()));
		webVO.getProfessione().setPersonaGiuridica(hVO.getPersona_giuridica());

		if (ValidazioneDati.isFilled(hVO.getTipo_pers_giur()))
			webVO.getProfessione().setTipoPersona(String.valueOf(hVO.getTipo_pers_giur()));
		if (hVO.getCredito_polo() != null)
			webVO.getBibliopolo().setPoloCredito(hVO.getCredito_polo().doubleValue(), format, locale);

		if (hVO.getData_reg() != null)
			webVO.getBibliopolo().setPoloDataRegistrazione(dateToString(hVO.getData_reg()));

		webVO.getBibliopolo().setPoloInfrazioni(trimString(hVO.getNote()));
		webVO.getBibliopolo().setPoloNote(trimString(hVO.getNote_polo()));
		webVO.getBibliopolo().setCodBibXUteBib(hVO.getCod_bib());
		webVO.getBibliopolo().setCodPoloXUteBib(hVO.getCod_polo_bib());


		if (ValidazioneDati.isFilled(diritti) ) {
			int p = 0;
			for (Trl_diritti_utente d : diritti) {
				ServizioVO servizio = daHibernateAWebDirittoUtente(d);
				servizio.setProgressivo(++p);
				webVO.getAutorizzazioni().getServizi().add(servizio);
			}
		}
		if (ValidazioneDati.isFilled(materie) ) {
			int p = 0;
			for (Trl_materie_utenti m : materie) {
				MateriaVO materia = daHibernateAWebMateriaUtente(m);
				materia.setProgressivo(++p);
				webVO.getProfessione().getMaterie().add(materia);
			}
		}

		// aggiunta il 15.02.10
		webVO.setLastAccess(hVO.getLast_access());
		webVO.setChiaveUte(hVO.getChiave_ute());

		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));
		webVO.setFlCancAna(String.valueOf(hVO.getFl_canc())); // 16.02.2010

		Character cd_tipo_ute = hVO.getCd_tipo_ute();
		webVO.setTipoUtente(cd_tipo_ute == null ? Servizi.Utenti.UTENTE_TIPO_SBNWEB : cd_tipo_ute.toString() );

		return webVO;
	}

	public static final AnagrafeUtenteProfessionaleVO daHibernateAWebUtenteProfessionale(Tbf_anagrafe_utenti_professionali hVO) {
		AnagrafeUtenteProfessionaleVO webVO = new AnagrafeUtenteProfessionaleVO();

		webVO.setCognome(hVO.getCognome());
		webVO.setFlCanc(hVO.getFl_canc()+"");
		webVO.setIdUtenteProfessionale(hVO.getId_utente_professionale());
		webVO.setNome(hVO.getNome());
		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());

		//almaviva5_20150216
		webVO.setUserId(hVO.getTbf_utenti_professionali_web().getUserid());

		return webVO;
	}

	public static final SinteticaUtenteVO daHibernateAWebUteRicerca(Trl_utenti_biblioteca uteBib) throws Exception {
		Tbl_tipi_autorizzazioni aut = new Tbl_tipi_autorizzazioni();
		SinteticaUtenteVO webVO = new SinteticaUtenteVO();
		Tbl_utenti ute = uteBib.getId_utenti();
		if (ute != null) {
			webVO.setIdUtente(uteBib.getId_utenti_biblioteca());
			//almaviva5_20120229 #4682
			webVO.setIdUtenteAna(ute.getId_utenti());
		} else
			return webVO;

		if (ute != null && ute.getCd_bib() != null) {
			webVO.setPolo(ute.getCd_bib().getCd_polo().getCd_polo());
			webVO.setBiblioteca(ute.getCd_bib().getCd_biblioteca());
		}
		webVO.setCodice(ute.getCod_utente());

		//errore: polo di utebiblio identifica per l'utente-biblioteca il codice identificativo del polo della biblioteca corrispondente
		//webVO.setPoloDiUteBiblio(uteBib.getCd_biblioteca().getCd_polo().getCd_polo());
		//webVO.setBiblioDiUteBiblio(uteBib.getCd_biblioteca().getCd_biblioteca());

		// correzione rox 10.11.09 misunderstanding biblio e polo di utebiblio ovvero per utente-biblioteca introdotta bib e polo erogante

		webVO.setPoloErogante(uteBib.getCd_biblioteca().getCd_polo().getCd_polo());
		webVO.setBibErogante(uteBib.getCd_biblioteca().getCd_biblioteca());

		webVO.setPoloDiUteBiblio(ute.getCod_polo_bib());
		webVO.setBiblioDiUteBiblio(ute.getCod_bib());

		webVO.setDescrizione(trimString(ute.getCognome()) + " "
				+ trimString(ute.getNome()));
		webVO.setIndirizzo(trimString(ute.getIndirizzo_dom()));
//		webVO.setCitta(ute.getCitta_dom() + " " + ute.getProv_dom());
		webVO.setCitta(ute.getCitta_dom());
		webVO.setProvincia(ute.getProv_dom());
		webVO.setTelefono(ute.getTel_dom());
		webVO.setAteneo(ute.getCod_ateneo());
		webVO.setMatricola(ute.getCod_matricola());
		webVO.setLuogoNascita(ute.getLuogo_nascita());
		webVO.setCittaRes(ute.getCitta_res());
		webVO.setCap(ute.getCap_res());

		if (ute.getData_nascita() != null) {
			webVO.setDataNascita(dateToString(ute.getData_nascita()));
		}
		if (webVO.getDataNascita().equals("01/01/9999"))
		{
			webVO.setDataNascita("");
		}
		webVO.setEmail(ute.getInd_posta_elettr());
		webVO.setCodiceAutorizzazione(uteBib.getCod_tipo_aut());
		if (!ValidazioneDati.strIsNull(uteBib.getCod_tipo_aut())) {
			aut = instance.autDAO.getTipoAutorizzazione(
				uteBib.getCd_biblioteca().getCd_polo().getCd_polo(),
				uteBib.getCd_biblioteca().getCd_biblioteca(),
				uteBib.getCod_tipo_aut());
			if (aut != null) {
				webVO.setDescrizioneAutorizzazione(aut.getDescr());
				webVO.setIdAutorizzazione(aut.getId_tipi_autorizzazione());
				webVO.setFlgAutorizzazione(String.valueOf(aut.getFlag_aut()));
			}
		}
		if (uteBib.getData_fine_aut() != null) {
			webVO.setScadenzaAutorizzazione(dateToString(uteBib.getData_fine_aut()));
		}
		if (ute.getPersona_giuridica() != null) {
			webVO.setTipo(ute.getPersona_giuridica().toString());
		} else {
			webVO.setTipo(null);
		}

		//almaviva5_20110427 #4398
		webVO.setFlCanc(ValidazioneDati.isFilled(uteBib.getFl_canc()) ? String.valueOf(uteBib.getFl_canc()) : "S");

		return webVO;
	}

	public static final Tbl_tipi_autorizzazioni daWebAHibernateAnagrafeAutorizzazioni(AutorizzazioneVO recAutorizzazione)
		throws Exception {
		Tbl_tipi_autorizzazioni hibernateVO = null;

		if (recAutorizzazione.getIdAutorizzazione()>0) {
			hibernateVO = instance.autDAO.getTipoAutorizzazioneById(recAutorizzazione.getIdAutorizzazione());
		}
		else {
			hibernateVO = new Tbl_tipi_autorizzazioni();
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(recAutorizzazione.getCodPolo(),
						recAutorizzazione.getCodBiblioteca());
			hibernateVO.setCd_bib(bib);
			hibernateVO.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione());
		}


		hibernateVO.setDescr(recAutorizzazione.getDesAutorizzazione());
		hibernateVO.setFlag_aut(' ');
		if (!ValidazioneDati.strIsNull(recAutorizzazione.getAutomaticoPer()))
		{
			hibernateVO.setFlag_aut(recAutorizzazione.getAutomaticoPer().charAt(0));
		}
		hibernateVO.setUte_ins(recAutorizzazione.getUteIns());
		hibernateVO.setTs_ins(recAutorizzazione.getTsIns());
		hibernateVO.setUte_var(recAutorizzazione.getUteVar());
		hibernateVO.setTs_var(recAutorizzazione.getTsVar());
		if (recAutorizzazione.getFlCanc()!=null)
			hibernateVO.setFl_canc(recAutorizzazione.getFlCanc().charAt(0));
		else hibernateVO.setFl_canc('N');

		return hibernateVO;
	}

	public static final Tbl_controllo_iter daWebAHibernateControlloIter(FaseControlloIterVO controlloIterVO, int idTipoServizio, String codAttivita, boolean nuovo)
	throws Exception {
		Tbl_controllo_iter controlloIter = null;
		if (nuovo){
			controlloIter = new Tbl_controllo_iter();
			Tbl_iter_servizio iterServizio = instance.iterDAO.getIterServizio(idTipoServizio, codAttivita);

			controlloIter.setCod_controllo(controlloIterVO.getCodControllo());
			controlloIter.setId_iter_servizio(iterServizio);
			controlloIter.setTs_ins(new Timestamp(controlloIterVO.getDataIns().getTime()));
			controlloIter.setTs_var(new Timestamp(controlloIterVO.getDataAgg().getTime()));

		} else {
			controlloIter = instance.iterDAO.getControlloIter(idTipoServizio, codAttivita, controlloIterVO.getCodControllo(), "N");
		}

		controlloIter.setFl_canc(controlloIterVO.getFlCanc()!=null ? controlloIterVO.getFlCanc().charAt(0) : 'N');
		controlloIter.setFl_bloccante(controlloIterVO.isFlagBloc() ? 'S' : 'N');
		controlloIter.setMessaggio(controlloIterVO.getMessaggio());
		controlloIter.setUte_ins(controlloIterVO.getUteIns());
		controlloIter.setUte_var(controlloIterVO.getUteVar());

		return controlloIter;
	}

	public static final Tbf_biblioteca_default daWebAHibernateDefaultBiblioteca(
			String value, Tbf_biblioteca_in_polo bibInPolo, Tbf_default defDB) {

		Tbf_biblioteca_default hibernateVO = new Tbf_biblioteca_default();

		hibernateVO.setCd_biblioteca(bibInPolo);
		hibernateVO.setId_default(defDB);
		hibernateVO.setValue(value);
		return hibernateVO;
	}

	public static final Tbf_bibliotecario_default daWebAHibernateDefaultUtente(
			String value, Tbf_bibliotecario utenteWeb, Tbf_default defDB) {

		Tbf_bibliotecario_default hibernateVO = new Tbf_bibliotecario_default();

		hibernateVO.setId_utente_professionale(utenteWeb);
		hibernateVO.setId_default(defDB);
		hibernateVO.setValue(value);
		return hibernateVO;
	}

	public static final Trl_diritti_utente daWebAHibernateDirittoUtente(ServizioVO webVO,
			Trl_diritti_utente oldDiritto) {
		Trl_diritti_utente hibernateVO = null;
		if (oldDiritto == null) {
			hibernateVO = new Trl_diritti_utente();
			hibernateVO.setFl_canc('N');

			Tbl_servizio servizio = UtilitaDAO.creaIdServizio(webVO
					.getCodPolo(), webVO.getCodBib(), webVO.getCodice(), webVO
					.getServizio());
			hibernateVO.setId_servizio(servizio);

		} else
			hibernateVO = oldDiritto;
		hibernateVO.setData_inizio_serv(stringToDate(webVO.getDataInizio()));
		hibernateVO.setData_fine_serv(stringToDate(webVO.getDataFine()));
		hibernateVO
				.setData_inizio_sosp(stringToDate(webVO.getSospDataInizio()));
		hibernateVO.setData_fine_sosp(stringToDate(webVO.getSospDataFine()));
		hibernateVO.setNote(trimString(webVO.getNote()));

		if (webVO.getUteVar() != null)
			hibernateVO.setUte_var(webVO.getUteVar()); // tck 3880

		if (webVO.getUteIns() != null)
			hibernateVO.setUte_ins(webVO.getUteIns()); // tck 3880

		hibernateVO.setCod_tipo_aut(webVO.getFlag_aut_ereditato()); //20.01.10
		Timestamp ts = DaoManager.now();
		if (webVO.getTsIns() == null)
			hibernateVO.setTs_ins(ts);
		else
			hibernateVO.setTs_ins(webVO.getTsIns());
//		hibernateVO.setTs_var(ts);
		if (!ValidazioneDati.strIsNull(webVO.getFlCanc()))
			hibernateVO.setFl_canc(webVO.getFlCanc().charAt(0));

		return hibernateVO;
	}

	public static final Tbl_materie daWebAHibernateMateria(MateriaVO materiaVO, boolean nuovo)
	throws Exception {
		Tbl_materie materia = new Tbl_materie();
		if (nuovo) {
			materia.setCd_biblioteca(instance.utilDAO.getBibliotecaInPolo(materiaVO.getCodPolo(), materiaVO.getCodBib()));
			materia.setCod_mat(materiaVO.getCodice());
			materia.setTs_ins(materiaVO.getTsIns());
			materia.setUte_ins(materiaVO.getUteIns());
		} else {
			materia = instance.matDAO.getMateriaBibliotecaById(materiaVO.getIdMateria());
		}
		materia.setDescr(materiaVO.getDescrizione());
		materia.setFl_canc((materiaVO.getFlCanc()!=null ? materiaVO.getFlCanc().charAt(0) : 'N'));
		materia.setUte_var(materiaVO.getUteVar());

		return materia;
	}

	public static final Trl_materie_utenti daWebAHibernateMateriaUtente(MateriaVO webVO,
			Trl_materie_utenti oldMateria) {
		Trl_materie_utenti hibernateVO = null;
		if (oldMateria == null)
			hibernateVO = new Trl_materie_utenti();
		else
			hibernateVO = oldMateria;
		hibernateVO.setUte_ins(webVO.getUteIns());
		hibernateVO.setUte_var(webVO.getUteVar());
		Timestamp ts = DaoManager.now();
		if (webVO.getTsIns() == null)
			hibernateVO.setTs_ins(ts);
		else
			hibernateVO.setTs_ins(webVO.getTsIns());
//		hibernateVO.setTs_var(ts);
		if (!ValidazioneDati.strIsNull(webVO.getFlCanc()))
			hibernateVO.setFl_canc(webVO.getFlCanc().charAt(0));
		return hibernateVO;
	}

	public static final Tbl_materie daWebAHibernateMaterieRicerca(RicercaMateriaVO webVO)
			throws Exception {
		Tbl_materie hibernateVO = new Tbl_materie();

		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO.getCodPolo(), webVO.getCodBib());
		hibernateVO.setCd_biblioteca(bib);
		hibernateVO.setCod_mat(webVO.getCodice());
		hibernateVO.setDescr(trimString(webVO.getDescrizione()));
		return hibernateVO;
	}

	public static final Tbl_richiesta_servizio daWebAHibernateMovimento(MovimentoVO webVO, int idServizio)
	throws Exception {

		Tbl_richiesta_servizio hVO = new Tbl_richiesta_servizio();
		long cod_rich_serv = webVO.getIdRichiesta();
		ConversioneHibernateVO.toHibernate().assignEntityID(hVO, "cod_rich_serv", cod_rich_serv);

		try {
			Tbl_servizio servizio            = instance.srvDAO.getServizioBibliotecaById(idServizio);
			Tbl_iter_servizio iterServizio   = instance.iterDAO.getIterServizio(servizio.getId_tipo_servizio(), webVO.getCodAttivita());
			String codPolo = webVO.getCodPolo();
			String codBibOperante = webVO.getCodBibOperante();
			Tbl_supporti_biblioteca supporto = instance.suppDAO.getSupportoBiblioteca(codPolo, codBibOperante, webVO.getCodSupporto(), "N");


			Trl_utenti_biblioteca utenteBiblioteca =
				instance.utentiDAO.getUtenteBiblioteca(codPolo, webVO.getCodBibUte(), webVO.getCodUte(), codBibOperante);

			hVO.setData_richiesta(webVO.getDataRichiesta());
			hVO.setBid(webVO.getBid());

			if (!ValidazioneDati.strIsNull(webVO.getCodRisp()))
				hVO.setCod_risp(webVO.getCodRisp().charAt(0));
			if (!ValidazioneDati.strIsNull(webVO.getCopyright()))
				hVO.setCopyright(webVO.getCopyright().charAt(0));
			if (!ValidazioneDati.strIsNull(webVO.getFlCondiz()))
				hVO.setFl_condiz(webVO.getFlCondiz().charAt(0));
			if (!ValidazioneDati.strIsNull(webVO.getFlSvolg()))
				hVO.setFl_svolg(webVO.getFlSvolg().charAt(0));

			hVO.setId_utenti_biblioteca(utenteBiblioteca);

			hVO.setTs_ins(webVO.getTsIns());
			hVO.setUte_ins(webVO.getUteIns());
			hVO.setTs_var(webVO.getTsVar());
			hVO.setUte_var(webVO.getUteVar());

			PrenotazionePostoVO pp = webVO.getPrenotazionePosto();
			if (pp != null) {
				final Tbl_prenotazione_posto tbl_prenotazione_posto = pp.isNuovo() ? null : instance.saleDAO.getPrenotazionePostoById(pp.getId_prenotazione());
				hVO.getPrenotazioni_posti().add(ConvertToHibernate.Sale.prenotazione(tbl_prenotazione_posto, pp));
			}

			if (webVO.isRichiestaSuInventario() ) {
				Tbc_inventario inventario = instance.invDAO.getInventario(codPolo, webVO.getCodBibInv(), webVO.getCodSerieInv(), new Integer(webVO.getCodInvenInv()));
				hVO.setCd_polo_inv(inventario);
			} else {
				Tbl_esemplare_documento_lettore esDocLett = instance.docDAO.getEsemplareDocumentoLettore(codPolo, webVO.getCodBibDocLett(), webVO.getTipoDocLett(), new Long(webVO.getCodDocLet()), new Short(webVO.getProgrEsempDocLet()));
				if (esDocLett != null) {
					hVO.setId_esemplare_documenti_lettore(esDocLett);
					hVO.setId_documenti_lettore(esDocLett.getId_documenti_lettore());
				}
			}

			if (!ValidazioneDati.strIsNull(webVO.getAnnoPeriodico()))
				hVO.setAnno_period(new BigDecimal(webVO.getAnnoPeriodico()));
			hVO.setNum_volume(webVO.getNumVolume());

			hVO.setNum_fasc(webVO.getNumFascicolo());
			hVO.setId_servizio(servizio);
			hVO.setId_iter_servizio(iterServizio);
			hVO.setId_supporti_biblioteca(supporto);
			hVO.setCod_bib_dest(webVO.getCodBibDest());
			hVO.setCod_bib_prelievo(webVO.getCodBibPrelievo());
			hVO.setCod_bib_restituzione(webVO.getCodBibRestituzione());
			hVO.setData_fine_eff(webVO.getDataFineEff());
			hVO.setData_fine_prev(webVO.getDataFinePrev());
			hVO.setData_in_eff(webVO.getDataInizioEff());
			hVO.setData_in_prev(webVO.getDataInizioPrev());
			hVO.setData_massima(webVO.getDataMax());
			hVO.setData_proroga(webVO.getDataProroga());
			hVO.setNote_bibliotecario(webVO.getNoteBibliotecario());
			hVO.setNote_ut(webVO.getNoteUtente());
			hVO.setCod_attivita(webVO.getCodAttivita());
			hVO.setCod_tipo_serv_rich(webVO.getCodTipoServRich());
			hVO.setFl_canc((!ValidazioneDati.strIsNull(webVO.getFlCanc())) ? webVO.getFlCanc().charAt(0) : 'N');
			hVO.setNum_rinnovi(new Short(webVO.getNumRinnovi()));

			if (!ValidazioneDati.strIsNull(webVO.getCodStatoMov()))
				hVO.setCod_stato_mov(webVO.getCodStatoMov().charAt(0));
			if (!ValidazioneDati.strIsNull(webVO.getCodStatoRic()))
				hVO.setCod_stato_rich(webVO.getCodStatoRic().charAt(0));
			if (!ValidazioneDati.strIsNull(webVO.getCod_erog()))
				hVO.setCod_erog(webVO.getCod_erog());
			if (!ValidazioneDati.strIsNull(webVO.getCostoServizio()))
				hVO.setCosto_servizio(new BigDecimal(webVO.getCostoServizioDouble()));
			if (!ValidazioneDati.strIsNull(webVO.getNumPezzi()))
				hVO.setNum_pezzi(new Short(webVO.getNumPezzi()));
			if (!ValidazioneDati.strIsNull(webVO.getPrezzoMax()))
				hVO.setPrezzo_max(new BigDecimal(webVO.getPrezzoMaxDouble()));
			if (webVO.getFlTipoRec() != null)
				hVO.setFl_tipo_rec(webVO.getFlTipoRec().getFlagValue());

			hVO.setIntervallo_copia(webVO.getIntervalloCopia());

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return hVO;
	}



	public static final Tbl_occupazioni daWebAHibernateOccupazione(OccupazioneVO occVO, boolean nuovo)
	throws Exception {
		Tbl_occupazioni occupazione = new Tbl_occupazioni();
		if (nuovo) {
			occupazione.setCd_biblioteca(instance.utilDAO.getBibliotecaInPolo(occVO.getCodPolo(), occVO.getCodBiblioteca()));
			occupazione.setProfessione(occVO.getProfessione().charAt(0));
			occupazione.setOccupazione(occVO.getCodOccupazione());  // 22.12.09 .charAt(0)
			occupazione.setTs_ins(occVO.getTsIns());
			occupazione.setUte_ins(occVO.getUteIns());
		} else {
			occupazione = instance.occDAO.getOccupazioneById(occVO.getIdOccupazioni());
		}
		occupazione.setDescr(occVO.getDesOccupazione());
		occupazione.setFl_canc((occVO.getFlCanc()!=null ? occVO.getFlCanc().charAt(0) : 'N'));
		occupazione.setUte_var(occVO.getUteVar());

		return occupazione;
	}


	public static final Tbl_occupazioni daWebAHibernateOccupazioneRicerca(
			RicercaOccupazioneVO webVO) {
		Tbl_occupazioni hibernateVO = new Tbl_occupazioni();

		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO.getCodPolo(), webVO.getCodBib());
		hibernateVO.setCd_biblioteca(bib);
		hibernateVO.setDescr(webVO.getDesOccupazione());
		if (ValidazioneDati.strIsNull(webVO.getCodOccupazione()))
			hibernateVO.setOccupazione("  "); // 22.12.09  NULL_CHAR
		else
			hibernateVO.setOccupazione(webVO.getCodOccupazione()); // 22.12.09 .charAt(0)
		if (ValidazioneDati.strIsNull(webVO.getProfessione()))
			hibernateVO.setProfessione(NULL_CHAR);
		else
			hibernateVO.setProfessione(webVO.getProfessione().charAt(0));

		if (webVO.getId_occupazioni()>0)
			hibernateVO.setId_occupazioni(webVO.getId_occupazioni());

		return hibernateVO;
	}





	public static final Tbl_tipi_autorizzazioni daWebAHibernateRicercaTipiAut(
			RicercaAutorizzazioneVO webVO) {
		Tbl_tipi_autorizzazioni hibernateVO = new Tbl_tipi_autorizzazioni();

		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO
				.getCodPolo(), webVO.getCodBib());
		hibernateVO.setCd_bib(bib);
		hibernateVO.setCod_tipo_aut(webVO.getCodice());
		hibernateVO.setDescr(webVO.getDescrizione());

		return hibernateVO;
	}


	public static final Tbl_disponibilita_precatalogati daWebAHibernateSegnatura(RangeSegnatureVO vo)
	throws ApplicationException {
		Tbl_disponibilita_precatalogati db = new Tbl_disponibilita_precatalogati();

		Tbf_biblioteca_in_polo bib;
		try {
			bib = instance.utilDAO.getBibliotecaInPolo(vo.getCodPolo(), vo.getCodBiblioteca());
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}


		if (vo.getId()>0) {
			try {
				db = instance.segnDAO.select(vo.getId());
			} catch (DaoManagerException e) {
				throw new ApplicationException(e);
			}
		} else {
			db.setCd_bib(bib);
			db.setCod_segn( vo.getCodSegnatura() );
			db.setTs_ins(vo.getTsIns());
			db.setUte_ins(vo.getUteIns());
		}
		db.setCod_frui(vo.getCodFruizione());
		db.setCod_no_disp(vo.getCodIndisp());
		db.setFl_canc(vo.getFlCanc()!=null ? vo.getFlCanc().charAt(0) : 'N');
		db.setSegn_a(vo.getSegnA());
		db.setSegn_da(vo.getSegnDa());
		db.setSegn_fine(vo.getSegnFine());
		db.setSegn_inizio(vo.getSegnInizio());
		db.setTs_var(vo.getTsVar());
		db.setUte_var(vo.getUteVar());

		return db;
	}


	public static final Tbl_servizio daWebAHibernateServizio(ServizioBibliotecaVO servizioVO, int idTipoServizio)
	throws Exception {
		Tbl_servizio servizio          = null;
		Tbl_penalita_servizio penalita = null;

		if (servizioVO.getIdServizio()>0) {
			//Aggiornamento
			servizio = instance.srvDAO.getServizioBibliotecaById(servizioVO.getIdServizio());
			penalita = servizio.getTbl_penalita_servizio();
		} else {
			//Inserimento
			//servizio = UtilitaDAO.creaIdServizio(servizioVO.getCodPolo(), servizioVO.getCodBib(), servizioVO.getCodTipoServ(), servizioVO.getCodServ());
			Tbl_tipo_servizio tipoServizio = instance.tipoSrvDAO.getTipoServizioById(idTipoServizio);//dao.getTipoServizio(servizioVO.getCodPolo(), servizioVO.getCodBib(), servizioVO.getCodTipoServ());
			servizio = new Tbl_servizio();
			servizio.setId_tipo_servizio(tipoServizio);
			//penalita = new Tbl_penalita_servizio();
		}
		servizio.setCod_servizio    (servizioVO.getCodServ());
		servizio.setDescr           (servizioVO.getDescr());
		servizio.setDur_max_rinn1   (servizioVO.getDurMaxRinn1()==null ? 0 : servizioVO.getDurMaxRinn1());
		servizio.setDur_max_rinn2   (servizioVO.getDurMaxRinn2()==null ? 0 : servizioVO.getDurMaxRinn2());
		servizio.setDur_max_rinn3   (servizioVO.getDurMaxRinn3()==null ? 0 : servizioVO.getDurMaxRinn3());
		servizio.setDur_mov         (servizioVO.getDurMov()==null ? 0 : servizioVO.getDurMov());
		servizio.setFl_canc         (servizioVO.getFlCanc()==null ? 'N' : servizioVO.getFlCanc().charAt(0));
		servizio.setMax_gg_ant      (servizioVO.getMaxGgAnt()==null ? 0 : servizioVO.getMaxGgAnt());
		servizio.setMax_gg_cons     (servizioVO.getMaxGgCons()==null ? 0 : servizioVO.getMaxGgCons());
		servizio.setMax_gg_dep      (servizioVO.getMaxGgDep()==null ? 0 : servizioVO.getMaxGgDep());
		servizio.setN_max_ggval_pren( new BigDecimal(servizioVO.getNMaxGgvalPren()==null ? 0 : servizioVO.getNMaxGgvalPren()) );
		servizio.setN_max_pren      ( new BigDecimal(servizioVO.getNMaxPren()==null ? 0 : servizioVO.getNMaxPren()) );
		servizio.setNum_max_mov     (servizioVO.getNumMaxMov()==null ? 0 : servizioVO.getNumMaxMov());
		servizio.setNum_max_rich    (servizioVO.getNumMaxRich()==null ? 0 : servizioVO.getNumMaxRich());
		servizio.setNum_max_riprod  (servizioVO.getNumMaxRiprod()==null ? 0 : servizioVO.getNumMaxRiprod());
		servizio.setN_max_pren_posto(servizioVO.getNumMaxPrenPosto()==null ? 0 : servizioVO.getNumMaxPrenPosto());
		servizio.setN_gg_prep_supp(ValidazioneDati.coalesce(servizioVO.getNumGgPrepSupp(), (short)0));
		servizio.setTs_orario_limite_pren(servizioVO.getOrarioLimitePrepSupp());
		servizio.setTs_ins          ( new Timestamp(servizioVO.getTsIns().getTime()) );
		servizio.setTs_var          ( new Timestamp(servizioVO.getTsVar().getTime()) );
		servizio.setUte_ins         (servizioVO.getUteIns());
		servizio.setUte_var         (servizioVO.getUteVar());

		//almaviva5_20161206 servizi ill
		servizio.setN_gg_rest_ill	(servizioVO.getGiorniRestituzioneRichiedente());

		//Gestione penalità
		//if (servizioVO.impostataPenalita()) {
			if (penalita==null) {
				penalita = new Tbl_penalita_servizio();
				servizio.setTbl_penalita_servizio(penalita);
			}
			penalita.setCoeff_sosp(new BigDecimal(servizioVO.getPenalita().getCoeffSosp()));
			penalita.setFl_canc   (servizioVO.getPenalita().getFlCanc()== null ? 'N' : servizioVO.getPenalita().getFlCanc().charAt(0));
			penalita.setGg_sosp   (servizioVO.getPenalita().getGgSosp());
			penalita.setTs_ins    (new Timestamp(servizioVO.getPenalita().getTsIns().getTime()));
			penalita.setTs_var    (new Timestamp(servizioVO.getPenalita().getTsVar().getTime()));
			penalita.setUte_ins   (servizioVO.getPenalita().getUteIns());
			penalita.setUte_var   (servizioVO.getPenalita().getUteVar());
			penalita.setId_servizio(servizio);
		//}

		return servizio;
	}


	public static final Tbl_specificita_titoli_studio daWebAHibernateSpecTitoloStudio(SpecTitoloStudioVO vo, boolean nuovo)
	throws Exception {
		Tbl_specificita_titoli_studio specTitoloStudio = new Tbl_specificita_titoli_studio();

		if (nuovo) {
			specTitoloStudio.setCd_biblioteca(instance.utilDAO.getBibliotecaInPolo(vo.getCodPolo(), vo.getCodBiblioteca()));
			specTitoloStudio.setTit_studio(vo.getTitoloStudio().charAt(0));
			specTitoloStudio.setSpecif_tit(vo.getCodSpecialita()); // .charAt(0) 22.12.09
			specTitoloStudio.setTs_ins(vo.getTsIns());
			specTitoloStudio.setUte_ins(vo.getUteIns());
		} else {
			specTitoloStudio = instance.titStudioDAO.getTitoloStudioById(vo.getIdTitoloStudio());
		}

		specTitoloStudio.setDescr(vo.getDesSpecialita());
		specTitoloStudio.setFl_canc((vo.getFlCanc()!=null ? vo.getFlCanc().charAt(0) : 'N'));
		specTitoloStudio.setUte_var(vo.getUteVar());

		return specTitoloStudio;
	}


	public static final Tbl_supporti_biblioteca daWebAHibernateSupportiBiblioteca(SupportoBibliotecaVO supportoVO)
			throws Exception {
		Tbl_supporti_biblioteca supporto = null;
		if (!supportoVO.isNuovo()) {
			// carico supporto da DB
			supporto = instance.suppDAO.getSupportoBibliotecaById(supportoVO.getId());
			if (supporto == null)
				throw new Exception("Supporto non trovato. Cod. supporto:" + supportoVO.getCodSupporto());
		} else {
			supporto = new Tbl_supporti_biblioteca();
			Tbf_biblioteca_in_polo bib = instance.utilDAO.getBibliotecaInPolo(supportoVO.getCd_polo(), supportoVO.getCd_bib());
			if (bib == null)
				throw new Exception("Biblioteca in polo non trovata in base dati. Cod.polo: " + supportoVO.getCd_polo()
						+ "  Cod.Bib:" + supportoVO.getCd_bib());
			supporto.setCd_bib(bib);
		}
		fillBaseHibernate(supporto, supportoVO);

		supporto.setCod_supporto(supportoVO.getCodSupporto());
		supporto.setImp_unita(instance.getBigDecimal(supportoVO.getImportoUnitarioDouble(), false));
		supporto.setCosto_fisso(instance.getBigDecimal(supportoVO.getCostoFissoDouble(), false));

		String flSvolg = supportoVO.getFlSvolg();
		supporto.setFl_svolg(isFilled(flSvolg) ? flSvolg.charAt(0) : 'L');

		return supporto;
	}


	public static final Trl_relazioni_servizi daWebAHibernateTabellaRelazioni(RelazioniVO relazioneVO)
	throws ApplicationException {
		Trl_relazioni_servizi relazioneHib=null;
		try {
			if (relazioneVO!=null) {
				if (relazioneVO.getId()>0) {
					relazioneHib = instance.relDAO.select(relazioneVO.getId());
				} else {
					relazioneHib = new Trl_relazioni_servizi();
					Tbf_biblioteca_in_polo bib = instance.utilDAO.getBibliotecaInPolo(relazioneVO.getCodPolo(), relazioneVO.getCodBib());
					relazioneHib.setCd_bib(bib);
					relazioneHib.setCd_relazione(relazioneVO.getCodRelazione());
					relazioneHib.setTs_ins(relazioneVO.getTsIns());
					relazioneHib.setUte_ins(relazioneVO.getUteIns());
				}
				relazioneHib.setFl_canc( relazioneVO.getFlCanc()!=null ? relazioneVO.getFlCanc().charAt(0) : 'N');
				relazioneHib.setTabella_di_relazione(relazioneVO.getTabellaRelazione());
				relazioneHib.setId_relazione_tabella_di_relazione(relazioneVO.getIdTabellaRelazione());
				relazioneHib.setTabella_tb_codici(relazioneVO.getTabellaCodici());
				relazioneHib.setId_relazione_tb_codici(String.valueOf(relazioneVO.getIdTabellaCodici()));
				relazioneHib.setTs_var(relazioneVO.getTsVar());
				relazioneHib.setUte_var(relazioneVO.getUteVar());
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return relazioneHib;
	}

	public static final Trl_tariffe_modalita_erogazione daWebAHibernateTariffeModalitaErogazione(TariffeModalitaErogazioneVO tariffeVO,
							boolean nuovo, int idTipoServizio)
	throws Exception {
		Trl_tariffe_modalita_erogazione tariffe;
		Trl_tariffe_modalita_erogazione tariffe_canc;

		Tbl_tipo_servizio tipoServizio = instance.tipoSrvDAO.getTipoServizioById(idTipoServizio);
		if (nuovo) {
			//
			tariffe = new Trl_tariffe_modalita_erogazione();
			tariffe.setCod_erog(tariffeVO.getCodErog());
			tariffe.setId_tipo_servizio(tipoServizio);
			tariffe.setUte_ins(tariffeVO.getUteIns());

			tariffe_canc = instance.modErogDAO.getTariffeModalitaErogazione_canc(tariffeVO.getCodErog(), tipoServizio);
			if (tariffe_canc==null){
				tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
			} else {
				tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
				tariffe.setTs_var(tariffe_canc.getTs_var());
				instance.modErogDAO.getCurrentSession().evict(tariffe_canc);
			}
		} else  {
			//Aggiornamento. Leggo da DB.
			tariffe = instance.modErogDAO.getTariffeModalitaErogazione(tariffeVO.getCodErog(), tipoServizio);
		}
		//l'unico campo che si può aggiornare da Web
		tariffe.setFl_canc(tariffeVO.getFlCanc().charAt(0));
		tariffe.setUte_var(tariffeVO.getUteVar());

		return tariffe;
	}

	public static final Trl_supporti_modalita_erogazione daWebAHibernateSupportiModalitaErogazione(SupportiModalitaErogazioneVO tariffeVO,
			boolean nuovo, int idSupportiBiblioteca)
		throws Exception {
		Trl_supporti_modalita_erogazione tariffe;
		Trl_supporti_modalita_erogazione tariffe_canc;

		Tbl_supporti_biblioteca supporto = instance.suppDAO.getSupportoById(idSupportiBiblioteca);
		if (nuovo) {
			//
			tariffe = new Trl_supporti_modalita_erogazione();
			tariffe.setCod_erog(tariffeVO.getCodErog());
			tariffe.setId_supporti_biblioteca(supporto);
			tariffe.setUte_ins(tariffeVO.getUteIns());

			tariffe_canc = instance.modErogDAO.getSupportiModalitaErogazione_canc(tariffeVO.getCodErog(), supporto);
			if (tariffe_canc == null) {
				tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
			} else {
				tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
				tariffe.setTs_var(tariffe_canc.getTs_var());
				instance.modErogDAO.getCurrentSession().evict(tariffe_canc);
			}
		} else  {
		//Aggiornamento. Leggo da DB.
		tariffe = instance.modErogDAO.getSupportiModalitaErogazione(tariffeVO.getCodErog(), supporto);
		}
		//l'unico campo che si può aggiornare da Web
		tariffe.setFl_canc(tariffeVO.getFlCanc().charAt(0));
		tariffe.setUte_var(tariffeVO.getUteVar());

		return tariffe;
		}

	public static final Tbl_modalita_erogazione daWebAHibernateModalitaErogazione(ModalitaErogazioneVO tariffeVO,
			boolean nuovo)
	throws Exception {
	Tbl_modalita_erogazione tariffe;
	Tbl_modalita_erogazione tariffe_canc;
	Tbl_modalita_erogazioneDAO modalitaErogazioneDAO = new Tbl_modalita_erogazioneDAO();

	if (nuovo) {
	//
		tariffe = new Tbl_modalita_erogazione();
		tariffe.setCod_erog(tariffeVO.getCodErog());
		tariffe.setUte_ins(tariffeVO.getUteIns());
		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(tariffeVO.getCodPolo(), tariffeVO.getCodBib());
		tariffe.setCd_bib(bib);

		tariffe_canc = modalitaErogazioneDAO.getTariffeModalitaErogazione_canc(tariffeVO.getCodPolo(), tariffeVO.getCodBib(), tariffeVO.getCodErog());
		if (tariffe_canc==null){
			tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
		} else {
			tariffe.setTs_ins(new Timestamp(tariffeVO.getTsIns().getTime()));
			tariffe.setTs_var(tariffe_canc.getTs_var());
			ConversioneHibernateVO.toHibernate().assignEntityID(tariffe, "id_modalita_erogazione", tariffe_canc.getId_modalita_erogazione());
			instance.modErogDAO.getCurrentSession().evict(tariffe_canc);
		}
	} else  {
		//Aggiornamento. Leggo da DB.
		tariffe = modalitaErogazioneDAO.getTariffeModalitaErogazione(tariffeVO.getCodPolo(), tariffeVO.getCodBib(), tariffeVO.getCodErog());
	}
	//gli unici campi che si possono aggiornare da Web
	tariffe.setTar_base(new BigDecimal(tariffeVO.getTarBaseDouble()));
	tariffe.setCosto_unitario(new BigDecimal(tariffeVO.getCostoUnitarioDouble()));
	tariffe.setFl_canc(tariffeVO.getFlCanc().charAt(0));
	tariffe.setUte_var(tariffeVO.getUteVar());

	String flSvolg = tariffeVO.getFlSvolg();
	tariffe.setFl_svolg(isFilled(flSvolg) ? flSvolg.charAt(0) : 'L');

	return tariffe;
	}


	public static final Tbl_tipi_autorizzazioni daWebAHibernateTipiAut(
			UtenteBibliotecaVO webVO) throws Exception {
		Tbl_tipi_autorizzazioni hibernateVO = new Tbl_tipi_autorizzazioni();
		hibernateVO.setDescr(webVO.getAutorizzazioni().getAutorizzazione());
		hibernateVO
				.setCod_tipo_aut(webVO.getAutorizzazioni().getCodTipoAutor());
		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO
				.getAutorizzazioni().getCodPoloAutor(), webVO
				.getAutorizzazioni().getCodBibAutor());
		hibernateVO.setCd_bib(bib);
		return hibernateVO;
	}


	public static final Tbl_tipo_servizio daWebAHibernateTipoServizio(Tbl_tipo_servizio hVO, TipoServizioVO webVO)
	throws Exception {
		//Tbl_tipo_servizio hVO = null;

		if (webVO.getIdTipoServizio() > 0) {
			//Aggiornamento
			hVO = instance.tipoSrvDAO.getTipoServizioById(webVO.getIdTipoServizio());
		} else {
			//Inserimento
			hVO = hVO != null ? hVO : new Tbl_tipo_servizio();
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO.getCodPolo(), webVO.getCodBib());
			hVO.setCd_bib(bib);
			hVO.setCod_tipo_serv(webVO.getCodiceTipoServizio());
		}
		//tipoServizio.setCoda_richieste((short)tipoServizioVO.getCodaRichieste());
		hVO.setCoda_richieste(webVO.isCodaRichieste() ? 'S' : 'N');

		hVO.setNum_max_mov((short)webVO.getNumMaxMov());

		if (webVO.getOreRidis() == 0)
			hVO.setOre_ridis((short) (webVO.getGgRidis() * 24));
		else
			hVO.setOre_ridis((short)webVO.getOreRidis());

		hVO.setPenalita(webVO.isPenalita() ? 'S' : 'N');

		hVO.setIns_richieste_utente(webVO.isIns_richieste_utente() ? 'S' : 'N');
		hVO.setAnche_da_remoto(webVO.isAnche_da_remoto() ? 'S' : 'N');

		fillBaseHibernate(hVO, webVO);

		//almaviva5_20180914 #6685
		hVO.setCd_iso_ill(webVO.getCodServizioILL());

		if (!webVO.isIns_richieste_utente()) {
			hVO.setAnche_da_remoto('N');
			webVO.setAnche_da_remoto(false);
		}

		return hVO;
	}


	public static final Tbl_servizio_web_dati_richiesti daWebAHibernateModuloRichiesta(
			ServizioWebDatiRichiestiVO webDatiRichiestiVO,
			Tbl_tipo_servizio tipoServizio)
	throws Exception {

		Tbl_servizio_web_dati_richiesti servizioWebDatiRichiesti = new Tbl_servizio_web_dati_richiesti();

		servizioWebDatiRichiesti.setCampo_richiesta((short)webDatiRichiestiVO.getCampoRichiesta());
		servizioWebDatiRichiesti.setId_tipo_servizio(tipoServizio);

		servizioWebDatiRichiesti.setObbligatorio(webDatiRichiestiVO.isObbligatorio() ? 'S' : 'N');

		servizioWebDatiRichiesti
				.setTs_ins(webDatiRichiestiVO.getTsIns() != null ?
						webDatiRichiestiVO.getTsIns() : DaoManager.now());
		servizioWebDatiRichiesti.setTs_var(webDatiRichiestiVO.getTsVar());
		servizioWebDatiRichiesti.setUte_ins(webDatiRichiestiVO.getUteIns());
		servizioWebDatiRichiesti.setUte_var(webDatiRichiestiVO.getUteVar());

		servizioWebDatiRichiesti.setFl_canc(webDatiRichiestiVO.isUtilizzato() ? 'N' : 'S');


		return servizioWebDatiRichiesti;
	}


	public static final Tbl_specificita_titoli_studio daWebAHibernateTitoloStudioRicerca(RicercaTitoloStudioVO webVO)
	throws Exception {
		Tbl_specificita_titoli_studio hibernateVO = new Tbl_specificita_titoli_studio();

		Tbf_biblioteca_in_polo bib = instance.utilDAO.getBibliotecaInPolo(webVO.getCodPolo(), webVO.getCodBib());//UtilitaDAO.creaIdBib(webVO.getCodPolo(), webVO.getCodBib());
		hibernateVO.setCd_biblioteca(bib);
		hibernateVO.setDescr(webVO.getDesSpecialita());
		if (ValidazioneDati.strIsNull(webVO.getCodSpecialita()))
			hibernateVO.setSpecif_tit("  "); //NULL_CHAR 22.12.09
		else
			hibernateVO.setSpecif_tit(webVO.getCodSpecialita()); // .charAt(0) 22.12.09
		if (ValidazioneDati.strIsNull(webVO.getTitoloStudio()))
			hibernateVO.setTit_studio(NULL_CHAR);
		else
			hibernateVO.setTit_studio(webVO.getTitoloStudio().charAt(0));

		if (webVO.getId_specificita_titoli_studio()>0)
			hibernateVO.setId_specificita_titoli_studio(webVO.getId_specificita_titoli_studio());

		return hibernateVO;
	}


	public static final Trl_utenti_biblioteca daWebAHibernateUteBibDettaglio(
			UtenteBibliotecaVO webVO, Trl_utenti_biblioteca oldUteBib)
			throws Exception {

		Trl_utenti_biblioteca hVO = null;
		if (oldUteBib == null) {
			hVO = new Trl_utenti_biblioteca();
			Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO.getCodPolo(), webVO.getCodBibSer());
			hVO.setCd_biblioteca(bib);
		} else
			hVO = oldUteBib;

		AutorizzazioniVO aut = webVO.getAutorizzazioni();
		BiblioPoloVO bp = webVO.getBibliopolo();
		hVO.setCod_tipo_aut(aut.getCodTipoAutor());

		if (!ValidazioneDati.strIsNull(bp.getBiblioCredito()))
			hVO.setCredito_bib(new BigDecimal(bp.getBiblioCreditoDouble()));
		if (!ValidazioneDati.strIsNull(bp.getBiblioNote()))
			hVO.setNote_bib(bp.getBiblioNote().trim());
		if (!ValidazioneDati.strIsNull(bp.getInizioAuto()))
			hVO.setData_inizio_aut(stringToDate(bp.getInizioAuto()));
		if (!ValidazioneDati.strIsNull(bp.getFineAuto()))
			hVO.setData_fine_aut(stringToDate(bp.getFineAuto()));
		if (bp != null && bp.getInizioSosp() != null)
			hVO.setData_inizio_sosp(stringToDate(bp.getInizioSosp()));
		if (bp != null && bp.getFineSosp() != null)
			hVO.setData_fine_sosp(stringToDate(bp.getFineSosp()));

		// occupazioni
		int idOccupazione = 0;
		if (!ValidazioneDati.strIsNull(webVO.getProfessione().getIdOccupazione()))
			idOccupazione  = Integer.valueOf(webVO.getProfessione().getIdOccupazione());

		if (idOccupazione > 0)
			hVO.setId_occupazioni(UtilitaDAO
					.creaIdOccupazione(webVO.getCodPoloSer(), webVO
							.getCodBibSer(), idOccupazione));
		else
			hVO.setId_occupazioni(null);

		// titoli studio
		int idSpecTitoloStudio = 0;
		if (!ValidazioneDati.strIsNull(webVO.getProfessione().getIdSpecTitoloStudio()))
			idSpecTitoloStudio = Integer.valueOf(webVO.getProfessione().getIdSpecTitoloStudio());
		if (idSpecTitoloStudio > 0)
			hVO.setId_specificita_titoli_studio(UtilitaDAO
					.creaIdTitoloStudio(webVO.getCodPoloSer(), webVO
							.getCodBibSer(), idSpecTitoloStudio));
		else
			hVO.setId_specificita_titoli_studio(null);

		hVO.setUte_ins(webVO.getUteIns());
		hVO.setUte_var(webVO.getUteVar());
		Timestamp ts = DaoManager.now();
		if (webVO.getTsIns() == null)
			hVO.setTs_ins(ts);
		else
			hVO.setTs_ins(webVO.getTsIns());
//		hibernateVO.setTs_var(ts);
		if (!ValidazioneDati.strIsNull(webVO.getFlCanc()))
			hVO.setFl_canc(webVO.getFlCanc().charAt(0));
		return hVO;
	}


	public static final Trl_utenti_biblioteca daWebAHibernateUteBibRicerca(
			UtenteBibliotecaVO webVO) throws Exception {
		Trl_utenti_biblioteca hibernateVO = new Trl_utenti_biblioteca();
		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO
				.getCodPolo(), webVO.getCodBibSer());
		Tbl_utenti ute = hibernateVO.getId_utenti();
		hibernateVO.setCd_biblioteca(bib);
		hibernateVO
				.setCod_tipo_aut(webVO.getAutorizzazioni().getCodTipoAutor());
		hibernateVO.setCredito_bib(new BigDecimal(webVO.getBibliopolo()
				.getBiblioCreditoDouble()));
		hibernateVO.setNote_bib(webVO.getBibliopolo().getBiblioNote());
		hibernateVO.setData_inizio_aut(Date.valueOf(webVO.getBibliopolo()
				.getInizioAuto()));
		hibernateVO.setData_fine_aut(Date.valueOf(webVO.getBibliopolo()
				.getFineAuto()));
		hibernateVO.setData_inizio_sosp(Date.valueOf(webVO.getBibliopolo()
				.getInizioSosp()));
		hibernateVO.setData_fine_sosp(Date.valueOf(webVO.getBibliopolo()
				.getFineSosp()));
//		hibernateVO.getId_occupazioni().setOccupazione(
//				webVO.getProfessione().getOccupazione().charAt(0));
//		hibernateVO.getId_occupazioni().setProfessione(
//				webVO.getProfessione().getProfessione().charAt(0));
//		hibernateVO.getId_specificita_titoli_studio().setTit_studio(
//				webVO.getProfessione().getTitoloStudio().charAt(0));
//		hibernateVO.getId_specificita_titoli_studio().setSpecif_tit(
//				webVO.getProfessione().getSpecTitoloStudio().charAt(0));

		ute.setCognome(webVO.getCognome());
		ute.setNome(webVO.getNome());
		AnagrafeVO anagrafe = webVO.getAnagrafe();
		if (ValidazioneDati.isFilled(anagrafe.getDataNascita()) )
			ute.setData_nascita((new SimpleDateFormat("dd/MM/yyyy")).parse(anagrafe.getDataNascita()));
		ute.setCod_fiscale(ValidazioneDati.trimOrNull(anagrafe.getCodFiscale()));
		ute.setInd_posta_elettr(anagrafe.getPostaElettronica());
		ute.setPaese_citt(anagrafe.getNazionalita());
		ute.setProv_res(anagrafe.getResidenza().getProvincia());
		ute.setCd_bib(bib);
		ute.setCod_utente(webVO.getCodiceUtente());
		ute.setCod_bib(webVO.getBibliopolo().getCodBibXUteBib());
		ute.setCod_polo_bib(webVO.getBibliopolo().getCodPoloXUteBib());

//		if (webVO.getPassword()!= null && !webVO.getPassword().trim().equals("")) {
//			PasswordEncrypter encrypter = new PasswordEncrypter(webVO.getPassword().trim());
//			String encryptedPassword = encrypter.encrypt(webVO.getPassword().trim());
//			ute.setPassword(encryptedPassword);
//		}

		ute.setLuogo_nascita(anagrafe.getLuogoNascita());
		if (anagrafe.getProvenienza() != null
				&& anagrafe.getProvenienza().length() != 0)
			ute.setCod_proven(anagrafe.getProvenienza().charAt(0));
		ute.setSesso(anagrafe.getSesso().charAt(0));
		for (int ind = 0; ind < 4; ind++) {
			DocumentoVO doc = webVO.getDocumento().get(ind);
			if (ind == 0) {
				if (doc.getDocumento() != null)
					ute.setTipo_docum1(doc.getDocumento().charAt(0));
				if (doc.getNumero() != null)
					ute.setNum_docum1(doc.getNumero());
				if (doc.getAutRilascio() != null)
					ute.setAut_ril_docum1(doc.getAutRilascio());
			}
			if (ind == 1) {
				if (doc.getDocumento() != null)
					ute.setTipo_docum2(doc.getDocumento().charAt(0));
				if (doc.getNumero() != null)
					ute.setNum_docum2(doc.getNumero());
				if (doc.getAutRilascio() != null)
					ute.setAut_ril_docum2(doc.getAutRilascio());
			}
			if (ind == 2) {
				if (doc.getDocumento() != null)
					ute.setTipo_docum3(doc.getDocumento().charAt(0));
				if (doc.getNumero() != null)
					ute.setNum_docum3(doc.getNumero());
				if (doc.getAutRilascio() != null)
					ute.setAut_ril_docum3(doc.getAutRilascio());
			}
			if (ind == 3) {
				if (doc.getDocumento() != null)
					ute.setTipo_docum4(doc.getDocumento().charAt(0));
				if (doc.getNumero() != null)
					ute.setNum_docum4(doc.getNumero());
				if (doc.getAutRilascio() != null)
					ute.setAut_ril_docum4(doc.getAutRilascio());
			}
		}
		ute.setCap_dom(anagrafe.getDomicilio().getCap());
		ute.setCitta_dom(anagrafe.getDomicilio().getCitta());
		ute.setFax_dom(anagrafe.getDomicilio().getFax());
		ute.setTel_dom(anagrafe.getDomicilio().getTelefono());
		ute.setIndirizzo_dom(anagrafe.getDomicilio().getVia());
		ute.setProv_dom(anagrafe.getDomicilio().getProvincia());
		ute.setCap_res(anagrafe.getResidenza().getCap());
		ute.setCitta_res(anagrafe.getResidenza().getCitta());
		ute.setFax_res(anagrafe.getResidenza().getFax());
		ute.setTel_res(anagrafe.getResidenza().getTelefono());
		ute.setIndirizzo_res(anagrafe.getResidenza().getVia());
		ute.setProv_res(anagrafe.getResidenza().getProvincia());
		ute.setPaese_res(anagrafe.getResidenza().getNazionalita());
		ute.setCod_ateneo(webVO.getProfessione().getAteneo());
		ute.setCorso_laurea(webVO.getProfessione().getCorsoLaurea());
		ute.setCod_matricola(webVO.getProfessione().getMatricola());
		ute.setNome_referente(webVO.getProfessione().getReferente());
		if (webVO.getProfessione().getTipoPersona() != null
				&& webVO.getProfessione().getTipoPersona().length() != 0)
			ute.setPersona_giuridica(webVO.getProfessione().getTipoPersona()
					.charAt(0));
		if (webVO.getBibliopolo().getPoloCredito() != null
				&& webVO.getBibliopolo().getPoloCredito().length() != 0) {
			//ute.setCredito_polo(new BigDecimal(webVO.getBibliopolo().getPoloCredito()));
			ute.setCredito_polo(new BigDecimal(webVO.getBibliopolo().getPoloCreditoDouble()));
		}
		if (webVO.getBibliopolo().getPoloNote() != null)
			ute.setNote_polo(webVO.getBibliopolo().getPoloNote().trim());
		if (webVO.getBibliopolo().getPoloDataRegistrazione() != null
				&& webVO.getBibliopolo().getPoloDataRegistrazione().length() != 0)
			ute.setData_reg((new SimpleDateFormat("dd/MM/yyyy")).parse(webVO
					.getBibliopolo().getPoloDataRegistrazione()));
		if (webVO.getBibliopolo().getPoloInfrazioni() != null)
			ute.setNote(webVO.getBibliopolo().getPoloInfrazioni().trim());
		return hibernateVO;
	}



	public static final Tbl_utenti daWebAHibernateUteDettaglio(UtenteBibliotecaVO webVO,
			Tbl_utenti oldUte) throws Exception {
		Tbl_utenti hVO = null;
		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO
				.getCodPolo(), webVO.getCodiceBiblioteca());

		if (oldUte == null) {
			hVO = new Tbl_utenti(); // nuovo utente
			hVO.setCd_bib(bib);
		} else {
			hVO = oldUte; // aggiorna utente

		}

		hVO.setChiave_ute(trimString(webVO.getChiaveUte()));

		hVO.setCognome(trimString(webVO.getCognome()));
		hVO.setNome(trimString(webVO.getNome()));
		AnagrafeVO anag = webVO.getAnagrafe();
		//LFV bug 30/08/2018
		SimpleDateFormat ddMMyyyparser = new SimpleDateFormat("dd/MM/yyyy");
		if (ValidazioneDati.isFilled(anag.getDataNascita())) {
			hVO.setData_nascita(ddMMyyyparser.parse(anag.getDataNascita()));
		} else if (!ValidazioneDati.isFilled(anag.getDataNascita())) {
			hVO.setData_nascita(ddMMyyyparser.parse("01/01/1900"));
		} else if (webVO.getProfessione().getPersonaGiuridica() == 'S') {
			hVO.setData_nascita(ddMMyyyparser.parse("01/01/9999"));
		}
		hVO.setCod_fiscale(ValidazioneDati.trimOrNull(anag.getCodFiscale()));

//		hVO.setInd_posta_elettr(null); //tck 3914
		//almaviva5_20150428
		hVO.setInd_posta_elettr(ValidazioneDati.trimOrNull(anag.getPostaElettronica()));
		hVO.setInd_posta_elettr2(ValidazioneDati.trimOrNull(anag.getPostaElettronica2()));
//		if (anag.getPostaElettronica()!=null && anag.getPostaElettronica().trim().length()>0  ) // tck 3914
//		{
//			hVO.setInd_posta_elettr(trimString(anag.getPostaElettronica()));
//		}
		hVO.setPaese_citt(anag.getNazionalita());
		hVO.setProv_res(anag.getResidenza().getProvincia());
//		hibernateVO.setCd_bib(bib);
		if (webVO.getCodiceUtente() != null)
			hVO.setCod_utente(webVO.getCodiceUtente());

		BiblioPoloVO bp = webVO.getBibliopolo();
		hVO.setCod_bib(bp.getCodBibXUteBib());
		hVO.setCod_polo_bib(bp.getCodPoloXUteBib());

		if (ValidazioneDati.isFilled(webVO.getPassword())) {
			hVO.setPassword(webVO.getPassword());
			hVO.setLast_access(webVO.getLastAccess());
			hVO.setChange_password(ValidazioneDati.isFilled(webVO
					.getChangePassword()) ? webVO.getChangePassword().charAt(0)
					: 'N');
		}

		hVO.setLuogo_nascita(anag.getLuogoNascita());
		if (anag.getProvenienza() != null
				&& anag.getProvenienza().length() != 0)
			hVO.setCod_proven(anag.getProvenienza().charAt(0));
		hVO.setSesso( ValidazioneDati.strIsNull(anag.getSesso()) ? ' ' : anag.getSesso().charAt(0) );
		for (int ind = 0; ind < 4; ind++) {
			DocumentoVO doc = webVO.getDocumento().get(ind);
			if (ind == 0) {
				if (!ValidazioneDati.strIsNull(doc.getDocumento()))
					hVO.setTipo_docum1(doc.getDocumento().charAt(0));
				if (!ValidazioneDati.strIsNull(doc.getNumero()))
					hVO.setNum_docum1(doc.getNumero());
				if (!ValidazioneDati.strIsNull(doc.getAutRilascio()))
					hVO.setAut_ril_docum1(trimString(doc
							.getAutRilascio()));
			}
			if (ind == 1) {
				if (!ValidazioneDati.strIsNull(doc.getDocumento()))
					hVO.setTipo_docum2(doc.getDocumento().charAt(0));
				if (!ValidazioneDati.strIsNull(doc.getNumero()))
					hVO.setNum_docum2(doc.getNumero());
				if (!ValidazioneDati.strIsNull(doc.getAutRilascio()))
					hVO.setAut_ril_docum2(trimString(doc
							.getAutRilascio()));
			}
			if (ind == 2) {
				if (!ValidazioneDati.strIsNull(doc.getDocumento()))
					hVO.setTipo_docum3(doc.getDocumento().charAt(0));
				if (!ValidazioneDati.strIsNull(doc.getNumero()))
					hVO.setNum_docum3(doc.getNumero());
				if (!ValidazioneDati.strIsNull(doc.getAutRilascio()))
					hVO.setAut_ril_docum3(trimString(doc
							.getAutRilascio()));
			}
			if (ind == 3) {
				if (!ValidazioneDati.strIsNull(doc.getDocumento()))
					hVO.setTipo_docum4(doc.getDocumento().charAt(0));
				if (!ValidazioneDati.strIsNull(doc.getNumero()))
					hVO.setNum_docum4(doc.getNumero());
				if (!ValidazioneDati.strIsNull(doc.getAutRilascio()))
					hVO.setAut_ril_docum4(trimString(doc
							.getAutRilascio()));
			}
		}

		hVO.setAllinea(webVO.getTipoSMS());
		hVO.setCap_dom(anag.getDomicilio().getCap());
		hVO.setCitta_dom(anag.getDomicilio().getCitta());
		hVO.setFax_dom(anag.getDomicilio().getFax());
		hVO.setTel_dom(anag.getDomicilio().getTelefono());
		hVO.setIndirizzo_dom(trimString(anag.getDomicilio().getVia()));
		hVO.setProv_dom(anag.getDomicilio().getProvincia());
		hVO.setCap_res(anag.getResidenza().getCap());
		hVO.setCitta_res(anag.getResidenza().getCitta());
		hVO.setFax_res(anag.getResidenza().getFax());
		hVO.setTel_res(anag.getResidenza().getTelefono());
		hVO.setIndirizzo_res(trimString(anag.getResidenza().getVia()));
		hVO.setProv_res(anag.getResidenza().getProvincia());
		hVO.setPaese_res(anag.getResidenza().getNazionalita());
		hVO.setCod_ateneo(webVO.getProfessione().getAteneo());
		hVO.setCorso_laurea(webVO.getProfessione().getCorsoLaurea());
		hVO.setCod_matricola(webVO.getProfessione().getMatricola());
		hVO.setNome_referente(trimString(webVO.getProfessione().getReferente()));
		hVO.setPersona_giuridica(webVO.getProfessione().getPersonaGiuridica());

		// esame esistenza della sola professione 18.01.10
		if (ValidazioneDati.isFilled(webVO.getProfessione().getProfessione()) ) {
			hVO.setProfessione(webVO.getProfessione().getProfessione().trim().charAt(0));
		}

		// esame esistenza solo titolo di studio 18.01.10
		if (ValidazioneDati.isFilled(webVO.getProfessione().getTitoloStudio()) ) {
			hVO.setTit_studio(webVO.getProfessione().getTitoloStudio().trim().charAt(0));
		}

		if (ValidazioneDati.isFilled(webVO.getProfessione().getTipoPersona()) )
			hVO.setTipo_pers_giur(webVO.getProfessione().getTipoPersona().charAt(0));
		if (ValidazioneDati.isFilled(bp.getPoloCredito().length()) ) {
			//hibernateVO.setCredito_polo(new BigDecimal(webVO.getBibliopolo().getPoloCredito()));
			hVO.setCredito_polo(new BigDecimal(bp.getPoloCreditoDouble()));
		}
		if (bp.getPoloNote() != null)
			hVO.setNote_polo(bp.getPoloNote().trim());
		if (bp.getPoloDataRegistrazione() != null
				&& bp.getPoloDataRegistrazione().length() != 0)
			hVO.setData_reg(ddMMyyyparser.parse(bp.getPoloDataRegistrazione()));
		if (bp.getPoloInfrazioni() != null)
			hVO.setNote(bp.getPoloInfrazioni().trim());

		//almaviva5_20110428 #4401
		hVO.setCodice_anagrafe(bp.getCodiceAnagrafe());

		hVO.setUte_ins(webVO.getUteIns());
		hVO.setUte_var(webVO.getUteVar());
		final Timestamp ts = DaoManager.now();
		if (webVO.getTsIns() == null)
			hVO.setTs_ins(ts);
		else
			hVO.setTs_ins(webVO.getTsIns());
	//	hibernateVO.setTs_var(ts);
		if (!ValidazioneDati.strIsNull(webVO.getFlCanc()))
			hVO.setFl_canc(webVO.getFlCanc().charAt(0));

		if (!ValidazioneDati.strIsNull(webVO.getFlCancAna()))
			hVO.setFl_canc(webVO.getFlCancAna().charAt(0));

		String tipoUtente = webVO.getTipoUtente();
		hVO.setCd_tipo_ute(isFilled(tipoUtente) ? tipoUtente.charAt(0) : Servizi.Utenti.UTENTE_TIPO_SBNWEB_CHR);

		// segnalazione CSA: mancato aggiornamento diritti
		hVO.setLast_access(ValidazioneDati.coalesce(webVO.getLastAccess(), ts));

		return hVO;
	}

	public static final Trl_utenti_biblioteca daWebAHibernateUteRicerca(RicercaUtenteBibliotecaVO webVO)
	throws Exception {
		Trl_utenti_biblioteca hVO = new Trl_utenti_biblioteca();
		Tbl_utenti utente = new Tbl_utenti();
		Tbl_specificita_titoli_studio specTitStudio = null;
		Tbl_occupazioni professione = null;

		Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(webVO.getCodPoloSer(), webVO.getCodBibSer());
		hVO.setCd_biblioteca(bib);
		hVO.setCod_tipo_aut(webVO.getTipoAutorizzazione());

		utente.setCod_fiscale(ValidazioneDati.trimOrNull(webVO.getCodFiscale()));
		utente.setCod_utente(webVO.getCodUte());
		utente.setCod_matricola(webVO.getMatricola());
		utente.setCod_ateneo(webVO.getCodiceAteneo());

		utente.setCognome(webVO.getCognome());
		utente.setNome(webVO.getNome());

		//09.02.10
		if (!ValidazioneDati.strIsNull(webVO.getChiave_ute()))
		{
			utente.setChiave_ute(webVO.getChiave_ute());
			utente.setCognome("");
			utente.setNome("");
		}

		// 18.11.09
		if (!ValidazioneDati.strIsNull(webVO.getTipoPersona()))
			utente.setTipo_pers_giur(webVO.getTipoPersona().charAt(0));
		if (!ValidazioneDati.strIsNull(webVO.getPersonaGiuridica()) )
			utente.setPersona_giuridica(webVO.getPersonaGiuridica().charAt(0));

		// 18.11.09
/*		if (!ValidazioneDati.strIsNull(webVO.getDataNascita()))
			utente.setData_nascita((new SimpleDateFormat("dd/MM/yyyy")).parse(webVO.getDataNascita()));
*/
		if (!ValidazioneDati.strIsNull(webVO.getDataNascita()))
			(new SimpleDateFormat("dd/MM/yyyy")).parse(webVO.getDataNascita());

		if (!ValidazioneDati.strIsNull(webVO.getDataNascitaA()))
			(new SimpleDateFormat("dd/MM/yyyy")).parse(webVO.getDataNascitaA());

		if (!ValidazioneDati.strIsNull(webVO.getDataFineAut()))
		{
			(new SimpleDateFormat("dd/MM/yyyy")).parse(webVO.getDataFineAut());
		}

		if (!ValidazioneDati.strIsNull(webVO.getDataFineAutA()))
			(new SimpleDateFormat("dd/MM/yyyy")).parse(webVO.getDataFineAutA());

		utente.setInd_posta_elettr(webVO.getEmail());
		//almaviva5_20150428
		utente.setInd_posta_elettr2(null);
		utente.setPaese_citt(webVO.getNazCitta());
		utente.setProv_res(webVO.getProvResidenza());

		if (!ValidazioneDati.strIsNull(webVO.getSesso())) {
			utente.setSesso(webVO.getSesso().charAt(0));
		}

		if (!ValidazioneDati.strIsNull(webVO.getProfessione())) {
			professione = new Tbl_occupazioni();
			char prof = webVO.getProfessione().charAt(0);
			professione.setProfessione(prof);
			//almaviva5_20140905 #5634
			utente.setProfessione(prof);
			professione.setOccupazione("  "); // 22.12.09
			if (!ValidazioneDati.strIsNull(webVO.getOccupazione()))
				professione.setOccupazione(webVO.getOccupazione()); // 22.12.09 .charAt(0)

		}

		if (!ValidazioneDati.strIsNull(webVO.getTitStudio())) {
			specTitStudio = new Tbl_specificita_titoli_studio();
			char titStudio = webVO.getTitStudio().charAt(0);
			specTitStudio.setTit_studio(titStudio);
			//almaviva5_20140905 #5634
			utente.setTit_studio(titStudio);
			specTitStudio.setSpecif_tit("  ");
			if (!ValidazioneDati.strIsNull(webVO.getSpecificita()))
				specTitStudio.setSpecif_tit(webVO.getSpecificita()); // 22.12.09 .charAt(0)

		}

		hVO.setId_utenti(utente);
		hVO.setId_specificita_titoli_studio(specTitStudio);
		hVO.setId_occupazioni(professione);

		return hVO;
	}

	protected ServiziConversioneVO() {
		super();
	}

}
