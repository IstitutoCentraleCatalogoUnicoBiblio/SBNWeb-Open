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

import static it.iccu.sbn.util.sbnmarc.SBNMarcUtil.livelloSoglia;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnClassiDAO;
import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnSoggettiDAO;
import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.SbnMarcFactory.util.UtilityDate;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.VariazioneBodyTitoli;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A210;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.Ac_210Type;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.C105bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C110;
import it.iccu.sbn.ejb.model.unimarcmodel.C115;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C121;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C124;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C126;
import it.iccu.sbn.ejb.model.unimarcmodel.C127;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C140;
import it.iccu.sbn.ejb.model.unimarcmodel.C140bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C181;
import it.iccu.sbn.ejb.model.unimarcmodel.C182;
import it.iccu.sbn.ejb.model.unimarcmodel.C183;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C205;
import it.iccu.sbn.ejb.model.unimarcmodel.C206;
import it.iccu.sbn.ejb.model.unimarcmodel.C207;
import it.iccu.sbn.ejb.model.unimarcmodel.C208;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.C231;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C423;
import it.iccu.sbn.ejb.model.unimarcmodel.C454A;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C923;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoSeriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiImportSuPoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoDatiImport1ParzialeVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.persistence.dao.bibliografica.AutoreDAO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.z3950.Z3950ClientFactory;
import it.iccu.sbn.util.Constants.Semantica.Soggetti;
import it.iccu.sbn.util.marc.MarcUtil;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.custom.esporta.QueryData;
import it.iccu.sbn.web.constant.PeriodiciConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.asSingletonList;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.length;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.rtrim;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.size;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.strIsNull;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.strIsNumeric;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trunc;

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
public class SbnGestioneImportSuPoloDao extends DaoManager {

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;
	private DocumentoFisicoBMT documentoFisico;

	private Session session;
	private Logger log;

	private int currentYear;
	private AutoreDAO autDao;
	private TitoloDAO titDao;


	public SbnGestioneImportSuPoloDao(FactorySbn indice, FactorySbn polo, SbnUserType user, BatchLogWriter blw) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;

		//almaviva5_20120614
		this.log = blw.getLogger();

		//almaviva5_20150211
		currentYear = Calendar.getInstance().get(Calendar.YEAR);

		titDao = new TitoloDAO();
		autDao = new AutoreDAO();
	}

	public static final String IID_STRINGAVUOTA = "";
	public static final String IID_SPAZIO = " ";
	public static final String IID_FILLER = "\u007C";

	public AreaDatiImportSuPoloVO trattamentoDocumento(AreaDatiImportSuPoloVO areaDatiPass) { // (etichette UNIMARC da 000 a 300)

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);

		TracciatoDatiImport1ParzialeVO tracciatoRecord;
		String idNew = "";
		String idConvertito = "";
		String tipoMateriale = "M";
		String bidDaAssegnare = "";
		String areaAppoggio1 = "";
		String areaAppoggioNota300 = "";
		String areaAppoggioNota323 = "";
		String areaAppoggioNota327 = "";
		String areaAppoggioNota330 = "";
		String areaAppoggioNota336 = "";
		String areaAppoggioNota337 = "";
		String esitoProtocollo = "0000";
		String testoLog = "";

		// identificatore nature W
		boolean naturaW=false;

		// identificatore nel caso di titoli seriali della natura P tipo seriale =a e natura C tipo seriale =b
		String tipoSeriale="";


		String stringaISBD="";

		// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
		// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
		// l'oggetto bibliografico e consentire al catalogatore di correggerlo
		boolean presenzaCaratteriSpec = false;
		// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

		SBNMarc sbnRisposta;
		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		creaType = new CreaType();
		creaType.setTipoControllo(SbnSimile.CONFERMA);

		creaTypeChoice = new CreaTypeChoice();

		DocumentoType documentoType = new DocumentoType();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

		DatiDocType datiDocType = new DatiDocType();
		AnticoType anticoType = null;
		MusicaType musicaType = null;
		ModernoType modernoType = null;
		CartograficoType cartograficoType = null;
		GraficoType graficoType = null;
		AudiovisivoType audiovisivoType = null;

		GuidaDoc guidaDoc = new GuidaDoc();

		// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
		// e IMPORT PER DISCOTECA DI STATO

		C100 c100 = null;
		C101 c101 = null;
		C102 c102 = null;
		C105 c105 = null;
		C105bis c105bis = null;
		C110 c110 = null;
		C140 c140 = null;
		C140bis c140bis = null;
		C200 c200 = null;
		C205 c205 = null;
		C206 c206 = null;
		C207 c207 = null;
		C210 c210 = null;
		C215 c215 = null;
		C801 c801 = null;
		C125 c125 = null;
		C126 c126 = null;
		C127 c127 = null;
		C128 c128 = null;
		C923 c923 = null;
		C922 c922 = null;

		C927[] c927 = null;
		List<String> listaPersonaggi = new ArrayList<String>();

		C120 c120 = null;
		C121 c121 = null;
		C123 c123 = null;
		C124 c124 = null;
		C115 c115 = null;
		C116 c116 = null;

		List<C181> c181 = new ArrayList<C181>();
		List<C182> c182 = new ArrayList<C182>();

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		C183[] c183 = null;

		C208 c208 = null;
		C856 c856 = null;


		A230 a230 = null;
		A300 a300 = null;
		C3XX[] c3xx = null;

		NumStdType[] numISBN = null;
		C012[] c012 = null;
		try {
		if (isFilled(areaDatiPass.getListaTracciatoRecordArray())) {
			tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(0);

			idNew = selectImportIdLink(areaDatiPass.getNrRichiesta(), tracciatoRecord.getIdInput());
			if (idNew.startsWith("Errore")) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
						+ " " + idNew);
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			} else if (!idNew.equals("")) {
				testoLog = setTestoLog("Id Unimarc: " + tracciatoRecord.getIdInput() + " trattato; nuovo Id:" + idNew);
				// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
				// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//				areaDatiPass.addListaSegnalazioni(testoLog);
//				areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
				return areaDatiPass;
			}
		} else {
			testoLog = setTestoLog("Lista Record Unimarc vuota");
			areaDatiPass.addListaSegnalazioni(testoLog);
			areaDatiPass.setCodErr("9999");
			return areaDatiPass;
		}


		if (isFilled(areaDatiPass.getListaTracciatoRecordArray())) {
			int size = areaDatiPass.getListaTracciatoRecordArray().size();
			for (int j = 0; j < size; j++) {

				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);
				idConvertito = String.valueOf(tracciatoRecord.getIdInput());

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				int tagNumeric = Integer.parseInt(tracciatoRecord.getTag());
				if (!isFilled(tracciatoRecord.getDati()) ) {
					areaDatiPass.setCodErr("9999");
					testoLog= setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo dati è vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (tracciatoRecord.getDati().contains("^")) {
					presenzaCaratteriSpec = true;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"


				// Inizio Modifica almaviva2 15.06.2012 inserita decodifica della natura A che in sbnWEB corrisponde alla N
				if (tracciatoRecord.getNatura().equals("S")) {
					if (tipoSeriale.equals("b")) {
						tracciatoRecord.setNatura("C");
					}
				} else if (tracciatoRecord.getNatura().equals("A")) {
					tracciatoRecord.setNatura("N");
				}

				if (tagNumeric == 200 && tracciatoRecord.getIndicatore1() == '0') {
					naturaW = true;
					testoLog = setTestoLog("ATTENZIONE rilevata natura W di documento Id Unimarc: " + idConvertito
							+ ". Trattamento rimandato alla seconda fase dell'elaborazione delle etichette 2xx");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
				}

				switch (tagNumeric) {
				case 1:
					break;
				case 5:
					// Identificativo di versione: contiene la data ultima variazione del record dalla Base Dati di partenza;
					// essendo un nuovo inserimento in Polo non sara valorizzato
					break;
				case 10:
					// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
					// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
					if (datiDocType.getNumSTDCount() < 4) {
						numISBN = ricostruisci010(tracciatoRecord.getDati());
						if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
							datiDocType.addNumSTD(numISBN[0]);
						}
					}
					break;
				case 11:
					// Inizio Modifica almaviva2 15.06.2012 se siamo in caso di Monografia si elimina la decodifica
					// dell'ISSN che è relativo alle sole Collane
					// ISSN (es:$a1972-456X$bErrato.):
					if (!tracciatoRecord.getNatura().equals("M")) {

						// Modifica del 21.01.2014 adeguato il trattamento  quello dell'etichetta 010
						numISBN = ricostruisci011(tracciatoRecord.getDati());
						if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
							datiDocType.addNumSTD(numISBN[0]);
						}
					}
					break;
				case 12:
					// Impronta (es:.........):
					c012 = ricostruisci012(tracciatoRecord.getDati());
					break;
				case 13:
					// ISMN (es:.......):
					datiDocType.addNumSTD(ricostruisci013(tracciatoRecord.getDati())[0]);
					break;
				case 17:
					// Altri numeri standard  (es:.......):
					NumStdType numStdType = ricostruisci017(tracciatoRecord.getDati());
					if (numStdType != null)
						datiDocType.addNumSTD(numStdType);
					break;
				case 20:
					// Numero di bibliografia nazionale  (es:.......):
					numISBN = ricostruisci020(tracciatoRecord.getDati());
					if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
						datiDocType.addNumSTD(numISBN[0]);
					}
					break;
				case 22:
					// Numero di pubblicazione governativa  (es:.......):
					datiDocType.addNumSTD(ricostruisci022(tracciatoRecord.getDati())[0]);
					break;
				case 71:
					// Numero di lastra (MUSICA) o numero editoriale (MUSICA)  (es:.......):
					// Modifica Ottobre 2013 con decodifica codici tag 071
					// .il numero viene inserito solo se è presente l'indicatore ( 2 e 4)
					// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
					// .il numero viene inserito anche per indicatore 0 e 1
					if (isFilled(tracciatoRecord.getDati())) {
						datiDocType.addNumSTD(ricostruisci071(tracciatoRecord.getTipoRecord(), tracciatoRecord.getDati(),
								tracciatoRecord.getIndicatore1(), tracciatoRecord.getIndicatore2())[0]);
					}
					break;
				case 100:
					// Dati generali per l'elaborazione (es:  $a19910206g19731977|||y0itaa0103    ba)
					c100 = ricostruisciC100(tracciatoRecord.getDati(), tracciatoRecord.getTipoRecord(), tracciatoRecord.getNatura());
					if (UtilityDate.isAntico(c100.getA_100_9()))
						tipoMateriale = "E";
					break;
				case 101:
					// Lingua della pubblicazione (es: xx$aita$aeng$afre)
					c101 = ricostruisciC101(tracciatoRecord.getDati());
					break;
				case 102:
					// Paese della pubblicazione o distribuzione(es:  $ait)
					c102 = new C102();
					if (tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("")
						|| tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("--")) {
							c102.setA_102("UN");
					} else {

						// Intervento del 29 luglio 2013 - controllo su tabella codice sull'esistenza paese pubblicazione
						String paesePubb = tagliaEtichetta(tracciatoRecord.getDati(), 'a').toUpperCase();
						try {
							paesePubb = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_PAESE, paesePubb);
							} catch (Exception e) {
								paesePubb = "UN";
							}
							if (!isFilled(paesePubb)) {
								paesePubb = "UN";
							}
							c102.setA_102(paesePubb);
//						c102.setA_102(tagliaEtichetta(tracciatoRecord.getDati(), 'a').toUpperCase());
					}
					break;
				case 105:
					// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					// verificare quale if fare per differenziare 105 e 105bis dato che il tag dovrebbe essere NUMERICO
					// Dati codificati: MONOGRAFIE (es:   $a    e   00|||)
					c105 = ricostruisciC105(tracciatoRecord.getDati());	// genere per Materiale MODERNO
					// Impostazione del tipoTestoLetterario per Moderno
					c105bis = ricostruisciC105bis(tracciatoRecord.getDati());
					break;
				case 110:
					// Dati codificati: PERIODICI (es:  $aaGa |||0||0)
					c110 = new C110(); // tipo Seriale
					areaAppoggio1 = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
					tipoSeriale = getTipoSerialeSbnMarc(areaAppoggio1.substring(0, 1));
					c110.setA_110_0(TipoSeriale.valueOf(tipoSeriale));
					break;

				case 115:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record g
					tipoMateriale="H";
					c115 = ricostruisciC115(tracciatoRecord.getDati());
					break;
				case 116:
					// Dati codificati: MATERIALE GRAFICO ()
					tipoMateriale = "G";
					c116 = ricostruisciC116(tracciatoRecord.getDati());
					break;

				case 120:
					// Dati codificati: MATERIALE Cartografico - Dati Generali ()
					tipoMateriale = "C";
					c120 = ricostruisciC120(tracciatoRecord.getDati());
					break;

				case 121:
					// Dati codificati: MATERIALE Cartografico - Caratteristiche fisiche ()
					tipoMateriale = "C";
					c121 = ricostruisciC121(tracciatoRecord.getDati());
					break;
				case 123:
					// Dati codificati: MATERIALE Cartografico - Scala e coordinate ()
					tipoMateriale="C";
					c123 = ricostruisciC123(tracciatoRecord.getDati(), tracciatoRecord.getIndicatore1());
					break;
				case 124:
					// Dati codificati: MATERIALE Cartografico - designazione specifica del materiale ()
					tipoMateriale="C";
					c124 = ricostruisciC124(tracciatoRecord.getDati());
					break;
				case 125:
					// Dati codificati: MATERIALE Musica a stampa - designazione specifica del materiale ()
					tipoMateriale="U";
					c125 = ricostruisciC125(tracciatoRecord.getDati());
					break;
				case 126:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
					tipoMateriale="H";
					c126 = ricostruisciC126(tracciatoRecord.getDati());
					break;
				case 127:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
					// DurataRegistraz
					c127 = ricostruisciC127(tracciatoRecord.getDati());
					break;
				case 128:
					// Dati codificati: Elaborazioni musicali (elaborazione) ()
					// almaviva2: modifica del 01.07.2015 per presenza di etichetta 128 non si attribuisce il tipo materiale U
					// se era già stata trovata etichetta 126 che lo rende Audiovisivo (quindi H)
					if (!tipoMateriale.equals("H"))  {
						tipoMateriale = "U";
					}
					//almaviva5_20150528
					c128 = ricostruisciC128(tracciatoRecord.getDati());
					break;
				case 140:
					// Dati codificati: MATERIALE ANTICO ()
					tipoMateriale = "E";
					c140 = ricostruisciC140(tracciatoRecord.getDati());
					// Impostazione del tipoTestoLetterario per Antico
					c140bis = ricostruisciC140bis(tracciatoRecord.getDati());
					break;
				case 181:
				    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					//almaviva5_20150528
					c181.add(ricostruisciC181(tracciatoRecord.getDati()));
					break;
				case 182:
				    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					//Campi Area0 Tipo Mediazione
					//almaviva5_20150528
					C182 tag182 = ricostruisciC182(tracciatoRecord.getDati());
					c182.add(tag182);
					// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
					if (isFilled(tag182.getA_182_0())) {
						c183 = new C183[1];
						c183[0] = imposta183Area0Default(tag182.getA_182_0());
					}
					break;
				case 200:
					// INFORMAZIONI DESCRITTIVE - Titolo e indicazione di responsabilita ()
					c200 = ricostruisciC200(tracciatoRecord.getDati());
					break;
				case 205:
					// INFORMAZIONI DESCRITTIVE - Area dell'edizione
					c205 = ricostruisciC205(tracciatoRecord.getDati());
					break;
				case 206:
					// AREA SPECIFICA DEL MATERIALE: CARTOGRAFICO DATI MATEMATICI
					//tipoMateriale="C";
					c206 = ricostruisciC206(tracciatoRecord.getDati());
					break;
				case 207:
					// AREA DELLA NUMERAZIONE
					c207 = ricostruisciC207(tracciatoRecord.getDati());
					break;
				case 208:
					// AREA SPECIFICA DELLA MUSICA A STAMPA
					tipoMateriale="U";
					c208 = ricostruisciC208(tracciatoRecord.getDati());
					break;
				case 210:
					// AREA DELLA PUBBLICAZIONE
					c210 = ricostruisciC210(tracciatoRecord.getDati());
					// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
					// sia data1 che data2 - se mancanti si desumono dall'area di pubblicazione
					//almaviva5_20150312 per natura N la data va lasciata a null
					if (c100.getA_100_9() == null && !ValidazioneDati.equals(tracciatoRecord.getNatura(), "N")) {
							String dataPubblicazione = estraiDataPublicazione(tracciatoRecord.getDati());
							c100.setA_100_9(dataPubblicazione);
							c100.setA_100_13(dataPubblicazione);
					}

					break;
				case 215:
					// AREA DELLA DESCRIZIONE FISICA
					c215 = ricostruisciC215(tracciatoRecord.getDati());
					break;
				case 225:
					// DESCRIZIONI DELLE SERIE COLLEZIONI
					break;
				case 230:
					// AREA SPECIFICA DELLE RISORSE ELETTRONICHE
					a230 = ricostruisciA230(tracciatoRecord.getDati());
					break;
				case 300:
				case 301:
				case 302:
				case 303:
				case 304:
				case 305:
				case 306:
				case 307:
				case 308:
				case 310:
				case 311:
				case 312:
				case 313:
				case 314:
				case 315:
				case 316:
				case 317:
				case 318:
				case 320:
				case 321:
				case 322:
				case 324:
				case 325:
				case 326:
				case 328:
				case 332:
				case 333:
				case 334:
				case 345:
					// NOTE GENERALI
					// almaviva2 15.06.2012 controllo su presenza della $a altrimenti si scarta il contenuto della nota
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota300.length() == 0) {
							areaAppoggioNota300 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota

							// Settembre 2018: Import:correzione etichietta 300; se esiste già la punteggiatura (es .) viene eliminata così
							// da essere correttamente reinserita dal protocollo
							if (areaAppoggioNota300.endsWith(".")) {
								int len = areaAppoggioNota300.length();
								areaAppoggioNota300 = areaAppoggioNota300.substring(0, len - 1);
							}
						} else {
							areaAppoggioNota300 = areaAppoggioNota300 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}

					// Modifica Novembre 2014: nel caso di etichetta 318 la presenza di $c e $j indica tempo e luogo della
					// rappresentazione e viene quindi valorizzata area c922
					if (tagNumeric == 318){
						if (tracciatoRecord.getDati().contains("$c") || tracciatoRecord.getDati().contains("$j")) {
							c922 = new C922();
							// Manutenzione Novembre 2015, almaviva2 - Si limita la lunghezza dei campi dell'etichetta 922 al
							// valore del DB
//							c922.setQ_922(tagliaEtichetta(tracciatoRecord.getDati(), 'c')); //anno data o periodo di rappresentazione
//							c922.setS_922(tagliaEtichetta(tracciatoRecord.getDati(), 'j')); //luogo di rappresentazione
							c922.setQ_922(trunc(tagliaEtichetta(tracciatoRecord.getDati(), 'c'), 15)); //anno data o periodo di rappresentazione
							c922.setS_922(trunc(tagliaEtichetta(tracciatoRecord.getDati(), 'j'), 30)); //luogo di rappresentazione

						}
					}
					break;

					// INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
				case 323:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota323.length() == 0) {
							areaAppoggioNota323 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 323
						} else {
							areaAppoggioNota323 = areaAppoggioNota323 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 327:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota327.length() == 0) {
							areaAppoggioNota327 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 327
						} else {
							areaAppoggioNota327 = areaAppoggioNota327 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 330:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota330.length() == 0) {
							areaAppoggioNota330 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 330
						} else {
							areaAppoggioNota330 = areaAppoggioNota330 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 336:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota336.length() == 0) {
							areaAppoggioNota336 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 336
						} else {
							areaAppoggioNota336 = areaAppoggioNota336 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 337:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota337.length() == 0) {
							areaAppoggioNota337 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 337
						} else {
							areaAppoggioNota337 = areaAppoggioNota337 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;

				case 856:
					// AREA SPECIFICA DELLE INIDIRIZZO RISORSA ELETTRONICHE
					// Maggio 2013 - Viene inserita la corretta gestione di impostazione del campo uriDigitalBorn(etichetta 856)
					String uriDigitalBorn = tagliaEtichetta(tracciatoRecord.getDati(), 'u');
					byte[] indirizzo = uriDigitalBorn.getBytes();
					c856 = new C856();
					c856.setU_856(uriDigitalBorn);
					c856.setC9_856_1(indirizzo);
					break;
				case 922:
					// AREA SPECIFICA DELLE RAPPRESENTAZIONI (MAT. MUSICALE)
					// Dicembre 2014 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
					c922 = new C922();
					c922 = ricostruisciC922(tracciatoRecord.getDati());
					break;
				case 927:
					// AREA SPECIFICA DEL MATERIALE MUSICALE PER LEGAME A INTERPRETI
					// Gennaio 2015 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
					listaPersonaggi.add(tracciatoRecord.getDati());
					break;
				}

				// Modifica 30.11.2012 almaviva2 -controllo su lunghezza totale ISBD che non deve superare i 1200
				// (si tiene il limite a 1190 per i caratteri speciali eventuali
				switch (tagNumeric) {
				case 200:
				case 205:
				case 206:
				case 207:
				case 208:
				case 210:
				case 215:
				case 230:
					stringaISBD = stringaISBD + tracciatoRecord.getDati();
					break;
				}
			}

			if ((stringaISBD + areaAppoggioNota300).length() > 1180) {
				areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1180-stringaISBD.length());
				stringaISBD="";
			} else {
				stringaISBD="";
			}


			// INIZIO INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
			int note =0;
			if (isFilled(areaAppoggioNota300))
				note++;
			if (isFilled(areaAppoggioNota323))
				note++;
			if (isFilled(areaAppoggioNota327))
				note++;
			if (isFilled(areaAppoggioNota330))
				note++;
			if (isFilled(areaAppoggioNota336))
				note++;
			if (isFilled(areaAppoggioNota337))
				note++;

			c3xx = new C3XX[(note)];
			int k=0;

			// Bug mantis esercizio 6171- almaviva2 aprile 2016 a seguito del'intervento di inserimento della nota 321
			// la valorizzazione dei codici nota nella classe SbnTipoNota utilizzata per inviare il tipo di Nota al protocollo
			// Polo/Indice è cambiata; la valorizzazione del campo tipo nota viene quindi effettuata con metodo valueOf indicando
			// esplicitamente la nota in oggetto e non con il suo progressivo automatico che ha subito variazioni (e potrebbe subirne altre);
			if (isFilled(areaAppoggioNota300)) {
				if (areaAppoggioNota300.length() > 1920) {
					areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota300);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("300"));
				k++;

				a300 = new A300();
				a300.setA_300(areaAppoggioNota300);
				areaAppoggioNota300 = "";
			}
			if (isFilled(areaAppoggioNota323)) {
				if (areaAppoggioNota323.length() > 1920) {
					areaAppoggioNota323 = areaAppoggioNota323.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota323);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("323"));
				k++;
			}
			if (isFilled(areaAppoggioNota327)) {
				if (areaAppoggioNota327.length() > 1920) {
					areaAppoggioNota327 = areaAppoggioNota327.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota327);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("327"));
				k++;
			}
			if (isFilled(areaAppoggioNota330)) {
				if (areaAppoggioNota330.length() > 1920) {
					areaAppoggioNota330 = areaAppoggioNota330.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota330);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("330"));
				k++;
			}
			if (isFilled(areaAppoggioNota336)) {
				if (areaAppoggioNota336.length() > 1920) {
					areaAppoggioNota336 = areaAppoggioNota336.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota336);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("336"));
				k++;
			}
			if (isFilled(areaAppoggioNota337)) {
				if (areaAppoggioNota337.length() > 1920) {
					areaAppoggioNota337 = areaAppoggioNota337.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota337);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("337"));
				k++;
			}

			// Parte vecchia sostituita dalla nuova gestione
			// Modifica 19.09.2012 almaviva2 - nuovo trattamento note richiesta  x trattamento altri tag da 301-345
//			if (isFilled(areaAppoggioNota300)) {
//				c3xx = new C3XX[1];
//				c3xx[0] = new C3XX();
//				c3xx[0].setA_3XX((String) areaAppoggioNota300);
//				c3xx[0].setTipoNota(SbnTipoNota.VALUE_0);
//
//				a300 = new A300();
//				a300.setA_300(areaAppoggioNota300);
//				areaAppoggioNota300 = "";
//			}
			// FINE INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi

			// 1. calcolo del codice tipo materiale del bid da utilizzare nell'inserimento
			AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
			// Identificativo del record:  contiene id record dalla Base Dati di partenza; essendo un nuovo inserimento in Polo
			// sara valorizzato con la routine di calcolo nuovo ID Sbn
			if (tipoMateriale.equals("")) {
				areaDatiPassGetIdSbn.setTipoMat(null);
			} else {
				// Inizio Modifica almaviva2 17.12.2013 inserita decodifica del tipo materiale che per i periodici è sempre Moderno
				// che deve essere fatta prima di invocare la richiesta di bid alla sequence altrimenti ci restituisce un bid antico
				if (tracciatoRecord.getNatura().equals("S")){
					if (tipoMateriale.equals("E")) {
						tipoMateriale = "M";
					}
				}
				// Inizio Modifica almaviva2 21.01.2014 inserita decodifica del tipo materiale anche per gli SPOGLI è sempre Moderno
				// che deve essere fatta prima di invocare la richiesta di bid alla sequence altrimenti ci restituisce un bid antico
				if (tracciatoRecord.getNatura().equals("N")){
					if (tipoMateriale.equals("E")) {
						tipoMateriale = "M";
					}
				}
				// Fine Modifica almaviva2 21.01.2014


				areaDatiPassGetIdSbn.setTipoMat(tipoMateriale);
			}

			if (tracciatoRecord.getTipoRecord().equals("")) {
				areaDatiPassGetIdSbn.setTipoRec(null);
			} else {
				areaDatiPassGetIdSbn.setTipoRec(tracciatoRecord.getTipoRecord());
			}

			areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
			if (!isFilled(areaDatiPassGetIdSbn.getIdSbn())) {
				areaDatiPass.setCodErr(areaDatiPassGetIdSbn.getCodErr());
				testoLog = setTestoLog(idConvertito + "-" + areaDatiPassGetIdSbn.getTestoProtocollo());
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			} else {
				bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
			}

			if (isFilled(tracciatoRecord.getTipoRecord())) {
				guidaDoc.setTipoRecord(TipoRecord.valueOf(tracciatoRecord.getTipoRecord()));
			}

			//============================================
			// Controlli per sanare eventuali mancanze nel tracciato Unimarc
			// nel caso di Data assente viene impostata comunque il TipoData altrimenti il protocollo va in errore
			/*
			if (c100 == null) {
				c100 = new C100();
				c100.setA_100_8("f"); // Tipo data
				// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
				// sia data1 che data2
				c100.setA_100_9("1831");
				//almaviva5_20150123
				c100.setA_100_13(Integer.toString(currentYear - 1) );
			}
			*/
			// Controlli per sanare eventuali mancanze nel tracciato Unimarc
			// nel caso di Data assente viene impostata comunque il TipoData altrimenti il protocollo va in errore
			// Modifica Febbraio 2015per trattare le collane senza data si forza una C100 con tipoData "a" e data "19.." generica
			if (c100 == null) {
				if (ValidazioneDati.equals(tracciatoRecord.getNatura(), "C") ) {
					c100 = new C100();
					c100.setA_100_8("a"); // Tipo data
					c100.setA_100_9("19..");
				} else {
					c100 = new C100();
					c100.setA_100_8("f"); // Tipo data
					// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
					// sia data1 che data2
					c100.setA_100_9("1831");
					//almaviva5_20150123
					c100.setA_100_13(Integer.toString(currentYear - 1) );
				}
			}

			if (numISBN != null) {
				if (numISBN.length > 0 && numISBN[0] == null) {
					numISBN = null;
				}
			}

			if (tracciatoRecord.getNatura().equals("S")) {
				if (c101 == null) {
					c101 = new C101();
					c101.addA_101("UND");
				}
				if (c102 == null) {
					c102 = new C102();
					c102.setA_102("UN");
				}
				if (c210 == null) {
					Ac_210Type ac_210Type = new Ac_210Type();
					ac_210Type.setA_210(new String[] { "[S.l. : s.n.]" });
					c210 = new C210();
					c210.addAc_210(ac_210Type);
				}
				if (c215 == null) {
					c215 = new C215();
					c215.addA_215("sconosciuta");
				}
			} else if (tracciatoRecord.getNatura().equals("C")) {
				tipoMateriale = "";
				if (c102 == null) {
					c102 = new C102();
					c102.setA_102("UN");
				}
				if (c210 == null) {
					Ac_210Type ac_210Type = new Ac_210Type();
					ac_210Type.setA_210(new String[] { "[S.l. : s.n.]" });
					c210 = new C210();
					c210.addAc_210(ac_210Type);
				}
			} else if (tracciatoRecord.getNatura().equals("N")) {
				if (c101 == null) {
					c101 = new C101();
					c101.addA_101("UND");
				}
			} else if (tracciatoRecord.getNatura().equals("M")) {
				if (c101 == null) {
					c101 = new C101();
					c101.addA_101("UND");
				}
				if (c102 == null) {
					c102 = new C102();
					c102.setA_102("UN");
				}
			} else if (tracciatoRecord.getNatura().equals("W")) {
				if (c101 == null) {
					c101 = new C101();
					c101.addA_101("UND");
				}
				if (c102 == null) {
					c102 = new C102();
					c102.setA_102("UN");
				}
			}

			if (c102 == null) {
				c102 = new C102();
				c102.setA_102("UN");
			}

			if (naturaW && c215 == null) {
				c215 = new C215();
				c215.addA_215("sconosciuta");
			}

			if (tracciatoRecord.getNatura().equals("S") || tracciatoRecord.getNatura().equals("C")) {
				if (c110 == null) {
					c110 = new C110(); // tipo Seriale
					c110.setA_110_0(TipoSeriale.valueOf("a"));
					tracciatoRecord.setNatura("S");
					tipoSeriale = "a";
				}
			}

			if (tracciatoRecord.getNatura().equals("S")){
				// Inizio Modifica almaviva2 15.06.2012 inserita decodifica del tipo materiale che per i periodici è sempre Moderno
				if (tipoMateriale.equals("E")) {
					tipoMateriale = "M";
				}
				if (c210 == null) {
					Ac_210Type ac_210Type = new Ac_210Type();
					ac_210Type.setA_210(new String[] { "[S.l. : s.n.]" });
					c210 = new C210();
					c210.addAc_210(ac_210Type);
				}
				if (c215 == null) {
					c215 = new C215();
					c215.addA_215("sconosciuta");
				}
			}

			// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
			if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
				if (!isFilled(c181) && !isFilled(c182) ) {
					c181.add(imposta181Area0Default(tracciatoRecord.getTipoRecord()));
					C182 tag182 = imposta182Area0Default(tracciatoRecord.getTipoRecord());
					c182.add(tag182);

					// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
					if (isFilled(tag182.getA_182_0())) {
						c183 = new C183[1];
						c183[0] = imposta183Area0Default(tag182.getA_182_0());
					}
				}
			}
			// Fine almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)



			// Gestione documenti di type diverso
			if (tipoMateriale.equals("M")) {
				modernoType = new ModernoType();
				if (naturaW) {
					modernoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					modernoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}
				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					modernoType.setT105bis(c105bis);
					modernoType.setT140bis(c140bis);
					modernoType.setT181(c181.toArray(new C181[0]));
					modernoType.setT182(c182.toArray(new C182[0]));

					//modernoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					modernoType.setT183(c183);
				}

				modernoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				modernoType.setT001(bidDaAssegnare);
				modernoType.setT005(null);
				modernoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				modernoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					modernoType.setNumSTD(datiDocType.getNumSTD());
				if (isFilled(datiDocType.getNumSTD()))
					modernoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					modernoType.setT856(new C856[] { c856 });
				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						modernoType.setT102(c102);
					}
				}
				if (c100 != null)
					modernoType.setT100(c100);
				if (c101 != null)
					modernoType.setT101(c101);
				if (c105 != null)
					modernoType.setT105(c105);
				if (c200 != null)
					modernoType.setT200(c200);
				if (c205 != null)
					modernoType.setT205(c205);
				if (c207 != null)
					modernoType.setT207(c207);
				if (c210 != null)
					modernoType.setT210(new C210[] { c210 });
				if (c215 != null)
					modernoType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						modernoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					modernoType.setT801(c801);
				datiDocType = modernoType;
			} else if (tipoMateriale.equals("E")) {
				anticoType = new AnticoType();
				if (naturaW) {
					anticoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					anticoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}
				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					anticoType.setT105bis(c105bis);
					anticoType.setT140bis(c140bis);
					anticoType.setT181(c181.toArray(new C181[0]));
					anticoType.setT182(c182.toArray(new C182[0]));

					//anticoType.setT183(c183);
				}
				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					anticoType.setT183(c183);
				}

				anticoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				anticoType.setT001(bidDaAssegnare);
				anticoType.setT005(null);
				anticoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				anticoType.setGuida(guidaDoc);
				if (c856 != null)
					anticoType.setT856(new C856[] { c856 });
				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						anticoType.setT102(c102);
					}
				}
				if (c100 != null)
					anticoType.setT100(c100);
				if (c101 != null)
					anticoType.setT101(c101);
				if (c140 != null) {
					if (c140.getA_140_9Count() > 0) {
						anticoType.setT140(c140);
					}
				}
				if (c200 != null)
					if (!c200.getA_200(0).contains("*")) {
						c200.setA_200(0, "*" + c200.getA_200(0));
					}
					anticoType.setT200(c200);
				if (c205 != null)
					anticoType.setT205(c205);
				if (c207 != null)
					anticoType.setT207(c207);
				if (c210 != null)
					anticoType.setT210(new C210[] { c210 });
				if (c215 != null)
					anticoType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						anticoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					anticoType.setT801(c801);
				if (c012 != null)
					anticoType.setT012(c012);
				datiDocType = anticoType;
			} else if (tipoMateriale.equals("U")) {
				musicaType = new MusicaType();
				if (naturaW) {
					musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}
				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					musicaType.setT105bis(c105bis);
					musicaType.setT140bis(c140bis);
					musicaType.setT181(c181.toArray(new C181[0]));
					musicaType.setT182(c182.toArray(new C182[0]));

					//musicaType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					musicaType.setT183(c183);
				}

				musicaType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				musicaType.setT001(bidDaAssegnare);
				musicaType.setT005(null);
				musicaType.setLivelloAut(SbnLivello.valueOf("05"));
				musicaType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				musicaType.setGuida(guidaDoc);
				// I numeri standard e l'impronta li aggiungo solo se il tipo record è diverso da d
				if (!tracciatoRecord.getTipoRecord().equals("d")) {
//					if (numISBN != null) {
//						musicaType.setNumSTD(numISBN);
//					}
					if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
						musicaType.setNumSTD(datiDocType.getNumSTD());

					if (c012 != null)
						musicaType.setT012(c012);
				}
				if (c856 != null)
					musicaType.setT856(new C856[] { c856 });
				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						musicaType.setT102(c102);
					}
				}
				if (c100 != null)
					musicaType.setT100(c100);
				if (c101 != null)
					musicaType.setT101(c101);
				if (c200 != null)
					musicaType.setT200(c200);
				if (c205 != null)
					musicaType.setT205(c205);
				if (c207 != null)
					musicaType.setT207(c207);
				if (c210 != null)
					musicaType.setT210(new C210[] { c210 });
				if (c215 != null)
					musicaType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						musicaType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					musicaType.setT801(c801);
				if (c125 != null)
					musicaType.setT125(c125);
				if (c128 != null)
					musicaType.setT128(c128);
				if (c923 != null)
					musicaType.setT923(c923);
				if (c922 != null)
					musicaType.setT922(c922);

