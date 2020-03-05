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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.EsemplareFascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.MessaggioFascicoloVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.acquisizioni.Tra_messaggiDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.periodici.PeriodiciDAO;
import it.iccu.sbn.persistence.dao.servizi.AutorizzazioniDAO;
import it.iccu.sbn.persistence.dao.servizi.IterServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.Tbl_documenti_lettoriDAO;
import it.iccu.sbn.persistence.dao.servizi.TipoServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_loc_indice;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sogp_sogi;
import it.iccu.sbn.polo.orm.periodici.Tbp_esemplare_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Trp_messaggio_fascicolo;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_dati_richiesta_ill;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_messaggio;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.util.ConvertiVo.calendario.CalendarioToHibernate;
import it.iccu.sbn.util.ConvertiVo.sale.SaleToHibernate;
import it.iccu.sbn.util.ConvertiVo.utenti.UtentiToHibernate;
import it.iccu.sbn.util.cloning.ClonePool;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.reflect.TypeToken;

public final class ConvertToHibernate extends DataBindingConverter {

	private static final long serialVersionUID = 3710699891392436142L;

	public static CalendarioToHibernate Calendario = new CalendarioToHibernate();
	public static SaleToHibernate Sale = new SaleToHibernate();

	protected ConvertToHibernate() {
		super();
	}

	public final Tbf_biblioteca anagraficaBiblioteca(Tbf_biblioteca entity, BibliotecaVO webVO) {

		Tbf_biblioteca hVO = entity != null ? entity : new Tbf_biblioteca();

		if (webVO.getIdBiblioteca() > 0)
			assignEntityID(hVO, "id_biblioteca", new Integer(webVO.getIdBiblioteca()) );

		//almaviva5_20120319 #4842
		hVO.setCd_polo(ValidazioneDati.isFilled(webVO.getCod_polo()) ? webVO.getCod_polo() : null );
		hVO.setCd_bib(ValidazioneDati.isFilled(webVO.getCod_bib()) ? webVO.getCod_bib() : null );
		hVO.setCd_ana_biblioteca(ValidazioneDati.isFilled(webVO.getCd_ana_biblioteca()) ? webVO.getCd_ana_biblioteca() : null);
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setFl_canc(webVO.getFlCanc().charAt(0));
		hVO.setNom_biblioteca(webVO.getNom_biblioteca());
		hVO.setTipo_biblioteca(ValidazioneDati.isFilled(webVO.getTipo_biblioteca()) ? webVO.getTipo_biblioteca().charAt(0): 'Z');
		hVO.setChiave_ente(webVO.getChiave_ente());
		hVO.setUnit_org(webVO.getUnit_org());
		hVO.setCd_fiscale(webVO.getCod_fiscale());
		hVO.setP_iva(webVO.getP_iva());
		hVO.setIndirizzo(webVO.getIndirizzo());
		hVO.setCap(webVO.getCap());
		hVO.setCpostale(webVO.getCpostale());
		hVO.setLocalita(webVO.getLocalita());
		hVO.setProvincia(webVO.getProvincia());
		hVO.setPaese(webVO.getPaese());
		hVO.setE_mail(webVO.getE_mail());
		hVO.setTelefono(webVO.getTelefono());
		hVO.setNote(webVO.getNote());
		hVO.setFax(webVO.getFax());
		hVO.setCd_bib_cs(webVO.getCod_bib_cs());
		hVO.setCd_bib_ut(webVO.getCod_bib_ut());
		hVO.setCd_utente(Long.valueOf(webVO.getCod_utente()).intValue());
		hVO.setFlag_bib(ValidazioneDati.isFilled(webVO.getFlag_bib()) ? webVO.getFlag_bib().charAt(0) : '\u0020');
		hVO.setChiave_bib(webVO.getChiave_bib());
		hVO.setChiave_ente(webVO.getChiave_ente());
		hVO.setUte_ins(webVO.getUteIns());
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setTs_var(webVO.getTsVar());

		return hVO;
	}

	public  final Tr_sogp_sogi datiCondivisioneSoggetto(
			Tb_soggetto cidPolo, DatiCondivisioneSoggettoVO webVO,
			Tr_sogp_sogi entity) {

		Tr_sogp_sogi hibVO = entity;
		if (hibVO == null) { // solo su nuova riga
			hibVO = new Tr_sogp_sogi();
			hibVO.setTs_ins(webVO.getTsIns());
			hibVO.setUte_ins(webVO.getUteIns());
		}

		hibVO.setUte_var(webVO.getUteVar());
		hibVO.setTs_var(webVO.getTsVar());
		hibVO.setFl_canc(webVO.getFlCanc().charAt(0));

		hibVO.setCid_p(cidPolo);
		hibVO.setCid_i(webVO.getCidIndice());
		hibVO.setBid(webVO.getBid());

		switch (webVO.getOrigineSoggetto()) {
		case INDICE:
			hibVO.setFl_imp_sog('I');
			break;
		case POLO:
			hibVO.setFl_imp_sog('E');
			break;
		}

		switch (webVO.getOrigineLegame()) {
		case NESSUNO:
			hibVO.setFl_imp_tit_sog(null);
			break;
		case INDICE:
			hibVO.setFl_imp_tit_sog('I');
			break;
		case POLO:
			hibVO.setFl_imp_tit_sog('E');
			break;
		}

		switch (webVO.getOrigineModifica()) {
		case NESSUNO:
			hibVO.setFl_sog_mod_da(null);
			break;
		case INDICE:
			hibVO.setFl_sog_mod_da('I');
			break;
		case POLO:
			hibVO.setFl_sog_mod_da('P');
			break;
		}

		return hibVO;
	}

