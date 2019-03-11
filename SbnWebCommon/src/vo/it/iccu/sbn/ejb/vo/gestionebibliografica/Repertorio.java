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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;


/**
 * <p>Title: Interfaccia in diretta</p>
 * <p>Description: Interfaccia web per il sistema bibliotecario nazionale</p>
 * <p>Repertorio legato all'autore.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Finsiel</p>
 * @author Finsiel
 * @version 1.0
 */
public class Repertorio  extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -148292453773201372L;

	// = Repertorio.class.hashCode();
    /** DOCUMENT ME! */
    private String tipoLegame;

    /** DOCUMENT ME! */
    private String codiceRepertorio;

    /** DOCUMENT ME! */
    private String notaAlLegame;

    /** DOCUMENT ME! */
    private String descrizione;

    /** DOCUMENT ME! */
    private int citazione;

    /**
     * Creates a new Repertorio object.
     */
    public Repertorio(String tipoLegame, String codiceRepertorio,
        String notaAlLegame) {
        this.tipoLegame           = tipoLegame;
        this.codiceRepertorio     = codiceRepertorio;
        this.notaAlLegame         = notaAlLegame;
    }

    /**
     * Creates a new Repertorio object.
     */
    public Repertorio(String tipoLegame, String codiceRepertorio,
        int citazione, String notaAlLegame) {
        this.tipoLegame           = tipoLegame;
        this.codiceRepertorio     = codiceRepertorio;
        this.citazione            = citazione;
        this.notaAlLegame         = notaAlLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTipoLegame() {
        return tipoLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEsito() {
        return tipoLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCodiceRepertorio() {
        return codiceRepertorio;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCitazione() {
        return citazione;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public String getNotaAlLegame() {
        return notaAlLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setNotaAlLegame(String text) {
        notaAlLegame = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setTipoLegame(String text) {
        tipoLegame = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setCitazione(int number) {
        citazione = number;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setDescrizione(String text) {
        descrizione = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setCodiceRepertorio(String text) {
        codiceRepertorio = text;
    }
}
