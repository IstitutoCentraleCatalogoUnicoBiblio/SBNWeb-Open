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
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.SinteticaTitoli;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityCampiTitolo;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti.LocalizzazioneMassiva;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.domain.semantica.classi.Classi;
import it.iccu.sbn.ejb.domain.semantica.soggetti.Soggetti;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.Ac_210Type;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CdBibType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAutoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaLuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaMarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.ComunicaAllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SequenceType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndicatorePubblicato;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoDigitalizzazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiCatturaReticoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.CitazioneStandard;
import it.iccu.sbn.ejb.vo.gestionebibliografica.Repertorio;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaLocalizzazioniView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPerConfrontoCitazioniMarcheVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiControlliPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_collocazioneDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_esemplare_titoloDao;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.util.Constants.DocFisico;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.mail.MailBodyBuilder;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.servizi.ServiziMail;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.exception.SbnBaseException;

import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;

import org.exolab.castor.xml.MarshalException;

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
public class SbnGestioneAllAuthorityDao {

	private FactorySbn indice;

	private FactorySbn polo;

	private SbnUserType user;

	private ServiziMail util = new ServiziMail();

	public SbnGestioneAllAuthorityDao() {
		super();
	}

	public SbnGestioneAllAuthorityDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public static final String IID_STRINGAVUOTA = "";

	private int level = 0;

	private PeriodiciSBN periodiciSBN;
	private Soggetti soggetti;
	private Classi classi;

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

	private PeriodiciSBN getPeriodiciBean() {
		try {
			if (periodiciSBN != null)
				return periodiciSBN;

				periodiciSBN = DomainEJBFactory.getInstance().getPeriodici();
			return periodiciSBN;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	private Soggetti getSoggetti() {
		try {
			if (soggetti != null)
				return soggetti;

			soggetti = DomainEJBFactory.getInstance().getSoggetti();
			return soggetti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	private Classi getClassi() {
		try {
			if (classi != null)
				return classi;

			classi = DomainEJBFactory.getInstance().getClassi();
			return classi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private Profiler getProfilo() {
		try {
				return DomainEJBFactory.getInstance().getProfiler();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private Map<String, String> tabellaTimeStamp = new HashMap<String, String>();

	private String[] listaInfDaCatt;

	private UtilityCastor utilityCastor = new UtilityCastor();
	private Set<String> listaOggetti = null;

	// ===================================================================
	// Da utilizzare in caso di malfunzionamenti vari
	//
	// StringWriter stringWriter1 = new StringWriter();
	// sbnmessage.marshal(stringWriter1);
	// String xmlRequest = stringWriter1.toString();
	// ===================================================================

	public AreaDatiPassaggioGetIdSbnVO getIdSbn(
			AreaDatiPassaggioGetIdSbnVO areaDatiPass) {
		SBNMarc sbnRisposta = null;

		try {

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;

			creaType = new CreaType();
			creaType.setTipoControllo(SbnSimile.CONFERMA);
			creaTypeChoice = new CreaTypeChoice();

			SequenceType sequenceType = new SequenceType();
			if (areaDatiPass.getTipoAut() != null) {
				sequenceType.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAut()));
			} else {
				if (areaDatiPass.getTipoMat() != null) {
					sequenceType.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
				} else {
					sequenceType.setTipoMateriale(SbnMateriale.valueOf("M"));
				}
				if (areaDatiPass.getTipoRec() != null) {
					sequenceType.setTipoRecord(TipoRecord.valueOf(areaDatiPass.getTipoRec()));
				} else {
					sequenceType.setTipoRecord(TipoRecord.valueOf("a"));
				}
			}
			sequenceType.setIdSequence(SBNMarcUtil.SBNMARC_DEFAULT_ID);
			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();
			sbnmessage.setSbnRequest(sbnrequest);

			creaTypeChoice.setSequence(sequenceType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(creaType);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerPol");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPass;
			}
			areaDatiPass.setCodErr("0000");
			areaDatiPass.setIdSbn(sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getSequence().getIdSequence());
			return areaDatiPass;

		} catch (SbnMarcException ve) {
			ve.printStackTrace();
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPass;
	}

	public int verificaLocalizzazione(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass,
			boolean soloPresenzaPolo) {

		int localizzazioniOggetto = TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE;
		String codBiblio = areaDatiPass.getCodiceSbn();
		areaDatiPass.setCodiceSbn(areaDatiPass.getCodiceSbn().substring(0, 3)
				+ "   ");
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(
				indice, polo, user);
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

		areaDatiPassReturn = gestioneAllAuthority.cercaLocalizzazioni(
				areaDatiPass, soloPresenzaPolo);

		if (soloPresenzaPolo) {
			if (areaDatiPassReturn.getTotRighe() > 0) {
				localizzazioniOggetto = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
			}
			return localizzazioniOggetto;
		}

		SinteticaLocalizzazioniView sinteticaLocalizzazioniView;
		if (areaDatiPassReturn.getListaSintetica() != null) {
			int size = areaDatiPassReturn.getListaSintetica().size();
			for (int j = 0; j < size; j++) {
				sinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) areaDatiPassReturn.getListaSintetica().get(j);
				if (sinteticaLocalizzazioniView.getIDSbn().equals(codBiblio)) {
					if (sinteticaLocalizzazioniView.getTipoLoc().equals("Gestione")) {
						localizzazioniOggetto = TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE;
					} else if (sinteticaLocalizzazioniView.getTipoLoc().equals("Possesso")) {
						localizzazioniOggetto = TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO;
					} else if (sinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione")) {
						localizzazioniOggetto = TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA;
					}
					break;
				} else {
					localizzazioniOggetto = TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
					// if
					// (sinteticaLocalizzazioniView.getTipoLoc().equals("Gestione"))
					// {
					// localizzazioniOggetto =
					// TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
					// } else if
					// (sinteticaLocalizzazioniView.getTipoLoc().equals("Possesso"))
					// {
					// localizzazioniOggetto =
					// TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
					// } else if
					// (sinteticaLocalizzazioniView.getTipoLoc().equals("Possesso/Gestione"))
					// {
					// localizzazioniOggetto =
					// TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO;
					// }
				}
			}
		}
		return localizzazioniOggetto;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO cercaLocalizzazioni(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass,
			boolean soloPresenzaPolo) {

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			LocalizzaInfoType localizzaInfo = new LocalizzaInfoType();

			// tipoOutput sintetica 001
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			localizzaInfo.setSbnIDLoc(areaDatiPass.getIdLoc());

			C899 c899 = new C899();

			// anagrafe
			if (areaDatiPass.getCodiceAnagrafeBiblioteca() != null) {
				if (!areaDatiPass.getCodiceAnagrafeBiblioteca().equals(IID_STRINGAVUOTA)) {
					c899.setC1_899(areaDatiPass.getCodiceAnagrafeBiblioteca());
				}
			} else {
				areaDatiPass.setCodiceAnagrafeBiblioteca(IID_STRINGAVUOTA);
			}

			// polo+sigla
			if (areaDatiPass.getCodiceSbn() != null) {
				if (!areaDatiPass.getCodiceSbn().equals(IID_STRINGAVUOTA)) {
					c899.setC2_899(areaDatiPass.getCodiceSbn());
				}
			} else {
				areaDatiPass.setCodiceSbn(IID_STRINGAVUOTA);
			}

			c899.setTipoInfo(SbnTipoLocalizza.TUTTI);

			C899[] arrayC899 = new C899[1];
			arrayC899[0] = c899;

			if ((!areaDatiPass.getCodiceSbn().equals(IID_STRINGAVUOTA))
					|| (!areaDatiPass.getCodiceAnagrafeBiblioteca().equals(IID_STRINGAVUOTA))) {
				localizzaInfo.setT899(arrayC899);
			}

			localizzaInfo.setTipoInfo(SbnTipoLocalizza.TUTTI);

			SbnOggetto sbnOggetto = new SbnOggetto();

			if ((areaDatiPass.getAuthority() == null) || (areaDatiPass.getAuthority().equals("TU"))	|| (areaDatiPass.getAuthority().equals("UM"))) {
				if (areaDatiPass.getNatura() != null && areaDatiPass.getNatura().equals("A")) {
					if (areaDatiPass.getTipoMat() == null || areaDatiPass.getTipoMat().equals(IID_STRINGAVUOTA)) {
						sbnOggetto.setTipoAuthority(SbnAuthority.TU);
					} else if ((areaDatiPass.getTipoMat().equals("M")) || (areaDatiPass.getTipoMat().equals("U"))) {
						sbnOggetto.setTipoAuthority(SbnAuthority.UM);
					} else {
					}
				} else {
					// ALMAVIVA2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf("M"));
					// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_0);
					// Testare perchè dovrebbe essere 5 e non zero ... da verificare
					// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_5);
				}
			} else if (areaDatiPass.getAuthority().equals("AU")) {
				sbnOggetto.setTipoAuthority(SbnAuthority.AU);
			} else if (areaDatiPass.getAuthority().equals("MA")) {
				sbnOggetto.setTipoAuthority(SbnAuthority.MA);
			} else if (areaDatiPass.getAuthority().equals("LU")) {
				sbnOggetto.setTipoAuthority(SbnAuthority.LU);
			}

			localizzaInfo.setTipoOggetto(sbnOggetto);

			localizzaInfo.setTipoOperazione(SbnAzioneLocalizza.ESAME);

			cercaType.setCercaLocalizzaInfo(localizzaInfo);

			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);
			if (areaDatiPass.isPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			}
			if (areaDatiPass.isIndice()) {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}

			if (sbnRisposta != null) {
				String esito = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
				String testoEsito = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

				if (sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getTotRighe() == 0) {
					areaDatiPassReturn.setCodErr(esito);
					if (!esito.equals("3001")) {
						areaDatiPassReturn.setTestoProtocollo(testoEsito);
					}
					return areaDatiPassReturn;
				}

				SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

				SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
				SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
				int totRighe = sbnOutPut.getTotRighe();

				if (soloPresenzaPolo) {
					areaDatiPassReturn.setTotRighe(totRighe);
					return areaDatiPassReturn;
				}

				int progressivo = 0;
				SinteticaLocalizzazioniView sinteticaLocalizzazioniView;
				List listaSintentica = new ArrayList();

				int localizzaInfoCount = sbnOutPut.getLocalizzaInfoCount();

				for (int i = 0; i < localizzaInfoCount; i++) {
					sinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
					LocalizzaInfoType localizzaInfoOut = sbnOutPut
							.getLocalizzaInfo(i);

					for (int j = 0; j < localizzaInfoOut.getT899Count(); j++) {

						sinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
						C899 c899out = localizzaInfoOut.getT899(j);
						areaDatiPassReturn.setC899Localizzazioni(c899out);

						++progressivo;
						sinteticaLocalizzazioniView.setProgressivo(progressivo);
						sinteticaLocalizzazioniView.setIDSbn(c899out.getC2_899());
						sinteticaLocalizzazioniView.setCodBiblioteca(c899out.getC2_899().substring(3, 6));
						sinteticaLocalizzazioniView.setIDAnagrafe(c899out.getC1_899());
						String nome = c899out.getA_899();
						if (nome != null) {
							sinteticaLocalizzazioniView.setDescrBiblioteca(nome);
						} else {
							sinteticaLocalizzazioniView.setDescrBiblioteca(IID_STRINGAVUOTA);
						}
						int tipoLoc = c899out.getTipoInfo().getType();
						if (tipoLoc == SbnTipoLocalizza.GESTIONE_TYPE) {
							sinteticaLocalizzazioniView.setTipoLoc("Gestione");
						} else if (tipoLoc == SbnTipoLocalizza.POSSESSO_TYPE) {
							sinteticaLocalizzazioniView.setTipoLoc("Possesso");
						} else {
							sinteticaLocalizzazioniView.setTipoLoc("Possesso/Gestione");
						}

						// Modifica ALMAVIVA2 14.10.2010 BUG MANTIS 3930 per ogni elemento di lista si salva anche tutta l'etichetta
						// C899 per inviarla in ciclo in Indice nel caso di invio in Indice delle localizzazioni presenti in Polo
						sinteticaLocalizzazioniView.setC899Localizzazioni(c899out);
						if (c899out.getB_899() != null) {
							sinteticaLocalizzazioniView.setFondo(c899out.getB_899());
						} else {
							sinteticaLocalizzazioniView.setFondo("");
						}

						if (c899out.getG_899() != null) {
							sinteticaLocalizzazioniView.setSegnatura(c899out.getG_899());
						} else {
							sinteticaLocalizzazioniView.setSegnatura("");
						}

						if (c899out.getS_899() != null) {
							sinteticaLocalizzazioniView.setSegnaturaAntica(c899out.getS_899());
						} else {
							sinteticaLocalizzazioniView.setSegnaturaAntica("");
						}

						if (c899out.getZ_899() != null) {
							sinteticaLocalizzazioniView.setConsistenza(c899out.getZ_899());
						} else {
							sinteticaLocalizzazioniView.setConsistenza("");
						}

						if (c899out.getN_899() != null) {
							sinteticaLocalizzazioniView.setNote(c899out.getN_899());
						} else {
							sinteticaLocalizzazioniView.setNote("");
						}

						if (c899out.getE_899() != null) {
							sinteticaLocalizzazioniView.setFormatoElettronico(c899out.getE_899().toString());
						} else {
							sinteticaLocalizzazioniView.setFormatoElettronico("N");
						}

						if (c899out.getQ_899() != null) {
							sinteticaLocalizzazioniView.setValoreM(c899out.getQ_899().toString());
						} else {
							sinteticaLocalizzazioniView.setValoreM("N");
						}

						if (c899out.getU_899() != null) {
							sinteticaLocalizzazioniView
									.setUriCopiaElettr(c899out.getU_899());
						} else {
							sinteticaLocalizzazioniView.setUriCopiaElettr("");
						}

						if (c899out.getT_899() != null) {
							sinteticaLocalizzazioniView.setTipoDigitalizzazione(c899out.getT_899().toString());
						} else {
							sinteticaLocalizzazioniView.setTipoDigitalizzazione("");
						}

						listaSintentica.add(sinteticaLocalizzazioniView);
					}
				}

				areaDatiPassReturn.setIdLista(null);
				areaDatiPassReturn.setMaxRighe(0);
				areaDatiPassReturn.setTotRighe(totRighe);
				areaDatiPassReturn.setNumBlocco(1);
				areaDatiPassReturn.setNumNotizie(totRighe);
				areaDatiPassReturn.setTotBlocchi(1);
				areaDatiPassReturn.setListaSintetica(listaSintentica);
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



	// Evolutiva Google3: Viene creato il nuovo metodo localizzaUnicoXML che effettua la chiamata per localizzazione tramite
	// un unica chiamata ai protocollo di Indice/Polo utilizzando sempre l'area di passaggio AreaDatiLocalizzazioniAuthorityMultiplaVO
	// questo metodo sostituirà localizzaAuthorityMultipla e localizzaAuthority
	public AreaDatiVariazioneReturnVO localizzaUnicoXML(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPassMultipla) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		// SbnIDLoc: è quello del titolo per cui si vuole inserire una nuova localizzazione.
		String sbnIdLoc = "";;
		AreaDatiLocalizzazioniAuthorityVO areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();;
		SbnMessageType sbnmessage = new SbnMessageType();
		SbnRequestType sbnrequest = new SbnRequestType();
		SBNMarc sbnRisposta = null;

		int size = areaDatiPassMultipla.getListaAreaLocalizVO().size();
		LocalizzaType localizzaType = new LocalizzaType();
		LocalizzaInfoType[] localizzaInfoType = new LocalizzaInfoType[size];


		for (int i = 0; i < size; i++) {
			areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();
			areaDatiPass = areaDatiPassMultipla.getListaAreaLocalizVO().get(i);

			// Inizio caricamento ciclico delle localizzaqzioni in Indice con unico XML
			areaDatiPassReturn.setCodErr("0000");
			areaDatiPassReturn.setTestoProtocollo("");

			if (areaDatiPass.getNatura() == null) {
				areaDatiPass.setNatura("");
			}

			LocalizzaInfoType locElem = new LocalizzaInfoType();
			C899[] c899 = new C899[1];
			C899 c899Elem = new C899();

			sbnIdLoc = areaDatiPass.getIdLoc();
			locElem.setSbnIDLoc(sbnIdLoc);
			SbnOggetto sbnOggetto = new SbnOggetto();
			if (areaDatiPass.getAuthority().equals("")) {
				// ALMAVIVA2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
				// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_0);
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf("M"));
			} else {
				sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthority()));
			}
			locElem.setTipoOggetto(sbnOggetto);

			// NOME BIBLIOTECA
			String nomeBiblioteca = areaDatiPass.getNomeBiblioteca();
			if (nomeBiblioteca != null) {
				if (!nomeBiblioteca.equals(IID_STRINGAVUOTA)) {
					c899Elem.setA_899(nomeBiblioteca);
				}
			}

			if (areaDatiPass.getCodiceAnagrafeBiblioteca() != null) {
				// CODICE ANAGRAFE
				String codiceAnagrafe = areaDatiPass.getCodiceAnagrafeBiblioteca().trim();
				if (!codiceAnagrafe.equals(IID_STRINGAVUOTA)) {
					c899Elem.setC1_899(codiceAnagrafe);
				}
			}
			if (areaDatiPass.getCodiceSbn() != null) {
				// CODICE SBN
				String codiceSbn = areaDatiPass.getCodiceSbn();
				if (!codiceSbn.equals(IID_STRINGAVUOTA)) {
					c899Elem.setC2_899(codiceSbn);
				}
			}

			// FONDO
			if (areaDatiPass.getFondo() != null) {
				String fondo = areaDatiPass.getFondo();
				c899Elem.setB_899(fondo);
			}

			// CONSISTENZA
			if (areaDatiPass.getConsistenza() != null) {
				String consistenza = areaDatiPass.getConsistenza();
				c899Elem.setZ_899(consistenza);
			}
			// SEGNATURA
			if (areaDatiPass.getSegnatura() != null) {
				String segnatura = areaDatiPass.getSegnatura();
				c899Elem.setG_899(segnatura);
			}
			if (areaDatiPass.getSegnaturaAntica() != null) {
				// SEGNATURA ANTICA
				String segnaturaAntica = areaDatiPass.getSegnaturaAntica();
				c899Elem.setS_899(segnaturaAntica);
			}

			// NOTE
			if (areaDatiPass.getNote() != null) {
				String note = areaDatiPass.getNote();
				// Modifica gennaio 2019 - ALMAVIVA2 Bug Mantis 6876 - malfunzionamento risolto (il campo note può essere vuoto e come
				// tale deve essere inviato altrimenti l'aggiornamento da valorizzato a vuoto non viene effettuato sul DB)
//				if (!note.equals(IID_STRINGAVUOTA)) {
//					c899Elem.setN_899(note);
//				}
				c899Elem.setN_899(note);
			}

			// DISPONIBILITA' FORMATO ELETTRONICO
			if (areaDatiPass.getDispoFormatoElettr() != null) {
				String dispoFormatoElettr = areaDatiPass
						.getDispoFormatoElettr();
				c899Elem.setE_899(SbnIndicatore.valueOf(dispoFormatoElettr));
			}
			// INDICATORE DI MUTILO
			if (areaDatiPass.getIndicatoreMutilo() != null) {
				String indicatoreMutilo = areaDatiPass.getIndicatoreMutilo();
				c899Elem.setQ_899(SbnIndicatore.valueOf(indicatoreMutilo));
			} else {
				c899Elem.setQ_899(SbnIndicatore.N);
			}

			// URI ACCESSO COPIA ELETTRONICA
			if (areaDatiPass.getUriAccesso() != null) {
				String uriAccesso = areaDatiPass.getUriAccesso();
				if (!uriAccesso.equals(IID_STRINGAVUOTA)) {
					c899Elem.setU_899(uriAccesso);
				}
			}
			// TIPO DIGITALIZZAZIONE COPIA ELETTRONICA
			if (areaDatiPass.getTipoDigitalizzazione() != null) {
				if (areaDatiPass.getTipoDigitalizzazione().equals("")) {
					c899Elem.setT_899(null);
				} else {
					String tipoDigitalizzazione = areaDatiPass.getTipoDigitalizzazione();
					c899Elem.setT_899(SbnTipoDigitalizzazione.valueOf(tipoDigitalizzazione));
				}
			} else {
				c899Elem.setT_899(null);
			}
			// TIPO OPERAZIONE (Localizza, Delocalizza, Correggi, Allineato, Esame)
			locElem.setTipoOperazione(SbnAzioneLocalizza.valueOf(areaDatiPass.getTipoOpe()));

			// TIPO INFO (Gestione, Possesso, Tutti)
			if (areaDatiPass.getTipoLoc().equals("Gestione")) {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.GESTIONE);
			} else if (areaDatiPass.getTipoLoc().equals("Possesso")) {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.POSSESSO);
			} else {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.TUTTI);
			}

			c899Elem.setTipoInfo(areaDatiPass.getSbnTipoLoc());
			locElem.setTipoInfo(areaDatiPass.getSbnTipoLoc());

			if (areaDatiPass.getC899Localizzazioni() != null) {
				// quest'area è presente quando si viene da Cataloga in Indice (l'area 899 è quella del POLO)
				c899[0] = areaDatiPass.getC899Localizzazioni();
				locElem.setTipoInfo(areaDatiPass.getC899Localizzazioni().getTipoInfo());
			} else {
				// C'E' LA POSSIBILITA' DI INSERIRE FINO A 100 LOCALIZZAZIONI
				c899[0] = c899Elem;
			}
			locElem.addT899(c899[0]);

			localizzaInfoType[i] = locElem;
			// Fine   caricamento ciclico delle localizzazioni in Indice con unico XML
		}
		localizzaType.setLocalizzaInfo(localizzaInfoType);

		try {
			sbnrequest.setLocalizza(localizzaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (areaDatiPass.isIndice() && areaDatiPass.isPolo()
					&& areaDatiPass.isMantieniAllineamento()) {


				// Inizio intervento ALMAVIVA2 - mail Scognamiglio 12.06.2012
				//.le raccolte fattizie essendo oggetti solo locali non devono essere localizzate in Indice
				if (!areaDatiPass.getNatura().equals("R")) {
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerInd");
						return areaDatiPassReturn;
					}

					//almaviva5_20140206 evolutive google3
					String bib = this.user.getBiblioteca();
					SbnMarcEsitoType esitoType = SbnMarcEsitoType.of(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					if (esitoType == SbnMarcEsitoType.SERVER_NON_DISPONIBILE) {
						//salvataggio xml per batch localizzazione massiva
						LocalizzazioneMassiva.salvaLocalizzazione(bib.substring(0, 3), bib.substring(3), sbnmessage, this.user);
					}

					if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
						return areaDatiPassReturn;
					}

					if (esitoType == SbnMarcEsitoType.OK || esitoType == SbnMarcEsitoType.LOCALIZZA_CORREZIONE) {
						LocalizzazioneMassiva.rimuoviLocalizzazione(bib.substring(0, 3), bib.substring(3), sbnmessage, this.user);
					}
				}

				// L'aggiornamento ion Polo viene fatto solo per tenere allineate le informazioni relative al possesso;
				if (areaDatiPass.getTipoLoc().equals("Gestione")) {
					return areaDatiPassReturn;
				}

				for (int i = 0; i < sbnmessage.getSbnRequest().getLocalizza().getLocalizzaInfoCount(); i++) {
					sbnmessage.getSbnRequest().getLocalizza().getLocalizzaInfo(i).setTipoInfo(SbnTipoLocalizza.POSSESSO);
					sbnmessage.getSbnRequest().getLocalizza().getLocalizzaInfo(i).getT899(0).setTipoInfo(SbnTipoLocalizza.POSSESSO);
				}

				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3013")) {
						areaDatiPassReturn.setCodErr("0000");
						areaDatiPassReturn.setTestoProtocollo("Aggiornamento correttamente eseguito in Indice;<BR>ATTENZIONE: l'oggetto è assente dalla dati di Polo");
					} else {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					}
				}
				return areaDatiPassReturn;
			}

			if (areaDatiPass.isPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					return areaDatiPassReturn;
				}
				// Inizio manutenzione ALMAVIVA2 09.12.2009 - BUG MANTIS 3248 - la doppia localizzazione per possesso dell'UNICUM
				// non comporta errore bloccante; la transazione continua (risposta 3252 quindi non deve essere inviata)
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3252")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
			}

			if (areaDatiPass.isIndice()) {
				// Inizio intervento ALMAVIVA2 - mail Scognamiglio 12.06.2012
				//.le raccolte fattizie essendo oggetti solo locali non devono essere localizzate in Indice
				if (!areaDatiPass.getNatura().equals("R")) {
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerInd");
						return areaDatiPassReturn;
					}

					//almaviva5_20140206 evolutive google3
					SbnResultType result = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult();
					if (SbnMarcEsitoType.of(result.getEsito()) == SbnMarcEsitoType.SERVER_NON_DISPONIBILE) {
						//salvataggio xml per batch localizzazione massiva
						String bib = this.user.getBiblioteca();
						LocalizzazioneMassiva.salvaLocalizzazione(bib.substring(0, 3), bib.substring(3), sbnmessage, this.user);
					}

					// La risposta 7017 non deve essere inviata;
					// Errore :7017 Attenzione: utilizzare la funzionalità "Correggi" per modificare gli attributi del possesso.
					// sbnIdLoc errato: CUB0496948
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
							|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("7017")) {
						return areaDatiPassReturn;
					}
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
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




	public AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPassMultipla) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		AreaDatiLocalizzazioniAuthorityVO areaDatiPass;
		AreaDatiVariazioneReturnVO areaDatiPassReturnInterna;
		int size = areaDatiPassMultipla.getListaAreaLocalizVO().size();
		for (int i = 0; i < size; i++) {
			areaDatiPass = new AreaDatiLocalizzazioniAuthorityVO();
			areaDatiPassReturnInterna = new AreaDatiVariazioneReturnVO();
			areaDatiPass = areaDatiPassMultipla
					.getListaAreaLocalizVO().get(i);
			areaDatiPassReturnInterna = localizzaAuthority(areaDatiPass);
			if (areaDatiPassReturnInterna == null) {
				areaDatiPassReturn = areaDatiPassReturnInterna;
				break;
			}
			if (!areaDatiPassReturnInterna.getCodErr().equals("0000")) {
				areaDatiPassReturn = areaDatiPassReturnInterna;
				break;
			}
			if (areaDatiPassReturnInterna.getTestoProtocollo() != null) {
				if (areaDatiPassReturn.getTestoProtocolloInformational() == null) {
					areaDatiPassReturn
							.setTestoProtocolloInformational(areaDatiPassReturnInterna
									.getTestoProtocollo());
				} else {
					areaDatiPassReturn
							.setTestoProtocolloInformational(areaDatiPassReturn
									.getTestoProtocolloInformational()
									+ areaDatiPassReturnInterna
											.getTestoProtocollo());
				}
			}
		}
		return areaDatiPassReturn;

	}

	public AreaDatiVariazioneReturnVO localizzaAuthority(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");

		// Modifica ALMAVIVA2 Bug Mantis 3965 Inserita inizializzazione sul null del campo areaDatiPassReturn.getTestoProtocollo()
		areaDatiPassReturn.setTestoProtocollo("");

		if (areaDatiPass.getNatura() == null) {
			areaDatiPass.setNatura("");
		}

		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			LocalizzaType localizzaType = new LocalizzaType();
			LocalizzaInfoType[] localizzaInfoType = new LocalizzaInfoType[1];
			LocalizzaInfoType locElem = new LocalizzaInfoType();
			C899[] c899 = new C899[1];
			C899 c899Elem = new C899();

			// SbnIDLoc: è quello del titolo per cui si vuole
			// inserire una nuova localizzazione.
			String sbnIdLoc = areaDatiPass.getIdLoc();
			locElem.setSbnIDLoc(sbnIdLoc);
			SbnOggetto sbnOggetto = new SbnOggetto();
			if (areaDatiPass.getAuthority().equals("")) {
				// ALMAVIVA2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
				//sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_0);
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf("M"));
			} else {
				sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getAuthority()));
			}
			locElem.setTipoOggetto(sbnOggetto);

			// NOME BIBLIOTECA
			String nomeBiblioteca = areaDatiPass.getNomeBiblioteca();
			if (nomeBiblioteca != null) {
				if (!nomeBiblioteca.equals(IID_STRINGAVUOTA)) {
					c899Elem.setA_899(nomeBiblioteca);
				}
			}

			if (areaDatiPass.getCodiceAnagrafeBiblioteca() != null) {
				// CODICE ANAGRAFE
				String codiceAnagrafe = areaDatiPass.getCodiceAnagrafeBiblioteca().trim();
				if (!codiceAnagrafe.equals(IID_STRINGAVUOTA)) {
					c899Elem.setC1_899(codiceAnagrafe);
				}
			}
			if (areaDatiPass.getCodiceSbn() != null) {
				// CODICE SBN
				String codiceSbn = areaDatiPass.getCodiceSbn();
				if (!codiceSbn.equals(IID_STRINGAVUOTA)) {
					c899Elem.setC2_899(codiceSbn);
				}
			}

			// modifica ALMAVIVA2 BUG MANTIS 4499 (collaudo)
			// Nell'inserimento/aggiornamento dei dati di possesso è stato eliminato il controllo su valore vuoto
			// che è ammesso per i campi Consistenza, Fondo, Segnatura, Segnatura antica


			// FONDO
			if (areaDatiPass.getFondo() != null) {
				String fondo = areaDatiPass.getFondo();
				c899Elem.setB_899(fondo);
			}

			// CONSISTENZA
			if (areaDatiPass.getConsistenza() != null) {
				String consistenza = areaDatiPass.getConsistenza();
				c899Elem.setZ_899(consistenza);
			}
			// SEGNATURA
			if (areaDatiPass.getSegnatura() != null) {
				String segnatura = areaDatiPass.getSegnatura();
				c899Elem.setG_899(segnatura);
			}
			if (areaDatiPass.getSegnaturaAntica() != null) {
				// SEGNATURA ANTICA
				String segnaturaAntica = areaDatiPass.getSegnaturaAntica();
				c899Elem.setS_899(segnaturaAntica);
			}

			// NOTE
			if (areaDatiPass.getNote() != null) {
				String note = areaDatiPass.getNote();
				if (!note.equals(IID_STRINGAVUOTA)) {
					c899Elem.setN_899(note);
				}
			}

			// DISPONIBILITA' FORMATO ELETTRONICO
			if (areaDatiPass.getDispoFormatoElettr() != null) {
				String dispoFormatoElettr = areaDatiPass
						.getDispoFormatoElettr();
				c899Elem.setE_899(SbnIndicatore.valueOf(dispoFormatoElettr));
			}
			// INDICATORE DI MUTILO
			if (areaDatiPass.getIndicatoreMutilo() != null) {
				String indicatoreMutilo = areaDatiPass.getIndicatoreMutilo();
				c899Elem.setQ_899(SbnIndicatore.valueOf(indicatoreMutilo));
			} else {
				c899Elem.setQ_899(SbnIndicatore.N);
			}

			// URI ACCESSO COPIA ELETTRONICA
			if (areaDatiPass.getUriAccesso() != null) {
				String uriAccesso = areaDatiPass.getUriAccesso();
				if (!uriAccesso.equals(IID_STRINGAVUOTA)) {
					c899Elem.setU_899(uriAccesso);
				}
			}
			// TIPO DIGITALIZZAZIONE COPIA ELETTRONICA
			if (areaDatiPass.getTipoDigitalizzazione() != null) {
				if (areaDatiPass.getTipoDigitalizzazione().equals("")) {
					c899Elem.setT_899(null);
				} else {
					String tipoDigitalizzazione = areaDatiPass
							.getTipoDigitalizzazione();
					c899Elem.setT_899(SbnTipoDigitalizzazione
							.valueOf(tipoDigitalizzazione));
				}
			} else {
				c899Elem.setT_899(null);
			}
			// TIPO OPERAZIONE (Localizza, Delocalizza, Correggi, Allineato,
			// Esame)
			locElem.setTipoOperazione(SbnAzioneLocalizza.valueOf(areaDatiPass
					.getTipoOpe()));

			// TIPO INFO (Gestione, Possesso, Tutti)
			if (areaDatiPass.getTipoLoc().equals("Gestione")) {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.GESTIONE);
			} else if (areaDatiPass.getTipoLoc().equals("Possesso")) {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.POSSESSO);
			} else {
				areaDatiPass.setSbnTipoLoc(SbnTipoLocalizza.TUTTI);
			}

			c899Elem.setTipoInfo(areaDatiPass.getSbnTipoLoc());
			locElem.setTipoInfo(areaDatiPass.getSbnTipoLoc());

			if (areaDatiPass.getC899Localizzazioni() != null) {
				// quest'area è presente quando si viene da Cataloga in Indice
				// (l'area 899 è quella del POLO)
				c899[0] = areaDatiPass.getC899Localizzazioni();
				locElem.setTipoInfo(areaDatiPass.getC899Localizzazioni()
						.getTipoInfo());
			} else {
				// C'E' LA POSSIBILITA' DI INSERIRE FINO A 100 LOCALIZZAZIONI
				c899[0] = c899Elem;
			}
			locElem.addT899(c899[0]);

			localizzaInfoType[0] = locElem;
			localizzaType.addLocalizzaInfo(localizzaInfoType[0]);

			sbnrequest.setLocalizza(localizzaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (areaDatiPass.isIndice() && areaDatiPass.isPolo()
					&& areaDatiPass.isMantieniAllineamento()) {


				// Inizio intervento ALMAVIVA2 - mail Scognamiglio 12.06.2012
				//.le raccolte fattizie essendo oggetti solo locali non devono essere localizzate in Indice
				if (!areaDatiPass.getNatura().equals("R")) {
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
				}

				// L'aggiornamento ion Polo viene fatto solo per tenere allineate le informazioni relative al possesso;
				if (areaDatiPass.getTipoLoc().equals("Gestione")) {
					return areaDatiPassReturn;
				}

				sbnmessage.getSbnRequest().getLocalizza().getLocalizzaInfo(0).setTipoInfo(SbnTipoLocalizza.POSSESSO);
				sbnmessage.getSbnRequest().getLocalizza().getLocalizzaInfo(0).getT899(0).setTipoInfo(SbnTipoLocalizza.POSSESSO);
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					return areaDatiPassReturn;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3013")) {
						areaDatiPassReturn.setCodErr("0000");
						areaDatiPassReturn.setTestoProtocollo("Aggiornamento correttamente eseguito in Indice;<BR>ATTENZIONE: l'oggetto è assente dalla dati di Polo");
					} else {
						areaDatiPassReturn.setCodErr("9999");
						areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					}
				}
				return areaDatiPassReturn;
			}

			if (areaDatiPass.isPolo()) {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiPassReturn.setCodErr("noServerPol");
					return areaDatiPassReturn;
				}
				// Inizio manutenzione ALMAVIVA2 09.12.2009 - BUG MANTIS
				// 3248 - la doppia localizzazione per possesso dell'UNICUM non comporta errore bloccante; la transazione continua
				// (risposta 3252 quindi non deve essere inviata)
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
						&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3252")) {
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
			}

			if (areaDatiPass.isIndice()) {
				// Inizio intervento ALMAVIVA2 - mail Scognamiglio 12.06.2012
				//.le raccolte fattizie essendo oggetti solo locali non devono essere localizzate in Indice
				if (!areaDatiPass.getNatura().equals("R")) {
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturn.setCodErr("noServerInd");
						return areaDatiPassReturn;
					}
					String bib = this.user.getBiblioteca();

					//almaviva5_20140206 evolutive google3
					SbnMarcEsitoType esitoType = SbnMarcEsitoType.of(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					if (esitoType == SbnMarcEsitoType.SERVER_NON_DISPONIBILE) {
						//salvataggio xml per batch localizzazione massiva
						LocalizzazioneMassiva.salvaLocalizzazione(bib.substring(0, 3), bib.substring(3), sbnmessage, this.user);
					}

					// La risposta 7017 non deve essere inviata;
					// Errore :7017 Attenzione: utilizzare la funzionalità "Correggi" per modificare gli attributi del possesso.
					// sbnIdLoc errato: CUB0496948
					if (esitoType == SbnMarcEsitoType.OK || esitoType == SbnMarcEsitoType.LOCALIZZA_CORREZIONE) {
						LocalizzazioneMassiva.rimuoviLocalizzazione(bib.substring(0, 3), bib.substring(3), sbnmessage, this.user);
						return areaDatiPassReturn;
					}
					areaDatiPassReturn.setCodErr("9999");
					areaDatiPassReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPassReturn;
				}
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
	}// end localizza



	// Inizio modifica ALMAVIVA2 BUG MANTIS 3930 del 13.10.2010
	// le operazioni di inserimento localizzazioni per possesso e dati di possesso devono essere fatte con due operazioni differenti
	// inoltre si deve ciclare su tutte le biblioteche del Polo: il metodo viene completamente ridisegnato!
	// il metodo viene spostato in questa classe perchè viene richiamato sia da SbnGestioneAccorpamentoDao che da SbnGestioneTitoliDao
	// ULTERIORE modifica ALMAVIVA2 BUG MANTIS 3871 del 25.10.2010
	// si inviano sia il bid di partenza che serve per interrogare in Polo che il bid di arrivo che serve per spedire
	// in Indice i dati di localizzazione copiati; nel caso di catalogazione in Indice di elemento locale il bid di partenza
	// e di arrivo coincidono

	// Intervento interno ottobre 2015 ALMAVIVA2 che allarga le funzionalità del metodo copiaLocalizzazPoloSuIndice
	// e lo rende generalizzato copiando le localizzazioni da un oggetto ad un altro da una base dati ad un altra
	// La funzione viene rinominata copiaLocalizzazDocument
	// Valori di origineArrivo:
	// PoloIndice:  copia le localizzazioni da un oggetto di polo ad uno di Indice (INTERROGA in Polo AGGIORNA in Indice)
	// PoloPolo:  copia le localizzazioni da un oggetto di polo ad uno di polo (INTERROGA in Polo AGGIORNA in Polo)
	// IndiceIndice:  copia le localizzazioni da un oggetto di indice ad uno di indice (INTERROGA in Indice AGGIORNA in Indice)

	public String copiaLocalizzazDocumento(String bidPartenzaCopia, String bidArrivoCopia, String tipoMateriale, String userBiblioteca,
			String origineArrivo) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiLocalizzazioniAuthorityVO leggiLocalPolo = new AreaDatiLocalizzazioniAuthorityVO();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO leggiLocalPoloReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

		leggiLocalPolo.setIdLoc(bidPartenzaCopia);
		leggiLocalPolo.setTipoMat(tipoMateriale);
		leggiLocalPolo.setCodiceSbn(userBiblioteca.substring(0,3) + "   ");

		if (origineArrivo.equals("PoloIndice") || origineArrivo.equals("PoloPolo")) {
			leggiLocalPolo.setIndice(false);
			leggiLocalPolo.setPolo(true);
		} else if (origineArrivo.equals("IndiceIndice")) {
			leggiLocalPolo.setIndice(true);
			leggiLocalPolo.setPolo(false);
		} else {
			// Questo è il vecchio valore di default del metodo copiaLocalizzazPoloSuIndice
			leggiLocalPolo.setIndice(true);
			leggiLocalPolo.setPolo(false);
		}

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
		leggiLocalPoloReturn = gestioneAllAuthority.cercaLocalizzazioni(leggiLocalPolo, false);

		// se codErr = 3001 vuol dire che in polo non ci sono localizzazioni quindi in Indice non spediamo nulla
		// perchè l'unica localizzazione da inviare è quella per gestione per la Biblio che ha richiesto la fusione
		// ed è stata inserita automaticamente all'atto della cattura.

		// Modifica ALMAVIVA2 del 28.10.2010 BUG Mantis 3954 il copia local vale solo per i Documenti e si ammette
		// l'errore 3001 e 3010 che significa non trovato o non trovato id di localizzazione;
		if (leggiLocalPoloReturn.getCodErr().equals("3001") ||  leggiLocalPoloReturn.getCodErr().equals("3013")) {
			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
			areaLocalizza.setIdLoc(bidArrivoCopia);
			areaLocalizza.setTipoMat(tipoMateriale);
			areaLocalizza.setAuthority("");
			areaLocalizza.setCodiceSbn(userBiblioteca);
			areaLocalizza.setTipoOpe("Localizza");
			areaLocalizza.setTipoLoc("Gestione");


			if (origineArrivo.equals("PoloIndice") || origineArrivo.equals("IndiceIndice")) {
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
			} else if (origineArrivo.equals("PoloPolo")) {
				areaLocalizza.setIndice(false);
				areaLocalizza.setPolo(true);
			} else {
				// Questo è il vecchio valore di default del metodo copiaLocalizzazPoloSuIndice
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
			}

			areaDatiPassReturn = gestioneAllAuthority.localizzaAuthority(areaLocalizza);

			// Modifica ALMAVIVA2 del 28.10.2010 BUG Mantis 3954 il copia local vale solo per i Documenti e si ammette
			// l'errore 3001 e 3013 che significa non trovato o non trovato id di localizzazione;
			if (areaDatiPassReturn.getTestoProtocollo().contains("3001")
					|| areaDatiPassReturn.getTestoProtocollo().contains("3013")) {
				return "0000";
			} else {
				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return areaDatiPassReturn.getCodErr() + areaDatiPassReturn.getTestoProtocollo();
				}
			}
			return "0000";
		}

		if (!leggiLocalPoloReturn.getCodErr().equals("0000")) {
			return leggiLocalPoloReturn.getCodErr() + leggiLocalPoloReturn.getTestoProtocollo();
		}

		// si cicla per inserire tutte le biblioteche
		int size = leggiLocalPoloReturn.getListaSintetica().size();
		SinteticaLocalizzazioniView sinteticaLocalizzazioniView;
		for (int i = 0; i < size; i++) {
			sinteticaLocalizzazioniView = new SinteticaLocalizzazioniView();
			sinteticaLocalizzazioniView = (SinteticaLocalizzazioniView) leggiLocalPoloReturn.getListaSintetica().get(i);
			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
			areaLocalizza.setIdLoc(bidArrivoCopia);
			areaLocalizza.setTipoMat(tipoMateriale);
			areaLocalizza.setAuthority("");
			areaLocalizza.setCodiceSbn(sinteticaLocalizzazioniView.getIDSbn());
			areaLocalizza.setTipoOpe("Localizza");
			if (sinteticaLocalizzazioniView.getTipoLoc().contains("Possesso") || sinteticaLocalizzazioniView.getTipoLoc().contains("Tutti")) {
				areaLocalizza.setTipoLoc("Possesso/Gestione");
			} else {
				areaLocalizza.setTipoLoc("Gestione");
			}

			if (origineArrivo.equals("PoloIndice") || origineArrivo.equals("IndiceIndice")) {
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
			} else if (origineArrivo.equals("PoloPolo")) {
				areaLocalizza.setIndice(false);
				areaLocalizza.setPolo(true);
			} else {
				// Questo è il vecchio valore di default del metodo copiaLocalizzazPoloSuIndice
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
			}

			areaDatiPassReturn = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
			if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				return areaDatiPassReturn.getCodErr() + areaDatiPassReturn.getTestoProtocollo();
			}

			if (sinteticaLocalizzazioniView.getTipoLoc().contains("Possesso") || sinteticaLocalizzazioniView.getTipoLoc().contains("Tutti")) {
				// Si inviano gli attributi del Possesso eventualmente presenti in Polo
				areaLocalizza.setTipoOpe("Correggi");
				areaLocalizza.setC899Localizzazioni(sinteticaLocalizzazioniView.getC899Localizzazioni());
				areaLocalizza.getC899Localizzazioni().setTipoInfo(SbnTipoLocalizza.TUTTI);
				areaDatiPassReturn = gestioneAllAuthority.localizzaAuthority(areaLocalizza);
				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return areaDatiPassReturn.getCodErr() + areaDatiPassReturn.getTestoProtocollo();
				}
			}
		}

		return areaDatiPassReturn.getCodErr();
	}
	// Fine modifica ALMAVIVA2 BUG MANTIS 3930 del 13.10.2010



	public AreaDatiVariazioneReturnVO catturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturnInsert.setCodErr("0000");

		// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
		String idNewSpoglio = "";
		if (areaDatiPass.isCopiaSpoglioSenzaMadre()) {
			String esitoCopiaSpoglio="";
			esitoCopiaSpoglio = copiaSpoglioConCreazioneNuovaMadre(areaDatiPass);

			if (esitoCopiaSpoglio.length() > 4) {
				if (esitoCopiaSpoglio.startsWith("0000")) {
					idNewSpoglio=esitoCopiaSpoglio.substring(4);
				} else {
					areaDatiPassReturnInsert.setCodErr(esitoCopiaSpoglio.substring(0,4));
					areaDatiPassReturnInsert.setTestoProtocollo(esitoCopiaSpoglio.substring(4));
					return areaDatiPassReturnInsert;
				}
			}

			// se lo spoglio è condiviso si reimpostano i valori del bid da catturare con quelli del nuovo Spoglio creato
			// così da portarlo in Polo; se lo spoglio è solo locale si puo terminare il processo;
			if (areaDatiPass.isSpoglioCondiviso()) {
				areaDatiPass.setIdPadre(idNewSpoglio);
			} else {
				areaDatiPassReturnInsert.setCodErr("0000");
				areaDatiPassReturnInsert
						.setTestoProtocollo("Creazione del nuovo spoglio " + idNewSpoglio + " terminata correttamente"
								+ "<br />"
								+ " Creazione del legame a Documento " + areaDatiPass.getIdNewMadre() + " terminata correttamente");
				return areaDatiPassReturnInsert;
			}
		}
		// Fine Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova


		// ALMAVIVA2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
		// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
		// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
		// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
		String idNewMonografia = "";
		if (areaDatiPass.isCopiaReticoloConSpoglioCondivisione()) {
			String esitoCopiaReticolo="";
			esitoCopiaReticolo = copiaReticoloConSpoglioInCondivisione(areaDatiPass);

			// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
			// Intervento ALMAVIVA2 gennaio 2017
			if (!esitoCopiaReticolo.substring(0,4).equals("0000")) {
				if (!esitoCopiaReticolo.equals("")) {
					areaDatiPassReturnInsert.setCodErr(esitoCopiaReticolo.substring(0,4));
					areaDatiPassReturnInsert.setTestoProtocollo(esitoCopiaReticolo.substring(4));
					return areaDatiPassReturnInsert;
				}

				if (esitoCopiaReticolo.substring(0,4).equals("9999")) {
					areaDatiPassReturnInsert.setCodErr(esitoCopiaReticolo.substring(0,4));
					areaDatiPassReturnInsert.setTestoProtocollo(esitoCopiaReticolo.substring(4));
					return areaDatiPassReturnInsert;
				}
			}

			idNewMonografia = esitoCopiaReticolo.substring(4);
			areaDatiPass.setIdPadre(idNewMonografia);
		}
		// FINE ALMAVIVA2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:

		// Fase di accesso all'Indice per catturare il reticolo dell'oggetto


		if (areaDatiPass.getInferioriDaCatturare() != null) {
			listaInfDaCatt = areaDatiPass.getInferioriDaCatturare();
		}
		areaDatiPass.setSbnOutputTypeDaAllineare(null);
		String esito = ricercaElementiReticoloPerCattura(areaDatiPass);

		if (!esito.equals("0000")) {
			areaDatiPassReturnInsert.setCodErr("9999");
			areaDatiPassReturnInsert.setTestoProtocollo(esito
					+ " Errore durante la fase di lettura da Indice/Polo");
			return areaDatiPassReturnInsert;
		}

		// Fase di passaggio dei dati fra le aree
		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
		areaTabellaOggettiDaCatturareVO.setTabellaOggettiPerCattura(areaDatiPass.getTabellaOggettiPerCattura());
		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdPadre());
		areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getTipoAuthority());
		areaTabellaOggettiDaCatturareVO.setSoloCopia(areaDatiPass.isSoloCopia());
		areaTabellaOggettiDaCatturareVO.setCopiaReticolo(areaDatiPass.isCopiaReticolo());
		areaTabellaOggettiDaCatturareVO.setSoloRadice(areaDatiPass.isSoloRadice());
		areaTabellaOggettiDaCatturareVO.setCreaNuovoId(areaDatiPass.isCreaNuovoId());


		// Inizio Modifica Marzo 2014: passaggio del valore del campo ProvenienzaAllineamento fra le aree della cattura così
		// da consentire la corretta localizzazione o non degli oggetti in cattura/allineamento.
		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(areaDatiPass.isProvenienzaAllineamento());
		// Fine Modifica Marzo 2014

		// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
		// la cattura avviene solo per le authority selzionate con il check (come per le W)
		areaTabellaOggettiDaCatturareVO.setTicket(areaDatiPass.getTicket());

		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		areaDatiPassReturnInsert = inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);

		if (areaDatiPass.isSoloCopia() || areaDatiPass.isCopiaReticolo()) {
			return areaDatiPassReturnInsert;
		} else {
			if (areaDatiPassReturnInsert.getCodErr().equals("")
					|| areaDatiPassReturnInsert.getCodErr().equals("0000")
					&& areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {

				// Chiamata per comunicazione allineamento documenti;
				SbnMessageType sbnmessage = new SbnMessageType();
				SbnRequestType sbnrequest = new SbnRequestType();
				SBNMarc sbnRisposta;
				ComunicaAllineatiType comunicaAllineatiType = new ComunicaAllineatiType();
				AllineatiType allineatiType = new AllineatiType();

				SbnOggetto sbnOggetto = new SbnOggetto();
				if (ValidazioneDati.isFilled(areaDatiPass.getTipoAuthority())) {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAuthority()));

				} else {
					// Bug esercizio Mantis 6184 maggio 2016 ALMAVIVA2
					// nel caso delle collane il tipo materiele è Valore spazio e non vuoto quindi il controllo sul tipo
					// materiale deve tenerne conto altrimenti quando si effettua l'allineamento non si comunica l'allineamento
					// della collana dando luogo alla segnalazione : Protocollo di INDICE: 3313 Occorre allinearsi sulla notizia prima di correggerla. - In allineamento.
					//almaviva5_20140714 il tipo materiale non può essere assegnato insieme al tipo authority.

					//if (ValidazioneDati.isFilled(areaDatiPass.getTipoMateriale())) {
					// Bug esercizio Mantis 6572 aprile 2018 ALMAVIVA2
					// Anche questo controllo non va bene perchè così rimangono esclusi dallo spegnimento del flag i titoli non documento
					// come le D o le B)
					//if (ValidazioneDati.notEmpty(areaDatiPass.getTipoMateriale())) {
					if (areaDatiPass.getTipoMateriale() == null || areaDatiPass.getTipoMateriale().equals("")) {
						sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(" "));
					} else  {
						sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMateriale()));
					}
				}


				if (sbnOggetto.getTipoAuthority() != null
						|| sbnOggetto.getTipoMateriale() != null) {

					allineatiType.setTipoOggetto(sbnOggetto);
					allineatiType.setIdAllineato(areaDatiPass.getIdPadre());
					comunicaAllineatiType.addAllineati(allineatiType);

					sbnrequest.setComunicaAllineati(comunicaAllineatiType);
					sbnmessage.setSbnRequest(sbnrequest);

					try {
						this.indice.setMessage(sbnmessage, this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();

						if (sbnRisposta == null) {
							areaDatiPassReturnInsert.setCodErr("9999");
							areaDatiPassReturnInsert
									.setTestoProtocollo("Cattura terminata correttamente"
											+ "<br />"
											+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati per assenza Connessione con Sistema Centrale");
							return areaDatiPassReturnInsert;
						} else if (!sbnRisposta.getSbnMessage()
								.getSbnResponse().getSbnResult().getEsito()
								.equals("0000")) {
							areaDatiPassReturnInsert.setCodErr("9999");
							areaDatiPassReturnInsert
									.setTestoProtocollo("Cattura terminata correttamente"
											+ "<br />"
											+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati:"
											+ "<br />"
											+ sbnRisposta.getSbnMessage()
													.getSbnResponse()
													.getSbnResult().getEsito()
											+ " "
											+ sbnRisposta.getSbnMessage()
													.getSbnResponse()
													.getSbnResult()
													.getTestoEsito());
							return areaDatiPassReturnInsert;
						}
					} catch (SbnMarcException e) {
						e.printStackTrace();
						areaDatiPassReturnInsert.setCodErr("9999");
						areaDatiPassReturnInsert.setTestoProtocollo("ERROR >>"
								+ e.getMessage());
					}
				}
			}
		}

		return areaDatiPassReturnInsert;
	}


	// Inizio Agosto 2015: Evolutiva Copia reticolo Spoglio da legare a M nuova
	// a questo punto si ricerca nel reticolo il legame alla madre (M o S) e si cancella/sostituisce con l'altro
	public String copiaSpoglioConCreazioneNuovaMadre(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {


		String esito = "0000";

		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;
		DocumentoType documentoType = null;

		String idNuovoSpoglio = "";

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

			cercaDatiTitTypeChoice.setT001(areaDatiPass.getIdPadre());
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			// CHIAMATA AL PROTOCOLLO di polo o indice in base alla tipologia di spoglio Condiviso o no che deve creare
			if (areaDatiPass.isSpoglioCondiviso()) {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "NoSeverInd";
				}
			} else {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "NoSeverPol";
				}
			}


			// Inizio Prova per funzione di Copia Reticolo
			String xml = null;
			if (sbnRisposta != null) {
				try {
					StringWriter stringWriter = new StringWriter();
					// 1- Validate
					sbnRisposta.validate();
					// 2- Marshall
					sbnRisposta.marshal(stringWriter);
					xml = stringWriter.toString();
					stringWriter.close();

				} catch (org.exolab.castor.xml.ValidationException x) {
					x.printStackTrace();
					throw x;
				} catch (Exception x) {
					x.printStackTrace();
					throw x;
				}
			}

			AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
			areaDatiPassGetIdSbn.setTipoMat(areaDatiPass.getTipoMateriale());
			areaDatiPassGetIdSbn.setTipoRec(null);
			areaDatiPassGetIdSbn = getIdSbn(areaDatiPassGetIdSbn);
			if (areaDatiPassGetIdSbn.getIdSbn() == null || areaDatiPassGetIdSbn.getIdSbn().equals("")) {
				esito = areaDatiPassGetIdSbn.getCodErr() + areaDatiPassGetIdSbn.getTestoProtocollo();
				return esito;
			}

			String xmlNew = xml.replaceAll(areaDatiPass.getIdPadre(), areaDatiPassGetIdSbn.getIdSbn());
			StringReader stringReader = new StringReader(xmlNew);
			try {
				sbnRisposta = SBNMarc.unmarshalSBNMarc(stringReader);
			} catch (MarshalException e) {
				throw e;
			} finally {
				stringReader.close();
			}
			idNuovoSpoglio = areaDatiPassGetIdSbn.getIdSbn();
			// Fine Prova per funzione di Copia Reticolo


			SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			sbnOutPut = sbnResponseChoice.getSbnOutput();

			if (sbnOutPut.getDocumentoCount() > 0) {
				documentoType = sbnOutPut.getDocumento(0);
				
				// MANUTENZIONE migliorativa almaviva2 ottobre 2019
				// al nuovo spoglio va assegnato il livello autorità del bibliotecario e non quello dello spoglio
				// da cui e' copiato
				documentoType.getDocumentoTypeChoice().getDatiDocumento().setLivelloAutDoc(SbnLivello.valueOf(areaDatiPass.getLivAutUtente()));


				if (areaDatiPass.isSpoglioCondiviso()) {
					documentoType.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.S);
				} else {
					documentoType.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);
				}
				if (documentoType.getLegamiDocumentoCount() > 0) {
					int count = documentoType.getLegamiDocumentoCount();
					for (int i = 0; i < count; i++) {
						for (int j = 0; j < documentoType.getLegamiDocumento(i).getArrivoLegameCount(); j++) {
							if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc() != null) {
								if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getTipoLegame().toString().equals("461")) {
									
									if (areaDatiPass.isSpoglioCondiviso()) {
										documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
									} else {
										documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.N);
									}
									documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().setDocumentoLegato(null);
									documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().setIdArrivo(areaDatiPass.getIdNewMadre());
									documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().setNoteLegame("");
								}
							}
						}
					}
				} else {
					// Modifica settembre 2015 ALMAVIVA2: se lo spoglio non ha legami non è possibile slegarlo
					// dalla madre, viene quindi creato il legame ex novo

					LegamiType legamiType;

					ArrivoLegame arrivoLegame;
					arrivoLegame = new ArrivoLegame();

					LegameDocType legameDocType;
					legameDocType = new LegameDocType();
					legameDocType.setTipoLegame(SbnLegameDoc.valueOf("461"));
					if (areaDatiPass.isSpoglioCondiviso()) {
						legameDocType.setCondiviso(LegameDocTypeCondivisoType.S);
					} else {
						legameDocType.setCondiviso(LegameDocTypeCondivisoType.N);
					}
					legameDocType.setDocumentoLegato(null);
					legameDocType.setIdArrivo(areaDatiPass.getIdNewMadre());
					legameDocType.setNoteLegame("");

					arrivoLegame.setLegameDoc(legameDocType);

					legamiType = new LegamiType();
					legamiType.setIdPartenza(areaDatiPassGetIdSbn.getIdSbn());
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
					legamiType.addArrivoLegame(arrivoLegame);

					documentoType.addLegamiDocumento(legamiType);

				}
				sbnOutPut.setDocumento(0, documentoType);
			}



			CreaType creaType = null;
			creaType = new CreaType();
			CreaTypeChoice creaTypeChoice = null;
			creaTypeChoice = new CreaTypeChoice();
			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			creaType.setTipoControllo(SbnSimile.CONFERMA);

			sbnrequest.setCerca(null);
			sbnrequest.setCrea(creaType);
			sbnmessage.setSbnRequest(sbnrequest);



			// CHIAMATA AL PROTOCOLLO di polo o indice in base alla tipologia di spoglio Condiviso o no che deve creare
			if (areaDatiPass.isSpoglioCondiviso()) {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "NoSeverInd";
				}
			} else {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "NoSeverPol";
				}
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				esito = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() +
				sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
				return esito;
			}

			// evolutive ottobre 2015 ALMAVIVA2 - a seguito di creazione/copia di uno spoglio su madre già collocata
			// viene estesa alla N nuova la localizzazione per possesso della madre
			SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
			if (areaDatiPass.isSpoglioCondiviso()) {
				String origineArrivo = "IndiceIndice";
				String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
						areaDatiPass.getIdNewMadre(), idNuovoSpoglio, SbnMateriale.valueOf("M").toString(),
						sbnRisposta.getSbnUser().getBiblioteca(),
						origineArrivo);
				if (!codStringaErrore.substring(0,4).equals("0000")) {
					esito = codStringaErrore;
					return esito;
				}
			} else {
				String origineArrivo = "PoloPolo";
				String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
						areaDatiPass.getIdNewMadre(), idNuovoSpoglio, SbnMateriale.valueOf("M").toString(),
						sbnRisposta.getSbnUser().getBiblioteca(),
						origineArrivo);
				if (!codStringaErrore.substring(0,4).equals("0000")) {
					esito = codStringaErrore;
					return esito;
				}
			}

			if (esito.equals("0000")) {
				esito = esito + idNuovoSpoglio;
			}


		} catch (SbnMarcException ve) {
			esito = "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			esito = "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			esito = "ERROR >>" + e.getMessage();
		}

		return esito;

	}




	// ALMAVIVA2: Inizio Ottobre 2016: Evolutiva Copia reticolo Spoglio Condiviso:
	// L'evolutiva comporta la creazione di una nuova Monografia in tutto identica a quella di partenza con tutti
	// gli elementi del reticolo in cui solo i titolo analitici N saranno nuovi; gli altri saranno gli stessi di prima;
	// la Creazione in oggetto verrà fatta in indice e poi catturata in polo;
	public String copiaReticoloConSpoglioInCondivisione(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {
		String esito = "0000";

		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;
		DocumentoType documentoType = null;

		String idNewMonografia = "";
		String xmlNew = "";

		try {

			SbnMessageType sbnMessage = new SbnMessageType();
			SbnRequestType sbnRequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setNumPrimo(1);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1

			String spoglioDiRicerca = areaDatiPass.getInferioriDaCatturare()[0];
			String monografiaDiRicerca = areaDatiPass.getIdPadre();

			cercaDatiTitTypeChoice.setT001(spoglioDiRicerca);
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnMessage.setSbnRequest(sbnRequest);
			sbnRequest.setCerca(cercaType);

			this.indice.setMessage(sbnMessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				return "NoSeverInd";
			}

			// Ricerca sul messaggio del legame dello Spoglio alla monografia, estrazione del DocumentoType della Monografia che servirà per
			// crearla in Indice con tutti i suoi legami ESCLUSI gli spogli che saranno creati poi, contestualmente al nuovo legame
			sbnMessage = sbnRisposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			sbnOutPut = sbnResponseChoice.getSbnOutput();
			documentoType = sbnOutPut.getDocumento(0);

			if (documentoType.getLegamiDocumentoCount() > 0) {
				int count = documentoType.getLegamiDocumentoCount();
				for (int i = 0; i < count; i++) {
					for (int j = 0; j < documentoType.getLegamiDocumento(i).getArrivoLegameCount(); j++) {
						if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc() != null) {
							if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getTipoLegame().toString().equals("461")
									&& documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getIdArrivo().equals(monografiaDiRicerca)) {
								documentoType = documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getDocumentoLegato();
								break;
							}
						}
					}
				}
			}

			// INIZIO:Inviamo a Indice la richiesta di CREAZIONE della nuova Monografia e di tutti i suoi legami
			CreaType creaType = null;
			creaType = new CreaType();
			CreaTypeChoice creaTypeChoice = null;
			creaTypeChoice = new CreaTypeChoice();
			// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
			// Intervento ALMAVIVA2 gennaio 2017
			documentoType.getDocumentoTypeChoice().getDatiDocumento().setLivelloAutDoc(SbnLivello.valueOf(areaDatiPass.getLivAutUtente()));
			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			creaType.setTipoControllo(SbnSimile.CONFERMA);

			sbnRequest.setCerca(null);
			sbnRequest.setCrea(creaType);
			sbnMessage.setSbnRequest(sbnRequest);
			sbnMessage.setSbnResponse(null);

			String xml = null;
			if (sbnMessage != null) {
				try {
					StringWriter stringWriter = new StringWriter();
					// 1- Validate
					sbnMessage.validate();
					// 2- Marshall
					sbnMessage.marshal(stringWriter);
					xml = stringWriter.toString();
					stringWriter.close();

				} catch (org.exolab.castor.xml.ValidationException x) {
					x.printStackTrace();
					throw x;
				} catch (Exception x) {
					x.printStackTrace();
					throw x;
				}
			}

			AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
			areaDatiPassGetIdSbn.setTipoMat(areaDatiPass.getTipoMateriale());
			areaDatiPassGetIdSbn.setTipoRec(null);
			areaDatiPassGetIdSbn = getIdSbn(areaDatiPassGetIdSbn);
			if (areaDatiPassGetIdSbn.getIdSbn() == null || areaDatiPassGetIdSbn.getIdSbn().equals("")) {
				esito = areaDatiPassGetIdSbn.getCodErr() + areaDatiPassGetIdSbn.getTestoProtocollo();
				return esito;
			}

			idNewMonografia = areaDatiPassGetIdSbn.getIdSbn();
			// Assegnazione del nuovo bid alla nuova monografia
			xmlNew = xml.replaceAll(monografiaDiRicerca, idNewMonografia);

			// Si riporta l'XML sul sbnOutPut che poi verrà modificato e inviato all'Indice per la creazione del nuovo reticolo
			StringReader stringReader = new StringReader(xmlNew);
			try {
				sbnMessage = SbnMessageType.unmarshalSbnMessageType(stringReader);
			} catch (MarshalException e) {
				throw e;
			} finally {
				stringReader.close();
			}

			this.indice.setMessage(sbnMessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				return "NoSeverInd";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				esito = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() +
				sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
				return esito;
			}

			// Chiamata al metodo che interroga la vecchia monografia e ricrea tutti i titoli analitici ricreandoli
			// legati alla nuova monografia appena creata
			String[] listaInferiori = creazioneCopiaTitoliAnaliticiConLegame(monografiaDiRicerca, idNewMonografia, areaDatiPass.getLivAutUtente());

			int listaInferioriCount = listaInferiori.length;
			String[] listaInferioriNew = new String[listaInferioriCount];
			for (int i = 0; i < listaInferioriCount; i++) {
				if (listaInferiori[i].startsWith("OK")) {
					listaInferioriNew[i] = listaInferiori[i].substring(2);
				} else {
					esito = "9999" + listaInferiori[i].substring(2);
				}
			}
			areaDatiPass.setInferioriDaCatturare(listaInferioriNew);

			if (esito.equals("0000")) {
				esito = "0000" + idNewMonografia;
			}

		} catch (SbnMarcException ve) {
			esito = "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			esito = "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			esito = "ERROR >>" + e.getMessage();
		}

		return esito;

	}
	public String[] creazioneCopiaTitoliAnaliticiConLegame(String monogDaCopiare, String monogNuova, String livAutUtente) {


		String esito = "0000";
		HashSet<String> tmp = new HashSet<String>();
		AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();

		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;
		DocumentoType documentoType = null;

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

			cercaDatiTitTypeChoice.setT001(monogDaCopiare);
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				tmp.add("KO" + "NoSeverInd");
				esito = "9999";
			}


			// Ricerca sul messaggio di tutti i legami con titoli Analitici (N) al fine di creare tutti i nuovi BID

			SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
			sbnOutPut = sbnResponseChoice.getSbnOutput();
			documentoType = sbnOutPut.getDocumento(0);

			if (documentoType.getLegamiDocumentoCount() > 0) {
				String idNewAnalitico = "";
				String idOldAnalitico = "";
				int count = documentoType.getLegamiDocumentoCount();
				for (int i = 0; i < count; i++) {
					for (int j = 0; j < documentoType.getLegamiDocumento(i).getArrivoLegameCount(); j++) {
						if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc() != null) {
							if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getTipoLegame().toString().equals("464")) {
								idOldAnalitico = documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getIdArrivo();
								// il legame in esame è di tipo M464N quindi va creato il nuovo bid per il nuovo titolo analitico e
								// sostituito al vecchio identificativo nell'XML da ricaricare
								// INIZIO:Inviamo a Indice la richiesta di CREAZIONE del singolo titolo analitico
								CreaType creaType = null;
								creaType = new CreaType();
								CreaTypeChoice creaTypeChoice = null;
								creaTypeChoice = new CreaTypeChoice();

								creaTypeChoice.setDocumento(documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getDocumentoLegato());

								// il legame in esame è di tipo M464N quindi va creato il nuovo bid per il nuovo titolo analitico e
								// sostituito al vecchio identificativo nell'XML da ricaricare
								areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
								areaDatiPassGetIdSbn.setTipoMat("U");
								areaDatiPassGetIdSbn.setTipoRec(null);
								areaDatiPassGetIdSbn = getIdSbn(areaDatiPassGetIdSbn);
								if (areaDatiPassGetIdSbn.getIdSbn() == null || areaDatiPassGetIdSbn.getIdSbn().equals("")) {
									esito = areaDatiPassGetIdSbn.getCodErr() + areaDatiPassGetIdSbn.getTestoProtocollo();
									tmp.add("KO" + esito);
									esito = "9999";
								}
								idNewAnalitico = areaDatiPassGetIdSbn.getIdSbn();


								creaTypeChoice.getDocumento().getDocumentoTypeChoice().getDatiDocumento().setT001(idNewAnalitico);

								// MAIL Scognamiglio del 03.01.2017 Collaudo nuova funzionalità copia reticolo con spogli
								// Intervento ALMAVIVA2 gennaio 2017
								creaTypeChoice.getDocumento().getDocumentoTypeChoice().getDatiDocumento().setLivelloAutDoc(SbnLivello.valueOf(livAutUtente));

								// creazione del legame alla Monografia creata precedentemente
								//LegamiType[] arrayLegamiType = new LegamiType[1];
								LegamiType legamiType;
								ArrivoLegame arrivoLegame;

								legamiType = new LegamiType();

								legamiType.setIdPartenza(idNewAnalitico);
								legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
								arrivoLegame = new ArrivoLegame();
								LegameDocType legameDocType = new LegameDocType();
								legameDocType.setTipoLegame(SbnLegameDoc.valueOf("461"));
								legameDocType.setNoteLegame(documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getNoteLegame());

								// Mail scognamiglio 26.01.2017 - non viene copiato il numero di sequenza
								legameDocType.setSequenza(documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc().getSequenza());

								legameDocType.setIdArrivo(monogNuova);

								legameDocType.setDocumentoLegato(null);
								arrivoLegame.setLegameDoc(legameDocType);

								ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
								arrayArrivoLegame[0] = arrivoLegame;

								legamiType.setArrivoLegame(arrayArrivoLegame);
								//arrayLegamiType[0] = legamiType;

								// aggiungo IL LEGAME
								//creaTypeChoice.getDocumento().setLegamiDocumento(arrayLegamiType);
								creaTypeChoice.getDocumento().addLegamiDocumento(legamiType);

								creaType.setCreaTypeChoice(creaTypeChoice);
								creaType.setTipoControllo(SbnSimile.CONFERMA);

								sbnrequest.setCerca(null);
								sbnrequest.setCrea(creaType);
								sbnmessage.setSbnRequest(sbnrequest);

								// Modifica di tutti gli oggetti che fanno riferimento al vecchio idanalitico con il nuovo
								String xml = null;
								if (sbnMessage != null) {
									try {
										StringWriter stringWriter = new StringWriter();
										// 1- Validate
										sbnMessage.validate();
										// 2- Marshall
										sbnMessage.marshal(stringWriter);
										xml = stringWriter.toString();
										stringWriter.close();

									} catch (org.exolab.castor.xml.ValidationException x) {
										x.printStackTrace();
										throw x;
									} catch (Exception x) {
										x.printStackTrace();
										throw x;
									}
								}

								xml = xml.replaceAll(idOldAnalitico, idNewAnalitico);

								// Si riporta l'XML sul sbnOutPut che poi verrà modificato e inviato all'Indice per la creazione del nuovo reticolo
								StringReader stringReader = new StringReader(xml);
								try {
									sbnMessage = SbnMessageType.unmarshalSbnMessageType(stringReader);
								} catch (MarshalException e) {
									throw e;
								} finally {
									stringReader.close();
								}


								this.indice.setMessage(sbnmessage, this.user);
								sbnRisposta = this.indice.eseguiRichiestaServer();
								if (sbnRisposta == null) {
									tmp.add("KO" + "NoSeverInd");
									esito = "9999";
								}

								if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
										&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
									esito = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() +
									sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
									tmp.add("KO" + esito);
									esito = "9999";
								}
								// FINE

								// nell'apposita tabella degli inferiori da catturare così che insieme alla M vengano
								// catturati in Polo anche tutte le nuove N create
								tmp.add("OK" + idNewAnalitico);
							}
						}
					}
				}
			}

			int size = tmp.size();
			if (size > 0) {
				String[] listaInferiori = new String[size];
				listaInferiori = tmp.toArray(listaInferiori);
				return listaInferiori;
			} else {
				return null;
			}

		} catch (SbnMarcException ve) {
			esito = "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			esito = "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			esito = "ERROR >>" + e.getMessage();
		}

		return null;
	}


	public String ricercaElementiReticoloPerCattura(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {


		String esito = "0000";

		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;
		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = null;

		try {

			if (areaDatiPass.getSbnOutputTypeDaAllineare() == null) {
				SbnMessageType sbnmessage = new SbnMessageType();
				SbnRequestType sbnrequest = new SbnRequestType();
				CercaType cercaType = new CercaType();
				CercaTitoloType cercaTitoloType = new CercaTitoloType();
				CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
				CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();

				cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				cercaType.setNumPrimo(1);
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1

				cercaDatiTitTypeChoice.setT001(areaDatiPass.getIdPadre());
				cercaDatiTitType
						.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
				cercaType.setCercaTitolo(cercaTitoloType);

				sbnmessage.setSbnRequest(sbnrequest);
				sbnrequest.setCerca(cercaType);


				// Inizio modifica ALMAVIVA2 27 maggio 2014
				// Viene attivata la richiesta ARCOBALENO -richiesta tipo 4 per avere subito le localizzazioni - per
				// effettuare la localizzazione del reticolo in allineamento, non per la biblioteca operante che potrebbe
				// anche non gestire il bid ma per una delle biblioteche che effettivamente lo gestiscono;
				// al momento dell'allineamento del bid radice verrà inviata la richiesta di localizzazione che l'Indice
				// automaticamente esploderà a tutto il reticolo; in questo modo anche i nuovi elementi di reticolo
				// (autori o collane ad esempio) verranno correttamente localizzati;
				sbnmessage.getSbnRequest().getCerca().setTipoOutput(SbnTipoOutput.VALUE_3);
				// Fine modifica ALMAVIVA2 ARCOBALENO


				// CHIAMATA AL PROTOCOLLO
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "NoSeverInd";
				}

				// Inizio Prova per funzione di Copia Reticolo
				if (areaDatiPass.isCopiaReticolo()) {
					String xml = null;
					if (sbnRisposta != null) {
						try {
							StringWriter stringWriter = new StringWriter();
							// 1- Validate
							sbnRisposta.validate();
							// 2- Marshall
							sbnRisposta.marshal(stringWriter);
							xml = stringWriter.toString();
							stringWriter.close();

						} catch (org.exolab.castor.xml.ValidationException x) {
							x.printStackTrace();
							throw x;
						} catch (Exception x) {
							x.printStackTrace();
							throw x;
						}
					}

					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					areaDatiPassGetIdSbn.setTipoMat(areaDatiPass.getTipoMateriale());
					areaDatiPassGetIdSbn.setTipoRec(null);
					areaDatiPassGetIdSbn = getIdSbn(areaDatiPassGetIdSbn);
					if (areaDatiPassGetIdSbn.getIdSbn() == null || areaDatiPassGetIdSbn.getIdSbn().equals("")) {
						esito = areaDatiPassGetIdSbn.getCodErr() + areaDatiPassGetIdSbn.getTestoProtocollo();
						return esito;
					}

					String xmlNew = xml.replaceAll(areaDatiPass.getIdPadre(), areaDatiPassGetIdSbn.getIdSbn());
					StringReader stringReader = new StringReader(xmlNew);
					try {
						sbnRisposta = SBNMarc.unmarshalSBNMarc(stringReader);
					} catch (MarshalException e) {
						throw e;
					} finally {
						stringReader.close();
					}
					areaDatiPass.setIdPadre(areaDatiPassGetIdSbn.getIdSbn());
				}
				// Fine Prova per funzione di Copia Reticolo

				SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

				SbnResponseTypeChoice sbnResponseChoice = sbnResponse
						.getSbnResponseTypeChoice();
				sbnOutPut = sbnResponseChoice.getSbnOutput();

				if (areaDatiPass.isCopiaReticolo()) {
					// Inizio settaggio a non condiviso di tutti i legami fra il bid modificato e gli oggetti sotto
					if (sbnOutPut.getDocumentoCount() > 0) {
						DocumentoType documentoType = null;
						documentoType = sbnOutPut.getDocumento(0);

						if (documentoType.getLegamiDocumentoCount() > 0) {
							int count = documentoType.getLegamiDocumentoCount();
							for (int i = 0; i < count; i++) {
								for (int j = 0; j < documentoType.getLegamiDocumento(i).getArrivoLegameCount(); j++) {
									if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc() != null) {
										documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameDoc()
												.setCondiviso(LegameDocTypeCondivisoType.N);
									} else if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameElementoAut() != null) {
										documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameElementoAut()
												.setCondiviso(LegameElementoAutTypeCondivisoType.N);
									} else if (documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameTitAccesso() != null) {
										documentoType.getLegamiDocumento(i).getArrivoLegame(j).getLegameTitAccesso()
												.setCondiviso(LegameTitAccessoTypeCondivisoType.N);
									}
								}
							}
						}
						sbnOutPut.setDocumento(0, documentoType);
					}
					// Fine settaggio a non condiviso di tutti i legami fra il bid modificato e gli oggetti sotto

				}

			} else {
				sbnOutPut = areaDatiPass.getSbnOutputTypeDaAllineare();
			}

			ElementAutType elementoAut = null;
			DocumentoType documentoType = null;

			if (sbnOutPut.getDocumentoCount() > 0) {
				documentoType = sbnOutPut.getDocumento(0);

				if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
					DocumentoTypeChoice documentoTypeChoice = null;
					DatiDocType datiDocType = null;

					documentoTypeChoice = documentoType.getDocumentoTypeChoice();
					datiDocType = documentoTypeChoice.getDatiDocumento();
					areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
					areaDatiCatturaReticoloVO.setIdOggetto(datiDocType.getT001());

					areaDatiCatturaReticoloVO.setVersioneOggetto(datiDocType.getT005());
					areaDatiCatturaReticoloVO.setTipoAuthority("");
					areaDatiCatturaReticoloVO.setTipoMateriale(datiDocType.getTipoMateriale().toString());
					areaDatiCatturaReticoloVO.setLivAut(datiDocType.getLivelloAutDoc().toString());
					areaDatiCatturaReticoloVO.setNatura(datiDocType.getNaturaDoc().toString());

					areaDatiCatturaReticoloVO.setDatiDocType(datiDocType);
					level++;

					if (datiDocType.getNaturaDoc().toString().equals("W")
							&& documentoType.getLegamiDocumentoCount() > 0) {
						// rintraccio le informazioni per attaccarci il legame
						// con la sua superiore
						// ed inserirlo contemporaneamente (GESTIONE 51)
						// ed effettuo l'inserimento della riga nella
						// tabellaOggetti
						DocumentoType documentoTypeInf = null;
						documentoTypeInf = sbnOutPut.getDocumento(0);
						getLegameSuperiore(documentoTypeInf, null,
								areaDatiCatturaReticoloVO, areaDatiPass);
					} else {
						areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiDocType.getT001());
						areaDatiPass.getTabellaOggettiPerCattura().put("0" + datiDocType.getT001(),	areaDatiCatturaReticoloVO);
					}

				} else {
					DocumentoTypeChoice documentoTypeChoice = null;
					TitAccessoType titAccessoType = null;

					documentoTypeChoice = documentoType
							.getDocumentoTypeChoice();
					titAccessoType = documentoTypeChoice.getDatiTitAccesso();
					areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
					areaDatiCatturaReticoloVO.setIdOggetto(titAccessoType
							.getT001());

					areaDatiCatturaReticoloVO.setVersioneOggetto(titAccessoType
							.getT005());
					areaDatiCatturaReticoloVO.setTipoAuthority("");
					areaDatiCatturaReticoloVO.setTipoMateriale("");
					areaDatiCatturaReticoloVO.setLivAut(titAccessoType
							.getLivelloAut().toString());
					areaDatiCatturaReticoloVO.setNatura(titAccessoType
							.getNaturaTitAccesso().toString());

					areaDatiCatturaReticoloVO.setTitAccessoType(titAccessoType);
					level++;
					areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "
							+ titAccessoType.getT001());
					areaDatiPass.getTabellaOggettiPerCattura().put(
							"0" + titAccessoType.getT001(),
							areaDatiCatturaReticoloVO);
				}

				if (!areaDatiPass.isSoloRadice()) {
					if (documentoType.getLegamiDocumentoCount() > 0) {
						documentoType = sbnOutPut.getDocumento(0);
						getLegamiElemento(sbnOutPut, sbnRisposta,
								documentoType, null, areaDatiPass);
					}
				}
			}

			// LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
			if (sbnOutPut.getElementoAutCount() > 0) {
				elementoAut = sbnOutPut.getElementoAut(0);
				DatiElementoType datiElementoType = null;

				areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

				datiElementoType = elementoAut.getDatiElementoAut();

				areaDatiCatturaReticoloVO.setIdOggetto(datiElementoType
						.getT001());

				areaDatiCatturaReticoloVO.setVersioneOggetto(datiElementoType
						.getT005());
				areaDatiCatturaReticoloVO.setTipoAuthority(datiElementoType
						.getTipoAuthority().toString());
				areaDatiCatturaReticoloVO.setTipoMateriale("");
				areaDatiCatturaReticoloVO.setLivAut(datiElementoType
						.getLivelloAut().toString());
				areaDatiCatturaReticoloVO.setNatura("");

				areaDatiCatturaReticoloVO.setDatiElementoType(datiElementoType);
				level++;
				areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "
						+ datiElementoType.getT001());
				areaDatiPass.getTabellaOggettiPerCattura().put(
						"0" + datiElementoType.getT001(),
						areaDatiCatturaReticoloVO);

				if (!areaDatiPass.isSoloRadice()) {
					getLegamiElemento(sbnOutPut, sbnRisposta, null,
							elementoAut, areaDatiPass);
				}

			}

		} catch (SbnMarcException ve) {
			esito = "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			esito = "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			esito = "ERROR >>" + e.getMessage();
		}

		return esito;
	}

	public void getLegameSuperiore(DocumentoType documentoType,
			ElementAutType elemen,
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO,
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		DocumentoTypeChoice documentoTypeChoice = null;
		DatiDocType datiDocType = null;

		if (documentoType != null) {
			documentoTypeChoice = documentoType.getDocumentoTypeChoice();
			datiDocType = documentoTypeChoice.getDatiDocumento();
		}

		LegamiType legamiType = null;
		int count = documentoType.getLegamiDocumentoCount();

		for (int i = 0; i < count; i++) {

			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			int arrivoLegameCount = legamiType.getArrivoLegameCount();
			for (int j = 0; j < arrivoLegameCount; j++) {

				// LEGGO LEGAME ELEMENTO AUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();

				// SE E UN LEGAME DOC (Inserito dopo per W legate sia a M che a
				// C --> E IL TIPO LEGAME E' 461)

				if (legameDocType != null) {
					// Intervento Interno - ALMAVIVA2 02.12.2009 - i legami
					// W464N non devono essere riportati qui
					// perchè saranno trattati al momento delle inferiori
					// verificando la lista inferiori da catturare
					if (!legameDocType.getTipoLegame().toString().equals("464")) {
						// ========
						AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVONew = new AreaDatiCatturaReticoloVO();
						areaDatiCatturaReticoloVONew
								.setIdOggetto(areaDatiCatturaReticoloVO
										.getIdOggetto());
						areaDatiCatturaReticoloVONew
								.setVersioneOggetto(areaDatiCatturaReticoloVO
										.getVersioneOggetto());
						areaDatiCatturaReticoloVONew.setTipoAuthority("");
						areaDatiCatturaReticoloVONew
								.setTipoMateriale(areaDatiCatturaReticoloVO
										.getTipoMateriale());
						areaDatiCatturaReticoloVONew
								.setLivAut(areaDatiCatturaReticoloVO
										.getLivAut());
						areaDatiCatturaReticoloVONew
								.setNatura(areaDatiCatturaReticoloVO
										.getNatura());
						areaDatiCatturaReticoloVONew
								.setDatiDocType(areaDatiCatturaReticoloVO
										.getDatiDocType());
						// ========
						areaDatiCatturaReticoloVONew
								.setLegameDocType(legameDocType);
						areaDatiCatturaReticoloVONew.setIdOggetto(legameDocType
								.getIdArrivo());
						areaDatiCatturaReticoloVONew.setNotaEsplicativa("ID: "
								+ datiDocType.getT001());
						// areaDatiPass.getTabellaOggettiPerCattura().put("9" +
						// "0" + (Integer.valueOf(j)).toString() +
						// datiDocType.getT001(), areaDatiCatturaReticoloVONew);
						areaDatiPass.getTabellaOggettiPerCattura().put(
								"7" + "0" + (Integer.valueOf(j)).toString()
										+ datiDocType.getT001(),
								areaDatiCatturaReticoloVONew);
					}
				}
			}
		}
		return;
	}

	public void getLegamiElemento(SbnOutputType sbnOutPut, SBNMarc sbnMarcType,
			DocumentoType documentoType, ElementAutType elemen,
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
		String keyLegame = "";
		String notaEsplicativa = "";

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

				// LEGGO LEGAME ELEMENTO AUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
				areaDatiCatturaReticoloVO.setIdOggetto(legamiType
						.getIdPartenza());

				if (elemen != null) {
					areaDatiCatturaReticoloVO.setVersioneOggetto(elemen
							.getDatiElementoAut().getT005());
					areaDatiCatturaReticoloVO
							.setTipoAuthority(elemen.getDatiElementoAut()
									.getTipoAuthority().toString());
					areaDatiCatturaReticoloVO.setTipoMateriale("");
					areaDatiCatturaReticoloVO.setLivAut(elemen
							.getDatiElementoAut().getLivelloAut().toString());
					areaDatiCatturaReticoloVO.setNatura("");
				} else {
					if (documentoType.getDocumentoTypeChoice()
							.getDatiDocumento() != null) {
						areaDatiCatturaReticoloVO
								.setVersioneOggetto(documentoType
										.getDocumentoTypeChoice()
										.getDatiDocumento().getT005());
						areaDatiCatturaReticoloVO.setTipoAuthority("");
						areaDatiCatturaReticoloVO
								.setTipoMateriale(documentoType
										.getDocumentoTypeChoice()
										.getDatiDocumento().getTipoMateriale()
										.toString());
						areaDatiCatturaReticoloVO.setLivAut(documentoType
								.getDocumentoTypeChoice().getDatiDocumento()
								.getLivelloAutDoc().toString());
						areaDatiCatturaReticoloVO.setNatura(documentoType
								.getDocumentoTypeChoice().getDatiDocumento()
								.getNaturaDoc().toString());
					} else {
						areaDatiCatturaReticoloVO
								.setVersioneOggetto(documentoType
										.getDocumentoTypeChoice()
										.getDatiTitAccesso().getT005());
						areaDatiCatturaReticoloVO.setTipoAuthority("");
						areaDatiCatturaReticoloVO.setTipoMateriale("");
						areaDatiCatturaReticoloVO.setLivAut(documentoType
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getLivelloAut().toString());
						areaDatiCatturaReticoloVO.setNatura(documentoType
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getNaturaTitAccesso().toString());
					}
				}

				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {
					// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
					// la cattura avviene solo per le authority selzionate con il check (come per le W)
//					if (legameElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE
//							|| legameElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE
//							|| legameElemento.getTipoAuthority().getType() == SbnAuthority.DE_TYPE) {
//						break;
//					}
					if (legameElemento.getTipoAuthority().getType() == SbnAuthority.DE_TYPE) {
						break;
					}

					if (legameElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE
							|| legameElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
						boolean trovato = false;
						int tappo = 0;
						if (listaInfDaCatt != null) {
							tappo = listaInfDaCatt.length;
						}
						for (int iCat = 0; iCat < tappo; iCat++) {
							if (legameElemento.getIdArrivo().equals(listaInfDaCatt[iCat])) {
								trovato = true;
								break;
							}
						}
						if (!trovato) {
							continue;
						}
					}
					// FINE ALMAVIVA2 Evolutiva Maggio 2017


					level++;

					String appoRelatorCode = "";
					if (legameElemento.getRelatorCode() != null) {
						appoRelatorCode = legameElemento.getRelatorCode();
					}

					keyLegame = "8" + legamiType.getIdPartenza()
							+ legameElemento.getIdArrivo()
							+ legameElemento.getTipoLegame() + appoRelatorCode;
					notaEsplicativa = "Legame "
							+ legameElemento.getTipoLegame() + " fra ID:  "
							+ legamiType.getIdPartenza() + " e ID: "
							+ legameElemento.getIdArrivo();

					areaDatiCatturaReticoloVO
							.setLegameElementoAutType(legameElemento);
					if (legameElemento.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
						// Nulla da fare !!
					} else if (legameElemento.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType()) {

			         	//Inizio intervento ALMAVIVA2  - BUG Mantis 4515 collaudo. si inserisce il controllo anche per
						// Authority UM che può avere legame a repertori.
			         	// Intervento ALMAVIVA2  - BUG Mantis 5566 esercizio. si inserisce il controllo anche per
						// Authority TU che può avere legame a repertori. ALMAVIVA2
						// evolutive ottobre 2015 ALMAVIVA2 -  Nella gestione dei luoghi viene data la possibilità
						// di gestire i campi nota informativa , nota catalogatore e legame a repertori

						if (elemen.getDatiElementoAut().getTipoAuthority().toString().equals("MA")
								|| elemen.getDatiElementoAut().getTipoAuthority().toString().equals("AU")
								|| elemen.getDatiElementoAut().getTipoAuthority().toString().equals("UM")
								|| elemen.getDatiElementoAut().getTipoAuthority().toString().equals("TU")
								|| elemen.getDatiElementoAut().getTipoAuthority().toString().equals("LU")) {
							// padre è una marca con legame attuale 810 (Marca-repertori)
							// Nel caso di repertori agganciati a forme di rinvio si tralascia di portarli in Polo
							// perchè sono residui di uno scambio forma;
							if (elemen.getDatiElementoAut().getFormaNome() != null
									&& elemen.getDatiElementoAut().getFormaNome().getType() == SbnFormaNome.R_TYPE) {
								// Nulla da fare !!
							} else {
								areaDatiCatturaReticoloVO.setDatiElementoType(elemen.getDatiElementoAut());
								areaDatiCatturaReticoloVO.setLegamiType(legamiType);

								keyLegame = "1" + legamiType.getIdPartenza() + legameElemento.getIdArrivo() + legameElemento.getTipoLegame();
								areaDatiCatturaReticoloVO
										.setNotaEsplicativa("Legame "
												+ legameElemento.getTipoLegame()
												+ " fra ID:  "
												+ legamiType.getIdPartenza()
												+ " e ID: "
												+ legameElemento.getIdArrivo());

								areaDatiPass.getTabellaOggettiPerCattura().put(keyLegame, areaDatiCatturaReticoloVO);
					         	// Intervento ALMAVIVA2  - BUG Mantis 5566 esercizio. PROVA ELIMINAZIONE BREAK
								//break;
							}
						}
					} else if (legameElemento.getTipoLegame().getType() == SbnLegameAut.valueOf("921").getType()) {
						// padre è una marca o un autore con legame attuale 921 che le lega
						// si deve verificare che il legame non sia già stato inserito in modo inverso
						// per effettuare due volte lo stesso inserimento

						appoRelatorCode = "";
						if (legameElemento.getRelatorCode() != null) {
							appoRelatorCode = legameElemento.getRelatorCode();
						}

						String keyRicerca = "8" + legameElemento.getIdArrivo()
								+ legamiType.getIdPartenza()
								+ legameElemento.getTipoLegame()
								+ appoRelatorCode;

						if (areaDatiPass.getTabellaOggettiPerCattura().containsKey(keyRicerca)) {
							// niente da fere il legame inverso è stato gia inserito
						} else {
							areaDatiCatturaReticoloVO.setNotaEsplicativa(notaEsplicativa);
							areaDatiPass.getTabellaOggettiPerCattura().put(keyLegame, areaDatiCatturaReticoloVO);
						}
					} else {
						String appoggioLevel = String.valueOf(level);
						if (appoggioLevel.length() == 0) {
							appoggioLevel = "00";
						} else if (appoggioLevel.length() == 1) {
							appoggioLevel = "0" + appoggioLevel;
						}

						appoRelatorCode = "";
						if (legameElemento.getRelatorCode() != null) {
							appoRelatorCode = legameElemento.getRelatorCode();
						}

						keyLegame = "8"
								+ appoggioLevel // String.valueOf(level)
								+ legamiType.getIdPartenza()
								+ legameElemento.getIdArrivo()
								+ legameElemento.getTipoLegame()
								+ appoRelatorCode;

						areaDatiCatturaReticoloVO.setNotaEsplicativa("Legame "
								+ legameElemento.getTipoLegame() + " fra ID:  "
								+ legamiType.getIdPartenza() + " e ID: "
								+ legameElemento.getIdArrivo());
						areaDatiPass.getTabellaOggettiPerCattura().put(
								keyLegame, areaDatiCatturaReticoloVO);
					}

					legameElemento.getTipoLegame().toString();
					elemen2 = legameElemento.getElementoAutLegato();

					if ((elemen2 != null) && (elemen2.getLegamiElementoAutCount() > 0)) {
						DatiElementoType datiElemento = elemen2.getDatiElementoAut();
						areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

						areaDatiCatturaReticoloVO.setIdOggetto(datiElemento.getT001());
						areaDatiCatturaReticoloVO.setVersioneOggetto(datiElemento.getT005());
						areaDatiCatturaReticoloVO.setTipoAuthority(datiElemento.getTipoAuthority().toString());
						areaDatiCatturaReticoloVO.setTipoMateriale("");
						areaDatiCatturaReticoloVO.setLivAut(datiElemento.getLivelloAut().toString());
						areaDatiCatturaReticoloVO.setNatura("");

						areaDatiCatturaReticoloVO.setDatiElementoType(datiElemento);
						if ((datiElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE
								|| datiElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
								&& datiElemento.getFormaNome().getType() ==	SbnFormaNome.R_TYPE) {

							LegameElementoAutType legameElementoAutTypeAppoggio = new LegameElementoAutType();
							legameElementoAutTypeAppoggio = legameElemento;
							if (legameElementoAutTypeAppoggio.getElementoAutLegato().getLegamiElementoAutCount() > 0) {
								legameElementoAutTypeAppoggio.getElementoAutLegato().clearLegamiElementoAut();
								areaDatiCatturaReticoloVO.setLegameElementoAutType(legameElementoAutTypeAppoggio);
							} else {
								areaDatiCatturaReticoloVO.setLegameElementoAutType(legameElementoAutTypeAppoggio);
							}

							areaDatiCatturaReticoloVO.setIdOggetto(legamiType.getIdPartenza());
							areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: " + datiElemento.getT001());
							areaDatiPass.getTabellaOggettiPerCattura().put("5" + datiElemento.getT001(), areaDatiCatturaReticoloVO);
						} else if (datiElemento.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
							// nessuna operazione da effettuare
						} else {
							areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: " + datiElemento.getT001());
							areaDatiPass.getTabellaOggettiPerCattura().put("1" + datiElemento.getT001(), areaDatiCatturaReticoloVO);
						}

						// salto i repertori
						if (datiElemento.getTipoAuthority().getType() != SbnAuthority.RE_TYPE) {
							getLegamiElemento(sbnOutPut, sbnMarcType, documentoType2, elemen2, areaDatiPass);
						}
					} else {
						if (elemen2 != null) {
							// e' un tipo authority foglia del albero
							DatiElementoType datiElemento = elemen2.getDatiElementoAut();
							areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

							areaDatiCatturaReticoloVO.setIdOggetto(datiElemento.getT001());
							areaDatiCatturaReticoloVO.setVersioneOggetto(datiElemento.getT005());
							areaDatiCatturaReticoloVO.setTipoAuthority(datiElemento.getTipoAuthority().toString());
							areaDatiCatturaReticoloVO.setTipoMateriale("");
							areaDatiCatturaReticoloVO.setLivAut(datiElemento.getLivelloAut().toString());
							areaDatiCatturaReticoloVO.setNatura("");

							if (datiElemento.getFormaNome() == null) {
								datiElemento.setFormaNome(SbnFormaNome.A);
							}
							areaDatiCatturaReticoloVO.setDatiElementoType(datiElemento);

							if ((datiElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE
									|| datiElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
									&& datiElemento.getFormaNome().getType() == SbnFormaNome.R_TYPE) {
								LegameElementoAutType legameElementoAutTypeAppoggio = new LegameElementoAutType();
								legameElementoAutTypeAppoggio = legameElemento;
								if (legameElementoAutTypeAppoggio.getElementoAutLegato().getLegamiElementoAutCount() > 0) {
									legameElementoAutTypeAppoggio.getElementoAutLegato().clearLegamiElementoAut();
									areaDatiCatturaReticoloVO.setLegameElementoAutType(legameElementoAutTypeAppoggio);
								} else {
									areaDatiCatturaReticoloVO.setLegameElementoAutType(legameElementoAutTypeAppoggio);
								}
								areaDatiCatturaReticoloVO.setIdOggetto(legamiType.getIdPartenza());
								areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: " + datiElemento.getT001());
								areaDatiPass.getTabellaOggettiPerCattura().put("5" + datiElemento.getT001(), areaDatiCatturaReticoloVO);
							} else {
								areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: " + datiElemento.getT001());
								areaDatiPass.getTabellaOggettiPerCattura().put("1" + datiElemento.getT001(), areaDatiCatturaReticoloVO);
							}
						}
					}
				} else
				// SE E UN LEGAME DOC
				if (legameDocType != null) {
					level++;
					keyLegame = "8" + legamiType.getIdPartenza()+ legameDocType.getIdArrivo()+ legameDocType.getTipoLegame();

					notaEsplicativa = "Legame " + legameDocType.getTipoLegame()
							+ " fra ID:  " + legamiType.getIdPartenza()
							+ " e ID: " + legameDocType.getIdArrivo();

					areaDatiCatturaReticoloVO.setLegameDocType(legameDocType);
					// Trattamento dei legami 51
					boolean trovato = false;
					if (legameDocType.getTipoLegame().toString().equals("463")
							|| legameDocType.getTipoLegame().toString().equals("464")) {
						int tappo = 0;
						if (listaInfDaCatt != null) {
							tappo = listaInfDaCatt.length;
						}
						for (int iCat = 0; iCat < tappo; iCat++) {
							if (legameDocType.getIdArrivo().equals(listaInfDaCatt[iCat])) {
								trovato = true;
								break;
							}
						}
					} else {
						trovato = true;
					}
					if (trovato) {
						// Modifica ALMAVIVA2 21 genn 2009 per gestione
						// cattura spogli
						//almaviva5_20120223 questo controllo è inutile
						//if (legameDocType.getTipoLegame().getType() != SbnLegameAut.valueOf("4XX").getType()
						if (legameDocType.getTipoLegame().getType() != SbnLegameDoc.valueOf("463").getType()
								&& legameDocType.getTipoLegame().getType() != SbnLegameDoc.valueOf("464").getType()) {

							// Modifica secca per legame fra una W ed una
							// collana di tipo 410
							if (areaDatiCatturaReticoloVO.getNatura().equals("W")
									&& legameDocType.getTipoLegame().getType() == SbnLegameDoc.valueOf("410").getType()) {
								areaDatiCatturaReticoloVO.setNotaEsplicativa(notaEsplicativa);
								areaDatiPass.getTabellaOggettiPerCattura().put(keyLegame, areaDatiCatturaReticoloVO);

							} else if (!(areaDatiCatturaReticoloVO.getNatura().equals("W")
									&& areaDatiCatturaReticoloVO.getLegameDocType() != null)) {
								areaDatiCatturaReticoloVO.setNotaEsplicativa(notaEsplicativa);
								areaDatiPass.getTabellaOggettiPerCattura().put(keyLegame, areaDatiCatturaReticoloVO);
							}
						}
						documentoType2 = legameDocType.getDocumentoLegato();
						getDocumento(documentoType2, legameDocType
								.getTipoLegame().toString(), legameDocType,
								legamiType.getIdPartenza(), areaDatiPass);

						if (documentoType2.getLegamiDocumentoCount() > 0) {
							getLegamiElemento(sbnOutPut, sbnMarcType,
									documentoType2, null, areaDatiPass);
						}
					}

					// Trattamento delle forme di rinvio
				} else
				// SE E UN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {
					level++;
					// keyLegame = "8" + String.valueOf(level)
					keyLegame = "8" + legamiType.getIdPartenza()+ legameTitAccessoType.getIdArrivo()+ legameTitAccessoType.getTipoLegame();
					areaDatiCatturaReticoloVO.setLegameTitAccessoType(legameTitAccessoType);
					//almaviva5_20120223 questo controllo è inutile
					//if (legameTitAccessoType.getTipoLegame().getType() != SbnLegameAut.valueOf("4XX").getType()) {

						areaDatiCatturaReticoloVO.setNotaEsplicativa("Legame "
								+ legameTitAccessoType.getTipoLegame()
								+ " fra ID:  " + legamiType.getIdPartenza()
								+ " e ID: "
								+ legameTitAccessoType.getIdArrivo());

						areaDatiPass.getTabellaOggettiPerCattura().put(keyLegame, areaDatiCatturaReticoloVO);
					//}

					documentoType2 = legameTitAccessoType.getTitAccessoLegato();
					getDocumento(documentoType2, legameTitAccessoType
							.getTipoLegame().toString(), legameDocType,
							legamiType.getIdPartenza(), areaDatiPass);

					if (documentoType2.getLegamiDocumentoCount() > 0) {
						getLegamiElemento(sbnOutPut, sbnMarcType,
								documentoType2, null, areaDatiPass);
					}
				}
			}
		}
		return;
	}

	public void getDocumento(DocumentoType documentoType, String tipoLegame,
			LegameDocType legameDocType, String idArrivo,
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

		DocumentoTypeChoice documentoTypeChoice = documentoType
				.getDocumentoTypeChoice();

		// LEGGO SBNOUTPUT.DOCUMENTO.DATIDOCUMENTO PRIMO LIVELLO
		if (documentoTypeChoice.getDatiDocumento() != null) {
			DatiDocType datiDocType = documentoTypeChoice.getDatiDocumento();
			areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

			areaDatiCatturaReticoloVO.setIdOggetto(datiDocType.getT001());
			areaDatiCatturaReticoloVO.setVersioneOggetto(datiDocType.getT005());
			areaDatiCatturaReticoloVO.setTipoAuthority("");
			areaDatiCatturaReticoloVO.setTipoMateriale(datiDocType.getTipoMateriale().toString());
			areaDatiCatturaReticoloVO.setLivAut(datiDocType.getLivelloAutDoc().toString());
			areaDatiCatturaReticoloVO.setNatura(datiDocType.getNaturaDoc().toString());

			areaDatiCatturaReticoloVO.setDatiDocType(datiDocType);
			// Modifica ALMAVIVA2 21 genn. 2009 per catturare gli spogli
			// tramite la voce di cattura inferiori (natura N e legame 464 M464N)
			if ((datiDocType.getNaturaDoc().toString().equals("M")
					|| datiDocType.getNaturaDoc().toString().equals("W")
					|| datiDocType.getNaturaDoc().toString().equals("N"))
					&& (tipoLegame.equals("463") || tipoLegame.equals("464"))) {
				areaDatiCatturaReticoloVO.setLegameDocType(legameDocType);
				areaDatiCatturaReticoloVO.setIdOggetto(idArrivo);
				areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiDocType.getT001());
				areaDatiPass.getTabellaOggettiPerCattura().put("2" + datiDocType.getT001(), areaDatiCatturaReticoloVO);
			} else {
				areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiDocType.getT001());
				areaDatiPass.getTabellaOggettiPerCattura().put("1" + datiDocType.getT001(), areaDatiCatturaReticoloVO);
			}

		}

		// IN ALTERNATIVA LEGGO SBNOUTPUT.DOCUMENTO.DATITITACCESSO PRIMO LIVELLO
		if (documentoTypeChoice.getDatiTitAccesso() != null) {
			TitAccessoType titAccessoType = documentoTypeChoice.getDatiTitAccesso();
			areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();

			areaDatiCatturaReticoloVO.setIdOggetto(titAccessoType.getT001());
			areaDatiCatturaReticoloVO.setVersioneOggetto(titAccessoType.getT005());
			areaDatiCatturaReticoloVO.setTipoAuthority("");
			areaDatiCatturaReticoloVO.setTipoMateriale("");
			areaDatiCatturaReticoloVO.setLivAut(titAccessoType.getLivelloAut().toString());
			areaDatiCatturaReticoloVO.setNatura(titAccessoType.getNaturaTitAccesso().toString());

			areaDatiCatturaReticoloVO.setTitAccessoType(titAccessoType);
			areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: " + titAccessoType.getT001());
			areaDatiPass.getTabellaOggettiPerCattura().put("1" + titAccessoType.getT001(), areaDatiCatturaReticoloVO);
		}
		return;
	}

	public AreaDatiVariazioneReturnVO catturaAutore(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		// Fase di accesso all'Indice per catturare il reticolo dell'oggetto
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = new AreaDatiVariazioneReturnVO();
		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();

		String esito = ricercaElementiAutorePerCattura(areaDatiPass);

		if (!esito.equals("0000")) {

			// Inizio Modifica ALMAVIVA2 19.11.2010 Mail Interna R. almaviva:
			// Errore:ID: MUS0242524 - Segnalazione: ERROR >>Index: 0, Size: 0 Errore durante la fase di lettura da Indice/Polo
			// succede quando un interprete dell'elenco interpreti non esiste sulla Base dati di Indice - secondo le regole
			// di catalogazione non si può attribuire un interprete che non sia presente sulla tabella autori
			// Viene inserita la corretta decodifica del massaggio di errore (metodo catturaAutore - SbnGestioneAllAuthorityDao;
			if (esito.substring(0, 4).equals("9999")) {
				areaDatiPassReturnInsert.setCodErr("9999");
				areaDatiPassReturnInsert.setTestoProtocollo(esito.substring(5));
				return areaDatiPassReturnInsert;
			}
			// Fine Modifica ALMAVIVA2 19.11.2010 Mail Interna R. almaviva

			areaDatiPassReturnInsert.setCodErr("9999");
			areaDatiPassReturnInsert.setTestoProtocollo(esito
					+ " Errore durante la fase di lettura da Indice/Polo");
			return areaDatiPassReturnInsert;
		}

		areaDatiPassReturnCattura.setTabellaOggettiPerCattura(areaDatiPass.getTabellaOggettiPerCattura());

		// Fase di passaggio dei dati fra le aree
		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
		areaTabellaOggettiDaCatturareVO
				.setTabellaOggettiPerCattura(areaDatiPassReturnCattura
						.getTabellaOggettiPerCattura());
		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdPadre());
		areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass
				.getTipoAuthority());

		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto

		areaDatiPassReturnInsert = inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);

		if (areaDatiPassReturnInsert.getCodErr().equals("")
				|| areaDatiPassReturnInsert.getCodErr().equals("0000")
				&& areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {

			// Chiamata per comunicazione allineamento documenti;
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			SBNMarc sbnRisposta;
			ComunicaAllineatiType comunicaAllineatiType = new ComunicaAllineatiType();
			AllineatiType allineatiType = new AllineatiType();

			SbnOggetto sbnOggetto = new SbnOggetto();
			if (areaDatiPass.getTipoAuthority() != null) {
				if (!areaDatiPass.getTipoAuthority().equals("")) {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAuthority()));
				}
			}
			if (areaDatiPass.getTipoMateriale() != null) {
				if (!areaDatiPass.getTipoMateriale().equals("")) {
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMateriale()));
				}
			}

			if (sbnOggetto.getTipoAuthority() != null || sbnOggetto.getTipoMateriale() != null) {
				allineatiType.setTipoOggetto(sbnOggetto);
				allineatiType.setIdAllineato(areaDatiPass.getIdPadre());
				comunicaAllineatiType.addAllineati(allineatiType);

				sbnrequest.setComunicaAllineati(comunicaAllineatiType);
				sbnmessage.setSbnRequest(sbnrequest);

				try {
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturnInsert.setCodErr("9999");
						areaDatiPassReturnInsert
								.setTestoProtocollo("Cattura terminata correttamente"
										+ "<br />"
										+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati per assenza Connessione con Sistema Centrale");
						return areaDatiPassReturnInsert;
					} else if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
						areaDatiPassReturnInsert.setCodErr("9999");
						areaDatiPassReturnInsert
								.setTestoProtocollo("Cattura terminata correttamente"
										+ "<br />"
										+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati:"
										+ "<br />"
										+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito()
										+ " "
										+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
						return areaDatiPassReturnInsert;
					}
				} catch (SbnMarcException e) {
					e.printStackTrace();
					areaDatiPassReturnInsert.setCodErr("9999");
					areaDatiPassReturnInsert.setTestoProtocollo("ERROR >>" + e.getMessage());
				}
			}
		}

		return areaDatiPassReturnInsert;
	}

	public String ricercaElementiAutorePerCattura(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		String idPadre = areaDatiPass.getIdPadre();
		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;

		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = null;

		try {

			if (areaDatiPass.getSbnOutputTypeDaAllineare() == null) {
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
				canali.setT001(idPadre);

				cercaAutoreType.setCanaliCercaDatiAut(canali);
				cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
				cercaElemento.setCercaDatiAut(cercaAutoreType);

				sbnmessage.setSbnRequest(sbnrequest);
				sbnrequest.setCerca(cercaType);

				// CHIAMATA AL PROTOCOLLO
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null)
					return "NoSeverInd";

				// dimensione di ogni riga del allbero
				SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

				SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
				sbnOutPut = sbnResponseChoice.getSbnOutput();
			} else {
				sbnOutPut = areaDatiPass.getSbnOutputTypeDaAllineare();
			}

			// RADICE FORMA ACCETTATA


			// Inizio Modifica ALMAVIVA2 19.11.2010 Mail Interna R. almaviva:
			// Errore:ID: MUS0242524 - Segnalazione: ERROR >>Index: 0, Size: 0 Errore durante la fase di lettura da Indice/Polo
			// succede quando un interprete dell'elenco interpreti non esiste sulla Base dati di Indice - secondo le regole
			// di catalogazione non si può attribuire un interprete che non sia presente sulla tabella autori
			// Viene inserito il controllo sul non trovato (metodo ricercaElementiAutorePerCattura - SbnGestioneAllAuthorityDao;;
			if (sbnOutPut.getElementoAutCount() == 0) {
				return "9999 Attenzione: l'interprete " + idPadre + " è assente dalla Base Dati di Indice e non sarà quindi allineato sul Polo";
			}
			// Fine Modifica ALMAVIVA2 19.11.2010 Mail Interna R. almaviva

			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElementoType = elementoAut.getDatiElementoAut();

			areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
			areaDatiCatturaReticoloVO.setIdOggetto(datiElementoType.getT001());
			areaDatiCatturaReticoloVO.setVersioneOggetto(datiElementoType.getT005());
			areaDatiCatturaReticoloVO.setTipoAuthority(datiElementoType.getTipoAuthority().toString());
			areaDatiCatturaReticoloVO.setTipoMateriale("");
			areaDatiCatturaReticoloVO.setLivAut(datiElementoType.getLivelloAut().toString());
			areaDatiCatturaReticoloVO.setNatura("");

			areaDatiCatturaReticoloVO.setDatiElementoType(datiElementoType);
			level++;
			areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiElementoType.getT001());
			areaDatiPass.getTabellaOggettiPerCattura().put("0" + datiElementoType.getT001(), areaDatiCatturaReticoloVO);
			if (!areaDatiPass.isSoloRadice()) {
				getLegamiElemento(sbnOutPut, sbnRisposta, null, elementoAut, areaDatiPass);
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			return "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}

		return "0000";
	}

	public AreaDatiVariazioneReturnVO catturaMarca(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		// Fase di accesso all'Indice per catturare il reticolo dell'oggetto
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = new AreaDatiVariazioneReturnVO();
		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();

		String esito = ricercaElementiMarcaPerCattura(areaDatiPass);

		if (!esito.equals("0000")) {
			areaDatiPassReturnInsert.setCodErr("9999");
			areaDatiPassReturnInsert.setTestoProtocollo(esito
					+ " Errore durante la fase di lettura da Indice/Polo");
			return areaDatiPassReturnInsert;
		}

		areaDatiPassReturnCattura.setTabellaOggettiPerCattura(areaDatiPass.getTabellaOggettiPerCattura());

		// Fase di passaggio dei dati fra le aree
		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
		areaTabellaOggettiDaCatturareVO
				.setTabellaOggettiPerCattura(areaDatiPassReturnCattura
						.getTabellaOggettiPerCattura());
		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdPadre());
		areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass.getTipoAuthority());

		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto

		areaDatiPassReturnInsert = inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);

		if (areaDatiPassReturnInsert.getCodErr().equals("")
				|| areaDatiPassReturnInsert.getCodErr().equals("0000")
				&& areaDatiPassReturnInsert.getTestoProtocollo().equals("")) {

			// Chiamata per comunicazione allineamento documenti;
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			SBNMarc sbnRisposta;
			ComunicaAllineatiType comunicaAllineatiType = new ComunicaAllineatiType();
			AllineatiType allineatiType = new AllineatiType();

			SbnOggetto sbnOggetto = new SbnOggetto();
			if (areaDatiPass.getTipoAuthority() != null) {
				if (!areaDatiPass.getTipoAuthority().equals("")) {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAuthority()));
				}
			}
			if (areaDatiPass.getTipoMateriale() != null) {
				if (!areaDatiPass.getTipoMateriale().equals("")) {
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMateriale()));
				}
			}

			if (sbnOggetto.getTipoAuthority() != null
					|| sbnOggetto.getTipoMateriale() != null) {

				allineatiType.setTipoOggetto(sbnOggetto);
				allineatiType.setIdAllineato(areaDatiPass.getIdPadre());
				comunicaAllineatiType.addAllineati(allineatiType);

				sbnrequest.setComunicaAllineati(comunicaAllineatiType);
				sbnmessage.setSbnRequest(sbnrequest);

				try {
					this.indice.setMessage(sbnmessage, this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						areaDatiPassReturnInsert.setCodErr("9999");
						areaDatiPassReturnInsert
								.setTestoProtocollo("Cattura terminata correttamente"
										+ "<br />"
										+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati per assenza Connessione con Sistema Centrale");
						return areaDatiPassReturnInsert;
					} else if (!sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("0000")) {
						areaDatiPassReturnInsert.setCodErr("9999");
						areaDatiPassReturnInsert
								.setTestoProtocollo("Cattura terminata correttamente"
										+ "<br />"
										+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati:"
										+ "<br />"
										+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito()
										+ " "
										+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
						return areaDatiPassReturnInsert;
					}
				} catch (SbnMarcException e) {
					e.printStackTrace();
					areaDatiPassReturnInsert.setCodErr("9999");
					areaDatiPassReturnInsert.setTestoProtocollo("ERROR >>"
							+ e.getMessage());
				}
			}
		}

		return areaDatiPassReturnInsert;
	}

	public AreaDatiVariazioneReturnVO catturaLuogo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		// Fase di accesso all'Indice per catturare il reticolo dell'oggetto
		AreaDatiVariazioneReturnVO areaDatiPassReturnCattura = new AreaDatiVariazioneReturnVO();
		AreaDatiVariazioneReturnVO areaDatiPassReturnInsert = new AreaDatiVariazioneReturnVO();

		String esito = ricercaElementiLuogoPerCattura(areaDatiPass);

		if (!esito.equals("0000")) {
			areaDatiPassReturnInsert.setCodErr("9999");
			areaDatiPassReturnInsert.setTestoProtocollo(esito
					+ " Errore durante la fase di lettura da Indice/Polo");
			return areaDatiPassReturnInsert;
		}

		areaDatiPassReturnCattura.setTabellaOggettiPerCattura(areaDatiPass
				.getTabellaOggettiPerCattura());

		// Fase di passaggio dei dati fra le aree
		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
		areaTabellaOggettiDaCatturareVO
				.setTabellaOggettiPerCattura(areaDatiPassReturnCattura
						.getTabellaOggettiPerCattura());
		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPass.getIdPadre());
		areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPass
				.getTipoAuthority());

		// Fase di accesso al Polo per aggiornare il reticolo dell'oggetto
		areaDatiPassReturnInsert = inserisciReticoloCatturato(areaTabellaOggettiDaCatturareVO);
		return areaDatiPassReturnInsert;
	}

	public String ricercaElementiMarcaPerCattura(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		String idPadre = areaDatiPass.getIdPadre();
		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;

		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = null;

		try {

			if (areaDatiPass.getSbnOutputTypeDaAllineare() == null) {
				SbnMessageType sbnmessage = new SbnMessageType();
				SbnRequestType sbnrequest = new SbnRequestType();
				CercaType cercaType = new CercaType();
				CercaElementoAutType cercaElemento = new CercaElementoAutType();

				cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				cercaType.setNumPrimo(1);
				cercaType.setMaxRighe(1);

				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
				cercaType.setCercaElementoAut(cercaElemento);
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

				canali.setT001(idPadre);

				CercaMarcaType cercaMarcaType = new CercaMarcaType();
				cercaMarcaType.setCanaliCercaDatiAut(canali);
				cercaMarcaType.setTipoAuthority(SbnAuthority.MA);
				cercaElemento.setCercaDatiAut(cercaMarcaType);

				sbnmessage.setSbnRequest(sbnrequest);
				sbnrequest.setCerca(cercaType);

				// CHIAMATA AL PROTOCOLLO
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null)
					return "NoSeverInd";

				// dimensione di ogni riga del allbero
				SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

				SbnResponseTypeChoice sbnResponseChoice = sbnResponse
						.getSbnResponseTypeChoice();
				sbnOutPut = sbnResponseChoice.getSbnOutput();
			} else {
				sbnOutPut = areaDatiPass.getSbnOutputTypeDaAllineare();
			}

			// RADICE FORMA ACCETTATA
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElementoType = elementoAut.getDatiElementoAut();

			areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
			areaDatiCatturaReticoloVO.setIdOggetto(datiElementoType.getT001());
			areaDatiCatturaReticoloVO.setVersioneOggetto(datiElementoType.getT005());
			areaDatiCatturaReticoloVO.setTipoAuthority(datiElementoType.getTipoAuthority().toString());
			areaDatiCatturaReticoloVO.setTipoMateriale("");
			areaDatiCatturaReticoloVO.setLivAut(datiElementoType.getLivelloAut().toString());
			areaDatiCatturaReticoloVO.setNatura("");

			areaDatiCatturaReticoloVO.setDatiElementoType(datiElementoType);
			level++;
			areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiElementoType.getT001());
			areaDatiPass.getTabellaOggettiPerCattura().put("0" + datiElementoType.getT001(),areaDatiCatturaReticoloVO);
			if (!areaDatiPass.isSoloRadice()) {
				getLegamiElemento(sbnOutPut, sbnRisposta, null, elementoAut,areaDatiPass);
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			return "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}

		return "0000";
	}

	public String ricercaElementiLuogoPerCattura(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		String idPadre = areaDatiPass.getIdPadre();
		SBNMarc sbnRisposta = null;
		SbnOutputType sbnOutPut = null;

		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO = null;

		try {

			if (areaDatiPass.getSbnOutputTypeDaAllineare() == null) {
				SbnMessageType sbnmessage = new SbnMessageType();
				SbnRequestType sbnrequest = new SbnRequestType();
				CercaType cercaType = new CercaType();
				CercaElementoAutType cercaElemento = new CercaElementoAutType();

				cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				cercaType.setNumPrimo(1);
				cercaType.setMaxRighe(1);

				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
				cercaType.setCercaElementoAut(cercaElemento);
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

				canali.setT001(idPadre);

				CercaLuogoType cercaLuogoType = new CercaLuogoType();
				cercaLuogoType.setCanaliCercaDatiAut(canali);
				cercaLuogoType.setTipoAuthority(SbnAuthority.LU);
				cercaElemento.setCercaDatiAut(cercaLuogoType);

				sbnmessage.setSbnRequest(sbnrequest);
				sbnrequest.setCerca(cercaType);

				// CHIAMATA AL PROTOCOLLO
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();

				if (sbnRisposta == null)
					return "NoSeverInd";

				// dimensione di ogni riga del allbero
				SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
				SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

				SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
				sbnOutPut = sbnResponseChoice.getSbnOutput();
			} else {
				sbnOutPut = areaDatiPass.getSbnOutputTypeDaAllineare();
			}

			// RADICE FORMA ACCETTATA
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElementoType = elementoAut.getDatiElementoAut();

			areaDatiCatturaReticoloVO = new AreaDatiCatturaReticoloVO();
			areaDatiCatturaReticoloVO.setIdOggetto(datiElementoType.getT001());
			areaDatiCatturaReticoloVO.setVersioneOggetto(datiElementoType.getT005());
			areaDatiCatturaReticoloVO.setTipoAuthority(datiElementoType.getTipoAuthority().toString());
			areaDatiCatturaReticoloVO.setTipoMateriale("");
			areaDatiCatturaReticoloVO.setLivAut(datiElementoType.getLivelloAut().toString());
			areaDatiCatturaReticoloVO.setNatura("");

			areaDatiCatturaReticoloVO.setDatiElementoType(datiElementoType);
			level++;
			areaDatiCatturaReticoloVO.setNotaEsplicativa("ID: "	+ datiElementoType.getT001());
			areaDatiPass.getTabellaOggettiPerCattura().put("0" + datiElementoType.getT001(),areaDatiCatturaReticoloVO);
			if (!areaDatiPass.isSoloRadice()) {
				getLegamiElemento(sbnOutPut, sbnRisposta, null, elementoAut,areaDatiPass);
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (IllegalArgumentException ie) {
			return "ERROR >>" + ie.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}

		return "0000";
	}

	public AreaDatiVariazioneReturnVO inserisciReticoloCatturato(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setBid(areaDatiPass.getIdPadre());
		areaDatiPassReturn.setCodErr("0000");
		areaDatiPassReturn.setTestoProtocollo("");
		AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO;

		listaOggetti = areaDatiPass.getTabellaOggettiPerCattura().keySet();
		String testoProtocollo = "";
		String diagnFinale = "";

		Map<String, String> listaCorrCidIndCidPol = new HashMap<String, String>();

		String esito = "";

		for (Object o : listaOggetti) {
			areaDatiCatturaReticoloVO = (AreaDatiCatturaReticoloVO) areaDatiPass.getTabellaOggettiPerCattura().get(o);

			// ALMAVIVA2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
			// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
			// del flag di avvenuto allineamento.
			areaDatiCatturaReticoloVO.setAllineamentoDaFileLocale(areaDatiPass.isAllineamentoDaFileLocale());


			if (areaDatiCatturaReticoloVO.getLegameDocType() != null
					&& areaDatiCatturaReticoloVO.getDatiDocType() != null) {
				// Inserimento Documento con legame 463 (GESTIONE INFERIORI)
				// esito =
				// inserisciDocumentoCatturato(areaDatiCatturaReticoloVO,
				// areaDatiPass.isSoloCopia(), areaDatiPass.isCopiaReticolo(),
				// areaDatiPass.isCreaNuovoId());
				esito = inserisciDocumentoCatturato(areaDatiCatturaReticoloVO,	false, areaDatiPass.isCopiaReticolo(),
						false, areaDatiPass.isSoloRadice(), areaDatiPass.isProvenienzaAllineamento(), false);
				if (esito.length() > 4) {
					String codEsito = esito.substring(0, 4);
					if (codEsito.equals("0000")) {
						areaDatiPassReturn.setBid(esito.substring(4, 14));
						esito = codEsito;
					} else {
						diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()
								+ " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")"	+ "<br />";
						esito = "";
					}
				} else {
					if (!esito.equals("0000") && !esito.equals("")) {
						diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()
								+ " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")"	+ "<br />";
						esito = "";
					}
				}
			} else if ((areaDatiCatturaReticoloVO.getTipoAuthority().equals("AU")
					|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("LU"))
					&& areaDatiCatturaReticoloVO.getDatiElementoType() != null
					&& areaDatiCatturaReticoloVO.getDatiElementoType().getFormaNome().toString().equals("R")) {
				// Inserimento Forma di Rinvio e suo legame a Forma Accettata
				esito = inserisciRinvioCatturato(areaDatiCatturaReticoloVO);
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("AU")
					&& areaDatiCatturaReticoloVO.getDatiElementoType() != null
					&& areaDatiCatturaReticoloVO.getDatiElementoType().getFormaNome().toString().equals("A")) {
				esito = inserisciAutoreCatturato(areaDatiCatturaReticoloVO,	areaDatiPass.isSoloRadice());
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("MA")
					&& areaDatiCatturaReticoloVO.getLegamiType() != null
					&& areaDatiCatturaReticoloVO.getDatiElementoType() != null) {
				// Inserimento Marca con repertori agganciati
				esito = inserisciMarcaConRepertoriCatturato(areaDatiCatturaReticoloVO);
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("MA")
					&& (areaDatiCatturaReticoloVO.getIdOggetto().equals("SBNM000000")
							|| areaDatiCatturaReticoloVO.getIdOggetto().equals("SBNM000001"))) {
				// Inserimento Marca senza repertori agganciati
				esito = inserisciMarcaSenzaRepertoriCatturato(areaDatiCatturaReticoloVO);
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			// evolutive ottobre 2015 ALMAVIVA2 -  Nella gestione dei luoghi viene data la possibilità di gestire
			// i campi nota informativa , nota catalogatore e legame a repertori
			// INIZIO SPOSTAMENTO
			} else if ((areaDatiCatturaReticoloVO.getTipoAuthority().equals("TU") || areaDatiCatturaReticoloVO.getTipoAuthority().equals("UM"))
					&& areaDatiCatturaReticoloVO.getDatiElementoType() != null) {
				esito = inserisciTitUniformeCatturato(
						areaDatiCatturaReticoloVO, areaDatiPass.isSoloRadice(),
						areaDatiPass.isProvenienzaAllineamento());
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("LU")) {
				esito = inserisciLuogoCatturato(areaDatiCatturaReticoloVO, areaDatiPass.isSoloRadice());
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()+ " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			// FINE SPOSTAMENTO


			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("SO")) {
				// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
				// la cattura avviene solo per le authority selzionate con il check (come per le W)
				esito = inserisciSoggettoCatturato(areaDatiCatturaReticoloVO, areaDatiPass.getTicket());
				if (esito.startsWith("0000")) {
					listaCorrCidIndCidPol.put(esito.substring(4, 14), esito.substring(14));
				} else {
					if (!esito.equals("0000") && !esito.equals("")) {
						diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()+ " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
						esito = "";
					}
				}

			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("CL")) {
				// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
				// la cattura avviene solo per le authority selzionate con il check (come per le W)
				esito = inserisciClasseCatturato(areaDatiCatturaReticoloVO, areaDatiPass.getTicket());
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()+ " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			} else if (areaDatiCatturaReticoloVO.getLegameDocType() != null
					|| areaDatiCatturaReticoloVO.getLegameElementoAutType() != null
					|| areaDatiCatturaReticoloVO.getLegameTitAccessoType() != null) {
				if (areaDatiCatturaReticoloVO.getNatura() != null
						&& !areaDatiCatturaReticoloVO.getNatura().equals("")) {
					// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
					// la cattura avviene solo per le authority selzionate con il check (come per le W)
					if (areaDatiCatturaReticoloVO.getLegameElementoAutType() != null
							&& areaDatiCatturaReticoloVO.getLegameElementoAutType().getTipoAuthority() != null
							&& areaDatiCatturaReticoloVO.getLegameElementoAutType().getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
						String cidIndice = areaDatiCatturaReticoloVO.getLegameElementoAutType().getIdArrivo();
						String cidPolo = listaCorrCidIndCidPol.get(cidIndice);
						areaDatiCatturaReticoloVO.setCorrispondenzaCidIndCidPol(cidPolo);
						esito = inserisciLegameTitoloCatturato(areaDatiCatturaReticoloVO, areaDatiPass.getTicket());
						areaDatiCatturaReticoloVO.setCorrispondenzaCidIndCidPol("");
					} else {
						areaDatiCatturaReticoloVO.setCorrispondenzaCidIndCidPol("");
						esito = inserisciLegameTitoloCatturato(areaDatiCatturaReticoloVO, areaDatiPass.getTicket());
					}
					if (!esito.equals("0000") && !esito.equals("")) {
						diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")"	+ "<br />";
						esito = "";
					}
				} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("AU")
						|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("MA")
						|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("TU")
						|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("UM")) {
					esito = inserisciLegameAuthorityCatturato(areaDatiCatturaReticoloVO);
					if (!esito.equals("0000") && !esito.equals("")) {
						diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")"	+ "<br />";
						esito = "";
					}
				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("")) {
				switch (areaDatiCatturaReticoloVO.getNatura().charAt(0)) {
				case 'A':
					esito = inserisciTitUniformeCatturato(areaDatiCatturaReticoloVO, areaDatiPass
									.isSoloRadice(), areaDatiPass.isProvenienzaAllineamento());
					break;
				case 'C':
				case 'M':
				case 'S':
				case 'W':
				case 'N':
					boolean isRadice = areaDatiPass.getIdPadre().equals(areaDatiCatturaReticoloVO.getIdOggetto());
					if (isRadice) {
						esito = inserisciDocumentoCatturato(
								areaDatiCatturaReticoloVO, areaDatiPass.isSoloCopia(), areaDatiPass.isCopiaReticolo(),
									areaDatiPass.isCreaNuovoId(), areaDatiPass.isSoloRadice(), areaDatiPass.isProvenienzaAllineamento(), isRadice);
					} else {
						esito = inserisciDocumentoCatturato(areaDatiCatturaReticoloVO, false, areaDatiPass.isCopiaReticolo(),
								false, areaDatiPass.isSoloRadice(), areaDatiPass.isProvenienzaAllineamento(), false);
					}
					break;
				case 'B':
				case 'D':
				case 'P':
				case 'T':
					esito = inserisciTitAccessoCatturato(areaDatiCatturaReticoloVO.getTitAccessoType(),
							areaDatiPass.isSoloRadice(), areaDatiPass.isProvenienzaAllineamento());
					break;
				}
				if (esito.length() > 4) {
					String codEsito = esito.substring(0, 4);
					if (codEsito.equals("0000")) {
						areaDatiPassReturn.setBid(esito.substring(5, 15));
						esito = codEsito;
					} else {
						diagnFinale = diagnFinale
								+ " "
								+ areaDatiCatturaReticoloVO
										.getNotaEsplicativa()
								+ " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito
								+ " (key di aggiornamento:" + o + ")"
								+ "<br />";
						esito = "";

						// Risoluzione malfunzionamento Mantis 6882 - Gennaio 2019 - ALMAVIVA2
						// per inviare la diagnostica di errore in LOCALIZZAZIONE ma continuare la procedura di cattura viene inviato
						// il diagnostico di errore mandato da indice concatenato alla sua descrizione; all'uscita viene tracciata la
						// segnalazione, pulito il flag di segnalazione e si ciontinua con l'elaborazione.
						if (!testoProtocollo.contains("3232")) {
							if (o.toString().substring(0, 1).equals("0")) {
								// solo nel caso di errore sulla radice del riticolo da catturare si torna al client
								areaDatiPassReturn.setCodErr("9999");
								areaDatiPassReturn
										.setTestoProtocollo("Cattura terminata a meno dei seguenti oggetti:"
												+ "<br />" + diagnFinale);
								return areaDatiPassReturn;
							}
						}
					}
				} else {
					if (!esito.equals("0000") && !esito.equals("")) {
						diagnFinale = diagnFinale
								+ " "
								+ areaDatiCatturaReticoloVO
										.getNotaEsplicativa()
								+ " - Segnalazione: " + esito + "<br />";
						testoProtocollo = testoProtocollo + esito
								+ " (key di aggiornamento:" + o + ")"
								+ "<br />";
						esito = "";
					}
				}
// evolutive ottobre 2015 ALMAVIVA2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi nota informativa ,
// nota catalogatore e legame a repertori
// ATTENZIONE gli if per aggiornatmento dei TitoloUniformi e sui luoghi vengono spostati sopra a quelli sui Documenti
// a seguire tutte le altre Authority (Autori e Marche)	altrimenti i legami ai repertori non vengono mai chiamati perchè si entra
// nell'if della presenza di almeno un legame e quindi si saltano a pie pari queste due situazioni
//			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("TU")
//					|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("UM")) {
//				esito = inserisciTitUniformeCatturato(
//						areaDatiCatturaReticoloVO, areaDatiPass.isSoloRadice(),
//						areaDatiPass.isProvenienzaAllineamento());
//				if (!esito.equals("0000") && !esito.equals("")) {
//					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa() + " - Segnalazione: " + esito + "<br />";
//					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
//					esito = "";
//				}
//			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("LU")) {
//				esito = inserisciLuogoCatturato(areaDatiCatturaReticoloVO, areaDatiPass.isSoloRadice());
//				if (!esito.equals("0000") && !esito.equals("")) {
//					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()+ " - Segnalazione: " + esito + "<br />";
//					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
//					esito = "";
//				}
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("RE")) {
				esito = "0000";
				if (!esito.equals("0000") && !esito.equals("")) {
					diagnFinale = diagnFinale + " "	+ areaDatiCatturaReticoloVO.getNotaEsplicativa()+ " - Segnalazione: " + esito + "<br />";
					testoProtocollo = testoProtocollo + esito + " (key di aggiornamento:" + o + ")" + "<br />";
					esito = "";
				}
			}
		}

		if (!testoProtocollo.equals("")) {
			areaDatiPassReturn.setCodErr("9999");
			areaDatiPassReturn.setTestoProtocollo("Cattura terminata a meno dei seguenti oggetti:" + "<br />" + diagnFinale);
			return areaDatiPassReturn;
		}

		if (esito.equals("0000")) {
			return areaDatiPassReturn;
		}
		if (!esito.equals("")) {
			areaDatiPassReturn.setCodErr("9999");
			return areaDatiPassReturn;
		}
		return areaDatiPassReturn;
	}

	public String inserisciDocumentoCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO,
			boolean soloCopia, boolean copiaReticolo, boolean creaNuovoId,
			boolean soloRadice, boolean provAllineamento, boolean isRadice) {

		DatiDocType datiDocTypeInd = new DatiDocType();
		datiDocTypeInd = areaDatiCatturaReticoloVO.getDatiDocType();
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		areaDatiPassReturn.setTestoProtocollo("");
		String tipoAuthority = "";

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiDocTypeInd.getT001());
		if (soloRadice) {
			areaDatiControlliPoloVO.setCancellareInferiori(false);
		} else {
			areaDatiControlliPoloVO.setCancellareInferiori(true);
		}
		areaDatiControlliPoloVO = getDocumentoPuliziaLegamiPolo(areaDatiControlliPoloVO);

		// inizio Bug MANTIS 5908 ALMAVIVA2
    	// La modifica reale è stata effettuata nell'oggetto TitoloValidaModifica (verificaLivelloModifica); la modifica in oggetto
		// ha il fine di non interrompere un'allineamento/cattura quando al materiale del documento non corrisponde la presenta della
		// relativa occorrenza sulla tabella specificità (es. Mat "U" senza tb_musica-il protocollo restituisce il cod:3029 )
		// e consentire così la correzione dell'incoerenza precedentemente creata;
		if (areaDatiControlliPoloVO.getCodErr().equals("3029")) {
			// return areaDatiControlliPoloVO.getTestoProtocollo();
			// Si continua per consentire la correzione/creazione della tabella specialistica
		} else {
			if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
				return areaDatiControlliPoloVO.getTestoProtocollo();
			}
		}
//		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
//			return areaDatiControlliPoloVO.getTestoProtocollo();
//		}
		// fine Bug MANTIS 5908 ALMAVIVA2


		// Controllo su Unicum per non inviare la localizzazione
		boolean unicum = false;

		if (datiDocTypeInd.getGuida() != null) {
			if (datiDocTypeInd.getGuida().getTipoRecord() != null) {
				if (datiDocTypeInd.getGuida().getTipoRecord().toString().equals("f")
						|| datiDocTypeInd.getGuida().getTipoRecord().toString().equals("d")
						|| datiDocTypeInd.getGuida().getTipoRecord().toString().equals("b")) {
					unicum = true;
				}
			}
		}

		DatiDocType datiDocTypePolo = null;
		// boolean infoLocalPolo = false;
		if (areaDatiControlliPoloVO.getDatiDocType() == null) {
			insert = true;
		} else {
			datiDocTypePolo = areaDatiControlliPoloVO.getDatiDocType();
			insert = false;

		}

		try {

			SBNMarc sbnRisposta = null;

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;

			ModificaType modificaType = null;

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
			}

			DocumentoType documentoType = new DocumentoType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			if (!insert == true) {
				datiDocTypeInd.setT005(datiDocTypePolo.getT005());
			}

			// ALMAVIVA2 - Marzo 2018 - Inserimento correttivi delle Aree ISBD in caso di cattura per evitare
			// alcuni casi di mancato allineamento
			if (datiDocTypeInd.getNaturaDoc().equals(SbnNaturaDocumento.W) || datiDocTypeInd.getNaturaDoc().equals(SbnNaturaDocumento.S)) {
				if (datiDocTypeInd.getT215() == null) {
					C215 c215 = new C215();;
					c215.setA_215(new String[] { "n.p." });
					datiDocTypeInd.setT215(c215);
				}
			}



			// ALMAVIVA2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
			// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
			// del flag di avvenuto allineamento.
			if (! areaDatiCatturaReticoloVO.isAllineamentoDaFileLocale()) {
				// INIZIO -----TUTTA QUESTA PARTE RIGUARDA INSERIMENTO DI INTERPRETI ED INCIPIT E DEVE ESSERE SALTATA QUANDO
				// ASSENTE IL COLLEGAMENTO AD INDICE

				// Nel caso di titolo musicale con interpreti l'inserimento viene fatto
				// inserendo prima ciclicamente gli eventuali interpreti e poi l'occorrenza sul titolo;
				// in fase di inserimento degli autori si aggioneranno le occorrenze degli autori;
				if (datiDocTypeInd instanceof MusicaType) {
					MusicaType musicaType = (MusicaType) datiDocTypeInd;
					C927 c927 = new C927();
					if (musicaType.getT927Count() > 0) {
						String idInterprete;
						String result;
						for (int i = 0; i < musicaType.getT927Count(); i++) {
							c927 = musicaType.getT927(i);
							idInterprete = c927.getC3_927();
							if (idInterprete != null && !idInterprete.equals("")) {
								result = inserisciInterpreteDocumentoCatturato(idInterprete);
								if (!result.equals("0000") && !result.equals("")) {
									return result;
								}
							}
						}
					}
					// Inizio modifica 18.12.2009 BUG MANTIS 3248 le D contenute
					// nell'incipit devono seguire lo stesso trattamento
					// degli autori altrimenti non si riesce ad inserire la M
					if (musicaType.getT926Count() > 0) {
						C926[] c926 = musicaType.getT926();
						String idIncipitTestuale;
						String result;
						for (int i = 0; i < c926.length; i++) {
							if (c926[i].getR_926() != null) {
								idIncipitTestuale = c926[i].getR_926();
								result = inserisciIncipitTestualeCatturato(idIncipitTestuale);
								if (!result.equals("0000") && !result.equals("")) {
									return result;
								}
							}
						}
					}
				}

				// Mantis esercizio Bug 6120 : Marzo 2016 anche i titoli Audiovisivi hanno gli interpreti come il musicale quindi
				// va esteso il trattamento di cattura degli interpreti/autori al fine di creare poi correttamente il
				// legame doc/Interprete
				if (datiDocTypeInd instanceof AudiovisivoType) {
					AudiovisivoType audiovisivoType = (AudiovisivoType) datiDocTypeInd;
					C927 c927 = new C927();
					if (audiovisivoType.getT927Count() > 0) {
						String idInterprete;
						String result;
						for (int i = 0; i < audiovisivoType.getT927Count(); i++) {
							c927 = audiovisivoType.getT927(i);
							idInterprete = c927.getC3_927();
							if (idInterprete != null && !idInterprete.equals("")) {
								result = inserisciInterpreteDocumentoCatturato(idInterprete);
								if (!result.equals("0000") && !result.equals("")) {
									return result;
								}
							}
						}
					}
				}

				// Marzo 2016: Evolutiva ALMAVIVA2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
				// viene esteso anche al Materiale Moderno e Antico
				if (datiDocTypeInd instanceof ModernoType) {
					ModernoType modernoType = (ModernoType) datiDocTypeInd;
					C927 c927 = new C927();
					if (modernoType.getT927Count() > 0) {
						String idInterprete;
						String result;
						for (int i = 0; i < modernoType.getT927Count(); i++) {
							c927 = modernoType.getT927(i);
							idInterprete = c927.getC3_927();
							if (idInterprete != null && !idInterprete.equals("")) {
								result = inserisciInterpreteDocumentoCatturato(idInterprete);
								if (!result.equals("0000") && !result.equals("")) {
									return result;
								}
							}
						}
					}
				}
				if (datiDocTypeInd instanceof AnticoType) {
					AnticoType anticoType = (AnticoType) datiDocTypeInd;
					C927 c927 = new C927();
					if (anticoType.getT927Count() > 0) {
						String idInterprete;
						String result;
						for (int i = 0; i < anticoType.getT927Count(); i++) {
							c927 = anticoType.getT927(i);
							idInterprete = c927.getC3_927();
							if (idInterprete != null && !idInterprete.equals("")) {
								result = inserisciInterpreteDocumentoCatturato(idInterprete);
								if (!result.equals("0000") && !result.equals("")) {
									return result;
								}
							}
						}
					}
				}
				// FINE  -----TUTTA QUESTA PARTE RIGUARDA INSERIMENTO DI INTERPRETI ED INCIPIT E DEVE ESSERE SALTATA QUANDO
				// ASSENTE IL COLLEGAMENTO AD INDICE
			}

			// Nel caso di titolo moderno si deve verificare che, se c'è l'area
			// di pubblicazione
			// T210 con d210
			if (datiDocTypeInd instanceof ModernoType) {
				ModernoType modernoType = (ModernoType) datiDocTypeInd;
				String areaPubblicazione = IID_STRINGAVUOTA;
				UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
				areaPubblicazione = utilityCampiTitolo.getAreaPubblicazione(modernoType);

				// ALMAVIVA2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
				// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
				IndicatorePubblicato pubblicatoSiNo = null;
				for (int k = 0; k < 1; k++) {
					if (datiDocTypeInd.getT210Count() > 0) {
						pubblicatoSiNo = datiDocTypeInd.getT210(k).getId2();
					}
				}

				C210 c210 = null;

				if (!areaPubblicazione.equals("")) {
					c210 = new C210();
					Ac_210Type ac_210Type = new Ac_210Type();
					ac_210Type.setA_210(new String[] { areaPubblicazione });
					c210.addAc_210(ac_210Type);
					c210.setId2(pubblicatoSiNo);
				}
				if (c210 != null) {
					modernoType.setT210(new C210[] { c210 });
				}
			}

			// Controllo su Unicum per non inviare la localizzazione

			documentoTypeChoice.setDatiDocumento(datiDocTypeInd);
			String idPartenza = datiDocTypeInd.getT001();
			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			// Inserimento della parte legami nel caso di legame inferiore (GEST
			// 51)
			if (areaDatiCatturaReticoloVO.getLegameDocType() != null) {

				LegamiType[] arrayLegamiType = new LegamiType[1];
				LegamiType legamiType;
				ArrivoLegame arrivoLegame;

				legamiType = new LegamiType();
				legamiType.setIdPartenza(idPartenza);

				if (insert == true) {
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
				} else {
					legamiType.setTipoOperazione(SbnTipoOperazione.MODIFICA);
				}

				arrivoLegame = new ArrivoLegame();

				LegameDocType legameDocType = new LegameDocType();
				legameDocType.setNoteLegame(areaDatiCatturaReticoloVO.getLegameDocType().getNoteLegame());
				legameDocType.setSequenza(areaDatiCatturaReticoloVO.getLegameDocType().getSequenza());

				// Inizio Modifica ALMAVIVA2 29.09.2009
				// Per copia reticolo si deve inserire il legame 51 (e ovviamente la W) come locale

				// Intervento interno ALMAVIVA2 INIZIO marzo 2017: manca la dichiarazione di condivisione del legame:
				// nel caso di qualunque creazione di legame che non sia fatta con il copia reticolo;
				if (copiaReticolo) {
					legameDocType.setCondiviso(LegameDocTypeCondivisoType.N);
				} else {
					legameDocType.setCondiviso(LegameDocTypeCondivisoType.S);
				}
				// Fine Modifica ALMAVIVA2 29.09.2009

				if (areaDatiCatturaReticoloVO.getLegameDocType().getTipoLegame().toString().equals("461")
						|| areaDatiCatturaReticoloVO.getLegameDocType().getTipoLegame().toString().equals("410")) {
					legameDocType.setTipoLegame(SbnLegameDoc
							.valueOf(areaDatiCatturaReticoloVO
									.getLegameDocType().getTipoLegame()
									.toString()));
				} else {
					legameDocType.setTipoLegame(SbnLegameDoc.valueOf("461"));
				}

				legameDocType.setIdArrivo(areaDatiCatturaReticoloVO.getIdOggetto());

				legameDocType.setDocumentoLegato(null);
				arrivoLegame.setLegameDoc(legameDocType);

				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				legamiType.setArrivoLegame(arrayArrivoLegame);
				arrayLegamiType[0] = legamiType;

				// aggiungo IL LEGAME
				documentoType.setLegamiDocumento(arrayLegamiType);
				// Inizio Modifica ALMAVIVA2 29.09.2009
				// Per copia reticolo si deve inserire il legame 51 (e
				// ovviamente la W) come locale
				if (copiaReticolo) {
					documentoType.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);
				}
				// Fine Modifica ALMAVIVA2 29.09.2009

			}

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();
			sbnmessage.setSbnRequest(sbnrequest);

			String bidDaAssegnare = "";
			if (insert == true) {
				if (soloCopia) {
					if (copiaReticolo) {
						documentoType.getDocumentoTypeChoice().getDatiDocumento().setLivelloAutDoc(SbnLivello.VALUE_10);
						// MANUTENZIONE ADEGUATIVA APRILE 2018 - ALMAVIVA2 - Vedi Mantis
						// Il livello di autorità della specificità deve essere 51 fisso così da non aver problemi al momento
						// di una successiva condivisione in Indice
						if (documentoType.getDocumentoTypeChoice().getDatiDocumento() instanceof MusicaType) {
							MusicaType musicaType = (MusicaType) documentoType.getDocumentoTypeChoice().getDatiDocumento();
							musicaType.setLivelloAut(SbnLivello.VALUE_1);
							documentoType.getDocumentoTypeChoice().setDatiDocumento(musicaType);
						} else if (documentoType.getDocumentoTypeChoice().getDatiDocumento() instanceof GraficoType) {
							GraficoType graficoType = (GraficoType) documentoType.getDocumentoTypeChoice().getDatiDocumento();
							graficoType.setLivelloAut(SbnLivello.VALUE_1);
							documentoType.getDocumentoTypeChoice().setDatiDocumento(graficoType);
						} else if (documentoType.getDocumentoTypeChoice().getDatiDocumento() instanceof CartograficoType) {
							CartograficoType cartograficoType = (CartograficoType) documentoType.getDocumentoTypeChoice().getDatiDocumento();
							cartograficoType.setLivelloAut(SbnLivello.VALUE_1);
							documentoType.getDocumentoTypeChoice().setDatiDocumento(cartograficoType);
						} else if (documentoType.getDocumentoTypeChoice().getDatiDocumento() instanceof AudiovisivoType) {
							AudiovisivoType audiovisivoType = (AudiovisivoType) documentoType.getDocumentoTypeChoice().getDatiDocumento();
							audiovisivoType.setLivelloAut(SbnLivello.VALUE_1);
							documentoType.getDocumentoTypeChoice().setDatiDocumento(audiovisivoType);
						} else if (documentoType.getDocumentoTypeChoice().getDatiDocumento() instanceof ElettronicoType) {
							ElettronicoType elettronicoType = (ElettronicoType) documentoType.getDocumentoTypeChoice().getDatiDocumento();
							elettronicoType.setLivelloAut(SbnLivello.VALUE_1);
							documentoType.getDocumentoTypeChoice().setDatiDocumento(elettronicoType);
						}

					} else {
						documentoType.getDocumentoTypeChoice().getDatiDocumento().setLivelloAutDoc(SbnLivello.VALUE_7);
					}

					documentoType.getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);
					if (creaNuovoId) {
						SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
						AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
						areaDatiPassGetIdSbn.setTipoMat(documentoType
								.getDocumentoTypeChoice().getDatiDocumento()
								.getTipoMateriale().toString());
						areaDatiPassGetIdSbn.setTipoRec(null);
						areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
						if (areaDatiPassGetIdSbn.getIdSbn() == null
								|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
							areaDatiPassReturn.setCodErr(areaDatiPassGetIdSbn.getCodErr());
							areaDatiPassReturn.setTestoProtocollo(areaDatiPassGetIdSbn.getTestoProtocollo());
							return areaDatiPassReturn.getTestoProtocollo();
						} else {
							bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
							documentoType.getDocumentoTypeChoice().getDatiDocumento().setT001(bidDaAssegnare);
						}
					}
				}
				creaTypeChoice.setDocumento(documentoType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				modificaType.setDocumento(documentoType);
				modificaType.getDocumento().setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3029")) {
				sbnmessage.getSbnRequest().getModifica().getDocumento()
						.getLegamiDocumento(0).setTipoOperazione(
								SbnTipoOperazione.CREA);

				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "noServerLoc";
				}
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiDocTypeInd.getT001(), datiDocTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(datiDocTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getDocumento(0).getDocumentoTypeChoice()
						.getDatiDocumento().getT005());
			}


			// ALMAVIVA2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
			// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
			// del flag di avvenuto allineamento.
			if (areaDatiCatturaReticoloVO.isAllineamentoDaFileLocale()) {
				if (!bidDaAssegnare.equals("")) {
					areaDatiPassReturn.setBid(bidDaAssegnare);
					return "0000-" + bidDaAssegnare;
				} else {
					areaDatiPassReturn.setBid(idPartenza);
				}
				return "0000";
			}




			// Modifica ALMAVIVA2 03.03.2010 - per non localizzare quando su proviene dall'allineamento
			// Modifica ALMAVIVA2 27 maggio 2014
			// Viene attivata la richiesta ARCOBALENO in fase di ChiediAllinea -richiesta tipo 4 per avere subito le localizzazioni
			// per effettuare la localizzazione del reticolo in allineamento, non per la biblioteca operante che potrebbe
			// anche non gestire il bid ma per una delle biblioteche che effettivamente lo gestiscono;
			// al momento dell'allineamento del bid radice verrà inviata la richiesta di localizzazione che l'Indice
			// automaticamente esploderà a tutto il reticolo; in questo modo anche i nuovi elementi di reticolo
			// (autori o collane ad esempio) verranno correttamente localizzati;
			if (!soloCopia) {
				if (provAllineamento) {
					// Inserimento della localizzazione in Indice per l'oggetto inserito per effettuare esplosione a tutto il reticolo
					// solo se il Documento non è già localizzato per la biblioteca e immediata delocalizzazione dello stesso bid
					// così da mantenere solo la locazizzazione per il reticolo
					// Tutto il lavoro viene fatto SOLO quando si mette in lavorazione tutto il reticolo altrimenti, se è coinvolto
					// solo il bid la localizazione non deve essere toccata
					if (!soloRadice) {
						areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
						areaLocalizza.setIdLoc(datiDocTypeInd.getT001());
						areaLocalizza.setAuthority(tipoAuthority);
						areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
						areaLocalizza.setIndice(true);
						areaLocalizza.setPolo(false);
						areaLocalizza.setTipoLoc("Gestione");

						if (isRadice) {
							//se si sta allineando il bid radice questo va localizzato per propagare la loc. a tutti
							//gli elementi figli.
							areaLocalizza.setTipoOpe("Localizza");
							areaDatiPassReturn = localizzaAuthority(areaLocalizza);
							if (!areaDatiPassReturn.getCodErr().equals("0000")) {
								return "0000";
							}

							//se il bid radice non era in origine localizzato per la biblioteca operante la loc. va cancellata
							//(il delocalizza non si propaga agli elementi figli).
							if (!datiDocTypeInd.getSbnLocaliz().contains("bib")) {
								areaLocalizza.setTipoOpe("Delocalizza");
								areaDatiPassReturn = localizzaAuthority(areaLocalizza);
								if (!areaDatiPassReturn.getCodErr().equals("0000")) {
									return "0000";
								}
							}
						} else {
							//altri elementi del reticolo
							//se l'elemento in origine era localizzato per altra biblioteca del polo
							//va delocalizzato dalla bib. operante
							if (datiDocTypeInd.getSbnLocaliz().contains("polo")) {
								areaLocalizza.setTipoOpe("Delocalizza");
								areaDatiPassReturn = localizzaAuthority(areaLocalizza);
								if (!areaDatiPassReturn.getCodErr().equals("0000")) {
									return "0000";
								}
							}
						}
					}

				} else {
					// Inserimento della localizzazione in Indice per l'oggetto inserito
					areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
					areaLocalizza.setIdLoc(datiDocTypeInd.getT001());
					areaLocalizza.setAuthority(tipoAuthority);
					areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
					areaLocalizza.setTipoOpe("Localizza");
					areaLocalizza.setTipoLoc("Gestione");
					areaLocalizza.setIndice(true);
					areaLocalizza.setPolo(false);
					areaDatiPassReturn = localizzaAuthority(areaLocalizza);
					if (!areaDatiPassReturn.getCodErr().equals("0000")) {
						// Per problemi nella localizzazione dell'oggetto non ci si deve fermare ma continuare l'elaborazione
						// segnalando il problema
						// Risoluzione malfunzionamento Mantis 6882 - Gennaio 2019 - ALMAVIVA2
						// per inviare la diagnostica di errore in LOCALIZZAZIONE ma continuare la procedura di cattura viene inviato
						// il diagnostico di errore mandato da indice concatenato alla sua descrizione; all'uscita viene tracciata la
						// segnalazione, pulito il flag di segnalazione e si ciontinua con l'elaborazione.
						// return "0000";
						// return areaDatiPassReturn.getTestoProtocollo();
						return areaDatiPassReturn.getCodErr() + "-" + areaDatiPassReturn.getTestoProtocollo();
					}
				}
			} else {
				if (!bidDaAssegnare.equals("")) {
					areaDatiPassReturn.setBid(bidDaAssegnare);
					return "0000-" + bidDaAssegnare;
				} else {
					areaDatiPassReturn.setBid(idPartenza);
				}
			}
		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";
	}

	public AreaDatiControlliPoloVO getDocumentoPuliziaLegamiPolo(
			AreaDatiControlliPoloVO areaDatiControlliPoloVO) {

		areaDatiControlliPoloVO.setCodErr("0000");

		DatiDocType datiDocTypePolo = new DatiDocType();
		SBNMarc sbnRisposta = null;
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

			cercaDatiTitTypeChoice.setT001(areaDatiControlliPoloVO
					.getIdRicerca());
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			// CHIAMATA AL PROTOCOLLO
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiControlliPoloVO.setCodErr("9999");
				areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
				return areaDatiControlliPoloVO;
			}

			// Inizio ALMAVIVA2 10.11.2009 Richiesta interna - per errore di
			// marshalling (0101) o qualunque altro errore
			// deve bloccarsi e non continuare come fosse non trovato

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiControlliPoloVO;
			}
			// Fine ALMAVIVA2 10.11.2009 Richiesta interna

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe() == 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				return areaDatiControlliPoloVO;
			}

			datiDocTypePolo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
					.getDocumentoTypeChoice().getDatiDocumento();

			if (!areaDatiControlliPoloVO.isCancellareInferiori()) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setDatiDocType(datiDocTypePolo);
				return areaDatiControlliPoloVO;
			}

			int numLegami = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
					.getLegamiDocumentoCount();

			if (numLegami == 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setDatiDocType(datiDocTypePolo);
				return areaDatiControlliPoloVO;
			}

			ModificaType modificaType = null;

			modificaType = new ModificaType();
			modificaType.setCattura(true);
			modificaType.setTipoControllo(SbnSimile.CONFERMA);

			DocumentoType documentoType = new DocumentoType();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			documentoTypeChoice.setDatiDocumento(datiDocTypePolo);

			documentoType.setDocumentoTypeChoice(documentoTypeChoice);
			int contaArrivoLegami = 0;

			LegamiType legamiTypeDaCancellare = new LegamiType();
			DocumentoType documentoTypePerCanc = documentoType;
			documentoTypePerCanc.clearLegamiDocumento();


			boolean cancelAllAuthor = false;

			for (int i = 0; i < numLegami; i++) {
				ArrivoLegame arrivoLegame = null;

				contaArrivoLegami = sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getDocumento(0).getLegamiDocumento(i)
						.getArrivoLegameCount();

				for (int j = 0; j < contaArrivoLegami; j++) {
					arrivoLegame = sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getDocumento(0).getLegamiDocumento(i)
							.getArrivoLegame(j);
					if (arrivoLegame.getLegameDoc() != null) {
						// Modifica ALMAVIVA2 anche i legami 464 (M464N) non
						// devono essere cancellati come i 463;
						if (arrivoLegame.getLegameDoc().getTipoLegame().toString().equals("463")
								|| arrivoLegame.getLegameDoc().getTipoLegame().toString().equals("464")
						// ||
						// arrivoLegame.getLegameDoc().getTipoLegame().toString().equals("461")
						// Niente da fare
						) {
						} else if (arrivoLegame.getLegameDoc().getTipoLegame().toString().equals("461")
								&& sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice()
										.getSbnOutput().getDocumento(0)
										.getDocumentoTypeChoice()
										.getDatiDocumento() != null
								&& sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice()
										.getSbnOutput().getDocumento(0)
										.getDocumentoTypeChoice()
										.getDatiDocumento().getNaturaDoc()
										.toString().equals("W")) {
							// Niente da fare
						} else if (arrivoLegame.getLegameDoc().getCondiviso() != null
								&& arrivoLegame.getLegameDoc().getCondiviso().toString().equals("n")) {
							// Niente da fare
						} else {
							String chiaveLegame = sbnRisposta.getSbnMessage()
									.getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput()
									.getDocumento(0).getLegamiDocumento(i)
									.getIdPartenza()
									+ arrivoLegame.getLegameDoc().getIdArrivo()
									+ arrivoLegame.getLegameDoc()
											.getTipoLegame().toString();

							boolean trovato = false;
							for (Object oggetto : listaOggetti) {
								if (((String) oggetto).contains(chiaveLegame)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta
												.getSbnMessage()
												.getSbnResponse()
												.getSbnResponseTypeChoice()
												.getSbnOutput().getDocumento(0)
												.getLegamiDocumento(i)
												.getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
							}
						}
					} else if (arrivoLegame.getLegameElementoAut() != null) {
						if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AB_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
							// niente da fare
						} else if (arrivoLegame.getLegameElementoAut().getCondiviso() != null
								&& arrivoLegame.getLegameElementoAut().getCondiviso().toString().equals("n")) {
							// niente da fare
						} else if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {

							// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
							// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
							// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
							// non lo consente e l'allineamento non si conslude correttamente) - viene utilizzato il campo cancelAllAuthor
							if (cancelAllAuthor) {
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
										.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								// Intervento BUG MANTIS 4623 (esercizio) - ALMAVIVA2 19.09.2011
								// viene eliminato il break nel ciclo di cancellazione legami-autori prima dell'aggiornamento
								// altrimenti non si cancellano tutti ilegami oltre il primo e si aggiunge un else
//								break;
							} else {
								// Intervento interno: nel caso di relator code null si deve impostare il codice di 3 spazi vuoti
								// e non il campo VUOTO altrimenti la verifica della modifica non andrebbe a buon fine
//								String appoRelatorCode = "";
								String appoRelatorCode = "   ";

								if (arrivoLegame.getLegameElementoAut().getRelatorCode() != null) {
									appoRelatorCode = arrivoLegame.getLegameElementoAut().getRelatorCode();
								}
								String chiaveLegamePolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
										.getDocumento(0).getLegamiDocumento(i).getIdPartenza() + arrivoLegame.getLegameElementoAut().getIdArrivo()
										+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString() + appoRelatorCode;

								boolean trovato = false;
								String chiaveLegameIndice = "";
								for (Object oggetto : listaOggetti) {
									chiaveLegameIndice = (String) oggetto;
									if (chiaveLegameIndice.contains(chiaveLegamePolo)) {
										trovato = true;
										break;
										// Manutenzione ALMAVIVA2 BUG MANTIS 4318  (esercizio)
										// correzione della Segnalazione: Protocollo di POLO: 3045 Errore: tipo di legame con autore non valido -
										// tipo legame autore eliminato mil controllo errato fra tipo-legame e relator code;
//										if (chiaveLegameIndice.substring(chiaveLegameIndice.length() - 3).equals(appoRelatorCode)) {
//										}
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
											.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);

									// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
									// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
									// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
									// non lo consente e l'allineamento non si conslude correttamente)
									// Fabbraio 2018 ALMAVIVA2 la cancellazione di tutti i legami autori nel caso di variazione
									// del legame ti tipo 1 (autore principale) va fatta sia per i legami con autori personali (700)
									// che con autori collettivo (710)
									//if (arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("700")) {
									if (arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("700") ||
											arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("710")) {
										cancelAllAuthor = true;
									}
									// Fine Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
								}

							}
							// Fine Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
						} else {
							String chiaveLegamePolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
									.getDocumento(0).getLegamiDocumento(i).getIdPartenza()
									+ arrivoLegame.getLegameElementoAut().getIdArrivo()
									+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString();

							boolean trovato = false;
							String chiaveLegameIndice = "";
							for (Object oggetto : listaOggetti) {
								chiaveLegameIndice = (String) oggetto;
								if (chiaveLegameIndice.contains(chiaveLegamePolo)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta
												.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
												.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
							}
						}
					} else if (arrivoLegame.getLegameTitAccesso() != null) {

						if (arrivoLegame.getLegameTitAccesso().getCondiviso() != null
								&& arrivoLegame.getLegameTitAccesso()
										.getCondiviso().toString().equals("n")) {
						} else {
							String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza()
									+ arrivoLegame.getLegameTitAccesso().getIdArrivo()
									+ arrivoLegame.getLegameTitAccesso().getTipoLegame().toString();

							boolean trovato = false;
							for (Object oggetto : listaOggetti) {
								if (((String) oggetto).contains(chiaveLegame)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
												.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
							}
						}
					}
				}
			}

			if (legamiTypeDaCancellare.getArrivoLegameCount() <= 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setDatiDocType(datiDocTypePolo);
				return areaDatiControlliPoloVO;
			}

			documentoTypePerCanc.addLegamiDocumento(legamiTypeDaCancellare);
			modificaType.setDocumento(documentoTypePerCanc);

			sbnrequest = new SbnRequestType();
			sbnmessage = new SbnMessageType();
			sbnmessage.setSbnRequest(sbnrequest);

			modificaType.setDocumento(documentoType);
			sbnrequest.setModifica(modificaType);

			// CHIAMATA AL PROTOCOLLO
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
				return areaDatiControlliPoloVO;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResult().getEsito());
				areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta
						.getSbnMessage().getSbnResponse().getSbnResult()
						.getTestoEsito());
				return areaDatiControlliPoloVO;
			}

			datiDocTypePolo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
					.getDocumentoTypeChoice().getDatiDocumento();

			areaDatiControlliPoloVO.setCodErr("0000");
			areaDatiControlliPoloVO.setDatiDocType(datiDocTypePolo);
			return areaDatiControlliPoloVO;

		} catch (SbnMarcException ve) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ ie.getMessage());
		} catch (Exception e) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ e.getMessage());
		}
		return areaDatiControlliPoloVO;
	}

	public String inserisciTitUniformeCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO,
			boolean soloRadice, boolean provAllineamento) {

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		String tipoAuthority = "";
		String naturaTU = "A";

		if (areaDatiCatturaReticoloVO.getTipoAuthority() == null) {
			tipoAuthority = "TU";
		} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("")
				|| areaDatiCatturaReticoloVO.getTipoAuthority().equals("TU")) {
			tipoAuthority = "TU";
		} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("UM")) {
			tipoAuthority = "UM";
		}

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(areaDatiCatturaReticoloVO
				.getDatiElementoType().getT001());
		if (soloRadice) {
			areaDatiControlliPoloVO.setCancellareInferiori(false);
		} else {
			areaDatiControlliPoloVO.setCancellareInferiori(true);
		}


		// ALMAVIVA2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
		if (areaDatiCatturaReticoloVO.getDatiElementoType() != null) {
			if (areaDatiCatturaReticoloVO.getDatiElementoType().getNaturaTU() != null) {
				if (areaDatiCatturaReticoloVO.getDatiElementoType().getNaturaTU().equals("V")) {
					naturaTU = "V";
				}
			}
		}

		if (naturaTU.equals("A")) {
			areaDatiControlliPoloVO = getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

			if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
				return areaDatiControlliPoloVO.getTestoProtocollo();
			}
		}


		TitAccessoType titAccessoTypePolo = null;
		DatiElementoType datiElementoTypePolo = null;
		String dataVariazionePolo = "";

		if (areaDatiControlliPoloVO.getTitAccessoType() == null
				&& areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		}
		if (areaDatiControlliPoloVO.getDatiElementoType() != null) {
			datiElementoTypePolo = areaDatiControlliPoloVO
					.getDatiElementoType();
			dataVariazionePolo = datiElementoTypePolo.getT005();
			insert = false;
		}
		if (areaDatiControlliPoloVO.getTitAccessoType() != null) {
			titAccessoTypePolo = areaDatiControlliPoloVO.getTitAccessoType();
			dataVariazionePolo = titAccessoTypePolo.getT005();
			insert = false;
		}

		SBNMarc sbnRisposta = null;

		try {

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;

			ModificaType modificaType = null;

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				modificaType.setCattura(true);
				areaDatiCatturaReticoloVO.getDatiElementoType().setT005(
						dataVariazionePolo);
			}

			ElementAutType elementAutType = new ElementAutType();
			elementAutType.setDatiElementoAut(areaDatiCatturaReticoloVO
					.getDatiElementoType());

			if (insert == true) {
				creaTypeChoice.setElementoAut(elementAutType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut()
						.setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			sbnmessage.setSbnRequest(sbnrequest);
			this.polo.setMessage(sbnmessage, this.user);

			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3115")) {
				return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				tabellaTimeStamp.put(areaDatiCatturaReticoloVO.getDatiElementoType().getT001(), datiElementoTypePolo.getT005());
			} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3115")) {
			} else {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO
							.getDatiElementoType().getT001(), sbnRisposta
							.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getElementoAut(0).getDatiElementoAut().getT005());
				} else {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO
							.getDatiElementoType().getT001(), sbnRisposta
							.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getDocumento(0).getDocumentoTypeChoice()
							.getDatiDocumento().getT005());
				}
			}

			// Inserimento della localizzazione in Indice per l'oggetto inserito
			// Modifica ALMAVIVA2 03.03.2010 - per non localizzare quando su
			// proviene dall'allineamento
			if (!provAllineamento) {
				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(areaDatiCatturaReticoloVO.getDatiElementoType().getT001());
				areaLocalizza.setAuthority(tipoAuthority);
				areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
				areaLocalizza.setTipoOpe("Localizza");
				areaLocalizza.setTipoLoc("Gestione");
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
				areaDatiPassReturn = localizzaAuthority(areaLocalizza);
				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return areaDatiPassReturn.getTestoProtocollo();
				}
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}

		return "0000";

	}

	public AreaDatiControlliPoloVO getTitNoDocPuliziaLegamiPolo(
			AreaDatiControlliPoloVO areaDatiControlliPoloVO) {

		areaDatiControlliPoloVO.setCodErr("0000");

		TitAccessoType titAccessoTypePolo = new TitAccessoType();
		DatiElementoType datiElementoTypePolo = new DatiElementoType();

		SBNMarc sbnRisposta = null;
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

			cercaDatiTitTypeChoice.setT001(areaDatiControlliPoloVO
					.getIdRicerca());
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			// CHIAMATA AL PROTOCOLLO
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiControlliPoloVO.setCodErr("9999");
				areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
				return areaDatiControlliPoloVO;
			}

			// Inizio ALMAVIVA2 10.11.2009 Richiesta interna - per errore di
			// marshalling (0101) o qualunque altro errore
			// deve bloccarsi e non continuare come fosse non trovato

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiControlliPoloVO;
			}
			// Fine ALMAVIVA2 10.11.2009 Richiesta interna

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe() == 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				return areaDatiControlliPoloVO;
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
				titAccessoTypePolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
						.getDocumentoTypeChoice().getDatiTitAccesso();
				if (!areaDatiControlliPoloVO.isCancellareInferiori()) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setTitAccessoType(titAccessoTypePolo);
					return areaDatiControlliPoloVO;
				}

				int numLegami = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
						.getDocumento(0).getLegamiDocumentoCount();

				if (numLegami == 0) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setTitAccessoType(titAccessoTypePolo);
					return areaDatiControlliPoloVO;
				}

				ModificaType modificaType = null;

				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);

				DocumentoType documentoType = new DocumentoType();
				DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

				documentoTypeChoice.setDatiTitAccesso(titAccessoTypePolo);

				documentoType.setDocumentoTypeChoice(documentoTypeChoice);

				int contaArrivoLegami = 0;
				LegamiType legamiTypeDaCancellare = new LegamiType();
				DocumentoType documentoTypePerCanc = documentoType;
				documentoTypePerCanc.clearLegamiDocumento();




				boolean cancelAllAuthor = false;


				for (int i = 0; i < numLegami; i++) {
					ArrivoLegame arrivoLegame = null;

					contaArrivoLegami = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getArrivoLegameCount();

					for (int j = 0; j < contaArrivoLegami; j++) {
						arrivoLegame = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
								.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getArrivoLegame(j);
						if (arrivoLegame.getLegameDoc() != null) {

							if (arrivoLegame.getLegameDoc().getCondiviso() != null
									&& arrivoLegame.getLegameDoc().getCondiviso().toString().equals("n")) {

							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
										.getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza()
										+ arrivoLegame.getLegameDoc().getIdArrivo()
										+ arrivoLegame.getLegameDoc().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getDocumento(0).getLegamiDocumento(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						} else if (arrivoLegame.getLegameElementoAut() != null) {
							if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AB_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
								// ... niente da fare
							} else if (arrivoLegame.getLegameElementoAut().getCondiviso() != null
									&& arrivoLegame.getLegameElementoAut().getCondiviso().toString().equals("n")) {
								// ...niente da fare
							} else if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {


								// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
								// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
								// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
								// non lo consente e l'allineamento non si conslude correttamente) - viene utilizzato il campo cancelAllAuthor
								if (cancelAllAuthor) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getDocumento(0).getLegamiDocumento(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
									// Intervento BUG MANTIS 4623 (esercizio) - ALMAVIVA2 19.09.2011
									// viene eliminato il break nel ciclo di cancellazione legami-autori prima dell'aggiornamento
									// altrimenti non si cancellano tutti ilegami oltre il primo e si aggiunge un else
//									break;
								} else {
									// Modifica ALMAVIVA2 del 07.09.2012 replicata dal metoto getDocumentoPuliziaLegamiPolo
									// Intervento interno: nel caso di relator code null si deve impostare il codice di 3 spazi vuoti
									// e non il campo VUOTO altrimenti la verifica della modifica non andrebbe a buon fine
	//								String appoRelatorCode = "";
									String appoRelatorCode = "   ";

									if (arrivoLegame.getLegameElementoAut().getRelatorCode() != null) {
										appoRelatorCode = arrivoLegame.getLegameElementoAut().getRelatorCode();
									}
									String chiaveLegamePolo = sbnRisposta.getSbnMessage().getSbnResponse()
											.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza()
											+ arrivoLegame.getLegameElementoAut().getIdArrivo()
											+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString()
											+ appoRelatorCode;

									boolean trovato = false;
									String chiaveLegameIndice = "";
									for (Object oggetto : listaOggetti) {
										chiaveLegameIndice = (String) oggetto;
										if (chiaveLegameIndice.contains(chiaveLegamePolo)) {
											trovato = true;
											break;
											// Modifica ALMAVIVA2 del 07.09.2012 replicata dal metoto getDocumentoPuliziaLegamiPolo
											// Manutenzione ALMAVIVA2 BUG MANTIS 4318  (esercizio)
											// correzione della Segnalazione: Protocollo di POLO: 3045 Errore: tipo di legame con autore non valido -
											// tipo legame autore eliminato mil controllo errato fra tipo-legame e relator code;
	//										if (chiaveLegameIndice.substring(chiaveLegameIndice.length() - 3).equals(appoRelatorCode)) {
	//										}
										}
									}
									if (!trovato) {
										legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
										legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
														.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
														.getDocumento(0).getLegamiDocumento(i).getIdPartenza());
										legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);

										// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
										// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
										// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
										// non lo consente e l'allineamento non si conslude correttamente)
										if (arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("700")) {
											cancelAllAuthor = true;
										}
										// Fine Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011

									}
								}
							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza()
										+ arrivoLegame.getLegameElementoAut().getIdArrivo()
										+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getDocumento(0).getLegamiDocumento(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						} else if (arrivoLegame.getLegameTitAccesso() != null) {
							if (arrivoLegame.getLegameTitAccesso().getCondiviso() != null
									&& arrivoLegame.getLegameTitAccesso().getCondiviso().toString().equals("n")) {
							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getLegamiDocumento(i).getIdPartenza()
										+ arrivoLegame.getLegameTitAccesso().getIdArrivo()
										+ arrivoLegame.getLegameTitAccesso().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getDocumento(0).getLegamiDocumento(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						}
					}
				}

				if (legamiTypeDaCancellare.getArrivoLegameCount() <= 0) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setTitAccessoType(titAccessoTypePolo);
					return areaDatiControlliPoloVO;
				}

				documentoTypePerCanc.addLegamiDocumento(legamiTypeDaCancellare);
				modificaType.setDocumento(documentoTypePerCanc);

				sbnrequest = new SbnRequestType();
				sbnmessage = new SbnMessageType();
				sbnmessage.setSbnRequest(sbnrequest);

				modificaType.setDocumento(documentoType);
				sbnrequest.setModifica(modificaType);

				// CHIAMATA AL PROTOCOLLO
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
					return areaDatiControlliPoloVO;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiControlliPoloVO;
				}
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setTitAccessoType(titAccessoTypePolo);

			} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
				datiElementoTypePolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0).getDatiElementoAut();

				if (!areaDatiControlliPoloVO.isCancellareInferiori()) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
					return areaDatiControlliPoloVO;
				}

				int numLegami = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getLegamiElementoAutCount();

				if (numLegami == 0) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
					return areaDatiControlliPoloVO;
				}

				ModificaType modificaType = null;

				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);

				ElementAutType elementAutType = new ElementAutType();

				elementAutType.setDatiElementoAut(datiElementoTypePolo);

				int contaArrivoLegami = 0;
				LegamiType legamiTypeDaCancellare = new LegamiType();
				ElementAutType elementAutTypePerCanc = elementAutType;
				elementAutTypePerCanc.clearLegamiElementoAut();

				boolean cancelAllAuthor = false;

				for (int i = 0; i < numLegami; i++) {
					ArrivoLegame arrivoLegame = null;

					contaArrivoLegami = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getElementoAut(0).getLegamiElementoAut(i).getArrivoLegameCount();

					for (int j = 0; j < contaArrivoLegami; j++) {
						arrivoLegame = sbnRisposta.getSbnMessage()
								.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
								.getLegamiElementoAut(i).getArrivoLegame(j);
						if (arrivoLegame.getLegameDoc() != null) {

							if (arrivoLegame.getLegameDoc().getCondiviso() != null
									&& arrivoLegame.getLegameDoc().getCondiviso().toString().equals("n")) {
							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
										.getLegamiElementoAut(i).getIdPartenza()
										+ arrivoLegame.getLegameDoc().getIdArrivo()
										+ arrivoLegame.getLegameDoc().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						} else if (arrivoLegame.getLegameElementoAut() != null) {
							if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AB_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE
									|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
							} else if (arrivoLegame.getLegameElementoAut().getCondiviso() != null
									&& arrivoLegame.getLegameElementoAut().getCondiviso().toString().equals("n")) {

							} else if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {


								// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
								// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
								// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
								// non lo consente e l'allineamento non si conslude correttamente) - viene utilizzato il campo cancelAllAuthor
								if (cancelAllAuthor) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
									// Intervento BUG MANTIS 4623 (esercizio) - ALMAVIVA2 19.09.2011
									// viene eliminato il break nel ciclo di cancellazione legami-autori prima dell'aggiornamento
									// altrimenti non si cancellano tutti ilegami oltre il primo e si aggiunge un else
//									break;
								} else {

									// Intervento interno del 29.10.2012 da Allineamento LIG
									// modificata impostazione del valore di default del relator code così da indurre la cancellazione
									// del legame in oggetto se gia esiste su Polo senza il relator code valorizzato mentre l'aggiornamento proveniente
									// dall'Indice contiene il relator code valorizzato (senza la modifica si produce la segnalazione: 7016,
									// "Errore: Legame non valido. Esiste già lo stesso legame con campo relazione non impostato. Modificare quello esistente

									// ALMAVIVA2: BUG esercizio 5739 e 5746 febbraio 2015
									// viene inserita la gestione del campo appoRelatorCodeVuoto perchè SOLO quando sia in Indice che in Polo
									// il relator code è vuoto si deve fare il doppio controllo con relator code = "   " e "";
//									String appoRelatorCode = "";
									String appoRelatorCode = "   ";

									if (arrivoLegame.getLegameElementoAut().getRelatorCode() != null) {
										appoRelatorCode = arrivoLegame.getLegameElementoAut().getRelatorCode();
									}

									String chiaveLegamePolo = sbnRisposta.getSbnMessage().getSbnResponse()
											.getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
											.getLegamiElementoAut(i).getIdPartenza()
											+ arrivoLegame.getLegameElementoAut().getIdArrivo()
											+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString()
											+ appoRelatorCode;

									boolean trovato = false;
									String chiaveLegameIndice = "";
									for (Object oggetto : listaOggetti) {
										chiaveLegameIndice = (String) oggetto;

										if (chiaveLegameIndice.contains(chiaveLegamePolo)) {
											trovato = true;
											break;
											// Modifica ALMAVIVA2 del 07.09.2012 replicata dal metoto getDocumentoPuliziaLegamiPolo
											// Manutenzione ALMAVIVA2 BUG MANTIS 4318  (esercizio)
											// correzione della Segnalazione: Protocollo di POLO: 3045 Errore: tipo di legame con autore non valido -
											// tipo legame autore eliminato mil controllo errato fra tipo-legame e relator code;
		//									if (chiaveLegameIndice.substring(chiaveLegameIndice.length() - 3).equals(appoRelatorCode)) {
		//									}
										}
									}
									if (!trovato) {
										legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
										legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
														.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
														.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
										legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);

										// Inizio Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
										// Nel caso di variazione del vid di responsabilita principale (vale anche per lo scambio forma
										// è necessario cancellare anche tutti gli autori di responsabilità alternativa altrimenti il protocollo
										// non lo consente e l'allineamento non si conslude correttamente)
										if (arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("700")) {
											cancelAllAuthor = true;
										}
										// Fine Intervento BUG MANTIS 4607 (esercizio) - ALMAVIVA2 30.08.2011
									}
								}
							} else if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {

								// Intervento ALMAVIVA2 - BUG Mantis 5566 esercizio. ne caso di legame a repertori si cancella sempre
								// perchè il reinserimento garantisce la ricerca sulla tabella dei repertori e dopo l'allineamento della
								// anagrafica dei repertori cambia il valore del FLAG_TROVATO che altrimenti non verrebbe mai riaggiornato.
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
												.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
												.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);

							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
										.getLegamiElementoAut(i).getIdPartenza()
										+ arrivoLegame.getLegameElementoAut().getIdArrivo()
										+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						} else if (arrivoLegame.getLegameTitAccesso() != null) {
							if (arrivoLegame.getLegameTitAccesso().getCondiviso() != null
									&& arrivoLegame.getLegameTitAccesso().getCondiviso().toString().equals("n")) {
							} else {
								String chiaveLegame = sbnRisposta.getSbnMessage().getSbnResponse()
										.getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
										.getLegamiElementoAut(i).getIdPartenza()
										+ arrivoLegame.getLegameTitAccesso().getIdArrivo()
										+ arrivoLegame.getLegameTitAccesso().getTipoLegame().toString();

								boolean trovato = false;
								for (Object oggetto : listaOggetti) {
									if (((String) oggetto).contains(chiaveLegame)) {
										trovato = true;
										break;
									}
								}
								if (!trovato) {
									legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
									legamiTypeDaCancellare.setIdPartenza(sbnRisposta.getSbnMessage()
													.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
													.getElementoAut(0).getLegamiElementoAut(i).getIdPartenza());
									legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
								}
							}
						}
					}
				}

				if (legamiTypeDaCancellare.getArrivoLegameCount() <= 0) {
					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
					return areaDatiControlliPoloVO;
				}

				elementAutTypePerCanc.addLegamiElementoAut(legamiTypeDaCancellare);
				modificaType.setElementoAut(elementAutTypePerCanc);

				sbnrequest = new SbnRequestType();
				sbnmessage = new SbnMessageType();
				sbnmessage.setSbnRequest(sbnrequest);

				modificaType.setElementoAut(elementAutType);
				sbnrequest.setModifica(modificaType);

				// CHIAMATA AL PROTOCOLLO
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

				if (sbnRisposta == null) {
					areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
					return areaDatiControlliPoloVO;
				}

				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiControlliPoloVO;
				}

				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
					datiElementoTypePolo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getElementoAut(0).getDatiElementoAut();

					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
					return areaDatiControlliPoloVO;
				} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumentoCount() > 0) {
					datiElementoTypePolo = new DatiElementoType();
					datiElementoTypePolo.setT005(sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
							.getDocumentoTypeChoice().getDatiDocumento().getT005());

					areaDatiControlliPoloVO.setCodErr("0000");
					areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);

				}

				return areaDatiControlliPoloVO;
			}

		} catch (SbnMarcException ve) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		}
		return areaDatiControlliPoloVO;

	}

	public String inserisciTitAccessoCatturato(
			TitAccessoType titAccessoTypeInd, boolean soloRadice,
			boolean provAllineamento) {

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		String tipoAuthority = "";

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(titAccessoTypeInd.getT001());
		if (soloRadice) {
			areaDatiControlliPoloVO.setCancellareInferiori(false);
		} else {
			areaDatiControlliPoloVO.setCancellareInferiori(true);
		}

		areaDatiControlliPoloVO = getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		TitAccessoType titAccessoTypePolo = null;
		DatiElementoType datiElementoTypePolo = null;
		String dataVariazionePolo = "";

		if (areaDatiControlliPoloVO.getTitAccessoType() == null
				&& areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		}
		if (areaDatiControlliPoloVO.getTitAccessoType() != null) {
			titAccessoTypePolo = areaDatiControlliPoloVO.getTitAccessoType();
			dataVariazionePolo = titAccessoTypePolo.getT005();
			insert = false;
		}
		if (areaDatiControlliPoloVO.getDatiElementoType() != null) {
			datiElementoTypePolo = areaDatiControlliPoloVO
					.getDatiElementoType();
			dataVariazionePolo = datiElementoTypePolo.getT005();
			insert = false;
		}

		SBNMarc sbnRisposta = null;

		try {

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;

			ModificaType modificaType = null;

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
			}

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();

			TitAccessoType titAccessoType = new TitAccessoType();
			DocumentoType documentoType = new DocumentoType();
			TitAccessoTypeChoice titAccessoTypeChoice = new TitAccessoTypeChoice();
			DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

			titAccessoType.setTitAccessoTypeChoice(titAccessoTypeChoice);
			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			documentoType.getDocumentoTypeChoice().setDatiTitAccesso(
					titAccessoTypeInd);

			sbnmessage.setSbnRequest(sbnrequest);

			if (insert == true) {
				creaTypeChoice.setDocumento(documentoType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				if (!dataVariazionePolo.equals("")) {
					documentoType.getDocumentoTypeChoice().getDatiTitAccesso()
							.setT005(dataVariazionePolo);
				}
				modificaType.setDocumento(documentoType);
				modificaType.getDocumento().setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito();
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("3090")) {
				tabellaTimeStamp.put(titAccessoTypeInd.getT001(),
						titAccessoTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(titAccessoTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getDocumento(0).getDocumentoTypeChoice()
						.getDatiDocumento().getT005());
			}

			// Inserimento della localizzazione in Indice per l'oggetto inserito
			// Modifica ALMAVIVA2 03.03.2010 - per non localizzare quando su
			// proviene dall'allineamento
			if (!provAllineamento) {
				areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(titAccessoTypeInd.getT001());
				areaLocalizza.setAuthority(tipoAuthority);
				areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser()
						.getBiblioteca());
				areaLocalizza.setTipoOpe("Localizza");
				areaLocalizza.setTipoLoc("Gestione");
				areaLocalizza.setIndice(true);
				areaLocalizza.setPolo(false);
				areaDatiPassReturn = localizzaAuthority(areaLocalizza);
				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					return areaDatiPassReturn.getTestoProtocollo();
				}

			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";

	}

	public String inserisciAutoreCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO,
			boolean soloRadice) {

		DatiElementoType datiElementoTypeInd = areaDatiCatturaReticoloVO.getDatiElementoType();
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		String tipoAuthority = "AU";
		SBNMarc sbnRisposta = null;

		boolean insert = false;
		// Inizio verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiElementoTypeInd.getT001());
		areaDatiControlliPoloVO.setTipoAut("AU");
		if (soloRadice) {
			areaDatiControlliPoloVO.setCancellareInferiori(false);
		} else {
			areaDatiControlliPoloVO.setCancellareInferiori(true);
		}

		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		} else {
			datiElementoTypePolo = areaDatiControlliPoloVO
					.getDatiElementoType();
			insert = false;
		}

		// Fine verifica di esistenza del Documento su Polo

		try {

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;
			ModificaType modificaType = null;

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				datiElementoTypeInd.setT005(datiElementoTypePolo.getT005());
			}

			// // INIZIO MODIFICA PER GESTIONE DELLE QUALIFICAZIONI PER AUTORE
			// PERSONALE
			if (datiElementoTypeInd instanceof AutorePersonaleType) {
				AutorePersonaleType autorePersonaleType = new AutorePersonaleType();
				autorePersonaleType = (AutorePersonaleType) datiElementoTypeInd;
				String nomePartePrimaria = utilityCastor
						.getNominativoDatiElemento(autorePersonaleType);
				String nomeParteSecondaria = null;
				A200 a200 = new A200();
				a200.setId2(Indicatore.VALUE_1);

				a200.setA_200(nomePartePrimaria);
				if (nomeParteSecondaria != null) {
					a200.setB_200(nomeParteSecondaria);
				}
				autorePersonaleType.setT200(a200);
			}
			// // FINE MODIFICA PER GESTIONE DELLE QUALIFICAZIONI

			ElementAutType elementAutType = new ElementAutType();
			elementAutType.setDatiElementoAut(datiElementoTypeInd);

			if (areaDatiCatturaReticoloVO.getLegamiType() != null) {
				LegamiType[] arrayLegamiTypeRep = new LegamiType[1];
				arrayLegamiTypeRep[0] = areaDatiCatturaReticoloVO
						.getLegamiType();
				arrayLegamiTypeRep[0].setTipoOperazione(SbnTipoOperazione.CREA);
				elementAutType.setLegamiElementoAut(arrayLegamiTypeRep);
			}

			if (insert == true) {
				creaTypeChoice.setElementoAut(elementAutType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut()
						.setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			sbnmessage.setSbnRequest(sbnrequest);
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3030")) {
				if (sbnmessage.getSbnRequest().getModifica().getElementoAut() != null) {
					if (sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAutCount() > 0) {
						sbnmessage.getSbnRequest().getModifica()
								.getElementoAut().getLegamiElementoAut(0)
								.setTipoOperazione(SbnTipoOperazione.MODIFICA);

						// Richiesta al protocollo
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							return "noServerLoc";
						}
					}
				} else {
					return "";
				}
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(),	datiElementoTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getDatiElementoAut().getT005());
			}
			if (insert == true) {

				// ALMAVIVA2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
				// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
				// del flag di avvenuto allineamento.
				if (!areaDatiCatturaReticoloVO.isAllineamentoDaFileLocale()) {
					// Inserimento della localizzazione in Indice per l'oggetto
					// inserito
					areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
					areaLocalizza.setIdLoc(datiElementoTypeInd.getT001());
					areaLocalizza.setAuthority(tipoAuthority);
					areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser()
							.getBiblioteca());
					areaLocalizza.setTipoOpe("Localizza");
					areaLocalizza.setTipoLoc("Gestione");
					areaLocalizza.setIndice(true);
					areaLocalizza.setPolo(false);
					areaDatiPassReturn = localizzaAuthority(areaLocalizza);
					if (!areaDatiPassReturn.getCodErr().equals("0000")) {
						return areaDatiPassReturn.getTestoProtocollo();
					}
				}
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";

	}

	public String inserisciInterpreteDocumentoCatturato(String idInterprete) {

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();

		// Inizio verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(idInterprete);
		areaDatiControlliPoloVO.setTipoAut("AU");
		areaDatiControlliPoloVO.setCancellareInferiori(false);
		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() != null) {
			return "0000";
		}
		// Fine verifica di esistenza del Documento su Polo

		if (datiElementoTypePolo == null) {
			// non esiste l'oggetto: va prima catturato
			AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
			areaTabellaOggettiDaCatturareVO.setIdPadre(idInterprete);
			areaTabellaOggettiDaCatturareVO.setTipoAuthority("AU");
			AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = this
					.catturaAutore(areaTabellaOggettiDaCatturareVO);
			if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
				areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
				areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
				return areaDatiPassReturn.getTestoProtocollo();
			}
		}
		return "0000";
	}

	public String inserisciIncipitTestualeCatturato(String idIncipitTestuale) {

		// AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		// String tipoAuthority = "";

		// boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(idIncipitTestuale);
		areaDatiControlliPoloVO.setCancellareInferiori(false);

		areaDatiControlliPoloVO = getTitNoDocPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		// TitAccessoType titAccessoTypePolo = null;
		// DatiElementoType datiElementoTypePolo = null;
		// String dataVariazionePolo = "";

		if (areaDatiControlliPoloVO.getTitAccessoType() == null
				&& areaDatiControlliPoloVO.getDatiElementoType() == null) {
			// non esiste l'oggetto: va prima catturato
			AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
			areaTabellaOggettiDaCatturareVO.setIdPadre(idIncipitTestuale);
			// areaTabellaOggettiDaCatturareVO.setTipoAuthority("AU");
			AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = this.catturaReticolo(areaTabellaOggettiDaCatturareVO);
			if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000")) {
				areaDatiPassReturn.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
				areaDatiPassReturn.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
				return areaDatiPassReturn.getTestoProtocollo();
			}
		} else {
			return "0000";
		}
		return "0000";
	}

	public String inserisciRinvioCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO) {

		DatiElementoType datiElementoTypeInd = areaDatiCatturaReticoloVO.getDatiElementoType();
		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();

		String tipoAuthority = areaDatiCatturaReticoloVO.getDatiElementoType().getTipoAuthority().toString();

		SBNMarc sbnRisposta = null;

		boolean insert = false;
		boolean insertLegame = false;
		// Inizio verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiElementoTypeInd.getT001());
		areaDatiControlliPoloVO.setTipoAut(tipoAuthority);
		areaDatiControlliPoloVO.setFormaNome("R");
		areaDatiControlliPoloVO.setCancellareInferiori(true);
		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		String timeStampModif = "";
		if (areaDatiControlliPoloVO.getTimeStampRinvio().equals("")) {
			if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
				insert = true;
			} else {
				timeStampModif = areaDatiControlliPoloVO.getDatiElementoType().getT005();
				insert = false;
				insertLegame = true;

			}
		} else {
			timeStampModif = areaDatiControlliPoloVO.getTimeStampRinvio();
			insert = false;
		}
		// Fine verifica di esistenza del Documento su Polo

		try {

			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;
			ModificaType modificaType = null;

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				datiElementoTypeInd.setT005(timeStampModif);
			}

			// Inizio modifica ALMAVIVA2 09.07.2010 - Riportata la stessa
			// modifica fatta per gli Autori in forma accettata
			// // INIZIO MODIFICA PER GESTIONE DELLE QUALIFICAZIONI PER AUTORE
			// PERSONALE
			if (datiElementoTypeInd instanceof AutorePersonaleType) {
				AutorePersonaleType autorePersonaleType = new AutorePersonaleType();
				autorePersonaleType = (AutorePersonaleType) datiElementoTypeInd;
				String nomePartePrimaria = utilityCastor.getNominativoDatiElemento(autorePersonaleType);
				String nomeParteSecondaria = null;
				A200 a200 = new A200();
				a200.setId2(Indicatore.VALUE_1);

				a200.setA_200(nomePartePrimaria);
				if (nomeParteSecondaria != null) {
					a200.setB_200(nomeParteSecondaria);
				}
				autorePersonaleType.setT200(a200);
			}
			// // FINE MODIFICA PER GESTIONE DELLE QUALIFICAZIONI
			// Fine modifica ALMAVIVA2 09.07.2010 - Riportata la stessa
			// modifica fatta per gli Autori in forma accettata

			ElementAutType elementAutType = new ElementAutType();

			elementAutType.setDatiElementoAut(datiElementoTypeInd);

			// Inserimento della parte legami nel caso di forma di RINVIO
			if (datiElementoTypeInd.getFormaNome().getType() == SbnFormaNome.R_TYPE) {

				LegamiType[] arrayLegamiType = new LegamiType[1];
				LegamiType legamiType;
				ArrivoLegame arrivoLegame;

				legamiType = new LegamiType();
				legamiType.setIdPartenza(areaDatiCatturaReticoloVO.getLegameElementoAutType().getIdArrivo());

				arrivoLegame = new ArrivoLegame();

				LegameElementoAutType legameElementoAut = new LegameElementoAutType();
				legameElementoAut.setNoteLegame(areaDatiCatturaReticoloVO.getLegameElementoAutType().getNoteLegame());
				legameElementoAut.setTipoAuthority(SbnAuthority.valueOf(tipoAuthority));
				legameElementoAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));
				legameElementoAut.setIdArrivo(areaDatiCatturaReticoloVO.getIdOggetto());
				arrivoLegame.setLegameElementoAut(legameElementoAut);

				ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
				arrayArrivoLegame[0] = arrivoLegame;

				legamiType.setArrivoLegame(arrayArrivoLegame);
				if (insert == true) {
					legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
				} else {
					if (insertLegame == true) {
						legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
					} else {
						legamiType.setTipoOperazione(SbnTipoOperazione.MODIFICA);
					}
				}

				arrayLegamiType[0] = legamiType;

				elementAutType.setLegamiElementoAut(arrayLegamiType);

			}

			if (insert == true) {
				creaTypeChoice.setElementoAut(elementAutType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut().setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			sbnmessage.setSbnRequest(sbnrequest);
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3336")) {
				String esito = inserisciRinvioCatturato(areaDatiCatturaReticoloVO);
				return esito;
			}

			// Inizio modifica ALMAVIVA2 02.03.2011 BUG MANTIS 4276 (esercizio): nel caso di inserimento di una forma di
			// rinvio che sia già legata ad altra forma accettata si torna indietro senza fermare la cattura ed inviando
			// solo la messaggistica in quanto tale possibilità si verifica solo in caso di fusione autori e lo
			// spostamento delle fome di rinvio avviene automaticamente al momento dela fusione in locale;
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3029")) {
				return "0000";
			}


			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(),	areaDatiControlliPoloVO.getTimeStampRinvio());
			} else {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(), sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT005());
			}

			if (insert == true) {
				if (!tipoAuthority.equals("LU")) {

					// ALMAVIVA2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
					// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
					// del flag di avvenuto allineamento.
					if (!areaDatiCatturaReticoloVO.isAllineamentoDaFileLocale()) {
						// Inserimento della localizzazione in Indice per l'oggetto
						// inserito
						areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
						areaLocalizza.setIdLoc(datiElementoTypeInd.getT001());
						areaLocalizza.setAuthority(tipoAuthority);
						areaLocalizza.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
						areaLocalizza.setTipoOpe("Localizza");
						areaLocalizza.setTipoLoc("Gestione");
						areaLocalizza.setIndice(true);
						areaLocalizza.setPolo(false);
						areaDatiPassReturn = localizzaAuthority(areaLocalizza);
						if (!areaDatiPassReturn.getCodErr().equals("0000")) {
							return areaDatiPassReturn.getTestoProtocollo();
						}
					}
				}
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";

	}

	public AreaDatiControlliPoloVO getAuthorityPuliziaLegamiPolo(
			AreaDatiControlliPoloVO areaDatiControlliPoloVO) {

		areaDatiControlliPoloVO.setCodErr("0000");
		areaDatiControlliPoloVO.setTimeStampRinvio("");
		DatiElementoType datiElementoTypePolo = new DatiElementoType();
		SBNMarc sbnRisposta = null;
		try {

			SbnMessageType sbnmessage = new SbnMessageType();

			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaElementoAutType cercaElemento = new CercaElementoAutType();

			if (areaDatiControlliPoloVO.getTipoAut().equals("AU")) {
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
				canali.setT001(areaDatiControlliPoloVO.getIdRicerca());

				cercaAutoreType.setCanaliCercaDatiAut(canali);
				cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
				cercaElemento.setCercaDatiAut(cercaAutoreType);
			} else if (areaDatiControlliPoloVO.getTipoAut().equals("MA")) {
				CercaMarcaType cercaMarcaType = new CercaMarcaType();
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

				// tipoOutput analitica 000
				cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				cercaType.setNumPrimo(1);
				cercaType.setMaxRighe(1);

				// tipo ORDINAMENTO
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
				cercaType.setCercaElementoAut(cercaElemento);

				// MID
				canali.setT001(areaDatiControlliPoloVO.getIdRicerca());

				cercaMarcaType.setCanaliCercaDatiAut(canali);
				cercaMarcaType.setTipoAuthority(SbnAuthority.MA);
				cercaElemento.setCercaDatiAut(cercaMarcaType);
			} else if (areaDatiControlliPoloVO.getTipoAut().equals("LU")) {
				CercaLuogoType cercaLuogoType = new CercaLuogoType();
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

				// tipoOutput analitica 000
				cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
				cercaType.setNumPrimo(1);
				cercaType.setMaxRighe(1);

				// tipo ORDINAMENTO
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0); // Corrisponde a 1
				cercaType.setCercaElementoAut(cercaElemento);

				// MID
				canali.setT001(areaDatiControlliPoloVO.getIdRicerca());

				cercaLuogoType.setCanaliCercaDatiAut(canali);
				cercaLuogoType.setTipoAuthority(SbnAuthority.LU);
				cercaElemento.setCercaDatiAut(cercaLuogoType);
			}

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			// CHIAMATA AL PROTOCOLLO
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiControlliPoloVO.setCodErr("9999");
				areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
				return areaDatiControlliPoloVO;
			}

			// Inizio ALMAVIVA2 10.11.2009 Richiesta interna - per errore di
			// marshalling (0101) o qualunque altro errore
			// deve bloccarsi e non continuare come fosse non trovato

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
				areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta
						.getSbnMessage().getSbnResponse().getSbnResult()
						.getTestoEsito());
				return areaDatiControlliPoloVO;
			}
			// Fine ALMAVIVA2 10.11.2009 Richiesta interna

			if (sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput().getTotRighe() == 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				return areaDatiControlliPoloVO;
			}

			if (areaDatiControlliPoloVO.getFormaNome() == null) {
				areaDatiControlliPoloVO.setFormaNome("");
			}
			if ((areaDatiControlliPoloVO.getTipoAut().equals("AU")
					|| areaDatiControlliPoloVO.getTipoAut().equals("LU"))
					&& areaDatiControlliPoloVO.getFormaNome().equals("R")) {
				int numLegami = sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getLegamiElementoAutCount();

				for (int i = 0; i < numLegami; i++) {

					int numSubLegami = sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice()
							.getSbnOutput().getElementoAut(0)
							.getLegamiElementoAut(i).getArrivoLegameCount();

					for (int j = 0; j < numSubLegami; j++) {
						if (sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResponseTypeChoice().getSbnOutput()
								.getElementoAut(0).getLegamiElementoAut(i)
								.getArrivoLegame(j).getLegameElementoAut()
								.getElementoAutLegato().getDatiElementoAut()
								.getT001().equals(
										areaDatiControlliPoloVO.getIdRicerca())) {
							areaDatiControlliPoloVO
									.setTimeStampRinvio(sbnRisposta
											.getSbnMessage().getSbnResponse()
											.getSbnResponseTypeChoice()
											.getSbnOutput().getElementoAut(0)
											.getLegamiElementoAut(i)
											.getArrivoLegame(j)
											.getLegameElementoAut()
											.getElementoAutLegato()
											.getDatiElementoAut().getT005());
							return areaDatiControlliPoloVO;
						}
					}
				}
			}

			if (areaDatiControlliPoloVO.getTipoAut().equals("MA")) {
				areaDatiControlliPoloVO.setElementAutType(sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0));
			}

			datiElementoTypePolo = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAut(0).getDatiElementoAut();

			if (!areaDatiControlliPoloVO.isCancellareInferiori()) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
				return areaDatiControlliPoloVO;
			}

			int numLegami = sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAut(0).getLegamiElementoAutCount();

			if (numLegami == 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
				return areaDatiControlliPoloVO;
			}

			ModificaType modificaType = null;

			modificaType = new ModificaType();
			modificaType.setCattura(true);
			modificaType.setTipoControllo(SbnSimile.CONFERMA);

			ElementAutType elementAutType = new ElementAutType();

			elementAutType.setDatiElementoAut(datiElementoTypePolo);

			int contaArrivoLegami = 0;
			LegamiType legamiTypeDaCancellare = new LegamiType();
			ElementAutType elementAutTypePerCanc = elementAutType;
			elementAutTypePerCanc.clearLegamiElementoAut();

			for (int i = 0; i < numLegami; i++) {
				ArrivoLegame arrivoLegame = null;

				contaArrivoLegami = sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0).getLegamiElementoAut(
								i).getArrivoLegameCount();

				for (int j = 0; j < contaArrivoLegami; j++) {
					arrivoLegame = sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResponseTypeChoice().getSbnOutput()
							.getElementoAut(0).getLegamiElementoAut(i)
							.getArrivoLegame(j);
					if (arrivoLegame.getLegameDoc() != null) {

						if (arrivoLegame.getLegameDoc().getCondiviso() != null
								&& arrivoLegame.getLegameDoc().getCondiviso()
										.toString().equals("n")) {
						} else {
							String chiaveLegame = sbnRisposta.getSbnMessage()
									.getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput()
									.getElementoAut(0).getLegamiElementoAut(i)
									.getIdPartenza()
									+ arrivoLegame.getLegameDoc().getIdArrivo()
									+ arrivoLegame.getLegameDoc()
											.getTipoLegame().toString();

							boolean trovato = false;
							for (Object oggetto : listaOggetti) {
								if (((String) oggetto).contains(chiaveLegame)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta
												.getSbnMessage()
												.getSbnResponse()
												.getSbnResponseTypeChoice()
												.getSbnOutput().getElementoAut(0)
												.getLegamiElementoAut(i)
												.getIdPartenza());
								legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
							}
						}
					} else if (arrivoLegame.getLegameElementoAut() != null) {
						if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AB_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE
								|| arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
						} else if (arrivoLegame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
							legamiTypeDaCancellare.setTipoOperazione(SbnTipoOperazione.CANCELLA);
							legamiTypeDaCancellare.setIdPartenza(sbnRisposta
									.getSbnMessage().getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput()
									.getElementoAut(0).getLegamiElementoAut(i)
									.getIdPartenza());
							legamiTypeDaCancellare.addArrivoLegame(arrivoLegame);
						} else if (arrivoLegame.getLegameElementoAut().getCondiviso() != null
								&& arrivoLegame.getLegameElementoAut().getCondiviso().toString().equals("n")) {
						} else {
							String chiaveLegame = "";
							if (arrivoLegame.getLegameElementoAut().getTipoLegame().toString().equals("4XX")) {
								chiaveLegame = arrivoLegame.getLegameElementoAut().getIdArrivo();
							} else {
								chiaveLegame = sbnRisposta.getSbnMessage()
										.getSbnResponse()
										.getSbnResponseTypeChoice()
										.getSbnOutput().getElementoAut(0)
										.getLegamiElementoAut(i)
										.getIdPartenza()
										+ arrivoLegame.getLegameElementoAut().getIdArrivo()
										+ arrivoLegame.getLegameElementoAut().getTipoLegame().toString();
							}

							boolean trovato = false;
							for (Object oggetto : listaOggetti) {
								if (((String) oggetto).contains(chiaveLegame)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare
										.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta
												.getSbnMessage()
												.getSbnResponse()
												.getSbnResponseTypeChoice()
												.getSbnOutput().getElementoAut(0)
												.getLegamiElementoAut(i)
												.getIdPartenza());
								legamiTypeDaCancellare
										.addArrivoLegame(arrivoLegame);
							}
						}
					} else if (arrivoLegame.getLegameTitAccesso() != null) {
						if (arrivoLegame.getLegameTitAccesso().getCondiviso() != null
								&& arrivoLegame.getLegameTitAccesso()
										.getCondiviso().toString().equals("n")) {
						} else {
							String chiaveLegame = sbnRisposta.getSbnMessage()
									.getSbnResponse()
									.getSbnResponseTypeChoice().getSbnOutput()
									.getElementoAut(0).getLegamiElementoAut(i)
									.getIdPartenza()
									+ arrivoLegame.getLegameTitAccesso()
											.getIdArrivo()
									+ arrivoLegame.getLegameTitAccesso()
											.getTipoLegame().toString();

							boolean trovato = false;
							for (Object oggetto : listaOggetti) {
								if (((String) oggetto).contains(chiaveLegame)) {
									trovato = true;
									break;
								}
							}
							if (!trovato) {
								legamiTypeDaCancellare
										.setTipoOperazione(SbnTipoOperazione.CANCELLA);
								legamiTypeDaCancellare
										.setIdPartenza(sbnRisposta
												.getSbnMessage()
												.getSbnResponse()
												.getSbnResponseTypeChoice()
												.getSbnOutput().getElementoAut(0)
												.getLegamiElementoAut(i)
												.getIdPartenza());
								legamiTypeDaCancellare
										.addArrivoLegame(arrivoLegame);
							}
						}
					}
				}
			}

			if (legamiTypeDaCancellare.getArrivoLegameCount() <= 0) {
				areaDatiControlliPoloVO.setCodErr("0000");
				areaDatiControlliPoloVO
						.setDatiElementoType(datiElementoTypePolo);
				return areaDatiControlliPoloVO;
			}

			elementAutTypePerCanc.addLegamiElementoAut(legamiTypeDaCancellare);
			modificaType.setElementoAut(elementAutTypePerCanc);

			sbnrequest = new SbnRequestType();
			sbnmessage = new SbnMessageType();
			sbnmessage.setSbnRequest(sbnrequest);

			modificaType.setElementoAut(elementAutType);
			sbnrequest.setModifica(modificaType);

			// CHIAMATA AL PROTOCOLLO
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiControlliPoloVO.setTestoProtocollo("noServerLoc");
				return areaDatiControlliPoloVO;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				areaDatiControlliPoloVO.setCodErr(sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResult().getEsito());
				areaDatiControlliPoloVO.setTestoProtocollo(sbnRisposta
						.getSbnMessage().getSbnResponse().getSbnResult()
						.getTestoEsito());
				return areaDatiControlliPoloVO;
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getElementoAutCount() > 0) {
				datiElementoTypePolo = sbnRisposta.getSbnMessage()
						.getSbnResponse().getSbnResponseTypeChoice()
						.getSbnOutput().getElementoAut(0).getDatiElementoAut();
			}

			areaDatiControlliPoloVO.setCodErr("0000");
			areaDatiControlliPoloVO.setDatiElementoType(datiElementoTypePolo);
			return areaDatiControlliPoloVO;

		} catch (SbnMarcException ve) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ ie.getMessage());
		} catch (Exception e) {
			areaDatiControlliPoloVO.setCodErr("9999");
			areaDatiControlliPoloVO.setTestoProtocollo("ERROR >>"
					+ e.getMessage());
		}
		return areaDatiControlliPoloVO;
	}

	public String inserisciMarcaSenzaRepertoriCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO) {

		DatiElementoType datiElementoTypeInd = new DatiElementoType();
		datiElementoTypeInd = areaDatiCatturaReticoloVO.getDatiElementoType();

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		String tipoAuthority = "MA";
		SBNMarc sbnRisposta = null;

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiElementoTypeInd.getT001());
		areaDatiControlliPoloVO.setTipoAut("MA");

		areaDatiControlliPoloVO.setCancellareInferiori(false);
		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		} else {
			return "0000";
		}

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;

			creaType = new CreaType();
			creaType.setTipoControllo(SbnSimile.CONFERMA);
			creaType.setCattura(true);
			creaTypeChoice = new CreaTypeChoice();

			ElementAutType elementAutType = new ElementAutType();
			elementAutType.setDatiElementoAut(datiElementoTypeInd);

			creaTypeChoice.setElementoAut(elementAutType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(creaType);

			sbnmessage.setSbnRequest(sbnrequest);

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(),
						datiElementoTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getDatiElementoAut().getT005());
			}

			// Inserimento della localizzazione in Indice per l'oggetto
			// inserito
			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
			areaLocalizza.setIdLoc(datiElementoTypeInd.getT001());
			areaLocalizza.setAuthority(tipoAuthority);
			areaLocalizza
					.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
			areaLocalizza.setTipoOpe("Localizza");
			areaLocalizza.setTipoLoc("Gestione");
			areaLocalizza.setIndice(true);
			areaLocalizza.setPolo(false);
			areaDatiPassReturn = localizzaAuthority(areaLocalizza);
			if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				return areaDatiPassReturn.getTestoProtocollo();
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";
	}

	public String inserisciMarcaConRepertoriCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO) {

		DatiElementoType datiElementoTypeInd = new DatiElementoType();
		datiElementoTypeInd = areaDatiCatturaReticoloVO.getDatiElementoType();

		AreaDatiLocalizzazioniAuthorityVO areaLocalizza;
		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		String tipoAuthority = "MA";
		SBNMarc sbnRisposta = null;

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiElementoTypeInd.getT001());
		areaDatiControlliPoloVO.setTipoAut("MA");

		// areaDatiControlliPoloVO.setCancellareInferiori(true);
		areaDatiControlliPoloVO.setCancellareInferiori(false);
		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		} else {
			datiElementoTypePolo = areaDatiControlliPoloVO
					.getDatiElementoType();
			insert = false;
		}

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;
			ModificaType modificaType = null;

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);
				datiElementoTypeInd.setT005(datiElementoTypePolo.getT005());
			}

			ElementAutType elementAutType = new ElementAutType();

			LegamiType[] arrayLegamiType = new LegamiType[1];
			arrayLegamiType[0] = areaDatiCatturaReticoloVO.getLegamiType();
			arrayLegamiType[0].setTipoOperazione(SbnTipoOperazione.CREA);
			elementAutType.setLegamiElementoAut(arrayLegamiType);

			elementAutType.setDatiElementoAut(datiElementoTypeInd);

			if (insert == true) {
				creaTypeChoice.setElementoAut(elementAutType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				// ==========================================================================================================
				// Prova per gestione dei repertori come in modifica marca
				// costruire un'apposito metodo per inviare solo la parte dei
				// legami della
				// marca ed avere al ritorno il List dei repertori
				String[] vecchieCitazioniPolo = null;
				if (areaDatiControlliPoloVO.getElementAutType() != null) {
					List repertoriPolo = utilityCastor
							.getListRepertori(areaDatiControlliPoloVO
									.getElementAutType());
					if (repertoriPolo != null) {
						int size = repertoriPolo.size();
						if (size > 0) {
							int numvecchieCitazioni = size + size;
							vecchieCitazioniPolo = new String[numvecchieCitazioni];
							Repertorio repertorio;
							int i = 0;
							for (int t = 0; t < size; t++) {
								repertorio = (Repertorio) repertoriPolo.get(t);
								vecchieCitazioniPolo[i] = repertorio
										.getCodiceRepertorio();
								i++;
								vecchieCitazioniPolo[i] = String
										.valueOf(repertorio.getCitazione());
								i++;
							}
						}
					}
				}

				String[] nuoveCitazioniIndice = null;
				List repertoriIndice = utilityCastor
						.getListRepertori(elementAutType);
				if (repertoriIndice != null) {
					int size = repertoriIndice.size();
					if (size > 0) {
						int numNuoveCitazioni = size + size;
						nuoveCitazioniIndice = new String[numNuoveCitazioni];
						Repertorio repertorio;
						int i = 0;
						for (int t = 0; t < size; t++) {
							repertorio = (Repertorio) repertoriIndice.get(t);
							nuoveCitazioniIndice[i] = repertorio
									.getCodiceRepertorio();
							i++;
							nuoveCitazioniIndice[i] = String.valueOf(repertorio
									.getCitazione());
							i++;
						}
					}
				}

				// List con le Citazioni standard su cui operare
				SbnGestioneMarcheDao gestioneMarcheDao = new SbnGestioneMarcheDao(
						indice, polo, user);

				AreaDatiPerConfrontoCitazioniMarcheVO areaPerCitaz = confrontoArrayCitazioni(
						vecchieCitazioniPolo, nuoveCitazioniIndice);

				// Elimina dal List tutte le Citazioni Standard che non
				// bisogna prendere in considerazione (stato INVARIATO)
				List vectorOperazioni = gestioneMarcheDao
						.impostaListOperazioniLegami(areaPerCitaz
								.getStatoNewCitazioni(), areaPerCitaz
								.getStatoOldCitazioni());

				// Array con i nuovi legami di inserimento/cancellazione
				LegamiType[] arrayLegamiTypeNuovo = new LegamiType[vectorOperazioni
						.size()];

				// Imposta i legami a Repertorio della Marca
				gestioneMarcheDao.setLegamiCitazioniPerVariazione(
						arrayLegamiTypeNuovo, vectorOperazioni,
						datiElementoTypeInd.getT001());

				// Aggiunta di tutti i Legami
				elementAutType.setLegamiElementoAut(arrayLegamiTypeNuovo);
				// ==========================================================================================================
				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut()
						.setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			sbnmessage.setSbnRequest(sbnrequest);

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(),
						datiElementoTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getDatiElementoAut().getT005());
			}

			// Inserimento della localizzazione in Indice per l'oggetto
			// inserito
			areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
			areaLocalizza.setIdLoc(datiElementoTypeInd.getT001());
			areaLocalizza.setAuthority(tipoAuthority);
			areaLocalizza
					.setCodiceSbn(sbnRisposta.getSbnUser().getBiblioteca());
			areaLocalizza.setTipoOpe("Localizza");
			areaLocalizza.setTipoLoc("Gestione");
			areaLocalizza.setIndice(true);
			areaLocalizza.setPolo(false);
			areaDatiPassReturn = localizzaAuthority(areaLocalizza);
			if (!areaDatiPassReturn.getCodErr().equals("0000")) {
				return areaDatiPassReturn.getTestoProtocollo();
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";
	}

	public AreaDatiPerConfrontoCitazioniMarcheVO confrontoArrayCitazioni(
			String[] vecchieCitazioni, String[] nuoveCitazioni) {

		AreaDatiPerConfrontoCitazioniMarcheVO areaDatiPerConfrontoCitazioniMarcheVO = new AreaDatiPerConfrontoCitazioniMarcheVO();
		List citazioniStandard = new ArrayList();
		List statoOldCitazioni = new ArrayList();
		String INSERIMENTO = "INSERIMENTO";
		String CANCELLAZIONE = "CANCELLAZIONE";
		String INVARIATO = "INVARIATO";

		int pos = 0;

		// CONFRONTO DI CIASCUNA DELLE NUOVE CITAZIONI CON
		// CIASCUNA DELLE VECCHIE CITAZIONI
		for (int i = 0; i < nuoveCitazioni.length; i = i + 2) {
			boolean inserimento = false;
			boolean invariato = false;

			if (vecchieCitazioni == null) {
				// QUESTA SITUAZIONE NON DOVREBBE MAI VERIFICARSI IN QUANTO OGNI
				// MARCA DEVE NECESSARIAMENTE AVERE ALMENO UNA CITAZIONE
				// STANDARD.
				// Vecchie citazioni assenti (base dati corrotta). Non c'è alcun
				// termine di paragone; le nuove Citazioni sono tutti
				// INSERIMENTI.
				inserimento = true;
				pos++;
			} else {
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
					// QUESTA SITUAZIONE NON DOVREBBE MAI VERIFICARSI IN QUANTO
					// OGNI MARCA DEVE NECESSARIAMENTE AVERE ALMENO UNA
					// CITAZIONE STANDARD.
					// Vecchie citazioni assenti (base dati corrotta). Non c'è
					// alcun termine di paragone; le nuove Citazioni sono tutti
					// INSERIMENTI.
					inserimento = true;
					pos++;
				}

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
		if (vecchieCitazioni != null) {
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
		}

		areaDatiPerConfrontoCitazioniMarcheVO
				.setStatoNewCitazioni(citazioniStandard);
		areaDatiPerConfrontoCitazioniMarcheVO
				.setStatoOldCitazioni(statoOldCitazioni);
		return areaDatiPerConfrontoCitazioniMarcheVO;

	}// end confrontoArrayCitazioni

	public String inserisciLuogoCatturato(AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO, boolean soloRadice) {

		// evolutive ottobre 2015 ALMAVIVA2 -  Nella gestione dei luoghi viene data la possibilità di gestire
		// i campi nota informativa , nota catalogatore e legame a repertori
		DatiElementoType datiElementoTypeInd = areaDatiCatturaReticoloVO.getDatiElementoType();

		AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
		areaDatiPassReturn.setCodErr("0000");
		SBNMarc sbnRisposta = null;

		boolean insert = false;
		// verifica di esistenza del Documento su Polo
		AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
		areaDatiControlliPoloVO.setIdRicerca(datiElementoTypeInd.getT001());
		areaDatiControlliPoloVO.setTipoAut("LU");
		if (soloRadice) {
			areaDatiControlliPoloVO.setCancellareInferiori(false);
		} else {
			areaDatiControlliPoloVO.setCancellareInferiori(true);
		}

		areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

		if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
			return areaDatiControlliPoloVO.getTestoProtocollo();
		}

		DatiElementoType datiElementoTypePolo = null;
		if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
			insert = true;
		} else {
			datiElementoTypePolo = areaDatiControlliPoloVO
					.getDatiElementoType();
			insert = false;
		}

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType creaType = null;
			CreaTypeChoice creaTypeChoice = null;
			ModificaType modificaType = null;

			if (insert == true) {
				creaType = new CreaType();
				creaType.setTipoControllo(SbnSimile.CONFERMA);
				creaType.setCattura(true);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modificaType = new ModificaType();
				modificaType.setCattura(true);
				modificaType.setTipoControllo(SbnSimile.CONFERMA);

				LuogoType luogoTypePolo = new LuogoType();
				luogoTypePolo = (LuogoType) datiElementoTypePolo;
				LuogoType luogoTypeIndice = new LuogoType();
				luogoTypeIndice = (LuogoType) datiElementoTypeInd;

				luogoTypeIndice.setT005(luogoTypePolo.getT005());

				// BUG mantis 5270 collaudo - Maggio 2013 - Il paesa non è obbligatoria nel modifica/crea dell'authority luogo
				// viene eliminata la forzatura
//				luogoTypeIndice.getT260().setA_260("IT");

				datiElementoTypeInd = luogoTypeIndice;
			}

			ElementAutType elementAutType = new ElementAutType();

			elementAutType.setDatiElementoAut(datiElementoTypeInd);


			// evolutive ottobre 2015 ALMAVIVA2 -  Nella gestione dei luoghi viene data la possibilità di gestire
			// i campi nota informativa , nota catalogatore e legame a repertori
			if (areaDatiCatturaReticoloVO.getLegamiType() != null) {
				LegamiType[] arrayLegamiTypeRep = new LegamiType[1];
				arrayLegamiTypeRep[0] = areaDatiCatturaReticoloVO
						.getLegamiType();
				arrayLegamiTypeRep[0].setTipoOperazione(SbnTipoOperazione.CREA);
				elementAutType.setLegamiElementoAut(arrayLegamiTypeRep);
			}



			if (insert == true) {
				creaTypeChoice.setElementoAut(elementAutType);
				creaType.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(creaType);
			} else {
				modificaType.setElementoAut(elementAutType);
				modificaType.getElementoAut().getDatiElementoAut()
						.setStatoRecord(StatoRecord.C);
				sbnrequest.setModifica(modificaType);
			}

			sbnmessage.setSbnRequest(sbnrequest);

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("3090")) {
				return sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito();
			}
			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("3090")) {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(),
						datiElementoTypePolo.getT005());
			} else {
				tabellaTimeStamp.put(datiElementoTypeInd.getT001(), sbnRisposta
						.getSbnMessage().getSbnResponse()
						.getSbnResponseTypeChoice().getSbnOutput()
						.getElementoAut(0).getDatiElementoAut().getT005());
			}

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
		return "0000";
	}

	public String inserisciLegameTitoloCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO, String ticket) {

		SBNMarc sbnRisposta = null;

		// evolutive ottobre 2015 ALMAVIVA2 - a seguito di creazione/copia di uno spoglio su madre già collocata
		// viene estesa alla N nuova la localizzazione per possesso della madre
		String bidMadrePerPropagazione = "";
		String tipoLegamePerPropagazione = "";

		try {

			ModificaType modificaType = null;
			modificaType = new ModificaType();

			modificaType.setTipoControllo(SbnSimile.CONFERMA);
			modificaType.setCattura(true);
			DocumentoType documentoType = new DocumentoType();
			DocumentoTypeChoice documentoTypeChoice;
			String timeStampOggetto = "";
			switch (areaDatiCatturaReticoloVO.getNatura().charAt(0)) {
			case 'A':
			case 'C':
			case 'M':
			case 'S':
			case 'W':
			case 'N':
				DatiDocType datiDocType = new DatiDocType();
				documentoTypeChoice = new DocumentoTypeChoice();

				datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(areaDatiCatturaReticoloVO.getNatura()));
				datiDocType.setTipoMateriale(SbnMateriale.valueOf(areaDatiCatturaReticoloVO.getTipoMateriale()));
				datiDocType.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				datiDocType.setT005(timeStampOggetto);
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));
				documentoTypeChoice.setDatiDocumento(datiDocType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			case 'B':
			case 'D':
			case 'P':
			case 'T':
				TitAccessoType titAccessoType = new TitAccessoType();
				documentoTypeChoice = new DocumentoTypeChoice();

				titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(areaDatiCatturaReticoloVO.getNatura()));
				titAccessoType.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				titAccessoType.setT005(timeStampOggetto);
				titAccessoType.setLivelloAut(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));

				documentoTypeChoice.setDatiTitAccesso(titAccessoType);
				documentoType.setDocumentoTypeChoice(documentoTypeChoice);
				break;
			}

			LegamiType[] arrayLegamiType = new LegamiType[1];

			LegamiType legamiType;
			ArrivoLegame arrivoLegame;
			legamiType = new LegamiType();

			// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
			// la cattura avviene solo per le authority selzionate con il check (come per le W)
			if (ValidazioneDati.isFilled(areaDatiCatturaReticoloVO.getCorrispondenzaCidIndCidPol())) {
				legamiType.setIdPartenza(areaDatiCatturaReticoloVO.getCorrispondenzaCidIndCidPol());
			} else {
				legamiType.setIdPartenza(areaDatiCatturaReticoloVO.getIdOggetto());
			}


			legamiType.setTipoOperazione(SbnTipoOperazione.MODIFICA);

			arrivoLegame = new ArrivoLegame();

			if (areaDatiCatturaReticoloVO.getLegameElementoAutType() != null) {
				areaDatiCatturaReticoloVO.getLegameElementoAutType().setElementoAutLegato(null);
				if (areaDatiCatturaReticoloVO.getLegameElementoAutType().getRelatorCode() != null) {
					if (areaDatiCatturaReticoloVO.getLegameElementoAutType().getRelatorCode().equals("   ")) {
						areaDatiCatturaReticoloVO.getLegameElementoAutType().setRelatorCode(null);
					}
				}
				arrivoLegame.setLegameElementoAut(areaDatiCatturaReticoloVO.getLegameElementoAutType());
				if (arrivoLegame.getLegameElementoAut().getCondiviso() == null) {
					arrivoLegame.getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);
				}
				// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
				// la cattura avviene solo per le authority selzionate con il check (come per le W)
				if (ValidazioneDati.isFilled(areaDatiCatturaReticoloVO.getCorrispondenzaCidIndCidPol())) {
					arrivoLegame.getLegameElementoAut().setRank(1);
					arrivoLegame.getLegameElementoAut().setIdArrivo(areaDatiCatturaReticoloVO.getCorrispondenzaCidIndCidPol());
				}



			} else if (areaDatiCatturaReticoloVO.getLegameDocType() != null) {
				areaDatiCatturaReticoloVO.getLegameDocType().setDocumentoLegato(null);
				arrivoLegame.setLegameDoc(areaDatiCatturaReticoloVO.getLegameDocType());
				bidMadrePerPropagazione = areaDatiCatturaReticoloVO.getLegameDocType().getIdArrivo();
				tipoLegamePerPropagazione = areaDatiCatturaReticoloVO.getLegameDocType().getTipoLegame().toString();
				if (arrivoLegame.getLegameDoc().getCondiviso() == null) {
					arrivoLegame.getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
				}
			} else if (areaDatiCatturaReticoloVO.getLegameTitAccessoType() != null) {
				areaDatiCatturaReticoloVO.getLegameTitAccessoType().setTitAccessoLegato(null);
				arrivoLegame.setLegameTitAccesso(areaDatiCatturaReticoloVO.getLegameTitAccessoType());

				// BUG esercizio 6616: ALMAVIVA2 Giugno 2018 nel caso in cui in fase di allineamento arrivi
				// una notizia con titpo materiale diverso da MUSICA e sottotipoLegame valorizzato questo deve essere pulito
				// per non lasciare un legame con attributo non valido INIZIO
				if (!areaDatiCatturaReticoloVO.getTipoMateriale().equals("U")) {
					if (arrivoLegame.getLegameTitAccesso().getSottoTipoLegame() != null) {
						arrivoLegame.getLegameTitAccesso().setSottoTipoLegame(null);
					}
				}
				// BUG esercizio 6616: ALMAVIVA2 Giugno 2018 FINE
				if (arrivoLegame.getLegameTitAccesso().getCondiviso() == null) {
					arrivoLegame.getLegameTitAccesso().setCondiviso(LegameTitAccessoTypeCondivisoType.S);
				}
			}

			arrivoLegame.setLegameElementoAut(areaDatiCatturaReticoloVO.getLegameElementoAutType());



			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[0] = legamiType;

			documentoType.setLegamiDocumento(arrayLegamiType);
			modificaType.setDocumento(documentoType);

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();

			sbnrequest.setModifica(modificaType);
			sbnmessage.setSbnRequest(sbnrequest);

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);

			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3030")) {

				if (sbnmessage.getSbnRequest().getModifica().getDocumento() != null) {
					if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumentoCount() > 0) {
						sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);

						// Richiesta al protocollo
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							return "noServerLoc";
						}
					}
				} else {
					return "";
				}
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3029")
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3093")) {

				if (sbnmessage.getSbnRequest().getModifica().getDocumento() != null) {
					if (sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumentoCount() > 0) {
						sbnmessage.getSbnRequest().getModifica().getDocumento().getLegamiDocumento(0).setTipoOperazione(SbnTipoOperazione.CREA);

						// Richiesta al protocollo
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							return "noServerLoc";
						}
					}
				} else {
					return "";
				}
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
						.getDocumento(0).getDocumentoTypeChoice().getDatiDocumento() != null) {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO.getIdOggetto(), sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
							.getDocumentoTypeChoice().getDatiDocumento().getT005());
				} else {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO.getIdOggetto(), sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
							.getDocumentoTypeChoice().getDatiTitAccesso().getT005());
				}

				// evolutive ottobre 2015 ALMAVIVA2 - a seguito di creazione/copia di uno spoglio su madre già collocata
				// viene estesa alla N nuova la localizzazione per possesso della madre
				if (areaDatiCatturaReticoloVO.getNatura().equals("N") && tipoLegamePerPropagazione.equals("461")) {
					SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
					String origineArrivo = "PoloPolo";
					String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
							bidMadrePerPropagazione, areaDatiCatturaReticoloVO.getIdOggetto(), SbnMateriale.valueOf("M").toString(),
							sbnRisposta.getSbnUser().getBiblioteca(),
							origineArrivo);
					if (!codStringaErrore.substring(0,4).equals("0000")) {
						return codStringaErrore;
					}
				}

				//almaviva5_20180321 attiva link condivisione titolo-soggetto
				LegameElementoAutType legameAut = areaDatiCatturaReticoloVO.getLegameElementoAutType();
				if (legameAut != null && ValidazioneDati.eqAuthority(legameAut.getTipoAuthority(), SbnAuthority.SO)) {
					getSoggetti().attivaCondivisioneTitoloSoggetto(ticket,
							legameAut.getIdArrivo(),
							areaDatiCatturaReticoloVO.getIdOggetto());
				}

				return "";
			}
			return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}
	}

	public String inserisciLegameAuthorityCatturato(
			AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO) {

		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = new ModificaType();
			modifica.setCattura(true);

			ElementAutType elementAutType = new ElementAutType();
			String timeStampOggetto = "";
			if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("AU")) {

				// L'ELEMENTO PADRE E' UN AUTORE (ENTE)

				EnteType ente = new EnteType();

				// TIPO AUTHORITY
				ente.setTipoAuthority(SbnAuthority.AU);
				ente.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				ente.setFormaNome(SbnFormaNome.A);
				ente.setLivelloAut(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));

				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				ente.setT005(timeStampOggetto);
				elementAutType.setDatiElementoAut(ente);
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("MA")) {
				MarcaType marca = new MarcaType();
				marca.setTipoAuthority(SbnAuthority.MA);
				marca.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				marca.setLivelloAut(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));
				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				marca.setT005(timeStampOggetto);
				elementAutType.setDatiElementoAut(marca);
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("TU")) {
				TitoloUniformeType titoloUniformeType = new TitoloUniformeType();
				titoloUniformeType.setTipoAuthority(SbnAuthority.TU);
				titoloUniformeType.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				titoloUniformeType.setLivelloAut(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));
				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				titoloUniformeType.setT005(timeStampOggetto);
				elementAutType.setDatiElementoAut(titoloUniformeType);
			} else if (areaDatiCatturaReticoloVO.getTipoAuthority().equals("UM")) {
				TitoloUniformeMusicaType titoloUniformeMusicaType = new TitoloUniformeMusicaType();
				titoloUniformeMusicaType.setTipoAuthority(SbnAuthority.UM);
				titoloUniformeMusicaType.setT001(areaDatiCatturaReticoloVO.getIdOggetto());
				titoloUniformeMusicaType.setLivelloAut(SbnLivello.valueOf(areaDatiCatturaReticoloVO.getLivAut()));
				timeStampOggetto = tabellaTimeStamp.get(areaDatiCatturaReticoloVO.getIdOggetto());
				titoloUniformeMusicaType.setT005(timeStampOggetto);
				elementAutType.setDatiElementoAut(titoloUniformeMusicaType);
			}

			LegamiType[] arrayLegamiType = new LegamiType[1];

			LegamiType legamiType;
			ArrivoLegame arrivoLegame;

			legamiType = new LegamiType();

			legamiType.setIdPartenza(areaDatiCatturaReticoloVO.getIdOggetto());
			legamiType.setTipoOperazione(SbnTipoOperazione.MODIFICA);
			// legamiType.setTipoOperazione(SbnTipoOperazione.CREA);

			arrivoLegame = new ArrivoLegame();

			if (areaDatiCatturaReticoloVO.getLegameElementoAutType() != null) {
				areaDatiCatturaReticoloVO.getLegameElementoAutType().setElementoAutLegato(null);
				arrivoLegame.setLegameElementoAut(areaDatiCatturaReticoloVO.getLegameElementoAutType());
				if (arrivoLegame.getLegameElementoAut().getCondiviso() == null) {
					arrivoLegame.getLegameElementoAut().setCondiviso(LegameElementoAutTypeCondivisoType.S);
				}
			} else if (areaDatiCatturaReticoloVO.getLegameDocType() != null) {
				areaDatiCatturaReticoloVO.getLegameDocType().setDocumentoLegato(null);
				arrivoLegame.setLegameDoc(areaDatiCatturaReticoloVO.getLegameDocType());
				if (arrivoLegame.getLegameDoc().getCondiviso() == null) {
					arrivoLegame.getLegameDoc().setCondiviso(LegameDocTypeCondivisoType.S);
				}
			} else if (areaDatiCatturaReticoloVO.getLegameTitAccessoType() != null) {
				areaDatiCatturaReticoloVO.getLegameTitAccessoType().setTitAccessoLegato(null);
				arrivoLegame.setLegameTitAccesso(areaDatiCatturaReticoloVO.getLegameTitAccessoType());
				if (arrivoLegame.getLegameTitAccesso().getCondiviso() == null) {
					arrivoLegame.getLegameTitAccesso().setCondiviso(LegameTitAccessoTypeCondivisoType.S);
				}
			}

			arrivoLegame.setLegameElementoAut(areaDatiCatturaReticoloVO.getLegameElementoAutType());
			ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
			arrayArrivoLegame[0] = arrivoLegame;

			legamiType.setArrivoLegame(arrayArrivoLegame);
			arrayLegamiType[0] = legamiType;

			// aggiungo IL LEGAME
			elementAutType.setLegamiElementoAut(arrayLegamiType);

			// FINE FORMA ACCETTATA
			modifica.setElementoAut(elementAutType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setModifica(modifica);

			// Richiesta al protocollo
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				return "noServerLoc";
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3014")) {
				// ricerca del timeStamp dell'oggetto su Polo
				AreaDatiControlliPoloVO areaDatiControlliPoloVO = new AreaDatiControlliPoloVO();
				areaDatiControlliPoloVO.setIdRicerca(areaDatiCatturaReticoloVO.getIdOggetto());
				areaDatiControlliPoloVO.setTipoAut(areaDatiCatturaReticoloVO.getTipoAuthority());
				areaDatiControlliPoloVO.setCancellareInferiori(false);
				areaDatiControlliPoloVO = getAuthorityPuliziaLegamiPolo(areaDatiControlliPoloVO);

				if (!areaDatiControlliPoloVO.getCodErr().equals("0000")) {
					return areaDatiControlliPoloVO.getTestoProtocollo();
				}

				if (areaDatiControlliPoloVO.getDatiElementoType() == null) {
					return "noServerLoc";
				} else {
					String timeStampPolo = areaDatiControlliPoloVO.getDatiElementoType().getT005();
					sbnmessage.getSbnRequest().getModifica().getElementoAut().getDatiElementoAut().setT005(timeStampPolo);
					// Richiesta al protocollo
					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();

					if (sbnRisposta == null) {
						return "noServerLoc";
					}
				}
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3029")
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3093")) {
				sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAut(0).setTipoOperazione(SbnTipoOperazione.CREA);
				// Richiesta al protocollo
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "noServerLoc";
				}
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3030")) {
				sbnmessage.getSbnRequest().getModifica().getElementoAut().getLegamiElementoAut(0).setTipoOperazione(SbnTipoOperazione.MODIFICA);
				// Richiesta al protocollo
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					return "noServerLoc";
				}
			}

			if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount() > 0) {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO.getIdOggetto(), sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0)
							.getDatiElementoAut().getT005());
					return "";
				} else {
					tabellaTimeStamp.put(areaDatiCatturaReticoloVO.getIdOggetto(), sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0)
							.getDocumentoTypeChoice().getDatiDocumento().getT005());
					return "";
				}

			}
			return sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

		} catch (SbnMarcException ve) {
			return "ERROR >>" + ve.getMessage();
		} catch (Exception e) {
			return "ERROR >>" + e.getMessage();
		}

	}

	public AreaDatiVariazioneReturnVO scatturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass) {

		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();
		areaDatiVariazioneReturnVO.setCodErr("0000");

		// ==================================================================================================
		// LA FUNZIONE E' STATA IN TUTTO SOSTITUITA DA CANCELLA AUTHORITY CHE
		// EFFETTUA ENTRAMBE LE FUNZIONI
		// ==================================================================================================
		return areaDatiVariazioneReturnVO;
	}

	/**
	 * Questo metodo fa una richiesta di cancellazione Titolo al protocollo
	 *
	 * @param Bid
	 *            del titolo.
	 * @return Oggetto Castor con la risposta
	 */

	public AreaDatiPassaggioCancAuthorityVO cancellaAuthority(
			AreaDatiPassaggioCancAuthorityVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		areaDatiPass.setCodErr("0000");

		// Evolutiva Gennaio 2016 : viene inserita una nuova funzionalità per cancellare uno spoglio (natura N) di Monografia (M)
		// che senza effettuare alcun controllo su gestionali effettua la canecllazione/delocalizzazione dello Spoglio in polo per
		// tutte le biblioteche, e, se lo spoglio non è gestito da altri Poli lo delocalizza/cancella anche da Indice;
		// se invece è gestito da altri Poli lo delocalizza/cancella solo per tutte le biblio del polo operante
		if (areaDatiPass.getTipoOperazione().equals("CancellaSpoglioDiMonografia")) {
			cancellaSpoglio(areaDatiPass);
			return areaDatiPass;
		}
		// Fine Evolutiva Gennaio 2016 : viene inserita una nuova funzionalità per cancellare uno spoglio





		boolean ordiniPolo = false;
		boolean inventPolo = false;
		boolean collocPolo = false;
		boolean esemplPolo = false;

		boolean inventDismessiBiblio = false;
		boolean inventAttiviPolo = false;
		boolean inventDismessiPolo = false;

		// 05.02.2013 ALMAVIVA2 su richiesta a voce almaviva1
		// nuovo campo x gestire la messaggistica
		boolean altriPoliIndice = false;
		boolean altreBiblIndicePerPolo = false;
		//almaviva5_20111115 controllo esistenza legami a fascicoli
		boolean fascicoliPolo = false;

		boolean delocalizzaBiblio = true;
		boolean delocalizzaIndice = true;
		boolean cancellaBiblio = false;
		boolean cancellaIndice = false;

		String diagnosticaPerLuoghi = "";

		// Modifica ALMAVIVA2 BUG MANTIS COLLAUDO 4403
		// modificati tutti i diagnostici informativi (non solo quelli degli ordini) secondo le seguenti indicazioni:
		// Nel messaggio che segue al tentativo di delocalizzare un documento
		// (Attenzione esistono Ordini legati al Documento per la Biblioteca di cui si è richiesta la cancellazione)
		// la parola 'cancellazione' andrebbe sostituita con 'delocalizzazione';
		// inoltre le parole '...per la Biblioteca...' potrebbero essere eliminato.

		try {

			// Inizio controlli su presenza di gestionali ancora validi
			// Area acquisizioni:
			if (areaDatiPass.getTipoAut() == null
					|| areaDatiPass.getTipoAut().equals("")) {
				StrutturaCombo StrutturaCombo;
				ListaSuppOrdiniVO listaSuppOrdiniVO = new ListaSuppOrdiniVO();
				GenericJDBCAcquisizioniDAO acquisizioni = new GenericJDBCAcquisizioniDAO();
				StrutturaCombo = new StrutturaCombo(areaDatiPass.getBid(), "");
				listaSuppOrdiniVO.setTitolo(StrutturaCombo);
				List<OrdiniVO> ordini = null;
				try {
					ordini = acquisizioni.getRicercaListaOrdini(listaSuppOrdiniVO);
				} catch (ValidationException ve) {
					if (ve.getError() == 4001) {
						ordiniPolo = false;
					} else {
						areaDatiPass.setCodErr("9999");
						// Gestione Bibliografica: Bug Mantis esercizio 5582 viene trasmesso la segnalazione riportata dal metodo acquisizioni.getRicercaListaOrdini
						// al posto di quello fittizio presente prima
//						areaDatiPass
//								.setTestoProtocollo("Attenzione esistono Ordini legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
						areaDatiPass
						.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza ordini su Polo ha riscontrato il seguente malfunzionamento: "
								+ ve.getMessage());

						return areaDatiPass;
					}
				} catch (Exception e) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass
							.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza ordini su Polo ha riscontrato il seguente malfunzionamento: "
									+ e.getMessage());
					return areaDatiPass;
				}
				if (ordini != null && ordini.size() > 0) {
					ordiniPolo = true;
					for (int index = 0; index < ordini.size(); index++) {
						OrdiniVO recResult = ordini.get(index);
						if (recResult.getCodBibl().equals(this.user.getBiblioteca().substring(3, 6))) {
							areaDatiPass.setCodErr("9999");
							areaDatiPass
									.setTestoProtocollo("Attenzione esistono Ordini legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
							return areaDatiPass;
						}
					}
				}

				// Area documento Fisico:
				Tbc_inventarioDao inventari = new Tbc_inventarioDao();
				List tbcInventario = inventari.getListaInventari(areaDatiPass.getBid());

				if (tbcInventario.size() > 0) {
					inventPolo = true;

					// Modifica ALMAVIVA2 2014.07.21 BUG MANTIS esercizio 5602 - se la richiesta di cancellazione
					// documento viene effettuata da un polo che ha tutti gli inventari del documento allo stato
					// di dismesso (cd-sit=3) il sw procederà alla delocalizzazione per possesso sia in indice che in polo,
					// senza cancellare ne dal polo ne dall'Indice la notizia
					for (int index = 0; index < tbcInventario.size(); index++) {
						Tbc_inventario recResult = (Tbc_inventario) tbcInventario.get(index);
						if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
							inventDismessiPolo = true;
						} else {
							inventAttiviPolo = true;
							break;
						}
					}

					// Modifica ALMAVIVA2 2014.07.21 BUG MANTIS esercizio 5602 - se la richiesta di delocalizzazione
					// documento viene effettuata da una biblioteca che ha tutti gli inventari del documento allo stato
					// di dismesso (cd-sit=3) il sw procederà alla delocalizzazione per possesso sia in indice che in polo,
					// senza cancellare dal polo la notizia (anche se gestita solo dalla biblioteca operante)
					// e quindi senza delocalizzazione per gestione in Indice
					for (int index = 0; index < tbcInventario.size(); index++) {
						Tbc_inventario recResult = (Tbc_inventario) tbcInventario.get(index);
						if (recResult.getCd_serie().getCd_polo().getCd_biblioteca().equals(this.user.getBiblioteca().substring(3, 6))) {
							if (recResult.getCd_sit() == DocFisico.Inventari.INVENTARIO_DISMESSO_CHR) {
								inventDismessiBiblio = true;
							} else {
								areaDatiPass.setCodErr("9999");
								areaDatiPass
										.setTestoProtocollo("Attenzione esistono Inventari legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
								return areaDatiPass;
							}
						}
					}
				} else {
					// NON ESISTONO INVENTARI PER IL POLO CANCELLAZIONE OK
					inventPolo = false;
				}

				Tbc_collocazioneDao collocazioni = new Tbc_collocazioneDao();
				List<Tbc_collocazione> tbcCollocazione = collocazioni.getListaCollocazioni(areaDatiPass.getBid());

				if (tbcCollocazione.size() > 0) {
					collocPolo = true;
					for (int index = 0; index < tbcCollocazione.size(); index++) {
						Tbc_collocazione recResult = tbcCollocazione.get(index);
						if (recResult.getCd_sez().getCd_polo().getCd_biblioteca().equals(this.user.getBiblioteca().substring(3,	6))) {
							areaDatiPass.setCodErr("9999");
							areaDatiPass
									.setTestoProtocollo("Attenzione esistono Collocazioni legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
							return areaDatiPass;
						}
					}
				} else {
					// NON ESISTONO COLLOCAZIONI PER IL POLO CANCELLAZIONE OK
					collocPolo = false;
				}

				Tbc_esemplare_titoloDao esemplari = new Tbc_esemplare_titoloDao();
				List tbcEsemplare = esemplari.getListaEsemplari(areaDatiPass.getBid());

				if (tbcEsemplare.size() > 0) {
					for (int index = 0; index < tbcEsemplare.size(); index++) {
						esemplPolo = true;
						Tbc_esemplare_titolo recResult = (Tbc_esemplare_titolo) tbcEsemplare.get(index);
						if (recResult.getCd_polo().getCd_biblioteca().equals(this.user.getBiblioteca().substring(3, 6))) {
							areaDatiPass.setCodErr("9999");
							areaDatiPass
									.setTestoProtocollo("Attenzione esistono Esemplari legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
							return areaDatiPass;
						}
					}
				} else {
					// NON ESISTONO ESEMPLARI PER IL POLO CANCELLAZIONE OK
					esemplPolo = false;
				}
			}

			//almaviva5_20111115 controllo esistenza legami a fascicoli
			try {
				//almaviva5_20140721 #5612 biblioteche con fascicoli per questo bid
				//String annate = getPeriodiciBean().getAnnateFascicoliTitolo(areaDatiPass.getBid());
				//fascicoli = ValidazioneDati.isFilled(annate);
				Map<String, Integer> fascicoliBib = getPeriodiciBean().countEsemplariBiblioteche(areaDatiPass.getBid());
				fascicoliPolo = ValidazioneDati.isFilled(fascicoliBib);	//esistono esemplari in polo

				//esistono es. fascicoli per questa biblioteca
				if (fascicoliBib.containsKey(this.user.getBiblioteca().substring(3, 6))) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo("Attenzione esistono fascicoli legati al Documento di cui si è richiesta la delocalizzazione/cancellazione");
					return areaDatiPass;
				}

			} catch (SbnBaseException e) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza fascicoli ha riscontrato il seguente malfunzionamento: " + e.getErrorCode());
				return areaDatiPass;
			} catch (Exception e) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza fascicoli ha riscontrato il seguente malfunzionamento: " + e.getMessage());
				return areaDatiPass;
			}

			// Fine controlli su presenza di gestionali ancora validi

			//almaviva5_20111115 controllo esistenza legami a fascicoli
			//if (!ordiniPolo && !inventPolo && !collocPolo && !esemplPolo) {
			if (!ordiniPolo && !inventPolo && !collocPolo && !esemplPolo && !fascicoliPolo) {
				delocalizzaBiblio = true;
				delocalizzaIndice = true;
				cancellaBiblio = true;
				cancellaIndice = true;
			}

			if (areaDatiPass.getTipoAut() != null
					&& areaDatiPass.getTipoAut().toString().equals("LU")) {
				delocalizzaBiblio = false;
				delocalizzaIndice = false;
			}

			// Delocalizzazione in Polo per l'oggetto inserito
			if (delocalizzaBiblio) {
				AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
				areaDatiPassReturn.setCodErr("0000");
				AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
				areaLocalizza.setIdLoc(areaDatiPass.getBid());
				if (areaDatiPass.getTipoAut() == null) {
					areaLocalizza.setAuthority("");
				} else {
					areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
				}
				areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
				areaLocalizza.setTipoOpe("Delocalizza");
				areaLocalizza.setTipoLoc("Tutti");
				areaLocalizza.setIndice(false);
				areaLocalizza.setPolo(true);
				areaDatiPassReturn = localizzaAuthority(areaLocalizza);


				// Inizio Modifica ALMAVIVA2 del 16.11.2010 BUG Mantis 3982 nel caso di codice di ritorno 3001 e 3013 dall'operazione
				// di ritorno di cancellazione localizzazione non si invia diagnostica in quanto l'effetto di eliminare la
				// localizzazione è comunque stato raggiunto (SbnGestioneAllAuthorityDao.cancellaAuthority);

				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
					if (!areaDatiPassReturn.getTestoProtocollo().contains("3001") && !areaDatiPassReturn.getTestoProtocollo().contains("3013")) {
						areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
						areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
						return areaDatiPass;
					}
				}
//				if (!areaDatiPassReturn.getCodErr().equals("0000")) {
//					areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
//					areaDatiPass.setTestoProtocollo(areaDatiPassReturn
//							.getTestoProtocollo());
//					return areaDatiPass;
//				}
				// Fine Modifica ALMAVIVA2 del 16.11.2010 BUG Mantis 3982
			}

			// Inizio Modifica ALMAVIVA2 2010.11.15 BUG MANTIS 3982 - non si deve effettuare la cancellazione in polo
			// dell'oggetto se esistono in Indice localizzazioni per altre biblioteche del Polo stesso (SbnGestioneAllAuthorityDao.cancellaAuthority);
			if (cancellaBiblio) {
				// Inizio Modifica ALMAVIVA2 2011.02.01 Telefonata Aste/Scognamiglio: Per i titoli solo locali nono si deve effettuare
				// alcuna verifica sulle localizzazioni in Indice che ovviamente non esistono (SbnGestioneAllAuthorityDao.cancellaAuthority);

				if (areaDatiPass.isRicercaIndice()) {

					// Modifica ALMAVIVA2 2011.04.08 BUG MANTIS 4336 - non si deve effettuare la richiesta di localizazione in Indice
					// per i luoghi perchè non esiste tale dato. (SbnGestioneAllAuthorityDao.cancellaAuthority);
					if (areaDatiPass.getTipoAut() != null && areaDatiPass.getTipoAut().equals("LU")) {
						delocalizzaIndice = false;
					} else {
						AreaDatiLocalizzazioniAuthorityVO leggiLocalIndice = new AreaDatiLocalizzazioniAuthorityVO();
						AreaDatiPassaggioInterrogazioneTitoloReturnVO leggiLocalIndiceReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

						leggiLocalIndice.setIdLoc(areaDatiPass.getBid());
						if (areaDatiPass.getTipoAut() == null) {
							leggiLocalIndice.setTipoMat(areaDatiPass.getTipoMat());
						} else {
							leggiLocalIndice.setAuthority(areaDatiPass.getTipoAut());
						}
						leggiLocalIndice.setCodiceSbn(this.user.getBiblioteca().substring(0,3) + "   ");
						leggiLocalIndice.setIndice(true);
						leggiLocalIndice.setPolo(false);
						SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
						leggiLocalIndiceReturn = gestioneAllAuthority.cercaLocalizzazioni(leggiLocalIndice, false);

						if (leggiLocalIndiceReturn.getCodErr().equals("0000")) {
							if (leggiLocalIndiceReturn.getTotRighe() == 0) {
								delocalizzaIndice = false;
							} else if (leggiLocalIndiceReturn.getTotRighe() == 1) {
								// Se la biblioteca trovata è unica ed è quella che ha effettuato la richiesta si può procedere con
								// la cancellazione altrimenti si deve bloccare la cancellazione ed effettuare solo la delocalizzazione
								if (!((SinteticaLocalizzazioniView)leggiLocalIndiceReturn.getListaSintetica().get(0)).getIDSbn().equals(
										this.user.getBiblioteca())) {
									altreBiblIndicePerPolo = true;
									cancellaBiblio = false;
									cancellaIndice = false;
								}
							} else {
								// esistono più biblioteche del Polo a gestire l'oggetto; non si deve effettuare la cancellazione
								altreBiblIndicePerPolo = true;
								cancellaBiblio = false;
								cancellaIndice = false;
							}
						} else if (leggiLocalIndiceReturn.getCodErr().equals("3001") || leggiLocalIndiceReturn.getCodErr().equals("3013")) {
							delocalizzaIndice = false;
						} else {
							areaDatiPass.setCodErr(leggiLocalIndiceReturn.getCodErr());
							areaDatiPass.setTestoProtocollo(leggiLocalIndiceReturn.getTestoProtocollo());
							return areaDatiPass;
						}
					}
				}
			}

			// Fine Modifica ALMAVIVA2 2010.11.15 BUG MANTIS 3982


			if (cancellaBiblio) {

				// Cancellazione in Polo per l'oggetto inserito
				SbnMessageType sbnmessage = new SbnMessageType();
				SbnRequestType sbnrequest = new SbnRequestType();
				CancellaType cancellaType = new CancellaType();
				SbnOggetto sbnOggetto = new SbnOggetto();

				if (areaDatiPass.getTipoAut() == null
						|| areaDatiPass.getTipoAut().equals(IID_STRINGAVUOTA)) {
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
				} else {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAut()));
				}

				cancellaType.setTipoOggetto(sbnOggetto);
				cancellaType.setIdCancella(areaDatiPass.getBid());

				sbnmessage.setSbnRequest(sbnrequest);
				sbnrequest.setCancella(cancellaType);

				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiPass.setCodErr("noServerPol");
					return areaDatiPass;
				}

				// Modifica ALMAVIVA2 2010.11.16 BUG MANTIS 3982 - se l'oggetto da cancellare sul polo è già assente si
				// invia la segnalazione ma si procede con le operazioni su indice (SbnGestioneAllAuthorityDao.cancellaAuthority);
				if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")
							&& (areaDatiPass.getTipoAut() != null
							&& areaDatiPass.getTipoAut().toString().equals("LU"))) {
						diagnosticaPerLuoghi = ":l'oggetto era già assente dalla Base Dati Locale;";
					} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3013")) {
						diagnosticaPerLuoghi = ":l'oggetto era già assente dalla Base Dati Locale;";
					} else if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3096")) {
						// il diagnostico indica che l'oggetto da cancellare è un documento di natura A non musicale (quindi Tit.
						// Unif.) si ripete la richiesta di cancellazione con i giusti parametri
						SbnOggetto sbnOggettoNew = new SbnOggetto();
						sbnOggettoNew.setTipoAuthority(SbnAuthority.TU);
						cancellaType.setTipoOggetto(sbnOggettoNew);
						cancellaType.setIdCancella(areaDatiPass.getBid());

						sbnmessage.setSbnRequest(sbnrequest);
						sbnrequest.setCancella(cancellaType);

						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
						if (sbnRisposta == null) {
							areaDatiPass.setCodErr("noServerPol");
							return areaDatiPass;
						}
						if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
							areaDatiPass.setCodErr("9999");
							areaDatiPass.setTestoProtocollo(sbnRisposta
									.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito()
									+ " sulla Base Dati locale");
							return areaDatiPass;
						}
					} else {
						areaDatiPass.setCodErr("9999");
						areaDatiPass.setTestoProtocollo(sbnRisposta
								.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito()
								+ " sulla Base Dati locale");
						return areaDatiPass;
					}
				}
			}

			if (areaDatiPass.isRicercaIndice()) {

				// Intervento interno 25.07.2012 ALMAVIVA2 x Rossana e 
				// spostato sotto perchè l'indice provvede a cancellare se possibile altrimenti effettua la delocalizzazione in automatico
//				if (delocalizzaIndice) {
//					// Delocalizzazione in Indice per l'oggetto
//					AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
//					areaDatiPassReturn.setCodErr("0000");
//					AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
//					areaLocalizza.setIdLoc(areaDatiPass.getBid());
//					if (areaDatiPass.getTipoAut() == null) {
//						areaLocalizza.setAuthority("");
//					} else {
//						areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
//					}
//					if (!areaLocalizza.getAuthority().equals("LU")
//							&& !areaLocalizza.getAuthority().equals("TU")) {
//						areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
//						areaLocalizza.setTipoOpe("Delocalizza");
//						areaLocalizza.setTipoLoc("Tutti");
//						areaLocalizza.setIndice(true);
//						areaLocalizza.setPolo(false);
//						areaDatiPassReturn = localizzaAuthority(areaLocalizza);
//
//						if (!areaDatiPassReturn.getCodErr().equals("0000")) {
//							areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
//							areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
//							return areaDatiPass;
//						}
//					}
//				}

				// Cancellazione in Indice per l'oggetto inserito
				if (areaDatiPass.getTipoOperazione().equals("Cancella")) {
					if (cancellaIndice) {
						SbnMessageType sbnmessage = new SbnMessageType();
						SbnRequestType sbnrequest = new SbnRequestType();
						CancellaType cancellaType = new CancellaType();
						SbnOggetto sbnOggetto = new SbnOggetto();

						if (areaDatiPass.getTipoAut() == null
								|| areaDatiPass.getTipoAut().equals(IID_STRINGAVUOTA)) {

							sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
						} else {
							sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAut()));
						}

						cancellaType.setTipoOggetto(sbnOggetto);
						cancellaType.setIdCancella(areaDatiPass.getBid());

						sbnmessage.setSbnRequest(sbnrequest);
						sbnrequest.setCancella(cancellaType);

						this.indice.setMessage(sbnmessage, this.user);
						sbnRisposta = this.indice.eseguiRichiestaServer();

						if (sbnRisposta == null) {
							areaDatiPass.setCodErr("noServerInd");
							return areaDatiPass;
						}

						if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
							// 05.02.2013 ALMAVIVA2 su richiesta a voce almaviva1
							// Se la cancellazione non è andata a buon fine provvedo ad effetture la delocalizzazione
							// inviando un Messaggio Inserito controllo secco x 3092 così da richiamare la delocalizzazione in Indice
							cancellaIndice = false;
							if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3092")) {
								altriPoliIndice = true;
								// Delocalizzazione in Indice per l'oggetto
								AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
								areaDatiPassReturn.setCodErr("0000");
								AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
								areaLocalizza.setIdLoc(areaDatiPass.getBid());
								if (areaDatiPass.getTipoAut() == null) {
									areaLocalizza.setAuthority("");
								} else {
									areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
								}
								if (!areaLocalizza.getAuthority().equals("LU")
										&& !areaLocalizza.getAuthority().equals("TU")) {
									areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
									areaLocalizza.setTipoOpe("Delocalizza");
									areaLocalizza.setTipoLoc("Tutti");
									areaLocalizza.setIndice(true);
									areaLocalizza.setPolo(false);
									areaDatiPassReturn = localizzaAuthority(areaLocalizza);

									if (!areaDatiPassReturn.getCodErr().equals("0000")) {
										areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
										areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
										return areaDatiPass;
									}
								}
							} else {
								areaDatiPass.setCodErr("9999");
								areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
												.getSbnResult().getTestoEsito());
								return areaDatiPass;
							}
						}
					} else {
						// Modifica ALMAVIVA2 2014.07.21 BUG MANTIS esercizio 5602 - se la richiesta di cancellazione
						// documento viene effettuata da un polo che ha tutti gli inventari del documento allo stato
						// di dismesso (cd-sit=3) il sw procederà alla delocalizzazione per possesso sia in indice che in polo,
						// senza cancellare ne dal polo ne dall'Indice la notizia
						// Inizio Qui non si puo cancellare (cancellaIndice=false) e si procede con la solo delocalizzazione
						if (inventDismessiPolo && !inventAttiviPolo) {
							// Delocalizzazione in Indice per l'oggetto
							AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
							areaDatiPassReturn.setCodErr("0000");
							AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
							areaLocalizza.setIdLoc(areaDatiPass.getBid());
							if (areaDatiPass.getTipoAut() == null) {
								areaLocalizza.setAuthority("");
							} else {
								areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
							}
							if (!areaLocalizza.getAuthority().equals("LU")
									&& !areaLocalizza.getAuthority().equals("TU")) {
								areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
								areaLocalizza.setTipoOpe("Delocalizza");
								areaLocalizza.setTipoLoc("Possesso");
								areaLocalizza.setIndice(true);
								areaLocalizza.setPolo(false);
								areaDatiPassReturn = localizzaAuthority(areaLocalizza);

								if (!areaDatiPassReturn.getCodErr().equals("0000")) {
									areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
									areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
									return areaDatiPass;
								}
							}
						} else {
							// Inizio Manutenzione interna Gennaio 2016 -
							// questo else serve per gestire i casi in cui siamo in presenza una richiesta di cancella notizia
							// che ha più localizzazioni per biblio del Polo per cui non si può effettuare la cancellazione
							// in Indice ma SOLO la delocalizzazione per la biblioteca operante

							if (delocalizzaIndice) {
								// Delocalizzazione in Indice per l'oggetto
								AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
								areaDatiPassReturn.setCodErr("0000");
								AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
								areaLocalizza.setIdLoc(areaDatiPass.getBid());
								if (areaDatiPass.getTipoAut() == null) {
									areaLocalizza.setAuthority("");
								} else {
									areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
						}
								if (!areaLocalizza.getAuthority().equals("LU") && !areaLocalizza.getAuthority().equals("TU")) {
									areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
									areaLocalizza.setTipoOpe("Delocalizza");

									// Modifica ALMAVIVA2 2014.07.21 BUG MANTIS esercizio 5602 - se la richiesta di delocalizzazione
									// documento viene effettuata da una biblioteca che ha tutti gli inventari del documento allo stato
									// di dismesso (cd-sit=3) il sw procederà alla delocalizzazione per possesso sia in indice che in polo,
									// senza cancellare dal polo la notizia (anche se gestita solo dalla biblioteca operante)
									// e quindi senza delocalizzazione per gestione in Indice
									if (inventDismessiBiblio) {
										areaLocalizza.setTipoLoc("Possesso");
									} else  {
										areaLocalizza.setTipoLoc("Tutti");
									}

									areaLocalizza.setIndice(true);
									areaLocalizza.setPolo(false);
									areaDatiPassReturn = localizzaAuthority(areaLocalizza);

									if (!areaDatiPassReturn.getCodErr().equals("0000")) {
										areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
										areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
										return areaDatiPass;
									}
								}
							}
							// Fine Manutenzione interna Gennaio 2016 -
						}
						// Fine Qui non si puo cancellare (cancellaIndice=false) e si procede con la solo delocalizzazione
					}
				} else {

					if (delocalizzaIndice) {
						// Delocalizzazione in Indice per l'oggetto
						AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
						areaDatiPassReturn.setCodErr("0000");
						AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
						areaLocalizza.setIdLoc(areaDatiPass.getBid());
						if (areaDatiPass.getTipoAut() == null) {
							areaLocalizza.setAuthority("");
						} else {
							areaLocalizza.setAuthority(areaDatiPass.getTipoAut());
						}
						if (!areaLocalizza.getAuthority().equals("LU")
								&& !areaLocalizza.getAuthority().equals("TU")) {
							areaLocalizza.setCodiceSbn(this.user.getBiblioteca());
							areaLocalizza.setTipoOpe("Delocalizza");

							// Modifica ALMAVIVA2 2014.07.21 BUG MANTIS esercizio 5602 - se la richiesta di delocalizzazione
							// documento viene effettuata da una biblioteca che ha tutti gli inventari del documento allo stato
							// di dismesso (cd-sit=3) il sw procederà alla delocalizzazione per possesso sia in indice che in polo,
							// senza cancellare dal polo la notizia (anche se gestita solo dalla biblioteca operante)
							// e quindi senza delocalizzazione per gestione in Indice
							if (inventDismessiBiblio) {
								areaLocalizza.setTipoLoc("Possesso");
							} else  {
								areaLocalizza.setTipoLoc("Tutti");
							}
//							areaLocalizza.setTipoLoc("Tutti");


							areaLocalizza.setIndice(true);
							areaLocalizza.setPolo(false);
							areaDatiPassReturn = localizzaAuthority(areaLocalizza);

							if (!areaDatiPassReturn.getCodErr().equals("0000")) {
								areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
								areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
								return areaDatiPass;
							}
						}
					}

					// Manutenzione 10.03.2010 ALMAVIVA2; in caso di richiesta di delocalizza(Scattura)
					// non si effettua il cancella in Indice quindi non si deve enviare il diagnostico che dice che
					// l'ha effettuata - Intervento interno
					cancellaIndice = false;
				}
			} else {
				delocalizzaIndice = false;
				cancellaIndice = false;
			}

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		String testoDiagnostico = "<BR>";
		if (ordiniPolo) {
			testoDiagnostico = testoDiagnostico + "..Sono presenti ordini per "
					+ areaDatiPass.getBid() + " per il Polo<BR>";
		}
		if (inventPolo) {
			if (inventDismessiBiblio) {
				testoDiagnostico = testoDiagnostico
				+ "..Sono presenti inventari DISMESSI per " + areaDatiPass.getBid()
				+ " per il Polo/Biblio<BR>";

			} else {
				testoDiagnostico = testoDiagnostico
				+ "..Sono presenti inventari per " + areaDatiPass.getBid()
				+ " per il Polo<BR>";

			}
		}

		if (collocPolo) {
			testoDiagnostico = testoDiagnostico
					+ "..Sono presenti collocazioni per "
					+ areaDatiPass.getBid() + " per il Polo<BR>";
		}
		if (esemplPolo) {
			testoDiagnostico = testoDiagnostico
					+ "..Sono presenti esemplari per " + areaDatiPass.getBid()
					+ " per il Polo<BR>";
		}

		testoDiagnostico = testoDiagnostico
				+ "Si riporta di seguito l'elenco delle operazioni effettuate:<BR>";

		if (delocalizzaBiblio) {
			testoDiagnostico = testoDiagnostico + "..Delocalizzazione di "
					+ areaDatiPass.getBid() + " per la biblioteca "
					+ this.user.getBiblioteca()
					+ " sulla Base Dati di Polo<BR>";
		}

		// Modifica ALMAVIVA2 2010.11.15 BUG MANTIS 3982 - variazione del diagnostico per eliminare la dicitura
		//" per la biblioteca " in quanto la cancellazione vale per tutto il Polo (SbnGestioneAllAuthorityDao.cancellaAuthority);
		if (cancellaBiblio) {
			testoDiagnostico = testoDiagnostico + "..Cancellazione di    "
					+ areaDatiPass.getBid()
//					+ " per la biblioteca "	+ this.user.getBiblioteca()
					+ " sulla Base Dati di Polo "
					+ diagnosticaPerLuoghi + "<BR>";
		} else {
			if (altreBiblIndicePerPolo) {
				testoDiagnostico = testoDiagnostico + "..In indice sono presenti localizzazioni per altre biblioteche del polo; l'oggetto "
				+ areaDatiPass.getBid()
				+ " non è stato cancellato dalla base dati di polo "
				+  "<BR>";
			}
		}

		// Modifica ALMAVIVA2 2010.11.15 BUG MANTIS 3982
		if (delocalizzaIndice) {
			testoDiagnostico = testoDiagnostico + "..Delocalizzazione di "
					+ areaDatiPass.getBid() + " per la biblioteca "
					+ this.user.getBiblioteca()
					+ " sulla Base Dati di Indice<BR>";
		}

		// Modifica ALMAVIVA2 2010.11.15 BUG MANTIS 3982 - variazione del diagnostico per eliminare la dicitura
		//" per la biblioteca " in quanto la cancellazione vale per tutto l'indice (SbnGestioneAllAuthorityDao.cancellaAuthority);
		if (cancellaIndice) {
			testoDiagnostico = testoDiagnostico + "..Cancellazione di    "
					+ areaDatiPass.getBid()
//					+ " per la biblioteca "	+ this.user.getBiblioteca()
					+ " sulla Base Dati di Indice<BR>";
		} else {
			if (altriPoliIndice) {
				testoDiagnostico = testoDiagnostico + "..In indice sono presenti localizzazioni per altri Poli; l'oggetto "
				+ areaDatiPass.getBid()
				+ " non è stato cancellato dalla base dati di Indice "
				+  "<BR>";
			}


		}




		areaDatiPass.setTestoProtocollo(testoDiagnostico);

		return areaDatiPass;
	}

	// Nuova classe ALMAVIVA2 27 gennaio 2010 - creata per gestire la
	// cancellazione Authority in Allineamento

	public AreaDatiPassaggioCancAuthorityVO cancellaAuthorityDaAllineamento(AreaDatiPassaggioCancAuthorityVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		areaDatiPass.setCodErr("0000");

		boolean ordiniPolo = false;
		boolean inventPolo = false;
		boolean collocPolo = false;
		boolean esemplPolo = false;
		//almaviva5_20111115 controllo esistenza legami a fascicoli
		boolean fascicoli = false;


		// Modifica ALMAVIVA2 BUG MANTIS 4395 esercizio, 4320 esercizio
		// Quando il report allineamenti segnala  la presenza di gestionali da spostare,
		// il testo era riportato in modo difforme: ora è stato reso uniforme;
		String diagnosticaPerAllineamento = "";

		try {

			// Inizio controlli su presenza di gestionali ancora validi
			// Area acquisizioni:
			//almaviva5_20150119 #5701
			if (ValidazioneDati.strIsEmpty(areaDatiPass.getTipoAut()) ) {

				String datiSegnalazione = "";
				StrutturaCombo StrutturaCombo;
				ListaSuppOrdiniVO listaSuppOrdiniVO = new ListaSuppOrdiniVO();
				GenericJDBCAcquisizioniDAO acquisizioni = new GenericJDBCAcquisizioniDAO();
				StrutturaCombo = new StrutturaCombo(areaDatiPass.getBid(), "");
				listaSuppOrdiniVO.setTitolo(StrutturaCombo);
				List<OrdiniVO> ordini = null;
				try {
					ordini = acquisizioni.getRicercaListaOrdini(listaSuppOrdiniVO);
				} catch (ValidationException ve) {
					if (ve.getError() == 4001) {
						ordiniPolo = false;
					} else {
						areaDatiPass.setCodErr("9999");
						areaDatiPass
								.setTestoProtocollo("La procedura di verifica dell'esistenza ordini su Polo ha riscontrato il seguente malfunzionamento: "
										+ ve.getMessage());
						return areaDatiPass;
					}
				} catch (Exception e) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass
							.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza ordini su Polo ha riscontrato il seguente malfunzionamento: "
									+ e.getMessage());
					return areaDatiPass;
				}
				if (ValidazioneDati.isFilled(ordini) ) {
					ordiniPolo = true;
					diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ "<br />Attenzione esistono Ordini legati al Documento: ";
					for (OrdiniVO ordine : ordini) {
						// ALMAVIVA2 16.02.2011 BUG MANTIS 4228 esercizio - inserito nella diagnostica della presenza ordini per cancellazione
						// anche  codice biblio
//						String appo = recResult.getAnnoOrdine() + " " + recResult.getTipoOrdine() + " " + recResult.getCodOrdine();
						String kord = ordine.getCodPolo() + " " +  ordine.getCodBibl() + " " + ordine.getChiaveOrdine() + " ";


						diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ kord + ";";

						datiSegnalazione = "l'ordine: " + kord;
						boolean esitoMail = inviaPresenzaGestionaliMail(ordine.getCodPolo(),
								ordine.getCodBibl(),
								areaDatiPass.getBid(),
								datiSegnalazione);

					}
				}

				// Area documento Fisico:
				Tbc_inventarioDao inventari = new Tbc_inventarioDao();
				List<Tbc_inventario> tbcInventario = inventari.getListaInventari(areaDatiPass.getBid());

				if (ValidazioneDati.isFilled(tbcInventario) ) {
					inventPolo = true;
					diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ "<br />Attenzione esistono Inventari legati al Documento: ";
					for (Tbc_inventario inventario : tbcInventario) {
						Tbc_serie_inventariale serie = inventario.getCd_serie();
						Tbf_biblioteca_in_polo bib = serie.getCd_polo();
						String kinv = bib.getCd_polo().getCd_polo()
								+ bib.getCd_biblioteca() + " "
								+ ValidazioneDati.trimOrEmpty(serie.getCd_serie()) + " "
								+ inventario.getCd_inven();
						diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ kinv + ";";

						datiSegnalazione = "l'inventario " + kinv;
						boolean esitoMail = inviaPresenzaGestionaliMail(bib.getCd_polo().getCd_polo(),
								bib.getCd_biblioteca(),
								areaDatiPass.getBid(),
								datiSegnalazione);
					}
				} else {
					// NON ESISTONO INVENTARI PER IL POLO CANCELLAZIONE OK
					inventPolo = false;
				}

				Tbc_collocazioneDao collocazioni = new Tbc_collocazioneDao();
				List<Tbc_collocazione> tbcCollocazione = collocazioni.getListaCollocazioni(areaDatiPass.getBid());

				if (ValidazioneDati.isFilled(tbcCollocazione) ) {
					collocPolo = true;
					diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ "<br />Attenzione esistono Collocazioni legate al Documento: ";
					for (Tbc_collocazione coll : tbcCollocazione) {
						Tbc_sezione_collocazione sez = coll.getCd_sez();
						Tbf_biblioteca_in_polo bib = sez.getCd_polo();
						String kcoll = bib.getCd_polo().getCd_polo() + bib.getCd_biblioteca()
								+ " " + ConversioneHibernateVO.toWeb().formattaCollocazione(coll);
						diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ kcoll + ";";

						datiSegnalazione = "la collocazione di inventario " + kcoll;
						boolean esitoMail = inviaPresenzaGestionaliMail(bib.getCd_polo().getCd_polo(),
								bib.getCd_biblioteca(),
								areaDatiPass.getBid(),
								datiSegnalazione);

					}
				} else {
					// NON ESISTONO COLLOCAZIONI PER IL POLO CANCELLAZIONE OK
					collocPolo = false;
				}

				Tbc_esemplare_titoloDao esemplari = new Tbc_esemplare_titoloDao();
				List tbcEsemplare = esemplari.getListaEsemplari(areaDatiPass.getBid());

				if (ValidazioneDati.isFilled(tbcEsemplare) ) {
					esemplPolo = true;
					diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ "<br />Attenzione esistono Esemplari legati al Documento per: ";
					for (int index = 0; index < tbcEsemplare.size(); index++) {
						Tbc_esemplare_titolo esempl = (Tbc_esemplare_titolo) tbcEsemplare.get(index);

						Tbf_biblioteca_in_polo bib = esempl.getCd_polo();
						String ekey = bib.getCd_polo().getCd_polo()
								+ bib.getCd_biblioteca() + " "
								+ esempl.getCd_doc() + " "
								+ esempl.getB().getBid();
						diagnosticaPerAllineamento = diagnosticaPerAllineamento	+ ekey + ";";

						datiSegnalazione = "l'esemplare di collocazione " + ekey;
						boolean esitoMail = inviaPresenzaGestionaliMail(bib.getCd_polo().getCd_polo(),
								bib.getCd_biblioteca(),
								areaDatiPass.getBid(),
								datiSegnalazione);

					}
				} else {
					// NON ESISTONO ESEMPLARI PER IL POLO CANCELLAZIONE OK
					esemplPolo = false;
				}

				//almaviva5_20111115 controllo esistenza legami a fascicoli
				try {
					String annate = getPeriodiciBean().getAnnateFascicoliTitolo(areaDatiPass.getBid());
					fascicoli = ValidazioneDati.isFilled(annate);
					if (fascicoli) {
						diagnosticaPerAllineamento += "<br />Attenzione esistono fascicoli legati al Documento per le annate: " + annate;
						datiSegnalazione = "fascicoli per annate " + annate;
							String ticket = areaDatiPass.getTicket();
							inviaPresenzaGestionaliMail(DaoManager.codPoloFromTicket(ticket),
									DaoManager.codBibFromTicket(ticket),
									areaDatiPass.getBid(),
									datiSegnalazione);

						}

				} catch (SbnBaseException e) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza fascicoli ha riscontrato il seguente malfunzionamento: " + e.getErrorCode());
					return areaDatiPass;
				} catch (Exception e) {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo("ERROR >> La procedura di verifica dell'esistenza fascicoli ha riscontrato il seguente malfunzionamento: " + e.getMessage());
					return areaDatiPass;
				}

			}

			// Fine controlli su presenza di gestionali ancora validi
			//almaviva5_20111115 controllo esistenza legami a fascicoli
			//if (ordiniPolo || inventPolo || collocPolo || esemplPolo) {
			if (ordiniPolo || inventPolo || collocPolo || esemplPolo || fascicoli) {

				// Inizio Intervento ALMAVIVA2 30.05.2011 su richiesta /almaviva1
				// nel caso di presenza gestionali non si interrompe la procedura ma si aggiorna allo stato di "non condiviso" così
				// da consentire lo spostamento dei gestionali e poi si esce;

				// prima si effettua la lettura in Polo dell'oggetto in questione;
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPassaggioInterrogazioneTitoloAnaliticaVO =
					new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(this.user.getBiblioteca());
				areaDatiPassaggioInterrogazioneTitoloAnaliticaVO.setBidRicerca(areaDatiPass.getBid());
				areaDatiPassaggioInterrogazioneTitoloAnaliticaVO.setInviaSoloSbnMarc(true);
				areaDatiPassaggioInterrogazioneTitoloAnaliticaVO.setRicercaIndice(false);
				areaDatiPassaggioInterrogazioneTitoloAnaliticaVO.setRicercaPolo(true);

				SbnGestioneTitoliDao sbnGestioneTitoliDao = new SbnGestioneTitoliDao(indice, polo, user);
				AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO returnVO =
					sbnGestioneTitoliDao.creaRichiestaAnaliticoTitoliPerBID(areaDatiPassaggioInterrogazioneTitoloAnaliticaVO);

				if (returnVO.getCodErr().equals("") || returnVO.getCodErr().equals("0000")) {
					// poi si effettua l'aggiornamento in Polo dell'oggetto in questione con il flag di condivisione = N;
					if (returnVO.getSbnMarcType().getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
									.getDocumento() != null) {

						if (returnVO.getSbnMarcType().getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput()
											.getDocumento(0).getDocumentoTypeChoice().getDatiDocumento().getCondiviso().getType() == DatiDocTypeCondivisoType.S_TYPE) {
							SbnMessageType sbnmessage = new SbnMessageType();
							SbnRequestType sbnrequest = new SbnRequestType();
							ModificaType modificaType = null;
							modificaType = new ModificaType();
							modificaType.setTipoControllo(SbnSimile.CONFERMA);

							// Modifica ALMAVIVA2 20.06.2011 BUG MANTIS COLLAUDO 4508 viene impostato a true il flag cattura per forzare l'aggiornamento
							// del flag di condivisione nel caso di cancellazione impossibile per presenza gestionali.
							modificaType.setCattura(true);
							DocumentoType documentoType = new DocumentoType();

							// Inizio Modifica ALMAVIVA2 20.06.2011 BUG MANTIS COLLAUDO 4508
							// nel caso di cancellazione di bid che ha legami si deve inviare solo la variazione dell'oggetto e non
							// la sua catena di legami che non subiscono variazione altrimenti va in errore
							// (si deve inviare il getDocumentoTypeChoice() e non il getDocumento(0))
//							DocumentoType documentoType = returnVO.getSbnMarcType().getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getDocumento(0);
							documentoType.setDocumentoTypeChoice(returnVO.getSbnMarcType().getSbnMessage().getSbnResponse()
											.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getDocumentoTypeChoice());
							// Fine Modifica ALMAVIVA2 20.06.2011 BUG MANTIS COLLAUDO 4508
							documentoType.setNLista(0);
							modificaType.setDocumento(documentoType);
							modificaType.getDocumento().setStatoRecord(StatoRecord.C);
							modificaType.getDocumento().getDocumentoTypeChoice().getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);

							sbnmessage.setSbnRequest(sbnrequest);
							sbnrequest.setModifica(modificaType);

							// Richiesta al protocollo
							this.polo.setMessage(sbnmessage, this.user);
							sbnRisposta = this.polo.eseguiRichiestaServer();

							// Inizio AGOSTO 2015: QUI si deve inserire la chiamata a Inidce per spegnere AALINEAENTO altrimenti la richiesta
							// di cancellazione rimane valida e ogni mattina i bibliotecari ritrovano le mail !!!!!

							if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {

								// Chiamata per comunicazione allineamento documenti;
								sbnmessage = new SbnMessageType();
								sbnrequest = new SbnRequestType();
								ComunicaAllineatiType comunicaAllineatiType = new ComunicaAllineatiType();
								AllineatiType allineatiType = new AllineatiType();
								SbnOggetto sbnOggetto = new SbnOggetto();
								sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
								allineatiType.setTipoOggetto(sbnOggetto);
								allineatiType.setIdAllineato(areaDatiPass.getBid());
								comunicaAllineatiType.addAllineati(allineatiType);

								sbnrequest.setComunicaAllineati(comunicaAllineatiType);
								sbnmessage.setSbnRequest(sbnrequest);

								try {
									this.indice.setMessage(sbnmessage, this.user);
									sbnRisposta = this.indice.eseguiRichiestaServer();

									if (sbnRisposta == null) {
										areaDatiPass.setCodErr("9999");
										areaDatiPass.setTestoProtocollo("Cattura terminata correttamente"
												+ "<br />"
												+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati per assenza Connessione con Sistema Centrale");
										return areaDatiPass;
									} else if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
										areaDatiPass.setCodErr("9999");
										areaDatiPass.setTestoProtocollo("Attenzione"
														+ "<br />"
														+ "Errore nella chiamata a Indice per aggiornamento FLAG oggetti allineati:"
														+ "<br />"
														+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito()
														+ " "
														+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
										return areaDatiPass;
									}
								} catch (SbnMarcException e) {
									e.printStackTrace();
									areaDatiPass.setCodErr("9999");
									areaDatiPass.setTestoProtocollo("ERROR >>"	+ e.getMessage());
								}

							}
							// Fine AGOSTO 2015: inserire la chiamata a Indce per spegnere ALLINEAENTO altrimenti la richiesta
							// di cancellazione rimane valida e ogni mattina i bibliotecari ritrovano le mail !!!!!

						}
					}
				}
				// Fine Intervento ALMAVIVA2 30.05.2011 su richiesta /almaviva1

				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo(diagnosticaPerAllineamento);
				return areaDatiPass;
			}

			diagnosticaPerAllineamento = "";

			// Cancellazione in Polo per l'oggetto inserito
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CancellaType cancellaType = new CancellaType();
			SbnOggetto sbnOggetto = new SbnOggetto();

			if (areaDatiPass.getTipoAut() == null
					|| areaDatiPass.getTipoAut().equals(IID_STRINGAVUOTA)) {
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass
						.getTipoMat()));
			} else {
				sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass
						.getTipoAut()));
			}

			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(areaDatiPass.getBid());

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCancella(cancellaType);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerPol");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
					.getEsito().equals("0000")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult()
						.getEsito().equals("3001")
						&& (areaDatiPass.getTipoAut() != null && areaDatiPass
								.getTipoAut().toString().equals("LU"))) {
					diagnosticaPerAllineamento = ":l'oggetto era già assente dalla Base Dati Locale;";
				} else if (sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getEsito().equals("3096")) {
					// il diagnostico indica che l'oggetto da cancellare è un
					// documento di natura A non musicale (quindi Tit. Unif.)
					// si ripete la richiesta di cancellazione con i giusti
					// parametri
					SbnOggetto sbnOggettoNew = new SbnOggetto();
					sbnOggettoNew.setTipoAuthority(SbnAuthority.TU);
					cancellaType.setTipoOggetto(sbnOggettoNew);
					cancellaType.setIdCancella(areaDatiPass.getBid());

					sbnmessage.setSbnRequest(sbnrequest);
					sbnrequest.setCancella(cancellaType);

					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();
					if (sbnRisposta == null) {
						areaDatiPass.setCodErr("noServerPol");
						return areaDatiPass;
					}
					if (!sbnRisposta.getSbnMessage().getSbnResponse()
							.getSbnResult().getEsito().equals("0000")) {
						areaDatiPass.setCodErr("9999");
						areaDatiPass.setTestoProtocollo(sbnRisposta
								.getSbnMessage().getSbnResponse()
								.getSbnResult().getTestoEsito()
								+ " sulla Base Dati locale");
						return areaDatiPass;
					}
				} else {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage()
							.getSbnResponse().getSbnResult().getTestoEsito()
							+ " sulla Base Dati locale");
					return areaDatiPass;
				}
			}

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPass;
	}

	// Evolutiva Gennaio 2016 : viene inserita una nuova funzionalità per cancellare uno spoglio (natura N) a MONOGRAFIA
	// che senza effettuare alcun controllo su gestionali effettua la canecllazione/delocalizzazione dello Spoglio in polo
	// per tutte le biblioteche, e, se lo spoglio non è gestito da altri Poli lo delocalizza/cancella anche da Indice;
	// se invece è gestito da altri Poli lo delocalizza/cancella solo per tutte le biblio del polo operante

	public AreaDatiPassaggioCancAuthorityVO cancellaSpoglio(
			AreaDatiPassaggioCancAuthorityVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		areaDatiPass.setCodErr("0000");


		boolean altriPoliIndice = false;
		boolean cancellaBiblio = true;
		boolean cancellaIndice = true;

		try {

			// Cancellazione in Polo per l'oggetto inserito
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CancellaType cancellaType = new CancellaType();
			SbnOggetto sbnOggetto = new SbnOggetto();

			sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(areaDatiPass.getBid());

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCancella(cancellaType);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();
			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerPol");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiPass.setCodErr("9999");
				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
						.getSbnResult().getTestoEsito()	+ " sulla Base Dati locale");
				return areaDatiPass;
			}

			// ALMAVIVA2 Febbraio 2016 - Intervento interno - se lo spoglio è solo Locale la procedura di cancellazioneSpoglio
			// termina alla fase di cancellazione dell'oggetto dal polo e si invia l'apposito messaggio
			if (!areaDatiPass.isRicercaIndice()) {
				String testoDiagnostico = "<BR>";
				testoDiagnostico = testoDiagnostico	+ "Si riporta di seguito l'elenco delle operazioni effettuate:<BR>";
				if (cancellaBiblio) {
					testoDiagnostico = testoDiagnostico + "..Cancellazione di    "	+ areaDatiPass.getBid()	+ " sulla Base Dati di Polo " + "<BR>";
				}
				areaDatiPass.setTestoProtocollo(testoDiagnostico);
				return areaDatiPass;
			}


			// Cancellazione in Indice per l'oggetto inserito
			sbnmessage = new SbnMessageType();
			sbnrequest = new SbnRequestType();
			cancellaType = new CancellaType();
			sbnOggetto = new SbnOggetto();

			sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMat()));
			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(areaDatiPass.getBid());

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCancella(cancellaType);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerInd");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				// 05.02.2013 ALMAVIVA2 su richiesta a voce almaviva1
				// Se la cancellazione non è andata a buon fine provvedo ad effetture la delocalizzazione
				// inviando un Messaggio Inserito controllo secco x 3092 così da richiamare la delocalizzazione in Indice
				cancellaIndice = false;
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3092")) {
					altriPoliIndice = true;
					// Delocalizzazione in Indice per l'oggetto
					AreaDatiVariazioneReturnVO areaDatiPassReturn = new AreaDatiVariazioneReturnVO();
					areaDatiPassReturn.setCodErr("0000");
					AreaDatiLocalizzazioniAuthorityVO areaLocalizza = new AreaDatiLocalizzazioniAuthorityVO();
					areaLocalizza.setIdLoc(areaDatiPass.getBid());
					areaLocalizza.setAuthority("");
					areaLocalizza.setCodiceSbn(this.user.getBiblioteca().substring(0, 3) + "   ");
					areaLocalizza.setTipoOpe("Delocalizza");
					areaLocalizza.setTipoLoc("Tutti");
					areaLocalizza.setIndice(true);
					areaLocalizza.setPolo(false);
					areaDatiPassReturn = localizzaAuthority(areaLocalizza);

					if (!areaDatiPassReturn.getCodErr().equals("0000")) {
						areaDatiPass.setCodErr(areaDatiPassReturn.getCodErr());
						areaDatiPass.setTestoProtocollo(areaDatiPassReturn.getTestoProtocollo());
						return areaDatiPass;
					}
				} else {
					areaDatiPass.setCodErr("9999");
					areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse()
									.getSbnResult().getTestoEsito());
					return areaDatiPass;
				}
			}

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		String testoDiagnostico = "<BR>";
		testoDiagnostico = testoDiagnostico	+ "Si riporta di seguito l'elenco delle operazioni effettuate:<BR>";

		if (cancellaBiblio) {
			testoDiagnostico = testoDiagnostico + "..Cancellazione di    "	+ areaDatiPass.getBid()	+ " sulla Base Dati di Polo " + "<BR>";
		}

		if (cancellaIndice) {
			testoDiagnostico = testoDiagnostico + "..Cancellazione di    "	+ areaDatiPass.getBid()	+ " sulla Base Dati di Indice<BR>";
		} else {
			if (altriPoliIndice) {
				testoDiagnostico = testoDiagnostico + "..In indice sono presenti localizzazioni per altri Poli; l'oggetto "
				+ areaDatiPass.getBid()	+ " non è stato cancellato dalla base dati di Indice ma solo Delocalizzato per il Polo operante" +  "<BR>";
			}
		}

		areaDatiPass.setTestoProtocollo(testoDiagnostico);
		return areaDatiPass;
	}


	public AreaTabellaOggettiDaCondividereVO ricercaAutorePerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass) {

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo("");
		// Fase di accesso all'Indice per verificare esistenza dell'Autore radice

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setNumPrimo(1);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);

			CercaElementoAutType cercaElemento = new CercaElementoAutType();
			CercaAutoreType cercaAutoreType = new CercaAutoreType();

			if (areaDatiPass.getTipoRicerca().equals("PRIMARIC") && (areaDatiPass.getDettAutGenVO().getVid() != null)) {
				areaDatiPass.setTipoRicerca("IDENTIFICATIVO");
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
				canali.setT001(areaDatiPass.getDettAutGenVO().getVid().toUpperCase());
				cercaAutoreType.setCanaliCercaDatiAut(canali);
			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					&& (areaDatiPass.getDettAutGenVO().getTipoNome().equals("A")
							|| areaDatiPass.getDettAutGenVO().getTipoNome().equals("B")
							|| areaDatiPass.getDettAutGenVO().getTipoNome().equals("C")
							|| areaDatiPass.getDettAutGenVO().getTipoNome().equals("D"))) {
				// CASITICA DEL NOME PERSONALE
				areaDatiPass.setTipoRicerca("IDENTIFICATIVOPERS");

				StringaCercaType s = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
				stringaTypeChoice.setStringaLike(areaDatiPass.getDettAutGenVO().getNome());

				boolean[] tipoNomeBoolean = new boolean[7];
				tipoNomeBoolean[0] = true;
				tipoNomeBoolean[1] = true;
				tipoNomeBoolean[2] = true;
				tipoNomeBoolean[3] = true;
				tipoNomeBoolean[4] = false;
				tipoNomeBoolean[5] = false;
				tipoNomeBoolean[6] = false;
				SbnTipoNomeAutore[] sbnTipoNome = utilityCastor.converteTipoNome(tipoNomeBoolean);
				cercaAutoreType.setTipoNome(sbnTipoNome);

				s.setStringaCercaTypeChoice(stringaTypeChoice);
				canali.setStringaCerca(s);
				cercaAutoreType.setCanaliCercaDatiAut(canali);
			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					&& (areaDatiPass.getDettAutGenVO().getTipoNome().equals("E")
							|| areaDatiPass.getDettAutGenVO().getTipoNome().equals("R")
							|| areaDatiPass.getDettAutGenVO().getTipoNome().equals("G"))) {
				// CASITICA DEL NOME COLLETTIVO
				areaDatiPass.setTipoRicerca("FINITO");

				StringaCercaType s = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();

				// Inizio modifica ALMAVIVA2 su mail almaviva di RMR 25.05.2011
				// si deve conteggiare la lunghezza della stringa; se maggiore di 80 si deve troncare
				if (areaDatiPass.getDettAutGenVO().getNome().length() > 80) {
					stringaTypeChoice.setStringaEsatta(areaDatiPass.getDettAutGenVO().getNome().substring(0,80));
				} else {
					stringaTypeChoice.setStringaEsatta(areaDatiPass.getDettAutGenVO().getNome());
				}
				// Fine modifica ALMAVIVA2 su mail almaviva di RMR 25.05.2011

				boolean[] tipoNomeBoolean = new boolean[7];
				tipoNomeBoolean[0] = false;
				tipoNomeBoolean[1] = false;
				tipoNomeBoolean[2] = false;
				tipoNomeBoolean[3] = false;
				tipoNomeBoolean[4] = true;
				tipoNomeBoolean[5] = true;
				tipoNomeBoolean[6] = true;
				SbnTipoNomeAutore[] sbnTipoNome = utilityCastor.converteTipoNome(tipoNomeBoolean);
				cercaAutoreType.setTipoNome(sbnTipoNome);

				s.setStringaCercaTypeChoice(stringaTypeChoice);
				canali.setStringaCerca(s);
				cercaAutoreType.setCanaliCercaDatiAut(canali);

			} else {
				if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVOPERS")) {
					areaDatiPass.setTipoRicerca("FINITO");
					StringTokenizer st = new StringTokenizer(areaDatiPass.getDettAutGenVO().getNome());
					String[] parole = new String[4];
					int i = 0;
					while (st.hasMoreTokens()) {
						parole[i] = st.nextToken();
						i++;
						if (i > 3)
							break;
					}
					cercaAutoreType.setParoleAut(parole);
				}
			}

			cercaAutoreType.setTipoAuthority(SbnAuthority.AU);
			cercaElemento.setCercaDatiAut(cercaAutoreType);

			cercaType.setCercaElementoAut(cercaElemento);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerInd");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					if (areaDatiPass.getTipoRicerca().equals("FINITO")) {
						areaDatiPass.setCodErr("0000");
						areaDatiPass.setNumNotizie(0);
						return areaDatiPass;
					} else {
						// NON TROVATO: si prevede un secondo giro di interrogazione per firma documento
						areaDatiPass = this.ricercaAutorePerCondividi(areaDatiPass);
						return areaDatiPass;
					}
				} else {
					areaDatiPass.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPass;
				}
			}
			// Sono state trovate una o più autori relative ai dati inseriti;
			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
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

			SbnGestioneAutoriDao sbnGestioneAutoriDao = new SbnGestioneAutoriDao(indice, polo, user);
			for (int t = 0; t < tappoScorrimento; t++) {
				++progressivo;
				sinteticaAutoriVo = sbnGestioneAutoriDao.creaElementoLista(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResponseTypeChoice().getSbnOutput(), t, progressivo);
				listaSintentica.add(sinteticaAutoriVo);
			}

			int numBlocchi = 1;
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) totRighe
						/ (double) maxRighe));
			}

			areaDatiPass.setIdLista(idLista);
			areaDatiPass.setMaxRighe(maxRighe);
			areaDatiPass.setTotRighe(totRighe);
			areaDatiPass.setNumBlocco(1);
			areaDatiPass.setNumNotizie(totRighe);
			areaDatiPass.setTotBlocchi(numBlocchi);
			areaDatiPass.setListaSintetica(listaSintentica);

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPass;

	}

	public AreaTabellaOggettiDaCondividereVO ricercaDocumentoPerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass) {

		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo("");
		// Fase di accesso all'Indice per verificare esistenza del Documento
		// radice
		// 1. Impostazioni aree per ricerca in Indice del Documento radice (per
		// ISBD/IMPRONTA oppure
		// carta identità del documento RADICE:

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();

			cercaType.setTipoOutput(SbnTipoOutput.VALUE_2);
			cercaType.setNumPrimo(1);

			// Inizio Modifica ALMAVIVA2 BUG MANTIS 4293 (Collaudo) la prospettazione dei simili per catalogazione In Indice di
			// elemento locale deve essere fatta con la tipologia esatta di ordinamento e non con quella di default
//			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			if (areaDatiPass.getTipoOrdinamSelez() == null || areaDatiPass.getTipoOrdinamSelez().equals("")) {
				cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			} else {
				cercaType.setTipoOrd(SbnTipoOrd.valueOf(areaDatiPass.getTipoOrdinamSelez()));
			}
			// Fine Modifica ALMAVIVA2 BUG MANTIS 4293 (Collaudo)

			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();

			CercaDatiTitType cercaDatiTitType = null;

			int size = areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().size();
			int size2 = areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().size();
			if (areaDatiPass.getTipoRicerca().equals("PRIMARIC")
					&& (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getBid() != null)) {
				areaDatiPass.setTipoRicerca("IDENTIFICATIVO");
				cercaDatiTitType = new CercaDatiTitType();
				cercaDatiTitTypeChoice.setT001(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getBid());
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					&& (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard() != null && size > 0)) {
				areaDatiPass.setTipoRicerca("NUMSTANDARD");
				cercaDatiTitType = new CercaDatiTitType();
				NumStdType numStdType = new NumStdType();
				TabellaNumSTDImpronteVO tabNumNSt = new TabellaNumSTDImpronteVO();
				;
				for (int i = 0; i < size; i++) {
					tabNumNSt = (TabellaNumSTDImpronteVO) areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaNumStandard().get(i);
					// if (tabNumNSt.getCampoDue().equals("I")) {
					String tipoNumSt = codici.SBNToUnimarc(CodiciType.CODICE_TIPO_NUMERO_STANDARD, tabNumNSt.getCampoDue());
		            // Ottobre 2014 ALMAVIVA2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//					numStdType.setTipoSTD(SbnTipoSTD.valueOf(tipoNumSt));
					numStdType.setTipoSTD(tipoNumSt);

					numStdType.setNumeroSTD(tabNumNSt.getCampoUno());
					break;
					// }
				}
				cercaDatiTitTypeChoice.setNumSTD(numStdType);
				cercaDatiTitType
						.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					&& areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("E")
					&& (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte() != null && size2 > 0)) {
				areaDatiPass.setTipoRicerca("IMPRONTA");
				cercaDatiTitType = new CercaDocAnticoType();
				CercaDocAnticoType cercaDocAnticoType = (CercaDocAnticoType) cercaDatiTitType;
				C012 c012 = new C012();
				TabellaNumSTDImpronteVO tabImpronte = new TabellaNumSTDImpronteVO();

				if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte() != null) {
					for (int i = 0; i < size2; i++) {
						tabImpronte = (TabellaNumSTDImpronteVO) areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().get(i);
						c012.setA_012_1(tabImpronte.getCampoDue().substring(0,10));
						c012.setA_012_2(tabImpronte.getCampoDue().substring(10,24));
						c012.setA_012_3(tabImpronte.getCampoDue().substring(24));
						break;
					}
				}
				cercaDocAnticoType.setT012(c012);
				cercaDocAnticoType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					&& areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoMat().equals("U")
					&& (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte() != null && size2 > 0)) {
				areaDatiPass.setTipoRicerca("IMPRONTA");
				cercaDatiTitType = new CercaDocMusicaType();
				CercaDocMusicaType cercaDocMusicaType = (CercaDocMusicaType) cercaDatiTitType;
				C012 c012 = new C012();
				TabellaNumSTDImpronteVO tabImpronte = new TabellaNumSTDImpronteVO();
				;
				if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte() != null) {
					for (int i = 0; i < size2; i++) {
						tabImpronte = (TabellaNumSTDImpronteVO) areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getListaImpronte().get(i);
						c012.setA_012_1(tabImpronte.getCampoDue().substring(0,10));
						c012.setA_012_2(tabImpronte.getCampoDue().substring(10,24));
						c012.setA_012_3(tabImpronte.getCampoDue().substring(24));
						break;
					}
				}
				cercaDocMusicaType.setT012(c012);
				cercaDocMusicaType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

			} else if (areaDatiPass.getTipoRicerca().equals("IDENTIFICATIVO")
					|| areaDatiPass.getTipoRicerca().equals("IMPRONTA")
					|| areaDatiPass.getTipoRicerca().equals("NUMSTANDARD")) {

				cercaDatiTitType = new CercaDatiTitType();
				StringaCercaType stringaCercaType = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				TitoloCercaType titoloCercaType = new TitoloCercaType();

				if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo() != null) {
					if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo().equals(IID_STRINGAVUOTA)) {
						areaDatiPass.setTipoRicerca("PRIMADATIDOC");
						// CRITERI DI RICERCA
						// a) titolo con ricerca esatta e non parziale
						// b) natura
						// c) tipo record
						// d) paese
						// e) lingua
						// f) prima data pubblicazione

						stringaCercaType.setStringaCercaTypeChoice(stringaTypeChoice);
						UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
						String stringaEsatta = "";
						if (areaDatiPass.getNatura().equals("W")) {
							stringaEsatta = utilityCampiTitolo.componiStringaRicTitEsatta(areaDatiPass.getDescrizionePerRicercaMadre51());
						} else {
							stringaEsatta = utilityCampiTitolo.componiStringaRicTitEsatta(areaDatiPass.getDescrizionePerRicerca());
						}
						if (stringaEsatta.length() > 80) {
							stringaTypeChoice.setStringaEsatta(stringaEsatta.substring(0, 80));
						} else {
							stringaTypeChoice.setStringaEsatta(stringaEsatta);
						}

						titoloCercaType.setStringaCerca(stringaCercaType);
						cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

						if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals(IID_STRINGAVUOTA)) {
							List temp = new ArrayList();
							temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura());
							String[] valoriEffettivi = (String[]) temp.toArray(new String[temp.size()]);
							cercaDatiTitType.setNaturaSbn(valoriEffettivi);
						}

						if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals(IID_STRINGAVUOTA)) {
							int count = 1;
							GuidaDoc[] guidaDocArr = new GuidaDoc[count];
							count = 0;
							TipoRecord tipoRecord = TipoRecord.valueOf(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec());
							GuidaDoc guidaDoc = new GuidaDoc();
							guidaDocArr[count] = guidaDoc;
							guidaDoc.setTipoRecord(tipoRecord);
							count++;
							cercaDatiTitType.setGuida(guidaDocArr);
						}

						if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese() != null
								&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese().equals(IID_STRINGAVUOTA)) {
							C102 c102 = new C102();
							c102.setA_102(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese());
							cercaDatiTitType.setT102(c102);
						}

						// LINGUA
						// si potebbrero selezionare da 1 a 3 lingue
						// Modifica ALMAVIVA2 26.11.2010 BUG MANTIS 3993
						// eliminazione del filtro lingua nella ricerca simili per condividi in indice per Titoli A
						if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")) {
							if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1() != null
									&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
								C101 c101 = new C101();
								List temp = new ArrayList();
								if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
									temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1());
								}
								if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2() != null
										&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2().equals(IID_STRINGAVUOTA)) {
									temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2());
								}
								if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3() != null
										&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3().equals(IID_STRINGAVUOTA)) {
									temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3());
								}
								if (temp.size() > 0) {
									String[] lingue = (String[]) temp.toArray(new String[temp.size()]);
									// cercaDatiTitType.setNaturaSbn(lingue);
									c101.setA_101(lingue);
									cercaDatiTitType.setT101(c101);
								}
							}
						}


						C100 c100Da = new C100();
						if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1() != null
								&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1().equals(IID_STRINGAVUOTA)) {
							c100Da.setA_100_9(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1());
							cercaDatiTitType.setT100_A(c100Da);
							cercaDatiTitType.setT100_Da(c100Da);
						}
					}
				}
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);

			} else {
				cercaDatiTitType = new CercaDatiTitType();
				StringaCercaType stringaCercaType = new StringaCercaType();
				StringaCercaTypeChoice stringaTypeChoice = new StringaCercaTypeChoice();
				TitoloCercaType titoloCercaType = new TitoloCercaType();

				if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo() != null) {
					if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getAreaTitTitolo().equals(IID_STRINGAVUOTA)) {
						areaDatiPass.setTipoRicerca("SECONDADATIDOC");
						// CRITERI DI RICERCA
						// a) titolo con ricerca esatta e non parziale
						// b) natura
						// c) tipo record
						// d) paese
						// e) lingua
						// f) tipo data = F senza alcuna data pubblicazione

						stringaCercaType.setStringaCercaTypeChoice(stringaTypeChoice);
						UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();


						// Inizio Modifica Aprile 2015: solo per le interrogazioni Massive Batch (comunque la ricerca in SECONDADATIDOC
						// al momento è utilizzata solo per questa strada) la ricerca va fatta per Stringa parziale
						// per allargare le probabilità di trovare gli oggetti ricercati;
						if (areaDatiPass.isCallBatch() == false) {
							String stringaEsatta = "";
							if (areaDatiPass.getNatura().equals("W")) {
								stringaEsatta = utilityCampiTitolo.componiStringaRicTitEsatta(areaDatiPass.getDescrizionePerRicercaMadre51());
							} else {
								stringaEsatta = utilityCampiTitolo.componiStringaRicTitEsatta(areaDatiPass.getDescrizionePerRicerca());
							}
							if (stringaEsatta.length() > 80) {
								stringaTypeChoice.setStringaEsatta(stringaEsatta.substring(0, 80));
							} else {
								stringaTypeChoice.setStringaEsatta(stringaEsatta);
							}
						} else  {
							String stringaLike = "";
							if (areaDatiPass.getNatura().equals("W")) {
								stringaLike = areaDatiPass.getDescrizionePerRicercaMadre51();
							} else {
								stringaLike = areaDatiPass.getDescrizionePerRicerca();
							}
							if (stringaLike.length() > 80) {
								stringaTypeChoice.setStringaLike(stringaLike.substring(0, 80));
							} else {
								stringaTypeChoice.setStringaLike(stringaLike);
							}
						}
						// fine Modifica Aprile 2015:


						titoloCercaType.setStringaCerca(stringaCercaType);
						cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
						cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);

						if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals(IID_STRINGAVUOTA)) {
							List temp = new ArrayList();
							temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura());
							String[] valoriEffettivi = (String[]) temp.toArray(new String[temp.size()]);
							cercaDatiTitType.setNaturaSbn(valoriEffettivi);
						}

						if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec().equals(IID_STRINGAVUOTA)) {
							int count = 1;
							GuidaDoc[] guidaDocArr = new GuidaDoc[count];
							count = 0;
							TipoRecord tipoRecord = TipoRecord.valueOf(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoRec());
							GuidaDoc guidaDoc = new GuidaDoc();
							guidaDocArr[count] = guidaDoc;
							guidaDoc.setTipoRecord(tipoRecord);
							count++;
							cercaDatiTitType.setGuida(guidaDocArr);
						}

						if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese() != null
								&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese().equals(IID_STRINGAVUOTA)) {
							C102 c102 = new C102();
							c102.setA_102(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getPaese());
							cercaDatiTitType.setT102(c102);
						}

						// Inizio Modifica Giugno 2014: solo per le interrogazioni Massive Batch (comunque la ricerca in SECONDADATIDOC
						// al momento è utilizzata solo per questa strada) non deve utilizzare i filtri di Lingua e TipoData
						// per allargare le probabilità di trovare gli oggetti ricercati;
						if (areaDatiPass.isCallBatch() == false) {
							// LINGUA
							// si potebbrero selezionare da 1 a 3 lingue
							// Modifica ALMAVIVA2 26.11.2010 BUG MANTIS 3993
							// eliminazione del filtro lingua nella ricerca simili per condividi in indice per Titoli A
							if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("A")) {
								if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1() != null
										&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
									C101 c101 = new C101();
									List temp = new ArrayList();
									if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1().equals(IID_STRINGAVUOTA)) {
										temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua1());
									}
									if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2() != null
											&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2().equals(IID_STRINGAVUOTA)) {
										temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua2());
									}
									if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3() != null
											&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3().equals(IID_STRINGAVUOTA)) {
										temp.add(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getLingua3());
									}
									if (temp.size() > 0) {
										String[] lingue = (String[]) temp.toArray(new String[temp.size()]);
										// cercaDatiTitType.setNaturaSbn(lingue);
										c101.setA_101(lingue);
										cercaDatiTitType.setT101(c101);
									}
								}
							}

							if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getNatura().equals("M")) {
								if (!areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getTipoData().equals(IID_STRINGAVUOTA)) {
									C100 c100Da = new C100();
									C100 c100A = new C100();
									String tipoDataUnimarc = codici.SBNToUnimarc(
											CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE,areaDatiPass.getDettTitComVO()
															.getDetTitoloPFissaVO().getTipoData());
									c100A.setA_100_8(tipoDataUnimarc);
									c100Da.setA_100_8(tipoDataUnimarc);
									cercaDatiTitType.setT100_A(c100A);
									cercaDatiTitType.setT100_Da(c100Da);
								}
							}

						} else {
							// Inizio Modifica Aprile 2015: solo per le interrogazioni Massive Batch (comunque la ricerca in SECONDADATIDOC
							// al momento è utilizzata solo per questa strada) deve utilizzare i filtri di DataPubbl1
							// per allargare le probabilità di trovare gli oggetti ricercati;
							C100 c100Da = new C100();
							if (areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1() != null
									&& !areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1().equals(IID_STRINGAVUOTA)) {
								c100Da.setA_100_9(areaDatiPass.getDettTitComVO().getDetTitoloPFissaVO().getDataPubbl1());
								cercaDatiTitType.setT100_A(c100Da);
								cercaDatiTitType.setT100_Da(c100Da);
							}
						}
						// Fine Modifica Aprile 2015
						// Fine Modifica Giugno 2014: solo per le interrogazioni Massive Batch
					}
				}
				cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
				cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			}

			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);

			// INIZIO Evolutiva Novembre 2015 - ALMAVIVA2 gestione interrogazione da SERVIZI ILL
			// Vengono inviate le biblioteche per cui si deve effettuare la ricerca
			if (areaDatiPass.getLivelloRicerca() != null && areaDatiPass.getLivelloRicerca().equals("LOC"))  {
				List<BibliotecaVO> filtroLocBib = areaDatiPass.getFiltroLocBib();
				if (ValidazioneDati.isFilled(filtroLocBib)) {
					for (BibliotecaVO bib : filtroLocBib) {
						CdBibType bibType = new CdBibType();
						bibType.setCdPol(bib.getCod_polo());
						bibType.setCdBib(bib.getCod_bib());
						cercaTitoloType.addLocBib(bibType);
					}
				}
			}
			// INIZIO Evolutiva Novembre 2015 - ALMAVIVA2 gestione interrogazione da SERVIZI ILL
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			// INIZIO Evolutiva Novembre 2015 - ALMAVIVA2 gestione interrogazione da SERVIZI ILL
			// Viene differenziata la ricerca fra polo e Inidce in base all'apposito campo
			if (areaDatiPass.getLivelloRicerca() != null && areaDatiPass.getLivelloRicerca().equals("LOC"))  {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
			} else {
				this.indice.setMessage(sbnmessage, this.user);
				sbnRisposta = this.indice.eseguiRichiestaServer();
			}
			// FINE Evolutiva Novembre 2015 - ALMAVIVA2 gestione interrogazione da SERVIZI ILL

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerInd");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")
					&& !sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3004")) {
				if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("3001")) {
					// Modifica ALMAVIVA2 del 05.03.2010 - BUG MANTIS 3571
					// Per l'ennesima volta si cambia la gestione e si tralascia
					// l'ultimo giro di ricerca - si termina al primo !!!!
					// if (areaDatiPass.getTipoRicerca().equals("SECONDADATIDOC"))
					// {

					// Modifica Novembre 2013: si riattiva la SECONDADATIDOC solo per le interrogazioni Massive Batch
					// Per il resto rimane come prima (cioè x attivazione Vai a --> Cataloga in Indice
//					if (areaDatiPass.getTipoRicerca().equals("PRIMADATIDOC")) {
					if (areaDatiPass.getTipoRicerca().equals("PRIMADATIDOC") && areaDatiPass.isCallBatch() == false) {
						areaDatiPass.setCodErr("0000");
						areaDatiPass.setNumNotizie(0);
						return areaDatiPass;
					} else {
						// NON TROVATO: si prevedono vari giri successivi di interrogazione per firma documento
						if (areaDatiPass.getTipoRicerca().equals("SECONDADATIDOC")) {
							areaDatiPass.setCodErr("0000");
							areaDatiPass.setNumNotizie(0);
							return areaDatiPass;
						}
						areaDatiPass = this.ricercaDocumentoPerCondividi(areaDatiPass);
						return areaDatiPass;
					}
				} else {
					areaDatiPass.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
					areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
					return areaDatiPass;
				}
			}
			// Sono state trovate una o più notizie relative ai dati inseriti;
			SinteticaTitoli st = new SinteticaTitoli();

			int maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			int totRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			String idLista = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getIdLista();

			List listaSintetica = st.getSintetica(sbnRisposta, false, 0,"NO");

			int numBlocchi = 1;
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) totRighe	/ (double) maxRighe));
			}

			areaDatiPass.setIdLista(idLista);
			areaDatiPass.setMaxRighe(maxRighe);
			areaDatiPass.setTotRighe(totRighe);
			areaDatiPass.setNumBlocco(1);
			areaDatiPass.setNumNotizie(totRighe);
			areaDatiPass.setTotBlocchi(numBlocchi);
			areaDatiPass.setListaSintetica(listaSintetica);

		} catch (SbnMarcException ve) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiPass.setCodErr("9999");
			areaDatiPass.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiPass;

	}

	// Modifica del 30.09.2010 ALMAVIVA2 Richiesta interna
	// Classe utilizzata per inviare mail alla biblioteca in caso di
	// cancellazione Documento in fase di allineamento per
	// segnalare la presenza di gestionali ai bibliotecari
	private boolean inviaPresenzaGestionaliMail(String codPolo, String codBibl,	String bid, String segnalazione) {
		try {

			InternetAddress from = util.getMailBiblioteca(codPolo, codBibl, true);	//indirizzo bib o polo se forzato
			InternetAddress to   = util.getMailBiblioteca(codPolo, codBibl, false);	//sempre indirizzo bib

			MailVO mail = new MailVO();
			mail.setFrom(from);
			mail.getTo().add(to);
			// Inizio indirizzo di prova per effettuare i test dall'Ufficio Almaviva
//			mail.getTo().add(new InternetAddress("indirizzoUtente@almavivaitalia.it"));
			// Fine indirizzo di prova per effettuare i test
			mail.setSubject("Allineamento Base Informativa - notifica su cancellazione");

			mail.setBody(MailBodyBuilder.GestioneBibliografica.segnalazionePresenzaGestionali(bid, segnalazione));

			return MailUtil.sendMail(mail);

		} catch (Exception e) {
			return false;
		}
	}



	// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	public String inserisciSoggettoCatturato(AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO, String ticket) {

		SoggettoType soggettoType = new SoggettoType();
		soggettoType = (SoggettoType) areaDatiCatturaReticoloVO.getDatiElementoType();

		DettaglioSoggettoVO dettaglioSoggettoVO = new DettaglioSoggettoVO();
		dettaglioSoggettoVO.setCid(soggettoType.getT001());
		dettaglioSoggettoVO.setTesto(SoggettiUtil.costruisciStringaSoggetto(soggettoType));
		dettaglioSoggettoVO.setCampoSoggettario(SemanticaUtil.getSoggettarioSBN(soggettoType.getT250()));
		dettaglioSoggettoVO.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(soggettoType.getT250().getC2_250()));
		dettaglioSoggettoVO.setLivAut(soggettoType.getLivelloAut().toString());

		try {
			AnaliticaSoggettoVO analiticaSoggettoVO = getSoggetti().importaSoggettoDaIndice(ticket, dettaglioSoggettoVO);
			if (analiticaSoggettoVO.isEsitoOk()) {
				return "0000" + soggettoType.getT001() + analiticaSoggettoVO.getReticolo().getDescription();
			} else {
				return analiticaSoggettoVO.getEsito();
			}

		} catch (SbnBaseException be) {
			return "ERROR >>" + be.getMessage();

		} catch (RemoteException re) {
			return "ERROR >>" + re.getMessage();
		}

	}

	// ALMAVIVA2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
	// la cattura avviene solo per le authority selzionate con il check (come per le W)
	public String inserisciClasseCatturato(AreaDatiCatturaReticoloVO areaDatiCatturaReticoloVO, String ticket) {

		ClasseType classeType = new ClasseType();
		classeType = (ClasseType) areaDatiCatturaReticoloVO.getDatiElementoType();

		CreaVariaClasseVO creaVariaClasseVO = new CreaVariaClasseVO();

		creaVariaClasseVO.setT001(classeType.getT001());

		creaVariaClasseVO.setT005("");

		creaVariaClasseVO.setT005(classeType.getT005());
		creaVariaClasseVO.setLivello(classeType.getLivelloAut().toString());
		creaVariaClasseVO.setCondiviso(classeType.getCondiviso() == DatiElementoTypeCondivisoType.S);


		String dataIns = SBNMarcUtil.converteDataSBN(classeType.getT100().getA_100_0().toString());
		creaVariaClasseVO.setDataInserimento(dataIns);
		creaVariaClasseVO.setDataVariazione(SBNMarcUtil.converteDataVariazione(classeType.getT005()));

		A676 a676 = classeType.getClasseTypeChoice().getT676();
		if (a676 != null) {
			// dewey
			creaVariaClasseVO.setCodSistemaClassificazione("D");
			creaVariaClasseVO.setSimbolo(a676.getA_676());
			creaVariaClasseVO.setCodEdizioneDewey(a676.getV_676().toString());
			creaVariaClasseVO.setDescrizione(a676.getC_676());
			creaVariaClasseVO.setCostruito(a676.getC9_676() == SbnIndicatore.S);

		} else {
			// non dewey
			A686 a686 = classeType.getClasseTypeChoice().getT686();
			creaVariaClasseVO.setCodSistemaClassificazione(a686.getC2_686());
			creaVariaClasseVO.setSimbolo(a686.getA_686());
			creaVariaClasseVO.setDescrizione(a686.getC_686());
			creaVariaClasseVO.setCodEdizioneDewey("");
		}

		creaVariaClasseVO.setLivelloPolo(true);

		try {

			creaVariaClasseVO = getClassi().importaClasseDaIndice(creaVariaClasseVO, ticket);

			if (creaVariaClasseVO.isEsitoOk()) {
				return "0000";
			} else {
				return creaVariaClasseVO.getEsito();
			}

		} catch (DAOException DAOe) {
			return "ERROR >>" + DAOe.getMessage();

		} catch (SbnBaseException be) {
			return "ERROR >>" + be.getMessage();

		} catch (RemoteException re) {
			return "ERROR >>" + re.getMessage();
		}

	}
}
