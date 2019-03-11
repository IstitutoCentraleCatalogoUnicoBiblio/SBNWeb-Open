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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.util.Constants;
import it.iccu.sbn.web.constant.ConstantDefault;

public class RicercaClassiVO extends SerializableVO {

	private static final long serialVersionUID = 4654119063363122027L;

	private boolean polo;
	private String identificativoClasse; // Sistema Edizione Simbolo
	private String simbolo;
	private String parole;
	private String livelloAutoritaDa;
	private String livelloAutoritaA;
	private String elemBlocco;
	private String codSistemaClassificazione;
	private String descSistemaClassificazione;
	private String codEdizioneDewey = "  ";
	private String descEdizioneDewey;
	private TipoOrdinamentoClasse ordinamentoClasse;
	private boolean utilizzati;
	private boolean puntuale;
	private boolean ricercaPerImporta = false;

	public RicercaClassiVO() {
		super();
		this.elemBlocco = ConstantDefault.ELEMENTI_BLOCCHI.getDefault();
		this.ordinamentoClasse = TipoOrdinamentoClasse.PER_ID;
	}


	public boolean isPuntuale() {
		return puntuale;
	}

	public void setPuntuale(boolean puntuale) {
		this.puntuale = puntuale;
	}

	public boolean isUtilizzati() {
		return utilizzati;
	}

	public void setUtilizzati(boolean utilizzati) {
		this.utilizzati = utilizzati;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public boolean isDewey() {
		return (isFilled(codSistemaClassificazione) && codSistemaClassificazione.equals(Constants.Semantica.Classi.SISTEMA_CLASSE_DEWEY));
	}

	public String getLivelloAutoritaA() {
		return livelloAutoritaA;
	}

	public void setLivelloAutoritaA(String livelloAutoritaA) {
		this.livelloAutoritaA = livelloAutoritaA;
	}

	public String getLivelloAutoritaDa() {
		return livelloAutoritaDa;
	}

	public void setLivelloAutoritaDa(String livelloAutoritaDa) {
		this.livelloAutoritaDa = livelloAutoritaDa;
	}


	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}


	public String getIdentificativoClasse() {
		return identificativoClasse;
	}

	public void setIdentificativoClasse(String identificativoClasse) {
		this.identificativoClasse = identificativoClasse;
	}

	public String getParole() {
		return parole;
	}

	public void setParole(String parole) {
		this.parole = parole;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = trimAndSet(simbolo);
	}

	public String getCodSistemaClassificazione() {
		return codSistemaClassificazione;
	}

	public void setCodSistemaClassificazione(String codSistemaClassificazione) {
		this.codSistemaClassificazione = codSistemaClassificazione;
	}

	public String getDescSistemaClassificazione() {
		return descSistemaClassificazione;
	}

	public void setDescSistemaClassificazione(String descSistemaClassificazione) {
		this.descSistemaClassificazione = descSistemaClassificazione;
	}

	public TipoOrdinamentoClasse getOrdinamentoClasse() {
		return ordinamentoClasse;
	}

	public void setOrdinamentoClasse(TipoOrdinamentoClasse ordinamentoClasse) {
		this.ordinamentoClasse = ordinamentoClasse;
	}

	public String getOrdinamento() {
		return this.ordinamentoClasse.toString();
	}

	public void setOrdinamento(String ordinamentoClasse) {
		this.ordinamentoClasse = TipoOrdinamentoClasse.valueOf(ordinamentoClasse);
	}

	public String getCodEdizioneDewey() {
		return codEdizioneDewey;
	}

	public void setCodEdizioneDewey(String codEdizioneDewey) {
		this.codEdizioneDewey = trimOrFill(codEdizioneDewey, ' ', 2);
	}

	public String getDescEdizioneDewey() {
		return descEdizioneDewey;
	}

	public void setDescEdizioneDewey(String descEdizioneDewey) {
		this.descEdizioneDewey = descEdizioneDewey;
	}

	public boolean isRicercaPerImporta() {
		return this.ricercaPerImporta;
	}

	public void setRicercaPerImporta(boolean ricercaPerImporta) {
		this.ricercaPerImporta = ricercaPerImporta;
	}

	public boolean isIndice() {
		return !polo;
	}

}