//				Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
				if (isFilled(listaPersonaggi)) {
					c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
					musicaType.setT927(c927);
				}

				datiDocType = musicaType;
			} else if (tipoMateriale.equals("C")) {
				cartograficoType = new CartograficoType();
				if (naturaW) {
					cartograficoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					cartograficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}
				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					cartograficoType.setT105bis(c105bis);
					cartograficoType.setT140bis(c140bis);
					cartograficoType.setT181(c181.toArray(new C181[0]));
					cartograficoType.setT182(c182.toArray(new C182[0]));

					// cartograficoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					cartograficoType.setT183(c183);
				}

				cartograficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				cartograficoType.setT001(bidDaAssegnare);
				cartograficoType.setT005(null);
				cartograficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				cartograficoType.setLivelloAut(SbnLivello.valueOf("05"));
				cartograficoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					cartograficoType.setNumSTD(numISBN);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					cartograficoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					cartograficoType.setT856(new C856[] { c856 });
				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						cartograficoType.setT102(c102);
					}
				}
				if (c100 != null)
					cartograficoType.setT100(c100);
				if (c101 != null)
					cartograficoType.setT101(c101);
				if (c200 != null)
					cartograficoType.setT200(c200);
				if (c205 != null)
					cartograficoType.setT205(c205);
				if (c207 != null)
					cartograficoType.setT207(c207);
				if (c210 != null)
					cartograficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					cartograficoType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						cartograficoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					cartograficoType.setT801(c801);
				if (c120 != null)
					cartograficoType.setT120(c120);
				if (c121 != null)
					cartograficoType.setT121(c121);
				if (c123 != null)
					cartograficoType.setT123(c123);
				if (c124 != null)
					cartograficoType.setT124(c124);
				datiDocType = cartograficoType;
			} else if (tipoMateriale.equals("G")) {
				graficoType = new GraficoType();
				if (naturaW) {
					graficoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					graficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}
				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					graficoType.setT105bis(c105bis);
					graficoType.setT140bis(c140bis);
					graficoType.setT181(c181.toArray(new C181[0]));
					graficoType.setT182(c182.toArray(new C182[0]));

					//graficoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					graficoType.setT183(c183);
				}

				graficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				graficoType.setT001(bidDaAssegnare);
				graficoType.setT005(null);
				graficoType.setLivelloAut(SbnLivello.valueOf("05"));
				graficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				graficoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					graficoType.setNumSTD(numISBN);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					graficoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					graficoType.setT856(new C856[] { c856 });
				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						graficoType.setT102(c102);
					}
				}
				if (c100 != null)
					graficoType.setT100(c100);
				if (c101 != null)
					graficoType.setT101(c101);
				if (c200 != null)
					graficoType.setT200(c200);
				if (c205 != null)
					graficoType.setT205(c205);
				if (c207 != null)
					graficoType.setT207(c207);
				if (c210 != null)
					graficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					graficoType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						graficoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					graficoType.setT801(c801);
				if (c116 != null)
					graficoType.setT116(c116);
				datiDocType = graficoType;

			} else if (tipoMateriale.equals("H")) {
				audiovisivoType = new AudiovisivoType();
				if (naturaW) {
					audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					audiovisivoType.setT105bis(c105bis);
					audiovisivoType.setT140bis(c140bis);
					audiovisivoType.setT181(c181.toArray(new C181[0]));
					audiovisivoType.setT182(c182.toArray(new C182[0]));

					// audiovisivoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					audiovisivoType.setT183(c183);
				}

				// tipicita dell'audiovisivo
				audiovisivoType.setT115(c115);
				audiovisivoType.setT126(c126);
				audiovisivoType.setT127(c127);
				// FINE almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				audiovisivoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				audiovisivoType.setT001(bidDaAssegnare);
				audiovisivoType.setT005(null);
				audiovisivoType.setLivelloAut(SbnLivello.valueOf("05"));
				audiovisivoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				audiovisivoType.setGuida(guidaDoc);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					audiovisivoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					audiovisivoType.setT856(new C856[] { c856 });

				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						audiovisivoType.setT102(c102);
					}
				}
				if (c100 != null)
					audiovisivoType.setT100(c100);
				if (c101 != null)
					audiovisivoType.setT101(c101);
				if (c200 != null)
					audiovisivoType.setT200(c200);
				if (c205 != null)
					audiovisivoType.setT205(c205);
				if (c207 != null)
					audiovisivoType.setT207(c207);
				if (c210 != null)
					audiovisivoType.setT210(new C210[] { c210 });
				if (c215 != null)
					audiovisivoType.setT215(c215);
				if (c3xx != null) {
					for (int i = 0; i < c3xx.length; i++) {
						audiovisivoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					audiovisivoType.setT801(c801);

				// Modifica del 09/02/2014 almaviva2 campo c125 T125
				// ADEGUAMENTO ALLO SCHEMA 2.0 DOVE SCOMPARE QUESTO CAMPO CHE VIENE RICOMPRESO NEL 105/140
//				if (c125 != null)
//					audiovisivoType.setT125(c125);
				if (c128 != null)
					audiovisivoType.setT128(c128);

				if (c922 != null)
					audiovisivoType.setT922(c922);

//				Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
				if (isFilled(listaPersonaggi)) {
					c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
					audiovisivoType.setT927(c927);
				}

				datiDocType = audiovisivoType;
			} else {
				// E' una natura C, quindi senza tipo materiale
				datiDocType = new DatiDocType();
				if (naturaW) {
					datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)

				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					datiDocType.setT105bis(c105bis);
					datiDocType.setT140bis(c140bis);
					datiDocType.setT181(c181.toArray(new C181[0]));
					datiDocType.setT182(c182.toArray(new C182[0]));

					// datiDocType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					datiDocType.setT183(c183);
				}

				datiDocType.setT001(bidDaAssegnare);
				datiDocType.setT005(null);
				datiDocType.setLivelloAutDoc(SbnLivello.valueOf("05"));

				// le collane non hanno tipo record
				if (!tracciatoRecord.getNatura().equals("C")) {
					datiDocType.setGuida(guidaDoc);
				}
				if (numISBN != null)
					datiDocType.setNumSTD(numISBN);
				if (c856 != null)
					datiDocType.setT856(new C856[] { c856 });

				// almaviva2 18.06.2012 - I titoli con natura N (spogli) non devono avere il paese impostato
				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						datiDocType.setT102(c102);
					}
				}
				if (c100 != null)
					datiDocType.setT100(c100);

				// le collane non hanno lingua
				if (!tracciatoRecord.getNatura().equals("C")) {
					if (c101 != null) {
						datiDocType.setT101(c101);
					}
				}

				if (c200 != null)
					datiDocType.setT200(c200);
				if (c205 != null)
					datiDocType.setT205(c205);

				// le collane non hanno titolo di numerazione e descrizione fisica
				if (!tracciatoRecord.getNatura().equals("C")) {
					if (c207 != null) {
						datiDocType.setT207(c207);
					}
					if (c215 != null) {
						datiDocType.setT215(c215);
					}
				}

				if (c210 != null)
					datiDocType.setT210(new C210[] { c210 });
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						datiDocType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null) {
					datiDocType.setT801(c801);
				}
			}
			// Schema da 1.07 a 1.09
			if (c206 != null) {
				datiDocType.setT206(new C206[] { c206 });
			}
			if (c208 != null) {
				datiDocType.setT208(c208);
			}

			documentoTypeChoice.setDatiDocumento(datiDocType);
			documentoTypeChoice.getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);

			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			SbnRequestType sbnrequest = new SbnRequestType();
			SbnMessageType sbnmessage = new SbnMessageType();

			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(creaType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (naturaW) {
				testoLog = setTestoLog("ATTENZIONE rilevata natura W di documento Id Unimarc: " + idConvertito
						+ ". Trattamento rimandato alla seconda fase dell'elaborazione delle etichette 2xx");
					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setNatureW(naturaW);
					areaDatiPass.setCreaTypeW(creaType);
					return areaDatiPass;
			}



			// 2. invio dei dati al protocollo
			// 2.a inserimento in locale;
			try {
				this.polo.setMessage(sbnmessage, this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();

			} catch (SbnMarcException ve) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE validazione - inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
					+ " " + "(" + ve.getMessage() +")");
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;

			} catch (Exception e) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
					+ " " + "(" + e.getMessage() +")");
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			}

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
					+ " " + " RISPOSTA DA PROTOCOLLO = null)");
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			}

			esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
			if (!esitoProtocollo.equals("0000") && !esitoProtocollo.equals("3004")) {
				if (esitoProtocollo.equals("3099")) {
					C200 c200New = new C200();
					String c200A = "";
					c200New = sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().getDocumentoTypeChoice()
							.getDatiDocumento().getT200();
					c200A = c200New.getA_200(0);
					c200New.setA_200(0,"*TITOLO FORZATO. ((identifivativo originario " + idConvertito + " / ");
					// da inserire
					sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getDocumento().getDocumentoTypeChoice()
							.getDatiDocumento().setT200(c200New);

					try {
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE validazione - inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
							+ " " + "(" + ve.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
							+ " " + "(" + e.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					}
					if (sbnRisposta == null) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
							+ " " + " RISPOSTA DA PROTOCOLLO = null)");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					}
					testoLog = setTestoLog("ATTENZIONE: Inserito ID: " + bidDaAssegnare + " corrispondente a UNIMARC: " + idConvertito
							+ " con isbd = *TITOLO FORZATO. Isbd originale =" + c200A);
					areaDatiPass.addListaSegnalazioni(testoLog);

				} else {
					areaDatiPass.setCodErr("9999");
					String msg = "ERRORE inserimento Documento Id Unimarc: " + idConvertito + " nuovo Id Sbn: " + bidDaAssegnare
							+ " " + esitoProtocollo + ":" +  sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
					testoLog = setTestoLog(msg);
					log.error(msg);
					areaDatiPass.addListaSegnalazioni(testoLog);
					return areaDatiPass;
				}
			}

			areaDatiPass.setCodErr("0000");
			areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
			insertImportIdLink(idConvertito, bidDaAssegnare, areaDatiPass.getNrRichiesta());

			// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
			// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//			testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " + idConvertito  + "-" + bidDaAssegnare + ";");
//			areaDatiPass.addListaSegnalazioni(testoLog);

			// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
			// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
			// l'oggetto bibliografico e consentire al catalogatore di correggerlo
			if (presenzaCaratteriSpec) {
				testoLog = setTestoLog("ATTENZIONE l'oggetto appena inserito con identificativo " + bidDaAssegnare
												+ " contiene il carattere ^; Ripristinare il carattere corretto");
				areaDatiPass.addListaSegnalazioni(testoLog);
				presenzaCaratteriSpec = false;
			}
			// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

		}
		} catch (Exception e) {
			this.log.error(String.format("trattamentoDocumento(): id_input = %s: ", idConvertito), e);
		}

		return areaDatiPass;
	}

	/**
	 * Restituisce un sotto-insieme dei tipi seriale così come gestito da SbnMarc
	 * @param $a
	 * @return
	 */
	private String getTipoSerialeSbnMarc(String $a) {
		if (in($a, "a", "b"))
			return $a;
		return "a";
	}

	public AreaDatiImportSuPoloVO trattamentoDocumentoInferiore(AreaDatiImportSuPoloVO areaDatiPass) throws Exception { // (etichette UNIMARC da 000 a 300)

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);

		TracciatoDatiImport1ParzialeVO tracciatoRecord;
		String idNew = "";
		String idConvertito = "";
		String tipoMateriale = "M";
		String bidDaAssegnare = "";
		String areaAppoggio1 = "";
		String areaAppoggioNota300 = "";
		String areaAppoggioNota323 = "";
		String areaAppoggioNota327 = "";
		String areaAppoggioNota330 = "";
		String areaAppoggioNota336 = "";
		String areaAppoggioNota337 = "";
		String testoLog = "";

		// identificatore nature W
		boolean naturaW = true;

		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		creaType = new CreaType();
		creaType.setTipoControllo(SbnSimile.CONFERMA);

		creaTypeChoice = new CreaTypeChoice();

		DocumentoType documentoType = new DocumentoType();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

		DatiDocType datiDocType = new DatiDocType();
		AnticoType anticoType = null;
		MusicaType musicaType = null;
		ModernoType modernoType = null;
		CartograficoType cartograficoType = null;
		GraficoType graficoType = null;
		AudiovisivoType audiovisivoType = null;

		GuidaDoc guidaDoc = new GuidaDoc();

		C100 c100 = null;
		C101 c101 = null;
		C102 c102 = null;
		C105 c105 = null;
		C105bis c105bis = null;
		C110 c110 = null;
		C140 c140 = null;
		C140bis c140bis = null;
		C200 c200 = null;
		C205 c205 = null;
		C206 c206 = null;
		C207 c207 = null;
		C210 c210 = null;
		C215 c215 = null;
		C801 c801 = null;
		C125 c125 = null;
		C126 c126 = null;
		C127 c127 = null;
		C128 c128 = null;
		C923 c923 = null;
		C922 c922 = null;

		C927[] c927 = null;
		List<String> listaPersonaggi = new ArrayList<String>();
		C120 c120 = null;
		C121 c121 = null;
		C123 c123 = null;
		C124 c124 = null;
		C115 c115 = null;
		C116 c116 = null;

		List<C181> c181 = new ArrayList<C181>();
		List<C182> c182 = new ArrayList<C182>();

		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
		C183[] c183 = null;

		C208 c208 = null;
		C856 c856 = null;

		A230 a230 = null;
		A300 a300 = null;
		C3XX[] c3xx = null;

		NumStdType[] numISBN = null;
		C012[] c012 = null;

		String stringaISBD="";

		if (isFilled(areaDatiPass.getListaTracciatoRecordArray())) {
			tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(0);

			idNew = selectImportIdLink(areaDatiPass.getNrRichiesta(), tracciatoRecord.getIdInput());
			if (idNew.startsWith("Errore")) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
						+ " " + idNew);
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			} else if (!idNew.equals("")) {
				// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
				// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//				testoLog = setTestoLog("Id Unimarc: " + tracciatoRecord.getIdInput() + " trattato; nuovo Id:" + idNew);
//				areaDatiPass.addListaSegnalazioni(testoLog);
				areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
				return areaDatiPass;
			}
		} else {
			testoLog = setTestoLog("Lista Record Unimarc vuota");
			areaDatiPass.addListaSegnalazioni(testoLog);
			areaDatiPass.setCodErr("9999");
			return areaDatiPass;
		}

		if (areaDatiPass.getListaTracciatoRecordArray() != null) {
			int size = areaDatiPass.getListaTracciatoRecordArray().size();
			for (int j = 0; j < size; j++) {

				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);
				idConvertito = String.valueOf(tracciatoRecord.getIdInput());

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				int tagNumeric = Integer.parseInt(tracciatoRecord.getTag());
				if (!isFilled(tracciatoRecord.getDati()) ) {
					areaDatiPass.setCodErr("9999");
					testoLog= setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo dati è vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				tracciatoRecord.setNatura("W");

				switch (tagNumeric) {
				case 1:
					break;
				case 5:
					// Identificativo di versione: contiene la data ultima variazione del record dalla Base Dati di partenza;
					// essendo un nuovo inserimento in Polo non sara valorizzato
					break;


					//====================
				case 10:
					// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
					// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
					if (datiDocType.getNumSTDCount()< 4) {
						numISBN = ricostruisci010(tracciatoRecord.getDati());
						if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
							datiDocType.addNumSTD(numISBN[0]);
						}
					}
					break;
				case 11:
					// Inizio Modifica almaviva2 15.06.2012 se siamo in caso di Monografia si elimina la decodifica
					// dell'ISSN che è relativo alle sole Collane
					// ISSN (es:$a1972-456X$bErrato.):
					if (!tracciatoRecord.getNatura().equals("M")) {
						datiDocType.addNumSTD(ricostruisci011(tracciatoRecord.getDati())[0]);
					}
					break;
				case 12:
					// Impronta (es:.........):
					c012 = ricostruisci012(tracciatoRecord.getDati());
					break;
				case 13:
					// ISMN (es:.......):
					datiDocType.addNumSTD(ricostruisci013(tracciatoRecord.getDati())[0]);
					break;
				case 17:
					// Altri numeri standard  (es:.......):
					NumStdType numStdType = ricostruisci017(tracciatoRecord.getDati());
					if (numStdType != null)
						datiDocType.addNumSTD(numStdType);
					break;
				case 20:
					// Numero di bibliografia nazionale  (es:.......):
					numISBN = ricostruisci020(tracciatoRecord.getDati());
					if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
						datiDocType.addNumSTD(numISBN[0]);
					}
					break;
				case 22:
					// Numero di pubblicazione governativa  (es:.......):
					datiDocType.addNumSTD(ricostruisci022(tracciatoRecord.getDati())[0]);
					break;
				case 71:
					// Numero di lastra (MUSICA) o numero editoriale (MUSICA)  (es:.......):
					// Modifica Ottobre 2013 con decodifica codici tag 071
					// .il numero viene inserito solo se è presente l'indicatore ( 2 e 4)
					// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
					// .il numero viene inserito anche per indicatore 0 e 1
					if (isFilled(tracciatoRecord.getDati())) {
						datiDocType.addNumSTD(ricostruisci071(tracciatoRecord.getTipoRecord(), tracciatoRecord.getDati(),
								tracciatoRecord.getIndicatore1(), tracciatoRecord.getIndicatore2())[0]);
					}
					break;
				case 100:
					// Dati generali per l’elaborazione (es:  $a19910206g19731977|||y0itaa0103    ba)
					c100 = ricostruisciC100(tracciatoRecord.getDati(), tracciatoRecord.getTipoRecord(), tracciatoRecord.getNatura());
					if (UtilityDate.isAntico(c100.getA_100_9()))
						tipoMateriale = "E";
					break;
				case 101:
					// Lingua della pubblicazione (es: xx$aita$aeng$afre)
					c101 = ricostruisciC101(tracciatoRecord.getDati());
					break;
				case 102:
					// Paese della pubblicazione o distribuzione(es:  $ait)
					c102 = new C102();
					if (tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("")
						|| tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("--")) {
							c102.setA_102("UN");
					} else {

						// Intervento del 29 luglio 2013 - controllo su tabella codice sull'esistenza paese pubblicazione
						String paesePubb = tagliaEtichetta(tracciatoRecord.getDati(), 'a').toUpperCase();
						try {
							paesePubb = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_PAESE, paesePubb);
							} catch (Exception e) {
								paesePubb="UN";
								continue;
							}
							if (strIsNull(paesePubb)) {
								paesePubb="UN";
								continue;
							}
							c102.setA_102(paesePubb);
//						c102.setA_102(tagliaEtichetta(tracciatoRecord.getDati(), 'a').toUpperCase());
					}
					break;
				case 105:
					// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					// verificare quale if fare per differenziare 105 e 105bis dato che il tag dovrebbe essere NUMERICO
					// Dati codificati: MONOGRAFIE (es:   $a    e   00|||)
					c105 = ricostruisciC105(tracciatoRecord.getDati());
					// Impostazione del tipoTestoLetterario per Moderno
					c105bis = ricostruisciC105bis(tracciatoRecord.getDati());
					break;
				case 110:
					// Dati codificati: PERIODICI (es:  $aaGa |||0||0)
					c110 = new C110(); // tipo Seriale
					areaAppoggio1 = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
					c110.setA_110_0(TipoSeriale.valueOf(areaAppoggio1.substring(0,1)));
					break;

				case 115:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record g
					tipoMateriale = "H";
					c115 = ricostruisciC115(tracciatoRecord.getDati());
					break;
				case 116:
					// Dati codificati: MATERIALE GRAFICO ()
					tipoMateriale = "G";
					c116 = ricostruisciC116(tracciatoRecord.getDati());
					break;

				case 120:
					// Dati codificati: MATERIALE Cartografico - Dati Generali ()
					tipoMateriale = "C";
					c120 = ricostruisciC120(tracciatoRecord.getDati());
					break;

				case 121:
					// Dati codificati: MATERIALE Cartografico - Caratteristiche fisiche ()
					tipoMateriale = "C";
					c121 = ricostruisciC121(tracciatoRecord.getDati());
					break;
				case 123:
					// Dati codificati: MATERIALE Cartografico - Scala e coordinate ()
					tipoMateriale="C";
					c123 = ricostruisciC123(tracciatoRecord.getDati(), tracciatoRecord.getIndicatore1());
					break;
				case 124:
					// Dati codificati: MATERIALE Cartografico - designazione specifica del materiale ()
					tipoMateriale="C";
					c124 = ricostruisciC124(tracciatoRecord.getDati());
					break;
				case 125:
					// Dati codificati: MATERIALE Musica a stampa - designazione specifica del materiale ()
					tipoMateriale="U";
					c125 = ricostruisciC125(tracciatoRecord.getDati());
					break;
				case 126:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
					tipoMateriale="H";
					c126 = ricostruisciC126(tracciatoRecord.getDati());
					break;
				case 127:
					// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
					// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
					// DurataRegistraz
					c127 = ricostruisciC127(tracciatoRecord.getDati());
					break;
				case 128:
					// Dati codificati: Elaborazioni musicali (elaborazione) ()
					// almaviva2: modifica del 01.07.2015 per presenza di etichetta 128 non si attribuisce il tipo materiale U
					// se era già stata trovata etichetta 126 che lo rende Audiovisivo (quindi H)
					if (!tipoMateriale.equals("H"))  {
						tipoMateriale = "U";
					}
					c128 = ricostruisciC128(tracciatoRecord.getDati());
					break;
				case 140:
					// Dati codificati: MATERIALE ANTICO ()
					tipoMateriale="E";
					c140 = ricostruisciC140(tracciatoRecord.getDati());
					// Impostazione del tipoTestoLetterario per Antico
					c140bis = ricostruisciC140bis(tracciatoRecord.getDati());
					break;
				case 181:
					// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					c181.add(ricostruisciC181(areaAppoggio1));
					break;
				case 182:
					// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
					C182 tag182 = ricostruisciC182(areaAppoggio1);
					c182.add(tag182);
					// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
					if (isFilled(tag182.getA_182_0())) {
						c183 = new C183[1];
						c183[0] = imposta183Area0Default(tag182.getA_182_0());
					}
					break;
				case 200:
					// INFORMAZIONI DESCRITTIVE - Titolo e indicazione di responsabilita ()
					// Modifica almaviva2 13.07.2012 - intervento interno Import –  "*" dall’area del titolo proprio se la notizia è W
					tracciatoRecord.setDati(tracciatoRecord.getDati().replace("*", ""));
					c200 = ricostruisciC200(tracciatoRecord.getDati());
					break;
				case 205:
					// INFORMAZIONI DESCRITTIVE - Area dell'edizione
					c205 = ricostruisciC205(tracciatoRecord.getDati());
					break;
				case 206:
					// AREA SPECIFICA DEL MATERIALE: CARTOGRAFICO DATI MATEMATICI
					//tipoMateriale="C";
					c206 = ricostruisciC206(tracciatoRecord.getDati());
					break;
				case 207:
					// AREA DELLA NUMERAZIONE
					c207 = ricostruisciC207(tracciatoRecord.getDati());
					break;
				case 208:
					// AREA SPECIFICA DELLA MUSICA A STAMPA
					tipoMateriale="U";
					c208 = ricostruisciC208(tracciatoRecord.getDati());
					break;
				case 210:
					// AREA DELLA PUBBLICAZIONE
					c210 = ricostruisciC210(tracciatoRecord.getDati());
					// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
					// sia data1 che data2 - se mancanti si desumono dall'area di pubblicazione
					//almaviva5_20150312 per natura N la data va lasciata a null
					if (c100.getA_100_9() == null && !ValidazioneDati.equals(tracciatoRecord.getNatura(), "N")) {
						String dataPubblicazione = estraiDataPublicazione(tracciatoRecord.getDati());
						c100.setA_100_9(dataPubblicazione);
						c100.setA_100_13(dataPubblicazione);
					}
					break;
				case 215:
					// AREA DELLA DESCRIZIONE FISICA
					c215 = ricostruisciC215(tracciatoRecord.getDati());
					break;
				case 225:
					// DESCRIZIONI DELLE SERIE COLLEZIONI
					break;
				case 230:
					// AREA SPECIFICA DELLE RISORSE ELETTRONICHE
					a230 = ricostruisciA230(tracciatoRecord.getDati());
					break;
//				case 300:
//					// NOTE GENERALI
//					// almaviva2 15.06.2012 controllo su presenza della $a altrimenti si scarta il contenuto della nota
//					if (tracciatoRecord.getDati().contains("$a")) {
//						c3xx = new C3XX[1];
//						areaAppoggio1 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
//						if (isFilled(areaAppoggio1)) {
//							c3xx[0] = new C3XX();
//							c3xx[0].setA_3XX((String) areaAppoggio1);
//							c3xx[0].setTipoNota(SbnTipoNota.VALUE_0);
//						}
//
//						a300 = new A300();
//						areaAppoggio1 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
//						if (isFilled(areaAppoggio1)) {
//							a300.setA_300(areaAppoggio1);
//						}
//					}
//					break;
				case 300:
				case 301:
				case 302:
				case 303:
				case 304:
				case 305:
				case 306:
				case 307:
				case 308:
				case 310:
				case 311:
				case 312:
				case 313:
				case 314:
				case 315:
				case 316:
				case 317:
				case 318:
				case 320:
				case 321:
				case 322:
				case 324:
				case 325:
				case 326:
				case 328:
				case 332:
				case 333:
				case 334:
				case 345:
					// NOTE GENERALI
					// almaviva2 15.06.2012 controllo su presenza della $a altrimenti si scarta il contenuto della nota
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota300.length() == 0) {
							areaAppoggioNota300 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						} else {
							areaAppoggioNota300 = areaAppoggioNota300 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}

					// Modifica Novembre 2014: nel caso di etichetta 318 la presenza di $c e $j indica tempo e luogo della
					// rappresentazione e viene quindi valorizzata area c922
					if (tagNumeric == 318){
						if (tracciatoRecord.getDati().contains("$c") || tracciatoRecord.getDati().contains("$j")) {
							c922 = new C922();
							c922.setQ_922(tagliaEtichetta(tracciatoRecord.getDati(), 'c')); //anno data o periodo di rappresentazione
							c922.setS_922(tagliaEtichetta(tracciatoRecord.getDati(), 'j')); //luogo di rappresentazione
						}
					}
					break;

					// INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
				case 323:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota323.length() == 0) {
							areaAppoggioNota323 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 323
						} else {
							areaAppoggioNota323 = areaAppoggioNota323 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 327:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota327.length() == 0) {
							areaAppoggioNota327 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 327
						} else {
							areaAppoggioNota327 = areaAppoggioNota327 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 330:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota330.length() == 0) {
							areaAppoggioNota330 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 330
						} else {
							areaAppoggioNota330 = areaAppoggioNota330 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 336:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota336.length() == 0) {
							areaAppoggioNota336 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 336
						} else {
							areaAppoggioNota336 = areaAppoggioNota336 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 337:
					if (tracciatoRecord.getDati().contains("$a")) {
						if (areaAppoggioNota337.length() == 0) {
							areaAppoggioNota337 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 337
						} else {
							areaAppoggioNota337 = areaAppoggioNota337 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
						}
					}
					break;
				case 856:
					// AREA SPECIFICA DELLE INIDIRIZZO RISORSA ELETTRONICHE
					// Maggio 2013 - Viene inserita la corretta gestione di impostazione del campo uriDigitalBorn(etichetta 856)
					String uriDigitalBorn = tagliaEtichetta(tracciatoRecord.getDati(), 'u');
					byte[] indirizzo = uriDigitalBorn.getBytes();
					c856 = new C856();
					c856.setU_856(uriDigitalBorn);
					c856.setC9_856_1(indirizzo);
					break;
				case 922:
					// AREA SPECIFICA DELLE RAPPRESENTAZIONI (MAT. MUSICALE)
					// Dicembre 2014 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
					c922 = ricostruisciC922(tracciatoRecord.getDati());
					break;
				case 927:
					// AREA SPECIFICA DEL MATERIALE MUSICALE PER LEGAME A INTERPRETI
					// Gennaio 2015 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
					listaPersonaggi.add(tracciatoRecord.getDati());
					break;
				}
				// Modifica 30.11.2012 almaviva2 -controllo su lunghezza totale ISBD che non deve superare i 1200
				// (si tiene il limite a 1190 per i caratteri speciali eventuali
				switch (tagNumeric) {
				case 200:
				case 205:
				case 206:
				case 207:
				case 208:
				case 210:
				case 215:
				case 230:
					stringaISBD = stringaISBD + tracciatoRecord.getDati();
					break;
				}
			}

			if ((stringaISBD + areaAppoggioNota300).length() > 1180) {
				areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1180-stringaISBD.length());
				stringaISBD="";
			} else {
				stringaISBD="";
			}

			// INIZIO INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
			int note =0;
			if (isFilled(areaAppoggioNota300))
				note++;
			if (isFilled(areaAppoggioNota323))
				note++;
			if (isFilled(areaAppoggioNota327))
				note++;
			if (isFilled(areaAppoggioNota330))
				note++;
			if (isFilled(areaAppoggioNota336))
				note++;
			if (isFilled(areaAppoggioNota337))
				note++;

			c3xx = new C3XX[(note)];
			int k=0;

			// Bug mantis esercizio 6171- almaviva2 aprile 2016 a seguito del'intervento di inserimento della nota 321
			// la valorizzazione dei codici nota nella classe SbnTipoNota utilizzata per inviare il tipo di Nota al protocollo
			// Polo/Indice è cambiata; la valorizzazione del campo tipo nota viene quindi effettuata con metodo valueOf indicando
			// esplicitamente la nota in oggetto e non con il suo progressivo automatico che ha subito variazioni (e potrebbe subirne altre);
			if (isFilled(areaAppoggioNota300)) {
				if (areaAppoggioNota300.length() > 1920) {
					areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota300);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("300"));
				k++;

				a300 = new A300();
				a300.setA_300(areaAppoggioNota300);
				areaAppoggioNota300 = "";
			}
			if (isFilled(areaAppoggioNota323)) {
				if (areaAppoggioNota323.length() > 1920) {
					areaAppoggioNota323 = areaAppoggioNota323.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota323);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("323"));
				k++;
			}
			if (isFilled(areaAppoggioNota327)) {
				if (areaAppoggioNota327.length() > 1920) {
					areaAppoggioNota327 = areaAppoggioNota327.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota327);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("327"));
				k++;
			}
			if (isFilled(areaAppoggioNota330)) {
				if (areaAppoggioNota330.length() > 1920) {
					areaAppoggioNota330 = areaAppoggioNota330.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota330);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("330"));
				k++;
			}
			if (isFilled(areaAppoggioNota336)) {
				if (areaAppoggioNota336.length() > 1920) {
					areaAppoggioNota336 = areaAppoggioNota336.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota336);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("336"));
				k++;
			}
			if (isFilled(areaAppoggioNota337)) {
				if (areaAppoggioNota337.length() > 1920) {
					areaAppoggioNota337 = areaAppoggioNota337.substring(0, 1919);
				}
				c3xx[k] = new C3XX();
				c3xx[k].setA_3XX(areaAppoggioNota337);
				c3xx[k].setTipoNota(SbnTipoNota.valueOf("337"));
				k++;
			}

			// Parte vecchia sostituita dalla nuova gestione
			// Modifica 19.09.2012 almaviva2 - nuovo trattamento note richiesta  x trattamento altri tag da 301-345
//			if (isFilled(areaAppoggioNota300)) {
//				c3xx = new C3XX[1];
//				c3xx[0] = new C3XX();
//				c3xx[0].setA_3XX((String) areaAppoggioNota300);
//				c3xx[0].setTipoNota(SbnTipoNota.VALUE_0);
//
//				a300 = new A300();
//				a300.setA_300(areaAppoggioNota300);
//				areaAppoggioNota300 = "";
//			}
			// FINE INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi


			// 1. calcolo del codice tipo materiale del bid da utilizzare nell'inserimento
			AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
			// Identificativo del record:  contiene id record dalla Base Dati di partenza; essendo un nuovo inserimento in Polo
			// sara valorizzato con la routine di calcolo nuovo ID Sbn
			if (tipoMateriale.equals("")) {
				areaDatiPassGetIdSbn.setTipoMat(null);
			} else {
				areaDatiPassGetIdSbn.setTipoMat(tipoMateriale);
			}
			if (tracciatoRecord.getTipoRecord().equals("")) {
				areaDatiPassGetIdSbn.setTipoRec(null);
			} else {
				areaDatiPassGetIdSbn.setTipoRec(tracciatoRecord.getTipoRecord());
			}

			areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
			if (!isFilled(areaDatiPassGetIdSbn.getIdSbn())) {
				areaDatiPass.setCodErr(areaDatiPassGetIdSbn.getCodErr());
				testoLog = setTestoLog(idConvertito + "-" + areaDatiPassGetIdSbn.getTestoProtocollo());
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			} else {
				bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
			}

			if (isFilled(tracciatoRecord.getTipoRecord())) {
				guidaDoc.setTipoRecord(TipoRecord.valueOf(tracciatoRecord.getTipoRecord()));
			}

			//============================================
			// Controlli per sanare eventuali mancanze nel tracciato Unimarc
			// nel caso di Data assente viene impostata comunque il TipoData altrimenti il protocollo va in errore
			if (c100 == null) {
				c100 = new C100();
				c100.setA_100_8("f"); // Tipo data
				// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
				// sia data1 che data2
				c100.setA_100_9("1831");
				//almaviva5_20150123
				c100.setA_100_13(Integer.toString(currentYear - 1) );
			}

			if (numISBN != null) {
				if (numISBN.length > 0 && numISBN[0] == null) {
					numISBN = null;
				}
			}


			// Inizio intervento febbraio 2014 - inserimento della lingua Indefinita nel caso di W dove è mancante etichetta 101
			if (c101 == null) {
				c101 = new C101();
				c101.addA_101("UND");
			}
			// Fine intervento febbraio 2014


			if (c102 == null) {
				c102 = new C102();
				c102.setA_102("UN");
			}

			if (c215 == null) {
				c215 = new C215();
				c215.addA_215("sconosciuta");
			}

			// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
			if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
				if (!isFilled(c181) && !isFilled(c182)) {
					c181.add(imposta181Area0Default(tracciatoRecord.getTipoRecord()));
					C182 tag182 = imposta182Area0Default(tracciatoRecord.getTipoRecord());
					c182.add(tag182);

					// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
					if (isFilled(tag182.getA_182_0())) {
						c183 = new C183[1];
						c183[0] = imposta183Area0Default(tag182.getA_182_0());
					}
				}
			}
			// Fine almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)

			// Gestione documenti di type diverso
			if (tipoMateriale.equals("M")) {
				modernoType = new ModernoType();
				modernoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					modernoType.setT105bis(c105bis);
					modernoType.setT140bis(c140bis);
					modernoType.setT181(c181.toArray(new C181[0]));
					modernoType.setT182(c182.toArray(new C182[0]));

					// modernoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					modernoType.setT183(c183);
				}

				modernoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				modernoType.setT001(bidDaAssegnare);
				modernoType.setT005(null);
				modernoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				modernoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					modernoType.setNumSTD(datiDocType.getNumSTD());
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					modernoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					modernoType.setT856(new C856[] { c856 });
				if (c102 != null)
					modernoType.setT102(c102);
				if (c100 != null)
					modernoType.setT100(c100);
				if (c101 != null)
					modernoType.setT101(c101);
				if (c105 != null)
					modernoType.setT105(c105);
				if (c200 != null)
					modernoType.setT200(c200);
				if (c205 != null)
					modernoType.setT205(c205);
				if (c207 != null)
					modernoType.setT207(c207);
				if (c210 != null)
					modernoType.setT210(new C210[] { c210 });
				if (c215 != null)
					modernoType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						modernoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					modernoType.setT801(c801);
				datiDocType = modernoType;
			} else if (tipoMateriale.equals("E")) {
				anticoType = new AnticoType();
				anticoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					anticoType.setT105bis(c105bis);
					anticoType.setT140bis(c140bis);
					anticoType.setT181(c181.toArray(new C181[0]));
					anticoType.setT182(c182.toArray(new C182[0]));

					// anticoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					anticoType.setT183(c183);
				}

				anticoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				anticoType.setT001(bidDaAssegnare);
				anticoType.setT005(null);
				anticoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				anticoType.setGuida(guidaDoc);
				if (c856 != null)
					anticoType.setT856(new C856[] { c856 });
				if (c102 != null) {
					anticoType.setT102(c102);
				}
				if (c100 != null)
					anticoType.setT100(c100);
				if (c101 != null)
					anticoType.setT101(c101);
				if (c140 != null) {
					if (c140.getA_140_9Count() > 0) {
						anticoType.setT140(c140);
					}
				}
				// almaviva2 Febbraio 2016 - Intervento interno - Nel caso di documenti di natura W non va mai inserito asterisco
				// nell'area 200; tale trattamento è dovuto solo per le nature M
				if (c200 != null)
//					if (!c200.getA_200(0).contains("*")) {
//						c200.setA_200(0, "*" + c200.getA_200(0));
//					}
					anticoType.setT200(c200);
				if (c205 != null)
					anticoType.setT205(c205);
				if (c207 != null)
					anticoType.setT207(c207);
				if (c210 != null)
					anticoType.setT210(new C210[] { c210 });
				if (c215 != null)
					anticoType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						anticoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					anticoType.setT801(c801);
				if (c012 != null)
					anticoType.setT012(c012);
				datiDocType = anticoType;
			} else if (tipoMateriale.equals("U")) {
				musicaType = new MusicaType();
				musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					musicaType.setT105bis(c105bis);
					musicaType.setT140bis(c140bis);
					musicaType.setT181(c181.toArray(new C181[0]));
					musicaType.setT182(c182.toArray(new C182[0]));

					// musicaType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					musicaType.setT183(c183);
				}

				musicaType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				musicaType.setT001(bidDaAssegnare);
				musicaType.setT005(null);
				musicaType.setLivelloAut(SbnLivello.valueOf("05"));
				musicaType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				musicaType.setGuida(guidaDoc);
				// I numeri standard e l'impronta li aggiungo solo se il tipo record è diverso da d
				if (!tracciatoRecord.getTipoRecord().equals("d")) {
//					if (numISBN != null) {
//						musicaType.setNumSTD(numISBN);
//					}
					if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
						musicaType.setNumSTD(datiDocType.getNumSTD());


					if (c012 != null)
						musicaType.setT012(c012);
				}
				if (c856 != null)
					musicaType.setT856(new C856[] { c856 });
				if (c102 != null) {
					musicaType.setT102(c102);
				}
				if (c100 != null)
					musicaType.setT100(c100);
				if (c101 != null)
					musicaType.setT101(c101);
				if (c200 != null)
					musicaType.setT200(c200);
				if (c205 != null)
					musicaType.setT205(c205);
				if (c207 != null)
					musicaType.setT207(c207);
				if (c210 != null)
					musicaType.setT210(new C210[] { c210 });
				if (c215 != null)
					musicaType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						musicaType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					musicaType.setT801(c801);
				if (c125 != null)
					musicaType.setT125(c125);
				if (c128 != null)
					musicaType.setT128(c128);
				if (c923 != null)
					musicaType.setT923(c923);
				if (c922 != null)
					musicaType.setT922(c922);

//	Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
				if (isFilled(listaPersonaggi)) {
					c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
					musicaType.setT927(c927);
				}

				// DISASTERISCARE POI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//					// INCIPIT
//					if (getIncipit() != null)
//						musicaType.setT926(getIncipit());

				datiDocType = musicaType;
			} else if (tipoMateriale.equals("C")) {
				cartograficoType = new CartograficoType();
				cartograficoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					cartograficoType.setT105bis(c105bis);
					cartograficoType.setT140bis(c140bis);
					cartograficoType.setT181(c181.toArray(new C181[0]));
					cartograficoType.setT182(c182.toArray(new C182[0]));

					// cartograficoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					cartograficoType.setT183(c183);
				}

				cartograficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				cartograficoType.setT001(bidDaAssegnare);
				cartograficoType.setT005(null);
				cartograficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				cartograficoType.setLivelloAut(SbnLivello.valueOf("05"));
				cartograficoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					cartograficoType.setNumSTD(numISBN);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					cartograficoType.setNumSTD(datiDocType.getNumSTD());


				if (c856 != null)
					cartograficoType.setT856(new C856[] { c856 });
				if (c102 != null) {
					cartograficoType.setT102(c102);
				}
				if (c100 != null)
					cartograficoType.setT100(c100);
				if (c101 != null)
					cartograficoType.setT101(c101);
				if (c200 != null)
					cartograficoType.setT200(c200);
				if (c205 != null)
					cartograficoType.setT205(c205);
				if (c207 != null)
					cartograficoType.setT207(c207);
				if (c210 != null)
					cartograficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					cartograficoType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						cartograficoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					cartograficoType.setT801(c801);
				if (c120 != null)
					cartograficoType.setT120(c120);
				if (c121 != null)
					cartograficoType.setT121(c121);
				if (c123 != null)
					cartograficoType.setT123(c123);
				if (c124 != null)
					cartograficoType.setT124(c124);
				datiDocType = cartograficoType;
			} else if (tipoMateriale.equals("G")) {
				graficoType = new GraficoType();
				graficoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					graficoType.setT105bis(c105bis);
					graficoType.setT140bis(c140bis);
					graficoType.setT181(c181.toArray(new C181[0]));
					graficoType.setT182(c182.toArray(new C182[0]));

					// graficoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					graficoType.setT183(c183);
				}

				graficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				graficoType.setT001(bidDaAssegnare);
				graficoType.setT005(null);
				graficoType.setLivelloAut(SbnLivello.valueOf("05"));
				graficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				graficoType.setGuida(guidaDoc);