	public  final Tbl_parametri_biblioteca parametriBiblioteca(ParametriBibliotecaVO webVO)
	throws Exception {
		UtilitaDAO dao = new UtilitaDAO();
		Tbl_parametri_biblioteca hVO = new Tbl_parametri_biblioteca();

		assignEntityID(hVO, "id_parametri_biblioteca", webVO.getId() );

		Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(webVO.getCodPolo(), webVO.getCodBib());
		if (bib == null)
			throw new Exception(
					"Biblioteca in polo non trovata in base dati. Cod.polo: "
							+ webVO.getCodPolo() + "  Cod.Bib:"
							+ webVO.getCodBib());
		hVO.setCd_bib(bib);
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setNum_gg_ritardo1(webVO.getGgRitardo1());
		hVO.setNum_gg_ritardo2(webVO.getGgRitardo2());
		hVO.setNum_gg_ritardo3(webVO.getGgRitardo3());
		hVO.setNum_lettere(webVO.getNumeroLettere());
		hVO.setNum_prenotazioni(webVO.getNumeroPrenotazioni());

		String codFruizione = webVO.getCodFruizione();
		hVO.setCd_catfrui_nosbndoc(ValidazioneDati.isFilled(codFruizione) ? codFruizione : null);

		String codRiproduzione = webVO.getCodRiproduzione();
		hVO.setCd_catriprod_nosbndoc(ValidazioneDati.isFilled(codRiproduzione) ? codRiproduzione : null);

		boolean codFruiDaIntervallo = webVO.isCodFruiDaIntervallo();
		hVO.setFl_catfrui_nosbndoc(codFruiDaIntervallo ? 'S' : 'N');

		String codModalitaInvio1Sollecito1 = webVO.getCodModalitaInvio1Sollecito1();
		hVO.setCod_modalita_invio1_sollecito1(ValidazioneDati.isFilled(codModalitaInvio1Sollecito1) ? codModalitaInvio1Sollecito1.charAt(0) : null);

		String codModalitaInvio2Sollecito1 = webVO.getCodModalitaInvio2Sollecito1();
		hVO.setCod_modalita_invio2_sollecito1(ValidazioneDati.isFilled(codModalitaInvio2Sollecito1) ? codModalitaInvio2Sollecito1.charAt(0) : null);

		String codModalitaInvio3Sollecito1 = webVO.getCodModalitaInvio3Sollecito1();
		hVO.setCod_modalita_invio3_sollecito1(ValidazioneDati.isFilled(codModalitaInvio3Sollecito1) ? codModalitaInvio3Sollecito1.charAt(0) : null);

		String codModalitaInvio1Sollecito2 = webVO.getCodModalitaInvio1Sollecito2();
		hVO.setCod_modalita_invio1_sollecito2(ValidazioneDati.isFilled(codModalitaInvio1Sollecito2) ? codModalitaInvio1Sollecito2.charAt(0) : null);

		String codModalitaInvio2Sollecito2 = webVO.getCodModalitaInvio2Sollecito2();
		hVO.setCod_modalita_invio2_sollecito2(ValidazioneDati.isFilled(codModalitaInvio2Sollecito2) ? codModalitaInvio2Sollecito2.charAt(0) : null);

		String codModalitaInvio3Sollecito2 = webVO.getCodModalitaInvio3Sollecito2();
		hVO.setCod_modalita_invio3_sollecito2(ValidazioneDati.isFilled(codModalitaInvio3Sollecito2) ? codModalitaInvio3Sollecito2.charAt(0) : null);

		String codModalitaInvio1Sollecito3 = webVO.getCodModalitaInvio1Sollecito3();
		hVO.setCod_modalita_invio1_sollecito3(ValidazioneDati.isFilled(codModalitaInvio1Sollecito3) ? codModalitaInvio1Sollecito3.charAt(0) : null);

		String codModalitaInvio2Sollecito3 = webVO.getCodModalitaInvio2Sollecito3();
		hVO.setCod_modalita_invio2_sollecito3(ValidazioneDati.isFilled(codModalitaInvio2Sollecito3) ? codModalitaInvio2Sollecito3.charAt(0) : null);

		String codModalitaInvio3Sollecito3 = webVO.getCodModalitaInvio3Sollecito3();
		hVO.setCod_modalita_invio3_sollecito3(ValidazioneDati.isFilled(codModalitaInvio3Sollecito3) ? codModalitaInvio3Sollecito3.charAt(0) : null);

		hVO.setNum_gg_validita_prelazione(webVO.getGgValiditaPrelazione());

		boolean ammessaAutoregistrazioneUtente = webVO.isAmmessaAutoregistrazioneUtente();
		hVO.setAmmessa_autoregistrazione_utente(ammessaAutoregistrazioneUtente ? 'S' : 'N');

		boolean ammessoInserimentoUtente = webVO.isAmmessoInserimentoUtente();
		hVO.setAmmesso_inserimento_utente(ammessoInserimentoUtente ? 'S' : 'N');

		boolean ancheDaRemoto = webVO.isAncheDaRemoto();
		hVO.setAnche_da_remoto(ancheDaRemoto ? 'S' : 'N');

		switch (webVO.getNumeroLettere() ) {
		case 0:
			hVO.setNum_gg_ritardo1((short)0);
			hVO.setNum_gg_ritardo2((short)0);
			hVO.setNum_gg_ritardo3((short)0);

			hVO.setCod_modalita_invio1_sollecito1(null);
			hVO.setCod_modalita_invio2_sollecito1(null);
			hVO.setCod_modalita_invio3_sollecito1(null);

			hVO.setCod_modalita_invio1_sollecito2(null);
			hVO.setCod_modalita_invio2_sollecito2(null);
			hVO.setCod_modalita_invio3_sollecito2(null);

			hVO.setCod_modalita_invio1_sollecito3(null);
			hVO.setCod_modalita_invio2_sollecito3(null);
			hVO.setCod_modalita_invio3_sollecito3(null);
			break;

		case 1:
			hVO.setNum_gg_ritardo2((short)0);
			hVO.setNum_gg_ritardo3((short)0);

			hVO.setCod_modalita_invio1_sollecito2(null);
			hVO.setCod_modalita_invio2_sollecito2(null);
			hVO.setCod_modalita_invio3_sollecito2(null);

			hVO.setCod_modalita_invio1_sollecito3(null);
			hVO.setCod_modalita_invio2_sollecito3(null);
			hVO.setCod_modalita_invio3_sollecito3(null);
			break;

		case 2:
			hVO.setNum_gg_ritardo3((short)0);

			hVO.setCod_modalita_invio1_sollecito3(null);
			hVO.setCod_modalita_invio2_sollecito3(null);
			hVO.setCod_modalita_invio3_sollecito3(null);
			break;
		}

		if (!webVO.isAmmessoInserimentoUtente()) {
			hVO.setAnche_da_remoto('N');
			webVO.setAncheDaRemoto(false);
		}

		hVO.setCd_cat_med_digit(webVO.getCatMediazioneDigit());

		//almaviva5_20150511
		hVO.setXml_modello_soll(ClonePool.toJson(webVO.getModelloSollecito()));

		//almaviva5_20150618
		hVO.setFl_tipo_rinnovo(webVO.getTipoRinnovo());
		hVO.setNum_gg_rinnovo_richiesta(webVO.getGgRinnovoRichiesta());

		//almaviva5_20150630
		hVO.setFl_priorita_prenot(webVO.isPrioritaPrenotazioni() ? 'S' : 'N');

		//almaviva5_20160707
		hVO.setEmail_notifica(webVO.getMailNotifica());

		//almaviva5_20151125
		String codAut = webVO.getAutorizzazioneILL().getCodAutorizzazione();
		if (ValidazioneDati.isFilled(codAut) ) {
			AutorizzazioniDAO adao = new AutorizzazioniDAO();
			hVO.setId_autorizzazione_ill(adao.getTipoAutorizzazione(webVO.getCodPolo(), webVO.getCodBib(), codAut) );
		}

		hVO.setFl_att_servizi_ill(webVO.isServiziILLAttivi() ? 'S' : 'N');

		return hVO;
	}

