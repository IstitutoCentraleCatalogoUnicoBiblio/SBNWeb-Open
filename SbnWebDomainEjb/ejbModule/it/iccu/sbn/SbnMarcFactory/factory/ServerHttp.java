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
package it.iccu.sbn.SbnMarcFactory.factory;

import gnu.trove.THashSet;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.custom.Credentials;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

/**
 * <p>
 * Questa classe ha il compito di:
 * </p>
 * <ol>
 * <li>stabilire una connessione al server SBN</li>
 * <li>inviare alla servlet la stringa xml con la richiesta</li>
 * <li>ritornare la stringa xml prodotta in risposta dalla servlet</li>
 * <li>Eseguire l'upload di un file sul server per l'import</li>
 * </ol>
 * Viene utilizzato il package <B>HttpClient</B> del progetto
 * Jakarta in quanto le API standard Java HttpUrlConnection non sono sufficienti
 * ad una gestione robusta della connessione HTTP, in particolare le API
 * HttpClient Jakarta permettono di impostare i timeout di connessione e di
 * risposta, cosa non permessa dalla normale classe URLConnection.
 *
 * @author Corrado Di Pietro
 * @author almaviva
 */
public class ServerHttp implements Runnable, ConfigChangeInterceptor {

	private static Logger log = Logger.getLogger("sbnmarc");

	// property per config interceptor
	private static final String[] OBSERVED_PROPS = new String[] {
		FactorySbn.URL_INDICE,
		FactorySbn.URL_POLO,
		Configuration.HTTP_CONNECTION_TIMEOUT,
		Configuration.HTTP_REQUEST_TIMEOUT,
		Configuration.HTTP_NUMERO_TENTATIVI
	};

	private static final String FORCE_RETRY = "::FORCE_RETRY::";

	static class SbnRetryHandler implements HttpMethodRetryHandler {

		int retries;
		boolean debug;

		SbnRetryHandler(int retries, boolean debug) {
			this.retries = retries;
			this.debug = debug;
		}

		public boolean retryMethod(final HttpMethod method, final IOException e, int executionCount) {
			log.warn(String.format("errore connessione (%s: %s), tentativo %d di %d", e.getClass().getSimpleName(), 
					e.getLocalizedMessage(), executionCount, retries));
			boolean check = checkError(method, e, executionCount);
			if (!check) {
				log.error("exception class: " + e.getClass().getCanonicalName() );
			}
			return check;
		}

		private boolean checkError(final HttpMethod method, final IOException e, int executionCount) {
			if (debug)
				return true;

			if (executionCount >= retries)
				return false;

			if (e instanceof NoHttpResponseException)
				return true;

			if (!method.isRequestSent())
				return true;

			return true;
		}
	};

	private HttpClient httpClient = null;
	private int httpReturnCode;
	private ParametriHttp param;
	private Credentials credentials;
	private String tipo;
	private HostConfiguration conf;
	private String asyncXmlRequest;

	//almaviva5_20091002 MODIFICHE PER TIMEOUT INDICE
	private StringBuilder xml_text_buffer;
	private char[] buf = new char[64 * 1024];	//64Kb
	private String httpId = "000000000";

	protected ServerHttp() {
		xml_text_buffer = new StringBuilder();
		CommonConfiguration.addInterceptor(this, FactorySbn.URL_POLO, FactorySbn.URL_INDICE);
	}

	public ServerHttp(ParametriHttp param, Credentials credentials)
			throws SbnMarcException {
		this();
		if (param == null)
			throw new SbnMarcException("Parametri di connessione non settati");
		this.param = param;
		if (credentials == null)
			throw new SbnMarcException("Credenziali non settati");
		this.credentials = credentials;
	}

	public ServerHttp(ParametriHttp param, Credentials credentials,
			String xmlRequest) throws SbnMarcException {
		this(param, credentials);
		this.asyncXmlRequest = xmlRequest;
	}

