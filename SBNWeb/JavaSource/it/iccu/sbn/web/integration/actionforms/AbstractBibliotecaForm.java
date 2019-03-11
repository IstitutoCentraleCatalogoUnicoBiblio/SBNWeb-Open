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
package it.iccu.sbn.web.integration.actionforms;

import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoPasswordVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractBibliotecaForm extends ActionForm {


	private static final long serialVersionUID = -5673391000553182151L;
	private final Timestamp creationTime;
	private String biblioteca = "XXXAMM";
	private String userId = "00001";
	private int maxRighe = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private String paginaCorrente = "1";
	private String tipoOrd = "1";
	private String tipoOutput = "001";
	private int totRighe;
	private int bloccoSelezionato = 1;
	private String idLista = "";
	private int numPrimo = 0;
	private int totBlocchi = 0;
	private InfoPasswordVO infoPwd = new InfoPasswordVO();

	{
		this.creationTime = DaoManager.now();
	}

	public int getTotBlocchi() {
		return totBlocchi;
	}

	public void setTotBlocchi(int totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public String getPaginaCorrente() {
		return paginaCorrente;
	}

	public void setPaginaCorrente(String paginaCorrente) {
		this.paginaCorrente = paginaCorrente;
	}

	public String getTipoOrd() {
		return tipoOrd;
	}

	public void setTipoOrd(String tipoOrd) {
		this.tipoOrd = tipoOrd;
	}

	public String getTipoOutput() {
		return tipoOutput;
	}

	public void setTipoOutput(String tipoOutput) {
		this.tipoOutput = tipoOutput;
	}

	public int getTotRighe() {
		return totRighe;
	}

	public void setTotRighe(int totRighe) {
		this.totRighe = totRighe;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTotPagineOut() {
		int np = 0;
		try {
			np = getTotRighe() / getMaxRighe();
			if (getTotRighe() % getMaxRighe() > 0) {
				np++;
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		return np;
	}

	public int getBloccoSelezionato() {
		return bloccoSelezionato;
	}

	public void setBloccoSelezionato(int bloccoSelezionato) {
		this.bloccoSelezionato = bloccoSelezionato;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public String getIdLista() {
		return idLista;
	}

	public void setIdLista(String idLista) {
		this.idLista = idLista;
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public InfoPasswordVO getInfoPwd() {
		return infoPwd;
	}

	public void setInfoPwd(InfoPasswordVO infoPwd) {
		this.infoPwd = infoPwd;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

}
