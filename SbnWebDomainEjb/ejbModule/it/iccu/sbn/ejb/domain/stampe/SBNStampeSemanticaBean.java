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
package it.iccu.sbn.ejb.domain.stampe;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;



public class SBNStampeSemanticaBean extends SBNStampeBase {

	private static Logger log = Logger.getLogger(SBNStampeSemanticaBean.class);

	private WeakReference<Semantica> serviziSemant;

	public Semantica getServiziSemantica() throws EJBException {

		if (serviziSemant != null && serviziSemant.get() != null)
			return serviziSemant.get();

		try {
			this.serviziSemant = new WeakReference<Semantica>(DomainEJBFactory.getInstance().getSemanticaBMT() );

			return serviziSemant.get();

		} catch (Exception e) {
			throw new EJBException(e);
		}
	}



	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		return;
	}

	public void ejbRemove() throws EJBException {
		return;
	}

	public void ejbCreate() {
		log.info("creato ejb");
		return;
	}

	public Object elabora(StampaVo stampaVO, BatchLogWriter logger)	throws Exception {

		String nameFile = "";
		getDownloadFileName().clear();

		String pathDownload = StampeUtil.getBatchFilesPath();
		String utente = null;
		String fileJrxml = null;
		InputStream streamRichiestaStampa;

		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		fileJrxml = stampaVO.getTemplate();

		// Stampa differita
		ElaborazioniDifferiteOutputVo outputVO = new ElaborazioniDifferiteOutputVo(stampaVO);

		Logger _log = logger.getLogger();	// log automatico batch

		List<ParametriStampaVO> listaOutput = new ArrayList<ParametriStampaVO>();
		if (ValidazioneDati.equals(stampaVO.getCodAttivita(), CodiciAttivita.getIstance().STAMPA_SOGGETTARIO))
			listaOutput.add(eseguiStampaSoggettario(stampaVO, logger) );
		if (ValidazioneDati.equals(stampaVO.getCodAttivita(), CodiciAttivita.getIstance().STAMPA_DESCRITTORI))
			listaOutput.add(eseguiStampaDescrittori(stampaVO, logger) );

		/*
		if (!ValidazioneDati.isFilled(listaOutput.get(0).getOutput()) ) {
			_log.warn("Nessun elemento trovato");
			outputVO.setStato(ConstantsJMS.STATO_OK);
			return outputVO;
		}
		*/
//		for (Object o : listaOutput)
//			_log.debug(o);

		try {
			String tipoStampa=stampaVO.getTipoStampa();
			streamRichiestaStampa = effettuaStampa(
					fileJrxml,
					tipoStampa,
					listaOutput);

			boolean listaEsistente = false;

			if (streamRichiestaStampa != null) {
				try {
					nameFile = stampaVO.getFirmaBatch() + "." + tipoStampa;
					this.scriviFile(utente, tipoStampa, streamRichiestaStampa, pathDownload, nameFile);
				}catch (Exception ef) {
					throw new Exception("Creazione del file"+ nameFile+"fallita");
				}
				String filename = "";
				for(int index = 0; index < getDownloadFileName().size(); index++){
					listaEsistente = true;
					filename = (getDownloadFileName().get(index));
					listaDownload.add(new DownloadVO(filename, filename));
				}
				if(!listaEsistente){
					throw new Exception("elaborazione fallita a causa di un errore nella creazione della lista dei file di cui fare il download");
				}
				outputVO.setDownloadList(listaDownload);
				outputVO.setStato(ConstantsJMS.STATO_OK);
			}

		}catch (Exception e) {
			_log.error("", e);
			outputVO.setStato(ConstantsJMS.STATO_ERROR);
		}

		return outputVO;

	}

	private ParametriStampaVO eseguiStampaDescrittori(StampaVo richiesta, BatchLogWriter logger) throws Exception {
		SemanticaDAO dao = new SemanticaDAO();
		SubReportVO stampaDescrittori = dao.stampaDescrittori((ParametriStampaDescrittoriVO) richiesta, logger);
		ParametriStampaDescrittoriVO pd = (ParametriStampaDescrittoriVO) richiesta.clone();
		pd.setSubReportDescrittori(stampaDescrittori);
		//decodifica cod sogg.
		String soggettario = CodiciProvider.cercaDescrizioneCodice(pd.getCodSoggettario(),
				CodiciType.CODICE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);
		String edizione = CodiciProvider.cercaDescrizioneCodice(pd.getEdizioneSoggettario(),
				CodiciType.CODICE_EDIZIONE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);

		if (ValidazioneDati.isFilled(edizione))
			soggettario += " (" + edizione + ')';

		pd.setCodSoggettario(soggettario);
		pd.setEdizioneSoggettario(edizione);

		return pd;
	}


	private ParametriStampaVO eseguiStampaSoggettario(StampaVo richiesta, BatchLogWriter logger) throws Exception {
		SemanticaDAO dao = new SemanticaDAO();

		SubReportVO stampaSoggettario = dao.stampaSoggettario((ParametriStampaSoggettarioVO) richiesta, logger);
		ParametriStampaSoggettarioVO ps = (ParametriStampaSoggettarioVO) richiesta.clone();
		ps.setSubReportSoggetti(stampaSoggettario);
		//decodifica cod sogg.
		String soggettario = CodiciProvider.cercaDescrizioneCodice(ps.getCodSoggettario(),
				CodiciType.CODICE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);
		String edizione = CodiciProvider.cercaDescrizioneCodice(ps.getEdizioneSoggettario(),
				CodiciType.CODICE_EDIZIONE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);

		if (ValidazioneDati.isFilled(edizione))
			soggettario += " (" + edizione + ')';

		ps.setCodSoggettario(soggettario);
		ps.setEdizioneSoggettario(edizione);

		return ps;
	}

	public StampaTerminiThesauroVO impostaThesauroDaStampare(ElementoStampaTerminiThesauroVO elemStampa, ParametriStampaTerminiThesauroVO parStaTerThes){

		boolean bapp=false;
		StampaTerminiThesauroVO stampa = new StampaTerminiThesauroVO();
		//String denominazione della biblioteca
		stampa.setDenBib(parStaTerThes.getDescrizioneBiblioteca());

		stampa.setDescThesauro(parStaTerThes.getDescrizioneThesauro());
		stampa.setDateInsDa(parStaTerThes.getTsIns_da());
		stampa.setDateInsA(parStaTerThes.getTsIns_a());
		stampa.setDateAggDa(parStaTerThes.getTsVar_da());
		stampa.setDateAggA(parStaTerThes.getTsVar_a());
		bapp=parStaTerThes.isStampaTitoli();
		stampa.setStampaTitoli(String.valueOf(bapp));//parStaTerThes.isStampaTitoli());//Boolean.valueOf(parStaTerThes.isStampaTitoli()));
		bapp=parStaTerThes.isStampaNote();
		stampa.setStampaStringaThes(String.valueOf(bapp));//Boolean.valueOf(parStaTerThes.isStampaNote()));
		bapp = parStaTerThes.isSoloTerminiBiblioteca();
		stampa.setThesauriBiblio(String.valueOf(bapp));//Boolean.valueOf(parStaTerThes.isSoloTerminiBiblioteca()));
		stampa.setRelazAltriTerm(String.valueOf(parStaTerThes.isStampaTerminiCollegati()));//Boolean.valueOf(parStaTerThes.isStampaTerminiCollegati()));
		stampa.setCodBib(parStaTerThes.getCodBib());
		stampa.setCodPolo(parStaTerThes.getCodPolo());

		stampa.setStampaNoteTitle(String.valueOf(false));//Boolean.valueOf(false));
		stampa.setStampaNoteThes(String.valueOf(false));//Boolean.valueOf(false));
		stampa.setRelazAltreForm(String.valueOf(false));//Boolean.valueOf(false));
		stampa.setStampaTermBiblio(String.valueOf(false));//Boolean.valueOf(false));
		stampa.setTermine(elemStampa.getTesto());
		stampa.setDid(elemStampa.getId());
		stampa.setNote(elemStampa.getNote());

		return stampa;
	}

}