	private HostConfiguration setHostConfiguration() throws URIException {
		log.debug("connessione host: " + param.getURL_SERVLET());
		HostConfiguration hc = new HostConfiguration();
		hc.setHost(new URI(param.getURL_SERVLET(), false));

		//almaviva5_20111215 proxy indice
		if (param.isUSE_PROXY()) {
			ProxyHost proxy = new ProxyHost(param.getPROXY_URL(), param.getPROXY_PORT());
			hc.setProxyHost(proxy);
			log.debug("connessione via proxy: " + param.getPROXY_URL() + ":" + param.getPROXY_PORT());
		}

		httpClient.setHostConfiguration(hc);
		return hc;
	}

	private PostMethod makePOST_Chunked(String xmlRequest) {

		PostMethod httppost = new PostMethod(this.param.getURL_SERVLET());
		RequestEntity entity = null;
		try {
			entity = new StringRequestEntity(xmlRequest, "text/xml", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("httpId: " + httpId + " - ", e);
		}
		httppost.setRequestEntity(entity);

		// Modalità chunked
		//almaviva5_20120117 errore proxy HTTP/1.0
		if (!param.isCHUNKED()) {
			httppost.setContentChunked(false);
			httppost.addRequestHeader("Content-Length", String.valueOf(entity.getContentLength()) );
		} else
			httppost.setContentChunked(true);

		httppost.addRequestHeader("Cache-Control", "no-cache");
		return httppost;
	}

	private GetMethod makeGET_Normal(String xmlRequest) {
		GetMethod httppost = new GetMethod(param.getURL_SERVLET());
		if (ValidazioneDati.isFilled(xmlRequest))
			httppost.setQueryString(xmlRequest);

		return httppost;
	}

	private void configureHttpClient() throws URIException {

		if (httpClient == null) {

			this.httpClient = new HttpClient();
			HttpClientParams params = httpClient.getParams();

			params.setParameter(HttpClientParams.USER_AGENT, Constants.APP_NAME);
			params.setParameter(HttpClientParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			params.setParameter("http.connection.timeout", param.getHTTP_CONNECTION_TIMEOUT());
			params.setParameter(HttpClientParams.SO_TIMEOUT, param.getHTTP_REQUEST_TIMEOUT());

			params.setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
			params.setParameter(HttpClientParams.CREDENTIAL_CHARSET, "UTF-8");

			//params.setParameter("http.protocol.expect-continue", true);
			params.setParameter("http.authentication.preemptive", true);

			params.setParameter("http.connection.stalecheck", false);

			boolean debug = false;
			try {
				 debug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, "false"));
			} catch (Exception ex) { }

			params.setParameter(HttpClientParams.RETRY_HANDLER, new SbnRetryHandler(param.getHTTP_NUMERO_TENTATIVI(), debug));

			UsernamePasswordCredentials login =
				new UsernamePasswordCredentials(credentials.getUsername(), credentials.getPassword());

			this.conf = this.setHostConfiguration();
			AuthScope scope = new AuthScope(AuthScope.ANY_HOST,
					AuthScope.ANY_PORT, AuthScope.ANY_REALM,
					AuthScope.ANY_SCHEME);
			httpClient.getState().setCredentials(scope, login);

			//almaviva5_20111215 proxy indice
			if (param.isUSE_PROXY()) {
				String user = param.getPROXY_USER();
				String ppwd = param.getPROXY_PWD();
				if (ValidazioneDati.isFilled(user) && ValidazioneDati.isFilled(ppwd) ) {
					UsernamePasswordCredentials proxy_login = new UsernamePasswordCredentials(user, ppwd);
					httpClient.getState().setProxyCredentials(scope, proxy_login);
				}
			}

			log.info("Impostato canale HTTP per " + credentials.getUsername());
		}
	}

