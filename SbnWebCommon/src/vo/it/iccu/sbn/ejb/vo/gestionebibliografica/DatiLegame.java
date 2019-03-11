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
 * @author UEPA0025
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatiLegame  extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 330078005773598295L;
	// = DatiLegame.class.hashCode();
    /** DOCUMENT ME! */

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////// TIPO DI LEGAME //////////////////////////////////////////
    /////////////////////  LEGAME PADRE - FIGLIO ///////////////////////////////////
    public static final int LEGAME_NULL = 0;
    public static final int LEGAME_DOCUMENTO_AUTORE = 1;
    public static final int LEGAME_DOCUMENTO_MARCA = 2;
    public static final int LEGAME_AUTORE_AUTORE = 4;
    public static final int LEGAME_AUTORE_MARCA = 5;
    public static final int LEGAME_MARCA_AUTORE = 6;
    public static final int LEGAME_DOCUMENTO_DOCUMENTO = 7;
    public static final int LEGAME_DOCUMENTO_TITOLO_UNIFORME = 8;
    public static final int LEGAME_DOCUMENTO_TITACCESSO = 9;
    public static final int LEGAME_DOCUMENTO_SOGGETTO = 10;
    public static final int LEGAME_DOCUMENTO_LUOGO = 11;
    public static final int LEGAME_DOCUMENTO_CLASSE = 12;
    public static final int LEGAME_LUOGO_LUOGO = 13;
    public static final int LEGAME_DOCUMENTO_THESAURO = 14;

    /** DOCUMENT ME! */
    private String natura;

    /** DOCUMENT ME! */
    private String sequenzaMusica;

    /** DOCUMENT ME! */
    private String sottoTipoLegame;

    /** DOCUMENT ME! */
    private String sici;

    /** DOCUMENT ME! */
    private String relatorCode;

    // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
	// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
	// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
	//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
    private String specStrumVoci;

    /** DOCUMENT ME! */
    private String tipoLegame;

    /** DOCUMENT ME! */
    private String notaLegame;

    /** DOCUMENT ME! */
    private String sequenza;

    /** DOCUMENT ME! */
    private String responsabilita;

    /** DOCUMENT ME! */
    private boolean incerto;

    /** DOCUMENT ME! */
    private boolean superfluo;

    /** DOCUMENT ME! */
    private boolean flagCondiviso;

    /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
    public String getNotaLegame() {
        return notaLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getResponsabilita() {
        return responsabilita;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSequenza() {
        return sequenza;
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
     * @param string DOCUMENT ME!
     */
    public void setNotaLegame(String string) {
        notaLegame = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setResponsabilita(String string) {
        responsabilita = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setSequenza(String string) {
        sequenza = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setTipoLegame(String string) {
        tipoLegame = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isIncerto() {
        return incerto;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setIncerto(boolean b) {
        incerto = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRelatorCode() {
        return relatorCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSequenzaMusica() {
        return sequenzaMusica;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSici() {
        return sici;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSottoTipoLegame() {
        return sottoTipoLegame;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setRelatorCode(String string) {
        relatorCode = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setSequenzaMusica(String string) {
        sequenzaMusica = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setSici(String string) {
        sici = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setSottoTipoLegame(String string) {
        sottoTipoLegame = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSuperfluo() {
        return superfluo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setSuperfluo(boolean b) {
        superfluo = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param natura DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTitAccesso(String natura) {
        return ((natura.equals("B")) || (natura.equals("D")) ||
        (natura.equals("P")) || (natura.equals("T"))) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param natura DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTitoloUniforme(String natura) {
        return (natura.equals("A")) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param natura DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDocumento(String natura) {
        return ((!this.isTitAccesso(natura)) &&
        (!this.isTitoloUniforme(natura))) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNatura() {
        return natura;
    }

    /**
     * @param string
     */
    public void setNatura(String string) {
        natura = string;
    }

	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}

	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}

	public String getSpecStrumVoci() {
		return specStrumVoci;
	}

	public void setSpecStrumVoci(String specStrumVoci) {
		this.specStrumVoci = specStrumVoci;
	}
}
