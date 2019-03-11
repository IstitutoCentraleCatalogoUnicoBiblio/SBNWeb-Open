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
package it.iccu.sbn.persistence.dao.semantica;

import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.gestionesemantica.CountLegamiSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiCondivisioneSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch.ParametriCancellazioneSoggettiNonUtilizzatiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSistemaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_classe;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_descrittore;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_soggetto;
import it.iccu.sbn.polo.orm.gestionesemantica.Tb_termine_thesauro;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_des_des;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sistemi_classi_biblioteche;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sog_des;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sogp_sogi;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_tit_sog_bib;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.jasper.BatchCollectionSerializer;
import it.iccu.sbn.util.semantica.SemanticaUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

public class SemanticaDAO extends DaoManager {

	public SubReportVO stampaSoggettario(ParametriStampaSoggettarioVO richiesta, BatchLogWriter logger)
			throws DaoManagerException {

		Logger _log = logger.getLogger();

		Session session = getCurrentSession();
		SubReportVO output;
		try {
			Criteria c = session.createCriteria(Tb_soggetto.class);
			addTimeStampRange(c, "ts_ins", richiesta.getTsIns_da(), richiesta.getTsIns_a());
			addTimeStampRange(c, "ts_var", richiesta.getTsVar_da(), richiesta.getTsVar_a())
					.add(Restrictions.eq("cd_soggettario", richiesta.getCodSoggettario()))
					.add(Restrictions.ne("fl_canc", 'S'));

			//almaviva5_20120604 evolutive CFI
			String edizione = richiesta.getEdizioneSoggettario();
			if (ValidazioneDati.isFilled(edizione)) {
				SbnEdizioneSoggettario cd_edizione = SbnEdizioneSoggettario.valueOf(edizione);
				c.add(Restrictions.in("cd_edizione", SemanticaUtil.getEdizioniRicerca(cd_edizione)));
			}

			c.addOrder(Order.asc("ky_cles1_s"))	//ordino per chiave normalizzata
					.addOrder(Order.asc("ds_soggetto"))
					.addOrder(Order.asc("cid"));

			// stampo solo soggetti legati a titoli localizzati in biblioteca
			if (richiesta.isStampaSoloUtilizzati())
				c.add(addFiltroSoggettiUtilizzati(richiesta.getCodPolo(), richiesta.getCodBib()));

			//filtro condivisi
			if (richiesta.isCondiviso())
				c.add(Restrictions.eq("fl_condiviso", 's'));

			//filtro testo
			String ky_from = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(richiesta.getTestoFrom()));
			String ky_to = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(richiesta.getTestoTo()));
			if (ValidazioneDati.isFilled(ky_from) || ValidazioneDati.isFilled(ky_to))
				c.add(Restrictions.between("ky_cles1_s",
						ky_from, ValidazioneDati.fillRight(ky_to, 'Z', 240)));

