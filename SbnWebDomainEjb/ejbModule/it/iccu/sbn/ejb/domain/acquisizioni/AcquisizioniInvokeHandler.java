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

import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsNot.not;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeStrumentiPatrimonio;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.acquisizioni.OrdiniUtil;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.ElementoStampaInvOrdineRilegaturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.OrdineStampaOnlineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.StampaCartRoutingSheetVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.StampaShippingManifestVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_cambi_ufficialiDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tbb_bilanciDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.CsvWriter2;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.custom.acquisizioni.ordini.ElementoStampaInvOrdineRilegaturaDecorator;
import it.iccu.sbn.vo.validators.acquisizioni.StrutturaInventariOrdValidator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import ch.lambdaj.Lambda;
import ch.lambdaj.group.Group;


public class AcquisizioniInvokeHandler extends SerializableVO {

	private static final long serialVersionUID = -1881235648589534035L;

	private static Logger log = Logger.getLogger(Acquisizioni.class);

	private final AcquisizioniBean ejb;
	private final GenericJDBCAcquisizioniDAO dao;

	private AcquisizioniBMT getBMT() throws Exception {
		return DomainEJBFactory.getInstance().getAcquisizioniBMT();
	}

	SBNStampeStrumentiPatrimonio getEjbStrumentiPatrimonio() throws Exception {
		return DomainEJBStampeFactory.getInstance().getSBNStampeStrumentiPatrimonio();
	}

