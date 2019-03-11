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
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.VariazioneBodyTitoli;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.model.unimarcmodel.A010;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.A152;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A210;
import it.iccu.sbn.ejb.model.unimarcmodel.A260;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A801;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAutoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
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
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
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
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteOriginarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.naming.NamingException;

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
public class SbnGestioneAutoriDao {

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;


	public SbnGestioneAutoriDao(FactorySbn indice,FactorySbn polo,SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public static final int RICERCA_TIPO_LEGAME = 0;

	public static final int RICERCA_NOTA_AL_LEGAME = 1;
	public static String PrimaDiVariare = "";
	public static boolean IS_CONFERMA_RICERCA_DIFFERITA = false;
	private static String IID_SPAZIO = " ";
	public static final String IID_STRINGAVUOTA = "";
	public static final String AUT_TIPO_NOME_A = "A";
	public static final String AUT_TIPO_NOME_B = "B";
	public static final String AUT_TIPO_NOME_C = "C";
	public static final String AUT_TIPO_NOME_D = "D";
	public static final String AUT_TIPO_NOME_E = "E";
	public static final String AUT_TIPO_NOME_R = "R";
	public static final String AUT_TIPO_NOME_G = "G";

	private final String AUT_TIPO_LEGAME_1 = "4XX";
	private final String AUT_TIPO_LEGAME_14 = "810";
	private final String AUT_TIPO_LEGAME_15 = "815";

	private int setKeyCtr = -1;
	private String myChiamata = "";
	private String codSbnBiblioteca = "";
	static int number = 0;

	private static Codici codici;

	static {
		try {
			codici = DomainEJBFactory.getInstance().getCodici();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	UtilityCastor utilityCastor = new UtilityCastor();
	DettaglioOggetti dettOggetti = new DettaglioOggetti();

	/**
	 * Questo metodo viene chiamato quando l'utente preme il tasto ricerca
	 * autore
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutori(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

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
			CercaAutoreType cercaAutoreType = new CercaAutoreType();

			// FORMA AL NOME
			if (!areaDatiPass.getInterrGener().getFormaAutore().equals("")) {
				if (!areaDatiPass.getInterrGener().getFormaAutore().equals("tutti")) {
					SbnFormaNome sbnFormaNome;
					if (areaDatiPass.getInterrGener().getFormaAutore().equals(
							"autore")) {
						sbnFormaNome = SbnFormaNome.A;
					} else {
						sbnFormaNome = SbnFormaNome.R;
					}
					cercaAutoreType.setFormaNome(sbnFormaNome);
				}
			}

			StringaCercaType s = new StringaCercaType();

			StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();

			// Nominativo
			if (!areaDatiPass.getInterrGener().getNome().equals("")) {
				if (areaDatiPass.getInterrGener().getTipoRicerca().equals(
						"inizio")) {
					stringaTypeChoice.setStringaLike(areaDatiPass
							.getInterrGener().getNome());
					s.setStringaCercaTypeChoice(stringaTypeChoice);
					canali.setStringaCerca(s);
					cercaAutoreType.setCanaliCercaDatiAut(canali);
				} else if (areaDatiPass.getInterrGener().getTipoRicerca()
						.equals("intero")) {
					stringaTypeChoice.setStringaEsatta(areaDatiPass
							.getInterrGener().getNome());
					s.setStringaCercaTypeChoice(stringaTypeChoice);
					canali.setStringaCerca(s);
					cercaAutoreType.setCanaliCercaDatiAut(canali);
				} else {
					cercaAutoreType.setParoleAut(areaDatiPass.getInterrGener()
							.getParole());
				}
			} else if (!areaDatiPass.getInterrGener().getIsadn().equals("")) {
				// ISADN
				A015 a015 = new A015();
				a015.setA_015(areaDatiPass.getInterrGener().getIsadn()
						.toUpperCase());
				canali.setT015(a015);
				cercaAutoreType.setCanaliCercaDatiAut(canali);
			} else if (!areaDatiPass.getInterrGener().getVid().equals("")) {
				// VID
				canali.setT001(areaDatiPass.getInterrGener().getVid()
						.toUpperCase());
				cercaAutoreType.setCanaliCercaDatiAut(canali);
			}

			// TIPO NOME

			SbnTipoNomeAutore[] sbnTipoNome = utilityCastor
					.converteTipoNome(areaDatiPass.getInterrGener()
							.getTipoNomeBoolean());

			if (sbnTipoNome.length <= 4) {
				cercaAutoreType.setTipoNome(sbnTipoNome);
			}

			if (!areaDatiPass.getInterrGener().getPaeseSelez().equals("")) {
				// Codice Paese
				C102 c102 = new C102();
				c102.setA_102(areaDatiPass.getInterrGener().getPaeseSelez());
				cercaAutoreType.setT102(c102);
			}

			if (!areaDatiPass.getInterrGener().getDataAnnoNascitaA().equals("")) {
				cercaAutoreType.setDataInizio_A(areaDatiPass.getInterrGener().getDataAnnoNascitaA());
			}
			if (!areaDatiPass.getInterrGener().getDataAnnoNascitaDa().equals("")) {
				cercaAutoreType.setDataInizio_Da(areaDatiPass.getInterrGener().getDataAnnoNascitaDa());
				// Modifica almaviva2 11.10.2010 BUG MANTIS 3927
				// se è impostato solo l'anno di nascita Da si deve preimpostare l'anna di nascita A con lo stesso valore
				if (areaDatiPass.getInterrGener().getDataAnnoNascitaA().equals("")) {
					cercaAutoreType.setDataInizio_A(areaDatiPass.getInterrGener().getDataAnnoNascitaDa());
				}
			}

			if (!areaDatiPass.getInterrGener().getDataAnnoMorteA().equals("")) {
				cercaAutoreType.setDataFine_A(areaDatiPass.getInterrGener().getDataAnnoMorteA());
			}
			if (!areaDatiPass.getInterrGener().getDataAnnoMorteDa().equals("")) {
				cercaAutoreType.setDataFine_Da(areaDatiPass.getInterrGener().getDataAnnoMorteDa());
			}

			// tipo authority
			cercaAutoreType.setTipoAuthority(SbnAuthority.AU);

			cercaElemento.setCercaDatiAut(cercaAutoreType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			int totRighe = 0;

			if (areaDatiPass.isRicercaPolo()) {
				this.polo.setMessage(sbnmessage,this.user);
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
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				if (totRighe == 0) {
					if (areaDatiPass.isRicercaIndice()) {
						this.indice.setMessage(sbnmessage,this.user);
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
							areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage()
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
				this.indice.setMessage(sbnmessage,this.user);
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
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
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
			}

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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


	public String controlloFormaleDati(AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass) {
		String esito = "";

		int combinazioni = 0;

		if (areaDatiPass.getInterrGener().getNome().length() > 0 ) combinazioni = combinazioni +1;
		if (areaDatiPass.getInterrGener().getVid().length()  > 0 ) combinazioni = combinazioni +1;
		if (areaDatiPass.getInterrGener().getIsadn().length() > 0 ) combinazioni = combinazioni +1;

		if (combinazioni == 0)
			return "noCanPrim";
		if (combinazioni > 1)
			return "soloUnCanPrim";


		UtilityAutori utilityAutori = new UtilityAutori();
		esito = utilityAutori.isOkControlloAnno(areaDatiPass.getInterrGener().getDataAnnoMorteDa(),
				areaDatiPass.getInterrGener().getDataAnnoMorteA());
		if (!esito.equals(""))
			return esito;

		esito = utilityAutori.isOkControlloAnno(areaDatiPass.getInterrGener().getDataAnnoNascitaDa(),
				areaDatiPass.getInterrGener().getDataAnnoNascitaA());
		if (!esito.equals(""))
			return esito;

		if (!areaDatiPass.getInterrGener().getNome().equals("") &&
				areaDatiPass.getInterrGener().getTipoRicerca().equals("parole")) {
			StringTokenizer st = new StringTokenizer(areaDatiPass.getInterrGener().getNome());

			if (st.countTokens() > 4) {
				// inserire diagnostico di troppe parole (al massimo 4)
				return "ric021";
			}
		}


		combinazioni = 0;
		for (int i = 0; i < areaDatiPass.getInterrGener().getTipoNomeBoolean().length; i++) {
			if (areaDatiPass.getInterrGener().getTipoNomeBoolean()[i]) {
				combinazioni = combinazioni +1;
			}
		}
		if (combinazioni > 4 ) {
			// inserire diagnostico di troppi tipi nome (al massimo 4)
			return "ric022";
		}
		return esito;
	}



	/**
	 * Ottiene un'altro blocco della lista sintetica degli autori.
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoAutori(
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

			cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
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

			this.indice.setMessage(sbnmessage,this.user);
			this.polo.setMessage(sbnmessage,this.user);

			if (areaDatiPass.isRicercaPolo()){
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
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito());
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}
			int numNotizie= 0;
			numPrimo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			numNotizie = sbnRisposta.getSbnMessage().getSbnResponse()
			.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			areaDatiPassReturn.setNumNotizie(numNotizie);
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSinteticaSuccessiva = new ArrayList<SinteticaAutoriView>();

			int tappoRicerca = maxRighe;
			if ((totRighe - numPrimo + 1) < maxRighe) {
				tappoRicerca = (totRighe - numPrimo + 1);
			}

			for (int t = 0; t < tappoRicerca; t++) {
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t, numPrimo);
				listaSinteticaSuccessiva.add(sinteticaAutoriVo);
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

	public SinteticaAutoriView creaElementoLista(SbnOutputType sbnOutPut,
			int elementIndex, int progressivo) {

		// List contenente la nuova riga alla tabella
		SinteticaAutoriView data = new SinteticaAutoriView();

		// icona della prima colonna della tabella
		data.setImageUrl("blank.png");

		// PROGRESSIVO: Numero del elemento
		data.setProgressivo(progressivo);

		data.setTipoAutority("AU");

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		// prendo il nominativo
		String nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);

		// prendo il tipo nome
		String tipoNome = utilityCastor.getTipoNomeDatiElemento(datiElemento);

		// prendo il VID
		String vid = datiElemento.getT001();
		data.setVid(vid.trim());

		DatiElementoTypeCondivisoType condiviso = datiElemento.getCondiviso();
		if (condiviso == null)
			data.setFlagCondiviso(true);
		else
			data.setFlagCondiviso(condiviso.getType() == DatiElementoTypeCondivisoType.S_TYPE);

		// prendo la forma
		SbnFormaNome sbnForma = datiElemento.getFormaNome();

		String VIDLegato = "";
		String nominativoLegato = "";
		String tipoNomeLegato = "";

		if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
			VIDLegato = vid;
			data.setForma("A");
		} else {
			data.setForma("R");
			if (elementoAut.getLegamiElementoAutCount() > 0) {
				String nominativoFormaRinvio = nominativo;
				// l'autore trovato è una forma di rinvio allora devo
				// mettere la sua forma accettata sotto di questo nella riga
				// della sintetica
				// PRENDO IL LEGAME DOVREBBE avere la sua forma accettata
				LegamiType legamiType = elementoAut.getLegamiElementoAut(0);
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(0);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();
				datiElemento = elementAutType.getDatiElementoAut();

				// LEGAMI
				nominativoLegato = utilityCastor.getNominativoDatiElemento(datiElemento);
				VIDLegato = datiElemento.getT001();
				tipoNomeLegato = utilityCastor.getTipoNomeDatiElemento(datiElemento);

				nominativo = new String(nominativoFormaRinvio + "<br />--> "
						+ VIDLegato + IID_SPAZIO + nominativoLegato);
			}
		}

		// prendo la nota informativa
		String notaInformativa = utilityCastor
				.getNotaInformativaDatiElemento(datiElemento);
		if (notaInformativa != null) {
			int index = notaInformativa.indexOf(" //");
			if (index != -1) {
				// prendo la datazione prima della "stringa-spazio-doppio slash"
				data.setDatazione(notaInformativa.substring(0, index));
			}
		}

		// aggiungo il nominativo alla tabella

		if (!data.isFlagCondiviso()) {
			data.setNome("[loc] " + nominativo);
		} else {
			data.setNome(nominativo);
		}

		data.setKeyVidNome(data.getVid() + data.getNome());

		data.setVidAccettata(VIDLegato);
		data.setNomeAccettata(nominativoLegato);
		data.setTipoNomeAccettata(tipoNomeLegato);


		// aggiungo il tipo nome alla tabella
		data.setTipoNome(tipoNome);

		SbnLivello livello = datiElemento.getLivelloAut();

		// aggiungo il Livello di autorità
		data.setLivelloAutorita(livello.toString());


		return data;
	}

	/**
	 * Questo metodo crea un oggetto Castor pronto per fare una richiesta per
	 * VID
	 *
	 * @param VID:
	 *            VID Dell'autore.
	 */
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoAutorePerVid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

		try {


			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaAutoreType cercaAutoreType = new CercaAutoreType();
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

			// tipoOutput analitica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(1);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
			cercaType.setCercaElementoAut(cercaElemento);

			// VID
			canali.setT001(areaDatiPass.getBidRicerca());

			cercaAutoreType.setCanaliCercaDatiAut(canali);
			cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
			cercaElemento.setCercaDatiAut(cercaAutoreType);


			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
			String chiamata = IID_STRINGAVUOTA;
			if (areaDatiPass.isRicercaPolo()) {
				chiamata = "P";
				this.polo.setMessage(sbnmessage,this.user);
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

				this.indice.setMessage(sbnmessage,this.user);
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


			if (areaDatiPass.isInviaSoloTimeStampRadice()) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
					ElementAutType elementAutType = new ElementAutType();
					elementAutType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);
					areaDatiPassReturn.setTimeStampRadice(elementAutType.getDatiElementoAut().getT005());
				}
				return areaDatiPassReturn;
			}


			if (areaDatiPass.isInviaSoloSbnMarc()) {
				areaDatiPassReturn.setSbnMarcType(sbnRisposta);
				return areaDatiPassReturn;
			}

			TreeElementViewTitoli root = new TreeElementViewTitoli();
			root = this.getReticoloAutore(sbnRisposta, root, chiamata, areaDatiPassLocal);
			areaDatiPassReturn.setTreeElementViewTitoli(root);

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

	/**
	 * Crea/Modifica un autore in forma accettata, modo atomico
	 *
	 * @param frame
	 * @param controllo
	 *            conferma per la forzatura del inserimento, simile per inserire
	 *            un autore.
	 * @param creazione
	 *            indica se l'autore si deve creare o modificare
	 *
	 * @return risposta protocollo con il VID dell'autore, null risposta non
	 *         valida
	 */
	public AreaDatiVariazioneReturnVO inserisciAutore(
			AreaDatiVariazioneAutoreVO areaDatiPass) {

		UtilityAutori utilityAutori = new UtilityAutori();
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		String livello = areaDatiPass.getDettAutoreVO().getLivAut();
		if (areaDatiPass.isInserimentoIndice()) {
			if (Integer.valueOf(livello) < 5) {
				areaDatiPassReturn.setCodErr("livAutInfMinimoIndice");
				return areaDatiPassReturn;
			}
		}


		if (!utilityAutori.isOkControlloRepertoriAutore(areaDatiPass
				.getDettAutoreVO().getListaRepertoriNew())) {
			areaDatiPassReturn.setCodErr("ins038");
			return areaDatiPassReturn;
		}


		CreaType crea = null;
		CreaTypeChoice creaTypeChoice = null;
		ModificaType modifica = null;

		SbnMessageType sbnmessage = new SbnMessageType();
		SbnRequestType sbnrequest = new SbnRequestType();

		if (!areaDatiPass.isModifica()) {
			crea = new CreaType();
			creaTypeChoice = new CreaTypeChoice();
		} else {
			modifica = new ModificaType();
		}

		// VERIFICO SE SI DEVE FARE UNA CONFERMA O TROVARE DEI SIMILI
		if (areaDatiPass.isConferma()) {
			if (!areaDatiPass.isModifica()) {
				crea.setTipoControllo(SbnSimile.CONFERMA);
			} else {
				modifica.setTipoControllo(SbnSimile.CONFERMA);
			}
		} else {
			if (!areaDatiPass.isModifica()) {
				crea.setTipoControllo(SbnSimile.SIMILE);
			} else {
				modifica.setTipoControllo(SbnSimile.SIMILE);
			}
		}

		ElementAutType elementAutType = new ElementAutType();
		DatiElementoType datiElemento = new DatiElementoType();

		String nomePartePrimaria = areaDatiPass.getDettAutoreVO().getNome();
		String nomeParteSecondaria = null;

		// QUALIFICAZIONI
		String qualificazioni = utilityAutori.getNomeParteQualificazioni(
				areaDatiPass.getDettAutoreVO().getNome(), areaDatiPass
						.getDettAutoreVO().getTipoNome());


		// CAMPO DATAZIONI
		String datazioni = areaDatiPass.getDettAutoreVO().getDatazioni();

		// LIVELLO DI AUTORITA'
		livello = areaDatiPass.getDettAutoreVO().getLivAut();

		// LINGUA
		String lingua = areaDatiPass.getDettAutoreVO().getLingua();

		// TIPO AUTHORITY
		datiElemento.setTipoAuthority(SbnAuthority.AU);

		// forma ACCETTATA
		datiElemento.setFormaNome(SbnFormaNome.valueOf(areaDatiPass.getDettAutoreVO().getForma()));

		// PAESE
		String paese = areaDatiPass.getDettAutoreVO().getPaese();
		C102 c102 = new C102();
		c102.setA_102(areaDatiPass.getDettAutoreVO().getPaese());

		// NOTA AL NOME
		String notaAlNome = areaDatiPass.getDettAutoreVO().getNotaInformativa();
		A300 a300 = new A300();

		// SE C'E' LA DATAZIONE SI AGGIUNGE ALLA NOTA INFORMATIVA
		// Intervento intreno a seguito di BUG MANTIS 3344 - almaviva2 09.12.2009
		// trovato errore non gestito per datazioni uguale a null
		if (datazioni != null && datazioni.length() > 0) {
			notaAlNome = datazioni + " // " + notaAlNome;
		}

		a300.setA_300(notaAlNome);

		// FONTE
		String fonteAgenzia = "";
		if (areaDatiPass.getDettAutoreVO().getFonteAgenzia() != null) {
			fonteAgenzia = areaDatiPass.getDettAutoreVO().getFonteAgenzia();
		}
		String fontePaese = "";
		if (areaDatiPass.getDettAutoreVO().getFontePaese() != null) {
			fontePaese = areaDatiPass.getDettAutoreVO().getFontePaese();
		}

		A801 a801 = new A801();
		a801.setA_801(fontePaese.trim());
		a801.setB_801(fonteAgenzia.trim());

		// REGOLE DI CATALOGAZIONE
		// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
		// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
		// In creazione il default è REICAT.
		// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
		// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
		// String regoleDiCatal = areaDatiPass.getDettAutoreVO().getRegoleDiCatal();
		String regoleDiCatal = areaDatiPass.getDettAutoreVO().getNorme();
		if (regoleDiCatal.equals("")) {
			regoleDiCatal = "REICAT";
		}
		A152 a152 = new A152();
		a152.setA_152(regoleDiCatal);

		// NOTA DEL CATALOGATORE
		String notaDelCatal = areaDatiPass.getDettAutoreVO()
				.getNotaCatalogatore();
		A830 a830 = new A830();
		a830.setA_830(notaDelCatal);

		SbnTipoNomeAutore tipoNomeCastor= null;
		// TIPO NOME
		if (areaDatiPass.getDettAutoreVO().getTipoNome() != null) {
			if (!areaDatiPass.getDettAutoreVO().getTipoNome().equals("")) {
				tipoNomeCastor = SbnTipoNomeAutore
				.valueOf(areaDatiPass.getDettAutoreVO().getTipoNome());
			} else {
				areaDatiPassReturn.setCodErr("ins040");
				return areaDatiPassReturn;
			}
		} else {
			areaDatiPassReturn.setCodErr("ins040");
			return areaDatiPassReturn;
		}

		String newVid = "";
		if (!areaDatiPass.isModifica()) {
			if (areaDatiPass.isConferma()
					&& (areaDatiPass.getBidTemporaneo() != null && !areaDatiPass.getBidTemporaneo().equals(""))) {
				newVid = areaDatiPass.getBidTemporaneo();
				areaDatiPass.setBidTemporaneo("");
			} else {
				if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
					newVid = areaDatiPass.getDettAutoreVO().getVid();
				} else {
					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					areaDatiPassGetIdSbn.setTipoAut("AU");

					areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
					if (areaDatiPassGetIdSbn.getIdSbn() == null
							|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
						areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					newVid = areaDatiPassGetIdSbn.getIdSbn();
				}
			}
		}

		if (utilityAutori.isEnte(areaDatiPass.getDettAutoreVO().getTipoNome())) {
			// ENTE
			EnteType ente = new EnteType();
			ente.setTipoAuthority(SbnAuthority.AU);

			// TIPO NOME
			ente.setTipoNome(tipoNomeCastor);

			// ATTENZIONE ! VID: stringa di 10 zeri 0000000000
			if (!areaDatiPass.isModifica()) {
				ente.setT001(newVid);
			} else {
				ente.setT001(areaDatiPass.getDettAutoreVO().getVid());
			}

			// paese
			if (!paese.equals(IID_STRINGAVUOTA)) {
				ente.setT102(c102);
			}

			// lingua
			if ((lingua != null) && (!lingua.equals(IID_STRINGAVUOTA))) {
				C101 c101 = new C101();
				String[] lingue = new String[3];
				lingue[0] = lingua;
				c101.setA_101(lingue);
				ente.setT101(c101);
			}

			// regole di catalogazione
			if ((regoleDiCatal != null)
					&& (!regoleDiCatal.equals(IID_STRINGAVUOTA))) {
				ente.setT152(a152);
			}

			// Marzo 2016: gestione ISNI (International standard number identifier)
			String codISNI = areaDatiPass.getDettAutoreVO().getIsadn();
			A010 a010 = new A010();
			if ((codISNI != null) && (!codISNI.equals(IID_STRINGAVUOTA))) {
				a010.setA_010(codISNI);
				ente.setT010(a010);
			}

			// Aggiunge spazio nel caso di nota al nome "vuota"
			if (IID_STRINGAVUOTA.equals(notaAlNome))
				notaAlNome = IID_SPAZIO;
			// nota al nome
			if (!notaAlNome.equals(IID_STRINGAVUOTA)) {
				ente.setT300(a300);
			}

			// fonte
			if (!fontePaese.equals(IID_STRINGAVUOTA)) {
				ente.setT801(a801);
			}

			// nota del catalogatore
			if (!notaDelCatal.equals(IID_STRINGAVUOTA)) {
				ente.setT830(a830);
			}

			// nominativo
			A210 a210 = new A210();

			// questi due sono required
			a210.setId1(Indicatore.VALUE_1);
			a210.setId2(Indicatore.VALUE_1);

			// NUOVA GESTIONE PARTE PRIMARIA E QUALIFICAZIONE
			String strA210 = nomePartePrimaria;
			String strA210_Org = nomePartePrimaria;
			String[] arrayQualificazioni = null;

			// L'eventuale qualificazione viene
			// accodata alla parte primaria.
			if (qualificazioni != null) {
				arrayQualificazioni = qualificazioni.split(" ; ");
			}
			if (arrayQualificazioni != null) {
				for (int i = 0; i < arrayQualificazioni.length; i++) {
					strA210 = strA210 + IID_SPAZIO + arrayQualificazioni[i];
				}

				// Se l'autore è di tipo G, accoda anche
				// la parte <b_210>...</b_210> di <a_210_G>
				if (areaDatiPass.getDettAutoreVO().getTipoNome().equals(
						AUT_TIPO_NOME_G)) {
					String[] b210 = utilityAutori
							.getNomeTipoG_b_210(areaDatiPass
									.getDettAutoreVO().getNome());
					if (b210 != null) {
						for (int j = 0; j < b210.length; j++) {
							if (b210[j] != null) {
								strA210 = strA210 + IID_SPAZIO + b210[j];
							}
						}
					}
				}
			}

			// Comunque invio stringa originale
			a210.setA_210(strA210_Org);
			ente.setT210(a210);

			// livello dI AUTORITA'
			if (livello != IID_STRINGAVUOTA) {
				ente.setLivelloAut(SbnLivello.valueOf(livello));
			}
			if (areaDatiPass.isFlagCondiviso()) {
				ente.setCondiviso(DatiElementoTypeCondivisoType.S);
			} else {
				ente.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			datiElemento = ente;
		} else {
			// è un autore
			AutorePersonaleType autorePersonale = new AutorePersonaleType();

			autorePersonale.setTipoAuthority(SbnAuthority.AU);

			// TIPO NOME
			autorePersonale.setTipoNome(tipoNomeCastor);

			// ATTENZIONE ! VID: stringa di 10 zeri 0000000000
			autorePersonale.setT001(newVid);

			if (!areaDatiPass.isModifica()) {
				autorePersonale.setT001(newVid);
			} else {
				autorePersonale.setT001(areaDatiPass.getDettAutoreVO().getVid());
			}

			// LIVELLO DI AUTORITA
			if (livello != IID_STRINGAVUOTA) {
				autorePersonale.setLivelloAut(SbnLivello.valueOf(livello));
			}

			// nominativo
			A200 a200 = new A200();
			a200.setId2(Indicatore.VALUE_1);

			// ESEMPIO: Pippo, Giuseppe = nomeCompleto
			// UNA PARTE PRINCIPALE C'E' SEMPRE
			a200.setA_200(nomePartePrimaria);

			if (nomeParteSecondaria != null) {
				a200.setB_200(nomeParteSecondaria);
			}

			autorePersonale.setT200(a200);

			// paese
			if ((paese != null) && (!paese.equals(IID_STRINGAVUOTA))) {
				autorePersonale.setT102(c102);
			}

			// regole di catalogazione
			if ((regoleDiCatal != null)
					&& (!regoleDiCatal.equals(IID_STRINGAVUOTA))) {
				autorePersonale.setT152(a152);
			}

			// Marzo 2016: gestione ISNI (International standard number identifier)
			String codISNI = areaDatiPass.getDettAutoreVO().getIsadn();
			A010 a010 = new A010();
			if ((codISNI != null) && (!codISNI.equals(IID_STRINGAVUOTA))) {
				a010.setA_010(codISNI);
				autorePersonale.setT010(a010);
			}

			// Aggiunge spazio nel caso di nota al nome "vuota"
			if (IID_STRINGAVUOTA.equals(notaAlNome))
				notaAlNome = IID_SPAZIO;
			// nota al nome
			if (!notaAlNome.equals(IID_STRINGAVUOTA)) {
				autorePersonale.setT300(a300);
			}

			// fonte
			if ((fontePaese != null)
					&& (!fontePaese.equals(IID_STRINGAVUOTA))) {
				autorePersonale.setT801(a801);
			}

			// nota del catalogatore
			if ((notaDelCatal != null)
					&& (!notaDelCatal.equals(IID_STRINGAVUOTA))) {
				autorePersonale.setT830(a830);
			}

			// lingua
			if ((lingua != null) && (!lingua.equals(IID_STRINGAVUOTA))) {
				C101 c101 = new C101();
				String[] lingue = new String[3];
				lingue[0] = lingua;
				c101.setA_101(lingue);
				autorePersonale.setT101(c101);
			}
			if (areaDatiPass.isFlagCondiviso()) {
				autorePersonale.setCondiviso(DatiElementoTypeCondivisoType.S);
			} else {
				autorePersonale.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			datiElemento = autorePersonale;
		}

		// DATA VARIAZIONE
		if (areaDatiPass.isModifica()) {
			datiElemento.setT005(areaDatiPass.getDettAutoreVO()
					.getVersione());
			if (areaDatiPass.isVariazione()) {
				datiElemento.setStatoRecord(StatoRecord.C);
			}
		}

		// FORMA ACCETTATA
		datiElemento.setFormaNome(SbnFormaNome.valueOf(areaDatiPass.getDettAutoreVO().getForma()));
		elementAutType.setDatiElementoAut(datiElemento);

		// REPERTORI
		List listaRepertoriOld = areaDatiPass.getDettAutoreVO()
				.getListaRepertori();
		List listaRepertoriNew = areaDatiPass.getDettAutoreVO()
				.getListaRepertoriNew();

		// totale dei repertori con le modifiche
		int countOld = listaRepertoriOld.size();
		int countNew = listaRepertoriNew.size();
		LegamiType[] arrayLegamiType = new LegamiType[countOld + countNew];

		if (areaDatiPass.isModifica()) {
			eliminaAllRepertori(arrayLegamiType, listaRepertoriOld,
					areaDatiPass.getDettAutoreVO().getVid());
			addNewRepertori(arrayLegamiType, listaRepertoriNew,
					areaDatiPass.getDettAutoreVO().getVid(), countOld);
		} else {
			if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
				// nel caso di cataloga in Indice i repertori sono contenuti nell'Old e non nel New
				addOldRepertori(arrayLegamiType, listaRepertoriOld,	areaDatiPass.getDettAutoreVO().getVid(), 0);
			} else {
				addNewRepertori(arrayLegamiType, listaRepertoriNew,	areaDatiPass.getDettAutoreVO().getVid(), 0);
			}

		}

		// Se è presente almeno un legame (tra vecchi e
		// nuovi) aggiungo l'array dei legami all'ElementAut.
		if ((countOld > 0) || (countNew > 0)) {
			elementAutType.setLegamiElementoAut(arrayLegamiType);
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
		areaDatiPass.setPrimoBloccoSimili(true);

		// ==================================================================

		if (!areaDatiPass.isModifica()) {
			if (areaDatiPass.isInserimentoIndice() && areaDatiPass.isFlagCondiviso()) {
				// CASO DI CREAZIONE TITOLO IN INDICE: Operazioni da
				// compiere
				// 1. inserimento in indice;
				// 2. inserimento in locale;
				// 3. localizzazione in indice;
				// 3. localizzazione in polo;
				areaDatiPassReturn = chiamataInsertIndice(sbnmessage, areaDatiPass);
			}
			if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
				// CASO DI CONDIVISIONE TITOLO CATALOGATO LOCALMENTE IN INDICE: Operazioni da
				// compiere
				// 1. inserimento in indice;
				// 2. aggiornamento in locale (solo per la parte relativa al flagCondiviso che passa da N a S;
				// 3. localizzazione in indice;
				areaDatiPassReturn = chiamataInsertIndiceUdatePolo(sbnmessage, areaDatiPass);
			}

			if (areaDatiPass.isInserimentoPolo() && !areaDatiPass.isFlagCondiviso()) {
				// CASO DI CREAZIONE TITOLO IN POLO: Operazioni da
				// compiere
				// 2. inserimento in locale;
				// 3. localizzazione SOLO in polo CATALOGAZIONE LOCALE;
				areaDatiPassReturn = chiamataInsertLocale(sbnmessage, areaDatiPass);
			}
		}
		if (areaDatiPass.isModifica()) {
			if (areaDatiPass.isInserimentoIndice()) {
				// CASO DI MODIFICA TITOLO IN INDICE: Operazioni da compiere
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

	}

	private AreaDatiVariazioneReturnVO chiamataInsertIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneAutoreVO areaDatiPass) {

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

		if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)	&& areaDatiPass.isConferma()) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito()
					+ " - TROVATO AUTORE IDENTICO - FORZATURA IMPOSSIBILE");
			return areaDatiPassReturn;
		}


		if (!utilityCastor.isElementiSimiliTrovati(sbnRisposta)
				|| areaDatiPass.isConferma()) {
			// operazione andata a buon fine; in caso di Inserimento nuovo
			// autore si invia la chiamata per localizzazione in Indice dell'Autore

				AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0)
						.getDatiElementoAut().getT001());
				areaLocalizza.setAuthority("AU");
				areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser()
						.getBiblioteca());
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


