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

import static it.iccu.sbn.util.Constants.Servizi.Movimenti.NUMBER_FORMAT_PREZZI;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.DocumentoNonSbnDecorator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatiRichiestaILLDecorator extends DatiRichiestaILLVO {

	private static final long serialVersionUID = 638983504705059560L;

	private String descrizioneServizio;
	private String descrizioneStatoRichiesta;
	private String descrizioneSupporto;
	private String descrizioneModErogazione;

	private String docDeliveryLink;

	public DatiRichiestaILLDecorator(DatiRichiestaILLVO dati) throws Exception {
		super(dati);

		//almaviva5_20150219
		DocumentoNonSbnVO doc = this.getDocumento();
		if (doc != null)
			setDocumento(new DocumentoNonSbnDecorator(doc));

		setDescrizioneServizio(getServizio());
		setDescrizioneStatoRichiesta(getCurrentState());
		setDescrizioneSupporto(getCd_supporto());
		setDescrizioneModErogazione(getCod_erog());

		List<MessaggioVO> messaggi = getMessaggio();
		int size = size(messaggi);
		if (size > 0) {
			List<MessaggioVO> decorated = new ArrayList<MessaggioVO>(size);
			for (MessaggioVO msg : messaggi)
				decorated.add(new MessaggioVODecorator(msg));

			this.setMessaggio(decorated);
		}

		//link per carico/scarico file riproduzione
		docDeliveryLink = ILLConfiguration2.getInstance().generaUrlDocumentDelivery(dati);
	}

	@Override
	public int getRepeatableId() {
		return getIdRichiestaILL();
	}

	public String getDescrizioneServizio() {
		return descrizioneServizio;
	}

	private void setDescrizioneServizio(String servizio) {
		try {
			descrizioneServizio = CodiciProvider.cercaDescrizioneCodice(
					servizio, CodiciType.CODICE_TIPO_SERVIZIO_ILL,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			descrizioneServizio = servizio;
		}
	}

	@Override
	public void setServizio(String servizio) {
		super.setServizio(servizio);

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

	public String getDescrizioneSupporto() {
		return descrizioneSupporto;
	}

	public void setDescrizioneSupporto(String supp) {
		try {
			descrizioneSupporto = CodiciProvider.cercaDescrizioneCodice(
					supp, CodiciType.CODICE_SUPPORTO_COPIA_ILL,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			descrizioneSupporto = supp;
		}
	}

	public String getDescrizioneModErogazione() {
		return descrizioneModErogazione;
	}

	public void setDescrizioneModErogazione(String mod_erog) {
		try {
			descrizioneModErogazione = CodiciProvider.cercaDescrizioneCodice(
					mod_erog, CodiciType.CODICE_MODALITA_EROGAZIONE_ILL,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			descrizioneModErogazione = mod_erog;
		}
	}

	public String getSegnatura() {
		if (isInventarioPresente()) {
			InventarioTitoloVO inv = getInventario();
			return ServiziUtil.formattaSegnaturaCollocazione(inv.getCodSez(),
					inv.getCodLoc(),
					inv.getSpecLoc(),
					inv.getSeqColl(),
					inv.getNatura().charAt(0),
					null);
		} else
			return getDocumento().getSegnatura();
	}

	public String getTitolo() {
		if (isInventarioPresente())
			return getInventario().getIsbd();
		else
			return getDocumento().getTitolo();
	}

	public String getDataInizioString() {
		return (getDataInizio() == null ? null : DateUtil.formattaDataOra(getDataInizio().getTime()));
	}

	public String getDataFineString() {
		return (getDataFine() == null ? null : DateUtil.formattaDataOra(getDataFine().getTime()));
	}

	public String getDataScadenzaString() {
		return (getDataScadenza() == null ? null : DateUtil.formattaData(getDataScadenza()));
	}

	public String getDataDesiderataString() {
		return (getDataDesiderata() == null ? null : DateUtil.formattaData(getDataDesiderata()));
	}

	public String getDataMassimaString() {
		return (getDataMassima() == null ? null : DateUtil.formattaData(getDataMassima()));
	}

	public String getDescrizioneStatoRichiestaLoc() {
		try {
			MovimentoVO mov = getMovimento();
			if (mov != null) {
				return CodiciProvider.cercaDescrizioneCodice(
						mov.getCodAttivita(), CodiciType.CODICE_ATTIVITA_ITER,
						CodiciRicercaType.RICERCA_CODICE_SBN);
			}
		} catch (Exception e) {
			return "";
		}

		return "";
	}

	public String getDenominazioneBibFornitrice() {
		List<BibliotecaVO> fornitrici = getBibliotecheFornitrici();
		if (isFilled(fornitrici))
			return fornitrici.get(0).getNom_biblioteca();

		return "";
	}

	public String getImportoStr() {
		Number importo = getImporto();
		if (isFilled(importo))
			return ValidazioneDati.getStringFromDouble(importo.doubleValue(), NUMBER_FORMAT_PREZZI, Locale.getDefault());

		return "0";
	}

	public void getImportoStr(String value) {
		value = trimOrEmpty(value);
		if (isFilled(value))
			try {
				setImporto(ValidazioneDati.getDoubleFromString(value, NUMBER_FORMAT_PREZZI, Locale.getDefault()));
			} catch (Exception e) {
				setImporto(0);
			}
	}

	public String getCostoMaxStr() {
		Number costoMax = getCostoMax();
		if (isFilled(costoMax))
			return ValidazioneDati.getStringFromDouble(costoMax.doubleValue(), NUMBER_FORMAT_PREZZI, Locale.getDefault());

		return "0";
	}

	public void setCostoMaxStr(String value) {
		value = trimOrEmpty(value);
		if (isFilled(value))
			try {
				setCostoMax(ValidazioneDati.getDoubleFromString(value, NUMBER_FORMAT_PREZZI, Locale.getDefault()));
			} catch (Exception e) {
				setCostoMax(0);
			}
	}

	public String getDocDeliveryLink() {
		return docDeliveryLink;
	}

	public String getUltimaAttivita() {
		MovimentoVO mov = this.getMovimento();
		if (mov == null)
			return getDescrizioneStatoRichiesta();

		Timestamp tsVarILL = this.getUltimoCambioStato();
		Timestamp tsVarLoc = mov.getTsVar();
		if (tsVarILL.after(tsVarLoc))
			return getDescrizioneStatoRichiesta();
		else
			return getDescrizioneStatoRichiestaLoc();
	}

}
