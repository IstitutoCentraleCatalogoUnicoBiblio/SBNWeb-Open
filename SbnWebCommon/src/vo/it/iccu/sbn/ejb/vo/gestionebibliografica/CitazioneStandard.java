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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

/**
 * <p>Title: Interfaccia in diretta</p>
 *
 * <p>Description: Interfaccia web per il sistema bibliotecario nazionale</p>
 *
 * <p>Citazione standard legata alla marca.</p>
 *
 * <p>Copyright: Copyright (c) 2002</p>
 *
 * <p>Company: Finsiel</p>
 *
 * @author Giuseppe Casafina
 *
 * @version 1.0
 */
public class CitazioneStandard extends SerializableVO {

	// = CitazioneStandard.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -1430994393512784132L;

	/**
	 * Sigla Repertorio della citazione standard.
	 */
	private String sigla;

	/**
	 * Numero della citazione standard.
	 */
	private int citazione;

	/**
	 * Stato della citazione standard.
	 * INSERIMENTO: Nuova citazione standard da inserire.
	 * CANCELLAZIONE: Citazione standard da cancellare.
	 * INVARIATO: Citazione standard invariata, da non gestire.
	 */
	private String stato;

	/**
	 * Crea un nuovo oggetto CitazioneStandard.
	 *
	 * @param sigla sigla repertorio della citazione standard
	 * @param citazione numero della citazione standard
	 * @param stato stato della citazione standard
	 */
	public CitazioneStandard(String sigla, int citazione, String stato){
		this.sigla = sigla;
		this.citazione = citazione;
		this.stato = stato;
	}

	/**
	 * Restituisce il numero della citazione standard.
	 *
	 * @return citazione int
	 */
	public int getCitazione() {
		return citazione;
	}

	/**
	 * Restituisce la sigla repertorio della citazione standard.
	 *
	 * @return sigla String
	 */
	public String getSigla() {
		return (sigla==null)?"":sigla;
	}

	/**
	 * Restituisce lo stato della citazione standard.
	 * INSERIMENTO, CANCELLAZIONE O INVARIATO.
	 *
	 * @return stato String
	 */
	public String getStato() {
		return stato;
	}

	@Override
	public boolean equals(Object citazione){
		try{
			CitazioneStandard altraCitazione = (CitazioneStandard)citazione;
			return 	(
					this.getSigla().equals(altraCitazione.getSigla()) &&
					this.getCitazione() == altraCitazione.getCitazione() );
		}catch ( ClassCastException ccx){
			return false;
		}
	}

}// end class CitazioneStandard
