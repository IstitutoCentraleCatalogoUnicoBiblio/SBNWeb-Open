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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.vo.gestionebibliografica.Repertorio;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maurizio Alvino
 *
 * Questo è un componente non visuale che racchiude la logica per
 * determinare quali repertori sono stati inseriti ex novo dall'utente
 * e quali erano già presenti nella base dati.
 *
 */
public class UtilityRepertori  {

	/** Stato iniziale dei repertori */
	private List<Repertorio> arrListRepertoriIniziali = new ArrayList<Repertorio>();

	/** Numero iniziale dei repertori */
	private int numeroRepertoriIniziali = 0;

	/** Numero finale dei repertori */
	private int numeroRepertoriFinali = 0;

	/** Numero iniziale dei legami non di tipo repertorio */
	private int numeroLegamiNonRepertori = 0;

	/**
	 * Costruttore vuoto
	 */
	//public UtilityRepertori() {
	//}

	/**
	 * Salva la tabella repertori prima delle modifiche dell'utente
	 *
	 * @param tabella repertori
	 */
	public void setRepertoriIniziali(List tabellaRepertori) {
		//Se la tabella non esiste esco
		if (tabellaRepertori == null) return;

		//Inizializzo i repertori iniziali
		arrListRepertoriIniziali = new ArrayList<Repertorio>();

		//Oggetto di appoggio per i repertori
		Repertorio repertorio = null;

		//Per ogni riga della tabella prendo il relativo repertorio
		//e lo metto in arrListRepertoriIniziali
		numeroRepertoriIniziali = tabellaRepertori.size();
		TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
		for (int i = 0; i < numeroRepertoriIniziali; i++) {
			tabImpST = (TabellaNumSTDImpronteVO) tabellaRepertori.get(i);
			repertorio = new Repertorio(
					tabImpST.getCampoUno(),
					tabImpST.getCampoDue(),
					tabImpST.getNota());
			arrListRepertoriIniziali.add(repertorio);
		}

		//Indicazione per il Garbage Collector
		repertorio = null;
	}

	/**
	 * Restituisce in formato array i repertori inizialmente presenti
	 *
	 * @return array di repertori
	 */
	public int getNumeroRepertoriIniziali() {
		return arrListRepertoriIniziali.size();
	}

	/**
	 * Elimina tutti i repertori inizialmente presenti
	 *
	 * @param arrayLegamiType
	 * @param id del titolo
	 */
	private void eliminaRepertoriIniziali(LegamiType[] arrayLegamiType, String id) {

		//Se arrayList dei repertori iniziali non esiste esco
		if (arrListRepertoriIniziali == null) return;

		// creo i legami per ogni repertorio
		//(uno per ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;

		//Oggetto di appoggio per i repertori
		Repertorio repertorio = null;

		//Per ogni repertorio attivo il tipo operazione CANCELLA
		for (int i = numeroLegamiNonRepertori; i < numeroLegamiNonRepertori + numeroRepertoriIniziali; i++) {
			repertorio = arrListRepertoriIniziali.get(i);
			legamiType = new LegamiType();

			legamiType.setIdPartenza(id);
			legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(repertorio.getNotaAlLegame());

			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// prendo il tipo legame della tabella
			String tipoLegame = repertorio.getTipoLegame();

			// tipo legame
			UtilityCastor utilityCastor = new UtilityCastor();
			legameElementoAut.setTipoLegame(utilityCastor.codificaLegameRepertorio(tipoLegame));

			// prendo il codice del repertorio
			String codice = repertorio.getCodiceRepertorio();
			legameElementoAut.setIdArrivo(codice.trim());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[i] = legamiType;
		}

		//Indicazione per il Garbage Collector
		repertorio = null;
	}

	private void aggiungiRepertori(LegamiType[] arrayLegamiType, List tabellaRepertori, String id) {
		// creo i legami per ogni repertorio (ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;

		TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();

		for (int i = 0; i < numeroRepertoriFinali; i++) {
			tabImpST = (TabellaNumSTDImpronteVO) tabellaRepertori.get(i);
			legamiType = new LegamiType();

			legamiType.setIdPartenza(id);
			legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame     = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(tabImpST.getNota());

			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// prendo il tipo legame della tabella
			String tipoLegame = tabImpST.getCampoUno();
			// tipo legame
			 UtilityCastor utilityCastor = new UtilityCastor();
			legameElementoAut.setTipoLegame(utilityCastor.codificaLegameRepertorio(tipoLegame));

			// prendo il codice del repertorio
			String codice = tabImpST.getCampoDue();
			legameElementoAut.setIdArrivo(codice.trim());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[numeroLegamiNonRepertori + numeroRepertoriIniziali + i] = legamiType;
		}
	}

	public void inserisciRepertori(LegamiType[] arrayLegamiType, List tabellaRepertori, String id, int numeroLegamiNonRepertori) {
		//Se l'array legami è null esco
		if (arrayLegamiType == null) return;

		//Inizializzo le proprietà della classe
		this.numeroRepertoriIniziali = getNumeroRepertoriIniziali();
		this.numeroRepertoriFinali = tabellaRepertori.size();
		this.numeroLegamiNonRepertori = numeroLegamiNonRepertori;

		eliminaRepertoriIniziali(arrayLegamiType, id);
		aggiungiRepertori(arrayLegamiType, tabellaRepertori, id);
	}

}