//				if (numISBN != null)
//					graficoType.setNumSTD(numISBN);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					graficoType.setNumSTD(datiDocType.getNumSTD());


				if (c856 != null)
					graficoType.setT856(new C856[] { c856 });
				if (c102 != null) {
					graficoType.setT102(c102);
				}
				if (c100 != null)
					graficoType.setT100(c100);
				if (c101 != null)
					graficoType.setT101(c101);
				if (c200 != null)
					graficoType.setT200(c200);
				if (c205 != null)
					graficoType.setT205(c205);
				if (c207 != null)
					graficoType.setT207(c207);
				if (c210 != null)
					graficoType.setT210(new C210[] { c210 });
				if (c215 != null)
					graficoType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						graficoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					graficoType.setT801(c801);
				if (c116 != null)
					graficoType.setT116(c116);
				datiDocType = graficoType;
			} else if (tipoMateriale.equals("H")) {
				audiovisivoType = new AudiovisivoType();
				if (naturaW) {
					audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf("W"));
				} else {
					audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf(tracciatoRecord.getNatura()));
				}

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
					audiovisivoType.setT105bis(c105bis);
					audiovisivoType.setT140bis(c140bis);
					audiovisivoType.setT181(c181.toArray(new C181[0]));
					audiovisivoType.setT182(c182.toArray(new C182[0]));

					// audiovisivoType.setT183(c183);
				}

				// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
				// valorizzato solo per le nature M,W,S e non N
				if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
					audiovisivoType.setT183(c183);
				}

				// tipicita dell'audiovisivo
				audiovisivoType.setT115(c115);
				audiovisivoType.setT126(c126);
				audiovisivoType.setT127(c127);
				// FINE almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				audiovisivoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
				audiovisivoType.setT001(bidDaAssegnare);
				audiovisivoType.setT005(null);
				audiovisivoType.setLivelloAut(SbnLivello.valueOf("05"));
				audiovisivoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
				audiovisivoType.setGuida(guidaDoc);
				if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
					audiovisivoType.setNumSTD(datiDocType.getNumSTD());

				if (c856 != null)
					audiovisivoType.setT856(new C856[] { c856 });

				if (!tracciatoRecord.getNatura().equals("N")) {
					if (c102 != null) {
						audiovisivoType.setT102(c102);
					}
				}
				if (c100 != null)
					audiovisivoType.setT100(c100);
				if (c101 != null)
					audiovisivoType.setT101(c101);
				if (c200 != null)
					audiovisivoType.setT200(c200);
				if (c205 != null)
					audiovisivoType.setT205(c205);
				if (c207 != null)
					audiovisivoType.setT207(c207);
				if (c210 != null)
					audiovisivoType.setT210(new C210[] { c210 });
				if (c215 != null)
					audiovisivoType.setT215(c215);
				if (c3xx != null) {
					for (int i=0; i<c3xx.length; i++) {
						audiovisivoType.addT3XX(c3xx[i]);
					}
				}
				if (c801 != null)
					audiovisivoType.setT801(c801);
				// Modifica del 09/02/2014 almaviva2 campo c125 T125
				// ADEGUAMENTO ALLO SCHEMA 2.0 DOVE SCOMPARE QUESTO CAMPO CHE VIENE RICOMPRESO NEL 105/140
//				if (c125 != null)
//					audiovisivoType.setT125(c125);


				if (c128 != null)
					audiovisivoType.setT128(c128);

				if (c922 != null)
					audiovisivoType.setT922(c922);

//				Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
				if (isFilled(listaPersonaggi)) {
					c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
					audiovisivoType.setT927(c927);
				}

				datiDocType = audiovisivoType;
			}

			// Schema da 1.07 a 1.09
			if (c206 != null) {
				datiDocType.setT206(new C206[] { c206 });
			}
			if (c208 != null) {
				datiDocType.setT208(c208);
			}

			documentoTypeChoice.setDatiDocumento(datiDocType);
			documentoTypeChoice.getDatiDocumento().setCondiviso(DatiDocTypeCondivisoType.N);

			documentoType.setDocumentoTypeChoice(documentoTypeChoice);

			SbnRequestType sbnrequest = new SbnRequestType();

			creaTypeChoice.setDocumento(documentoType);
			creaType.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(creaType);
			areaDatiPass.setNatureW(naturaW);
			areaDatiPass.setCreaTypeW(creaType);
		}

		return areaDatiPass;
	}

	public AreaDatiImportSuPoloVO trattamentoLegamiDocumento(AreaDatiImportSuPoloVO areaDatiPass, String livelloGerarchiche) { // (etichette UNIMARC da 000 a 300)

		// Trattamento delle 400 per l'inserimento dei legami del documento in esame a documenti
		// che dovrebbero essere già stati inseriti con richiesta 4xx

		SbnSoggettiDAO sbnSoggettiDAO = new SbnSoggettiDAO(indice, polo, user);
		SbnClassiDAO sbnClassiDAO = new SbnClassiDAO(indice, polo, user);

		Tb_titolo titRadice = null;

		DatiLegameTitoloSoggettoVO datiLegameTitoloSoggettoVO;

		DatiLegameTitoloClasseVO datiLegameTitoloClasseVO;
		DocumentoType documentoTypeIdRadice = null;
		String idRadiceT005 = null;
		DocumentoType documentoTypeSave;

		UtilityCastor utilityCastor = new UtilityCastor();
		AreaDatiLegameTitoloVO areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();

		TracciatoDatiImport1ParzialeVO tracciatoRecord;

		String idNew = "";
		String idRadice = "";
		String idArrivoLegame = "";
		String idConvertito = "";
		String esitoProtocollo = "0000";
		String testoLog = "";
		String legameOldId="";
		String legameNewId="";
		boolean autore = false;
		boolean legameAutPrinc = false;

		SBNMarc sbnRisposta = null;
		CreaType creaType = null;

		creaType = new CreaType();
		creaType.setTipoControllo(SbnSimile.CONFERMA);
		DocumentoType documentoType;

		tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(0);

		idConvertito = String.valueOf(tracciatoRecord.getIdInput());


		idRadice = selectImportIdLink(areaDatiPass.getNrRichiesta(), tracciatoRecord.getIdInput());
		if (idRadice.startsWith("Errore")) {
			areaDatiPass.setCodErr("9999");
			testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
				+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
				+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()
				+ areaDatiPass.getTestoProtocollo() + " Codice Errore:" + idRadice));
			areaDatiPass.addListaSegnalazioni(testoLog);
			return areaDatiPass;
		} else if (idRadice.equals("")) {
			if (!areaDatiPass.isNatureW()) {
				if (tracciatoRecord.getTag().startsWith("6")) {
					idRadice = idConvertito;
				} else {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()
						+ areaDatiPass.getTestoProtocollo() + " Codice Errore:" + areaDatiPass.getCodErr()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					return areaDatiPass;
				}
			}
		}



		if (areaDatiPass.isNatureW()) {
			// informazioni relative al bid radice recuperate dal CreaType del precedente passaggio
			documentoTypeIdRadice = areaDatiPass.getCreaTypeW().getCreaTypeChoice().getDocumento();
			idRadice = documentoTypeIdRadice.getDocumentoTypeChoice().getDatiDocumento().getT001();
		} else {


			try {
				//almaviva5_20150707 lettura del titolo dal db senza utilizzare il protocollo sbnmarc.
				titRadice = titDao.getTitoloLazy(idRadice);
				if (titRadice == null) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE: fase di interrogazione su DB Polo"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
						+ " " + idNew );
					areaDatiPass.addListaSegnalazioni(testoLog);
					return areaDatiPass;
				}
			} catch (DaoManagerException e) {
				areaDatiPass.setCodErr("9999");
				testoLog = setTestoLog("ERRORE: fase di interrogazione su DB Polo"
					+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
					+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
					+ " " + idNew );
				areaDatiPass.addListaSegnalazioni(testoLog);
				return areaDatiPass;
			}

			// informazioni relative al bid radice per salvataggio del T005
			idRadiceT005 = SBNMarcUtil.timestampToT005(titRadice.getTs_var());
		}

		if (areaDatiPass.getListaTracciatoRecordArray() != null) {
			int size = areaDatiPass.getListaTracciatoRecordArray().size();
			for (int j = 0; j < size; j++) {

				// Dicembre 2015:si ripulisce tutta l'areaareaDatiLegameTitoloVO altrimenti si potrebbero creare situazioni
				// in cui il nuovo legame viene salvato con i dati del precedente (es. note che potrebbero nonm sessere valorizzate)
				areaDatiLegameTitoloVO = new AreaDatiLegameTitoloVO();

				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
				}

				if (in(tracciatoRecord.getTag(), "790", "791")) {
					testoLog = setTestoLog("ATTENZIONE: trovato tag " + tracciatoRecord.getTag() +" attualmente non gestito"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc Input=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
						+ " id unimarc Link=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				if (in(tracciatoRecord.getTag(), "451", "452", "453")) {
					testoLog = setTestoLog("ATTENZIONE: trovato tag " + tracciatoRecord.getTag() +" attualmente non gestito"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc Input=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
						+ " id unimarc Link=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Settembre 2013: si inserisce la segnalazione per tag 421 (S421S) che non viene trattato (inverso del 422)
				if (tracciatoRecord.getTag().equals("421")) {
					testoLog = setTestoLog("ATTENZIONE: trovato tag " + tracciatoRecord.getTag() +" attualmente non gestito"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc Input=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
						+ " id unimarc Link=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Intervento Ottobre 2013 eliminata l'esclusione di tag 463 e 464 (nel trattamento successivo
				// verranno comunque escluse a meno di quelle in cui la natura è N (Spogli)
				if (tracciatoRecord.getTag().equals("463")) {
					if (!tracciatoRecord.getNatura().equals("A")) {
						continue;
					}
				}


				String areaAppoggioNome="";
				String areaAppoggioTitolo="";
				String areaAppoggioIdentif="";
				if (tracciatoRecord.getTag().substring(0,1).equals("7")) {
//					String areaAppoggioA = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
//					String areaAppoggioB = tagliaEtichetta(tracciatoRecord.getDati(), 'b');
//					areaAppoggioNome = areaAppoggioA + areaAppoggioB;
					areaAppoggioNome = tracciatoRecord.getTitoloFormattato();

					// Ottobre 2015 - intervento interno - nell'import dei legami titolo-autori si deve verificare la presenza
					// di legamicon stesso bid-vid-tipoLegame ma differente tipo-relazione perchè sono casistiche possibili;
					// E' quindi necessario salvare nella ImportIdLink anche qesta informazione altrimenti si importa solo
					// il primo legame andando in duplicazione per i successivi (aggiunta gestione campo tempRelatorCode)
					String tempRelatorCode = tagliaEtichetta(tracciatoRecord.getDati(), '4');
					legameOldId = tracciatoRecord.getIdInput() +
								notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioNome) + tracciatoRecord.getTag() + tempRelatorCode;
				} else {
//					areaAppoggioTitolo = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
					areaAppoggioTitolo = tracciatoRecord.getTitoloFormattato();
					legameOldId = tracciatoRecord.getIdInput() +
								notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) + tracciatoRecord.getTag();
				}


				idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(), legameOldId);
				if (idArrivoLegame.startsWith("Errore")) {
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id legame=" + legameOldId + " testo Errore: " + idArrivoLegame);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} else if (!idArrivoLegame.equals("")) {
					// Inizio Intervento interno Febbraio 2014:
					// eliminato diagnostico per legame precedentemento inserito in Import per snellire il file di Log
//					testoLog = setTestoLog("Legame legameOldId " + legameOldId + " precedentemente inserito come:" + idArrivoLegame);
//					areaDatiPass.addListaSegnalazioni(testoLog);
					// Fine Intervento interno Febbraio 2014:
					continue;
				}

				if (!isFilled(tracciatoRecord.getDati())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo dati è vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				if (tracciatoRecord.getTag().substring(0,1).equals("7")) {
					idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
							notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioNome));
				} else if (tracciatoRecord.getTag().substring(0,1).equals("5")){
					idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
							notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
				} else if (tracciatoRecord.getTag().substring(0,1).equals("6")){
					idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
							notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
				} else {
					// inserire prima la chiamata con l'etichetta $1 che è presente nei dati e poi per
					// non trovato si ritenta con areaAppoggioTitolo
					areaAppoggioIdentif = tagliaEtichetta(tracciatoRecord.getDati(), '1');
					//almaviva5_20150604 controllo presenza etichetta 001 per occorrenze importate da basi dati con
					//identificativi preceduti da prefisso ISADN.
					if (areaAppoggioIdentif.length() >= 3) {
						if (areaAppoggioIdentif.substring(0, 3).equals("001")) {
							log.debug("sottocampo $1: " + areaAppoggioIdentif);
							areaAppoggioIdentif = areaAppoggioIdentif.substring(3, areaAppoggioIdentif.length()).trim();


							// Modifica 12/03/2015: aggiunto controllo per eliminare la dicitura It ICCU e gli \ per recuperare
							// le collane/varie gia presenti in Base Dati;
							areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("\\\\", "");
	//						areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("IT", "");
	//						areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ICCU", "");
							areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ITICCU", "");
							areaAppoggioIdentif = areaAppoggioIdentif.trim();

							idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
									notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioIdentif));

							// Intervento Settembre 2013: per verificare che il titolo arrivo di legame sia stato correttamente inserito
							// prima si ricerca sulla id_link con l'etichetta $1 che è presente nei dati e poi per
							// non trovato si ritenta con areaAppoggioTitolo
							if (idArrivoLegame.equals("")) {
								idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
										notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
							}


						} else {
							idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
									notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
						}
					} else {
						idArrivoLegame = selectImportIdLink(areaDatiPass.getNrRichiesta(),
								notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
					}
				}

				// almaviva2 - Gennaio 2015: in caso di creazione dei legami (trattamento import 001) se l'oggetto di arrivo del legame
				// non è presente sulla import-id-link non si continua tentando di inserire il lemame successivo ma si
				// interrompe il trattamento cos' da non impostare con il valore "T" il rocord in oggetto; in questo modo
				// dopo aver risolto i problema si potrà richiedere nuovamente il trattamento 001 si record ancora vivi!!!
				if (idArrivoLegame.startsWith("Errore")) {
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()
						+ areaDatiPass.getTestoProtocollo() + " Codice Errore:" + idArrivoLegame));
					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setCodErr("9999");
					return areaDatiPass;
					//continue;
				} else if (idArrivoLegame.equals("")) {
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
						+ " oggetto arrivo non trovato per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
						+ " per tag=" + tracciatoRecord.getTag()
						+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setCodErr("9999");
					return areaDatiPass;
					//continue;
				}


				String tipoLegame = "";
				String relatorCode = "";
				if (tracciatoRecord.getTag().substring(0,1).equals("6")) {
					int tagNumeric = Integer.parseInt(tracciatoRecord.getTag());
					// nel caso dei soggetti in tipoLegame viene impostato con il tag corrispondente anche se il
					// il tipo legame non esiste
					switch (tagNumeric) {
					case 600:
					case 601:
					case 602:
					case 604:
					case 605:
					case 606:
					case 607:
					case 610:
						try {
							tipoLegame= tracciatoRecord.getTag();
							datiLegameTitoloSoggettoVO = new DatiLegameTitoloSoggettoVO();
							datiLegameTitoloSoggettoVO.setLivelloPolo(true);
							datiLegameTitoloSoggettoVO.setBidNatura(Character.toString(titRadice.getCd_natura()));
							datiLegameTitoloSoggettoVO.setBidTipoMateriale(Character.toString(titRadice.getTp_materiale()));
							datiLegameTitoloSoggettoVO.setBid(idRadice);
							datiLegameTitoloSoggettoVO.setT005(idRadiceT005);
							datiLegameTitoloSoggettoVO.setBidLivelloAut(livelloSoglia(titRadice.getCd_livello()));
							datiLegameTitoloSoggettoVO.setOperazione(TipoOperazioneLegame.CREA);
							datiLegameTitoloSoggettoVO.getLegami().add(datiLegameTitoloSoggettoVO.new LegameTitoloSoggettoVO(idArrivoLegame, ""));

							sbnRisposta = sbnSoggettiDAO.gestioneLegameTitoloSoggetto(datiLegameTitoloSoggettoVO);
						} catch (Exception e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
										+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
										+ " ERROR >>" + e.getMessage());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						break;
					case 675:
					case 676:
					case 686:
						try {
							tipoLegame= tracciatoRecord.getTag();
							datiLegameTitoloClasseVO = new DatiLegameTitoloClasseVO();
							datiLegameTitoloClasseVO.setLivelloPolo(true);
							datiLegameTitoloClasseVO.setBidNatura(Character.toString(titRadice.getCd_natura()));
							datiLegameTitoloClasseVO.setBidTipoMateriale(Character.toString(titRadice.getTp_materiale()));
							datiLegameTitoloClasseVO.setBid(idRadice);
							datiLegameTitoloClasseVO.setT005(idRadiceT005);
							datiLegameTitoloClasseVO.setBidLivelloAut(livelloSoglia(titRadice.getCd_livello()));
							datiLegameTitoloClasseVO.setOperazione(TipoOperazioneLegame.CREA);
							datiLegameTitoloClasseVO.getLegami().add(datiLegameTitoloClasseVO.new LegameTitoloClasseVO("", idArrivoLegame, ""));

							sbnRisposta = sbnClassiDAO.gestioneLegameTitoloClasse(datiLegameTitoloClasseVO);
						} catch (Exception e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
										+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
										+ " ERROR >>" + e.getMessage());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						break;
					 default:
						testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
								+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
								+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

				} else {

					if (idArrivoLegame.substring(3, 4).equals("V")) {
						if (!tracciatoRecord.getTag().substring(0,1).equals("7")) {
							testoLog = setTestoLog("ERRORE nel record: Tag unimarc incoerente con identificativo di arrivo legame"
									+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
									+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
									+ " tag unimarc=" + tracciatoRecord.getTag());
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
						}
						autore = true;
					}

					//analiticaReturnIdLegDocVO = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

					Tb_titolo titArrivo = null;
					// Intervento Interno Luglio 2015: almaviva2 per evitare un accesso all'autore tramite protocollo
					// per ogni legame autore da creare al solo fine di reperire il tipo nome si effettua un accesso diretto
					// alla base dati
					if (autore) {
						//RICORDA analiticaReturnIdLegDocVO = gestioneAutori.creaRichiestaAnaliticoAutorePerVid(analiticaVO);
						//analiticaReturnIdLegDocVO.setCodErr("");
					} else {
						try {
							//almaviva5_20150707 lettura del titolo dal db senza utilizzare il protocollo sbnmarc.
							titArrivo = titDao.getTitoloLazy(idArrivoLegame);
							if (titArrivo == null) {
								areaDatiPass.setCodErr("9999");
								testoLog = setTestoLog("ERRORE in fase di interrogazione su DB Polo"
									+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
									+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " " + idNew);
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
							}

						} catch (DaoManagerException e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ERRORE in fase di interrogazione su DB Polo"
								+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
								+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
					}

					ModificaType modificaType = new ModificaType();
					//modificaType.setTipoControllo(SbnSimile.CONFERMA);
					modificaType.setTipoControllo(SbnSimile.SIMILE);

					documentoType = new DocumentoType();
					if (areaDatiPass.isNatureW()) {
						//dati completi del documento madre per creazione
						documentoType.setDocumentoTypeChoice(documentoTypeIdRadice.getDocumentoTypeChoice());
					} else {
						try {
							//dati per modifica
							DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();
							SbnLivello livello = SbnLivello.valueOf(livelloSoglia(titRadice.getCd_livello()));

							String natura = Character.toString(titRadice.getCd_natura());
							if (in(natura, "M", "S", "C", "W", "N", "R") ) {
								//documento
								DatiDocType datiDocType = new DatiDocType();
								datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));
								datiDocType.setTipoMateriale(SbnMateriale.valueOf(Character.toString(titRadice.getTp_materiale())));
								datiDocType.setT001(idRadice);
								datiDocType.setT005(idRadiceT005);
								datiDocType.setLivelloAutDoc(livello);
								documentoTypeChoice.setDatiDocumento(datiDocType);
							} else {
								//tit. accesso
								TitAccessoType titAccessoType = new TitAccessoType();
								titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(natura));
								titAccessoType.setT001(idRadice);
								titAccessoType.setT005(idRadiceT005);
								titAccessoType.setLivelloAut(livello);
								documentoTypeChoice.setDatiTitAccesso(titAccessoType);
							}
							documentoType.setDocumentoTypeChoice(documentoTypeChoice);

						} catch (IllegalArgumentException e) {
							log.error("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame + ": " + e.getMessage());
							throw e;
						}
					}

					VariazioneBodyTitoli xmlBodyTitoli = new VariazioneBodyTitoli();
					LegamiType[] arrayLegamiType = new LegamiType[1];

					areaDatiLegameTitoloVO.setBidPartenza(idRadice);
					areaDatiLegameTitoloVO.setTipoOperazione("Crea");

					String naturaBidPartenza = null;
					if (areaDatiPass.isNatureW()) {
						//dati completi del documento madre per creazione
						naturaBidPartenza = documentoTypeIdRadice.getDocumentoTypeChoice().getDatiDocumento().getNaturaDoc().toString();
					} else {
						//dati per modifica
						naturaBidPartenza = Character.toString(titRadice.getCd_natura());
					}


					// informazioni relative al bid radice
					// 04.12.2012 inserito controllo su troppi legami 700/710 rilevato durante migrazione Cassino

					if (autore) {
						// Intervento Interno Luglio 2015: almaviva2 per evitare un accesso all'autore tramite protocollo
						// per ogni legame autore da creare al solo fine di reperire il tipo nome si effettua un accesso diretto
						// alla base dati
						//elementAutType = analiticaReturnIdLegDocVO.getSbnMarcType().getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0);
						tipoLegame = tracciatoRecord.getTag();
						int tagNumeric = Integer.parseInt(tracciatoRecord.getTag());
						switch (tagNumeric) {
							case 700:
							case 710:
								if (legameAutPrinc) {
									areaDatiLegameTitoloVO.setTipoResponsNew("3");
								} else {
									areaDatiLegameTitoloVO.setTipoResponsNew("1");
									legameAutPrinc = true;
								}

								break;
							case 701:
							case 711:
								if (legameAutPrinc) {
									areaDatiLegameTitoloVO.setTipoResponsNew("2");
								} else {
									areaDatiLegameTitoloVO.setTipoResponsNew("1");
									legameAutPrinc = true;
								}
								break;
							case 702:
							case 712:
								areaDatiLegameTitoloVO.setTipoResponsNew("3");
								break;
							 default:
								testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
										+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
										+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
						}

						areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("AU");
						relatorCode = tagliaEtichetta(tracciatoRecord.getDati(), '4');

						// 04.12.2012 Inserita decodifica di controllo del relator code per evitare inserimento relator code inesistenti
						if (isFilled(relatorCode)) {
							String relatorCodeCorretto="";
							try {
								relatorCodeCorretto = CodiciProvider.unimarcToSBN(CodiciType.CODICE_LEGAME_TITOLO_AUTORE, relatorCode);
							} catch (Exception e) {
								areaDatiPass.setCodErr("9999");
								testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
									+ " RELATOR CODE INESISTENTE: " + relatorCode + "(" + e.getMessage() +")");
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
							}
							if (relatorCodeCorretto != null && relatorCodeCorretto.length() > 0) {
								areaDatiLegameTitoloVO.setRelatorCodeNew(relatorCode);

								// Inizio modifica Dicembre 2014 - nel caso in cui si sia in presenza di legame 712 (legame con Ente)
								// si imposta sempre il TipoResponsNew al valore 3 TRANNE nel caso di relator-code=650 che
								// identifica il legame di Tipo Editore: a questo punto si imposta il TipoResponsNew al valore 4
								if (tracciatoRecord.getTag().equals("712") && relatorCode.equals("650") ) {
									areaDatiLegameTitoloVO.setTipoResponsNew("4");
								}
								// Fine modifica Dicembre 2014
							} else {
								// Dicembre 2015 - si ripulisce il campo in presenza di CODICE_LEGAME_TITOLO_AUTORE non valido
								// altrimenti si salverebbe il legame con il vecchio valore
								areaDatiLegameTitoloVO.setRelatorCodeNew("");
							}
						}
						// Intervento Interno Luglio 2015: almaviva2per evitare un accesso all'autore tramite protocollo
						// per ogni legame autore da creare al solo fine di reperire il tipo nome si effettua un accesso diretto
						// alla base dati
						String tipoNome = selectTipoNomeSuTbAutore (idArrivoLegame);
						areaDatiLegameTitoloVO.setTipoNomeArrivo(tipoNome);
//						if (elementAutType.getDatiElementoAut() instanceof AutorePersonaleType) {
//							AutorePersonaleType personaleType = (AutorePersonaleType) elementAutType.getDatiElementoAut();
//							areaDatiLegameTitoloVO.setTipoNomeArrivo(personaleType.getTipoNome().toString());
//						} else {
//							EnteType enteType = (EnteType) elementAutType.getDatiElementoAut();
//							areaDatiLegameTitoloVO.setTipoNomeArrivo(enteType.getTipoNome().toString());
//						}
					} else {
						//String naturaBidArrivo = utilityCastor.getNaturaTitolo(idArrivoLegame, analiticaReturnIdLegDocVO.getSbnMarcType());
						String naturaBidArrivo = Character.toString(titArrivo.getCd_natura());
						areaDatiLegameTitoloVO.setNaturaBidArrivo(naturaBidArrivo);
						if (naturaBidArrivo.equals("A")) {
							areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("TU");
						} else {
							areaDatiLegameTitoloVO.setAuthorityOggettoArrivo("");
						}

						String codLegUnimarc = naturaBidPartenza + tracciatoRecord.getTag() + naturaBidArrivo;

						// Modifica interna x decodificare il legame N463M che non esiste sulle nostre tabelle in N461M
						// che invece rispetta le regole della formazione dei legami
						if (codLegUnimarc.equals("N463M")) {
							codLegUnimarc = "N461M";
						}
						if (codLegUnimarc.equals("N463S")) {
							codLegUnimarc = "N461S";
						}




						// Modifica interna Settembre 2013 - decodificare il legame WN462M che non esiste sulle nostre tabelle
						// in W461M che invece rispetta le regole della formazione dei legami
						if (codLegUnimarc.equals("W462M")) {
							codLegUnimarc = "W461M";
						}



						try {
							tipoLegame = CodiciProvider.unimarcToSBN(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO, codLegUnimarc);
						} catch (Exception e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
								+ " di tipo " + codLegUnimarc + "(" + e.getMessage() +")");
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						if (tipoLegame == null) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
								+ " di tipo " + codLegUnimarc + " non esiste sulla tabella dei legami ammessi");
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}

						areaDatiLegameTitoloVO.setNoteLegameNew("");
						areaDatiLegameTitoloVO.setTipoLegameNew(tipoLegame);

						// Inserimento sequenza di legame nel caso delle 410 (legame a collana)
						// Modifica dicembre 2014 - Inserimento nota al legame nel caso di etchetta 463/464 (legame a spogli)
						if (tracciatoRecord.getTag().equals("410") && tracciatoRecord.getDati().contains("$v")) {
							areaDatiLegameTitoloVO.setSequenzaNew(tagliaEtichetta(tracciatoRecord.getDati(), 'v')); // sequenza del legame
						} else if ((tracciatoRecord.getTag().equals("463") || tracciatoRecord.getTag().equals("464"))
								&& tracciatoRecord.getDati().contains("$v")){
							areaDatiLegameTitoloVO.setSequenzaNew("");
							areaDatiLegameTitoloVO.setNoteLegameNew(tagliaEtichetta(tracciatoRecord.getDati(), 'v')); // nota al legame
						} else {
							 // Modifica 13.07.2012 L.almaviva2 Posizione nella sequenza in legami 46x viene valorizzata con la $v della 410
							areaDatiLegameTitoloVO.setSequenzaNew("");
						}
					}


					areaDatiLegameTitoloVO.setFlagCondivisoLegame(false);
					areaDatiLegameTitoloVO.setIdArrivo(idArrivoLegame);
					arrayLegamiType = xmlBodyTitoli.addLegami(areaDatiLegameTitoloVO);
					if (arrayLegamiType == null) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID oggetto arrivo: " + idArrivoLegame
							+ " di tipo " + tipoLegame + "(Legame non previsto)");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					}
					documentoType.setLegamiDocumento(arrayLegamiType);

					SbnRequestType sbnrequest = new SbnRequestType();
					SbnMessageType sbnmessage = new SbnMessageType();


					if (areaDatiPass.isNatureW()) {
						CreaTypeChoice creaTypeChoice = new CreaTypeChoice();
						creaTypeChoice.setDocumento(documentoType);
						creaType.setCreaTypeChoice(creaTypeChoice);
						sbnrequest.setCrea(creaType);
					} else {
						modificaType.setDocumento(documentoType);
						sbnrequest.setModifica(modificaType);
					}

					sbnmessage.setSbnRequest(sbnrequest);

					try {
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE validazione - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
							+ " di tipo " + tipoLegame + "(" + ve.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
							+ " di tipo " + tipoLegame + "(" + e.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						return areaDatiPass;
					}
				}

				if (sbnRisposta == null) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE - legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
						+ " di tipo " + tipoLegame + "(noServerPol)");
					areaDatiPass.addListaSegnalazioni(testoLog);
					return areaDatiPass;
				}

				esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
				if (esitoProtocollo.equals("0000")) {
					areaDatiPass.setCodErr("0000");


					// Ottobre 2015 - intervento interno - nell'import dei legami titolo-autori si deve verificare la presenza
					// di legamicon stesso bid-vid-tipoLegame ma differente tipo-relazione perchè sono casistiche possibili;
					// E' quindi necessario salvare nella ImportIdLink anche qesta informazione altrimenti si importa solo
					// il primo legame andando in duplicazione per i successivi (aggiunta gestione campo relatorCode sulla chiave)
					if (tracciatoRecord.getTag().substring(0,1).equals("7")) {
//						legameOldId = tracciatoRecord.getIdInput() + areaAppoggioNome + tracciatoRecord.getTag();
						legameOldId = tracciatoRecord.getIdInput() +
								notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioNome) +
								tracciatoRecord.getTag() +
								relatorCode;
					} else {
//						if (areaDatiPass.isNatureW()) {
							legameOldId = tracciatoRecord.getIdInput() + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) + tracciatoRecord.getTag();
//						} else {
//							legameOldId = tracciatoRecord.getIdInput() + areaAppoggioTitolo + tracciatoRecord.getTag();
//						}

					}
					legameNewId = idRadice + idArrivoLegame + tipoLegame;

					// inserimento della sola W nel caso naturaW valorizzato a true
					if (areaDatiPass.isNatureW()) {
						insertImportIdLink(tracciatoRecord.getIdInput(), idRadice, areaDatiPass.getNrRichiesta());
					}
					areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
					insertImportIdLink(legameOldId, legameNewId, areaDatiPass.getNrRichiesta());
					// Inizio Intervento interno Febbraio 2014:
					// eliminato diagnostico per operazione OK su creazione legame in Import per snellire il file di Log
//					testoLog = setTestoLog("Inserito nuovo legame su tabella import_tb_link con coppia: " + legameOldId  + "-" + legameNewId + ";");
//					areaDatiPass.addListaSegnalazioni(testoLog);
					// Fine Intervento interno Febbraio 2014:

					// informazioni relative al bid radice e salvataggio del T005
					documentoTypeSave = utilityCastor.getElementoDocumento(idRadice, sbnRisposta);
					idRadiceT005 = documentoTypeSave.getDocumentoTypeChoice().getDatiDocumento().getT005();

				} else if (esitoProtocollo.equals("3030")) {
					areaDatiPass.setCodErr("0000");
					testoLog = setTestoLog("Il legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
						+ " di tipo " + tipoLegame + " gia' presente ");
					areaDatiPass.addListaSegnalazioni(testoLog);
				} else {
					areaDatiPass.setCodErr("9999");
					String msg = "ERRORE - Legame fra ID radice: " + idRadice + " e ID arrivo: " + idArrivoLegame
						+ " di tipo " + tipoLegame + " fallito "
						+ "(" + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() + ")";
					testoLog = setTestoLog(msg);
					log.error(msg);
					areaDatiPass.addListaSegnalazioni(testoLog);
				}
			}
		}
		return areaDatiPass;

	}



	public AreaDatiImportSuPoloVO trattamentoAutore(AreaDatiImportSuPoloVO areaDatiPass) { // (etichette UNIMARC 700)

		TracciatoDatiImport1ParzialeVO tracciatoRecord;
		String idConvertito = "";
		String vidDaAssegnare = "";
		String idFormaAccettataNew = "";
		String esitoProtocollo = "0000";
		String testoLog = "";

		// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
		// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
		// l'oggetto bibliografico e consentire al catalogatore di correggerlo
		boolean presenzaCaratteriSpec = false;
		// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

		String areaAppoggioNome = "";
		String areaAppoggioIdentif="";

		SBNMarc sbnRisposta;
		SbnMessageType sbnmessage = null;
		SbnRequestType sbnrequest = null;
		CreaType crea = null;
		CreaTypeChoice creaTypeChoice = null;
		ElementAutType elementAutType = null;
		DatiElementoType datiElemento = null;
		AutorePersonaleType autorePersonale = null;
		try {
		if (areaDatiPass.getListaTracciatoRecordArray() != null) {
			int size = areaDatiPass.getListaTracciatoRecordArray().size();
			for (int j = 0; j < size; j++) {

				areaDatiPass.setCodErr("0000");

				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);
				if (idConvertito.equals("")) {
					idConvertito = String.valueOf(tracciatoRecord.getIdInput());
				} else if (!(String.valueOf(tracciatoRecord.getIdInput()).equals(idConvertito))) {
					// Pulizia delle aree per nuovo autore
					idConvertito = String.valueOf(tracciatoRecord.getIdInput());
				}

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
				}
				int tagNumeric = Integer.parseInt(tracciatoRecord.getTag());

				if (!isFilled(tracciatoRecord.getDati()) ) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo dati è vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}


				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (tracciatoRecord.getDati().contains("^")) {
					presenzaCaratteriSpec = true;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

				idFormaAccettataNew = selectImportIdLink(areaDatiPass.getNrRichiesta(),
						notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getTitoloFormattato()));
				if (idFormaAccettataNew.startsWith("Errore")) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
							+ " " + idFormaAccettataNew);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} else if (!idFormaAccettataNew.equals("")) {
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("Identificativo Unimarc: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioNome)
//							+ " trattato in precedenza; Id:" + idFormaAccettataNew);
//					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
					continue;
				} else if (tracciatoRecord.getDati().contains("$3")) {
					// Modifica Gennaio 2015 BUG 5707 esercizio: se nell'etichetta 7?? è presente la $3 (identificativo) verifichiamo che abbia le
					// caratteristiche di un Vid e poi effettuiamo la ricerca su tb_autore così da non duplicare gli autori
					// se la ricerca da esito positivo si salva la coppia su import_id_link cos' da non duplicare le successive ricerche
					areaAppoggioIdentif =  tagliaEtichetta(tracciatoRecord.getDati(), '3');
					areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("\\\\", "");
					//areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("IT", "");
					//areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ICCU", "");
					areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ITICCU", "");
					areaAppoggioIdentif = areaAppoggioIdentif.trim();

					if (areaAppoggioIdentif.length() == 10 && areaAppoggioIdentif.substring(3,4).equals("V")) {
						idFormaAccettataNew = selectVidSuTbAutore(areaAppoggioIdentif, areaDatiPass.getNrRichiesta());
						if (idFormaAccettataNew.startsWith("Errore")) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ERRORE in ricerca su tb_autore per chiave contenuta nella $3: =" + areaAppoggioIdentif);
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						} else if (!idFormaAccettataNew.equals("")) {
							testoLog = setTestoLog("Identificativo VidUnimarc: " + areaAppoggioIdentif
									+ " presente in base dati sulla tb_autore");
							areaDatiPass.addListaSegnalazioni(testoLog);
							areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
							continue;
						}
					}
				}


				SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
				AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
				areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
				areaDatiPassGetIdSbn.setTipoAut("AU");

				areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
				if (areaDatiPassGetIdSbn.getIdSbn() == null	|| areaDatiPassGetIdSbn.getIdSbn().equals("")) {
					areaDatiPass.setCodErr(areaDatiPassGetIdSbn.getCodErr());
					testoLog = setTestoLog(idConvertito + "-" + areaDatiPassGetIdSbn.getTestoProtocollo());
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}
				vidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();

				switch (tagNumeric) {
				case 700:
				case 701:
				case 702:
					// Autore personale in forma accettata
					autorePersonale = new AutorePersonaleType();
					autorePersonale = valorizzaAutPers(tracciatoRecord, vidDaAssegnare);
					datiElemento = autorePersonale;
					datiElemento.setFormaNome(SbnFormaNome.A);
					datiElemento.setT005(null);
					break;

				case 710:
				case 711:
				case 712:
					// Autore gruppo in forma accettata
					EnteType ente = new EnteType();
					ente = valorizzaAutEnte(tracciatoRecord, vidDaAssegnare);
					datiElemento = ente;
					datiElemento.setFormaNome(SbnFormaNome.A);
					datiElemento.setT005(null);
					break;

				case 790:
					testoLog = setTestoLog("ATTENZIONE: trovato tag " + tracciatoRecord.getTag() +" attualmente non gestito"
					+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
					+ " id unimarc Input=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
					+ " id unimarc Link=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					break;

				case 791:
					testoLog = setTestoLog("ATTENZIONE: trovato tag " + tracciatoRecord.getTag() +" attualmente non gestito"
					+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
					+ " id unimarc Input=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
					+ " id unimarc Link=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					break;
				 default:
					testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				sbnmessage = new SbnMessageType();
				sbnrequest = new SbnRequestType();
				crea = new CreaType();
				creaTypeChoice = new CreaTypeChoice();
				elementAutType = new ElementAutType();

				elementAutType.setDatiElementoAut(datiElemento);

				creaTypeChoice.setElementoAut(elementAutType);
				crea.setCreaTypeChoice(creaTypeChoice);
				crea.setTipoControllo(SbnSimile.CONFERMA);
				sbnrequest.setCrea(crea);
				sbnmessage.setSbnRequest(sbnrequest);

				sbnRisposta = new SBNMarc();
				// 2. invio dei dati al protocollo
				// 2.a inserimento in locale;
				try {
					this.polo.setMessage(sbnmessage, this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();
				} catch (SbnMarcException ve) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE validazione - inserimento Autore Id Unimarc: "
							+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
							+ " " + "(" + ve.getMessage() +")");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} catch (Exception e) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE inserimento Autore Id Unimarc: "
						+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
						+ " " + "(" + e.getMessage() +")");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				if (sbnRisposta == null) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE inserimento Autore Id Unimarc: "
						+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
						+ " " + " RISPOSTA DA PROTOCOLLO = null)");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
				if (esitoProtocollo.equals("3004")) {
					if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount()>0) {
						testoLog = setTestoLog("ID: " + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
							+ " risulta presente con Identificativo: "
							+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
						areaDatiPass.addListaSegnalazioni(testoLog);
					} else {
						testoLog = setTestoLog("ID: " + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
								+ " risulta presente");
						areaDatiPass.addListaSegnalazioni(testoLog);
					}
						// 2. nuovo invio dei dati al protocollo con l'aggiunta della qualificazione <OMONIMO NON IDENTIFICATO>
					if (sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getDatiElementoAut() instanceof EnteType) {
						EnteType ente = new EnteType();
						ente = (EnteType) sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getDatiElementoAut();
						ente.getT210().setA_210(ente.getT210().getA_210() + " <OMONIMO NON IDENTIFICATO>");
						sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().setDatiElementoAut(ente);
					} else {
						AutorePersonaleType autPers = new AutorePersonaleType();
						autPers = (AutorePersonaleType) sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getDatiElementoAut();
						autPers.getT200().setA_200(autPers.getT200().getA_200() + " <OMONIMO NON IDENTIFICATO>");
						sbnmessage.getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().setDatiElementoAut(autPers);
					}

					// 2.a Nuovo inserimento in locale;
					try {
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE validazione - inserimento Autore Id Unimarc: "
								+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
							+ " " + "(" + ve.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE inserimento Autore Id Unimarc: "
								+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
							+ " " + "(" + e.getMessage() +")");
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					if (sbnRisposta == null) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE inserimento Autore Id Unimarc: "
								+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
								+ " " + " RISPOSTA DA PROTOCOLLO = null)");
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}
					esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
					if (esitoProtocollo.equals("3004")) {
						areaDatiPass.setCodErr("0000");
						if (sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAutCount()>0) {
							testoLog = setTestoLog("ID: " + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " risulta presente con Identificativo: "
									+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001());
							insertImportIdLink(notEmptyOrOther(tracciatoRecord.getIdLink(),
									tracciatoRecord.getTitoloFormattato()), sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001(),
									areaDatiPass.getNrRichiesta());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
					}
				}

				if (!esitoProtocollo.equals("0000")) {
					areaDatiPass.setCodErr("9999");
					String msg = "ERRORE inserimento Autore Id Unimarc: "
							+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) + " nuovo Id Sbn: " + vidDaAssegnare
						+ " " + esitoProtocollo + ":" +  sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
					testoLog = setTestoLog(msg);
					log.error(msg);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				areaDatiPass.setCodErr("0000");
