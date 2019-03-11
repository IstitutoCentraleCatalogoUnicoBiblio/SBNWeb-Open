/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: almaviva
 * License Type: Purchased
 */
package it.iccu.sbn.ejb.vo.servizi.sale;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;


public class PostoSalaVO extends BaseVO {

	private static final long serialVersionUID = -2782055010921793862L;

	private int id_posto;
	private int idSala;
	private short num_posto;
	private boolean occupato;
	private short gruppo = 1;

	private SalaVO sala;

	private List<String> categorieMediazione = new ArrayList<String>();

	public int getId_posto() {
		return id_posto;
	}

	public void setId_posto(int id_posto) {
		this.id_posto = id_posto;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int sala) {
		this.idSala = sala;
	}

	public short getNum_posto() {
		return num_posto;
	}

	public void setNum_posto(short num_posto) {
		this.num_posto = num_posto;
	}

	public boolean isOccupato() {
		return occupato;
	}

	public void setOccupato(boolean occupato) {
		this.occupato = occupato;
	}

	public short getGruppo() {
		return gruppo;
	}

	public void setGruppo(short gruppo) {
		this.gruppo = gruppo;
	}

	public SalaVO getSala() {
		return sala;
	}

	public void setSala(SalaVO sala) {
		this.sala = sala;
	}

	public boolean isNuovo() {
		return id_posto == 0;
	}

	public List<String> getCategorieMediazione() {
		return categorieMediazione;
	}

	public void setCategorieMediazione(List<String> categorieMediazione) {
		this.categorieMediazione = categorieMediazione;
	}

	public int compareTo(PostoSalaVO ps) {
		int cmp = this.gruppo - ps.gruppo;
		cmp = cmp != 0 ? cmp : this.num_posto - ps.num_posto;
		return cmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + gruppo;
		result = prime * result + idSala;
		result = prime * result + id_posto;
		result = prime * result + num_posto;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PostoSalaVO other = (PostoSalaVO) obj;
		if (gruppo != other.gruppo) {
			return false;
		}
		if (idSala != other.idSala) {
			return false;
		}
		if (id_posto != other.id_posto) {
			return false;
		}
		if (num_posto != other.num_posto) {
			return false;
		}
		return true;
	}

}
