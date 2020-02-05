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
package it.iccu.sbn.util.ConvertiVo;

import static it.iccu.sbn.util.sbnmarc.SBNMarcUtil.livelloSoglia;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.utils.isbd.IsbdTokenizer;
import it.iccu.sbn.ejb.utils.isbd.IsbdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.ElementoStampaInvOrdineRilegaturaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioListeVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineLegameTitoloSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineModificaSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO.OrigineSoggetto;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO.ModelloStampaType;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.CatenaRinnoviOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieEsemplareCollVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ElementoStampaSollecitoVO.TipoUtente;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.validation.JSScriptVO;
import it.iccu.sbn.periodici.model.previsionale.DateFascicoloType;
import it.iccu.sbn.periodici.model.previsionale.DatiPeriodicoType;
import it.iccu.sbn.periodici.model.previsionale.PeriodicoNumType;
import it.iccu.sbn.periodici.model.previsionale.PrevisionaleType;
import it.iccu.sbn.periodici.model.previsionale.SbnPeriodico;
import it.iccu.sbn.periodici.model.previsionale.types.GiornoSettimanaType;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_cambi_ufficialiDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.documentofisico.Trc_formati_sezioniDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.ServiziIllDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_js_script;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe;
import it.iccu.sbn.polo.orm.bibliografica.Tb_numero_std;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Trc_formati_sezioni;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sogp_sogi;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_termini_termini;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_tit_cla;
import it.iccu.sbn.polo.orm.gestionesemantica.Trs_termini_titoli_biblioteche;
import it.iccu.sbn.polo.orm.periodici.Tbp_esemplare_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_modello_previsionale;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_messaggio;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Vl_richiesta_servizio;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants;
import it.iccu.sbn.util.TitoloHibernate;
import it.iccu.sbn.util.ConvertiVo.calendario.CalendarioToWeb;
import it.iccu.sbn.util.ConvertiVo.sale.SaleToWeb;
import it.iccu.sbn.util.ConvertiVo.utenti.UtentiToWeb;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.periodici.UnionEsamePeriodico;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.ServiziConstant;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.exolab.castor.types.GMonthDay;

import com.google.gson.reflect.TypeToken;

public final class ConvertToWeb extends DataBindingConverter {

	private static final long serialVersionUID = 5588610933500096276L;

	private static final String TBF_MODELLO_STAMPA_SEPARATORE = ",";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static CalendarioToWeb Calendario = new CalendarioToWeb();
	public static SaleToWeb Sale = new SaleToWeb();

	protected ConvertToWeb() {
		super();
	}

	public final BibliotecaVO anagraficaBiblioteca(Tbf_biblioteca hibVO) {
		BibliotecaVO webVO = new BibliotecaVO();
		webVO.setIdBiblioteca(hibVO.getId_biblioteca());
		webVO.setCd_ana_biblioteca(hibVO.getCd_ana_biblioteca());
		webVO.setCod_polo(hibVO.getCd_polo());
		webVO.setCod_bib(hibVO.getCd_bib());
		webVO.setNom_biblioteca(hibVO.getNom_biblioteca());
		String tipoBib = String.valueOf(hibVO.getTipo_biblioteca());
		webVO.setTipo_biblioteca(tipoBib);
		webVO.setChiave_ente(hibVO.getChiave_ente());
		webVO.setUnit_org(hibVO.getUnit_org());
		webVO.setCod_fiscale(hibVO.getCd_fiscale());
		webVO.setP_iva(hibVO.getP_iva());
		webVO.setIndirizzo(hibVO.getIndirizzo());
		webVO.setCap(hibVO.getCap());
		webVO.setCpostale(hibVO.getCpostale());
		webVO.setLocalita(hibVO.getLocalita());
		webVO.setProvincia(hibVO.getProvincia());
		webVO.setPaese(hibVO.getPaese());
		webVO.setE_mail(hibVO.getE_mail());
		webVO.setTelefono(hibVO.getTelefono());
		webVO.setFax(hibVO.getFax());
		webVO.setCod_bib_cs(hibVO.getCd_bib_cs());
		webVO.setCod_bib_ut(hibVO.getCd_bib_ut());
		webVO.setNote(hibVO.getNote());

		webVO.setCod_utente( hibVO.getCd_utente() == null ? 0L : hibVO.getCd_utente().longValue() );

		webVO.setFlag_bib(String.valueOf(hibVO.getFlag_bib()));
		webVO.setChiave_bib(hibVO.getChiave_bib());
		webVO.setChiave_ente(hibVO.getChiave_ente());
		webVO.setFlCanc(String.valueOf(hibVO.getFl_canc()));

		webVO.setTsIns(hibVO.getTs_ins());
		webVO.setTsVar(hibVO.getTs_var());
		webVO.setUteIns(hibVO.getUte_ins());
		webVO.setUteVar(hibVO.getUte_var());

		webVO.setInserito(0);

		return webVO;
	}

	public final ElementoStampaClassificazioneVO elementoStampaClassificazione(Tb_classe classe, List<Tr_tit_cla> titoliLegati) {
		ElementoStampaClassificazioneVO webVO = new ElementoStampaClassificazioneVO();

		webVO.setId(classe.getCd_sistema() + classe.getCd_edizione() + classe.getClasse());
		webVO.setTesto(classe.getDs_classe() );
		webVO.setLivelloAutorita(livelloSoglia(classe.getCd_livello()));

		if (titoliLegati != null && titoliLegati.size() > 0) {

			List<LegameTitoloVO> tmp = new ArrayList<LegameTitoloVO>();

			for (Tr_tit_cla legame : titoliLegati) {
				Tb_titolo titolo = legame.getB();
				LegameTitoloVO datiLegame = new LegameTitoloVO();
				datiLegame.setBid(titolo.getBid());
				datiLegame.setTitolo(titolo.getIsbd());
				tmp.add(datiLegame);
			}
			webVO.setLegamiTitoli(tmp);
		}

		return webVO;
	}

	public final ElementoStampaTerminiThesauroVO elementoStampaTerminiThesauro(
			Tb_termine_thesauro termine, List<Trs_termini_titoli_biblioteche> titoliLegati, List<Tr_termini_termini> terminiLegati) {
		ElementoStampaTerminiThesauroVO webVO = new ElementoStampaTerminiThesauroVO();

		webVO.setId(termine.getCd_the() + " " + termine.getDid());
		webVO.setTesto(termine.getDs_termine_thesauro());
		//webVO.setLivelloAutorita(livelloSoglia(termine.getCd_livello()));

		if (titoliLegati != null && titoliLegati.size() > 0) {

			List<LegameTitoloVO> tmp = new ArrayList<LegameTitoloVO>();

			for (Trs_termini_titoli_biblioteche legame : titoliLegati) {
				Tb_titolo titolo = legame.getB();
				LegameTitoloVO datiLegame = new LegameTitoloVO();
				datiLegame.setBid(titolo.getBid());
				datiLegame.setTitolo(titolo.getIsbd());
				tmp.add(datiLegame);
			}
			webVO.setLegamiTitoli(tmp);
		}

		if (terminiLegati != null && terminiLegati.size() > 0) {

			List<LegameTermineVO> tmp = new ArrayList<LegameTermineVO>();

			for (Tr_termini_termini legame : terminiLegati) {
				Tb_termine_thesauro term = legame.getDid_coll();
				LegameTermineVO datiLegame = new LegameTermineVO();
				datiLegame.setDid(term.getDid());
				datiLegame.setTesto(term.getDs_termine_thesauro());
				datiLegame.setTipoLegame(legame.getTipo_coll());
				datiLegame.setNotaLegame(legame.getNota_termini_termini());
				tmp.add(datiLegame);
			}
			webVO.setLegamiTermini(tmp);
		}

		return webVO;
	}

	public final ElementoStampaDescrittoreVO elementoStampaDescrittore(Tb_descrittore descrittore, String tipoLegame) {
		ElementoStampaDescrittoreVO webVO = new ElementoStampaDescrittoreVO();
		webVO.setId(descrittore.getDid() );
		webVO.setTesto(descrittore.getDs_descrittore() );
		webVO.setCodSoggettario(descrittore.getCd_soggettario());
		webVO.setLivelloAutorita(livelloSoglia(descrittore.getCd_livello()));
		webVO.setTipoLegame(tipoLegame);

		//almaviva5_20120604 evolutive CFI
		Character edizione = descrittore.getCd_edizione();
		webVO.setEdizioneSoggettario(edizione != null ? edizione.toString() : "");

		return webVO;
	}

