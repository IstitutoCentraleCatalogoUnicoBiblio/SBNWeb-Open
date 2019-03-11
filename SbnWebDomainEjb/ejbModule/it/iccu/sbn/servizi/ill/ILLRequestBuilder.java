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
package it.iccu.sbn.servizi.ill;

import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.api.impl.ILLMessageHandler;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.ConvertiVo.ConvertToXML;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.ill.apdu.AmountType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.AnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CancelReplyType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CancelType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CheckedInType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ClientIdType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ConditionalReplyType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ConditionalType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CostInfoType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CostType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CurrentState;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DamagedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateCheckedIn;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateDue;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateForReply;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateOfLastTransiction;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateReceived;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateReturned;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateShipped;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryAddressType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryService;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DeliveryType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DesiredDueDate;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ElectronicAddress;
import it.iccu.sbn.vo.xml.binding.ill.apdu.EstimateDateAvailable;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ExpiredType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ExpiryDate;
import it.iccu.sbn.vo.xml.binding.ill.apdu.HistoryReport;
import it.iccu.sbn.vo.xml.binding.ill.apdu.HoldPlaced;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLBiblioType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.LostType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MaximumCost;
import it.iccu.sbn.vo.xml.binding.ill.apdu.MessageType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.NeedBeforeDate;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ObjectFactory;
import it.iccu.sbn.vo.xml.binding.ill.apdu.OverdueType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.PostalAddress;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RecallType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ReceivedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RenewAnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RenewType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RequesterNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ResponderNote;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ReturnedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ServiceDateTimeType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ShippedType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusOrErrorReportType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusQueryType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusReportType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.SupplyDetails;
import it.iccu.sbn.vo.xml.binding.ill.apdu.SupplyMediumTypeType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.TransactionIdType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.Unfilled;
import it.iccu.sbn.vo.xml.binding.ill.apdu.WillSupply;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.xml.sax.SAXException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class ILLRequestBuilder {

	static Logger log = Logger.getLogger(ILLRequestBuilder.class);

	public static final String APDU_XML_ENCODING = "iso-8859-1";

	public static final String PARAM_BUFFER_ILL = "BufferILL";

	private static final String CONTENT_TYPE = "application/xml";
	private static final String CONTENT_TYPE_HEADER = CONTENT_TYPE + "; charset=" + APDU_XML_ENCODING;

	private static final String PARAM_FASE_ILL = "faseILL";

	private static JAXBContext ctx;
	protected static Schema apdu_xsd;

	private static ObjectFactory factory;
	
	static ParametriHttp parametriHttp;

	static {
		try {
			factory = new ObjectFactory();
			ctx = JAXBContext.newInstance(ILLAPDU.class.getPackage().getName());
			SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			URL xsd_url = ILLRequestBuilder.class.getResource("/META-INF/ILL/ill-apdu-fixed.xsd");
			apdu_xsd = sf.newSchema(xsd_url);
		} catch (JAXBException e) {
			log.error("", e);
		} catch (SAXException e) {
			log.error("", e);
		}
		
		try {
			parametriHttp = DomainEJBFactory.getInstance().getPolo().getIndice();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static ILLAPDU unmarshal(String xml) throws JAXBException {
		try {
			return _unmarshal(xml);
		} catch (JAXBException e) {
			log.error("", e);
		}

		//fix xml con entity errate da server ill
		xml = StringEscapeUtils.unescapeXml(xml);
		return _unmarshal(xml);
	}

	private static ILLAPDU _unmarshal(String xml) throws JAXBException {
		Unmarshaller um = ctx.createUnmarshaller();
		//um.setSchema(apdu_xsd);
		ILLAPDU apdu = (ILLAPDU) um.unmarshal(new StringReader(xml));

		return apdu;
	}

	public static String marshal(ILLAPDU apdu) throws JAXBException {
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, APDU_XML_ENCODING);
		//m.setSchema(apdu_xsd);
		StringWriter sw = new StringWriter(4096);
		m.marshal(apdu, sw);

		return sw.toString();
	}

	private static ILLAPDU send(final ILLAPDU apdu) throws Exception {
		String xml = marshal(apdu);

		if (log.isDebugEnabled()) {
			try {
				log.debug("ILL APDU request: " + xml);
				String path = CommonConfiguration.getProperty(Configuration.ILL_XML_SAVE_PATH, FileUtil.getTempFilesDir() );
				String fileName = path + File.separator + getOutXMLName(apdu);
				FileUtil.writeStringToFile(fileName, xml, APDU_XML_ENCODING);
			} catch (Exception e) {	}
		}

		URI uri;
		try {
			final String url = CommonConfiguration.getProperty(Configuration.ILL_SBN_SERVER_URL);
			uri = new URI(url);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_CONFIGURAZIONE_ILL_INCOMPLETA);
		}
		//xml = "Øyvind";
		ClientRequest request = getClient(uri);
		request.header("Content-Type", CONTENT_TYPE_HEADER)
			.queryParameter(PARAM_FASE_ILL, "DRS0")
			.queryParameter(PARAM_BUFFER_ILL, URLEncoder.encode(xml, APDU_XML_ENCODING) );

		log.debug("apdu final URI: " + request.getUri());

		ClientResponse<ILLAPDU> response;
		try {
			response = request.accept(CONTENT_TYPE).post(ILLAPDU.class);
			Status status = response.getResponseStatus();
			if (status != Status.OK)
				throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_SERVER,
						String.format("%d - %s", status.getStatusCode(), status.getReasonPhrase()));
		} catch (SbnBaseException e) {
			throw e;

		} catch (IOException e) {
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_SERVER, e, e.toString() );
		}

		ILLAPDU output = response.getEntity();
		if (log.isDebugEnabled()) {
			String xml_res = marshal(output);
			log.debug("ILL APDU response: " + xml_res);
		}
		return output;

	}

	private static ClientRequest getClient(URI uri) {
		
		HttpClient httpClient = new HttpClient();
		ClientExecutor executor = new ApacheHttpClientExecutor(httpClient);
		
		if (parametriHttp.isUSE_PROXY()) {
			//configurazione proxy
			String user = parametriHttp.getPROXY_USER();
			String ppwd = parametriHttp.getPROXY_PWD();
			String proxy_URL = parametriHttp.getPROXY_URL();
			int proxy_PORT = parametriHttp.getPROXY_PORT();
			
			ProxyHost proxy = new ProxyHost(proxy_URL, proxy_PORT);
			httpClient.getHostConfiguration().setProxyHost(proxy);
			log.debug("connessione via proxy: " + proxy_URL + ":" + proxy_PORT);

			if (ValidazioneDati.isFilled(user) && ValidazioneDati.isFilled(ppwd) ) {
				UsernamePasswordCredentials proxy_login = new UsernamePasswordCredentials(user, ppwd);
				AuthScope scope = new AuthScope(AuthScope.ANY_HOST,
						AuthScope.ANY_PORT, AuthScope.ANY_REALM,
						AuthScope.ANY_SCHEME);
				httpClient.getState().setProxyCredentials(scope, proxy_login);
			}
		}
	
		ClientRequest request = new ClientRequest(uri.toString(), executor);
		request.header("Content-Type", CONTENT_TYPE_HEADER);
		return request;
	}

	private static String getOutXMLName(ILLAPDU apdu) throws ApplicationException {
		ILLMessageHandler handler = ILLMessageHandler.getHandler(SbnSIP2Ticket.DUMMY_TICKET, apdu);
		if (handler != null)
			try {
				return "out_" + handler.getXMLName();
			} catch (Exception e) {	}

		return String.format("out_apdu_%d.xml", System.currentTimeMillis() );
	}

	public static String getInXMLName(String ticket, String xml, String targetIsil) throws JAXBException, ApplicationException {
		ILLAPDU apdu = unmarshal(xml);
		ILLMessageHandler handler = ILLMessageHandler.getHandler(ticket, apdu);
		if (handler != null) {
			handler.setTarget(targetIsil);
			return "in_" + handler.getXMLName();
		}

		return String.format("in_apdu_%d.xml", System.currentTimeMillis() );
	}

	public static ILLAPDU executeHandler(String ticket, String xml, String targetIsil) throws JAXBException, ApplicationException {
		ILLAPDU apdu = unmarshal(xml);
		return executeHandler(ticket, apdu, targetIsil);
	}

	public static ILLAPDU executeHandler(String ticket, ILLAPDU apdu, String targetIsil) throws JAXBException, ApplicationException {

		ILLMessageHandler handler = ILLMessageHandler.getHandler(ticket, apdu);
		ILLAPDU output = null;
		try {
			handler.setTarget(targetIsil);
			handler.execute();
			output = handler.output();
		} catch (ValidationException e) {

		} catch (ApplicationException e) {
			throw e;
		}

		return output;
	}

	public static void checkResponse(ILLAPDU response) throws ApplicationException {
		if (response == null)
			return;

		StatusOrErrorReportType statusOrErrorReport = response.getStatusOrErrorReport();
		if (statusOrErrorReport != null) {
			String errorReport = statusOrErrorReport.getErrorReport();
			if (ValidazioneDati.isFilled(errorReport) && !errorReport.equals("0")) {
				throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_SERVER, errorReport);
			}
		}
	}

	public static List<ILLBiblioType> getListaBibliotecheILL() throws Exception {

		List<ILLBiblioType> bib = Collections.emptyList();
		URI uri = UriBuilder.fromUri(CommonConfiguration.getProperty(Configuration.ILL_SBN_SERVER_URL))
					.queryParam(PARAM_FASE_ILL, "{arg1}")
					.build("LISTABIBLIOTECHE.XML.ALL");

		ClientRequest request = new ClientRequest(uri.toASCIIString());

		ClientResponse<ILLAPDU> response = request.accept(CONTENT_TYPE).get(ILLAPDU.class);
		Status status = response.getResponseStatus();
		if (status != Status.OK)
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_SERVER,
					String.format("%d - %s", status.getStatusCode(), status.getReasonPhrase()));

		ILLAPDU apdu = response.getEntity();
		if (apdu != null)
			bib = apdu.getBib();

		return bib;
	}

	public static ILLAPDU statusOrErrorReport(long tid, String requesterId, String responderId, Date date) throws Exception {

		ILLAPDU apdu = factory.createILLAPDU();

		StatusOrErrorReportType sq = factory.createStatusOrErrorReportType();
		if (isFilled(tid))
			sq.setTransactionId(buildTransactionId(tid));

		if (isFilled(requesterId))
			sq.setRequesterId(requesterId);

		if (isFilled(responderId))
			sq.setResponderId(responderId);

		StatusReportType sr = factory.createStatusReportType();
		HistoryReport hr = factory.createHistoryReport();

		ConvertToXML conv = ConversioneHibernateVO.toXML();
		if (date != null) {
			DateOfLastTransiction dlt = factory.createDateOfLastTransiction();
			dlt.setDate(conv.convertiIllDate(date));
			hr.setDateOfLastTransiction(dlt);
		}
/*
		if (isFilled(state)) {
			CurrentState cr = factory.createCurrentState();
			cr.setState(state);
			MostRecentService mrs = factory.createMostRecentService();
			mrs.setCurrentState(cr);
			hr.setMostRecentService(mrs);
		}
*/
		sr.setHistoryReport(hr);
		sq.setStatusReport(sr);

		apdu.setStatusOrErrorReport(sq);
		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU statusQuery(long tid, String requesterId, String responderId, Date date, String state) throws Exception {

		ILLAPDU apdu = factory.createILLAPDU();

		StatusQueryType sq = factory.createStatusQueryType();
		if (isFilled(requesterId))
			sq.setRequesterId(requesterId);

		if (tid > 0)
			sq.setTransactionId(buildTransactionId(tid));

		if (isFilled(responderId))
			sq.setResponderId(responderId);

		ConvertToXML conv = ConversioneHibernateVO.toXML();
		if (date != null) {
			ServiceDateTimeType dt = conv.convertiIllDateTime(date);
			sq.setServiceDateTime(dt);
		}

		apdu.setStatusQuery(sq);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU inoltraRichiestaAdAltraBiblioteca(MovimentoVO mov, MessaggioVO msg) throws Exception {

		ConvertToXML conv = ConversioneHibernateVO.toXML();
		DatiRichiestaILLVO dati = mov.getDatiILL();

		ILLAPDU apdu = factory.createILLAPDU();
		ILLRequestType req = factory.createILLRequestType();
		req.setILLSERVICETYPE(ILLServiceType.fromValue(dati.getServizio()));
		req.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		req.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));

		req.setServiceDateTime(conv.convertiIllDateTime(mov.getDataRichiesta()));
		CurrentState state = factory.createCurrentState();
		state.setState(msg.getStato());
		req.setCurrentState(state);

		// tipo supporto
		SupplyMediumTypeType supp = factory.createSupplyMediumTypeType();
		supp.setSupplyTyp(dati.getCd_supporto());
		req.getSupplyMediumType().add(supp);

		// tipo erogazione
		DeliveryService deliveryService = factory.createDeliveryService();
		DeliveryType deliveryType = DeliveryType.fromValue(dati.getCod_erog());
		deliveryService.setDelivTyp(deliveryType);
		deliveryService.setValue(dati.getRequester_email());
		req.setDeliveryService(deliveryService);

		// dati utente
		ClientIdType user = factory.createClientIdType();
		user.setClientIdentifier(mov.getCodUte());
		user.setClientName(dati.getCognomeNome());
		user.setEmail(mov.getEmail());
		req.setClientId(user);

		// indirizzo bib
		DeliveryAddressType address = factory.createDeliveryAddressType();
		/*
		<Delivery-address>
	      <Postal-address>
	        <Street-and-number>P.le Aldo Moro, 5</Street-and-number>
	        <City>Roma</City>
	        <Postal-code>00185</Postal-code>
	        <Region>RM</Region>
	      </Postal-address>
	      <Electronic-address>s.almaviva5@almaviva.it</Electronic-address>
	    </Delivery-address>
	    */
		PostalAddress pa = factory.createPostalAddress();
		pa.setStreetAndNumber(dati.getVia());
		pa.setCity(dati.getComune());
		pa.setPostalCode(dati.getCap());
		pa.setRegion(dati.getProv());
		pa.setCountry(dati.getCd_paese());
		address.setPostalAddress(pa);

		ElectronicAddress ea = factory.createElectronicAddress();
		ea.setValue(dati.getRequester_email());
		address.setElectronicAddress(ea);

		req.setDeliveryAddress(address);

		// dati doc.
		req.setItemId(conv.costruisciItemId(dati.getDocumento() ));

		// note
		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			req.setRequesterNote(rn);
		}

		// data scadenza
		ExpiryDate expiryDate = factory.createExpiryDate();
		expiryDate.setDate(conv.convertiIllDate(mov.getDataFinePrev()));
		req.setExpiryDate(expiryDate);

		// costo
		String costoMax = mov.getPrezzoMax();
		if (isFilled(costoMax)) {
			CostInfoType costInfoType = factory.createCostInfoType();
			MaximumCost maxCost = factory.createMaximumCost();
			AmountType amount = conv.amount(mov.getPrezzoMaxDouble());
			maxCost.setAmount(amount);
			costInfoType.setMaximumCost(maxCost);
			req.setCostInfoType(costInfoType);
		}

		// data max interesse
		Date dataMax = mov.getDataMax();
		if (dataMax != null) {
			NeedBeforeDate needBeforeDate = factory.createNeedBeforeDate();
			needBeforeDate.setDate(conv.convertiIllDate(dataMax));
			req.setNeedBeforeDate(needBeforeDate);
		}

		apdu.getILLRequest().add(req);
		ILLAPDU response = send(apdu);

		return response;
	}

	private static TransactionIdType buildTransactionId(long l) {
		TransactionIdType id = factory.createTransactionIdType();
		id.setValue(String.valueOf(l));
		return id;
	}

	public static ILLAPDU rifiutaRichiesta(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ILLAnswerType answer = factory.createILLAnswerType();
		answer.setTransactionId(buildTransactionId(dati.getTransactionId()));
		answer.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		answer.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		answer.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			answer.setResponderNote(rn);
		}

		AnswerType answerType = factory.createAnswerType();
		Unfilled unfilled = factory.createUnfilled();
		unfilled.setId(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode());
		answerType.setUnfilled(unfilled);
		answer.setAnswerType(answerType);

		apdu.setILLAnswer(answer);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU accettaRichiesta(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ILLAnswerType answer = factory.createILLAnswerType();
		answer.setTransactionId(buildTransactionId(dati.getTransactionId()));
		answer.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		answer.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		answer.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			answer.setResponderNote(rn);
		}

		AnswerType answerType = factory.createAnswerType();
		WillSupply willSupply = factory.createWillSupply();
		willSupply.setId(StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA.getISOCode());
		answerType.setWillSupply(willSupply);
		answer.setAnswerType(answerType);

		apdu.setILLAnswer(answer);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU spedizioneMateriale(MovimentoVO mov, MessaggioVO msg) throws Exception {
		DatiRichiestaILLVO dati = mov.getDatiILL();
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ShippedType shipped = factory.createShippedType();
		shipped.setId(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE.getISOCode());
		shipped.setTransactionId(buildTransactionId(dati.getTransactionId()));
		shipped.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		shipped.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		shipped.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			shipped.setResponderNote(rn);
		}

		SupplyDetails sd = factory.createSupplyDetails();
		//data invio
		DateShipped dateShipped = factory.createDateShipped();
		dateShipped.getDate().add(conv.convertiIllDate(msg.getDataMessaggio()));

		sd.setDateShipped(dateShipped);
		//fine prevista (per prestito)
		if (ILLConfiguration2.getInstance().isRichiestaPrestito(dati)) {
			DateDue dateDue = factory.createDateDue();
			dateDue.getDate().add(conv.convertiIllDate(mov.getDataFinePrev()));
			sd.setDateDue(dateDue);
		}

		double costo = mov.getCostoServizioDouble();
		if (isFilled(costo)) {
			CostType cost = new CostType();
			AmountType amount = conv.amount(costo);
			cost.getAmount().add(amount);
			sd.setCost(cost);
		}

		short numPezzi = mov.getNumPezziShort();
		sd.setChargeableUnits(String.valueOf(Math.max(1, numPezzi)));

