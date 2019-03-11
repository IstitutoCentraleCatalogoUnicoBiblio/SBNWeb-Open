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
/**
 * Offerte del fornitore
 */
/**
 * ORM-Persistable Class
 */
public class Tba_offerte_fornitore implements Serializable {

	private static final long serialVersionUID = 124482073276488922L;

	public Tba_offerte_fornitore() {
	}

	private int id_offerte_fornitore;

	private it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_bib;

	private it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori cod_fornitore;

	private String bid_p;

	private char tip_rec;

	private char natura;

	private String paese;

	private String lingua;

	private char tipo_data;

	private String data1;

	private String num_standard;

	private String aut1;

	private String k_aut1;

	private char tip_aut1;

	private char resp_aut1;

	private String aut2;

	private String k_aut2;

	private char tip_aut2;

	private char resp_aut2;

	private String aut3;

	private String k_aut3;

	private char tip_aut3;

	private char resp_aut3;

	private String aut4;

	private String k_aut4;

	private char tip_aut4;

	private char resp_aut4;

	private String isbd_1;

	private String isbd_2;

	private String k_titolo;

	private String serie1;

	private String k1_serie;

	private String num1_serie;

	private String serie2;

	private String k2_serie;

	private String num2_serie;

	private String serie3;

	private String k3_serie;

	private String num3_serie;

	private char tipo1_classe;

	private String classe1;

	private char tipo2_classe;

	private String classe2;

	private char tipo3_classe;

	private String classe3;

	private String sog1;

	private String k_sog1;

	private String sog2;

	private String k_sog2;

	private String sog3;

	private String k_sog3;

	private String num_stand_pro;

	private java.util.Date data_offerta;

	private String num_offerta_g;

	private java.math.BigDecimal num_linea;

	private String valuta;

	private java.math.BigDecimal prezzo;

	private String tipo_prezzo;

	private String inf_sul_prezzo;

	private String altri_rif;

	private String note_edi;

	private String prot_edi;

	private String num_offerta_p;

	private String bid;

	private char stato_offerta;

	private java.sql.Timestamp ts_ins;

	private String ute_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private void setId_offerte_fornitore(int value) {
		this.id_offerte_fornitore = value;
	}

	public int getId_offerte_fornitore() {
		return id_offerte_fornitore;
	}