//				INIZIO MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009
//				VALE ANCHE PER CAMBIO COLORE VERDE/BLU (possesso o solo gestione)
//				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//				areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
//						.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
//				areaLocalizza.setAuthority("AU");
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
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getTotRighe();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getMaxRighe();

			String idLista = "";
			if (sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista() != null) {
				idLista = sbnRisposta.getSbnMessage().getSbnResponse()
				.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			}

			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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


	private AreaDatiVariazioneReturnVO chiamataInsertLocale(SbnMessageType sbnmessage,
			AreaDatiVariazioneAutoreVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;
		sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().
					getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.N);

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

		// Siamo nel caso Simili ma con il flag di conferma impostato (quindi IDENTICO); si invia il diagnostico
		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")
				&& areaDatiPass.isConferma()) {
			areaDatiPassReturn.setCodErr("forzaturaAutoreNoDisp");
			return areaDatiPassReturn;
		}
		UtilityCastor utilityCastor = new UtilityCastor();
		if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getTotRighe();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}
			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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

		areaDatiPassReturn.setBid(sbnRisposta.getSbnMessage()
				.getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0)
				.getDatiElementoAut().getT001());

		return areaDatiPassReturn;
	}


	private AreaDatiVariazioneReturnVO chiamataUpdateIndice(SbnMessageType sbnmessage,
			AreaDatiVariazioneAutoreVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {
			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
			// almaviva2 BUG MANTIS 4236 - inserito il livello di ricerca per la prospettazione corretta sulla sintetica simili trovati
			// nella variazione;
			areaDatiPassReturn.setLivelloTrovato("I");
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

		// Inizio modifica almaviva2 04.05.2010 - BUG MANTIS 3697 (preso da chiamataInsertIndice)
		if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)	&& areaDatiPass.isConferma()) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
