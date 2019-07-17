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
package it.iccu.sbn.SbnMarcFactory.factory.semantica;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.gateway.intf.KeySoggetto;
import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneTitoliDao;
import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.semantica.ReticoloSoggetti;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A600;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DesType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.Posizione;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SogType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.PosizionePosType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.PosizioneDescrittore;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO.LegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.LegameTitoloAuthSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.LegameSogDesVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoParoleVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoPerDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

/**
 * @author Anna Romani
 * @version 1.0
 */
public class SbnSoggettiDAO {

	private static Logger log = Logger.getLogger(SbnSoggettiDAO.class);

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;
	private SemanticaDAO dao;

	public SbnSoggettiDAO(FactorySbn indice, FactorySbn polo, SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;

		this.dao = new SemanticaDAO();
	}

	// Default = true se tutti i campi di uno dei due panel di Ricerca
	// (Soggetto, Descrittore) sono vuoti
	public static boolean vuoti = true;

	public static boolean IS_CONFERMA_RICERCA_DIFFERITA = false;

	private static String IID_SPAZIO = " ";
	private UtilityCastor utilityCastor = new UtilityCastor();

	public SBNMarc ricercaSoggetti(RicercaComuneVO ricercaComune) {

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaDatiAutType cercaDatiAut = new CercaDatiAutType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			if (ricercaComune.getElemBlocco() != null)
				cercaType.setMaxRighe(Integer.parseInt(ricercaComune.getElemBlocco()));

			StringaCercaType strCerca = new StringaCercaType();
			StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
			CercaSoggettoDescrittoreClassiReperType cercaSogDescrReperType = null;

			if (IS_CONFERMA_RICERCA_DIFFERITA) {
				cercaType.setConfermaRicerca(SbnIndicatore.S);
				IS_CONFERMA_RICERCA_DIFFERITA = false;
			}

			Object tipoRicerca = ricercaComune.getOperazione();
			String codSoggettario = ricercaComune.getCodSoggettario();
			if ((tipoRicerca instanceof RicercaSoggettoDescrittoriVO)
					|| (tipoRicerca instanceof RicercaSoggettoParoleVO)) {

				// CERCO SOGGETTI
				TipoOrdinamento ordinamento = ricercaComune.getOrdinamentoSoggetto();
				switch (ordinamento) {
				case PER_ID:
					// ordina per cid
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
					break;
				case PER_TESTO:
					// ordina per soggetto
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
					break;
				case PER_DATA:
					// ordina per data var./ins. + Testo
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
					break;
				}

				cercaDatiAut.setTipoAuthority(SbnAuthority.SO);
				//almaviva5_20091125 #2407
				if (ricercaComune.isPolo())
					cercaDatiAut.setUtilizzato(((RicercaSoggettoVO)tipoRicerca).isUtilizzati() ? SbnIndicatore.S : SbnIndicatore.N);

				if (ValidazioneDati.isFilled(ricercaComune.getRicercaSoggetto().getCid())) {
					// cerco per CID
					canali.setT001(ricercaComune.getRicercaSoggetto().getCid());
					cercaDatiAut.setCanaliCercaDatiAut(canali);

				} else {

					cercaSogDescrReperType = new CercaSoggettoDescrittoreClassiReperType();
					cercaSogDescrReperType.setTipoAuthority(SbnAuthority.SO);
					//almaviva5_20091125 #2407
					if (ricercaComune.isPolo())
						cercaSogDescrReperType.setUtilizzato(((RicercaSoggettoVO)tipoRicerca).isUtilizzati() ? SbnIndicatore.S : SbnIndicatore.N);

					String testoSogg = ricercaComune.getRicercaSoggetto().getTestoSogg();
					if (ValidazioneDati.isFilled(testoSogg)) {
						// CERCO PER TESTO SOGGETTO
						// INIZIO CONTROLLO SU STRINGA DI SOGGETTO
						switch (ricercaComune.getRicercaTipo() ) {
						case STRINGA_INIZIALE:
							// cerca per stringa iniziale
							strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaLike(testoSogg);
							canali.setStringaCerca(strCerca);
							cercaSogDescrReperType.setCanaliCercaDatiAut(canali);
							break;

						case STRINGA_ESATTA:
							// cerca per stringa esatta
							strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaEsatta(testoSogg);
							canali.setStringaCerca(strCerca);
							cercaSogDescrReperType.setCanaliCercaDatiAut(canali);
							break;
						case PAROLE:
							// cerca per parole di soggetto
							String[] parole = OrdinamentoUnicode.getInstance().convert(testoSogg).split("\\s+"); //splitto sugli spazi
							if (parole.length > 4) { // max 4 parole
								String[] tmp = new String[4];
								System.arraycopy(parole, 0, tmp, 0, 4);
								parole = tmp;
							}
							cercaSogDescrReperType.setParoleAut(parole);
							break;
						}

					} else if (tipoRicerca instanceof RicercaSoggettoParoleVO) {
						// CERCO PER PAROLE
						// devono essere massimo 4 parole
						RicercaSoggettoParoleVO ricercaSoggParole = (RicercaSoggettoParoleVO) tipoRicerca;
						if (ValidazioneDati.isFilled(ricercaSoggParole.getParola0())
								|| ValidazioneDati.isFilled(ricercaSoggParole.getParola1())
								|| ValidazioneDati.isFilled(ricercaSoggParole.getParola2())
								|| ValidazioneDati.isFilled(ricercaSoggParole.getParola3())) {

							String[] parole = new String[4];
							parole[0] = (ricercaSoggParole).getParola0();
							parole[1] = (ricercaSoggParole).getParola1();
							parole[2] = (ricercaSoggParole).getParola2();
							parole[3] = (ricercaSoggParole).getParola3();
							cercaSogDescrReperType.setParoleAut(parole);
						}

					} else if (tipoRicerca instanceof RicercaSoggettoDescrittoriVO) {
						// CERCO PER DESCRITTORI DI SOGGETTO
						// devono essere massimo 4 descrittori o did
						int descrittori = ((RicercaSoggettoDescrittoriVO) ricercaComune.getRicercaSoggetto()).count();
						int didCount = ((RicercaSoggettoDescrittoriVO) ricercaComune.getRicercaSoggetto()).didCount();
						int idx = 0;
						int i = Math.max(descrittori, didCount);
						String[] descrittoriArray = new String[i];
						String[] didArray = new String[i];
						RicercaSoggettoDescrittoriVO ricercaSoggettoDescrittori = (RicercaSoggettoDescrittoriVO) tipoRicerca;

						if (ValidazioneDati.isFilled(ricercaSoggettoDescrittori
								.getDescrittoriSogg())
								|| ValidazioneDati.isFilled(ricercaSoggettoDescrittori.getDid1())) {
							descrittoriArray[idx] = (ricercaSoggettoDescrittori).getDescrittoriSogg();
							didArray[idx] = (ricercaSoggettoDescrittori).getDid1();
							idx++;
						}
						if (ValidazioneDati.isFilled(ricercaSoggettoDescrittori
								.getDescrittoriSogg1())
								|| ValidazioneDati.isFilled(ricercaSoggettoDescrittori.getDid2())) {
							descrittoriArray[idx] = (ricercaSoggettoDescrittori).getDescrittoriSogg1();
							didArray[idx] = (ricercaSoggettoDescrittori).getDid2();
							idx++;
						}
						if (ValidazioneDati.isFilled(ricercaSoggettoDescrittori
								.getDescrittoriSogg2())
								|| ValidazioneDati.isFilled(ricercaSoggettoDescrittori
										.getDid3())) {
							descrittoriArray[idx] = (ricercaSoggettoDescrittori)
									.getDescrittoriSogg2();
							didArray[idx] = (ricercaSoggettoDescrittori)
									.getDid3();
							idx++;
						}
						if (ValidazioneDati.isFilled(ricercaSoggettoDescrittori
								.getDescrittoriSogg3())
								|| ValidazioneDati.isFilled(ricercaSoggettoDescrittori
										.getDid4())) {
							descrittoriArray[idx] = (ricercaSoggettoDescrittori)
									.getDescrittoriSogg3();
							didArray[idx] = (ricercaSoggettoDescrittori)
									.getDid4();
							idx++;
						}

						if (idx > 0 && ValidazioneDati.isFilled(didArray[0]))
							cercaSogDescrReperType
									.setDidSoggettoPerDescrittori(didArray);
						else
							cercaSogDescrReperType
									.setParoleSoggettoPerDescrittori(descrittoriArray);
					}
				}
			} else {

				//	CERCO DESCRITTORI
				TipoOrdinamento ordinamento = ricercaComune.getOrdinamentoDescrittore();
				switch (ordinamento) {
				case PER_ID:
					// ordina per did
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
					break;
				case PER_TESTO:
					// ordina per descrittore
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
					break;
				case PER_DATA:
					// ordina per data var./ins. + Testo
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
					break;
				}

				// cerco descrittori
				cercaDatiAut.setTipoAuthority(SbnAuthority.DE);

				if (ValidazioneDati.isFilled(ricercaComune
						.getRicercaDescrittore().getDid())) {
					// cerco per DID
					canali.setT001(ricercaComune.getRicercaDescrittore()
							.getDid());
					cercaDatiAut.setCanaliCercaDatiAut(canali);
				} else {
					cercaSogDescrReperType = new CercaSoggettoDescrittoreClassiReperType();
					cercaSogDescrReperType.setTipoAuthority(SbnAuthority.DE);
					if (ValidazioneDati.isFilled(ricercaComune
							.getRicercaDescrittore().getTestoDescr())) {

						// CERCO PER TESTO Descrittore
						// INIZIO CONTROLLO SU STRINGA DI SOGGETTO
						if (ricercaComune.getRicercaTipoD().equals(TipoRicerca.STRINGA_INIZIALE)) {
							// cerca per stringa iniziale
							strCerca
									.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaLike(ricercaComune
									.getRicercaDescrittore().getTestoDescr());
							canali.setStringaCerca(strCerca);
							cercaSogDescrReperType
									.setCanaliCercaDatiAut(canali);
						} else {
							// cerca per stringa esatta
							strCerca
									.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaEsatta(ricercaComune
									.getRicercaDescrittore().getTestoDescr());
							canali.setStringaCerca(strCerca);
							cercaSogDescrReperType
									.setCanaliCercaDatiAut(canali);
						}
					}
					if (ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
							.getParole())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getDid1())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getParole1())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getDid2())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getParole2())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getDid3())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getParole3())
							|| ValidazioneDati.isFilled(ricercaComune.getRicercaDescrittore()
									.getDid4())) {
						// CERCO PER PAROLE devono essere massimo 4 parole
						// DISTINGUO SE LA RICERCA E' IN POLO O IN INDICE
						if (!ricercaComune.isPolo()) {
							int contaParole = ricercaComune
									.getRicercaDescrittore().countParole();
							int idy = 0;
							String[] paroleArray = new String[contaParole];
							if (ricercaComune.getRicercaDescrittore()
									.getParole() != null
									&& ricercaComune.getRicercaDescrittore()
											.getParole().length() > 0) {
								paroleArray[idy] = ricercaComune
										.getRicercaDescrittore().getParole();
								idy++;
							}
							if (ricercaComune.getRicercaDescrittore()
									.getParole1() != null
									&& ricercaComune.getRicercaDescrittore()
											.getParole1().length() > 0) {
								paroleArray[idy] = ricercaComune
										.getRicercaDescrittore().getParole1();
								idy++;
							}
							if (ricercaComune.getRicercaDescrittore()
									.getParole2() != null
									&& ricercaComune.getRicercaDescrittore()
											.getParole2().length() > 0) {
								paroleArray[idy] = ricercaComune
										.getRicercaDescrittore().getParole2();
								idy++;
							}
							if (ricercaComune.getRicercaDescrittore()
									.getParole3() != null
									&& ricercaComune.getRicercaDescrittore()
											.getParole3().length() > 0) {
								paroleArray[idy] = ricercaComune
										.getRicercaDescrittore().getParole3();
								idy++;
							}
							cercaSogDescrReperType.setParoleAut(paroleArray);
						} else {
							int parolePolo = ricercaComune
									.getRicercaDescrittore().countParole();
							int didCount = ricercaComune
									.getRicercaDescrittore().didCount();

							int idx = 0;
							int i = Math.max(parolePolo, didCount);
							String[] paroleArray = new String[i];
							String[] didArray = new String[i];
							if (ValidazioneDati.isFilled(ricercaComune
									.getRicercaDescrittore().getParole())
									|| ValidazioneDati.isFilled(ricercaComune
											.getRicercaDescrittore().getDid1())) {
								paroleArray[idx] = ricercaComune
										.getRicercaDescrittore().getParole();
								didArray[idx] = ricercaComune
										.getRicercaDescrittore().getDid1();
								idx++;

							}

							if (ValidazioneDati.isFilled(ricercaComune
									.getRicercaDescrittore().getParole1())
									|| ValidazioneDati.isFilled(ricercaComune
											.getRicercaDescrittore().getDid2())) {
								paroleArray[idx] = ricercaComune
										.getRicercaDescrittore().getParole1();
								didArray[idx] = ricercaComune
										.getRicercaDescrittore().getDid2();
								idx++;

							}

							if (ValidazioneDati.isFilled(ricercaComune
									.getRicercaDescrittore().getParole2())
									|| ValidazioneDati.isFilled(ricercaComune
											.getRicercaDescrittore().getDid3())) {
								paroleArray[idx] = ricercaComune
										.getRicercaDescrittore().getParole2();
								didArray[idx] = ricercaComune
										.getRicercaDescrittore().getDid3();
								idx++;

							}

							if (ValidazioneDati.isFilled(ricercaComune
									.getRicercaDescrittore().getParole3())
									|| ValidazioneDati.isFilled(ricercaComune
											.getRicercaDescrittore().getDid4())) {
								paroleArray[idx] = ricercaComune
										.getRicercaDescrittore().getParole3();
								didArray[idx] = ricercaComune
										.getRicercaDescrittore().getDid4();
								idx++;

							}

							if (idx == 1) {
								cercaSogDescrReperType.setParoleAut(paroleArray);
							} else {
								// CODICE SOGGETTARIO
								if (ValidazioneDati.isFilled(codSoggettario))
									cercaSogDescrReperType.setC2_250(codSoggettario);
								if (ValidazioneDati.isFilled(ricercaComune.getEdizioneSoggettario()))
									cercaSogDescrReperType.setEdizione(SbnEdizioneSoggettario.valueOf(ricercaComune.getEdizioneSoggettario()));
								if (idx > 0 && ValidazioneDati.isFilled(didArray[0]))
									cercaSogDescrReperType.setParoleAut(didArray);
								else
									cercaSogDescrReperType.setParoleDescrittoriPerDescrittori(paroleArray);
							}

						}

					}
				}

			}

			if (cercaSogDescrReperType == null)
				cercaElemento.setCercaDatiAut(cercaDatiAut);
			else {
				if (!ValidazioneDati.strIsNull(codSoggettario))
					cercaSogDescrReperType.setC2_250(codSoggettario);
				if (ricercaComune.isPolo() && ValidazioneDati.isFilled(ricercaComune.getEdizioneSoggettario()))
					cercaSogDescrReperType.setEdizione(SbnEdizioneSoggettario.valueOf(ricercaComune.getEdizioneSoggettario()));

				cercaElemento.setCercaDatiAut(cercaSogDescrReperType);
			}

