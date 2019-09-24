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
package it.iccu.sbn.web.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboSoloDescVO;
import it.iccu.sbn.ejb.vo.servizi.common.CodiceEleVO;

import java.util.ArrayList;
import java.util.List;


public class CaricamentoCombo {

	//--------------------------------------------------------------------------
	//	 AREA GESTIONE BIBLIOGRAFICA
	//--------------------------------------------------------------------------

	public List loadElencoOrganico(List listaCodici) {

		List lista = new ArrayList();
		ComboCodDescVO codDesc;
		for ( int i=0; i<listaCodici.size(); i++ ) {
			codDesc = new ComboCodDescVO();
			TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
			if (tracciatoCodici.getCd_tabella() == null) {
				codDesc.setCodice("");
			} else {
				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
				if (tracciatoCodici.getDs_tabella() == null) {
					codDesc.setDescrizione("");
				} else {
					codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
				}
				lista.add(codDesc);
			}
		}
		return lista;
	}



	public List loadComboCodiciDesc(List listaCodici) {
		List lista = new ArrayList();
		ComboCodDescVO codDesc;
		int size = listaCodici.size();
		for (int i = 0; i < size; i++) {
			codDesc = new ComboCodDescVO();
			TB_CODICI tracciatoCodici = (TB_CODICI) listaCodici.get(i);
			if (tracciatoCodici.getCd_tabella() == null)
				codDesc.setCodice("");
			else
				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());

			if (tracciatoCodici.getDs_tabella() == null)
				codDesc.setDescrizione("");
			else
				codDesc.setDescrizione(tracciatoCodici.getDs_tabella());

			lista.add(codDesc);
		}
		return lista;
	}

