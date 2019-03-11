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
package it.iccu.sbn.ejb.utils.collocazioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;

import org.apache.log4j.Logger;

public final class NormalizzaRangeCollocazioni {

	private static Logger log = Logger.getLogger(NormalizzaRangeCollocazioni.class);

	private static final int COLL_MAX_LEN = 80;
	private static final int SPEC_MAX_LEN = 40;

	public static CodiciNormalizzatiVO normalizzaCollSpec(String tipoColloc, String dallaColl, String allaColl,
			boolean esattoColl, String dallaSpec, String allaSpec, boolean esattoSpec, String ord)
			throws DataException, ValidationException {

		String locNormalizzata = null;
		String specNormalizzata = null;
		String aLocNormalizzata = null;
		String aSpecNormalizzata = null;
		CodiciNormalizzatiVO codiciNormalizzatiVO = null;

		try {
			codiciNormalizzatiVO = new CodiciNormalizzatiVO();

			// dallaColl
			if (ValidazioneDati.isFilled(dallaColl)) {
				if (tipoColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
					locNormalizzata = OrdinamentoCollocazione2.normalizza(dallaColl, true);
				} else {
					locNormalizzata = OrdinamentoCollocazione2.normalizza(dallaColl);
				}
				locNormalizzata = ValidazioneDati.fillRight(locNormalizzata, ' ', COLL_MAX_LEN);

			} else
				locNormalizzata = ValidazioneDati.fillRight("", ' ', COLL_MAX_LEN);

			codiciNormalizzatiVO.setDaColl(locNormalizzata);

			// allColl
			if (!esattoColl) {
				if (ValidazioneDati.isFilled(dallaColl)) {
					if (ValidazioneDati.isFilled(allaColl)) {
						if (tipoColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
							aLocNormalizzata = OrdinamentoCollocazione2.normalizza(allaColl, true);
						} else {
							aLocNormalizzata = OrdinamentoCollocazione2.normalizza(allaColl);
						}
						// se la ricerca è per coll. non esatta e l'ultimo gruppo è numerico
						// si estende la ricerca alle collocazioni successive (fino a 999999)
						String daColl = codiciNormalizzatiVO.getDaColl().trim();
						if (daColl.length() >= 6) {
							// controllo se ultimo gruppo numerico
							String ultimo = daColl.substring(daColl.length() - 6);
							if (ValidazioneDati.strIsNumeric(ultimo)) {
								aLocNormalizzata = daColl.substring(0, daColl.length() - 6) + "999999";
								aLocNormalizzata = ValidazioneDati.fillRight(aLocNormalizzata, 'Z', COLL_MAX_LEN);
							} else {
								aLocNormalizzata = ValidazioneDati.fillRight(aLocNormalizzata, 'Z', COLL_MAX_LEN);
							}
						} else {
							aLocNormalizzata = ValidazioneDati.fillRight(aLocNormalizzata, 'Z', COLL_MAX_LEN);
						}
					} else {
						aLocNormalizzata = ValidazioneDati.fillRight("", 'Z', COLL_MAX_LEN);
					}
				} else {
					aLocNormalizzata = ValidazioneDati.fillRight("", 'Z', COLL_MAX_LEN);
				}
			} else {
				codiciNormalizzatiVO.setEsattoColl(esattoColl);
				aLocNormalizzata = codiciNormalizzatiVO.getDaColl();
			}

			codiciNormalizzatiVO.setAColl(aLocNormalizzata);

			// dallaSpec
			if (ValidazioneDati.isFilled(dallaSpec)) {
				if (tipoColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
					specNormalizzata = OrdinamentoCollocazione2.normalizza(dallaSpec, true);
				} else {
					specNormalizzata = OrdinamentoCollocazione2.normalizza(dallaSpec);
				}
				specNormalizzata = ValidazioneDati.fillRight(specNormalizzata, ' ', SPEC_MAX_LEN);

			} else
				specNormalizzata = ValidazioneDati.fillRight("", ' ', SPEC_MAX_LEN);

			codiciNormalizzatiVO.setDaSpec(specNormalizzata);

			// allaspec
			if (!esattoSpec) {
				if (ValidazioneDati.isFilled(dallaSpec)) {
					if (ValidazioneDati.isFilled(allaSpec)) {
						if (tipoColloc.equals(DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE)) {
							aSpecNormalizzata = OrdinamentoCollocazione2.normalizza(allaSpec, true);
						} else {
							aSpecNormalizzata = OrdinamentoCollocazione2.normalizza(allaSpec);
						}
						// se la ricerca è per spec. non esatta e l'ultimo gruppo è numerico
						// si estende la ricerca alle specificazioni successive (fino a 999999)
						String daSpec = codiciNormalizzatiVO.getDaSpec().trim();
						if (daSpec.length() >= 6) {
							// controllo se ultimo gruppo numerico
							String ultimo = daSpec.substring(daSpec.length() - 6);
							if (ValidazioneDati.strIsNumeric(ultimo)) {
								aSpecNormalizzata = daSpec.substring(0, daSpec.length() - 6) + "999999";
								aSpecNormalizzata = ValidazioneDati.fillRight(aSpecNormalizzata, 'Z', SPEC_MAX_LEN);
							} else {
								aSpecNormalizzata = ValidazioneDati.fillRight(aSpecNormalizzata, 'Z', SPEC_MAX_LEN);
							}
						} else {
							aSpecNormalizzata = ValidazioneDati.fillRight(aSpecNormalizzata, 'Z', SPEC_MAX_LEN);
						}
					} else {
						aSpecNormalizzata = ValidazioneDati.fillRight("", 'Z', SPEC_MAX_LEN);
					}
				} else {
					aSpecNormalizzata = ValidazioneDati.fillRight("", 'Z', SPEC_MAX_LEN);
				}
			} else {
				codiciNormalizzatiVO.setEsattoSpec(esattoSpec);
				aSpecNormalizzata = codiciNormalizzatiVO.getDaSpec();
			}

			codiciNormalizzatiVO.setASpec(aSpecNormalizzata);

			if (codiciNormalizzatiVO != null) {
				if ((codiciNormalizzatiVO.getDaColl() != null || codiciNormalizzatiVO.getAColl() != null)
						&& (codiciNormalizzatiVO.getDaSpec() != null || codiciNormalizzatiVO.getASpec() != null)) {
					return codiciNormalizzatiVO;
				} else {
					codiciNormalizzatiVO = null;
				}
			}

			return codiciNormalizzatiVO;

			// } catch (ValidationException e) {
			// throw e;
		} catch (Exception e) {
			log.error("", e);
			throw new DataException(e);
		}
	}