//				testoLog = setTestoLog("Inserito ID: " + vidDaAssegnare + " corrispondente a UNIMARC: "
//						+ notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
//				areaDatiPass.addListaSegnalazioni(testoLog);

				// Intervento Interno in sede di import RML 23.11.2012 (corretta la gestione del contatore)
				areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
				insertImportIdLink(notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getTitoloFormattato()), vidDaAssegnare, areaDatiPass.getNrRichiesta());

				// Intervento 29 luglio 2013 - corretta la valorizzazione del old codice autore nel caso in cui id-link non
				// sia valorizzato
//				testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioNome)  + "-" + vidDaAssegnare + ";");

				// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
				// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//				testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getTitoloFormattato())  + "-" + vidDaAssegnare + ";");
//				areaDatiPass.addListaSegnalazioni(testoLog);

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (presenzaCaratteriSpec) {
					testoLog = setTestoLog("ATTENZIONE l'oggetto appena inserito con identificativo " + vidDaAssegnare
													+ " contiene il carattere ^; Ripristinare il carattere corretto");
					areaDatiPass.addListaSegnalazioni(testoLog);
					presenzaCaratteriSpec = false;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

			}
		}
		} catch (Exception e) {
			this.log.error(String.format("trattamentoAutore(): id_input = %s: ", idConvertito), e);
		}
		return areaDatiPass;
	}




	public AreaDatiImportSuPoloVO trattamentoTitCollegati4xx(AreaDatiImportSuPoloVO areaDatiPass) throws Exception { // (etichette UNIMARC 4xx)

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);

		TracciatoDatiImport1ParzialeVO tracciatoRecord;

		String idNew = "";
		String idNewAutLeg = "";
		String idConvertito = "";
		int tagNumeric;
		String tagStringInterno;
		int tagNumericInterno;
		String bidDaAssegnare = "";
		String areaAppoggioProgres = "";
		String areaAppoggio1 = "";
		String areaAppoggio2 = "";
		String areaAppoggio3 = "";
		String areaAppoggioA = "";
		String areaAppoggioB = "";
		String areaAppoggioNome = "";
		String areaAppoggioNota300 = "";
		String areaAppoggioNota323 = "";
		String areaAppoggioNota327 = "";
		String areaAppoggioNota330 = "";
		String areaAppoggioNota336 = "";
		String areaAppoggioNota337 = "";



		// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
		// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
		// l'oggetto bibliografico e consentire al catalogatore di correggerlo
		boolean presenzaCaratteriSpec = false;
		// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

		Boolean creaLegameAutore = false;
		List<String> listalegamiAutore;

		String esitoProtocollo = "0000";
		String testoLog = "";

		String tipoMateriale;
		String tipoRecord;
		String natura = "";

		SBNMarc sbnRisposta;
		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		creaType = new CreaType();
		creaType.setTipoControllo(SbnSimile.CONFERMA);

		creaTypeChoice = new CreaTypeChoice();

		DocumentoType documentoType = new DocumentoType();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();


		// Intervento Settembre 2013: inserita inizializzazione campo datiDocType perchè assente
//		DatiDocType datiDocType = null;
		DatiDocType datiDocType = new DatiDocType();



		TitAccessoType titAccessoType = null;
		TitAccessoTypeChoice titAccessoTypeChoice = new TitAccessoTypeChoice();

		GuidaDoc guidaDoc = new GuidaDoc();

		String stringaISBD="";
		String areaAppoggioTitolo = "";

		if (areaDatiPass.getListaTracciatoRecordArray() != null) {
			int size = areaDatiPass.getListaTracciatoRecordArray().size();
			for (int j = 0; j < size; j++) {

				areaDatiPass.setCodErr("0000");



				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);
				idConvertito = String.valueOf(tracciatoRecord.getIdInput());

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
				}
				tagNumeric = Integer.parseInt(tracciatoRecord.getTag());

				if (!(tracciatoRecord.getDati()!= null && tracciatoRecord.getDati().trim().length() > 0)) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo dati è vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (tracciatoRecord.getDati().contains("^")) {
					presenzaCaratteriSpec = true;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"


				// in questa fase devono essere trattati solo i titoli i cui legami non sono gerarchici che verranno trattati in separata
				// sede (questi infatti compariranno anche com 001 e lì verranno caricati)
				if (in(tracciatoRecord.getTag(), "461", "462", "463")) {
					continue;
				} else
					if (in(tracciatoRecord.getTag(), "419")) {
						testoLog = setTestoLog("ATTENZIONE: E' stato individuato tag " + tracciatoRecord.getTag()
								+ " ammesso da UNIMARC ma non gestito da import per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
								+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

				areaAppoggioTitolo = tracciatoRecord.getTitoloFormattato();
				idNew = selectImportIdLink(areaDatiPass.getNrRichiesta(), notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
				if (idNew.startsWith("Errore")) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
							+ " " + idNew);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} else if (!idNew.equals("")) {
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("Id Unimarc: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) +
//							" trattato in precedenza; Id:" + idNew);
//					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
					continue;
				}  else {
					// Modifica Febbraio 2015: ulteriore verifica per vedere se la COLLANA (tag 410) è giaà presente sulla tb_titolo
					// si utilizza il bid eventualmente presente sulla tracciatoRecord.getIdLink() così da non duplicare le collane
					// se la ricerca da esito positivo si salva la coppia su import_id_link cos' da non duplicare le successive ricerche
					if (tracciatoRecord.getTag().equals("410"))  {
						if (tracciatoRecord.getIdLink() != null) {

							// Modifica 05/03/2015: aggiunto controllo per eliminare la dicitura It ICCU e gli \ per recuperare
							// le collane gia presenti in Base Dati;
							String areaAppoggioIdentif = tracciatoRecord.getIdLink();
							areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("\\\\", "");
							//areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("IT", "");
							//areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ICCU", "");
							areaAppoggioIdentif = areaAppoggioIdentif.replaceAll("ITICCU", "");
							areaAppoggioIdentif = areaAppoggioIdentif.trim();

							idNew = selectBidSuTbTitolo(areaAppoggioIdentif, areaDatiPass.getNrRichiesta());
							if (idNew.startsWith("Errore")) {
								areaDatiPass.setCodErr("9999");
								testoLog = setTestoLog("ERRORE in ricerca su tb_titolo per chiave idLink: =" + tracciatoRecord.getIdLink());
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
							} else if (!idNew.equals("")) {
								testoLog = setTestoLog("Identificativo collana presente in idLink: " + tracciatoRecord.getIdLink()
										+ " presente in base dati sulla tb_titolo");
								areaDatiPass.addListaSegnalazioni(testoLog);
								areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
								continue;
							}
						}
					}
				}

				// Si crea la il titolo collegato tagliando la parte dati in base al carattere $1 e trattando la parte successiva
				// dell'etichetta come fosse il tag ($1200 esempio identifica l'etichetta 200)


				int inizioDati = 0;
				int fineStringa = 0;
				int fineDati = tracciatoRecord.getDati().length();
				tipoMateriale = "M";
				tipoRecord = "a";

				AnticoType anticoType = null;
				MusicaType musicaType = null;
				ModernoType modernoType = null;
				CartograficoType cartograficoType = null;
				GraficoType graficoType = null;
				AudiovisivoType audiovisivoType = null;
				// Intervento interno Febbraio 2014 inserita inizializzazione dell'area per errore riscontrato in Import SALERNO
				datiDocType = new DatiDocType();

				// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
				// e IMPORT PER DISCOTECA DI STATO
				// CAMPO FITTIZIO PER TEST SU VALORI NON DEFINITI
				// utilizzato per costruire la struttura del programma
				String fintoCampo = "";



				C100 c100 = null;
				C101 c101 = null;
				C102 c102 = null;
				C105 c105 = null;
				C105bis c105bis = null;
				C110 c110 = null;
				C140 c140 = null;
				C140bis c140bis = null;
				C200 c200 = null;
				C205 c205 = null;
				C206 c206 = null;
				C207 c207 = null;
				C210 c210 = null;
				C215 c215 = null;
				C801 c801 = null;
				C125 c125 = null;
				C126 c126 = null;
				C127 c127 = null;
				C128 c128 = null;
				C923 c923 = null;
				C922 c922 = null;

				C927[] c927 = null;
				List<String> listaPersonaggi = new ArrayList<String>();

				C120 c120 = null;
				C121 c121 = null;
				C123 c123 = null;
				C124 c124 = null;
				C115 c115 = null;
				C116 c116 = null;

				List<C181> c181 = new ArrayList<C181>();
				List<C182> c182 = new ArrayList<C182>();

				// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				C183[] c183 = null;

				C208 c208 = null;
				C856 c856 = null;


				A230 a230 = null;
				A300 a300 = null;
				C3XX[] c3xx = null;

				NumStdType[] numISBN = null;

				C012[] c012 = null;

				creaLegameAutore = false;
				listalegamiAutore = new ArrayList<String>();

				for (int scorrimento = inizioDati; scorrimento < fineDati; scorrimento++) {
					areaAppoggioProgres = tagliaEtichettaMolteplice(tracciatoRecord.getDati(), '1', scorrimento, fineDati);

					// Ottobre 2018 - almaviva2 viene inserito il controllo dopo la ricerca di una etichetta Unimarc con Molteplicità
					// affinche alla risposta di non trovato si blocchi la ricerca immediatamente e non continui a "spulciare" la stringa
					// dati carattere x carattere fino alla fine !!
					if (length(areaAppoggioProgres) == 0) {
						break;
					}

					for (int i = 0; i < areaAppoggioProgres.length(); i++) {
						if (areaAppoggioProgres.substring(i, i+1).equals("|")
								&& areaAppoggioProgres.substring(i+1,i+2).equals("|")
								&& areaAppoggioProgres.substring(i+2, i+3).equals("|")) {
							fineStringa = Integer.parseInt(areaAppoggioProgres.substring(0, i));
							areaAppoggio1 = areaAppoggioProgres.substring(i+3, areaAppoggioProgres.length());
							scorrimento = scorrimento + fineStringa - 1;
							break;
						}
					}
					tagStringInterno="1";
					tagNumericInterno = 1;
					if (areaAppoggio1.length() < 3) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + "area appoggio insufficiente per contenere tag:" + areaAppoggio1);
						areaDatiPass.addListaSegnalazioni(testoLog);
						break;
					} else {
						tagStringInterno = areaAppoggio1.substring(0,3);
						try {
							tagNumericInterno = Integer.parseInt(areaAppoggio1.substring(0,3));
						} catch (NumberFormatException nfe) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
										+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
										+ " ERROR >>" + nfe.getMessage());
							areaDatiPass.addListaSegnalazioni(testoLog);
						}
						areaAppoggio1 = areaAppoggio1.substring(3, areaAppoggio1.length());
					}

					switch (tagNumericInterno) {

						case 1:
							break;
						case 5:
							// Identificativo di versione: contiene la data ultima variazione del record dalla Base Dati di partenza;
							// essendo un nuovo inserimento in Polo non sara valorizzato
							break;

						//======================
						case 10:
							// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
							// ISBN (es:  $a04-122-0810-5$bScience Paperback edition   ):
							if (datiDocType.getNumSTDCount()< 4) {
								numISBN = ricostruisci010(tracciatoRecord.getDati());
								if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
									datiDocType.addNumSTD(numISBN[0]);
								}
							}
							break;
						case 11:
							// Inizio Modifica almaviva2 15.06.2012 se siamo in caso di Monografia si elimina la decodifica
							// dell'ISSN che è relativo alle sole Collane
							// ISSN (es:$a1972-456X$bErrato.):
							if (!tracciatoRecord.getNatura().equals("M")) {
								datiDocType.addNumSTD(ricostruisci011(tracciatoRecord.getDati())[0]);
							}
							break;
						case 12:
							// Impronta (es:.........):
							c012 = ricostruisci012(tracciatoRecord.getDati());
							break;
						case 13:
							// ISMN (es:.......):
							datiDocType.addNumSTD(ricostruisci013(tracciatoRecord.getDati())[0]);
							break;
						case 17:
							// Altri numeri standard  (es:.......):
							NumStdType numStdType = ricostruisci017(tracciatoRecord.getDati());
							if (numStdType != null)
								datiDocType.addNumSTD(numStdType);
							break;
						case 20:
							// Numero di bibliografia nazionale  (es:.......):
							numISBN = ricostruisci020(tracciatoRecord.getDati());
							if (numISBN != null && numISBN.length > 0 && numISBN[0] != null) {
								datiDocType.addNumSTD(numISBN[0]);
							}
							break;
						case 22:
							// Numero di pubblicazione governativa  (es:.......):
							datiDocType.addNumSTD(ricostruisci022(tracciatoRecord.getDati())[0]);
							break;
						case 71:
							// Numero di lastra (MUSICA) o numero editoriale (MUSICA)  (es:.......):
							// Modifica Ottobre 2013 con decodifica codici tag 071
							// .il numero viene inserito solo se è presente l'indicatore ( 2 e 4)
							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							// .il numero viene inserito anche per indicatore 0 e 1
							if (isFilled(tracciatoRecord.getDati())) {
								datiDocType.addNumSTD(ricostruisci071(tracciatoRecord.getTipoRecord(), tracciatoRecord.getDati(),
										tracciatoRecord.getIndicatore1(), tracciatoRecord.getIndicatore2())[0]);
							}
							break;
						case 100:
							// Dati generali per l’elaborazione (es:  $a19910206g19731977|||y0itaa0103    ba)
							c100 = ricostruisciC100(areaAppoggio1, tracciatoRecord.getTipoRecord(), tracciatoRecord.getNatura());
							break;
						case 101:
							// Lingua della pubblicazione (es: xx$aita$aeng$afre)
							c101 = ricostruisciC101(areaAppoggio1);
							break;
						case 102:
							// Paese della pubblicazione o distribuzione(es:  $ait)
							c102 = new C102();

							// Intervento del 23 gennaio 2014: tutti i tagli devono essere fatti su areaAppoggio1
							// e non su tracciatoRecord.getDati() che è l'area completa e non quella correttamente tagliata;
//							if (tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("")
//									|| tagliaEtichetta(tracciatoRecord.getDati(), 'a').equals("--")) {
//										c102.setA_102("UN");
//								} else {
//									c102.setA_102(tagliaEtichetta(tracciatoRecord.getDati(), 'a').toUpperCase());
//								}

							if (tagliaEtichetta(areaAppoggio1, 'a').equals("")
									|| tagliaEtichetta(areaAppoggio1, 'a').equals("--")) {
										c102.setA_102("UN");
								} else {
									c102.setA_102(tagliaEtichetta(areaAppoggio1, 'a').toUpperCase());
								}



//							c102.setA_102(tagliaEtichetta(areaAppoggio1, 'a').toUpperCase());
							break;
						case 105:
							// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
							// verificare quale if fare per differenziare 105 e 105bis dato che il tag dovrebbe essere NUMERICO
							// Dati codificati: MONOGRAFIE (es:   $a    e   00|||)
							c105 = ricostruisciC105(areaAppoggio1);
							// Impostazione del tipoTestoLetterario per Moderno
							c105bis = ricostruisciC105bis(tracciatoRecord.getDati());
							break;
						case 110:
							// Dati codificati: PERIODICI (es:  $aaGa |||0||0)
							c110 = new C110(); // tipo Seriale
							areaAppoggio2 = tagliaEtichetta(areaAppoggio1, 'a');
							c110.setA_110_0(TipoSeriale.valueOf(areaAppoggio2.substring(0,1)));
							break;
						case 115:
							// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
							// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record g
							tipoMateriale="H";
							tipoRecord="g";
							c115 = ricostruisciC115(areaAppoggio1);
							break;

						case 116:
							// Dati codificati: MATERIALE GRAFICO ()
							tipoMateriale = "G";
							tipoRecord="k";
							c116 = ricostruisciC116(areaAppoggio1);
							break;
						case 120:
							// Dati codificati: MATERIALE Cartografico - Dati Generali ()
							tipoMateriale = "C";
							tipoRecord = "e";
							c120 = ricostruisciC120(areaAppoggio1);
							break;
						case 121:
							// Dati codificati: MATERIALE Cartografico - Caratteristiche fisiche ()
							tipoMateriale="C";
							tipoRecord="e";
							c121 = ricostruisciC121(areaAppoggio1);
							break;
						case 123:
							// Dati codificati: MATERIALE Cartografico - Scala e coordinate ()
							tipoMateriale="C";
							tipoRecord="e";
							c123 = ricostruisciC123(areaAppoggio1, tracciatoRecord.getIndicatore1());
							break;
						case 124:
							// Dati codificati: MATERIALE Cartografico - designazione specifica del materiale ()
							tipoMateriale="C";
							tipoRecord="e";
							c124 = ricostruisciC124(areaAppoggio1);
							break;
						case 125:
							// Dati codificati: MATERIALE Musica a stampa - designazione specifica del materiale ()
							tipoMateriale="U";
							tipoRecord="c";
							c125 = ricostruisciC125(areaAppoggio1);
							break;
						case 126:
							// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
							// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
							tipoMateriale="H";
							fintoCampo = "fintoCampo";
							if (fintoCampo.equals("")) {
								tipoRecord="i";
							} else  {
								tipoRecord="j";
							}
							c126 = ricostruisciC126(areaAppoggio1);
							break;
						case 127:
							// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
							// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record i oppure j
							// DurataRegistraz
							c127 = ricostruisciC127(areaAppoggio1);
							break;
						case 128:
							// Dati codificati: Elaborazioni musicali (elaborazione) ()
							// almaviva2: modifica del 01.07.2015 per presenza di etichetta 128 non si attribuisce il tipo materiale U
							// se era già stata trovata etichetta 126 che lo rende Audiovisivo (quindi H)
							if (!tipoMateriale.equals("H"))  {
								tipoMateriale = "U";
								tipoRecord="c";
							}
							c128 = ricostruisciC128(areaAppoggio1);
							break;
						case 140:
							// Dati codificati: MATERIALE ANTICO ()
							tipoMateriale="E";
							tipoRecord="a";
							c140 = ricostruisciC140(areaAppoggio1);
							// Impostazione del tipoTestoLetterario per Antico
							c140bis = ricostruisciC140bis(tracciatoRecord.getDati());
							break;

						case 181:
						    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
							c181.add(ricostruisciC181(areaAppoggio1));
							break;

						case 182:
						    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
							C182 tag182 = ricostruisciC182(areaAppoggio1);
							c182.add(tag182);
							// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
							if (isFilled(tag182.getA_182_0())) {
								c183 = new C183[1];
								c183[0] = imposta183Area0Default(tag182.getA_182_0());
							}
							break;

						case 200:
							// INFORMAZIONI DESCRITTIVE - Titolo e indicazione di responsabilita ()
							// Modifica 03.12.2012 almaviva2 - trattamento con area del getTitoloFormattato deve essere fatta solo x le 410
							// e non per tutte le 400 (esempio le nature T 423 non va bene) durante import Cassino
							// Modifica 07.12.2012 almaviva2 - Ulteriore trattamento con area del getTitoloFormattato
							c200 = ricostruisciC200(areaAppoggio1);
							if (c200.getA_200Count() == 0) {
								areaDatiPass.setCodErr("9999");
								testoLog = setTestoLog("ATTENZIONE: Manca la definizione dell'etichetta 200"
										+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
										+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
										+ " area dati:" + tracciatoRecord.getDati());
								areaDatiPass.addListaSegnalazioni(testoLog);
							}
							if (tracciatoRecord.getTag().equals("410") && tracciatoRecord.getDati210().length() > 0) {
								// AREA DELLA PUBBLICAZIONE ricostruita dall'apposito campo di select
								c210 = new C210();

								// almaviva2 Febbraio 2016 - Intervento interno - Nuovo metodo utilizzato solo nella ricostruzione delle collane (Trattamento 4XX)
								// in questo caso il trattamento dell'etichetta 210 deve trattare solo la $a $c e tralasciare gli altri
								//c210 = ricostruisciC210(tracciatoRecord.getDati210());
								c210 = ricostruisciC210PerCollane(tracciatoRecord.getDati210());
							}
							break;
						case 205:
							// INFORMAZIONI DESCRITTIVE - Area dell'edizione
							c205 = ricostruisciC205(areaAppoggio1);
							break;
						case 206:
							// AREA SPECIFICA DEL MATERIALE: CARTOGRAFICO DATI MATEMATICI
							//tipoMateriale="C";
							tipoRecord="e";
							c206 = ricostruisciC206(tracciatoRecord.getDati());
							break;
						case 207:
							// AREA DELLA NUMERAZIONE
							c207 = ricostruisciC207(areaAppoggio1);
							break;
						case 208:
							// AREA SPECIFICA DELLA MUSICA A STAMPA
							tipoMateriale="U";
							tipoRecord="c";
							c208 = ricostruisciC208(areaAppoggio1);
							break;
						case 210:
							// AREA DELLA PUBBLICAZIONE
							c210 = ricostruisciC210(areaAppoggio1);
							// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
							// sia data1 che data2 - se mancanti si desumono dall'area di pubblicazione
							//almaviva5_20150312 per natura N la data va lasciata a null
							if (c100.getA_100_9() == null && !ValidazioneDati.equals(tracciatoRecord.getNatura(), "N")) {
								String dataPubblicazione = estraiDataPublicazione(tracciatoRecord.getDati());
								c100.setA_100_9(dataPubblicazione);
								c100.setA_100_13(dataPubblicazione);
							}
							break;
						case 215:
							// AREA DELLA DESCRIZIONE FISICA
							c215 = ricostruisciC215(areaAppoggio1);
							break;
						case 225:
							// DESCRIZIONI DELLE SERIE COLLEZIONI
							break;
						case 230:
							// AREA SPECIFICA DELLE RISORSE ELETTRONICHE
							a230 = ricostruisciA230(areaAppoggio1);
							break;
//						case 300:
//							// NOTE GENERALI
//							c3xx = new C3XX[1];
//							areaAppoggio2 = tagliaEtichetta(areaAppoggio1, 'a'); //testo della nota
//							if (isFilled(areaAppoggio2)) {
//								c3xx[0] = new C3XX();
//								c3xx[0].setA_3XX((String) areaAppoggio2);
//								c3xx[0].setTipoNota(SbnTipoNota.VALUE_0);
//							}
//
//							a300 = new A300();
//							areaAppoggio2 = tagliaEtichetta(areaAppoggio1, 'a'); //testo della nota
//							if (isFilled(areaAppoggio2)) {
//								a300.setA_300(areaAppoggio2);
//							}
//							break;

						case 300:
						case 301:
						case 302:
						case 303:
						case 304:
						case 305:
						case 306:
						case 307:
						case 308:
						case 310:
						case 311:
						case 312:
						case 313:
						case 314:
						case 315:
						case 316:
						case 317:
						case 318:
						case 320:
						case 321:
						case 322:
						case 324:
						case 325:
						case 326:
						case 328:
						case 332:
						case 333:
						case 334:
						case 345:
							// NOTE GENERALI
							// almaviva2 15.06.2012 controllo su presenza della $a altrimenti si scarta il contenuto della nota
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota300.length() == 0) {
									areaAppoggioNota300 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								} else {
									areaAppoggioNota300 = areaAppoggioNota300 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							// Modifica Novembre 2014: nel caso di etichetta 318 la presenza di $c e $j indica tempo e luogo della
							// rappresentazione e viene quindi valorizzata area c922
							if (tagNumeric == 318){
								if (tracciatoRecord.getDati().contains("$c") || tracciatoRecord.getDati().contains("$j")) {
									c922 = new C922();
									c922.setQ_922(tagliaEtichetta(tracciatoRecord.getDati(), 'c')); //anno data o periodo di rappresentazione
									c922.setS_922(tagliaEtichetta(tracciatoRecord.getDati(), 'j')); //luogo di rappresentazione
								}
							}
							break;

							// INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
						case 323:
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota323.length() == 0) {
									areaAppoggioNota323 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 323
								} else {
									areaAppoggioNota323 = areaAppoggioNota323 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							break;
						case 327:
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota327.length() == 0) {
									areaAppoggioNota327 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 327
								} else {
									areaAppoggioNota327 = areaAppoggioNota327 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							break;
						case 330:
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota330.length() == 0) {
									areaAppoggioNota330 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 330
								} else {
									areaAppoggioNota330 = areaAppoggioNota330 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							break;
						case 336:
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota336.length() == 0) {
									areaAppoggioNota336 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 336
								} else {
									areaAppoggioNota336 = areaAppoggioNota336 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							break;
						case 337:
							if (tracciatoRecord.getDati().contains("$a")) {
								if (areaAppoggioNota337.length() == 0) {
									areaAppoggioNota337 = tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota 337
								} else {
									areaAppoggioNota337 = areaAppoggioNota337 + ". -" + tagliaEtichetta(tracciatoRecord.getDati(), 'a'); //testo della nota
								}
							}
							break;

						case 856:
							// AREA SPECIFICA DELLE INIDIRIZZO RISORSA ELETTRONICHE
							// Maggio 2013 - Viene inserita la corretta gestione di impostazione del campo uriDigitalBorn(etichetta 856)
							String uriDigitalBorn = tagliaEtichetta(tracciatoRecord.getDati(), 'u');
							byte[] indirizzo = uriDigitalBorn.getBytes();
							c856 = new C856();
							c856.setU_856(uriDigitalBorn);
							c856.setC9_856_1(indirizzo);
							break;
						case 922:
							// AREA SPECIFICA DELLE RAPPRESENTAZIONI (MAT. MUSICALE)
							// Dicembre 2014 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
							c922 = ricostruisciC922(tracciatoRecord.getDati());
							break;
						case 927:
							// AREA SPECIFICA DEL MATERIALE MUSICALE PER LEGAME A INTERPRETI
							// Gennaio 2015 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
							listaPersonaggi.add(tracciatoRecord.getDati());
							break;
						case 700:
						case 701:
						case 702:
						case 710:
						case 711:
						case 712:
							// AREA CHE CONTIENE L'AUTORE LEGATO ( Autore personale/gruppo in forma accettata)
							areaAppoggio2 = tagliaEtichetta(areaAppoggio1, '3'); //ricerca dell'identificativo
							areaAppoggio3 = tagliaEtichetta(areaAppoggio1, '4'); //ricerca relatorCode
							if (areaAppoggio3.equals("")) {
								areaAppoggio3 = "X";
							}

							if (isFilled(areaAppoggio2)) {
								idNewAutLeg = selectImportIdLinkObsoleta(areaDatiPass.getNrRichiesta(), areaAppoggio2, "V");
								if (idNewAutLeg.startsWith("Errore")) {
									areaDatiPass.setCodErr("9999");
									testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
											+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
											+ " id unimarc=" + notEmptyOrOther(areaAppoggio2, tracciatoRecord.getDati())
											+ " " + idNew);
									areaDatiPass.addListaSegnalazioni(testoLog);
								} else if (!idNewAutLeg.equals("")) {
									// l'autore è già presente: si deve inserire solo il legame
									creaLegameAutore = true;
									listalegamiAutore.add(tagStringInterno + areaAppoggio3 + idNewAutLeg);
								} else {
									// l'autore è assente; si deve inserire sia l'autore che il legame
									areaDatiPass.setCodErr("9999");
									testoLog = setTestoLog("ATTENZIONE: L'autore da collegare è assente da tabella import_id_link"
											+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
											+ " id unimarc=" + notEmptyOrOther(areaAppoggio2, tracciatoRecord.getDati())
											+ " " + idNew);
									areaDatiPass.addListaSegnalazioni(testoLog);
								}
							} else {
								//
								areaAppoggioA = tagliaEtichetta(areaAppoggio1, 'a');
								areaAppoggioB = tagliaEtichetta(areaAppoggio1, 'b');
								areaAppoggioNome = areaAppoggioA + areaAppoggioB;

								//idNewAutLeg = selectImportIdLinkObsoleta(areaDatiPass.getNrRichiesta(), areaAppoggioNome, "V");
								idNewAutLeg = selectImportIdLink(areaDatiPass.getNrRichiesta(), tagNumericInterno, areaAppoggio1);
								if (idNewAutLeg.startsWith("Errore")) {
									areaDatiPass.setCodErr("9999");
									testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
											+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
											+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
											+ " " + idNew);
									areaDatiPass.addListaSegnalazioni(testoLog);
								} else if (!idNewAutLeg.equals("")) {
									// l'autore è già presente: si deve inserire solo il legame
									creaLegameAutore = true;
									listalegamiAutore.add(tagStringInterno + areaAppoggio3 + idNewAutLeg);
								} else {
									// l'autore è assente; si deve inserire sia l'autore che il legame
									// Intervento 30 luglio 2013: se la chiave autore embedded è assente dalla id-link si elimina impostazione CodErr a 9999
									// e si procede con l'inserimento del titolo senza il legame
//									areaDatiPass.setCodErr("9999");

									// Intervento del 23 sett. 2013 nella diagnostica di non trovato oggetto sulla tabella
									// dei link deve essere impostato l'autore che si sta effettivamente cercando e non il titolo
									// a cui era agganciato (corretto sostituendo areaAppoggio1 a areaAppoggioTitolo)

									testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
											+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
//											+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo)
											+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggio1)
											+ " " + idNew);
									areaDatiPass.addListaSegnalazioni(testoLog);
								}
								break;
							}
							break;
						// -----------------------------------------------------
					}

					// Modifica 30.11.2012 almaviva2 -controllo su lunghezza totale ISBD che non deve superare i 1200
					// (si tiene il limite a 1190 per i caratteri speciali eventuali
					switch (tagNumericInterno) {
					case 200:
					case 205:
					case 206:
					case 207:
					case 208:
					case 210:
					case 215:
					case 230:
						stringaISBD = stringaISBD + tracciatoRecord.getDati();
						break;
					}
				}

				if ((stringaISBD + areaAppoggioNota300).length() > 1180) {
					areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1180-stringaISBD.length());
					stringaISBD="";
				} else {
					stringaISBD="";
				}


				// INIZIO INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi
		   	    int note =0;
			    if (isFilled(areaAppoggioNota300))
					note++;
				if (isFilled(areaAppoggioNota323))
					note++;
				if (isFilled(areaAppoggioNota327))
					note++;
				if (isFilled(areaAppoggioNota330))
					note++;
				if (isFilled(areaAppoggioNota336))
					note++;
				if (isFilled(areaAppoggioNota337))
					note++;

				c3xx = new C3XX[(note)];
				int k=0;

				// Bug mantis esercizio 6171- almaviva2 aprile 2016 a seguito del'intervento di inserimento della nota 321
				// la valorizzazione dei codici nota nella classe SbnTipoNota utilizzata per inviare il tipo di Nota al protocollo
				// Polo/Indice è cambiata; la valorizzazione del campo tipo nota viene quindi effettuata con metodo
				// valueOf indicando esplicitamente la nota in oggetto e non con il suo progressivo automatico che ha
				// subito variazioni (e potrebbe subirne altre);
				if (isFilled(areaAppoggioNota300)) {
					if (areaAppoggioNota300.length() > 1920) {
						areaAppoggioNota300 = areaAppoggioNota300.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota300);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("300"));
					k++;

					a300 = new A300();
					a300.setA_300(areaAppoggioNota300);
					areaAppoggioNota300 = "";
				}
				if (isFilled(areaAppoggioNota323)) {
					if (areaAppoggioNota323.length() > 1920) {
						areaAppoggioNota323 = areaAppoggioNota323.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota323);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("323"));
					k++;
				}
				if (isFilled(areaAppoggioNota327)) {
					if (areaAppoggioNota327.length() > 1920) {
						areaAppoggioNota327 = areaAppoggioNota327.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota327);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("327"));
					k++;
				}
				if (isFilled(areaAppoggioNota330)) {
					if (areaAppoggioNota330.length() > 1920) {
						areaAppoggioNota330 = areaAppoggioNota330.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota330);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("330"));
					k++;
				}
				if (isFilled(areaAppoggioNota336)) {
					if (areaAppoggioNota336.length() > 1920) {
						areaAppoggioNota336 = areaAppoggioNota336.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota336);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("336"));
					k++;
				}
				if (isFilled(areaAppoggioNota337)) {
					if (areaAppoggioNota337.length() > 1920) {
						areaAppoggioNota337 = areaAppoggioNota337.substring(0, 1919);
					}
					c3xx[k] = new C3XX();
					c3xx[k].setA_3XX(areaAppoggioNota337);
					c3xx[k].setTipoNota(SbnTipoNota.valueOf("337"));
					k++;
				}

				// Parte vecchia sostituita dalla nuova gestione
				// Modifica 19.09.2012 almaviva2 - nuovo trattamento note richiesta  x trattamento altri tag da 301-345
//				if (isFilled(areaAppoggioNota300)) {
//					c3xx = new C3XX[1];
//					c3xx[0] = new C3XX();
//					c3xx[0].setA_3XX((String) areaAppoggioNota300);
//					c3xx[0].setTipoNota(SbnTipoNota.VALUE_0);
	//
