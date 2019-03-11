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

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class SinteticaProposteDiCorrezioneView extends SerializableVO {

	// = SinteticaProposteDiCorrezioneView.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 3071652006904660042L;

	private int numNotizie;

	private int progressivo;

	private String dataInserimento;
	private String destinatari;
	private String idOggetto;
	private int idProposta;
	private String mittenteBiblioteca;
	private String mittenteUserId;
	private String statoProposta;
	private String testo;
	private String tipoMateriale;
	private String natura;
	private String tipoAuthority;
	private String key;

	private String destinatariData;
	private String destinatariBiblio;
	private String destinatariNote;

	private List listaDestinatariProp;

	private Pattern pattern;

	private static final String HTML_NEW_LINE_REGEX = "&lt;.*?br.*?&gt;";
    private static final String HTML_NEW_LINE = "<br />";


	public SinteticaProposteDiCorrezioneView() {
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

    public String getDataInserimento() {
		return dataInserimento;
	}


	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}


	public String getDestinatari() {
		return destinatari;
	}


	public void setDestinatari(String destinatari) {
		this.destinatari = destinatari;
	}


	public String getIdOggetto() {
		return idOggetto;
	}


	public void setIdOggetto(String idOggetto) {
		this.idOggetto = idOggetto;
	}



	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}



	public String getMittenteBiblioteca() {
		return mittenteBiblioteca;
	}


	public void setMittenteBiblioteca(String mittenteBiblioteca) {
		this.mittenteBiblioteca = mittenteBiblioteca;
	}


	public String getMittenteUserId() {
		return mittenteUserId;
	}


	public void setMittenteUserId(String mittenteUserId) {
		this.mittenteUserId = mittenteUserId;
	}


	public int getNumNotizie() {
		return numNotizie;
	}


	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}


	public String getStatoProposta() {
		return statoProposta;
	}


	public void setStatoProposta(String statoProposta) {
		this.statoProposta = statoProposta;
	}


	public String getTesto() {
		return testo;
	}


	public void setTesto(String testo) {
		this.testo = testo;
	}


	public String getTipoAuthority() {
		return tipoAuthority;
	}


	public void setTipoAuthority(String tipoAuthority) {
		this.tipoAuthority = tipoAuthority;
	}


	public String getTipoMateriale() {
		return tipoMateriale;
	}


	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
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
    public static final Comparator sortListaSinteticaAut = new Comparator() {
    	public int compare(Object o1, Object o2) {
    		int myProgr1 = ((SinteticaProposteDiCorrezioneView) o1).getProgressivo();
    		int myProgr2 = ((SinteticaProposteDiCorrezioneView) o2).getProgressivo();
    		return myProgr1-myProgr2;
    	}
    };



	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}


	public int getIdProposta() {
		return idProposta;
	}


	public void setIdProposta(int idProposta) {
		this.idProposta = idProposta;
	}


	public String getDestinatariBiblio() {
		return destinatariBiblio;
	}


	public void setDestinatariBiblio(String destinatariBiblio) {
		this.destinatariBiblio = destinatariBiblio;
	}


	public String getDestinatariData() {
		return destinatariData;
	}


	public void setDestinatariData(String destinatariData) {
		this.destinatariData = destinatariData;
	}


	public String getDestinatariNote() {
		return destinatariNote;
	}


	public void setDestinatariNote(String destinatariNote) {
		this.destinatariNote = destinatariNote;
	}


	public String getNatura() {
		return natura;
	}


	public void setNatura(String natura) {
		this.natura = natura;
	}


	public List getListaDestinatariProp() {
		return listaDestinatariProp;
	}


	public void setListaDestinatariProp(List listaDestinatariProp) {
		this.listaDestinatariProp = listaDestinatariProp;
	}


}