//			areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito()
//					+ " - TROVATO AUTORE IDENTICO - FORZATURA IMPOSSIBILE");
			return areaDatiPassReturn;
		}
		// Fine modifica almaviva2 04.05.2010 - BUG MANTIS 3697 (preso da chiamataInsertIndice)

		if (!utilityCastor.isElementiSimiliTrovati(sbnRisposta)	|| areaDatiPass.isConferma()) {

				// Inizio verifica di esistenza dell'Autore su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getDettAutoreVO().getVid());
				areaDatiControlliPoloVO.setTipoAut("AU");
				areaDatiControlliPoloVO.setFormaNome(areaDatiPass.getDettAutoreVO().getForma());

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
						// Modifica BUG Mantis 3955 almaviva2 28.10.2010
						// si adegua il trattamento a quello dei titoli
//						areaDatiPassReturn.setCodErr("9999");
//						areaDatiPassReturn.setTestoProtocollo("Attenzione: l'autore è assente dalla Base dati locale");
						areaDatiPassReturn.setCodErr("disalPoloIndice");

						return areaDatiPassReturn;
					}
				} else {
					datiElementoTypePolo = areaDatiControlliPoloVO.getDatiElementoType();
				}
//				 Fine verifica di esistenza del Documento su Polo

				sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(datiElementoTypePolo.getT005());
				sbnmessage.getSbnRequest().getModifica().setTipoControllo(SbnSimile.CONFERMA);
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
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getTotRighe();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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
			AreaDatiVariazioneAutoreVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		// almaviva2 04.03.2010 nel contesto del BUG 3582 è stato adeguato il flag di conferma a quello dei titoli
		sbnmessage.getSbnRequest().getModifica().setTipoControllo(SbnSimile.CONFERMA);
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
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getTotRighe();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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
			AreaDatiVariazioneAutoreVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		String verIndice = "";
		String verPolo = "";

//		 CASO DI CREAZIONE AUTORE IN INDICE: Operazioni da compiere
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
		// VERSIONE NUOVA - NON SI EFFETTUA MAI LA FUSIONE AUTOMATICA !!!!!!!!!!!!!!!!!!!!

