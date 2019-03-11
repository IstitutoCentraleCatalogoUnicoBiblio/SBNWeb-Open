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
package it.iccu.sbn.vo.validators.acquisizioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.PartecipantiGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.persistence.dao.acquisizioni.GenericJDBCAcquisizioniDAO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.strIsAlfabetic;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.strIsNumeric;

public class Validazione {

	private static Logger log = Logger.getLogger(Validazione.class);

	private static String struttura(final String sql) {
		StringBuilder buf = new StringBuilder(sql);
		buf.append(sql.indexOf("where") > 0 ? " and " : " where ");
		return buf.toString();
	}

	@SuppressWarnings("unused")
	public static int validaDataPassata(String data) {
		int DATA_OK = 0;
		int DATA_ERRATA = 1;
		int DATA_MAGGIORE = 2;
		int DATA_PASSATA_ERRATA = 3;

		int codRitorno = -1;
		if (data == null) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			//DateParser.parseDate(data);
			// l'istruzione sottostante va in errore se non non riesce a fare il parsing del rispetto del formato
			java.util.Date date = format.parse(data);
			if (java.util.regex.Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				Date oggi = new Date(System.currentTimeMillis());
				//if (date.after(oggi)) {
				//	codRitorno=DATA_MAGGIORE;
                //    throw new Exception(); // data > data odierna
				//}
				codRitorno=DATA_OK;
				return codRitorno; // tutto OK
			} else {
                codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}


	public static void ValidaListaSuppCambioVO(ListaSuppCambioVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}
		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoLista.getCodValuta() != null) {
			if (strIsNumeric(oggettoLista.getCodValuta())
					&& oggettoLista.getCodValuta().trim().length() != 0) {
				throw new ValidationException(
						"cambierroreCampoValutaAlfabetico",
						ValidationExceptionCodici.cambierroreCampoValutaAlfabetico);
			}
			if (oggettoLista.getCodValuta().length() > 3) {
				throw new ValidationException(
						"cambierroreCampoValutaEccedente",
						ValidationExceptionCodici.cambierroreCampoValutaEccedente);
			}
		}

