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
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityAutori;
import it.iccu.sbn.ejb.model.unimarcmodel.A260;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAutoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaLuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.SinteticaLuoghiView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: SbnWEB
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
 * Funzioni per la creazione e parsing di alberi dom castor relativi alle
 * interrogazioni sugli Autori. Utilizza la classe XMLFactory per scambiare
 * flussi XML con il server sbn, il formato dei flussi XML scambiati rispetta lo
 * schema XSD del protocollo SBN-MARC, tale schema è rappresentato mediante un
 * object model generato con Castor. Le classi che realizzano i frame ed i
 * pannelli dell'interfaccia grafica per l'area autori utilizzano XMLAutori per
 * effettuare interrogazioni e modifiche sui dati degli autori mediante il
 * protocollo SBN-MARcercaAutoreType.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Finsiel
 * @version 1.0
 */
public class SbnGestioneLuoghiDao {

	private FactorySbn indice;

	private FactorySbn polo;

	private SbnUserType user;

	public SbnGestioneLuoghiDao() {
		super();

	}

	public SbnGestioneLuoghiDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public static final String IID_STRINGAVUOTA = "";

	private String myChiamata;

	private int setKeyCtr = -1;

	private String codSbnBiblioteca = "";

	private final String AUT_TIPO_LEGAME_14 = "810";
	private final String AUT_TIPO_LEGAME_15 = "815";
	private static String IID_SPAZIO = " ";

	Map tabellaTimeStamp = new HashMap();

	String[] listaInfDaCatt;

	UtilityCastor utilityCastor = new UtilityCastor();

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLuoghi(
			AreaDatiPassaggioInterrogazioneLuogoVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		if (!areaDatiPass.isRicercaPolo() && !areaDatiPass.isRicercaIndice()) {
			areaDatiPassReturn.setCodErr("livRicObblig");
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}
		String esito = "0000";
		int contaCanali = 0;

		if (areaDatiPass.getInterrGener().getDenominazione() != null) {
			if (!areaDatiPass.getInterrGener().getDenominazione().equals("")) {
				contaCanali = contaCanali + 1;
			}
		} else {
			areaDatiPass.getInterrGener().setDenominazione("");
		}
		if (areaDatiPass.getInterrGener().getLid() != null) {
			if (!areaDatiPass.getInterrGener().getLid().equals("")) {
				contaCanali = contaCanali + 1;
			}
		} else {
			areaDatiPass.getInterrGener().setLid("");
		}

		if (contaCanali == 0) {
			esito = "noCanPrim";
			areaDatiPassReturn.setCodErr(esito);
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}
		if (contaCanali > 1) {
			esito = "soloUnCanPrim";
			areaDatiPassReturn.setCodErr(esito);
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}

		if (areaDatiPass.getInterrGener().getPaeseSelez() == null) {
			areaDatiPass.getInterrGener().setPaeseSelez("");
		}

		if (!areaDatiPass.getInterrGener().getPaeseSelez().equals("")
				&& areaDatiPass.getInterrGener().getDenominazione().equals("")) {
			esito = "noCanPrim";
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
			cercaType.setMaxRighe(areaDatiPass.getInterrGener().getMaxRighe());

			if (areaDatiPass.getInterrGener().getTipoOrdinamSelez() == null) {
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			} else if (areaDatiPass.getInterrGener().getTipoOrdinamSelez()
					.equals("")) {
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			} else {
				cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass
						.getInterrGener().getTipoOrdinamSelez()));
			}

			// NUMERI BLOCCO
			if (areaDatiPass.getInterrGener().getElemXBlocchi() != 0) {
				cercaType.setMaxRighe(areaDatiPass.getInterrGener()
						.getElemXBlocchi());
			} else {
				cercaType.setMaxRighe(10);
			}

			cercaType.setCercaElementoAut(cercaElemento);
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
			CercaLuogoType cercaLuogoType = new CercaLuogoType();

			StringaCercaType s = new StringaCercaType();

			StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();

			// Nominativo
			if (!areaDatiPass.getInterrGener().getDenominazione().equals("")) {
				if (areaDatiPass.getInterrGener().getTipoRicerca().equals(
						"inizio")) {
					stringaTypeChoice.setStringaLike(areaDatiPass
							.getInterrGener().getDenominazione());
					s.setStringaCercaTypeChoice(stringaTypeChoice);
					canali.setStringaCerca(s);
					cercaLuogoType.setCanaliCercaDatiAut(canali);
				} else if (areaDatiPass.getInterrGener().getTipoRicerca()
						.equals("intero")) {
					stringaTypeChoice.setStringaEsatta(areaDatiPass
							.getInterrGener().getDenominazione());
					s.setStringaCercaTypeChoice(stringaTypeChoice);
					canali.setStringaCerca(s);
					cercaLuogoType.setCanaliCercaDatiAut(canali);
				}
			} else if (!areaDatiPass.getInterrGener().getLid().equals("")) {
				// LID
				canali.setT001(areaDatiPass.getInterrGener().getLid()
						.toUpperCase());
				cercaLuogoType.setCanaliCercaDatiAut(canali);
			}

			if (!areaDatiPass.getInterrGener().getPaeseSelez().equals("")) {
				cercaLuogoType.setA_260(areaDatiPass.getInterrGener().getPaeseSelez());
			}

			// tipo authority
			cercaLuogoType.setTipoAuthority(SbnAuthority.LU);