//					a300 = new A300();
//					a300.setA_300(areaAppoggioNota300);
//					areaAppoggioNota300 = "";
//				}
				// FINE INTERVENTO OTTOBRE 2013; (import POLI MAGING PAM) Gestione nuove note con TAG diversi




				// Mancano i campi che nel caso di documento radice sono presenti nell'etichetta:
				// .tipoMateriale
				// .tipoRecord
				// .natura (questa dovrebbe venir calcolata in base al tipo legame dedotto dal tag
				// In base al tagNumeric si può preimpostare il campo natura del documento da creare
				// Si imposta poi il tipo materiale (nei casi diversi dalla Collane - tag 410) e dedurre il tipoRecord (in via presunta)


				if (areaDatiPass.getCodErr().equals("9999")) {
					areaDatiPass.setCodErr("0000");
					continue;
				}



				// ATTENZIONE AL CAMPO 530
				boolean presenza530 = false;

				switch (tagNumeric) {
				case 410: // legame 01C quindi si cre una oggetto sulla tb_titolo con la natura suddetta
					natura="C";
					tipoMateriale="";
					tipoRecord="";
					break;
				case 421:
					if (tracciatoRecord.getNatura().toUpperCase().equals("S") || presenza530) {
						natura = "S";
					} else {
						natura = "M";
					}
					break;
				case 422:
					if (tracciatoRecord.getNatura().toUpperCase().equals("S") || presenza530) {
						natura = "S";
					} else {
						natura = "M";
					}
					break;
				case 423: // legame 03T quindi si crea una oggetto sulla tb_titolo con la natura suddetta
					natura="T";
					break;
				case 430: // legame 04  quindi si crea una oggetto sulla tb_titolo con la natura (campo 530 ?????)
					if (tracciatoRecord.getNatura().toUpperCase().equals("S") || tracciatoRecord.getNatura().toUpperCase().equals("C")) {
						natura = tracciatoRecord.getNatura().toUpperCase();
					} else if (tracciatoRecord.getNatura().toUpperCase().equals("M")) {
						if (presenza530) {
							natura = "S";
						} else {
							natura = "M";
						}
					}
					break;
				case 431: // legame 43S quindi si crea una oggetto sulla tb_titolo con la natura suddetta
					natura = "S";
					break;
				case 434: // legame 41S quindi si crea una oggetto sulla tb_titolo con la natura suddetta
					natura = "S";
					break;
				case 440: // legame 05  quindi si crea una oggetto sulla tb_titolo con la natura
					if (tracciatoRecord.getNatura().toUpperCase().equals("S") || tracciatoRecord.getNatura().toUpperCase().equals("C")) {
						natura = tracciatoRecord.getNatura().toUpperCase();
					} else if (tracciatoRecord.getNatura().toUpperCase().equals("M")) {
						if (presenza530) {
							natura = "S";
						} else {
							natura = "M";
						}
					}
					break;
				case 447: // legame 42S quindi si crea una oggetto sulla tb_titolo con la natura suddetta
					natura = "S";
					break;
				case 451: // legame 07  quindi si crea una oggetto sulla tb_titolo con la natura
				case 452: // legame 07  quindi si crea una oggetto sulla tb_titolo con la natura
				case 453: // legame 07  quindi si crea una oggetto sulla tb_titolo con la natura
					natura = tracciatoRecord.getNatura();
					break;
				case 454: // legame 06B quindi si crea una oggetto sulla tb_titolo con la natura suddetta
					natura = "B";
					break;
					// in questa fase devono essere trattati solo i titoli i cui legami non sono gerarchici perchè questi
					// verranno trattati in separata sede (questi infatti compariranno anche com 001 e lì verranno caricati)
//				case 461: // legame 01  LEGAME A LIVELLO PIU' ELEVATO (SET)(C,M,W)
//				case 462: // legame 01  LEGAME A LIVELLI INTERMEDI (SUBSET) (C,M,W)
//				case 463: // legame 01  LEGAME PARTE ANALITICA - PADRE (N????)
				case 464: // legame 51  LEGAME ALLO SPOGLIO (N)
					natura = "N";
					break;
				 default:
					testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}


				boolean invioProtocollo= true;
				if (invioProtocollo) {
					invioProtocollo = false;
					// 1. calcolo del codice tipo materiale del bid da utilizzare nell'inserimento
					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					// Identificativo del record:  contiene id record dalla Base Dati di partenza; essendo un nuovo inserimento in Polo
					// sara valorizzato con la routine di calcolo nuovo ID Sbn
					if (tipoMateriale.equals("")) {
						areaDatiPassGetIdSbn.setTipoMat(null);
					} else {
						areaDatiPassGetIdSbn.setTipoMat(tipoMateriale);
					}
					if (tipoRecord.equals("")) {
						areaDatiPassGetIdSbn.setTipoRec(null);
					} else {
						areaDatiPassGetIdSbn.setTipoRec(tipoRecord);
					}

					areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
					if (!isFilled(areaDatiPassGetIdSbn.getIdSbn())) {
						areaDatiPass.setCodErr(areaDatiPassGetIdSbn.getCodErr());
						testoLog = setTestoLog(idConvertito + "-" + areaDatiPassGetIdSbn.getTestoProtocollo());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					} else {
						bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
					}

					guidaDoc = new GuidaDoc();
					if (isFilled(tipoRecord)) {
						guidaDoc.setTipoRecord(TipoRecord.valueOf(tipoRecord));
					}

					// Controlli per sanare eventuali mancanze nel tracciato Unimarc
					// nel caso di Data assente viene impostata comunque il TipoData altrimenti il protocollo va in errore
					// Modifica Febbraio 2015per trattare le collane senza data si forza una C100 con tipoData "a" e data "19.." generica
					if (c100 == null) {
						// Novembre 2018 Anche nel caso di natura S (così come per la natura C) il tipo data deve essere
						// "a" e non "f"
						//if (natura == "C") {
						if (natura.equals("C") || natura.equals("S")) {
							c100 = new C100();
							c100.setA_100_8("a"); // Tipo data
							c100.setA_100_9("19..");
						} else {
							c100 = new C100();
							c100.setA_100_8("f"); // Tipo data
							// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
							// sia data1 che data2
							c100.setA_100_9("1831");
							//almaviva5_20150123
							c100.setA_100_13(Integer.toString(currentYear - 1) );
						}
					}

					if (numISBN != null) {
						if (numISBN.length > 0 && numISBN[0] == null) {
							numISBN = null;
						}
					}

					if (natura.equals("S")) {
						if (c101 == null) {
							c101 = new C101();
							c101.addA_101("UND");
						}
						if (c102 == null) {
							c102 = new C102();
							c102.setA_102("UN");
						}
						if (c210 == null) {
							Ac_210Type ac_210Type = new Ac_210Type();
							ac_210Type.setA_210(new String[] { "[S.l. : s.n.]" });
							c210 = new C210();
							c210.addAc_210(ac_210Type);
						}
						if (c215 == null) {
							c215 = new C215();
							c215.addA_215("sconosciuta");
						}
					} else if (natura.equals("C")) {
						tipoMateriale="";
						tipoRecord="";
						if (c102 == null) {
							c102 = new C102();
							c102.setA_102("UN");
						}
						if (c210 == null) {
							Ac_210Type ac_210Type = new Ac_210Type();
							ac_210Type.setA_210(new String[] { "[S.l. : s.n.]" });
							c210 = new C210();
							c210.addAc_210(ac_210Type);
						}

					} else if (natura.equals("N")) {
						if (c101 == null) {
							c101 = new C101();
							c101.addA_101("UND");
						}
					} else if (natura.equals("M")) {
						if (c101 == null) {
							c101 = new C101();
							c101.addA_101("UND");
						}
						if (c102 == null) {
							c102 = new C102();
							c102.setA_102("UN");
						}
					} else if (natura.equals("W")) {
						if (c101 == null) {
							c101 = new C101();
							c101.addA_101("UND");
						}
						if (c102 == null) {
							c102 = new C102();
							c102.setA_102("UN");
						}
					}

					// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
					if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
						if (!isFilled(c181) && !isFilled(c182)) {
							c181.add(imposta181Area0Default(tracciatoRecord.getTipoRecord()));
							C182 tag182 = imposta182Area0Default(tracciatoRecord.getTipoRecord());
							c182.add(tag182);

							// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
							if (isFilled(tag182.getA_182_0())) {
								c183 = new C183[1];
								c183[0] = imposta183Area0Default(tag182.getA_182_0());
							}
						}
					}
					// Fine almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)


					documentoType = new DocumentoType();

					char naturaChar = natura.charAt(0);
					switch (naturaChar) {
					case 'A':
						// Titolo Uniforme al momento non gestito
						break;
					case 'C':
					case 'R':
					case 'M':
					case 'S':
					case 'W':
					case 'N':
						// Gestione documenti di type diverso
						if (tipoMateriale.equals("M")) {
							modernoType = new ModernoType();
							modernoType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								modernoType.setT105bis(c105bis);
								modernoType.setT140bis(c140bis);
								modernoType.setT181(c181.toArray(new C181[0]));
								modernoType.setT182(c182.toArray(new C182[0]));

								// modernoType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								modernoType.setT183(c183);
							}

							modernoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							modernoType.setT001(bidDaAssegnare);
							modernoType.setT005(null);
							modernoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							modernoType.setGuida(guidaDoc);
//							if (numISBN != null)
//								modernoType.setNumSTD(numISBN);
							if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
								modernoType.setNumSTD(datiDocType.getNumSTD());


							if (c856 != null)
								modernoType.setT856(new C856[] { c856 });
							if (c102 != null)
								modernoType.setT102(c102);
							if (c100 != null)
								modernoType.setT100(c100);
							if (c101 != null)
								modernoType.setT101(c101);
							if (c105 != null)
								modernoType.setT105(c105);
							if (c200 != null)
								modernoType.setT200(c200);
							if (c205 != null)
								modernoType.setT205(c205);
							if (c207 != null)
								modernoType.setT207(c207);
							if (c210 != null)
								modernoType.setT210(new C210[] { c210 });
							if (c215 != null)
								modernoType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									modernoType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								modernoType.setT801(c801);
							datiDocType = modernoType;
						} else if (tipoMateriale.equals("E")) {
							anticoType = new AnticoType();
							anticoType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								anticoType.setT105bis(c105bis);
								anticoType.setT140bis(c140bis);
								anticoType.setT181(c181.toArray(new C181[0]));
								anticoType.setT182(c182.toArray(new C182[0]));

								// anticoType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								anticoType.setT183(c183);
							}

							anticoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							anticoType.setT001(bidDaAssegnare);
							anticoType.setT005(null);
							anticoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							anticoType.setGuida(guidaDoc);
							if (c856 != null)
								anticoType.setT856(new C856[] { c856 });
							if (c102 != null)
								anticoType.setT102(c102);
							if (c100 != null)
								anticoType.setT100(c100);
							if (c101 != null)
								anticoType.setT101(c101);
							if (c140 != null) {
								if (c140.getA_140_9Count() > 0) {
									anticoType.setT140(c140);
								}
							}
							if (c200 != null)
								anticoType.setT200(c200);
							if (c205 != null)
								anticoType.setT205(c205);
							if (c207 != null)
								anticoType.setT207(c207);
							if (c210 != null)
								anticoType.setT210(new C210[] { c210 });
							if (c215 != null)
								anticoType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									anticoType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								anticoType.setT801(c801);
							if (c012 != null)
								anticoType.setT012(c012);
							datiDocType = anticoType;
						} else if (tipoMateriale.equals("U")) {
							musicaType = new MusicaType();
							musicaType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								musicaType.setT105bis(c105bis);
								musicaType.setT140bis(c140bis);
								musicaType.setT181(c181.toArray(new C181[0]));
								musicaType.setT182(c182.toArray(new C182[0]));

								// musicaType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								musicaType.setT183(c183);
							}

							musicaType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							musicaType.setT001(bidDaAssegnare);
							musicaType.setT005(null);
							musicaType.setLivelloAut(SbnLivello.valueOf("05"));
							musicaType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							musicaType.setGuida(guidaDoc);
							// I numeri standard e l'impronta li aggiungo solo se il tipo record è diverso da d
							if (!tipoRecord.equals("d")) {
//								if (numISBN != null) {
//									musicaType.setNumSTD(numISBN);
//								}
								if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
									musicaType.setNumSTD(datiDocType.getNumSTD());


								if (c012 != null)
									musicaType.setT012(c012);
							}
							if (c856 != null)
								musicaType.setT856(new C856[] { c856 });
							if (c102 != null)
								musicaType.setT102(c102);
							if (c100 != null)
								musicaType.setT100(c100);
							if (c101 != null)
								musicaType.setT101(c101);
							if (c200 != null)
								musicaType.setT200(c200);
							if (c205 != null)
								musicaType.setT205(c205);
							if (c207 != null)
								musicaType.setT207(c207);
							if (c210 != null)
								musicaType.setT210(new C210[] { c210 });
							if (c215 != null)
								musicaType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									musicaType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								musicaType.setT801(c801);
							if (c125 != null)
								musicaType.setT125(c125);
							if (c128 != null)
								musicaType.setT128(c128);
							if (c923 != null)
								musicaType.setT923(c923);
							if (c922 != null)
								musicaType.setT922(c922);

//							Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
							if (isFilled(listaPersonaggi)) {
								c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
								musicaType.setT927(c927);
							}

							datiDocType = musicaType;
						} else if (tipoMateriale.equals("C")) {
							cartograficoType = new CartograficoType();
							cartograficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								cartograficoType.setT105bis(c105bis);
								cartograficoType.setT140bis(c140bis);
								cartograficoType.setT181(c181.toArray(new C181[0]));
								cartograficoType.setT182(c182.toArray(new C182[0]));

								// cartograficoType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								cartograficoType.setT183(c183);
							}

							cartograficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							cartograficoType.setT001(bidDaAssegnare);
							cartograficoType.setT005(null);
							cartograficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							cartograficoType.setLivelloAut(SbnLivello.valueOf("05"));
							cartograficoType.setGuida(guidaDoc);
//							if (numISBN != null)
//								cartograficoType.setNumSTD(numISBN);
							if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
								cartograficoType.setNumSTD(datiDocType.getNumSTD());


							if (c856 != null)
								cartograficoType.setT856(new C856[] { c856 });
							if (c102 != null)
								cartograficoType.setT102(c102);
							if (c100 != null)
								cartograficoType.setT100(c100);
							if (c101 != null)
								cartograficoType.setT101(c101);
							if (c200 != null)
								cartograficoType.setT200(c200);
							if (c205 != null)
								cartograficoType.setT205(c205);
							if (c207 != null)
								cartograficoType.setT207(c207);
							if (c210 != null)
								cartograficoType.setT210(new C210[] { c210 });
							if (c215 != null)
								cartograficoType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									cartograficoType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								cartograficoType.setT801(c801);
							if (c120 != null)
								cartograficoType.setT120(c120);
							if (c121 != null)
								cartograficoType.setT121(c121);
							if (c123 != null)
								cartograficoType.setT123(c123);
							if (c124 != null)
								cartograficoType.setT124(c124);
							datiDocType = cartograficoType;
						} else if (tipoMateriale.equals("G")) {
							graficoType = new GraficoType();
							graficoType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								graficoType.setT105bis(c105bis);
								graficoType.setT140bis(c140bis);
								graficoType.setT181(c181.toArray(new C181[0]));
								graficoType.setT182(c182.toArray(new C182[0]));

								// graficoType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								graficoType.setT183(c183);
							}

							graficoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							graficoType.setT001(bidDaAssegnare);
							graficoType.setT005(null);
							graficoType.setLivelloAut(SbnLivello.valueOf("05"));
							graficoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							graficoType.setGuida(guidaDoc);
//							if (numISBN != null)
//								graficoType.setNumSTD(numISBN);
							if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
								graficoType.setNumSTD(datiDocType.getNumSTD());


							if (c856 != null)
								graficoType.setT856(new C856[] { c856 });
							if (c102 != null)
								graficoType.setT102(c102);
							if (c100 != null)
								graficoType.setT100(c100);
							if (c101 != null)
								graficoType.setT101(c101);
							if (c200 != null)
								graficoType.setT200(c200);
							if (c205 != null)
								graficoType.setT205(c205);
							if (c207 != null)
								graficoType.setT207(c207);
							if (c210 != null)
								graficoType.setT210(new C210[] { c210 });
							if (c215 != null)
								graficoType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									graficoType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								graficoType.setT801(c801);
							if (c116 != null)
								graficoType.setT116(c116);
							datiDocType = graficoType;
						} else if (tipoMateriale.equals("H")) {
							audiovisivoType = new AudiovisivoType();
							audiovisivoType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								audiovisivoType.setT105bis(c105bis);
								audiovisivoType.setT140bis(c140bis);
								audiovisivoType.setT181(c181.toArray(new C181[0]));
								audiovisivoType.setT182(c182.toArray(new C182[0]));

								audiovisivoType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								audiovisivoType.setT183(c183);
							}

							// tipicita dell'audiovisivo
							audiovisivoType.setT115(c115);
							audiovisivoType.setT126(c126);
							audiovisivoType.setT127(c127);
							// FINE almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							audiovisivoType.setTipoMateriale(SbnMateriale.valueOf(tipoMateriale));
							audiovisivoType.setT001(bidDaAssegnare);
							audiovisivoType.setT005(null);
							audiovisivoType.setLivelloAut(SbnLivello.valueOf("05"));
							audiovisivoType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							audiovisivoType.setGuida(guidaDoc);
							if (datiDocType.getNumSTD() != null && datiDocType.getNumSTD().length > 0)
								audiovisivoType.setNumSTD(datiDocType.getNumSTD());

							if (c856 != null)
								audiovisivoType.setT856(new C856[] { c856 });

							if (!tracciatoRecord.getNatura().equals("N")) {
								if (c102 != null) {
									audiovisivoType.setT102(c102);
								}
							}
							if (c100 != null)
								audiovisivoType.setT100(c100);
							if (c101 != null)
								audiovisivoType.setT101(c101);
							if (c200 != null)
								audiovisivoType.setT200(c200);
							if (c205 != null)
								audiovisivoType.setT205(c205);
							if (c207 != null)
								audiovisivoType.setT207(c207);
							if (c210 != null)
								audiovisivoType.setT210(new C210[] { c210 });
							if (c215 != null)
								audiovisivoType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									audiovisivoType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								audiovisivoType.setT801(c801);
							// Modifica del 09/02/2014 almaviva2 campo c125 T125
							// ADEGUAMENTO ALLO SCHEMA 2.0 DOVE SCOMPARE QUESTO CAMPO CHE VIENE RICOMPRESO NEL 105/140
//							if (c125 != null)
//								audiovisivoType.setT125(c125);
							if (c128 != null)
								audiovisivoType.setT128(c128);

							if (c922 != null)
								audiovisivoType.setT922(c922);

//							Gennaio 2015 almaviva2 - Inserita gestione dei Personaggi/interpreti
							if (isFilled(listaPersonaggi)) {
								c927 = ricostruisciC927(listaPersonaggi, areaDatiPass.getNrRichiesta());
								audiovisivoType.setT927(c927);
							}

							datiDocType = audiovisivoType;
						} else {
							// E' una natura C, quindi senza tipo materiale
							datiDocType = new DatiDocType();
							datiDocType.setNaturaDoc(SbnNaturaDocumento.valueOf(natura));

							// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
							if (in(tracciatoRecord.getNatura(), "M", "W", "S", "N")) {
								datiDocType.setT105bis(c105bis);
								datiDocType.setT140bis(c140bis);
								datiDocType.setT181(c181.toArray(new C181[0]));
								datiDocType.setT182(c182.toArray(new C182[0]));

								// datiDocType.setT183(c183);
							}

							// Manutenzione interna Luglio 2016 - a correzione del precedente intervento su import : il campo tipo supporto deve essere
							// valorizzato solo per le nature M,W,S e non N
							if (in(tracciatoRecord.getNatura(), "M", "W", "S")) {
								datiDocType.setT183(c183);
							}

							datiDocType.setT001(bidDaAssegnare);
							datiDocType.setT005(null);
							datiDocType.setLivelloAutDoc(SbnLivello.valueOf("05"));
							datiDocType.setGuida(guidaDoc);
//							if (numISBN != null)
//								datiDocType.setNumSTD(numISBN);
							if (c856 != null)
								datiDocType.setT856(new C856[] { c856 });
							if (c102 != null)
								datiDocType.setT102(c102);
							if (c100 != null)
								datiDocType.setT100(c100);
							if (c101 != null)
								datiDocType.setT101(c101);
							if (c200 != null)
								datiDocType.setT200(c200);
							if (c205 != null)
								datiDocType.setT205(c205);
							if (c207 != null)
								datiDocType.setT207(c207);
							if (c210 != null)
								datiDocType.setT210(new C210[] { c210 });
							if (c215 != null)
								datiDocType.setT215(c215);
							if (c3xx != null) {
								for (int i=0; i<c3xx.length; i++) {
									datiDocType.addT3XX(c3xx[i]);
								}
							}
							if (c801 != null)
								datiDocType.setT801(c801);
						}
						// Schema da 1.07 a 1.09
						if (c206 != null)
							datiDocType.setT206(new C206[] { c206 });
						if (c208 != null)
							datiDocType.setT208(c208);

						datiDocType.setCondiviso(DatiDocTypeCondivisoType.N);
						documentoTypeChoice.setDatiDocumento(datiDocType);
						documentoTypeChoice.setDatiTitAccesso(null);
						documentoType.setDocumentoTypeChoice(documentoTypeChoice);
						creaTypeChoice.setDocumento(documentoType);
						break;
					case 'B':
					case 'D':
					case 'P':
					case 'T':

						titAccessoType = new TitAccessoType();
						titAccessoTypeChoice = new TitAccessoTypeChoice();
						titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(natura));
						titAccessoType.setT001(bidDaAssegnare);
						titAccessoType.setT005(null);
						titAccessoType.setLivelloAut(SbnLivello.valueOf("05"));
						C423 c423 = new C423();
						if (natura.equals("T")) {
							if (c101 != null) {
								c423.setT101(c101);
							} else {
								c101= new C101();
								c101.addA_101("UND");
								c423.setT101(c101);
							}
							if (c105 != null) {
								c423.setT105(c105);
							}
						}
						c423.setT200(c200);

						// Intervento interno Luglio 2016 per adeguare alla obbligatorietà della Lingua nei titoli di natura B
						// utilizzando quindi il tag T454A e non T454 (versione protocollo 2.02)
						if (natura.equals("B")) {
							//titAccessoTypeChoice.setT454(c200);
							C454A c454a = new C454A();
							c454a.setT200(c200);
							c101 = new C101();
							c101.addA_101("UND");
							c454a.setT101(c101);
							titAccessoTypeChoice.setT454A(c454a);
						} else if (natura.equals("D")) {
							titAccessoTypeChoice.setT517(c200);
						} else if (natura.equals("P")) {
							titAccessoTypeChoice.setT510(c200);
						} else if (natura.equals("T")) {
							titAccessoTypeChoice.setT423(c423);
						}

						titAccessoType.setTitAccessoTypeChoice(titAccessoTypeChoice);
						titAccessoType.setCondiviso(TitAccessoTypeCondivisoType.N);
						documentoTypeChoice.setDatiTitAccesso(titAccessoType);
						documentoTypeChoice.setDatiDocumento(null);
						documentoType.setDocumentoTypeChoice(documentoTypeChoice);
						creaTypeChoice.setDocumento(documentoType);
						break;
					}

					SbnRequestType sbnrequest = new SbnRequestType();
					SbnMessageType sbnmessage = new SbnMessageType();

					creaType.setCreaTypeChoice(creaTypeChoice);
					sbnrequest.setCrea(creaType);
					sbnmessage.setSbnRequest(sbnrequest);
					sbnRisposta = new SBNMarc();
					// 2. invio dei dati al protocollo
					// 2.a inserimento in locale;
					try {
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE validazione - Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + ve.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + e.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					if (sbnRisposta == null) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " RISPOSTA DA PROTOCOLLO = null");
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();

					if (!esitoProtocollo.equals("0000") && !esitoProtocollo.equals("3004")) {
						areaDatiPass.setCodErr("9999");
						String msg = "Identificativo Base:" + tracciatoRecord.getIdInput()
							+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo)
							+ " " + areaDatiPass.getTestoProtocollo()
							+ " Codice Errore:"
							+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
						testoLog = setTestoLog(msg);
						log.error(msg);
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					areaDatiPass.setCodErr("0000");
//					testoLog = setTestoLog("Inserito ID: " + bidDaAssegnare + " corrispondente a UNIMARC: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
//					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
					insertImportIdLink(notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo), bidDaAssegnare, areaDatiPass.getNrRichiesta());
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " +  bidDaAssegnare + "-" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) + ";");
//					areaDatiPass.addListaSegnalazioni(testoLog);

					// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
					// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
					// l'oggetto bibliografico e consentire al catalogatore di correggerlo
					if (presenzaCaratteriSpec) {
						testoLog = setTestoLog("ATTENZIONE l'oggetto appena inserito con identificativo " + bidDaAssegnare
														+ " contiene il carattere ^; Ripristinare il carattere corretto");
						areaDatiPass.addListaSegnalazioni(testoLog);
						presenzaCaratteriSpec = false;
					}
					// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"



					// Verifica su esistenza di legami autore al titolo appena creato
					if (creaLegameAutore) {
						creaLegameAutore = false;

						ModificaType modificaType = new ModificaType();
						LegamiType legamiType = new LegamiType();
						int listalegamiAutoreSize = listalegamiAutore.size();
						LegamiType[]arrayLegamiType = new LegamiType[listalegamiAutoreSize];
						LegameElementoAutType legameElementoAut;
						ArrivoLegame arrivoLegame = new ArrivoLegame();;
						for (int iLegAut=0; iLegAut<listalegamiAutoreSize; iLegAut++) {
							legamiType.setIdPartenza(bidDaAssegnare);
							legamiType.setTipoOperazione(SbnTipoOperazione.CREA);
							legameElementoAut = new LegameElementoAutType();
							legameElementoAut.setTipoAuthority(SbnAuthority.AU);
							legameElementoAut.setIdArrivo(listalegamiAutore.get(iLegAut).substring(4,14));
							legameElementoAut.setTipoLegame(SbnLegameAut.valueOf(listalegamiAutore.get(iLegAut).substring(0,3)));
							if (listalegamiAutore.get(iLegAut).substring(0,3).equals("700")
									|| listalegamiAutore.get(iLegAut).substring(0,3).equals("710")) {
								legameElementoAut.setTipoRespons(SbnRespons.valueOf("1"));
							} else if (listalegamiAutore.get(iLegAut).substring(0,3).equals("701")
									|| listalegamiAutore.get(iLegAut).substring(0,3).equals("711")) {
								legameElementoAut.setTipoRespons(SbnRespons.valueOf("2"));
							} else if (listalegamiAutore.get(iLegAut).substring(0,3).equals("702")
									|| listalegamiAutore.get(iLegAut).substring(0,3).equals("712")) {
								legameElementoAut.setTipoRespons(SbnRespons.valueOf("3"));
							}
							legameElementoAut.setCondiviso(LegameElementoAutTypeCondivisoType.N);
							legameElementoAut.setNoteLegame("");
							if (!listalegamiAutore.get(iLegAut).substring(3,4).equals("X")) {
								legameElementoAut.setRelatorCode(listalegamiAutore.get(iLegAut).substring(3,4));
							}
							arrivoLegame.setLegameElementoAut(legameElementoAut);

							ArrivoLegame[] arrayArrivoLegame = new ArrivoLegame[1];
							arrayArrivoLegame[0] = arrivoLegame;

							// aggiungo arrivo legame
							legamiType.setArrivoLegame(arrayArrivoLegame);

							arrayLegamiType[iLegAut] = legamiType;

						}

						documentoType = new DocumentoType();
						documentoType.setDocumentoTypeChoice(sbnRisposta.getSbnMessage().getSbnResponse()
								.getSbnResponseTypeChoice().getSbnOutput().getDocumento(0).getDocumentoTypeChoice());
						documentoType.setLegamiDocumento(arrayLegamiType);
						modificaType.setDocumento(documentoType);

						sbnrequest = new SbnRequestType();
						sbnmessage = new SbnMessageType();
						sbnrequest.setModifica(modificaType);
						sbnmessage.setSbnRequest(sbnrequest);
						sbnRisposta = new SBNMarc();

						// 2. invio dei dati al protocollo
						// 2.a inserimento in locale;
						try {
							this.polo.setMessage(sbnmessage, this.user);
							sbnRisposta = this.polo.eseguiRichiestaServer();
						} catch (SbnMarcException ve) {
							areaDatiPass.setCodErr("9999");
							setTestoLog("ERRORE validazione - Problemi in inserimento legami dal ID radice: " + bidDaAssegnare
									+ " ai ID indicati in : " + tracciatoRecord.getDati()
									+ " ERROR >>" + ve.getMessage());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						} catch (Exception e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("Problemi in inserimento legami dal ID radice: " + bidDaAssegnare
									+ " ai ID indicati in : " + tracciatoRecord.getDati()
										+ " ERROR >>" + e.getMessage());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}

						if (sbnRisposta == null) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("Problemi in inserimento legami dal ID radice: " + bidDaAssegnare
									+ " ai ID indicati in : " + tracciatoRecord.getDati()
										+ " RISPOSTA DA PROTOCOLLO = null");
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}

						esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
						if (!esitoProtocollo.equals("0000") && !esitoProtocollo.equals("3004")) {
							areaDatiPass.setCodErr(esitoProtocollo);
							String msg = "Problemi in inserimento legami dal ID radice: " + bidDaAssegnare
									+ " ai ID indicati in : " + tracciatoRecord.getDati()
								+ " " + areaDatiPass.getTestoProtocollo()
								+ " Codice Errore:"
								+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
							testoLog = setTestoLog(msg);
							log.error(msg);
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}

						areaDatiPass.setCodErr("0000");
						testoLog = setTestoLog("Inseriti legame dal ID radice: " + bidDaAssegnare + " ai ID indicati in : " + tracciatoRecord.getDati());
						areaDatiPass.addListaSegnalazioni(testoLog);
					}
				}
			}
		}
		return areaDatiPass;
	}


	private String selectImportIdLink(String nrRichiesta, int tag, String dati) {

		String testo = "";
		StringBuilder sql = new StringBuilder();
		try {
			//dati = dati.replace("'", "''").trim();
			dati = dati.trim();

			Object[] record;
			sql.append("select id_inserito, id_input from import_id_link ");
			sql.append("where id_input=calcola_import_id_link('").append(tag).append("', '").append(DaoManager.escapeSql(dati)).append("')");
			List list = getRecords(sql);
			if (!isFilled(list))
				return "";

			for (int i = 0; i < list.size(); i++) {
				record = (Object[]) list.get(i);
				testo = (String) record[0]; // id_inserito = bid sulla nuova base dati
			}
		} catch (Exception e) {
			testo = "Errore " + e.getMessage();
			return testo;
		}

		return testo;
	}

	public AreaDatiImportSuPoloVO trattamentoTitCollegati5xx(AreaDatiImportSuPoloVO areaDatiPass) { // (etichette UNIMARC 5xx)

		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);

		TracciatoDatiImport1ParzialeVO tracciatoRecord;

		String idNew = "";
		String idConvertito = "";
		int tagNumeric;
		String bidDaAssegnare = "";

		// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
		// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
		// l'oggetto bibliografico e consentire al catalogatore di correggerlo
		boolean presenzaCaratteriSpec = false;
		// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

		// Intervento settembre 2015 - si inserisce un contatore per i record non trattati su richiesta di 
		int contAmmessiUnimarcNonGestiti = 0;
		int contNonAmmessiUnimarc = 0;

		String esitoProtocollo = "0000";
		String testoLog = "";

		String tipoMateriale;
		String tipoRecord;
		String natura = "";

		SBNMarc sbnRisposta;
		CreaType creaType = null;
		CreaTypeChoice creaTypeChoice = null;

		creaType = new CreaType();
		creaType.setTipoControllo(SbnSimile.CONFERMA);

		creaTypeChoice = new CreaTypeChoice();

		DocumentoType documentoType = new DocumentoType();
		DocumentoTypeChoice documentoTypeChoice = new DocumentoTypeChoice();

		TitAccessoType titAccessoType = null;
		TitAccessoTypeChoice titAccessoTypeChoice = new TitAccessoTypeChoice();

		A928 a928 = null;
		A929 a929 = null;

		GuidaDoc guidaDoc = new GuidaDoc();
		String areaAppoggioTitolo = "";

		if (areaDatiPass.getListaTracciatoRecordArray() != null) {
			int size = size(areaDatiPass.getListaTracciatoRecordArray());
			for (int j = 0; j < size; j++) {

				// Modifica Dicembre 2014 inserimento dei tag 928 e 929 per estrazione delle informazioni sulla composizione per creare
				// i titoli Uniformi Musicali
				// le etichette 928/929 vengono cercate nelle posizioni subito successive a quella del tag 500
				// facendo affidamento sull'ordinamento della lista (per tag crescente)
				//almaviva5_20151027 fix per ricerca 928/929 su ultimo titolo estratto
				int jpiuUno = (j < (size - 1)) ? j + 1 : j;			//928
				int jpiuDue = (j < (size - 2)) ? j + 2 : jpiuUno;	//929

				areaDatiPass.setCodErr("0000");

				tracciatoRecord = areaDatiPass.getListaTracciatoRecordArray().get(j);
				idConvertito = String.valueOf(tracciatoRecord.getIdInput());

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
				}
				tagNumeric = Integer.parseInt(tracciatoRecord.getTag());

				if (!(tracciatoRecord.getDati()!= null && tracciatoRecord.getDati().trim().length() > 0)) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE valore del campo dati vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (tracciatoRecord.getDati().contains("^")) {
					presenzaCaratteriSpec = true;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

				areaAppoggioTitolo = tracciatoRecord.getTitoloFormattato();

				// Si crea la il titolo collegato utilizzando la $a che contiene il titolo del documento di tipo A
				tipoMateriale = "M";
				tipoRecord = "a";

				C100 c100 = null;
				C101 c101 = null;
				C200 c200 = null;
				A230 a230 = null;
				A300 a300 = null;

				//                         areaAppoggioTitolo = tagliaEtichetta(tracciatoRecord.getDati(), 'a');

				// ATTENZIONE AL CAMPO 530
//				boolean presenza530 = false;

				switch (tagNumeric) {
				case 500: // legame 09  LEGAME Natura oggetto A
					natura = "A";
					break;
				case 510: // legame 08  LEGAME Natura oggetto P
					natura = "P";
					break;
				case 517: // legame 08  LEGAME Natura oggetto D
					natura = "D";
					break;
//				case 514:   // LEGAME Natura oggetto D
//				case 515:
//					natura = "D";
//					break;
//				case 530: // legame 09  LEGAME Natura oggetto A
//					natura = "A";
//					break;
//				case 545: // legame 08  LEGAME Natura oggetto P
//					natura = "P";
//					break;

				case 928: // Aree per gestione Titoli Uniformi Musicali (vengono gestiti successivamente)
				case 929:
					continue;
				case 503: // AMMESSI DA UNIMARC MA NON GESTITO  DA IMPORT
				case 501:
				case 511:
				case 512:
				case 513:
				case 514:
				case 515:
				case 516:
				case 518:
				case 520:
				case 530:
				case 531:
				case 532:
				case 540:
				case 541:
				case 545:
				case 560:
					testoLog = setTestoLog("ATTENZIONE: E' stato individuato tag " + tracciatoRecord.getTag()
							+ " ammesso da UNIMARC ma non gestito da import per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					contAmmessiUnimarcNonGestiti++;
					continue;
				 default:
					testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					contNonAmmessiUnimarc++;
					continue;
				}

				idNew = selectImportIdLink(areaDatiPass.getNrRichiesta(), notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
				if (idNew.startsWith("Errore")) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
							+ " " + idNew);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} else if (!idNew.equals("")) {
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("Id Unimarc: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) +
//							" trattato in precedenza; Id:" + idNew);
//					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
					continue;
				}

				boolean invioProtocollo= true;
				if (invioProtocollo) {
					invioProtocollo = false;
					// 1. calcolo del codice tipo materiale del bid da utilizzare nell'inserimento
					AreaDatiPassaggioGetIdSbnVO areaDatiPassGetIdSbn = new AreaDatiPassaggioGetIdSbnVO();
					// Identificativo del record:  contiene id record dalla Base Dati di partenza; essendo un nuovo inserimento in Polo
					// sara valorizzato con la routine di calcolo nuovo ID Sbn
					if (tipoMateriale.equals("")) {
						areaDatiPassGetIdSbn.setTipoMat(null);
					} else {
						areaDatiPassGetIdSbn.setTipoMat(tipoMateriale);
					}
					if (tipoRecord.equals("")) {
						areaDatiPassGetIdSbn.setTipoRec(null);
					} else {
						areaDatiPassGetIdSbn.setTipoRec(tipoRecord);
					}

					areaDatiPassGetIdSbn = gestioneAllAuthority.getIdSbn(areaDatiPassGetIdSbn);
					if (!isFilled(areaDatiPassGetIdSbn.getIdSbn())) {
						areaDatiPass.setCodErr(areaDatiPassGetIdSbn.getCodErr());
						testoLog = setTestoLog(idConvertito + "-" + areaDatiPassGetIdSbn.getTestoProtocollo());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					} else {
						bidDaAssegnare = areaDatiPassGetIdSbn.getIdSbn();
					}

					guidaDoc = new GuidaDoc();
					if (isFilled(tipoRecord)) {
						guidaDoc.setTipoRecord(TipoRecord.valueOf(tipoRecord));
					}


					// nel caso di Data assente viene impostata comunque il TipoData altrimenti il protocollo va in errore
					if (c100 == null) {
						c100 = new C100();
						c100.setA_100_8("f"); // Tipo data
						// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
						// sia data1 che data2
						c100.setA_100_9("1831");
						//almaviva5_20150123
						c100.setA_100_13(Integer.toString(currentYear - 1) );
					}


					documentoType = new DocumentoType();

					char naturaChar = natura.charAt(0);
					switch (naturaChar) {
					case 'A':
						// Titolo Uniforme A
						// Area titolo

						a230 = ricostruisciA230(tracciatoRecord.getDati());
						if (a230.getA_230() == null) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ATTENZIONE: Manca la definizione dell'etichetta 230"
									+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
									+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
									+ " area dati:" + tracciatoRecord.getDati());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						if (!a230.getA_230().contains("*")) {
							a230.setA_230("*" + a230.getA_230());
						}

						//Dalla versione sbnmarc 2.03 per i titoli uniformi NON musicali viene gestita l'etichetta 231 al posto della 230
						C231 c231 = ricostruisciC231(tracciatoRecord.getDati());
						if (c231.getA_231() == null) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ATTENZIONE: Manca la definizione dell'etichetta 230"
									+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
									+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
									+ " area dati:" + tracciatoRecord.getDati());
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						if (!c231.getA_231().contains("*")) {
							c231.setA_231("*" + c231.getA_231());
						}

						ElementAutType elementAutType = new ElementAutType();
						DatiElementoType datiElementoType = null;
						//datiElementoType = new TitoloUniformeType();



						// Modifica Dicembre 2014 inserimento dei tag 928 e 929 per estrazione delle informazioni sulla composizione per creare
						// i titoli Uniformi Musicali
						// Ricerca per verificare se sono presenti i tag 928/929 per creare un titolo Uniforme Musicale
						// agganciando i dati della composizione

						if (areaDatiPass.getListaTracciatoRecordArray().get(jpiuUno).getTag().equals("928")) {
							TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElementoType;
							titoloUniformeMusicaType = new TitoloUniformeMusicaType();
							titoloUniformeMusicaType.setT001(bidDaAssegnare);
							titoloUniformeMusicaType.setT005(null);
							titoloUniformeMusicaType.setLivelloAut(SbnLivello.valueOf("05"));
							titoloUniformeMusicaType.setCondiviso(DatiElementoTypeCondivisoType.N);

							if (c101 != null)
								titoloUniformeMusicaType.setT101(c101);
							if (a230 != null)
								titoloUniformeMusicaType.setT230(a230);
							if (a300 != null)
								titoloUniformeMusicaType.setT300(a300);
							titoloUniformeMusicaType.setTipoAuthority(SbnAuthority.UM);
							String dati928 = areaDatiPass.getListaTracciatoRecordArray().get(jpiuUno).getDati();
							a928 = ricostruisci928(dati928);

							if (areaDatiPass.getListaTracciatoRecordArray().get(jpiuDue).getTag().equals("929")) {
								String dati929 = areaDatiPass.getListaTracciatoRecordArray().get(jpiuDue).getDati();
								a929 = ricostruisci929(dati929);
							}

							titoloUniformeMusicaType.setT928(a928);
							titoloUniformeMusicaType.setT929(a929);
							titoloUniformeMusicaType.setLivelloAut(SbnLivello.valueOf("05"));
							elementAutType.setDatiElementoAut(titoloUniformeMusicaType);
						} else {
							TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElementoType;
							titoloUniformeType = new TitoloUniformeType();
							titoloUniformeType.setT001(bidDaAssegnare);
							titoloUniformeType.setT005(null);
							titoloUniformeType.setLivelloAut(SbnLivello.valueOf("05"));
							titoloUniformeType.setCondiviso(DatiElementoTypeCondivisoType.N);
							if (c101 != null)
								titoloUniformeType.setT101(c101);
							if (a230 != null)
								titoloUniformeType.setT230(a230);
							if (a300 != null)
								titoloUniformeType.setT300(a300);
							if (c231 != null)
								titoloUniformeType.setT231(c231);
							titoloUniformeType.setTipoAuthority(SbnAuthority.TU);
							titoloUniformeType.setLivelloAut(SbnLivello.valueOf("05"));
							elementAutType.setDatiElementoAut(titoloUniformeType);
						}


						creaTypeChoice.setElementoAut(elementAutType);
						creaTypeChoice.setDocumento(null);
						break;

					case 'D':
					case 'P':
						titAccessoType = new TitAccessoType();
						titAccessoTypeChoice = new TitAccessoTypeChoice();
						titAccessoType.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(natura));
						titAccessoType.setT001(bidDaAssegnare);
						titAccessoType.setT005(null);
						titAccessoType.setLivelloAut(SbnLivello.valueOf("05"));

						c200 = ricostruisciC200(tracciatoRecord.getDati());
						if (c200.getA_200Count() == 0) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog("ATTENZIONE: Manca la definizione dell'etichetta 200"
									+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
									+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdInput(), tracciatoRecord.getDati())
									+ " area dati:" + tracciatoRecord.getDati());
							areaDatiPass.addListaSegnalazioni(testoLog);
						}

						if (natura.equals("D")) {
							titAccessoTypeChoice.setT517(c200);
						} else if (natura.equals("P")) {
							titAccessoTypeChoice.setT510(c200);
						}

						titAccessoType.setTitAccessoTypeChoice(titAccessoTypeChoice);
						titAccessoType.setCondiviso(TitAccessoTypeCondivisoType.N);
						documentoTypeChoice.setDatiTitAccesso(titAccessoType);
						documentoTypeChoice.setDatiDocumento(null);
						documentoType.setDocumentoTypeChoice(documentoTypeChoice);
						creaTypeChoice.setDocumento(documentoType);
						creaTypeChoice.setElementoAut(null);
						break;
					}

					SbnRequestType sbnrequest = new SbnRequestType();
					SbnMessageType sbnmessage = new SbnMessageType();

					creaType.setCreaTypeChoice(creaTypeChoice);
					sbnrequest.setCrea(creaType);
					sbnmessage.setSbnRequest(sbnrequest);
					sbnRisposta = new SBNMarc();
					// 2. invio dei dati al protocollo
					// 2.a inserimento in locale;
					try {
						this.polo.setMessage(sbnmessage, this.user);
						sbnRisposta = this.polo.eseguiRichiestaServer();
					} catch (SbnMarcException ve) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("ERRORE validazione - Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + ve.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + e.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					if (sbnRisposta == null) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " RISPOSTA DA PROTOCOLLO = null");
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();
					if (!esitoProtocollo.equals("0000") && !esitoProtocollo.equals("3004")) {
						areaDatiPass.setCodErr("9999");
						String msg = "Identificativo Base:" + tracciatoRecord.getIdInput()
							+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo)
							+ " " + areaDatiPass.getTestoProtocollo()
							+ " Codice Errore:"
							+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
						testoLog = setTestoLog(msg);
						log.error(msg);
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					areaDatiPass.setCodErr("0000");
//					testoLog = setTestoLog("Inserito ID: " + bidDaAssegnare + " corrispondente a UNIMARC: " + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo));
//					areaDatiPass.addListaSegnalazioni(testoLog);

					// Intervento Interno in sede di import RML 23.11.2012 (corretta la gestione del contatore)
					areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
					insertImportIdLink(notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo), bidDaAssegnare, areaDatiPass.getNrRichiesta());
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " +  bidDaAssegnare + "-" + notEmptyOrOther(tracciatoRecord.getIdLink(), areaAppoggioTitolo) + ";");
//					areaDatiPass.addListaSegnalazioni(testoLog);

					// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
					// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
					// l'oggetto bibliografico e consentire al catalogatore di correggerlo
					if (presenzaCaratteriSpec) {
						testoLog = setTestoLog("ATTENZIONE l'oggetto appena inserito con identificativo " + bidDaAssegnare
														+ " contiene il carattere ^; Ripristinare il carattere corretto");
						areaDatiPass.addListaSegnalazioni(testoLog);
						presenzaCaratteriSpec = false;
					}
					// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

				}
			}
		}

		// Intervento settembre 2015 - si inserisce un contatore per i record non trattati su richiesta di 
		areaDatiPass.setContNonAmmessiUnimarc(contNonAmmessiUnimarc);
		areaDatiPass.setContAmmessiUnimarcNonGestiti(contAmmessiUnimarcNonGestiti);
		return areaDatiPass;
	}

	public AreaDatiImportSuPoloVO trattamentoSogClaCollegati6xx(AreaDatiImportSuPoloVO areaDatiPass) { // (etichette UNIMARC SOggetti e Classi)

		String idNew = "";
		String idConvertito = "";
		int tagNumeric;
		String bidAssegnato = "";

		// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
		// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
		// l'oggetto bibliografico e consentire al catalogatore di correggerlo
		boolean presenzaCaratteriSpec = false;
		// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"


		String esitoProtocollo = "0000";
		String testoLog = "";

		SbnSoggettiDAO sbnSoggettiDAO = new SbnSoggettiDAO(indice, polo, user);
		SbnClassiDAO sbnClassiDAO = new SbnClassiDAO(indice, polo, user);

		String soggettarioImportato = "";
		String soggUnimarc = "FI";

		String codSistemaClassificazione = "D";
		String codSistemaClasse = "";
		String codEdizioneDewey = "";
		String descrizioneClasse = "";
		String ulterioreTermineClasse = "";
		String simboloClasse = "";

		String livelloSogCla = "05";
		String tipoSoggetto = "1";
		String noteSogCla = "";



		SBNMarc sbnRisposta;
		sbnRisposta = new SBNMarc();

		if (isFilled(areaDatiPass.getListaTracciatoRecordArray()) ) {

			for (TracciatoDatiImport1ParzialeVO tracciatoRecord : areaDatiPass.getListaTracciatoRecordArray() ) {

				areaDatiPass.setCodErr("0000");

				idConvertito = String.valueOf(tracciatoRecord.getIdInput());

				if (!strIsNumeric(tracciatoRecord.getTag())) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE il valore del campo tag contiene caratteri non numerici: " + tracciatoRecord.getTag());
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}
				tagNumeric = Integer.parseInt(tracciatoRecord.getTag());

				if (!isFilled(tracciatoRecord.getDati()) ) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog(idConvertito + "-" + "ATTENZIONE valore del campo dati vuoto o null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (tracciatoRecord.getDati().contains("^")) {
					presenzaCaratteriSpec = true;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

				idNew = selectImportIdLink(areaDatiPass.getNrRichiesta(),
						notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getTitoloFormattato()));
				if (idNew.startsWith("Errore")) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
							+ " " + idNew);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				} else if (!idNew.equals("")) {
					// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
					// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//					testoLog = setTestoLog("IdUnimarc: " + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()) +
//							" trattato in precedenza; Id:" + idNew);
//					areaDatiPass.addListaSegnalazioni(testoLog);
					areaDatiPass.setContOldInsert(areaDatiPass.getContOldInsert() + 1);
					continue;
				}

				String newTestoSoggetto = "";
				String newTestoClasse = "";

				switch (tagNumeric) {
				case 600:
				case 601:
				case 602:
				case 604:
				case 605:
				case 606:
				case 607:
				case 610:
					soggettarioImportato = trimOrEmpty(tagliaEtichetta(tracciatoRecord.getDati(), '2')).toUpperCase();
					if (isFilled(soggettarioImportato) ) {
						try {
							soggUnimarc = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_SOGGETTARIO, soggettarioImportato);
						} catch (Exception e) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog(idConvertito + "- ERRORE - decodifica Soggettario: " + soggettarioImportato +  "(" + e.getMessage() +")");
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
						//almaviva5_20190618 fix codice soggettario
						if (!isFilled(soggUnimarc) && !soggettarioImportato.equals(Soggetti.SOGGETTARIO_FIRENZE)) {
							areaDatiPass.setCodErr("9999");
							testoLog = setTestoLog(idConvertito + "- ERRORE - Parametro codice soggettario errato: " + soggettarioImportato );
							areaDatiPass.addListaSegnalazioni(testoLog);
							continue;
						}
					}

					soggUnimarc = coalesce(soggUnimarc, Soggetti.SOGGETTARIO_FIRENZE);

					SbnEdizioneSoggettario edizione = null;//SbnEdizioneSoggettario.I;

					if (ValidazioneDati.equals(soggUnimarc, Soggetti.SOGGETTARIO_FIRENZE)) {
						//almaviva5_20181019 trattamento edizione soggettario
						String edizioneUnimarc = tagliaEtichetta(tracciatoRecord.getDati(), '9');
						if (isFilled(edizioneUnimarc) ) {
							try {
								edizione = SbnEdizioneSoggettario.valueOf(edizioneUnimarc);
							} catch (Exception e) {
								areaDatiPass.setCodErr("9999");
								testoLog = setTestoLog(idConvertito + "- ERRORE - Parametro edizione soggettario errato: " + edizioneUnimarc );
								areaDatiPass.addListaSegnalazioni(testoLog);
								continue;
							}
						}
					}

					newTestoSoggetto = tracciatoRecord.getDati();
					int newGenericFine = tracciatoRecord.getDati().length();
					newGenericFine = tracciatoRecord.getDati().indexOf("$2");
					if (newGenericFine > 0) {
						newTestoSoggetto = tracciatoRecord.getDati().substring(0, newGenericFine--);
					}
					newGenericFine = newTestoSoggetto.indexOf("$3");
					if (newGenericFine > 0) {
						newTestoSoggetto = tracciatoRecord.getDati().substring(0, newGenericFine--);
					}
					newTestoSoggetto = newTestoSoggetto.replace("$a", "");
					newTestoSoggetto = newTestoSoggetto.replace(" $b", ", ");
					newTestoSoggetto = newTestoSoggetto.replace(" $c", "");
					newTestoSoggetto = newTestoSoggetto.replace(" $x", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace(" $y", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace(" $z", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace(" $j", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace(" $e", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace(" $f", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$b", ", ");
					newTestoSoggetto = newTestoSoggetto.replace("$c", "");
					newTestoSoggetto = newTestoSoggetto.replace("$x", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$y", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$z", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$j", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$e", Soggetti.SEPARATORE_TERMINI_SOGGETTO);
					newTestoSoggetto = newTestoSoggetto.replace("$f", Soggetti.SEPARATORE_TERMINI_SOGGETTO);

					try {
						sbnRisposta = sbnSoggettiDAO.creaSoggetto(soggUnimarc, newTestoSoggetto, livelloSogCla,
								tipoSoggetto, noteSogCla, true, "", false, edizione);
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + e.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}
					break;

				case 675:
				case 676:
					codEdizioneDewey = tagliaEtichetta(tracciatoRecord.getDati(), 'v');
					if (!isFilled(codEdizioneDewey) ) {
						codEdizioneDewey = "21";
					}
					descrizioneClasse = tagliaEtichetta(tracciatoRecord.getDati(), 'c');
					simboloClasse = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
					newTestoClasse = codEdizioneDewey + descrizioneClasse + simboloClasse;

					try {
						sbnRisposta = sbnClassiDAO.creaClasse(codSistemaClassificazione, codEdizioneDewey,
								descrizioneClasse, livelloSogCla, ulterioreTermineClasse, simboloClasse, false, true, false);
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + e.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					break;

				case 686:
					codSistemaClasse = tagliaEtichetta(tracciatoRecord.getDati(), '2');
					descrizioneClasse = tagliaEtichetta(tracciatoRecord.getDati(), 'c');
					simboloClasse = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
					newTestoClasse = codEdizioneDewey + descrizioneClasse + simboloClasse;

					try {
						sbnRisposta = sbnClassiDAO.creaClasse(codSistemaClasse, "  ",
								descrizioneClasse, livelloSogCla, ulterioreTermineClasse, simboloClasse, false, true, false);
					} catch (Exception e) {
						areaDatiPass.setCodErr("9999");
						testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
									+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
									+ " ERROR >>" + e.getMessage());
						areaDatiPass.addListaSegnalazioni(testoLog);
						continue;
					}

					break;

				 default:
					testoLog = setTestoLog("ATTENZIONE: E' stato individuato un tag inaspettato: " + tracciatoRecord.getTag()
							+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
							+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati()));
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				if (sbnRisposta == null) {
					areaDatiPass.setCodErr("9999");
					testoLog = setTestoLog("Identificativo Base:" + tracciatoRecord.getIdInput()
								+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
								+ " RISPOSTA DA PROTOCOLLO = null");
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				esitoProtocollo = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito();

				if (!esitoProtocollo.equals("0000") && !esitoProtocollo.equals("3004") && !esitoProtocollo.equals("3344")) {
					areaDatiPass.setCodErr("9999");
					String msg = "Identificativo Base:" + tracciatoRecord.getIdInput()
						+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), newTestoSoggetto+newTestoClasse)
						+ " " + areaDatiPass.getTestoProtocollo()
						+ " Codice Errore:"
						+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();
					testoLog = setTestoLog(msg);
					log.error(msg);
					areaDatiPass.addListaSegnalazioni(testoLog);
					continue;
				}

				if (esitoProtocollo.equals("0000") || esitoProtocollo.equals("3004")) {
					bidAssegnato = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
											.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001();
				}
				if (esitoProtocollo.equals("3344")) {
					String msg = "Identificativo Base:" + tracciatoRecord.getIdInput()
							+ " Identificativo Oggetto legato:" + notEmptyOrOther(tracciatoRecord.getIdLink(), newTestoSoggetto+newTestoClasse)
							+ " " + areaDatiPass.getTestoProtocollo()
							+ " Codice Errore:"
							+ sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito()
							+ " Viene utilizzato questo Soggetto";
					testoLog = setTestoLog(msg);
					log.error(msg);
					areaDatiPass.addListaSegnalazioni(testoLog);
					bidAssegnato = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice()
											.getSbnOutput().getElementoAut(0).getDatiElementoAut().getT001();
				}
				areaDatiPass.setCodErr("0000");
				areaDatiPass.setContInseritiOK(areaDatiPass.getContInseritiOK() + 1);
				insertImportIdLink(notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getTitoloFormattato()),
						bidAssegnato, areaDatiPass.getNrRichiesta());
				// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
				// Togliere tutte le segnalazioni SALVO quelle di errore !!!!!
//				testoLog = setTestoLog("Inserito nuovo record su tabella import_tb_link con coppia: " +
//						bidAssegnato + "-" + notEmptyOrOther(tracciatoRecord.getIdLink(), newTestoSoggetto+newTestoClasse) + ";");
//				areaDatiPass.addListaSegnalazioni(testoLog);

				// Inizio Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^" che sostituisce i
				// caratteri non UTF8 eventualmente presenti; si invia un messaggio così da rendere identificabile
				// l'oggetto bibliografico e consentire al catalogatore di correggerlo
				if (presenzaCaratteriSpec) {
					testoLog = setTestoLog("ATTENZIONE l'oggetto appena inserito con identificativo " + bidAssegnato
													+ " contiene il carattere ^; Ripristinare il carattere corretto");
					areaDatiPass.addListaSegnalazioni(testoLog);
					presenzaCaratteriSpec = false;
				}
				// Fine Modifica almaviva2 15.05.2012 per gestire la sequenza di caratteri "^"

			}
		}

		return areaDatiPass;
	}

	private C200 ricostruisciC200 (String dati) {

		// almaviva2 23.05.2012 Modifica nel caso delle etichette 210 dove il $v identifica il progressivo di sequenza
		// nel legame a cui il titolo è riferito
		if (dati.contains("$v")) {
			dati = dati.substring(0, dati.indexOf("$v"));
		}

		C200 c200 = new C200();
		String areaAppoggio1 = "";


		// Inizio Modifica almaviva2 15.06.2012 gestione della sequenza " : " nell'area del titolo proprio, prima dell'asterisco
		// Luglio 2013 gestione della sequenza " :" al termine della $a perchè verrà inserito dopo dal protocollo
		int posPreAst = 0;
		String areaPreAst = "";
		String areaPostAst = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); // titolo proprio
		areaAppoggio1 = trattaCaratteriSpeciali(areaAppoggio1);
		if (isFilled(areaAppoggio1)) {
			posPreAst = areaAppoggio1.indexOf("*");
			if (posPreAst > 0) {
				areaPreAst = areaAppoggio1.substring(0, posPreAst);
				areaPostAst = areaAppoggio1.substring(posPreAst, areaAppoggio1.length());
				areaPreAst = areaPreAst.replaceAll(" : ", ": ");
				//almaviva5_20151116
				//areaPostAst = areaPostAst.replaceAll(" :", "");
				areaPostAst = StringUtils.removeEnd(areaPostAst, " :");	//elimina separatore solo se a fine stringa
				areaAppoggio1 = areaPreAst + areaPostAst;
			}
			c200.addA_200(areaAppoggio1);
		}

		// modifica Novembre 2014: il trattamento dell $b perchè mai utilizzata
