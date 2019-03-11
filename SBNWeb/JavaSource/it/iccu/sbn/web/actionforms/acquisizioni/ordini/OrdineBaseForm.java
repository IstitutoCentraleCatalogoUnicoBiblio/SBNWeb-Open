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
package it.iccu.sbn.web.actionforms.acquisizioni.ordini;

import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

import org.apache.struts.action.ActionForm;

public abstract class OrdineBaseForm extends ActionForm  implements AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = -6813704816264609938L;
	private OrdiniVO datiOrdine = new OrdiniVO();
	private boolean esaminaOrdine = false;
	private String[] pulsanti;

//	private String prezzo;
//	private String prezzoEur;
	private String valuta;
	private String prezzoStr;
	private String prezzoEurStr;
	private double prezzoOrdine;
	private double prezzoEuroOrdine;
	private List listaValuta;
	private boolean conferma = false;

	//almaviva5_20121115 evolutive google
	private List<TB_CODICI> listaTipoLavorazione;
	private String cd_tipo_lav;

	private CambioVO valutaRif;

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public OrdiniVO getDatiOrdine() {
		return datiOrdine;
	}

	public void setDatiOrdine(OrdiniVO datiOrdine) {
		this.datiOrdine = datiOrdine;
	}

	public boolean isEsaminaOrdine() {
		return esaminaOrdine;
	}

	public void setEsaminaOrdine(boolean esaminaOrdine) {
		this.esaminaOrdine = esaminaOrdine;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public String getCodFornitore() {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getFornitore() == null){
				try {
					this.datiOrdine.setFornitore(new StrutturaCombo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return this.datiOrdine.getFornitore().getCodice();
	}

	public String getFornitore() {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getFornitore() == null){
				try {
					this.datiOrdine.setFornitore(new StrutturaCombo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return this.datiOrdine.getFornitore().getDescrizione();
	}

	public void setCodFornitore(String codFornitore) {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setFornitore(new StrutturaCombo());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getFornitore() == null){
				try {
					this.datiOrdine.setFornitore(new StrutturaCombo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.datiOrdine.getFornitore().setCodice(codFornitore);

	}

	public void setFornitore(String fornitore) {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setFornitore(new StrutturaCombo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getFornitore() == null){
				try {
					this.datiOrdine.setFornitore(new StrutturaCombo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.datiOrdine.getFornitore().setDescrizione(fornitore);
	}


	public FornitoreVO getFornitoreVO() {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setAnagFornitore(new FornitoreVO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getAnagFornitore() == null){
				try {
					this.datiOrdine.setAnagFornitore(new FornitoreVO());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return this.datiOrdine.getAnagFornitore();
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		if (this.datiOrdine == null){
			try {
				this.datiOrdine = new OrdiniVO();
				this.datiOrdine.setAnagFornitore(new FornitoreVO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if (this.datiOrdine.getAnagFornitore() == null){
				try {
					this.datiOrdine.setAnagFornitore(new FornitoreVO());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.datiOrdine.setAnagFornitore(fornitoreVO);
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public String getPrezzoStr() {
		return prezzoStr;
	}

	public void setPrezzoStr(String prezzoStr) {
		this.prezzoStr = prezzoStr;
	}

	public String getPrezzoEurStr() {
		return prezzoEurStr;
	}

	public void setPrezzoEurStr(String prezzoEurStr) {
		this.prezzoEurStr = prezzoEurStr;
	}

	public double getPrezzoOrdine() {
		return prezzoOrdine;
	}

	public void setPrezzoOrdine(double prezzoOrdine) {
		this.prezzoOrdine = prezzoOrdine;
	}

	public double getPrezzoEuroOrdine() {
		return prezzoEuroOrdine;
	}

	public void setPrezzoEuroOrdine(double prezzoEuroOrdine) {
		this.prezzoEuroOrdine = prezzoEuroOrdine;
	}

	public List getListaValuta() {
		return listaValuta;
	}

	public void setListaValuta(List listaValuta) {
		this.listaValuta = listaValuta;
	}

	public List<TB_CODICI> getListaTipoLavorazione() {
		return listaTipoLavorazione;
	}

	public void setListaTipoLavorazione(List<TB_CODICI> listaTipoLavorazione) {
		this.listaTipoLavorazione = listaTipoLavorazione;
	}

	public String getCd_tipo_lav() {
		return cd_tipo_lav;
	}

	public void setCd_tipo_lav(String cd_tipo_lav) {
		this.cd_tipo_lav = cd_tipo_lav;
	}

	public CambioVO getValutaRif() {
		return valutaRif;
	}

	public void setValutaRif(CambioVO valutaRif) {
		this.valutaRif = valutaRif;
	}

}
