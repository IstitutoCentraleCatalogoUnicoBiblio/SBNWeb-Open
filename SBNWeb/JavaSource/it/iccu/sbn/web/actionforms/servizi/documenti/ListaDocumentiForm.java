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
package it.iccu.sbn.web.actionforms.servizi.documenti;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

import java.util.List;

public class ListaDocumentiForm extends RicercaDocumentoForm {


	private static final long serialVersionUID = -2677723215508929101L;
	private DescrittoreBloccoVO blocco;
	private List<DocumentoNonSbnVO> documenti;
	private int bloccoSelezionato;
	private String codSelezionato;
	private String[] idSelezionati;
	private boolean conferma = false;
	private TipoOperazione operazione = null;


	public DescrittoreBloccoVO getBlocco() {
		return blocco;
	}

	public void setBlocco(DescrittoreBloccoVO blocco) {
		this.blocco = blocco;
	}

	public List<DocumentoNonSbnVO> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(List<DocumentoNonSbnVO> documenti) {
		this.documenti = documenti;
	}

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public String[] getIdSelezionati() {
		return idSelezionati;
	}

	public void setIdSelezionati(String[] idSelezionati) {
		this.idSelezionati = idSelezionati;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public TipoOperazione getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazione operazione) {
		this.operazione = operazione;
	}

}
