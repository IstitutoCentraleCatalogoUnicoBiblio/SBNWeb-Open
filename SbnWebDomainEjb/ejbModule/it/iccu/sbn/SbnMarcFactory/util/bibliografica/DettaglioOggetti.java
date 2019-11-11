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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO.Institution;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.Repertorio;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioClasseGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioLuogoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioSoggettoGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.DettaglioTermineThesauroGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.DettaglioAutoreGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.TreeElementViewAutori;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.DettaglioMarcaGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.TreeElementViewMarche;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioIncipitVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tr_bid_altroid;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.semantica.SemanticaUtil;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DettaglioOggetti {

	public static final int REPERTORIO_TUTTI = 0;

	UtilityCastor utilityCastor = new UtilityCastor();
	UtilityDate utilityDate = new UtilityDate();
	TitoloDAO dao = new TitoloDAO();

	private Codici codici;

	private static boolean showOCN;

	static {
		//almaviva5_20161005 evolutiva oclc
		try {
			showOCN = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.GB_CARICA_OCLC_CONTROL_NUMBER, "false"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DettaglioOggetti() {
		try {
			this.codici = DomainEJBFactory.getInstance().getCodici();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Manutenzione BUG MANTIS COLLAUDO 5016 almaviva2 26.06.2012
	// inserito il break negli switch relativi al tipo legame nelle classi di decodifica del dettaglio delle marche;


	public DettaglioTitoloCompletoVO getDettaglioTitolo(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegameInt) {

		String BID = nodoCorrente.getKey();
		String BIDRoot = "";
		if (nodoCorrente.getParent() != null) {
			BIDRoot = nodoCorrente.getParent().getKey();
		} else {
			BIDRoot = nodoCorrente.getKey();
		}

		DettaglioTitoloCompletoVO dettTitolo = new DettaglioTitoloCompletoVO();

		String codNatura = utilityCastor.getNaturaTitolo(BID, sbnMarc);
		UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo(BID,	BIDRoot, sbnMarc);

		// Campi comuni
		dettTitolo.getDetTitoloPFissaVO().setAreaTitTitolo(utilityCampiTitolo.getAreaTitoloCompleto(BID, sbnMarc, false));

		dettTitolo.getDetTitoloPFissaVO().setBid(utilityCampiTitolo.getBid());

		dettTitolo.getDetTitoloPFissaVO().setNatura(codNatura);
		dettTitolo.getDetTitoloPFissaVO().setLivCatal(utilityCampiTitolo.getLivelloAut());
		dettTitolo.getDetTitoloPFissaVO().setLivAut(utilityCampiTitolo.getLivelloAut());
		dettTitolo.getDetTitoloPFissaVO().setFonteRec(utilityCampiTitolo.getFonteRecord());
		dettTitolo.getDetTitoloPFissaVO().setTipoRec(utilityCampiTitolo.getTipoRecord());
		if (utilityCampiTitolo.getAgenzia() != null) {
			if (!utilityCampiTitolo.getAgenzia().equals("")) {
				dettTitolo.getDetTitoloPFissaVO().setFontePaese(utilityCampiTitolo.getAgenzia().substring(0,2));
				dettTitolo.getDetTitoloPFissaVO().setFonteAgenzia(utilityCampiTitolo.getAgenzia().substring(3));
			}
		}
		dettTitolo.getDetTitoloPFissaVO().setNorme(utilityCampiTitolo.getNorme());

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
		dettTitolo.getDetTitoloPFissaVO().setTipoTestoLetterario(utilityCampiTitolo.getTipoTestoLetterario());


		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		//13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
		//	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni -->
		if (dettTitolo.getDetTitoloPFissaVO().getTipoRec() != null && dettTitolo.getDetTitoloPFissaVO().getTipoRec().equals("i")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoTestoRegSonora(utilityCampiTitolo.getTipoTestoRegSonora());
		}
		dettTitolo.getDetTitoloPFissaVO().setFormaContenuto(utilityCampiTitolo.getFormaContenuto());
		dettTitolo.getDetTitoloPFissaVO().setTipoContenuto(utilityCampiTitolo.getTipoContenuto());
		dettTitolo.getDetTitoloPFissaVO().setMovimento(utilityCampiTitolo.getMovimento());
		dettTitolo.getDetTitoloPFissaVO().setDimensione(utilityCampiTitolo.getDimensione());
		dettTitolo.getDetTitoloPFissaVO().setSensorialita1(utilityCampiTitolo.getSensorialita1());
		dettTitolo.getDetTitoloPFissaVO().setSensorialita2(utilityCampiTitolo.getSensorialita2());
		dettTitolo.getDetTitoloPFissaVO().setSensorialita3(utilityCampiTitolo.getSensorialita3());
		dettTitolo.getDetTitoloPFissaVO().setTipoMediazione(utilityCampiTitolo.getTipoMediazione());

		// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
		// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
		dettTitolo.getDetTitoloPFissaVO().setPubblicatoSiNo(utilityCampiTitolo.getPubblicatoSiNo());

		dettTitolo.getDetTitoloPFissaVO().setFormaContenutoBIS(utilityCampiTitolo.getFormaContenutoBIS());
		dettTitolo.getDetTitoloPFissaVO().setTipoContenutoBIS(utilityCampiTitolo.getTipoContenutoBIS());
		dettTitolo.getDetTitoloPFissaVO().setMovimentoBIS(utilityCampiTitolo.getMovimentoBIS());
		dettTitolo.getDetTitoloPFissaVO().setDimensioneBIS(utilityCampiTitolo.getDimensioneBIS());
		dettTitolo.getDetTitoloPFissaVO().setSensorialitaBIS1(utilityCampiTitolo.getSensorialitaBIS1());
		dettTitolo.getDetTitoloPFissaVO().setSensorialitaBIS2(utilityCampiTitolo.getSensorialitaBIS2());
		dettTitolo.getDetTitoloPFissaVO().setSensorialitaBIS3(utilityCampiTitolo.getSensorialitaBIS3());
		dettTitolo.getDetTitoloPFissaVO().setTipoMediazioneBIS(utilityCampiTitolo.getTipoMediazioneBIS());

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		dettTitolo.getDetTitoloPFissaVO().setTipoSupporto(utilityCampiTitolo.getTipoSupporto());
		dettTitolo.getDetTitoloPFissaVO().setTipoSupportoBIS(utilityCampiTitolo.getTipoSupportoBIS());

		// almaviva5_20161005 evolutiva OCLC
		if (showOCN)
			try {
				//numero ocn associato al bid
				Tr_bid_altroid ocn = dao.getInstitutionId(Institution.OCLC.getCd_istituzione(), BID);
				if (ocn != null)
					dettTitolo.getDetTitoloPFissaVO().setOcn(ocn.getIst_id().toString());

			} catch (DaoManagerException e) {
				e.printStackTrace();
			}


		if (utilityCampiTitolo.getTipoMateriale().equals("Moderno")
				|| utilityCampiTitolo.getTipoMateriale().equals("M")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("M");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Antico")
				|| utilityCampiTitolo.getTipoMateriale().equals("E")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("E");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Musica")
				|| utilityCampiTitolo.getTipoMateriale().equals("U")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("U");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Cartografia")
				|| utilityCampiTitolo.getTipoMateriale().equals("C")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("C");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Grafica")
				|| utilityCampiTitolo.getTipoMateriale().equals("G")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("G");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Grafica")
				|| utilityCampiTitolo.getTipoMateriale().equals("H")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("H");
		} else if (utilityCampiTitolo.getTipoMateriale().equals("Audiovisivo")
				|| utilityCampiTitolo.getTipoMateriale().equals("L")) {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("L");

		} else {
			dettTitolo.getDetTitoloPFissaVO().setTipoMat("");
		}

		if (utilityCampiTitolo.getTipoMateriale().equals("Moderno")	|| utilityCampiTitolo.getTipoMateriale().equals("M")
				|| (utilityCampiTitolo.getTipoMateriale().equals("Antico") || utilityCampiTitolo.getTipoMateriale().equals("E"))) {
			dettTitolo.getDetTitoloPFissaVO().setGenere1(utilityCampiTitolo.getArrGenere()[0]);
			dettTitolo.getDetTitoloPFissaVO().setGenere2(utilityCampiTitolo.getArrGenere()[1]);
			dettTitolo.getDetTitoloPFissaVO().setGenere3(utilityCampiTitolo.getArrGenere()[2]);
			dettTitolo.getDetTitoloPFissaVO().setGenere4(utilityCampiTitolo.getArrGenere()[3]);
			if (utilityCampiTitolo.getAreaMusica() != null) {
				dettTitolo.getDetTitoloPFissaVO().setAreaTitMusica(utilityCampiTitolo.getAreaMusica());
			}
			if (utilityCampiTitolo.getAreaDatiMatematici() != null) {
				dettTitolo.getDetTitoloPFissaVO().setAreaTitDatiMatem(utilityCampiTitolo.getAreaDatiMatematici());
			}
		}

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (utilityCampiTitolo.getTipoMateriale().equals(" ") && codNatura.equals("R")) {
			dettTitolo.getDetTitoloPFissaVO().setGenere1(utilityCampiTitolo.getArrGenere()[0]);
			dettTitolo.getDetTitoloPFissaVO().setGenere2(utilityCampiTitolo.getArrGenere()[1]);
			dettTitolo.getDetTitoloPFissaVO().setGenere3(utilityCampiTitolo.getArrGenere()[2]);
			dettTitolo.getDetTitoloPFissaVO().setGenere4(utilityCampiTitolo.getArrGenere()[3]);
		}

		// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		// viene esteso anche al Materiale Moderno e Antico
		if ((utilityCampiTitolo.getTipoMateriale().equals("Moderno") || utilityCampiTitolo.getTipoMateriale().equals("M"))
				|| (utilityCampiTitolo.getTipoMateriale().equals("Antico") || utilityCampiTitolo.getTipoMateriale().equals("E"))) {
			dettTitolo.getDetTitoloModAntVO().setGenereRappr(utilityCampiTitolo.getGenereRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setAnnoIRappr(utilityCampiTitolo.getAnnoRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setPeriodoIRappr(utilityCampiTitolo.getPeriodoRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setSedeIRappr(utilityCampiTitolo.getSedeRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setLocalitaIRappr(utilityCampiTitolo.getLuogoRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setNoteIRappr(utilityCampiTitolo.getNotaRappresentazione());
			dettTitolo.getDetTitoloModAntVO().setOccasioneIRappr(utilityCampiTitolo.getOccasioneRappresentazione());

			// personaggi
			TabellaNumSTDImpronteVO tabPersonaggi;
			String descPers = "";
			for (int i = 0; i < utilityCampiTitolo.getNumPersonaggio(); i++) {
				tabPersonaggi = new TabellaNumSTDImpronteVO(utilityCampiTitolo.getPersonaggio()[i],
						utilityCampiTitolo.getTimbriVocale()[i], descPers, utilityCampiTitolo.getInterprete()[i]);
				dettTitolo.getDetTitoloModAntVO().addListaPersonaggi(tabPersonaggi);
			}
		}

		if (utilityCampiTitolo.getTipoMateriale().equals("Musica")
				|| utilityCampiTitolo.getTipoMateriale().equals("U")) {
			dettTitolo.getDetTitoloPFissaVO().setAreaTitMusica(	utilityCampiTitolo.getAreaMusica());
			dettTitolo.getDetTitoloMusVO().setTipoElabor(utilityCampiTitolo.getTipoElaborazione());
			dettTitolo.getDetTitoloMusVO().setStesura(utilityCampiTitolo.getStesura());
			dettTitolo.getDetTitoloMusVO().setGenereRappr(utilityCampiTitolo.getGenereRappresentazione());
			dettTitolo.getDetTitoloMusVO().setAnnoIRappr(utilityCampiTitolo.getAnnoRappresentazione());
			dettTitolo.getDetTitoloMusVO().setPeriodoIRappr(utilityCampiTitolo.getPeriodoRappresentazione());
			dettTitolo.getDetTitoloMusVO().setSedeIRappr(utilityCampiTitolo.getSedeRappresentazione());
			dettTitolo.getDetTitoloMusVO().setLocalitaIRappr(utilityCampiTitolo.getLuogoRappresentazione());
			dettTitolo.getDetTitoloMusVO().setNoteIRappr(utilityCampiTitolo.getNotaRappresentazione());
			dettTitolo.getDetTitoloMusVO().setOccasioneIRappr(utilityCampiTitolo.getOccasioneRappresentazione());

			// personaggi
			TabellaNumSTDImpronteVO tabPersonaggi;
			String descPers = "";
			for (int i = 0; i < utilityCampiTitolo.getNumPersonaggio(); i++) {
				tabPersonaggi = new TabellaNumSTDImpronteVO(utilityCampiTitolo
						.getPersonaggio()[i], utilityCampiTitolo
						.getTimbriVocale()[i], descPers, utilityCampiTitolo
						.getInterprete()[i]);
				dettTitolo.getDetTitoloMusVO()
						.addListaPersonaggi(tabPersonaggi);
			}

			//C926[] c926 = utilityCampiTitolo.getIncipit();
			// Inserire gestione della tabella Incipit sul modello di quella personaggi
			// protected String[] columnNames = {
			// "N° movimento", // f_926
			// "N° prog. nel documento", // g_926
			// "Contesto musicale", // c_926
			// "Indicatore", // a_926
			// "N° composizione", // b_926
			// "Voce/strumento", // h_926
			// "Nome personaggio", // q_926
			// "Indicazione del movimento", // p_926
			// "Forma musicale", // i_926
			// "Tonalità", // l_926
			// "Chiave", // m_926
			// "Alterazioni", // n_926
			// "Misura", // o_926
			// "Incipit testuale" // r_926
			// };


			// INCIPIT
			TabellaNumSTDImpronteVO tabIncipit;
			DettaglioIncipitVO elemDettIncipit;
			if (utilityCampiTitolo.getIncipit() != null) {
				C926[] c926 = utilityCampiTitolo.getIncipit();
				//String descPers = "";
				for (int i = 0; i < c926.length; i++) {
					tabIncipit = new TabellaNumSTDImpronteVO(
							c926[i].getF_926(),
							c926[i].getG_926(),
							"",
							c926[i].getC_926());
					dettTitolo.getDetTitoloMusVO().addListaIncipit(tabIncipit);

					elemDettIncipit = new DettaglioIncipitVO(
							c926[i].getB_926() == null ? "" :c926[i].getB_926(),
							c926[i].getF_926() == null ? "" :c926[i].getF_926(),
							c926[i].getG_926() == null ? "" :c926[i].getG_926(),
							c926[i].getQ_926() == null ? "" :c926[i].getQ_926(),
							c926[i].getH_926() == null ? "" :c926[i].getH_926(),
							c926[i].getI_926() == null ? "" :c926[i].getI_926(),
							c926[i].getP_926() == null ? "" :c926[i].getP_926(),
							c926[i].getL_926() == null ? "" :c926[i].getL_926(),
							c926[i].getM_926() == null ? "" :c926[i].getM_926(),
							c926[i].getN_926() == null ? "" :c926[i].getN_926(),
							c926[i].getO_926() == null ? "" :c926[i].getO_926(),
							c926[i].getC_926() == null ? "" :c926[i].getC_926(),
							c926[i].getR_926() == null ? "" :c926[i].getR_926());
					dettTitolo.getDetTitoloMusVO().addListaDettagliIncipit(elemDettIncipit);


				}
			}
		}

		// Valori di numNatura: 0 titolo Uniforme; 1 documento; 2 titolo accesso
		int numNatura = 0;

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (codNatura.equals("M") || codNatura.equals("W") || codNatura.equals("N") || codNatura.equals("S")
				|| codNatura.equals("C") || codNatura.equals("R")) {

			numNatura = 1;
			dettTitolo.getDetTitoloPFissaVO().setTipoData(utilityCampiTitolo.getTipoData().toUpperCase());
			dettTitolo.getDetTitoloPFissaVO().setPaese(utilityCampiTitolo.getPaese());
			dettTitolo.getDetTitoloPFissaVO().setLingua(utilityCampiTitolo.getArrLingua()[0]);
			dettTitolo.getDetTitoloPFissaVO().setLingua1(utilityCampiTitolo.getArrLingua()[0]);
			dettTitolo.getDetTitoloPFissaVO().setLingua2(utilityCampiTitolo.getArrLingua()[1]);
			dettTitolo.getDetTitoloPFissaVO().setLingua3(utilityCampiTitolo.getArrLingua()[2]);

			if (utilityCampiTitolo.getVersione() != null) {
				if (utilityCampiTitolo.getVersione().length() >= 10) {
					dettTitolo.getDetTitoloPFissaVO().setVersione(utilityCampiTitolo.getVersione());
					dettTitolo.getDetTitoloPFissaVO().setDataAgg(utilityDate.converteDataSBN(utilityCampiTitolo.getVersione()));
				}
			}
			dettTitolo.getDetTitoloPFissaVO().setDataIns(utilityCampiTitolo.getDataInserimento());
			dettTitolo.getDetTitoloPFissaVO().setDataPubbl1(utilityCampiTitolo.getData1());
			dettTitolo.getDetTitoloPFissaVO().setDataPubbl2(utilityCampiTitolo.getData2());

			dettTitolo.getDetTitoloPFissaVO().setAreaTitDescrFis(utilityCampiTitolo.getAreaDescrizioneFisicaCompleta(BID, sbnMarc));

//			GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote(utilityCampiTitolo.getAreaNoteCompleta(BID, sbnMarc));
//			 Intervento almaviva2 - BUG MANTIS 3360 - il campo note deve rimanere impostato con tutte le 300 in sequenza
//			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.VALUE_0));

			// Bug MANTIS esercizio 6171 - almaviva2 Aprile 2016 a seguito del'intervento di inserimento della nota 321
			// la valorizzazione dei codici nota nella classe SbnTipoNota utilizzata per inviare il tipo di Nota al
			// protocollo Polo/Indiceè cambiata; la valorizzazione del campo tipo nota viene quindi effettuata con metodo
			// valueOf indicando esplicitamente la nota in oggetto e non con il suo progressivo automatico
			// che ha subito variazioni (e potrebbe subirne altre);
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote323(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("323")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote327(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("327")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote330(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("330")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote336(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("336")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNote337(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("337")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNoteDATA(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("DATA")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNoteORIG(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("ORIG")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNoteFILI(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("FILI")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNotePROV(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("PROV")));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitNotePOSS(utilityCampiTitolo.getAreaNotaSpecifica(BID, sbnMarc, SbnTipoNota.valueOf("POSS")));
//			GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274


			dettTitolo.getDetTitoloPFissaVO().setAreaTitPubbl(utilityCampiTitolo.getAreaPubblicazioneCompleta(BID, sbnMarc));
			dettTitolo.getDetTitoloPFissaVO().setAreaTitEdiz(utilityCampiTitolo.getAreaEdizioneCompleta(BID, sbnMarc));


			dettTitolo.getDetTitoloPFissaVO().setUriDigitalBorn(utilityCampiTitolo.getUriDigitalBorn() );



			// Numero Standard
			TabellaNumSTDImpronteVO tabNumNSt;
			String descNumStandard = "";
			String sbnNumStandard = "";
			String notaNumStandard = "";
			for (int i = 0; i < utilityCampiTitolo.getNumStdCount(); i++) {
				try {
					descNumStandard = codici.cercaDescrizioneCodice(
							utilityCampiTitolo.getTipoSTD()[i],
							CodiciType.CODICE_TIPO_NUMERO_STANDARD,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				try {
					sbnNumStandard = codici.unimarcToSBN(CodiciType.CODICE_TIPO_NUMERO_STANDARD,
							utilityCampiTitolo.getTipoSTD()[i]);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				if (utilityCampiTitolo.getNotaSTD()[i] == null) {
					notaNumStandard = "";
				} else {
					notaNumStandard= utilityCampiTitolo.getNotaSTD()[i];
				}

				tabNumNSt = new TabellaNumSTDImpronteVO(
						utilityCampiTitolo.getNumSTD()[i],
						sbnNumStandard,
						descNumStandard,
						notaNumStandard);
				dettTitolo.getDetTitoloPFissaVO()
						.addListaNumStandard(tabNumNSt);
			}

			// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
			// al link dei documenti su Basi Esterne - Link verso base date digitali
			TabellaNumSTDImpronteVO tabLinkEsterni;
			String linkEsterniBD = "";
			String linkEsterniID = "";
			String linkEsterniURL = "";
			for (int i = 0; i < utilityCampiTitolo.getLinkEsterniCount(); i++) {
				try {
					linkEsterniBD = codici.cercaDescrizioneCodice(
							utilityCampiTitolo.getLinkEsterniBD()[i],
							CodiciType.CODICE_LINK_ALTRA_BASE_DATI,
							CodiciRicercaType.RICERCA_CODICE_UNIMARC);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				if (utilityCampiTitolo.getLinkEsterniID()[i] == null) {
					linkEsterniID = "";
				} else {
					linkEsterniID = utilityCampiTitolo.getLinkEsterniID()[i];
				}

				if (utilityCampiTitolo.getLinkEsterniURL()[i] == null) {
					linkEsterniURL = "";
				} else {
					linkEsterniURL = utilityCampiTitolo.getLinkEsterniURL()[i];
				}
				tabLinkEsterni = new TabellaNumSTDImpronteVO(
						utilityCampiTitolo.getLinkEsterniBD()[i],
						linkEsterniID,
						linkEsterniBD,
						linkEsterniURL);
				dettTitolo.getDetTitoloPFissaVO().addListaLinkEsterni(tabLinkEsterni);
			}

			// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
			// ai repertori cartacei - Riferimento a repertorio cartaceo
			TabellaNumSTDImpronteVO tabReperCartaceo;
			String reperCartaceoAutTit = "";
			String reperCartaceoData = "";
			String reperCartaceoPos = "";
			for (int i = 0; i < utilityCampiTitolo.getReperCartaceoCount(); i++) {

				if (utilityCampiTitolo.getReperCartaceoAutTit()[i] == null) {
					reperCartaceoAutTit = "";
				} else {
					reperCartaceoAutTit = utilityCampiTitolo.getReperCartaceoAutTit()[i];
				}
				if (utilityCampiTitolo.getReperCartaceoData()[i] == null) {
					reperCartaceoData = "";
				} else {
					reperCartaceoData = utilityCampiTitolo.getReperCartaceoData()[i];
				}

				if (utilityCampiTitolo.getReperCartaceoPos()[i] == null) {
					reperCartaceoPos = "";
				} else {
					reperCartaceoPos = utilityCampiTitolo.getReperCartaceoPos()[i];
				}
				tabReperCartaceo = new TabellaNumSTDImpronteVO(
						reperCartaceoAutTit,
						reperCartaceoData,
						"",
						reperCartaceoPos);
				dettTitolo.getDetTitoloPFissaVO().addListaReperCartaceo(tabReperCartaceo);
			}

			// Impronta
			TabellaNumSTDImpronteVO tabNumImp;
			for (int i = 0; i < utilityCampiTitolo.getImprontaCount(); i++) {
				tabNumImp = new TabellaNumSTDImpronteVO(String.valueOf(i + 1),
						utilityCampiTitolo.getImpronta1()[i]
								+ utilityCampiTitolo.getImpronta2()[i]
								+ utilityCampiTitolo.getImpronta3()[i], " ",
						utilityCampiTitolo.getNotaImpronta()[i]);

				dettTitolo.getDetTitoloPFissaVO().addListaImpronte(tabNumImp);
			}

			if (utilityCampiTitolo.getTipoMateriale().equals("Musica")
					|| utilityCampiTitolo.getTipoMateriale().equals("U")) {
				dettTitolo.getDetTitoloMusVO().setLivAutSpec(utilityCampiTitolo.getLivelloAutMus());
				dettTitolo.getDetTitoloMusVO().setTipoTesto(utilityCampiTitolo.getTipoTesto());
				dettTitolo.getDetTitoloMusVO().setElabor(utilityCampiTitolo.getTipoElaborazione());
				dettTitolo.getDetTitoloMusVO().setTipoElabor(utilityCampiTitolo.getTipoElaborazione());
				dettTitolo.getDetTitoloMusVO().setStesura(utilityCampiTitolo.getStesura());
				dettTitolo.getDetTitoloMusVO().setComposito(utilityCampiTitolo.getComposito());
				dettTitolo.getDetTitoloMusVO().setPalinsesto(utilityCampiTitolo.getPalinsesto());
				dettTitolo.getDetTitoloMusVO().setMateria(utilityCampiTitolo.getMateria());
				dettTitolo.getDetTitoloMusVO().setTipoTesto(utilityCampiTitolo.getTipoTesto());

				dettTitolo.getDetTitoloMusVO().setPresent(utilityCampiTitolo.getPresentazione().toUpperCase());
				if (utilityCampiTitolo.getPresentazione().toUpperCase().equalsIgnoreCase("x")) {
					dettTitolo.getDetTitoloMusVO().setPresent("NA");
				}

				dettTitolo.getDetTitoloMusVO().setOrgSint(utilityCampiTitolo.getOrganicoSintetico());
				dettTitolo.getDetTitoloMusVO().setOrgAnal(utilityCampiTitolo.getOrganicoAnalitico());
				dettTitolo.getDetTitoloMusVO().setDatazione(utilityCampiTitolo.getDatazione());

				dettTitolo.getDetTitoloMusVO().setIllustrazioni(utilityCampiTitolo.getIllustrazioni());
				dettTitolo.getDetTitoloMusVO().setNotazioneMusicale(utilityCampiTitolo.getNotazioneMusicale());
				dettTitolo.getDetTitoloMusVO().setLegatura(utilityCampiTitolo.getLegatura());
				dettTitolo.getDetTitoloMusVO().setConservazione(utilityCampiTitolo.getConservazione());
			}

			if (utilityCampiTitolo.getTipoMateriale().equals("Cartografia")
					|| utilityCampiTitolo.getTipoMateriale().equals("C")) {
				dettTitolo.getDetTitoloCarVO().setLivAutSpec(utilityCampiTitolo.getLivelloAutCar());
				dettTitolo.getDetTitoloCarVO().setPubblicazioneGovernativa(utilityCampiTitolo.getPubblicazioneGovernativa());
				dettTitolo.getDetTitoloCarVO().setIndicatoreColore(utilityCampiTitolo.getIndicatoreColoreCartografia());
				dettTitolo.getDetTitoloCarVO().setMeridianoOrigine(utilityCampiTitolo.getMeridianoOrigine());
				dettTitolo.getDetTitoloCarVO().setSupportoFisico(utilityCampiTitolo.getSupportoFisico());
				dettTitolo.getDetTitoloCarVO().setTecnicaCreazione(utilityCampiTitolo.getTecnicaCreazione());
				dettTitolo.getDetTitoloCarVO().setFormaRiproduzione(utilityCampiTitolo.getFormaRiproduzione());
				dettTitolo.getDetTitoloCarVO().setFormaPubblicazione(utilityCampiTitolo.getFormaPubblicazione());
				dettTitolo.getDetTitoloCarVO().setAltitudine(utilityCampiTitolo.getAltitudine());
				dettTitolo.getDetTitoloCarVO().setTipoScala(utilityCampiTitolo.getTipoScala());
				dettTitolo.getDetTitoloCarVO().setIndicatoreTipoScala(utilityCampiTitolo.getIndicatoreTipoScala());
				dettTitolo.getDetTitoloCarVO().setCarattereImmagine(utilityCampiTitolo.getCarattereImmagine());
				dettTitolo.getDetTitoloCarVO().setForma(utilityCampiTitolo.getForma());
				dettTitolo.getDetTitoloCarVO().setPiattaforma(utilityCampiTitolo.getPiattaforma());
				dettTitolo.getDetTitoloCarVO().setCategoriaSatellite(utilityCampiTitolo.getCategoriaSatellite());
				// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
				dettTitolo.getDetTitoloCarVO().setProiezioneCarte(utilityCampiTitolo.getProiezioneCarte());

				// almaviva2 2011.02.11 - Correzione interna per errore di valorizzazione campo !!!!!!!!!!!!!!!!!!!!!!
//				dettTitolo.getDetTitoloCarVO().setTipoScala(utilityCampiTitolo.getIndicatoreTipoScala());



				// almaviva2 2012.04.23 - BUG MANTIS 4928 (collaudo) E' necessario inizializzare anche i campi relativi alla tipologia di
				// di coordinate oltre che al loro valore
				// Longitudine 1
				if (utilityCampiTitolo.getCoordinateOvest() != null
						&& !utilityCampiTitolo.getCoordinateOvest().equals("")) {
					dettTitolo.getDetTitoloCarVO().setLongitTipo1(utilityCampiTitolo.getCoordinateOvest().substring(0, 1));
					dettTitolo.getDetTitoloCarVO().setLongitValEO1(utilityCampiTitolo.getCoordinateOvest().substring(1));
				} else {
					dettTitolo.getDetTitoloCarVO().setLongitTipo1("");
					dettTitolo.getDetTitoloCarVO().setLongitValEO1("");
				}
				// Longitudine 2
				if (utilityCampiTitolo.getCoordinateEst() != null
						&& !utilityCampiTitolo.getCoordinateEst().equals("")) {
					dettTitolo.getDetTitoloCarVO().setLongitTipo2(utilityCampiTitolo.getCoordinateEst().substring(0,1));
					dettTitolo.getDetTitoloCarVO().setLongitValEO2(utilityCampiTitolo.getCoordinateEst().substring(1));
				} else {
					dettTitolo.getDetTitoloCarVO().setLongitTipo2("");
					dettTitolo.getDetTitoloCarVO().setLongitValEO2("");
				}



				// Latitudine 1
				if (utilityCampiTitolo.getCoordinateNord() != null
						&& !utilityCampiTitolo.getCoordinateNord().equals("")) {
					dettTitolo.getDetTitoloCarVO().setLatitTipo1(utilityCampiTitolo.getCoordinateNord().substring(0,1));
					dettTitolo.getDetTitoloCarVO().setLatitValNS1(utilityCampiTitolo.getCoordinateNord().substring(1));
				} else {
					dettTitolo.getDetTitoloCarVO().setLatitTipo1("");
					dettTitolo.getDetTitoloCarVO().setLatitValNS1("");
				}

				// Latitudine 2
				if (utilityCampiTitolo.getCoordinateSud() != null
						&& !utilityCampiTitolo.getCoordinateSud().equals("")) {
					dettTitolo.getDetTitoloCarVO().setLatitTipo2(utilityCampiTitolo.getCoordinateSud().substring(0,1));
					dettTitolo.getDetTitoloCarVO().setLatitValNS2(utilityCampiTitolo.getCoordinateSud().substring(1));
				} else {
					dettTitolo.getDetTitoloCarVO().setLatitTipo2("");
					dettTitolo.getDetTitoloCarVO().setLatitValNS2("");
				}


				dettTitolo.getDetTitoloCarVO().setScalaOriz(utilityCampiTitolo.getScalaOrizzontale());
				dettTitolo.getDetTitoloCarVO().setScalaVert(utilityCampiTitolo.getScalaVerticale());
			}

			if (utilityCampiTitolo.getTipoMateriale().equals("Grafica")
					|| utilityCampiTitolo.getTipoMateriale().equals("G")) {
				dettTitolo.getDetTitoloGraVO().setLivAutSpec(utilityCampiTitolo.getLivelloAutGra());
				dettTitolo.getDetTitoloGraVO().setDesignMat(utilityCampiTitolo.getDesignazioneMateriale());
				dettTitolo.getDetTitoloGraVO().setSuppPrim(utilityCampiTitolo.getSupportoPrimario());
				dettTitolo.getDetTitoloGraVO().setIndicCol(utilityCampiTitolo.getIndicatoreColoreGrafica());
				dettTitolo.getDetTitoloGraVO().setIndicTec1(utilityCampiTitolo.getIndicatoreTecnica1());
				dettTitolo.getDetTitoloGraVO().setIndicTec2(utilityCampiTitolo.getIndicatoreTecnica2());
				dettTitolo.getDetTitoloGraVO().setIndicTec3(utilityCampiTitolo.getIndicatoreTecnica3());
				dettTitolo.getDetTitoloGraVO().setIndicTecSta1(utilityCampiTitolo.getIndicatoreTecnicaStampe1());
				dettTitolo.getDetTitoloGraVO().setIndicTecSta2(utilityCampiTitolo.getIndicatoreTecnicaStampe2());
				dettTitolo.getDetTitoloGraVO().setIndicTecSta3(utilityCampiTitolo.getIndicatoreTecnicaStampe3());
				dettTitolo.getDetTitoloGraVO().setDesignFun(utilityCampiTitolo.getDesignazioneFunzione());
			}


			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (utilityCampiTitolo.getTipoMateriale().equals("Audiovisivo")
					|| utilityCampiTitolo.getTipoMateriale().equals("H")) {
				dettTitolo.getDetTitoloAudVO().setLivAutSpec(utilityCampiTitolo.getLivelloAutAudiov());
				if (utilityCampiTitolo.getTipoRecord().equals("g")) {
					dettTitolo.getDetTitoloAudVO().setTipoVideo(utilityCampiTitolo.getTipoVideo());
					dettTitolo.getDetTitoloAudVO().setLunghezza(utilityCampiTitolo.getLunghezza());
					dettTitolo.getDetTitoloAudVO().setIndicatoreColore(utilityCampiTitolo.getIndicatoreColore());
					dettTitolo.getDetTitoloAudVO().setIndicatoreSuono(utilityCampiTitolo.getIndicatoreSuono());
					dettTitolo.getDetTitoloAudVO().setSupportoSuono(utilityCampiTitolo.getSupportoSuono());
					dettTitolo.getDetTitoloAudVO().setLarghezzaDimensioni(utilityCampiTitolo.getLarghezzaDimensioni());
					dettTitolo.getDetTitoloAudVO().setFormaPubblDistr(utilityCampiTitolo.getFormaPubblDistr());
					dettTitolo.getDetTitoloAudVO().setTecnicaVideoFilm(utilityCampiTitolo.getTecnicaVideoFilm());
					dettTitolo.getDetTitoloAudVO().setPresentImmagMov(utilityCampiTitolo.getPresentImmagMov());
					dettTitolo.getDetTitoloAudVO().setMaterAccompagn1(utilityCampiTitolo.getArrMaterAccompagn()[0]);
					dettTitolo.getDetTitoloAudVO().setMaterAccompagn2(utilityCampiTitolo.getArrMaterAccompagn()[1]);
					dettTitolo.getDetTitoloAudVO().setMaterAccompagn3(utilityCampiTitolo.getArrMaterAccompagn()[2]);
					dettTitolo.getDetTitoloAudVO().setMaterAccompagn4(utilityCampiTitolo.getArrMaterAccompagn()[3]);
					dettTitolo.getDetTitoloAudVO().setPubblicVideoreg(utilityCampiTitolo.getPubblicVideoreg());
					dettTitolo.getDetTitoloAudVO().setPresentVideoreg(utilityCampiTitolo.getPresentVideoreg());
					dettTitolo.getDetTitoloAudVO().setMaterialeEmulsBase(utilityCampiTitolo.getMaterialeEmulsBase());
					dettTitolo.getDetTitoloAudVO().setMaterialeSupportSec(utilityCampiTitolo.getMaterialeSupportSec());
					dettTitolo.getDetTitoloAudVO().setStandardTrasmiss(utilityCampiTitolo.getStandardTrasmiss());
					dettTitolo.getDetTitoloAudVO().setVersioneAudiovid(utilityCampiTitolo.getVersioneAudiovid());
					dettTitolo.getDetTitoloAudVO().setElementiProd(utilityCampiTitolo.getElementiProd());
					dettTitolo.getDetTitoloAudVO().setSpecCatColoreFilm(utilityCampiTitolo.getSpecCatColoreFilm());
					dettTitolo.getDetTitoloAudVO().setEmulsionePellic(utilityCampiTitolo.getEmulsionePellic());
					dettTitolo.getDetTitoloAudVO().setComposPellic(utilityCampiTitolo.getComposPellic());
					dettTitolo.getDetTitoloAudVO().setSuonoImmagMovimento(utilityCampiTitolo.getSuonoImmagMovimento());
					dettTitolo.getDetTitoloAudVO().setTipoPellicStampa(utilityCampiTitolo.getTipoPellicStampa());

				} else if (utilityCampiTitolo.getTipoRecord().equals("i")
						|| utilityCampiTitolo.getTipoRecord().equals("j")) {

					dettTitolo.getDetTitoloAudVO().setFormaPubblicazioneDisco(utilityCampiTitolo.getFormaPubblicazioneDisco());
					dettTitolo.getDetTitoloAudVO().setVelocita(utilityCampiTitolo.getVelocita());
					dettTitolo.getDetTitoloAudVO().setTipoSuono(utilityCampiTitolo.getTipoSuono());
					dettTitolo.getDetTitoloAudVO().setLarghezzaScanal(utilityCampiTitolo.getLarghezzaScanal());
					dettTitolo.getDetTitoloAudVO().setDimensioni(utilityCampiTitolo.getDimensioni());
					dettTitolo.getDetTitoloAudVO().setLarghezzaNastro(utilityCampiTitolo.getLarghezzaNastro());
					dettTitolo.getDetTitoloAudVO().setConfigurazNastro(utilityCampiTitolo.getConfigurazNastro());
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn1(utilityCampiTitolo.getArrMaterTestAccompagn()[0]);
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn2(utilityCampiTitolo.getArrMaterTestAccompagn()[1]);
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn3(utilityCampiTitolo.getArrMaterTestAccompagn()[2]);
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn4(utilityCampiTitolo.getArrMaterTestAccompagn()[3]);
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn5(utilityCampiTitolo.getArrMaterTestAccompagn()[4]);
					dettTitolo.getDetTitoloAudVO().setMaterTestAccompagn6(utilityCampiTitolo.getArrMaterTestAccompagn()[5]);
					dettTitolo.getDetTitoloAudVO().setTecnicaRegistraz(utilityCampiTitolo.getTecnicaRegistraz());
					dettTitolo.getDetTitoloAudVO().setSpecCarattRiprod(utilityCampiTitolo.getSpecCarattRiprod());
					dettTitolo.getDetTitoloAudVO().setDatiCodifRegistrazSonore(utilityCampiTitolo.getDatiCodifRegistrazSonore());
					dettTitolo.getDetTitoloAudVO().setTipoDiMateriale(utilityCampiTitolo.getTipoDiMateriale());
					dettTitolo.getDetTitoloAudVO().setTipoDiTaglio(utilityCampiTitolo.getTipoDiTaglio());
					dettTitolo.getDetTitoloAudVO().setDurataRegistraz(utilityCampiTitolo.getDurataRegistraz());

				}

				// Parte dell'Area Musica
				dettTitolo.getDetTitoloMusVO().setOrgSint(utilityCampiTitolo.getOrganicoSintetico());
				dettTitolo.getDetTitoloMusVO().setOrgAnal(utilityCampiTitolo.getOrganicoAnalitico());
				dettTitolo.getDetTitoloMusVO().setElabor(utilityCampiTitolo.getTipoElaborazione());
				dettTitolo.getDetTitoloMusVO().setTipoElabor(utilityCampiTitolo.getTipoElaborazione());
				dettTitolo.getDetTitoloMusVO().setGenereRappr(utilityCampiTitolo.getGenereRappresentazione());
				dettTitolo.getDetTitoloMusVO().setAnnoIRappr(utilityCampiTitolo.getAnnoRappresentazione());
				dettTitolo.getDetTitoloMusVO().setPeriodoIRappr(utilityCampiTitolo.getPeriodoRappresentazione());
				dettTitolo.getDetTitoloMusVO().setSedeIRappr(utilityCampiTitolo.getSedeRappresentazione());
				dettTitolo.getDetTitoloMusVO().setLocalitaIRappr(utilityCampiTitolo.getLuogoRappresentazione());
				dettTitolo.getDetTitoloMusVO().setNoteIRappr(utilityCampiTitolo.getNotaRappresentazione());
				dettTitolo.getDetTitoloMusVO().setOccasioneIRappr(utilityCampiTitolo.getOccasioneRappresentazione());

				// personaggi
				TabellaNumSTDImpronteVO tabPersonaggi;
				String descPers = "";
				for (int i = 0; i < utilityCampiTitolo.getNumPersonaggio(); i++) {
					tabPersonaggi = new TabellaNumSTDImpronteVO(utilityCampiTitolo
							.getPersonaggio()[i], utilityCampiTitolo
							.getTimbriVocale()[i], descPers, utilityCampiTitolo.getInterprete()[i]);
					dettTitolo.getDetTitoloMusVO().addListaPersonaggi(tabPersonaggi);
				}

			}

			if (utilityCampiTitolo.getTipoMateriale().equals("Elettronico")
					|| utilityCampiTitolo.getTipoMateriale().equals("L")) {
				dettTitolo.getDetTitoloEleVO().setLivAutSpec(utilityCampiTitolo.getLivelloAutElettr());

				// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
				// Gestione nuovi campi specifici per etichetta 135
				dettTitolo.getDetTitoloEleVO().setTipoRisorsaElettronica(utilityCampiTitolo.getTipoRisorsaElettronica());
				dettTitolo.getDetTitoloEleVO().setIndicazioneSpecificaMateriale(utilityCampiTitolo.getIndicazioneSpecificaMateriale());
				dettTitolo.getDetTitoloEleVO().setColoreElettronico(utilityCampiTitolo.getColoreElettronico());
				dettTitolo.getDetTitoloEleVO().setDimensioniElettronico(utilityCampiTitolo.getDimensioniElettronico());
				dettTitolo.getDetTitoloEleVO().setSuonoElettronico(utilityCampiTitolo.getSuonoElettronico());
			}
			// Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

		} else if (codNatura.equals("A")  || codNatura.equals("V") ) {
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			dettTitolo.getDetTitoloPFissaVO().setVersione(utilityCampiTitolo.getVersione());
			dettTitolo.getDetTitoloPFissaVO().setDataAgg(utilityDate.converteDataSBN(utilityCampiTitolo.getVersione()));
			dettTitolo.getDetTitoloPFissaVO().setNoteInformative(utilityCampiTitolo.getNotaInformativa());
			dettTitolo.getDetTitoloPFissaVO().setNoteCatalogatore(utilityCampiTitolo.getNotaCatalogatore());
			dettTitolo.getDetTitoloPFissaVO().setDataIns(utilityCampiTitolo.getDataInserimento());
			dettTitolo.getDetTitoloPFissaVO().setIsadn(utilityCampiTitolo.getIsadn());

			if (utilityCampiTitolo.getListRepertori() != null) {
				TabellaNumSTDImpronteOriginarioVO tabRepertori;
				List<TabellaNumSTDImpronteOriginarioVO> listaAppoggio = new ArrayList<TabellaNumSTDImpronteOriginarioVO>();
				Repertorio repertorio;
				Iterator e = utilityCampiTitolo.getListRepertori().iterator();
				while (e.hasNext()) {
					repertorio = (Repertorio) e.next();
					tabRepertori = new TabellaNumSTDImpronteOriginarioVO(repertorio
							.getEsito(), repertorio.getCodiceRepertorio(), "",
							repertorio.getNotaAlLegame());
					listaAppoggio.add(tabRepertori);
				}
				dettTitolo.getDetTitoloPFissaVO().setListaRepertoriOld(listaAppoggio);
			}

			numNatura = 0;
			if (utilityCampiTitolo.getTipoMateriale().equals("Musica")
					|| utilityCampiTitolo.getTipoMateriale().equals("U")) {
				dettTitolo.getDetTitoloMusVO().setFormaMusic1(utilityCampiTitolo.getFormaMusicale()[0]);
				dettTitolo.getDetTitoloMusVO().setFormaMusic2(utilityCampiTitolo.getFormaMusicale()[1]);
				dettTitolo.getDetTitoloMusVO().setFormaMusic3(utilityCampiTitolo.getFormaMusicale()[2]);

				dettTitolo.getDetTitoloMusVO().setTitOrdinam(utilityCampiTitolo.getTitoloOrdinamento());
				dettTitolo.getDetTitoloMusVO().setTitEstratto(utilityCampiTitolo.getTitoloEstratto());
				dettTitolo.getDetTitoloMusVO().setAppellativo(utilityCampiTitolo.getTitoloAppellativo());
				dettTitolo.getDetTitoloMusVO().setOrgSint(utilityCampiTitolo.getOrganicoSintetico());
				dettTitolo.getDetTitoloMusVO().setOrgAnal(utilityCampiTitolo.getOrganicoAnalitico());
				dettTitolo.getDetTitoloMusVO().setNumOrdine(utilityCampiTitolo.getNumeroOrdine());
				dettTitolo.getDetTitoloMusVO().setNumCatTem(utilityCampiTitolo.getNumeroCatalogoTematico());
				dettTitolo.getDetTitoloMusVO().setNumOpera(utilityCampiTitolo.getNumeroOpera());
				dettTitolo.getDetTitoloMusVO().setDatazione(utilityCampiTitolo.getDatazione());
				dettTitolo.getDetTitoloMusVO().setTonalita(utilityCampiTitolo.getTonalita());
				dettTitolo.getDetTitoloMusVO().setSezioni(utilityCampiTitolo.getSezioni());
			} else {
				dettTitolo.getDetTitoloPFissaVO().setLingua(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua1(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua2(utilityCampiTitolo.getArrLingua()[1]);
				dettTitolo.getDetTitoloPFissaVO().setLingua3(utilityCampiTitolo.getArrLingua()[2]);
				 // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
                // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
				dettTitolo.getDetTitoloPFissaVO().setFormaOpera231(utilityCampiTitolo.getFormaOpera231());
				dettTitolo.getDetTitoloPFissaVO().setDataOpera231(utilityCampiTitolo.getDataOpera231());
				dettTitolo.getDetTitoloPFissaVO().setAltreCarat231(utilityCampiTitolo.getAltreCarat231());
				dettTitolo.getDetTitoloPFissaVO().setPaese(utilityCampiTitolo.getPaese());
			}
		} else {

			numNatura = 2;
			if (utilityCampiTitolo.getVersione() != null) {
				if (utilityCampiTitolo.getVersione().length() >= 10) {
					dettTitolo.getDetTitoloPFissaVO().setVersione(utilityCampiTitolo.getVersione());
					dettTitolo.getDetTitoloPFissaVO().setDataAgg(utilityDate.converteDataSBN(utilityCampiTitolo.getVersione()));
				}
			}

			if (codNatura.equals("T")) {
				dettTitolo.getDetTitoloPFissaVO().setLingua(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua1(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua2(utilityCampiTitolo.getArrLingua()[1]);
				dettTitolo.getDetTitoloPFissaVO().setLingua3(utilityCampiTitolo.getArrLingua()[2]);
			}

			// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
			if (codNatura.equals("B")) {
				dettTitolo.getDetTitoloPFissaVO().setLingua(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua1(utilityCampiTitolo.getArrLingua()[0]);
				dettTitolo.getDetTitoloPFissaVO().setLingua2(utilityCampiTitolo.getArrLingua()[1]);
				dettTitolo.getDetTitoloPFissaVO().setLingua3(utilityCampiTitolo.getArrLingua()[2]);
			}
		}


		if (nodoCorrente.getDatiLegame() != null) {
			DatiLegame datiLegame = nodoCorrente.getDatiLegame();
			String tipoLegame = "";
			tipoLegame = datiLegame.getTipoLegame();

			String naturaArrivo = nodoCorrente.getNatura();
			int tipoLegameNumerico = 0;

			switch (numNatura) {
			case 1:

				dettTitolo.setCampoNotaAlLegame(datiLegame.getNotaLegame());
				dettTitolo.setCampoTipoLegame(tipoLegame);

				try {
					tipoLegameNumerico = Integer.parseInt(datiLegame.getTipoLegame());
				} catch (NumberFormatException exx) {
				}
				String codiceLegameCompleto = naturaPartenza + tipoLegameNumerico + naturaArrivo;
				try {
					codiceLegameCompleto = (codici.cercaDescrizioneCodice(
							codiceLegameCompleto,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				dettTitolo.setCampoTipoLegame(codiceLegameCompleto);

				// Inizio modifica almaviva2 06.12.2010 - BUG MANTIS 4042 inserito nuovo campo campoTipoLegameConNature
				// per salvare e portarsi dietro il legame con le nature dei titoli diapartenza e arrivo (DettaglioOggetti - getDettaglioTitolo)
				dettTitolo.setCampoTipoLegameConNature(naturaPartenza + codiceLegameCompleto + naturaArrivo);
				// Fine modifica almaviva2 06.12.2010 - BUG MANTIS 4042

				// La sequenza deve esserci solo per legami di tipo 01, 02 e 51.
				if ((codiceLegameCompleto != null)
						&& ((codiceLegameCompleto.equals("01"))
								|| (codiceLegameCompleto.equals("02")) || (codiceLegameCompleto.equals("51")))) {
					dettTitolo.setCampoSequenza(datiLegame.getSequenza());
				}

				// Campo SICI presente solo per legami S-N o N-S
				if (((naturaPartenza.equals("S")) && (naturaArrivo.equals("N")))
						|| ((naturaPartenza.equals("N")) && (naturaArrivo.equals("S")))) {
					dettTitolo.setCampoSici(datiLegame.getSici());
				}

				// dati del padre
				dettTitolo.setNominativoPadre(nodoCorrente.getParent().getDescription());
				dettTitolo.setIdPadre(nodoCorrente.getParent().getKey());

				break;
			case 2:

				// CAMPO SOTTOTIPOLEGAME SOLO SE IL TITOLO HA NATURA D
				// Se il nodo è null, controlla la String natura globale.
				if (nodoCorrente != null) {
					if (nodoCorrente.getNatura().equals("D")) {
						dettTitolo.setCampoSottoTipoLegame(datiLegame.getSottoTipoLegame());
					}
				}
				dettTitolo.setCampoNotaAlLegame(datiLegame.getNotaLegame());

				try {
					tipoLegameNumerico = Integer.parseInt(datiLegame.getTipoLegame());
				} catch (NumberFormatException exx) {
				}
				codiceLegameCompleto = naturaPartenza + tipoLegameNumerico + naturaArrivo;
				try {
					codiceLegameCompleto = (codici.cercaDescrizioneCodice(
							codiceLegameCompleto,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				dettTitolo.setCampoTipoLegame(codiceLegameCompleto);

				// Inizio modifica almaviva2 06.12.2010 - BUG MANTIS 4042 inserito nuovo campo campoTipoLegameConNature
				// per salvare e portarsi dietro il legame con le nature dei titoli diapartenza e arrivo (DettaglioOggetti - getDettaglioTitolo)
				dettTitolo.setCampoTipoLegameConNature(naturaPartenza + codiceLegameCompleto + naturaArrivo);
				// Fine modifica almaviva2 06.12.2010 - BUG MANTIS 4042



				// dati del padre
				dettTitolo.setNominativoPadre(nodoCorrente.getParent().getDescription());
				dettTitolo.setIdPadre(nodoCorrente.getParent().getKey());
				break;
			case 0:
				dettTitolo.setCampoNotaAlLegame(datiLegame.getNotaLegame());
				try {
					tipoLegameNumerico = Integer.parseInt(datiLegame.getTipoLegame());
				} catch (NumberFormatException exx) {
				}

				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
				if (tipoLegameNumerico == 0 && datiLegame.getTipoLegame().startsWith("531")) {
					codiceLegameCompleto = naturaPartenza + datiLegame.getTipoLegame() + naturaArrivo;
					try {
						codiceLegameCompleto = (codici.cercaDescrizioneCodice(
								codiceLegameCompleto,
								CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
								CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} else {
					codiceLegameCompleto = naturaPartenza + tipoLegameNumerico + naturaArrivo;
					try {
						codiceLegameCompleto = (codici.cercaDescrizioneCodice(
								codiceLegameCompleto,
								CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
								CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}


				dettTitolo.setCampoTipoLegame(codiceLegameCompleto);

				// Inizio modifica almaviva2 06.12.2010 - BUG MANTIS 4042 inserito nuovo campo campoTipoLegameConNature
				// per salvare e portarsi dietro il legame con le nature dei titoli diapartenza e arrivo (DettaglioOggetti - getDettaglioTitolo)
				dettTitolo.setCampoTipoLegameConNature(naturaPartenza + codiceLegameCompleto + naturaArrivo);
				// Fine modifica almaviva2 06.12.2010 - BUG MANTIS 4042


				// dati del padre
				dettTitolo.setNominativoPadre(nodoCorrente.getParent().getDescription());
				dettTitolo.setIdPadre(nodoCorrente.getParent().getKey());
				break;
			}
			dettTitolo.setTipoLegame("TI_TI");
		} else {
			dettTitolo.setTipoLegame("");
		}


		 if ("S".equalsIgnoreCase(utilityCampiTitolo.getNatura())) {
			 dettTitolo.getDetTitoloPFissaVO().setAreaTitNumer(
					 utilityCampiTitolo.getAreaNumerazioneCompleta(BID, sbnMarc));
		 }

		 if (("E".equalsIgnoreCase(utilityCampiTitolo.getTipoRecord()))
				 || ("F".equalsIgnoreCase(utilityCampiTitolo.getTipoRecord()))) {
			 dettTitolo.getDetTitoloPFissaVO().setAreaTitDatiMatem(
					 utilityCampiTitolo.getAreaDatiMatematici());
		 }

		return dettTitolo;
	}

	public DettaglioAutoreGeneraleVO getDettaglioAutore(SBNMarc sbnMarc, DatiElementoType datiElementoPassato,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String VID = nodoCorrente.getKey();

		String VIDRoot = "";
		String descVidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			VIDRoot = nodoCorrente.getParent().getKey();
			descVidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}

		DettaglioAutoreGeneraleVO dettAutore = new DettaglioAutoreGeneraleVO();
		DatiElementoType datiElemento;

		// prendo il datiElementoType
		if (sbnMarc == null && datiElementoPassato != null) {
			datiElemento = datiElementoPassato;
		} else {
			datiElemento = utilityCastor.getElementoAuthority(VID, sbnMarc);
		}


		dettAutore.setNome(utilityCastor
				.getNominativoDatiElemento(datiElemento));
		dettAutore.setDataIns(utilityCastor
				.getDataInserimentoDatiElemento(datiElemento));

		if (VID != null) {
			dettAutore.setVid(VID);
		}

		// data variazione
		dettAutore.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
		dettAutore.setVersione(datiElemento.getT005());

		// Livello di autorità
		SbnLivello livello = datiElemento.getLivelloAut();
		dettAutore.setLivAut(livello.toString());

		// FORMA
		String forma = utilityCastor.getFormaDatiElemento(datiElemento);
		dettAutore.setForma(forma);

		String notaDelCatalogatore = utilityCastor
				.getNotaDelCatalogatoreDatiElemento(datiElemento);
		String notaInformativa = utilityCastor
				.getNotaInformativaDatiElemento(datiElemento);
		String isadn = utilityCastor.getIsadnDatiElemento(datiElemento);
		String paese = utilityCastor.getPaeseDatiElemento(datiElemento);
		String norme = utilityCastor.getNormeDatiElemento(datiElemento);
		String agenzia = utilityCastor.getAgenziaDatiElemento(datiElemento);
		String lingua = utilityCastor.getLinguaDatiElemento(datiElemento);

		// TIPO NOME
		String tipoNome = utilityCastor.getTipoNomeDatiElemento(datiElemento);
		dettAutore.setTipoNome(tipoNome);

		// regole
		if (norme != null) {
			dettAutore.setNorme(norme);
		} else {
			dettAutore.setNorme("");
		}

		// Nota al nome
		if (notaInformativa != null) {
			dettAutore.setNotaInformativa(notaInformativa);

			// CERCO LA DATAZIONE NEL CAMPO NOTA INFORAMTIVA prima di " // "
			int index = notaInformativa.indexOf(" // ");
			if (index != -1) {
				// MODIFICO LE DATAZIONI
				dettAutore.setDatazioni(notaInformativa.substring(0, index));
				dettAutore.setNotaInformativa(notaInformativa.substring(
						index + 4, notaInformativa.length()));
			}
		} else {
			dettAutore.setDatazioni("");
			dettAutore.setNotaInformativa("");
		}

		// Nota nel catalogatore
		if (notaDelCatalogatore != null) {
			dettAutore.setNotaCatalogatore(notaDelCatalogatore);
		} else {
			dettAutore.setNotaCatalogatore("");
		}

		// Agenzia
		if (agenzia != null) {
			dettAutore.setAgenzia(agenzia);
		} else {
			dettAutore.setAgenzia("");
		}

		// lingua
		if (lingua != null) {
			dettAutore.setLingua(lingua);
		} else {
			dettAutore.setLingua("");
		}

		// codice paese
		if (paese != null) {
			dettAutore.setPaese(paese);
		} else {
			dettAutore.setPaese("");
		}

		// isadn
		if (isadn != null) {
			dettAutore.setIsadn(isadn);
		} else {
			dettAutore.setIsadn("");
		}

		// if il nodo selezionato non è uguale al Root
		if (VIDRoot != null) {
			switch (tipoLegame) {
			case DatiLegame.LEGAME_DOCUMENTO_AUTORE:
				dettAutore.setTipoLegame("TI_AU");
				if (datiLegame.getRelatorCode() != null) {
					try {
						dettAutore.setRelatorCode(codici.cercaDescrizioneCodice(datiLegame
								.getRelatorCode(), CodiciType.CODICE_LEGAME_TITOLO_AUTORE,CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
					// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
					//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
					if (datiLegame.getRelatorCode().equals("590") || datiLegame.getRelatorCode().equals("906")) {
						dettAutore.setSpecStrumVoci(datiLegame.getSpecStrumVoci());
					}
				}

				dettAutore.setResponsabilita(datiLegame.getResponsabilita());
				dettAutore.setIncerto(datiLegame.isIncerto());
				dettAutore.setSuperfluo(datiLegame.isSuperfluo());

				break;

			case DatiLegame.LEGAME_AUTORE_AUTORE:
				dettAutore.setTipoLegame("AU_AU");
				break;
			case DatiLegame.LEGAME_MARCA_AUTORE:
				dettAutore.setTipoLegame("MA_AU");
				break;
			case DatiLegame.LEGAME_AUTORE_MARCA:
				dettAutore.setTipoLegame("AU_MA");
				DatiElementoType datiElementoRoot = utilityCastor
						.getElementoAuthority(VIDRoot, sbnMarc);

				if (datiElementoRoot != null) {
					// salvo la data di variazione del padre
					dettAutore.setDataVariazionePadre(datiElementoRoot
							.getT005());
				}
				break;
			}

			dettAutore.setVidPadre(VIDRoot);
			dettAutore.setNominativoPadre(descVidRoot);
			dettAutore.setNotaAlLegame(notaAlLegame);
			dettAutore.setTipoLegameCastor(tipoLegameCastor);
		}

		// REPERTORI
		dettAutore.setLegamiRepertori("");
		if (forma.equals("A")) {
			List repertori = utilityCastor.getRepertoriElementoAuthority(VID,
					sbnMarc);

			String legamiPositivi = "";
			String legamiNegativi = "";

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					// per il momento lascio la tabella dei repertori in dettaglio

					Repertorio repertorio = (Repertorio) repertori.get(i);
					String tipoLegameRepertorio = repertorio.getTipoLegame()
							.trim();
					String notaAlLegameRepertorio = repertorio
							.getNotaAlLegame();
					String codiceRepertorio = repertorio.getCodiceRepertorio();

					TabellaNumSTDImpronteOriginarioVO tabRepert;
					tabRepert = new TabellaNumSTDImpronteOriginarioVO(tipoLegameRepertorio,
							codiceRepertorio, " ", notaAlLegameRepertorio);
					dettAutore.addListaRepertori(tabRepert);

					if (tipoLegameRepertorio.equals("Si")) {
						if ((notaAlLegameRepertorio != null)
								&& (!notaAlLegameRepertorio.equals(""))
								&& (!notaAlLegameRepertorio.equals(" "))) {
							if (legamiPositivi.equals("")) {
								legamiPositivi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
							} else {
								legamiPositivi = legamiPositivi
										+ ", "
										+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
							}
						} else {
							if (legamiPositivi.equals("")) {
								legamiPositivi = codiceRepertorio;
							} else {
								legamiPositivi = legamiPositivi + ", "
										+ codiceRepertorio;
							}
						}
					} else
					// ESITO NEGATIVO
					if ((notaAlLegameRepertorio != null)
							&& (!notaAlLegameRepertorio.equals(""))
							&& (!notaAlLegameRepertorio.equals(" "))) {
						if (legamiNegativi.equals("")) {
							legamiNegativi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
						} else {
							legamiNegativi = legamiNegativi
									+ ", "
									+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
						}
					} else {
						if (legamiNegativi.equals("")) {
							legamiNegativi = codiceRepertorio;
						} else {
							legamiNegativi = legamiNegativi + ", "
									+ codiceRepertorio;
						}
					}
				}

				if (legamiNegativi.equals("")) {
					dettAutore.setLegamiRepertori(legamiPositivi);
				} else {
					dettAutore.setLegamiRepertori(legamiPositivi + " // "
							+ legamiNegativi);
				}
			}
		}

		return dettAutore;
	}

	public DettaglioAutoreGeneraleVO getDettaglioAutore(SBNMarc sbnMarc,
			TreeElementViewAutori nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String VID = nodoCorrente.getKey();
		String VIDRoot = "";
		String descVidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			VIDRoot = nodoCorrente.getParent().getKey();
			descVidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}
		DettaglioAutoreGeneraleVO dettAutore = new DettaglioAutoreGeneraleVO();

		// prendo il datiElementoType
		DatiElementoType datiElemento = utilityCastor.getElementoAuthority(VID,
				sbnMarc);

		dettAutore.setNome(utilityCastor
				.getNominativoDatiElemento(datiElemento));
		dettAutore.setDataIns(utilityCastor
				.getDataInserimentoDatiElemento(datiElemento));

		if (VID != null) {
			dettAutore.setVid(VID);
		}

		// data variazione
		dettAutore.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
		dettAutore.setVersione(datiElemento.getT005());

		// Livello di autorità
		SbnLivello livello = datiElemento.getLivelloAut();
		dettAutore.setLivAut(livello.toString());

		// FORMA
		String forma = utilityCastor.getFormaDatiElemento(datiElemento);
		dettAutore.setForma(forma);

		String notaDelCatalogatore = utilityCastor
				.getNotaDelCatalogatoreDatiElemento(datiElemento);
		String notaInformativa = utilityCastor
				.getNotaInformativaDatiElemento(datiElemento);
		String isadn = utilityCastor.getIsadnDatiElemento(datiElemento);
		String paese = utilityCastor.getPaeseDatiElemento(datiElemento);
		String norme = utilityCastor.getNormeDatiElemento(datiElemento);
		String agenzia = utilityCastor.getAgenziaDatiElemento(datiElemento);
		String lingua = utilityCastor.getLinguaDatiElemento(datiElemento);

		// TIPO NOME
		String tipoNome = utilityCastor.getTipoNomeDatiElemento(datiElemento);
		dettAutore.setTipoNome(tipoNome);

		// regole
		if (norme != null) {
			dettAutore.setNorme(norme);
		} else {
			dettAutore.setNorme(" ");
		}

		// Nota al nome
		if (notaInformativa != null) {
			dettAutore.setNotaInformativa(notaInformativa);

			// CERCO LA DATAZIONE NEL CAMPO NOTA INFORAMTIVA prima di " // "
			int index = notaInformativa.indexOf(" // ");
			if (index != -1) {
				// MODIFICO LE DATAZIONI
				dettAutore.setDatazioni(notaInformativa.substring(0, index));
				dettAutore.setNotaInformativa(notaInformativa.substring(
						index + 4, notaInformativa.length()));
			}
		} else {
			dettAutore.setDatazioni(" ");
			dettAutore.setNotaInformativa(" ");
		}

		// Nota nel catalogatore
		if (notaDelCatalogatore != null) {
			dettAutore.setNotaCatalogatore(notaDelCatalogatore);
		} else {
			dettAutore.setNotaCatalogatore(" ");
		}

		// Agenzia
		if (agenzia != null) {
			dettAutore.setAgenzia(agenzia);
		} else {
			dettAutore.setAgenzia(" ");
		}

		// lingua
		if (lingua != null) {
			dettAutore.setLingua(lingua);
		} else {
			dettAutore.setLingua(" ");
		}

		// codice paese
		if (paese != null) {
			dettAutore.setPaese(paese);
		} else {
			dettAutore.setPaese(" ");
		}

		// isadn
		if (isadn != null) {
			dettAutore.setIsadn(isadn);
		} else {
			dettAutore.setIsadn(" ");
		}

		// if il nodo selezionato non è uguale al Root
		if (VIDRoot != null
				|| !VIDRoot.equals("")) {
			switch (tipoLegame) {
			case DatiLegame.LEGAME_DOCUMENTO_AUTORE:
				dettAutore.setTipoLegame("TI_AU");
				if (datiLegame.getRelatorCode() != null) {
					try {
						dettAutore.setRelatorCode(codici.cercaDescrizioneCodice(datiLegame
								.getRelatorCode(), CodiciType.CODICE_LEGAME_TITOLO_AUTORE, CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
					// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
					//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
					if (datiLegame.getRelatorCode().equals("590") || datiLegame.getRelatorCode().equals("906")) {
						dettAutore.setSpecStrumVoci(datiLegame.getSpecStrumVoci());
					}
				}

				dettAutore.setResponsabilita(datiLegame.getResponsabilita());
				dettAutore.setIncerto(datiLegame.isIncerto());
				dettAutore.setSuperfluo(datiLegame.isSuperfluo());

				break;

			case DatiLegame.LEGAME_AUTORE_AUTORE:
				dettAutore.setTipoLegame("AU_AU");
				break;
			case DatiLegame.LEGAME_MARCA_AUTORE:
				dettAutore.setTipoLegame("MA_AU");
				break;
			case DatiLegame.LEGAME_AUTORE_MARCA:
				dettAutore.setTipoLegame("AU_MA");
				DatiElementoType datiElementoRoot = utilityCastor
						.getElementoAuthority(VIDRoot, sbnMarc);

				if (datiElementoRoot != null) {
					// salvo la data di variazione del padre
					dettAutore.setDataVariazionePadre(datiElementoRoot
							.getT005());
				}
				break;
			}

			dettAutore.setVidPadre(VIDRoot);
			dettAutore.setNominativoPadre(descVidRoot);
			dettAutore.setNotaAlLegame(notaAlLegame);
			dettAutore.setTipoLegameCastor(tipoLegameCastor);
		}

		// REPERTORI
		dettAutore.setLegamiRepertori("");
		if (forma.equals("A")) {
			List repertori = utilityCastor.getRepertoriElementoAuthority(VID,
					sbnMarc);

			String legamiPositivi = "";
			String legamiNegativi = "";

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					// per il momento lascio la tabella dei repertori in
					// dettaglio
					// this.addRepertorio((Repertorio) repertori.get(i)// ,
					// // repertoriTable
					// );

					Repertorio repertorio = (Repertorio) repertori.get(i);
					String tipoLegameRepertorio = repertorio.getTipoLegame()
							.trim();
					String notaAlLegameRepertorio = repertorio
							.getNotaAlLegame();
					String codiceRepertorio = repertorio.getCodiceRepertorio();

					TabellaNumSTDImpronteOriginarioVO tabRepert;
					tabRepert = new TabellaNumSTDImpronteOriginarioVO(tipoLegameRepertorio,
							codiceRepertorio, " ", notaAlLegameRepertorio);
					dettAutore.addListaRepertori(tabRepert);

					if (tipoLegameRepertorio.equals("Si")) {
						if ((notaAlLegameRepertorio != null)
								&& (!notaAlLegameRepertorio.equals(""))
								&& (!notaAlLegameRepertorio.equals(" "))) {
							if (legamiPositivi.equals("")) {
								legamiPositivi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
							} else {
								legamiPositivi = legamiPositivi
										+ ", "
										+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
							}
						} else {
							if (legamiPositivi.equals("")) {
								legamiPositivi = codiceRepertorio;
							} else {
								legamiPositivi = legamiPositivi + ", "
										+ codiceRepertorio;
							}
						}
					} else
					// ESITO NEGATIVO
					if ((notaAlLegameRepertorio != null)
							&& (!notaAlLegameRepertorio.equals(""))
							&& (!notaAlLegameRepertorio.equals(" "))) {
						if (legamiNegativi.equals("")) {
							legamiNegativi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
						} else {
							legamiNegativi = legamiNegativi
									+ ", "
									+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
						}
					} else {
						if (legamiNegativi.equals("")) {
							legamiNegativi = codiceRepertorio;
						} else {
							legamiNegativi = legamiNegativi + ", "
									+ codiceRepertorio;
						}
					}
				}

				if (legamiNegativi.equals("")) {
					dettAutore.setLegamiRepertori(legamiPositivi);
				} else {
					dettAutore.setLegamiRepertori(legamiPositivi + " // "
							+ legamiNegativi);
				}
			}
		}

		return dettAutore;
	}

	public DettaglioAutoreGeneraleVO getDettaglioAutore(SBNMarc sbnMarc,
			TreeElementViewMarche nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String VID = nodoCorrente.getKey();

		String VIDRoot = "";
		String descVidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			VIDRoot = nodoCorrente.getParent().getKey();
			descVidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}

		DettaglioAutoreGeneraleVO dettAutore = new DettaglioAutoreGeneraleVO();

		// prendo il datiElementoType
		DatiElementoType datiElemento = utilityCastor.getElementoAuthority(VID,
				sbnMarc);

		dettAutore.setNome(utilityCastor
				.getNominativoDatiElemento(datiElemento));
		dettAutore.setDataIns(utilityCastor
				.getDataInserimentoDatiElemento(datiElemento));

		if (VID != null) {
			dettAutore.setVid(VID);
		}

		// data variazione
		dettAutore.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
		dettAutore.setVersione(datiElemento.getT005());

		// Livello di autorità
		SbnLivello livello = datiElemento.getLivelloAut();
		dettAutore.setLivAut(livello.toString());

		// FORMA
		String forma = utilityCastor.getFormaDatiElemento(datiElemento);
		dettAutore.setForma(forma);

		String notaDelCatalogatore = utilityCastor
				.getNotaDelCatalogatoreDatiElemento(datiElemento);
		String notaInformativa = utilityCastor
				.getNotaInformativaDatiElemento(datiElemento);
		String isadn = utilityCastor.getIsadnDatiElemento(datiElemento);
		String paese = utilityCastor.getPaeseDatiElemento(datiElemento);
		String norme = utilityCastor.getNormeDatiElemento(datiElemento);
		String agenzia = utilityCastor.getAgenziaDatiElemento(datiElemento);
		String lingua = utilityCastor.getLinguaDatiElemento(datiElemento);

		// TIPO NOME
		String tipoNome = utilityCastor.getTipoNomeDatiElemento(datiElemento);
		dettAutore.setTipoNome(tipoNome);

		// regole
		if (norme != null) {
			dettAutore.setNorme(norme);
		} else {
			dettAutore.setNorme(" ");
		}

		// Nota al nome
		if (notaInformativa != null) {
			dettAutore.setNotaInformativa(notaInformativa);

			// CERCO LA DATAZIONE NEL CAMPO NOTA INFORAMTIVA prima di " // "
			int index = notaInformativa.indexOf(" // ");
			if (index != -1) {
				// MODIFICO LE DATAZIONI
				dettAutore.setDatazioni(notaInformativa.substring(0, index));
				dettAutore.setNotaInformativa(notaInformativa.substring(
						index + 4, notaInformativa.length()));
			}
		} else {
			dettAutore.setDatazioni(" ");
			dettAutore.setNotaInformativa(" ");
		}

		// Nota nel catalogatore
		if (notaDelCatalogatore != null) {
			dettAutore.setNotaCatalogatore(notaDelCatalogatore);
		} else {
			dettAutore.setNotaCatalogatore(" ");
		}

		// Agenzia
		if (agenzia != null) {
			dettAutore.setAgenzia(agenzia);
		} else {
			dettAutore.setAgenzia(" ");
		}

		// lingua
		if (lingua != null) {
			dettAutore.setLingua(lingua);
		} else {
			dettAutore.setLingua(" ");
		}

		// codice paese
		if (paese != null) {
			dettAutore.setPaese(paese);
		} else {
			dettAutore.setPaese(" ");
		}

		// isadn
		if (isadn != null) {
			dettAutore.setIsadn(isadn);
		} else {
			dettAutore.setIsadn(" ");
		}

		// if il nodo selezionato non è uguale al Root
		if (VIDRoot != null) {
			switch (tipoLegame) {
			case DatiLegame.LEGAME_DOCUMENTO_AUTORE:
				dettAutore.setTipoLegame("TI_AU");
				if (datiLegame.getRelatorCode() != null) {
					try {
						dettAutore.setRelatorCode(codici.cercaDescrizioneCodice(datiLegame.getRelatorCode(),
										CodiciType.CODICE_LEGAME_TITOLO_AUTORE,	CodiciRicercaType.RICERCA_CODICE_UNIMARC));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
					// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
					// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
					//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
					if (datiLegame.getRelatorCode().equals("590") || datiLegame.getRelatorCode().equals("906")) {
						dettAutore.setSpecStrumVoci(datiLegame.getSpecStrumVoci());
					}
				}

				dettAutore.setResponsabilita(datiLegame.getResponsabilita());
				dettAutore.setIncerto(datiLegame.isIncerto());
				dettAutore.setSuperfluo(datiLegame.isSuperfluo());

				break;

			case DatiLegame.LEGAME_AUTORE_AUTORE:
				dettAutore.setTipoLegame("AU_AU");
				break;
			case DatiLegame.LEGAME_MARCA_AUTORE:
				dettAutore.setTipoLegame("MA_AU");
				break;
			case DatiLegame.LEGAME_AUTORE_MARCA:
				dettAutore.setTipoLegame("AU_MA");
				DatiElementoType datiElementoRoot = utilityCastor
						.getElementoAuthority(VIDRoot, sbnMarc);

				if (datiElementoRoot != null) {
					// salvo la data di variazione del padre
					dettAutore.setDataVariazionePadre(datiElementoRoot
							.getT005());
				}
				break;
			}

			dettAutore.setVidPadre(VIDRoot);
			dettAutore.setNominativoPadre(descVidRoot);
			dettAutore.setNotaAlLegame(notaAlLegame);
			dettAutore.setTipoLegameCastor(tipoLegameCastor);
		}

		// REPERTORI
		dettAutore.setLegamiRepertori("");
		if (forma.equals("A")) {
			List repertori = utilityCastor.getRepertoriElementoAuthority(VID,
					sbnMarc);

			String legamiPositivi = "";
			String legamiNegativi = "";

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					// per il momento lascio la tabella dei repertori in
					// dettaglio
//					this.addRepertorio((Repertorio) repertori.get(i)// ,
//							// repertoriTable
//							);

					Repertorio repertorio = (Repertorio) repertori.get(i);
					String tipoLegameRepertorio = repertorio.getTipoLegame()
							.trim();
					String notaAlLegameRepertorio = repertorio
							.getNotaAlLegame();
					String codiceRepertorio = repertorio.getCodiceRepertorio();

					TabellaNumSTDImpronteOriginarioVO tabRepert;
					tabRepert = new TabellaNumSTDImpronteOriginarioVO(tipoLegameRepertorio,
							codiceRepertorio, " ", notaAlLegameRepertorio);
					dettAutore.addListaRepertori(tabRepert);

					if (tipoLegameRepertorio.equals("Si")) {
						if ((notaAlLegameRepertorio != null)
								&& (!notaAlLegameRepertorio.equals(""))
								&& (!notaAlLegameRepertorio.equals(" "))) {
							if (legamiPositivi.equals("")) {
								legamiPositivi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
							} else {
								legamiPositivi = legamiPositivi
										+ ", "
										+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
							}
						} else {
							if (legamiPositivi.equals("")) {
								legamiPositivi = codiceRepertorio;
							} else {
								legamiPositivi = legamiPositivi + ", "
										+ codiceRepertorio;
							}
						}
					} else
					// ESITO NEGATIVO
					if ((notaAlLegameRepertorio != null)
							&& (!notaAlLegameRepertorio.equals(""))
							&& (!notaAlLegameRepertorio.equals(" "))) {
						if (legamiNegativi.equals("")) {
							legamiNegativi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
						} else {
							legamiNegativi = legamiNegativi
									+ ", "
									+ (codiceRepertorio + "/" + notaAlLegameRepertorio);
						}
					} else {
						if (legamiNegativi.equals("")) {
							legamiNegativi = codiceRepertorio;
						} else {
							legamiNegativi = legamiNegativi + ", "
									+ codiceRepertorio;
						}
					}
				}

				if (legamiNegativi.equals("")) {
					dettAutore.setLegamiRepertori(legamiPositivi);
				} else {
					dettAutore.setLegamiRepertori(legamiPositivi + " // "
							+ legamiNegativi);
				}
			}
		}

		return dettAutore;
	}

	public DettaglioMarcaGeneraleVO getDettaglioMarca(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String MID = nodoCorrente.getKey();

		String MIDRoot = "";
		String descMidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			MIDRoot = nodoCorrente.getParent().getKey();
			descMidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}

		DettaglioMarcaGeneraleVO dettMarca = new DettaglioMarcaGeneraleVO();

		DatiElementoType datiElemento = utilityCastor.getDatiElemento(MID,
				sbnMarc);

		// Costruisce il dettaglio solo se il DatiElementoType
		// non è null, così si ovvia a casi in cui i dati della
		// marca sono danneggiati e quindi possono provocare errori.
		if (datiElemento != null) {

			// Mid: identificativo della marca
			if (MID != null) {
				dettMarca.setMid(MID);
			}

			// Descrizione
			String descrizione = utilityCastor
					.getNominativoDatiElemento(datiElemento);

			if (descrizione != null) {
				dettMarca.setDesc(descrizione);
			}

			// Data di inserimento
			dettMarca.setDataIns(utilityCastor
					.getDataInserimentoDatiElemento(datiElemento));

			// Data variazione
			dettMarca.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
			dettMarca.setVersione(datiElemento.getT005());

			if (datiElemento instanceof MarcaType) {
				MarcaType marcaType = (MarcaType) datiElemento;
				A921 a921 = marcaType.getT921();

				// Il metodo restituisce un array di String la cui dimensione
				// varia a seconda del numero delle parole chiave della marca
				dettMarca.setParChiave1("");
				dettMarca.setParChiave2("");
				dettMarca.setParChiave3("");
				dettMarca.setParChiave4("");
				dettMarca.setParChiave5("");

				if (a921.getB_921().length >= 1) {
					dettMarca.setParChiave1(a921.getB_921()[0]);
				}
				if (a921.getB_921().length >= 2) {
					dettMarca.setParChiave2(a921.getB_921()[1]);
				}
				if (a921.getB_921().length >= 3) {
					dettMarca.setParChiave3(a921.getB_921()[2]);
				}
				if (a921.getB_921().length >= 4) {
					dettMarca.setParChiave4(a921.getB_921()[3]);
				}
				if (a921.getB_921().length >= 5) {
					dettMarca.setParChiave5(a921.getB_921()[4]);
				}

				dettMarca.setMotto(a921.getE_921());
				dettMarca.setNota(a921.getD_921());

				//IMMAGINI LEGATE A MARCA
				int imgCount = marcaType.getT856Count();
				if (imgCount > 0) {
//					C856 c856 = marcaType.getT856(0);
					//almaviva5_20090423 #2834 il tag non é obbligatorio
					//if (c856.getU_856() != null)
					for ( int i = 0 ; i < imgCount; i++) {
						if (marcaType.getT856(i).getC9_856_1().length > 0) {
							dettMarca.getListaImmagini().add( marcaType.getT856(i).getC9_856_1());
						}
					}
				}
			}

			// Livello di autorità
			SbnLivello livello = datiElemento.getLivelloAut();
			dettMarca.setLivAut(livello.toString());

			if (MIDRoot != null) {
				switch (tipoLegame) {
				case DatiLegame.LEGAME_DOCUMENTO_MARCA:
					dettMarca.setTipoLegame("TI_MA");
					break;
				case DatiLegame.LEGAME_AUTORE_AUTORE:
				case DatiLegame.LEGAME_MARCA_AUTORE:
				case DatiLegame.LEGAME_AUTORE_MARCA:
					dettMarca.setTipoLegame("AU_MA");
					DatiElementoType datiElementoRoot = utilityCastor
							.getElementoAuthority(MIDRoot, sbnMarc);
					if (datiElementoRoot != null) {
						// salvo la data di variazione del padre
						dettMarca.setDataVariazionePadre(datiElementoRoot
								.getT005());
					}
					break;
				}// end switch

				dettMarca.setMidPadre(MIDRoot);
				dettMarca.setDescrizionePadre(descMidRoot);
				dettMarca.setNotaAlLegame(notaAlLegame);
				dettMarca.setTipoLegameCastor(tipoLegameCastor);
			}

			// REPERTORI
			List repertori = utilityCastor.getRepertoriElementoAuthority(MID,
					sbnMarc);

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					dettMarca = setCitazioniPerDettaglio((Repertorio) repertori
							.get(i), i, dettMarca);
				}
			}
		}
		return dettMarca;
	}

	public DettaglioMarcaGeneraleVO getDettaglioMarca(SBNMarc sbnMarc,
			TreeElementViewAutori nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String MID = nodoCorrente.getKey();

		String MIDRoot = "";
		String descMidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			MIDRoot = nodoCorrente.getParent().getKey();
			descMidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}

		DettaglioMarcaGeneraleVO dettMarca = new DettaglioMarcaGeneraleVO();

		DatiElementoType datiElemento = utilityCastor.getDatiElemento(MID,
				sbnMarc);

		// Costruisce il dettaglio solo se il DatiElementoType
		// non è null, così si ovvia a casi in cui i dati della
		// marca sono danneggiati e quindi possono provocare errori.
		if (datiElemento != null) {

			// Mid: identificativo della marca
			if (MID != null) {
				dettMarca.setMid(MID);
			}

			// Descrizione
			String descrizione = utilityCastor
					.getNominativoDatiElemento(datiElemento);

			if (descrizione != null) {
				dettMarca.setDesc(descrizione);
			}

			// Data di inserimento
			dettMarca.setDataIns(utilityCastor
					.getDataInserimentoDatiElemento(datiElemento));

			// Data variazione
			dettMarca.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
			dettMarca.setVersione(datiElemento.getT005());

			if (datiElemento instanceof MarcaType) {
				MarcaType marcaType = (MarcaType) datiElemento;
				A921 a921 = marcaType.getT921();

				// Il metodo restituisce un array di String la cui dimensione
				// varia a seconda del numero delle parole chiave della marca
				dettMarca.setParChiave1("");
				dettMarca.setParChiave2("");
				dettMarca.setParChiave3("");
				dettMarca.setParChiave4("");
				dettMarca.setParChiave5("");

				if (a921.getB_921().length >= 1) {
					dettMarca.setParChiave1(a921.getB_921()[0]);
				}
				if (a921.getB_921().length >= 2) {
					dettMarca.setParChiave2(a921.getB_921()[1]);
				}
				if (a921.getB_921().length >= 3) {
					dettMarca.setParChiave3(a921.getB_921()[2]);
				}
				if (a921.getB_921().length >= 4) {
					dettMarca.setParChiave4(a921.getB_921()[3]);
				}
				if (a921.getB_921().length >= 5) {
					dettMarca.setParChiave5(a921.getB_921()[4]);
				}

				dettMarca.setMotto(a921.getE_921());
				dettMarca.setNota(a921.getD_921());

				//IMMAGINI LEGATE A MARCA
				for ( int i = 0 ; i < marcaType.getT856Count(); i++) {
					dettMarca.getListaImmagini().add( marcaType.getT856(i).getC9_856_1());
				}

			}

			// Livello di autorità
			SbnLivello livello = datiElemento.getLivelloAut();
			dettMarca.setLivAut(livello.toString());

			if (MIDRoot != null) {
				switch (tipoLegame) {
				case DatiLegame.LEGAME_DOCUMENTO_MARCA:
					dettMarca.setTipoLegame("TI_MA");
					break;
				case DatiLegame.LEGAME_AUTORE_AUTORE:
				case DatiLegame.LEGAME_MARCA_AUTORE:
				case DatiLegame.LEGAME_AUTORE_MARCA:
					dettMarca.setTipoLegame("AU_MA");
					DatiElementoType datiElementoRoot = utilityCastor
							.getElementoAuthority(MIDRoot, sbnMarc);
					if (datiElementoRoot != null) {
						// salvo la data di variazione del padre
						dettMarca.setDataVariazionePadre(datiElementoRoot
								.getT005());
					}
					break;
				}// end switch

				dettMarca.setMidPadre(MIDRoot);
				dettMarca.setDescrizionePadre(descMidRoot);
				dettMarca.setNotaAlLegame(notaAlLegame);
				dettMarca.setTipoLegameCastor(tipoLegameCastor);
			}

			// REPERTORI
			List repertori = utilityCastor.getRepertoriElementoAuthority(MID,
					sbnMarc);

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					dettMarca = setCitazioniPerDettaglio((Repertorio) repertori
							.get(i), i, dettMarca);
				}
			}
		}
		return dettMarca;
	}

	public DettaglioMarcaGeneraleVO getDettaglioMarca(SBNMarc sbnMarc,
			TreeElementViewMarche nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String MID = nodoCorrente.getKey();

		String MIDRoot = "";
		String descMidRoot = "";
		DatiLegame datiLegame = new DatiLegame();
		String notaAlLegame = null;
		String tipoLegameCastor = null;

		if (nodoCorrente.getParent() != null) {
			MIDRoot = nodoCorrente.getParent().getKey();
			descMidRoot = nodoCorrente.getParent().getDescription();
			datiLegame = nodoCorrente.getDatiLegame();
			notaAlLegame = datiLegame.getNotaLegame();
			tipoLegameCastor = datiLegame.getTipoLegame();
		}

		DettaglioMarcaGeneraleVO dettMarca = new DettaglioMarcaGeneraleVO();

		DatiElementoType datiElemento = utilityCastor.getDatiElemento(MID,
				sbnMarc);

		// Costruisce il dettaglio solo se il DatiElementoType
		// non è null, così si ovvia a casi in cui i dati della
		// marca sono danneggiati e quindi possono provocare errori.
		if (datiElemento != null) {

			// Mid: identificativo della marca
			if (MID != null) {
				dettMarca.setMid(MID);
			}

			// Descrizione
			String descrizione = utilityCastor
					.getNominativoDatiElemento(datiElemento);

			if (descrizione != null) {
				dettMarca.setDesc(descrizione);
			}

			// Data di inserimento
			dettMarca.setDataIns(utilityCastor
					.getDataInserimentoDatiElemento(datiElemento));

			// Data variazione
			dettMarca.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));
			dettMarca.setVersione(datiElemento.getT005());

			if (datiElemento instanceof MarcaType) {
				MarcaType marcaType = (MarcaType) datiElemento;
				A921 a921 = marcaType.getT921();

				// Il metodo restituisce un array di String la cui dimensione
				// varia a seconda del numero delle parole chiave della marca
				dettMarca.setParChiave1("");
				dettMarca.setParChiave2("");
				dettMarca.setParChiave3("");
				dettMarca.setParChiave4("");
				dettMarca.setParChiave5("");

				if (a921.getB_921().length >= 1) {
					dettMarca.setParChiave1(a921.getB_921()[0]);
				}
				if (a921.getB_921().length >= 2) {
					dettMarca.setParChiave2(a921.getB_921()[1]);
				}
				if (a921.getB_921().length >= 3) {
					dettMarca.setParChiave3(a921.getB_921()[2]);
				}
				if (a921.getB_921().length >= 4) {
					dettMarca.setParChiave4(a921.getB_921()[3]);
				}
				if (a921.getB_921().length >= 5) {
					dettMarca.setParChiave5(a921.getB_921()[4]);
				}

				dettMarca.setMotto(a921.getE_921());
				dettMarca.setNota(a921.getD_921());

				//IMMAGINI LEGATE A MARCA
				for ( int i = 0 ; i < marcaType.getT856Count(); i++) {
					dettMarca.getListaImmagini().add( marcaType.getT856(i).getC9_856_1());
				}
			}

			// Livello di autorità
			SbnLivello livello = datiElemento.getLivelloAut();
			dettMarca.setLivAut(livello.toString());

			if (MIDRoot != null) {
				switch (tipoLegame) {
				case DatiLegame.LEGAME_DOCUMENTO_MARCA:
					dettMarca.setTipoLegame("TI_MA");
					break;
				case DatiLegame.LEGAME_AUTORE_AUTORE:
				case DatiLegame.LEGAME_MARCA_AUTORE:
				case DatiLegame.LEGAME_AUTORE_MARCA:
					dettMarca.setTipoLegame("AU_MA");
					DatiElementoType datiElementoRoot = utilityCastor
							.getElementoAuthority(MIDRoot, sbnMarc);
					if (datiElementoRoot != null) {
						// salvo la data di variazione del padre
						dettMarca.setDataVariazionePadre(datiElementoRoot
								.getT005());
					}
					break;
				}// end switch

				dettMarca.setMidPadre(MIDRoot);
				dettMarca.setDescrizionePadre(descMidRoot);
				dettMarca.setNotaAlLegame(notaAlLegame);
				dettMarca.setTipoLegameCastor(tipoLegameCastor);
			}

			// REPERTORI
			List repertori = utilityCastor.getRepertoriElementoAuthority(MID,
					sbnMarc);

			if (repertori != null) {
				for (int i = 0; i < repertori.size(); i++) {
					dettMarca = setCitazioniPerDettaglio((Repertorio) repertori
							.get(i), i, dettMarca);
				}
			}
		}
		return dettMarca;
	}

	/**
	 * Valorizza i campi delle citazioni standard di JPanelDettaglioMarca
	 *
	 * @param rep
	 *            Repertorio
	 * @param i
	 *            int
	 */
	public DettaglioMarcaGeneraleVO setCitazioniPerDettaglio(Repertorio rep,
			int i, DettaglioMarcaGeneraleVO dettMarca) {
		int citazione = 0;
		String citazioneString;

		switch (i) {
		case 0:
			dettMarca.setCampoCodiceRep1Old(rep.getCodiceRepertorio());
			citazione = rep.getCitazione();
			citazioneString = new Integer(citazione).toString();
			dettMarca.setCampoProgressivoRep1Old(citazioneString);

			break;

		case 1:
			dettMarca.setCampoCodiceRep2Old(rep.getCodiceRepertorio());
			citazione = rep.getCitazione();
			citazioneString = new Integer(citazione).toString();
			dettMarca.setCampoProgressivoRep2Old(citazioneString);

			break;

		case 2:
			dettMarca.setCampoCodiceRep3Old(rep.getCodiceRepertorio());
			citazione = rep.getCitazione();
			citazioneString = new Integer(citazione).toString();
			dettMarca.setCampoProgressivoRep3Old(citazioneString);

			break;
		}
		return dettMarca;
	}

	public DettaglioSoggettoGeneraleVO getDettaglioSoggetto(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String CID = nodoCorrente.getKey();
		String CIDRoot = nodoCorrente.getParent().getKey();
		String descCidRoot = nodoCorrente.getParent().getDescription();
		DatiLegame datiLegame = nodoCorrente.getDatiLegame();

		DettaglioSoggettoGeneraleVO dettSoggetto = new DettaglioSoggettoGeneraleVO();
		String stringaSoggetto = "";
		String codSoggettario = "";

		// DatiElementoType
		DatiElementoType datiElemento = utilityCastor.getDatiElemento(CID,
				sbnMarc);

		// Livello di Autorità
		SbnLivello livello = datiElemento.getLivelloAut();
		dettSoggetto.setLivAut(livello.toString());

		if (CID != null) {
			dettSoggetto.setCid(CID);
		}

		SoggettoType soggettoType = (SoggettoType) datiElemento;
		A250 a250 = soggettoType.getT250();

		//almaviva5_20121005 evolutive CFI
		stringaSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

		if (stringaSoggetto != null) {
			dettSoggetto.setCampoStringaSoggetto(stringaSoggetto);
		}

		// CODICE SOGGETTARIO
		dettSoggetto.setCampoSoggettario(SemanticaUtil.getSoggettarioSBN(a250));

		// Data di inserimento
		dettSoggetto.setDataIns(utilityCastor
				.getDataInserimentoDatiElemento(datiElemento));

		// Data variazione
		dettSoggetto.setDataAgg(utilityDate.converteDataSBN(datiElemento.getT005()));

		if (tipoLegame != DatiLegame.LEGAME_NULL) {
			dettSoggetto.setBidPadre(CIDRoot);
			dettSoggetto.setDescrizionePadre(descCidRoot);
			dettSoggetto.setNotaAlLegame(datiLegame.getNotaLegame());
			dettSoggetto.setTipoLegameValore(datiLegame.getTipoLegame());
		}
		dettSoggetto.setTipoLegame("TI_SO");

		//almaviva5_20120424 evolutive CFI
		dettSoggetto.setEdizioneSoggettario(a250.getEdizione() != null ? a250.getEdizione().toString() : null);

		return dettSoggetto;
	}

	public DettaglioClasseGeneraleVO getDettaglioClasse(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String ident = nodoCorrente.getKey();
		String identRoot = nodoCorrente.getParent().getKey();
		String descIdentRoot = nodoCorrente.getParent().getDescription();
		DatiLegame datiLegame = nodoCorrente.getDatiLegame();

		DettaglioClasseGeneraleVO dettClasse = new DettaglioClasseGeneraleVO();
		if (ident != null) {
			try {
				dettClasse.setIdentificativo(ident);
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dettClasse.setDescrizione(nodoCorrente.getDescription());
		}

		if (tipoLegame != DatiLegame.LEGAME_NULL) {
			dettClasse.setIdPadre(identRoot);
			dettClasse.setDescrizionePadre(descIdentRoot);
			dettClasse.setTipoLegameValore(datiLegame.getTipoLegame());
			//almaviva5_20181022 #6749
			dettClasse.setNotaAlLegame(datiLegame.getNotaLegame());
		}

		dettClasse.setTipoLegame("TI_CL");
		return dettClasse;
	}

	public DettaglioTermineThesauroGeneraleVO getDettaglioTermineThesauro(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String ident = nodoCorrente.getKey();
		String identRoot = nodoCorrente.getParent().getKey();
		String descIdentRoot = nodoCorrente.getParent().getDescription();
		DatiLegame datiLegame = nodoCorrente.getDatiLegame();

		DettaglioTermineThesauroGeneraleVO dettTermThesauro = new DettaglioTermineThesauroGeneraleVO();
		if (ident != null) {
			dettTermThesauro.setIdentificativo(ident);
			dettTermThesauro.setDescrizione(nodoCorrente.getDescription());
		}

		if (tipoLegame != DatiLegame.LEGAME_NULL) {
			dettTermThesauro.setIdPadre(identRoot);
			dettTermThesauro.setDescrizionePadre(descIdentRoot);
			dettTermThesauro.setTipoLegameValore(datiLegame.getTipoLegame());
		}

		dettTermThesauro.setTipoLegame("TI_TH");
		return dettTermThesauro;
	}


	public DettaglioLuogoGeneraleVO getDettaglioLuogo(SBNMarc sbnMarc,
			TreeElementViewTitoli nodoCorrente, String naturaPartenza,
			int tipoLegame) {

		String LID = nodoCorrente.getKey();
		String LIDRoot = "";
		String descLidRoot = "";

		if (nodoCorrente.getParent() != null) {
			LIDRoot = nodoCorrente.getParent().getKey();
			descLidRoot = nodoCorrente.getParent().getDescription();
		}

		DatiLegame datiLegame = nodoCorrente.getDatiLegame();

		DettaglioLuogoGeneraleVO dettLuogo = new DettaglioLuogoGeneraleVO();
		if (LID != null) {
			dettLuogo.setLid(LID);
			dettLuogo.setDenomLuogo(nodoCorrente.getDescription());

			// DatiElementoType
			DatiElementoType datiElemento = utilityCastor.getDatiElemento(LID, sbnMarc);
			LuogoType luogoType = (LuogoType) datiElemento;

			dettLuogo.setForma(luogoType.getFormaNome().toString());
			dettLuogo.setLivAut(luogoType.getLivelloAut().toString());
			dettLuogo.setPaese(luogoType.getT260().getA_260());
			dettLuogo.setVersione(datiElemento.getT005());

			// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
			// nota informativa , nota catalogatore e legame a repertori
			if (luogoType.getT300() != null) {
				dettLuogo.setNotaInformativa(luogoType.getT300().getA_300());
			} else {
				dettLuogo.setNotaInformativa("");
			}
			if (luogoType.getT830() != null) {
				dettLuogo.setNotaCatalogatore(luogoType.getT830().getA_830());
			} else {
				dettLuogo.setNotaCatalogatore("");
			}


		}

		if (tipoLegame != DatiLegame.LEGAME_NULL) {
			dettLuogo.setIdPadre(LIDRoot);
			dettLuogo.setDescrizionePadre(descLidRoot);
			dettLuogo.setNotaAlLegame(datiLegame.getNotaLegame());
			dettLuogo.setTipoLegameValore(datiLegame.getTipoLegame());
			dettLuogo.setRelatorCode(datiLegame.getRelatorCode());
		}

		if (LIDRoot != null) {
			switch (tipoLegame) {
			case DatiLegame.LEGAME_DOCUMENTO_LUOGO:
				dettLuogo.setTipoLegame("TI_LU");
				break;
			case DatiLegame.LEGAME_LUOGO_LUOGO:
				dettLuogo.setTipoLegame("LU_LU");
				break;
			}
		}


		// REPERTORI
		dettLuogo.setLegamiRepertori("");
		List repertori = utilityCastor.getRepertoriElementoAuthority(LID, sbnMarc);

		String legamiPositivi = "";
		String legamiNegativi = "";

		if (repertori != null) {
			for (int i = 0; i < repertori.size(); i++) {

				Repertorio repertorio = (Repertorio) repertori.get(i);
				String tipoLegameRepertorio = repertorio.getTipoLegame().trim();
				String notaAlLegameRepertorio = repertorio.getNotaAlLegame();
				String codiceRepertorio = repertorio.getCodiceRepertorio();

				TabellaNumSTDImpronteOriginarioVO tabRepert;
				tabRepert = new TabellaNumSTDImpronteOriginarioVO(tipoLegameRepertorio,	codiceRepertorio, " ", notaAlLegameRepertorio);
				dettLuogo.addListaRepertori(tabRepert);

				if (tipoLegameRepertorio.equals("Si")) {
					if ((notaAlLegameRepertorio != null) && (!notaAlLegameRepertorio.equals("")) && (!notaAlLegameRepertorio.equals(" "))) {
						if (legamiPositivi.equals("")) {
							legamiPositivi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
						} else {
							legamiPositivi = legamiPositivi	+ ", " + (codiceRepertorio + "/" + notaAlLegameRepertorio);
						}
					} else {
						if (legamiPositivi.equals("")) {
							legamiPositivi = codiceRepertorio;
						} else {
							legamiPositivi = legamiPositivi + ", " + codiceRepertorio;
						}
					}
				} else
				// ESITO NEGATIVO
				if ((notaAlLegameRepertorio != null) && (!notaAlLegameRepertorio.equals("")) && (!notaAlLegameRepertorio.equals(" "))) {
					if (legamiNegativi.equals("")) {
						legamiNegativi = (codiceRepertorio + "/" + notaAlLegameRepertorio);
					} else {
						legamiNegativi = legamiNegativi	+ ", " + (codiceRepertorio + "/" + notaAlLegameRepertorio);
					}
				} else {
					if (legamiNegativi.equals("")) {
						legamiNegativi = codiceRepertorio;
					} else {
						legamiNegativi = legamiNegativi + ", " + codiceRepertorio;
					}
				}
			}

			if (legamiNegativi.equals("")) {
				dettLuogo.setLegamiRepertori(legamiPositivi);
			} else {
				dettLuogo.setLegamiRepertori(legamiPositivi + " // " + legamiNegativi);
			}
		}

		return dettLuogo;
	}

}
