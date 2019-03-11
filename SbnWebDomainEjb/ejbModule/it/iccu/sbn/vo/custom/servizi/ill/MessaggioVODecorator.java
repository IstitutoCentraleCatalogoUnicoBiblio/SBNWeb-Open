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
package it.iccu.sbn.vo.custom.servizi.ill;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.sql.Timestamp;
import java.util.List;

public class MessaggioVODecorator extends MessaggioVO {

	private static final long serialVersionUID = 1977804256813163848L;

	private String descrizioneStatoRichiesta;
	private String descrizioneCondizione;

	private final List<TB_CODICI> listaTipoCondizione;

	public MessaggioVODecorator(MessaggioVO msg) throws Exception {
		super(msg);
		setDescrizioneStatoRichiesta(getStato());
		setDescrizioneCondizione(msg.getCondizione());
		listaTipoCondizione = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_CONDIZIONE_ILL);
	}

	public String getDescrizioneStatoRichiesta() {
		return descrizioneStatoRichiesta;
	}

	private void setDescrizioneStatoRichiesta(String stato) {
		try {
			descrizioneStatoRichiesta = CodiciProvider.cercaDescrizioneCodice(
					stato, CodiciType.CODICE_STATO_RICHIESTA_ILL,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			descrizioneStatoRichiesta = stato;
		}
	}

	public String getDescrizioneCondizione() {
		return descrizioneCondizione;
	}

	public void setDescrizioneCondizione(String cond) {
		try {
			descrizioneCondizione = CodiciProvider.cercaDescrizioneCodice(
					cond, CodiciType.CODICE_TIPO_CONDIZIONE_ILL,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			descrizioneCondizione = cond;
		}
	}

	public String getDataMessaggioStr() {
		Timestamp dataMessaggio = getDataMessaggio();
		return (dataMessaggio == null ? null : DateUtil.formattaData(dataMessaggio.getTime()));
	}

	public List<TB_CODICI> getListaTipoCondizione() {
		return listaTipoCondizione;
	}

	public String getIsil() {
		switch(getTipoInvio()) {
		case INVIATO:
			return getRuolo() == RuoloBiblioteca.FORNITRICE ? getRequesterId() : getResponderId();
		case RICEVUTO:
			return getRuolo() == RuoloBiblioteca.FORNITRICE ? getRequesterId() : getResponderId();
		}

		return "";
	}

}
