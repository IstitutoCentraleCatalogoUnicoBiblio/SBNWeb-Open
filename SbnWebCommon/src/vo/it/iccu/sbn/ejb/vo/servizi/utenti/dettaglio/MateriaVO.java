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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.Comparator;

public class MateriaVO extends BaseVO {

	private static final long serialVersionUID = 4076009448637383897L;
	public static final int INIZIALE_NON_SELEZIONATO           = 0; //Sel iniziale NO
	public static final int INSERITO_NUOVO                     = 1; //Inserito
	public static final int INIZIALE_SELEZIONATO               = 2; //Sel iniziale SI
	public static final int CANCELLATO_DA_INIZIALE_SELEZIONATO = 3; //Cancellato da ISI


	public static final Comparator<MateriaVO> ORDINAMENTO_PER_DESCRIZIONE = new Comparator<MateriaVO>() {
		public int compare(MateriaVO o1, MateriaVO o2) {
			return o1.getDescrizione().compareTo(o2.getDescrizione());
		}
	};


	private String codPolo;
	private String codBib;
	private String codice;
	private String descrizione;
	private int    stato = INIZIALE_NON_SELEZIONATO;
	private String selezionato;
	private int    progressivo;
	private int    idMateria;


	public MateriaVO(String codice, String descrizione, String selezionato) throws Exception {
		super();

		setCodice(codice);
		setDescrizione(descrizione);
		this.selezionato = "";
	}

	public MateriaVO() {
		super();
	}


	public int getIdMateria() {
		return idMateria;
	}

	public void setIdMateria(int idMateria) {
		this.idMateria = idMateria;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = trimAndSet(codice);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public String getSelezionato() {
		if (selezionato == null) selezionato = "";
		return selezionato;
	}

	public void setSelezionato(String selezionato) {
		if (selezionato == null) selezionato = "";
		this.selezionato = selezionato;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MateriaVO other = (MateriaVO) obj;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;

		return true;
	}

}
