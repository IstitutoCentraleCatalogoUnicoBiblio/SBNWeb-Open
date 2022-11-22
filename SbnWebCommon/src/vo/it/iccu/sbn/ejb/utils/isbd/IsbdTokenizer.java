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
package it.iccu.sbn.ejb.utils.isbd;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import org.apache.log4j.Logger;

public final class IsbdTokenizer {

	private static Logger log = Logger.getLogger(IsbdTokenizer.class);

	private enum TipoEtichettaIsbd {
		T200,
		T205,
		T210,
		T215;

		static final String PREFIX = "T";
	}

	private static final String SEPARATORE_ETICHETTE = "\\;";
	private static final String SEPARATORE_ETICHETTA = "\\-";

	private static final String[] SEPARATORI_ISBD = new String[] {". -", "((", "))" };

	public static final IsbdVO tokenize(String isbd, String indice_isbd) throws Exception {

		IsbdVO out = new IsbdVO();

		if (ValidazioneDati.strIsNull(indice_isbd))  //non ho l'indice. (errore?)
			return new IsbdVO(isbd);

		// 200-0001;210-0268;215-0343;300-0389;
		String[] etichette = indice_isbd.split(SEPARATORE_ETICHETTE);
		if (ValidazioneDati.size(etichette) < 2) {	// solo area 200?
			log.warn("indice_isbd con una sola etichetta: " + indice_isbd);
			return new IsbdVO(isbd);
		}

		int start = isbd.length();
		int lastAreaStart = start;

		for (int idx = (etichette.length - 1); idx >= 0; idx--) {
			String[] aree = etichette[idx].split(SEPARATORE_ETICHETTA);
			if (ValidazioneDati.size(aree) != 2) // ERRORE
				return new IsbdVO(isbd);

			try {
				if (!ValidazioneDati.strIsNumeric(aree[1])) {//errore offset area
					log.warn("indice_isbd etichetta non valida: " + etichette[idx]);
					return new IsbdVO(isbd);
				}

				start = Integer.valueOf(aree[1]) - 1;
				TipoEtichettaIsbd tipoEtichetta = TipoEtichettaIsbd.valueOf(TipoEtichettaIsbd.PREFIX + aree[0]);
				switch (tipoEtichetta) {
				case T200:
					out.setT200_areaTitolo(substring(isbd, start, lastAreaStart) );
					break;
				case T205:
					out.setT205_areaEdizione(substring(isbd, start, lastAreaStart) );
					break;
				case T210:
					out.setT210_areaPubblicazione(substring(isbd, start, lastAreaStart) );
					break;
				case T215:
					out.setT215_areaDescrizioneFisica(substring(isbd, start, lastAreaStart) );
					break;
				}

				lastAreaStart = start;

			} catch (IllegalArgumentException e) {
				lastAreaStart = start;
				continue;
			} catch (StringIndexOutOfBoundsException siob) {
				log.error(String.format("errore split aree: indice_isbd: '%s', isbd: '%s'", indice_isbd, isbd));
				return new IsbdVO(isbd);
			}

		}

		return out;
	}

	private static final String substring(String isbd, int start, int end) {
		String sub = "";
		if (end > isbd.length() )
			sub = isbd.substring(start);
		else
			sub = isbd.substring(start, end);

		return eliminaSeparatori(sub);
	}

	private static final String eliminaSeparatori(String value) {
		if (ValidazioneDati.strIsNull(value))
			return "";

		for (String sep : SEPARATORI_ISBD)
			value = value.replace(sep, "");

		return value;
	}

	public static void main(String... args) throws Exception {

		String indice = "200-0001;205-0080;210-0089;215-0115;";
		String isbd = "Il *visconte dimezzato / Italo Calvino ; illustrazioni di Francesco Luzzati. - 3. ed. - Torino : Einaudi, 1981. - 85 p. : ill. ; 24 cm";

		System.out.println(tokenize(isbd, indice));
	}

}
