/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.finsiel.sbn.polo.exception.util;

import java.io.Serializable;

public class Errore implements Serializable {

	private static final long serialVersionUID = 3445445959322864309L;

	private Integer id;
    private String descrizione;
    private String nome_referente;
    private String mail_referente;

	public Errore(Integer id, String descrizione, String nome_referente, String mail_referente) {
		this.id = id;
		this.descrizione = descrizione;
		this.nome_referente = nome_referente;
		this.mail_referente = mail_referente;
	}

    /**
     * Returns the descrizione.
     * @return String
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Returns the id.
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the mail_referente.
     * @return String
     */
    public String getMail_referente() {
        return mail_referente;
    }

    /**
     * Returns the nome_referente.
     * @return String
     */
    public String getNome_referente() {
        return nome_referente;
    }

    /**
     * Sets the descrizione.
     * @param descrizione The descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Sets the id.
     * @param id The id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Sets the mail_referente.
     * @param mail_referente The mail_referente to set
     */
    public void setMail_referente(String mail_referente) {
        this.mail_referente = mail_referente;
    }

    /**
     * Sets the nome_referente.
     * @param nome_referente The nome_referente to set
     */
    public void setNome_referente(String nome_referente) {
        this.nome_referente = nome_referente;
    }

}
