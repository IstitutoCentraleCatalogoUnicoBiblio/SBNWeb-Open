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
package it.iccu.sbn.web.actionforms.servizi.configurazione;

import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class DettaglioServizioForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = 6056143178945272942L;
	private String  chiamante;
	private boolean conferma = false;
	private boolean sessione = false;
	private boolean nuovo    = false;
	private boolean penalita = false;

	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             codiceTipoServizio;
	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             desTipoServizio;
	//Deve essere sempre valorizzato
	private int                idTipoServizio;
	private ServizioBibliotecaVO      servizio = new ServizioBibliotecaVO();
	//Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	//per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	//un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	private ServizioBibliotecaVO      ultimoSalvato;
	//Lista contenente i codici ti tutti i servizi associati al tipo servizio in esame
	private List<String>       lstCodiciServiziAssociati;

	private boolean richiedePrenotPosto = false;



	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public boolean isConferma() {
		return conferma;
	}
	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public boolean isNuovo() {
		return nuovo;
	}
	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}
	public String getCodiceTipoServizio() {
		return codiceTipoServizio;
	}
	public void setCodiceTipoServizio(String codiceTipoServizio) {
		this.codiceTipoServizio = codiceTipoServizio;
	}
	public String getDesTipoServizio() {
		return desTipoServizio;
	}
	public void setDesTipoServizio(String desTipoServizio) {
		this.desTipoServizio = desTipoServizio;
	}
	public ServizioBibliotecaVO getServizio() {
		return servizio;
	}
	public void setServizio(ServizioBibliotecaVO servizio) {
		this.servizio = servizio;
	}
	public boolean isPenalita() {
		return penalita;
	}
	public void setPenalita(boolean penalita) {
		this.penalita = penalita;
	}
	public List<String> getLstCodiciServiziAssociati() {
		return lstCodiciServiziAssociati;
	}
	public void setLstCodiciServiziAssociati(List<String> lstCodiciServiziAssociati) {
		this.lstCodiciServiziAssociati = lstCodiciServiziAssociati;
	}
	public boolean isRichiedePrenotPosto() {
		return richiedePrenotPosto;
	}
	public void setRichiedePrenotPosto(boolean richiedePrenotPosto) {
		this.richiedePrenotPosto = richiedePrenotPosto;
	}
	public int getIdTipoServizio() {
		return idTipoServizio;
	}
	public void setIdTipoServizio(int idTipoServizio) {
		this.idTipoServizio = idTipoServizio;
	}
	public ServizioBibliotecaVO getUltimoSalvato() {
		return ultimoSalvato;
	}
	public void setUltimoSalvato(ServizioBibliotecaVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}

}
