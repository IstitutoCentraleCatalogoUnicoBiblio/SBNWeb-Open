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
package it.iccu.sbn.util.servizi;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.calendario.SlotVO;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.matchers.Sale;
import it.iccu.sbn.vo.custom.servizi.sale.StatoPrenotazionePosto2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.joda.time.LocalTime;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.last;

public class SaleUtil {

	public static List<GruppoPostiVO> generaGruppi(List<PostoSalaVO> input) {

		Map<Short, List<PostoSalaVO>> gruppi = Stream.of(input).collect(Collectors.groupingBy(Sale.byGruppoPosti()));
		List<GruppoPostiVO> output = new ArrayList<GruppoPostiVO>();
		for (Short gruppo : gruppi.keySet()) {
			List<PostoSalaVO> posti = gruppi.get(gruppo);
			//lista filtrata delle mediazioni previste
			List<String> categorie = Stream.of(posti).flatMap(Sale.postoCategorieMediazione()).distinct().toList();
			Collections.sort(categorie);
			GruppoPostiVO gp = new GruppoPostiVO(gruppo, first(posti).getNum_posto(), last(posti).getNum_posto(), categorie);
			output.add(gp);
		}
		Collections.sort(output);

		return output;
	}

	public static void assegnaGruppo(List<GruppoPostiVO> gruppi, PostoSalaVO posto) {
		for (GruppoPostiVO gruppo : gruppi) {
			if (gruppo.contains(posto)) {
				posto.setGruppo((short) gruppo.getGruppo());
				posto.setCategorieMediazione(gruppo.getCategorieMediazione());
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static SlotVO mergeSlots(List<SlotVO> slots) {
		SlotVO merged = new SlotVO(0);
		LocalTime inizio = slots.get(0).getInizio();
		LocalTime fine = slots.get(0).getFine();
		List<PostoSalaVO> posti = slots.get(0).getPosti();
		for (SlotVO s : slots) {
			if (s.getInizio().isBefore(inizio) ) inizio = s.getInizio();
			if (s.getFine().isAfter(fine) ) fine = s.getFine();

			posti = ListUtils.intersection(posti, s.getPosti());
		}

		merged.setInizio(inizio);
		merged.setFine(fine);
		merged.setPosti(posti);

		return merged;
	}

	public static StatoPrenotazionePosto2 getStatoDinamico(final StatoPrenotazionePosto stato, final Timestamp ts_fine, final Timestamp now) {
		switch (stato) {
		case IMMESSA:
			if (ts_fine.after(now) ) return StatoPrenotazionePosto2.IMMESSA;
			if (ts_fine.before(now) ) return StatoPrenotazionePosto2.NON_FRUITA;
			break;

		case FRUITA:
			if (ts_fine.after(now) ) return StatoPrenotazionePosto2.IN_CORSO;
			if (ts_fine.before(now) ) return StatoPrenotazionePosto2.CONCLUSA;
			break;

		case ANNULLATA:
			return StatoPrenotazionePosto2.ANNULLATA;

		case DISDETTA:
			return StatoPrenotazionePosto2.DISDETTA;

		case CONCLUSA:
			return StatoPrenotazionePosto2.CONCLUSA;

		default:
			break;
		}

		return null;
	}

	public static List<String> tipoMatInv2CatMediazione(String codMatInv) throws Exception {
		List<String> listaCatMed = new ArrayList<String>();
		//si cercano tutte le associazioni cross su questo mat. inventariale (nel flag2)
		List<TB_CODICI> codiciWithFlags = CodiciProvider.getCodiciWithFlags(CodiciType.CODICE_CAT_STRUMENTO_MEDIAZIONE_TIPO_MAT_INV, true,
				new Integer[] { 2 }, new String[] { codMatInv });
		for (TB_CODICI c : codiciWithFlags)
			listaCatMed.add(c.getCd_flg1()); //cat mediazione

		return listaCatMed;
	}

}
