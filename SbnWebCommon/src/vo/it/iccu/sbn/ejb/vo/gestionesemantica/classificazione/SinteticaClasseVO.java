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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.Comparator;

public class SinteticaClasseVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -3420723577898214145L;

	private int progr;
	private String identificativoClasse;
	private String sistema; // sist. classificazione
	private String simbolo; // notazione
	private String livelloAutorita;
	private String edDewey; // edizione
	private String indicatore;
	private String parole;
	private String condivisoLista;
	private int numTitoliPolo;
	private int numTitoliBiblio;
	private short rank;
	private boolean costruito = false;
	private SimboloDeweyVO simboloDewey = new SimboloDeweyVO();

	public static final Comparator<SinteticaClasseVO> ORDINA_PER_PROGRESSIVO = new Comparator<SinteticaClasseVO>() {
		public int compare(SinteticaClasseVO o1, SinteticaClasseVO o2) {
			int p1 = o1.getProgr();
			int p2 = o2.getProgr();
			return p1 - p2;
		}
	};

	public SinteticaClasseVO() {
		super();
	}

	public SinteticaClasseVO(int progr, String notazione, String stato,
			String indicatore, String condivisoLista, String descrizione)
			throws Exception {

		this.progr = progr;
		this.simbolo = notazione;
		this.livelloAutorita = stato;
		this.indicatore = indicatore;
		this.condivisoLista = condivisoLista;
		this.parole = descrizione;

	}

	public SinteticaClasseVO(CreaVariaClasseVO classe) throws ValidationException {
		setIdentificativoClasse(classe.getT001());
		this.sistema = classe.getCodSistemaClassificazione();
		this.edDewey = trimOrFill(classe.getCodEdizioneDewey(), ' ', 2);
		this.livelloAutorita = classe.getLivello();
		this.parole = classe.getDescrizione();
		this.costruito = classe.isCostruito();
		this.setLivelloPolo(classe.isLivelloPolo());
	}

	public String getParole() {
		return parole;
	}

	public void setParole(String parole) {
		this.parole = trimAndSet(parole);
	}

	public String getIndicatore() {
		return indicatore;
	}

	public void setIndicatore(String indicatore) {
		this.indicatore = indicatore;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String notazione) {
		this.simbolo = trimAndSet(notazione);
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String stato) {
		this.livelloAutorita = stato;
	}

	public String getEdDewey() {
		return edDewey;
	}

	public void setEdDewey(String edDewey) {
		this.edDewey = trimOrFill(edDewey, ' ', 2);
	}

	public String getIdentificativoClasse() {
		return identificativoClasse;
	}

	public void setIdentificativoClasse(String identificativoClasse) throws ValidationException {
		this.identificativoClasse = trimAndSet(identificativoClasse);
		this.simboloDewey = SimboloDeweyVO.parse(this.identificativoClasse);
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getCondivisoLista() {
		return condivisoLista;
	}

	public void setCondivisoLista(String condivisoLista) {
		this.condivisoLista = condivisoLista;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

	public void setCostruito(boolean costruito) {
		this.costruito = costruito;
	}

	public boolean isCostruito() {
		return costruito;
	}

	public SimboloDeweyVO getSimboloDewey() {
		return simboloDewey;
	}

	public void setSimboloDewey(SimboloDeweyVO simboloDewey) {
		this.simboloDewey = simboloDewey;
	}

	@Override
	public int getRepeatableId() {
		return (identificativoClasse != null ? identificativoClasse.hashCode() : this.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SinteticaClasseVO other = (SinteticaClasseVO) obj;
		if (edDewey == null) {
			if (other.edDewey != null)
				return false;
		} else if (!edDewey.equals(other.edDewey))
			return false;
		if (identificativoClasse == null) {
			if (other.identificativoClasse != null)
				return false;
		} else if (!identificativoClasse.equals(other.identificativoClasse))
			return false;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		if (sistema == null) {
			if (other.sistema != null)
				return false;
		} else if (!sistema.equals(other.sistema))
			return false;
		return true;
	}

}
