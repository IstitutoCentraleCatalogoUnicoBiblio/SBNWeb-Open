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
package it.iccu.sbn.web.constant;

public class ServiziConstant {
	public final static String NUMBER_FORMAT_CONVERTER="###,###,###,##0.00";
	public final static String VALORE_IMPORTO_NUMBER_FORMAT_CONVERTER="###,###,##0.00";

	public final static double MAX_VALORE = 999999999.999;

	// 22 marzo 2010: aggiunto stato S (documento disponibile, movimento non ancora completato)
	public static final String[] STATI_MOVIMENTO_NON_ATTIVI = new String[] {"C", "E", "P", "S" };

	//almaviva5_20110113 #3988 #4938
	public static final String MSG_INVENTARIO_CANCELLATO = "Titolo non trovato/Inventario cancellato";
	//almaviva5_20181107 #6774
	public static final String MSG_UTENTE_NON_TROVATO = "Utente non trovato/cancellato";

	public static String[] COPPIA_NATURA = { "S99", "S9", "S8", "S26",
		"S25", "S24", "S22", "S18", "S16", "S15", "S14", "S13",
		"S12", "S11", "S10", "M8", "M26", "M25", "M24", "M22", "M18",
		"M16", "M15", "M14", "M13",	"M12", "M11", "W8", "W26","W25",
		"W24", "W22", "W18", "W16", "W15", "W14", "W13", "W12", "W11",
		"M27", "W27", "S27", "N27" };

	//almaviva5_20110208
	public static final String FAMIGLIA_SRV_PRESTITO = "PR";
	public static final String FAMIGLIA_SRV_CONSULTAZIONE = "CO";
	public static final String FAMIGLIA_SRV_RIPRODUZIONE = "RP";

	public static final String ANNO_PERIODICO_99 = "99";

	public static final String RICHIESTE = "RICHIESTE";
	public static final String SOLLECITI = "SOLLECITI";
	public static final String BLOCCO_RICHIESTE = "BLOCCO_RICHIESTE";

	public static int NUM_PAGINE_ERROR = - 0xFF;
}