//		ShippedViaType shippedViaType = factory.createShippedViaType();
//		sd.setShippedVia(shippedViaType);

		shipped.setSupplyDetails(sd);

		apdu.setShipped(shipped);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU restituzioneDaBibRichiedente(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		CheckedInType checkin = factory.createCheckedInType();
		checkin.setId(StatoIterRichiesta.F128_RICEZIONE_MATERIALE.getISOCode());
		checkin.setTransactionId(buildTransactionId(dati.getTransactionId()));
		checkin.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		checkin.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		checkin.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			checkin.setResponderNote(rn);
		}

		DateCheckedIn dateIn = factory.createDateCheckedIn();
		dateIn.getDate().add(conv.convertiIllDate(DaoManager.now()));
		checkin.setDateCheckedIn(dateIn);

		apdu.setCheckedIn(checkin);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU arrivoMateriale(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ReceivedType received = factory.createReceivedType();

		received.setId(StatoIterRichiesta.F114_ARRIVO_MATERIALE.getISOCode());
		received.setTransactionId(buildTransactionId(dati.getTransactionId()));
		received.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		received.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		received.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		DateReceived dateReceived = factory.createDateReceived();
		dateReceived.getDate().add(conv.convertiIllDate(msg.getDataMessaggio()));
		received.setDateReceived(dateReceived);

		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			received.setRequesterNote(rn);
		}

		apdu.setReceived(received);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU rispedizioneMateriale(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ReturnedType returned = factory.createReturnedType();
		returned.setId(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE.getISOCode());
		returned.setTransactionId(buildTransactionId(dati.getTransactionId()));
		returned.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		returned.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		returned.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			returned.setRequesterNote(rn);
		}

		DateReturned dateReturned = factory.createDateReturned();
		dateReturned.getDate().add(conv.convertiIllDate(msg.getDataMessaggio()));
		returned.setDateReturned(dateReturned);

		apdu.setReturned(returned);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU condizioneRichiesta(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ILLAnswerType answer = factory.createILLAnswerType();
		answer.setTransactionId(buildTransactionId(dati.getTransactionId()));
		answer.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		answer.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		answer.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			answer.setResponderNote(rn);
		}

		AnswerType answerType = factory.createAnswerType();
		ConditionalType condition = factory.createConditionalType();
		condition.setId(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA.getISOCode());
		condition.setConditions(msg.getCondizione());
		DateForReply dateForReply = factory.createDateForReply();
		//TODO da configurare?
		dateForReply.getDate().add(conv.convertiIllDate(DateUtil.addDay(DaoManager.now(), 7)));
		condition.setDateForReply(dateForReply);
		answerType.setConditional(condition);
		answer.setAnswerType(answerType);

		apdu.setILLAnswer(answer);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU rispostaCondizioneRichiesta(DatiRichiestaILLVO dati, MessaggioVO msg, boolean accettata) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ConditionalReplyType reply = factory.createConditionalReplyType();
		reply.setTransactionId(buildTransactionId(dati.getTransactionId()));
		reply.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		reply.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		reply.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			reply.setRequesterNote(rn);
		}

		//lo stato non contiene la lettera finale
		String flag = accettata ? "B" : "A";
		reply.setId(StringUtils.removeEnd(msg.getStato(), flag));
		reply.setAnswer(flag);

		apdu.setConditionalReply(reply);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU richiestaDiRinnovo(MovimentoVO mov, MessaggioVO msg) throws Exception {
		DatiRichiestaILLVO dati = mov.getDatiILL();
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		RenewType renew = factory.createRenewType();

		renew.setId(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO.getISOCode());
		renew.setTransactionId(buildTransactionId(dati.getTransactionId()));
		renew.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		renew.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		renew.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		DesiredDueDate desiredDueDate = factory.createDesiredDueDate();
		desiredDueDate.getDate().add(conv.convertiIllDate(mov.getDataProroga()));
		renew.setDesiredDueDate(desiredDueDate);

		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			renew.setRequesterNote(rn);
		}

		apdu.setRenew(renew);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU rispostaRichiestaDiRinnovo(MovimentoVO mov, MessaggioVO msg, boolean accettata) throws Exception {
		DatiRichiestaILLVO dati = mov.getDatiILL();
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		RenewAnswerType renewAnswer = factory.createRenewAnswerType();

		renewAnswer.setTransactionId(buildTransactionId(dati.getTransactionId()));
		renewAnswer.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		renewAnswer.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		renewAnswer.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		//fine prevista
		DateDue dateDue = factory.createDateDue();
		dateDue.getDate().add(conv.convertiIllDate(mov.getDataFinePrev()));
		renewAnswer.setDateDue(dateDue);

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			renewAnswer.setResponderNote(rn);
		}

		//lo stato non contiene la lettera finale
		String flag = accettata ? "9" : "A";
		renewAnswer.setId(StringUtils.removeEnd(msg.getStato(), flag));
		renewAnswer.setAnswer(flag);

		apdu.setRenewAnswer(renewAnswer);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU prenotaDocumento(MovimentoVO mov, MessaggioVO msg) throws Exception {
		DatiRichiestaILLVO dati = mov.getDatiILL();
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ILLAnswerType answer = factory.createILLAnswerType();
		answer.setTransactionId(buildTransactionId(dati.getTransactionId()));
		answer.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		answer.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		answer.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			answer.setResponderNote(rn);
		}

		AnswerType answerType = factory.createAnswerType();
		HoldPlaced hold = factory.createHoldPlaced();
		hold.setId(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO.getISOCode());

		//data disponibilità
		EstimateDateAvailable dateAvailable = factory.createEstimateDateAvailable();
		dateAvailable.getDate().add(conv.convertiIllDate(mov.getDataInizioPrev()));
		hold.setEstimateDateAvailable(dateAvailable);

		answerType.setHoldPlaced(hold);
		answer.setAnswerType(answerType);

		apdu.setILLAnswer(answer);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU propostaAnnullamentoRichiesta(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		CancelType cancel = factory.createCancelType();

		cancel.setId(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO.getISOCode());
		cancel.setTransactionId(buildTransactionId(dati.getTransactionId()));
		cancel.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		cancel.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		cancel.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			RequesterNote rn = factory.createRequesterNote();
			rn.getNote().add(note);
			cancel.setRequesterNote(rn);
		}

		apdu.setCancel(cancel);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU rispostaRichiestaDiAnnullamento(DatiRichiestaILLVO dati, MessaggioVO msg, boolean rifiuta) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		CancelReplyType cancelReply = factory.createCancelReplyType();

		cancelReply.setTransactionId(buildTransactionId(dati.getTransactionId()));
		cancelReply.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		cancelReply.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		cancelReply.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			cancelReply.setResponderNote(rn);
		}

		//lo stato non contiene la lettera finale
		int flag = rifiuta ? 1 : 0;
		cancelReply.setId(StringUtils.removeEnd(msg.getStato(), Integer.toString(flag)) );
		cancelReply.setAnswer(flag);

		apdu.setCancelReply(cancelReply);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU sollecitoRestituzioneUrgente(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		RecallType recall = factory.createRecallType();

		recall.setId(StatoIterRichiesta.F12E_RICHIESTA_RESTITUZIONE_URGENTE.getISOCode());
		recall.setTransactionId(buildTransactionId(dati.getTransactionId()));
		recall.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		recall.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		recall.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			recall.setResponderNote(rn);
		}

		apdu.setRecall(recall);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU sollecitoRestituzione(MovimentoVO mov, MessaggioVO msg) throws Exception {
		DatiRichiestaILLVO dati = mov.getDatiILL();
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		OverdueType overdue = factory.createOverdueType();

		overdue.setId(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO.getISOCode());
		overdue.setTransactionId(buildTransactionId(dati.getTransactionId()));
		overdue.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		overdue.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		overdue.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		String note = msg.getNote();
		if (isFilled(note)) {
			ResponderNote rn = factory.createResponderNote();
			rn.getNote().add(note);
			overdue.setResponderNote(rn);
		}

		//fine prevista
		DateDue dateDue = factory.createDateDue();
		dateDue.getDate().add(conv.convertiIllDate(mov.getDataFinePrev()));
		overdue.setDateDue(dateDue);

		apdu.setOverdue(overdue);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU termineScaduto(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		ExpiredType expired = factory.createExpiredType();

		expired.setId(StatoIterRichiesta.F1218_TERMINE_SCADUTO.getISOCode());
		expired.setTransactionId(buildTransactionId(dati.getTransactionId()));
		expired.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		expired.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		expired.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));

		apdu.setExpired(expired);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU inviaMessaggio(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		MessageType message = factory.createMessageType();

		message.setTransactionId(buildTransactionId(dati.getTransactionId()));
		message.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		message.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		message.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));
		message.setNote(msg.getNote());

		apdu.setMessage(message);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU materialeSmarrito(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		LostType lost = factory.createLostType();

		lost.setId(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO.getISOCode());
		lost.setTransactionId(buildTransactionId(dati.getTransactionId()));
		lost.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		lost.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		lost.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));
		lost.setNote(msg.getNote());

		apdu.setLost(lost);

		ILLAPDU response = send(apdu);

		return response;
	}

	public static ILLAPDU materialeDanneggiato(DatiRichiestaILLVO dati, MessaggioVO msg) throws Exception {
		ConvertToXML conv = ConversioneHibernateVO.toXML();

		ILLAPDU apdu = factory.createILLAPDU();
		DamagedType damaged = factory.createDamagedType();

		damaged.setId(StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO.getISOCode());
		damaged.setTransactionId(buildTransactionId(dati.getTransactionId()));
		damaged.setRequesterId(ServiziUtil.shortIsil(dati.getRequesterId()));
		damaged.setResponderId(ServiziUtil.shortIsil(dati.getResponderId()));
		damaged.setServiceDateTime(conv.convertiIllDateTime(msg.getDataMessaggio()));
		damaged.setNote(msg.getNote());

		apdu.setDamaged(damaged);

		ILLAPDU response = send(apdu);

		return response;
	}

}
