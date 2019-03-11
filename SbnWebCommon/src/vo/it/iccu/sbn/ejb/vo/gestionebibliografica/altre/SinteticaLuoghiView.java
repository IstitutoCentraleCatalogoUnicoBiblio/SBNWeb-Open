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
package it.iccu.sbn.ejb.vo.gestionebibliografica.altre;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Comparator;
import java.util.regex.Pattern;

public class SinteticaLuoghiView  extends SerializableVO {

	// = SinteticaLuoghiView.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -8533547781519149993L;

	private int numLuoghi;

	private int progressivo;

	private String imageUrl;

	private String descrizioneLegami;

	private String livelloAutorita;

	private String parametri;

	private String tipoAutority;

	private String dataIns;

	private String lid;
	private String denominazione;
	private String forma;
	private boolean flagCondiviso;

	// Inizio modifica almaviva2 BUG MANTIS 4137 (Collaudo)- l'esamina su forme di rinvio deve essere fatto con la sua forma accettata
	private String lidAccettata;
	private String luogoAccettata;
	private String tipoLuogoAccettata;
	// Fine modifica almaviva2 BUG MANTIS 4137 (Collaudo)

	private Pattern pattern;

	private static final String HTML_NEW_LINE_REGEX = "&lt;.*?br.*?&gt;";
    private static final String HTML_NEW_LINE = "<br />";


	public SinteticaLuoghiView() {
		this.pattern = Pattern.compile(HTML_NEW_LINE_REGEX, Pattern.CASE_INSENSITIVE);
	}


	private String filter(String value)
    {
        if(value == null || value.length() == 0)
            return value;
        StringBuffer result = null;
        String filtered = null;
        for(int i = 0; i < value.length(); i++)
        {
            filtered = null;
            switch(value.charAt(i))
            {
            case 60: // '<'
                filtered = "&lt;";
                break;

            case 62: // '>'
                filtered = "&gt;";
                break;

            case 38: // '&'
                filtered = "&amp;";
                break;

            case 34: // '"'
                filtered = "&quot;";
                break;

            case 39: // '\''
                filtered = "&#39;";
                break;
            }
            if(result == null)
            {
                if(filtered != null)
                {
                    result = new StringBuffer(value.length() + 50);
                    if(i > 0)
                        result.append(value.substring(0, i));
                    result.append(filtered);
                }
            } else
            if(filtered == null)
                result.append(value.charAt(i));
            else
                result.append(filtered);
        }

        return result != null ? result.toString() : value;
    }

    private String htmlFilter(String value) {

    	String tmp = this.filter(value);
    	if (tmp != null)
    		tmp = pattern.matcher(tmp).replaceAll(HTML_NEW_LINE);
    	return tmp;
    }



    /**
     * Comparator that can be used for a case insensitive sort of
     * <code>LabelValueBean</code> objects.
     */
    public static final Comparator sortListaSinteticaLuo = new Comparator() {
    	public int compare(Object o1, Object o2) {
    		int myProgr1 = ((SinteticaLuoghiView) o1).getProgressivo();
    		int myProgr2 = ((SinteticaLuoghiView) o2).getProgressivo();
    		return myProgr1-myProgr2;
    	}
    };



	public String getParametri() {
		return parametri;
	}

	public void setParametri(String parametri) {
		this.parametri = parametri;
	}

	public String getDescrizioneLegami() {
		return descrizioneLegami;
	}

	public void setDescrizioneLegami(String descrizioniLegami) {
		this.descrizioneLegami = this.htmlFilter(descrizioniLegami);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getTipoAutority() {
		return tipoAutority;
	}

	public void setTipoAutority(String tipoAutority) {
		this.tipoAutority = tipoAutority;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}


	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}



	public int getNumLuoghi() {
		return numLuoghi;
	}


	public void setNumLuoghi(int numLuoghi) {
		this.numLuoghi = numLuoghi;
	}


	public String getLid() {
		return lid;
	}


	public void setLid(String lid) {
		this.lid = lid;
	}


	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}


	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}


	public String getLidAccettata() {
		return lidAccettata;
	}


	public void setLidAccettata(String lidAccettata) {
		this.lidAccettata = lidAccettata;
	}


	public String getLuogoAccettata() {
		return luogoAccettata;
	}


	public void setLuogoAccettata(String luogoAccettata) {
		this.luogoAccettata = luogoAccettata;
	}


	public String getTipoLuogoAccettata() {
		return tipoLuogoAccettata;
	}


	public void setTipoLuogoAccettata(String tipoLuogoAccettata) {
		this.tipoLuogoAccettata = tipoLuogoAccettata;
	}


	public boolean isRinvio() {
		return ValidazioneDati.equalsIgnoreCase(forma, "R");
	}

	public boolean isAccettata() {
		return ValidazioneDati.equalsIgnoreCase(forma, "A");
	}




}