//		// Sono stati trovati simili in Indice in fase di condivisione:
//		// Se la lista è composta da un solo elemento viene effettuata la cattura e la fusione
//		// in Polo automaticamente
//		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004") &&
//				sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe() == 1) {
//
//			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() >0 ) {
//				ElementAutType elementAutType = new ElementAutType();
//				elementAutType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);
//
//				AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
//				AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;
//
//				areaDatiPassCattura.setIdPadre(elementAutType.getDatiElementoAut().getT001());
//				String[] appo = new String[0];
//				areaDatiPassCattura.setInferioriDaCatturare(appo);
//				SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
//				areaDatiPassReturnCattura = gestioneAllAuthority.catturaAutore(areaDatiPassCattura);
//				if (areaDatiPassReturnCattura.getCodErr().equals("") || areaDatiPassReturnCattura.getCodErr().equals("0000")) {
//					AreaDatiAccorpamentoVO areaDatiAccorpamentoVO = new AreaDatiAccorpamentoVO();
//					AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO = new AreaDatiAccorpamentoReturnVO();
//					areaDatiAccorpamentoVO.setIdElementoEliminato(areaDatiPass.getDettAutoreVO().getVid());
//					areaDatiAccorpamentoVO.setIdElementoAccorpante(elementAutType.getDatiElementoAut().getT001());
//					areaDatiAccorpamentoVO.setTipoAuthority(SbnAuthority.AU);
//					areaDatiAccorpamentoVO.setTipoMateriale("");
//					areaDatiAccorpamentoVO.setLivelloBaseDati("P");
//
//					SbnGestioneAccorpamentoDao gestioneAccorpamentoDao = new SbnGestioneAccorpamentoDao( indice, polo, user);
//					areaDatiAccorpamentoReturnVO = gestioneAccorpamentoDao.richiestaAccorpamento(areaDatiAccorpamentoVO);
//					if (areaDatiAccorpamentoReturnVO.getCodErr().equals("") || areaDatiAccorpamentoReturnVO.getCodErr().equals("0000")) {
//						areaDatiPassReturn.setBid(elementAutType.getDatiElementoAut().getT001());
//						areaDatiPassReturn.setVersioneIndice("");
//						areaDatiPassReturn.setTestoProtocolloInformational("ATTENZIONE: L'autore locale "
//								+ areaDatiPass.getDettAutoreVO().getVid() + " è stato fuso con l'autore presente in Indice "
//								+ elementAutType.getDatiElementoAut().getT001() + " perchè identico;<BR>"
//								+ "L'autore " + elementAutType.getDatiElementoAut().getT001() + " è stato quindi catturato ed è disponibile al Polo");
//						return areaDatiPassReturn;
//
//
//					} else {
//						areaDatiPassReturn.setCodErr(areaDatiAccorpamentoReturnVO.getCodErr());
//						areaDatiPassReturn.setTestoProtocollo(areaDatiAccorpamentoReturnVO.getTestoProtocollo());
//						return areaDatiPassReturn;
//					}
//				} else {
//					areaDatiPassReturn.setCodErr(areaDatiPassReturnCattura.getCodErr());
//					areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnCattura.getTestoProtocollo());
//					return areaDatiPassReturn;
//				}
//			}
//		}
		// ====================================================================================================


		// Intervento interno almaviva2 30.03.2011 - inserito controllo su tentato inserimento autore identico  MAIL almaviva
		// Siamo nel caso Simili ma con il flag di conferma impostato (quindi IDENTICO); si invia il diagnostico
		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")
				&& areaDatiPass.isConferma()) {
			areaDatiPassReturn.setCodErr("forzaturaAutoreNoDisp");
			return areaDatiPassReturn;
		}


		// la procedura si interrompe per consentirne la fusione in Indice (si Invia l'elenco simili trovati)
		if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
			UtilityCastor utilityCastor = new UtilityCastor();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getTotRighe();
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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


		// Inizio Prima di effettuare l'aggiornamento dei flag di condivisione si devono inviare in Indice
		// le forme di rinvio eventualmente presenti sulla tabella apposita

		if (areaDatiPass.isLegameDaCondividere()) {
			AreaDatiLegameTitoloVO areaDatiLegameTitoloVO;
			AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO;
			for (int i = 0; i < areaDatiPass.getListaAreaDatiLegameTitoloVO().size(); i++) {
				areaDatiLegameTitoloVO = areaDatiPass.getListaAreaDatiLegameTitoloVO().get(i);
				areaDatiVariazioneReturnVO = creaFormaRinvio(areaDatiLegameTitoloVO);
				if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
			}
		}

		// Fine Prima di effettuare l'aggiornamento dei flag di condivisione si devono inviare in Indice
		// le forme di rinvio eventualmente presenti


		// 2. aggiornamento in locale del solo flag condiviso
		ElementAutType elementAutType = sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut();



		// Inizio BUG 3202 Modifica per consentire l'aggiornamento dell'ayutore senza il Crea legame ai repertori
		if (elementAutType.getLegamiElementoAutCount() > 0) {
			LegamiType[] arrayLegamiType = new LegamiType[0];
			elementAutType.setLegamiElementoAut(arrayLegamiType);
		}

		// FIne BUG 3202 Modifica per consentire l'aggiornamento dell'ayutore senza il Crea legame ai repertori


		ModificaType modificaType = null;
		modificaType = new ModificaType();

		modificaType.setTipoControllo(SbnSimile.CONFERMA);
		modificaType.setElementoAut(elementAutType);

		modificaType.getElementoAut().getDatiElementoAut().setStatoRecord(StatoRecord.C);
		modificaType.getElementoAut().getDatiElementoAut().setT005(areaDatiPass.getDettAutoreVO().getVersione());
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


		AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
		areaLocalizza.setIdLoc(sbnRisposta.getSbnMessage()
				.getSbnResponse().getSbnResponseTypeChoice()
				.getSbnOutput().getElementoAut(0)
				.getDatiElementoAut().getT001());
		areaLocalizza.setAuthority("AU");
		areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser()
				.getBiblioteca());
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
		areaDatiPassReturn.setBid(areaLocalizza.getIdLoc());
		areaDatiPassReturn.setVersioneIndice(verIndice);
		areaDatiPassReturn.setVersionePolo(verPolo);
		return areaDatiPassReturn;
	}


	/**
	 * Dato un array di tipo LegamiType[], aggiunge i legami a repertorio
	 * (fonte) a partire dall'indice indicato.
	 *
	 * @param arrayLegamiType
	 *            array di tipo LegamiType[].
	 * @param listaRepertori
	 *            IIDTable di repertori.
	 * @param vid
	 *            identificativo di partenza.
	 * @param numRepertoriCanc
	 *            numero di repertori da cancellare (già presenti nelle prime
	 *            posizioni dell'array di tipo LegamiType[]).
	 */
	public void addNewRepertori(LegamiType[] arrayLegamiType,
			List listaRepertori, String vid, int numRepertoriCanc) {

		// creo i legami per ogni repertorio (ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		int count = listaRepertori.size();

		for (int i = 0; i < count; i++) {
			legamiType = new LegamiType();

			legamiType.setIdPartenza(vid);
			legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			TabellaNumSTDImpronteAggiornataVO repertorio = (TabellaNumSTDImpronteAggiornataVO) listaRepertori.get(i);
			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(repertorio.getNota());

			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// prendo il tipo legame della tabella
			String tipoLegame = repertorio.getCampoUno();

			// tipo legame
			legameElementoAut.setTipoLegame(utilityCastor.codificaLegameRepertorio(tipoLegame));

			// prendo il codice del repertorio
			String codice = repertorio.getCampoDue();
			legameElementoAut.setIdArrivo(codice.trim());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[i + numRepertoriCanc] = legamiType;
		}
	}

	public void addOldRepertori(LegamiType[] arrayLegamiType,
			List listaRepertori, String vid, int numRepertoriCanc) {

		// creo i legami per ogni repertorio (ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		int count = listaRepertori.size();

		for (int i = 0; i < count; i++) {
			legamiType = new LegamiType();

			legamiType.setIdPartenza(vid);
			legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			TabellaNumSTDImpronteOriginarioVO repertorio = (TabellaNumSTDImpronteOriginarioVO) listaRepertori.get(i);
			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(repertorio.getNota());

			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// prendo il tipo legame della tabella
			String tipoLegame = repertorio.getCampoUno();

			// tipo legame
			legameElementoAut.setTipoLegame(utilityCastor.codificaLegameRepertorio(tipoLegame));

			// prendo il codice del repertorio
			String codice = repertorio.getCampoDue();
			legameElementoAut.setIdArrivo(codice.trim());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[i + numRepertoriCanc] = legamiType;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param arrayLegamiType
	 *            DOCUMENT ME!
	 * @param repertori
	 *            DOCUMENT ME!
	 * @param vid
	 *            DOCUMENT ME!
	 */
	public void eliminaAllRepertori(LegamiType[] arrayLegamiType,
			List repertori, String vid) {
		// creo i legami per ogni repertorio (ogni riga della tabella)
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		int count = repertori.size();

		for (int i = 0; i < count; i++) {

			TabellaNumSTDImpronteOriginarioVO repertorio = (TabellaNumSTDImpronteOriginarioVO) repertori.get(i);

			legamiType = new LegamiType();

			legamiType.setIdPartenza(vid);
			legamiType.setTipoOperazione(SbnTipoOperazione.CANCELLA);

			// ARRIVO LEGAME
			// un solo arrivo legame per repertorio
			arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(repertorio.getNota());

			legameElementoAut.setTipoAuthority(SbnAuthority.RE);

			// prendo il tipo legame della tabella
			String tipoLegame = repertorio.getCampoUno();

			// tipo legame
			legameElementoAut.setTipoLegame(utilityCastor
					.codificaLegameRepertorio(tipoLegame));

			// prendo il codice del repertorio
			String codice = repertorio.getCampoDue();
			legameElementoAut.setIdArrivo(codice.trim());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno perché 1 arrivo legame
			// 1 repertorio
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[i] = legamiType;
		}
	}


	public AreaDatiVariazioneReturnVO creaFormaRinvio(
			AreaDatiLegameTitoloVO areaDatiPass) {

		UtilityAutori utilityAutori = new UtilityAutori();
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		if (areaDatiPass.getDescArrivo().equals(IID_STRINGAVUOTA)) {
			areaDatiPassReturn.setCodErr("ins036");
			return areaDatiPassReturn;
		}
		// DATI DI LEGAME
		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		LegamiType[] arrayLegamiType = null;

		SBNMarc sbnRisposta = null;

		try {


			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = null;
			CreaType crea = null;
			CreaTypeChoice creaTypeChoice = null;

			if (areaDatiPass.getTipoOperazione().equals("Crea")) {
				crea = new CreaType();
				creaTypeChoice = new CreaTypeChoice();
				crea.setTipoControllo(SbnSimile.CONFERMA);
			} else {
				modifica = new ModificaType();
				modifica.setTipoControllo(SbnSimile.CONFERMA);
			}

			String newVid = "";

			ElementAutType elementAutType = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();

			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("AU")) {
				String nomePartePrimaria = areaDatiPass.getDescArrivo();

				String nomeParteSecondaria = null;
				String qualificazioni = utilityAutori.getNomeParteQualificazioni(
						areaDatiPass.getDescArrivo(), areaDatiPass
								.getTipoNomeArrivo());

				String livello = areaDatiPass.getLivAutIdArrivo();
				datiElemento.setTipoAuthority(SbnAuthority.AU);
				datiElemento.setFormaNome(SbnFormaNome.R);
				String notaAlNome = areaDatiPass.getNotaInformativaIdArrivo();
				A300 a300 = new A300();
				a300.setA_300(notaAlNome);
				SbnTipoNomeAutore tipoNomeCastor = SbnTipoNomeAutore
						.valueOf(areaDatiPass.getTipoNomeArrivo());


				if (areaDatiPass.getTipoOperazione().equals("Crea")) {
					if (areaDatiPass.getIdArrivo() == null || areaDatiPass.getIdArrivo().equals("")) {
						AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
						areaDatiPassGetIdSbn.setTipoAut("AU");

						areaDatiPassGetIdSbn = gestioneAllAuthority
								.getIdSbn(areaDatiPassGetIdSbn);
						if (areaDatiPassGetIdSbn.getIdSbn() == null
								|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
							areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn
									.getCodErr());
							areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn
									.getTestoProtocollo());
							return areaDatiPassReturn;
						}
						newVid = areaDatiPassGetIdSbn.getIdSbn();
					} else {
						newVid = areaDatiPass.getIdArrivo();
					}
				}

				if (utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo())) {
					EnteType ente = new EnteType();
					ente.setTipoAuthority(SbnAuthority.AU);
					ente.setTipoNome(tipoNomeCastor);

					if (areaDatiPass.isFlagCondivisoLegame()) {
						ente.setCondiviso(DatiElementoTypeCondivisoType.S);
					} else {
						ente.setCondiviso(DatiElementoTypeCondivisoType.N);
					}

					if (areaDatiPass.getTipoOperazione().equals("Crea")) {
						ente.setT001(newVid);
					} else {
						ente.setT001(areaDatiPass.getIdArrivo());
					}
					if (!notaAlNome.equals(IID_STRINGAVUOTA)) {
						ente.setT300(a300);
					}
					A210 a210 = new A210();
					a210.setId1(Indicatore.VALUE_1);
					a210.setId2(Indicatore.VALUE_1);

					String strA210 = nomePartePrimaria;
					String strA210_Org = nomePartePrimaria;
					String[] arrayQualificazioni = null;

					// L'eventuale qualificazione viene accodata alla parte primaria.
					if (qualificazioni != null) {
						arrayQualificazioni = qualificazioni.split(" ; ");
					}

					if (arrayQualificazioni != null) {
						for (int i = 0; i < arrayQualificazioni.length; i++) {
							strA210 = strA210 + IID_SPAZIO + arrayQualificazioni[i];
						}

						if (areaDatiPass.getTipoNomeArrivo()
								.equals(AUT_TIPO_NOME_G)) {
							String[] b210 = utilityAutori
									.getNomeTipoG_b_210(areaDatiPass
											.getDescArrivo());
							if (b210 != null) {
								for (int j = 0; j < b210.length; j++) {
									if (b210[j] != null) {
										strA210 = strA210 + IID_SPAZIO + b210[j];
									}
								}
							}
						}
					}

					a210.setA_210(strA210_Org);

					ente.setT210(a210);

					ente.setLivelloAut(SbnLivello.valueOf(livello));

					datiElemento = ente;
				} else {
					// è un autore
					AutorePersonaleType autorePersonale = new AutorePersonaleType();
					autorePersonale.setTipoAuthority(SbnAuthority.AU);

					autorePersonale.setTipoNome(tipoNomeCastor);

					if (areaDatiPass.isFlagCondivisoLegame()) {
						autorePersonale.setCondiviso(DatiElementoTypeCondivisoType.S);
					} else {
						autorePersonale.setCondiviso(DatiElementoTypeCondivisoType.N);
					}



					if (areaDatiPass.getTipoOperazione().equals("Crea")) {
						autorePersonale.setT001(newVid);
					} else {
						autorePersonale.setT001(areaDatiPass.getIdArrivo());
					}
					autorePersonale.setLivelloAut(SbnLivello.valueOf(livello));

					A200 a200 = new A200();
					a200.setId2(Indicatore.VALUE_1);

					a200.setA_200(nomePartePrimaria);

					if (nomeParteSecondaria != null) {
						a200.setB_200(nomeParteSecondaria);
					}

					autorePersonale.setT200(a200);
					if (!notaAlNome.equals(IID_STRINGAVUOTA)) {
						autorePersonale.setT300(a300);
					}
					datiElemento = autorePersonale;
				}
			}

			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("LU")) {
				LuogoType luogoType = new LuogoType();

				A260 a260 = new A260();

				luogoType.setTipoAuthority(SbnAuthority.LU);

				luogoType.setLivelloAut(SbnLivello.valueOf(areaDatiPass.getLivAutIdArrivo()));
				a260.setD_260(areaDatiPass.getDescArrivo());
				luogoType.setT260(a260);

				if (areaDatiPass.getTipoOperazione().equals("Crea")) {
					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					areaDatiPassGetIdSbn.setTipoAut("LU");

					areaDatiPassGetIdSbn = gestioneAllAuthority
							.getIdSbn(areaDatiPassGetIdSbn);
					if (areaDatiPassGetIdSbn.getIdSbn() == null
							|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
						areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn
								.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn
								.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					newVid = areaDatiPassGetIdSbn.getIdSbn();
					luogoType.setT001(newVid);
				}
				datiElemento = luogoType;
			}


			// DATA VARIAZIONE
			if (!areaDatiPass.getTipoOperazione().equals("Crea")) {
				datiElemento.setT005(areaDatiPass.getTimeStampBidPartenza());
			}

			// FORMA RINVIO
			datiElemento.setFormaNome(SbnFormaNome.R);
			elementAutType.setDatiElementoAut(datiElemento);

			legamiType = new LegamiType();

			legamiType.setIdPartenza(newVid);

			if (!areaDatiPass.getTipoOperazione().equals("Crea")) {
				legamiType.setTipoOperazione(SbnTipoOperazione.MODIFICA);
			} else {
				legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
			}

			// ARRIVO LEGAME
			arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			// NOTA AL LEGAME
			legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());

			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("AU")) {
				legameElementoAut.setTipoAuthority(SbnAuthority.AU);
			}
			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("LU")) {
				legameElementoAut.setTipoAuthority(SbnAuthority.LU);
			}


			// tipo legame
			legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));

			if (areaDatiPass.isFlagCondivisoLegame()) {
				legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
			} else {
				legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
			}

			// COME ID ARRIVO IL VID DELLA FORMA ACCETTATA
			legameElementoAut.setIdArrivo(areaDatiPass.getBidPartenza());

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			// la dimensione dell'array deve essere uno
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);

			arrayLegamiType = new LegamiType[1];
			arrayLegamiType[0] = legamiType;

			// aggiungo IL LEGAME
			elementAutType.setLegamiElementoAut(arrayLegamiType);

			// FINE FORMA ACCETTATA
			if (areaDatiPass.getTipoOperazione().equals("Crea")) {
				creaTypeChoice.setElementoAut(elementAutType);
				crea.setCreaTypeChoice(creaTypeChoice);
			} else {
				modifica.setElementoAut(elementAutType);
			}


			sbnmessage.setSbnRequest(sbnrequest);

			if (areaDatiPass.getTipoOperazione().equals("Crea")) {
				sbnrequest.setCrea(crea);
			} else {
				sbnrequest.setModifica(modifica);
			}

			AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
			AreaDatiVariazioneReturnVO areaDatiPassReturnLocal = new AreaDatiVariazioneReturnVO();

			if (areaDatiPass.isInserimentoIndice()) {
				// Inserimento del legame in Indice
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}

				// Inserimento della localizzazione in Indice per l'oggetto inserito
				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(newVid);
				if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("AU")) {
					areaLocalizza.setAuthority("AU");
				}
				if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("LU")) {
					areaLocalizza.setAuthority("LU");
				}

				areaLocalizza
						.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
				areaLocalizza.setTipoOpe("Localizza");
				areaLocalizza.setTipoLoc("Gestione");
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
				if (!areaDatiPass.getAuthorityOggettoPartenza().toString().equals("LU")) {
					areaDatiPassReturnLocal = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
					if (!areaDatiPassReturnLocal.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocal
								.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			}

			// inizio intervento almaviva2
			// a causa della mancanza dell'informazione sulla localizzazione non possimao sapere le il luogo
			// è presente o no in polo quindi invece dell'aggiornamento puntuale viene effettuata una operazione
			// cattura/allinea così da sanare eventuali incongruenze.
			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("LU")) {
				AreaTabellaOggettiDaCatturareVO areaDatiPassCattura = new AreaTabellaOggettiDaCatturareVO();
				AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = null;

				areaDatiPassCattura.setIdPadre(newVid);
				String[] appo = new String[0];
				areaDatiPassCattura.setInferioriDaCatturare(appo);
				areaDatiPassReturnCattura = gestioneAllAuthority.catturaLuogo(areaDatiPassCattura);

				if (areaDatiPassReturnCattura.getCodErr().equals("") || areaDatiPassReturnCattura.getCodErr().equals("0000")) {
					areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());
					return areaDatiPassReturn;
				} else {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
			}
			// fine intervento almaviva2

			// nel caso di inserimento rinvio solo in indice, in Polo si dovrà aggiornare solo il flag di condivisione della forma di rinvio
			if (!areaDatiPass.isInserimentoPolo()) {
				// aggiornamento in locale del solo flag condiviso
				ElementAutType elementAutTypeIndice = new ElementAutType();
				elementAutTypeIndice.setDatiElementoAut(sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getDatiElementoAut());
				elementAutTypeIndice.setNLista(0);

				ModificaType modificaType = null;
				modificaType = new ModificaType();

				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				modificaType.setElementoAut(elementAutTypeIndice);

				modificaType.getElementoAut().getDatiElementoAut().setT005(areaDatiPass.getTimeStampBidArrivo());
				modificaType.getElementoAut().getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.S);
				modificaType.getElementoAut().getDatiElementoAut().setStatoRecord(StatoRecord.C);

				sbnmessage.getSbnRequest().setModifica(modificaType);
				sbnmessage.getSbnRequest().setCrea(null);
			}

			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPassReturn.setCodErr("noServerLoc");
				return areaDatiPassReturn;
			}

			// Modifica BUG Mantis 3955 almaviva2 28.10.2010 per valore codErr 3013 si invia apposito diagnostico
			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3013")) {
					areaDatiPassReturn.setCodErr("disalPoloIndice");
				} else {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				}
				return areaDatiPassReturn;
			}


