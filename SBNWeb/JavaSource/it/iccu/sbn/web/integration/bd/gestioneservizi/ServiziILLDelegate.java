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

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ServiziILLDelegate {

	static Logger log = Logger.getLogger(ServiziILLDelegate.class);

	private static final String DELEGATE_ATTR = "req." + ServiziILLDelegate.class.getSimpleName();

	Utente utenteEjb;
	final HttpServletRequest request;
	private UserVO utente;

	private FactoryEJBDelegate factory;

	public static final ServiziILLDelegate getInstance(HttpServletRequest request) {
		ServiziILLDelegate delegate = (ServiziILLDelegate) request.getAttribute(DELEGATE_ATTR);
		if (delegate == null) {
			delegate = new ServiziILLDelegate(request);
			request.setAttribute(DELEGATE_ATTR, delegate);
		}
		return delegate;
	}

	protected ServiziILLDelegate(HttpServletRequest request) {
		this.request = request;
		this.utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		this.utente = Navigation.getInstance(request).getUtente();
		try {
			this.factory = FactoryEJBDelegate.getInstance();
		} catch (Exception e) {	}
	}


	public List<BibliotecaVO> filtraBibliotecheAffiliateILL(List<BibliotecaVO> bibliotecheOpac, String tipoServizio,
			String isilBibRichiedente) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_FILTRA_BIBLIOTECHE_AFFILIATE, new ArrayList<BibliotecaVO>(bibliotecheOpac),
					tipoServizio, isilBibRichiedente);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (List<BibliotecaVO>) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return Collections.emptyList();

	}

	public MovimentoVO inoltraRichiestaAdAltraBiblioteca(MovimentoVO mov, BibliotecaVO bibForn) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_INOLTRA_AD_ALTRA_BIBLIOTECA, mov.copy(), bibForn);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (MovimentoVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;

	}

	public void aggiornaBibliotecaILL(BibliotecaVO bib) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_AGGIORNA_BIBLIOTECA, bib.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

	}

	public DatiRichiestaILLVO inviaMessaggioILL(DatiRichiestaILLVO dati) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_INVIA_MESSAGGIO, dati.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DatiRichiestaILLVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

	}

	public DatiRichiestaILLVO rifiutaRichiestaILL(DatiRichiestaILLVO dati) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_RIFIUTA_RICHIESTA, dati.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			DatiRichiestaILLVO output = (DatiRichiestaILLVO) result.getResult();
			return output;

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public boolean configuraTipoServizioILL(TipoServizioVO tipoServizio) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_CONFIGURA_TIPO_SERVIZIO, tipoServizio.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (Boolean) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}
		return false;

	}

	public MovimentoVO inserisciPrenotazioneILL(MovimentoVO movimento) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_INSERISCI_PRENOTAZIONE, movimento.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (MovimentoVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}
		return null;

	}

	public DatiRichiestaILLVO inviaCondizioneILL(DatiRichiestaILLVO dati) throws Exception {
		CommandInvokeVO invoke =
				new CommandInvokeVO(utente.getTicket(),
					CommandType.SRV_ILL_PONI_CONDIZIONE, dati.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DatiRichiestaILLVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new ApplicationException(e);
		}

	}

	public DatiRichiestaILLVO getDettaglioRichiestaILL(DatiRichiestaILLVO dati) throws Exception {
		CommandInvokeVO invoke =
			new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_DETTAGLIO_RICHIESTA, dati.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			DatiRichiestaILLVO output = (DatiRichiestaILLVO) result.getResult();
			return output;

		} catch (ApplicationException e) {
			log.error("", e);
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public DatiRichiestaILLVO aggiornaDatiRichiestaILL(DatiRichiestaILLVO dati)
			throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(
				utente.getTicket(),
				CommandType.SRV_ILL_AGGIORNA_DATI_RICHIESTA,
				dati.copy() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DatiRichiestaILLVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public DatiRichiestaILLVO avanzaRichiestaILL(DatiRichiestaILLVO dati, StatoIterRichiesta newState, MessaggioVO msg)
			throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_AVANZA_RICHIESTA,
				dati.copy(), newState.getISOCode(), msg.copy());
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DatiRichiestaILLVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public DatiRichiestaILLVO allineaRichiestaILL(DatiRichiestaILLVO dati) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_ALLINEA_RICHIESTA, dati.copy() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DatiRichiestaILLVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public List<BibliotecaVO> getBibliotechePoloILL(String codPolo) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_LISTA_BIBLIOTECHE_POLO_ILL, codPolo );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (List<BibliotecaVO>) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public DocumentoNonSbnVO creaDocumentoNonPossedutoDaInventario(String codBib, InventarioVO inv) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(),
				CommandType.SRV_ILL_CREA_DOC_DA_INVENTARIO, codBib, inv.copy() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (DocumentoNonSbnVO) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

	public ILLAPDU executeHandler(String ill_xml, String isil) throws Exception {
		CommandInvokeVO invoke = new CommandInvokeVO(utente.getTicket(), CommandType.SRV_ILL_XML_REQUEST,
				ill_xml, isil.toUpperCase() );
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(invoke);
			result.throwError();

			return (ILLAPDU) result.getResult();

		} catch (ApplicationException e) {
			log.error("", e);
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
		}

		return null;
	}

}