	private String executeHTTPRequest(HttpMethodBase method) throws Exception {

		String xmlReturned = null;
		int loop = 0;	// contatore cicli read

		try {
			try {
				httpReturnCode = httpClient.executeMethod(method);
				//almaviva5_20120117 errore proxy HTTP/1.0
				int statusCode = method.getStatusCode();
				if (param.isCHUNKED()
					&& ValidazioneDati.in(statusCode,
							HttpStatus.SC_LENGTH_REQUIRED,
							HttpStatus.SC_NOT_IMPLEMENTED) ) {
					log.warn("proxy returns HTTP code '" + statusCode + "': try disabling chunked request...");
					param.setCHUNKED(false);
					return FORCE_RETRY;
				}

			} catch (HttpException e) {
				httpReturnCode = 0;
				throw e;
			} catch (IOException e) {
				httpReturnCode = 0;
				throw e;
			}

			try {
				// Se la risposta HTTP è positiva allora si prende l'XML ritornato dal
				// server(caratteri codificati UTF-8)
				switch (method.getStatusCode()) {
				case HttpStatus.SC_OK: {
					log.debug("httpId: " + httpId + " - HTTP 200 OK ricevuto - Ricezione response body");
					// Decoding risposta: UTF-8
					xmlReturned = null;

					BufferedReader breader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
					xml_text_buffer.setLength(0);
					int read = 0;
					loop = 0;
					while ( (read = breader.read(buf)) > 0) {
						xml_text_buffer.append(buf, 0, read);
						loop++;
					}

					xmlReturned = xml_text_buffer.toString();
					break;
				}

				case HttpStatus.SC_MOVED_TEMPORARILY:
				case HttpStatus.SC_MOVED_PERMANENTLY: {
					// follow redirect
					final String newLocation = method.getResponseHeader("Location").getValue();
					log.warn("httpId: " + httpId + " - HTTP 3xx redirect ricevuto. Nuova location: " + newLocation);
					URI uri = new URI(newLocation, false);
					method.releaseConnection();
					method.setURI(uri);
					xmlReturned = this.executeHTTPRequest(method);
					break;
				}

				default:
					log.error("httpId: " + httpId + " - HTTP "
							+ method.getStatusCode() + " Unexpected failure: "
							+ method.getStatusLine().toString());
				}

			} catch (Exception e) {
				if (xml_text_buffer.length() > 0)
					log.error("httpId: " + httpId + " - Lettura Buffer incompleta: " + xml_text_buffer.toString());
				throw e;
			}

		} finally {
			log.debug("httpId: " + httpId + " - response body letti: "
					+ xml_text_buffer.length() + " bytes (in " + loop
					+ " cicli)");
			method.releaseConnection();
		}

		return xmlReturned;
	}

	public String buildHTTPQuery(Map<String, String> params, String encoding) throws UnsupportedEncodingException {
		if (!ValidazioneDati.isFilled(params))
			return "";

		StringBuilder buf = new StringBuilder(1024);
		Iterator<String> i = params.keySet().iterator();
		for (;;) {
			String key = i.next();
			buf.append(key).append('=').append(URLEncoder.encode(params.get(key), encoding));
			if (i.hasNext())
				buf.append('&');
			else
				break;
		}

		return buf.toString();
	}

