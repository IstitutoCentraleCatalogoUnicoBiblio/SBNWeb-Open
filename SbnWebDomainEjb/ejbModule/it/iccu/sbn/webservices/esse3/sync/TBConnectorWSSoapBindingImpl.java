/**
 * TBConnectorWSSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.iccu.sbn.webservices.esse3.sync;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.esse3.conf.Esse3BibConfigEntry;
import it.iccu.sbn.ejb.domain.servizi.esse3.conf.Esse3ConfigProvider;
import it.iccu.sbn.ejb.domain.servizi.esse3.xmlMarshall.Esse3DataModelBuilder;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

public class TBConnectorWSSoapBindingImpl implements it.iccu.sbn.webservices.esse3.sync.TBConnectorWS {

	static Logger log = Logger.getLogger(TBConnectorWS.class);
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm_");

	private void saveParameters(String codBib, String apikey, String entity, String op, String sourceId, String xmlData, Throwable t) {
		try {
			String dateFileName = simpleDateFormat.format(DaoManager.now());

			StringBuilder dataBuilder = new StringBuilder();
			dataBuilder.append(dateFileName).append(" - ").append("Dati Ricevuti da Esse3 ws: ").append("\n")
					.append("- codBib:   ").append(codBib).append("\n")
					.append("- apikey:   ").append(apikey).append("\n")
					.append("- entity:   ").append(entity).append("\n")
					.append("- op:       ").append(op).append("\n")
					.append("- sourceId: ").append(sourceId).append("\n")
					.append("- csvData:  ").append(xmlData);

			if (t != null) {
				StringWriter trace = new StringWriter();
				t.printStackTrace(new PrintWriter(trace));
				dataBuilder.append("\n").append("- error:    ").append(trace.toString());
			}

			String fileName = dateFileName + IdGenerator.getId() + "-esse3_call.log";
			String userHome = FileUtil.getUserHomeDir();
			// almaviva3 04/04/2019
			// Raggruppati i log di Esse3 WS in una cartella specifica, se non esiste la creo
			// Testato in locale, funziona. Da testare a runtime
			String logDir = userHome + File.separator + "esse3WS_logs";
			File file = new File(logDir);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathLogFile = logDir + File.separator + fileName;
			FileUtil.writeStringToFile(pathLogFile, dataBuilder.toString());

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public java.lang.String sync(java.lang.String pass, java.lang.String entity, java.lang.String op,
			java.lang.String sourceId, byte[] cvXml) throws java.rmi.RemoteException {
		log.info("------------- ELABORAZIONE DATI WS da chiamata ESSE3 -----------------");

		String xml_data = null;
		String codBib = null;
		Throwable error = null;
		try {
			xml_data = new String(cvXml, "UTF-8");
			//Marshal
			List<PERSONA> persone = Esse3DataModelBuilder.unmarshalToSingleValueList(xml_data);

			// Lettura configurazione esse3 da file
			Esse3BibConfigEntry bibConfig = Esse3ConfigProvider.fromApikey(pass);
			if (bibConfig == Esse3ConfigProvider.EMPTY) {
				throw new Exception("api-key non riconosciuto");
			}
			log.debug("configurazione esse3 caricata: " + bibConfig);

			codBib = bibConfig.getCd_bib();
			String codPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();
			String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, Constants.UTENTE_WEB_TICKET, InetAddress.getLocalHost());

			Boolean updated = DomainEJBFactory.getInstance().getServiziBMT().aggiornaUtentiESSE3(codPolo, codBib, ticket, persone);
			return updated ? "1" : "0";
		} catch (Exception e) {
			log.error("Errore WS esse3", e);
			error = e;
			return "ERRORE";

		} finally {
			saveParameters(codBib, pass, entity, op, sourceId, xml_data, error);
			log.info("-------------- FINE ELABORAZIONE DATI ESSE3 ----------------");
		}

	}

}
