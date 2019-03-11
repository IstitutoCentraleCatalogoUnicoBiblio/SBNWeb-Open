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
package it.iccu.sbn.polo.orm.servizi;



import it.iccu.sbn.polo.orm.Tb_base;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Tbl_dati_richiesta_ill extends Tb_base {

	private static final long serialVersionUID = -1910740593062533209L;

	private int id_dati_richiesta;

	private Tbl_richiesta_servizio richiesta;

	private Tbl_documenti_lettori documento;

	private Tbc_inventario inventario;

	private long transactionId;
	private String responderId;
	private String requesterId;
	private String clientId;

	private Character fl_ruolo;
	private Timestamp data_inizio;
	private Timestamp data_fine;

	private String cd_stato;
	private String cd_servizio;

	private String clientName;
	private String client_email;

	private String via;
	private String comune;
	private String prov;
	private String cap;
	private String cd_paese;
	private String requester_email;

	private Date dt_data_desiderata;
	private Date dt_data_massima;
	private Date dt_data_scadenza;

	private String cod_erog;
	private String cd_supporto;
	private String cd_valuta;
	private Number importo;
	private Number costo_max;

	private Long cod_rich_serv_old;

	private Timestamp ts_last_cambio_stato;
	private String biblioteche;

	private String intervallo_copia;

	private Set<Tbl_messaggio> messaggio = new HashSet<Tbl_messaggio>();

	public int getId_dati_richiesta() {
		return id_dati_richiesta;
	}

	public void setId_dati_richiesta(int id_dati_richiesta) {
		this.id_dati_richiesta = id_dati_richiesta;
	}

	public Tbl_richiesta_servizio getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(Tbl_richiesta_servizio rs) {
		this.richiesta = rs;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getResponderId() {
		return responderId;
	}

	public void setResponderId(String responderId) {
		this.responderId = responderId;
	}

	public Tbl_documenti_lettori getDocumento() {
		return documento;
	}

	public void setDocumento(Tbl_documenti_lettori documento) {
		this.documento = documento;
	}

	public Tbc_inventario getInventario() {
		return inventario;
	}

	public void setInventario(Tbc_inventario inventario) {
		this.inventario = inventario;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Character getFl_ruolo() {
		return fl_ruolo;
	}

	public void setFl_ruolo(Character fl_richiedente) {
		this.fl_ruolo = fl_richiedente;
	}

	public Timestamp getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(Timestamp data_inizio) {
		this.data_inizio = data_inizio;
	}

	public Timestamp getData_fine() {
		return data_fine;
	}

	public void setData_fine(Timestamp data_fine) {
		this.data_fine = data_fine;
	}

	public String getCd_stato() {
		return cd_stato;
	}

	public void setCd_stato(String cd_stato) {
		this.cd_stato = cd_stato;
	}

	public String getCd_servizio() {
		return cd_servizio;
	}

	public void setCd_servizio(String cd_servizio) {
		this.cd_servizio = cd_servizio;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClient_email() {
		return client_email;
	}

	public void setClient_email(String client_email) {
		this.client_email = client_email;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setCd_paese(String cd_paese) {
		this.cd_paese = cd_paese;
	}

	public String getRequester_email() {
		return requester_email;
	}

	public void setRequester_email(String requester_mail) {
		this.requester_email = requester_mail;
	}

	public Date getDt_data_desiderata() {
		return dt_data_desiderata;
	}

	public void setDt_data_desiderata(Date dt_data_desiderata) {
		this.dt_data_desiderata = dt_data_desiderata;
	}

	public Date getDt_data_massima() {
		return dt_data_massima;
	}

	public void setDt_data_massima(Date dt_data_massima) {
		this.dt_data_massima = dt_data_massima;
	}

	public Date getDt_data_scadenza() {
		return dt_data_scadenza;
	}

	public void setDt_data_scadenza(Date dt_data_scadenza) {
		this.dt_data_scadenza = dt_data_scadenza;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setCod_erog(String cod_erog) {
		this.cod_erog = cod_erog;
	}

	public String getCd_supporto() {
		return cd_supporto;
	}

	public void setCd_supporto(String cd_supporto) {
		this.cd_supporto = cd_supporto;
	}

	public String getCd_valuta() {
		return cd_valuta;
	}

	public void setCd_valuta(String cd_valuta) {
		this.cd_valuta = cd_valuta;
	}

	public Number getImporto() {
		return importo;
	}

	public void setImporto(Number importo) {
		this.importo = importo;
	}

	public Number getCosto_max() {
		return costo_max;
	}

	public void setCosto_max(Number costo_max) {
		this.costo_max = costo_max;
	}

	public Long getCod_rich_serv_old() {
		return cod_rich_serv_old;
	}

	public void setCod_rich_serv_old(Long cod_rich_serv_old) {
		this.cod_rich_serv_old = cod_rich_serv_old;
	}

	public Timestamp getTs_last_cambio_stato() {
		return ts_last_cambio_stato;
	}

	public void setTs_last_cambio_stato(Timestamp ts) {
		this.ts_last_cambio_stato = ts;
	}

	public String getBiblioteche() {
		return biblioteche;
	}

	public void setBiblioteche(String biblioteche) {
		this.biblioteche = biblioteche;
	}

	public Set<Tbl_messaggio> getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(Set<Tbl_messaggio> messaggi) {
		this.messaggio = messaggi;
	}

	public String getIntervallo_copia() {
		return intervallo_copia;
	}

	public void setIntervallo_copia(String intervallo_copia) {
		this.intervallo_copia = intervallo_copia;
	}

}
