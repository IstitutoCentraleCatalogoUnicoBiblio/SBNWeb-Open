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
package it.iccu.sbn.polo.orm.bibliografica;

import java.io.Serializable;
/**
 * OGGETTI BIBLIOGRAFICI
 */
/**
 * ORM-Persistable Class
 */
public class Tb_titolo implements Serializable {

	private static final long serialVersionUID = -7959607072948095990L;

	private String bid;

	private String isadn;

	private char tp_materiale;

	private Character tp_record_uni;

	private char cd_natura;

	private String cd_paese;

	private String cd_lingua_1;

	private String cd_lingua_2;

	private String cd_lingua_3;

	private String aa_pubb_1;

	private String aa_pubb_2;

	private Character tp_aa_pubb;

	private String cd_genere_1;

	private String cd_genere_2;

	private String cd_genere_3;

	private String cd_genere_4;

	private String ky_cles1_t;

	private String ky_cles2_t;

	private String ky_clet1_t;

	private String ky_clet2_t;

	private String ky_cles1_ct;

	private String ky_cles2_ct;

	private String ky_clet1_ct;

	private String ky_clet2_ct;

	private String cd_livello;

	private char fl_speciale;

	private String isbd;

	private String indice_isbd;

	private String ky_editore;

	private String cd_agenzia;

	private String cd_norme_cat;

	private String nota_inf_tit;

	private String nota_cat_tit;

	private String bid_link;

	private Character tp_link;

	private String ute_ins;

	private java.sql.Timestamp ts_ins;

	private String ute_var;

	private java.sql.Timestamp ts_var;

	private String ute_forza_ins;

	private String ute_forza_var;

	private char fl_canc;

	private Character cd_periodicita;

	private char fl_condiviso;

	private String ute_condiviso;

	private java.sql.Timestamp ts_condiviso;

	private java.util.Set Tbc_collocazione = new java.util.HashSet();

	private java.util.Set Tbc_esemplare_titolo = new java.util.HashSet();

	private java.util.Set Tbc_inventario = new java.util.HashSet();

	private java.util.Set Tbp_fascicolo = new java.util.HashSet();

	private java.util.Set Tb_abstract = new java.util.HashSet();

	private java.util.Set Tr_tit_cla = new java.util.HashSet();

	private java.util.Set Tr_tit_sog_bib = new java.util.HashSet();

	private java.util.Set Trs_termini_titoli_biblioteche = new java.util.HashSet();

	private java.util.Set Tb_arte_tridimens = new java.util.HashSet();

	private java.util.Set Tb_audiovideo = new java.util.HashSet();

	private java.util.Set Tb_cartografia = new java.util.HashSet();

	private java.util.Set Tb_composizione = new java.util.HashSet();

	private java.util.Set Tb_disco_sonoro = new java.util.HashSet();

	private java.util.Set Tb_grafica = new java.util.HashSet();

	private java.util.Set Tb_impronta = new java.util.HashSet();

	private java.util.Set Tb_incipit = new java.util.HashSet();

	private java.util.Set Tb_microforma = new java.util.HashSet();

	private java.util.Set Tb_musica = new java.util.HashSet();

	private java.util.Set Tb_nota = new java.util.HashSet();

	private java.util.Set Tb_numero_std = new java.util.HashSet();

	private java.util.Set Tb_personaggio = new java.util.HashSet();

	private java.util.Set Tb_rappresent = new java.util.HashSet();

	private java.util.Set Tb_risorsa_elettr = new java.util.HashSet();

	private java.util.Set Tr_rep_tit = new java.util.HashSet();

	private java.util.Set Tr_tit_aut = new java.util.HashSet();

	private java.util.Set Tr_tit_bib = new java.util.HashSet();

	private java.util.Set Tr_tit_luo = new java.util.HashSet();

	private java.util.Set Tr_tit_mar = new java.util.HashSet();

	private java.util.Set Tr_tit_tit = new java.util.HashSet();

	private java.util.Set Tr_tit_tit1 = new java.util.HashSet();

	public void setBid(String value) {
		this.bid = value;
	}

	public String getBid() {
		return bid;
	}

	public String getORMID() {
		return getBid();
	}

	public void setIsadn(String value) {
		this.isadn = value;
	}

	public String getIsadn() {
		return isadn;
	}

