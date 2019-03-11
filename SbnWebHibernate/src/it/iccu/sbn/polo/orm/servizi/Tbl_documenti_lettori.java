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
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.util.HashSet;
import java.util.Set;

/**
 * Documenti inseriti dai lettori
 */
/**
 * ORM-Persistable Class
 */
public class Tbl_documenti_lettori extends Tb_base {

	private static final long serialVersionUID = -6313570529866582342L;

	private int id_documenti_lettore;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private char tipo_doc_lett;

	private long cod_doc_lett;

	private it.iccu.sbn.polo.orm.servizi.Tbl_utenti id_utenti;

	private String titolo;

	private String luogo_edizione;

	private String editore;

	private java.math.BigDecimal anno_edizione;

	private String autore;

	private String num_volume;

	private String annata;

	private Character tipo_data;

	private java.math.BigDecimal prima_data;

	private java.math.BigDecimal seconda_data;

	private Character stato_catal;

	private char natura;

	private String paese;

	private String lingua;

	private char fonte;

	private char stato_sugg;

	private String bid;

	private java.util.Date data_sugg_lett;

	private String note;

	private String msg_lettore;

	private String segnatura;

	private String ord_segnatura;

	private String cod_bib_inv;

	private String cod_serie;

	private Integer cod_inven;

	private Set<Tbl_richiesta_servizio> Tbl_richiesta_servizio = new HashSet<Tbl_richiesta_servizio>();

	private Set<Tbl_esemplare_documento_lettore> Tbl_esemplare_documento_lettore = new HashSet<Tbl_esemplare_documento_lettore>();

	private String denom_biblioteca_doc;

	private String cd_catfrui;

	private String cd_no_disp;

	// almaviva5_20141125 servizi ill
	private Character tp_numero_std;
	private String numero_std;
	private String biblioteche;

	private Character tp_record_uni;

	private String ente_curatore;
	private String fa_parte;
	private String fascicolo;
	private String data_pubb;
	private String autore_articolo;
	private String titolo_articolo;
	private String pagine;
	private String fonte_rif;

	public String getDenom_biblioteca_doc() {
		return denom_biblioteca_doc;
	}

	public void setDenom_biblioteca_doc(String denom_biblioteca_doc) {
		this.denom_biblioteca_doc = denom_biblioteca_doc;
	}

	public String getCd_catfrui() {
		return cd_catfrui;
	}

	public void setCd_catfrui(String cd_catfrui) {
		this.cd_catfrui = cd_catfrui;
	}

	public String getCd_no_disp() {
		return cd_no_disp;
	}

	public void setCd_no_disp(String cd_no_disp) {
		this.cd_no_disp = cd_no_disp;
	}

	protected void setId_documenti_lettore(int value) {
		this.id_documenti_lettore = value;
	}

	public int getId_documenti_lettore() {
		return id_documenti_lettore;
	}

	public int getORMID() {
		return getId_documenti_lettore();
	}

	/**
	 * codice identificativo della tipologia del documento lettore
	 */
	public void setTipo_doc_lett(char value) {
		this.tipo_doc_lett = value;
	}

	/**
	 * codice identificativo della tipologia del documento lettore
	 */
	public char getTipo_doc_lett() {
		return tipo_doc_lett;
	}

	/**
	 * codice identificativo del documento lettore
	 */
	public void setCod_doc_lett(long value) {
		this.cod_doc_lett = value;
	}

	/**
	 * codice identificativo del documento lettore
	 */
	public long getCod_doc_lett() {
		return cod_doc_lett;
	}

	/**
	 * titolo proprio
	 */
	public void setTitolo(String value) {
		this.titolo = value;
	}

	/**
	 * titolo proprio
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * luogo di edizione
	 */
	public void setLuogo_edizione(String value) {
		this.luogo_edizione = value;
	}

	/**
	 * luogo di edizione
	 */
	public String getLuogo_edizione() {
		return luogo_edizione;
	}

	/**
	 * editore
	 */
	public void setEditore(String value) {
		this.editore = value;
	}

	/**
	 * editore
	 */
	public String getEditore() {
		return editore;
	}

