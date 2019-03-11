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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch;

import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class ParametriBatchSoggettoBaseVO extends ParametriRichiestaElaborazioneDifferitaVO {

	private static final long serialVersionUID = -8509072224672313705L;

	private String codSoggettario;
	private String edizioneSoggettario;
	private CommandType tipoOperazione;

	private String fromCid;
	private String toCid;

	private String inputFile;

	public ParametriBatchSoggettoBaseVO() {
		super();
	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (!ValidazioneDati.in(tipoOperazione, CommandType.SEM_AGGIORNAMENTO_MASSIVO_SOGGETTI))
			if (isNull(codSoggettario))
				throw new ValidationException(SbnErrorTypes.GS_CODICE_SOGGETTARIO_OBBLIGATORIO);

		if (isFilled(fromCid) && !fromCid.matches("[A-Z]{1}\\w{2}C\\d{6}") )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "cid");

		if (isFilled(toCid) && !toCid.matches("[A-Z]{1}\\w{2}C\\d{6}") )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "cid");

		if (isFilled(inputFile) && (isFilled(fromCid) || isFilled(toCid)) )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "file");
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public CommandType getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(CommandType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getFromCid() {
		return fromCid;
	}

	public void setFromCid(String mig_tsVar) {
		this.fromCid = mig_tsVar;
	}

	public String getToCid() {
		return toCid;
	}

	public void setToCid(String toCid) {
		this.toCid = toCid;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public boolean isFromFile() {
		return (isFilled(inputFile) && !isFilled(fromCid) && !isFilled(toCid));
	}

}
