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
package it.iccu.sbn.polo.orm.acquisizioni;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;
/**
 * Ordini
 */
/**
 * ORM-Persistable Class
 */
public class Tba_ordini implements Serializable {

	private static final long serialVersionUID = -6444574982037127186L;

	public Tba_ordini() {
	}

	private int id_ordine;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private char cod_tip_ord;

	private java.math.BigDecimal anno_ord;

	private int cod_ord;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore;

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche id_sez_acquis_bibliografiche;

	private it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali id_valuta;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci tbb_bilancicod_mat;

	private java.sql.Timestamp data_ins;

	private java.sql.Timestamp data_agg;

	private java.util.Date data_ord;

	private String note;

	private int num_copie;

	private char continuativo;

	private char stato_ordine;

	private Character tipo_doc_lett;

	private Long cod_doc_lett;

	private char tipo_urgenza;

	private Long cod_rich_off;

	private String bid_p;

	private String note_forn;

	private char tipo_invio;

	private java.math.BigDecimal anno_1ord;

	private Integer cod_1ord;

	private java.math.BigDecimal prezzo;

	private String paese;

	private String cod_bib_sugg;

	private java.math.BigDecimal cod_sugg_bibl;

	private String bid;

	private Character stato_abb;

	private Character cod_per_abb;

	private java.math.BigDecimal prezzo_lire;

	private String reg_trib;

	private java.math.BigDecimal anno_abb;

	private String num_fasc;

	private java.util.Date data_fasc;

	private String annata;

	private Short num_vol_abb;

	private char natura;

	private java.util.Date data_fine;

	private boolean stampato = false;

	private boolean rinnovato = false;

	private java.sql.Timestamp data_chiusura_ord;

	private String ute_ins;

	private String ute_var;

	private java.sql.Timestamp ts_ins;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Character cd_tipo_lav;

	private Tra_ordine_carrello_spedizione tra_ordine_carrello_spedizione;

	private java.util.Set Tba_righe_fatture = new java.util.HashSet();

	private java.util.Set Tra_ordine_inventari = new java.util.HashSet();

	private java.util.Set Tbp_esemplare_fascicolo  = new java.util.HashSet();

	private java.util.Set Trp_messaggio_fascicolo = new java.util.HashSet();

	private void setId_ordine(int value) {
		this.id_ordine = value;
	}

	public int getId_ordine() {
		return id_ordine;
	}

