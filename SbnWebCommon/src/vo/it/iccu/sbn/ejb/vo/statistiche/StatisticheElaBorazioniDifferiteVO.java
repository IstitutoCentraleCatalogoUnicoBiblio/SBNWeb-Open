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
package it.iccu.sbn.ejb.vo.statistiche;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.List;

public class StatisticheElaBorazioniDifferiteVO extends ElaborazioniDifferiteOutputVo{

	/**
	 *
	 */
	private static final long serialVersionUID = 6865772697839425389L;


	public StatisticheElaBorazioniDifferiteVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public void validate(List<String> errori) throws ValidationException {
		try{
			super.validate();
			if (this.getCodPolo() == null || (this.getCodPolo()!=null && this.getCodPolo().trim().length()==0)){
				errori.add("ERRORE: CodPoloObbligatorio");
			}
			if (this.getCodBib() == null || (this.getCodBib()!=null && this.getCodBib().trim().length()==0)){
				errori.add("ERRORE: CodBibObbligatorio");
			}
			if (this.getCodPolo() !=null &&  this.getCodPolo().length()!=0)	{
				if (this.getCodPolo().length()>3)	{
					errori.add("ERRORE: CodPoloEccedente");
				}
			}
			if (this.getCodBib() !=null &&  this.getCodBib().length()!=0)	{
				if (this.getCodBib().length()>3)	{
					errori.add("ERRORE: CodBerroriEccedente");
				}
			}
//				if (this.getSerie() !=null) {
//					if (this.getSerie().length()>3) {
//						errori.add("ERRORE: codSerieEccedente");
//					}
//				}else{
//					errori.add("ERRORE: codSerie = null");
//				}
//
//				if (!ValidazioneDati.strIsNull(this.getStartInventario())) {
//					if (ValidazioneDati.strIsNumeric(this.getStartInventario())){
//						if ((this.getStartInventario()).length()>9) {
//							errori.add("codInventDaEccedente");
//						}
//					}else{
//						errori.add("ERRORE: codInventDaDeveEssereNumerico");
//					}
//				}else{
//					errori.add("ERRORE: codInvent = null");
//				}
//
//				if (!ValidazioneDati.strIsNull(this.getEndInventario())) {
//					if (ValidazioneDati.strIsNumeric(this.getEndInventario())){
//						if ((this.getEndInventario()).length()>9) {
//							throw new ValidationException("codInventAEccedente");
//						}
//					}else{
//						errori.add("ERRORE: codInventADeveEssereNumerico");
//					}
//				}else{
//					errori.add("ERRORE: codInvent = null");
//				}
//
//
//				if (this.getEndInventario() != null && this.getStartInventario() != null){
//					if (Integer.valueOf(this.getEndInventario()) < Integer.valueOf(this.getStartInventario()) ) {
//						errori.add("codInventDaDeveEssereMinoreDiCodInventA");
//					}
//				}else{
//					errori.add("ERRORE: codInvent = null");
//				}
//

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
