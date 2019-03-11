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
package it.iccu.sbn.persistence.dao.periodici;

import static org.hamcrest.Matchers.not;

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.esame.CatenaRinnoviOrdineVO;
import it.iccu.sbn.persistence.dao.acquisizioni.Tba_ordiniDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.vo.custom.periodici.UnionEsamePeriodico;
import it.iccu.sbn.web.constant.PeriodiciConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectFirst;

import ch.lambdaj.group.Group;

public class EsamePeriodicoDAO {

	private static Logger log = Logger.getLogger(EsamePeriodicoDAO.class);

	private final PeriodiciDAO dao;
	private final Tba_ordiniDao daoOrd;
	private final Tbc_collocazioneDao daoColl;

	//cache per duplicati
	private final Set<Object> entity_cache;


	public class IsEmptyOrNull<T> extends BaseMatcher<T> {

		public boolean matches(Object item) {
			if (item == null)
				return true;

			return (item instanceof String) && "".equals(((String) item).trim());
		}

		public void describeTo(Description description) {
			description.appendText("isEmptyOrNull");
		}
	}

	private final <T> Matcher<T> isEmptyOrNull() {
		return new IsEmptyOrNull<T>();
	}

	public EsamePeriodicoDAO() {
		dao = new PeriodiciDAO();
		daoOrd = new Tba_ordiniDao();
		daoColl = new Tbc_collocazioneDao();
		entity_cache = new THashSet<Object>();
	}

	private Object cache_get(Object obj) {
		if (obj == null || entity_cache.contains(obj))
			return null;
		entity_cache.add(obj);
		return obj;
	}

	public List<CatenaRinnoviOrdineVO> getCatenaRinnoviOrdine(String codPolo, String codBib, String bid) throws DaoManagerException {
		List<CatenaRinnoviOrdineVO> output = new ArrayList<CatenaRinnoviOrdineVO>();
		Map<String, List<Integer>> catene = new LinkedHashMap<String, List<Integer>>();
		Random rnd = new Random();

		List<Object[]> listaOrdini = dao.getCatenaRinnoviOrdine(codPolo, codBib, bid);
		for (Object[] row : listaOrdini) {

			String kord = (String) row[0];
			String catena = ValidazioneDati.coalesce((Number)row[1], new Integer(0)).toString() + '-'
				+ ValidazioneDati.coalesce((Number)row[2], new Integer(0)).toString() ;
			//almaviva5_20110209 é necessario differenziare gli ordini con catena 0-0, perché non
			//appartenenti a una catena di rinnovi reale.
			if (ValidazioneDati.equals(catena, PeriodiciConstants.CATENA_RINNOVO_VUOTA))
				catena = String.valueOf(rnd.nextLong());

			int idOrdine = Integer.valueOf(kord.split("\\-")[1].trim());

			List<Integer> rinnovi = catene.get(catena);
			if (rinnovi == null) { //nuova catena
				rinnovi = new ArrayList<Integer>();
				catene.put(catena, rinnovi);
			}
			rinnovi.add(idOrdine);
		}


		for (Entry<String, List<Integer>> e : catene.entrySet())
			output.add(ConversioneHibernateVO.toWeb().catenaRinnoviOrdine(codPolo, codBib, e.getValue()));

		return output;
	}

