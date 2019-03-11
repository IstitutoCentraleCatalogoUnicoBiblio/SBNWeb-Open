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
package it.iccu.sbn.polo.orm.documentofisico;

import java.io.Serializable;
/**
 * INVENTARI
 */
/**
 * ORM-Persistable Class
 */
public class Tbc_inventario implements Serializable {

	private static final long serialVersionUID = -5975428393401099586L;

	public Tbc_inventario() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Tbc_inventario))
			return false;
		Tbc_inventario tbc_inventario = (Tbc_inventario)aObj;
		if (getCd_serie() == null && tbc_inventario.getCd_serie() != null)
			return false;
		if (!getCd_serie().equals(tbc_inventario.getCd_serie()))
			return false;
		if (getCd_inven() != tbc_inventario.getCd_inven())
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getCd_serie() != null) {
			hashcode = hashcode + (getCd_serie().getCd_serie() == null ? 0 : getCd_serie().getCd_serie().hashCode());
			hashcode = hashcode + (getCd_serie().getCd_polo() == null ? 0 : getCd_serie().getCd_polo().hashCode());
		}
		hashcode = hashcode + getCd_inven();
		return hashcode;
	}

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale cd_serie;

	private int cd_inven;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario cd_proven;

	private it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione key_loc;

	private it.iccu.sbn.polo.orm.bibliografica.Tb_titolo b;

	private Integer key_loc_old;

	private String seq_coll;

	private String precis_inv;

	private char cd_sit;

	private java.math.BigDecimal valore;

	private java.math.BigDecimal importo;

	private Integer num_vol;

	private Integer tot_loc;

	private Integer tot_inter;

	private Integer anno_abb;

	private char flg_disp;

	private char flg_nuovo_usato;

	private String stato_con;

	private Integer cd_fornitore;

	private String cd_mat_inv;

	private String cd_bib_ord;

	private Character cd_tip_ord;

	private Integer anno_ord;

	private Integer cd_ord;

	private Integer riga_ord;

	private String cd_bib_fatt;

	private Integer anno_fattura;

	private Integer prg_fattura;

	private Integer riga_fattura;

	private String cd_no_disp;

	private String cd_frui;

	private Character cd_carico;

	private Integer num_carico;

	private java.util.Date data_carico;

	private String cd_polo_scar;

	private String cd_bib_scar;

	private Character cd_scarico;

	private Integer num_scarico;

	private java.util.Date data_scarico;

	private java.util.Date data_delibera;

	private String delibera_scar;

	private String sez_old;

	private String loc_old;

	private String spec_old;

	private String cd_supporto;

	private String ute_ins_prima_coll;

	private java.sql.Timestamp ts_ins_prima_coll;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private char fl_canc;

	private Character tipo_acquisizione;

	private String supporto_copia;

	private Character digitalizzazione;

	private String disp_copia_digitale;

	private String id_accesso_remoto;

	private String rif_teca_digitale;

	private String cd_riproducibilita;

	private java.util.Date data_ingresso;

	private java.util.Date data_redisp_prev;

	private Integer id_bib_scar;



	private java.util.Set Trc_poss_prov_inventari = new java.util.HashSet();

	private java.util.Set Tbl_richiesta_servizio = new java.util.HashSet();

	private java.util.Set Tra_ordine_inventari = new java.util.HashSet();

	private java.util.Set Tbc_nota_inv = new java.util.HashSet();

	private java.util.Set Tbp_esemplare_fascicolo  = new java.util.HashSet();


	public void setCd_inven(int value) {
		this.cd_inven = value;
	}

	public int getCd_inven() {
		return cd_inven;
	}

	/**
	 * chiave della collocazione definitiva (utilizzato nella funzione di spostamento temporaneo di collocazioni)
	 */
	public void setKey_loc_old(int value) {
		setKey_loc_old(new Integer(value));
	}

	/**
	 * chiave della collocazione definitiva (utilizzato nella funzione di spostamento temporaneo di collocazioni)
	 */
	public void setKey_loc_old(Integer value) {
		this.key_loc_old = value;
	}

	/**
	 * chiave della collocazione definitiva (utilizzato nella funzione di spostamento temporaneo di collocazioni)
	 */
	public Integer getKey_loc_old() {
		return key_loc_old;
	}

	/**
	 * sequenza della collocazione
	 */
	public void setSeq_coll(String value) {
		this.seq_coll = value;
	}

	/**
	 * sequenza della collocazione
	 */
	public String getSeq_coll() {
		return seq_coll;
	}

	/**
	 * precisazione dell'inventario
	 */
	public void setPrecis_inv(String value) {
		this.precis_inv = value;
	}

	/**
	 * precisazione dell'inventario
	 */
	public String getPrecis_inv() {
		return precis_inv;
	}

	/**
	 * codice identificativo della situazione amministrativa
	 */
	public void setCd_sit(char value) {
		this.cd_sit = value;
	}

	/**
	 * codice identificativo della situazione amministrativa
	 */
	public char getCd_sit() {
		return cd_sit;
	}

	/**
	 * valore inventariale
	 */
	public void setValore(java.math.BigDecimal value) {
		this.valore = value;
	}

	/**
	 * valore inventariale
	 */
	public java.math.BigDecimal getValore() {
		return valore;
	}

	/**
	 * importo reale
	 */
	public void setImporto(java.math.BigDecimal value) {
		this.importo = value;
	}

	/**
	 * importo reale
	 */
	public java.math.BigDecimal getImporto() {
		return importo;
	}

	/**
	 * numero dei volumi in cui risultano rilegati i periodici aventi uno stesso numero di inventario
	 */
	public void setNum_vol(int value) {
		setNum_vol(new Integer(value));
	}

	/**
	 * numero dei volumi in cui risultano rilegati i periodici aventi uno stesso numero di inventario
	 */
	public void setNum_vol(Integer value) {
		this.num_vol = value;
	}

	/**
	 * numero dei volumi in cui risultano rilegati i periodici aventi uno stesso numero di inventario
	 */
	public Integer getNum_vol() {
		return num_vol;
	}

	/**
	 * totale dei prestiti locali dell'inventario
	 */
	public void setTot_loc(int value) {
		setTot_loc(new Integer(value));
	}

	/**
	 * totale dei prestiti locali dell'inventario
	 */
	public void setTot_loc(Integer value) {
		this.tot_loc = value;
	}

	/**
	 * totale dei prestiti locali dell'inventario
	 */
	public Integer getTot_loc() {
		return tot_loc;
	}

	/**
	 * totale dei prestiti interbibliotecari
	 */
	public void setTot_inter(int value) {
		setTot_inter(new Integer(value));
	}

	/**
	 * totale dei prestiti interbibliotecari
	 */
	public void setTot_inter(Integer value) {
		this.tot_inter = value;
	}

	/**
	 * totale dei prestiti interbibliotecari
	 */
	public Integer getTot_inter() {
		return tot_inter;
	}

	/**
	 * anno di abbonamento dell'inventario (per periodici)
	 */
	public void setAnno_abb(int value) {
		setAnno_abb(new Integer(value));
	}

	/**
	 * anno di abbonamento dell'inventario (per periodici)
	 */
	public void setAnno_abb(Integer value) {
		this.anno_abb = value;
	}

	/**
	 * anno di abbonamento dell'inventario (per periodici)
	 */
	public Integer getAnno_abb() {
		return anno_abb;
	}

	/**
	 * indicatore di disponibilia'/non disponibilita'
	 */
	public void setFlg_disp(char value) {
		this.flg_disp = value;
	}

	/**
	 * indicatore di disponibilia'/non disponibilita'
	 */
	public char getFlg_disp() {
		return flg_disp;
	}

	/**
	 * indicatore di inventario acquisto usato ammette i valori ' ' =nuovo, 'u' = usato
	 */
	public void setFlg_nuovo_usato(char value) {
		this.flg_nuovo_usato = value;
	}

	/**
	 * indicatore di inventario acquisto usato ammette i valori ' ' =nuovo, 'u' = usato
	 */
	public char getFlg_nuovo_usato() {
		return flg_nuovo_usato;
	}

	/**
	 * codice dello stato di conservazione
	 */
	public void setStato_con(String value) {
		this.stato_con = value;
	}

	/**
	 * codice dello stato di conservazione
	 */
	public String getStato_con() {
		return stato_con;
	}

	/**
	 * codice del fornitore
	 */
	public void setCd_fornitore(int value) {
		setCd_fornitore(new Integer(value));
	}

	/**
	 * codice del fornitore
	 */
	public void setCd_fornitore(Integer value) {
		this.cd_fornitore = value;
	}

	/**
	 * codice del fornitore
	 */
	public Integer getCd_fornitore() {
		return cd_fornitore;
	}

	/**
	 * codice identificativo del materiale da inventariare
	 */
	public void setCd_mat_inv(String value) {
		this.cd_mat_inv = value;
	}

	/**
	 * codice identificativo del materiale da inventariare
	 */
	public String getCd_mat_inv() {
		return cd_mat_inv;
	}

	/**
	 * codice identificativo della biblioteca che ha effettuato l'ordine
	 */
	public void setCd_bib_ord(String value) {
		this.cd_bib_ord = value;
	}

	/**
	 * codice identificativo della biblioteca che ha effettuato l'ordine
	 */
	public String getCd_bib_ord() {
		return cd_bib_ord;
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public void setCd_tip_ord(char value) {
		setCd_tip_ord(new Character(value));
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public void setCd_tip_ord(Character value) {
		this.cd_tip_ord = value;
	}

	/**
	 * codice identificativo della tipologia dell'ordine
	 */
	public Character getCd_tip_ord() {
		return cd_tip_ord;
	}

	/**
	 * anno d'acquisizione dell'ordine
	 */
	public void setAnno_ord(int value) {
		setAnno_ord(new Integer(value));
	}

	/**
	 * anno d'acquisizione dell'ordine
	 */
	public void setAnno_ord(Integer value) {
		this.anno_ord = value;
	}

	/**
	 * anno d'acquisizione dell'ordine
	 */
	public Integer getAnno_ord() {
		return anno_ord;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public void setCd_ord(int value) {
		setCd_ord(new Integer(value));
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public void setCd_ord(Integer value) {
		this.cd_ord = value;
	}

	/**
	 * codice identificativo dell'ordine
	 */
	public Integer getCd_ord() {
		return cd_ord;
	}

	public void setRiga_ord(int value) {
		setRiga_ord(new Integer(value));
	}

	public void setRiga_ord(Integer value) {
		this.riga_ord = value;
	}

	public Integer getRiga_ord() {
		return riga_ord;
	}

	/**
	 * codice identificativo della biblioteca che ha emesso la fattura
	 */
	public void setCd_bib_fatt(String value) {
		this.cd_bib_fatt = value;
	}

	/**
	 * codice identificativo della biblioteca che ha emesso la fattura
	 */
	public String getCd_bib_fatt() {
		return cd_bib_fatt;
	}

	/**
	 * anno di registrazione della fattura
	 */
	public void setAnno_fattura(int value) {
		setAnno_fattura(new Integer(value));
	}

	/**
	 * anno di registrazione della fattura
	 */
	public void setAnno_fattura(Integer value) {
		this.anno_fattura = value;
	}

	/**
	 * anno di registrazione della fattura
	 */
	public Integer getAnno_fattura() {
		return anno_fattura;
	}

	/**
	 * progressivo che identifica la fattura nell'ambito dell'anno
	 */
	public void setPrg_fattura(int value) {
		setPrg_fattura(new Integer(value));
	}

	/**
	 * progressivo che identifica la fattura nell'ambito dell'anno
	 */
	public void setPrg_fattura(Integer value) {
		this.prg_fattura = value;
	}

	/**
	 * progressivo che identifica la fattura nell'ambito dell'anno
	 */
	public Integer getPrg_fattura() {
		return prg_fattura;
	}

	/**
	 * numero della riga della fattura
	 */
	public void setRiga_fattura(int value) {
		setRiga_fattura(new Integer(value));
	}

	/**
	 * numero della riga della fattura
	 */
	public void setRiga_fattura(Integer value) {
		this.riga_fattura = value;
	}

	/**
	 * numero della riga della fattura
	 */
	public Integer getRiga_fattura() {
		return riga_fattura;
	}

	/**
	 * codice di non disponibilita' del documento
	 */
	public void setCd_no_disp(String value) {
		this.cd_no_disp = value;
	}

	/**
	 * codice di non disponibilita' del documento
	 */
	public String getCd_no_disp() {
		return cd_no_disp;
	}

	/**
	 * codice della fruizione: ammette i valori dati dalla combinazione dei vari tipi di servizio ammissibili per un inventario: ad esempio consultazione e prestito solo locale
	 */
	public void setCd_frui(String value) {
		this.cd_frui = value;
	}

	/**
	 * codice della fruizione: ammette i valori dati dalla combinazione dei vari tipi di servizio ammissibili per un inventario: ad esempio consultazione e prestito solo locale
	 */
	public String getCd_frui() {
		return cd_frui;
	}

	/**
	 * codice di carico
	 */
	public void setCd_carico(char value) {
		setCd_carico(new Character(value));
	}

	/**
	 * codice di carico
	 */
	public void setCd_carico(Character value) {
		this.cd_carico = value;
	}

	/**
	 * codice di carico
	 */
	public Character getCd_carico() {
		return cd_carico;
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public void setNum_carico(int value) {
		setNum_carico(new Integer(value));
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public void setNum_carico(Integer value) {
		this.num_carico = value;
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public Integer getNum_carico() {
		return num_carico;
	}

	/**
	 * data di scarico o carico
	 */
	public void setData_carico(java.util.Date value) {
		this.data_carico = value;
	}

	/**
	 * data di scarico o carico
	 */
	public java.util.Date getData_carico() {
		return data_carico;
	}

	/**
	 * codice polo della biblioteca verso cui si scarica
	 */
	public void setCd_polo_scar(String value) {
		this.cd_polo_scar = value;
	}

	/**
	 * codice polo della biblioteca verso cui si scarica
	 */
	public String getCd_polo_scar() {
		return cd_polo_scar;
	}

	/**
	 * codice identificativo della biblioteca verso cui si effettua lo scarico
	 */
	public void setCd_bib_scar(String value) {
		this.cd_bib_scar = value;
	}

	/**
	 * codice identificativo della biblioteca verso cui si effettua lo scarico
	 */
	public String getCd_bib_scar() {
		return cd_bib_scar;
	}

	/**
	 * codice di scarico
	 */
	public void setCd_scarico(char value) {
		setCd_scarico(new Character(value));
	}

	/**
	 * codice di scarico
	 */
	public void setCd_scarico(Character value) {
		this.cd_scarico = value;
	}

	/**
	 * codice di scarico
	 */
	public Character getCd_scarico() {
		return cd_scarico;
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public void setNum_scarico(int value) {
		setNum_scarico(new Integer(value));
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public void setNum_scarico(Integer value) {
		this.num_scarico = value;
	}

	/**
	 * numero del buono di scarico o di carico
	 */
	public Integer getNum_scarico() {
		return num_scarico;
	}

	/**
	 * data di scarico o carico
	 */
	public void setData_scarico(java.util.Date value) {
		this.data_scarico = value;
	}

	/**
	 * data di scarico o carico
	 */
	public java.util.Date getData_scarico() {
		return data_scarico;
	}

	/**
	 * data della delibera di scarico
	 */
	public void setData_delibera(java.util.Date value) {
		this.data_delibera = value;
	}

	/**
	 * data della delibera di scarico
	 */
	public java.util.Date getData_delibera() {
		return data_delibera;
	}

	/**
	 * testo della delibera di scarico
	 */
	public void setDelibera_scar(String value) {
		this.delibera_scar = value;
	}

	/**
	 * testo della delibera di scarico
	 */
	public String getDelibera_scar() {
		return delibera_scar;
	}

	/**
	 * sezione precedente
	 */
	public void setSez_old(String value) {
		this.sez_old = value;
	}

	/**
	 * sezione precedente
	 */
	public String getSez_old() {
		return sez_old;
	}

	/**
	 * collocazione precedente
	 */
	public void setLoc_old(String value) {
		this.loc_old = value;
	}

	/**
	 * collocazione precedente
	 */
	public String getLoc_old() {
		return loc_old;
	}

	/**
	 * specificazione della collocazione precedente
	 */
	public void setSpec_old(String value) {
		this.spec_old = value;
	}

	/**
	 * specificazione della collocazione precedente
	 */
	public String getSpec_old() {
		return spec_old;
	}

	/**
	 * codice di tipo supporto
	 */
	public void setCd_supporto(String value) {
		this.cd_supporto = value;
	}

	/**
	 * codice di tipo supporto
	 */
	public String getCd_supporto() {
		return cd_supporto;
	}

	/**
	 * Utente che ha effettuato la prima collocazione dell'inventario
	 */
	public void setUte_ins_prima_coll(String value) {
		this.ute_ins_prima_coll = value;
	}

	/**
	 * Utente che ha effettuato la prima collocazione dell'inventario
	 */
	public String getUte_ins_prima_coll() {
		return ute_ins_prima_coll;
	}

	/**
	 * Timestamp di inserimento della prima collocazione
	 */
	public void setTs_ins_prima_coll(java.sql.Timestamp value) {
		this.ts_ins_prima_coll = value;
	}

	/**
	 * Timestamp di inserimento della prima collocazione
	 */
	public java.sql.Timestamp getTs_ins_prima_coll() {
		return ts_ins_prima_coll;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public void setUte_ins(String value) {
		this.ute_ins = value;
	}

	/**
	 * Utente che ha effettuato l'inserimento
	 */
	public String getUte_ins() {
		return ute_ins;
	}

	/**
	 * Timestamp di inserimento
	 */
	public void setTs_ins(java.sql.Timestamp value) {
		this.ts_ins = value;
	}

	/**
	 * Timestamp di inserimento
	 */
	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public void setUte_var(String value) {
		this.ute_var = value;
	}

	/**
	 * Utente che ha effettuato la variazione
	 */
	public String getUte_var() {
		return ute_var;
	}

	/**
	 * Timestamp di variazione
	 */
	public void setTs_var(java.sql.Timestamp value) {
		this.ts_var = value;
	}

	/**
	 * Timestamp di variazione
	 */
	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public void setFl_canc(char value) {
		this.fl_canc = value;
	}

	/**
	 * Flag di cancellazione logica
	 */
	public char getFl_canc() {
		return fl_canc;
	}



	/**
	 * Codice del tipo di acquisizione del materiale; se presente l'ordine deve essere coerente con tipo ordine
	 */
	public void setTipo_acquisizione(char value) {
		setTipo_acquisizione(new Character(value));
	}

	/**
	 * Codice del tipo di acquisizione del materiale; se presente l'ordine deve essere coerente con tipo ordine
	 */
	public void setTipo_acquisizione(Character value) {
		this.tipo_acquisizione = value;
	}

	/**
	 * Codice del tipo di acquisizione del materiale; se presente l'ordine deve essere coerente con tipo ordine
	 */
	public Character getTipo_acquisizione() {
		return tipo_acquisizione;
	}

	/**
	 * Codice del tipo di supporto utilizzato per la copia del documento.
	 */
	public void setSupporto_copia(String value) {
		this.supporto_copia = value;
	}

	/**
	 * Codice del tipo di supporto utilizzato per la copia del documento.
	 */
	public String getSupporto_copia() {
		return supporto_copia;
	}

	/**
	 * se diverso da null esprime la presenza di una digitalizzazione: 0=parziale, 1=completa, 2=born digital (l'orginale e' gia' digitale)
	 */
	public void setDigitalizzazione(char value) {
		setDigitalizzazione(new Character(value));
	}

	/**
	 * se diverso da null esprime la presenza di una digitalizzazione: 0=parziale, 1=completa, 2=born digital (l'orginale e' gia' digitale)
	 */
	public void setDigitalizzazione(Character value) {
		this.digitalizzazione = value;
	}

	/**
	 * se diverso da null esprime la presenza di una digitalizzazione: 0=parziale, 1=completa, 2=born digital (l'orginale e' gia' digitale)
	 */
	public Character getDigitalizzazione() {
		return digitalizzazione;
	}

	/**
	 * indica se e come e' accessibile la copia digitale; valorizzato in base ai contenuti dell'apposita tabella codici
	 */
	public void setDisp_copia_digitale(String value) {
		this.disp_copia_digitale = value;
	}

	/**
	 * indica se e come e' accessibile la copia digitale; valorizzato in base ai contenuti dell'apposita tabella codici
	 */
	public String getDisp_copia_digitale() {
		return disp_copia_digitale;
	}

	/**
	 * identificativo da fornire all''applicazione che gestisce la teca delle copie digitali
	 */
	public void setId_accesso_remoto(String value) {
		this.id_accesso_remoto = value;
	}

	/**
	 * identificativo da fornire all''applicazione che gestisce la teca delle copie digitali
	 */
	public String getId_accesso_remoto() {
		return id_accesso_remoto;
	}

	/**
	 * codice identificativo della teca digitale; valori in tabella codici
	 */
	public void setRif_teca_digitale(String value) {
		this.rif_teca_digitale = value;
	}

	/**
	 * codice identificativo della teca digitale; valori in tabella codici
	 */
	public String getRif_teca_digitale() {
		return rif_teca_digitale;
	}

	/**
	 * codice che descrive il gruppo di supporti per i quali e' ammesso servizio di riproduzione
	 */
	public void setCd_riproducibilita(String value) {
		this.cd_riproducibilita = value;
	}

	/**
	 * codice che descrive il gruppo di supporti per i quali e' ammesso servizio di riproduzione
	 */
	public String getCd_riproducibilita() {
		return cd_riproducibilita;
	}

	/**
	 * Data di ingresso in biblioteca
	 */
	public void setData_ingresso(java.util.Date value) {
		this.data_ingresso = value;
	}

	/**
	 * Data di ingresso in biblioteca
	 */
	public java.util.Date getData_ingresso() {
		return data_ingresso;
	}

	/**
	 * Data di redisponibilita' presunta
	 */
	public void setData_redisp_prev(java.util.Date value) {
		this.data_redisp_prev = value;
	}

	/**
	 * Data di redisponibilita' presunta
	 */
	public java.util.Date getData_redisp_prev() {
		return data_redisp_prev;
	}






	public void setCd_serie(it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale value) {
		this.cd_serie = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale getCd_serie() {
		return cd_serie;
	}

	public void setCd_proven(it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario value) {
		this.cd_proven = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario getCd_proven() {
		return cd_proven;
	}

	public void setKey_loc(it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione value) {
		this.key_loc = value;
	}

	public it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione getKey_loc() {
		return key_loc;
	}

	public void setB(it.iccu.sbn.polo.orm.bibliografica.Tb_titolo value) {
		this.b = value;
	}

	public it.iccu.sbn.polo.orm.bibliografica.Tb_titolo getB() {
		return b;
	}

	public void setTrc_poss_prov_inventari(java.util.Set value) {
		this.Trc_poss_prov_inventari = value;
	}

	public java.util.Set getTrc_poss_prov_inventari() {
		return Trc_poss_prov_inventari;
	}

	public void setTra_ordine_inventari(java.util.Set value) {
		this.Tra_ordine_inventari = value;
	}

	public java.util.Set getTra_ordine_inventari() {
		return Tra_ordine_inventari;
	}


	public void setTbc_nota_inv(java.util.Set value) {
		this.Tbc_nota_inv = value;
	}

	public java.util.Set getTbc_nota_inv() {
		return Tbc_nota_inv;
	}


	public void setTbl_richiesta_servizio(java.util.Set value) {
		this.Tbl_richiesta_servizio = value;
	}

	public java.util.Set getTbl_richiesta_servizio() {
		return Tbl_richiesta_servizio;
	}


	public String toString() {
		return String.valueOf(((getCd_serie() == null) ? "" : String.valueOf(getCd_serie().getCd_serie()) + " " + String.valueOf(getCd_serie().getCd_polo())) + " " + getCd_inven());
	}

	private boolean _saved = false;

	public void onSave() {
		_saved=true;
	}


	public void onLoad() {
		_saved=true;
	}


	public boolean isSaved() {
		return _saved;
	}

	public Integer getId_bib_scar() {
		return id_bib_scar;
	}

	public void setId_bib_scar(Integer id_bib_scar) {
		this.id_bib_scar = id_bib_scar;
	}

	public java.util.Set getTbp_esemplare_fascicolo() {
		return Tbp_esemplare_fascicolo;
	}

	public void setTbp_esemplare_fascicolo(java.util.Set tbp_esemplare_fascicolo) {
		Tbp_esemplare_fascicolo = tbp_esemplare_fascicolo;
	}


}
