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
package it.finsiel.sbn.polo.orm.viste;

import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.intf.DatiComuni;

/**
 * Classe V_daticomuni ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
 * AUTOMATICAMENTE. NESSUNA MODIFICA DEVE ESSERE APPORTATA MANUALMENTE, PERCHE'
 * SARA' PERSA IN FUTURO. OGNI AGGIUNTA MANUALE NON E' ATTUALMENTE POSSIBILE.
 *
 * <p>
 * Classe che contiene gli attributi estratti dalla tavola del DB
 *
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author generatore automatico di Ragazzini Taymer
 * @version 23/9/2014
 */
public class V_daticomuni extends Tb_titolo implements DatiComuni {

	private static final long serialVersionUID = -7541724048993822195L;

	// Attributi
	private String S181_CD_DIMENSIONE_2;
	private String S181_CD_DIMENSIONE_1;
	private String S125_INDICATORE_TESTO;
	private String S181_CD_SENSORIALE_3_2;
	private String S181_CD_SENSORIALE_3_1;
	private String S181_CD_MOVIMENTO_2;
	private String S181_CD_SENSORIALE_2_2;
	private String S181_CD_MOVIMENTO_1;
	private String S105_TP_TESTO_LETTERARIO;
	private String S181_CD_SENSORIALE_2_1;
	private String S181_CD_SENSORIALE_1_2;
	private String S181_CD_SENSORIALE_1_1;
	private String S181_TP_FORMA_CONTENUTO_2;
	private String S181_TP_FORMA_CONTENUTO_1;
	private String S181_CD_TIPO_CONTENUTO_2;
	private String S181_CD_TIPO_CONTENUTO_1;
	private String S140_TP_TESTO_LETTERARIO;
	private String S182_TP_MEDIAZIONE_2;
	private String S182_TP_MEDIAZIONE_1;

	//almaviva5_20150923 sbnmarc v2.01
	private String S183_TP_SUPPORTO_1;
	private String S183_TP_SUPPORTO_2;

	private String S210_IND_PUBBLICATO;

	public String toString() {
		return String.valueOf(((getBID() == null) ? "" : String
				.valueOf(getBID())));
	}

	public String getS105_TP_TESTO_LETTERARIO() {
		return S105_TP_TESTO_LETTERARIO;
	}

	public void setS105_TP_TESTO_LETTERARIO(String value) {
		S105_TP_TESTO_LETTERARIO = value;
		this.settaParametro(KeyParameter.XXXs105_tp_testo_letterario, value);
	}

	public String getS140_TP_TESTO_LETTERARIO() {
		return S140_TP_TESTO_LETTERARIO;
	}

	public void setS140_TP_TESTO_LETTERARIO(String value) {
		S140_TP_TESTO_LETTERARIO = value;
		this.settaParametro(KeyParameter.XXXs140_tp_testo_letterario, value);
	}

	public String getS181_TP_FORMA_CONTENUTO_1() {
		return S181_TP_FORMA_CONTENUTO_1;
	}