	public int getORMID() {
		return getId_ordine();
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public void setCod_tip_ord(char value) {
		this.cod_tip_ord = value;
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public char getCod_tip_ord() {
		return cod_tip_ord;
	}

	/**
	 * anno di acquisizione dell'ordine
	 */
	public void setAnno_ord(java.math.BigDecimal value) {
		this.anno_ord = value;
	}

	/**
	 * anno di acquisizione dell'ordine
	 */
	public java.math.BigDecimal getAnno_ord() {
		return anno_ord;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public void setCod_ord(int value) {
		this.cod_ord = value;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public int getCod_ord() {
		return cod_ord;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setData_ins(java.sql.Timestamp value) {
		this.data_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getData_ins() {
		return data_ins;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setData_agg(java.sql.Timestamp value) {
		this.data_agg = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getData_agg() {
		return data_agg;
	}

	/**
	 * data dell'ordine
	 */
	public void setData_ord(java.util.Date value) {
		this.data_ord = value;
	}

	/**
	 * data dell'ordine
	 */
	public java.util.Date getData_ord() {
		return data_ord;
	}

	/**
	 * osservazioni relative all'ordine
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * osservazioni relative all'ordine
	 */
	public String getNote() {
		return note;
	}

	/**
	 * numero di copie ordinate
	 */
	public void setNum_copie(int value) {
		this.num_copie = value;
	}

	/**
	 * numero di copie ordinate
	 */
	public int getNum_copie() {
		return num_copie;
	}

	/**
	 * flag indicante se l'ordine e' continuativo
	 */
	public void setContinuativo(char value) {
		this.continuativo = value;
	}

	/**
	 * flag indicante se l'ordine e' continuativo
	 */
	public char getContinuativo() {
		return continuativo;
	}

	/**
	 * codice identificativo dello stato dell'ordine
	 */
	public void setStato_ordine(char value) {
		this.stato_ordine = value;
	}

	/**
	 * codice identificativo dello stato dell'ordine
	 */
	public char getStato_ordine() {
		return stato_ordine;
	}

	/**
	 * codice identificativo della tipologia del documento lettore ("s" = suggerimento)
	 */
	public void setTipo_doc_lett(char value) {
		setTipo_doc_lett(new Character(value));
	}

	/**
	 * codice identificativo della tipologia del documento lettore ("s" = suggerimento)
	 */
	public void setTipo_doc_lett(Character value) {
		this.tipo_doc_lett = value;
	}

	/**
	 * codice identificativo della tipologia del documento lettore ("s" = suggerimento)
	 */
	public Character getTipo_doc_lett() {
		return tipo_doc_lett;
	}

	/**
	 * codice identificativo del documento suggerito dal lettore
	 */
	public void setCod_doc_lett(long value) {
		setCod_doc_lett(new Long(value));
	}

	/**
	 * codice identificativo del documento suggerito dal lettore
	 */
	public void setCod_doc_lett(Long value) {
		this.cod_doc_lett = value;
	}

	/**
	 * codice identificativo del documento suggerito dal lettore
	 */
	public Long getCod_doc_lett() {
		return cod_doc_lett;
	}

	/**
	 * codice identificativo della tipologia di urgenza
	 */
	public void setTipo_urgenza(char value) {
		this.tipo_urgenza = value;
	}

	/**
	 * codice identificativo della tipologia di urgenza
	 */
	public char getTipo_urgenza() {
		return tipo_urgenza;
	}

	/**
	 * codice identificativo della richiesta di offerta
	 */
	public void setCod_rich_off(long value) {
		setCod_rich_off(new Long(value));
	}

	/**
	 * codice identificativo della richiesta di offerta
	 */
	public void setCod_rich_off(Long value) {
		this.cod_rich_off = value;
	}

	/**
	 * codice identificativo della richiesta di offerta
	 */
	public Long getCod_rich_off() {
		return cod_rich_off;
	}

	/**
	 * codice identificativo dell'offerta del fornitore
	 */
	public void setBid_p(String value) {
		this.bid_p = value;
	}

	/**
	 * codice identificativo dell'offerta del fornitore
	 */
	public String getBid_p() {
		return bid_p;
	}

	/**
	 * note per il fornitore in riferimento all'ordine
	 */
	public void setNote_forn(String value) {
		this.note_forn = value;
	}

	/**
	 * note per il fornitore in riferimento all'ordine
	 */
	public String getNote_forn() {
		return note_forn;
	}

	/**
	 * codice identificativo del tipo invio
	 */
	public void setTipo_invio(char value) {
		this.tipo_invio = value;
	}

	/**
	 * codice identificativo del tipo invio
	 */
	public char getTipo_invio() {
		return tipo_invio;
	}

	/**
	 * anno di acquisizione del primo ordine
	 */
	public void setAnno_1ord(java.math.BigDecimal value) {
		this.anno_1ord = value;
	}

	/**
	 * anno di acquisizione del primo ordine
	 */
	public java.math.BigDecimal getAnno_1ord() {
		return anno_1ord;
	}

	/**
	 * codice identificativo del primo ordine
	 */
	public void setCod_1ord(int value) {
		setCod_1ord(new Integer(value));
	}

	/**
	 * codice identificativo del primo ordine
	 */
	public void setCod_1ord(Integer value) {
		this.cod_1ord = value;
	}

	/**
	 * codice identificativo del primo ordine
	 */
	public Integer getCod_1ord() {
		return cod_1ord;
	}

	/**
	 * prezzo in valuta
	 */
	public void setPrezzo(java.math.BigDecimal value) {
		this.prezzo = value;
	}

	/**
	 * prezzo in valuta
	 */
	public java.math.BigDecimal getPrezzo() {
		return prezzo;
	}

	/**
	 * codice identificativo del paese
	 */
	public void setPaese(String value) {
		this.paese = value;
	}

	/**
	 * codice identificativo del paese
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * codice identificativo della biblioteca dove e' stato effettuato il suggerimento dal bibliotecario
	 */
	public void setCod_bib_sugg(String value) {
		this.cod_bib_sugg = value;
	}

	/**
	 * codice identificativo della biblioteca dove e' stato effettuato il suggerimento dal bibliotecario
	 */
	public String getCod_bib_sugg() {
		return cod_bib_sugg;
	}

	/**
	 * codice identificativo del suggerimento del bibliotecario
	 */
	public void setCod_sugg_bibl(java.math.BigDecimal value) {
		this.cod_sugg_bibl = value;
	}

	/**
	 * codice identificativo del suggerimento del bibliotecario
	 */
	public java.math.BigDecimal getCod_sugg_bibl() {
		return cod_sugg_bibl;
	}

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
	public void setBid(String value) {
		this.bid = value;
	}

	/**
	 * codice identificativo dell'oggetto bibliografico
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * codice identificativo dello stato dell'abbonamento
	 */
	public void setStato_abb(char value) {
		setStato_abb(new Character(value));
	}

	/**
	 * codice identificativo dello stato dell'abbonamento
	 */
	public void setStato_abb(Character value) {
		this.stato_abb = value;
	}

	/**
	 * codice identificativo dello stato dell'abbonamento
	 */
	public Character getStato_abb() {
		return stato_abb;
	}

	/**
	 * codice del periodo di validita' dell'abbonamento
	 */
	public void setCod_per_abb(char value) {
		setCod_per_abb(new Character(value));
	}

	/**
	 * codice del periodo di validita' dell'abbonamento
	 */
	public void setCod_per_abb(Character value) {
		this.cod_per_abb = value;
	}

	/**
	 * codice del periodo di validita' dell'abbonamento
	 */
	public Character getCod_per_abb() {
		return cod_per_abb;
	}

	/**
	 * prezzo in valuta
	 */
	public void setPrezzo_lire(java.math.BigDecimal value) {
		this.prezzo_lire = value;
	}

	/**
	 * prezzo in valuta
	 */
	public java.math.BigDecimal getPrezzo_lire() {
		return prezzo_lire;
	}

	/**
	 * numero della registrazione del tribunale per deposito legale, data di registrazione e citta' dove si e' effettuata la registrazione
	 */
	public void setReg_trib(String value) {
		this.reg_trib = value;
	}

	/**
	 * numero della registrazione del tribunale per deposito legale, data di registrazione e citta' dove si e' effettuata la registrazione
	 */
	public String getReg_trib() {
		return reg_trib;
	}

	/**
	 * anno dell'abbonamento
	 */
	public void setAnno_abb(java.math.BigDecimal value) {
		this.anno_abb = value;
	}

	/**
	 * anno dell'abbonamento
	 */
	public java.math.BigDecimal getAnno_abb() {
		return anno_abb;
	}

	/**
	 * numero del fascicolo (primo fascicolo se l'abbonamento e' da aprire, ultimo fascicolo se l'abbonamento e' da chiudere)
	 */
	public void setNum_fasc(String value) {
		this.num_fasc = value;
	}

	/**
	 * numero del fascicolo (primo fascicolo se l'abbonamento e' da aprire, ultimo fascicolo se l'abbonamento e' da chiudere)
	 */
	public String getNum_fasc() {
		return num_fasc;
	}

	/**
	 * data di pubblicazione del fascicolo (primo fascicolo se l'abbonamento e' da aprire, ultimo fascicolo se l'abbonamento e' da chiudere)
	 */
	public void setData_fasc(java.util.Date value) {
		this.data_fasc = value;
	}

	/**
	 * data di pubblicazione del fascicolo (primo fascicolo se l'abbonamento e' da aprire, ultimo fascicolo se l'abbonamento e' da chiudere)
	 */
	public java.util.Date getData_fasc() {
		return data_fasc;
	}

	/**
	 * anno descrittivo dell'abbonamento
	 */
	public void setAnnata(String value) {
		this.annata = value;
	}

	/**
	 * anno descrittivo dell'abbonamento
	 */
	public String getAnnata() {
		return annata;
	}

	/**
	 * numero del volume relativo all'abbonamento
	 */
	public void setNum_vol_abb(short value) {
		setNum_vol_abb(new Short(value));
	}

	/**
	 * numero del volume relativo all'abbonamento
	 */
	public void setNum_vol_abb(Short value) {
		this.num_vol_abb = value;
	}

	/**
	 * numero del volume relativo all'abbonamento
	 */
	public Short getNum_vol_abb() {
		return num_vol_abb;
	}

	/**
	 * codice identificativo della natura bibliografica
	 */
	public void setNatura(char value) {
		this.natura = value;
	}

	/**
	 * codice identificativo della natura bibliografica
	 */
	public char getNatura() {
		return natura;
	}

	/**
	 * data di chiusura abbonamento
	 */
	public void setData_fine(java.util.Date value) {
		this.data_fine = value;
	}

	/**
	 * data di chiusura abbonamento
	 */
	public java.util.Date getData_fine() {
		return data_fine;
	}

	public void setStampato(boolean value) {
		this.stampato = value;
	}

	public boolean getStampato() {
		return stampato;
	}

	public void setRinnovato(boolean value) {
		this.rinnovato = value;
	}

	public boolean getRinnovato() {
		return rinnovato;
	}

	public void setData_chiusura_ord(java.sql.Timestamp value) {
		this.data_chiusura_ord = value;
	}

	public java.sql.Timestamp getData_chiusura_ord() {
		return data_chiusura_ord;
	}

	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_var(String value) {
		this.ute_var = value;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
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

	public void setTbb_bilancicod_mat(it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci value) {
		this.tbb_bilancicod_mat = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci getTbb_bilancicod_mat() {
		return tbb_bilancicod_mat;
	}

	public void setId_valuta(it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali value) {
		this.id_valuta = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali getId_valuta() {
		return id_valuta;
	}

	public void setCod_fornitore(it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori value) {
		this.cod_fornitore = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori getCod_fornitore() {
		return cod_fornitore;
	}

	public void setId_sez_acquis_bibliografiche(it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche value) {
		this.id_sez_acquis_bibliografiche = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche getId_sez_acquis_bibliografiche() {
		return id_sez_acquis_bibliografiche;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setTba_righe_fatture(java.util.Set value) {
		this.Tba_righe_fatture = value;
	}

	public java.util.Set getTba_righe_fatture() {
		return Tba_righe_fatture;
	}


	public void setTra_ordine_inventari(java.util.Set value) {
		this.Tra_ordine_inventari = value;
	}

	public java.util.Set getTra_ordine_inventari() {
		return Tra_ordine_inventari;
	}


	public String toString() {
		return String.valueOf(getId_ordine());
	}

	public java.util.Set getTbp_esemplare_fascicolo() {
		return Tbp_esemplare_fascicolo;
	}

	public void setTbp_esemplare_fascicolo(java.util.Set tbp_esemplare_fascicolo) {
		Tbp_esemplare_fascicolo = tbp_esemplare_fascicolo;
	}

	public java.util.Set getTrp_messaggio_fascicolo() {
		return Trp_messaggio_fascicolo;
	}

	public void setTrp_messaggio_fascicolo(java.util.Set trp_messaggio_fascicolo) {
		Trp_messaggio_fascicolo = trp_messaggio_fascicolo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Tba_ordini.class))
			return false;
		Tba_ordini other = (Tba_ordini) obj;
		if (id_ordine != other.getId_ordine())
			return false;

		return true;
	}

	public Character getCd_tipo_lav() {
		return cd_tipo_lav;
	}

	public void setCd_tipo_lav(Character cd_tipo_lav) {
		this.cd_tipo_lav = cd_tipo_lav;
	}

	public Tra_ordine_carrello_spedizione getTra_ordine_carrello_spedizione() {
		return tra_ordine_carrello_spedizione;
	}

	public void setTra_ordine_carrello_spedizione(
			Tra_ordine_carrello_spedizione tra_ordine_carrello_spedizione) {
		this.tra_ordine_carrello_spedizione = tra_ordine_carrello_spedizione;
	}

}
