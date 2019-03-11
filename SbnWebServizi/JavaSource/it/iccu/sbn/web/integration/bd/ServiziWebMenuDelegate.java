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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.common.TreeElementViewMenu;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.integration.bd.gestioneservizi.SaleDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.ServiziWebDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServiziWebMenuDelegate {

	public static final TreeElementView renderMenu(HttpServletRequest request) throws Exception {

		TreeElementViewMenu base = new TreeElementViewMenu();
		TreeElementViewMenu root = (TreeElementViewMenu) base.addChild();

		HttpSession session = request.getSession();
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		Navigation navi = Navigation.getInstance(request);
		String polo = navi.getPolo();

		root.setKey("menu.servizi");
		root.open();

		TreeElementViewMenu child = (TreeElementViewMenu) root.addChild();
		child.setKey("menu.servizi.sceltabiblioteca");
		child.setUrl("/serviziweb/selezionaBib.do");
		//
		//la voce di menù Nuova richiesta compare solo se la biblioteca
		//selezionata ammette servizi su documenti nonSBN(catFrui di default valorizzato or)
		//almeno un range collocazione definito
		//tali campi sono reperibili su getParametriBiblioteca e contaRangeSegnature
		//
		boolean abilitaRemoto = false;
		String bibCorrente = (String) session.getAttribute(Constants.COD_BIBLIO);
		if (ValidazioneDati.isFilled(bibCorrente) ) {
			ServiziWebDelegate delegate = ServiziWebDelegate.getInstance(request);;
			abilitaRemoto = delegate.isAbilitatoInserimentoRemoto(polo, bibCorrente, utente);
		}

		if (abilitaRemoto) {
			child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.menuservizi");
	        child.setUrl("/serviziweb/menuServizi.do");//Nuova richiesta
		}

		if (ValidazioneDati.isFilled(bibCorrente) ) {
			child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.esamerichieste");
			child.setUrl("/serviziweb/esameRichieste.do");

			//almaviva5_20100609
			child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.esamerichieste.storico");
			child.setUrl("/serviziweb/esameStoricoRichieste.do");

			child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.dirittiutente");
			child.setUrl("/serviziweb/DirittiUtente.do");

	        child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.esamesugacquisto");
	        child.setUrl("/serviziweb/esameSugAcq.do");

	        //almaviva5_20110117 #4152
	        child = (TreeElementViewMenu) root.addChild();
			child.setKey("menu.servizi.anagrafe.utente");
	        child.setUrl("/serviziweb/datiUtenteWeb.do");

	        try {
	        	//verifica esistenza sale
	        	RicercaSalaVO ricerca = new RicercaSalaVO();
	        	ricerca.setCodPolo(polo);
	        	ricerca.setCodBib(bibCorrente);
				if (SaleDelegate.getInstance(request).getNumeroPostiLiberi(ricerca) > 0) {
					child = (TreeElementViewMenu) root.addChild();
					child.setKey("menu.servizi.prenotazione.posto");
			        child.setUrl("/serviziweb/listaPrenotazioniPosti.do");
				}
	        } catch (Exception e) { }
		}

        if (ValidazioneDati.isFilled(bibCorrente) )
        try {
			TB_CODICI codicePolo = CodiciProvider.cercaCodice(polo,
					CodiciType.CODICE_CONFIGURAZIONE_URL_OPAC,
					CodiciRicercaType.RICERCA_CODICE_SBN, true);
			TB_CODICI codiceBib = CodiciProvider.cercaCodice(bibCorrente != null ? bibCorrente : utente.getCodBib(),
					CodiciType.CODICE_CONFIGURAZIONE_URL_OPAC,
					CodiciRicercaType.RICERCA_CODICE_SBN, true);
			if (codiceBib == null)	// non trovo la riga per la bib. passo alla configurazione default di polo (*)
				codiceBib = codicePolo;

			// se non esiste almeno una configurazione, ignoro i link a cataloghi
			if (codiceBib == null)
				return base;

			if (codiceBib.getCd_flg3().equals("S") && ValidazioneDati.isFilled(codiceBib.getCd_flg2())) {
				// opac catalogato é utilizzato da questa bib.
				child = (TreeElementViewMenu) root.addChild();
				child.setKey("menu.servizi.catalogosbn");
		        child.setUrl("/serviziweb/catalogoSBN.do?type=SBN");
			}

			if (codiceBib.getCd_flg5().equals("S") && ValidazioneDati.isFilled(codiceBib.getCd_flg4())) {
				// opac non catalogato é utilizzato da questa bib. tab
				child = (TreeElementViewMenu) root.addChild();
				child.setKey("menu.servizi.catalogoNonSbn");
		        child.setUrl("/serviziweb/catalogoSBN.do?type=NOSBN");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
 		return base;
	}
}
