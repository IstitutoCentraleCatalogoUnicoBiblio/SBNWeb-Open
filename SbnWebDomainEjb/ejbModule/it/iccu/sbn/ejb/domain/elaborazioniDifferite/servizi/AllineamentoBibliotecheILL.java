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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.servizi.ServiziIllDAO;
import it.iccu.sbn.persistence.dao.servizi.UtentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_biblioteca_ill;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.biblioteca.importer.BibliotecheImporter;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.Isil;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.Biblioteca;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLBiblioType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import com.annimon.stream.Optional;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.listToMap;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.size;

public class AllineamentoBibliotecheILL {

	private static final String LAST_SYNC_FILE = "ill-server-last-sync.properties";
	private static final String LAST_SYNC = "LAST_SYNC";

	private Logger log = Logger.getLogger(AllineamentoBibliotecheILL.class);
	private final ParametriAllineamentoBibliotecheILLVO richiesta;
	private final UserTransaction tx;
	private Map<String, BibliotecaVO> biblioteche;

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}
	};

	private Date readLastSyncTime() {
		Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(FileUtil.getUserHomeDir() + File.separator + LAST_SYNC_FILE);
			props.load(in);
			return DateUtil.toDateISO(props.getProperty(LAST_SYNC));
		} catch (Exception e) {
			//range standard: 7 giorni.
			return DateUtil.addDay(DaoManager.now(), -7);
		} finally {
			FileUtil.close(in);
		}
	}

	private void writeLastSyncTime(Date when) {
		FileOutputStream out = null;
		try {
			Properties props = new Properties();
			props.setProperty(LAST_SYNC, DateUtil.toFormatoIso(when));
			out = new FileOutputStream(FileUtil.getUserHomeDir() + File.separator + LAST_SYNC_FILE);
			props.store(out, "last sync date");
		} catch (Exception e) {

		} finally {
			FileUtil.close(out);
		}
	}

	public AllineamentoBibliotecheILL(UserTransaction tx, ParametriAllineamentoBibliotecheILLVO richiesta,
			BatchLogWriter blw) {
		this.tx = tx;
		this.richiesta = richiesta;
		log = blw != null ? blw.getLogger() : log;
	}

	public ElaborazioniDifferiteOutputVo allinea() throws SbnBaseException {
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		output.setStato(ConstantsJMS.STATO_ERROR);
		try {
			log.debug("Inizio sincronizzazione con server ILL SBN...");

			DaoManager.begin(tx);
			try {
				biblioteche = caricaBibliotecheConfigurateILL();
				if (!isFilled(biblioteche) ) {
					log.warn("connessione al server ILL non configurata.");
					return output;
				}

				//aggiornamento adesioni biblioteche
				if (richiesta.isAllineaBiblioteche())
					allineaBibliotecheILL();

				if (richiesta.isAllineaRichieste())
					allineaRichiesteILL();

				output.setStato(ConstantsJMS.STATO_OK);

			} catch (SbnBaseException e) {
				DaoManager.rollback(tx);
				log.error("", e);

			} finally {
				DaoManager.commit(tx);
			}

			log.debug("Fine sincronizzazione con server ILL SBN.");

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		}

		return output;
	}

	private void allineaRichiesteILL() throws Exception {
		String ticket = richiesta.getTicket();
		String codPolo = DaoManager.codPoloFromTicket(ticket);

		DaoManager.begin(tx);

		//si richiedono le modifiche a partire dall'ultima sincronizzazione riuscita
		Date start = readLastSyncTime();

		for (final String isil : biblioteche.keySet()) {
			log.debug("allineamento richieste della biblioteca: " + isil);

			try {
				updateAsRequester(codPolo, biblioteche, start, isil);
			} catch (Exception e) {
				log.error("updateRichiestaILL: " + e.getLocalizedMessage());
			}

			try {
				updateAsResponder(codPolo, biblioteche, start, isil);
			} catch (Exception e) {
				log.error("updateRichiestaILL: " + e.getLocalizedMessage());
			}

		}

		//ILLAPDU apdu = ILLRequestBuilder.statusOrErrorReport(0, null, null, start);
		DaoManager.commit(tx);

		writeLastSyncTime(DaoManager.now());
	}

	private void updateAsRequester(String codPolo, Map<String, BibliotecaVO> biblioteche, Date start, final String requesterId)
			throws Exception {

		ILLAPDU apdu = ILLRequestBuilder.statusOrErrorReport(0, requesterId, null, start);
		ILLRequestBuilder.checkResponse(apdu);
		List<ILLRequestType> richieste = apdu.getILLRequest();
		log.debug(String.format("updateAsRequester(): richieste ILL modificate al %tF: %d", start, size(richieste) ) );

		for (ILLRequestType request : richieste) {
			//check richiedente
			updateRichiestaILL(codPolo, biblioteche, request);
		}
	}

	private void updateAsResponder(String codPolo, Map<String, BibliotecaVO> biblioteche, Date start, final String responderId)
			throws Exception {

		ILLAPDU apdu = ILLRequestBuilder.statusOrErrorReport(0, null, responderId, start);
		ILLRequestBuilder.checkResponse(apdu);
		List<ILLRequestType> richieste = apdu.getILLRequest();
		log.debug(String.format("updateAsResponder(): richieste ILL modificate al %tF: %d", start, size(richieste) ) );

		for (ILLRequestType request : richieste) {
			//check richiedente
			updateRichiestaILL(codPolo, biblioteche, request);
		}
	}

	private Map<String, BibliotecaVO> caricaBibliotecheConfigurateILL() throws Exception {
		Map<String, BibliotecaVO> biblioteche = Collections.emptyMap();
		//check sbnweb configurato per colloquio con server ILL
		String url = CommonConfiguration.getProperty(Configuration.ILL_SBN_SERVER_URL);
		boolean url_present = isFilled(url);

		if (url_present) {
			//lista biblioteche abilitate a ILL
			String ticket = richiesta.getTicket();
			String codPolo = DaoManager.codPoloFromTicket(ticket);

			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_LISTA_BIBLIOTECHE_POLO_ILL, codPolo);
			CommandResultVO result = servizi.get().invoke(command);
			result.throwError();

			biblioteche = listToMap((List<BibliotecaVO>) result.getResultAsCollection(BibliotecaVO.class), String.class, "isil");
			log.debug("biblioteche del polo abilitate a ILL: " + size(biblioteche) );
		}

		return biblioteche;
	}

	private void updateRichiestaILL(String codPolo, Map<String, BibliotecaVO> biblioteche, ILLRequestType r) throws Exception {
		// scheletro xml ill
		ILLAPDU apdu = new ILLAPDU();
		apdu.getILLRequest().add(r);
		String country = Locale.getDefault().getCountry();

		try {
			String requesterId = ServiziUtil.formattaIsil(r.getRequesterId(), country);
			BibliotecaVO bibRich = biblioteche.get(requesterId);
			if (bibRich != null) {
				DaoManager.begin(tx);
				String tmp_ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, bibRich.getCod_bib(), "sbnill", InetAddress.getLocalHost());
				CommandInvokeVO command = CommandInvokeVO.build(tmp_ticket, CommandType.SRV_ILL_XML_REQUEST, apdu, requesterId);
				CommandResultVO result = servizi.get().invoke(command);
				result.throwError();
				DaoManager.commit(tx);
			}

		} catch (Exception e) {
			log.error("updateRichiestaILL: ", e);
			DaoManager.rollback(tx);
		}

		try {
			String responderId = ServiziUtil.formattaIsil(r.getResponderId(), country);
			BibliotecaVO bibForn = biblioteche.get(responderId);
			if (bibForn != null) {
				DaoManager.begin(tx);
				String tmp_ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, bibForn.getCod_bib(), "sbnill", InetAddress.getLocalHost());
				CommandInvokeVO command = CommandInvokeVO.build(tmp_ticket, CommandType.SRV_ILL_XML_REQUEST, apdu, responderId);
				CommandResultVO result = servizi.get().invoke(command);
				result.throwError();
				DaoManager.commit(tx);
			}

		} catch (Exception e) {
			log.error("updateRichiestaILL: ", e);
			DaoManager.rollback(tx);
		}
	}

	private void allineaBibliotecheILL() throws ApplicationException {
		try {
			String ticket = richiesta.getTicket();
			List<ILLBiblioType> bibliotecheILL = ILLRequestBuilder.getListaBibliotecheILL();
			if (isFilled(bibliotecheILL)) {
				log.debug("Biblioteche ILL lette da xml: " + bibliotecheILL.size() );
				//biblioteche caricate da export anagrafe biblioteche
				BibliotecheImporter importer = new BibliotecheImporter();
				importer.execute();

				ServiziIllDAO dao = new ServiziIllDAO();
				UtentiDAO udao = new UtentiDAO();

				String firmaUtente = DaoManager.getFirmaUtente(ticket);
				AmministrazioneBiblioteca biblioteca = DomainEJBFactory.getInstance().getBiblioteca();
				for (ILLBiblioType ibt : bibliotecheILL) {
					try {
						DaoManager.begin(tx);
						log.debug("Allineamento biblioteca ILL: " + ibt.getIdb() );
						String idb = ibt.getIdb();
						String isil = ServiziUtil.formattaIsil(idb, Locale.getDefault().getCountry());

						Optional<Biblioteca> imported = importer.find(isil); //dati anagrafe

						Tbl_biblioteca_ill tbl_bibIll = dao.getBibliotecaByIsil(isil, false);
						BibliotecaILLVO bibIll = ConversioneHibernateVO.fromXML()
								.bibliotecaILL(ConversioneHibernateVO.toWeb().bibliotecaILL(tbl_bibIll, null), ibt);
						bibIll.setIsil(isil);

						if (bibIll.isNuovo()) {
							// nuova biblioteca, aggancio a anagrafica biblioteche
							BibliotecaRicercaVO richiesta = new BibliotecaRicercaVO();
							richiesta.setCodiceAna(isil);
							BibliotecaVO anagBib = first(biblioteca.cercaBiblioteche(ticket, richiesta));

							if (anagBib == null) {
								// biblioteca non trovata per isil, viene tentato il recupero dallo scarico
								// dell'anagrafe delle biblioteche italiane (http://opendata.anagrafe.iccu.sbn.it/biblioteche.zip)
								if (imported.isPresent()) {
									richiesta.setCodiceAna(null);
									richiesta.setCodicePolo(imported.get().getCodici_identificativi().getCodPolo());
									richiesta.setCodiceBib(imported.get().getCodici_identificativi().getCodBib());
									anagBib = first(biblioteca.cercaBiblioteche(ticket, richiesta));
									if (anagBib != null) {
										//aggiornamento codice isil
										anagBib.setCd_ana_biblioteca(Isil.parse(isil).getSuffisso());
										anagBib.setUteVar(firmaUtente);
										anagBib.setTsVar(DaoManager.now());
										biblioteca.updateBiblioteca(anagBib);
									}
								}
							}

							if (anagBib == null) {
								// non esiste la biblioteca, creazione anagrafica
								anagBib = new BibliotecaVO(bibIll);
								anagBib.setUteIns(firmaUtente);
								if (imported.isPresent()) {
									//codici sbn
									anagBib.setCod_polo(imported.get().getCodici_identificativi().getCodPolo());
									anagBib.setCod_bib(imported.get().getCodici_identificativi().getCodBib());
								}
								anagBib = biblioteca.creaBiblioteca(anagBib, firmaUtente, false, false, null);
							}
							bibIll.setBiblioteca(anagBib);
						}

						// verifica se la biblioteca è iscritta come utente
						bibIll.setUtente(ServiziConversioneVO.daHibernateAWebUtente(udao.getUtenteByIsil(bibIll.getIsil())));

						bibIll.setUteVar(firmaUtente);
						bibIll.setFlCanc("N");
						Tbl_biblioteca_ill biblioteca_ill = ConversioneHibernateVO.toHibernate().bibliotecaILL(null, bibIll);
						dao.aggiornaBiblioteca(biblioteca_ill);

						DaoManager.commit(tx);

					} catch (Exception e) {
						DaoManager.rollback(tx);
						log.error("Errore aggiornamento bib: " + ibt.getIdb() );
					}
				}
			}
		} catch (Exception e) {
			DaoManager.rollback(tx);
			throw new ApplicationException(e);
		}
	}

}
