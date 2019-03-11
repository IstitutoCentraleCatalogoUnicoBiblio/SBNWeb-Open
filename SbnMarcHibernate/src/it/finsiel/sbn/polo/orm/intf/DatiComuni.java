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
package it.finsiel.sbn.polo.orm.intf;

public interface DatiComuni {

	void setBID(String value);

	String getS105_TP_TESTO_LETTERARIO();

	void setS105_TP_TESTO_LETTERARIO(String value);

	String getS140_TP_TESTO_LETTERARIO();

	void setS140_TP_TESTO_LETTERARIO(String value);

	String getS181_TP_FORMA_CONTENUTO_1();

	void setS181_TP_FORMA_CONTENUTO_1(String value);

	String getS181_CD_TIPO_CONTENUTO_1();

	void setS181_CD_TIPO_CONTENUTO_1(String value);

	String getS181_CD_MOVIMENTO_1();

	void setS181_CD_MOVIMENTO_1(String value);

	String getS181_CD_DIMENSIONE_1();

	void setS181_CD_DIMENSIONE_1(String value);

	String getS181_CD_SENSORIALE_1_1();

	void setS181_CD_SENSORIALE_1_1(String value);

	String getS181_CD_SENSORIALE_2_1();

	void setS181_CD_SENSORIALE_2_1(String value);

	String getS181_CD_SENSORIALE_3_1();

	void setS181_CD_SENSORIALE_3_1(String value);

	String getS181_TP_FORMA_CONTENUTO_2();

	void setS181_TP_FORMA_CONTENUTO_2(String value);

	String getS181_CD_TIPO_CONTENUTO_2();

	void setS181_CD_TIPO_CONTENUTO_2(String value);

	String getS181_CD_MOVIMENTO_2();

	void setS181_CD_MOVIMENTO_2(String value);

	String getS181_CD_DIMENSIONE_2();

	void setS181_CD_DIMENSIONE_2(String value);

	String getS181_CD_SENSORIALE_1_2();

	void setS181_CD_SENSORIALE_1_2(String value);

	String getS181_CD_SENSORIALE_2_2();

	void setS181_CD_SENSORIALE_2_2(String value);

	String getS181_CD_SENSORIALE_3_2();

	void setS181_CD_SENSORIALE_3_2(String value);

	String getS182_TP_MEDIAZIONE_1();

	void setS182_TP_MEDIAZIONE_1(String value);

	String getS182_TP_MEDIAZIONE_2();

	void setS182_TP_MEDIAZIONE_2(String value);

	String getS125_INDICATORE_TESTO();

	void setS125_INDICATORE_TESTO(String value);

	String getS183_TP_SUPPORTO_1();

	void setS183_TP_SUPPORTO_1(String value);

	String getS183_TP_SUPPORTO_2();

	void setS183_TP_SUPPORTO_2(String value);

	String getS210_IND_PUBBLICATO();

	void setS210_IND_PUBBLICATO(String value);

}
