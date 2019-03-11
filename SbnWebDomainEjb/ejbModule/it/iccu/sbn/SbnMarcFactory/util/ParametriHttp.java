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
package it.iccu.sbn.SbnMarcFactory.util;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ParametriHttp extends SerializableVO {

	private static final long serialVersionUID = 7417365327812276816L;

	//almaviva5_20111215 proxy indice
	private boolean USE_PROXY = false;
	private String PROXY_URL;
	private int PROXY_PORT;
	private String PROXY_USER;
	private String PROXY_PWD;

	//almaviva5_20120117 errore proxy HTTP/1.0
	private boolean CHUNKED = true;


	/**
	 * Indirizzo della servlet del server SBN, il parametro passato in modalità
	 * POST si chiama "testo_xml"
	 */

	// Indirizzi per il protocollo di Indice
	private String URL_SERVLET = "http://localhost";

	private String UPLOAD_URL = "http://localhost";

	// almaviva5_20091002 MODIFICHE PER TIMEOUT INDICE
	private int HTTP_CONNECTION_TIMEOUT = 30 * 1000; // 30 secondi

	private int HTTP_REQUEST_TIMEOUT = 15 * 60 * 1000; // 15 minuti

	private int HTTP_NUMERO_TENTATIVI = 2;


	public int getHTTP_CONNECTION_TIMEOUT() {
		return HTTP_CONNECTION_TIMEOUT;
	}

	public void setHTTP_CONNECTION_TIMEOUT(int http_connection_timeout) {
		HTTP_CONNECTION_TIMEOUT = http_connection_timeout;
	}

	public int getHTTP_NUMERO_TENTATIVI() {
		return HTTP_NUMERO_TENTATIVI;
	}

	public void setHTTP_NUMERO_TENTATIVI(int http_numero_tentativi) {
		HTTP_NUMERO_TENTATIVI = http_numero_tentativi;
		if (HTTP_NUMERO_TENTATIVI < 1)
			HTTP_NUMERO_TENTATIVI = 1;
	}

	public int getHTTP_REQUEST_TIMEOUT() {
		return HTTP_REQUEST_TIMEOUT;
	}

	public void setHTTP_REQUEST_TIMEOUT(int http_request_timeout) {
		HTTP_REQUEST_TIMEOUT = http_request_timeout;
	}

	public String getUPLOAD_URL() {
		return UPLOAD_URL;
	}

	public void setUPLOAD_URL(String upload_url) {
		UPLOAD_URL = upload_url;
	}

	public String getURL_SERVLET() {
		return URL_SERVLET;
	}

	public void setURL_SERVLET(String url_servlet) {
		URL_SERVLET = url_servlet;
	}

	public boolean isUSE_PROXY() {
		return USE_PROXY;
	}

	public void setUSE_PROXY(boolean useProxy) {
		this.USE_PROXY = useProxy;
	}

	public String getPROXY_URL() {
		return PROXY_URL;
	}

	public void setPROXY_URL(String uRL_PROXY) {
		PROXY_URL = uRL_PROXY;
	}

	public void setPROXY_PORT(int port) {
		PROXY_PORT = port;
	}

	public int getPROXY_PORT() {
		return PROXY_PORT;
	}

	public String getPROXY_USER() {
		return PROXY_USER;
	}

	public void setPROXY_USER(String pROXY_USER) {
		PROXY_USER = pROXY_USER;
	}

	public String getPROXY_PWD() {
		return PROXY_PWD;
	}

	public void setPROXY_PWD(String pROXY_PWD) {
		PROXY_PWD = pROXY_PWD;
	}

	public boolean isCHUNKED() {
		return CHUNKED;
	}

	public void setCHUNKED(boolean chunked) {
		CHUNKED = chunked;
	}

}
