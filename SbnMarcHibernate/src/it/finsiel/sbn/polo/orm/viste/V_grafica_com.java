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

/**
 * Classe V_grafica_com ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
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
 * @version 6/10/2014
 */
public class V_grafica_com extends V_daticomuni {

	private static final long serialVersionUID = 1684604600428641593L;

	// Attributi
	private String S181_CD_SENSORIALE_1_2;
	private String S181_CD_SENSORIALE_1_1;
	private String S181_CD_TIPO_CONTENUTO_2;
	private String S181_CD_TIPO_CONTENUTO_1;
	private String S181_CD_DIMENSIONE_2;
	private String S181_CD_DIMENSIONE_1;
	private String S181_TP_FORMA_CONTENUTO_2;
	private String S181_TP_FORMA_CONTENUTO_1;
	private String S140_TP_TESTO_LETTERARIO;
	private String S105_TP_TESTO_LETTERARIO;
	private String S181_CD_MOVIMENTO_2;
	private String S181_CD_MOVIMENTO_1;
	private String CD_SUPPORTO;
	private String S125_INDICATORE_TESTO;
	private String CD_LIVELLO_G;
	private String CD_DESIGN_FUNZ;
	private String TP_MATERIALE_GRA;
	private String CD_TECNICA_DIS_3;
	private String CD_TECNICA_DIS_2;
	private String CD_TECNICA_DIS_1;
	private String CD_TECNICA_STA_3;
	private String CD_TECNICA_STA_2;
	private String CD_TECNICA_STA_1;
	private String S181_CD_SENSORIALE_3_2;
	private String CD_COLORE;
	private String S181_CD_SENSORIALE_3_1;
	private String S181_CD_SENSORIALE_2_2;
	private String S182_TP_MEDIAZIONE_2;
	private String S181_CD_SENSORIALE_2_1;
	private String S182_TP_MEDIAZIONE_1;

	public void setCD_LIVELLO_G(String value) {
		this.CD_LIVELLO_G = value;
		this.settaParametro(KeyParameter.XXXcd_livello_g, value);
	}

	public String getCD_LIVELLO_G() {
		return CD_LIVELLO_G;
	}

	public void setTP_MATERIALE_GRA(String value) {
		this.TP_MATERIALE_GRA = value;
		this.settaParametro(KeyParameter.XXXtp_materiale_gra, value);
	}

	public String getTP_MATERIALE_GRA() {
		return TP_MATERIALE_GRA;
	}

	public void setCD_SUPPORTO(String value) {
		this.CD_SUPPORTO = value;
		this.settaParametro(KeyParameter.XXXcd_supporto, value);
	}

	public String getCD_SUPPORTO() {
		return CD_SUPPORTO;
	}

	public void setCD_COLORE(String value) {
		this.CD_COLORE = value;
		this.settaParametro(KeyParameter.XXXcd_colore, value);
	}

	public String getCD_COLORE() {
		return CD_COLORE;
	}

	public void setCD_TECNICA_DIS_1(String value) {
		this.CD_TECNICA_DIS_1 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_dis_1, value);
	}

	public String getCD_TECNICA_DIS_1() {
		return CD_TECNICA_DIS_1;
	}

	public void setCD_TECNICA_DIS_2(String value) {
		this.CD_TECNICA_DIS_2 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_dis_2, value);
	}

	public String getCD_TECNICA_DIS_2() {
		return CD_TECNICA_DIS_2;
	}

	public void setCD_TECNICA_DIS_3(String value) {
		this.CD_TECNICA_DIS_3 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_dis_3, value);
	}

	public String getCD_TECNICA_DIS_3() {
		return CD_TECNICA_DIS_3;
	}

	public void setCD_TECNICA_STA_1(String value) {
		this.CD_TECNICA_STA_1 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_sta_1, value);
	}

	public String getCD_TECNICA_STA_1() {
		return CD_TECNICA_STA_1;
	}

	public void setCD_TECNICA_STA_2(String value) {
		this.CD_TECNICA_STA_2 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_sta_2, value);
	}

	public String getCD_TECNICA_STA_2() {
		return CD_TECNICA_STA_2;
	}

	public void setCD_TECNICA_STA_3(String value) {
		this.CD_TECNICA_STA_3 = value;
		this.settaParametro(KeyParameter.XXXcd_tecnica_sta_3, value);
	}

	public String getCD_TECNICA_STA_3() {
		return CD_TECNICA_STA_3;
	}

	public void setCD_DESIGN_FUNZ(String value) {
		this.CD_DESIGN_FUNZ = value;
		this.settaParametro(KeyParameter.XXXcd_design_funz, value);
	}

	public String getCD_DESIGN_FUNZ() {
		return CD_DESIGN_FUNZ;
	}

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

}