			ScrollableResults cursor = c.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);

			BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch(richiesta);
			output = bcs.startReport("SOGGETTI");
			int soggCount = 0;

			while (cursor.next() ) {
				try {
					if ((++soggCount % 50) == 0)
						BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
				} catch (BatchInterruptedException e) {
					cursor.close();
					throw new DaoManagerException(e);
				}


				Tb_soggetto soggetto = (Tb_soggetto) cursor.get(0);

				ElementoStampaSoggettoVO elemento = ConversioneHibernateVO.toWeb().elementoStampaSoggettoDecodificato(soggetto);

				// stampa descrittori manuali
				if (richiesta.isStampaDescrittoriManuali()) {
					List<ElementoStampaDescrittoreVO> descrittori = estraiDescrittoriManuali(soggetto);
					if (ValidazioneDati.isFilled(descrittori)) {
						SubReportVO subDes = bcs.startReport("DESCRITTORI");
						for (ElementoStampaDescrittoreVO d : descrittori)
							bcs.writeVO("DESCRITTORI", d);
						elemento.setSubReportDescrittori(subDes);
						bcs.endReport("DESCRITTORI");
					}
				}

				//stampa titoli collegati
				if (richiesta.isStampaTitoli())  {
					List<LegameTitoloVO> titoli = estraiLegamiTitolo(soggetto);
					if (ValidazioneDati.isFilled(titoli)) {
						SubReportVO subTit = bcs.startReport("TITOLI");
						for (LegameTitoloVO tit : titoli)
							bcs.writeVO("TITOLI", tit);
						elemento.setSubReportTitoli(subTit);
						bcs.endReport("TITOLI");
					}
				}

				bcs.writeVO("SOGGETTI", elemento);
				session.clear();

				if ((soggCount % 500) == 0)
					_log.debug(soggCount + " soggetti estratti");

			}

			//jcs.close();
			cursor.close();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

		return output;
	}


	public SubReportVO stampaDescrittori(ParametriStampaDescrittoriVO richiesta, BatchLogWriter logger)
			throws DaoManagerException {

		Logger _log = logger.getLogger();

		Session session = getCurrentSession();
		SubReportVO output;
		try {
			Criteria c = session.createCriteria(Tb_descrittore.class);
			addTimeStampRange(c, "ts_ins", richiesta.getTsIns_da(), richiesta.getTsIns_a());
			addTimeStampRange(c, "ts_var", richiesta.getTsVar_da(), richiesta.getTsVar_a())
					.add(Restrictions.eq("cd_soggettario", richiesta.getCodSoggettario()))
					.add(Restrictions.ne("fl_canc", 'S'));

			//almaviva5_20120604 evolutive CFI
			String edizione = richiesta.getEdizioneSoggettario();
			if (ValidazioneDati.isFilled(edizione)) {
				SbnEdizioneSoggettario cd_edizione = SbnEdizioneSoggettario.valueOf(edizione);
				c.add(Restrictions.in("cd_edizione", SemanticaUtil.getEdizioniRicerca(cd_edizione)));
			}

			//filtro forma nome
			if (!richiesta.isStampaFormeRinvio())
				c.add(Restrictions.eq("tp_forma_des", SbnFormaNome.A.toString()));

			//solo descrittori ins/mod dalla biblioteca
			if (richiesta.isStampaInsModBiblioteca()) {
				String bib = richiesta.getCodPolo() + richiesta.getCodBib();
				SimpleExpression r1 = Restrictions.like("ute_ins", bib, MatchMode.START);
				SimpleExpression r2 = Restrictions.like("ute_var", bib, MatchMode.START);
				LogicalExpression or = Restrictions.or(r1, r2);
				c.add(or);
			}

			//stampa solo descrittori legati a soggetti condivisi
			if (richiesta.isCondiviso())
				c.add(addFiltroStampaLegatiSoggettiCondivisi() );

			//filtro testo
			String ky_from = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(richiesta.getTestoFrom()));
			String ky_to = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(richiesta.getTestoTo()));
			if (ValidazioneDati.isFilled(ky_from) || ValidazioneDati.isFilled(ky_to))
				c.add(Restrictions.between("ky_norm_descritt",
						ky_from, ValidazioneDati.fillRight(ky_to, 'Z', 240)));

			// stampa descrittori legati a soggetti legati a titoli localizzati in biblioteca
			if (richiesta.isStampaSoloUtilizzati())
				c.add(addFiltroDescrittoriDiSoggettiUtilizzati(richiesta.getCodPolo(), richiesta.getCodBib()));

			c.addOrder(Order.asc("ky_norm_descritt"))	//ordinamento per chiave normalizzata
					.addOrder(Order.asc("ds_descrittore"))
					.addOrder(Order.asc("did"));

			ScrollableResults cursor = c.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
			BatchCollectionSerializer bcs = BatchCollectionSerializer.forBatch(richiesta);
			output = bcs.startReport("DESCRITTORI");
			int desCount = 0;

			while (cursor.next() ) {

				try {
					if ((++desCount % 50) == 0)
						BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
				} catch (BatchInterruptedException e) {
					cursor.close();
					throw new DaoManagerException(e);
				}

				Tb_descrittore descrittore = (Tb_descrittore) cursor.get(0);

				ElementoStampaDescrittoreVO elemento = ConversioneHibernateVO.toWeb().elementoStampaDescrittoreDecodificato(descrittore);

				// stampa descrittori manuali
				if (richiesta.isStampaDescrittoriManuali()) {
					List<ElementoStampaDescrittoreVO> descrittori = estraiDescrittoriCollegati(descrittore);
					if (ValidazioneDati.isFilled(descrittori)) {
						SubReportVO subDes = bcs.startReport("DESDES");
						for (ElementoStampaDescrittoreVO des : descrittori)
							bcs.writeVO("DESDES", des);
						elemento.setSubReportDescrittori(subDes);
						bcs.endReport("DESDES");
					}
				}

				// stampa soggetti collegati
				if (richiesta.isStampaSoggettiCollegati()) {
					List<ElementoStampaSoggettoVO> soggetti = estraiSoggettiCollegati(descrittore);
					if (ValidazioneDati.isFilled(soggetti)) {
						SubReportVO subSogg = bcs.startReport("SOGGETTI");
						for (ElementoStampaSoggettoVO sogg : soggetti)
							bcs.writeVO("SOGGETTI", sogg);
						elemento.setSubReportSoggetti(subSogg);
						bcs.endReport("SOGGETTI");
					}
				}

				bcs.writeVO("DESCRITTORI", elemento);
				session.clear();

				if ((desCount % 500) == 0)
					_log.debug(desCount + " descrittori estratti");
			}

			cursor.close();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

		return output;
	}


	private Criterion addFiltroStampaLegatiSoggettiCondivisi() {
		StringBuilder sql = new StringBuilder();
		sql.append("exists (");
		sql.append(" select 1 from tr_sog_des sd ");
		sql.append(" inner join tb_soggetto s on s.cid=sd.cid ");
		sql.append(" where sd.fl_canc<>'S'");
		sql.append(" and sd.did={alias}.did");
		sql.append(" and s.fl_canc<>'S'");
		sql.append(" and s.fl_condiviso='").append(DatiElementoTypeCondivisoType.S.toString()).append("'");
		sql.append(")");

		return Restrictions.sqlRestriction(sql.toString());
	}


	private Criterion addFiltroDescrittoriDiSoggettiUtilizzati(String codPolo, String codBib) {
		StringBuilder sql = new StringBuilder();
		sql.append("exists (");
		sql.append(" select titsog.bid from tr_tit_sog_bib titsog");
		sql.append(" inner join tr_tit_bib titbib on titbib.bid=titsog.bid");
		sql.append(" inner join tr_sog_des sogdes on sogdes.cid=titsog.cid");
		sql.append(" where titsog.fl_canc<>'S'");
		sql.append(" and titbib.fl_canc<>'S'");
		sql.append(" and sogdes.fl_canc<>'S'");
		sql.append(" and titbib.fl_possesso='S'");
		sql.append(" and titsog.cid=sogdes.cid");
		sql.append(" and sogdes.did={alias}.did");
		sql.append(" and titbib.cd_polo='").append(codPolo).append("' and titbib.cd_biblioteca='").append(codBib).append("'");
		sql.append(")");

		return Restrictions.sqlRestriction(sql.toString());
	}


	private List<LegameTitoloVO> estraiLegamiTitolo(Tb_soggetto soggetto) throws DaoManagerException {
		List<LegameTitoloVO> legami = new ArrayList<LegameTitoloVO>();
		Set titoli = soggetto.getTr_tit_sog_bib();
		Query query = getCurrentSession().createFilter(titoli, "where fl_canc<>'S'" +
				" order by B.ky_cles1_t,B.ky_cles2_t,B.bid");

		for (Object ts : query.list()) {
			Tr_tit_sog_bib titSog = (Tr_tit_sog_bib) ts;
			Tb_titolo tit = titSog.getB();
			LegameTitoloVO leg = new LegameTitoloVO();
			leg.setBid(tit.getBid());
			leg.setTitolo(tit.getIsbd());
			leg.setNotaLegame(titSog.getNota_tit_sog_bib());
			legami.add(leg);
		}

		return legami;
	}


	private List<ElementoStampaDescrittoreVO> estraiDescrittoriManuali(Tb_soggetto soggetto) throws DaoManagerException {
		String kySogg = soggetto.getKy_cles1_s().trim();
		List<ElementoStampaDescrittoreVO> descrittori = new ArrayList<ElementoStampaDescrittoreVO>();
		Set sogDes = soggetto.getTr_sog_des();
		Query query = getCurrentSession().createFilter(sogDes, "where fl_canc<>'S' order by D.ky_norm_descritt");

		for (Object sd : query.list()) {
			// se la chiave normalizzata del descrittore é contenuta
			// nella chiave soggetto
			Tb_descrittore des = ((Tr_sog_des)sd).getD();
			if (kySogg.indexOf(des.getKy_norm_descritt().trim()) > -1)
				continue;	// automatico

			descrittori.add(ConversioneHibernateVO.toWeb().elementoStampaDescrittore(des, null));
		}

		return descrittori;
	}

	private List<ElementoStampaSoggettoVO> estraiSoggettiCollegati(Tb_descrittore descrittore) throws Exception {
		List<ElementoStampaSoggettoVO> soggetti = new ArrayList<ElementoStampaSoggettoVO>();
		Set sogDes = descrittore.getTr_sog_des();
		Query query = getCurrentSession().createFilter(sogDes, "where fl_canc<>'S' order by C.ky_cles1_s");
		for (Object sd : query.list()) {
			Tr_sog_des legame = (Tr_sog_des)sd;
			Tb_soggetto sog = legame.getC();
			soggetti.add(ConversioneHibernateVO.toWeb().elementoStampaSoggettoDecodificato(sog));
		}
		return soggetti;
	}


	private List<ElementoStampaDescrittoreVO> estraiDescrittoriCollegati(Tb_descrittore descrittore) throws DaoManagerException {
		List<ElementoStampaDescrittoreVO> descrittori = new ArrayList<ElementoStampaDescrittoreVO>();
		Set desDes = descrittore.getTr_des_des();
		Query query = getCurrentSession().createFilter(desDes, "where fl_canc<>'S' order by Did_coll.ky_norm_descritt");

		for (Object sd : query.list()) {
			Tr_des_des legame = (Tr_des_des)sd;
			Tb_descrittore des = legame.getDid_coll();
			descrittori.add(ConversioneHibernateVO.toWeb().elementoStampaDescrittore(des, legame.getTp_legame()));
		}

		return descrittori;
	}



	private Criterion addFiltroSoggettiUtilizzati(String codPolo, String codBib) {
		StringBuilder sql = new StringBuilder();
		sql.append("exists (");
		sql.append(" select titsog.bid from tr_tit_sog_bib titsog");
		sql.append(" inner join tr_tit_bib titbib on titbib.bid=titsog.bid");
		sql.append(" where titsog.fl_canc<>'S'");
		sql.append(" and titbib.fl_canc<>'S'");
		sql.append(" and titbib.fl_possesso='S'");
		sql.append(" and titsog.cid={alias}.CID");
		sql.append(" and titbib.cd_polo='").append(codPolo).append("' and titbib.cd_biblioteca='").append(codBib).append("'");
		sql.append(")");

		return Restrictions.sqlRestriction(sql.toString());
	}

	private Criterion addFiltroClassiUtilizzate(String codPolo, String codBib) {
		StringBuilder sql = new StringBuilder();
		sql.append("exists (");
		sql.append(" select titcla.bid from tr_tit_cla titcla");
		sql.append(" inner join tr_tit_bib titbib on titbib.bid=titcla.bid");
		sql.append(" where titcla.fl_canc<>'S'");
		sql.append(" and titbib.fl_canc<>'S'");
		sql.append(" and titbib.fl_possesso='S'");
		sql.append(" and titcla.cd_sistema={alias}.cd_sistema");
		sql.append(" and titcla.cd_edizione={alias}.cd_edizione");
		sql.append(" and titcla.classe={alias}.classe");
		sql.append(" and titbib.cd_polo='").append(codPolo).append("' and titbib.cd_biblioteca='").append(codBib).append("'");
		sql.append(")");

		return Restrictions.sqlRestriction(sql.toString());
	}


	@SuppressWarnings("unchecked")
	public List<ElementoStampaTerminiThesauroVO> stampaTerminiThesauro(ParametriStampaTerminiThesauroVO richiesta)
			throws DaoManagerException {

		List<ElementoStampaTerminiThesauroVO> output = new ArrayList<ElementoStampaTerminiThesauroVO>();
		Session session = getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tb_termine_thesauro.class);
			addTimeStampRange(criteria, "ts_ins", richiesta.getTsIns_da(), richiesta.getTsIns_a());
			addTimeStampRange(criteria, "ts_var", richiesta.getTsVar_da(), richiesta.getTsVar_a())
					.add(Restrictions.eq("cd_the", richiesta.getCodThesauro()))
					.add(Restrictions.ne("fl_canc", 'S'));

			String firma = richiesta.getCodPolo() + richiesta.getCodBib();

			// stampo solo i termini creati o modificati dalla mia biblioteca
			if (richiesta.isSoloTerminiBiblioteca()) {
				LogicalExpression ute =
					Restrictions.or(Restrictions.like("ute_ins", firma, MatchMode.START),
									Restrictions.like("ute_var", firma, MatchMode.START));
				criteria.add(ute);
			}

			// stampo solo termini legati a titoli localizzati in biblioteca
			if (richiesta.isSoloLegamiTitoloInseritiDaBiblioteca()) {
				criteria.createCriteria("Trs_termini_titoli_biblioteche")
					.add(Restrictions.like("ute_ins", firma, MatchMode.START))
					.add(Restrictions.ne("fl_canc", 'S'));
			}

			List<Tb_termine_thesauro> termini = criteria.list();

			if (!richiesta.isStampaTitoli() && !richiesta.isStampaTerminiCollegati()) {
				for (Tb_termine_thesauro termine : termini) {
					ElementoStampaTerminiThesauroVO elemento =
						ConversioneHibernateVO.toWeb().elementoStampaTerminiThesauro(termine, null, null);
					output.add(elemento);
				}
				return output;
			}

			if (richiesta.isStampaTitoli() && !richiesta.isStampaTerminiCollegati()) {

				//Tbf_biblioteca_in_polo bib = creaIdBib(richiesta.getCodPolo(), richiesta.getCodBib());
				for (Tb_termine_thesauro termine : termini) {
					Set legami = termine.getTrs_termini_titoli_biblioteche();
					Query query = session.createFilter(legami, "where fl_canc<>'S'");

					ElementoStampaTerminiThesauroVO elemento =
						ConversioneHibernateVO.toWeb().elementoStampaTerminiThesauro(termine, query.list(), null);
					output.add(elemento);
				}
				return output;
			}

			if (!richiesta.isStampaTitoli() && richiesta.isStampaTerminiCollegati()) {

				//Tbf_biblioteca_in_polo bib = creaIdBib(richiesta.getCodPolo(), richiesta.getCodBib());
				for (Tb_termine_thesauro termine : termini) {
					Set legami = termine.getTr_termini_termini();
					Query query = session.createFilter(legami, "where fl_canc<>'S'");

					ElementoStampaTerminiThesauroVO elemento =
						ConversioneHibernateVO.toWeb().elementoStampaTerminiThesauro(termine, null, query.list());
					output.add(elemento);
				}
				return output;
			}

			for (Tb_termine_thesauro termine : termini) {
				Set legamiTit = termine.getTrs_termini_titoli_biblioteche();
				Query query1 = session.createFilter(legamiTit, "where fl_canc<>'S'");

				Set legamiThe = termine.getTr_termini_termini();
				Query query2 = session.createFilter(legamiThe, "where fl_canc<>'S'");

				ElementoStampaTerminiThesauroVO elemento =
					ConversioneHibernateVO.toWeb().elementoStampaTerminiThesauro(termine, query1.list(), query2.list());
				output.add(elemento);
			}

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

		return output;
	}

	public List<ElementoStampaClassificazioneVO> stampaSistemaClassificazione(
			ParametriStampaSistemaClassificazioneVO richiesta)
			throws DaoManagerException {

		Session session = getCurrentSession();
		List<ElementoStampaClassificazioneVO> output = new ArrayList<ElementoStampaClassificazioneVO>();
		try {
			Criteria c = session.createCriteria(Tb_classe.class);
			addTimeStampRange(c, "ts_ins", richiesta.getTsIns_da(),
					richiesta.getTsIns_a());
			addTimeStampRange(c, "ts_var", richiesta.getTsVar_da(),
					richiesta.getTsVar_a()).add(
					Restrictions.eq("cd_sistema", richiesta.getSistema())).add(
					Restrictions.eq("cd_edizione", richiesta.getEdizione()))
					.add(Restrictions.ne("fl_canc", 'S'));

			// stampo solo classi legate a titoli localizzati in biblioteca
			if (richiesta.isStampaSoloUtilizzati())
				c.add(addFiltroClassiUtilizzate(richiesta.getCodPolo(), richiesta.getCodBib()));

			Tbf_biblioteca_in_polo bib = creaIdBib(richiesta.getCodPolo(), richiesta.getCodBib());

			ScrollableResults cursor = c.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
			while (cursor.next() ) {
				Tb_classe classe = (Tb_classe) cursor.get(0);

				if (!richiesta.isSoloNotazione() && !richiesta.isStampaTitoli()) {
					ElementoStampaClassificazioneVO elemento =
						ConversioneHibernateVO.toWeb().elementoStampaClassificazione(classe, null);
					output.add(elemento);
					continue;
				}

				Query titoli = session.createQuery("titoliCollegatiClasseInBiblioteca");
				titoli.setEntity("classe", classe);
				titoli.setEntity("bib", bib);
				List list = titoli.list();
				ElementoStampaClassificazioneVO elemento =
					ConversioneHibernateVO.toWeb().elementoStampaClassificazione(classe, list);
				output.add(elemento);
			}

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
		return output;

	}

	public CountLegamiSemanticaVO countLegamiSemantica(String bid, String area) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Number countThesauro = 0;
			Number countAbstract = 0;

			Query queryCla = session.createQuery("select count(*) from Tr_tit_cla where B.id=:bid and fl_canc<>'S'");
			queryCla.setString("bid", bid);
			Number countClassi = (Number)queryCla.uniqueResult();

			Query querySog = session.createQuery("select count(*) from Tr_tit_sog_bib where B.id=:bid and fl_canc<>'S'");
			querySog.setString("bid", bid);
			Number countSoggetti = (Number)querySog.uniqueResult();

			if (!ValidazioneDati.equals(area, "D")){
				Query queryThe = session.createQuery("select count(*) from Trs_termini_titoli_biblioteche where B.id=:bid and fl_canc<>'S'");
				queryThe.setString("bid", bid);
				countThesauro = (Number)queryThe.uniqueResult();

				Query queryAbs = session.createQuery("select count(*) from Tb_abstract where bid=:bid and fl_canc<>'S'");
				queryAbs.setString("bid", bid);
				countAbstract = (Number)queryAbs.uniqueResult();
			}

			return new CountLegamiSemanticaVO(bid, countSoggetti.intValue(),
					countClassi.intValue(), countThesauro.intValue(), countAbstract.intValue());

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	public int countTerminiCollegati(String did) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Query query = session.createQuery("select count(*) from Tr_termini_termini where Did_base.id=:did and fl_canc<>'S'");
			query.setString("did", did);
			Number count = (Number) query.uniqueResult();

			return count.intValue();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	public int contaSoggettiCollegatiDid(String did) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Query query = session.getNamedQuery("contaSoggettiCollegatiDid");
			query.setString("did", did);
			Number count = (Number) query.uniqueResult();

			return count.intValue();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	public int contaSoggettiUtilizzatiCollegatiDid(String codPolo, String codBib, String did) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Query query = session.getNamedQuery("contaSoggettiUtilizzatiCollegatiDid");
			query.setString("polo", codPolo);
			query.setString("bib", codBib);
			query.setString("did", did);
			Number count = (Number) query.uniqueResult();

			return count.intValue();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	public ScrollableResults cursor_CancellaDescrittoriNonUtilizzati(
			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta)
			throws DaoManagerException {

		Session session = getCurrentSession();
		// cursore per iterazioni ulteriori (con filtro su ultimo cid trattato)
		Query query = session.getNamedQuery("descrittoriNonUtilizzati");

		query.setString("codSogg", richiesta.getCodSoggettario() );

		//almaviva5_20120604 evolutive CFI
		String edizione = richiesta.getEdizioneSoggettario();
		if (ValidazioneDati.isFilled(edizione)) {
			SbnEdizioneSoggettario cd_edizione = SbnEdizioneSoggettario.valueOf(edizione);
			query.setParameterList("edizione", SemanticaUtil.getEdizioniRicerca(cd_edizione));
		} else
			query.setString("edizione", "ALL");

		return query.setCacheMode(CacheMode.IGNORE)
				//.setLockMode("d", LockMode.UPGRADE)
				.scroll(ScrollMode.FORWARD_ONLY);

	}


	public ScrollableResults cursor_CancellaSoggettiNonUtilizzati(
			ParametriCancellazioneSoggettiNonUtilizzatiVO richiesta,
			String lastCid) throws DaoManagerException {

		Session session = getCurrentSession();

		StringBuilder hql = new StringBuilder(1024);
		Map<String, Object> params = new HashMap<String, Object>();

		hql.append("select sogg from Tb_soggetto sogg ")
			.append("where sogg.cd_soggettario=:codSogg ")
			.append("and sogg.fl_canc<>'S' ")
			.append("and not exists (from Tr_tit_sog_bib legame where legame.C.cid=sogg.cid and legame.fl_canc<>'S' ) ");

		params.put("codSogg", richiesta.getCodSoggettario());

		if (ValidazioneDati.isFilled(lastCid)) {
			hql.append("and sogg.cid>:cid ");
			params.put("cid", lastCid);
		}

		//almaviva5_20120604 evolutive CFI
		String edizione = richiesta.getEdizioneSoggettario();
		if (ValidazioneDati.isFilled(edizione)) {
			hql.append("and sogg.cd_edizione in (:edizione) ");
			SbnEdizioneSoggettario cd_edizione = SbnEdizioneSoggettario.valueOf(edizione);
			params.put("edizione", SemanticaUtil.getEdizioniRicerca(cd_edizione));
		}

		Query query = session.createQuery(hql.toString());
		query.setProperties(params);

		return query.setCacheMode(CacheMode.IGNORE).setLockMode("sogg", LockMode.UPGRADE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public void cancellaSoggettoNonUtilizzato(Tb_soggetto soggetto, String firmaUte, Timestamp ts) throws DaoManagerException {

		Session session = getCurrentSession();
		setSessionCurrentCfg();	// imposto la conf. per la sessione (TSearch)
		// cancello i legami ai descrittori
		Query descrittoriLegati = session.createFilter(soggetto.getTr_sog_des(), "where fl_canc<>'S'").setLockMode("this", LockMode.UPGRADE);
		for (Object o : descrittoriLegati.list() ) {
			Tr_sog_des legame = (Tr_sog_des) o;
			legame.setFl_canc('S');
			legame.setTs_var(ts);
			legame.setFl_posizione(0);
			legame.setUte_var(firmaUte);
			session.update(legame);
		}

		// cancello dati condivisione con indice (se presenti)
		eliminaCondivisioneSoggetto(soggetto, firmaUte, ts);

		// cancello logicamente il soggetto
		soggetto.setFl_canc('S');
		soggetto.setTs_var(ts);
		soggetto.setUte_var(firmaUte);
		session.update(soggetto);

		session.flush();

	}

	private void eliminaCondivisioneSoggetto(Tb_soggetto soggetto, String user, Timestamp ts) throws DaoManagerException {
		Session session = getCurrentSession();
		Query q = session.getNamedQuery("eliminaCondivisioneSoggetto");
		q.setEntity("sogg", soggetto);
		q.setString("ute", user);
		q.setTimestamp("var", ts);
		q.executeUpdate();
	}

	public List<DatiCondivisioneSoggettoVO> getDatiCondivisioneSoggetto(String cidPolo, String cidIndice, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		List<DatiCondivisioneSoggettoVO> output = new ArrayList<DatiCondivisioneSoggettoVO>();
		try {
			Criteria c = session.createCriteria(Tr_sogp_sogi.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			if (ValidazioneDati.isFilled(cidPolo))
				c.add(Restrictions.eq("cid_p.id", cidPolo));
			if (ValidazioneDati.isFilled(cidIndice))
				c.add(Restrictions.eq("cid_i", cidIndice));
			if (!ValidazioneDati.strIsEmpty(bid))
				c.add(Restrictions.eq("bid", bid));

			for (Object o : c.list() )
				output.add(ConversioneHibernateVO.toWeb().datiCondivisioneSoggetto((Tr_sogp_sogi) o));

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
		return output;

	}

	public void aggiornaDatiCondivisioneSoggetto(List<DatiCondivisioneSoggettoVO> datiCondivisione) throws DaoManagerException {

		Session session = getCurrentSession();
		try {
			setSessionCurrentCfg();
			for (DatiCondivisioneSoggettoVO elementoCondivisione : datiCondivisione) {

				Tb_soggetto cidPolo = (Tb_soggetto) loadNoLazy(session, Tb_soggetto.class, elementoCondivisione.getCidPolo() );
				Tr_sogp_sogi soggetto = ConversioneHibernateVO.toHibernate().datiCondivisioneSoggetto(cidPolo, elementoCondivisione, null);

				Tr_sogp_sogi entity = (Tr_sogp_sogi) session.get(Tr_sogp_sogi.class, soggetto);
				if (entity == null)
					session.save(soggetto);
				else {
					entity = ConversioneHibernateVO.toHibernate().datiCondivisioneSoggetto(cidPolo, elementoCondivisione, entity);
					session.update(entity);
				}
			}
			session.flush();

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

	}

	public void attivaCondivisioneSoggetto(String cid, String uteVar) throws DaoManagerException {

		Session session = getCurrentSession();
		try {
			setSessionCurrentCfg();
			Tb_soggetto cidPolo = (Tb_soggetto) loadNoLazy(session, Tb_soggetto.class, cid );

			List<DatiCondivisioneSoggettoVO> dcs = getDatiCondivisioneSoggetto(cid, null, null);
			boolean condiviso = ValidazioneDati.isFilled(dcs);
			Timestamp ts = now();

			if (condiviso) {
				cidPolo.setFl_condiviso('s');
				cidPolo.setTs_condiviso(ts);

			} else {
				cidPolo.setFl_condiviso('n');
				cidPolo.setTs_condiviso(DateUtil.MIN_TIMESTAMP);
			}

			cidPolo.setTs_var(ts);
			cidPolo.setUte_var(uteVar);

			session.update(cidPolo);

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}

	}

	public ScrollableResults getListaSoggettiCollegatiDid(String did) throws DaoManagerException {

		Session session = getCurrentSession();
		try {
			Query query = session.getNamedQuery("listaSoggettiCollegatiDid");
			query.setString("did", did);

			return query.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);

		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellaDescrittoreNonUtilizzato(Tb_descrittore descrittore,
			String firmaUte, Timestamp ts) throws DaoManagerException {

		Session session = getCurrentSession();
		setSessionCurrentCfg(); // imposto la conf. per la sessione (TSearch)

		// cancello i legami ai descrittori
		Set<Tr_des_des> dd = new HashSet<Tr_des_des>();
		dd.addAll(session.createFilter(descrittore.getTr_des_des(), "where fl_canc<>'S'").setLockMode("this", LockMode.UPGRADE).list());
		dd.addAll(session.createFilter(descrittore.getTr_des_des1(), "where fl_canc<>'S'").setLockMode("this", LockMode.UPGRADE).list());
		for (Tr_des_des legame : dd ) {
			legame.setFl_canc('S');
			legame.setTs_var(ts);
			legame.setUte_var(firmaUte);
			session.update(legame);
		}

		// cancello logicamente il descrittore
		descrittore.setFl_canc('S');
		descrittore.setTs_var(ts);
		descrittore.setUte_var(firmaUte);
		session.update(descrittore);
		session.flush();
	}


	public ScrollableResults cursor_ListaTotaleSoggetti(String cdSogg, String fromCid, String toCid) throws DaoManagerException {
		Session session = getCurrentSession();
		StringBuilder hql = new StringBuilder();
		hql.append("select s.cid from Tb_soggetto s ")
			.append("where s.fl_canc<>'S' ");
		if (ValidazioneDati.isFilled(cdSogg))
			hql.append("and s.cd_soggettario=:cdSogg ");
		if (ValidazioneDati.isFilled(fromCid))
			hql.append("and s.cid>=:fromCid ");
		if (ValidazioneDati.isFilled(toCid))
			hql.append("and s.cid<=:toCid ");
		hql.append("order by s.cid");

		Query q = session.createQuery(hql.toString());
		if (ValidazioneDati.isFilled(cdSogg))
			q.setString("cdSogg", cdSogg);
		if (ValidazioneDati.isFilled(fromCid))
			q.setString("fromCid", fromCid);
		if (ValidazioneDati.isFilled(toCid))
			q.setString("toCid", toCid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}


	public boolean isDescrittoreAutomatico(String did) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tr_sog_des.class);
		c.setProjection(Projections.count("fl_posizione"));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("D.id", did));
		c.add(Restrictions.gt("fl_posizione", 0));
		c.add(Restrictions.ne("fl_posizione", 99));
		c.add(Restrictions.ne("fl_primavoce", 'M'));

		Number cnt = (Number) c.setCacheMode(CacheMode.IGNORE).uniqueResult();
		return (cnt.intValue() > 0);
	}


	public Tb_soggetto getSoggettoById(String cid) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_soggetto.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("id", cid));

		Tb_soggetto s = (Tb_soggetto) c.setCacheMode(CacheMode.IGNORE).uniqueResult();
		return s;
	}


	public List<Tr_tit_sog_bib> getListaLegamiTitoloPosizioneZeroByCid(String cid) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tr_tit_sog_bib.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("C.id", cid));
		c.add(Restrictions.eq("posizione", 0));

		return c.list();
	}

	public Tb_descrittore getDescrittoreById(String did) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_descrittore.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("id", did));

		Tb_descrittore d = (Tb_descrittore) c.setCacheMode(CacheMode.IGNORE).uniqueResult();
		return d;
	}

	public int verificaSistemaEdizionePerBiblioteca(String codPolo, String codBib,
			String codSistema, String edizione) throws DaoManagerException {

		Session session = getCurrentSession();

		Criteria c = preparaCriteriaSistemaEdizionePerBiblioteca(session, codPolo, codBib, codSistema, edizione);

		Number result = (Number) c.setProjection(
				Projections.projectionList().add(Projections.rowCount()))
				.uniqueResult();

		return result.intValue();
	}

	public List<Tr_sistemi_classi_biblioteche> getListaSistemaEdizionePerBiblioteca(
			String codPolo, String codBib, String codSistema, String edizione)
			throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = preparaCriteriaSistemaEdizionePerBiblioteca(session,
				codPolo, codBib, codSistema, edizione);

		return c.list();
	}


	private Criteria preparaCriteriaSistemaEdizionePerBiblioteca(Session session, String codPolo, String codBib,
			String codSistema, String edizione) {
		Criteria c = session.createCriteria(Tr_sistemi_classi_biblioteche.class);
		c.add(Restrictions.ne("fl_canc", "S"));
		c.add(Restrictions.eq("flg_att", "1"));
		c.add(Restrictions.eq("cd_biblioteca", creaIdBib(codPolo, codBib)));
		c.add(Restrictions.eq("cd_sistema", codSistema));
		if (ValidazioneDati.isFilled(edizione) )
			c.add(Restrictions.eq("cd_edizione", edizione));
		return c;
	}

	public List<Tb_soggetto> getListaSoggettiLegatiPerBid(String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_soggetto.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		Criteria c2 = c.createCriteria("Tr_tit_sog_bib", "ts");
		c2.add(Restrictions.ne("ts.fl_canc", 'S'));
		c2.add(Restrictions.eq("ts.B.id", bid));

		c.addOrder(Order.asc("ts.posizione")).addOrder(Order.asc("ky_cles1_s"));

		return c.list();
	}


	public List<Tb_classe> getListaClassiLegatePerBid(Object bid) throws DaoManagerException {
		// ORDER BY this_.CD_SISTEMA, this_.CD_EDIZIONE, this_.CLASSE

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_classe.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		Criteria c2 = c.createCriteria("Tr_tit_cla", "tc");
		c2.add(Restrictions.ne("tc.fl_canc", 'S'));
		c2.add(Restrictions.eq("tc.B.id", bid));

		c.addOrder(Order.asc("cd_sistema")).addOrder(Order.asc("cd_edizione")).addOrder(Order.asc("classe"));

		return c.list();
	}

}