//		areaAppoggio1 = tagliaEtichetta(dati, 'b'); //designazione generica di materiale
//		if (isFilled(areaAppoggio1)) {
//			c200.addB_200(areaAppoggio1);
//		}

//		areaAppoggio1 = tagliaEtichetta(tracciatoRecord.getDati(), 'c'); //titolo proprio di un altro autore (dove va messo ??)
//		if (isFilled(areaAppoggio1)) {
//			c200.addC_200(areaAppoggio1);
//		}

		areaAppoggio1 = tagliaEtichetta(dati, 'd'); //titolo parallelo
		if (isFilled(areaAppoggio1)) {
			c200.addD_200(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'e'); //complemento del titolo
		areaAppoggio1 = trattaCaratteriSpeciali(areaAppoggio1);
		areaAppoggio1 = areaAppoggio1.replaceAll(" /", "");
		if (isFilled(areaAppoggio1)) {
			c200.addE_200(areaAppoggio1);
		}

		// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
		// etichetta 200 $f eliminato il carattere " ;" che viene inserito poi dal protocollo altrimenti duplicazione
		areaAppoggio1 = tagliaEtichetta(dati, 'f'); //prima indicazione di responsabilita
		if (isFilled(areaAppoggio1)) {
			areaAppoggio1 = areaAppoggio1.replaceAll(" ;", "");
			c200.addF_200(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'g'); //altre indicazioni di responsabilita
		if (isFilled(areaAppoggio1)) {
			c200.addG_200(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'h'); //numero della parte
		if (isFilled(areaAppoggio1)) {
			c200.addH_200(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'i'); //nome della parte
		if (isFilled(areaAppoggio1)) {
			// Intervento del 30 luglio 2013 - trattamento caratteri nosorting anche nell'area i della 200
			// Intervento del 02 settembre 2013 - sostituito trattamento asterisco (da "*" a "\\*")
			areaAppoggio1 = trattaCaratteriSpeciali(areaAppoggio1);
			areaAppoggio1 = areaAppoggio1.replaceAll("\\*", "");
			c200.addI_200(areaAppoggio1);
		}

		c200.setId1(Indicatore.VALUE_2);

		return c200;
	}

	private C205 ricostruisciC205 (String dati) {

		C205 c205 = new C205();
		String areaAppoggio1 = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); //edizione
		if (isFilled(areaAppoggio1)) {
			c205.setA_205(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'b'); //ulteriore edizione dell'edizione
		if (isFilled(areaAppoggio1)) {
			c205.addB_205(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'f'); //casa editrice (indicazione di responsabilita)
		if (isFilled(areaAppoggio1)) {
			c205.addF_205(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'g'); //altra casa editrice
		if (isFilled(areaAppoggio1)) {
			c205.addG_205(areaAppoggio1);
		}
		return c205;
	}

	private C210 ricostruisciC210 (String dati) {
		// esempio: $aTorino $cUtet $d1998 che dovrebbe diventare " - Torino : Utet, 1998"

		// almaviva2 23.05.2012 Modifica nel caso delle etichette 210 dove il $v identifica il progressivo di sequenza
		// nel legame a cui il titolo è riferito
		if (dati.contains("$v")) {
			dati = dati.substring(0, dati.indexOf("$v"));
		}

		C210 c210 = new C210();
		String areaAppoggio1 = "";
		String areaAppoggio2 = "";
		int genericSize = 0;
		int genericInizio =0;

		areaAppoggio2 = dati;
		genericSize = dati.length();
		for (int a = 0; a < genericSize; a++) {
			if (areaAppoggio2.charAt(a) == '$' && areaAppoggio2.charAt(a+1) == 'a') {
				genericInizio = a;
				break;
			}
		}
		// il trattamento viene fatto sostituendo i caratteri preceduti da $ con le apposite parole chiave
		areaAppoggio1 = areaAppoggio2.substring(genericInizio, genericSize); //luogo della pubblicazione


		// Luglio 2013: Import:correzione etichietta 210; se esiste già la punteggiatura (es : o ,) viene eliminata così
		// da essere correttamente reinserita dal protocollo
		areaAppoggio1 = areaAppoggio1.replace(" :$", "$");
		areaAppoggio1 = areaAppoggio1.replace(",$d", "$d");


		// Inizio maggio 2015; almaviva2 mail Giliberto: errore nella costruzione dei dati dell'etichetta 210:
		// se sono presenti due sottocampi 'a' genera ' - ' invece deve essere ' ; '

//		if (areaAppoggio1.contains("$a")) {
//			areaAppoggio1 = areaAppoggio1.replace("$a", " - ");
//		}

		// interventi giugno per Discoteca di Stato richiesti da  almaviva; almaviva2
		// etichetta 210 $a Il vecchio intervento fatto a Maggio per correggere il caso in cui fossero presenti due aree
		// con $a deve essere modificato perchè il comando "replaceFirst" utilizzato per sostituire la prima $a non effettua
		// modifiche in quanto il carattere $ lo inbisce viene quindi preceduto dal carattere "\\"
		// es. areaAppoggio1 = areaAppoggio1.replaceFirst("$a", " - "); diventa areaAppoggio1 = areaAppoggio1.replaceFirst("\\$a", " - ")
		if (areaAppoggio1.contains("$a")) {
			areaAppoggio1 = areaAppoggio1.replaceFirst("\\$a", " - ");
		}
		if (areaAppoggio1.contains("$a")) {
			areaAppoggio1 = areaAppoggio1.replace("$a", " ; ");
		}
		// Fine maggio 2015; almaviva2

		if (areaAppoggio1.contains("$c")) {
			areaAppoggio1 = areaAppoggio1.replace("$c", " : ");
		}
		if (areaAppoggio1.contains("$d")) {
			areaAppoggio1 = areaAppoggio1.replace("$d", ", ");
		}


		// Inizio maggio 2015; almaviva2 mail Giliberto; va gestito la $e nella 210 che va sostituito con ':'.
		// Il trattamento della E va ricontrollato per inserire correttamente le parentesi tonde di chiusura
		if (areaAppoggio1.contains("$e")) {
			areaAppoggio1 = areaAppoggio1.replace("$e", " (");
		}
		if (areaAppoggio1.contains("$f")) {
			areaAppoggio1 = areaAppoggio1.replace("$f", " ");
		}
		// Fine Maggio 2015: almaviva2 mail di della Gilibero; va gestito la $e nella 210 che va sostituito con ':'.


		if (areaAppoggio1.contains("$g")) {
			areaAppoggio1 = areaAppoggio1.replace("$g", " : ");
		}
		if (areaAppoggio1.contains("$h")) {
			if (areaAppoggio1.contains("$e")) {
				areaAppoggio1 = areaAppoggio1.replace("$h", ", ");
			} else {
				areaAppoggio1 = areaAppoggio1.replace("$h", " (");
			}
		}

		if (areaAppoggio1.contains("$e") || areaAppoggio1.contains("$h")) {
			areaAppoggio1 = areaAppoggio1 + ")";
		}

		areaAppoggio1 = areaAppoggio1.replace("", "");

		if (isFilled(areaAppoggio1)) {
			if (areaAppoggio1.length() > 3 && areaAppoggio1.substring(0,3).equals(" - ")) {
				areaAppoggio1 = areaAppoggio1.substring(3, areaAppoggio1.length());
			}
			Ac_210Type ac_210Type = new Ac_210Type();
			ac_210Type.setA_210(new String[] { areaAppoggio1 });
			c210.addAc_210(ac_210Type);
		}
		return c210;
	}


	// almaviva2 Febbraio 2016 - Intervento interno - Nuovo metodo utilizzato solo nella ricostruzione delle collane (Trattamento 4XX)
	// in questo caso il trattamento dell'etichetta 210 deve trattare solo la $a $c e tralasciare gli altri
	private C210 ricostruisciC210PerCollane (String dati) {
		// esempio: $aTorino $cUtet $d1998 che dovrebbe diventare " - Torino : Utet"
		String risultato = "";
		// almaviva2 23.05.2012 Modifica nel caso delle etichette 210 dove il $v identifica il progressivo di sequenza
		// nel legame a cui il titolo è riferito
		if (dati.contains("$v")) {
			dati = dati.substring(0, dati.indexOf("$v"));
		}

		C210 c210 = new C210();
		String areaAppoggio1 = "";
		String areaAppoggio2 = "";
		int genericSize = 0;
		int genericInizio =0;

		areaAppoggio2 = dati;
		genericSize = dati.length();
		for (int a = 0; a < genericSize; a++) {
			if (areaAppoggio2.charAt(a) == '$' && areaAppoggio2.charAt(a+1) == 'a') {
				genericInizio = a;
				break;
			}
		}

		areaAppoggio1 = areaAppoggio2.substring(genericInizio, genericSize); //luogo della pubblicazione

		String areaAppoggio = "";
		areaAppoggio = tagliaEtichetta(areaAppoggio1, 'a');
		if (isFilled(areaAppoggio)) {
			risultato = areaAppoggio;
		}

		areaAppoggio = tagliaEtichetta(areaAppoggio1, 'c');
		if (isFilled(areaAppoggio)) {
			risultato = risultato + " : " + areaAppoggio;
		}

		if (isFilled(risultato)) {
			Ac_210Type ac_210Type = new Ac_210Type();
			ac_210Type.setA_210(new String[] { risultato });
			c210.addAc_210(ac_210Type);
		}
		return c210;
	}

	private String estraiDataPublicazione (String dati) {

		// esempio: $aTorino $cUtet $d1998 in questa fase ci interessa tracciare solo la $d per la data ove mancante
		String areaAppoggio = "";
		areaAppoggio = tagliaEtichetta(dati, 'd');
		if (isFilled(areaAppoggio)) {
			int size = areaAppoggio.length();
			String risultato = "";

			for (int a = 0; a < size; a++) {
				if (areaAppoggio.charAt(a) == '1' || areaAppoggio.charAt(a) == '2') {
					risultato = risultato + areaAppoggio.substring(a, a+4);
					break;
				}
			}
			if (risultato.matches(PeriodiciConstants.REGEX_FORMATO_ANNO)) {
				return risultato;
			}
		}
		return "1831";
	}

	// modifica Febbraio 2015: nel caso di natura N se tipodata è impostato ad F devono essere impostati tutti i campi
	// relativi alla data stessa, altrimenti si elimina anche il tipo data;
	private C100 ricostruisciC100 (String dati, String tipoRecord, String natura) {

		//dummy c100
		final C100 dummy = new C100();
		if (in(natura, "S", "C")) {
			dummy.setA_100_8("a"); // Tipo data
			dummy.setA_100_9("19..");
			dummy.setA_100_13(null);
		} else {
			dummy.setA_100_8("f"); // Tipo data
			dummy.setA_100_9("1831");
			dummy.setA_100_13(Integer.toString(currentYear - 1));
		}
		//

		C100 c100 = new C100();
		String areaAppoggio1 = "";
		int anno=0;
		String appoAnno1 = "";

		// almaviva2 18-06.2012 cambiati controlli su anno pubblicazione per cui se anno1 non e' valido si
		// imposta tipo data f e si esce.
		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio1)) {
			String tipoData = areaAppoggio1.substring(8,9);

			// inizio almaviva2 - Gennaio 2015: Modifica per correggere il tipo data h da noi non considerato
			// verrà trattato come fosse un tipo data
			if (tipoData.equals("h") || tipoData.equals("H")) {
				tipoData = "d";
			}
			// fine almaviva2 - Gennaio 2015
			if (tipoData.toUpperCase().equals("U")) {
				tipoData = "f";
			}

			//almaviva5_20180705 tipo data 'c' non gestito da Sbn
			if (tipoData.equals("c")) {
				tipoData = "b";	//seriale cessato
			}

			c100.setA_100_8(tipoData); // Tipo data
			appoAnno1 = areaAppoggio1.substring(9,13);

			// Modifica Ottobre 2013 se anno è impostato con 4 cifre 0 viene impostato a vuoto e non riportato;
			// modifica Febbraio 2015: nel caso di natura N se tipodata è impostato ad F devono essere impostati tutti i campi
			// relativi alla data stessa, altrimenti si elimina anche il tipo data;
			if (appoAnno1.equals("0000")) {
				if (natura.equals("N")) {
					c100.setA_100_8(null);
					return c100;
				}
				appoAnno1="";
			}

			if (appoAnno1.matches(PeriodiciConstants.REGEX_FORMATO_ANNO)) {
				c100.setA_100_9(appoAnno1); // Data 1
			} else {
				//c100.setA_100_8("f"); // Tipo data
				//return c100; aaaaaaaaaaaaaaaaaa
				return dummy;
			}


			// almaviva2 17.03.2015: nel caso in cui tipoData=f e Data2= "0000" l'anno2 deve essere uguale a anno-1
			if (!tipoData.toUpperCase().equals("A") && !tipoData.toUpperCase().equals("D")) { // in questo caso la data2 deve essere vuota
				if (!areaAppoggio1.substring(13,17).trim().equals("")) {   // Data 2
					try {
						if (tipoData.toUpperCase().equals("F") && areaAppoggio1.substring(13,17).equals("0000")) {
							// tipoData=f e Data2= "0000"
							anno = currentYear - 1;
						} else  {
							anno = Integer.parseInt(areaAppoggio1.substring(13,17));
						}
						// anno = Integer.parseInt(areaAppoggio1.substring(13,17));
					} catch (NumberFormatException nfe) {
						//c100.setA_100_8("f");
						//return c100;
						return dummy;
					}

					// almaviva2 17.03.2015: riportare nel campo setA_100_13 il valore di anno e non dell'areaAppoggio
					if (anno != 0) {
						//c100.setA_100_13(areaAppoggio1.substring(13,17));
						c100.setA_100_13(Integer.toString(anno));
					} else {
						// almaviva2 - Evolutiva per modifiche gestione tipo data F dove devono essere valorizzate OBBLIGATORIAMENTE
						// sia data1 che data2 - se mancanti si desumono dall'area di pubblicazione
						// almaviva2 - Febbraio 2015 Il secondo anno pubblicazione deve essere uguale maggiore del primo anno
						// pubblicazione; su indicazione di  almaviva si forza l'anno corrente.
						if (tipoData.toUpperCase().equals("F")) {
							//c100.setA_100_13(appoAnno1);
							c100.setA_100_13(String.valueOf(currentYear));
						}
					}
				}
			}

			if (tipoRecord.equals("e") || tipoRecord.equals("f")) {
				c100.setA_100_20(checkFiller(areaAppoggio1.substring(20,21), null)); // Pubblicazione governativa
			}
		} else
			return dummy;

		return c100;
	}

	private C101 ricostruisciC101 (String dati) {
		C101  c101= new C101();
		String areaAppoggioProgres = "";
		int fineStringa = 0;
		int inizioDati = 0;
		int fineDati = dati.length();
		String areaAppoggio1 = "";

		for (int scorrimento = inizioDati; scorrimento < fineDati; scorrimento++) {
			areaAppoggioProgres = tagliaEtichettaMolteplice(dati, 'a', scorrimento, fineDati);

			// Ottobre 2018 - almaviva2 viene inserito il controllo dopo la ricerca di una etichetta Unimarc con Molteplicità
			// affinche alla risposta di non trovato si blocchi la ricerca immediatamente e non continui a "spulciare" la stringa
			// dati carattere x carattere fino alla fine !!
			if (length(areaAppoggioProgres) == 0) {
				break;
			}

			for (int i = 0; i < areaAppoggioProgres.length(); i++) {
				if (areaAppoggioProgres.substring(i, i+1).equals("|")
						&& areaAppoggioProgres.substring(i+1,i+2).equals("|")
						&& areaAppoggioProgres.substring(i+2, i+3).equals("|")) {
					fineStringa = Integer.parseInt(areaAppoggioProgres.substring(0, i));
					areaAppoggio1 = areaAppoggioProgres.substring(i+3, areaAppoggioProgres.length()).trim();
					if (areaAppoggio1.length() == 3 || areaAppoggio1.length() > 3 ) {

						// Intervento del 29 luglio 2013 - controllo su tabella codice sull'esistenza lingua pubblicazione
						// inserito inoltre controllo su presenza di piu di 3 lingue (NON AMMESSE)
						String linguaPubb = areaAppoggio1.substring(0, 3).toUpperCase();
						try {
							linguaPubb = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_LINGUA, linguaPubb);
							} catch (Exception e) {
								linguaPubb="UND";
							}
							if (strIsNull(linguaPubb)) {
								linguaPubb="UND";
							}
							c101.addA_101(linguaPubb);
							if (c101.getA_101Count() == 3) {
								return c101;
							}
//						c101.addA_101(areaAppoggio1.substring(0, 3).toUpperCase());
					}
					scorrimento = scorrimento + fineStringa - 1;
					break;
				}
			}
		}
		return c101;
	}

	private C116 ricostruisciC116 (String dati) {
		C116 c116 = new C116();
		String areaAppoggio1 = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		c116.setA_116_0(areaAppoggio1.substring(0,1)); // Designazione materiale

		// Supporto primario Modifica Luglio 2013: campo obbligatorio; viene impostato a "u" (unknown) se vuoto;
		if (areaAppoggio1.substring(1,2).equals(" ")) {
			c116.setA_116_1("u"); // Supporto primario
		} else {
			c116.setA_116_1(areaAppoggio1.substring(1,2)); // Supporto primario
		}

		c116.setA_116_3(areaAppoggio1.substring(3,4)); // Indicatore di colore


		// Luglio 2013 il metodo di verifica sulla presenza dei dati relativi all'etichetta 116 (grafica) deve verificare
		// cke gli indicatori di tecnica siano valorizzati oltre che presenti (da notEmpty a isFilled);
		// Indicatore di tecnica (disegni)
		int elem = 0;
		if (isFilled(areaAppoggio1.substring(4,6)))
			elem++;
		if (isFilled(areaAppoggio1.substring(6,8)))
			elem++;
		if (isFilled(areaAppoggio1.substring(8,10)))
			elem++;

		if (elem > 0) {
			String[] arrIndicTecnica = new String[elem];
			if (isFilled(areaAppoggio1.substring(4,6)))
				arrIndicTecnica[0] = areaAppoggio1.substring(4,6);
			if (isFilled(areaAppoggio1.substring(6,8)))
				arrIndicTecnica[1] = areaAppoggio1.substring(6,8);
			if (isFilled(areaAppoggio1.substring(8,10)))
				arrIndicTecnica[2] = areaAppoggio1.substring(8,10);
			c116.setA_116_4(arrIndicTecnica);
		}

		// Indicatore di tecnica (stampe)
		elem = 0;
		if (isFilled(areaAppoggio1.substring(10,12)))
			elem++;
		if (isFilled(areaAppoggio1.substring(12,14)))
			elem++;
		if (isFilled(areaAppoggio1.substring(14,16)))
			elem++;
		if (elem > 0) {
			String[] arrIndicTecnicaSt = new String[elem];
			if (isFilled(areaAppoggio1.substring(10,12)))
				arrIndicTecnicaSt[0] = areaAppoggio1.substring(10,12);
			if (isFilled(areaAppoggio1.substring(12,14)))
				arrIndicTecnicaSt[1] = areaAppoggio1.substring(12,14);
			if (isFilled(areaAppoggio1.substring(14,16)))
				arrIndicTecnicaSt[2] = areaAppoggio1.substring(14,16);
			c116.setA_116_10(arrIndicTecnicaSt);
		}

		c116.setA_116_16(areaAppoggio1.substring(16,18)); // Designazione funzione
		return c116;
	}

	private C120 ricostruisciC120(String dati) {
		C120 c120 = new C120();
		String areaAppoggio1 = "";
		DataField tag = MarcUtil.string2field("120", dati);
		areaAppoggio1 = tag.getSubfield('a').getData();

		// Indicatore di colore
		if (isFilled(areaAppoggio1.substring(0, 1))) {
			c120.setA_120_0(areaAppoggio1.substring(0, 1));
		} else {
			c120.setA_120_0(IID_SPAZIO);
		}

		// Meridiano d'origine
		if (isFilled(areaAppoggio1.substring(9, 13))) {
			// Ottobre 2018: i caratteri validi, accettati da SbnMarc, sono solo i primi due
			//c120.setA_120_9(areaAppoggio1.substring(9, 13));
			c120.setA_120_9(areaAppoggio1.substring(9, 11));
		} else {
			c120.setA_120_9(IID_SPAZIO);
		}
		return c120;
	}


	private C121 ricostruisciC121 (String dati) {
		C121 c121 = new C121();
		String areaAppoggio1 = "";

		// almaviva2 Ottobre 2018 - inserito sostituzione del primo carattere della stringa che potrebbe arrivare
		// a spazio e sarebbe elimina dalla routine tagliaEtichetta tanto il carttere 1 (Physical dimension) non viene utilizzato;
		if (dati.startsWith("$a ")) {
			dati = dati.replace("$a ", "$ax");
		}
		DataField tag = MarcUtil.string2field("121", dati.replace('|', ' '));

		areaAppoggio1 = tag.getSubfield('a').getData();

		// Supporto fisico
		if (isFilled(areaAppoggio1.substring(3,5))) {
			c121.setA_121_3(areaAppoggio1.substring(3,5));
		}

		// Tecnica creazione
		if (isFilled(areaAppoggio1.substring(5,6))) {
			c121.setA_121_5(areaAppoggio1.substring(5,6));
		}

		// Forma riproduzione
		if (isFilled(areaAppoggio1.substring(6,7))) {
			c121.setA_121_6(areaAppoggio1.substring(6,7));
		}

		// Forma pubblicazione
		if (isFilled(areaAppoggio1.substring(8,9))) {
			c121.setA_121_8(areaAppoggio1.substring(8,9));
		}

		areaAppoggio1 = tag.getSubfield('b').getData();
		// Altitudine
		// almaviva2 Ottobre 2018 - inserito controllo su presenza della $b perchè se è assente
		// si incorre nell'errore java.lang.StringIndexOutOfBoundsException: String index out of range: 1
		if (isFilled(areaAppoggio1)) {
			c121.setB_121_0(areaAppoggio1.substring(0,1));
		}

		return c121;
	}

	private C123 ricostruisciC123 (String dati, Character ind1) {
		C123 c123 = new C123();
		String areaAppoggio1 = "";

		// Indicatore tipo scala
		c123.setId1(Indicatore.valueOf(String.valueOf(ind1)));

		// Tipo scala
		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio1)) {
			c123.setA_123(areaAppoggio1);
		}

		// Scala orizzontale
		areaAppoggio1 = tagliaEtichetta(dati, 'b');
		if (isFilled(areaAppoggio1)) {
			c123.setB_123(areaAppoggio1);
		}

		// Scala verticale
		areaAppoggio1 = tagliaEtichetta(dati, 'c');
		if (isFilled(areaAppoggio1)) {
			c123.setC_123(areaAppoggio1);
		}

		// Coordinate ovest
		areaAppoggio1 = tagliaEtichetta(dati, 'd');
		if (isFilled(areaAppoggio1)) {
			c123.setD_123(areaAppoggio1);
		}

		// Coordinate est
		areaAppoggio1 = tagliaEtichetta(dati, 'e');
		if (isFilled(areaAppoggio1)) {
			c123.setE_123(areaAppoggio1);
		}

		// Coordinate nord
		areaAppoggio1 = tagliaEtichetta(dati, 'f');
		if (isFilled(areaAppoggio1)) {
			c123.setF_123(areaAppoggio1);
		}

		// Coordinate sud
		areaAppoggio1 = tagliaEtichetta(dati, 'g');
		if (isFilled(areaAppoggio1)) {
			c123.setG_123(areaAppoggio1);
		}

		return c123;
	}

	private C124 ricostruisciC124 (String dati) {

		C124 c124 = new C124();
		String areaAppoggio1 = "";

		// Carattere immagine
		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio1)) {
			c124.setA_124(areaAppoggio1);
		}

		// Forma
		areaAppoggio1 = tagliaEtichetta(dati, 'b');
		if (isFilled(areaAppoggio1)) {
			c124.setB_124(areaAppoggio1);
		}

		// Piattaforma
		areaAppoggio1 = tagliaEtichetta(dati, 'd');
		if (isFilled(areaAppoggio1)) {
			c124.setD_124(areaAppoggio1);
		}

		// Categoria satellite
		areaAppoggio1 = tagliaEtichetta(dati, 'e');
		if (isFilled(areaAppoggio1)) {
			c124.setE_124(areaAppoggio1);
		}
		return c124;
	}

	private C125 ricostruisciC125 (String dati) {
		C125 c125 = new C125();
		String areaAppoggio1 = "";

		// Codice presentazione
		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio1.substring(0,1))) {
			// c125.setA_125_0(areaAppoggio1.substring(0,1));
			// Gennaio 2015 : il codice presentazione NA deve essere decodificato con x minuscala (vedi Tab. Presentazione)
			if (areaAppoggio1.equals("NA")) {
				areaAppoggio1 = "x";
			}
			c125.setA_125_0(areaAppoggio1);
		}

		// Tipo di testo letterario
		areaAppoggio1 = tagliaEtichetta(dati, 'b');
		if (isFilled(areaAppoggio1)) {
			c125.setB_125(areaAppoggio1);
		}
		return c125;
	}
	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private C126 ricostruisciC126 (String dati) {

		C126 c126 = new C126();
		String areaAppoggioA = "";
		String areaAppoggioB = "";
		String areaAppoggio1 = "";
		String areaAppoggio2 = "";
		String areaAppoggio3 = "";
		String areaAppoggio4 = "";
		String areaAppoggio5 = "";
		String areaAppoggio6 = "";

		areaAppoggioA = tagliaEtichettaSenzaTrim(dati, 'a'); // Sound Recording Coded data
		if (isFilled(areaAppoggioA)) {
			// la $a è composta da 14 caratteri così da valorizzare tutti i sottocampi
			c126.setA_126_0(areaAppoggioA.substring(0, 1)); // FormaPubblicazioneDisco
			c126.setA_126_1(areaAppoggioA.substring(1, 2)); // Velocita
			c126.setA_126_2(areaAppoggioA.substring(2, 3)); // TipoSuono
			c126.setA_126_3(areaAppoggioA.substring(3, 4)); // LarghezzaScanal
			c126.setA_126_4(areaAppoggioA.substring(4, 5)); // Dimensioni

			if (areaAppoggioA.substring(5, 6).equals(" ")) {                // LarghezzaNastro
				c126.setA_126_5(null);
			} else {
				c126.setA_126_5(areaAppoggioA.substring(5, 6));
			}

			if (areaAppoggioA.substring(6, 7).equals(" ")) {                // ConfigurazNastro
				c126.setA_126_6(null);
			} else {
				c126.setA_126_6(areaAppoggioA.substring(6, 7));
			}


			 // Gestione dei campi MaterTestualeAccompagn (sono 6 sottocampi)
			areaAppoggio1 = areaAppoggioA.substring(7, 8);
			areaAppoggio2 = areaAppoggioA.substring(8, 9);
			areaAppoggio3 = areaAppoggioA.substring(9, 10);
			areaAppoggio4 = areaAppoggioA.substring(10, 11);
			areaAppoggio5 = areaAppoggioA.substring(11, 12);
			areaAppoggio6 = areaAppoggioA.substring(12, 13);
			int elem = 0;
			if (isFilled(areaAppoggio1))
				elem++;
			if (isFilled(areaAppoggio2))
				elem++;
			if (isFilled(areaAppoggio3))
				elem++;
			if (isFilled(areaAppoggio4))
				elem++;
			if (isFilled(areaAppoggio5))
				elem++;
			if (isFilled(areaAppoggio6))
				elem++;

			String[] arrMaterAccompagn = new String[elem]; // MaterTestualeAccompagn sono 6 sottocampi
			if (isFilled(areaAppoggio1))
				arrMaterAccompagn[0] = (areaAppoggio1);
			if (isFilled(areaAppoggio2))
				arrMaterAccompagn[1] = (areaAppoggio2);
			if (isFilled(areaAppoggio3))
				arrMaterAccompagn[2] = (areaAppoggio3);
			if (isFilled(areaAppoggio4))
				arrMaterAccompagn[3] = (areaAppoggio4);
			if (isFilled(areaAppoggio5))
				arrMaterAccompagn[4] = (areaAppoggio5);
			if (isFilled(areaAppoggio6))
				arrMaterAccompagn[5] = (areaAppoggio6);
			c126.setA_126_7(arrMaterAccompagn);

			c126.setA_126_13(areaAppoggioA.substring(13, 14)); // TecnicaRegistraz
			c126.setA_126_14(areaAppoggioA.substring(14, 15)); // SpecCarattRiprod

		}

		areaAppoggioB = tagliaEtichettaSenzaTrim(dati, 'b'); // Sound Recording Coded data
		if (isFilled(areaAppoggioB)) {
			// la $b è composta da 3 caratteri così da valorizzare tutti i sottocampi
			if (areaAppoggioB.length()> 0) {
				c126.setB_126_0(areaAppoggioB.substring(0, 1)); // DatiCodifRegistrazSonore
			} else  {
				c126.setB_126_0(" ");
			}

			if (areaAppoggioB.length()> 1) {
				c126.setB_126_1(areaAppoggioB.substring(1, 2)); // TipoDiMateriale
			} else  {
				c126.setB_126_1(" ");
			}
			if (areaAppoggioB.length()> 2) {
				c126.setB_126_2(areaAppoggioB.substring(2, 3)); // TipoDiTaglio
			} else {
				c126.setB_126_2(" ");
			}
		}

		return c126;
	}

	private C127 ricostruisciC127 (String dati) {
		C127 c127 = new C127();
		String areaAppoggio = tagliaEtichetta(dati, 'a'); // Durata Registrazione
		// Il formato dovrebbe essere : $a003100$a021839 cioè 00h31m00s e 02h18m 39s
		// Nei record di Discoteca di stato abbiamo trovato: "$a15 h., 52 min. ca." oppure "$a119 min., 35 sec."
		//almaviva5_20141030 evolutive area zero
		if (isFilled(areaAppoggio)) {
			c127.setA_127_a(SBNMarcUtil.parseTag127(areaAppoggio));
		}
		return c127;
	}
	// FINE almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

	private C128 ricostruisciC128 (String dati) {
		C128 c128 = new C128();
		String areaAppoggio1 = "";
		//almaviva5_20150528 corrette corrispondenze tag <--> sottocampo
		areaAppoggio1 = tagliaEtichetta(dati, 'b'); // Organico sintetico
		if (isFilled(areaAppoggio1)) {
			c128.setB_128(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, 'c'); // Organico analitico
		if (isFilled(areaAppoggio1)) {
			c128.setC_128(areaAppoggio1);
		}

		areaAppoggio1 = tagliaEtichetta(dati, '9'); // Tipo elaborazione
		if (isFilled(areaAppoggio1)) {
			c128.setD_128(areaAppoggio1);
		}

		return c128;
	}
	private C140 ricostruisciC140 (String dati) {
		C140 c140 = new C140();
		String areaAppoggio1 = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a');
		String codGenere = "";


		if (areaAppoggio1.length() > 17) {
			if (isFilled(areaAppoggio1.substring(9,11))) {
				codGenere = areaAppoggio1.substring(9,11).toUpperCase();
				try {
					codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
				} catch (Exception e) {
					codGenere = "";
				}
				if (isFilled(codGenere) ) {
					c140.addA_140_9(codGenere);
				}
			}
			if (isFilled(areaAppoggio1.substring(11,13))) {
				codGenere = areaAppoggio1.substring(11,13).toUpperCase();
				try {
					codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
				} catch (Exception e) {
					codGenere = "";
				}
				if (isFilled(codGenere) ) {
					c140.addA_140_9(codGenere);
				}
			}
			if (isFilled(areaAppoggio1.substring(13,15))) {
				codGenere = areaAppoggio1.substring(13,15).toUpperCase();
				try {
					codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
				} catch (Exception e) {
					codGenere = "";
				}
				if (isFilled(codGenere) ) {
					c140.addA_140_9(codGenere);
				}
			}
			if (isFilled(areaAppoggio1.substring(15,17))) {
				codGenere = areaAppoggio1.substring(15,17).toUpperCase();
				try {
					codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
				} catch (Exception e) {
					codGenere = "";
				}
				if (isFilled(codGenere) ) {
					c140.addA_140_9(codGenere);
				}
			}

		}
		return c140;
	}

	// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	private C140bis ricostruisciC140bis (String dati) {
		C140bis c140bis = new C140bis();
		String testoLetterarioAntico = "";
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		if (areaAppoggio1.length() >= 17) {
			if (isFilled(areaAppoggio1.substring(16,17))) {
				testoLetterarioAntico = checkFiller(areaAppoggio1.substring(16,17), null);
				try {
					testoLetterarioAntico = CodiciProvider.unimarcToSBN(CodiciType.CODICE_CONTENUTO_LETTERARIO_ANTICO, testoLetterarioAntico);
				} catch (Exception e) {
					testoLetterarioAntico = "";
				}
			}
		}
		if (!isFilled(testoLetterarioAntico))
			return null;
	
		c140bis.setA_140_17(testoLetterarioAntico);
		return c140bis;
	}

	private C206 ricostruisciC206 (String dati) {
		C206 c206 = new C206();
		String areaAppoggio1 = "";
		String areaAppoggio2 = " - ";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); //indicazione dati matematici

		if (areaAppoggio1.contains("$a")) {
			areaAppoggio1 = areaAppoggio1.replace("$a", " - ");
		}
		if (isFilled(areaAppoggio1)) {
			c206.setA_206(areaAppoggio2 + areaAppoggio1);
		}
		return c206;
	}

	private C207 ricostruisciC207 (String dati) {
		C207 c207 = new C207();
		String areaAppoggio1 = "";
		String areaAppoggio2 = " - ";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); //designazione del numero di volume e delle date
		if (areaAppoggio1.contains("$a")) {
			areaAppoggio1 = areaAppoggio1.replace("$a", " - ");
		}
		if (isFilled(areaAppoggio1)) {
			c207.addA_207(areaAppoggio2 + areaAppoggio1);
		}

		return c207;
	}

	private C208 ricostruisciC208 (String dati) {
		C208 c208 = new C208();
		String areaAppoggio1 = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); //specifica per musica a stampa
		if (isFilled(areaAppoggio1)) {
			c208.setA_208(areaAppoggio1);
		}
		areaAppoggio1 = tagliaEtichetta(dati, 'd'); //titolo parallelo
		if (isFilled(areaAppoggio1)) {
			c208.addD_208(areaAppoggio1);
		}

		return c208;
	}

	private C215 ricostruisciC215 (String dati) {

		// Settembre 2018: Import:correzione etichietta 215; se esiste già la punteggiatura (es ; :  ,) viene eliminata così
		// da essere correttamente reinserita dal protocollo
		if (dati != null && dati.length() > 0){
			dati = dati.replace(";$", "$");
			dati = dati.replace(":$", "$");
			dati = dati.replace(",$", "$");
		}


		// Settembre 2018: Import:correzione etichietta 215; se esiste già la punteggiatura (es ; :  ,) viene eliminata così
		// da essere correttamente reinserita dal protocollo
		if (dati != null && dati.length() > 0){
			dati = dati.replace(";$", "$");
			dati = dati.replace(":$", "$");
			dati = dati.replace(",$", "$");
		}

		C215 c215 = new C215();
		String areaAppoggio1 = "";

		areaAppoggio1 = tagliaEtichetta(dati, 'a'); //designazione specifica del materiale
		if (isFilled(areaAppoggio1)) {
			c215.addA_215(areaAppoggio1);
		}
		areaAppoggio1 = tagliaEtichetta(dati, 'c'); //altre dimensioni fisiche
		if (isFilled(areaAppoggio1)) {
			c215.setC_215(areaAppoggio1);
		}
		areaAppoggio1 = tagliaEtichetta(dati, 'd'); //dimensioni
		if (isFilled(areaAppoggio1)) {
			c215.addD_215(areaAppoggio1);
		}
		areaAppoggio1 = tagliaEtichetta(dati, 'e'); //Materiale allegato
		if (isFilled(areaAppoggio1)) {
			c215.addE_215(areaAppoggio1);
		}
		return c215;
	}

	private A230 ricostruisciA230 (String dati) {
		A230 a230 = new A230();
		String areaAppoggio1 = tagliaEtichetta(dati, 'a'); //tipo di risorsa

		if (isFilled(areaAppoggio1)) {
			// Intervento del 23.01.2014 inserito trattamento nosorting come per il trattamento delle 200
			// nel caso dei documenti anche qui (trattamento delle 230 per altri titoli) Vedi i commenti sotto
			// Intervento del 30 luglio 2013 - trattamento caratteri nosorting anche nell'area i della 200
			// Intervento del 02 settembre 2013 - sostituito trattamento asterisco (da "*" a "\\*")
			areaAppoggio1 = trattaCaratteriSpeciali(areaAppoggio1);
			areaAppoggio1 = areaAppoggio1.replaceAll("\\*", "");

			a230.setA_230(areaAppoggio1);
		}
		return a230;
	}

	//Protocollo 2.03: viene generata una etichetta 231 e non più 230
	private C231 ricostruisciC231(String dati) {
		C231 c231 = new C231();
		String areaAppoggio1 = tagliaEtichetta(dati, 'a'); //tipo di risorsa

		if (isFilled(areaAppoggio1)) {
			areaAppoggio1 = trattaCaratteriSpeciali(areaAppoggio1);
			areaAppoggio1 = areaAppoggio1.replaceAll("\\*", "");

			c231.setA_231(areaAppoggio1);
		}
		return c231;
	}


	// almaviva2 18.06.2012 - correzione del controllo sul genere con accesso sulla opportuna tabella
	private C105 ricostruisciC105(String dati) {
		String codGenere = "";
		String codGenereDecod = "";
		C105 c105 = new C105();
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');

		int start = 4;	//genere pubblicazione (posizioni 4-7)
		String genere1 = areaAppoggio1.substring(start++, start);
		if (!genere1.trim().equals("")) {
			codGenere = genere1.toUpperCase();
			try {
				codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
			} catch (Exception e) {
				codGenere = "";
			}
			codGenereDecod += coalesce(codGenere, "");
		}
		String genere2 = areaAppoggio1.substring(start++, start);
		if (!genere2.trim().equals("")) {
			codGenere = genere2.toUpperCase();
			try {
				codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
			} catch (Exception e) {
				codGenere = "";
			}
			codGenereDecod += coalesce(codGenere, "");
		}
		String genere3 = areaAppoggio1.substring(start++, start);
		if (!genere3.trim().equals("")) {
			codGenere = genere3.toUpperCase();
			try {
				codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
			} catch (Exception e) {
				codGenere = "";
			}
			codGenereDecod += coalesce(codGenere, "");
		}
		String genere4 = areaAppoggio1.substring(start++, start);
		if (!genere4.trim().equals("")) {
			codGenere = genere4.toUpperCase();
			try {
				codGenere = CodiciProvider.unimarcToSBN(CodiciType.CODICE_GENERE_PUBBLICAZIONE, codGenere);
			} catch (Exception e) {
				codGenere = "";
			}
			codGenereDecod += coalesce(codGenere, "");
		}

		if (codGenereDecod.length() == 1) {
			c105.addA_105_4(codGenereDecod.substring(0,1));
		}
		if (codGenereDecod.length() == 2) {
			c105.addA_105_4(codGenereDecod.substring(0,1));
			c105.addA_105_4(codGenereDecod.substring(1,2));
		}
		if (codGenereDecod.length() == 3) {
			c105.addA_105_4(codGenereDecod.substring(0,1));
			c105.addA_105_4(codGenereDecod.substring(1,2));
			c105.addA_105_4(codGenereDecod.substring(2,3));
		}
		if (codGenereDecod.length() == 4) {
			c105.addA_105_4(codGenereDecod.substring(0,1));
			c105.addA_105_4(codGenereDecod.substring(1,2));
			c105.addA_105_4(codGenereDecod.substring(2,3));
			c105.addA_105_4(codGenereDecod.substring(3,4));
		}

		return c105;
	}


// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
// Modifiche generalizzate a tutte le ricostruzioni dei Numeri Standard
    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	private C105bis ricostruisciC105bis(String dati) {
		C105bis c105bis = new C105bis();
		String testoLetterarioModerno = "";
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		int start = 11;	//Codice contenuto letterario (pos. 11)
		if (areaAppoggio1.length() > start) {
			String tipoTesto = areaAppoggio1.substring(start++, start);
			if (isFilled(tipoTesto)) {
				testoLetterarioModerno = checkFiller(tipoTesto, null);
				try {
					testoLetterarioModerno = CodiciProvider.unimarcToSBN(CodiciType.CODICE_CONTENUTO_LETTERARIO_MODERNO, testoLetterarioModerno);
				} catch (Exception e) {
					testoLetterarioModerno = "";
				}
			}
		}
		//almaviva5_20150522
		if (!isFilled(testoLetterarioModerno))
			return null;

		c105bis.setA_105_11(testoLetterarioModerno);
		return c105bis;
	}

	private C181 ricostruisciC181 (String dati) {
		String areaAppoggio="";
		C181 c181 = new C181();
		
		// ISBD Content form Code
		DataField tag = MarcUtil.string2field("181", dati);
		areaAppoggio = tag.getSubfield('a').getData();
		//areaAppoggio = tagliaEtichetta(dati, 'a');
		c181.setA_181_0(checkFiller(String.valueOf(areaAppoggio.charAt(0)), null)); // forma Contenuto

		//ISBD Content Qualification Code
		areaAppoggio = tag.getSubfield('b').getData();
		c181.setB_181_0(checkFiller(String.valueOf(areaAppoggio.charAt(0)), null)); // tipo Contenuto
		c181.setB_181_1(checkFiller(String.valueOf(areaAppoggio.charAt(1)), null)); // movimento
		c181.setB_181_2(checkFiller(String.valueOf(areaAppoggio.charAt(2)), null)); // dimensione
		c181.setB_181_3(checkFiller(String.valueOf(areaAppoggio.charAt(3)), null)); // sensorialita1
		c181.setB_181_4(checkFiller(String.valueOf(areaAppoggio.charAt(4)), null)); // sensorialita2
		c181.setB_181_5(checkFiller(String.valueOf(areaAppoggio.charAt(5)), null)); // sensorialita3

		return c181;
	}

	private C182 ricostruisciC182 (String dati) {
		C182 c182 = new C182();
		String areaAppoggio = "";

		// ISBD Media Type Code
		areaAppoggio = tagliaEtichetta(dati, 'a');
		c182.setA_182_0(checkFiller(areaAppoggio, null)); // tipo mediazione1

		return c182;
	}

	private C181 imposta181Area0Default (String tipoRecord) {

		C181 c181 = new C181();
		if (in(tipoRecord, "a", "b")) {
			c181.setA_181_0("i");
		} else if (in(tipoRecord, "c", "d", "j")) {
			c181.setA_181_0("d");
		} else if (in(tipoRecord, "e", "f", "g", "k")) {
			c181.setA_181_0("b");
		} else if (in(tipoRecord, "i")) {
			c181.setA_181_0("h");
		} else if (in(tipoRecord, "l", "m")) {
			c181.setA_181_0("m");
		} else if (in(tipoRecord, "r")) {
			c181.setA_181_0("e");
		}

		if (in(tipoRecord, "c", "d")) {
			c181.setB_181_0("a");
		} else if (in(tipoRecord, "e", "f")) {
			c181.setB_181_0("c");
		} else if (tipoRecord.equals("j")) {
			c181.setB_181_0("b");
		}

		if (in(tipoRecord, "e", "f", "k")) {
			c181.setB_181_1("b");
		} else if (in(tipoRecord, "g")) {
			c181.setB_181_1("a");
		}

		if (in(tipoRecord, "e", "f", "g", "k")) {
			c181.setB_181_2("2");
		} else if (in(tipoRecord, "r")) {
			// Agosto 2015: telefonico con Scognamiglio: per tipo record "r" il campo c181.setB_181_2 rimane vuoto
			// c181.setB_181_2("3");
		}

		if (in(tipoRecord, "a", "b", "c", "d", "e", "f", "g", "k", "l", "m", "r")) {
			c181.setB_181_3("e");
		} else if (in(tipoRecord, "i", "j")) {
			c181.setB_181_3("a");
		}
		return c181;
	}

	private C182 imposta182Area0Default (String tipoRecord) {
		// Modifica del 19.05.2015 almaviva2 (adeguamento all'Indice) per accettare il valore n del tipoMediazione
		C182 c182 = new C182();
		if (in(tipoRecord, "a", "b", "c", "d", "e", "f", "k", "r")) {
			//c182.setA_182_0("y");
			c182.setA_182_0("n");
		} else if (in(tipoRecord, "g")) {
			c182.setA_182_0("g");
		}  else if (in(tipoRecord, "i", "j")) {
			c182.setA_182_0("a");
		} else if (in(tipoRecord, "l")) {
			// MODIFICA almaviva2 SETTEMBRE tiporec=l forma contenuto=m tipo mediazione=m
			// c182.setA_182_0("b");
			c182.setA_182_0("m");
		} else if (in(tipoRecord, "m")) {
			c182.setA_182_0("m");
		}
		return c182;
	}

	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
	private C183 imposta183Area0Default (String tipoMediazione) {
		// Modifica del 19.05.2015 almaviva2 (adeguamento all'Indice) per accettare il valore n del tipoMediazione
		C183 c183 = new C183();
		if (in(tipoMediazione, "a")) {
			c183.setA_183_0("sz");
		} else if (in(tipoMediazione, "g")) {
			c183.setA_183_0("vz");
		} else if (in(tipoMediazione, "m", "n")) {
			c183.setA_183_0("nz");
		}
		return c183;
	}

	// FINE almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	private C115 ricostruisciC115 (String dati) {
		// Dati codificati: MATERIALE AUDIOVISIVO () con tipo record g
		C115 c115 = new C115();
		String areaAppoggio = "";

		String areaAppoggio1 = "";
		String areaAppoggio2 = "";
		String areaAppoggio3 = "";
		String areaAppoggio4 = "";

		areaAppoggio = tagliaEtichettaSenzaTrim(dati, 'a');
		// la $a è composta da 20 caratteri così da valorizzare tutti i sottocampi

		// ATTENZIONE: VALORE IMPOSTATO AD 'a' SOLO NEL CASO DI CAMPO VUOTO: IL CAMPO E' OBBLIGATORIO QUINDI
		// LA VALORIZZAZIONE E' UNA FORZATURA PER COPRIRE I DATI INCOMPLETI

		if (!areaAppoggio.substring(0, 1).equals(" "))  {
			c115.setA_115_0(areaAppoggio.substring(0, 1)); // TipoVideo
		} else {
			c115.setA_115_0("b"); // TipoVideo FORZATO !!!!!!!!
		}
//		c115.setA_115_0(areaAppoggio.substring(0, 1)); // TipoVideo

		//almaviva5_20150210
		String tipoVideo = c115.getA_115_0();

		c115.setA_115_1(areaAppoggio.substring(1, 4).trim()); // lunghezza
		c115.setA_115_4(areaAppoggio.substring(4, 5)); // IndicatoreColore
		c115.setA_115_5(areaAppoggio.substring(5, 6)); // IndicatoreSuono


		if (areaAppoggio.substring(6, 7).equals(" ")) {  // SupportoSuono
			c115.setA_115_6(null);
		} else {
			c115.setA_115_6(areaAppoggio.substring(6, 7));
		}


		if (areaAppoggio.substring(7, 8).equals(" ")) {  // LarghezzaDimensioni
			c115.setA_115_7(null);
		} else {
			c115.setA_115_7(areaAppoggio.substring(7,8));
		}

		if (areaAppoggio.substring(8,9).equals(" ")) {  // FormaPubblDistr
			c115.setA_115_8(null);
		} else {
			c115.setA_115_8(areaAppoggio.substring(8,9));
		}

		if (areaAppoggio.substring(9,10).equals(" ")) {  // TecnicaVideoFilm
			c115.setA_115_9(null);
		} else {
			c115.setA_115_9(areaAppoggio.substring(9,10));
		}

		if (areaAppoggio.substring(10,11).equals(" ")) {  // PresentImmagMov
			c115.setA_115_10(null);
		} else {
			c115.setA_115_10(areaAppoggio.substring(10,11));
		}

		 // Gestione dei campi MaterAccompagn (sono 4 sottocampi)
		areaAppoggio1 = areaAppoggio.substring(11, 12);
		areaAppoggio2 = areaAppoggio.substring(12, 13);
		areaAppoggio3 = areaAppoggio.substring(13, 14);
		areaAppoggio4 = areaAppoggio.substring(14, 15);
		int elem = 0;
		if (isFilled(areaAppoggio1))
			elem++;
		if (isFilled(areaAppoggio2))
			elem++;
		if (isFilled(areaAppoggio3))
			elem++;
		if (isFilled(areaAppoggio4))
			elem++;

		String[] arrMaterAccompagn = new String[elem]; // MaterAccompagn sono 4 sottocampi
		if (isFilled(areaAppoggio1))
			arrMaterAccompagn[0] = (areaAppoggio1);
		if (isFilled(areaAppoggio2))
			arrMaterAccompagn[1] = (areaAppoggio2);
		if (isFilled(areaAppoggio3))
			arrMaterAccompagn[2] = (areaAppoggio3);
		if (isFilled(areaAppoggio4))
			arrMaterAccompagn[3] = (areaAppoggio4);
		c115.setA_115_11(arrMaterAccompagn);

		if (areaAppoggio.substring(15, 16).equals(" ")) {  // PubblicVideoreg
			c115.setA_115_15(null);
		} else {
			c115.setA_115_15(areaAppoggio.substring(15, 16));
		}
		if (areaAppoggio.substring(16, 17).equals(" ")) {  // PresentVideoreg
			c115.setA_115_16(null);
		} else {
			c115.setA_115_16(areaAppoggio.substring(16, 17));
		}
		if (areaAppoggio.substring(17, 18).equals(" ")) {  // MaterialeEmulsBase
			c115.setA_115_17(null);
		} else {
			c115.setA_115_17(areaAppoggio.substring(17, 18));
		}
		if (areaAppoggio.substring(18, 19).equals(" ")) {  // MaterialeSupportSec
			c115.setA_115_18(null);
		} else {
			c115.setA_115_18(areaAppoggio.substring(18, 19));
		}


		// ATTENZIONE: VALORE IMPOSTATO AD ' ' SOLO NEL CASO DI CAMPO INESISTENTE: IL CAMPO DEVE ESSERE PRESENTE ANCHE SE A SPAZIO
		// QUINDI LA VALORIZZAZIONE E' UNA FORZATURA PER COPRIRE I DATI INCOMPLETI

		if (areaAppoggio.length() > 19)  {
			c115.setA_115_19(areaAppoggio.substring(19, 20)); // StandardTrasmiss
		} else {
			c115.setA_115_19(null); // StandardTrasmiss
		}
//		c115.setA_115_19(areaAppoggio.substring(19, 20)); // StandardTrasmiss

		//almaviva5_20150210 il sottocampo $b è ammesso solo per tipoVideo == a (film)
		if (ValidazioneDati.equals(tipoVideo, "a")) {
			areaAppoggio = tagliaEtichettaSenzaTrim(dati, 'b');
			// la $a è composta da 15 caratteri così da valorizzare tutti i sottocampi
			if (areaAppoggio.substring(0, 1).equals(" ")) { // VersioneAudiovid
				c115.setB_115_0(null);
			} else {
				c115.setB_115_0(areaAppoggio.substring(0, 1));
			}
			if (areaAppoggio.substring(1, 2).equals(" ")) { // ElementiProd
				c115.setB_115_1(null);
			} else {
				c115.setB_115_1(areaAppoggio.substring(1, 2));
			}
			if (areaAppoggio.substring(2, 3).equals(" ")) { // SpecCatColoreFilm
				c115.setB_115_2(null);
			} else {
				c115.setB_115_2(areaAppoggio.substring(2, 3));
			}
			if (areaAppoggio.substring(3, 4).equals(" ")) { // EmulsionePellic
				c115.setB_115_3(null);
			} else {
				c115.setB_115_3(areaAppoggio.substring(3, 4));
			}
			if (areaAppoggio.substring(4, 5).equals(" ")) { // ComposPellic
				c115.setB_115_4(null);
			} else {
				c115.setB_115_4(areaAppoggio.substring(4, 5));
			}
			if (areaAppoggio.substring(5, 6).equals(" ")) { // SuonoImmagMovimento
				c115.setB_115_5(null);
			} else {
				c115.setB_115_5(areaAppoggio.substring(5, 6));
			}
			if (areaAppoggio.substring(6, 7).equals(" ")) { // TipoPellicStampa
				c115.setB_115_6(null);
			} else {
				c115.setB_115_6(areaAppoggio.substring(6, 7));
			}
		}

		// Campi al momento non gestiti
//		areaAppoggio.substring(7, 1);  // Deterioration stage
//		areaAppoggio.substring(8, 1);  // Completeness
//		areaAppoggio.substring(9, 14); // Film inspection date

		return c115;
	}

	// FINE almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

// Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
// Modifiche generalizzate a tutte le ricostruzioni dei Numeri Standard

	// Modifica 29.11.2012 almaviva2 - nuovo trattamento tipoSTD che viene decodificato in Base al valueOf del tag stesso
	// e non più assegnato in base ai valori predefiniti.
	// Inoltre nelle chiamate non viene effettuata la set ma la add per inserire tutti i numeri standard trovati
	// e non solo l'ultimo
	private NumStdType[] ricostruisci010 (String dati) {
		NumStdType[] numISBN = new NumStdType[1];

		String areaAppoggio1 = eliminaCarattere(tagliaEtichetta(dati, 'a'), '-');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');


		// Intervento interno almaviva2 12.03.2015 controllo su lunghezza anche per uguaglianza a 13 caratteri non solo a 10
		numISBN = new NumStdType[1];
		if (isFilled(areaAppoggio1)) {
			if (areaAppoggio1.length() == 10 || areaAppoggio1.length() == 13) {
				numISBN[0] = new NumStdType();
				numISBN[0].setNumeroSTD(areaAppoggio1);
			} else {
				return null;
			}
		}
		if (isFilled(areaAppoggio2)) {
			areaAppoggio2 = trunc(areaAppoggio2 ,30);
			numISBN[0].setNotaSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				if (areaAppoggio2.length() > 22) {
					areaAppoggio2 = areaAppoggio2.substring(0,22);
				}

				numISBN[0].setNotaSTD(areaAppoggio2 + "(errato)");
			}
		}
		if (numISBN[0] != null) {
			numISBN[0].setTipoSTD("010");  // controllare valori numeriStandard per ISBN "I"
		}
		return numISBN;
	}

	private NumStdType[] ricostruisci011 (String dati) {
		NumStdType[] numISSN = new NumStdType[1];
		String areaAppoggio1 = eliminaCarattere(tagliaEtichetta(dati, 'a'), '-');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');

		// Modifica del 21.01.2014 riportato lo steso controllo che si usa nel ricostruisci010
		if (isFilled(areaAppoggio1)) {
			if (areaAppoggio1.length() == 10) {
				numISSN[0] = new NumStdType();
				numISSN[0].setNumeroSTD(areaAppoggio1);
			} else {
				return null;
			}
		}
		if (isFilled(areaAppoggio2)) {
			numISSN[0].setNotaSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				numISSN[0].setNotaSTD(areaAppoggio2 + "(errato)");
			}
		}
		if (numISSN[0] != null) {
			numISSN[0].setTipoSTD("011");  // controllare valori numeriStandard per ISBN "I"
		}

		return numISSN;
	}

	private C012[] ricostruisci012 (String dati) {

		C012[] c012 = null;
		c012 = new C012[1];
		c012[0] = new C012();
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		String areaAppoggio2 = tagliaEtichetta(dati, '9');

		if (isFilled(areaAppoggio1)) {

			// Intervento 01 agosto 2013 - la lunghezza dell'area deve essere maggiore di 32 altrimenti risulterà invalida
			if (areaAppoggio1.length() != 32) {
				return null;
			}

			// Intervento 29 luglio 2013 - eliminazione caratteri accentati (immissione tastiera) da impronta  con caratteri base
			areaAppoggio1 = areaAppoggio1.replace("\u00E0", "a")
				.replace("\u00E8", "e")
				.replace("\u00E9", "e")
				.replace("\u00EC", "i")
				.replace("\u00ED", "i")
				.replace("\u00F2", "o")
				.replace("\u00F3", "o")
				.replace("\u00F9", "u")
				.replace("\u00FA", "u");

			c012[0].setA_012_1(areaAppoggio1.substring(0,10));
			c012[0].setA_012_2(areaAppoggio1.substring(10,24));
			if (areaAppoggio1.substring(24).length() != 8) {
				// Intervento 01 agosto 2013 - la lunghezza della III parte deve essere pari a 8 altrimenti risulterà invalida
				return null;
			} else {
				c012[0].setA_012_3(areaAppoggio1.substring(24));
			}
		}
		if (isFilled(areaAppoggio2)) {
			c012[0].setNota(areaAppoggio2);
		}

		return c012;
	}

	private NumStdType[] ricostruisci013 (String dati) {
		NumStdType[] numISMN = new NumStdType[1];
		String areaAppoggio1 = eliminaCarattere(tagliaEtichetta(dati, 'a'), '-');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');


		if (isFilled(areaAppoggio1)) {
			numISMN[0] = new NumStdType();
			numISMN[0].setNumeroSTD(areaAppoggio1);
		}
		if (isFilled(areaAppoggio2)) {
			numISMN[0].setNotaSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				numISMN[0].setNotaSTD(areaAppoggio2 + "(errato)");
			}

		}
		if (numISMN[0] != null) {
			numISMN[0].setTipoSTD("013");  // FATTO controllare valori numeriStandard per ISSN "M"
		}
		return numISMN;
	}

	private NumStdType ricostruisci017 (String dati) throws Exception {
		NumStdType numALTRI = new NumStdType();
		String areaAppoggio1 = eliminaCarattere(tagliaEtichetta(dati, 'a'), '-');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');
		String areaAppoggio4 = tagliaEtichetta(dati, '2');

		if (isFilled(areaAppoggio1)) {
			numALTRI.setNumeroSTD(areaAppoggio1);
		}
		if (isFilled(areaAppoggio2)) {
			numALTRI.setNotaSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				numALTRI.setNotaSTD(areaAppoggio2 + "(errato)");
			}
		}
		if (isFilled(areaAppoggio4)) {
			if (!CodiciProvider.exists(CodiciType.CODICE_TIPO_NUMERO_STANDARD, areaAppoggio4))
				return null;

			numALTRI.setTipoSTD(areaAppoggio4);  // valori numeriStandard per ALTRI ISSN uguale a $2
		}
		return numALTRI;
	}

	private NumStdType[] ricostruisci020 (String dati) {
		NumStdType[] numBiblNaz = new NumStdType[1];
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');


		if (isFilled(areaAppoggio1)) {
			numBiblNaz[0] = new NumStdType();
			numBiblNaz[0].setPaeseSTD(areaAppoggio1);
		} else {
			return null;
		}
		if (isFilled(areaAppoggio2)) {
			numBiblNaz[0].setNumeroSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				numBiblNaz[0].setNotaSTD(areaAppoggio2 + "(errato)");
			}
		} else {
			return null;
		}
		if (numBiblNaz[0] != null) {
			numBiblNaz[0].setTipoSTD("020");  // FATTO controllare valori numeriStandard per ISSN "B"
		}
		return numBiblNaz;
	}

	private NumStdType[] ricostruisci022 (String dati) {
		NumStdType[] numPubGov = new NumStdType[1];
		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');
		String areaAppoggio3 = tagliaEtichetta(dati, 'z');

		if (isFilled(areaAppoggio1)) {
			numPubGov[0] = new NumStdType();
			numPubGov[0].setPaeseSTD(areaAppoggio1);
		}
		if (isFilled(areaAppoggio2)) {
			numPubGov[0].setNumeroSTD(areaAppoggio2);
			if (isFilled(areaAppoggio3)) {
				numPubGov[0].setNotaSTD(areaAppoggio2 + "(errato)");
			}
		}
		if (numPubGov[0] != null) {
			numPubGov[0].setTipoSTD("022");  // controllare valori numeriStandard per ISSN "G"
		}
		return numPubGov;
	}

	private NumStdType[] ricostruisci071 (String tipoRecord, String dati, char indicatore1, char indicatore2) {

		// Modifica Luglio 2013 con decodifica codici tag 071 fornita com mail da Carla Scognamiglio
		// .indicatore 20 corrisponde al codice L della tabella NSTD
		// .indicatore 40 corrisponde al codice E della stessa NSTD
		// Modifica Ottobre 2013 con decodifica codici tag 071
		// .indicatore solo di un carattere ( 2 e 4 invece di 20 e 40)

		// almaviva2 Evolutiva Ottobre 2014 - EVOLUTIVA SU IMPORT PER SCHEMA 2.0 (AREA0 e NUOVI MATERIALI Audiovisivo/Discosonoro)
		// .indicatore 00 corrisponde al codice ? della tabella NSTD corrisponde a Sound recording: Issue Number senza nota
		// .indicatore 01 corrisponde al codice ? della tabella NSTD corrisponde a Sound recording: Issue Number con nota

		NumStdType[] numLastra = new NumStdType[1];

		String areaAppoggio1 = tagliaEtichetta(dati, 'a');
		String areaAppoggio2 = tagliaEtichetta(dati, 'b');

		areaAppoggio2 = trunc(areaAppoggio2, 30);

		if (isFilled(areaAppoggio1)) {
			if (dati.startsWith("2") || indicatore1 == '2') {
				numLastra[0] = new NumStdType();
				numLastra[0].setNumeroSTD(areaAppoggio1);
				numLastra[0].setTipoSTD("L");
				if (isFilled(areaAppoggio2)) {
					numLastra[0].setNotaSTD(areaAppoggio2);
				}
			} else if (dati.startsWith("4") || indicatore1 == '4') {
				numLastra[0] = new NumStdType();
				numLastra[0].setNumeroSTD(areaAppoggio1);
				//almaviva5_20141124
				if (in(tipoRecord, "i", "j"))
					numLastra[0].setTipoSTD("A");
				else
					if (in(tipoRecord, "g"))
						numLastra[0].setTipoSTD("H");
					else
						numLastra[0].setTipoSTD("E");
				//
				if (isFilled(areaAppoggio2)) {
					numLastra[0].setNotaSTD(areaAppoggio2);
				}
			} else if (dati.startsWith("0") || indicatore1 == '0') {
				numLastra[0] = new NumStdType();
				numLastra[0].setNumeroSTD(areaAppoggio1);
				numLastra[0].setTipoSTD("A");
				if (isFilled(areaAppoggio2)) {
					numLastra[0].setNotaSTD(areaAppoggio2);
				}
			} else if (dati.startsWith("1") || indicatore1 == '1') {
				numLastra[0] = new NumStdType();
				numLastra[0].setNumeroSTD(areaAppoggio1);
				numLastra[0].setTipoSTD("F");
				if (isFilled(areaAppoggio2)) {
					numLastra[0].setNotaSTD(areaAppoggio2);
				}
			}
		}
		return numLastra;
	}

	String tagliaEtichetta(final DataField input, char carattere) {
		if (input == null)
			return "";
		Subfield sf = input.getSubfield(carattere);
		if (sf == null)
			return "";

		return sf.getData();
	}

	private String tagliaEtichetta (final String input, char carattere) {

		try {
			String dati = new String(input);
			int size = dati.length();
			int inizio = 0;
			int fine = 0;

			// RIVISTA E CORRETTA il 23.05.2012
			int posizione = 0;
			posizione = dati.indexOf("$" + carattere);
			if (posizione < 0) {
				return "";
			} else {
				inizio = posizione+2;
				posizione = dati.indexOf("$", inizio);
				if (posizione < 0) {
					fine = size;
				} else {
					fine = posizione;
				}

				dati = dati.substring(inizio, fine);
				if (dati.length() > 0) {
					size = dati.length();
					//if (dati.substring(size-1,size).equals(" ")) {
					if (dati.endsWith(" ")) {
						dati = dati.substring(0, size - 1);
					}
					//if (dati.substring(0,1).equals(" ")) {
					if (dati.startsWith(" ")) {
						dati = dati.substring(1, dati.length());
					}
				}
				return dati;
			}
			// RIVISTA E CORRETTA
		} catch (Exception e) {
			log.error("errore etichetta: " + input, e);
			return "";
		}
	}


	// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
	// Dicembre 2014 per Import di Discoteca di Stato si Importa anche questa nuova etichetta
	// Manutenzione Novembre 2015, almaviva2 - Si limita la lunghezza dei campi dell'etichetta 922 al
	// valore del DB

	private C922 ricostruisciC922 (String dati) {
		C922 c922 = new C922();
		String areaAppoggio="";

		areaAppoggio = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio)) {
			c922.setA_922(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'p');
		if (isFilled(areaAppoggio)) {
			c922.setP_922(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'q');
		if (isFilled(areaAppoggio)) {
			// c922.setQ_922(areaAppoggio);
			c922.setQ_922(trunc(areaAppoggio, 15));
		}
		areaAppoggio = tagliaEtichetta(dati, 'r');
		if (isFilled(areaAppoggio)) {
			// c922.setR_922(areaAppoggio);
			c922.setR_922(trunc(areaAppoggio, 30));
		}
		areaAppoggio = tagliaEtichetta(dati, 's');
		if (isFilled(areaAppoggio)) {
			// c922.setS_922(areaAppoggio);
			 c922.setS_922(trunc(areaAppoggio, 30));
		}
		areaAppoggio = tagliaEtichetta(dati, 't');
		if (isFilled(areaAppoggio)) {
			c922.setT_922(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'u');
		if (isFilled(areaAppoggio)) {
			c922.setU_922(areaAppoggio);
		}

		return c922;
	}

	// almaviva2 Evolutiva Gennaio 2015 per Materiale Musicale e Audiovisivo
	// impostazione dei campi Interpreti
	private C927[] ricostruisciC927(List<String> listaPers, String numRichiesta) {

		// Valori da impostare su UniMarc
		// c927[i].setA_927(PERSONAGGIO); potrebbe essere anche il nome vero del cantente/suonatore ?????
		// c927[i].setB_927(TIMBRO VOCALE); potrebbe esssere anche lo strumento suonato ?????
		// c927[i].setC3_927(INTERPRETE); dovrebbe essere il VID dell'autore/cantante/suonatore

		String personaggioInf;
		String areaAppoggioA = "";
		String areaAppoggioB = "";
		String areaAppoggioC = "";
		String areaAppoggio3 = "";
		C927[] c927 = null;
		int size = size(listaPers);
		c927 = new C927[size];

		String decodStrumento= "";
		String idNew= "";

		for (int i = 0; i < size; i++) {
			if (listaPers.get(i) != null) {
				personaggioInf = listaPers.get(i);
				if (isFilled(personaggioInf)) {
					areaAppoggioA = tagliaEtichetta(personaggioInf, 'a'); // Nome personaggio
					areaAppoggioB = tagliaEtichetta(personaggioInf, 'i'); // Strumento DOVE LO METTIAMO ?????
					// PROVIAMO INIZIO
					decodStrumento ="";;
					try {
						decodStrumento = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_STRUMENTI_VOCI_ORGANICO, areaAppoggioB);

						} catch (Exception e) {
							decodStrumento="abs";
						}
						if (strIsNull(decodStrumento)) {
							decodStrumento="abs";
						}
					// PROVIAMO FINE


					areaAppoggioC = tagliaEtichetta(personaggioInf, 'c'); // contiene il nomeCognome esteso dell'Interprete
					areaAppoggio3 = tagliaEtichetta(personaggioInf, '3'); // VID collegato
					// PROVIAMO INIZIO
					idNew="";
					idNew = selectImportIdLink(numRichiesta, notEmptyOrOther(areaAppoggio3, ""));
					if (idNew.startsWith("Errore") || idNew.equals("") ) {
						idNew = null;
//						areaDatiPass.setCodErr("9999");
//						testoLog = setTestoLog("ERRORE in ricerca su import_id_link"
//								+ " per chiave: n.richiesta=" + areaDatiPass.getNrRichiesta()
//								+ " id unimarc=" + notEmptyOrOther(tracciatoRecord.getIdLink(), tracciatoRecord.getDati())
//								+ " " + idNew);
//						areaDatiPass.addListaSegnalazioni(testoLog);
//						continue;
					}
					// PROVIAMO FINE

					c927[i] = new C927();
					//almaviva5_20150312
					c927[i].setA_927(trunc(areaAppoggioA, 25) );
					c927[i].setB_927(decodStrumento);
					c927[i].setC3_927(idNew);
				}
			}
		}
		return c927;
	}

	private A928 ricostruisci928 (String dati) {
		A928 a928 = new A928();

		String areaAppoggioProgres = "";
		int fineStringa = 0;
		int inizioDati = 0;
		int fineDati = dati.length();
		String areaAppoggio1 = "";

		for (int scorrimento = inizioDati; scorrimento < fineDati; scorrimento++) {
			areaAppoggioProgres = tagliaEtichettaMolteplice(dati, 'a', scorrimento, fineDati);

			// Ottobre 2018 - almaviva2 viene inserito il controllo dopo la ricerca di una etichetta Unimarc con Molteplicità
			// affinche alla risposta di non trovato si blocchi la ricerca immediatamente e non continui a "spulciare" la stringa
			// dati carattere x carattere fino alla fine !!
			if (length(areaAppoggioProgres) == 0) {
				break;
			}

			for (int i = 0; i < areaAppoggioProgres.length(); i++) {
				if (areaAppoggioProgres.substring(i, i+1).equals("|")
						&& areaAppoggioProgres.substring(i+1,i+2).equals("|")
						&& areaAppoggioProgres.substring(i+2, i+3).equals("|")) {
					fineStringa = Integer.parseInt(areaAppoggioProgres.substring(0, i));
					areaAppoggio1 = areaAppoggioProgres.substring(i+3, areaAppoggioProgres.length()).trim();
					if (areaAppoggio1.length() == 3 || areaAppoggio1.length() > 3 ) {

						String formaMus = areaAppoggio1.substring(0, 3).toLowerCase();
						try {
							formaMus = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_FORMA_MUSICALE, formaMus);
							} catch (Exception e) {
								formaMus="";
							}
							if (strIsNull(formaMus)) {
								formaMus="";
							}
							a928.addA_928(formaMus);
							if (a928.getA_928Count() == 3) {
								continue;
							}
					}
					scorrimento = scorrimento + fineStringa - 1;
					break;
				}
			}
		}
		String areaAppoggio = "";
		areaAppoggio = tagliaEtichetta(dati, 'b');
		if (isFilled(areaAppoggio)) {
			a928.setB_928(areaAppoggio);
		}

		areaAppoggio = tagliaEtichetta(dati, 'c');
		if (isFilled(areaAppoggio)) {
			a928.setC_928(areaAppoggio);
		}
		return a928;
	}

	private A929 ricostruisci929 (String dati) {
		A929 a929 = new A929();

		String areaAppoggio = "";
		areaAppoggio = tagliaEtichetta(dati, 'a');
		if (isFilled(areaAppoggio)) {
			a929.setA_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'b');
		if (isFilled(areaAppoggio)) {
			a929.setB_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'c');
		if (isFilled(areaAppoggio)) {
			a929.setC_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'd');
		if (isFilled(areaAppoggio)) {
			a929.setD_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'e');
		if (isFilled(areaAppoggio)) {
			a929.setE_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'f');
		if (isFilled(areaAppoggio)) {
			a929.setF_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'g');
		if (isFilled(areaAppoggio)) {

			if (!areaAppoggio.contains("*")) {
				areaAppoggio = "*" + areaAppoggio;
			}

			a929.setG_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'h');
		if (isFilled(areaAppoggio)) {
			a929.setH_929(areaAppoggio);
		}
		areaAppoggio = tagliaEtichetta(dati, 'i');
		if (isFilled(areaAppoggio)) {
			a929.setI_929(areaAppoggio);
		}
		return a929;
	}



	private String tagliaEtichettaSenzaTrim (final String input, char carattere) {

		try {
			String dati = new String(input);
			int size = dati.length();
			int inizio = 0;
			int fine = 0;
			int posizione = 0;
			posizione = dati.indexOf("$" + carattere);
			if (posizione < 0) {
				return "";
			} else {
				inizio = posizione+2;
				posizione = dati.indexOf("$", inizio);
				if (posizione < 0) {
					fine = size;
				} else {
					fine = posizione;
				}

				dati = dati.substring(inizio, fine);
				if (dati.length() > 0) {
					size = dati.length();
				}
				return dati;
			}
		} catch (Exception e) {
			log.error("errore etichetta: " + input, e);
			return "";
		}

	}

	private String tagliaEtichettaMolteplice (String dati, char carattere, int inizioRicerca, int fineRicerca) {

		dati = dati.substring(inizioRicerca, fineRicerca);

		int fineScorrRicerca = 0;
		int size = dati.length();

		if (!dati.contains("$" + carattere)) {
			return "";
		}

		for (int a = 0; a < size; a++) {
			if (dati.charAt(a) == '$' && dati.charAt(a+1) == carattere) {
				inizioRicerca = a + 2;
				break;
			}
		}
		if (inizioRicerca == 0) {
			return "";
		}

		size--;
		for (int b = inizioRicerca; b < size; b++) {
			if (dati.charAt(b) == '$' && dati.charAt(b+1) == carattere) {
				fineScorrRicerca = b;
				break;
			}
		}
		if (fineScorrRicerca == 0) {
			fineScorrRicerca = dati.length();
		}
		if (fineScorrRicerca <= inizioRicerca)
			return "";

		dati = dati.substring(inizioRicerca, fineScorrRicerca);
		if (dati.substring(0,1).equals(" ")) {
			dati = dati.substring(1,dati.length());
		}
		return String.valueOf(fineScorrRicerca) + "|||" + dati;
	}

	private String tagliaEtichettaEntiConMoltepl (String dati, char carattere, int inizioRicerca, int fineRicerca) {

		// Evolutiva Ottobre 2018 - almaviva2 - per trattare la etichetta $b negli autori di tipo Ente che è ripetibile
		// quando si cerca la fine della stringa la ricerca si deve interrompere al carattere $ e non anche al carattere
		// Questa routine è praticamente identica a tagliaEtichettaMolteplice fatto salvo il controllo evidenziato
		// dal commento
		dati = dati.substring(inizioRicerca, fineRicerca);

		int fineScorrRicerca = 0;
		int size = dati.length();

		if (!dati.contains("$" + carattere)) {
			return "";
		}

		for (int a = 0; a < size; a++) {
			if (dati.charAt(a) == '$' && dati.charAt(a+1) == carattere) {
				inizioRicerca = a + 2;
				break;
			}
		}
		if (inizioRicerca == 0) {
			return "";
		}

		size--;
		for (int b = inizioRicerca; b < size; b++) {
			// Evolutiva Ottobre 2018 - almaviva2 - per trattare la etichetta $b negli autori di tipo Ente che è ripetibile
			// quando si cerca la fine della stringa la ricerca si deve interrompere al carattere $ e non anche al carattere
			// if (dati.charAt(b) == '$' && dati.charAt(b+1) == carattere) {
			if (dati.charAt(b) == '$') {
				fineScorrRicerca = b;
				break;
				}
			}
		if (fineScorrRicerca == 0) {
			fineScorrRicerca = dati.length();
		}
		if (fineScorrRicerca <= inizioRicerca)
			return "";

		dati = dati.substring(inizioRicerca, fineScorrRicerca);
		if (dati.substring(0,1).equals(" ")) {
			dati = dati.substring(1,dati.length());
		}
		return String.valueOf(fineScorrRicerca) + "|||" + dati;
	}

	private String eliminaCarattere (String dati, char carattere) {
		int size = dati.length();
		String risultato="";

		for (int a = 0; a < size; a++) {
			if (dati.charAt(a) != carattere) {
				risultato = risultato + dati.charAt(a);
			}
		}
		return risultato;
	}

	private String trattaCaratteriSpeciali(String dati) {

		dati = Z3950ClientFactory.stripNoSortDelimiters(dati);

		return dati;
	}

	private String notEmptyOrOther(String data1, String data2) {

		if (isFilled(data1)) {
			return data1;
		} else {
			return data2;
		}
	}
	
	private String checkFiller(String value, String def) {
		if (!isFilled(value) || IID_FILLER.equals(value))
			return def;

		return value;
	}

	private AutorePersonaleType valorizzaAutPers(TracciatoDatiImport1ParzialeVO tracciatoRecord, String vidDaAssegnare) {

		AutorePersonaleType autorePersonale = new AutorePersonaleType();

		String areaAppoggioA = "";
		String areaAppoggioB = "";
		String areaAppoggioC = "";
		String areaAppoggioF = "";
		String areaAppoggioNome = "";
		String tipoNome = "";

		autorePersonale.setTipoAuthority(SbnAuthority.AU);

		areaAppoggioA = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
		areaAppoggioA =	rtrim(areaAppoggioA);

		areaAppoggioB = tagliaEtichetta(tracciatoRecord.getDati(), 'b');

		// Inizio almaviva2 08.05.2012 Funzione di Import Autore: Nel caso di tipo autore A non deve esserci la virgola
		// di divisione fra le aree che ovviamente non è coerente con il tipo nome e manda poi il messaggio di errore
		// del protocollo "Nome non congruente"
		// Luglio 2013 Se nell'Import degli autori su presenta una stringa in cui la virgola è presente nella $b questa
		// deve essere eliminata perchè sarà inserita al termine della $a in maniera corretta es($apippo$b, pluto)
		if (isFilled(areaAppoggioB)) {
			areaAppoggioB = areaAppoggioB.replace(", ", "");
			if (!areaAppoggioA.contains(",")) {
				areaAppoggioA = areaAppoggioA + ",";
			}
		}

		// Modifica almaviva2 del 10.05.2012 inserimento del carattere spazione far areaA e AreaB dell'autore
		// necessaria alla correttezza del nome personale.
		// Inizio Modifica Giugno 2015 almaviva2: Lo spazio fra le due componenti del nome $a e $b deve essere inserito
		// solo nel caso in cui la $b sia valorizzata altrimenti si crea una incoerenza nella regole del tipo nome A
		// che non può terminare con lo spazio (SbnGestioneImportSuPoloDao metodo:valorizzaAutPers)
		if (isFilled(areaAppoggioB)) {
			areaAppoggioNome = areaAppoggioA + " " + areaAppoggioB;
		} else {
			areaAppoggioNome = areaAppoggioA;
		}
		//  areaAppoggioNome = areaAppoggioA + " " + areaAppoggioB;
		// Fine Modifica Giugno 2015 almaviva2:

		if (isFilled(areaAppoggioB)) {
			// tipo nome C o D - dipende dal numero di elementi

			// almaviva2 17.03.2015 Modifica per eliminare SOLO IN FASE DI CONTROLLO tutta la parte del nome a partire
			// dalla sequenza " : " per identificare correttamente i tipi nomi
			String nomeTemp = areaAppoggioA;
			if (nomeTemp.indexOf(" : ") >= 0) {
				nomeTemp = nomeTemp.substring(0, nomeTemp.indexOf(" : "));
			}
			//if (areaAppoggioA.contains(" ")) {
			if (nomeTemp.contains(" ")) {
				tipoNome="D";
			} else {
				tipoNome="C";
			}
		} else {
			// Intervento Ottobre 2013: per i casi in cui c'è la virgola dopo il cognome ma il nome NON è valorizzato es "De_Filippis, "
			int lenAppoA = areaAppoggioA.length();
			if (areaAppoggioA.subSequence(lenAppoA-1, lenAppoA).equals(",")) {
				// tipo nome C o D - dipende dal numero di elementi

				// almaviva2 17.03.2015 Modifica per eliminare SOLO IN FASE DI CONTROLLO tutta la parte del nome a partire
				// dalla sequenza " : " per identificare correttamente i tipi nomi
				String nomeTemp = areaAppoggioA;
				if (nomeTemp.indexOf(" : ") >= 0) {
					nomeTemp = nomeTemp.substring(0, nomeTemp.indexOf(" : "));
				}
				// if (areaAppoggioA.contains(" ")) {
				if (nomeTemp.contains(" ")) {
					tipoNome="D";
				} else {
					tipoNome="C";
				}
			} else {
				// tipo nome A o B - dipende dal numero di elementi

				// almaviva2 17.03.2015 Modifica per eliminare SOLO IN FASE DI CONTROLLO tutta la parte del nome a partire
				// dalla sequenza " : " per identificare correttamente i tipi nomi
				String nomeTemp = areaAppoggioA;
				if (nomeTemp.indexOf(" : ") >= 0) {
					nomeTemp = nomeTemp.substring(0, nomeTemp.indexOf(" : "));
				}
				//if (areaAppoggioA.contains(" ")) {
				if (nomeTemp.contains(" ")) {
					tipoNome="B";
				} else {
					tipoNome="A";
				}
			}
		}
		autorePersonale.setTipoNome(SbnTipoNomeAutore.valueOf(tipoNome)); // TIPO NOME

		autorePersonale.setT001(vidDaAssegnare);
		autorePersonale.setLivelloAut(SbnLivello.valueOf("05"));

		// nominativo
		A200 a200 = new A200();
		a200.setId2(Indicatore.valueOf(String.valueOf(tracciatoRecord.getIndicatore2())));
		a200.setA_200(areaAppoggioNome);

		areaAppoggioC = tagliaEtichetta(tracciatoRecord.getDati(), 'c');
		if (isFilled(areaAppoggioC)) {
			a200.addC_200(areaAppoggioC);
		}
		areaAppoggioF = tagliaEtichetta(tracciatoRecord.getDati(), 'f');
		if (isFilled(areaAppoggioF)) {
			a200.setF_200(areaAppoggioF);
		}
		autorePersonale.setT200(a200);
		autorePersonale.setCondiviso(DatiElementoTypeCondivisoType.N);



		return autorePersonale;
	}

	private EnteType valorizzaAutEnte(TracciatoDatiImport1ParzialeVO tracciatoRecord, String vidDaAssegnare) {

		EnteType ente = new EnteType();

		String areaAppoggioA = "";
		String areaAppoggioB = "";
		String areaAppoggioC = "";
		String areaAppoggioD = "";
		String areaAppoggioE = "";
		String areaAppoggioF = "";
		String areaAppoggioInd1 = "";
		String areaAppoggioInd2 = "";

		ente.setTipoAuthority(SbnAuthority.AU);

		// TIPO NOME E INDICATORI
		areaAppoggioInd1 = String.valueOf(tracciatoRecord.getIndicatore1());
		areaAppoggioInd2 = String.valueOf(tracciatoRecord.getIndicatore2());

		if ((areaAppoggioInd1).equals("1")) {
			ente.setTipoNome(SbnTipoNomeAutore.R);
		} else if ((areaAppoggioInd1 + areaAppoggioInd2).equals("01")) {
			ente.setTipoNome(SbnTipoNomeAutore.G);
		} else if ((areaAppoggioInd1 + areaAppoggioInd2).equals("02")) {
			ente.setTipoNome(SbnTipoNomeAutore.E);
		} else {
			ente.setTipoNome(SbnTipoNomeAutore.E);
		}

		ente.setT001(vidDaAssegnare);
		ente.setLivelloAut(SbnLivello.valueOf("05"));

		// nominativo
		A210 a210 = new A210();
		a210.setId1(Indicatore.valueOf(areaAppoggioInd1));
		a210.setId2(Indicatore.valueOf(areaAppoggioInd2));

		areaAppoggioA = tagliaEtichetta(tracciatoRecord.getDati(), 'a');
		if (!areaAppoggioA.contains("*")) {
			areaAppoggioA = "*" + areaAppoggioA;
		}

		// almaviva2 30.05.2012 - Modifica per gestione del distanziatore " : " nei tipi nome ENTE
		if (areaAppoggioA.contains(" : ")) {
			areaAppoggioA = areaAppoggioA.replaceAll(" : ", "");
		}


		if (isFilled(areaAppoggioA)) {
			a210.setA_210(areaAppoggioA);
		}

		// Evolutiva Ottobre 2018 - almaviva2 - per trattare la etichetta $b negli autori di tipo Ente che è ripetibile

		// =================================================================================================================
		String areaAppoggioProgres = "";
		int fineStringa = 0;
		int inizioDati = 0;
		int fineDati = tracciatoRecord.getDati().length();
		String areaAppoggio1 = "";

		for (int scorrimento = inizioDati; scorrimento < fineDati; scorrimento++) {
			areaAppoggioProgres = tagliaEtichettaEntiConMoltepl(tracciatoRecord.getDati(), 'b', scorrimento, fineDati);

			// Ottobre 2018 - almaviva2 viene inserito il controllo dopo la ricerca di una etichetta Unimarc con Molteplicità
			// affinche alla risposta di non trovato si blocchi la ricerca immediatamente e non continui a "spulciare" la stringa
			// dati carattere x carattere fino alla fine !!
			if (length(areaAppoggioProgres) == 0) {
				break;
			}

			for (int i = 0; i < areaAppoggioProgres.length(); i++) {
				if (areaAppoggioProgres.substring(i, i + 1).equals("|")
						&& areaAppoggioProgres.substring(i + 1, i + 2).equals("|")
						&& areaAppoggioProgres.substring(i + 2, i + 3).equals("|")) {
					fineStringa = Integer.parseInt(areaAppoggioProgres.substring(0, i));
					areaAppoggio1 = areaAppoggioProgres.substring(i + 3, areaAppoggioProgres.length()).trim();

					if (areaAppoggio1.endsWith(".")){
						areaAppoggio1 = areaAppoggio1.substring(0, areaAppoggio1.length()-1);
					}


					if (areaAppoggio1.startsWith(" : ")) {
						areaAppoggioB = areaAppoggioB + areaAppoggio1;
					} else {
						areaAppoggioB = areaAppoggioB + " : " + areaAppoggio1;
					}

					scorrimento = scorrimento + fineStringa - 1;
					break;
				}
			}
		}
		// Ottobre 2018 - almaviva2 tutta questa parte viene eliminata perchè il campo utilizzato per inviare le $b
		// basta la stringa e non ci serve l'oggetto b210
//		UtilityAutori utilityAutori = new UtilityAutori();
//		String[] b210 = utilityAutori.getNomeTipoG_b_210(areaAppoggioB);
//		String areaAppoggioBtemp ="";
//		if (b210 != null) {
//			for (int h = 0; h < b210.length; h++) {
//				if (b210[h] != null) {
//					// Ottobre 2018 - almaviva2 viene inserito la ltrim per eliminare gli eventuali spazi presenti
//					// prima della $b vera e propria
//					// Ottobre 2018 - almaviva2 al posto di IID_SPAZIO, per separare le aree delle $b ci deve essere il ";"
//					areaAppoggioBtemp = areaAppoggioBtemp + " : " + ValidazioneDati.ltrim(b210[h]);
//				}
//			}
//		}
		areaAppoggioA = areaAppoggioA + IID_SPAZIO + areaAppoggioB;
		a210.setA_210(areaAppoggioA);

		// PARTE VECCHIA CHE GESTIVA LA $b univoca
//		areaAppoggioB = tagliaEtichetta(tracciatoRecord.getDati(), 'b');
//		if (isFilled(areaAppoggioB)) {
//
//			// almaviva2 30.05.2012 - Modifica per gestione del distanziatore " : " nei tipi nome ENTE
//			if (!areaAppoggioB.startsWith(" : ")) {
//				areaAppoggioB = " : " + areaAppoggioB;
//			}
//
//			UtilityAutori utilityAutori = new UtilityAutori();
//			String[] b210 = utilityAutori.getNomeTipoG_b_210(areaAppoggioB);
//			if (b210 != null) {
//				for (int h = 0; h < b210.length; h++) {
//					if (b210[h] != null) {
//						areaAppoggioA = areaAppoggioA + IID_SPAZIO + b210[h];
//					}
//				}
//			}
//			a210.setA_210(areaAppoggioA);
//		}

		// =================================================================================================================

		areaAppoggioC = tagliaEtichetta(tracciatoRecord.getDati(), 'c');
		if (isFilled(areaAppoggioC)) {
			a210.addC_210(areaAppoggioC);
		}

		areaAppoggioD = tagliaEtichetta(tracciatoRecord.getDati(), 'd');
		areaAppoggioD = areaAppoggioD.replace(":", "");
		areaAppoggioD = areaAppoggioD.replace("(", "");
		areaAppoggioD = areaAppoggioD.replace(")", "");
		if (isFilled(areaAppoggioD)) {
			a210.addD_210(areaAppoggioD);
		}

		areaAppoggioE = tagliaEtichetta(tracciatoRecord.getDati(), 'e');
		areaAppoggioE = areaAppoggioE.replace(":", "");
		areaAppoggioE = areaAppoggioE.replace("(", "");
		areaAppoggioE = areaAppoggioE.replace(")", "");
		if (isFilled(areaAppoggioE)) {
			a210.addE_210(areaAppoggioE);
		}

		areaAppoggioF = tagliaEtichetta(tracciatoRecord.getDati(), 'f');
		areaAppoggioF = areaAppoggioF.replace(":", "");
		areaAppoggioF = areaAppoggioF.replace("(", "");
		areaAppoggioF = areaAppoggioF.replace(")", "");
		if (isFilled(areaAppoggioF)) {
			a210.setF_210(areaAppoggioF);
		}

		ente.setT210(a210);
		ente.setCondiviso(DatiElementoTypeCondivisoType.N);

		return ente;
	}


	private String selectImportIdLinkObsoleta (String numRichiesta, String idUnimarc, String tipoAuth) {
		String testo="";
		StringBuilder sql = new StringBuilder();
		try {

//			Attenzione: ERRORE in ricerca su import_id_link per chiave: n.richiesta=1 id unimarc=
//			$aAguirre D'Amico, $bMaria Luisa Errore org.hibernate.exception.SQLGrammarException: could not execute query

//			if (idUnimarc.contains("'")) {
//				idUnimarc = idUnimarc.replace("'", "''");
//			}

			// Modifica Settembre 2013: la chiave di ricerca sulla tabella import_id_link deve essere tagliata ai primi 300 byte
			// di lunghezza (che corrisponde a quella del campo stesso)
			idUnimarc = trunc(idUnimarc, 300);


			Object[] record;


			// almaviva2 29.05.2012 modificata la select così da individuare i duplicati
			// se si differenziano per i soli caratteri maiuscoli/minuscoli

			// almaviva2 18.06.2012 - correzione della select per render i due upper omogenei
			sql.append("select id_inserito, id_input from import_id_link ");
			sql.append("where upper(id_input)= upper('" + DaoManager.escapeSql(idUnimarc) + "')");


//			sql.append("where upper(id_input)= '" + idUnimarc.toUpperCase() + "'");
//			sql.append("where id_input= '" + idUnimarc + "'");


			List list = getRecords(sql);
			if (!isFilled(list)) {
				return "";

			}

			sql = null;

			for (int i=0; i<list.size(); i++){

				record = (Object[]) list.get(i);
				testo = (String)record[0];	//id_inserito = bid sulla nuova base dati
			}
		} catch (Exception e) {
			testo ="Errore " + e.getMessage();
			return testo;
		}

		// Intervento Interno in sede di import RML 23.11.2012 (controllo che la stringa trovata sia della stessa autority cercata)
		if (tipoAuth.equals("")) {
			return testo;
		} else {
			if (testo.substring(3,4).equals(tipoAuth)) {
				return testo;
			} else {
				return "";
			}
		}
//		return testo;
	}


	private String selectImportIdLink (String numRichiesta, String chiaveCalcolata) {
		String testo="";
		StringBuilder sql = new StringBuilder();
		try {

//			Attenzione: ERRORE in ricerca su import_id_link per chiave: n.richiesta=1 id unimarc=
//			$aAguirre D'Amico, $bMaria Luisa Errore org.hibernate.exception.SQLGrammarException: could not execute query

//			if (chiaveCalcolata.contains("'")) {
//				chiaveCalcolata = chiaveCalcolata.replace("'", "''");
//			}

			// Modifica Settembre 2013: la chiave di ricerca sulla tabella import_id_link deve essere tagliata ai primi 300 byte
			// di lunghezza (che corrisponde a quella del campo stesso)
			chiaveCalcolata = trunc(chiaveCalcolata, 300);

			Object[] record;
			sql.append("select id_inserito, id_input from import_id_link ");
			sql.append("where id_input='").append(DaoManager.escapeSql(chiaveCalcolata)).append("' ");
			sql.append("and nr_richiesta=").append(numRichiesta);
			List list = getRecords(sql);
			int size = size(list);
			if (size == 0) {
				return "";
			}

			if (size > 1) {
				return "Errore trovati duplicati per id_input: " + chiaveCalcolata;
			}

			sql = null;
			for (int i = 0; i < size; i++) {
				record = (Object[]) list.get(i);
				testo = (String)record[0];	//id_inserito = bid sulla nuova base dati
			}

		} catch (Exception e) {
			testo = "Errore " + e.getMessage();
			return testo;
		}
		return testo;
	}

	private String insertImportIdLink(String chiaveCalcolata, String idSbn, String numRichiesta) {

	//		Attenzione: ERRORE in ricerca su import_id_link per chiave: n.richiesta=1 id unimarc=
	//		$aAguirre D'Amico, $bMaria Luisa Errore org.hibernate.exception.SQLGrammarException: could not execute query

//		if (chiaveCalcolata.contains("'")) {
//			chiaveCalcolata = chiaveCalcolata.replace("'", "''");
//		}

		// Modifica Settembre 2013: la chiave di ricerca sulla tabella import_id_link deve essere tagliata ai primi 300 byte
		// di lunghezza (che corrisponde a quella del campo stesso)
		chiaveCalcolata = trunc(chiaveCalcolata, 300);


		String sqlTemp;
		sqlTemp = "INSERT INTO import_id_link (id_input, id_inserito, fl_stato, ute_ins, ts_ins, nr_richiesta) VALUES (";
		sqlTemp += "'" + DaoManager.escapeSql(chiaveCalcolata) + "'";
		sqlTemp += ", '" + idSbn + "'";
		sqlTemp += ", 'I'";
		sqlTemp += ", '" + "IMPORT" + "'";
		sqlTemp += ", '" + now() + "'";
		sqlTemp += ", '" + numRichiesta + "'";
		sqlTemp += ")";

		try {
			executeInsertUpdate(sqlTemp, "");
		} catch (Exception e) {
			log.error("[Exception] " + e.getMessage());
			e.printStackTrace();
			return "";
		}

		return "";
	}

	private String selectVidSuTbAutore (String vid, String numRichiesta) {
		try {
			if (!autDao.esisteAutore(vid) )
				return "";

			insertImportIdLink(vid, vid, numRichiesta);

		} catch (Exception e) {
			return "Errore " + e.getMessage();
		}
		return vid;
	}

	private String selectTipoNomeSuTbAutore(String vid) {
		String tipoNome = "";
		try {
			tipoNome = autDao.getTipoNomeAutore(vid);
			if (!isFilled(tipoNome))
				return "";

		} catch (Exception e) {
			return "Errore " + e.getMessage();
		}
		return tipoNome;
	}

	private String selectBidSuTbTitolo(String bid, String numRichiesta) {
		try {
			if (!titDao.esisteTitolo(bid) )
				return "";

			// Inserimento sulla tb_link dell'oggetto già trovato
			insertImportIdLink(bid, bid, numRichiesta);

		} catch (Exception e) {
			return "Errore " + e.getMessage();
		}
		return bid;
	}

	private String setTestoLog (String testoOld) {
		return "<TR><TD>" + testoOld + "</TD></TR>";
	}

	private List getRecords(StringBuilder sqlSelect) throws DaoManagerException {
		try {
			session = getCurrentSession();
			SQLQuery query = session.createSQLQuery(sqlSelect.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	private void executeInsertUpdate(final String query, final String param) throws Exception {
		try {
			QueryData qd = new QueryData() {
				public String query() {	return query; }
				public String param() { return param; }
			};
			getDocumentoFisico().executeInsertUpdateImport(asSingletonList(qd));

		} catch (Exception e) {
			throw e;
		}
	}

	private DocumentoFisicoBMT getDocumentoFisico() throws Exception {
		if (documentoFisico != null)
			return documentoFisico;

		this.documentoFisico = DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
		return documentoFisico;
	}

}
