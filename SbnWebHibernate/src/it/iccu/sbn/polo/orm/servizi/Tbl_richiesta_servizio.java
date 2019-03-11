/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: almaviva
 * License Type: Purchased
 */
package it.iccu.sbn.polo.orm.servizi;

import it.iccu.sbn.polo.orm.Tb_base;

import java.util.HashSet;
import java.util.Set;
/**
 * ORM-Persistable Class
 * @param <Tbl_solleciti>
 */
public class Tbl_richiesta_servizio extends Tb_base {

	private static final long serialVersionUID = -1108654240017564385L;

	public Tbl_richiesta_servizio() {
		super();
	}

	private long cod_rich_serv;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario cd_polo_inv;

	private it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori id_documenti_lettore;

	private it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore id_esemplare_documenti_lettore;

	private it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca id_utenti_biblioteca;

	private it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento id_modalita_pagamento;

	private it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca id_supporti_biblioteca;

	private it.iccu.sbn.polo.orm.servizi.Tbl_servizio id_servizio;

	private it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio id_iter_servizio;

	private char fl_tipo_rec;

	private String note_bibliotecario;

	private java.math.BigDecimal costo_servizio;

	private String num_fasc;

	private String num_volume;

	private java.math.BigDecimal anno_period;

	private String cod_tipo_serv_rich;

	private String cod_tipo_serv_alt;

	private char cod_stato_rich;

	private char cod_stato_mov;

	private java.sql.Timestamp data_in_eff;

	private java.sql.Timestamp data_fine_eff;

	private Short num_rinnovi;

	private String bid;

	private String cod_attivita;

	private java.sql.Timestamp data_richiesta;

	private Short num_pezzi;

	private String note_ut;

	private java.math.BigDecimal prezzo_max;

	private java.util.Date data_massima;

	private java.util.Date data_proroga;

	private java.sql.Timestamp data_in_prev;

	private java.sql.Timestamp data_fine_prev;

	private char fl_svolg;

	private Character copyright;

	private String cod_erog;

	private Character cod_risp;

	private Character fl_condiz;

	private Character fl_inoltro;

	private Set<Tbl_prenotazione_posto> prenotazioni_posti = new HashSet<Tbl_prenotazione_posto>();

	private String cod_bib_dest;

	private String cod_bib_prelievo;

	private String cod_bib_restituzione;

	private Set<Tbl_solleciti> Tbl_solleciti = new HashSet<Tbl_solleciti>();

	private Tbl_dati_richiesta_ill dati_richiesta_ill;

	private String intervallo_copia;

	public void setCod_rich_serv(long value) {
		this.cod_rich_serv = value;
	}

	public long getCod_rich_serv() {
		return cod_rich_serv;
	}

	public long getORMID() {
		return getCod_rich_serv();
	}

	public void setFl_tipo_rec(char value) {
		this.fl_tipo_rec = value;
	}

	public char getFl_tipo_rec() {
		return fl_tipo_rec;
	}

	public void setNote_bibliotecario(String value) {
		this.note_bibliotecario = value;
	}

	public String getNote_bibliotecario() {
		return note_bibliotecario;
	}

	public void setCosto_servizio(java.math.BigDecimal value) {
		this.costo_servizio = value;
	}

	public java.math.BigDecimal getCosto_servizio() {
		return costo_servizio;
	}

	public void setNum_fasc(String value) {
		this.num_fasc = value;
	}

	public String getNum_fasc() {
		return num_fasc;
	}

	public void setNum_volume(String value) {
		this.num_volume = value;
	}

	public String getNum_volume() {
		return num_volume;
	}

	public void setAnno_period(java.math.BigDecimal value) {
		this.anno_period = value;
	}

	public java.math.BigDecimal getAnno_period() {
		return anno_period;
	}

	public void setCod_tipo_serv_rich(String value) {
		this.cod_tipo_serv_rich = value;
	}

	public String getCod_tipo_serv_rich() {
		return cod_tipo_serv_rich;
	}

	public void setCod_tipo_serv_alt(String value) {
		this.cod_tipo_serv_alt = value;
	}

	public String getCod_tipo_serv_alt() {
		return cod_tipo_serv_alt;
	}

	public void setCod_stato_rich(char value) {
		this.cod_stato_rich = value;
	}

	public char getCod_stato_rich() {
		return cod_stato_rich;
	}

	public void setCod_stato_mov(char value) {
		this.cod_stato_mov = value;
	}

	public char getCod_stato_mov() {
		return cod_stato_mov;
	}

	public void setData_in_eff(java.sql.Timestamp value) {
		this.data_in_eff = value;
	}

	public java.sql.Timestamp getData_in_eff() {
		return data_in_eff;
	}

	public void setData_fine_eff(java.sql.Timestamp value) {
		this.data_fine_eff = value;
	}

	public java.sql.Timestamp getData_fine_eff() {
		return data_fine_eff;
	}

	public void setNum_rinnovi(short value) {
		setNum_rinnovi(new Short(value));
	}

	public void setNum_rinnovi(Short value) {
		this.num_rinnovi = value;
	}

	public Short getNum_rinnovi() {
		return num_rinnovi;
	}

	public void setBid(String value) {
		this.bid = value;
	}

	public String getBid() {
		return bid;
	}

	public void setCod_attivita(String value) {
		this.cod_attivita = value;
	}

	public String getCod_attivita() {
		return cod_attivita;
	}

	public void setData_richiesta(java.sql.Timestamp value) {
		this.data_richiesta = value;
	}

	public java.sql.Timestamp getData_richiesta() {
		return data_richiesta;
	}

	public void setNum_pezzi(short value) {
		setNum_pezzi(new Short(value));
	}

