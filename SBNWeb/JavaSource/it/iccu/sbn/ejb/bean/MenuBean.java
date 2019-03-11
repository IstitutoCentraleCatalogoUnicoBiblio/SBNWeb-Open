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
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.home.MenuHome;
import it.iccu.sbn.ejb.model.tree.TreeElement;
import it.iccu.sbn.ejb.remote.Menu;
import it.iccu.sbn.ejb.utils.menu.AcqMenuAttivitaChecker;
import it.iccu.sbn.ejb.utils.menu.BaseMenuAttivitaChecker;
import it.iccu.sbn.ejb.utils.menu.BatchMenuAttivitaChecker;
import it.iccu.sbn.ejb.utils.menu.ServiziILLBibMenuChecker;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.DescrizioneFunzioneVO;
import it.iccu.sbn.web.vo.vaia.Condiviso;
import it.iccu.sbn.web.vo.vaia.LivelloRicerca;
import it.iccu.sbn.web.vo.vaia.Localizzazione;
import it.iccu.sbn.web.vo.vaia.RootReticolo;
import it.iccu.sbn.web.vo.vaia.TipoOggetto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

public class MenuBean extends AbstractStatelessSessionBean implements Menu {

	private static final long serialVersionUID = -4744398388630611888L;

    private static Logger log = Logger.getLogger(Menu.class);


	public TreeElement getRootMenu() throws EJBException {

		try {
			log.debug("getRootMenu()");
			TreeElement root;
			root = new TreeElement("menu");

			aggiungiMenu_GestioneBibliografica(root);
			aggiungiMenu_Acquisizioni(root);
			aggiungiMenu_DocumentoFisico(root);
			aggiungiMenu_Servizi(root);
			// STAMPE
			//aggiungiMenu_Stampe(root);
//			aggiungiMenu_Statistiche(root);
			aggiungiMenu_ElaborazioniDifferite(root);
			aggiungiMenu_Amministrazione(root);

			return root;

		} catch (Exception e) {
			log.error("", e);
			throw new EJBException(e);
		}
	}

	private void aggiungiMenu_Amministrazione(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("amministrazionesistema", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().AMMINISTRAZIONE) );

        menu2 = new TreeElement("gestisciutente", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().ANAGRAFICA_UTENTE));
        menu2.setUrl("/amministrazionesistema/ricercaBibliotecario.do");

        menu2 = new TreeElement("gestiscibiblioteca", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().ANAGRAFICA_BIBLIOTECA));
        menu2.setUrl("/amministrazionesistema/ricercaBiblioteca.do");

        menu2 = new TreeElement("profilazionepolo", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().PROFILAZIONE_POLO));
        menu2.setUrl("/amministrazionesistema/abilitazionePolo/profilazionePolo.do");

        menu2 = new TreeElement("defaultutente", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_DEFAULT_UTENTE));
        menu2.setUrl("/amministrazionesistema/defaultUtente.do");
        menu2 = new TreeElement("defaultbiblioteca", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_DEFAULT_BIBLIOTECA));
        menu2.setUrl("/amministrazionesistema/defaultBiblioteca.do");
        menu2 = new TreeElement("gestionecodici", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().INTERROGAZIONE_CODICI_VALIDAZIONE));
        menu2.setUrl("/amministrazionesistema/sinteticaCodici.do");
        menu2 = new TreeElement("sistemimetropolitani", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_SISTEMI_METROPOLITANI));
        menu2.setUrl("/amministrazionesistema/sistemiMetropolitani.do");
        menu2 = new TreeElement("centrisistema", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_CENTRI_SISTEMA));
        menu2.setUrl("/amministrazionesistema/centriSistema.do");
	}

	private void aggiungiMenu_ElaborazioniDifferite(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("elaborazionidifferite", root,
				new BatchMenuAttivitaChecker(CodiciAttivita.getIstance().PROCEDURE_DIFFERITE, CodiciAttivita.getIstance().STAMPE));

        menu2 = new TreeElement("stampe", menu1, new BatchMenuAttivitaChecker(TipoAttivita.STAMPA));
        menu2.setUrl("/elaborazioniDifferite/invioElaborazioniDifferite.do?type=" + TipoAttivita.STAMPA);

        menu2 = new TreeElement("invioRichieste", menu1, new BatchMenuAttivitaChecker(TipoAttivita.FUNZIONE));
        menu2.setUrl("/elaborazioniDifferite/invioElaborazioniDifferite.do?type=" + TipoAttivita.FUNZIONE);

        menu2 = new TreeElement("statistiche", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().STATISTICHE));
        menu2.setUrl("/statistiche/areeStatistiche.do?type=" + TipoAttivita.STATISTICHE);

        menu2 = new TreeElement("statoRichieste", menu1);
        menu2.setUrl("/elaborazioniDifferite/statoElaborazioniDifferite.do");
	}