	public Tbl_documenti_lettori documentoNonSbn(DocumentoNonSbnVO webVO) throws DaoManagerException {

		Tbl_documenti_lettori hVO = new Tbl_documenti_lettori();

		assignEntityID(hVO, "id_documenti_lettore", webVO.getIdDocumento() );

		Tbf_biblioteca_in_polo bibInPolo = DaoManager.creaIdBib(webVO.getCodPolo(), webVO.getCodBib());
		hVO.setCd_bib(bibInPolo);

		String annoEdizione = webVO.getAnnoEdizione();
		if (ValidazioneDati.isFilled(annoEdizione))
			hVO.setAnno_edizione(new BigDecimal(annoEdizione) );
		hVO.setAnnata(webVO.getAnnata());
		hVO.setAutore(webVO.getAutore());
		hVO.setTipo_doc_lett(webVO.getTipo_doc_lett());
		hVO.setCod_doc_lett(webVO.getCod_doc_lett());

		hVO.setData_sugg_lett(webVO.getData_sugg_lett());
		hVO.setFonte(webVO.getFonte());
		hVO.setNatura(webVO.getNatura());
		hVO.setOrd_segnatura(webVO.getOrd_segnatura());
		hVO.setEditore(webVO.getEditore());
		hVO.setLuogo_edizione(webVO.getLuogoEdizione());
		hVO.setSegnatura(webVO.getSegnatura());
		//almaviva5_20160825 #6261 pulizia campo titolo
		String titolo = StringUtils.normalizeSpace(webVO.getTitolo());
		hVO.setTitolo(titolo);
		hVO.setData_sugg_lett(webVO.getData_sugg_lett());

		hVO.setBid(webVO.getBid());
		hVO.setNote(webVO.getNote());
		hVO.setMsg_lettore(webVO.getMessaggioAlLettore() );
		hVO.setCod_bib_inv(webVO.getCod_bib_inv());
		hVO.setCod_serie(webVO.getCod_serie());
		hVO.setCod_inven(webVO.getCod_inven());
		hVO.setNum_volume(webVO.getNum_volume());
		hVO.setPaese(webVO.getPaese());
		hVO.setLingua(webVO.getLingua());
		hVO.setStato_sugg(webVO.getStato_sugg());

		hVO.setCd_catfrui(webVO.getCodFruizione());
		hVO.setCd_no_disp(webVO.getCodNoDisp());
		hVO.setDenom_biblioteca_doc(webVO.getDenominazioneBib());

		hVO.setUte_ins(webVO.getUteIns());
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setTs_var(webVO.getTsVar());

		String flCanc = webVO.getFlCanc();
		hVO.setFl_canc(ValidazioneDati.isFilled(flCanc) ? flCanc.charAt(0) : 'N');

		String utente = webVO.getUtente();
		if (ValidazioneDati.isFilled(utente) )
			try {
				UtentiDAO utenteDAO = new UtentiDAO();
				hVO.setId_utenti(utenteDAO.getUtente(utente) );
			} catch (DaoManagerException e) {
				e.printStackTrace();
			}

		//almaviva5_20141125 servizi ill
		String tp_numero_std = webVO.getTipoNumStd();
		if (ValidazioneDati.isFilled(tp_numero_std) ) {
			hVO.setTp_numero_std(tp_numero_std.charAt(0));
			hVO.setNumero_std(webVO.getNumeroStd());
		}

		List<BibliotecaVO> biblioteche = webVO.getBiblioteche();
		if (ValidazioneDati.isFilled(biblioteche)) {
			Type type = new TypeToken<List<BibliotecaVO>>(){}.getType();
			hVO.setBiblioteche(ClonePool.toJson(new ArrayList<BibliotecaVO>(biblioteche), type) );
		}

		Character tipoRecord = webVO.getTipoRecord();
		if (ValidazioneDati.isFilled(tipoRecord))
			hVO.setTp_record_uni(tipoRecord);

		hVO.setEnte_curatore(webVO.getEnteCuratore());
		hVO.setFa_parte(webVO.getFaParte());
		hVO.setFascicolo(webVO.getFascicolo());
		hVO.setData_pubb(webVO.getDataPubb());
		hVO.setAutore_articolo(webVO.getAutoreArticolo());
		hVO.setTitolo_articolo(webVO.getTitoloArticolo());
		hVO.setPagine(webVO.getPagine());
		hVO.setFonte_rif(webVO.getFonteRif());

		return hVO;
	}

