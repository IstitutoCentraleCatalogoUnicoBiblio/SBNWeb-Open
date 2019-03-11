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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti;

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAllAuthorityDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAllineamentiIndicePoloDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAutoriDao;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneTitoliDao;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.FusioneMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

public class Allineamenti {

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;

	final UserTransaction tx;

	public Allineamenti(FactorySbn indice, FactorySbn polo, SbnUserType user, UserTransaction tx) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
		this.tx = tx;
	}

	public ElaborazioniDifferiteOutputVo allinea(String prefissoOutput, AllineaVO allineaVo, BatchLogWriter blw) {
		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Allineamenti Elaborazioni differite - Inizio di Allineamenti.java metodo Allinea");
		blw.logWriteLine("Sone le " + DateUtil.now() );
		blw.logWriteLine("++============================================================================++");

		Logger log = blw.getLogger();
		log.debug("Test log allineamenti Gest. bibliografica");

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(allineaVo);

		// Crea lista dei file da scaricare
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		String filename = prefissoOutput + ".htm";

		SbnGestioneAllineamentiIndicePoloDao allineamentiIndicePoloDao = new SbnGestioneAllineamentiIndicePoloDao(indice, polo, user);
//
//		AreaDatiAllineamentoPoloIndiceVO allineamentoPoloIndiceVO = new AreaDatiAllineamentoPoloIndiceVO();
//		allineamentoPoloIndiceVO.setTipoMaterialeDaAllineare(allineaVo.getTipoMaterialeDaAllineare());
//		allineamentoPoloIndiceVO.setDataAllineaDa(allineaVo.getDataAllineaDa());
//		allineamentoPoloIndiceVO.setDataAllineaA(allineaVo.getDataAllineaA());
//		allineamentoPoloIndiceVO.setIdFileAllineamenti(allineaVo.getIdFileAllineamenti());

//		AllineaVO allineamentoPoloIndiceReturnVO = allineamentiIndicePoloDao.richiediListaAllineamenti(allineamentoPoloIndiceVO);


		AllineaVO allineamentoPoloIndiceReturnVO = allineamentiIndicePoloDao.richiediListaAllineamenti(allineaVo);

		BufferedWriter w = null;
		try {
			FileOutputStream out = new FileOutputStream(allineaVo.getDownloadPath() + File.separator + filename);
			w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			TracciatoStampaOutputAllineamentoVO outputAllineamentoVO;

			List<TracciatoStampaOutputAllineamentoVO> logAnalitico = allineamentoPoloIndiceReturnVO.getLogAnalitico();
			int size = logAnalitico.size();

			for (int i = 0; i < size; i++) {
				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO();
				outputAllineamentoVO = logAnalitico.get(i);
				w.write(outputAllineamentoVO.getStringaToPrint());
			}

		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} finally {
			FileUtil.close(w);
		}

		// listaDownload.add(new DownloadVO(filename,
		// allineaVo.getDownloadPath()+filename));
		listaDownload.add(new DownloadVO(filename, allineaVo.getDownloadLinkPath() + filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.now() );

		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Allineamenti Elaborazioni differite - Fine di Allineamenti.java metodo Allinea");
		blw.logWriteLine("Sone le " + DateUtil.getDate() + DateUtil.getTime());
		blw.logWriteLine("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}


	public ElaborazioniDifferiteOutputVo catturaMassivaBatch(String prefissoOutput, CatturaMassivaBatchVO catturaMassivaBatchVO, BatchLogWriter blw) {


		// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
		if (catturaMassivaBatchVO.isCallInterrogPerCreazListe()) {
			return interrogMassivaBatch(prefissoOutput, catturaMassivaBatchVO, blw);
		}


		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Cattura Massiva Elaborazioni differite - Inizio di Allineamenti.java metodo catturaMassivaBatch");
		blw.logWriteLine("Sone le " + DateUtil.now() );
		blw.logWriteLine("++============================================================================++");

		Logger log = blw.getLogger();
		log.debug("Test log catturaMassiva Gest. bibliografica");

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(catturaMassivaBatchVO);



		// Crea lista dei file da scaricare
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		String filename = prefissoOutput + ".htm";

		SbnGestioneAllineamentiIndicePoloDao allineamentiIndicePoloDao = new SbnGestioneAllineamentiIndicePoloDao(
				indice, polo, user);

		allineamentiIndicePoloDao.catturaMassivaBatch(catturaMassivaBatchVO);

		BufferedWriter w = null;
		try {
			FileOutputStream out = new FileOutputStream(catturaMassivaBatchVO.getDownloadPath() + File.separator + filename);
			w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			TracciatoStampaOutputAllineamentoVO outputAllineamentoVO;

			for (int i = 0; i < catturaMassivaBatchVO
					.getLogAnalitico().size(); i++) {
				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO();
				outputAllineamentoVO = catturaMassivaBatchVO
						.getLogAnalitico().get(i);
				w.write(outputAllineamentoVO.getStringaToPrint());
			}

		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} finally {
			FileUtil.close(w);
		}

		listaDownload.add(new DownloadVO(filename, catturaMassivaBatchVO.getDownloadLinkPath() + filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.now() );

		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Cattura Massiva Elaborazioni differite - Fine di Allineamenti.java metodo catturaMassivaBatch");
		blw.logWriteLine("Sone le " + DateUtil.getDate() + DateUtil.getTime());
		blw.logWriteLine("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}


	public ElaborazioniDifferiteOutputVo fusioneMassivaBatch(String prefissoOutput, FusioneMassivaBatchVO fusioneMassivaBatchVO, BatchLogWriter blw) {

		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Fusione Massiva Elaborazioni differite - Inizio di Allineamenti.java metodo fusioneMassivaBatch");
		blw.logWriteLine("Sone le " + DateUtil.now() );
		blw.logWriteLine("++============================================================================++");

		Logger log = blw.getLogger();

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(fusioneMassivaBatchVO);

		// Crea lista dei file da scaricare
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		String filename = prefissoOutput + ".htm";

		SbnGestioneAllineamentiIndicePoloDao allineamentiIndicePoloDao = new SbnGestioneAllineamentiIndicePoloDao(
				indice, polo, user);

		allineamentiIndicePoloDao.fusioneMassivaBatchVO(fusioneMassivaBatchVO);

		BufferedWriter w = null;
		try {
			FileOutputStream out = new FileOutputStream(fusioneMassivaBatchVO.getDownloadPath() + File.separator + filename);
			w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

			w.append("<html>");
			w.append("<head>");
			w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
			w.append("<title>Fusione Massiva</title>");
			w.append("</head>");
			w.append("<body>");
			w.append("<table>");

			for (int i = 0; i < fusioneMassivaBatchVO.getLogAnalitico().size(); i++) {
				TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = fusioneMassivaBatchVO.getLogAnalitico().get(i);
				w.write(outputAllineamentoVO.getStringaToPrint());
			}

			w.append("</table>");
			w.append("</body>");
			w.append("</html>");

		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} finally {
			FileUtil.close(w);
		}

		listaDownload.add(new DownloadVO(filename, fusioneMassivaBatchVO.getDownloadLinkPath() + filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.now() );

		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Fusione Massiva Elaborazioni differite - Fine di Allineamenti.java metodo fusioneMassivaBatch");
		blw.logWriteLine("Sone le " + DateUtil.getDate() + DateUtil.getTime());
		blw.logWriteLine("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}

	// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
	public ElaborazioniDifferiteOutputVo interrogMassivaBatch(String prefissoOutput, CatturaMassivaBatchVO catturaMassivaBatchVO, BatchLogWriter log) {

		log.logWriteLine("++============================================================================++");
		log.logWriteLine("Interrogazione Massiva Per creazione Liste Elaborazioni differite - Inizio di Allineamenti.java metodo interrogMassivaBatch");
		log.logWriteLine("Sone le " + DateUtil.now() );
		log.logWriteLine("++============================================================================++");

		Logger logger = log.getLogger();
		logger.debug("Test log interrogMassivaBatch Gest. bibliografica");

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(catturaMassivaBatchVO);

		// Crea lista dei file da scaricare
		String filename = prefissoOutput + ".txt";

		int sizeListaId = ValidazioneDati.size(catturaMassivaBatchVO.getListaBidDaCatturare());
		if (sizeListaId < 1) {
			logger.error("Lista bid vuota.");
			return elaborazioniDifferiteOutputVo;
		}

		List<String> listaCoppieResult = new ArrayList<String>(sizeListaId);


		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPassInterrogazione = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO("");

		areaDatiPassInterrogazione.setRicercaPolo(true);
		areaDatiPassInterrogazione.setRicercaIndice(false);

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO rispostaInterrogazione;
		AreaTabellaOggettiDaCondividereVO areaDatiPassConfronto;

		SbnGestioneTitoliDao gestioneTitoli = new SbnGestioneTitoliDao(indice, polo, user);
		SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(indice, polo, user);
		SinteticaTitoliView sinteticaTitoliView;
		SinteticaAutoriView sinteticaAutoriView;

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);


		String stringaCoppia = "";
		String specialChar = "|";
		String idPartenza = "";
		String ls = System.getProperty("line.separator");


		for (int iListaId = 0; iListaId < sizeListaId; iListaId++) {

			rispostaInterrogazione = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

			idPartenza = catturaMassivaBatchVO.getListaBidDaCatturare().get(iListaId);
			areaDatiPassInterrogazione.setBidRicerca(idPartenza);
			if (idPartenza.substring(3, 4).equals("V")) {
				rispostaInterrogazione = gestioneAutori.creaRichiestaAnaliticoAutorePerVid(areaDatiPassInterrogazione);
			} else {
				rispostaInterrogazione = gestioneTitoli.creaRichiestaAnaliticoTitoliPerBID(areaDatiPassInterrogazione);
			}

			String codErr = rispostaInterrogazione.getCodErr();
			if (codErr.equals("3001")) {
				logger.error(String.format("id di polo %s non trovato.", idPartenza));
				continue;
			} else if (!codErr.equals("0000") && !codErr.equals("")) {
				logger.error(String.format(
						"id di polo %s. Errore protocollo: %s - %s",
						idPartenza, codErr,
						rispostaInterrogazione.getTestoProtocollo()));
				continue;
			}


			areaDatiPassConfronto = new AreaTabellaOggettiDaCondividereVO();
			areaDatiPassConfronto.setCallBatch(true);
			areaDatiPassConfronto.setTipoRicerca("PRIMARIC");

			if (idPartenza.substring(3, 4).equals("V")) {
				areaDatiPassConfronto.setDettAutGenVO(rispostaInterrogazione.getTreeElementViewTitoli().getAreaDatiDettaglioOggettiVO().getDettaglioAutoreGeneraleVO());
				areaDatiPassConfronto = gestioneAllAuthority.ricercaAutorePerCondividi(areaDatiPassConfronto);
			} else {
				areaDatiPassConfronto.setDettTitComVO(rispostaInterrogazione.getTreeElementViewTitoli().getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO());
				areaDatiPassConfronto.setNatura(rispostaInterrogazione.getTreeElementViewTitoli().getNatura());
				areaDatiPassConfronto.setDescrizionePerRicerca(rispostaInterrogazione.getTreeElementViewTitoli().getDescription());
				areaDatiPassConfronto = gestioneAllAuthority.ricercaDocumentoPerCondividi(areaDatiPassConfronto);
			}


			if (areaDatiPassConfronto.getListaSintetica() != null) {
				int sizeListaSint = areaDatiPassConfronto.getListaSintetica().size();
				for (int iListaSint = 0; iListaSint < sizeListaSint; iListaSint++) {

					if (idPartenza.substring(3, 4).equals("V")) {
						sinteticaAutoriView = (SinteticaAutoriView) areaDatiPassConfronto.getListaSintetica().get(iListaSint);
						if (sinteticaAutoriView.getVid().equals(idPartenza)) {
							stringaCoppia = "U" + specialChar + idPartenza + specialChar + sinteticaAutoriView.getVid() + ls;//"<br>";
						} else {
							stringaCoppia = "S" + specialChar + idPartenza + specialChar + sinteticaAutoriView.getVid() + ls;//"<br>";
						}
					} else {
						sinteticaTitoliView = (SinteticaTitoliView) areaDatiPassConfronto.getListaSintetica().get(iListaSint);
						if (sinteticaTitoliView.getBid().equals(idPartenza)) {
							stringaCoppia = "U" + specialChar + idPartenza + specialChar + sinteticaTitoliView.getBid() + ls;//"<br>";
						} else {
							stringaCoppia = "S" + specialChar + idPartenza + specialChar + sinteticaTitoliView.getBid() + ls;//"<br>";
						}
					}
					listaCoppieResult.add(stringaCoppia);
				}
			} else {
				stringaCoppia = "N" + specialChar + idPartenza + ls;//"<br>";
				listaCoppieResult.add(stringaCoppia);
			}
		}

		if (ValidazioneDati.isFilled(listaCoppieResult)) {
			BufferedWriter w = null;
			try {
				FileOutputStream out = new FileOutputStream(catturaMassivaBatchVO.getDownloadPath() + File.separator + filename);
				w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				TracciatoStampaOutputAllineamentoVO outputAllineamentoVO;
				for (int i = 0; i < listaCoppieResult.size(); i++) {
					outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO(listaCoppieResult.get(i));
					w.write(outputAllineamentoVO.getStringaToPrint());
				}

			} catch (FileNotFoundException e) {
				logger.error("", e);
			} catch (IOException e) {
				logger.error("", e);
			} finally {
				FileUtil.close(w);
			}

			elaborazioniDifferiteOutputVo.addDownload(filename, catturaMassivaBatchVO.getDownloadLinkPath() + filename);
		}

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.now() );

		log.logWriteLine("++============================================================================++");
		log.logWriteLine("Interrogazione Massiva Per creazione Liste Elaborazioni differite - Fine di Allineamenti.java metodo interrogMassivaBatch");
		log.logWriteLine("Sone le " + DateUtil.getDate() + DateUtil.getTime());
		log.logWriteLine("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}

}
