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
package it.finsiel.sbn.util;

import it.finsiel.sbn.polo.factoring.profile.ProfilerCache;

import java.io.Serializable;

public class CodiciAttivita implements Serializable {

	private static final long serialVersionUID = 7681500175095336510L;
	/*
	public static String CERCA_DOCUMENTO_1002 												= "1002";
	public static String CERCA_ELEMENTO_AUTHORITY_1003 										= "1003";
	public static String CERCA_PROPOSTE_DI_CORREZIONE_1004 									= "1004";
	public static String CERCA_LOCALIZZAZIONI_DI_POSSEDUTO_1005 							= "1005";
	public static String CERCA_LOCALIZZAZIONI_PER_GESTIONE_1006 							= "1006";
	public static String LOCALIZZA_1007 													= "1007";
	public static String LOCALIZZA_PER_POSSEDUTO_1008 										= "1008";
	public static String LOCALIZZA_PER_GESTIONE_1009 										= "1009";
	public static String LOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1010 						= "1010";
	public static String DELOCALIZZA_PER_POSSEDUTO_1012 									= "1012";
	public static String DELOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1013 						= "1013";
	public static String DELOCALIZZA_PER_GESTIONE_1014 										= "1014";
	public static String CREA_DOCUMENTO_1016 												= "1016";
	public static String CREA_ELEMENTO_DI_AUTHORITY_1017 									= "1017";
	public static String CREA_PROPOSTA_CORREZIONE_1018 										= "1018";
	public static String MODIFICA_1019 														= "1019";
	public static String MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020 							= "1020";
	public static String MODIFICA_NATURA_DOCUMENTO_1021 									= "1021";
	public static String MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022 			= "1022";
	public static String MODIFICA_DOCUMENTO_1023 											= "1023";
	public static String FONDE_DOCUMENTI_1024 												= "1024";
	public static String CANCELLA_DOCUMENTO_1025 											= "1025";
	public static String MODIFICA_ELEMENTO_DI_AUTHORITY_1026 								= "1026";
	public static String FONDE_ELEMENTI_DI_AUTHORITY_1027 									= "1027";
	public static String CANCELLA_ELEMENTO_AUTHORITY_1028 									= "1028";
	public static String SCAMBIO_FORMA_1029 												= "1029";
	public static String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030 				= "1030";
	public static String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_PER_ALTRI_POLI_1031 	= "1031";
	public static String CHIEDI_ALLINEA_DOCUMENTI_1033 										= "1033";
	public static String CHIEDI_ALLINEA_ELEMENTI_DI_AUTHORITY_1034 							= "1034";
	public static String COMUNICA_ALLINEATI_1035 											= "1035";
	public static String IMPORTA_DOCUMENTI_1037 											= "1037";
	public static String IMPORTA_ELEMENTI_DI_AUTHORITY_1038 								= "1038";
	public static String ESPORTA_DOCUMENTI_1040 											= "1040";
	public static String ESPORTA_ELEMENTI_DI_AUTHORITY_1041 								= "1041";


	public static String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200 							= "1200";
	public static String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201 					= "1201";
	public static String FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202 							= "1202";
	*/
	/*
	public static String MODIFICA_TITOLO_UNIFORME_1261 										= "1261";
	public static String MODIFICA_TITOLO_UNIFORME_MUSICA_1262 								= "1262";
	public static String MODIFICA_AUTORE_1263 												= "1263";
	public static String MODIFICA_MARCA_1264 												= "1264";
	public static String MODIFICA_SOGGETTO_1265 											= "1265";
	public static String MODIFICA_CLASSE_1266 												= "1266";
	public static String MODIFICA_LUOGO_1267 												= "1267";
	public static String FONDE_TITOLO_UNIFORME_1268 										= "1268";
	public static String FONDE_TITOLO_UNIFORME_MUSICA_1269 									= "1269";
	public static String FONDE_AUTORE_1270 													= "1270";
	public static String FONDE_MARCA_1271 													= "1271";
	public static String FONDE_SOGGETTO_1272 												= "1272";
	public static String FONDE_CLASSE_1273 													= "1273";
	public static String FONDE_LUOGO_1274 													= "1274";
	*/
	public String CERCA_CLASSI_1247 												= "1247";
	public String CERCA_LUOGHI_1248 												= "1248";
	public String CERCA_TITOLI_UNIFORMI_1249 										= "1249";
	public String CERCA_TITOLI_UNIFORMI_MUSICA_1250 								= "1250";
	public String CERCA_AUTORI_1251 												= "1251";
	public String CERCA_MARCHE_1252 												= "1252";
	public String CERCA_SOGGETTI_1253 												= "1253";
	public String CREA_TITOLO_UNIFORME_1254 										= "1254";
	public String CREA_TITOLO_UNIFORME_MUSICA_1255 									= "1255";
	public String CREA_AUTORE_1256 													= "1256";
	public String CREA_MARCA_1257 													= "1257";
	public String CREA_SOGGETTO_1258 												= "1258";
	public String CREA_CLASSE_1259 													= "1259";
	public String CREA_LUOGO_1260 													= "1260";
	public String CANCELLA_TITOLO_UNIFORME_1275 									= "1275";
	public String CANCELLA_TITOLO_UNIFORME_MUSICA_1276 								= "1276";
	public String CANCELLA_AUTORE_1277 												= "1277";
	public String CANCELLA_MARCA_1278 												= "1278";
	public String CANCELLA_SOGGETTO_1279 											= "1279";
	public String CANCELLA_CLASSE_1280 												= "1280";
	public String CANCELLA_LUOGO_1281 												= "1281";
	public String CANCELLA_DESCRITTORE_1282 										= "1282";
	public String CERCA_DESCRITTORE_1283 											= "1283";
	public String CREA_DESCRITTORE_1284 											= "1284";