	public final Tbl_modalita_pagamento modalitaPagamento(
			ModalitaPagamentoVO webVO) throws Exception {

		UtilitaDAO dao = new UtilitaDAO();
		Tbl_modalita_pagamento hibernateVO = new Tbl_modalita_pagamento();

		assignEntityID(hibernateVO, "id_modalita_pagamento", webVO.getId() );

		Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(webVO.getCdPolo(), webVO.getCdBib());
		if (bib == null)
			throw new Exception(
				"Biblioteca in polo non trovata in base dati. Cod.polo: "
					+ webVO.getCdPolo() + "  Cod.Bib:"
					+ webVO.getCdBib());

		hibernateVO.setCd_bib(bib);
		hibernateVO.setCod_mod_pag(webVO.getCodModPagamento());
		hibernateVO.setTs_ins(webVO.getTsIns());
		hibernateVO.setUte_ins(webVO.getUteIns());

		hibernateVO.setTs_var(webVO.getTsVar());
		hibernateVO.setUte_var(webVO.getUteVar());
		hibernateVO.setFl_canc(webVO.getFlCanc() == null ? 'N' : webVO.getFlCanc().charAt(0));
		hibernateVO.setDescr(webVO.getDescrizione());

		return hibernateVO;
	}

	public final Tbl_iter_servizio iterServizio(IterServizioVO webVO, int idTipoServizio, boolean nuovo)
	throws Exception {
		IterServizioDAO     iterServizioDAO = new IterServizioDAO();
		TipoServizioDAO     tipoServizioDAO = new TipoServizioDAO();

		Tbl_iter_servizio hibernateVO = null;
		Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizioById(idTipoServizio);

		if (nuovo) {
			hibernateVO  = new Tbl_iter_servizio();
			hibernateVO.setId_tipo_servizio(tipoServizio);
			hibernateVO.setCod_attivita(webVO.getCodAttivita());
			if (!ValidazioneDati.strIsNull(webVO.getFlCanc())) {
				hibernateVO.setFl_canc(webVO.getFlCanc().charAt(0));
			}
			else {
				hibernateVO.setFl_canc('N');
			}
			hibernateVO.setTs_ins(new Timestamp(webVO.getTsIns().getTime()));
			hibernateVO.setUte_ins(webVO.getUteIns());
		} else {
			hibernateVO = iterServizioDAO.getIterServizio(tipoServizio, webVO.getCodAttivita());
		}

		if (!ValidazioneDati.strIsNull(webVO.getCodStatCir()))
			hibernateVO.setCod_stat_cir(webVO.getCodStatCir().charAt(0));
		else
			hibernateVO.setCod_stat_cir(' ');

		if (!ValidazioneDati.strIsNull(webVO.getCodStatoRicIll()))
			hibernateVO.setCod_stato_ric_ill(webVO.getCodStatoRicIll().charAt(0));
		else
			hibernateVO.setCod_stato_ric_ill(' ');

		if (!ValidazioneDati.strIsNull(webVO.getCodStatoRich()))
			hibernateVO.setCod_stato_rich(webVO.getCodStatoRich().charAt(0));
		else
			hibernateVO.setCod_stato_rich(' ');

		if (!ValidazioneDati.strIsNull(webVO.getFlagStampa()))
			hibernateVO.setFlag_stampa(webVO.getFlagStampa().charAt(0));
		else
			hibernateVO.setFlag_stampa(' ');

		if (!ValidazioneDati.strIsNull(webVO.getFlgAbil()))
			hibernateVO.setFlg_abil(webVO.getFlgAbil().charAt(0));
		else
			hibernateVO.setFlg_abil(' ');

		if (!ValidazioneDati.strIsNull(webVO.getFlgRinn()))
			hibernateVO.setFlg_rinn(webVO.getFlgRinn().charAt(0));
		else
			hibernateVO.setFlg_rinn(' ');

		if (!ValidazioneDati.strIsNull(webVO.getObbl()))
			hibernateVO.setObbl(webVO.getObbl().charAt(0));
		else
			hibernateVO.setObbl(' ');

		hibernateVO.setProgr_iter(webVO.getProgrIter());

		if (!ValidazioneDati.strIsNull(webVO.getStatoIter()))
			hibernateVO.setStato_iter(webVO.getStatoIter().charAt(0));
		else
			hibernateVO.setStato_iter(' ');

		if (!ValidazioneDati.strIsNull(webVO.getCodStatoMov()))
			hibernateVO.setStato_mov(webVO.getCodStatoMov().charAt(0));
		else
			hibernateVO.setStato_mov(' ');

		hibernateVO.setTesto(webVO.getTesto());
		hibernateVO.setNum_pag(webVO.getNumPag());
		hibernateVO.setTs_var(new Timestamp(webVO.getTsVar().getTime()));
		hibernateVO.setUte_var(webVO.getUteVar());

		return hibernateVO;
	}