//			INIZIO MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009
//			VALE ANCHE PER CAMBIO COLORE VERDE/BLU (possesso o solo gestione)

			// Inserimento della localizzazione in Polo per l'oggetto inserito
//			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//			areaLocalizza.setIdLoc(newVid);
//			if (areaDatiPass.getAuthorityOggettoPartenza().toString().equals("AU")) {
//				areaLocalizza.setAuthority("AU");
//			}
//
//			areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
//			areaLocalizza.setTipoOpe("Localizza");
//			areaLocalizza.setTipoLoc("Gestione");
//			areaLocalizza.setIndice(false);
//			areaLocalizza.setPolo(true);
//			areaDatiPassReturnLocal = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
//			if (!areaDatiPassReturnLocal.getCodErr().equals("0000")) {
//				areaDatiPassReturn.setCodErr("9999");
//				areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnLocal.getTestoProtocollo());
//				return areaDatiPassReturn;
//			}
//			FINE MODIFICA almaviva2 PER ELIMINAZIONE LOCALIZZAZIONI IN POLO PER GESTIONE 10.LUGLIO.2009


			areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;

	}

	/**
	 * Scambia la forma di una foram di rinvio
	 *
	 * @param frame
	 *            frame scambia forma
	 *
	 * @return risposta del server
	 */

	public AreaDatiVariazioneReturnVO scambiaForma(
			AreaDatiLegameTitoloVO areaDatiPass) {

		UtilityAutori utilityAutori = new UtilityAutori();
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		String vidFormaAccettata = areaDatiPass.getBidPartenza();
		String vidFormaRinvio = areaDatiPass.getIdArrivo();

		SBNMarc sbnRisposta = null;

		try {


			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = new ModificaType();
			ElementAutType elementoAut = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();

			// DATI DELL'ELEMENTO
			// METTO IL VID DELLA FORMA DI RINVIO
			if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {
				if (utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo())) {
					EnteType ente = new EnteType();
					ente.setT001(vidFormaAccettata);
					datiElemento = ente;
				} else {
					AutorePersonaleType autorePersonale = new AutorePersonaleType();
					autorePersonale.setT001(vidFormaAccettata);
					datiElemento = autorePersonale;
				}
				datiElemento.setTipoAuthority(SbnAuthority.AU);
			}
			if (areaDatiPass.getAuthorityOggettoPartenza().equals("LU")) {
				LuogoType luogoType = new LuogoType();
				luogoType.setT001(vidFormaAccettata);
				datiElemento = luogoType;
				datiElemento.setTipoAuthority(SbnAuthority.LU);
			}


			// data variazione
			datiElemento.setT005(areaDatiPass.getTimeStampBidPartenza());

			// forma nome
			datiElemento.setFormaNome(SbnFormaNome.A);

			// IL LIVELLO DI AUTORITA' SARA' QUELLO DEL UTENTE ATTIVO
			//
			datiElemento.setLivelloAut(SbnLivello.valueOf(areaDatiPass.getLivAutIdArrivo()));



			// METTO COME LEGAME LA FORMA ACCETTATA
			LegamiType[] arrayLegamiType = new LegamiType[1];
			LegamiType legamiType = new LegamiType();
			legamiType.setIdPartenza(vidFormaAccettata);
			legamiType.setTipoOperazione(SbnTipoOperazione.SCAMBIOFORMA);

			LegameElementoAutType legameElementoAut = new LegameElementoAutType();

			// ARRIVO LEGAME
			ArrivoLegame arrivoLegame = new ArrivoLegame();

			// tipo authority
			legameElementoAut = new LegameElementoAutType();

			// tipo legame
			legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));

			if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {
				legameElementoAut.setTipoAuthority(SbnAuthority.AU);
			}

			if (areaDatiPass.getAuthorityOggettoPartenza().equals("LU")) {
				legameElementoAut.setTipoAuthority(SbnAuthority.LU);
			}

			legameElementoAut.setIdArrivo(vidFormaRinvio);

			arrivoLegame.setLegameElementoAut(legameElementoAut);

			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[0] = legamiType;

			elementoAut.setLegamiElementoAut(arrayLegamiType);
			elementoAut.setDatiElementoAut(datiElemento);
			modifica.setElementoAut(elementoAut);
			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			// Scambio forma in Indice
			this.indice.setMessage(sbnmessage,this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPassReturn.setCodErr("noServerInd");
				return areaDatiPassReturn;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito());
				return areaDatiPassReturn;
			}

			//======================================================================
			// Inizio verifica di esistenza del Documento su Polo
			AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
			areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
			areaDatiControlliPoloVO.setTipoAut("AU");
			areaDatiControlliPoloVO.setCancellareInferiori(false);
			areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

			if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
				areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
				areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
				return areaDatiPassReturn;
			}

			DatiElementoType datiElementoTypePolo = null;
			if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo("L'oggetto " + areaDatiPass.getBidPartenza() +" non è stato trovato sulla Base Dati Locale");
				return areaDatiPassReturn;
			} else {
				datiElementoTypePolo = areaDatiControlliPoloVO.getDatiElementoType();
			}
			// Fine verifica di esistenza del Documento su Polo
			//======================================================================

			sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(datiElementoTypePolo.getT005());

			// Scambio forma in Polo
			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPassReturn.setCodErr("noServerLoc");
				return areaDatiPassReturn;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito());
				return areaDatiPassReturn;
			}

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;

	}


	/**
	 * Crea un collegamente fra due autori (enti) oppure tra un autore (ente) e
	 * una marca e viceversa.
	 *
	 * @param frame
	 *            frame con i dati di legame
	 * @param tipoLegame
	 * @param tipoAuthorityPartenza
	 * @param tipoAuthorityArrivo
	 * @param tipoOperazione
	 * @return risposta del server
	 */
	public AreaDatiVariazioneReturnVO collegaElementoAuthority(
			AreaDatiLegameTitoloVO areaDatiPass) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		// ////////////////////////////////////////////////////////////////////
		// ///////// COLLEGA UN AUTORE(ente) a un AUTORE(ente) ////////////
		// ///////// OPPURE ////////////
		// ///////// UNA MARCA a un AUTORE(ente) E VICEVERSA ////////////
		// ////////////////////////////////////////////////////////////////////

		LegamiType legamiType;
		ArrivoLegame arrivoLegame;
		LegameElementoAutType legameElementoAut;
		LegameTitAccessoType legameTitAccessoType;
		LegamiType[] arrayLegamiType = null;

		SBNMarc sbnRisposta = null;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = new ModificaType();

			ElementAutType elementAutType = new ElementAutType();

