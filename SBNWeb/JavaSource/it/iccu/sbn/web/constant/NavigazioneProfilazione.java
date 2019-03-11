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

public class NavigazioneProfilazione {

	public static final int LIVAUT_SEARCH_ID = ("MENU" + "profilo.polo.parametri.livello").hashCode();
	public static final int FL_ABIL_SEARCH_ID = ("RADIO" + "profilo.polo.parametri.abil").hashCode();
	public static final int FL_ABIL_LEG_SEARCH_ID = ("RADIO" + "profilo.polo.parametri.abil_legame").hashCode();
	public static final int LEG_AUT_SEARCH_ID = ("RADIO" + "profilo.polo.parametri.leg_auth").hashCode();
	public static final int ABIL_FORZ_SEARCH_ID = ("RADIO" + "profilo.polo.parametri.abil_forzat").hashCode();
	public static final int SOLO_LOC_SEARCH_ID = ("RADIO" + "profilo.polo.parametri.sololocale").hashCode();

	public static final String PARAMETRI_AUTHORITY = "auth";
	public static final String PARAMETRI_SEMANTICA = "sem";
	public static final String PARAMETRI_MATERIALI = "mat";
	public static final String NOME_UTENTE = "nomeUtente";
	public static final String USERNAME = "username";
	public static final String BIBLIOTECA = "biblioteca";
	public static final String ID_UTENTE = "utente";
	public static final String COD_BIBLIOTECA = "codBib";

	//almaviva5_20140702
	public static final String AUTHORITY_OK = "profilo.user.auth.ok";
	public static final String METERIALI_OK = "profilo.user.mat.ok";

}
