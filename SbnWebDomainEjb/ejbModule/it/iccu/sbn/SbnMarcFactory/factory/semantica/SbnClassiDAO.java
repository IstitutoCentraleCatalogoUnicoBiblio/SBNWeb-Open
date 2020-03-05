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
import it.iccu.sbn.SbnMarcFactory.factory.bibliografica.SbnGestioneTitoliDao;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseTypeChoice;
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
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
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
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.LegameTitoloAuthSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SbnClassiDAO {

	private static Logger log = Logger.getLogger(SbnClassiDAO.class);
	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;

	public SbnClassiDAO(FactorySbn indice, FactorySbn polo, SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;

		//log.debug("Istanzio la classe DAO per le classi SbnClassiDao");
	}

	public static final String SEPARATORE_TERMINI = "\\s-\\s";

    private static final String EDIZIONE_NON_DEWEY = "  ";

	public SBNMarc cercaClassi(RicercaClassiVO datiRicerca) {

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaSoggettoDescrittoreClassiReperType cercaClasseType = new CercaSoggettoDescrittoreClassiReperType();
			CercaElementoAutType cercaElementoAutType = new CercaElementoAutType();

			cercaClasseType.setTipoAuthority(SbnAuthority.CL);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);

			if (datiRicerca.getElemBlocco()!= null)
				cercaType.setMaxRighe(Integer.parseInt(datiRicerca.getElemBlocco()));

			// tipoOrd
			TipoOrdinamentoClasse ordinamento = datiRicerca.getOrdinamentoClasse();
			switch (ordinamento) {
			case PER_ID:
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				break;
			case PER_TESTO:
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_1);
				break;
			case PER_DATA:
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);
				break;
			default:
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				break;
			}

			// IMPOSTAZIONE CANALI PRIMARI
			// CANALE 1: SISTEMA EDIZIONE SIMBOLO
			if (!ValidazioneDati.strIsNull(datiRicerca.getIdentificativoClasse())) {
				CanaliCercaDatiAutType canaliCercaDatiAutType = new CanaliCercaDatiAutType();
				canaliCercaDatiAutType.setT001(datiRicerca
						.getIdentificativoClasse());
				cercaClasseType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
			} else
			// CANALE 2: SISTEMA SIMBOLO
			if (!ValidazioneDati.strIsNull(datiRicerca.getSimbolo())) {
				CanaliCercaDatiAutType canaliCercaDatiAutType = new CanaliCercaDatiAutType();
				StringaCercaType stringaCercaType = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				stringaCercaType.setStringaCercaTypeChoice(stringaTypeChoice);
				stringaTypeChoice.setStringaLike(datiRicerca.getSimbolo());
				canaliCercaDatiAutType.setStringaCerca(stringaCercaType);
				cercaClasseType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
				cercaClasseType.setSistemaClassificazione(datiRicerca
						.getCodSistemaClassificazione());
			} else
			// CANALE 3: PAROLE
			if (!ValidazioneDati.strIsNull(datiRicerca.getParole())) {
				//almaviva5_20130114 segnalazione ICCU: la ricerca in indice per parole non ammette punteggiatura
				String[] parole = SemanticaUtil.normalizzaParolePerRicerca(datiRicerca.getParole());//datiRicerca.getParole().toUpperCase();
				cercaClasseType.setParoleAut(parole);
				cercaClasseType.setSistemaClassificazione(datiRicerca.getCodSistemaClassificazione());
			} else
				return null;

