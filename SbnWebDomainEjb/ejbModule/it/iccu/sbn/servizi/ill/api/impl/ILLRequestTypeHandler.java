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
package it.iccu.sbn.servizi.ill.api.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.servizi.RichiesteServizioDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.api.ILLBaseAction;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ItemIdType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;
import java.util.Locale;

class ILLRequestTypeHandler extends ILLMessageHandler {

	private static final long serialVersionUID = 6368465477016874386L;
	private final List<ILLRequestType> requests;

	private BibliotecaILLVO requester;
	private BibliotecaILLVO responder;
	private RuoloBiblioteca ruolo;

	private String transactionId;
	private String oldState;
	private String newState;

	public ILLRequestTypeHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		requests = input.getILLRequest();
	}

	public void execute() throws ApplicationException, ValidationException {
		log.debug("Oggetti ILLRequestType: " + ValidazioneDati.size(requests));
		for (ILLRequestType ilt : requests)
			try {
				log.debug(ilt);
				requester = getBibliotecaByIsil(ilt.getRequesterId());
				if (requester == null)
					throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);

				responder = getBibliotecaByIsil(ilt.getResponderId());
				if (responder == null)
					throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);

				if (ValidazioneDati.equals(requester.getCodPolo(), getCodPolo())
						&& ValidazioneDati.equals(requester.getCodBib(), getCodBib()))
					ruolo = RuoloBiblioteca.RICHIEDENTE;
				else
					if (ValidazioneDati.equals(responder.getCodPolo(), getCodPolo())
						&& ValidazioneDati.equals(responder.getCodBib(), getCodBib()))
					ruolo = RuoloBiblioteca.FORNITRICE;
				else
					throw new ApplicationException(SbnErrorTypes.AMM_BIBLIOTECA_NON_TROVATA);

				if (ruolo == RuoloBiblioteca.RICHIEDENTE)
					executeAsRequester(ilt);
				else
					executeAsResponder(ilt);

			} catch (ApplicationException e) {
				throw e;

			} catch (Exception e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_VALIDAZIONE_APDU, e);
			}

	}

	private void executeAsResponder(ILLRequestType ilt) throws Exception {

		transactionId = ilt.getTransactionId().getValue();

		//1. ricerca richiesta ill
		DatiRichiestaILLVO dati_richiesta_ill = getDatiRichiestaIll(transactionId, ruolo, null, ilt.getResponderId() );

		//memorizzazione stato richiesta
		oldState = dati_richiesta_ill.getCurrentState();

		dati_richiesta_ill = ConversioneHibernateVO.fromXML().datiRichiestaILL(dati_richiesta_ill, ilt);
		dati_richiesta_ill.setRuolo(ruolo);

		//nuovo stato
		newState = dati_richiesta_ill.getCurrentState();
		log.debug(String.format("richiesta ill n. %s: stato: %s --> %s", transactionId, oldState, newState));
		if (ValidazioneDati.equals(oldState, newState))
			return;

		//2. validazione biblioteca/utente
		controllaUtenteBiblioteca(getCodPolo(), getCodBib(), requester);

		//4. ins/mod documento
		ItemIdType itemId = ilt.getItemId();
		if (itemId != null) {
			DocumentoNonSbnVO doc = dati_richiesta_ill.getDocumento();
			//ricerca documento già esistente (per segnatura)
			doc = doc != null ? doc : cercaDocumentoIll(transactionId, ruolo, itemId.getLocationNote(),
					ilt.getRequesterId(), ilt.getResponderId());
			//integrazione dati documento da xml
			doc = ConversioneHibernateVO.fromXML().documentoNonSbn(doc, itemId);

			List<DocumentoNonSbnVO> docs = getServizi().aggiornaDocumentoNonSbn(getTicket(), ValidazioneDati.asSingletonList(doc));
			dati_richiesta_ill.setDocumento(ValidazioneDati.first(docs));

			//collegamento inventario
			if (doc.hasInventario()) {
				try {
					InventarioVO inv = DomainEJBFactory.getInstance().getInventario().getInventario(getCodPolo(),
							doc.getCod_bib_inv(), doc.getCod_serie(), doc.getCod_inven(), Locale.getDefault(),
							getTicket());
					dati_richiesta_ill.setInventario(new InventarioTitoloVO(inv));
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}

		//5. ins/mod richiesta locale
		long idRichiesta = dati_richiesta_ill.getCod_rich_serv();
		if (idRichiesta > 0) {
			//aggiorna mov. locale + richiesta ill
			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			Tbl_richiesta_servizio rs = dao.getMovimentoById(idRichiesta);
			MovimentoVO mov = ServiziConversioneVO.daHibernateAWebMovimento(rs, Locale.getDefault());

			//3. controllo abilitazione utente
			ServizioBibliotecaVO servizio = getServizi().getServizioBiblioteca(getTicket(), mov.getCodPolo(),
					mov.getCodBibOperante(), mov.getCodTipoServ(), mov.getCodServ());

			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(mov, dati_richiesta_ill, servizio,
				StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState) );

			mov.setDatiILL(dati_richiesta_ill);
			getServizi().aggiornaRichiesta(getTicket(), mov, servizio.getIdServizio() );
		} else {
			//aggiorna solo richiesta ill
			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(null, dati_richiesta_ill, null,
					StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState) );
			aggiornaDatiRichiestaILL(dati_richiesta_ill);
		}

	}

	private void executeAsRequester(ILLRequestType ilt) throws Exception {

		transactionId = ilt.getTransactionId().getValue();

		//1. ricerca richiesta ill
		DatiRichiestaILLVO dati_richiesta_ill = getDatiRichiestaIll(transactionId, ruolo, ilt.getRequesterId(), null);
		//memorizzazione stato richiesta
		oldState = dati_richiesta_ill.getCurrentState();

		dati_richiesta_ill = ConversioneHibernateVO.fromXML().datiRichiestaILL(dati_richiesta_ill, ilt);
		dati_richiesta_ill.setRuolo(ruolo);

		//nuovo stato
		newState = dati_richiesta_ill.getCurrentState();
		log.debug(String.format("richiesta ill n. %s: stato: %s --> %s", transactionId, oldState, newState));
		if (ValidazioneDati.equals(oldState, newState))
			return;

		//2. validazione biblioteca/utente
		UtenteBibliotecaVO utente = getUtenteBiblioteca(dati_richiesta_ill.getCodUtente());
		if (utente != null)
			//fix nome utente
			dati_richiesta_ill.setCognomeNome(utente.getCognomeNome());
		else
			//utente biblioteca non valido o non trovato
			//throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_NON_TROVATO);
			log.warn(String.format("Utente '%s' non trovato o non iscritto a biblioteca %s%s", dati_richiesta_ill.getCodUtente(), getCodPolo(), getCodBib()) );

		//4. ins/mod documento
		ItemIdType itemId = ilt.getItemId();
		if (itemId != null) {
			DocumentoNonSbnVO doc = dati_richiesta_ill.getDocumento();
			//ricerca documento già esistente (per segnatura)
			doc = doc != null ? doc : cercaDocumentoIll(transactionId, ruolo, itemId.getLocationNote(),
					ilt.getRequesterId(), ilt.getResponderId());
			//integrazione dati documento da xml
			doc = ConversioneHibernateVO.fromXML().documentoNonSbn(doc, itemId);

			List<DocumentoNonSbnVO> docs = getServizi().aggiornaDocumentoNonSbn(getTicket(), ValidazioneDati.asSingletonList(doc));
			dati_richiesta_ill.setDocumento(ValidazioneDati.first(docs));
		}

		//5. ins/mod richiesta locale
		long idRichiesta = dati_richiesta_ill.getCod_rich_serv();
		if (idRichiesta > 0) {
			//utente necessario se presente mov. locale
			if (utente == null)
				throw new ApplicationException(SbnErrorTypes.SRV_UTENTE_NON_TROVATO);

			//aggiorna mov. locale + richiesta ill
			RichiesteServizioDAO dao = new RichiesteServizioDAO();
			Tbl_richiesta_servizio rs = dao.getMovimentoById(idRichiesta);
			MovimentoVO mov = ServiziConversioneVO.daHibernateAWebMovimento(rs, Locale.getDefault());
			//3. controllo abilitazione utente
			ServizioBibliotecaVO servizio = getServizi().getServizioBiblioteca(getTicket(), mov.getCodPolo(),
					mov.getCodBibOperante(), mov.getCodTipoServ(), mov.getCodServ());

			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(mov, dati_richiesta_ill, servizio,
					StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState) );

			mov.setDatiILL(dati_richiesta_ill);
			getServizi().aggiornaRichiesta(getTicket(), mov, servizio.getIdServizio() );
		} else {
			//aggiorna solo richiesta ill
			ILLConfiguration2.getInstance().controllaCambiamentoDiStato(null, dati_richiesta_ill, null,
					StatoIterRichiesta.of(oldState), StatoIterRichiesta.of(newState) );
			aggiornaDatiRichiestaILL(dati_richiesta_ill);
		}

	}

	private DocumentoNonSbnVO cercaDocumentoIll(String tid, RuoloBiblioteca ruolo, String loc, String reqId, String resId) throws Exception {
		//almaviva5_20151202 la biblioteca fornitrice crea un doc di tipo P, la richiedente D
		char tipoDoc = (ruolo == RuoloBiblioteca.FORNITRICE) ? 'P' : 'D';

		DocumentoNonSbnRicercaVO docRicerca = new DocumentoNonSbnRicercaVO();
		docRicerca.setTipo_doc_lett(tipoDoc);

		//collocazione da opac o generata in modo automatico (se di tipo P)
		String segn = "";
		if (ValidazioneDati.isFilled(loc))
			segn = loc;
		else
			if (tipoDoc == 'P')	//segnatura obbligatoria
				segn = ServiziUtil.generaFakeSegnaturaDocIll(tid, ruolo, reqId, resId);
		docRicerca.setSegnatura(segn);

		docRicerca.setOrd_segnatura(OrdinamentoCollocazione2.normalizza(segn));
		docRicerca.setCodPolo(getCodPolo());
		docRicerca.setCodBib(getCodBib());

		if (ruolo == RuoloBiblioteca.RICHIEDENTE)
			return docRicerca;	//doc. tipo D va sempre creato ex novo.

		List<DocumentoNonSbnVO> docs = getServizi().getListaDocumentiNonSbn(getTicket(), docRicerca);
		return ValidazioneDati.isFilled(docs) ? docs.get(0) : docRicerca;
	}

	@Override
	protected void valorizzaDatiBase(DatiRichiestaILLVO dri, ILLBaseAction ilt) {
		dri.setRequesterId(ilt.getRequesterId());
		dri.setResponderId(ilt.getResponderId());
		String tid = ilt.getTransactionId().getValue();
		dri.setTransactionId(new Long(tid));
	}

	public String getXMLName() {
		StringBuilder buf = new StringBuilder(32);
		ILLRequestType ilt = ValidazioneDati.first(requests);
		boolean withId = ilt.getTransactionId() != null;

		if (withId) {
			//richiesta già presente su SBN ILL
			long tid = Long.valueOf(ilt.getTransactionId().getValue());
			buf.append(String.format("%06d", tid));
		} else {
			buf.append("req_from_")
				.append(ilt.getRequesterId())
				.append("_to_")
				.append(ilt.getResponderId());
		}

		buf.append('_')
			.append(ilt.getILLSERVICETYPE().name());
		String isil = getTarget();
		if (isFilled(isil))
			buf.append('_')
				.append(isil);

		buf.append(".xml");
		return buf.toString();
	}

	public ILLAPDU output() {
		return null;
	}

}
