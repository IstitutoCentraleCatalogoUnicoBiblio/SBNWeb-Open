/**
 * TBConnectorWSSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.iccu.sbn.webservices.esse3.sync;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.servizi.esse3.xmlMarshall.Esse3DataModelBuilder;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;
import it.iccu.sbn.web2.util.Constants;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

public class TBConnectorWSSoapBindingImpl implements it.iccu.sbn.webservices.esse3.sync.TBConnectorWS {

	static Logger log = Logger.getLogger(TBConnectorWS.class);
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm_");

	private void saveParameters(String pass, String entity, String op, String sourceId, String xmlData) {
		try {
			String dateFileName = simpleDateFormat.format(DaoManager.now());

			StringBuilder dataBuilder = new StringBuilder();
			dataBuilder.append(dateFileName).append(" - ").append("Dati Ricevuti da Esse3 ws: ").append("\n")
					.append("- entity:	 ").append(entity).append("\n").append("- op:      ").append(op).append("\n")
					.append("- sourceId:").append(sourceId).append("\n").append("- csvData: ").append(xmlData);

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

		// log.debug(this);
		try {

			AmministrazionePolo polo = DomainEJBFactory.getInstance().getPolo();
			String codPolo = polo.getInfoPolo().getCd_polo();

			// Inserire ESSE3_COD_BIB=\u0020LU in sbnweb.conf
			String codBib = CommonConfiguration.getProperty(Configuration.ESSE3_COD_BIB);
			String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, Constants.UTENTE_WEB_TICKET,
					InetAddress.getLocalHost());
			String xml_data = new String(cvXml, "UTF-8");
			//Marshal
			saveParameters(pass, entity, op, sourceId, xml_data);
			List<PERSONA> persone = Esse3DataModelBuilder.unmarshalToSingleValueList(xml_data);

			Boolean updated = DomainEJBFactory.getInstance().getServiziBMT().aggiornaUtentiESSE3(codPolo, codBib,
					ticket, persone);
			return updated ? "1" : "0";
		} catch (Exception e) {
			log.info("Errore WS esse3: " + e.getMessage());
			log.error("Errore WS esse3", e);
			return "ERRORE";

		} finally {
			log.info("-------------- FINE ELABORAZIONE DATI ESSE3 ----------------");

		}

	}

}