	public int getORMID() {
		return getId_offerte_fornitore();
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
	 * codice identificativo del tipo provenienza dei dati
	 */
	public void setTip_rec(char value) {
		this.tip_rec = value;
	}

	/**
	 * codice identificativo del tipo provenienza dei dati
	 */
	public char getTip_rec() {
		return tip_rec;
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
	 * codice del tipo di data
	 */
	public void setTipo_data(char value) {
		this.tipo_data = value;
	}

	/**
	 * codice del tipo di data
	 */
	public char getTipo_data() {
		return tipo_data;
	}

	/**
	 * anno di pubblicazione
	 */
	public void setData1(String value) {
		this.data1 = value;
	}

	/**
	 * anno di pubblicazione
	 */
	public String getData1() {
		return data1;
	}

	/**
	 * codice isbn/issn
	 */
	public void setNum_standard(String value) {
		this.num_standard = value;
	}

	/**
	 * codice isbn/issn
	 */
	public String getNum_standard() {
		return num_standard;
	}

	/**
	 * primo autore
	 */
	public void setAut1(String value) {
		this.aut1 = value;
	}

	/**
	 * primo autore
	 */
	public String getAut1() {
		return aut1;
	}

	/**
	 * chiave normalizzata del primo autore
	 */
	public void setK_aut1(String value) {
		this.k_aut1 = value;
	}

	/**
	 * chiave normalizzata del primo autore
	 */
	public String getK_aut1() {
		return k_aut1;
	}

	/**
	 * tipologia dell'autore
	 */
	public void setTip_aut1(char value) {
		this.tip_aut1 = value;
	}

	/**
	 * tipologia dell'autore
	 */
	public char getTip_aut1() {
		return tip_aut1;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public void setResp_aut1(char value) {
		this.resp_aut1 = value;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public char getResp_aut1() {
		return resp_aut1;
	}

	/**
	 * secondo autore
	 */
	public void setAut2(String value) {
		this.aut2 = value;
	}

	/**
	 * secondo autore
	 */
	public String getAut2() {
		return aut2;
	}

	/**
	 * chiave normalizzata del secondo autore
	 */
	public void setK_aut2(String value) {
		this.k_aut2 = value;
	}

	/**
	 * chiave normalizzata del secondo autore
	 */
	public String getK_aut2() {
		return k_aut2;
	}

	/**
	 * tipologia dell'autore
	 */
	public void setTip_aut2(char value) {
		this.tip_aut2 = value;
	}

	/**
	 * tipologia dell'autore
	 */
	public char getTip_aut2() {
		return tip_aut2;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public void setResp_aut2(char value) {
		this.resp_aut2 = value;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public char getResp_aut2() {
		return resp_aut2;
	}

	/**
	 * terzo autore
	 */
	public void setAut3(String value) {
		this.aut3 = value;
	}

	/**
	 * terzo autore
	 */
	public String getAut3() {
		return aut3;
	}

	/**
	 * chiave normalizzata del terzo autore
	 */
	public void setK_aut3(String value) {
		this.k_aut3 = value;
	}

	/**
	 * chiave normalizzata del terzo autore
	 */
	public String getK_aut3() {
		return k_aut3;
	}

	/**
	 * tipologia dell'autore
	 */
	public void setTip_aut3(char value) {
		this.tip_aut3 = value;
	}

	/**
	 * tipologia dell'autore
	 */
	public char getTip_aut3() {
		return tip_aut3;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public void setResp_aut3(char value) {
		this.resp_aut3 = value;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public char getResp_aut3() {
		return resp_aut3;
	}

	/**
	 * quarto autore
	 */
	public void setAut4(String value) {
		this.aut4 = value;
	}

	/**
	 * quarto autore
	 */
	public String getAut4() {
		return aut4;
	}

	/**
	 * chiave normalizzata del quarto autore
	 */
	public void setK_aut4(String value) {
		this.k_aut4 = value;
	}

	/**
	 * chiave normalizzata del quarto autore
	 */
	public String getK_aut4() {
		return k_aut4;
	}

	/**
	 * tipologia dell'autore
	 */
	public void setTip_aut4(char value) {
		this.tip_aut4 = value;
	}

	/**
	 * tipologia dell'autore
	 */
	public char getTip_aut4() {
		return tip_aut4;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public void setResp_aut4(char value) {
		this.resp_aut4 = value;
	}

	/**
	 * codice del tipo di responsabilita' dell'autore
	 */
	public char getResp_aut4() {
		return resp_aut4;
	}

	/**
	 * descrizione in formato isbd
	 */
	public void setIsbd_1(String value) {
		this.isbd_1 = value;
	}

	/**
	 * descrizione in formato isbd
	 */
	public String getIsbd_1() {
		return isbd_1;
	}

	/**
	 * descrizione in formato isbd
	 */
	public void setIsbd_2(String value) {
		this.isbd_2 = value;
	}

	/**
	 * descrizione in formato isbd
	 */
	public String getIsbd_2() {
		return isbd_2;
	}

	/**
	 * chiave normalizzata del titolo
	 */
	public void setK_titolo(String value) {
		this.k_titolo = value;
	}

	/**
	 * chiave normalizzata del titolo
	 */
	public String getK_titolo() {
		return k_titolo;
	}

	/**
	 * collana
	 */
	public void setSerie1(String value) {
		this.serie1 = value;
	}

	/**
	 * collana
	 */
	public String getSerie1() {
		return serie1;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public void setK1_serie(String value) {
		this.k1_serie = value;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public String getK1_serie() {
		return k1_serie;
	}

	/**
	 * numero della collana
	 */
	public void setNum1_serie(String value) {
		this.num1_serie = value;
	}

	/**
	 * numero della collana
	 */
	public String getNum1_serie() {
		return num1_serie;
	}

	/**
	 * collana
	 */
	public void setSerie2(String value) {
		this.serie2 = value;
	}

	/**
	 * collana
	 */
	public String getSerie2() {
		return serie2;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public void setK2_serie(String value) {
		this.k2_serie = value;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public String getK2_serie() {
		return k2_serie;
	}

	/**
	 * numero della collana
	 */
	public void setNum2_serie(String value) {
		this.num2_serie = value;
	}

	/**
	 * numero della collana
	 */
	public String getNum2_serie() {
		return num2_serie;
	}

	/**
	 * collana
	 */
	public void setSerie3(String value) {
		this.serie3 = value;
	}

	/**
	 * collana
	 */
	public String getSerie3() {
		return serie3;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public void setK3_serie(String value) {
		this.k3_serie = value;
	}

	/**
	 * chiave normalizzata della collana
	 */
	public String getK3_serie() {
		return k3_serie;
	}

	/**
	 * numero della collana
	 */
	public void setNum3_serie(String value) {
		this.num3_serie = value;
	}

	/**
	 * numero della collana
	 */
	public String getNum3_serie() {
		return num3_serie;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public void setTipo1_classe(char value) {
		this.tipo1_classe = value;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public char getTipo1_classe() {
		return tipo1_classe;
	}

	/**
	 * codice identificativo della classe
	 */
	public void setClasse1(String value) {
		this.classe1 = value;
	}

	/**
	 * codice identificativo della classe
	 */
	public String getClasse1() {
		return classe1;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public void setTipo2_classe(char value) {
		this.tipo2_classe = value;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public char getTipo2_classe() {
		return tipo2_classe;
	}

	/**
	 * codice identificativo della classe
	 */
	public void setClasse2(String value) {
		this.classe2 = value;
	}

	/**
	 * codice identificativo della classe
	 */
	public String getClasse2() {
		return classe2;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public void setTipo3_classe(char value) {
		this.tipo3_classe = value;
	}

	/**
	 * codice del sistema di classificazione
	 */
	public char getTipo3_classe() {
		return tipo3_classe;
	}

	/**
	 * codice identificativo della classe
	 */
	public void setClasse3(String value) {
		this.classe3 = value;
	}

	/**
	 * codice identificativo della classe
	 */
	public String getClasse3() {
		return classe3;
	}

	/**
	 * soggetto
	 */
	public void setSog1(String value) {
		this.sog1 = value;
	}

	/**
	 * soggetto
	 */
	public String getSog1() {
		return sog1;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public void setK_sog1(String value) {
		this.k_sog1 = value;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public String getK_sog1() {
		return k_sog1;
	}

	/**
	 * soggetto
	 */
	public void setSog2(String value) {
		this.sog2 = value;
	}

	/**
	 * soggetto
	 */
	public String getSog2() {
		return sog2;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public void setK_sog2(String value) {
		this.k_sog2 = value;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public String getK_sog2() {
		return k_sog2;
	}

	/**
	 * soggetto
	 */
	public void setSog3(String value) {
		this.sog3 = value;
	}

	/**
	 * soggetto
	 */
	public String getSog3() {
		return sog3;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public void setK_sog3(String value) {
		this.k_sog3 = value;
	}

	/**
	 * chiave normalizzata del soggetto
	 */
	public String getK_sog3() {
		return k_sog3;
	}

	/**
	 * numero standard assegnato dal libraio o dal db esterno
	 */
	public void setNum_stand_pro(String value) {
		this.num_stand_pro = value;
	}

	/**
	 * numero standard assegnato dal libraio o dal db esterno
	 */
	public String getNum_stand_pro() {
		return num_stand_pro;
	}

	/**
	 * data dell'offerta (edi)
	 */
	public void setData_offerta(java.util.Date value) {
		this.data_offerta = value;
	}

	/**
	 * data dell'offerta (edi)
	 */
	public java.util.Date getData_offerta() {
		return data_offerta;
	}

	/**
	 * numero dell'offerta generale
	 */
	public void setNum_offerta_g(String value) {
		this.num_offerta_g = value;
	}

	/**
	 * numero dell'offerta generale
	 */
	public String getNum_offerta_g() {
		return num_offerta_g;
	}

	/**
	 * numero di linea dell'offerta
	 */
	public void setNum_linea(java.math.BigDecimal value) {
		this.num_linea = value;
	}

	/**
	 * numero di linea dell'offerta
	 */
	public java.math.BigDecimal getNum_linea() {
		return num_linea;
	}

	/**
	 * codice della valuta
	 */
	public void setValuta(String value) {
		this.valuta = value;
	}

	/**
	 * codice della valuta
	 */
	public String getValuta() {
		return valuta;
	}

	/**
	 * prezzo
	 */
	public void setPrezzo(java.math.BigDecimal value) {
		this.prezzo = value;
	}

	/**
	 * prezzo
	 */
	public java.math.BigDecimal getPrezzo() {
		return prezzo;
	}

	/**
	 * codice della tipologia di prezzo (ca=catalogo, ct=contratto, di=prezzo del distributore)
	 */
	public void setTipo_prezzo(String value) {
		this.tipo_prezzo = value;
	}

	/**
	 * codice della tipologia di prezzo (ca=catalogo, ct=contratto, di=prezzo del distributore)
	 */
	public String getTipo_prezzo() {
		return tipo_prezzo;
	}

	/**
	 * informazioni sul prezzo
	 */
	public void setInf_sul_prezzo(String value) {
		this.inf_sul_prezzo = value;
	}

	/**
	 * informazioni sul prezzo
	 */
	public String getInf_sul_prezzo() {
		return inf_sul_prezzo;
	}

	/**
	 * altri riferimenti
	 */
	public void setAltri_rif(String value) {
		this.altri_rif = value;
	}

	/**
	 * altri riferimenti
	 */
	public String getAltri_rif() {
		return altri_rif;
	}

	/**
	 * note edi
	 */
	public void setNote_edi(String value) {
		this.note_edi = value;
	}

	/**
	 * note edi
	 */
	public String getNote_edi() {
		return note_edi;
	}

	/**
	 * coordinate messaggio edi
	 */
	public void setProt_edi(String value) {
		this.prot_edi = value;
	}

	/**
	 * coordinate messaggio edi
	 */
	public String getProt_edi() {
		return prot_edi;
	}

	/**
	 * numero dell'offerta particolare
	 */
	public void setNum_offerta_p(String value) {
		this.num_offerta_p = value;
	}

	/**
	 * numero dell'offerta particolare
	 */
	public String getNum_offerta_p() {
		return num_offerta_p;
	}

	/**
	 * codice identificativo dell'oggetto bibliograficoe
	 */
	public void setBid(String value) {
		this.bid = value;
	}

	/**
	 * codice identificativo dell'oggetto bibliograficoe
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * stato dell'offerta
	 */
	public void setStato_offerta(char value) {
		this.stato_offerta = value;
	}

	/**
	 * stato dell'offerta
	 */
	public char getStato_offerta() {
		return stato_offerta;
	}

	/**
	 * data e ora d'inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * data e ora d'inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
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

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * data e ora dell'ultimo aggiornamento
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setCod_fornitore(it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori value) {
		this.cod_fornitore = value;
	}

	public it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori getCod_fornitore() {
		return cod_fornitore;
	}

	public void setCd_bib(it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_bib = value;
	}

	public it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public String toString() {
		return String.valueOf(getId_offerte_fornitore());
	}

}