	private static String normalizeAndFill(String tipoColloc, String value, char filler, int maxLen) {
		String norm = OrdinamentoCollocazione2.normalizza(value,
				ValidazioneDati.equals(tipoColloc, DocumentoFisicoCostant.COD_SISTEMA_DI_CLASSIFICAZIONE));
		return ValidazioneDati.fillRight(norm, filler, maxLen);
	}

	public static CodiciNormalizzatiVO normalizzaCollSpecRange(String tipoColloc, String dallaColl, String allaColl,
			String dallaSpec, String allaSpec, String ord) throws DataException, ValidationException {

		String locNormalizzata = null;
		String specNormalizzata = null;
		String aLocNormalizzata = null;
		String aSpecNormalizzata = null;

		boolean isCollDaValorizzato = ValidazioneDati.isFilled(dallaColl);
		boolean isSpecDaValorizzato = ValidazioneDati.isFilled(dallaSpec);

		boolean isCollAValorizzato = ValidazioneDati.isFilled(allaColl);
		boolean isSpecAValorizzato = ValidazioneDati.isFilled(allaSpec);

		try {
			CodiciNormalizzatiVO range = new CodiciNormalizzatiVO();

			// collocazione da
			locNormalizzata = isCollDaValorizzato ? normalizeAndFill(tipoColloc, dallaColl, ' ', COLL_MAX_LEN)
					: ValidazioneDati.fillRight("", ' ', COLL_MAX_LEN);

			range.setDaColl(locNormalizzata);

			// specificazione da
			specNormalizzata = isSpecDaValorizzato ? normalizeAndFill(tipoColloc, dallaSpec, ' ', SPEC_MAX_LEN)
					: ValidazioneDati.fillRight("", ' ', SPEC_MAX_LEN);

			range.setDaSpec(specNormalizzata);

			///////////////////////////////////////////////////////////////////////////////////////////

			// collocazione a
			aLocNormalizzata = isCollAValorizzato ? normalizeAndFill(tipoColloc, allaColl, ' ', COLL_MAX_LEN)
					: ValidazioneDati.fillRight("", isSpecAValorizzato ? ' ' : 'Z', COLL_MAX_LEN);

			range.setAColl(aLocNormalizzata);

			// specificazione a
			aSpecNormalizzata = isSpecAValorizzato ? normalizeAndFill(tipoColloc, allaSpec, ' ', SPEC_MAX_LEN)
					: ValidazioneDati.fillRight("", 'Z', SPEC_MAX_LEN);

			range.setASpec(aSpecNormalizzata);

			log.debug("Range collocazione normalizzato: " + range);

			return range;

		} catch (Exception e) {
			log.error("", e);
			throw new DataException(e);
		}

	}

	public static void main(String[] args) throws DataException, ValidationException {
		// System.out.println(normalizzaCollSpec("M", "333", null, false, null,
		// null, false, ""));
		System.out.println(normalizzaCollSpec("M", "333bis", "333bis", false, null, null, false, ""));
	}

}
