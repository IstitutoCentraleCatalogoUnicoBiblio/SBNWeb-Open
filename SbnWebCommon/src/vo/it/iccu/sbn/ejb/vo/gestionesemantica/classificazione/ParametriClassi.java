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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParametriClassi extends SerializableVO {

	private static final long serialVersionUID = -6149946217339635958L;
	private static final String PARAMETRI_CLASSI = "gs.params.CLA.req";
	private Map<ClassiParamType, Serializable> params;

	public static ParametriClassi retrieve(HttpServletRequest request) {
		return (ParametriClassi) request.getAttribute(PARAMETRI_CLASSI);
	}

	public static void send(HttpServletRequest request, ParametriClassi parametri) {
		request.setAttribute(PARAMETRI_CLASSI, parametri);
	}

	public enum ModalitaCercaType {
		CERCA,
		CERCA_PER_COLLOCAZIONE,
		CREA_LEGAME_TITOLO,
		TRASCINA_TITOLI;
	}

	public enum ModalitaGestioneType {
		ESAMINA,
		GESTIONE,
		CREA,
		CREA_PER_LEGAME_TITOLO,
		CREA_PER_TRASCINA_TITOLI;
	}

	public enum ModalitaLegameTitoloTermineType {
		CREA,
		MODIFICA;
	}

	public enum ClassiParamType {

		MODALITA_CERCA_CLASSE,
		MODALITA_GESTIONE_CLASSE,
		MODALITA_LEGAME_TITOLO_CLASSE,

		COD_POLO,
		COD_BIB,
		LIVELLO_POLO,
		SISTEMA_CLASSIFICAZIONE,
		EDIZIONE_DEWEY,
		DATI_BIBLIOGRAFICI,
		PARAMETRI_RICERCA,
		OUTPUT_SINTETICA,
		DATI_LEGAME_TERMINI,
		DATI_LEGAME_TITOLO,
		DETTAGLIO_OGGETTO,
		SIF_ATTIVAZIONE;
	}

	public ParametriClassi() {
		super();
		this.params = new HashMap<ClassiParamType, Serializable>();
	}

	public Serializable put(ClassiParamType type,
			Serializable value) {
		return this.params.put(type, value);
	}

	public Serializable get(ClassiParamType type) {
		return this.params.get(type);
	}

	public Serializable remove(ClassiParamType type) {
		return this.params.remove(type);
	}

	public void clear() {
		this.params.clear();
	}

	public Enumeration<ClassiParamType> getNames() {
		return Collections.enumeration(this.params.keySet());
	}

}
