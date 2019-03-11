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
package it.iccu.sbn.ejb.vo.periodici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParametriPeriodici extends SerializableVO {

	private static final long serialVersionUID = -201735179976656046L;
	private static final String PARAMETRI_PERIODICI = "per.params.periodici.req";
	private Map<ParamType, Serializable> params;

	public static ParametriPeriodici retrieve(HttpServletRequest request) {
		return (ParametriPeriodici) request.getAttribute(PARAMETRI_PERIODICI);
	}

	public static void send(HttpServletRequest request, ParametriPeriodici parametri) {
		request.setAttribute(PARAMETRI_PERIODICI, parametri);
	}


	public enum TipoOperazione {
		KARDEX,
		NUOVO_FASCICOLO,
		GESTIONE_FASCICOLO,
		GESTIONE_FASCICOLO_NO_AMM,
		RICEVI,
		ANNULLA_RICEZIONE,
		CANCELLA,
		CONFERMA_RICEZIONE,
		ULTERIORI_FASCICOLI,
		ASSOCIA_MULTIPLA_INVENTARIO,
		RICEZIONE_MULTIPLA,
		SCEGLI_FASCICOLI_PER_CREA_ORDINE,
		SCEGLI_FASCICOLI_PER_COMUNICAZIONE,
		SCEGLI_FASCICOLI_PER_ASSOCIA_INVENTARI_ORDINE,
		ASSOCIA_MULTIPLA_GRUPPO,
		ESAME,
		PREVISIONALE,
		DESCRIZIONE_FASCICOLI,
		SALVA_PREVISIONALE,
		NUOVO_FASCICOLO_NO_AMM,
		NUOVO_FASCICOLO_PREVISTO,
		PREVISIONALE_GIORNI;
	}


	public enum ParamType {

		DATI_BIBLIOGRAFICI_PER_PERIODICI	(DatiBibliograficiPeriodicoVO.class),
		KARDEX_PERIODICO					(KardexPeriodicoVO.class),
		DETTAGLIO_FASCICOLO					(FascicoloVO.class),
		PARAMETRI_RICERCA					(RicercaKardexPeriodicoVO.class),
		ESAME_SERIE_PERIODICO				(EsameSeriePeriodicoVO.class),
		TIPO_OPERAZIONE						(TipoOperazione.class),
		PARAMETRI_RICERCA_ORDINI			(RicercaOrdiniPeriodicoVO.class),
		LISTA_ORDINI_PERIODICO				(Object.class),
		INVENTARIO							(InventarioVO.class),
		COMUNICAZIONE						(ComunicazioneVO.class),
		SIF_KARDEX_RETURN_ATTRIBUTE			(String.class),

		//almaviva5_20110216 #3986
		LISTA_FASCICOLI						(Object.class),
		POSIZIONE_LISTA_FASCICOLI			(Map.class),
		POSIZIONA_ID_FASCICOLO				(Integer.class),
		OGGETTO_RIFERIMENTO					(OggettoRiferimentoVO.class),
		LISTA_ESEMPLARI_FASCICOLO			(KardexPeriodicoVO.class),
		PARAMETRI_PREVISIONALE				(RicercaKardexPrevisionaleVO.class);

		private final Class<?> paramClass;

		private ParamType(Class<?> paramClass) {
			this.paramClass = paramClass;
		}

	}

	public ParametriPeriodici() {
		super();
		this.params = new HashMap<ParamType, Serializable>();
	}

	public Serializable put(ParamType type, Serializable value)
		throws ValidationException {

		if (value == null)
			return null;
		if (type.paramClass != null && !type.paramClass.isInstance(value))
			throw new ValidationException("Parametro non previsto " + type + ": " + value);
		return this.params.put(type, value);
	}

	public Serializable get(ParamType type) {
		return this.params.get(type);
	}

	public Serializable getAndRemove(ParamType type) {
		Serializable value = this.params.get(type);
		this.remove(type);

		return value;
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