	public static UtentiToHibernate Utenti = new UtentiToHibernate();

	public final Tbl_esemplare_documento_lettore esemplareDocumentoNonSbn(
			Tbl_documenti_lettori documento, int idDocumento,
			EsemplareDocumentoNonSbnVO webVO) throws DaoManagerException {

		Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
		Tbl_esemplare_documento_lettore hibernateVO = new Tbl_esemplare_documento_lettore();

		assignEntityID(hibernateVO, "id_esemplare", webVO.getIdEsemplare() );

		if (documento != null)
			hibernateVO.setId_documenti_lettore(documento);
		else
			if (idDocumento != 0)
				hibernateVO.setId_documenti_lettore(dao.getDocumentoLettoreById(idDocumento));

		hibernateVO.setPrg_esemplare(webVO.getPrg_esemplare());

		String annata = webVO.getAnnata();
		if (ValidazioneDati.isFilled(annata))
		{
			hibernateVO.setPrecisazione(annata);
		}
		else
		{
			hibernateVO.setPrecisazione(null);
		}
/*		try {
			if (ValidazioneDati.isFilled(annata))
			{
				hibernateVO.setAnnata(annata.charAt(0));
			}
			else
				hibernateVO.setAnnata(null);

		} catch (ParseException e) {
			hibernateVO.setAnnata(null);
			e.printStackTrace();
		}
*/
		hibernateVO.setInventario(webVO.getInventario());
		hibernateVO.setCod_no_disp(webVO.getCodNoDisp());
		hibernateVO.setFonte(webVO.getFonte());

		hibernateVO.setTs_ins(webVO.getTsIns());
		hibernateVO.setUte_ins(webVO.getUteIns());

		hibernateVO.setTs_var(webVO.getTsVar());
		hibernateVO.setUte_var(webVO.getUteVar());
		hibernateVO.setFl_canc(ValidazioneDati.strIsNull(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));

		return hibernateVO;
	}

