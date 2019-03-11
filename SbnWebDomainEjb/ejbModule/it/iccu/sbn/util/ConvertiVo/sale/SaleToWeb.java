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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;
import it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_sale;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_posto_sala_categoria_mediazione;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.ConvertiVo.DataBindingConverter;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.servizi.SaleUtil;
import it.iccu.sbn.vo.custom.servizi.calendario.ModelloCalendarioDecorator;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SaleToWeb extends DataBindingConverter {

	private static final long serialVersionUID = 3344469237127052732L;

	public SalaVO sala(Tbl_sale hVO, List<Tbl_posti_sala> posti, List<Tbl_modello_calendario> calendari) {
		if (hVO == null)
			return null;

		SalaVO webVO = new SalaVO();
		fillBaseWeb(hVO, webVO);

		Tbf_biblioteca_in_polo bib = hVO.getCd_bib();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());
		webVO.setIdSala(hVO.getId_sale());
		webVO.setCodSala(hVO.getCod_sala());
		webVO.setDescrizione(hVO.getDescr());
		webVO.setNumeroPosti(hVO.getNum_max_posti());
		webVO.setPostiOccupati(hVO.getNum_prg_posti());
		webVO.setDurataFascia(hVO.getDurata_fascia());
		webVO.setMaxFascePrenotazione(hVO.getNum_max_fasce_prenot());
		webVO.setMaxUtentiPerPrenotazione(hVO.getNum_max_utenti_per_prenot());
		webVO.setPrenotabileDaRemoto(ValidazioneDati.in(hVO.getFl_prenot_remoto(), 'S', 's'));

		if (isFilled(posti)) {
			for (Tbl_posti_sala posto : posti) {
				PostoSalaVO ps = this.posto(posto);
				webVO.getPosti().add(ps);
			}

			//accorpamento posti in gruppi
			List<GruppoPostiVO> gruppi = SaleUtil.generaGruppi(webVO.getPosti());
			webVO.setGruppi(gruppi);
		}

		if (isFilled(calendari))
			for (Tbl_modello_calendario calendario : calendari) {
				ModelloCalendarioVO modello = ConvertToWeb.Calendario.modelloCalendario(calendario);
				ModelloCalendarioVO decorated = ModelloCalendarioDecorator.decorate(modello);
				if (!decorated.isCancellato())
					webVO.setCalendario(decorated);
			}

		return webVO;
	}

	public PostoSalaVO posto(Tbl_posti_sala hVO) {
		if (hVO == null)
			return null;

		PostoSalaVO webVO = new PostoSalaVO();
		fillBaseWeb(hVO, webVO);

		webVO.setId_posto(hVO.getId_posti_sala());
		webVO.setGruppo(hVO.getGruppo());
		webVO.setNum_posto(hVO.getNum_posto());
		webVO.setOccupato(hVO.getOccupato() == 'S');

		Tbl_sale id_sale = hVO.getId_sale();
		webVO.setIdSala(id_sale.getId_sale());
		webVO.setSala(this.sala(id_sale, null, null));

		for (Trl_posto_sala_categoria_mediazione cat_med : hVO.getTrl_posto_sala_categoria_mediazione() )
			webVO.getCategorieMediazione().add(cat_med.getCd_cat_mediazione());

		return webVO;
	}

	public PrenotazionePostoVO prenotazione(Tbl_prenotazione_posto hVO, MovimentoVO mov) throws DaoManagerException {
		if (hVO == null)
			return null;

		PrenotazionePostoVO webVO = new PrenotazionePostoVO();
		fillBaseWeb(hVO, webVO);

		webVO.setId_prenotazione(hVO.getId_prenotazione());

		Tbf_biblioteca_in_polo bib = hVO.getBiblioteca();
		webVO.setCodPolo(bib.getCd_polo().getCd_polo());
		webVO.setCodBib(bib.getCd_biblioteca());

		webVO.setPosto(this.posto(hVO.getPosto()));
		webVO.setUtente(ServiziConversioneVO.daHibernateAWebUtente(hVO.getUtente()));
		List<MovimentoVO> movimenti = webVO.getMovimenti();
		if (mov != null)
			movimenti.add(mov);
		else {
			Set<Tbl_richiesta_servizio> richieste = hVO.getRichieste();
			for (Tbl_richiesta_servizio richiesta : richieste)
				movimenti.add(ServiziConversioneVO.daHibernateAWebMovimento(richiesta, Locale.getDefault()));
		}

		webVO.setStato(StatoPrenotazionePosto.of(hVO.getCd_stato()));
		webVO.setTs_inizio(hVO.getTs_inizio());
		webVO.setTs_fine(hVO.getTs_fine());

		webVO.setCatMediazione(hVO.getCd_cat_mediazione());

		Set<Tbl_utenti> utenti = hVO.getUtenti();
		List<UtenteBaseVO> altriUtenti = webVO.getAltriUtenti();
		for (Tbl_utenti utente : utenti) {
			altriUtenti.add(ServiziConversioneVO.daHibernateAWebUtente(utente));
		}

		return webVO;
	}

}