	public String ALLINEAMENTI_1032 												= "IA000";
	public String CHIEDI_ALLINEA_DOCUMENTI_1033 									= "IA001";
	public String CHIEDI_ALLINEA_ELEMENTI_DI_AUTHORITY_1034 						= "IA002";
	public String COMUNICA_ALLINEATI_1035 											= "IA003";

	public String CERCA_1001 														= "IC000";
	public String CERCA_DOCUMENTO_1002 												= "IC001";
	public String CERCA_ELEMENTO_AUTHORITY_1003 									= "IC002";
	public String CERCA_PROPOSTE_DI_CORREZIONE_1004 								= "IC003";
	public String CERCA_LOCALIZZAZIONI_DI_POSSEDUTO_1005 							= "IC004";
	public String CERCA_LOCALIZZAZIONI_PER_GESTIONE_1006 							= "IC005";

	public String CREA_1015			 												= "ICR00";
	public String CREA_DOCUMENTO_1016 												= "ICR01";
	public String CREA_ELEMENTO_DI_AUTHORITY_1017									= "ICR02";
	public String CREA_PROPOSTA_CORREZIONE_1018	 									= "ICR03";

	public String DELOCALIZZA_1011 													= "ID000";
	public String DELOCALIZZA_PER_POSSEDUTO_1012 									= "ID001";
	public String DELOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1013 					= "ID002";
	public String DELOCALIZZA_PER_GESTIONE_1014 									= "ID003";

	public String ESPORTA_1039 														= "IE000";
	public String ESPORTA_DOCUMENTI_1040 											= "IE001";
	public String ESPORTA_ELEMENTI_DI_AUTHORITY_1041								= "IE002";

	public String FONDE_ELEMENTI_DI_AUTHORITY_1027 									= "IF000";
	public String FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202 						= "IF001";
	public String FONDE_TITOLO_UNIFORME_MUSICA_1269 								= "IF002";
	public String FONDE_TITOLO_UNIFORME_1268 										= "IF003";
	public String FONDE_AUTORE_1270 												= "IF004";
	public String FONDE_LUOGO_1274 													= "IF005";
	public String FONDE_CLASSE_1273	 												= "IF006";
	public String FONDE_SOGGETTO_1272 												= "IF007";
	public String FONDE_MARCA_1271 													= "IF008";
	public String FONDE_THESAURO_1300 												= "IF009";

	public String IMPORTA_1036														= "II000";
	public String IMPORTA_DOCUMENTI_1037											= "II001";
	public String IMPORTA_ELEMENTI_DI_AUTHORITY_1038				 				= "II002";

	public String LOCALIZZA_1007 													= "IL000";
	public String LOCALIZZA_PER_POSSEDUTO_1008 										= "IL001";
	public String LOCALIZZA_PER_GESTIONE_1009 										= "IL002";
	public String LOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1010 						= "IL003";

	public String MODIFICA_1019 													= "IM000";
	public String MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020 							= "IM001";
	public String MODIFICA_NATURA_DOCUMENTO_1021 									= "IM002";
	public String MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022 			= "IM003";
	public String MODIFICA_DOCUMENTO_1023 											= "IM004";
	public String FONDE_DOCUMENTI_1024 												= "IM005";
	public String CANCELLA_DOCUMENTO_1025 											= "IM006";
	public String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200							= "IM007";
	public String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201					= "IM008";
	public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_PER_ALTRI_POLI_1031 = "IM010";
	//almaviva5_20140320 evolutive google3
	//public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030 				= "IM009";
	public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_GESTIONE						= "IM011";

	public String MODIFICA_ELEMENTO_DI_AUTHORITY_1026 								= "IMA00";
	public String CANCELLA_ELEMENTO_AUTHORITY_1028 									= "IMA01";
	public String MODIFICA_LUOGO_1267 												= "IMA02";
	public String MODIFICA_TITOLO_UNIFORME_MUSICA_1262 								= "IMA03";
	public String MODIFICA_AUTORE_1263			 									= "IMA04";
	public String MODIFICA_MARCA_1264 												= "IMA05";
	public String MODIFICA_CLASSE_1266 												= "IMA06";
	public String MODIFICA_SOGGETTO_1265 											= "IMA07";
	public String MODIFICA_TITOLO_UNIFORME_1261 									= "IMA08";
	public String SCAMBIO_FORMA_1029 												= "IMA09";
	public String MODIFICA_THESAURO_1301											= "IMA10";

	public static final CodiciAttivita getIstance()	{
		return ProfilerCache.getCodiciAttivitaInstance();
	}

}
