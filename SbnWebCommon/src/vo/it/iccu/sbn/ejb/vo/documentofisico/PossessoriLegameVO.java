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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;

import java.util.StringTokenizer;

public class PossessoriLegameVO extends PossessoriDettaglioVO {
	String notaAlLegame;
	String tipoLegame;
	String pidPadre;
	String pidFiglio;

//	static final long serialVersionUID = InterrogazioneAutoreGeneraleVO.class.hashCode();

	/*
	 *  aggiungere i due parametri per il legame - nota e tipo legame
	 * */

	/**
	 *
	 */
	private static final long serialVersionUID = -2050782632414502827L;





	public void validaCanaliPrim() throws ValidationException {
		int combinazioni = 0;

		if (this.getNome().length() > 0 ) combinazioni = combinazioni +1;

		if (combinazioni == 0)
			throw new ValidationException("noCanPrim", ValidationExceptionCodici.noCanPrim);
		if (combinazioni > 1)
			throw new ValidationException("soloUnCanPrim", ValidationExceptionCodici.soloUnCanPrim);
	}

	public void validaParametriGener() throws ValidationException {

		if (!this.getNome().equals("") && this.getTipoRicerca().equals("parole")) {
			StringTokenizer st = new StringTokenizer(this.getNome());

			if (st.countTokens() > 4) {
				// inserire diagnostico di troppe parole (al massimo 4)
				throw new ValidationException("ric021", ValidationExceptionCodici.livRicObblig);
			} else {
				// devono essere massimo 4 parole
				parole = new String[4];
				int      i = 0;
				while (st.hasMoreTokens()) {
					parole[i] = st.nextToken();
					i++;
				}
			}
		}
		// TIPO NOME

		int i = -1;
		int countTipoNome = 0;
		tipoNomeBoolean = new boolean[7];

		if (countTipoNome > 4 ) {
			// inserire diagnostico di troppi tipi nome (al massimo 4)
			throw new ValidationException("ric022", ValidationExceptionCodici.livRicObblig);
		}
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getPidPadre() {
		return pidPadre;
	}

	public void setPidPadre(String pidPadre) {
		this.pidPadre = pidPadre;
	}

	public String getPidFiglio() {
		return pidFiglio;
	}

	public void setPidFiglio(String pidFiglio) {
		this.pidFiglio = pidFiglio;
	}

}
