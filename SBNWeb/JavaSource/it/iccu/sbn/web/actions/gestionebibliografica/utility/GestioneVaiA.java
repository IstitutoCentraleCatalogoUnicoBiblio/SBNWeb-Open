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
package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import it.iccu.sbn.ejb.home.MenuHome;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.periodici.DatiBibliograficiPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici;
import it.iccu.sbn.ejb.vo.periodici.ParametriPeriodici.ParamType;
import it.iccu.sbn.web.actions.gestionesemantica.utility.VerificaOggettoPerSoggettazione;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.DescrizioneFunzioneVO;
import it.iccu.sbn.web.vo.vaia.Condiviso;
import it.iccu.sbn.web.vo.vaia.LivelloRicerca;
import it.iccu.sbn.web.vo.vaia.Localizzazione;
import it.iccu.sbn.web.vo.vaia.RootReticolo;
import it.iccu.sbn.web.vo.vaia.TipoOggetto;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GestioneVaiA {

	private static Log log = LogFactory.getLog(GestioneVaiA.class);

	private List<DescrizioneFunzioneVO> getLista(
			HttpServletRequest request, String codice) {
		Map<String, List<DescrizioneFunzioneVO>> funzioni = (Map<String, List<DescrizioneFunzioneVO>>) request.getSession()
				.getAttribute(Constants.USER_FUNZ);
		List<DescrizioneFunzioneVO> list = funzioni
				.get(codice);
		return list;
	}

	private List<DescrizioneFunzioneVO> getLista(
			HttpServletRequest request, String codice, TipoOggetto tipo,
			LivelloRicerca livello, Localizzazione loc, Condiviso condiviso,
			RootReticolo rootReticolo) {

		// String key = tipo + "_" + livello + "_" + loc + "_" + condiviso + "_"
		// + rootReticolo;

		List<DescrizioneFunzioneVO> list = getLista(request, codice);

		List<DescrizioneFunzioneVO> listaFiltrata = new ArrayList<DescrizioneFunzioneVO>();

		for (DescrizioneFunzioneVO funz : list) {
			if ((funz.getTipo().equals(TipoOggetto.QUALSIASI) || funz.getTipo()
					.equals(tipo))
					&& (funz.getLivello().equals(LivelloRicerca.QUALSIASI) || funz
							.getLivello().equals(livello))
					&& (funz.getLoc().equals(Localizzazione.QUALSIASI) || funz
							.getLoc().equals(loc))
					&& (funz.getCondiviso().equals(Condiviso.QUALSIASI) || funz
							.getCondiviso().equals(condiviso))
					&& (funz.getRootReticolo().equals(RootReticolo.QUALSIASI) || funz
							.getRootReticolo().equals(rootReticolo))) {
				listaFiltrata.add(funz);
			}
		}

		// if (funz.getKey().equals(key))
		// listaFiltrata.add(funz);

		return listaFiltrata;
	}

	private List<ComboCodDescVO> convertList(HttpServletRequest request,
			List<DescrizioneFunzioneVO> lista) {
		Locale local = (Locale) request.getSession().getAttribute(
				Globals.LOCALE_KEY);
		List<ComboCodDescVO> ret = new ArrayList<ComboCodDescVO>();

		Iterator<DescrizioneFunzioneVO> itr = lista.iterator();
		while (itr.hasNext()) {
			DescrizioneFunzioneVO element = itr.next();
			ComboCodDescVO combo = new ComboCodDescVO();
			combo.setCodice(element.getCodice());
			// combo.setDescrizione(element.getDescrizione(local.getLanguage()+"_"+local.getCountry()));
			combo.setDescrizione(element.getDescrizione(local.getLanguage()));
			ret.add(combo);
		}
		return ret;
	}

	public List<ComboCodDescVO> getDocumentoFisicoLista(
			HttpServletRequest request) {
		List<?> list = this.getLista(request,
				MenuHome.MODULE_DOCUMENTO_FISICO);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();
		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list
					.get(index);
			ret.add(funz);

		}
		return convertList(request, ret);
	}

	public List<ComboCodDescVO> getSemanticaLista(
			HttpServletRequest request, String livello,
			TreeElementViewTitoli elementoTree) {
		List<?> list = this.getLista(request, MenuHome.MODULE_SEMANTICA);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();

		boolean presenzaLocal = false;
		if (elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
			presenzaLocal = true;
		}

		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list.get(index);

			String codice = funz.getCodice();
			if (codice.equals(MenuHome.FUNZ_SEMANTICA_DEFAULT))
				ret.add(funz);

			//almaviva5_20090715
			boolean soggettabile = VerificaOggettoPerSoggettazione.isSoggettabile(elementoTree);

			if (codice.equals(MenuHome.FUNZ_SEMANTICA_SOGGETTAZIONE)) {
				if (presenzaLocal && soggettabile)
					ret.add(funz);
			}
			// almaviva5_20090327 #2744
			if (codice
					.equals(MenuHome.FUNZ_SEMANTICA_CLASSIFICAZIONE)) {
				if (presenzaLocal && VerificaOggettoPerSoggettazione.isNaturaAmmessa(elementoTree.getNatura()))
					ret.add(funz);
			}

			if (codice
					.equals(MenuHome.FUNZ_SEMANTICA_THESAURO)) {
				if (presenzaLocal && soggettabile)
					ret.add(funz);
			}

			if (codice
					.equals(MenuHome.FUNZ_SEMANTICA_ABSTRACT)) {
				if (presenzaLocal && soggettabile)
					ret.add(funz);
			}

		}
		return convertList(request, ret);
	}

	public List<ComboCodDescVO> getAcquisizioneLista(
			HttpServletRequest request, String livelloRicerca,
			TreeElementViewTitoli elementoTree) {

		// List<DescrizioneFunzioneVO> list = this.getLista(request,
		// MenuHome.MODULE_ACQUISIZIONI);
		// List<DescrizioneFunzioneVO> ret =
		// (List<DescrizioneFunzioneVO>) list.clone();
		List<?> list = this
				.getLista(request, MenuHome.MODULE_ACQUISIZIONI);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();

		boolean presenzaLocal = false;
		if (elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
			presenzaLocal = true;
		}

		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list
					.get(index);
			if (funz.getCodice().equals(MenuHome.FUNZ_ACQUISIZIONE_DEFAULT)) {
				ret.add(funz);
			} else if (funz.getCodice().equals(
					MenuHome.FUNZ_ACQUISIZIONE_ORDINE)) {
				if (presenzaLocal)
					ret.add(funz);
			} else if (funz.getCodice().equals(
					MenuHome.FUNZ_ACQUISIZIONE_SUGGERIMENTO_BIBLIOTECARIO)) {
				if (presenzaLocal)
					ret.add(funz);
			} else if (funz.getCodice().equals(
					MenuHome.FUNZ_ACQUISIZIONE_GARA_D_ACQUISTO)) {
				if (presenzaLocal)
					ret.add(funz);
			} else if (funz.getCodice().equals(
					MenuHome.FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE)) {
				// if (livelloRicerca.equals("I") && !presenzaLocal &&
				// (elementoTree.getNatura().equals("M") ||
				// elementoTree.getNatura().equals("C"))) {
				// ret.add(funz);
				// }
			}

		}

		return convertList(request, ret);
	}

	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
	public List<ComboCodDescVO> getPeriodiciLista(
			HttpServletRequest request, String livelloRicerca,
			TreeElementViewTitoli elementoTree) {

		List<?> list = this.getLista(request, MenuHome.MODULE_PERIODICI);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();

		boolean presenzaLocal = false;
		if (elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
				|| elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
			presenzaLocal = true;
		}

		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list.get(index);
			if (funz.getCodice().equals(MenuHome.FUNZ_PERIODICI_DEFAULT)) {
				ret.add(funz);
			} else if (funz.getCodice().equals(MenuHome.FUNZ_PERIODICI_DESCRIZIONE_FASCICOLI)) {
				if (presenzaLocal)
					ret.add(funz);
			} else if (funz.getCodice().equals(MenuHome.FUNZ_PERIODICI_GESTIONE_AMMINISTRATIVA)) {
				if (presenzaLocal)
					ret.add(funz);
			}
		}
		return convertList(request, ret);
	}
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici

	public List<ComboCodDescVO> getPossessoreLista(
			HttpServletRequest request, String livelloRicerca,
			TreeElementViewTitoli elementoTree) {

		List<?> list = this.getLista(request, MenuHome.MODULE_POSSESSORI);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();

		boolean presenzaLocal = false;
		log.debug(presenzaLocal);
		int localizzazione = elementoTree.getLocalizzazione();
		if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
				|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
				|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
				|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
				|| localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
			presenzaLocal = true;
		}

		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list
					.get(index);
			String codice = funz.getCodice();
			if (codice.equals(MenuHome.FUNZ_POSSESSORI_DEFAULT)) {
				ret.add(funz);
			} else if (codice
					.equals(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_DESCRIZIONE)) {
				if (!elementoTree.isPossessoreFormaRinvio()) {
					ret.add(funz);
				}
			} else if (codice
					.equals(MenuHome.FUNZ_POSSESSORI_INSERIMENTO_LEGAME_INVENTARIO)) {
				if (request.getSession().getAttribute("parametriDOCFIS") != null) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_POSSESSORI_CANCELLAZIONE)) {
				ret.add(funz);
			} else if (codice
					.equals(MenuHome.FUNZ_POSSESSORI_INSERIMENTO_FORMA_RINVIO)) {
				if (!elementoTree.isPossessoreFormaRinvio()) {
					ret.add(funz);
				}
			} else if (codice
					.equals(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_FORMA_RINVIO)) {
				if (elementoTree.isPossessoreFormaRinvio()) {
					ret.add(funz);
				}
			} else if (codice
					.equals(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_LEGAME)) {
				if (elementoTree.isPossessoreFormaRinvio()) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_POSSESSORI_SCAMBIO_FORMA)) {
				if (elementoTree.isPossessoreFormaRinvio()) {
					ret.add(funz);
				}
			}
		}

		return convertList(request, ret);
	}

	public List<ComboCodDescVO> getBibliograficaTitoloLista(
			HttpServletRequest request, String livelloRicerca,
			TreeElementViewTitoli elementoTree, boolean bidRoot) {
		// List<?> listVecchia = this.getLista(request,
		// MenuHome.MODULE_BIBLIOGRAFICA);

		// Nuova versione
		TipoOggetto tipoOggettoNew = null;


		// Intervento interno 07.09.2001 Correzione dell'impostazione della natura che viene valorizzata a VUOTO nel caso in cui sia null
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		// spostata la decodifica della natura per gestire il RINVIO a livello di setrazione delle funzioni di VAI A
		String natura = ValidazioneDati.trimOrEmpty(elementoTree.getNatura());


		if (elementoTree.getTipoAuthority() == null) {
			tipoOggettoNew = TipoOggetto.TITOLO;
		} else {
			if (elementoTree.getTipoAuthority().toString().equals("AU")) {
				tipoOggettoNew = TipoOggetto.AUTORE;
			} else if (elementoTree.getTipoAuthority().toString().equals("MA")) {
				tipoOggettoNew = TipoOggetto.MARCA;
			} else if (elementoTree.getTipoAuthority().toString().equals("LU")) {
				tipoOggettoNew = TipoOggetto.LUOGO;
			} else if (elementoTree.getTipoAuthority().toString().equals("TU")
					|| elementoTree.getTipoAuthority().toString().equals("UM")) {
				if (natura.equals("V")) {
					tipoOggettoNew = TipoOggetto.TIT_UNIFORME_RINVIO;
				}  else {
					tipoOggettoNew = TipoOggetto.TIT_UNIFORME;
				}
			} else if (elementoTree.getTipoAuthority().toString().equals("CL")
					|| elementoTree.getTipoAuthority().toString().equals("SO")
					|| elementoTree.getTipoAuthority().toString().equals("TH")) {
				tipoOggettoNew = TipoOggetto.SEMANTICA;
			} else if (elementoTree.getTipoAuthority().toString().equals("PO")) {
				tipoOggettoNew = TipoOggetto.POSSESSORE;
			}
		}

		LivelloRicerca livelloRicercaNew = null;
		if (livelloRicerca.equals("I")) {
			livelloRicercaNew = LivelloRicerca.INDICE;
		} else {
			livelloRicercaNew = LivelloRicerca.POLO;
		}

		boolean presenzaLocal = false;
		Localizzazione localizzazioneNew = null;
		int localizzazione = elementoTree.getLocalizzazione();
		if (tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
			localizzazioneNew = Localizzazione.SI;
		} else {
			if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
					|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
					|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
//					|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
					|| localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
				presenzaLocal = true;
				localizzazioneNew = Localizzazione.SI;
			} else {
				localizzazioneNew = Localizzazione.NO;
			}
		}

		Condiviso condivisoNew = null;
		if (elementoTree.isFlagCondiviso()) {
			condivisoNew = Condiviso.SI;
		} else {
			condivisoNew = Condiviso.NO;
		}

		RootReticolo rootReticoloNew = null;
		if (bidRoot) {
			rootReticoloNew = RootReticolo.SI;
		} else {
			rootReticoloNew = RootReticolo.NO;
		}
		List<?> list = this.getLista(request,
				MenuHome.MODULE_BIBLIOGRAFICA, tipoOggettoNew,
				livelloRicercaNew, localizzazioneNew, condivisoNew,
				rootReticoloNew);

		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();
		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list.get(index);
			String codice = funz.getCodice();

