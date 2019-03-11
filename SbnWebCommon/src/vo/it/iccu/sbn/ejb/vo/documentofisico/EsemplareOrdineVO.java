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
package it.iccu.sbn.ejb.vo.documentofisico;

public class EsemplareOrdineVO extends EsemplareVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 4959485755512985171L;
	private String codTipOrd;
	private String annoOrd;
	private String codOrd;

	public EsemplareOrdineVO(){
	}
	public EsemplareOrdineVO(String codPolo, String codBib, String codTipOrd, String annoOrd, String codOrd)
	throws Exception {
		super(codPolo, codBib);
		this.codTipOrd = codTipOrd;
		this.annoOrd = annoOrd;
		this.codOrd = codOrd;
	}
	public String getAnnoOrd() {
		return annoOrd;
	}
	public void setAnnoOrd(String annoOrd) {
		this.annoOrd = annoOrd;
	}
	public String getCodOrd() {
		return codOrd;
	}
	public void setCodOrd(String codOrd) {
		this.codOrd = codOrd;
	}
	public String getCodTipOrd() {
		return codTipOrd;
	}
	public void setCodTipOrd(String codTipOrd) {
		this.codTipOrd = codTipOrd;
	}
}
