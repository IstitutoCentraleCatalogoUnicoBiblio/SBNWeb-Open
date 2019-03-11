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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class AreaDatiPassaggioElenchiListeConfrontoVO  extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -7285560542791506365L;


	public AreaDatiPassaggioElenchiListeConfrontoVO() {
		super();

		listaNomeLista = new ArrayList();
		listaDataLista = new ArrayList();
		listaIdLista = new ArrayList();
	}
	/**
	 *
	 */
	private List listaIdLista;

	private List listaNomeLista;
	private List listaDataLista;

	private String ticket;

	private String codErr;
	private String testoProtocollo;



	public List getListaNomeLista() {
		return listaNomeLista;
	}
	public void setListaNomeLista(List listaNomeLista) {
		this.listaNomeLista = listaNomeLista;
	}
	public List addListaNomeLista(String newLine) {
		listaNomeLista.add(newLine);
		return listaNomeLista;
	}

	public List getListaDataLista() {
		return listaDataLista;
	}
	public void setListaDataLista(List listaDataLista) {
		this.listaDataLista = listaDataLista;
	}
	public List addListaDataLista(String newLine) {
		listaDataLista.add(newLine);
		return listaDataLista;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCodErr() {
		return codErr;
	}
	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}
	public String getTestoProtocollo() {
		return testoProtocollo;
	}
	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}
	public List getListaIdLista() {
		return listaIdLista;
	}
	public void setListaIdLista(List listaIdLista) {
		this.listaIdLista = listaIdLista;
	}
	public List addListaIdLista(int newLine) {
		listaIdLista.add(newLine);
		return listaIdLista;
	}

}