	public AcquisizioniInvokeHandler(AcquisizioniBean ejb, GenericJDBCAcquisizioniDAO dao) {
		this.ejb = ejb;
		this.dao = dao;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException {
		try {
			switch (command.getCommand() ) {
			case ACQ_SPEDISCI_ORDINE_RILEGATURA:
				return spedisciOrdineRilegatura(command);

			case ACQ_STAMPA_ORDINE_RILEGATURA:
				return stampaOrdineRilegatura(command);

			case ACQ_STAMPA_SHIPPING_MANIFEST:
				return stampaShippingManifest(command);

			case ACQ_STAMPA_CART_ROUTING_SHEET:
				return stampaCartRoutingSheet(command);

			case ACQ_STAMPA_MODULO_PRELIEVO:
				return stampaOnlineModuloPrelievo(command);

			case ACQ_ESISTE_LEGAME_RIGA_BILANCIO:
				return esisteLegameRigaBilancio(command);

			case ACQ_ESISTE_INVENTARIO_DIGITALIZZATO:
				return esisteInventarioDigitalizzato(command);

			case ACQ_CERCA_VALUTA_RIFERIMENTO:
				//almaviva5_20140612 #5078
				return cercaValutaRiferimento(command);

			case ACQ_IMPORTO_INVENTARI_ORDINE:
				//almaviva5_20140618 #4967
				return importoInventariOrdine(command);

			case ACQ_COUNT_INVENTARI_ORDINE:
				return countInventariOrdine(command);

			default:
				return null;
			}

		} catch (TicketExpiredException e) {
			throw new ApplicationException(SbnErrorTypes.TICKET_EXPIRED);
		} catch	(ApplicationException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			ejb.ctx.setRollbackOnly();
			if (e instanceof SbnBaseException)
				return CommandResultVO.build(command, null, e);
			else
				return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

	private CommandResultVO cercaValutaRiferimento(CommandInvokeVO command) throws Exception {
		Tba_cambi_ufficialiDao dao = new Tba_cambi_ufficialiDao();
		Serializable[] params = command.getParams();
		String codPolo = (String) params[0];
		String codBib = (String) params[1];
		Tba_cambi_ufficiali cu = dao.cercaValutaRiferimento(codPolo, codBib);
		CambioVO cambio = null;
		if (cu != null)
			cambio = ConversioneHibernateVO.toWeb().cambio(cu);

		return CommandResultVO.build(command, cambio, null);
	}

	private CommandResultVO stampaCartRoutingSheet(CommandInvokeVO command) throws Exception {
		Serializable[] params = command.getParams();
		OrdiniVO ordine = (OrdiniVO) params[0];

		StampaCartRoutingSheetVO output = new StampaCartRoutingSheetVO(ordine);
		Tba_ordiniDao odao = new Tba_ordiniDao();
		Object[] count = odao.countInventariRilegatura(ordine.getIDOrd());

		output.setInventari(((Number)count[0]).intValue());
		output.setVolumi(((Number)count[1]).intValue());

		ConfigurazioneORDVO richiesta = new ConfigurazioneORDVO(ordine);
		ConfigurazioneORDVO config = dao.loadConfigurazioneOrdini(richiesta);
		if (config == null)
			throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

		String cd_bib_google = config.getCd_bib_google();
		if (!ValidazioneDati.isFilled(cd_bib_google))
			throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

		output.setCd_bib_google(cd_bib_google);

		return CommandResultVO.build(command, output, null);
	}

	private Inventario getInventario() throws Exception {
		return DomainEJBFactory.getInstance().getInventario();
	}

	private boolean aggiornaDisponibilitaInventari(String ticket,
			OrdiniVO ordine, List<StrutturaInventariOrdVO> inventari)
			throws ApplicationException {
		try {
			List<StrutturaQuinquies> listaPerDF = new ArrayList<StrutturaQuinquies>();
			for (StrutturaInventariOrdVO inv : inventari) {
				StrutturaQuinquies eleListPerDF = new StrutturaQuinquies();
				eleListPerDF.setCodice1(inv.getSerie());
				eleListPerDF.setCodice2(inv.getNumero());
				eleListPerDF.setCodice3(inv.getDataRientroPresunta());
				// esame se data rientro valorizzata
				if (ValidazioneDati.isFilled(inv.getDataRientro()) )
					eleListPerDF.setCodice4("R");
				else
					eleListPerDF.setCodice4(null);

				//almaviva5_20130910 evolutive google2
				eleListPerDF.setCodice5(Boolean.toString(inv.isFlag_canc()));
				//

				listaPerDF.add(eleListPerDF);
			}

			AggDispVO richiesta = new AggDispVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);

			richiesta.setCodPolo(ordine.getCodPolo());
			richiesta.setCodBib(ordine.getCodBibl());
			richiesta.setListaInventari(listaPerDF);

			//almaviva5_20121116 evolutive google
			String cd_tipo_lav = ordine.getCd_tipo_lav();
			richiesta.setCodNoDispo(cd_tipo_lav);
			TB_CODICI c = CodiciProvider.cercaCodice(cd_tipo_lav, CodiciType.CODICE_NON_DISPONIBILITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (c == null)
				throw new ApplicationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			richiesta.setCodDigitalizzazione(ValidazioneDati.trimOrEmpty(c.getCd_flg4()));

			return getInventario().aggiornaInventarioDisponibilita(richiesta, ticket);

		} catch (Exception e) {
			if (e.getMessage().equals("inventarioDismesso")) // tck 3732
				throw new ApplicationException(SbnErrorTypes.GDF_INVENTARIO_DISMESSO);
		}

		return false;
	}

	private CommandResultVO stampaShippingManifest(CommandInvokeVO command) throws Exception {
		StampaShippingManifestVO richiesta = (StampaShippingManifestVO) command.getParams()[0];
		BatchLogWriter blw = (BatchLogWriter) command.getParams()[1];
		Logger _log = blw.getLogger();
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);

		Tba_ordiniDao odao = new Tba_ordiniDao();

		OrdineCarrelloSpedizioneVO ocs = richiesta.getOrdineCarrelloSpedizione();
		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();
		List<Tra_ordine_carrello_spedizione> ss = odao.getSpedizioni(codPolo, codBib, ocs.getDataSpedizione(), ocs.getPrgSpedizione());

		Tra_ordine_carrello_spedizione p = Lambda.on(Tra_ordine_carrello_spedizione.class);
		Group<Tra_ordine_carrello_spedizione> groups = Lambda.group(ss, Lambda.by(p.getPrg_spedizione()) );
		//group by su progressivo spedizione
		for (Group<Tra_ordine_carrello_spedizione> g : groups.subgroups() ) {
			//ciclo sulle spedizioni
			Date dt_spedizione = g.first().getDt_spedizione();
			short prg_spedizione = g.first().getPrg_spedizione();
			_log.debug(String.format("produzione elenco carrelli per spedizione n. %d del %tF.", prg_spedizione, dt_spedizione));

			//almaviva5_20130121 evolutive google
			ConfigurazioneORDVO covo = new ConfigurazioneORDVO();
			covo.setCodPolo(codPolo);
			covo.setCodBibl(codBib);
			covo.setTicket(command.getTicket());
			ConfigurazioneORDVO config = dao.loadConfigurazioneOrdini(covo);
			if (config == null)
				throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

			String cd_bib_google = config.getCd_bib_google();
			if (!ValidazioneDati.isFilled(cd_bib_google))
				throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

			String filename = OrdiniUtil.formattaShippingManifest(cd_bib_google, dt_spedizione, prg_spedizione);
			FileWriter fw = new FileWriter(StampeUtil.getBatchFilesPath() + File.separator + filename);
			//almaviva5_20130305 evolutive google
			char sep = CommonConfiguration.getProperty(Configuration.CSV_FIELD_SEPARATOR,
					String.valueOf(Constants.CSV_DEFAULT_SEPARATOR)).charAt(0);
			_log.debug("separatore configurato per campo CSV: '" + sep + "'");
			CsvWriter2 csv = new CsvWriter2(fw, sep);

			for (Tra_ordine_carrello_spedizione s : g.findAll() ) {
				//ciclo sui carrelli
				String cart_name = ValidazioneDati.trimOrEmpty(s.getCart_name());
				_log.debug(String.format("stampa inventari per carrello: '%s'.", cart_name));
				List<Tra_ordine_inventari> ii = odao.getListaInventariRilegatura(s.getIdOrdine());
				for (Tra_ordine_inventari i : ii) {
					//ciclo su inventari e scrittura csv
					csv.write(cart_name);
					csv.write(ConversioneHibernateVO.toWeb().chiaveInventario(i.getCd_polo()));
					csv.endRecord();
				}
			}

			FileUtil.flush(csv);
			FileUtil.close(csv);
			output.addDownload(filename, filename);
		}
		output.setStato(ConstantsJMS.STATO_OK);

		return CommandResultVO.build(command, output, null);
	}


	private CommandResultVO stampaOrdineRilegatura(CommandInvokeVO command) throws Exception {
		Serializable[] params = command.getParams();
		OrdiniVO ordine = (OrdiniVO) params[0];

		FornitoreVO fornitore = ValidazioneDati.first(dao.getRicercaListaFornitori(new ListaSuppFornitoreVO(ordine)));
		OrdineCarrelloSpedizioneVO carrello = ordine.getOrdineCarrelloSpedizione();
		String chiaveOrdine = ordine.getChiaveOrdine();

		ConfigurazioneBOVO confBO = new ConfigurazioneBOVO(ordine);
		confBO = ejb.loadConfigurazione(confBO);
		if (confBO == null)
			throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

		String lingua = "ITA";
		String tipoOrdine = ordine.getTipoOrdine();
		String oggetto = confBO.leggiOgg(lingua, tipoOrdine);
		String intro = confBO.leggiIntro(lingua, tipoOrdine, ordine.getCd_tipo_lav());
		List<String> intestazione = impostaIntestazione(confBO);
		String descrBib = ValidazioneDati.first(intestazione);
		intestazione = CaricamentoCombo.cutFirst(intestazione);
		String intest = ValidazioneDati.formatValueList(intestazione, "\n");
		//almaviva5_20140529 #4527
		String note = ordine.getNoteFornitore();

		Tba_ordiniDao odao = new Tba_ordiniDao();
		List<Tra_ordine_inventari> inventari = odao.getListaInventariRilegatura(ordine.getIDOrd());

		ArrayList<ElementoStampaInvOrdineRilegaturaVO> output = new ArrayList<ElementoStampaInvOrdineRilegaturaVO>();

		for (Tra_ordine_inventari oi : inventari) {
			ElementoStampaInvOrdineRilegaturaVO esio = new ElementoStampaInvOrdineRilegaturaDecorator(
					ConversioneHibernateVO.toWeb().elementoStampaInventarioOrdine(oi));
			esio.setChiaveOrdine(chiaveOrdine);
			esio.setFornitore(fornitore);
			esio.setCarrello(carrello);
			esio.setTestoOggetto(oggetto);
			esio.setTestoIntroduttivo(intro);
			esio.setListaDatiIntestazione(intest);
			esio.setDescrBib(descrBib);
			//almaviva5_20140529 #4527
			esio.setNote(note);
			output.add(esio);
		}

//		if (!ordine.isGoogle() && !ordine.isStampato() ) {
//			ordine.setStampato(true);
//			if (!ejb.modificaOrdine(ordine))
//				throw new ApplicationException(SbnErrorTypes.ACQ_ERRORE_MODIFICA);
//		}

		return CommandResultVO.build(command, output, null);
	}

	private List<String> impostaIntestazione(ConfigurazioneBOVO conf) {
		List<String> output = new ArrayList<String>();
		for (StrutturaTerna st : conf.getListaDatiIntestazione() )
			output.add(st.getCodice2());

		return output;
	}

	@SuppressWarnings("unchecked")
	private CommandResultVO spedisciOrdineRilegatura(CommandInvokeVO command) throws Exception {
		String ticket = command.getTicket();
		Serializable[] params = command.getParams();
		OrdiniVO ordine = (OrdiniVO) params[0];

		boolean google = ordine.isGoogle();
		String codPolo = ordine.getCodPolo();
		String codBib = ordine.getCodBibl();

		OrdineCarrelloSpedizioneVO ocs = ordine.getOrdineCarrelloSpedizione();

		//configurazione ordini
		ConfigurazioneORDVO richiesta = new ConfigurazioneORDVO(ordine);
		ConfigurazioneORDVO config = dao.loadConfigurazioneOrdini(richiesta);
		if (config == null)
			throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);

		String cd_bib_google = null;
		if (google) {
			cd_bib_google = config.getCd_bib_google();
			if (!ValidazioneDati.isFilled(cd_bib_google))
				throw new ApplicationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_INCOMPLETA);
		}

		ejb.ValidaOrdiniVO(ordine);

		List<StrutturaInventariOrdVO> inventari = ordine.getRigheInventariRilegatura();
		String dataUscita = DateUtil.formattaData(ocs.getDataSpedizione());
		Validator<StrutturaInventariOrdVO> v = new StrutturaInventariOrdValidator(ordine);
		for (StrutturaInventariOrdVO i : inventari) {
			i.setDataUscita(dataUscita);
			i.validate(v);
		}

		if (!aggiornaDisponibilitaInventari(ticket, ordine, inventari) )
			throw new ApplicationException(SbnErrorTypes.ACQ_ERRORE_DISPONIBILITA_INVENTARI_RILEGATURA);

		if (google) {
			String cartName = getBMT().calcolaCartName(ticket, codPolo, codBib, cd_bib_google);
			log.debug("cart_name calcolato: " + cartName);
			ocs.setCartName(cartName);
		} else {
			ocs.setPrgSpedizione((short) 0);
			ocs.setCartName(null);
		}

		//aggiornamento ordine
		ordine.setStampato(true);
		ordine.setUtente(DaoManager.getFirmaUtente(ticket));
		boolean updated = ejb.modificaOrdine(ordine);
		if (!updated)
			return CommandResultVO.build(command, null, null);

		ListaSuppOrdiniVO ricerca = new ListaSuppOrdiniVO(ordine);
		List<OrdiniVO> ordini = ejb.getRicercaListaOrdini(ricerca);
		if (!ValidazioneDati.isFilled(ordini))
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

		return CommandResultVO.build(command, ValidazioneDati.first(ordini), null);
	}

