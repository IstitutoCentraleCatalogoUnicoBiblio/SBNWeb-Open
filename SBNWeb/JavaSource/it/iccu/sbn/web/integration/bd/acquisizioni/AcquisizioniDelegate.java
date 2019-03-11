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
package it.iccu.sbn.web.integration.bd.acquisizioni;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class AcquisizioniDelegate {

	private static final String ACQUISIZIONI_DELEGATE = "req.acquisiz.delegate";

	private final UserVO utente;
	private final HttpServletRequest request;
	private final FactoryEJBDelegate factory;
	private final Utente utenteEJB;

	public static final AcquisizioniDelegate getInstance(
			HttpServletRequest request) throws Exception {
		AcquisizioniDelegate delegate = (AcquisizioniDelegate) request
				.getAttribute(ACQUISIZIONI_DELEGATE);
		if (delegate == null) {
			delegate = new AcquisizioniDelegate(request);
			request.setAttribute(ACQUISIZIONI_DELEGATE, delegate);
		}
		return delegate;
	}

	private AcquisizioniDelegate(HttpServletRequest request) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		this.request = request;
		factory = FactoryEJBDelegate.getInstance();
		utente = navi.getUtente();
		utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
	}

	public ConfigurazioneORDVO getConfigurazioneOrdini() throws Exception {

		ConfigurazioneORDVO richiesta = new ConfigurazioneORDVO();
		richiesta.setCodPolo(utente.getCodPolo());
		richiesta.setCodBibl(utente.getCodBib());
		richiesta.setTicket(utente.getTicket());
		ConfigurazioneORDVO config = factory.getGestioneAcquisizioni()
				.loadConfigurazioneOrdini(richiesta);
		return config;
	}

	public boolean modificaOrdine(OrdiniVO ordine) throws Exception {

		factory.getGestioneAcquisizioni().ValidaOrdiniVO(ordine);
		boolean result = factory.getGestioneAcquisizioni().modificaOrdine(
				ordine);
		return result;
	}

	public OrdiniVO spedisciOrdineRilegatura(OrdiniVO ordine) throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_SPEDISCI_ORDINE_RILEGATURA, ordine.copy());
		try {
			CommandResultVO response = factory.getGestioneAcquisizioni()
					.invoke(cmd);
			response.throwError();

			return (OrdiniVO) response.getResult();

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public boolean esisteLegameRigaBilancio(int idBilancio,
			BilancioDettVO rigaBilancio) throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_ESISTE_LEGAME_RIGA_BILANCIO, idBilancio,
				rigaBilancio.copy());
		try {
			CommandResultVO response = factory.getGestioneAcquisizioni()
					.invoke(cmd);
			response.throwError();

			return (Boolean) response.getResult();

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public List<InventarioVO> esisteInventarioDigitalizzato(
			StrutturaInventariOrdVO inv, List<String> invEsclusi)
			throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_ESISTE_INVENTARIO_DIGITALIZZATO, inv.copy(),
				new ArrayList<String>(invEsclusi));
		try {
			CommandResultVO response = factory.getGestioneAcquisizioni()
					.invoke(cmd);
			response.throwError();

			return (List<InventarioVO>) response.getResultAsCollection(InventarioVO.class);

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	public CambioVO getValutaRiferimento(String codPolo, String codBib)
			throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_CERCA_VALUTA_RIFERIMENTO, codPolo, codBib);
		try {
			// almaviva5_20140612 #5078
			CommandResultVO response = factory.getGestioneAcquisizioni()
					.invoke(cmd);
			response.throwError();

			return (CambioVO) response.getResult();

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public BigDecimal importoInventariOrdine(OrdiniVO ordine) throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_IMPORTO_INVENTARI_ORDINE, ordine.copy());
		try {
			// almaviva5_20140618 #4967
			CommandResultVO response = factory.getGestioneAcquisizioni().invoke(cmd);
			response.throwError();

			return (BigDecimal) response.getResult();

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public int countInventariOrdine(OrdiniVO ordine) throws Exception {
		CommandInvokeVO cmd = CommandInvokeVO.build(utente.getTicket(),
				CommandType.ACQ_COUNT_INVENTARI_ORDINE, ordine.copy());
		try {
			// almaviva5_20140618 #4967
			CommandResultVO response = factory.getGestioneAcquisizioni().invoke(cmd);
			response.throwError();

			return (Integer) response.getResult();

		} catch (SbnBaseException e) {
			throw e;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public List<BuoniOrdineVO> ricercaBuoniOrdine(
			ListaSuppBuoniOrdineVO ricerca) throws Exception {
		try {
			List<BuoniOrdineVO> listaBuoniOrdine = factory
					.getGestioneAcquisizioni().getRicercaListaBuoniOrd(ricerca);
			return listaBuoniOrdine;
		} catch (ValidationException e) {
			if (e.getError() == ValidationExceptionCodici.assenzaRisultati)
				return null;
			throw e;
		}
	}

	public boolean isOkAttivita(String attivita,
			String codPolo, String codBib) throws Exception {
		try {
			utenteEJB.checkAttivita(attivita, codPolo, codBib, null);
			return true;
		} catch (UtenteNotAuthorizedException ute) {
			return false;
		}

	}

	public boolean modificaBuonoOrdine(BuoniOrdineVO buono) throws Exception {
		return factory.getGestioneAcquisizioni().modificaBuonoOrd(buono);
	}

	public int countRigheFatturaOrdine(OrdiniVO ordine) throws SbnBaseException {
		try {
			return DomainEJBFactory.getInstance().getAcquisizioni().countRigheFatturaOrdine(ordine);
		} catch (SbnBaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

}
