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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;



public class DettaglioModalitaSupportoForm extends AbstractBibliotecaForm {

	private static final long serialVersionUID = -8039350041822849016L;
	private String  chiamante;
	private boolean conferma = false;
	private boolean sessione = false;
	private boolean nuovo    = false;
	//Contiene l'istanza con i valori salvati sul db. Utilizzato in fase di aggiornamento
	//per stabilire se ci sono delle modifiche da salvare o meno, per evitare di effettuare
	//un aggiornamento inutile con valori uguali a quelli precedentemente inseriti.
	private SupportiModalitaErogazioneVO ultimoSalvato;

	private SupportiModalitaErogazioneVO supportoModalitaErogazione = new SupportiModalitaErogazioneVO();

	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             codiceSupporto;
	//Deve essere sempre valorizzato, sia nel caso di Esamina che nel caso di Nuovo
	private String             desSupporto;
	//Deve essere sempre valorizzato
	private int                idSupporto;

	//Lista dei codici erogazione già associati al supporto
	private List<String>         lstCodiciErogazioneAssociati;
	//Lista dei codici erogazione tra i quali scegliere nel caso di "Aggiungi nuova modalità"
	private List<ComboCodDescVO> lstModalitaErogazione;


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
	public SupportiModalitaErogazioneVO getUltimoSalvato() {
		return ultimoSalvato;
	}
	public void setUltimoSalvato(SupportiModalitaErogazioneVO ultimoSalvato) {
		this.ultimoSalvato = ultimoSalvato;
	}
	public String getCodiceSupporto() {
		return codiceSupporto;
	}
	public void setCodiceSupporto(String codiceSupporto) {
		this.codiceSupporto = codiceSupporto;
	}
	public String getDesSupporto() {
		return desSupporto;
	}
	public void setDesSupporto(String desSupporto) {
		this.desSupporto = desSupporto;
	}
	public int getIdSupporto() {
		return idSupporto;
	}
	public void setIdSupporto(int idSupporto) {
		this.idSupporto = idSupporto;
	}
	public SupportiModalitaErogazioneVO getSupportoModalitaErogazione() {
		return supportoModalitaErogazione;
	}
	public void setSupportoModalitaErogazione(
			SupportiModalitaErogazioneVO supportoModalitaErogazione) {
		this.supportoModalitaErogazione = supportoModalitaErogazione;
	}
	public List<String> getLstCodiciErogazioneAssociati() {
		return lstCodiciErogazioneAssociati;
	}
	public void setLstCodiciErogazioneAssociati(
			List<String> lstCodiciErogazioneAssociati) {
		this.lstCodiciErogazioneAssociati = lstCodiciErogazioneAssociati;
	}
	public List<ComboCodDescVO> getLstModalitaErogazione() {
		return lstModalitaErogazione;
	}
	public void setLstModalitaErogazione(List<ComboCodDescVO> lstModalitaErogazione) {
		this.lstModalitaErogazione = lstModalitaErogazione;
	}
}
