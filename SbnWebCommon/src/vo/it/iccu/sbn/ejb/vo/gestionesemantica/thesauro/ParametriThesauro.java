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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParametriThesauro extends SerializableVO {

	private static final long serialVersionUID = -6149946217339635958L;
	private static final String PARAMETRI_THESAURO = "gs.params.thes.req";
	private Map<ParamType, Serializable> params;

	public static ParametriThesauro retrieve(HttpServletRequest request) {
		return (ParametriThesauro) request.getAttribute(PARAMETRI_THESAURO);
	}

	public static void send(HttpServletRequest request, ParametriThesauro parametri) {
		request.setAttribute(PARAMETRI_THESAURO, parametri);
	}

	public enum TipoOperazione {
		CREA_LEGAME_TERMINE_CLASSE,
		MODIFICA_LEGAME_TERMINE_CLASSE,
		ELIMINA_LEGAME_TERMINE_CLASSE,
		RANKING;
	}

	public enum ModalitaCercaType {
		CERCA,
		CREA_LEGAME_TERMINI,
		CREA_LEGAME_TITOLO,
		TRASCINA_TITOLI;
	}

	public enum ModalitaGestioneType {
		ESAMINA,
		GESTIONE,
		CANCELLA,
		CREA,
		CREA_PER_LEGAME_TERMINI,
		CREA_PER_LEGAME_TITOLO,
		CREA_PER_TRASCINA_TITOLI;
	}

	public enum ModalitaLegameTerminiType {
		CREA,
		MODIFICA,
		CANCELLA;
	}

	public enum ModalitaLegameTitoloTermineType {
		CREA,
		MODIFICA,
		CANCELLA;
	}

	public enum ModalitaAnaliticaType {
		CONFERMA_SCAMBIA_FORMA;
	}

	public enum ParamType {

		MODALITA_CERCA_TERMINE						(ModalitaCercaType.class),
		MODALITA_GESTIONE_TERMINE					(ModalitaGestioneType.class),
		MODALITA_LEGAME_TERMINI						(ModalitaLegameTerminiType.class),
		MODALITA_LEGAME_TITOLO_TERMINE				(ModalitaLegameTitoloTermineType.class),
		MODALITA_ANALITICA							(ModalitaAnaliticaType.class),

		COD_POLO									(String.class),
		COD_BIB										(String.class),
		LIVELLO_POLO								(Boolean.class),
		CODICE_THESAURO								(String.class),
		TESTO_THESAURO	 							(String.class),
		DATI_BIBLIOGRAFICI							(AreaDatiPassBiblioSemanticaVO.class),
		PARAMETRI_RICERCA 							(ThRicercaComuneVO.class),
		OUTPUT_SINTETICA							(RicercaThesauroListaVO.class),
		ANALITICA									(TreeElementViewSoggetti.class),
		LISTA_DID_SELEZIONATI						(String[].class),
		DATI_LEGAME_TERMINI							(DatiLegameTerminiVO.class),
		DATI_LEGAME_TITOLO							(DatiLegameTitoloTermineVO.class),
		DETTAGLIO_OGGETTO							(DettaglioTermineThesauroVO.class),
		NODO_PARTENZA_LEGAME						(TreeElementViewSoggetti.class),
		CATALOGAZIONE_TITOLO_T005 					(String.class),
		DATI_FUSIONE_TERMINI						(DatiFusioneTerminiVO.class),
		ID_NODO_SELEZIONATO							(Integer.class),
		FORZA_TRASCINAMENTO							(Boolean.class),
		DID_TARGET_FUSIONE							(String.class),
		OGGETTO_RIFERIMENTO							(OggettoRiferimentoVO.class),

		//almaviva5_20111020 evolutive CFI
		PARAMETRI_RICERCA_CLASSI					(RicercaClassiTermineVO.class),
		CLASSI_COLLEGATE							(ClassiCollegateTermineVO.class),
		TIPO_OPERAZIONE_LEGAME_CLASSE				(TipoOperazione.class),
		DETTAGLIO_LEGAME_CLASSE						(DettaglioClasseVO.class);

		private final Class<?> paramClass;

		private ParamType(Class<?> paramClass) {
			this.paramClass = paramClass;
		}

	}

	public ParametriThesauro() {
		super();
		this.params = new HashMap<ParamType, Serializable>();
	}

	public Serializable put(ParamType type, Serializable value)
		throws ValidationException {

		if (value == null)
			return null;
		if (type.paramClass != null && !type.paramClass.isInstance(value))
			throw new ValidationException("Parametro Thesauro non previsto " + type + ": " + value);
		return this.params.put(type, value);
	}

	public Serializable get(ParamType type) {
		return this.params.get(type);
	}

	public Serializable remove(ParamType type) {
		return this.params.remove(type);
	}

	public void clear() {
		this.params.clear();
	}

	public Enumeration<ParamType> getNames() {
		return Collections.enumeration(this.params.keySet());
	}

}