//	private void aggiungiMenu_Statistiche(TreeElement root) {
//		TreeElement menu1;
//		TreeElement menu2;
//		menu1 = new TreeElement("statistiche", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().STATISTICHE));
//
//		menu2 = new TreeElement("statistiche", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().STATISTICHE));
//		menu2.setUrl("/statistiche/areeStatistiche.do");
//	}

	protected void aggiungiMenu_Stampe(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("gestionestampe", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().STAMPE));

        menu2 = new TreeElement("ingresso", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_REGISTRO_DI_INGRESSO));
        menu2.setUrl("/gestionestampe/ingresso/stampaRegistroIngresso.do");
        menu2 = new TreeElement("ordini", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_ORDINE));
        menu2.setUrl("/gestionestampe/ordini/stampaOrdini.do");
        menu2 = new TreeElement("buoni", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_BUONI_DI_CARICO));
        menu2.setUrl("/gestionestampe/buoni/stampaBuoniCarico.do");

        menu2 = new TreeElement("fornitori", menu1,
        		new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_BIBLIOTECA,
        				CodiciAttivita.getIstance().GA_STAMPA_FORNITORI_DI_POLO));
        menu2.setUrl("/gestionestampe/fornitori/stampaFornitori.do");

        menu2 = new TreeElement("etichette", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GDF_ETICHETTE));
        menu2.setUrl("/gestionestampe/etichette/stampaEtichette.do");

        menu2 = new TreeElement("conservazione", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_DI_CONSERVAZIONE));
        menu2.setUrl("/gestionestampe/conservazione/stampaRegistroConservazione.do");
        menu2 = new TreeElement("topografico", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GDF_STAMPA_REGISTRO_TOPOGRAFICO));
        menu2.setUrl("/gestionestampe/topografico/stampaRegistroTopografico.do");
        menu2 = new TreeElement("utenti", menu1);
        menu2.setUrl("/gestionestampe/utenti/stampaUtenti.do");

        menu2 = new TreeElement("biblioteche", menu1);
        menu2.setUrl("/gestionestampe/biblioteche/stampaBiblioteche.do");

        menu2 = new TreeElement("bollettario", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_BOLLETTARIO));
        menu2.setUrl("/gestionestampe/bollettario/stampaBollettario.do");

        menu2 = new TreeElement("spese", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_STAMPA_RIPARTIZIONE_SPESE));
        menu2.setUrl("/gestionestampe/spese/ripartizioneSpese.do");
	}

	private void aggiungiMenu_Servizi(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("servizi", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().SERVIZI));
        menu2 = new TreeElement("configurazione", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().SRV_CONFIGURAZIONE_ESAME));
        menu2.setUrl("/servizi/configurazione/Configurazione.do");
        menu2 = new TreeElement("erogazione", menu1);
        menu2.setUrl("/servizi/erogazione/ErogazioneRicerca.do");

        menu2 = new TreeElement("richieste_ill", menu1, new ServiziILLBibMenuChecker() );
        menu2.setUrl("/servizi/erogazione/erogazioneRicercaILL.do");

        menu2 = new TreeElement("utenti", menu1);
        menu2.setUrl("/servizi/utenti/RicercaUtenti.do");
        menu2 = new TreeElement("autorizzazioni", menu1);
        menu2.setUrl("/servizi/autorizzazioni/RicercaAutorizzazioni.do");
        menu2 = new TreeElement("segnature", menu1);
        menu2.setUrl("/servizi/segnature/RicercaSegnature.do");
        menu2 = new TreeElement("documenti", menu1);
        menu2.setUrl("/servizi/documenti/RicercaDocumenti.do");
        menu2 = new TreeElement("occupazioni", menu1);
        menu2.setUrl("/servizi/occupazioni/RicercaOccupazioni.do");
        menu2 = new TreeElement("materieinteresse", menu1);
        menu2.setUrl("/servizi/materieinteresse/RicercaMaterieInteresse.do");
        menu2 = new TreeElement("spectitolostudio", menu1);
        menu2.setUrl("/servizi/spectitolostudio/RicercaSpecTitoloStudio.do");

        //almaviva5_20160721
        menu2 = new TreeElement("sale", menu1);
        menu2.setUrl("/servizi/sale/ricercaSale.do");

	}

	private void aggiungiMenu_DocumentoFisico(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("documentofisico", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_DOCUMENTO_FISICO));
        menu2 = new TreeElement("esameCollocazioni", menu1);
        menu2.setUrl("/documentofisico/esameCollocazioni/esameCollocRicerca.do");
        menu2 = new TreeElement("sezioniCollocazioni", menu1);
        menu2.setUrl("/documentofisico/sezioniCollocazioni/sezioniCollocazioniLista.do");
        menu2 = new TreeElement("serieInventariali", menu1);
        menu2.setUrl("/documentofisico/serieInventariali/serieInventarialeLista.do");
        menu2 = new TreeElement("codiciProvenienza", menu1);
        menu2.setUrl("/documentofisico/codiciProvenienza/codiciProvenienzaLista.do");
        menu2 = new TreeElement("possessori", menu1);
        menu2.setUrl("/documentofisico/possessori/possessoriRicerca.do");
        menu2 = new TreeElement("modelliEtichette", menu1);
        menu2.setUrl("/documentofisico/modelliEtichette/modelliEtichetteLista.do");
        menu2 = new TreeElement("parametriBiblio", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GDF_PARAMETRI_BIBLIOTECA));
        menu2.setUrl("/documentofisico/parametriBiblio/parametriBiblio.do");


	}

	private void aggiungiMenu_Acquisizioni(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("acquisizioni", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_ACQUISIZIONI));
        menu2 = new TreeElement("gare", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_GARE_ACQUISTO));
        //menu2 = new TreeElement("gare", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_GARE_ACQUISTO));
		menu2.setUrl("/acquisizioni/gare/gareRicercaParziale.do");
        menu2 = new TreeElement("ordini", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_ORDINI));
        //menu2 = new TreeElement("ordini", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_ORDINI));
        menu2.setUrl("/acquisizioni/ordini/ordineRicercaParziale.do");
        menu2 = new TreeElement("buoniordine", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_BUONI_ORDINE));
        //menu2 = new TreeElement("buoniordine", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE));
        menu2.setUrl("/acquisizioni/buoniordine/buoniRicercaParziale.do");
        menu2 = new TreeElement("fatture", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_FATTURE));
        //menu2 = new TreeElement("fatture", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_FATTURE));
        menu2.setUrl("/acquisizioni/fatture/fattureRicercaParziale.do");
        menu2 = new TreeElement("comunicazioni", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_COMUNICAZIONI));
        //menu2 = new TreeElement("comunicazioni", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_COMUNICAZIONI));
        menu2.setUrl("/acquisizioni/comunicazioni/comunicazioniRicercaParziale.do");
        menu2 = new TreeElement("suggerimenti", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_BIBLIOTECARIO));
        //menu2 = new TreeElement("suggerimenti", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_AGGIORNAMENTO_SUGGERIMENTO_BIBLIOTECARIO));
        menu2.setUrl("/acquisizioni/suggerimenti/suggerimentiBiblRicercaParziale.do");
        menu2 = new TreeElement("documenti", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SUGGERIMENTO_LETTORE));
        //menu2 = new TreeElement("documenti", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_SUGGERIMENTO_LETTORE));
        menu2.setUrl("/acquisizioni/documenti/documentiRicercaParziale.do");
        menu2 = new TreeElement("fornitori", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_FORNITORI));
        //menu2 = new TreeElement("fornitori", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_FORNITORI));
        menu2.setUrl("/acquisizioni/fornitori/fornitoriRicercaParziale.do");
        menu2 = new TreeElement("bilancio", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_BILANCIO));
        //menu2 = new TreeElement("bilancio", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO));
        menu2.setUrl("/acquisizioni/bilancio/bilancioRicercaParziale.do");
        menu2 = new TreeElement("sezioni", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI));
        //menu2 = new TreeElement("sezioni", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_SEZIONE_ACQUISIZIONI));
        menu2.setUrl("/acquisizioni/sezioni/sezioniRicercaParziale.do");
        menu2 = new TreeElement("profili", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO));
        //menu2 = new TreeElement("profili", menu1, new AcqMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_PROFILI_DI_ACQUISTO));
        menu2.setUrl("/acquisizioni/profiliacquisto/profiliRicercaParziale.do");
        menu2 = new TreeElement("cambi", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_INTERROGAZIONE_CAMBI));
        //menu2 = new TreeElement("cambi", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_GESTIONE_CAMBI));
        menu2.setUrl("/acquisizioni/cambi/cambiRicercaParziale.do");
        menu2 = new TreeElement("configurazione", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GA_PARAMETRI_BIBLIOTECA));
        menu2.setUrl("/acquisizioni/configurazione/configurazioneBO.do");
	}

	private void aggiungiMenu_GestioneBibliografica(TreeElement root) {
		TreeElement menu1;
		TreeElement menu2;
		menu1 = new TreeElement("gestionebibliografica", root, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_1001 ));
        menu2 = new TreeElement("titolo", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_1001 ));
        menu2.setUrl("/gestionebibliografica/titolo/interrogazioneTitolo.do");
        menu2 = new TreeElement("autore", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionebibliografica/autore/interrogazioneAutore.do");
        menu2 = new TreeElement("marca", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionebibliografica/marca/interrogazioneMarca.do");
        menu2 = new TreeElement("luogo", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionebibliografica/luogo/interrogazioneLuogo.do");
        menu2 = new TreeElement("soggetti", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionesemantica/soggetto/RicercaSoggetto.do");
        menu2 = new TreeElement("classificazioni", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionesemantica/classificazione/RicercaClasse.do");
        menu2 = new TreeElement("thesauro", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003));
        menu2.setUrl("/gestionesemantica/thesauro/RicercaThesauro.do");
        menu2 = new TreeElement("idgestionali", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_1001));
        menu2.setUrl("/gestionebibliografica/titolo/interrogazioneTitoloPerGestionali.do");

        // Inizio Evolutiva Ba1 - Editori almaviva2 Novembre 2012
//        menu2 = new TreeElement("editori", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().PRODUZIONE_EDITORIALE));
        menu2 = new TreeElement("editori", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().GESTIONE_EDITORI));
        menu2.setUrl("/acquisizioni/fornitori/fornitoriRicercaParziale.do");
        // Fine Evolutiva Ba1 - Editori almaviva2 Novembre 2012

        menu2 = new TreeElement("propostaDiCorrezione", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().CERCA_PROPOSTE_DI_CORREZIONE_1004));
        menu2.setUrl("/gestionebibliografica/utility/interrogazionePropostaDiCorrezione.do");
        menu2 = new TreeElement("listeDiConfronto", menu1, new BaseMenuAttivitaChecker(CodiciAttivita.getIstance().IMPORTA_FILE_LISTE_CONFRONTO));
        menu2.setUrl("/gestionebibliografica/titolo/interrogazioneListeDiConfronto.do");

	}

    public boolean isUserEnabled(String codiceUtente, String codiceBiblio,
            String menuKey) throws EJBException {
        return true;
    }


    private List<DescrizioneFunzioneVO> getAcquisizioni(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Ordine");
		funz.addDescrizione("en","Ordine EN");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_ORDINE);
		funz.setActionPath("/acquisizioni/ordini/ordineRicercaParziale.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Suggerimento d'acquisto");
		funz.addDescrizione("en","Suggerimento d'acquisto en");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_SUGGERIMENTO_ACQUISTO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Cattura a suggerimento e Vai a: Gestione ordine");
		funz.addDescrizione("en","Cattura a suggerimento e Vai a: Gestione ordine en");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_CATTURA_A_SUGG_E_ORDINE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Suggerimento bibliotecario");
		funz.addDescrizione("en","Suggerimento bibliotecario EN");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_SUGGERIMENTO_BIBLIOTECARIO);
		funz.setActionPath("/acquisizioni/suggerimenti/suggerimentiBiblRicercaParziale.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Gara d'acquisto");
		funz.addDescrizione("en","Gara d'acquisto EN");
		funz.setCodice(MenuHome.FUNZ_ACQUISIZIONE_GARA_D_ACQUISTO);
		funz.setActionPath("/acquisizioni/gare/gareRicercaParziale.do");
		lista.add(funz);
		return lista;

    }


	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
    private List<DescrizioneFunzioneVO> getPeriodici(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_PERIODICI_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Descrizione fascicoli");
		funz.addDescrizione("en","Descrizione fascicoli EN");
		funz.setCodice(MenuHome.FUNZ_PERIODICI_DESCRIZIONE_FASCICOLI);
		funz.setActionPath("/periodici/ricercaFascicoli.do");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Gestione amministrativa");
		funz.addDescrizione("en","Gestione amministrativa en");
		funz.setCodice(MenuHome.FUNZ_PERIODICI_GESTIONE_AMMINISTRATIVA);
		funz.setActionPath("/periodici/gestionePeriodici.do");
		lista.add(funz);
		return lista;

    }
	// Fine Modifica almaviva2 04.08.2010 - Gestione periodici


    private List<DescrizioneFunzioneVO> getSemantica(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_SEMANTICA_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Soggettazione");
		funz.addDescrizione("en","Soggettazione en");
		funz.setCodice(MenuHome.FUNZ_SEMANTICA_SOGGETTAZIONE);
		funz.setActionPath("/gestionesemantica/catalogazionesemantica/CatalogazioneSemantica.do?folderT=FOLDER_SOGGETTI");
		lista.add(funz);

		//almaviva5_20090327 #2744
		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Classificazione");
		funz.addDescrizione("en","Classificazione en");
		funz.setCodice(MenuHome.FUNZ_SEMANTICA_CLASSIFICAZIONE);
		funz.setActionPath("/gestionesemantica/catalogazionesemantica/CatalogazioneSemantica.do?folderT=FOLDER_CLASSI");
		lista.add(funz);

		//almaviva5_20090714 #2866
		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Thesauro");
		funz.addDescrizione("en","Thesauro en");
		funz.setCodice(MenuHome.FUNZ_SEMANTICA_THESAURO);
		funz.setActionPath("/gestionesemantica/catalogazionesemantica/CatalogazioneSemantica.do?folderT=FOLDER_THESAURO");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Abstract");
		funz.addDescrizione("en","Abstract en");
		funz.setCodice(MenuHome.FUNZ_SEMANTICA_ABSTRACT);
		funz.setActionPath("/gestionesemantica/catalogazionesemantica/CatalogazioneSemantica.do?folderT=FOLDER_ABSTRACT");
		lista.add(funz);

		return lista;
    }

    private List<DescrizioneFunzioneVO> getDocumentoFisico(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_DOCUMENTO_FISICO_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Gestione inventari e collocazioni");
		funz.addDescrizione("en","Gestione inventari e collocazioni en");
		funz.setCodice(MenuHome.FUNZ_DOCUMENTO_FISICO_INVENTARI);
		funz.setActionPath("/documentofisico/datiInventari/vaiAListaInventariTitolo.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Inventariazione e Collocazione automatica");
		funz.addDescrizione("en","Inventariazione e Collocazione automatica en");
		funz.setCodice(MenuHome.FUNZ_DOCUMENTO_FISICO_COLLOC_VELOCE);
		funz.setActionPath("/documentofisico/datiInventari/vaiAInserimentoColl.do");
		lista.add(funz);

		return lista;
    }


    private List<DescrizioneFunzioneVO> getPossessori(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Variazione descrizione");
		funz.addDescrizione("en","Variazione descrizione en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_DESCRIZIONE);
		funz.setActionPath("/documentofisico/possessori/variazioneDescrizioneFormaAccettata.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Cancellazione");
		funz.addDescrizione("en","Cancellazione en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_CANCELLAZIONE);
		funz.setActionPath("/documentofisico/possessori/vaiAListaInventariTitolo.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Inserimento forma rinvio");
		funz.addDescrizione("en","Inserimento forma rinvio en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_INSERIMENTO_FORMA_RINVIO);
		funz.setActionPath("/documentofisico/possessori/legamePossessori.do?PROVENIENZA=INSERIMENTI_RINVIO");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Variazione descrizione forma rinvio");
		funz.addDescrizione("en","Variazione descrizione forma rinvio en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_FORMA_RINVIO);
		funz.setActionPath("/documentofisico/possessori/variazioneDescrizioneFormaRinvio.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Variazione legame");
		funz.addDescrizione("en","Variazione legame en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_VARIAZIONE_LEGAME);
		funz.setActionPath("/documentofisico/possessori/variazioneLegame.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Scambio forma");
		funz.addDescrizione("en","Scambio forma en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_SCAMBIO_FORMA);
		funz.setActionPath("/documentofisico/possessori/vaiAListaInventariTitolo.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Inserimento legame a inventario");
		funz.addDescrizione("en","Inserimento legame a inventario en");
		funz.setCodice(MenuHome.FUNZ_POSSESSORI_INSERIMENTO_LEGAME_INVENTARIO);
		funz.setActionPath("/documentofisico/possessori/creaLegameInventarioPossessore.do");
		lista.add(funz);

		return lista;
    }



    private List<DescrizioneFunzioneVO> getBibliografica(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.QUALSIASI, Localizzazione.QUALSIASI, Condiviso.QUALSIASI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Cataloga in Indice");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Cataloga in Indice");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Cataloga in Indice");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Cataloga in Indice elemento di reticolo");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice elemento di reticolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Cataloga in Indice elemento di reticolo");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice elemento di reticolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Cataloga in Indice elemento di reticolo");
		funz.addDescrizione("en","NUOVA VERSIONE - Cataloga in Indice elemento di reticolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO_NEW_VERSION);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Reinvio in Indice");
		funz.addDescrizione("en","Reinvio in Indice en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_REINVIO_IN_INDICE);
		funz.setActionPath("");
		lista.add(funz);



		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Ricerca oggetto condiviso per fusione");
		funz.addDescrizione("en","Ricerca oggetto condiviso per fusione en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_FONDI_ON_LINE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Imposta flag condivisione a [loc]");
		funz.addDescrizione("en","Imposta flag condivisione a [loc] en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_SCONDIVIDI);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.NO, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Cattura");
		funz.addDescrizione("en","Cattura en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Allinea reticolo");
		funz.addDescrizione("en","Allinea reticolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_RETICOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Allinea autore");
		funz.addDescrizione("en","Allinea autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.MARCA, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Allinea marca");
		funz.addDescrizione("en","Allinea marca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_MARCA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Allinea titoloUniforme");
		funz.addDescrizione("en","Allinea titoloUniforme en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_TITOLOUNIFORME);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.LUOGO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Allinea luogo");
		funz.addDescrizione("en","Allinea luogo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_ALLINEA_LUOGO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Varia descrizione");
		funz.addDescrizione("en","Variazione descrizione en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_DESCRIZIONE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Crea rinvio");
		funz.addDescrizione("en","Inserimento forma di rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Crea rinvio");
		funz.addDescrizione("en","Inserimento forma di rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO);
		funz.setActionPath("");
		lista.add(funz);

		// Gestione Bibliografica Intervento Interno: tutta la gestione dei Luoghi è demandata all'Indice vengono quindi asteriscate
		// tutte le attivazioni;
//		funz = new DescrizioneFunzioneVO(TipoOggetto.LUOGO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.QUALSIASI);
//		funz.addDescrizione("it","Crea rinvio");
//		funz.addDescrizione("en","Inserimento forma di rinvio en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_FORMA_RINVIO);
//		funz.setActionPath("");
//		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Crea rinvio reciproco");
		funz.addDescrizione("en","Inserimento legame fra enti en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORI);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Crea rinvio reciproco");
		funz.addDescrizione("en","Inserimento legame fra enti en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORI);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Cambia natura da A a B e viceversa");
		funz.addDescrizione("en","Cambia natura da A a B e viceversa en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CAMBIA_NATURA_BA_AB);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Varia descrizione");
		funz.addDescrizione("en","Variazione descrizione en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_DESCRIZIONE);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Correggi nota ISBD");
		funz.addDescrizione("en","Correzione nota ISBD en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CORREZIONE_NOTA_ISBD);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Correggi nota ISBD");
		funz.addDescrizione("en","Correzione nota ISBD en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CORREZIONE_NOTA_ISBD);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega titolo");
		funz.addDescrizione("en","Inserimento legame titolo-titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega titolo");
		funz.addDescrizione("en","Inserimento legame titolo-titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_TITOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega titolo a Raccolta");
		funz.addDescrizione("en","Inserimento legame titolo-raccolta en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_RACCOLTA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega titolo");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega titolo");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega autore");
		funz.addDescrizione("en","Inserimento legame titolo-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega autore");
		funz.addDescrizione("en","Inserimento legame titolo-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega autore");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega autore");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.MARCA, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega editore");
		funz.addDescrizione("en","Inserimento legame marca-editore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_MARCA_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega marca");
		funz.addDescrizione("en","Inserimento legame titolo-marca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_MARCA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega marca");
		funz.addDescrizione("en","Inserimento legame titolo-marca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_MARCA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega marca");
		funz.addDescrizione("en","Inserimento legame editore-marca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORE_MARCA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega marca");
		funz.addDescrizione("en","Inserimento legame editore-marca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_EDITORE_MARCA);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega luogo");
		funz.addDescrizione("en","Inserimento legame titolo-luogo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_LUOGO );
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Lega luogo");
		funz.addDescrizione("en","Inserimento legame titolo-luogo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLO_LUOGO );
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Crea volume inferiore");
		funz.addDescrizione("en","Inserimento volumi inferiori en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_VOLUMI_INFERIORI);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Crea volume inferiore");
		funz.addDescrizione("en","Inserimento volumi inferiori en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_VOLUMI_INFERIORI);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Crea titolo analitico (N)");
		funz.addDescrizione("en","Inserimento titolo analitico (spoglio) en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Crea titolo analitico (N)");
		funz.addDescrizione("en","Inserimento titolo analitico (spoglio) en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Crea titolo analitico (N) solo locale");
		funz.addDescrizione("en","Inserimento titolo analitico (spoglio) solo locale en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_SPOGLIO_LOCALE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Cattura vol inferiore / tit analitico (N)");
		funz.addDescrizione("en","Cattura volumi inferiori e/o titoli analitici (N) en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_INFERIORI);
		funz.setActionPath("");
		lista.add(funz);

		// Inizio modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione cpme vecchia Gestione 51
		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Delocalizza vol inferiore / tit analitico (N)");
		funz.addDescrizione("en","Delocalizza volumi inferiori e/o titoli analitici (N) en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA_VOLUMI_INFERIORI);
		funz.setActionPath("");
		lista.add(funz);
		// Fine modifica 12.07.2010 MANTIS 3677 - Inserimento nuova funzione

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Delocalizza documento");
		funz.addDescrizione("en","Delocalizza en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Delocalizza documento");
		funz.addDescrizione("en","Delocalizza en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_DELOCALIZZA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Cancella notizia");
		funz.addDescrizione("en","Cancellazione titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO);
		funz.setActionPath("");
		lista.add(funz);


		// Intervento del 27.07.2012 almaviva2 - intervento durante collaudo su richiesta almaviva1/almaviva
		// Funzionalità di cancellazione titolo Uniforme inserita
		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Cancella notizia");
		funz.addDescrizione("en","Cancellazione titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO);
		funz.setActionPath("");
		lista.add(funz);




		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Cancella notizia");
		funz.addDescrizione("en","Cancellazione titolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_TITOLO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Cancella autore");
		funz.addDescrizione("en","Cancellazione autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
		funz.addDescrizione("it","Cancella autore");
		funz.addDescrizione("en","Cancellazione autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		// Gestione Bibliografica Intervento Interno: tutta la gestione dei Luoghi è demandata all'Indice vengono quindi asteriscate
		// tutte le attivazioni;
//		funz = new DescrizioneFunzioneVO(TipoOggetto.LUOGO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.SI);
//		funz.addDescrizione("it","Cancella luogo");
//		funz.addDescrizione("en","Cancellazione luogo en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LUOGO);
//		funz.setActionPath("");
//		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Copia notizia");
		funz.addDescrizione("en","Copia notizia en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CATTURA_A_SUGGERIMENTO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Copia reticolo");
		funz.addDescrizione("en","Copia reticolo en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO);
		funz.setActionPath("");
		lista.add(funz);

		// almaviva2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
		// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
		// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
		// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
		// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
		// Intervento almaviva2 gennaio 2017
		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.SI);
		funz.addDescrizione("it","Copia reticolo con tit. analitici e inserisce in Indice");
		funz.addDescrizione("en","Copia reticolo con tit. analitici e inserisce in Indice en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_COPIA_RETICOLO_TIT_ANALITICI);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Varia legame");
		funz.addDescrizione("en","Variazione legame en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_LEGAME);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.QUALSIASI, RootReticolo.NO);
		funz.addDescrizione("it","Varia legame");
		funz.addDescrizione("en","Variazione legame en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_VARIAZIONE_LEGAME);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Scambia autore alternativo con autore principale");
		funz.addDescrizione("en","Scambia autore alternativo con autore principale en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIA_LEGAME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Scambia autore alternativo con autore principale");
		funz.addDescrizione("en","Scambia autore alternativo con autore principale en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIA_LEGAME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Scambia forma");
		funz.addDescrizione("en","Scambio forma en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIO_FORMA);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Scambia forma");
		funz.addDescrizione("en","Scambio forma en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_SCAMBIO_FORMA);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Cancella rinvio");
		funz.addDescrizione("en","Cancellazione forma di rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.NO);
		funz.addDescrizione("it","Cancella rinvio");
		funz.addDescrizione("en","Cancellazione forma di rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME_RINVIO, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Cancella rinvio");
		funz.addDescrizione("en","Cancellazione forma di rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO);
		funz.setActionPath("");
		lista.add(funz);


		// Gestione Bibliografica Intervento Interno: tutta la gestione dei Luoghi è demandata all'Indice vengono quindi asteriscate
		// tutte le attivazioni;
//		funz = new DescrizioneFunzioneVO(TipoOggetto.LUOGO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.NO);
//		funz.addDescrizione("it","Cancella rinvio");
//		funz.addDescrizione("en","Cancellazione forma di rinvio en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_FORMA_RINVIO);
//		funz.setActionPath("");
//		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Cancella legame");
		funz.addDescrizione("en","Cancellazione legame en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LEGAME);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.QUALSIASI, RootReticolo.NO);
		funz.addDescrizione("it","Cancella legame");
		funz.addDescrizione("en","Cancellazione legame en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_LEGAME);
		funz.setActionPath("");
		lista.add(funz);


		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Proposta di correzione");
		funz.addDescrizione("en","Proposta di correzione en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_PROPOSTA_CORREZIONE);
		funz.setActionPath("");
		lista.add(funz);

		// Inizio Evolutiva Google3:
		// Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza (per oggetti già presenti POLO)

		// FUNZIONE CHE ANDRA' ELIMINATA:
//		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.QUALSIASI);
//		funz.addDescrizione("it","OLD - Operazioni di servizio su localizzazioni");
//		funz.addDescrizione("en","Operazioni di servizio su localizzazioni en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI);
//		funz.setActionPath("");
//		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Modifica localizzazioni per gestione");
		funz.addDescrizione("en","Modifica localizzazioni per gestione en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_GESTIONE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.QUALSIASI, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Aggiorna dati di possesso");
		funz.addDescrizione("en","Aggiorna dati di possesso en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Aggiorna dati di possesso della Biblioteca");
		funz.addDescrizione("en","Aggiorna dati di possesso della Biblioteca en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI_POSSESSO);
		// funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_GESTIONE_LOCALIZZAZIONI);
		funz.setActionPath("");
		lista.add(funz);
		// Fine Evolutiva Google3:

		// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: trascinamento legami autore ma M a N (esempio disco M con tracce N)
		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.NO);
		funz.addDescrizione("it","Trascina legami titolo-autore");
		funz.addDescrizione("en","Trascina legami titolo-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_TRASCINA_LEGAME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);
		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.QUALSIASI, RootReticolo.NO);
		funz.addDescrizione("it","Trascina legami titolo-autore");
		funz.addDescrizione("en","Trascina legami titolo-autore en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_TRASCINA_LEGAME_AUTORE);
		funz.setActionPath("");
		lista.add(funz);


		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
        // con tipo legame 431 (A08V)
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Inserimento legame titoloUniforme-Rinvio");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-Rinvio en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_RINVIO);
		funz.setActionPath("");
		lista.add(funz);
		funz = new DescrizioneFunzioneVO(TipoOggetto.TIT_UNIFORME, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.QUALSIASI);
		funz.addDescrizione("it","Inserimento legame titoloUniforme-titoloUniforme");
		funz.addDescrizione("en","Inserimento legame titoloUniforme-titoloUniforme en");
		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_TITOLOUNIFORME_TITOLOUNIFORME);
		funz.setActionPath("");
		lista.add(funz);

// FUNZIONALITA' OBSOLETE O CAMBIAMENTO DI NOMI/DICITURE

//		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
//		funz.addDescrizione("it","Cataloga in Indice");
//		funz.addDescrizione("en","Cataloga in Indice en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI);
//		funz.setActionPath("");
//		lista.add(funz);
//
//		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.NO, RootReticolo.SI);
//		funz.addDescrizione("it","Cataloga in Indice");
//		funz.addDescrizione("en","Cataloga in Indice en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI);
//		funz.setActionPath("");
//		lista.add(funz);

//		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
//		funz.addDescrizione("it","Reinvio in Indice");
//		funz.addDescrizione("en","Reinvio in Indice en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO);
//		funz.setActionPath("");
//		lista.add(funz);

//		funz = new DescrizioneFunzioneVO(TipoOggetto.MARCA, LivelloRicerca.INDICE, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
//		funz.addDescrizione("it","Cancellazione marca");
//		funz.addDescrizione("en","Cancellazione marca en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CANCELLAZIONE_MARCA);
//		funz.setActionPath("");
//		lista.add(funz);

//		funz = new DescrizioneFunzioneVO(TipoOggetto.TITOLO, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
//		funz.addDescrizione("it","Reinvio in Indice");
//		funz.addDescrizione("en","Reinvio in Indice en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO);
//		funz.setActionPath("");
//		lista.add(funz);

//		funz = new DescrizioneFunzioneVO(TipoOggetto.AUTORE, LivelloRicerca.POLO, Localizzazione.SI, Condiviso.SI, RootReticolo.SI);
//		funz.addDescrizione("it","Reinvio in Indice");
//		funz.addDescrizione("en","Reinvio in Indice en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_CONDIVIDI_ELEMENTI_RETICOLO);
//		funz.setActionPath("");
//		lista.add(funz);

//		funz = new DescrizioneFunzioneVO(TipoOggetto.LUOGO, LivelloRicerca.INDICE, Localizzazione.QUALSIASI, Condiviso.SI, RootReticolo.QUALSIASI);
//		funz.addDescrizione("it","Inserimento legame fra luoghi");
//		funz.addDescrizione("en","Inserimento legame fra luoghi en");
//		funz.setCodice(MenuHome.FUNZ_BIBLIOGRAFICA_INSERIMENTO_LEGAME_LUOGHI);
//		funz.setActionPath("");
//		lista.add(funz);

		return lista;
    }

    private List<DescrizioneFunzioneVO> getStampe(String ticket) {
		List<DescrizioneFunzioneVO> lista = new ArrayList<DescrizioneFunzioneVO>();
		DescrizioneFunzioneVO funz= null;

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","");
		funz.addDescrizione("en","");
		funz.setCodice(MenuHome.FUNZ_STAMPE_DEFAULT);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Stampa schede catalografiche");
		funz.addDescrizione("en","Stampa schede catalografiche en");
		funz.setCodice(MenuHome.FUNZ_STAMPE_CATALOGRAFICHE);
		funz.setActionPath("");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Lega editore");
		funz.addDescrizione("en","Lega editore en");
		funz.setCodice(MenuHome.FUNZ_LEGA_EDITORE);
		funz.setActionPath("/acquisizioni/fornitori/fornitoriRicercaParziale.do");
		lista.add(funz);

		funz = new DescrizioneFunzioneVO();
		funz.addDescrizione("it","Gestione legami titolo editori");
		funz.addDescrizione("en","Gestione legami titolo editori en");
		funz.setCodice(MenuHome.FUNZ_GEST_LEGAMI_TIT_EDI);
		funz.setActionPath("/gestionebibliografica/titolo/sinteticaTitoli.do?EDITCOLLTITOLI=TRUE");
		lista.add(funz);

		return lista;
    }

	public Map<String, List<DescrizioneFunzioneVO>> getFunzioni(String ticket) throws EJBException {
		Map<String, List<DescrizioneFunzioneVO>> funzioni = new HashMap<String, List<DescrizioneFunzioneVO>>();

		// ACQUISIZIONI
		funzioni.put("0001", this.getAcquisizioni(ticket));

		// CATALOGAZIONE SEMANTICA
		funzioni.put("0002", this.getSemantica(ticket));

		// GESTIONE DOCUMENTO FISICO
		funzioni.put("0003", this.getDocumentoFisico(ticket));

//		 GESTIONE BIBLIOGRAFICA
		funzioni.put("0004", this.getBibliografica(ticket));

//		 PRODUZIONE STAMPE
		funzioni.put("0005", this.getStampe(ticket));

//		 GESTIONE POSSESSORI
		funzioni.put("0006", this.getPossessori(ticket));

		// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici
		// PERIODICI
		funzioni.put("0007", this.getPeriodici(ticket));
		// Fine Modifica almaviva2 04.08.2010 - Gestione periodici

		return funzioni;
	}

}
