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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Title: Interfaccia in diretta
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
 * Pannello analitico delle Marche
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Giuseppe Casafina
 * @version 1.0
 */
public class UtilityMarche extends UtilityDate {

	protected static Log log = LogFactory.getLog(UtilityMarche.class);

	public String IID_STRINGAVUOTA = "";

	/**
	 * Creates a new GUIUtilMarche object.
	 */
	public UtilityMarche() {
	}

	/**
	 * Controllo dei campi del JFrameInserisceMarca
	 *
	 * @param frame
	 *            DOCUMENT ME!
	 */
	public String isOkControlli(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String esito = "";
		// DESCRIZIONE: SE NON PRESENTE
		// PAROLE CHIAVE: SE ALMENO UNA
		// REPERTORI LEGATI: SE ALMENO UNO
		if (getCountCitazioniStandard(areaDatiPass) == 0) {
			esito = "ins031";
			return esito;
		}
		esito = situazioneCitazioniCongruente(areaDatiPass);
		if (!esito.equals("")) {
			return esito;
		} else if (areaDatiPass.getDettMarcaVO().getDesc().equals(
				IID_STRINGAVUOTA)) {
			esito = "ins033";
			return esito;
		} else if (getArrayParoleChiave(areaDatiPass) == null) {
			esito = "ins034";
			return esito;
		}

		if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1() != null) {
			if (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1().equals("")) {
				if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1().length() > 5) {
					esito = "ins042";
					return esito;
				}
			}
		}
		if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2() != null) {
			if (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2().equals("")) {
				if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2().length() > 5) {
					esito = "ins042";
					return esito;
				}
			}
		}
		if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3() != null) {
			if (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3().equals("")) {
				if (areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3().length() > 5) {
					esito = "ins042";
					return esito;
				}
			}
		}

		return esito;
	}// end isOkControlli

	/**
	 * Restituisce il numero delle Citazioni Standard inserite
	 *
	 * @return numeroCitazioniStandard
	 */
	public int getCountCitazioniStandard(AreaDatiVariazioneMarcaVO areaDatiPass) {
		int numeroCitazioniStandard = 0;
		if (getCitazioneStandard1(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		if (getCitazioneStandard2(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		if (getCitazioneStandard3(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		return numeroCitazioniStandard;
	}

	public int getCountCitazioniOld(AreaDatiVariazioneMarcaVO areaDatiPass) {
		int numeroCitazioniStandard = 0;
		if (getCitazioneStandard1Old(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		if (getCitazioneStandard2Old(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		if (getCitazioneStandard3Old(areaDatiPass) != null) {
			numeroCitazioniStandard = numeroCitazioniStandard + 1;
		}
		return numeroCitazioniStandard;
	}

	/**
	 * Se non è stata inserita alcuna Citazione Standard, restituisce un null.
	 *
	 * @return array di stringhe con i contenuti dei campi
	 */
	public String[] getCitazioneStandard1(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep1().equals(
				IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1()
						.equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep1();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep1();
			return arrayCitazioniStandard;
		}
	}

	public String[] getCitazioneStandard2(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep2().equals(
				IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2()
						.equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep2();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep2();
			return arrayCitazioniStandard;
		}
	}

	public String[] getCitazioneStandard3(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep3().equals(
				IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3()
						.equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep3();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep3();
			return arrayCitazioniStandard;
		}
	}


	/**
	 * Se non è stata inserita alcuna Citazione Standard, restituisce un null.
	 *
	 * @return array di stringhe con i contenuti dei campi
	 */
	public String[] getCitazioneStandard1Old(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep1Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1Old() == null) {
			return null;
		}
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep1Old().equals(
				IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1Old()
						.equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep1Old();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep1Old();
			return arrayCitazioniStandard;
		}
	}

	public String[] getCitazioneStandard2Old(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep2Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2Old() == null) {
			return null;
		}
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep2Old().equals(IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2Old().equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep2Old();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep2Old();
			return arrayCitazioniStandard;
		}
	}

	public String[] getCitazioneStandard3Old(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayCitazioniStandard[] = null;
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep3Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3Old() == null) {
			return null;
		}
		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep3Old().equals(
				IID_STRINGAVUOTA)
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3Old()
						.equals(IID_STRINGAVUOTA)) {
			return null;
		} else {
			arrayCitazioniStandard = new String[2];
			arrayCitazioniStandard[0] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep3Old();
			arrayCitazioniStandard[1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep3Old();
			return arrayCitazioniStandard;
		}
	}




	/**
	 * Se non è stata inserita alcuna parola chiave, ritorna un null.
	 *
	 * @return array di stringhe con i contenuti dei campi Parole Chiave
	 */
	public String[] getArrayParoleChiave(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String arrayParoleChiave[] = null;
		if ((areaDatiPass.getDettMarcaVO().getParChiave1()
				.equals(IID_STRINGAVUOTA))
				&& (areaDatiPass.getDettMarcaVO().getParChiave2()
						.equals(IID_STRINGAVUOTA))
				&& (areaDatiPass.getDettMarcaVO().getParChiave3()
						.equals(IID_STRINGAVUOTA))
				&& (areaDatiPass.getDettMarcaVO().getParChiave4()
						.equals(IID_STRINGAVUOTA))
				&& (areaDatiPass.getDettMarcaVO().getParChiave5()
						.equals(IID_STRINGAVUOTA))) {
			return null;
		} else {
			arrayParoleChiave = new String[5];
			arrayParoleChiave[0] = areaDatiPass.getDettMarcaVO()
					.getParChiave1();
			arrayParoleChiave[1] = areaDatiPass.getDettMarcaVO()
					.getParChiave2();
			arrayParoleChiave[2] = areaDatiPass.getDettMarcaVO()
					.getParChiave3();
			arrayParoleChiave[3] = areaDatiPass.getDettMarcaVO()
					.getParChiave4();
			arrayParoleChiave[4] = areaDatiPass.getDettMarcaVO()
					.getParChiave5();
			return arrayParoleChiave;
		}
	}

	/**
	 * JFrameInserisceMarca: Controlla se tutte le coppie di Citazioni Standard
	 * sono state inserite correttamente.
	 *
	 * @param frame
	 *            frame di inserimento della marca.
	 * @return true se le Citazioni sono state inserite correttamente,
	 *         altrimenti false.
	 */
	public String situazioneCitazioniCongruente(
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		String esito = "";
		// Citazione Standard: Codice Rep. + Progressivo Rep.
		// Al massimo tre Citazioni Standard per ogni marca da inserire.
		if (((areaDatiPass.getDettMarcaVO().getCampoCodiceRep1()
				.equals(IID_STRINGAVUOTA)) && (!areaDatiPass.getDettMarcaVO()
				.getCampoProgressivoRep1().equals(IID_STRINGAVUOTA)))
				|| ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep1()
						.equals(IID_STRINGAVUOTA)) && (areaDatiPass
						.getDettMarcaVO().getCampoProgressivoRep1()
						.equals(IID_STRINGAVUOTA)))) {
			esito = "ins032";
			return esito;
		} else if (((areaDatiPass.getDettMarcaVO().getCampoCodiceRep2()
				.equals(IID_STRINGAVUOTA)) && (!areaDatiPass.getDettMarcaVO()
				.getCampoProgressivoRep2().equals(IID_STRINGAVUOTA)))
				|| ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep2()
						.equals(IID_STRINGAVUOTA)) && (areaDatiPass
						.getDettMarcaVO().getCampoProgressivoRep2()
						.equals(IID_STRINGAVUOTA)))) {
			esito = "ins032";
			return esito;
		} else if (((areaDatiPass.getDettMarcaVO().getCampoCodiceRep3()
				.equals(IID_STRINGAVUOTA)) && (!areaDatiPass.getDettMarcaVO()
				.getCampoProgressivoRep3().equals(IID_STRINGAVUOTA)))
				|| ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep3()
						.equals(IID_STRINGAVUOTA)) && (areaDatiPass
						.getDettMarcaVO().getCampoProgressivoRep3()
						.equals(IID_STRINGAVUOTA)))) {
			esito = "ins032";
			return esito;
		}
		return esito;
	}// end situazioneCitazioniCongruente

	/**
	 * Restituisce true se è stata immessa una Citazione Standard con sigla di
	 * repertorio duplicata, altrimenti restituisce false.
	 *
	 * @param frameIns
	 *            frame di inserimento della marca.
	 * @return true se la Citazione è duplicata
	 */
	public boolean isCitazioneDuplicata(AreaDatiVariazioneMarcaVO areaDatiPass) {
		String sigla1 = areaDatiPass.getDettMarcaVO().getCampoCodiceRep1();
		String sigla2 = areaDatiPass.getDettMarcaVO().getCampoCodiceRep2();
		String sigla3 = areaDatiPass.getDettMarcaVO().getCampoCodiceRep3();

		// Se è stata inserita più di una Citazione con lo stesso
		// Codice Rep., manda un messaggio d'errore.
		if ((sigla1.equals(sigla2)) || (sigla1.equals(sigla3))
				|| (sigla2.equals(sigla3))) {

			// Controllo che non ci siano 2 sigle vuote, perchè
			// in quel caso significa che è stata inserita solo
			// una citazione e quindi non ci sono duplicati, ma
			// il controllo di sopra è stato comunque superato
			// perchè ha incontrato due campi vuoti e quindi uguali.
			if (!((sigla1.equals("") && (sigla2.equals(""))))
					&& !((sigla1.equals("") && (sigla3.equals(""))))
					&& !((sigla2.equals("") && (sigla3.equals(""))))) {

				// ESISTE CITAZIONE DUPLICATA
				return true;
			} else {
				// CONTROLLO SU CITAZIONI DUPLICATE OK
				return false;
			}

		} else {
			// CONTROLLO SU CITAZIONI DUPLICATE OK
			return false;
		}

	}// end isCitazioneDuplicata

}// end class GUIUtilMarche