//			// FILTRO LIVELLO AUTORITA
//			if (!ValidazioneDati.strIsNull(datiRicerca.getLivelloAutoritaDa())) {
//				cercaClasseType.setLivelloAut_Da(SbnLivello.valueOf(datiRicerca
//						.getLivelloAutoritaDa()));
//
//			}
//
//			if (!ValidazioneDati.strIsNull(datiRicerca.getLivelloAutoritaA())) {
//				cercaClasseType.setLivelloAut_A(SbnLivello.valueOf(datiRicerca
//						.getLivelloAutoritaA()));
//
//			}

			// FILTRO EDIZIONE
			String edizione = datiRicerca.getCodEdizioneDewey();
			if (!ValidazioneDati.strIsNull(edizione))
				cercaClasseType.setV_676(edizione);

			cercaElementoAutType.setCercaDatiAut(cercaClasseType);
			cercaType.setCercaElementoAut(cercaElementoAutType);
			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (datiRicerca.isPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;
	}

	/**
	 * Ottiene un'altro blocco della lista sintetica delle Classi dei Titoli.
	 *
	 * @return Oggetto Castor
	 */
	public SBNMarc getNextBloccoClassi(SBNMarcCommonVO richiesta) {

		SBNMarc sbnRisposta = null;

		int numPrimo = richiesta.getNumPrimo();
		int maxRighe = richiesta.getMaxRighe();
		String idLista = richiesta.getIdLista();

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaSoggettoDescrittoreClassiReperType cercaClasse = new CercaSoggettoDescrittoreClassiReperType();

			cercaClasse.setTipoAuthority(SbnAuthority.CL);
			cercaElemento.setCercaDatiAut(cercaClasse);

			// tipoOutput sintetica
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(numPrimo);
			cercaType.setMaxRighe(maxRighe);
			cercaType.setIdLista(idLista);
			log.debug("idLista: " + idLista);

			cercaType.setCercaElementoAut(cercaElemento);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			if (richiesta.isLivelloPolo()) {
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

	}// end getNextBloccoClassi

	public SBNMarc ricercaClassiPerBidCollegato(boolean livelloPolo,
			String bid, String sistemaClassificazione, String deweyEd,
			int elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
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
			cercaDatiAutType.setTipoAuthority(SbnAuthority.CL);
			if (!ValidazioneDati.strIsNull(sistemaClassificazione))
				cercaDatiAutType.setSistemaClassificazione(sistemaClassificazione);
			if (!ValidazioneDati.strIsNull(deweyEd))
				cercaDatiAutType.setV_676(deweyEd);

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
	} // end ricercaClassiPerBidCollegato


	public List<SinteticaClasseVO> creaListaSinteticaClassi(SBNMarc response, boolean livelloPolo) throws ValidationException {

		List<SinteticaClasseVO> listaSintentica = new ArrayList<SinteticaClasseVO>();

		// Reperimento dei dati della lista: classe SbnOutputType
		SbnMessageType sbnMessage = response.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		int numeroElementi = sbnOutPut.getElementoAutCount();
		if (numeroElementi < 1)
			return listaSintentica;

		int progressivo = sbnOutPut.getNumPrimo() - 1; // ulteriore blocco?
		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		if (SBNMarcUtil.eqAuthority(datiElemento.getTipoAuthority(), SbnAuthority.CL) ) {

			//almaviva5_20090915 #2940
			String codPolo = user.getBiblioteca().substring(0, 3);
			String codBib  = user.getBiblioteca().substring(3);

			for (int i = 0; i < numeroElementi; i++) {
				++progressivo;
				SinteticaClasseVO classe = creaElementoListaClassi(sbnOutPut, i, progressivo, livelloPolo, codPolo, codBib);
				listaSintentica.add(classe);
			}
		}

		return listaSintentica;
	}

	public SinteticaClasseVO creaElementoListaClassi(SbnOutputType sbnOutPut,
			int elementIndex, int progressivo, boolean livelloPolo, String codPolo, String codBib) throws ValidationException {
		SinteticaClasseVO classe = new SinteticaClasseVO();

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		ClasseType classeType = (ClasseType) datiElemento;

		classe.setProgr(progressivo);

		classe.setT005(classeType.getT005());
		classe.setLivelloAutorita(classeType.getLivelloAut().toString());
		classe.setIdentificativoClasse(classeType.getT001());

		String sistema = null;
		String simbolo = null;
		String edizione = null;
		String parole = null;
		boolean costruito = false;

		A676 a676 = classeType.getClasseTypeChoice().getT676();
		if (a676 != null) {
			// dewey
			sistema = "D";
			simbolo = a676.getA_676();
			edizione = a676.getV_676().toString();
			parole = a676.getC_676();
			costruito = (a676.getC9_676() == SbnIndicatore.S);

		} else {
			// non dewey
			A686 a686 = classeType.getClasseTypeChoice().getT686();
			sistema = a686.getC2_686();
			simbolo = a686.getA_686();
			edizione = EDIZIONE_NON_DEWEY;
			parole = a686.getC_686();
		}

		classe.setSistema(sistema);
		classe.setSimbolo(simbolo);
		classe.setParole(parole);
		classe.setEdDewey(edizione);
		classe.setCostruito(costruito);

		//almaviva5_20111024 evolutive CFI
		classe.setRank((short) classeType.getRank());

		// deve essere inserito
		if (livelloPolo) {
			classe.setCondiviso(datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE);
			if (!classe.isCondiviso())
				classe.setCondivisoLista("NO");
			else
				classe.setCondivisoLista("SI");

			try {
//				classe.setNumTitoliPolo(dao.contaTitoliCollegatiClasse(sistema, edizione, simbolo) );
//				//almaviva5_20090915 #2940
//				classe.setNumTitoliBiblio(dao.contaTitoliCollegatiClasseBiblioteca(codPolo, codBib, sistema, edizione, simbolo));
				classe.setNumTitoliPolo(datiElemento.getNum_tit_coll());
				classe.setNumTitoliBiblio(datiElemento.getNum_tit_coll_bib());
			} catch (Exception e) {
				classe.setNumTitoliPolo(0);
				classe.setNumTitoliBiblio(0);
			}

			// IMPOSTO IL CAMPO LEGATO A TITOLI "SI" "NO"
			if (classe.getNumTitoliPolo() != 0) {
				classe.setIndicatore("SI");//String.valueOf(classe.getNumTitoliPolo()));
			} else {
				classe.setIndicatore("NO");
			}
		}

		return classe;
	}// end creaElementoListaClassi

	public SBNMarc creaClasse(String codSistemaClassificazione,
			String codEdizioneDewey, String descrizione, String livello,
			String ulterioreTermine, String simbolo, boolean costruito,
			boolean livelloPolo, boolean forzaCreazione) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			ClasseType classeType = new ClasseType();
			ElementAutType elementAutType = new ElementAutType();
			ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			if (forzaCreazione)
				crea.setTipoControllo(SbnSimile.CONFERMA);
			else
				crea.setTipoControllo(SbnSimile.SIMILE);

			// TIPO AUTHORITY
			classeType.setTipoAuthority(SbnAuthority.CL);

			// Default DID
			classeType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			classeType.setLivelloAut(SbnLivello.valueOf(livello));

			if (codSistemaClassificazione.equals("D")) {
				A676 a676 = new A676();
				a676.setA_676(simbolo);
				a676.setC_676(descrizione);
				a676.setV_676(codEdizioneDewey);
				a676.setC9_676(costruito ? SbnIndicatore.S : SbnIndicatore.N);

				classeTypeChoice.setT676(a676);
			} else {
				A686 a686 = new A686();
				a686.setA_686(simbolo);
				a686.setC_686(descrizione);
				a686.setC2_686(codSistemaClassificazione);

				classeTypeChoice.setT686(a686);
			}
			elementAutType.setDatiElementoAut(classeType);

			//almaviva5_20090401 #2780
			if (livelloPolo)
				classeType.setUltTermine(ulterioreTermine);

			classeType.setClasseTypeChoice(classeTypeChoice);
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

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Crea classificazione: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea classificazione: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end creaClasse

	public SBNMarc importaClasse(String codSistemaClassificazione,
			String codEdizioneDewey, String descrizione, String livello,
			String ulterioreTermine, String simbolo, boolean costruito, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			ClasseType classeType = new ClasseType();
			ElementAutType elementAutType = new ElementAutType();
			ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);
			crea.setCattura(true);

			// TIPO AUTHORITY
			classeType.setTipoAuthority(SbnAuthority.CL);

			// Default DID
			classeType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);

			// CONDIVISO CON INDICE
			classeType.setCondiviso(DatiElementoTypeCondivisoType.S);

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			classeType.setLivelloAut(SbnLivello.valueOf(livello));

			if (codSistemaClassificazione.equals("D")) {
				A676 a676 = new A676();
				a676.setA_676(simbolo);
				a676.setC_676(descrizione);
				a676.setV_676(codEdizioneDewey);
				a676.setC9_676(costruito ? SbnIndicatore.S : SbnIndicatore.N);

				classeTypeChoice.setT676(a676);
			} else {
				A686 a686 = new A686();
				a686.setA_686(simbolo);
				a686.setC_686(descrizione);
				a686.setC2_686(codSistemaClassificazione);

				classeTypeChoice.setT686(a686);
			}

			//almaviva5_20090605 #2463
			if (livelloPolo)
				classeType.setUltTermine(ulterioreTermine);

			elementAutType.setDatiElementoAut(classeType);

			classeType.setClasseTypeChoice(classeTypeChoice);
			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage,this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Importa classificazione: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Importa classificazione: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end importaClasse


	public SBNMarc catturaClasse(String codSistemaClassificazione,
			String codEdizioneDewey, String descrizione, String livello,
			String ulterioreTermine, String simbolo, boolean costruito, boolean livelloPolo) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
			ClasseType classeType = new ClasseType();
			ElementAutType elementAutType = new ElementAutType();
			ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();

			crea.setCattura(true);
			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);

			// TIPO AUTHORITY
			classeType.setTipoAuthority(SbnAuthority.CL);

			// Default DID
			classeType.setT001(SBNMarcUtil.SBNMARC_DEFAULT_ID);

			// //////////// PRENDE I DATI INSERITI /////
			// Livello di Autorità
			classeType.setLivelloAut(SbnLivello.valueOf(livello));

			if (codSistemaClassificazione.equals("D")) {
				A676 a676 = new A676();
				a676.setA_676(simbolo);
				a676.setC_676(descrizione);
				a676.setV_676(codEdizioneDewey);
				a676.setC9_676(costruito ? SbnIndicatore.S : SbnIndicatore.N);

				classeTypeChoice.setT676(a676);

			} else {
				A686 a686 = new A686();
				a686.setA_686(simbolo);
				a686.setC_686(descrizione);
				a686.setC2_686(codSistemaClassificazione);
				classeTypeChoice.setT686(a686);
			}
			elementAutType.setDatiElementoAut(classeType);

			classeType.setClasseTypeChoice(classeTypeChoice);
			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);

			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			if (livelloPolo) {
				this.polo.setMessage(sbnmessage,this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage,this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Cattura classificazione: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Cattura classificazione: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end CatturaClasse


	public SBNMarc cancellaClasse(boolean livelloPolo, String idClasse) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CancellaType cancellaType = new CancellaType();
			SbnOggetto sbnOggetto = new SbnOggetto();
			sbnOggetto.setTipoAuthority(SbnAuthority.CL);

			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(idClasse);

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
	} // end cancellaClasse


	public SBNMarc variaClasse(CreaVariaClasseVO classe) {
		ClasseType classeType = new ClasseType();
		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = null;

			modifica = new ModificaType();
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			ElementAutType elementAutType = new ElementAutType();
			classeType.setTipoAuthority(SbnAuthority.CL);

			// Livello di Autorità
			String livello = classe.getLivello();
			classeType.setLivelloAut(SbnLivello.valueOf(livello));

			ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();
			if (classe.getCodSistemaClassificazione().equals("D")) {
				A676 a676 = new A676();
				a676.setA_676(classe.getSimbolo());
				a676.setC_676(classe.getDescrizione());
				a676.setV_676(classe.getCodEdizioneDewey());
				a676.setC9_676(classe.isCostruito() ? SbnIndicatore.S : SbnIndicatore.N);

				classeTypeChoice.setT676(a676);
			} else {
				A686 a686 = new A686();
				a686.setA_686(classe.getSimbolo());
				a686.setC_686(classe.getDescrizione());
				a686.setC2_686(classe.getCodSistemaClassificazione());

				classeTypeChoice.setT686(a686);
			}
			classeType.setClasseTypeChoice(classeTypeChoice);

			// Prendo i dati inseriti nel CreaVariaClasseVO
			// DATI DELLA CLASSE

			// Notazione
			classeType.setT001(classe.getT001());

			// Data di variazione
			classeType.setT005(classe.getT005());

			// Stato Record (C)
			classeType.setStatoRecord(StatoRecord.C);

			// CONDIVISO CON INDICE
			if (classe.isLivelloPolo()) {
				//almaviva5_20090401 #2780
				classeType.setUltTermine(classe.getUlterioreTermine() );
				if (classe.isCondiviso())
					classeType.setCondiviso(DatiElementoTypeCondivisoType.S);
				else
					classeType.setCondiviso(DatiElementoTypeCondivisoType.N);
			}
			elementAutType.setDatiElementoAut(classeType);

			modifica.setElementoAut(elementAutType);
			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);

			if (classe.isLivelloPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR Varia classe: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Varia classe: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end variaClasse

	public SBNMarc analiticaClasse(boolean livelloPolo, String simbolo) {

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

			canali.setT001(simbolo);

			CercaSoggettoDescrittoreClassiReperType cercaSogDesc = new CercaSoggettoDescrittoreClassiReperType();
			cercaSogDesc.setCanaliCercaDatiAut(canali);
			cercaSogDesc.setTipoAuthority(SbnAuthority.CL);
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
			log.error("ERROR Analitica classe: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Analitica classe: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end creaRichiestaAnaliticoClasse

	public SBNMarc trascinaTitoliTraClassi(boolean livelloPolo,
			AreaDatiAccorpamentoVO area) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.CL);
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
			log.error("ERROR Trascina Titoli tra classi: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Trascina Titoli tra classi: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end trascinaTitoliTraClassi

	public SBNMarc richiestaAccorpamentoClassi(boolean livelloPolo,	AreaDatiAccorpamentoVO area) {

		SBNMarc sbnRisposta = null;
		// Non ci sono ID di titoli collegati, per cui si tratta di una
		// richiesta di accorpamento
		// e non di spostamento legami
		area.setIdTitoliLegati(null);

		try {
			//SBNMarc sbnRichiesta = creaIntestazione();
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			FondeType fonde = new FondeType();
			SbnOggetto oggetto = new SbnOggetto();

			oggetto.setTipoAuthority(SbnAuthority.CL);
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
			log.error("ERROR Accorpa classi: " + ve.getMessage()
					+ ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Accorpa classi: " + e.getMessage()
					+ e.toString());
		}

		return sbnRisposta;
	} // end richiestaAccorpamentoClassi



	public SBNMarc gestioneLegameTitoloClasse(DatiLegameTitoloClasseVO input) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			ModificaType modifica = new ModificaType();

			LegamiType legami = new LegamiType();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			modifica.setTipoControllo(SbnSimile.SIMILE);

			DatiDocType datiDocType = new DatiDocType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(input.getBidNatura()));
			//almaviva5_20130520 #5316
			String tipoMateriale = input.getBidTipoMateriale();
			datiDocType.setTipoMateriale(ValidazioneDati.isFilled(tipoMateriale) ? SbnMateriale.valueOf(tipoMateriale) : null);
			datiDocType.setT001(input.getBid().toUpperCase());
			// DEVO VERIFICARE LA CONGRUENZA DEL T005
			String rightT005 = input.getT005();
			if (ValidazioneDati.strIsNull(rightT005)) {

				SbnGestioneTitoliDao gestioneTitoliDao = new SbnGestioneTitoliDao(
						this.indice, this.polo, this.user);


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
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = gestioneTitoliDao
						.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass);

				if (!areaDatiPassReturn.getCodErr().equals("")
						&& !areaDatiPassReturn.getCodErr().equals("0000"))
					throw new Exception(areaDatiPassReturn.getTestoProtocollo());

				rightT005 = areaDatiPassReturn.getTimeStampRadice();//.getTreeElementViewTitoli().getT005();

			}

			datiDocType.setT005(rightT005);
			datiDocType.setLivelloAutDoc(SbnLivello.valueOf(input
					.getBidLivelloAut()));
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
			}

			legami.setIdPartenza(input.getBid());

			for (LegameTitoloAuthSemanticaVO lts : input.getLegami()) {

				LegameTitoloClasseVO datiLegame = (LegameTitoloClasseVO) lts;
				ArrivoLegame arrivoLegame = new ArrivoLegame();
				LegameElementoAutType legame = new LegameElementoAutType();
				legame.setTipoAuthority(SbnAuthority.CL);

				SimboloDeweyVO sd = SimboloDeweyVO.parse(datiLegame.getIdentificativoClasse());
				if (sd.isDewey()) {
					legame.setTipoLegame(SbnLegameAut.valueOf("676"));
					legame.setIdArrivo(sd.toString());
				} else {
					legame.setTipoLegame(SbnLegameAut.valueOf("686"));
					legame.setIdArrivo(sd.toString());
				}
				legame.setNoteLegame(datiLegame.getNotaLegame());

				arrivoLegame.setLegameElementoAut(legame);
				legami.addArrivoLegame(arrivoLegame);
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
			log.error("ERROR Crea legame titolo classificazione: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			log.error("ERROR Crea legame titolo classificazione: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaLegameTitoloclassificazione

	public SBNMarc ricercaClassiPerBidCollegato(String did,
			String sistemaClassificazione, String deweyEd,
			String elementiBlocco) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			// tipoOutput sintetica 000
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(Integer.valueOf(elementiBlocco) );

			// tipo ORDINAMENTO
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaSoggettoDescrittoreClassiReperType cercaDatiAutType = new CercaSoggettoDescrittoreClassiReperType();
			cercaDatiAutType.setTipoAuthority(SbnAuthority.CL);
			if (!ValidazioneDati.strIsNull(sistemaClassificazione))
				cercaDatiAutType.setSistemaClassificazione(sistemaClassificazione);
			if (!ValidazioneDati.strIsNull(deweyEd))
				cercaDatiAutType.setV_676(deweyEd);

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legameAut = new LegameElementoAutType();
			legameAut.setTipoAuthority(SbnAuthority.TH);
			legameAut.setTipoLegame(SbnLegameAut.valueOf("941")); // 941
			legameAut.setIdArrivo(did);
			arrivoLegame.setLegameElementoAut(legameAut);

			cercaElemento.setCercaDatiAut(cercaDatiAutType);
			cercaElemento.setArrivoLegame(arrivoLegame);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();


		} catch (SbnMarcException ve) {
			log.error("", ve);
			log.error("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;

	}

}
