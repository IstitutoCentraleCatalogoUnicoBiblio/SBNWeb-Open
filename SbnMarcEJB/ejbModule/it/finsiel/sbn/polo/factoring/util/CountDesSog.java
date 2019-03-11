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
package it.finsiel.sbn.polo.factoring.util;

import java.io.Serializable;
/**
 * ORM-Persistable Class
 */
public class CountDesSog implements Serializable {



    // DOVE pos 1 = Descrizione Forma Accettata
    // DOVE pos 2 = Descrizione Forma Rinvio
    // DOVE pos 3 = Totale Soggetti Legati
    // DOVE pos 4 = Totale descrittori in des des
    // DOVE pos 5 = Did Accettato
    // DOVE pos 6 = Did Rinvio


	private static final long serialVersionUID = 4886047264326920131L;

	private String Des_accettato;

	private String Des_rinvio;

	private int Sog_count;

	private int Des_Des_count;

	private String Did_accettato;

	private String Did_rinvio;

	public String getDes_accettato() {
		return Des_accettato;
	}

	public void setDes_accettato(String des_accettato) {
		Des_accettato = des_accettato;
	}

	public String getDes_rinvio() {
		return Des_rinvio;
	}

	public void setDes_rinvio(String des_rinvio) {
		Des_rinvio = des_rinvio;
	}

	public int getSog_count() {
		return Sog_count;
	}

	public void setSog_count(int sog_count) {
		Sog_count = sog_count;
	}

	public int getDes_Des_count() {
		return Des_Des_count;
	}

	public void setDes_Des_count(int des_Des_count) {
		Des_Des_count = des_Des_count;
	}

	public String getDid_accettato() {
		return Did_accettato;
	}

	public void setDid_accettato(String did_accettato) {
		Did_accettato = did_accettato;
	}

	public String getDid_rinvio() {
		return Did_rinvio;
	}

	public void setDid_rinvio(String did_rinvio) {
		Did_rinvio = did_rinvio;
	}


}
