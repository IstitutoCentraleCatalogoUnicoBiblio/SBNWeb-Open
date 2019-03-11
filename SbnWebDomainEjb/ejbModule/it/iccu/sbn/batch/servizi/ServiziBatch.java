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
package it.iccu.sbn.batch.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

import javax.transaction.UserTransaction;

public class ServiziBatch implements SchedulableBatchExecutor {

	private AmministrazioneBiblioteca amministrazioneBiblioteca;
	private AmministrazioneGestioneCodici codici;

	private ServiziBMT getEjb() throws Exception {
		return DomainEJBFactory.getInstance().getServiziBMT();
	}

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;
	}

	private AmministrazioneGestioneCodici getCodici() throws Exception {

		if (codici != null)
			return codici;

		this.codici = DomainEJBFactory.getInstance().getCodiciBMT();

		return codici;
	}

	public boolean validateInput(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx)
			throws Exception {

		String codAttivita = params.getCodAttivita();
		if (ValidazioneDati.equals(CodiciAttivita.getIstance().SERVIZI_SOLLECITI, codAttivita))
			return eseguiBatchSolleciti(params, log);

		if (ValidazioneDati.equals(CodiciAttivita.getIstance().SRV_RIFIUTO_PRENOTAZIONI_SCADUTE, codAttivita))
			return rifiutaPrenotazioniScadute(params, log);

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		log.getLogger().error("Codice Attività '" + codAttivita + "' non gestito");
		output.setStato(ConstantsJMS.STATO_ERROR);
		return output;
	}

	private ElaborazioniDifferiteOutputVo eseguiBatchSolleciti(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log)
			throws Exception {

		return getEjb().eseguiBatchSolleciti((ParametriBatchSollecitiVO)params, log);
	}

	private ElaborazioniDifferiteOutputVo rifiutaPrenotazioniScadute(
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log)
			throws Exception {

		return getEjb().rifiutaPrenotazioniScadute((RifiutaPrenotazioniScaduteVO) params, log);
	}

	public void setStart(ParametriRichiestaElaborazioneDifferitaVO params)
			throws Exception {
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

		sb.setLatestSuccessfulEnd(DaoManager.now() );

		getCodici().salvaTabellaCodici(sb.getConfig(), false);
	}

	public ParametriRichiestaElaborazioneDifferitaVO buildActivationParameters(
			SchedulableBatchVO params, Serializable... otherParams)
			throws Exception {

		String codAttivita = params.getCd_attivita();
		if (ValidazioneDati.equals(CodiciAttivita.getIstance().SERVIZI_SOLLECITI, codAttivita))
			return buildParametriSolleciti(params, otherParams);

		if (ValidazioneDati.equals(CodiciAttivita.getIstance().SRV_RIFIUTO_PRENOTAZIONI_SCADUTE, codAttivita)) {
			String userId = params.getUser();

			String codPolo = userId.substring(0, 3);
			String codBib = userId.substring(3, 6);
			String user = userId.substring(6);
			RifiutaPrenotazioniScaduteVO richiesta = ServiziUtil.buildRifiutaPrenotazioniScaduteVO(codPolo, codBib, user);
			richiesta.setPayload(params);
			String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, user, InetAddress.getLocalHost());
			richiesta.setTicket(ticket);

			return richiesta;
		}

		return null;
	}

	private ParametriRichiestaElaborazioneDifferitaVO buildParametriSolleciti(
			SchedulableBatchVO params, Serializable[] otherParams) throws Exception {


		ParametriBatchSollecitiVO richiesta = new ParametriBatchSollecitiVO();
		richiesta.setPayload(params);

		String userId = params.getUser();

		String codPolo = userId.substring(0, 3);
		String codBib = userId.substring(3, 6);
		String user = userId.substring(6);

		richiesta.setCodPolo(codPolo);
		richiesta.setCodBib(codBib);
		richiesta.setUser(user);
		String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, user, InetAddress.getLocalHost());
		richiesta.setTicket(ticket);

		richiesta.setDataScadenza(DaoManager.now() );
		richiesta.setDescrizioneBiblioteca(getAmministrazioneBiblioteca().getBiblioteca(codPolo, codBib).getNom_biblioteca());
		richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);

		List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);
		if (!ValidazioneDati.isFilled(modelli))
			throw new ValidationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);

		String jrxml = modelli.get(0).getJrxml();

		String basePath = File.separator;//this.servlet.getServletContext().getRealPath(File.separator);
		// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
		String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
		richiesta.setTemplate(pathJrxml);
		richiesta.setTipoStampa(TipoStampa.PDF.name());

		return richiesta;

	}

}
