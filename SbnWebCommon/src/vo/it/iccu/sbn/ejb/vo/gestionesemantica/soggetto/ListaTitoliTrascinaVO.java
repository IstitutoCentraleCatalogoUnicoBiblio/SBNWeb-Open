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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;

import java.util.Comparator;
import java.util.regex.Pattern;


public class ListaTitoliTrascinaVO extends SerializableVO {

	private static final long serialVersionUID = -3609994152676432610L;

	private static final String HTML_NEW_LINE_REGEX = "&lt;.*?br.*?&gt;";
	private static final String HTML_NEW_LINE = "<br />";
	private static final Pattern pattern = Pattern.compile(HTML_NEW_LINE_REGEX, Pattern.CASE_INSENSITIVE);
	private int progr;
	private String bid;
	private String stato;
	private String utilizzato;
	private String isbdELegami;
	private boolean localizzato;

	private String selezBox;

	public ListaTitoliTrascinaVO() {
		super();
	}

	public ListaTitoliTrascinaVO(SinteticaTitoliView titolo) {
		super();

		this.progr = titolo.getProgressivo();
		this.bid = titolo.getBid();
		this.isbdELegami =titolo.getDescrizioneLegami();
		this.stato = titolo.getLivelloAutorita();
		this.localizzato = titolo.isLocalizzato();
		//this.utilizzato = lisTrasVO.setUtilizzato("SI");
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

	public static final Comparator<ListaTitoliTrascinaVO> ORDINA_PER_PROGRESSIVO = new Comparator<ListaTitoliTrascinaVO>() {
		public int compare(ListaTitoliTrascinaVO o1, ListaTitoliTrascinaVO o2) {
			int p1 = o1.getProgr();
			int p2 = o2.getProgr();
			return p1-p2;
		}
	};

	public String getBid() {
		return bid;
	}

	public String getIsbdELegami() {
		return isbdELegami;
	}

	public int getProgr() {
		return progr;
	}


    public String getSelezBox() {
		return selezBox;
	}

	public String getStato() {
		return stato;
	}

	public String getUtilizzato() {
		return utilizzato;
	}

	private String htmlFilter(String value) {

    	String tmp = this.filter(value);
    	if (tmp != null)
    		tmp = pattern.matcher(tmp).replaceAll(HTML_NEW_LINE);
    	return tmp;
    }

	public void setBid(String bid) {
		this.bid = bid;
	}

	public void setIsbdELegami(String isbdELegami) {
		this.isbdELegami = this.htmlFilter(isbdELegami);;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public void setSelezBox(String selezBox) {
		this.selezBox = selezBox;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public void setUtilizzato(String utilizzato) {
		this.utilizzato = utilizzato;
	}

	public boolean isLocalizzato() {
		return localizzato;
	}

	public void setLocalizzato(boolean localizzato) {
		this.localizzato = localizzato;
	}

}
