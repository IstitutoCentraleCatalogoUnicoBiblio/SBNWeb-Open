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
package it.iccu.sbn.vo.custom.servizi.sale;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.servizi.SaleUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;

public class PrenotazionePostoDecorator extends PrenotazionePostoVO {

	private static final long serialVersionUID = 7979536831368043419L;

	private final static SimpleDateFormat _formatter = new SimpleDateFormat("HH:mm");

	private String descrizioneStato;
	private String descrizioneCatMediazione;

	private StatoPrenotazionePosto2 statoDinamico;

	public PrenotazionePostoDecorator(PrenotazionePostoVO pp) {
		super(pp);
		setStato(pp.getStato());
		try {
			this.descrizioneCatMediazione = CodiciProvider
					.getDescrizioneCodiceSBN(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE, getCatMediazione());

			Collections.sort(this.getAltriUtenti(), UtenteBaseVO.ORDINAMENTO_COGNOME_NOME);
		} catch (Exception e) {	}
	}

	public String getDataInizio() {
		return DateUtil.formattaData(getTs_inizio());
	}

	public String getDataFine() {
		return DateUtil.formattaData(getTs_fine());
	}

	public String getOrarioInizio() {
		return _formatter.format(getTs_inizio());
	}

	public String getOrarioFine() {
		return _formatter.format(getTs_fine());
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public String getDescrizionePosto() {
		PostoSalaVO posto = getPosto();
		return String.format("%s / %d", posto.getSala().getDescrizione(), posto.getNum_posto() );
	}

	@Override
	public void setStato(StatoPrenotazionePosto stato) {
		super.setStato(stato);
		try {
			statoDinamico = SaleUtil.getStatoDinamico(this.getStato(), this.getTs_fine(), creationTime);
			this.descrizioneStato = CodiciProvider.cercaDescrizioneCodice(statoDinamico.getStato(),
					CodiciType.CODICE_STATO_PRENOTAZIONE_POSTO, CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			return;
		}
	}

	public String getDescrizioneCatMediazione() {
		return descrizioneCatMediazione;
	}

	public StatoPrenotazionePosto2 getStatoDinamico() {
		return statoDinamico;
	}

	public String getDescrizionePrenotazione() {
		StringBuilder buf = new StringBuilder(64);
		buf.append("n. ").append(getId_prenotazione())
			.append(" del ")
			.append(DateUtil.formattaData(getTs_inizio())).append("; ")
			.append(getDescrizionePosto()).append("; ")
			.append(getOrarioInizio()).append(" - ").append(getOrarioFine());

		return buf.toString();
	}

}
