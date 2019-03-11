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
package it.iccu.sbn.ejb.home;

import it.iccu.sbn.ejb.remote.Menu;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface MenuHome extends EJBHome {
	public static final String COMP_NAME = "java:comp/env/ejb/Menu";
	public static final String JNDI_NAME = "Menu";

	public static final String MODULE_ACQUISIZIONI 		= "0001";
	public static final String MODULE_SEMANTICA 		= "0002";
	public static final String MODULE_DOCUMENTO_FISICO 	= "0003";
	public static final String MODULE_BIBLIOGRAFICA 	= "0004";
	public static final String MODULE_STAMPE 			= "0005";
	public static final String MODULE_POSSESSORI 		= "0006";
	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
	public static final String MODULE_PERIODICI 		= "0007";
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici
	public static final String MODULE_STATISTICHE 		= "0008";//rp

	public static final String FUNZ_STATISTICHE 		= "001";//rp

	public static final String FUNZ_ACQUISIZIONE_DEFAULT 						="";
	public static final String FUNZ_ACQUISIZIONE_ORDINE 						="001";
	public static final String FUNZ_ACQUISIZIONE_SUGGERIMENTO_ACQUISTO			="002";
	public static final String FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE		="003";
	public static final String FUNZ_ACQUISIZIONE_SUGGERIMENTO_BIBLIOTECARIO		="004";
	public static final String FUNZ_ACQUISIZIONE_GARA_D_ACQUISTO				="005";

	public static final String FUNZ_SEMANTICA_DEFAULT 			="";
	public static final String FUNZ_SEMANTICA_SOGGETTAZIONE		="001";
	//almaviva5_20090327 #2744
	public static final String FUNZ_SEMANTICA_CLASSIFICAZIONE	="002";
	//almaviva5_20090714 #2866
	public static final String FUNZ_SEMANTICA_THESAURO			="003";
	public static final String FUNZ_SEMANTICA_ABSTRACT			="004";

	public static final String FUNZ_DOCUMENTO_FISICO_DEFAULT 		="";
	public static final String FUNZ_DOCUMENTO_FISICO_INVENTARI 		="001";
	public static final String FUNZ_DOCUMENTO_FISICO_COLLOC_VELOCE	="002";


	public static final String FUNZ_POSSESSORI_DEFAULT				 				="";
	public static final String FUNZ_POSSESSORI_VARIAZIONE_DESCRIZIONE 				="001";
	public static final String FUNZ_POSSESSORI_INSERIMENTO_FORMA_RINVIO 	 		="002";
	public static final String FUNZ_POSSESSORI_VARIAZIONE_FORMA_RINVIO 	 			="003";
	public static final String FUNZ_POSSESSORI_VARIAZIONE_LEGAME 					="004";
	public static final String FUNZ_POSSESSORI_CANCELLAZIONE 	 					="005";
	//public static final String FUNZ_POSSESSORI_CANCELLAZIONE_FORMA_RINVIO 	 		="006";
	public static final String FUNZ_POSSESSORI_SCAMBIO_FORMA 						="007";
	public static final String FUNZ_POSSESSORI_INSERIMENTO_LEGAME_INVENTARIO 		="008";


	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
	public static final String FUNZ_PERIODICI_DEFAULT 						="";
	public static final String FUNZ_PERIODICI_DESCRIZIONE_FASCICOLI			="001";
	public static final String FUNZ_PERIODICI_GESTIONE_AMMINISTRATIVA		="002";
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici


	public static final String FUNZ_STAMPE_DEFAULT 			="";
	public static final String FUNZ_STAMPE_CATALOGRAFICHE 	="001";
	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	public static final String FUNZ_LEGA_EDITORE 			="002";
	public static final String FUNZ_GEST_LEGAMI_TIT_EDI		="003";

	public static final String FUNZ_BIBLIOGRAFICA_DEFAULT 								="";
//	public static final String FUNZ_BIBLIOGRAFICA_CONDIVIDI 							="001";
	public static final String FUNZ_BIBLIOGRAFICA_CATTURA_INFERIORI 					="002";
	public static final String FUNZ_BIBLIOGRAFICA_ALLINEA_RETICOLO						="003";
	public static final String FUNZ_BIBLIOGRAFICA_VARIAZIONE_DESCRIZIONE 				="004";
	public static final String FUNZ_BIBLIOGRAFICA_CORREZIONE_NOTA_ISBD 					="005";
	public static final String FUNZ_BIBLIOGRAFICA_VARIANTE_MUTILO						="006";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO 		="007";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_INFERIORE 	="008";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE 		="009";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_MARCA 		="010";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_LUOGO 		="011";
	public static final String FUNZ_BIBLIOGRAFICA_VARIAZIONE_LEGAME						="012";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LEGAME 					="013";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO 					="014";
	public static final String FUNZ_BIBLIOGRAFICA_PROPOSTA_CORREZIONE					="015";
	public static final String FUNZ_BIBLIOGRAFICA_CATTURA 								="016";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORI 			="017";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORE_MARCA 		="018";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO 				="019";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE 					="020";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO 			="021";
	public static final String FUNZ_BIBLIOGRAFICA_SCAMBIO_FORMA 						="022";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_MARCA_AUTORE 		="023";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_MARCA					="024";
	public static final String FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI				="025";
	public static final String FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LUOGO					="026";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLO 		="027";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_AUTORE 		="028";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_LUOGHI		 		="029";
	public static final String FUNZ_BIBLIOGRAFICA_CATTURA_A_SUGGERIMENTO		 		="030";
	public static final String FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO				 		="031";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_VOLUMI_INFERIORI			="032";
	public static final String FUNZ_BIBLIOGRAFICA_DELOCALIZZA							="033";
//	public static final String FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO 			="034";
	public static final String FUNZ_BIBLIOGRAFICA_SCAMBIA_LEGAME_AUTORE		 			="035";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO			 		="036";
	public static final String FUNZ_BIBLIOGRAFICA_ALLINEA_AUTORE 						="037";
	public static final String FUNZ_BIBLIOGRAFICA_ALLINEA_MARCA 						="038";
	public static final String FUNZ_BIBLIOGRAFICA_ALLINEA_TITOLOUNIFORME				="039";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE			="040";
	public static final String FUNZ_BIBLIOGRAFICA_ALLINEA_LUOGO							="041";
	public static final String FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION 				="042";
	public static final String FUNZ_BIBLIOGRAFICA_SCONDIVIDI			 				="043";
	public static final String FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION	="044";
	public static final String FUNZ_BIBLIOGRAFICA_FONDI_ON_LINE							="045";
	public static final String FUNZ_BIBLIOGRAFICA_CAMBIA_NATURA_BA_AB					="046";
	public static final String FUNZ_BIBLIOGRAFICA_REINVIO_IN_INDICE						="047";
	public static final String FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI			="048";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA	="0049";

	public static final String FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_GESTIONE		="050";
	public static final String FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO		="051";
	public static final String FUNZ_BIBLIOGRAFICA_TRASCINA_LEGAME_AUTORE				="052";


	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_RINVIO			="053";
	public static final String FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLOUNIFORME	="054";

	public static final String FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO_TIT_ANALITICI				 		="055";

	public Menu create() throws CreateException, RemoteException;
}