	/**
	 * anno di edizione
	 */
	public void setAnno_edizione(java.math.BigDecimal value) {
		this.anno_edizione = value;
	}

	/**
	 * anno di edizione
	 */
	public java.math.BigDecimal getAnno_edizione() {
		return anno_edizione;
	}

	/**
	 * primo autore
	 */
	public void setAutore(String value) {
		this.autore = value;
	}

	/**
	 * primo autore
	 */
	public String getAutore() {
		return autore;
	}

	/**
	 * numero del volume della monografia
	 */
	public void setNum_volume(short value) {
		setNum_volume(new Short(value));
	}

	/**
	 * numero del volume della monografia
	 */
	public void setNum_volume(String value) {
		this.num_volume = value;
	}

	/**
	 * numero del volume della monografia
	 */
	public String getNum_volume() {
		return num_volume;
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
	 * codice del tipo di data
	 */
	public void setTipo_data(char value) {
		setTipo_data(new Character(value));
	}

	/**
	 * codice del tipo di data
	 */
	public void setTipo_data(Character value) {
		this.tipo_data = value;
	}

	/**
	 * codice del tipo di data
	 */
	public Character getTipo_data() {
		return tipo_data;
	}

	/**
	 * prima data
	 */
	public void setPrima_data(java.math.BigDecimal value) {
		this.prima_data = value;
	}

	/**
	 * prima data
	 */
	public java.math.BigDecimal getPrima_data() {
		return prima_data;
	}

	/**
	 * seconda data
	 */
	public void setSeconda_data(java.math.BigDecimal value) {
		this.seconda_data = value;
	}

	/**
	 * seconda data
	 */
	public java.math.BigDecimal getSeconda_data() {
		return seconda_data;
	}

	/**
	 * codice dello stato di catalogazione
	 */
	public void setStato_catal(char value) {
		setStato_catal(new Character(value));
	}

	/**
	 * codice dello stato di catalogazione
	 */
	public void setStato_catal(Character value) {
		this.stato_catal = value;
	}

	/**
	 * codice dello stato di catalogazione
	 */
	public Character getStato_catal() {
		return stato_catal;
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
	 * codice identificativo della lingua
	 */
	public void setLingua(String value) {
		this.lingua = value;
	}

	/**
	 * codice identificativo della lingua
	 */
	public String getLingua() {
		return lingua;
	}

	/**
	 * codice identificativo della tipologia di riproduzione di un documento
	 */
	public void setFonte(char value) {
		this.fonte = value;
	}

	/**
	 * codice identificativo della tipologia di riproduzione di un documento
	 */
	public char getFonte() {
		return fonte;
	}

	/**
	 * stato del suggerimento ("w" = attesa di risposta, "a" = accettato, "r" =
	 * rifiutato, "o" = ordinato)
	 */
	public void setStato_sugg(char value) {
		this.stato_sugg = value;
	}

	/**
	 * stato del suggerimento ("w" = attesa di risposta, "a" = accettato, "r" =
	 * rifiutato, "o" = ordinato)
	 */
	public char getStato_sugg() {
		return stato_sugg;
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
	 * data di registrazione del suggerimento
	 */
	public void setData_sugg_lett(java.util.Date value) {
		this.data_sugg_lett = value;
	}

	/**
	 * data di registrazione del suggerimento
	 */
	public java.util.Date getData_sugg_lett() {
		return data_sugg_lett;
	}

	/**
	 * note relative al suggerimento dato dal lettore
	 */
	public void setNote(String value) {
		this.note = value;
	}

	/**
	 * note relative al suggerimento dato dal lettore
	 */
	public String getNote() {
		return note;
	}

	/**
	 * messaggio per il lettore relativo al suggerimento
	 */
	public void setMsg_lettore(String value) {
		this.msg_lettore = value;
	}

	/**
	 * messaggio per il lettore relativo al suggerimento
	 */
	public String getMsg_lettore() {
		return msg_lettore;
	}

	/**
	 * numero di segnatura (campo obbligatorio per tipo_doc_lett=p)
	 */
	public void setSegnatura(String value) {
		this.segnatura = value;
	}

	/**
	 * numero di segnatura (campo obbligatorio per tipo_doc_lett=p)
	 */
	public String getSegnatura() {
		return segnatura;
	}

	/**
	 * codice identificativo della biblioteca che possiede il documento
	 * richiesto
	 */
	public void setCod_bib_inv(String value) {
		this.cod_bib_inv = value;
	}

	/**
	 * codice identificativo della biblioteca che possiede il documento
	 * richiesto
	 */
	public String getCod_bib_inv() {
		return cod_bib_inv;
	}

	/**
	 * codice identificativo della serie inventariale del documento richiesto
	 */
	public void setCod_serie(String value) {
		this.cod_serie = value;
	}

	/**
	 * codice identificativo della serie inventariale del documento richiesto
	 */
	public String getCod_serie() {
		return cod_serie;
	}

	/**
	 * numero d'inventario del documento richiesto
	 */
	public void setCod_inven(int value) {
		setCod_inven(new Integer(value));
	}

	/**
	 * numero d'inventario del documento richiesto
	 */
	public void setCod_inven(Integer value) {
		this.cod_inven = value;
	}

	/**
	 * numero d'inventario del documento richiesto
	 */
	public Integer getCod_inven() {
		return cod_inven;
	}

	public void setOrd_segnatura(String value) {
		this.ord_segnatura = value;
	}

	public String getOrd_segnatura() {
		return ord_segnatura;
	}

	public void setCd_bib(Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setId_utenti(Tbl_utenti value) {
		this.id_utenti = value;
	}

	public Tbl_utenti getId_utenti() {
		return id_utenti;
	}

	public void setTbl_richiesta_servizio(Set<Tbl_richiesta_servizio> value) {
		this.Tbl_richiesta_servizio = value;
	}

	public Set<Tbl_richiesta_servizio> getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}

	public void setTbl_esemplare_documento_lettore(Set<Tbl_esemplare_documento_lettore> value) {
		this.Tbl_esemplare_documento_lettore = value;
	}

	public Set<Tbl_esemplare_documento_lettore> getTbl_esemplare_documento_lettore() {
		return Tbl_esemplare_documento_lettore;
	}

	public String toString() {
		return String.valueOf(getId_documenti_lettore());
	}

	public Character getTp_numero_std() {
		return tp_numero_std;
	}

	public void setTp_numero_std(Character tp_numero_std) {
		this.tp_numero_std = tp_numero_std;
	}

	public String getNumero_std() {
		return numero_std;
	}

	public void setNumero_std(String numero_std) {
		this.numero_std = numero_std;
	}

	public String getBiblioteche() {
		return biblioteche;
	}

	public void setBiblioteche(String biblioteche) {
		this.biblioteche = biblioteche;
	}

	public Character getTp_record_uni() {
		return tp_record_uni;
	}

	public void setTp_record_uni(Character tp_record_uni) {
		this.tp_record_uni = tp_record_uni;
	}

	public String getEnte_curatore() {
		return ente_curatore;
	}

	public void setEnte_curatore(String ente_curatore) {
		this.ente_curatore = ente_curatore;
	}

	public String getFa_parte() {
		return fa_parte;
	}

	public void setFa_parte(String fa_parte) {
		this.fa_parte = fa_parte;
	}

	public String getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(String fascicolo) {
		this.fascicolo = fascicolo;
	}

	public String getData_pubb() {
		return data_pubb;
	}

	public void setData_pubb(String data_pubb) {
		this.data_pubb = data_pubb;
	}

	public String getAutore_articolo() {
		return autore_articolo;
	}

	public void setAutore_articolo(String autore_articolo) {
		this.autore_articolo = autore_articolo;
	}

	public String getTitolo_articolo() {
		return titolo_articolo;
	}

	public void setTitolo_articolo(String titolo_articolo) {
		this.titolo_articolo = titolo_articolo;
	}

	public String getPagine() {
		return pagine;
	}

	public void setPagine(String pagine) {
		this.pagine = pagine;
	}

	public String getFonte_rif() {
		return fonte_rif;
	}

	public void setFonte_rif(String fonte_rif) {
		this.fonte_rif = fonte_rif;
	}

}