//	public List loadComboCodiciOrdTitoli(List listaCodici, String primaCall) {
//		List lista = new ArrayList();
//		ComboCodDescVO codDesc;
//		int size = listaCodici.size();
//		for (int i = 0; i < size; i++) {
//			codDesc = new ComboCodDescVO();
//			TB_CODICI tracciatoCodici = (TB_CODICI) listaCodici.get(i);
//			if (tracciatoCodici.getCd_tabella() == null)
//				codDesc.setCodice("");
//			else
//				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
//
//			if (tracciatoCodici.getDs_tabella() == null)
//				codDesc.setDescrizione("");
//			else
//				codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
//
//			if (primaCall.equals("NO")) {
//				lista.add(codDesc);
//			} else {
//				if (tracciatoCodici.getCd_flg2().equals("SI")) {
//					lista.add(codDesc);
//				}
//			}
//		}
//		return lista;
//	}

	public List loadComboCodiciDescACQ(List listaCodici) {
		List lista = new ArrayList();
		ComboCodDescVO codDesc;
		for ( int i=0; i<listaCodici.size(); i++ ) {
			codDesc = new ComboCodDescVO();
			TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
			if (tracciatoCodici.getCd_tabella() == null) {
				codDesc.setCodice("");
			} else {
				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
			}
			if (tracciatoCodici.getDs_tabella() == null) {
				codDesc.setDescrizione("");
				if (codDesc.getCodice().equals(""))
				{
					codDesc.setDescrizione("-");
				}
			} else {
				codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
				if (codDesc.getCodice().equals(""))
				{
					codDesc.setDescrizione("-");
				}
			}
			lista.add(codDesc);
		}
		return lista;
	}


	public List loadComboCodiciDescNatura(List listaCodici, String tipoFiltro, String natura) {
		List lista = new ArrayList();
		ComboCodDescVO codDesc;

		// Intervento almaviva2 12.01.2012 Bug Mantis 3932 (collaudo)
		// inserita riga vuota nella lista nature per i casi in cui in creazione titolo non c'è natura di default ne
		// è stata inserita una natura nella previa fase di Interrogazione
		lista.add(new ComboCodDescVO());

		for ( int i=0; i<listaCodici.size(); i++ ) {
			codDesc = new ComboCodDescVO();
			TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
			if (tracciatoCodici.getCd_tabella() == null) {
				codDesc.setCodice("");
			} else {
				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
			}
			if (tracciatoCodici.getDs_tabella() == null) {
				codDesc.setDescrizione("");
			} else {
				codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
			}

			if (tipoFiltro == null || tipoFiltro.equals("")) {
//				if (tipoFiltro == null || tipoFiltro.equals("") || tipoFiltro.equals("Titolo_infe")) {
				lista.add(codDesc);
			} else if (tipoFiltro.equals("Titolo_base")) {
				if (codDesc.getCodice().equals("M")
						|| codDesc.getCodice().equals("S")
						|| codDesc.getCodice().equals("A")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Titolo_coll")) {
				//if (!codDesc.getCodice().equals("W") && !codDesc.getCodice().equals("N")) {
				if (!codDesc.getCodice().equals("W")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Titolo_infe")) {
				//if (codDesc.getCodice().equals("W") || codDesc.getCodice().equals("M") || codDesc.getCodice().equals("N")) {
				if (codDesc.getCodice().equals("") || codDesc.getCodice().equals("W") || codDesc.getCodice().equals("M")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Titolo_spoglio")) {
				//if (codDesc.getCodice().equals("W") || codDesc.getCodice().equals("M") || codDesc.getCodice().equals("N")) {
				if (codDesc.getCodice().equals("N")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Rinvio")) {
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
				if (codDesc.getCodice().equals("V")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Cambio_natura")) {
				if (codDesc.getCodice().equals("A") || codDesc.getCodice().equals("B")) {
					lista.add(codDesc);
				}
			}  else if (tipoFiltro.equals("Solo_descr")) {
				if (codDesc.getCodice().equals(natura)) {
					lista.add(codDesc);
				}
				// Modifica almaviva2 del 19.04.2012 - BUG MANTIS 4841 (collaudo) RACCOLTE FATTIZIE si creano solo in locale
//			} else if (tipoFiltro.equals("Solo_docum")) {
			} else if (tipoFiltro.equals("Solo_docum_condiviso")) {
				// Modifica almaviva2 del 20.10.2010 BUG Mantis 3932
				// Inserito else if: tutte le creazioni che si possono attivare dal tasto Crea dell'interrogazione Titolo e Sintetiche Titolo
				// (quindi non attivate nell'ambito di una sessione di vai a) possono essere SOLO nature M, S, C
				// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le collane
				if (codDesc.getCodice().equals("M")
						|| codDesc.getCodice().equals("S")
//						|| codDesc.getCodice().equals("R")
						|| codDesc.getCodice().equals("C")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Solo_docum_locale")) {
				// Modifica almaviva2 del 20.10.2010 BUG Mantis 3932
				// Inserito else if: tutte le creazioni che si possono attivare dal tasto Crea dell'interrogazione Titolo e Sintetiche Titolo
				// (quindi non attivate nell'ambito di una sessione di vai a) possono essere SOLO nature M, S, C
				// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le collane
				if (codDesc.getCodice().equals("M")
						|| codDesc.getCodice().equals("S")
						|| codDesc.getCodice().equals("R")
						|| codDesc.getCodice().equals("C")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Solo_documenti_per_stampe")) {
				// Intervento del 11.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
				if (codDesc.getCodice().equals("M")
						|| codDesc.getCodice().equals("S")
						|| codDesc.getCodice().equals("N")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Solo_nature_per_editori")) {
				// Intervento Maggio 2013: Nella tabella Natura andrebbero riportate soltanto le nature M (che comprende W) e S e C (?)
				if (codDesc.getCodice().equals("M")
						|| codDesc.getCodice().equals("S")
						|| codDesc.getCodice().equals("C")) {
					lista.add(codDesc);
				}
			} else if (tipoFiltro.equals("Legami_su_A")) {
				// Inizio Modifica almaviva2 del 20.09.2019 BUG Mantis 7119
				// quando si proviene da creazione nuodo documento a seguito di un crea legame effettuato su un titolo
				// di natura A gli oggetti che è possibile creare sono solo D e P e A
				if (codDesc.getCodice().equals("D")
						|| codDesc.getCodice().equals("P")
						|| codDesc.getCodice().equals("A")) {
					lista.add(codDesc);
				}
			}
		}
		return lista;
	}

	// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
	public List loadComboCodiciDescGenere(List listaCodici, String tipoFiltro) {
		List lista = new ArrayList();
		ComboCodDescVO codDesc;
		lista.add(new ComboCodDescVO());

		for ( int i=0; i<listaCodici.size(); i++ ) {
			codDesc = new ComboCodDescVO();
			TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
			if (tracciatoCodici.getCd_tabella() == null) {
				codDesc.setCodice("");
			} else {
				codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
			}
			if (tracciatoCodici.getDs_tabella() == null) {
				codDesc.setDescrizione("");
			} else {
				codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
			}

			if (tipoFiltro == null || tipoFiltro.equals("")) {
				lista.add(codDesc);
			} else if (tipoFiltro.equals("E") && codDesc.getCodice().length()==2) {
				lista.add(codDesc);
			} else if (!tipoFiltro.equals("E") && codDesc.getCodice().length()==1) {
				lista.add(codDesc);
			}
		}
		return lista;
	}



	public List loadComboDesc(List listaCodici) {
	List lista = new ArrayList();
	ComboSoloDescVO desc;
	for ( int i=0; i<listaCodici.size(); i++ ) {
		desc = new ComboSoloDescVO();
		TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
		desc.setDescrizione(tracciatoCodici.getDs_tabella());
		lista.add(desc);
	}
	return lista;
}

	//--------------------------------------------------------------------------
	//	 AREA DOCUMENTO FISICO
	//--------------------------------------------------------------------------


	public List loadCodiceDesc(List listaCodici) {
		Object tracciato = null;
		List lista = new ArrayList();
		CodiceVO codDesc;
		ComboVO codDesc1;
		for ( int i=0; i<listaCodici.size(); i++ ) {
			tracciato = listaCodici.get(i);
			if (tracciato instanceof TB_CODICI) {
				codDesc = new CodiceVO();
				TB_CODICI tracciatoCodici = (TB_CODICI)listaCodici.get(i);

				if (tracciatoCodici.getCd_tabella() == null) {
					codDesc.setCodice("");
				} else {
					codDesc.setCodice(tracciatoCodici.getCd_tabella().trim());
				}
				if (tracciatoCodici.getDs_tabella() == null) {
					codDesc.setDescrizione("");
				} else {
					codDesc.setDescrizione(tracciatoCodici.getDs_tabella());
				}
				if (tracciatoCodici.getCd_flg2() != null){
					codDesc.setTerzo(tracciatoCodici.getCd_flg2());
				}
				lista.add(codDesc);
			}else if (tracciato instanceof CodiceVO) {
				codDesc = new CodiceVO();
				CodiceVO tracciatoCodici = (CodiceVO)listaCodici.get(i);

				if (tracciatoCodici.getCodice() == null) {
					codDesc.setCodice("");
				} else {
					codDesc.setCodice(tracciatoCodici.getCodice().trim());
				}
				if (tracciatoCodici.getDescrizione() == null) {
					codDesc.setDescrizione("");
				} else {
					codDesc.setDescrizione(tracciatoCodici.getDescrizione());
				}
				lista.add(codDesc);
			}else if (tracciato instanceof ComboVO) {
				codDesc1 = new ComboVO();
				ComboVO tracciatoCodici = (ComboVO)listaCodici.get(i);

				if (tracciatoCodici.getCodice() == null) {
					codDesc1.setCodice("");
				} else {
					codDesc1.setCodice(tracciatoCodici.getCodice());
				}
				if (tracciatoCodici.getDescrizione() == null) {
					codDesc1.setDescrizione("");
				} else {
					codDesc1.setDescrizione(tracciatoCodici.getDescrizione());
				}
				lista.add(codDesc1);
			}else if (tracciato instanceof CodiceVO) {
				codDesc = new CodiceVO();
				CodiceVO tracciatoCodici = (CodiceVO)listaCodici.get(i);

				if (tracciatoCodici.getCodice() == null) {
					codDesc.setCodice("");
				} else {
					codDesc.setCodice(tracciatoCodici.getCodice());
				}
				if (tracciatoCodici.getDescrizione() == null) {
					codDesc.setDescrizione("");
				} else {
					codDesc.setDescrizione(tracciatoCodici.getDescrizione());
				}
				lista.add(codDesc);
			}
		}
		return lista;
	}

	public List loadDesc(List listaCodici) {
	List lista = new ArrayList();
	CodiceVO desc;
	for ( int i=0; i<listaCodici.size(); i++ ) {
		desc = new CodiceVO();
		TB_CODICI tracciatoCodici =(TB_CODICI)listaCodici.get(i);
		desc.setDescrizione(tracciatoCodici.getDs_tabella());
		lista.add(desc);
		}
	return lista;
	}
	public List loadCodDescPezzi(List listaFormati) {
		List lista = new ArrayList();
		FormatiSezioniVO formatoSezione;
		FormatiSezioniVO formati;
		lista.add(new FormatiSezioniVO());
		for ( int i=0; i<listaFormati.size(); i++ ) {
			formati = new FormatiSezioniVO();
			formatoSezione = new FormatiSezioniVO();
			if (listaFormati.get(i) instanceof FormatiSezioniVO) {
				FormatiSezioniVO forSez = (FormatiSezioniVO) listaFormati.get(i);
				formati.setCodPolo(forSez.getCodPolo());
				formati.setCodBib(forSez.getCodBib());
				formati.setCodSez(forSez.getCodSez());
				formati.setCodFormato(forSez.getCodFormato());
				formati.setDescrFor(forSez.getDescrFor());
				formati.setNumeroPezzi(forSez.getNumeroPezzi());
			}else{
			formati =(FormatiSezioniVO)listaFormati.get(i);
			}
			formatoSezione.setCodFormato(formati.getDescrizioneEstesa());//+ String.valueOf(tracciatoFormati.getNumPezzi()));
			lista.add(formatoSezione);
//			lista.add(new CodiceVO("____", "_________________________", "_______"));
			}
		return lista;
		}
 	public List loadCodice(List listaSerie) {
		List lista = new ArrayList();
		SerieVO tracciatoSerie = new SerieVO();
		CodiceVO cod;
		for ( int i=0; i<listaSerie.size(); i++ ) {
			cod = new CodiceVO();
			tracciatoSerie =(SerieVO)listaSerie.get(i);
			cod.setCodice(tracciatoSerie.getCodSerie().trim());
			lista.add(cod);
			}
		return lista;
		}
 	public List loadBuoniCarico(List listaSerie) {
		List lista = new ArrayList();
		SerieVO tracciatoSerie = new SerieVO();
		CodiceVO cod;
		for ( int i=0; i<listaSerie.size(); i++ ) {
			cod = new CodiceVO();
			tracciatoSerie =(SerieVO)listaSerie.get(i);
			cod.setCodice(tracciatoSerie.getBuonoCarico());
			lista.add(cod);
			}
		return lista;
		}
	//--------------------------------------------------------------------------
	//	 fine AREA DOCUMENTO FISICO
	//--------------------------------------------------------------------------


	public List loadNumStandard() {
		List lista = new ArrayList();
		ComboCodDescVO numstandard = new ComboCodDescVO();
		numstandard.setCodice("");
		numstandard.setDescrizione("");
		lista.add(numstandard);
		numstandard = new ComboCodDescVO();
		numstandard.setCodice("I");
		numstandard.setDescrizione("ISBN");
		lista.add(numstandard);
		numstandard = new ComboCodDescVO();
		numstandard.setCodice("M");
		numstandard.setDescrizione("ISMN");
		lista.add(numstandard);
		return lista;

	}

	public List loadListaNature() {
		List lista = new ArrayList();
		ComboCodDescVO listaNature = new ComboCodDescVO();
		listaNature.setCodice("");
		listaNature.setDescrizione("");
		lista.add(listaNature);
		listaNature = new ComboCodDescVO();
		listaNature.setCodice("Ac");
		listaNature.setDescrizione("TITOLO DI COMPOSIZIONE");
		lista.add(listaNature);
		listaNature = new ComboCodDescVO();
		listaNature.setCodice("Au");
		listaNature.setDescrizione("TITOLO UNIFORME");
		lista.add(listaNature);
		listaNature = new ComboCodDescVO();
		listaNature.setCodice("C");
		listaNature.setDescrizione("COLLANA");
		lista.add(listaNature);
		listaNature = new ComboCodDescVO();
		listaNature.setCodice("D");
		listaNature.setDescrizione("-------");
		lista.add(listaNature);
		listaNature = new ComboCodDescVO();
		listaNature.setCodice("M");
		listaNature.setDescrizione("MONOGRAFIA");
		lista.add(listaNature);

		return lista;
	}

	public List loadSottonatureD() {
		List lista = new ArrayList();
		ComboCodDescVO listaSottoNature = new ComboCodDescVO();
		listaSottoNature.setCodice("");
		listaSottoNature.setDescrizione("");
		lista.add(listaSottoNature);
		listaSottoNature = new ComboCodDescVO();
		listaSottoNature.setCodice("M");
		listaSottoNature.setDescrizione("INCIPIT");
		lista.add(listaSottoNature);
		listaSottoNature = new ComboCodDescVO();
		listaSottoNature.setCodice("S");
		listaSottoNature.setDescrizione("TITOLO ALTERNATIVO");
		lista.add(listaSottoNature);

		return lista;
	}

	public List loadTipoData() {
		List lista = new ArrayList();
		ComboCodDescVO listaTipoData = new ComboCodDescVO();
		listaTipoData.setCodice("");
		listaTipoData.setDescrizione("");
		lista.add(listaTipoData);
		listaTipoData = new ComboCodDescVO();
		listaTipoData.setCodice("F");
		listaTipoData.setDescrizione("INCERTA");
		lista.add(listaTipoData);
		listaTipoData = new ComboCodDescVO();
		listaTipoData.setCodice("0");
		listaTipoData.setDescrizione("......");
		lista.add(listaTipoData);

		return lista;
	}

	public List loadLingue() {
		List lista = new ArrayList();
		ComboCodDescVO listaLingue = new ComboCodDescVO();
		listaLingue.setCodice("");
		listaLingue.setDescrizione("");
		lista.add(listaLingue);
		listaLingue = new ComboCodDescVO();
		listaLingue.setCodice("ITA");
		listaLingue.setDescrizione("ITALIANO");
		lista.add(listaLingue);
		listaLingue = new ComboCodDescVO();
		listaLingue.setCodice("0");
		listaLingue.setDescrizione("......");
		lista.add(listaLingue);

		return lista;
	}

	public List loadPaesi() {
		List lista = new ArrayList();
		ComboCodDescVO listaPaese = new ComboCodDescVO();
		listaPaese.setCodice("");
		listaPaese.setDescrizione("");
		lista.add(listaPaese);
		listaPaese = new ComboCodDescVO();
		listaPaese.setCodice("IT");
		listaPaese.setDescrizione("ITALIA");
		lista.add(listaPaese);
		listaPaese = new ComboCodDescVO();
		listaPaese.setCodice("0");
		listaPaese.setDescrizione("......");
		lista.add(listaPaese);

		return lista;
	}

	public List loadResponsabilita() {
		List lista = new ArrayList();
		ComboCodDescVO listaResponsabilita = new ComboCodDescVO();
		listaResponsabilita.setCodice("");
		listaResponsabilita.setDescrizione("");
		lista.add(listaResponsabilita);
		listaResponsabilita = new ComboCodDescVO();
		listaResponsabilita.setCodice("0");
		listaResponsabilita.setDescrizione("NOME CITATO");
		lista.add(listaResponsabilita);
		listaResponsabilita = new ComboCodDescVO();
		listaResponsabilita.setCodice("0");
		listaResponsabilita.setDescrizione("......");
		lista.add(listaResponsabilita);

		return lista;
	}

	public List loadRelazioni() {
		List lista = new ArrayList();
		ComboCodDescVO listaRelazioni = new ComboCodDescVO();
		listaRelazioni.setCodice("");
		listaRelazioni.setDescrizione("");
		lista.add(listaRelazioni);
		listaRelazioni = new ComboCodDescVO();
		listaRelazioni.setCodice("010");
		listaRelazioni.setDescrizione("ADATTATORE");
		lista.add(listaRelazioni);
		listaRelazioni = new ComboCodDescVO();
		listaRelazioni.setCodice("0");
		listaRelazioni.setDescrizione("......");
		lista.add(listaRelazioni);

		return lista;
	}

	public List loadTipiRecord() {
		List lista = new ArrayList();
		ComboCodDescVO listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("");
		listaTipiRecord.setDescrizione("");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("a");
		listaTipiRecord.setDescrizione("TESTO A STAMPA");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("b");
		listaTipiRecord.setDescrizione("TESTO MANOSCRITTO");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("c");
		listaTipiRecord.setDescrizione("MUSICA A STAMPA");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("e");
		listaTipiRecord.setDescrizione("CARTOGRAFIA A STAMPA");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("k");
		listaTipiRecord.setDescrizione("MATERIALE GRAFICO");
		lista.add(listaTipiRecord);
		listaTipiRecord = new ComboCodDescVO();
		listaTipiRecord.setCodice("0");
		listaTipiRecord.setDescrizione("..........");
		lista.add(listaTipiRecord);
		return lista;
	}

	public List loadTipiOrdinam() {
		List lista = new ArrayList();
		ComboSoloDescVO listaTipiOrdinam = new ComboSoloDescVO();
		listaTipiOrdinam.setDescrizione("TITOLO + DATA1");
		lista.add(listaTipiOrdinam);
		listaTipiOrdinam = new ComboSoloDescVO();
		listaTipiOrdinam.setDescrizione("DATA1 + TITOLO");
		lista.add(listaTipiOrdinam);
		listaTipiOrdinam = new ComboSoloDescVO();
		listaTipiOrdinam.setDescrizione("IDENTIFICATIVO");
		lista.add(listaTipiOrdinam);

		return lista;
	}

	public List loadFormatoLista() {
		List lista = new ArrayList();
		ComboSoloDescVO listaFormatoLista = new ComboSoloDescVO();
		listaFormatoLista.setDescrizione("MIN");
		lista.add(listaFormatoLista);
		listaFormatoLista = new ComboSoloDescVO();
		listaFormatoLista.setDescrizione("MAX");
		lista.add(listaFormatoLista);

		return lista;
	}

	public List loadBiblioteche() {
		List lista = new ArrayList();
		ComboCodDescVO biblioteche = new ComboCodDescVO();
		biblioteche.setCodice("");
		biblioteche.setDescrizione("");
		lista.add(biblioteche);
		biblioteche = new ComboCodDescVO();
		biblioteche.setCodice("POLO");
		biblioteche.setDescrizione("POLO");
		lista.add(biblioteche);
		biblioteche = new ComboCodDescVO();
		biblioteche.setCodice("01");
		biblioteche.setDescrizione("CENTRO SISTEMA");
		lista.add(biblioteche);
		biblioteche = new ComboCodDescVO();
		biblioteche.setCodice("* 02");
		biblioteche.setDescrizione("BIBLIOTECA DI PROVA 02");
		lista.add(biblioteche);
		biblioteche = new ComboCodDescVO();
		biblioteche.setCodice("03");
		biblioteche.setDescrizione("BIBLIOTECA DI PROVA 03");
		lista.add(biblioteche);

		return lista;
	}

	public List loadLivelloAutor() {
		List lista = new ArrayList();
		ComboCodDescVO livAutor = new ComboCodDescVO();
		livAutor.setCodice("");
		livAutor.setDescrizione("");
		lista.add(livAutor);
		livAutor = new ComboCodDescVO();
		livAutor.setCodice("05");
		livAutor.setDescrizione("minima");
		lista.add(livAutor);
		livAutor = new ComboCodDescVO();
		livAutor.setCodice("99");
		livAutor.setDescrizione("massima");
		lista.add(livAutor);

		return lista;
	}

	public List loadCitazStandard() {
		List lista = new ArrayList();
		ComboCodDescVO citStand = new ComboCodDescVO();
		citStand.setCodice("");
		citStand.setDescrizione("");
		lista.add(citStand);
		return lista;
	}


// Metodi di caricamento Combo Area Musicale

	public List loadElaborazione() {
		List lista = new ArrayList();
		ComboCodDescVO elaboraz = new ComboCodDescVO();
		elaboraz.setCodice("");
		elaboraz.setDescrizione("");
		lista.add(elaboraz);
		elaboraz = new ComboCodDescVO();
		elaboraz.setCodice("A");
		elaboraz.setDescrizione("ADATTAMENTO");
		lista.add(elaboraz);
		elaboraz = new ComboCodDescVO();
		elaboraz.setCodice("M");
		elaboraz.setDescrizione("ARMONIZZAZIONE");
		lista.add(elaboraz);

		return lista;
	}

	public List loadPresentazione() {
		List lista = new ArrayList();
		ComboCodDescVO presentaz = new ComboCodDescVO();
		presentaz.setCodice("");
		presentaz.setDescrizione("");
		lista.add(presentaz);
		presentaz = new ComboCodDescVO();
		presentaz.setCodice("CA");
		presentaz.setDescrizione("CARTINA");
		lista.add(presentaz);
		presentaz = new ComboCodDescVO();
		presentaz.setCodice("...");
		presentaz.setDescrizione("ALTRO");
		lista.add(presentaz);

		return lista;
	}

	public List loadFormaMusicale() {
		List lista = new ArrayList();
		ComboCodDescVO formaMus = new ComboCodDescVO();
		formaMus.setCodice("");
		formaMus.setDescrizione("");
		lista.add(formaMus);
		formaMus = new ComboCodDescVO();
		formaMus.setCodice("und");
		formaMus.setDescrizione("forma");
		lista.add(formaMus);
		formaMus = new ComboCodDescVO();
		formaMus.setCodice("...");
		formaMus.setDescrizione("ALTRO");
		lista.add(formaMus);

		return lista;
	}

	public List loadTonalita() {
		List lista = new ArrayList();
		ComboCodDescVO tonalita = new ComboCodDescVO();
		tonalita.setCodice("");
		tonalita.setDescrizione("");
		lista.add(tonalita);
		tonalita = new ComboCodDescVO();
		tonalita.setCodice("e");
		tonalita.setDescrizione("MI maggiorew");
		lista.add(tonalita);
		tonalita = new ComboCodDescVO();
		tonalita.setCodice("...");
		tonalita.setDescrizione("ALTRO");
		lista.add(tonalita);

		return lista;
	}

// Metodi di caricamento Combo Area Cartografia

	public List loadMeridianoOrigine() {
		List lista = new ArrayList();
		ComboCodDescVO meridOrig = new ComboCodDescVO();
		meridOrig.setCodice("");
		meridOrig.setDescrizione("");
		lista.add(meridOrig);
		meridOrig = new ComboCodDescVO();
		meridOrig.setCodice("ab");
		meridOrig.setDescrizione("AMSTERDAM");
		lista.add(meridOrig);
		meridOrig = new ComboCodDescVO();
		meridOrig.setCodice("A");
		meridOrig.setDescrizione("ALTRO");
		lista.add(meridOrig);

		return lista;
	}

//	 Metodi di caricamento Combo Area Grafica

	public List loadDesignSpecMater() {
		List lista = new ArrayList();
		ComboCodDescVO desMat = new ComboCodDescVO();
		desMat.setCodice("");
		desMat.setDescrizione("");
		lista.add(desMat);
		desMat = new ComboCodDescVO();
		desMat.setCodice("a");
		desMat.setDescrizione("collage");
		lista.add(desMat);
		desMat = new ComboCodDescVO();
		desMat.setCodice("A");
		desMat.setDescrizione("ALTRO");
		lista.add(desMat);

		return lista;
	}

	public List loadSupportoPrimario() {
		List lista = new ArrayList();
		ComboCodDescVO supPrim = new ComboCodDescVO();
		supPrim.setCodice("");
		supPrim.setDescrizione("");
		lista.add(supPrim);
		supPrim = new ComboCodDescVO();
		supPrim.setCodice("b");
		supPrim.setDescrizione("bristol");
		lista.add(supPrim);
		supPrim = new ComboCodDescVO();
		supPrim.setCodice("A");
		supPrim.setDescrizione("ALTRO");
		lista.add(supPrim);

		return lista;
	}

	public List loadIndicatoreColore() {
		List lista = new ArrayList();
		ComboCodDescVO indCol = new ComboCodDescVO();
		indCol.setCodice("");
		indCol.setDescrizione("");
		lista.add(indCol);
		indCol = new ComboCodDescVO();
		indCol.setCodice("b");
		indCol.setDescrizione("bianco e nero");
		lista.add(indCol);
		indCol = new ComboCodDescVO();
		indCol.setCodice("A");
		indCol.setDescrizione("ALTRO");
		lista.add(indCol);

		return lista;
	}

	public List loadIndicatoreTecnica() {
		List lista = new ArrayList();
		ComboCodDescVO indTec = new ComboCodDescVO();
		indTec.setCodice("");
		indTec.setDescrizione("");
		lista.add(indTec);
		indTec = new ComboCodDescVO();
		indTec.setCodice("bh");
		indTec.setDescrizione("acquaforte");
		lista.add(indTec);
		indTec = new ComboCodDescVO();
		indTec.setCodice("A");
		indTec.setDescrizione("ALTRO");
		lista.add(indTec);

		return lista;
	}

	public List loadDesignatoreFunzione() {
		List lista = new ArrayList();
		ComboCodDescVO desFun = new ComboCodDescVO();
		desFun.setCodice("");
		desFun.setDescrizione("");
		lista.add(desFun);
		desFun = new ComboCodDescVO();
		desFun.setCodice("af");
		desFun.setDescrizione("biglietto augurale");
		lista.add(desFun);
		desFun = new ComboCodDescVO();
		desFun.setCodice("A");
		desFun.setDescrizione("ALTRO");
		lista.add(desFun);

		return lista;
	}

	// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
	// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
	// In creazione il default è REICAT.
	// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
	// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
	public List loadListaNormeCatalografiche() {
		List lista = new ArrayList();
		ComboCodDescVO codDesc;
		codDesc = new ComboCodDescVO();
		codDesc.setCodice("0");
		codDesc.setDescrizione("");
		lista.add(codDesc);
		codDesc = new ComboCodDescVO();
		codDesc.setCodice("1");
		codDesc.setDescrizione("REICAT");
		lista.add(codDesc);
		codDesc = new ComboCodDescVO();
		codDesc.setCodice("2");
		codDesc.setDescrizione("RICA");
		lista.add(codDesc);
		return lista;
	}



	//--------------------------------------------------------------------------
	//	 FINE AREA GESTIONE BIBLIOGRAFICA
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	//	 AREA Servizi
	//--------------------------------------------------------------------------

	public List loadEleAutor() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("01", "G Utente generico");
		lista.add(cod);
		cod = new CodiceEleVO("02", "ERA Studenti erasmus");
		lista.add(cod);
		cod = new CodiceEleVO("03", "MUS Musicista");
		lista.add(cod);
		cod = new CodiceEleVO("04", "PD1 Docente interno");
		lista.add(cod);
		//this.listaelementi.setTipoAutor(lista);
		return lista;
	}

	public List loadEleProvenienza() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("01", "Erasmus");
		lista.add(cod);
		cod = new CodiceEleVO("02", "Provincia");
		lista.add(cod);
		cod = new CodiceEleVO("03", "Regione");
		lista.add(cod);
		cod = new CodiceEleVO("04", "Non dichiarata");
		lista.add(cod);
		cod = new CodiceEleVO("05", "Altro");
		lista.add(cod);
		cod = new CodiceEleVO("06", "Comune");
		lista.add(cod);
		//this.listaelementi.setTipoAutor(lista);
		return lista;
	}

	public List loadEleProfessioni() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("A", "Artigiano");
		lista.add(cod);
		cod = new CodiceEleVO("C", "Casalinga");
		lista.add(cod);
		cod = new CodiceEleVO("G", "Agricoltore");
		lista.add(cod);
		cod = new CodiceEleVO("U", "Docente Universitario");
		lista.add(cod);
		cod = new CodiceEleVO("X", "Non dichiarato");
		lista.add(cod);
		return lista;
	}

	public List loadEleOccupazioni() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("01", "Bibliotecario");
		lista.add(cod);
		cod = new CodiceEleVO("02", "Lqvoro temporaneo");
		lista.add(cod);
		cod = new CodiceEleVO("03", "Bibliotecario");
		lista.add(cod);
		return lista;
	}

	public List loadEleSpecTitoloStudio() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("01", "Master in catalogazione");
		lista.add(cod);
		cod = new CodiceEleVO("02", "Scienza dell'informazione");
		lista.add(cod);
		cod = new CodiceEleVO("03", "Scienza della comunicazione");
		lista.add(cod);
		return lista;
	}

	public List loadEleTipoPersonalita() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("01", "Ente morale");
		lista.add(cod);
		cod = new CodiceEleVO("02", "biblioteca Statale");
		lista.add(cod);
		cod = new CodiceEleVO("03", "Università");
		lista.add(cod);
		return lista;
	}

	public List loadEleAteneo() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("NA", "Università di Napoli");
		lista.add(cod);
		cod = new CodiceEleVO("SA", "La Sapienza di Roma");
		lista.add(cod);
		return lista;
	}


	public List loadEleNazCitta() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("ZA", "Africa del sud");
		lista.add(cod);
		cod = new CodiceEleVO("AF", "Afghanistan");
		lista.add(cod);
		cod = new CodiceEleVO("UK", "Regno Unito");
		lista.add(cod);
		cod = new CodiceEleVO("IT", "Italia");
		lista.add(cod);
		return lista;
	}

	public List loadEleProvinciaResidenza() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("AG", "Agrigento");
		lista.add(cod);
		cod = new CodiceEleVO("RM", "Roma");
		lista.add(cod);
		cod = new CodiceEleVO("AL", "Alessandria");
		lista.add(cod);
		return lista;
	}
	public List loadEleTipoDocumento() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("Codice Fiscale", "Codice Fiscale");
		lista.add(cod);
		cod = new CodiceEleVO("Patente", "Patente");
		lista.add(cod);
		cod = new CodiceEleVO("Passaporto", "Passaporto");
		lista.add(cod);
		cod = new CodiceEleVO("Carta d'Identità", "Carta d'Identità");
		lista.add(cod);
		cod = new CodiceEleVO("Libretto Universitario", "Libretto Universitario");
		lista.add(cod);
		return lista;
	}
