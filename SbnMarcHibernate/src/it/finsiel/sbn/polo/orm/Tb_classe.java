/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: antoniospatera@libero.it
 * License Type: Evaluation
 */
package it.finsiel.sbn.polo.orm;


/**
 * ORM-Persistable Class
 */
public class Tb_classe extends OggettoServerSbnMarc {

	private static final long serialVersionUID = 7247791460144628843L;

	public boolean equals(Object aObj) {
		if (aObj == null)
			return false;
		if (!(aObj instanceof Tb_classe))
			return false;
		Tb_classe tb_classe = (Tb_classe) aObj;
		if (getCD_SISTEMA() != tb_classe.getCD_SISTEMA())
			return false;
		if (getCD_EDIZIONE() != tb_classe.getCD_EDIZIONE())
			return false;
		if (getCLASSE() != null && !getCLASSE().equals(tb_classe.getCLASSE()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = hashcode + getCD_SISTEMA().hashCode();
		hashcode = hashcode + getCD_EDIZIONE().hashCode();
		hashcode = hashcode + getCLASSE().hashCode();
		return hashcode;
	}

	protected String CD_SISTEMA;
	protected String CD_EDIZIONE;
	protected String CLASSE;
	protected String CD_LIVELLO;
	protected String FL_COSTRUITO;
	protected String FL_SPECIALE;
	protected String DS_CLASSE;
	protected String KY_CLASSE_ORD;
	protected String SUFFISSO;
	protected String ULT_TERM;
	protected String FL_CONDIVISO;
	protected String UTE_CONDIVISO;
	protected java.util.Date TS_CONDIVISO;

	public String getFL_CONDIVISO() {
		return FL_CONDIVISO;
	}

	public java.util.Date getTS_CONDIVISO() {
		return TS_CONDIVISO;
	}

	public String getUTE_CONDIVISO() {
		return UTE_CONDIVISO;
	}

	public void setFL_CONDIVISO(String value) {
		this.FL_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXfl_condiviso, value);
	}

	public void setTS_CONDIVISO(java.util.Date value) {
		this.TS_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXts_condiviso, value);
	}

	public void setUTE_CONDIVISO(String value) {
		this.UTE_CONDIVISO = value;
		this.settaParametro(KeyParameter.XXXute_condiviso, value);
	}

	public String getKY_CLASSE_ORD() {
		return KY_CLASSE_ORD;
	}

	public void setKY_CLASSE_ORD(String value) {
		this.KY_CLASSE_ORD = value;
		this.settaParametro(KeyParameter.XXXky_classe_ord, value);
	}

	public String getSUFFISSO() {
		return SUFFISSO;
	}

	public void setSUFFISSO(String value) {
		this.SUFFISSO = value;
		this.settaParametro(KeyParameter.XXXsuffisso, value);
	}

	public String getULT_TERM() {
		return ULT_TERM;
	}

	public void setULT_TERM(String value) {
		this.ULT_TERM = value;
		this.settaParametro(KeyParameter.XXXult_term, value);
	}

	public void setCD_SISTEMA(String value) {
		this.CD_SISTEMA = value;
		this.settaParametro(KeyParameter.XXXcd_sistema, value);
	}

	public String getCD_SISTEMA() {
		return CD_SISTEMA;
	}

	public void setCD_EDIZIONE(String value) {
		this.CD_EDIZIONE = value;
		this.settaParametro(KeyParameter.XXXcd_edizione, value);
	}

	public String getCD_EDIZIONE() {
		return CD_EDIZIONE;
	}

	public void setCLASSE(String value) {
		this.CLASSE = value;
		this.settaParametro(KeyParameter.XXXclasse, value);
	}

	public String getCLASSE() {
		return CLASSE;
	}

	public void setCD_LIVELLO(String value) {
		this.CD_LIVELLO = value;
		this.settaParametro(KeyParameter.XXXcd_livello, value);
	}

	public String getCD_LIVELLO() {
		return CD_LIVELLO;
	}

	public void setFL_COSTRUITO(String value) {
		this.FL_COSTRUITO = value;
		this.settaParametro(KeyParameter.XXXfl_costruito, value);
	}

	public String getFL_COSTRUITO() {
		return FL_COSTRUITO;
	}

	public void setFL_SPECIALE(String value) {
		this.FL_SPECIALE = value;
		this.settaParametro(KeyParameter.XXXfl_speciale, value);
	}

	public String getFL_SPECIALE() {
		return FL_SPECIALE;
	}

	public void setDS_CLASSE(String value) {
		this.DS_CLASSE = value;
		this.settaParametro(KeyParameter.XXXds_classe, value);
	}

	public String getDS_CLASSE() {
		return DS_CLASSE;
	}

	public String toString() {
		return String.valueOf(getCD_SISTEMA() + " " + getCD_EDIZIONE() + " "
				+ getCLASSE());
	}

}
