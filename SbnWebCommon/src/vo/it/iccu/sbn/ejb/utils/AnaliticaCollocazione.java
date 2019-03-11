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
package it.iccu.sbn.ejb.utils;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class AnaliticaCollocazione {


	// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
	// viene estesa alla N nuova la localizzazione per possesso della madre
	// Qui viene aggiunto il tipo legame M464N che deve essere compresi fra quelli a cui si propaga la loc per POSSESSO
	private static String[] TIPI_LEGAME = { "C410C", "C430C", "C676c", "C700a", "C710a",
			//"M410C", "M422M", "M422S", "M430M", "M430S", "M461M", "M461S",
			"M410C", "M422M", "M422S", "M430M", "M430S", "M461M", "M461S", "M464N",
			"M676c", "M700a", "M710a", "S410C", "S422S", "S430S", "S676c", "S700a", "S710a",
			"W410C", "W461M", "W676c", "W700a", "W710a", "R410R", "M410R" };

	private static Logger log = Logger.getLogger(AnaliticaCollocazione.class);

	private static AnaliticaCollocazione instance = new AnaliticaCollocazione();

	public static final DatiBibliograficiCollocazioneVO getDatiBibliografici(TreeElementViewTitoli analitica) {

		//comprimo l'analitica di gest. bibliografica
		log.debug("compressione analitica per collocazione");
		TreeElementViewTitoli titoli = instance.comprimiAnalitica(analitica.<TreeElementViewTitoli>copy());
		CollocazioneTitoloVO titoloGenerale = instance.getTitoloGenerale(titoli);
		CollocazioneTitoloVO[] listaTitoliCollocazione = instance.getListaTitoliCollocazione(titoli);
		CollocazioneTitoloVO[] listaTitoliLocalizzazione = instance.getListaTitoliLocalizzazione(titoli, null);
		//almaviva5_20140522 evolutive google3
		//il bid va inserito solo se non già localizzato per possesso
		CollocazioneTitoloVO[] listaTitoliLocalizzazioneNonPosseduti = instance.getListaTitoliLocalizzazione(titoli,
				new Integer[] {TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA, TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO} );

		return new DatiBibliograficiCollocazioneVO(titoli, titoloGenerale,
				listaTitoliCollocazione, listaTitoliLocalizzazione,
				listaTitoliLocalizzazioneNonPosseduti);
	}

	private AnaliticaCollocazione() {
		super();
	}

	private TreeElementViewTitoli comprimiAnalitica(TreeElementViewTitoli node) {

		if (node == null)
			return null;

		// elimino tutti i dettagli
		node.setAreaDatiDettaglioOggettiVO(null);

		// natura fittizia per autori e classi
		SbnAuthority tipoAuthority = node.getTipoAuthority();
		char natura = ValidazioneDati.eqAuthority(tipoAuthority, SbnAuthority.AU) ? 'a'
				: ValidazioneDati.eqAuthority(tipoAuthority, SbnAuthority.CL) ? 'c'
				: node.getNatura().charAt(0);

		switch (natura) {
		case 'M': // Monografia
		case 'S': // Periodico
		case 'C': // Collezione
		case 'W': // Tit. inferiore
		case 'R': // Raccolta fattizia

			Iterator<TreeElementView> i = node.getChildren().iterator();
			while (i.hasNext()) {
				TreeElementViewTitoli child = (TreeElementViewTitoli) i.next();
				SbnAuthority childAuth = child.getTipoAuthority();
				char childNat = ValidazioneDati.eqAuthority(childAuth, SbnAuthority.AU) ? 'a'
						: ValidazioneDati.eqAuthority(childAuth, SbnAuthority.CL) ? 'c'
						: child.getNatura().charAt(0);

				if (testLegame(natura, child.getDatiLegame().getTipoLegame(), childNat))
					comprimiAnalitica(child);
				else
					i.remove();

			} // end for
			break;

		case 'a': // Autore
		case 'c': // Classe
		default:
			node.getChildren().clear();
		}
		return node;
	}

	private boolean testLegame(char natura, String tipoLegame, char childNat) {
		String test = natura + tipoLegame + childNat;
		//logger.info("relazione elemento: " + test);
		for (int i = 0; i < TIPI_LEGAME.length; i++)
			if (test.equals(TIPI_LEGAME[i]))
				return true;

		return false;
	}

	private CollocazioneTitoloVO getTitoloGenerale(TreeElementViewTitoli node) {

		CollocazioneTitoloVO titGen = new CollocazioneTitoloVO(0, node.getKey(),
				node.getDescription(), node.getNatura(), null, null, cercaAutorePrincipale(node) );
		for (Object o : node.getChildren()) {
			TreeElementViewTitoli child = (TreeElementViewTitoli) o;
			if (child.getDatiLegame().getTipoLegame().equals("461")) // legame 01
				return new CollocazioneTitoloVO(0, child.getKey(), child.getDescription(),
					child.getNatura(), null, null, cercaAutorePrincipale(child) );
		}

		return titGen;
	}

	private CollocazioneTitoloVO[] getListaTitoliCollocazione(TreeElementViewTitoli analitica) {
		Map<String, CollocazioneTitoloVO> listaBid = new HashMap<String, CollocazioneTitoloVO>();
		this.estraiTitolo(analitica, listaBid);
		int size = ValidazioneDati.size(listaBid);
		if (size < 1 )
			return null;

		CollocazioneTitoloVO[] ret = new CollocazioneTitoloVO[size];
		listaBid.values().toArray(ret);
		return ret;
	}

	private CollocazioneTitoloVO[] getListaTitoliLocalizzazione(TreeElementViewTitoli analitica, Integer[] filtroLoc) {

		Map<String, CollocazioneTitoloVO> listaBid = new HashMap<String, CollocazioneTitoloVO>();

		//almaviva5_20140522 evolutive google3
		if (!ValidazioneDati.in(analitica.getLocalizzazione(), filtroLoc))
			listaBid.put(analitica.getKey(), new CollocazioneTitoloVO(0, analitica.getKey(),
				analitica.getDescription(), analitica.getNatura(),
				null, null, cercaAutorePrincipale(analitica) ));

		// il sistema seleziona sempre e comunque il bid della notizia corrente (root dell'analitica)
		// e per i titoli legati ad esso cerca ricorsivamente le eventuali monografie superiori (legame 461).
		// esempio: la natura S che non prevede legami S461M sarà sempre estratto singolarmente.
		this.estraiTitoloLoc(analitica, listaBid, filtroLoc);
		int size = ValidazioneDati.size(listaBid);
		if (size < 1 )
			return null;

		CollocazioneTitoloVO[] ret = new CollocazioneTitoloVO[size];
		listaBid.values().toArray(ret);
		return ret;
	}

	private void estraiTitolo(TreeElementViewTitoli node, Map<String, CollocazioneTitoloVO> listaBid) {
		if (node.getTipoAuthority() != null)
			return;
		char natura = node.getNatura().charAt(0);

		switch (natura) {
		case 'M': // Monografia
		case 'S': // Periodico
		case 'C': // Collezione
		case 'W': // Tit. inferiore
		case 'R': // Raccolta fattizia
			listaBid.put(node.getKey(), new CollocazioneTitoloVO(0, node.getKey(),
				node.getDescription(), node.getNatura(), null, null, cercaAutorePrincipale(node) ));
		}

		for (Object o : node.getChildren()) {
			TreeElementViewTitoli child = (TreeElementViewTitoli) o;
			estraiTitolo(child, listaBid);
		}

	}

	private void estraiTitoloLoc(TreeElementViewTitoli node, Map<String, CollocazioneTitoloVO> listaBid, Integer[] filtroLoc) {
		if (node.getTipoAuthority() != null)
			return;

		// almaviva2 marzo 2017 - intervento bug esercizio #6403: il filtro sulle localizzazioni viene inserito anche quando
		// si verificano i legami a spogli (464) altrimenti nell'elenco dei bid rientrano tutti gli oggetti qualunque localizzazione
		// essi abbiano.

		char natura = node.getNatura().charAt(0);
		DatiLegame datiLegame = node.getDatiLegame();

		// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
		// viene estesa alla N nuova la localizzazione per possesso della madre
		// Qui viene iserito anche il case sul valore N -titolo analitico (spoglio)- per il tipo legame 464 a cui deve essere
		// esplosa la localizzazione per POSSESSO
		if (datiLegame != null) {
			String tipoLegame = datiLegame.getTipoLegame();
			switch (natura) {
			case 'M': // Monografia
				if (ValidazioneDati.equals(tipoLegame, "461") ) // legame 01
					listaBid.put(node.getKey(), new CollocazioneTitoloVO(0, node.getKey(),
						node.getDescription(), node.getNatura(), null, null, cercaAutorePrincipale(node) ));

			case 'R': // raccolta
				if (ValidazioneDati.equals(tipoLegame, "410") ) // legame 01
					listaBid.put(node.getKey(), new CollocazioneTitoloVO(0, node.getKey(),
						node.getDescription(), node.getNatura(), null, null, cercaAutorePrincipale(node) ));

			case 'N': // titolo analitico (spoglio)
				if (ValidazioneDati.equals(tipoLegame, "464") ) // legame 01
					if (!ValidazioneDati.in(node.getLocalizzazione(), filtroLoc)) {
						listaBid.put(node.getKey(), new CollocazioneTitoloVO(0, node.getKey(),
						node.getDescription(), node.getNatura(), null, null, cercaAutorePrincipale(node) ));
					}
			}
		}

		for (Object o : node.getChildren()) {
			TreeElementViewTitoli child = (TreeElementViewTitoli) o;
			estraiTitoloLoc(child, listaBid, filtroLoc);
		}

	}

	private String cercaAutorePrincipale(TreeElementViewTitoli node) {
		for (Object o : node.getChildren()) {
			TreeElementViewTitoli child = (TreeElementViewTitoli) o;
			SbnAuthority authority = child.getTipoAuthority();
			if (authority == null)
				continue;

			DatiLegame datiLegame = child.getDatiLegame();
			if (datiLegame == null)
				continue;

			String responsabilita = datiLegame.getResponsabilita();
			if (ValidazioneDati.eqAuthority(authority, SbnAuthority.AU)
					&& ValidazioneDati.equals(responsabilita, "1"))
				return child.getKey();
		}
		return null;
	}

}