	public static void main(String[] args) {
		System.out.println(String.format("%tF", new Date()) );
	}

	private CommandResultVO stampaOnlineModuloPrelievo(CommandInvokeVO command) throws Exception {
		Serializable[] params = command.getParams();
		OrdineStampaOnlineVO oso = (OrdineStampaOnlineVO) params[0];
		OrdiniVO ordine = oso.getOrdine();

		StampaOnLineVO sol = oso;//new StampaOnLineVO();
		sol.setTicket(command.getTicket());
		sol.setDatiStampa(ValidazioneDati.asSingletonList(ordine));
		Object output = getEjbStrumentiPatrimonio().elabora(sol, null);

		return CommandResultVO.build(command, (Serializable) output, null);
	}

	private CommandResultVO esisteLegameRigaBilancio(CommandInvokeVO command) throws Exception {
		//almaviva5_20130604 #4757
		Serializable[] params = command.getParams();
		int idBilancio = (Integer) params[0];
		BilancioDettVO riga = (BilancioDettVO) params[1];

		Tbb_bilanciDao dao = new Tbb_bilanciDao();
		boolean exists = dao.esisteLegameRigaBilancio(idBilancio, riga.getImpegno().charAt(0));
		return CommandResultVO.build(command, exists, null);
	}

	private CommandResultVO esisteInventarioDigitalizzato(CommandInvokeVO command) throws Exception {
		List<InventarioVO> result = new ArrayList<InventarioVO>();

		StrutturaInventariOrdVO inv = (StrutturaInventariOrdVO) command.getParams()[0];
		@SuppressWarnings("unchecked")
		List<String> invEsclusi = (List<String>) command.getParams()[1];

		Tbc_inventarioDao dao = new Tbc_inventarioDao();
		List<Tbc_inventario> inventari = dao.getListaAltriInventariDigitalizzati(inv.getCodPolo(),
				inv.getCodBibl(), inv.getBid(), inv.getSerie(), inv.getNumero());

		if (isFilled(inventari)) {
			for (Tbc_inventario i : inventari)
				result.add(ConversioneHibernateVO.toWeb().inventario(null, i, Locale.getDefault()));

			InventarioVO p = on(InventarioVO.class);
			result = select(result, having(p.getChiaveInventario(), not(isIn(invEsclusi))));
		}

		return CommandResultVO.build(command, new ArrayList<InventarioVO>(result), null);
	}

	private CommandResultVO importoInventariOrdine(CommandInvokeVO command)	throws Exception {
		// almaviva5_20140618 #4967
		OrdiniVO o = (OrdiniVO) command.getParams()[0];
		Tba_ordiniDao dao = new Tba_ordiniDao();
		BigDecimal sum = dao.importoInventariOrdine(o.getCodPolo(),
				o.getCodBibl(), o.getTipoOrdine(),
				Integer.valueOf(o.getAnnoOrdine()),
				Integer.valueOf(o.getCodOrdine()));

		return CommandResultVO.build(command, sum, null);
	}

	private CommandResultVO countInventariOrdine(CommandInvokeVO command) throws Exception {
		// almaviva5_20140618 #4967
		OrdiniVO o = (OrdiniVO) command.getParams()[0];
		Tba_ordiniDao dao = new Tba_ordiniDao();
		int cntInv = dao.countInventariOrdine(o.getCodPolo(),
				o.getCodBibl(), o.getTipoOrdine(),
				Integer.valueOf(o.getAnnoOrdine()),
				Integer.valueOf(o.getCodOrdine()));

		return CommandResultVO.build(command, cntInv, null);
	}
}
