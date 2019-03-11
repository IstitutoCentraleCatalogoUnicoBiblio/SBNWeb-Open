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
package it.iccu.sbn.servizi.ill.batch;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriAllineamentoBibliotecheILLVO;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.web2.util.Constants;

import java.net.InetAddress;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ServiziILLUpdateJob implements Job {

	private static final Logger log = Logger.getLogger(ServiziILLUpdateJob.class);

	static Reference<AmministrazionePolo> polo = new Reference<AmministrazionePolo>() {
		@Override
		protected AmministrazionePolo init() throws Exception {
			return DomainEJBFactory.getInstance().getPolo();
		}
	};

	static Reference<ServiziBMT> servizi = new Reference<ServiziBMT>() {
		@Override
		protected ServiziBMT init() throws Exception {
			return DomainEJBFactory.getInstance().getServiziBMT();
		}
	};

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			String codPolo = polo.get().getInfoPolo().getCd_polo();
			String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, Constants.ROOT_BIB, Constants.SBNDIF_USER, InetAddress.getLocalHost());

			ParametriAllineamentoBibliotecheILLVO richiesta = new ParametriAllineamentoBibliotecheILLVO();
			richiesta.setTicket(ticket);
			richiesta.setAllineaBiblioteche(true);
			richiesta.setAllineaRichieste(true);
			servizi.get().allineaServerILL(richiesta, null);

		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException(e);
		}
	}

/*
	private static final String LAST_SYNC_FILE = "ill-server-last-sync.properties";
	private static final String LAST_SYNC = "LAST_SYNC";

	static Reference<AmministrazionePolo> polo = new Reference<AmministrazionePolo>() {
		@Override
		protected AmministrazionePolo init() throws Exception {
			return DomainEJBFactory.getInstance().getPolo();
		}
	};

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

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		try {
			log.debug("Inizio sincronizzazione con server ILL SBN...");

			String codPolo = polo.get().getInfoPolo().getCd_polo();
			String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, Constants.ROOT_BIB, Constants.SBNDIF_USER, InetAddress.getLocalHost());

			try {
				//aggiornamento adesioni biblioteche
				updateBibliotecheILL(ticket);

				CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_LISTA_BIBLIOTECHE_POLO_ILL, codPolo);
				CommandResultVO result = servizi.get().invoke(command);
				result.throwError();

				@SuppressWarnings("unchecked")
				Map<String, BibliotecaVO> biblioteche =
					ValidazioneDati.listToMap((List<BibliotecaVO>) result.getResult(), String.class, "isil");

				//si richiedono le modifiche a partire dall'ultima sincronizzazione riuscita
				Date start = readLastSyncTime();
				ILLAPDU apdu = ILLRequestBuilder.statusOrErrorReport(0, null, null, start);
				ILLRequestBuilder.checkResponse(apdu);

				List<ILLRequestType> richieste = apdu.getILLRequest();
				log.debug(String.format("richieste ILL modificate al %tF: %d",  start, richieste.size()) );
				for (ILLRequestType r : richieste) {
					//check richiedente
					updateRichiestaILL(codPolo, biblioteche, r);
				}

				writeLastSyncTime(DaoManager.now());

			} catch (SbnBaseException e) {
				log.error("", e);
			}

			log.debug("Fine sincronizzazione con server ILL SBN.");

		} catch (Exception e) {
			log.error("", e);
			throw new JobExecutionException(e);
		}
	}

	private void updateBibliotecheILL(String ticket) {
		try {
//			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_ALLINEA_BIBLIOTECHE);
//			CommandResultVO result = servizi.get().invoke(command);
//			result.throwError();
		} catch (Exception e) {
			log.error("updateBibliotecheILL: ", e);
		}
	}

	private void updateRichiestaILL(String codPolo, Map<String, BibliotecaVO> biblioteche, ILLRequestType r) {
		// scheletro xml ill
		ILLAPDU apdu = new ILLAPDU();
		apdu.getILLRequest().add(r);

		String country = Locale.getDefault().getCountry();
		try {
			String requesterId = ServiziUtil.formattaIsil(r.getRequesterId(), country);
			BibliotecaVO bibRich = biblioteche.get(requesterId);
			if (bibRich != null) {
				String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, bibRich.getCod_bib(), Constants.SBNDIF_USER, InetAddress.getLocalHost());
				CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_XML_REQUEST, apdu, requesterId);
				CommandResultVO result = servizi.get().invoke(command);
				result.throwError();
			}

		} catch (Exception e) {
			log.error("updateRichiestaILL: ", e);
		}

		try {
			String responderId = ServiziUtil.formattaIsil(r.getResponderId(), country);
			BibliotecaVO bibForn = biblioteche.get(responderId);
			if (bibForn != null) {
				String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, bibForn.getCod_bib(), Constants.SBNDIF_USER, InetAddress.getLocalHost());
				CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.SRV_ILL_XML_REQUEST, apdu, responderId);
				CommandResultVO result = servizi.get().invoke(command);
				result.throwError();
			}

		} catch (Exception e) {
			log.error("updateRichiestaILL: ", e);
		}
	}
*/

}