	public void setS181_TP_FORMA_CONTENUTO_1(String value) {
		S181_TP_FORMA_CONTENUTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_tp_forma_contenuto_1, value);
	}

	public String getS181_CD_TIPO_CONTENUTO_1() {
		return S181_CD_TIPO_CONTENUTO_1;
	}

	public void setS181_CD_TIPO_CONTENUTO_1(String value) {
		S181_CD_TIPO_CONTENUTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_tipo_contenuto_1, value);
	}

	public String getS181_CD_MOVIMENTO_1() {
		return S181_CD_MOVIMENTO_1;
	}

	public void setS181_CD_MOVIMENTO_1(String value) {
		S181_CD_MOVIMENTO_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_movimento_1, value);
	}

	public String getS181_CD_DIMENSIONE_1() {
		return S181_CD_DIMENSIONE_1;
	}

	public void setS181_CD_DIMENSIONE_1(String value) {
		S181_CD_DIMENSIONE_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_dimensione_1, value);
	}

	public String getS181_CD_SENSORIALE_1_1() {
		return S181_CD_SENSORIALE_1_1;
	}

	public void setS181_CD_SENSORIALE_1_1(String value) {
		S181_CD_SENSORIALE_1_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_1_1, value);
	}

	public String getS181_CD_SENSORIALE_2_1() {
		return S181_CD_SENSORIALE_2_1;
	}

	public void setS181_CD_SENSORIALE_2_1(String value) {
		S181_CD_SENSORIALE_2_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_2_1, value);
	}

	public String getS181_CD_SENSORIALE_3_1() {
		return S181_CD_SENSORIALE_3_1;
	}

	public void setS181_CD_SENSORIALE_3_1(String value) {
		S181_CD_SENSORIALE_3_1 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_3_1, value);
	}

	public String getS181_TP_FORMA_CONTENUTO_2() {
		return S181_TP_FORMA_CONTENUTO_2;
	}

	public void setS181_TP_FORMA_CONTENUTO_2(String value) {
		S181_TP_FORMA_CONTENUTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_tp_forma_contenuto_2, value);
	}

	public String getS181_CD_TIPO_CONTENUTO_2() {
		return S181_CD_TIPO_CONTENUTO_2;
	}

	public void setS181_CD_TIPO_CONTENUTO_2(String value) {
		S181_CD_TIPO_CONTENUTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_tipo_contenuto_2, value);
	}

	public String getS181_CD_MOVIMENTO_2() {
		return S181_CD_MOVIMENTO_2;
	}

	public void setS181_CD_MOVIMENTO_2(String value) {
		S181_CD_MOVIMENTO_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_movimento_2, value);
	}

	public String getS181_CD_DIMENSIONE_2() {
		return S181_CD_DIMENSIONE_2;
	}

	public void setS181_CD_DIMENSIONE_2(String value) {
		S181_CD_DIMENSIONE_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_dimensione_2, value);
	}

	public String getS181_CD_SENSORIALE_1_2() {
		return S181_CD_SENSORIALE_1_2;
	}

	public void setS181_CD_SENSORIALE_1_2(String value) {
		S181_CD_SENSORIALE_1_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_1_2, value);
	}

	public String getS181_CD_SENSORIALE_2_2() {
		return S181_CD_SENSORIALE_2_2;
	}

	public void setS181_CD_SENSORIALE_2_2(String value) {
		S181_CD_SENSORIALE_2_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_2_2, value);
	}

	public String getS181_CD_SENSORIALE_3_2() {
		return S181_CD_SENSORIALE_3_2;
	}

	public void setS181_CD_SENSORIALE_3_2(String value) {
		S181_CD_SENSORIALE_3_2 = value;
		this.settaParametro(KeyParameter.XXXs181_cd_sensoriale_3_2, value);
	}

	public String getS182_TP_MEDIAZIONE_1() {
		return S182_TP_MEDIAZIONE_1;
	}

	public void setS182_TP_MEDIAZIONE_1(String value) {
		S182_TP_MEDIAZIONE_1 = value;
		this.settaParametro(KeyParameter.XXXs182_tp_mediazione_1, value);
	}

	public String getS182_TP_MEDIAZIONE_2() {
		return S182_TP_MEDIAZIONE_2;
	}

	public void setS182_TP_MEDIAZIONE_2(String value) {
		S182_TP_MEDIAZIONE_2 = value;
		this.settaParametro(KeyParameter.XXXs182_tp_mediazione_2, value);
	}

	public String getS125_INDICATORE_TESTO() {
		return S125_INDICATORE_TESTO;
	}

	public void setS125_INDICATORE_TESTO(String value) {
		S125_INDICATORE_TESTO = value;
		this.settaParametro(KeyParameter.XXXs125_indicatore_testo, value);
	}

	public String getS183_TP_SUPPORTO_1() {
		return S183_TP_SUPPORTO_1;
	}

	public void setS183_TP_SUPPORTO_1(String value) {
		S183_TP_SUPPORTO_1 = value;
		this.settaParametro(KeyParameter.XXXs183_tp_supporto_1, value);
	}

	public String getS183_TP_SUPPORTO_2() {
		return S183_TP_SUPPORTO_2;
	}

	public void setS183_TP_SUPPORTO_2(String value) {
		S183_TP_SUPPORTO_2 = value;
		this.settaParametro(KeyParameter.XXXs183_tp_supporto_2, value);
	}

	public String getS210_IND_PUBBLICATO() {
		return S210_IND_PUBBLICATO;
	}

	public void setS210_IND_PUBBLICATO(String value) {
		S210_IND_PUBBLICATO = value;
		this.settaParametro(KeyParameter.XXXs210_ind_pubblicato, value);
	}

}
