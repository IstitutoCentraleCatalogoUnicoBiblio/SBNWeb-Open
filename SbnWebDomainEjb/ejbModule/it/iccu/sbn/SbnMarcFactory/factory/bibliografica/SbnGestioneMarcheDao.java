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
package it.iccu.sbn.SbnMarcFactory.factory.bibliografica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.DettaglioOggetti;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityMarche;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaMarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.CitazioneStandard;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.SinteticaMarcheView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <p>
 * Title: Interfaccia in diretta
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
 * Funzioni per la creazione e parsing di alberi dom castor relativi alle
 * interrogazioni sulle Marche. Utilizza la classe XMLFactory per scambiare
 * flussi XML con il server sbn, il formato dei flussi XML scambiati rispetta lo
 * schema XSD del protocollo SBN-MARC, tale schema è rappresentato mediante un
 * object model generato con Castor. Le classi che realizzano i frame ed i
 * pannelli dell'interfaccia grafica per l'area marche utilizzano XMLMarche per
 * effettuare interrogazioni e modifiche sui dati delle marche mediante il
 * protocollo SBN-MARcercaMarcaType.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Giuseppe Casafina
 * @version 1.0
 */
public class SbnGestioneMarcheDao {

	private FactorySbn indice;

	private FactorySbn polo;

	private SbnUserType user;

	public SbnGestioneMarcheDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	private static final String INSERIMENTO = "INSERIMENTO";

	private static final String CANCELLAZIONE = "CANCELLAZIONE";

	private static final String INVARIATO = "INVARIATO";

	public static final String IID_STRINGAVUOTA = "";

	public static final String IID_ESITO_POSITIVO = "Si";

	private final String AUT_TIPO_LEGAME_14 = "810";

	private final String AUT_TIPO_LEGAME_15 = "815";

	private List statoOldCitazioni = null;

	private int setKeyCtr = -1;

	private String myChiamata = "";

	private String codSbnBiblioteca = "";

	UtilityCastor utilityCastor = new UtilityCastor();

