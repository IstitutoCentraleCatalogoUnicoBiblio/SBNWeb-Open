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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElementoSinteticaDescrittoreVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 3685085883735520613L;

	private static final Pattern patternXid = Pattern.compile("(\\w{3}[E|M|V|C|D|L|0-9]\\d{6})", Pattern.CASE_INSENSITIVE);

	private static final String HTML_NEW_LINE = "<br />";
	private static final String HTML_NEW_LINE_REGEX = "(&lt;br\\s{0,1}?&gt;)|(&lt;br\\s*?/&gt;)";
	private static final Pattern patternNewLine = Pattern.compile(HTML_NEW_LINE_REGEX, Pattern.CASE_INSENSITIVE);

	private int progr;
	private String did;
	private String termine;
	private String formaNome;
	private String livelloAutorita;
	private String nome;
	private String nomeSenzaRinvio;
	private String keyDidNome;
	private String codiceSoggettario;
	private String edizioneSoggettario;
	private int soggetti;
	private int utilizzati;
	private String didFormaAccettata;

	private String categoriaTermine;

	public String getDidFormaAccettata() {
		return didFormaAccettata;
	}

	public void setDidFormaAccettata(String didFormaAccettata) {
		this.didFormaAccettata = didFormaAccettata;
	}

	public ElementoSinteticaDescrittoreVO() {
		super();
	}

	public static final Comparator<ElementoSinteticaDescrittoreVO> ORDINA_PER_PROGRESSIVO = new Comparator<ElementoSinteticaDescrittoreVO>() {
		public int compare(ElementoSinteticaDescrittoreVO o1,
				ElementoSinteticaDescrittoreVO o2) {
			int p1 = (o1).getProgr();
			int p2 = (o2).getProgr();
			return p1 - p2;
		}
	};

	public ElementoSinteticaDescrittoreVO(int progr, String did, String termine) {
		this.progr = progr;
		this.did = did;
		this.termine = termine;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTermine() {
		return termine;
	}

	public String getTermineWithLinks() {
		String filter = htmlFilter(this.termine);
		Matcher m = patternXid.matcher(filter);
		return m.replaceAll("key{$1}");
	}

	public void setTermine(String termine) {
		this.termine = termine;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getFormaNome() {
		return formaNome;
	}

	public boolean isRinvio() {
		return (isFilled(formaNome) && formaNome.equalsIgnoreCase("R"));
	}

	public void setFormaNome(String formaNome) {
		this.formaNome = formaNome;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getKeyDidNome() {
		return keyDidNome;
	}

	public void setKeyDidNome(String keyDidNome) {
		this.keyDidNome = keyDidNome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = this.htmlFilter(nome);
	}

	private String filter(String value) {
		if (value == null || value.length() == 0)
			return value;
		StringBuffer result = null;
		String filtered = null;
		for (int i = 0; i < value.length(); i++) {
			filtered = null;
			switch (value.charAt(i)) {
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
			if (result == null) {
				if (filtered != null) {
					result = new StringBuffer(value.length() + 50);
					if (i > 0)
						result.append(value.substring(0, i));
					result.append(filtered);
				}
			} else if (filtered == null)
				result.append(value.charAt(i));
			else
				result.append(filtered);
		}

		return result != null ? result.toString() : value;
	}

	private String htmlFilter(String value) {

		String tmp = this.filter(value);
		if (tmp != null)
			tmp = patternNewLine.matcher(tmp).replaceAll(HTML_NEW_LINE);
		return tmp;
	}

	public String getNomeSenzaRinvio() {
		return nomeSenzaRinvio;
	}

	public void setNomeSenzaRinvio(String nomeSenzaRinvio) {
		this.nomeSenzaRinvio = nomeSenzaRinvio;
	}

	public int getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(int soggetti) {
		this.soggetti = soggetti;
	}

	public int getUtilizzati() {
		return utilizzati;
	}

	public void setUtilizzati(int utilizzati) {
		this.utilizzati = utilizzati;
	}

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public boolean isGestisceEdizione() {
		return isFilled(edizioneSoggettario);
	}

	public String getCategoriaTermine() {
		return categoriaTermine;
	}

	public void setCategoriaTermine(String categoriaTermine) {
		this.categoriaTermine = categoriaTermine;
	}

}
