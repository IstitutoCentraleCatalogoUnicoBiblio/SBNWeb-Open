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
package it.finsiel.sbn.polo.orm.amministrazione;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class Trf_utente_professionale_biblioteca implements Serializable {

	private static final long serialVersionUID = 6377384578965194351L;

	public Trf_utente_professionale_biblioteca() {
	}

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof Trf_utente_professionale_biblioteca))
			return false;
		Trf_utente_professionale_biblioteca trf_utente_professionale_biblioteca = (Trf_utente_professionale_biblioteca)aObj;
		if (getId_utente_professionale() == null && trf_utente_professionale_biblioteca.getId_utente_professionale() != null)
			return false;
		if (!getId_utente_professionale().equals(trf_utente_professionale_biblioteca.getId_utente_professionale()))
			return false;
		if (getCd_polo() == null && trf_utente_professionale_biblioteca.getCd_polo() != null)
			return false;
		if (!getCd_polo().equals(trf_utente_professionale_biblioteca.getCd_polo()))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getId_utente_professionale() != null) {
			hashcode = hashcode + getId_utente_professionale().getORMID();
		}
		if (getCd_polo() != null) {
			hashcode = hashcode + (getCd_polo().getCd_biblioteca() == null ? 0 : getCd_polo().getCd_biblioteca().hashCode());
			hashcode = hashcode + (getCd_polo().getCd_polo() == null ? 0 : getCd_polo().getCd_polo().hashCode());
		}
		return hashcode;
	}

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali id_utente_professionale;

	private it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo cd_polo;

	private String tp_ruolo;

	private String note_competenze;

	private String ufficio_appart;

	/**
	 * null = dipendente della biblioteca
	 * admin =
	 * dipendente della biblioteca
	 * con diritti di amministratore semplice
	 * super Admin =
	 * dipendente della biblioteca
	 * con diritti
	 * amministratore con tutti i privilegi
	 */
	public void setTp_ruolo(String value) {
		this.tp_ruolo = value;
	}

	/**
	 * null = dipendente della biblioteca
	 * admin =
	 * dipendente della biblioteca
	 * con diritti di amministratore semplice
	 * super Admin =
	 * dipendente della biblioteca
	 * con diritti
	 * amministratore con tutti i privilegi
	 */
	public String getTp_ruolo() {
		return tp_ruolo;
	}

	public void setNote_competenze(String value) {
		this.note_competenze = value;
	}

	public String getNote_competenze() {
		return note_competenze;
	}

	public void setUfficio_appart(String value) {
		this.ufficio_appart = value;
	}

	public String getUfficio_appart() {
		return ufficio_appart;
	}

	public void setCd_polo(it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo value) {
		this.cd_polo = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo getCd_polo() {
		return cd_polo;
	}

	public void setId_utente_professionale(it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali value) {
		this.id_utente_professionale = value;
	}

	public it.finsiel.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali getId_utente_professionale() {
		return id_utente_professionale;
	}

	public String toString() {
		return String.valueOf(((getId_utente_professionale() == null) ? "" : String.valueOf(getId_utente_professionale().getORMID())) + " " + ((getCd_polo() == null) ? "" : String.valueOf(getCd_polo().getCd_biblioteca()) + " " + String.valueOf(getCd_polo().getCd_polo())));
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


}