	/**
	 * Questo metodo viene chiamato quando l'utente preme il tasto ricerca del
	 * JPanelRicercaMarche (vedi: JPanelTastiMarche: Ricerca)
	 *
	 * @param frame
	 *            Frame JFrameAreaMarche
	 *
	 * @return sbnRichiesta Oggetto Castor che rappresenta la lista sintetica
	 *         restituita dal server o un messaggio di errore
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarche(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass) {

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		SBNMarc sbnRisposta = null;

		if (!areaDatiPass.isRicercaPolo() && !areaDatiPass.isRicercaIndice()) {
			areaDatiPassReturn.setCodErr("livRicObblig");
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}
		String esito = controlloFormaleDati(areaDatiPass);
		if (!esito.equals("")) {
			areaDatiPassReturn.setCodErr(esito);
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setNumPrimo(areaDatiPass.getInterrGener().getNumPrimo());
			cercaType.setMaxRighe(areaDatiPass.getInterrGener().getElemXBlocchi());

			// cercaType.setTipoOrd(areaDatiPass.getTipoOrdinamSelez());
			cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass.getInterrGener().getTipoOrdinamSelez()));

			cercaType.setCercaElementoAut(cercaElemento);

			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
			CercaMarcaType cercaMarcaType = new CercaMarcaType();

			if (!(areaDatiPass.getInterrGener().getDescrizione().equals(""))) {
				StringTokenizer st = new StringTokenizer(areaDatiPass
						.getInterrGener().getDescrizione());
				String[] parole = new String[4];
				int i = 0;
				while (st.hasMoreTokens()) {
					parole[i] = st.nextToken();
					i++;
				}
				cercaMarcaType.setParoleAut(parole);
			} else if (!areaDatiPass.getInterrGener().getParolaChiave1().equals("")
							|| !areaDatiPass.getInterrGener().getParolaChiave2().equals("")
							|| !areaDatiPass.getInterrGener().getParolaChiave3().equals("")) {
				if (!areaDatiPass.getInterrGener().getParolaChiave1().equals("")) {
					cercaMarcaType.addB_921(areaDatiPass.getInterrGener().getParolaChiave1());
				}
				if (!areaDatiPass.getInterrGener().getParolaChiave2().equals("")) {
					cercaMarcaType.addB_921(areaDatiPass.getInterrGener().getParolaChiave2());
				}
				if (!areaDatiPass.getInterrGener().getParolaChiave3().equals("")) {
					cercaMarcaType.addB_921(areaDatiPass.getInterrGener().getParolaChiave3());
				}
			} else if (!areaDatiPass.getInterrGener().getMid().equals("")) {
				canali.setT001(areaDatiPass.getInterrGener().getMid()
						.toUpperCase());
				cercaMarcaType.setCanaliCercaDatiAut(canali);
			} else if ((!areaDatiPass.getInterrGener()
					.getCitazioneStandardSelez().equals(""))
					|| (!areaDatiPass.getInterrGener().getSiglaRepertorio()
							.equals(""))) {
				// CITAZIONE e REPERTORIO
				cercaMarcaType.setCitazione(Integer.valueOf(areaDatiPass
						.getInterrGener().getSiglaRepertorio()));
				cercaMarcaType.setRepertorio(areaDatiPass.getInterrGener()
						.getCitazioneStandardSelez());
			} else if (!areaDatiPass.getInterrGener().getMotto().equals("")) {
				// MOTTO (Ricerca per stringa troncata)
				cercaMarcaType.setE_921(areaDatiPass.getInterrGener()
						.getMotto());
			}
			// TIPO AUTHORITY
			cercaMarcaType.setTipoAuthority(SbnAuthority.MA);

			cercaElemento.setCercaDatiAut(cercaMarcaType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			int totRighe = 0;

			if (areaDatiPass.isRicercaPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				if (!sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getEsito().equals("3001")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta
							.getSbnMessage().getSbnResponse().getSbnResult()
							.getEsito()
							+ sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				if (totRighe == 0) {
					if (areaDatiPass.isRicercaIndice()) {
						this.indice.setMessage(sbnmessage, this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							areaDatiPassReturn.setCodErr("noServerInd");
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						}
						if (!sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getEsito().equals("0000")
								&& !sbnRisposta.getSbnMessage()
										.getSbnResponse().getSbnResult()
										.getEsito().equals("3001")) {
							areaDatiPassReturn.setCodErr("9999");
							areaDatiPassReturn.setTestoProtocollo(sbnRisposta
									.getSbnMessage().getSbnResponse()
									.getSbnResult().getEsito()
									+ sbnRisposta.getSbnMessage()
											.getSbnResponse().getSbnResult()
											.getTestoEsito());
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						}
						totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResponseTypeChoice().getSbnOutput()
								.getTotRighe();
						if (totRighe == 0) {
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						} else {
							areaDatiPassReturn.setLivelloTrovato("I");
						}
					} else {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					}
				} else {
					areaDatiPassReturn.setLivelloTrovato("P");
				}
			} else if (areaDatiPass.isRicercaIndice()) {
				this.indice.setMessage(sbnmessage, this.user);



				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				if (!sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getEsito().equals("3001")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta
							.getSbnMessage().getSbnResponse().getSbnResult()
							.getTestoEsito());
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				if (totRighe == 0) {
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				} else {
					areaDatiPassReturn.setLivelloTrovato("I");
				}
			}

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaMarcheView sinteticaMarcheVO;
			List listaSintentica = new ArrayList();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getElementoAut().length != maxRighe) {
				tappoScorrimento = totRighe;
			}
			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaMarcheVO = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaMarcheVO);
			}

			int numBlocchi = 1;
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
			}

			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setMaxRighe(maxRighe);
			areaDatiPassReturn.setTotRighe(totRighe);
			areaDatiPassReturn.setNumBlocco(1);
			areaDatiPassReturn.setNumNotizie(totRighe);
			areaDatiPassReturn.setTotBlocchi(numBlocchi);
			areaDatiPassReturn.setListaSintetica(listaSintentica);

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}

	public String controlloFormaleDati(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass) {
		String esito = "";

		int combinazioni = 0;

		if (areaDatiPass.getInterrGener().getDescrizione().length() > 0) {
			StringTokenizer st = new StringTokenizer(areaDatiPass
					.getInterrGener().getDescrizione());
			if (st.countTokens() > 4)
				return "ric021";
		}

		if (areaDatiPass.getInterrGener().getDescrizione().length() > 0)
			combinazioni = combinazioni + 1;

		if (areaDatiPass.getInterrGener().getParolaChiave1().length() > 0
				|| areaDatiPass.getInterrGener().getParolaChiave2().length() > 0
				|| areaDatiPass.getInterrGener().getParolaChiave3().length() > 0)
			combinazioni = combinazioni + 1;

		if (areaDatiPass.getInterrGener().getMid().length() > 0)
			combinazioni = combinazioni + 1;

		if ((!areaDatiPass.getInterrGener().getCitazioneStandardSelez().equals(
				""))
				&& (areaDatiPass.getInterrGener().getSiglaRepertorio()
						.equals("")))
			return "ric024";
		if ((areaDatiPass.getInterrGener().getCitazioneStandardSelez()
				.equals(""))
				&& (!areaDatiPass.getInterrGener().getSiglaRepertorio().equals(
						"")))
			return "ric023";

		if ((!areaDatiPass.getInterrGener().getCitazioneStandardSelez().equals(
				""))
				&& (!areaDatiPass.getInterrGener().getSiglaRepertorio().equals(
						"")))
			combinazioni = combinazioni + 1;

		if ((!areaDatiPass.getInterrGener().getMotto().equals("")))
			combinazioni = combinazioni + 1;

		if (combinazioni == 0)
			return "noCanPrim";
		if (combinazioni > 1)
			return "soloUnCanPrim";

		return esito;
	}

	/**
	 * Ottiene un'altro blocco della lista sintetica delle marche.
	 *
	 * @return Oggetto Castor
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoMarche(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass) {
		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		int numPrimo = areaDatiPass.getNumPrimo();
		int maxRighe = areaDatiPass.getMaxRighe();
		String idLista = areaDatiPass.getIdLista();

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaMarcaType cercaMarcaType = new CercaMarcaType();

			cercaMarcaType.setTipoAuthority(SbnAuthority.MA);
			cercaElemento.setCercaDatiAut(cercaMarcaType);

			cercaType.setTipoOutput(SbnTipoOutput.valueOf(areaDatiPass
					.getTipoOutput()));
			cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass
					.getTipoOrdinam()));
			cercaType.setNumPrimo(numPrimo);
			cercaType.setMaxRighe(maxRighe);
			cercaType.setIdLista(idLista);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (areaDatiPass.isRicercaPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}
			if (areaDatiPass.isRicercaIndice()) {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

			if (sbnRisposta == null) {
				if (areaDatiPass.isRicercaPolo())
					areaDatiPassReturn.setCodErr("noServerLoc");
				if (areaDatiPass.isRicercaIndice())
					areaDatiPassReturn.setCodErr("noServerInd");
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0102")) {
				areaDatiPassReturn.setCodErr("fineScorrimento");
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta
						.getSbnMessage().getSbnResponse().getSbnResult()
						.getTestoEsito());
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}

			int numNotizie = 0;
			numPrimo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			numNotizie = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			areaDatiPassReturn.setNumNotizie(numNotizie);
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			SinteticaMarcheView sinteticaMarcheVO;
			List listaSinteticaSuccessiva = new ArrayList();

			int tappoRicerca = maxRighe;
			if ((totRighe - numPrimo + 1) < maxRighe) {
				tappoRicerca = (totRighe - numPrimo + 1);
			}

			for (int t = 0; t < tappoRicerca; t++) {
				sinteticaMarcheVO = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t, numPrimo);
				listaSinteticaSuccessiva.add(sinteticaMarcheVO);
				numPrimo++;
			}

			idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);

			areaDatiPassReturn.setListaSintetica(listaSinteticaSuccessiva);

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}

	public SinteticaMarcheView creaElementoLista(SbnOutputType sbnOutPut,
			int elementIndex, int progressivo) {

		// List contenente la nuova riga alla tabella
		SinteticaMarcheView data = new SinteticaMarcheView();

		// VOCE: icona della prima colonna della tabella
		data.setImageUrl("blank.png");

		// PROGRESSIVO: Numero dell'elemento
		data.setProgressivo(progressivo);

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		MarcaType marcaType = (MarcaType) datiElemento;
		A921 a921 = marcaType.getT921();

		// MID: Identificativo dell'elemento e NOMINATIVO: descrizione
		// dell'elemento
		data.setMid(datiElemento.getT001().trim());

		 if (datiElemento.getCondiviso() == null){
         	data.setFlagCondiviso(true);
         } else {
         	 if (datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
              	data.setFlagCondiviso(false);
  			} else {
  				data.setFlagCondiviso(true);
  			}
         }

		 if (!data.isFlagCondiviso()) {
				data.setNome("[loc] " + utilityCastor.getNominativoDatiElemento(datiElemento));
			} else {
				data.setNome(utilityCastor.getNominativoDatiElemento(datiElemento));
			}

		data.setKeyMidNomeFinto(data.getMid() + data.getNome());

		// LIVELLO D'AUTORITA'
		SbnLivello livello = datiElemento.getLivelloAut();
		data.setLivelloAutorita(livello.toString());

		// MOTTO
		if (a921.getE_921() != null) {
			data.setMotto(a921.getE_921().toString());
		} else {
			data.setMotto(" ");
		}

		// CITAZIONE STANDARD
		// Se la marca non ha legami a repertori, al posto della citazione
		// standard
		// verrà aggiunta una stringa vuota al List data
		String citazioneStandardString = "";

		// IMMAGINI LEGATE A MARCA
		if (marcaType.getT856Count() > 0) {
			for (int i = 0; i < marcaType.getT856Count(); i++) {
				byte[] buf = marcaType.getT856(i).getC9_856_1();
				if (ValidazioneDati.isFilled(buf) ) {
					data.getListaImmagini().add(buf);
					data.setImageUrl("immaginiMarca.gif");
				}
			}
		}

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				String tipoLegameString = sbnLegameAut.toString();

				// REPERTORI LEGATI ALLA MARCA (tipo legame 810 o 815)
				if ((tipoLegameString.equals("810"))
						|| (tipoLegameString.equals("815"))) {

					// SIGLA REPERTORIO
					String siglaRepertorioLegato = legameElemento.getIdArrivo();
					// NUMERO (Citazione)
					int citazioneLegata = legameElemento.getCitazione();

					// CITAZIONE STANDARD (SIGLA REPERTORIO + NUMERO)
					String citazioneStandard = siglaRepertorioLegato
							+ citazioneLegata;

					citazioneStandardString = citazioneStandardString
							+ citazioneStandard;

					// Carattere separatore delle citazioni standard: virgola
					// Ma prima controlla che ce ne siano altre
					if (j != (legamiType.getArrivoLegameCount() - 1)) {
						citazioneStandardString = citazioneStandardString
								+ ", ";
					}

				}
			}// end for interno
		}// end for esterno

		data.setCitazione(citazioneStandardString);

		return data;
	}

	/**
	 * Questo metodo crea un oggetto Castor pronto per fare una richiesta per
	 * Mid
	 *
	 * @param Mid:
	 *            Mid Dell'autore.
	 *
	 * @return Oggetto Castor
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoMarchePerMid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass) {
		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

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

			// Mid
			canali.setT001(areaDatiPass.getBidRicerca());

			CercaMarcaType cercaMarcaType = new CercaMarcaType();
			cercaMarcaType.setCanaliCercaDatiAut(canali);
			cercaMarcaType.setTipoAuthority(SbnAuthority.MA);
			cercaElemento.setCercaDatiAut(cercaMarcaType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
			String chiamata = IID_STRINGAVUOTA;
			if (areaDatiPass.isRicercaPolo()) {
				chiamata = "P";
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}

			if (areaDatiPass.isRicercaIndice()) {
				areaDatiPassLocal.setIndice(true);
				areaDatiPassLocal.setPolo(false);
				areaDatiPassLocal.setCodiceSbn(this.user.getBiblioteca());
				chiamata = "I";

				// Inizio modifica almaviva2 ARCOBALENO -richiesta tipo 4 per avere subito le localizzazioni
				sbnmessage.getSbnRequest().getCerca().setTipoOutput(SbnTipoOutput.VALUE_3);
				// Fine modifica almaviva2 ARCOBALENO

				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

			if (sbnRisposta == null) {
				areaDatiPassReturn.setTreeElementViewTitoli(null);
				return areaDatiPassReturn;
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				areaDatiPassReturn.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				return areaDatiPassReturn;
			}

			TreeElementViewTitoli root = new TreeElementViewTitoli();
			root = this.getReticoloMarca(sbnRisposta, root,
					chiamata, areaDatiPassLocal);
			areaDatiPassReturn.setTreeElementViewTitoli(root);

		} catch (IllegalArgumentException ie) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());

		}
		return areaDatiPassReturn;
	}// end creaRichiestaAnaliticoMarchePerMid

	/**
	 * Creazione di una nuova marca
	 *
	 * @param frame
	 *            frame che contiene i dati della nuova marca da creare
	 * @param marcaType
	 *            creato dal frame di inserimento della marca, contiene i dati e
	 *            le immagini della marca
	 */