			cercaElemento.setCercaDatiAut(cercaLuogoType);

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
							.getTestoEsito());
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
									.getSbnResult().getTestoEsito());
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

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}


	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoLuoghi(
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
			CercaAutoreType cercaAutoreType = new CercaAutoreType();

			cercaAutoreType.setTipoAuthority(SbnAuthority.LU);
			cercaElemento.setCercaDatiAut(cercaAutoreType);

			// tipoOutput sintetica
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

			this.indice.setMessage(sbnmessage, this.user);
			this.polo.setMessage(sbnmessage, this.user);

			if (areaDatiPass.isRicercaPolo()) {
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}
			if (areaDatiPass.isRicercaIndice()) {
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

			// /////////////
			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSinteticaSuccessiva = new ArrayList<SinteticaLuoghiView>();

			int tappoRicerca = maxRighe;
			if ((totRighe - numPrimo + 1) < maxRighe) {
				tappoRicerca = (totRighe - numPrimo + 1);
			}

			for (int t = 0; t < tappoRicerca; t++) {
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t, numPrimo);
				listaSinteticaSuccessiva.add(sinteticaLuoghiView);
				++numPrimo;
			}

			idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			areaDatiPassReturn.setIdLista(idLista);
			areaDatiPassReturn.setNumPrimo(numPrimo);

			areaDatiPassReturn.setListaSintetica(listaSinteticaSuccessiva);

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;
	}

	public SinteticaLuoghiView creaElementoListaLuoghi(SbnOutputType sbnOutPut,
			int elementIndex, int progressivo) {

		ElementAutType elementAutType = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElementoType = elementAutType.getDatiElementoAut();
		LuogoType luogoType = (LuogoType) datiElementoType;

		// List contenente la nuova riga alla tabella
		SinteticaLuoghiView data = new SinteticaLuoghiView();

		// VOCE: icona della prima colonna della tabella
		data.setImageUrl("blank.png");

		// PROGRESSIVO: Numero dell'elemento
		data.setProgressivo(progressivo);

		// ID: Identificativo dell'elemento
		data.setLid(luogoType.getT001().toString());

		if (luogoType.getCondiviso() == null ){
        	data.setFlagCondiviso(true);
		} else {
			if (luogoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
	        	data.setFlagCondiviso(false);
			} else {
				data.setFlagCondiviso(true);
			}
		}
		// Luogo
		A260 a260 = new A260();
		a260 = luogoType.getT260();
		data.setDenominazione(a260.getD_260().toString());

		// FORMA
		data.setForma(luogoType.getFormaNome().toString());

		// Inizio Modifica almaviva2 16 luglio 2009 BUG 3076 manca la gestione della seconda riga in sintetica con
		// il link alla sua forma accettata

		String LIDLegato = "";
		String nominativoLegato = "";
		String tipoNomeLegato = "";

		if (luogoType.getFormaNome().getType() == SbnFormaNome.A_TYPE) {
			LIDLegato = luogoType.getT001();
			data.setForma("A");
		} else {
			data.setForma("R");
			if (elementAutType.getLegamiElementoAutCount() > 0) {
				String nominativoFormaRinvio = a260.getD_260().toString();
				// il luogo trovato è una forma di rinvio allora devo mettere la sua forma accettata sotto di questo nella riga
				// della sintetica PRENDO IL LEGAME DOVREBBE avere la sua forma accettata
				LegamiType legamiType = elementAutType.getLegamiElementoAut(0);
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(0);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

				ElementAutType elementAutTypeLegato = legameElemento.getElementoAutLegato();
				DatiElementoType datiElementoLegato = elementAutTypeLegato.getDatiElementoAut();


				// LEGAMI
				nominativoLegato = utilityCastor.getNominativoDatiElemento(datiElementoLegato);
				LIDLegato = datiElementoLegato.getT001();
				tipoNomeLegato = utilityCastor.getTipoNomeDatiElemento(datiElementoLegato);

				data.setDenominazione(new String(nominativoFormaRinvio + "<br />--> "
						+ LIDLegato + " " + nominativoLegato));
			}
		}
		// Fine Modifica almaviva2 16 luglio 2009 BUG 3076

		// Inizio Modifica almaviva2 BUG MANTIS 4137 (Collaudo)
		//l'esamina su forme di rinvio deve essere fatto con la sua forma accettata (deve essere valorizzata anche la sintetica
		data.setLidAccettata(LIDLegato);
		data.setLuogoAccettata(nominativoLegato);
		data.setTipoLuogoAccettata(tipoNomeLegato);



		// LIVELLO AUTORITY
		data.setLivelloAutorita(luogoType.getLivelloAut().toString());

		return data;
	}


	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoLuoghiPerLid(
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

			// Lid
			canali.setT001(areaDatiPass.getBidRicerca());

			CercaLuogoType cercaLuogoType = new CercaLuogoType();
			cercaLuogoType.setCanaliCercaDatiAut(canali);
			cercaLuogoType.setTipoAuthority(SbnAuthority.LU);
			cercaElemento.setCercaDatiAut(cercaLuogoType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
			String chiamata = IID_STRINGAVUOTA;
			if (areaDatiPass.isRicercaPolo()) {
				areaDatiPassLocal.setIndice(false);
				areaDatiPassLocal.setPolo(true);
				areaDatiPassLocal.setCodiceSbn(this.user.getBiblioteca());
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
			root = this.getReticoloLuogo(sbnRisposta,
					root, chiamata, areaDatiPassLocal);
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

	public TreeElementViewTitoli getReticoloLuogo(SBNMarc sbnMarcType,
			TreeElementViewTitoli root, String chiamata,
			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal) {

		UtilityCastor utilityCastor = new UtilityCastor();
		myChiamata = chiamata;

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		setKeyCtr = 1;
		codSbnBiblioteca = areaDatiPassLocal.getCodiceSbn();

		LuogoType luogoType = null;

		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		luogoType = (LuogoType) datiElemento;

		// prendo il MID, Descrizione
		String lidRadice = luogoType.getT001();
		String descrizione = luogoType.getT260().getD_260().toString();

		root.setRigaReticoloCtr(setKeyCtr++);
		root.setKey(lidRadice);
		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
		root.setT005(luogoType.getT005());
		root.setLivelloAutorita(luogoType.getLivelloAut().toString());
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		if (myChiamata.equals("P")) {
			root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			if (luogoType.getCondiviso() == null ){
				root.setFlagCondiviso(true);
			} else if (luogoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
				root.setFlagCondiviso(false);
			} else {
				root.setFlagCondiviso(true);
			}
		} else {
			root.setFlagCondiviso(true);
			// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice in occasione di tale intervento viene eliminata
			// la ricerca localizzazione in Indice in quanto non esistente e sostituita con l'impostazione fissa del
			// valore LIV_DI_RICERCA_INDICE;
//			int oggettoLocalizzato = 0;
//			areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
//			areaDatiPassLocal.setIdLoc(lidRadice);
//			areaDatiPassLocal.setAuthority("LU");
//			oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
			root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
			// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice
		}

		//root.setText(lidRadice + " " + descrizione);
		root.setText(descrizione);
		root.setDescription(descrizione);
		root.setTipoAuthority(SbnAuthority.LU);// root.setTipoAuthority(null);

		// Inizio inserimento della gestione del dettaglio Luogo
		DettaglioOggetti dettOggetti = new DettaglioOggetti();
		root.getAreaDatiDettaglioOggettiVO().setDettaglioLuogoGeneraleVO(
				dettOggetti.getDettaglioLuogo(sbnMarcType, root, "", 0));
		// Fine inserimento della gestione del dettaglio Luogo

		root.setIdNode(0);
		root.open();

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

				// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
				// di gestire i campi nota informativa , nota catalogatore e legame a repertori
				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				int tipoLegame = sbnLegameAut.getType();
				String tipoLegameString = sbnLegameAut.toString();
				// ESCLUDO REPERTORI LEGATI ALL'AUTORE tipo legame 810 , tipo
				// legame 815 REPERTORI
				if (!(tipoLegameString.equals(AUT_TIPO_LEGAME_14))&& (!tipoLegameString.equals(AUT_TIPO_LEGAME_15))) {

					TreeElementViewTitoli nodoCorrente = (TreeElementViewTitoli) root.addChild();
					ElementAutType elementAutType = legameElemento.getElementoAutLegato();

					if (elementAutType != null) {

						if (elementAutType.getDatiElementoAut() instanceof LuogoType) {
							luogoType = (LuogoType) elementAutType.getDatiElementoAut();

							String nominativoLegame = luogoType.getT260().getD_260().toString();
							String valueDelNodo = "";
							valueDelNodo = nominativoLegame;

							nodoCorrente.setRigaReticoloCtr(setKeyCtr++);
							nodoCorrente.setKey(luogoType.getT001());
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(luogoType.getT005());
							nodoCorrente.setLivelloAutorita(luogoType.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								// if (luogoType.getInfoLocBibCount() > 0) {
								// nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
								// } else {
								// nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								// }
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
								if (luogoType.getCondiviso() == null ){
									nodoCorrente.setFlagCondiviso(true);
								} else if (luogoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodoCorrente.setFlagCondiviso(false);
								} else {
									nodoCorrente.setFlagCondiviso(true);
								}
							} else {
								nodoCorrente.setFlagCondiviso(true);
								nodoCorrente.setLocalizzazione(0);
							}

							nodoCorrente.setText(valueDelNodo);
							nodoCorrente.setDescription(nominativoLegame);
							nodoCorrente.setTipoAuthority(SbnAuthority.LU);
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));

							String livello = elementAutType.getDatiElementoAut().getLivelloAut().toString();
							nodoCorrente.setLivelloAutorita(livello);


							SbnFormaNome sbnForma = luogoType.getFormaNome();

							if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
								// METTO NEL VECTOR IL VID DELLA FORMA ACCETTATA
							} else {
								nodoCorrente.setLuogoFormaRinvio(true);
							}

							nodoCorrente.setIdNode(root.getIdNode() + 1);
							// Inizio inseriemnto della gestione dettaglio luogo
							tipoLegame = DatiLegame.LEGAME_LUOGO_LUOGO;

							nodoCorrente.getAreaDatiDettaglioOggettiVO()
									.setDettaglioLuogoGeneraleVO(dettOggetti.getDettaglioLuogo(sbnMarcType, nodoCorrente, "", tipoLegame));
							// Fine inserimento della gestione del dettaglio
							// Luogo
						}
					}

				}




			}// end for interno
		}// end for esterno

		return root;
	}// end setReticolo





	public AreaDatiVariazioneReturnVO inserisciLuogo(
			AreaDatiVariazioneLuogoVO areaDatiPass) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
		SbnGestioneAutoriDao gestioneAutori = new SbnGestioneAutoriDao(indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
		// nota informativa , nota catalogatore e legame a repertori
		UtilityAutori utilityAutori = new UtilityAutori();
		if (!utilityAutori.isOkControlloRepertoriAutore(areaDatiPass
				.getDettLuogoVO().getListaRepertoriNew())) {
			areaDatiPassReturn.setCodErr("ins038");
			return areaDatiPassReturn;
		}

		// Controllo sui campi inseriti verificare quali inserire
		SBNMarc sbnRisposta = null;
//		try {

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

			SbnSimile appoSimile = SbnSimile.SIMILE;
			if (areaDatiPass.isConferma()) {
				appoSimile = SbnSimile.CONFERMA;
			}

			if (!areaDatiPass.isModifica()) {
				crea.setTipoControllo(appoSimile);
			} else {
				modifica.setTipoControllo(appoSimile);
			}

			LuogoType luogoType = new LuogoType();

			ElementAutType elementAutType = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();
			A260 a260 = new A260();

			datiElemento.setTipoAuthority(SbnAuthority.LU);
			luogoType.setTipoAuthority(SbnAuthority.LU);

			String livello = areaDatiPass.getDettLuogoVO().getLivAut();
			if (areaDatiPass.isInserimentoIndice()) {
				if (Integer.valueOf(livello) < 5) {
					areaDatiPassReturn.setCodErr("livAutInfMinimoIndice");
					return areaDatiPassReturn;
				}
			}

			luogoType.setLivelloAut(SbnLivello.valueOf(areaDatiPass.getDettLuogoVO().getLivAut()));
			luogoType.setFormaNome(SbnFormaNome.valueOf(areaDatiPass.getDettLuogoVO().getForma()));


			// BUG mantis 5270
			if (areaDatiPass.getDettLuogoVO().getPaese().length() > 0) {
				a260.setA_260(areaDatiPass.getDettLuogoVO().getPaese());
			} else {
				a260.setA_260(null);
			}

			a260.setD_260(areaDatiPass.getDettLuogoVO().getDenomLuogo());
			luogoType.setT260(a260);

			// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
			// nota informativa , nota catalogatore e legame a repertori
			// NOTA DEL CATALOGATORE
			String notaDelCatal = areaDatiPass.getDettLuogoVO().getNotaCatalogatore();
			A830 a830 = new A830();
			a830.setA_830(notaDelCatal);
			luogoType.setT830(a830);
			// NOTA INFORMATIVA
			String notaAlNome = areaDatiPass.getDettLuogoVO().getNotaInformativa();
			A300 a300 = new A300();
			if (IID_STRINGAVUOTA.equals(notaAlNome))
				notaAlNome = IID_SPAZIO;
			if (!notaAlNome.equals(IID_STRINGAVUOTA)) {
				a300.setA_300(notaAlNome);
				luogoType.setT300(a300);
			}

			// REPERTORI
			List listaRepertoriOld = areaDatiPass.getDettLuogoVO().getListaRepertori();
			List listaRepertoriNew = areaDatiPass.getDettLuogoVO().getListaRepertoriNew();

			// totale dei repertori con le modifiche
			int countOld = listaRepertoriOld.size();
			int countNew = listaRepertoriNew.size();
			LegamiType[] arrayLegamiType = new LegamiType[countOld + countNew];



			if (areaDatiPass.isModifica()) {
				gestioneAutori.eliminaAllRepertori(arrayLegamiType, listaRepertoriOld,	areaDatiPass.getDettLuogoVO().getLid());
				gestioneAutori.addNewRepertori(arrayLegamiType, listaRepertoriNew,	areaDatiPass.getDettLuogoVO().getLid(), countOld);
			} else {
				if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
					// nel caso di cataloga in Indice i repertori sono contenuti nell'Old e non nel New
					gestioneAutori.addOldRepertori(arrayLegamiType, listaRepertoriOld,	areaDatiPass.getDettLuogoVO().getLid(), 0);
				} else {
					gestioneAutori.addNewRepertori(arrayLegamiType, listaRepertoriNew,	areaDatiPass.getDettLuogoVO().getLid(), 0);
				}

			}

			// Se è presente almeno un legame (tra vecchi e
			// nuovi) aggiungo l'array dei legami all'ElementAut.
			if ((countOld > 0) || (countNew > 0)) {
				elementAutType.setLegamiElementoAut(arrayLegamiType);
			}


			if (areaDatiPass.isFlagCondiviso()) {
				luogoType.setCondiviso(DatiElementoTypeCondivisoType.S);
			} else {
				luogoType.setCondiviso(DatiElementoTypeCondivisoType.N);
			}


			if (!areaDatiPass.isModifica()) {
				if (areaDatiPass.isConferma()
						&& (areaDatiPass.getBidTemporaneo() != null && !areaDatiPass.getBidTemporaneo().equals(""))) {
					luogoType.setT001(areaDatiPass.getBidTemporaneo());
					areaDatiPass.setBidTemporaneo("");
				} else {
					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					areaDatiPassGetIdSbn.setTipoAut("LU");
					areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
					if (areaDatiPassGetIdSbn.getIdSbn() == null
							|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
						areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					luogoType.setT001(areaDatiPassGetIdSbn.getIdSbn());
				}
			} else {
				luogoType.setT001(areaDatiPass.getDettLuogoVO().getLid());
			}

			datiElemento = luogoType;

			// DATA VARIAZIONE
			if (areaDatiPass.isModifica()) {
				datiElemento.setT005(areaDatiPass.getDettLuogoVO().getVersione());
				if (areaDatiPass.isVariazione()) {
					datiElemento.setStatoRecord(StatoRecord.C);
				}
			}

			elementAutType.setDatiElementoAut(datiElemento);

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

			// almaviva2 17.03.2009 Inizio nuova gestione luoghi in modo da consentirne la catalogazione locale e poi la condivisione

			areaDatiPass.setPrimoBloccoSimili(true);

			// ==================================================================

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

			// almaviva2 17.03.2009 Fine   nuova gestione luoghi in modo da consentirne la catalogazione locale e poi la condivisione

		return areaDatiPassReturn;
	}// end creaMarca



	// almaviva2 17.03.2009 Inizio nuova gestione luoghi in modo da consentirne la catalogazione locale e poi la condivisione
	private AreaDatiVariazioneReturnVO chiamataInsertIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneLuogoVO areaDatiPass) {

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
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
				!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		UtilityCastor utilityCastor = new UtilityCastor();
		if (!utilityCastor.isElementiSimiliTrovati(sbnRisposta) || areaDatiPass.isConferma()) {
			// operazione andata a buon fine; in caso di Inserimento nuovo
			// autore si invia la chiamata per localizzazione in Indice dell'Autore

//				AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//				areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage()
//						.getSbnResponse().getSbnResponseTypeChoice()
//						.getSbnOutput().getElementoAut(0)
//						.getDatiElementoAut().getT001());
//				areaLocalizza.setAuthority("LU");
//				areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
//				areaLocalizza.setTipoOpe("Localizza");
//				areaLocalizza.setTipoLoc("Gestione");
//				areaLocalizza.setIndice(true);
//				areaLocalizza.setPolo(false);
//
//				AreaDatiVariazioneReturnVO areaDatiPassReturnLocalizza = new AreaDatiVariazioneReturnVO();
//
//				SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
//				areaDatiPassReturnLocalizza = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
//				if (!areaDatiPassReturnLocalizza.getCodErr().equals("0000")) {
//					areaDatiPassReturn.setCodErr(areaDatiPassReturnLocalizza.getCodErr());
//					areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocalizza.getTestoProtocollo());
//					return areaDatiPassReturn;
//				}

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


//				INIZIO MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009
//				VALE ANCHE PER CAMBIO COLORE VERDE/BLU (possesso o solo gestione)
//				AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//				AreaDatiVariazioneReturnVO areaDatiPassReturnLocalizza = new AreaDatiVariazioneReturnVO();
//
//				SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
//
//				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//				areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
//						.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
//				areaLocalizza.setAuthority("LU");
//				areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
//				areaLocalizza.setTipoOpe("Localizza");
//				areaLocalizza.setTipoLoc("Gestione");
//				areaLocalizza.setIndice(false);
//				areaLocalizza.setPolo(true);
//
//				areaDatiPassReturnLocalizza = new AreaDatiVariazioneReturnVO();
//				areaDatiPassReturnLocalizza = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
//				areaDatiPassReturn.setCodErr(areaDatiPassReturnLocalizza.getCodErr());
//				areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocalizza.getTestoProtocollo());
//				FINE MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009


				areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
				return areaDatiPassReturn;

		} else {

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
				.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
				.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
			.getSbnResponseTypeChoice().getSbnOutput()
			.getTotRighe();

			int progressivo = 0;

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			return areaDatiPassReturn;

		}

	}

	private AreaDatiVariazioneReturnVO chiamataInsertIndiceUdatePolo(SbnMessageType sbnmessage,
			AreaDatiVariazioneLuogoVO areaDatiPass) {

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
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
				!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		//====================================================================================================
		// Sono stati trovati simili in Indice in fase di condivisione:
		// Se la lista è composta da un solo elemento viene effettuata la cattura e la fusione
		// in Polo automaticamente
		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004") &&
				sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe() == 1) {

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() >0 ) {
				ElementAutType elementAutType = new ElementAutType();
				elementAutType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);

				AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
				AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;

				areaDatiPassCattura.setIdPadre(elementAutType.getDatiElementoAut().getT001());
				String[] appo = new String[0];
				areaDatiPassCattura.setInferioriDaCatturare(appo);
				SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
				areaDatiPassReturnCattura = gestioneAllAuthority.catturaAutore(areaDatiPassCattura);
				if (areaDatiPassReturnCattura.getCodErr().equals("") || areaDatiPassReturnCattura.getCodErr().equals("0000")) {
					AreaDatiAccorpamentoVO areaDatiAccorpamentoVO = new AreaDatiAccorpamentoVO();
					AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO = new AreaDatiAccorpamentoReturnVO();
					areaDatiAccorpamentoVO.setIdElementoEliminato(areaDatiPass.getDettLuogoVO().getLid());
					areaDatiAccorpamentoVO.setIdElementoAccorpante(elementAutType.getDatiElementoAut().getT001());
					areaDatiAccorpamentoVO.setTipoAuthority(SbnAuthority.LU);
					areaDatiAccorpamentoVO.setTipoMateriale("");
					areaDatiAccorpamentoVO.setLivelloBaseDati("P");

					SbnGestioneAccorpamentoDao gestioneAccorpamentoDao = new SbnGestioneAccorpamentoDao( indice, polo, user);
					areaDatiAccorpamentoReturnVO = gestioneAccorpamentoDao.richiestaAccorpamento(areaDatiAccorpamentoVO);
					if (areaDatiAccorpamentoReturnVO.getCodErr().equals("") || areaDatiAccorpamentoReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setBid(elementAutType.getDatiElementoAut().getT001());
						areaDatiPassReturn.setVersioneIndice("");
						return areaDatiPassReturn;


					} else {
						areaDatiPassReturn.setCodErr(areaDatiAccorpamentoReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiAccorpamentoReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				} else {
					areaDatiPassReturn.setCodErr(areaDatiPassReturnCattura.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnCattura.getTestoProtocollo());
					return areaDatiPassReturn;
				}
			}
		}
		// ====================================================================================================


		// la procedura si interrompe per consentirne la fusione in Indice (si Invia l'elenco simili trovati)
		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			UtilityCastor utilityCastor = new UtilityCastor();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			int progressivo = 0;

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			return areaDatiPassReturn;
		}

		//=====================================================================================================

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
		modificaType.getElementoAut().getDatiElementoAut().setT005(areaDatiPass.getDettLuogoVO().getVersione());
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



