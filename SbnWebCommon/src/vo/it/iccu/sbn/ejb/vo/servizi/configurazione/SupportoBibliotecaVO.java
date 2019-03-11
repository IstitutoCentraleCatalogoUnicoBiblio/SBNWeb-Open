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
package it.iccu.sbn.ejb.vo.servizi.configurazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.text.ParseException;
import java.util.Locale;

public class SupportoBibliotecaVO extends BaseVO {

	private static final long serialVersionUID = 8413325356257045511L;
	private int id;
	private String cd_polo;
	private String cd_bib;
	private String codSupporto;
	private String descrizione;
	private String importoUnitario;
	private String costoFisso;

	private String flSvolg = "L";

	/**
	 * Locale per la gestione della conversione dell'importo unitario da stringa
	 * a numerico e viceversa
	 */
	private Locale locale = Locale.getDefault();
	/**
	 * Formato per la gestione della conversione dell'importo unitario da
	 * stringa a numerico e viceversa
	 */
	private String numberFormat = "##,###,##0.00";

	public SupportoBibliotecaVO() {
	}

	public SupportoBibliotecaVO(Locale locale, String numberFormat) {
		this.locale = locale;
		this.numberFormat = numberFormat;
	}

	public SupportoBibliotecaVO(BaseVO base) {
		super(base);
	}

	public SupportoBibliotecaVO(BaseVO base, Locale locale, String numberFormat) {
		super(base);
		this.locale = locale;
		this.numberFormat = numberFormat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public void setCd_polo(String cd_polo) {
		this.cd_polo = cd_polo;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
	}

	public String getCodSupporto() {
		return codSupporto;
	}

	public void setCodSupporto(String codSupporto) {
		this.codSupporto = codSupporto;
	}

	public String getImportoUnitario() {
		return importoUnitario;
	}

	public double getImportoUnitarioDouble() throws ParseException {
		return this.getImportoUnitario(locale, numberFormat);
	}

	public double getImportoUnitario(Locale locale, String numberFormat)
			throws ParseException {
		if (isFilled(importoUnitario))
			return ValidazioneDati.getDoubleFromString(importoUnitario,
					numberFormat, locale);
		else
			return 0;
	}

	public void setImportoUnitario(String importoUnitario) {
		this.importoUnitario = importoUnitario;
	}

	public void setImportoUnitario(double importo) {
		this.setImportoUnitario(importo, locale, numberFormat);
	}

	public void setImportoUnitario(double importo, Locale locale,
			String numberFormat) {
		this.importoUnitario = ValidazioneDati.getStringFromDouble(importo,
				numberFormat, locale);
	}

	public String getCostoFisso() {
		return costoFisso;
	}

	public double getCostoFissoDouble() throws ParseException {
		return this.getCostoFisso(locale, numberFormat);
	}

	public double getCostoFisso(Locale locale, String numberFormat)
			throws ParseException {
		if (isFilled(costoFisso))
			return ValidazioneDati.getDoubleFromString(costoFisso,
					numberFormat, locale);
		else
			return 0;
	}

	public void setCostoFisso(String costoFisso) {
		this.costoFisso = costoFisso;
	}

	public void setCostoFisso(double importo) {
		this.setCostoFisso(importo, locale, numberFormat);
	}

	public void setCostoFisso(double importo, Locale locale,
			String numberFormat) {
		this.costoFisso = ValidazioneDati.getStringFromDouble(importo,
				numberFormat, locale);
	}


	public String getFlSvolg() {
		return flSvolg;
	}

	public void setFlSvolg(String flSvolg) {
		this.flSvolg = trimAndSet(flSvolg);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	@Override
	public boolean equals(Object obj) {
		final SupportoBibliotecaVO other = (SupportoBibliotecaVO) obj;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		if (cd_polo == null) {
			if (other.cd_polo != null)
				return false;
		} else if (!cd_polo.equals(other.cd_polo))
			return false;

		if (cd_bib == null) {
			if (other.cd_bib != null)
				return false;
		} else if (!cd_bib.equals(other.cd_bib))
			return false;

		if (codSupporto == null) {
			if (other.codSupporto != null)
				return false;
		} else if (!codSupporto.equals(other.codSupporto))
			return false;

		if (importoUnitario == null) {
			if (other.importoUnitario != null)
				return false;
		} else if (!importoUnitario.equals(other.importoUnitario))
			return false;

		if (costoFisso == null) {
			if (other.costoFisso != null)
				return false;
		} else if (!costoFisso.equals(other.costoFisso))
			return false;

		return true;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public boolean isNuovo() {
		return id == 0;
	}

	public boolean isLocale() {
		return flSvolg.equals("L");
	}


}
