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
package it.iccu.sbn.ejb.domain.servizi;

import static it.iccu.sbn.util.Constants.Servizi.Movimenti.NUMBER_FORMAT_PREZZI;

import gnu.trove.THashMap;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.servizi.calendario.Calendario;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.utils.isbd.IsbdTokenizer;
import it.iccu.sbn.ejb.utils.isbd.IsbdVO;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.MovimentoPerStampaServCorrVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziCorrentiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziStoricizzatiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaServiziVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_poloDao;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.AttivitaBibliotecarioDAO;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.IterServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.MaterieDAO;
import it.iccu.sbn.persistence.dao.servizi.ModalitaErogazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.ModalitaPagamentoDAO;
import it.iccu.sbn.persistence.dao.servizi.OccupazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.persistence.dao.servizi.SegnatureDAO;
import it.iccu.sbn.persistence.dao.servizi.ServiziDAO;
import it.iccu.sbn.persistence.dao.servizi.SupportiBibliotecaDAO;
import it.iccu.sbn.persistence.dao.servizi.TabelleRelazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_documenti_lettoriDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_modalita_erogazioneDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_servizio_web_dati_richiestiDAO;
import it.iccu.sbn.persistence.dao.servizi.TipoServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.TitoliDiStudioDAO;
import it.iccu.sbn.persistence.dao.servizi.Trl_diritti_utenteDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiProfessionaliDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.servizi.Tbl_controllo_iter;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_disponibilita_precatalogati;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_materie;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;
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
import it.iccu.sbn.polo.orm.servizi.Trl_attivita_bibliotecario;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_materie_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_relazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_supporti_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Trl_tariffe_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Vl_richiesta_servizio;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.servizi.z3950.PQFBuilder;
import it.iccu.sbn.servizi.z3950.Z3950ClientFactory;
import it.iccu.sbn.util.Constants.DocFisico;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jasper.BatchCollectionSerializer;
import it.iccu.sbn.util.mail.EmailValidator;
import it.iccu.sbn.util.mail.MailBodyBuilder;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.util.mail.servizi.ServiziMail;
import it.iccu.sbn.util.servizi.SaleUtil;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.util.servizi.SollecitiUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.vo.validators.servizi.MovimentoValidator;
import it.iccu.sbn.vo.validators.servizi.RicercaUtenteBibliotecaValidator;
import it.iccu.sbn.vo.validators.servizi.ServizioBibliotecaValidator;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DisponibilitaType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DocumentoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.PrenotazioniType;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.mail.internet.InternetAddress;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.exception.DataException;
import org.marc4j.marc.Record;

