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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;

public class ScaricoInventarialeVO extends
		DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -5947622987212263342L;

	private String versoBiblioteca;
	private String polo;
	private String versoBibliotecaDescr;
	private String motivoDelloScarico;
	private String descrMotivoDelloScarico;
	private int numBuonoScarico;
	private String dataScarico;
	private String dataDelibera;
	private String testoDelibera;

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	public ScaricoInventarialeVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public void validate(List<String> errori) throws ValidationException {
		try {
			super.validate(errori);
			if (this.getMotivoDelloScarico() == null
					|| (this.getMotivoDelloScarico() != null && this
							.getMotivoDelloScarico().trim().length() == 0)) {
				errori.add("ERRORE: MotivoDelloScaricoObbligatorio");
			}
			if (this.getMotivoDelloScarico() != null
					&& this.getMotivoDelloScarico().length() != 0) {
				if (this.getMotivoDelloScarico().length() > 1) {
					errori.add("ERRORE: MotivoDelloScaricoEccedente");
				}
			}
			if (!ValidazioneDati.strIsNull(String.valueOf(this
					.getNumBuonoScarico()))) {
				if (ValidazioneDati.strIsNumeric(String.valueOf(this
						.getNumBuonoScarico()))) {
					if (String.valueOf(this.getNumBuonoScarico()).length() > 9) {
						errori.add("ERRORE: numeroBuonoScaricoEccedente");
					}
				} else {
					errori.add("ERRORE: numeroBuonoScaricoNumerico");
				}
			}
			if (this.getDataScarico() != null
					&& this.getDataScarico().length() != 0) {
				int codRitorno = ValidazioneDati.validaData(this
						.getDataScarico());
				if (codRitorno != ValidazioneDati.DATA_OK)
					errori.add("ERRORE: dataScaricoErrata");
				if (this.getDataScarico().equals("00/00/0000")) {
					errori.add("ERRORE: dataScaricoObbligatoria");
				}
			}

			if (this.getDataDelibera() != null
					&& this.getDataDelibera().length() != 0) {
				int codRitorno = ValidazioneDati.validaData(this
						.getDataDelibera());
				if (codRitorno != ValidazioneDati.DATA_OK)
					errori.add("ERRORE: dataDelibera");
			}
			if (this.getTestoDelibera() != null
					&& this.getTestoDelibera().length() != 0) {
				if (this.getTestoDelibera().length() > 50) {
					errori.add("ERRORE: TestoDeliberaEccedente");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getVersoBiblioteca() {
		return versoBiblioteca;
	}

	public void setVersoBiblioteca(String versoBiblioteca) {
		this.versoBiblioteca = versoBiblioteca;
	}

	public String getPolo() {
		return polo;
	}

	public void setPolo(String polo) {
		this.polo = polo;
	}

	public String getMotivoDelloScarico() {
		return motivoDelloScarico;
	}

	public void setMotivoDelloScarico(String motivoDelloScarico) {
		this.motivoDelloScarico = motivoDelloScarico;
	}

	public int getNumBuonoScarico() {
		return numBuonoScarico;
	}

	public void setNumBuonoScarico(int numBuonoScarico) {
		this.numBuonoScarico = numBuonoScarico;
	}

	public String getDataScarico() {
		return dataScarico;
	}

	public void setDataScarico(String dataScarico) {
		this.dataScarico = dataScarico;
	}

	public String getDataDelibera() {
		return dataDelibera;
	}

	public void setDataDelibera(String dataDelibera) {
		this.dataDelibera = dataDelibera;
	}

	public String getTestoDelibera() {
		return testoDelibera;
	}

	public void setTestoDelibera(String testoDelibera) {
		this.testoDelibera = testoDelibera;
	}

	public String getStringaToPrint() {

		if (this.msg == null || this.msg.equals("")) {
			return "<br>";
		} else {
			return msg + "<br>";
		}
	}

	public String getDescrMotivoDelloScarico() {
		return descrMotivoDelloScarico;
	}

	public void setDescrMotivoDelloScarico(String descrMotivoDelloScarico) {
		this.descrMotivoDelloScarico = descrMotivoDelloScarico;
	}

	public String getVersoBibliotecaDescr() {
		return versoBibliotecaDescr;
	}

	public void setVersoBibliotecaDescr(String versoBibliotecaDescr) {
		this.versoBibliotecaDescr = versoBibliotecaDescr;
	}

}