//			 Inizio Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita
			if (areaDatiPass.getTipoOperazione() != null) {
				if (areaDatiPass.getTipoOperazione().equals("Condividi")) {
					if (areaDatiPass.getTimeStampBidPartenza() == null) {

						// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
						// operante che nel caso di centro Sistema non coincidono
						AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO =
							new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
//						AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
						// Fine Modifica almaviva2 16.07.2010

						titoloAnaliticaVO.setBidRicerca(areaDatiPass.getBidPartenza());
						titoloAnaliticaVO.setRicercaIndice(true);
						titoloAnaliticaVO.setRicercaPolo(false);
						titoloAnaliticaVO.setInviaSoloTimeStampRadice(true);
						AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO returnVO = creaRichiestaAnaliticoAutorePerVid(titoloAnaliticaVO);
						if (returnVO.getCodErr().equals("") || returnVO.getCodErr().equals("0000")) {
							areaDatiPass.setTimeStampBidPartenza(returnVO.getTimeStampRadice());
						} else {
							areaDatiPassReturn.setCodErr(returnVO.getCodErr());
							areaDatiPassReturn.setTestoProtocollo(returnVO.getTestoProtocollo());
							return areaDatiPassReturn;
						}
					}
				}
			}
			// Fine Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita



			if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {

				// L'ELEMENTO PADRE E' UN AUTORE (ENTE)
				EnteType ente = new EnteType();
				ente.setTipoAuthority(SbnAuthority.AU);
				ente.setT001(areaDatiPass.getBidPartenza());
				ente.setFormaNome(SbnFormaNome.A);
				ente.setLivelloAut(SbnLivello.valueOf(areaDatiPass
						.getLivAutBidPartenza()));
				ente.setT005(areaDatiPass.getTimeStampBidPartenza());
				elementAutType.setDatiElementoAut(ente);
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("MA")) {

				// L'ELEMENTO PADRE E' UNA MARCA
				MarcaType marca = new MarcaType();
				marca.setTipoAuthority(SbnAuthority.MA);
				marca.setT001(areaDatiPass.getBidPartenza());
				marca.setLivelloAut(SbnLivello.valueOf(areaDatiPass
						.getLivAutBidPartenza()));
				marca.setT005(areaDatiPass.getTimeStampBidPartenza());
				elementAutType.setDatiElementoAut(marca);
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("TU")) {
				// L'ELEMENTO PADRE E' UNA TITOLO UNIFORME
				TitoloUniformeType titoloUniforme = new TitoloUniformeType();
				titoloUniforme.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoPartenza()));
				titoloUniforme.setT001(areaDatiPass.getBidPartenza());
				titoloUniforme.setLivelloAut(SbnLivello.valueOf(areaDatiPass
						.getLivAutBidPartenza()));
				titoloUniforme.setT005(areaDatiPass.getTimeStampBidPartenza());
				elementAutType.setDatiElementoAut(titoloUniforme);
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("UM")) {
				// L'ELEMENTO PADRE E' UNA TITOLO UNIFORME MUSICALE
				TitoloUniformeMusicaType titoloUniformeMusica = new TitoloUniformeMusicaType();
				titoloUniformeMusica.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoPartenza()));
				titoloUniformeMusica.setT001(areaDatiPass.getBidPartenza());
				titoloUniformeMusica.setLivelloAut(SbnLivello.valueOf(areaDatiPass
						.getLivAutBidPartenza()));
				titoloUniformeMusica.setT005(areaDatiPass.getTimeStampBidPartenza());
				elementAutType.setDatiElementoAut(titoloUniformeMusica);
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("LU")) {
				// L'ELEMENTO PADRE E' UNA LUOGO
				LuogoType luogo = new LuogoType();
				luogo.setTipoAuthority(SbnAuthority.LU);
				luogo.setT001(areaDatiPass.getBidPartenza());
				luogo.setLivelloAut(SbnLivello.valueOf(areaDatiPass
						.getLivAutBidPartenza()));
				luogo.setT005(areaDatiPass.getTimeStampBidPartenza());
				elementAutType.setDatiElementoAut(luogo);

			}


			legamiType = new LegamiType();
			legamiType.setIdPartenza(areaDatiPass.getBidPartenza());

			// Inizio Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita
			if (areaDatiPass.getTipoOperazione() != null) {
				if (areaDatiPass.getTipoOperazione().equals("Condividi")) {
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
				} else {
					legamiType.setTipoOperazione(SbnTipoOperazione.valueOf(areaDatiPass.getTipoOperazione()));
				}
			}



			// legamiType.setTipoOperazione(SbnTipoOperazione.valueOf(areaDatiPass.getTipoOperazione()));
			// Fine Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita


			// ARRIVO LEGAME

			arrivoLegame = new ArrivoLegame();
			if (areaDatiPass.getAuthorityOggettoPartenza().equals("TU") || areaDatiPass.getAuthorityOggettoPartenza().equals("UM")) {

				if (areaDatiPass.getAuthorityOggettoArrivo() == null
						|| areaDatiPass.getAuthorityOggettoArrivo().equals("")) {
					legameTitAccessoType = new LegameTitAccessoType();
					String notaAlLegame = areaDatiPass.getNoteLegameNew();

					if (notaAlLegame != null) {
						legameTitAccessoType.setNoteLegame(notaAlLegame);
					}

					String tipoLegame = codici.SBNToUnimarc(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO, areaDatiPass.getTipoLegameNew());

					legameTitAccessoType.setTipoLegame(SbnLegameTitAccesso.valueOf(tipoLegame.substring(1,4)));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameTitAccessoType.setCondiviso(LegameTitAccessoTypeCondivisoType.S);
					} else {
						legameTitAccessoType.setCondiviso(LegameTitAccessoTypeCondivisoType.N);
					}
					legameTitAccessoType.setIdArrivo(areaDatiPass.getIdArrivo());

					arrivoLegame.setLegameTitAccesso(legameTitAccessoType);
				} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("AU")) {


					//************************************************************
					VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli();

					// Inizio Gestione Bibliografica: 05-11-2012 BUG Mantis (Collaudo) 5166
					// nel caso di variazione legame titoloUniforme/UniformeMusicale Autore veniva erroneamente inviato
					// il messaggio di modifica mentre si deve inviare (come del caso di titoloDocumento) l'accoppiata
					// Cancella/Crea al fine di ottenere lo stesso risultato. a tal fine si richiama lo stesso oggetto addLegami
					// che ricompone in maniera corretta la sequenza dei messaggi da inviare.
					arrayLegamiType = xmlBodyTitoli.addLegami(areaDatiPass);
					if (arrayLegamiType == null) {
						areaDatiPassReturn.setCodErr("LegameNonPrevisto");
						return areaDatiPassReturn;
					}

					//************************************************************
//					legameElementoAut = new LegameElementoAutType();
//					if ((areaDatiPass.getTipoResponsNew() != null)
//							&& (!areaDatiPass.getTipoResponsNew().equals(""))) {
//						legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsNew()));
//					}
//					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
//					if (areaDatiPass.isSuperfluoNew()) {
//						legameElementoAut.setSuperfluo(SbnIndicatore.S);
//					} else {
//						legameElementoAut.setSuperfluo(SbnIndicatore.N);
//					}
//					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
//					if ((areaDatiPass.getRelatorCodeNew() != null) && (!areaDatiPass.getRelatorCodeNew().equals(IID_STRINGAVUOTA))) {
//						legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeNew());
//					}
//					if (areaDatiPass.isIncertoNew()) {
//						legameElementoAut.setIncerto(SbnIndicatore.S);
//					} else {
//						legameElementoAut.setIncerto(SbnIndicatore.N);
//					}
//					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());
//
//					UtilityAutori utilityAutori = new UtilityAutori();
//					boolean isAutoreEnte = utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo());
//					String tipoLegameTitAut = xmlBodyTitoli.setTipoLegameConResponsabilita(areaDatiPass.getTipoResponsNew(), isAutoreEnte);
//					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegameTitAut));
//
//					if (areaDatiPass.isFlagCondivisoLegame()) {
//						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
//					} else {
//						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
//					}
//
//					if (!xmlBodyTitoli.isControlloOkResponsabilitaTipoNome(areaDatiPass.getTipoResponsNew(), areaDatiPass.getRelatorCodeNew()))
//						return null;
//
//					arrivoLegame.setLegameElementoAut(legameElementoAut);

					// Fine Gestione Bibliografica: 05-11-2012 BUG Indice 5166

					//************************************************************
				} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("MA")) {

					//************************************************************
					legameElementoAut = new LegameElementoAutType();
					if ((areaDatiPass.getTipoResponsNew() != null)
							&& (!areaDatiPass.getTipoResponsNew().equals(""))) {
						legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsNew()));
					}
					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					if (areaDatiPass.isSuperfluoNew()) {
						legameElementoAut.setSuperfluo(SbnIndicatore.S);
					} else {
						legameElementoAut.setSuperfluo(SbnIndicatore.N);
					}
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
					if ((areaDatiPass.getRelatorCodeNew() != null) && (!areaDatiPass.getRelatorCodeNew().equals(IID_STRINGAVUOTA))) {
						legameElementoAut.setRelatorCode(areaDatiPass.getRelatorCodeNew());
					}
					if (areaDatiPass.isIncertoNew()) {
						legameElementoAut.setIncerto(SbnIndicatore.S);
					} else {
						legameElementoAut.setIncerto(SbnIndicatore.N);
					}
					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

					UtilityAutori utilityAutori = new UtilityAutori();
					VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli();
					boolean isAutoreEnte = utilityAutori.isEnte(areaDatiPass.getTipoNomeArrivo());
					String tipoLegameTitAut = xmlBodyTitoli.setTipoLegameConResponsabilita(areaDatiPass.getTipoResponsNew(), isAutoreEnte);
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegameTitAut));

					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}

					if (!xmlBodyTitoli.isControlloOkResponsabilitaTipoNome(areaDatiPass.getTipoResponsNew(), areaDatiPass.getRelatorCodeNew()))
						return null;

					arrivoLegame.setLegameElementoAut(legameElementoAut);

					//************************************************************

				} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("TU")
						|| areaDatiPass.getAuthorityOggettoArrivo().equals("UM")) {
					// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
					//************************************************************
					legameElementoAut = new LegameElementoAutType();
					if ((areaDatiPass.getTipoResponsNew() != null) && (!areaDatiPass.getTipoResponsNew().equals(""))) {
						legameElementoAut.setTipoRespons(SbnRespons.valueOf(areaDatiPass.getTipoResponsNew()));
					}
					legameElementoAut.setNoteLegame(areaDatiPass.getNoteLegameNew());
					legameElementoAut.setSuperfluo(SbnIndicatore.N);
					legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));
					legameElementoAut.setIncerto(SbnIndicatore.N);
					legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());


					String tipoLegame = codici.SBNToUnimarc(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO, areaDatiPass.getTipoLegameNew());
					if (tipoLegame.startsWith("A531")) {
						legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegame.substring(1,5)));
					} else {
						legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegame.substring(1,4)));
					}
					if (areaDatiPass.isFlagCondivisoLegame()) {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
					} else {
						legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
					}
					arrivoLegame.setLegameElementoAut(legameElementoAut);

					//************************************************************
				}
			} else {
				legameElementoAut = new LegameElementoAutType();
				String notaAlLegame = areaDatiPass.getNoteLegameNew();

				if (notaAlLegame != null) {
					legameElementoAut.setNoteLegame(notaAlLegame);
				}
				legameElementoAut.setTipoAuthority(SbnAuthority
						.valueOf(areaDatiPass.getAuthorityOggettoArrivo()));

				legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(areaDatiPass
						.getTipoLegameNew()));

				if (areaDatiPass.isFlagCondivisoLegame()) {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.S);
				} else {
					legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
				}

				legameElementoAut.setIdArrivo(areaDatiPass.getIdArrivo());

				arrivoLegame.setLegameElementoAut(legameElementoAut);

			}

			// la dimensione dell'array deve essere uno
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			// aggiungo arrivo legame
			legamiType.setArrivoLegame(arrayArrivoLegame);


			// aggiungo IL LEGAME

			// Inizio Gestione Bibliografica: 05-11-2012 BUG Mantis (Collaudo) 5166
			// nel caso di variazione legame titoloUniforme/UniformeMusicale Autore veniva erroneamente inviato
			// il messaggio di modifica mentre si deve inviare (come del caso di titoloDocumento) l'accoppiata
			// Cancella/Crea al fine di ottenere lo stesso risultato. a tal fine si richiama lo stesso oggetto addLegami
			// che ricompone in maniera corretta la sequenza dei messaggi da inviare.
			// Inizio Gestione Bibliografica: 05-12-2014 BUG Mantis (Esercizio) 5673
			// Si deve inserire il controllo su areaDatiPass.getAuthorityOggettoArrivo() diverso da null perchè
			// il legame da variare con il titolo uniforme potrebbe essere con una natura D che ovviamente avrebbe null nel campo
			// authority e D nel campo natura;
			// if (areaDatiPass.getAuthorityOggettoArrivo().equals("AU")) {
			if (ValidazioneDati.in(areaDatiPass.getAuthorityOggettoArrivo(), "AU")) {

				// Inizio Intervento eseguito su richiesta di   su Comunicazione di Elsa Adducci 05.02.2013
				// nel caso di legame fra ENTI (5XX) il campo arrayLegamiType è null e deve quindi essere valorizzato
//				elementAutType.setLegamiElementoAut(arrayLegamiType);
				if (arrayLegamiType == null) {
					arrayLegamiType = new LegamiType[1];
					arrayLegamiType[0] = legamiType;
					elementAutType.setLegamiElementoAut(arrayLegamiType);
				} else {
					elementAutType.setLegamiElementoAut(arrayLegamiType);
				}
				// Fine Intervento eseguito su richiesta di   su Comunicazione di Elsa Adducci 05.02.2013
			} else {
				arrayLegamiType = new LegamiType[1];
				arrayLegamiType[0] = legamiType;
				elementAutType.setLegamiElementoAut(arrayLegamiType);
			}
			// Fine Gestione Bibliografica: 05-11-2012 BUG Indice 5166

			// FINE FORMA ACCETTATA
			modifica.setElementoAut(elementAutType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setModifica(modifica);



			// Inizio Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita
			if (areaDatiPass.getTipoOperazione() != null) {
				if (areaDatiPass.getTipoOperazione().equals("Condividi")) {

					String verIndice = "";
					String verPolo = "";

					// Inserimento del legame in Indice
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerInd");
						return areaDatiPassReturn;
					}

					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
						return areaDatiPassReturn;
					}

					// Valorizzazione del campo versione Indice con la risposta appena ottenuta

					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
						verIndice = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT005();
					}

					// Aggiornamento del legame in Polo per il flag di condivisione ( da n a s)
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(areaDatiPass.getTimeStampBidPartenzaPolo());
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAut(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAut(0).getArrivoLegame(0).getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);

					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerPol");
						return areaDatiPassReturn;
					}
					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
						return areaDatiPassReturn;
					}

					// Valorizzazione del campo versione Indice con la risposta appena ottenuta

					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
						verPolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT005();
					}

					areaDatiPassReturn.setCodErr("0000");
					areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());
					areaDatiPassReturn.setVersioneIndice(verIndice);
					areaDatiPassReturn.setVersionePolo(verPolo);

					return areaDatiPassReturn;

				}
			}
			// Fine Controllo sul tipo operazione perchè l'operazione di condividi in Indice va gestita


			if (areaDatiPass.isInserimentoIndice()) {
				// Inserimento del legame in Indice
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
			}

			// Inserimento del legame in Polo Solo se l'oggetto è già presente
			// altrimenti bisona prima
			// effettuarne la cattura e poi il legame.
			// sequenza operazioni:
			// interrogazione localizzazioni bid arrivo del legame
			// se oggetto assente per il polo (cattura -creazione legame)
			// se oggetto presente per il polo (localizazione dell'oggetto sia
			// in I che in P poi -creazione legame)
			// se oggetto localizzato per la biblio (creazione legame)

			// Ricerca del time stamp sul DB di Polo
			String timeStampPolo = "";

			if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {
				//===========================================================
				// Inizio verifica di esistenza del Autore su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
				areaDatiControlliPoloVO.setTipoAut("AU");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				} else {
					timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(timeStampPolo);
				}

				// Fine verifica di esistenza del Autore su Polo
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("MA")) {
				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
				areaDatiControlliPoloVO.setTipoAut("MA");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				} else {
					timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
					sbnmessage.getSbnRequest().getModifica()
							.getElementoAut().getDatiElementoAut().setT005(
									timeStampPolo);
				}
				// Fine verifica di esistenza del Documento su Polo
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("TU")
					|| areaDatiPass.getAuthorityOggettoPartenza().equals("UM")) {

				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
				areaDatiControlliPoloVO.setTipoAut(areaDatiPass.getAuthorityOggettoPartenza());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				} else {
					timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(timeStampPolo);
				}
				// Fine verifica di esistenza del Documento su Polo
			}

			if (areaDatiPass.getAuthorityOggettoArrivo() == null) {
				areaDatiPass.setAuthorityOggettoArrivo("");
			}

			if (areaDatiPass.getAuthorityOggettoArrivo().equals("AU")) {
				//===========================================================================
				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("AU");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				//===========================================================================

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					// non esiste l'oggetto: va prima catturato
					AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
					areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdArrivo());
					areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getAuthorityOggettoArrivo());
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaAutore(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					} else {
						// siamo nel caso di legame AUTORE-AUTORE dopo aver catturato l'autore è stato
						// autometicamente portato in locale anche il nuovo legame; non è quindi necessario
						// replicare la creazione del legame.
						if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {
							return areaDatiPassReturn;
						}
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("MA")) {

				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("MA");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					// non esiste l'oggetto: va prima catturato
					AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
					areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdArrivo());
					areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getAuthorityOggettoArrivo());
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority
							.catturaMarca(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					} else {
						// siamo nel caso di legame MARCA-AUTORE dopo aver catturato l'autore è stato
						// autometicamente portato in locale anche il nuovo legame; non è quindi necessario
						// replicare la creazione del legame.
						if (areaDatiPass.getAuthorityOggettoPartenza().equals("AU")) {
							return areaDatiPassReturn;
						}
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("TU")
					|| areaDatiPass.getAuthorityOggettoPartenza().equals("UM")) {
				// Febbraio 2018 - correzione in caso di creazione legame A verso A se l'oggetto di arrivo è
				// assente da Base Dati di Polo e quindi va prima catturato
				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut(areaDatiPass.getAuthorityOggettoArrivo());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					// non esiste l'oggetto: va prima catturato
					AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
					areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdArrivo());
					areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getAuthorityOggettoArrivo());

					// RICONTROLLARE LA CHIAMATA catturaReticolo
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority
							.catturaReticolo(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("")) {

				// Inizio verifica di esistenza del Documento su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				if (areaDatiPass.getNaturaBidArrivo().equals("B")
						|| areaDatiPass.getNaturaBidArrivo().equals("D")
						|| areaDatiPass.getNaturaBidArrivo().equals("P")
						|| areaDatiPass.getNaturaBidArrivo().equals("T")) {
					areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);
				}

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getDatiDocType() == null) {
					// non esiste l'oggetto: va prima catturato
					AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
					areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdArrivo());
					areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getAuthorityOggettoArrivo());
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaReticolo(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			}

			this.polo.setMessage(sbnmessage,this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPassReturn.setCodErr("noServerLoc");
				return areaDatiPassReturn;
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("3030")) {
				sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAut(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);

				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				}
			}


			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito());
				return areaDatiPassReturn;
			}
			areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}

	/**
	 * Questo metodo crea un oggetto Castor pronto per fare una richiesta per
	 * Vid
	 *
	 * @param Vid:
	 *            Vid Dell'autore.
	 *
	 * @return Oggetto Castor
	 */
	/**
	 * DOCUMENT ME!
	 *
	 * @param bid
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutoriCollegati(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		try {


			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 003
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1

			CercaDatiAutType cercaDatiAutType = new CercaDatiAutType();
			cercaDatiAutType.setTipoAuthority(SbnAuthority.AU);

			ArrivoLegame arrivoLegame = new ArrivoLegame();

			switch (areaDatiPass.getTipoOggetto()) {
			case TitoliCollegatiInvoke.AUTORI_COLLEGATI_A_MARCA:
				LegameElementoAutType legameElemento = new LegameElementoAutType();
				legameElemento.setTipoAuthority(SbnAuthority.MA);
				legameElemento.setTipoLegame(SbnLegameAut.valueOf("921"));
				legameElemento.setIdArrivo(areaDatiPass.getOggDiRicerca());
				arrivoLegame.setLegameElementoAut(legameElemento);
				break;
			case TitoliCollegatiInvoke.AUTORI_COLLEGATI_A_TITOLO:
				String NaturaDaLista = areaDatiPass.getNaturaTitBase();
				String TMaterialeDaLista = areaDatiPass.getTipMatTitBase();
				if ((NaturaDaLista.toUpperCase().equals("A"))
						&& (TMaterialeDaLista.toUpperCase().equals("U"))) {
					LegameElementoAutType legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setTipoAuthority(SbnAuthority.UM);
					legameElementoAut.setIdArrivo(areaDatiPass.getOggDiRicerca());
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("tutti"));
					arrivoLegame.setLegameElementoAut(legameElementoAut);

				} else if ((NaturaDaLista.toUpperCase().equals("A"))
						&& (TMaterialeDaLista.toUpperCase().equals(""))) {
					LegameElementoAutType legameElementoAut = new LegameElementoAutType();
					legameElementoAut.setTipoAuthority(SbnAuthority.TU);
					legameElementoAut.setIdArrivo(areaDatiPass.getOggDiRicerca());
					legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("tutti"));
					arrivoLegame.setLegameElementoAut(legameElementoAut);

				} else if ((NaturaDaLista.toUpperCase().equals("B"))
						|| (NaturaDaLista.toUpperCase().equals("D"))
						|| (NaturaDaLista.toUpperCase().equals("T"))
						|| (NaturaDaLista.toUpperCase().equals("P"))) {
					LegameTitAccessoType legameTitAccesso = new LegameTitAccessoType();
					legameTitAccesso.setIdArrivo(areaDatiPass.getOggDiRicerca());
					legameTitAccesso.setTipoLegame(SbnLegameTitAccesso.VALUE_0);
					arrivoLegame.setLegameTitAccesso(legameTitAccesso);
				} else if ((NaturaDaLista.toUpperCase().equals("M"))
						|| (NaturaDaLista.toUpperCase().equals("S"))
						|| (NaturaDaLista.toUpperCase().equals("C"))
						|| (NaturaDaLista.toUpperCase().equals("W"))
						|| (NaturaDaLista.toUpperCase().equals("F"))
						|| (NaturaDaLista.toUpperCase().equals("N"))) {
					LegameDocType legameElementoDoc = new LegameDocType();
					legameElementoDoc.setTipoLegame(SbnLegameDoc.valueOf("tutti"));
					legameElementoDoc.setIdArrivo(areaDatiPass.getOggDiRicerca());
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
				this.polo.setMessage(sbnmessage,this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					if (areaDatiPass.isRicercaIndice()) {
						this.indice.setMessage(sbnmessage,this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							areaDatiPassReturn.setNumNotizie(0);
							areaDatiPassReturn.setCodErr("noServerInd");
							return areaDatiPassReturn;
						} else {
							totRighe = sbnRisposta.getSbnMessage()
									.getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput()
									.getTotRighe();
							if (totRighe == 0) {
								areaDatiPassReturn.setNumNotizie(0);
								return areaDatiPassReturn;
							} else {
								areaDatiPassReturn.setLivelloTrovato("I");
							}
						}
					} else {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					}
				} else {
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getTotRighe();
					if (totRighe == 0) {
						if (areaDatiPass.isRicercaIndice()) {
							this.indice.setMessage(sbnmessage,this.user);
							sbnRisposta = this.indice.eseguiRichiestaServer();
							if (sbnRisposta == null) {
								areaDatiPassReturn.setNumNotizie(0);
								return areaDatiPassReturn;
							} else {
								totRighe = sbnRisposta.getSbnMessage()
										.getSbnResponse()
										.getSbnResponseTypeChoice()
										.getSbnOutput().getTotRighe();
								if (totRighe == 0) {
									areaDatiPassReturn.setNumNotizie(0);
									return areaDatiPassReturn;
								} else {
									areaDatiPassReturn.setLivelloTrovato("I");
								}
							}
						} else {
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						}
					} else {
						areaDatiPassReturn.setLivelloTrovato("P");
					}
				}
			} else if (areaDatiPass.isRicercaIndice()) {
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				} else {

					totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
					if (totRighe == 0) {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					} else {
						areaDatiPassReturn.setLivelloTrovato("I");
					}
				}
			}
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);
				listaSintentica.add(sinteticaAutoriVo);
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


	public TreeElementViewTitoli getReticoloAutore(SBNMarc sbnMarcType,
			TreeElementViewTitoli root, String chiamata, AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal) {

		myChiamata = chiamata;
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		// dimensione di ogni riga del allbero
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		// RADICE FORMA ACCETTATA
		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);

		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		setKeyCtr = 1;
		codSbnBiblioteca = areaDatiPassLocal.getCodiceSbn();

		// prendo il nominativo
		String Vid = datiElemento.getT001().trim();
		String nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
		String livello = null;

		root.setRigaReticoloCtr(setKeyCtr++);
		root.setKey(Vid);
		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
		root.setT005(datiElemento.getT005());
		root.setLivelloAutorita(elementoAut.getDatiElementoAut().getLivelloAut().toString());
		if (myChiamata.equals("P")) {
			root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			if (datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
				root.setFlagCondiviso(false);
			} else {
				root.setFlagCondiviso(true);
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
				areaDatiPassLocal.setIdLoc(Vid);
				areaDatiPassLocal.setAuthority("AU");
				oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
			}
			// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

			root.setLocalizzazione(oggettoLocalizzato);
		}
//		root.setText(Vid + " " + nominativo);
		root.setText(nominativo);
		root.setDescription(nominativo);


		//root.setTipoAuthority(null);
		root.setTipoAuthority(SbnAuthority.AU);


		// Inizio inserimento della gestione del dettaglio Autore
		//DettaglioOggetti dettOggetti = new DettaglioOggetti();
		root.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(
				dettOggetti.getDettaglioAutore(sbnMarcType, null, root, "", 0));
		// Fine inserimento della gestione del dettaglio Autore

		root.setIdNode(0);
		root.open();

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				int tipoLegame = sbnLegameAut.getType();
				String tipoLegameString = sbnLegameAut.toString();

				// ESCLUDO REPERTORI LEGATI ALL'AUTORE tipo legame 810 , tipo
				// legame 815 REPERTORI
				if (!(tipoLegameString.equals(AUT_TIPO_LEGAME_14))&& (!tipoLegameString.equals(AUT_TIPO_LEGAME_15))) {

					TreeElementViewTitoli nodoCorrente = (TreeElementViewTitoli) root.addChild();

					ElementAutType elementAutType = legameElemento.getElementoAutLegato();

					// VID del legame
					String vidLegato;

					// valore del nodo
					String valueDelNodo;
					String nominativoLegame;

					if (elementAutType != null) {

						datiElemento = elementAutType.getDatiElementoAut();
						livello = datiElemento.getLivelloAut().toString();

						number = number + 1;

						// ELEMENTO TROVATO: AutorePersonaleType
						if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {

							AutorePersonaleType autorePersonale = (AutorePersonaleType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(autorePersonale);

							// VID del legame
							vidLegato = autorePersonale.getT001();

							// valore del nodo
							valueDelNodo = nominativoLegame;

							// aggiungo un nodo al albero
							nodoCorrente.setRigaReticoloCtr(setKeyCtr++);
							nodoCorrente.setKey(vidLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(autorePersonale.getT005());
							nodoCorrente.setLivelloAutorita(autorePersonale.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (autorePersonale.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodoCorrente.setFlagCondiviso(false);
								} else {
									nodoCorrente.setFlagCondiviso(true);
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
									areaDatiPassLocal.setIdLoc(vidLegato);
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

							SbnFormaNome sbnForma = autorePersonale.getFormaNome();

							if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
								// METTO NEL VECTOR IL VID DELLA FORMA ACCETTATA
							} else {
								nodoCorrente.setAutoreFormaRinvio(true);
							}

							nodoCorrente.setIdNode(root.getIdNode() + 1);

							// Inizio inserimento della gestione del dettaglio Autore
							if (root.getTipoAuthority() != null) {
								if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
									tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
								} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
									tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
								}
							}
							nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(dettOggetti.getDettaglioAutore(
													sbnMarcType, null, nodoCorrente, "", tipoLegame));
							// Fine inserimento della gestione del dettaglio Autore

							// SE L'AUTORE TROVATO HA ALTRI LEGAMI, QUESTI VENGONO AGGIUNTI AL NODO SOLO SE SONO DI TIPO "4XX".
//							impostaNodoLegame4XX(elementAutType, root, number);
							impostaNodoLegame4XX(elementAutType, nodoCorrente, number, myChiamata, codSbnBiblioteca);

						}

						// ELEMENTO TROVATO: EnteType
						else if (elementAutType.getDatiElementoAut() instanceof EnteType) {

							EnteType enteType = (EnteType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(enteType);

							// VID del legame
							vidLegato = enteType.getT001();

							// valore del nodo
							valueDelNodo = nominativoLegame;

							// aggiungo un nodo al albero
							nodoCorrente.setRigaReticoloCtr(setKeyCtr++);
							nodoCorrente.setKey(vidLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(enteType.getT005());
							nodoCorrente.setLivelloAutorita(enteType.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (enteType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodoCorrente.setFlagCondiviso(false);
								} else {
									nodoCorrente.setFlagCondiviso(true);
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
									areaDatiPassLocal.setIdLoc(vidLegato);
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
							}

							nodoCorrente.setText(valueDelNodo);
							nodoCorrente.setDescription(nominativoLegame);
							nodoCorrente.setTipoAuthority(SbnAuthority.AU);
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null,null));

							SbnFormaNome sbnForma = enteType.getFormaNome();

							if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
							} else {
								nodoCorrente.setAutoreFormaRinvio(true);
							}

							nodoCorrente.setIdNode(root.getIdNode() + 1);

							// Inizio inserimento della gestione del dettaglio
							// Autore
							if (root.getTipoAuthority() != null) {
								if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
									tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
								} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
									tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
								}
							}
							nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(dettOggetti.getDettaglioAutore(
													sbnMarcType, null, nodoCorrente, "", tipoLegame));
							// Fine inserimento della gestione del dettaglio Autore


							// SE L'ENTE TROVATO HA ALTRI LEGAMI, QUESTI VENGONO AGGIUNTI AL NODO SOLO SE SONO DI TIPO "4XX".
							//impostaNodoLegame4XX(elementAutType, root, number);
							impostaNodoLegame4XX(elementAutType, nodoCorrente, number, myChiamata, codSbnBiblioteca);

						}

						// ELEMENTO TROVATO: MarcaType
						else if (elementAutType.getDatiElementoAut() instanceof MarcaType) {

							MarcaType marcaType = (MarcaType) elementAutType.getDatiElementoAut();

							nominativoLegame = marcaType.getT921().getA_921();

							String MIDLegato = marcaType.getT001();

							// valore del nodo
//							valueDelNodo = MIDLegato + IID_SPAZIO
//									+ nominativoLegame;
							valueDelNodo = nominativoLegame;

							// aggiungo un nodo al albero
							nodoCorrente.setRigaReticoloCtr(setKeyCtr++);
							nodoCorrente.setKey(MIDLegato);
							nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodoCorrente.setT005(marcaType.getT005());
							nodoCorrente.setLivelloAutorita(marcaType.getLivelloAut().toString());
							if (myChiamata.equals("P")) {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (marcaType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodoCorrente.setFlagCondiviso(false);
								} else {
									nodoCorrente.setFlagCondiviso(true);
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

							nodoCorrente.setLivelloAutorita(livello);

							nodoCorrente.setIdNode(root.getIdNode() + 1);
							// Inizio inseriemnto della gestione dettaglio marca
							tipoLegame = DatiLegame.LEGAME_AUTORE_MARCA;

							nodoCorrente.getAreaDatiDettaglioOggettiVO()
									.setDettaglioMarcaGeneraleVO(
											dettOggetti.getDettaglioMarca(
													sbnMarcType, nodoCorrente,
													"", tipoLegame));
							// Fine inserimento della gestione del dettaglio
							// Marca
						}

					}// end if (elementAutType != null)
				}
			}
		}

		return root;
	}// end setReticolo

	// /////////////////////////////////////////////////
	// METODO TIPICO PER GLI AUTORI
	/**
	 * Controlla se l'elemento in questione ha ulteriori legami di tipo "4XX".
	 * In caso positivo aggiunge un ulteriore nodo-figlio.
	 *
	 * @param elementoAut
	 *            Elemeno di authority da verificare
	 * @param nodo
	 *            Nodo da verificare
	 * @param tree
	 *            Albero
	 * @param number
	 *            id del nodo da verificare
	 */

	// almaviva2 15.12.2009 - BUG MANTIS ???? inserita variabile myChiamataVariabile
	public void impostaNodoLegame4XX(ElementAutType elementoAut,
			TreeElementViewTitoli nodo, int number, String myChiamataVariabile, String myCodSbnBibliotecaVariabile) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();

				String tipoLegameString = sbnLegameAut.toString();

				// SOLO SE IL LEGAME E' DI TIPO "4XX"
				if (tipoLegameString.equals(AUT_TIPO_LEGAME_1)) {

					TreeElementViewTitoli nodo2 = (TreeElementViewTitoli) nodo.addChild();

					ElementAutType elementAutType = legameElemento.getElementoAutLegato();
					String vidLegato;
					String valueDelNodo;
					String nominativoLegame;

					if (elementAutType != null) {
						DatiElementoType datiElemento = elementAutType.getDatiElementoAut();
						String livello = datiElemento.getLivelloAut().toString();
						number = number + 1;

						// ELEMENTO TROVATO: AutorePersonaleType
						if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {

							AutorePersonaleType autorePersonale = (AutorePersonaleType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(autorePersonale);
							vidLegato = autorePersonale.getT001();
							valueDelNodo = nominativoLegame;

							nodo2.setKey(vidLegato);
							nodo2.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodo2.setT005(autorePersonale.getT005());
							nodo2.setLivelloAutorita(autorePersonale.getLivelloAut().toString());
							if (myChiamataVariabile.equals("P")) {
								nodo2.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (autorePersonale.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodo2.setFlagCondiviso(false);
								} else {
									nodo2.setFlagCondiviso(true);
								}
							} else {
								nodo2.setFlagCondiviso(true);
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
									AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(myCodSbnBibliotecaVariabile);
									areaDatiPassLocal.setIdLoc(vidLegato);
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo2.setLocalizzazione(oggettoLocalizzato);
							}
							nodo2.setText(valueDelNodo);
							nodo2.setDescription(nominativoLegame);
							nodo2.setTipoAuthority(SbnAuthority.AU);
							nodo2.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));

							SbnFormaNome sbnForma = autorePersonale.getFormaNome();

							if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
							} else {
								nodo2.setAutoreFormaRinvio(true);
							}
						}

						// ELEMENTO TROVATO: EnteType
						else if (elementAutType.getDatiElementoAut() instanceof EnteType) {

							EnteType enteType = (EnteType) elementAutType.getDatiElementoAut();
							nominativoLegame = utilityCastor.getNominativoDatiElemento(enteType);
							vidLegato = enteType.getT001();
							valueDelNodo = nominativoLegame;
							nodo2.setKey(vidLegato);
							nodo2.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
							nodo2.setT005(enteType.getT005());
							nodo2.setLivelloAutorita(enteType.getLivelloAut().toString());
							if (myChiamataVariabile.equals("P")) {
								nodo2.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								if (enteType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodo2.setFlagCondiviso(false);
								} else {
									nodo2.setFlagCondiviso(true);
								}
							} else {
								nodo2.setFlagCondiviso(true);
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
									AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(myCodSbnBibliotecaVariabile);
									areaDatiPassLocal.setIdLoc(vidLegato);
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo2.setLocalizzazione(oggettoLocalizzato);
							}

							nodo2.setText(valueDelNodo);
							nodo2.setDescription(nominativoLegame);
							nodo2.setTipoAuthority(SbnAuthority.AU);
							nodo2.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null, null));

							SbnFormaNome sbnForma = enteType.getFormaNome();

							if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
							} else {
								nodo2.setAutoreFormaRinvio(true);
							}
						}

						// Inizio inserimento della gestione del dettaglio Autore
						//DettaglioOggetti dettOggetti = new DettaglioOggetti();
						nodo2.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(dettOggetti.getDettaglioAutore(
								null, elementAutType.getDatiElementoAut(), nodo2, "", DatiLegame.LEGAME_AUTORE_AUTORE));
						// Fine inserimento della gestione del dettaglio Autore
						// ============================

						nodo.setLivelloAutorita(livello);

					}// end if (elementAutType != null)
				}
			}
		}
	}

	// almaviva2 - gestioneBibliografica evolutiva x liste confronto Autori 04.05.2012
	// Nuovo metodo per la creazione di una sintetica autori arrichita dai primi 10 titoli collegati
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutoriPerSintArricchita(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass) {


		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		if (!areaDatiPass.isRicercaPolo() && !areaDatiPass.isRicercaIndice()) {
			areaDatiPassReturn.setCodErr("livRicObblig");
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
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setMaxRighe(10);

			cercaType.setCercaElementoAut(cercaElemento);
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
			CercaAutoreType cercaAutoreType = new CercaAutoreType();
			StringaCercaType s = new StringaCercaType();

			if (!areaDatiPass.getInterrGener().getVid().equals("")) {
				canali.setT001(areaDatiPass.getInterrGener().getVid().toUpperCase());
				cercaAutoreType.setCanaliCercaDatiAut(canali);
			}

			SbnTipoNomeAutore[] sbnTipoNomeVuoto = new SbnTipoNomeAutore[0];
			cercaAutoreType.setTipoNome(sbnTipoNomeVuoto);

			cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
			cercaElemento.setCercaDatiAut(cercaAutoreType);
			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			int totRighe = 0;

			if (areaDatiPass.isRicercaPolo()) {
				this.polo.setMessage(sbnmessage,this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
				if (totRighe == 0) {
					if (areaDatiPass.isRicercaIndice()) {
						this.indice.setMessage(sbnmessage,this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							areaDatiPassReturn.setCodErr("noServerInd");
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						}
						if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
								&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
							areaDatiPassReturn.setCodErr("9999");
							areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						}
						totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
						if (totRighe == 0) {
							areaDatiPassReturn.setCodErr("3001");
							areaDatiPassReturn.setNumNotizie(0);
							return areaDatiPassReturn;
						} else {
							areaDatiPassReturn.setLivelloTrovato("I");
						}
					} else {
						areaDatiPassReturn.setCodErr("3001");
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					}
				} else {
					areaDatiPassReturn.setLivelloTrovato("P");
				}
			} else if (areaDatiPass.isRicercaIndice()) {
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
				if (totRighe == 0) {
					areaDatiPassReturn.setCodErr("3001");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				} else {
					areaDatiPassReturn.setLivelloTrovato("I");
				}
			}

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();
			int progressivo = 0;

			SinteticaAutoriView sinteticaAutoriVo;
			List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();

			int tappoScorrimento = totRighe;
			if (maxRighe == 0) {
				tappoScorrimento = totRighe;
			} else if (totRighe > maxRighe) {
				tappoScorrimento = maxRighe;
			}


			SbnGestioneTitoliDao sbnGestioneTitoliDao = new SbnGestioneTitoliDao(indice, polo, user);
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturnTitoloReturnVO;

			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = creaElementoLista(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput(), t,
						progressivo);



				// richiamo a ricercaTitoli per le aree del titolo dei titoli collegati
				AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPassTitoloVO = new AreaDatiPassaggioInterrogazioneTitoloVO();

				areaDatiPassTitoloVO.clear();

				areaDatiPassTitoloVO.setOggDiRicerca(sinteticaAutoriVo.getVid());
				areaDatiPassTitoloVO.setTipoOggetto(1);
				areaDatiPassTitoloVO.setTipoOggettoFiltrato(99);
				if (areaDatiPass.isRicercaPolo())
					areaDatiPassTitoloVO.setRicercaPolo(true);

				if (areaDatiPass.isRicercaIndice())
					areaDatiPassTitoloVO.setRicercaIndice(true);

				// Modifica del 27.11.2012 Intervento Interno richiesto da Renoato durante test di RMR
				// Ordinamento per Nome per i titoli legati ad autore
				areaDatiPassTitoloVO.setTipoOrdinamSelez("2");

				areaDatiPassReturnTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
				areaDatiPassReturnTitoloReturnVO = sbnGestioneTitoliDao.ricercaTitoli(areaDatiPassTitoloVO);
				if (!areaDatiPassReturnTitoloReturnVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(areaDatiPassReturnTitoloReturnVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				// Bug Mantis Collaudo 0005007: almaviva2 Giugno 2014: Liste di confronto Autori
				// - Decodifica caratteri apostrofo e parentesi uncinate corretta inserendo un nuovo campo sulla
				// SinteticaTitoliView chiamato NomeTitColl; in questo campo vengono aggiornate tutte le informazioni
				// della sintetica senza passare dalla setNome che invece decodificava OGNI VOLTA questi caratteri speciali
				// alla fine della creazione stringa viene riportata una sla volta sulla setNome
				if (areaDatiPassReturnTitoloReturnVO.getTotRighe() > 0) {
					SinteticaTitoliView sinteticaTitoliVo;
					String stringaTitColl = "";
					int tappoScorrimentoColl = 10;
					if (areaDatiPassReturnTitoloReturnVO.getTotRighe() < 10) {
						tappoScorrimentoColl = areaDatiPassReturnTitoloReturnVO.getTotRighe();
					}

					for (int tColl = 0; tColl < tappoScorrimentoColl; tColl++) {
						sinteticaTitoliVo = (SinteticaTitoliView) areaDatiPassReturnTitoloReturnVO.getListaSintetica().get(tColl);
						stringaTitColl = sinteticaTitoliVo.getBid() +  " " + sinteticaTitoliVo.getTitolo();//sinteticaTitoliVo.getDescrizioneLegami();
						if (tColl == 0) {
							sinteticaAutoriVo.setNomeTitColl(sinteticaAutoriVo.getVid() + " " + sinteticaAutoriVo.getOriginal_nome() + "<br />-- " + stringaTitColl);
						} else {
							sinteticaAutoriVo.setNomeTitColl(sinteticaAutoriVo.getNomeTitColl() + "<br />-- " + stringaTitColl);
						}
					}
					sinteticaAutoriVo.setNome(sinteticaAutoriVo.getNomeTitColl());
					listaSintentica.add(sinteticaAutoriVo);
				} else {
					sinteticaAutoriVo.setNome(sinteticaAutoriVo.getVid() + " " + sinteticaAutoriVo.getOriginal_nome());
					listaSintentica.add(sinteticaAutoriVo);
				}
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
}