//		INIZIO MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009
//		VALE ANCHE PER CAMBIO COLORE VERDE/BLU (possesso o solo gestione)
//		AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//		areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
//				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
//		areaLocalizza.setAuthority("LU");
//		areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
//		areaLocalizza.setTipoOpe("Localizza");
//		areaLocalizza.setTipoLoc("Gestione");
//		areaLocalizza.setIndice(true);
//		areaLocalizza.setPolo(false);
//
//		AreaDatiVariazioneReturnVO areaDatiPassReturnLocalizza = new AreaDatiVariazioneReturnVO();
//
//		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
//		areaDatiPassReturnLocalizza = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
//		if (!areaDatiPassReturnLocalizza.getCodErr().equals("0000")) {
//			areaDatiPassReturn.setCodErr(areaDatiPassReturnLocalizza.getCodErr());
//			areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocalizza.getTestoProtocollo());
//			return areaDatiPassReturn;
//		}
//		FINE MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009



		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
		areaDatiPassReturn.setVersioneIndice(verIndice);
		areaDatiPassReturn.setVersionePolo(verPolo);
		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataInsertLocale(SbnMessageType sbnmessage,
			AreaDatiVariazioneLuogoVO areaDatiPass) {

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
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
				!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		UtilityCastor utilityCastor = new UtilityCastor();
		if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			int progressivo = 0;

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			return areaDatiPassReturn;
		}

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());

		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataUpdateIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneLuogoVO areaDatiPass) {

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
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
				!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		UtilityCastor utilityCastor = new UtilityCastor();
		if (!utilityCastor.isElementiSimiliTrovati(sbnRisposta)	|| areaDatiPass.isConferma()) {

				// Inizio verifica di esistenza dell'Autore su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getDettLuogoVO().getLid());
				areaDatiControlliPoloVO.setTipoAut("LU");
				areaDatiControlliPoloVO.setFormaNome(areaDatiPass.getDettLuogoVO().getForma());

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
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
				areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0)
						.getDatiElementoAut().getT001());
				return areaDatiPassReturn;

		} else {
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			int progressivo = 0;

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			return areaDatiPassReturn;
		}
	}


	private AreaDatiVariazioneReturnVO chiamataUpdateLocale(SbnMessageType sbnmessage,
			AreaDatiVariazioneLuogoVO areaDatiPass) {

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
		if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000") &&
				!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getTestoEsito());
			return areaDatiPassReturn;
		}

		UtilityCastor utilityCastor = new UtilityCastor();
		if (!utilityCastor.isElementiSimiliTrovati(sbnRisposta)	|| areaDatiPass.isConferma()) {
					// caso di variazione di un oggetto locale: dopo l'aggiornamento
					// si torna al client
					areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getElementoAut(0)
							.getDatiElementoAut().getT001());
					return areaDatiPassReturn;
		} else {
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			int progressivo = 0;

			SinteticaLuoghiView sinteticaLuoghiView;
			List<SinteticaLuoghiView> listaSintentica = new ArrayList<SinteticaLuoghiView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaLuoghiView = creaElementoListaLuoghi(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaLuoghiView);
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
			return areaDatiPassReturn;
		}

	}


	// almaviva2 17.03.2009 Fine nuova gestione luoghi in modo da consentirne la catalogazione locale e poi la condivisione

}