	public void setTp_materiale(char value) {
		this.tp_materiale = value;
	}

	public char getTp_materiale() {
		return tp_materiale;
	}

	public void setTp_record_uni(char value) {
		setTp_record_uni(new Character(value));
	}

	public void setTp_record_uni(Character value) {
		this.tp_record_uni = value;
	}

	public Character getTp_record_uni() {
		return tp_record_uni;
	}

	public void setCd_natura(char value) {
		this.cd_natura = value;
	}

	public char getCd_natura() {
		return cd_natura;
	}

	public void setCd_paese(String value) {
		this.cd_paese = value;
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setCd_lingua_1(String value) {
		this.cd_lingua_1 = value;
	}

	public String getCd_lingua_1() {
		return cd_lingua_1;
	}

	public void setCd_lingua_2(String value) {
		this.cd_lingua_2 = value;
	}

	public String getCd_lingua_2() {
		return cd_lingua_2;
	}

	public void setCd_lingua_3(String value) {
		this.cd_lingua_3 = value;
	}

	public String getCd_lingua_3() {
		return cd_lingua_3;
	}

	public void setAa_pubb_1(String value) {
		this.aa_pubb_1 = value;
	}

	public String getAa_pubb_1() {
		return aa_pubb_1;
	}

	public void setAa_pubb_2(String value) {
		this.aa_pubb_2 = value;
	}

	public String getAa_pubb_2() {
		return aa_pubb_2;
	}

	public void setTp_aa_pubb(char value) {
		setTp_aa_pubb(new Character(value));
	}

	public void setTp_aa_pubb(Character value) {
		this.tp_aa_pubb = value;
	}

	public Character getTp_aa_pubb() {
		return tp_aa_pubb;
	}

	public void setCd_genere_1(String value) {
		this.cd_genere_1 = value;
	}

	public String getCd_genere_1() {
		return cd_genere_1;
	}

	public void setCd_genere_2(String value) {
		this.cd_genere_2 = value;
	}

	public String getCd_genere_2() {
		return cd_genere_2;
	}

	public void setCd_genere_3(String value) {
		this.cd_genere_3 = value;
	}

	public String getCd_genere_3() {
		return cd_genere_3;
	}

	public void setCd_genere_4(String value) {
		this.cd_genere_4 = value;
	}

	public String getCd_genere_4() {
		return cd_genere_4;
	}

	public void setKy_cles1_t(String value) {
		this.ky_cles1_t = value;
	}

	public String getKy_cles1_t() {
		return ky_cles1_t;
	}

	public void setKy_cles2_t(String value) {
		this.ky_cles2_t = value;
	}

	public String getKy_cles2_t() {
		return ky_cles2_t;
	}

	public void setKy_clet1_t(String value) {
		this.ky_clet1_t = value;
	}

	public String getKy_clet1_t() {
		return ky_clet1_t;
	}

	public void setKy_clet2_t(String value) {
		this.ky_clet2_t = value;
	}

	public String getKy_clet2_t() {
		return ky_clet2_t;
	}

	public void setKy_cles1_ct(String value) {
		this.ky_cles1_ct = value;
	}

	public String getKy_cles1_ct() {
		return ky_cles1_ct;
	}

	public void setKy_cles2_ct(String value) {
		this.ky_cles2_ct = value;
	}

	public String getKy_cles2_ct() {
		return ky_cles2_ct;
	}

	public void setKy_clet1_ct(String value) {
		this.ky_clet1_ct = value;
	}

	public String getKy_clet1_ct() {
		return ky_clet1_ct;
	}

	public void setKy_clet2_ct(String value) {
		this.ky_clet2_ct = value;
	}

	public String getKy_clet2_ct() {
		return ky_clet2_ct;
	}

	public void setCd_livello(String value) {
		this.cd_livello = value;
	}

	public String getCd_livello() {
		return cd_livello;
	}

	public void setFl_speciale(char value) {
		this.fl_speciale = value;
	}

	public char getFl_speciale() {
		return fl_speciale;
	}

	public void setIsbd(String value) {
		this.isbd = value;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIndice_isbd(String value) {
		this.indice_isbd = value;
	}

	public String getIndice_isbd() {
		return indice_isbd;
	}

	public void setKy_editore(String value) {
		this.ky_editore = value;
	}

	public String getKy_editore() {
		return ky_editore;
	}

	public void setCd_agenzia(String value) {
		this.cd_agenzia = value;
	}

	public String getCd_agenzia() {
		return cd_agenzia;
	}

	public void setCd_norme_cat(String value) {
		this.cd_norme_cat = value;
	}

	public String getCd_norme_cat() {
		return cd_norme_cat;
	}

	public void setNota_inf_tit(String value) {
		this.nota_inf_tit = value;
	}

	public String getNota_inf_tit() {
		return nota_inf_tit;
	}

	public void setNota_cat_tit(String value) {
		this.nota_cat_tit = value;
	}

	public String getNota_cat_tit() {
		return nota_cat_tit;
	}

	public void setBid_link(String value) {
		this.bid_link = value;
	}

	public String getBid_link() {
		return bid_link;
	}

	public void setTp_link(char value) {
		setTp_link(new Character(value));
	}

	public void setTp_link(Character value) {
		this.tp_link = value;
	}

	public Character getTp_link() {
		return tp_link;
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

	public void setUte_forza_ins(String value) {
		this.ute_forza_ins = value;
	}

	public String getUte_forza_ins() {
		return ute_forza_ins;
	}

	public void setUte_forza_var(String value) {
		this.ute_forza_var = value;
	}

	public String getUte_forza_var() {
		return ute_forza_var;
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
	 * Codice di periodicità' (solo per nature S)
	 */
	public void setCd_periodicita(char value) {
		setCd_periodicita(new Character(value));
	}

	/**
	 * Codice di periodicità' (solo per nature S)
	 */
	public void setCd_periodicita(Character value) {
		this.cd_periodicita = value;
	}

	/**
	 * Codice di periodicità' (solo per nature S)
	 */
	public Character getCd_periodicita() {
		return cd_periodicita;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public void setFl_condiviso(char value) {
		this.fl_condiviso = value;
	}

	/**
	 * Flag di condivisione gestione del legame con indice
	 */
	public char getFl_condiviso() {
		return fl_condiviso;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public void setUte_condiviso(String value) {
		this.ute_condiviso = value;
	}

	/**
	 * Timestamp di condivisione gestione del legame con indice
	 */
	public String getUte_condiviso() {
		return ute_condiviso;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public void setTs_condiviso(java.sql.Timestamp value) {
		this.ts_condiviso = value;
	}

	/**
	 * Utente che ha attivato la gestione condivisa del legame con indice
	 */
	public java.sql.Timestamp getTs_condiviso() {
		return ts_condiviso;
	}

	public void setTbc_collocazione(java.util.Set value) {
		this.Tbc_collocazione = value;
	}

	public java.util.Set getTbc_collocazione() {
		return Tbc_collocazione;
	}


	public void setTbc_esemplare_titolo(java.util.Set value) {
		this.Tbc_esemplare_titolo = value;
	}

	public java.util.Set getTbc_esemplare_titolo() {
		return Tbc_esemplare_titolo;
	}


	public void setTbc_inventario(java.util.Set value) {
		this.Tbc_inventario = value;
	}

	public java.util.Set getTbc_inventario() {
		return Tbc_inventario;
	}

	public void setTb_abstract(java.util.Set value) {
		this.Tb_abstract = value;
	}

	public java.util.Set getTb_abstract() {
		return Tb_abstract;
	}


	public void setTr_tit_cla(java.util.Set value) {
		this.Tr_tit_cla = value;
	}

	public java.util.Set getTr_tit_cla() {
		return Tr_tit_cla;
	}


	public void setTr_tit_sog_bib(java.util.Set value) {
		this.Tr_tit_sog_bib = value;
	}

	public java.util.Set getTr_tit_sog_bib() {
		return Tr_tit_sog_bib;
	}


	public void setTrs_termini_titoli_biblioteche(java.util.Set value) {
		this.Trs_termini_titoli_biblioteche = value;
	}

	public java.util.Set getTrs_termini_titoli_biblioteche() {
		return Trs_termini_titoli_biblioteche;
	}


	public void setTb_arte_tridimens(java.util.Set value) {
		this.Tb_arte_tridimens = value;
	}

	public java.util.Set getTb_arte_tridimens() {
		return Tb_arte_tridimens;
	}


	public void setTb_audiovideo(java.util.Set value) {
		this.Tb_audiovideo = value;
	}

	public java.util.Set getTb_audiovideo() {
		return Tb_audiovideo;
	}


	public void setTb_cartografia(java.util.Set value) {
		this.Tb_cartografia = value;
	}

	public java.util.Set getTb_cartografia() {
		return Tb_cartografia;
	}


	public void setTb_composizione(java.util.Set value) {
		this.Tb_composizione = value;
	}

	public java.util.Set getTb_composizione() {
		return Tb_composizione;
	}


	public void setTb_disco_sonoro(java.util.Set value) {
		this.Tb_disco_sonoro = value;
	}

	public java.util.Set getTb_disco_sonoro() {
		return Tb_disco_sonoro;
	}


	public void setTb_grafica(java.util.Set value) {
		this.Tb_grafica = value;
	}

	public java.util.Set getTb_grafica() {
		return Tb_grafica;
	}


	public void setTb_impronta(java.util.Set value) {
		this.Tb_impronta = value;
	}

	public java.util.Set getTb_impronta() {
		return Tb_impronta;
	}


	public void setTb_incipit(java.util.Set value) {
		this.Tb_incipit = value;
	}

	public java.util.Set getTb_incipit() {
		return Tb_incipit;
	}


	public void setTb_microforma(java.util.Set value) {
		this.Tb_microforma = value;
	}

	public java.util.Set getTb_microforma() {
		return Tb_microforma;
	}


	public void setTb_musica(java.util.Set value) {
		this.Tb_musica = value;
	}

	public java.util.Set getTb_musica() {
		return Tb_musica;
	}


	public void setTb_nota(java.util.Set value) {
		this.Tb_nota = value;
	}

	public java.util.Set getTb_nota() {
		return Tb_nota;
	}


	public void setTb_numero_std(java.util.Set value) {
		this.Tb_numero_std = value;
	}

	public java.util.Set getTb_numero_std() {
		return Tb_numero_std;
	}


	public void setTb_personaggio(java.util.Set value) {
		this.Tb_personaggio = value;
	}

	public java.util.Set getTb_personaggio() {
		return Tb_personaggio;
	}


	public void setTb_rappresent(java.util.Set value) {
		this.Tb_rappresent = value;
	}

	public java.util.Set getTb_rappresent() {
		return Tb_rappresent;
	}


	public void setTb_risorsa_elettr(java.util.Set value) {
		this.Tb_risorsa_elettr = value;
	}

	public java.util.Set getTb_risorsa_elettr() {
		return Tb_risorsa_elettr;
	}


	public void setTr_rep_tit(java.util.Set value) {
		this.Tr_rep_tit = value;
	}

	public java.util.Set getTr_rep_tit() {
		return Tr_rep_tit;
	}


	public void setTr_tit_aut(java.util.Set value) {
		this.Tr_tit_aut = value;
	}

	public java.util.Set getTr_tit_aut() {
		return Tr_tit_aut;
	}


	public void setTr_tit_bib(java.util.Set value) {
		this.Tr_tit_bib = value;
	}

	public java.util.Set getTr_tit_bib() {
		return Tr_tit_bib;
	}


	public void setTr_tit_luo(java.util.Set value) {
		this.Tr_tit_luo = value;
	}

	public java.util.Set getTr_tit_luo() {
		return Tr_tit_luo;
	}


	public void setTr_tit_mar(java.util.Set value) {
		this.Tr_tit_mar = value;
	}

	public java.util.Set getTr_tit_mar() {
		return Tr_tit_mar;
	}


	public void setTr_tit_tit(java.util.Set value) {
		this.Tr_tit_tit = value;
	}

	public java.util.Set getTr_tit_tit() {
		return Tr_tit_tit;
	}


	public void setTr_tit_tit1(java.util.Set value) {
		this.Tr_tit_tit1 = value;
	}

	public java.util.Set getTr_tit_tit1() {
		return Tr_tit_tit1;
	}


	public String toString() {
		return String.valueOf(getBid());
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

	public java.util.Set getTbp_fascicolo() {
		return Tbp_fascicolo;
	}

	public void setTbp_fascicolo(java.util.Set tbp_fascicolo) {
		Tbp_fascicolo = tbp_fascicolo;
	}


}
