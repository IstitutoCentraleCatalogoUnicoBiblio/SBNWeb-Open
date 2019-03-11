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
public class CountDesDes implements Serializable {



    // DOVE pos 1 = Parola Descrittore
    // DOVE pos 2 = Numero di descrittori contenente la parola


	private static final long serialVersionUID = 9194517165512147173L;

	private String Parole_des;

	private int Des_count;

	public int getDes_count() {
		return Des_count;
	}

	public void setDes_count(int des_count) {
		Des_count = des_count;
	}

	public String getParole_des() {
		return Parole_des;
	}

	public void setParole_des(String parole_des) {
		Parole_des = parole_des;
	}



}
