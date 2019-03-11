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
package it.iccu.sbn.ejb.vo.servizi.erogazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.FiltroCollocazioneVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;

public class MovimentoRicercaVO extends MovimentoVO {

	private static final long serialVersionUID = 8003488579637747350L;

	protected String dataFinePrev_da;
	protected String dataFinePrev_a;
	protected Timestamp tsDataFinePrev_da;
	protected Timestamp tsDataFinePrev_a;
	//
	// indica se richiesti movimenti in corso
	private boolean richiesteInCorso = true;
	// indica se richiesti movimenti respinti
	private boolean richiesteRespinte = false;
	// indica se richiesti movimenti evasi
	private boolean richiesteEvase = false;

	//Inizio modifica del 30/03/2010 almaviva
	// indica se richiesti di prenotazioni
	private boolean richiestePrenotazioni = false;

	private boolean utenteLettore = false;

	private String rfidChiaveInventario;

	private boolean escludiSollecitiProrogati = true;

	private FiltroCollocazioneVO filtroColl = new FiltroCollocazioneVO();

	private boolean includiPrenotazioniPosto;


	public void validate() throws ValidationException {
		super.validate();

		Timestamp from = null;
		Timestamp to = null;

		// validazione date
		if (isFilled(dataFinePrev_da) && ValidazioneDati.validaData(dataFinePrev_da) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (isFilled(dataFinePrev_a) && ValidazioneDati.validaData(dataFinePrev_a) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		from = DateUtil.toTimestamp(dataFinePrev_da);
		to = DateUtil.toTimestampA(dataFinePrev_a);

		if (isFilled(dataFinePrev_da) && isFilled(dataFinePrev_a) )
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		tsDataFinePrev_da = from;
		tsDataFinePrev_a = to;

	}

	public String getDataFinePrev_da() {
		return dataFinePrev_da;
	}

	public void setDataFinePrev_da(String dataFinePrevDa) {
		dataFinePrev_da = dataFinePrevDa;
	}

	public String getDataFinePrev_a() {
		return dataFinePrev_a;
	}

	public void setDataFinePrev_a(String dataFinePrevA) {
		dataFinePrev_a = dataFinePrevA;
	}

	public Timestamp getTsDataFinePrev_da() {
		return tsDataFinePrev_da;
	}

	public void setTsDataFinePrev_da(Timestamp tsDataFinePrevDa) {
		tsDataFinePrev_da = tsDataFinePrevDa;
	}

	public Timestamp getTsDataFinePrev_a() {
		return tsDataFinePrev_a;
	}

	public void setTsDataFinePrev_a(Timestamp tsDataFinePrevA) {
		tsDataFinePrev_a = tsDataFinePrevA;
	}

	public boolean isRichiesteInCorso() {
		return richiesteInCorso;
	}

	public void setRichiesteInCorso(boolean movimentiInCorso) {
		this.richiesteInCorso = movimentiInCorso;
	}

	public boolean isRichiesteRespinte() {
		return richiesteRespinte;
	}

	public void setRichiesteRespinte(boolean movimentiRespinti) {
		this.richiesteRespinte = movimentiRespinti;
	}

	public boolean isRichiesteEvase() {
		return richiesteEvase;
	}

	public void setRichiesteEvase(boolean movimentiChiusi) {
		this.richiesteEvase = movimentiChiusi;
	}

	//Inizio modifica del 30/03/2010 almaviva
	public void setRichiestePrenotazioni(boolean richiestePrenotazioni) {
		this.richiestePrenotazioni = richiestePrenotazioni;
	}

	public boolean isRichiestePrenotazioni() {
		return richiestePrenotazioni;
	}

	//Fine modifica del 30/03/2010 almaviva

	public void clearDatiDocumento() {
		this.codBibInv = "";
		this.codSerieInv = "";
		this.codInvenInv = "";

		this.codBibDocLett = "";
		this.tipoDocLett = "";
		this.codDocLet = "";
		this.progrEsempDocLet = "";
	}

	public boolean isUtenteLettore() {
		return utenteLettore;
	}

	public void setUtenteLettore(boolean utenteLettore) {
		this.utenteLettore = utenteLettore;
	}

	public String getRfidChiaveInventario() {
		return rfidChiaveInventario;
	}

	public void setRfidChiaveInventario(String rfidChiaveInventario) {
		this.rfidChiaveInventario = rfidChiaveInventario;
	}

	public boolean isEscludiSollecitiProrogati() {
		return escludiSollecitiProrogati;
	}

	public void setEscludiSollecitiProrogati(boolean includiSollecitiProrogati) {
		this.escludiSollecitiProrogati = includiSollecitiProrogati;
	}

	public FiltroCollocazioneVO getFiltroColl() {
		return filtroColl;
	}

	public void setFiltroColl(FiltroCollocazioneVO coll) {
		this.filtroColl = coll;
	}

	public boolean isIncludiPrenotazioniPosto() {
		return includiPrenotazioniPosto;
	}

	public void setIncludiPrenotazioniPosto(boolean includiPrenotazioniPosto) {
		this.includiPrenotazioniPosto = includiPrenotazioniPosto;
	}

}
