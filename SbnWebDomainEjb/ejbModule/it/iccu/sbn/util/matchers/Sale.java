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
package it.iccu.sbn.util.matchers;

import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;

public class Sale extends BaseMatchers {

	public static Function<PostoSalaVO, Short> byGruppoPosti() {
		return new Function<PostoSalaVO, Short>() {
			public Short apply(PostoSalaVO posto) {
				return posto.getGruppo();
			}
		};
	}

	public static Predicate<GruppoPostiVO> gruppoOverlaps(final GruppoPostiVO gp1) {
		return new Predicate<GruppoPostiVO>() {
			public boolean test(GruppoPostiVO gp2) {
				if (gp1 == gp2)
					return false;
				//x1 <= y2 && y1 <= x2
				return (gp1.getPosto_da() <= gp2.getPosto_a()) && (gp2.getPosto_da() <= gp1.getPosto_a());
			}
		};
	}

	public static Function<PostoSalaVO, Stream<String>> postoCategorieMediazione() {
		return new Function<PostoSalaVO, Stream<String>>() {
			public Stream<String> apply(PostoSalaVO ps) {
				return Stream.of(ps.getCategorieMediazione());
			}
		};
	}

	public static Function<PostoSalaVO, SalaVO> bySala() {
		return new Function<PostoSalaVO, SalaVO>() {
			public SalaVO apply(PostoSalaVO ps) {
				return ps.getSala();
			}
		};
	}

	public static Predicate<SalaVO> isSalaPrenotabile(final boolean utenteRemoto) {
		return new Predicate<SalaVO>() {
			public boolean test(SalaVO sala) {
				//check sala prenotabile da remoto
				if (!sala.isPrenotabileDaRemoto() && utenteRemoto)
					return false;

				short durataFascia = sala.getDurataFascia();
				if (durataFascia > 0) {
					short maxFascePrenotazione = sala.getMaxFascePrenotazione();
					if (maxFascePrenotazione < 1)
						return true;

					int durataMaxPren = durataFascia * maxFascePrenotazione;
					return (durataMaxPren > 0);
				}

				return false;
			}};
	}

}
