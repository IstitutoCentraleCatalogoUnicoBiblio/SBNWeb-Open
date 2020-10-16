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
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnEJB;
import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.DettaglioOggetti;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.SinteticaTitoli;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityCampiTitolo;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.VariazioneBodyTitoli;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C140;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CdBibType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocCartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocGraficaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriAudiovisivoCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriDatiComuniCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriElettronicoCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSpecLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiScambioInInserimentoTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.Impronta;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaSchedeVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiScambiaResponsLegameTitAutVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaPassaggioReticoloTitoliVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloCompletoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloAudiovisivoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloElettronicoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.InterrogazioneTitoloGeneraleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteAggiornataVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice;
import it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice_id_arrivo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_report_indice_id_locali;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.domain.bibliografica.AreaDatiServizioInterrTitoloDomVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.exolab.castor.xml.ValidationException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrNull;

/**
 * Funzionalità di creazione/sintesi di alberi DOM Castor per l'accorpamento.
 *
 * @author almaviva2
 */
public class SbnGestioneTitoliDao {

	private FactorySbn indice;

	private FactorySbn polo;

	private SbnUserType user;

	public SbnGestioneTitoliDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}


	public static final String IID_SPAZIO = " ";

	public static final String IID_STRINGAVUOTA = "";

	public static final int CODICE_TIPO_NUMERO_STANDARD = 10;

	//public final static String COORDINATA_VUOTA = "   °  \'  \"";
	public final static String COORDINATA_VUOTA = "°  \'  \"";

	public static final int TIT_LEN_TITOLO_ORDINAMENTO = 240;

	public static final int TIT_LEN_TITOLO_ESTRATTO = 240;

	public static final int TIT_LEN_APPELLATIVO = 240;

	public static final int TIT_LEN_ORGANICO_SINTETICO = 80;

	public static final int TIT_LEN_ORGANICO_ANALITICO = 320;

	public static final int TIT_LEN_NUMERO_ORDINE = 20;

	public static final int TIT_LEN_NUMERO_OPERA = 20;

	public static final int TIT_LEN_CATALOGO_TEMATICO = 20;

	public static final int TIT_LEN_DATAZIONE = 10;

	public static final int TIT_LEN_SEZIONI = 20;

	private String myChiamata;

	private int setKeyCtr = -1;

	private String codSbnBiblioteca = "";

	static int level = 0;

	private static TreeElementViewTitoli nodo1;

	private int livello = 0;

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

	Map tabellaLocalizzazioni = new HashMap();

	DettaglioOggetti dettOggetti = new DettaglioOggetti();

	/**
	 * @param idOrigine
	 *            id dell'oggetto che sarà eliminato
	 * @param idDestinazione
	 *            id dell'oggetto accorpante
	 * @param tipoAccorpamento,
	 *            una delle costanti in MediatorAccorpamento che indica il tipo
	 *            delle entità da accorpare
	 * @param frameCorrente
	 *            frame corrente contenente la richiesta XML da inviare
	 * @param framePadre
	 *            areaTitoli di provenienza contenente i dati del titolo di
	 *            provenienza, framePadre = null se non si proviene da una altra
	 *            area titoli
	 */
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoli(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass) {



		if (areaDatiPass.getRicercaPerGest() == null) {
			areaDatiPass.setRicercaPerGest("NO");
		}

		int tipoOggetto = areaDatiPass.getTipoOggetto();
		int tipoOggettoFiltrato = areaDatiPass.getTipoOggettoFiltrato();
		String visualNumSequenza = "NO";

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		if (!areaDatiPass.isRicercaPolo() && !areaDatiPass.isRicercaIndice()) {
			areaDatiPassReturn.setCodErr("livRicObblig");
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}
		if (tipoOggetto == 99) {
			String esito = controlloFormaleDati(areaDatiPass);
			if (!esito.equals("")) {
				areaDatiPassReturn.setCodErr(esito);
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}
		}

		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();

			FiltriDatiComuniCercaType filtriDatiComuniCercaType = new FiltriDatiComuniCercaType();

			// INIZIA PROGRESSIVO DI DEFAULT SETTO A 1
			cercaType.setNumPrimo(1);

			InterrogazioneTitoloGeneraleVO interTitGen = areaDatiPass.getInterTitGen();
			if (interTitGen == null) {
				if (areaDatiPass.getElemXBlocchi() != 0) {
					cercaType.setMaxRighe(areaDatiPass.getElemXBlocchi());
				} else {
					cercaType.setMaxRighe(10);
				}
				if (areaDatiPass.getFormatoListaSelez() == null) {
					cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
				} else if (areaDatiPass.getFormatoListaSelez().equals("")) {
					cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
				} else {
					cercaType.setTipoOutput(SbnTipoOutput.valueOf(areaDatiPass.getFormatoListaSelez()));
				}

				if (areaDatiPass.getTipoOrdinamSelez() == null) {
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				} else if (areaDatiPass.getTipoOrdinamSelez().equals("")) {
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				} else {
					cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass.getTipoOrdinamSelez()));
				}
			} else {
				if (interTitGen.getElemXBlocchi() != 0) {
					cercaType.setMaxRighe(interTitGen.getElemXBlocchi());
				} else {
					cercaType.setMaxRighe(10);
				}

				if (interTitGen.getFormatoListaSelez() == null
						|| interTitGen.getFormatoListaSelez().equals(IID_STRINGAVUOTA)) {
					cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
				} else {
					cercaType.setTipoOutput(SbnTipoOutput.valueOf(interTitGen.getFormatoListaSelez()));
				}

				if (interTitGen.getTipoOrdinamSelez() == null
						|| interTitGen.getTipoOrdinamSelez().equals("")) {
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				} else {
					cercaType.setTipoOrd(SbnTipoOrd.valueOf(interTitGen.getTipoOrdinamSelez()));
				}
			}

			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			// Modifiche in tutto il metodo
			if (!(interTitGen == null
					&& areaDatiPass.getInterTitMus() == null
					&& areaDatiPass.getInterTitGra() == null
					&& areaDatiPass.getInterTitCar() == null
					&& areaDatiPass.getInterTitAud() == null
					&& areaDatiPass.getInterTitEle() == null)) {

				CercaDatiTitType cercaDatiTitType = null;

				if ((interTitGen.getTipoMateriale().equals(""))
						|| (interTitGen.getTipoMateriale().equals("*"))) {
					cercaDatiTitType = new CercaDatiTitType();
				} else if (interTitGen.getTipoMateriale().equals("E")) { // ANTICO
					cercaDatiTitType = new CercaDocAnticoType();
				} else if (interTitGen.getTipoMateriale().equals("C")) { // CARTOGRAFIA
					cercaDatiTitType = new CercaDocCartograficoType();
				} else if (interTitGen.getTipoMateriale().equals("G")) { // GRAFICA
					cercaDatiTitType = new CercaDocGraficaType();
				} else if (interTitGen.getTipoMateriale().equals("M")) { // MODERNO
					cercaDatiTitType = new CercaDatiTitType();
				} else if (interTitGen.getTipoMateriale().equals("U")) { // MUSICA
					cercaDatiTitType = new CercaDocMusicaType();
				} else if (interTitGen.getTipoMateriale().equals("H")) { // AUDIOVISIVO
					cercaDatiTitType = new CercaDocAudiovisivoType();
				} else if (interTitGen.getTipoMateriale().equals("L")) { // ELETTRONICO
					cercaDatiTitType = new CercaDocElettronicoType();

				}

				CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
				TitoloCercaType titoloCercaType = new TitoloCercaType();
				StringaCercaType stringaCercaType = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				ElementoAutLegatoType elementoAutLegatoType = new ElementoAutLegatoType();
				CanaliCercaDatiAutType canaliCercaDatiAutType = new CanaliCercaDatiAutType();
				// SPOSTATA COME DICHIARAZIONE GLOBALE
				StringaCercaType stringaCercaTypeElementType = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoiceElement = new StringaCercaTypeChoice();

				// LEGGO CAMPI PANNELLO STANDART
				// RICERCA PER TITOLO
				if (!interTitGen.getTitolo().equals("")) {
					// - ricerca per parte in like
					if (!interTitGen.isTitoloPunt()) {
						stringaCercaType.setStringaCercaTypeChoice(stringaTypeChoice);
						stringaTypeChoice.setStringaLike(interTitGen.getTitolo());
						titoloCercaType.setStringaCerca(stringaCercaType);
						cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					}

					// - ricerca puntuale
					if (interTitGen.isTitoloPunt()) {
						stringaCercaType.setStringaCercaTypeChoice(stringaTypeChoice);
						UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
						stringaTypeChoice.setStringaEsatta(utilityCampiTitolo.componiStringaRicTitEsatta(interTitGen.getTitolo()));
//						stringaTypeChoice.setStringaEsatta(areaDatiPass.getInterTitGen().getTitolo());
						titoloCercaType.setStringaCerca(stringaCercaType);
						cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					}
				}

				// RICERCA PER BID
				if (!interTitGen.getBid().equals("")) {
					cercaDatiTitTypeChoice.setT001(interTitGen.getBid());
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				}

				// Tipo Materiale
				if (interTitGen.getTipoMateriale().equals("")) {
					// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
					// SbnMateriale sbnMateriale = SbnMateriale.VALUE_5;
					SbnMateriale sbnMateriale = SbnMateriale.valueOf(" ");
					SbnMateriale[] sbnMatArr = new SbnMateriale[1];
					sbnMatArr[0] = sbnMateriale;
					cercaDatiTitType.setTipoMateriale(sbnMatArr);
				} else if (interTitGen.getTipoMateriale()
						.equals("*")) {
					// NON PASSO NULLA
				} else {
					SbnMateriale sbnMateriale = SbnMateriale.valueOf(interTitGen.getTipoMateriale());
					SbnMateriale[] sbnMatArr = new SbnMateriale[1];
					sbnMatArr[0] = sbnMateriale;
					cercaDatiTitType.setTipoMateriale(sbnMatArr);
				}

				// NATURA
				List temp = new ArrayList();
				if (!interTitGen.getNaturaSelez1().equals("")) {
					temp.add(interTitGen.getNaturaSelez1());
				}
				if (!interTitGen.getNaturaSelez2().equals("")) {
					temp.add(interTitGen.getNaturaSelez2());
				}
				if (!interTitGen.getNaturaSelez3().equals("")) {
					temp.add(interTitGen.getNaturaSelez3());
				}
				if (!interTitGen.getNaturaSelez4().equals("")) {
					temp.add(interTitGen.getNaturaSelez4());
				}

				if (temp.size() > 0) {
					String[] valoriEffettivi = (String[]) temp.toArray(new String[temp.size()]);
					cercaDatiTitType.setNaturaSbn(valoriEffettivi);
				}

				// TIPO RECORD
				if (!interTitGen.getTipiRecordSelez().equals("")) {
					int count = 1;
					GuidaDoc[] guidaDocArr = new GuidaDoc[count];
					count = 0;

					TipoRecord tipoRecord = TipoRecord.valueOf(interTitGen.getTipiRecordSelez());
					GuidaDoc guidaDoc = new GuidaDoc();
					guidaDocArr[count] = guidaDoc;
					guidaDoc.setTipoRecord(tipoRecord);
					count++;
					cercaDatiTitType.setGuida(guidaDocArr);
				}

				// PAESE
				if (!interTitGen.getPaeseSelez().equals("")) {
					C102 c102 = new C102();
					c102.setA_102(interTitGen.getPaeseSelez());
					cercaDatiTitType.setT102(c102);
				}

				// LINGUA
				// si potebbrero selezionare da 1 a 3 lingue
				if (!interTitGen.getLinguaSelez().equals("")) {
					C101 c101 = new C101();
					String[] lingue = new String[1];
					lingue[0] = interTitGen.getLinguaSelez();
					c101.setA_101(lingue);
					cercaDatiTitType.setT101(c101);
				}

				C100 c100Da = new C100();
				C100 c100A = new C100();

				// DATE PUBBLICAZIONE TIPO DATA
				if (!interTitGen.getTipoDataSelez().equals("")) {
					String tipoDataUnimarc = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE,
							interTitGen.getTipoDataSelez());
					c100A.setA_100_8(tipoDataUnimarc);
					c100Da.setA_100_8(tipoDataUnimarc);
					cercaDatiTitType.setT100_A(c100A);
					cercaDatiTitType.setT100_Da(c100Da);
				}

				// DATA 1 - DA
				if (!interTitGen.getData1Da().equals("")) {
					c100Da.setA_100_9(interTitGen.getData1Da());
					cercaDatiTitType.setT100_Da(c100Da);
					if (interTitGen.getData1A().equals("")) {
						c100A.setA_100_9(interTitGen.getData1Da());
						cercaDatiTitType.setT100_A(c100A);
					}
				}

				// DATA 1 - A
				if (!interTitGen.getData1A().equals("")) {
					c100A.setA_100_9(interTitGen.getData1A());
					cercaDatiTitType.setT100_A(c100A);
				}

				// DATA 2 - DA
				if (!interTitGen.getData2Da().equals("")) {
					c100Da.setA_100_13(interTitGen.getData2Da());
					cercaDatiTitType.setT100_Da(c100Da);
					if (interTitGen.getData2A().equals("")) {
						c100A.setA_100_13(interTitGen.getData2Da());
						cercaDatiTitType.setT100_A(c100A);
					}
				}

				// DATA 2 - A
				if (!interTitGen.getData2A().equals(IID_STRINGAVUOTA)) {
					c100A.setA_100_13(interTitGen.getData2A());
					cercaDatiTitType.setT100_A(c100A);
				}
		        // almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -


				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio
				// Testo letterario per nuovo protocollo SCHEMA 2.0 e valori area0
				if (areaDatiPass.getInterTitGen().isLibretto()) {
					filtriDatiComuniCercaType.setA_105_11("i");
					filtriDatiComuniCercaType.setA_140_17("da");
					cercaDatiTitType.setFiltriDatiComuniCerca(filtriDatiComuniCercaType);
				}
				// Fine Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio

				// Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
				// e opportunamente salvato sulla tb_titolo campo ky_editore
				if (ValidazioneDati.isFilled(interTitGen.getParoleEditore())) {
					cercaDatiTitType.addParoleEditore(interTitGen.getParoleEditore());
				}
				// Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore


				if (interTitGen.getTipoMateriale().equals("*") || interTitGen.getTipoMateriale().equals(IID_STRINGAVUOTA)) {
					// NOME COLLEGATO
					if (!interTitGen.getNomeCollegato().equals(IID_STRINGAVUOTA)) {

						// INSERIRE UN TIPO AUTORE
						/*******************************************************
						 * tipologia di authority: AU = autore, TU = Titolo
						 * Uniforme, UM = Titolo Uniforme Musica, SO = Soggetto,
						 * DE = Descrittore, LU = Luogo, CL = Classe, MA =
						 * Marca, RE = Repertorio
						 ******************************************************/
						elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);

						if (!interTitGen.isNomeCollegatoPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getNomeCollegato());
						}

						if (interTitGen.isNomeCollegatoPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaEsatta(interTitGen.getNomeCollegato());
						}

						canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
						elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
						cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
					}


					// Luogo di pubblicazione
					if (!interTitGen.getLuogoPubbl().equals(IID_STRINGAVUOTA)) {
						elementoAutLegatoType.setTipoAuthority(SbnAuthority.LU);
						if (!interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}

						if (interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaEsatta(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}
				}

				// RESPONSABILITA
				if (!interTitGen.getResponsabilitaSelez().equals(IID_STRINGAVUOTA)) {
					elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);
					elementoAutLegatoType.setTipoRespons(SbnRespons.valueOf(interTitGen.getResponsabilitaSelez().toString()));
					cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
				}


				// RELAZIONE
				if (!interTitGen.getRelazioniSelez().equals(IID_STRINGAVUOTA)) {
					elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);
					elementoAutLegatoType.setRelatorCode(interTitGen.getRelazioniSelez().toString());
					cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
				}

				// SOTTO TIPO LEGAME
				if (!interTitGen.getSottoNaturaDSelez().equals(IID_STRINGAVUOTA)) {
					cercaDatiTitType.setSottoTipoLegame(SbnSpecLegameDoc.valueOf(interTitGen.getSottoNaturaDSelez().toString()));
				}


				// =======================================
				// CANALI PRIMARI
				NumStdType numStdType1 = new NumStdType();

				if (!interTitGen.getTipoMateriale().equals("U")) {
					// NUMERO E TIPO STANDARD (deve essere valorizzato solo per i casi diversi da lastra)
//					if (!areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) {
						if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
							numStdType1.setNumeroSTD(interTitGen.getNumStandard1().toString());
							cercaDatiTitTypeChoice.setNumSTD(numStdType1);
							cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
							cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
						}

						if (!interTitGen.getNumStandardSelez().equals(IID_STRINGAVUOTA)) {
							String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,
											interTitGen.getNumStandardSelez());
				            // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//							SbnTipoSTD sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//							numStdType1.setTipoSTD(sbnTipoSTD);
							numStdType1.setTipoSTD(descNumStandard);

							cercaDatiTitTypeChoice.setNumSTD(numStdType1);
							cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
							cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
						}
//					}
				}

				// Inizio modifiche a seguito della mail del 16/07/2008 di Gabriella Contardi
				// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
				int countGenere = 0;
				countGenere += ValidazioneDati.isFilled(interTitGen.getGenereSelez1()) ? 1 : 0;
				countGenere += ValidazioneDati.isFilled(interTitGen.getGenereSelez2()) ? 1 : 0;
				countGenere += ValidazioneDati.isFilled(interTitGen.getGenereSelez3()) ? 1 : 0;
				countGenere += ValidazioneDati.isFilled(interTitGen.getGenereSelez4()) ? 1 : 0;

				String[] genere = new String[countGenere];
				if (countGenere > 0) {
					countGenere = 0;

					if (!interTitGen.getGenereSelez1().equals("")) {
						genere[countGenere] = interTitGen.getGenereSelez1().toString();
						countGenere++;
					}
					if (!interTitGen.getGenereSelez2().equals("")) {
						genere[countGenere] = interTitGen.getGenereSelez2().toString();
						countGenere++;
					}
					if (!interTitGen.getGenereSelez3().equals("")) {
						genere[countGenere] = interTitGen.getGenereSelez3().toString();
						countGenere++;
					}
					if (!interTitGen.getGenereSelez4().equals("")) {
						genere[countGenere] = interTitGen.getGenereSelez4().toString();
						countGenere++;
					}

					if (!interTitGen.getTipoMateriale().equals("E")) {
						C105 c105 = new C105();
						c105.setA_105_4(genere);
						cercaDatiTitType.setT105(c105);
					}
				}

				// Fine   modifiche a seguito della mail del 16/07/2008 di Gabriella Contardi


				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE MODERNO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////
				// CANALI PRIMARI
				if (interTitGen.getTipoMateriale().equals("M")) {
					NumStdType numStdType = new NumStdType();

					// NUMERO E TIPO STANDART
					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						numStdType.setNumeroSTD(interTitGen.getNumStandard1().toString());
						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					if (!interTitGen.getNumStandardSelez().equals(IID_STRINGAVUOTA)) {
						String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,interTitGen.getNumStandardSelez());
			            // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//						SbnTipoSTD sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//						numStdType.setTipoSTD(sbnTipoSTD);
						numStdType.setTipoSTD(descNumStandard);

						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					// NOME COLLEGATO
					if (!interTitGen.getNomeCollegato().equals(IID_STRINGAVUOTA)) {
						elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);
						if (!interTitGen.isNomeCollegatoPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getNomeCollegato());
						}

						if (interTitGen.isNomeCollegatoPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaEsatta(interTitGen.getNomeCollegato());
						}

						canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
						elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
						cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
					}


					// EDITORE
//					if (areaDatiPass.getInterTitGen().getNomeCollegato() != null) {
//						if (!areaDatiPass.getInterTitGen().getNomeCollegato()
//								.equals(IID_STRINGAVUOTA)) {
//							titoloCercaType.setEditoreKey(areaDatiPass
//									.getInterTitGen().getNomeCollegato());
//							titoloCercaType.setStringaCerca(stringaCercaType);
//							cercaDatiTitTypeChoice
//									.setTitoloCerca(titoloCercaType);
//							cercaDatiTitType
//							// EDITORE		.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
//							cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
//						}
//					}
				}
				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// FINE MATERIALE MODERNO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE ANTICO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////
				if (interTitGen.getTipoMateriale().equals("E")) {
					CercaDocAnticoType cercaDocAnticoType = (CercaDocAnticoType) cercaDatiTitType;
					C012 c012 = new C012();

					if (!interTitGen.getImpronta1().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_1(interTitGen.getImpronta1().toString());
						cercaDocAnticoType.setT012(c012);
					}

					if (!interTitGen.getImpronta2().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_2(interTitGen.getImpronta2().toString());
						cercaDocAnticoType.setT012(c012);
					}

					if (!interTitGen.getImpronta3().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_3(interTitGen.getImpronta3().toString());
						cercaDocAnticoType.setT012(c012);
					}

					// Intervento per Evolutive BARI 30.10.2012 Viene inserito il filtro di ricerca per genere
					if (countGenere > 0) {
						C140 c140 = new C140();
						c140.setA_140_9(genere);
						cercaDocAnticoType.setT140(c140);
					}


					// Luogo di pubblicazione
					if (!interTitGen.getLuogoPubbl().equals(IID_STRINGAVUOTA)) {
						elementoAutLegatoType.setTipoAuthority(SbnAuthority.LU);

						if (!interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}

						if (interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaEsatta(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}

					// MID COLLEGATO
					if (areaDatiPass.getTipoAut() != null) {
						if (areaDatiPass.getTipoAut().equals("MA")) {
							elementoAutLegatoType.setTipoAuthority(SbnAuthority.MA);
							canaliCercaDatiAutType.setT001(areaDatiPass.getXidDiRicerca());
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}

				// EDITORE
					if (interTitGen.getNomeCollegato() != null) {
						if (!interTitGen.getNomeCollegato().equals("")) {
							elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getNomeCollegato());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}

					cercaDocAnticoType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				}
				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// FINE MATERIALE ANTICO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE MUSICA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				if (interTitGen.getTipoMateriale().equals("U")) {
					CercaDocMusicaType cercaDocMusicaType = (CercaDocMusicaType) cercaDatiTitType;
					NumStdType numStdType = new NumStdType();

					// NUMERO E TIPO STANDART

					// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//					SbnTipoSTD sbnTipoSTD = null;
					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						// TIPO STANDART E NUMERI DI LASTRA
						if (interTitGen.getNumStandardSelez().equals("L")) {
							int numeroDa = 0;
							int numeroA = 0;
							numeroDa = Integer.parseInt(interTitGen.getNumStandard1().trim());
							numeroA = Integer.parseInt(interTitGen.getNumStandard2().toString());
							cercaDocMusicaType.setNumLastra_Da(numeroDa);

							if (numeroA == 0) {
								cercaDocMusicaType.setNumLastra_A(numeroDa);
							}

							cercaDocMusicaType.setNumLastra_A(numeroA);
						} else if (interTitGen.getNumStandardSelez().equals("E")) {
							int numeroDa = Integer.parseInt(interTitGen.getNumStandard1().trim());
							int numeroA = Integer.parseInt(interTitGen.getNumStandard2().toString());
							cercaDocMusicaType.setNumEditor_Da(numeroDa);
							cercaDocMusicaType.setNumEditor_A(numeroA);
						} else {
							String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,
									interTitGen.getNumStandardSelez());
// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//							sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//							numStdType.setTipoSTD(sbnTipoSTD);
							numStdType.setTipoSTD(descNumStandard);

							numStdType.setNumeroSTD(interTitGen.getNumStandard1().toString());
							cercaDatiTitTypeChoice.setNumSTD(numStdType);
							cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
							cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
						}
					}

					C012 c012 = new C012();
					C899 c899 = new C899();

					if (!interTitGen.getImpronta1().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_1(interTitGen.getImpronta1().toString());
						cercaDocMusicaType.setT012(c012);
					}

					if (!interTitGen.getImpronta2().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_2(interTitGen.getImpronta2().toString());
						cercaDocMusicaType.setT012(c012);
					}

					if (!interTitGen.getImpronta3().equals(IID_STRINGAVUOTA)) {
						c012.setA_012_3(interTitGen.getImpronta3().toString());
						cercaDocMusicaType.setT012(c012);
					}

					// localizzazione
					if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
						if (!areaDatiPass.getInterTitMus().getLocalizzazione().equals(IID_STRINGAVUOTA)) {
							c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
							c899.setC2_899(areaDatiPass.getInterTitMus().getLocalizzazione().toString());
							cercaDocMusicaType.setT899(c899);
						}
					}
					// fondo
					if (areaDatiPass.getInterTitMus().getFondo() != null) {
						if (!areaDatiPass.getInterTitMus().getFondo().equals(IID_STRINGAVUOTA)) {
							c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
							c899.setB_899(areaDatiPass.getInterTitMus().getFondo().toString());
							cercaDocMusicaType.setT899(c899);
						}
					}
					// Segnatura
					if (areaDatiPass.getInterTitMus().getSegnatura() != null) {
						if (!areaDatiPass.getInterTitMus().getSegnatura().equals(IID_STRINGAVUOTA)) {
							c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
							c899.setG_899(areaDatiPass.getInterTitMus().getSegnatura().toString());
							cercaDocMusicaType.setT899(c899);
						}
					}

					// Luogo di pubblicazione
					if (!interTitGen.getLuogoPubbl().equals(IID_STRINGAVUOTA)) {
						elementoAutLegatoType.setTipoAuthority(SbnAuthority.LU);
						if (!interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}

						if (interTitGen.isLuogoPubblPunt()) {
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaEsatta(interTitGen.getLuogoPubbl());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}

					// Editore
					if (interTitGen.getNomeCollegato() != null) {
						if (!interTitGen.getNomeCollegato().equals("")) {
							elementoAutLegatoType.setTipoAuthority(SbnAuthority.AU);
							stringaCercaTypeElementType.setStringaCercaTypeChoice(stringaTypeChoiceElement);
							stringaTypeChoiceElement.setStringaLike(interTitGen.getNomeCollegato());
							canaliCercaDatiAutType.setStringaCerca(stringaCercaTypeElementType);
							elementoAutLegatoType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
							cercaDatiTitType.setElementoAutLegato(elementoAutLegatoType);
						}
					}



					// Testo letterario
					C125 c125 = new C125();
					if (areaDatiPass.getInterTitMus().getTipoTestoLetterarioSelez() != null) {
						if (!areaDatiPass.getInterTitMus().getTipoTestoLetterarioSelez().equals(IID_STRINGAVUOTA)) {
							c125.setB_125(areaDatiPass.getInterTitMus().getTipoTestoLetterarioSelez().toString());
							cercaDocMusicaType.setT125(c125);
						}
					}

					C128 c128 = new C128();
					// elaborazione
					if (areaDatiPass.getInterTitMus().getElaborazioneSelez() != null) {
						if (!areaDatiPass.getInterTitMus().getElaborazioneSelez().equals(IID_STRINGAVUOTA)) {
							c128.setD_128(areaDatiPass.getInterTitMus().getElaborazioneSelez().toString().toLowerCase());
							cercaDocMusicaType.setT128(c128);
						}
					}
					// organico sintetico
					if (areaDatiPass.getInterTitMus().getOrganicoSintet() != null) {
						if (!areaDatiPass.getInterTitMus().getOrganicoSintet().equals(IID_STRINGAVUOTA)) {
							c128.setB_128(areaDatiPass.getInterTitMus().getOrganicoSintet().toString());
							cercaDocMusicaType.setT128(c128);
						}
					}

					// organico analitico
					if (areaDatiPass.getInterTitMus().getOrganicoAnalit() != null) {
						if (!areaDatiPass.getInterTitMus().getOrganicoAnalit().equals(IID_STRINGAVUOTA)) {
							c128.setC_128(areaDatiPass.getInterTitMus().getOrganicoAnalit().toString());
							cercaDocMusicaType.setT128(c128);
						}
					}

					// Presentazione
					if (areaDatiPass.getInterTitMus().getPresentazioneSelez() != null) {
						if (!areaDatiPass.getInterTitMus().getPresentazioneSelez().equals(IID_STRINGAVUOTA)) {
							c125.setA_125_0(areaDatiPass.getInterTitMus().getPresentazioneSelez().toString());
							cercaDocMusicaType.setT125(c125);
						}
					}

					A928 a928 = new A928();
					// Forma Musicale

					int countForma = 0;
					if (areaDatiPass.getInterTitMus().getFormaMusicaleSelez1() != null) {
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez1().equals(IID_STRINGAVUOTA)) {
							countForma++;
						}
					}
					if (areaDatiPass.getInterTitMus().getFormaMusicaleSelez2() != null) {
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez2().equals(IID_STRINGAVUOTA)) {
							countForma++;
						}
					}
					if (areaDatiPass.getInterTitMus().getFormaMusicaleSelez3() != null) {
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez3().equals(IID_STRINGAVUOTA)) {
							countForma++;
						}
					}

					if (countForma > 0) {
						String[] formaMusicale = new String[countForma];
						countForma = 0;
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez1().equals(IID_STRINGAVUOTA)) {
							formaMusicale[countForma] = areaDatiPass.getInterTitMus().getFormaMusicaleSelez1().toString();
							countForma++;
						}
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez2().equals(IID_STRINGAVUOTA)) {
							formaMusicale[countForma] = areaDatiPass.getInterTitMus().getFormaMusicaleSelez2().toString();
							countForma++;
						}
						if (!areaDatiPass.getInterTitMus().getFormaMusicaleSelez3().equals(IID_STRINGAVUOTA)) {
							formaMusicale[countForma] = areaDatiPass.getInterTitMus().getFormaMusicaleSelez3().toString();
							countForma++;
						}
						a928.setA_928(formaMusicale);
						cercaDocMusicaType.setT928(a928);
					}

					A929 a929 = new A929();
					// Tonalità
					if (areaDatiPass.getInterTitMus().getTonalitaSelez() != null) {
						if (!areaDatiPass.getInterTitMus().getTonalitaSelez().equals(IID_STRINGAVUOTA)) {
							a929.setE_929(areaDatiPass.getInterTitMus().getTonalitaSelez().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// Numero opera
					if (areaDatiPass.getInterTitMus().getNumOpera() != null) {
						if (!areaDatiPass.getInterTitMus().getNumOpera().equals(IID_STRINGAVUOTA)) {
							a929.setB_929(areaDatiPass.getInterTitMus().getNumOpera().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// Numero ordine
					if (areaDatiPass.getInterTitMus().getNumOrdine() != null) {
						if (!areaDatiPass.getInterTitMus().getNumOrdine().equals(IID_STRINGAVUOTA)) {
							a929.setA_929(areaDatiPass.getInterTitMus().getNumOrdine().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// Numero catalogo
					if (areaDatiPass.getInterTitMus().getNumCatalogoTem() != null) {
						if (!areaDatiPass.getInterTitMus().getNumCatalogoTem().equals(IID_STRINGAVUOTA)) {
							a929.setC_929(areaDatiPass.getInterTitMus().getNumCatalogoTem().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// //////////////// START DATE COMPOSIZIONE
					// DATA INIZIO - DA
					if (areaDatiPass.getInterTitMus().getDataCompInizioDa() != null) {
						if (!areaDatiPass.getInterTitMus().getDataCompInizioDa().equals(IID_STRINGAVUOTA)) {
							cercaDocMusicaType.setDataInizio_Da(areaDatiPass.getInterTitMus().getDataCompInizioDa());
							if (areaDatiPass.getInterTitMus().getDataCompInizioA() != null) {
								if (areaDatiPass.getInterTitMus().getDataCompInizioA().equals(IID_STRINGAVUOTA)) {
									cercaDocMusicaType.setDataInizio_A(areaDatiPass.getInterTitMus().getDataCompInizioDa());
								}
							}
						}
					}
					if (areaDatiPass.getInterTitMus().getDataCompInizioA() != null) {
						// DATA INIZIO - A
						if (!areaDatiPass.getInterTitMus().getDataCompInizioA().equals(IID_STRINGAVUOTA)) {
							cercaDocMusicaType.setDataInizio_A(areaDatiPass.getInterTitMus().getDataCompInizioA());
						}
					}
					// DATA FINE - DA
					if (areaDatiPass.getInterTitMus().getDataCompFineDa() != null) {
						if (!areaDatiPass.getInterTitMus().getDataCompFineDa().equals(IID_STRINGAVUOTA)) {
							cercaDocMusicaType.setDataFine_Da(areaDatiPass.getInterTitMus().getDataCompFineDa());
							if (areaDatiPass.getInterTitMus().getDataCompFineA() != null) {
								if (areaDatiPass.getInterTitMus().getDataCompFineA().equals(IID_STRINGAVUOTA)) {
									cercaDocMusicaType.setDataFine_A(areaDatiPass.getInterTitMus().getDataCompFineDa());
								}
							}
						}
					}
					// DATA FINE - A
					if (areaDatiPass.getInterTitMus().getDataCompFineA() != null) {
						if (!areaDatiPass.getInterTitMus().getDataCompFineA().equals(IID_STRINGAVUOTA)) {
							cercaDocMusicaType.setDataFine_A(areaDatiPass.getInterTitMus().getDataCompFineA());
						}
					}

					// ////////////////FINE DATE COMPOSIZIONE
					// Organico sintetico Comp.
					if (areaDatiPass.getInterTitMus().getOrganicoSintetCompl() != null) {
						if (!areaDatiPass.getInterTitMus().getOrganicoSintetCompl().equals(IID_STRINGAVUOTA)) {
							a928.setB_928(areaDatiPass.getInterTitMus().getOrganicoSintetCompl().toString());
							cercaDocMusicaType.setT928(a928);
						}
					}
					// Organico Analitico Comp.
					if (areaDatiPass.getInterTitMus().getOrganicoAnalitCompl() != null) {
						if (!areaDatiPass.getInterTitMus().getOrganicoAnalitCompl().equals(IID_STRINGAVUOTA)) {
							a928.setC_928(areaDatiPass.getInterTitMus().getOrganicoAnalitCompl().toString());
							cercaDocMusicaType.setT928(a928);
						}
					}
					// TITOLO ORDINAMENTO
					if (areaDatiPass.getInterTitMus().getTitoloOrdinamento() != null) {
						if (!areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals(IID_STRINGAVUOTA)) {
							a929.setG_929(areaDatiPass.getInterTitMus().getTitoloOrdinamento().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// TITOLO DI ESTRATTO
					if (areaDatiPass.getInterTitMus().getTitoloEstratto() != null) {
						if (!areaDatiPass.getInterTitMus().getTitoloEstratto().equals(IID_STRINGAVUOTA)) {
							a929.setH_929(areaDatiPass.getInterTitMus().getTitoloEstratto().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}
					// APPELLATIVO
					if (areaDatiPass.getInterTitMus().getAppellativo() != null) {
						if (!areaDatiPass.getInterTitMus().getAppellativo().equals(IID_STRINGAVUOTA)) {
							a929.setI_929(areaDatiPass.getInterTitMus().getAppellativo().toString());
							cercaDocMusicaType.setT929(a929);
						}
					}

					cercaDocMusicaType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				}

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// FINE MATERIALE MUSICA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE GRAFICA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				if (interTitGen.getTipoMateriale().equals("G")) {
					CercaDocGraficaType cercaDocGraficaType = (CercaDocGraficaType) cercaDatiTitType;

					C116 c116 = new C116();
					// DESIGNAZIONE SPECIFICA MATERIALE
					if (areaDatiPass.getInterTitGra().getDesignSpecMaterSelez() != null) {
						if (!areaDatiPass.getInterTitGra().getDesignSpecMaterSelez().equals(IID_STRINGAVUOTA)) {
							c116.setA_116_0(areaDatiPass.getInterTitGra().getDesignSpecMaterSelez().toString());
							cercaDocGraficaType.setT116(c116);
						}
					}
					// Supporto Primario
					if (areaDatiPass.getInterTitGra().getSupportoPrimarioSelez() != null) {
						if (!areaDatiPass.getInterTitGra().getSupportoPrimarioSelez().equals(IID_STRINGAVUOTA)) {
							c116.setA_116_1(areaDatiPass.getInterTitGra().getSupportoPrimarioSelez().toString());
							cercaDocGraficaType.setT116(c116);
						}
					}

					// Indicatore Colore
					if (areaDatiPass.getInterTitGra().getIndicatoreColoreSelez() != null) {
						if (!areaDatiPass.getInterTitGra().getIndicatoreColoreSelez().equals(IID_STRINGAVUOTA)) {
							c116.setA_116_3(areaDatiPass.getInterTitGra().getIndicatoreColoreSelez().toString());
							cercaDocGraficaType.setT116(c116);
						}
					}
					// Indicatore di tecnica


						// CAMPI PER TECNICADISEGNI TECD
						int countDisegni = 0;
						if (areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Disegno() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Disegno().equals(IID_STRINGAVUOTA)) {
								countDisegni++;
							}
						} else
							areaDatiPass.getInterTitGra().setIndicatoreTecnicaSelez1Disegno(IID_STRINGAVUOTA);

						if (areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Disegno() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Disegno().equals(IID_STRINGAVUOTA)) {
								countDisegni++;
							}
						} else
							areaDatiPass.getInterTitGra().setIndicatoreTecnicaSelez2Disegno(IID_STRINGAVUOTA);

						if (areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Disegno() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Disegno().equals(IID_STRINGAVUOTA)) {
								countDisegni++;
							}
						} else
							areaDatiPass.getInterTitGra().setIndicatoreTecnicaSelez3Disegno(IID_STRINGAVUOTA);

						if (countDisegni > 0) {

							String[] DECT = new String[countDisegni];
							countDisegni = 0;

							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Disegno().equals(IID_STRINGAVUOTA)) {
								DECT[countDisegni] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Disegno().toString();
								countDisegni++;
							}
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Disegno().equals(IID_STRINGAVUOTA)) {
								DECT[countDisegni] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Disegno().toString();
								countDisegni++;
							}
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Disegno().equals(IID_STRINGAVUOTA)) {
								DECT[countDisegni] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Disegno().toString();
								countDisegni++;
							}
							c116.setA_116_4(DECT);
							cercaDocGraficaType.setT116(c116);
						}


						//	CAMPI PER TECNICASTAMPE TECS
						int countGrafica = 0;
						if (areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Grafica() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Grafica().equals(IID_STRINGAVUOTA)) {
								countGrafica++;
							}
						} else
							areaDatiPass.getInterTitGra()
									.setIndicatoreTecnicaSelez1Grafica(IID_STRINGAVUOTA);

						if (areaDatiPass.getInterTitGra()
								.getIndicatoreTecnicaSelez2Grafica() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Grafica().equals(IID_STRINGAVUOTA)) {
								countGrafica++;
							}
						} else
							areaDatiPass.getInterTitGra().setIndicatoreTecnicaSelez2Grafica(IID_STRINGAVUOTA);

						if (areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Grafica() != null) {
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Grafica().equals(IID_STRINGAVUOTA)) {
								countGrafica++;
							}
						} else
							areaDatiPass.getInterTitGra().setIndicatoreTecnicaSelez3Grafica(IID_STRINGAVUOTA);

						if (countGrafica > 0) {

							String[] TECS = new String[countGrafica];
							countGrafica = 0;

							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Grafica().equals(IID_STRINGAVUOTA)) {
								TECS[countGrafica] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez1Grafica().toString();
								countGrafica++;
							}
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Grafica().equals(IID_STRINGAVUOTA)) {
								TECS[countGrafica] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez2Grafica().toString();
								countGrafica++;
							}
							if (!areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Grafica().equals(IID_STRINGAVUOTA)) {
								TECS[countGrafica] = areaDatiPass.getInterTitGra().getIndicatoreTecnicaSelez3Grafica().toString();
								countGrafica++;
							}
							c116.setA_116_10(TECS);
							cercaDocGraficaType.setT116(c116);
						}



					// Designazione di funzione
					if (areaDatiPass.getInterTitGra().getDesignatoreFunzioneSelez() != null) {
						if (!areaDatiPass.getInterTitGra().getDesignatoreFunzioneSelez().equals(IID_STRINGAVUOTA)) {
							c116.setA_116_16(areaDatiPass.getInterTitGra().getDesignatoreFunzioneSelez().toString());
							cercaDocGraficaType.setT116(c116);
						}
					}
				}

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// FINE MATERIALE GRAFICA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE CARTOGRAFIA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				if (interTitGen.getTipoMateriale().equals("C")) {
					CercaDocCartograficoType cercaDocCartograficoType = (CercaDocCartograficoType) cercaDatiTitType;
					// NUMERO E TIPO STANDART
					NumStdType numStdType = new NumStdType();
					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						numStdType.setNumeroSTD(interTitGen.getNumStandard1().toString());
						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,interTitGen.getNumStandardSelez());
// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//						SbnTipoSTD sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//						numStdType.setTipoSTD(sbnTipoSTD);
						numStdType.setTipoSTD(descNumStandard);

						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					// Meridiano d'origine
					C120 c120 = new C120();
					if (areaDatiPass.getInterTitCar().getMeridianoOrigineSelez() != null) {
						if (!areaDatiPass.getInterTitCar().getMeridianoOrigineSelez().equals(IID_STRINGAVUOTA)) {
							c120.setA_120_0(IID_SPAZIO);
							c120.setA_120_9(areaDatiPass.getInterTitCar().getMeridianoOrigineSelez());
							cercaDocCartograficoType.setT120(c120);
						}
					}
					C123 c123 = new C123();

					// Tipo Scala (a, b, c - lineare, angolare, altro)
					if (areaDatiPass.getInterTitCar().getTipoScalaRadio() != null) {
						if (!areaDatiPass.getInterTitCar().getTipoScalaRadio().equals(IID_STRINGAVUOTA)) {
							if (areaDatiPass.getInterTitCar().getTipoScalaRadio().equals("lineare")) {
								c123.setA_123("a");
							}
							if (areaDatiPass.getInterTitCar().getTipoScalaRadio().equals("angolare")) {
								c123.setA_123("b");
							}
							if (areaDatiPass.getInterTitCar().getTipoScalaRadio().equals("altro")) {
								c123.setA_123("c");
							}
							cercaDocCartograficoType.setT123(c123);
						}
					}

					// Scala orizzontale
					if (areaDatiPass.getInterTitCar().getScalaH() != null) {
						if (!areaDatiPass.getInterTitCar().getScalaH().equals(IID_STRINGAVUOTA)) {
							c123.setB_123(areaDatiPass.getInterTitCar().getScalaH());
							cercaDocCartograficoType.setT123(c123);
						}
					}
					// Scala verticale
					if (areaDatiPass.getInterTitCar().getScalaV() != null) {
						if (!areaDatiPass.getInterTitCar().getScalaV().equals(IID_STRINGAVUOTA)) {
							c123.setC_123(areaDatiPass.getInterTitCar().getScalaV());
							cercaDocCartograficoType.setT123(c123);
						}
					}

					// COORDINATE
					// TextField e rispettive ComboBox devono essere entrambe valorizzate perchè il campo coordinate
					// deve essere composto da 8 caratteri (1+7).
					if (areaDatiPass.getInterTitCar().getLongitudineInput1() != null) {
						if (!areaDatiPass.getInterTitCar().getLongitudineInput1().equals(IID_STRINGAVUOTA)) {
							String value1 =
								areaDatiPass.getInterTitCar().getLongitTipo1() + areaDatiPass.getInterTitCar().getLongitudineInput1();
							c123.setD_123(value1);
							cercaDocCartograficoType.setT123(c123);
						}
					}
					if (areaDatiPass.getInterTitCar().getLongitudineInput2() != null) {
						if (!areaDatiPass.getInterTitCar().getLongitudineInput2().equals(IID_STRINGAVUOTA)) {
							String value2 =
								areaDatiPass.getInterTitCar().getLongitTipo2() + areaDatiPass.getInterTitCar().getLongitudineInput2();
							c123.setE_123(value2);
							cercaDocCartograficoType.setT123(c123);
						}
					}
					if (areaDatiPass.getInterTitCar().getLatitudineInput1() != null) {
						if (!areaDatiPass.getInterTitCar().getLatitudineInput1().equals(IID_STRINGAVUOTA)) {
							String value3 =
								areaDatiPass.getInterTitCar().getLatitTipo1() + areaDatiPass.getInterTitCar().getLatitudineInput1();
							c123.setF_123(value3);
							cercaDocCartograficoType.setT123(c123);
						}
					}
					if (areaDatiPass.getInterTitCar().getLatitudineInput2() != null) {
						if (!areaDatiPass.getInterTitCar().getLatitudineInput2().equals(IID_STRINGAVUOTA)) {
							String value4 =
								areaDatiPass.getInterTitCar().getLatitTipo2() + areaDatiPass.getInterTitCar().getLatitudineInput2();
							c123.setG_123(value4);
							cercaDocCartograficoType.setT123(c123);
						}
					}
					// SETTO DI default id del campo C123 A 1
					c123.setId1(Indicatore.VALUE_1);

					cercaDocCartograficoType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

				}

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// FINE MATERIALE CARTOGRAFIA ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE AUDIOVISIVO ////////////

				if (interTitGen.getTipoMateriale().equals("H")) {
					CercaDocAudiovisivoType cercaDocAudiovisivoType = (CercaDocAudiovisivoType) cercaDatiTitType;
					// NUMERO E TIPO STANDART
					NumStdType numStdType = new NumStdType();
					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						numStdType.setNumeroSTD(interTitGen.getNumStandard1().toString());
						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,
								interTitGen.getNumStandardSelez());
						// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//						SbnTipoSTD sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//						numStdType.setTipoSTD(sbnTipoSTD);
						numStdType.setTipoSTD(descNumStandard);

						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					cercaDocAudiovisivoType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

					//almaviva5_20150225 filtri audiovisivo
					InterrogazioneTitoloAudiovisivoVO interTitAud = areaDatiPass.getInterTitAud();
					FiltriAudiovisivoCercaType filtri = new FiltriAudiovisivoCercaType();
					String tipoVideo = interTitAud.getTipoVideoSelez();
					if (ValidazioneDati.isFilled(tipoVideo))
						filtri.setA1150(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_TIPO_VIDEO, tipoVideo));

					String formaPubblDistr = interTitAud.getFormaPubblDistrSelez();
					if (ValidazioneDati.isFilled(formaPubblDistr))
						filtri.setA1158(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_FORMA_PUBBLICAZIONE_DISTRIBUZIONE, formaPubblDistr));

					String tecnicaVideoFilm = interTitAud.getTecnicaVideoFilmSelez();
					if (ValidazioneDati.isFilled(tecnicaVideoFilm))
						filtri.setA1159(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_TECNICA_VIDEOREGISTRAZIONI_E_FILM, tecnicaVideoFilm));

					String formaPubb = interTitAud.getFormaPubblicazioneSelez();
					if (ValidazioneDati.isFilled(formaPubb))
						filtri.setA1260(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_FORMA_PUBBLICAZIONE_AUDIOVIDEO, formaPubb));

					String velocita = interTitAud.getVelocitaSelez();
					if (ValidazioneDati.isFilled(velocita))
						filtri.setA1261(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_VELOCITA, velocita));

					cercaDocAudiovisivoType.setFiltriAudiovisivoCerca(filtri);


				}
				// ////// FINE MATERIALE AUDIOVISIVO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////

				// ////////////////////////////////////////////////////////////////////////////////////
				// ////// INIZIO MATERIALE ELETTRONICO ////////////

				if (interTitGen.getTipoMateriale().equals("L")) {
					CercaDocElettronicoType cercaDocElettronicoType = (CercaDocElettronicoType) cercaDatiTitType;
					// NUMERO E TIPO STANDART
					NumStdType numStdType = new NumStdType();
					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						numStdType.setNumeroSTD(interTitGen.getNumStandard1().toString());
						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					if (!interTitGen.getNumStandard1().equals(IID_STRINGAVUOTA)) {
						String descNumStandard = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD,
								interTitGen.getNumStandardSelez());
						// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//						SbnTipoSTD sbnTipoSTD = SbnTipoSTD.valueOf(descNumStandard);
//						numStdType.setTipoSTD(sbnTipoSTD);
						numStdType.setTipoSTD(descNumStandard);

						cercaDatiTitTypeChoice.setNumSTD(numStdType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
						cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
					}

					cercaDocElettronicoType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);


					// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
					// Gestione nuovi campi specifici per etichetta 13
					InterrogazioneTitoloElettronicoVO interTitEle= areaDatiPass.getInterTitEle();
					FiltriElettronicoCercaType filtri = new FiltriElettronicoCercaType();
					String tipoRisorsa = interTitEle.getTipoRisorsaElettronicaSelez();
					if (ValidazioneDati.isFilled(tipoRisorsa))
						filtri.setA1350(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_TIPO_RISORSA_ELETTRONICA, tipoRisorsa));
					String indicazioneMateriale = interTitEle.getIndicazioneSpecificaMaterialeSelez();
					if (ValidazioneDati.isFilled(indicazioneMateriale))
						filtri.setA1351(CodiciProvider.SBNToUnimarc(CodiciType.CODICE_INDICAZIONE_SPECIFICA_MATERIALE, indicazioneMateriale));
					cercaDocElettronicoType.setFiltriElettronicoCerca(filtri);


				}
				// ////// FINE MATERIALE ELETTRONICO ////////////
				// ////////////////////////////////////////////////////////////////////////////////////


				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);

			} // FINE delle impostazioni da interrogazione Titolo usando le 4
			// aree di passaggio

			// Inizio richieste liste Sintetiche da altri oggetti

			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameElementoAutType legameElementoAutType = new LegameElementoAutType();
			SbnAuthority sbnAuthority;
			SbnLegameAut sbnLegameAut;

			if (areaDatiPass.getOggDiRicerca() != null && !areaDatiPass.getOggDiRicerca().equals("")) {
				if (tipoOggetto == 99 ) {
					if	(tipoOggettoFiltrato < 99 ) {
						tipoOggetto = tipoOggettoFiltrato;
					}
				}
				switch (tipoOggetto) {
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_TITOLO:

					String NaturaDaLista = areaDatiPass.getNaturaTitBase();
					// Inizio modifica almaviva2 02.11.2009 inserito controllo per valore null BUG MANTIS 3287
					String TMaterialeDaLista = ValidazioneDati.trimOrEmpty(areaDatiPass.getTipMatTitBase());
					// Fine modifica almaviva2 02.11.2009 inserito controllo per valore null BUG MANTIS 3287
					if ((NaturaDaLista.toUpperCase().equals("A"))
							&& (TMaterialeDaLista.toUpperCase().equals("U"))) {
						sbnAuthority = SbnAuthority.UM;
						sbnLegameAut = SbnLegameAut.valueOf("tutti");
						legameElementoAutType.setTipoAuthority(sbnAuthority);
						legameElementoAutType.setTipoLegame(sbnLegameAut);
						legameElementoAutType.setIdArrivo(areaDatiPass
								.getOggDiRicerca());

						if (!areaDatiPass.getCodiceLegame().equals("")) {
							sbnLegameAut = SbnLegameAut.valueOf(areaDatiPass
									.getCodiceLegame().substring(1, 4));
							legameElementoAutType.setTipoLegame(sbnLegameAut);
						}
						arrivoLegame.setLegameElementoAut(legameElementoAutType);
						cercaTitoloType.setArrivoLegame(arrivoLegame);
					} else if ((NaturaDaLista.toUpperCase().equals("A"))
							&& (TMaterialeDaLista.toUpperCase().equals(""))) {
						sbnAuthority = SbnAuthority.TU;
						sbnLegameAut = SbnLegameAut.valueOf("tutti");
						legameElementoAutType.setTipoAuthority(sbnAuthority);
						legameElementoAutType.setTipoLegame(sbnLegameAut);
						legameElementoAutType.setIdArrivo(areaDatiPass
								.getOggDiRicerca());
						if (!areaDatiPass.getCodiceLegame().equals("")) {
							sbnLegameAut = SbnLegameAut.valueOf(areaDatiPass
									.getCodiceLegame().substring(1, 4));
							legameElementoAutType.setTipoLegame(sbnLegameAut);
						}
						arrivoLegame.setLegameElementoAut(legameElementoAutType);
						cercaTitoloType.setArrivoLegame(arrivoLegame);
					} else if ((NaturaDaLista.toUpperCase().equals("B"))
							|| (NaturaDaLista.toUpperCase().equals("D"))
							|| (NaturaDaLista.toUpperCase().equals("T"))
							|| (NaturaDaLista.toUpperCase().equals("P"))) {
						LegameTitAccessoType legameTitAccessoType = new LegameTitAccessoType();
						SbnLegameTitAccesso sbnLegameTitAccesso = SbnLegameTitAccesso.VALUE_0;
						legameTitAccessoType.setTipoLegame(sbnLegameTitAccesso);
						legameTitAccessoType.setIdArrivo(areaDatiPass
								.getOggDiRicerca());
						if (!areaDatiPass.getCodiceLegame().equals("")) {
							sbnLegameTitAccesso = SbnLegameTitAccesso
									.valueOf(areaDatiPass.getCodiceLegame()
											.substring(1, 4));
							legameTitAccessoType.setTipoLegame(sbnLegameTitAccesso);
						}
						arrivoLegame.setLegameTitAccesso(legameTitAccessoType);
						cercaTitoloType.setArrivoLegame(arrivoLegame);
					}
					// se sono nature M S C W F N
					// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
					else if ((NaturaDaLista.toUpperCase().equals("M"))
							|| (NaturaDaLista.toUpperCase().equals("S"))
							|| (NaturaDaLista.toUpperCase().equals("C"))
							|| (NaturaDaLista.toUpperCase().equals("W"))
							|| (NaturaDaLista.toUpperCase().equals("F"))
							|| (NaturaDaLista.toUpperCase().equals("N"))
							|| (NaturaDaLista.toUpperCase().equals("R"))) {
						LegameDocType legameDocType = new LegameDocType();

						// PASSO IL VALORE "Tutti"
						SbnLegameDoc sbnLegameDoc = SbnLegameDoc.VALUE_0;
						legameDocType.setTipoLegame(sbnLegameDoc);
						legameDocType.setIdArrivo(areaDatiPass.getOggDiRicerca());
						if (!areaDatiPass.getCodiceLegame().equals("")) {
							sbnLegameDoc = SbnLegameDoc.valueOf(areaDatiPass
									.getCodiceLegame().substring(1, 4));
							legameDocType.setTipoLegame(sbnLegameDoc);
						}
						if (!areaDatiPass.getCodiceSici().equals("")) {
							legameDocType.setSici(areaDatiPass.getCodiceSici());
						}

						arrivoLegame.setLegameDoc(legameDocType);
						cercaTitoloType.setArrivoLegame(arrivoLegame);
						visualNumSequenza = "SI";
					}
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_AUTORE:
					sbnAuthority = SbnAuthority.AU;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					// Inizio Manutenzione interna - 16.11.2009 - per eliminare l'ordinamento tipo 4 che in questa richiesta non esiste.
					if (cercaType.getTipoOrd().toString().equals("4")) {
						cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
					}
					// Fine Manutenzione interna - 16.11.2009 -
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_MARCA:
					sbnAuthority = SbnAuthority.MA;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_LUOGO:
					sbnAuthority = SbnAuthority.LU;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO:
					sbnAuthority = SbnAuthority.SO;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE:
					sbnAuthority = SbnAuthority.CL;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					break;
				case TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_THESAURO:
					sbnAuthority = SbnAuthority.TH;
					sbnLegameAut = SbnLegameAut.valueOf("tutti");
					legameElementoAutType.setTipoAuthority(sbnAuthority);
					legameElementoAutType.setTipoLegame(sbnLegameAut);
					legameElementoAutType.setIdArrivo(areaDatiPass
							.getOggDiRicerca());
					arrivoLegame.setLegameElementoAut(legameElementoAutType);
					cercaTitoloType.setArrivoLegame(arrivoLegame);
					break;
				}
			}

			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			int totRighe = 0;
			if (areaDatiPass.isRicercaPolo()) {

				//almaviva5_20091007 Inserimento filtro per Biblioteca
				List<BibliotecaVO> filtroLocBib = areaDatiPass.getFiltroLocBib();
				if (ValidazioneDati.isFilled(filtroLocBib)) {
					for (BibliotecaVO bib : filtroLocBib) {
						CdBibType bibType = new CdBibType();
						bibType.setCdPol(bib.getCod_polo());
						bibType.setCdBib(bib.getCod_bib());
						cercaTitoloType.addLocBib(bibType);
					}
				}

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
						//almaviva5_20091012
						cercaTitoloType.clearLocBib();	// non supportato

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

			SinteticaTitoli st = new SinteticaTitoli();

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			List listaSintetica = st.getSintetica(sbnRisposta, false, 0, visualNumSequenza);

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

			areaDatiPassReturn.setListaSintetica(listaSintetica);

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

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoliPerGestionali(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass) {

		if (areaDatiPass.getRicercaPerGest() == null) {
			areaDatiPass.setRicercaPerGest("NO");
		}

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		if (!areaDatiPass.isRicercaPolo() && !areaDatiPass.isRicercaIndice()) {
			areaDatiPassReturn.setCodErr("livRicObblig");
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}



		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();

			// INIZIA PROGRESSIVO DI DEFAULT SETTO A 1
			cercaType.setNumPrimo(1);

			if (areaDatiPass.getInterTitGen() == null) {
				cercaType.setMaxRighe(10);
				cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			} else {
				if (areaDatiPass.getInterTitGen().getElemXBlocchi() != 0) {
					cercaType.setMaxRighe(areaDatiPass.getInterTitGen().getElemXBlocchi());
				} else {
					cercaType.setMaxRighe(10);
				}

				if (areaDatiPass.getInterTitGen().getFormatoListaSelez() == null
						|| areaDatiPass.getInterTitGen().getFormatoListaSelez().equals(IID_STRINGAVUOTA)) {
					cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
				} else {
					cercaType.setTipoOutput(SbnTipoOutput.valueOf(areaDatiPass.getInterTitGen().getFormatoListaSelez()));
				}

				if (areaDatiPass.getInterTitGen().getTipoOrdinamSelez() == null
						|| areaDatiPass.getInterTitGen().getTipoOrdinamSelez().equals(IID_STRINGAVUOTA)) {
					cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
				} else {
					cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass.getInterTitGen().getTipoOrdinamSelez()));
				}
			}

			if (areaDatiPass.getTipoProspettazioneGestionali().equals("LocFonSeg")) {
				String esito = controlloFormaleDati(areaDatiPass);
				if (!esito.equals("")) {
					areaDatiPassReturn.setCodErr(esito);
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
				// Valorizzazione aree per la ricerca SbnMarc
				CercaDatiTitType cercaDatiTitType = null;
				cercaDatiTitType = new CercaDocMusicaType();

				CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();

				SbnMateriale sbnMateriale = SbnMateriale.valueOf(areaDatiPass.getInterTitGen().getTipoMateriale());
				SbnMateriale[] sbnMatArr = new SbnMateriale[1];
				sbnMatArr[0] = sbnMateriale;
				cercaDatiTitType.setTipoMateriale(sbnMatArr);

				CercaDocMusicaType cercaDocMusicaType = (CercaDocMusicaType) cercaDatiTitType;
				C899 c899 = new C899();

				// localizzazione
				if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
					if (!areaDatiPass.getInterTitMus().getLocalizzazione().equals(IID_STRINGAVUOTA)) {
						c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
						c899.setC2_899(areaDatiPass.getInterTitMus().getLocalizzazione().toString());
						cercaDocMusicaType.setT899(c899);
					}
				}
				// fondo
				if (areaDatiPass.getInterTitMus().getFondo() != null) {
					if (!areaDatiPass.getInterTitMus().getFondo().equals(IID_STRINGAVUOTA)) {
						c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
						c899.setB_899(areaDatiPass.getInterTitMus().getFondo().toString());
						cercaDocMusicaType.setT899(c899);
					}
				}
				// Segnatura
				if (areaDatiPass.getInterTitMus().getSegnatura() != null) {
					if (!areaDatiPass.getInterTitMus().getSegnatura().equals(IID_STRINGAVUOTA)) {
						c899.setTipoInfo(SbnTipoLocalizza.TUTTI);
						c899.setG_899(areaDatiPass.getInterTitMus().getSegnatura().toString());
						cercaDocMusicaType.setT899(c899);
					}
				}
				cercaDocMusicaType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);

			} else if (areaDatiPass.getTipoProspettazioneGestionali().equals("Inventario")
					|| areaDatiPass.getTipoProspettazioneGestionali().equals("Ordine")) {
				if (areaDatiPass.getInterTitGen().getBid() != null
						&& !areaDatiPass.getInterTitGen().getBid().equals("")) {
					CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
					CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();
					cercaDatiTitTypeChoice.setT001(areaDatiPass.getInterTitGen().getBid());
					cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
					cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
				} else {
					areaDatiPassReturn.setCodErr("0000");
					areaDatiPassReturn.setNumNotizie(0);
					return areaDatiPassReturn;
				}
			}

			cercaType.setCercaTitolo(cercaTitoloType);

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

			SinteticaTitoli st = new SinteticaTitoli();

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			List listaSintetica = st.getSintetica(sbnRisposta, false, 0, "NO");

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

			areaDatiPassReturn.setListaSintetica(listaSintetica);

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


	public String controlloFormaleDati(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass) {
		String esito = "";

		if (areaDatiPass.getRicercaPerGest().equals("SI")) {
			if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("U")) {

				if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
					if ((areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
							&& ((!areaDatiPass.getInterTitMus().getFondo().equals("")) ||
								(!areaDatiPass.getInterTitMus().getSegnatura().equals("")))) {
						return "ric012";
					}
				}

				if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
					if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(
							""))
							&& ((areaDatiPass.getInterTitMus().getFondo()
									.equals("")) && (areaDatiPass.getInterTitMus()
									.getSegnatura().equals("")))) {
						return "ric012";
					}
				}

				if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
					if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(
							""))) {
						if ((areaDatiPass.getInterTitMus().getLocalizzazione()
								.length() < 6)) {
							return "ric013";
						}
					}
				}
			}
			return esito;
		}

		int canaliprimari = 0;
		if (!areaDatiPass.getInterTitGen().getTitolo().equals("")) {
			canaliprimari = canaliprimari + 1;
		}
		if (!areaDatiPass.getInterTitGen().getBid().equals("")) {
			canaliprimari = canaliprimari + 1;
		}
		if (!areaDatiPass.getInterTitGen().getNumStandardSelez().equals("")) {
			canaliprimari = canaliprimari + 1;
		}
		if (areaDatiPass.getInterTitGen().getImpronta1().equals("")
				&& areaDatiPass.getInterTitGen().getImpronta2().equals("")
				&& areaDatiPass.getInterTitGen().getImpronta3().equals("")) {
		} else {
			canaliprimari = canaliprimari + 1;
		}
		if (canaliprimari > 1) {
			return "soloUnCanPrim";
		}

		UtilityDate utilityDate = new UtilityDate();

		// CONTROLLO DELLE DATE PRESENTI SUL PANNELLO
		// Date pubblicazione (Da1-A1)
		  /**
		   * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
		   * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
		   * o uguale al carattere punto '.';
		   * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
		   */

		/**if (!areaDatiPass.getInterTitGen().getData1Da().equals("")) {
			try {
				Integer.parseInt(areaDatiPass.getInterTitGen().getData1Da());
			} catch (NumberFormatException e) {
				return "formDataInv";
			}
		}
		if (!areaDatiPass.getInterTitGen().getData1A().equals("")) {
			try {
				Integer.parseInt(areaDatiPass.getInterTitGen().getData1A());
			} catch (NumberFormatException e) {
				return "formDataInv";
			}
		}
		if (!areaDatiPass.getInterTitGen().getData2Da().equals("")) {
			try {
				Integer.parseInt(areaDatiPass.getInterTitGen().getData2Da());
			} catch (NumberFormatException e) {
				return "formDataInv";
			}
		}
		if (!areaDatiPass.getInterTitGen().getData2A().equals("")) {
			try {
				Integer.parseInt(areaDatiPass.getInterTitGen().getData2A());
			} catch (NumberFormatException e) {
				return "formDataInv";
			}
		}
		 */
		String testoErr="";
		if (!areaDatiPass.getInterTitGen().getData1Da().equals("")) {
			testoErr = utilityDate.verificaFormatoDataParziale(areaDatiPass.getInterTitGen().getData1Da(), "Data1Da");
			if (testoErr.length()>0) {
				return testoErr;
			}
		}

		if (!areaDatiPass.getInterTitGen().getData1A().equals("")) {
			testoErr = utilityDate.verificaFormatoDataParziale(areaDatiPass.getInterTitGen().getData1A(), "Data1A");
			if (testoErr.length()>0) {
				return testoErr;
			}
		}

		if (!areaDatiPass.getInterTitGen().getData2Da().equals("")) {
			testoErr = utilityDate.verificaFormatoDataParziale(areaDatiPass.getInterTitGen().getData2Da(), "Data2Da");
			if (testoErr.length()>0) {
				return testoErr;
			}
		}

		if (!areaDatiPass.getInterTitGen().getData2A().equals("")) {
			testoErr = utilityDate.verificaFormatoDataParziale(areaDatiPass.getInterTitGen().getData2A(), "Data2A");
			if (testoErr.length()>0) {
				return testoErr;
			}
		}
	   // almaviva2 - Settembre 2014 - FINE Evolutiva per la gestione delle date del titolo per gestire il carattere punto -

		esito = utilityDate.isOkControlloDate(areaDatiPass.getInterTitGen()
				.getData1Da(), areaDatiPass.getInterTitGen().getData1A());
		if (!esito.equals("")) {
			return esito;
		}
		// Date pubblicazione (Da2-A2)
		esito = utilityDate.isOkControlloDate(areaDatiPass.getInterTitGen()
				.getData2Da(), areaDatiPass.getInterTitGen().getData2A());
		if (!esito.equals("")) {
			return esito;
		}

		if (areaDatiPass.getInterTitGen().getTitolo().equals("")
				&& areaDatiPass.getInterTitGen().getBid().equals("")
				&& ((!areaDatiPass.getInterTitGen().getNumStandard1()
						.equals("")) && (areaDatiPass.getInterTitGen()
						.getNumStandardSelez().equals("")))
				|| ((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) && (!areaDatiPass
						.getInterTitGen().getNumStandardSelez().equals("")))
				&& areaDatiPass.getInterTitGen().getNumStandardSelez().equals(
						"")
				&& areaDatiPass.getInterTitGen().getImpronta1().equals("")
				&& areaDatiPass.getInterTitGen().getImpronta2().equals("")
				&& areaDatiPass.getInterTitGen().getImpronta3().equals("")) {
			return "ric003";
		}

		if (((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) && (areaDatiPass
				.getInterTitGen().getNumStandardSelez().equals("")))
				|| ((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) && (!areaDatiPass
						.getInterTitGen().getNumStandardSelez().equals("")))) {
			return "ric005";
		}

		int intAutoreLuogoMarcaCollegata = 0;

		// verifico il nome collegato Autore
		if (!areaDatiPass.getInterTitGen().getNomeCollegato().equals("")) {
			intAutoreLuogoMarcaCollegata = intAutoreLuogoMarcaCollegata + 1;
		}

		// verifico luogo
		if (!areaDatiPass.getInterTitGen().getLuogoPubbl().equals("")) {
			intAutoreLuogoMarcaCollegata = intAutoreLuogoMarcaCollegata + 1;
		}

		// ho selezionato un campo tra Autore marca o luogo
		if ((intAutoreLuogoMarcaCollegata == 1)
				|| (intAutoreLuogoMarcaCollegata == 0)) {
		} else {
			return "ric004";
		}

		// ANTICO
		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("E")) {
		}
		// FINE ANTICO

		// CARTOGRAFIA
		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("C")) {

			// Titolo//BID//IDLISTA//Numero Standart & Numero standart
			// Tipo//Tipo scala & o coordinate
			if ((areaDatiPass.getInterTitGen().getTitolo().equals(""))
					&& (areaDatiPass.getInterTitGen().getBid().equals(""))
					&& (areaDatiPass.getInterTitGen().getNumStandard1().equals(""))
					&& (areaDatiPass.getInterTitGen().getNumStandardSelez().equals(""))
					&& (areaDatiPass.getInterTitCar().getTipoScalaRadio().equals(""))
					&& (areaDatiPass.getInterTitCar().getLongitudineInput2().equals(""))
					&& (areaDatiPass.getInterTitCar().getLatitudineInput1().equals(""))
					&& (areaDatiPass.getInterTitCar().getLongitudineInput1().equals(""))
					&& (areaDatiPass.getInterTitCar().getLatitudineInput2().equals(""))
					&& ((areaDatiPass.getInterTitCar().getLongitTipo1().equals("")))
					&& ((areaDatiPass.getInterTitCar().getLongitTipo2().equals("")))
					&& ((areaDatiPass.getInterTitCar().getLatitTipo1().equals("")))
					&& ((areaDatiPass.getInterTitCar().getLatitTipo2().equals("")))) {
				if (areaDatiPass.getOggDiRicerca() == null) areaDatiPass.setOggDiRicerca("");
				if (areaDatiPass.getOggDiRicerca().equals("")) {
					return "ric003";
				}
			}

			int appoggioCoord = 0;
			if (!areaDatiPass.getInterTitCar().getLongitudineInput1()
					.equals("")) {
				try {
					appoggioCoord = Integer.parseInt(areaDatiPass.getInterTitCar().getLongitudineInput1());
				} catch (NumberFormatException e) {
					return "ric017";
				}
				if (appoggioCoord > 3600000) {
					return "ric017";
				}
			}
			if (!areaDatiPass.getInterTitCar().getLongitudineInput2().equals("")) {
				try {
					appoggioCoord = Integer.parseInt(areaDatiPass.getInterTitCar().getLongitudineInput2());
				} catch (NumberFormatException e) {
					return "ric018";
				}
				if (appoggioCoord > 3600000) {
					return "ric018";
				}
			}
			if (!areaDatiPass.getInterTitCar().getLatitudineInput1().equals("")) {
				try {
					appoggioCoord = Integer.parseInt(areaDatiPass.getInterTitCar().getLatitudineInput1());
				} catch (NumberFormatException e) {
					return "ric019";
				}
				if (appoggioCoord > 900000) {
					return "ric019";
				}
			}
			if (!areaDatiPass.getInterTitCar().getLatitudineInput2().equals("")) {
				try {
					appoggioCoord = Integer.parseInt(areaDatiPass.getInterTitCar().getLatitudineInput2());
				} catch (NumberFormatException e) {
					return "ric020";
				}
				if (appoggioCoord > 900000) {
					return "ric020";
				}
			}

			// //////////// Controllo accoppiata campi/combo coordinate
			boolean coppiaVuotaLong1 = false;
			boolean coppiaVuotaLong2 = false;
			boolean coppiaVuotaLat1 = false;
			boolean coppiaVuotaLat2 = false;
			String COORDINATA_VUOTA = "";
			if ((areaDatiPass.getInterTitCar().getLongitTipo1().equals(""))
					&& (areaDatiPass.getInterTitCar().getLongitudineInput1().equals(COORDINATA_VUOTA))) {
				coppiaVuotaLong1 = true;
			}
			if ((areaDatiPass.getInterTitCar().getLongitTipo2().equals(""))
					&& (areaDatiPass.getInterTitCar().getLongitudineInput2().equals(COORDINATA_VUOTA))) {
				coppiaVuotaLong2 = true;
			}
			if ((areaDatiPass.getInterTitCar().getLatitTipo1().equals(""))
					&& (areaDatiPass.getInterTitCar().getLatitudineInput1().equals(COORDINATA_VUOTA))) {
				coppiaVuotaLat1 = true;
			}
			if ((areaDatiPass.getInterTitCar().getLatitTipo2().equals(""))
					&& (areaDatiPass.getInterTitCar().getLatitudineInput2().equals(COORDINATA_VUOTA))) {
				coppiaVuotaLat2 = true;
			}

			// CONTROLLO MERIDIANO D'ORIGINE E COORDINATE

			if (areaDatiPass.getInterTitCar().getMeridianoOrigineSelez() != null) {
				if (!areaDatiPass.getInterTitCar().getMeridianoOrigineSelez()
						.equals("")) {
					if ((coppiaVuotaLong1) && (coppiaVuotaLong2)
							&& (coppiaVuotaLat1) && (coppiaVuotaLat2)) {
						return "ric006";
					}
				}
			}
			if (areaDatiPass.getInterTitCar().getMeridianoOrigineSelez() != null) {
				if (areaDatiPass.getInterTitCar().getMeridianoOrigineSelez()
						.equals("")) {
					if ((!coppiaVuotaLong1) || (!coppiaVuotaLong2)
							|| (!coppiaVuotaLat1) || (!coppiaVuotaLat2)) {
						return "ric006";
					}
				}
			}

			// Coppia combo/campo nr.1
			if (((areaDatiPass.getInterTitCar().getLongitTipo1().equals(""))
					&& (!areaDatiPass.getInterTitCar().getLongitudineInput1().equals(COORDINATA_VUOTA)))
					|| ((!areaDatiPass.getInterTitCar().getLongitTipo1().equals(""))
							&& (areaDatiPass.getInterTitCar().getLongitudineInput1().equals(COORDINATA_VUOTA)))) {
				return "ric007";
			}
			// Coppia combo/campo nr.2
			if (((areaDatiPass.getInterTitCar().getLongitTipo2().equals(""))
					&& (!areaDatiPass.getInterTitCar().getLongitudineInput2().equals(COORDINATA_VUOTA)))
					|| ((!areaDatiPass.getInterTitCar().getLongitTipo2().equals(""))
							&& (areaDatiPass.getInterTitCar().getLongitudineInput2().equals(COORDINATA_VUOTA)))) {
				return "ric008";
			}
			// Coppia combo/campo nr.3
			if (((areaDatiPass.getInterTitCar().getLatitTipo1().equals(""))
					&& (!areaDatiPass.getInterTitCar().getLatitudineInput1().equals(COORDINATA_VUOTA)))
					|| ((!areaDatiPass.getInterTitCar().getLatitTipo1().equals(""))
							&& (areaDatiPass.getInterTitCar().getLatitudineInput1().equals(COORDINATA_VUOTA)))) {
				return "ric009";
			}
			// Coppia combo/campo nr.4
			if (((areaDatiPass.getInterTitCar().getLatitTipo2().equals(""))
					&& (!areaDatiPass.getInterTitCar().getLatitudineInput2().equals(COORDINATA_VUOTA)))
					|| ((!areaDatiPass.getInterTitCar().getLatitTipo2().equals(""))
							&& (areaDatiPass.getInterTitCar()
							.getLatitudineInput2().equals(COORDINATA_VUOTA)))) {
				return "ric010";
			}

		}
		// FINE CARTOGRAFIA

		// GRAFICA
		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("G")) {
		}

		// FINE GRAFICA

		// MODERNO
		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("M")) {
		}
		// FINE MODERNO

		// Inserimento controlli per natura a (Titolo uniforme musicale)

		boolean naturaA = false;
		if (areaDatiPass.getInterTitGen().getNaturaSelez1().equals("A")
				|| areaDatiPass.getInterTitGen().getNaturaSelez2().equals("A")
				|| areaDatiPass.getInterTitGen().getNaturaSelez3().equals("A")
				|| areaDatiPass.getInterTitGen().getNaturaSelez4().equals("A")) {
			naturaA = true;
		}

		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("U") && naturaA) {

			if (areaDatiPass.getInterTitMus().getTitoloOrdinamento() != null) {
				if (((areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals("")) &&
						((!areaDatiPass.getInterTitMus().getTitoloEstratto().equals("")) &&
								(!areaDatiPass.getInterTitMus().getAppellativo().equals(""))))
						|| ((areaDatiPass.getInterTitMus().getTitoloEstratto().equals("")) &&
								((!areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals("")) &&
										(!areaDatiPass.getInterTitMus().getAppellativo().equals(""))))
						|| ((areaDatiPass.getInterTitMus().getAppellativo().equals("")) &&
								((!areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals("")) &&
										(!areaDatiPass.getInterTitMus().getTitoloEstratto().equals(""))))
						|| ((!areaDatiPass.getInterTitMus().getAppellativo().equals("")) &&
								((!areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals("")) &&
										(!areaDatiPass.getInterTitMus().getTitoloEstratto().equals(""))))) {
					return "ric011";
				}
			}

			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
						&& ((!areaDatiPass.getInterTitMus().getFondo().equals("")) || (!areaDatiPass.getInterTitMus().getSegnatura().equals("")))) {
					return "ric012";
				}
			}

			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
						&& ((areaDatiPass.getInterTitMus().getFondo().equals("")) && (areaDatiPass.getInterTitMus().getSegnatura().equals("")))) {
					return "ric012";
				}
			}

			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))) {
					if ((areaDatiPass.getInterTitMus().getLocalizzazione().length() < 6)) {
						return "ric013";
					}
				}
			}

			if (((((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
					(areaDatiPass.getInterTitGen().getNumStandard1().equals(""))) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))
					|| ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
							((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
									(areaDatiPass.getInterTitGen().getNumStandard2().equals(""))))
					|| ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
							((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
									(!areaDatiPass.getInterTitGen().getNumStandard2().equals("")))))) {
				return "ric005";
			}

			if (((((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
					(areaDatiPass.getInterTitGen().getNumStandard1().equals(""))) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))
					|| ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
							((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
									(areaDatiPass.getInterTitGen().getNumStandard2().equals(""))))
					|| ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
							((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
									(!areaDatiPass.getInterTitGen().getNumStandard2().equals("")))))) {
				return "ric005";
			}

			// Titolo//BID//IDLISTA//Numero Standart & Numero standart Tipo
			// Impronta//Localizzazione + Fondo + Segnatura//Localizzazione +
			// Fondo//Localizzazione + Segnatura

			// Manutenzione del 08.02.2013 Inserito controllo sul null in tutti i campi di tipo Musicale
			// BUG 5195 (Collaudo): Da autore Chopin chiedo i titoli collegati con filtro: natura A e campi specifici;
			// se non inserisco ache un titolo il sistema va in errore (Null Exception)
			if ((areaDatiPass.getInterTitGen().getTitolo().equals(""))
					&& (areaDatiPass.getInterTitGen().getBid().equals(""))
					&& (areaDatiPass.getInterTitGen().getNumStandard1().equals(""))
					&& (areaDatiPass.getInterTitGen().getNumStandard2().equals(""))
					&& (areaDatiPass.getInterTitGen().getImpronta1().equals(""))
					&& (areaDatiPass.getInterTitGen().getImpronta2().equals(""))
					&& (areaDatiPass.getInterTitGen().getImpronta3().equals(""))
					&& (areaDatiPass.getInterTitMus().getLocalizzazione() == null || areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
					&& (areaDatiPass.getInterTitMus().getFondo() == null || areaDatiPass.getInterTitMus().getFondo().equals(""))
					&& (areaDatiPass.getInterTitMus().getSegnatura() == null || areaDatiPass.getInterTitMus().getSegnatura().equals(""))
					&& (areaDatiPass.getInterTitMus().getTitoloOrdinamento() == null || areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals(""))
					&& (areaDatiPass.getInterTitMus().getTitoloEstratto() == null || areaDatiPass.getInterTitMus().getTitoloEstratto().equals(""))
					&& (areaDatiPass.getInterTitMus().getAppellativo() == null || areaDatiPass.getInterTitMus().getAppellativo().equals(""))) {
				return "ric014";
			}
			if (areaDatiPass.getInterTitGen().getTitolo() != null) {
				if (areaDatiPass.getInterTitGen().getTitolo().equals("")) {

					if (areaDatiPass.getInterTitMus().getTitoloOrdinamento() != null) {
						if ((areaDatiPass.getInterTitMus().getTitoloOrdinamento().equals(""))
								&& (areaDatiPass.getInterTitMus().getTitoloEstratto().equals(""))
								&& (areaDatiPass.getInterTitMus().getAppellativo().equals(""))) {
							return "ric014";
						}
					}
				}
			}
		}

		// FINE MUSICA CON NATURA A

		// INIZIO MUSICA SENZA NATURA A
		// if (panel.getTipoMateriale().equals("U")) { // MUSICA
		if (areaDatiPass.getInterTitGen().getTipoMateriale().equals("U") && !naturaA) {
			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
						&& ((!areaDatiPass.getInterTitMus().getFondo().equals("")) ||
								(!areaDatiPass.getInterTitMus().getSegnatura().equals("")))) {
					return "ric012";
				}
			}

			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))
						&& ((areaDatiPass.getInterTitMus().getFondo().equals("")) &&
								(areaDatiPass.getInterTitMus().getSegnatura().equals("")))) {
					return "ric012";
				}
			}

			if (areaDatiPass.getInterTitMus().getLocalizzazione() != null) {
				if ((!areaDatiPass.getInterTitMus().getLocalizzazione().equals(""))) {
					if ((areaDatiPass.getInterTitMus().getLocalizzazione().length() < 6)) {
						return "ric013";
					}
				}
			}

			if (((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
					(areaDatiPass.getInterTitGen().getNumStandardSelez().equals("")))
					|| ((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
							(!areaDatiPass.getInterTitGen().getNumStandardSelez().equals("")))) {
				return "ric005";
			}


			if ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
					((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))) {
				areaDatiPass.getInterTitGen().setNumStandard2(areaDatiPass.getInterTitGen().getNumStandard1());
			}


			if (((((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
					(areaDatiPass.getInterTitGen().getNumStandard1().equals(""))) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))
					||
					((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
					((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals(""))))
					||
					((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("L")) &&
					((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
					(!areaDatiPass.getInterTitGen().getNumStandard2().equals("")))))) {
				return "ric005";
			}

			if (((((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
					(areaDatiPass.getInterTitGen().getNumStandard1().equals(""))) &&
					(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))
					|| ((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
							((!areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
									(areaDatiPass.getInterTitGen().getNumStandard2().equals("")))) ||
									((areaDatiPass.getInterTitGen().getNumStandardSelez().equals("E")) &&
											((areaDatiPass.getInterTitGen().getNumStandard1().equals("")) &&
													(!areaDatiPass.getInterTitGen().getNumStandard2().equals("")))))) {
				return "ric005";
			}

			// Titolo//BID//IDLISTA//Numero Standart & Numero standart Tipo
			// Impronta//Localizzazione + Fondo + Segnatura//Localizzazione +
			// Fondo//Localizzazione + Segnatura

			if (areaDatiPass.getInterTitMus().getLocalizzazione() == null) {
				areaDatiPass.getInterTitMus().setLocalizzazione("");
			}
			if (areaDatiPass.getInterTitMus().getFondo() == null) {
				areaDatiPass.getInterTitMus().setFondo("");
			}
			if (areaDatiPass.getInterTitMus().getSegnatura() == null) {
				areaDatiPass.getInterTitMus().setSegnatura("");
			}

		}

		// FINE MUSICA
		// CONTROLLI GENERICI che devono comunque essere fatti indipendentemente
		// dal tipo materiale

		if (areaDatiPass.getInterTitGen().getResponsabilitaSelez() != null) {
			if (!areaDatiPass.getInterTitGen().getResponsabilitaSelez().equals("")) {
				if (!areaDatiPass.getInterTitGen().getNomeCollegato().equals("")) {
				} else {
					return "ric015";
				}
			}
		}

		if (areaDatiPass.getInterTitGen().getRelazioniSelez() != null) {
			if (!areaDatiPass.getInterTitGen().getRelazioniSelez().equals("")) {
				if (!areaDatiPass.getInterTitGen().getNomeCollegato().equals("")) {
				} else {
					return "ric016";
				}
			}
		}
		if (areaDatiPass.getInterTitGen().getSottoNaturaDSelez().length() > 0
				&& (!areaDatiPass.getInterTitGen().getNaturaSelez1().equals("D")
						&& !areaDatiPass.getInterTitGen().getNaturaSelez2().equals("D")
						&& !areaDatiPass.getInterTitGen().getNaturaSelez3().equals("D")
						&& !areaDatiPass.getInterTitGen().getNaturaSelez4().equals("D"))) {
			return "selObblNaturaD";
		}

		return esito;
	}

	/**
	 * Ottiene un'altro blocco della lista sintetica dei Titoli.
	 *
	 *
	 * @return Oggetto Castor
	 */

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoTitoli(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass) {
		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		int numPrimo = areaDatiPass.getNumPrimo();
		int maxRighe = areaDatiPass.getMaxRighe();
		int numNotizie = 0;
		String idLista = areaDatiPass.getIdLista();

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
			TitoloCercaType titoloCercaType = new TitoloCercaType();
			StringaCercaType stringaCercaType = new StringaCercaType();

			cercaType.setNumPrimo(numPrimo);
			cercaType.setMaxRighe(maxRighe);
			cercaType.setIdLista(idLista);
			cercaType.setTipoOutput(SbnTipoOutput.valueOf(areaDatiPass
					.getTipoOutput()));
			cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass
					.getTipoOrdinam()));

			titoloCercaType.setStringaCerca(stringaCercaType);
			cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			String chiamata = IID_STRINGAVUOTA;
			if (areaDatiPass.isRicercaPolo()) {
				chiamata = "P";
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}
			if (areaDatiPass.isRicercaIndice()) {
				chiamata = "I";
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

			// Intervento interno - almaviva217 nov. 2009 - qiunado si carica il blocco sucessivo al primo, nel caso
			// della ricerca dei simili, l'indice rispondo con il bloco successivo ma imposta comunque il codice 3004 per indicare
			// l'elenco dei simili; quindi anche 3004 indica che l'interrogazione ha avuto esito positivo.
			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito());
				areaDatiPassReturn.setNumNotizie(0);
				return areaDatiPassReturn;
			}

			areaDatiPassReturn.setLivelloTrovato(chiamata);

			SinteticaTitoli st = new SinteticaTitoli();

			numPrimo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getNumPrimo();
			numNotizie = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe();

			areaDatiPassReturn.setNumNotizie(numNotizie);

			// Inizio modifica - almaviva2 16.11.2009 BUG MANTIS 3333 . Si Imposta a "SI" il valore per la prospettazione del num.Sequenza
			// nel caso di tipoOrd = 4 (SEQUENZA)
			// Modifica - almaviva2 18.01.2010 BUG MANTIS 3494 . Si Imposta a "SI" il valore per la prospettazione del num.Sequenza
			// anche nel caso di richiesta di sintetica per collegamento

			List listaSinteticaSuccessiva = new ArrayList();
			if (areaDatiPass.getTipoOrdinam().equals("4")
					|| areaDatiPass.isPresenzaSequenzaLegame()) {
				listaSinteticaSuccessiva = st.getSintetica(sbnRisposta,	false, --numPrimo, "SI");
			} else {
				listaSinteticaSuccessiva = st.getSintetica(sbnRisposta,	false, --numPrimo, "NO");
			}
			// Fine modifica - almaviva2 16.11.2009 BUG MANTIS 3333 . Si Imposta a "SI" il valore per la prospettazione del num.Sequenza


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

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoTitoliPerBID(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setNumPrimo(1);

			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1

			cercaDatiTitTypeChoice.setT001(areaDatiPass.getBidRicerca());
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
			String chiamata = IID_STRINGAVUOTA;
			if (areaDatiPass.isRicercaPolo()) {
				chiamata = "P";

				// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
				// operante che nel caso di centro Sistema non coincidono
				if (!this.user.getBiblioteca().equals(areaDatiPass.getCodiceBiblioSbn())) {
					if (ValidazioneDati.notEmpty(areaDatiPass.getCodiceBiblioSbn())) {
						this.user.setBiblioteca(areaDatiPass.getCodiceBiblioSbn());
					}
				}
				// Fine Modifica almaviva2 16.07.2010


				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}
			if (areaDatiPass.isRicercaIndice()) {
				areaDatiPassLocal.setIndice(true);
				areaDatiPassLocal.setPolo(false);
				// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
				// operante che nel caso di centro Sistema non coincidono
				areaDatiPassLocal.setCodiceSbn(areaDatiPass.getCodiceBiblioSbn());
//				areaDatiPassLocal.setCodiceSbn(this.user.getBiblioteca());
				// Fine Modifica almaviva2 16.07.2010
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

			areaDatiPassReturn.setLivelloTrovato(chiamata);

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				areaDatiPassReturn.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				return areaDatiPassReturn;
			} else if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("9999");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPassReturn;
			}

			if (areaDatiPass.isInviaSoloTimeStampRadice()) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
					DocumentoType documentoType = new DocumentoType();
					documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
					if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
						areaDatiPassReturn.setTimeStampRadice(documentoType.getDocumentoTypeChoice().getDatiDocumento().getT005());
					} else if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
						areaDatiPassReturn.setTimeStampRadice(documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT005());
					}
				} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
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
			AreaPassaggioReticoloTitoliVO areaPassaggioReticoloTitoliVO = new AreaPassaggioReticoloTitoliVO();
			areaPassaggioReticoloTitoliVO = this.getReticoloTitolo(sbnRisposta,
					root, chiamata, areaDatiPassLocal);

			areaDatiPassReturn
					.setTreeElementViewTitoli(areaPassaggioReticoloTitoliVO
							.getTreeElementViewTitoli());
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
	 * Metodo inserisciTitolo
	 *
	 * @param frame
	 * @return SBNMarc
	 *
	 * Gestisce la chiamata a metodi inserimento diversi a seconda della natura
	 * selezionata dall'utente.
	 */

	public AreaDatiVariazioneReturnVO inserisciTitolo(
			AreaDatiVariazioneTitoloVO areaDatiPass) {

		char natura = ' ';
		String idErrore = "";
		if (areaDatiPass.isModifica()) {
			natura = areaDatiPass.getNaturaTitoloDaVariare().charAt(0);
		} else {
			natura = areaDatiPass.getDetTitoloPFissaVO().getNatura().charAt(0);
		}

		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		switch (natura) {
		case 'A':
		case 'V':
			idErrore = RichiestaInserimentoTitoloUniforme(areaDatiPass);
			break;

		case 'C':
		case 'R':
		case 'M':
		case 'S':
		case 'W':
		case 'N':
			idErrore = RichiestaInserimentoDocumento(areaDatiPass);
			break;
		case 'B':
		case 'D':
		case 'P':
		case 'T':
			idErrore = RichiestaInserimentoTitoloAccesso(areaDatiPass);
			break;
		}

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
				indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("");
		AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();

		if (areaDatiPass.isInserimentoIndice()) {
			if (Integer.valueOf(areaDatiPass.getDetTitoloPFissaVO().getLivAut()) < 5) {
				areaDatiPassReturn.setCodErr("livAutInfMinimoIndice");
				return areaDatiPassReturn;
			}
		}

		// SE I CONTROLLI SONO ANDATI A BUON FINE
		if (!idErrore.equals("")) {
			areaDatiPassReturn.setCodErr(idErrore);
			return areaDatiPassReturn;
		}

		try {
			String tipoInserimento = "";
			String tipoAuthority = "";
			AreaDatiScambioInInserimentoTitoloVO areaDatiScambioInInserimentoTitoloVO = new AreaDatiScambioInInserimentoTitoloVO();
			SbnMessageType sbnMessageAppoggio = null;

			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
			switch (natura) {
			case 'A':
			case 'V':
				tipoInserimento = "TITUNI";
				if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
					tipoAuthority = "UM";
				} else {
					tipoAuthority = "TU";
				}
				break;
			case 'C':
			case 'R':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
				tipoInserimento = "TITTIT";
				tipoAuthority = "";
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				tipoInserimento = "TITACC";
				tipoAuthority = "";
				break;
			}



			String bidDaAssegnare = "";
			if (!areaDatiPass.isModifica()) {
				if (areaDatiPass.isConferma()
						&& (areaDatiPass.getBidTemporaneo() != null && !areaDatiPass.getBidTemporaneo().equals(""))) {
					bidDaAssegnare = areaDatiPass.getBidTemporaneo();
					areaDatiPass.setBidTemporaneo("");
				} else {
					areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
						bidDaAssegnare = areaDatiPass.getDetTitoloPFissaVO().getBid();
					} else {

						if (tipoInserimento.equals("TITTIT") || tipoInserimento.equals("TITACC")) {
							if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("")) {
								areaDatiPassGetIdSbn.setTipoMat(null);
							} else {
								areaDatiPassGetIdSbn.setTipoMat(areaDatiPass.getDetTitoloPFissaVO().getTipoMat());
							}
							if (areaDatiPass.getDetTitoloPFissaVO().getTipoRec().equals("")) {
								areaDatiPassGetIdSbn.setTipoRec(null);
							} else {
								areaDatiPassGetIdSbn.setTipoRec(areaDatiPass.getDetTitoloPFissaVO().getTipoRec());
							}
						} else {
							areaDatiPassGetIdSbn.setTipoAut(tipoAuthority);
						}
						areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
						if (areaDatiPassGetIdSbn.getIdSbn() == null || areaDatiPassGetIdSbn.getIdSbn().equals("")) {
							areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn.getCodErr());
							areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn.getTestoProtocollo());
							return areaDatiPassReturn;
						} else {
							bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
						}
					}
				}
			} else {
				areaDatiPass.getDetTitoloPFissaVO().getBid();
			}

			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
			switch (natura) {
			case 'A':
			case 'V':
				areaDatiScambioInInserimentoTitoloVO = inserisciTitoloUniforme(areaDatiPass, bidDaAssegnare);
				if (areaDatiScambioInInserimentoTitoloVO.getCodErr().equals("9999")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(areaDatiScambioInInserimentoTitoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				sbnMessageAppoggio = areaDatiScambioInInserimentoTitoloVO.getSbnMessageAppoggio();
				break;
			case 'C':
			case 'R':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
				areaDatiScambioInInserimentoTitoloVO = inserisciDocumento(areaDatiPass, bidDaAssegnare);
				if (areaDatiScambioInInserimentoTitoloVO.getCodErr().equals("9999")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(areaDatiScambioInInserimentoTitoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				sbnMessageAppoggio = areaDatiScambioInInserimentoTitoloVO.getSbnMessageAppoggio();
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				areaDatiScambioInInserimentoTitoloVO = inserisciTitoloAccesso(areaDatiPass, bidDaAssegnare);
				if (areaDatiScambioInInserimentoTitoloVO.getCodErr().equals("9999")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(areaDatiScambioInInserimentoTitoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				sbnMessageAppoggio = areaDatiScambioInInserimentoTitoloVO.getSbnMessageAppoggio();
				break;
			}

			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
			if (!areaDatiPass.isModifica()) {
				switch (natura) {
				case 'A':
				case 'V':
					sbnMessageAppoggio.getSbnRequest().getCrea()
							.getCreaTypeChoice().getElementoAut()
							.getDatiElementoAut().setT001(bidDaAssegnare);
					break;
				case 'C':
				case 'R':
				case 'M':
				case 'S':
				case 'W':
				case 'N':
					sbnMessageAppoggio.getSbnRequest().getCrea()
							.getCreaTypeChoice().getDocumento()
							.getDocumentoTypeChoice().getDatiDocumento()
							.setT001(bidDaAssegnare);
					break;
				case 'B':
				case 'D':
				case 'P':
				case 'T':
					sbnMessageAppoggio.getSbnRequest().getCrea()
							.getCreaTypeChoice().getDocumento()
							.getDocumentoTypeChoice().getDatiTitAccesso()
							.setT001(bidDaAssegnare);
					break;
				}
			}

			if (areaDatiPass.isLegameInf()) {

				LegamiType[] arrayLegamiType = new LegamiType[1];
				LegamiType legamiType;
				ArrivoLegame arrivoLegame;

				legamiType = new LegamiType();
				legamiType.setIdPartenza(bidDaAssegnare);
				legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

				arrivoLegame = new ArrivoLegame();

				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
				if (areaDatiPass.getNaturaTitoloDaVariare().equals("V") && areaDatiPass.getTipoLegame().equals("08")) {
					LegameElementoAutType legameElemento = new LegameElementoAutType();
					legameElemento.setNoteLegame(areaDatiPass.getNoteLegame());
					legameElemento.setTipoAuthority(SbnAuthority.TU);
					legameElemento.setTipoLegame(SbnLegameAut.valueOf("431"));
					legameElemento.setIdArrivo(areaDatiPass.getBidArrivo());

					// Intervento interno almaviva2: manca la dichiarazione di condivisione del legame per cui passando
					// il valore null, i legami vengono creati con la n di default come fossero legami solo locali;
					legameElemento.setCondiviso(areaDatiPass.isFlagCondiviso() ?
							LegameElementoAutTypeCondivisoType.S :
							LegameElementoAutTypeCondivisoType.N);

					arrivoLegame.setLegameElementoAut(legameElemento);

					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					// Gennaio 2018 almaviva2 - correzione per creazione legame A??V
					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[0] = legamiType;

					sbnMessageAppoggio.getSbnRequest().getCrea()
							.getCreaTypeChoice().getElementoAut().setLegamiElementoAut(arrayLegamiType);
					sbnMessageAppoggio.getSbnRequest().getCrea().setTipoControllo(SbnSimile.CONFERMA);
				} else  {
					LegameDocType legameDocType = new LegameDocType();
					legameDocType.setNoteLegame(areaDatiPass.getNoteLegame());
					legameDocType.setSequenza(areaDatiPass.getSequenza());
					// Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
					// Inizio modifica almaviva2 BUG MANTIS 4131 (Collaudo): inserito controllo su contenuto SICI
					// che deve essere impostato solo se valorizzato
					if (areaDatiPass.getSici() != null && !areaDatiPass.getSici().equals("")) {
						legameDocType.setSici(areaDatiPass.getSici());
					}
					// Fine modifica almaviva2 BUG MANTIS 4131 (Collaudo)
					// Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->

					legameDocType.setTipoLegame(SbnLegameDoc.valueOf("461"));
					legameDocType.setIdArrivo(areaDatiPass.getBidArrivo());

					// Intervento interno almaviva2: manca la dichiarazione di condivisione del legame per cui passando
					// il valore null, i legami vengono creati con la n di default come fossero legami solo locali;
					legameDocType.setCondiviso(areaDatiPass.isFlagCondiviso() ?
							LegameDocTypeCondivisoType.S :
							LegameDocTypeCondivisoType.N);

					arrivoLegame.setLegameDoc(legameDocType);

					ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
					arrayArrivoLegame[0] = arrivoLegame;

					legamiType.setArrivoLegame(arrayArrivoLegame);
					arrayLegamiType[0] = legamiType;

					sbnMessageAppoggio.getSbnRequest().getCrea()
							.getCreaTypeChoice().getDocumento().setLegamiDocumento(arrayLegamiType);
				}


			}

			if (areaDatiPass.isLegameDaCondividere()) {

				LegamiType[] arrayLegamiType = new LegamiType[1];
				arrayLegamiType[0] = areaDatiPass.getLegamiTypeDaCondividere();

				// MANTIS BUG 5434 (esercizio LIG) L'invio in indice di titoli locali di natura A quando hanno un legame ad un Autore
				// o altra authority produceva il messaggio di errore ERROR>>null. dovuto all'assegnazione senza effettuare controlli
				// dell'area "arrayLegamiType" al Documento invece che a quella della e nature A (setLegamiElementoAut e non setLegamiDocumento)
//				sbnMessageAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().setLegamiDocumento(arrayLegamiType);
				if (sbnMessageAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento() != null) {
					sbnMessageAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().setLegamiDocumento(arrayLegamiType);
				} else if (sbnMessageAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut() != null) {
					sbnMessageAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().setLegamiElementoAut(arrayLegamiType);
				}
			}

			// Inizio Intervento Interno 12.11.2012 - A seguito del bug MANTIS INDICE 5179 (LIG);
			// in inserimento/variazione di un titolo Musicale non veniva catturato l'autore definito come Interprete
			// scaturendo così l'errore del protocollo "Autore non presente in base dati"
			if (areaDatiPass.getDetTitoloMusVO() != null
					&& areaDatiPass.getDetTitoloMusVO().getListaPersonaggi() != null
					&& areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				String result;
				for (int i = 0; i < areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size(); i++) {
					tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().get(i);
					if (tabImpST.getNota() != null) {
						result = gestioneAllAuthority.inserisciInterpreteDocumentoCatturato(tabImpST.getNota());
						if (!result.equals("0000") && !result.equals("")) {
							areaDatiPassReturn.setCodErr(result);
						}
					}
				}
			}
			// Fine Intervento Interno 12.11.2012 - A seguito del bug MANTIS INDICE 5179 (LIG);

			if (!areaDatiPass.isModifica()) {
				if (areaDatiPass.isInserimentoIndice() && areaDatiPass.isFlagCondiviso()) {
					// CASO DI CREAZIONE TITOLO IN INDICE: Operazioni da
					// compiere
					// 1. inserimento in indice;
					// 2. inserimento in locale;
					// 3. localizzazione in indice;
					// 3. localizzazione in polo;
					areaDatiPassReturn = chiamataInsertIndice(
							sbnMessageAppoggio, areaDatiPass.isConferma(),
							tipoAuthority, bidDaAssegnare, areaDatiPass.isPropagaLocMadre(), areaDatiPass.getBidArrivo());
				}
				if (areaDatiPass.isInserimentoIndice() && !areaDatiPass.isFlagCondiviso()) {
					// CASO DI CONDIVISIONE TITOLO CATALOGATO LOCALMENTE IN INDICE: Operazioni da
					// compiere
					// 1. inserimento in indice;
					// 2. aggiornamento in locale (solo per la parte relativa al flagCondiviso che passa da N a S;
					// 3. localizzazione in indice;
					String timeStampPolo = areaDatiPass.getTimeStampPolo();
					areaDatiPassReturn = chiamataInsertIndiceUdatePolo(
							sbnMessageAppoggio, areaDatiPass.isConferma(),
							tipoAuthority, bidDaAssegnare, timeStampPolo);
				}

				if (areaDatiPass.isInserimentoPolo() && !areaDatiPass.isFlagCondiviso()) {
					// CASO DI CREAZIONE TITOLO IN POLO: Operazioni da
					// compiere
					// 2. inserimento in locale;
					// 3. localizzazione SOLO in polo CATALOGAZIONE LOCALE;
					areaDatiPassReturn = chiamataInsertLocale(
							sbnMessageAppoggio, areaDatiPass.isConferma(),
							tipoAuthority, bidDaAssegnare, areaDatiPass.isPropagaLocMadre(), areaDatiPass.getBidArrivo() );
				}
			}
			if (areaDatiPass.isModifica()) {
				if (areaDatiPass.isInserimentoIndice()) {
					// CASO DI MODIFICA TITOLO IN INDICE: Operazioni da compiere
					// 1. variazione in indice;
					// 2. variazione in locale;
					areaDatiPassReturn = chiamataUpdateIndice(
							sbnMessageAppoggio, areaDatiPass.isConferma(),
							tipoAuthority, areaDatiPass.getDetTitoloPFissaVO().getBid(), tipoInserimento);
					if (!areaDatiPassReturn.getCodErr().equals("")) {
						return areaDatiPassReturn;
					}

				}
				if (areaDatiPass.isInserimentoPolo() && !areaDatiPass.isFlagCondiviso()) {
					// CASO DI MODIFICA TITOLO SU TITOLO CATALOGATO IN LOCALE:
					// Operazioni da compiere
					// 2. variazione in locale;
					areaDatiPassReturn = chiamataUpdateLocale(
							sbnMessageAppoggio, areaDatiPass.isConferma(),
							tipoAuthority, areaDatiPass.getDetTitoloPFissaVO().getBid());

				}
			}

		} catch (IllegalArgumentException ie) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
			return areaDatiPassReturn;
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
			return areaDatiPassReturn;

		}
		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataInsertIndice(
			SbnMessageType sbnAreaAppoggio, boolean conferma,
			String tipoAuthority, String idSbn, boolean PropagaLocMadre, String bidMadre) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
				indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		SBNMarc sbnRisposta;

		try {
			// CASO DI CREAZIONE TITOLO IN INDICE: Operazioni da compiere
			// 1. inserimento in indice;
			this.indice.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
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

			// CONTROLLO SUI SIMILI
			UtilityCastor utilityCastor = new UtilityCastor();
			if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {

				// Sono presenti simili in Indice; non si effettuano altre
				// operazioni e si invia la lista sintetica dei simili prodotta;

				SinteticaTitoli st = new SinteticaTitoli();

				int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getMaxRighe();
				String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

				List listaSintetica = null;
				try {
					listaSintetica = st.getSintetica(sbnRisposta, false, 0, "NO");
				} catch (RemoteException e) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo("ERROR >>"
							+ e.getMessage());
				}

				int numBlocchi = 1;
				if (maxRighe > 0) {
					numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
				}
				areaDatiPassReturn.setLivelloTrovato("I");
				areaDatiPassReturn.setIdLista(idLista);
				areaDatiPassReturn.setMaxRighe(maxRighe);
				areaDatiPassReturn.setTotRighe(totRighe);
				areaDatiPassReturn.setNumBlocco(1);
				areaDatiPassReturn.setNumNotizie(totRighe);
				areaDatiPassReturn.setTotBlocchi(numBlocchi);
				areaDatiPassReturn.setListaSintetica(listaSintetica);
				areaDatiPassReturn.setBidTemporaneo(idSbn);

				return areaDatiPassReturn;
			}

			// 2. inserimento in locale con forzatura per la conferma di
			// Inserimento
			// (senza ricerca dei simili);
			sbnAreaAppoggio.getSbnRequest().getCrea().setTipoControllo(
					SbnSimile.CONFERMA);
			this.polo.setMessage(sbnAreaAppoggio, this.user);

			sbnRisposta = this.polo.eseguiRichiestaServer();
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

			// 3. localizzazione in indice;
			if (PropagaLocMadre) {
				// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
				// viene estesa alla N nuova la localizzazione per possesso della madre (bidArrivo)
				String origineArrivo = "IndiceIndice";
				String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
						bidMadre, idSbn, SbnMateriale.valueOf("M").toString(),
						sbnRisposta.getSbnUser().getBiblioteca(),
						origineArrivo);
				if (!codStringaErrore.substring(0,4).equals("0000")) {
					areaDatiPassReturn.setCodErr(codStringaErrore.substring(0,4));
					if (codStringaErrore.length() > 4) {
						areaDatiPassReturn.setTestoProtocollo(codStringaErrore.substring(5));
					}
					return areaDatiPassReturn;
				}

				origineArrivo = "PoloPolo";
				codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
						bidMadre, idSbn, SbnMateriale.valueOf("M").toString(),
						sbnRisposta.getSbnUser().getBiblioteca(),
						origineArrivo);
				if (!codStringaErrore.substring(0,4).equals("0000")) {
					areaDatiPassReturn.setCodErr(codStringaErrore.substring(0,4));
					if (codStringaErrore.length() > 4) {
						areaDatiPassReturn.setTestoProtocollo(codStringaErrore.substring(5));
					}
					return areaDatiPassReturn;
				}
			} else {
				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(idSbn);
				areaLocalizza.setAuthority(tipoAuthority);
				areaLocalizza
						.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
				areaLocalizza.setTipoOpe("Localizza");
				areaLocalizza.setTipoLoc("Gestione");
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
				areaDatiPassReturn = gestioneAllAuthority
						.localizzaAuthority(areaLocalizza);
				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return areaDatiPassReturn;
				}
			}

			areaDatiPassReturn.setBid(idSbn);

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;

	}


	private AreaDatiVariazioneReturnVO chiamataInsertIndiceUdatePolo(
			SbnMessageType sbnAreaAppoggio, boolean conferma,
			String tipoAuthority, String idSbn, String timeStampPolo) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
				indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		SBNMarc sbnRisposta;
		String verIndice = "";
		String verPolo = "";

		try {
			// CASO DI CREAZIONE TITOLO IN INDICE: Operazioni da compiere
			// 1. inserimento in indice;
			this.indice.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
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

			// Inizio manutenzione almaviva2 2010.11.08 BUG MANTIS 3971
			// In fase di aggiornamento del flag di condivisione si deve verificare se stiamo trattando un Documento
			// o un titolo Uniforme altrimenti ERROR: null

			ModificaType modificaType = null;
			modificaType = new ModificaType();
			modificaType.setTipoControllo(SbnSimile.CONFERMA);

			if (sbnAreaAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento() != null) {
				DocumentoType documentoType = new DocumentoType();
				documentoType.setDocumentoTypeChoice(sbnAreaAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().getDocumentoTypeChoice());
				documentoType.setLegamiDocumento(sbnAreaAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().getLegamiDocumento());
				documentoType.setNLista(0);

				modificaType.setDocumento(documentoType);
				modificaType.getDocumento().setStatoRecord(StatoRecord.C);
				// Modifica su richiesta  - Maggio 2013
				// errore di tipo ERROR: null perchè mancava la gestione del getDatiTitAccesso che è stata ora inserita
				if (modificaType.getDocumento().getDocumentoTypeChoice().getDatiDocumento() !=  null) {
					modificaType.getDocumento().getDocumentoTypeChoice().getDatiDocumento().setT005(timeStampPolo);
					modificaType.getDocumento().getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.S);
				}
				if (modificaType.getDocumento().getDocumentoTypeChoice().getDatiTitAccesso() !=  null) {
					modificaType.getDocumento().getDocumentoTypeChoice().getDatiTitAccesso().setT005(timeStampPolo);
					modificaType.getDocumento().getDocumentoTypeChoice().getDatiTitAccesso().setCondiviso(TitAccessoTypeCondivisoType.S);
				}
				// Intervento interno almaviva2 INIZIO Febbraio 2017: manca la dichiarazione di condivisione del legame:
				// dopo aver condiviso un reticolo oltre ad aggiornare il flag di condivisione del documento si devono aggiornare
				// i flag di condivisione di tutti i legami. inserita la valorizzazione corretta.
				if (modificaType.getDocumento().getLegamiDocumentoCount() > 0) {
					int totLegami = modificaType.getDocumento().getLegamiDocumentoCount();
					for (int cntLegami = 0; cntLegami < totLegami; cntLegami++) {
						LegamiType legami = modificaType.getDocumento().getLegamiDocumento(cntLegami);
						if (legami.getArrivoLegameCount() > 0) {
							legami.setTipoOperazione(SbnTipoOperazione.MODIFICA);
							int totArrivoLegami = legami.getArrivoLegameCount();
							for (int cntArrivoLegami = 0; cntArrivoLegami < totArrivoLegami; cntArrivoLegami++) {
								if (legami.getArrivoLegame(cntArrivoLegami).getLegameDoc()!= null) {
									legami.getArrivoLegame(cntArrivoLegami).getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
								} else if (legami.getArrivoLegame(cntArrivoLegami).getLegameElementoAut()!= null) {
									legami.getArrivoLegame(cntArrivoLegami).getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);
								} else if (legami.getArrivoLegame(cntArrivoLegami).getLegameTitAccesso()!= null) {
									legami.getArrivoLegame(cntArrivoLegami).getLegameTitAccesso().setCondiviso(LegameTitAccessoTypeCondivisoType.S);
								}
							}
						}
					}
				}
				// Intervento interno almaviva2 FINE Febbraio 2017:
			} else if (sbnAreaAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut() != null) {
				ElementAutType elementAutType = new ElementAutType();
				elementAutType.setDatiElementoAut(sbnAreaAppoggio.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getDatiElementoAut());
				elementAutType.setNLista(0);

				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut().setStatoRecord(StatoRecord.C);
				modificaType.getElementoAut().getDatiElementoAut().setT005(timeStampPolo);
				modificaType.getElementoAut().getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.S);

			}

			// Fine manutenzione almaviva2 2010.11.08 BUG MANTIS 3971


			sbnAreaAppoggio.getSbnRequest().setModifica(modificaType);
			sbnAreaAppoggio.getSbnRequest().setCrea(null);

			this.polo.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
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

			// Valorizzazione del campo versione polo con la risposta appena ottenuta

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

			// Inizio modifica almaviva2 BUG MANTIS 3930 del 13.10.2010
			// le operazioni di inserimento localizzazioni per possesso e dati di possesso devono essere fatte con due operazioni differenti
			// inoltre si deve ciclare su tutte le biblioteche del Polo: il punto 3.0 3 .1 viene demandato al nuovo metodo
			// copiaLocalizzazPoloSuIndice su gestioneAllAuthority
			// ULTERIORE modifica almaviva2 BUG MANTIS 3871 del 25.10.2010
			// si inviano sia il bid di partenza che serve per interrogare in Polo che il bid di arrivo che serve per spedire
			// in Indice i dati di localizzazione copiati; nel caso di catalogazione in Indice di elemento locale il bid di partenza
			// e di arrivo coincidono

			// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
			// Intervento interno ottobre 2015 almaviva2 che allarga le funzionalità del metodo
			// copiaLocalizzazPoloSuIndice e lo rende generalizzato copiando le localizzazioni da un oggetto
			// ad un altro da una base dati ad un altra - La funzione viene rinominata copiaLocalizzazDocumento
			String origineArrivo = "PoloIndice";
			String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
					idSbn,
					idSbn,
					//SbnMateriale.VALUE_5.toString(),
					SbnMateriale.valueOf("M").toString(),
					sbnRisposta.getSbnUser().getBiblioteca(),
					origineArrivo);
			if (!codStringaErrore.substring(0,4).equals("0000")) {
				areaDatiPassReturn.setCodErr(codStringaErrore.substring(0,4));
				if (codStringaErrore.length() > 4) {
					areaDatiPassReturn.setTestoProtocollo(codStringaErrore.substring(5));
				}
				return areaDatiPassReturn;
			}


			areaDatiPassReturn.setVersioneIndice(verIndice);
			areaDatiPassReturn.setVersionePolo(verPolo);
			areaDatiPassReturn.setBid(idSbn);

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;

	}

	private AreaDatiVariazioneReturnVO chiamataInsertLocale(
			SbnMessageType sbnAreaAppoggio, boolean conferma,
			String tipoAuthority, String idSbn, boolean PropagaLocMadre, String bidMadre) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
				indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		SBNMarc sbnRisposta;

		try {

			if (sbnAreaAppoggio.getSbnRequest().getCrea()
						.getCreaTypeChoice().getElementoAut() != null) {
				sbnAreaAppoggio.getSbnRequest().getCrea()
				.getCreaTypeChoice().getElementoAut().getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.N);
			} else {
				if (sbnAreaAppoggio.getSbnRequest().getCrea()
						.getCreaTypeChoice().getDocumento()
						.getDocumentoTypeChoice().getDatiDocumento() != null) {
					sbnAreaAppoggio.getSbnRequest().getCrea()
					.getCreaTypeChoice().getDocumento()
					.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);
				}
				if (sbnAreaAppoggio.getSbnRequest().getCrea()
						.getCreaTypeChoice().getDocumento()
						.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
					sbnAreaAppoggio.getSbnRequest().getCrea()
					.getCreaTypeChoice().getDocumento()
					.getDocumentoTypeChoice().getDatiTitAccesso().setCondiviso(TitAccessoTypeCondivisoType.N);
				}
			}

			// CASO DI CREAZIONE TITOLO IN LOCALE: Operazioni da compiere
			// 1. inserimento in locale;



			this.polo.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

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

			// CONTROLLO SUI SIMILI
			UtilityCastor utilityCastor = new UtilityCastor();
			if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {

				// Sono presenti simili in Locale; non si effettuano altre
				// operazioni e si
				// invia la lista sintetica dei simili prodotta;

				SinteticaTitoli st = new SinteticaTitoli();

				int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getMaxRighe();
				String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

				List listaSintetica = null;
				try {
					listaSintetica = st.getSintetica(sbnRisposta, false, 0, "NO");
				} catch (RemoteException e) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo("ERROR >>"
							+ e.getMessage());
				}

				int numBlocchi = 1;
				if (maxRighe > 0) {
					numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
				}

				// Modifica almaviva2 BUG MANTIS 3182 16.12.2010 - impostazione campo del livRicerca sul quale si sta operando
				areaDatiPassReturn.setLivelloTrovato("P");

				areaDatiPassReturn.setIdLista(idLista);
				areaDatiPassReturn.setMaxRighe(maxRighe);
				areaDatiPassReturn.setTotRighe(totRighe);
				areaDatiPassReturn.setNumBlocco(1);
				areaDatiPassReturn.setNumNotizie(totRighe);
				areaDatiPassReturn.setTotBlocchi(numBlocchi);
				areaDatiPassReturn.setListaSintetica(listaSintetica);
				return areaDatiPassReturn;
			}
			if (PropagaLocMadre) {
				// evolutive ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata
				// viene estesa alla N nuova la localizzazione per possesso della madre (bidArrivo)
				String origineArrivo = "PoloPolo";
				String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
						bidMadre, idSbn, SbnMateriale.valueOf("M").toString(),
						sbnRisposta.getSbnUser().getBiblioteca(),
						origineArrivo);
				if (!codStringaErrore.substring(0,4).equals("0000")) {
					areaDatiPassReturn.setCodErr(codStringaErrore.substring(0,4));
					if (codStringaErrore.length() > 4) {
						areaDatiPassReturn.setTestoProtocollo(codStringaErrore.substring(5));
					}
					return areaDatiPassReturn;
				}
			}

			areaDatiPassReturn.setBid(idSbn);
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;

	}

	private AreaDatiVariazioneReturnVO chiamataUpdateIndice(
			SbnMessageType sbnAreaAppoggio, boolean conferma,
			String tipoAuthority, String idSbn, String tipoInserimento) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		SBNMarc sbnRisposta;

		try {
			// CASO DI VARIAZIONE TITOLO IN INDICE: Operazioni da compiere
			// 1. variazione in indice;
			this.indice.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
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

			// CONTROLLO SUI SIMILI
			UtilityCastor utilityCastor = new UtilityCastor();
			if (utilityCastor.isElementiSimiliTrovati(sbnRisposta)) {

				// Sono presenti simili in Indice; non si effettuano altre
				// operazioni e si
				// invia la lista sintetica dei simili prodotta;

				SinteticaTitoli st = new SinteticaTitoli();

				int totRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe();
				int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getMaxRighe();
				String idLista = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput().getIdLista();

				List listaSintetica = null;
				try {
					listaSintetica = st.getSintetica(sbnRisposta, false, 0, "NO");
				} catch (RemoteException e) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo("ERROR >>"
							+ e.getMessage());
				}

				int numBlocchi = 1;
				if (maxRighe > 0) {
					numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
				}
				areaDatiPassReturn.setLivelloTrovato("I");
				areaDatiPassReturn.setIdLista(idLista);
				areaDatiPassReturn.setMaxRighe(maxRighe);
				areaDatiPassReturn.setTotRighe(totRighe);
				areaDatiPassReturn.setNumBlocco(1);
				areaDatiPassReturn.setNumNotizie(totRighe);
				areaDatiPassReturn.setTotBlocchi(numBlocchi);
				areaDatiPassReturn.setListaSintetica(listaSintetica);
				return areaDatiPassReturn;
			}

			// 2. variazione in locale con forzatura per la conferma (senza
			// ricerca
			// dei simili);

			// verifica di esistenza del Documento su Polo
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
					indice, polo, user);
			AreaDatiControlliPoloVO areaDatiControlliPoloVO = null;
			String timeStampPolo = "";
			if (tipoInserimento.equals("TITTIT")) {
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(sbnAreaAppoggio.getSbnRequest()
						.getModifica().getDocumento().getDocumentoTypeChoice()
						.getDatiDocumento().getT001());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getCodErr().equals("0000") &&
						areaDatiControlliPoloVO.getDatiDocType() == null) {
					areaDatiPassReturn.setCodErr("disalPoloIndice");
					return areaDatiPassReturn;

				}

				timeStampPolo = areaDatiControlliPoloVO.getDatiDocType().getT005();
				sbnAreaAppoggio.getSbnRequest().getModifica()
					.getDocumento().getDocumentoTypeChoice()
					.getDatiDocumento().setT005(timeStampPolo);

			} else if (tipoInserimento.equals("TITACC")) {
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(sbnAreaAppoggio.getSbnRequest()
						.getModifica().getDocumento().getDocumentoTypeChoice()
						.getDatiTitAccesso().getT001());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				//areaDatiControlliPoloVO = gestioneAllAuthority.getTitAccessoPuliziaLegamiPolo(areaDatiControlliPoloVO);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					if (areaDatiControlliPoloVO.getDatiElementoType() == null
								&& areaDatiControlliPoloVO.getTitAccessoType() == null) {
						areaDatiPassReturn.setCodErr("disalPoloIndice");
						return areaDatiPassReturn;

					}
				}


				if (areaDatiControlliPoloVO.getDatiElementoType() != null) {
					timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
				}
				if (areaDatiControlliPoloVO.getTitAccessoType() != null) {
					timeStampPolo = areaDatiControlliPoloVO.getTitAccessoType().getT005();
				}

				sbnAreaAppoggio.getSbnRequest().getModifica()
					.getDocumento().getDocumentoTypeChoice()
					.getDatiTitAccesso().setT005(timeStampPolo);

			}  else if (tipoInserimento.equals("TITUNI")) {
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(sbnAreaAppoggio.getSbnRequest()
						.getModifica().getElementoAut().getDatiElementoAut()
						.getT001());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				//areaDatiControlliPoloVO = gestioneAllAuthority.getTitUniformePuliziaLegamiPolo(areaDatiControlliPoloVO);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

				if (areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					if (areaDatiControlliPoloVO.getDatiElementoType() == null
								&& areaDatiControlliPoloVO.getTitAccessoType() == null) {
						areaDatiPassReturn.setCodErr("disalPoloIndice");
						return areaDatiPassReturn;

					}
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() != null) {
					timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
				}
				if (areaDatiControlliPoloVO.getTitAccessoType() != null) {
					timeStampPolo = areaDatiControlliPoloVO.getTitAccessoType().getT005();
				}

				sbnAreaAppoggio.getSbnRequest().getModifica()
					.getElementoAut().getDatiElementoAut()
					.setT005(timeStampPolo);
			}

			sbnAreaAppoggio.getSbnRequest().getModifica().setTipoControllo(
					SbnSimile.CONFERMA);
			this.polo.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				areaDatiPassReturn.setCodErr("noServerInd");
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

		    // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice Nuova modifica
			// per gestire il ritorno con un diagnostico informativo ma non di Errore
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiPassReturn.setCodErr("0000");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				areaDatiPassReturn.setBid(idSbn);
				return areaDatiPassReturn;
			}
		    // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice


			areaDatiPassReturn.setBid(idSbn);
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;
	}

	private AreaDatiVariazioneReturnVO chiamataUpdateLocale(
			SbnMessageType sbnAreaAppoggio, boolean conferma,
			String tipoAuthority, String idSbn) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		SBNMarc sbnRisposta;

		try {

			if (sbnAreaAppoggio.getSbnRequest().getModifica()
					.getElementoAut() != null) {
				sbnAreaAppoggio.getSbnRequest().getModifica()
				.getElementoAut().getDatiElementoAut().setCondiviso(DatiElementoTypeCondivisoType.N);
			} else {
				if (sbnAreaAppoggio.getSbnRequest().getModifica().getDocumento()
						.getDocumentoTypeChoice().getDatiDocumento() != null) {
					sbnAreaAppoggio.getSbnRequest().getModifica().getDocumento()
					.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);
				}
				if (sbnAreaAppoggio.getSbnRequest().getModifica().getDocumento()
						.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
					sbnAreaAppoggio.getSbnRequest().getModifica().getDocumento()
					.getDocumentoTypeChoice().getDatiTitAccesso().setCondiviso(TitAccessoTypeCondivisoType.N);
				}
			}

			// 2. variazione in locale con forzatura per la conferma (senza
			// ricerca
			// dei simili);
			sbnAreaAppoggio.getSbnRequest().getModifica().setTipoControllo(
					SbnSimile.CONFERMA);
			this.polo.setMessage(sbnAreaAppoggio, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
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

			// almaviva2 16.03.2015 gestione del ritorno solonel testo esito e non campo esito di un messaggio di Errore
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito().contains("3010")) {
				areaDatiPassReturn.setCodErr("0000");
				areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPassReturn;
			}

			areaDatiPassReturn.setBid(idSbn);
		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiPassReturn;
	}

	private String RichiestaInserimentoTitoloUniforme(
			AreaDatiVariazioneTitoloVO areaDatiPass) {

		// Verifica di obbligatorietà dei campi
		if (areaDatiPass.getDetTitoloPFissaVO().getLivAut().equals(IID_STRINGAVUOTA)) {
			return "ins001";
		} else {
			int authority = 0;
			try {
				authority = Integer.parseInt(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}

		if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo().equals(IID_STRINGAVUOTA)) {
			return "ins002";
		}


		// Modifica richiesta con BUG 3193 almaviva2 29.09.2009
		// Eliminare controllo sulla presenza delle RICA in tutti i casi
//		if (!areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
//			if (areaDatiPass.getDetTitoloPFissaVO().getNorme() == null
//					|| areaDatiPass.getDetTitoloPFissaVO().getNorme().trim().equals(IID_STRINGAVUOTA)) {
//				return "ins003";
//			}
//		}

		if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
			if (areaDatiPass.getDetTitoloMusVO().getTitOrdinam().trim().equals(IID_STRINGAVUOTA)) {
				return "ins004";
			}
		}

		if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {

			if (areaDatiPass.getDetTitoloMusVO().getTitOrdinam().length() > TIT_LEN_TITOLO_ORDINAMENTO) {
				return "ins006";
			}
			if (areaDatiPass.getDetTitoloMusVO().getTitEstratto().length() > TIT_LEN_TITOLO_ESTRATTO) {
				return "ins007";
			}
			if (areaDatiPass.getDetTitoloMusVO().getAppellativo().length() > TIT_LEN_APPELLATIVO) {
				return "ins008";
			}
			if (areaDatiPass.getDetTitoloMusVO().getOrgSint().length() > TIT_LEN_ORGANICO_SINTETICO) {
				return "ins009";
			}
			if (areaDatiPass.getDetTitoloMusVO().getOrgAnal().length() > TIT_LEN_ORGANICO_ANALITICO) {
				return "ins010";
			}
			if (areaDatiPass.getDetTitoloMusVO().getNumOrdine().length() > TIT_LEN_NUMERO_ORDINE) {
				return "ins011";
			}
			if (areaDatiPass.getDetTitoloMusVO().getNumOpera().length() > TIT_LEN_NUMERO_OPERA) {
				return "ins012";
			}
			if (areaDatiPass.getDetTitoloMusVO().getNumCatTem().length() > TIT_LEN_CATALOGO_TEMATICO) {
				return "ins013";
			}
			if (areaDatiPass.getDetTitoloMusVO().getDatazione().length() > TIT_LEN_DATAZIONE) {
				return "ins014";
			}
			if (areaDatiPass.getDetTitoloMusVO().getSezioni().length() > TIT_LEN_SEZIONI) {
				return "ins015";
			}

			// Inizio Intervento febbraio 2014 verifica sulla presenza di null nei campi Forma Musicale dei titoli natura A
			if (areaDatiPass.getDetTitoloMusVO().getFormaMusic1()== null) {
				areaDatiPass.getDetTitoloMusVO().setFormaMusic1("");
			}
			if (areaDatiPass.getDetTitoloMusVO().getFormaMusic2()== null) {
				areaDatiPass.getDetTitoloMusVO().setFormaMusic2("");
			}
			if (areaDatiPass.getDetTitoloMusVO().getFormaMusic3()== null) {
				areaDatiPass.getDetTitoloMusVO().setFormaMusic3("");
			}

			// Fine Intervento febbraio 2014 verifica sulla presenza di null nei campi Forma Musicale dei titoli natura A

			if (areaDatiPass.getDetTitoloMusVO().getFormaMusic1().length() > 4 ||
					areaDatiPass.getDetTitoloMusVO().getFormaMusic2().length() > 4 ||
				areaDatiPass.getDetTitoloMusVO().getFormaMusic3().length() > 4) {
				return "ins044";
			}
			if (areaDatiPass.getDetTitoloMusVO().getTonalita().length() > 2) {
				return "ins045";
			}



		}
		// Se non ci sono errori formali:
		return "";
	}

	private String RichiestaInserimentoDocumento(
			AreaDatiVariazioneTitoloVO areaDatiPass) {
		// Verifica di obbligatorietà dei campi
		if (areaDatiPass.getDetTitoloPFissaVO().getLivAut().equals(IID_STRINGAVUOTA)) {
			return "ins001";
		} else {

			int authority = 0;

			try {
				authority = Integer.parseInt(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}

		// //////// Controllo sul Cartografico
		if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("C")) {
			if (areaDatiPass.getDetTitoloCarVO().getIndicatoreColore().equals(IID_STRINGAVUOTA)) {
				return "ins016";
			}
	        // Inizio modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)
	        // Il protocollo SBNMARC è stato corretto e non richiede più per il tipo materiale 'C' il meridiano d'origine.
	        // Il campo infatti è da intendersi obbligatorio solo in presenza delle coordinate geografiche,
	        // che non sono un campo obbligatorio.
//			if (areaDatiPass.getDetTitoloCarVO().getMeridianoOrigine().equals(IID_STRINGAVUOTA)) {
//				return "ins017";
//			}
			// Fine modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)


			if (areaDatiPass.getDetTitoloCarVO().getIndicatoreTipoScala().equals(IID_STRINGAVUOTA)) {
				return "ins018";
			}
			if (areaDatiPass.getDetTitoloCarVO().getSupportoFisico().equals(IID_STRINGAVUOTA)) {
				return "ins019";
			}
		}


//		 //////// Controllo sul GENERE in caso di materiale moderno
		if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("M")) {
			if (areaDatiPass.getDetTitoloPFissaVO().getGenere1() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getGenere2() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getGenere3() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getGenere4() != null) {
				if (areaDatiPass.getDetTitoloPFissaVO().getGenere1().length() > 1 ||
						areaDatiPass.getDetTitoloPFissaVO().getGenere2().length() > 1 ||
						areaDatiPass.getDetTitoloPFissaVO().getGenere3().length() > 1 ||
						areaDatiPass.getDetTitoloPFissaVO().getGenere4().length() > 1 ) {
					return "ins046";
				}
			}
		}


		// Intervento interno su MAIL Scognamiglio - Aprile 2016
		// nel caso di cambio materiale da musica a audiovisivo, se era impostato il tipo testo letterario (libretto)
		// questo deve essere eliminato altrimenti il salvataggio non va a buon fine
		if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("H")) {
			areaDatiPass.getDetTitoloPFissaVO().setTipoTestoLetterario("");
		}



		// Modifica almaviva2 19.10.2010 - Intervento interno - eliminazione enter alla fine della stringa
		areaDatiPass.getDetTitoloPFissaVO().setAreaTitTitolo(areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo().replace("\r\n", " "));

		if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo().trim().equals(IID_STRINGAVUOTA)) {
			return "ins002";
		}

		// CONTROLLO DATA SOLO PER NATURE M S C W
		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if ((areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("M"))
				|| (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("S"))
				|| (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("C"))
				|| (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("R"))
				|| (areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("W"))) {

			if (areaDatiPass.getDetTitoloPFissaVO().getTipoData().equals(IID_STRINGAVUOTA)) {
				return "ins020";
			}

			// Se il tipo data non è F, allora devono essere specificate le date
			// 1 e 2 secondo i seguenti criteri
			if (!areaDatiPass.getDetTitoloPFissaVO().getTipoData().equalsIgnoreCase("F")) {
				// Data 1 obbligatoria
				if (areaDatiPass.getDetTitoloPFissaVO().getDataPubbl1().trim().equals(IID_STRINGAVUOTA)) {
					return "ins021";
				}
				// La data 2 è assente se il tipo data è A,D
				if (areaDatiPass.getDetTitoloPFissaVO().getTipoData().equalsIgnoreCase("a")
						|| areaDatiPass.getDetTitoloPFissaVO().getTipoData().equalsIgnoreCase("d")) {
					if (!areaDatiPass.getDetTitoloPFissaVO().getDataPubbl2().trim().equals(IID_STRINGAVUOTA)) {
						return "ins022";
					}
				}

				// Se presente data 2, data 1 obbligatoria
				if (!areaDatiPass.getDetTitoloPFissaVO().getDataPubbl2().trim().equals(IID_STRINGAVUOTA)) {
					if (areaDatiPass.getDetTitoloPFissaVO().getDataPubbl1().trim().equals(IID_STRINGAVUOTA)) {
						return "ins023";
					}
				}
			}
			UtilityDate utilityDate = new UtilityDate();
			String esito = utilityDate.isOkControlloDate(areaDatiPass
					.getDetTitoloPFissaVO().getDataPubbl1(), areaDatiPass
					.getDetTitoloPFissaVO().getDataPubbl2());

			if ((areaDatiPass.getDetTitoloPFissaVO().getTipoData().equals("E") || areaDatiPass.getDetTitoloPFissaVO().getTipoData().equals("R"))
					&& esito.equals("ric002")) {
			} else {
				if (!esito.equals("")) {
					return "ins024";
				}
			}

			// Se tipo data = E, la data 2 deve essere minore della data 1
			if ("e".indexOf(areaDatiPass.getDetTitoloPFissaVO().getTipoData()) != -1) {
				if (UtilityDate.comparaAnni(areaDatiPass.getDetTitoloPFissaVO()
						.getDataPubbl1().trim(), areaDatiPass
						.getDetTitoloPFissaVO().getDataPubbl2().trim()) > 0) {
					return "ins025";
				}
			}

			// Se tipo data = B,F,G,R, la data 2 deve essere maggiore della data
			// 1 DA IMPLEMENTARE
			if ("bfgr".indexOf(areaDatiPass.getDetTitoloPFissaVO().getTipoData()) != -1) {
				if (UtilityDate.comparaAnni(areaDatiPass.getDetTitoloPFissaVO()
						.getDataPubbl1().trim(), areaDatiPass
						.getDetTitoloPFissaVO().getDataPubbl2().trim()) < 0) {
					return "ins026";
				}
			}
		}
		// END DATE

		// Controlli su eventuale presenza e correttezza
		// dell'impronta inserita (per tipi materiale E ed U)
		// L'impronta si trova in titoli che non è possibile
		// inserire, quindi la gestione della stessa è possibile
		// solo in stato di variazione.
		if (areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size() > 0) {

			List vectImpronta = null;

			int totImpronte = areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size();
			Impronta impronta = null;
			int progressivo = 0;
			TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
			// Tabella Impronta del dettaglio Titolo:
			// colonna 0: numero progressivo;
			// colonna 1: testo dell'impronta;
			// colonna 2: nota dell'impronta.
			for (int i = 0; i < totImpronte; i++) {
				progressivo = i + 1;
				tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass
						.getDetTitoloPFissaVO().getListaImpronte().get(i);
				impronta = new Impronta(tabImpST.getCampoUno()
						+ tabImpST.getCampoDue() + tabImpST.getDescCampoDue(),
						tabImpST.getNota());
				if (impronta.isOkImpronta()) {
					if (vectImpronta == null) {
						vectImpronta = new ArrayList();
					}
					vectImpronta.add(impronta);
				} else {
					return "ins000Impronta, Riga " + progressivo + ": "
							+ impronta.getMessaggioErrore();
				}
			}// end for
		}

		// Se non ci sono errori formali:
		return "";

	}

	private String RichiestaInserimentoTitoloAccesso(
			AreaDatiVariazioneTitoloVO areaDatiPass) {

		// Verifica di obbligatorietà dei campi
		if (areaDatiPass.getDetTitoloPFissaVO().getLivAut().equals(IID_STRINGAVUOTA)) {
			return "ins001";
		} else {
			int authority = 0;
			try {
				authority = Integer.parseInt(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}

		if ((!areaDatiPass.isModifica() && areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("T"))
				|| (areaDatiPass.isModifica() && areaDatiPass.getNaturaTitoloDaVariare().equals("T"))) {
			if (areaDatiPass.getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
				return "ins027";
			}
		}

		if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo().trim().equals(IID_STRINGAVUOTA)) {
			return "ins002";
		}

		// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
		if ((!areaDatiPass.isModifica() && areaDatiPass.getDetTitoloPFissaVO().getNatura().equals("B"))
				|| (areaDatiPass.isModifica() && areaDatiPass.getNaturaTitoloDaVariare().equals("B"))) {
			if (areaDatiPass.getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
				return "ins027";
			}
		}

		// Se non ci sono errori formali:
		return "";

	}




	/**
	 * Presiede alla raccolta dei dati del frame e imposta con questi le
	 * proprietà di un oggetto SBNBody, il quale contiene la logica per la
	 * costruzione dell'albero SBNMarc. E' usato anche per la modifica di nature
	 * M, W, N, S, oltre che per l'inserimento/modifica della natura C.
	 *
	 * @param frame
	 *            Il frame dal quale si fa l'inserimento
	 * @param modifica
	 *            segnala se è un inserimento (valore false) o una modifica
	 *            (valore true)
	 *
	 * @return l'albero SBNMarc
	 */

	public AreaDatiScambioInInserimentoTitoloVO inserisciDocumento(AreaDatiVariazioneTitoloVO areaDatiPass, String bidDaAssegnare) {

		AreaDatiScambioInInserimentoTitoloVO areaDatiScambioInInserimentoTitoloVO = new AreaDatiScambioInInserimentoTitoloVO();
		areaDatiScambioInInserimentoTitoloVO.setCodErr("0000");
		areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("");

		SBNMarc sbnAreaAppoggio = null;
		SbnMessageType sbnmessage = null;
		try {
			String appoTipoMateriale = areaDatiPass.getDetTitoloPFissaVO()
					.getTipoMat();
			String appoNatura = areaDatiPass.isModifica() ? areaDatiPass
					.getNaturaTitoloDaVariare() : areaDatiPass
					.getDetTitoloPFissaVO().getNatura();

			SbnSimile appoSimile = SbnSimile.SIMILE;
			if (areaDatiPass.isConferma())
				appoSimile = SbnSimile.CONFERMA;

			// Creo l'oggetto XMLBodyTitoli nel quale andrò ad inserire
			// tutti i campi del frame. Sarà compito di XMLBodyTitoli
			// convertire in XML i dati da mandare al server

			VariazioneBodyTitoli sbnBody = new VariazioneBodyTitoli(
					sbnAreaAppoggio, appoNatura, appoTipoMateriale, appoSimile,
					areaDatiPass);

			sbnBody.setModifica(areaDatiPass.isModifica());

			sbnBody.setBID(areaDatiPass.getDetTitoloPFissaVO().getBid());

			// almaviva2 verificare che l'impostazione del TimeStamp sia con la data di variazione titolo
			sbnBody.setTimeStamp(areaDatiPass.getDetTitoloPFissaVO().getVersione());
			sbnBody.setLivelloAutoritaDocumento(areaDatiPass.getDetTitoloPFissaVO().getLivAut());


			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				sbnBody.setLivelloAutorita(areaDatiPass.getDetTitoloMusVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("C")) {
					sbnBody.setLivelloAutorita(areaDatiPass.getDetTitoloCarVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("G")) {
					sbnBody.setLivelloAutorita(areaDatiPass.getDetTitoloGraVO().getLivAutSpec());
			}  else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("H")) {
					sbnBody.setLivelloAutorita(areaDatiPass.getDetTitoloAudVO().getLivAutSpec());
			}  else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("L")) {
					sbnBody.setLivelloAutorita(areaDatiPass.getDetTitoloEleVO().getLivAutSpec());
			}



			sbnBody.setIsadn(areaDatiPass.getDetTitoloPFissaVO().getIsadn());
			sbnBody.setTipoRecord(areaDatiPass.getDetTitoloPFissaVO().getTipoRec());

			if (areaDatiPass.getDetTitoloPFissaVO().getLingua1() != null) {
				sbnBody.setLingua1(areaDatiPass.getDetTitoloPFissaVO().getLingua1());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getLingua2() != null) {
				sbnBody.setLingua2(areaDatiPass.getDetTitoloPFissaVO().getLingua2());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getLingua3() != null) {
				sbnBody.setLingua3(areaDatiPass.getDetTitoloPFissaVO().getLingua3());
			}


			if (areaDatiPass.getDetTitoloPFissaVO().getGenere1() != null) {
				sbnBody.setGenere1(areaDatiPass.getDetTitoloPFissaVO().getGenere1());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getGenere2() != null) {
				sbnBody.setGenere2(areaDatiPass.getDetTitoloPFissaVO().getGenere2());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getGenere3() != null) {
				sbnBody.setGenere3(areaDatiPass.getDetTitoloPFissaVO().getGenere3());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getGenere4() != null) {
				sbnBody.setGenere4(areaDatiPass.getDetTitoloPFissaVO().getGenere4());
			}


		    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
			sbnBody.setTipoTestoLetterario(areaDatiPass.getDetTitoloPFissaVO().getTipoTestoLetterario());

			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
			// 	 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni
			sbnBody.setTipoTestoRegSonora(areaDatiPass.getDetTitoloPFissaVO().getTipoTestoRegSonora());

			sbnBody.setFormaContenuto(areaDatiPass.getDetTitoloPFissaVO().getFormaContenuto());
			sbnBody.setTipoContenuto(areaDatiPass.getDetTitoloPFissaVO().getTipoContenuto());
			sbnBody.setMovimento(areaDatiPass.getDetTitoloPFissaVO().getMovimento());
			sbnBody.setDimensione(areaDatiPass.getDetTitoloPFissaVO().getDimensione());
			sbnBody.setSensorialita1(areaDatiPass.getDetTitoloPFissaVO().getSensorialita1());
			sbnBody.setSensorialita2(areaDatiPass.getDetTitoloPFissaVO().getSensorialita2());
			sbnBody.setSensorialita3(areaDatiPass.getDetTitoloPFissaVO().getSensorialita3());
			sbnBody.setTipoMediazione(areaDatiPass.getDetTitoloPFissaVO().getTipoMediazione());



			sbnBody.setFormaContenutoBIS(areaDatiPass.getDetTitoloPFissaVO().getFormaContenutoBIS());
			sbnBody.setTipoContenutoBIS(areaDatiPass.getDetTitoloPFissaVO().getTipoContenutoBIS());
			sbnBody.setMovimentoBIS(areaDatiPass.getDetTitoloPFissaVO().getMovimentoBIS());
			sbnBody.setDimensioneBIS(areaDatiPass.getDetTitoloPFissaVO().getDimensioneBIS());
			sbnBody.setSensorialitaBIS1(areaDatiPass.getDetTitoloPFissaVO().getSensorialitaBIS1());
			sbnBody.setSensorialitaBIS2(areaDatiPass.getDetTitoloPFissaVO().getSensorialitaBIS2());
			sbnBody.setSensorialitaBIS3(areaDatiPass.getDetTitoloPFissaVO().getSensorialitaBIS3());
			sbnBody.setTipoMediazioneBIS(areaDatiPass.getDetTitoloPFissaVO().getTipoMediazioneBIS());
		    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

			// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
			// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
			sbnBody.setPubblicatoSiNo(areaDatiPass.getDetTitoloPFissaVO().getPubblicatoSiNo());

			// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
			sbnBody.setTipoSupporto(areaDatiPass.getDetTitoloPFissaVO().getTipoSupporto());
			sbnBody.setTipoSupportoBIS(areaDatiPass.getDetTitoloPFissaVO().getTipoSupportoBIS());

			sbnBody.setAreaTitolo(areaDatiPass.getDetTitoloPFissaVO().getAreaTitTitolo());
			sbnBody.setTipoData(areaDatiPass.getDetTitoloPFissaVO().getTipoData().toLowerCase());
			sbnBody.setData1(areaDatiPass.getDetTitoloPFissaVO().getDataPubbl1());
			sbnBody.setData2(areaDatiPass.getDetTitoloPFissaVO().getDataPubbl2());

			if (areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().size(); i++) {

					tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass.getDetTitoloPFissaVO().getListaNumStandard().get(i);

					// BUG mantis 5319  collaudo - Maggio 2013 - la X del codice di controllo ISBN a 10 caratteri viene accettata anche
					// se scritta in minuscolo; sara maiuscolizzata in fase di inserimento in Base Dati
					sbnBody.vectorSetNumeroSTD(i, tabImpST.getCampoUno().toUpperCase());
					String appo = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD, tabImpST.getCampoDue());
					sbnBody.vectorSetTipoSTD(i, appo);
					sbnBody.vectorSetNotaSTD(i, tabImpST.getNota());
				}
			}

			// IMPRONTA
			if (areaDatiPass.getDetTitoloPFissaVO().getListaImpronte() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().size(); i++) {

					tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass.getDetTitoloPFissaVO().getListaImpronte().get(i);

					sbnBody.vectorSetImpronta1(i, tabImpST.getCampoUno());
					sbnBody.vectorSetImpronta2(i, tabImpST.getCampoDue());
					sbnBody.vectorSetImpronta3(i, tabImpST.getDescCampoDue());
					if (tabImpST.getNota() == null)
						tabImpST.setNota("");
					sbnBody.vectorSetNotaImpronta(i, tabImpST.getNota());
				}
			}

			// PERSONAGGI
			if (areaDatiPass.getDetTitoloMusVO().getListaPersonaggi() != null
					&& areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().size(); i++) {

					tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass.getDetTitoloMusVO().getListaPersonaggi().get(i);

					if (tabImpST.getCampoUno() == null) {
						sbnBody.vectorSetPersonaggi(i,"");
					} else {
						// BUG MANTIS 5556 (esercizio): Vengono eliminati gli spazi nei campi relativi allo interprete/personaggio
						// (tag a_927) perchè nel caso di presenza di caratteri accentati fanno superare limite di 25 byte su campo
						// e causano errore 1050 - aggiunta trim;
						sbnBody.vectorSetPersonaggi(i, tabImpST.getCampoUno().trim());
					}

					if (tabImpST.getCampoDue() == null) {
						sbnBody.vectorSetTimbroVocale(i,"");
					} else {
						sbnBody.vectorSetTimbroVocale(i, tabImpST.getCampoDue());
					}

					// Modifica almaviva2 su Call  almaviva (discordanza fra controllo e settaggio dell'interprete)
					// il controllo va fatto su Nota e non su DescCampoDue
					if (tabImpST.getNota() == null) {
						sbnBody.vectorSetInterprete(i,"");
					} else {
						sbnBody.vectorSetInterprete(i, tabImpST.getNota());
					}
				}
			}

			// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
			// viene esteso anche al Materiale Moderno e Antico
			if (areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi() != null
					&& areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi().size(); i++) {

					tabImpST = (TabellaNumSTDImpronteVO) areaDatiPass.getDetTitoloModAntVO().getListaPersonaggi().get(i);

					if (tabImpST.getCampoUno() == null) {
						sbnBody.vectorSetPersonaggi(i,"");
					} else {
						// BUG MANTIS 5556 (esercizio): Vengono eliminati gli spazi nei campi relativi allo interprete/personaggio
						// (tag a_927) perchè nel caso di presenza di caratteri accentati fanno superare limite di 25 byte su campo
						// e causano errore 1050 - aggiunta trim;
						sbnBody.vectorSetPersonaggi(i, tabImpST.getCampoUno().trim());
					}

					if (tabImpST.getCampoDue() == null) {
						sbnBody.vectorSetTimbroVocale(i,"");
					} else {
						sbnBody.vectorSetTimbroVocale(i, tabImpST.getCampoDue());
					}

					// Modifica almaviva2 su Call  almaviva (discordanza fra controllo e settaggio dell'interprete)
					// il controllo va fatto su Nota e non su DescCampoDue
					if (tabImpST.getNota() == null) {
						sbnBody.vectorSetInterprete(i,"");
					} else {
						sbnBody.vectorSetInterprete(i, tabImpST.getNota());
					}
				}
			}


			// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
			// al link dei documenti su Basi Esterne - Link verso base date digitali
			if (areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().size(); i++) {

					tabImpST = areaDatiPass.getDetTitoloPFissaVO().getListaLinkEsterni().get(i);
					sbnBody.vectorSetLinkEsternoDb(i, tabImpST.getCampoUno());
					sbnBody.vectorSetLinkEsternoId(i, tabImpST.getCampoDue());

					// Evolutiva Maggio 2016 - gestione del campo nota che deve essere valorizzato automaticamente
					String url321 = tabImpST.getNota(); //SBNMarcUtil.generatoreURL321(tabImpST.getCampoUno(), tabImpST.getCampoDue());
					sbnBody.vectorSetLinkEsternoURL(i, url321);
					// sbnBody.vectorSetLinkEsternoURL(i, tabImpST.getNota());
					String appo = codici.SBNToUnimarc(CodiciType.CODICE_LINK_ALTRA_BASE_DATI, tabImpST.getCampoUno());
					sbnBody.vectorSetLinkEsternoNota(i, appo);
				}
			}

			// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
			// ai repertori cartacei - Riferimento a repertorio cartaceo
			if (areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().size() > 0) {
				TabellaNumSTDImpronteVO tabImpST = new TabellaNumSTDImpronteVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().size(); i++) {

					tabImpST = areaDatiPass.getDetTitoloPFissaVO().getListaReperCartaceo().get(i);
					sbnBody.vectorSetReperCartaceoAutTit(i, tabImpST.getCampoUno());
					sbnBody.vectorSetReperCartaceoData(i, tabImpST.getCampoDue());
					sbnBody.vectorSetReperCartaceoPos(i, tabImpST.getNota());
				}
			}

			if (areaDatiPass.getDetTitoloPFissaVO().getPaese() != null) {
				sbnBody.setPaese(areaDatiPass.getDetTitoloPFissaVO().getPaese());
			}

			if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitEdiz() != null) {
				sbnBody.setAreaEdizione(areaDatiPass.getDetTitoloPFissaVO().getAreaTitEdiz());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitDatiMatem() != null) {
				sbnBody.setAreaDatiMatematici(areaDatiPass.getDetTitoloPFissaVO().getAreaTitDatiMatem());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitNumer() != null) {
				sbnBody.setAreaNumerazione(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNumer());
			}

			// Inizio modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010
//			sbnBody.setAreaPubblicazione(areaDatiPass.getDetTitoloPFissaVO().getAreaTitPubbl());
//			sbnBody.setAreaDescrizioneFisica(areaDatiPass.getDetTitoloPFissaVO().getAreaTitDescrFis());

			if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitPubbl() != null) {
				sbnBody.setAreaPubblicazione(areaDatiPass.getDetTitoloPFissaVO().getAreaTitPubbl());
			}
			if (areaDatiPass.getDetTitoloPFissaVO().getAreaTitDescrFis() != null) {
				sbnBody.setAreaDescrizioneFisica(areaDatiPass.getDetTitoloPFissaVO().getAreaTitDescrFis());
			}

			// Fine modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010


//			GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
			sbnBody.setAreaNote(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote());
			sbnBody.setAreaNote323(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote323()));
			sbnBody.setAreaNote327(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote327()));
			sbnBody.setAreaNote330(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote330()));
			sbnBody.setAreaNote336(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote336()));
			sbnBody.setAreaNote337(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNote337()));
			sbnBody.setAreaNoteDATA(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNoteDATA()));
			sbnBody.setAreaNoteORIG(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNoteORIG()));
			sbnBody.setAreaNoteFILI(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNoteFILI()));
			sbnBody.setAreaNotePROV(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNotePROV()));
			sbnBody.setAreaNotePOSS(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloPFissaVO().getAreaTitNotePOSS()));
//			GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274

			if (areaDatiPass.getDetTitoloPFissaVO().getFonteRec() == null) {
				sbnBody.setFonteRecord("");
			} else {
				sbnBody.setFonteRecord(areaDatiPass.getDetTitoloPFissaVO().getFonteRec());
			}
			sbnBody.setFonteRecord(areaDatiPass.getDetTitoloPFissaVO().getFonteRec());

			// Schema da 1.07 a 1.09 - Area della musica (Area ISBD)
			sbnBody.setAreaMusica(areaDatiPass.getDetTitoloPFissaVO().getAreaTitMusica());

			// Uri per la grafica
			if (areaDatiPass.getDetTitoloPFissaVO().getUriDigitalBorn() != null) {
				if (areaDatiPass.getDetTitoloPFissaVO().getUriDigitalBorn().length() > 0) {
					sbnBody.setUriDigitalBorn(areaDatiPass.getDetTitoloPFissaVO().getUriDigitalBorn());
				}
			}

			// MusicaType
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				// almaviva2 03.03.2010 - Richiesta interna - sistemata con la validazione dati il null nel campo
				sbnBody.setTipoTesto(ValidazioneDati.trimOrEmpty(areaDatiPass.getDetTitoloMusVO().getTipoTesto()));
//				sbnBody.setTipoTesto(areaDatiPass.getDetTitoloMusVO().getTipoTesto());


				// Inizio almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906
				// correzione impostazione campo tipo elaborazione per renderlo visualizzabile sia in
				// dettaglio che in variazione (trascodifica in Unimarc da SBN)

				String codiceElaborazioneUnimarc = "";
				try {
					codiceElaborazioneUnimarc = codici.SBNToUnimarc(CodiciType.CODICE_ELABORAZIONE, areaDatiPass.getDetTitoloMusVO().getElabor());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				sbnBody.setTipoElaborazione(codiceElaborazioneUnimarc);

//				if (areaDatiPass.getDetTitoloMusVO().getTipoElabor() == null) {
//					sbnBody.setTipoElaborazione("");
//				} else {
//					sbnBody.setTipoElaborazione(areaDatiPass.getDetTitoloMusVO()
//							.getTipoElabor());
//				}
				// Fine almaviva2 - Modifica del 20.03.2012 BUG MANTIS 4906


				if (areaDatiPass.getDetTitoloMusVO().getOrgSint() == null) {
					sbnBody.setOrganicoSintetico("");
				} else {
					sbnBody.setOrganicoSintetico(areaDatiPass.getDetTitoloMusVO().getOrgSint());
				}

				if (areaDatiPass.getDetTitoloMusVO().getOrgAnal() == null) {
					sbnBody.setOrganicoAnalitico("");
				} else {
					sbnBody.setOrganicoAnalitico(areaDatiPass.getDetTitoloMusVO().getOrgAnal());
				}

				if (areaDatiPass.getDetTitoloMusVO().getStesura() == null) {
					sbnBody.setStesura("");
				} else {
					sbnBody.setStesura(areaDatiPass.getDetTitoloMusVO().getStesura());
				}

				if (areaDatiPass.getDetTitoloMusVO().getComposito() == null) {
					sbnBody.setComposito("");
				} else {
					sbnBody.setComposito(areaDatiPass.getDetTitoloMusVO().getComposito());
				}

				if (areaDatiPass.getDetTitoloMusVO().getPalinsesto() == null) {
					sbnBody.setPalinsesto("");
				} else {
					sbnBody.setComposito(areaDatiPass.getDetTitoloMusVO().getPalinsesto());
				}

				if (areaDatiPass.getDetTitoloMusVO().getDatazione() == null) {
					sbnBody.setDatazione("");
				} else {
					sbnBody.setDatazione(areaDatiPass.getDetTitoloMusVO().getDatazione());
				}


				String codicePresentazioneUnimarc = "";
				try {
					codicePresentazioneUnimarc = codici.SBNToUnimarc(CodiciType.CODICE_PRESENTAZIONE, areaDatiPass.getDetTitoloMusVO().getPresent());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				sbnBody.setPresentazione(codicePresentazioneUnimarc);


				if (areaDatiPass.getDetTitoloMusVO().getMateria() == null) {
					sbnBody.setMateria("");
				} else {
					sbnBody.setMateria(areaDatiPass.getDetTitoloMusVO().getMateria());
				}

				if (areaDatiPass.getDetTitoloMusVO().getIllustrazioni() == null) {
					sbnBody.setIllustrazioni("");
				} else {
					sbnBody.setIllustrazioni(areaDatiPass.getDetTitoloMusVO().getIllustrazioni());
				}

				if (areaDatiPass.getDetTitoloMusVO().getNotazioneMusicale() == null) {
					sbnBody.setNotazioneMusicale("");
				} else {
					sbnBody.setNotazioneMusicale(areaDatiPass.getDetTitoloMusVO().getNotazioneMusicale());
				}

				if (areaDatiPass.getDetTitoloMusVO().getLegatura() == null) {
					sbnBody.setLegatura("");
				} else {
					sbnBody.setLegatura(areaDatiPass.getDetTitoloMusVO().getLegatura());
				}

				if (areaDatiPass.getDetTitoloMusVO().getConservazione() == null) {
					sbnBody.setConservazione("");
				} else {
					sbnBody.setConservazione(areaDatiPass.getDetTitoloMusVO().getConservazione());
				}

				sbnBody.setGenereRappresentazione(areaDatiPass.getDetTitoloMusVO().getGenereRappr());
				sbnBody.setAnnoRappresentazione(areaDatiPass.getDetTitoloMusVO().getAnnoIRappr());
				sbnBody.setPeriodoRappresentazione(areaDatiPass.getDetTitoloMusVO().getPeriodoIRappr());
				sbnBody.setSedeRappresentazione(areaDatiPass.getDetTitoloMusVO().getSedeIRappr());
				sbnBody.setLuogoRappresentazione(areaDatiPass.getDetTitoloMusVO().getLocalitaIRappr());
				sbnBody.setNotaRappresentazione(areaDatiPass.getDetTitoloMusVO().getNoteIRappr());
				sbnBody.setOccasioneRappresentazione(areaDatiPass.getDetTitoloMusVO().getOccasioneIRappr());

				// almaviva2 Verificare le impostazioni di quali campi si deve fare
				// (vedi la gestione di personaggi o impronte)

				// Inizio Gestione INCIPIT
				UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
				int contIncipit = areaDatiPass.getDetTitoloMusVO().getListaDettagliIncipit().size();
				C926[] c926A = new C926[contIncipit];
				for (int i = 0; i < contIncipit; i++) {
					C926 rigaIncipit = utilityCampiTitolo.getIncipit(areaDatiPass.getDetTitoloMusVO().getListaDettagliIncipit().get(i), i);
					c926A[i] = rigaIncipit;
				}
				sbnBody.setIncipit(c926A);
				// Fine   Gestione INCIPIT
			}


			// Cartografia

			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("C")) {

				sbnBody.setPubblicazioneGovernativa(areaDatiPass.getDetTitoloCarVO().getPubblicazioneGovernativa());
				sbnBody.setIndicatoreColoreCartografia(areaDatiPass.getDetTitoloCarVO().getIndicatoreColore());
				sbnBody.setMeridianoOrigine(areaDatiPass.getDetTitoloCarVO().getMeridianoOrigine());
				sbnBody.setSupportoFisico(areaDatiPass.getDetTitoloCarVO().getSupportoFisico());
				sbnBody.setTecnicaCreazione(areaDatiPass.getDetTitoloCarVO().getTecnicaCreazione());
				sbnBody.setFormaRiproduzione(areaDatiPass.getDetTitoloCarVO().getFormaRiproduzione());
				sbnBody.setFormaPubblicazione(areaDatiPass.getDetTitoloCarVO().getFormaPubblicazione());
				sbnBody.setAltitudine(areaDatiPass.getDetTitoloCarVO().getAltitudine());
				sbnBody.setIndicatoreTipoScala(areaDatiPass.getDetTitoloCarVO().getIndicatoreTipoScala());
				sbnBody.setTipoScala(areaDatiPass.getDetTitoloCarVO().getTipoScala());
				sbnBody.setScalaOrizzontale(areaDatiPass.getDetTitoloCarVO().getScalaOriz());
				sbnBody.setScalaVerticale(areaDatiPass.getDetTitoloCarVO().getScalaVert());

				// almaviva2 Mantis 5620: In tutte le valorizzazioni delle coordinate si deve differenziare fra cattura/inserimento
				// di un titolo cartografico in cui le coordinate sono nel formato 011°14'00" e la catalogazione in Indice di un
				// titolo precedentemente copiato dove le coordinate sono nel formato 0111400 quindi gia correttamente formattate;

				String longitValEO1 = coalesce(trimOrNull(areaDatiPass.getDetTitoloCarVO().getLongitValEO1()), COORDINATA_VUOTA);
				if (!longitValEO1.equals(COORDINATA_VUOTA)
						&& !areaDatiPass.getDetTitoloCarVO().getLongitTipo1().equals(IID_STRINGAVUOTA)) {
					int lenghtCoord = longitValEO1.length();

					String long1 = "";
					if (lenghtCoord == 10) {
						long1 = areaDatiPass.getDetTitoloCarVO().getLongitTipo1()
								+ longitValEO1.substring(0, 3)
								+ longitValEO1.substring(4, 6)
								+ longitValEO1.substring(7, lenghtCoord - 1);
					} else {
						long1 = areaDatiPass.getDetTitoloCarVO().getLongitTipo1() + longitValEO1;
					}
					sbnBody.setCoordinateOvest(long1);
				}

				String longitValEO2 = coalesce(trimOrNull(areaDatiPass.getDetTitoloCarVO().getLongitValEO2()), COORDINATA_VUOTA);
				if (!longitValEO2.equals(COORDINATA_VUOTA)
						&& !areaDatiPass.getDetTitoloCarVO().getLongitTipo2().equals(IID_STRINGAVUOTA)) {
					int lenghtCoord = longitValEO2.length();

					String long2 = "";
					if (lenghtCoord == 10) {
						long2 = areaDatiPass.getDetTitoloCarVO().getLongitTipo2()
								+ longitValEO2.substring(0, 3)
								+ longitValEO2.substring(4, 6)
								+ longitValEO2.substring(7,	lenghtCoord - 1);
					} else {
						long2 = areaDatiPass.getDetTitoloCarVO().getLongitTipo2() + longitValEO2;
					}
					sbnBody.setCoordinateEst(long2);
				}

//				Bug collaudo 5009: il formato corretto dele coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
//		        Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta
//		        Modificato controllo su latitudine affinche in presenza di 000° 00' 00'' cioe EQUATORE non si richieda l'emisfero (N/S)

				String latitValNS1 = coalesce(trimOrNull(areaDatiPass.getDetTitoloCarVO().getLatitValNS1()), COORDINATA_VUOTA);
				if (!latitValNS1.equals(COORDINATA_VUOTA)) {
//				if (!areaDatiPass.getDetTitoloCarVO().getLatitValNS1().equals(COORDINATA_VUOTA)
//						&& !areaDatiPass.getDetTitoloCarVO().getLatitTipo1().equals(IID_STRINGAVUOTA)) {
					if (areaDatiPass.getDetTitoloCarVO().getLatitTipo1().equals(IID_STRINGAVUOTA)) {
						areaDatiPass.getDetTitoloCarVO().setLatitTipo1(" ");
					}
					int lenghtCoord = latitValNS1.length();

					String lat1 = "";
					if (lenghtCoord == 10) {
						lat1 = areaDatiPass.getDetTitoloCarVO().getLatitTipo1()
								+ latitValNS1.substring(0, 3)
								+ latitValNS1.substring(4, 6)
								+ latitValNS1.substring(7, lenghtCoord - 1);
					} else {
						lat1 = areaDatiPass.getDetTitoloCarVO().getLatitTipo1() + latitValNS1;
					}
					sbnBody.setCoordinateNord(lat1);
				}

				String latitValNS2 = coalesce(trimOrNull(areaDatiPass.getDetTitoloCarVO().getLatitValNS2()), COORDINATA_VUOTA);
				if (!latitValNS2.equals(COORDINATA_VUOTA)) {
//				if (!areaDatiPass.getDetTitoloCarVO().getLatitValNS2().equals(COORDINATA_VUOTA)
//						&& !areaDatiPass.getDetTitoloCarVO().getLatitTipo2().equals(IID_STRINGAVUOTA)) {
					if (areaDatiPass.getDetTitoloCarVO().getLatitTipo2().equals(IID_STRINGAVUOTA)) {
						areaDatiPass.getDetTitoloCarVO().setLatitTipo2(" ");
					}
					int lenghtCoord = latitValNS2.length();

					String lat2 = "";
					if (lenghtCoord == 10) {
						lat2 = areaDatiPass.getDetTitoloCarVO().getLatitTipo2()
								+ latitValNS2.substring(0, 3)
								+ latitValNS2.substring(4, 6)
								+ latitValNS2.substring(7, lenghtCoord - 1);
					} else {
						lat2 = areaDatiPass.getDetTitoloCarVO().getLatitTipo2() + latitValNS2;
					}
					sbnBody.setCoordinateSud(lat2);
				}

				sbnBody.setCarattereImmagine(areaDatiPass.getDetTitoloCarVO().getCarattereImmagine());
				sbnBody.setForma(areaDatiPass.getDetTitoloCarVO().getForma());
				sbnBody.setPiattaforma(areaDatiPass.getDetTitoloCarVO().getPiattaforma());
				sbnBody.setCategoriaSatellite(areaDatiPass.getDetTitoloCarVO().getCategoriaSatellite());
				// almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
				sbnBody.setProiezioneCarte(areaDatiPass.getDetTitoloCarVO().getProiezioneCarte());
			}// FINE Cartografia

			// START GRAFICA
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("G")) {
				sbnBody.setDesignazioneMateriale(areaDatiPass.getDetTitoloGraVO().getDesignMat());
				sbnBody.setSupportoPrimario(areaDatiPass.getDetTitoloGraVO().getSuppPrim());
				sbnBody.setIndicatoreColoreGrafica(areaDatiPass.getDetTitoloGraVO().getIndicCol());

				sbnBody.setIndicatoreTecnica1(areaDatiPass.getDetTitoloGraVO().getIndicTec1());
				sbnBody.setIndicatoreTecnica2(areaDatiPass.getDetTitoloGraVO().getIndicTec2());
				sbnBody.setIndicatoreTecnica3(areaDatiPass.getDetTitoloGraVO().getIndicTec3());
				sbnBody.setIndicatoreTecnicaStampe1(areaDatiPass.getDetTitoloGraVO().getIndicTecSta1());
				sbnBody.setIndicatoreTecnicaStampe2(areaDatiPass.getDetTitoloGraVO().getIndicTecSta2());
				sbnBody.setIndicatoreTecnicaStampe3(areaDatiPass.getDetTitoloGraVO().getIndicTecSta3());

				sbnBody.setDesignazioneFunzione(areaDatiPass.getDetTitoloGraVO().getDesignFun());
			}// END GRAFICA


			// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
			// START AUDIOVISIVO
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("H")) {
				if (areaDatiPass.getDetTitoloPFissaVO().getTipoRec().equals("g")) {
					sbnBody.setTipoVideo(areaDatiPass.getDetTitoloAudVO().getTipoVideo());
					sbnBody.setLunghezza(areaDatiPass.getDetTitoloAudVO().getLunghezza());
					sbnBody.setIndicatoreColore(areaDatiPass.getDetTitoloAudVO().getIndicatoreColore());
					sbnBody.setIndicatoreSuono(areaDatiPass.getDetTitoloAudVO().getIndicatoreSuono());
					sbnBody.setSupportoSuono(areaDatiPass.getDetTitoloAudVO().getSupportoSuono());
					sbnBody.setLarghezzaDimensioni(areaDatiPass.getDetTitoloAudVO().getLarghezzaDimensioni());
					sbnBody.setFormaPubblDistr(areaDatiPass.getDetTitoloAudVO().getFormaPubblDistr());
					sbnBody.setTecnicaVideoFilm(areaDatiPass.getDetTitoloAudVO().getTecnicaVideoFilm());
					sbnBody.setPresentImmagMov(areaDatiPass.getDetTitoloAudVO().getPresentImmagMov());
					sbnBody.setMaterAccompagn1(areaDatiPass.getDetTitoloAudVO().getMaterAccompagn1());
					sbnBody.setMaterAccompagn2(areaDatiPass.getDetTitoloAudVO().getMaterAccompagn2());
					sbnBody.setMaterAccompagn3(areaDatiPass.getDetTitoloAudVO().getMaterAccompagn3());
					sbnBody.setMaterAccompagn4(areaDatiPass.getDetTitoloAudVO().getMaterAccompagn4());
					sbnBody.setPubblicVideoreg(areaDatiPass.getDetTitoloAudVO().getPubblicVideoreg());
					sbnBody.setPresentVideoreg(areaDatiPass.getDetTitoloAudVO().getPresentVideoreg());
					sbnBody.setMaterialeEmulsBase(areaDatiPass.getDetTitoloAudVO().getMaterialeEmulsBase());
					sbnBody.setMaterialeSupportSec(areaDatiPass.getDetTitoloAudVO().getMaterialeSupportSec());
					sbnBody.setStandardTrasmiss(areaDatiPass.getDetTitoloAudVO().getStandardTrasmiss());
					sbnBody.setVersioneAudiovid(areaDatiPass.getDetTitoloAudVO().getVersioneAudiovid());
					sbnBody.setElementiProd(areaDatiPass.getDetTitoloAudVO().getElementiProd());
					sbnBody.setSpecCatColoreFilm(areaDatiPass.getDetTitoloAudVO().getSpecCatColoreFilm());
					sbnBody.setEmulsionePellic(areaDatiPass.getDetTitoloAudVO().getEmulsionePellic());
					sbnBody.setComposPellic(areaDatiPass.getDetTitoloAudVO().getComposPellic());
					sbnBody.setSuonoImmagMovimento(areaDatiPass.getDetTitoloAudVO().getSuonoImmagMovimento());
					sbnBody.setTipoPellicStampa(areaDatiPass.getDetTitoloAudVO().getTipoPellicStampa());
				} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoRec().equals("i")
						|| areaDatiPass.getDetTitoloPFissaVO().getTipoRec().equals("j")) {
					sbnBody.setFormaPubblicazioneDisco(areaDatiPass.getDetTitoloAudVO().getFormaPubblicazioneDisco());
					sbnBody.setVelocita(areaDatiPass.getDetTitoloAudVO().getVelocita());
					sbnBody.setTipoSuono(areaDatiPass.getDetTitoloAudVO().getTipoSuono());
					sbnBody.setLarghezzaScanal(areaDatiPass.getDetTitoloAudVO().getLarghezzaScanal());
					sbnBody.setDimensioni(areaDatiPass.getDetTitoloAudVO().getDimensioni());
					sbnBody.setLarghezzaNastro(areaDatiPass.getDetTitoloAudVO().getLarghezzaNastro());
					sbnBody.setConfigurazNastro(areaDatiPass.getDetTitoloAudVO().getConfigurazNastro());
					sbnBody.setMaterTestAccompagn1(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn1());
					sbnBody.setMaterTestAccompagn2(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn2());
					sbnBody.setMaterTestAccompagn3(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn3());
					sbnBody.setMaterTestAccompagn4(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn4());
					sbnBody.setMaterTestAccompagn5(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn5());
					sbnBody.setMaterTestAccompagn6(areaDatiPass.getDetTitoloAudVO().getMaterTestAccompagn6());
					sbnBody.setTecnicaRegistraz(areaDatiPass.getDetTitoloAudVO().getTecnicaRegistraz());
					sbnBody.setSpecCarattRiprod(areaDatiPass.getDetTitoloAudVO().getSpecCarattRiprod());
					sbnBody.setDatiCodifRegistrazSonore(areaDatiPass.getDetTitoloAudVO().getDatiCodifRegistrazSonore());
					sbnBody.setTipoDiMateriale(areaDatiPass.getDetTitoloAudVO().getTipoDiMateriale());
					sbnBody.setTipoDiTaglio(areaDatiPass.getDetTitoloAudVO().getTipoDiTaglio());
					sbnBody.setDurataRegistraz(areaDatiPass.getDetTitoloAudVO().getDurataRegistraz());
				}

				// Perte della Musica
				if (areaDatiPass.getDetTitoloMusVO().getOrgSint() == null) {
					sbnBody.setOrganicoSintetico("");
				} else {
					sbnBody.setOrganicoSintetico(areaDatiPass.getDetTitoloMusVO().getOrgSint());
				}
				if (areaDatiPass.getDetTitoloMusVO().getOrgAnal() == null) {
					sbnBody.setOrganicoAnalitico("");
				} else {
					sbnBody.setOrganicoAnalitico(areaDatiPass.getDetTitoloMusVO().getOrgAnal());
				}

				String codiceElaborazioneUnimarc = "";
				try {
					codiceElaborazioneUnimarc = codici.SBNToUnimarc(CodiciType.CODICE_ELABORAZIONE, areaDatiPass.getDetTitoloMusVO().getElabor());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				sbnBody.setTipoElaborazione(codiceElaborazioneUnimarc);

				sbnBody.setGenereRappresentazione(areaDatiPass.getDetTitoloMusVO().getGenereRappr());
				sbnBody.setAnnoRappresentazione(areaDatiPass.getDetTitoloMusVO().getAnnoIRappr());
				sbnBody.setPeriodoRappresentazione(areaDatiPass.getDetTitoloMusVO().getPeriodoIRappr());
				sbnBody.setSedeRappresentazione(areaDatiPass.getDetTitoloMusVO().getSedeIRappr());
				sbnBody.setLuogoRappresentazione(areaDatiPass.getDetTitoloMusVO().getLocalitaIRappr());
				sbnBody.setNotaRappresentazione(areaDatiPass.getDetTitoloMusVO().getNoteIRappr());
				sbnBody.setOccasioneRappresentazione(areaDatiPass.getDetTitoloMusVO().getOccasioneIRappr());

			}// END AUDIOVISIVO

			// START ELETTRONICO
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("L")) {
				// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
				// Gestione nuovi campi specifici per etichetta 135
				sbnBody.setTipoRisorsaElettronica(areaDatiPass.getDetTitoloEleVO().getTipoRisorsaElettronica());
				sbnBody.setIndicazioneSpecificaMateriale(areaDatiPass.getDetTitoloEleVO().getIndicazioneSpecificaMateriale());
				sbnBody.setColoreElettronico(areaDatiPass.getDetTitoloEleVO().getColoreElettronico());
				sbnBody.setDimensioniElettronico(areaDatiPass.getDetTitoloEleVO().getDimensioniElettronico());
				sbnBody.setSuonoElettronico(areaDatiPass.getDetTitoloEleVO().getSuonoElettronico());

			}// END ELETTRONICO


			// Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
			// viene esteso anche al Materiale Moderno e Antico
			// START MODERNO/ANTICO
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("M") || areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("E")) {
				sbnBody.setGenereRappresentazione(areaDatiPass.getDetTitoloModAntVO().getGenereRappr());
				sbnBody.setAnnoRappresentazione(areaDatiPass.getDetTitoloModAntVO().getAnnoIRappr());
				sbnBody.setPeriodoRappresentazione(areaDatiPass.getDetTitoloModAntVO().getPeriodoIRappr());
				sbnBody.setSedeRappresentazione(areaDatiPass.getDetTitoloModAntVO().getSedeIRappr());
				sbnBody.setLuogoRappresentazione(areaDatiPass.getDetTitoloModAntVO().getLocalitaIRappr());
				sbnBody.setNotaRappresentazione(areaDatiPass.getDetTitoloModAntVO().getNoteIRappr());
				sbnBody.setOccasioneRappresentazione(areaDatiPass.getDetTitoloModAntVO().getOccasioneIRappr());

			}// END MODERNO/ANTICO


			sbnmessage = sbnBody.addToSBNMarc(areaDatiPass.isAbilPerTipoMat(), areaDatiPass.isAbilPerTipoMatMusicale(), bidDaAssegnare);
			areaDatiPass.setPrimoBloccoSimili(true);

			areaDatiScambioInInserimentoTitoloVO.setSbnMessageAppoggio(sbnmessage);
			return areaDatiScambioInInserimentoTitoloVO;

		} catch (ValidationException ve) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiScambioInInserimentoTitoloVO;
	}

	/**
	 * Presiede alla raccolta dei dati del frame e imposta con questi le
	 * proprietà di un oggetto SBNBody, il quale contiene la logica per la
	 * costruzione dell'albero SBNMarc per l'inserimento o la modifica di un
	 * titolo
	 *
	 * @param frame
	 *            Il frame dal quale si fa l'inserimento
	 *
	 * @return l'albero SBNMarc
	 */

	public AreaDatiScambioInInserimentoTitoloVO inserisciTitoloUniforme(
			AreaDatiVariazioneTitoloVO areaDatiPass, String bidDaAssegnare) {

		AreaDatiScambioInInserimentoTitoloVO areaDatiScambioInInserimentoTitoloVO = new AreaDatiScambioInInserimentoTitoloVO();
		areaDatiScambioInInserimentoTitoloVO.setCodErr("0000");
		areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("");

		SBNMarc sbnMarc = null;
		SbnMessageType sbnmessage = null;
		try {

			String appoTipoMateriale = IID_STRINGAVUOTA;
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				appoTipoMateriale = "Musica";
			} else {
				appoTipoMateriale = "Altro";
			}
			SbnSimile appoSimile = SbnSimile.SIMILE;
			if (areaDatiPass.isConferma())
				appoSimile = SbnSimile.CONFERMA;


			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
			String natura = "";
			if (ValidazioneDati.isFilled(areaDatiPass.getNaturaTitoloDaVariare())) {
				natura = areaDatiPass.getNaturaTitoloDaVariare();
			} else {
				natura = "A";
			}

			// Creo l'oggetto XMLBodyTitoli nel quale andrò ad inserire
			// tutti i campi del frame. Sarà compito di XMLBodyTitoli
			// convertire in XML i dati da mandare al server
			VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli(
					sbnMarc, natura, appoTipoMateriale, appoSimile,
					areaDatiPass);

			xmlBodyTitoli.setModifica(areaDatiPass.isModifica());

			xmlBodyTitoli.setBID(areaDatiPass.getDetTitoloPFissaVO().getBid());
			xmlBodyTitoli.setTimeStamp(areaDatiPass.getDetTitoloPFissaVO()
					.getVersione());
			xmlBodyTitoli.setLivelloAutoritaDocumento(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloPFissaVO().getLivAut());

			if (areaDatiPass.getDetTitoloMusVO().getLivAutSpec() == null &&
					areaDatiPass.getDetTitoloCarVO().getLivAutSpec() == null &&
					areaDatiPass.getDetTitoloGraVO().getLivAutSpec() == null) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			}


			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloMusVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("C")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloCarVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("G")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloGraVO().getLivAutSpec());
			}

			xmlBodyTitoli.setIsadn(areaDatiPass.getDetTitoloPFissaVO()
					.getIsadn());
			xmlBodyTitoli.setAreaTitolo(areaDatiPass.getDetTitoloPFissaVO()
					.getAreaTitTitolo());

			// MAGGIO 2017 - almaviva2 - EVOLUTIVA norme catalografiche:
			// La valorizzazione del campo è obbligatoria; al campo deve essere associato un drop list che contiene solo i valori RICA e REICAT
			// In creazione il default è REICAT.
			// In variazione se il valore inviato da Indice è diverso da uno di quelli ammessi deve essere automaticamente
			// sostituito dal valore di default (REICAT), altrimenti viene visualizzato il valore presente nell’XML
			// Modifica gennaio 2018 almaviva2 controllo che il campo norme venga valorizzato correttamente sia per valore
			// vuoto che per valore null
			//if (areaDatiPass.getDetTitoloPFissaVO().getNorme().equals("")) {
			if (areaDatiPass.getDetTitoloPFissaVO().getNorme() == null || areaDatiPass.getDetTitoloPFissaVO().getNorme().equals("")) {
				xmlBodyTitoli.setNorme("REICAT");
			} else {
				xmlBodyTitoli.setNorme(areaDatiPass.getDetTitoloPFissaVO().getNorme());
			}

			xmlBodyTitoli.setNoteInformative(areaDatiPass
					.getDetTitoloPFissaVO().getNoteInformative());
			xmlBodyTitoli.setNoteCatalogatore(areaDatiPass
					.getDetTitoloPFissaVO().getNoteCatalogatore());

			// REPERTORI
			if (areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriNew() != null
					&& areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriNew().size() > 0) {
				TabellaNumSTDImpronteAggiornataVO tabImpST = new TabellaNumSTDImpronteAggiornataVO();
				for (int i = 0; i < areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriNew().size(); i++) {
					tabImpST = areaDatiPass.getDetTitoloPFissaVO().getListaRepertoriNew().get(i);
					xmlBodyTitoli.addRepertorio(tabImpST.getCampoUno(),	tabImpST.getCampoDue(), tabImpST.getNota());
				}
			}

			// Specializzazione per titolo uniforme musicale
			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {

				xmlBodyTitoli.setTitoloOrdinamento(areaDatiPass
						.getDetTitoloMusVO().getTitOrdinam());
				xmlBodyTitoli.setTitoloEstratto(areaDatiPass
						.getDetTitoloMusVO().getTitEstratto());
				xmlBodyTitoli.setAppellativo(areaDatiPass.getDetTitoloMusVO()
						.getAppellativo());
				xmlBodyTitoli.setFormaMusicale(new String[] {
						areaDatiPass.getDetTitoloMusVO().getFormaMusic1(),
						areaDatiPass.getDetTitoloMusVO().getFormaMusic2(),
						areaDatiPass.getDetTitoloMusVO().getFormaMusic3() });
				xmlBodyTitoli.setOrganicoSintetico(areaDatiPass
						.getDetTitoloMusVO().getOrgSint());
				xmlBodyTitoli.setOrganicoAnalitico(areaDatiPass
						.getDetTitoloMusVO().getOrgAnal());
				xmlBodyTitoli.setNumeroOrdine(areaDatiPass.getDetTitoloMusVO()
						.getNumOrdine());
				xmlBodyTitoli.setNumeroOpera(areaDatiPass.getDetTitoloMusVO()
						.getNumOpera());
				xmlBodyTitoli.setNumeroCatalogo(areaDatiPass
						.getDetTitoloMusVO().getNumCatTem());
				xmlBodyTitoli.setDatazione(areaDatiPass.getDetTitoloMusVO()
						.getDatazione());

				xmlBodyTitoli.setTonalita(areaDatiPass.getDetTitoloMusVO()
						.getTonalita());
				xmlBodyTitoli.setSezioni(areaDatiPass.getDetTitoloMusVO()
						.getSezioni());
			} else {
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua1() != null) {
					xmlBodyTitoli.setLingua1(areaDatiPass.getDetTitoloPFissaVO().getLingua1());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua2() != null) {
					xmlBodyTitoli.setLingua2(areaDatiPass.getDetTitoloPFissaVO().getLingua2());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua3() != null) {
					xmlBodyTitoli.setLingua3(areaDatiPass.getDetTitoloPFissaVO().getLingua3());
				}

				// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
	            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
				if (areaDatiPass.getDetTitoloPFissaVO().getFormaOpera231() != null) {
					xmlBodyTitoli.setFormaOpera231(areaDatiPass.getDetTitoloPFissaVO().getFormaOpera231());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getDataOpera231() != null) {
					xmlBodyTitoli.setDataOpera231(areaDatiPass.getDetTitoloPFissaVO().getDataOpera231());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getPaese() != null) {
					xmlBodyTitoli.setPaese(areaDatiPass.getDetTitoloPFissaVO().getPaese());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getAltreCarat231() != null) {
					xmlBodyTitoli.setAltreCarat231(areaDatiPass.getDetTitoloPFissaVO().getAltreCarat231());
				}
			}

			sbnmessage = xmlBodyTitoli.addToSBNMarc(areaDatiPass.isAbilPerTipoMat(), areaDatiPass.isAbilPerTipoMatMusicale(), bidDaAssegnare);
			areaDatiPass.setPrimoBloccoSimili(true);

			areaDatiScambioInInserimentoTitoloVO.setSbnMessageAppoggio(sbnmessage);
			return areaDatiScambioInInserimentoTitoloVO;

		} catch (ValidationException ve) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiScambioInInserimentoTitoloVO;
	}

	public AreaDatiScambioInInserimentoTitoloVO inserisciTitoloAccesso(AreaDatiVariazioneTitoloVO areaDatiPass, String bidDaAssegnare) {

		AreaDatiScambioInInserimentoTitoloVO areaDatiScambioInInserimentoTitoloVO = new AreaDatiScambioInInserimentoTitoloVO();
		areaDatiScambioInInserimentoTitoloVO.setCodErr("0000");
		areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("");

		SBNMarc sbnMarc = null;
		SbnMessageType sbnmessage = null;

		try {

			String appoTipoMateriale = IID_STRINGAVUOTA;
			String appoNatura = areaDatiPass.isModifica() ? areaDatiPass
					.getNaturaTitoloDaVariare() : areaDatiPass
					.getDetTitoloPFissaVO().getNatura();

			SbnSimile appoSimile = SbnSimile.SIMILE;
			if (areaDatiPass.isConferma())
				appoSimile = SbnSimile.CONFERMA;

			// Creo l'oggetto XMLBodyTitoli nel quale andrò ad inserire
			// tutti i campi del frame. Sarà compito di XMLBodyTitoli
			// convertire in XML i dati da mandare al server

			VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli(
					sbnMarc, appoNatura, appoTipoMateriale, appoSimile,
					areaDatiPass);

			xmlBodyTitoli.setModifica(areaDatiPass.isModifica());

			xmlBodyTitoli.setBID(areaDatiPass.getDetTitoloPFissaVO().getBid());
			xmlBodyTitoli.setTimeStamp(areaDatiPass.getDetTitoloPFissaVO()
					.getVersione());
			xmlBodyTitoli.setLivelloAutoritaDocumento(areaDatiPass.getDetTitoloPFissaVO().getLivAut());
			xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloPFissaVO().getLivAut());

			if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloMusVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("C")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloCarVO().getLivAutSpec());
			} else if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("G")) {
				xmlBodyTitoli.setLivelloAutorita(areaDatiPass.getDetTitoloGraVO().getLivAutSpec());
			}


			// Specializzazione per natura T
			if (appoNatura.equals("T")) {

				if (areaDatiPass.getDetTitoloPFissaVO().getLingua1() != null) {
					xmlBodyTitoli.setLingua1(areaDatiPass.getDetTitoloPFissaVO().getLingua1());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua2() != null) {
					xmlBodyTitoli.setLingua2(areaDatiPass.getDetTitoloPFissaVO().getLingua2());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua3() != null) {
					xmlBodyTitoli.setLingua3(areaDatiPass.getDetTitoloPFissaVO().getLingua3());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getGenere1() != null) {
					xmlBodyTitoli.setGenere1(areaDatiPass.getDetTitoloPFissaVO().getGenere1());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getGenere2() != null) {
					xmlBodyTitoli.setGenere2(areaDatiPass.getDetTitoloPFissaVO().getGenere2());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getGenere3() != null) {
					xmlBodyTitoli.setGenere3(areaDatiPass.getDetTitoloPFissaVO().getGenere3());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getGenere4() != null) {
					xmlBodyTitoli.setGenere4(areaDatiPass.getDetTitoloPFissaVO().getGenere4());
				}
			}

			// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
			// Specializzazione per natura B
			if (appoNatura.equals("B")) {
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua1() != null) {
					xmlBodyTitoli.setLingua1(areaDatiPass.getDetTitoloPFissaVO().getLingua1());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua2() != null) {
					xmlBodyTitoli.setLingua2(areaDatiPass.getDetTitoloPFissaVO().getLingua2());
				}
				if (areaDatiPass.getDetTitoloPFissaVO().getLingua3() != null) {
					xmlBodyTitoli.setLingua3(areaDatiPass.getDetTitoloPFissaVO().getLingua3());
				}
			}


			// Area del titolo
			xmlBodyTitoli.setAreaTitolo(areaDatiPass.getDetTitoloPFissaVO()
					.getAreaTitTitolo());

			sbnmessage = xmlBodyTitoli.addToSBNMarc(areaDatiPass.isAbilPerTipoMat(), areaDatiPass.isAbilPerTipoMatMusicale(), bidDaAssegnare);
			areaDatiPass.setPrimoBloccoSimili(true);


			areaDatiScambioInInserimentoTitoloVO.setSbnMessageAppoggio(sbnmessage);
			return areaDatiScambioInInserimentoTitoloVO;


		} catch (ValidationException ve) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiScambioInInserimentoTitoloVO.setCodErr("9999");
			areaDatiScambioInInserimentoTitoloVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiScambioInInserimentoTitoloVO;
	}

	public AreaDatiVariazioneReturnVO scambiaResponsabilitaLegameTitoloAutore(AreaDatiScambiaResponsLegameTitAutVO areaDatiPassCompleta) {
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {
			ModificaType modificaType = null;
			modificaType = new ModificaType();
			modificaType.setTipoControllo(SbnSimile.CONFERMA);
			modificaType.setTipoControllo(SbnSimile.SIMILE);
			DocumentoType documentoType = new DocumentoType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			switch (areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getNaturaBidPartenza().charAt(0)) {
			case 'A':
			case 'C':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
				DatiDocType datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getNaturaBidPartenza()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getTipMatBidPartenza()));
				datiDocType.setT001(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getBidPartenza());
				datiDocType.setT005(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getTimeStampBidPartenza());
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getLivAutBidPartenza()));
				documentoTypeChoice.setDatiDocumento(datiDocType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				TitAccessoType titAccessoType = new TitAccessoType();
				titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getNaturaBidPartenza()));
				titAccessoType.setLivelloAut(SbnLivello.valueOf(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getLivAutBidPartenza()));

				C200 c200 = new C200();
				c200.addA_200(0,areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getDescPartenza());
				c200.setId1(Indicatore.VALUE_2);

				TitAccessoTypeChoice accessoTypeChoice = new TitAccessoTypeChoice();
				accessoTypeChoice.setT454(c200);

				titAccessoType.setTitAccessoTypeChoice(accessoTypeChoice);
				titAccessoType.setT001(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getBidPartenza());
				titAccessoType.setT005(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getTimeStampBidPartenza());
				documentoTypeChoice.setDatiTitAccesso(titAccessoType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			}

			LegamiType[] arrayLegamiTypeCompleto = new LegamiType[4];

			VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli();
			LegamiType[] arrayLegamiType21 = new LegamiType[1];
			arrayLegamiType21 = xmlBodyTitoli.addLegami(areaDatiPassCompleta.getAreaDatiLegameTitolo21VO());
			if (arrayLegamiType21 == null) {
				areaDatiPassReturn.setCodErr("ins037");
				return areaDatiPassReturn;
			}

			int iCompl = 0;
			for (int i = 0; i < arrayLegamiType21.length; i++) {
				arrayLegamiTypeCompleto[iCompl] = arrayLegamiType21[i];
				iCompl++;
			}

			LegamiType[] arrayLegamiType12 = new LegamiType[1];
			arrayLegamiType12 = xmlBodyTitoli.addLegami(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO());
			if (arrayLegamiType12 == null) {
				areaDatiPassReturn.setCodErr("ins037");
				return areaDatiPassReturn;
			}

			for (int i = 0; i < arrayLegamiType12.length; i++) {
				arrayLegamiTypeCompleto[iCompl] = arrayLegamiType12[i];
				iCompl++;
			}

			documentoType.setLegamiDocumento(arrayLegamiTypeCompleto);
			modificaType.setDocumento(documentoType);

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();

			sbnrequest.setModifica(modificaType);
			sbnmessage.setSbnRequest(sbnrequest);


			if (areaDatiPassCompleta.isInserimentoIndice()) {
				// Inserimento del legame in Indice
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
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
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
			AreaDatiControlliPoloVO areaDatiControlliPoloVO;
			String timeStampPolo = "";
			switch (areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getNaturaBidPartenza().charAt(0)) {
			case 'A':
			case 'C':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getBidPartenza());
				areaDatiControlliPoloVO.setTipoAut(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getAuthorityOggettoPartenza());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO.setCodErr("");

				if (areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getAuthorityOggettoPartenza()== null
						|| areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getAuthorityOggettoPartenza().equals("")) {
					gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);
				} else if (areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getAuthorityOggettoPartenza().equals("TU")
						|| areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getAuthorityOggettoPartenza().equals("UM")) {
					gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);
				}

				if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				DatiDocType datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();
				if (datiDocTypePolo == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				}
				timeStampPolo = datiDocTypePolo.getT005();
				sbnmessage.getSbnRequest().getModifica().getDocumento().getDocumentoTypeChoice().getDatiDocumento().setT005(timeStampPolo);
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getBidPartenza());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				TitAccessoType titAccessoTypePolo = areaDatiControlliPoloVO.getTitAccessoType();
				if (titAccessoTypePolo == null) {
					areaDatiPassReturn.setCodErr("noServerLoc");
					return areaDatiPassReturn;
				}
				timeStampPolo = titAccessoTypePolo.getT005();
				sbnmessage.getSbnRequest().getModifica().getDocumento().getDocumentoTypeChoice().getDatiTitAccesso().setT005(timeStampPolo);
				break;
			}


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
			areaDatiPassReturn.setCodErr("0000");
			areaDatiPassReturn.setBid(areaDatiPassCompleta.getAreaDatiLegameTitolo12VO().getBidPartenza());

			return areaDatiPassReturn;

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}


		return areaDatiPassReturn;
	}
	public AreaDatiVariazioneReturnVO inserisciLegameTitolo(AreaDatiLegameTitoloVO areaDatiPass) {

		SBNMarc sbnRisposta = null;

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		try {
			ModificaType modificaType = null;
			modificaType = new ModificaType();
			modificaType.setTipoControllo(SbnSimile.CONFERMA);
			modificaType.setTipoControllo(SbnSimile.SIMILE);
			DocumentoType documentoType = new DocumentoType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

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
						AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO returnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
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
			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			switch (areaDatiPass.getNaturaBidPartenza().charAt(0)) {
			case 'A':
			case 'C':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
			case 'R':
				DatiDocType datiDocType = new DatiDocType();
				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(areaDatiPass.getNaturaBidPartenza()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipMatBidPartenza()));
				datiDocType.setT001(areaDatiPass.getBidPartenza());
				datiDocType.setT005(areaDatiPass.getTimeStampBidPartenza());
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(areaDatiPass.getLivAutBidPartenza()));
				documentoTypeChoice.setDatiDocumento(datiDocType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				TitAccessoType titAccessoType = new TitAccessoType();
				titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(areaDatiPass.getNaturaBidPartenza()));
				titAccessoType.setLivelloAut(SbnLivello.valueOf(areaDatiPass.getLivAutBidPartenza()));

				C200 c200 = new C200();
				c200.addA_200(0,areaDatiPass.getDescPartenza());
				c200.setId1(Indicatore.VALUE_2);

				TitAccessoTypeChoice accessoTypeChoice = new TitAccessoTypeChoice();
				accessoTypeChoice.setT454(c200);

				titAccessoType.setTitAccessoTypeChoice(accessoTypeChoice);
				titAccessoType.setT001(areaDatiPass.getBidPartenza());
				titAccessoType.setT005(areaDatiPass.getTimeStampBidPartenza());
				documentoTypeChoice.setDatiTitAccesso(titAccessoType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			}

			VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli();
			LegamiType[] arrayLegamiType = new LegamiType[1];

			arrayLegamiType = xmlBodyTitoli.addLegami(areaDatiPass);
			if (arrayLegamiType == null) {
				areaDatiPassReturn.setCodErr("LegameNonPrevisto");
				return areaDatiPassReturn;
			}
			documentoType.setLegamiDocumento(arrayLegamiType);
			modificaType.setDocumento(documentoType);

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();

			sbnrequest.setModifica(modificaType);
			sbnmessage.setSbnRequest(sbnrequest);

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

					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
							.getEsito().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
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

					// Aggiornamento del legame in Polo per il flag di condivisione ( da n a s)
					switch (areaDatiPass.getNaturaBidPartenza().charAt(0)) {
					case 'A':
					case 'C':
					case 'M':
					case 'S':
					case 'W':
					case 'N':
						sbnmessage.getSbnRequest().getModifica().getDocumento()
								.getDocumentoTypeChoice().getDatiDocumento().setT005(areaDatiPass.getTimeStampBidPartenzaPolo());
						sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);

						if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameDoc() != null) {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
						} else if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameElementoAut() != null) {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);
						} else {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameTitAccesso().setCondiviso(LegameTitAccessoTypeCondivisoType.S);
						}

						break;
					case 'B':
					case 'D':
					case 'P':
					case 'T':
						sbnmessage.getSbnRequest().getModifica().getDocumento()
								.getDocumentoTypeChoice().getDatiTitAccesso().setT005(areaDatiPass.getTimeStampBidPartenzaPolo());
						sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);

						if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameDoc() != null) {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
						} else if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameElementoAut() != null) {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);
						} else {
							sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).getArrivoLegame(0).getLegameTitAccesso().setCondiviso(LegameTitAccessoTypeCondivisoType.S);
						}
						break;
					}

					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerPol");
						return areaDatiPassReturn;
					}
					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
							.getEsito().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
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

					areaDatiPassReturn.setCodErr("0000");
					areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());
					areaDatiPassReturn.setVersioneIndice(verIndice);
					areaDatiPassReturn.setVersionePolo(verPolo);

					return areaDatiPassReturn;

				}
			}


			if (areaDatiPass.isInserimentoIndice()) {
				// Inserimento del legame in Indice
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerInd");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("0000")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
			}

			// Inserimento del legame in Polo Solo se l'oggetto è già presente altrimenti bisona prima
			// effettuarne la cattura e poi il legame.
			// sequenza operazioni:
			// interrogazione localizzazioni bid arrivo del legame
			// ...se oggetto assente per il polo (cattura -creazione legame)
			// ...se oggetto presente per il polo (localizazione dell'oggetto sia in I che in P poi -creazione legame)
			// ...se oggetto localizzato per la biblio (creazione legame)

			// Ricerca del time stamp sul DB di Polo
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
			AreaDatiControlliPoloVO areaDatiControlliPoloVO;
			String timeStampPolo = "";

			// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
			switch (areaDatiPass.getNaturaBidPartenza().charAt(0)) {
			case 'A':
			case 'C':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
			case 'R':
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
				areaDatiControlliPoloVO.setTipoAut(areaDatiPass.getAuthorityOggettoPartenza());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO.setCodErr("");

				if (areaDatiPass.getAuthorityOggettoPartenza()== null || areaDatiPass.getAuthorityOggettoPartenza().equals("")) {
					gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);
				} else if (areaDatiPass.getAuthorityOggettoPartenza().equals("TU")	|| areaDatiPass.getAuthorityOggettoPartenza().equals("UM")) {
					gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);
				}

				if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				DatiDocType datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();
				// Modifica BUG Mantis 3955 almaviva2 28.10.2010
				// se il codErr è 0000 vuol dire che manca il titolo dal Polo e non che il servere non è disponibile
				if (datiDocTypePolo == null) {
					if (areaDatiControlliPoloVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr("disalPoloIndice");
					} else {
						areaDatiPassReturn.setCodErr("noServerLoc");
					}
					return areaDatiPassReturn;
				}
				timeStampPolo = datiDocTypePolo.getT005();
				sbnmessage.getSbnRequest().getModifica().getDocumento().getDocumentoTypeChoice().getDatiDocumento().setT005(timeStampPolo);
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getBidPartenza());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}
				TitAccessoType titAccessoTypePolo = areaDatiControlliPoloVO.getTitAccessoType();
				// Modifica BUG Mantis 3955 almaviva2 28.10.2010
				// se il codErr è 0000 vuol dire che manca il titolo dal Polo e non che il servere non è disponibile
				if (titAccessoTypePolo == null) {
					if (areaDatiControlliPoloVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr("disalPoloIndice");
					} else {
						areaDatiPassReturn.setCodErr("noServerLoc");
					}
					return areaDatiPassReturn;
				}
				timeStampPolo = titAccessoTypePolo.getT005();
				sbnmessage.getSbnRequest().getModifica().getDocumento().getDocumentoTypeChoice().getDatiTitAccesso().setT005(timeStampPolo);
				break;
			}

			if (areaDatiPass.getAuthorityOggettoArrivo() == null || areaDatiPass.getAuthorityOggettoArrivo().equals("")) {

				DatiDocType datiDocTypePolo = new DatiDocType();
				TitAccessoType titAccessoTypePolo = new TitAccessoType();
				switch (areaDatiPass.getNaturaBidArrivo().charAt(0)) {
				case 'A':
				case 'C':
				case 'M':
				case 'S':
				case 'W':
					areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
					areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
					areaDatiControlliPoloVO.setTipoAut(areaDatiPass.getAuthorityOggettoArrivo());
					areaDatiControlliPoloVO.setCancellareInferiori(false);
					areaDatiControlliPoloVO.setCodErr("");

					if (areaDatiPass.getAuthorityOggettoArrivo() == null || areaDatiPass.getAuthorityOggettoArrivo().equals("")) {
						gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);
					} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("TU") || areaDatiPass.getAuthorityOggettoArrivo().equals("UM")) {
						gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);
					}

					if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();
					if (datiDocTypePolo == null) {
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
					break;
				case 'N':
					areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
					areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
					areaDatiControlliPoloVO.setTipoAut(areaDatiPass.getAuthorityOggettoArrivo());
					areaDatiControlliPoloVO.setCancellareInferiori(false);
					areaDatiControlliPoloVO.setCodErr("");

					if (areaDatiPass.getAuthorityOggettoArrivo() == null || areaDatiPass.getAuthorityOggettoArrivo().equals("")) {
						gestioneAllAuthority.getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);
					} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("TU") || areaDatiPass.getAuthorityOggettoArrivo().equals("UM")) {
						gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);
					}

					if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();
					if (datiDocTypePolo == null) {
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
						areaDatiPassReturn.setCodErr("0000");
						areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());
						return areaDatiPassReturn;
					}
					break;
				case 'B':
				case 'D':
				case 'P':
				case 'T':
					areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
					areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
					areaDatiControlliPoloVO.setCancellareInferiori(false);
					areaDatiControlliPoloVO = gestioneAllAuthority.getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

					if (!areaDatiControlliPoloVO.getCodErr().equals("") && !areaDatiControlliPoloVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiControlliPoloVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiControlliPoloVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
					titAccessoTypePolo = areaDatiControlliPoloVO.getTitAccessoType();
					if (titAccessoTypePolo == null) {
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
					break;
				}


			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("AU")) {
				//================================================================
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("AU");
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
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaAutore(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("MA")) {
				// Inizio verifica di esistenza del Documento su Polo
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("MA");
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = gestioneAllAuthority
						.getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

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
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaMarca(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("TU")
					|| areaDatiPass.getAuthorityOggettoArrivo().equals("UM")) {

				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
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
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaReticolo(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			} else if (areaDatiPass.getAuthorityOggettoArrivo().equals("LU")) {
				// Inizio verifica di esistenza del Documento su Polo
				areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiPass.getIdArrivo());
				areaDatiControlliPoloVO.setTipoAut("LU");
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
					AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaLuogo(areaTabellaOggettiDaCatturareVO);
					if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
						areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
						areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
						return areaDatiPassReturn;
					}
				}
			}


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
			areaDatiPassReturn.setCodErr("0000");
			areaDatiPassReturn.setBid(areaDatiPass.getBidPartenza());
			return areaDatiPassReturn;

		} catch (SbnMarcException ve) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPassReturn;
	}

	// ///////////////////////////////////////////
	// COSTRUZIONE DEL RETICOLO TITOLI
	public String getElementoAut(ElementAutType elementoAut) {
		String value = "";

		String livelloAutorita = "";
		String livelloAutoritaDesc = "";

		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		if (datiElemento.getLivelloAut() != null) {
			livelloAutorita = datiElemento.getLivelloAut().toString();
			// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
			// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
			// nel caso delle Authority viene inserito DAL NULLA perchè prima non era visualizzato
			TB_CODICI codici = new TB_CODICI();
			try {
				codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
					livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
				} else {
					livelloAutoritaDesc = livelloAutorita;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = datiElemento.getT001() + " ";

		if (datiElemento instanceof TitoloUniformeType) {
			TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;

			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			// value = value + "A ";
			value = value + titoloUniformeType.getNaturaTU().toString() + " ";
			// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
			 if (titoloUniformeType.getT230()  != null) {
				 value = value + titoloUniformeType.getT230().getA_230().toString()	+ " ";
             } else if (titoloUniformeType.getT231()  != null) {
            	 value = value + titoloUniformeType.getT231().getA_231().toString()	+ " ";
             } else if (titoloUniformeType.getT431()  != null) {
            	 value = value + titoloUniformeType.getT431().getA_431().toString()	+ " ";
             }
		}

		if (datiElemento instanceof TitoloUniformeMusicaType) {
			TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;
			value = value + "A ";
			value = value	+ titoloUniformeMusicaType.getT230().getA_230().toString()	+ " ";
		}
		return value;
	}

	public AreaPassaggioReticoloTitoliVO getReticoloTitolo(SBNMarc sbnMarcType,
			TreeElementViewTitoli root, String chiamata,
			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal) {


		UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		myChiamata = chiamata;
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		String ritorno = "";
		setKeyCtr = 1;
		codSbnBiblioteca = areaDatiPassLocal.getCodiceSbn();

		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;
		String myDescrTitolo = "";
		String myNatura = "";
		String myTipoMateriale = "";
		String myBID = "";

		// LEGGO SBNOUTPUT.DOCUMENTO OPPURE
		// SBNOUTPUT.DOCUMENTO.DATITITACCESSO 1^ LIVELLO
		if (sbnOutPut.getDocumentoCount() > 0) {

			documentoType = sbnOutPut.getDocumento(0);
			ritorno = getDocumento(documentoType, null, "", "", root, "");
			if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {

				myBID = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
				myNatura = documentoType.getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString();
				myTipoMateriale = documentoType.getDocumentoTypeChoice().getDatiDocumento().getTipoMateriale().toString();
				myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,sbnMarcType, true);

				root.setRigaReticoloCtr(setKeyCtr++);
				root.setKey(myBID);
				root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
				root.setT005(documentoType.getDocumentoTypeChoice()
						.getDatiDocumento().getT005());
				root.setLivelloAutorita(documentoType.getDocumentoTypeChoice()
						.getDatiDocumento().getLivelloAutDoc().toString());
				if (myChiamata.equals("P")) {
					if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBibCount() > 0) {
						String localizzazione = documentoType.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBib(0);
						if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("S")) {
							root.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
						} else if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("N")) {
							root.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE);
						} else if (localizzazione.substring(7,8).equals("N") && localizzazione.substring(9,10).equals("S")) {
//							root.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO);
							root.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
						} else {
							root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
						}
					} else {
						root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					}
					if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE){
						root.setFlagCondiviso(false);
					} else {
						root.setFlagCondiviso(true);
					}
				} else {
					root.setFlagCondiviso(true);
					int oggettoLocalizzato = 0;
					if (tabellaLocalizzazioni.get(myBID) == null) {

						// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
						// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
						oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
						if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz() != null) {
							String localizzazione = documentoType.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz();
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
							areaDatiPassLocal.setIdLoc(myBID);
							areaDatiPassLocal.setNatura(myNatura);
							oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, false);
						}
						// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

						root.setLocalizzazione(oggettoLocalizzato);
						tabellaLocalizzazioni.put(myBID, String.valueOf(oggettoLocalizzato));
					} else {
						root.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(myBID)));
					}
				}
				if (ritorno == null || ritorno.equals("")) {
					root.setText(myNatura + " " + myDescrTitolo);
				} else {
					root.setText(ritorno);
				}
				root.setDescription(myDescrTitolo);
				root.setTipoMateriale(myTipoMateriale);
				root.setNatura(myNatura);
			} else {
				myNatura = documentoType.getDocumentoTypeChoice()
						.getDatiTitAccesso().getNaturaTitAccesso().toString();
				myBID = documentoType.getDocumentoTypeChoice()
						.getDatiTitAccesso().getT001().toString();

				myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,	sbnMarcType, true);
				root.setRigaReticoloCtr(setKeyCtr++);
				root.setKey(myBID);
				root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
				root.setT005(documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT005());
				root.setLivelloAutorita(documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getLivelloAut().toString());
				if (myChiamata.equals("P")) {
					root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					if (documentoType.getDocumentoTypeChoice()
							.getDatiTitAccesso().getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE){
						root.setFlagCondiviso(false);
					} else {
						root.setFlagCondiviso(true);
					}
				} else {
					root.setFlagCondiviso(true);
					int oggettoLocalizzato = 0;
					if (tabellaLocalizzazioni.get(myBID) == null) {

						// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
						// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
						oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
						if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getSbnLocaliz() != null) {
							String localizzazione = documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getSbnLocaliz();
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
							areaDatiPassLocal.setIdLoc(myBID);
							areaDatiPassLocal.setNatura(myNatura);
							oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, false);
						}
						// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

						root.setLocalizzazione(oggettoLocalizzato);
						tabellaLocalizzazioni.put(myBID, String.valueOf(oggettoLocalizzato));
					} else {
						root.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(myBID)));
					}
				}

				root.setText(myNatura + " " + myDescrTitolo);
				root.setDescription(myDescrTitolo);
				root.setTipoMateriale(myTipoMateriale);
				root.setNatura(myNatura);

			}
			// Inizio inserimento della gestione del dettaglio Titolo
			//DettaglioOggetti dettOggetti = new DettaglioOggetti();
			root.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
					dettOggetti.getDettaglioTitolo(sbnMarcType, root, root
							.getNatura(), 0));
			// Fine inserimento della gestione del dettaglio Titolo

			root.setIdNode(0);

			root.open();
			if (documentoType.getLegamiDocumentoCount() > 0) {
				documentoType = sbnOutPut.getDocumento(0);
				getLegamiElemento(sbnOutPut, root, sbnMarcType, null,
						documentoType, null, level, areaDatiPassLocal);
			}
		}

		// LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
		if (sbnOutPut.getElementoAutCount() > 0) {

			// nodo = root.getRootNode();
			elementoAut = sbnOutPut.getElementoAut(0);
			documentoType = null;
			ritorno = getElementoAut(elementoAut);

			// FORZO NATURA = A
			// MyTipoMateriale =elementoAut.getDatiElementoAut().;

			// MODIFICA DEL TREE
			myBID = elementoAut.getDatiElementoAut().getT001();
			myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,
					sbnMarcType, true);

			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			// myNatura = "A";
			// Febbraio 2018 - almaviva2 nel campo dei titoli uniformi musicali si deve continuare ad impostare le
			// vecchia natura A in maniera forzata altrimenti va in errore
			if (elementoAut.getDatiElementoAut().getNaturaTU() == null) {
				myNatura = "A";
			} else {
				myNatura = elementoAut.getDatiElementoAut().getNaturaTU().toString();
			}

			root.setRigaReticoloCtr(setKeyCtr++);
			root.setKey(myBID);
			root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
			root.setT005(elementoAut.getDatiElementoAut().getT005());
			root.setLivelloAutorita(elementoAut.getDatiElementoAut().getLivelloAut().toString());
			if (myChiamata.equals("P")) {
				root.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
				if (elementoAut.getDatiElementoAut().getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
					root.setFlagCondiviso(false);
				} else {
					root.setFlagCondiviso(true);
				}
			} else {
				root.setFlagCondiviso(true);
				int oggettoLocalizzato = 0;
				if (tabellaLocalizzazioni.get(myBID) == null) {

					// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
					// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
					oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
					if (elementoAut.getDatiElementoAut().getSbnLocaliz() != null) {
						String localizzazione = elementoAut.getDatiElementoAut().getSbnLocaliz();
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
						areaDatiPassLocal.setIdLoc(myBID);
						areaDatiPassLocal.setNatura(myNatura);
						oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
					}
					// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

					root.setLocalizzazione(oggettoLocalizzato);
					tabellaLocalizzazioni.put(myBID, String.valueOf(oggettoLocalizzato));
				} else {
					root.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(myBID)));
				}
			}

			root.setText(myNatura + " " + myDescrTitolo);
			root.setDescription(myDescrTitolo);
			root.setNatura(myNatura);
			root.setTipoAuthority(elementoAut.getDatiElementoAut().getTipoAuthority());
			// Inizio inserimento della gestione del dettaglio Titolo
			//DettaglioOggetti dettOggetti = new DettaglioOggetti();
			root.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
					dettOggetti.getDettaglioTitolo(sbnMarcType, root, root
							.getNatura(), 0));
			// Fine inserimento della gestione del dettaglio Titolo

			root.setIdNode(0);
			root.open();
			getLegamiElemento(sbnOutPut, root, sbnMarcType, nodo1, null,
					elementoAut, level, areaDatiPassLocal);
		}

		AreaPassaggioReticoloTitoliVO areaPassaggioReticoloTitoliVO = new AreaPassaggioReticoloTitoliVO();
		areaPassaggioReticoloTitoliVO.setTreeElementViewTitoli(root);
		return areaPassaggioReticoloTitoliVO;
	}// end setReticolo

	private boolean haLegami(DocumentoType documentoType) {
		return documentoType.getLegamiDocumentoCount() > 0;
	}

	public String getLegamiElemento(SbnOutputType sbnOutPut,
			TreeElementViewTitoli root, SBNMarc sbnMarcType,
			TreeElementViewTitoli nodo, DocumentoType documentoType,
			ElementAutType elemen, int level,
			AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		String ritorno = "";

		livello++;

		LegamiType legamiType = null;
		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;

		int count = 0;

		if (elemen != null) {
			count = elemen.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		for (int i = 0; i < count; i++) {

			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
				TreeElementViewTitoli nodoCorrente = null;

				// LEGGO LEGAME ELEMENTO AUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame.getLegameTitAccesso();

				// il nodoCorrente deve essere creato solo per i legami ad oggetti diversi dai repertori
				if (legameElemento != null) {
					if (legameElemento.getElementoAutLegato() != null) {
						if (legameElemento.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().getType() != SbnAuthority.RE_TYPE) {
							nodoCorrente = (TreeElementViewTitoli) root.addChild();
						}
					}
				} else {
					nodoCorrente = (TreeElementViewTitoli) root.addChild();
				}




				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {

					// Inizio intervento almaviva2/ almaviva5 01.04.2011 - Modifica relativa al nuovo
					// protocollo (nuovo campo prioritàPoli il quale conterrà delle stringhe così composte:
					//	<prioritaPoli>S - BVE</prioritaPoli>
					//	<prioritaPoli>N - CSW</prioritaPoli>
					// Dove Si e No rappresentano la possibilità di “sostituibilità del legame” mentre dopo il trattino viene indicato
					// come informazione aggiuntiva il polo “proprietario”.

					int type = legameElemento.getTipoAuthority().getType();
					if (ValidazioneDati.in(type, SbnAuthority.SO_TYPE, SbnAuthority.CL_TYPE)) {
						//almaviva5_20110513 fix per errore indice tag priorita polo vuoto
						if (ValidazioneDati.isFilled(legameElemento.getPrioritaPoli()) ) {
							String[] pp = legameElemento.getPrioritaPoli().split("\\s-\\s");
							if (pp.length == 2) {
								if (type == SbnAuthority.SO_TYPE) {
									root.setProprietarioSoggettazioneIndice(pp[0].equals("S"));
									root.setPoloSoggettazioneIndice(pp[1]);
								}
								if (type == SbnAuthority.CL_TYPE) {
									root.setProprietarioClassificazioneIndice(pp[0].equals("S"));
									root.setPoloClassificazioneIndice(pp[1]);
								}
							}
						}
					}

					// Fine intervento almaviva2/ almaviva5 01.04.2011 -


					legameElemento.getTipoLegame().toString();
					elemen2 = legameElemento.getElementoAutLegato();
					level++;
					if ((elemen2 != null)	&& (elemen2.getLegamiElementoAutCount() > 0)) {
						DatiElementoType datiElemento = elemen2.getDatiElementoAut();

						// salto i repertori
						if (datiElemento.getTipoAuthority().getType() != SbnAuthority.RE_TYPE) {
							if ((datiElemento instanceof EnteType) || (datiElemento instanceof AutorePersonaleType)) {
								if (utilityCastor.isFormaRinvio(datiElemento)) {
									if (nodoCorrente != null) {
										nodoCorrente.setAutoreFormaRinvio(true);
									}
								}
							}
							if (datiElemento instanceof LuogoType) {
								if (utilityCastor.isFormaRinvio(datiElemento)) {
									if (nodoCorrente != null) {
										nodoCorrente.setLuogoFormaRinvio(true);
									}
								}
							}

							nodoCorrente.setDescription(getDescrizioneElementoAut(legameElemento, root, nodoCorrente));

							nodoCorrente.setText(getLegameElementoAut(legameElemento, root, nodoCorrente));
							nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

							nodoCorrente.setTipoAuthority(datiElemento.getTipoAuthority());
							nodoCorrente.setLivelloAutorita(datiElemento.getLivelloAut().toString());
							nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento, null,null));
							nodoCorrente.setNatura("A");
							nodoCorrente.setIdNode(root.getIdNode() + 1);

							// Inizio inserimento della gestione del dettaglio Autore
							if (datiElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = 0;
								if (root.getTipoAuthority() != null) {
									if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
										tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
									} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
										tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
									} else if (root.getTipoAuthority().getType() == SbnAuthority.UM_TYPE || root.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
										tipoLegame = DatiLegame.LEGAME_DOCUMENTO_AUTORE;
									}

								} else {
									tipoLegame = DatiLegame.LEGAME_DOCUMENTO_AUTORE;
								}
								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(
												dettOggetti.getDettaglioAutore(sbnMarcType, null, nodoCorrente, root.getNatura(),tipoLegame));

							}// Fine inserimento della gestione del dettaglio Autore
							// Inizio inseriemnto della gestione dettaglio marca
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = 0;
								if (root.getTipoAuthority() != null) {
									if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
										tipoLegame = DatiLegame.LEGAME_AUTORE_MARCA;
									}
								} else {
									tipoLegame = DatiLegame.LEGAME_DOCUMENTO_MARCA;
								}

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioMarcaGeneraleVO(
												dettOggetti.getDettaglioMarca(sbnMarcType,nodoCorrente, root.getNatura(),tipoLegame));
							}// Fine inserimento della gestione del dettaglio Marca
							// Inizio inseriemnto della gestione dettaglio Soggetto
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_SOGGETTO;

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioSoggettoGeneraleVO(
												dettOggetti.getDettaglioSoggetto(sbnMarcType,nodoCorrente,root.getNatura(),tipoLegame));
							}// Fine inserimento della gestione del dettaglio Soggetto


							// Inizio inserimento della gestione dettaglio THESAURO
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_THESAURO;

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTermineThesauroGeneraleVO(
												dettOggetti.getDettaglioTermineThesauro(sbnMarcType,nodoCorrente,root.getNatura(),tipoLegame));
							}// Fine inserimento della  gestione dettaglio Termine Thesauro

							// Inizio inseriemnto della gestione dettaglio Classe
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_CLASSE;

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioClasseGeneraleVO(
												dettOggetti.getDettaglioClasse(sbnMarcType,nodoCorrente, root.getNatura(),tipoLegame));
							}// Fine inserimento della gestione del dettaglio Classe

							// Inizio inseriemnto della gestione dettaglio Luogo
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_LUOGO;
								if (root.getTipoAuthority() != null) {
									if (root.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
										tipoLegame = DatiLegame.LEGAME_LUOGO_LUOGO;
									}
								}

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioLuogoGeneraleVO(
												dettOggetti.getDettaglioLuogo(sbnMarcType,	nodoCorrente, root.getNatura(),tipoLegame));
							}// Fine inserimento della gestione del dettaglio Luogo

							//  PARTE NUOVA PER AUTHORITY TU E UM
							// Inizio inserimento della gestione dettaglio Titolo Uniforme
							else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.TU_TYPE ||	datiElemento.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
								//DettaglioOggetti dettOggetti = new DettaglioOggetti();
								int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_TITOLO_UNIFORME;
								if (root.getTipoAuthority() != null) {
									if (root.getTipoAuthority().getType() == SbnAuthority.TU_TYPE || root.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
										tipoLegame = DatiLegame.LEGAME_DOCUMENTO_TITOLO_UNIFORME;
									}
								}

								nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
												dettOggetti.getDettaglioTitolo(sbnMarcType,nodoCorrente, root.getNatura(),tipoLegame));
							}// Fine inserimento della gestione del dettaglio Titolo Uniforme
							getLegamiElemento(sbnOutPut, nodoCorrente, sbnMarcType, null, documentoType2, elemen2, level, areaDatiPassLocal);
						}
					} else {
						if (elemen2 != null) {
							// e' un tipo authority foglia del albero
							DatiElementoType datiElemento = elemen2.getDatiElementoAut();
							// salto i repertori
							if (datiElemento.getTipoAuthority().getType() != SbnAuthority.RE_TYPE) {
								nodoCorrente.setDescription(getDescrizioneElementoAut(legameElemento, root,nodoCorrente));

								nodoCorrente.setText(getLegameElementoAut(legameElemento, root, nodoCorrente));
								nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

								if ((datiElemento instanceof EnteType)	|| (datiElemento instanceof AutorePersonaleType)) {
									if (utilityCastor.isFormaRinvio(datiElemento)) {
										nodoCorrente.setAutoreFormaRinvio(true);
									}
								}
								if (datiElemento instanceof LuogoType) {
									if (utilityCastor.isFormaRinvio(datiElemento)) {
										if (nodoCorrente != null) {
											nodoCorrente.setLuogoFormaRinvio(true);
										}
									}
								}
								nodoCorrente.setTipoAuthority(datiElemento.getTipoAuthority());
								nodoCorrente.setLivelloAutorita(datiElemento.getLivelloAut().toString());
								// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
	                            // con tipo legame 431 (A08V)
								nodoCorrente.setNatura("A");
								if (datiElemento.getTipoAuthority().toString().equals("TU")) {
									// almaviva2 dicembre 2017 - controllo su valore null della naturaTU dei Titoli uniformi
									if (!ValidazioneDati.strIsNull(datiElemento.getNaturaTU())) {
										nodoCorrente.setNatura(datiElemento.getNaturaTU().toString());
									}
								}

								nodoCorrente.setDatiLegame(utilityCastor.getDatiLegameNodo(legameElemento,null, null));
								nodoCorrente.setIdNode(root.getIdNode() + 1);

								// Inizio inserimento della gestione del
								// dettaglio Autore
								if (datiElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = 0;
									// Intervento interno almaviva2 07.09.2012
									// Inserito controllo per esistenza legame fra TitUniforme e Autore che era mancante per cui
									// nella visualizzazione del dettaglio legame si vedeva solo il dettaglio del'autore arrivo
									// ma non i dati del legame stesso.
									// Intervento interno almaviva2 05.11.2012
									// Inserito controllo per esistenza legame fra TitUniformeMusicale e Autore che era mancante per cui
									// nella visualizzazione del dettaglio legame si vedeva solo il dettaglio del'autore arrivo
									// ma non i dati del legame stesso.
									if (root.getTipoAuthority() != null) {
										if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_AUTORE_AUTORE;
										} else if (root.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
											tipoLegame = DatiLegame.LEGAME_MARCA_AUTORE;
										} else if (root.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_DOCUMENTO_AUTORE;
										} else if (root.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
											tipoLegame = DatiLegame.LEGAME_DOCUMENTO_AUTORE;
										}
									} else {
										tipoLegame = DatiLegame.LEGAME_DOCUMENTO_AUTORE;
									}

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioAutoreGeneraleVO(
													dettOggetti.getDettaglioAutore(sbnMarcType, null, nodoCorrente, root.getNatura() ,tipoLegame));
								}// Fine inserimento della gestione del dettaglio Autore
								// Inizio inseriemnto della gestione dettaglio marca
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = 0;
									if (root.getTipoAuthority() != null) {
										if (root.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_AUTORE_MARCA;
										}
									} else {
										tipoLegame = DatiLegame.LEGAME_DOCUMENTO_MARCA;
									}
									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioMarcaGeneraleVO(
													dettOggetti.getDettaglioMarca(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della gestione del
								// dettaglio Marca
								// Inizio inseriemnto della gestione dettaglio
								// Soggetto
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_SOGGETTO;

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioSoggettoGeneraleVO(
													dettOggetti.getDettaglioSoggetto(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della gestione del dettaglio Soggetto
								// Inizio inseriemnto della gestione dettaglio Classe
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_CLASSE;

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioClasseGeneraleVO(
													dettOggetti.getDettaglioClasse(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della gestione del dettaglio Classe
								// Inizio inseriemnto della gestione dettaglio Termine Thesauro
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_THESAURO;

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTermineThesauroGeneraleVO(
													dettOggetti.getDettaglioTermineThesauro(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della  gestione dettaglio Termine Thesauro

								// Inizio inseriemnto della gestione dettaglio Luogo
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_LUOGO;

									if (root.getTipoAuthority() != null) {
										if (root.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_LUOGO_LUOGO;
										}
									}

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioLuogoGeneraleVO(
													dettOggetti.getDettaglioLuogo(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della gestione del dettaglio Luogo

								//	PARTE NUOVA PER TITOLO UNIFORME TU e UM
								// Inizio inseriemnto della gestione dettaglio
								// Luogo
								else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.TU_TYPE ||
										datiElemento.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
									//DettaglioOggetti dettOggetti = new DettaglioOggetti();
									int tipoLegame = DatiLegame.LEGAME_DOCUMENTO_TITOLO_UNIFORME;
									if (root.getTipoAuthority() != null) {
										if (root.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_DOCUMENTO_TITOLO_UNIFORME;
										}
										// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
										if (root.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
											tipoLegame = DatiLegame.LEGAME_DOCUMENTO_TITOLO_UNIFORME;
										}
									}

									nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
													dettOggetti.getDettaglioTitolo(sbnMarcType, nodoCorrente, root.getNatura(), tipoLegame));
								}// Fine inserimento della gestione del dettaglio Titolo Uniforme
								level--;
							}
						}
					}
				} else
				// SE E UN LEGAME DOC
				if (legameDocType != null) {
					level++;
					documentoType2 = legameDocType.getDocumentoLegato();

					// almaviva2 Evolutiva marzo 2017: La richiesta è quella di poter visualizzare nell’analitica della monografia
					// il numero di sequenza dei titoli analitici dopo la data o, in assenza della data, dopo il tipo legame e prima del titolo,
					//tra parentesi tonde al fine di evidenziarlo meglio, es.: BID  Min N 51 (1) *Arte punica
					// occorre inviare anche il numero di sequenza al metodo getDocumento
					ritorno = getDocumento(documentoType2, nodoCorrente,
							legameDocType.getTipoLegame().toString(),"",  root, legameDocType.getSequenza());
					String descrizioneRitorno = getDescrizioneDocumento(
							documentoType2, nodoCorrente, legameDocType
									.getTipoLegame().toString(), root);
					if (haLegami(documentoType2)) {
						level++;
						nodoCorrente.setDescription(descrizioneRitorno);

						nodoCorrente.setText(ritorno);
						nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

						nodoCorrente.setTipoMateriale(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getTipoMateriale().toString());
						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getLivelloAutDoc().toString());
						nodoCorrente.setNatura(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getNaturaDoc().toString());
						nodoCorrente.setDatiLegame(utilityCastor
								.getDatiLegameNodo(null, legameDocType, null));
						nodoCorrente.setIdNode(root.getIdNode() + 1);
						nodoCorrente.setKey(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getT001());
						nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodoCorrente.setT005(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getT005());
						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getLivelloAutDoc().toString());
						if (myChiamata.equals("P")) {
							if (documentoType2.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBibCount() > 0) {
								String localizzazione = documentoType2.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBib(0);
								if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("S")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
								} else if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("N")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE);
								} else if (localizzazione.substring(7,8).equals("N") && localizzazione.substring(9,10).equals("S")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
								} else {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								}
							} else {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							}
							if (documentoType2
									.getDocumentoTypeChoice().getDatiDocumento().getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE){
								nodoCorrente.setFlagCondiviso(false);
							} else {
								nodoCorrente.setFlagCondiviso(true);
							}
						} else {
							nodoCorrente.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001()) == null) {

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (documentoType2.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz() != null) {
									String localizzazione = documentoType2.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz();
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
									areaDatiPassLocal.setIdLoc(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001());
									areaDatiPassLocal.setNatura(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString());
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, false);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001(),
										String.valueOf(oggettoLocalizzato));
							} else {
								nodoCorrente.setLocalizzazione(Integer.valueOf((String)
										tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001())));
							}
						}

						// Inizio inserimento della gestione del dettaglio Titolo
						//DettaglioOggetti dettOggetti = new DettaglioOggetti();
						nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
										dettOggetti.getDettaglioTitolo(sbnMarcType, nodoCorrente, root.getNatura(), 0));
						// Fine inserimento della gestione del dettaglio Titolo

						getLegamiElemento(sbnOutPut, nodoCorrente, sbnMarcType,null, documentoType2, null, level,areaDatiPassLocal);
					} else {

						nodoCorrente.setDescription(descrizioneRitorno);

						nodoCorrente.setText(ritorno);
						nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

						nodoCorrente.setTipoMateriale(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getTipoMateriale().toString());
						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getLivelloAutDoc().toString());
						nodoCorrente.setNatura(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getNaturaDoc().toString());
						nodoCorrente.setDatiLegame(utilityCastor
								.getDatiLegameNodo(null, legameDocType, null));
						nodoCorrente.setIdNode(root.getIdNode() + 1);
						nodoCorrente.setKey(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getT001());
						nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodoCorrente.setT005(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getT005());
						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiDocumento()
								.getLivelloAutDoc().toString());
						if (myChiamata.equals("P")) {
							if (documentoType2.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBibCount() > 0) {
								String localizzazione = documentoType2.getDocumentoTypeChoice().getDatiDocumento().getInfoLocBib(0);
								if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("S")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
								} else if (localizzazione.substring(7,8).equals("S") && localizzazione.substring(9,10).equals("N")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE);
								} else if (localizzazione.substring(7,8).equals("N") && localizzazione.substring(9,10).equals("S")) {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA);
								} else {
									nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
								}
							} else {
								nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							}

							if (documentoType2
									.getDocumentoTypeChoice().getDatiDocumento().getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE){
								nodoCorrente.setFlagCondiviso(false);
							} else {
								nodoCorrente.setFlagCondiviso(true);
							}
						} else {
							nodoCorrente.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001()) == null) {

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (documentoType2.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz() != null) {
									String localizzazione = documentoType2.getDocumentoTypeChoice().getDatiDocumento().getSbnLocaliz();
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
									areaDatiPassLocal.setIdLoc(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001());
									areaDatiPassLocal.setNatura(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString());
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, false);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001(),
										String.valueOf(oggettoLocalizzato));
							} else {
								nodoCorrente.setLocalizzazione(Integer.valueOf((String)
										tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001())));
							}
						}

						// Inizio inserimento della gestione del dettaglio
						// Titolo
						//DettaglioOggetti dettOggetti = new DettaglioOggetti();
						nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
										dettOggetti.getDettaglioTitolo(sbnMarcType, nodoCorrente, root.getNatura(), 0));
						// Fine inserimento della gestione del dettaglio Titolo
						level--;
					}
				} else
				// SE E UN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {
					level++;
					documentoType2 = legameTitAccessoType.getTitAccessoLegato();
					String sottoTipolegame = "";

					if (legameTitAccessoType.getSottoTipoLegame() != null) {
						sottoTipolegame = legameTitAccessoType.getSottoTipoLegame().toString();
						try {
							sottoTipolegame = codici.cercaDescrizioneCodice(
									sottoTipolegame,
									CodiciType.CODICE_LEGAME_TITOLO_MUSICA,
									CodiciRicercaType.RICERCA_CODICE_SBN);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					ritorno = getDocumento(documentoType2, nodoCorrente,
							legameTitAccessoType.getTipoLegame().toString(),
							sottoTipolegame, root, "");

					String descrizioneRitorno = getDescrizioneDocumento(
							documentoType2, nodoCorrente, legameTitAccessoType
									.getTipoLegame().toString(), root);

					if (haLegami(documentoType2)) {
						level++;
						nodoCorrente.setDescription(descrizioneRitorno);
						nodoCorrente.setText(ritorno);
						nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getLivelloAut().toString());
						nodoCorrente.setNatura(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getNaturaTitAccesso().toString());
						nodoCorrente.setDatiLegame(utilityCastor
								.getDatiLegameNodo(null, null,
										legameTitAccessoType));
						nodoCorrente.setIdNode(root.getIdNode() + 1);
						nodoCorrente.setKey(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getT001());
						nodoCorrente.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodoCorrente.setT005(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getT005());
						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getLivelloAut().toString());
						if (myChiamata.equals("P")) {
							nodoCorrente.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (documentoType2
									.getDocumentoTypeChoice().getDatiTitAccesso().getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE){
								nodoCorrente.setFlagCondiviso(false);
							} else {
								nodoCorrente.setFlagCondiviso(true);
							}
						} else {
							nodoCorrente.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getT001()) == null) {

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getSbnLocaliz() != null) {
									String localizzazione = documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getSbnLocaliz();
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
									areaDatiPassLocal.setIdLoc(documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getT001());
									areaDatiPassLocal.setNatura(documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getNaturaTitAccesso().toString());
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, false);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodoCorrente.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getT001(),
										String.valueOf(oggettoLocalizzato));
							} else {
								nodoCorrente.setLocalizzazione(Integer.valueOf((String)
										tabellaLocalizzazioni.get(documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getT001())));
							}
						}
						// Inizio inserimento della gestione del dettaglio
						// Titolo
						//DettaglioOggetti dettOggetti = new DettaglioOggetti();
						nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(
										dettOggetti.getDettaglioTitolo(sbnMarcType, nodoCorrente, root.getNatura(), 0));
						// Fine inserimento della gestione del dettaglio Titolo
						getLegamiElemento(sbnOutPut, nodoCorrente, sbnMarcType,	null, documentoType2, null, level, areaDatiPassLocal);
					} else {

						nodoCorrente.setDescription(descrizioneRitorno);

						nodoCorrente.setText(ritorno);
						nodoCorrente.setRigaReticoloCtr(setKeyCtr++);

						nodoCorrente.setLivelloAutorita(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getLivelloAut().toString());
						nodoCorrente.setNatura(documentoType2
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getNaturaTitAccesso().toString());
						nodoCorrente.setDatiLegame(utilityCastor
								.getDatiLegameNodo(null, null,
										legameTitAccessoType));
						nodoCorrente.setIdNode(root.getIdNode() + 1);

						// Inizio inserimento della gestione del dettaglio Titolo
						AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
						areaDatiDettaglioOggettiVO.setDettaglioLegameVO(null);
						areaDatiDettaglioOggettiVO.setDettaglioAutoreGeneraleVO(null);
						//DettaglioOggetti dettOggetti = new DettaglioOggetti();
						DettaglioTitoloCompletoVO dettTitCompVO = dettOggetti.getDettaglioTitolo(sbnMarcType, nodoCorrente,	root.getNatura(), 0);

						nodoCorrente.getAreaDatiDettaglioOggettiVO().setDettaglioTitoloCompletoVO(dettTitCompVO);
						// Fine inserimento della gestione del dettaglio Titolo
						level--;
					}
				}
			}
		}
		livello--;
		return null;
	}

	public String getDescrizioneDocumento(DocumentoType documentoType,
			TreeElementViewTitoli nodo, String codiceLegame,
			TreeElementViewTitoli root) {

		UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
		DocumentoTypeChoice documentoTypeChoice = documentoType
				.getDocumentoTypeChoice();
		DatiDocType datiDocType = documentoTypeChoice.getDatiDocumento();

		String TitoloProprio = "";

		if (datiDocType != null) {
			TitoloProprio = utilityCampiTitolo
					.getTitoloResponsabilitaXReticolo(datiDocType);
			return TitoloProprio;
		}

		if (documentoTypeChoice.getDatiTitAccesso() != null) {
			TitAccessoType titAccessoType = documentoTypeChoice
					.getDatiTitAccesso();
			if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
				return utilityCampiTitolo
						.getTitoloResponsabilitaDatiTitAccessoXReticolo(
								titAccessoType, c200);
			}
			if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT517();
				return utilityCampiTitolo
						.getTitoloResponsabilitaDatiTitAccessoXReticolo(
								titAccessoType, c200);
			}
			if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
				return utilityCampiTitolo
						.getTitoloResponsabilitaDatiTitAccessoXReticolo(
								titAccessoType, c200);
			}
			if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT423()
						.getT200();
				return utilityCampiTitolo
						.getTitoloResponsabilitaDatiTitAccessoXReticolo(
								titAccessoType, c200);
			}
		}
		return "";
	}

	public String getDocumento(DocumentoType documentoType,
			TreeElementViewTitoli nodo, String codiceLegame, String sottoTipoLegame,
			TreeElementViewTitoli root, String numeroSequenza ) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);
		UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
		DocumentoTypeChoice documentoTypeChoice = documentoType
				.getDocumentoTypeChoice();
		DatiDocType datiDocType = documentoTypeChoice.getDatiDocumento();
		String value = "";
		String Natura = "";
		String TitoloProprio = "";
		String codiceLegameCompleto = " ";
		String livelloAutorita = "";
		String livelloAutoritaDesc = "";

		String myData1 = "";
		// LEGGO SBNOUTPUT.DOCUMENTO.DATIDOCUMENTO PRIMO LIVELLO
		if (datiDocType != null) {
			TitoloProprio = utilityCampiTitolo.getTitoloResponsabilitaXReticolo(datiDocType);

			if (datiDocType.getNaturaDoc() != null) {
				Natura = datiDocType.getNaturaDoc().toString();
			}

			if (datiDocType.getLivelloAutDoc() != null) {
				livelloAutorita = datiDocType.getLivelloAutDoc().toString();
				// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
				// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
				TB_CODICI codici = new TB_CODICI();
				try {
					codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
						livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
					} else {
						livelloAutoritaDesc = livelloAutorita;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


			if (root != null) {
				if (root.getNatura() != null) {
					codiceLegameCompleto = root.getNatura().toString()
							+ codiceLegame + Natura;
				} else {
					codiceLegameCompleto = Natura + codiceLegame + Natura;
				}
			} else {
				codiceLegameCompleto = Natura + codiceLegame + Natura;
			}

			// almaviva2 Evolutiva marzo 2017: La richiesta è quella di poter visualizzare nell’analitica della monografia
			// il numero di sequenza dei titoli analitici dopo la data o, in assenza della data, dopo il tipo legame e prima del titolo,
			//tra parentesi tonde al fine di evidenziarlo meglio, es.: BID  Min N 51 (1) *Arte punica
			// almaviva2 Correttiva dicembre 2017: la richiesta di cui sopra vale anche per i legami alle W (non solo M)
			boolean visualizzaSequenza = false;
			if (codiceLegameCompleto.equals("M464N") || codiceLegameCompleto.equals("W464N")) {
				visualizzaSequenza = true;
			}

			if (!codiceLegame.equals("")) {
				try {
					codiceLegameCompleto = codici.cercaDescrizioneCodice(
							codiceLegameCompleto,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				codiceLegameCompleto = " ";
			}

			if (datiDocType.getT100() != null) {
				if (datiDocType.getT100().getA_100_9() != null) {
					myData1 = datiDocType.getT100().getA_100_9().toString();
				}
			}

			// almaviva2 Evolutiva marzo 2017: La richiesta è quella di poter visualizzare nell’analitica della monografia
			// il numero di sequenza dei titoli analitici dopo la data o, in assenza della data, dopo il tipo legame e prima del titolo,
			//tra parentesi tonde al fine di evidenziarlo meglio, es.: BID  Min N 51 (1) *Arte punica
			// value = Natura + livelloAutorita + " " + codiceLegameCompleto + " " + myData1 + " " + TitoloProprio;
			// value = livelloAutoritaDesc + " " + Natura + " " + codiceLegameCompleto + " " + myData1 + " " + TitoloProprio;
			if (visualizzaSequenza) {
				if (numeroSequenza == null || numeroSequenza.equals("")) {
					value = livelloAutoritaDesc + " " + Natura + " " + codiceLegameCompleto + " " + myData1 + " " + TitoloProprio;
				} else {
					value = livelloAutoritaDesc + " " + Natura + " " + codiceLegameCompleto + " " + myData1 + " (" +
					numeroSequenza.trim() + ") " + TitoloProprio;
				}
			} else {
				value = livelloAutoritaDesc + " " + Natura + " " + codiceLegameCompleto + " " + myData1 + " " + TitoloProprio;
			}

		}

		// IN ALTERNATIVA LEGGO SBNOUTPUT.DOCUMENTO.DATITITACCESSO PRIMO LIVELLO
		if (documentoTypeChoice.getDatiTitAccesso() != null) {
			TitAccessoType titAccessoType = documentoTypeChoice
					.getDatiTitAccesso();
			if (nodo != null) {
				nodo.setKey(titAccessoType.getT001());
				nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
				nodo.setT005(titAccessoType.getT005());
				nodo.setLivelloAutorita(titAccessoType.getLivelloAut().toString());
				if (myChiamata.equals("P")) {
					nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
					if (titAccessoType.getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE){
						nodo.setFlagCondiviso(false);
					} else {
						nodo.setFlagCondiviso(true);
					}
				} else {
					nodo.setFlagCondiviso(true);
					int oggettoLocalizzato = 0;
					if (tabellaLocalizzazioni.get(titAccessoType.getT001()) == null) {

						// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
						// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
						oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
						if (titAccessoType.getSbnLocaliz() != null) {
							String localizzazione = titAccessoType.getSbnLocaliz();
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
							areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
							areaDatiPassLocal.setIdLoc(titAccessoType.getT001());
							areaDatiPassLocal.setNatura(titAccessoType.getNaturaTitAccesso().toString());
							oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
						}
						// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

						nodo.setLocalizzazione(oggettoLocalizzato);
						tabellaLocalizzazioni.put(titAccessoType.getT001(), String.valueOf(oggettoLocalizzato));
					} else {
						nodo.setLocalizzazione(Integer.valueOf((String)tabellaLocalizzazioni.get(titAccessoType.getT001())));
					}
				}
			}
			if (titAccessoType.getNaturaTitAccesso().toString() != null) {
				Natura = titAccessoType.getNaturaTitAccesso().toString();
			}

			if (titAccessoType.getLivelloAut() != null) {
				livelloAutorita = titAccessoType.getLivelloAut().toString();
				// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
				// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
				TB_CODICI codici = new TB_CODICI();
				try {
					codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
					if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
						livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
					} else {
						livelloAutoritaDesc = livelloAutorita;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (nodo != null) {
				// MODIFICA DEL 22.11.2007 L.almaviva2 la natura della radice del legame va trovata nel getParent
				// e non nel nodo che sarebbe la natura di se stesso
				//if (nodo.getNatura() != null) {
				if (((TreeElementViewTitoli)nodo.getParent()).getNatura() != null) {
					codiceLegameCompleto = ((TreeElementViewTitoli)nodo.getParent()).getNatura().toString()	+ codiceLegame + Natura;
				} else {
					codiceLegameCompleto = Natura + codiceLegame + Natura;
				}
			} else {
				codiceLegameCompleto = Natura + codiceLegame + Natura;
			}

			if (!codiceLegame.equals("")) {
				try {
					codiceLegameCompleto = codici.cercaDescrizioneCodice(
							codiceLegameCompleto,
							CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
							CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				codiceLegameCompleto = " ";
			}

			if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
				// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
				// C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454A().getT200();

//				value = Natura	+ livelloAutorita + " " + codiceLegameCompleto	+ " "
//						+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
				value = livelloAutoritaDesc + " " + Natura	+ " " + codiceLegameCompleto	+ " "
				+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
			}

			// se i Dati sono di natura D leggo T517
			if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT517();

				// Inserito in Analitica anche il campo del SottoTipo Legame
				if (sottoTipoLegame.equals("")) {
//					value = Natura	+ livelloAutorita	+ " " + codiceLegameCompleto	+ " "
//							+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					value = livelloAutoritaDesc	+ " " + Natura	+ " " + codiceLegameCompleto	+ " "
					+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
				} else {
//					value = Natura	+ livelloAutorita	+ " " + codiceLegameCompleto + " [" + sottoTipoLegame + "] "
//							+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
					value = livelloAutoritaDesc	+ " " + Natura	+ " " + codiceLegameCompleto + " [" + sottoTipoLegame + "] "
					+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
				}
			}

			// se i Dati sono di natura P leggo T510
			if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
//				value = Natura	+ livelloAutorita	+ " " + codiceLegameCompleto + " "
//						+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
				value = livelloAutoritaDesc	+ " " + Natura	+ " " + codiceLegameCompleto + " "
				+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
			}

			// se i Dati sono di natura T leggo T423
			if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
				C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT423().getT200();
//				value = Natura	+ livelloAutorita	+ " " + codiceLegameCompleto	+ " "
//						+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
				value = livelloAutoritaDesc	+ " " + Natura	+ " " + codiceLegameCompleto	+ " "
				+ utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccessoXReticolo(titAccessoType, c200);
			}
		}

		return value;
	}

	public String getDescrizioneElementoAut(
			LegameElementoAutType legameElemento, TreeElementViewTitoli root,
			TreeElementViewTitoli nodo) {
		String descrizione = "";
		if (legameElemento != null) {
			// prendo il tipo legame
			SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
			String tipoLegameString = sbnLegameAut.toString();

			if ((!tipoLegameString.equals(810))
					&& (!tipoLegameString.equals(815))) {

				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();
				String Legame = "";
				String nominativoLegame;
				int tipoLegameNumerico = 0;

				if (elementAutType != null) {
					if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {
						AutorePersonaleType autorePersonale = (AutorePersonaleType) elementAutType
								.getDatiElementoAut();
						try {
							tipoLegameNumerico = Integer
									.parseInt(legameElemento.getTipoLegame()
											.toString());
						} catch (NumberFormatException exx) {
						}

						Legame = legameElemento.getTipoLegame().toString();

						if ((tipoLegameNumerico >= 410)
								&& (tipoLegameNumerico <= 517)) {
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							return nominativoLegame;
						}
						if ((tipoLegameNumerico >= 700)
								&& (tipoLegameNumerico <= 712)) {
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							return nominativoLegame;
						}
						if (((Legame.equals("4XX")) || (Legame.equals("5XX")))
								&& (root.getTipoAuthority().toString()
										.equals("AU"))) {
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							return nominativoLegame;
						}
						if (tipoLegameNumerico == 921) {
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							return nominativoLegame;
						}
					} else if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeMusicaType) {
						TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) elementAutType
								.getDatiElementoAut();

						nominativoLegame = utilityCastor
								.getNominativoDatiElemento(titoloUniformeMusicaType);
						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeType) {
						TitoloUniformeType titoloUniformeType = (TitoloUniformeType) elementAutType
								.getDatiElementoAut();
						nominativoLegame = utilityCastor
								.getNominativoDatiElemento(titoloUniformeType);
						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof EnteType) {
						EnteType enteType = (EnteType) elementAutType
								.getDatiElementoAut();
						nominativoLegame = utilityCastor
								.getNominativoDatiElemento(enteType);
						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof MarcaType) {

						MarcaType marcaType = (MarcaType) elementAutType
								.getDatiElementoAut();
						nominativoLegame = marcaType.getT921().getA_921();
						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof SoggettoType) {
						SoggettoType soggettoType = (SoggettoType) elementAutType
								.getDatiElementoAut();
						nominativoLegame = SemanticaUtil.getSoggettarioSBN(soggettoType.getT250());

						// Inizio Modifica almaviva2 25.09.2012 per riportare edizione soggettario ovunque in analitica e dettaglio
						if (soggettoType.getT250().getEdizione() != null) {
							nominativoLegame = nominativoLegame + " " + soggettoType.getT250().getEdizione().toString();
						}
						// Fine Modifica almaviva2 25.09.2012 per riportare edizione soggettario ovunque in analitica e dettaglio

						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof ClasseType) {
						ClasseType classeType = (ClasseType) elementAutType
								.getDatiElementoAut();

						if (tipoLegameString.equals("676")) {
							nominativoLegame = classeType.getClasseTypeChoice()
									.getT676().getC_676();
							if (nominativoLegame == null)
								nominativoLegame = "";
						} else {
							nominativoLegame = classeType.getClasseTypeChoice()
									.getT686().getC_686();
							if (nominativoLegame == null)
								nominativoLegame = "";
						}
						return nominativoLegame;
					} else if (elementAutType.getDatiElementoAut() instanceof TermineType) {
						TermineType termineType = (TermineType) elementAutType
								.getDatiElementoAut();
						nominativoLegame = termineType.getT935().getC2_935().toString()
								+ " - "
								+ termineType.getT935().getA_935().toString();
						return nominativoLegame;

					} else if (elementAutType.getDatiElementoAut() instanceof LuogoType) {
						LuogoType luogoType = (LuogoType) elementAutType.getDatiElementoAut();
						nominativoLegame = luogoType.getT260().getD_260().toString();
						return nominativoLegame;
					}
				}
			}
		}

		// =================================================
		return descrizione;
	}

	public String getLegameElementoAut(LegameElementoAutType legameElemento,
			TreeElementViewTitoli root, TreeElementViewTitoli nodo) {

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao( indice, polo, user);

		String valueDelNodo = null;

		String livelloAutorita = "";
		String livelloAutoritaDesc = "";

		if (legameElemento != null) {
			// prendo il tipo legame
			SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
			String tipoLegameString = sbnLegameAut.toString();

			// NON PRENDE IN ESAME I REPERTORI LEGATI
			// tipo legame 810 , tipo legame 815 REPERTORI
			if ((!tipoLegameString.equals(810))
					&& (!tipoLegameString.equals(815))) {

				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();
				String VIDLegato;
				//String Responsabilita = "";
				String Legame = "";
				String CodiceRelazione = "";
				String TipoRespons = "";
				String nominativoLegame;
				int tipoLegameNumerico = 0;

				if (elementAutType != null) {
					// SE AUTORE IN FORMA ACCETTATA O di rinvio
					if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {
						AutorePersonaleType autorePersonale = (AutorePersonaleType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(autorePersonale.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(autorePersonale.getT005());
						nodo.setLivelloAutorita(autorePersonale.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (autorePersonale.getLivelloAut() != null) {
							livelloAutorita = autorePersonale.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}


						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (autorePersonale.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(autorePersonale.getT001()) == null) {

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
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(autorePersonale.getT001());
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(autorePersonale.getT001(), String.valueOf(oggettoLocalizzato));
							} else {
								nodo.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(autorePersonale.getT001())));
							}
						}
						try {
							tipoLegameNumerico = Integer.parseInt(legameElemento.getTipoLegame().toString());
						} catch (NumberFormatException exx) {
						}

						Legame = legameElemento.getTipoLegame().toString();

						if ((tipoLegameNumerico >= 410)	&& (tipoLegameNumerico <= 517)) {
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							valueDelNodo = nominativoLegame;
						}

						if ((tipoLegameNumerico >= 700)
								&& (tipoLegameNumerico <= 712)) {
							VIDLegato = autorePersonale.getT001();
							if (legameElemento.getRelatorCode() != null) {
								CodiceRelazione = legameElemento
										.getRelatorCode().toString();
							}
							if (legameElemento.getTipoRespons() != null) {
								TipoRespons = legameElemento.getTipoRespons()
										.toString();
							}
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							// DECODIFICO IL CODICE RELAZIONE
							if (!CodiceRelazione.equals("")) {
								// OMETTO LA STRINGA SE é UN AUTORE codice 070
								if (!CodiceRelazione.equals("070")) {
									try {
										CodiceRelazione = CodiciProvider
												.cercaDescrizioneCodice(
														CodiceRelazione,
														CodiciType.CODICE_LEGAME_TITOLO_AUTORE,
														CodiciRicercaType.RICERCA_CODICE_UNIMARC);
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									CodiceRelazione = " ";
								}
							}
							if (CodiceRelazione == null) {
								CodiceRelazione = " ";
							}

							// Inizio Modifica almaviva2 25.11.2010 BUG MANTIS 4008 - racchiudere l'informazione di relator code in chiaro tra quadre
							// valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							if (CodiceRelazione.equals(" ")) {
								valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							} else {
								if (CodiceRelazione.equals("")) {
									valueDelNodo = TipoRespons + " " + nominativoLegame;
								} else {
									valueDelNodo = TipoRespons + " [" + CodiceRelazione + "] " + nominativoLegame;
								}
							}
							// Fine Modifica almaviva2 25.11.2010 BUG MANTIS 4008
						}

						if (((Legame.equals("4XX")) || (Legame.equals("5XX")))
								&& (root.getTipoAuthority().toString()
										.equals("AU"))) {
							VIDLegato = autorePersonale.getT001();
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							//valueDelNodo = VIDLegato + " " + nominativoLegame;
							valueDelNodo = nominativoLegame;
						}

						if (tipoLegameNumerico == 921) {
							VIDLegato = autorePersonale.getT001();
							nominativoLegame = utilityCastor
									.getNominativoDatiElemento(autorePersonale);
							valueDelNodo = nominativoLegame;
						}
					}
					// E UN TITOLO UNIFORME MUSICA
					else if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeMusicaType) {
						TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) elementAutType
								.getDatiElementoAut();

						nominativoLegame = utilityCastor
								.getNominativoDatiElemento(titoloUniformeMusicaType);
						nodo.setKey(titoloUniformeMusicaType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(titoloUniformeMusicaType.getT005());
						nodo.setLivelloAutorita(titoloUniformeMusicaType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (titoloUniformeMusicaType.getLivelloAut() != null) {
							livelloAutorita = titoloUniformeMusicaType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (titoloUniformeMusicaType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(titoloUniformeMusicaType.getT001()) == null) {

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (titoloUniformeMusicaType.getSbnLocaliz() != null) {
									String localizzazione = titoloUniformeMusicaType.getSbnLocaliz();
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
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(titoloUniformeMusicaType.getT001());
									areaDatiPassLocal.setAuthority("UM");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(titoloUniformeMusicaType.getT001(), String.valueOf(oggettoLocalizzato));
							} else {
								nodo.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(titoloUniformeMusicaType.getT001())));
							}
						}
						try {
							tipoLegameNumerico = Integer
									.parseInt(legameElemento.getTipoLegame()
											.toString());
						} catch (NumberFormatException exx) {
						}

						Legame = legameElemento.getTipoLegame().toString();

						if ((tipoLegameNumerico >= 410)
								&& (tipoLegameNumerico <= 517)) {

							String codiceLegameCompleto = "";
							codiceLegameCompleto = root.getNatura()
									+ tipoLegameNumerico + "A";
							try {
								codiceLegameCompleto = codici
										.cercaDescrizioneCodice(
												codiceLegameCompleto,
												CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
												CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							nominativoLegame = "A" + " " + codiceLegameCompleto	+ " " + nominativoLegame;
							valueDelNodo = nominativoLegame;
						}

						// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
						// il tipo legame 531 si defferenzia in 531 con, a seguire una lettera che differenzia il tipo quindi non si può fare if su
						// tipoLegameNumerico ma si deve fare su Legame che è di tipo String
						if (tipoLegameNumerico == 0 &&
								Legame.startsWith("531")) {
							String codiceLegameCompleto = "";
							codiceLegameCompleto = root.getNatura()	+ Legame + "A";
							try {
								CodiceRelazione = codici
										.cercaDescrizioneCodice(
												codiceLegameCompleto,
												CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
												CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							nominativoLegame = "A" + " " + CodiceRelazione + " " + nominativoLegame;
							valueDelNodo = nominativoLegame;
						}

						if ((tipoLegameNumerico >= 700)
								&& (tipoLegameNumerico <= 712)) {
							VIDLegato = titoloUniformeMusicaType.getT001();
							valueDelNodo = nominativoLegame;
							if (legameElemento.getRelatorCode() != null) {
								CodiceRelazione = legameElemento
										.getRelatorCode().toString();
							}
							if (legameElemento.getTipoRespons() != null) {
								TipoRespons = legameElemento.getTipoRespons()
										.toString();
							}
							// DECODIFICO IL CODICE RELAZIONE
							if (!CodiceRelazione.equals("")) {
								// OMETTO LA STRINGA SE é UN AUTORE codice 070
								if (!CodiceRelazione.equals("070")) {
									try {
										CodiceRelazione = codici
												.cercaDescrizioneCodice(
														CodiceRelazione,
														CodiciType.CODICE_LEGAME_TITOLO_AUTORE,
														CodiciRicercaType.RICERCA_CODICE_UNIMARC);
									} catch (RemoteException e) {
										e.printStackTrace();
									}
								} else {
									CodiceRelazione = " ";
								}

							}
							if (CodiceRelazione == null) {
								CodiceRelazione = "NON TRADOTTO";
							}
							// Inizio Modifica almaviva2 25.11.2010 BUG MANTIS 4008 - racchiudere l'informazione di relator code in chiaro tra quadre
							// valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							if (CodiceRelazione.equals(" ")) {
								valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							} else {
								if (CodiceRelazione.equals("")) {
									valueDelNodo = TipoRespons + " " + nominativoLegame;
								} else {
									valueDelNodo = TipoRespons + " [" + CodiceRelazione + "] " + nominativoLegame;
								}
							}
							// Fine Modifica almaviva2 25.11.2010 BUG MANTIS 4008
						}

						if (((Legame.equals("4XX")) || (Legame.equals("5XX")))
								&& (root.getTipoAuthority().toString()
										.equals("AU"))) {
							VIDLegato = titoloUniformeMusicaType.getT001();
							valueDelNodo = nominativoLegame;
						}

						if (tipoLegameNumerico == 921) {
							VIDLegato = titoloUniformeMusicaType.getT001();
							valueDelNodo = nominativoLegame;
						}
					}

					// E UN TITOLO UNIFORME
					else if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeType) {
						TitoloUniformeType titoloUniformeType = (TitoloUniformeType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(titoloUniformeType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(titoloUniformeType.getT005());
						nodo.setLivelloAutorita(titoloUniformeType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (titoloUniformeType.getLivelloAut() != null) {
							livelloAutorita = titoloUniformeType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (titoloUniformeType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(titoloUniformeType.getT001()) == null) {

								// Inizio intervento almaviva2 01.04.2011 - Arcobaleno di Indice per eliminare chiamata di
								// ricerca localizzazioni e sostituirla con la decodifica dei valori di SbnLocaliz
								oggettoLocalizzato = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
								if (titoloUniformeType.getSbnLocaliz() != null) {
									String localizzazione = titoloUniformeType.getSbnLocaliz();
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
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(titoloUniformeType.getT001());
									areaDatiPassLocal.setAuthority("TU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(titoloUniformeType.getT001(), String.valueOf(oggettoLocalizzato));
							} else {
								nodo.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(titoloUniformeType.getT001())));
							}

						}
						nominativoLegame = utilityCastor
								.getNominativoDatiElemento(titoloUniformeType);

						try {
							tipoLegameNumerico = Integer
									.parseInt(legameElemento.getTipoLegame()
											.toString());
						} catch (NumberFormatException exx) {
						}

						Legame = legameElemento.getTipoLegame().toString();

						if ((tipoLegameNumerico >= 410)
								&& (tipoLegameNumerico <= 517)) {

							String codiceLegameCompleto = "";

							// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
							if (tipoLegameNumerico == 431) {
								// TitoloUniforme-Forma variante (natura V
								codiceLegameCompleto = root.getNatura()	+ tipoLegameNumerico + "V";
								try {
									CodiceRelazione = codici
											.cercaDescrizioneCodice(
													codiceLegameCompleto,
													CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
													CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
								} catch (RemoteException e) {
									e.printStackTrace();
								}

								nominativoLegame = "V" + " " + CodiceRelazione + " " + nominativoLegame;
								valueDelNodo = nominativoLegame;
							} else {
								// Titolo-Titolo
								codiceLegameCompleto = root.getNatura()
										+ tipoLegameNumerico + "A";
								try {
									CodiceRelazione = codici
											.cercaDescrizioneCodice(
													codiceLegameCompleto,
													CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
													CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
								} catch (RemoteException e) {
									e.printStackTrace();
								}

								nominativoLegame = "A" + " " + CodiceRelazione + " " + nominativoLegame;
								valueDelNodo = nominativoLegame;
							}


						}

						// almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)
						// il tipo legame 531 si defferenzia in 531 con, a seguire una lettera che differenzia il tipo quindi non si può fare if su
						// tipoLegameNumerico ma si deve fare su Legame che è di tipo String
						if (tipoLegameNumerico == 0 &&
								Legame.startsWith("531")) {
							String codiceLegameCompleto = "";
							codiceLegameCompleto = root.getNatura()	+ Legame + "A";
							try {
								CodiceRelazione = codici
										.cercaDescrizioneCodice(
												codiceLegameCompleto,
												CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
												CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							nominativoLegame = "A" + " " + CodiceRelazione + " " + nominativoLegame;
							valueDelNodo = nominativoLegame;
						}


						if ((tipoLegameNumerico >= 700)
								&& (tipoLegameNumerico <= 712)) {
							VIDLegato = titoloUniformeType.getT001();
							valueDelNodo = VIDLegato + " " + nominativoLegame;
							if (legameElemento.getRelatorCode() != null) {
								CodiceRelazione = legameElemento
										.getRelatorCode().toString();
							}
							if (legameElemento.getTipoRespons() != null) {
								TipoRespons = legameElemento.getTipoRespons()
										.toString();
							}
							// DECODIFICO IL CODICE RELAZIONE
							if (!CodiceRelazione.equals("")) {
								if (!CodiceRelazione.equals("070")) {
									try {
										CodiceRelazione = codici
												.cercaDescrizioneCodice(
														CodiceRelazione,
														CodiciType.CODICE_LEGAME_TITOLO_AUTORE,
														CodiciRicercaType.RICERCA_CODICE_UNIMARC);
									} catch (RemoteException e) {
										e.printStackTrace();
									}
								} else {
									CodiceRelazione = " ";
								}
							}
							if (CodiceRelazione == null) {
								CodiceRelazione = " ";
							}
							// Inizio Modifica almaviva2 25.11.2010 BUG MANTIS 4008 - racchiudere l'informazione di relator code in chiaro tra quadre
							// valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							if (CodiceRelazione.equals(" ")) {
								valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							} else {
								if (CodiceRelazione.equals("")) {
									valueDelNodo = TipoRespons + " " + nominativoLegame;
								} else {
									valueDelNodo = TipoRespons + " [" + CodiceRelazione + "] " + nominativoLegame;
								}
							}
							// Fine Modifica almaviva2 25.11.2010 BUG MANTIS 4008

						}

						if (((Legame.equals("4XX")) || (Legame.equals("5XX")))
								&& (root.getTipoAuthority().toString().equals("AU"))) {
							VIDLegato = titoloUniformeType.getT001();
							valueDelNodo = nominativoLegame;
						}

						if (tipoLegameNumerico == 921) {
							VIDLegato = titoloUniformeType.getT001();
							valueDelNodo = nominativoLegame;
						}
					}

					// SE AUTORE IN FORMA TitoloUniformeType
					// SECONDO ME MANCA E DAL PROTOCOLLO NON ARRIVA IN MANIERA
					// CORRETTA
					// SE E' UN ENTE DI FATTO E' == AUTORE in forma acettata o
					// DI rinvio
					else if (elementAutType.getDatiElementoAut() instanceof EnteType) {
						EnteType enteType = (EnteType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(enteType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(enteType.getT005());
						nodo.setLivelloAutorita(enteType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (enteType.getLivelloAut() != null) {
							livelloAutorita = enteType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (enteType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(enteType.getT001()) == null) {

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
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(enteType.getT001());
									areaDatiPassLocal.setAuthority("AU");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(enteType.getT001(), String.valueOf(oggettoLocalizzato));
							} else {
								nodo.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(enteType.getT001())));
							}
						}
						nominativoLegame = utilityCastor.getNominativoDatiElemento(enteType);

						try {
							tipoLegameNumerico = Integer.parseInt(legameElemento.getTipoLegame().toString());
						} catch (NumberFormatException exx) {
						}

						Legame = legameElemento.getTipoLegame().toString();

						if ((tipoLegameNumerico >= 410)
								&& (tipoLegameNumerico <= 517)) {
							nominativoLegame = utilityCastor.getNominativoDatiElemento(enteType);
							valueDelNodo = nominativoLegame;
						}

						if ((tipoLegameNumerico >= 700)
								&& (tipoLegameNumerico <= 712)) {
							VIDLegato = enteType.getT001();
							valueDelNodo = VIDLegato + " " + nominativoLegame;
							if (legameElemento.getRelatorCode() != null) {
								CodiceRelazione = legameElemento
										.getRelatorCode().toString();
							}
							if (legameElemento.getTipoRespons() != null) {
								TipoRespons = legameElemento.getTipoRespons()
										.toString();
							}
							// DECODIFICO IL CODICE RELAZIONE
							if (!CodiceRelazione.equals("")) {
								// OMETTO LA STRINGA SE é UN AUTORE codice 070
								if (!CodiceRelazione.equals("070")) {
									try {
										CodiceRelazione = CodiciProvider
												.cercaDescrizioneCodice(
														CodiceRelazione,
														CodiciType.CODICE_LEGAME_TITOLO_AUTORE,
														CodiciRicercaType.RICERCA_CODICE_UNIMARC);
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									CodiceRelazione = " ";
								}
							}

							if (CodiceRelazione == null) {
								CodiceRelazione = " ";
							}
							// Inizio Modifica almaviva2 25.11.2010 BUG MANTIS 4008 - racchiudere l'informazione di relator code in chiaro tra quadre
							// valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							if (CodiceRelazione.equals(" ")) {
								valueDelNodo = TipoRespons + " " + CodiceRelazione + " " + nominativoLegame;
							} else {
								if (CodiceRelazione.equals("")) {
									valueDelNodo = TipoRespons + " " + nominativoLegame;
								} else {
									valueDelNodo = TipoRespons + " [" + CodiceRelazione + "] " + nominativoLegame;
								}
							}
							// Fine Modifica almaviva2 25.11.2010 BUG MANTIS 4008
						}

						if (((Legame.equals("4XX")) || (Legame.equals("5XX")))
								&& (root.getTipoAuthority().toString().equals("AU"))) {
							VIDLegato = enteType.getT001();
							valueDelNodo = nominativoLegame;
						}

						if (tipoLegameNumerico == 921) {
							VIDLegato = enteType.getT001();
							valueDelNodo = nominativoLegame;
						}
					}

					// SE E' UNA MARCA Forma accettata o rinvio(INSERITA
					// ICONA=SI)
					else if (elementAutType.getDatiElementoAut() instanceof MarcaType) {

						MarcaType marcaType = (MarcaType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(marcaType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(marcaType.getT005());
						nodo.setLivelloAutorita(marcaType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (marcaType.getLivelloAut() != null) {
							livelloAutorita = marcaType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (marcaType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
							int oggettoLocalizzato = 0;
							if (tabellaLocalizzazioni.get(marcaType.getT001()) == null) {

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
									AreaDatiLocalizzazioniAuthorityVO areaDatiPassLocal = new AreaDatiLocalizzazioniAuthorityVO();
									areaDatiPassLocal.setIndice(true);
									areaDatiPassLocal.setPolo(false);
									areaDatiPassLocal.setCodiceSbn(codSbnBiblioteca);
									areaDatiPassLocal.setIdLoc(marcaType.getT001());
									areaDatiPassLocal.setAuthority("MA");
									oggettoLocalizzato = gestioneAllAuthority.verificaLocalizzazione(areaDatiPassLocal, true);
								}
								// Fine intervento almaviva2 01.04.2011 - Arcobaleno di Indice

								nodo.setLocalizzazione(oggettoLocalizzato);
								tabellaLocalizzazioni.put(marcaType.getT001(), String.valueOf(oggettoLocalizzato));
							} else {
								nodo.setLocalizzazione(Integer.valueOf((String) tabellaLocalizzazioni.get(marcaType.getT001())));
							}
						}
						nominativoLegame = marcaType.getT921().getA_921();

						// Inizializza la stringa che conterrà le
						// Citazioni Standard della Marca trovata
						String citazioneStandardString = " ";

						// CONTROLLA I LEGAMI MARCA PER TROVARE LE CITAZIONI
						for (int k = 0; k < elementAutType.getLegamiElementoAutCount(); k++) {
							LegamiType legamiTypeM = elementAutType.getLegamiElementoAut(k);

							for (int y = 0; y < legamiTypeM.getArrivoLegameCount(); y++) {

								ArrivoLegame arrivoLegameM = legamiTypeM.getArrivoLegame(y);
								LegameElementoAutType legameElementoM = arrivoLegameM.getLegameElementoAut();

								SbnLegameAut sbnLegameAutM = legameElementoM.getTipoLegame();
								String tipoLegameStringM = sbnLegameAutM.toString();

								// REPERTORI LEGATI ALLA MARCA
								// tipo legame 810 , tipo legame 815 REPERTORI

								// Se la marca ha legami a repertori
								if ((tipoLegameStringM.equals(810))
										|| (tipoLegameStringM.equals(815))) {
									String siglaRepertorioLegato = legameElementoM
											.getIdArrivo();
									int citazioneLegata = legameElementoM
											.getCitazione();
									String citazioneStandard = siglaRepertorioLegato
											+ citazioneLegata;
									citazioneStandardString = citazioneStandardString
											+ citazioneStandard;
									if (y != (legamiTypeM
											.getArrivoLegameCount() - 1)) {
										citazioneStandardString = citazioneStandardString
												+ ", ";
									}
								}// end if
							}// end for interno
						}// end for esterno

						// VALORE DEL NODO

						// Se non ha trovato la citazione
						if (citazioneStandardString.equals(" ")) {
							valueDelNodo = nominativoLegame;
						} else {
							valueDelNodo = nominativoLegame;
						}
					}

					// SE E' UN soggetto
					else if (elementAutType.getDatiElementoAut() instanceof SoggettoType) {
						SoggettoType soggettoType = (SoggettoType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(soggettoType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(soggettoType.getT005());
						nodo.setLivelloAutorita(soggettoType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (soggettoType.getLivelloAut() != null) {
							livelloAutorita = soggettoType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
						if (myChiamata.equals("P")) {
							if (soggettoType.getCondiviso().getType() ==
									DatiElementoTypeCondivisoType.N_TYPE) {
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
						}

						// valore del nodo
						nominativoLegame = SemanticaUtil.getSoggettarioSBN(soggettoType.getT250());
						String edizioneSoggettario="";

						// Inizio Modifica almaviva2 25.09.2012 per riportare edizione soggettario ovunque in analitica e dettaglio
						if (soggettoType.getT250().getEdizione() != null) {
							edizioneSoggettario = " " + soggettoType.getT250().getEdizione().toString();
						}
						// Fine Modifica almaviva2 25.09.2012 per riportare edizione soggettario ovunque in analitica e dettaglio


						nominativoLegame = "<" + nominativoLegame + edizioneSoggettario + "> "
								+ SoggettiUtil.costruisciStringaSoggetto(soggettoType);

						valueDelNodo = nominativoLegame;
						// SE E' UNA CLASSE
						// Forma accettata o rinvio(INSERITA ICONA = NO)
					} else if (elementAutType.getDatiElementoAut() instanceof ClasseType) {
						ClasseType classeType = (ClasseType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(classeType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(classeType.getT005());
						nodo.setLivelloAutorita(classeType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (classeType.getLivelloAut() != null) {
							livelloAutorita = classeType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);

						if (myChiamata.equals("P")) {
							if (classeType.getCondiviso().getType() ==
									DatiElementoTypeCondivisoType.N_TYPE) {
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
						}

						// Esistono due tipi di classe:
						// 1 - Classificazione dewey (T676)
						// 2 - Classificazione diversa da dewey (T686)
						// Lo si vede subito dal TipoLegame
						if (tipoLegameString.equals("676")) {
							// Classificazione dewey
							nominativoLegame = classeType.getClasseTypeChoice().getT676().getC_676();
							if (nominativoLegame == null) {
								nominativoLegame = "";
							}
						} else {
							// Classificazione diversa da dewey
							nominativoLegame = classeType.getClasseTypeChoice().getT686().getC_686();
							if (nominativoLegame == null) {
								nominativoLegame = "";
							}
						}
						valueDelNodo = nominativoLegame;
//						 SE E' UN TERMINE THESAURO
					} else if (elementAutType.getDatiElementoAut() instanceof TermineType) {
						TermineType termineType = (TermineType) elementAutType.getDatiElementoAut();
						nodo.setKey(termineType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(termineType.getT005());
						nodo.setLivelloAutorita(termineType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (termineType.getLivelloAut() != null) {
							livelloAutorita = termineType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);

						if (myChiamata.equals("P")) {
							if (termineType.getCondiviso().getType() ==
									DatiElementoTypeCondivisoType.N_TYPE) {
								nodo.setFlagCondiviso(false);
							} else {
								nodo.setFlagCondiviso(true);
							}
						} else {
							nodo.setFlagCondiviso(true);
						}

						nominativoLegame = termineType.getT935().getC2_935().toString()
								+ " - "
								+ termineType.getT935().getA_935().toString();
						valueDelNodo = nominativoLegame;
						// SE E' UN LUOGO
					} else if (elementAutType.getDatiElementoAut() instanceof LuogoType) {
						LuogoType luogoType = (LuogoType) elementAutType
								.getDatiElementoAut();
						nodo.setKey(luogoType.getT001());
						nodo.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
						nodo.setT005(luogoType.getT005());
						nodo.setLivelloAutorita(luogoType.getLivelloAut().toString());

						// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del
						// livelloAutorita l'opportuna descrizione perchè più leggibile
						// ( potrebbe confondersi con il tipo legame - caso di valore = 51)
						// CASO DELLE AUTORITY: in questo caso si aggiunge del tutto perchè era completamente assente il livello di Autorità
						if (luogoType.getLivelloAut() != null) {
							livelloAutorita = luogoType.getLivelloAut().toString();
							// almaviva2- Aprile 2015 nelle mappe di Analitica Documento si sostituisce al valore numerico del livelloAutorita
							// l'opportuna descrizione perchè più leggibile ( potrebbe confondersi con il tipo legame - caso di valore = 51)
							TB_CODICI codici = new TB_CODICI();
							try {
								codici = CodiciProvider.cercaCodice(livelloAutorita, CodiciType.CODICE_LIVELLO_AUTORITA, CodiciRicercaType.RICERCA_CODICE_SBN);
								if (codici.getDs_cdsbn_ulteriore() != null && codici.getDs_cdsbn_ulteriore().length()>0 ) {
									livelloAutoritaDesc = codici.getDs_cdsbn_ulteriore();
								} else {
									livelloAutoritaDesc = livelloAutorita;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (myChiamata.equals("P")) {
							nodo.setLocalizzazione(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
							if (luogoType.getCondiviso() == null) {
								nodo.setFlagCondiviso(true);
							} else {
								if (luogoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
									nodo.setFlagCondiviso(false);
								} else {
									nodo.setFlagCondiviso(true);
								}
							}
						} else {
							nodo.setFlagCondiviso(true);
							nodo.setLocalizzazione(root.getLocalizzazione());
						}

						// Modifica almaviva2 06.09.2012 Intervento interno - inserito codice relazione nell'analitica
						// del legame titolo-luogo
						nominativoLegame = legameElemento.getRelatorCode() + " " + luogoType.getT260().getD_260().toString();
//						nominativoLegame = luogoType.getT260().getD_260().toString();
						valueDelNodo = nominativoLegame;
					}
				}
			}
		}

		if (!livelloAutoritaDesc.equals("")){
			valueDelNodo = livelloAutoritaDesc + " " + valueDelNodo;
		}
		return valueDelNodo;
	}


	public AreaDatiServizioInterrTitoloDomVO servizioDatiTitoloPerBid(
			String bid) {

		AreaDatiServizioInterrTitoloDomVO areaDatiServizioRet = new AreaDatiServizioInterrTitoloDomVO();
		areaDatiServizioRet.setCodErr("0000");

		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();

			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(10);

			//cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
			cercaDatiTitTypeChoice.setT001(bid);
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				areaDatiServizioRet.setCodErr("9999");
				areaDatiServizioRet.setTestoProtocollo("Server di Polo non disponibile");
				return areaDatiServizioRet;
			}
			if (!sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResult().getEsito().equals("0000")) {
				areaDatiServizioRet.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getEsito());
				areaDatiServizioRet.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito());
				return areaDatiServizioRet;
			}

			UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo(bid,	bid, sbnRisposta);
			if (sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
				DocumentoType documentoType = new DocumentoType();
				documentoType = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
				if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
					DatiDocType datiDocType = documentoType.getDocumentoTypeChoice().getDatiDocumento();
					areaDatiServizioRet.setBid(datiDocType.getT001());
					areaDatiServizioRet.setLivAut(datiDocType.getLivelloAutDoc().toString());
					areaDatiServizioRet.setNatura(datiDocType.getNaturaDoc().toString());
					areaDatiServizioRet.setTipoMateriale(datiDocType.getTipoMateriale().toString());
					areaDatiServizioRet.setTipoAuthority("");
					areaDatiServizioRet.setTitoloResponsabilita(utilityCampiTitolo.getTitoloResponsabilita(datiDocType));
					areaDatiServizioRet.setCodPaese(utilityCampiTitolo.getPaese());
					areaDatiServizioRet.setArrCodLingua(utilityCampiTitolo.getArrLingua());
					areaDatiServizioRet.setTimeStamp(datiDocType.getT005());
					return areaDatiServizioRet;
				} else if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
					TitAccessoType titAccessoType = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
					areaDatiServizioRet.setBid(titAccessoType.getT001());
					areaDatiServizioRet.setLivAut(titAccessoType.getLivelloAut().toString());
					areaDatiServizioRet.setNatura(titAccessoType.getNaturaTitAccesso().toString());
					areaDatiServizioRet.setTipoMateriale("");
					areaDatiServizioRet.setTipoAuthority("");
					C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
					areaDatiServizioRet.setTitoloResponsabilita
								(utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200 ));
					areaDatiServizioRet.setCodPaese("");
					areaDatiServizioRet.setTimeStamp(titAccessoType.getT005());
					return areaDatiServizioRet;
				}
			} else {

				areaDatiServizioRet.setCodErr("9999");
				areaDatiServizioRet.setTestoProtocollo("Dati del titolo non disponibili sul server di Polo");
				return areaDatiServizioRet;
			}

		} catch (SbnMarcException ve) {
			areaDatiServizioRet.setCodErr("9999");
			areaDatiServizioRet.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiServizioRet.setCodErr("9999");
			areaDatiServizioRet.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiServizioRet.setCodErr("9999");
			areaDatiServizioRet.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiServizioRet;
	}

	// Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
	// fra periodici sia verso l'alto che verso il basso
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLegamiFraPeriodici(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass) {

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		int totRighe = 0;
		String appoggioCodaDescr="";
		// Manuenzione almaviva2 16.03.2011 BUG MANTIS 4312 (collaudo) richiamata la ValidazioneDati.trimOrEmpty in tutte le impostazioni
		// dei campi appoggioData1Pubbl e appoggioData2Pubbl per eliminare l'errore nel caso di date pubblicazione uguali a null
		String appoggioData1Pubbl="";
		String appoggioData2Pubbl="";

		// impostazione delle aree per i titoli collegati al bid di ricerca
		TreeMap tabellaLegamiPeriodici = null;
		tabellaLegamiPeriodici = new TreeMap(gestPeriodiciComparator);

		String legameInverso="";
		String legamiDiretti="";
		String bidOrigine="";
		String myBID="";
		String myDescrTitolo= "";
		String daProspettare="";

		SBNMarc sbnRisposta = null;

		try {
			// impostazione delle aree per i titoli collegati al bid di ricerca
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			cercaType.setNumPrimo(1);
			cercaType.setMaxRighe(10);
			// risposta in formato Analitica
//			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			// risposta in formato Sintetica
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();

			List temp = new ArrayList();
			temp.add("S");

			if (temp.size() > 0) {
				String[] valoriEffettivi = (String[]) temp.toArray(new String[temp.size()]);
				cercaDatiTitType.setNaturaSbn(valoriEffettivi);
			}

			// Passo il valore "Tutti" ma con le sole nature "S" così da estrarre solo i legami fra Periodici
			ArrivoLegame arrivoLegame = new ArrivoLegame();
			LegameDocType legameDocType = new LegameDocType();
			LegameDocType legameDocType1 = new LegameDocType();
			SbnLegameDoc sbnLegameDoc = SbnLegameDoc.VALUE_0;
			legameDocType.setTipoLegame(sbnLegameDoc);
			legameDocType.setIdArrivo(areaDatiPass.getOggDiRicerca());
			arrivoLegame.setLegameDoc(legameDocType);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaTitoloType.setArrivoLegame(arrivoLegame);

			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.polo.setMessage(sbnmessage, this.user);

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
				// Non ci sono titoli collegati (quindi legami verso l'alto) si deve richiedere l'analitica per avere i legami in basso
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
				titoloAnaliticaVO.setBidRicerca(areaDatiPass.getOggDiRicerca());
				titoloAnaliticaVO.setRicercaPolo(true);
				titoloAnaliticaVO.setRicercaIndice(false);
				titoloAnaliticaVO.setInviaSoloSbnMarc(true);
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO;
				titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
				if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
					sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
					if (totRighe == 0) {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					} else {
						// Si legge la risposta dell'analitica per i soli legami a scendere
						if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
							DocumentoType documentoType = new DocumentoType();
							documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
							if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
								myBID = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
								UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo(myBID, myBID, sbnRisposta);
								myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,sbnRisposta , true);

								// Inizio Intervento almaviva2 2011.03.02 BUG MANTIS 4206 si decodifica il legame Continuazione di:
								// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
								// o in assenza data1 e, se presente data2
								appoggioCodaDescr = utilityCampiTitolo.getAreaNumerazioneCompleta(myBID, sbnRisposta);
								if (!appoggioCodaDescr.equals("")) {
									myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
								} else {
									if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100() != null) {
										appoggioData1Pubbl = ValidazioneDati.trimOrEmpty(documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_9());
										appoggioData2Pubbl = ValidazioneDati.trimOrEmpty(documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_13());
										if (!appoggioData1Pubbl.equals("")) {
											myDescrTitolo = myDescrTitolo + " (Data pubbl." + appoggioData1Pubbl + "-";
											if (!appoggioData2Pubbl.equals("")) {
												myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
											} else {
												myDescrTitolo = myDescrTitolo + ")";
											}
										}
									}
								}
								// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206

								tabellaLegamiPeriodici.put("00" + myBID, "" + "|" + myDescrTitolo);
								bidOrigine= myBID + " " + myDescrTitolo;

								LegamiType legamiType = null;
								int legamiDocumentoCount = documentoType.getLegamiDocumentoCount();
								if (legamiDocumentoCount > 0) {
									legamiType = new LegamiType();


									for (int iLegDoc = 0; iLegDoc < legamiDocumentoCount; iLegDoc++) {
										legamiType = documentoType.getLegamiDocumento(iLegDoc);

										int arrivoLegameCount = legamiType.getArrivoLegameCount();
										if (arrivoLegameCount > 0) {

											for (int iArrLeg = 0; iArrLeg < arrivoLegameCount; iArrLeg++) {
												if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc() != null) {
													if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento() != null) {
														if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString().equals("S")) {
															myBID = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
															myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,sbnRisposta , true);

															// Inizio Intervento almaviva2 2011.03.02 BUG MANTIS 4206 si decodifica il legame Continuazione di:
															// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
															// o in assenza data1 e, se presente data2
															appoggioCodaDescr = utilityCampiTitolo.getAreaNumerazioneCompleta(myBID, sbnRisposta);
															if (!appoggioCodaDescr.equals("")) {
																myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
															} else {
																if (documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100() != null) {
																	appoggioData1Pubbl = ValidazioneDati.trimOrEmpty(documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_9());
																	appoggioData2Pubbl = ValidazioneDati.trimOrEmpty(documentoType.getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_13());
																	if (!appoggioData1Pubbl.equals("")) {
																		myDescrTitolo = myDescrTitolo + " (Data pubbl." + appoggioData1Pubbl + "-";
																		if (!appoggioData2Pubbl.equals("")) {
																			myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
																		} else {
																			myDescrTitolo = myDescrTitolo + ")";
																		}
																	}
																}
															}
															// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206

															// Inizio Modifica almaviva2 15.12.2010 Intervento Interno
															int tipoLegameNumerico = 0;
															String legameDoc = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getTipoLegame().toString();
															try {
																tipoLegameNumerico = Integer.parseInt(legameDoc);
															} catch (NumberFormatException exx) {
																areaDatiPassReturn.setCodErr("9999");
																areaDatiPassReturn.setTestoProtocollo("ERROR >>" + exx.getMessage());
															}

															String codiceLegameCompleto = "S" + tipoLegameNumerico + "S";
															String descCodiceLegameCompleto = "";
															String descCodiceLegameCompletoLETT = "";
															try {
																codiceLegameCompleto = codici.cercaDescrizioneCodice(
																		codiceLegameCompleto,
																					CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																					CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);

																descCodiceLegameCompleto = codici.getDescrizioneCodice(
																		CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																		"S" + tipoLegameNumerico + "S");

																// Modifica almaviva2 02.03.2011 BUG MANTIS 4206 :per la descrizione del legame si accede
																// alla tabella LETT invece che alla LICR perchè sono presenti tutte le descrizioni dei legami da decodificare
																descCodiceLegameCompletoLETT = codici.getDescrizioneCodice(
																		CodiciType.CODICE_LEGAME_TITOLO_TITOLO,
																		codiceLegameCompleto);




																tabellaLegamiPeriodici.put(codiceLegameCompleto + myBID, descCodiceLegameCompletoLETT + "|" + myDescrTitolo);

//																legamiDiretti = legamiDiretti + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo + "<br />";
																if (legamiDiretti.length() == 0) {
																	legamiDiretti = "-->" + descCodiceLegameCompletoLETT + " " + myBID + " " + myDescrTitolo;
																} else {
																	legamiDiretti = legamiDiretti + "<br />" + "-->" + descCodiceLegameCompletoLETT + " " + myBID + " " + myDescrTitolo;
																}

															} catch (RemoteException re) {
																areaDatiPassReturn.setCodErr("9999");
																areaDatiPassReturn.setTestoProtocollo("ERROR >>" + re.getMessage());
															}
															// Fine Modifica almaviva2 15.12.2010 Intervento Interno
														}
													}
													// Inizio Modifica almaviva2 15.12.2010 Intervento Interno
													// Spostato all'interno dell'if sulla natura S altrimenti non ha senso
//													int tipoLegameNumerico = 0;
//													String legameDoc = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getTipoLegame().toString();
//													try {
//														tipoLegameNumerico = Integer.parseInt(legameDoc);
//													} catch (NumberFormatException exx) {
//														areaDatiPassReturn.setCodErr("9999");
//														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + exx.getMessage());
//													}
//
//													String codiceLegameCompleto = "S" + tipoLegameNumerico + "S";
//													String descCodiceLegameCompleto = "";
//													try {
//														codiceLegameCompleto = codici.cercaDescrizioneCodice(
//																codiceLegameCompleto,
//																			CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
//																			CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
//
//														descCodiceLegameCompleto = codici.getDescrizioneCodice(
//																CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
//																"S" + tipoLegameNumerico + "S");
//
//														tabellaLegamiPeriodici.put(codiceLegameCompleto + myBID, descCodiceLegameCompleto + "|" + myDescrTitolo);
//														legamiDiretti = legamiDiretti + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo + "<br />";
//													} catch (RemoteException re) {
//														areaDatiPassReturn.setCodErr("9999");
//														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + re.getMessage());
//													}
													// Fine Modifica almaviva2 15.12.2010 Intervento Interno
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					areaDatiPassReturn.setCodErr(titoloAnaliticaReturnVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

			} else {

				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
					UtilityCampiTitolo utilityCampiTitoloNew = new UtilityCampiTitolo();
					DocumentoType documentoType = null;
					int documentoCount = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount();

					// Estrazione delle informazioni sui Bid legati per Esamina
					for (int iDoc = 0; iDoc < documentoCount; iDoc++) {
						documentoType = new DocumentoType();
						documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(iDoc);
						DocumentoTypeChoice documentoTypeChoice = documentoType.getDocumentoTypeChoice();
						DatiDocType datiDocType = documentoTypeChoice.getDatiDocumento();
						if (datiDocType != null) {
							myBID = datiDocType.getT001();
							myDescrTitolo = utilityCampiTitoloNew.getTitoloResponsabilita(datiDocType);

							// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
							// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
							// o in assenza data1 e, se presente data2
							appoggioCodaDescr = utilityCampiTitoloNew.getAreaNumerazioneCompleta(myBID, sbnRisposta);
							if (!appoggioCodaDescr.equals("")) {
								myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
							} else {
								if (datiDocType.getT100() != null) {
									appoggioData1Pubbl = ValidazioneDati.trimOrEmpty(datiDocType.getT100().getA_100_9());
									appoggioData2Pubbl = ValidazioneDati.trimOrEmpty(datiDocType.getT100().getA_100_13());
									if (!appoggioData1Pubbl.equals("")) {
										myDescrTitolo = myDescrTitolo + " (Data pubbl." + appoggioData1Pubbl + "-";
										if (!appoggioData2Pubbl.equals("")) {
											myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
										} else {
											myDescrTitolo = myDescrTitolo + ")";
										}
									}
								}
							}
							// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206

							LegamiType legamiType = null;
							int legamiDocumentoCount1 = documentoType.getLegamiDocumentoCount();
							if (legamiDocumentoCount1 > 0) {
								for (int iLegDoc1 = 0; iLegDoc1 < legamiDocumentoCount1; iLegDoc1++) {
									legamiType = new LegamiType();
									legamiType = documentoType.getLegamiDocumento(iLegDoc1);
									int arrivoLegameCount1 = legamiType.getArrivoLegameCount();
									if (arrivoLegameCount1 > 0) {
										for (int iArrLeg1 = 0; iArrLeg1 < arrivoLegameCount1; iArrLeg1++) {
											if (legamiType.getArrivoLegame(iArrLeg1).getLegameDoc() != null
													&& legamiType.getArrivoLegame(iArrLeg1).getLegameDoc().getIdArrivo().equals(areaDatiPass.getOggDiRicerca())) {
												legameDocType1 = new LegameDocType();
												legameDocType1 = legamiType.getArrivoLegame(iArrLeg1).getLegameDoc();
												String legameDoc = legameDocType1.getTipoLegame().toString();
												// Inserito dopo .... verificare se è giusto invertirlo
												int tipoLegameNumerico = 0;
												String codiceLegameCompleto="";
												String descCodiceLegameCompleto = "";
												// BUG MANTIS collaudo 4666 vengono insertite le corrette decodifiche dei legami
												// in forma inversa inviati da G.Contardi in nota al bug
												if (legameDoc.equals("430")) {
													// Inverto il legame creandone uno fittizio al fin di darne la corretta decodifica
													codiceLegameCompleto= "40";
													descCodiceLegameCompleto = "Continua con";
												} else if (legameDoc.equals("422")) {
													codiceLegameCompleto= "20";
//													descCodiceLegameCompleto = "INVERSO DI Supplemento di";
													descCodiceLegameCompleto = "Ha per supplemento";
												} else if (legameDoc.equals("434")) {
													codiceLegameCompleto= "14";
//													descCodiceLegameCompleto = "INVERSO DI Assorbe";
													descCodiceLegameCompleto = "Assorbito da";
												} else if (legameDoc.equals("437")) {
													codiceLegameCompleto= "24";
//													descCodiceLegameCompleto = "INVERSO DI Si fonde con";
													descCodiceLegameCompleto = "Si fonde con";
												} else if (legameDoc.equals("431")) {
													codiceLegameCompleto= "34";
//													descCodiceLegameCompleto = "INVERSO DI Continuazione parziale";
													descCodiceLegameCompleto = "Si scinde in";
												} else {
													try {
														tipoLegameNumerico = Integer.parseInt(legameDoc);
													} catch (NumberFormatException exx) {
														areaDatiPassReturn.setCodErr("9999");
														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + exx.getMessage());
													}

													codiceLegameCompleto = "S" + tipoLegameNumerico + "S";

													try {
														codiceLegameCompleto = codici.cercaDescrizioneCodice(
																codiceLegameCompleto,
																			CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																			CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);

														descCodiceLegameCompleto = codici.getDescrizioneCodice(
																CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																"S" + tipoLegameNumerico + "S");

													} catch (RemoteException re) {
														areaDatiPassReturn.setCodErr("9999");
														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + re.getMessage());
													}
												}
												tabellaLegamiPeriodici.put(codiceLegameCompleto + myBID, descCodiceLegameCompleto + "|" + myDescrTitolo);
//												legameInverso = legameInverso + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo + "<br />";
												if (legameInverso.length() == 0) {
													legameInverso = "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
												} else {
													legameInverso = legameInverso + "<br />" + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
												}



												// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 in questo messaggio sono assenti
												// le informazioni su l'area 3,e dati;
												// viene asteriscata e le informazioni si prendono nell'interrogazione diretta fatta sotto
//												if (bidOrigine.equals("")) {
//													if (legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento() != null) {
//														myBID = legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
//														myDescrTitolo = utilityCampiTitoloNew.getTitoloResponsabilita(legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento());
//
//														// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
//														// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
//														// o in assenza data1 e, se presente data2
//														appoggioCodaDescr = utilityCampiTitoloNew.getAreaPubblicazione(legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento());
//														if (!appoggioCodaDescr.equals("")) {
//															myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
//														} else {
//															if (legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT100() != null) {
//																appoggioData1Pubbl = legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_9().toString();
//																appoggioData2Pubbl = legameDocType1.getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT100().getA_100_13().toString();
//																if (!appoggioData1Pubbl.equals("")) {
//																	myDescrTitolo = myDescrTitolo + " (Data pubbl." + appoggioData1Pubbl + "-";
//																	if (!appoggioData2Pubbl.equals("")) {
//																		myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
//																	} else {
//																		myDescrTitolo = myDescrTitolo + ")";
//																	}
//																}
//															}
//														}
//														// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206
//
//
//
//
//														tabellaLegamiPeriodici.put("00" + myBID, "" + "|" + myDescrTitolo);
//														bidOrigine= myBID + " " + myDescrTitolo;
//													}
//												}
											}
										}
									}
								}
							}
						 }
					}
				}

				// Si deve richiedere l'analitica per avere i legami in basso
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
				titoloAnaliticaVO.setBidRicerca(areaDatiPass.getOggDiRicerca());
				titoloAnaliticaVO.setRicercaPolo(true);
				titoloAnaliticaVO.setRicercaIndice(false);
				titoloAnaliticaVO.setInviaSoloSbnMarc(true);
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO;
				titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
				if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
					sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
					if (totRighe == 0) {
						areaDatiPassReturn.setNumNotizie(0);
						return areaDatiPassReturn;
					} else {
						// Si legge la risposta dell'analitica per i soli legami a scendere
						if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
							DocumentoType documentoType = new DocumentoType();
							documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
							if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
								if (bidOrigine.equals("")) {
									myBID = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
									UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo(myBID, myBID, sbnRisposta);
									myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,sbnRisposta , true);

									// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
									// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
									// o in assenza data1 e, se presente data2
									appoggioCodaDescr = utilityCampiTitolo.getAreaNumerazioneCompleta(myBID,sbnRisposta);
									if (!appoggioCodaDescr.equals("")) {
										myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
									} else {
										appoggioData1Pubbl = ValidazioneDati.trimOrEmpty(utilityCampiTitolo.getData1());
										appoggioData2Pubbl = ValidazioneDati.trimOrEmpty(utilityCampiTitolo.getData2());
										if (!appoggioData1Pubbl.equals("")) {
											myDescrTitolo = myDescrTitolo + " (Data pubbl. " + appoggioData1Pubbl + "-";
											if (!appoggioData2Pubbl.equals("")) {
												myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
											} else {
												myDescrTitolo = myDescrTitolo + ")";
											}
										}
									}
									// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206

									tabellaLegamiPeriodici.put("00" + myBID, "" + "|" + myDescrTitolo);
									bidOrigine= myBID + " " + myDescrTitolo;
								}

								LegamiType legamiType = null;
								int legamiDocumentoCount = documentoType.getLegamiDocumentoCount();
								if (legamiDocumentoCount > 0) {
									legamiType = new LegamiType();


									for (int iLegDoc = 0; iLegDoc < legamiDocumentoCount; iLegDoc++) {
										legamiType = documentoType.getLegamiDocumento(iLegDoc);

										int arrivoLegameCount = legamiType.getArrivoLegameCount();
										if (arrivoLegameCount > 0) {

											for (int iArrLeg = 0; iArrLeg < arrivoLegameCount; iArrLeg++) {
												if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc() != null) {
													if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento() != null) {
														if (legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString().equals("S")) {
															myBID = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getDocumentoLegato().getDocumentoTypeChoice().getDatiDocumento().getT001().toString();
															UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo(myBID, myBID, sbnRisposta);
															myDescrTitolo = utilityCampiTitolo.getAreaTitoloCompleto(myBID,sbnRisposta , true);

															// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
															// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
															// o in assenza data1 e, se presente data2
															appoggioCodaDescr = utilityCampiTitolo.getAreaNumerazioneCompleta(myBID,sbnRisposta);
															if (!appoggioCodaDescr.equals("")) {
																myDescrTitolo = myDescrTitolo + ". - " + appoggioCodaDescr;
															} else {
																appoggioData1Pubbl = ValidazioneDati.trimOrEmpty(utilityCampiTitolo.getData1());
																appoggioData2Pubbl = ValidazioneDati.trimOrEmpty(utilityCampiTitolo.getData2());
																if (!appoggioData1Pubbl.equals("")) {
																	myDescrTitolo = myDescrTitolo + " (Data pubbl. " + appoggioData1Pubbl + "-";
																	if (!appoggioData2Pubbl.equals("")) {
																		myDescrTitolo = myDescrTitolo + appoggioData2Pubbl + ")";
																	} else {
																		myDescrTitolo = myDescrTitolo + ")";
																	}
																}
															}
															// Fine Intervento almaviva2 2011.02.11 BUG MANTIS 4206

															// Inizio Intervento almaviva2 21.05.2012 BUG MANTIS LIGURIA 0024 esercizio
															// Spostata decodifica dei legami perchè siamo interessati solo alle nature "S"
															int tipoLegameNumerico = 0;
															String legameDoc = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getTipoLegame().toString();
															try {
																tipoLegameNumerico = Integer.parseInt(legameDoc);
															} catch (NumberFormatException exx) {
																areaDatiPassReturn.setCodErr("9999");
																areaDatiPassReturn.setTestoProtocollo("ERROR >>" + exx.getMessage());
															}
															String codiceLegameCompleto = "S" + tipoLegameNumerico + "S";
															String descCodiceLegameCompleto = "";
															try {
																codiceLegameCompleto = codici.cercaDescrizioneCodice(
																		codiceLegameCompleto,
																					CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																					CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);

																descCodiceLegameCompleto = codici.getDescrizioneCodice(
																		CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
																		"S" + tipoLegameNumerico + "S");

																// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
																// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
																// o in assenza data1 e, se presente data2
																if (descCodiceLegameCompleto.equals("")) {
																	if (("S" + tipoLegameNumerico + "S").equals("S430S")) {
																		descCodiceLegameCompleto = "Continuazione di";
																	}
																}

																tabellaLegamiPeriodici.put(codiceLegameCompleto + myBID, descCodiceLegameCompleto + "|" + myDescrTitolo);

//																legamiDiretti = legamiDiretti + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo + "<br />";
																if (legamiDiretti.length() == 0) {
																	legamiDiretti = "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
																} else {
																	legamiDiretti = legamiDiretti + "<br />" + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
																}

															} catch (RemoteException re) {
																areaDatiPassReturn.setCodErr("9999");
																areaDatiPassReturn.setTestoProtocollo("ERROR >>" + re.getMessage());
															}
															// Fine Intervento almaviva2 21.05.2012 BUG MANTIS LIGURIA 0024 esercizio

														} else {
															// natura diversa da "S" ????????????
														}
													}
//													int tipoLegameNumerico = 0;
//													String legameDoc = legamiType.getArrivoLegame(iArrLeg).getLegameDoc().getTipoLegame().toString();
//													try {
//														tipoLegameNumerico = Integer.parseInt(legameDoc);
//													} catch (NumberFormatException exx) {
//														areaDatiPassReturn.setCodErr("9999");
//														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + exx.getMessage());
//													}

//													String codiceLegameCompleto = "S" + tipoLegameNumerico + "S";
//													String descCodiceLegameCompleto = "";
//													try {
//														codiceLegameCompleto = codici.cercaDescrizioneCodice(
//																codiceLegameCompleto,
//																			CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
//																			CodiciRicercaType.CODICE_LEGAME_TITOLO_TITOLO);
//
//														descCodiceLegameCompleto = codici.getDescrizioneCodice(
//																CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
//																"S" + tipoLegameNumerico + "S");
//
//														// Inizio Intervento almaviva2 2011.02.11 BUG MANTIS 4206 si decodifica il legame Continuazione di:
//														// che essendo un legame inverso non è presente nella tabella; inoltre viene prospettata l'area 3,
//														// o in assenza data1 e, se presente data2
//														if (descCodiceLegameCompleto.equals("")) {
//															if (("S" + tipoLegameNumerico + "S").equals("S430S")) {
//																descCodiceLegameCompleto = "Continuazione di";
//															}
//														}
//
//														tabellaLegamiPeriodici.put(codiceLegameCompleto + myBID, descCodiceLegameCompleto + "|" + myDescrTitolo);
//
////														legamiDiretti = legamiDiretti + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo + "<br />";
//														if (legamiDiretti.length() == 0) {
//															legamiDiretti = "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
//														} else {
//															legamiDiretti = legamiDiretti + "<br />" + "-->" + descCodiceLegameCompleto + " " + myBID + " " + myDescrTitolo;
//														}
//
//													} catch (RemoteException re) {
//														areaDatiPassReturn.setCodErr("9999");
//														areaDatiPassReturn.setTestoProtocollo("ERROR >>" + re.getMessage());
//													}
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					areaDatiPassReturn.setCodErr(titoloAnaliticaReturnVO.getCodErr());
					areaDatiPassReturn.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
					return areaDatiPassReturn;
				}

			}
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
		if (legameInverso.equals("")) {
			daProspettare = bidOrigine + "<br />" + legamiDiretti;
		} else {
			daProspettare = bidOrigine + "<br />" + legameInverso + "<br />" + legamiDiretti;
		}

		areaDatiPassReturn.setStringDaProsp(daProspettare);
		areaDatiPassReturn.setTreeLegamiPeriodici(tabellaLegamiPeriodici);
		return areaDatiPassReturn;
	}

	private Comparator gestPeriodiciComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((String) o1).compareTo((String) o2);
		}
	};



	// Area dei metodi relativi all'accesso alle tabelle per le liste di confronto


	public AreaDatiPassaggioElenchiListeConfrontoVO getElenchiListeConfronto(
			AreaDatiPassaggioElenchiListeConfrontoVO areaDatiPass) {


		DaoManager daoManager = new DaoManager();

		try {

			Session session = daoManager.getCurrentSession();
			Criteria c = session.createCriteria(Tb_report_indice.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.asc("id"));

			List<Tb_report_indice> list = c.list();
			for (Tb_report_indice ri : list) {
				areaDatiPass.addListaNomeLista(ri.getId() + " - " + ri.getNome_lista());
				areaDatiPass.addListaDataLista(ri.getData_prod_lista() + " - " + ri.getNome_lista());
				areaDatiPass.addListaIdLista(ri.getId());
			}

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}

		return areaDatiPass;
	}


	// almaviva2 - gestioneBibliografica evolutiva x liste confronto Autori 04.05.2012
	// Metodo implementato per la creazione di una sintetica autori arrichita dai primi 10 titoli collegati

	public AreaDatiPassaggioSchedaDocCiclicaVO getSchedaDocCiclica(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass) {

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setMessaggisticaDiLavorazione("");
		String bidAttuale = "";

		AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPassaggioInterrogazioneAutoreVO = new AreaDatiPassaggioInterrogazioneAutoreVO();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassaggioInterrogazioneTitoloReturnVO = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		SbnGestioneAutoriDao sbnGestioneAutoriDao = new SbnGestioneAutoriDao(indice, polo, user);
		SinteticaAutoriView sinteticaAutoriView = new SinteticaAutoriView();

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();
		SbnGestioneTitoliDao sbnGestioneTitoliDao = new SbnGestioneTitoliDao(indice, polo, user);
		SbnStampaSchedeDao sbnStampaSchedeDao = new SbnStampaSchedeDao(indice, new FactorySbnEJB("polo"), user);
		SinteticaTitoliView sinteticaTitoliView = new SinteticaTitoliView();
		TracciatoStampaSchedeVO schedaVO;

		if (areaDatiPass.isAggiornamento()) {
			areaDatiPass = aggiornaTbReportIndiceIdLocali(areaDatiPass);
			if (!areaDatiPass.getCodErr().equals("0000")) {
				return areaDatiPass;
			}
		}

		areaDatiPass.newListaIdDocArrFusione();
		areaDatiPass.newListaSchedaIdLocale();
		areaDatiPass.newListaSinteticaSchede();

		areaDatiPass = getPrimoDocDaElab(areaDatiPass);
		if (!areaDatiPass.getCodErr().equals("0000")) {
			return areaDatiPass;
		}


		if (areaDatiPass.getBidDocLocale().substring(3,4).toString().equals("V")) {
			areaDatiPass.setAutore(true);
			areaDatiPass.setTitolo(false);
		} else {
			areaDatiPass.setAutore(false);
			areaDatiPass.setTitolo(true);
		}

		if (areaDatiPass.isAutore()) {

			if (areaDatiPass.getBidDocLocale() != null) {
				// Interrogazione del Bid sulla base dati Locale per le informazioni di Gestione bibliografica
				bidAttuale = areaDatiPass.getBidDocLocale();

				areaDatiPassaggioInterrogazioneAutoreVO.getInterrGener().setVid(bidAttuale);
				areaDatiPassaggioInterrogazioneAutoreVO.setRicercaIndice(false);
				areaDatiPassaggioInterrogazioneAutoreVO.setRicercaPolo(true);

				areaDatiPassaggioInterrogazioneTitoloReturnVO = sbnGestioneAutoriDao.ricercaAutoriPerSintArricchita(areaDatiPassaggioInterrogazioneAutoreVO);

				if (areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("")
						|| areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("0000")) {

					sinteticaAutoriView = (SinteticaAutoriView) areaDatiPassaggioInterrogazioneTitoloReturnVO.getListaSintetica().get(0);

					sinteticaTitoliView = new SinteticaTitoliView();
					sinteticaTitoliView.setProgressivo(sinteticaAutoriView.getProgressivo());
					sinteticaTitoliView.setBid(sinteticaAutoriView.getVid());
					sinteticaTitoliView.setTipoAutority("AU");
					sinteticaTitoliView.setNatura("");
					sinteticaTitoliView.setTipoMateriale("");
					sinteticaTitoliView.setFlagCondiviso(sinteticaAutoriView.isFlagCondiviso());
					sinteticaTitoliView.setImageUrl(sinteticaAutoriView.getImageUrl());
					
					// MANTIS 7172 almaviva2 - ottobre 2019 Nelle sintetiche delle Liste di confronto le parentesi uncinate relative
					// alle qualificazioni dell’autore non vengono visualizzate correttamente
					// viene utilizzato il campo getOriginal_nome al posto di getNome
					// sinteticaTitoliView.setTipoRecDescrizioneLegami(sinteticaAutoriView.getNome());
					sinteticaTitoliView.setTipoRecDescrizioneLegami(sinteticaAutoriView.getOriginal_nome());
					// Fine Mantis 7172
					areaDatiPass.addListaSchedaIdLocale(sinteticaTitoliView);
					if (areaDatiPass.getStatoLavorRecord().equals("4") && sinteticaTitoliView.isFlagCondiviso()) {
						areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: Lo stato di lavorazione dell'autore(TRATTATO) è incongruente con lo stato di LOCALE dell'autore stesso");
					}
				} else {
					if (areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("3001")) {
						sinteticaTitoliView = new SinteticaTitoliView();
						sinteticaTitoliView.setProgressivo(0);
						sinteticaTitoliView.setBid(bidAttuale);
						if (areaDatiPass.getStatoLavorRecord().equals("0")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale);
							areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: L'autore "
									+ bidAttuale +" è assente dalla Base Dati di Polo con stato di lavorazione invalido"
									+ areaDatiPass.getStatoLavorRecord() + " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else if (areaDatiPass.getStatoLavorRecord().equals("2")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale);
							areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: L'autore "
									+ bidAttuale +" è assente dalla Base Dati di Polo con stato di lavorazione incoerente"
									+ " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else if (areaDatiPass.getStatoLavorRecord().equals("4")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale + ": "
									+ "FUSO sull'Autore di Indice "
									+ areaDatiPass.getIdArrivoFusione()
									+ " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale + ": " + "ERRORE >>" + "L'AUTORE LOCALE E' ASSENTE DALLA BASE DATI DI POLO - PASSARE AL SUCCESSIVO ELEMENTO");
						}

						areaDatiPass.addListaSchedaIdLocale(sinteticaTitoliView);
						areaDatiPass.addListaSinteticaSchede(new SinteticaTitoliView());
						return areaDatiPass;
					}
				}
			}

		} else if (areaDatiPass.isTitolo()) {

			if (areaDatiPass.getBidDocLocale() != null) {
				// Interrogazione del Bid sulla base dati Locale per le informazioni di Gestione bibliografica
				bidAttuale = areaDatiPass.getBidDocLocale();
				titoloAnaliticaVO.setBidRicerca(bidAttuale);
				titoloAnaliticaVO.setRicercaIndice(false);
				titoloAnaliticaVO.setRicercaPolo(true);
				titoloAnaliticaVO.setInviaSoloSbnMarc(true);

				titoloAnaliticaReturnVO = sbnGestioneTitoliDao.creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
				if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
					schedaVO = new TracciatoStampaSchedeVO("");
					schedaVO = sbnStampaSchedeDao.datiBibliograficiScheda(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE,
							bidAttuale,
							schedaVO,
							titoloAnaliticaReturnVO.getSbnMarcType(),
							areaDatiPass.getTicket(),
							true,
							null,
							null);
					schedaVO.setBidEsaminato(bidAttuale);
					sinteticaTitoliView = schedaVO.getStringaForListaSchede();
					sinteticaTitoliView.setProgressivo(0);
					areaDatiPass.addListaSchedaIdLocale(sinteticaTitoliView);
					if (areaDatiPass.getStatoLavorRecord().equals("4") && schedaVO.getOggettoCondiviso().equals("[Loc]")) {
						areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: Lo stato di lavorazione del documento(TRATTATO) è incongruente con lo stato di LOCALE del documento");
					}
				} else {
					if (titoloAnaliticaReturnVO.getCodErr().equals("3001")) {
						schedaVO = new TracciatoStampaSchedeVO("");
						schedaVO.setBidEsaminato(bidAttuale);
						sinteticaTitoliView = new SinteticaTitoliView();
						sinteticaTitoliView.setProgressivo(0);
						sinteticaTitoliView.setBid(bidAttuale);
						if (areaDatiPass.getStatoLavorRecord().equals("0")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale);
							areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: Il documento "
									+ bidAttuale +" è assente dalla Base Dati di Polo con stato di lavorazione invalido"
									+ areaDatiPass.getStatoLavorRecord() + " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else if (areaDatiPass.getStatoLavorRecord().equals("2")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale);
							areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: Il documento "
									+ bidAttuale +" è assente dalla Base Dati di Polo con stato di lavorazione incoerente"
									+ " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else if (areaDatiPass.getStatoLavorRecord().equals("4")) {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale + ": "
									+ "FUSO sul Documento di Indice "
									+ areaDatiPass.getIdArrivoFusione()
									+ " - PASSARE AL SUCCESSIVO ELEMENTO");
						} else {
							sinteticaTitoliView.setTipoRecDescrizioneLegamiSenzaKEY(bidAttuale + ": " + "ERRORE >>" + "IL DOCUMENTO LOCALE E' ASSENTE DALLA BASE DATI DI POLO - PASSARE AL SUCCESSIVO ELEMENTO");
						}

						areaDatiPass.addListaSchedaIdLocale(sinteticaTitoliView);
						areaDatiPass.addListaSinteticaSchede(new SinteticaTitoliView());
						return areaDatiPass;
					}
				}
			}
		}

		if (areaDatiPass.getStatoConfronto().equals("N")) {
			areaDatiPass.addListaSinteticaSchede(new SinteticaTitoliView());
			return areaDatiPass;
		}

		int sizeListaIdDoc = areaDatiPass.getListaIdDocArrFusione().size();

		if (areaDatiPass.getIdArrivoFusione() != null) {
			areaDatiPass.setMessaggisticaDiLavorazione("Attenzione: L'oggetto "
					+ bidAttuale +" è in attesa di fusione con l'oggetto "+ areaDatiPass.getIdArrivoFusione()
					+". Per modificare l'arrivo di fusione selezionare un altro oggetto e premere nuovamente il tasto Salva coppia oggetti"
					+ " - PASSARE AL SUCCESSIVO ELEMENTO");
		}


		int iProgrLista = 0;


		// almaviva2 22.05.2012 Modifica nella gestione della ricerca degli oggetti arrivo fusione in Indice per cui in assenza
		// dell'oggetto stesso (getCodErr().equals("3001")) si inserisce una riga con l'identificativo cercato e la descrizione
		// contenente la messaggistica opportuna;

		if (areaDatiPass.isAutore()) {
			int appoProgr=0;
			for (int iListaIdDoc = 0; iListaIdDoc < sizeListaIdDoc; iListaIdDoc++) {
				appoProgr++;
				// Interrogazione dei Bid sulla base dati per le informazioni di Gestione bibliografica
				bidAttuale = areaDatiPass.getListaIdDocArrFusione().get(iListaIdDoc);
				areaDatiPassaggioInterrogazioneAutoreVO.getInterrGener().setVid(bidAttuale);
				areaDatiPassaggioInterrogazioneAutoreVO.setRicercaIndice(true);
				areaDatiPassaggioInterrogazioneAutoreVO.setRicercaPolo(false);

				areaDatiPassaggioInterrogazioneTitoloReturnVO = sbnGestioneAutoriDao.ricercaAutoriPerSintArricchita(areaDatiPassaggioInterrogazioneAutoreVO);
				// Modifica del 27.11.2012 Intervento Interno richiesto da Renoato durante test di RMR
				// Inserito controllo per troppi titoli legati ad Autore che mandava in non trovato l'intera ricerca sulle liste;
				if (areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("9999")) {
					sinteticaTitoliView = new SinteticaTitoliView();
					sinteticaTitoliView.setProgressivo(0);
					sinteticaTitoliView.setBid(bidAttuale);
					sinteticaTitoliView.setTipoAutority("AU");
					sinteticaTitoliView.setNatura("");
					sinteticaTitoliView.setTipoMateriale("");
					sinteticaTitoliView.setFlagCondiviso(true);
					sinteticaTitoliView.setImageUrl(null);
					sinteticaTitoliView.setTipoRecDescrizioneLegami(bidAttuale
							+ ": ATTENZIONE:L'oggetto arrivo di Fusione ha troppi titoli collegati - "
							+ "verificare tramite la funzione di titoli collegati con filtro sulla Base Dati di Indice");
					areaDatiPass.addListaSinteticaSchede(sinteticaTitoliView);
				} else if (areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("3001")) {
					sinteticaTitoliView = new SinteticaTitoliView();
					sinteticaTitoliView.setProgressivo(0);
					sinteticaTitoliView.setBid(bidAttuale);
					sinteticaTitoliView.setTipoAutority("AU");
					sinteticaTitoliView.setNatura("");
					sinteticaTitoliView.setTipoMateriale("");
					sinteticaTitoliView.setFlagCondiviso(true);
					sinteticaTitoliView.setImageUrl(null);
					sinteticaTitoliView.setTipoRecDescrizioneLegami(bidAttuale + ": ATTENZIONE:non esiste alcun oggetto condiviso sulla Base Dati di Indice");
					areaDatiPass.addListaSinteticaSchede(sinteticaTitoliView);
				} else if (areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("")
						|| areaDatiPassaggioInterrogazioneTitoloReturnVO.getCodErr().equals("0000")) {
					sinteticaAutoriView = (SinteticaAutoriView) areaDatiPassaggioInterrogazioneTitoloReturnVO.getListaSintetica().get(0);
					sinteticaTitoliView = new SinteticaTitoliView();
//					sinteticaTitoliView.setProgressivo(sinteticaAutoriView.getProgressivo());
					sinteticaTitoliView.setProgressivo(appoProgr);
					sinteticaTitoliView.setBid(sinteticaAutoriView.getVid());
					sinteticaTitoliView.setTipoAutority("AU");
					sinteticaTitoliView.setNatura("");
					sinteticaTitoliView.setTipoMateriale("");
					sinteticaTitoliView.setFlagCondiviso(sinteticaAutoriView.isFlagCondiviso());
					sinteticaTitoliView.setImageUrl(sinteticaAutoriView.getImageUrl());
					sinteticaTitoliView.setTipoRecDescrizioneLegami(sinteticaAutoriView.getOriginal_nome());
					areaDatiPass.addListaSinteticaSchede(sinteticaTitoliView);
				}
			}
		} else if (areaDatiPass.isTitolo()) {
			for (int iListaIdDoc = 0; iListaIdDoc < sizeListaIdDoc; iListaIdDoc++) {

				// Interrogazione dei Bid sulla base dati per le informazioni di Gestione bibliografica
				bidAttuale = areaDatiPass.getListaIdDocArrFusione().get(iListaIdDoc);
				titoloAnaliticaVO.setBidRicerca(bidAttuale);
				titoloAnaliticaVO.setRicercaIndice(true);
				titoloAnaliticaVO.setRicercaPolo(false);
				titoloAnaliticaVO.setInviaSoloSbnMarc(true);

				titoloAnaliticaReturnVO = sbnGestioneTitoliDao.creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
				if (titoloAnaliticaReturnVO.getCodErr().equals("3001")) {
					sinteticaTitoliView = new SinteticaTitoliView();
					sinteticaTitoliView.setProgressivo(0);
					sinteticaTitoliView.setBid(bidAttuale);
					sinteticaTitoliView.setNatura("");
					sinteticaTitoliView.setTipoMateriale("");
					sinteticaTitoliView.setFlagCondiviso(true);
					sinteticaTitoliView.setImageUrl(null);
					sinteticaTitoliView.setTipoRecDescrizioneLegami(bidAttuale + ": ATTENZIONE:non esiste alcun oggetto condiviso sulla Base Dati di Indice");
					areaDatiPass.addListaSinteticaSchede(sinteticaTitoliView);
				} else if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
					schedaVO = new TracciatoStampaSchedeVO("");
					schedaVO = sbnStampaSchedeDao.datiBibliograficiScheda(CodiciAttivita.getIstance().GDF_STAMPA_SCHEDE_CATALOGRAFICHE,
							bidAttuale,
							schedaVO,
							titoloAnaliticaReturnVO.getSbnMarcType(),
							areaDatiPass.getTicket(),
							true,
							null,
							null);
					schedaVO.setBidEsaminato(bidAttuale);
					iProgrLista++;
					sinteticaTitoliView = schedaVO.getStringaForListaSchede();
					sinteticaTitoliView.setProgressivo(iProgrLista);
					areaDatiPass.addListaSinteticaSchede(sinteticaTitoliView);
				}
			}
		}

//		if (areaDatiPass.getListaSinteticaSchede() == null) {
//			areaDatiPass.setCodErr("3001");
//			areaDatiPass.setTestoProtocollo("ERROR >>" + "non esiste alcun oggetto condiviso sulla Base Dati di Indice");
//			return areaDatiPass;
//		}
		return areaDatiPass;
	}


	public AreaDatiPassaggioSchedaDocCiclicaVO getPrimoDocDaElab(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass) {


		DaoManager daoManager = new DaoManager();

		try {

			Session session = daoManager.getCurrentSession();
			Criteria cLoc = session.createCriteria(Tb_report_indice_id_locali.class);
			cLoc.add(Restrictions.ne("fl_canc", 'S'));

			if (areaDatiPass.getIdListaSelez() > 0) {
				cLoc.add(Restrictions.eq("id_lista.id", areaDatiPass.getIdListaSelez()));
			}

			if (areaDatiPass.getStatoLavorRecordSelez().equals("")) {
				cLoc.add(Restrictions.ne("stato_lavorazione", "3"));
			} else {
				if (areaDatiPass.getStatoLavorRecordSelez().equals("5")) {
					cLoc.add(Restrictions.or(Restrictions.eq("stato_lavorazione", "1"),	Restrictions.eq("stato_lavorazione", "2")));
				} else {
					cLoc.add(Restrictions.eq("stato_lavorazione", areaDatiPass.getStatoLavorRecordSelez()));
				}
			}


			if (areaDatiPass.successivo && areaDatiPass.getIdDocLoc() > 0) {
				cLoc.add(Restrictions.gt("id", areaDatiPass.getIdDocLoc()));
			}

			cLoc.addOrder(Order.asc("id"));
			List<Tb_report_indice_id_locali> listLocali = cLoc.list();
			if (listLocali.isEmpty()) {

				areaDatiPass.setCodErr("3001");
				areaDatiPass.setTestoProtocollo("La ricerca non ha prodotto risultati oppure non ci sono successivi elementi da visualizzare");
				return areaDatiPass;
			}

			for (Tb_report_indice_id_locali riLoc : listLocali) {
				areaDatiPass.setBidDocLocale(riLoc.getId_oggetto_locale());
				areaDatiPass.setIdDocLoc(riLoc.getId());
				areaDatiPass.setIdArrivoFusione(riLoc.getId_arrivo_fusione());
				if (riLoc.getStato_lavorazione() == null) {
					areaDatiPass.setStatoLavorRecord("1");
				} else {
					areaDatiPass.setStatoLavorRecord(riLoc.getStato_lavorazione().toString());
				}
				areaDatiPass.setStatoConfronto(riLoc.getRisultato_confronto().toString());

				if (riLoc.getRisultato_confronto().toString().equals("N")) {
					areaDatiPass.setListaIdDocArrFusione(null);
					return areaDatiPass;
				} else {
					Criteria cArr = session.createCriteria(Tb_report_indice_id_arrivo.class);
					cArr.add(Restrictions.ne("fl_canc", 'S'));
					cArr.add(Restrictions.eq("id_lista_ogg_loc.id", riLoc.getId()));

					cArr.addOrder(Order.asc("id"));
					List<Tb_report_indice_id_arrivo> listArrivo = cArr.list();
					for (Tb_report_indice_id_arrivo riArr : listArrivo) {
						areaDatiPass.addListaIdDocArrFusione(riArr.getId_arrivo_fusione());
					}
				}
				break;
			}

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}

		return areaDatiPass;
	}


	public AreaDatiPassaggioSchedaDocCiclicaVO getListaCoppieFusioneMassiva(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass) {


		DaoManager daoManager = new DaoManager();

		try {

			Session session = daoManager.getCurrentSession();
			Criteria cLoc = session.createCriteria(Tb_report_indice_id_locali.class);
			cLoc.add(Restrictions.ne("fl_canc", 'S'));

			if (areaDatiPass.getIdListaSelez() > 0) {
				cLoc.add(Restrictions.eq("id_lista.id", areaDatiPass.getIdListaSelez()));
			}
			if (!areaDatiPass.getStatoLavorRecordSelez().equals("")) {
				cLoc.add(Restrictions.eq("stato_lavorazione", areaDatiPass.getStatoLavorRecordSelez()));
			}

			cLoc.addOrder(Order.asc("id"));
			List<Tb_report_indice_id_locali> listLocali = cLoc.list();
			if (listLocali.isEmpty()) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("La ricerca non ha prodotto risultati oppure non ci sono successivi elementi da visualizzare");
				return areaDatiPass;
			}

			for (Tb_report_indice_id_locali riLoc : listLocali) {
				areaDatiPass.addListaCoppieBidDaFondere(riLoc.getId_oggetto_locale() + "|" + riLoc.getId_arrivo_fusione() + "|" + riLoc.getId());
			}

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}

		return areaDatiPass;
	}


	public AreaDatiPassaggioSchedaDocCiclicaVO aggiornaTbReportIndiceIdLocali(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass)  {

		DaoManager daoManager = new DaoManager();

		try {
			Session session = daoManager.getCurrentSession();
			Criteria cLoc = session.createCriteria(Tb_report_indice_id_locali.class);
			cLoc.add(Restrictions.eq("id_lista.id", areaDatiPass.getIdListaSelez()));
			cLoc.add(Restrictions.eq("id_oggetto_locale", areaDatiPass.getBidDocLocale()));
			cLoc.add(Restrictions.ne("fl_canc", 'S'));
			List<Tb_report_indice_id_locali> listLocali = cLoc.list();

			if (listLocali.isEmpty()) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + "La funzione di aggiornamento non funziona!!!!");
				return areaDatiPass;
			}
			for (Tb_report_indice_id_locali riLoc : listLocali) {
				riLoc.setStato_lavorazione(areaDatiPass.getStatoLavorRecordNew().charAt(0));
				if (areaDatiPass.getIdArrivoFusione().length() > 0) {
					riLoc.setId_arrivo_fusione(areaDatiPass.getIdArrivoFusione());
				}
				if (areaDatiPass.getTipoTrattamento().length() > 0) {
					riLoc.setTipo_trattamento_selezionato(areaDatiPass.getTipoTrattamento().charAt(0));
				}
				riLoc.setUte_var(this.user.getUserId());
				session.save(riLoc);
			}
			session.flush();

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}

		return areaDatiPass;
	}



	// Area dei metodi relativi all'inserimento alle tabelle per le liste di confronto

	public AreaDatiPassaggioSchedaDocCiclicaVO insertTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass)  {

		DaoManager daoManager = new DaoManager();
		String appoggioCoppia = "";


		try {
			Session session = daoManager.getCurrentSession();

			int sizeListaCoppie = areaDatiPass.getListaCoppieEsitoConfronto().size();
			if (sizeListaCoppie < 1) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >>" + " Non ci sono occorrenze da inserire sulle tabelle relative alle Liste di confronto");
			}

			Tb_report_indice riListe = new Tb_report_indice();
			riListe.setNome_lista(areaDatiPass.getNomeListaSelez());
			riListe.setFl_canc('N');
			riListe.setUte_ins("IMPORT");
			riListe.setTs_ins(new java.sql.Timestamp(System.currentTimeMillis()));
			riListe.setUte_var("IMPORT");
			riListe.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));

			Timestamp aa = new java.sql.Timestamp(System.currentTimeMillis());
			riListe.setData_prod_lista(aa.toString().replace("-", "/").substring(0, 10));

			session.save(riListe);
			session.flush();

			Tb_report_indice_id_locali riListeLoc;
			Tb_report_indice_id_arrivo riListeArr;

			// Tipo tracciato di appoggio coppia U|ANA0012003|RMR0110348

			String appoggioCoppiaInterno = "";
			String appoggioCoppiaSuccessiva = "";
			String appoggioTipo = "";
			String appoggioIdPolo = "";
			String appoggioIdPoloInterno = "";
			String appoggioIdIndice = "";
			String appoggioIdIndiceInterno = "";
			boolean trovataFusioneAutomatica = false;

			for (int i = 0; i < sizeListaCoppie; i++) {

				appoggioCoppia = (String) areaDatiPass.getListaCoppieEsitoConfronto().get(i);
				appoggioTipo = appoggioCoppia.substring(0,1);
				appoggioIdPolo = appoggioCoppia.substring(2,12);
				if (appoggioTipo.equals("N")) {
					appoggioIdIndice = "";
				} else {
					appoggioIdIndice = appoggioCoppia.substring(13,23);
				}


				// Manutenzione 11.01.2013 segnalazione di  almaviva;
				// nel caso di ultima occorrenza uguale ad U non veniva controllato che il valore di i+1 non
				// sfondasse il totale occorrenze. Problema corretto.
				if (areaDatiPass.isCaricaConFusioneAutomatica() && appoggioTipo.toString().equals("U")) {
					if ((i + 1) == sizeListaCoppie) {
						trovataFusioneAutomatica = true;
					} else {
						appoggioCoppiaSuccessiva = (String) areaDatiPass.getListaCoppieEsitoConfronto().get(i + 1);
						if (appoggioIdPolo.equals(appoggioCoppiaSuccessiva.substring(2,12))) {
							trovataFusioneAutomatica = false;
						} else {
							trovataFusioneAutomatica = true;
						}
					}
				} else {
					// BUG MANTIS (Collaudo) 5183: deve essere reimposto il flag per la fusione automatica quando il
					// tipo esito del confronto è diverso da U (Uguaglianza)
					trovataFusioneAutomatica = false;
				}

				if (trovataFusioneAutomatica) {

					// SIAMO NEL CASO IN CUI  SI PUO' FARE LA FUSIONE AUTOMATICA
					riListeLoc = new Tb_report_indice_id_locali();
					riListeLoc.setId_lista(riListe);
					riListeLoc.setId_oggetto_locale(appoggioIdPolo);
					riListeLoc.setRisultato_confronto(appoggioTipo.charAt(0));
					riListeLoc.setStato_lavorazione('2');
					riListeLoc.setTipo_trattamento_selezionato('2');
					riListeLoc.setId_arrivo_fusione(appoggioIdIndice);
					riListeLoc.setFl_canc('N');
					riListeLoc.setUte_ins("IMPORT");
					riListeLoc.setTs_ins(new java.sql.Timestamp(System.currentTimeMillis()));
					riListeLoc.setUte_var("IMPORT");
					riListeLoc.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
					session.save(riListeLoc);
					session.flush();

					riListeArr = new Tb_report_indice_id_arrivo();
					riListeArr.setId_lista_ogg_loc(riListeLoc);
					riListeArr.setId_oggetto_locale(appoggioIdPolo);
					riListeArr.setId_arrivo_fusione(appoggioIdIndice);
					riListeArr.setTipologia_uguaglianza(appoggioTipo.charAt(0));
					riListeArr.setFl_canc('N');
					riListeArr.setUte_ins("IMPORT");
					riListeArr.setTs_ins(new java.sql.Timestamp(System.currentTimeMillis()));
					riListeArr.setUte_var("IMPORT");
					riListeArr.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
					session.save(riListeArr);
					session.flush();
					session.evict(riListeArr);

				} else {

					// SIAMO NEL CASO IN CUI NON E' RICHIESTA O NON SI PUO' FARE LA FUSIONE AUTOMATICA
					riListeLoc = new Tb_report_indice_id_locali();
					riListeLoc.setId_lista(riListe);
					riListeLoc.setId_oggetto_locale(appoggioIdPolo);
					riListeLoc.setRisultato_confronto(appoggioTipo.charAt(0));
					riListeLoc.setStato_lavorazione('1');
					riListeLoc.setTipo_trattamento_selezionato(null);
					riListeLoc.setId_arrivo_fusione(null);
					riListeLoc.setFl_canc('N');
					riListeLoc.setUte_ins("IMPORT");
					riListeLoc.setTs_ins(new java.sql.Timestamp(System.currentTimeMillis()));
					riListeLoc.setUte_var("IMPORT");
					riListeLoc.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
					session.save(riListeLoc);
					session.flush();

					if (!appoggioTipo.equals("N")) {
						int iInterno;
						for (iInterno = i; iInterno < sizeListaCoppie; iInterno++) {
							appoggioCoppiaInterno = (String) areaDatiPass.getListaCoppieEsitoConfronto().get(iInterno);
							appoggioIdPoloInterno = appoggioCoppiaInterno.substring(2,12);
							if (appoggioIdPolo.equals(appoggioIdPoloInterno)) {
								appoggioIdIndiceInterno = appoggioCoppiaInterno.substring(13,23);
								riListeArr = new Tb_report_indice_id_arrivo();
								riListeArr.setId_lista_ogg_loc(riListeLoc);
								riListeArr.setId_oggetto_locale(appoggioIdPoloInterno);
								riListeArr.setId_arrivo_fusione(appoggioIdIndiceInterno);
								riListeArr.setTipologia_uguaglianza(appoggioTipo.charAt(0));
								riListeArr.setFl_canc('N');
								riListeArr.setUte_ins("IMPORT");
								riListeArr.setTs_ins(new java.sql.Timestamp(System.currentTimeMillis()));
								riListeArr.setUte_var("IMPORT");
								riListeArr.setTs_var(new java.sql.Timestamp(System.currentTimeMillis()));
								session.save(riListeArr);
								session.flush();
								session.evict(riListeArr);
							} else {
								session.evict(riListeLoc);
								i = --iInterno;
								break;
							}
						}
						if (iInterno == sizeListaCoppie) {
							break;
						}
					}

				}
			}

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("Riga:" + appoggioCoppia + " ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}
		return areaDatiPass;
	}


	public AreaDatiPassaggioSchedaDocCiclicaVO cancellaTabelleTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass)  {

		DaoManager daoManager = new DaoManager();

		try {
			Tb_report_indice riListe = new Tb_report_indice();

			Session session = daoManager.getCurrentSession();
			riListe = (Tb_report_indice) session.load(Tb_report_indice.class, areaDatiPass.getIdListaSelez());
			session.delete(riListe);

		} catch (HibernateException hE) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + hE.getMessage());
		} catch (DaoManagerException dME) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + dME.getMessage());
		}

		return areaDatiPass;
	}



	// Marzo 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO: INIZIO trascinamento legami autore ma M a N (esempio disco M con tracce N)

	public AreaDatiVariazioneReturnVO trascinaLegameAutore(AreaDatiLegameTitoloVO areaDatiPass) {

		String testoProtocolloEsteso = "";
		SBNMarc sbnRisposta = null;
		int totRighe = 0;
		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();

		// Effettuiamo il trascinamento sulla Basi dati di interesse
		if (areaDatiPass.isInserimentoIndice()) {
			// Interroghiamo la M superiore per avere il reticolo conpleto ed il legame autore da copiare
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
			titoloAnaliticaVO.setBidRicerca(areaDatiPass.getBidPartenza());
			titoloAnaliticaVO.setRicercaPolo(false);
			titoloAnaliticaVO.setRicercaIndice(true);
			titoloAnaliticaVO.setInviaSoloSbnMarc(true);
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO;
			titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
			LegamiType legamiTypeDaCopiare = new LegamiType();
			ArrivoLegame arrivoLegameDaCopiare = new ArrivoLegame();

			if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
				sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
				if (totRighe == 0) {
					return areaDatiVariazioneReturnVO;
				} else {
					// Si legge la risposta dell'analitica per i soli legami a scendere
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
						DocumentoType documentoType = new DocumentoType();
						documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
						LegamiType legamiType = null;
						int legamiDocumentoCount = documentoType.getLegamiDocumentoCount();
						if (legamiDocumentoCount > 0) {
							legamiType = new LegamiType();

							for (int iLegDoc = 0; iLegDoc < legamiDocumentoCount; iLegDoc++) {
								legamiType = documentoType.getLegamiDocumento(iLegDoc);

								int arrivoLegameCount = legamiType.getArrivoLegameCount();
								if (arrivoLegameCount > 0) {

									for (int iArrLeg = 0; iArrLeg < arrivoLegameCount; iArrLeg++) {
										if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut() != null) {
											// SIAMO ARRIVATI AI LEGAMI AUTORE DELLA MONOGRAFIA DI PARTENZA
											// si deve controllare che sia il legame corretto da trascinare sugli Spogli
											// almaviva2 Febbraio 2016 - Intervento interno - la copia del legame titolo-autore deve essere fatta
											// considerando sia il vid che il relatorCode che il tiporesponsabilità

											// Intervento mantis 6158 almaviva2 - Aprile 2016
											// nei casi in cui si trascina un legame titolo-autore che ha il relatorCode nullo il trascinamento va in errore
											// a fronte dei controlli quindi si deve impostare il valore in modo tale che il null sia uguale a campo-vuoto
											if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getRelatorCode() == null) {
												legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().setRelatorCode("");
											}

											if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getIdArrivo().equals(areaDatiPass.getIdArrivo())
													&& legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getRelatorCode().equals(areaDatiPass.getRelatorCodeNew())
													&& legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getTipoRespons().toString().equals(areaDatiPass.getTipoResponsNew())) {
												legamiTypeDaCopiare = legamiType;
												arrivoLegameDaCopiare = legamiType.getArrivoLegame(iArrLeg);
												
												// Almaviva2 ottobre 2019 BUG MANTIS 7040
												// nel caso in cui non sia presente il Relator Code il campo deve essere impostato a null e non a vuoto
												if (arrivoLegameDaCopiare.getLegameElementoAut().getRelatorCode().equals("")) {
													arrivoLegameDaCopiare.getLegameElementoAut().setRelatorCode(null);
												}
												// FINE  Almaviva2 ottobre 2019 BUG MANTIS 7040
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				areaDatiVariazioneReturnVO.setCodErr(titoloAnaliticaReturnVO.getCodErr());
				areaDatiVariazioneReturnVO.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
				return areaDatiVariazioneReturnVO;
			}

			// Interroghiamo in ciclo le N per inserire i nuovo legame autore da copiare
			int tappo = areaDatiPass.getInferioriDaCatturare().length;
			for (int iCat = 0; iCat < tappo; iCat++) {

				String spoglioSelez = areaDatiPass.getInferioriDaCatturare()[iCat];
				titoloAnaliticaVO.setBidRicerca(spoglioSelez);
				titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
				if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
					sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
					totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
					if (totRighe == 0) {
						areaDatiVariazioneReturnVO.setCodErr(titoloAnaliticaReturnVO.getCodErr());
						areaDatiVariazioneReturnVO.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
						return areaDatiVariazioneReturnVO;
					}

					// Valorizzazione del protocollo con la nuova legamiType aggiornata con l'operazione di creazione
					ModificaType modificaType = null;
					modificaType = new ModificaType();
					modificaType.setTipoControllo(SbnSimile.CONFERMA);
					modificaType.setTipoControllo(SbnSimile.SIMILE);
					// Per ogni N/W/A si deve aggiungere il legame salvato nella precedente elaborazione
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0){
						DocumentoType documentoType = new DocumentoType();
						documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
						documentoType.clearLegamiDocumento();
						legamiTypeDaCopiare.clearArrivoLegame();
						legamiTypeDaCopiare.setIdPartenza(spoglioSelez);
						legamiTypeDaCopiare.setTipoOperazione(SbnTipoOperazione.CREA);
						legamiTypeDaCopiare.addArrivoLegame(arrivoLegameDaCopiare);

						documentoType.addLegamiDocumento(legamiTypeDaCopiare);
						modificaType.setDocumento(documentoType);
					} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0){
						ElementAutType elementAutType = new ElementAutType();
						elementAutType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);
						elementAutType.clearLegamiElementoAut();
						legamiTypeDaCopiare.clearArrivoLegame();
						legamiTypeDaCopiare.setIdPartenza(spoglioSelez);
						legamiTypeDaCopiare.setTipoOperazione(SbnTipoOperazione.CREA);
						legamiTypeDaCopiare.addArrivoLegame(arrivoLegameDaCopiare);

						elementAutType.addLegamiElementoAut(legamiTypeDaCopiare);
						modificaType.setElementoAut(elementAutType);
					}

					SbnRequestType sbnrequest = new SbnRequestType();
					SbnMessageType sbnmessage = new SbnMessageType();


					sbnrequest.setModifica(modificaType);
					sbnmessage.setSbnRequest(sbnrequest);
					try {
						this.indice.setMessage(sbnmessage, this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiVariazioneReturnVO.setCodErr("9999");
						areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
					} catch (IllegalArgumentException ie) {
						areaDatiVariazioneReturnVO.setCodErr("9999");
						areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + ie.getMessage());
					} catch (Exception e) {
						areaDatiVariazioneReturnVO.setCodErr("9999");
						areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + e.getMessage());
					}
					if (sbnRisposta == null) {
						areaDatiVariazioneReturnVO.setCodErr("noServerPol");
						areaDatiVariazioneReturnVO.setNumNotizie(0);
						return areaDatiVariazioneReturnVO;
					}
					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
							&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
						areaDatiVariazioneReturnVO.setCodErr("0000");
						testoProtocolloEsteso = testoProtocolloEsteso + "<BR>" +
							spoglioSelez + ":" + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
						areaDatiVariazioneReturnVO.setTestoProtocollo(testoProtocolloEsteso);
					}
				}
			}
		}


		// Interroghiamo la M superiore per avere il reticolo conpleto ed il legame autore da copiare
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO titoloAnaliticaVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
		titoloAnaliticaVO.setBidRicerca(areaDatiPass.getBidPartenza());
		titoloAnaliticaVO.setRicercaPolo(true);
		titoloAnaliticaVO.setRicercaIndice(false);
		titoloAnaliticaVO.setInviaSoloSbnMarc(true);
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO titoloAnaliticaReturnVO;
		titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
		LegamiType legamiTypeDaCopiare = new LegamiType();
		ArrivoLegame arrivoLegameDaCopiare = new ArrivoLegame();


		if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
			sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
			totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			if (totRighe == 0) {
				return areaDatiVariazioneReturnVO;
			} else {
				// Si legge la risposta dell'analitica per i soli legami a scendere
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
					DocumentoType documentoType = new DocumentoType();
					documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
					LegamiType legamiType = null;
					int legamiDocumentoCount = documentoType.getLegamiDocumentoCount();
					if (legamiDocumentoCount > 0) {
						legamiType = new LegamiType();

						for (int iLegDoc = 0; iLegDoc < legamiDocumentoCount; iLegDoc++) {
							legamiType = documentoType.getLegamiDocumento(iLegDoc);

							int arrivoLegameCount = legamiType.getArrivoLegameCount();
							if (arrivoLegameCount > 0) {

								for (int iArrLeg = 0; iArrLeg < arrivoLegameCount; iArrLeg++) {
									if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut() != null) {
										
										// SIAMO ARRIVATI AI LEGAMI AUTORE DELLA MONOGRAFIA DI PARTENZA
										// si deve controllare che sia il legame corretto da trascinare sugli Spogli
										// almaviva2 Febbraio 2016 - Intervento interno - la copia del legame titolo-autore deve essere fatta
										// considerando sia il vid che il relatorCode che il tiporesponsabilità
										
										// Intervento mantis 7040 almaviva2 - Ottobre 2019
										// nei casi in cui si trascina un legame titolo-autore che ha il relatorCode nullo il trascinamento va in errore
										// a fronte dei controlli quindi si deve impostare il valore in modo tale che il null sia uguale a campo-vuoto
										if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getRelatorCode() == null) {
											legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().setRelatorCode("");
										}
										if (legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getIdArrivo().equals(areaDatiPass.getIdArrivo())
												&& legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getRelatorCode().equals(areaDatiPass.getRelatorCodeNew())
												&& legamiType.getArrivoLegame(iArrLeg).getLegameElementoAut().getTipoRespons().toString().equals(areaDatiPass.getTipoResponsNew())) {
											legamiTypeDaCopiare = legamiType;
											arrivoLegameDaCopiare = legamiType.getArrivoLegame(iArrLeg);
											
											// Almaviva2 ottobre 2019 BUG MANTIS 7040
											// nel caso in cui non sia presente il Relator Code il campo deve essere impostato a null e non a vuoto
											if (arrivoLegameDaCopiare.getLegameElementoAut().getRelatorCode().equals("")) {
												arrivoLegameDaCopiare.getLegameElementoAut().setRelatorCode(null);
											}
											// FINE  Almaviva2 ottobre 2019 BUG MANTIS 7040
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			areaDatiVariazioneReturnVO.setCodErr(titoloAnaliticaReturnVO.getCodErr());
			areaDatiVariazioneReturnVO.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
			return areaDatiVariazioneReturnVO;
		}

		// Interroghiamo in ciclo le N per inserire i nuovo legame autore da copiare
		int tappo = areaDatiPass.getInferioriDaCatturare().length;
		for (int iCat = 0; iCat < tappo; iCat++) {

			String spoglioSelez = areaDatiPass.getInferioriDaCatturare()[iCat];
			titoloAnaliticaVO.setBidRicerca(spoglioSelez);
			titoloAnaliticaReturnVO = creaRichiestaAnaliticoTitoliPerBID(titoloAnaliticaVO);
			if (titoloAnaliticaReturnVO.getCodErr().equals("") || titoloAnaliticaReturnVO.getCodErr().equals("0000")) {
				sbnRisposta = titoloAnaliticaReturnVO.getSbnMarcType();
				totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
				if (totRighe == 0) {
					areaDatiVariazioneReturnVO.setCodErr(titoloAnaliticaReturnVO.getCodErr());
					areaDatiVariazioneReturnVO.setTestoProtocollo(titoloAnaliticaReturnVO.getTestoProtocollo());
					return areaDatiVariazioneReturnVO;
				}

				// Valorizzazione del protocollo con la nuova legamiType aggiornata con l'operazione di creazione
				ModificaType modificaType = null;
				modificaType = new ModificaType();
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				modificaType.setTipoControllo(SbnSimile.SIMILE);
				// Per ogni N/W/A si deve aggiungere il legame salvato nella precedente elaborazione
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0){
					DocumentoType documentoType = new DocumentoType();
					documentoType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
					documentoType.clearLegamiDocumento();
					legamiTypeDaCopiare.clearArrivoLegame();
					legamiTypeDaCopiare.setIdPartenza(spoglioSelez);
					legamiTypeDaCopiare.setTipoOperazione(SbnTipoOperazione.CREA);
					legamiTypeDaCopiare.addArrivoLegame(arrivoLegameDaCopiare);

					documentoType.addLegamiDocumento(legamiTypeDaCopiare);
					modificaType.setDocumento(documentoType);
				} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0){
					ElementAutType elementAutType = new ElementAutType();
					elementAutType = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);
					elementAutType.clearLegamiElementoAut();
					legamiTypeDaCopiare.clearArrivoLegame();
					legamiTypeDaCopiare.setIdPartenza(spoglioSelez);
					legamiTypeDaCopiare.setTipoOperazione(SbnTipoOperazione.CREA);
					legamiTypeDaCopiare.addArrivoLegame(arrivoLegameDaCopiare);

					elementAutType.addLegamiElementoAut(legamiTypeDaCopiare);
					modificaType.setElementoAut(elementAutType);
				}

				SbnRequestType sbnrequest = new SbnRequestType();
				SbnMessageType sbnmessage = new SbnMessageType();

				sbnrequest.setModifica(modificaType);
				sbnmessage.setSbnRequest(sbnrequest);
				try {
					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();
				} catch (SbnMarcException ve) {
					areaDatiVariazioneReturnVO.setCodErr("9999");
					areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
				} catch (IllegalArgumentException ie) {
					areaDatiVariazioneReturnVO.setCodErr("9999");
					areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + ie.getMessage());
				} catch (Exception e) {
					areaDatiVariazioneReturnVO.setCodErr("9999");
					areaDatiVariazioneReturnVO.setTestoProtocollo("ERROR >>" + e.getMessage());
				}
				if (sbnRisposta == null) {
					areaDatiVariazioneReturnVO.setCodErr("noServerPol");
					areaDatiVariazioneReturnVO.setNumNotizie(0);
					return areaDatiVariazioneReturnVO;
				}
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					areaDatiVariazioneReturnVO.setCodErr("0000");
					testoProtocolloEsteso = testoProtocolloEsteso + "<BR>" +
						spoglioSelez + ":" + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
					areaDatiVariazioneReturnVO.setTestoProtocollo(testoProtocolloEsteso);
				}
			}
		}
		areaDatiVariazioneReturnVO.setCodErr("0000");
		return areaDatiVariazioneReturnVO;
	}

}// end class XMLTitoli
