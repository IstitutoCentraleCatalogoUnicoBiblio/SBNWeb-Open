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
package it.iccu.sbn.web.integration.bd.gestioneservizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.blocchi.DescrittoreBlocchiUtil;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.util.ThreeState;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class SaleDelegate {

	static Logger log = Logger.getLogger(SaleDelegate.class);

	private static final String DELEGATE_ATTR = "req." + SaleDelegate.class.getSimpleName();

	private Utente utenteEjb;
	private final HttpServletRequest request;
	private UserVO utente;

	private Sale sale;

	public static final SaleDelegate getInstance(HttpServletRequest request) {
		SaleDelegate delegate = (SaleDelegate) request.getAttribute(DELEGATE_ATTR);
		if (delegate == null) {
			delegate = new SaleDelegate(request);
			request.setAttribute(DELEGATE_ATTR, delegate);
		}
		return delegate;
	}

	protected SaleDelegate(HttpServletRequest request) {
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		this.utente = Navigation.getInstance(request).getUtente();
		try {
			this.sale = DomainEJBFactory.getInstance().getSale();
		} catch (Exception e) {	}
	}


	public DescrittoreBloccoVO getListaSale(RicercaSalaVO ricerca) throws Exception {
		try {
			List<SalaVO> richieste = sale.getListaSale(utente.getTicket(), (RicercaSalaVO) ricerca.clone());

			DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(), richieste,
					ricerca.getElementiPerBlocco(), null);

			return blocco1;

		} catch (ApplicationException e) {
			log.error("", e);
		}

		return null;
	}

	public SalaVO aggiornaSala(SalaVO sala) throws Exception {
		try {

			sala = sale.aggiornaSala(utente.getTicket(), (SalaVO) sala.copy());

			return sala;

		} catch (ApplicationException e) {
			throw e;
		}

	}

	public SalaVO cancellaSala(SalaVO sala) throws Exception {
		try {

			sala = sale.cancellaSala(utente.getTicket(), (SalaVO) sala.copy());

			return sala;

		} catch (ApplicationException e) {
			throw e;
		}

	}

	public SalaVO getDettaglioSala(SalaVO sala) throws Exception {
		try {

			sala = sale.getSalaById(utente.getTicket(), sala.getIdSala());
			return sala;

		} catch (ApplicationException e) {
			throw e;
		}
	}

	public DescrittoreBloccoVO getListaCategorieMediazione(String codPolo, String codBib, boolean legataPosto,
			ThreeState richiedeSupp, int elementiPerBlocco)
			throws Exception {
		try {
			List<Mediazione> mediazioni = sale.getListaCategorieMediazione(utente.getTicket(), codPolo, codBib, legataPosto, richiedeSupp);

			DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(), mediazioni,
					elementiPerBlocco, null);

			return blocco1;

		} catch (ApplicationException e) {
			log.error("", e);
		}

		return null;
	}

	public DescrittoreBloccoVO getListaPrenotazioniPosto(RicercaPrenotazionePostoVO ricerca) throws Exception {
		try {
			List<PrenotazionePostoVO> prenotazioni = sale.getListaPrenotazioniPosto(utente.getTicket(), ricerca);

			DescrittoreBloccoVO blocco1 = DescrittoreBlocchiUtil.saveBlocchi(utente.getTicket(), prenotazioni,
					ricerca.getElementiPerBlocco(),
					ricerca.isDettaglioCompleto() ? new ListaPrenotazioniPostoInterceptor(utente.getTicket()) : null);

			return blocco1;

		} catch (ApplicationException e) {
			log.error("", e);
		}

		return null;
	}

	public PrenotazionePostoVO aggiornaPrenotazionePosto(PrenotazionePostoVO pp) throws Exception {
		return sale.aggiornaPrenotazionePosto(utente.getTicket(), pp, true);
	}

	public PrenotazionePostoVO getDettaglioPrenotazionePosto(PrenotazionePostoVO pp) throws Exception {
		try {

			PrenotazionePostoVO prenotazionePosto = sale.getPrenotazionePostoById(utente.getTicket(), pp.getId_prenotazione() );
			return prenotazionePosto;

		} catch (ApplicationException e) {
			throw e;
		}
	}

	public PrenotazionePostoVO rifiutaPrenotazionePosto(PrenotazionePostoVO pp, boolean inviaMailNotifica) throws Exception {
		return sale.rifiutaPrenotazionePosto(utente.getTicket(), pp, inviaMailNotifica);
	}

	public int getNumeroPostiLiberi(RicercaSalaVO ricerca) throws Exception {
		return sale.getNumeroPostiLiberi(utente.getTicket(), ricerca);
	}

}