//			// Intervento interno 07.09.2001 Correzione dell'impostazione della natura che viene valorizzata a VUOTO nel caso in cui sia null
//			String natura = ValidazioneDati.trimOrEmpty(elementoTree.getNatura());
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = elementoTree.getAreaDatiDettaglioOggettiVO();
			DettaglioTitoloCompletoVO dettaglioTitoloCompletoVO = areaDatiDettaglioOggettiVO.getDettaglioTitoloCompletoVO();
			if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA)) {
				if (!natura.equals("D")
						&& !natura.equals("P")
						&& !natura.equals("T")
						&& !natura.equals("B")) {
					DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloCompletoVO.getDetTitoloPFissaVO();
					String tipoRecord = detTitoloPFissaVO.getTipoRec();
					if (!(tipoRecord.equals("f") || tipoRecord.equals("d") || tipoRecord.equals("b"))) {
						ret.add(funz);
					} else {
						// Inizio manutenzione almaviva2 09.12.2009 - BUG MANTIS 3248 - i manoscritti localizati per possesso dalla
						// biblioteca operante possono essere catturati dalla stessa così da averne anche la gestione.
						if (elementoTree.getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO) {
							ret.add(funz);
						}
						// Fine manutenzione almaviva2 09.12.2009
					}
				}

				// INIZIO RICORDA  DA ELIMINARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_TRASCINA_LEGAME_AUTORE)) {
				// attualmente la funzione è invisibile: verrà attivata in seguito
				// FINE RICORDA  DA ELIMINARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			// Inizio modifica 28.10.2010 MANTIS 3962 - natura S non deve essere presente la voce "cattura vol.inferiore/tit. analitico(N)"
			// Modifica almaviva2 MANTIS 4299 - eliminare Cattura vol. inferiore/tit. analitico titoli N e W (in radice nel reticolo).
			// almaviva2 Novembre 2016 : BUG esercizio 6297 da risolvere come manutenzione adeguativa/migliorativa
			// la cattura multipla dei titoli analitici (spogli natura N) può essere effettuata anche a partire dalla W e non solo
			// dai documenti di tipo M - si cambia if per far comparire la voce nel "vai a"
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_INFERIORI)) {
				//if (!natura.equals("S") && !natura.equals("W") && !natura.equals("N")) {
				if (!natura.equals("S") && !natura.equals("N")) {
					ret.add(funz);
				}
			// Fine Inizio modifica 28.10.2010 MANTIS 3962

			// Modifica almaviva2 MANTIS 4299  - eliminare Copia reticolo titoli N e W (in radice nel reticolo).
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO)) {
				if (!natura.equals("D") && !natura.equals("P") && !natura.equals("T") && !natura.equals("B")
						&& !natura.equals("W") && !natura.equals("N")) {
					ret.add(funz);
				}
			// almaviva2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
			// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
			// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
			// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO_TIT_ANALITICI)) {
				DettaglioTitoloParteFissaVO detTitoloPFissaVO = dettaglioTitoloCompletoVO.getDetTitoloPFissaVO();
				String tipoRecord = detTitoloPFissaVO.getTipoRec();
				if (natura.equals("M") && (tipoRecord.equals("j") || tipoRecord.equals("i") || tipoRecord.equals("g"))) {
						ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_A_SUGGERIMENTO)) {
				if (natura.equals("M")
						|| natura.equals("C")
						|| natura.equals("S")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION)) {
				if (!elementoTree.isFlagCondiviso()) {
					// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
					if (!natura.equals("R")) {
						ret.add(funz);
					}
				}
			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_FONDI_ON_LINE)) {
				if (!elementoTree.isFlagCondiviso()) {
					// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
					if (!natura.equals("R")) {
						ret.add(funz);
					}
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO)) {
				// Modifica almaviva2 del 20.09.2011 Intervento interno per RACCOLTE FATTIZIE ( cod natura R)
				// e' ammessa la voce di lega Titolo per consentire la creazione del legame M01R
//				if (!natura.equals("D") && !natura.equals("P") && !natura.equals("R")) {
				if (!natura.equals("D") && !natura.equals("P")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA)) {
				// Inizio intervento almaviva2 - mail Scognamiglio 12.06.2012
				// nuova voce attivabile in locale su M condivise e consente solo di legare le raccolte
				if (natura.equals("M")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE)) {
				if (!natura.equals("D") && !natura.equals("P")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_VOLUMI_INFERIORI)) {
				if (natura.equals("M")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_MARCA)) {
				String tipoMateriale = elementoTree.getTipoMateriale();
				if (tipoMateriale != null && tipoMateriale.toString().equals("M")) {
				} else {
					if (natura.equals("M") || natura.equals("W")) {
						ret.add(funz);
					}
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO)
					|| codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE)) {
				if (natura.equals("M")
						|| natura.equals("W")
						|| natura.equals("S")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_LUOGO)) {
				if (natura.equals("M")
						|| natura.equals("S")
						|| natura.equals("W")
						|| natura.equals("C")) {
					ret.add(funz);
				}
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA)) {
				// Modifica almaviva2 BUG MANTIS COLLAUDO 4403 Gestione Bibliografica
				// Le collane (ma anche tutti gli altri documenti) non devono essere delocalizzate, non essendo localizzate per possesso.
				// La delocalizzazione deve riguardare solo titoli che hanno il posseduto in Indice. Aggiunto and su tipo localizzazione;
				// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
				// Inizio Modifica almaviva2 BUG MANTIS Esercizio 5266 Gestione Bibliografica 28.02.2013
				// RETROFONT !!!!!!!!!!!!!!!!!!!!
				// Si possono delocalizzare i documenti anche se localizzati solo x Gestione.
				// Viene asteriscato l'and inserito con la manutenzione precedente;
//				if ((natura.equals("M") || natura.equals("S") || natura.equals("W") || natura.equals("C") || natura.equals("R")) &&
//						(localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA || localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO
//						|| localizzazione == TitoliCollegatiInvoke.LOCAL_POL_COMPLETA || localizzazione == TitoliCollegatiInvoke.LOCAL_POL_POSSESSO)) {
//					ret.add(funz);
//				}
				if (natura.equals("M") || natura.equals("S") || natura.equals("W") || natura.equals("C") || natura.equals("R")) {
					ret.add(funz);
				}
				// Fine modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione come vecchia Gestione 51
				// Fine Modifica almaviva2 BUG MANTIS Esercizio 5266 Gestione Bibliografica 28.02.2013
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI)) {
				if ((natura.equals("M")	|| natura.equals("N") || natura.equals("W"))
						&&	(presenzaLocal)) {
					ret.add(funz);
				}
				// Fine modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione cpme vecchia Gestione 51
			} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CORREZIONE_NOTA_ISBD)) {
				if (natura.equals("M")
						|| natura.equals("S")
						|| natura.equals("W")
						|| natura.equals("C")
						|| natura.equals("N")) {
					ret.add(funz);
				}
			} else {
				DettaglioAutoreGeneraleVO dettaglioAutoreGeneraleVO = areaDatiDettaglioOggettiVO.getDettaglioAutoreGeneraleVO();
				DettaglioLuogoGeneraleVO dettaglioLuogoGeneraleVO = areaDatiDettaglioOggettiVO.getDettaglioLuogoGeneraleVO();
				if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO)) {
					if (tipoOggettoNew.equals(TipoOggetto.AUTORE)) {
						if (dettaglioAutoreGeneraleVO != null) {
							if (dettaglioAutoreGeneraleVO.getForma() != null
									&& dettaglioAutoreGeneraleVO.getForma().equals("A")) {
								ret.add(funz);
							}
						}
					} else if (tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
						if (dettaglioLuogoGeneraleVO != null) {
							if (dettaglioLuogoGeneraleVO.getForma() != null
									&& dettaglioLuogoGeneraleVO.getForma().equals("A")) {
								ret.add(funz);
							}
						}
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO)) {
					if (tipoOggettoNew.equals(TipoOggetto.AUTORE)) {
						if (dettaglioAutoreGeneraleVO != null) {
							if (dettaglioAutoreGeneraleVO.getForma() != null
									&& dettaglioAutoreGeneraleVO.getForma().equals("R")) {
								ret.add(funz);
							}
						}
					} else if (tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
						if (dettaglioLuogoGeneraleVO != null) {
							if (dettaglioLuogoGeneraleVO.getForma() != null
									&& dettaglioLuogoGeneraleVO.getForma().equals("R")) {
								ret.add(funz);
							}
						}
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LEGAME)) {
					if (livelloRicercaNew.equals(LivelloRicerca.POLO)
							&& elementoTree.isFlagCondiviso()
							&& elementoTree.getParent().isFlagCondiviso()) {
						// in Polo cue elementi condivisi non si mostra la voce ... Niente da fare
					} else {
						if (tipoOggettoNew.equals(TipoOggetto.AUTORE)) {
							if (dettaglioAutoreGeneraleVO != null) {
								if (dettaglioAutoreGeneraleVO.getForma() != null
										&& dettaglioAutoreGeneraleVO.getForma().equals("A")) {
									ret.add(funz);
								}
							}
						} else if (tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
							if (dettaglioLuogoGeneraleVO != null) {
								if (dettaglioLuogoGeneraleVO.getForma() != null
										&& dettaglioLuogoGeneraleVO.getForma().equals("A")) {
									ret.add(funz);
								}
							}

						// almaviva2 09.04.2010 Mantis 3677 - per i legami 51 non deve essere attivabile la voce cancella legame
						} else if (tipoOggettoNew.equals(TipoOggetto.TITOLO)) {
							//almaviva5_20100415 per default va aggiunto, per poi eliminarlo in caso di legame 51
							ret.add(funz);
							if (dettaglioTitoloCompletoVO != null) {
								if (ValidazioneDati.equals(dettaglioTitoloCompletoVO.getCampoTipoLegame(), "51")) {
									ret.remove(funz);
								// Inizio modifica almaviva2 06.12.2010 - BUG MANTIS 4042 aggiunto else if su nuovo campo
								// campoTipoLegameConNature per verificare la prsenza della voce CANCELLA_LEGAME
								// (GestioneVaiA - getBibliograficaTitoloLista)
								} else if (ValidazioneDati.equals(dettaglioTitoloCompletoVO.getCampoTipoLegameConNature(), "W01M")
										|| ValidazioneDati.equals(dettaglioTitoloCompletoVO.getCampoTipoLegameConNature(), "N01M")
										|| ValidazioneDati.equals(dettaglioTitoloCompletoVO.getCampoTipoLegameConNature(), "N01S")) {
											ret.remove(funz);
								}
								// Fine modifica almaviva2 06.12.2010 - BUG MANTIS 4042
							}
//							// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
//						} else if (tipoOggettoNew.equals(TipoOggetto.TIT_UNIFORME_RINVIO)) {
//							//La cancellazione del legame non è ammessa; si puo fare solo cancellazione forma di rinvio
						} else {
							ret.add(funz);
						}
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_LEGAME)) {
					if (livelloRicercaNew.equals(LivelloRicerca.POLO)
							&& elementoTree.isFlagCondiviso()
							&& elementoTree.getParent().isFlagCondiviso()) {
						// in Polo su elementi condivisi non si mostra la voce ... Niente da fare
					} else if (tipoOggettoNew.equals(TipoOggetto.LUOGO)
							&& dettaglioLuogoGeneraleVO != null
							&& dettaglioLuogoGeneraleVO.getForma() != null
							&& dettaglioLuogoGeneraleVO.getForma().equals("R")) {
						// Gestione Bibliografica Intervento Interno: tutta la gestione dei Luoghi è demandata all'Indice vengono quindi asteriscate
						// tutte le attivazioni;

					} else {
						ret.add(funz);
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION)) {
					if (tipoOggettoNew.equals(TipoOggetto.AUTORE)) {
						if (dettaglioAutoreGeneraleVO != null) {
							if (dettaglioAutoreGeneraleVO.getForma() != null
									&& dettaglioAutoreGeneraleVO.getForma().equals("A")) {
								ret.add(funz);
							}
						}
					} else if (tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
						if (dettaglioLuogoGeneraleVO != null) {
							if (dettaglioLuogoGeneraleVO.getForma() != null
									&& dettaglioLuogoGeneraleVO.getForma().equals("A")) {
								ret.add(funz);
							}
						}
					} else {
						ret.add(funz);
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIO_FORMA)) {
					if (dettaglioAutoreGeneraleVO.getForma().equals("R")) {
						ret.add(funz);
					}

				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIA_LEGAME_AUTORE)) {
						if (dettaglioAutoreGeneraleVO.getForma().equals("A")) {
							if (dettaglioAutoreGeneraleVO.getResponsabilita() != null) {
								if (dettaglioAutoreGeneraleVO.getResponsabilita().equals("2")) {
									ret.add(funz);
								}
							}
						}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORE_MARCA)) {
					if (dettaglioAutoreGeneraleVO.getForma().equals("A")
							&& (dettaglioAutoreGeneraleVO.getTipoNome().equals("E")
									|| dettaglioAutoreGeneraleVO.getTipoNome().equals("G"))) {
						ret.add(funz);
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORI)) {
					// Inizio Dicembre 2015 almaviva2 : evoluticva per consentire la creazione dei legami A4A anche fra autore personale
					// ed autore Ente (solo a partire da autore personale) - modifiche effettuate in creazione/modifica/cancellazione/allineamento
					// sia su autori di Indice che solo locali
					if (dettaglioAutoreGeneraleVO.getForma().equals("A")
							&& (dettaglioAutoreGeneraleVO.getTipoNome().equals("A") ||
								dettaglioAutoreGeneraleVO.getTipoNome().equals("B") ||
								dettaglioAutoreGeneraleVO.getTipoNome().equals("C") ||
								dettaglioAutoreGeneraleVO.getTipoNome().equals("D") ||
								dettaglioAutoreGeneraleVO.getTipoNome().equals("E")	|| dettaglioAutoreGeneraleVO.getTipoNome().equals("G"))) {
						ret.add(funz);
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI)
						|| codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_GESTIONE)
						|| codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO)) {
					// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
					if (!tipoOggettoNew.equals(TipoOggetto.LUOGO) && !tipoOggettoNew.equals(TipoOggetto.TIT_UNIFORME_RINVIO)) {
						ret.add(funz);
					}
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_CAMBIA_NATURA_BA_AB)) {
					if ((tipoOggettoNew.equals(TipoOggetto.TITOLO) || tipoOggettoNew.equals(TipoOggetto.TIT_UNIFORME))
							&& (natura.equals("A") || natura.equals("B"))) {
						ret.add(funz);
					}


					// Febbraio 2018 - La gestione dei legami fra titoli uniformi con rinvii è
					// possibile solo per i titoli uniformi TU non per quelli musicali (quindi tipo UM)
					// MAGGIO 2018 almaviva2 - La gestione dei legami fra titoli uniformi con rinvii è
					// possibile ANCHE per i titoli uniformi TU E NON SOLO per quelli musicali (quindi tipo UM)
				} else if (	codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_RINVIO)) {
					if (elementoTree.getTipoAuthority().toString().equals("TU") || elementoTree.getTipoAuthority().toString().equals("UM")) {
						ret.add(funz);
					}


				// Gestione Bibliografica Intervento Interno: tutta la gestione dei Luoghi è demandata all'Indice vengono quindi asteriscate
				// tutte le attivazioni;
				} else if (codice.equals(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_DESCRIZIONE)) {
					if (!tipoOggettoNew.equals(TipoOggetto.LUOGO)) {
						ret.add(funz);
					}
				}
				else {
					ret.add(funz);
				}
			}

		}
		return convertList(request, ret);
	}

	public List<ComboCodDescVO> getStampeLista(
			HttpServletRequest request, String livelloRicerca,
			TreeElementViewTitoli elementoTree) {

		String natura = ValidazioneDati.trimOrEmpty(elementoTree.getNatura());
		List<?> list = this.getLista(request, MenuHome.MODULE_STAMPE);
		List<DescrizioneFunzioneVO> ret = new ArrayList<DescrizioneFunzioneVO>();

		for (int index = 0; index < list.size(); index++) {
			DescrizioneFunzioneVO funz = (DescrizioneFunzioneVO) list.get(index);
			// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
			// Nuova funzione di Lega Editore a Titolo e di Gestione (modifica e cancellazione dei legami preesistenti)
			if (funz.getCodice().equals(MenuHome.FUNZ_STAMPE_DEFAULT)
					|| funz.getCodice().equals(MenuHome.FUNZ_STAMPE_CATALOGRAFICHE)) {
				ret.add(funz);
			} else if (funz.getCodice().equals(MenuHome.FUNZ_LEGA_EDITORE)) {
			} else if (funz.getCodice().equals(MenuHome.FUNZ_GEST_LEGAMI_TIT_EDI)) {
				if ((natura.equals("M") || natura.equals("S") || natura.equals("W") || natura.equals("C") &&
						livelloRicerca.equals("P"))) {
					ret.add(funz);
				}
			}
		}
		return convertList(request, ret);
	}

	public ActionForward getAcquisizioneAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree) throws Exception {
		if (ValidazioneDati.isFilled(cod)) {

			// ACQUISIZIONI
			request.setAttribute("MODULO_VAI_A", MenuHome.MODULE_ACQUISIZIONI);
			request.setAttribute("FUNZIONE_VAI_A", cod);

			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			request.setAttribute("natura", elementoTree.getNatura());
			if (cod.equals(MenuHome.FUNZ_ACQUISIZIONE_ORDINE)) {
				// return mapping.findForward("vaiAOrdine");
				return mapping.findForward("vaiA");
			}
			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}

	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
	public ActionForward getPeriodiciAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree) throws Exception {
		if (ValidazioneDati.isFilled(cod)) {

			// periodici
			request.setAttribute("MODULO_VAI_A", MenuHome.MODULE_PERIODICI);
			request.setAttribute("FUNZIONE_VAI_A", cod);

			DatiBibliograficiPeriodicoVO dbp = new DatiBibliograficiPeriodicoVO(
					elementoTree.getKey(),
					elementoTree.getDescription(),
					elementoTree);

			ParametriPeriodici params = new ParametriPeriodici();
			params.put(ParamType.DATI_BIBLIOGRAFICI_PER_PERIODICI, dbp);
			ParametriPeriodici.send(request, params);
			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici

	public ActionForward getSemanticaAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree, String livello)
			throws Exception {
		if (ValidazioneDati.isFilled(cod)) {
			// SEMANTICA
			if (cod.equals(MenuHome.FUNZ_SEMANTICA_SOGGETTAZIONE)
					|| cod.equals(MenuHome.FUNZ_SEMANTICA_CLASSIFICAZIONE)
					|| cod.equals(MenuHome.FUNZ_SEMANTICA_THESAURO)
					|| cod.equals(MenuHome.FUNZ_SEMANTICA_ABSTRACT)) {

				AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();

				if (livello.equals("P")) {
					areaDatiPassBiblioSemanticaVO.setInserimentoPolo(true);
				} else {
					areaDatiPassBiblioSemanticaVO.setInserimentoIndice(true);
				}
				areaDatiPassBiblioSemanticaVO.setBidPartenza(elementoTree
						.getKey());
				areaDatiPassBiblioSemanticaVO.setDescPartenza(elementoTree
						.getDescription());

				areaDatiPassBiblioSemanticaVO.setTreeElement(elementoTree);

				request.setAttribute(
						NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
						areaDatiPassBiblioSemanticaVO);

				if (livello.equals("I"))
					request.setAttribute(
							NavigazioneSemantica.LIVELLO_RICERCA_POLO, false);
				else
					request.setAttribute(
							NavigazioneSemantica.LIVELLO_RICERCA_POLO, true);

				request.setAttribute("MODULO_VAI_A", MenuHome.MODULE_SEMANTICA);
				request.setAttribute("FUNZIONE_VAI_A", cod);
			}
			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}

	public ActionForward getDocumentoFisicoAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree) throws Exception {
		if (ValidazioneDati.isFilled(cod)) {
			request.setAttribute("MODULO_VAI_A",
					MenuHome.MODULE_DOCUMENTO_FISICO);
			request.setAttribute("FUNZIONE_VAI_A", cod);
			// Lista inventari del titolo
			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			request.setAttribute("reticolo", elementoTree);

			// Colloc. veloce
			if (cod.equals(MenuHome.FUNZ_DOCUMENTO_FISICO_COLLOC_VELOCE))
				request.setAttribute("prov", "CV");

			Navigation navi = Navigation.getInstance(request);
			request.setAttribute("codBib", navi.getUtente().getCodBib());
			request.setAttribute("descrBib", navi.getUtente().getBiblioteca());
			request.setAttribute("titolo", elementoTree.getDescription());
			// Modifica almaviva2 del 07.07.2010 richiesta Documento Fisico - serve anche il flag di loc
			request.setAttribute("flagLoc", elementoTree.isFlagCondiviso());

			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}

	public ActionForward getPossessoriAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree) throws Exception {
		if (ValidazioneDati.isFilled(cod)) {
			request.setAttribute("MODULO_VAI_A", MenuHome.MODULE_POSSESSORI);
			request.setAttribute("FUNZIONE_VAI_A", cod);

			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			request.setAttribute("reticolo", elementoTree);
			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}

	public ActionForward getStampeAction(HttpServletRequest request,
			ActionMapping mapping, String cod,
			TreeElementViewTitoli elementoTree) throws Exception {
		if (ValidazioneDati.isFilled(cod)) {
			request.setAttribute("MODULO_VAI_A", MenuHome.MODULE_STAMPE);
			request.setAttribute("FUNZIONE_VAI_A", cod);

			request.setAttribute("bid", elementoTree.getKey());
			request.setAttribute("desc", elementoTree.getDescription());
			if (cod.equals(MenuHome.FUNZ_LEGA_EDITORE)) {
				request.setAttribute("creazLegameTitEdit", "SI");
				request.setAttribute("editore", "SI");
			}
			return mapping.findForward("vaiA");
		}
		throw new Exception();
	}
}