	public String esecuzioneRichiesta(String xmlRequest)
			throws SbnMarcException {
		String xmlReturned = null;
		try {
			this.configureHttpClient();

		} catch (URIException e) {
			log.error("httpId: " + httpId + " - ", e);
			throw new SbnMarcException("httpId: " + httpId + " - URL non corretto", e);
		}

		// Esecuzione della richiesta per un certo numero di tentativi
		int retry = 1;//param.getHTTP_NUMERO_TENTATIVI();
		boolean error = false;

		SbnMarcException exc = new SbnMarcException();
		PostMethod post = null;

		while (retry > 0) {
			try {
				post = makePOST_Chunked(xmlRequest);
				xmlReturned = this.executeHTTPRequest(post);
				if (ValidazioneDati.equals(xmlReturned, FORCE_RETRY)) {
					++retry;
					continue;
				}

				retry = 0;
				error = false;
/*
			} catch (HttpException e) {
				retry--;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));

			} catch (ConnectTimeoutException e) {
				retry--;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));

			} catch (SocketException e) {
				retry = post.isRequestSent() ? 0 : (retry - 1);
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));
*/
			} catch (IOException e) {
				retry--; // = post.isRequestSent() ? 0 : (retry - 1);
				this.httpReturnCode = -1;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));

			} catch (Exception e) {
				retry = 0;
				this.httpReturnCode = -1;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));
			}

			if (retry > 0)
				log.debug("httpId: " + httpId + " - retry connection to " + tipo);
		}

		if (error && exc != null)
			throw exc;

		return xmlReturned;
	}

	public String esecuzioneRichiestaGET(String xmlRequest)
			throws SbnMarcException {
		String xmlReturned = null;
		try {
			this.configureHttpClient();

		} catch (URIException e) {
			log.error("httpId: " + httpId + " - ", e);
			throw new SbnMarcException("URL non corretto", e);
		}

		log.debug("http query: " + xmlRequest);

		// Esecuzione della richiesta per un certo numero di tentativi
		int retry = param.getHTTP_NUMERO_TENTATIVI();
		boolean error = false;

		SbnMarcException exc = new SbnMarcException();
		while (retry > 0) {
			try {
				xmlReturned = this.executeHTTPRequest(makeGET_Normal(xmlRequest));
				retry = 0;
				error = false;

			} catch (HttpException e) {
				retry--;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));

			} catch (IOException e) {
				// Caso del timeout
				retry = 0;
				this.httpReturnCode = -1;
				error = false;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));

			} catch (Exception e) {
				retry = 0;
				this.httpReturnCode = -1;
				error = true;
				exc = new SbnMarcException("httpId: " + httpId + " - impossibile contattare il server di " + tipo, e);
				log.error(String.format("httpId: %s - ERRORE CONNESSIONE (%s): %s", httpId, tipo, e.getLocalizedMessage()));
			}
		}

		if (error)
			throw exc;

		return xmlReturned;
	}

	public void esecuzioneRichiestaAsync(String xmlRequest) {
		try {
			ServerHttp asyncHttp = new ServerHttp(param, credentials, xmlRequest);
			Thread thread = new Thread(asyncHttp);

			thread.start();
		} catch (Exception e) {
			log.error("httpId: " + httpId + " - ", e);
		}
	}

	public int getHttpReturnCode() {
		return this.httpReturnCode;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void forceClose() {
		try {
			if (httpClient != null)
				httpClient.getHttpConnectionManager().getConnection(conf).close();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			this.httpClient = null;
			this.conf = null;
		}
	}

	public void run() {
		try {
			esecuzioneRichiesta(asyncXmlRequest);
		} catch (SbnMarcException e) {
			log.error("httpId: " + httpId + " - ", e);
		}
	}

	public void setTransactionId(String httpId) {
		this.httpId = httpId;
	}

	public void onConfigPropertyChange(String key) {
		if (tipo != null) {
			Set<String> prop = new THashSet<String>();
			prop.add(key);
			onConfigReload(prop);
		}

	}

	public void onConfigReload(Set<String> changedProperties) {
		//procedo con il reload solo se ho cambiato le URL
		for (String prop : OBSERVED_PROPS)
			if (changedProperties.contains(prop)) {
				log.debug("reload configurazione HTTP");
				try {
					//almaviva5_20131213
					String type = ValidazioneDati.trimOrEmpty(tipo).toUpperCase();
					if (type.equals("INDICE"))
						param.setURL_SERVLET(CommonConfiguration.getProperty(FactorySbn.URL_INDICE));
					if (type.equals("POLO"))
						param.setURL_SERVLET(CommonConfiguration.getProperty(FactorySbn.URL_POLO));
					// almaviva5_20201021 timeout connessione
					param.setHTTP_CONNECTION_TIMEOUT(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_CONNECTION_TIMEOUT, 30 * 1000));
					param.setHTTP_REQUEST_TIMEOUT(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_REQUEST_TIMEOUT, 15 * 60 * 1000));
					param.setHTTP_NUMERO_TENTATIVI(CommonConfiguration.getPropertyAsInteger(Configuration.HTTP_NUMERO_TENTATIVI, 2));

					//almaviva5_20150707
					Credentials credentials = new Credentials();
					credentials.setUsername(CommonConfiguration.getProperty(FactorySbn.INDICE_USER));
					credentials.setPassword(CommonConfiguration.getProperty(FactorySbn.INDICE_PWD));
					this.credentials = credentials;

					forceClose();

				} catch (Exception e) {
					log.error("", e);
				}

				httpClient = null;
			}
	}

}
