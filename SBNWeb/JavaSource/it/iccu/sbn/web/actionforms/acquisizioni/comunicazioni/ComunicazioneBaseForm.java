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
package it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import org.apache.struts.action.ActionForm;

public abstract class ComunicazioneBaseForm extends ActionForm implements AcquisizioniBaseFormIntf{


	private static final long serialVersionUID = 697584293115002708L;

	private String testoVuoto = "";

	private String tipoDocumento;
	private List listaTipoDocumento;
	private String statoComunicazione;
	private List listaStatoComunicazione;
	private String tipoMessaggio;
	private List ListaTipoMessaggio;
	private String direzioneComunicazione;
	private List listaDirezioneComunicazione;
	private String tipoOrdine;
	private List ListaTipoOrdine;
	private String tipoInvio;
	private List ListaTipoInvio;
	private ComunicazioneVO datiComunicazione;

	private boolean caricamentoIniziale = false;

	private boolean sessione;
	private boolean conferma = false;
	private String pressioneBottone = null;
	private boolean disabilitaTutto = false;

//	private String codFornitore;
//
//	private String fornitore;

	public String getTestoVuoto() {
		return testoVuoto;
	}

	public void setTestoVuoto(String testoVuoto) {
		this.testoVuoto = testoVuoto;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public String getStatoComunicazione() {
		return statoComunicazione;
	}

	public void setStatoComunicazione(String statoComunicazione) {
		this.statoComunicazione = statoComunicazione;
	}

	public List getListaStatoComunicazione() {
		return listaStatoComunicazione;
	}

	public void setListaStatoComunicazione(List listaStatoComunicazione) {
		this.listaStatoComunicazione = listaStatoComunicazione;
	}

	public String getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public List getListaTipoMessaggio() {
		return ListaTipoMessaggio;
	}

	public void setListaTipoMessaggio(List listaTipoMessaggio) {
		ListaTipoMessaggio = listaTipoMessaggio;
	}

	public String getDirezioneComunicazione() {
		return direzioneComunicazione;
	}

	public void setDirezioneComunicazione(String direzioneComunicazione) {
		this.direzioneComunicazione = direzioneComunicazione;
	}

	public List getListaDirezioneComunicazione() {
		return listaDirezioneComunicazione;
	}

	public void setListaDirezioneComunicazione(
			List listaDirezioneComunicazione) {
		this.listaDirezioneComunicazione = listaDirezioneComunicazione;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public List getListaTipoOrdine() {
		return ListaTipoOrdine;
	}

	public void setListaTipoOrdine(List listaTipoOrdine) {
		ListaTipoOrdine = listaTipoOrdine;
	}

	public String getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(String tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	public List getListaTipoInvio() {
		return ListaTipoInvio;
	}

	public void setListaTipoInvio(List listaTipoInvio) {
		ListaTipoInvio = listaTipoInvio;
	}

	public ComunicazioneVO getDatiComunicazione() {
		return datiComunicazione;
	}

	public void setDatiComunicazione(ComunicazioneVO datiComunicazione) {
		this.datiComunicazione = datiComunicazione;
	}

	public boolean isCaricamentoIniziale() {
		return caricamentoIniziale;
	}

	public void setCaricamentoIniziale(boolean caricamentoIniziale) {
		this.caricamentoIniziale = caricamentoIniziale;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getPressioneBottone() {
		return pressioneBottone;
	}

	public void setPressioneBottone(String pressioneBottone) {
		this.pressioneBottone = pressioneBottone;
	}

	public boolean isDisabilitaTutto() {
		return disabilitaTutto;
	}

	public void setDisabilitaTutto(boolean disabilitaTutto) {
		this.disabilitaTutto = disabilitaTutto;
	}

	public String getCodFornitore() {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.datiComunicazione.getFornitore().getCodice();
	}

	public String getFornitore() {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.datiComunicazione.getFornitore().getDescrizione();
	}

	public void setCodFornitore(String codFornitore) {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setFornitore(new StrutturaCombo());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.datiComunicazione.getFornitore().setCodice(codFornitore);

	}

	public void setFornitore(String fornitore) {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.datiComunicazione.getFornitore().setDescrizione(fornitore);
	}

	public FornitoreVO getFornitoreVO() {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setAnagFornitore(new FornitoreVO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiComunicazione.getAnagFornitore() == null){
				try {
					this.datiComunicazione.setAnagFornitore(new FornitoreVO());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return this.datiComunicazione.getAnagFornitore();
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		if (this.datiComunicazione == null){
			try {
				this.datiComunicazione = new ComunicazioneVO();
				this.datiComunicazione.setAnagFornitore(new FornitoreVO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiComunicazione.getAnagFornitore() == null){
				try {
					this.datiComunicazione.setAnagFornitore(new FornitoreVO());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.datiComunicazione.setAnagFornitore(fornitoreVO);
	}

}