	public void cercaEsamePeriodicoPerBiblioteca(RicercaPeriodicoVO<?> richiesta,
			List<ElementoSeriePeriodicoVO> output)	throws DaoManagerException, Exception {

		String codPolo = richiesta.getCodPolo();
		String codBib = richiesta.getCodBib();
		String bid = richiesta.getBid();

		//carico le catene dei rinnovi per gli ordini su questo bid
		List<CatenaRinnoviOrdineVO> catene = getCatenaRinnoviOrdine(codPolo, codBib, bid);

		List<Object[]> rows = dao.getEsamePeriodicoPerBiblioteca(codPolo, codBib, bid);
		List<UnionEsamePeriodico> union = new ArrayList<UnionEsamePeriodico>();
		for (Object[] row : rows)
			union.add(ConversioneHibernateVO.toWeb().unionEsamePeriodico(row));

		/*
		1.cerco gruppo righe con stesso esemplare (ogni es. deve apparire 1 volta)
		2.per ogni gruppo trovato prendo la collocazione più recente (max(ts_inscoll))
		3.cerco nel gruppo la prima riga con ordine e controllo che sia l'ultimo rinnovo.
			se non lo é devo usare la chiave dell'ultimo rinnovo
		 */

		//1.cerco gruppo righe con stesso esemplare (ogni es. deve apparire 1 volta)
		UnionEsamePeriodico p = on(UnionEsamePeriodico.class);
		Group<UnionEsamePeriodico> gruppiEsempl = group(union, by(p.getEsemplare()));
		log.debug("gruppi esemplare: " + gruppiEsempl.subgroups().size());
		for (Group<UnionEsamePeriodico> ge : gruppiEsempl.subgroups()) {

			Tbc_esemplare_titolo e = null;
			Tbc_collocazione c = null;
			Tba_ordini o = null;

			UnionEsamePeriodico fe = ge.first();
			if (ValidazioneDati.isFilled(fe.getEsemplare())) { //gruppo con esemplare
				//la prima riga é quella con collocazione più recente
				UnionEsamePeriodico primoOrd = selectFirst(ge.findAll(), having(p.getPrimoOrdine(), not(isEmptyOrNull())) );
				o = primoOrd != null ? (Tba_ordini) cache_get(getUltimoRinnovo(catene, primoOrd.getId_ordine()) ) : null;
				c = (Tbc_collocazione) ((fe.getKey_loc() > 0) ? cache_get(daoColl.getCollocazione(fe.getKey_loc())) : null);
				e = (Tbc_esemplare_titolo) ((c != null) ? cache_get(c.getCd_biblioteca_doc()) : null);
				ElementoSeriePeriodicoVO esp = ConversioneHibernateVO.toWeb().elementoSeriePeriodico(e, c, fe.getTs_ins_prima_coll(), o, catene);
				if (esp != null)
					output.add(esp);
				//elimino ulteriori ordini (altre catene)
				for (UnionEsamePeriodico uep : ge.findAll())
					if (uep.getId_ordine() > 0)
						cache_get(getUltimoRinnovo(catene, uep.getId_ordine()) );

			} else {
				//gruppo con collocazione
				Group<UnionEsamePeriodico> gruppiColl = group(ge.findAll(), by(p.getKey_loc()));
				log.debug("gruppi collocazione: " + gruppiColl.subgroups().size());
				for (Group<UnionEsamePeriodico> gc : gruppiColl.subgroups()) {
					//la prima riga é quella con collocazione più recente
					UnionEsamePeriodico fc = gc.first();
					if (fc.getKey_loc() > 0) {
						UnionEsamePeriodico primoOrd = selectFirst(gc.findAll(), having(p.getPrimoOrdine(), not(isEmptyOrNull())) );
						o = primoOrd != null ? (Tba_ordini) cache_get(getUltimoRinnovo(catene, primoOrd.getId_ordine()) ) : null;
						c = (Tbc_collocazione) ((fc.getKey_loc() > 0) ? cache_get(daoColl.getCollocazione(fc.getKey_loc())) : null);
						ElementoSeriePeriodicoVO esp = ConversioneHibernateVO.toWeb().elementoSeriePeriodico(null, c, fc.getTs_ins_prima_coll(), o, catene);
						if (esp != null)
							output.add(esp);
						//elimino ulteriori ordini (altre catene)
						for (UnionEsamePeriodico uep : gc.findAll())
							if (uep.getId_ordine() > 0)
								cache_get(getUltimoRinnovo(catene, uep.getId_ordine()) );

					} else {
						//inventari precisati con ordini
						Group<UnionEsamePeriodico> gruppiOrd = group(gc.findAll(), by(p.getId_ordine()));
						log.debug("gruppi ordine: " + gruppiOrd.subgroups().size());
						for (Group<UnionEsamePeriodico> go : gruppiOrd.subgroups()) {
							UnionEsamePeriodico fo = go.first();
							o = fo.getId_ordine() > 0 ? (Tba_ordini) cache_get(getUltimoRinnovo(catene, fo.getId_ordine()) ) : null;
							ElementoSeriePeriodicoVO esp = ConversioneHibernateVO.toWeb().elementoSeriePeriodico(null, null, null, o, catene);
							if (esp != null)
								output.add(esp);
						}
					}
				}	//ciclo gruppo coll.
			}
		}	//ciclo gruppo esemplari
	}

	private Tba_ordini getUltimoRinnovo(List<CatenaRinnoviOrdineVO> catene,
			int id_ordine) throws DaoManagerException {

		//carico dal DB l'ordine più recente della catena
		for (CatenaRinnoviOrdineVO c : catene) {
			if (c.isUltimoRinnovo(id_ordine) || c.getOrdiniPrecedenti().contains(id_ordine))
				return daoOrd.getOrdineById(c.getId_ordine());
		}

		return null;
	}

}