	public AreaDatiVariazioneReturnVO inserisciMarca(
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		// Controllo sui campi inseriti
		UtilityMarche utilityMarche = new UtilityMarche();
		String esito = utilityMarche.isOkControlli(areaDatiPass);

		if (!esito.equals("")) {
			// BUG MANTIS 3513 almaviva2: esito era tra verigolette e quindi il diagnostico era sbagliato
			areaDatiPassReturn.setCodErr(esito);
			return areaDatiPassReturn;
		}

		String livello = areaDatiPass.getDettMarcaVO().getLivAut();
		if (areaDatiPass.isInserimentoIndice()) {
			if (Integer.valueOf(livello) < 5) {
				areaDatiPassReturn.setCodErr("livAutInfMinimoIndice");
				return areaDatiPassReturn;
			}
		}


		// Controlla se sono state inserite due citazioni
		// con sigle di repertorio uguali.
		if (utilityMarche.isCitazioneDuplicata(areaDatiPass)) {
			esito = "ins035";
			// BUG MANTIS 3513 almaviva2: esito era tra verigolette e quindi il diagnostico era sbagliato
			areaDatiPassReturn.setCodErr(esito);
			return areaDatiPassReturn;
		}

		SBNMarc sbnRisposta = null;

		SbnMessageType sbnmessage = new SbnMessageType();
		SbnRequestType sbnrequest = new SbnRequestType();
		CreaType crea = null;
		CreaTypeChoice creaTypeChoice = null;
		ModificaType modifica = null;

		if (!areaDatiPass.isModifica()) {
			crea = new CreaType();
			creaTypeChoice = new CreaTypeChoice();
		} else {
			modifica = new ModificaType();
		}

		if (!areaDatiPass.isModifica()) {
			crea.setTipoControllo(SbnSimile.CONFERMA);
		} else {
			modifica.setTipoControllo(SbnSimile.CONFERMA);
		}

		MarcaType marcaType = new MarcaType();

		ElementAutType elementAutType = new ElementAutType();
		DatiElementoType datiElemento = new DatiElementoType();

		datiElemento.setTipoAuthority(SbnAuthority.MA);

		marcaType.setTipoAuthority(SbnAuthority.MA);

		marcaType.setLivelloAut(SbnLivello.valueOf(livello));

		String descrizione = areaDatiPass.getDettMarcaVO().getDesc();
		String arrayParoleChiave[] = new String[5];
		arrayParoleChiave = utilityMarche
				.getArrayParoleChiave(areaDatiPass);
		String motto = areaDatiPass.getDettMarcaVO().getMotto();
		String nota = areaDatiPass.getDettMarcaVO().getNota();

		// Inizio a impostare i dati della marca editoriale
		if (!areaDatiPass.isModifica()) {

			AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
			areaDatiPassGetIdSbn.setTipoAut("MA");
			areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
			if (areaDatiPassGetIdSbn.getIdSbn() == null
					|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
				areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn.getCodErr());
				areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn.getTestoProtocollo());
				return areaDatiPassReturn;
			}
			marcaType.setT001(areaDatiPassGetIdSbn.getIdSbn());
		} else {
			marcaType.setT001(areaDatiPass.getDettMarcaVO().getMid());
		}

		A921 a921 = new A921();
		a921.setA_921(descrizione);
		a921.setB_921(arrayParoleChiave);
		a921.setE_921(motto);
		a921.setD_921(nota);
		marcaType.setT921(a921);

		datiElemento = marcaType;

		// DATA VARIAZIONE
		if (areaDatiPass.isModifica()) {
			datiElemento.setT005(areaDatiPass.getDettMarcaVO()
					.getVersione());
			if (areaDatiPass.isVariazione()) {
				datiElemento.setStatoRecord(StatoRecord.C);
			}
		}

		elementAutType.setDatiElementoAut(datiElemento);

		if (areaDatiPass.isModifica()) {
			// Numero delle citazioni standard della marca prima di essere
			// variata.
			int oldCount = utilityMarche.getCountCitazioniOld(areaDatiPass);
			String[] arrayCitazioniOld = getArrayCitazioniStandardOld(
					areaDatiPass, oldCount);

			// Numero delle citazioni standard della marca da variare
			int count = utilityMarche
					.getCountCitazioniStandard(areaDatiPass);
			String[] arrayCitazioni = getArrayCitazioniStandardInserite(
					areaDatiPass, count);

			// List con le Citazioni standard su cui operare
			List statoNewCitazioni = confrontoArrayCitazioni(
					arrayCitazioniOld, arrayCitazioni);

			// Elimina dal List tutte le Citazioni Standard che non
			// bisogna prendere in considerazione (stato INVARIATO)
			List vectorOperazioni = impostaListOperazioniLegami(
					statoNewCitazioni, statoOldCitazioni);

			// Array con i nuovi legami di inserimento/cancellazione
			LegamiType[] arrayLegamiType = new LegamiType[vectorOperazioni
					.size()];

			// Imposta i legami a Repertorio della Marca
			setLegamiCitazioniPerVariazione(arrayLegamiType,
					vectorOperazioni, areaDatiPass.getDettMarcaVO()
							.getMid());

			// Aggiunta di tutti i Legami
			elementAutType.setLegamiElementoAut(arrayLegamiType);
		} else {
			int count = utilityMarche
					.getCountCitazioniStandard(areaDatiPass);
			String[] arrayCitazioni = getArrayCitazioniStandardInserite(
					areaDatiPass, count);

			if (count > 0) {
				LegamiType legamiType;
				ArrivoLegame arrivoLegame;
				LegameElementoAutType legameElementoAut;
				LegamiType[] arrayLegamiType = new LegamiType[count];
				for (int i = 0; i < count; i++) {
					legamiType = new LegamiType();
					if (!areaDatiPass.isModifica()) {
						legamiType.setIdPartenza(SBNMarcUtil.SBNMARC_DEFAULT_ID);
					} else {
						legamiType.setIdPartenza(areaDatiPass
								.getDettMarcaVO().getMid());
					}

					legameElementoAut = new LegameElementoAutType();
					arrivoLegame = new ArrivoLegame();
					legameElementoAut.setTipoAuthority(SbnAuthority.RE);
					String tipoLegame = IID_ESITO_POSITIVO;
					legameElementoAut.setTipoLegame(utilityCastor
							.codificaLegameRepertorio(tipoLegame));
					impostaLegameCitazioni(legameElementoAut,
							arrayCitazioni, i);
					arrivoLegame.setLegameElementoAut(legameElementoAut);
					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;
					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[i] = legamiType;
				}// end for
				// Aggiunta di tutti i Legami
				elementAutType.setLegamiElementoAut(arrayLegamiType);
			}// end if
		}

		// almaviva5_20071123 IMMAGINI LEGATE A MARCA
		List listaImmagini = areaDatiPass.getDettMarcaVO().getListaImmagini();

		if (areaDatiPass.isModifica()) {
			if (listaImmagini.size() > 0) {
				for (Object o : listaImmagini) {
					byte[] img = (byte[]) o;
					C856 c856 = new C856();
					c856.setU_856("test");
					c856.setC9_856_1(img);
					marcaType.addT856(c856);
				}
			} else {
				C856 c856 = new C856();
				marcaType.addT856(c856);
			}
		} else {
			if (listaImmagini.size() > 0) {
				for (Object o : listaImmagini) {
					byte[] img = (byte[]) o;
					C856 c856 = new C856();
					c856.setU_856("test");
					c856.setC9_856_1(img);
					marcaType.addT856(c856);
				}
			}
		}

		if (!areaDatiPass.isModifica()) {
			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);
		} else {
			modifica.setElementoAut(elementAutType);
		}
		if (!areaDatiPass.isModifica()) {
			sbnrequest.setCrea(crea);
		} else {
			sbnrequest.setModifica(modifica);
		}

		sbnmessage.setSbnRequest(sbnrequest);

		// ==================================================================
		// almaviva2 21.09.2009 Fine   nuova gestione marche in modo da consentirne la catalogazione locale e poi la condivisione

		if (!areaDatiPass.isModifica()) {
			if (areaDatiPass.isInserimentoIndice() && areaDatiPass.isFlagCondiviso()) {
				// CASO DI CREAZIONE LUOGO IN INDICE: Operazioni da compiere
				// 1. inserimento in indice;
				// 2. inserimento in locale;
				// 3. localizzazione in indice;
				// 3. localizzazione in polo;
				areaDatiPassReturn = chiamataInsertIndice(sbnmessage, areaDatiPass);
			}
			if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
				// CASO DI CONDIVISIONE LUOGO CATALOGATO LOCALMENTE IN INDICE: Operazioni da
				// compiere
				// 1. inserimento in indice;
				// 2. aggiornamento in locale (solo per la parte relativa al flagCondiviso che passa da N a S;
				// 3. localizzazione in indice;
				areaDatiPassReturn = chiamataInsertIndiceUdatePolo(sbnmessage, areaDatiPass);
			}

			if (areaDatiPass.isInserimentoPolo() && !areaDatiPass.isFlagCondiviso()) {
				// CASO DI CREAZIONE LUOGO IN POLO: Operazioni da
				// compiere
				// 2. inserimento in locale;
				// 3. localizzazione SOLO in polo CATALOGAZIONE LOCALE;
				areaDatiPassReturn = chiamataInsertLocale(sbnmessage, areaDatiPass);
			}
		}
		if (areaDatiPass.isModifica()) {
			if (areaDatiPass.isInserimentoIndice()) {
				// CASO DI MODIFICA LUOGO IN INDICE: Operazioni da compiere
				// 1. variazione in indice;
				// 2. variazione in locale;
				areaDatiPassReturn = chiamataUpdateIndice(sbnmessage, areaDatiPass);
				if (!areaDatiPassReturn.getCodErr().equals("")) {
					return areaDatiPassReturn;
				}

			}
			if (areaDatiPass.isInserimentoPolo() && !areaDatiPass.isFlagCondiviso()) {
				// CASO DI MODIFICA TITOLO SU TITOLO CATALOGATO IN LOCALE:
				// Operazioni da compiere
				// 2. variazione in locale;
				areaDatiPassReturn = chiamataUpdateLocale(sbnmessage, areaDatiPass);

			}
		}

		return areaDatiPassReturn;
		// almaviva2 21.09.2009 Fine   nuova gestione marche in modo da consentirne la catalogazione locale e poi la condivisione
	}// end creaMarca


	private AreaDatiVariazioneReturnVO chiamataInsertIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {
			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}


		// Controllo per la visualizzazione del messaggio
		// di "Simili trovati" solo sul primo blocco ricevuto.
		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerInd");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);

		// nelle marche non c'è controllo sui simili
		//	if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
		//		!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") ) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		// operazione andata a buon fine; in caso di Inserimento nuova
		// marca si invia la chiamata per localizzazione in Indice della marca

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
		areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage()
				.getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0)
				.getDatiElementoAut().getT001());
		areaLocalizza.setAuthority("MA");
		areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
		areaLocalizza.setTipoOpe("Localizza");
		areaLocalizza.setTipoLoc("Gestione");
		areaLocalizza.setIndice(true);
		areaLocalizza.setPolo(false);

		AreaDatiVariazioneReturnVO areaDatiPassReturnLocalizza = new AreaDatiVariazioneReturnVO();

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		areaDatiPassReturnLocalizza = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
		if (!areaDatiPassReturnLocalizza.getCodErr().equals("0000")) {
			areaDatiPassReturn.setCodErr(areaDatiPassReturnLocalizza.getCodErr());
			areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocalizza.getTestoProtocollo());
			return areaDatiPassReturn;
		}

		sbnmessage.getSbnRequest().getCrea().setTipoControllo(SbnSimile.CONFERMA);

		try {
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerPol");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
		return areaDatiPassReturn;

	}

	private AreaDatiVariazioneReturnVO chiamataInsertIndiceUdatePolo(SbnMessageType sbnmessage,
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		String verIndice = "";
		String verPolo = "";

//		 CASO DI CREAZIONE LUOGO IN INDICE: Operazioni da compiere
		// 1. inserimento in indice;

		try {
			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerInd");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}


		// Valorizzazione del campo versione Indice con la risposta appena ottenuta

		if (sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
					.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiDocumento() != null) {
				verIndice = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiDocumento().getT005();
			} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
					.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiTitAccesso() != null) {
				verIndice = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiTitAccesso().getT005();
			}
		} else if (sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
			verIndice = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
			.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT005();
		}


		// 2. aggiornamento in locale del solo flag condiviso
		ElementAutType elementAutType = sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut();

		ModificaType modificaType = null;
		modificaType = new ModificaType();

		modificaType.setTipoControllo(SbnSimile.CONFERMA);
		modificaType.setElementoAut(elementAutType);

		modificaType.getElementoAut().getDatiElementoAut().setStatoRecord(StatoRecord.C);
		modificaType.getElementoAut().getDatiElementoAut().setT005(areaDatiPass.getDettMarcaVO().getVersione());
		modificaType.getElementoAut().getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.S);

		sbnmessage.getSbnRequest().setModifica(modificaType);
		sbnmessage.getSbnRequest().setCrea(null);

		try {
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerPol");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
				.getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		// Valorizzazione del campo versione Indice con la risposta appena ottenuta

		if (sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
					.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiDocumento() != null) {
				verPolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiDocumento().getT005();
			} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
					.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiTitAccesso() != null) {
				verPolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getDocumento(0).getDocumentoTypeChoice().getDatiTitAccesso().getT005();
			}
		} else if (sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
			verPolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
			.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT005();
		}

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
		areaDatiPassReturn.setVersioneIndice(verIndice);
		areaDatiPassReturn.setVersionePolo(verPolo);
		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataInsertLocale(SbnMessageType sbnmessage,
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;
		sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().
					getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.N);
		sbnmessage.getSbnRequest().getCrea().setTipoControllo(SbnSimile.CONFERMA);

		try {
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}


		// Controllo per la visualizzazione del messaggio
		// di "Simili trovati" solo sul primo blocco ricevuto.
		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerPol");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());

		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataUpdateIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {
			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		// Controllo per la visualizzazione del messaggio
		// di "Simili trovati" solo sul primo blocco ricevuto.
		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerInd");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		// Inizio verifica di esistenza dell'Autore su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getDettMarcaVO().getMid());
		areaDatiControlliPoloVO.setTipoAut("MA");
		areaDatiControlliPoloVO.setCancellareInferiori(false);

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
			areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
			return areaDatiPassReturn;
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
			if (areaDatiControlliPoloVO.getFormaNome().equals("R") && areaDatiControlliPoloVO.getTimeStampRinvio() != null) {
				datiElementoTypePolo = new DatiElementoType();
				datiElementoTypePolo.setT005(areaDatiControlliPoloVO.getTimeStampRinvio());
			} else {
				// squadratura Base Dati: manca su Polo l'oggetto in Modifica
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo("Attenzione: l'autore è assente dalla Base dati locale");
				return areaDatiPassReturn;
			}
		} else {
			datiElementoTypePolo = areaDatiControlliPoloVO.getDatiElementoType();
		}