import com.annimon.stream.Optional;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Servizi" description="A session bean named Servizi"
 *           display-name="Servizi" jndi-name="sbnWeb/Servizi" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 *           <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class ServiziBean extends TicketChecker implements Servizi {

	private static final long serialVersionUID = -306211605647475722L;

	private static final String ANNO_PERIODICO_99 = "99";

	private ServiziCommon serviziCommon = null;
	private AmministrazioneBiblioteca amministrazioneBib = null;
	private Calendario calendario;
	private Sale sale;

	private static Logger log = Logger.getLogger(Servizi.class);

	private static final Map<String, Comparator<MovimentoListaVO>> comparators = new HashMap<String, Comparator<MovimentoListaVO>>();

	SessionContext ctx;

	private ServiziILLInvokeHandler ill_handler = new ServiziILLInvokeHandler(this);

	static {
		comparators.put(MovimentoListaVO.ORDINAMENTO_COGNOME_NOME_ASC, MovimentoListaVO.ORDINA_PER_COGNOME_NOME);
		comparators.put(MovimentoListaVO.ORDINAMENTO_COGNOME_NOME_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_COGNOME_NOME));

		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_RICHIESTA_ASC, MovimentoListaVO.ORDINA_PER_DATA_RICHIESTA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_RICHIESTA_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_DATA_RICHIESTA));

		comparators.put(MovimentoListaVO.ORDINAMENTO_INVENTARIO_ASC, MovimentoListaVO.ORDINA_PER_INVENTARIO);
		comparators.put(MovimentoListaVO.ORDINAMENTO_INVENTARIO_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_INVENTARIO));

		comparators.put(MovimentoListaVO.ORDINAMENTO_TITOLO_ASC, MovimentoListaVO.ORDINA_PER_TITOLO);
		comparators.put(MovimentoListaVO.ORDINAMENTO_TITOLO_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_TITOLO));

		comparators.put(MovimentoListaVO.ORDINAMENTO_STATO_RICHIESTA_ASC, MovimentoListaVO.ORDINA_PER_STATO_RICHIESTA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_STATO_RICHIESTA_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_STATO_RICHIESTA));

		comparators.put(MovimentoListaVO.ORDINAMENTO_SERVIZIO_ASC, MovimentoListaVO.ORDINA_PER_SERVIZIO);
		comparators.put(MovimentoListaVO.ORDINAMENTO_SERVIZIO_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_SERVIZIO));

		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_SCADENZA_ASC, MovimentoListaVO.ORDINA_PER_DATA_SCADENZA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_SCADENZA_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_DATA_SCADENZA));

		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_FINE_PREVISTA_ASC,
				MovimentoListaVO.ORDINA_PER_DATA_FINE_PREVISTA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_FINE_PREVISTA_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_DATA_FINE_PREVISTA));

		// lista movimenti
		comparators.put(MovimentoListaVO.ORDINAMENTO_COGNOME_NOME_ASC, MovimentoListaVO.ORDINA_PER_COGNOME_NOME);
		comparators.put(MovimentoListaVO.ORDINAMENTO_COGNOME_NOME_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_COGNOME_NOME));

		comparators.put("03", MovimentoListaVO.ORDINA_PER_SEGNATURA);
		comparators.put("04", ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_SEGNATURA));

		comparators.put("07", MovimentoListaVO.ORDINA_PER_ID_RICHIESTA);
		comparators.put("08", ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_ID_RICHIESTA));

		comparators.put("09", MovimentoListaVO.ORDINA_PER_ATTIVITA);
		comparators.put("10", ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_ATTIVITA));

		// ord. composto per pronotazioni
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(MovimentoListaVO.ORDINA_PER_DATA_INIZIO_PREVISTA);
		chain.addComparator(MovimentoListaVO.ORDINA_PER_SEGNATURA);
		chain.addComparator(MovimentoListaVO.ORDINA_PER_DATA_RICHIESTA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_INIZIO_COLL_DATA_RICH_ASC, chain);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_INIZIO_COLL_DATA_RICH_DESC,
				ValidazioneDati.invertiComparatore(chain));

		// solleciti
		ComparatorChain chain2 = new ComparatorChain();
		chain2.addComparator(MovimentoListaVO.ORDINA_PER_NRO_SOLLECITO);
		chain2.addComparator(MovimentoListaVO.ORDINA_PER_DATA_SOLLECITO);
		chain2.addComparator(MovimentoListaVO.ORDINA_PER_DATA_SCADENZA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_NRO_SOLL_DATA_SOLLECITO_DATA_SCADENZA_ASC, chain2);
		comparators.put(MovimentoListaVO.ORDINAMENTO_NRO_SOLL_DATA_SOLLECITO_DATA_SCADENZA_DESC,
				ValidazioneDati.invertiComparatore(chain2));

		// almaviva5_20100309 #3623
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_INIZIO_CONSEGNA_ASC,
				MovimentoListaVO.ORDINA_PER_DATA_INIZIO_FLAG_CONSEGNA);
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_INIZIO_CONSEGNA_DESC,
				ValidazioneDati.invertiComparatore(MovimentoListaVO.ORDINA_PER_DATA_INIZIO_FLAG_CONSEGNA));

		// almaviva5_20160526 #6113 data scadenza (asc) + n. solleciti (desc) + data invio
		// (asc).
		comparators.put(MovimentoListaVO.ORDINAMENTO_DATA_SCADENZA_NRO_SOLL_DATA_INVIO,
				MovimentoListaVO.ORDINA_PER_DATA_SCADENZA_NRO_SOLL_DATA_INVIO);
	}

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
		try {
			calendario = DomainEJBFactory.getInstance().getCalendario();
			sale = DomainEJBFactory.getInstance().getSale();
		} catch (Exception e) {
		}
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
	}

	private ServiziCommon getServiziCommon() {

		if (serviziCommon != null)
			return serviziCommon;

		try {
			serviziCommon = DomainEJBFactory.getInstance().getServiziCommon();
			return serviziCommon;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	private AmministrazioneBiblioteca getAmministrazioneBib() {

		if (amministrazioneBib != null)
			return amministrazioneBib;

		try {
			amministrazioneBib = DomainEJBFactory.getInstance().getBiblioteca();
			return amministrazioneBib;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	/**
	 * Cancella le modalità pagamento con id contenuto nell'array idModalita.<br/>
	 * Se una modalità ha delle richieste/movimenti ancora aperti non viene
	 * cancellata e il suo identificativo viene inserito nell'array
	 * idModalitaNonCancellate
	 *
	 * @param idSupporti
	 * @param idSupportiNonCancellati
	 * @return
	 * @throws ApplicationException
	 */
	public boolean cancellaModalitaPagamento(String ticket, String[] idModalita, Integer[] idModalitaNonCancellate,
			BaseVO utenteVar) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		ModalitaPagamentoDAO modalitaPagamentoDAO = new ModalitaPagamentoDAO();

		int indiceOut = 0;
		idModalitaNonCancellate = new Integer[] {};

		try {
			checkTicket(ticket);
			for (int i = 0; i < idModalita.length; i++) {
				Integer idModalitaID = new Integer(idModalita[i]);
				Tbl_modalita_pagamento modalita = modalitaPagamentoDAO.getModalitaPagamentoById(idModalitaID);
				if (modalita != null) {
					if (!richiesteDAO.esistonoRichiestePer(modalita)) {
						modalita.setUte_var(utenteVar.getUteVar());
						modalita.setTs_var(utenteVar.getTsVar());
						modalitaPagamentoDAO.cancellaModalitaPagamento(modalita);
					} else {
						idModalitaNonCancellate[indiceOut] = idModalitaID;
						indiceOut++;
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public ModalitaPagamentoVO aggiornaModalitaPagamento(String ticket, ModalitaPagamentoVO modalitaVO)
			throws ApplicationException {

		ModalitaPagamentoDAO modalitaPagamentoDAO = new ModalitaPagamentoDAO();

		try {
			checkTicket(ticket);
			// Controllo se esiste una riga cancellata con lo stesso codice
			Tbl_modalita_pagamento modalita = modalitaPagamentoDAO.getModalitaPagamento(modalitaVO.getCdPolo(),
					modalitaVO.getCdBib(), modalitaVO.getCodModPagamento(), "S");

			if (modalita != null) {
				modalita.setTs_var(modalitaVO.getTsVar());
				modalita.setUte_var(modalitaVO.getUteVar());
				modalita.setFl_canc(modalitaVO.getFlCanc() == null ? 'N' : modalitaVO.getFlCanc().charAt(0));
				modalita.setDescr(modalitaVO.getDescrizione());
			} else
				modalita = ConversioneHibernateVO.toHibernate().modalitaPagamento(modalitaVO);

			Tbl_modalita_pagamento modPag = modalitaPagamentoDAO.aggiornaModalitaPagamento(modalita);
			return ConversioneHibernateVO.toWeb().modalitaPagamento(modPag);

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Torna una lista di ModalitaPagamentoVO relativi alle modalita pagamento
	 * associate alla bilioteca
	 *
	 * @param codPolo
	 * @param codBib
	 * @return
	 * @throws ApplicationException
	 */
	public List<ModalitaPagamentoVO> getModalitaPagamento(String ticket, String codPolo, String codBib)
			throws ApplicationException {

		ModalitaPagamentoDAO modalitaPagamentoDAO = new ModalitaPagamentoDAO();
		List<ModalitaPagamentoVO> listaModalitaVO = new ArrayList<ModalitaPagamentoVO>();

		try {
			checkTicket(ticket);
			List<Tbl_modalita_pagamento> listaModalita = modalitaPagamentoDAO.getModalitaPagamento(codPolo, codBib);
			for (Tbl_modalita_pagamento modalita : listaModalita) {
				listaModalitaVO.add(ConversioneHibernateVO.toWeb().modalitaPagamento(modalita));
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return listaModalitaVO;
	}

	/**
	 * Cancella i supporti con id contenuto nell'array idSupporti.<br/>
	 * Se un supporto ha delle richieste/movimenti ancora aperti non viene
	 * cancellato e il suo identificativo viene inserito nell'array
	 * idSupportiNonCancellati
	 *
	 * @param idSupporti
	 * @param idSupportiNonCancellati
	 * @return
	 * @throws ApplicationException
	 */
	public boolean cancellaSupportiBiblioteca(String ticket, String[] idSupporti, Integer[] idSupportiNonCancellati,
			BaseVO utenteVar) throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		SupportiBibliotecaDAO supportiDAO = new SupportiBibliotecaDAO();
		UtilitaDAO dao = new UtilitaDAO();

		idSupportiNonCancellati = new Integer[idSupporti.length];

		try {
			checkTicket(ticket);
			for (int i = 0; i < idSupporti.length; i++) {
				Tbl_supporti_biblioteca sup = supportiDAO.getSupportoBibliotecaById(new Integer(idSupporti[i]));
				if (sup == null)
					continue;

				if (richiesteDAO.esistonoRichiestePer(sup))
					throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_SUPPORTO_LEGATO_RICHIESTA,
							sup.getCod_supporto());

				// almaviva5_20110419 #4385
				Tbf_biblioteca_in_polo bib = sup.getCd_bib();
				Tbl_parametri_biblioteca params = dao.getParametriBiblioteca(bib.getCd_polo().getCd_polo(),
						bib.getCd_biblioteca());
				if (params != null) {
					String catRip = params.getCd_catriprod_nosbndoc();
					List<TB_CODICI> codici = CodiciProvider
							.getCodiciCross(CodiciType.CODICE_TIPI_RIPRODUZIONE_CODICE_SUPPORTO, catRip, true);
					for (TB_CODICI cod : (codici != null ? codici : ValidazioneDati.emptyList(TB_CODICI.class)))
						if (ValidazioneDati.equals(cod.getCd_tabellaTrim(), sup.getCod_supporto()))
							throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_SUPPORTO_LEGATO_PARAMETRI_BIB,
									sup.getCod_supporto());
				}

				sup.setUte_var(utenteVar.getUteVar());
				sup.setTs_var(utenteVar.getTsVar());
				supportiDAO.cancellaSupportoBiblioteca(sup);

			}
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaSupportoBiblioteca(String ticket, SupportoBibliotecaVO supportoVO)
			throws ApplicationException {

		SupportiBibliotecaDAO supportiDAO = new SupportiBibliotecaDAO();

		try {
			checkTicket(ticket);
			Tbl_supporti_biblioteca supporto = supportiDAO.getSupportoBiblioteca(supportoVO.getCd_polo(),
					supportoVO.getCd_bib(), supportoVO.getCodSupporto(), null);
			if (supporto != null) {
				supporto.setTs_var(supportoVO.getTsVar());
				supporto.setUte_var(supportoVO.getUteVar());
				supporto.setFl_canc(supportoVO.getFlCanc() == null ? 'N' : supportoVO.getFlCanc().charAt(0));
				supporto.setImp_unita(new BigDecimal(supportoVO.getImportoUnitarioDouble()));
				supporto.setCosto_fisso(new BigDecimal(supportoVO.getCostoFissoDouble()));
			} else {
				supporto = ServiziConversioneVO.daWebAHibernateSupportiBiblioteca(supportoVO);
			}
			supportiDAO.aggiornaSupportoBiblioteca(supporto);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean verificaAutoregistrazione(String ticket, String codPolo, String codBib)
			throws DataException, ApplicationException {

		UtilitaDAO dao = new UtilitaDAO();
		boolean ret = false;

		try {
			checkTicket(ticket);
			ret = dao.verificaAutoregistrazione(codPolo, codBib);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}
		return ret;

	}

	public SupportoBibliotecaVO getSupportoBiblioteca(String ticket, String codPolo, String codBib, String codSupporto)
			throws ApplicationException {

		SupportiBibliotecaDAO supportiDAO = new SupportiBibliotecaDAO();

		SupportoBibliotecaVO supportoVO = null;

		Tbl_supporti_biblioteca supporto = null;
		try {
			checkTicket(ticket);
			supporto = supportiDAO.getSupportoBiblioteca(codPolo, codBib, codSupporto, "N");

			if (supporto != null) {
				supportoVO = ServiziConversioneVO.daHibernateAWebSupportoBiblioteca(supporto);
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return supportoVO;
	}

	/**
	 * Torna una Lista di SupportoBibliotecaVO
	 *
	 * @param codPolo
	 * @param codBib
	 * @return
	 * @throws ApplicationException
	 */
	public List getSupportiBiblioteca(String ticket, String codPolo, String codBib, String fl_svolg)
			throws ApplicationException {

		List<SupportoBibliotecaVO> listaSupportiVO = new ArrayList<SupportoBibliotecaVO>();
		SupportiBibliotecaDAO supportiDAO = new SupportiBibliotecaDAO();

		try {
			checkTicket(ticket);
			List<Tbl_supporti_biblioteca> listaSupporti = supportiDAO.getSupportiBiblioteca(codPolo, codBib, fl_svolg);
			if (ValidazioneDati.isFilled(listaSupporti))
				for (Tbl_supporti_biblioteca supporto : listaSupporti)
					listaSupportiVO.add(ServiziConversioneVO.daHibernateAWebSupportoBiblioteca(supporto));

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return listaSupportiVO;
	}

	public ParametriBibliotecaVO aggiornaParametriBiblioteca(String ticket, ParametriBibliotecaVO parametriVO)
			throws ApplicationException {

		UtilitaDAO dao = new UtilitaDAO();
		try {
			Tbl_parametri_biblioteca parametri = ConversioneHibernateVO.toHibernate().parametriBiblioteca(parametriVO);
			parametri = dao.aggiornaParametriBiblioteca(parametri);
			ParametriBibliotecaVO parametriBiblioteca = ConversioneHibernateVO.toWeb().parametriBiblioteca(parametri);

			// aggiorna calendario
			ModelloCalendarioVO mc = parametriVO.getCalendario();
			if (mc != null) {
				ModelloCalendarioVO modelloCalendario = calendario.aggiornaModelloCalendario(ticket, mc);
				parametriBiblioteca.setCalendario(modelloCalendario);
			}

			return parametriBiblioteca;

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public ParametriBibliotecaVO getParametriBiblioteca(String ticket, String codPolo, String codBib)
			throws ApplicationException {
		UtilitaDAO dao = new UtilitaDAO();

		ParametriBibliotecaVO parametriVO = null;

		try {
			Tbl_parametri_biblioteca parametri = dao.getParametriBiblioteca(codPolo, codBib);
			if (parametri != null) {
				parametriVO = ConversioneHibernateVO.toWeb().parametriBiblioteca(parametri);
				// calendario
				parametriVO.setCalendario(calendario.getCalendarioBiblioteca(ticket, codPolo, codBib));
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return parametriVO;
	}

	public boolean cancellaControlloIter(String ticket, String[] codiciControllo, int idTipoServizio,
			String codAttivita, String utenteVar) throws ApplicationException {
		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		try {
			checkTicket(ticket);
			List<String> listaCodiciControllo = new ArrayList<String>();
			for (int i = 0; i < codiciControllo.length; i++) {
				listaCodiciControllo.add(codiciControllo[i]);
			}

			iterServizioDAO.cancellaControlloIter(listaCodiciControllo, idTipoServizio, codAttivita, utenteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaControlloIter(String ticket, FaseControlloIterVO controlloIterVO, int idTipoServizio,
			String codAttivita, boolean nuovo, short posizione, TipoAggiornamentoIter tipoOperazione)
			throws ApplicationException {
		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		boolean ripristino = false;

		try {
			checkTicket(ticket);
			Tbl_controllo_iter controlloIter = iterServizioDAO.getControlloIter(idTipoServizio, codAttivita,
					controlloIterVO.getCodControllo(), "S");

			if (controlloIter == null) {
				controlloIter = ServiziConversioneVO.daWebAHibernateControlloIter(controlloIterVO, idTipoServizio,
						codAttivita, nuovo);
			} else {
				ripristino = true;
				controlloIter.setFl_bloccante(controlloIterVO.isFlagBloc() ? 'S' : 'N');
				controlloIter.setMessaggio(controlloIterVO.getMessaggio());
				controlloIter.setUte_ins(controlloIterVO.getUteIns());
				controlloIter.setUte_var(controlloIterVO.getUteVar());
			}

			iterServizioDAO.aggiornamentoControlloIter(controlloIter, idTipoServizio, codAttivita, tipoOperazione,
					posizione, ripristino);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	/**
	 *
	 * @param idTipoServizio
	 * @param codAttivita
	 * @return Lista di istanze della classe FaseControlloIterVO
	 * @throws ApplicationException
	 */
	public List getControlloIter(String ticket, int idTipoServizio, String codAttivita) throws ApplicationException {

		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		List listaOutput = new ArrayList();

		try {
			checkTicket(ticket);
			List listaControlloIterHibernate = iterServizioDAO.getControlloIter(idTipoServizio, codAttivita);
			if (ValidazioneDati.isFilled(listaControlloIterHibernate)) {
				FaseControlloIterVO controlloVO = null;
				Tbl_controllo_iter controlloIter = null;
				Iterator iterator = listaControlloIterHibernate.iterator();
				while (iterator.hasNext()) {
					controlloIter = (Tbl_controllo_iter) iterator.next();
					controlloVO = ServiziConversioneVO.daHibernateAWebControlloIter(controlloIter);

					// setto la classe che implementa il controllo leggendola
					// dalla TB_CODICI
					TB_CODICI codice = CodiciProvider.cercaCodice(String.valueOf(controlloIter.getCod_controllo()),
							CodiciType.CODICE_FUNZIONE_CONTROLLO_ITER, CodiciRicercaType.RICERCA_CODICE_SBN);
					// almaviva5_20100929 fix migrazione NAP
					if (codice == null)
						continue;
					// almaviva5_20091211
					String classeControllo = codice.getCd_flg2();
					controlloVO.setClasse(classeControllo);

					String richiedeSupporto = codice.getCd_flg3();
					controlloVO.setRichiedeSupporto(ValidazioneDati.equals(richiedeSupporto, "S"));

					String controlloAggiornamento = codice.getCd_flg4();
					controlloVO.setControlloAggiornamento(ValidazioneDati.equals(controlloAggiornamento, "S"));

					String controlloInoltraPrenotazione = codice.getCd_flg5();
					controlloVO
							.setControlloInoltraPrenotazione(ValidazioneDati.equals(controlloInoltraPrenotazione, "S"));

					listaOutput.add(controlloVO);
				}
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			throw new ApplicationException(e.detail);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return listaOutput;
	}

	public boolean aggiornaIter(String ticket, int idTipoServizio, int progr_iter_selezionato, IterServizioVO iter,
			TipoAggiornamentoIter tipoOperazione, boolean nuovo) throws ApplicationException {

		IterServizioDAO iterServizioDAO = new IterServizioDAO();
		Tbl_iter_servizio iterServizio = null;

		try {
			checkTicket(ticket);

			if (iter != null)
				iterServizio = ConversioneHibernateVO.toHibernate().iterServizio(iter, idTipoServizio, nuovo);

			iterServizioDAO.aggiornaIterServizio(idTipoServizio, progr_iter_selezionato, iterServizio, tipoOperazione);

			switch (tipoOperazione) {
			case AGGIUNTA:
				iter.setProgrIter(iterServizio.getProgr_iter());
				break;
			case INSERIMENTO:
				iter.setProgrIter(iterServizio.getProgr_iter());
				break;
			default:
				break;
			}

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean cancellaIterServizio(String ticket, String[] progressiviIter, int idTipoServizio, String utenteVar)
			throws ApplicationException {
		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		try {
			checkTicket(ticket);
			List<Short> listaprogressiviIter = new ArrayList<Short>();
			for (int i = 0; i < progressiviIter.length; i++) {
				listaprogressiviIter.add(new Short(progressiviIter[i]));
			}

			return iterServizioDAO.cancellaIterServizio(idTipoServizio, listaprogressiviIter, utenteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}

	public boolean aggiornaAttivitaBibliotecario(String ticket, List bibliotecariDaAggiungere,
			List bibliotecariDaRimuovere, BaseVO infoBase, int idTipoServizio, String codiceAttivita)
			throws DataException, ApplicationException {
		AttivitaBibliotecarioDAO bibliotecarioDAO = new AttivitaBibliotecarioDAO();
		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		try {
			checkTicket(ticket);
			Tbl_iter_servizio iterServizio = iterServizioDAO.getIterServizio(idTipoServizio, codiceAttivita);

			Trl_attivita_bibliotecario attivita_bibliotecario = null;

			// Aggiorno recod da cancellare
			Iterator iterator = bibliotecariDaRimuovere.iterator();
			while (iterator.hasNext()) {
				attivita_bibliotecario = bibliotecarioDAO.getAttivitaBibliotecario(iterServizio,
						new Integer((String) iterator.next()).intValue(), false);
				attivita_bibliotecario.setFl_canc('S');
				attivita_bibliotecario.setUte_var(infoBase.getUteVar());
				bibliotecarioDAO.cancellazioneAttivitaBibliotecario(attivita_bibliotecario);
			}

			// Inserimento nuovi record
			iterator = bibliotecariDaAggiungere.iterator();
			int idUtenteProfessionale;
			while (iterator.hasNext()) {
				idUtenteProfessionale = new Integer((String) iterator.next()).intValue();
				attivita_bibliotecario = bibliotecarioDAO.getAttivitaBibliotecario(iterServizio, idUtenteProfessionale,
						true);
				if (attivita_bibliotecario == null) {
					attivita_bibliotecario = new Trl_attivita_bibliotecario();
					attivita_bibliotecario.setFl_canc('N');
					attivita_bibliotecario.setId_iter_servizio_id_iter_servizio(iterServizio.getId_iter_servizio());
					attivita_bibliotecario.setId_bibliotecario_id_utente_professionale(idUtenteProfessionale);
					attivita_bibliotecario.setTs_ins(infoBase.getTsIns());
					attivita_bibliotecario.setTs_var(infoBase.getTsVar());
					attivita_bibliotecario.setUte_ins(infoBase.getUteIns());
					attivita_bibliotecario.setUte_var(infoBase.getUteVar());
				} else {
					attivita_bibliotecario.setFl_canc('N');
					attivita_bibliotecario.setUte_ins(infoBase.getUteIns());
					attivita_bibliotecario.setUte_var(infoBase.getUteVar());
					attivita_bibliotecario.setTs_ins(infoBase.getTsIns());
					attivita_bibliotecario.setTs_var(infoBase.getTsVar());
				}
				bibliotecarioDAO.inserimentoAttivitaBibliotecario(attivita_bibliotecario);
				attivita_bibliotecario = null;
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	/**
	 *
	 * @param tipoServizioVO
	 * @return Mappa con due chiavi.<br/>
	 *         <ul>
	 *         <li>BIBLIOTECARI: lista di AnagrafeUtenteProfessionaleVO relativa ai
	 *         bibliotecari non associati all'attività</li>
	 *         <li>BIBLIOTECARI_ASSOCIATI: lista di AnagrafeUtenteProfessionaleVO
	 *         relativa ai bibliotecari già associati all'attività</li>
	 *         </ul>
	 * @throws DataException
	 * @throws ApplicationException
	 */
	public Map getListeAttivitaBibliotecari(String ticket, TipoServizioVO tipoServizioVO, String codAttivita)
			throws DataException, ApplicationException {

		UtentiProfessionaliDAO utentiProfessionaliDAO = new UtentiProfessionaliDAO();

		Map mappaListe = new HashMap();

		try {
			List listaUtentiProfessionali = utentiProfessionaliDAO.getUtentiProfessionali(tipoServizioVO.getCodPolo(),
					tipoServizioVO.getCodBib());
			List listaUtentiProfessionaliAssociati = utentiProfessionaliDAO
					.getUtentiProfessionali(tipoServizioVO.getIdTipoServizio(), codAttivita);

			if (listaUtentiProfessionaliAssociati.size() > 0 && listaUtentiProfessionali.size() > 0) {
				listaUtentiProfessionali.removeAll(listaUtentiProfessionaliAssociati);
			}

			AnagrafeUtenteProfessionaleVO utenteProfessionaleVO = null;

			List listaUtentiProfessionaliAssociatiVO = new ArrayList();
			if (listaUtentiProfessionaliAssociati.size() > 0) {
				Iterator iterator = listaUtentiProfessionaliAssociati.iterator();

				while (iterator.hasNext()) {
					utenteProfessionaleVO = ServiziConversioneVO
							.daHibernateAWebUtenteProfessionale((Tbf_anagrafe_utenti_professionali) iterator.next());
					listaUtentiProfessionaliAssociatiVO.add(utenteProfessionaleVO);
				}
			}

			List listaUtentiProfessionaliVO = new ArrayList();
			if (listaUtentiProfessionali.size() > 0) {
				Iterator iterator = listaUtentiProfessionali.iterator();
				while (iterator.hasNext()) {
					utenteProfessionaleVO = ServiziConversioneVO
							.daHibernateAWebUtenteProfessionale((Tbf_anagrafe_utenti_professionali) iterator.next());
					listaUtentiProfessionaliVO.add(utenteProfessionaleVO);
				}
			}

			mappaListe.put("BIBLIOTECARI", listaUtentiProfessionaliVO);
			mappaListe.put("BIBLIOTECARI_ASSOCIATI", listaUtentiProfessionaliAssociatiVO);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return mappaListe;
	}

	public boolean aggiornaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO tariffeVO,
			boolean nuovo, int idTipoServizio) throws DataException, ApplicationException {

		ModalitaErogazioneDAO modalitaErogazioneDAO = new ModalitaErogazioneDAO();
		try {
			checkTicket(ticket);
			Trl_tariffe_modalita_erogazione tariffe = ServiziConversioneVO
					.daWebAHibernateTariffeModalitaErogazione(tariffeVO, nuovo, idTipoServizio);
			modalitaErogazioneDAO.aggiornamentoTariffeModalitaErogazione(tariffe);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO tariffeVO,
			boolean nuovo, int idSupportiBiblioteca) throws DataException, ApplicationException {

		ModalitaErogazioneDAO modalitaErogazioneDAO = new ModalitaErogazioneDAO();
		try {
			checkTicket(ticket);
			Trl_supporti_modalita_erogazione tariffe = ServiziConversioneVO
					.daWebAHibernateSupportiModalitaErogazione(tariffeVO, nuovo, idSupportiBiblioteca);
			modalitaErogazioneDAO.aggiornamentoSupportiModalitaErogazione(tariffe);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaModalitaErogazione(String ticket, ModalitaErogazioneVO tariffeVO, boolean nuovo)
			throws DataException, ApplicationException {

		ModalitaErogazioneDAO modalitaErogazioneDAO = new ModalitaErogazioneDAO();
		try {
			checkTicket(ticket);
			Tbl_modalita_erogazione tariffe = ServiziConversioneVO.daWebAHibernateModalitaErogazione(tariffeVO, nuovo);
			modalitaErogazioneDAO.aggiornamentoModalitaErogazione(tariffe);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public List<ServizioWebDatiRichiestiVO> getServizioWebDatiRichiesti(String ticket, String codPolo, String codBib,
			String codTipoServizio, String natura) throws ApplicationException {

		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();

		Tbl_servizio_web_dati_richiestiDAO servizioWebDatiRichiestiDAO = new Tbl_servizio_web_dati_richiestiDAO();
		List<ServizioWebDatiRichiestiVO> listaDatiRichiesti = null;
		try {
			checkTicket(ticket);

			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServizio);
			if (tipoServizio == null)
				return listaDatiRichiesti;

			List<Tbl_servizio_web_dati_richiesti> lstServizioWebDatiRichiesti = servizioWebDatiRichiestiDAO
					.getServizioWebDatiRichiesti(codPolo, codBib, codTipoServizio);

			Map<String, ServizioWebDatiRichiestiVO> map = new HashMap<String, ServizioWebDatiRichiestiVO>();
			for (Tbl_servizio_web_dati_richiesti datiRichiesti : lstServizioWebDatiRichiesti)
				map.put(datiRichiesti.getCampo_richiesta() + "",
						ServiziConversioneVO.daHibernateAWebModuloRichiesta(datiRichiesti));

			List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_CAMPO_RICHIESTA_WEB);
			for (TB_CODICI cod : codici) {
				String flg_ordinamento = cod.getCd_flg4();
				String flg_obbligatorio = cod.getCd_flg3();
				String campoRichiesta = cod.getCd_tabella().trim();
				if (ValidazioneDati.strIsNull(campoRichiesta))
					continue;

				ServizioWebDatiRichiestiVO datiRichiestiVO = map.get(campoRichiesta);
				if (datiRichiestiVO == null) {
					// questo campo richiesta non é presente sul db
					datiRichiestiVO = new ServizioWebDatiRichiestiVO();
					datiRichiestiVO.setCodPolo(codPolo);
					datiRichiestiVO.setCodBib(codBib);
					datiRichiestiVO.setCodiceTipoServizio(codTipoServizio);
					datiRichiestiVO.setCampoRichiesta(Integer.valueOf(campoRichiesta));
					datiRichiestiVO.setDescrizione(cod.getDs_tabella());
					datiRichiestiVO.setOrdinamento(flg_ordinamento);
					datiRichiestiVO.setNote(cod.getDs_cdsbn_ulteriore());

					String firmaUtente = DaoManager.getFirmaUtente(ticket);
					datiRichiestiVO.setUteIns(firmaUtente);
					datiRichiestiVO.setUteVar(firmaUtente);

					datiRichiestiVO.setFlCanc("S");
					if (ValidazioneDati.equals(flg_obbligatorio, "S")) {
						datiRichiestiVO.setUtilizzato(true);
						datiRichiestiVO.setFlCanc("N");
						datiRichiestiVO.setObbligatorio(true);
						datiRichiestiVO.setObbligatorioTabCodici(true);
					}
					map.put(datiRichiestiVO.getCampoRichiesta() + "", datiRichiestiVO);

				} else {
					// il dato é sul DB, prendo la descrizione e lo imposto come utilizzato
					// datiRichiestiVO.setUtilizzato(true);
					datiRichiestiVO.setDescrizione(cod.getDs_tabella());
					datiRichiestiVO.setOrdinamento(flg_ordinamento);
					datiRichiestiVO.setNote(cod.getDs_cdsbn_ulteriore());
					if (flg_obbligatorio.equals("S")) {
						datiRichiestiVO.setUtilizzato(true);
						datiRichiestiVO.setFlCanc("N");
						datiRichiestiVO.setObbligatorio(true);
						datiRichiestiVO.setObbligatorioTabCodici(true);
					}
				}
			}

			// almaviva5_20100524 anno periodico (99) obbligatorio per natura='S'
			if (ValidazioneDati.equals(natura, "S") && map.containsKey(ANNO_PERIODICO_99)) {
				ServizioWebDatiRichiestiVO campo = map.get(ANNO_PERIODICO_99);
				campo.setUtilizzato(true);
				campo.setObbligatorio(true);
			}

			listaDatiRichiesti = new ArrayList<ServizioWebDatiRichiestiVO>(map.values());

			// ordino per flg_4 (campo contenente l'ordinamento) della tabella codici
			Collections.sort(listaDatiRichiesti, ordCampiRichiesta);

			return listaDatiRichiesti;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	private static final Comparator<ServizioWebDatiRichiestiVO> ordCampiRichiesta = new Comparator<ServizioWebDatiRichiestiVO>() {

		public int compare(ServizioWebDatiRichiestiVO o1, ServizioWebDatiRichiestiVO o2) {
			try {
				int codRitorno = o1.getOrdinamento().compareTo(o2.getOrdinamento());
				return codRitorno;
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	public boolean aggiornaModuloRichiesta(String ticket,
			List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO) throws EJBException, ApplicationException {

		Tbl_servizio_web_dati_richiestiDAO servizioWebDatiRichiestiDAO = new Tbl_servizio_web_dati_richiestiDAO();
		TipoServizioDAO servizioDAO = new TipoServizioDAO();
		try {
			checkTicket(ticket);
			if (!ValidazioneDati.isFilled(lstServizioWebDatiRichiestiVO))
				return false;

			ServizioWebDatiRichiestiVO webDatiRichiestiVO = lstServizioWebDatiRichiestiVO.get(0);
			Tbl_tipo_servizio tipoServizio = servizioDAO.getTipoServizio(webDatiRichiestiVO.getCodPolo(),
					webDatiRichiestiVO.getCodBib(), webDatiRichiestiVO.getCodiceTipoServizio());

			for (ServizioWebDatiRichiestiVO modRichiesta : lstServizioWebDatiRichiestiVO) {
				Tbl_servizio_web_dati_richiesti servizioWebDatiRichiesti = ServiziConversioneVO
						.daWebAHibernateModuloRichiesta(modRichiesta, tipoServizio);
				servizioWebDatiRichiesti.setUte_var(DaoManager.getFirmaUtente(ticket));
				servizioWebDatiRichiestiDAO.aggiornamentoModuloRichiesta(servizioWebDatiRichiesti);
			}

		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaTipoServizio(String ticket, TipoServizioVO tipoServizioVO)
			throws DataException, ApplicationException {

		TipoServizioDAO dao = new TipoServizioDAO();
		try {
			checkTicket(ticket);
			Tbl_tipo_servizio tipoServizio = dao.getTipoServizio(tipoServizioVO.getCodPolo(),
					tipoServizioVO.getCodBib(), tipoServizioVO.getCodiceTipoServizio(), false);

			tipoServizio = ServiziConversioneVO.daWebAHibernateTipoServizio(tipoServizio, tipoServizioVO);
			dao.aggiornamentoTipoServizio(tipoServizio);
			tipoServizioVO.setIdTipoServizio(tipoServizio.getId_tipo_servizio());

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean aggiornaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio)
			throws DataException, ApplicationException {

		ServiziDAO dao = new ServiziDAO();
		Tbl_servizio servizio;

		try {
			checkTicket(ticket);

			servizioVO.validate(new ServizioBibliotecaValidator());

			// cerco eventuale servizio con stesso codice ma cancellato
			servizio = dao.getServizioBibliotecaCancellato(servizioVO.getCodPolo(), servizioVO.getCodBib(),
					servizioVO.getCodTipoServ(), servizioVO.getCodServ());
			if (servizio != null)
				servizioVO.setIdServizio(servizio.getId_servizio());

			servizio = ServiziConversioneVO.daWebAHibernateServizio(servizioVO, idTipoServizio);

			dao.aggiornamentoServizio(servizio);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean cancellaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio)
			throws DataException, ApplicationException {

		try {
			checkTicket(ticket);
			cancellazioneServizioBiblioteca(ticket, servizioVO.getIdServizio());

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean cancellaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO modErog,
			int idTipoServizio) throws DataException, ApplicationException {

		ModalitaErogazioneDAO dao = new ModalitaErogazioneDAO();
		Trl_tariffe_modalita_erogazione trl_tariffe_modalita_erogazione;

		try {
			checkTicket(ticket);
			trl_tariffe_modalita_erogazione = ServiziConversioneVO.daWebAHibernateTariffeModalitaErogazione(modErog,
					false, idTipoServizio);
			trl_tariffe_modalita_erogazione.setFl_canc('S');
			trl_tariffe_modalita_erogazione.setUte_var(DaoManager.getFirmaUtente(ticket));
			dao.cancellazioneTariffeModalitaErogazione(trl_tariffe_modalita_erogazione);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean cancellaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO modErog,
			int idSupportiBiblioteca) throws DataException, ApplicationException {

		ModalitaErogazioneDAO dao = new ModalitaErogazioneDAO();
		Trl_supporti_modalita_erogazione trl_supporti_modalita_erogazione;

		try {
			checkTicket(ticket);
			trl_supporti_modalita_erogazione = ServiziConversioneVO.daWebAHibernateSupportiModalitaErogazione(modErog,
					false, idSupportiBiblioteca);
			trl_supporti_modalita_erogazione.setFl_canc('S');
			trl_supporti_modalita_erogazione.setUte_var(DaoManager.getFirmaUtente(ticket));
			dao.cancellazioneSupportiModalitaErogazione(trl_supporti_modalita_erogazione);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	public boolean cancellaModalitaErogazione(String ticket, ModalitaErogazioneVO modErog)
			throws DataException, ApplicationException {

		ModalitaErogazioneDAO dao = new ModalitaErogazioneDAO();
		Tbl_modalita_erogazione tbl_modalita_erogazione;

		try {
			checkTicket(ticket);
			tbl_modalita_erogazione = ServiziConversioneVO.daWebAHibernateModalitaErogazione(modErog, false);
			tbl_modalita_erogazione.setFl_canc('S');
			tbl_modalita_erogazione.setUte_var(DaoManager.getFirmaUtente(ticket));
			dao.cancellazioneModalitaErogazione(tbl_modalita_erogazione);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	// autorizzazioni
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
	 *
	 */
	public List getListaAutorizzazioni(String ticket, RicercaAutorizzazioneVO ricercaAut)
			throws ResourceNotFoundException, ApplicationException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		Tbl_tipi_autorizzazioni aut = ServiziConversioneVO.daWebAHibernateRicercaTipiAut(ricercaAut);
		try {
			checkTicket(ticket);

			TB_CODICI tb_codici = null;
			String ordinamento = "";
			if (ValidazioneDati.isFilled(ricercaAut.getOrdinamento())) {
				tb_codici = CodiciProvider.cercaCodice(ricercaAut.getOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_AUTORIZZAZIONI, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (tb_codici != null)
					ordinamento = tb_codici.getDs_cdsbn_ulteriore();
			}
			List listaAutorizzazioni = autDAO.getListaTipiAutorizzazione(aut, ordinamento);

			List listaOutput = new ArrayList();
			for (int i = 0; i < listaAutorizzazioni.size(); i++) {
				AutorizzazioneVO autVO = ServiziConversioneVO
						.daHibernateAWebTipoAutorizzazione((Tbl_tipi_autorizzazioni) listaAutorizzazioni.get(i), i + 1);
				if (ricercaAut.isSoloILL() && !autVO.isILL())
					continue;
				listaOutput.add(autVO);
			}
			return listaOutput;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	// autorizzazioni
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
	 *
	 */
	public List getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente)
			throws ResourceNotFoundException, ApplicationException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		try {
			checkTicket(ticket);
			String codAut = utente.getAutorizzazioni().getCodTipoAutor();
			List serviziAut = autDAO.getListaServiziAutorizzazione(utente.getCodPoloSer(), utente.getCodBibSer(),
					codAut);

			List listaOutput = new ArrayList();
			for (int i = 0; i < serviziAut.size(); i++) {

				ElementoSinteticaServizioVO servizioVO = ServiziConversioneVO
						.daHibernateAWebServizioAutorizzazione((Trl_autorizzazioni_servizi) serviziAut.get(i), i + 1);
				listaOutput.add(servizioVO);
			}

			List list = this.preparaListaServizi(listaOutput, utente);
			return list;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public String getListaAutorizzazioniServizio(String ticket, String codPoloSer, String codBibSer, int idServ)
			throws ResourceNotFoundException, ApplicationException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		try {
			checkTicket(ticket);
			String serviziAut = autDAO.getListaAutorizzazioniServizio(codPoloSer, codBibSer, idServ);
			return serviziAut;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public boolean insertAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws DataException, ApplicationException {

		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		Tbl_tipi_autorizzazioni autorizzazione;

		try {
			checkTicket(ticket);
			// Si crea istanza di Tbl_tipi_autorizzazioni attraverso la classe
			// AllineaVoXHibernateVoXHibernate
			autorizzazione = ServiziConversioneVO.daWebAHibernateAnagrafeAutorizzazioni(recAutorizzazione);

			// List risultato=autDAO.getListaTipiAutorizzazione(autorizzazione, "");
			// verifica esistenza autorizzazione con lo stesso codice ma
			// cancellata
			Tbl_tipi_autorizzazioni esisteCancellata = autDAO.esisteAutorizzazioneCancellata(
					recAutorizzazione.getCodPolo(), recAutorizzazione.getCodBiblioteca(),
					recAutorizzazione.getCodAutorizzazione());
			if (esisteCancellata != null) {
				autorizzazione = esisteCancellata;
				// aggiornamento autorizzazione già esistente
				// flegga
				autorizzazione.setFl_canc('N');
				autorizzazione.setTs_var(recAutorizzazione.getTsVar());
				autorizzazione.setUte_var(recAutorizzazione.getUteVar());
				autDAO.aggiornamentoAutorizzazione(autorizzazione);
			} else {
				// Inserimento nuova autorizzazione
				// verifica esistenza autorizzazione
				boolean esiste = autDAO.esisteAutorizzazione(recAutorizzazione.getCodPolo(),
						recAutorizzazione.getCodBiblioteca(), recAutorizzazione.getCodAutorizzazione(),
						recAutorizzazione.getDesAutorizzazione());
				if (esiste) {
					// throw new ApplicationException("bye bye");
					throw new ApplicationException(SbnErrorTypes.SRV_AUTORIZZAZIONE_ESISTENTE);
					// throw new EJBException("Errore in generazione blocchi");
				}
				autDAO.inserimentoAutorizzazione(autorizzazione, recAutorizzazione.getCodPolo(),
						recAutorizzazione.getCodBiblioteca());
			}

			// Inserimento servizi associati
			this.inserimentoServiziAssociati(recAutorizzazione, autorizzazione);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
		// return dao.insertAutorizzazione(recAutorizzazione);
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
	 *
	 */
	public boolean updateAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws DataException, ApplicationException {

		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		Tbl_tipi_autorizzazioni autorizzazione;
		try {
			checkTicket(ticket);
			// creo una nuova istanza di Tbl_tipi_autorizzazioni
			autorizzazione = ServiziConversioneVO.daWebAHibernateAnagrafeAutorizzazioni(recAutorizzazione);

			autDAO.aggiornamentoAutorizzazione(autorizzazione);
			this.aggiornamentoServiziAssociati(recAutorizzazione, autorizzazione);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return true;
		// return dao.updateAutorizzazione(recAutorizzazione);
	}

	private boolean inserimentoServiziAssociati(AutorizzazioneVO recAutorizzazione,
			Tbl_tipi_autorizzazioni autorizzazione) throws DataException, ApplicationException {
		ServiziDAO dao = new ServiziDAO();
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		ElementoSinteticaServizioVO servizio = null;
		Tbl_servizio tbl_servizio = null;
		List listaServizi;
		Trl_autorizzazioni_servizi autorizzazione_servizio = null;

		listaServizi = recAutorizzazione.getElencoServizi();
		Iterator iterator = listaServizi.iterator();
		try {
			while (iterator.hasNext()) {
				servizio = (ElementoSinteticaServizioVO) iterator.next();
				if (servizio.getStato() == ElementoSinteticaServizioVO.NEW) {
					tbl_servizio = dao.getServizioBiblioteca(servizio.getCodPolo(), servizio.getCodBiblioteca(),
							servizio.getTipServizio(), servizio.getCodServizio());
					Trl_autorizzazioni_servizi esisteDirCanc = autDAO.getServizioAutorizzazione(servizio.getCodPolo(),
							servizio.getCodBiblioteca(), recAutorizzazione.getIdAutorizzazione(),
							servizio.getIdServizio(), recAutorizzazione.getCodAutorizzazione(), 'S');
					if (esisteDirCanc != null) {
						autorizzazione_servizio = esisteDirCanc;
						// flegga
						autorizzazione_servizio.setFl_canc('N');
						autorizzazione_servizio.setTs_var(recAutorizzazione.getTsVar());
						autorizzazione_servizio.setUte_var(recAutorizzazione.getUteVar());
						autDAO.aggiornamentoAutorizzazioneServizio(autorizzazione_servizio);
					} else {
						autorizzazione_servizio = new Trl_autorizzazioni_servizi();
						autorizzazione_servizio.setFl_canc('N');
						autorizzazione_servizio.setTs_ins(recAutorizzazione.getTsIns());
						autorizzazione_servizio.setTs_var(recAutorizzazione.getTsVar());
						autorizzazione_servizio.setUte_ins(recAutorizzazione.getUteIns());
						autorizzazione_servizio.setUte_var(recAutorizzazione.getUteVar());
						autorizzazione_servizio.setId_servizio(tbl_servizio);
						autorizzazione_servizio.setId_tipi_autorizzazione(autorizzazione);

						autDAO.inserimentoAutorizzazioneServizio(autorizzazione_servizio);
					}

				}
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
		return true;
	}

	private boolean aggiornamentoServiziAssociati(AutorizzazioneVO aut, Tbl_tipi_autorizzazioni autorizzazione)
			throws DataException, ApplicationException {

		ServiziDAO serviziDAO = new ServiziDAO();
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
		Trl_diritti_utenteDAO trl_dirDAO = new Trl_diritti_utenteDAO();
		UtentiDAO utentiDao = new UtentiDAO();

		ElementoSinteticaServizioVO servizio = null;
		Tbl_servizio tbl_servizio = null;
		Trl_autorizzazioni_servizi aut_srv = null;

		List<ElementoSinteticaServizioVO> listaServizi = aut.getElencoServizi();
		List<Trl_autorizzazioni_servizi> serviziAutGiaPresenti = null;

		try {
			serviziAutGiaPresenti = autDAO.getListaServiziAutorizzazione(aut.getCodPolo(), aut.getCodBiblioteca(),
					aut.getCodAutorizzazione());
			Set<Integer> idServiziGiaAss = getIdServiziAutGiaAssociati(serviziAutGiaPresenti);
			Set<Integer> idServiziDaAss = getIdServiziDaAssociare(aut.getElencoServizi());

			// Scorro lista servizi da inserire
			Iterator<ElementoSinteticaServizioVO> i = listaServizi.iterator();
			while (i.hasNext()) {
				servizio = i.next();
				int idServizio = servizio.getIdServizio();
				Tbl_servizio srv = serviziDAO.getServizioBibliotecaById(idServizio);
				// almaviva5_20130830 #5397
				if (srv == null || ValidazioneDati.in(srv.getFl_canc(), 's', 'S')
						|| ValidazioneDati.in(srv.getId_tipo_servizio().getFl_canc(), 's', 'S'))
					if (ValidazioneDati.in(servizio.getStato(), ElementoSinteticaServizioVO.NEW,
							ElementoSinteticaServizioVO.OLD))
						throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO,
								servizio.getDescrizioneTipoServizio());

				if (!idServiziGiaAss.contains(idServizio)) {
					// Si tratta di un nuovo servizio. Va inserito
					tbl_servizio = serviziDAO.getServizioBiblioteca(servizio.getCodPolo(), servizio.getCodBiblioteca(),
							servizio.getTipServizio(), servizio.getCodServizio());
					Trl_autorizzazioni_servizi esisteDirCanc = autDAO.getServizioAutorizzazione(servizio.getCodPolo(),
							servizio.getCodBiblioteca(), aut.getIdAutorizzazione(), idServizio,
							aut.getCodAutorizzazione(), 'S');
					if (esisteDirCanc != null) {
						// flegga
						esisteDirCanc.setFl_canc('N');
						esisteDirCanc.setTs_var(aut.getTsVar());
						esisteDirCanc.setUte_var(aut.getUteVar());
						autDAO.aggiornamentoAutorizzazioneServizio(esisteDirCanc);
						aut_srv = esisteDirCanc;
					} else {
						// inserisci
						aut_srv = new Trl_autorizzazioni_servizi();
						aut_srv.setFl_canc('N');
						aut_srv.setTs_ins(aut.getTsIns());
						aut_srv.setTs_var(aut.getTsVar());
						aut_srv.setUte_ins(aut.getUteIns());
						aut_srv.setUte_var(aut.getUteVar());
						aut_srv.setId_servizio(tbl_servizio);
						aut_srv.setId_tipi_autorizzazione(autorizzazione);

						autDAO.inserimentoAutorizzazioneServizio(aut_srv);
					}
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI
					// CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESO COD_AUTORIZZAZIONE E inserisco
					// I DIRiTTI
					// Tbl_utenti id_utenti, Tbl_servizio
					List<Tbl_utenti> listaUtenti = utentiDao.getUtenteByAut(servizio.getCodPolo(),
							servizio.getCodBiblioteca(), aut.getCodAutorizzazione(), false);
					Date dataFineAut = null;
					Date dataInizioAut = null;
					Date dataSospInizio = null;
					Date dataSospFine = null;

					for (int idx = 0; idx < listaUtenti.size(); idx++) {
						Tbl_utenti ute_single = listaUtenti.get(idx);
						// ricerca della data di fine autorizzazione
						if (listaUtenti.get(idx).getTrl_utenti_biblioteca() != null) {
							try {
								Object[] elencoUteBibl = listaUtenti.get(idx).getTrl_utenti_biblioteca().toArray();
								for (int j = 0; j < elencoUteBibl.length; j++) {
									Trl_utenti_biblioteca eleUteBibl = (Trl_utenti_biblioteca) elencoUteBibl[j];
									if (eleUteBibl.getCod_tipo_aut().trim().equals(aut.getCodAutorizzazione().trim())
											&& eleUteBibl.getCd_biblioteca().getCd_biblioteca()
													.equals(servizio.getCodBiblioteca())) {
										dataFineAut = eleUteBibl.getData_fine_aut();
										dataInizioAut = eleUteBibl.getData_inizio_aut();
										dataSospInizio = eleUteBibl.getData_inizio_sosp();
										dataSospFine = eleUteBibl.getData_fine_sosp();
									}
								}
							} catch (Exception e) {
								log.error("", e);
							}
						}

						// listaUtenti.setFl_canc('N');
						// DA QUI
						// controllo L'ESISTENZA di un record non cancellato che appartenga alla stessa
						// famiglia del servizio a cui il diritto preso in considerazione appartiene
						Trl_diritti_utente dirittoEsistente = utentiDao
								.verificaEsistenzaServizioDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						// Trl_diritti_utente diritto =
						// utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						// diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte,
						// diritto);
						// tbl_utenti utente, Tbl_servizio servizio, Trl_diritti_utente diritto_utente
						if (dirittoEsistente != null) {
							// esiste un diritto legato alla stessa famiglia del servizio
							if (dirittoEsistente.getId_servizio_id_servizio() == tbl_servizio.getId_servizio()) {
								// diritto già associato come personalizzazione: non faccio nulla
							}
							if (dirittoEsistente.getId_servizio_id_servizio() != tbl_servizio.getId_servizio()) {
								// diritto da cancellare logicamente per inserire il nuovo diritto della stessa
								// famiglia
								// cancellazione logica di quello trovato
								// RICHIESTA CONTARDI DEL 03.05.10 - vince sempre il diritto personalizzato
								// rispetto a quello ereditato per la stessa famiglia di servizio
								// diritto della stessa famiglia già associato come personalizzazione: non
								// faccio nulla

								/*
								 * dirittoEsistente.setFl_canc('S');
								 * dirittoEsistente.setTs_var(recAutorizzazione.getTsVar());
								 * dirittoEsistente.setUte_var(recAutorizzazione.getUteVar());
								 * utentiDao.aggiornaDirittoUtente(dirittoEsistente);
								 */

							}
						}
						if (dirittoEsistente == null) {// RICHIESTA CONTARDI DEL 03.05.10 (IN ASSENZA DI
														// PERSONALIZZAZIONI)
							// controllo esistenza del nuovo diritto per verificare se presente ma
							// cancellato logicamente
							Trl_diritti_utente diritto = utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single,
									tbl_servizio);
							// diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte,
							// diritto);
							// tbl_utenti utente, Tbl_servizio servizio, Trl_diritti_utente diritto_utente
							if (diritto == null) {
								Trl_diritti_utente dirittoNew = new Trl_diritti_utente();
								dirittoNew.setFl_canc('N');
								dirittoNew.setTs_ins(aut.getTsIns());
								dirittoNew.setTs_var(aut.getTsVar());
								dirittoNew.setUte_ins(aut.getUteIns());
								dirittoNew.setUte_var(aut.getUteVar());
								dirittoNew.setData_inizio_serv(Calendar.getInstance().getTime());

								dirittoNew.setData_fine_serv(null);
								if (dataFineAut != null) {
									dirittoNew.setData_fine_serv(dataFineAut);
								}

								dirittoNew.setData_inizio_sosp(null);
								if (dataSospInizio != null) {
									dirittoNew.setData_inizio_sosp(dataSospInizio);
								}

								dirittoNew.setData_fine_sosp(null);
								if (dataSospFine != null) {
									dirittoNew.setData_fine_sosp(dataSospFine);
								}

								dirittoNew
										.setId_servizio_id_servizio(new Integer(aut_srv.getId_servizio_id_servizio()));
								dirittoNew.setNote("");
								dirittoNew.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); // 20.01.10
								utentiDao.inserisciDirittoUtente(ute_single, tbl_servizio, dirittoNew);
							} else {
								if (diritto.getFl_canc() == 'S') {
									diritto.setFl_canc('N');
									// date da aggiornare
									diritto.setData_inizio_serv(Calendar.getInstance().getTime());
									if (dataFineAut != null) {
										diritto.setData_fine_serv(dataFineAut);
									}
									if (dataSospInizio != null) {
										diritto.setData_inizio_sosp(dataSospInizio);
									}
									diritto.setData_fine_sosp(null);
									if (dataSospFine != null) {
										diritto.setData_fine_sosp(dataSospFine);
									}
									diritto.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); // 20.01.10
									utentiDao.aggiornaDirittoUtente(diritto);
								}
							}
						}

						// controllo esistenza del nuovo diritto per verificare se presente ma
						// cancellato logicamente
						/*
						 * Trl_diritti_utente diritto =
						 * utentiDao.verificaEsistenzaDirittoUtenteBiblioteca(ute_single, tbl_servizio);
						 * //diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte,
						 * diritto); //tbl_utenti utente, Tbl_servizio servizio, Trl_diritti_utente
						 * diritto_utente if (diritto==null) { Trl_diritti_utente dirittoNew= new
						 * Trl_diritti_utente(); dirittoNew.setFl_canc('N');
						 * dirittoNew.setTs_ins(recAutorizzazione.getTsIns());
						 * dirittoNew.setTs_var(recAutorizzazione.getTsVar());
						 * dirittoNew.setUte_ins(recAutorizzazione.getUteIns());
						 * dirittoNew.setUte_var(recAutorizzazione.getUteVar());
						 * dirittoNew.setData_inizio_serv(Calendar.getInstance().getTime());
						 * dirittoNew.setData_fine_serv(null); if (dataFineAut!=null) {
						 * dirittoNew.setData_fine_serv(dataFineAut); }
						 * dirittoNew.setData_inizio_sosp(null); if (dataSospInizio!=null) {
						 * dirittoNew.setData_inizio_sosp(dataSospInizio); }
						 * dirittoNew.setData_fine_sosp(null); if (dataSospFine!=null) {
						 * dirittoNew.setData_fine_sosp(dataSospFine); }
						 * dirittoNew.setId_servizio_id_servizio(new
						 * Integer(autorizzazione_servizio.getId_servizio_id_servizio()));
						 * dirittoNew.setNote("");
						 * dirittoNew.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim());
						 * //20.01.10 utentiDao.inserisciDirittoUtente(ute_single,tbl_servizio,
						 * dirittoNew); } else { if (diritto.getFl_canc()=='S') {
						 * diritto.setFl_canc('N'); // date da aggiornare
						 * diritto.setData_inizio_serv(Calendar.getInstance().getTime()); if
						 * (dataFineAut!=null) { diritto.setData_fine_serv(dataFineAut); } if
						 * (dataSospInizio!=null) { diritto.setData_inizio_sosp(dataSospInizio); }
						 * diritto.setData_fine_sosp(null); if (dataSospFine!=null) {
						 * diritto.setData_fine_sosp(dataSospFine); }
						 * diritto.setCod_tipo_aut(recAutorizzazione.getCodAutorizzazione().trim());
						 * //20.01.10 utentiDao.aggiornaDirittoUtente(diritto); } }
						 */
						// A QUI

					}

				}
			}

			// Scorro lista servizi gia associati per stabilire se qualche
			// servizio va cancellato
			Iterator iteratorGiaAss = serviziAutGiaPresenti.iterator();
			while (iteratorGiaAss.hasNext()) {
				aut_srv = (Trl_autorizzazioni_servizi) iteratorGiaAss.next();

				if (!idServiziDaAss.contains(new Integer(aut_srv.getId_servizio_id_servizio()))) {
					// L'associazione è da cancellare in quanto non più
					// contenuta tra i servizi da associare
					aut_srv.setFl_canc('S');
					aut_srv.setTs_var(aut.getTsVar());
					aut_srv.setUte_var(aut.getUteVar());
					autDAO.aggiornamentoAutorizzazioneServizio(aut_srv);
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI
					// CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESSO COD_AUTORIZZAZIONE E FLAGGO I
					// DIRiTTI
					List<Trl_diritti_utente> ute_dir = trl_dirDAO
							.getDirittoUtenteByID(aut_srv.getId_servizio().getId_tipo_servizio().getCd_bib(),
									new Integer(aut_srv.getId_servizio_id_servizio()), aut.getCodAutorizzazione(),
									false);
					if (ute_dir != null && ute_dir.size() > 0) {
						// si devono flaggare i diritti_utente degli utenti con la specifica
						// autorizzazione
						for (int idx = 0; idx < ute_dir.size(); idx++) {
							Trl_diritti_utente ute_dirSingle = ute_dir.get(idx);
							ute_dirSingle.setFl_canc('S');
							utentiDao.aggiornaDirittoUtente(ute_dirSingle);
						}
					}
				} else {
					// *************************************************************************************************************************
					// TALE CONDIZIONE ALTERNATIVA E' STATA AGGIUNTA TRANSITORIAMENTE PER ADEGUARE
					// RAPIDAMENTE IL CAMPO COD_TIP_AUT SE EREDITATO
					// POTRA' ESSERE RIMOSSA A REGIME
					// *************************************************************************************************************************
					// L'associazione è già presente
					// aggiorno solo il codice autorizzazione
					// 01.12.09 almaviva4 NON E' SUFFICIENTE OCCORRE AGGIORNARE ANCHE TUTTI I LEGAMI
					// CON GLI UTENTI
					// LEGGO Trl_diritti_utente CON ID SERVIZIO
					// MATCH CON ID UTENTE_BIBLIOTECA CHE HANNO STESSO COD_AUTORIZZAZIONE
					List<Trl_diritti_utente> ute_dir = trl_dirDAO
							.getDirittoUtenteByID(aut_srv.getId_servizio().getId_tipo_servizio().getCd_bib(),
									new Integer(aut_srv.getId_servizio_id_servizio()), aut.getCodAutorizzazione(),
									false);
					if (ute_dir != null && ute_dir.size() > 0) {
						for (int idx = 0; idx < ute_dir.size(); idx++) {
							Trl_diritti_utente ute_dirSingle = ute_dir.get(idx);
							ute_dirSingle.setCod_tipo_aut(aut.getCodAutorizzazione().trim()); // 20.01.10
							utentiDao.aggiornaDirittoUtente(ute_dirSingle);
						}
					}
				}

			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return true;
	}

	private Set<Integer> getIdServiziAutGiaAssociati(List<Trl_autorizzazioni_servizi> servizi)
			throws DataException, ApplicationException {
		Set<Integer> idServizi = new HashSet<Integer>();

		Iterator<Trl_autorizzazioni_servizi> iterator = servizi.iterator();
		while (iterator.hasNext()) {
			idServizi.add(iterator.next().getId_servizio_id_servizio());
		}

		return idServizi;
	}

	private Set<Integer> getIdServiziDaAssociare(List<ElementoSinteticaServizioVO> servizi)
			throws DataException, ApplicationException {
		Set<Integer> idServizi = new HashSet<Integer>();

		Iterator<ElementoSinteticaServizioVO> iterator = servizi.iterator();
		while (iterator.hasNext()) {
			idServizi.add(iterator.next().getIdServizio());
		}

		return idServizi;
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
	 *
	 */
	public boolean cancelAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws DataException, ApplicationException {
		boolean ret = true;

		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		try {
			checkTicket(ticket);
			// Cancellazione autorizzazione
			Tbl_tipi_autorizzazioni autorizzazione = autDAO
					.getTipoAutorizzazioneById(recAutorizzazione.getIdAutorizzazione());
			autorizzazione.setFl_canc('S');
			autDAO.aggiornamentoAutorizzazione(autorizzazione);

			// Cancellazione servizi associati all'autorizzazione
			Set autorizzazione_servizi = autorizzazione.getTrl_autorizzazioni_servizi();
			Trl_autorizzazioni_servizi aut_serv = null;
			Iterator iterator = autorizzazione_servizi.iterator();
			while (iterator.hasNext()) {
				aut_serv = (Trl_autorizzazioni_servizi) iterator.next();
				if (aut_serv.getFl_canc() == 'N') {
					aut_serv.setFl_canc('S');
					autDAO.aggiornamentoAutorizzazioneServizio(aut_serv);
				}
			}

		} catch (Exception e) {
			// ret=false;
			throw new ApplicationException(e);
		}

		return ret;

		// return dao.cancelAutorizzazione(recAutorizzazione);
	}

	public boolean esistonoUtentiCon(String ticket, String codPolo, String codBib, String codAutorizzazione)
			throws DataException, ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		boolean ret = false;

		try {
			checkTicket(ticket);
			ret = dao.esistonoUtentiCon(codPolo, codBib, codAutorizzazione);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}
		return ret;
	}

	public boolean esistonoUtentiConOcc(String ticket, String codPolo, String codBib, int idOcc)
			throws DataException, ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		boolean ret = false;

		try {
			checkTicket(ticket);
			ret = dao.esistonoUtentiConOcc(codPolo, codBib, idOcc);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}
		return ret;
	}

	public boolean esistonoUtentiConSpecTit(String ticket, String codPolo, String codBib, int idSpecTit)
			throws DataException, ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		boolean ret = false;

		try {
			checkTicket(ticket);
			ret = dao.esistonoUtentiConSpecTit(codPolo, codBib, idSpecTit);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}
		return ret;
	}

	/**
	 *
	 * @param codicePolo
	 * @param codiceBiblioteca
	 * @param codTipoServizio
	 * @return Lista di elementi di AnagServizioVO
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	public List getListaServiziPerTipoServizio(String ticket, String codicePolo, String codiceBiblioteca,
			String codTipoServizio) throws ApplicationException {
		ServiziDAO serviziDAO = new ServiziDAO();

		List listaServizi = new ArrayList();

		try {
			checkTicket(ticket);
			List listaServiziHibernate = serviziDAO.getListaServiziPerTipoServizio(codicePolo, codiceBiblioteca,
					codTipoServizio);
			Iterator iterator = listaServiziHibernate.iterator();

			int count = 0;
			while (iterator.hasNext()) {
				// listaServizi.add(
				// AllineaVoXHibernate.daHibernateAWebAnagServizio
				// ((Tbl_servizio)iterator.next(), count) );
				listaServizi.add(ServiziConversioneVO.daHibernateAWebServizio((Tbl_servizio) iterator.next()));
				count++;
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		return listaServizi;
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
	 *
	 */
	public List getListaServizi(String ticket, String codicePolo, String codiceBiblioteca,
			String codiceAutorizzazione) throws ResourceNotFoundException, ApplicationException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
		ServiziDAO serviziDAO = new ServiziDAO();

		List listaServizi = new ArrayList();

		List listaAutorizzazioniServizi = null;
		try {
			checkTicket(ticket);
			listaAutorizzazioniServizi = autDAO.getListaServiziAutorizzazione(codicePolo, codiceBiblioteca,
					codiceAutorizzazione);
			Tbl_servizio servizio = null;
			Trl_autorizzazioni_servizi autServ = null;

			Iterator iterator = listaAutorizzazioniServizi.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				autServ = (Trl_autorizzazioni_servizi) iterator.next();

				servizio = serviziDAO.getServizioBibliotecaById(autServ.getId_servizio_id_servizio());

				listaServizi.add(ServiziConversioneVO.daHibernateAWebAnagServizio(servizio, count + 1));
				count++;
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return listaServizi;
		// return dao.getListaServizi(codiceBiblioteca, codiceAutorizzazione);
	}

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServizio
	 * @param codServizio
	 * @return
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 */
	public ServizioBibliotecaVO getServizioBiblioteca(String ticket, String codPolo, String codBib,
			String codTipoServizio, String codServizio) throws EJBException {
		ServiziDAO serviziDAO = new ServiziDAO();

		ServizioBibliotecaVO servizioVO = null;

		try {
			checkTicket(ticket);
			Tbl_servizio servizio = serviziDAO.getServizioBiblioteca(codPolo, codBib, codTipoServizio, codServizio);
			if (servizio != null) {
				servizioVO = ServiziConversioneVO.daHibernateAWebServizio(servizio);
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return servizioVO;
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
	 *
	 */
	public List getListaServiziBiblioteca(String ticket, List serviziAssociati, String codicePolo,
			String codiceBiblioteca) throws ResourceNotFoundException, ApplicationException {
		ServiziDAO serviziDAO = new ServiziDAO();

		List serviziBib;
		try {
			checkTicket(ticket);
			List listaCodiciTipoServizio = getListaCodiciTipoServizio(serviziAssociati);
			serviziBib = serviziDAO.getListaServiziBiblioteca(listaCodiciTipoServizio, codicePolo, codiceBiblioteca);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		// trasformo lista di tbl_servizio in lista AnagServizioVO
		List listaServizi = new ArrayList();
		for (int i = 0; i < serviziBib.size(); i++) {
			listaServizi.add(ServiziConversioneVO.daHibernateAWebAnagServizio((Tbl_servizio) serviziBib.get(i), i + 1));
		}

		return listaServizi;
	}

	private List getListaCodiciTipoServizio(List serviziAssociati)
			throws ResourceNotFoundException, ApplicationException {
		Iterator iterator = serviziAssociati.iterator();
		ElementoSinteticaServizioVO servizio = null;
		List listaCodiciServiziAssociati = new ArrayList();
		while (iterator.hasNext()) {
			servizio = (ElementoSinteticaServizioVO) iterator.next();
			listaCodiciServiziAssociati.add(servizio.getTipServizio());
		}
		return listaCodiciServiziAssociati;
	}

	public List<MovimentoListaVO> getListeTematiche(String ticket, MovimentoVO filtroMov, boolean attivitaAttuale)
			throws ResourceNotFoundException, ApplicationException, RemoteException {

		try {
			checkTicket(ticket);
			RichiesteServizioDAO dao = new RichiesteServizioDAO();

			MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) filtroMov;

			// almaviva5_20141021 servizi ill
			String flSvolg = movRicerca.getFlSvolg();

			List<MovimentoListaVO> output = new ArrayList<MovimentoListaVO>();
			ScrollableResults richieste = null;

			try {
				richieste = dao.cursor_getListaRichiesteFiltriTematici(movRicerca.getCodPolo(),
						movRicerca.getCodBibOperante(), flSvolg, movRicerca.getCodTipoServ(),
						movRicerca.getCodStatoMov(), movRicerca.getCodStatoRic(), movRicerca.getCod_erog(),
						movRicerca.getCodAttivita(), movRicerca.getTsDataFinePrev_da(),
						movRicerca.getTsDataFinePrev_a(), attivitaAttuale, null, null, null, null, null, null, null,
						null, null, false, movRicerca.getFiltroColl());
				Session session = dao.getCurrentSession();
				int cnt = 0;

				while (richieste.next()) {
					Vl_richiesta_servizio req = (Vl_richiesta_servizio) richieste.get(0);
					MovimentoListaVO movimento = ConversioneHibernateVO.toWeb().movimentoLista(req);
					// MovimentoListaVO movimento =
					// ServiziConversioneVO.daHibernateAWebMovimentoLista(req, null, now);
					// this.decodificaCodiciMovimento(movimento, movRicerca.getCodPolo(),
					// movRicerca.getCodBibOperante(), null, req);
					// impostaFlagConsegnato(movRicerca, movimento, req, iter_cache);

					output.add(movimento);

					if ((++cnt % (filtroMov.getElemPerBlocchi() * 10)) == 0)
						session.clear();
				}

			} finally {
				richieste.close();
			}

			ordinaListaMovimenti(output, movRicerca.getTipoOrdinamento());

			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public List<StampaServiziCorrentiVO> getStampaListeTematiche(String ticket, MovimentoVO filtroMov,
			boolean attivitaAttuale) throws ResourceNotFoundException, ApplicationException, RemoteException {

		try {
			checkTicket(ticket);
			RichiesteServizioDAO dao = new RichiesteServizioDAO();

			String descrBib = null;

			MovimentoRicercaVO mov = (MovimentoRicercaVO) filtroMov;

			Tbf_biblioteca_in_poloDao daoBib = new Tbf_biblioteca_in_poloDao();
			Tbf_biblioteca_in_polo biblioteca = daoBib.select(mov.getCodPolo(), mov.getCodBibOperante());
			if (biblioteca != null) {
				descrBib = biblioteca.getDs_biblioteca();
			} else {
				descrBib = "Descrizione biblioteca mancante";
			}
			String tipoOrd = null;
			List<Tbl_richiesta_servizio> richieste = dao.getListaRichiesteFiltriTematici(mov.getCodPolo(),
					mov.getCodBibOperante(), mov.getFlSvolg(), mov.getCodTipoServ(), mov.getCodStatoMov(),
					mov.getCodStatoRic(), mov.getCod_erog(), mov.getCodAttivita(), mov.getDataInizioEff(),
					mov.getDataFineEff(), attivitaAttuale, tipoOrd, mov.getCodBibInv(), mov.getCodSerieInv(),
					mov.getCodInvenInv(), mov.getSegnatura(), mov.getCodBibDocLett(), mov.getTipoDocLett(),
					mov.getCodDocLet(), mov.getProgrEsempDocLet(), true, mov.getFiltroColl());

			int size = ValidazioneDati.size(richieste);
			List<StampaServiziCorrentiVO> output = new ArrayList<StampaServiziCorrentiVO>(size);

			for (Tbl_richiesta_servizio req : richieste) {
				MovimentoPerStampaServCorrVO movimento = ServiziConversioneVO.daHibernateAWebMovimentoPerStampa(req,
						null, ticket);
				// this.decodificaCodiciMovimento(movimento, movRicerca.getCodPolo(),
				// movRicerca.getCodBibOperante(), null, req);
				StampaServiziCorrentiVO sscdVO = new StampaServiziCorrentiVO(
						FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);
				sscdVO.setCodBib(mov.getCodBibOperante());//
				sscdVO.setDescrBib(descrBib);//
				sscdVO.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
				sscdVO.setCodPolo(mov.getCodPolo());
				sscdVO.setCodAttivita(mov.getCodAttivita());
				sscdVO.setDataDa("");
				sscdVO.setDataDa("");
				sscdVO.setDataIns(movimento.getTsIns().toString());
				sscdVO.setDataAgg(movimento.getTsVarString());
				sscdVO.setDataInsDate(movimento.getTsIns());
				sscdVO.setDataAggDate(movimento.getTsVar());
				sscdVO.setProgrRichiesta(String.valueOf(movimento.getIdRichiesta()));
				// almaviva2 - bug 0004070
				if (movimento.getCodStatoRic() != null && !movimento.getCodStatoRic().trim().equals("")) {
					TB_CODICI tb_codici = CodiciProvider.cercaCodice(String.valueOf(movimento.getCodStatoRic().trim()),
							CodiciType.CODICE_STATO_RICHIESTA, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null) {
						sscdVO.setStatoRichiesta(tb_codici.getDs_tabella());
					}
				} else {
					sscdVO.setStatoRichiesta("");
				}
				//
				sscdVO.setCodBibTipoServ(movimento.getCodBibTipoServ());
				sscdVO.setTipoSrevizio(movimento.getCodTipoServ());
				sscdVO.setCodServizio(movimento.getCodServ());
				sscdVO.setDataInizioMov(movimento.getDataInizioEffString());
				sscdVO.setDataFineMov(movimento.getDataFineEffString());
				sscdVO.setDataFinePrevMov(movimento.getDataFinePrevString());// bug 0004997bis

				if (movimento.getDataInizioEff() != null && !movimento.getDataInizioEff().equals("")) {// bug 0004997
					sscdVO.setDataInizioMovDate(movimento.getDataInizioEff());
				} else {
					sscdVO.setDataInizioMovDate(movimento.getDataInizioPrev());
				}
				// sscdVO.setDataInizioMovDate(movimento.getDataInizioEff());//bug 0004997
				if (movimento.getDataFineEff() != null && !movimento.getDataFineEff().equals("")) {// bug 0004997
					sscdVO.setDataFineMovDate(movimento.getDataFineEff());
				} else {
					sscdVO.setDataFineMovDate(movimento.getDataFinePrev());
				}
				// sscdVO.setDataFineMovDate(movimento.getDataFineEff());//bug 0004997
				// bug 0004997bis dopo nota Contardi per prospettazione in stampa della data
				// fine mov prevista
				// fermo restando i controlli inseriti prima della nota
				sscdVO.setDataFinePrevMovDate(movimento.getDataFinePrev());// bug 0004997bis

				sscdVO.setProgrMovimento(movimento.getCodStatoMov());
				sscdVO.setNumRinnovi(String.valueOf(movimento.getNumRinnovi()));
				sscdVO.setNoteBilioteca(movimento.getNoteBibliotecario());
				if (movimento.getCostoServizio() != null && !movimento.getCostoServizio().trim().equals("")) {
					sscdVO.setCostoServizio(movimento.getCostoServizioDouble(), NUMBER_FORMAT_PREZZI,
							movimento.getLocale());
				} else {
					sscdVO.setCostoServizio(0f, NUMBER_FORMAT_PREZZI, movimento.getLocale());
				}
				if (movimento.getCodStatoMov().equals("A") && movimento.getCodStatoRic().equals("A")) {
					sscdVO.setPrenotazione("SI");
				} else {
					sscdVO.setPrenotazione("");
				}
				sscdVO.setStatoMovimento(movimento.getCodStatoMov());
				sscdVO.setProgrIter(movimento.getProgrIter());
				sscdVO.setCodAttivita(movimento.getCodAttivita());
				sscdVO.setCodBibUtente(movimento.getCodBibUte());
				sscdVO.setProgrUtente(movimento.getCodUte());
				sscdVO.setNominativo(movimento.getNominativo());
				sscdVO.setCodProfessione(movimento.getProfessione());
				sscdVO.setCodTitoloStudio(movimento.getTitoloStudio());
				sscdVO.setCodBibInventario(movimento.getCodBibInv());
				sscdVO.setCodSerie(movimento.getCodSerieInv());
				sscdVO.setCodInventario(movimento.getCodInvenInv());
				sscdVO.setTipoDocLettore(movimento.getTipoDocLett());
				sscdVO.setCodDocLettore(movimento.getCodDocLet());
				sscdVO.setBid(movimento.getBid());
				sscdVO.setNumFascicolo(movimento.getNumFascicolo());
				sscdVO.setNumVolume(movimento.getNumVolume());
				sscdVO.setSegnatura(movimento.getSegnatura());
				sscdVO.setTitolo(movimento.getTitolo());

				output.add(sscdVO);

				// almaviva5_20161221 #6322
				dao.evict(req);
			}
			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public SubReportVO getStampaListeTematicheStorico(String ticket, StampaServiziVO sscVO)
			throws ResourceNotFoundException, ApplicationException, RemoteException {

		try {
			checkTicket(ticket);
			RichiesteServizioDAO dao = new RichiesteServizioDAO();

			Tbf_biblioteca_in_polo biblioteca = null;
			String descrBib = null;

			MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) sscVO.getRichiesta();

			Tbf_biblioteca_in_poloDao daoBib = new Tbf_biblioteca_in_poloDao();
			biblioteca = daoBib.select(movRicerca.getCodPolo(), movRicerca.getCodBibOperante());
			if (biblioteca != null) {
				descrBib = biblioteca.getDs_biblioteca();
			} else {
				descrBib = "Descrizione biblioteca mancante";
			}

			List<Object[]> richieste = dao.getIdsRichiesteFiltriTematiciStorico(movRicerca.getCodPolo(),
					movRicerca.getCodBibOperante(), movRicerca.getDataInizioEff(), movRicerca.getDataFineEff(),
					movRicerca.getCodUte(), null);

			if (!ValidazioneDati.isFilled(richieste))
				return null;

			String idBatch = sscVO.getIdBatch();
			BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch(sscVO);
			SubReportVO output = bcs.startReport("STORICO");
			int progr = 0;
			for (Object[] row : richieste) {
				// almaviva5_20130515 #5312
				if ((progr % 50) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(idBatch);

				Tbl_storico_richieste_servizio req = dao.getStoricoRichiestaById((String) row[0], (String) row[1],
						((BigDecimal) row[2]).longValue());
				if (req == null)
					continue;

				MovimentoPerStampaServCorrVO mov = ServiziConversioneVO.daHibernateAWebMovimentoPerStampa(req, null,
						ticket);
				StampaServiziStoricizzatiVO sscdVO = new StampaServiziStoricizzatiVO(
						FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

				// dati comuni a correnti e storicizzati
				sscdVO.setCodBib(movRicerca.getCodBibOperante());//
				// almaviva5_20170824 #6472
				sscdVO.setDescrBib(StringUtils.normalizeSpace(descrBib));//
				sscdVO.setDataDiElaborazione(DateUtil.formattaData(DaoManager.now().getTime()));
				sscdVO.setCodPolo(movRicerca.getCodPolo());

				sscdVO.setDataIns(mov.getTsIns().toString());
				sscdVO.setDataAgg(mov.getTsVarString());
				sscdVO.setDataInsDate(mov.getTsIns());
				sscdVO.setDataAggDate(mov.getTsVar());
				sscdVO.setProgrRichiesta(mov.getCodRichServ());

				sscdVO.setTipoSrevizio(StringUtils.normalizeSpace(mov.getCodTipoServ()));
				sscdVO.setDataInizioMov(mov.getDataInizioEffString());
				sscdVO.setDataFineMov(mov.getDataFineEffString());
				sscdVO.setDataInizioMovDate(mov.getDataInizioEff());
				sscdVO.setDataFineMovDate(mov.getDataFineEff());
				sscdVO.setNumRinnovi(String.valueOf(mov.getNumRinnovi()));
				sscdVO.setNoteBilioteca(StringUtils.normalizeSpace(mov.getNoteBibliotecario()));
				if (mov.getCostoServizio() != null && !mov.getCostoServizio().trim().equals("")) {
					sscdVO.setCostoServizio(mov.getCostoServizioDouble(), NUMBER_FORMAT_PREZZI, mov.getLocale());
				} else {
					sscdVO.setCostoServizio(0f, NUMBER_FORMAT_PREZZI, mov.getLocale());
				}
				sscdVO.setStatoMovimento(mov.getCodStatoMov());
				sscdVO.setCodBibUtente(mov.getCodBibUte());
				sscdVO.setProgrUtente(mov.getCodUte());
				sscdVO.setNominativo(StringUtils.normalizeSpace(mov.getNominativo()));
				// sscdVO.setCodProfessione(mov.getProfessione());
				// sscdVO.setCodTitoloStudio(mov.getTitoloStudio());
				sscdVO.setCodBibInventario(mov.getCodBibInv());
				sscdVO.setCodSerie(mov.getCodSerieInv());
				sscdVO.setCodInventario(mov.getCodInvenInv());
				sscdVO.setTipoDocLettore(mov.getTipoDocLett());
				sscdVO.setCodDocLettore(mov.getCodDocLet());
				sscdVO.setBid(mov.getBid());
				sscdVO.setNumFascicolo(StringUtils.normalizeSpace(mov.getNumFascicolo()));
				sscdVO.setNumVolume(mov.getNumVolume());
				sscdVO.setSegnatura(StringUtils.normalizeSpace(mov.getSegnatura()));
				sscdVO.setTitolo(StringUtils.normalizeSpace(mov.getTitolo()));
				sscdVO.setPrenotazione(mov.getPrenotazione());
				// solo storicizzati
				if (mov.getCodTipoServ() != null && !mov.getCodTipoServ().trim().equals("")) {
					TB_CODICI tb_codici = CodiciProvider.cercaCodice(String.valueOf(mov.getCodTipoServ().trim()),
							CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null) {
						sscdVO.setDescrTipoServ(StringUtils.normalizeSpace(tb_codici.getDs_tabella()));
					}
				} else {
					sscdVO.setDescrTipoServ("");
				}
				sscdVO.setDataRichiesta(mov.getDataRichiestaString());// trattamento data
				sscdVO.setNoteUtente(StringUtils.normalizeSpace(mov.getNoteUtente()));
				sscdVO.setPrezzoMassimo(mov.getPrezzoMax());// trattamento double
				sscdVO.setDataMassima(mov.getDataMassima());
				sscdVO.setDataProroga(mov.getDataProrogaString());
				sscdVO.setDataInizioPrev(mov.getDataInizioPrevString());
				sscdVO.setDataFinePrev(mov.getDataFinePrevString());
				// almaviva5_20120906 #4829
				sscdVO.setLocaleIll(mov.getFlSvolg());
				sscdVO.setCopyright(mov.getCopyright());

				if (mov.getModErog() != null && !mov.getModErog().trim().equals("")) {
					sscdVO.setModErog(StringUtils.normalizeSpace(mov.getModErog()));
				} else {
					sscdVO.setModErog("");
				}
				if (mov.getSupporto() != null && !mov.getSupporto().trim().equals("")) {
					sscdVO.setSupporto(StringUtils.normalizeSpace(mov.getSupporto()));
				} else {
					sscdVO.setSupporto("");
				}
				if (mov.getRisposta() != null && !mov.getRisposta().trim().equals("")) {
					sscdVO.setRisposta(StringUtils.normalizeSpace(mov.getRisposta()));
				} else {
					sscdVO.setRisposta("");
				}
				if (mov.getModPagamento() != null && !mov.getModPagamento().trim().equals("")) {
					sscdVO.setModPagamento(StringUtils.normalizeSpace(mov.getModPagamento()));
				} else {
					sscdVO.setModPagamento("");
				}
				sscdVO.setServIllAltern(mov.getServIllAltern());
				if (mov.getServIllAltern() != null && !mov.getServIllAltern().trim().equals("")) {
					TB_CODICI tb_codici = CodiciProvider.cercaCodice(String.valueOf(mov.getServIllAltern().trim()),
							CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null) {
						sscdVO.setCodErogAltern(StringUtils.normalizeSpace(tb_codici.getDs_tabella()));
					}
				} else {
					sscdVO.setCodErogAltern("");
				}
				// sscdVO.setCodErogAltern(movimento.getServIllAltern());
				sscdVO.setCodBiblDest(mov.getCodBibDest());
				sscdVO.setNoteBibliotecario(StringUtils.normalizeSpace(mov.getNoteBibliotecario()));
				sscdVO.setNumVolumi(mov.getNumVolumi());
				sscdVO.setNumPezzi(mov.getNumPezzi());
				sscdVO.setNoteBibl(StringUtils.normalizeSpace(mov.getNoteBibl()));
				sscdVO.setAutore(StringUtils.normalizeSpace(mov.getAutore()));
				sscdVO.setEditore(StringUtils.normalizeSpace(mov.getEditore()));
				sscdVO.setAnnoEdizione(StringUtils.normalizeSpace(mov.getAnnoEdizione()));
				sscdVO.setLuogoEdizione(StringUtils.normalizeSpace(mov.getLuogoEdizione()));
				sscdVO.setNumSolleciti(mov.getNumSolleciti());
				if (mov.getDataUltSoll() != null)
					sscdVO.setDataUltSoll(mov.getDataUltSoll().toString());
				sscdVO.setNumVolumeMon(mov.getNumVolumeMon());
				sscdVO.setDescrStatoMov(StringUtils.normalizeSpace(mov.getDescrStatoMov()));
				sscdVO.setDataRichiestaDate(mov.getDataRichiesta());
				sscdVO.setDataMassimaDate(mov.getDataMax());
				sscdVO.setDataProrogaDate(mov.getDataProroga());
				sscdVO.setDataInizioPrevDate(mov.getDataInizioPrev());
				sscdVO.setDataFinePrevDate(mov.getDataFinePrev());
				if (mov.getDataUltSoll() != null)
					sscdVO.setDataUltSollDate(mov.getDataUltSoll());

				sscdVO.setInventario(movRicerca.getCodBibInv() + " " + movRicerca.getCodSerieInv() + " "
						+ movRicerca.getCodInvenInv());
				sscdVO.setCodBibCollocazione(movRicerca.getCodBibDocLett() + " " + sscVO.getCodBibCollocazione());
				sscdVO.setCollocazione(StringUtils.normalizeSpace(sscVO.getCollocazione()));
				sscdVO.setDataDa(sscVO.getDataDa());
				sscdVO.setDataA(sscVO.getDataA());
				sscdVO.setData(DateUtil.getDate() + DateUtil.getTime());

				bcs.writeVO("STORICO", sscdVO);
				dao.getCurrentSession().clear();
			}
			bcs.endReport("STORICO");
			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
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
	 *
	 */
	// public int getAutomaticoX(String ticket, String codAutomaticoX, String
	// codAutorizzazione)
	public int getAutomaticoX(String ticket, String codicePolo, String codiceBib, String codAutorizzazione,
			char codiceAutomaticoX) throws ResourceNotFoundException, ApplicationException {
		int numeroAut = 0;
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		boolean esisteAutomaticoX = false;

		try {
			checkTicket(ticket);
			esisteAutomaticoX = autDAO.esisteAutomaticoX(codicePolo, codiceBib, codAutorizzazione, codiceAutomaticoX);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

		if (esisteAutomaticoX) {
			numeroAut = 1;
		}

		return numeroAut;
		// return dao.getAutomaticoX(codAutomaticoX, codAutorizzazione);
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
	 *
	 */
	public AutorizzazioniVO getAutorizzazioneByProfessione(String ticket, String codicePolo, String codiceBib,
			int idOcc) throws ResourceNotFoundException, ApplicationException {
		int numeroAut = 0;
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
		AutorizzazioneVO autorizzazioneVO = null;
		Tbl_tipi_autorizzazioni autorizzazione = null;
		AutorizzazioniVO autorizzazioniVO = null;

		try {
			checkTicket(ticket);
			autorizzazione = autDAO.getAutorizzazioneByProfessione(codicePolo, codiceBib, idOcc);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

		if (autorizzazione != null) {

			autorizzazioneVO = ServiziConversioneVO.daHibernateAWebTipoAutorizzazione(autorizzazione, 0);
			AutorizzazioniVO webVO = new AutorizzazioniVO();
			webVO.setAutorizzazione(autorizzazioneVO.getDesAutorizzazione());
			webVO.setCodTipoAutor(autorizzazioneVO.getCodAutorizzazione());
			webVO.setCodPoloAutor(autorizzazioneVO.getCodPolo());
			webVO.setCodBibAutor(autorizzazioneVO.getCodBiblioteca());
			webVO.setFlCanc(autorizzazioneVO.getFlCanc());
			webVO.setUteIns(autorizzazioneVO.getUteIns());
			webVO.setUteVar(autorizzazioneVO.getUteVar());
			webVO.setTsIns(autorizzazioneVO.getTsIns());
			webVO.setTsVar(autorizzazioneVO.getTsVar());
			ServizioVO serVO = new ServizioVO();
			List<ServizioVO> servizi = new ArrayList<ServizioVO>();
			for (int index = 0; index < autorizzazioneVO.getListaServizi().size(); index++) {
				serVO = ServiziConversioneVO.daWebVOAWebDirittoUtente(
						autorizzazioneVO.getListaServizi().get(index));
				servizi.add(serVO);
			}
			if (servizi != null && servizi.size() > 0) {
				webVO.setServizi(servizi);
			}
			autorizzazioniVO = webVO;
		}
		return autorizzazioniVO;
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
	 *
	 */
	public void cancellaOccupazioni(String ticket, Integer[] id, String uteVar) throws EJBException {
		OccupazioniDAO dao = new OccupazioniDAO();
		try {
			checkTicket(ticket);
			dao.cancellaOccupazioni(id, uteVar);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
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
	 *
	 */
	public List getListaOccupazioni(String ticket, RicercaOccupazioneVO ricerca)
			throws ResourceNotFoundException, ApplicationException {

		OccupazioniDAO occupazioniDAO = new OccupazioniDAO();
		TB_CODICI tb_codici = null;
		String ordinamento = null;

		try {
			checkTicket(ticket);
			Tbl_occupazioni occupazione = ServiziConversioneVO.daWebAHibernateOccupazioneRicerca(ricerca);
			if (ValidazioneDati.isFilled(ricerca.getOrdinamento())) {
				if (ricerca.getOrdinamento().equals("DETTUTEANA")) {
					ordinamento = "professione:A";
				} else {
					tb_codici = CodiciProvider.cercaCodice(ricerca.getOrdinamento(),
							CodiciType.CODICE_ORDINAMENTO_LISTA_OCCUPAZIONI, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null) {
						ordinamento = tb_codici.getDs_cdsbn_ulteriore();
					}
				}
			}
			List occupazioni = occupazioniDAO.getListaOccupazioni(occupazione, ordinamento);

			List listaOutput = new ArrayList();
			for (int o = 0; o < occupazioni.size(); o++) {
				listaOutput.add(
						ServiziConversioneVO.daHibernateAWebOccupazione((Tbl_occupazioni) occupazioni.get(o), o + 1));
			}

			for (int o = 0; o < listaOutput.size(); o++) {
				OccupazioneVO occup = (OccupazioneVO) listaOutput.get(o);
				String professione = CodiciProvider.cercaDescrizioneCodice(occup.getProfessione(),
						CodiciType.CODICE_PROFESSIONI, CodiciRicercaType.RICERCA_CODICE_SBN);
				occup.setComboDescrizione(professione + " - " + occup.getDesOccupazione());
			}

			return listaOutput;
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
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
	 *
	 */
	public void aggiornaOccupazione(String ticket, OccupazioneVO occupazioneVO, boolean nuovo)
			throws EJBException, AlreadyExistsException {

		OccupazioniDAO occupazioniDAO = new OccupazioniDAO();

		Tbl_occupazioni occupazione = null;
		try {
			checkTicket(ticket);
			if (nuovo) {
				/*
				 * occupazione = occupazioniDAO.getOccupazione(occupazioneVO .getCodPolo(),
				 * occupazioneVO.getCodBiblioteca(), occupazioneVO.getProfessione(),
				 * occupazioneVO .getCodOccupazione());
				 */
				occupazione = occupazioniDAO.getOccupazioneDescr(occupazioneVO.getCodPolo(),
						occupazioneVO.getCodBiblioteca(), occupazioneVO.getProfessione(),
						occupazioneVO.getCodOccupazione(), occupazioneVO.getDesOccupazione());

				if (occupazione != null && occupazione.getFl_canc() == 'N')
					if (nuovo) {
						throw new AlreadyExistsException();
					} else if (occupazioneVO.getIdOccupazioni() != occupazione.getId_occupazioni()) {
						// siamo in modifica con descrizione già esistente
						throw new AlreadyExistsException();
					}

			}
			if (occupazione == null) {
				occupazione = ServiziConversioneVO.daWebAHibernateOccupazione(occupazioneVO, nuovo);
			} else {
				occupazione.setDescr(occupazioneVO.getDesOccupazione());
				occupazione.setFl_canc('N');
				occupazione.setUte_var(occupazioneVO.getUteVar());
			}
			occupazioniDAO.aggiornaOccupazione(occupazione);

			if (nuovo)
				occupazioneVO.setIdOccupazioni(occupazione.getId_occupazioni());
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public void aggiornaMateria(String ticket, MateriaVO materiaVO, boolean nuovo)
			throws EJBException, AlreadyExistsException {
		MaterieDAO materieDao = new MaterieDAO();

		try {
			checkTicket(ticket);
			Tbl_materie materia = null;
			if (nuovo) {
				// controllo se esiste una materia
				materia = materieDao.getMateriaBiblioteca(materiaVO.getCodPolo(), materiaVO.getCodBib(),
						materiaVO.getCodice());
				if (materia != null && materia.getFl_canc() == 'N') {
					// Esiste già una materia con lo stesso codice
					throw new AlreadyExistsException();
				}
			}
			if (materia == null) {
				materia = ServiziConversioneVO.daWebAHibernateMateria(materiaVO, nuovo);
			} else {
				materia.setDescr(materiaVO.getDescrizione());
				materia.setFl_canc('N');
				// materia.setUte_ins(materiaVO.getUteIns());
				materia.setUte_var(materiaVO.getUteVar());
				// materia.setTs_ins(materiaVO.getTsIns());
			}
			materieDao.aggiornaMateria(materia);

			if (nuovo)
				materiaVO.setIdMateria(materia.getId_materia());
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public void cancellaMaterie(String ticket, Integer[] idMaterie, String uteVar)
			throws ApplicationException, EJBException {
		MaterieDAO dao = new MaterieDAO();
		try {
			checkTicket(ticket);
			// almaviva5_20120506 controllo legami utente-materia
			if (ValidazioneDati.isFilled(idMaterie))
				for (int id : idMaterie) {
					if (dao.countUtentiLegatiMateria(id) > 0) {
						Tbl_materie mat = dao.getMateriaBibliotecaById(id);
						if (mat == null)
							throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

						throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_MATERIA_LEGATA_UTENTE,
								ValidazioneDati.trimOrEmpty(mat.getDescr()));
					}
				}

			dao.cancellaMaterie(idMaterie, uteVar);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
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
	 *
	 */
	public List getListaMaterie(String ticket, RicercaMateriaVO matWeb) throws EJBException {
		MaterieDAO dao = new MaterieDAO();

		try {
			checkTicket(ticket);
			Tbl_materie materia = ServiziConversioneVO.daWebAHibernateMaterieRicerca(matWeb);
			TB_CODICI tb_codici = null;
			String ordinamento = "";
			// estraggo l'ordinamento dal codice ordinamento impostato
			if (ValidazioneDati.isFilled(matWeb.getOrdinamento())) {
				tb_codici = CodiciProvider.cercaCodice(matWeb.getOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_MATERIE, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (tb_codici != null)
					ordinamento = tb_codici.getDs_cdsbn_ulteriore();
			}
			List<Tbl_materie> ricercaMaterie = dao.ricercaMaterie(materia, ordinamento);
			List output = new ArrayList();
			for (int i = 0; i < ricercaMaterie.size(); i++) {
				output.add(ServiziConversioneVO.daHibernateAWebMateria(ricercaMaterie.get(i), i + 1));
			}
			return output;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public void aggiornaSpecTitoloStudio(String ticket, SpecTitoloStudioVO specTitoloVO, boolean nuovo)
			throws EJBException, AlreadyExistsException {

		TitoliDiStudioDAO titoliStudioDAO = new TitoliDiStudioDAO();

		Tbl_specificita_titoli_studio specTitoloStudio = null;
		try {
			checkTicket(ticket);
			// if (nuovo) {
			/*
			 * specTitoloStudio = titoliStudioDAO.getSpecificitaTitoloStudio(
			 * specTitoloVO.getCodPolo(), specTitoloVO .getCodBiblioteca(), specTitoloVO
			 * .getTitoloStudio(), specTitoloVO .getCodSpecialita());
			 */
			specTitoloStudio = titoliStudioDAO.getSpecificitaTitoloStudioDescr(specTitoloVO.getCodPolo(),
					specTitoloVO.getCodBiblioteca(), specTitoloVO.getTitoloStudio(), specTitoloVO.getCodSpecialita(),
					specTitoloVO.getDesSpecialita());
			if (specTitoloStudio != null && specTitoloStudio.getFl_canc() == 'N') {
				if (nuovo) {
					throw new AlreadyExistsException();
				} else if (specTitoloVO.getIdTitoloStudio() != specTitoloStudio.getId_specificita_titoli_studio()) {
					// siamo in modifica con descrizione già esistente
					throw new AlreadyExistsException();
				}
			}
			// }
			if (specTitoloStudio == null) {
				specTitoloStudio = ServiziConversioneVO.daWebAHibernateSpecTitoloStudio(specTitoloVO, nuovo);
			} else {
				specTitoloStudio.setDescr(specTitoloVO.getDesSpecialita());
				specTitoloStudio.setUte_var(specTitoloVO.getUteVar());
				specTitoloStudio.setFl_canc('N');
			}

			titoliStudioDAO.aggiorna(specTitoloStudio);

			if (nuovo)
				specTitoloVO.setIdTitoloStudio(specTitoloStudio.getId_specificita_titoli_studio());
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public void cancellaSpecTitoloStudio(String ticket, Integer[] id, String uteVar) throws EJBException {
		TitoliDiStudioDAO dao = new TitoliDiStudioDAO();
		try {
			checkTicket(ticket);
			dao.cancellaSpecTitoloStudio(id, uteVar);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
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
	 *
	 */
	public List getListaTitoloStudio(String ticket, RicercaTitoloStudioVO ricTDS)
			throws ResourceNotFoundException, ApplicationException, Exception {
		UtilitaDAO hibernateDAO = new UtilitaDAO();
		TitoliDiStudioDAO titoliDiStudioDAO = new TitoliDiStudioDAO();

		TB_CODICI tb_codici = null;
		String ordinamento = "";

		try {
			checkTicket(ticket);

			if (ValidazioneDati.isFilled(ricTDS.getOrdinamento())) {
				if (ricTDS.getOrdinamento().equals("DETTUTEANA")) {
					ordinamento = "tit_studio:A";
				} else {
					tb_codici = CodiciProvider.cercaCodice(ricTDS.getOrdinamento(),
							CodiciType.CODICE_ORDINAMENTO_LISTA_TITOLI_STUDIO, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null) {
						ordinamento = tb_codici.getDs_cdsbn_ulteriore();
					}
				}
			}

			Tbl_specificita_titoli_studio titoloStudio = ServiziConversioneVO
					.daWebAHibernateTitoloStudioRicerca(ricTDS);
			List titoliStudio = titoliDiStudioDAO.getListaTitoliStudio(titoloStudio, ordinamento);

			List listaOutput = new ArrayList();
			for (int t = 0; t < titoliStudio.size(); t++) {
				listaOutput.add(ServiziConversioneVO
						.daHibernateAWebTitoloStudio((Tbl_specificita_titoli_studio) titoliStudio.get(t), t + 1));
			}

			for (int o = 0; o < listaOutput.size(); o++) {
				SpecTitoloStudioVO titStudio = (SpecTitoloStudioVO) listaOutput.get(o);
				String titoloDesc = CodiciProvider.cercaDescrizioneCodice(titStudio.getTitoloStudio(),
						CodiciType.CODICE_TITOLO_STUDIO, CodiciRicercaType.RICERCA_CODICE_SBN);
				titStudio.setComboDescrizione(titoloDesc + " - " + titStudio.getDesSpecialita());
			}

			return listaOutput;
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
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
	 *
	 *
	 */
	public List getListaUtenti(String ticket, RicercaUtenteBibliotecaVO ricerca)
			throws ResourceNotFoundException, ApplicationException, ValidationException {
		checkTicket(ticket);
		List<SinteticaUtenteVO> utenti = getListaUtenti(ricerca);
		// almaviva5_20110512 #4446
		if (ricerca.isRicercaUtentePolo())
			utenti = intersectListaUtenti(ricerca.getCodBibSer(), utenti, ricerca.getIdTrovati());

		return utenti;
	}

	private List<SinteticaUtenteVO> intersectListaUtenti(String codBib, List<SinteticaUtenteVO> listaUtenti,
			Set<String> idTrovati) {

		// mappa ordinata
		Map<String, SinteticaUtenteVO> listaScremata = new LinkedHashMap<String, SinteticaUtenteVO>();
		// filtro su biblioteche diverse da quella considerata
		for (Object o : listaUtenti) {
			SinteticaUtenteVO utentePolo = (SinteticaUtenteVO) o;
			if (!idTrovati.contains(utentePolo.getCodice()))
				if (ValidazioneDati.equals(utentePolo.getFlCanc(), "S")) // cancellato in biblio
					listaScremata.put(utentePolo.getCodice(), utentePolo);
				else if (!ValidazioneDati.equals(utentePolo.getBibErogante(), codBib))
					listaScremata.put(utentePolo.getCodice(), utentePolo);

		}

		int prg = 0;
		Collection<SinteticaUtenteVO> values = listaScremata.values();
		for (SinteticaUtenteVO u : values)
			u.setProgressivo(++prg);

		return new ArrayList<SinteticaUtenteVO>(values);
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
	 *
	 */
	public List getListaUtenti(RicercaUtenteBibliotecaVO ricerca)
			throws ResourceNotFoundException, ApplicationException, ValidationException {
		UtentiDAO dao = new UtentiDAO();
		MaterieDAO materieDao = new MaterieDAO();

		try {
			ricerca.validate(new RicercaUtenteBibliotecaValidator());

			Trl_utenti_biblioteca uteBib = ServiziConversioneVO.daWebAHibernateUteRicerca(ricerca);
			List<Integer> listaUtenti = null;
			String ordinamento = "";

			if (ValidazioneDati.isFilled(ricerca.getOrdinamento())) {
				TB_CODICI cod = CodiciProvider.cercaCodice(ricerca.getOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_UTENTI, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					ordinamento = cod.getDs_cdsbn_ulteriore();
			}

			if (ricerca.isRicercaUtentePolo()) {

				if (ricerca.getIdUteUte() > 0
						|| (ricerca.getCodUte() != null && ricerca.getCodUte().trim().length() > 0)
						|| (ricerca.getCodFiscale() != null && ricerca.getCodFiscale().trim().length() > 0)
						|| (ricerca.getEmail() != null && ricerca.getEmail().trim().length() > 0)
						|| ((ricerca.getCodiceAteneo() != null && ricerca.getCodiceAteneo().trim().length() > 0)
								&& (ricerca.getMatricola() != null && ricerca.getMatricola().trim().length() > 0))) {
					// ricerca dell'esistenza della sola anagrafica
					String cdUte = "";
					String cdFisc = "";
					String cdMail = "";
					String cdAte = "";
					String cdMat = "";

					if (ricerca.getCodUte() != null && ricerca.getCodUte().trim().length() > 0) {
						cdUte = ricerca.getCodUte();
					}
					if (ricerca.getCodFiscale() != null && ricerca.getCodFiscale().trim().length() > 0) {
						cdFisc = ricerca.getCodFiscale();
					}
					if (ricerca.getEmail() != null && ricerca.getEmail().trim().length() > 0) {
						cdMail = ricerca.getEmail();
					}
					if (ricerca.getCodiceAteneo() != null && ricerca.getCodiceAteneo().trim().length() > 0) {
						cdAte = ricerca.getCodiceAteneo();
					}
					if (ricerca.getMatricola() != null && ricerca.getMatricola().trim().length() > 0) {
						cdMat = ricerca.getMatricola();
					}

					List<Tbl_utenti> utenti = dao.ricercaUtenteBasePolo(ricerca.getIdUteUte(), cdUte, cdFisc,
							ordinamento, cdMail, cdAte.trim(), cdMat.trim());
					if (ValidazioneDati.isFilled(utenti)) {
						listaUtenti = new ArrayList<Integer>();
						for (Tbl_utenti u : utenti) {
							Trl_utenti_biblioteca utente_bib = (Trl_utenti_biblioteca) ValidazioneDati
									.first(u.getTrl_utenti_biblioteca());
							listaUtenti.add(utente_bib.getId_utenti_biblioteca());
						}
					}
				}

				if (!ValidazioneDati.isFilled(listaUtenti)) {
					if (ValidazioneDati.isFilled(ricerca.getChiave_ute())) {
						if (ricerca.getTipoRicerca() != null && ricerca.getTipoRicerca()
								.equalsIgnoreCase(RicercaUtenteBibliotecaVO.RICERCA_PAROLE)) {
							listaUtenti = dao.ricercaUtentiParole(uteBib, ricerca.getChiave_ute(), ordinamento,
									ricerca);
						} else if (ricerca.getTipoRicerca() != null
								&& ValidazioneDati.equals(ricerca.getTipoRicerca(), "ini")) {
							listaUtenti = dao.ricercaUtentiLike(uteBib, ordinamento, ricerca);
						} else {// "int"
							listaUtenti = dao.ricercaUtentiExact(uteBib, ordinamento, ricerca);
						}

					} else {
						if (!ValidazioneDati.isFilled(listaUtenti))
							listaUtenti = dao.ricercaUtentiPolo(uteBib, ordinamento);

						if (!ValidazioneDati.isFilled(listaUtenti)) {
							if (ValidazioneDati.isFilled(ricerca.getChiave_ute()) && uteBib.getId_utenti() != null) {
								List<Tbl_utenti> ricercaUtentiAna = dao.ricercaUtentiPoloChiaveUte(
										uteBib.getId_utenti(), ricerca.getTipoRicerca(), ricerca.isRicercaUtentePolo());
								listaUtenti = new ArrayList<Integer>();
								if (ValidazioneDati.isFilled(ricercaUtentiAna)) {
									for (Tbl_utenti utente : ricercaUtentiAna) {
										Trl_utenti_biblioteca utente_bib = (Trl_utenti_biblioteca) ValidazioneDati
												.first(utente.getTrl_utenti_biblioteca());
										listaUtenti.add(utente_bib.getId_utenti_biblioteca());
									}
								}
							}
						}
					}
				}
			} else if (ValidazioneDati.equals(ricerca.getTipoRicerca(), "ini")) {
				listaUtenti = dao.ricercaUtentiLike(uteBib, ordinamento, ricerca);
			} else if (ValidazioneDati.equals(ricerca.getTipoRicerca(), "par")) {
				listaUtenti = dao.ricercaUtentiParole(uteBib, ricerca.getChiave_ute(), ordinamento,
						ricerca);
			} else // "int"
				listaUtenti = dao.ricercaUtentiExact(uteBib, ordinamento, ricerca);

			// lista id utente ignorati
			List<Integer> idUtentiIgnorati = ricerca.getIdUtentiIgnorati();
			boolean withIdUtentiIgnorati = ValidazioneDati.isFilled(idUtentiIgnorati);

			Session session = dao.getCurrentSession();
			List output = new ArrayList(listaUtenti.size());
			int prg = 0;
			for (int id : listaUtenti) {
				Trl_utenti_biblioteca utente = dao.getUtenteBibliotecaById(id);
				SinteticaUtenteVO rec = ServiziConversioneVO.daHibernateAWebUteRicerca(utente);
				Tbl_utenti id_utenti = utente.getId_utenti();
				session.evict(id_utenti);
				session.evict(utente);

				if (!withIdUtentiIgnorati || !idUtentiIgnorati.contains(id_utenti.getId_utenti())) {
					rec.setProgressivo(++prg);
					output.add(rec);
				}
			}

			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public List getListaAnagServiziUte(String ticket, UtenteBibliotecaVO utente)
			throws ResourceNotFoundException, ApplicationException {
		UtilitaDAO hdao = new UtilitaDAO();
		ServiziDAO serviziDAO = new ServiziDAO();

		List serviziBib;
		try {
			checkTicket(ticket);
			List listaTipiServizi = new ArrayList();
			List uteServizi = new ArrayList();
			uteServizi = utente.getAutorizzazioni().getListaServizi();
			int index = 0;
			while (index < uteServizi.size()) {
				ServizioVO uteSer = (ServizioVO) uteServizi.get(index);
				if (uteSer.getStato() == ServizioVO.MOD || uteSer.getStato() == ServizioVO.NEW
						|| uteSer.getStato() == ServizioVO.OLD)
					listaTipiServizi.add(new String(uteSer.getCodice()));
				index++;
			}
			serviziBib = serviziDAO.getListaServiziBiblioteca(listaTipiServizi, utente.getCodPoloSer(),
					utente.getCodBibSer());
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
		// trasformo lista di tbl_servizio in lista AnagServizioVO
		List listaServizi = new ArrayList();
		for (int i = 0; i < serviziBib.size(); i++) {
			listaServizi.add(ServiziConversioneVO.daHibernateAWebAnagServizio((Tbl_servizio) serviziBib.get(i), i + 1));
		}

		List listaServiziRit = new ArrayList();
		listaServiziRit = preparaListaServizi(listaServizi, utente);
		listaServiziRit = filtraServiziUte(listaServiziRit, utente.getAutorizzazioni().getListaServizi());
		return listaServiziRit;

	}

	private List preparaListaServizi(List elencoAnagServizio, UtenteBibliotecaVO utente) {
		if (elencoAnagServizio == null && elencoAnagServizio.size() == 0)
			return null;
		int index = 0;
		List elencoServizi = new ArrayList();
		while (index < elencoAnagServizio.size()) {
			ElementoSinteticaServizioVO serAna = (ElementoSinteticaServizioVO) elencoAnagServizio.get(index);
			ServizioVO serLst = new ServizioVO();
			serLst.setCodPolo(utente.getCodPoloSer());
			serLst.setCodBib(utente.getCodBibSer());
			serLst.setCodPoloUte(utente.getCodPolo());
			serLst.setCodBibUte(utente.getCodiceBiblioteca());
			serLst.setCodUte(utente.getCodiceUtente());
			if (utente.getIdUtente() != null)
				serLst.setIdUtente(Integer.valueOf(utente.getIdUtente()));
			else
				serLst.setIdUtente(0);
			serLst.setCodice(serAna.getTipServizio());
			serLst.setServizio(serAna.getCodServizio());
			serLst.setDescrizione(serAna.getDesServizio());
			serLst.setStato(ServizioVO.NEW);
			serLst.setCancella("");
			serLst.setProgressivo(serAna.getProgressivo());
			serLst.setIdServizio(serAna.getIdServizio());
			elencoServizi.add(serLst);
			index++;
		}
		return elencoServizi;
	}

	private List filtraServiziUte(List elencoScegliServizio, List serviziUtente) {
		if (serviziUtente == null || serviziUtente.size() == 0)
			return elencoScegliServizio;
		int index = 0;
		while (index < elencoScegliServizio.size()) {
			ServizioVO serBib = (ServizioVO) elencoScegliServizio.get(index);
			for (int index2 = 0; index2 < serviziUtente.size(); index2++) {
				ServizioVO serUte = (ServizioVO) serviziUtente.get(index2);
				// elimino gli uguali a meno che non siano cancellati
				if (serUte.getIdServizio() == serBib.getIdServizio()) {
					if (serUte.getStato() != ServizioVO.DELDELMOD && serUte.getStato() != ServizioVO.DELDELOLD
							&& serUte.getStato() != ServizioVO.DELELI) {
						elencoScegliServizio.remove(serBib);
						index--;
						break;
					}
				}
			}
			index++;
		}
		return elencoScegliServizio;
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
	 *
	 */
	public boolean updateDataRinnovoAut(String ticket, String[] lstCodUte, String dataRinnAut)
			throws DataException, ApplicationException {

		boolean ret = false;

		UtilitaDAO hibernateDAO = new UtilitaDAO();
		UtentiDAO utentiDAO = new UtentiDAO();

		List listaDiritti;
		Trl_diritti_utente diritto;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		try {
			checkTicket(ticket);
			// Scorrro array codici utente
			for (int i = 0; i < lstCodUte.length; i++) {
				// ottengo la lista diritti utente associata all'utente corrente
				listaDiritti = utentiDAO.getListaDirittiUtente(new Integer(lstCodUte[i]).intValue());
				// ciclo sui diritti utente per aggiornarli
				Iterator iterator = listaDiritti.iterator();
				Date dataRinnovo;
				while (iterator.hasNext()) {
					diritto = (Trl_diritti_utente) iterator.next();
					dataRinnovo = format.parse(dataRinnAut);
					diritto.setData_fine_serv(dataRinnovo);
					utentiDAO.aggiornaDirittoUtente(diritto);
				}
			}
			ret = true;
		} catch (ParseException e) {
			log.error("", e);
			throw new ApplicationException(e);
		} catch (NumberFormatException e) {
			log.error("", e);
			throw new ApplicationException(e);
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

		return ret;
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
	 *
	 */
	public UtenteBibliotecaVO getDettaglioUtente(String ticket, RicercaUtenteBibliotecaVO ricerca, String numberFormat,
			Locale locale) throws ResourceNotFoundException, ApplicationException {
		String locNumberFormat = null;
		if (numberFormat == null)
			locNumberFormat = ServiziConstant.NUMBER_FORMAT_CONVERTER;
		else
			locNumberFormat = numberFormat;

		MaterieDAO materieDAO = new MaterieDAO();
		UtentiDAO utentiDAO = new UtentiDAO();

		try {
			checkTicket(ticket);
			Trl_utenti_biblioteca utenteBib;
			if (ricerca.getIdUte() > 0)
				utenteBib = utentiDAO.getUtenteBibliotecaById(ricerca.getIdUte());
			else {
				// almaviva5_20170329 aggiunto filtro cod.fiscale
				utenteBib = utentiDAO.getUtenteBiblioteca(ricerca.getCodPolo(), ricerca.getCodBibUte(),
						ricerca.getCodUte(), ricerca.getCodBib());
				if (utenteBib == null) {
					String codFiscale = ricerca.getCodUte().toUpperCase();
					utenteBib = utentiDAO.getUtenteBibByCodFiscale(ricerca.getCodPolo(), ricerca.getCodBibUte(),
							codFiscale, ricerca.getCodBib());
				}
			}

			if (utenteBib == null)
				return null;

			UtenteBibliotecaVO utenteBibliotecaVO = ServiziConversioneVO.daHibernateAWebUteDettaglio(utenteBib,
					utentiDAO.getListaDirittiUtente(utenteBib.getId_utenti_biblioteca()),
					materieDAO.getListaMaterieUtenteBiblioteca(utenteBib), locNumberFormat, locale);
			return utenteBibliotecaVO;
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public UtenteBibliotecaVO getDettaglioUtenteBase(String ticket, RicercaUtenteBibliotecaVO ricerca,
			String numberFormat, Locale locale) throws ResourceNotFoundException, ApplicationException {
		String locNumberFormat = null;
		if (numberFormat == null)
			locNumberFormat = ServiziConstant.NUMBER_FORMAT_CONVERTER;
		else
			locNumberFormat = numberFormat;

		// UtentiDAO utenteDAO = new UtentiDAO();
		MaterieDAO materieDAO = new MaterieDAO();
		UtentiDAO dao = new UtentiDAO();

		try {
			checkTicket(ticket);
			Tbl_utenti utenteBase = ValidazioneDati.first(
					dao.ricercaUtenteBasePolo(ricerca.getIdUteUte(), ricerca.getCodUte(), ricerca.getCodFiscale(), "",
							// ricerca.getEmail(),
							null, ValidazioneDati.trimOrEmpty(ricerca.getCodiceAteneo()),
							ValidazioneDati.trimOrEmpty(ricerca.getMatricola())));

			/*
			 * UtenteBibliotecaVO utenteBibliotecaVO = ServiziConversioneVO
			 * .daHibernateAWebUtenteBase(utenteBase,
			 * utentiDAO.getListaDirittiUtente(utenteBase.getId_utenti()), materieDAO
			 * .getListaMaterieUtenteBiblioteca(utenteBase), locNumberFormat, locale);
			 */

			UtenteBibliotecaVO utenteBibliotecaVO = ServiziConversioneVO.daHibernateAWebUtenteBase(utenteBase, null,
					null, locNumberFormat, locale);

			return utenteBibliotecaVO;

		} catch (Exception e) {
			throw new EJBException(e);
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
	 *
	 */
	public List<SinteticaUtenteVO> verificaEsistenzaUtentePolo(String ticket, UtenteBibliotecaVO uteAna)
			throws DataException, ApplicationException {

		UtentiDAO dao = new UtentiDAO();
		try {
			checkTicket(ticket);
			Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(uteAna, null);
			List<Integer> utentiPolo = dao.ricercaUtentiPolo(utente);
			List<SinteticaUtenteVO> output = new ArrayList<SinteticaUtenteVO>();
			int prg = 0;
			for (Integer id : utentiPolo) {
				Trl_utenti_biblioteca ub = dao.getUtenteBibliotecaById(id);
				SinteticaUtenteVO su = ServiziConversioneVO.daHibernateAWebUteRicerca(ub);
				su.setProgressivo(++prg);
				output.add(su);
			}
			return output;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public List<SinteticaUtenteVO> verificaEsistenzaUtentePolo(String ticket, BibliotecaVO bibVO, String codPolo,
			String codBib, BaseVO vo) throws DataException, ApplicationException {

		DaoManager hDAO = new UtentiDAO();
		try {
			checkTicket(ticket);
			UtenteBibliotecaVO uteVO = ServiziConversioneVO.daBibliotecaVOAUtenteBibliotecaVO(bibVO);
			uteVO.setCodiceBiblioteca(codBib);
			uteVO.setCodPolo(codPolo);
			uteVO.setCodBibSer(codBib);
			uteVO.setCodPoloSer(codPolo);
			uteVO.setTsIns(vo.getTsIns());
			uteVO.setUteIns(vo.getUteIns());
			uteVO.setUteVar(vo.getUteVar());

			return this.verificaEsistenzaUtentePolo(ticket, uteVO);
		} catch (Exception e) {
			throw new ApplicationException(e);
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
	 *
	 */
	public boolean insertUtente(String ticket, UtenteBibliotecaVO recUte) throws DataException, ApplicationException {

		boolean ret = true;

		UtilitaDAO hibernateDAO = new UtilitaDAO();
		OccupazioniDAO occupazioniDAO = new OccupazioniDAO();
		TitoliDiStudioDAO titoliDiStudioDAO = new TitoliDiStudioDAO();

		try {
			checkTicket(ticket);
			Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(recUte, null);

			UtentiDAO utentiDao = new UtentiDAO();
			String codBib = utente.getCd_bib().getCd_biblioteca();

			//almaviva5_20191031 #7173 check codice isil
			checkCodiceAnagrafe(recUte);

			// imposto la password di default
			// almaviva5_20110228 #4207 per gli enti la password non é significativa
			String cod_fiscale = ValidazioneDati.trimOrEmpty(utente.getCod_fiscale());
			if (ValidazioneDati.isFilled(cod_fiscale)) {
				PasswordEncrypter crypt = new PasswordEncrypter(cod_fiscale);
				String encryptPwd = crypt.encrypt(cod_fiscale);
				utente.setPassword(encryptPwd);
				utente.setChange_password('S'); // almaviva5_20101203 #4036
			} else { // cod. fiscale vuoto
				utente.setPassword(null);
				utente.setChange_password('N');
			}

			//LFV 18/07/18 modifica per import utenti esse3, codUtente già presente,no reset psw
			if (!ValidazioneDati.isFilled(recUte.getCodiceUtente())) {
				String codUtente = utentiDao.calcolaNextCodUtente(codBib);
				utente.setCod_utente(codUtente);
			} else {
				utente.setPassword(null);
				utente.setChange_password('N');
			}


			Timestamp ts = DaoManager.now();
			utente.setData_reg(ts);
			utente.setLast_access(ts);
			utente.setTs_var_password(ts);

			utentiDao.inserimentoUtente(utente);

			// aggiorno l'istanza di UtenteBibliotecaVO con il codice utente
			// calcolato dalla funzione di inserimento
			recUte.setCodiceUtente(utente.getCod_utente());
			// tck 3880
			recUte.setIdUtente(String.valueOf(utente.getId_utenti()));

			Trl_utenti_biblioteca utenteBib = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(recUte, null);

			Tbl_occupazioni id_occupazioni = utenteBib.getId_occupazioni();
			if (id_occupazioni != null)
				utenteBib.setId_occupazioni(occupazioniDAO.getOccupazioneById(id_occupazioni.getId_occupazioni()));

			Tbl_specificita_titoli_studio id_specificita_titoli_studio = utenteBib.getId_specificita_titoli_studio();
			if (id_specificita_titoli_studio != null)
				utenteBib.setId_specificita_titoli_studio(titoliDiStudioDAO
						.getTitoloStudioById(id_specificita_titoli_studio.getId_specificita_titoli_studio()));
			// almaviva4 intervento date 11.12.09
			if (utenteBib.getCod_tipo_aut() != null && utenteBib.getCod_tipo_aut().trim().length() > 0) {
				if (utenteBib.getData_inizio_aut() == null) {
					utenteBib.setData_inizio_aut(utente.getData_reg());
				}
				if (utenteBib.getData_fine_aut() == null) {
					Date dataFineAut = DateUtil.addYear(utenteBib.getData_inizio_aut(), 1);
					utenteBib.setData_fine_aut(dataFineAut);
				}
			}
			utentiDao.inserimentoUtenteBiblioteca(utenteBib, utente);
			// 3880
			recUte.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));
			this.aggiornaDirittiUtente(utenteBib, recUte);
			this.aggiornaMaterie_Utente(utenteBib, recUte);

		} catch (ApplicationException e) {
			throw e;

		} catch (Exception e) {
			ret = false;
			throw new ApplicationException(e);
		}
		return ret;
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
	 *
	 */
	public boolean importaUtente(String ticket, UtenteBibliotecaVO recUte) throws DataException, ApplicationException {

		boolean ret = true;

		UtentiDAO utentiDao = new UtentiDAO();
		UtilitaDAO hibernateDAO = new UtilitaDAO();
		OccupazioniDAO occupazioniDAO = new OccupazioniDAO();
		TitoliDiStudioDAO titoliDiStudioDAO = new TitoliDiStudioDAO();

		try {
			checkTicket(ticket);
			// aggiorno anagrafica utente
			int idUtente = Integer.valueOf(recUte.getIdUtente());
			Tbl_utenti oldUte = utentiDao.getUtenteAnagraficaById(idUtente);

			Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(recUte, oldUte);
			utentiDao.aggiornaUtente(utente);

			// aggiorno l'istanza di UtenteBibliotecaVO con il codice utente
			// calcolato dalla funzione di inserimento
			// tck 3880
			recUte.setCodiceUtente(utente.getCod_utente());

			// inserisco l'utente nella nuova biblioteca
			boolean found = false;
			Trl_utenti_biblioteca utenteBib = null;
			List<Trl_utenti_biblioteca> utentiCancellati = utentiDao.ricercaLocalizzazioneCancellata(idUtente);
			if (ValidazioneDati.isFilled(utentiCancellati)) {
				for (Trl_utenti_biblioteca ub : utentiCancellati) {
					Tbf_biblioteca_in_polo bib = ub.getCd_biblioteca();
					if (ValidazioneDati.equals(bib.getCd_biblioteca(), recUte.getCodBibSer())) {
						utenteBib = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(recUte, ub);
						// recupero record
						utenteBib.setFl_canc('N');
						utenteBib.setUte_var(hibernateDAO.getFirmaUtente(ticket));
						found = true;
						break;
					}
				}
			}

			// almaviva5_20130110 #5225
			if (!found)
				utenteBib = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(recUte, null);

			// tck 3880
			recUte.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));

			Tbl_occupazioni id_occupazioni = utenteBib.getId_occupazioni();
			if (id_occupazioni != null)
				utenteBib.setId_occupazioni(occupazioniDAO.getOccupazioneById(id_occupazioni.getId_occupazioni()));

			Tbl_specificita_titoli_studio id_specificita_titoli_studio = utenteBib.getId_specificita_titoli_studio();
			if (id_specificita_titoli_studio != null)
				utenteBib.setId_specificita_titoli_studio(titoliDiStudioDAO
						.getTitoloStudioById(id_specificita_titoli_studio.getId_specificita_titoli_studio()));

			if (found)
				utentiDao.aggiornaUtenteBiblioteca(utenteBib);
			else
				utentiDao.inserimentoUtenteBiblioteca(utenteBib, utente);

			this.aggiornaDirittiUtente(utenteBib, recUte);
			this.aggiornaMaterie_Utente(utenteBib, recUte);

		} catch (Exception e) {
			ret = false;
			throw new ApplicationException(e);
		}
		return ret;
	}

	/**
	 * @param codPolo
	 *            codice polo biblioteca operante
	 * @param codBib
	 *            codice biblioteca operante
	 * @param bibVO
	 *            dati biblioteca da importare
	 * @param vo
	 *            dati sull'utente che sta effettuando l'operazione
	 * @param stato
	 *            <ul>
	 *            <li>0: tutto OK</li>
	 *            <li>1: la biblioteca è già stata importata nella tabella
	 *            utenti</li>
	 *            </ul>
	 * @return Istanza di UtenteBibliotecaVO se ok, null altrimenti
	 *
	 * @throws ApplicationException
	 */
	public UtenteBibliotecaVO importaBibliotecaComeUtente(String ticket, String codPolo, String codBib,
			BibliotecaVO bibVO, BaseVO vo, Locale locale) throws ApplicationException {
		UtenteBibliotecaVO utenteVO = null;

		UtentiDAO utentiDao = new UtentiDAO();

		if (locale == null)
			locale = Locale.getDefault();

		try {
			checkTicket(ticket);
			Trl_utenti_biblioteca utente_bib = utentiDao.getBibliotecaImportata(codPolo, codBib, bibVO.getCod_polo(),
					bibVO.getCod_bib());

			UtenteBibliotecaVO uteVO = ServiziConversioneVO.daBibliotecaVOAUtenteBibliotecaVO(bibVO);
			uteVO.setCodiceBiblioteca(codBib);
			uteVO.setCodPolo(codPolo);
			uteVO.setCodBibSer(codBib);
			uteVO.setCodPoloSer(codPolo);
			uteVO.setTsIns(vo.getTsIns());
			uteVO.setUteIns(vo.getUteIns());
			uteVO.setUteVar(vo.getUteVar());

			if (utente_bib == null) {
				// la biblioteca non è mai stata importata in tabella utenti

				// inserimento record in tbl_utenti
				Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(uteVO, null);
				utentiDao.inserimentoUtente(utente);

				// inserimento record in trl_utenti_biblioteca
				Trl_utenti_biblioteca utenteBib = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(uteVO, null);
				utentiDao.inserimentoUtenteBiblioteca(utenteBib, utente);

				utenteVO = uteVO;
				utenteVO.setNuovoUte(true);
				utenteVO.setCodiceUtente(utente.getCod_utente());
				utenteVO.setIdUtente(String.valueOf(utente.getId_utenti()));
				utenteVO.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));

				// aggiorno il codice utente in tabella tbf_biblioteca
				bibVO.setCod_utente(Long.valueOf(utenteVO.getCodiceUtente().substring(2)));
				bibVO.setCod_bib_ut(utenteVO.getCodiceBiblioteca());
				bibVO.setCod_bib_agg(utenteVO.getUteVar());
				bibVO.setTsVar(DaoManager.now());

				getAmministrazioneBib().updateBiblioteca(bibVO);
			} else {
				if (utente_bib.getFl_canc() != 'S') {
					// la biblioteca risulta già importata come utente
					utenteVO = ServiziConversioneVO.daHibernateAWebUteDettaglio(utente_bib, null, null, null, locale);
					utenteVO.setNuovoUte(false);
				} else {
					// la biblioteca è stata già importata ma in seguito
					// cancellata. ripristino la relazione
					utente_bib.setFl_canc('N');
					utente_bib.setUte_var(vo.getUteVar());

					if (utente_bib.getId_utenti().getFl_canc() == 'S') {
						// risulta cancellata anche il record in tbl_utenti. va
						// ripristinato aggiornando i dati

						// aggiornamento record in tbl_utenti
						Tbl_utenti oldUte = utente_bib.getId_utenti();
						Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(uteVO, oldUte);
						utente.setUte_var(vo.getUteVar());
						utentiDao.aggiornaUtente(utente);
					}
					// aggiornamento record in trl_utenti_biblioteca
					Trl_utenti_biblioteca utenteBib = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(uteVO,
							utente_bib);
					utentiDao.aggiornaUtenteBiblioteca(utenteBib);

					utenteVO = uteVO;
					utenteVO.setNuovoUte(true);
					utenteVO.setCodiceUtente(utente_bib.getId_utenti().getCod_utente());
					utenteVO.setIdUtente(String.valueOf((utente_bib.getId_utenti().getId_utenti())));
					utenteVO.setIdUtenteBiblioteca(String.valueOf(utenteBib.getId_utenti_biblioteca()));
				}
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return utenteVO;
	}

	private void aggiornaDirittiUtente(Trl_utenti_biblioteca utenteBib, UtenteBibliotecaVO recUte)
			throws DaoManagerException, ApplicationException {
		UtilitaDAO hibernateDAO = new UtilitaDAO();
		UtentiDAO utentiDAO = new UtentiDAO();
		ServiziDAO serviziDAO = new ServiziDAO();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		List servizi = recUte.getAutorizzazioni().getServizi();
		int index = 0;
		// poi scorre servizi e :
		// se new inserisce servizio in tab relazione
		// se mod aggiorna servizio in tab relazione
		// se del cancella servizio in tab relazione
		// se Old o DelEli non fa nulla
		int size = ValidazioneDati.size(servizi);
		while (index < size) {
			ServizioVO servUte = (ServizioVO) servizi.get(index);
			Tbl_servizio servizio = serviziDAO.getServizioBiblioteca(servUte.getCodPolo(), servUte.getCodBib(),
					servUte.getCodice(), servUte.getServizio());
			// almaviva5_20130830 #5397
			if (servizio == null && ValidazioneDati.in(servUte.getStato(), ServizioVO.NEW, ServizioVO.MOD))
				throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO,
						servUte.getDescrizioneTipoServizio());

			if (servUte.getStato() == ServizioVO.NEW) {
				servUte.setUteIns(recUte.getUteVar());
				servUte.setUteVar(recUte.getUteVar());
				servUte.setFlCanc("N");
				Trl_diritti_utente diritto = utentiDAO
						.verificaEsistenzaDirittoUtenteBiblioteca(utenteBib.getId_utenti(), servizio);
				diritto = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte, diritto);

				// almaviva4 intervento date 11.12.09
				if (diritto != null) {

					Date dataIniAut = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getInizioAuto())) {
							dataIniAut = formato.parse(recUte.getBibliopolo().getInizioAuto());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_inizio_serv() == null) {
						if (dataIniAut != null) {
							diritto.setData_inizio_serv(dataIniAut);
						}
					}
					Date dataFineAut = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getFineAuto())) {
							dataFineAut = formato.parse(recUte.getBibliopolo().getFineAuto());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_fine_serv() == null) {
						if (dataFineAut != null) {
							diritto.setData_fine_serv(dataFineAut);
						}
					}

					Date dataIniSosp = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getInizioSosp())) {
							dataIniSosp = formato.parse(recUte.getBibliopolo().getInizioSosp());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_inizio_sosp() == null) {
						if (dataIniSosp != null) {
							diritto.setData_inizio_sosp(dataIniSosp);
						}
					}
					Date dataFineSosp = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getFineSosp())) {
							dataFineSosp = formato.parse(recUte.getBibliopolo().getFineSosp());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_fine_sosp() == null) {
						if (dataFineSosp != null) {
							diritto.setData_fine_sosp(dataFineSosp);
						}
					}
				}
				utentiDAO.inserisciDirittoUtente(utenteBib.getId_utenti(), servizio, diritto);
			}
			if (servUte.getStato() == ServizioVO.DELDELMOD || servUte.getStato() == ServizioVO.DELDELOLD) {
				Trl_diritti_utente diritto = utentiDAO.getDirittoUtenteBiblioteca(utenteBib.getId_utenti(), servizio);
				if (diritto != null) {
					diritto.setFl_canc('S');
					diritto.setUte_var(recUte.getUteVar());
					utentiDAO.aggiornaDirittoUtente(diritto);
				}
			}
			if (servUte.getStato() == ServizioVO.MOD) {
				servUte.setUteVar(recUte.getUteVar());

				// Trl_diritti_utente diritto =
				// utentiDAO.getDirittoUtenteBiblioteca(utenteBib.getId_utenti(),servizio);
				Trl_diritti_utente diritto = utentiDAO
						.verificaEsistenzaDirittoUtenteBiblioteca(utenteBib.getId_utenti(), servizio);

				// almaviva4 intervento date 11.12.09
				if (diritto != null) {
					Date dataIniAut = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getInizioAuto())) {
							dataIniAut = formato.parse(recUte.getBibliopolo().getInizioAuto());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_inizio_serv() == null) {
						if (dataIniAut != null) {
							diritto.setData_inizio_serv(dataIniAut);
						}
					}
					Date dataFineAut = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getFineAuto())) {
							dataFineAut = formato.parse(recUte.getBibliopolo().getFineAuto());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_fine_serv() == null) {
						if (dataFineAut != null) {
							diritto.setData_fine_serv(dataFineAut);
						}
					}

					Date dataIniSosp = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getInizioSosp())) {
							dataIniSosp = formato.parse(recUte.getBibliopolo().getInizioSosp());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_inizio_sosp() == null) {
						if (dataIniSosp != null) {
							diritto.setData_inizio_sosp(dataIniSosp);
						}
					}
					Date dataFineSosp = null;
					try {
						if (!ValidazioneDati.strIsNull(recUte.getBibliopolo().getFineSosp())) {
							dataFineSosp = formato.parse(recUte.getBibliopolo().getFineSosp());
						}

					} catch (ParseException e) {
						log.error("", e);
					}

					if (diritto.getData_fine_sosp() == null) {
						if (dataFineSosp != null) {
							diritto.setData_fine_sosp(dataFineSosp);
						}
					}
					if (diritto.getFl_canc() == 'S') // diritto esistente xchè cancellato logicamente
					{
						diritto.setFl_canc('N');
					}
				}
				Trl_diritti_utente trl_diritti_utente = ServiziConversioneVO.daWebAHibernateDirittoUtente(servUte,
						diritto);
				utentiDAO.aggiornaDirittoUtente(trl_diritti_utente);
			}
			index++;
		}
	}

	private void aggiornaMaterie_Utente(Trl_utenti_biblioteca utenteBib, UtenteBibliotecaVO recUte)
			throws DaoManagerException {
		MaterieDAO materieDAO = new MaterieDAO();

		// if (recUte.getProfessione().getMaterie().size()==0) return;

		String uteIns = utenteBib.getUte_ins();
		String uteVar = utenteBib.getUte_var();

		// MateriaVO
		List materieAssociateVO = new ArrayList(recUte.getProfessione().getMaterie());
		// ||
		// \/
		// Tbl_materie
		List materieAssociateHib = new ArrayList();
		Iterator iterator = materieAssociateVO.iterator();
		while (iterator.hasNext()) {
			materieAssociateHib.add(materieDAO.getMateriaBibliotecaById(((MateriaVO) iterator.next()).getIdMateria()));
		}

		// Tbl_materie
		List materieGiaAssociateHib = new ArrayList();
		// Trl_materie_utenti
		iterator = utenteBib.getId_utenti().getTrl_materie_utenti().iterator();
		Trl_materie_utenti materia_utente;
		while (iterator.hasNext()) {
			materia_utente = (Trl_materie_utenti) iterator.next();
			if (materia_utente.getFl_canc() != 'S')
				materieGiaAssociateHib.add(materia_utente.getId_materia());
		}

		// Determino le nuove materie da inserire come differenza tra
		// materie presenti nel VO di ingresso e quelle già presenti sul DB
		List listaAppoggio = new ArrayList(materieAssociateHib);
		listaAppoggio.removeAll(materieGiaAssociateHib);
		iterator = listaAppoggio.iterator();
		Timestamp now = DaoManager.now();
		while (iterator.hasNext()) {
			materia_utente = new Trl_materie_utenti();
			materia_utente.setId_utenti(utenteBib.getId_utenti());
			materia_utente.setId_materia((Tbl_materie) iterator.next());
			materia_utente.setFl_canc('N');
			materia_utente.setTs_ins(now);
			materia_utente.setTs_var(now);
			materia_utente.setUte_ins(uteIns);
			materia_utente.setUte_var(uteVar);

			materieDAO.inserisciMateriaUtente(materia_utente);
		}

		// Determino le materie da cancellare come differenza tra quelle
		// presenti nel db
		// e quelle presenti nel VO di ingresso
		listaAppoggio = new ArrayList(materieGiaAssociateHib);
		listaAppoggio.removeAll(materieAssociateHib);
		iterator = listaAppoggio.iterator();
		while (iterator.hasNext()) {
			materia_utente = materieDAO.getMateriaUtenteBiblioteca(utenteBib.getId_utenti(),
					(Tbl_materie) iterator.next());
			materia_utente.setFl_canc('S');
			materia_utente.setTs_var(now);
			materia_utente.setUte_var(uteVar);

			materieDAO.aggiornaMateriaUtente(materia_utente);
		}

	}

	private void aggiornaMaterieUtente(Trl_utenti_biblioteca utenteBib, UtenteBibliotecaVO recUte)
			throws DaoManagerException {
		MaterieDAO materieDAO = new MaterieDAO();

		List materieAsso = recUte.getProfessione().getMaterie();
		int index = 0;
		// poi scorre materie e :
		// se INIZIALE_NON_SELEZIONATO = 0;
		// se INSERITO_NUOVO = 1;
		// se INIZIALE_SELEZIONATO = 2;
		// se CANCELLATO_DA_INIZIALE_SELEZIONATO = 3;
		// se 0 o 2 non fa nulla perchè non toccato
		// se 1 inserisce relazione
		// se 3 cancello relazione
		while (index < materieAsso.size()) {
			MateriaVO matUte = (MateriaVO) materieAsso.get(index);
			Tbl_materie materia = materieDAO.getMateriaBibliotecaById(matUte.getIdMateria());

			if (matUte.getStato() == MateriaVO.INSERITO_NUOVO) {
				matUte.setUteIns(utenteBib.getUte_ins());
				matUte.setUteVar(utenteBib.getUte_var());
				matUte.setFlCanc("N");
				Trl_materie_utenti materia_utente = materieDAO
						.verificaEsistenzaMateriaUtenteBiblioteca(utenteBib.getId_utenti(), materia);
				materia_utente = ServiziConversioneVO.daWebAHibernateMateriaUtente(matUte, materia_utente);
				materieDAO.inserisciMateriaUtente(utenteBib.getId_utenti(), materia, materia_utente);
			}

			if (matUte.getStato() == MateriaVO.CANCELLATO_DA_INIZIALE_SELEZIONATO) {
				Trl_materie_utenti materia_utente = materieDAO.getMateriaUtenteBiblioteca(utenteBib.getId_utenti(),
						materia);
				materia_utente.setFl_canc('S');
				materia_utente.setTs_var(new Timestamp(System.currentTimeMillis()));
				materia_utente.setUte_var(utenteBib.getUte_var());
				materieDAO.aggiornaMateriaUtente(materia_utente);
			}
			index++;
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
	 *
	 */
	public boolean updateUtente(String ticket, UtenteBibliotecaVO ub) throws DataException, ApplicationException {
		boolean ret = true;

		try {
			checkTicket(ticket);
			UtentiDAO dao = new UtentiDAO();
			OccupazioniDAO oDao = new OccupazioniDAO();
			UtilitaDAO uDao = new UtilitaDAO();
			TitoliDiStudioDAO tsDao = new TitoliDiStudioDAO();

			Trl_utenti_biblioteca oldUte = dao.getUtenteBibliotecaById(Integer.valueOf(ub.getIdUtenteBiblioteca()));

			// almaviva5_20150430 aggiornamento mail secondarie
			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			String newMail = ub.getAnagrafe().getPostaElettronica();
			if (ValidazioneDati.isFilled(newMail)) {
				String oldMail = ValidazioneDati.trimOrEmpty(oldUte.getId_utenti().getInd_posta_elettr());
				if (ValidazioneDati.isFilled(oldMail) && !oldMail.equalsIgnoreCase(newMail)) {
					List<Tbl_utenti> utenti = dao.checkMailUtente(ub.getIdUtente(), null, oldMail);
					for (Tbl_utenti u : utenti) {
						u.setInd_posta_elettr2(newMail);
						u.setUte_var(firmaUtente);
						dao.aggiornaUtente(u);
					}
				}
			}

			//almaviva5_20191031 #7173 check codice isil
			checkCodiceAnagrafe(ub);

			String autOriginal = "";
			String autDiMappa = "";
			int numServNew = 0;

			if (oldUte.getCod_tipo_aut() != null) {
				autOriginal = oldUte.getCod_tipo_aut().trim();
			}
			if (ub.getAutorizzazioni().getCodTipoAutor() != null) {
				autDiMappa = ub.getAutorizzazioni().getCodTipoAutor().trim();
			}
			if (ub.getAutorizzazioni() != null && ub.getAutorizzazioni().getListaServizi() != null
					&& ub.getAutorizzazioni().getListaServizi().size() > 0) {
				numServNew = ub.getAutorizzazioni().getListaServizi().size();
			}

			Tbl_utenti utente = ServiziConversioneVO.daWebAHibernateUteDettaglio(ub, oldUte.getId_utenti());
			utente.setUte_var(firmaUtente);
			ub.setUteIns(utente.getUte_ins());
			ub.setTsIns(utente.getTs_ins());
			ub.setUteVar(utente.getUte_var());
			oldUte = ServiziConversioneVO.daWebAHibernateUteBibDettaglio(ub, oldUte);
			Tbl_occupazioni id_occupazioni = oldUte.getId_occupazioni();
			if (id_occupazioni != null)
				oldUte.setId_occupazioni(oDao.getOccupazioneById(id_occupazioni.getId_occupazioni()));

			Tbl_specificita_titoli_studio id_specificita_titoli_studio = oldUte.getId_specificita_titoli_studio();
			if (id_specificita_titoli_studio != null)
				oldUte.setId_specificita_titoli_studio(
						tsDao.getTitoloStudioById(id_specificita_titoli_studio.getId_specificita_titoli_studio()));

			dao.aggiornaUtente(utente);

			if (!autOriginal.equals(autDiMappa)) {
				List servOld = dao.getListaDirittiUtente(oldUte.getId_utenti_biblioteca());
				int riga = 0;
				for (riga = 0; riga < servOld.size(); riga++) {
					Trl_diritti_utente singSerOld = (Trl_diritti_utente) servOld.get(riga);
					singSerOld.setUte_var(utente.getUte_var());
					singSerOld.setFl_canc('S');
					dao.aggiornaDirittoUtente(singSerOld);
				}
			}
			this.aggiornaDirittiUtente(oldUte, ub);
			this.aggiornaMaterie_Utente(oldUte, ub);

		} catch (ApplicationException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
			ret = false;
		}
		return ret;
	}

	private void checkCodiceAnagrafe(UtenteBibliotecaVO ub)
			throws DaoManagerException, ApplicationException {
		String isil = ub.getBibliopolo().getCodiceAnagrafe();
		//controllo codice isil univoco
		if (ValidazioneDati.isFilled(isil)) {
			UtentiDAO dao = new UtentiDAO();
			Optional<Tbl_utenti> utenteByIsil = Optional.ofNullable(dao.getUtenteByIsil(isil));
			boolean error = false;
			if (utenteByIsil.isPresent()) {
				if (ub.isNuovoUte())
					// nuovo utente = errore
					error = true;
				else {
					// modifica utente, check id
					int id_utente = Integer.valueOf(ub.getIdUtente());
					error = (utenteByIsil.get().getId_utenti() != id_utente);
				}

				if (error)
					throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_ANAGRAFE_BIB_ESISTENTE, isil);
			}
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
	 *
	 */
	public boolean cancelUtenteBiblioteca(String ticket, UtenteBibliotecaVO recUte)
			throws DataException, ApplicationException {
		boolean ret = true;

		try {
			checkTicket(ticket);
			UtentiDAO utenteDAO = new UtentiDAO();
			MaterieDAO materieDAO = new MaterieDAO();

			// Carico associazione utente-biblioteca
			Trl_utenti_biblioteca uteBib = utenteDAO
					.getUtenteBibliotecaById(Integer.valueOf(recUte.getIdUtenteBiblioteca()));

			// Aggiornamento trl_utenti_biblioteca
			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			uteBib.setUte_var(firmaUtente);
			uteBib.setFl_canc('S');
			utenteDAO.aggiornaUtenteBiblioteca(uteBib);

			// cancellazione diritti
			List<Trl_diritti_utente> servizi = utenteDAO.getListaDirittiUtente(uteBib.getId_utenti_biblioteca());
			if (ValidazioneDati.isFilled(servizi))
				for (Trl_diritti_utente srv : servizi) {
					srv.setUte_var(firmaUtente);
					srv.setFl_canc('S');
					utenteDAO.aggiornaDirittoUtente(srv);
				}

			// almaviva5_20110211 segnalazione MO1
			// controllare che non ci siano piu' utenti su trl_utenti_biblioteca
			// e cancellare logicamente su tbl_utenti
			Tbl_utenti utente = uteBib.getId_utenti();
			if (utenteDAO.getNumUtenteBibliotecaByIdUtente(utente) < 1) {

				// Aggiornamento tbl_utenti
				utente.setUte_var(firmaUtente);
				utente.setFl_canc('S');
				// utente.setCod_fiscale(null);
				// utente.setInd_posta_elettr(null);
				utenteDAO.aggiornaUtente(utente);

				// Aggiornamento materie
				Trl_materie_utenti materia_utente;
				List<MateriaVO> materie = recUte.getProfessione().getMaterie();
				if (ValidazioneDati.isFilled(materie))
					for (MateriaVO mat : materie) {
						Tbl_materie materia = materieDAO.getMateriaBibliotecaById(mat.getIdMateria());
						materia_utente = materieDAO.getMateriaUtenteBiblioteca(utente, materia);
						materia_utente.setFl_canc('S');
						materia_utente.setUte_var(firmaUtente);
						materieDAO.aggiornaMateriaUtente(materia_utente);
					}
			}

		} catch (Exception e) {
			log.error("", e);
			ret = false;
		}
		return ret;
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
	 *
	 */
	public int verificaMovimentiUtente(String ticket, String idUte)
			throws ResourceNotFoundException, ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		try {
			checkTicket(ticket);
			return dao.verificaMovimentiUtente(Integer.parseInt(idUte));
		} catch (NumberFormatException e) {
			throw new ApplicationException(e);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}

	public boolean esisteRichiestaPerUtente(String ticket, MovimentoVO movimento) throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		try {
			checkTicket(ticket);
			int annata = getAnnataPeriodico(movimento);

			if (movimento.isRichiestaSuInventario())

				return richiesteDAO.esistonoMovimentiAttiviPerUtente(movimento.getCodPolo(),
						movimento.getCodBibOperante(), movimento.getCodBibUte(), movimento.getCodUte(),
						movimento.getCodBibInv(), movimento.getCodSerieInv(),
						Integer.parseInt(movimento.getCodInvenInv()), annata);
			else
				return richiesteDAO.esistonoMovimentiAttiviPerUtente(movimento.getCodPolo(),
						movimento.getCodBibOperante(), movimento.getCodBibUte(), movimento.getCodUte(),
						movimento.getCodBibDocLett(), movimento.getTipoDocLett(),
						Long.parseLong(movimento.getCodDocLet()), Short.parseShort(movimento.getProgrEsempDocLet()),
						annata);

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws DaoManagerException
	 * @throws RemoteException
	 * @return Map con 2 chiavi<br/>
	 *         <ul>
	 *         <li><strong>RICHIESTE:</strong> lista delle richieste (istanze di
	 *         MovimentoListaVO)</li>
	 *         <li><strong>SOLLECITI:</strong> lista dei solleciti (istanze di
	 *         SollecitiVO)</li>
	 *         </ul>
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 */
	public Map getListaMovimentiPerErogazione(String ticket, MovimentoVO filtroMov, RicercaRichiesteType filtro,
			Locale locale) throws ResourceNotFoundException, ApplicationException, RemoteException {

		UtentiDAO dao = new UtentiDAO();
		UtilitaDAO daoServizi = new UtilitaDAO();
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		Map output = new HashMap();
		output.put(ServiziConstant.RICHIESTE, ValidazioneDati.emptyList(MovimentoListaVO.class));
		output.put(ServiziConstant.SOLLECITI, ValidazioneDati.emptyList(SollecitiVO.class));

		String ordinamento = "";
		try {
			checkTicket(ticket);
			List<Tbl_richiesta_servizio> richieste = null;

			// Determino il tipo di ordinamento
			if (ValidazioneDati.isFilled(filtroMov.getTipoOrdinamento())) {
				TB_CODICI cod = CodiciProvider.cercaCodice(filtroMov.getTipoOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_MOVIMENTI, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					ordinamento = cod.getDs_cdsbn_ulteriore();
			}

			// Ricerca richieste in base ai criteri
			switch (filtro) {
			case RICERCA_PER_UTENTE:
				richieste = dao.getListaRichiestePerUtente(filtroMov.getCodPolo(), filtroMov.getCodBibUte(),
						filtroMov.getCodUte(), filtroMov.getCodBibOperante(), ordinamento, filtroMov);
				break;

			case RICERCA_PER_INVENTARIO:
				richieste = richiesteDAO.getListaRichiestePerInventario(filtroMov.getCodPolo(),
						filtroMov.getCodBibInv(), filtroMov.getCodSerieInv(),
						Integer.parseInt(filtroMov.getCodInvenInv()), ordinamento, filtroMov);
				break;

			case RICERCA_PER_SEGNATURA:
				richieste = richiesteDAO.getListaRichiestePerSegnatura(filtroMov.getCodPolo(),
						filtroMov.getCodBibDocLett(), filtroMov.getTipoDocLett(),
						Long.parseLong(filtroMov.getCodDocLet()), Short.parseShort(filtroMov.getProgrEsempDocLet()),
						ordinamento, filtroMov);
				break;

			case RICERCA_RICHIESTE_SCADUTE_UTENTE:
				richieste = dao.getListaRichiestePerUtenteScadute(filtroMov.getCodPolo(),
						filtroMov.getCodBibUte(), filtroMov.getCodUte(), filtroMov.getCodBibOperante(), ordinamento);
			}

			int size = ValidazioneDati.size(richieste);
			List<MovimentoListaVO> listaRichieste = new ArrayList(size);
			List<SollecitiVO> listaSolleciti = new ArrayList(size * 3); // max. 3 solleciti
			Map<Integer, Tbl_iter_servizio> iter_cache = new THashMap<Integer, Tbl_iter_servizio>();
			Timestamp now = DaoManager.now();

			for (Tbl_richiesta_servizio req : richieste) {
				MovimentoListaVO movimento = ServiziConversioneVO.daHibernateAWebMovimentoLista(req, locale, now);
				this.decodificaCodiciMovimento(movimento, filtroMov.getCodPolo(), filtroMov.getCodBibOperante(), locale,
						req);
				impostaFlagConsegnato(filtroMov, movimento, req, iter_cache);
				listaRichieste.add(movimento);

				// Ricerca eventuali solleciti
				for (Object row : req.getTbl_solleciti()) {
					Tbl_solleciti sollecito = (Tbl_solleciti) row;
					SollecitiVO sollecitoVO = ServiziConversioneVO.daHibernateAWebSolleciti(sollecito);
					sollecitoVO.setCodBib(filtroMov.getCodBibOperante());
					sollecitoVO.setMovimento(movimento);
					listaSolleciti.add(sollecitoVO);
				}
			}

			if (filtroMov instanceof MovimentoRicercaVO) {
				// aggiungi prenotazioni posto senza supporto
				if (((MovimentoRicercaVO) filtroMov).isIncludiPrenotazioniPosto()) {
					RicercaPrenotazionePostoVO ricerca = new RicercaPrenotazionePostoVO();
					ricerca.setCodPolo(filtroMov.getCodPolo());
					ricerca.setCodBib(filtroMov.getCodBibOperante());
					ricerca.setUtente(getUtente(ticket, filtroMov.getCodUte()));
					ricerca.setEscludiPrenotConSupporto(true);
					List<PrenotazionePostoVO> prenotazioniPosto = sale.getListaPrenotazioniPosto(ticket, ricerca);

					for (PrenotazionePostoVO pp : prenotazioniPosto) {
						MovimentoListaVO m = creaDettaglioRichiestaPerPrenotazionePosto(pp, now);
						if (m != null)
							listaRichieste.add(m);
					}
				}
			}

			output.put(ServiziConstant.RICHIESTE, listaRichieste);
			output.put(ServiziConstant.SOLLECITI, listaSolleciti);
			// almaviva5_20120713
			Collections.sort(listaSolleciti, SollecitiVO.ORDINA_PER_DATA_SOLLECITO);
			ordinaListaMovimenti(listaRichieste, filtroMov.getTipoOrdinamento());

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (Exception e) {
			throw new EJBException(e);
		}

		return output;
	}

	private MovimentoListaVO creaDettaglioRichiestaPerPrenotazionePosto(PrenotazionePostoVO pp, Timestamp now) {
		try {
			MovimentoListaVO m = new MovimentoListaVO();
			m.setDataRichiesta(pp.getTsIns());
			m.setAttivita(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_ATTIVITA_ITER,
					StatoIterRichiesta.PRENOTAZIONE_POSTO.getISOCode()));
			m.setTipoServizio(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE,
					pp.getCatMediazione()));
			// m.setStato_richiesta(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_RICHIESTA,
			// "A")); //immessa
			m.setStato_richiesta(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_PRENOTAZIONE_POSTO,
					SaleUtil.getStatoDinamico(pp.getStato(), pp.getTs_fine(), now).getStato()));
			m.setStato_movimento(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_MOVIMENTO, "P")); // prenotazione
			m.setPrenotazionePosto(pp);
			return m;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	private final void decodificaCodiciMovimento(MovimentoListaVO movimento, String codPolo, String codBib,
			Locale locale, Tbl_richiesta_servizio richiesta) throws Exception {

		movimento.setCodPolo(codPolo);
		movimento.setCodBibOperante(codBib);

		movimento.decode();

		Session session = new UtilitaDAO().getCurrentSession();

		if (movimento.isRichiestaSuInventario()) {
			Tbc_inventario inv = richiesta.getCd_polo_inv();
			Tb_titolo t = inv.getB();
			if (t != null) {
				movimento.setKcles(t.getKy_cles1_t() + t.getKy_cles2_t()); // chiave ordinamento
				String isbd = ConversioneHibernateVO.toWeb().estraiTitoloPerAreaServizi(t);

				// almaviva5_20100830 per documenti W devo aggiungere in testa il
				// titolo proprio della M superiore (area 200 sottocampo $a)
				if (ValidazioneDati.equals(t.getCd_natura(), 'W')) {
					TitoloDAO dao = new TitoloDAO();
					Tb_titolo sup = dao.getMonografiaSuperiore(t);
					if (sup != null) {
						IsbdVO tokens = IsbdTokenizer.tokenize(sup.getIsbd(), sup.getIndice_isbd());
						String tit$a = tokens.getT200$a_areaTitolo();
						if (ValidazioneDati.isFilled(tit$a)) {
							StringBuilder tmp = new StringBuilder();
							tmp.append(tit$a).append(". ").append(isbd);
							isbd = tmp.toString();
						}
					}
				}

				// almaviva5_20100323 #3561 split del titolo in etichette unimarc
				movimento.setTitolo(isbd);
				movimento.setBid(t.getBid());
				movimento.setNatura(String.valueOf(t.getCd_natura()));

				session.evict(t);
			} else
				movimento.setTitolo(ServiziConstant.MSG_INVENTARIO_CANCELLATO);

			movimento.setCat_fruizione(inv.getCd_frui());
			movimento.setCat_riproduzione(inv.getCd_riproducibilita());
			movimento.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(richiesta));

			// se anno periodico su mov. é vuoto prendo quello dell'inventario (se presente)
			Integer anno = inv.getAnno_abb();
			if (ValidazioneDati.strIsNull(movimento.getAnnoPeriodico()) && ValidazioneDati.isFilled(anno))
				movimento.setAnnoPeriodico(String.valueOf(anno));

			session.evict(inv);

		}

		if (movimento.isRichiestaSuSegnatura()) {
			Tbl_documenti_lettori doc = richiesta.getId_documenti_lettore();
			String titolo = doc.getTitolo();
			movimento.setTitolo(titolo);
			movimento.setNatura(String.valueOf(doc.getNatura()));
			movimento.setBid(doc.getBid());
			movimento.setCat_fruizione(doc.getCd_catfrui());

			// almaviva5_20100830 chiave titolo fittizia per ordinamento (senza asterisco iniziale)
			movimento.setKcles(OrdinamentoCollocazione2.normalizza(titolo.substring(titolo.indexOf('*') + 1)));

			String segnatura = ValidazioneDati.trimOrEmpty(doc.getSegnatura());
			if (movimento.isPeriodico())
				segnatura += " [" + movimento.getAnnoPeriodico() + "]";

			movimento.setSegnatura(segnatura);

			session.evict(doc);
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
	 *
	 */
	public void cancellaRichieste(String ticket, Long[] codRichieste, String uteVar) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		try {
			checkTicket(ticket);
			richiesteDAO.cancellaRichieste(codRichieste, uteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
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
	 *
	 */
	public void rifiutaRichieste(String ticket, Long[] codRichieste, String uteVar, boolean inviaMailNotifica)
			throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		Tbf_poloDao pdao = new Tbf_poloDao();
		Set<Tbl_prenotazione_posto> prenotazioni = new HashSet<Tbl_prenotazione_posto>();
		try {
			checkTicket(ticket);
			String codPolo = pdao.getPolo().getCd_polo();
			for (long idRichiesta : codRichieste) {
				Tbl_richiesta_servizio richiesta = richiesteDAO.getMovimentoById(idRichiesta);
				prenotazioni.addAll(richiesta.getPrenotazioni_posti());
				richiesta = richiesteDAO.rifiutaRichiesta(idRichiesta, uteVar);
				if (inviaMailNotifica)
					inviaMailUtenteRifiutoRichiesta(codPolo, richiesta);
			}

			// check prenotazioni posto
			// le prenotazioni senza più alcuna richiesta collegata vengono annullate
			SaleDAO sdao = new SaleDAO();
			for (Tbl_prenotazione_posto pp : prenotazioni) {
				if (sdao.countRichiesteCollegatePrenotazione(pp) < 1) {
					PrenotazionePostoVO prenotazione = ConvertToWeb.Sale.prenotazione(pp, null);
					sale.rifiutaPrenotazionePosto(ticket, prenotazione, false);
				}
			}

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public MovimentoVO aggiornaRichiesta(String ticket, MovimentoVO mov, int idServizio, boolean update_all)
			throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		try {
			checkTicket(ticket);
			mov.validate(new MovimentoValidator());

			boolean nuovaRichiesta = mov.isNuovo();
			mov.setUteVar(DaoManager.getFirmaUtente(ticket));

			// almaviva5_20170403 prenotazioni posto
			PrenotazionePostoVO pp = mov.getPrenotazionePosto();
			if (update_all && pp != null) {
				pp.setUteVar(DaoManager.getFirmaUtente(ticket));
				pp = sale.aggiornaPrenotazionePosto(ticket, pp, false);
				mov.setPrenotazionePosto(pp);
			}

			Tbl_richiesta_servizio richiesta = ServiziConversioneVO.daWebAHibernateMovimento(mov, idServizio);

			richiesta = richiesteDAO.aggiornaRichiesta(richiesta);

			long idRichiesta = richiesta.getCod_rich_serv();
			mov.setCodRichServ(idRichiesta + "");
			mov.setIdRichiesta(idRichiesta);
			mov.setTsVar(richiesta.getTs_var()); // campo versione

			// almaviva5_20150127 servizi ill
			if (mov.isRichiestaILL()) {
				DatiRichiestaILLVO datiILL = mov.getDatiILL();
				datiILL.setCod_rich_serv(idRichiesta);
				datiILL.setUteVar(DaoManager.getFirmaUtente(ticket));
				datiILL = ill_handler.aggiornaDatiRichiestaILL(ticket, datiILL);
				mov.setDatiILL(datiILL);
			}
			// almaviva5_20160707 se ho inserito una nuova richiesta (non prenotazione) va
			// notificata
			// la biblioteca
			if (nuovaRichiesta)
				inviaMailNotificaNuovaRichiesta(
						CommandInvokeVO.build(ticket, CommandType.SRV_NOTIFICA_NUOVA_RICHIESTA, mov.copy()));

			return mov;

		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (DaoManagerException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		} catch (ValidationException e) {
			// almaviva5_20101018 #3858
			throw new ApplicationException(e.getErrorCode(), e.getLabels());
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public MovimentoVO aggiornaRichiesta(String ticket, MovimentoVO mov, int idServizio) throws ApplicationException {
		return this.aggiornaRichiesta(ticket, mov, idServizio, true);
	}

	public MovimentoVO aggiornaRichiestaPerCambioServizio(String ticket, MovimentoVO nuovaRichiesta,
			long codRichDaCancellare, int idServizio, String uteVar) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			Tbl_richiesta_servizio nuovaRichiestaCambioServizio = ServiziConversioneVO
					.daWebAHibernateMovimento(nuovaRichiesta, idServizio);
			richiesteDAO.aggiornaRichiesta(nuovaRichiestaCambioServizio, codRichDaCancellare, uteVar);
			return ServiziConversioneVO.daHibernateAWebMovimento(nuovaRichiestaCambioServizio, Locale.getDefault());

		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public int getNumeroMovimentiAttivi(String ticket, String codPolo, String codBibInv, String codSerieInv,
			int codInven) throws ApplicationException {
		// RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			return getServiziCommon().getNumeroMovimentiAttivi(codPolo, codBibInv, codSerieInv, codInven);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		}
	}

	public int getNumeroRichiesteAttivePerUtente(String ticket, MovimentoVO anaMov) throws ApplicationException {
		UtentiDAO dao = new UtentiDAO();

		try {
			checkTicket(ticket);
			return dao.getNumeroRichiestePerUtenteTipoServizio(anaMov.getCodPolo(), anaMov.getCodBibUte(),
					anaMov.getCodUte(), anaMov.getCodBibOperante(), anaMov);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}

	public int getNumeroMovimentiAttiviPerUtente(String ticket, MovimentoVO anaMov) throws ApplicationException {
		UtentiDAO dao = new UtentiDAO();

		try {
			checkTicket(ticket);
			return dao.getNumeroMovimentiPerUtenteTipoServizio(anaMov.getCodPolo(), anaMov.getCodBibUte(),
					anaMov.getCodUte(), anaMov.getCodBibOperante(), anaMov);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}

	public List<MovimentoVO> getListaMovimentiAttiviPerUtente(String ticket, MovimentoVO anaMov)
			throws ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		try {
			checkTicket(ticket);
			List<Tbl_richiesta_servizio> movimenti = dao.getListaMovimentiPerUtenteTipoServizio(anaMov.getCodPolo(),
					anaMov.getCodBibUte(), anaMov.getCodUte(), anaMov.getCodBibOperante(), anaMov);
			List<MovimentoVO> listaOutput = new ArrayList<MovimentoVO>();
			MovimentoVO movimentoVO;
			for (int i = 0; i < movimenti.size(); i++) {
				movimentoVO = ServiziConversioneVO.daHibernateAWebMovimento(movimenti.get(i),
						Locale.getDefault());
				listaOutput.add(movimentoVO);
			}
			return listaOutput;
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public int getNumeroRichiesteGiornalierePerServizio(String ticket, MovimentoVO anaMov) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			List richieste = richiesteDAO.getListaRichiesteGiornaliere(anaMov.getCodPolo(), anaMov.getCodBibOperante(),
					anaMov.getCodTipoServ(), anaMov.getCodServ(), null);

			return richieste.size();
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public int getNumeroRichiesteGiornalierePerTipoServizio(String ticket, MovimentoVO anaMov)
			throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			// List richieste = richiesteDAO.getListaRichiesteGiornaliere(anaMov
			// .getCodPolo(), anaMov.getCodBibOperante(), anaMov
			// .getCodTipoServ(), anaMov.getCodServ(), null);

			// return richieste.size();

			return richiesteDAO.getNumeroRichiesteGiornaliereTipoServizio(anaMov.getCodPolo(),
					anaMov.getCodBibOperante(), anaMov.getCodTipoServ());

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
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
	 *
	 */
	public List getListaSollecitiUte(String ticket, MovimentoVO anaMov, RicercaRichiesteType filtro)
			throws ResourceNotFoundException, ApplicationException, RemoteException {

		UtentiDAO dao = new UtentiDAO();
		List listaSolleciti = new ArrayList();
		String ordinamento = "";

		try {
			checkTicket(ticket);

			// Determino il tipo di ordinamento
			if (ValidazioneDati.isFilled(anaMov.getTipoOrdinamento())) {
				TB_CODICI tb_codici = CodiciProvider.cercaCodice(anaMov.getTipoOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_MOVIMENTI, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (tb_codici != null)
					ordinamento = tb_codici.getDs_cdsbn_ulteriore();
			}

			List richieste = dao.getListaRichiestePerUtente(anaMov.getCodPolo(), anaMov.getCodBibUte(),
					anaMov.getCodUte(), anaMov.getCodBibOperante(), ordinamento, null);

			Iterator iterSolleciti = null;
			List solleciti;
			Tbl_solleciti sollecito = null;
			SollecitiVO sollecitoVO = null;

			Tbl_richiesta_servizio richiesta = null;
			Iterator iter = richieste.iterator();
			while (iter.hasNext()) {
				richiesta = (Tbl_richiesta_servizio) iter.next();
				if (richiesta.getTbl_solleciti() != null && richiesta.getTbl_solleciti().size() > 0) {
					iterSolleciti = richiesta.getTbl_solleciti().iterator();
					while (iterSolleciti.hasNext()) {
						sollecito = (Tbl_solleciti) iterSolleciti.next();
						sollecitoVO = ServiziConversioneVO.daHibernateAWebSolleciti(sollecito);
						sollecitoVO.setCodBib(anaMov.getCodBibOperante());

						listaSolleciti.add(sollecitoVO);
					}
				}
			}
		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return listaSolleciti;
		// return dao.getListaSollecitiUte(anaMov, filtro, ticket);
	}

	private void distinctModalitaErogazionePerCodAttivita(List listaModalitaErogazione) {
		Iterator iterator = listaModalitaErogazione.iterator();
		List lstCodAttivita = new ArrayList();
		List daEliminare = new ArrayList();

		while (iterator.hasNext()) {
			Trl_tariffe_modalita_erogazione tme = (Trl_tariffe_modalita_erogazione) iterator.next();
			if (lstCodAttivita.contains(tme.getCod_erog())) {
				daEliminare.add(tme);
			} else
				lstCodAttivita.add(tme.getCod_erog());
		}

		listaModalitaErogazione.removeAll(daEliminare);
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
	 *
	 */
	// public TariffeModalitaErogazioneVO getTariffaModalitaErogazione(String
	// ticket,
	// String codiceBiblioteca, String codErog, String codTipoServ,
	// String ticket) throws EJBException {
	public List getTariffaModalitaErogazione(String ticket, String codicePolo, String codiceBiblioteca,
			String codTipoServ, String fl_svolg) throws EJBException {

		UtilitaDAO dao = new UtilitaDAO();
		Tbl_modalita_erogazioneDAO modalitaErogazioneDAO = new Tbl_modalita_erogazioneDAO();

		List listaTariffeModErogVO = new ArrayList();
		List<Object[]> listaTariffeModalita = null;

		try {
			checkTicket(ticket);

			listaTariffeModalita = modalitaErogazioneDAO.getListaTariffeModalita(codicePolo, codiceBiblioteca,
					codTipoServ);

			if (ValidazioneDati.isFilled(listaTariffeModalita)) {

				if (ValidazioneDati.strIsNull(codTipoServ)) {
					// voglio solo un oggetto per ogni modalità di erogazione
					distinctPerModalitaErogazione(listaTariffeModalita);
				}

				for (Object[] modErog : listaTariffeModalita) {
					TariffeModalitaErogazioneVO tariffaModErogVO = ServiziConversioneVO
							.daHibernateAWebTariffeModalitaErogazione((Tbl_modalita_erogazione) modErog[0],
									(Trl_tariffe_modalita_erogazione) modErog[1]);

					// filtro su svolgimento locale o ill, se impostato
					if (ValidazioneDati.isFilled(fl_svolg) && !fl_svolg.equals(tariffaModErogVO.getFlSvolg()))
						continue;

					listaTariffeModErogVO.add(tariffaModErogVO);
				}
			}

			return listaTariffeModErogVO;

		} catch (DaoManagerException dE) {
			throw new EJBException(dE);
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
	 *
	 */
	// public SupportiModalitaErogazioneVO getSupportoModalitaErogazione(String
	// ticket,
	// String codiceBiblioteca, String codErog, String codSupporto,
	// String ticket) throws EJBException {
	public List getSupportoModalitaErogazione(String ticket, String codicePolo, String codiceBiblioteca,
			String codSupporto, String fl_svolg) throws EJBException {

		UtilitaDAO dao = new UtilitaDAO();
		Tbl_modalita_erogazioneDAO modalitaErogazioneDAO = new Tbl_modalita_erogazioneDAO();

		List<SupportiModalitaErogazioneVO> listaSupportiModErogVO = new ArrayList<SupportiModalitaErogazioneVO>();
		List<Object[]> listaSupportiModalita = null;

		try {
			checkTicket(ticket);

			listaSupportiModalita = modalitaErogazioneDAO.getListaSupportiModalita(codicePolo, codiceBiblioteca,
					codSupporto);

			if (ValidazioneDati.isFilled(listaSupportiModalita)) {

				for (Object[] modErog : listaSupportiModalita) {
					SupportiModalitaErogazioneVO supportoModErogVO = ServiziConversioneVO
							.daHibernateAWebSupportiModalitaErogazione((Tbl_modalita_erogazione) modErog[0],
									(Trl_supporti_modalita_erogazione) modErog[1]);

					if (!ValidazioneDati.isFilled(fl_svolg) || fl_svolg.equals(supportoModErogVO.getFlSvolg()))
						listaSupportiModErogVO.add(supportoModErogVO);
				}
			}

			return listaSupportiModErogVO;

		} catch (DaoManagerException dE) {
			throw new EJBException(dE);
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
	 *
	 */
	// public TariffeModalitaErogazioneVO getTariffaModalitaErogazione(String
	// ticket,
	// String codiceBiblioteca, String codErog, String codTipoServ,
	// String ticket) throws EJBException {
	public List getTariffaModalitaErogaz(String ticket, String codicePolo, String codiceBiblioteca)
			throws EJBException {

		UtilitaDAO dao = new UtilitaDAO();
		Tbl_modalita_erogazioneDAO modalitaErogazioneDAO = new Tbl_modalita_erogazioneDAO();

		List listaTariffeModErogVO = new ArrayList();
		List<Tbl_modalita_erogazione> listaTariffeModalitaErogazione = null;

		try {
			checkTicket(ticket);

			listaTariffeModalitaErogazione = modalitaErogazioneDAO.getListaTariffeModalita(codicePolo,
					codiceBiblioteca);
			if (ValidazioneDati.isFilled(listaTariffeModalitaErogazione)) {

				for (Tbl_modalita_erogazione modErog : listaTariffeModalitaErogazione) {
					ModalitaErogazioneVO tariffaModErogVO = ServiziConversioneVO
							.daHibernateAWebModalitaErogazione(modErog);
					listaTariffeModErogVO.add(tariffaModErogVO);
				}
			}

			return listaTariffeModErogVO;

		} catch (DaoManagerException dE) {
			throw new EJBException(dE);
		}
	}

	private void distinctIterServizioPerCodAttivita(List listaIterServizio) {
		Iterator iterator = listaIterServizio.iterator();
		List lstCodAttivita = new ArrayList();
		List daEliminare = new ArrayList();

		Tbl_iter_servizio iter = null;
		while (iterator.hasNext()) {
			iter = (Tbl_iter_servizio) iterator.next();
			if (lstCodAttivita.contains(iter.getCod_attivita())) {
				daEliminare.add(iter);
			} else
				lstCodAttivita.add(iter.getCod_attivita());
		}

		listaIterServizio.removeAll(daEliminare);

		// return listaIterServizio;
	}

	private void distinctPerModalitaErogazione(List listaTariffeModalita) {
		Iterator iterator = listaTariffeModalita.iterator();
		List<Object[]> lstTarModalita = new ArrayList();
		Set<String> listaCodErog = new HashSet<String>();

		Trl_tariffe_modalita_erogazione modErog = null;
		while (iterator.hasNext()) {
			Object[] obj = (Object[]) iterator.next();
			modErog = (Trl_tariffe_modalita_erogazione) obj[1];

			if (listaCodErog.contains(modErog.getCod_erog())) {
				iterator.remove();
			} else
				listaCodErog.add(modErog.getCod_erog());
		}

		// listaTariffeModalita.removeAll(daEliminare);

		// return listaIterServizio;
	}

	public List<IterServizioVO> getListaIterServizio(String ticket, String codicePolo, String codiceBiblioteca,
			String codTipoServ) throws EJBException {
		UtilitaDAO dao = new UtilitaDAO();

		IterServizioDAO iterServizioDAO = new IterServizioDAO();

		List listaIterServizioVO = new ArrayList();
		List<Tbl_iter_servizio> listaIterServizio = null;

		try {
			checkTicket(ticket);
			listaIterServizio = iterServizioDAO.getListaIterServizio(codicePolo, codiceBiblioteca, codTipoServ);
			if (ValidazioneDati.isFilled(listaIterServizio)) {

				if (ValidazioneDati.strIsNull(codTipoServ)) {
					// voglio solo un oggetto per ogni codice attività
					distinctIterServizioPerCodAttivita(listaIterServizio);
				}

				// allineamento con il VO
				for (Tbl_iter_servizio iter : listaIterServizio) {
					IterServizioVO iterServizioVO = ConversioneHibernateVO.toWeb().iterServizio(iter);
					listaIterServizioVO.add(iterServizioVO);
				}
			}

			return listaIterServizioVO;

		} catch (DaoManagerException dE) {
			throw new EJBException(dE);
		}
	}

	public IterServizioVO getIterServizio(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServ,
			StatoIterRichiesta stato) throws EJBException {
		IterServizioDAO dao = new IterServizioDAO();
		TipoServizioDAO tsdao = new TipoServizioDAO();
		try {
			Tbl_tipo_servizio tipoServizio = tsdao.getTipoServizio(codicePolo, codiceBiblioteca, codTipoServ);
			if (tipoServizio != null) {
				Tbl_iter_servizio iter_servizio = dao.getIterServizio(tipoServizio.getId_tipo_servizio(),
						stato.getISOCode());
				if (iter_servizio != null) {
					return ConversioneHibernateVO.toWeb().iterServizio(iter_servizio);
				}
			}

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
		return null;
	}

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @generated
	 */
	public int getNumMovimentiAttiviPerServizioUtente(String ticket, int idUtenteBiblioteca, int idServizio)
			throws EJBException {

		UtentiDAO dao = new UtentiDAO();

		int numeroServizi = 0;
		try {
			checkTicket(ticket);
			numeroServizi = dao.getNumMovimentiAttiviPerServizioUtente(idUtenteBiblioteca, idServizio);
			return numeroServizi;
		} catch (DaoManagerException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 * @generated
	 */
	public AutorizzazioneVO getTipoAutorizzazione(String ticket, String codPolo, String codBib, String codTipoAut)
			throws EJBException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();

		Tbl_tipi_autorizzazioni aut;
		AutorizzazioneVO autVO = null;
		try {
			checkTicket(ticket);
			aut = autDAO.getTipoAutorizzazione(codPolo, codBib, codTipoAut);
			if (aut != null) {
				autVO = ServiziConversioneVO.daHibernateAWebTipoAutorizzazione(aut, 0);
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return autVO;
	}

	public java.util.List getListaTipiServizio(String ticket, String codPolo, String codBib) throws EJBException {

		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();
		List<TipoServizioVO> tipiServizioOut = new ArrayList<TipoServizioVO>();

		try {
			checkTicket(ticket);
			List<Tbl_tipo_servizio> tipiServizio = tipoServizioDAO.getListaTipiServizio(codPolo, codBib);
			Iterator<Tbl_tipo_servizio> iterator = tipiServizio.iterator();
			while (iterator.hasNext()) {
				TipoServizioVO tipoServizio = ServiziConversioneVO.daHibernateAWebTipoServizio(iterator.next());
				tipoServizio.setDescrizione(CodiciProvider.cercaDescrizioneCodice(tipoServizio.getCodiceTipoServizio(),
						CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
				tipiServizioOut.add(tipoServizio);

			}

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}

		return tipiServizioOut;
	}

	/**
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws RemoteException
	 * @generated
	 */
	public java.util.List getListaCodiciTipiServizio(String ticket, String codPolo, String codBib) throws EJBException {
		UtilitaDAO dao = new UtilitaDAO();
		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();
		List codiciTipiServizio = new ArrayList();

		List tipiServizio = null;
		try {
			checkTicket(ticket);
			tipiServizio = tipoServizioDAO.getListaTipiServizio(codPolo, codBib);

			Iterator iterator;
			if (tipiServizio != null && tipiServizio.size() > 0) {
				iterator = tipiServizio.iterator();
				while (iterator.hasNext()) {
					codiciTipiServizio.add(((Tbl_tipo_servizio) iterator.next()).getCod_tipo_serv());
				}
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return codiciTipiServizio;
	}

	public TipoServizioVO getTipoServizio(String ticket, String codPolo, String codBib, String codTipoServizio)
			throws EJBException {
		UtilitaDAO dao = new UtilitaDAO();
		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();

		TipoServizioVO tipoServizioVO = new TipoServizioVO();

		try {
			checkTicket(ticket);
			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServizio);
			if (tipoServizio != null) {
				tipoServizioVO = ServiziConversioneVO.daHibernateAWebTipoServizio(tipoServizio);
				tipoServizioVO.setCodBib(codBib);
				tipoServizioVO.setCodPolo(codPolo);
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return tipoServizioVO;
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket, String codPolo, String codBibUte,
			String codUtente, String codBib, Timestamp data, boolean remoto) throws EJBException {

		UtentiDAO dao = new UtentiDAO();
		UtilitaDAO utilDAO = new UtilitaDAO();
		List<ServizioBibliotecaVO> out = new ArrayList<ServizioBibliotecaVO>();

		try {
			checkTicket(ticket);
			List<Tbl_servizio> serviziAttivi = dao.getServiziAttivi(codPolo, codBibUte, codUtente, codBib, data);

			if (!ValidazioneDati.isFilled(serviziAttivi))
				return out;

			Tbl_parametri_biblioteca parBib = utilDAO.getParametriBiblioteca(codPolo, codBib);
			if (parBib == null)
				return out;

			// controllo che l'inserimento richiesta da utenti remoti
			// sia autorizzato a livello di bilioteca erogante
			boolean includiBib = !remoto
					|| (parBib.getAmmesso_inserimento_utente() == 'S' && parBib.getAnche_da_remoto() == 'S');

			for (Tbl_servizio s : serviziAttivi) {

				boolean includi = includiBib;
				// se non autorizzato a livello di biblioteca devo
				// controllare a livello di tipo servizio
				if (!includi) {
					Tbl_tipo_servizio tipoServ = s.getId_tipo_servizio();
					includi = (tipoServ.getIns_richieste_utente() == 'S' && tipoServ.getAnche_da_remoto() == 'S');
				}

				if (includi)
					out.add(ServiziConversioneVO.daHibernateAWebServizio(s));
			}

			if (ValidazioneDati.isFilled(out))
				out.add(0, new ServizioBibliotecaVO()); // riga vuota

			return out;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public boolean isUtenteAutorizzato(String ticket, String codPolo, String codBibUte, String codUtente, String codBib,
			String codTipoServ, String codServ, Timestamp data) throws EJBException {

		try {
			checkTicket(ticket);
			UtentiDAO dao = new UtentiDAO();

			Trl_utenti_biblioteca utenteBiblioteca = dao.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);
			if (utenteBiblioteca == null)
				return false;

			//check abilitazione globale
			if (!DateUtil.between(data, utenteBiblioteca.getData_inizio_aut(), utenteBiblioteca.getData_fine_aut()))
				return false;

			//check diritti
			return dao.isUtenteAutorizzato(codPolo, codBibUte, codUtente, codBib, codTipoServ, codServ, data);

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public boolean isUtenteSospeso(String ticket, String codPolo, String codBibUte, String codUtente, String codBib,
			String codTipoServ, String codServ, Timestamp data) throws EJBException {

		try {
			checkTicket(ticket);
			UtentiDAO dao = new UtentiDAO();

			Trl_utenti_biblioteca utenteBiblioteca = dao.getUtenteBiblioteca(codPolo, codBibUte, codUtente, codBib);
			if (utenteBiblioteca == null)
				return false;

			// check sospensione globale
			if (DateUtil.between(data, utenteBiblioteca.getData_inizio_sosp(),
					utenteBiblioteca.getData_fine_sosp()))
				return true;

			//check diritti
			return dao.isUtenteSospeso(codPolo, codBibUte, codUtente, codBib, codTipoServ, codServ, data);

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket, String codPolo, String codBib, String codFrui)
			throws EJBException {

		ServiziDAO dao = new ServiziDAO();
		List<ServizioBibliotecaVO> serviziAttiviVO = new ArrayList<ServizioBibliotecaVO>();

		List<Tbl_servizio> serviziAttivi;
		try {
			checkTicket(ticket);
			serviziAttivi = dao.getServiziAttivi(codPolo, codBib, codFrui);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
		if (serviziAttivi.size() > 0) {
			serviziAttiviVO.add(new ServizioBibliotecaVO());
			Iterator<Tbl_servizio> iterator = serviziAttivi.iterator();

			while (iterator.hasNext()) {
				serviziAttiviVO.add(ServiziConversioneVO.daHibernateAWebServizio(iterator.next()));
			}
		}

		return serviziAttiviVO;
	}

	public void scambioControlliIter(String ticket, int idTipoServizio, String codAttivita, short progressivo,
			TipoAggiornamentoIter tipoOp) throws EJBException {
		IterServizioDAO iterServizioDAO = new IterServizioDAO();
		try {
			checkTicket(ticket);
			iterServizioDAO.scambioControlliIter(idTipoServizio, codAttivita, progressivo, tipoOp);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public void scambioIter(String ticket, int id_tipo_servizio, int progressivo, TipoAggiornamentoIter tipoOp)
			throws EJBException {
		IterServizioDAO iterServizioDAO = new IterServizioDAO();
		try {
			checkTicket(ticket);
			iterServizioDAO.scambioIter(id_tipo_servizio, progressivo, tipoOp);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public UtenteBaseVO getUtente(String ticket, String codPolo, String codBibUte, String codUtente, String codBib)
			throws EJBException {
		UtentiDAO dao = new UtentiDAO();

		try {
			checkTicket(ticket);
			Tbl_utenti utente = dao.getUtente(codPolo, codBibUte, codUtente, codBib);
			if (utente != null)
				return ServiziConversioneVO.daHibernateAWebUtente(utente);
			else
				return null;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public UtenteBaseVO getUtente(String ticket, String codUtente) throws EJBException {
		UtentiDAO dao = new UtentiDAO();

		try {
			checkTicket(ticket);
			if (!ValidazioneDati.isFilled(codUtente))
				return null;

			Tbl_utenti utente = dao.getUtente(codUtente);
			if (utente == null) {
				// almaviva5_20160527 utente non trovato per userId, secondo tentativo per cod.fiscale
				String codFiscale = ValidazioneDati.trimOrEmpty(codUtente).toUpperCase();
				utente = dao.getUtenteByCodFiscale(codFiscale);
			}

			if (utente != null)
				return ServiziConversioneVO.daHibernateAWebUtente(utente);
			else
				return null;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	/**
	 *
	 * @param codPolo
	 * @param codBib
	 * @param codTipoServ
	 * @param progrIter
	 * @return Lista di istanze di Attivita. Rappresentano attività successive a
	 *         progrIter fino alla prima obbligatoria
	 * @throws DaoManagerException
	 * @throws ApplicationException
	 * @throws DataException
	 */
	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String ticket, String codPolo, String codBib,
			String codTipoServ, int progrIter, DatiRichiestaILLVO datiILL) throws EJBException {
		// Lista di AttivitaVO
		List<AttivitaServizioVO> listaAttivita = new ArrayList<AttivitaServizioVO>();

		try {
			checkTicket(ticket);
			IterServizioDAO iterServizioDao = new IterServizioDAO();
			// Leggo iter servizio associati al tipo servizio
			List<Tbl_iter_servizio> listaIterServizio = iterServizioDao.getListaIterServizio(codPolo, codBib,
					codTipoServ);

			if (!ValidazioneDati.isFilled(listaIterServizio))
				return listaAttivita;

			Tbl_iter_servizio iterCorrente = iterServizioDao.getIterServizio(codPolo, codBib, codTipoServ, progrIter);

			// Filtro la lista considerando i passi con progressivo maggiore
			// di progrIter fino al primo obbligatorio
			// La funzione che mi torna i passi li ordina per progressivo
			// iter ascendente

			CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(codTipoServ,
					CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));

			// includo l'attività "03" "Consegna documento al lettore" se siamo in presenza
			// di
			// un servizio la cui famiglia di servizi è "CO" (Cd_flg3)
			// e l'attività attuale è "05" "Collocazione presso punto deposito"
			// e l'attività "03" è una di quelle configurate per il servizio
			boolean includiAttivita03 = (ts.isConsultazione() && iterCorrente != null && StatoIterRichiesta
					.of(iterCorrente.getCod_attivita()) == StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO);

			for (Tbl_iter_servizio iter : listaIterServizio) {

				if ((iter.getProgr_iter() > progrIter) || (includiAttivita03 && StatoIterRichiesta
						.of(iter.getCod_attivita()) == StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
					IterServizioVO iterServizioVO = ConversioneHibernateVO.toWeb().iterServizio(iter);
					listaAttivita.add(getAttivita(ticket, codPolo, iterServizioVO));

					// almaviva5_20160215 se il servizio è ill devo caricare tutti i passi previsti che
					// saranno
					// filtrati in seguito dalla configurazione ILL.
					if (!ts.isILL() && iter.getObbl() == 'S') {
						if (includiAttivita03 && iter.getCod_attivita().equals("03")) {
							// mi fermo alla prima attività obbligatoria solo se
							// non sto recuperando la "03"
							continue;
						} else {
							// in caso contrario
							// mi fermo alla prima attività obbligatoria
							break;
						}
					}
				}
			}

			if (ts.isILL() && datiILL != null) {
				// servizio ILL
				ILLConfiguration2 conf = ILLConfiguration2.getInstance();
				String state = datiILL.getCurrentState();
				String servizioILL = datiILL.getServizio();
				RuoloBiblioteca ruoloBib = datiILL.getRuolo();
				// si integra l'iter locale con le attività ILL possibili a questo passo
				listaAttivita = conf.getListaAttivitaSuccessive(codBib, codTipoServ, servizioILL, ruoloBib, state,
						iterCorrente.getCod_attivita(), listaAttivita);
			}

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}

		return listaAttivita;
	}

	public AttivitaServizioVO getAttivita(String ticket, String codPolo, IterServizioVO iter) throws EJBException {
		AttivitaServizioVO attivita = new AttivitaServizioVO();

		if (iter != null) {
			attivita.setPassoIter(iter);
			try {
				checkTicket(ticket);
				String codAttivita = iter.getCodAttivita();
				boolean ill = StatoIterRichiesta.of(codAttivita).isILL();

				if (!ill) {
					iter.setDescrizione(
							CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_ATTIVITA_ITER, codAttivita));
					// Cerco i bibliotecari associati
					TipoServizioVO tipoSrv = this.getTipoServizio(ticket, codPolo, iter.getCodBib(),
							iter.getCodTipoServ());

					UtentiProfessionaliDAO upDAO = new UtentiProfessionaliDAO();
					List<Tbf_anagrafe_utenti_professionali> utenti = upDAO
							.getUtentiProfessionali(tipoSrv.getIdTipoServizio(), codAttivita);

					List<AnagrafeUtenteProfessionaleVO> bibliotecari = new ArrayList<AnagrafeUtenteProfessionaleVO>();

					if (ValidazioneDati.isFilled(utenti))
						for (Tbf_anagrafe_utenti_professionali aup : utenti)
							bibliotecari.add(ServiziConversioneVO.daHibernateAWebUtenteProfessionale(aup));

					attivita.setBibliotecari(bibliotecari);

					// Cerco i controlli associati
					attivita.setControlli(this.getControlloIter(ticket, tipoSrv.getIdTipoServizio(), codAttivita));
				}

			} catch (RemoteException e) {
				throw new EJBException(e);
			} catch (Exception e) {
				throw new EJBException(e);
			}
		}

		return attivita;
	}

	public PenalitaServizioVO getPenalitaServizio(String ticket, String codPolo, String codBib, String codTipoServizio,
			String codServizio) {
		PenalitaServizioVO penalitaServizioVO = null;
		ServiziDAO serviziDAO = new ServiziDAO();

		try {
			checkTicket(ticket);
			Tbl_penalita_servizio penalitaServizio = serviziDAO.getPenalitaServizio(codPolo, codBib, codTipoServizio,
					codServizio);
			if (penalitaServizio != null) {
				penalitaServizioVO = ServiziConversioneVO.daHibernateAWebPenalitaServizio(penalitaServizio);
			}
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return penalitaServizioVO;
	}

	public void sospendiDirittoUtente(String ticket, MovimentoVO mov, Date dataSospensione, BaseVO datiModifica)
			throws ApplicationException {
		UtentiDAO dao = new UtentiDAO();
		try {
			checkTicket(ticket);
			Trl_diritti_utente diritto = dao.getDirittoUtenteBiblioteca(mov.getCodPolo(), mov.getCodBibUte(),
					mov.getCodUte(), mov.getCodBibOperante(), mov.getCodTipoServ(), mov.getCodServ());

			if (diritto == null)
				throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);

			// imposto un flag per verificare se ci sono delle variazioni sulle date
			boolean flgVariazione = false;
			Timestamp now = DaoManager.now();

			Date dataInizioSosp = diritto.getData_inizio_sosp();
			if (dataInizioSosp == null || dataInizioSosp.after(now))
				// se la data di inizio sospensione presente sul diritto
				// dell'utente esaminato è null o maggiore di quella attuale
				// modifico la data di inizio sospensione con la data attuale
				diritto.setData_inizio_sosp(now);
			flgVariazione = true;

			Date dataFineSosp = diritto.getData_fine_sosp();
			if (dataFineSosp == null || dataFineSosp.before(dataSospensione))
				// se la data di fine sospensione presente sul diritto
				// dell'utente esaminato è null o minore di quella
				// della sospensione calcolata per la penalità
				// modifico la data di fine sospensione con quella calcolata
				diritto.setData_fine_sosp(dataSospensione);
			flgVariazione = true;

			if (flgVariazione) {
				// se è stata effettuata una delle precedenti impostazioni delle date
				// modifico il timbro dell'utente che ha effettuato la variazione
				diritto.setUte_var(datiModifica.getUteVar());
				dao.getCurrentSession().update(diritto);
			}

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public boolean esistonoPrenotazioni(String ticket, UtenteBaseVO utenteBaseVO, MovimentoVO anaMov, Timestamp data)
			throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			if (anaMov.isRichiestaSuInventario())
				return richiesteDAO.esistonoPrenotazioni(anaMov.getCodPolo(), anaMov.getCodBibOperante(),
						utenteBaseVO.getCodUtente(), utenteBaseVO.getCodBib(), anaMov.getCodBibInv(),
						anaMov.getCodSerieInv(), Integer.parseInt(anaMov.getCodInvenInv()), 0, 0);
			else
				return richiesteDAO.esistonoPrenotazioni(anaMov.getCodPolo(), anaMov.getCodBibOperante(),
						utenteBaseVO.getCodUtente(), utenteBaseVO.getCodBib(), anaMov.getCodBibDocLett(),
						anaMov.getTipoDocLett(), Long.parseLong(anaMov.getCodDocLet()),
						Short.parseShort(anaMov.getProgrEsempDocLet()), 0, 0);
		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	public boolean esistonoPrenotazioni(String ticket, MovimentoVO movimento, Timestamp data)
			throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);

			int annata = getAnnataPeriodico(movimento);

			if (movimento.isRichiestaSuInventario())
				return richiesteDAO.esistonoPrenotazioni(movimento.getCodPolo(), movimento.getCodBibInv(),
						movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()), data, annata);
			else
				return richiesteDAO.esistonoPrenotazioni(movimento.getCodPolo(), movimento.getCodBibDocLett(),
						movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
						Short.parseShort(movimento.getProgrEsempDocLet()), data, annata);
		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	public int getNumeroPrenotazioni(String ticket, MovimentoVO movimento) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		List richieste = null;

		try {
			checkTicket(ticket);
			int annata = getAnnataPeriodico(movimento);

			if (movimento.isRichiestaSuInventario())
				return richiesteDAO.getNumeroPrenotazioni(movimento.getCodPolo(), movimento.getCodBibInv(),
						movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()), annata);
			else
				return richiesteDAO.getNumeroPrenotazioni(movimento.getCodPolo(), movimento.getCodBibDocLett(),
						movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
						Short.parseShort(movimento.getProgrEsempDocLet()), annata);
		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	public int getNumeroPrenotazioni(String ticket) throws ApplicationException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			return richiesteDAO.getNumeroPrenotazioni();
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public List<MovimentoListaVO> getPrenotazioni(String ticket, MovimentoVO filtroMov, String codBibDest,
			Locale locale, String tipoOrd) throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		List prenotazioniVO = new ArrayList<MovimentoListaVO>();
		String ordinamento = "";

		try {
			checkTicket(ticket);
			List<Vl_richiesta_servizio> prenotazioni = null;
			if (filtroMov != null) {
				// Determino il tipo di ordinamento
				String tipoOrdinamento = filtroMov.getTipoOrdinamento();
				if (ValidazioneDati.isFilled(tipoOrdinamento)) {
					TB_CODICI tb_codici = CodiciProvider.cercaCodice(tipoOrdinamento,
							CodiciType.CODICE_ORDINAMENTO_LISTA_MOVIMENTI, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (tb_codici != null)
						ordinamento = tb_codici.getDs_cdsbn_ulteriore();
				}
				if (filtroMov.isRichiestaSuInventario()) {
					prenotazioni = richiesteDAO.getListaPrenotazioniPerInventario(filtroMov.getCodPolo(),
							filtroMov.getCodBibInv(), filtroMov.getCodSerieInv(),
							Integer.parseInt(filtroMov.getCodInvenInv()), ordinamento);
				} else if (filtroMov.isRichiestaSuInventario()) {
					prenotazioni = richiesteDAO.getListaPrenotazioniPerSegnatura(filtroMov.getCodPolo(),
							filtroMov.getCodBibDocLett(), filtroMov.getTipoDocLett(),
							Long.parseLong(filtroMov.getCodDocLet()), Short.parseShort(filtroMov.getProgrEsempDocLet()),
							ordinamento);
				} else {
					// filtro su tipo servizio / data inizio prevista
					Timestamp dataInizioPrev = filtroMov.getDataInizioPrev();
					dataInizioPrev = dataInizioPrev == null ? null
							: new Timestamp(DateUtil.removeTime(dataInizioPrev).getTime());
					prenotazioni = richiesteDAO.getListaPrenotazioni(codBibDest, filtroMov.getCodTipoServ(),
							dataInizioPrev);
				}
			} else
				prenotazioni = richiesteDAO.getListaPrenotazioni(codBibDest, ordinamento);

			if (ValidazioneDati.isFilled(prenotazioni)) {

				if (filtroMov == null) {
					filtroMov = new MovimentoVO();
					filtroMov.setCodPolo(prenotazioni.get(0).getId_servizio().getId_tipo_servizio().getCd_bib()
							.getCd_polo().getCd_polo());
					filtroMov.setCodBibOperante(codBibDest);
				}

				int progr = 0;
				for (Vl_richiesta_servizio richiesta : prenotazioni) {

					// MovimentoListaVO movimento =
					// ServiziConversioneVO.daHibernateAWebMovimentoLista(richiesta, locale, now);
					MovimentoListaVO movimento = ConversioneHibernateVO.toWeb().movimentoLista(richiesta);
					// decodificaCodiciMovimento(movimento, filtroMov.getCodPolo(),
					// filtroMov.getCodBibOperante(), locale, richiesta);
					movimento.setProgr(++progr);
					prenotazioniVO.add(movimento);
				}
			}
			// almaviva5_20100119
			ordinaListaMovimenti(prenotazioniVO, tipoOrd);
			return prenotazioniVO;

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (RemoteException e) {
			throw new ApplicationException(e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List getProroghe(String ticket, MovimentoVO filtroMov, String codBibDest, Locale locale, String tipoOrd)
			throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		List prorogheVO = new ArrayList<MovimentoListaVO>();
		String ordinamento = "";

		try {
			checkTicket(ticket);
			List<Vl_richiesta_servizio> proroghe = richiesteDAO.getListaProroghe(codBibDest, ordinamento);

			if (ValidazioneDati.isFilled(proroghe)) {

				filtroMov = new MovimentoVO();
				filtroMov.setCodPolo(proroghe.get(0).getCd_polo());
				filtroMov.setCodBibOperante(codBibDest);

				int progr = 0;
				for (Vl_richiesta_servizio richiesta : proroghe) {
					// MovimentoListaVO movimento =
					// ServiziConversioneVO.daHibernateAWebMovimentoLista(richiesta, locale, now);
					MovimentoListaVO movimento = ConversioneHibernateVO.toWeb().movimentoLista(richiesta);
					movimento.setProgr(++progr);
					// decodificaCodiciMovimento(movimento, filtroMov.getCodPolo(),
					// filtroMov.getCodBibOperante(), locale, richiesta);
					prorogheVO.add(movimento);
				}
			}

			// almaviva5_20100119
			ordinaListaMovimenti(prorogheVO, tipoOrd);
			return prorogheVO;

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List getGiacenze(String ticket, MovimentoVO filtroMov, String codBibDest, Locale locale, String tipoOrd)
			throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		IterServizioDAO iterServizioDAO = new IterServizioDAO();
		List giacenzeVO = new ArrayList<MovimentoListaVO>();
		String ordinamento = "";

		try {
			checkTicket(ticket);
			List<Vl_richiesta_servizio> movimentiAttivi = richiesteDAO.getListaMovimentiAttiviPerGiacenze(codBibDest);

			if (!ValidazioneDati.isFilled(movimentiAttivi))
				return giacenzeVO;

			filtroMov = new MovimentoVO();
			filtroMov.setCodPolo(movimentiAttivi.get(0).getCd_polo());
			filtroMov.setCodBibOperante(codBibDest);

			for (Vl_richiesta_servizio req : movimentiAttivi) {

				// almaviva5_20110222 #4246
				// controllo che le richieste della famiglia (flag3) riproduzione
				// non siano incluse nella lista
				String codTipoServ = req.getCod_tipo_serv();
				TB_CODICI cod = CodiciProvider.cercaCodice(codTipoServ, CodiciType.CODICE_TIPO_SERVIZIO,
						CodiciRicercaType.RICERCA_CODICE_SBN);

				if (cod == null)
					continue;

				// almaviva5_20110408 #4350
				String flg3 = cod.getCd_flg3();
				if (ValidazioneDati.equals(flg3, ServiziConstant.FAMIGLIA_SRV_RIPRODUZIONE))
					continue;

				// Nelle giacenze vengono riportati tutti i movimenti attivi
				// (risultato del precedente metodo getListaMovimentiAttivi)
				// con attività che precede la "Consegna del documento all'utente" cioè
				// con il progressivo dell'iter del movimento minore del
				// progressivo dell'iter (relativo a quel servizio) corrispondente
				// all'attività di "Consegna del documento all'utente"
				// e con la data di inizio prevista minore di quella corrente (prelazione
				// scaduta)
				if (ValidazioneDati.equals(flg3, ServiziConstant.FAMIGLIA_SRV_PRESTITO))
					if (req.getData_in_eff() != null)
						continue;

				// Nelle giacenze vengono riportati tutti i movimenti attivi
				// (risultato del precedente metodo getListaMovimentiAttivi)
				// con attività uguale alla "Collocazione presso punto deposito"
				// e con la data di fine prevista minore di quella corrente (durata del deposito
				// scaduta)
				if (ValidazioneDati.equals(flg3, ServiziConstant.FAMIGLIA_SRV_CONSULTAZIONE))
					if (req.getData_in_eff() != null && !ValidazioneDati.equals(req.getCod_attivita(),
							StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO.getISOCode()))
						continue;

				// MovimentoListaVO movimento =
				// ServiziConversioneVO.daHibernateAWebMovimentoLista(req, locale, now);
				MovimentoListaVO movimento = ConversioneHibernateVO.toWeb().movimentoLista(req);
				// decodificaCodiciMovimento(movimento, filtroMov.getCodPolo(),
				// filtroMov.getCodBibOperante(), locale, req);
				giacenzeVO.add(movimento);
			}

			// almaviva5_20100119
			ordinaListaMovimenti(giacenzeVO, tipoOrd);
			return giacenzeVO;

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public boolean esistePrenotazionePerUtente(String ticket, MovimentoVO movimento) throws ApplicationException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		try {
			checkTicket(ticket);
			// almaviva5_20100518 richiesta su periodici
			int annata = getAnnataPeriodico(movimento);

			if (movimento.isRichiestaSuInventario())
				return richiesteDAO.esistonoPrenotazioni(movimento.getCodPolo(), movimento.getCodBibOperante(),
						movimento.getCodUte(), movimento.getCodBibUte(), movimento.getCodBibInv(),
						movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()),
						movimento.getIdRichiesta(), annata);
			else
				return richiesteDAO.esistonoPrenotazioni(movimento.getCodPolo(), movimento.getCodBibOperante(),
						movimento.getCodUte(), movimento.getCodBibUte(), movimento.getCodBibDocLett(),
						movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
						Short.parseShort(movimento.getProgrEsempDocLet()), movimento.getIdRichiesta(), annata);

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	private int getAnnataPeriodico(MovimentoVO movimento) {
		// almaviva5_20120518 #5004
		if (movimento == null || !movimento.isPeriodico())
			return 0;

		String annata = movimento.getAnnoPeriodico();
		if (!ValidazioneDati.strIsNumeric(annata))
			return 0;

		return Integer.valueOf(annata);
	}

	public void aggiornaSegnatura(String ticket, RangeSegnatureVO segnaturaVO, boolean isNew)
			throws it.iccu.sbn.ejb.exception.IntervalloSegnaturaNonValidoException, AlreadyExistsException,
			ApplicationException {

		SegnatureDAO segnatureDao = new SegnatureDAO();

		Tbl_disponibilita_precatalogati segnatura = ServiziConversioneVO.daWebAHibernateSegnatura(segnaturaVO);

		try {
			checkTicket(ticket);
			if (isNew) {
				segnatureDao.insert(segnatura);
				segnaturaVO.setId(segnatura.getId_disponibilita_precatalogati());
			} else {
				segnatureDao.update(segnatura);
			}
		} catch (it.iccu.sbn.persistence.dao.exception.IntervalloSegnaturaNonValidoException e) {
			throw new it.iccu.sbn.ejb.exception.IntervalloSegnaturaNonValidoException(e);
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public void cancellaSegnature(String ticket, Integer[] id, String uteVar) throws ApplicationException {
		SegnatureDAO segnatureDao = new SegnatureDAO();

		try {
			checkTicket(ticket);
			segnatureDao.delete(id, uteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public List caricaSegnature(String ticket, String codPolo, String codBib, RangeSegnatureVO segnaturaVO)
			throws ApplicationException {

		SegnatureDAO dao = new SegnatureDAO();

		List listaOut = new ArrayList();

		Tbl_disponibilita_precatalogati segnatura = ServiziConversioneVO.daWebAHibernateSegnatura(segnaturaVO);

		List<Tbl_disponibilita_precatalogati> lista;
		try {
			checkTicket(ticket);
			String ordinamento = "";
			TB_CODICI tb_codici = null;
			try {
				tb_codici = CodiciProvider.cercaCodice(segnaturaVO.getTipoOrdinamento(),
						CodiciType.CODICE_ORDINAMENTO_LISTA_SEGNATURE, CodiciRicercaType.RICERCA_CODICE_SBN);
			} catch (RemoteException e) {
				throw new ApplicationException(e);
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
			if (tb_codici != null)
				ordinamento = tb_codici.getDs_cdsbn_ulteriore();

			lista = dao.select(codPolo, codBib, segnatura, ordinamento);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

		if (ValidazioneDati.isFilled(lista)) {
			int progr = 0;
			Iterator iterator = lista.iterator();
			while (iterator.hasNext()) {
				RangeSegnatureVO rs = ServiziConversioneVO
						.daHibernateAWebSegnatura((Tbl_disponibilita_precatalogati) iterator.next());
				rs.setProgr(++progr);

				try {
					rs.setFruizione(CodiciProvider.cercaDescrizioneCodice(rs.getCodFruizione(),
							CodiciType.CODICE_CATEGORIA_FRUIZIONE, CodiciRicercaType.RICERCA_CODICE_SBN));

					rs.setIndisponibile(CodiciProvider.cercaDescrizioneCodice(rs.getCodIndisp(),
							CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN));

				} catch (RemoteException e) {
					throw new ApplicationException(e);
				} catch (Exception e) {
					throw new ApplicationException(e);
				}

				listaOut.add(rs);
			}
		}

		return listaOut;
	}

	public List<RelazioniVO> caricaRelazioni(String ticket, String codPolo, String codBib, String codRelazione)
			throws ApplicationException {

		TabelleRelazioneDAO relazioniDao = new TabelleRelazioneDAO();
		List<RelazioniVO> relazioniOut = new ArrayList<RelazioniVO>();

		try {
			checkTicket(ticket);
			List relazioniHib = relazioniDao.select(codPolo, codBib, codRelazione);

			if (relazioniHib != null && relazioniHib.size() > 0) {
				Iterator<Trl_relazioni_servizi> iterator = relazioniHib.iterator();

				CodiciType codType = CodiciType.fromString(codRelazione);

				String descIdCodici = "";
				String descIdRelazione = "";
				int count = 0;
				int IdTabellaRelazione = 0;

				TipoServizioDAO serviziDAO = new TipoServizioDAO();
				SupportiBibliotecaDAO supportiDAO = new SupportiBibliotecaDAO();

				while (iterator.hasNext()) {
					Trl_relazioni_servizi relazione = iterator.next();
					RelazioniVO relazioneVO = ServiziConversioneVO.daHibernateAWebTabellaRelazioni(relazione);

					switch (codType) {
					case CODICE_TIPO_SERVIZIO_MOD_EROGAZIONE:
						descIdCodici = CodiciProvider.cercaDescrizioneCodice(relazioneVO.getIdTabellaCodici(),
								CodiciType.fromString(relazioneVO.getTabellaCodici()),
								CodiciRicercaType.RICERCA_CODICE_SBN);

						IdTabellaRelazione = relazioneVO.getIdTabellaRelazione();
						Tbl_tipo_servizio tipoServizio = serviziDAO.getTipoServizioById(IdTabellaRelazione);
						descIdRelazione = CodiciProvider.cercaDescrizioneCodice(tipoServizio.getCod_tipo_serv(),
								CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
						break;

					case CODICE_TIPO_SERVIZIO_CATEGORIA_FRUIZIONE:
						descIdCodici = CodiciProvider.cercaDescrizioneCodice(relazioneVO.getIdTabellaCodici(),
								CodiciType.fromString(relazioneVO.getTabellaCodici()),
								CodiciRicercaType.RICERCA_CODICE_SBN);

						IdTabellaRelazione = relazioneVO.getIdTabellaRelazione();
						tipoServizio = serviziDAO.getTipoServizioById(IdTabellaRelazione);
						descIdRelazione = CodiciProvider.cercaDescrizioneCodice(tipoServizio.getCod_tipo_serv(),
								CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN);
						break;

					case CODICE_TIPO_SUPPORTO_MODALITA_EROGAZIONE:
						descIdCodici = CodiciProvider.cercaDescrizioneCodice(relazioneVO.getIdTabellaCodici(),
								CodiciType.fromString(relazioneVO.getTabellaCodici()),
								CodiciRicercaType.RICERCA_CODICE_SBN);

						IdTabellaRelazione = relazioneVO.getIdTabellaRelazione();
						Tbl_supporti_biblioteca supporto = supportiDAO.getSupportoBibliotecaById(IdTabellaRelazione);
						descIdRelazione = CodiciProvider.cercaDescrizioneCodice(supporto.getCod_supporto(),
								CodiciType.CODICE_SUPPORTO_COPIA, CodiciRicercaType.RICERCA_CODICE_SBN);
						break;

					default:
						break;
					}

					relazioneVO.setDescIdTabellaCodici(descIdCodici);
					relazioneVO.setDescIdTabellaRelazione(descIdRelazione);
					relazioneVO.setProgr(++count);
					relazioniOut.add(relazioneVO);
				}
			}
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return relazioniOut;
	}

	public void cancellaRelazioni(String ticket, Integer[] id, String uteVar) throws ApplicationException {
		TabelleRelazioneDAO dao = new TabelleRelazioneDAO();

		try {
			checkTicket(ticket);
			dao.delete(id, uteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public void riattivaRelazioni(String ticket, Integer[] id, String uteVar) throws ApplicationException {
		TabelleRelazioneDAO dao = new TabelleRelazioneDAO();

		try {
			checkTicket(ticket);
			dao.restore(id, uteVar);
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public void aggiornaRelazione(String ticket, RelazioniVO relazioneVO)
			throws ApplicationException, AlreadyExistsException {

		TabelleRelazioneDAO relazioniDao = new TabelleRelazioneDAO();

		try {
			checkTicket(ticket);
			Trl_relazioni_servizi relazione = ServiziConversioneVO.daWebAHibernateTabellaRelazioni(relazioneVO);

			if (relazione.getId() > 0) {
				// aggiornamento
				relazioniDao.update(relazione);
			} else {
				// nuovo inserimento
				relazioniDao.insert(relazione);
				relazioneVO.setId(relazione.getId());
			}
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public boolean checkRelazioneTipoServizioCodFruizione(String ticket, String codPolo, String codBib,
			String codTipoServ, String codFrui) throws ApplicationException, AlreadyExistsException {

		TabelleRelazioneDAO relazioniDao = new TabelleRelazioneDAO();
		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();

		try {
			checkTicket(ticket);

			Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
			if (tipoServizio == null)
				return false;

			// viene utilizzata non più la tabella relazioni "Trl_relazioni_servizi" (vedi
			// le successive istruzioni commentate)
			// ma la tabella "tb_codici" relativamente al codice TSFC

			/*
			 * List<Trl_relazioni_servizi> relazioniHib = relazioniDao.select(codPolo,
			 * codBib, CodiciType.CODICE_TIPO_SERVIZIO_CATEGORIA_FRUIZIONE.getTp_Tabella());
			 * if (!ValidazioneDati.isFilled(relazioniHib) ) return false; codFrui =
			 * codFrui.trim(); for (Trl_relazioni_servizi relazione : relazioniHib) { if (
			 * (relazione.getFl_canc() != 'S') &&
			 * (relazione.getId_relazione_tabella_di_relazione() ==
			 * tipoServizio.getId_tipo_servizio()) &&
			 * (relazione.getId_relazione_tb_codici().trim().equals(codFrui)) ) return true;
			 * }
			 */

			String cod_tipo_serv = tipoServizio.getCod_tipo_serv().trim();
			codFrui = codFrui.trim();
			List<TB_CODICI> listaRelazioni;
			try {
				listaRelazioni = CodiciProvider.getCodiciCross(CodiciType.CODICE_CAT_FRUIZIONE_TIPO_SERVIZIO, codFrui,
						true);
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
			if (ValidazioneDati.isFilled(listaRelazioni))
				for (TB_CODICI cod : listaRelazioni)
					if (cod_tipo_serv.equals(ValidazioneDati.trimOrEmpty(cod.getCd_tabella())))
						return true;

			return false;

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}
	}

	public DocumentoNonSbnVO getCategoriaFruizioneSegnatura(String ticket, MovimentoVO movimento)
			throws ApplicationException, AlreadyExistsException {

		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO documentiDAO = new Tbl_documenti_lettoriDAO();

			// 1. cerco cat. fruizione sul record del documento lettore
			String codPolo = movimento.getCodPolo();
			String codBib = movimento.getCodBibDocLett();
			Tbl_documenti_lettori doc = documentiDAO.getDocumentoLettore(codPolo, codBib, movimento.getTipoDocLett(),
					Long.valueOf(movimento.getCodDocLet()));
			if (doc == null)
				throw new ApplicationException(SbnErrorTypes.SRV_DOCUMENTO_LETTORE_NON_TROVATO);

			DocumentoNonSbnVO baseDoc = ConversioneHibernateVO.toWeb().documentoNonSbn(doc, null);
			if (ValidazioneDati.isFilled(doc.getCd_catfrui()) || ValidazioneDati.isFilled(doc.getCd_no_disp()))
				return baseDoc;

			//almaviva5_20191108 #7178 solo per richieste ill come fornitore:
			//se non impostato si carica la categoria di fruizione dalla configurazione
			if (movimento.isFornitriceRichiestaILL()) {
				String defaultCodFrui = CommonConfiguration.getProperty(Configuration.ILL_SBN_DEFAULT_COD_FRUIZIONE);
				if (ValidazioneDati.isFilled(defaultCodFrui)) {
					baseDoc.setCodFruizione(defaultCodFrui);
					return baseDoc;	
				}
			}

			// cerco la cat. frui mancante
			return internalGetCategoriaFruizioneSegnatura(baseDoc, codPolo, codBib, doc.getOrd_segnatura());

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e);
		}
	}

	public DocumentoNonSbnVO getCategoriaFruizioneSegnatura(String ticket, String codPolo, String codBib,
			String ordSegn) throws ApplicationException {
		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
			Tbl_documenti_lettori docLettore = dao.select(codPolo, codBib, ordSegn);

			// 1. cerco il doc. esatto e controllo se ha valorizzato cod.frui o cod.no.disp.
			DocumentoNonSbnVO doc = null;
			if (docLettore != null) {
				List<EsemplareDocumentoNonSbnVO> esemplari = internalGetListaEsemplariDocumentiNonSbn(
						docLettore.getId_documenti_lettore());
				doc = ConversioneHibernateVO.toWeb().documentoNonSbn(docLettore, esemplari);
				if (ValidazioneDati.isFilled(docLettore.getCd_catfrui())
						|| ValidazioneDati.isFilled(docLettore.getCd_no_disp()))
					return doc;
			}

			// cerco la cat. frui mancante
			return internalGetCategoriaFruizioneSegnatura(doc, codPolo, codBib, ordSegn);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (ApplicationException e) {
			throw e;
		}
	}

	private DocumentoNonSbnVO internalGetCategoriaFruizioneSegnatura(DocumentoNonSbnVO baseDoc, String codPolo,
			String codBib, String ordSegn) throws ApplicationException {

		try {
			DocumentoNonSbnVO fakeDoc = baseDoc != null ? baseDoc : new DocumentoNonSbnVO();
			if (fakeDoc.isNuovo()) {
				fakeDoc.setCodPolo(codPolo);
				fakeDoc.setCodBib(codBib);
				fakeDoc.setTipo_doc_lett('P');
				fakeDoc.setFonte('B');
			}

			UtilitaDAO utilitaDAO = new UtilitaDAO();
			// 2. Carico la configurazione di biblioteca
			Tbl_parametri_biblioteca parametriBiblioteca = utilitaDAO.getParametriBiblioteca(codPolo, codBib);
			if (parametriBiblioteca == null)
				throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

			ParametriBibliotecaVO params = ConversioneHibernateVO.toWeb().parametriBiblioteca(parametriBiblioteca);
			String codFruizioneBib = params.getCodFruizione();

			// 3. La biblioteca gestisce cod_frui per range di segnature
			SegnatureDAO segnatureDAO = new SegnatureDAO();
			Tbl_disponibilita_precatalogati range = segnatureDAO.selectNarrowestRange(codPolo, codBib, ordSegn);
			if (range != null) {
				// ho trovato qualcosa sul range
				fakeDoc.setCodFruizione(range.getCod_frui());
				fakeDoc.setCodNoDisp(range.getCod_no_disp());
				return fakeDoc;
			}

			// non ho trovato nulla sui range di segnature, uso default biblioteca
			if (!ValidazioneDati.isFilled(codFruizioneBib))
				throw new ApplicationException(SbnErrorTypes.SRV_CATEGORIA_FRUIZIONE_DEFAULT_NON_IMPOSTATA);

			fakeDoc.setCodFruizione(codFruizioneBib);

			return fakeDoc;

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public Timestamp calcolaRedisponibilitaAlServizio(String ticket, String codPolo, String codBib, String codTipoServ,
			Timestamp dataInizioPrevista) throws ApplicationException {

		try {
			checkTicket(ticket);
			return internalCalcolaRedisponibilita(codPolo, codBib, codTipoServ, dataInizioPrevista);

		} catch (DaoManagerException e) {
			throw new ApplicationException(e);
		}

	}

	private Timestamp internalCalcolaRedisponibilita(String codPolo, String codBib, String codTipoServ,
			Timestamp dataInizioPrevista) throws DaoManagerException, ApplicationException {
		TipoServizioDAO servizioDAO = new TipoServizioDAO();

		Tbl_tipo_servizio tipoServizio = servizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
		if (tipoServizio == null)
			throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO, codTipoServ);

		short ore_ridis = tipoServizio.getOre_ridis();
		if (ore_ridis == 0)
			// il documento è subito redisponibile
			return dataInizioPrevista;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataInizioPrevista);

		if (ore_ridis > 12) {
			// redisponibilità calcolata in giorni
			calendar.add(Calendar.HOUR_OF_DAY, ore_ridis);
			// per la redisponibilità in giorni non considero le ore
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			return new Timestamp(calendar.getTimeInMillis());
		}

		// redisp in ore
		calendar.add(Calendar.HOUR_OF_DAY, ore_ridis);
		return new Timestamp(calendar.getTimeInMillis());
	}

	public ControlloDisponibilitaVO controlloDisponibilita(String ticket, ControlloDisponibilitaVO out)
			throws ApplicationException {

		try {
			checkTicket(ticket);

			RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
			ServiziDAO serviziDAO = new ServiziDAO();

			MovimentoVO movimento = out.getMovimento();

			Tbl_richiesta_servizio reqAttiva = internalGetMovimentoAttivo(movimento);

			if (reqAttiva == null) { // non esiste alcuna richiesta

				// controllo se il documento é già disponibile o devo prenotare
				MovimentoVO lastMov = internalGetUltimoMovimento(movimento);

				if (lastMov == null)
					// primo movimento su questo tipo servizio, doc. disponibile
					return out.setResult(ControlloAttivitaServizioResult.OK);

				// devo verificare che sia già disponibile dal mov. concluso
				Timestamp dataRedisponibilita = internalCalcolaRedisponibilita(movimento.getCodPolo(),
						movimento.getCodBibOperante(), lastMov.getCodTipoServ(), lastMov.getDataFineEff());

				if (movimento.getDataInizioPrev().after(dataRedisponibilita)) // disponibile
					return out.setResult(ControlloAttivitaServizioResult.OK);

				// doc. in ricollocazione, inserisco la richiesta ma imposto la prima data utile
				// per il ritiro
				out.setDataPrenotazione(dataRedisponibilita);

				// almaviva5_20150603 descrizione non disponibilità
				out.setMotivo(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO,
						lastMov.getCodTipoServ()));

				return out.setResult(ControlloAttivitaServizioResult.OK_NON_ANCORA_DISPONIBILE);

			}

			// movAttivo != null (esiste un movimento in corso)
			MovimentoVO movAttivo = ServiziConversioneVO.daHibernateAWebMovimento(reqAttiva, Locale.getDefault());
			out.setMovimentoAttivo(movAttivo);

			// fine prevista/effettiva sul mov. attivo
			// se la richiesta é conclusa (='H') il documento é stato riconsegnato
			// (data_fine_eff != null)
			Timestamp dataFineMovAttivo = (reqAttiva.getCod_stato_mov() == 'A')
					&& (reqAttiva.getCod_stato_rich() != 'H') ? reqAttiva.getData_fine_prev()
							: reqAttiva.getData_fine_eff(); // fine eff. se stato mov == 'S' o stato ric == 'H'

			// calcolo la prima data utile a partire dalla data prevista per la chiusura del
			// mov.
			Timestamp dataRedisp = internalCalcolaRedisponibilita(movimento.getCodPolo(), movimento.getCodBibOperante(),
					movAttivo.getCodTipoServ(), dataFineMovAttivo);
			out.setDataPrenotazione(dataRedisp);
			// almaviva5_20150603 descrizione non disponibilità
			out.setMotivo(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO,
					movAttivo.getCodTipoServ()));

			// se movimento in conclusione (stato_mov='S') non blocco l'inserimento
			Timestamp now = DaoManager.now();
			if (reqAttiva.getCod_stato_mov() == 'S')
				if (dataRedisp.before(now))
					// già disponibile
					return out.setResult(ControlloAttivitaServizioResult.OK);
				else
					// in ricollocazione
					return out.setResult(ControlloAttivitaServizioResult.OK_NON_ANCORA_DISPONIBILE);

			// sto provando a inoltrare una prenotazione con mov. attivo, esco con errore
			if (movimento.isPrenotazione())
				return out.setResult(ControlloAttivitaServizioResult.ERRORE_INOLTRO_PRENOTAZIONE_IMPOSSIBILE);

			// il bibliotecario sta forzando l'inoltro di una prenotazione di periodico
			// permetto l'operazione saltando i controlli sulla prenotabilità del servizio
			if (movimento.isPeriodico() && out.isInoltroPrenotazione())
				if (dataRedisp.before(now))
					// già disponibile
					return out.setResult(ControlloAttivitaServizioResult.OK);
				else
					// in ricollocazione
					return out.setResult(ControlloAttivitaServizioResult.OK_NON_ANCORA_DISPONIBILE);

			// numero prenotazioni già presenti sul documento
			int numPrenotDoc = internalGetNumeroPrenotazioni(movimento);
			out.setPrenotazioniPendenti(numPrenotDoc);

			// almaviva5_20120622
			if (out.isNoPrenotazione())
				return out;

			// controllo se posso prenotare per il servizio scelto
			Tbl_servizio servizioBib = serviziDAO.getServizioBiblioteca(movimento.getCodPolo(),
					movimento.getCodBibOperante(), movimento.getCodTipoServ(), movimento.getCodServ());

			char codaRichiesteTipoServ = servizioBib.getId_tipo_servizio().getCoda_richieste();
			if (codaRichiesteTipoServ == 'N') // non prenotabile
				return out.setResult(ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE_NO_PRENOT);

			// questo controllo non si deve fare qui
			// ma, se configurato come controllo dell'iter
			// (numero prenotazioni per documento)
			// sarà effettuato successivamente
			// estraggo dai parametri di biblioteca il numero massimo di prenotazioni
			// ammesse per uno stesso documento
			/*
			 * UtilitaDAO utilitaDAO = new UtilitaDAO(); Tbl_parametri_biblioteca
			 * parametriBiblioteca =
			 * utilitaDAO.getParametriBiblioteca(movimento.getCodPolo(),
			 * movimento.getCodBibOperante() ); if (parametriBiblioteca == null) throw new
			 * ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);
			 * ParametriBibliotecaVO params =
			 * ConversioneHibernateVO.toWeb().parametriBiblioteca(parametriBiblioteca);
			 * short numMaxPrenotazioniDoc = params.getNumeroPrenotazioni(); if
			 * (numMaxPrenotazioniDoc == 0) { // non sono ammesse prenotazioni sul documento
			 * return out.setResult(ControlloAttivitaServizioResult.
			 * ERRORE_NO_PRENOTAZIONI_DOCUMENTO); } else { if (numPrenotDoc >=
			 * numMaxPrenotazioniDoc) // troppe prenotazioni sul documento return
			 * out.setResult(ControlloAttivitaServizioResult.
			 * ERRORE_TROPPE_PRENOTAZIONI_DOCUMENTO); }
			 */

			// é possibile prenotare
			return out.setResult(ControlloAttivitaServizioResult.RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO);

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			// almaviva5_20150603
			out.setDisponibile(out.getDataPrenotazione() == null);
		}

	}

	public MovimentoListaVO getMovimentoListaVO(String ticket, MovimentoVO mov, Locale locale)
			throws ApplicationException {

		try {
			checkTicket(ticket);

			if (mov == null)
				return null;

			Tbl_richiesta_servizio movAttivo = internalGetMovimento(mov); // mov per ID
			if (movAttivo == null) // non esiste il movimento
				return null;

			Timestamp now = DaoManager.now();
			MovimentoListaVO movimento_out = ServiziConversioneVO.daHibernateAWebMovimentoLista(movAttivo, locale, now);

			String codPolo = ValidazioneDati.isFilled(mov.getCodPolo()) ? mov.getCodPolo()
					: DaoManager.codPoloFromTicket(ticket);
			String codBibOperante = ValidazioneDati.isFilled(mov.getCodBibOperante()) ? mov.getCodBibOperante()
					: DaoManager.codBibFromTicket(ticket);
			this.decodificaCodiciMovimento(movimento_out, codPolo, codBibOperante, locale, movAttivo);
			movimento_out.setProgr(mov.getProgr());

			impostaFlagConsegnato(mov, movimento_out, movAttivo, new THashMap<Integer, Tbl_iter_servizio>());

			movimento_out.setCodPolo(codPolo);
			movimento_out.setCodBibOperante(codBibOperante);

			if (movimento_out.isWithPrenotazionePosto())
				movimento_out
						.setPrenotazionePosto(new PrenotazionePostoDecorator(movimento_out.getPrenotazionePosto()));

			// almaviva5_20160212 servizi ill;
			DatiRichiestaILLVO dati = movimento_out.getDatiILL();
			if (movimento_out.isRichiestaILL() && dati.isRichiedente()) {
				List<BibliotecaVO> biblioteche = dati.getBibliotecheFornitrici();
				if (!ValidazioneDati.isFilled(biblioteche)) {
					// dati bib.fornitrice
					BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
					richiesta.setCodiceAna(dati.getResponderId());
					biblioteche = DomainEJBFactory.getInstance().getBiblioteca().cercaBiblioteche(ticket, richiesta);
					dati.setBibliotecheFornitrici(biblioteche);
				}
			}

			return movimento_out;

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		} catch (RemoteException e) {
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	private void impostaFlagConsegnato(MovimentoVO filtroMov, MovimentoListaVO mov, Tbl_richiesta_servizio req,
			Map<Integer, Tbl_iter_servizio> cache) throws Exception {
		// imposto nel campo consegnato (booleano) true
		// se il progressivo dell'iter del movimento è maggiore o uguale al
		// progressivo dell'iter (relativo a quel servizio) corrispondente
		// all'attività di "Consegna del documento all'utente"

		Tbl_tipo_servizio tipoSrv = req.getId_servizio().getId_tipo_servizio();
		boolean consegnato = false;
		// almaviva5_20110208 in riproduzione non é presente attivita 03
		// controllo data inizio effettiva
		TB_CODICI cod = CodiciProvider.cercaCodice(tipoSrv.getCod_tipo_serv(), CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		if (cod != null && ValidazioneDati.equals(cod.getCd_flg3(), ServiziConstant.FAMIGLIA_SRV_RIPRODUZIONE)) {
			Timestamp dtInizioEff = mov.getDataInizioEff();
			consegnato = (dtInizioEff != null && dtInizioEff.before(DaoManager.now()));
		} else {
			int progrIterMov = Integer.valueOf(mov.getProgrIter());
			Tbl_iter_servizio iterServizio = cache.get(tipoSrv.getId_tipo_servizio());
			if (iterServizio == null) {
				IterServizioDAO iterDAO = new IterServizioDAO();
				iterServizio = iterDAO.getIterServizio(tipoSrv, "03");
				if (iterServizio != null)
					cache.put(tipoSrv.getId_tipo_servizio(), iterServizio);
			}

			consegnato = (iterServizio != null && progrIterMov >= iterServizio.getProgr_iter());
		}

		mov.setConsegnato(consegnato);
	}

	private MovimentoVO internalGetUltimoMovimento(MovimentoVO movimento)
			throws NumberFormatException, DaoManagerException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		int annata = getAnnataPeriodico(movimento);
		Tbl_richiesta_servizio req = null;

		if (movimento.isRichiestaSuInventario())
			req = richiesteDAO.getUltimoMovimento(movimento.getCodPolo(), movimento.getCodBibOperante(),
					movimento.getCodBibInv(), movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()),
					annata);
		else
			req = richiesteDAO.getUltimoMovimento(movimento.getCodPolo(), movimento.getCodBibOperante(),
					movimento.getCodBibDocLett(), movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
					Short.parseShort(movimento.getProgrEsempDocLet()), annata);

		return (req != null) ? ServiziConversioneVO.daHibernateAWebMovimento(req, Locale.getDefault()) : null;
	}

	public boolean esisteMovimentoAttivo(String ticket, MovimentoVO movimento) throws ApplicationException {

		checkTicket(ticket);
		try {
			RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

			int annata = getAnnataPeriodico(movimento);

			if (movimento.isRichiestaSuInventario())
				return richiesteDAO.esistonoMovimentiAttivi(movimento.getCodPolo(), movimento.getCodBibInv(),
						movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()), annata);
			else
				return richiesteDAO.esistonoMovimentiAttivi(movimento.getCodPolo(), movimento.getCodBibDocLett(),
						movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
						Short.parseShort(movimento.getProgrEsempDocLet()), annata);

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	public boolean movimentoStatoPrecedeConsegnaDocLett(String ticket, MovimentoListaVO mov)
			throws ApplicationException {

		checkTicket(ticket);
		try {
			RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

			// viene riletto il movimento per acquisire gli eventuali aggiornamenti
			Tbl_richiesta_servizio movAttivo = internalGetMovimento(mov);
			if (movAttivo == null)
				return true;

			if (!mov.isRichiestaILL()) { // mov locale

				// Si verifica che il movimento sia in
				// un'attività che precede la "Consegna del documento all'utente" cioè
				// con il progressivo dell'iter attuale del movimento sia minore del
				// progressivo dell'iter (relativo a quel servizio) corrispondente
				// all'attività di "Consegna del documento all'utente" (03)

				IterServizioDAO iterServizioDAO = new IterServizioDAO();
				int progrIterMov = Integer.valueOf(mov.getProgrIter());
				Tbl_iter_servizio iterServizio = iterServizioDAO
						.getIterServizio(movAttivo.getId_servizio().getId_tipo_servizio(), "03");
				if (iterServizio == null) {
					// se non presente l'attività di "Consegna del documento all'utente"
					// si considera il movimento come se fosse in un'attività
					// che precede la "Consegna del documento all'utente"
					return true;
				} else
					return (progrIterMov < iterServizio.getProgr_iter());
			} else {
				// almaviva5_20160118 servizi ILL
				return ILLConfiguration2.getInstance().isRichiestaAnnullabile(mov.getDatiILL());
			}

		} catch (DaoManagerException ex) {
			throw new ApplicationException(ex);
		}
	}

	private Tbl_richiesta_servizio internalGetMovimentoAttivo(MovimentoVO movimento)
			throws NumberFormatException, DaoManagerException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
		int annata = getAnnataPeriodico(movimento);

		if (movimento.isRichiestaSuInventario())
			return richiesteDAO.getMovimentoAttivo(movimento.getCodPolo(), movimento.getCodBibOperante(),
					movimento.getCodBibInv(), movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()),
					annata);
		else
			return richiesteDAO.getMovimentoAttivo(movimento.getCodPolo(), movimento.getCodBibOperante(),
					movimento.getCodBibDocLett(), movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
					Short.parseShort(movimento.getProgrEsempDocLet()), annata);
	}

	private Tbl_richiesta_servizio internalGetMovimento(MovimentoVO movimento)
			throws NumberFormatException, DaoManagerException {

		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		return richiesteDAO.getMovimentoById(movimento.getIdRichiesta());

	}

	private int internalGetNumeroPrenotazioni(MovimentoVO movimento) throws DaoManagerException {
		RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();

		int annata = getAnnataPeriodico(movimento);

		if (movimento.isRichiestaSuInventario())
			return richiesteDAO.getNumeroPrenotazioni(movimento.getCodPolo(), movimento.getCodBibInv(),
					movimento.getCodSerieInv(), Integer.parseInt(movimento.getCodInvenInv()), annata);
		else
			return richiesteDAO.getNumeroPrenotazioni(movimento.getCodPolo(), movimento.getCodBibDocLett(),
					movimento.getTipoDocLett(), Long.parseLong(movimento.getCodDocLet()),
					Short.parseShort(movimento.getProgrEsempDocLet()), annata);
	}

	public DocumentoNonSbnVO getDettaglioDocumentoNonSbn(String ticket, DocumentoNonSbnVO documento)
			throws ApplicationException {
		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();

			Tbl_documenti_lettori dettaglio = dao.getDocumentoLettoreById(documento.getIdDocumento());
			if (dettaglio == null)
				return null;

			List<EsemplareDocumentoNonSbnVO> esemplari = internalGetListaEsemplariDocumentiNonSbn(
					dettaglio.getId_documenti_lettore());

			return ConversioneHibernateVO.toWeb().documentoNonSbn(dettaglio, esemplari);

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public List<DocumentoNonSbnVO> getListaDocumentiNonSbn(String ticket, DocumentoNonSbnRicercaVO filtro)
			throws ApplicationException {
		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();

			String ordinamento = filtro.getOrdinamento();
			String tipoOrd = null;
			if (ValidazioneDati.isFilled(ordinamento)) {
				TB_CODICI cod = CodiciProvider.cercaCodice(ordinamento,
						CodiciType.CODICE_ORDINAMENTO_LISTA_DOCUMENTO_LETTORE, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					tipoOrd = cod.getDs_cdsbn_ulteriore();
			}

			List<DocumentoNonSbnVO> output = new ArrayList<DocumentoNonSbnVO>();

			// almaviva5_20160317 serviziILL
			if (filtro.isRicercaOpac()) {
				// ricerca su OPAC
				String zurl = CommonConfiguration.getProperty(Configuration.OPAC_Z3950_URL);
				String zdb = CommonConfiguration.getProperty(Configuration.OPAC_Z3950_DB);
				List<Record> opacHits = Z3950ClientFactory.getClient().search(zurl, zdb, "unimarc",
						PQFBuilder.buildPQFQuery(filtro),
						CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, 4000));

				int progr = 0;
				for (Record rec : opacHits) {
					if (rec == null)
						continue;

					DocumentoNonSbnVO doc = new DocumentoNonSbnDecorator(Z3950ClientFactory.documentoFromMarc(rec));
					doc.setMarcRecord(rec.toString());
					doc.setProgr(++progr);
					output.add(doc);
				}

			} else {
				// ricerca su polo
				Tbl_documenti_lettori doc = ConversioneHibernateVO.toHibernate().documentoNonSbn(filtro);
				List<Tbl_documenti_lettori> documenti = dao.select(doc, filtro.getInventario(), tipoOrd,
						filtro.getListaBib());

				int progr = 0;
				for (Tbl_documenti_lettori row : documenti) {
					DocumentoNonSbnVO documentoNonSbn = ConversioneHibernateVO.toWeb().documentoNonSbn(row, null);
					documentoNonSbn.setProgr(++progr);
					output.add(new DocumentoNonSbnDecorator(documentoNonSbn));
				}
			}

			// almaviva5_20110406 #4348
			if (ValidazioneDati.equals(ordinamento, "1"))
				BaseVO.sortAndEnumerate(output, DocumentoNonSbnDecorator.ORDINA_PER_TITOLO);

			return output;

		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode());
		} catch (ApplicationException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<EsemplareDocumentoNonSbnVO> getListaEsemplariDocumentiNonSbn(String ticket,
			DocumentoNonSbnRicercaVO filtro) throws ApplicationException {
		try {
			checkTicket(ticket);
			return internalGetListaEsemplariDocumentiNonSbn(filtro.getIdDocumento());

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	private List<EsemplareDocumentoNonSbnVO> internalGetListaEsemplariDocumentiNonSbn(int idDocumento)
			throws DaoManagerException {

		Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();

		List<Tbl_esemplare_documento_lettore> esemplari = dao.getListaEsemplariDocumentoLettore(idDocumento);

		List<EsemplareDocumentoNonSbnVO> output = new ArrayList<EsemplareDocumentoNonSbnVO>();
		int progr = 0;
		for (Tbl_esemplare_documento_lettore row : esemplari)
			output.add(ConversioneHibernateVO.toWeb().esemplareDocumentoNonSbn(++progr, row));

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
	 *
	 *
	 */
	public Date getDateAutorizzazione(String ticket, String codPolo, String codBib, String codAutorizzazione,
			int idutente, String tipo) throws ResourceNotFoundException, ApplicationException {
		AutorizzazioniDAO autDAO = new AutorizzazioniDAO();
		Date output = null;
		try {
			checkTicket(ticket);
			output = autDAO.getDateAutorizzazione(codPolo, codBib, codAutorizzazione, idutente, tipo);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
		return output;
	}

	public List<DocumentoNonSbnVO> aggiornaDocumentoNonSbn(String ticket, List<DocumentoNonSbnVO> documenti)
			throws ApplicationException {
		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
			List<DocumentoNonSbnVO> output = new ArrayList<DocumentoNonSbnVO>(documenti.size());

			for (DocumentoNonSbnVO documento : documenti) {
				DocumentoNonSbnVO richiesta = documento.copy();

				String firmaUtente = DaoManager.getFirmaUtente(ticket);
				Timestamp ts = DaoManager.now();

				if (richiesta.isNuovo()) {
					richiesta.setData_sugg_lett(ts);
					richiesta.setTsIns(ts);
					richiesta.setUteIns(firmaUtente);
					richiesta.setUteVar(firmaUtente);
				} else {
					richiesta.setUteVar(firmaUtente);
				}

				// i suggerimenti non hanno esemplari o controlli su segnatura
				if (richiesta.isSuggerimento()) {
					richiesta.setOrd_segnatura("");
					Tbl_documenti_lettori doc = ConversioneHibernateVO.toHibernate().documentoNonSbn(richiesta);
					doc = dao.saveDocumento(doc);
					output.add(ConversioneHibernateVO.toWeb().documentoNonSbn(doc, null));
					continue;
				}

				// controlli su segnatura per documenti P
				richiesta.setOrd_segnatura(
						ValidazioneDati.coalesce(OrdinamentoCollocazione2.normalizza(richiesta.getSegnatura()), ""));

				Tbl_documenti_lettori doc = ConversioneHibernateVO.toHibernate().documentoNonSbn(richiesta);

				// controllo duplicazione segnatura per tipo=P
				if (richiesta.isPosseduto()) {
					Tbl_documenti_lettori oldSegnatura = dao.esisteSegnatura(doc);
					if (oldSegnatura != null) {
						if (oldSegnatura.getFl_canc() != 'S')
							throw new ApplicationException(SbnErrorTypes.SRV_SEGNATURA_ESISTENTE);

						if (documento.isNuovo()) {
							// recupero record cancellato
							ConversioneHibernateVO.toHibernate().assignEntityID(doc, "id_documenti_lettore",
									oldSegnatura.getId_documenti_lettore());
							doc.setCod_doc_lett(doc.getId_documenti_lettore());
							doc.setTs_var(oldSegnatura.getTs_var());
							doc.setFl_canc('N');
						} else {
							// almaviva5_20120620 #5038 modifica documento
							// se la segnatura corrisponde a una segnatura cancellata, quest'ultima
							// dev'essere resa univoca
							oldSegnatura.setOrd_segnatura("");
							dao.saveDocumento(oldSegnatura);
						}
					}
				}

				doc = dao.saveDocumento(doc);

				List<EsemplareDocumentoNonSbnVO> esemplari = new ArrayList<EsemplareDocumentoNonSbnVO>();

				if (documento.isNuovo() && !richiesta.isSuggerimento()) {
					// viene creato il 1° esemplare (solo per tipoDoc == P, D)
					Tbl_esemplare_documento_lettore esemplare = new Tbl_esemplare_documento_lettore();
					esemplare.setId_documenti_lettore(doc);
					// almaviva5_20110209
					// segnalazione LIG: errore valorizzazione prog. con numeri > 32768
					// esemplare.setPrg_esemplare((short)1 );
					esemplare.setFonte(doc.getFonte());
					esemplare.setInventario("vol. 1");
					esemplare.setFl_canc('N');
					esemplare.setTs_ins(ts);
					esemplare.setUte_ins(firmaUtente);
					esemplare.setUte_var(firmaUtente);

					esemplare = dao.saveEsemplare(esemplare);
					esemplari.add(ConversioneHibernateVO.toWeb().esemplareDocumentoNonSbn(1, esemplare));
				} else
					esemplari = internalAggiornaEsemplariDocumentoNonSbn(ticket, doc, documento.getEsemplari());

				output.add(ConversioneHibernateVO.toWeb().documentoNonSbn(doc, esemplari));

			}

			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		}
	}

	public void aggiornaEsemplariDocumentoNonSbn(String ticket, List<EsemplareDocumentoNonSbnVO> esemplari)
			throws ApplicationException {
		try {
			checkTicket(ticket);
			internalAggiornaEsemplariDocumentoNonSbn(ticket, null, esemplari);

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION);
		}
	}

	private List<EsemplareDocumentoNonSbnVO> internalAggiornaEsemplariDocumentoNonSbn(String ticket,
			Tbl_documenti_lettori documento, List<EsemplareDocumentoNonSbnVO> esemplari)
			throws DaoManagerException, DAOConcurrentException {

		Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
		List<EsemplareDocumentoNonSbnVO> output = new ArrayList<EsemplareDocumentoNonSbnVO>();

		if (ValidazioneDati.size(esemplari) < 1)
			return output;

		int idDocumento = esemplari.get(0).getIdDocumento();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		Timestamp ts = DaoManager.now();
		int progr = 0;

		for (EsemplareDocumentoNonSbnVO esemplare : esemplari) {

			if (esemplare.isNuovo()) {
				esemplare.setUteIns(firmaUtente);
				esemplare.setUteVar(firmaUtente);
				esemplare.setTsIns(ts);
			} else
				esemplare.setUteVar(firmaUtente);

			Tbl_esemplare_documento_lettore esempl = ConversioneHibernateVO.toHibernate()
					.esemplareDocumentoNonSbn(documento, idDocumento, esemplare);
			esempl = dao.saveEsemplare(esempl);

			output.add(ConversioneHibernateVO.toWeb().esemplareDocumentoNonSbn(++progr, esempl));
		}

		return output;
	}

	private List<Vl_richiesta_servizio> getListaRichiesteScaduteSollecitabili(MovimentoRicercaVO filtro,
			short ggPrimoSoll) {

		TipoServizioDAO tipoServizioDAO = new TipoServizioDAO();
		List<Vl_richiesta_servizio> output = new ArrayList<Vl_richiesta_servizio>();
		try {
			List<String> serviziSollecitabili = tipoServizioDAO.getTipiServizioSollecitabili(filtro.getCodPolo(),
					filtro.getCodBibOperante());

			// non ci sono servizi dichiarati sollecitabili
			if (!ValidazioneDati.isFilled(serviziSollecitabili))
				return output;

			RichiesteServizioDAO richiesteDAO = new RichiesteServizioDAO();
			output = richiesteDAO.getListaRichiesteScadute(filtro.getCodPolo(), filtro.getCodBibOperante(),
					filtro.getDataFinePrev(), serviziSollecitabili.toArray(new String[0]), filtro.getTipoOrdinamento(),
					ggPrimoSoll, filtro.isEscludiSollecitiProrogati());

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}

		return output;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			if (command == null)
				throw new ApplicationException(SbnErrorTypes.COMMAND_INVOKE_VALIDATION);

			command.validate();
			String ticket = command.getTicket();
			checkTicket(ticket);

			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();

			Serializable[] params = command.getParams();

			switch (command.getCommand()) {
			case ECHO:
				String txt = (String) params[0];
				return CommandResultVO.build(command, "echo: " + txt + " " + System.currentTimeMillis());

			case AMM_RELOAD:
				ProfilerManager.getProfilerManagerInstance().reload();
				BatchManager.getBatchManagerInstance().reload();
				return CommandResultVO.build(command);

			case DETTAGLIO_DOCUMENTO_NON_SBN_CON_ESEMPLARE:
				String cdpolo = (String) params[0];
				String cdbib = (String) params[1];
				String tipo = (String) params[2];
				int codDoc = (Integer) params[3];
				int prg_esempl = (Integer) params[4];
				Tbl_esemplare_documento_lettore esemplare = dao.getEsemplareDocumentoLettore(cdpolo, cdbib, tipo,
						codDoc, (short) prg_esempl);
				if (esemplare == null)
					throw new ApplicationException(SbnErrorTypes.SRV_DOCUMENTO_LETTORE_NON_TROVATO);

				List<EsemplareDocumentoNonSbnVO> esemplari = new ArrayList<EsemplareDocumentoNonSbnVO>();
				esemplari.add(ConversioneHibernateVO.toWeb().esemplareDocumentoNonSbn(1, esemplare));
				DocumentoNonSbnVO doc = ConversioneHibernateVO.toWeb()
						.documentoNonSbn(esemplare.getId_documenti_lettore(), esemplari);
				return CommandResultVO.build(command, doc);

			case CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN:
				DocumentoNonSbnVO input = (DocumentoNonSbnVO) params[0];
				MovimentoVO mov = new MovimentoVO();
				mov.setCodPolo(input.getCodPolo());
				mov.setCodBibOperante(input.getCodBib());
				mov.setCodBibDocLett(input.getCodBib());
				mov.setTipoDocLett(input.getTipo_doc_lett() + "");
				mov.setCodDocLet(input.getCod_doc_lett() + "");
				mov.setProgrEsempDocLet(ValidazioneDati.first(input.getEsemplari()).getPrg_esemplare() + "");
				DocumentoNonSbnVO docFrui = getCategoriaFruizioneSegnatura(ticket, mov);
				return CommandResultVO.build(command, docFrui);

			case LISTA_RICHIESTE_SCADUTE:
				return getListaRichiesteScadute(command);

			case LISTA_MOD_EROGAZIONE_LEGATE_SUPPORTI:
				MovimentoVO filtroMov = (MovimentoVO) params[0];
				List<TariffeModalitaErogazioneVO> listaModErog = getListaModErogLegateASupporti(filtroMov);
				return CommandResultVO.build(command, (Serializable) listaModErog);

			// almaviva5_20100219
			case DETTAGLIO_MOVIMENTO_DI_PRENOTAZIONE:
				MovimentoVO pren = (MovimentoVO) params[0];
				Locale locale = (Locale) params[1];
				MovimentoVO dettaglioMov = getDettaglioMovimentoDiPrenotazione(pren, locale);
				return CommandResultVO.build(command, dettaglioMov);

			// almaviva5_20100322 #3647
			case CATEGORIA_FRUIZIONE_DOC_NON_SBN_PER_SALVA:
				DocumentoNonSbnVO input2 = (DocumentoNonSbnVO) params[0];
				input2.setOrd_segnatura(OrdinamentoCollocazione2.normalizza(input2.getSegnatura()));
				DocumentoNonSbnVO doc2 = internalGetCategoriaFruizioneSegnatura(null, input2.getCodPolo(),
						input2.getCodBib(), input2.getOrd_segnatura());
				return CommandResultVO.build(command, doc2, null);

			// almaviva5_20100505
			case ESISTE_MOVIMENTO_ATTIVO_PER_ITER:
				IterServizioVO iter = (IterServizioVO) params[0];
				IterServizioDAO iterDAO = new IterServizioDAO();
				boolean esiste = (iterDAO.contaMovimentiAttiviPerIter(iter.getIdIterServizio()) > 0);
				return CommandResultVO.build(command, new Boolean(esiste), null);
			/*
			 * case INVIA_MAIL_UTENTE_PRENOTAZIONE: MovimentoListaVO prenotazione =
			 * (MovimentoListaVO) params[0]; boolean send =
			 * inviaMailUtentePrenotazione(ticket, prenotazione); return
			 * CommandResultVO.build(command, new Boolean(send), null);
			 */
			// almaviva5_20100531
			case CERCA_ESEMPLARE_DOCUMENTO_LIBERO:
				String polo = (String) params[0];
				String bib = (String) params[1];
				DocumentoNonSbnVO doc1 = (DocumentoNonSbnVO) params[2];
				short prg = cercaEsemplareLibero(polo, bib, doc1);
				return CommandResultVO.build(command, new Short(prg), null);

			// almaviva5_20100609
			case LISTA_RICHIESTE_STORICO:
				MovimentoVO filtroStorico = (MovimentoVO) params[0];
				List<MovimentoPerStampaServCorrVO> storico = getListaRichiesteStorico(ticket, filtroStorico);
				return CommandResultVO.build(command, (Serializable) storico, null);

			// almaviva5_20101103 #3958
			case ESISTE_INVENTARIO_BIB_SISTEMA_METRO:
				List<BibliotecaVO> filtroBib = (List<BibliotecaVO>) params[0];
				String bid = (String) params[1];
				boolean found = esisteInventarioBibliotecaSistemaMetro(ticket, filtroBib, bid);
				return CommandResultVO.build(command, new Boolean(found), null);

			// almaviva5_20101206 #4043 #4044
			case SRV_OCCUPAZIONE_BY_ID:
				OccupazioniDAO odao = new OccupazioniDAO();
				Tbl_occupazioni occupazione = odao.getOccupazioneById((Integer) params[0]);
				OccupazioneVO occ = null;
				if (occupazione != null)
					occ = ServiziConversioneVO.daHibernateAWebOccupazione(occupazione, 0);
				return CommandResultVO.build(command, occ, null);

			case SRV_TITOLO_STUDIO_BY_ID:
				TitoliDiStudioDAO tdao = new TitoliDiStudioDAO();
				Tbl_specificita_titoli_studio sts = tdao.getTitoloStudioById((Integer) params[0]);
				SpecTitoloStudioVO titstu = null;
				if (sts != null)
					titstu = ServiziConversioneVO.daHibernateAWebTitoloStudio(sts, 0);
				return CommandResultVO.build(command, titstu, null);

			case SRV_LISTA_ALTRE_BIB_AUTOREG:
				List<BibliotecaVO> listBibAutoreg = getListaAltreBibPerAutoregistrazione((String) params[0],
						(Integer) params[1]);
				return CommandResultVO.build(command, (Serializable) listBibAutoreg, null);

			case SRV_CONTA_ESEMPLARI_DOCUMENTO_LIBERO:
				int cnt = dao.countEsemplariDocumento((Integer) params[0]);
				return CommandResultVO.build(command, cnt, null);

			case SRV_CONTROLLO_DISPONIBILITA_WS:
				return wsControlloDisponibilita(command);

			case SRV_CONTROLLA_MAIL_UTENTE:
				return controllaMailUtente(command);

			case SRV_VALIDA_MODELLO_SOLLECITO:
				return validaModelloSollecito(command);

			case SRV_AGGIORNA_RICHIESTA_PER_PROROGA:
				return aggiornaRichiestaPerProroga(command);

			case SRV_NOTIFICA_UTENTE_PRENOTAZIONE:
				return inviaNotificaUtentePrenotazione(command);

			case SRV_CANCELLA_TIPO_SERVIZIO:
				return cancellaTipoServizio(command);

			case SRV_NOTIFICA_NUOVA_RICHIESTA:
				return inviaMailNotificaNuovaRichiesta(command);

			case SRV_DOCUMENTO_NON_SBN_CANCELLABILE:
				return documentoNonSbnCancellabile(command);
			}

			// handler per servizi ILL
			return ill_handler.invoke(command);

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch (ValidationException e) {
			throw new ApplicationException(e.getErrorCode(), e.getLabels());
		} catch (Exception e) {
			log.error("", e);
			ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

	private CommandResultVO wsControlloDisponibilita(CommandInvokeVO command) throws Exception {

		ControlloDisponibilitaVO cdvo = (ControlloDisponibilitaVO) command.getParams()[0];
		boolean caricaDatiInvCollDoc = (Boolean) command.getParams()[1];
		MovimentoVO mov = cdvo.getMovimento();

		Tbc_inventarioDao idao = new Tbc_inventarioDao();
		String codPolo = mov.getCodPolo();
		String codBib = mov.getCodBibInv();
		Tbc_inventario i = idao.getInventario(codPolo, codBib, mov.getCodSerieInv(),
				Integer.valueOf(mov.getCodInvenInv()));
		if (i == null)
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		String ticket = command.getTicket();
		ControlloDisponibilitaVO disp = this.controlloDisponibilita(ticket, cdvo);

		boolean inPrestito = !disp.isDisponibile();

		// almaviva5_20150603 altri controlli disponibilità
		if (disp.isDisponibile()) {
			// controllo dismissione
			if (i.getCd_sit() == DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
				inPrestito = false;
				disp.setDisponibile(false);
				disp.setMotivo(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_MOTIVI_DI_SCARICO_INVENTARIALE,
						i.getCd_scarico()));
			}

			String noDisp = i.getCd_no_disp();
			if (ValidazioneDati.isFilled(noDisp)) {
				inPrestito = false;
				disp.setDisponibile(false);
				Timestamp dataRientro = null;
				// controllo su ordini di rilegatura
				Tba_ordiniDao odao = new Tba_ordiniDao();
				Tra_ordine_inventari oi = odao.getInventarioRilegatura(i);
				if (oi != null) {
					dataRientro = ValidazioneDati.coalesce(oi.getData_rientro(),
							oi.getData_rientro_presunta());
				}

				if (dataRientro == null) {
					//almaviva5_20190523
					Date data_redisp_prev = i.getData_redisp_prev();
					if (data_redisp_prev != null) {
						dataRientro = new Timestamp(data_redisp_prev.getTime());
					}
				}
				// disponibilità intrinseca
				disp.setDataPrenotazione(dataRientro);
				disp.setMotivo(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_NON_DISPONIBILITA, noDisp));
			}
		}

		DisponibilitaType dtype = ConversioneHibernateVO.toXML().disponibilita(disp);
		if (caricaDatiInvCollDoc) {
			InventarioType itype = ConversioneHibernateVO.toXML().inventario(i);
			itype.setCollocazione(ConversioneHibernateVO.toXML().collocazione(i.getKey_loc()));
			dtype.setInventario(itype);
			// almaviva5_20150514
			DocumentoType doc = new DocumentoType();
			doc.setBid(i.getB().getBid());
			itype.setDocumento(doc);
		}

		// almaviva5_20150619 ulteriori dettagli su prenotabilità
		if (inPrestito) {
			PrenotazioniType ptype = new PrenotazioniType();
			boolean prenotabile = false;

			int prenotazioniPendenti = disp.getPrenotazioniPendenti();
			ParametriBibliotecaVO parBib = getParametriBiblioteca(ticket, codPolo, codBib);
			if (parBib == null)
				throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

			short numMaxPrenotazioniDoc = parBib.getNumeroPrenotazioni();
			prenotabile = (numMaxPrenotazioniDoc > 0) && prenotazioniPendenti < numMaxPrenotazioniDoc;

			ptype.setNumeroPrenotazioni(prenotazioniPendenti > 0 ? prenotazioniPendenti : null);

			// controllo su prenotabilità dei servizi ammessi al documento
			if (prenotabile) {
				prenotabile = false;
				List<TB_CODICI> servizi = CodiciProvider.getCodiciCross(CodiciType.CODICE_CAT_FRUIZIONE_TIPO_SERVIZIO,
						i.getCd_frui().trim(), true);
				if (ValidazioneDati.isFilled(servizi)) {
					List<String> serviziPrenotabili = ptype.getServizio();
					for (TB_CODICI cod : servizi) {
						String codTipoServ = cod.getCd_tabellaTrim();
						TipoServizioVO ts = getTipoServizio(ticket, codPolo, codBib, codTipoServ);
						if (ts.isCodaRichieste()) {
							serviziPrenotabili.add(CodiciProvider
									.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, codTipoServ));
						}
					}
					prenotabile = ValidazioneDati.isFilled(serviziPrenotabili);
				}
			}

			ptype.setPrenotabile(prenotabile);
			dtype.setPrenotazioni(ptype);
		}

		return CommandResultVO.build(command, dtype, null);
	}

	private CommandResultVO getListaRichiesteScadute(CommandInvokeVO command) throws Exception {
		MovimentoRicercaVO ricerca = (MovimentoRicercaVO) command.getParams()[0];
		// almaviva5_20100216
		UtilitaDAO utilDAO = new UtilitaDAO();
		Tbl_parametri_biblioteca parBib = utilDAO.getParametriBiblioteca(ricerca.getCodPolo(),
				ricerca.getCodBibOperante());
		if (parBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);
		// continuo solo se la biblioteca gestisce almeno un sollecito
		short numGgRitardo1 = parBib.getNum_gg_ritardo1();
		if (parBib.getNum_lettere() == 0 || numGgRitardo1 == 0)
			throw new ApplicationException(SbnErrorTypes.SRV_BIBLIOTECA_NO_SOLLECITI);

		List<Vl_richiesta_servizio> richiesteScadute = getListaRichiesteScaduteSollecitabili(ricerca, numGgRitardo1);
		ArrayList<MovimentoListaVO> output = new ArrayList<MovimentoListaVO>();

		for (Vl_richiesta_servizio req : richiesteScadute) {
			// MovimentoListaVO movScaduto =
			// ServiziConversioneVO.daHibernateAWebMovimentoLista(req, null, now);
			MovimentoListaVO movScaduto = ConversioneHibernateVO.toWeb().movimentoLista(req);
			// almaviva5_20111107 #4691
			movScaduto.setConsegnato(true);
			// decodificaCodiciMovimento(movScaduto, mov1.getCodPolo(),
			// mov1.getCodBibOperante(), null, req);
			output.add(movScaduto);
			// almaviva5_20111118
			utilDAO.getCurrentSession().evict(req);

		}

		// almaviva5_20100119
		ordinaListaMovimenti(output, ricerca.getTipoOrdinamento());
		return CommandResultVO.build(command, output, null);
	}

	private boolean esisteInventarioBibliotecaSistemaMetro(String ticket, List<BibliotecaVO> filtroBib, String bid)
			throws Exception {

		if (!ValidazioneDati.isFilled(filtroBib)) {
			// imposto comunque la bib corrente
			BibliotecaVO bib = new BibliotecaVO();
			bib.setCod_polo(DaoManager.codPoloFromTicket(ticket));
			bib.setCod_bib(DaoManager.codBibFromTicket(ticket));
			filtroBib = ValidazioneDati.asSingletonList(bib);
		}

		Tbc_inventarioDao dao = new Tbc_inventarioDao();
		for (BibliotecaVO b : filtroBib)
			if (dao.countInventariPerCollocato(b.getCod_polo(), b.getCod_bib(), bid) > 0)
				return true;

		return false;
	}

	private List<MovimentoPerStampaServCorrVO> getListaRichiesteStorico(String ticket, MovimentoVO filtroStorico)
			throws Exception {
		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		IterServizioDAO iterServizioDAO = new IterServizioDAO();
		Tbf_biblioteca_in_polo biblioteca = null;
		String descrBib = null;

		MovimentoRicercaVO movRicerca = (MovimentoRicercaVO) filtroStorico;

		Tbf_biblioteca_in_poloDao daoBib = new Tbf_biblioteca_in_poloDao();
		biblioteca = daoBib.select(movRicerca.getCodPolo(), movRicerca.getCodBibOperante());
		if (biblioteca != null) {
			descrBib = biblioteca.getDs_biblioteca();
		} else {
			descrBib = "Descrizione biblioteca mancante";
		}
		String tipoOrd = movRicerca.getTipoOrdinamento();
		List<Tbl_storico_richieste_servizio> richieste = dao.getListaRichiesteFiltriTematiciStorico(
				movRicerca.getCodPolo(), movRicerca.getCodBibOperante(), movRicerca.getDataInizioEff(),
				movRicerca.getDataFineEff(), movRicerca.getCodUte(), tipoOrd);

		int progr = 0;
		List<MovimentoPerStampaServCorrVO> output = new ArrayList<MovimentoPerStampaServCorrVO>();
		for (Tbl_storico_richieste_servizio req : richieste) {
			MovimentoPerStampaServCorrVO movimento = ServiziConversioneVO.daHibernateAWebMovimentoPerStampa(req, null,
					ticket);
			output.add(movimento);
		}
		return output;

	}

	private short cercaEsemplareLibero(String polo, String bib, DocumentoNonSbnVO doc) throws Exception {
		Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
		// cerco un esemplare non occupato da movimenti attivi
		Tbl_esemplare_documento_lettore esempl = dao.cercaEsemplareLibero(polo, bib, doc.getIdDocumento());
		if (esempl != null)
			return esempl.getPrg_esemplare();

		// se non esiste un esemplare libero cerco quello che ha la data
		// di rientro più vicina
		esempl = dao.cercaEsemplareProssimoAllaRiconsegna(polo, bib, doc.getIdDocumento());
		if (esempl != null)
			return esempl.getPrg_esemplare();

		return 0;
	}

	public MovimentoVO getDettaglioMovimentoDiPrenotazione(MovimentoVO pren, Locale locale)
			throws RemoteException, Exception {

		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		Tbl_richiesta_servizio richiesta;
		int annata = getAnnataPeriodico(pren);

		if (pren.isRichiestaSuInventario())
			richiesta = dao.getMovimentoAttivo(pren.getCodPolo(), pren.getCodBibOperante(), pren.getCodBibInv(),
					pren.getCodSerieInv(), Integer.parseInt(pren.getCodInvenInv()), annata);
		else
			richiesta = dao.getMovimentoAttivo(pren.getCodPolo(), pren.getCodBibOperante(), pren.getCodBibDocLett(),
					pren.getTipoDocLett(), Long.parseLong(pren.getCodDocLet()),
					Short.parseShort(pren.getProgrEsempDocLet()), annata);

		if (richiesta == null)
			return null;

		Timestamp now = DaoManager.now();
		MovimentoListaVO movimentoListaVO = ServiziConversioneVO.daHibernateAWebMovimentoLista(richiesta, locale, now);
		decodificaCodiciMovimento(movimentoListaVO, pren.getCodPolo(), pren.getCodBibOperante(), locale, richiesta);

		return movimentoListaVO;
	}

	public RinnovoDirittiDiffVO gestioneDifferitaRinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO)
			throws ApplicationException {
		RinnovoDirittiDiffVO output = null;
		try {
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return output;
	}

	private List<TariffeModalitaErogazioneVO> getListaModErogLegateASupporti(MovimentoVO mov) {
		ModalitaErogazioneDAO dao = new ModalitaErogazioneDAO();
		try {
			List<TariffeModalitaErogazioneVO> output = new ArrayList<TariffeModalitaErogazioneVO>();
			Set<String> codErogDistinct = new HashSet<String>();
			List<Object[]> lista = dao.getListaModErogLegateASupporti(mov.getCodPolo(), mov.getCodBibOperante());
			for (Object[] modErog : lista) {
				String codErog = ValidazioneDati.trimOrEmpty(((Tbl_modalita_erogazione) modErog[0]).getCod_erog());
				if (codErogDistinct.contains(codErog))
					continue;

				codErogDistinct.add(codErog);

				SupportiModalitaErogazioneVO suppVO = ServiziConversioneVO.daHibernateAWebSupportiModalitaErogazione(
						(Tbl_modalita_erogazione) modErog[0], (Trl_supporti_modalita_erogazione) modErog[1]);
				TariffeModalitaErogazioneVO tariffaModErogVO = new TariffeModalitaErogazioneVO();
				ClonePool.copyCommonProperties(tariffaModErogVO, suppVO);
				output.add(tariffaModErogVO);
			}
			return output;

		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
	}

	public boolean checkEsistenzaUtente(String codfiscale, String mail, String[] ateneo_mat, String idUte)
			throws ResourceNotFoundException {
		// ResourceNotFoundException, ApplicationException {
		boolean ritorno = false;
		try {
			// seleziono l'utente
			UtentiDAO utenteDAO = new UtentiDAO();
			ritorno = utenteDAO.checkEsistenzaUtente(codfiscale, mail, ateneo_mat, idUte);
		} catch (DaoManagerException e) {
			throw new EJBException(e);
		}
		return ritorno;
	}

	private void ordinaListaMovimenti(List<MovimentoListaVO> listaDaOrdinare, String tipoOrd) {

		if (!ValidazioneDati.isFilled(tipoOrd) || !ValidazioneDati.isFilled(listaDaOrdinare))
			return;

		Comparator<MovimentoListaVO> c = comparators.get(tipoOrd);
		if (c == null) {
			log.warn("Tipo ordinamento richiesto non esistente: '" + tipoOrd + "'");
			return;
		}

		BaseVO.sortAndEnumerate(listaDaOrdinare, c);
	}

	public boolean aggiornaChiaveUtenteById(String ticket, String idUtente, String chiaveUte)
			throws DataException, ApplicationException {
		boolean esito = false;

		try {
			UtentiDAO utentiDao = new UtentiDAO();
			checkTicket(ticket);
			Trl_utenti_biblioteca uteBibTrovato = utentiDao.getUtenteBibliotecaById(Integer.valueOf(idUtente));
			if (uteBibTrovato != null) {
				Tbl_utenti uteTrovato = uteBibTrovato.getId_utenti();
				if (uteTrovato != null && !ValidazioneDati.isFilled(uteTrovato.getChiave_ute())
						&& ValidazioneDati.isFilled(chiaveUte)) {
					uteTrovato.setChiave_ute(chiaveUte);
					// uteTrovato.setTs_var(uteTrovato.getTs_var());
					uteTrovato.setUte_var(DaoManager.getFirmaUtente(ticket));
					utentiDao.aggiornaUtente(uteTrovato);
				}
				esito = true;
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return esito;
	}

	public List<DocumentoNonSbnVO> esisteDocumentoNelRangeDiSegnatura(String ticket, String codPolo, String codBib,
			RangeSegnatureVO segnaturaVO, String ordinamento) throws DataException, ApplicationException {
		List<DocumentoNonSbnVO> output = new ArrayList<DocumentoNonSbnVO>();

		SegnatureDAO segnatureDao = new SegnatureDAO();

		Tbl_disponibilita_precatalogati segnatura = ServiziConversioneVO.daWebAHibernateSegnatura(segnaturaVO);

		try {
			checkTicket(ticket);
			Tbl_documenti_lettoriDAO documentiDAO = new Tbl_documenti_lettoriDAO();

			List<Tbl_documenti_lettori> outputTblDoc = documentiDAO.esisteDocumentoNelRangeDiSegnatura(codPolo, codBib,
					segnatura, ordinamento);

			for (int i = 0; i < outputTblDoc.size(); i++) {
				DocumentoNonSbnVO baseDoc = ConversioneHibernateVO.toWeb().documentoNonSbn(outputTblDoc.get(i), null);
				output.add(baseDoc);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return output;
	}

	public boolean inviaMailUtentePrenotazione(String ticket, MovimentoListaVO p)
			throws ApplicationException, EJBException {
		try {
			checkTicket(ticket);

			if (p == null)
				return false;

			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			Tbl_richiesta_servizio req = dao.getMovimentoById(p.getIdRichiesta());
			if (req == null) {
				log.warn("Richiesta non esistente: " + p.getIdRichiesta());
				return false;
			}

			ServiziMail util = new ServiziMail();
			AddressPair pair = util.getMailBiblioteca(p.getCodPolo(), p.getCodBibDest());
			InternetAddress uteMail = util.getMailUtenteMovimento(req);
			if (uteMail == null) {
				log.error("Indirizzo mail non impostato per utente "
						+ req.getId_utenti_biblioteca().getId_utenti().getCod_utente());
				return false;
			}

			MailVO mail = new MailVO();
			mail.setFrom(pair.getFrom());
			mail.getReplyTo().add(pair.getReplyTo());
			mail.getTo().add(uteMail);

			StringBuilder subject = new StringBuilder();
			// almaviva5_20150916 nuova gestione prenotazioni
			// subject.append("Prenotazione n. ");
			subject.append(
					p.getFlTipoRec() == RichiestaRecordType.FLAG_PRENOTAZIONE ? "Prenotazione n. " : "Richiesta n. ");
			subject.append(p.getCodPolo());
			subject.append(p.getCodBibDest()).append(' ');
			subject.append(req.getCod_rich_serv());
			mail.setSubject(subject.toString());

			mail.setBody(MailBodyBuilder.Servizi.prenotazioneDocRestituito(p));
			return MailUtil.sendMailAsync(mail);

		} catch (ApplicationException e) {
			log.error("", e);
			return false;
		} catch (ValidationException e) {
			log.error("", e);
			return false;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public void eseguiControlliRichiestaSIP2(DatiControlloVO dati) throws ApplicationException, EJBException {
		try {
			ControlloAttivitaServizio controlliBase = ControlloAttivitaServizio.getControlliBase();
			// controlli base (utente, diritti, documento)
			ControlloAttivitaServizioResult result = controlliBase.controlloDefault(dati);
			if (!ControlloAttivitaServizioResult.isOK(result))
				return;

			// controlli primo iter (se previsti)
			IterServizioDAO iterDAO = new IterServizioDAO();
			MovimentoVO mov = dati.getMovimento();
			Tbl_iter_servizio iter = iterDAO.getIterServizio(mov.getCodPolo(), mov.getCodBibOperante(),
					mov.getCodTipoServ(), 1);
			if (iter == null)
				return;

			List<FaseControlloIterVO> controlli = new ArrayList<FaseControlloIterVO>();
			Tbl_tipo_servizio tipoSrv = iter.getId_tipo_servizio();
			List<Tbl_controllo_iter> listaControlli = iterDAO.getControlloIter(tipoSrv.getId_tipo_servizio(),
					iter.getCod_attivita());
			if (ValidazioneDati.isFilled(listaControlli))
				for (Tbl_controllo_iter ci : listaControlli)
					controlli.add(ServiziConversioneVO.daHibernateAWebControlloIter(ci));

			ControlloAttivitaServizio cas = new ControlloAttivitaServizio(
					ConversioneHibernateVO.toWeb().iterServizio(iter), controlli, null);
			List<String> messaggi = dati.getCodiciMsgSupplementari();
			if (!cas.controlloIter(dati, messaggi, false, false)) // controllo non superato
				dati.setResult(ControlloAttivitaServizioResult.ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO);

		} catch (Exception e) {
			log.error("", e);
			throw new EJBException(e);
		}
	}

	public void aggiornaIterRichiestaSIP2(DatiControlloVO dati) throws ApplicationException, EJBException {
		try {
			// controlli primo iter (se previsti)
			IterServizioDAO iterDAO = new IterServizioDAO();
			MovimentoVO mov = dati.getMovimento();
			Tbl_iter_servizio iter = null;
			if (mov.isNuovo()) {
				iter = iterDAO.getIterServizio(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), 1); // primo
																													// iter
			} else
				iter = iterDAO.getIterServizio(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(),
						mov.getCodAttivita());
			if (iter == null)
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_CONFIGURAZIONE_ITER);

			IterServizioVO iterVO = ConversioneHibernateVO.toWeb().iterServizio(iter);
			mov.setProgrIter(iterVO.getProgrIter().toString());
			mov.setCodStatoMov(iterVO.getCodStatoMov());
			mov.setCodStatoRic(iterVO.getCodStatoRich());
			mov.setCodAttivita(iterVO.getCodAttivita());

			List<FaseControlloIterVO> controlli = new ArrayList<FaseControlloIterVO>();
			Tbl_tipo_servizio tipoSrv = iter.getId_tipo_servizio();
			List<Tbl_controllo_iter> listaControlli = iterDAO.getControlloIter(tipoSrv.getId_tipo_servizio(),
					iter.getCod_attivita());
			if (ValidazioneDati.isFilled(listaControlli))
				for (Tbl_controllo_iter ci : listaControlli)
					controlli.add(ServiziConversioneVO.daHibernateAWebControlloIter(ci));

			ControlloAttivitaServizio cas = new ControlloAttivitaServizio(iterVO, controlli, null);
			List<String> messaggi = dati.getCodiciMsgSupplementari();
			ControlloAttivitaServizioResult result = cas.controlloDefault(dati);
			if (!ControlloAttivitaServizioResult.isOK(result))
				dati.setResult(result);
			else
				aggiornaRichiesta(dati.getTicket(), mov, dati.getServizio().getIdServizio());

		} catch (Exception e) {
			log.error("", e);
			throw new EJBException(e);
		}
	}

	public DatiControlloVO eseguiControlliIterRichiesta(DatiControlloVO dati)
			throws ApplicationException, EJBException {
		try {
			// controlli primo iter (se previsti)
			IterServizioDAO iterDAO = new IterServizioDAO();
			MovimentoVO mov = dati.getMovimento();
			Tbl_iter_servizio iter = null;
			if (mov.isNuovo()) {
				ControlloAttivitaServizio controlliBase = ControlloAttivitaServizio.getControlliBase();
				// controlli base (utente, diritti, documento)
				ControlloAttivitaServizioResult result = controlliBase.controlloDefault(dati);
				if (!ControlloAttivitaServizioResult.isOK(result))
					return dati.setResult(result);

				iter = iterDAO.getIterServizio(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), 1); // primo
																													// iter
			} else
				iter = iterDAO.getIterServizio(mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(),
						mov.getCodAttivita());
			if (iter == null)
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_CONFIGURAZIONE_ITER);

			IterServizioVO iterVO = ConversioneHibernateVO.toWeb().iterServizio(iter);
			mov.setProgrIter(iterVO.getProgrIter().toString());
			mov.setCodStatoMov(iterVO.getCodStatoMov());
			mov.setCodStatoRic(iterVO.getCodStatoRich());
			mov.setCodAttivita(iterVO.getCodAttivita());

			List<FaseControlloIterVO> controlli = new ArrayList<FaseControlloIterVO>();
			Tbl_tipo_servizio tipoSrv = iter.getId_tipo_servizio();
			List<Tbl_controllo_iter> listaControlli = iterDAO.getControlloIter(tipoSrv.getId_tipo_servizio(),
					iter.getCod_attivita());
			if (ValidazioneDati.isFilled(listaControlli))
				for (Tbl_controllo_iter ci : listaControlli)
					controlli.add(ServiziConversioneVO.daHibernateAWebControlloIter(ci));

			ControlloAttivitaServizio cas = new ControlloAttivitaServizio(iterVO, controlli, null);

			// controllo default iter
			ControlloAttivitaServizioResult result = cas.controlloDefault(dati);
			if (!ControlloAttivitaServizioResult.isOK(result))
				return dati.setResult(result);

			// controlli supplementari
			List<String> messaggi = dati.getCodiciMsgSupplementari();
			if (!cas.controlloIter(dati, messaggi, false, false)) // controllo non superato
				dati.setResult(ControlloAttivitaServizioResult.ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO);

		} catch (Exception e) {
			log.error("", e);
			throw new EJBException(e);
		}

		return dati;
	}

	private boolean inviaMailUtenteRifiutoRichiesta(String codPolo, Tbl_richiesta_servizio req) {

		try {
			MovimentoListaVO mov = ServiziConversioneVO.daHibernateAWebMovimentoLista(req, Locale.getDefault(),
					DaoManager.now());
			String codBib = mov.isRichiestaSuInventario() ? mov.getCodBibInv() : mov.getCodBibDocLett();
			decodificaCodiciMovimento(mov, codPolo, codBib, Locale.getDefault(), req);

			ServiziMail util = new ServiziMail();
			AddressPair pair = util.getMailBiblioteca(codPolo, codBib);
			InternetAddress uteMail = util.getMailUtenteMovimento(req);
			if (uteMail == null) {
				log.error("Indirizzo mail non impostato per utente "
						+ req.getId_utenti_biblioteca().getId_utenti().getCod_utente());
				return false;
			}

			MailVO mail = new MailVO();
			mail.setFrom(pair.getFrom());
			mail.getReplyTo().add(pair.getReplyTo());
			mail.getTo().add(uteMail);

			StringBuilder subject = new StringBuilder();
			subject.append("Rifiuto ");
			subject.append(
					mov.getFlTipoRec() == RichiestaRecordType.FLAG_PRENOTAZIONE ? "prenotazione n. " : "richiesta n. ");
			subject.append(codPolo);
			subject.append(mov.getCodBibDest()).append(' ');
			subject.append(req.getCod_rich_serv());
			mail.setSubject(subject.toString());

			mail.setBody(MailBodyBuilder.Servizi.rifiutoRichiesta(mov));
			return MailUtil.sendMailAsync(mail);

		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	private List<BibliotecaVO> getListaAltreBibPerAutoregistrazione(String codPolo, int idUtente) throws Exception {
		List<BibliotecaVO> output = new ArrayList();

		UtentiDAO dao = new UtentiDAO();
		List<String> listabib = dao.getListaAltreBibPerAutoregistrazione(codPolo, idUtente);

		Iterator<String> i = listabib.iterator();
		while (i.hasNext()) {
			BibliotecaVO b = new BibliotecaVO();
			b.setCod_polo(codPolo);
			b.setCod_bib(i.next());
			output.add(b);
		}

		return output;
	}

	public void fixRangeSegnature(String ticket) throws EJBException {
		try {
			checkTicket(ticket);

			SegnatureDAO dao = new SegnatureDAO();
			List<Tbl_disponibilita_precatalogati> range = dao.selectAll();
			if (!ValidazioneDati.isFilled(range))
				return;

			String uteVar = DaoManager.getFirmaUtente(ticket);
			log.debug("fix range segnature post-migrazione");

			for (Tbl_disponibilita_precatalogati dp : range) {
				String from = ValidazioneDati.trimOrEmpty(dp.getSegn_inizio());
				String to = ValidazioneDati.isFilled(dp.getSegn_fine()) ? ValidazioneDati.trimOrEmpty(dp.getSegn_fine())
						: from;
				String norm_from = ValidazioneDati.fillRight(OrdinamentoCollocazione2.normalizza(from), ' ', 80);
				String norm_to = ValidazioneDati.fillRight(OrdinamentoCollocazione2.normalizza(to), 'Z', 80);
				log.debug("normalizza: " + from + " - " + to + " --> " + norm_from + " - " + norm_to);

				dp.setSegn_da(norm_from);
				dp.setSegn_a(norm_to);
				dp.setUte_var(uteVar);
				dao.getCurrentSession().update(dp);
			}

		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	private CommandResultVO controllaMailUtente(CommandInvokeVO command) throws Exception {
		String idUtente = (String) command.getParams()[0];
		String mail1 = (String) command.getParams()[1];
		String mail2 = (String) command.getParams()[2];

		boolean hasMail1 = ValidazioneDati.isFilled(mail1);
		boolean hasMail2 = ValidazioneDati.isFilled(mail2);

		if (!hasMail1 && !hasMail2)
			// nessuna mail
			return CommandResultVO.build(command);

		if (hasMail1 && hasMail2)
			throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_DOPPIA_MAIL_UTENTE);

		UtentiDAO dao = new UtentiDAO();
		// almaviva5_20151021 #6007
		EmailValidator v = EmailValidator.getInstance();
		if (hasMail1) {
			// mail principale (univoca)
			// almaviva5_20151021 #6007
			if (!v.validate(mail1))
				throw new ValidationException(SbnErrorTypes.SRV_ERRORE_EMAIL_NON_VALIDA);

			List<Tbl_utenti> utenti = dao.checkMailUtente(idUtente, mail1, null);
			if (ValidazioneDati.isFilled(utenti))
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_EMAIL_DUPLICATA);

			if (ValidazioneDati.isFilled(idUtente)) { // utente in modifica
				UtenteBaseVO ub = ServiziConversioneVO
						.daHibernateAWebUtente(dao.getUtenteAnagraficaById(Integer.valueOf(idUtente)));
				if (ub == null)
					throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_NON_TROVATO);

				// se la mail principale è stata cambiata ed esistono utenti con questa mail
				// impostata
				// come secondaria devono essere notificati al bibliotecario.
				String oldMail = ub.getMail1();
				if (ValidazioneDati.isFilled(oldMail) && !oldMail.equalsIgnoreCase(mail1)) {
					utenti = dao.checkMailUtente(idUtente, null, oldMail);
					if (ValidazioneDati.isFilled(utenti)) {
						ArrayList<UtenteBaseVO> output = new ArrayList<UtenteBaseVO>();
						for (Tbl_utenti u : utenti)
							output.add(ServiziConversioneVO.daHibernateAWebUtente(u));

						return CommandResultVO.build(command, output);
					}
				}
			}
		}

		if (hasMail2) {
			// mail secondaria
			// almaviva5_20151021 #6007
			if (!v.validate(mail2))
				throw new ValidationException(SbnErrorTypes.SRV_ERRORE_EMAIL_NON_VALIDA);

			// deve esistere un altro utente attivo con questa mail come principale
			List<Tbl_utenti> utenti = dao.checkMailUtente(idUtente, mail2, null);
			if (!ValidazioneDati.isFilled(utenti))
				throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_EMAIL_SECONDARIA_NON_PRESENTE);
		}

		// controllo ok
		return CommandResultVO.build(command);
	}

	private CommandResultVO validaModelloSollecito(CommandInvokeVO command) throws Exception {
		ModelloSollecitoVO modello = (ModelloSollecitoVO) command.getParams()[0];
		TipoModello tipo = (TipoModello) command.getParams()[1];
		ElementoStampaSollecitoVO ess = new ElementoStampaSollecitoVO();
		ess.setNumSollecito("1");
		ess.setCodUtente("UTE0000001");
		ess.setTipoServizio("Prestito semplice");
		ess.setCap("00100");
		ess.setCitta("Roma");
		ess.setNazione("IT");
		ess.setProvincia("RM");
		ess.setNomeUtente("Utente Prova");
		ess.setInventario("XXABC000000001");
		ess.setIndirizzo("Via test 101");
		ess.setTitolo(
				"*Crittografia e sicurezza delle reti / William Stallings ; edizione italiana a cura di Luca Salgarel. - 2. ed. - "
						+ "Milano [etc.] : McGraw-Hill, ©2007 (stampa 2006). - XVIII, 694 p. ; 24 cm.");
		String data = DateUtil.formattaData(DaoManager.now());
		ess.setDataPrestito(data);
		ess.setDataScadenza(data);
		ess.setSegnatura("coll. test. 1");

		String fileName = null;
		try {
			String output = SollecitiUtil.costruisciModelloSollecito(modello, tipo, ess, true);

			if (tipo == TipoModello.LETTERA) {
				JasperPrint jp = SollecitiUtil.buildJasperPrint(output);
				fileName = FileUtil.getTempFilesDir() + File.separator + "test_soll_" + System.currentTimeMillis()
						+ ".pdf";
				JasperExportManager.exportReportToPdfFile(jp, fileName);
				BatchManager.getBatchManagerInstance().markForDeletion(new File(fileName));
			}

		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_MODELLO_SOLLECITO, e, tipo.name());
		}

		return CommandResultVO.build(command, fileName);
	}

	private CommandResultVO aggiornaRichiestaPerProroga(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		MovimentoVO mov = (MovimentoVO) command.getParams()[0];
		Date dataProroga = (Date) command.getParams()[1];
		mov.setDataProroga(new java.sql.Date(dataProroga.getTime()));

		String codPolo = mov.getCodPolo();
		String codBib = mov.getCodBibDest();
		String tipoServ = mov.getCodTipoServ();

		ParametriBibliotecaVO parBib = getParametriBiblioteca(ticket, codPolo, codBib);
		if (parBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

		ServizioBibliotecaVO servizio = getServizioBiblioteca(ticket, codPolo, codBib, tipoServ, mov.getCodServ());

		IterServizioDAO dao = new IterServizioDAO();
		IterServizioVO iter = ConversioneHibernateVO.toWeb()
				.iterServizio(dao.getIterServizio(codPolo, codBib, tipoServ, Integer.valueOf(mov.getProgrIter())));

		// controlli formali su prorogabilità
		ServiziUtil.controllaProroga(mov, parBib, servizio, iter);

		// prenotazioni pendenti
		List<MovimentoListaVO> prenotazioni = getPrenotazioni(ticket, mov, codBib, Locale.getDefault(), null);
		boolean prenotato = ValidazioneDati.isFilled(prenotazioni);

		// almaviva5_20160526 #6113 solleciti
		RichiesteServizioDAO rsdao = new RichiesteServizioDAO();
		boolean sollecitato = ValidazioneDati.isFilled(rsdao.getNumeroSollecitiById(mov.getIdRichiesta()));

		// data prorogata
		Date nuovaDataFine = mov.getDataProroga();
		nuovaDataFine = DateUtil.copiaOrario(mov.getDataFinePrev(), nuovaDataFine);

		MovimentoVO updated = null;
		char tipoRinnovo = parBib.getTipoRinnovo();

		// almaviva5_20161205 servizi ILL
		// Se si sta prorogando una richiesta ill come richiedente non è possibile
		// rinnovarla
		// in automatico perchè andrà validata dalla bib. fornitrice
		if (mov.isRichiedenteRichiestaILL())
			tipoRinnovo = '0'; // comportamento standard: la proroga sarà validata dal bibliotecario

		switch (tipoRinnovo) {
		case '0': // mai
			// comportamento standard: la proroga sarà validata dal bibliotecario
			mov.setCodStatoRic("P"); // P = attesa di proroga

			updated = aggiornaRichiesta(ticket, mov, servizio.getIdServizio());
			break;

		case '1': // sempre
			// la proroga sarà accettata automaticamente dal sistema
			// almaviva5_20160526 #6113 solo se non già sollecitato
			if (!sollecitato) {
				mov.setCodStatoRic("N"); // prorogata
				mov.setNumRinnovi((short) (mov.getNumRinnovi() + 1));
				mov.setDataFinePrev(new Timestamp(nuovaDataFine.getTime()));
			} else {
				// sollecitato, interverrà in bibliotecario
				mov.setCodStatoRic("P"); // P = attesa di proroga
			}

			updated = aggiornaRichiesta(ticket, mov, servizio.getIdServizio());
			break;

		case '2': // automatico se non esistono prenotazioni
			// almaviva5_20160526 #6113 e non sollecitato
			if (!prenotato && !sollecitato) {
				mov.setCodStatoRic("N"); // prorogata
				mov.setNumRinnovi((short) (mov.getNumRinnovi() + 1));
				mov.setDataFinePrev(new Timestamp(nuovaDataFine.getTime()));
			} else {
				// prenotazioni/solleciti pendenti
				mov.setCodStatoRic("P"); // P = attesa di proroga
			}

			updated = aggiornaRichiesta(ticket, mov, servizio.getIdServizio());
			break;

		default:
			throw new ApplicationException("errore tipoRinnovo");
		}

		// se la richiesta è stata rinnovata e ci sono prenotazioni pendenti
		// queste vanno aggiornate alla nuova data scadenza
		if (prenotato && mov.getCodStatoRic().equals("N")) {
			Timestamp nuovaFinePrev = new Timestamp(nuovaDataFine.getTime());
			for (MovimentoListaVO p : prenotazioni) {
				p = getMovimentoListaVO(ticket, p, Locale.getDefault());
				// imposto la data inizio e data fine prevista della prenotazione con la data
				// proroga
				p.setDataInizioPrev(nuovaFinePrev);
				p.setDataFinePrev(nuovaFinePrev);

				servizio = getServizioBiblioteca(ticket, p.getCodPolo(), p.getCodBibOperante(), p.getCodTipoServ(),
						p.getCodServ());

				aggiornaRichiesta(ticket, p, servizio.getIdServizio());
			}
		}

		return CommandResultVO.build(command, updated);
	}

	private CommandResultVO inviaNotificaUtentePrenotazione(CommandInvokeVO command) throws Exception {
		// almaviva5_20150701 nuova gestione prenotazioni
		String ticket = command.getTicket();
		MovimentoVO mov = (MovimentoVO) command.getParams()[0];

		String codPolo = mov.getCodPolo();
		String codBib = mov.getCodBibDest();
		ParametriBibliotecaVO parBib = getParametriBiblioteca(ticket, codPolo, codBib);
		if (parBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

		// metodo da eseguire solo alla restituzione del doc. (attivita == 04)
		// oppure, se la biblioteca gestisce la priorità sulle prenotazioni, si inoltra
		// la prenotazione alla cancellazione/rifiuto della richiesta in corso
		// almaviva5_20190419 #6944 revisione controlli 
		boolean restituito = StatoIterRichiesta.of(mov.getCodAttivita()) == StatoIterRichiesta.RESTITUZIONE_DOCUMENTO;
		boolean prioritaPrenot = parBib.isPrioritaPrenotazioni();
		boolean cancellato = ServiziUtil.movimentoCancellato(mov);
		boolean rifiutato = ServiziUtil.movimentoRifiutato(mov);

		if (!restituito) {
			if (!prioritaPrenot)
				return CommandResultVO.build(command);

			if (!rifiutato && !cancellato)
				return CommandResultVO.build(command);
		}

		// prenotazioni pendenti, ordinate per data richiesta
		List<MovimentoListaVO> prenotazioni = getPrenotazioni(ticket, mov, codBib, Locale.getDefault(),
				MovimentoListaVO.ORDINAMENTO_DATA_RICHIESTA_ASC);
		if (!ValidazioneDati.isFilled(prenotazioni))
			return CommandResultVO.build(command);

		if (!prioritaPrenot) {
			// vecchia gestione:
			// viene inviata notifica a tutti gli utenti prenotati.
			for (MovimentoListaVO p : prenotazioni) {
				inviaMailUtentePrenotazione(ticket, p);
			}
		} else {
			// nuova gestione
			for (MovimentoListaVO p : prenotazioni) {
				// ciclo sulle prenotazioni finchè una non ha i requisiti per divenire movimento
				DatiControlloVO datiControllo = gestionePrioritaPrenotazione(ticket, codPolo, codBib, p, parBib);
				if (ControlloAttivitaServizioResult.isOK(datiControllo.getResult())) {
					// nuovo movimento creato dalla prenotazione
					mov = datiControllo.getMovimento();
					break;
				}
			}

		}

		return CommandResultVO.build(command, mov);
	}

	private DatiControlloVO gestionePrioritaPrenotazione(String ticket, String codPolo, String codBib,
			MovimentoListaVO p, ParametriBibliotecaVO parBib) throws ApplicationException, Exception {
		// nuova gestione:
		// il sistema invia la mail al primo utente e passa la sua prenotazione allo
		// stato di accettata;
		p = getMovimentoListaVO(ticket, p, Locale.getDefault());
		String tipoServ = p.getCodTipoServ();

		// controlli sulla possibilità di accettare la prenotazione
		DatiControlloVO datiControllo = new DatiControlloVO(ticket, p, OperatoreType.BIBLIOTECARIO, true, null);
		// controlli base
		ControlloAttivitaServizio controlliBase = ControlloAttivitaServizio.getControlliBase();
		ControlloAttivitaServizioResult controlliBaseResult = controlliBase.controlloDefault(datiControllo);
		if (!ControlloAttivitaServizioResult.isOK(controlliBaseResult))
			return datiControllo;

		List<IterServizioVO> listaIter = getListaIterServizio(ticket, codPolo, codBib, tipoServ);
		if (!ValidazioneDati.isFilled(listaIter))
			throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_CONFIGURAZIONE_ITER);

		AttivitaServizioVO attivita = getAttivita(ticket, codPolo,
				ServiziUtil.trovaAttivitaServizioChePrecede(listaIter, "03"));

		// controlli su iter
		ControlloAttivitaServizio iter = new ControlloAttivitaServizio(attivita);
		ControlloAttivitaServizioResult checkAttivita = iter.controlloDefault(datiControllo);
		// List<String> msgSupplementari = datiControllo.getCodiciMsgSupplementari();

		if (!ControlloAttivitaServizioResult.isOK(checkAttivita))
			return datiControllo;

		List<String> messaggi = new ArrayList<String>();
		boolean esitoControllo = iter.controlloIter(datiControllo, messaggi, false, true);

		// msgSupplementari.addAll(messaggi);

		if (!esitoControllo)
			return datiControllo.setResult(ControlloAttivitaServizioResult.ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO);

		Timestamp dataInoltro = DaoManager.now();

		ControlloAttivitaServizioResult checkControlliBase = datiControllo.getResult();

		switch (checkControlliBase) {
		case OK:
		case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
		case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
			break;

		case OK_NON_ANCORA_DISPONIBILE:
			ControlloDisponibilitaVO disponibilitaVO = (ControlloDisponibilitaVO) datiControllo.getCheckData();
			Timestamp tsRitiro = disponibilitaVO.getDataPrenotazione();
			dataInoltro = tsRitiro.after(dataInoltro) ? tsRitiro : dataInoltro;
			break;

		default:
			// throw new
			// ApplicationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);
			return datiControllo;
		}

		IterServizioVO passoIter = iter.getPassoIter();
		p.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
		p.setCodStatoMov(passoIter.getCodStatoMov());
		p.setCodStatoRic(passoIter.getCodStatoRich());
		p.setCodAttivita(passoIter.getCodAttivita());
		p.setCodTipoServ(passoIter.getCodTipoServ());
		p.setProgrIter(passoIter.getProgrIter().toString());

		ServizioBibliotecaVO servizio = getServizioBiblioteca(ticket, p.getCodPolo(), p.getCodBibOperante(), tipoServ,
				p.getCodServ());

		// almaviva5_20160408 #6150
		p.setDataInizioPrev(ServiziUtil.calcolaDataInizioPrevista(parBib, servizio, dataInoltro));

		Timestamp newDataFinePrev = ServiziUtil.calcolaDataFinePrevista(servizio, p.getDataInizioPrev());
		p.setDataFinePrev(newDataFinePrev);

		MovimentoListaVO nuovoMov = (MovimentoListaVO) aggiornaRichiesta(ticket, p, servizio.getIdServizio());

		// invio mail per informare utente che la sua prenotazione è diventata
		// movimento.
		inviaMailUtentePrenotazione(ticket, nuovoMov);

		return datiControllo.setMovimento(nuovoMov);
	}

	private void cancellazioneServizioBiblioteca(String ticket, int idServizio) throws DaoManagerException {
		// almaviva5_20160622 cencellazione logica di un diritto e, in cascata, delle tabelle
		// collegate
		String firmaUtente = DaoManager.getFirmaUtente(ticket);
		ServiziDAO dao = new ServiziDAO();
		// tbl_penalita_servizio
		dao.cancellaPenalitaServizio(idServizio, firmaUtente);

		// tbl_richiesta_servizio???

		// trl_autorizzazioni_servizi
		AutorizzazioniDAO adao = new AutorizzazioniDAO();
		// per tutti i profili di autorizzazione
		adao.cancellaDirittoAutorizzazione(0, idServizio, firmaUtente);

		// trl_diritti_utente
		UtentiDAO udao = new UtentiDAO();
		udao.cancellaDirittoUtenteBiblioteca(0, idServizio, firmaUtente);

		// tbl_servizio
		dao.cancellaServizio(idServizio, firmaUtente);
		dao.flush();
	}

	private CommandResultVO cancellaTipoServizio(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		TipoServizioVO tipoServizio = (TipoServizioVO) command.getParams()[0];
		String codPolo = tipoServizio.getCodPolo();
		String codBib = tipoServizio.getCodBib();
		String codTipoServ = tipoServizio.getCodiceTipoServizio();
		String firmaUtente = DaoManager.getFirmaUtente(ticket);

		ServiziDAO dao = new ServiziDAO();
		List<Tbl_servizio> diritti = dao.getListaServiziPerTipoServizio(codPolo, codBib, codTipoServ);

		if (ValidazioneDati.isFilled(diritti))
			throw new ApplicationException(SbnErrorTypes.SRV_ERRORE_TIPO_SERVIZIO_CON_LEGAMI);

		// cancellazione in cascata

		// tbl_iter_servizio
		IterServizioDAO idao = new IterServizioDAO();
		idao.cancellaIterServizio(tipoServizio.getIdTipoServizio(), null, firmaUtente);

		// tbl_calendario_servizi
		// non gestito

		// tbl_servizio_web_dati_richiesti
		Tbl_servizio_web_dati_richiestiDAO swdrdao = new Tbl_servizio_web_dati_richiestiDAO();
		swdrdao.cancellaServizioWebDatiRichiesti(codPolo, codBib, codTipoServ, (short) 0, firmaUtente);

		// trl_tariffe_modalita_erogazione
		ModalitaErogazioneDAO medao = new ModalitaErogazioneDAO();
		List<Trl_tariffe_modalita_erogazione> tariffe = medao.getListaTariffeModalitaPerTipoServizio(codPolo, codBib,
				codTipoServ);
		for (Trl_tariffe_modalita_erogazione tariffa : tariffe) {
			tariffa.setUte_var(firmaUtente);
			medao.cancellazioneTariffeModalitaErogazione(tariffa);
		}

		// tbl_servizio
		for (Tbl_servizio diritto : diritti)
			cancellazioneServizioBiblioteca(ticket, diritto.getId_servizio());

		// tbl_tipo_servizio
		TipoServizioDAO tsdao = new TipoServizioDAO();
		tsdao.cancellazioneTipoServizio(tipoServizio.getIdTipoServizio(), firmaUtente);

		tsdao.flush();

		return CommandResultVO.build(command, true);
	}

	private CommandResultVO inviaMailNotificaNuovaRichiesta(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		MovimentoVO mov = (MovimentoVO) command.getParams()[0];

		if (!ticket.contains(Constants.UTENTE_WEB_TICKET) // solo da utente web
				|| mov.isPrenotazione()) // no prenotazione
			return CommandResultVO.build(command);

		ParametriBibliotecaVO paramBib = getParametriBiblioteca(ticket, mov.getCodPolo(), mov.getCodBibDest());
		if (paramBib == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

		String mailNotifica = paramBib.getMailNotifica();
		if (!ValidazioneDati.isFilled(mailNotifica))
			return CommandResultVO.build(command);

		log.debug("invio mail notifica nuova richiesta a: " + mailNotifica);

		Tbl_richiesta_servizio richiesta = internalGetMovimento(mov);
		MovimentoListaVO dettaglio = ServiziConversioneVO.daHibernateAWebMovimentoLista(richiesta, Locale.getDefault(),
				DaoManager.now());
		decodificaCodiciMovimento(dettaglio, mov.getCodPolo(), mov.getCodBibDest(), Locale.getDefault(), richiesta);

		InternetAddress to = new InternetAddress(mailNotifica);

		ServiziMail util = new ServiziMail();
		AddressPair pair = util.getMailBiblioteca(mov.getCodPolo(), mov.getCodBibDest());

		MailVO mail = new MailVO();
		mail.setFrom(pair.getFrom());
		mail.getReplyTo().add(pair.getReplyTo());
		mail.getTo().add(to);

		mail.setSubject("Inserimento nuova richiesta");
		mail.setBody(MailBodyBuilder.Servizi.nuovaRichiesta(dettaglio));
		try {
			MailUtil.sendMailAsync(mail);
		} catch (Exception e) {
			log.error("", e);
		}

		return CommandResultVO.build(command);
	}

	private CommandResultVO documentoNonSbnCancellabile(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		DocumentoNonSbnVO doc = (DocumentoNonSbnVO) command.getParams()[0];

		// check mov. locali
		MovimentoVO mov = new MovimentoVO();
		mov.setCodPolo(doc.getCodPolo());
		mov.setCodBibDocLett(doc.getCodBib());
		mov.setTipoDocLett(String.valueOf(doc.getTipo_doc_lett()));
		mov.setCodDocLet(doc.getCod_doc_lett() + "");
		mov.setProgrEsempDocLet("0");

		boolean check = esisteMovimentoAttivo(ticket, mov);
		if (check)
			// ci sono movimenti attivi, non cancellabile
			return CommandResultVO.build(command, false);

		// check mov. ill
		RichiesteServizioDAO dao = new RichiesteServizioDAO();
		List<Tbl_dati_richiesta_ill> richiesteILL = dao.getListaDatiRichiestaIll(0, null, null, null, null, null, null,
				null, doc.getIdDocumento());

		boolean cancellabile = !ValidazioneDati.isFilled(richiesteILL);

		return CommandResultVO.build(command, cancellabile);
	}

}
