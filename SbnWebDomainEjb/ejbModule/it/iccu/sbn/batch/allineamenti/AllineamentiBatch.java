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
package it.iccu.sbn.batch.allineamenti;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti.ScaricaFileAllineamentiDaIndice;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.LocalizzazioneMassivaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import java.io.Serializable;
import java.net.InetAddress;

import javax.transaction.UserTransaction;

public class AllineamentiBatch implements SchedulableBatchExecutor {

	private AmministrazioneGestioneCodici codici;
	private ServiziBibliografici srvBib;

	private AmministrazioneGestioneCodici getCodici() throws Exception {

		if (codici != null)
			return codici;

		this.codici = DomainEJBFactory.getInstance().getCodiciBMT();

		return codici;
	}

	private ServiziBibliografici getServiziBibliografici() throws Exception {

		if (srvBib != null)
			return srvBib;

		this.srvBib = DomainEJBFactory.getInstance().getSrvBibliografica();

		return srvBib;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx)
			throws Exception {

		String cdAttivita = params.getCodAttivita();
		if (ValidazioneDati.in(cdAttivita,
				CodiciAttivita.getIstance().ALLINEAMENTI_1032,
				CodiciAttivita.getIstance().CATTURA_MASSIVA,
				CodiciAttivita.getIstance().FUSIONE_MASSIVA)) {

			//almaviva5_20140626 #5589 #5595
			return getServiziBibliografici().allineamenti(params, log);
		}

		//almaviva5_20140204 evolutive google3
		if (cdAttivita.equals(CodiciAttivita.getIstance().LOCALIZZAZIONE_MASSIVA)) {
			LocalizzazioneMassivaVO localizzazioneMassivaVO = (LocalizzazioneMassivaVO) params;
			return getServiziBibliografici().localizzazioneMassiva(localizzazioneMassivaVO, log);
		}

		//almaviva5_20161004 evolutive oclc
		if (cdAttivita.equals(CodiciAttivita.getIstance().IMPORTA_RELAZIONI_BID_ALTRO_ID)) {
			ImportaLegamiBidAltroIdVO importaLegamiBidOcnVO = (ImportaLegamiBidAltroIdVO) params;
			return getServiziBibliografici().importaLegamiBidOcn(importaLegamiBidOcnVO, log);
		}

		//almaviva5_20171117 salvataggio file allineamento su cartella server
		if (cdAttivita.equals(CodiciAttivita.getIstance().ALLINEAMENTI_SALVA_XML_INDICE)) {
			ScaricaFileAllineamentiDaIndice scarica = new ScaricaFileAllineamentiDaIndice();
			return scarica.execute((AllineaVO) params, log);
		}

		return null;
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {
		return true;
	}

	public void setStart(ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {

		SchedulableBatchVO sb = (SchedulableBatchVO) params.getPayload();
		if (sb == null)
			return;

		TB_CODICI cod = getCodici().caricaCodice(sb.getJobName(), CodiciType.CODICE_BATCH_PIANIFICABILE);
		sb = new SchedulableBatchVO(cod);

		sb.setLatestStart(DaoManager.now() );

		getCodici().salvaTabellaCodici(sb.getConfig(), false);


	}

	public void setEnd(ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo output) throws Exception {

		SchedulableBatchVO sb = (SchedulableBatchVO) params.getPayload();
		if (sb == null)
			return;

		TB_CODICI cod = getCodici().caricaCodice(sb.getJobName(), CodiciType.CODICE_BATCH_PIANIFICABILE);
		sb = new SchedulableBatchVO(cod);

		String stato = output.getStato();
		if (!ValidazioneDati.equals(stato, ConstantsJMS.STATO_OK))
			return;

		sb.setLatestSuccessfulEnd(DaoManager.now());

		getCodici().salvaTabellaCodici(sb.getConfig(), false);
	}

	public ParametriRichiestaElaborazioneDifferitaVO buildActivationParameters(
			SchedulableBatchVO params, Serializable... otherParams)	throws Exception {

		String userId = params.getUser();
		String codPolo = userId.substring(0, 3);
		String codBib = userId.substring(3, 6);
		String user = userId.substring(6);

		// INIZIO PRENOTAZIONE ALLINEAMENTO
		AllineaVO allinea = new AllineaVO();
		allinea.setPayload(params);
		allinea.setCodPolo(codPolo);
		allinea.setCodBib(codBib);
		allinea.setUser(user);
		allinea.setCodAttivita(CodiciAttivita.getIstance().ALLINEAMENTI_1032);

		String downloadPath = StampeUtil.getBatchFilesPath();
		allinea.setDownloadPath(downloadPath);
		allinea.setDownloadLinkPath("/"); // eliminato

		String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, user, InetAddress.getLocalHost());
		allinea.setTicket(ticket);
		allinea.setTipoMaterialeDaAllineare("*");
		allinea.setDataAllineaDa(null);
		allinea.setDataAllineaA(null);
		allinea.setIdFileAllineamenti(null);

		//almaviva5_20150330
		if (ValidazioneDati.isFilled(otherParams)) {
			String idFile = (String) otherParams[0];
			allinea.setIdFileAllineamenti(idFile);
		}

		return allinea;
	}

}