	public final ElementoStampaDescrittoreVO elementoStampaDescrittoreDecodificato(Tb_descrittore descrittore) throws Exception {
		ElementoStampaDescrittoreVO webVO = new ElementoStampaDescrittoreVO();
		webVO.setId(descrittore.getDid() );
		webVO.setTesto(descrittore.getDs_descrittore() );

		//almaviva5_20120604 evolutive CFI
		String soggettario = CodiciProvider.cercaDescrizioneCodice(descrittore.getCd_soggettario(),
				CodiciType.CODICE_SOGGETTARIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		String edizione = CodiciProvider.cercaDescrizioneCodice(descrittore.getCd_edizione(),
				CodiciType.CODICE_EDIZIONE_SOGGETTARIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		webVO.setCodSoggettario(soggettario);
		webVO.setEdizioneSoggettario(edizione != null ? edizione : "\u00A0");

		webVO.setLivelloAutorita(CodiciProvider.cercaDescrizioneCodice(livelloSoglia(descrittore.getCd_livello()),
				CodiciType.CODICE_LIVELLO_AUTORITA,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		webVO.setFormaNome(CodiciProvider.cercaDescrizioneCodice(String.valueOf(descrittore.getTp_forma_des()),
				CodiciType.CODICE_FORMA_DESCRITTORE,
				CodiciRicercaType.RICERCA_CODICE_SBN));

		return webVO;
	}

	public final ElementoStampaSoggettoVO elementoStampaSoggetto(Tb_soggetto soggetto) {
		ElementoStampaSoggettoVO webVO = new ElementoStampaSoggettoVO();
		webVO.setId(soggetto.getCid() );
		webVO.setTesto(soggetto.getDs_soggetto() );
		webVO.setCodSoggettario(soggetto.getCd_soggettario());
		webVO.setLivelloAutorita(livelloSoglia(soggetto.getCd_livello()));
		webVO.setTipoSoggetto(String.valueOf(soggetto.getCat_sogg()) );

		//almaviva5_20120604 evolutive CFI
		Character edizione = soggetto.getCd_edizione();
		webVO.setEdizioneSoggettario(edizione != null ? edizione.toString() : "\u00A0");

		return webVO;
	}

	public final ElementoStampaSoggettoVO elementoStampaSoggettoDecodificato(Tb_soggetto soggetto) throws Exception {
		ElementoStampaSoggettoVO webVO = new ElementoStampaSoggettoVO();
		webVO.setId(soggetto.getCid() );
		webVO.setTesto(soggetto.getDs_soggetto() );

		//almaviva5_20120604 evolutive CFI
		String soggettario = CodiciProvider.cercaDescrizioneCodice(soggetto.getCd_soggettario(),
				CodiciType.CODICE_SOGGETTARIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		String edizione = CodiciProvider.cercaDescrizioneCodice(soggetto.getCd_edizione(),
				CodiciType.CODICE_EDIZIONE_SOGGETTARIO,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		webVO.setCodSoggettario(soggettario);
		webVO.setEdizioneSoggettario(edizione != null ? edizione : "");

		webVO.setLivelloAutorita(CodiciProvider.cercaDescrizioneCodice(livelloSoglia(soggetto.getCd_livello()),
				CodiciType.CODICE_LIVELLO_AUTORITA,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		webVO.setTipoSoggetto(CodiciProvider.cercaDescrizioneCodice(String.valueOf(soggetto.getCat_sogg()),
				CodiciType.CODICE_CATEGORIA_SOGGETTO,
				CodiciRicercaType.RICERCA_CODICE_SBN));

		return webVO;
	}

	public final DatiCondivisioneSoggettoVO datiCondivisioneSoggetto(Tr_sogp_sogi tr_sogp_sogi) {
		DatiCondivisioneSoggettoVO webVO = new DatiCondivisioneSoggettoVO();
		webVO.setUteIns(tr_sogp_sogi.getUte_ins());
		webVO.setUteVar(tr_sogp_sogi.getUte_var());
		webVO.setTsIns(tr_sogp_sogi.getTs_ins());
		webVO.setTsVar(tr_sogp_sogi.getTs_var());
		webVO.setFlCanc(String.valueOf(tr_sogp_sogi.getFl_canc()) );

		webVO.setCidPolo(tr_sogp_sogi.getCid_p().getCid() );
		webVO.setCidIndice(tr_sogp_sogi.getCid_i() );
		webVO.setBid(tr_sogp_sogi.getBid() );
		if (tr_sogp_sogi.getFl_imp_sog() == 'I')
			webVO.setOrigineSoggetto(OrigineSoggetto.INDICE);
		else
			webVO.setOrigineSoggetto(OrigineSoggetto.POLO);

		Character fl_legame_imp = tr_sogp_sogi.getFl_imp_tit_sog();
		if (fl_legame_imp == null)
			webVO.setOrigineLegame(OrigineLegameTitoloSoggetto.NESSUNO);
		else
			if (fl_legame_imp == 'I')
				webVO.setOrigineLegame(OrigineLegameTitoloSoggetto.INDICE);
			else
				webVO.setOrigineLegame(OrigineLegameTitoloSoggetto.POLO);

		Character fl_sogp_mod = tr_sogp_sogi.getFl_sog_mod_da();
		if (fl_sogp_mod == null)
			webVO.setOrigineModifica(OrigineModificaSoggetto.NESSUNO);
		else
			if (fl_sogp_mod == 'I')
				webVO.setOrigineModifica(OrigineModificaSoggetto.INDICE);
			else
				webVO.setOrigineModifica(OrigineModificaSoggetto.POLO);

		return webVO;
	}

	public final ParametriBibliotecaVO parametriBiblioteca(Tbl_parametri_biblioteca hVO) throws Exception {
		ParametriBibliotecaVO webVO = new ParametriBibliotecaVO();

		if (hVO != null) {
			webVO.setId(hVO.getId_parametri_biblioteca());
			webVO.setCodBib(hVO.getCd_bib().getCd_biblioteca());
			webVO.setCodPolo(hVO.getCd_bib().getCd_polo().getCd_polo());

			webVO.setGgRitardo1(hVO.getNum_gg_ritardo1());
			webVO.setGgRitardo2(hVO.getNum_gg_ritardo2());
			webVO.setGgRitardo3(hVO.getNum_gg_ritardo3());
			webVO.setNumeroLettere(hVO.getNum_lettere());
			webVO.setNumeroPrenotazioni(hVO.getNum_prenotazioni());

			webVO.setTsIns(hVO.getTs_ins());
			webVO.setTsVar(hVO.getTs_var());
			webVO.setUteIns(hVO.getUte_ins());
			webVO.setUteVar(hVO.getUte_var());
			webVO.setFlCanc("N");

			String catFrui = hVO.getCd_catfrui_nosbndoc();
			webVO.setCodFruizione(ValidazioneDati.isFilled(catFrui) ? catFrui : "  ");

			String catRiproduzione = hVO.getCd_catriprod_nosbndoc();
			webVO.setCodRiproduzione(ValidazioneDati.isFilled(catRiproduzione) ? catRiproduzione : "  ");

			char fl_catfrui = hVO.getFl_catfrui_nosbndoc();
			webVO.setCodFruiDaIntervallo(fl_catfrui == 'S');
			// la precedente istruzione è analoga a quelle successive
			//if (fl_catfrui == 'S') {
			//	webVO.setCodFruiDaIntervallo(true);
			//}
			//else {
			//	webVO.setCodFruiDaIntervallo(false);
			//}

			Character cod_modalita_invio1_sollecito1 = hVO.getCod_modalita_invio1_sollecito1();
			webVO.setCodModalitaInvio1Sollecito1(ValidazioneDati.isFilled(cod_modalita_invio1_sollecito1) ? cod_modalita_invio1_sollecito1.toString() : "");

			Character cod_modalita_invio2_sollecito1 = hVO.getCod_modalita_invio2_sollecito1();
			webVO.setCodModalitaInvio2Sollecito1(ValidazioneDati.isFilled(cod_modalita_invio2_sollecito1) ? cod_modalita_invio2_sollecito1.toString() : "");

			Character cod_modalita_invio3_sollecito1 = hVO.getCod_modalita_invio3_sollecito1();
			webVO.setCodModalitaInvio3Sollecito1(ValidazioneDati.isFilled(cod_modalita_invio3_sollecito1) ? cod_modalita_invio3_sollecito1.toString() : "");

			Character cod_modalita_invio1_sollecito2 = hVO.getCod_modalita_invio1_sollecito2();
			webVO.setCodModalitaInvio1Sollecito2(ValidazioneDati.isFilled(cod_modalita_invio1_sollecito2) ? cod_modalita_invio1_sollecito2.toString() : "");

			Character cod_modalita_invio2_sollecito2 = hVO.getCod_modalita_invio2_sollecito2();
			webVO.setCodModalitaInvio2Sollecito2(ValidazioneDati.isFilled(cod_modalita_invio2_sollecito2) ? cod_modalita_invio2_sollecito2.toString() : "");

			Character cod_modalita_invio3_sollecito2 = hVO.getCod_modalita_invio3_sollecito2();
			webVO.setCodModalitaInvio3Sollecito2(ValidazioneDati.isFilled(cod_modalita_invio3_sollecito2) ? cod_modalita_invio3_sollecito2.toString() : "");

			Character cod_modalita_invio1_sollecito3 = hVO.getCod_modalita_invio1_sollecito3();
			webVO.setCodModalitaInvio1Sollecito3(ValidazioneDati.isFilled(cod_modalita_invio1_sollecito3) ? cod_modalita_invio1_sollecito3.toString() : "");

			Character cod_modalita_invio2_sollecito3 = hVO.getCod_modalita_invio2_sollecito3();
			webVO.setCodModalitaInvio2Sollecito3(ValidazioneDati.isFilled(cod_modalita_invio2_sollecito3) ? cod_modalita_invio2_sollecito3.toString() : "");

			Character cod_modalita_invio3_sollecito3 = hVO.getCod_modalita_invio3_sollecito3();
			webVO.setCodModalitaInvio3Sollecito3(ValidazioneDati.isFilled(cod_modalita_invio3_sollecito3) ? cod_modalita_invio3_sollecito3.toString() : "");

			webVO.setGgValiditaPrelazione(hVO.getNum_gg_validita_prelazione());

			char ammessa_autoregistrazione_utente = hVO.getAmmessa_autoregistrazione_utente();
			webVO.setAmmessaAutoregistrazioneUtente(ammessa_autoregistrazione_utente == 'S');

			char ammesso_inserimento_utente = hVO.getAmmesso_inserimento_utente();
			webVO.setAmmessoInserimentoUtente(ammesso_inserimento_utente == 'S');

			char anche_da_remoto = hVO.getAnche_da_remoto();
			webVO.setAncheDaRemoto(anche_da_remoto == 'S');

			webVO.setCatMediazioneDigit(hVO.getCd_cat_med_digit());

			//almaviva5_20150511
			ModelloSollecitoVO modelloSollecito = ClonePool.fromJson(hVO.getXml_modello_soll(), ModelloSollecitoVO.class);
			webVO.setModelloSollecito(modelloSollecito);

			webVO.setTipoRinnovo(ValidazioneDati.coalesce(hVO.getFl_tipo_rinnovo(), '0'));
			webVO.setGgRinnovoRichiesta(ValidazioneDati.coalesce(hVO.getNum_gg_rinnovo_richiesta(), (short)0));

			//almaviva5_20150630
			webVO.setPrioritaPrenotazioni(ValidazioneDati.in(hVO.getFl_priorita_prenot(), 's', 'S'));

			//almaviva5_20160707
			webVO.setMailNotifica(hVO.getEmail_notifica());

			//almaviva5_20151110 servizi ill
			Tbl_tipi_autorizzazioni aut = hVO.getId_autorizzazione_ill();
			AutorizzazioneVO autorizzazioneILL = aut != null ?
					ServiziConversioneVO.daHibernateAWebTipoAutorizzazione(aut, 0) :
						new AutorizzazioneVO("", "");
			webVO.setAutorizzazioneILL(autorizzazioneILL);
			webVO.setServiziILLAttivi(ValidazioneDati.in(hVO.getFl_att_servizi_ill(), 's', 'S'));
		}

		return webVO;
	}

	public static UtentiToWeb Utenti = new UtentiToWeb();

	public final DocumentoNonSbnVO documentoNonSbn(Tbl_documenti_lettori hVO, List<EsemplareDocumentoNonSbnVO> esemplari) {

		DocumentoNonSbnVO webVO = new DocumentoNonSbnVO();

		if (hVO != null) {

			webVO.setIdDocumento(hVO.getId_documenti_lettore());

			Tbf_biblioteca_in_polo bib = hVO.getCd_bib();
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBib(bib.getCd_biblioteca());

			webVO.setTipo_doc_lett(hVO.getTipo_doc_lett());
			webVO.setCod_doc_lett(hVO.getCod_doc_lett());
			webVO.setFonte(hVO.getFonte());

			webVO.setCodFruizione(hVO.getCd_catfrui());
			webVO.setCodNoDisp(hVO.getCd_no_disp());

			BigDecimal anno_edizione = hVO.getAnno_edizione();
			webVO.setAnnoEdizione(anno_edizione != null ? anno_edizione.toPlainString() : "");
			webVO.setAnnata(hVO.getAnnata());
			webVO.setAutore(hVO.getAutore());
			webVO.setEditore(hVO.getEditore());
			webVO.setLuogoEdizione(hVO.getLuogo_edizione());
			webVO.setTitolo(hVO.getTitolo());

			webVO.setBid(hVO.getBid());
			webVO.setNote(hVO.getNote());
			webVO.setMessaggioAlLettore(hVO.getMsg_lettore());
			webVO.setCod_bib_inv(hVO.getCod_bib_inv());
			webVO.setCod_serie(hVO.getCod_serie());
			webVO.setCod_inven(hVO.getCod_inven());
			webVO.setNum_volume(hVO.getNum_volume());
			webVO.setPaese(hVO.getPaese());
			webVO.setLingua(hVO.getLingua());
			webVO.setData_sugg_lett(hVO.getData_sugg_lett());
			webVO.setStato_sugg(hVO.getStato_sugg());

			webVO.setSegnatura(hVO.getSegnatura());
			webVO.setOrd_segnatura(hVO.getOrd_segnatura());
			webVO.setNatura(hVO.getNatura());

			fillBaseWeb(hVO, webVO);

			webVO.setDataIns(dateFormat.format(hVO.getTs_ins()));
			webVO.setDataAgg(dateFormat.format(hVO.getTs_var()));

			webVO.setEsemplari(esemplari);
			webVO.setNumeroEsemplari(ValidazioneDati.size(esemplari));

			Tbl_utenti id_utenti = hVO.getId_utenti();
			if (id_utenti != null) {
				webVO.setUtente(id_utenti.getCod_utente());
				webVO.setCognomeNome(id_utenti.getCognome().trim() + " " + id_utenti.getNome().trim() );
			}

			//almaviva5_20141125 servizi ill
			Character tp_numero_std = hVO.getTp_numero_std();
			if (ValidazioneDati.isFilled(tp_numero_std) ) {
				webVO.setTipoNumStd(tp_numero_std.toString());
				webVO.setNumeroStd(hVO.getNumero_std());
			}

			Type type = new TypeToken<List<BibliotecaVO>>(){}.getType();
			List<BibliotecaVO> biblioteche = ClonePool.fromJson(hVO.getBiblioteche(), type);
			if (ValidazioneDati.isFilled(biblioteche))
				webVO.setBiblioteche(biblioteche);

			Character tp_record_uni = hVO.getTp_record_uni();
			if (ValidazioneDati.isFilled(tp_record_uni))
				webVO.setTipoRecord(tp_record_uni);

			webVO.setEnteCuratore(hVO.getEnte_curatore());
			webVO.setFaParte(hVO.getFa_parte());
			webVO.setFascicolo(hVO.getFascicolo());
			webVO.setDataPubb(hVO.getData_pubb());
			webVO.setAutoreArticolo(hVO.getAutore_articolo());
			webVO.setTitoloArticolo(hVO.getTitolo_articolo());
			webVO.setPagine(hVO.getPagine());
			webVO.setFonteRif(hVO.getFonte_rif());

		}

		return webVO;
	}

	public final ModalitaPagamentoVO modalitaPagamento(Tbl_modalita_pagamento hibernateVO) {

		ModalitaPagamentoVO webVO = new ModalitaPagamentoVO();
		webVO.setCdBib(hibernateVO.getCd_bib().getCd_biblioteca());
		webVO.setCdPolo(hibernateVO.getCd_bib().getCd_polo().getCd_polo());
		webVO.setCodModPagamento(hibernateVO.getCod_mod_pag());
		webVO.setDescrizione(hibernateVO.getDescr());
		webVO.setFlCanc(hibernateVO.getFl_canc() + "");
		webVO.setId(hibernateVO.getId_modalita_pagamento());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());

		return webVO;
	}

	public final IterServizioVO iterServizio(Tbl_iter_servizio hibernateVO) {
		IterServizioVO webVO = new IterServizioVO();

		webVO.setIdIterServizio(hibernateVO.getId_iter_servizio());
		webVO.setCodAttivita(hibernateVO.getCod_attivita());
		webVO.setCodBib(hibernateVO.getId_tipo_servizio().getCd_bib().getCd_biblioteca());
		webVO.setCodStatCir(String.valueOf(hibernateVO.getCod_stat_cir()));
		webVO.setCodStatoMov(String.valueOf(hibernateVO.getStato_mov()));
		webVO.setCodStatoRich(String.valueOf(hibernateVO.getCod_stato_rich()));
		webVO.setCodStatoRicIll(String.valueOf(hibernateVO.getCod_stato_ric_ill()));
		webVO.setCodTipoServ(hibernateVO.getId_tipo_servizio().getCod_tipo_serv());
		webVO.setFlagStampa(String.valueOf(hibernateVO.getFlag_stampa()));

		webVO.setFlgAbil(String.valueOf(hibernateVO.getFlg_abil()));
		webVO.setFlgRinn(String.valueOf(hibernateVO.getFlg_rinn()));
		webVO.setNumPag(hibernateVO.getNum_pag());
		webVO.setObbl(String.valueOf(hibernateVO.getObbl()));
		webVO.setProgrIter(hibernateVO.getProgr_iter());
		webVO.setStatoIter(String.valueOf(hibernateVO.getStato_iter()));
		webVO.setTesto(hibernateVO.getTesto());
		fillBaseWeb(hibernateVO, webVO);

		webVO.setCodServizioILL(hibernateVO.getId_tipo_servizio().getCd_iso_ill());

		return webVO;
	}

	public final EsemplareDocumentoNonSbnVO esemplareDocumentoNonSbn(int progr, Tbl_esemplare_documento_lettore hibernateVO) {

		EsemplareDocumentoNonSbnVO webVO = new EsemplareDocumentoNonSbnVO();
		webVO.setProgr(progr);
		Tbl_documenti_lettori doc = hibernateVO.getId_documenti_lettore();
		webVO.setCodPolo(doc.getCd_bib().getCd_polo().getCd_polo());
		webVO.setCodBib(doc.getCd_bib().getCd_biblioteca());
		webVO.setTipo_doc_lett(doc.getTipo_doc_lett());
		webVO.setCod_doc_lett(doc.getCod_doc_lett());
		webVO.setIdDocumento(doc.getId_documenti_lettore());

		webVO.setIdEsemplare(hibernateVO.getId_esemplare());
		webVO.setPrg_esemplare(hibernateVO.getPrg_esemplare());

		if (hibernateVO.getPrecisazione()!=null)
		 {
			webVO.setAnnata(hibernateVO.getPrecisazione());
		 }
		webVO.setInventario(hibernateVO.getInventario());
		webVO.setFonte(hibernateVO.getFonte());
		webVO.setCodNoDisp(hibernateVO.getCod_no_disp());

		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(webVO.getUteVar());
		webVO.setFlCanc(hibernateVO.getFl_canc() + "");

		return webVO;
	}

	public final ModelloStampaVO modelloStampa(Tbf_modelli_stampe hibernateVO) {

		ModelloStampaVO webVO = new ModelloStampaVO(hibernateVO.getId_modello() );

		Tbf_biblioteca_in_polo bib = hibernateVO.getCd_bib();
		if (bib != null) {
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBib(bib.getCd_biblioteca());
		}

		webVO.setUteIns(hibernateVO.getUte_ins());
		webVO.setUteVar(hibernateVO.getUte_var());
		webVO.setTsIns(hibernateVO.getTs_ins());
		webVO.setTsVar(hibernateVO.getTs_var());
		webVO.setFlCanc(Character.toString(hibernateVO.getFl_canc()) );
		webVO.setTipoModello(ModelloStampaType.fromChar(hibernateVO.getTipo_modello()));

		webVO.setNomeModello(hibernateVO.getModello());
		webVO.setDescrizione(hibernateVO.getDescr());
		webVO.setJrxml(hibernateVO.getCampi_valori_del_form() );
		webVO.setAttivita(hibernateVO.getCd_attivita());
		webVO.setDescrizioneBib(hibernateVO.getDescr_bib());

		String sub = hibernateVO.getSubreport();
		if (ValidazioneDati.isFilled(sub)) {
			String[] tokens = sub.split(TBF_MODELLO_STAMPA_SEPARATORE);
			List<String> subReports = webVO.getSubReports();
			for (String token : tokens)
				//almaviva5_20101103 #3966
				if (ValidazioneDati.isFilled(token))
					subReports.add(token.trim());

		}

		return webVO;
	}

	public final CodiceVO elementoTabellaCodici(Tb_codici codice) {
		CodiceVO codiceVO = new CodiceVO();
		codiceVO.setCdTabella(codice.getCd_tabella());
		codiceVO.setDescrizione(codice.getDs_tabella());
		codiceVO.setNomeTabella(codice.getTp_tabella());
		codiceVO.setLunghezza(codice.getCd_flg1());
		codiceVO.setTipo(codice.getCd_flg2());
		codiceVO.setCd_unimarc(codice.getCd_unimarc());
		codiceVO.setCd_marc21(codice.getCd_marc_21());
		codiceVO.setPermessi(codice.getCd_flg3());

		Character materiale = codice.getTp_materiale();
		codiceVO.setMateriale(materiale != null ? String.valueOf(materiale) : null);

		codiceVO.setFlag1(codice.getCd_flg1());
		codiceVO.setFlag2(codice.getCd_flg2());
		codiceVO.setFlag3(codice.getCd_flg3());
		codiceVO.setFlag4(codice.getCd_flg4());
		codiceVO.setFlag5(codice.getCd_flg5());
		codiceVO.setFlag6(codice.getCd_flg6());
		codiceVO.setFlag7(codice.getCd_flg7());
		codiceVO.setFlag8(codice.getCd_flg8());
		codiceVO.setFlag9(codice.getCd_flg9());
		codiceVO.setFlag10(codice.getCd_flg10());
		codiceVO.setFlag11(codice.getCd_flg11());
		codiceVO.setDs_cdsbn_ulteriore(codice.getDs_cdsbn_ulteriore());
		codiceVO.setSalva(false);
		Date dataAttivazione = codice.getDt_fine_validita();
		Date oggi = new Date(System.currentTimeMillis());
		boolean attivo = (dataAttivazione!= null && dataAttivazione.after(oggi) );
		codiceVO.setAttivo(attivo);

		if (dataAttivazione != null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				String date = format.format(dataAttivazione);
				codiceVO.setDataAttivazione(date);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return codiceVO;
	}

	public final ElementoStampaSollecitoVO elementoStampaSollecito(
			Tbl_richiesta_servizio richiesta, Tbl_solleciti sollecito, String myEsito) throws Exception {
		ElementoStampaSollecitoVO webVO = new ElementoStampaSollecitoVO();
		webVO.setIdRichiesta(richiesta.getCod_rich_serv() + "");

		Tbl_utenti utente = richiesta.getId_utenti_biblioteca().getId_utenti();
		webVO.setCodUtente(utente.getCod_utente());
		//almaviva5_20110802 #4447
		//webVO.setNomeUtente(utente.getCognome().trim() + " " + utente.getNome().trim() );
		webVO.setNomeUtente(ValidazioneDati.trimOrEmpty(utente.getNome()) + ' ' + ValidazioneDati.trimOrEmpty(utente.getCognome()) );
		//
		boolean isDomicilio = ValidazioneDati.isFilled(utente.getIndirizzo_dom());

		webVO.setIndirizzo(isDomicilio ? utente.getIndirizzo_dom() : utente.getIndirizzo_res());
		webVO.setCap(isDomicilio ? utente.getCap_dom() : utente.getCap_res());
		webVO.setCitta(isDomicilio ? utente.getCitta_dom() : utente.getCitta_res());

		//almaviva5_20120528 #5003
		webVO.setProvincia(isDomicilio ? utente.getProv_dom() :	utente.getProv_res());
		webVO.setNazione(isDomicilio ? null : utente.getPaese_res());


		webVO.setDataPrestito(DateUtil.formattaData(richiesta.getData_in_eff()));
		webVO.setDataScadenza(DateUtil.formattaData(richiesta.getData_fine_prev()));

		if (ValidazioneDati.isFilled(myEsito))
			webVO.setEsito(myEsito);
		else
			if (sollecito != null && sollecito.getEsito() == 'N')
				webVO.setEsito("Non inviato");
			else
				webVO.setEsito("Inviato");

		webVO.setNumSollecito((sollecito != null ? sollecito.getProgr_sollecito() : 1) + "");

		Tbc_inventario inv = richiesta.getCd_polo_inv();
		if (inv != null) {
			Tb_titolo titolo = inv.getB();
			//almaviva5_20100323 #3561 split del titolo in etichette unimarc
			webVO.setTitolo(estraiTitoloPerAreaServizi(titolo));

		} else 	// doc. lett
			webVO.setTitolo(richiesta.getId_documenti_lettore().getTitolo());

		formattaSegnatura(webVO, richiesta);

		if (sollecito != null)
			webVO.setTipoInvio(CodiciProvider.cercaDescrizioneCodice(String.valueOf(sollecito.getTipo_invio()) ,
				CodiciType.CODICE_TIPO_INVIO_SOLLECITO,	CodiciRicercaType.RICERCA_CODICE_SBN));
		else
			webVO.setTipoInvio("");

		webVO.setTipoServizio(CodiciProvider.cercaDescrizioneCodice(richiesta
				.getId_servizio().getId_tipo_servizio().getCod_tipo_serv(),
				CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN));

		//almaviva5_20100322 per periodici
		webVO.setVolume(richiesta.getNum_volume());
		BigDecimal annoPeriod = richiesta.getAnno_period();
		webVO.setAnno(ValidazioneDati.isFilled(annoPeriod) ? String.valueOf(annoPeriod) : null);

		//almaviva5_20110802 #4447
		if (ValidazioneDati.in(utente.getPersona_giuridica(), 'S', 's'))
			webVO.setTipo(TipoUtente.ENTE);
		else
			if (ValidazioneDati.in(utente.getSesso(), 'M', 'm'))
				webVO.setTipo(TipoUtente.MASCHIO);
			else
				webVO.setTipo(TipoUtente.FEMMINA);

		return webVO;
	}

	private void formattaSegnatura(ElementoStampaSollecitoVO webVO, Tbl_richiesta_servizio req) {

		Tbc_inventario inv = req.getCd_polo_inv();

		BigDecimal anno = req.getAnno_period();
		if (inv != null && inv.getKey_loc() != null) {

			Tbc_serie_inventariale serie = inv.getCd_serie();
			Tbf_biblioteca_in_polo bib = serie.getCd_polo();
			webVO.setInventario(bib.getCd_biblioteca() + serie.getCd_serie() + inv.getCd_inven());

			// segnatura = Sezione + Collocazione + Specificazione + Numero di sequenza
			webVO.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(req));
		}

		Tbl_esemplare_documento_lettore esempl = req.getId_esemplare_documenti_lettore();
		if (esempl != null) {
			Tbl_documenti_lettori docLettore = esempl.getId_documenti_lettore();
			webVO.setSegnatura(docLettore.getSegnatura());
			webVO.setInventario(docLettore.getCd_bib().getCd_biblioteca() + " "
					+ docLettore.getTipo_doc_lett() + " "
					+ docLettore.getCod_doc_lett() + " "
					+ esempl.getPrg_esemplare() );
			if (docLettore.getNatura() == 'S' && ValidazioneDati.isFilled(anno)) //anno periodico
				webVO.setSegnatura(docLettore.getSegnatura() + " [" + anno.intValue() + "]");
			else
				webVO.setSegnatura(docLettore.getSegnatura());
		}

	}


	public final String formattaCollocazione(Tbc_collocazione c) {
		//almaviva5_20150119 #5701
		Tbc_sezione_collocazione sez = c.getCd_sez();

		StringBuilder buf = new StringBuilder(64);
		// Sezione
		buf.append(ValidazioneDati.trimOrEmpty(sez.getCd_sez()));

		// Collocazione
		String cd_loc = c.getCd_loc();
		if (ValidazioneDati.isFilled(cd_loc))
			buf.append(" ").append(ValidazioneDati.trimOrEmpty(cd_loc));

		// Specificazione
		String spec_loc = c.getSpec_loc();
		if (ValidazioneDati.isFilled(spec_loc))
			buf.append(" ").append(ValidazioneDati.trimOrEmpty(spec_loc));

		return ValidazioneDati.trimOrEmpty(buf.toString());
	}

	public final String estraiTitoloPerAreaServizi(String isbd, String indice_isbd) throws Exception {

		IsbdVO it = IsbdTokenizer.tokenize(isbd, indice_isbd);

		StringBuilder areaTit = new StringBuilder();

		String t200AreaTitolo = it.getT200_areaTitolo();
		areaTit.append(ValidazioneDati.length(t200AreaTitolo) > 100 ? t200AreaTitolo.substring(0, 100) : t200AreaTitolo);

		String t205AreaEdizione = it.getT205_areaEdizione();
		if (ValidazioneDati.isFilled(t205AreaEdizione))
			areaTit.append(". - ").append(t205AreaEdizione);

		String t210AreaPubblicazione = it.getT210_areaPubblicazione();
		if (ValidazioneDati.isFilled(t210AreaPubblicazione))
			areaTit.append(". - ").append(t210AreaPubblicazione);

		String t215AreaDescrizioneFisica = it.getT215_areaDescrizioneFisica();
		if (ValidazioneDati.isFilled(t215AreaDescrizioneFisica))
			areaTit.append(". - ").append(t215AreaDescrizioneFisica);

		return areaTit.toString();
	}

	public final String estraiTitoloPerAreaServizi(Tb_titolo titolo) throws Exception {

		return estraiTitoloPerAreaServizi(titolo.getIsbd(), titolo.getIndice_isbd());
	}

	public final ElementoSeriePeriodicoVO elementoSeriePeriodico(
			Tbc_esemplare_titolo e, Tbc_collocazione c, Timestamp tsInsPrimaColl, Tba_ordini o,
			List<CatenaRinnoviOrdineVO> catene) throws Exception {

		ElementoSeriePeriodicoVO esp = new ElementoSeriePeriodicoVO();

		if (e == null && c == null && o == null)
			return null;
		if (e != null && c != null)
			esp.setTipo(SeriePeriodicoType.ESEMPLARE);
		if (e == null && c != null)
			esp.setTipo(SeriePeriodicoType.COLLOCAZIONE);
		if (e == null && c == null && o != null)
			esp.setTipo(SeriePeriodicoType.ORDINE);

		switch (esp.getTipo() ) {
		case ESEMPLARE:
			SerieEsemplareCollVO esemplare = serieEsemplare(e, esp);
			esp.setEsemplare(esemplare);

			SerieCollocazioneVO collocazione = serieCollocazione(c, esp);
			esp.setCollocazione(collocazione);
			break;

		case COLLOCAZIONE:
			SerieCollocazioneVO collocazione2 = serieCollocazione(c, esp);
			esp.setCollocazione(collocazione2);
			break;

		case ORDINE:
		default:
			break;
		}

		if (c != null)
			esp.getCollocazione().setTsInsPrimaColl(tsInsPrimaColl);

		if (o != null) {
			SerieOrdineVO ordine = serieOrdine(o, esp, catene);
			esp.setOrdine(ordine);
		}

		return esp;
	}

	private SerieEsemplareCollVO serieEsemplare(Tbc_esemplare_titolo e,	ElementoSeriePeriodicoVO esp) {
		SerieEsemplareCollVO esemplare = new SerieEsemplareCollVO();
		esemplare.setParent(esp);
		ClonePool.copyCommonProperties(esemplare, e);
		esemplare.setCodPolo(e.getCd_polo().getCd_polo().getCd_polo());
		esemplare.setCodBib(e.getCd_polo().getCd_biblioteca());
		esemplare.setBid(e.getB().getBid());
		return esemplare;
	}

	public SerieOrdineVO serieOrdine(Tba_ordini o, ElementoSeriePeriodicoVO esp, List<CatenaRinnoviOrdineVO> catene) {
		SerieOrdineVO ordine = new SerieOrdineVO();
		ordine.setParent(esp);
		ClonePool.copyCommonProperties(ordine, o);
		ordine.setCod_bib_ord(o.getCd_bib().getCd_biblioteca());

		Tbr_fornitori fornitore = o.getCod_fornitore();
		ordine.setFornitore(fornitore.getNom_fornitore());
		ordine.setId_fornitore(fornitore.getCod_fornitore());

		if (ValidazioneDati.isFilled(catene))
			for (CatenaRinnoviOrdineVO cr : catene)
				if (cr.getOrdiniPrecedenti().contains(o.getId_ordine()))
					ordine.setOrdiniPrecedenti(cr.getOrdiniPrecedenti());

		return ordine;
	}

	private SerieCollocazioneVO serieCollocazione(Tbc_collocazione c, ElementoSeriePeriodicoVO esp) {
		SerieCollocazioneVO collocazione = new SerieCollocazioneVO();
		collocazione.setParent(esp);
		ClonePool.copyCommonProperties(collocazione, c);
		collocazione.setCodSez(c.getCd_sez().getCd_sez());
		collocazione.setBid(c.getB().getBid());
		return collocazione;
	}

	public final FascicoloVO fascicolo(Tbp_fascicolo f, Tbp_esemplare_fascicolo ef) {
		FascicoloVO webVO = new FascicoloVO();

		webVO.setFid(f.getFid());
		webVO.setSici(f.getSici());
		webVO.setEan(f.getEan());
		webVO.setData_conv_pub(f.getData_conv_pub());
		webVO.setCd_per(String.valueOf(f.getCd_per()));
		webVO.setCd_tipo_fasc(String.valueOf(f.getCd_tipo_fasc()));
		webVO.setData_pubb(f.getData_pubb());
		webVO.setDescrizione(f.getDescrizione());
		webVO.setAnnata(f.getAnnata());
		webVO.setNum_volume(f.getNum_volume());
		webVO.setNum_in_fasc(f.getNum_in_fasc());
		webVO.setData_in_pubbl(f.getData_in_pubbl());
		webVO.setNum_fi_fasc(f.getNum_fi_fasc());
		webVO.setData_fi_pubbl(f.getData_fi_pubbl());
		webVO.setNote(f.getNote());
		webVO.setNum_alter(f.getNum_alter());
		webVO.setBid_link(f.getBid_link());
		webVO.setFid_link(f.getFid_link());

		//ClonePool.copyCommonProperties(webVO, f);

		webVO.setTsIns(f.getTs_ins());
		webVO.setTsVar(f.getTs_var());
		webVO.setUteIns(f.getUte_ins());
		webVO.setUteVar(f.getUte_var());
		webVO.setFlCanc(String.valueOf(f.getFl_canc()));
		webVO.setBid(f.getTitolo().getBid());
		if (ef != null) {
			EsemplareFascicoloVO e = esemplareFascicolo(ef);
			webVO.setStato(e.isCancellato() ?
				StatoFascicolo.ATTESO : !e.isLegatoInventario() ?
					StatoFascicolo.RICEVUTO : StatoFascicolo.POSSEDUTO);
			webVO.setEsemplare(e);
		}

		return webVO;
	}

	public final EsemplareFascicoloVO esemplareFascicolo(Tbp_esemplare_fascicolo ef) {

		EsemplareFascicoloVO webVO = new EsemplareFascicoloVO();

		Tba_ordini ordine = ef.getOrdine();
		if (ordine != null) {
			ClonePool.copyCommonProperties(webVO, ordine);
			Tbf_biblioteca_in_polo bib = ordine.getCd_bib();
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBib(bib.getCd_biblioteca());
			webVO.setCod_bib_ord(bib.getCd_biblioteca());
		}

		Tbc_inventario inv = ef.getInventario();
		if (inv != null) {
			Tbc_serie_inventariale serie = inv.getCd_serie();
			Tbf_biblioteca_in_polo bib = serie.getCd_polo();
			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBib(bib.getCd_biblioteca());
			webVO.setCd_serie(serie.getCd_serie());
			webVO.setCd_inven(inv.getCd_inven());
			Tbc_collocazione c = inv.getKey_loc();
			if (c != null) {
				ClonePool.copyCommonProperties(webVO, c);
				webVO.setCodSez(c.getCd_sez().getCd_sez());
			}
		}

		ClonePool.copyCommonProperties(webVO, ef);
		webVO.setBid(ef.getFascicolo().getTitolo().getBid());
		webVO.setFid(ef.getFascicolo().getFid());

		webVO.setTsIns(ef.getTs_ins());
		webVO.setTsVar(ef.getTs_var());
		webVO.setUteIns(ef.getUte_ins());
		webVO.setUteVar(ef.getUte_var());
		webVO.setFlCanc(String.valueOf(ef.getFl_canc()));

		return webVO;
	}

	public final CatenaRinnoviOrdineVO catenaRinnoviOrdine(String codPolo,
			String codBib, List<Integer> rinnovi) {
		CatenaRinnoviOrdineVO webVO = new CatenaRinnoviOrdineVO();
		webVO.setOrdiniPrecedenti(rinnovi);
		webVO.setId_ordine(rinnovi.get(0));	//ordine più recente della catena
		return webVO;
	}

	public final UnionEsamePeriodico unionEsamePeriodico(Object[] row) {
		UnionEsamePeriodico vo = new UnionEsamePeriodico();
		vo.setId_ordine((Integer) row[0]);
		vo.setChiaveOrdine((String) row[1]);
		vo.setAnno_abb_ord((BigDecimal) row[2]);
		vo.setPrimoOrdine((String) row[3]);

		vo.setKey_loc((Integer) row[4]);
		vo.setConsis((String) row[5]);

		vo.setEsemplare((String) row[6]);
		vo.setCons_doc((String) row[7]);

		vo.setCd_sit((Character) row[8]);
		vo.setSeq_coll((String) row[9]);
		//almaviva5_20111207 #4669
		//vo.setAnno_abb_inv((Integer) row[10]);
		vo.setAnno_abb_inv(row[10] != null ? (Integer) row[10] : 0);
		vo.setTs_ins_prima_coll((Timestamp) row[11]);
		vo.setCd_serie((String) row[12]);
		vo.setCd_inven((Integer) row[13]);

		return vo;
	}

	public final InventarioListeVO inventarioListe(Tbc_inventario inv) throws Exception {

		InventarioListeVO webVO = new InventarioListeVO();

		// almaviva5_20100923 campi mancanti per gest.periodici
		Tbc_serie_inventariale cd_serie = inv.getCd_serie();
		Tbf_biblioteca_in_polo bib = cd_serie.getCd_polo();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		Integer anno_abb = inv.getAnno_abb();
		if (ValidazioneDati.isFilled(anno_abb) )
			webVO.setAnnoAbb(anno_abb.toString());
		//
		webVO.setCodBib(bib.getCd_biblioteca());
		webVO.setCodSerie(cd_serie.getCd_serie());
		webVO.setCodInvent(inv.getCd_inven());
		Tb_titolo tit = inv.getB();
		if (tit != null) {
			webVO.setBid(tit.getBid());
			List<TitoloVO> titoli = TitoloHibernate.getTitoloHib(tit, null);
			if (titoli != null) {
				if (ValidazioneDati.size(titoli) == 1) {
					TitoloVO titolo = titoli.get(0);
					webVO.setDescr(titolo.getIsbd());
				}
			} else {
				webVO.setDescr("Titolo non trovato in Polo");
			}
		} else {
			webVO.setDescr("Titolo non trovato in Polo");
		}
		// rec.setBid(recResult.getB().getBid());
		// rec.setDescr(recResult.getB().getIsbd());//ha sostituito la
		// getTitolo()
		webVO.setCodSit((String.valueOf(inv.getCd_sit())));
		if (webVO.isPrecisato() ) {
			webVO.setSitAmm("precisato");
		} else if (webVO.isCollocato() ) {
			webVO.setSitAmm("collocato");
		}
		webVO.setSeqColl(inv.getSeq_coll().trim());
		if (inv.getPrecis_inv().equals("$")) {
			webVO.setPrecInv("");
		} else {
			webVO.setPrecInv(inv.getPrecis_inv());
		}
		Tbc_collocazione key_loc = inv.getKey_loc();
		webVO.setKeyLoc(key_loc != null ? key_loc.getKey_loc() : 0);
		webVO.setKeyLocOld(inv.getKey_loc_old());
		webVO.setSezOld(inv.getSez_old());
		webVO.setLocOld(inv.getLoc_old());
		webVO.setSpecOld(inv.getSpec_old());
		// almaviva5_20100924 aggiunti dati ordine per periodici
		if (ValidazioneDati.isFilled(inv.getCd_bib_ord())) {
			webVO.setCodBibO(inv.getCd_bib_ord());
			webVO.setAnnoOrd(inv.getAnno_ord().toString());
			webVO.setCodTipoOrd(inv.getCd_tip_ord().toString());
			webVO.setCodOrd(inv.getCd_ord().toString());
		}

		return webVO;
	}

	public final String listaNumeriISSN(List<Tb_numero_std> list) {
		StringBuilder buf = new StringBuilder();
		Iterator<Tb_numero_std> i = list.iterator();
		while (i.hasNext()) {
			Tb_numero_std ns = i.next();
			buf.append(ValidazioneDati.trimOrEmpty(ns.getNumero_std()));
			if (i.hasNext())
				buf.append(", ");
		}
		return buf.toString();
	}

	public final String getEmailUtente(Tbl_utenti u) {
		//almaviva5_20150428
		String email1 = u != null ? ValidazioneDati.trimOrNull(u.getInd_posta_elettr()) : null;
		String email2 = u != null ? ValidazioneDati.trimOrNull(u.getInd_posta_elettr2()) : null;
		return ValidazioneDati.isFilled(email1) ? email1 : email2;
	}

	public final UtenteWeb utente(Tbl_utenti hVO) {
		UtenteWeb webVO = new UtenteWeb();

		webVO.setIdUtente(hVO.getId_utenti());
		webVO.setUserId(hVO.getCod_utente());
		webVO.setChangePassword(hVO.getChange_password());
		webVO.setLastAccess(hVO.getLast_access());
		webVO.setTsVarPassword(hVO.getTs_var_password());
		webVO.setCognome(hVO.getCognome().trim());
		webVO.setNome(hVO.getNome().trim());
		Tbf_biblioteca_in_polo bib = hVO.getCd_bib();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());
		//almaviva5_20150430
		//webVO.setIndPostaElettr(utente.getInd_posta_elettr());
		webVO.setPostaElettronica(hVO.getInd_posta_elettr());
		webVO.setPostaElettronica2(hVO.getInd_posta_elettr2());
		webVO.setCodFiscale(hVO.getCod_fiscale());

		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		Character cd_tipo_ute = hVO.getCd_tipo_ute();
		webVO.setTipoUtente(cd_tipo_ute == null ? Constants.Servizi.Utenti.UTENTE_TIPO_SBNWEB : cd_tipo_ute.toString() );

		return webVO;
	}

	public final OrdiniVO ordine(Tba_ordini o) throws Exception {
		OrdiniVO rec = new OrdiniVO();

		Tbf_biblioteca_in_polo bib = o.getCd_bib();
      	rec.setCodPolo(bib.getCd_polo().getCd_polo());
		rec.setCodBibl(bib.getCd_biblioteca());

		rec.setCodOrdine(String.valueOf(o.getCod_ord()));
		rec.setAnnoOrdine(String.valueOf(o.getAnno_ord()));
		rec.setTipoOrdine(String.valueOf(o.getCod_tip_ord()));

		rec.setDataOrdine(DateUtil.formattaData(o.getData_ord()));
		rec.setStatoOrdine(String.valueOf(o.getStato_ordine()));
		rec.setNaturaOrdine(String.valueOf(o.getNatura()));
		rec.setContinuativo(o.getContinuativo() == '1');
		rec.setStampato(o.getStampato());
		rec.setRinnovato(o.getRinnovato());

		String bid = o.getBid();
		if (ValidazioneDati.isFilled(bid)) {
			String isbd = null;
			try {
				Tba_ordiniDao dao = new Tba_ordiniDao();
				TitoloACQVO recTit = null;
				recTit = dao.getTitoloOrdineTerHib(bid);
				if (recTit != null && recTit.getIsbd() != null)
					isbd = recTit.getIsbd();

			} catch (Exception e) {
				isbd = "titolo non trovato";
			}
			rec.setTitolo(new StrutturaCombo(bid, isbd));
		}

		StrutturaCombo forn = new StrutturaCombo();

		Tbr_fornitori f = o.getCod_fornitore();
		if (f != null) {
			forn.setCodice(String.valueOf(f.getCod_fornitore()));
			forn.setDescrizione(ValidazioneDati.trimOrEmpty(f.getNom_fornitore()));

			if (!ValidazioneDati.isFilled(forn.getCodice()) || !ValidazioneDati.isFilled(forn.getDescrizione()) )
				forn.setDescrizione("fornitore non presente su base dati");

			// dati fornitore per stampe
			rec.setFornitore(forn);
		}

		//almaviva5_20110405 #4425
		if (ValidazioneDati.in(o.getCod_tip_ord(), 'A', 'V', 'R')) {
			Tbb_bilanci bil = o.getTbb_bilancicod_mat();
			if (bil != null) {
				Tbb_capitoli_bilanci cap = bil.getId_capitoli_bilanci();
				if (cap != null && ValidazioneDati.isFilled(cap.getEsercizio()) )
				try {
					rec.setBilancio(new StrutturaTerna(String.valueOf(cap.getEsercizio()),
							String.valueOf(cap.getCapitolo()),
							String.valueOf(bil.getCod_mat())));
				} catch (Exception e) {
					//errore mascherato
				}
			}
		}

		return rec;
	}

	public final FornitoreVO fornitore(Tbr_fornitori hVO) throws Exception {
		FornitoreVO webVO = new FornitoreVO();
		webVO.setCodFornitore(String.valueOf(hVO.getCod_fornitore()));
		webVO.setNomeFornitore(hVO.getNom_fornitore());
		webVO.setIndirizzo(hVO.getIndirizzo());
		webVO.setCitta(hVO.getCitta());
		webVO.setCap(hVO.getCap());
		webVO.setPaese(hVO.getPaese());
		webVO.setCodiceFiscale(hVO.getCod_fiscale());
		webVO.setEmail(hVO.getE_mail());
		webVO.setFax(hVO.getFax());
		webVO.setTelefono(hVO.getTelefono());

		return webVO;
	}

	public ModelloPrevisionaleVO modelloPrevisionale(Tbp_modello_previsionale hVO) throws Exception {
		ModelloPrevisionaleVO webVO = new ModelloPrevisionaleVO();

		ClonePool.copyCommonProperties(webVO, hVO);

		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		SbnPeriodico model = SbnPeriodico.unmarshalSbnPeriodico(new StringReader(hVO.getXml_model()));
		DatiPeriodicoType datiPeriodico = model.getDatiPeriodico();
		webVO.setCd_per(datiPeriodico.getPeriodicita().toString());

		PeriodicoNumType numerazione = datiPeriodico.getNumerazione();
		webVO.setNum_primo_volume(numerazione.getVolume().getValoreIniziale());
		webVO.setNum_fascicoli_per_volume(numerazione.getFascicoliPerVolume());
		webVO.setNum_primo_fascicolo(numerazione.getFascicolo().getValoreIniziale());
		webVO.setCd_tipo_num_fascicolo(numerazione.getFascicolo().getTipoNumerazione().toString());

		webVO.setNum_fascicolo_continuativo(numerazione.getFascicolo().hasContinuativo() && numerazione.getFascicolo().getContinuativo());

		PrevisionaleType prev = model.getPrevisionale();
		DateFascicoloType escludi = prev.getEscludi();

		for (GMonthDay day : escludi.getDataSingola())
			webVO.getListaEscludiDate().add(new GiornoMeseVO(day.getMonth(), day.getDay() ));

		for (GiornoSettimanaType gs : escludi.getGiornoSettimana())
			//il model castor numera da 0, mentre la classe Calendar da 1 (1 == SUNDAY)
			webVO.getListaEscludiGiorni().add(gs.getType() + 1);

		return webVO;
	}

	public CreaVariaSoggettoVO creaVariaSoggetto(Tb_soggetto s) throws Exception {
		CreaVariaSoggettoVO vo = new CreaVariaSoggettoVO();

		vo.setCodiceSoggettario(s.getCd_soggettario());
		vo.setCid(s.getCid());
		vo.setLivello(livelloSoglia(s.getCd_livello()) );

		vo.setTipoSoggetto(String.valueOf(s.getCat_sogg()));
		vo.setTesto(s.getDs_soggetto());
		vo.setCondiviso(s.getFl_condiviso() == 's');

		Character edizione = s.getCd_edizione();
		vo.setEdizioneSoggettario(edizione != null ? edizione.toString() : null);

		vo.setLivelloPolo(true);
		vo.setT005(SBNMarcUtil.timestampToT005(s.getTs_var()));
		// almaviva5_20120724 fix edizione mancante
		if (!ValidazioneDati.isFilled(vo.getEdizioneSoggettario())) {
			String cdSogg = CodiciProvider.cercaCodice(vo.getCodiceSoggettario(),
					CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN).getCd_unimarc();

			vo.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(cdSogg));
		}

		return vo;
	}

	public StrutturaInventariOrdVO inventarioOrdine(ResultSet rs) throws Exception {
		StrutturaInventariOrdVO rec = new StrutturaInventariOrdVO();
		rec.setIDOrd(rs.getInt("id_ordine"));
		rec.setCodBibl(rs.getString("cd_bib"));
		rec.setDenoBibl(rs.getString("nom_biblioteca"));
		rec.setCodPolo(rs.getString("cd_polo"));
		rec.setSerie(rs.getString("cd_serie"));
		rec.setNumero(String.valueOf(rs.getInt("cd_inven")));
		rec.setBid(rs.getString("bidInventario"));
		rec.setTitolo(rs.getString("isbd"));

		rec.setFornitore(new StrutturaCombo("",""));
		rec.getFornitore().setCodice(String.valueOf(rs.getInt("cod_fornitore")));
		rec.getFornitore().setDescrizione(rs.getString("nom_fornitore"));

		rec.setOrdine(new StrutturaTerna("","",""));
		rec.getOrdine().setCodice1(rs.getString("cod_tip_ord"));
		rec.getOrdine().setCodice2(String.valueOf(rs.getInt("anno_ord")));
		rec.getOrdine().setCodice3(String.valueOf(rs.getInt("cod_ord")));

		rec.setDataUscita(rs.getString("data_uscita_str"));
		rec.setDataRientroPresunta(rs.getString("data_rientro_presunta_str"));
		rec.setDataRientro(rs.getString("data_rientro_str"));
		rec.setNote(rs.getString("ota_fornitore"));
		if (rs.getString("cd_loc") != null){
			rec.setCollocazione(rs.getString("cd_sez").trim()+ " " + rs.getString("cd_loc").trim()+ " " + rs.getString("spec_loc").trim() + " " + rs.getString("seq_coll").trim());
		}else{
			rec.setCollocazione("");
		}

		//almaviva5_20121114 evolutive google
		rec.setPosizione(rs.getShort("posizione"));
		rec.setVolume(rs.getShort("volume"));

		return rec;
	}

	public final OrdineCarrelloSpedizioneVO ordineCarrelloSpedizione(Tra_ordine_carrello_spedizione hVO) {
		OrdineCarrelloSpedizioneVO webVO = new OrdineCarrelloSpedizioneVO();
		webVO.setIdOrdine(hVO.getIdOrdine());
		webVO.setDataSpedizione(hVO.getDt_spedizione());
		webVO.setPrgSpedizione(hVO.getPrg_spedizione());
		webVO.setCartName(hVO.getCart_name());

		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		return webVO;

	}

	public final ElementoStampaInvOrdineRilegaturaVO elementoStampaInventarioOrdine(Tra_ordine_inventari oi) {

		ElementoStampaInvOrdineRilegaturaVO e = new ElementoStampaInvOrdineRilegaturaVO();
		Tbc_inventario inv = oi.getCd_polo();
		Tbc_serie_inventariale serie = inv.getCd_serie();
		Tb_titolo titolo = inv.getB();

		e.setCodBibl(serie.getCd_polo().getCd_biblioteca());
		e.setSerie(serie.getCd_serie());
		e.setNumero(inv.getCd_inven());
		e.setCollocazione(ServiziUtil.formattaSegnaturaCollocazione(inv, null));
		e.setBid(titolo.getBid());
		e.setTitolo(titolo.getIsbd());

		e.setDataUscita(oi.getData_uscita());
		e.setDataRientroPresunta(oi.getData_rientro_presunta());
		e.setNoteInv(oi.getOta_fornitore());

		return e;
	}

	public final String chiaveInventario(String codBib, String serie, int numInv) {
		return String.format("%s%-3s%09d", ValidazioneDati.trimOrEmpty(codBib), //ValidazioneDati.fillLeft(codBib, ' ', 3),
				ValidazioneDati.trimOrEmpty(serie), numInv);
	}

	public final String chiaveInventario(Tbc_inventario inv) {
		Tbc_serie_inventariale serie = inv.getCd_serie();
		Tbf_biblioteca_in_polo bib = serie.getCd_polo();
		return chiaveInventario(bib.getCd_biblioteca(), serie.getCd_serie(), inv.getCd_inven());
	}

	public final InventarioVO inventario(InventarioVO old, Tbc_inventario i, Locale locale)
			throws DataException {
		if (i == null)
			return old;

		InventarioVO webVO = old != null ? old : new InventarioVO();
		try {
			webVO.setLocale(locale);

			Tbc_serie_inventariale serie = i.getCd_serie();
			Tbf_biblioteca_in_polo bib = serie.getCd_polo();

			webVO.setCodPolo(bib.getCd_polo().getCd_polo());
			webVO.setCodBib(bib.getCd_biblioteca());
			webVO.setCodSerie(serie.getCd_serie());
			webVO.setCodInvent(i.getCd_inven());

			Tbc_provenienza_inventario proven = i.getCd_proven();
			if (proven != null) {
				webVO.setCodProven(proven.getCd_proven());
				webVO.setDescrProven(proven.getDescr());
				webVO.setCodPoloProven(proven.getCd_polo().getCd_polo().getCd_polo());
				webVO.setCodBibProven(proven.getCd_polo().getCd_biblioteca());
			} else {
				webVO.setCodProven("");
				webVO.setCodPoloProven("");
				webVO.setCodBibProven("");
			}

			Tbc_collocazione coll = i.getKey_loc();
			if (coll == null) {
				webVO.setKeyLoc(0);
			} else {
				webVO.setKeyLoc(coll.getKey_loc());
			}

			Tb_titolo tit = i.getB();
			if (tit != null) {
				webVO.setBid(tit.getBid());
				webVO.setNatura(String.valueOf(tit.getCd_natura()));
			} else {
				webVO.setBid("");
				webVO.setNatura("");
			}
			webVO.setValoreDouble((i.getValore()).doubleValue());
			webVO.setImportoDouble(i.getImporto().doubleValue());
			if (i.getNum_vol() != null) {
				webVO.setNumVol(i.getNum_vol().toString());
			} else {
				webVO.setNumVol(String.valueOf(0));
			}
			if (i.getTot_loc() != null) {
				webVO.setTotLoc(i.getTot_loc().toString());
			} else {
				webVO.setTotLoc(String.valueOf(0));
			}
			if (i.getTot_inter() != null) {
				webVO.setTotInter(i.getTot_inter().toString());
			} else {
				webVO.setTotInter(String.valueOf(0));
			}
			webVO.setSeqColl(i.getSeq_coll().trim());
			if (i.getPrecis_inv() != null) {
				if (i.getPrecis_inv().equals("$")) {
					webVO.setPrecInv(" ");
				} else {
					webVO.setPrecInv(i.getPrecis_inv());
				}
			} else {
				webVO.setPrecInv("");
			}
			if (i.getAnno_abb() != null) {
				webVO.setAnnoAbb(i.getAnno_abb().toString());
			} else {
				webVO.setAnnoAbb(String.valueOf(0));
			}
			webVO.setFlagDisp(String.valueOf(i.getFlg_disp()));
			webVO.setFlagNU(String.valueOf(i.getFlg_nuovo_usato()));
			webVO.setStatoConser(i.getStato_con());

			if (i.getCd_fornitore() != null) {
				webVO.setCodFornitore(i.getCd_fornitore().toString());
			} else {
				webVO.setCodFornitore("");
			}
			webVO.setCodMatInv(i.getCd_mat_inv());
			webVO.setCodSit(String.valueOf(i.getCd_sit()));
			if (i.getCd_no_disp().trim() != null) {
				webVO.setCodNoDisp(i.getCd_no_disp().trim());
			} else {
				webVO.setCodNoDisp("");
			}
			if (i.getCd_frui().trim().trim() != null) {
				webVO.setCodFrui(i.getCd_frui());
			} else {
				webVO.setCodFrui("");
			}
			//
			//
			if (i.getCd_riproducibilita() == null) {
				webVO.setCodRiproducibilita("");
			} else {
				webVO.setCodRiproducibilita(i.getCd_riproducibilita()
						.toString());
			}
			if (i.getSupporto_copia() == null) {
				webVO.setSupportoCopia("");
			} else {
				webVO.setSupportoCopia(i.getSupporto_copia());
			}
			//
			if (i.getDigitalizzazione() == null) {
				webVO.setDigitalizzazione("");
			} else {
				webVO.setDigitalizzazione(i.getDigitalizzazione().toString());
			}
			if (i.getDisp_copia_digitale() == null
					|| (i.getDisp_copia_digitale() != null && i
							.getDisp_copia_digitale().trim().equals(""))) {
				webVO.setDispDaRemoto("");
				webVO.setDescrDispDaRemoto("");
			} else {
				webVO.setDispDaRemoto(i.getDisp_copia_digitale().toString());
				webVO.setDescrDispDaRemoto(CodiciProvider.cercaDescrizioneCodice(
						String.valueOf(i.getDisp_copia_digitale().trim()),
						CodiciType.CODICE_DISP_ACCESSO_REMOTO,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}
			if (i.getRif_teca_digitale() == null
					|| (i.getRif_teca_digitale() != null && i
							.getRif_teca_digitale().trim().equals(""))) {
				webVO.setRifTecaDigitale("");
				webVO.setDescrRifTecaDigitale("");
			} else {
				webVO.setRifTecaDigitale(i.getRif_teca_digitale());
				webVO.setDescrRifTecaDigitale(CodiciProvider.cercaDescrizioneCodice(
						String.valueOf(i.getRif_teca_digitale().trim()),
						CodiciType.CODICE_TECHE_DIGITALI,
						CodiciRicercaType.RICERCA_CODICE_SBN));
			}
			if (i.getId_accesso_remoto() == null) {
				webVO.setIdAccessoRemoto("");
			} else {
				webVO.setIdAccessoRemoto(i.getId_accesso_remoto());
			}
			// 23/04/2009
			// rosacreainv inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
			if (i.getCd_ord() == null || i.getCd_ord() == 0) {
				webVO.setCodBibO("");
				webVO.setAnnoOrd("");
				webVO.setCodOrd("");
				webVO.setRigaOrd("");
				// rosacreainv inv.setCodTipoOrd("");
				if (i.getCd_tip_ord() != null) {
					webVO.setCodTipoOrd(i.getCd_tip_ord().toString());
					if (i.getTipo_acquisizione() != null) {
						webVO.setTipoAcquisizione(i.getTipo_acquisizione()
								.toString());
					} else {
						webVO.setTipoAcquisizione("");
					}
				} else {
					webVO.setCodTipoOrd("");
					webVO.setTipoAcquisizione("");
				}
			} else {
				webVO.setCodBibO(i.getCd_bib_ord());
				webVO.setAnnoOrd(i.getAnno_ord().toString());
				webVO.setCodOrd(i.getCd_ord().toString());
				// rosacreainv
				// inv.setCodTipoOrd(recInv.getCd_tip_ord().toString());
				webVO.setCodTipoOrd(i.getCd_tip_ord().toString());
				if (i.getTipo_acquisizione() != null) {
					webVO.setTipoAcquisizione(i.getTipo_acquisizione()
							.toString());
				} else {
					webVO.setTipoAcquisizione("");
				}
			}
			if (i.getCd_bib_fatt() == null) {
				webVO.setCodBibF("");
				webVO.setAnnoFattura("");
				webVO.setProgrFattura("");
				webVO.setRigaFattura("");
			} else {
				if (i.getCd_bib_fatt().trim().equals("")) {
					webVO.setCodBibF(i.getCd_bib_fatt());
					webVO.setAnnoFattura("");
					webVO.setProgrFattura("");
					webVO.setRigaFattura("");
				} else {
					webVO.setCodBibF(i.getCd_bib_fatt());
					webVO.setAnnoFattura(i.getAnno_fattura().toString());
					webVO.setProgrFattura(i.getPrg_fattura().toString());
					webVO.setRigaFattura(i.getRiga_fattura().toString());
				}
			}
			if (i.getCd_polo_scar() != null) {
				webVO.setCodPoloScar(i.getCd_polo_scar());
			} else {
				webVO.setCodPoloScar(null);
			}
			if (i.getCd_bib_scar() != null) {
				webVO.setCodBibS(i.getCd_bib_scar());
			} else {
				webVO.setCodBibS("");
			}
			if (i.getId_bib_scar() != null) {
				webVO.setIdBibScar(i.getId_bib_scar());
			} else {
				webVO.setIdBibScar(0);
			}
			if (i.getCd_scarico() != null) {
				webVO.setCodScarico(i.getCd_scarico().toString());
			} else {
				webVO.setCodScarico("");
			}
			if (i.getNum_scarico() != null) {
				webVO.setNumScarico(i.getNum_scarico().toString());
			} else {
				webVO.setNumScarico("");
			}
			if (i.getCd_carico() != null) {
				webVO.setCodCarico(i.getCd_carico().toString());
			} else {
				webVO.setCodCarico("");
			}
			if (i.getNum_carico() != null) {
				webVO.setNumCarico(i.getNum_carico().toString());
			} else {
				webVO.setNumCarico("");
			}
			if (i.getData_redisp_prev() != null) {
				if (i.getData_redisp_prev().equals("")) {
					webVO.setDataRedisp("");
				} else {
					webVO.setDataRedisp(DateUtil.formattaData(i
							.getData_redisp_prev().getTime()));
				}
			} else {
				webVO.setDataRedisp("");
			}

			if (i.getData_carico() != null) {
				if (i.getData_carico().equals("9999-12-31")) {
					webVO.setDataCarico("00/00/0000");
				} else {
					webVO.setDataCarico(DateUtil.formattaData(i
							.getData_carico().getTime()));
				}
			} else {
				webVO.setDataCarico("00/00/0000");
			}
			if (i.getDelibera_scar() != null) {
				webVO.setDeliberaScarico(i.getDelibera_scar());
			} else {
				webVO.setDeliberaScarico("");
			}
			if (i.getData_scarico() != null) {
				if (i.getData_scarico().equals("9999-12-31")) {
					webVO.setDataScarico("00/00/0000");
				} else {
					webVO.setDataScarico(DateUtil.formattaData(i
							.getData_scarico().getTime()));
				}
			} else {
				webVO.setDataScarico("00/00/0000");
			}
			if (i.getData_delibera() != null) {
				if (i.getData_delibera().equals("9999-12-31")) {
					webVO.setDataDelibScar("00/00/0000");
				} else {
					webVO.setDataDelibScar(DateUtil.formattaData(i
							.getData_delibera().getTime()));
				}
			} else {
				webVO.setDataDelibScar("00/00/0000");
			}
			if (i.getSez_old() != null) {
				webVO.setSezOld(i.getSez_old().trim());
			} else {
				webVO.setSezOld("");
			}
			if (i.getLoc_old() != null) {
				webVO.setLocOld(i.getLoc_old());
			} else {
				webVO.setLocOld("");
			}
			if (i.getSpec_old() != null) {
				webVO.setSpecOld(i.getSpec_old());
			} else {
				webVO.setSpecOld("");
			}
			if (i.getKey_loc_old() != null) {
				webVO.setKeyLocOld(i.getKey_loc_old());
			} else {
				webVO.setKeyLocOld(0);
			}
			webVO.setCancDB2i(String.valueOf(i.getFl_canc()));
			if (i.getCd_supporto() != null) {
				webVO.setCodSupporto(i.getCd_supporto());
			} else {
				webVO.setCodSupporto("");
			}
			if (i.getData_ingresso() == null) {
				// inv.setDataIngresso(null);//richiesta rossana 21/02/2012
				webVO.setDataIngresso(DateUtil.formattaData(i.getTs_ins()
						.getTime()));
			} else {
				webVO.setDataIngresso(DateUtil.formattaData(i
						.getData_ingresso().getTime()));
			}
			if (i.getTs_ins_prima_coll() == null) {
				webVO.setDataInsPrimaColl("");
			} else {
				webVO.setDataInsPrimaColl(DateUtil.formattaData(i
						.getTs_ins_prima_coll().getTime()));
			}
			if (i.getUte_ins_prima_coll() == null) {
				webVO.setUteInsPrimaColl("");
			} else {
				webVO.setUteInsPrimaColl(i.getUte_ins_prima_coll().toString());
			}
			if (i.getTs_ins() != null) {
				webVO.setDataIns(DateUtil.formattaData(i.getTs_ins().getTime()));
			} else {
				webVO.setDataIns("");
			}
			if (i.getTs_var() != null) {
				webVO.setDataAgg(DateUtil.formattaData(i.getTs_var().getTime()));
			} else {
				webVO.setDataAgg("");
			}
			if (i.getUte_ins() != null) {
				webVO.setUteIns(i.getUte_ins());
			} else {
				webVO.setUteIns("");
			}
			if (i.getUte_var() != null) {
				webVO.setUteAgg(i.getUte_var());
			} else {
				webVO.setUteAgg("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return webVO;
	}

	public final MovimentoListaVO movimentoLista(Vl_richiesta_servizio hVO) throws Exception {

		MovimentoListaVO webVO = new MovimentoListaVO();

		webVO.setCodRichServ(Long.toString(hVO.getCod_rich_serv()));
		webVO.setIdRichiesta(hVO.getCod_rich_serv());

		if (hVO.getAnno_period() != null)
			webVO.setAnnoPeriodico(hVO.getAnno_period().toString());

		webVO.setCod_erog(hVO.getCod_erog());
		webVO.setCodAttivita(hVO.getCod_attivita());
		webVO.setCodBibDest(hVO.getCod_bib_dest());

		webVO.setCognomeNome(ValidazioneDati.trimOrEmpty(hVO.getCognome()) + " " + ValidazioneDati.trimOrEmpty(hVO.getNome()));

		//webVO.setCodServ(hVO.getId_servizio().getCod_servizio());
		webVO.setCodStatoMov(String.valueOf(hVO.getCod_stato_mov()));
		webVO.setCodStatoRic(String.valueOf(hVO.getCod_stato_rich()));

		webVO.setDataFineEff(hVO.getData_fine_eff());
		webVO.setDataFinePrev(hVO.getData_fine_prev());
		webVO.setDataInizioEff(hVO.getData_in_eff());
		webVO.setDataInizioPrev(hVO.getData_in_prev());

		webVO.setDataRichiesta(hVO.getData_richiesta());

		if (hVO.getData_massima() != null)
			webVO.setDataMax(new java.sql.Date(hVO.getData_massima().getTime()));
		if (hVO.getData_proroga() != null)
			webVO.setDataProroga(new java.sql.Date(hVO.getData_proroga().getTime()));

		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		if (hVO.getFl_condiz() != null)
			webVO.setFlCondiz(hVO.getFl_condiz().toString());

		webVO.setProgrIter(hVO.getProgr_iter() + "");
		webVO.setCodTipoServ(hVO.getCod_tipo_serv());

		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());

		webVO.setCodPolo(hVO.getCd_polo());
		webVO.setCodBibOperante(hVO.getCd_bib());

		//webVO.decode();

		String isbd = ValidazioneDati.trimOrEmpty(hVO.getIsbd());
		Character cd_natura = ValidazioneDati.coalesce(hVO.getCd_natura(), ' ');

		if (hVO.isFl_inventario() ) {
			Tbc_inventario inventario = hVO.getCd_polo_inv();
			if (inventario != null) {
				Tbc_serie_inventariale cd_serie = inventario.getCd_serie();
				webVO.setCodBibInv(cd_serie.getCd_polo().getCd_biblioteca());
				webVO.setCodSerieInv(cd_serie.getCd_serie());
				webVO.setCodInvenInv(inventario.getCd_inven() + "");
			}
			String indice_isbd = hVO.getIndice_isbd();
			if (ValidazioneDati.isFilled(isbd) && ValidazioneDati.isFilled(indice_isbd) ) {
				webVO.setKcles(hVO.getKcles());	//chiave ordinamento
				isbd = ConversioneHibernateVO.toWeb().estraiTitoloPerAreaServizi(isbd, indice_isbd);

				//almaviva5_20100830 per documenti W devo aggiungere in testa il
				//titolo proprio della M superiore (area 200 sottocampo $a)
				if (ValidazioneDati.equals(cd_natura, 'W')) {
					TitoloDAO dao = new TitoloDAO();
					Tb_titolo sup = dao.getMonografiaSuperiore(hVO.getBid());
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

				//almaviva5_20100323 #3561 split del titolo in etichette unimarc
				webVO.setTitolo(isbd);
				webVO.setBid(hVO.getBid());
				webVO.setNatura(cd_natura.toString());

			} else
				webVO.setTitolo(ServiziConstant.MSG_INVENTARIO_CANCELLATO);

			webVO.setSegnatura(ServiziUtil.formattaSegnaturaCollocazione(
					hVO.getCd_sez(), hVO.getCd_loc(), hVO.getSpec_loc(),
					hVO.getSeq_coll(), cd_natura, hVO.getAnno_period()));

			//se anno periodico su mov. é vuoto prendo quello dell'inventario (se presente)
			Integer anno = hVO.getAnno_abb();
			if (ValidazioneDati.strIsNull(webVO.getAnnoPeriodico()) && ValidazioneDati.isFilled(anno))
				webVO.setAnnoPeriodico(String.valueOf(anno));

		}

		if (!hVO.isFl_inventario() ) {
			//doc. non sbn
			Tbl_esemplare_documento_lettore id_esemplare = hVO.getId_esemplare_documenti_lettore();
			if (id_esemplare != null) {
				webVO.setProgrEsempDocLet(id_esemplare.getPrg_esemplare() + "");

				Tbl_documenti_lettori documento = id_esemplare.getId_documenti_lettore();
				webVO.setIdDocumento(documento.getId_documenti_lettore());
				webVO.setCodBibDocLett(hVO.getBib_doc_lett() );
				webVO.setTipoDocLett(hVO.getTipo_doc_lett().toString() );
				webVO.setCodDocLet(hVO.getCod_doc_lett().toString() );
			}

			webVO.setTitolo(isbd);
			webVO.setNatura(cd_natura.toString() );
			webVO.setBid(hVO.getBid());
			//webVO.setCat_fruizione(doc.getCd_catfrui());

			//almaviva5_20100830 chiave titolo fittizia per ordinamento (senza asterisco iniziale)
			webVO.setKcles(OrdinamentoCollocazione2.normalizza(isbd.substring(isbd.indexOf('*') + 1)));

			String segnatura = ValidazioneDati.trimOrEmpty(hVO.getSegnatura());
			if (webVO.isPeriodico())
				segnatura += " [" + webVO.getAnnoPeriodico() + "]";

			webVO.setSegnatura(segnatura);
		}

		Date dataInvio = hVO.getData_invio();
		if (dataInvio != null) {
			Number progrSoll = ValidazioneDati.coalesce(hVO.getProgr_sollecito(), 0);
			webVO.setNumSolleciti(progrSoll.intValue() );
			webVO.setDataUltimoSollecito(DateUtil.formattaData(dataInvio));
			webVO.setData_invio_soll(dataInvio);
		}

		//almaviva5_20170925 servizi ill
		char fl_svolg = hVO.getFl_svolg();
		webVO.setFlSvolg(String.valueOf(fl_svolg));
		if (fl_svolg == 'I') {
			Tbl_dati_richiesta_ill dati_ill = hVO.getDati_richiesta_ill();
			if (dati_ill != null && !ValidazioneDati.in(dati_ill.getFl_canc(), 's', 'S')) {
				DatiRichiestaILLVO datiILL = ConversioneHibernateVO.toWeb().datiRichiestaILL(dati_ill, null);
				datiILL.setMovimento(webVO);
				datiILL.setCod_rich_serv(webVO.getIdRichiesta());
				webVO.setDatiILL(datiILL);
			}
		}

		webVO.setNumVolume(hVO.getNum_volume());

		webVO.decode();

		return webVO;
	}

	public CambioVO cambio(Tba_cambi_ufficiali cu) throws Exception {
		CambioVO webVO = new CambioVO();

		webVO.setIDCambio(cu.getId_valuta());
		webVO.setCodValuta((cu.getValuta().trim().toUpperCase()));
		webVO.setDesValuta(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_VALUTA, webVO.getCodValuta()));

		Tbf_biblioteca_in_polo bib = cu.getCd_bib();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBibl(bib.getCd_biblioteca());

		BigDecimal bd = cu.getCambio();
		bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		webVO.setTassoCambio(bd.doubleValue());

		Date data_var = cu.getData_var();
		webVO.setDataVariazione(String.valueOf(data_var));

		webVO.setValRifer(data_var.equals(Tba_cambi_ufficialiDao.VALUTA_RIF_DATE));


		webVO.setDataUpd(cu.getTs_var());

		return webVO;
	}

	public CollocazioneVO collocazione(Tbc_collocazione hVO) throws Exception {
		CollocazioneVO webVO = new CollocazioneVO();
		Tb_classe classe = hVO.getCd_sistema();
		if (classe != null) {
			webVO.setCodSistema(String.valueOf(classe.getCd_sistema()));
			webVO.setCodEdizione(classe.getCd_edizione());
			webVO.setClasse(classe.getClasse());
		} else {
			webVO.setCodSistema("");
			webVO.setCodEdizione("");
			webVO.setClasse("");
		}
		Tbc_sezione_collocazione sez = hVO.getCd_sez();
		Tbf_biblioteca_in_polo bib = sez.getCd_polo();
		webVO.setCodPoloSez(bib.getCd_polo().getCd_polo());
		webVO.setCodBibSez(bib.getCd_biblioteca());
		webVO.setCodSez(sez.getCd_sez());
		Tbc_esemplare_titolo esempl = hVO.getCd_biblioteca_doc();
		if (esempl != null) {
			webVO.setCodPoloDoc(esempl.getCd_polo().getCd_polo().getCd_polo());
			webVO.setCodBibDoc(esempl.getCd_polo().getCd_biblioteca());
			webVO.setBidDoc(esempl.getB().getBid());
			webVO.setCodDoc(esempl.getCd_doc());
		} else {
			webVO.setCodPoloDoc("");
			webVO.setCodBibDoc("");
			webVO.setBidDoc("");
			webVO.setCodDoc(0);
		}
		webVO.setKeyColloc(hVO.getKey_loc());
		webVO.setBid(hVO.getB().getBid());
		webVO.setCodColloc(hVO.getCd_loc());
		webVO.setSpecColloc(hVO.getSpec_loc());
		if (hVO.getConsis().equals("$")) {
			webVO.setConsistenza("");
		} else {
			webVO.setConsistenza(hVO.getConsis());
		}
		webVO.setTotInv(hVO.getTot_inv());
		if (hVO.getIndice() != null) {
			webVO.setIndice(hVO.getIndice());
		} else {
			webVO.setIndice("");
		}
		webVO.setOrdLoc(hVO.getOrd_loc());
		webVO.setOrdSpec(hVO.getOrd_spec());
		if (hVO.getTot_inv_prov() != null) {
			webVO.setTotInvProv(hVO.getTot_inv_prov());
		} else {
			webVO.setTotInvProv(0);
		}
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setDataIns(String.valueOf(hVO.getTs_ins()));
		webVO.setUteAgg(hVO.getUte_var());
		webVO.setDataAgg(String.valueOf(hVO.getTs_var()));
		webVO.setCancDb2i(String.valueOf(hVO.getFl_canc()));

		// almaviva5_20140627 #5590
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());

		return webVO;
	}

	public CollocazioneDettaglioVO collocazioneDettaglio(Tbc_collocazione hVO)
			throws Exception {
		CollocazioneDettaglioVO webVO = new CollocazioneDettaglioVO();
		Tb_classe classe = hVO.getCd_sistema();
		if (classe != null) {
			webVO.setCodSistema(String.valueOf(classe.getCd_sistema()));
			webVO.setCodEdizione(classe.getCd_edizione());
			webVO.setClasse(classe.getClasse());
		} else {
			webVO.setCodSistema(null);
			webVO.setCodEdizione(null);
			webVO.setClasse(null);
		}
		Tbc_sezione_collocazione sez = hVO.getCd_sez();
		Tbf_biblioteca_in_polo bib = sez.getCd_polo();
		webVO.setCodPoloSez(bib.getCd_polo().getCd_polo());
		webVO.setCodBibSez(bib.getCd_biblioteca());
		webVO.setCodSez(sez.getCd_sez());
		Tbc_esemplare_titolo esempl = hVO.getCd_biblioteca_doc();
		if (esempl != null) {
			webVO.setCodPoloDoc(esempl.getCd_polo().getCd_polo().getCd_polo());
			webVO.setCodBibDoc(esempl.getCd_polo().getCd_biblioteca());
			webVO.setBidDoc(esempl.getB().getBid());
			webVO.setCodDoc(esempl.getCd_doc());
			webVO.setBidDocDescr(esempl.getB().getIsbd());
		} else {
			webVO.setCodPoloDoc(null);
			webVO.setCodBibDoc(null);
			webVO.setBidDoc(null);
			webVO.setCodDoc(0);
		}
		webVO.setKeyColloc(hVO.getKey_loc());
		Tb_titolo tit = hVO.getB();
		webVO.setCodColloc(hVO.getCd_loc());
		webVO.setSpecColloc(hVO.getSpec_loc());
		if (hVO.getConsis().equals("$")) {
			webVO.setConsistenza("");
		} else {
			webVO.setConsistenza(hVO.getConsis());
		}
		webVO.setTotInv(hVO.getTot_inv());
		if (hVO.getIndice() != null) {
			webVO.setIndice(hVO.getIndice());
		} else {
			webVO.setIndice(null);
		}
		webVO.setOrdLoc(hVO.getOrd_loc());
		webVO.setOrdSpec(hVO.getOrd_spec());
		if (hVO.getTot_inv_prov() != null) {
			webVO.setTotInvProv(hVO.getTot_inv_prov());
		} else {
			webVO.setTotInvProv(0);
		}
		if (tit != null) {
			webVO.setBid(tit.getBid());
			List<TitoloVO> titoli = null;
			try {
				titoli = TitoloHibernate.getTitoloHib(tit, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (titoli != null) {
				if (ValidazioneDati.size(titoli) == 1) {
					TitoloVO titolo = titoli.get(0);
					webVO.setBidDescr(titolo.getIsbd());
				}
			} else {
				webVO.setBidDescr("Titolo non trovato in Polo");
			}
		} else {
			webVO.setBidDescr("Titolo non trovato in Polo");
		}
		// coll.setBidDescr(collocazione.getB().getIsbd());
		webVO.setTipoColloc(String.valueOf(sez.getCd_colloc()));
		webVO.setTipoSez(String.valueOf(sez.getTipo_sez()));
		webVO.setCodCla(sez.getCd_cla());
		webVO.setTotInvSez(sez.getTot_inv());// inventari collocati
		webVO.setTotInvMax(sez.getTot_inv_max());// inventari
													// previsti
		if (sez.getDescr().trim().equals("$")) {
			webVO.setDescr("");
		} else {
			webVO.setDescr(sez.getDescr());
		}
		webVO.setNoteSez(sez.getNote_sez());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setDataIns(String.valueOf(hVO.getTs_ins()));
		webVO.setUteAgg(hVO.getUte_var());
		webVO.setDataAgg(String.valueOf(hVO.getTs_var()));
		webVO.setCancDb2i(String.valueOf(hVO.getFl_canc()));

		// almaviva5_20140314
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());

		return webVO;
	}

	public SezioneCollocazioneVO sezioneCollocazione(Tbc_sezione_collocazione hVO) throws DaoManagerException {
		if (hVO == null)
			return null;

		SezioneCollocazioneVO webVO = new SezioneCollocazioneVO();
		String codPolo = hVO.getCd_polo().getCd_polo().getCd_polo();
		String codBib = hVO.getCd_polo().getCd_biblioteca();
		String codSez = hVO.getCd_sez();
		boolean sezioneSpazio = !ValidazioneDati.isFilled(codSez);

		webVO.setSezioneSpazio(sezioneSpazio);
		webVO.setCodSezione(codSez);
		webVO.setCodBib(codBib);
		webVO.setCodPolo(codPolo);
		if (hVO.getNote_sez().trim().equals("$")) {
			webVO.setNoteSezione("");
		} else {
			webVO.setNoteSezione(hVO.getNote_sez().trim());
		}
		webVO.setInventariCollocati(hVO.getTot_inv());
		if (hVO.getDescr().trim().equals("$")) {
			webVO.setDescrSezione("");
		} else {
			webVO.setDescrSezione(hVO.getDescr().trim());
		}
		webVO.setTipoColloc(String.valueOf(hVO.getCd_colloc()));
		webVO.setTipoSezione(String.valueOf(hVO.getTipo_sez()));
		if (hVO.getCd_cla().trim().equals("$")) {
			webVO.setClassific("");
		} else {
			webVO.setClassific(hVO.getCd_cla().trim());
		}
		webVO.setInventariPrevisti(hVO.getTot_inv_max());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setDataIns(String.valueOf(hVO.getTs_ins()));
		webVO.setUteAgg(hVO.getUte_var());
		webVO.setDataAgg(String.valueOf(hVO.getTs_var()));
		webVO.setProgNum(0);
		if (hVO.getCd_colloc() == (DocumentoFisicoCostant.COD_MAGAZZINO_NON_A_FORMATO.charAt(0))) {
			// verifica esistenza di formato con codice 00
			Trc_formati_sezioniDao daoForSez = new Trc_formati_sezioniDao();
			Trc_formati_sezioni recForSez = daoForSez.getFormatiSezioni00(codPolo, codBib, codSez);
			if (recForSez != null) {
				webVO.setProgNum(recForSez.getProg_num());
			} else {
				webVO.setProgNum(0);
			}
		}

		return webVO;
	}

	public JSScriptVO jsScript(Tbf_js_script hVO) {
		JSScriptVO webVO = new JSScriptVO();
		webVO.setKey(hVO.getKey());
		webVO.setDescr(hVO.getDescr());
		webVO.setScript(hVO.getScript());
		return webVO;
	}

	public ElementoSinteticaSoggettoVO elementoSinteticaSoggetto(Tb_soggetto s) {
		ElementoSinteticaSoggettoVO webVO = new ElementoSinteticaSoggettoVO();
		webVO.setCid(s.getCid() );
		webVO.setTesto(s.getDs_soggetto() );
		webVO.setCodiceSoggettario(s.getCd_soggettario());
		webVO.setStato(livelloSoglia(s.getCd_livello()));

		//almaviva5_20120604 evolutive CFI
		Character edizione = s.getCd_edizione();
		webVO.setEdizioneSoggettario(edizione != null ? edizione.toString() : "\u00A0");

		return webVO;
	}

	public SinteticaClasseVO elementoSinteticaClasse(Tb_classe c) throws Exception {
		SinteticaClasseVO webVO = new SinteticaClasseVO();

		webVO.setLivelloAutorita(livelloSoglia(c.getCd_livello()));
		webVO.setParole(c.getDs_classe());

		StringBuilder id = new StringBuilder(32);
		String simbolo = c.getClasse().trim();
		String edizione = c.getCd_edizione().trim();
		String sistema = c.getCd_sistema();
		//almaviva5_20141114 edizione ridotta
		if (sistema.charAt(0) == 'D' && ValidazioneDati.isFilled(edizione)) {
			//dewey
			TB_CODICI cod = CodiciProvider.cercaCodice(edizione,
					CodiciType.CODICE_EDIZIONE_CLASSE,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			edizione = cod != null ? cod.getCd_unimarcTrim() : edizione;
			id.append('D').append(edizione).append(simbolo);
			//sistema = "D";
		}
		else
			//almaviva5_20090414
			id.append(ValidazioneDati.fillRight(sistema, ' ', 3)).append("  ").append(simbolo);

		webVO.setSistema(sistema);
		webVO.setSimbolo(simbolo);
		webVO.setEdDewey(edizione);

		webVO.setIdentificativoClasse(id.toString());

		return webVO;
	}

	public DatiRichiestaILLVO datiRichiestaILL(Tbl_dati_richiesta_ill hVO, Tbl_richiesta_servizio rs) throws DaoManagerException {
		if (hVO == null)
			return null;

		DatiRichiestaILLVO webVO = new DatiRichiestaILLVO();
		webVO.setIdRichiestaILL(hVO.getId_dati_richiesta());
		if (rs != null) {
			webVO.setCod_rich_serv(rs.getCod_rich_serv());
			webVO.setMovimento(ServiziConversioneVO.daHibernateAWebMovimento(rs, Locale.getDefault()));
		}
		webVO.setTransactionId(hVO.getTransactionId());
		webVO.setRequesterId(hVO.getRequesterId());
		webVO.setResponderId(hVO.getResponderId());

		webVO.setCodUtente(hVO.getClientId());
		webVO.setCognomeNome(hVO.getClientName());
		webVO.setUtente_email(hVO.getClient_email());

		webVO.setVia(hVO.getVia());
		webVO.setComune(hVO.getComune());
		webVO.setProv(hVO.getProv());
		webVO.setCap(hVO.getCap());
		webVO.setCd_paese(hVO.getCd_paese());
		webVO.setRequester_email(hVO.getRequester_email());

		webVO.setDataDesiderata(hVO.getDt_data_desiderata());
		webVO.setDataMassima(hVO.getDt_data_massima());
		webVO.setDataScadenza(hVO.getDt_data_scadenza());

		webVO.setCod_erog(hVO.getCod_erog());
		webVO.setCd_supporto(hVO.getCd_supporto());
		webVO.setCd_valuta(hVO.getCd_valuta());
		webVO.setImporto(hVO.getImporto());
		webVO.setCostoMax(hVO.getCosto_max());

		webVO.setUltimoCambioStato(hVO.getTs_last_cambio_stato());

		fillBaseWeb(hVO, webVO);

		//almaviva5_20150219 servizi ill
		webVO.setDocumento(documentoNonSbn(hVO.getDocumento(), null));

		try {
			webVO.setInventario(inventarioTitolo(hVO.getInventario(), Locale.getDefault()));
		} catch (DataException e) {}

		webVO.setCurrentState(hVO.getCd_stato());
		webVO.setServizio(hVO.getCd_servizio());

		boolean richiedente = ValidazioneDati.coalesce(hVO.getFl_ruolo(), 'R') == RuoloBiblioteca.RICHIEDENTE.getFl_ruolo();
		webVO.setRuolo(richiedente ? RuoloBiblioteca.RICHIEDENTE : RuoloBiblioteca.FORNITRICE);
		if (!richiedente) {
			//bib fornitrice: recupero del cod utente assegnato alla bib richiedente
			UtentiDAO dao = new UtentiDAO();
			Tbl_utenti utente = dao.getUtenteByIsil(hVO.getRequesterId());
			if (utente != null) {
				webVO.setCodUtenteBibRichiedente(utente.getCod_utente());
				webVO.setDenominazioneBibRichiedente(utente.getCognome());
			}
		} else {
			//richiedente
			ServiziIllDAO dao = new ServiziIllDAO();
			Tbl_biblioteca_ill bibForn = dao.getBibliotecaByIsil(hVO.getResponderId());
			if (bibForn != null) {
				Tbf_biblioteca bib = bibForn.getId_biblioteca();
				if (bib != null)
					webVO.setResponder_email(bib.getE_mail());
			}
		}

		Set<Tbl_messaggio> messaggio = hVO.getMessaggio();
		if (messaggio != null) {
			for (Tbl_messaggio msg : messaggio)
				webVO.getMessaggio().add(messaggio(msg));
		}

		webVO.setDataInizio(hVO.getData_inizio());
		webVO.setDataFine(hVO.getData_fine());

		Type type = new TypeToken<List<BibliotecaVO>>(){}.getType();
		List<BibliotecaVO> biblioteche = ClonePool.fromJson(hVO.getBiblioteche(), type);
		if (ValidazioneDati.isFilled(biblioteche))
			webVO.setBibliotecheFornitrici(biblioteche);

		//webVO.setIntervalloCopia(hVO.getIntervallo_copia());

		return webVO;
	}

	private MessaggioVO messaggio(Tbl_messaggio hVO) {
		if (hVO == null)
			return null;

		MessaggioVO webVO = new MessaggioVO();
		fillBaseWeb(hVO, webVO);

		webVO.setId_messaggio(hVO.getId_messaggio());
		webVO.setId_dati_richiesta_ill(hVO.getDati_richiesta().getId_dati_richiesta());
		webVO.setStato(hVO.getCd_stato());
		webVO.setCondizione(hVO.getFl_condizione());
		webVO.setDataMessaggio(hVO.getData_messaggio());
		webVO.setTipoInvio(hVO.getFl_tipo() == 'I' ? TipoInvio.INVIATO : TipoInvio.RICEVUTO);
		webVO.setNote(hVO.getNote());

		webVO.setRequesterId(hVO.getRequesterId());
		webVO.setResponderId(hVO.getResponderId());

		boolean richiedente = ValidazioneDati.coalesce(hVO.getFl_ruolo(), 'R') == RuoloBiblioteca.RICHIEDENTE.getFl_ruolo();
		webVO.setRuolo(richiedente ? RuoloBiblioteca.RICHIEDENTE : RuoloBiblioteca.FORNITRICE);

		return webVO;
	}

	private InventarioTitoloVO inventarioTitolo(Tbc_inventario i, Locale locale) throws DataException {
		if (i == null)
			return null;

		InventarioTitoloVO webVO = (InventarioTitoloVO) inventario(new InventarioTitoloVO(), i, locale);
		webVO.setIsbd(i.getB().getIsbd());

		Tbc_collocazione coll = i.getKey_loc();
		if (coll != null) {
			if (coll.getCd_biblioteca_doc() != null
					&& coll.getCd_biblioteca_doc().getFl_canc() != 'S'){
				webVO.setFlEsempl("E");
			}else{
				webVO.setFlEsempl("");
			}
			webVO.setKeyLoc(coll.getKey_loc());
			webVO.setCodSez(coll.getCd_sez().getCd_sez());
			webVO.setCodLoc(coll.getCd_loc());
			webVO.setSpecLoc(coll.getSpec_loc());
			if (coll.getConsis() == null || (coll.getConsis() != null && coll.getConsis().trim().equals("$"))){
				webVO.setConsistenza("");
			}else{
				webVO.setConsistenza(coll.getConsis());
			}
		}

		return webVO;

	}

	public BibliotecaILLVO bibliotecaILL(Tbl_biblioteca_ill hVO, BibliotecaVO bib) {
		if (hVO == null)
			return null;

		BibliotecaILLVO webVO = new BibliotecaILLVO();
		webVO.setId_biblioteca(hVO.getId_biblioteca_ill());
		webVO.setIsil(hVO.getCd_isil());
		webVO.setDescrizione(hVO.getDs_biblioteca());

		Character fl_servprestito = hVO.getFl_servprestito();
		if (fl_servprestito != null)
			webVO.setTipoPrestito(fl_servprestito.toString());
		Character fl_servdd = hVO.getFl_servdd();
		if (fl_servdd != null)
			webVO.setTipoDocDelivery(fl_servdd.toString());
		webVO.setRuolo(Character.toString(hVO.getFl_ruolo()));

		if (bib != null)
			webVO.setBiblioteca(bib);
		else {
			Tbf_biblioteca id_biblioteca = hVO.getId_biblioteca();
			if (id_biblioteca != null)
				webVO.setBiblioteca(anagraficaBiblioteca(id_biblioteca));
		}
		webVO.setUtente(ServiziConversioneVO.daHibernateAWebUtente(hVO.getId_utente()));

		webVO.setTsIns(hVO.getTs_ins());
		webVO.setTsVar(hVO.getTs_var());
		webVO.setUteIns(hVO.getUte_ins());
		webVO.setUteVar(hVO.getUte_var());
		webVO.setFlCanc(String.valueOf(hVO.getFl_canc()));

		return webVO;
	}

}
