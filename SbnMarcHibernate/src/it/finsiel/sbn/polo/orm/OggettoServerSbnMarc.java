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
package it.finsiel.sbn.polo.orm;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe OggettoServerSbnMarc
 * <p>
 * Classe da cui ereditano tutti gli oggetti di tipo attributitavole. Contiene
 * una struttura dati in cui settare i parametri che mappano i valori
 * all'interno del file contenente il codice sql.
 * </p>
 *
 * @author
 * @author
 *
 * @version 14-feb-03
 */
public abstract class OggettoServerSbnMarc implements Serializable {

	private static final long serialVersionUID = -4434807201757105608L;
	// Hashtable parametri = new Hashtable();
	// Utilizzo una hashmap per consentire valori nulli, non è sincronizzata, ma
	// non dovrebbero sussistere problemi di concorrenza.
	protected HashMap<String, Object> parametri = new HashMap<String, Object>();

	public String leggiParametro(String attributo) {
		String valore = (String) parametri.get(attributo);
		return valore;
	}

	public HashMap<String, Object> leggiAllParametro() {
		return parametri;
	}

	/*
	 * public void settaParametro(String attributo, java.util.Set value) {
	 * if(value!=null) parametri.put(attributo, value); } public void
	 * settaParametro(String attributo, java.sql.Blob value) { if(value!=null)
	 * parametri.put(attributo, value); } public void settaParametro(String
	 * attributo, Integer value) { if(value!=null) parametri.put(attributo,
	 * value); } public void settaParametro(String attributo, Long value) {
	 * if(value!=null) parametri.put(attributo, value); } public void
	 * settaParametro(String attributo, Date value) { if(value!=null)
	 * parametri.put(attributo, value); }
	 */
	/** Setta un parametro di nome attributo con il rispettivo valore */
	public void settaParametro(String attributo, Object valore) {
		if (valore != null)
			parametri.put(attributo, valore);
	}

	/** Azzera tutti i parametri valorizzati */
	public void azzeraParametri() {
		parametri.clear();
	}

	/**
	 * Passando il nome di un campo, lancio la relativa funzione di
	 * getNomeCampo() che mi restituisce il suo valore del campo sotto forma di
	 * stringa.
	 *
	 * Se il campo non esiste, mi restituisce null.
	 *
	 * @param record
	 * @param field
	 * @return 17-giu-03 @author sanba
	 */
	public String getField(String field) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String ret = null;
		Method metodo = null;

		if (field != null && field.trim().length() > 0) {
			char[] nome = field.toLowerCase().toCharArray();
			nome[0] = ((("" + nome[0]).toUpperCase()).toCharArray())[0];
			Class<? extends OggettoServerSbnMarc> clazz = this.getClass();
			metodo = clazz.getMethod("get" + new String(nome), (Class<?>[])null);
			ret = "" + metodo.invoke(this, (Object[])null);
		}
		return ret;
	}

	// CAMPI NUOVA ISTANZA
	private String FL_CONDIVISO;
	private String UTE_CONDIVISO;
	private Date TS_CONDIVISO;
	private String UTE_INS;
	private Date TS_INS;
	private String UTE_VAR;
	private Date TS_VAR;
	private String FL_CANC;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}

	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}

	public String getUniqueId() {
		return "" + this.hashCode();
	}

	public void setUTE_INS(String value) {
		this.UTE_INS = value;
	this.settaParametro(KeyParameter.XXXute_ins,value);
	}

	public String getUTE_INS() {
		return UTE_INS;
	}

	public void setTS_INS(Date value) {
		this.TS_INS = value;
	this.settaParametro(KeyParameter.XXXts_ins,value);
	}

	public Date getTS_INS() {
		return TS_INS;
	}

	public void setUTE_VAR(String value) {
		this.UTE_VAR = value;
	this.settaParametro(KeyParameter.XXXute_var,value);
	}

	public String getUTE_VAR() {
		return UTE_VAR;
	}

	public void setTS_VAR(Date value) {
		this.TS_VAR = value;
	this.settaParametro(KeyParameter.XXXts_var,value);
	}

	public Date getTS_VAR() {
		return TS_VAR;
	}

	public void setFL_CANC(String value) {
		this.FL_CANC = value;
	this.settaParametro(KeyParameter.XXXfl_canc,value);
	}

	public String getFL_CANC() {
		return FL_CANC;
	}

}