//			CercaDatiAutType filtroLiv = cercaElemento.getCercaDatiAut();
//			// FILTRO LIVELLO AUTORITA
//			if (ValidazioneDati.isFilled(ricercaComune.getLivelloAutoritaDa())) {
//				filtroLiv.setLivelloAut_Da(SbnLivello.valueOf(ricercaComune
//						.getLivelloAutoritaDa()));
//			}
//
//			if (ValidazioneDati.isFilled(ricercaComune.getLivelloAutoritaA())) {
//				filtroLiv.setLivelloAut_A(SbnLivello.valueOf(ricercaComune
//						.getLivelloAutoritaA()));
//
//			}

			//almaviva5_20091125 #2407 filtri gestione solo su polo
			if (ricercaComune.isPolo())
				cercaElemento.getCercaDatiAut().setGestito(SbnIndicatore.S);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (ricercaComune.isPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (IllegalArgumentException ie) {
			// Errore nella composizione della data.
			log.error("", ie);
		}
		return sbnRisposta;
	}

	public SBNMarc ricercaSoggettiPerDescrittore(RicercaComuneVO ricercaComune) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaDatiAutType cercaDatiAut = new CercaDatiAutType();

			// TIPO OUTPUT (000)
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(Integer.parseInt(ricercaComune
					.getElemBlocco()));

			StringaCercaType strCerca = new StringaCercaType();
			StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
			CercaSoggettoDescrittoreClassiReperType cercaSogDescrReperType = null;

			if (IS_CONFERMA_RICERCA_DIFFERITA == true) {
				cercaType.setConfermaRicerca(SbnIndicatore.S);
				IS_CONFERMA_RICERCA_DIFFERITA = false;
			}

			Object tipoRicerca = ricercaComune.getOperazione();
			if ((tipoRicerca instanceof RicercaSoggettoDescrittoriVO)) {

				// //////////////////////////////////////CERCO DESCRITTORI
				// ///////////////////////////////////////

				TipoOrdinamento ordinamento = ricercaComune
						.getOrdinamentoDescrittore();
				switch (ordinamento) {
				case PER_ID:
					// ordina per did
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
					break;
				case PER_TESTO:
					// ordina per descrittore
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
					break;
				case PER_DATA:
					// ordina per data var./ins. + Testo
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
					break;
				}

				// cerco descrittori
				cercaDatiAut.setTipoAuthority(SbnAuthority.DE);

				cercaSogDescrReperType = new CercaSoggettoDescrittoreClassiReperType();
				cercaSogDescrReperType.setTipoAuthority(SbnAuthority.DE);
				// // CODICE SOGGETTARIO
				// if (!Soggetti.ValidazioneDati.strIsNull(ricercaComune.getCodSoggettario())) {
				//
				// cercaSogDescrReperType.setC2_250(ricercaComune
				// .getCodSoggettario());
				// }
				// CERCO PER TESTO Descrittore
				RicercaSoggettoDescrittoriVO ricSogDesc = (RicercaSoggettoDescrittoriVO) ricercaComune
						.getRicercaSoggetto();

				if (ValidazioneDati.isFilled(ricSogDesc.getUnDescrittore())) {

					// INIZIO CONTROLLO SU STRINGA DI SOGGETTO, IN QUESTO
					// CASO LA RICERCA E' PER STRINGA ESATTA
					if (ricercaComune.getRicercaTipoD().equals(TipoRicerca.STRINGA_INIZIALE)) {
						// cerca per stringa iniziale
						strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
						stringaTypeChoice.setStringaLike(ricercaComune
								.getRicercaDescrittore().getTestoDescr());
						canali.setStringaCerca(strCerca);
						cercaSogDescrReperType.setCanaliCercaDatiAut(canali);
					} else {
						// cerca per stringa esatta
						strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
						stringaTypeChoice.setStringaEsatta(ricSogDesc
								.getUnDescrittore());
						canali.setStringaCerca(strCerca);
						cercaSogDescrReperType.setCanaliCercaDatiAut(canali);
					}
				}

			}

			if (cercaSogDescrReperType == null) {
				cercaElemento.setCercaDatiAut(cercaDatiAut);
			} else {
				if (!ValidazioneDati.strIsNull(ricercaComune.getCodSoggettario())) {
					cercaSogDescrReperType.setC2_250(ricercaComune.getCodSoggettario());
					if (ValidazioneDati.isFilled(ricercaComune.getEdizioneSoggettario()))
						cercaSogDescrReperType.setEdizione(SbnEdizioneSoggettario.valueOf(ricercaComune.getEdizioneSoggettario()));
				}
				cercaElemento.setCercaDatiAut(cercaSogDescrReperType);
			}

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);
			//this.indice.setMessage(sbnmessage, this.user);
			this.polo.setMessage(sbnmessage, this.user);

			// CHIAMATA AL PROTOCOLLO
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (IllegalArgumentException ie) {
			// Errore nella composizione della data.
			log.error("", ie);
		}
		return sbnRisposta;
	}

	public SBNMarc ricercaSoggettiPerDidCollegato(boolean livelloPolo,
			String did, int elementiBlocco, PosizioneDescrittore posizioneDescrittore,
			boolean utilizzati,	TipoOrdinamento ordinamento, SbnEdizioneSoggettario edizione) {

		SBNMarc sbnRisposta = null;
		Posizione pos = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(elementiBlocco);
			// cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);

			// CERCO SOGGETTI
			switch (ordinamento) {
			case PER_ID:
				// ordina per cid
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				break;
			case PER_TESTO:
				// ordina per soggetto
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
				break;
			case PER_DATA:
				// ordina per data var./ins. + Testo
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
				break;
			}

			CercaSoggettoDescrittoreClassiReperType cercaDatiAutType = new CercaSoggettoDescrittoreClassiReperType();

			cercaDatiAutType.setTipoAuthority(SbnAuthority.SO);

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legameElemento = new LegameElementoAutType();

			legameElemento.setTipoAuthority(SbnAuthority.DE);

			// legame Soggetto-Descrittore: 931
			legameElemento.setTipoLegame(SbnLegameAut.valueOf("931"));

			// posizione (solo Polo)
			if (livelloPolo && posizioneDescrittore != null) {
				pos = new Posizione();
				pos.setLegatoTitolo(utilizzati);
				pos.setPos(PosizionePosType.valueOf(posizioneDescrittore.getSBNMarcValue()));
				legameElemento.setPosizione(pos);
			}

			legameElemento.setIdArrivo(did);
			arrivoLegame.setLegameElementoAut(legameElemento);

			if (edizione != null)
				cercaDatiAutType.setEdizione(edizione);

			cercaElemento.setCercaDatiAut(cercaDatiAutType);
			cercaElemento.setArrivoLegame(arrivoLegame);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			// CHIAMATA AL PROTOCOLLO
			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;
	} // end ricercaSoggettiPerDidCollegato public SBNMarc

	public SBNMarc ricercaDescrittoriPerDidCollegato(boolean livelloPolo,
			String did, int elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
			did = ValidazioneDati.trimOrEmpty(did).toUpperCase();

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(elementiBlocco);
			// cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaDatiAutType cercaDatiAutType = new CercaDatiAutType();
			cercaDatiAutType.setTipoAuthority(SbnAuthority.DE);

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legameElemento = new LegameElementoAutType();

			SbnAuthority sbnAuthority = SbnAuthority.DE;
			legameElemento.setTipoAuthority(sbnAuthority);

			legameElemento.setTipoLegame(SbnLegameAut.valueOf("tutti"));

			legameElemento.setIdArrivo(did);
			arrivoLegame.setLegameElementoAut(legameElemento);

			cercaElemento.setCercaDatiAut(cercaDatiAutType);
			cercaElemento.setArrivoLegame(arrivoLegame);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;
	} // end ricercaDescrittoriPerDidCollegato

	public SBNMarc ricercaSoggettiPerBidCollegato(boolean livelloPolo,
			String bid, String codSoggettario, boolean soloGestiti, int elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
			bid = ValidazioneDati.trimOrEmpty(bid).toUpperCase();

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(elementiBlocco);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaSoggettoDescrittoreClassiReperType cercaDatiAutType = new CercaSoggettoDescrittoreClassiReperType();

			//almaviva5_20130403 #5294
			if (livelloPolo)
				cercaDatiAutType.setGestito(soloGestiti ? SbnIndicatore.S : SbnIndicatore.N);

			cercaDatiAutType.setTipoAuthority(SbnAuthority.SO);
			if (ValidazioneDati.isFilled(codSoggettario))
				cercaDatiAutType.setC2_250(codSoggettario);

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameDocType legameDoc = new LegameDocType();
			legameDoc.setTipoLegame(SbnLegameDoc.VALUE_0); // tutti
			legameDoc.setIdArrivo(bid);
			arrivoLegame.setLegameDoc(legameDoc);

			cercaElemento.setCercaDatiAut(cercaDatiAutType);
			cercaElemento.setArrivoLegame(arrivoLegame);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;
	} // end ricercaSoggettiPerBidCollegato

	// /////////////////////////////////////////////////
	// //// GESTIONE DEI BLOCCHI ///////////////////////
	// /////////////////////////////////////////////////
	/**
	 * Ottiene un'altro blocco della lista sintetica dei soggetti.
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc getNextBloccoSoggetti(SBNMarcCommonVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		int numPrimo = areaDatiPass.getNumPrimo();
		int maxRighe = areaDatiPass.getMaxRighe();
		String idLista = areaDatiPass.getIdLista();

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaSoggettoDescrittoreClassiReperType cercaSogDes = new CercaSoggettoDescrittoreClassiReperType();

			cercaSogDes.setTipoAuthority(SbnAuthority.SO);
			cercaElemento.setCercaDatiAut(cercaSogDes);

			// tipoOutput sintetica
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(numPrimo);
			cercaType.setMaxRighe(maxRighe);
			cercaType.setIdLista(idLista);
			cercaType.setCercaElementoAut(cercaElemento);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (areaDatiPass.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}

		return sbnRisposta;

	}// end getNextBloccoSoggetti

	/**
	 * Ottiene un'altro blocco della lista sintetica dei descrittori.
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc getNextBloccoDescrittori(SBNMarcCommonVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		int numPrimo = areaDatiPass.getNumPrimo();
		int maxRighe = areaDatiPass.getMaxRighe();
		String idLista = areaDatiPass.getIdLista();

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaSoggettoDescrittoreClassiReperType cercaSogDes = new CercaSoggettoDescrittoreClassiReperType();

			cercaSogDes.setTipoAuthority(SbnAuthority.DE);
			cercaElemento.setCercaDatiAut(cercaSogDes);

			// tipoOutput sintetica
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(numPrimo);
			cercaType.setMaxRighe(maxRighe);
			cercaType.setIdLista(idLista);
			cercaType.setCercaElementoAut(cercaElemento);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (areaDatiPass.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}

		return sbnRisposta;

	}// end getNextBloccoDescrittori

	public SBNMarc creaSoggetto(String soggettario, String testoSoggetto,
			String livello, String tipo, String note, boolean livelloPolo,
			String cidInPolo, boolean forzaCreazione, SbnEdizioneSoggettario edizione) {

		CreaVariaSoggettoVO cvs = new CreaVariaSoggettoVO();
		cvs.setCodiceSoggettario(soggettario);
		//almaviva5_20190619 fix edizione soggetto
		cvs.setEdizioneSoggettario(edizione != null ? edizione.toString() : SemanticaUtil.getEdizioneSoggettarioIndice(soggettario));
		cvs.setTesto(testoSoggetto);
		cvs.setLivello(livello);
		cvs.setTipoSoggetto(tipo);
		cvs.setNote(note);
		cvs.setLivelloPolo(livelloPolo);
		cvs.setForzaCreazione(forzaCreazione);

		return creaSoggetto(cvs);
	}

	public SBNMarc creaSoggetto(CreaVariaSoggettoVO soggetto) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			SoggettoType soggettoType = new SoggettoType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile)
			crea.setTipoControllo(soggetto.isForzaCreazione() ? SbnSimile.CONFERMA : SbnSimile.SIMILE);

			// TIPO AUTHORITY
			soggettoType.setTipoAuthority(SbnAuthority.SO);

			// Livello di Autorità
			soggettoType.setLivelloAut(SbnLivello.valueOf(soggetto.getLivello()));

			KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggetto.getCodiceSoggettario(), null, soggetto.getTesto());
			A250 a250 = key.getA250();//SoggettiUtil.costruisciSbnMarcStringaSoggetto(soggetto.getTesto());

			if (soggetto.isCattura())
				crea.setCattura(true);

			// Codice Soggettario
			a250.setC2_250(soggetto.getCodiceSoggettario());
			soggettoType.setT250(a250);

			String cid = soggetto.getCid();
			soggettoType.setT001(ValidazioneDati.isFilled(cid) ? cid : SBNMarcUtil.SBNMARC_DEFAULT_ID);

			if (soggetto.isLivelloPolo()) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(soggetto.getTipoSoggetto());
				t600.setA_602(soggetto.getNote());
				soggettoType.setT600(t600);
				//almaviva5_20111127 evolutive CFI
				String edizione = soggetto.getEdizioneSoggettario();
				if (ValidazioneDati.isFilled(edizione))
					a250.setEdizione(SbnEdizioneSoggettario.valueOf(edizione));
			}

			elementAutType.setDatiElementoAut(soggettoType);

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(crea);

			sbnmessage.setSbnRequest(sbnrequest);

			if (soggetto.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcDiagnosticoException e) {
			sbnRisposta = SBNMarcUtil.buildMessaggioErrore(e, this.user);

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea soggetto: " + ve.getMessage()	+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea soggetto: " + e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaSoggetto

	public SBNMarc importaSoggettoConLegami(String soggettario, String edizione, String testoSoggetto,
			String livello, String tipo, String note, boolean livelloPolo,
			String cidInPolo, DatiLegameSoggettoDescrittoreVO datiLegami) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			SoggettoType soggettoType = new SoggettoType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.SIMILE);
			if (livelloPolo) {
				crea.setCattura(false);
				soggettoType.setCondiviso(DatiElementoTypeCondivisoType.S);
			}

			// TIPO AUTHORITY
			soggettoType.setTipoAuthority(SbnAuthority.SO);

			// Default CID
			if (ValidazioneDati.strIsNull(cidInPolo))
				// il polo crea il cid
				soggettoType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
			else
				soggettoType.setT001(cidInPolo);

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			soggettoType.setLivelloAut(SbnLivello.valueOf(livello));

			// Spezzo il testo del soggetto
			KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggettario, null, testoSoggetto);
			A250 a250 = key.getA250();//SoggettiUtil.costruisciSbnMarcStringaSoggetto(testoSoggetto);

			// Codice Soggettario
			a250.setC2_250(soggettario);
			soggettoType.setT250(a250);

			if (livelloPolo) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(tipo);
				t600.setA_602(note);
				soggettoType.setT600(t600);
				//almaviva5_20120419 evolutive CFI
				if (ValidazioneDati.isFilled(edizione) )
					a250.setEdizione(SbnEdizioneSoggettario.valueOf(edizione));
			}

			elementAutType.setDatiElementoAut(soggettoType);

			//imposto i legami 931
			LegamiType legami = new LegamiType();
			legami.setTipoOperazione(SbnTipoOperazione.CREA);

			if (datiLegami != null) {
				legami.setIdPartenza(ValidazioneDati.trimOrEmpty(datiLegami.getCid()).toUpperCase());
				for (LegameSogDesVO legame931 : datiLegami.getLegami()) {
					ArrivoLegame arrivoLegame = new ArrivoLegame();
					LegameElementoAutType legame = new LegameElementoAutType();
					legame.setTipoAuthority(SbnAuthority.DE);
					legame.setTipoLegame(SbnLegameAut.valueOf("931"));
					legame.setIdArrivo(legame931.getDidArrivo());
					legame.setNoteLegame(legame931.getNotaLegame());

					arrivoLegame.setLegameElementoAut(legame);
					legami.addArrivoLegame(arrivoLegame);
				}
				if (legami.getArrivoLegameCount() > 0)
					elementAutType.addLegamiElementoAut(legami);
			}

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcDiagnosticoException e) {
			sbnRisposta = SBNMarcUtil.buildMessaggioErrore(e, this.user);

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea soggetto: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea soggetto: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end creaSoggetto


	public SBNMarc importaSoggetto(CreaVariaSoggettoVO soggetto) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			SoggettoType soggettoType = new SoggettoType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);
			crea.setCattura(true);

			// TIPO AUTHORITY
			soggettoType.setTipoAuthority(SbnAuthority.SO);

			// CONDIVISO CON INDICE
			soggettoType.setCondiviso(DatiElementoTypeCondivisoType.S);

			// Spezzo il testo del soggetto
			KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggetto.getCodiceSoggettario(), null, soggetto.getTesto());
			A250 a250 = key.getA250();//SoggettiUtil.costruisciSbnMarcStringaSoggetto(soggetto.getTesto());

			// Codice Soggettario
			a250.setC2_250(soggetto.getCodiceSoggettario());

			if (soggetto.isLivelloPolo()) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(soggetto.getTipoSoggetto());
				t600.setA_602(soggetto.getNote());
				soggettoType.setT600(t600);
				//almaviva5_20111127 evolutive CFI
				String edizione = soggetto.getEdizioneSoggettario();
				if (ValidazioneDati.isFilled(edizione))
					a250.setEdizione(SbnEdizioneSoggettario.valueOf(edizione));
			}

			// CID presente in Indice
			soggettoType.setT001(soggetto.getCid());

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			soggettoType.setLivelloAut(SbnLivello.valueOf(soggetto.getLivello()));

			// //////////////////////

			soggettoType.setT250(a250);
			elementAutType.setDatiElementoAut(soggettoType);

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (soggetto.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcDiagnosticoException e) {
			sbnRisposta = SBNMarcUtil.buildMessaggioErrore(e, this.user);

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea soggetto: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea soggetto: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end importaSoggetto


	public SBNMarc catturaSoggetto(String soggettario, String testoSoggetto,
			String cid, String livello, String tipo, String note,
			boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			SoggettoType soggettoType = new SoggettoType();

			ElementAutType elementAutType = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();

			crea.setCattura(true);
			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			datiElemento.setTipoAuthority(SbnAuthority.SO);
			soggettoType.setTipoAuthority(SbnAuthority.SO);

			// CONDIVISO CON INDICE
			datiElemento.setCondiviso(DatiElementoTypeCondivisoType.S);
			soggettoType.setCondiviso(DatiElementoTypeCondivisoType.S);

			// Note
			// datiElemento.setNote(note);

			// LINK A CID DI INDICE USUALMENTE IN POLO IL CID COINCIDERA' CON
			// QUELLO DI INDICE, SOLO SE IN POLO ESISTEVA GIA' LA CHIAVE(LEGGI
			// TESTO)
			// I DUE CID SARANNO DIVERSI
			// soggettoType.setCidIndice(cidIndice);

			// CID presente in Indice
			soggettoType.setT001(cid);

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			soggettoType.setLivelloAut(SbnLivello.valueOf(livello));

			// Spezzo il testo del soggetto
			KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggettario, null, testoSoggetto);
			A250 a250 = key.getA250();//SoggettiUtil.costruisciSbnMarcStringaSoggetto(testoSoggetto);

			// Codice Soggettario
			a250.setC2_250(soggettario);
			// //////////////////////

			soggettoType.setT250(a250);
			datiElemento = soggettoType;
			elementAutType.setDatiElementoAut(datiElemento);

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcDiagnosticoException e) {
			sbnRisposta = SBNMarcUtil.buildMessaggioErrore(e, this.user);

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea soggetto: " + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea soggetto: " + e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end importaSoggetto


	public SBNMarc catturaSoggettoConDescrittori(SBNMarc analiticaIndice) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			SoggettoType soggettoType = new SoggettoType();

			crea.setCattura(true);
			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);

			SbnResponseTypeChoice sbnResponseChoice = analiticaIndice.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			ElementAutType datiElementoDaCatturare = sbnOutPut.getElementoAut(0);
			soggettoType = (SoggettoType) datiElementoDaCatturare.getDatiElementoAut();
			// CONDIVISO CON INDICE
			soggettoType.setCondiviso(DatiElementoTypeCondivisoType.S);

			creaTypeChoice.setElementoAut(datiElementoDaCatturare);
			crea.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			//this.indice.setMessage(sbnmessage, this.user);
			this.polo.setMessage(sbnmessage, this.user);

			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea soggetto: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea soggetto: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end importaSoggetto



	public SBNMarc richiestaAccorpamentoSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area) {
		SBNMarc sbnRisposta = null;
		// Non ci sono ID di titoli collegati, per cui si tratta di una
		// richiesta di accorpamento
		// e non di spostamento legami
		area.setIdTitoliLegati(null);

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.SO);
			fonde.setTipoOggetto(oggetto);
			fonde.setIdPartenza(area.getIdElementoEliminato());
			fonde.setIdArrivo(area.getIdElementoAccorpante());
			fonde.setSpostaID(area.getIdTitoliLegati());

			sbnrequest.setFonde(fonde);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Accorpa soggetti: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Accorpa soggetti: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end richiestaAccorpamentoSoggetti

	public SBNMarc trascinaTitoliTraSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.SO);
			fonde.setTipoOggetto(oggetto);
			fonde.setIdPartenza(area.getIdElementoEliminato());
			fonde.setIdArrivo(area.getIdElementoAccorpante());
			fonde.setSpostaID(area.getIdTitoliLegati());

			sbnrequest.setFonde(fonde);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Trascina Titoli tra soggetti: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Trascina Titoli tra soggetti: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end trascinaTitoliTraSoggetti

	public SBNMarc fondiSoggetti(boolean livelloPolo,
			AreaDatiAccorpamentoVO area) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.SO);
			fonde.setTipoOggetto(oggetto);
			fonde.setIdPartenza(area.getIdElementoEliminato());
			fonde.setIdArrivo(area.getIdElementoAccorpante());

			sbnrequest.setFonde(fonde);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Fondi soggetti: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Fondi soggetti: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end fondiSoggetti

	public SBNMarc creaLegameSoggettoDescrittore(String cidPadre, String categoriaSoggetto,
			String T005, String livello, String did, String note, boolean condiviso,
			boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			SoggettoType soggetto = new SoggettoType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.SIMILE);

			// TIPO AUTHORITY
			soggetto.setTipoAuthority(SbnAuthority.SO);

			// Default CID
			soggetto.setT001(ValidazioneDati.trimOrEmpty(cidPadre).toUpperCase());
			soggetto.setT005(T005);

			if (livelloPolo) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(categoriaSoggetto);
				soggetto.setT600(t600);
			}

			if (livelloPolo) {
				if (condiviso)
					soggetto.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					soggetto.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			soggetto.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(soggetto);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf("931")); // 931 Soggetto -
			// Descrittore
			legame.setIdArrivo(ValidazioneDati.trimOrEmpty(did).toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(ValidazioneDati.trimOrEmpty(cidPadre).toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.CREA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea legame soggetto descrittore: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame soggetto descrittore: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaLegameSoggettoDescrittore

	public SBNMarc gestioneLegameTitoloSoggetto(DatiLegameTitoloSoggettoVO input) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();
			LegamiType legami = new LegamiType();

			modifica.setTipoControllo(SbnSimile.SIMILE);

			DatiDocType datiDocType = new DatiDocType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(input.getBidNatura()));
			//almaviva5_20130520 #5316
			String tipoMateriale = input.getBidTipoMateriale();
			datiDocType.setTipoMateriale(ValidazioneDati.isFilled(tipoMateriale) ? SbnMateriale.valueOf(tipoMateriale) : null);
			datiDocType.setT001(ValidazioneDati.trimOrEmpty(input.getBid()).toUpperCase());
			// DEVO VERIFICARE LA CONGRUENZA DEL T005
			String rightT005 = input.getT005();

			if (ValidazioneDati.strIsNull(rightT005)) {

				SbnGestioneTitoliDao gestioneTitoliDao = new SbnGestioneTitoliDao(this.indice, this.polo, this.user);

				// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
				// operante che nel caso di centro Sistema non coincidono
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass =
					new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
//				AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
				// Fine Modifica almaviva2 16.07.2010

				areaDatiPass.setBidRicerca(input.getBid());
				areaDatiPass.setRicercaPolo(input.isLivelloPolo());
				areaDatiPass.setRicercaIndice(!input.isLivelloPolo());
				areaDatiPass.setInviaSoloTimeStampRadice(true);
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn =
					gestioneTitoliDao.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass);

				if (!areaDatiPassReturn.getCodErr().equals("")
						&& !areaDatiPassReturn.getCodErr().equals("0000"))
					throw new Exception(areaDatiPassReturn.getTestoProtocollo());

				rightT005 = areaDatiPassReturn.getTimeStampRadice();//.getTreeElementViewTitoli().getT005();

			}

			datiDocType.setT005(rightT005);
			datiDocType.setLivelloAutDoc(SbnLivello.valueOf(input.getBidLivelloAut()));
			documentoTypeChoice.setDatiDocumento(datiDocType);
			DocumentoType documentoType = new DocumentoType();
			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			switch (input.getOperazione()) {
			case CREA:
				legami.setTipoOperazione(SbnTipoOperazione.CREA);
				break;
			case MODIFICA:
				legami.setTipoOperazione(SbnTipoOperazione.MODIFICA);
				break;
			case CANCELLA:
				legami.setTipoOperazione(SbnTipoOperazione.CANCELLA);
				break;
			default:
				return SBNMarcUtil.buildMessaggioErrore(new SbnMarcDiagnosticoException(3102, "non gestito"), user);
			}

			legami.setIdPartenza(input.getBid());

			for (LegameTitoloAuthSemanticaVO lts : input.getLegami() )  {

				LegameTitoloSoggettoVO datiLegame = (LegameTitoloSoggettoVO) lts;
				ArrivoLegame arrivoLegame = new ArrivoLegame();
				LegameElementoAutType legame = new LegameElementoAutType();
				legame.setTipoAuthority(SbnAuthority.SO);
				legame.setTipoLegame(SbnLegameAut.valueOf("606"));

				legame.setIdArrivo(datiLegame.getCid() );
				legame.setNoteLegame(datiLegame.getNotaLegame());

				//almaviva5_20120507 evolutive CFI
				if (input.isLivelloPolo())
					legame.setRank(datiLegame.getRank() );

				arrivoLegame.setLegameElementoAut(legame);
				legami.addArrivoLegame(arrivoLegame);
			}

			documentoType.addLegamiDocumento(legami);
			modifica.setDocumento(documentoType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (input.isLivelloPolo()) {
				modifica.setCattura(true); // test per inibire controllo livAut
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea legame titolo soggetto: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame titolo soggetto: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaLegameTitoloSoggetto




	public SBNMarc cancellaLegameSoggettoDescrittore(String cidPadre,
			String categoriaSoggetto, String T005, String livello, String did,
			String note, boolean condiviso, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			SoggettoType soggetto = new SoggettoType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.SIMILE);

			// TIPO AUTHORITY
			soggetto.setTipoAuthority(SbnAuthority.SO);

			// Default CID
			soggetto.setT001(ValidazioneDati.trimOrEmpty(cidPadre).toUpperCase());
			soggetto.setT005(T005);

			if (livelloPolo) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(categoriaSoggetto);
				soggetto.setT600(t600);
			}

			if (livelloPolo) {
				if (condiviso)
					soggetto.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					soggetto.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			soggetto.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(soggetto);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf("931")); // 931 Soggetto -
			// Descrittore
			legame.setIdArrivo(ValidazioneDati.trimOrEmpty(did).toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(ValidazioneDati.trimOrEmpty(cidPadre).toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.CANCELLA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea legame soggetto descrittore: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame soggetto descrittore: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end cancellaLegameSoggettoDescrittore

	public SBNMarc creaLegameDescrittori(String didPadre, String formaNome,
			String T005, String livello, String did, String tipoLegame,
			String note, boolean condiviso, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			DescrittoreType descrittore = new DescrittoreType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			descrittore.setTipoAuthority(SbnAuthority.DE);

			// Default DID
			descrittore.setT001(ValidazioneDati.trimOrEmpty(didPadre).toUpperCase());
			descrittore.setFormaNome(SbnFormaNome.valueOf(formaNome));
			descrittore.setT005(T005);

			if (livelloPolo) {
				if (condiviso)
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			descrittore.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(descrittore);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf(tipoLegame)); // 931
			// Soggetto
			// -
			// Descrittore
			legame.setIdArrivo(ValidazioneDati.trimOrEmpty(did).toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(ValidazioneDati.trimOrEmpty(didPadre).toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.CREA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea legame descrittori: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame descrittori: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaLegameDescrittori

	public SBNMarc scambioFormaDescrittori(String didPadre, String formaNome,
			String T005, String livello, String did, String tipoLegame,
			String note, boolean condiviso, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			DescrittoreType descrittore = new DescrittoreType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			descrittore.setTipoAuthority(SbnAuthority.DE);

			// Default DID
			descrittore.setT001(didPadre.toUpperCase());
			descrittore.setFormaNome(SbnFormaNome.A);
			descrittore.setT005(T005);
			if (livelloPolo) {
				if (condiviso)
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			descrittore.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(descrittore);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf(tipoLegame));
			legame.setIdArrivo(did.toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(didPadre.toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.SCAMBIOFORMA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Scambio Forma descrittori: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Scambio Forma descrittori: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end scambioFormaDescrittori

	public SBNMarc modificaLegameDescrittori(String didPadre, String formaNome,
			String T005, String livello, String did, String tipoLegame,
			String note, boolean condiviso, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			DescrittoreType descrittore = new DescrittoreType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			descrittore.setTipoAuthority(SbnAuthority.DE);

			// Default DID
			descrittore.setT001(didPadre.toUpperCase());
			descrittore.setFormaNome(SbnFormaNome.valueOf(formaNome));
			descrittore.setT005(T005);

			if (livelloPolo) {
				if (condiviso)
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			descrittore.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(descrittore);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf(tipoLegame)); // 931
			// Soggetto
			// -
			// Descrittore
			legame.setIdArrivo(did.toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(didPadre.toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.MODIFICA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Modifica legame descrittori: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Modifica legame descrittori: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end modificaLegameDescrittori

	public SBNMarc cancellaLegameDescrittori(String didPadre, String formaNome,
			String T005, String livello, String did, String tipoLegame,
			String note, boolean condiviso, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			DescrittoreType descrittore = new DescrittoreType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			descrittore.setTipoAuthority(SbnAuthority.DE);

			// Default DID
			descrittore.setT001(didPadre.toUpperCase());
			descrittore.setFormaNome(SbnFormaNome.valueOf(formaNome));
			descrittore.setT005(T005);

			if (livelloPolo) {
				if (condiviso)
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					descrittore.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			descrittore.setLivelloAut(SbnLivello.valueOf(livello));

			elementAutType.setDatiElementoAut(descrittore);

			legame.setTipoAuthority(SbnAuthority.DE);
			legame.setTipoLegame(SbnLegameAut.valueOf(tipoLegame));

			// Descrittore
			legame.setIdArrivo(did.toUpperCase());
			legame.setNoteLegame(note);

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(didPadre.toUpperCase());
			legami.addArrivoLegame(arrivoLegame);
			legami.setTipoOperazione(SbnTipoOperazione.CANCELLA);
			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Cancella legame descrittori: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Cancella legame descrittori: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end cancellaLegameDescrittori

	public SBNMarc creaDescrittoreManuale(CreaVariaDescrittoreVO descrittore) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			DescrittoreType descrittoreType = new DescrittoreType();
			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			descrittoreType.setTipoAuthority(SbnAuthority.DE);

			// Default DID
			if (ValidazioneDati.strIsNull(descrittore.getDid()))
				descrittoreType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
			else
				descrittoreType.setT001(descrittore.getDid());

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			descrittoreType.setLivelloAut(SbnLivello.valueOf(descrittore.getLivelloAutorita()));
			descrittoreType.setFormaNome(SbnFormaNome.valueOf(descrittore.getFormaNome()));

			A931 a931 = new A931();
			a931.setA_931(descrittore.getTesto());
			a931.setB_931(descrittore.getNote());
			a931.setC2_931(descrittore.getCodiceSoggettario());

			descrittoreType.setT931(a931);
			elementAutType.setDatiElementoAut(descrittoreType);

			if (descrittore.isLivelloPolo()) {
				descrittoreType.setCondiviso(descrittore.isCondiviso() ?
						DatiElementoTypeCondivisoType.S :
						DatiElementoTypeCondivisoType.N);
				crea.setCattura(descrittore.isCattura());

				//almaviva5_20120326 evolutive CFI
				String edizione = descrittore.getEdizioneSoggettario();
				if (ValidazioneDati.isFilled(edizione))
					a931.setEdizione(SbnEdizioneSoggettario.valueOf(edizione));
				a931.setCat_termine(ValidazioneDati.trimOrNull(descrittore.getCategoriaTermine()));
			}

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (descrittore.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea descrittore manuale: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea descrittore manuale: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaDescrittoreManuale

	public SBNMarc creaAnaliticaSoggettoPerCid(boolean livelloPolo, String Cid) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput analitica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(1);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
			cercaType.setCercaElementoAut(cercaElemento);
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

			// Cid
			canali.setT001(Cid);

			CercaSoggettoDescrittoreClassiReperType cercaSogDesc = new CercaSoggettoDescrittoreClassiReperType();
			cercaSogDesc.setCanaliCercaDatiAut(canali);
			cercaSogDesc.setTipoAuthority(SbnAuthority.SO);
			cercaElemento.setCercaDatiAut(cercaSogDesc);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Analitica soggetto: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Analitica soggetto: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end creaRichiestaAnaliticoSoggettoPerCid

	/**
	 * Questo metodo crea un oggetto Castor pronto per fare una richiesta per
	 * Did
	 *
	 * @param Did:
	 *            Did del Soggetto
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc creaAnaliticaSoggettoPerDid(boolean livelloPolo, String Did) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput analitica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(1);
			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
			cercaType.setCercaElementoAut(cercaElemento);
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

			// Did
			canali.setT001(Did);

			CercaSoggettoDescrittoreClassiReperType cercaSogDesc = new CercaSoggettoDescrittoreClassiReperType();
			cercaSogDesc.setCanaliCercaDatiAut(canali);
			cercaSogDesc.setTipoAuthority(SbnAuthority.DE);
			cercaElemento.setCercaDatiAut(cercaSogDesc);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Analitica descrittore: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Analitica descrittore: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end creaAnaliticoSoggettoPerDid

	// ///////////////////////////////////
	// ///////// VARIA SOGGETTO //////////
	// ///////////////////////////////////
	/**
	 * @param frame
	 *            frame di variazione del Soggetto
	 * @param soggettoType
	 *            creato dal frame di variazione del soggetto
	 */
	public SBNMarc variaSoggetto(CreaVariaSoggettoVO soggetto) {
		SoggettoType soggettoType = new SoggettoType();
		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = null;

			modifica = new ModificaType();
			// TIPO CONTROLLO (Conferma, Simile)
			modifica.setTipoControllo(soggetto.isForzaCreazione() ? SbnSimile.CONFERMA : SbnSimile.SIMILE);

			ElementAutType elementAutType = new ElementAutType();
			// TIPO AUTHORITY
			soggettoType.setTipoAuthority(SbnAuthority.SO);

			// Livello di Autorità
			String livello = soggetto.getLivello();
			soggettoType.setLivelloAut(SbnLivello.valueOf(livello));

			// Cid
			soggettoType.setT001(soggetto.getCid());

			// Spezzo il testo del soggetto
			KeySoggetto key = ReticoloSoggetti.getChiaveSoggetto(soggetto.getCodiceSoggettario(), null, soggetto.getTesto());
			A250 a250 = key.getA250();//SoggettiUtil.costruisciSbnMarcStringaSoggetto(soggetto.getTesto());

			// Codice Soggettario
			String codiceSoggettario = soggetto.getCodiceSoggettario();
			a250.setC2_250(codiceSoggettario);

			soggettoType.setT250(a250);

			if (soggetto.isLivelloPolo()) {
				// categoria e nota
				A600 t600 = new A600();
				t600.setA_601(soggetto.getTipoSoggetto());
				t600.setA_602(soggetto.getNote());
				soggettoType.setT600(t600);
				//almaviva5_20111127 evolutive CFI
				String edizione = soggetto.getEdizioneSoggettario();
				if (ValidazioneDati.isFilled(edizione))
					a250.setEdizione(SbnEdizioneSoggettario.valueOf(edizione) );
			}

			// CONDIVISO CON INDICE
			if (soggetto.isCondiviso())
				soggettoType.setCondiviso(DatiElementoTypeCondivisoType.S);
			else
				soggettoType.setCondiviso(DatiElementoTypeCondivisoType.N);

			// Data di variazione
			soggettoType.setT005(soggetto.getT005());

			// Stato Record (C)
			soggettoType.setStatoRecord(StatoRecord.C);
			elementAutType.setDatiElementoAut(soggettoType);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (soggetto.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcDiagnosticoException e) {
			sbnRisposta = SBNMarcUtil.buildMessaggioErrore(e, this.user);

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Varia soggetto: " + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Varia soggetto: " + e.getMessage()	+ e.toString());
		}

		return sbnRisposta;
	} // end variaSoggetto

	// ///////////////////////////////////
	// ///////// VARIA Descrittore //////////
	// ///////////////////////////////////
	/**
	 * @param frame
	 *            frame di variazione del Descrittore
	 * @param descrittoreType
	 *
	 */
	public SBNMarc variaDescrittore(CreaVariaDescrittoreVO descrittore) {
		DescrittoreType descrittoreType = new DescrittoreType();
		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = null;

			modifica = new ModificaType();
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			ElementAutType elementAutType = new ElementAutType();

			// TIPO AUTHORITY
			descrittoreType.setTipoAuthority(SbnAuthority.DE);
			descrittoreType.setFormaNome(SbnFormaNome.valueOf(descrittore.getFormaNome()));

			// Prendo i dati inseriti nel VariaDescrittoreVO
			String stringaDescrittore = descrittore.getTesto();
			String notaDescrittore = descrittore.getNote();
			String codiceSoggettario = descrittore.getCodiceSoggettario();

			// Did
			descrittoreType.setT001(descrittore.getDid());

			// DATI DEL DESCRITTORE
			A931 a931 = new A931();
			// Descrittore Testo
			a931.setA_931(stringaDescrittore);
			// Descrittore Nota
			a931.setB_931(notaDescrittore);
			// Codice Soggettario
			a931.setC2_931(codiceSoggettario);

			descrittoreType.setT931(a931);


			if (descrittore.isLivelloPolo()) {
				if (descrittore.isCondiviso())
					descrittoreType.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					descrittoreType.setCondiviso(DatiElementoTypeCondivisoType.N);
				//almaviva5_20111127 evolutive CFI
				String edizione = descrittore.getEdizioneSoggettario();
				if (ValidazioneDati.isFilled(edizione))
					a931.setEdizione(SbnEdizioneSoggettario.valueOf(edizione) );
				a931.setCat_termine(ValidazioneDati.trimOrNull(descrittore.getCategoriaTermine()));
			}

			// Data di variazione
			descrittoreType.setT005(descrittore.getT005());

			// Livello di Autorità
			String livello = descrittore.getLivelloAutorita();
			descrittoreType.setLivelloAut(SbnLivello.valueOf(livello));

			// Stato Record (C)
			descrittoreType.setStatoRecord(StatoRecord.C);
			// datiElemento.setCondiviso(descrittore.getCondiviso());

			elementAutType.setDatiElementoAut(descrittoreType);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (descrittore.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Varia descrittore: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Varia descrittore: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end variaDescrittore

	/**
	 * Questo metodo fa una richiesta di cancellazione Soggetto al protocollo
	 *
	 * @param id
	 *            cid del soggetto.
	 * @param frame
	 *            frame padre
	 * @return Oggetto Castor con la risposta
	 */
	public SBNMarc cancellaSoggettoDescrittore(boolean livelloPolo, String xid, SbnAuthority authority) {

		SBNMarc sbnRisposta = null;

		try {
			xid = ValidazioneDati.trimOrEmpty(xid).toUpperCase();
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CancellaType cancellaType = new CancellaType();

			SbnOggetto sbnOggetto = new SbnOggetto();
			sbnOggetto.setTipoAuthority(authority);

			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(xid);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCancella(cancellaType);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}

		return sbnRisposta;
	} // end cancellaSoggetto

	@SuppressWarnings("unchecked")
	public List creaListaSinteticaSoggetti(SBNMarc response,
			boolean livelloPolo, String codPolo, String codBib) throws DaoManagerException {
		List listaSintentica = null;

		// Reperimento dei dati della lista: classe SbnOutputType
		SbnMessageType sbnMessage = response.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		int numeroElementi = sbnOutPut.getElementoAutCount();
		if (sbnOutPut.getCountDesSog() != null) {
			int numSoggettoDescrittori = sbnOutPut.getCountDesSog()
					.getSogCount();
			if (numeroElementi < 1) {
				if (numSoggettoDescrittori > 0) {
					listaSintentica = new ArrayList();
					for (int i = 0; i < numSoggettoDescrittori; i++) {
						SogType descrittoreSog = (sbnOutPut.getCountDesSog().getSog(i));
						RicercaSoggettoPerDescrittoriVO descrittori = new RicercaSoggettoPerDescrittoriVO();
						descrittori.setDidAccettato(descrittoreSog.getDid_accettato());
						descrittori.setDesDidAccettato(descrittoreSog.getDes_accettato());
						descrittori.setDesDidRinvio(descrittoreSog.getDid_rinvio());
						descrittori.setDesDidRinvio(descrittoreSog.getDes_rinvio());
						descrittori.setCountDid(descrittoreSog.getDes_des_count());
						descrittori.setCountSogg(descrittoreSog.getSog_count());
						listaSintentica.add(descrittori);
					}
					return listaSintentica;

				} else {
					return new ArrayList();
				}
			}

		} else if (sbnOutPut.getCountDesDes() != null) {
			int numDescrittoreParole = sbnOutPut.getCountDesDes().getDesCount();
			if (numeroElementi < 1) {
				if (numDescrittoreParole > 0) {
					listaSintentica = new ArrayList();
					for (int i = 0; i < numDescrittoreParole; i++) {
						DesType paroleDes = sbnOutPut.getCountDesDes().getDes(i);
						RicercaSoggettoPerDescrittoriVO parole = new RicercaSoggettoPerDescrittoriVO();
						parole.setDesDidAccettato(paroleDes.getParolaDes());
						parole.setCountDid(paroleDes.getDesCount());
						listaSintentica.add(parole);
					}
					return listaSintentica;

				} else {
					return new ArrayList();
				}
			}

		} else {
			if (numeroElementi < 1) {
				return new ArrayList();
			}
		}
		int progressivo = sbnOutPut.getNumPrimo() - 1; // 0;
		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		SbnAuthority tipoAuthority = datiElemento.getTipoAuthority();
		if (ValidazioneDati.eqAuthority(tipoAuthority, SbnAuthority.SO) ) {
			listaSintentica = new ArrayList();
			for (int i = 0; i < numeroElementi; i++) {
				ElementoSinteticaSoggettoVO sogg = creaElementoListaSoggetti(sbnOutPut, i, ++progressivo,	livelloPolo);
				listaSintentica.add(sogg);
			}
		}
		if (ValidazioneDati.eqAuthority(tipoAuthority, SbnAuthority.DE) ) {
			listaSintentica = new ArrayList();
			for (int i = 0; i < numeroElementi; i++) {
				ElementoSinteticaDescrittoreVO desc = creaElementoListaDescrittori(sbnOutPut, i, ++progressivo, livelloPolo, codPolo, codBib);
				listaSintentica.add(desc);
			}
		}
		return listaSintentica;
	}

	public ElementoSinteticaSoggettoVO creaElementoListaSoggetti(
			SbnOutputType sbnOutPut, int elementIndex, int progressivo,
			boolean livelloPolo) throws DaoManagerException {
		ElementoSinteticaSoggettoVO sogg = new ElementoSinteticaSoggettoVO();

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		SoggettoType soggettoType = (SoggettoType) datiElemento;
		A250 a250 = soggettoType.getT250();

		String id = datiElemento.getT001().trim();

		// CONDIVISO CON INDICE

		if (livelloPolo) {
			sogg.setCondiviso(datiElemento.getCondiviso().getType() ==
					DatiElementoTypeCondivisoType.S_TYPE);

			sogg.setCondivisoLista(!sogg.isCondiviso() ? "NO" : "SI");

			// NUMERO TITOLI COLLEGATI IN POLO
			sogg.setNumTitoliPolo(soggettoType.getNum_tit_coll());

			// NUMERO TITOLI COLLEGATI IN BIBLIO
			sogg.setNumTitoliBiblio(soggettoType.getNum_tit_coll_bib());

			//almaviva5_20091028
			SbnLivello maxLivelloAutLegame = soggettoType.getMaxLivelloAutLegame();
			if (maxLivelloAutLegame != null)
				sogg.setMaxLivAutLegame(maxLivelloAutLegame.toString());

			// IMPOSTO IL CAMPO LEGATO A TITOLI "SI" "NO"
			sogg.setIndicatore(sogg.getNumTitoliBiblio() != 0 ? "SI" : "NO");


			// categoria e nota
			A600 t600 = soggettoType.getT600();
			sogg.setCategoriaSoggetto(t600.getA_601());
			sogg.setNota(t600.getA_602());

			sogg.setDatiCondivisione(dao.getDatiCondivisioneSoggetto(id, null, null));

			//almaviva5_20111126 evolutive CFI
			sogg.setCodiceSoggettario(SemanticaUtil.getSoggettarioSBN(a250));
			sogg.setEdizioneSoggettario(a250.getEdizione() != null ? a250.getEdizione().toString() : null);
			sogg.setRank((short) soggettoType.getRank());

		} else {
			//sogg. di indice
			sogg.setCondivisoLista("  ");
			sogg.setDatiCondivisione(dao.getDatiCondivisioneSoggetto(null, id, null));

			//almaviva5_20111126 evolutive CFI
			//in indice non è prevista edizione per il soggettario
			//va ricavato dal codice soggettario inviato dall'indice
			sogg.setCodiceSoggettario(SemanticaUtil.getSoggettarioSBN(a250));
			sogg.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(a250));
		}
		// PROGRESSIVO: Numero dell'elemento
		sogg.setProgr(progressivo);

		// CID: Identificativo dell'elemento
		sogg.setCid(id);

		// STRINGA DI SOGGETTO
		// Composta da a_250 sommato agli x_250, separati da " - ".
		String stringaSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);
		sogg.setTesto(stringaSoggetto);

		// LIVELLO D'AUTORITA'
		SbnLivello livello = datiElemento.getLivelloAut();
		sogg.setStato(livello.toString());

		return sogg;

	}// end creaElementoLista

	public ElementoSinteticaDescrittoreVO creaElementoListaDescrittori(
			SbnOutputType sbnOutPut, int elementIndex, int progressivo, boolean livelloPolo,
			String codPolo, String codBib) {

		ElementoSinteticaDescrittoreVO des = new ElementoSinteticaDescrittoreVO();

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		DescrittoreType descrittoreType = (DescrittoreType) datiElemento;
		A931 a931 = descrittoreType.getT931();

		// prendo il nominativo
		String nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
		String nominativoSenzaRinvio = utilityCastor.getNominativoDatiElemento(datiElemento);

		// PROGRESSIVO: Numero dell'elemento
		des.setProgr(progressivo);

		// DID: Identificativo dell'elemento
		String did = datiElemento.getT001().trim();
		des.setDid(did);

		// forma nome
		des.setFormaNome(datiElemento.getFormaNome().toString());

		// livello autorita
		des.setLivelloAutorita(datiElemento.getLivelloAut().toString());

		if (des.isRinvio() && elementoAut.getLegamiElementoAutCount() > 0) {

			for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

				// il descrittore trovato è una forma di rinvio allora devo
				// mettere la sua forma accettata sotto di questo nella riga
				// della sintetica
				// PRENDO IL LEGAME DOVREBBE avere la sua forma accettata
				LegamiType legamiType = elementoAut.getLegamiElementoAut(i);
				for (int i2 = 0; i2 < legamiType.getArrivoLegameCount(); i2++) {
					String nominativoFormaRinvio = nominativo;

					ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(i2);
					LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
					ElementAutType elementAutType = legameElemento.getElementoAutLegato();
					datiElemento = elementAutType.getDatiElementoAut();

					// LEGAMI
					String descrittoreLegato = utilityCastor.getNominativoDatiElemento(datiElemento);

					// prendo il DID
					String DIDLegato = datiElemento.getT001();
					// nominativoSenzaRinvio = nominativo;
					nominativo = new String(nominativoFormaRinvio
							+ "<br />--> " + DIDLegato + IID_SPAZIO
							+ legameElemento.getTipoLegame().toString()
							+ IID_SPAZIO + descrittoreLegato);
				}
			}
		}

		// imposto il did della forma accettata
		if (des.isRinvio() && elementoAut.getLegamiElementoAutCount() > 0) {
			String didAccettato = elementoAut.getLegamiElementoAut(0).getArrivoLegame(0)
					.getLegameElementoAut().getElementoAutLegato()
					.getDatiElementoAut().getT001();
			des.setDidFormaAccettata(didAccettato);
		}

		// aggiungo il descrittore alla tabella
		des.setTermine(nominativo);
		des.setNome(nominativo);
		des.setNomeSenzaRinvio(nominativoSenzaRinvio);
		des.setKeyDidNome(des.getDid() + IID_SPAZIO + des.getNome());

		if (livelloPolo) {
			//almaviva5_20111126 evolutive CFI
			des.setCodiceSoggettario(SemanticaUtil.getSoggettarioSBN(a931));
			des.setEdizioneSoggettario(a931.getEdizione() != null ? a931.getEdizione().toString() : null);
			des.setCategoriaTermine(a931.getCat_termine());
			try {
				des.setSoggetti(dao.contaSoggettiCollegatiDid(did));
			} catch (Exception e) {
				des.setSoggetti(0);
			}
		} else {
			//did indice
			//almaviva5_20111126 evolutive CFI
			des.setCodiceSoggettario(SemanticaUtil.getSoggettarioSBN(a931));
			des.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(a931));
		}

		return des;

	}// end creaElementoLista

} // end class
