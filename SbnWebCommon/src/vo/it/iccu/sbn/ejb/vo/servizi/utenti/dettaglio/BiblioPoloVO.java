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


import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.text.ParseException;
import java.util.Locale;

public class BiblioPoloVO extends SerializableVO {

	private static final long serialVersionUID = 7323087469317629281L;
	private String biblioCredito;
	private String biblioNote;
	private String poloCredito;
	private String poloNote;
	private String poloDataRegistrazione;
	private String inizioAuto;
	private String fineAuto;
	private String inizioSosp;
	private String fineSosp;
	private String codBibXUteBib = "";
	private String codPoloXUteBib = "";
	private String codiceAnagrafe = "";
	private String poloInfrazioni;
	private String dataPrimaRegistr;
	private boolean uteBibl=false;

	private Locale locale=Locale.getDefault();
	private String numberFormat="##,###,##0.00";

	public BiblioPoloVO() {
		this.clear();
	}

	public BiblioPoloVO(Locale locale, String numberFormat) {
		this.clear();
		if (locale != null)
			this.locale = locale;
		if (numberFormat != null)
			this.numberFormat = numberFormat;
	}

	public void clear() {
		this.biblioCredito = "";
		this.biblioNote = "";
		this.poloCredito = "";
		this.poloNote = "";
		this.poloDataRegistrazione = "";
		this.poloInfrazioni = "";
		this.fineAuto = "";
		this.fineSosp = "";
		this.inizioAuto = "";
		this.inizioSosp = "";
		this.dataPrimaRegistr = "";
		/* almaviva5_20110506 #4401
		this.codBibXUteBib = "";
		this.codPoloXUteBib = "";
		this.codiceAnagrafe = "";
		*/
	}

	public String trimBibPol(String campo) {
		return campo.trim();
	}

	public String getBiblioCredito() {
		return biblioCredito;
	}

	public double getBiblioCreditoDouble() throws ParseException {
		return getBiblioCredito(numberFormat, locale);
	}

	public double getBiblioCredito(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(biblioCredito, format, locale);
	}

	public void setBiblioCredito(String biblioCredito) {
		this.biblioCredito = biblioCredito;
	}

	public void setBiblioCreditoDouble(double biblioCredito) {
		setBiblioCredito(biblioCredito, numberFormat, locale);
	}

	public void setBiblioCredito(double biblioCredito, String format, Locale locale) {
		this.biblioCredito = ValidazioneDati.getStringFromDouble(biblioCredito, format, locale);
	}

	public String getBiblioNote() {
		return biblioNote;
	}

	public void setBiblioNote(String biblioNote) {
		this.biblioNote = biblioNote;
	}

	public String getPoloCredito() {
		return poloCredito;
	}

	public double getPoloCreditoDouble() throws ParseException {
		return getPoloCredito(numberFormat, locale);
	}

	public double getPoloCredito(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(poloCredito, format, locale);
	}

	public void setPoloCredito(String poloCredito) {
		this.poloCredito = poloCredito;
	}

	public void setPoloCreditoDouble(double poloCredito) {
		setPoloCredito(poloCredito, numberFormat, locale);
	}

	public void setPoloCredito(double poloCredito, String format, Locale locale) {
		this.poloCredito = ValidazioneDati.getStringFromDouble(poloCredito, format, locale);
	}

	public String getPoloDataRegistrazione() {
		return poloDataRegistrazione;
	}

	public void setPoloDataRegistrazione(String poloDataRegistrazione) {
		this.poloDataRegistrazione = poloDataRegistrazione;
	}

	public String getPoloInfrazioni() {
		return poloInfrazioni;
	}

	public void setPoloInfrazioni(String poloInfrazioni) {
		this.poloInfrazioni = poloInfrazioni;
	}

	public String getPoloNote() {
		return poloNote;
	}

	public void setPoloNote(String poloNote) {
		this.poloNote = poloNote;
	}

	public String getFineAuto() {
		return fineAuto;
	}

	public void setFineAuto(String fineAuto) {
		this.fineAuto = fineAuto;
	}

	public String getFineSosp() {
		return fineSosp;
	}

	public void setFineSosp(String fineSosp) {
		this.fineSosp = fineSosp;
	}

	public String getInizioAuto() {
		return inizioAuto;
	}

	public void setInizioAuto(String inizioAuto) {
		this.inizioAuto = inizioAuto;
	}

	public String getInizioSosp() {
		return inizioSosp;
	}

	public void setInizioSosp(String inizioSosp) {
		this.inizioSosp = inizioSosp;
	}

	public String getCodBibXUteBib() {
		return codBibXUteBib;
	}

	public void setCodBibXUteBib(String codBibXUteBib) {
		this.codBibXUteBib = codBibXUteBib;
		if (codBibXUteBib != null && codBibXUteBib.trim().length() != 0) {
			this.uteBibl = true;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BiblioPoloVO other = (BiblioPoloVO) obj;
		if (biblioCredito == null) {
			if (other.biblioCredito != null)
				return false;
		} else if (!biblioCredito.equals(other.biblioCredito))
			return false;
		if (biblioNote == null) {
			if (other.biblioNote != null)
				return false;
		} else if (!biblioNote.equals(other.biblioNote))
			return false;
		if (poloCredito == null) {
			if (other.poloCredito != null)
				return false;
		} else if (!poloCredito.equals(other.poloCredito))
			return false;
		if (poloDataRegistrazione == null) {
			if (other.poloDataRegistrazione != null)
				return false;
		} else if (!poloDataRegistrazione.equals(other.poloDataRegistrazione))
			return false;
		if (poloInfrazioni == null) {
			if (other.poloInfrazioni != null)
				return false;
		} else if (!poloInfrazioni.equals(other.poloInfrazioni))
			return false;
		if (poloNote == null) {
			if (other.poloNote != null)
				return false;
		} else if (!poloNote.equals(other.poloNote))
			return false;
		if (fineAuto == null) {
			if (other.fineAuto != null)
				return false;
		} else if (!fineAuto.equals(other.fineAuto))
			return false;
		if (inizioAuto == null) {
			if (other.inizioAuto != null)
				return false;
		} else if (!inizioAuto.equals(other.inizioAuto))
			return false;
		if (fineSosp == null) {
			if (other.fineSosp != null)
				return false;
		} else if (!fineSosp.equals(other.fineSosp))
			return false;
		if (inizioSosp == null) {
			if (other.inizioSosp != null)
				return false;
		} else if (!inizioSosp.equals(other.inizioSosp))
			return false;
		if (codBibXUteBib == null) {
			if (other.codBibXUteBib != null)
				return false;
		} else if (!codBibXUteBib.equals(other.codBibXUteBib))
			return false;
		return true;
	}

	public String getCodPoloXUteBib() {
		return codPoloXUteBib;
	}

	public void setCodPoloXUteBib(String codPoloXUteBib) {
		this.codPoloXUteBib = codPoloXUteBib;
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

	public String getDataPrimaRegistr() {
		return dataPrimaRegistr;
	}

	public void setDataPrimaRegistr(String dataPrimaRegistr) {
		this.dataPrimaRegistr = dataPrimaRegistr;
	}

	public String getCodiceAnagrafe() {
		return codiceAnagrafe;
	}

	public void setCodiceAnagrafe(String codiceAnagrafe) {
		this.codiceAnagrafe = trimAndSet(codiceAnagrafe);
	}

	public boolean isUteBibl() {
		return uteBibl;
	}

	public void setUteBibl(boolean uteBibl) {
		this.uteBibl = uteBibl;
	}


}