	public void setNum_pezzi(Short value) {
		this.num_pezzi = value;
	}

	public Short getNum_pezzi() {
		return num_pezzi;
	}

	public void setNote_ut(String value) {
		this.note_ut = value;
	}

	public String getNote_ut() {
		return note_ut;
	}

	public void setPrezzo_max(java.math.BigDecimal value) {
		this.prezzo_max = value;
	}

	public java.math.BigDecimal getPrezzo_max() {
		return prezzo_max;
	}

	public void setData_massima(java.util.Date value) {
		this.data_massima = value;
	}

	public java.util.Date getData_massima() {
		return data_massima;
	}

	public void setData_proroga(java.util.Date value) {
		this.data_proroga = value;
	}

	public java.util.Date getData_proroga() {
		return data_proroga;
	}

	public void setData_in_prev(java.sql.Timestamp value) {
		this.data_in_prev = value;
	}

	public java.sql.Timestamp getData_in_prev() {
		return data_in_prev;
	}

	public void setData_fine_prev(java.sql.Timestamp value) {
		this.data_fine_prev = value;
	}

	public java.sql.Timestamp getData_fine_prev() {
		return data_fine_prev;
	}

	public void setFl_svolg(char value) {
		this.fl_svolg = value;
	}

	public char getFl_svolg() {
		return fl_svolg;
	}

	public void setCopyright(char value) {
		setCopyright(new Character(value));
	}

	public void setCopyright(Character value) {
		this.copyright = value;
	}

	public Character getCopyright() {
		return copyright;
	}

	public void setCod_erog(String value) {
		this.cod_erog = value;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setCod_risp(char value) {
		setCod_risp(new Character(value));
	}

	public void setCod_risp(Character value) {
		this.cod_risp = value;
	}

	public Character getCod_risp() {
		return cod_risp;
	}

	public void setFl_condiz(char value) {
		setFl_condiz(new Character(value));
	}

	public void setFl_condiz(Character value) {
		this.fl_condiz = value;
	}

	public Character getFl_condiz() {
		return fl_condiz;
	}

	public void setFl_inoltro(char value) {
		setFl_inoltro(new Character(value));
	}

	public void setFl_inoltro(Character value) {
		this.fl_inoltro = value;
	}

	public Character getFl_inoltro() {
		return fl_inoltro;
	}

	public Set<Tbl_prenotazione_posto> getPrenotazioni_posti() {
		return prenotazioni_posti;
	}

	public void setPrenotazioni_posti(Set<Tbl_prenotazione_posto> prenotazione_posto) {
		this.prenotazioni_posti = prenotazione_posto;
	}

	public void setCod_bib_dest(String value) {
		this.cod_bib_dest = value;
	}

	public String getCod_bib_dest() {
		return cod_bib_dest;
	}

	/**
	 * Biblioteca del sistema metropolitano indicata come consegnataria del documento
	 */
	public void setCod_bib_prelievo(String value) {
		this.cod_bib_prelievo = value;
	}

	/**
	 * Biblioteca del sistema metropolitano indicata come consegnataria del documento
	 */
	public String getCod_bib_prelievo() {
		return cod_bib_prelievo;
	}

	/**
	 * Biblioteca del sistema metropolitano indicata come destinataria della riconsegna
	 */
	public void setCod_bib_restituzione(String value) {
		this.cod_bib_restituzione = value;
	}

	/**
	 * Biblioteca del sistema metropolitano indicata come destinataria della riconsegna
	 */
	public String getCod_bib_restituzione() {
		return cod_bib_restituzione;
	}

	public void setCd_polo_inv(it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario value) {
		this.cd_polo_inv = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario getCd_polo_inv() {
		return cd_polo_inv;
	}

	public void setId_documenti_lettore(it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori value) {
		this.id_documenti_lettore = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori getId_documenti_lettore() {
		return id_documenti_lettore;
	}

	public void setId_modalita_pagamento(it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento value) {
		this.id_modalita_pagamento = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento getId_modalita_pagamento() {
		return id_modalita_pagamento;
	}

	public void setId_supporti_biblioteca(it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca value) {
		this.id_supporti_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca getId_supporti_biblioteca() {
		return id_supporti_biblioteca;
	}

	public void setId_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_servizio value) {
		this.id_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_servizio getId_servizio() {
		return id_servizio;
	}

	public void setId_esemplare_documenti_lettore(it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore value) {
		this.id_esemplare_documenti_lettore = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_esemplare_documento_lettore getId_esemplare_documenti_lettore() {
		return id_esemplare_documenti_lettore;
	}

	public void setId_utenti_biblioteca(it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca value) {
		this.id_utenti_biblioteca = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca getId_utenti_biblioteca() {
		return id_utenti_biblioteca;
	}

	public void setId_iter_servizio(it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio value) {
		this.id_iter_servizio = value;
	}

	public it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio getId_iter_servizio() {
		return id_iter_servizio;
	}

	public void setTbl_solleciti(Set<Tbl_solleciti> value) {
		this.Tbl_solleciti = value;
	}

	public Set<Tbl_solleciti> getTbl_solleciti() {
		return Tbl_solleciti;
	}

	public Tbl_dati_richiesta_ill getDati_richiesta_ill() {
		return dati_richiesta_ill;
	}

	public void setDati_richiesta_ill(Tbl_dati_richiesta_ill dati_richiesta_ill) {
		this.dati_richiesta_ill = dati_richiesta_ill;
	}

	public String getIntervallo_copia() {
		return intervallo_copia;
	}

	public void setIntervallo_copia(String intervallo_copia) {
		this.intervallo_copia = intervallo_copia;
	}

	public String toString() {
		return String.valueOf(getCod_rich_serv());
	}

}
