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
package it.iccu.sbn.util.ConvertiVo.sale;

import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.persistence.dao.servizi.UtilitaDAO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_sale;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.util.ConvertiVo.DataBindingConverter;

public class SaleToHibernate extends DataBindingConverter {

	private static final long serialVersionUID = 8252091219270175229L;

	SaleDAO dao = new SaleDAO();
	UtilitaDAO utilDao = new UtilitaDAO();
	UtentiDAO udao = new UtentiDAO();
	RichiesteServizioDAO rdao = new RichiesteServizioDAO();

	public Tbl_sale sala(Tbl_sale old, SalaVO webVO) throws DaoManagerException {
		Tbl_sale hVO = old != null ? old : new Tbl_sale();
		fillBaseHibernate(hVO, webVO);

		Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(webVO.getCodPolo(), webVO.getCodBib());
		hVO.setCd_bib(bib);

		assignEntityID(hVO, "id_sale", new Integer(webVO.getIdSala()) );
		hVO.setCod_sala(webVO.getCodSala());
		hVO.setDescr(webVO.getDescrizione());
		hVO.setNum_max_posti(webVO.getNumeroPosti());
		hVO.setNum_prg_posti(webVO.getPostiOccupati());
		hVO.setDurata_fascia(webVO.getDurataFascia());
		hVO.setNum_max_fasce_prenot(webVO.getMaxFascePrenotazione());
		hVO.setNum_max_utenti_per_prenot(webVO.getMaxUtentiPerPrenotazione());
		hVO.setFl_prenot_remoto(webVO.isPrenotabileDaRemoto() ? 'S' : 'N');

		return hVO;
	}

	public Tbl_posti_sala posto(Tbl_posti_sala old, PostoSalaVO webVO) throws DaoManagerException {
		Tbl_posti_sala hVO = old != null ? old : new Tbl_posti_sala();
		fillBaseHibernate(hVO, webVO);

		hVO.setId_posti_sala(webVO.getId_posto());

		int id_sala = webVO.getIdSala();
		if (isFilled(id_sala)) {
			SaleDAO dao = new SaleDAO();
			hVO.setId_sale(dao.getSalaById(id_sala));
		}

		hVO.setGruppo(webVO.getGruppo());
		hVO.setNum_posto(webVO.getNum_posto());
		hVO.setOccupato(webVO.isOccupato() ? 'S' : 'N');

		return hVO;
	}

	public Tbl_prenotazione_posto prenotazione(PrenotazionePostoVO webVO) throws DaoManagerException {
		return this.prenotazione(new Tbl_prenotazione_posto(), webVO);
	}

	public Tbl_prenotazione_posto prenotazione(Tbl_prenotazione_posto target, PrenotazionePostoVO webVO) throws DaoManagerException {
		final boolean updating = target != null;
		Tbl_prenotazione_posto hVO = updating ? target : new Tbl_prenotazione_posto();
		fillBaseHibernate(hVO, webVO);

		hVO.setCd_stato(webVO.getStato().getStato());
		hVO.setTs_inizio(webVO.getTs_inizio());
		hVO.setTs_fine(webVO.getTs_fine());
		hVO.setCd_cat_mediazione(webVO.getCatMediazione());

		for (MovimentoVO mov : webVO.getMovimenti() ) {
			if (!mov.isNuovo())
				hVO.getRichieste().add(rdao.getMovimentoById(mov.getIdRichiesta()));
		}

		if (!updating) {
			// nuova prenotazione
			hVO.setId_prenotazione(webVO.getId_prenotazione());

			Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(webVO.getCodPolo(), webVO.getCodBib());
			hVO.setBiblioteca(bib);

			hVO.setPosto(this.posto(null, webVO.getPosto()));
			hVO.setUtente(udao.getUtenteAnagraficaById(webVO.getUtente().getIdUtente()));

			for (UtenteBaseVO u : webVO.getAltriUtenti()) {
				final Tbl_utenti utente = udao.getUtente(u.getCodUtente());
				if (utente != null) {
					hVO.getUtenti().add(utente);
				}
			}
		}

		return hVO;
	}

}
