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
package it.iccu.sbn.polo.orm.bibliografica.viste;

import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

public class V_catalogo_editoria extends Tb_titolo {

	private static final long serialVersionUID = 1L;

	private String isbn;

	private Integer cod_fornitore;
	private String nom_fornitore;
	private String paese;
	private String cod_regione;
	private String ds_regione;
	private String provincia;
	private String ds_provincia;
	private String cap;
	private String citta;
	private String chiave_for;
	private String isbn_editore;

	private String nota_legame;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getNom_fornitore() {
		return nom_fornitore;
	}

	public void setNom_fornitore(String nom_fornitore) {
		this.nom_fornitore = nom_fornitore;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getCod_regione() {
		return cod_regione;
	}

	public void setCod_regione(String cod_regione) {
		this.cod_regione = cod_regione;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getChiave_for() {
		return chiave_for;
	}

	public void setChiave_for(String chiave_for) {
		this.chiave_for = chiave_for;
	}

	public String getIsbn_editore() {
		return isbn_editore;
	}

	public void setIsbn_editore(String isbn_editore) {
		this.isbn_editore = isbn_editore;
	}

	public String getNota_legame() {
		return nota_legame;
	}

	public void setNota_legame(String nota_legame) {
		this.nota_legame = nota_legame;
	}

	public String getDs_regione() {
		return ds_regione;
	}

	public void setDs_regione(String ds_regione) {
		this.ds_regione = ds_regione;
	}

	public String getDs_provincia() {
		return ds_provincia;
	}

	public void setDs_provincia(String ds_provincia) {
		this.ds_provincia = ds_provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public Integer getCod_fornitore() {
		return cod_fornitore;
	}

	public void setCod_fornitore(Integer cod_fornitore) {
		this.cod_fornitore = cod_fornitore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cap == null) ? 0 : cap.hashCode());
		result = prime * result + ((chiave_for == null) ? 0 : chiave_for.hashCode());
		result = prime * result + ((citta == null) ? 0 : citta.hashCode());
		result = prime * result + ((cod_fornitore == null) ? 0 : cod_fornitore.hashCode());
		result = prime * result + ((cod_regione == null) ? 0 : cod_regione.hashCode());
		result = prime * result + ((ds_provincia == null) ? 0 : ds_provincia.hashCode());
		result = prime * result + ((ds_regione == null) ? 0 : ds_regione.hashCode());
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		result = prime * result + ((isbn_editore == null) ? 0 : isbn_editore.hashCode());
		result = prime * result + ((nom_fornitore == null) ? 0 : nom_fornitore.hashCode());
		result = prime * result + ((nota_legame == null) ? 0 : nota_legame.hashCode());
		result = prime * result + ((paese == null) ? 0 : paese.hashCode());
		result = prime * result + ((provincia == null) ? 0 : provincia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		V_catalogo_editoria other = (V_catalogo_editoria) obj;
		if (cap == null) {
			if (other.cap != null) {
				return false;
			}
		} else if (!cap.equals(other.cap)) {
			return false;
		}
		if (chiave_for == null) {
			if (other.chiave_for != null) {
				return false;
			}
		} else if (!chiave_for.equals(other.chiave_for)) {
			return false;
		}
		if (citta == null) {
			if (other.citta != null) {
				return false;
			}
		} else if (!citta.equals(other.citta)) {
			return false;
		}
		if (cod_fornitore == null) {
			if (other.cod_fornitore != null) {
				return false;
			}
		} else if (!cod_fornitore.equals(other.cod_fornitore)) {
			return false;
		}
		if (cod_regione == null) {
			if (other.cod_regione != null) {
				return false;
			}
		} else if (!cod_regione.equals(other.cod_regione)) {
			return false;
		}
		if (ds_provincia == null) {
			if (other.ds_provincia != null) {
				return false;
			}
		} else if (!ds_provincia.equals(other.ds_provincia)) {
			return false;
		}
		if (ds_regione == null) {
			if (other.ds_regione != null) {
				return false;
			}
		} else if (!ds_regione.equals(other.ds_regione)) {
			return false;
		}
		if (isbn == null) {
			if (other.isbn != null) {
				return false;
			}
		} else if (!isbn.equals(other.isbn)) {
			return false;
		}
		if (isbn_editore == null) {
			if (other.isbn_editore != null) {
				return false;
			}
		} else if (!isbn_editore.equals(other.isbn_editore)) {
			return false;
		}
		if (nom_fornitore == null) {
			if (other.nom_fornitore != null) {
				return false;
			}
		} else if (!nom_fornitore.equals(other.nom_fornitore)) {
			return false;
		}
		if (nota_legame == null) {
			if (other.nota_legame != null) {
				return false;
			}
		} else if (!nota_legame.equals(other.nota_legame)) {
			return false;
		}
		if (paese == null) {
			if (other.paese != null) {
				return false;
			}
		} else if (!paese.equals(other.paese)) {
			return false;
		}
		if (provincia == null) {
			if (other.provincia != null) {
				return false;
			}
		} else if (!provincia.equals(other.provincia)) {
			return false;
		}
		return true;
	}

}
