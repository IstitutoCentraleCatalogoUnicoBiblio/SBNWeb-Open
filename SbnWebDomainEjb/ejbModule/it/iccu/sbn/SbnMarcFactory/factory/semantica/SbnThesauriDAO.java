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

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneAllAuthorityDao;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
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
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
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
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO.LegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloTermineVO.LegameTitoloTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.LegameTitoloAuthSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaDescrittoreVO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Interfaccia in diretta
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
 * Funzioni per la creazione e parsing di alberi dom castor relativi alle
 * interrogazioni sui Soggetti. Utilizza la classe XMLFactory per scambiare
 * flussi XML con il server sbn, il formato dei flussi XML scambiati rispetta lo
 * schema XSD del protocollo SBN-MARC, tale schema è rappresentato mediante un
 * object model generato con Castor. Le classi che realizzano i frame ed i
 * pannelli dell'interfaccia grafica per l'area Soggetti utilizzano XMLSoggetti
 * per effettuare interrogazioni e modifiche sui dati dei Soggetti.
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
public class SbnThesauriDAO {

	public static boolean IS_CONFERMA_RICERCA_DIFFERITA = false;

	private static Logger log = Logger.getLogger(SbnThesauriDAO.class);
	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;

	public SbnThesauriDAO(FactorySbn indice, FactorySbn polo, SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public static boolean strIsNull(String data) {
		return (data == null) || (data.trim().equals(""));
	}

	/**
	 * Ottiene un'altro blocco della lista sintetica dei descrittori.
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc getNextBloccoTermini(RicercaThesauroListaVO areaDatiPass) {

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

			cercaSogDes.setTipoAuthority(SbnAuthority.TH);
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

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (IllegalArgumentException ie) {
			// Errore nella composizione della data.
			log.error("", ie);
		}

		return sbnRisposta;

	}// end getNextBloccoTermini

	public SBNMarc ricercaThesauro(ThRicercaComuneVO parametriRicerca) {

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElementoAutType = new CercaElementoAutType();
			CercaDatiAutType cercaDatiAut = new CercaDatiAutType();
			cercaDatiAut.setTipoAuthority(SbnAuthority.TH);
			CercaSoggettoDescrittoreClassiReperType cercaThesauroType = new CercaSoggettoDescrittoreClassiReperType();
			cercaThesauroType.setTipoAuthority(SbnAuthority.TH);

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(Integer.valueOf(parametriRicerca
					.getElemBlocco()));

			StringaCercaType strCerca = new StringaCercaType();
			StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
			CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

			TipoOrdinamento ordinamento = parametriRicerca
					.getOrdinamentoDescrittore();
			switch (ordinamento) {
			case PER_ID:
				// ordina per did
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				break;
			case PER_TESTO:
				// ordina per testo
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
				break;
			case PER_DATA:
				// ordina per data var./ins. + Testo
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
				break;
			}

			cercaDatiAut.setTipoAuthority(SbnAuthority.TH);

			if (!SbnThesauriDAO.strIsNull(parametriRicerca.getCodThesauro()))
				cercaThesauroType.setC2_250(parametriRicerca.getCodThesauro());

				// CANALE 1: DID
				if (!strIsNull(parametriRicerca.getRicercaThesauroDescrittore()
						.getDid())) {
					CanaliCercaDatiAutType canaliCercaDatiAutType = new CanaliCercaDatiAutType();
					canaliCercaDatiAutType.setT001(parametriRicerca
							.getRicercaThesauroDescrittore().getDid());
					cercaThesauroType
							.setCanaliCercaDatiAut(canaliCercaDatiAutType);
				} else {
					String testoTermine = parametriRicerca.getRicercaThesauroDescrittore().getTestoDescr();
					if (!strIsNull(testoTermine)) {

						switch (parametriRicerca.getRicercaTipo() ) {
						case STRINGA_INIZIALE:
							// cerca per stringa iniziale
							strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaLike(testoTermine);
							canali.setStringaCerca(strCerca);
							cercaThesauroType.setCanaliCercaDatiAut(canali);
							break;

						case STRINGA_ESATTA:
							// cerca per stringa esatta
							strCerca.setStringaCercaTypeChoice(stringaTypeChoice);
							stringaTypeChoice.setStringaEsatta(testoTermine);
							canali.setStringaCerca(strCerca);
							cercaThesauroType.setCanaliCercaDatiAut(canali);
							break;

						case PAROLE:
							// cerca per parole di soggetto
							String testoNormalizzato = (new OrdinamentoUnicode() ).convert(testoTermine);
							String[] parole = testoNormalizzato.split("\\s+"); //splitto sugli spazi
							if (parole.length > 4) { // max 4 parole
								String[] tmp = new String[4];
								System.arraycopy(parole, 0, tmp, 0, 4);
								parole = tmp;
							}
							cercaThesauroType.setParoleAut(parole);
							break;
						}

					} else
					// CANALE 3: PAROLE
					if (parametriRicerca.getRicercaThesauroDescrittore().count() > 0) {
						cercaThesauroType
								.setParoleAut(preparaArrayParole(parametriRicerca
										.getRicercaThesauroDescrittore()));

					} else
						return null;
				}
			//}

			cercaElementoAutType.setCercaDatiAut(cercaThesauroType);

			cercaType.setCercaElementoAut(cercaElementoAutType);
			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (parametriRicerca.isPolo()) {
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
	} // ricercaThesauro

	private String[] preparaArrayParole(ThRicercaDescrittoreVO params) {

		List<String> tmp = new ArrayList<String>();
		if (!strIsNull(params.getParole0()))
			tmp.add(params.getParole0());
		if (!strIsNull(params.getParole1()))
			tmp.add(params.getParole1());
		if (!strIsNull(params.getParole2()))
			tmp.add(params.getParole2());
		if (!strIsNull(params.getParole3()))
			tmp.add(params.getParole3());

		int size = tmp.size();
		if (size > 0)
			return tmp.toArray(new String[size]);

		return null;
	}

	public SBNMarc ricercaTerminiPerBidCollegato(boolean livelloPolo,
			String bid, String codThesauro, int elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
			bid = bid.toUpperCase();

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

			cercaDatiAutType.setTipoAuthority(SbnAuthority.TH);
			if (!strIsNull(codThesauro))
				cercaDatiAutType.setC2_250(codThesauro);

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
	} // end ricercaTerminiPerBidCollegato

	public SBNMarc gestioneTermineThesauro(CreaVariaTermineVO termine) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			TermineType termineType = new TermineType();
			ElementAutType elementAutType = new ElementAutType();
			// TIPO AUTHORITY
			termineType.setTipoAuthority(SbnAuthority.TH);

			// Default DID
			if (strIsNull(termine.getDid()))
				termineType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);
			else
				termineType.setT001(termine.getDid());

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			termineType.setLivelloAut(SbnLivello.valueOf(termine
					.getLivelloAutorita()));
			termineType.setFormaNome(SbnFormaNome.valueOf(termine
					.getFormaNome()));

			A935 a935 = new A935();
			a935.setA_935(termine.getTesto());
			a935.setB_935(termine.getNote());
			a935.setC2_935(termine.getCodThesauro());

			termineType.setT935(a935);
			elementAutType.setDatiElementoAut(termineType);

			if (termine.isLivelloPolo())
				if (termine.isCondiviso())
					termineType.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					termineType.setCondiviso(DatiElementoTypeCondivisoType.N);

			switch (termine.getOperazione()) {
			case CREA:
				CreaType crea = new CreaType();
				CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
				crea.setTipoControllo(SbnSimile.SIMILE);
				crea.setCattura(termine.isCattura());
				creaTypeChoice.setElementoAut(elementAutType);
				crea.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(crea);
				break;

			case MODIFICA:
				ModificaType modifica = new ModificaType();
				modifica.setTipoControllo(SbnSimile.SIMILE);
				termineType.setT005(termine.getT005());
				termineType.setStatoRecord(StatoRecord.C);
				modifica.setElementoAut(elementAutType);
				sbnrequest.setModifica(modifica);
				break;

			case CANCELLA:
				CancellaType cancellaType = new CancellaType();
				SbnOggetto sbnOggetto = new SbnOggetto();
				sbnOggetto.setTipoAuthority(SbnAuthority.TH);
				cancellaType.setTipoOggetto(sbnOggetto);
				cancellaType.setIdCancella(termine.getDid());
				sbnrequest.setCancella(cancellaType);
				break;
			}

			sbnmessage.setSbnRequest(sbnrequest);

			if (termine.isLivelloPolo()) {
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
	} // end creaTermineThesauro

	public SBNMarc gestioneLegameTermini(DatiLegameTerminiVO legameDaCreare,
			SbnTipoOperazione tipoOperazione) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			TermineType termine = new TermineType();
			LegamiType legami = new LegamiType();
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legame = new LegameElementoAutType();

			ElementAutType elementAutType = new ElementAutType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			termine.setTipoAuthority(SbnAuthority.TH);

			DettaglioTermineThesauroVO did1 = legameDaCreare.getDid1();
			DettaglioTermineThesauroVO did2 = legameDaCreare.getDid2();

			// Default DID
			termine.setT001(did1.getDid());
			termine.setFormaNome(SbnFormaNome.valueOf(did1.getFormaNome()));
			termine.setT005(did1.getT005());

			if (legameDaCreare.isLivelloPolo()) {
				if (did1.isCondiviso())
					termine.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					termine.setCondiviso(DatiElementoTypeCondivisoType.N);
			}

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			termine.setLivelloAut(SbnLivello.valueOf(did1.getLivAut()));

			elementAutType.setDatiElementoAut(termine);

			legame.setTipoAuthority(SbnAuthority.TH);
			legame.setTipoLegame(SbnLegameAut.valueOf(legameDaCreare
					.getTipoLegame().toString()));
			legame.setIdArrivo(did2.getDid());
			legame.setNoteLegame(legameDaCreare.getNoteLegame());

			arrivoLegame.setLegameElementoAut(legame);
			legami.setIdPartenza(did1.getDid());
			legami.addArrivoLegame(arrivoLegame);

			legami.setTipoOperazione(tipoOperazione);

			elementAutType.addLegamiElementoAut(legami);
			modifica.setElementoAut(elementAutType);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (legameDaCreare.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea legame termini: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame termini: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end gestioneLegameTermini

	public SBNMarc gestioneLegameTitoloTerminiClassi(DatiLegameTitoloTermineVO input) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = new ModificaType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.SIMILE);

			DatiDocType datiDocType = new DatiDocType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(input.getBidNatura()));
			//almaviva5_20130520 #5316
			String tipoMateriale = input.getBidTipoMateriale();
			datiDocType.setTipoMateriale(ValidazioneDati.isFilled(tipoMateriale) ? SbnMateriale.valueOf(tipoMateriale) : null);
			datiDocType.setT001(input.getBid().toUpperCase());
			datiDocType.setT005(input.getT005());
			datiDocType.setLivelloAutDoc(SbnLivello.valueOf(input.getBidLivelloAut()));
			documentoTypeChoice.setDatiDocumento(datiDocType);

			DocumentoType documentoType = new DocumentoType();
			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(this.indice, this.polo, this.user);
			AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
			areaDatiControlliPoloVO.setIdRicerca(input.getBid());
			areaDatiControlliPoloVO.setTipoAut("");
			areaDatiControlliPoloVO.setCancellareInferiori(false);
			areaDatiControlliPoloVO.setCodErr("");

			gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);
			if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
				throw new Exception("Documento non presente sulla base dati di Polo");
			}

			DatiDocType datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();

			if (datiDocTypePolo == null)
				throw new Exception("Documento non presente sulla base dati di Polo");

			String rightT005 = datiDocTypePolo.getT005();
			datiDocType.setT005(rightT005);

			LegamiType legami = new LegamiType();
			legami.setIdPartenza(input.getBid().toUpperCase());

			for (LegameTitoloAuthSemanticaVO lts : input.getLegami()) {
				ArrivoLegame arrivoLegame = new ArrivoLegame();
				LegameElementoAutType legame = new LegameElementoAutType();
				legame.setNoteLegame(lts.getNotaLegame());
				legame.setTipoAuthority(lts.getAuthority());
				arrivoLegame.setLegameElementoAut(legame);
				legami.addArrivoLegame(arrivoLegame);

				//almaviva5_20111025 evolutive CFI
				switch (lts.getAuthority().getType()) {
				case SbnAuthority.TH_TYPE:
					LegameTitoloTermineVO legameTh = (LegameTitoloTermineVO) lts;
					legame.setTipoLegame(SbnLegameAut.valueOf("606")); // ????
					legame.setIdArrivo(legameTh.getDid());
					break;

				case SbnAuthority.CL_TYPE:
					LegameTitoloClasseVO legameCl = (LegameTitoloClasseVO) lts;
					String idArrivo = legameCl.getIdentificativoClasse();
					legame.setIdArrivo(idArrivo);
					if (ClassiUtil.isT001Dewey(idArrivo))
						legame.setTipoLegame(SbnLegameAut.valueOf("676"));
					else
						legame.setTipoLegame(SbnLegameAut.valueOf("686"));

					break;
				}
			}


			switch (input.getOperazione()) {
			case CREA:
				legami.setTipoOperazione(SbnTipoOperazione.CREA);
				break;
			case MODIFICA:
				legami.setTipoOperazione(SbnTipoOperazione.MODIFICA);
				break;
			case CANCELLA:
				legami.setTipoOperazione(SbnTipoOperazione.CANCELLA);
			}


			documentoType.addLegamiDocumento(legami);
			modifica.setDocumento(documentoType);
			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (input.isLivelloPolo()) {
				modifica.setCattura(true);
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
	}// end gestioneLegameTitoloTermini

	public List<ElementoSinteticaThesauroVO> creaListaSinteticaThesauro(
			SBNMarc response) {
		List<ElementoSinteticaThesauroVO> listaSintentica = null;

		SbnMessageType sbnMessage = response.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		int numeroElementi = sbnOutPut.getElementoAutCount();
		if (numeroElementi < 1) {
			return new ArrayList<ElementoSinteticaThesauroVO>();
		}

		int progressivo = sbnOutPut.getNumPrimo() - 1; // 0;
		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		if (SBNMarcUtil.eqAuthority(datiElemento.getTipoAuthority(), SbnAuthority.TH) ) {
			listaSintentica = new ArrayList<ElementoSinteticaThesauroVO>();
			for (int i = 0; i < numeroElementi; i++) {
				++progressivo;
				ElementoSinteticaThesauroVO thes = creaElementoListaThesauro(
						sbnOutPut, i, progressivo);
				listaSintentica.add(thes);
			}
		}

		return listaSintentica;
	}

	public ElementoSinteticaThesauroVO creaElementoListaThesauro(
			SbnOutputType sbnOutPut, int elementIndex, int progressivo) {
		ElementoSinteticaThesauroVO thes = new ElementoSinteticaThesauroVO();

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		TermineType thesauroType = (TermineType) datiElemento;
		A935 a935 = thesauroType.getT935();

		// PROGRESSIVO: Numero dell'elemento
		thes.setProgr(progressivo);

		// DID: Identificativo dell'elemento
		thes.setDid(datiElemento.getT001().trim());

		thes.setFormaNome(thesauroType.getFormaNome().toString());
		thes.setCondiviso(thesauroType.getCondiviso().getType() ==
				DatiElementoTypeCondivisoType.S_TYPE);
		thes.setLivelloAutorita(thesauroType.getLivelloAut().toString());
		thes.setNumTitoliBiblio(thesauroType.getNum_tit_coll_bib());
		thes.setNumTitoliPolo(thesauroType.getNum_tit_coll());
		thes.setNumTerminiCollegati(0);

		// STRINGA DI Thesauro
		thes.setTermine(a935.getA_935());
		thes.setCodThesauro(a935.getC2_935());

		return thes;

	}// end creaElementoListaThesauro

	public SBNMarc fondiTerminiThesauro(boolean livelloPolo,
			DatiFusioneTerminiVO datiFusione) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.TH);
			fonde.setTipoOggetto(oggetto);
			fonde.setIdPartenza(datiFusione.getDid1().getDid());
			fonde.setIdArrivo(datiFusione.getDid2().getDid());

			String[] listaID = datiFusione.getSpostaID();
			if (listaID != null && listaID.length > 0 )
				fonde.setSpostaID(listaID);

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
			log.error("ERROR Fondi termini: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Fondi termini: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end fondiTerminiThesauro


	/**
	 * Questo metodo crea un oggetto Castor pronto per fare una richiesta per
	 * Did
	 *
	 * @param Did:
	 *            Did del Soggetto
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc creaAnaliticaThesauroPerDid(boolean livelloPolo, String Did) {

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
			cercaSogDesc.setTipoAuthority(SbnAuthority.TH);
			cercaElemento.setCercaDatiAut(cercaSogDesc);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (IllegalArgumentException ie) {
			// Errore nella composizione della data.
			log.error("", ie);
		}

		return sbnRisposta;
	} // end creaAnaliticoThesauroPerDid

	public SBNMarc ricercaTerminiPerDidCollegato(boolean livelloPolo,
			String did, int elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
			did = did.toUpperCase();

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

			CercaDatiAutType cercaDatiAutType = new CercaDatiAutType();

			cercaDatiAutType.setTipoAuthority(SbnAuthority.TH);

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legameElemento = new LegameElementoAutType();

			legameElemento.setTipoAuthority(SbnAuthority.TH);

			// legame Soggetto-Descrittore: 931
			legameElemento.setTipoLegame(SbnLegameAut.valueOf("tutti"));

			legameElemento.setIdArrivo(did);
			arrivoLegame.setLegameElementoAut(legameElemento);

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


	public SBNMarc gestioneLegameTermineClasse(DatiLegameTermineClasseVO input) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = new ModificaType();

			modifica.setTipoControllo(SbnSimile.CONFERMA);

			TermineType termine = new TermineType();
			termine.setTipoAuthority(SbnAuthority.TH);
			termine.setT001(input.getDid());
			termine.setT005(input.getT005());
			termine.setLivelloAut(SbnLivello.valueOf(input.getLivAut()));

			LegamiType legami = new LegamiType();
			legami.setIdPartenza(input.getDid());

			for (LegameTermineClasseVO datiLegame : input.getLegami()) {
				ArrivoLegame arrivoLegame = new ArrivoLegame();
				LegameElementoAutType legame = new LegameElementoAutType();
				legame.setTipoAuthority(SbnAuthority.CL);
				legame.setTipoLegame(SbnLegameAut.valueOf("941")); // ????
				legame.setIdArrivo(datiLegame.getIdentificativo());
				legame.setNoteLegame(datiLegame.getNotaLegame());
				legame.setRank(datiLegame.getRank() );
				arrivoLegame.setLegameElementoAut(legame);
				legami.addArrivoLegame(arrivoLegame);
			}

			switch (input.getOperazione()) {
			case CREA:
				legami.setTipoOperazione(SbnTipoOperazione.CREA);
				break;
			case MODIFICA:
				legami.setTipoOperazione(SbnTipoOperazione.MODIFICA);
				break;
			case CANCELLA:
				legami.setTipoOperazione(SbnTipoOperazione.CANCELLA);
			}

			ElementAutType aut = new ElementAutType();
			aut.setDatiElementoAut(termine);
			aut.addLegamiElementoAut(legami);
			modifica.setElementoAut(aut);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

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

	}

}
