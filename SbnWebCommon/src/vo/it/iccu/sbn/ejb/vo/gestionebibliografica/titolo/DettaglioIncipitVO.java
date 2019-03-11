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

package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

public class DettaglioIncipitVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 7632103175996184396L;

	private String numComposizione;
	private String numMovimento;
	private String numProgDocumento;
	private String nomePersonaggio;
	private String voceStrumentoSelez;
	private String formaMusicaleSelez;
	private String indicazMovimento;
	private String tonalitaSelez;
	private String chiave;
	private String alterazioni;
	private String misura;
	private String contestoMusicale;
	private String incipitTestuale;

	public DettaglioIncipitVO(String numComposizione, String numMovimento,
			String numProgDocumento, String nomePersonaggio,
			String voceStrumentoSelez, String formaMusicaleSelez,
			String indicazMovimento, String tonalitaSelez, String chiave,
			String alterazioni, String misura, String contestoMusicale,
			String incipitTestuale) {
		super();
		// TODO Auto-generated constructor stub
		this.numComposizione = numComposizione;
		this.numMovimento = numMovimento;
		this.numProgDocumento = numProgDocumento;
		this.nomePersonaggio = nomePersonaggio;
		this.voceStrumentoSelez = voceStrumentoSelez;
		this.formaMusicaleSelez = formaMusicaleSelez;
		this.indicazMovimento = indicazMovimento;
		this.tonalitaSelez = tonalitaSelez;
		this.chiave = chiave;
		this.alterazioni = alterazioni;
		this.misura = misura;
		this.contestoMusicale = contestoMusicale;
		this.incipitTestuale = incipitTestuale;
	}

	public DettaglioIncipitVO() {
		super();
		this.numComposizione = "";
		this.numMovimento = "";
		this.numProgDocumento = "";
		this.nomePersonaggio = "";
		this.voceStrumentoSelez = "";
		this.formaMusicaleSelez = "";
		this.indicazMovimento = "";
		this.tonalitaSelez = "";
		this.chiave = "";
		this.alterazioni = "";
		this.misura = "";
		this.contestoMusicale = "";
		this.incipitTestuale = "";
	}

	public String getAlterazioni() {
		return alterazioni;
	}

	public void setAlterazioni(String alterazioni) {
		this.alterazioni = alterazioni;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getContestoMusicale() {
		return contestoMusicale;
	}

	public void setContestoMusicale(String contestoMusicale) {
		this.contestoMusicale = trimAndSet(contestoMusicale);
	}

	public String getFormaMusicaleSelez() {
		return formaMusicaleSelez;
	}

	public void setFormaMusicaleSelez(String formaMusicaleSelez) {
		this.formaMusicaleSelez = formaMusicaleSelez;
	}

	public String getIncipitTestuale() {
		return incipitTestuale;
	}

	public void setIncipitTestuale(String incipitTestuale) {
		this.incipitTestuale = incipitTestuale;
	}

	public String getIndicazMovimento() {
		return indicazMovimento;
	}

	public void setIndicazMovimento(String indicazMovimento) {
		this.indicazMovimento = indicazMovimento;
	}

	public String getMisura() {
		return misura;
	}

	public void setMisura(String misura) {
		this.misura = misura;
	}

	public String getNomePersonaggio() {
		return nomePersonaggio;
	}

	public void setNomePersonaggio(String nomePersonaggio) {
		this.nomePersonaggio = nomePersonaggio;
	}

	public String getNumComposizione() {
		return numComposizione;
	}

	public void setNumComposizione(String numComposizione) {
		this.numComposizione = numComposizione;
	}

	public String getNumMovimento() {
		return numMovimento;
	}

	public void setNumMovimento(String numMovimento) {
		this.numMovimento = trimAndSet(numMovimento);
	}

	public String getNumProgDocumento() {
		return numProgDocumento;
	}

	public void setNumProgDocumento(String numProgDocumento) {
		this.numProgDocumento = trimAndSet(numProgDocumento);
	}

	public String getTonalitaSelez() {
		return tonalitaSelez;
	}

	public void setTonalitaSelez(String tonalitaSelez) {
		this.tonalitaSelez = tonalitaSelez;
	}

	public String getVoceStrumentoSelez() {
		return voceStrumentoSelez;
	}

	public void setVoceStrumentoSelez(String voceStrumentoSelez) {
		this.voceStrumentoSelez = voceStrumentoSelez;
	}

	@Override
	public int getRepeatableId() {
		StringBuilder buf = new StringBuilder(128);
		buf.append(ValidazioneDati.coalesce(OrdinamentoCollocazione2.normalizza(numMovimento), " "))
			.append('-')
			.append(ValidazioneDati.coalesce(OrdinamentoCollocazione2.normalizza(numProgDocumento),	" "));

		return buf.toString().hashCode();
	}

}
