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

import java.sql.Timestamp;


/**
 * Storico richieste di servizio
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_storico_richieste_servizio extends Tb_base {

	private static final long serialVersionUID = -4832786888756150112L;

	public Tbl_storico_richieste_servizio() {
		super();
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbl_storico_richieste_servizio))
			return false;
		Tbl_storico_richieste_servizio tbl_storico_richieste_servizio = (Tbl_storico_richieste_servizio)aObj;
		if ((getCd_polo() != null && !getCd_polo().equals(tbl_storico_richieste_servizio.getCd_polo())) || (getCd_polo() == null && tbl_storico_richieste_servizio.getCd_polo() != null))
			return false;
		if ((getCd_bib() != null && !getCd_bib().equals(tbl_storico_richieste_servizio.getCd_bib())) || (getCd_bib() == null && tbl_storico_richieste_servizio.getCd_bib() != null))
			return false;
		if (getCod_rich_serv() != tbl_storico_richieste_servizio.getCod_rich_serv())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + (getCd_polo() == null ? 0 : getCd_polo().hashCode());
		hashcode = hashcode + (getCd_bib() == null ? 0 : getCd_bib().hashCode());
		hashcode = hashcode + (getCod_rich_serv() == null ? 0 : getCod_rich_serv().hashCode());
		return hashcode;
	}

	private String cd_polo;

	private String cd_bib;

	private java.math.BigDecimal cod_rich_serv;

	private String cod_bib_ut;

	private java.math.BigDecimal cod_utente;

	private String cod_tipo_serv;

	private java.util.Date data_richiesta;

	private short num_volume;

	private String num_fasc;

	private short num_pezzi;

	private String note_ut;

	private java.math.BigDecimal prezzo_max;

	private java.math.BigDecimal costo_servizio;

	private java.util.Date data_massima;

	private String note_bibl;

	private java.util.Date data_proroga;

	private java.util.Date data_in_prev;

	private java.util.Date data_fine_prev;

	private char flag_svolg;

	private Character copyright;

	private String descr_erog;

	private String descr_stato_ric;

	private String descr_supporto;

	private String descr_risp;

	private String descr_mod_pag;

	private char flag_pren;

	private Character tipo_doc_lett;

	private java.math.BigDecimal cod_doc_lett;

	private short prg_esemplare;

	private String cod_serie;

	private int cod_inven;

	private Character flag_condiz;

	private String cod_tipo_serv_alt;

	private String descr_erog_alt;

	private String cod_bib_dest;

	private String titolo;

	private String autore;

	private String editore;

	private String anno_edizione;

	private String luogo_edizione;

	private String annata;

	private short num_vol_mon;

	private java.util.Date data_in_eff;

	private java.util.Date data_fine_eff;

	private short num_rinnovi;

	private String note_bibliotecario;

	private String descr_stato_mov;

	private Character flag_tipo_serv;

	private short num_solleciti;

	private java.util.Date data_ult_soll;

	//dati prenotazione posto
	private Integer id_prenot_posto;
	private String descr_sala;
	private String descr_stato_prenot_posto;
	private Timestamp ts_prenot_posto_inizio;
	private Timestamp ts_prenot_posto_fine;

	private String denominazione;

	public void setCd_polo(String value) {
		this.cd_polo = value;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public void setCd_bib(String value) {
		this.cd_bib = value;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCod_rich_serv(java.math.BigDecimal value) {
		this.cod_rich_serv = value;
	}

	public java.math.BigDecimal getCod_rich_serv() {
		return cod_rich_serv;
	}

	public void setCod_bib_ut(String value) {
		this.cod_bib_ut = value;
	}

	public String getCod_bib_ut() {
		return cod_bib_ut;
	}

	public void setCod_utente(java.math.BigDecimal value) {
		this.cod_utente = value;
	}

	public java.math.BigDecimal getCod_utente() {
		return cod_utente;
	}

	public void setCod_tipo_serv(String value) {
		this.cod_tipo_serv = value;
	}

	public String getCod_tipo_serv() {
		return cod_tipo_serv;
	}

	public void setData_richiesta(java.util.Date value) {
		this.data_richiesta = value;
	}

	public java.util.Date getData_richiesta() {
		return data_richiesta;
	}

/*	public void setNum_volume(short value) {
		this.num_volume = value;
	}
*/
	public void setNum_volume(short value) {
		setNum_volume(new Short(value));
	}

	public void setNum_volume(Short value) {
		this.num_volume = value;
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

	public short getNum_volume() {
		return num_volume;
	}

	public void setNum_fasc(String value) {
		this.num_fasc = value;
	}

	public String getNum_fasc() {
		return num_fasc;
	}

/*	public void setNum_pezzi(short value) {
		this.num_pezzi = value;
	}*/

	public void setNum_pezzi(short value) {
		setNum_pezzi(new Short(value));
	}

	public void setNum_pezzi(Short value) {
		this.num_pezzi = value;
	}


	public short getNum_pezzi() {
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

	public void setCosto_servizio(java.math.BigDecimal value) {
		this.costo_servizio = value;
	}

	public java.math.BigDecimal getCosto_servizio() {
		return costo_servizio;
	}

	public void setData_massima(java.util.Date value) {
		this.data_massima = value;
	}

	public java.util.Date getData_massima() {
		return data_massima;
	}

	public void setNote_bibl(String value) {
		this.note_bibl = value;
	}

	public String getNote_bibl() {
		return note_bibl;
	}

	public void setData_proroga(java.util.Date value) {
		this.data_proroga = value;
	}

	public java.util.Date getData_proroga() {
		return data_proroga;
	}

	public void setData_in_prev(java.util.Date value) {
		this.data_in_prev = value;
	}

	public java.util.Date getData_in_prev() {
		return data_in_prev;
	}

	public void setData_fine_prev(java.util.Date value) {
		this.data_fine_prev = value;
	}

	public java.util.Date getData_fine_prev() {
		return data_fine_prev;
	}

	public void setFlag_svolg(char value) {
		this.flag_svolg = value;
	}

	public char getFlag_svolg() {
		return flag_svolg;
	}



	public void setDescr_erog(String value) {
		this.descr_erog = value;
	}

	public String getDescr_erog() {
		return descr_erog;
	}

	public void setDescr_stato_ric(String value) {
		this.descr_stato_ric = value;
	}

	public String getDescr_stato_ric() {
		return descr_stato_ric;
	}

	public void setDescr_supporto(String value) {
		this.descr_supporto = value;
	}

	public String getDescr_supporto() {
		return descr_supporto;
	}

	public void setDescr_risp(String value) {
		this.descr_risp = value;
	}

	public String getDescr_risp() {
		return descr_risp;
	}

	public void setDescr_mod_pag(String value) {
		this.descr_mod_pag = value;
	}

	public String getDescr_mod_pag() {
		return descr_mod_pag;
	}

	public void setFlag_pren(char value) {
		this.flag_pren = value;
	}

	public char getFlag_pren() {
		return flag_pren;
	}

/*	public void setTipo_doc_lett(char value) {
		this.tipo_doc_lett = value;
	}*/

	public void setTipo_doc_lett(char value) {
		setTipo_doc_lett(new Character(value));
	}

	public void setTipo_doc_lett(Character value) {
		this.tipo_doc_lett = value;
	}





	public void setCod_doc_lett(java.math.BigDecimal value) {
		this.cod_doc_lett = value;
	}

	public java.math.BigDecimal getCod_doc_lett() {
		return cod_doc_lett;
	}

	public void setPrg_esemplare(short value) {
		this.prg_esemplare = value;
	}

	public short getPrg_esemplare() {
		return prg_esemplare;
	}

	public void setCod_serie(String value) {
		this.cod_serie = value;
	}

	public String getCod_serie() {
		return cod_serie;
	}

	public void setCod_inven(int value) {
		this.cod_inven = value;
	}

	public int getCod_inven() {
		return cod_inven;
	}

/*	public void setFlag_condiz(char value) {
		this.flag_condiz = value;
	}
	*/
	public void setFlag_condiz(char value) {
		setFlag_condiz(new Character(value));
	}

	public void setFlag_condiz(Character value) {
		this.flag_condiz = value;
	}




	public void setCod_tipo_serv_alt(String value) {
		this.cod_tipo_serv_alt = value;
	}

	public String getCod_tipo_serv_alt() {
		return cod_tipo_serv_alt;
	}

	public void setDescr_erog_alt(String value) {
		this.descr_erog_alt = value;
	}

	public String getDescr_erog_alt() {
		return descr_erog_alt;
	}

	public void setCod_bib_dest(String value) {
		this.cod_bib_dest = value;
	}

	public String getCod_bib_dest() {
		return cod_bib_dest;
	}

	public void setTitolo(String value) {
		this.titolo = value;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAutore(String value) {
		this.autore = value;
	}

	public String getAutore() {
		return autore;
	}

	public void setEditore(String value) {
		this.editore = value;
	}

	public String getEditore() {
		return editore;
	}

	public void setAnno_edizione(String value) {
		this.anno_edizione = value;
	}

	public String getAnno_edizione() {
		return anno_edizione;
	}

	public void setLuogo_edizione(String value) {
		this.luogo_edizione = value;
	}

	public String getLuogo_edizione() {
		return luogo_edizione;
	}

	public void setAnnata(String value) {
		this.annata = value;
	}

	public String getAnnata() {
		return annata;
	}

	public void setNum_vol_mon(short value) {
		this.num_vol_mon = value;
	}

	public short getNum_vol_mon() {
		return num_vol_mon;
	}

	public void setData_in_eff(java.util.Date value) {
		this.data_in_eff = value;
	}

	public java.util.Date getData_in_eff() {
		return data_in_eff;
	}

	public void setData_fine_eff(java.util.Date value) {
		this.data_fine_eff = value;
	}

	public java.util.Date getData_fine_eff() {
		return data_fine_eff;
	}

/*	public void setNum_rinnovi(short value) {
		this.num_rinnovi = value;
	}*/

	public void setNum_rinnovi(short value) {
		setNum_rinnovi(new Short(value));
	}

	public void setNum_rinnovi(Short value) {
		this.num_rinnovi = value;
	}

	public short getNum_rinnovi() {
		return num_rinnovi;
	}

	public void setNote_bibliotecario(String value) {
		this.note_bibliotecario = value;
	}

	public String getNote_bibliotecario() {
		return note_bibliotecario;
	}

	public void setDescr_stato_mov(String value) {
		this.descr_stato_mov = value;
	}

	public String getDescr_stato_mov() {
		return descr_stato_mov;
	}

/*	public void setFlag_tipo_serv(char value) {
		this.flag_tipo_serv = value;
	}*/

	public void setFlag_tipo_serv(char value) {
		setFlag_tipo_serv(new Character(value));
	}

	public void setFlag_tipo_serv(Character value) {
		this.flag_tipo_serv = value;
	}




	public void setNum_solleciti(short value) {
		this.num_solleciti = value;
	}

	public short getNum_solleciti() {
		return num_solleciti;
	}

	public void setData_ult_soll(java.util.Date value) {
		this.data_ult_soll = value;
	}

	public java.util.Date getData_ult_soll() {
		return data_ult_soll;
	}

	public Integer getId_prenot_posto() {
		return id_prenot_posto;
	}

	public void setId_prenot_posto(Integer id_prenot_posto) {
		this.id_prenot_posto = id_prenot_posto;
	}

	public String getDescr_sala() {
		return descr_sala;
	}

	public void setDescr_sala(String descr_sala) {
		this.descr_sala = descr_sala;
	}

	public String getDescr_stato_prenot_posto() {
		return descr_stato_prenot_posto;
	}

	public void setDescr_stato_prenot_posto(String descr_stato_prenot_posto) {
		this.descr_stato_prenot_posto = descr_stato_prenot_posto;
	}

	public Timestamp getTs_prenot_posto_inizio() {
		return ts_prenot_posto_inizio;
	}

	public void setTs_prenot_posto_inizio(Timestamp ts_prenot_posto_inizio) {
		this.ts_prenot_posto_inizio = ts_prenot_posto_inizio;
	}

	public Timestamp getTs_prenot_posto_fine() {
		return ts_prenot_posto_fine;
	}

	public void setTs_prenot_posto_fine(Timestamp ts_prenot_posto_fine) {
		this.ts_prenot_posto_fine = ts_prenot_posto_fine;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public String toString() {
		return String.valueOf(getCd_polo() + " " + getCd_bib() + " " + getCod_rich_serv());
	}

	public Character getFlag_condiz() {
		return flag_condiz;
	}

	public Character getFlag_tipo_serv() {
		return flag_tipo_serv;
	}

	public Character getTipo_doc_lett() {
		return tipo_doc_lett;
	}

}
