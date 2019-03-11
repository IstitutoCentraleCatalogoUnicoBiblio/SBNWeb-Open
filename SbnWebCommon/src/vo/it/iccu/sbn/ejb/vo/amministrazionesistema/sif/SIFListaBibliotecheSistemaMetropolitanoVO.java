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
package it.iccu.sbn.ejb.vo.amministrazionesistema.sif;

/*
 * @author	Simo
 * @date	26//11/2009
 */

public class SIFListaBibliotecheSistemaMetropolitanoVO extends SIFListaBibliotecheBaseVO {

	private static final long serialVersionUID = -3844125704098220504L;

	private final boolean selezionaTutti;

	public SIFListaBibliotecheSistemaMetropolitanoVO(String codPolo,
			String codBib, String testoNavigazione, boolean selezionaTutti,
			int elementiPerBlocco, String requestAttribute) {
		super();
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.multiSelezione = true;
		this.elementiPerBlocco = elementiPerBlocco;
		this.requestAttribute = requestAttribute;
		this.testoNavigazione = testoNavigazione;
		this.selezionaTutti = selezionaTutti;
	}

	public boolean isSelezionaTutti() {
		return selezionaTutti;
	}

}