		if (oggettoLista.getDesValuta() != null) {
			if (strIsNumeric(oggettoLista.getDesValuta())
					&& oggettoLista.getDesValuta().trim().length() != 0) {
				throw new ValidationException(
						"cambierroreCampoDescrizioneValutaAlfabetico",
						ValidationExceptionCodici.cambierroreCampoDescrizioneValutaAlfabetico);
			}

			if (oggettoLista.getDesValuta().length() > 160) {
				throw new ValidationException(
						"cambierroreCampoDescrizioneValutaEccedente",
						ValidationExceptionCodici.cambierroreCampoDescrizioneValutaEccedente);
			}
		}

	}

	public static void ValidaCambioVO(CambioVO oggettoVO)
			throws ValidationException {
		//
		if (oggettoVO.getCodBibl() == null) {
			return;
		}
		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl().length() > 0) {
			if (oggettoVO.getCodValuta().length() == 0) {
				throw new ValidationException(
						"cambierroreCampoValutaObbligatorio",
						ValidationExceptionCodici.cambierroreCampoValutaObbligatorio);
			}
			if (strIsNumeric(oggettoVO.getCodValuta())) {
				throw new ValidationException(
						"cambierroreCampoValutaAlfabetico",
						ValidationExceptionCodici.cambierroreCampoValutaAlfabetico);
			}
			if (oggettoVO.getCodValuta().length() > 3) {
				throw new ValidationException(
						"cambierroreCampoValutaEccedente",
						ValidationExceptionCodici.cambierroreCampoValutaEccedente);
			}
			if (String.valueOf(oggettoVO.getTassoCambio()).length() == 0) {
				throw new ValidationException(
						"cambierroreCampoTassoObbligatorio",
						ValidationExceptionCodici.cambierroreCampoTassoObbligatorio);
			}
			if (strIsAlfabetic(String.valueOf(oggettoVO.getTassoCambio()))) {
				throw new ValidationException("cambierroreCampoTassoNumerico",
						ValidationExceptionCodici.cambierroreCampoTassoNumerico);
			} else {

				if (oggettoVO.getTassoCambioStr() == null
						|| (oggettoVO.getTassoCambioStr() != null && oggettoVO
								.getTassoCambioStr().trim().equals("0,00"))) {
					throw new ValidationException("valoreNullo",
							ValidationExceptionCodici.valoreNullo);
				}
			}

		}
	}

	public static void ValidaRicerca(List<CambioVO> listaCambi)
			throws ValidationException {
		//
		if (listaCambi == null || listaCambi.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}

	}

	public static void ValidaRicercaBuoniOrd(
			List<BuoniOrdineVO> listaBuoniOrd) throws ValidationException {
		//
		if (listaBuoniOrd == null || listaBuoniOrd.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppBuoniOrdineVO(GenericJDBCAcquisizioniDAO dao,
			ListaSuppBuoniOrdineVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoLista.getDataBuonoOrdineDa() != null
				&& oggettoLista.getDataBuonoOrdineDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataBuonoOrdineDa())
					&& oggettoLista.getDataBuonoOrdineDa().length() == 4) {
				String strAnnata = oggettoLista.getDataBuonoOrdineDa();
				oggettoLista.setDataBuonoOrdineDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataBuonoOrdineDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoLista.getDataBuonoOrdineA() != null
				&& oggettoLista.getDataBuonoOrdineA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataBuonoOrdineA())
					&& oggettoLista.getDataBuonoOrdineA().length() == 4) {
				String strAnnata = oggettoLista.getDataBuonoOrdineA();
				oggettoLista.setDataBuonoOrdineA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataBuonoOrdineA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoLista.getBilancio() != null
				&& oggettoLista.getBilancio().getCodice1() != null
				&& oggettoLista.getBilancio().getCodice1().length() != 0) {
			if (!strIsNumeric(oggettoLista.getBilancio().getCodice1())
					&& oggettoLista.getBilancio().getCodice1().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioNumerico",
						ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
			}
			if (oggettoLista.getBilancio().getCodice1().length() != 4) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioEccedente",
						ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
			}
		}
		if (oggettoLista.getBilancio() != null
				&& oggettoLista.getBilancio().getCodice2() != null
				&& oggettoLista.getBilancio().getCodice2().length() != 0) {
			if (!strIsNumeric(oggettoLista.getBilancio().getCodice2())
					&& oggettoLista.getBilancio().getCodice2().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloNumerico",
						ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
			}
			if (oggettoLista.getBilancio().getCodice1().length() > 16) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloEccedente",
						ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
			}
		}

		if (oggettoLista.getNumBuonoOrdineDa() != null
				&& oggettoLista.getNumBuonoOrdineDa().length() != 0) {
			if (!strIsNumeric(oggettoLista.getNumBuonoOrdineDa().trim())
					&& oggettoLista.getNumBuonoOrdineDa().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
		}
		if (oggettoLista.getNumBuonoOrdineA() != null
				&& oggettoLista.getNumBuonoOrdineA().length() != 0) {
			if (!strIsNumeric(oggettoLista.getNumBuonoOrdineA().trim())
					&& oggettoLista.getNumBuonoOrdineA().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
		}

		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getCodice() != null
				&& oggettoLista.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(oggettoLista.getFornitore().getCodice().trim())
					&& oggettoLista.getFornitore().getCodice().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCodFornitoreNumerico",
						ValidationExceptionCodici.ordinierroreCampoCodFornitoreNumerico);
			}
		}
		//
		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getDescrizione() != null
				&& oggettoLista.getFornitore().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getFornitore().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoFornitoreDescrizioneAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoFornitoreDescrizioneAlfabetico);
			}
			if (oggettoLista.getFornitore().getDescrizione().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoFornitoreDescrizioneEccedente",
						ValidationExceptionCodici.ordinierroreCampoFornitoreDescrizioneEccedente);
			}
		}

		// tipo ordine
		if (oggettoLista.getOrdine() != null
				&& oggettoLista.getOrdine().getCodice1() != null
				&& oggettoLista.getOrdine().getCodice1().length() != 0) {
			if (strIsNumeric(oggettoLista.getOrdine().getCodice1())) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
			}
			if (oggettoLista.getOrdine().getCodice1().length() != 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
			}
		}
		// anno ord
		if (oggettoLista.getOrdine() != null
				&& String.valueOf(oggettoLista.getOrdine().getCodice2())
						.length() != 0) {
			if (!strIsNumeric(oggettoLista.getOrdine().getCodice2().trim())) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoValiditaSezioneNumerico",
						ValidationExceptionCodici.sezioneerroreCampoAnnoValiditaSezioneNumerico);
			}

			if (oggettoLista.getOrdine().getCodice2().length() != 4) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoValiditaSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoAnnoValiditaSezioneEccedente);
			}
		}
		// codice ordine
		if (oggettoLista.getOrdine() != null
				&& oggettoLista.getOrdine().getCodice3() != null
				&& oggettoLista.getOrdine().getCodice3().length() != 0) {
			if (!strIsNumeric(oggettoLista.getOrdine().getCodice3().trim())
					&& oggettoLista.getOrdine().getCodice3().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
		}
	}

	public static void ValidaBuoniOrdineVO(GenericJDBCAcquisizioniDAO dao, BuoniOrdineVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}
		// temporaneo
		// occorre vedere se se il buono è configurato per progressivo
		// automatico o manuale
		boolean numAutom = false;
		ConfigurazioneBOVO configBO = new ConfigurazioneBOVO();
		configBO.setCodBibl(oggettoVO.getCodBibl());
		try {
			if (dao.loadConfigurazione(configBO).isNumAutomatica()) {
				numAutom = true;
			}
		} catch (Exception e) {
			// l'errore capita in questo punto
			log.error("", e);
		}
		// è obbligatorio solo se la numerazione del bo non è automatica
		if (oggettoVO.getNumBuonoOrdine() != null
				&& oggettoVO.getNumBuonoOrdine().trim().length() == 0
				&& !numAutom) {
			throw new ValidationException(
					"buonierroreCampoNumBuonoObbligatorio",
					ValidationExceptionCodici.buonierroreCampoNumBuonoObbligatorio);
		}

		if (oggettoVO.getFornitore() != null
				&& oggettoVO.getFornitore().getCodice() != null
				&& oggettoVO.getFornitore().getCodice().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoFornitoreObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
		}

		if (oggettoVO.isGestBil() && oggettoVO.getBilancio() != null
				&& oggettoVO.getBilancio().getCodice1() != null
				&& oggettoVO.getBilancio().getCodice1().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoBilancioEsercizioObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBilancioEsercizioObbligatorio);
		}
		if (oggettoVO.isGestBil() && oggettoVO.getBilancio() != null
				&& oggettoVO.getBilancio().getCodice2() != null
				&& oggettoVO.getBilancio().getCodice2().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoBilancioCapitoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBilancioCapitoloObbligatorio);
		}
		// il controllo su stampato deve avvenire solo sul salva e non sul si di
		// conferma
		if (oggettoVO.getStatoBuonoOrdine() != null
				&& oggettoVO.getStatoBuonoOrdine().equals("S")
				&& !oggettoVO.isSalvaEffettuato()) {
			throw new ValidationException("ordinierroreBuonoOrdineStampato",
					ValidationExceptionCodici.ordinierroreBuonoOrdineStampato);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoVO.getDataBuonoOrdine() != null
				&& oggettoVO.getDataBuonoOrdine().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataBuonoOrdine()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoVO.isGestBil() && oggettoVO.getBilancio() != null
				&& oggettoVO.getBilancio().getCodice1() != null
				&& oggettoVO.getBilancio().getCodice1().length() != 0) {
			if (!strIsNumeric(oggettoVO.getBilancio().getCodice1().trim())
					&& oggettoVO.getBilancio().getCodice1().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioNumerico",
						ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
			}
			if (oggettoVO.getBilancio().getCodice1().length() != 4) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioEccedente",
						ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
			}
		}
		if (oggettoVO.isGestBil() && oggettoVO.getBilancio() != null
				&& oggettoVO.getBilancio().getCodice2() != null
				&& oggettoVO.getBilancio().getCodice2().length() != 0) {
			if (!strIsNumeric(oggettoVO.getBilancio().getCodice2().trim())
					&& oggettoVO.getBilancio().getCodice2().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloNumerico",
						ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
			}
			if (oggettoVO.getBilancio().getCodice1().length() > 16) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloEccedente",
						ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
			}
		}

		if (oggettoVO.getNumBuonoOrdine() != null
				&& oggettoVO.getNumBuonoOrdine().length() != 0) {
			if (!strIsNumeric(oggettoVO.getNumBuonoOrdine().trim())
					&& oggettoVO.getNumBuonoOrdine().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
		}

		if (oggettoVO.getStatoBuonoOrdine() != null
				&& oggettoVO.getStatoBuonoOrdine().length() != 0) {
			if (strIsNumeric(oggettoVO.getStatoBuonoOrdine())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getStatoBuonoOrdine().length() > 1) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoVO.getImportoStr() != null
				&& oggettoVO.getImportoStr().length() != 0) {
			if (strIsAlfabetic(oggettoVO.getImportoStr().trim())
					&& oggettoVO.getImportoStr().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
		}

		if (oggettoVO.getFornitore() != null
				&& oggettoVO.getFornitore().getCodice() != null
				&& oggettoVO.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(oggettoVO.getFornitore().getCodice().trim())
					&& oggettoVO.getFornitore().getCodice().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCodFornitoreNumerico",
						ValidationExceptionCodici.ordinierroreCampoCodFornitoreNumerico);
			}
		}

		if (oggettoVO.getListaOrdiniBuono() != null
				&& oggettoVO.getListaOrdiniBuono().size() != 0) {
			if (oggettoVO.getListaOrdiniBuono().size() > 0) {
				for (int i = 0; i < oggettoVO.getListaOrdiniBuono().size(); i++) {
					OrdiniVO oggettoDettVO = oggettoVO.getListaOrdiniBuono()
							.get(i);

					if (oggettoDettVO.getTipoOrdine() != null
							&& oggettoDettVO.getTipoOrdine().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoTipoOrdineObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoTipoOrdineObbligatorio);
					}

					if (oggettoDettVO.getTipoOrdine() != null
							&& oggettoDettVO.getTipoOrdine().length() != 0) {
						if (strIsNumeric(oggettoDettVO.getTipoOrdine())) {
							throw new ValidationException(
									"ordinierroreCampoTipoOrdineAlfabetico",
									ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
						}
						if (oggettoDettVO.getTipoOrdine().length() != 1) {
							throw new ValidationException(
									"ordinierroreCampoTipoOrdineEccedente",
									ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
						}
					}
					if (oggettoDettVO.getCodOrdine() != null
							&& oggettoDettVO.getCodOrdine().length() == 0) {
						{
							throw new ValidationException(
									"sezioneerroreCampoCodiceOrdineObbligatorio",
									ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineObbligatorio);
						}
					}

					if (oggettoDettVO.getCodOrdine() != null
							&& oggettoDettVO.getCodOrdine().length() != 0) {
						if (!strIsNumeric(oggettoDettVO.getCodOrdine())
								&& oggettoDettVO.getCodOrdine().trim().length() != 0) {
							throw new ValidationException(
									"sezioneerroreCampoCodiceOrdineNumerico",
									ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineNumerico);
						}
					}
					if (oggettoDettVO.getAnnoOrdine() != null
							&& oggettoDettVO.getAnnoOrdine().length() == 0) {
						throw new ValidationException(
								"sezioneerroreCampoAnnoOrdineObbligatorio",
								ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineObbligatorio);
					}
					if (oggettoDettVO.getAnnoOrdine() != null
							&& oggettoDettVO.getAnnoOrdine().length() != 0) {
						// controllo congruenza
						if (!strIsNumeric(oggettoDettVO.getAnnoOrdine())
								&& oggettoDettVO.getAnnoOrdine().trim()
										.length() != 0) {
							throw new ValidationException(
									"sezioneerroreCampoAnnoOrdineNumerico",
									ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
						}
						if (oggettoDettVO.getAnnoOrdine().length() != 4) {
							throw new ValidationException(
									"sezioneerroreCampoAnnoOrdineEccedente",
									ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
						}

					}

					// controllo duplicazione ordine
					String rigaOrd = oggettoDettVO.getChiave().trim();
					if (i > 0) {
						for (int j = 0; j < i; j++) {

							if (rigaOrd.equals(oggettoVO.getListaOrdiniBuono()
									.get(j).getChiave().trim())) {
								throw new ValidationException(
										"ordinierroreOrdineRipetuto",
										ValidationExceptionCodici.ordinierroreOrdineRipetuto);
							}
						}
					}

				}
			}
		} else {
			throw new ValidationException("buonoOdineRigheOrdineAssenti",
					ValidationExceptionCodici.buonoOdineRigheOrdineAssenti);
		}

	}

	public static void ValidaRicercaSezioni(List<SezioneVO> listaSezioni)
			throws ValidationException {
		//
		if (listaSezioni == null || listaSezioni.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppSezioneVO(ListaSuppSezioneVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoLista.getCodiceSezione() != null
				&& oggettoLista.getCodiceSezione().trim().length() != 0) {
			if (oggettoLista.getCodiceSezione().length() > 7) {
				throw new ValidationException(
						"sezioneerroreCodSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCodSezioneEccedente);
			}
		}

		if (oggettoLista.getDescrizioneSezione() != null
				&& oggettoLista.getDescrizioneSezione().length() != 0) {

			if (oggettoLista.getDescrizioneSezione().length() > 30) {
				throw new ValidationException(
						"sezioneerroreCampoDescrSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoDescrSezioneEccedente);
			}
		}

	}

	public static void ValidaSezioneVO(SezioneVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (oggettoVO.getCodiceSezione() != null
				&& oggettoVO.getCodiceSezione().trim().length() == 0) {
			throw new ValidationException(
					"sezioneerroreCampoCodSezioneObbligatorio",
					ValidationExceptionCodici.sezioneerroreCampoCodSezioneObbligatorio);
		}
		if (oggettoVO.getDescrizioneSezione() != null
				&& oggettoVO.getDescrizioneSezione().trim().length() == 0) {
			throw new ValidationException(
					"sezioneerroreCampoDescrSezioneObbligatorio",
					ValidationExceptionCodici.sezioneerroreCampoDescrSezioneObbligatorio);
		}
		if (oggettoVO.getAnnoValiditaSezione() != null
				&& oggettoVO.getAnnoValiditaSezione().trim().length() == 0) {
			throw new ValidationException(
					"sezioneerroreCampoAnnoValiditaObbligatorio",
					ValidationExceptionCodici.sezioneerroreCampoAnnoValiditaObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}
		if (oggettoVO.getCodiceSezione() != null
				&& oggettoVO.getCodiceSezione().trim().length() != 0) {
			if (oggettoVO.getCodiceSezione().length() > 7) {
				throw new ValidationException(
						"sezioneerroreCodSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCodSezioneEccedente);
			}
		}

		if (oggettoVO.getDescrizioneSezione() != null
				&& oggettoVO.getDescrizioneSezione().length() != 0) {
			if (strIsNumeric(oggettoVO.getDescrizioneSezione())
					&& oggettoVO.getDescrizioneSezione().trim().length() != 0) {
				throw new ValidationException(
						"sezioneerroreCampoDescrSezioneAlfabetico",
						ValidationExceptionCodici.sezioneerroreCampoDescrSezioneAlfabetico);
			}
			if (oggettoVO.getDescrizioneSezione().length() > 30) {
				throw new ValidationException(
						"sezioneerroreCampoDescrSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoDescrSezioneEccedente);
			}
		}
		if (String.valueOf(oggettoVO.getSommaDispSezione()).length() != 0) {
			if (strIsAlfabetic(String.valueOf(oggettoVO
					.getSommaDispSezione()))) {
				throw new ValidationException(
						"sezioneerroreCampoSommaDispNumerico",
						ValidationExceptionCodici.sezioneerroreCampoSommaDispNumerico);
			}

		}
		if (oggettoVO.getNoteSezione() != null
				&& oggettoVO.getNoteSezione().length() != 0) {
			if (strIsNumeric(oggettoVO.getNoteSezione())
					&& oggettoVO.getNoteSezione().trim().length() != 0) {
				throw new ValidationException(
						"sezioneerroreCampoNoteSezioneAlfabetico",
						ValidationExceptionCodici.sezioneerroreCampoNoteSezioneAlfabetico);
			}
			if (oggettoVO.getNoteSezione().length() > 255) {
				throw new ValidationException(
						"sezioneerroreCampoNoteSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoNoteSezioneEccedente);
			}
		}
		if (String.valueOf(oggettoVO.getAnnoValiditaSezione()).length() != 0) {
			if (!strIsNumeric(oggettoVO.getAnnoValiditaSezione())) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoValiditaSezioneNumerico",
						ValidationExceptionCodici.sezioneerroreCampoAnnoValiditaSezioneNumerico);
			}

			if (oggettoVO.getAnnoValiditaSezione().length() != 4) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoValiditaSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoAnnoValiditaSezioneEccedente);
			}
		}

		if (String.valueOf(oggettoVO.getBudgetSezione()).length() != 0) {
			if (strIsAlfabetic(String.valueOf(oggettoVO.getBudgetSezione()))) {
				throw new ValidationException(
						"sezioneerroreCampoBudgetSezioneNumerico",
						ValidationExceptionCodici.sezioneerroreCampoBudgetSezioneNumerico);
			}
		}
		if (oggettoVO.getDataVal() != null
				&& oggettoVO.getDataVal().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataVal()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoVO.getBudgetSezioneStr() != null
				&& oggettoVO.getBudgetSezioneStr().trim().length() != 0) {
			// controllo importoin valuta euro
			String amount = oggettoVO.getBudgetSezioneStr().trim();
			NumberFormat formatIT = NumberFormat.getCurrencyInstance();
			String strCurrency = "\u20AC ";
			strCurrency = strCurrency + amount;
			try {
				formatIT.parse(strCurrency); // va in errore se non
															// è riconosciuto
															// come formato euro
			} catch (ParseException e) {
				log.error("", e);
				throw new ValidationException("importoErrato",
						ValidationExceptionCodici.importoErrato);
			}
		}

	}

	public static void ValidaRicercaProfili(
			List<StrutturaProfiloVO> listaProfili)
			throws ValidationException {
		//
		if (listaProfili == null || listaProfili.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaRicercaFatture(List<FatturaVO> listaFatture)
			throws ValidationException {
		//
		if (listaFatture == null || listaFatture.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaRicercaBilanci(List<BilancioVO> listaBilanci)
			throws ValidationException {
		//
		if (listaBilanci == null || listaBilanci.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppProfiloVO(ListaSuppProfiloVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoLista.getSezione() != null
				&& oggettoLista.getSezione().getCodice() != null
				&& oggettoLista.getSezione().getCodice().length() != 0) {

			if (oggettoLista.getSezione().getCodice().length() > 7) {
				throw new ValidationException(
						"ordinierroreCodSezioneEccedente",
						ValidationExceptionCodici.ordinierroreCodSezioneEccedente);
			}

		}
		if (oggettoLista.getProfilo() != null
				&& oggettoLista.getProfilo().getCodice() != null
				&& oggettoLista.getProfilo().getCodice().length() != 0) {

			if (!strIsNumeric(oggettoLista.getProfilo().getCodice())) {
				throw new ValidationException(
						"ordineerroreCodProfAcqNumerico",
						ValidationExceptionCodici.ordineerroreCodProfAcqNumerico);
			}
		}
		if (oggettoLista.getProfilo() != null
				&& oggettoLista.getProfilo().getDescrizione() != null
				&& oggettoLista.getProfilo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getProfilo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoDescProfiloAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoDescProfiloAlfabetico);
			}
			if (oggettoLista.getProfilo().getDescrizione().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoDescProfiloEccedente",
						ValidationExceptionCodici.ordinierroreCampoDescProfiloEccedente);
			}
		}

	}

	public static void ValidaListaSuppFatturaVO(ListaSuppFatturaVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoLista.getNumFattura() != null
				&& oggettoLista.getNumFattura().length() != 0) {

			if (oggettoLista.getNumFattura().length() > 20) {
				throw new ValidationException(
						"fatturaerroreNumFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreNumFatturaEccedente);
			}
		}
		if (oggettoLista.getStatoFattura() != null
				&& oggettoLista.getStatoFattura().length() != 0) {
			if (strIsAlfabetic(String.valueOf(oggettoLista
					.getStatoFattura()))) {
				throw new ValidationException(
						"fatturaerroreStatoFatturaNumerico",
						ValidationExceptionCodici.fatturaerroreStatoFatturaNumerico);
			}

			if (oggettoLista.getStatoFattura().length() > 1) {
				throw new ValidationException(
						"fatturaerroreStatoFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreStatoFatturaEccedente);
			}
		}
		if (oggettoLista.getDataFatturaDa() != null
				&& oggettoLista.getDataFatturaDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataFatturaDa())
					&& oggettoLista.getDataFatturaDa().length() == 4) {
				String strAnnata = oggettoLista.getDataFatturaDa();
				oggettoLista.setDataFatturaDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataFatturaDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoLista.getDataFatturaA() != null
				&& oggettoLista.getDataFatturaA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataFatturaA())
					&& oggettoLista.getDataFatturaA().length() == 4) {
				String strAnnata = oggettoLista.getDataFatturaA();
				oggettoLista.setDataFatturaA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataFatturaA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoLista.getAnnoFattura() != null
				&& String.valueOf(oggettoLista.getAnnoFattura()).length() != 0) {
			if (!strIsNumeric(oggettoLista.getAnnoFattura())) {
				throw new ValidationException("erroreCampoAnnoNumerico",
						ValidationExceptionCodici.erroreCampoAnnoNumerico);
			}

			if (oggettoLista.getAnnoFattura().length() != 4) {
				throw new ValidationException(
						"erroreCampoAnnoNumericoEccedente",
						ValidationExceptionCodici.erroreCampoAnnoNumericoEccedente);
			}
		}

		if (oggettoLista.getProgrFattura() != null
				&& oggettoLista.getProgrFattura().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getProgrFattura())
					&& oggettoLista.getProgrFattura().trim().length() != 0) {
				throw new ValidationException(
						"fatturaerroreCampoProgrNumerico",
						ValidationExceptionCodici.fatturaerroreCampoProgrNumerico);
			}
		}
		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getCodice() != null
				&& oggettoLista.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoLista.getFornitore()
					.getCodice()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}
		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getDescrizione() != null
				&& oggettoLista.getFornitore().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getFornitore().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoLista.getFornitore().getDescrizione().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}
		if (oggettoLista.getTipoFattura() != null
				&& oggettoLista.getTipoFattura().length() != 0) {

			if (strIsNumeric(oggettoLista.getTipoFattura())) {
				throw new ValidationException(
						"fatturaerroreTipoFatturaAlfabetico",
						ValidationExceptionCodici.fatturaerroreTipoFatturaAlfabetico);
			}
			if (oggettoLista.getTipoFattura().length() > 1) {
				throw new ValidationException(
						"fatturaerroreTipoFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreTipoFatturaEccedente);
			}
		}
		if (oggettoLista.getOrdine() != null
				&& oggettoLista.getOrdine().getCodice1() != null
				&& oggettoLista.getOrdine().getCodice1().length() != 0) {
			if (strIsNumeric(oggettoLista.getOrdine().getCodice1())) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
			}
			if (oggettoLista.getOrdine().getCodice1().length() != 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
			}
		}
		if (oggettoLista.getOrdine() != null
				&& String.valueOf(oggettoLista.getOrdine().getCodice2())
						.length() != 0) {
			if (!strIsNumeric(oggettoLista.getOrdine().getCodice2().trim())) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
			}

			if (oggettoLista.getOrdine().getCodice2().length() != 4) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineEccedente",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
			}
		}
		if (oggettoLista.getOrdine() != null
				&& oggettoLista.getOrdine().getCodice3() != null
				&& oggettoLista.getOrdine().getCodice3().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoLista.getOrdine()
					.getCodice3().trim()))) {
				throw new ValidationException(
						"sezioneerroreCampoCodiceOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineNumerico);
			}
		}

	}

	public static void ValidaListaSuppBilancioVO(
			ListaSuppBilancioVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoLista.getEsercizio() != null
				&& oggettoLista.getEsercizio().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getEsercizio())
					&& oggettoLista.getEsercizio().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioNumerico",
						ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
			}
			if (oggettoLista.getEsercizio().length() != 4) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioEccedente",
						ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
			}
		}

		if (oggettoLista.getCapitolo() != null
				&& oggettoLista.getCapitolo().length() != 0) {
			if (!strIsNumeric(oggettoLista.getCapitolo())
					&& oggettoLista.getCapitolo().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloNumerico",
						ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
			}
			if (oggettoLista.getCapitolo().length() > 16) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloEccedente",
						ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
			}
		}

		if (oggettoLista.getImpegno() != null
				&& oggettoLista.getImpegno().length() != 0) {
			if (!strIsNumeric(oggettoLista.getImpegno())) {
				throw new ValidationException(
						"ordinierroreCampoBilancioImpegnoAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoAlfabetico);
			}
			if (oggettoLista.getImpegno().length() > 1) {
				throw new ValidationException(
						"ordinierroreCampoBilancioImpegnoEccedente",
						ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoEccedente);
			}
		}

	}

	public static void ValidaBilancioVO(BilancioVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (oggettoVO.getEsercizio() != null
				&& oggettoVO.getEsercizio().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoBilancioEsercizioObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBilancioEsercizioObbligatorio);
		}
		if (oggettoVO.getCapitolo() != null
				&& oggettoVO.getCapitolo().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoBilancioCapitoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBilancioCapitoloObbligatorio);
		}
		if (String.valueOf(oggettoVO.getBudgetDiCapitolo()).length() == 0) {
			throw new ValidationException("ordineerroreBilBudgetObbligatorio",
					ValidationExceptionCodici.ordineerroreBilBudgetObbligatorio);
		}

		if (oggettoVO.getCapitolo() != null
				&& oggettoVO.getCapitolo().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoBilancioEsercizioObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBilancioEsercizioObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoVO.getEsercizio() != null
				&& oggettoVO.getEsercizio().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getEsercizio())
					&& oggettoVO.getEsercizio().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioNumerico",
						ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
			}
			if (oggettoVO.getEsercizio().length() != 4) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioEccedente",
						ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
			}
		}

		if (oggettoVO.getCapitolo() != null
				&& oggettoVO.getCapitolo().length() != 0) {
			if (!strIsNumeric(oggettoVO.getCapitolo())
					&& oggettoVO.getCapitolo().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloNumerico",
						ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
			}
			if (oggettoVO.getCapitolo().length() > 16) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloEccedente",
						ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
			}
		}
		double totBdg = 0;

		if (oggettoVO.getDettagliBilancio() != null
				&& oggettoVO.getDettagliBilancio().size() != 0) {
			if (oggettoVO.getDettagliBilancio().size() > 0
					&& oggettoVO.getDettagliBilancio().size() < 5) {
				String[] rigaImp = { "", "", "", "" };
				double singleBdg = 0;
				for (int i = 0; i < oggettoVO.getDettagliBilancio().size(); i++) {

					BilancioDettVO oggettoDettVO = oggettoVO
							.getDettagliBilancio().get(i);

					if (oggettoDettVO.getImpegno() != null
							&& oggettoDettVO.getImpegno().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoBilancioImpegnoObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoObbligatorio);
					}

					if (String.valueOf(oggettoDettVO.getBudget()).length() == 0) {
						throw new ValidationException(
								"ordineerroreBilBudgetObbligatorio",
								ValidationExceptionCodici.ordineerroreBilBudgetObbligatorio);
					}

					if (oggettoDettVO.getImpegno() != null
							&& oggettoDettVO.getImpegno().length() != 0) {
						// controllo duplicazione tipo impegno
						rigaImp[i] = oggettoDettVO.getImpegno();
						if (i > 0) {
							for (int j = 0; j < i; j++) {
								if (rigaImp[i].equals(rigaImp[j])) {
									throw new ValidationException(
											"bilancioerroreCodiceImpegnoPresente",
											ValidationExceptionCodici.bilancioerroreCodiceImpegnoPresente);
								}
							}
						}
						// fine controllo duplicazione
						if (!strIsNumeric(oggettoDettVO.getImpegno())) {
							throw new ValidationException(
									"ordinierroreCampoBilancioImpegnoAlfabetico",
									ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoAlfabetico);
						}
						if (oggettoDettVO.getImpegno().length() > 1) {
							throw new ValidationException(
									"ordinierroreCampoBilancioImpegnoEccedente",
									ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoEccedente);
						}
					}
					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getBudget()))) {
						throw new ValidationException(
								"ordineerroreBilBudgetNumerico",
								ValidationExceptionCodici.ordineerroreBilBudgetNumerico);

					}
					singleBdg = oggettoDettVO.getBudget();
					totBdg = totBdg + singleBdg;

					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getDispCassa()))) {
						throw new ValidationException(
								"ordineerroreBilDispCassaNumerico",
								ValidationExceptionCodici.ordineerroreBilDispCassaNumerico);

					}

					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getDispCompetenza()))) {
						throw new ValidationException(
								"ordineerroreBilDispCompetenzaNumerico",
								ValidationExceptionCodici.ordineerroreBilDispCompetenzaNumerico);

					}
					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getFatturato()))) {
						throw new ValidationException(
								"ordineerroreBilFatturatoNumerico",
								ValidationExceptionCodici.ordineerroreBilFatturatoNumerico);

					}
					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getImpegnato()))) {
						throw new ValidationException(
								"ordineerroreBilImpegnatoNumerico",
								ValidationExceptionCodici.ordineerroreBilImpegnatoNumerico);

					}
					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getImpFatt()))) {
						throw new ValidationException(
								"ordineerroreBilImpFattNumerico",
								ValidationExceptionCodici.ordineerroreBilImpFattNumerico);

					}
					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getPagato()))) {
						throw new ValidationException(
								"ordineerroreBilPagatoNumerico",
								ValidationExceptionCodici.ordineerroreBilPagatoNumerico);

					}

				}

			} else {
				throw new ValidationException(
						"bilancioRigheCodiceImpegnoEccedenti",
						ValidationExceptionCodici.bilancioRigheCodiceImpegnoEccedenti);

			}
		} else {
			// almeno una riga
			throw new ValidationException("fatturaRigheAssenti",
					ValidationExceptionCodici.buonoOdineRigheOrdineAssenti);

		}

		if (strIsAlfabetic(String.valueOf(oggettoVO.getBudgetDiCapitolo()))) {
			throw new ValidationException("ordineerroreBilBudgetNumerico",
					ValidationExceptionCodici.ordineerroreBilBudgetNumerico);

		}
		if (oggettoVO.getBudgetDiCapitolo() < totBdg) {
			throw new ValidationException("bilancioTotincongruenza",
					ValidationExceptionCodici.bilancioTotincongruenza);

		}
	}

	public static void ValidaStrutturaProfiloVO(StrutturaProfiloVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}
		if (oggettoVO.getPaese().getCodice() != null
				&& oggettoVO.getPaese().getCodice().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoPaeseFornObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoPaeseFornObbligatorio);
		}
		if (oggettoVO.getSezione().getCodice() != null
				&& oggettoVO.getSezione().getCodice().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoSezioneObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoSezioneObbligatorio);
		}
		if (oggettoVO.getProfilo().getDescrizione() != null
				&& oggettoVO.getProfilo().getDescrizione().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoDescProfiloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoDescProfiloObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoVO.getSezione() != null
				&& oggettoVO.getSezione().getCodice() != null
				&& oggettoVO.getSezione().getCodice().length() != 0) {

			if (oggettoVO.getSezione().getCodice().length() > 7) {
				throw new ValidationException(
						"ordinierroreCodSezioneEccedente",
						ValidationExceptionCodici.ordinierroreCodSezioneEccedente);
			}

		}
		if (oggettoVO.getProfilo() != null
				&& oggettoVO.getProfilo().getCodice() != null
				&& oggettoVO.getProfilo().getCodice().length() != 0) {

			if (!strIsNumeric(oggettoVO.getProfilo().getCodice())) {
				throw new ValidationException(
						"ordineerroreCodProfAcqNumerico",
						ValidationExceptionCodici.ordineerroreCodProfAcqNumerico);
			}
		}
		if (oggettoVO.getProfilo() != null
				&& oggettoVO.getProfilo().getDescrizione() != null
				&& oggettoVO.getProfilo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoVO.getProfilo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoDescProfiloAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoDescProfiloAlfabetico);
			}
			if (oggettoVO.getProfilo().getDescrizione().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoDescProfiloEccedente",
						ValidationExceptionCodici.ordinierroreCampoDescProfiloEccedente);
			}
		}
		if (oggettoVO.getPaese() != null
				&& oggettoVO.getPaese().getCodice() != null
				&& oggettoVO.getPaese().getCodice().length() != 0) {
			if (strIsNumeric(oggettoVO.getPaese().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampPaeseAlfabetico",
						ValidationExceptionCodici.ordinierroreCampPaeseAlfabetico);
			}
			if (oggettoVO.getPaese().getCodice().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoPaeseFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoPaeseFornEccedente);
			}
		}

		if (oggettoVO.getListaFornitori() != null
				&& oggettoVO.getListaFornitori().size() != 0) {
			if (oggettoVO.getListaFornitori().size() > 0) {
				// String[] rigaImp={"","",""};
				String[] rigaImp = new String[oggettoVO.getListaFornitori()
						.size()];
				for (int i = 0; i < oggettoVO.getListaFornitori().size(); i++) {
					rigaImp[i] = "";
					StrutturaTerna oggettoDettVO = (StrutturaTerna) oggettoVO
							.getListaFornitori().get(i);

					if (oggettoDettVO.getCodice2() != null
							&& oggettoDettVO.getCodice2() != null
							&& oggettoDettVO.getCodice2().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoFornitoreObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
					}

					if (oggettoDettVO.getCodice2() != null
							&& oggettoDettVO.getCodice2().length() != 0) {
						if (!strIsNumeric(String.valueOf(oggettoDettVO
								.getCodice2()))) {
							throw new ValidationException(
									"ordineerroreCodFornNumerico",
									ValidationExceptionCodici.ordineerroreCodFornNumerico);
						}
					}
					if (oggettoDettVO.getCodice3() != null
							&& oggettoDettVO.getCodice3().length() != 0) {
						if (strIsNumeric(oggettoDettVO.getCodice3())) {
							throw new ValidationException(
									"ordinierroreCampoNomeFornAlfabetico",
									ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
						}
						if (oggettoDettVO.getCodice3().length() > 50) {
							throw new ValidationException(
									"ordinierroreCampoNomeFornEccedente",
									ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
						}
					}
					// controllo duplicazione fornitore
					rigaImp[i] = oggettoDettVO.getCodice2();
					if (i > 0) {
						for (int j = 0; j < i; j++) {
							if (rigaImp[i].equals(rigaImp[j])) {
								throw new ValidationException(
										"duplicazioneFornitore",
										ValidationExceptionCodici.duplicazioneFornitore);
							}
						}
					}
					// fine controllo duplicazione

				}
			} else {
				// almeno una riga
				throw new ValidationException("fatturaRigheAssenti",
						ValidationExceptionCodici.fatturaRigheAssenti);

			}

		} else {
			// almeno una riga
			throw new ValidationException("fatturaRigheAssenti",
					ValidationExceptionCodici.buonoOdineRigheOrdineAssenti);

		}

	}

	@SuppressWarnings("unused")
	public static void ValidaFatturaVO(FatturaVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}
		if (oggettoVO.getAnnoFattura() != null
				&& oggettoVO.getAnnoFattura().trim().length() == 0) {
			throw new ValidationException(
					"fatturaerroreCampoAnnoFatturaObbligatorio",
					ValidationExceptionCodici.fatturaerroreCampoAnnoFatturaObbligatorio);
		}
		if (oggettoVO.getDataRegFattura() != null
				&& oggettoVO.getDataRegFattura().trim().length() == 0) {
			throw new ValidationException(
					"fatturaerroreCampoDataRegObbligatorio",
					ValidationExceptionCodici.fatturaerroreCampoDataRegObbligatorio);
		}
		if (oggettoVO.getTipoFattura() != null
				&& oggettoVO.getTipoFattura().trim().length() == 0) {
			throw new ValidationException(
					"fatturaerroreCampoTipoObbligatorio",
					ValidationExceptionCodici.fatturaerroreCampoTipoObbligatorio);
		}
		if (oggettoVO.getDataFattura() != null
				&& oggettoVO.getDataFattura().trim().length() == 0) {
			throw new ValidationException("fatturaerroreDataFattObbligatorio",
					ValidationExceptionCodici.fatturaerroreDataFattObbligatorio);
		}
		if (oggettoVO.getNumFattura() != null
				&& oggettoVO.getNumFattura().trim().length() == 0) {
			throw new ValidationException("fatturaerroreNumFattObbligatorio",
					ValidationExceptionCodici.fatturaerroreNumFattObbligatorio);
		}
		if (!oggettoVO.isFatturaVeloce()) {
			if (String.valueOf(oggettoVO.getImportoFattura()) != null
					&& (String.valueOf(oggettoVO.getImportoFattura()).trim()
							.length() == 0)
					|| oggettoVO.getImportoFattura() == 0) {
				throw new ValidationException(
						"fatturaerroreImportoFattObbligatorio",
						ValidationExceptionCodici.fatturaerroreImportoFattObbligatorio);
			}
		}

		if (oggettoVO.isFatturaVeloce()
				&& String.valueOf(oggettoVO.getImportoFattura()) == null) {
			throw new ValidationException(
					"fatturaerroreImportoFattObbligatorio",
					ValidationExceptionCodici.fatturaerroreImportoFattObbligatorio);
		}

		if (String.valueOf(oggettoVO.getScontoFattura()) != null
				&& String.valueOf(oggettoVO.getScontoFattura()).trim().length() == 0) {
			throw new ValidationException(
					"fatturaerroreScontoFattObbligatorio",
					ValidationExceptionCodici.fatturaerroreScontoFattObbligatorio);
		}
		if (oggettoVO.getValutaFattura() != null
				&& oggettoVO.getValutaFattura().trim().length() == 0) {
			throw new ValidationException("fatturaerroreValutaObbligatorio",
					ValidationExceptionCodici.fatturaerroreValutaObbligatorio);
		}
		if (oggettoVO.getStatoFattura() != null
				&& oggettoVO.getStatoFattura().trim().length() == 0) {
			throw new ValidationException("fatturaerroreStatoObbligatorio",
					ValidationExceptionCodici.fatturaerroreStatoObbligatorio);
		}
		if (oggettoVO.getFornitoreFattura() != null
				&& oggettoVO.getFornitoreFattura().getCodice() != null
				&& oggettoVO.getFornitoreFattura().getCodice().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoFornitoreObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoVO.getAnnoFattura() != null
				&& String.valueOf(oggettoVO.getAnnoFattura()).length() != 0) {
			if (!strIsNumeric(oggettoVO.getAnnoFattura())) {
				throw new ValidationException("erroreCampoAnnoNumerico",
						ValidationExceptionCodici.erroreCampoAnnoNumerico);
			}

			if (oggettoVO.getAnnoFattura().length() != 4) {
				throw new ValidationException(
						"erroreCampoAnnoNumericoEccedente",
						ValidationExceptionCodici.erroreCampoAnnoNumericoEccedente);
			}
		}
		if (oggettoVO.getProgrFattura() != null
				&& oggettoVO.getProgrFattura().trim().length() != 0) {

			if (!strIsNumeric(oggettoVO.getProgrFattura())
					&& oggettoVO.getProgrFattura().trim().length() != 0) {
				throw new ValidationException(
						"fatturaerroreCampoProgrNumerico",
						ValidationExceptionCodici.fatturaerroreCampoProgrNumerico);
			}
		}
		if (oggettoVO.getDataRegFattura() != null
				&& oggettoVO.getDataRegFattura().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataRegFattura()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoVO.getTipoFattura() != null
				&& oggettoVO.getTipoFattura().length() != 0) {

			if (strIsNumeric(oggettoVO.getTipoFattura())) {
				throw new ValidationException(
						"fatturaerroreTipoFatturaAlfabetico",
						ValidationExceptionCodici.fatturaerroreTipoFatturaAlfabetico);
			}
			if (oggettoVO.getTipoFattura().length() > 1) {
				throw new ValidationException(
						"fatturaerroreTipoFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreTipoFatturaEccedente);
			}
		}
		if (oggettoVO.getDataFattura() != null
				&& oggettoVO.getDataFattura().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataFattura()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoVO.getNumFattura() != null
				&& oggettoVO.getNumFattura().length() != 0) {

			if (oggettoVO.getNumFattura().length() > 20) {
				throw new ValidationException(
						"fatturaerroreNumFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreNumFatturaEccedente);
			}
		}
		if (strIsAlfabetic(String.valueOf(oggettoVO.getImportoFattura()))) {
			throw new ValidationException("fatturaerroreimportoNumerico",
					ValidationExceptionCodici.fatturaerroreimportoNumerico);

		}
		if (strIsAlfabetic(String.valueOf(oggettoVO.getScontoFattura()))) {
			throw new ValidationException("fatturaerrorescontoNumerico",
					ValidationExceptionCodici.fatturaerrorescontoNumerico);

		}
		if (oggettoVO.getValutaFattura() != null
				&& oggettoVO.getValutaFattura().length() != 0) {
			if (strIsNumeric(oggettoVO.getValutaFattura())) {
				throw new ValidationException(
						"cambierroreCampoValutaAlfabetico",
						ValidationExceptionCodici.cambierroreCampoValutaAlfabetico);
			}

			if (oggettoVO.getValutaFattura().length() > 3) {
				throw new ValidationException(
						"cambierroreCampoValutaEccedente",
						ValidationExceptionCodici.cambierroreCampoValutaEccedente);
			}
		}
		if (oggettoVO.getStatoFattura() != null
				&& oggettoVO.getStatoFattura().length() != 0) {

			if (strIsAlfabetic(String.valueOf(oggettoVO.getStatoFattura()))) {
				throw new ValidationException(
						"fatturaerroreStatoFatturaNumerico",
						ValidationExceptionCodici.fatturaerroreStatoFatturaNumerico);

			}

			if (oggettoVO.getStatoFattura().length() > 1) {
				throw new ValidationException(
						"fatturaerroreStatoFatturaEccedente",
						ValidationExceptionCodici.fatturaerroreStatoFatturaEccedente);
			}
		}

		if (oggettoVO.getFornitoreFattura().getCodice() != null
				&& oggettoVO.getFornitoreFattura().getCodice().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoVO
					.getFornitoreFattura().getCodice()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}
		if (oggettoVO.getFornitoreFattura().getDescrizione() != null
				&& oggettoVO.getFornitoreFattura().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoVO.getFornitoreFattura().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoVO.getFornitoreFattura().getDescrizione().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}
		double totBdg = 0;

		if (oggettoVO.getRigheDettaglioFattura() != null
				&& oggettoVO.getRigheDettaglioFattura().size() != 0) {
			if (oggettoVO.getRigheDettaglioFattura().size() > 0) {
				// String[] rigaImp={"","",""};
				String[] rigaImp = new String[oggettoVO
						.getRigheDettaglioFattura().size()];
				double singleBdg = 0;
				for (int i = 0; i < oggettoVO.getRigheDettaglioFattura().size(); i++) {
					rigaImp[i] = "";
					StrutturaFatturaVO oggettoDettVO = oggettoVO
							.getRigheDettaglioFattura().get(i);
					if (!oggettoVO.isFatturaVeloce()) {
						if (String.valueOf(oggettoDettVO
								.getImportoRigaFattura()) != null
								&& (String
										.valueOf(
												oggettoDettVO
														.getImportoRigaFattura())
										.trim().length() == 0 || oggettoDettVO
										.getImportoRigaFattura() == 0)) {
							throw new ValidationException(
									"fatturaerroreImportoRigaFattObbligatorio",
									ValidationExceptionCodici.fatturaerroreImportoRigaFattObbligatorio);
						}
					}
					if (oggettoVO.isFatturaVeloce()
							&& String.valueOf(oggettoDettVO
									.getImportoRigaFattura()) == null) {
						throw new ValidationException(
								"fatturaerroreImportoRigaFattObbligatorio",
								ValidationExceptionCodici.fatturaerroreImportoRigaFattObbligatorio);
					}

					// se non sono altre spese l'ordine è obbligatorio
					if (oggettoVO.isGestBil()
							&& oggettoDettVO.getBilancio() != null
							&& oggettoDettVO.getBilancio().getCodice3() != null
							&& !oggettoDettVO.getBilancio().getCodice3().trim()
									.equals("4")) {
						if (oggettoDettVO.getOrdine() != null
								&& oggettoDettVO.getOrdine().getCodice1() != null
								&& oggettoDettVO.getOrdine().getCodice1()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaOrdineTipoObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaOrdineTipoObbligatorio);
						}
						if (oggettoDettVO.getOrdine() != null
								&& String.valueOf(
										oggettoDettVO.getOrdine().getCodice2())
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaOrdineAnnoObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaOrdineAnnoObbligatorio);
						}
						if (oggettoDettVO.getOrdine() != null
								&& oggettoDettVO.getOrdine().getCodice3() != null
								&& oggettoDettVO.getOrdine().getCodice3()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaOrdineCodiceObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaOrdineCodiceObbligatorio);
						}

					}
					// nel caso di altre spese l'ordine non è obbligatorio ma il
					// bilancio si
					if (oggettoVO.isGestBil()
							&& oggettoDettVO.getBilancio() != null
							&& oggettoDettVO.getBilancio().getCodice3() != null
							&& oggettoDettVO.getBilancio().getCodice3().trim()
									.equals("4")) {
						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& oggettoDettVO.getBilancio().getCodice1() != null
								&& oggettoDettVO.getBilancio().getCodice1()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioEsercizioObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioEsercizioObbligatorio);
						}

						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& oggettoDettVO.getBilancio().getCodice2() != null
								&& oggettoDettVO.getBilancio().getCodice2()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioCapitoloObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioCapitoloObbligatorio);
						}
						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& String.valueOf(
										oggettoDettVO.getBilancio()
												.getCodice3()).length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioTipoImpObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioTipoImpObbligatorio);
						}

					}

					// per alcune tipologie di ordine non è obbligatorio il
					// bilancio

					if (oggettoDettVO.getOrdine() != null
							&& oggettoDettVO.getOrdine().getCodice1() != null
							&& (oggettoDettVO.getOrdine().getCodice1()
									.equals("A")
									|| oggettoDettVO.getOrdine().getCodice1()
											.equals("V") || oggettoDettVO
									.getOrdine().getCodice1().equals("R"))) {
						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& oggettoDettVO.getBilancio().getCodice1() != null
								&& oggettoDettVO.getBilancio().getCodice1()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioEsercizioObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioEsercizioObbligatorio);
						}

						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& oggettoDettVO.getBilancio().getCodice2() != null
								&& oggettoDettVO.getBilancio().getCodice2()
										.length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioCapitoloObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioCapitoloObbligatorio);
						}
						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio() != null
								&& String.valueOf(
										oggettoDettVO.getBilancio()
												.getCodice3()).length() == 0) {
							throw new ValidationException(
									"fatturaerroreRigaBilancioTipoImpObbligatorio",
									ValidationExceptionCodici.fatturaerroreRigaBilancioTipoImpObbligatorio);
						}

					}
					if (oggettoVO.isGestBil()
							&& oggettoDettVO.getBilancio() != null
							&& oggettoDettVO.getBilancio().getCodice1() != null
							&& oggettoDettVO.getBilancio().getCodice1().trim()
									.length() > 0) {
						if (strIsAlfabetic(String.valueOf(oggettoDettVO
								.getBilancio().getCodice1()))) {
							throw new ValidationException(
									"ordinierroreCampoEsercizioNumerico",
									ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);

						}
						if (oggettoVO.isGestBil()
								&& oggettoDettVO.getBilancio().getCodice1()
										.length() != 4) {
							// lungh eserc
							throw new ValidationException(
									"ordinierroreCampoEsercizioEccedente",
									ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
						}
					}

					if (oggettoVO.isGestBil()
							&& oggettoDettVO.getBilancio() != null
							&& oggettoDettVO.getBilancio().getCodice2() != null
							&& oggettoDettVO.getBilancio().getCodice2().trim()
									.length() > 0) {

						if (strIsAlfabetic(String.valueOf(oggettoDettVO
								.getBilancio().getCodice2()))) {
							throw new ValidationException(
									"ordinierroreCampoCapitoloNumerico",
									ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);

						}
						if (oggettoDettVO.getBilancio().getCodice2().length() > 16) {
							throw new ValidationException(
									"ordinierroreCampoCapitoloEccedente",
									ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
						}

					}
					if (oggettoVO.isGestBil()
							&& oggettoDettVO.getBilancio() != null
							&& strIsAlfabetic(String.valueOf(oggettoDettVO
									.getBilancio().getCodice3()))) {
						throw new ValidationException(
								"ordineerroreBilImpegnatoNumerico",
								ValidationExceptionCodici.ordineerroreBilImpegnatoNumerico);

					}

					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getImportoRigaFattura()))) {
						throw new ValidationException(
								"fatturaerroreimportorigaNumerico",
								ValidationExceptionCodici.fatturaerroreimportorigaNumerico);

					}
					double importo = 0;
					double sconto1 = 0;
					double sconto2 = 0;
					double sconto3 = 0;
					double importo1 = 0; // (importo scontato 1)
					double importo2 = 0; // (importo scontato 2)
					importo = oggettoDettVO.getImportoRigaFattura();
					sconto1 = oggettoDettVO.getImportoRigaFattura()
							* oggettoDettVO.getSconto1RigaFattura() / 100;
					importo1 = oggettoDettVO.getImportoRigaFattura() - sconto1;
					sconto2 = importo1 * oggettoDettVO.getSconto2RigaFattura()
							/ 100;
					importo2 = importo1 - sconto2;
					sconto3 = importo2 * oggettoVO.getScontoFattura() / 100;
					singleBdg = importo2 - sconto3;

					totBdg = totBdg + singleBdg;

					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getSconto1RigaFattura()))) {
						throw new ValidationException(
								"fatturaerrorescontoNumerico",
								ValidationExceptionCodici.fatturaerrorescontoNumerico);

					}

					if (strIsAlfabetic(String.valueOf(oggettoDettVO
							.getSconto2RigaFattura()))) {
						throw new ValidationException(
								"fatturaerrorescontoNumerico",
								ValidationExceptionCodici.fatturaerrorescontoNumerico);

					}

					if (oggettoDettVO.getOrdine() != null
							&& oggettoDettVO.getOrdine().getCodice1() != null
							&& oggettoDettVO.getOrdine().getCodice1().length() != 0) {
						if (strIsNumeric(oggettoDettVO.getOrdine().getCodice1())) {
							throw new ValidationException(
									"ordinierroreCampoTipoOrdineAlfabetico",
									ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
						}
						if (oggettoDettVO.getOrdine().getCodice1().length() != 1) {
							throw new ValidationException(
									"ordinierroreCampoTipoOrdineEccedente",
									ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
						}
					}
					if (oggettoDettVO.getOrdine() != null
							&& String.valueOf(
									oggettoDettVO.getOrdine().getCodice2())
									.length() != 0) {
						if (!strIsNumeric(oggettoDettVO.getOrdine()
								.getCodice2().trim())) {
							throw new ValidationException(
									"sezioneerroreCampoAnnoOrdineNumerico",
									ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
						}

						if (oggettoDettVO.getOrdine().getCodice2().length() != 4) {
							throw new ValidationException(
									"sezioneerroreCampoAnnoOrdineEccedente",
									ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
						}
					}
					if (oggettoDettVO.getOrdine() != null
							&& oggettoDettVO.getOrdine().getCodice3() != null
							&& oggettoDettVO.getOrdine().getCodice3().length() != 0) {
						if (!strIsNumeric(String.valueOf(oggettoDettVO
								.getOrdine().getCodice3().trim()))) {
							throw new ValidationException(
									"sezioneerroreCampoCodiceOrdineNumerico",
									ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineNumerico);
						}
					}
					if (oggettoDettVO.getNoteRigaFattura() != null
							&& oggettoDettVO.getNoteRigaFattura().trim()
									.length() != 0) {
						if (oggettoDettVO.getNoteRigaFattura().trim().length() > 160) {
							throw new ValidationException(
									"ordinierroreCampoNoteFornEccedente",
									ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
						}
					}

				}
			} else {
				// almeno una riga
				throw new ValidationException("fatturaRigheAssenti",
						ValidationExceptionCodici.fatturaRigheAssenti);

			}

		} else {
			// almeno una riga
			throw new ValidationException("fatturaRigheAssenti",
					ValidationExceptionCodici.fatturaRigheAssenti);

		}
		BigDecimal bd = new BigDecimal(totBdg);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		totBdg = bd.doubleValue();

		// AGGIUNTO IL CONTROLLO SULLO STATO VEDI EMAIL ROSSANA DEL 10.07.08

		if (oggettoVO.getImportoFattura() < totBdg
				&& !oggettoVO.getStatoFattura().equals("1")) {
			throw new ValidationException("fatturaTotincongruenza",
					ValidationExceptionCodici.fatturaTotincongruenza);

		}
	}

	public static void ValidaRicercaFornitori(
			List<FornitoreVO> listaFornitori) throws ValidationException {
		//
		if (listaFornitori == null || listaFornitori.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppOrdiniVO(ListaSuppOrdiniVO oggettoLista)
			throws ValidationException {

		// ALMENO UN CRITERIO DI RICERCA solo per ricerche da menu principali e
		// non da lista supporto
		boolean criteriMinimiEsistenza = false;

		if (oggettoLista.getTipoOrdine() != null
				&& oggettoLista.getTipoOrdine().length() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getNaturaOrdine() != null
				&& oggettoLista.getNaturaOrdine().length() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getBidList() != null
				&& oggettoLista.getBidList().size() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getIdOrdList() != null
				&& oggettoLista.getIdOrdList().size() != 0) {
			criteriMinimiEsistenza = true;
		}

		if (oggettoLista.getCodBiblAffil() != null
				&& oggettoLista.getCodBiblAffil().length() != 0) {
			criteriMinimiEsistenza = true;
		}

		if (oggettoLista.getTitolo() != null
				&& oggettoLista.getTitolo().getCodice() != null
				&& oggettoLista.getTitolo().getCodice().length() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getStatoOrdine() != null
				&& oggettoLista.getStatoOrdine().length() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getStatoOrdineArr() != null
				&& oggettoLista.getStatoOrdineArr().length != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getAnnoOrdine() != null
				&& oggettoLista.getAnnoOrdine().trim().length() != 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getIDOrd() > 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getBilancio() != null) {
			if (oggettoLista.getBilancio().getCodice1() != null
					&& oggettoLista.getBilancio().getCodice1().trim().length() > 0) {
				criteriMinimiEsistenza = true;
			}
			if (oggettoLista.getBilancio().getCodice2() != null
					&& oggettoLista.getBilancio().getCodice2().trim().length() > 0) {
				criteriMinimiEsistenza = true;
			}
			if (oggettoLista.getBilancio().getCodice3() != null
					&& oggettoLista.getBilancio().getCodice3().trim().length() > 0) {
				criteriMinimiEsistenza = true;
			}
		}

		if (oggettoLista.getContinuativo() != null
				&& oggettoLista.getContinuativo().trim().length() > 0) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.isRinnovato()) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.isStampato()) {
			criteriMinimiEsistenza = true;
		}
		if (oggettoLista.getTipoInvioOrdine() != null
				&& oggettoLista.getTipoInvioOrdine().trim().length() > 0) {
			criteriMinimiEsistenza = true;
		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getDataOrdineDa() != null
				&& oggettoLista.getDataOrdineDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataOrdineDa())
					&& oggettoLista.getDataOrdineDa().length() == 4) {
				String strAnnata = oggettoLista.getDataOrdineDa();
				oggettoLista.setDataOrdineDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataOrdineDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}
		if (oggettoLista.getDataOrdineA() != null
				&& oggettoLista.getDataOrdineA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataOrdineA())
					&& oggettoLista.getDataOrdineA().length() == 4) {
				String strAnnata = oggettoLista.getDataOrdineA();
				oggettoLista.setDataOrdineA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataOrdineA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}
		if (oggettoLista.getDataStampaOrdineDa() != null
				&& oggettoLista.getDataStampaOrdineDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataStampaOrdineDa())
					&& oggettoLista.getDataStampaOrdineDa().length() == 4) {
				String strAnnata = oggettoLista.getDataStampaOrdineDa();
				oggettoLista.setDataStampaOrdineDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataStampaOrdineDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getDataStampaOrdineA() != null
				&& oggettoLista.getDataStampaOrdineA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataStampaOrdineA())
					&& oggettoLista.getDataStampaOrdineA().length() == 4) {
				String strAnnata = oggettoLista.getDataStampaOrdineA();
				oggettoLista.setDataStampaOrdineA("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataStampaOrdineA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getDataFineAbbOrdineDa() != null
				&& oggettoLista.getDataFineAbbOrdineDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataFineAbbOrdineDa())
					&& oggettoLista.getDataFineAbbOrdineDa().length() == 4) {
				String strAnnata = oggettoLista.getDataFineAbbOrdineDa();
				oggettoLista.setDataFineAbbOrdineDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataFineAbbOrdineDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}
		if (oggettoLista.getDataFineAbbOrdineA() != null
				&& oggettoLista.getDataFineAbbOrdineA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataFineAbbOrdineA())
					&& oggettoLista.getDataFineAbbOrdineA().length() == 4) {
				String strAnnata = oggettoLista.getDataFineAbbOrdineA();
				oggettoLista.setDataFineAbbOrdineA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataFineAbbOrdineA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getBilancio() != null
				&& oggettoLista.getBilancio().getCodice1() != null
				&& oggettoLista.getBilancio().getCodice1().length() != 0) {
			if (!strIsNumeric(oggettoLista.getBilancio().getCodice1())
					&& oggettoLista.getBilancio().getCodice1().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioNumerico",
						ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
			}
			if (oggettoLista.getBilancio().getCodice1().length() != 4) {
				throw new ValidationException(
						"ordinierroreCampoEsercizioEccedente",
						ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getBilancio() != null
				&& oggettoLista.getBilancio().getCodice2() != null
				&& oggettoLista.getBilancio().getCodice2().length() != 0) {
			if (!strIsNumeric(oggettoLista.getBilancio().getCodice2())
					&& oggettoLista.getBilancio().getCodice2().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloNumerico",
						ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
			}
			if (oggettoLista.getBilancio().getCodice2().length() > 16) {
				throw new ValidationException(
						"ordinierroreCampoCapitoloEccedente",
						ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getCodOrdine() != null
				&& oggettoLista.getCodOrdine().length() != 0) {
			if (!strIsNumeric(oggettoLista.getCodOrdine())
					&& oggettoLista.getCodOrdine().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoNumeroNumerico",
						ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getCodice() != null
				&& oggettoLista.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(oggettoLista.getFornitore().getCodice())
					&& oggettoLista.getFornitore().getCodice().trim().length() != 0) {
				throw new ValidationException(
						"ordinierroreCampoCodFornitoreNumerico",
						ValidationExceptionCodici.ordinierroreCampoCodFornitoreNumerico);
			}
			criteriMinimiEsistenza = true;

		}

		if (oggettoLista.getSezioneAcqOrdine() != null
				&& oggettoLista.getSezioneAcqOrdine().length() != 0) {

			if (oggettoLista.getSezioneAcqOrdine().length() > 7) {
				throw new ValidationException(
						"ordinierroreCodSezioneEccedente",
						ValidationExceptionCodici.ordinierroreCodSezioneEccedente);
			}
			criteriMinimiEsistenza = true;

		}

		InventarioVO inv = oggettoLista.getInventarioCollegato();
		if (inv != null)
			criteriMinimiEsistenza = true;

		if (!criteriMinimiEsistenza) {
			throw new ValidationException("ricercaDaRaffinare",
					ValidationExceptionCodici.ricercaDaRaffinare);
		}
	}

	public static void ValidaRicercaOrdini(List<OrdiniVO> listaOrdini)
			throws ValidationException {
		//
		if (listaOrdini == null || listaOrdini.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}

	}

	@SuppressWarnings("unused")
	public static int ValidaPreRicercaOrdini(GenericJDBCAcquisizioniDAO dao, ListaSuppOrdiniVO ricercaOrdini)
			throws ValidationException {
		//
		ValidaListaSuppOrdiniVO(ricercaOrdini);
		List<OrdiniVO> listaOrdini = new ArrayList<OrdiniVO>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numRighe = 0;

		try {
			// ricercaOrdini contiene i criteri di ricerca
			connection = dao.getConnection();

			ConfigurazioneBOVO configBO = new ConfigurazioneBOVO();
			configBO.setCodBibl(ricercaOrdini.getCodBibl());
			configBO.setCodPolo(ricercaOrdini.getCodPolo());

			String tipoRinnovoConfigurato = "";
			try {
				ConfigurazioneBOVO configBOletto = new ConfigurazioneBOVO();
				configBOletto = dao.loadConfigurazione(configBO);
				if (configBOletto != null) {
					tipoRinnovoConfigurato = configBOletto.getTipoRinnovo();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// l'errore capita in questo punto
				// 04.12.08 log.error("", e);
				log.error(e);
			}

			ConfigurazioneORDVO configurazioneORD = new ConfigurazioneORDVO();
			configurazioneORD.setCodBibl(ricercaOrdini.getCodBibl());
			configurazioneORD.setCodPolo(ricercaOrdini.getCodPolo());
			ConfigurazioneORDVO configurazioneORDLetta = new ConfigurazioneORDVO();
			Boolean gestBil = true;
			Boolean gestSez = true;
			Boolean gestProf = true;
			try {
				configurazioneORDLetta = dao.loadConfigurazioneOrdini(configurazioneORD);
				if (configurazioneORDLetta != null
						&& !configurazioneORDLetta.isGestioneBilancio()) {
					gestBil = false;
				}
				if (configurazioneORDLetta != null
						&& !configurazioneORDLetta.isGestioneSezione()) {
					gestSez = false;
				}
				if (configurazioneORDLetta != null
						&& !configurazioneORDLetta.isGestioneProfilo()) {
					gestProf = false;
				}

			} catch (Exception e) {
				// l'errore capita in questo punto
				log.error("", e);
			}

			// String
			// sql="select ord.*,  forn.nom_fornitore, tit.isbd, TO_CHAR(ord.data_ord,'dd/MM/yyyy') as data_ord_str,TO_CHAR(ord.data_fine,'dd/MM/yyyy') as data_fine_str, TO_CHAR(ord.data_fasc,'dd/MM/yyyy') as  data_fasc_str  from tba_ordini  ord ";
			String sql = "select count (distinct ord.id_ordine ) as totaleOrdini";
			sql = sql + " from tba_ordini  ord ";

			if (ricercaOrdini.getCodBiblAffil() != null
					&& ricercaOrdini.getCodBiblAffil().length() != 0) {
				sql = sql
						+ " join tra_ordini_biblioteche ordBibl on ordBibl.cod_bib_ord=ord.cd_bib and ordBibl.fl_canc='N' and  ordBibl.cod_tip_ord=ord.cod_tip_ord and ordBibl.anno_ord=ord.anno_ord and ordBibl.cod_ord=ord.cod_ord ";
			}
			sql = sql
					+ " left join tbr_fornitori forn on forn.cod_fornitore=ord.cod_fornitore and forn.fl_canc='N'";
			sql = sql
					+ " left join tba_sez_acquis_bibliografiche sez on sez.id_sez_acquis_bibliografiche=ord.id_sez_acquis_bibliografiche and sez.fl_canc='N' ";
			sql = sql
					+ " left join tbb_capitoli_bilanci capBil on  capBil.id_capitoli_bilanci=ord.id_capitoli_bilanci and capBil.fl_canc='N' ";
			sql = sql
					+ " left join tbb_bilanci bil on bil.id_capitoli_bilanci=capBil.id_capitoli_bilanci and bil.fl_canc='N' ";
			sql = sql
					+ " left join tba_cambi_ufficiali val on val.id_valuta=ord.id_valuta  and  val.fl_canc='N'";
			sql = sql + " where ord.fl_canc='N'";

			if (ricercaOrdini.getBidList() != null
					&& ricercaOrdini.getBidList().size() != 0) {
				Boolean aggiungiSQL = false;
				String sqla = "";
				sqla = sqla + " ( ";
				// sql=sql + " ord.stato_ordine='" +
				// ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getBidList().size(); index++) {
					String so = ricercaOrdini.getBidList().get(index);
					if (so != null && so.length() != 0) {
						if (!sqla.equals(" ( ")) {
							sqla = sqla + " OR ";
						}
						sqla = sqla + " upper(ord.bid)=?";
						aggiungiSQL = true;
					}
				}
				sqla = sqla + " ) ";
				if (aggiungiSQL) {
					sql = struttura(sql);
					sql = sql + sqla;
				}
			}

			if (ricercaOrdini.getIdOrdList() != null
					&& ricercaOrdini.getIdOrdList().size() != 0) {
				Boolean aggiungiSQL = false;
				String sqla = "";
				sqla = sqla + " ( ";
				// sql=sql + " ord.stato_ordine='" +
				// ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getIdOrdList().size(); index++) {
					Integer so = ricercaOrdini.getIdOrdList().get(index);
					if (so > 0) {
						if (!sqla.equals(" ( ")) {
							sqla = sqla + " OR ";
						}
						sqla = sqla + " ord.id_ordine=?";
						aggiungiSQL = true;
					}
				}
				sqla = sqla + " ) ";
				if (aggiungiSQL) {
					sql = struttura(sql);
					sql = sql + sqla;
				}
			}

			if (ricercaOrdini.getCodBibl() != null
					&& ricercaOrdini.getCodBibl().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.cd_bib= ?";
			}

			if (ricercaOrdini.getCodBiblAffil() != null
					&& ricercaOrdini.getCodBiblAffil().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ordBibl.cd_bib=?";
			}

			if (ricercaOrdini.getCodPolo() != null
					&& ricercaOrdini.getCodPolo().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.cd_polo= ?";

			}

			if (ricercaOrdini.getIDOrd() > 0) {
				sql = struttura(sql);
				sql = sql + " ord.id_ordine=?";

			}
			if (ricercaOrdini.getCodOrdine() != null
					&& ricercaOrdini.getCodOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.cod_ord=?";
			}
			if (ricercaOrdini.getTipoOrdine() != null
					&& ricercaOrdini.getTipoOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.cod_tip_ord=?";
			}
			if (ricercaOrdini.getAnnoOrdine() != null
					&& ricercaOrdini.getAnnoOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.anno_ord =? ";
			}
			if (ricercaOrdini.getNaturaOrdine() != null
					&& ricercaOrdini.getNaturaOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.natura=?";
			}
			if (ricercaOrdini.getStatoOrdine() != null
					&& ricercaOrdini.getStatoOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.stato_ordine=?";
			}
			if (ricercaOrdini.getSezioneAcqOrdine() != null
					&& ricercaOrdini.getSezioneAcqOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " upper(sez.cod_sezione)=?";
			}

			if (ricercaOrdini.getStatoOrdineArr() != null
					&& ricercaOrdini.getStatoOrdineArr().length != 0) {
				Boolean aggiungiSQL = false;
				String sqla = "";
				sqla = sqla + " ( ";
				// sql=sql + " ord.stato_ordine='" +
				// ricercaOrdini.getStatoOrdine() +"'";
				for (int index = 0; index < ricercaOrdini.getStatoOrdineArr().length; index++) {
					String so = ricercaOrdini.getStatoOrdineArr()[index];
					if (so != null && so.length() != 0) {
						if (!sqla.equals(" ( ")) {
							sqla = sqla + " OR ";
						}
						sqla = sqla + " ord.stato_ordine=?";
						aggiungiSQL = true;
					}
				}
				sqla = sqla + " ) ";
				if (aggiungiSQL) {
					sql = struttura(sql);
					sql = sql + sqla;
				}
			}

			if (ricercaOrdini.getTipoOrdineArr() != null
					&& ricercaOrdini.getTipoOrdineArr().length != 0) {
				Boolean aggiungiSQL = false;
				String sqla = "";
				sqla = sqla + " ( ";
				for (int index = 0; index < ricercaOrdini.getTipoOrdineArr().length; index++) {
					String so = ricercaOrdini.getTipoOrdineArr()[index];
					if (so != null && so.length() != 0) {
						if (!sqla.equals(" ( ")) {
							sqla = sqla + " OR ";
						}
						sqla = sqla + " ord.cod_tip_ord=?";
						aggiungiSQL = true;
					}
				}
				sqla = sqla + " ) ";
				if (aggiungiSQL) {
					sql = struttura(sql);
					sql = sql + sqla;
				}
			}

			// LA RICERCA PER ID DEVE ESCLUDERE IL CRITERIO SU STAMPATO se
			// attivato dalla maschera di ricerca
			if (ricercaOrdini.isAttivatoDaRicerca()) {
				sql = struttura(sql);
				sql = sql + " ord.rinnovato=? ";

				sql = struttura(sql);
				sql = sql + " ord.stampato=? ";
			} else {
				if (ricercaOrdini.isRinnovato()) {
					sql = struttura(sql);
					sql = sql + " ord.rinnovato=? ";
				}

				if (ricercaOrdini.isStampato()) {
					sql = struttura(sql);
					sql = sql + " ord.stampato=? ";
				}
			}

			if (ricercaOrdini.getDataOrdineDa() != null
					&& ricercaOrdini.getDataOrdineDa().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.data_ord >= TO_DATE(?,'dd/MM/yyyy')";
			}
			if (ricercaOrdini.getDataStampaOrdineDa() != null
					&& ricercaOrdini.getDataStampaOrdineDa().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.stampato=? ";
				sql = struttura(sql);
				sql = sql + " ord.data_ord >= TO_DATE(?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataOrdineA() != null
					&& ricercaOrdini.getDataOrdineA().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.data_ord <= TO_DATE (?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataStampaOrdineA() != null
					&& ricercaOrdini.getDataStampaOrdineA().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.stampato=? ";
				sql = struttura(sql);
				sql = sql + " ord.data_ord <= TO_DATE(?,'dd/MM/yyyy')";
			}
			if (ricercaOrdini.getDataFineAbbOrdineDa() != null
					&& ricercaOrdini.getDataFineAbbOrdineDa().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.data_fine >= TO_DATE (?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getDataFineAbbOrdineA() != null
					&& ricercaOrdini.getDataFineAbbOrdineA().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.data_fine <= TO_DATE (?,'dd/MM/yyyy')";
			}

			if (ricercaOrdini.getTipoInvioOrdine() != null
					&& ricercaOrdini.getTipoInvioOrdine().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.tipo_invio=?";
			}

			if (ricercaOrdini.getContinuativo() != null
					&& ricercaOrdini.getContinuativo().length() != 0
					&& ricercaOrdini.getContinuativo().equals("01")) {
				sql = struttura(sql);
				sql = sql + " ord.continuativo=?";
			}

			if (ricercaOrdini.getContinuativo() != null
					&& ricercaOrdini.getContinuativo().length() != 0
					&& ricercaOrdini.getContinuativo().equals("00")) {
				sql = struttura(sql);
				sql = sql + " ord.continuativo != ?";
			}
			if (ricercaOrdini.getFornitore() != null
					&& ricercaOrdini.getFornitore().getDescrizione() != null
					&& ricercaOrdini.getFornitore().getDescrizione().trim()
							.length() != 0) {
				sql = struttura(sql);
				sql = sql + " forn.nom_fornitore=?";
			}
			if (ricercaOrdini.getFornitore() != null
					&& ricercaOrdini.getFornitore().getCodice() != null
					&& ricercaOrdini.getFornitore().getCodice().length() != 0) {
				sql = struttura(sql);
				sql = sql + " forn.cod_fornitore=?";
			}

			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice1() != null
					&& ricercaOrdini.getBilancio().getCodice1().length() != 0) {
				sql = struttura(sql);
				sql = sql + " capBil.esercizio=?";
			}

			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice2() != null
					&& ricercaOrdini.getBilancio().getCodice2().length() != 0) {
				sql = struttura(sql);
				sql = sql + " capBil.capitolo=?";
			}

			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice3() != null
					&& ricercaOrdini.getBilancio().getCodice3().length() != 0) {
				sql = struttura(sql);
				sql = sql + " ord.tbb_bilancicod_mat=?";
			}
			if (ricercaOrdini.getTitolo() != null
					&& ricercaOrdini.getTitolo().getCodice() != null
					&& ricercaOrdini.getTitolo().getCodice().length() != 0) {
				sql = struttura(sql);
				sql = sql + " upper(ord.bid)=?";
			}

			// almaviva5_20121113 evolutive google
			InventarioVO inv = ricercaOrdini.getInventarioCollegato();
			if (inv != null) {
				sql = struttura(sql);
				StringBuilder buf = new StringBuilder();
				buf.append(" (");
				buf.append("(ord.cod_tip_ord='R' and exists (select 1 from tra_ordine_inventari oi ");
				buf.append(" where oi.id_ordine=ord.id_ordine and oi.cd_polo=? and oi.cd_bib=? and oi.cd_serie=? and oi.cd_inven=?) ");
				buf.append(" ) or (");
				buf.append("(ord.cod_tip_ord<>'R' and exists (select 1 from tbc_inventario i ");
				buf.append(" where i.cd_tip_ord=ord.cod_tip_ord and i.anno_ord=ord.anno_ord and i.cd_ord=ord.cod_ord ");
				buf.append(" and i.fl_canc<>'S' and i.cd_polo=? and i.cd_bib=? and i.cd_serie=? and i.cd_inven=?) ");
				buf.append(") ))");
				sql = sql + buf.toString();
			}

			pstmt = connection.prepareStatement(sql);
			int i = 1;

			if (ricercaOrdini.getBidList() != null
					&& ricercaOrdini.getBidList().size() != 0) {
				for (int index = 0; index < ricercaOrdini.getBidList().size(); index++) {
					String so = ricercaOrdini.getBidList().get(index);
					if (so != null && so.length() != 0) {
						pstmt.setString(i, so);
						i = i + 1;
					}
				}
			}

			if (ricercaOrdini.getIdOrdList() != null
					&& ricercaOrdini.getIdOrdList().size() != 0) {
				for (int index = 0; index < ricercaOrdini.getIdOrdList().size(); index++) {
					Integer so = ricercaOrdini.getIdOrdList().get(index);
					if (so > 0) {
						pstmt.setInt(i, so);
						i = i + 1;
					}
				}
			}

			if (ricercaOrdini.getCodBibl() != null
					&& ricercaOrdini.getCodBibl().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getCodBibl());
				i = i + 1;
			}

			if (ricercaOrdini.getCodBiblAffil() != null
					&& ricercaOrdini.getCodBiblAffil().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getCodBiblAffil());
				i = i + 1;
			}

			if (ricercaOrdini.getCodPolo() != null
					&& ricercaOrdini.getCodPolo().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getCodPolo());
				i = i + 1;
			}
			if (ricercaOrdini.getIDOrd() > 0) {
				pstmt.setInt(i, ricercaOrdini.getIDOrd());
				i = i + 1;
			}
			if (ricercaOrdini.getCodOrdine() != null
					&& ricercaOrdini.getCodOrdine().length() != 0) {
				pstmt.setInt(i, Integer.valueOf(ricercaOrdini.getCodOrdine()));
				i = i + 1;
			}
			if (ricercaOrdini.getTipoOrdine() != null
					&& ricercaOrdini.getTipoOrdine().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getTipoOrdine());
				i = i + 1;
			}
			if (ricercaOrdini.getAnnoOrdine() != null
					&& ricercaOrdini.getAnnoOrdine().length() != 0) {
				pstmt.setInt(i, Integer.valueOf(ricercaOrdini.getAnnoOrdine()));
				i = i + 1;
			}
			if (ricercaOrdini.getNaturaOrdine() != null
					&& ricercaOrdini.getNaturaOrdine().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getNaturaOrdine());
				i = i + 1;
			}
			if (ricercaOrdini.getStatoOrdine() != null
					&& ricercaOrdini.getStatoOrdine().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getStatoOrdine());
				i = i + 1;
			}
			if (ricercaOrdini.getSezioneAcqOrdine() != null
					&& ricercaOrdini.getSezioneAcqOrdine().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getSezioneAcqOrdine().trim()
						.toUpperCase());
				i = i + 1;
			}
			if (ricercaOrdini.getStatoOrdineArr() != null
					&& ricercaOrdini.getStatoOrdineArr().length != 0) {
				for (int index = 0; index < ricercaOrdini.getStatoOrdineArr().length; index++) {
					String so = ricercaOrdini.getStatoOrdineArr()[index];
					if (so != null && so.length() != 0) {
						pstmt.setString(i, so);
						i = i + 1;
					}
				}
			}

			if (ricercaOrdini.getTipoOrdineArr() != null
					&& ricercaOrdini.getTipoOrdineArr().length != 0) {
				for (int index = 0; index < ricercaOrdini.getTipoOrdineArr().length; index++) {
					String so = ricercaOrdini.getTipoOrdineArr()[index];
					if (so != null && so.length() != 0) {
						pstmt.setString(i, so);
						i = i + 1;
					}
				}
			}
			// LA RICERCA PER ID DEVE ESCLUDERE IL CRITERIO SU STAMPATO se
			// attivato dalla maschera di ricerca
			if (ricercaOrdini.isAttivatoDaRicerca()) {
				pstmt.setBoolean(i, ricercaOrdini.isRinnovato());
				i = i + 1;

				pstmt.setBoolean(i, ricercaOrdini.isStampato());
				i = i + 1;
			} else {
				if (ricercaOrdini.isRinnovato()) {
					pstmt.setBoolean(i, true);
					i = i + 1;
				}
				if (ricercaOrdini.isStampato()) {
					pstmt.setBoolean(i, true);
					i = i + 1;
				}
			}

			if (ricercaOrdini.getDataOrdineDa() != null
					&& ricercaOrdini.getDataOrdineDa().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getDataOrdineDa());
				i = i + 1;
			}
			if (ricercaOrdini.getDataStampaOrdineDa() != null
					&& ricercaOrdini.getDataStampaOrdineDa().length() != 0) {
				pstmt.setBoolean(i, true);
				i = i + 1;

				pstmt.setString(i, ricercaOrdini.getDataStampaOrdineDa());
				i = i + 1;
			}
			if (ricercaOrdini.getDataOrdineA() != null
					&& ricercaOrdini.getDataOrdineA().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getDataOrdineA());
				i = i + 1;
			}
			if (ricercaOrdini.getDataStampaOrdineA() != null
					&& ricercaOrdini.getDataStampaOrdineA().length() != 0) {
				pstmt.setBoolean(i, true);
				i = i + 1;

				pstmt.setString(i, ricercaOrdini.getDataStampaOrdineA());
				i = i + 1;
			}

			if (ricercaOrdini.getDataFineAbbOrdineDa() != null
					&& ricercaOrdini.getDataFineAbbOrdineDa().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getDataFineAbbOrdineDa());
				i = i + 1;
			}
			if (ricercaOrdini.getDataFineAbbOrdineA() != null
					&& ricercaOrdini.getDataFineAbbOrdineA().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getDataFineAbbOrdineA());
				i = i + 1;
			}
			if (ricercaOrdini.getTipoInvioOrdine() != null
					&& ricercaOrdini.getTipoInvioOrdine().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getTipoInvioOrdine());
				i = i + 1;
			}
			if (ricercaOrdini.getContinuativo() != null
					&& ricercaOrdini.getContinuativo().length() != 0
					&& ricercaOrdini.getContinuativo().equals("01")) {
				pstmt.setString(i, "1");
				i = i + 1;
			}

			if (ricercaOrdini.getContinuativo() != null
					&& ricercaOrdini.getContinuativo().length() != 0
					&& ricercaOrdini.getContinuativo().equals("00")) {
				pstmt.setString(i, "1");
				i = i + 1;

			}
			if (ricercaOrdini.getFornitore() != null
					&& ricercaOrdini.getFornitore().getDescrizione() != null
					&& ricercaOrdini.getFornitore().getDescrizione().trim()
							.length() != 0) {
				pstmt.setString(i, ricercaOrdini.getFornitore()
						.getDescrizione().trim());
				i = i + 1;
			}
			if (ricercaOrdini.getFornitore() != null
					&& ricercaOrdini.getFornitore().getCodice() != null
					&& ricercaOrdini.getFornitore().getCodice().length() != 0) {
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getFornitore()
						.getCodice()));
				i = i + 1;
			}
			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice1() != null
					&& ricercaOrdini.getBilancio().getCodice1().length() != 0) {
				// esercizio
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getBilancio()
						.getCodice1()));
				i = i + 1;

			}
			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice2() != null
					&& ricercaOrdini.getBilancio().getCodice2().length() != 0) {
				// capitolo
				pstmt.setInt(i, Integer.parseInt(ricercaOrdini.getBilancio()
						.getCodice2()));
				i = i + 1;
			}
			if (ricercaOrdini.getBilancio() != null
					&& ricercaOrdini.getBilancio().getCodice3() != null
					&& ricercaOrdini.getBilancio().getCodice3().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getBilancio().getCodice3());
				i = i + 1;
			}
			if (ricercaOrdini.getTitolo() != null
					&& ricercaOrdini.getTitolo().getCodice() != null
					&& ricercaOrdini.getTitolo().getCodice().length() != 0) {
				pstmt.setString(i, ricercaOrdini.getTitolo().getCodice().trim()
						.toUpperCase());
				i = i + 1;
			}

			// almaviva5_20121113 evolutive google
			if (inv != null) {
				pstmt.setString(i++, inv.getCodPolo());
				pstmt.setString(i++, inv.getCodBib());
				pstmt.setString(i++, inv.getCodSerie());
				pstmt.setInt(i++, inv.getCodInvent());

				pstmt.setString(i++, inv.getCodPolo());
				pstmt.setString(i++, inv.getCodBib());
				pstmt.setString(i++, inv.getCodSerie());
				pstmt.setInt(i++, inv.getCodInvent());
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("totaleOrdini") > 0) {
					numRighe = rs.getInt("totaleOrdini");
				}
			}
			rs.close();
			connection.close();

		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			dao.close(connection);
		}

		if (numRighe > 1000) {
			throw new ValidationException("ricercaDaRaffinareTroppi",
					ValidationExceptionCodici.ricercaDaRaffinareTroppi);
		}
		return numRighe;
	}

	public static void ValidaOrdiniVO(OrdiniVO ordine)
			throws ValidationException {

		if (ordine.getCodPolo() == null
				|| (ordine.getCodPolo() != null && ordine.getCodPolo().trim()
						.length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (ordine.getCodPolo().length() > 0) {

			if (ordine.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (ordine.getCodBibl() == null
				|| (ordine.getCodBibl() != null && ordine.getCodBibl().trim()
						.length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (ordine.getCodBibl().length() > 0) {

			/*
			 * if (strIsNumeric(oggettoVO.getCodBibl())) { throw new
			 * ValidationException("ordinierroreCodBiblAlfabetico",
			 * ValidationExceptionCodici.ordinierroreCodBiblAlfabetico); }
			 */
			if (ordine.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
			if (ordine.getStatoOrdine() == null
					|| ordine.getStatoOrdine().trim().length() == 0) {
				// anomalia da bloccare
				ordine.setStatoOrdine("A");
			}

			if (ordine.getTipoOrdine() != null
					&& ordine.getTipoOrdine().length() == 0) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineObbligatorio",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineObbligatorio);
			}

			if (ordine.getNaturaOrdine() != null
					&& ordine.getNaturaOrdine().trim().length() > 0
					&& (!ordine.getNaturaOrdine().equals("C")
							&& !ordine.getNaturaOrdine().equals("M") && !ordine
							.getNaturaOrdine().equals("S"))) {
				throw new ValidationException("naturaNonAmmessa",
						ValidationExceptionCodici.naturaNonAmmessa);
			}

			if (ordine.getTipoOrdine() != null
					&& ordine.getTipoOrdine().length() != 0) {
				if (strIsNumeric(ordine.getTipoOrdine())) {
					throw new ValidationException(
							"ordinierroreCampoTipoOrdineAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
				}
				if (ordine.getTipoOrdine().length() != 1) {
					throw new ValidationException(
							"ordinierroreCampoTipoOrdineEccedente",
							ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
				}
			}
			if (ordine.getValutaOrdine() != null) {
				if (strIsNumeric(ordine.getValutaOrdine())
						&& ordine.getValutaOrdine().trim().length() != 0) {
					throw new ValidationException(
							"cambierroreCampoValutaAlfabetico",
							ValidationExceptionCodici.cambierroreCampoValutaAlfabetico);
				}
				if (ordine.getValutaOrdine().length() > 3) {
					throw new ValidationException(
							"cambierroreCampoValutaEccedente",
							ValidationExceptionCodici.cambierroreCampoValutaEccedente);
				}
			}

			if (ordine.getDataOrdine() != null
					&& ordine.getDataOrdine().length() == 0) {
				throw new ValidationException(
						"ordinierroreCampoDataOrdineObbligatorio",
						ValidationExceptionCodici.ordinierroreCampoDataOrdineObbligatorio);
			}
			if (ordine.getDataOrdine() != null
					&& ordine.getDataOrdine().length() != 0) {
				// controllo congruenza
				if (validaDataPassata(ordine.getDataOrdine()) != 0) {
					throw new ValidationException("erroreData",
							ValidationExceptionCodici.erroreData);
				}
			}
			if (ordine.getTitolo() != null
					&& ordine.getTitolo().getCodice() != null
					&& ordine.getTitolo().getCodice().length() == 0
					&& !ordine.getTipoOrdine().equals("R")) {
				throw new ValidationException(
						"ordinierroreCampoBidObbligatorio",
						ValidationExceptionCodici.ordinierroreCampoBidObbligatorio);
			}
			if (ordine.getTitolo() != null
					&& ordine.getTitolo().getCodice() != null
					&& ordine.getTitolo().getCodice().length() != 0) {
				if (strIsNumeric(ordine.getTitolo().getCodice())) {
					throw new ValidationException(
							"ordinierroreCampoBidCodiceAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoBidCodiceAlfabetico);
				}
				if (ordine.getTitolo().getCodice().length() > 10) {
					throw new ValidationException(
							"ordinierroreCampoBidCodiceEccedente",
							ValidationExceptionCodici.ordinierroreCampoBidCodiceEccedente);
				}
			}
			if (ordine.getTitolo() != null
					&& ordine.getTitolo().getDescrizione() != null
					&& ordine.getTitolo().getDescrizione().length() != 0) {
				if (strIsNumeric(ordine.getTitolo().getDescrizione())) {
					throw new ValidationException(
							"ordinierroreCampoBidDescrizioneAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoBidDescrizioneAlfabetico);
				}
				if (ordine.getTitolo().getDescrizione().length() > 1200) {
					throw new ValidationException(
							"ordinierroreCampoBidDescrizioneEccedente",
							ValidationExceptionCodici.ordinierroreCampoBidDescrizioneEccedente);
				}
			}
			if (ordine.getFornitore() != null
					&& ordine.getFornitore().getCodice() != null
					&& ordine.getFornitore().getCodice().length() == 0) {
				throw new ValidationException(
						"ordinierroreCampoFornitoreObbligatorio",
						ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
			}
			if (ordine.getFornitore() != null
					&& ordine.getFornitore().getCodice() != null
					&& ordine.getFornitore().getCodice().length() != 0) {
				if (!strIsNumeric(ordine.getFornitore().getCodice())) {
					throw new ValidationException(
							"ordinierroreCampoFornitoreCodiceNumerico",
							ValidationExceptionCodici.ordinierroreCampoFornitoreCodiceNumerico);
				}
				if (ordine.getFornitore() != null
						&& ordine.getFornitore().getCodice().length() > 10) {
					throw new ValidationException(
							"ordinierroreCampoFornitoreCodiceEccedente",
							ValidationExceptionCodici.ordinierroreCampoFornitoreCodiceEccedente);
				}
			}
			if (ordine.getFornitore() != null
					&& ordine.getFornitore().getDescrizione() != null
					&& ordine.getFornitore().getDescrizione().length() != 0) {
				if (strIsNumeric(ordine.getFornitore().getDescrizione())) {
					throw new ValidationException(
							"ordinierroreCampoFornitoreDescrizioneAlfabetico",
							ValidationExceptionCodici.ordinierroreCampoFornitoreDescrizioneAlfabetico);
				}
				if (ordine.getFornitore() != null
						&& ordine.getFornitore().getDescrizione().length() > 50) {
					throw new ValidationException(
							"ordinierroreCampoFornitoreDescrizioneEccedente",
							ValidationExceptionCodici.ordinierroreCampoFornitoreDescrizioneEccedente);
				}
			}

			if (ordine.getTipoOrdine().equals("A")
					|| ordine.getTipoOrdine().equals("V")
					|| ordine.getTipoOrdine().equals("R")) {
				if (ordine.isGestBil() && ordine.getBilancio() != null
						&& ordine.getBilancio().getCodice1() != null
						&& ordine.getBilancio().getCodice1().length() == 0) {
					throw new ValidationException(
							"ordinierroreCampoBilancioEsercizioObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoBilancioEsercizioObbligatorio);
				}
				if (ordine.isGestBil() && ordine.getBilancio() != null
						&& ordine.getBilancio().getCodice2() != null
						&& ordine.getBilancio().getCodice2().length() == 0) {
					throw new ValidationException(
							"ordinierroreCampoBilancioCapitoloObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoBilancioCapitoloObbligatorio);
				}
				// gestione automatismo tipo impegno in base ad altri valori
				// gestione in assenza di valore // if ( oggettoVO.isGestBil()
				// && oggettoVO.getBilancio()!=null &&
				// oggettoVO.getBilancio().getCodice3()!=null &&
				// oggettoVO.getBilancio().getCodice3().length() == 0)
				// richiesta di aggiornamento in presenza di valore tck n. 2245
				if (ordine.isGestBil() && ordine.getBilancio() != null
						&& ordine.getBilancio().getCodice3() != null) {
					if (ordine.getStatoOrdine() != null
							&& ordine.getStatoOrdine().equals("A")
							&& !ordine.isRiapertura()) {
						if ((ordine.getNaturaOrdine() != null
								&& ordine.getNaturaOrdine().equals("S") || ordine
								.getNaturaOrdine().equals("M"))
								&& !ordine.isContinuativo()) {
							// ordine
							if (!ordine.getBilancio().getCodice3().equals("1")) {
								ordine.getBilancio().setCodice3("1");
								throw new ValidationException(
										"ordinierroreAutomatismoTipoImpegno",
										ValidationExceptionCodici.ordinierroreAutomatismoTipoImpegno);
							}
						}
						if (ordine.getNaturaOrdine() != null
								&& ordine.getNaturaOrdine().equals("S")
								&& ordine.isContinuativo()) {
							// abbonamento
							if (!ordine.getBilancio().getCodice3().equals("2")) {
								ordine.getBilancio().setCodice3("2");
								throw new ValidationException(
										"ordinierroreAutomatismoTipoImpegno",
										ValidationExceptionCodici.ordinierroreAutomatismoTipoImpegno);

							}

						}
						if ((ordine.getNaturaOrdine() != null
								&& ordine.getNaturaOrdine().equals("C") || ordine
								.getNaturaOrdine().equals("M"))
								&& ordine.isContinuativo()) {
							// ordine continuativo
							if (!ordine.getBilancio().getCodice3().equals("3")) {
								ordine.getBilancio().setCodice3("3");
								throw new ValidationException(
										"ordinierroreAutomatismoTipoImpegno",
										ValidationExceptionCodici.ordinierroreAutomatismoTipoImpegno);

							}

						}
					}
				}

				if (ordine.isGestBil() && ordine.getBilancio() != null
						&& ordine.getBilancio().getCodice3() != null
						&& ordine.getBilancio().getCodice3().length() == 0) {
					throw new ValidationException(
							"ordinierroreCampoBilancioImpegnoObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoBilancioImpegnoObbligatorio);
				}

				if (ordine.isGestSez() && ordine.getSezioneAcqOrdine() != null
						&& ordine.getSezioneAcqOrdine().length() == 0
						&& !ordine.getTipoOrdine().equals("R")) {
					throw new ValidationException(
							"ordinierroreCampoSezioneObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoSezioneObbligatorio);
				}
			}
			if (ordine.isGestSez() && ordine.getSezioneAcqOrdine() != null
					&& ordine.getSezioneAcqOrdine().length() != 0) {
				if (ordine.getSezioneAcqOrdine().length() > 7) {
					throw new ValidationException(
							"ordinierroreCampoSezioneEccedente",
							ValidationExceptionCodici.ordinierroreCampoSezioneEccedente);
				}
			}

			if (ordine.isGestBil() && ordine.getBilancio() != null
					&& ordine.getBilancio().getCodice1() != null
					&& ordine.getBilancio().getCodice1().length() != 0) {
				if (!strIsNumeric(ordine.getBilancio().getCodice1())
						&& ordine.getBilancio().getCodice1().trim().length() != 0) {
					throw new ValidationException(
							"ordinierroreCampoEsercizioNumerico",
							ValidationExceptionCodici.ordinierroreCampoEsercizioNumerico);
				}
				if (ordine.getBilancio().getCodice1().length() != 4) {
					throw new ValidationException(
							"ordinierroreCampoEsercizioEccedente",
							ValidationExceptionCodici.ordinierroreCampoEsercizioEccedente);
				}
			}

			if (ordine.isGestBil() && ordine.getBilancio() != null
					&& ordine.getBilancio().getCodice2() != null
					&& ordine.getBilancio().getCodice2().length() != 0) {
				if (!strIsNumeric(ordine.getBilancio().getCodice2())
						&& ordine.getBilancio().getCodice2().trim().length() != 0) {
					throw new ValidationException(
							"ordinierroreCampoCapitoloNumerico",
							ValidationExceptionCodici.ordinierroreCampoCapitoloNumerico);
				}
				if (ordine.getBilancio().getCodice1().length() > 16) {
					throw new ValidationException(
							"ordinierroreCampoCapitoloEccedente",
							ValidationExceptionCodici.ordinierroreCampoCapitoloEccedente);
				}
			}

			if (ordine.isGestBil() && ordine.getBilancio() != null
					&& ordine.getBilancio().getCodice3() != null
					&& ordine.getBilancio().getCodice3().trim().length() != 0) {
				if (ordine.getTipoOrdine().equals("R")
						&& !ordine.getBilancio().getCodice3().trim()
								.equals("4")) {
					throw new ValidationException("tipoImpegnoRilegatura",
							ValidationExceptionCodici.tipoImpegnoRilegatura);
				}
			}

			if (ordine.isGestBil() && ordine.getBilancio() != null
					&& ordine.getBilancio().getCodice3() != null
					&& ordine.getBilancio().getCodice3().trim().length() != 0) {
				if (!ordine.getTipoOrdine().equals("R")
						&& ordine.getBilancio().getCodice3().trim().equals("4")) {
					throw new ValidationException("tipoImpegnoNonRilegatura",
							ValidationExceptionCodici.tipoImpegnoNonRilegatura);
				}
			}

			// CONTROLLO SFORAMENTO DEL BUDGET DI BILANCIO

			if (ordine.getCodOrdine() != null
					&& ordine.getCodOrdine().length() != 0) {
				if (!strIsNumeric(ordine.getCodOrdine())
						&& ordine.getCodOrdine().trim().length() != 0) {
					throw new ValidationException(
							"ordinierroreCampoNumeroNumerico",
							ValidationExceptionCodici.ordinierroreCampoNumeroNumerico);
				}
			}

			if (ordine.getFornitore() != null
					&& ordine.getFornitore().getCodice() != null
					&& ordine.getFornitore().getCodice().length() != 0) {
				if (!strIsNumeric(ordine.getFornitore().getCodice())
						&& ordine.getFornitore().getCodice().trim().length() != 0) {
					throw new ValidationException(
							"ordinierroreCampoCodFornitoreNumerico",
							ValidationExceptionCodici.ordinierroreCampoCodFornitoreNumerico);
				}

			}
			// gestione del tipo rilegatura senza inventari associati tck 3026
			// collaudo

			// gestire il tipo rilegatura con stato CHIUSO e tutti gli inventari
			// rientrati
			if (ordine != null
					&& ordine.getTipoOrdine() != null
					&& (ordine.getDataChiusura() == null || (ordine
							.getDataChiusura() != null && ordine
							.getDataChiusura().trim().length() == 0))
					&& ordine.getStatoOrdine() != null
					&& ordine.getTipoOrdine().equals("R")
					&& ordine.getStatoOrdine().equals("C")) {
				List<StrutturaInventariOrdVO> listaRighe = new ArrayList<StrutturaInventariOrdVO>();
				if (ordine.getRigheInventariRilegatura() != null
						&& ordine.getRigheInventariRilegatura().size() > 0) {
					listaRighe = ordine.getRigheInventariRilegatura();
					boolean tuttiRientrati = true;
					for (int w = 0; w < listaRighe.size(); w++) {
						StrutturaInventariOrdVO elemRiga = listaRighe.get(w);
						if (elemRiga.getDataRientro() == null
								|| (elemRiga.getDataRientro() != null && elemRiga
										.getDataRientro().trim().length() == 0)) {
							tuttiRientrati = false;
							break;
						}
					}
					if (!tuttiRientrati) {
						throw new ValidationException("rilegaturaChiusura",
								ValidationExceptionCodici.rilegaturaChiusura);
					}
				}
			}

			if (ordine.isGestSez() && ordine.getSezioneAcqOrdine() != null
					&& ordine.getSezioneAcqOrdine().trim().length() != 0) {
				if (ordine.getSezioneAcqOrdine().length() > 7) {
					throw new ValidationException(
							"ordinierroreCodSezioneEccedente",
							ValidationExceptionCodici.ordinierroreCodSezioneEccedente);
				}
			}

			// almaviva5_20110328 #4322
			boolean isAbbonamento = ordine.isContinuativo()
					&& ordine.getNaturaOrdine().equals("S")
					&& ordine.getStatoOrdine().equals("A");
			if (isAbbonamento) {
				if (!isFilled(ordine.getPeriodoValAbbOrdine()))
					throw new ValidationException(
							"ordinierroreCampoPeriodoValAbbonObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoPeriodoValAbbonObbligatorio);

				if (!isFilled(ordine
						.getDataPubblFascicoloAbbOrdine()))
					throw new ValidationException(
							"ordinierroreCampoDataPubblFascicoloAbbonObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoDataPubblFascicoloAbbonObbligatorio);

				if (!isFilled(ordine.getDataFineAbbOrdine()))
					throw new ValidationException(
							"ordinierroreCampoDataFineAbbonObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoDataFineAbbonObbligatorio);

				if (!isFilled(ordine.getAnnoAbbOrdine()))
					throw new ValidationException(
							"ordinierroreCampoDataAbbonOrdineObbligatorio",
							ValidationExceptionCodici.ordinierroreCampoDataAbbonOrdineObbligatorio);
			}

			if (isAbbonamento
					&& (ordine.isGestBil() && ordine.getBilancio() != null
							&& ordine.getBilancio().getCodice3() != null && ordine
							.getBilancio().getCodice3().equals("2"))) {
				if (!ordine.isGestBil()
						|| (ordine.isGestBil() && ordine.getBilancio() != null
								&& ordine.getBilancio().getCodice3() != null && ordine
								.getBilancio().getCodice3().equals("2"))) {
					if (ordine.getPeriodoValAbbOrdine() != null
							&& ordine.getPeriodoValAbbOrdine().trim().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoPeriodoValAbbonObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoPeriodoValAbbonObbligatorio);
					}

					if (ordine.getDataFineAbbOrdine() != null
							&& ordine.getDataFineAbbOrdine().trim().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoDataFineAbbonObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoDataFineAbbonObbligatorio);
					}
					if (ordine.getAnnoAbbOrdine() != null
							&& ordine.getAnnoAbbOrdine().trim().length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoDataAbbonOrdineObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoDataAbbonOrdineObbligatorio);
					}

				}

			}

			// anno abbord
			if (ordine.getAnnoAbbOrdine() != null
					&& ordine.getAnnoAbbOrdine().trim().length() != 0
					&& !ordine.getAnnoAbbOrdine().equals("0")) {
				if (!strIsNumeric(ordine.getAnnoAbbOrdine().trim())) {
					throw new ValidationException(
							"sezioneerroreCampoAnnoAbbonamentoNumerico",
							ValidationExceptionCodici.sezioneerroreCampoAnnoAbbonamentoNumerico);
				}

				if (ordine.getAnnoAbbOrdine().length() != 4
						&& ordine.getAnnoAbbOrdine().trim().length() != 0) {
					throw new ValidationException(
							"sezioneerroreCampoAnnoAbbonamentoEccedente",
							ValidationExceptionCodici.sezioneerroreCampoAnnoAbbonamentoEccedente);
				}
			}

			if (ordine.getDataFineAbbOrdine() != null
					&& ordine.getDataFineAbbOrdine().trim().length() != 0) {
				// controllo congruenza
				if (validaDataPassata(ordine.getDataFineAbbOrdine()) != 0) {
					throw new ValidationException("erroreDataFineAbbonamento",
							ValidationExceptionCodici.erroreDataFineAbbonamento);
				}
			}
			if (ordine.getDataPubblFascicoloAbbOrdine() != null
					&& ordine.getDataPubblFascicoloAbbOrdine().trim().length() != 0) {
				// controllo congruenza
				if (validaDataPassata(ordine.getDataPubblFascicoloAbbOrdine()) != 0) {
					throw new ValidationException(
							"erroreDataPubblicazioneFascicolo",
							ValidationExceptionCodici.erroreDataPubblicazioneFascicolo);
				}
			}
			if (ordine.getNoteOrdine() != null
					&& ordine.getNoteOrdine().trim().length() != 0) {
				if (ordine.getNoteOrdine().trim().length() > 160) {
					throw new ValidationException(
							"ordinierroreCampoNoteFornEccedente",
							ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
				}
			}

			if (ordine.getNoteFornitore() != null
					&& ordine.getNoteFornitore().trim().length() != 0) {
				if (ordine.getNoteFornitore().trim().length() > 160) {
					throw new ValidationException(
							"ordinierroreCampoNoteFornEccedente",
							ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
				}
			}

			if (strIsAlfabetic(String.valueOf(ordine.getPrezzoOrdine()))) {
				throw new ValidationException("ordineerrorePrezzoNumerico",
						ValidationExceptionCodici.ordineerrorePrezzoNumerico);
			} else {
				int pos = String.valueOf(ordine.getPrezzoOrdine()).lastIndexOf(
						",");
				if (pos != -1) {
					throw new ValidationException("ordineerroreDecimalPoint",
							ValidationExceptionCodici.ordineerroreDecimalPoint);
				}
			}
			if (strIsAlfabetic(String.valueOf(ordine.getPrezzoEuroOrdine()))) {
				throw new ValidationException("ordineerrorePrezzoNumerico",
						ValidationExceptionCodici.ordineerrorePrezzoNumerico);
			} else {
				int pos = String.valueOf(ordine.getPrezzoEuroOrdine())
						.lastIndexOf(",");
				if (pos != -1) {
					throw new ValidationException("ordineerroreDecimalPoint",
							ValidationExceptionCodici.ordineerroreDecimalPoint);
				}
			}
		}

		// almaviva5_20121107 evolutive google
		if (ordine.getTipoOrdine().equals("R")) {
			if (!isFilled(ordine.getCd_tipo_lav()))
				throw new ValidationException(
						SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD,
						"ordine.label.tipoLav");

			OrdineCarrelloSpedizioneVO ocs = ordine
					.getOrdineCarrelloSpedizione();
			if (ocs != null)
				ocs.validate(new OrdineCarrelloSpedizioneValidator(ordine));
		}

	}

	public static void ValidaFornitoreVO(FornitoreVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoVO.getCodFornitore() != null
				&& oggettoVO.getCodFornitore().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoVO.getCodFornitore()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}

		if (oggettoVO.getNomeFornitore() != null
				&& oggettoVO.getNomeFornitore().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoNomeFornObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoNomeFornObbligatorio);
		}

		if (oggettoVO.getNomeFornitore() != null
				&& oggettoVO.getNomeFornitore().trim().length() != 0) {
			if (strIsNumeric(oggettoVO.getNomeFornitore())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoVO.getNomeFornitore().trim().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}

		if (oggettoVO.getTipoPartner() != null
				&& oggettoVO.getTipoPartner().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoTipoPartnerFornObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoTipoPartnerFornObbligatorio);
		}
		if (oggettoVO.getTipoPartner() != null
				&& oggettoVO.getTipoPartner().length() != 0) {
			if (oggettoVO.getTipoPartner().length() > 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoPartnerFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoPartnerFornEccedente);
			}
		}

		if (oggettoVO.getPaese() != null && oggettoVO.getPaese().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoPaeseFornObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoPaeseFornObbligatorio);
		}
		if (oggettoVO.getPaese() != null && oggettoVO.getPaese().length() != 0) {
			if (oggettoVO.getPaese().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoPaeseFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoPaeseFornEccedente);
			}
		}

		if (oggettoVO.getProvincia() != null
				&& oggettoVO.getProvincia().length() == 0) {
			if (oggettoVO.getPaese() != null
					&& oggettoVO.getPaese().trim().length() > 0
					&& oggettoVO.getPaese().trim().equals("IT")) {
				throw new ValidationException(
						"ordinierroreCampoProvFornObbligatorio",
						ValidationExceptionCodici.ordinierroreCampoProvFornObbligatorio);
			}
		}
		if (oggettoVO.getProvincia() != null
				&& oggettoVO.getProvincia().length() != 0) {
			if (oggettoVO.getProvincia().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoProvFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoProvFornEccedente);
			}
		}

		// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
		// gestione del campo regione e ISBN
		if (oggettoVO.getRegione() != null && oggettoVO.getRegione().length() != 0) {
			if (oggettoVO.getRegione().length() > 2) {
				throw new ValidationException("erroreGenericoAcquisizioni",
						ValidationExceptionCodici.erroreGenericoAcquisizioni);
			}
			// Modifica Settembre 2013- Mail Scognamiglio
			// In inserimento/modifica editore non deve essere possibile inserire un’associazione a numero standard senza valorizzare
			// la stringa n.ro standard. Pima di inserire la relazione tbr_editore_nro_std va inserito un controllo che
			// per il numero standard siano stati indicati almeno 3 chr.
			if (oggettoVO.getListaNumStandard() != null && oggettoVO.getListaNumStandard().size() > 0) {
				for (int i=0; i<oggettoVO.getListaNumStandard().size(); i++)	{
					if (((TabellaNumSTDImpronteVO)oggettoVO.getListaNumStandard().get(i)).getCampoUno().length() < 3) {
						throw new ValidationException(
								"fornitorierroreCampoIsbnMancante",
								ValidationExceptionCodici.fornitorierroreCampoIsbnMancante);
					}
				}
			}
			// OTTOBRE- Mail Aste
			// In inserimento/modifica editore PUO'  essere possibile inserire un editore anche senza valorizzare il numero standard
//			else {
//				throw new ValidationException(
//						"fornitorierroreCampoIsbnMancante",
//						ValidationExceptionCodici.fornitorierroreCampoIsbnMancante);
//			}
		}

		if (oggettoVO.getUnitaOrg() != null
				&& oggettoVO.getUnitaOrg().length() != 0) {
			if (oggettoVO.getUnitaOrg().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoUOFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoUOFornEccedente);
			}
		}
		if (oggettoVO.getIndirizzo() != null
				&& oggettoVO.getIndirizzo().trim().length() != 0) {
			if (oggettoVO.getIndirizzo().trim().length() > 70) {
				throw new ValidationException(
						"ordinierroreCampoIndFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoIndFornEccedente);
			}
		}
		if (oggettoVO.getCasellaPostale() != null
				&& oggettoVO.getCasellaPostale().trim().length() != 0) {
			if (oggettoVO.getCasellaPostale().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoCPostFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoCPostFornEccedente);
			}
		}

		if (oggettoVO.getCitta() != null
				&& oggettoVO.getCitta().trim().length() != 0) {
			if (oggettoVO.getCitta().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoCittaFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoCittaFornEccedente);
			}
		}

		if (oggettoVO.getTelefono() != null
				&& oggettoVO.getTelefono().trim().length() != 0) {
			if (oggettoVO.getTelefono().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoTelFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoTelFornEccedente);
			}
		}

		if (oggettoVO.getFax() != null
				&& oggettoVO.getFax().trim().length() != 0) {
			if (oggettoVO.getFax().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampoFaxFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoFaxFornEccedente);
			}
		}

		if (oggettoVO.getCodiceFiscale() != null
				&& oggettoVO.getCodiceFiscale().trim().length() != 0) {
			if (oggettoVO.getCodiceFiscale().trim().length() > 18) {
				throw new ValidationException(
						"ordinierroreCampoCFiscFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoCFiscFornEccedente);
			}
		}
		if (oggettoVO.getPartitaIva() != null
				&& oggettoVO.getPartitaIva().trim().length() != 0) {
			if (oggettoVO.getPartitaIva().trim().length() > 18) {
				throw new ValidationException(
						"ordinierroreCampoPIvaFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoPIvaFornEccedente);
			}
		}
		if (oggettoVO.getEmail() != null
				&& oggettoVO.getEmail().trim().length() != 0) {
			if (oggettoVO.getEmail().trim().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoEMailFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoEMailFornEccedente);
			}
		}

		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getTipoPagamento() != null
				&& oggettoVO.getFornitoreBibl().getTipoPagamento().trim()
						.length() != 0) {
			if (oggettoVO.getFornitoreBibl().getTipoPagamento().trim().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoTipoPagFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoPagFornEccedente);
			}
		}
		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getCodCliente() != null
				&& oggettoVO.getFornitoreBibl().getCodCliente().trim().length() != 0) {
			if (oggettoVO.getFornitoreBibl().getCodCliente().trim().length() > 40) {
				throw new ValidationException(
						"ordinierroreCampoCodCliFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoCodCliFornEccedente);
			}
		}
		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getNomContatto() != null
				&& oggettoVO.getFornitoreBibl().getNomContatto().trim()
						.length() != 0) {
			if (oggettoVO.getFornitoreBibl().getNomContatto().trim().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomContFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomContFornEccedente);
			}
		}
		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getTelContatto() != null
				&& oggettoVO.getFornitoreBibl().getTelContatto().trim()
						.length() != 0) {
			if (oggettoVO.getFornitoreBibl().getTelContatto().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampTelContFornEccedente",
						ValidationExceptionCodici.ordinierroreCampTelContFornEccedente);
			}
		}
		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getFaxContatto() != null
				&& oggettoVO.getFornitoreBibl().getFaxContatto().trim()
						.length() != 0) {
			if (oggettoVO.getFornitoreBibl().getFaxContatto().trim().length() > 20) {
				throw new ValidationException(
						"ordinierroreCampFaxContFornEccedente",
						ValidationExceptionCodici.ordinierroreCampFaxContFornEccedente);
			}
		}
		if (oggettoVO.getFornitoreBibl() != null
				&& oggettoVO.getFornitoreBibl().getValuta() != null
				&& oggettoVO.getFornitoreBibl().getValuta().trim().length() != 0) {
			if (oggettoVO.getFornitoreBibl().getValuta().trim().length() > 3) {
				throw new ValidationException(
						"ordinierroreCampValutaContFornEccedente",
						ValidationExceptionCodici.ordinierroreCampValutaContFornEccedente);
			}
		}

		if (oggettoVO.getNote() != null
				&& oggettoVO.getNote().trim().length() != 0) {
			if (oggettoVO.getNote().trim().length() > 160) {
				throw new ValidationException(
						"ordinierroreCampoNoteFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
			}
		}

	}

	public static void ValidaListaSuppFornitoreVO(
			ListaSuppFornitoreVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodFornitore() != null
				&& oggettoLista.getCodFornitore().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoLista
					.getCodFornitore()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoLista.getNomeFornitore() != null
				&& oggettoLista.getNomeFornitore().length() != 0) {
			if (strIsNumeric(oggettoLista.getNomeFornitore())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoLista.getNomeFornitore().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}
		if (oggettoLista.getTipoPartner() != null
				&& oggettoLista.getTipoPartner().length() != 0) {
			if (oggettoLista.getTipoPartner().length() > 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoPartnerFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoPartnerFornEccedente);
			}
		}

		if (oggettoLista.getPaese() != null
				&& oggettoLista.getPaese().length() != 0) {
			if (oggettoLista.getPaese().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoPaeseFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoPaeseFornEccedente);
			}
		}

		if (oggettoLista.getCodProfiloAcq() != null
				&& oggettoLista.getCodProfiloAcq().length() != 0) {

			if (!strIsNumeric(oggettoLista.getCodProfiloAcq())) {
				throw new ValidationException(
						"ordineerroreCodProfAcqNumerico",
						ValidationExceptionCodici.ordineerroreCodProfAcqNumerico);
			}
		}

		if (oggettoLista.getProvincia() != null
				&& oggettoLista.getProvincia().length() != 0) {
			if (oggettoLista.getProvincia().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoProvFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoProvFornEccedente);
			}
		}

		if (oggettoLista.getRegione() != null
				&& oggettoLista.getRegione().length() != 0) {
			if (oggettoLista.getRegione().length() > 2) {
				throw new ValidationException(
						"ordinierroreCampoProvFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoProvFornEccedente);
			}
		}
	}

	public static void ValidaRicercaComunicazioni(
			List<ComunicazioneVO> listaComunicazioni)
			throws ValidationException {
		//
		if (listaComunicazioni == null || listaComunicazioni.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppComunicazioneVO(
			ListaSuppComunicazioneVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoLista.getDataComunicazioneDa() != null
				&& oggettoLista.getDataComunicazioneDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataComunicazioneDa())
					&& oggettoLista.getDataComunicazioneDa().length() == 4) {
				String strAnnata = oggettoLista.getDataComunicazioneDa();
				oggettoLista.setDataComunicazioneDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataComunicazioneDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoLista.getDataComunicazioneA() != null
				&& oggettoLista.getDataComunicazioneA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataComunicazioneA())
					&& oggettoLista.getDataComunicazioneA().length() == 4) {
				String strAnnata = oggettoLista.getDataComunicazioneA();
				oggettoLista.setDataComunicazioneA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataComunicazioneA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoLista.getCodiceMessaggio() != null
				&& oggettoLista.getCodiceMessaggio().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getCodiceMessaggio())
					&& oggettoLista.getCodiceMessaggio().trim().length() != 0) {
				throw new ValidationException(
						"comunicazioneerroreCodMsgNumerico",
						ValidationExceptionCodici.comunicazioneerroreCodMsgNumerico);
			}
		}
		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getCodice() != null
				&& oggettoLista.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoLista.getFornitore()
					.getCodice()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}
		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getDescrizione() != null
				&& oggettoLista.getFornitore().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getFornitore().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoLista.getFornitore().getDescrizione().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}

		// anno fattura e tipo documento relativo
		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("F")
				&& oggettoLista.getIdDocumento().getCodice1() != null
				&& oggettoLista.getIdDocumento().getCodice1().trim().length() > 0) {
			throw new ValidationException("fatturaerroreCampoChiaveNonNullo",
					ValidationExceptionCodici.fatturaerroreCampoChiaveNonNullo);
		}

		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("F")
				&& oggettoLista.getIdDocumento().getCodice2() != null
				&& oggettoLista.getIdDocumento().getCodice2().trim().length() != 0) {

			if (String.valueOf(oggettoLista.getIdDocumento().getCodice2())
					.length() != 0) {
				if (!strIsNumeric(oggettoLista.getIdDocumento().getCodice2())) {
					throw new ValidationException("erroreCampoAnnoNumerico",
							ValidationExceptionCodici.erroreCampoAnnoNumerico);
				}

				if (oggettoLista.getIdDocumento().getCodice2().length() != 4) {
					throw new ValidationException(
							"erroreCampoAnnoNumericoEccedente",
							ValidationExceptionCodici.erroreCampoAnnoNumericoEccedente);
				}
			}
		}
		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("F")
				&& oggettoLista.getIdDocumento().getCodice3() != null
				&& oggettoLista.getIdDocumento().getCodice3().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getIdDocumento().getCodice3())
					&& oggettoLista.getIdDocumento().getCodice3().trim()
							.length() != 0) {
				throw new ValidationException(
						"fatturaerroreCampoProgrNumerico",
						ValidationExceptionCodici.fatturaerroreCampoProgrNumerico);
			}
		}
		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("O")
				&& oggettoLista.getIdDocumento().getCodice1() != null
				&& oggettoLista.getIdDocumento().getCodice1().trim().length() != 0) {

			if (strIsNumeric(oggettoLista.getIdDocumento().getCodice1())) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
			}
			if (oggettoLista.getIdDocumento().getCodice1().length() != 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
			}
		}

		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("O")
				&& oggettoLista.getIdDocumento().getCodice2() != null
				&& oggettoLista.getIdDocumento().getCodice2().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getIdDocumento().getCodice2().trim())) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
			}

			if (oggettoLista.getIdDocumento().getCodice2().length() != 4) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineEccedente",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
			}
		}
		if (oggettoLista.getIdDocumento() != null
				&& oggettoLista.getTipoDocumento().equals("O")
				&& oggettoLista.getIdDocumento().getCodice3() != null
				&& oggettoLista.getIdDocumento().getCodice3().trim().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoLista.getIdDocumento()
					.getCodice3().trim()))) {
				throw new ValidationException(
						"sezioneerroreCampoCodiceOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineNumerico);
			}
		}

	}

	public static void ValidaComunicazioneVO(ComunicazioneVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (oggettoVO.getDataComunicazione() != null
				&& oggettoVO.getDataComunicazione().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoDataObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoDataObbligatorio);
		}

		if (oggettoVO.getTipoDocumento() != null
				&& oggettoVO.getTipoDocumento().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoTipoDocObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoTipoDocObbligatorio);
		}
		if (oggettoVO.getDirezioneComunicazione() != null
				&& oggettoVO.getDirezioneComunicazione().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoDirComObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoDirComObbligatorio);
		}
		if (oggettoVO.getTipoMessaggio() != null
				&& oggettoVO.getTipoMessaggio().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoTipoMsgObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoTipoMsgObbligatorio);
		}
		// controllo congruenza tipo documento - tipo messaggio - direzione
		// comunicazione

		if (oggettoVO.getDirezioneComunicazione() != null
				&& oggettoVO.getDirezioneComunicazione().equals("A")) {
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("F")) {
				if (!oggettoVO.getTipoMessaggio().equals("07")
						&& !oggettoVO.getTipoMessaggio().equals("08")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}

			}
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("O")) {
				if (!oggettoVO.getTipoMessaggio().equals("01")
						&& !oggettoVO.getTipoMessaggio().equals("02")
						&& !oggettoVO.getTipoMessaggio().equals("03")
						&& !oggettoVO.getTipoMessaggio().equals("04")
						&& !oggettoVO.getTipoMessaggio().equals("05")
						&& !oggettoVO.getTipoMessaggio().equals("06")
						&& !oggettoVO.getTipoMessaggio().equals("24")
						&& !oggettoVO.getTipoMessaggio().equals("25")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}

			}
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("")) {
				if (!oggettoVO.getTipoMessaggio().equals("01")
						&& !oggettoVO.getTipoMessaggio().equals("02")
						&& !oggettoVO.getTipoMessaggio().equals("03")
						&& !oggettoVO.getTipoMessaggio().equals("04")
						&& !oggettoVO.getTipoMessaggio().equals("05")
						&& !oggettoVO.getTipoMessaggio().equals("06")
						&& !oggettoVO.getTipoMessaggio().equals("07")
						&& !oggettoVO.getTipoMessaggio().equals("08")
						&& !oggettoVO.getTipoMessaggio().equals("24")
						&& !oggettoVO.getTipoMessaggio().equals("25")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}
			}
		}
		if (oggettoVO.getDirezioneComunicazione() != null
				&& oggettoVO.getDirezioneComunicazione().equals("D")) {
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("F")) {
				if (!oggettoVO.getTipoMessaggio().equals("01")
						&& !oggettoVO.getTipoMessaggio().equals("15")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}

			}
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("O")) {
				if (!oggettoVO.getTipoMessaggio().equals("10")
						&& !oggettoVO.getTipoMessaggio().equals("11")
						&& !oggettoVO.getTipoMessaggio().equals("12")
						&& !oggettoVO.getTipoMessaggio().equals("13")
						&& !oggettoVO.getTipoMessaggio().equals("14")
						&& !oggettoVO.getTipoMessaggio().equals("16")
						&& !oggettoVO.getTipoMessaggio().equals("17")
						&& !oggettoVO.getTipoMessaggio().equals("18")
						&& !oggettoVO.getTipoMessaggio().equals("19")
						&& !oggettoVO.getTipoMessaggio().equals("20")
						&& !oggettoVO.getTipoMessaggio().equals("21")
						&& !oggettoVO.getTipoMessaggio().equals("22")
						&& !oggettoVO.getTipoMessaggio().equals("23")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}

			}
			if (oggettoVO.getTipoDocumento() != null
					&& oggettoVO.getTipoDocumento().equals("")) {
				if (!oggettoVO.getTipoMessaggio().equals("10")
						&& !oggettoVO.getTipoMessaggio().equals("11")
						&& !oggettoVO.getTipoMessaggio().equals("12")
						&& !oggettoVO.getTipoMessaggio().equals("13")
						&& !oggettoVO.getTipoMessaggio().equals("14")
						&& !oggettoVO.getTipoMessaggio().equals("16")
						&& !oggettoVO.getTipoMessaggio().equals("17")
						&& !oggettoVO.getTipoMessaggio().equals("18")
						&& !oggettoVO.getTipoMessaggio().equals("19")
						&& !oggettoVO.getTipoMessaggio().equals("20")
						&& !oggettoVO.getTipoMessaggio().equals("21")
						&& !oggettoVO.getTipoMessaggio().equals("22")
						&& !oggettoVO.getTipoMessaggio().equals("23")
						&& !oggettoVO.getTipoMessaggio().equals("01")
						&& !oggettoVO.getTipoMessaggio().equals("15")) {
					throw new ValidationException(
							"comunicazionierroreIncongruenzaTipoMessaggio",
							ValidationExceptionCodici.comunicazionierroreIncongruenzaTipoMessaggio);

				}
			}
		}
		// fine controllo congruenza tipo documento - tipo messaggio - direzione
		// comunicazione

		if (oggettoVO.getFornitore() == null
				|| (oggettoVO.getFornitore() != null
						&& oggettoVO.getFornitore().getCodice() != null && oggettoVO
						.getFornitore().getCodice().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoFornitoreObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
		}
		if (oggettoVO.getIdDocumento() == null
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("O")
						&& oggettoVO.getIdDocumento().getCodice1() != null && oggettoVO
						.getIdDocumento().getCodice1().length() == 0)
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("O")
						&& oggettoVO.getIdDocumento().getCodice2() != null && oggettoVO
						.getIdDocumento().getCodice2().length() == 0)
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("O")
						&& oggettoVO.getIdDocumento().getCodice3() != null && oggettoVO
						.getIdDocumento().getCodice3().length() == 0)) {
			throw new ValidationException(
					"comunicazionierroreDatiOrdineObbligatorio",
					ValidationExceptionCodici.comunicazionierroreDatiOrdineObbligatorio);
		}
		if (oggettoVO.getIdDocumento() == null
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("F")
						&& oggettoVO.getIdDocumento().getCodice1() != null && oggettoVO
						.getIdDocumento().getCodice1().length() != 0)
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("F")
						&& oggettoVO.getIdDocumento().getCodice2() != null && oggettoVO
						.getIdDocumento().getCodice2().length() == 0)
				|| (oggettoVO.getIdDocumento() != null
						&& oggettoVO.getTipoDocumento().equals("F")
						&& oggettoVO.getIdDocumento().getCodice3() != null && oggettoVO
						.getIdDocumento().getCodice3().length() == 0)) {
			throw new ValidationException(
					"comunicazionierroreDatiFatturaObbligatorio",
					ValidationExceptionCodici.comunicazionierroreDatiFatturaObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}
		if (oggettoVO.getDataComunicazione() != null
				&& oggettoVO.getDataComunicazione().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataComunicazione()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoVO.getCodiceMessaggio() != null
				&& oggettoVO.getCodiceMessaggio().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getCodiceMessaggio())
					&& oggettoVO.getCodiceMessaggio().trim().length() != 0) {
				throw new ValidationException(
						"comunicazioneerroreCodMsgNumerico",
						ValidationExceptionCodici.comunicazioneerroreCodMsgNumerico);
			}
		}
		if (oggettoVO.getFornitore() != null
				&& oggettoVO.getFornitore().getCodice() != null
				&& oggettoVO.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoVO.getFornitore()
					.getCodice()))) {
				throw new ValidationException("ordineerroreCodFornNumerico",
						ValidationExceptionCodici.ordineerroreCodFornNumerico);
			}
		}
		if (oggettoVO.getFornitore() != null
				&& oggettoVO.getFornitore().getDescrizione() != null
				&& oggettoVO.getFornitore().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoVO.getFornitore().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoNomeFornAlfabetico);
			}
			if (oggettoVO.getFornitore().getDescrizione().length() > 50) {
				throw new ValidationException(
						"ordinierroreCampoNomeFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNomeFornEccedente);
			}
		}

		// anno fattura e tipo documento relativo
		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("F")
				&& oggettoVO.getIdDocumento().getCodice1() != null
				&& oggettoVO.getIdDocumento().getCodice1().trim().length() > 0) {
			throw new ValidationException("fatturaerroreCampoChiaveNonNullo",
					ValidationExceptionCodici.fatturaerroreCampoChiaveNonNullo);
		}

		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("F")
				&& oggettoVO.getIdDocumento().getCodice2() != null
				&& oggettoVO.getIdDocumento().getCodice2().trim().length() != 0) {
			if (String.valueOf(oggettoVO.getIdDocumento().getCodice2())
					.length() != 0) {
				if (!strIsNumeric(oggettoVO.getIdDocumento().getCodice2())) {
					throw new ValidationException("erroreCampoAnnoNumerico",
							ValidationExceptionCodici.erroreCampoAnnoNumerico);
				}

				if (oggettoVO.getIdDocumento().getCodice2().length() != 4) {
					throw new ValidationException(
							"erroreCampoAnnoNumericoEccedente",
							ValidationExceptionCodici.erroreCampoAnnoNumericoEccedente);
				}
			}
		}
		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("F")
				&& oggettoVO.getIdDocumento().getCodice3() != null
				&& oggettoVO.getIdDocumento().getCodice3().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getIdDocumento().getCodice3())
					&& oggettoVO.getIdDocumento().getCodice3().trim().length() != 0) {
				throw new ValidationException(
						"fatturaerroreCampoProgrNumerico",
						ValidationExceptionCodici.fatturaerroreCampoProgrNumerico);
			}
		}
		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("O")
				&& oggettoVO.getIdDocumento().getCodice1() != null
				&& oggettoVO.getIdDocumento().getCodice1().trim().length() != 0) {

			if (strIsNumeric(oggettoVO.getIdDocumento().getCodice1())) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineAlfabetico);
			}
			if (oggettoVO.getIdDocumento().getCodice1().length() != 1) {
				throw new ValidationException(
						"ordinierroreCampoTipoOrdineEccedente",
						ValidationExceptionCodici.ordinierroreCampoTipoOrdineEccedente);
			}
		}

		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("O")
				&& oggettoVO.getIdDocumento().getCodice2() != null
				&& oggettoVO.getIdDocumento().getCodice2().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getIdDocumento().getCodice2().trim())) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineNumerico);
			}

			if (oggettoVO.getIdDocumento().getCodice2().length() != 4) {
				throw new ValidationException(
						"sezioneerroreCampoAnnoOrdineEccedente",
						ValidationExceptionCodici.sezioneerroreCampoAnnoOrdineEccedente);
			}
		}
		if (oggettoVO.getIdDocumento() != null
				&& oggettoVO.getTipoDocumento().equals("O")
				&& oggettoVO.getIdDocumento().getCodice3() != null
				&& oggettoVO.getIdDocumento().getCodice3().trim().length() != 0) {
			if (!strIsNumeric(String.valueOf(oggettoVO.getIdDocumento()
					.getCodice3().trim()))) {
				throw new ValidationException(
						"sezioneerroreCampoCodiceOrdineNumerico",
						ValidationExceptionCodici.sezioneerroreCampoCodiceOrdineNumerico);
			}
		}
		if (oggettoVO.getNoteComunicazione() != null
				&& oggettoVO.getNoteComunicazione().trim().length() != 0) {
			if (oggettoVO.getNoteComunicazione().trim().length() > 255) {
				throw new ValidationException(
						"sezioneerroreCampoNoteSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoNoteSezioneEccedente);
			}
		}

	}

	public static void ValidaRicercaSuggerimenti(
			List<SuggerimentoVO> listaSuggerimenti)
			throws ValidationException {
		//
		if (listaSuggerimenti == null || listaSuggerimenti.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppSuggerimentoVO(
			ListaSuppSuggerimentoVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoLista.getDataSuggerimentoDa() != null
				&& oggettoLista.getDataSuggerimentoDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataSuggerimentoDa())
					&& oggettoLista.getDataSuggerimentoDa().length() == 4) {
				String strAnnata = oggettoLista.getDataSuggerimentoDa();
				oggettoLista.setDataSuggerimentoDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataSuggerimentoDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoLista.getDataSuggerimentoA() != null
				&& oggettoLista.getDataSuggerimentoA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataSuggerimentoA())
					&& oggettoLista.getDataSuggerimentoA().length() == 4) {
				String strAnnata = oggettoLista.getDataSuggerimentoA();
				oggettoLista.setDataSuggerimentoA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataSuggerimentoA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoLista.getCodiceSuggerimento() != null
				&& oggettoLista.getCodiceSuggerimento().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getCodiceSuggerimento())
					&& oggettoLista.getCodiceSuggerimento().trim().length() != 0) {
				throw new ValidationException(
						"suggerimentoerroreCodSuggNumerico",
						ValidationExceptionCodici.suggerimentoerroreCodSuggNumerico);
			}
		}

		if (oggettoLista.getTitolo() != null
				&& oggettoLista.getTitolo().getCodice() != null
				&& oggettoLista.getTitolo().getCodice().length() != 0) {
			if (strIsNumeric(oggettoLista.getTitolo().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceAlfabetico);
			}
			if (oggettoLista.getTitolo().getCodice().length() > 10) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceEccedente);
			}
		}
		if (oggettoLista.getTitolo() != null
				&& oggettoLista.getTitolo().getDescrizione() != null
				&& oggettoLista.getTitolo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getTitolo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneAlfabetico);
			}
			if (oggettoLista.getTitolo().getDescrizione().length() > 1200) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneEccedente);
			}
		}
		if (oggettoLista.getBibliotecario() != null
				&& oggettoLista.getBibliotecario().getCodice() != null
				&& oggettoLista.getBibliotecario().getCodice().length() != 0) {

			if (oggettoLista.getBibliotecario().getCodice().trim().length() > 12) {
				throw new ValidationException(
						"suggerimentoerroreBibliotecarioDescrizioneEccedente",
						ValidationExceptionCodici.suggerimentoerroreBibliotecarioDescrizioneEccedente);
			}
		}
		if (oggettoLista.getStatoSuggerimento() != null
				&& oggettoLista.getStatoSuggerimento().length() != 0) {
			if (strIsNumeric(oggettoLista.getStatoSuggerimento())) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggAlfabetico",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggAlfabetico);
			}
			if (oggettoLista.getStatoSuggerimento().length() > 1) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggEccedente",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggEccedente);
			}
		}

	}

	public static void ValidaSuggerimentoVO(SuggerimentoVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (oggettoVO.getDataSuggerimento() != null
				&& oggettoVO.getDataSuggerimento().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoDataObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoDataObbligatorio);
		}

		if (oggettoVO.getStatoSuggerimento() != null
				&& oggettoVO.getStatoSuggerimento().trim().length() == 0) {
			throw new ValidationException(
					"suggerimentoerroreStatoSuggObbligatorio",
					ValidationExceptionCodici.suggerimentoerroreStatoSuggObbligatorio);
		}

		if (oggettoVO.getBibliotecario() != null
				&& (oggettoVO.getBibliotecario().getCodice() != null && oggettoVO
						.getBibliotecario().getCodice().trim().length() == 0)
				&& (oggettoVO.getBibliotecario().getDescrizione() != null && oggettoVO
						.getBibliotecario().getDescrizione().trim().length() == 0)) {
			throw new ValidationException(
					"suggerimentoerroreBibliotecarioObbligatorio",
					ValidationExceptionCodici.suggerimentoerroreBibliotecarioObbligatorio);
		}
		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getCodice() != null
				&& oggettoVO.getTitolo().getCodice().trim().length() == 0) {
			throw new ValidationException(
					"suggerimentoerroreTitoloObbligatorio",
					ValidationExceptionCodici.suggerimentoerroreTitoloObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}
		if (oggettoVO.getDataSuggerimento() != null
				&& oggettoVO.getDataSuggerimento().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataSuggerimento()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoVO.getCodiceSuggerimento() != null
				&& oggettoVO.getCodiceSuggerimento().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getCodiceSuggerimento())
					&& oggettoVO.getCodiceSuggerimento().trim().length() != 0) {
				throw new ValidationException(
						"suggerimentoerroreCodSuggNumerico",
						ValidationExceptionCodici.suggerimentoerroreCodSuggNumerico);
			}
		}

		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getCodice() != null
				&& oggettoVO.getTitolo().getCodice().length() != 0) {
			if (strIsNumeric(oggettoVO.getTitolo().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceAlfabetico);
			}
			if (oggettoVO.getTitolo().getCodice().length() > 10) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceEccedente);
			}
		}
		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getDescrizione() != null
				&& oggettoVO.getTitolo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoVO.getTitolo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneAlfabetico);
			}
			if (oggettoVO.getTitolo().getDescrizione().length() > 1200) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneEccedente);
			}
		}

		// temporaneamente
		if (oggettoVO.getBibliotecario() != null
				&& oggettoVO.getBibliotecario().getCodice() != null
				&& oggettoVO.getBibliotecario().getCodice().length() != 0) {

			if (oggettoVO.getBibliotecario().getCodice().trim().length() > 12) {
				throw new ValidationException(
						"suggerimentoerroreBibliotecarioDescrizioneEccedente",
						ValidationExceptionCodici.suggerimentoerroreBibliotecarioDescrizioneEccedente);
			}

		}

		if (oggettoVO.getStatoSuggerimento() != null
				&& oggettoVO.getStatoSuggerimento().length() != 0) {
			if (strIsNumeric(oggettoVO.getStatoSuggerimento())) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggAlfabetico",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggAlfabetico);
			}
			if (oggettoVO.getStatoSuggerimento().length() > 1) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggEccedente",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggEccedente);
			}
		}

		if (oggettoVO.getSezione() != null
				&& oggettoVO.getSezione().getCodice() != null
				&& oggettoVO.getSezione().getCodice().length() != 0) {

			if (oggettoVO.getSezione().getCodice().length() > 7) {
				throw new ValidationException(
						"ordinierroreCodSezioneEccedente",
						ValidationExceptionCodici.ordinierroreCodSezioneEccedente);
			}

		}

		if (oggettoVO.getNoteSuggerimento() != null
				&& oggettoVO.getNoteSuggerimento().trim().length() != 0) {
			if (oggettoVO.getNoteSuggerimento().trim().length() > 160) {
				throw new ValidationException(
						"ordinierroreCampoNoteFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
			}
		}
		if (oggettoVO.getNoteFornitore() != null
				&& oggettoVO.getNoteFornitore().trim().length() != 0) {
			if (oggettoVO.getNoteFornitore().trim().length() > 160) {
				throw new ValidationException(
						"ordinierroreCampoNoteFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
			}
		}
		if (oggettoVO.getNoteBibliotecario() != null
				&& oggettoVO.getNoteBibliotecario().trim().length() != 0) {
			if (oggettoVO.getNoteBibliotecario().trim().length() > 255) {
				throw new ValidationException(
						"sezioneerroreCampoNoteSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoNoteSezioneEccedente);
			}
		}

	}

	public static void ValidaRicercaDocumenti(
			List<DocumentoVO> listaDocumenti) throws ValidationException {
		//
		if (listaDocumenti == null || listaDocumenti.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppDocumentoVO(
			ListaSuppDocumentoVO oggettoLista) throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoLista.getDataSuggerimentoDocDa() != null
				&& oggettoLista.getDataSuggerimentoDocDa().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataSuggerimentoDocDa())
					&& oggettoLista.getDataSuggerimentoDocDa().length() == 4) {
				String strAnnata = oggettoLista.getDataSuggerimentoDocDa();
				oggettoLista.setDataSuggerimentoDocDa("01/01/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataSuggerimentoDocDa()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}
		if (oggettoLista.getDataSuggerimentoDocA() != null
				&& oggettoLista.getDataSuggerimentoDocA().length() != 0) {
			// controllo che non sia presente l'indicazione del solo anno
			if (strIsNumeric(oggettoLista.getDataSuggerimentoDocA())
					&& oggettoLista.getDataSuggerimentoDocA().length() == 4) {
				String strAnnata = oggettoLista.getDataSuggerimentoDocA();
				oggettoLista.setDataSuggerimentoDocA("31/12/" + strAnnata);
			}
			// controllo congruenza
			if (validaDataPassata(oggettoLista.getDataSuggerimentoDocA()) != 0) {
				throw new ValidationException("erroreData",
						ValidationExceptionCodici.erroreData);
			}
		}

		if (oggettoLista.getCodDocumento() != null
				&& oggettoLista.getCodDocumento().trim().length() != 0) {
			if (!strIsNumeric(oggettoLista.getCodDocumento())
					&& oggettoLista.getCodDocumento().trim().length() != 0) {
				throw new ValidationException("documentoerroreCodDocNumerico",
						ValidationExceptionCodici.documentoerroreCodDocNumerico);
			}
		}

		if (oggettoLista.getTitolo() != null
				&& oggettoLista.getTitolo().getDescrizione() != null
				&& oggettoLista.getTitolo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoLista.getTitolo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneAlfabetico);
			}
			if (oggettoLista.getTitolo().getDescrizione().length() > 1200) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneEccedente);
			}
		}
		if (oggettoLista.getStatoSuggerimentoDocumento() != null
				&& oggettoLista.getStatoSuggerimentoDocumento().length() != 0) {
			if (strIsNumeric(oggettoLista.getStatoSuggerimentoDocumento())) {
				throw new ValidationException(
						"documentoerroreStatoSuggDocAlfabetico",
						ValidationExceptionCodici.documentoerroreStatoSuggDocAlfabetico);
			}
			if (oggettoLista.getStatoSuggerimentoDocumento().length() > 1) {
				throw new ValidationException(
						"documentoerroreStatoSuggDocEccedente",
						ValidationExceptionCodici.documentoerroreStatoSuggDocEccedente);
			}
		}

		if (oggettoLista.getUtente() != null
				&& oggettoLista.getUtente().getCodice2() != null
				&& oggettoLista.getUtente().getCodice2().length() != 0) {

			if (oggettoLista.getUtente().getCodice2().length() > 25) {
				throw new ValidationException(
						"documentoerroreCodUtenteSecondaParteEccedente",
						ValidationExceptionCodici.documentoerroreCodUtenteSecondaParteEccedente);
			}
		}

		if (oggettoLista.getAnnoEdizione() != null
				&& String.valueOf(oggettoLista.getAnnoEdizione()).length() != 0) {
			if (!strIsNumeric(oggettoLista.getAnnoEdizione())) {
				throw new ValidationException(
						"documentoerroreCampoAnnoEdizioneNumerico",
						ValidationExceptionCodici.documentoerroreCampoAnnoEdizioneNumerico);
			}

			if (oggettoLista.getAnnoEdizione().length() != 4) {
				throw new ValidationException(
						"documentoerroreCampoAnnoEdizioneEccedente",
						ValidationExceptionCodici.documentoerroreCampoAnnoEdizioneEccedente);
			}
		}
	}

	public static void ValidaDocumentoVO(DocumentoVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}

		if (oggettoVO.getDataIns() != null
				&& oggettoVO.getDataIns().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoDataObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoDataObbligatorio);
		}
		if (oggettoVO.getDataAgg() != null
				&& oggettoVO.getDataAgg().trim().length() == 0) {
			throw new ValidationException(
					"comunicazionierroreCampoDataObbligatorio",
					ValidationExceptionCodici.comunicazionierroreCampoDataObbligatorio);
		}

		if (oggettoVO.getStatoSuggerimentoDocumento() != null
				&& oggettoVO.getStatoSuggerimentoDocumento().trim().length() == 0) {
			throw new ValidationException(
					"suggerimentoerroreStatoSuggObbligatorio",
					ValidationExceptionCodici.suggerimentoerroreStatoSuggObbligatorio);
		}
		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getDescrizione() != null
				&& oggettoVO.getTitolo().getDescrizione().trim().length() == 0) {
			throw new ValidationException(
					"documentoerroreTitoloDescObbligatorio",
					ValidationExceptionCodici.documentoerroreTitoloDescObbligatorio);
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().length() != 0) {
			if (strIsNumeric(oggettoVO.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoVO.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}

		}

		if (oggettoVO.getCodDocumento() != null
				&& oggettoVO.getCodDocumento().trim().length() != 0) {
			if (!strIsNumeric(oggettoVO.getCodDocumento())
					&& oggettoVO.getCodDocumento().trim().length() != 0) {
				throw new ValidationException(
						"suggerimentoerroreCodSuggNumerico",
						ValidationExceptionCodici.suggerimentoerroreCodSuggNumerico);
			}
		}

		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getCodice() != null
				&& oggettoVO.getTitolo().getCodice().length() != 0) {
			if (strIsNumeric(oggettoVO.getTitolo().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceAlfabetico);
			}
			if (oggettoVO.getTitolo().getCodice().length() > 10) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceEccedente);
			}
		}
		if (oggettoVO.getTitolo() != null
				&& oggettoVO.getTitolo().getDescrizione() != null
				&& oggettoVO.getTitolo().getDescrizione().length() != 0) {
			if (strIsNumeric(oggettoVO.getTitolo().getDescrizione())) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneAlfabetico);
			}
			if (oggettoVO.getTitolo().getDescrizione().length() > 1200) {
				throw new ValidationException(
						"ordinierroreCampoBidDescrizioneEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidDescrizioneEccedente);
			}
		}
		if (oggettoVO.getUtente() != null
				&& oggettoVO.getUtente().getCodice2() != null
				&& oggettoVO.getUtente().getCodice2().length() != 0) {
			if (oggettoVO.getUtente().getCodice2().length() > 25) {
				throw new ValidationException(
						"documentoerroreCodUtenteSecondaParteEccedente",
						ValidationExceptionCodici.documentoerroreCodUtenteSecondaParteEccedente);
			}
		}
		if (oggettoVO.getStatoSuggerimentoDocumento() != null
				&& oggettoVO.getStatoSuggerimentoDocumento().length() != 0) {
			if (strIsNumeric(oggettoVO.getStatoSuggerimentoDocumento())) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggAlfabetico",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggAlfabetico);
			}
			if (oggettoVO.getStatoSuggerimentoDocumento().length() > 1) {
				throw new ValidationException(
						"suggerimentoerroreStatoSuggEccedente",
						ValidationExceptionCodici.suggerimentoerroreStatoSuggEccedente);
			}
		}

		if (oggettoVO.getNoteDocumento() != null
				&& oggettoVO.getNoteDocumento().trim().length() != 0) {
			if (oggettoVO.getNoteDocumento().trim().length() > 160) {
				throw new ValidationException(
						"ordinierroreCampoNoteFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
			}
		}
		if (oggettoVO.getMsgPerLettore() != null
				&& oggettoVO.getMsgPerLettore().trim().length() != 0) {
			if (oggettoVO.getMsgPerLettore().trim().length() > 255) {
				throw new ValidationException(
						"sezioneerroreCampoNoteSezioneEccedente",
						ValidationExceptionCodici.sezioneerroreCampoNoteSezioneEccedente);
			}
		}

	}

	public static void ValidaRicercaGare(List<GaraVO> listaGare)
			throws ValidationException {
		//
		if (listaGare == null || listaGare.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppGaraVO(ListaSuppGaraVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}
		if (oggettoLista.getCodRicOfferta() != null
				&& oggettoLista.getCodRicOfferta().length() != 0) {

			if (!strIsNumeric(oggettoLista.getCodRicOfferta())) {
				throw new ValidationException("gareerroreCodRichNumerico",
						ValidationExceptionCodici.gareerroreCodRichNumerico);
			}
		}

		if (oggettoLista.getBid() != null
				&& oggettoLista.getBid().getCodice() != null
				&& oggettoLista.getBid().getCodice().length() != 0) {
			if (strIsNumeric(oggettoLista.getBid().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceAlfabetico",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceAlfabetico);
			}
			if (oggettoLista.getBid().getCodice().length() > 10) {
				throw new ValidationException(
						"ordinierroreCampoBidCodiceEccedente",
						ValidationExceptionCodici.ordinierroreCampoBidCodiceEccedente);
			}
		}

	}

	public static void ValidaGaraVO(GaraVO oggettoVO)
			throws ValidationException {

		if (oggettoVO.getCodPolo() == null
				|| (oggettoVO.getCodPolo() != null && oggettoVO.getCodPolo()
						.trim().length() == 0)) {
			throw new ValidationException(
					"ordinierroreCampoCodPoloObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodPoloObbligatorio);
		}

		if (oggettoVO.getCodPolo().length() > 0) {

			if (oggettoVO.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}
		}

		if (oggettoVO.getCodBibl() != null
				&& oggettoVO.getCodBibl().trim().length() == 0) {
			throw new ValidationException(
					"ordinierroreCampoCodBiblObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoCodBiblObbligatorio);
		}
		if (oggettoVO.getBid() != null
				&& oggettoVO.getBid().getCodice() != null
				&& oggettoVO.getBid().getCodice().length() == 0) {
			throw new ValidationException("ordinierroreCampoBidObbligatorio",
					ValidationExceptionCodici.ordinierroreCampoBidObbligatorio);
		}

		if (String.valueOf(oggettoVO.getNumCopieRicAcq()).length() == 0) {
			throw new ValidationException("gareerroreNumCopieObbligatorio",
					ValidationExceptionCodici.gareerroreNumCopieObbligatorio);
		}
		if (strIsAlfabetic(String.valueOf(oggettoVO.getNumCopieRicAcq()))) {
			throw new ValidationException("ordineerrorePrezzoNumerico",
					ValidationExceptionCodici.ordineerrorePrezzoNumerico);
		} else {
			int pos = String.valueOf(oggettoVO.getNumCopieRicAcq())
					.lastIndexOf(",");
			if (pos != -1) {
				throw new ValidationException("ordineerroreDecimalPoint",
						ValidationExceptionCodici.ordineerroreDecimalPoint);
			}
		}

		if (oggettoVO.getDataRicOfferta() != null
				&& oggettoVO.getDataRicOfferta().length() != 0) {
			// controllo congruenza
			if (validaDataPassata(oggettoVO.getDataRicOfferta()) != 0) {
				throw new ValidationException("erroreDataRichiestaOfferta",
						ValidationExceptionCodici.erroreDataRichiestaOfferta);
			}
		}

		if (oggettoVO.getNoteOrdine() != null
				&& oggettoVO.getNoteOrdine().trim().length() != 0) {
			if (oggettoVO.getNoteOrdine().trim().length() > 160) {
				throw new ValidationException(
						"ordinierroreCampoNoteFornEccedente",
						ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
			}
		}

		if (oggettoVO.getDettaglioPartecipantiGara() != null
				&& oggettoVO.getDettaglioPartecipantiGara().size() != 0) {
			if (oggettoVO.getDettaglioPartecipantiGara().size() > 0) {
				String[] rigaImp = new String[oggettoVO
						.getDettaglioPartecipantiGara().size()];
				int totVinc = 0;
				for (int i = 0; i < oggettoVO.getDettaglioPartecipantiGara()
						.size(); i++) {
					rigaImp[i] = "";
					PartecipantiGaraVO oggettoDettVO = oggettoVO
							.getDettaglioPartecipantiGara().get(i);

					if (oggettoDettVO.getFornitore() != null
							&& oggettoDettVO.getFornitore().getCodice() != null
							&& oggettoDettVO.getFornitore().getCodice()
									.length() == 0) {
						throw new ValidationException(
								"ordinierroreCampoFornitoreObbligatorio",
								ValidationExceptionCodici.ordinierroreCampoFornitoreObbligatorio);
					}

					if (oggettoDettVO.getDataInvioAlFornRicOfferta() != null
							&& oggettoDettVO.getDataInvioAlFornRicOfferta()
									.length() == 0) {
						throw new ValidationException(
								"garaerroreCampoDataInvioObbligatorio",
								ValidationExceptionCodici.garaerroreCampoDataInvioObbligatorio);
					}

					if (oggettoDettVO.getFornitore() != null
							&& oggettoDettVO.getFornitore().getCodice() != null
							&& oggettoDettVO.getFornitore().getCodice()
									.length() != 0) {
						if (!strIsNumeric(String.valueOf(oggettoDettVO
								.getFornitore().getCodice()))) {
							throw new ValidationException(
									"ordineerroreCodFornNumerico",
									ValidationExceptionCodici.ordineerroreCodFornNumerico);
						}
					}

					if (oggettoDettVO.getDataInvioAlFornRicOfferta() != null
							&& oggettoDettVO.getDataInvioAlFornRicOfferta()
									.length() != 0) {
						// controllo congruenza
						if (validaDataPassata(oggettoDettVO
								.getDataInvioAlFornRicOfferta()) != 0) {
							throw new ValidationException(
									"erroreDataInvioPartecipanteGara",
									ValidationExceptionCodici.erroreDataInvioPartecipanteGara);
						}
					}
					if (oggettoDettVO.getNoteAlFornitore() != null
							&& oggettoDettVO.getNoteAlFornitore().trim()
									.length() != 0) {
						if (oggettoDettVO.getNoteAlFornitore().trim().length() > 160) {
							throw new ValidationException(
									"ordinierroreCampoNoteFornEccedente",
									ValidationExceptionCodici.ordinierroreCampoNoteFornEccedente);
						}
					}
					if (oggettoDettVO.getMsgRispDaFornAGara() != null
							&& oggettoDettVO.getMsgRispDaFornAGara().trim()
									.length() != 0) {
						if (oggettoDettVO.getMsgRispDaFornAGara().trim()
								.length() > 255) {
							throw new ValidationException(
									"sezioneerroreCampoNoteSezioneEccedente",
									ValidationExceptionCodici.sezioneerroreCampoNoteSezioneEccedente);
						}
					}

					// deve esserci un unico vincitore
					String rigaStato = oggettoDettVO.getStatoPartecipante();
					if (rigaStato.equals("V")) {
						totVinc = totVinc + 1;
					}
				} // fine for
				if (totVinc > 1) {
					throw new ValidationException("gareVincitoreUnico",
							ValidationExceptionCodici.gareVincitoreUnico);

				}

			}
			// non mi sembra che sia obbligatoria almeno una riga
		}

	}

	public static void ValidaRicercaOfferte(
			List<OffertaFornitoreVO> listaOfferte)
			throws ValidationException {
		//
		if (listaOfferte == null || listaOfferte.size() < 1) {
			throw new ValidationException("assenzaRisultati",
					ValidationExceptionCodici.assenzaRisultati);
		}
	}

	public static void ValidaListaSuppOffertaFornitoreVO(
			ListaSuppOffertaFornitoreVO oggettoLista)
			throws ValidationException {

		if (oggettoLista.getCodPolo() != null
				&& oggettoLista.getCodPolo().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodPolo())) {
				throw new ValidationException("ordinierroreCodPoloAlfabetico",
						ValidationExceptionCodici.ordinierroreCodPoloAlfabetico);
			}
			if (oggettoLista.getCodPolo().length() > 3) {
				throw new ValidationException("ordinierroreCodPoloEccedente",
						ValidationExceptionCodici.ordinierroreCodPoloEccedente);
			}

		}

		if (oggettoLista.getCodBibl() != null
				&& oggettoLista.getCodBibl().length() != 0) {

			if (strIsNumeric(oggettoLista.getCodBibl())) {
				throw new ValidationException("ordinierroreCodBiblAlfabetico",
						ValidationExceptionCodici.ordinierroreCodBiblAlfabetico);
			}
			if (oggettoLista.getCodBibl().length() > 3) {
				throw new ValidationException("ordinierroreCodBiblEccedente",
						ValidationExceptionCodici.ordinierroreCodBiblEccedente);
			}
		}

		if (oggettoLista.getFornitore() != null
				&& oggettoLista.getFornitore().getCodice() != null
				&& oggettoLista.getFornitore().getCodice().length() != 0) {
			if (!strIsNumeric(oggettoLista.getFornitore().getCodice())) {
				throw new ValidationException(
						"ordinierroreCampoFornitoreCodiceNumerico",
						ValidationExceptionCodici.ordinierroreCampoFornitoreCodiceNumerico);
			}
			if (oggettoLista.getFornitore() != null
					&& oggettoLista.getFornitore().getCodice().length() > 10) {
				throw new ValidationException(
						"ordinierroreCampoFornitoreCodiceEccedente",
						ValidationExceptionCodici.ordinierroreCampoFornitoreCodiceEccedente);
			}
		}

		if (oggettoLista.getIdentificativoOfferta() != null
				&& oggettoLista.getIdentificativoOfferta().length() != 0) {
			// il campo è alfanumerico e non solo alfabetico
			if (oggettoLista.getIdentificativoOfferta().length() > 10) {
				throw new ValidationException("offertaerroreIDoffEccedente",
						ValidationExceptionCodici.offertaerroreIDoffEccedente);
			}

		}
		if (oggettoLista.getAutore() != null
				&& oggettoLista.getAutore().getDescrizione() != null
				&& oggettoLista.getAutore().getDescrizione().length() != 0) {

			if (strIsNumeric(oggettoLista.getAutore().getDescrizione())) {
				throw new ValidationException("offertaerroreAutoreAlfabetico",
						ValidationExceptionCodici.offertaerroreAutoreAlfabetico);
			}
			if (oggettoLista.getAutore().getDescrizione().length() > 160) {
				throw new ValidationException("offertaerroreAutoreEccedente",
						ValidationExceptionCodici.offertaerroreAutoreEccedente);
			}
		}
		if (oggettoLista.getClassificazione() != null
				&& oggettoLista.getClassificazione().getDescrizione() != null
				&& oggettoLista.getClassificazione().getDescrizione().length() != 0) {

			if (strIsNumeric(oggettoLista.getClassificazione().getDescrizione())) {
				throw new ValidationException(
						"offertaerroreClassifAlfabetico",
						ValidationExceptionCodici.offertaerroreClassifAlfabetico);
			}
			if (oggettoLista.getClassificazione().getDescrizione().length() > 31) {
				throw new ValidationException("offertaerroreClassifEccedente",
						ValidationExceptionCodici.offertaerroreClassifEccedente);
			}
		}

	}

}