//				 Fine verifica di esistenza del Documento su Polo

		sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(datiElementoTypePolo.getT005());
		areaDatiPass.setPrimoBloccoSimili(true);

		try {
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}

		// Controllo per la visualizzazione del messaggio
		// di "Simili trovati" solo sul primo blocco ricevuto.
		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerPol");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}
		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
		return areaDatiPassReturn;

	}


	private AreaDatiVariazioneReturnVO chiamataUpdateLocale(SbnMessageType sbnmessage,
			AreaDatiVariazioneMarcaVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;
		}


		// Controllo per la visualizzazione del messaggio
		// di "Simili trovati" solo sul primo blocco ricevuto.
		if (sbnRisposta == null) {
			areaDatiPassReturn.setCodErr("noServerPol");
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setPrimoBloccoSimili(false);
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
		return areaDatiPassReturn;

	}



	/**
	 * Imposta l'idArrivo e la Citazione, ad un legame di tipo
	 * LegameElementoAutType.
	 *
	 * @param legame
	 *            LegameElementoAutType
	 * @param arrayCitazioni
	 *            String[]
	 * @param i
	 *            int
	 */
	public void impostaLegameCitazioni(LegameElementoAutType legame,
			String[] arrayCitazioni, int i) {

		switch (i) {
		case 0:
			legame.setIdArrivo(arrayCitazioni[0]);
			legame.setCitazione(Integer.parseInt(arrayCitazioni[1]));
			break;
		case 1:
			legame.setIdArrivo(arrayCitazioni[2]);
			legame.setCitazione(Integer.parseInt(arrayCitazioni[3]));
			break;
		case 2:
			legame.setIdArrivo(arrayCitazioni[4]);
			legame.setCitazione(Integer.parseInt(arrayCitazioni[5]));
			break;
		}
	}// end impostaLegameCitazioni

	/**
	 * Restituisce un array di String[] contenente le Citazioni Standard
	 * inserite (pos.1 id arrivo; pos.2 citazione).
	 *
	 * @param frame
	 *            JFrameInserisceMarca
	 * @param count
	 *            int
	 * @return arrayCitazioni String[]
	 */
	public String[] getArrayCitazioniStandardInserite(
			AreaDatiVariazioneMarcaVO areaDatiPass, int count) {

		/*
		 * count indica il numero di citazioni standard inserite, mentre la
		 * dimensione dell'array è count*2 perchè una citazione standard è
		 * formata da una coppia di valori, cioè "id arrivo + citazione".
		 */
		String[] arrayCitazioni = new String[count * 2];
		int pos = 0;

		if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep1().equals(
				IID_STRINGAVUOTA))
				&& (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1()
						.equals(IID_STRINGAVUOTA))) {
			arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep1();
			arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep1();
			pos = pos + 2;
		}
		if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep2().equals(
				IID_STRINGAVUOTA))
				&& (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2()
						.equals(IID_STRINGAVUOTA))) {
			arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep2();
			arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep2();
			pos = pos + 2;
		}
		if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep3().equals(
				IID_STRINGAVUOTA))
				&& (!areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3()
						.equals(IID_STRINGAVUOTA))) {
			arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
					.getCampoCodiceRep3();
			arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
					.getCampoProgressivoRep3();
		}

		return arrayCitazioni;

	}// end getArrayCitazioniStandardInserite

	public String[] getArrayCitazioniStandardOld(
			AreaDatiVariazioneMarcaVO areaDatiPass, int count) {

		/*
		 * count indica il numero di citazioni standard inserite, mentre la
		 * dimensione dell'array è count*2 perchè una citazione standard è
		 * formata da una coppia di valori, cioè "id arrivo + citazione".
		 */
		String[] arrayCitazioni = new String[count * 2];
		int pos = 0;

		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep1Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep1Old() == null) {

		} else {
			if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep1Old().equals(
					IID_STRINGAVUOTA))
					&& (!areaDatiPass.getDettMarcaVO()
							.getCampoProgressivoRep1Old().equals(
									IID_STRINGAVUOTA))) {
				arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
						.getCampoCodiceRep1Old();
				arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
						.getCampoProgressivoRep1Old();
				pos = pos + 2;
			}
		}

		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep2Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep2Old() == null) {
		} else {
			if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep2Old().equals(
					IID_STRINGAVUOTA))
					&& (!areaDatiPass.getDettMarcaVO()
							.getCampoProgressivoRep2Old().equals(
									IID_STRINGAVUOTA))) {
				arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
						.getCampoCodiceRep2Old();
				arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
						.getCampoProgressivoRep2Old();
				pos = pos + 2;
			}
		}

		if (areaDatiPass.getDettMarcaVO().getCampoCodiceRep3Old() == null
				|| areaDatiPass.getDettMarcaVO().getCampoProgressivoRep3Old() == null) {
		} else {
			if ((!areaDatiPass.getDettMarcaVO().getCampoCodiceRep3Old().equals(
					IID_STRINGAVUOTA))
					&& (!areaDatiPass.getDettMarcaVO()
							.getCampoProgressivoRep3Old().equals(
									IID_STRINGAVUOTA))) {
				arrayCitazioni[pos] = areaDatiPass.getDettMarcaVO()
						.getCampoCodiceRep3Old();
				arrayCitazioni[pos + 1] = areaDatiPass.getDettMarcaVO()
						.getCampoProgressivoRep3Old();
			}
		}
		return arrayCitazioni;

	}// end getArrayCitazioniStandardInserite

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarcheCollegate(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 003
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1

//			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

			CercaDatiAutType cercaDatiAutType = new CercaDatiAutType();
			cercaDatiAutType.setTipoAuthority(SbnAuthority.MA);

			ArrivoLegame arrivoLegame = new ArrivoLegame();

			switch (areaDatiPass.getTipoOggetto()) {
			case TitoliCollegatiInvoke.MARCHE_COLLEGATE_A_AUTORE:
				LegameElementoAutType legameElemento = new LegameElementoAutType();
				legameElemento.setTipoAuthority(SbnAuthority.AU);
				legameElemento.setTipoLegame(SbnLegameAut.valueOf("921"));
				legameElemento.setIdArrivo(areaDatiPass.getOggDiRicerca());
				arrivoLegame.setLegameElementoAut(legameElemento);
				break;
			case TitoliCollegatiInvoke.MARCHE_COLLEGATE_A_TITOLO:

				String NaturaDaLista = areaDatiPass.getNaturaTitBase();
				String TMaterialeDaLista = areaDatiPass.getTipMatTitBase();
				if ((NaturaDaLista.toUpperCase().equals("A"))
						&& (TMaterialeDaLista.toUpperCase().equals("U"))) {
					LegameElementoAutType legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setTipoAuthority(SbnAuthority.UM);
					legameElementoAut.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("tutti"));
					arrivoLegame.setLegameElementoAut(legameElementoAut);

				} else if ((NaturaDaLista.toUpperCase().equals("A"))
						&& (TMaterialeDaLista.toUpperCase().equals(""))) {
					LegameElementoAutType legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setTipoAuthority(SbnAuthority.TU);
					legameElementoAut.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("tutti"));
					arrivoLegame.setLegameElementoAut(legameElementoAut);

				} else if ((NaturaDaLista.toUpperCase().equals("B"))
						|| (NaturaDaLista.toUpperCase().equals("D"))
						|| (NaturaDaLista.toUpperCase().equals("T"))
						|| (NaturaDaLista.toUpperCase().equals("P"))) {
					LegameTitAccessoType legameTitAccesso = new LegameTitAccessoType();
					legameTitAccesso
							.setIdArrivo(areaDatiPass.getOggDiRicerca());
					legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.VALUE_0);
					arrivoLegame.setLegameTitAccesso(legameTitAccesso);
				} else if ((NaturaDaLista.toUpperCase().equals("M"))
						|| (NaturaDaLista.toUpperCase().equals("S"))
						|| (NaturaDaLista.toUpperCase().equals("C"))
						|| (NaturaDaLista.toUpperCase().equals("W"))
						|| (NaturaDaLista.toUpperCase().equals("F"))
						|| (NaturaDaLista.toUpperCase().equals("N"))) {
					LegameDocType legameElementoDoc = new LegameDocType();
					legameElementoDoc.setTipoLegame(SbnLegameDoc
							.valueOf("tutti"));
					legameElementoDoc.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameDoc(legameElementoDoc);
				}
				break;
			}

			cercaElemento.setCercaDatiAut(cercaDatiAutType);
			cercaElemento.setArrivoLegame(arrivoLegame);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			int totRighe = 0;
			if (areaDatiPass.isRicercaPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				} else {
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getTotRighe();
					if (totRighe == 0) {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					} else {
						areaDatiPassReturn.setLivelloTrovato("P");
					}
				}
			} else if (areaDatiPass.isRicercaIndice()) {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				} else {
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getTotRighe();
					if (totRighe == 0) {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					} else {
						areaDatiPassReturn.setLivelloTrovato("I");
					}
				}
			}

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaMarcheView sinteticaMarcheVO;
			List listaSintentica = new ArrayList();

			for (int t = 0; t < totRighe; t++) {

				++progressivo;
				sinteticaMarcheVO = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaMarcheVO);
			}

			int numBlocchi = 1;
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
			}

			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setMaxRighe(maxRighe);
			areaDatiPassReturn.setTotRighe(totRighe);
			areaDatiPassReturn.setNumBlocco(1);
			areaDatiPassReturn.setNumNotizie(totRighe);
			areaDatiPassReturn.setTotBlocchi(numBlocchi);
			areaDatiPassReturn.setListaSintetica(listaSintentica);

		} catch (IllegalArgumentException ie) {
		} catch (Exception e) {
			System.out.println("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}

	/**
	 * Effettua il confronto fra ciascuno degli elementi di 2 array di Stringhe
	 * e restituisce un List di Citazioni Standard.
	 *
	 * @param vecchio
	 *            String[]
	 * @param nuovo
	 *            String[]
	 * @return citazioniStandard List
	 */
	public List confrontoArrayCitazioni(String[] vecchieCitazioni,
			String[] nuoveCitazioni) {

		List citazioniStandard = new ArrayList();
		statoOldCitazioni = new ArrayList();

		int pos = 0;

		// CONFRONTO DI CIASCUNA DELLE NUOVE CITAZIONI CON
		// CIASCUNA DELLE VECCHIE CITAZIONI
		for (int i = 0; i < nuoveCitazioni.length; i = i + 2) {
			boolean inserimento = false;
			boolean invariato = false;

			if (vecchieCitazioni.length > 0) {
				// Vecchie citazioni presenti quindi c'è
				// la possibilità di effettuare il confronto
				for (int j = 0; j < vecchieCitazioni.length; j = j + 2) {
					if (!nuoveCitazioni[i].equals(vecchieCitazioni[j])) {
						inserimento = true;
						pos++;
					} else if (nuoveCitazioni[i + 1]
							.equals(vecchieCitazioni[j + 1])) {
						invariato = true;
						pos++;
					} else {
						inserimento = true;
						pos++;
					}
				}// end for interno
			} else {
				// QUESTA SITUAZIONE NON DOVREBBE MAI VERIFICARSI
				// IN QUANTO OGNI MARCA DEVE NECESSARIAMENTE AVERE
				// ALMENO UNA CITAZIONE STANDARD.

				// Vecchie citazioni assenti (base dati corrotta).
				// Non c'è alcun termine di paragone quindi le
				// nuove Citazioni sono tutti INSERIMENTI.
				inserimento = true;
				pos++;
			}

			int citazione = Integer.parseInt(nuoveCitazioni[i + 1]);
			if (invariato) {
				citazioniStandard.add(new CitazioneStandard(nuoveCitazioni[i],
						citazione, INVARIATO));
			} else if (inserimento) {
				citazioniStandard.add(new CitazioneStandard(nuoveCitazioni[i],
						citazione, INSERIMENTO));
			}
		}// end for esterno

		// CONFRONTO DI CIASCUNA DELLE VECCHIE CITAZIONI CON
		// CIASCUNA DELLE NUOVE CITAZIONI
		for (int i = 0; i < vecchieCitazioni.length; i = i + 2) {
			boolean cancellazione = false;
			boolean invariato = false;

			for (int j = 0; j < nuoveCitazioni.length; j = j + 2) {
				if (!vecchieCitazioni[i].equals(nuoveCitazioni[j])) {
					cancellazione = true;
					pos++;
				} else if (vecchieCitazioni[i + 1]
						.equals(nuoveCitazioni[j + 1])) {
					invariato = true;
					pos++;
				} else {
					cancellazione = true;
					pos++;
				}
			}// end for interno

			int citazione = Integer.parseInt(vecchieCitazioni[i + 1]);
			if (invariato) {
				statoOldCitazioni.add(new CitazioneStandard(
						vecchieCitazioni[i], citazione, INVARIATO));
			} else if (cancellazione) {
				statoOldCitazioni.add(new CitazioneStandard(
						vecchieCitazioni[i], citazione, CANCELLAZIONE));
			}
		}// end for esterno

		return citazioniStandard;

	}// end confrontoArrayCitazioni

	/**
	 * Prende in input due vector contenenti rispettivamente le nuove e le
	 * vecchie citazioni standard. Controlla lo stato di ciascuna di esse e
	 * restituisce un nuovo List con tutte le citazioni standard su cui
	 * operare in inserimento o in cancellazione, tralasciando quelle rimaste
	 * invariate.
	 *
	 * @param statoNewCitazioni
	 *            List con le citazioni inserite
	 * @param statoOldCitazioni
	 *            List con le vecchie citazioni
	 * @return vectorOperazioni List con le citazioni su cui operare
	 */
	public List impostaListOperazioniLegami(List statoNewCitazioni,
			List statoOldCitazioni) {

		List vectorOperazioni = new ArrayList();
		// NUOVE CITAZIONI
		for (int i = 0; i < statoNewCitazioni.size(); i++) {
			CitazioneStandard objCitazione = (CitazioneStandard) statoNewCitazioni
					.get(i);
			if (!objCitazione.getStato().equals(INVARIATO)) {
				vectorOperazioni.add(objCitazione);
			}
		}
		// VECCHIE CITAZIONI
		for (int i = 0; i < statoOldCitazioni.size(); i++) {
			CitazioneStandard objCitazioneOld = (CitazioneStandard) statoOldCitazioni
					.get(i);
			if (!objCitazioneOld.getStato().equals(INVARIATO)) {
				vectorOperazioni.add(objCitazioneOld);
			}
		}

		return vectorOperazioni;
	}// end impostaListOperazioniLegami

	/**
	 * Per la variazione di una Marca. Imposta i legami a Repertorio della Marca
	 * da variare.
	 *
	 * @param arrayLegamiType
	 *            LegamiType[]
	 * @param vectorOperazioni
	 *            List
	 * @param mid
	 *            String
	 */
	public void setLegamiCitazioniPerVariazione(LegamiType[] arrayLegamiType,
			List vectorOperazioni, String mid) {

		// creo i legami per ogni repertorio (ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		int count = arrayLegamiType.length;

		for (int i = 0; i < count; i++) {
			legamiType = new LegamiType();

			legamiType.setIdPartenza(mid);

			CitazioneStandard objCitazione = (CitazioneStandard) vectorOperazioni
					.get(i);
			String stato = objCitazione.getStato();

			// 2 soli casi: INSERIMENTO O CANCELLAZIONE.
			if (stato.equals(INSERIMENTO)) {
				legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
			} else {
				legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);
			}

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame = new ArrivoLegame();

			// Tipo Authority
			legameElementoAut = new LegameElementoAutType();
			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// Tipo Legame
			String tipoLegame = IID_ESITO_POSITIVO;
			UtilityCastor utilityCastor = new UtilityCastor();
			legameElementoAut.setTipoLegame(utilityCastor
					.codificaLegameRepertorio(tipoLegame));

			// Sigla del Repertorio
			String sigla = objCitazione.getSigla();
			legameElementoAut.setIdArrivo(sigla.trim());

			// Citazione
			int citazione = objCitazione.getCitazione();
			legameElementoAut.setCitazione(citazione);

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[i] = legamiType;

		}// end for
	}// end setLegamiCitazioniPerVariazione



	public TreeElementViewTitoli getReticoloMarca(SBNMarc sbnMarcType,
			TreeElementViewTitoli root, String chiamata,
			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		myChiamata = chiamata;

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		setKeyCtr = 1;
		codSbnBiblioteca = areaDatiPassLocal.getCodiceSbn();

		MarcaType marcaType = null;

		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		marcaType = (MarcaType) datiElemento;
		A921 a921 = marcaType.getT921();

		// prendo il MID, Descrizione
		String midRadice = datiElemento.getT001();
		String descrizione = a921.getA_921();

		// CITAZIONE STANDARD
		// Se la marca ha legami a repertori, le citazioni standard
		// verranno aggiunte al nodo tra MID e Descrizione
		String citazioneStandardString = "";

		root.setRigaReticoloCtr(setKeyCtr++);
		root.setKey(midRadice);
		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
		root.setT005(datiElemento.getT005());
		root.setLivelloAutorita(datiElemento.getLivelloAut().toString());
		if (myChiamata.equals("P")) {
			root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			 if (datiElemento.getCondiviso() == null){
				 root.setFlagCondiviso(true);
	            } else {
	            	 if (datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
	            		 root.setFlagCondiviso(false);
	     			} else {
	     				root.setFlagCondiviso(true);
	     			}
	            }
		} else {
			root.setFlagCondiviso(true);
			int oggettoLocalizzato = 0;

			// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
			// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
			oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
			if (datiElemento.getSbnLocaliz() != null) {
				String localizzazione = datiElemento.getSbnLocaliz();
				if (localizzazione.equals("bib-gp")) {
					oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA;
				} else if (localizzazione.equals("bib-g")) {
					oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE;
				} else if (localizzazione.equals("bib-p")) {
					oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO;
				} else if (localizzazione.equals("polo")) {
					oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
				}
			} else {
				areaDatiPassLocal.setIndice(true);
				areaDatiPassLocal.setPolo(false);
				areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
				areaDatiPassLocal.setIdLoc(midRadice);
				areaDatiPassLocal.setAuthority("MA");
				oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
			}
			// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

			root.setLocalizzazione(oggettoLocalizzato);
		}

//		root.setText(midRadice + " " + descrizione);
		root.setText(descrizione);
		root.setDescription(descrizione);
		root.setTipoAuthority(SbnAuthority.MA);// root.setTipoAuthority(null);

		// Inizio inserimento della gestione del dettaglio Marca
		DettaglioOggetti dettOggetti = new DettaglioOggetti();
		root.getAreaDatiDettaglioOggettiVO().setDettaglioMarcaGeneraleVO(
				dettOggetti.getDettaglioMarca(sbnMarcType, root, "", 0));
		// Fine inserimento della gestione del dettaglio Marca

		root.setIdNode(0);
		root.open();

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

			//TreeElementViewTitoli nodoCorrente = (TreeElementViewTitoli) root.addChild();
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				int tipoLegame = sbnLegameAut.getType();
				String tipoLegameString = sbnLegameAut.toString();

				// REPERTORI LEGATI ALLA MARCA
				// tipo legame 810 , tipo legame 815 REPERTORI
				if (!(tipoLegameString.equals(this.AUT_TIPO_LEGAME_14))
						&& (!tipoLegameString.equals(this.AUT_TIPO_LEGAME_15))) {

					TreeElementViewTitoli nodoCorrente = (TreeElementViewTitoli) root.addChild();
					ElementAutType elementAutType = legameElemento.getElementoAutLegato();
					String VIDLegato;
					String valueDelNodo;
					String nominativoLegame;

					if (elementAutType != null) {

						if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {
							AutorePersonaleType autorePersonale = (AutorePersonaleType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(autorePersonale);
							VIDLegato = autorePersonale.getT001();
							valueDelNodo = nominativoLegame;

							nodoCorrente.setKey(VIDLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(autorePersonale.getT005());
							nodoCorrente.setLivelloAutorita(autorePersonale.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (autorePersonale.getCondiviso() == null){
									nodoCorrente.setFlagCondiviso(true);
						            } else {
						            	if (autorePersonale.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
											nodoCorrente.setFlagCondiviso(false);
										} else {
											nodoCorrente.setFlagCondiviso(true);
										}
						            }
							} else {
								nodoCorrente.setFlagCondiviso(true);
								int oggettoLocalizzato = 0;

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (autorePersonale.getSbnLocaliz() != null) {
									String localizzazione = autorePersonale.getSbnLocaliz();
									if (localizzazione.equals("bib-gp")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA;
									} else if (localizzazione.equals("bib-g")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE;
									} else if (localizzazione.equals("bib-p")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO;
									} else if (localizzazione.equals("polo")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
									}
								} else {
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(VIDLegato);
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
							}

							nodoCorrente.setText(valueDelNodo);
							nodoCorrente.setDescription(nominativoLegame);
							nodoCorrente.setTipoAuthority(SbnAuthority.AU);
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));

							// Inizio inserimento della gestione del dettaglio
							// Autore
							if (root.getTipoAuthority() != null) {
								if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
									tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
								} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
									tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
								}
							}
							nodoCorrente.getAreaDatiDettaglioOggettiVO()
									.setDettaglioAutoreGeneraleVO(
											dettOggetti.getDettaglioAutore(
													sbnMarcType, null, nodoCorrente,
													"", tipoLegame));
							// Fine inserimento della gestione del dettaglio
							// Autore

						} else if (elementAutType.getDatiElementoAut() instanceof EnteType) {
							EnteType enteType = (EnteType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(enteType);
							VIDLegato = enteType.getT001();
							valueDelNodo = nominativoLegame;

							nodoCorrente.setKey(VIDLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(enteType.getT005());
							nodoCorrente.setLivelloAutorita(enteType.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

								if (enteType.getCondiviso() == null){
									nodoCorrente.setFlagCondiviso(true);
						            } else {
						            	if (enteType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
											nodoCorrente.setFlagCondiviso(false);
										} else {
											nodoCorrente.setFlagCondiviso(true);
										}
						            }
							} else {
								nodoCorrente.setFlagCondiviso(true);
								int oggettoLocalizzato = 0;

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (enteType.getSbnLocaliz() != null) {
									String localizzazione = enteType.getSbnLocaliz();
									if (localizzazione.equals("bib-gp")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA;
									} else if (localizzazione.equals("bib-g")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE;
									} else if (localizzazione.equals("bib-p")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO;
									} else if (localizzazione.equals("polo")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
									}
								} else {
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(VIDLegato);
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
							}
							nodoCorrente.setText(valueDelNodo);
							nodoCorrente.setDescription(nominativoLegame);
							nodoCorrente.setTipoAuthority(SbnAuthority.AU);
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));

							// Inizio inserimento della gestione del dettaglio
							// Autore
							if (root.getTipoAuthority() != null) {
								if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
									tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
								} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
									tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
								}
							}
							nodoCorrente.getAreaDatiDettaglioOggettiVO()
									.setDettaglioAutoreGeneraleVO(
											dettOggetti.getDettaglioAutore(
													sbnMarcType, null, nodoCorrente,
													"", tipoLegame));
							// Fine inserimento della gestione del dettaglio
							// Autore

							// almaviva2 15.12.2009 - BUG MANTIS 3435  inserita chiamata a impostaNodoLegame4XX -
							// SE L'ENTE TROVATO HA ALTRI LEGAMI, QUESTI VENGONO AGGIUNTI AL NODO SOLO SE SONO DI TIPO "4XX".
							//impostaNodoLegame4XX(elementAutType, root, number);
							SbnGestioneAutoriDao gestioneAutoriDao = new SbnGestioneAutoriDao(indice, polo, user);
							gestioneAutoriDao.impostaNodoLegame4XX(elementAutType, nodoCorrente, 0, myChiamata, codSbnBiblioteca);



						} else if (elementAutType.getDatiElementoAut() instanceof MarcaType) {
							marcaType = (MarcaType) elementAutType
									.getDatiElementoAut();
							nominativoLegame = marcaType.getT921().getA_921();
							String MIDLegato = marcaType.getT001();
							valueDelNodo = nominativoLegame;

							nodoCorrente.setKey(MIDLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(marcaType.getT005());
							nodoCorrente.setLivelloAutorita(marcaType.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente
										.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);

								if (marcaType.getCondiviso() == null){
									nodoCorrente.setFlagCondiviso(true);
						            } else {
						            	if (marcaType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
											nodoCorrente.setFlagCondiviso(false);
										} else {
											nodoCorrente.setFlagCondiviso(true);
										}
						            }



							} else {
								nodoCorrente.setFlagCondiviso(true);
								int oggettoLocalizzato = 0;

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (marcaType.getSbnLocaliz() != null) {
									String localizzazione = marcaType.getSbnLocaliz();
									if (localizzazione.equals("bib-gp")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA;
									} else if (localizzazione.equals("bib-g")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE;
									} else if (localizzazione.equals("bib-p")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO;
									} else if (localizzazione.equals("polo")) {
										oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
									}
								} else {
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(MIDLegato);
									areaDatiPassLocal.setAuthority("MA");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
							}
							nodoCorrente.setText(valueDelNodo);
							nodoCorrente.setDescription(nominativoLegame);
							nodoCorrente.setTipoAuthority(SbnAuthority.MA);
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));
							tipoLegame = DatiLegame.LEGAME_AUTORE_MARCA;

							nodoCorrente.getAreaDatiDettaglioOggettiVO()
									.setDettaglioMarcaGeneraleVO(
											dettOggetti.getDettaglioMarca(
													sbnMarcType, nodoCorrente,
													"", tipoLegame));
						}

						nodoCorrente.setIdNode(root.getIdNode() + 1);
					}
				} else {
					String siglaRepertorioLegato = legameElemento.getIdArrivo();
					int citazioneLegata = legameElemento.getCitazione();
					String citazioneStandard = siglaRepertorioLegato + citazioneLegata;
					citazioneStandardString = citazioneStandardString + citazioneStandard;
					if (j != (legamiType.getArrivoLegameCount() - 1)) {
						citazioneStandardString = citazioneStandardString + ", ";
					}
				}// end else

			}// end for interno
		}// end for esterno

		return root;
	}// end setReticolo

}
// end class XMLMarche
