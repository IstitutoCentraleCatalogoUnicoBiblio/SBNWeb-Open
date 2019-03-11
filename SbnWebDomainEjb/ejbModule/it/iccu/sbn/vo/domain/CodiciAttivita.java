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
package it.iccu.sbn.vo.domain;

import it.iccu.sbn.ejb.exception.InfrastructureException;

import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CodiciAttivita implements Serializable {

	private static final long serialVersionUID = -5158121518912286267L;

	private static CodiciAttivita instance = null;

	public String CERCA_CLASSI_1247 												= "1247";
	public String CERCA_LUOGHI_1248 												= "1248";
	public String CERCA_TITOLI_UNIFORMI_1249 										= "1249";
	public String CERCA_TITOLI_UNIFORMI_MUSICA_1250									= "1250";
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
	public String CANCELLA_DESCRITTORE_1282		 									= "1282";
	public String CERCA_DESCRITTORE_1283 											= "1283";
	public String CREA_DESCRITTORE_1284 											= "1284";

	public String ALLINEAMENTI_1032 												= "IA000";
	public String CHIEDI_ALLINEA_DOCUMENTI_1033 									= "IA001";
	public String CHIEDI_ALLINEA_ELEMENTI_DI_AUTHORITY_1034 						= "IA002";
	public String COMUNICA_ALLINEATI_1035 											= "IA003";

	public String CATTURA_MASSIVA		 											= "IA004";
	public String ALLINEAMENTO_REPERTORI 											= "IA005";
	//almaviva5_20140204 evolutive google3
	public String LOCALIZZAZIONE_MASSIVA 											= "IA006";

	//almaviva5_20171117 salva file allineamento
	public String ALLINEAMENTI_SALVA_XML_INDICE										= "IA007";

	public String CERCA_1001 														= "IC000";
	public String CERCA_DOCUMENTO_1002 												= "IC001";
	public String CERCA_ELEMENTO_AUTHORITY_1003 									= "IC002";
	public String CERCA_PROPOSTE_DI_CORREZIONE_1004 								= "IC003";
	public String CERCA_LOCALIZZAZIONI_DI_POSSEDUTO_1005 							= "IC004";
	public String CERCA_LOCALIZZAZIONI_PER_GESTIONE_1006 							= "IC005";

	public String CREA_1015 														= "ICR00";
	public String CREA_DOCUMENTO_1016 												= "ICR01";
	public String CREA_ELEMENTO_DI_AUTHORITY_1017 									= "ICR02";
	public String CREA_PROPOSTA_CORREZIONE_1018 									= "ICR03";

	public String DELOCALIZZA_1011 													= "ID000";
	public String DELOCALIZZA_PER_POSSEDUTO_1012 									= "ID001";
	public String DELOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1013 					= "ID002";
	public String DELOCALIZZA_PER_GESTIONE_1014 									= "ID003";

	public String ESPORTA_1039 														= "IE000";
	public String ESPORTA_DOCUMENTI_1040 											= "IE001";
	public String ESPORTA_ELEMENTI_DI_AUTHORITY_1041 								= "IE002";
	public String ESPORTA_IDENTIFICATIVI_DOCUMENTO	 								= "IE003";
	public String ESPORTA_IDENTIFICATIVI_AUTHORITY	 								= "IE004";

	public String FONDE_ELEMENTI_DI_AUTHORITY_1027 									= "IF000";
	public String FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202 						= "IF001";
	public String FONDE_TITOLO_UNIFORME_MUSICA_1269 								= "IF002";
	public String FONDE_TITOLO_UNIFORME_1268 										= "IF003";
	public String FONDE_AUTORE_1270 												= "IF004";
	public String FONDE_LUOGO_1274 													= "IF005";
	public String FONDE_CLASSE_1273 												= "IF006";
	public String FONDE_SOGGETTO_1272 												= "IF007";
	public String FONDE_MARCA_1271 													= "IF008";
	public String FONDE_THESAURO_1300 												= "IF009";

	public String FUSIONE_MASSIVA		 											= "IF010";

	public String IMPORTA_1036 														= "II000";
	public String IMPORTA_DOCUMENTI_1037 											= "II001";
	public String IMPORTA_ELEMENTI_DI_AUTHORITY_1038 								= "II002";
	public String IMPORTA_FILE_LISTE_CONFRONTO		 								= "II100";

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
	public String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200 							= "IM007";
	public String FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201 					= "IM008";
	public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_PER_ALTRI_POLI_1031	= "IM010";
	//almaviva5_20140320 evolutive google3
	//public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030				= "IM009";
	public String MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_GESTIONE						= "IM011";

	public String MODIFICA_ELEMENTO_DI_AUTHORITY_1026								= "IMA00";
	public String CANCELLA_ELEMENTO_AUTHORITY_1028									= "IMA01";
	public String MODIFICA_LUOGO_1267 												= "IMA02";
	public String MODIFICA_TITOLO_UNIFORME_MUSICA_1262 								= "IMA03";
	public String MODIFICA_AUTORE_1263 												= "IMA04";
	public String MODIFICA_MARCA_1264 												= "IMA05";
	public String MODIFICA_CLASSE_1266 												= "IMA06";
	public String MODIFICA_SOGGETTO_1265 											= "IMA07";
	public String MODIFICA_TITOLO_UNIFORME_1261 									= "IMA08";
	public String SCAMBIO_FORMA_1029 												= "IMA09";
	public String MODIFICA_THESAURO_1301 											= "IMA10";

	public String GESTIONE_DOCUMENTO_FISICO 										= "C0000";
	public String GDF_SEZIONI_DI_COLLOCAZIONE 										= "C1000";
	public String GDF_SERIE_INVENTARIALE 											= "C2000";
	public String GDF_STAMPA_REGISTRO_TOPOGRAFICO 									= "C3000";
	public String GDF_ESAME_COLLOCAZIONI 											= "C4000";
	public String GDF_DATI_DI_INVENTARIO 											= "C5000";
	public String GDF_ESAME_POSSEDUTO 												= "C5010";
	public String GDF_STAMPA_SCHEDE_CATALOGRAFICHE 									= "C5020";
	public String GDF_STAMPA_CATALOGHI			 									= "ZG200";
	public String GDF_RICERCA_CLASSI_SOGGETTI_PER_COLLOCAZIONE 						= "C5100";
	public String GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE 								= "C6000";
	public String GDF_SPOSTAMENTO_COLLOCAZIONI 										= "C7000";
	public String GDF_CODICI_DI_PROVENIENZA 										= "C9100";
	public String GDF_SCARICO_INVENTARIALE 											= "C9200";
	public String GDF_POSSESSORI 													= "C9400";
	public String GDF_ETICHETTE 													= "CD000";
	public String GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI 						= "CE000";
	public String GDF_PARAMETRI_BIBLIOTECA											= "C9300";
	public String GDF_BOLLETTINO_NUOVE_ACCESSIONI									= "CB000";
	public String GDF_RIC_SOLO_DI_BIBLIOTECA										= "C0000";
	public String GDF_STRUMENTI_DI_CONTROLLO_SUL_PATRIMONIO							= "CS000";
	public String GDF_MODELLO_PRELIEVO												= "CMP00";

	//almaviva5_20090907
	public String GDF_GESTIONE_MODELLI_ETICHETTE									= "CC000";

	public String GESTIONE_ACQUISIZIONI 											= "A0000";
	public String GA_ORDINI 														= "A1000";
	public String GA_INTERROGAZIONE_ORDINI 											= "A1100";
	public String GA_GESTIONE_ORDINI 												= "A1200";
	public String GA_GESTIONE_ORDINI_ACQUISTO_VISIONE_TRATTENUTA 					= "A1210";
	public String GA_GESTIONE_ORDINI_DEPOSITO_LEGALE_DONO 							= "A1220";
	public String GA_GESTIONE_ORDINI_SCAMBIO 										= "A1230";
	public String GA_GESTIONE_ORDINI_RILEGATURA 									= "A1240";
	public String GA_CANCELLAZIONE_ORDINE 											= "A1300";
	public String GA_ACCESSIONAMENTO_ORDINI 										= "A1400";
	public String GA_GARE_ACQUISTO 													= "A2000";
	public String GA_INTERROGAZIONE_GARE_ACQUISTO 									= "A2100";
	public String GA_GESTIONE_GARE_ACQUISTO 										= "A2200";
	public String GA_BILANCIO 														= "A3000";
	public String GA_INTERROGAZIONE_BILANCIO 										= "A3100";
	public String GA_GESTIONE_BILANCIO 												= "A3200";
	public String GA_FATTURE 														= "A4000";
	public String GA_INTERROGAZIONE_FATTURE 										= "A4100";
	public String GA_GESTIONE_FATTURE 												= "A4200";
	public String GA_COMUNICAZIONI 													= "A5000";
	public String GA_INTERROGAZIONE_COMUNICAZIONI 									= "A5100";
	public String GA_GESTIONE_COMUNICAZIONI 										= "A5200";
	public String GA_SUGGERIMENTO_LETTORE 											= "A6000";
	public String GA_INTERROGAZIONE_SUGGERIMENTO_LETTORE 							= "A6100";
	public String GA_GESTIONE_SUGGERIMENTO_LETTORE 									= "A6200";
	public String GA_OFFERTE_FORNITORE 												= "A7000";
	public String GA_INTERROGAZIONE_OFFERTE_FORNITORE 								= "A7100";
	public String GA_GESTIONE_OFFERTE_FORNITORE 									= "A7200";
	public String GA_SEZIONE_ACQUISIZIONI 											= "A8000";
	public String GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI 							= "A8100";
	public String GA_GESTIONE_SEZIONE_ACQUISIZIONI 									= "A8200";
	public String GA_SUGGERIMENTO_BIBLIOTECARIO 									= "A9000";
	public String GA_INTERROGAZIONE_SUGGERIMENTO_BIBLIOTECARIO 						= "A9100";
	public String GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO 						= "A9200";
	public String GA_ACCETTA_RIFIUTA_SUGGERIMENTO_BIBLIOTECARIO						= "A9300";

	public String GA_PROFILI_DI_ACQUISTO 											= "AA000";
	public String GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO 							= "AA100";
	public String GA_GESTIONE_PROFILI_DI_ACQUISTO 									= "AA200";
	public String GA_CAMBI 															= "AB000";
	public String GA_INTERROGAZIONE_CAMBI 											= "AB100";
	public String GA_GESTIONE_CAMBI 												= "AB200";
	public String GA_STAMPA_REGISTRO_DI_INGRESSO 									= "AC100";
	public String GA_STAMPA_ORDINE 													= "AC200";
	public String GA_STAMPA_BUONI_DI_CARICO 										= "AC300";
	public String GA_BUONI_ORDINE 													= "AF000";
	public String GA_INTERROGAZIONE_BUONI_ORDINE 									= "AF100";
	public String GA_GESTIONE_BUONI_ORDINE 											= "AF200";
	public String GA_PARAMETRI_BIBLIOTECA 											= "AE000";
	public String GA_FORNITORI 														= "AR100";
	public String GA_INTERROGAZIONE_FORNITORI 										= "AR110";
	public String GA_GESTIONE_FORNITORI 											= "AR120";
	public String GA_STAMPA_FORNITORI_DI_POLO 										= "AR960";
	public String GA_STAMPA_FORNITORI_DI_BIBLIOTECA 								= "AR970";
	public String GA_STAMPA_RIPARTIZIONE_SPESE		 								= "AZ100";
	public String GA_STAMPA_BOLLETTARIO				 								= "AZ200";
	public String GA_STAMPA_FATTURA					 								= "AZ300";
	public String GA_STAMPA_COMUNCAZIONE			 								= "AZ400";
	public String GA_STAMPA_SUGGERIMENTI_BIBL			 							= "AZ500";
	public String GA_STAMPA_SUGGERIMENTI_LETT			 							= "AZ600";
	public String GA_STAMPA_SHIPPING_MANIFEST			 							= "AZ700";
	public String GA_OPERAZIONI_SU_ORDINE				 							= "AZ800";
	public String GA_STATISTICHE_TEMPI  				 							= "AZ900";

	public String AMMINISTRAZIONE													= "F0000";
	public String ANAGRAFICA_UTENTE													= "FU000";
	public String INTERROGAZIONE_ANAGRAFICA_UTENTE									= "FUIA0";
	public String INTERROGAZIONE_ABILITAZIONI_UTENTE								= "FUIF0";
	public String GESTIONE_ANAGRAFICA_UTENTE										= "FUGA0";
	public String GESTIONE_ABILITAZIONI_UTENTE										= "FUGF0";
	public String ANAGRAFICA_BIBLIOTECA												= "FB000";
	public String INTERROGAZIONE_ANAGRAFICA_BIBLIOTECA								= "FBIA0";
	public String INTERROGAZIONE_ABILITAZIONI_BIBLIOTECA							= "FBIF0";
	public String GESTIONE_ANAGRAFICA_BIBLIOTECA									= "FBGA0";
	public String GESTIONE_ABILITAZIONI_BIBLIOTECA									= "FBGF0";
	public String GESTIONE_DEFAULT_BIBLIOTECA										= "FBGD0";
	public String GESTIONE_DEFAULT_UTENTE											= "FUGD0";
	public String GESTIONE_SISTEMI_METROPOLITANI									= "LCSM0";
	public String GESTIONE_CENTRI_SISTEMA											= "FCS00";
	public String PROFILAZIONE_POLO													= "FP000";

	public String CODICI_VALIDAZIONE												= "FCV00";
	public String INTERROGAZIONE_CODICI_VALIDAZIONE									= "FCVI0";
	public String GESTIONE_CODICI_VALIDAZIONE										= "FCVG0";

	public String SERVIZI															= "L0000";
	public String SERVIZI_STAMPA_UTENTE												= "LR940";
	public String SERVIZI_STAMPA_RICHIESTA											= "LR339";
	public String SERVIZI_AGGIORNA_DIRITTI_AUTORIZZAZIONE							= "LD002";
	public String SERVIZI_SOLLECITI													= "LS337";
	public String SERVIZI_ARCHIVIAZIONE_MOVLOC										= "LS331";
	public String STAMPA_SERVIZI_CORRENTI											= "LS334";
	public String STAMPA_SERVIZI_STORICO											= "LS335";
	//almaviva5_20170518
	public String SRV_RIFIUTO_PRENOTAZIONI_SCADUTE									= "LS340";

	//almaviva5_20110103
	public String SRV_CONFIGURAZIONE_ESAME											= "LC001";
	public String SRV_CONFIGURAZIONE_GESTIONE										= "LC002";
	public String SRV_EROGAZIONE													= "LE001";
	public String SRV_GESTIONE_ANAGRAFE_UTENTE										= "LR001";
	public String SRV_DIRITTI_UTENTE												= "LD001";

	//almaviva5_20120116
	public String SRV_IMPORTA_UTENTI												= "LRI01";


	public String PROCEDURE_DIFFERITE												= "B0000";

	public String STAMPE															= "Z0000";
	public String STAMPA_SOGGETTARIO												= "ZS431";
	public String STAMPA_DESCRITTORI												= "ZS432";
	public String STAMPA_SISTEMA_CLASSIFICAZIONE									= "ZS435";
	public String STAMPA_THESAURO_POLO												= "ZS437";

	public String GESTIONE_SEMANTICA												= "S0000";
	public String CANCELLAZIONE_SOGGETTI_INUTILIZZATI								= "SSCSI";
	public String CANCELLAZIONE_DESCRITTORI_INUTILIZZATI							= "SSCDI";

	public String PERIODICI															= "P0000";
	public String DESCRIZIONE_FASCICOLI												= "P0001";
	public String AMMINISTRAZIONE_FASCICOLI											= "P0002";
	public String STAMPA_FASCICOLI													= "PZ001";

	public String STATISTICHE			  				 							= "T0001";

	public String CATALOGO_DI_POLO			  				 						= "CP000";
	public String CATALOGO_DI_BIB			  				 						= "CBI00";

	//gestione editori (evolutive BAT)
	public String PRODUZIONE_EDITORIALE		  				 						= "IR000";
	public String GESTIONE_EDITORI			  				 						= "IR001";
	public String STAMPA_TITOLI_EDITORI		  				 						= "IRZ01";

	//almaviva5_20130916 evolutive google2
	public String IMPORTA_URI_COPIA_DIGITALE				 						= "II150";

	//almaviva5_20161004 evolutiva oclc
	public String IMPORTA_RELAZIONI_BID_ALTRO_ID									= "II160";

	private static final ReadWriteLock lock = new ReentrantReadWriteLock();


	public static final CodiciAttivita getIstance() {
		lock.readLock().lock();
		try {
			return instance;
		} finally {
			lock.readLock().unlock();
		}

	}

	public static final void save(Object codiciAttivita) throws InfrastructureException {

		if (codiciAttivita instanceof CodiciAttivita) {

			lock.writeLock().lock();
			try {
				instance = (CodiciAttivita) codiciAttivita;
			} finally {
				lock.writeLock().unlock();
			}

		} else
			throw new InfrastructureException("Oggetto Codici Attivita non coerente");
	}

}
