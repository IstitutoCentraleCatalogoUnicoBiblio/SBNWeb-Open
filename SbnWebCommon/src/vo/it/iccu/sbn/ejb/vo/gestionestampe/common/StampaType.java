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
package it.iccu.sbn.ejb.vo.gestionestampe.common;

public enum StampaType {

	//Documento Fisico
	STAMPA_REGISTRO_TOPOGRAFICO										(AreaType.GESTIONE_DOCUMENTO_FISICO),
	STAMPA_REGISTRO_CONSERVAZIONE									(AreaType.GESTIONE_DOCUMENTO_FISICO),
	STAMPA_ETICHETTE												(AreaType.GESTIONE_DOCUMENTO_FISICO),
	STAMPA_REGISTRO_INGRESSO										(AreaType.GESTIONE_DOCUMENTO_FISICO),
	STAMPA_STATISTICHE_REGISTRO_INGRESSO							(AreaType.GESTIONE_DOCUMENTO_FISICO),

	//Servizi
	STAMPA_LISTA_UTENTI												(AreaType.GESTIONE_SERVIZI),
	STAMPA_RICHIESTA												(AreaType.GESTIONE_SERVIZI),
	STAMPA_RICHIESTA_AVANZA											(AreaType.GESTIONE_SERVIZI),

	//Acquisizioni
	STAMPA_BUONI_ORDINE												(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_LISTA_FORNITORI											(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_LISTA_ORDINI												(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_BOLLETTARIO												(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_FATTURA													(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_COMUNICAZIONE											(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_SPESE													(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_SUGGERIMENTI_BIBLIOTECARIO								(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_SUGGERIMENTI_LETTORE										(AreaType.GESTIONE_ACQUISIZIONI),

	//almaviva5_20121218 evolutive google
	STAMPA_ORDINE_RILEGATURA										(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_PATRON_CARD												(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_CART_ROUTING_SHEET										(AreaType.GESTIONE_ACQUISIZIONI),
	STAMPA_MODULO_PRELIEVO											(AreaType.GESTIONE_ACQUISIZIONI),

	//Amministrazione
	STAMPA_LISTA_BIBLIOTECHE										(AreaType.GESTIONE_SISTEMA),
	STAMPA_LISTA_BIBLIOTECARI										(AreaType.GESTIONE_SISTEMA),

	//Semantica
	STAMPA_SISTEMA_CLASSIFICAZIONE									(AreaType.GESTIONE_SEMANTICA),
	STAMPA_TERMINI_THESAURO											(AreaType.GESTIONE_SEMANTICA);

	private final AreaType area;

	public enum AreaType {
		GESTIONE_BIBLIOGRAFICA,
		GESTIONE_SEMANTICA,
		GESTIONE_DOCUMENTO_FISICO,
		GESTIONE_SERVIZI,
		GESTIONE_ACQUISIZIONI,
		GESTIONE_SISTEMA;
	}

	private StampaType(AreaType area) {
		this.area = area;
	}

	public AreaType getArea() {
		return area;
	}
}
