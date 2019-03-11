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
 * Classe V_musica_com ATTENZIONE! QUESTA CLASSE E' STATA GENERATA
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
 * @version 3/10/2014
 */
public class V_musica_com extends V_daticomuni {

	private static final long serialVersionUID = 8378277895066547407L;

	// Attributi
	private String S181_CD_SENSORIALE_1_2;
	private String S181_CD_SENSORIALE_1_1;
	private String FL_PALINSESTO;
	private String NOTAZIONE_MUSICALE;
	private String CD_PRESENTAZIONE;
	private String S181_CD_TIPO_CONTENUTO_2;
	private String S181_CD_TIPO_CONTENUTO_1;
	private String S181_CD_DIMENSIONE_2;
	private String S181_CD_DIMENSIONE_1;
	private String S181_TP_FORMA_CONTENUTO_2;
	private String S181_TP_FORMA_CONTENUTO_1;
	private String DS_ORG_ANAL;
	private String TP_ELABORAZIONE;
	private String S140_TP_TESTO_LETTERARIO;
	private String S105_TP_TESTO_LETTERARIO;
	private String DS_LEGATURA;
	private String S181_CD_MOVIMENTO_2;
	private String S181_CD_MOVIMENTO_1;
	private String CD_STESURA;
	private String DATAZIONE;
	private String TP_TESTO_LETTER;
	private String DS_CONSERVAZIONE;
	private String S125_INDICATORE_TESTO;
	private String CD_LIVELLO_M;
	private String DS_ORG_SINT;
	private String DS_ILLUSTRAZIONI;
	private String FL_COMPOSITO;
	private String CD_MATERIA;
	private String S181_CD_SENSORIALE_3_2;
	private String S181_CD_SENSORIALE_3_1;
	private String S181_CD_SENSORIALE_2_2;
	private String S182_TP_MEDIAZIONE_2;
	private String S181_CD_SENSORIALE_2_1;
	private String S182_TP_MEDIAZIONE_1;

	public void setCD_LIVELLO_M(String value) {
		this.CD_LIVELLO_M = value;
		this.settaParametro(KeyParameter.XXXcd_livello_m, value);
	}

	public String getCD_LIVELLO_M() {
		return CD_LIVELLO_M;
	}

	public void setDS_ORG_SINT(String value) {
		this.DS_ORG_SINT = value;
		this.settaParametro(KeyParameter.XXXds_org_sint, value);
	}

	public String getDS_ORG_SINT() {
		return DS_ORG_SINT;
	}

	public void setDS_ORG_ANAL(String value) {
		this.DS_ORG_ANAL = value;
		this.settaParametro(KeyParameter.XXXds_org_anal, value);
	}

	public String getDS_ORG_ANAL() {
		return DS_ORG_ANAL;
	}

	public void setTP_ELABORAZIONE(String value) {
		this.TP_ELABORAZIONE = value;
		this.settaParametro(KeyParameter.XXXtp_elaborazione, value);
	}

	public String getTP_ELABORAZIONE() {
		return TP_ELABORAZIONE;
	}

	public void setCD_STESURA(String value) {
		this.CD_STESURA = value;
		this.settaParametro(KeyParameter.XXXcd_stesura, value);
	}

	public String getCD_STESURA() {
		return CD_STESURA;
	}

	public void setFL_COMPOSITO(String value) {
		this.FL_COMPOSITO = value;
		this.settaParametro(KeyParameter.XXXfl_composito, value);
	}

	public String getFL_COMPOSITO() {
		return FL_COMPOSITO;
	}

	public void setFL_PALINSESTO(String value) {
		this.FL_PALINSESTO = value;
		this.settaParametro(KeyParameter.XXXfl_palinsesto, value);
	}

	public String getFL_PALINSESTO() {
		return FL_PALINSESTO;
	}

	public void setDATAZIONE(String value) {
		this.DATAZIONE = value;
		this.settaParametro(KeyParameter.XXXdatazione, value);
	}

	public String getDATAZIONE() {
		return DATAZIONE;
	}

	public void setCD_PRESENTAZIONE(String value) {
		this.CD_PRESENTAZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_presentazione, value);
	}

	public String getCD_PRESENTAZIONE() {
		return CD_PRESENTAZIONE;
	}

	public void setCD_MATERIA(String value) {
		this.CD_MATERIA = value;
		this.settaParametro(KeyParameter.XXXcd_materia, value);
	}

	public String getCD_MATERIA() {
		return CD_MATERIA;
	}

	public void setDS_ILLUSTRAZIONI(String value) {
		this.DS_ILLUSTRAZIONI = value;
		this.settaParametro(KeyParameter.XXXds_illustrazioni, value);
	}

	public String getDS_ILLUSTRAZIONI() {
		return DS_ILLUSTRAZIONI;
	}

	public void setNOTAZIONE_MUSICALE(String value) {
		this.NOTAZIONE_MUSICALE = value;
		this.settaParametro(KeyParameter.XXXnotazione_musicale, value);
	}

	public String getNOTAZIONE_MUSICALE() {
		return NOTAZIONE_MUSICALE;
	}

	public void setDS_LEGATURA(String value) {
		this.DS_LEGATURA = value;
		this.settaParametro(KeyParameter.XXXds_legatura, value);
	}

	public String getDS_LEGATURA() {
		return DS_LEGATURA;
	}

	public void setDS_CONSERVAZIONE(String value) {
		this.DS_CONSERVAZIONE = value;
		this.settaParametro(KeyParameter.XXXds_conservazione, value);
	}

	public String getDS_CONSERVAZIONE() {
		return DS_CONSERVAZIONE;
	}

	public void setTP_TESTO_LETTER(String value) {
		this.TP_TESTO_LETTER = value;
		this.settaParametro(KeyParameter.XXXtp_testo_letter, value);
	}

	public String getTP_TESTO_LETTER() {
		return TP_TESTO_LETTER;
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