	public final Tb_codici elementoTabellaCodici(CodiceVO webVO) {

		Tb_codici hibernateVO = new Tb_codici();
		hibernateVO.setTp_tabella(webVO.getNomeTabella());
		hibernateVO.setCd_tabella(webVO.getCdTabella());
		hibernateVO.setDs_tabella(webVO.getDescrizione());
		hibernateVO.setCd_unimarc(webVO.getCd_unimarc());
		hibernateVO.setCd_marc_21(webVO.getCd_marc21());
		hibernateVO.setDs_cdsbn_ulteriore(webVO.getDs_cdsbn_ulteriore());

		String materiale = webVO.getMateriale();
		hibernateVO.setTp_materiale(ValidazioneDati.isFilled(materiale) ?  materiale.charAt(0) : null);

		hibernateVO.setCd_flg1(webVO.getFlag1());
		hibernateVO.setCd_flg2(webVO.getFlag2());
		hibernateVO.setCd_flg3(webVO.getFlag3());
		hibernateVO.setCd_flg4(webVO.getFlag4());
		hibernateVO.setCd_flg5(webVO.getFlag5());
		hibernateVO.setCd_flg6(webVO.getFlag6());
		hibernateVO.setCd_flg7(webVO.getFlag7());
		hibernateVO.setCd_flg8(webVO.getFlag8());
		hibernateVO.setCd_flg9(webVO.getFlag9());
		hibernateVO.setCd_flg10(webVO.getFlag10());
		hibernateVO.setCd_flg11(webVO.getFlag11());

		hibernateVO.setDt_fine_validita(DateUtil.lastDayOfYear(9999));

		String dataInserita = webVO.getDataAttivazione();
		if (dataInserita != null)
			try {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date = format.parse(dataInserita);
				hibernateVO.setDt_fine_validita(date);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return hibernateVO;
	}

	public final Tbp_fascicolo fascicolo(FascicoloVO webVO) throws Exception {
		Tbp_fascicolo hVO = new Tbp_fascicolo();
		ClonePool.copyCommonProperties(hVO, webVO);
		TitoloDAO dao = new TitoloDAO();
		hVO.setTitolo(dao.getTitolo(webVO.getBid()) );
		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setFl_canc(ValidazioneDati.strIsNull(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));
		return hVO;
	}

	public final Tbp_esemplare_fascicolo esemplareFascicolo(EsemplareFascicoloVO webVO) throws Exception {

		if (webVO == null)
			return null;

		int id_ordine = webVO.getId_ordine();
		int cd_inven = webVO.getCd_inven();

		Tbp_esemplare_fascicolo hVO = new Tbp_esemplare_fascicolo();
		ClonePool.copyCommonProperties(hVO, webVO);

		PeriodiciDAO dao = new PeriodiciDAO();
		hVO.setFascicolo(dao.getFascicolo(webVO.getBid(), webVO.getFid()));

		if (id_ordine > 0) {
			Tba_ordiniDao odao = new Tba_ordiniDao();
			hVO.setOrdine(odao.getOrdineById(id_ordine));
		} else {
			String bib_ord = webVO.getCod_bib_ord();
			if (ValidazioneDati.isFilled(bib_ord)) {
				Tba_ordiniDao odao = new Tba_ordiniDao();
				hVO.setOrdine(odao.getOrdine(
								webVO.getCodPolo(),
								bib_ord,
								webVO.getAnno_ord(),
								webVO.getCod_tip_ord(),
								webVO.getCod_ord()));
			}
		}

		if (cd_inven > 0) {
			Tbc_inventarioDao idao = new Tbc_inventarioDao();
			hVO.setInventario(idao.getInventario(webVO.getCodPolo(), webVO.getCodBib(), webVO.getCd_serie(), cd_inven));
		}

		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setFl_canc(ValidazioneDati.strIsNull(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));

		return hVO;
	}

	public final Trp_messaggio_fascicolo messaggioFascicolo(MessaggioFascicoloVO webVO) throws Exception {

		if (webVO == null)
			return null;

		int id_ordine = webVO.getId_ordine();

		Trp_messaggio_fascicolo hVO = new Trp_messaggio_fascicolo();
		ClonePool.copyCommonProperties(hVO, webVO);

		PeriodiciDAO dao = new PeriodiciDAO();
		hVO.setFascicolo(dao.getFascicolo(webVO.getBid(), webVO.getFid()));

		if (id_ordine > 0) {
			Tba_ordiniDao odao = new Tba_ordiniDao();
			hVO.setOrdine(odao.getOrdineById(id_ordine));
		} else {
			String bib_ord = webVO.getCod_bib_ord();
			if (ValidazioneDati.isFilled(bib_ord)) {
				Tba_ordiniDao odao = new Tba_ordiniDao();
				hVO.setOrdine(odao.getOrdine(
								webVO.getCodPolo(),
								bib_ord,
								webVO.getAnno_ord(),
								webVO.getCod_tip_ord(),
								webVO.getCod_ord()));
			}
		}

		Tra_messaggiDao mdao = new Tra_messaggiDao();
		hVO.setMessaggio(mdao.getComunicazione(webVO.getCodPolo(), webVO.getCodBib(), webVO.getCod_msg() ));

		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setFl_canc(ValidazioneDati.strIsNull(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));

		return hVO;
	}

	public Tra_ordine_carrello_spedizione ordineCarrelloSpedizione(OrdineCarrelloSpedizioneVO webVO) {

		if (webVO == null)
			return null;

		Tra_ordine_carrello_spedizione hVO = new Tra_ordine_carrello_spedizione();
		hVO.setIdOrdine(webVO.getIdOrdine());
		hVO.setDt_spedizione(webVO.getDataSpedizione());
		hVO.setPrg_spedizione(webVO.getPrgSpedizione());
		hVO.setCart_name(webVO.getCartName());

		hVO.setTs_ins(webVO.getTsIns());
		hVO.setUte_ins(webVO.getUteIns());

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setFl_canc(ValidazioneDati.strIsNull(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));

		return hVO;
	}

	public Tb_loc_indice localizzazione(AreaDatiLocalizzazioniAuthorityVO loc) {
		Tb_loc_indice hVO = new Tb_loc_indice();
		hVO.setFl_canc('N');
		return hVO;
	}

	public Tbl_dati_richiesta_ill datiRichiestaILL(Tbl_dati_richiesta_ill hVO, DatiRichiestaILLVO webVO) throws DaoManagerException {
		if (webVO == null)
			return null;

		if (hVO == null) {
			hVO = new Tbl_dati_richiesta_ill();
			if (webVO.getCod_rich_serv() > 0) {
				RichiesteServizioDAO dao = new RichiesteServizioDAO();
				Tbl_richiesta_servizio rs = dao.getMovimentoById(webVO.getCod_rich_serv());
				hVO.setRichiesta(rs);
			}
		}

		DocumentoNonSbnVO doc = webVO.getDocumento();
		if (doc != null) {
			Tbl_documenti_lettoriDAO dao = new Tbl_documenti_lettoriDAO();
			hVO.setDocumento(dao.getDocumentoLettoreById(doc.getIdDocumento()));
		} else
			hVO.setDocumento(null);

		InventarioVO inv = webVO.getInventario();
		if (inv != null) {
			Tbc_inventarioDao dao = new Tbc_inventarioDao();
			hVO.setInventario(dao.getInventario(inv.getCodPolo(), inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent()));
		} else
			hVO.setInventario(null);

		hVO.setId_dati_richiesta(webVO.getIdRichiestaILL());
		hVO.setTransactionId(webVO.getTransactionId());
		hVO.setRequesterId(webVO.getRequesterId());
		hVO.setResponderId(webVO.getResponderId());

		hVO.setClientId(webVO.getCodUtente());
		hVO.setClientName(webVO.getCognomeNome());
		hVO.setClient_email(webVO.getUtente_email());

		hVO.setVia(webVO.getVia());
		hVO.setCap(webVO.getCap());
		hVO.setComune(webVO.getComune());
		hVO.setProv(webVO.getProv());
		hVO.setCd_paese(webVO.getCd_paese());
		hVO.setRequester_email(webVO.getRequester_email());

		hVO.setCd_valuta(webVO.getCd_valuta());
		hVO.setImporto(getBigDecimal(webVO.getImporto(), true));
		hVO.setCosto_max(getBigDecimal(webVO.getCostoMax(), true));

		hVO.setCod_erog(webVO.getCod_erog());
		hVO.setCd_supporto(webVO.getCd_supporto());

		hVO.setDt_data_desiderata(webVO.getDataDesiderata());
		hVO.setDt_data_massima(webVO.getDataMassima());
		hVO.setDt_data_scadenza(webVO.getDataScadenza());

		fillBaseHibernate(hVO, webVO);

		hVO.setCd_stato(webVO.getCurrentState());
		hVO.setFl_ruolo(webVO.getRuolo().getFl_ruolo() );
		hVO.setCd_servizio(webVO.getServizio());

		hVO.setData_inizio(webVO.getDataInizio());
		hVO.setData_fine(webVO.getDataFine());
		hVO.setTs_last_cambio_stato(webVO.getUltimoCambioStato());

		List<BibliotecaVO> biblioteche = webVO.getBibliotecheFornitrici();
		if (ValidazioneDati.isFilled(biblioteche)) {
			Type type = new TypeToken<List<BibliotecaVO>>(){}.getType();
			hVO.setBiblioteche(ClonePool.toJson(new ArrayList<BibliotecaVO>(biblioteche), type) );
		}

		//hVO.setIntervallo_copia(webVO.getIntervalloCopia());

		return hVO;
	}

	public Tbl_biblioteca_ill bibliotecaILL(Tbl_biblioteca_ill old, BibliotecaILLVO webVO) throws Exception {
		Tbl_biblioteca_ill hVO;

		if (old != null) {
			hVO = old;
		} else {
			hVO = new Tbl_biblioteca_ill();
			hVO.setId_biblioteca_ill(webVO.getId_biblioteca());
			hVO.setTs_ins(webVO.getTsIns());
			hVO.setUte_ins(webVO.getUteIns());
		}

		hVO.setTs_var(webVO.getTsVar());
		hVO.setUte_var(webVO.getUteVar());
		hVO.setFl_canc(!isFilled(webVO.getFlCanc()) ? 'N' : webVO.getFlCanc().charAt(0));

		hVO.setCd_isil(webVO.getIsil());
		hVO.setDs_biblioteca(webVO.getDescrizione());
		hVO.setFl_ruolo(getFlag(webVO.getRuolo(), true));
		hVO.setFl_servprestito(getFlag(webVO.getTipoPrestito(), true));
		hVO.setFl_servdd(getFlag(webVO.getTipoDocDelivery(), true));

		BibliotecaVO bib = webVO.getBiblioteca();
		if (bib != null) {
			Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
			hVO.setId_biblioteca(dao.getBiblioteca(bib.getIdBiblioteca()));
		}

		UtenteBaseVO utente = webVO.getUtente();
		if (utente != null) {
			UtentiDAO dao = new UtentiDAO();
			hVO.setId_utente(dao.getUtenteAnagraficaById(utente.getIdUtente()));
		}

		return hVO;
	}

	public Tbl_messaggio messaggio(MessaggioVO webVO) throws DaoManagerException {
		if (webVO == null)
			return null;

		Tbl_messaggio hVO = new Tbl_messaggio();
		fillBaseHibernate(hVO, webVO);

		hVO.setId_messaggio(webVO.getId_messaggio());
		hVO.setCd_stato(webVO.getStato());
		hVO.setFl_condizione(webVO.getCondizione());
		hVO.setNote(webVO.getNote());
		hVO.setData_messaggio(webVO.getDataMessaggio());
		hVO.setFl_tipo(webVO.getTipoInvio().getFl_tipo());

		int id_dati_richiesta_ill = webVO.getId_dati_richiesta_ill();
		if (id_dati_richiesta_ill > 0) {
			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			hVO.setDati_richiesta(dao.getDatiRichiestaIll(id_dati_richiesta_ill));
		}

		hVO.setRequesterId(webVO.getRequesterId());
		hVO.setResponderId(webVO.getResponderId());
		hVO.setFl_ruolo(webVO.getRuolo().getFl_ruolo() );

		return hVO;
	}

	public final Tbc_serie_inventariale serieInventariale(Tbc_serie_inventariale hVO, SerieVO webVO) {
		if (hVO == null)
			hVO = new Tbc_serie_inventariale();

		hVO.setPrg_inv_corrente(Integer.parseInt(webVO.getProgAssInv()));
		hVO.setPrg_inv_pregresso(Integer.parseInt(webVO.getPregrAssInv()));
		hVO.setDt_ingr_inv_preg(DateUtil.toDate(webVO.getDataIngrPregr()));
		hVO.setNum_man(Integer.parseInt(webVO.getNumMan()));
		hVO.setDt_ingr_inv_man(DateUtil.toDate(webVO.getDataIngrMan()));
		hVO.setFlg_chiusa((webVO.getFlChiusa().charAt(0)));
		hVO.setFl_default((webVO.getFlDefault().charAt(0)));

		hVO.setInizio_man(Integer.parseInt(webVO.getInizioMan1()));
		hVO.setFine_man(Integer.parseInt(webVO.getFineMan1()));
		hVO.setDt_ingr_inv_ris1(DateUtil.toDate(webVO.getDataIngrRis1()));
		hVO.setInizio_man2(Integer.parseInt(webVO.getInizioMan2()));
		hVO.setFine_man2(Integer.parseInt(webVO.getFineMan2()));
		hVO.setDt_ingr_inv_ris2(DateUtil.toDate(webVO.getDataIngrRis2()));
		hVO.setInizio_man3(Integer.parseInt(webVO.getInizioMan3()));
		hVO.setFine_man3(Integer.parseInt(webVO.getFineMan3()));
		hVO.setDt_ingr_inv_ris3(DateUtil.toDate(webVO.getDataIngrRis3()));
		hVO.setInizio_man4(Integer.parseInt(webVO.getInizioMan4()));
		hVO.setFine_man4(Integer.parseInt(webVO.getFineMan4()));
		hVO.setDt_ingr_inv_ris4(DateUtil.toDate(webVO.getDataIngrRis4()));
		hVO.setUte_var(webVO.getUteAgg());

		if (!isFilled(webVO.getDescrSerie())) {
			hVO.setDescr("$");
		} else {
			hVO.setDescr(webVO.getDescrSerie());
		}

		return hVO;
	}

}