/*
	public List loadEleAutorizzazione() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("001", "Studente");
		lista.add(cod);
		cod = new CodiceEleVO("002", "Amici dei Libri");
		lista.add(cod);
		cod = new CodiceEleVO("003", "Impiegati");
		lista.add(cod);
		cod = new CodiceEleVO("004", "Ragazzi");
		lista.add(cod);
		cod = new CodiceEleVO("005", "Universitari");
		lista.add(cod);
		return lista;
	}
*/
	public List loadEleAutomaticoX() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("001", "Studente");
		lista.add(cod);
		cod = new CodiceEleVO("002", "Operatore i campo culturale");
		lista.add(cod);
		cod = new CodiceEleVO("003", "Impiegato");
		lista.add(cod);
		cod = new CodiceEleVO("004", "Docente universitario");
		lista.add(cod);
		cod = new CodiceEleVO("005", "Dottorando in ricerca");
		lista.add(cod);
		cod = new CodiceEleVO("006", "Pensionato");
		lista.add(cod);
		return lista;
	}
	/*
	public List loadEleTitoloStudio() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("D", "Diploma scuola media superiore");
		lista.add(cod);
		cod = new CodiceEleVO("U", "Diploma universitario");
		lista.add(cod);
		cod = new CodiceEleVO("L", "Laurea");
		lista.add(cod);
		return lista;
	}

	public List loadEleFruizione() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("M", "Consultazione");
		lista.add(cod);
		cod = new CodiceEleVO("D", "Consultazione rari");
		lista.add(cod);
		cod = new CodiceEleVO("P", "Consultazione fotoriproduzioni");
		lista.add(cod);
		cod = new CodiceEleVO("X", "Consultazione prestito");
		lista.add(cod);
		cod = new CodiceEleVO("Q", "Consultazione riproduzione");
		lista.add(cod);
		cod = new CodiceEleVO("B", "Solo consultazione");
		lista.add(cod);
		cod = new CodiceEleVO("A", "Tutti i servizi");
		lista.add(cod);
		return lista;
	}

	public List loadEleIndisponibilita() throws Exception {
		List lista = new ArrayList();
		CodiceEleVO cod = new CodiceEleVO("A", "Alluvionato");
		lista.add(cod);
		cod = new CodiceEleVO("D", "Danneggiato");
		lista.add(cod);
		cod = new CodiceEleVO("S", "Smarrito");
		lista.add(cod);
		cod = new CodiceEleVO("E", "Escluso dalla circolazione");
		lista.add(cod);
		cod = new CodiceEleVO("C", "In corso di trattamento");
		lista.add(cod);
		cod = new CodiceEleVO("Q", "Periodico non rilegato");
		lista.add(cod);
		cod = new CodiceEleVO("R", "In restauro");
		lista.add(cod);
		return lista;
	}
*/
	//--------------------------------------------------------------------------
	//	 FINE AREA SERVIZI
	//--------------------------------------------------------------------------



	public static final <T> List<T> cutFirst(final List<T> list) {
		if (ValidazioneDati.size(list) > 1) {

			String codice = "";
			T first = list.get(0);
			if (first instanceof TB_CODICI)
				codice = ((TB_CODICI) first).getCd_tabella();

			if (!ValidazioneDati.isFilled(codice)) {
				List<T> tmp = new ArrayList<T>(list);
				tmp.remove(0);
				return tmp;
			}
		}
		return list;
	}

	// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
	// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
	public List loadPubblicatoSiNo() {
		List lista = new ArrayList();
		ComboCodDescVO numstandard = new ComboCodDescVO();
		numstandard.setCodice("#");
		numstandard.setDescrizione("pubblicato");
		lista.add(numstandard);
		numstandard = new ComboCodDescVO();
		numstandard.setCodice("1");
		numstandard.setDescrizione("non pubblicato");
		lista.add(numstandard);
		return lista;
	}
}
