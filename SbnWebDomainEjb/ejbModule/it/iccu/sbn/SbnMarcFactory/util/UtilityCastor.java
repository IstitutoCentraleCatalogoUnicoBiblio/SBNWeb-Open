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
package it.iccu.sbn.SbnMarcFactory.util;

import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityCampiTitolo;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A210;
import it.iccu.sbn.ejb.model.unimarcmodel.A210_GType;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A260;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionebibliografica.Repertorio;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * Title: Interfaccia in diretta
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * Raccolta con gli algoritmi di traduzione tra UNIMARC - SBN e conversioni di
 * vario tipo
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Corrado Di Pietro
 * @version 1.0
 */
public class UtilityCastor {

	/**
	 * Flag indicatore per l'esito della ricerca di un elemento di authority
	 * (autore o marca)
	 */
	private static boolean okIdTrovato = false;

	// Oggetti statici costanti utilizzati da tutta l'applicazione
	public static final String STRINGAVUOTA = "";

	public static final String IID_ESITO_POSITIVO = "Si";

	public static final String IID_ESITO_NEGATIVO = "No";

	// public static final String DATA_VUOTA = "__/__/____";

	/**
	 * Utilizzato da XMLMarche.creaMarca ed XmlAutori.creaAutore Converte la
	 * stringa "Si"/"No" (o la stringa "S"/"N") in un SbnLegameAut
	 *
	 * @param legame
	 *            la stringa "Si" o "No"
	 *
	 * @return il tipo di legame tra authority corrispondente ed il repertorio
	 */
	public SbnLegameAut codificaLegameRepertorio(String legameSiNo) {
		if (legameSiNo.equals(IID_ESITO_POSITIVO)
				|| IID_ESITO_POSITIVO.startsWith(legameSiNo)) {
			return SbnLegameAut.valueOf("810"); // 810 "Si"
		} else {
			return SbnLegameAut.valueOf("815"); // 815 "No"
		}
	}

	/**
	 * Restituisce la natura del documento
	 *
	 * @param documentoType
	 *
	 * @return Natura
	 */
	public String getNaturaDocumento(Object documento) {
		String natura = null;
		if (documento instanceof DocumentoType) {
			DocumentoType titolo = (DocumentoType) documento;
			if (titolo.getDocumentoTypeChoice().getDatiDocumento() != null) {
				// E' un documento
				natura = titolo.getDocumentoTypeChoice().getDatiDocumento()
						.getNaturaDoc().toString();
			} else {
				// E' un titolo di accesso
				natura = titolo.getDocumentoTypeChoice().getDatiTitAccesso()
						.getNaturaTitAccesso().toString();
			}
		} else if (documento instanceof ElementAutType) {
			ElementAutType titoloUniforme = (ElementAutType) documento;
			if (titoloUniforme.getDatiElementoAut().getTipoAuthority().getType() ==
					SbnAuthority.TU_TYPE
					|| titoloUniforme.getDatiElementoAut().getTipoAuthority()
							.getType() == SbnAuthority.UM_TYPE) {
				natura = "A";
			}

		}
		return natura;
	}

	public String getNaturaTitolo(String BID, SBNMarc sbnMarc) {

		UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
		DatiDocType datiDocType = null;
		TitAccessoType titAccessoType = null;
		String natura = "";
		UtilityCastor utilityCastor = new UtilityCastor();
		DocumentoType documentoType = utilityCastor.getElementoDocumento(BID, sbnMarc);
		DatiElementoType datiElementoType = utilityCastor.getElementoAuthority(BID, sbnMarc);

		if (documentoType != null) {
			// E' un titolo
			datiDocType = utilityCampiTitolo.getDatiDocTypeDocumento(documentoType,
					BID);
			titAccessoType = utilityCampiTitolo.getTitAccessoTypeDocumento(
					documentoType, BID);

			if (datiDocType != null) {
				// E' un documento
				natura = datiDocType.getNaturaDoc().toString();
			} else if (titAccessoType != null) {
				// E' un titolo di accesso
				natura = titAccessoType.getNaturaTitAccesso().toString();
			}
		} else {
			// E' un tipo authority
			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
			natura = "A";
			if (datiElementoType != null && datiElementoType instanceof TitoloUniformeType) {
				TitoloUniformeType titUniforme = (TitoloUniformeType) datiElementoType;
				natura = titUniforme.getNaturaTU();
			}
		}
		return natura;

	}

	/**
	 * Questo metodo prende un oggetto Castor di tipo DatiElemento e ritorna il
	 * suo Nominativo (Descrizione) campo T200 per AutorePersonaleType, T210 per
	 * EnteType, T921 per MarcaType, T260 per LuogoType, T250 per SoggettoType e
	 * T230 per TitoloUniformeType e TitoloUniformeMusicaType.
	 *
	 * @param datiElementoAut
	 *
	 * @return Nominativo dell'oggetto Castor di tipo DatiElemento
	 */
	public String getNominativoDatiElemento(DatiElementoType datiElemento) {
		String nominativo = "";
		String qualifica = null;
		String data = null;

		if (datiElemento instanceof AutorePersonaleType) {

			// //////////// AUTORE TYPE

			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			A200 a200;
			a200 = datiElementoAut.getT200();

			nominativo = a200.getA_200();

			// B200
			if (a200.getB_200() != null) {
				nominativo = nominativo + a200.getB_200();
			}

			// DEVO METTERE INSIEME AL NOME LA QUALIFICAZIONE
			qualifica = getQualificazioniDatiAutore(datiElementoAut);

			// DEVO METTERE INSIEME AL NOME LA QUALIFICAZIONE E LA DATAZIONE
			data = getDatazioniDatiElemento(datiElementoAut);

			if ((qualifica != null) || (data != null)) {
				// Modifica almaviva2 06.09.1012 bug esercizio 5104
				// viene eliminato lo spazio in piu, se presente fra ilnome e la qualifica che altrimenti verrebbe
				// reinserito ad ogni successivo allineamento
				nominativo = ValidazioneDati.rtrim(nominativo);
				nominativo = nominativo + " <";
			}

			// AGGIUNGO QUALFIICA
			if (qualifica != null) {
				nominativo = nominativo + qualifica;
			}

			// AGGIUNGO LA DATA SE PRIMA C'E' UNA QUALIFICAZIONE AGGIUNGO
			// PUNTEGGIATURA " ; "
			if (data != null) {
				if (qualifica != null) {
					nominativo = nominativo + " ; " + data;
				} else {
					nominativo = nominativo + data;
				}
			}

			if ((qualifica != null) || (data != null)) {
				nominativo = nominativo + "> ";
			}
		} else if (datiElemento instanceof EnteType) {
			// //////////// ENTE TYPE

			A210 a210;
			EnteType enteType = (EnteType) datiElemento;
			a210 = enteType.getT210();
			nominativo = a210.getA_210();

			String qualifica210 = getQualificazioniDatiAutore(enteType);

			if (qualifica210 != null) {
				// AGGIUNGO LA QUALIFICAZIONE DELLA PARTE PRINCIPALE DEL NOME
				if (!qualifica210.equals(""))
					nominativo = nominativo + " <" + qualifica210 + ">";
			}

			if (a210.getA210_GCount() > 0) {
				A210_GType a210g;

				// TIPO NOME G RIPETITIVO
				for (int i = 0; i < a210.getA210_GCount(); i++) {
					a210g = a210.getA210_G(i);

					nominativo = nominativo + " : " + a210g.getB_210();

					String c210 = "";

					// METTO IL PRIMO ELEMENTO
					if (a210g.getC_210Count() > 0) {
						c210 = " <" + a210g.getC_210(0);
					}

					// C210 AGGIUNGO PUNTEGGIATURA " ; "
					for (int c = 1; c < a210g.getC_210Count(); c++) {
						c210 = c210 + " ; " + a210g.getC_210(c);
					}

					if (a210g.getC_210Count() > 0) {
						c210 = c210 + ">";
					}

					nominativo = nominativo + c210;
				}
			}
		} else if (datiElemento instanceof MarcaType) {

			// //////////// MARCA TYPE

			A921 a921;
			MarcaType marcaType = (MarcaType) datiElemento;
			a921 = marcaType.getT921();
			nominativo = a921.getA_921();
		} else if (datiElemento instanceof LuogoType) {

			// //////////// LUOGO TYPE

			A260 a260;
			LuogoType luogoType = (LuogoType) datiElemento;
			a260 = luogoType.getT260();
			nominativo = a260.getD_260();

		} else if (datiElemento instanceof SoggettoType) {

			// //////////// SOGGETTO TYPE

			SoggettoType soggettoType = (SoggettoType) datiElemento;
			nominativo = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

		} else if (datiElemento instanceof DescrittoreType) {

			// //////////// Descrittore TYPE

			A931 a931;
			DescrittoreType descrittoreType = (DescrittoreType) datiElemento;
			a931 = descrittoreType.getT931();
			nominativo = a931.getA_931();

		} else if (datiElemento instanceof TitoloUniformeType) {

			// //////////// TITOLO UNIFORME


			// Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
			// A230 a230;
			TitoloUniformeType titUniforme = (TitoloUniformeType) datiElemento;
			 if (titUniforme.getT230()  != null) {
				A230 a230;
				a230 = titUniforme.getT230();
				nominativo = a230.getA_230();
             } else if (titUniforme.getT231()  != null) {
     			nominativo = titUniforme.getT231().getA_231();
             } else if (titUniforme.getT431()  != null) {
     			nominativo = titUniforme.getT431().getA_431();
             }

		} else if (datiElemento instanceof TitoloUniformeMusicaType) {

			// //////////// TITOLO UNIMORME MUSICALE

			A230 a230;
			TitoloUniformeMusicaType titUniformeMus = (TitoloUniformeMusicaType) datiElemento;
			a230 = titUniformeMus.getT230();
			nominativo = a230.getA_230();

		}

		return nominativo;
	}

	/**
	 * Indica se il DatiElemnto è una forma di rinvio
	 *
	 * @param datiElemento
	 *            datiElemento
	 *
	 * @return true, se l'autore è una forma di rinvio, false in caso contrario
	 */
	public boolean isFormaRinvio(DatiElementoType datiElemento) {
		if (datiElemento != null) {
			SbnFormaNome sbnForma = datiElemento.getFormaNome();

			if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Utilizzato solo da getNominativoDatiElemento
	 *
	 * @param datiElemento
	 *            info comune a tutte le authority
	 * @return le qualificazioni dell'autore
	 */
	private String getQualificazioniDatiAutore(DatiElementoType datiElemento) {

		A210 a210;
		String qualifica = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;
			A200 a200;
			a200 = datiElementoAut.getT200();
			String c200 = "";

			if (a200.getC_200Count() > 0) {
				qualifica = "";
				// METTO IL PRIMO ELEMENTO
				if (a200.getC_200Count() > 0) {
					c200 = a200.getC_200(0);
				}
				// C200 AGGIUNGO PUNTEGGIATURA " ; "
				for (int c = 1; c < a200.getC_200Count(); c++) {
					c200 = c200 + " ; " + a200.getC_200(c);
				}
				qualifica = qualifica + c200;
			}

		} else if (datiElemento instanceof EnteType) {

			EnteType enteType = (EnteType) datiElemento;
			a210 = enteType.getT210();
			qualifica = "";

			if (a210.getC_210Count() > 0) {
				// METTO IL PRIMO ELEMENTO
				String c210 = a210.getC_210(0);
				// C210 AGGIUNGO PUNTEGGIATURA " ; "
				for (int i = 1; i < a210.getC_210Count(); i++) {
					c210 = c210 + " ; " + a210.getC_210(i);
				}
				qualifica += c210;
			}

			// if (a210.getA210_GCount() > 0) {
			// // METTO IL PRIMO ELEMENTO
			// String a210G = a210.getA210_G(0).getB_210() + " ; ";
			// for (int i = 0; i < a210.getA210_G(0).getC_210Count(); i++)
			// a210G += " ; " + a210.getA210_G(0).getC_210(i);
			//
			// for (int i = 1; i < a210.getA210_GCount(); i++) {
			// A210_GType a210_GType = (A210_GType)a210.getA210_G(i);
			// if (a210.getA210_G(0).getC_210Count() > i) a210G += " ; " +
			// a210.getA210_G(0).getC_210(i);
			//
			// for (int j = 0; j < a210.getA210_G(i).getC_210Count(); j++)
			// if (a210.getA210_GCount() > i &&
			// a210.getA210_G(i).getC_210Count() > j) a210G = a210G + " ; " +
			// a210.getA210_G(i).getC_210(j);
			// }
			//
			// qualifica += a210G;
			// }

			if (a210.getD_210Count() > 0) {
				// METTO IL PRIMO ELEMENTO
				String d210 = a210.getD_210(0);
				// AGGIUNGO PUNTEGGIATURA " ; "
				for (int i = 1; i < a210.getD_210Count(); i++)
					d210 = d210 + " ; " + a210.getD_210(i);
				// qualifica += d210;
				if (!qualifica.equals(""))
					qualifica += " ; " + d210;
				else
					qualifica += d210;
			}

			if (enteType.getTipoNome() != null) {
				// if (ResourceString.AUT_TIPO_NOME_R
				// .equals(enteType.getTipoNome().toString())){
				if ("R".equals(enteType.getTipoNome().toString())) {

					// In caso di ENTE A CARATTERE TEMPORANEO
					// "TIPONOME=R", la sequenza è f-e e non e-f
					if (a210.getF_210() != null) {
						// METTO IL PRIMO ELEMENTO
						String f210 = a210.getF_210();
						if (!qualifica.equals(""))
							qualifica += " ; " + f210;
						else
							qualifica += f210;
					}
					if (a210.getE_210Count() > 0) {
						// METTO IL PRIMO ELEMENTO
						String e210 = a210.getE_210(0);
						// AGGIUNGO PUNTEGGIATURA " ; "
						for (int i = 1; i < a210.getE_210Count(); i++)
							e210 += " ; " + a210.getE_210(i);
						// qualifica += e210;
						if (!qualifica.equals(""))
							qualifica += " ; " + e210;
						else
							qualifica += e210;
					}
				} else {
					if (a210.getE_210Count() > 0) {
						// METTO IL PRIMO ELEMENTO
						String e210 = a210.getE_210(0);
						// AGGIUNGO PUNTEGGIATURA " ; "
						for (int i = 1; i < a210.getE_210Count(); i++)
							e210 += " ; " + a210.getE_210(i);
						// qualifica += e210;
						if (!qualifica.equals(""))
							qualifica += " ; " + e210;
						else
							qualifica += e210;
					}
					if (a210.getF_210() != null) {
						// METTO IL PRIMO ELEMENTO
						String f210 = a210.getF_210();
						if (!qualifica.equals(""))
							qualifica += " ; " + f210;
						else
							qualifica += f210;
					}
				}
			}

		}

		return qualifica;
	}

	/**
	 * Legge la forma "A"(Accettata) o "R"(Rinvio) dell'elemento di authority
	 *
	 * @param datiElemento
	 *            parte comune a tutti gli elementi di authority
	 *
	 * @return "A" per forma accettata, "R" per forma di rinvio
	 */
	public String getFormaDatiElemento(DatiElementoType datiElemento) {
		String forma = null;
		SbnFormaNome sbnForma = datiElemento.getFormaNome();
		forma = "A";

		if (sbnForma.getType() == SbnFormaNome.R_TYPE) {
			forma = "R";
		}

		return forma;
	}

	/**
	 * Calcola la nota informativa estraendola dai dati dell'autore
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return la nota informativa dell'autore
	 */
	public String getNotaInformativaDatiElemento(DatiElementoType datiElemento) {
		String nota = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT300() != null) {
				nota = datiElementoAut.getT300().getA_300();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT300() != null) {
				nota = enteType.getT300().getA_300();
			}
		}

		return nota;
	}

	/**
	 * Ritorna l'indentificativo dell'elemento. Per esempio: il VID per gli
	 * autori,
	 *
	 * @param datiElemento
	 *            DOCUMENT ME!
	 *
	 * @return identificativo.
	 */
	public String getIDDatiElemento(DatiElementoType datiElemento) {
		String id = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			id = datiElementoAut.getT001();
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			id = enteType.getT001();
		}

		return id;
	}

	/**
	 * Ritorna l'indentificativo dell'elemento. Utilizzato per memorizzare l'ID
	 * ritornato con la risposta di avvenuto inserimento, quando il protocollo
	 * restituisce l'ID assegnato all'elemento appena inserito.
	 *
	 * @param sbnMarc
	 *            Albero Castor
	 *
	 * @return identificativo.
	 */
	public String getIDSBNMarc(SBNMarc sbnMarc) {
		String id = null;

		SbnMessageType sbnMessage = sbnMarc.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		if (sbnOutPut.getElementoAutCount() > 0) {

			// ELEMENTO AUTHORITY

			ElementAutType elementAutType = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementAutType.getDatiElementoAut();

			if (datiElemento instanceof AutorePersonaleType) {
				AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;
				id = datiElementoAut.getT001();
			} else if (datiElemento instanceof EnteType) {
				EnteType enteType = (EnteType) datiElemento;
				id = enteType.getT001();
			} else if (datiElemento instanceof MarcaType) {
				MarcaType marcaType = (MarcaType) datiElemento;
				id = marcaType.getT001();
			} else if (datiElemento instanceof SoggettoType) {
				SoggettoType soggettoType = (SoggettoType) datiElemento;
				id = soggettoType.getT001();
			} else if (datiElemento instanceof DescrittoreType) {
				DescrittoreType descrittoreType = (DescrittoreType) datiElemento;
				id = descrittoreType.getT001();
			} else if (datiElemento instanceof TitoloUniformeType) {
				TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
				id = titoloUniformeType.getT001();
			} else if (datiElemento instanceof TitoloUniformeMusicaType) {
				TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;
				id = titoloUniformeMusicaType.getT001();
			} else if ((datiElemento.getT001() != null)
					&& (!datiElemento.getT001().equals(""))) {

				id = datiElemento.getT001();
			}

		}

		else if (sbnOutPut.getDocumentoCount() > 0) {

			// DOCUMENTO

			DocumentoTypeChoice docTypeChoice = sbnOutPut.getDocumento(0)
					.getDocumentoTypeChoice();
			if (docTypeChoice.getDatiDocumento() != null) {
				// DocumentoType
				id = docTypeChoice.getDatiDocumento().getT001();
			} else if (docTypeChoice.getDatiTitAccesso() != null) {
				// Titolo Accesso
				id = docTypeChoice.getDatiTitAccesso().getT001();
			}
		}

		return id;
	}

	/**
	 * Calcola le norme dell'autore estraendole dai dati comuni a tutte le
	 * authority
	 *
	 * @param datiElemento
	 *            dati dell'authority
	 *
	 * @return la norma dell'autore
	 */
	public String getNormeDatiElemento(DatiElementoType datiElemento) {
		String norme = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT152() != null) {
				norme = datiElementoAut.getT152().getA_152();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT152() != null) {
				norme = enteType.getT152().getA_152();
			}
		} else if (datiElemento instanceof TitoloUniformeType) {
			TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;

			if (titoloUniformeType.getT152() != null) {
				norme = titoloUniformeType.getT152().getA_152();
			}
		} else if (datiElemento instanceof TitoloUniformeMusicaType) {
			TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;

			if (titoloUniformeMusicaType.getT152() != null) {
				norme = titoloUniformeMusicaType.getT152().getA_152();
			}
		}

		return norme;
	}

	/**
	 * Calcola l'ISADN dell'autore
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return l'ISADN dell'autore
	 */
	public String getIsadnDatiElemento(DatiElementoType datiElemento) {
		String isadn = null;

		// marzo 2016: gestione ISNI (International standard number identifier)

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

//			if (datiElementoAut.getT015() != null) {
//				isadn = datiElementoAut.getT015().getA_015().trim();
//			}
			if (datiElementoAut.getT010() != null) {
				isadn = datiElementoAut.getT010().getA_010().trim();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

//			if (enteType.getT015() != null) {
//				isadn = enteType.getT015().getA_015().trim();
//			}
			if (enteType.getT010() != null) {
				isadn = enteType.getT010().getA_010().trim();
			}
		}

		return isadn;
	}

	/**
	 * Legge il paese dell'autore dai suoi dati
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return paese dell'autore
	 */
	public String getPaeseDatiElemento(DatiElementoType datiElemento) {
		String paese = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT102() != null) {
				paese = datiElementoAut.getT102().getA_102();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT102() != null) {
				paese = enteType.getT102().getA_102();
			}
		}

		return paese;
	}

	/**
	 * Estrae dai dati dell'elemento di authority la data di inserimento
	 *
	 * @param datiElemento
	 *            dati dell'elemento di authority
	 *
	 * @return la data di inserimento
	 */
	public String getDataInserimentoDatiElemento(DatiElementoType datiElemento) {
		String data = "";

		A100 a100 = datiElemento.getT100();

		if (a100 != null) {
			data = a100.getA_100_0().toString();
		}

		return data;
	}

	/**
	 * Estrae l'agenzia dell'autore dai suoi dati
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return l'agenzia dell'autore
	 */
	public String getAgenziaDatiElemento(DatiElementoType datiElemento) {
		String agenzia = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT801() != null) {
				agenzia = datiElementoAut.getT801().getA_801() + " "
						+ datiElementoAut.getT801().getB_801();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT801() != null) {
				agenzia = enteType.getT801().getA_801() + " "
						+ enteType.getT801().getB_801();
			}
		} else if (datiElemento instanceof TitoloUniformeType) {
			TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;

			if (titoloUniformeType.getT801() != null) {
				agenzia = titoloUniformeType.getT801().getA_801() + " "
						+ titoloUniformeType.getT801().getB_801();
			}
		} else if (datiElemento instanceof TitoloUniformeMusicaType) {
			TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;

			if (titoloUniformeMusicaType.getT801() != null) {
				agenzia = titoloUniformeMusicaType.getT801().getA_801() + " "
						+ titoloUniformeMusicaType.getT801().getB_801();
			}
		}

		return agenzia;
	}

	/**
	 * Estrae la lingua dell'autore dai suoi dati
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return la lingua dell'autore
	 */
	public String getLinguaDatiElemento(DatiElementoType datiElemento) {
		String lingua = null;
		C101 c101 = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT101() != null) {
				c101 = datiElementoAut.getT101();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT101() != null) {
				c101 = enteType.getT101();
			}
		}

		if (c101 != null) {
			if (c101.getA_101Count() > 0) {
				// metto il primo elemento
				lingua = c101.getA_101(0);

				for (int i = 1; i < c101.getA_101Count(); i++) {
					lingua = lingua + " " + c101.getA_101(i);
				}
			}
		}

		return lingua;
	}

	/**
	 * Estrae la nota del catalogatore dell'autore dai suoi dati
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return la nota del catalogare dell'autore
	 */
	public String getNotaDelCatalogatoreDatiElemento(
			DatiElementoType datiElemento) {
		String nota = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			if (datiElementoAut.getT830() != null) {
				nota = datiElementoAut.getT830().getA_830();
			}
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;

			if (enteType.getT830() != null) {
				nota = enteType.getT830().getA_830();
			}
		}

		return nota;
	}

	/**
	 * Estrae le datazioni dell'autore dai suoi dati
	 *
	 * @param datiElemento
	 *            dati dell'autore
	 *
	 * @return le datazioni dell'autore
	 */
	public String getDatazioniDatiElemento(DatiElementoType datiElemento) {
		A210 a210;
		String data = null;

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;

			A200 a200;

			a200 = datiElementoAut.getT200();

			data = a200.getF_200();
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;
			a210 = enteType.getT210();
			data = a210.getA_210();

			data = a210.getF_210();
		}

		if (data != null) {
			if (data.equals(" ")) {
				data = null;
			}
		}

		return data;
	}

	/**
	 * Questo metodo prende un oggetto Castor dell tipo DatiElemento e ritorna
	 * il suo Tipo Nome
	 *
	 *
	 * @param datiElemento
	 *
	 * @return Tipo Nome
	 */
	public String getTipoNomeDatiElemento(DatiElementoType datiElemento) {
		String tipoNome = "";

		if (datiElemento instanceof AutorePersonaleType) {
			AutorePersonaleType datiElementoAut = (AutorePersonaleType) datiElemento;
			tipoNome = datiElementoAut.getTipoNome().toString();
		} else if (datiElemento instanceof EnteType) {
			EnteType enteType = (EnteType) datiElemento;
			tipoNome = enteType.getTipoNome().toString();
		}

		return tipoNome;
	}

	/**
	 * Crea un array di tipi nome autore di tipo DOM Castor SbnTipoNomeAutore
	 * partendo da un array di boolean, true se il tipo nome deve essere incluso
	 * false altrimenti.
	 *
	 * @param tipoNome
	 *            lista di 7 flag di inclusione per i tipi nome corrispondenti
	 *            alle diverse posizioni dell'array
	 *
	 * @return un array di tipi nome
	 */
	public SbnTipoNomeAutore[] converteTipoNome(boolean[] tipoNome) {
		List lista = new ArrayList();

		if (tipoNome[0]) {
			lista.add(SbnTipoNomeAutore.A);
		}

		if (tipoNome[1]) {
			lista.add(SbnTipoNomeAutore.B);
		}

		if (tipoNome[2]) {
			lista.add(SbnTipoNomeAutore.C);
		}

		if (tipoNome[3]) {
			lista.add(SbnTipoNomeAutore.D);
		}

		if (tipoNome[4]) {
			lista.add(SbnTipoNomeAutore.E);
		}

		if (tipoNome[5]) {
			lista.add(SbnTipoNomeAutore.R);
		}

		if (tipoNome[6]) {
			lista.add(SbnTipoNomeAutore.G);
		}

		SbnTipoNomeAutore[] sbnTipoNome = new SbnTipoNomeAutore[lista.size()];

		for (int j = 0; j < lista.size(); j++) {
			sbnTipoNome[j] = (SbnTipoNomeAutore) lista.get(j);
		}

		return sbnTipoNome;
	}

	/**
	 * Preleva la data odierna
	 *
	 * @return la data odierna
	 */
	public String getCurrentDate() {
		java.util.Date currentDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale
				.getDefault());

		return formatter.format(currentDate);
	}

	/**
	 * Questo metodo indica se ci sono elementi trovati.
	 *
	 * @param sbnMarcType
	 *            risposta del protocollo
	 * @param frame
	 *            finestra
	 *
	 * @return true, se ci sono elementi trovati, false in caso contrario.
	 */
	// public static boolean elementiTrovati(SBNMarc sbnMarcType, JFrameRoot
	// frame) {
	// //////////////////////////////////////////////////////////////////////
	// ////////////////////// CASTOR ////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////
	// SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	// SbnResultType sbnResult = sbnResponse.getSbnResult();
	// String esito = sbnResult.getEsito();
	// String testoEsito = sbnResult.getTestoEsito();
	//
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
	//
	// // Per il caso di richiesta del profilo:
	// try {
	// if (sbnResponse.getSbnResponseTypeChoice().getSbnUserProfile() != null) {
	// return true;
	// }
	// } catch (Exception ex) {
	// }
	//
	// // Per gli altri casi la risposta non è un profilo e prevede
	// // la presenza di un nodo SbnOutPut
	// int totRighe = sbnOutPut.getTotRighe();
	//
	// if (totRighe == 0) {
	// // nessuon elemento trovato
	//
	// /* JFrameMessageDialog frameMessage = new JFrameMessageDialog(frame,
	// ResourceString.IID_ERRORE,
	// esito + ResourceString.IID_SPAZIO + testoEsito,
	// JFrameMessageDialog.ERROR_MESSAGE);*/
	// return false;
	// } else {
	// return true;
	// }
	// }
	/**
	 * Questo metodo indica se la risposta del protocollo ha restituito un solo
	 * elemento.
	 *
	 * @param sbnMarcType
	 *            risposta del protocollo.
	 *
	 * @return true se è stato trovato un solo elemento.
	 */
	public boolean isUnicoElementoTrovato(SBNMarc sbnMarcType) {
		// //////////////////////////////////////////////////////////////////////
		// ////////////////////// CASTOR
		// ////////////////////////////////////////
		// //////////////////////////////////////////////////////////////////////
		// SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		// SbnResultType sbnResult = sbnResponse.getSbnResult();
		// SbnResponseTypeChoice sbnResponseChoice =
		// sbnResponse.getSbnResponseTypeChoice();
		// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		//
		// // Numero di elementi trovati
		// int totRighe = sbnOutPut.getTotRighe();
		//
		// if (totRighe == 1) {
		// // Un solo elemento trovato
		// return true;
		// } else {
		// // Zero o più di un elemento trovati
		// return false;
		// }

		// Al momento la lista sintetica deve essere
		// visualizzata sempre, anche a fronte di
		// un unico elemento trovato.
		return false;
	}

	/**
	 * Questo metodo indica se ci sono elementi simili trovati
	 *
	 * @param sbnMarcType
	 *            risposta del protocollo
	 *
	 * @return true, se ci sono elementi, false caso contrario.
	 */
	public boolean isElementiSimiliTrovati(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		if (sbnMarcType != null) {
			SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();
			String testoEsito = sbnResult.getTestoEsito();

			SbnResponseTypeChoice sbnResponseChoice = sbnResponse
					.getSbnResponseTypeChoice();
			SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
			int totRighe = sbnOutPut.getTotRighe();

			// Bypass di altri errori che verranno
			// gestiti da altri metodi:
			// 3004, Elementi simili trovati - metodo isElementiSimiliTrovati
			// 3003, Troppi elementi trovati - metodo isTroppiElementiTrovati
			// if (esito.equals("3004") || (esito.equals("3003"))) {
			if (esito.equals("3004")/* || (esito.equals("3003")) */) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isElementiTrovati(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		// int totRighe = sbnOutPut.getTotRighe();

		// Bypass di altri errori che verranno
		// gestiti da altri metodi:
		// 3004, Elementi simili trovati - metodo isElementiSimiliTrovati
		// 3333, Troppi elementi trovati - metodo isTroppiElementiTrovati
		// 3014, Oggetto modificato da altro utente - metodo
		// isOggettoModificatoAltroUtente
		if ((esito.equals("3004")) ||
		// (esito.equals("3333")) ||
				(esito.equals("3014"))) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isError3001(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();

		// 3001, Nessun elemento trovato
		if (esito.equals("3001")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isError3333(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();

		// 3333, Troppi elementi trovati la richiesta verrà schedulata in
		// differita
		if (esito.equals("3333")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Restituisce true se il numero degli elementi trovati è troppo elevato e
	 * quindi è impossibile visualizzare il risultato della ricerca.
	 *
	 * @param sbnMarcType
	 *            Albero Castor con la risposta SBN
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isTroppiElementiTrovati(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		if (esito.equals("3003")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Restituisce true se l'oggetto per cui si sta facendo la richiesta di
	 * variazione, è già stato modificato da un altro utente.
	 *
	 * @param sbnMarcType
	 *            Albero Castor con la risposta SBN
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isOggettoModificatoAltroUtente(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		if (esito.equals("3014")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isErroreDiRecupero(SBNMarc sbnMarcType) {
		// ////////////////////////////////////////////////////////////////////
		// //////////////////// CASTOR ////////////////////////////////////////
		// ////////////////////////////////////////////////////////////////////
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		if (esito.equals("1020")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indica se il DatiElementoType e' un autore
	 *
	 * @param datiElemento
	 *            datiElemento
	 *
	 * @return true se e' un autore, false caso contrario
	 */
	public boolean isAutore(DatiElementoType datiElemento) {
		if ((datiElemento instanceof AutorePersonaleType)
				|| (datiElemento instanceof EnteType)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indica se il DatiElementoType e' una marca
	 *
	 * @param datiElemento
	 *            datiElelemento
	 *
	 * @return true se e' una marca, false caso contrario.
	 */
	public boolean isMarca(DatiElementoType datiElemento) {
		if ((datiElemento instanceof MarcaType)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indica se il DatiElementoType e' un soggetto
	 *
	 * @param datiElemento
	 *            datiElelemento
	 *
	 * @return true se e' un soggetto, false caso contrario.
	 */
	public boolean isSoggetto(DatiElementoType datiElemento) {
		if ((datiElemento instanceof SoggettoType)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indica se il DatiElementoType e' un descrittore
	 *
	 * @param datiElemento
	 *            datiElelemento
	 *
	 * @return true se e' un descrittore, false caso contrario.
	 */
	public boolean isDescrittore(DatiElementoType datiElemento) {
		if ((datiElemento instanceof DescrittoreType)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Controlla il tipo di lista sintetica restituita. Se è una lista BIS
	 * allora viene effettuato il controllo sulla congruenza o meno del tipo di
	 * lista, in relazione alla linea corrente. Restituisce true se la lista è
	 * congruente con la linea corrente. Restituisce false se la lista non è
	 * congruente alla linea corrente.
	 *
	 * @param sbnListaRicevuta
	 *            SBNMarc albero Castor con la lista sintetica
	 * @param sbnAuthorityCorrente
	 *            SbnAuthority Tipo Authority della linea corrente
	 * @param frame
	 *            JFrameRoot corrente
	 *
	 * @return true se la lista è congruente con la linea corrente, false se
	 *         viceversa.
	 */
	// SBNMarc sbnListaRicevuta,
	// SbnAuthority sbnAuthorityCorrente,
	// JFrameRoot frame){
	//
	// SbnMessageType sbnMessage = sbnListaRicevuta.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutputType = sbnResponseChoice.getSbnOutput();
	//
	// // Se l'array DocumentoType[] non ha lunghezza zero,
	// // ci sono dei dati di tipo DOCUMENTO
	// if (sbnOutputType.getDocumento().length != 0){
	//
	// // LISTA DI DOCUMENTI (LINEA TITOLI)
	// if ( (sbnAuthorityCorrente == SbnAuthority.TU) ||
	// (sbnAuthorityCorrente == SbnAuthority.UM) ){
	//
	// // Tipo Authority congruente
	// return true;
	// } else {
	// // Tipo Authority non congruente
	// JFrameMessageDialog frameMessage =
	// new JFrameMessageDialog(frame,
	// ResourceString.IID_ERRORE,
	// ResourceString.IID_ERR_CONGRUENZA_LISTABIS,
	// JFrameMessageDialog.ERROR_MESSAGE);
	// return false;
	// }
	//
	// }// end if
	//
	// // Altrimenti, se l'array DocumentoType[] è vuoto, i dati
	// // sono del tipo ELEMENTI DI AUTHORITY
	// else {
	//
	// if (sbnOutputType.getElementoAutCount() > 0){
	//
	// // LISTA DI ELEMENTI DI AUTHORITY
	// DatiElementoType datiElemento = sbnOutputType
	// .getElementoAut()[0].getDatiElementoAut();
	//
	// if (sbnAuthorityCorrente == datiElemento.getTipoAuthority() ){
	// // Tipo Authority congruente
	// return true;
	// } else {
	// // SE LA LINEA CORRENTE E' QUELLA DEI TITOLI,
	// // CONTROLLA CHE LA LISTA BIS RESTITUITA ABBIA
	// // AUTHORITY TU O UM PER DARE ESITO POSITIVO,
	// // ALTRIMENTI LA LISTA NON E' CONGRUENTE
	//
	// if ( ((sbnAuthorityCorrente == SbnAuthority.TU) ||
	// (sbnAuthorityCorrente == SbnAuthority.UM)) &&
	// ((datiElemento.getTipoAuthority() == SbnAuthority.TU) ||
	// (datiElemento.getTipoAuthority() == SbnAuthority.UM)) ){
	//
	// // Tipo Authority congruente
	// return true;
	//
	// } else {
	// // Tipo Authority non congruente
	// JFrameMessageDialog frameMessage =
	// new JFrameMessageDialog(frame,
	// ResourceString.IID_ERRORE,
	// ResourceString.IID_ERR_CONGRUENZA_LISTABIS,
	// JFrameMessageDialog.ERROR_MESSAGE);
	// return false;
	// }
	// }
	//
	// } else {
	// // Nessun Elemento Authority trovato
	// // Non c'è messaggio di errore in quanto
	// // viene già visualizzato a livello più alto.
	// return false;
	// }
	//
	// }// end else
	//
	// }// end isOkListaPerLineaCorrente
	/**
	 * Prende il DatiElementoType dell'autore o la marca trovato nell'albero
	 * castor sbnMarc. Questo metodo serve per gli autori e per le marche.
	 *
	 * @param ID
	 *            id dell'elemento.
	 * @param sbnMarc
	 *            albero castor
	 * @return datiElemento del ID trovato, null se non lo trova nell'albero
	 *         castor.
	 */
	public DatiElementoType getDatiElemento(String ID, SBNMarc sbnMarc) {
		DatiElementoType ritorno = null;
		SbnMessageType sbnMessage = sbnMarc.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();
		int count = 0;

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();
		ElementAutType elementoAut = null;
		DatiElementoType datiElemento = null;
		DocumentoType documentoType = null;
		DocumentoTypeChoice documentoTypeChoice = null;
		DatiDocType datiDocType = null;
		LegamiType legamiType = null;

		// RICERCA INFO NELLA RADICE ///////////////////////////////////////////
		// RADICE FORMA ACCETTATA //////////////////////////////////////////////
		if (sbnOutPut.getElementoAutCount() > 0) {
			elementoAut = sbnOutPut.getElementoAut(0);
			datiElemento = elementoAut.getDatiElementoAut();

			// LA RADICE E' L'AUTORE CHE STO CERCANDO ?
			if (datiElemento.getT001().equals(ID)) {
				ritorno = datiElemento;

				return ritorno;
			}
		} else {
			if (sbnOutPut.getDocumentoCount() > 0) {
				// QUESTO PER I TITOLI !!!
				documentoType = sbnOutPut.getDocumento(0);
				documentoTypeChoice = documentoType.getDocumentoTypeChoice();
				datiDocType = documentoTypeChoice.getDatiDocumento();
			}
		}

		// //////////////// RICERCA INFO IN LEGAMI ////////////////
		if (elementoAut != null) {
			count = elementoAut.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		// boolean ok = false;
		int i = 0;

		// while ((!ok) && (i < count)) {
		while (i < count) {
			if (elementoAut != null) {
				legamiType = elementoAut.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			// Ricerca ricorsiva dell'ID tra i legami dell'albero castor
			ritorno = getDatiElementoLegame(legamiType, ID, null);

			// for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
			// ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
			// LegameElementoAutType legameElemento =
			// arrivoLegame.getLegameElementoAut();
			// LegameDocType legameDocType = arrivoLegame.getLegameDoc();
			// LegameTitAccessoType legameTitAccessoType =
			// arrivoLegame.getLegameTitAccesso();
			//
			// if (legameElemento != null) {
			// ElementAutType elementAutType =
			// legameElemento.getElementoAutLegato();
			//
			// // ID del legame
			// String IDLegato = elementAutType.getDatiElementoAut()
			// .getT001();
			//
			// // SE IL ID LEGATO(VID o MID) E' UGUALE AL VID
			// // DELLA FORMA DI RINVIO CHE STO CERCANDO ALLORA
			// // HO TROVATO LA FORMA DI RINVIO !! EUREKA !!
			// if (IDLegato.equals(ID)) {
			// ok = true;
			// ritorno = elementAutType.getDatiElementoAut();
			//
			// break;
			// } else {
			// // SE L'ID LEGATO NON E' UGUALE A QUELLO
			// // CERCATO, CERCA IN EVENTUALI ALTRI LEGAMI
			// System.out.println("arrivi legami:
			// "+legamiType.getArrivoLegameCount());
			// System.out.println("altri legami:
			// "+elementAutType.getLegamiElementoAutCount());
			// }
			//
			// }
			// }

			// Se non è stato ancora trovato l'ID dell'elemento
			// cercato, incrementa l'indice per un nuovo ciclo
			// di ricerca, altrimenti esce subito dal ciclo.
			if (ritorno == null) {
				i++;
			} else {
				break;
			}
		}// end while() per la ricerca tra legami

		return ritorno;

	}// end getDatiElemento

	/**
	 * Prende il DatiElementoType dell'autore o la marca trovato tra i legami
	 * nell'albero castor sbnMarc. Fa una ricerca ricorsiva tra i vari nodi
	 * trovati. Questo metodo serve per gli autori e per le marche.
	 *
	 * @param legamiType
	 *            LegamiType nei quali si vuole cercare l'ID dell'elemento.
	 * @param idCercato
	 *            identificativo dell'elemento cercato.
	 * @param datiElem
	 *            DatiElementoType dell'ID, null se non è stato ancora trovato
	 *            tra i legami.
	 * @return DatiElementoType dell'ID, null se non lo trova tra i legami.
	 */
	private DatiElementoType getDatiElementoLegame(
			LegamiType legamiType, String idCercato, DatiElementoType datiElem) {

		DatiElementoType ritorno = null;

		if (datiElem != null) {
			ritorno = datiElem;
		} else {
			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				// LEGAME A ELEMENTO AUTHORITY
				if (legameElemento != null) {
					ElementAutType elementAutType = legameElemento
							.getElementoAutLegato();

					if (elementAutType != null) {
						// ID del legame
						String IDLegato = elementAutType.getDatiElementoAut()
								.getT001();

						// SE IL ID LEGATO(VID o MID) E' UGUALE AL VID
						// DELLA FORMA DI RINVIO CHE STO CERCANDO ALLORA
						// HO TROVATO LA FORMA DI RINVIO !!!
						if (IDLegato.equals(idCercato)) {
							okIdTrovato = true;
							ritorno = elementAutType.getDatiElementoAut();
							break;
						} else {
							// SE L'ID LEGATO NON E' UGUALE A QUELLO
							// CERCATO, CERCA IN EVENTUALI ALTRI LEGAMI
							if (elementAutType.getLegamiElementoAutCount() > 0) {
								for (int p = 0; p < elementAutType
										.getLegamiElementoAutCount(); p++) {
									// RICERCA RICORSIVA TRA I LEGAMI
									ritorno = getDatiElementoLegame(
											elementAutType
													.getLegamiElementoAut(p),
											idCercato, ritorno);
								}
							}
						}
					}
				}

				// LEGAME A DOCUMENTO TYPE
				if (legameDocType != null) {
					DocumentoType docType = legameDocType.getDocumentoLegato();

					if (docType != null) {
						// SE IL DOCUMENTO TROVATO HA DEI LEGAMI,
						// CERCA AL SUO INTERNO IL DatiElementoType
						if (docType.getLegamiDocumentoCount() > 0) {
							for (int p = 0; p < docType
									.getLegamiDocumentoCount(); p++) {
								// RICERCA RICORSIVA TRA I LEGAMI
								ritorno = getDatiElementoLegame(docType
										.getLegamiDocumento(p), idCercato,
										ritorno);
							}
						}
					}
				}

				// LEGAME A TITOLO ACCESSO
				// if (legameTitAccessoType != null) {
				// TitAccessoType titAccessoType =
				// legameTitAccessoType.getTitAccessoLegato();
				//
				// if (titAccessoType != null){
				// // SE IL DOCUMENTO TROVATO HA DEI LEGAMI,
				// // CERCA AL SUO INTERNO IL DatiElementoType
				// if (titAccessoType.getTitAccessoTypeChoice(). > 0){
				// for (int p=0; p<docType.getLegamiDocumentoCount(); p++){
				// // RICERCA RICORSIVA TRA I LEGAMI
				// ritorno = getDatiElementoLegame(
				// docType.getLegamiDocumento(p),
				// idCercato,
				// ritorno);
				// }
				// }
				// }
				// }

			}
		}

		return ritorno;

	}// end getDatiElementoLegame

	/**
	 * Analizza il codice dell'esito ritornato nella risposta xml del server, un
	 * codice uguale a "0000" significa esito positivo.
	 *
	 * @param sbnMarcType
	 *            la radice dell'albero DOM Castor contenente la risposta del
	 *            server
	 *
	 * @return true se il codice dell'esito contenuto nell'albero è "0000",
	 *         ossia esito positivo.
	 */
	public boolean isEsitoPositivo(SBNMarc sbnMarcType) {
		boolean resultatoEsito = false;

		if (sbnMarcType != null) {
			SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();
			String esito = sbnResult.getEsito();

			if (esito.equals("0000") || esito.equals("3089") // Caso della
			// richiesta di
			// export
			) {
				resultatoEsito = true;
			}
		}

		return resultatoEsito;
	}

	/**
	 * Analizza il codice dell'esito ritornato nella risposta xml del server, un
	 * codice uguale a "0000" significa esito positivo.
	 *
	 * @param sbnMarcType
	 *            la radice dell'albero DOM Castor contenente la risposta del
	 *            server
	 * @return il testo dell'esito riguardante la richiesta processata dal
	 *         server
	 */
	public String getTestoEsito(SBNMarc sbnMarcType) {
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		// String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		return testoEsito;
	}

	// //////////////////////////////////////////////
	// //////////////////////////////////////////////
	// 20/05/2004 //
	// COMMENTATI DOPO L'AGGIUNTA DELL'ATTRIBUTO //
	// DATILEGAME ALL'IIDDefaultMutableTreeNode //
	// //////////////////////////////////////////////
	// //////////////////////////////////////////////

	/**
	 * Prende i dati di legame (Nota la legame, relator code, incerto, ecc.)
	 * dell'elemento collegato.
	 *
	 * @param id
	 *            id dell'elemento collegato
	 * @param sbnMarcType
	 *            albero castor
	 * @param idPadre
	 *            id del padre dell'elemento collegato
	 *
	 * @return ritorna un'istanza della classe DatiLegame, null se non trova
	 *         l'elemento.
	 */
	public DatiLegame getDatiLegameElemento(String id, SBNMarc sbnMarcType,
			String idPadre) {

		// CASO DI TEST (ROMA): UM1E009133

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		DatiLegame datiLegame = null;
		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;
		DatiElementoType datiElemento = null;
		Esito esito = new Esito(false);

		boolean rootIsPadre = false;

		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;

		// LEGGO SBNOUTPUT.DOCUMENTO O SBNOUTPUT.DOCUMENTO.DATITITACCESSO 1^
		// LIVELLO
		if (sbnOutPut.getDocumentoCount() > 0) {
			documentoType = sbnOutPut.getDocumento(0);

			// /////////
			// Controlla se il primo elemento dell'albero Castor
			// è o meno il padre dell'elemento di cui si
			// stanno cercando i dati di legame.

			DatiDocType datiDocType = documentoType.getDocumentoTypeChoice()
					.getDatiDocumento();
			TitAccessoType titAccessoType = documentoType
					.getDocumentoTypeChoice().getDatiTitAccesso();
			if (datiDocType != null) {
				if (datiDocType.getT001().equals(idPadre)) {
					// Il root è il padre dell'elemento legato
					rootIsPadre = true;

				}
			} else if (titAccessoType != null) {
				if (titAccessoType.getT001().equals(idPadre)) {
					// Il root è il padre dell'elemento legato
					rootIsPadre = true;

				}
			}

			if (documentoType.getLegamiDocumentoCount() > 0) {

				if (rootIsPadre) {
					// L'elemento cercato è tra i legami al primo
					// livello dell'elemento root.

					LegamiType legamiType = null;
					ArrivoLegame arrivoLegame = null;

					for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {

						legamiType = documentoType.getLegamiDocumento()[i];

						if (datiLegame == null) {

							for (int j = 0; j < legamiType
									.getArrivoLegameCount(); j++) {

								if (datiLegame == null) {

									arrivoLegame = legamiType
											.getArrivoLegame(j);

									if (arrivoLegame.getLegameDoc() != null) {

										// DOCUMENTO LEGATO AL 1° LIVELLO

										LegameDocType legameDocumento = arrivoLegame
												.getLegameDoc();
										documentoType2 = legameDocumento
												.getDocumentoLegato();

										if (legameDocumento.getIdArrivo()
												.equals(id)) {
											// Elemento TROVATO

											datiLegame = new DatiLegame();

											// prendo la nota al legame
											datiLegame
													.setNotaLegame(legameDocumento
															.getNoteLegame());

											// prendo il tipo legame
											datiLegame
													.setTipoLegame(legameDocumento
															.getTipoLegame()
															.toString());

											// prendo la sequenza
											datiLegame
													.setSequenza(legameDocumento
															.getSequenza());

											// prendo il sici
											datiLegame.setSici(legameDocumento
													.getSici());
										}

									} else if (arrivoLegame
											.getLegameTitAccesso() != null) {

										// TITOLO ACCESSO LEGATO AL 1° LIVELLO

										LegameTitAccessoType legameTitAccesso = arrivoLegame
												.getLegameTitAccesso();
										documentoType2 = legameTitAccesso
												.getTitAccessoLegato();

										if (legameTitAccesso.getIdArrivo()
												.equals(id)) {
											// Elemento TROVATO

											datiLegame = new DatiLegame();

											// prendo la nota al legame
											datiLegame
													.setNotaLegame(legameTitAccesso
															.getNoteLegame());

											// prendo il tipo legame
											datiLegame
													.setTipoLegame(legameTitAccesso
															.getTipoLegame()
															.toString());

											// prendo la sequenza
											datiLegame
													.setSequenzaMusica(legameTitAccesso
															.getSequenzaMusica());

											// prendo il sottotipolegame
											if (legameTitAccesso
													.getSottoTipoLegame() != null) {
												datiLegame
														.setSottoTipoLegame(legameTitAccesso
																.getSottoTipoLegame()
																.toString());
											}
										}

									} else if (arrivoLegame
											.getLegameElementoAut() != null) {

										// ELEM.AUTHORITY LEGATO AL 1° LIVELLO

										LegameElementoAutType legameElemAut = arrivoLegame
												.getLegameElementoAut();
										elemen2 = legameElemAut
												.getElementoAutLegato();

										if (legameElemAut.getIdArrivo().equals(
												id)) {
											// Elemento TROVATO

											datiLegame = new DatiLegame();

											// prendo la nota al legame
											datiLegame
													.setNotaLegame(legameElemAut
															.getNoteLegame());

											// prendo il tipo legame
											datiLegame
													.setTipoLegame(legameElemAut
															.getTipoLegame()
															.toString());

											// prendo il tipo respons
											if (legameElemAut.getTipoRespons() != null) {
												datiLegame
														.setResponsabilita(legameElemAut
																.getTipoRespons()
																.toString());
											}

											// prendo il relator code
											datiLegame
													.setRelatorCode(legameElemAut
															.getRelatorCode());

											// prendo l'incerto
											if (legameElemAut.getIncerto() != null) {
												if (legameElemAut
														.getIncerto()
														.getType() == SbnIndicatore.N_TYPE) {
													datiLegame
															.setIncerto(false);
												} else {
													datiLegame.setIncerto(true);
												}
											}
										}
									}

								} else {
									break;
								}

							}// end for interno sugli arrivolegami al 1°
							// livello del root

						} else {
							break;
						}

					}// end for esterno sui legami al 1° livello del root

				} else {
					// L'elemento cercato non è tra i legami al primo
					// livello del root, quindi cerca nei sotto-legami
					// dei legami di primo livello.
					datiLegame = getDatiLegameElemento(sbnMarcType,
							documentoType, null, id, idPadre, esito);
				}
			}

		}

		// LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
		if (datiElemento == null) {
			// if (datiLegame == null) {
			if (sbnOutPut.getElementoAutCount() > 0) {
				elementoAut = sbnOutPut.getElementoAut(0);
				datiElemento = elementoAut.getDatiElementoAut();

				if (!datiElemento.getT001().equals(id)) {
					datiLegame = getDatiLegameElemento(sbnMarcType, null,
							elementoAut, id, idPadre, esito);
				}
			}
		}

		return datiLegame;
	}

	/**
	 * Metodo utilizzato intermanente, usare getDatiLegameElemento(String id,
	 * SBNMarc sbnMarcType, String idPadre)
	 *
	 * @param sbnOutPut
	 *            DOCUMENT ME!
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 * @param documentoType
	 *            DOCUMENT ME!
	 * @param elemen
	 *            DOCUMENT ME!
	 * @param id
	 *            DOCUMENT ME!
	 * @param idPadre
	 *            DOCUMENT ME!
	 * @param esito
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DatiLegame getDatiLegameElemento(SBNMarc sbnMarcType,
			DocumentoType documentoType, ElementAutType elemen, String id,
			String idPadre, Esito esito) {

		DocumentoTypeChoice documentoTypeChoice = null;
		DatiDocType datiDocType = null;
		DatiElementoType datiElemento = null;

		DatiLegame datiLegame = null;

		if (documentoType != null) {
			documentoTypeChoice = documentoType.getDocumentoTypeChoice();
			datiDocType = documentoTypeChoice.getDatiDocumento();
		}

		LegamiType legamiType = null;
		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;
		int count = 0;

		// il count serve per tutte due elementi di authority o titoli
		if (elemen != null) {
			count = elemen.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		int i = 0;

		while ((!esito.getEsito()) && (i < count)) {
			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			int j = 0;

			while ((!esito.getEsito())
					&& (j < legamiType.getArrivoLegameCount())) {

				// LEGGO LEGAMEELEMENTOAUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {

					// if (legameElemento.getIdArrivo().equals(idPadre)){

					elemen2 = legameElemento.getElementoAutLegato();

					datiLegame = new DatiLegame();

					// prendo la nota al legame
					datiLegame.setNotaLegame(legameElemento.getNoteLegame());

					// prendo il tipo legame
					datiLegame.setTipoLegame(legameElemento.getTipoLegame()
							.toString());

					// prendo il tipo respons
					if (legameElemento.getTipoRespons() != null) {
						datiLegame.setResponsabilita(legameElemento
								.getTipoRespons().toString());
					}

					// prendo il relator code
					datiLegame.setRelatorCode(legameElemento.getRelatorCode());

					// prendo l'incerto
					if (legameElemento.getIncerto() != null) {
						if (legameElemento.getIncerto().getType() == SbnIndicatore.N_TYPE) {
							datiLegame.setIncerto(false);
						} else {
							datiLegame.setIncerto(true);
						}
					}

					if ((elemen2 != null)
							&& (elemen2.getLegamiElementoAutCount() > 0)) {

						datiElemento = elemen2.getDatiElementoAut();
						String idCercato = datiElemento.getT001().trim();

						if (idCercato.equals(id)) {
							esito.setEsito(true);
						} else {
							datiLegame = getDatiLegameElemento(sbnMarcType,
									documentoType2, elemen2, id, idPadre, esito);
						}
					} else {
						if (elemen2 != null) {
							// e' un tipo authority foglia del albero
							datiElemento = elemen2.getDatiElementoAut();

							String idCercato = datiElemento.getT001().trim();

							// System.out.println("id cercato: " + idCercato);
							if (idCercato.equals(id)) {
								esito.setEsito(true);
							}
						}
					}

					// }

				} else

				// SE E UN LEGAME DOC
				if (legameDocType != null) {

					documentoType2 = legameDocType.getDocumentoLegato();

					String idCercato = documentoType2.getDocumentoTypeChoice()
							.getDatiDocumento().getT001().trim();

					datiLegame = new DatiLegame();

					// prendo la nota al legame
					datiLegame.setNotaLegame(legameDocType.getNoteLegame());

					// prendo il tipo legame
					datiLegame.setTipoLegame(legameDocType.getTipoLegame()
							.toString());

					// prendo la sequenza
					datiLegame.setSequenza(legameDocType.getSequenza());

					// prendo il sici
					datiLegame.setSici(legameDocType.getSici());

					if (idCercato.equals(id)) {
						esito.setEsito(true);
					} else {
						if (haLegami(documentoType2)) {
							datiLegame = getDatiLegameElemento(sbnMarcType,
									documentoType2, null, id, idPadre, esito);
						}
					}

				} else

				// SE EUN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {

					documentoType2 = legameTitAccessoType.getTitAccessoLegato();

					String idCercato = documentoType2.getDocumentoTypeChoice()
							.getDatiTitAccesso().getT001().trim();

					datiLegame = new DatiLegame();

					// prendo la nota al legame
					datiLegame.setNotaLegame(legameTitAccessoType
							.getNoteLegame());

					// System.out.println("prendo il legame ti tit accesso:
					// "+legameTitAccessoType.getTipoLegame().toString());
					// prendo il tipo legame
					datiLegame.setTipoLegame(legameTitAccessoType
							.getTipoLegame().toString());

					// prendo la sequenza
					datiLegame.setSequenzaMusica(legameTitAccessoType
							.getSequenzaMusica());

					// prendo il sottotipolegame
					if (legameTitAccessoType.getSottoTipoLegame() != null) {
						datiLegame.setSottoTipoLegame(legameTitAccessoType
								.getSottoTipoLegame().toString());
					}

					if (idCercato.equals(id)) {
						esito.setEsito(true);
					} else {
						if (haLegami(documentoType2)) {
							datiLegame = getDatiLegameElemento(sbnMarcType,
									documentoType2, null, id, idPadre, esito);
						}
					}

					// }

				}

				// INCREMENTO L'INDICE FIN CHE NON TROVO
				// L'ELEMENTO CHE STO CERCANDO
				j++;

			}

			// INCREMENTO L'INDICE FIN CHE NON TROVO
			// L'ELEMENTO CHE STO CERCANDO
			i++;
		}

		if (!esito.getEsito()) {
			datiLegame = null;
		}

		return datiLegame;
	}

	// ////////////////////////////////////////////
	// ////////////////////////////////////////////
	// FINE DEL COMMENTO //
	// 20/05/2004 //
	// ////////////////////////////////////////////
	// ////////////////////////////////////////////

	// //////////////////////////////////////////////////
	// //////////////////////////////////////////////////

	// COMMENTO: PEPPE
	// VECCHIO CODICE DEI DUE METODI getDatiLegameElemento
	// COMMENTATO IN DATA 20/04/2004

	/**
	 * Prende i dati di legame (Nota la legame, relator code, incerto, ecc.)
	 * dell'elemento collegato.
	 *
	 * @param id
	 *            id del elemento collegato
	 * @param sbnMarcType
	 *            albero castor
	 *
	 * @return ritorna un'istanza della classe DatiLegame, null se non trova
	 *         l'elemento.
	 */
	// public static DatiLegame getDatiLegameElemento(
	// String id,
	// SBNMarc sbnMarcType) {
	//
	// // Cambia la SIGNATURE del metodo: serve anche l'id del padre.
	// // Cambia la chiamata di 7 classi che lo richiamano.
	// // JFrameVariaTitolo e dettaglio di Titoli, Marche, Autori,
	// // Soggetti, Classi, Luoghi.
	//
	// // Metodo da rifare.
	// // Nel caso in cui un legame a un elemento è presente in più
	// // di una sola occasione, questo metodo ritorna il DatiLegame
	// // del primo elemento legato che trova in maniera sequenziale,
	// // ma il legame selezionato potrebbe non essere quello quindi
	// // il DatiLegame ritornato potrebbe essere quello errato.
	// System.out.println("-------> xmlcastutil peppe - id="+id);
	//
	// SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	// SbnResultType sbnResult = sbnResponse.getSbnResult();
	//
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
	// int totRighe = sbnOutPut.getTotRighe();
	//
	// DatiLegame datiLegame = null;
	//
	// ElementAutType elementoAut = null;
	// DocumentoType documentoType = null;
	// DatiElementoType datiElemento = null;
	// Esito esito = new Esito(false);
	//
	// // LEGGO SBNOUTPUT.DOCUMENTO O SBNOUTPUT.DOCUMENTO.DATITITACCESSO 1^
	// LIVELLO
	// if (sbnOutPut.getDocumentoCount() > 0) {
	// documentoType = sbnOutPut.getDocumento(0);
	//
	// //System.out.println("count:
	// "+documentoType.getLegamiDocumentoCount()+documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT001());
	// if (documentoType.getLegamiDocumentoCount() > 0) {
	// documentoType = sbnOutPut.getDocumento(0);
	//
	// datiLegame = getDatiLegameElemento(sbnMarcType, documentoType,
	// null, id, esito);
	// }
	// }
	//
	// // LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
	// if (datiElemento == null) {
	// if (sbnOutPut.getElementoAutCount() > 0) {
	// elementoAut = sbnOutPut.getElementoAut(0);
	// datiElemento = elementoAut.getDatiElementoAut();
	//
	// if (!datiElemento.getT001().equals(id)) {
	// datiLegame = getDatiLegameElemento(sbnMarcType, null,
	// elementoAut, id, esito);
	// }
	// }
	// }
	//
	// return datiLegame;
	// }
	/**
	 * Metodo utilizzato intermanente, usare getElementoAuthority(String id,
	 * SBNMarc sbnMarcType)
	 *
	 * @param sbnOutPut
	 *            DOCUMENT ME!
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 * @param documentoType
	 *            DOCUMENT ME!
	 * @param elemen
	 *            DOCUMENT ME!
	 * @param id
	 *            DOCUMENT ME!
	 * @param esito
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	// private static DatiLegame getDatiLegameElemento(SBNMarc sbnMarcType,
	// DocumentoType documentoType, ElementAutType elemen, String id,
	// Esito esito) {
	// DocumentoTypeChoice documentoTypeChoice = null;
	// DatiDocType datiDocType = null;
	// DatiElementoType datiElemento = null;
	//
	// DatiLegame datiLegame = null;
	//
	// if (documentoType != null) {
	// documentoTypeChoice = documentoType.getDocumentoTypeChoice();
	// datiDocType = documentoTypeChoice.getDatiDocumento();
	// }
	//
	// LegamiType legamiType = null;
	// ElementAutType elemen2 = null;
	// DocumentoType documentoType2 = null;
	//
	// int count = 0;
	//
	// // il count server per tutte due elementi di authority o titoli
	// if (elemen != null) {
	// count = elemen.getLegamiElementoAutCount();
	// } else {
	// count = documentoType.getLegamiDocumentoCount();
	// }
	//
	// int i = 0;
	//
	// // System.out.println("id a cercare: " + id + esito.getEsito() +
	// // " count: " + count);
	// while ((!esito.getEsito()) && (i < count)) {
	// if (elemen != null) {
	// legamiType = elemen.getLegamiElementoAut(i);
	// } else {
	// legamiType = documentoType.getLegamiDocumento(i);
	// }
	//
	// int j = 0;
	//
	// while ((!esito.getEsito()) &&
	// (j < legamiType.getArrivoLegameCount())) {
	// // LEGGO LEGAMEELEMENTOAUTORITY
	// ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
	// LegameElementoAutType legameElemento =
	// arrivoLegame.getLegameElementoAut();
	// LegameDocType legameDocType = arrivoLegame.getLegameDoc();
	// LegameTitAccessoType legameTitAccessoType =
	// arrivoLegame.getLegameTitAccesso();
	//
	// // SE E' UN LEGAME ELEMENTOAUT
	// if (legameElemento != null) {
	// elemen2 = legameElemento.getElementoAutLegato();
	//
	// datiLegame = new DatiLegame();
	//
	// // prendo la nota al legame
	// datiLegame.setNotaLegame(legameElemento.getNoteLegame());
	//
	// // prendo il tipo legame
	// datiLegame.setTipoLegame(
	// legameElemento.getTipoLegame().toString());
	//
	// // prendo il tipo respons
	// if ( legameElemento.getTipoRespons() != null ){
	// datiLegame.setResponsabilita(
	// legameElemento.getTipoRespons().toString());
	// }
	//
	// // prendo il relator code
	// datiLegame.setRelatorCode(legameElemento.getRelatorCode());
	//
	// // prendo l'incerto
	// if (legameElemento.getIncerto() != null) {
	// if (legameElemento.getIncerto().equals(SbnIndicatore.N)) {
	// datiLegame.setIncerto(false);
	// } else {
	// datiLegame.setIncerto(true);
	// }
	// }
	//
	// if ((elemen2 != null) &&
	// (elemen2.getLegamiElementoAutCount() > 0)) {
	// datiElemento = elemen2.getDatiElementoAut();
	//
	// String idCercato = datiElemento.getT001().trim();
	//
	// //System.out.println("id cercato: " + idCercato);
	// if (idCercato.equals(id)) {
	// esito.setEsito(true);
	// } else {
	// datiLegame = getDatiLegameElemento(sbnMarcType,
	// documentoType2, elemen2, id, esito);
	// }
	// } else {
	// if (elemen2 != null) {
	// // e' un tipo authority foglia del albero
	// datiElemento = elemen2.getDatiElementoAut();
	//
	// String idCercato = datiElemento.getT001().trim();
	//
	// // System.out.println("id cercato: " + idCercato);
	// if (idCercato.equals(id)) {
	// esito.setEsito(true);
	// }
	// }
	// }
	// } else
	// // SE E UN LEGAME DOC
	// if (legameDocType != null) {
	// documentoType2 = legameDocType.getDocumentoLegato();
	//
	// String idCercato = documentoType2.getDocumentoTypeChoice()
	// .getDatiDocumento().getT001()
	// .trim();
	//
	// datiLegame = new DatiLegame();
	//
	// // prendo la nota al legame
	// datiLegame.setNotaLegame(legameDocType.getNoteLegame());
	//
	// // prendo il tipo legame
	// datiLegame.setTipoLegame(legameDocType.getTipoLegame()
	// .toString());
	//
	// // prendo la sequenza
	// datiLegame.setSequenza(legameDocType.getSequenza());
	//
	// // prendo il sici
	// datiLegame.setSici(legameDocType.getSici());
	//
	// if (idCercato.equals(id)) {
	// esito.setEsito(true);
	// } else {
	// if (haLegami(documentoType2)) {
	// datiLegame = getDatiLegameElemento(sbnMarcType,
	// documentoType2, null, id, esito);
	// }
	// }
	// } else
	// // SE EUN LEGAMETITOLOACCESSO
	// if (legameTitAccessoType != null) {
	// documentoType2 = legameTitAccessoType.getTitAccessoLegato();
	//
	// String idCercato = documentoType2.getDocumentoTypeChoice()
	// .getDatiTitAccesso()
	// .getT001().trim();
	//
	// datiLegame = new DatiLegame();
	//
	// // prendo la nota al legame
	// datiLegame.setNotaLegame(legameTitAccessoType.getNoteLegame());
	//
	// //System.out.println("prendo il legame ti tit accesso:
	// "+legameTitAccessoType.getTipoLegame().toString());
	// // prendo il tipo legame
	// datiLegame.setTipoLegame(legameTitAccessoType.getTipoLegame()
	// .toString());
	//
	// // prendo la sequenza
	// datiLegame.setSequenzaMusica(legameTitAccessoType.getSequenzaMusica());
	//
	// // prendo il sottotipolegame
	// if (legameTitAccessoType.getSottoTipoLegame() != null) {
	// datiLegame.setSottoTipoLegame(legameTitAccessoType.getSottoTipoLegame()
	// .toString());
	// }
	//
	// if (idCercato.equals(id)) {
	// esito.setEsito(true);
	// } else {
	// if (haLegami(documentoType2)) {
	// datiLegame = getDatiLegameElemento(sbnMarcType,
	// documentoType2, null, id, esito);
	// }
	// }
	// }
	//
	// // INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
	// // STO CERCANDO
	// j++;
	// }
	//
	// // INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
	// // STO CERCANDO
	// i++;
	// }
	//
	// if (!esito.getEsito()) {
	// datiLegame = null;
	// }
	//
	// return datiLegame;
	// }
	// ////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////
	/**
	 * DOCUMENT ME!
	 *
	 * @param documentoType
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private boolean haLegami(DocumentoType documentoType) {
		return documentoType.getLegamiDocumentoCount() > 0;
	}

	/**
	 * Metodo usato internamente
	 *
	 * author Maurizio Alvino
	 *
	 * @param sbnMarcType
	 *            radice dell'albero
	 * @param elementoAut
	 *            elemento di authority
	 * @param id
	 *            bid del titolo uniforme
	 * @param esito
	 *            esito
	 *
	 * @return List di oggetti di tipo Repertorio
	 */
	// private static List getRepertoriTitoloAuthority(SBNMarc sbnMarcType,
	// ElementAutType elementoAut, String id, Esito esito) {
	// List vectorRepertori = new ArrayList();
	// DocumentoTypeChoice documentoTypeChoice = null;
	// DatiDocType datiDocType = null;
	// DatiElementoType datiElemento = null;
	//
	// for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
	// for (int j = 0;
	// j < elementoAut.getLegamiElementoAut(i)
	// .getArrivoLegameCount(); j++) {
	// if (elementoAut.getLegamiElementoAut(i).getArrivoLegame(j)
	// .getLegameElementoAut() != null) {
	// //Ho un legame con un tipo authority
	// //for (int k=0;
	// k<elementoAut.getLegamiElementoAut(i).getArrivoLegame(j).getLegameElementoAut().getElementoAutLegato().getLegamiElementoAutCount();
	// k++) {
	// //if
	// (elementoAut.getLegamiElementoAut(i).getArrivoLegame(j).getLegameElementoAut().getElementoAutLegato().getDatiElementoAut().getTipoAuthority()
	// == SbnAuthority.RE) {
	// if (elementoAut.getLegamiElementoAut(i).getArrivoLegame(j)
	// .getLegameElementoAut().getTipoAuthority() == SbnAuthority.RE) {
	// String tipoLegame = elementoAut.getLegamiElementoAut(i)
	// .getArrivoLegame(j)
	// .getLegameElementoAut()
	// .getTipoLegame()
	// .toString();
	//
	// if (tipoLegame == null) {
	// tipoLegame = "";
	// }
	//
	// String codiceRepertorio = elementoAut.getLegamiElementoAut(i)
	// .getArrivoLegame(j)
	// .getLegameElementoAut()
	// .getIdArrivo();
	//
	// if (codiceRepertorio == null) {
	// codiceRepertorio = "";
	// }
	//
	// String notaLegame = elementoAut.getLegamiElementoAut(i)
	// .getArrivoLegame(j)
	// .getLegameElementoAut()
	// .getNoteLegame();
	//
	// if (notaLegame == null) {
	// notaLegame = "";
	// }
	//
	// //Da verificare cosa sia di preciso l'esito!!!!
	// String esitoLegame = "";
	//
	// if (elementoAut.getLegamiElementoAut(i)
	// .getArrivoLegame(j)
	// .getLegameElementoAut()
	// .getTipoLegame().toString().equals(ResourceString.AUT_TIPO_LEGAME_14)) {
	// esitoLegame = "Si";
	// } else {
	// esitoLegame = "No";
	// }
	//
	// vectorRepertori.add(new Repertorio(esitoLegame,
	// codiceRepertorio, notaLegame));
	// }
	//
	// //}
	// }
	// }
	// }
	//
	// vectorRepertori.trimToSize();
	//
	// return vectorRepertori;
	// }
	/**
	 * Metodo usato internamente
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 * @param documentoType
	 *            DOCUMENT ME!
	 * @param elemen
	 *            DOCUMENT ME!
	 * @param id
	 *            DOCUMENT ME!
	 * @param esito
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private List getRepertoriElementoAuthority(SBNMarc sbnMarcType,
			DocumentoType documentoType, ElementAutType elemen, String id,
			Esito esito) {
		List vectorRepertori = null;
		DocumentoTypeChoice documentoTypeChoice = null;
		DatiDocType datiDocType = null;
		DatiElementoType datiElemento = null;

		if (documentoType != null) {
			documentoTypeChoice = documentoType.getDocumentoTypeChoice();
			datiDocType = documentoTypeChoice.getDatiDocumento();
		}

		LegamiType legamiType = null;
		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;

		int count = 0;

		// il count serve per tutte due, elementi di authority o titoli
		if (elemen != null) {
			count = elemen.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		int i = 0;

		while ((!esito.getEsito()) && (i < count)) {
			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			int j = 0;

			while ((!esito.getEsito())
					&& (j < legamiType.getArrivoLegameCount())) {
				// LEGGO LEGAMEELEMENTOAUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {
					elemen2 = legameElemento.getElementoAutLegato();

					if ((elemen2 != null)
							&& (elemen2.getLegamiElementoAutCount() > 0)) {
						datiElemento = elemen2.getDatiElementoAut();

						// prendo l'identificativo
						String idCercato = datiElemento.getT001().trim();

						// System.out.println("id cercato: " + idCercato);
						if (idCercato.equals(id)) {
							esito.setEsito(true);

							// //////////////////////////////////////////////////////////////////
							// prendo i repertori
							// //////////////////////////////////////////////
							// DA RIATTIVARE LARA
							// //////////////////////////////////////////////////////////////////
							vectorRepertori = getListRepertori(elemen2);

							// ////////////////////// fine repertori
							// ////////////////////////////
						} else {
							// continuo con la ricorsione
							vectorRepertori = getRepertoriElementoAuthority(
									sbnMarcType, documentoType2, elemen2, id,
									esito);
						}
					} else {
						if (elemen2 != null) {
							// e' un tipo authority foglia del albero
							datiElemento = elemen2.getDatiElementoAut();

							// prendo l'identificativo
							String idCercato = datiElemento.getT001().trim();

							// System.out.println("id cercato: " + idCercato);
							if (idCercato.equals(id)) {
								esito.setEsito(true);

								// //////////////////////////////////////////////////////////////////
								// prendo i repertori
								// //////////////////////////////////////////////
								// //////////////////////////////////////////////////////////////////
								vectorRepertori = getListRepertori(elemen2);

								// ////////////////// fine repertori
								// ////////////////////////////////
							}
						}
					}
				} else
				// SE E UN LEGAME DOC
				if (legameDocType != null) {
					documentoType2 = legameDocType.getDocumentoLegato();

					// se il documento ha dei legami continuo con la ricorsione
					if (haLegami(documentoType2)) {

						vectorRepertori = getRepertoriElementoAuthority(
								sbnMarcType, documentoType2, null, id, esito);
					}
				} else
				// SE EUN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {
					documentoType2 = legameTitAccessoType.getTitAccessoLegato();

					// se il documento ha dei legami continuo con la ricorsione
					if (haLegami(documentoType2)) {
						vectorRepertori = getRepertoriElementoAuthority(
								sbnMarcType, documentoType2, null, id, esito);
					}
				}

				// INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
				// STO CERCANDO
				j++;
			}

			// INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
			// STO CERCANDO
			i++;
		}

		if (!esito.getEsito()) {
			vectorRepertori = null;
		}

		return vectorRepertori;
	}

	/**
	 * Metodo usato internamente
	 *
	 * @param elementoAut
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */

	public List getListRepertori(ElementAutType elementoAut) {
		List ritorno = null;
		SbnAuthority tipoAuthority = elementoAut.getDatiElementoAut()
				.getTipoAuthority();

		// //////////////////////////////////////////////////////////////////////
		// /////////////////////// RICERCA INFO IN LEGAMI
		// ///////////////////////
		// //////////////////////////////////////////////////////////////////////
		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			// if (legamiType.getIdPartenza().equals(vid)) {
			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();

				if (arrivoLegame.getLegameElementoAut() != null) {
					// prendo il tipo legame
					String tipoLegameString = legameElemento.getTipoLegame()
							.toString();
					// prendo il tipo authority del legame
					SbnAuthority authorityLegame = legameElemento
							.getTipoAuthority();
					// REPERTORI LEGATI ALL'ELEMENTO
					if (authorityLegame.getType() == SbnAuthority.RE_TYPE) {
						// creo il vector la prima volta
						if (ritorno == null) {
							ritorno = new ArrayList();
						}
						// prendo il CODICE DEL REPERTORIO, viene come idArrivo
						String idArrivo = legameElemento.getIdArrivo();
						// AGGIUNGO IL REPERTORIO A UNA LISTA DEI REPERTORI
						String notaLegameRepertorio = legameElemento
								.getNoteLegame();
						// if (notaLegameRepertorio == null) {
						// notaLegameRepertorio = ResourceString.IID_SPAZIO;
						// }
						// CONVERTO IL CODICE DEL TIPO LEGAME A LA STRINGA ESITO
						// POSITIVO O NEGATIVO
						if (tipoLegameString.equals("810")) {
							tipoLegameString = "Si";
						} else {
							tipoLegameString = "No";
						}

						// /////////////////////////////////////////////////////////////////////////
						// //////////////// PER OGNI AREA
						// /////////////////////////////////////////////////////////////////////////
						// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
						// di gestire i campi nota informativa , nota catalogatore e legame a repertori
						if ((tipoAuthority.getType() == SbnAuthority.AU_TYPE)
								|| (tipoAuthority.getType() == SbnAuthority.TU_TYPE)
								|| (tipoAuthority.getType() == SbnAuthority.UM_TYPE)
								|| (tipoAuthority.getType() == SbnAuthority.LU_TYPE)
								) {
							// per gli autori
							ritorno.add(new Repertorio(tipoLegameString,
									idArrivo, notaLegameRepertorio));
						} else if (tipoAuthority.getType() == SbnAuthority.MA_TYPE) {
							// per le marche
							// CITAZIONE
							int citazione = legameElemento.getCitazione();
							ritorno.add(new Repertorio(tipoLegameString,
									idArrivo, citazione, notaLegameRepertorio));
						}
					}
				}
			}
		}
		return ritorno;
	}

	/**
	 * Prende i repertori di un elemento di authority
	 *
	 * @param id
	 *            identificativo del elemento
	 * @param sbnMarcType
	 *            albero castor
	 *
	 * @return vector con i repertori, ogni elemento di questo vector è una
	 *         instanza della classe Repertorio, null se non trova i repertori
	 */
	// public static List getRepertoriTitoloAuthority(String id,
	// SBNMarc sbnMarcType) {
	// List vectorRepertori = null;
	// SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	// SbnResultType sbnResult = sbnResponse.getSbnResult();
	//
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
	// int totRighe = sbnOutPut.getTotRighe();
	//
	// ElementAutType elementoAut = null;
	// DatiElementoType datiElemento = null;
	// Esito esito = new Esito(false);
	//
	// if (sbnOutPut.getElementoAutCount() > 0) {
	// elementoAut = sbnOutPut.getElementoAut(0);
	//
	// if (elementoAut.getLegamiElementoAutCount() > 0) {
	// vectorRepertori = getRepertoriTitoloAuthority(sbnMarcType,
	// elementoAut, id, esito);
	// }
	// }
	//
	// // LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
	// if (vectorRepertori == null) {
	// if (sbnOutPut.getElementoAutCount() > 0) {
	// elementoAut = sbnOutPut.getElementoAut(0);
	// datiElemento = elementoAut.getDatiElementoAut();
	//
	// if (!datiElemento.getT001().trim().equals(id)) {
	// vectorRepertori = getRepertoriTitoloAuthority(sbnMarcType,
	// elementoAut, id, esito);
	// } else {
	// // cerco i repertori nel primo elemento cioè l'elemento radice
	// vectorRepertori = getListRepertori(elementoAut);
	// }
	// }
	// }
	//
	// return vectorRepertori;
	// }
	// VECCHIA DI MANUEL
	public List getRepertoriElementoAuthority(String id, SBNMarc sbnMarcType) {
		List vectorRepertori = null;
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;
		DatiElementoType datiElemento = null;
		Esito esito = new Esito(false);

		// LEGGO SBNOUTPUT.DOCUMENTO O SBNOUTPUT.DOCUMENTO.DATITITACCESSO 1^
		// LIVELLO
		if (sbnOutPut.getDocumentoCount() > 0) {
			documentoType = sbnOutPut.getDocumento(0);

			if (documentoType.getLegamiDocumentoCount() > 0) {
				documentoType = sbnOutPut.getDocumento(0);

				vectorRepertori = getRepertoriElementoAuthority(sbnMarcType,
						documentoType, null, id, esito);
			}
		}

		// LEGGO SBNOUTPUT.ELEMENTOAUT PRIMO LIVELLO
		if (vectorRepertori == null) {
			if (sbnOutPut.getElementoAutCount() > 0) {
				elementoAut = sbnOutPut.getElementoAut(0);
				datiElemento = elementoAut.getDatiElementoAut();

				if (!datiElemento.getT001().trim().equals(id)) {
					vectorRepertori = getRepertoriElementoAuthority(
							sbnMarcType, null, elementoAut, id, esito);
				} else {
					// cerco i repertori nel primo elemento cioè l'elemento
					// radice
					vectorRepertori = getListRepertori(elementoAut);
				}
			}
		}

		return vectorRepertori;
	}


	/**
	 * getPersonaggiDocumentoMusicale
	 *
	 * Restituisce un vector di personaggi relativi ad un titolo di tipo
	 * MusicaType
	 *
	 * @param id
	 * @param sbnMarcType
	 * @return vector di personaggi
	 */
	// public static List getPersonaggiDocumentoMusicale(String id, SBNMarc
	// sbnMarcType) {
	// List vecRisultato = null;
	// SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	//
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
	//
	// DatiDocType datiDocType = null;
	// MusicaType musicaType = null;
	//
	// for (int i=0; i<sbnOutPut.getDocumentoCount(); i++) {
	// datiDocType =
	// sbnOutPut.getDocumento(i).getDocumentoTypeChoice().getDatiDocumento();
	// if (datiDocType instanceof MusicaType) {
	// if (datiDocType.getT001().equals(id)) {
	// musicaType = (MusicaType)datiDocType;
	// if (vecRisultato == null){
	// vecRisultato = new ArrayList();
	// }
	// for (int j=0; j<musicaType.getT927Count(); j++) {
	// Personaggio personaggio = new Personaggio();
	// personaggio.setPersonaggio(musicaType.getT927(j).getA_927());
	// personaggio.setTimbro(musicaType.getT927(j).getB_927());
	// personaggio.setInterprete(musicaType.getT927(j).getC3_927());
	// vecRisultato.add(personaggio);
	// }
	// }
	// }
	// }
	// return vecRisultato;
	// }
	/**
	 * Prende il DatiElementoType di un elemento di authority
	 *
	 * @param id
	 *            identificativo dell'elemento
	 * @param sbnMarcType
	 *            albero castor generico (per tutte le aree)
	 *
	 * @return ritorna il DatiElementoType dell'elemento, null se non esiste
	 */
	public DatiElementoType getElementoAuthority(String id, SBNMarc sbnMarcType) {

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;
		DatiElementoType datiElemento = null;
		Esito esito = new Esito(false);

		// System.out.println("--- getElementoAuthority ---");
		// System.out.println("id selezionato: "+id);

		// Nr. di Documenti di SbnOutput - 1° Livello
		if (sbnOutPut.getDocumentoCount() > 0) {
			documentoType = sbnOutPut.getDocumento(0);

			// Se il Documento ha legami, utilizza il metodo
			// omonimo interno per cercare l'id.
			if (documentoType.getLegamiDocumentoCount() > 0) {
				documentoType = sbnOutPut.getDocumento(0);
				datiElemento = getElementoAuthority(sbnMarcType, documentoType,
						null, id, esito);
			}
		}

		// Nr. di ElementAut di SbnOutput - 1° Livello
		if (datiElemento == null) {
			for (int i = 0; i < sbnOutPut.getElementoAutCount(); i++) {
				elementoAut = sbnOutPut.getElementoAut(i);
				datiElemento = elementoAut.getDatiElementoAut();

				// Se l'id trovato non è uguale a quello cercato,
				// utilizza il metodo omonimo interno per cercarlo
				// all'interno degli eventuali legami dell'ElementoAut.
				if (!datiElemento.getT001().equals(id)) {
					datiElemento = getElementoAuthority(sbnMarcType, null,
							elementoAut, id, esito);
				} else {
					// TROVATO - Esce dal ciclo
					break;
				}
			}
		}

		return datiElemento;
	}

	/**
	 * Metodo utilizzato intermanente, usare getElementoAuthority(String id,
	 * SBNMarc sbnMarcType)
	 *
	 * @param sbnOutPut
	 *            DOCUMENT ME!
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 * @param documentoType
	 *            DOCUMENT ME!
	 * @param elemen
	 *            DOCUMENT ME!
	 * @param id
	 *            DOCUMENT ME!
	 * @param esito
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DatiElementoType getElementoAuthority(SBNMarc sbnMarcType,
			DocumentoType documentoType, ElementAutType elemen, String id,
			Esito esito) {

		DocumentoTypeChoice documentoTypeChoice = null;
		DatiDocType datiDocType = null;
		DatiElementoType datiElemento = null;

		if (documentoType != null) {
			documentoTypeChoice = documentoType.getDocumentoTypeChoice();
			datiDocType = documentoTypeChoice.getDatiDocumento();
		}

		LegamiType legamiType = null;
		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;

		int count = 0;

		// il count server per tutte due elementi di authority o titoli
		if (elemen != null) {
			count = elemen.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		int i = 0;

		// System.out.println("id a cercare: " + id + esito.getEsito() +
		// " count: " + count);
		while ((!esito.getEsito()) && (i < count)) {
			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			int j = 0;

			while ((!esito.getEsito())
					&& (j < legamiType.getArrivoLegameCount())) {
				// LEGGO LEGAMEELEMENTOAUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {
					elemen2 = legameElemento.getElementoAutLegato();

					if ((elemen2 != null)
							&& (elemen2.getLegamiElementoAutCount() > 0)) {
						datiElemento = elemen2.getDatiElementoAut();

						String idCercato = datiElemento.getT001().trim();

						// System.out.println("id cercato: " + idCercato);
						if (idCercato.equals(id)) {
							esito.setEsito(true);
						} else {
							datiElemento = getElementoAuthority(sbnMarcType,
									documentoType2, elemen2, id, esito);
						}
					} else {
						if (elemen2 != null) {
							// e' un tipo authority foglia del albero
							datiElemento = elemen2.getDatiElementoAut();

							String idCercato = datiElemento.getT001().trim();

							// System.out.println("id cercato: " + idCercato);
							if (idCercato.equals(id)) {
								esito.setEsito(true);
							}
						}
					}
				} else
				// SE E UN LEGAME DOC
				if (legameDocType != null) {
					documentoType2 = legameDocType.getDocumentoLegato();

					if (haLegami(documentoType2)) {
						datiElemento = getElementoAuthority(sbnMarcType,
								documentoType2, null, id, esito);
					}
				} else
				// SE EUN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {
					documentoType2 = legameTitAccessoType.getTitAccessoLegato();

					if (haLegami(documentoType2)) {
						datiElemento = getElementoAuthority(sbnMarcType,
								documentoType2, null, id, esito);
					}
				}

				// INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
				// STO CERCANDO
				j++;
			}

			// INCREMENTO L'INDICE FIN CHE NON TROVO L'ELEMENTO CHE
			// STO CERCANDO
			i++;
		}

		if (!esito.getEsito()) {
			datiElemento = null;
		}

		return datiElemento;
	}

	/**
	 * Restituisce il livello di autorità di un tipo authority
	 *
	 * @param titolo
	 *            Un titolo può essere un documento, un titolo di accesso o un
	 *            tipo authority
	 *
	 * @return livello di autorità del titolo author Maurizio Alvino
	 */
	public SbnLivello getLivelloAutoritaTitolo(Object titolo) {
		DocumentoType documentoType = null;
		ElementAutType elementAutType = null;

		if (titolo instanceof DocumentoType) {
			// E' un documento o un titolo di accesso
			documentoType = (DocumentoType) titolo;

			if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
				return documentoType.getDocumentoTypeChoice()
						.getDatiDocumento().getLivelloAutDoc();
			} else {
				if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
					return documentoType.getDocumentoTypeChoice()
							.getDatiTitAccesso().getLivelloAut();
				}
			}
		} else {
			// E' un tipo authority
			elementAutType = (ElementAutType) titolo;

			if (elementAutType.getDatiElementoAut() != null) {
				return elementAutType.getDatiElementoAut().getLivelloAut();
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param documento
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getDataVariazioneTitolo(Object titolo) {
		DocumentoType documento = null;
		ElementAutType elementoAut = null;

		if (titolo instanceof DocumentoType) {
			documento = (DocumentoType) titolo;

			if (documento.getDocumentoTypeChoice().getDatiDocumento() != null) {
				return documento.getDocumentoTypeChoice().getDatiDocumento()
						.getT005();
			} else if (documento.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
				return documento.getDocumentoTypeChoice().getDatiTitAccesso()
						.getT005();
			}
		} else {
			if (titolo instanceof ElementAutType) {
				elementoAut = (ElementAutType) titolo;

				if (elementoAut.getDatiElementoAut() != null) {
					return elementoAut.getDatiElementoAut().getT005();
				}
			}
		}

		return null;
	}

	/**
	 * Prende la radice dell'albero di un titolo e restituisce un Object di tipo
	 * DocumentoType o ElementoAutType
	 *
	 * @param sbnMarcType
	 *            albero castor
	 *
	 * @return ritorna il DocumentoType, null se non esiste
	 *
	 * author Maurizio Alvino
	 */
	public Object getElementoTitolo(SBNMarc sbnMarcType) {
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;

		if (sbnOutPut.getDocumentoCount() > 0) {
			// E' un documento o un titolo di accesso
			documentoType = sbnOutPut.getDocumento(0);

			return documentoType;
		} else {
			// E' un tipo authority
			elementoAut = sbnOutPut.getElementoAut(0);

			return elementoAut;
		}
	}

	// public static Object getObjectElemento(String ID, SBNMarc sbnMarc) {
	// Object obj = null;
	// DatiElementoType ritorno = null;
	// SbnMessageType sbnMessage = sbnMarc.getSbnMessage();
	// SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
	// SbnResultType sbnResult = sbnResponse.getSbnResult();
	// String esito = sbnResult.getEsito();
	// String testoEsito = sbnResult.getTestoEsito();
	// int count = 0;
	//
	// SbnResponseTypeChoice sbnResponseChoice =
	// sbnResponse.getSbnResponseTypeChoice();
	// SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
	// ElementAutType elementoAut = null;
	// DatiElementoType datiElemento = null;
	// DocumentoType documentoType = null;
	// DocumentoTypeChoice documentoTypeChoice = null;
	// DatiDocType datiDocType = null;
	// LegamiType legamiType = null;
	//
	// if ( sbnOutPut.getElementoAutCount() > 0 ){
	// for (int i=0; i<sbnOutPut.getElementoAutCount(); i++){
	// if(sbnOutPut.getElementoAut(i).getDatiElementoAut().getT001().equals(ID)){
	// obj = sbnOutPut.getElementoAut(i).getDatiElementoAut();
	// break;
	// } else if (sbnOutPut.getElementoAut(i).getLegamiElementoAutCount()>0){
	//
	// }
	// }
	// }
	//
	// if (sbnOutPut.getDocumentoCount() > 0){
	// for (int i=0; i<sbnOutPut.getDocumentoCount(); i++){
	//
	// if (XMLTitoli.getDatiDocTypeDocumento(sbnOutPut.getDocumento(i), ID) ==
	// null){
	// if (XMLTitoli.getTitAccessoTypeDocumento(sbnOutPut.getDocumento(i), ID)
	// != null){
	// obj = XMLTitoli.getTitAccessoTypeDocumento(
	// sbnOutPut.getDocumento(i), ID);
	// break;
	// }
	// } else {
	// obj = XMLTitoli.getDatiDocTypeDocumento(
	// sbnOutPut.getDocumento(i), ID);
	// break;
	// }
	// }
	// }
	//
	// return obj;
	// }

	/**
	 * Prende il DocumentoType di un elemento di tipo Documento (Titolo)
	 *
	 * @param id
	 *            indentificativo dell'elemento
	 * @param sbnMarcType
	 *            albero castor
	 *
	 * @return ritorna il DocumentoType, null se non esiste
	 */
	public DocumentoType getElementoDocumento(String id, SBNMarc sbnMarcType) {

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();

		ElementAutType elementoAut = null;
		DocumentoType documentoType = null;
		DatiElementoType datiElemento = null;
		Esito esito = new Esito(false);

		if (sbnOutPut.getDocumentoCount() > 0) {

			// Ciclo sul numero dei Documenti di SbnOutput
			for (int i = 0; i < sbnOutPut.getDocumentoCount(); i++) {

				// Se l'elemento non è stato ancora trovato,
				// continua nella ricerca.
				if (!esito.getEsito()) {
					documentoType = sbnOutPut.getDocumento(i);

					if (documentoType.getDocumentoTypeChoice()
							.getDatiDocumento() != null) {

						// DatiDocumento
						String idCercato = documentoType
								.getDocumentoTypeChoice().getDatiDocumento()
								.getT001().trim();
						if (idCercato.equals(id)) {
							esito.setEsito(true);
						}
					} else {
						// DatiTitAccesso
						String idCercato = documentoType
								.getDocumentoTypeChoice().getDatiTitAccesso()
								.getT001().trim();
						if (idCercato.equals(id)) {
							esito.setEsito(true);
						}
					}

					// Se la ricerca non ha avuto buon esito,
					// cerca nei suoi eventuali legami.
					if (!esito.getEsito()) {
						// LEGAMI
						documentoType = getElementoDocumento(sbnMarcType,
								documentoType, null, id, esito);
					} else {
						break;
					}

				}

			}// end for

		}

		// AGGIUNTO
		// Nr. di Elementi Authority di SbnOutput - 1° Livello
		if (sbnOutPut.getElementoAutCount() > 0) {
			elementoAut = sbnOutPut.getElementoAut(0);
			documentoType = getElementoDocumento(sbnMarcType, null, elementoAut, id, esito);
		}

		// Se l'esito della ricerca è false, porta
		// a null il DocumentoType da restituire.
		if (!esito.getEsito()) {
			documentoType = null;
		}

		return documentoType;

	}// end getElementoDocumento

	/**
	 * Metodo utilizzato intermanente, usare getElementoDocumento(String id,
	 * SBNMarc sbnMarcType)
	 *
	 * @param sbnMarcType
	 *            DOCUMENT ME!
	 * @param documentoType
	 *            DOCUMENT ME!
	 * @param elemen
	 *            DOCUMENT ME!
	 * @param id
	 *            DOCUMENT ME!
	 * @param esito
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DocumentoType getElementoDocumento(SBNMarc sbnMarcType,
			DocumentoType documentoType, ElementAutType elemen, String id,
			Esito esito) {

		DocumentoTypeChoice documentoTypeChoice = null;
		DatiElementoType datiElemento = null;

		if (documentoType != null) {
			documentoTypeChoice = documentoType.getDocumentoTypeChoice();
		}

		LegamiType legamiType = null;
		ElementAutType elemen2 = null;
		DocumentoType documentoType2 = null;

		int count = 0;

		// il count server per tutte due elementi di authority o titoli
		if (elemen != null) {
			count = elemen.getLegamiElementoAutCount();
		} else {
			count = documentoType.getLegamiDocumentoCount();
		}

		int i = 0;

		while ((!esito.getEsito()) && (i < count)) {
			if (elemen != null) {
				legamiType = elemen.getLegamiElementoAut(i);
			} else {
				legamiType = documentoType.getLegamiDocumento(i);
			}

			int j = 0;

			while ((!esito.getEsito())
					&& (j < legamiType.getArrivoLegameCount())) {
				// LEGGO LEGAMEELEMENTOAUTORITY
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				LegameDocType legameDocType = arrivoLegame.getLegameDoc();
				LegameTitAccessoType legameTitAccessoType = arrivoLegame
						.getLegameTitAccesso();

				// SE E' UN LEGAME ELEMENTOAUT
				if (legameElemento != null) {
					elemen2 = legameElemento.getElementoAutLegato();

					if ((elemen2 != null) && (elemen2.getLegamiElementoAutCount() > 0)) {
						datiElemento = elemen2.getDatiElementoAut();
						documentoType2 = getElementoDocumento(sbnMarcType, documentoType2, elemen2, id, esito);
//						getElementoDocumento(sbnMarcType, documentoType2, elemen2, id, esito);
					}
				} else
				// SE E UN LEGAME DOC
				if (legameDocType != null) {
					documentoType2 = legameDocType.getDocumentoLegato();
					String idCercato = documentoType2.getDocumentoTypeChoice().getDatiDocumento().getT001().trim();
					if (idCercato.equals(id)) {
						esito.setEsito(true);
					} else if (haLegami(documentoType2)) {
						{
							documentoType2 = getElementoDocumento(sbnMarcType, documentoType2, null, id, esito);
						}
					} else {
						if (idCercato.equals(id)) {
							esito.setEsito(true);
						}
					}
				} else
				// SE EUN LEGAMETITOLOACCESSO
				if (legameTitAccessoType != null) {
					documentoType2 = legameTitAccessoType.getTitAccessoLegato();
					String idCercato = documentoType2.getDocumentoTypeChoice().getDatiTitAccesso().getT001().trim();

					if (idCercato.equals(id)) {
						esito.setEsito(true);
					} else if (haLegami(documentoType2)) {
						documentoType2 = getElementoDocumento(sbnMarcType, documentoType2, null, id, esito);

					} else {
						if (idCercato.equals(id)) {
							esito.setEsito(true);
						}
					}
				}

				// INCREMENTO L'INDICE FINCHE' NON
				// TROVO L'ELEMENTO CHE STO CERCANDO
				j++;
			}

			// INCREMENTO L'INDICE FINCHE' NON
			// TROVO L'ELEMENTO CHE STO CERCANDO
			i++;
		}

		if (!esito.getEsito()) {
			documentoType2 = null;
		}

		return documentoType2;
	}

	static class Esito {
		private boolean esito;

		Esito(boolean esito) {
			this.esito = esito;
		}

		public void setEsito(boolean esito) {
			this.esito = esito;
		}

		public boolean getEsito() {
			return esito;
		}
	}

	/**
	 * Creazione di un oggetto di tipo SbnRangeDate, entrambe le date debbono
	 * essere valorizzate altrimenti viene ritornato null
	 *
	 * @param dataDa
	 *            data di inizio del range in formato yyyy-mm-gg
	 * @param dataA
	 *            data di fine del range in formato yyyy-mm-gg
	 * @return un oggetto di tipo SbnRangeDate o null se non sono fornite
	 *         entrambe le date
	 */
	// public static SbnRangeDate makeSbnRangeDate(String dataDa, String dataA,
	// int tipoFiltro){
	// SbnRangeDate sbnRangeDate = null;
	// try {
	// if ( ( dataDa != null ) && ( dataA != null ) ){
	// if ((!dataDa.equals(IIDTextFieldData.DATA_VUOTA)) &&
	// (!dataA.equals(IIDTextFieldData.DATA_VUOTA))) {
	// sbnRangeDate = new SbnRangeDate();
	// // date castor
	// org.exolab.castor.types.Date dtDa =
	// new org.exolab.castor.types.Date(dataDa);
	// org.exolab.castor.types.Date dtA =
	// new org.exolab.castor.types.Date(dataA);
	// sbnRangeDate.setDataA(dtA);
	// sbnRangeDate.setDataDa(dtDa);
	// // Inserimento:0 la variazione(1) è di default
	// if ( tipoFiltro == 0 ) sbnRangeDate.setTipoFiltroDate(tipoFiltro);
	// }
	// }
	// } catch (ParseException e) {
	// }
	// return sbnRangeDate;
	// }
	//
	/**
	 * di default il tipo filtro data è "data modifica"
	 *
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	// public static SbnRangeDate makeSbnRangeDate(String dataDa, String dataA){
	// return makeSbnRangeDate(dataDa, dataA, 1);
	// }
	/**
	 * Dato il BID e l'albero Castor, restituisce la descrizione del Titolo
	 * selezionato se si tratta di un Documento (TitAccessoType o DatiDocType).
	 * Restituisce null se è un Tipo Authority.
	 *
	 * @param BID
	 * @param sbnMarc
	 * @return
	 */
	// public static String getDescrizioneTitolo(String BID, SBNMarc sbnMarc) {
	// DatiDocType datiDocType = null;
	// TitAccessoType titAccessoType = null;
	// String descrizione = null;
	// DocumentoType documentoType = UtilityCastor.getElementoDocumento(BID,
	// sbnMarc);
	//
	// if (documentoType != null) {
	// // TITOLO DI TIPO DOCUMENTO (DOCTYPE O TITACCESSOTYPE)
	// datiDocType = XMLTitoli.getDatiDocTypeDocumento(documentoType, BID);
	// titAccessoType = XMLTitoli.getTitAccessoTypeDocumento(documentoType,
	// BID);
	//
	// if (datiDocType != null) {
	//
	// // DOCUMENTO
	//
	// String [] arrayDesc = null;
	// if (datiDocType.getT200() != null){
	// arrayDesc = datiDocType.getT200().getA_200();
	// }
	// if (arrayDesc!=null){
	// // Precedentemente la descrizione era a null
	// descrizione = ResourceString.IID_STRINGAVUOTA;
	// for (int i=0; i<arrayDesc.length; i++){
	// descrizione = descrizione + arrayDesc[i];
	// }
	// }
	//
	// }// end caso DatiDocType
	//
	// else if (titAccessoType != null){
	//
	// // TITOLO ACCESSO
	//
	// String natura = titAccessoType.getNaturaTitAccesso().toString();
	// String [] arrayDesc = null;
	//
	// // Prende l'AREA DEL TITOLO a seconda della natura
	// if (natura.equals(ResourceString.TIT_NATURA_B)) {
	// arrayDesc = titAccessoType
	// .getTitAccessoTypeChoice().getT454().getA_200();
	// }
	// if (natura.equals(ResourceString.TIT_NATURA_D)) {
	// arrayDesc = titAccessoType
	// .getTitAccessoTypeChoice().getT517().getA_200();
	// }
	// if (natura.equals(ResourceString.TIT_NATURA_P)) {
	// arrayDesc = titAccessoType
	// .getTitAccessoTypeChoice().getT510().getA_200();
	// }
	// if (natura.equals(ResourceString.TIT_NATURA_T)) {
	// arrayDesc = titAccessoType
	// .getTitAccessoTypeChoice().getT423().getT200().getA_200();
	// }
	//
	// if (arrayDesc!=null){
	// // Precedentemente la descrizione era a null
	// descrizione = ResourceString.IID_STRINGAVUOTA;
	// for (int i=0; i<arrayDesc.length; i++){
	// descrizione = descrizione + arrayDesc[i];
	// }
	// }
	//
	// }// end caso TitAccessoType
	// }
	//
	// return descrizione;
	// }
	/**
	 * Restituisce i dati di legame dell'elemento selezionato, utilizzando i
	 * tipo di legame dello stesso (se legame ad un elemento authority, a un
	 * documento o a un titolo accesso).
	 *
	 * @param legameElemAut
	 * @param legameDoc
	 * @param legameTitAccesso
	 * @return dati di legame
	 */
	public DatiLegame getDatiLegameNodo(LegameElementoAutType legameElemAut,
			LegameDocType legameDoc, LegameTitAccessoType legameTitAccesso) {

		DatiLegame datiLegame = null;

		if (legameElemAut != null) {
			// LEGAME A ELEMENTO AUTHORITY
			datiLegame = new DatiLegame();

			boolean incerto = false;
			boolean superfluo = false;
			if (legameElemAut.getIncerto() != null) {
				if (legameElemAut.getIncerto().getType() == SbnIndicatore.S_TYPE) {
					incerto = true;
				} else {
					incerto = false;
				}
				datiLegame.setIncerto(incerto);
			}
			if (legameElemAut.getSuperfluo() != null) {
				if (legameElemAut.getSuperfluo().getType() == SbnIndicatore.S_TYPE) {
					superfluo = true;
				} else {
					superfluo = false;
				}
				datiLegame.setSuperfluo(superfluo);
			}

			datiLegame.setNotaLegame(legameElemAut.getNoteLegame());
			datiLegame.setRelatorCode(legameElemAut.getRelatorCode());
			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
			// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
			//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
			datiLegame.setSpecStrumVoci(legameElemAut.getStrumento());

			if (legameElemAut.getTipoLegame() != null) {
				datiLegame.setTipoLegame(legameElemAut.getTipoLegame()
						.toString());
			}
			if (legameElemAut.getTipoRespons() != null) {
				datiLegame.setResponsabilita(legameElemAut.getTipoRespons()
						.toString());
			}

			if (legameElemAut.getCondiviso() == null){
				datiLegame.setFlagCondiviso(true);
			} else {
				if (legameElemAut.getCondiviso().getType() == LegameElementoAutTypeCondivisoType.N_TYPE){
					datiLegame.setFlagCondiviso(false);
				} else {
					datiLegame.setFlagCondiviso(true);
				}

			}
		} else if (legameDoc != null) {
			// LEGAME A DOCUMENTO
			datiLegame = new DatiLegame();
			datiLegame.setNotaLegame(legameDoc.getNoteLegame());
			datiLegame.setSequenza(legameDoc.getSequenza());
			datiLegame.setSici(legameDoc.getSici());
			if (legameDoc.getTipoLegame() != null) {
				datiLegame.setTipoLegame(legameDoc.getTipoLegame().toString());
			}
			if (legameDoc.getCondiviso() == null){
				datiLegame.setFlagCondiviso(true);
			} else {
				if (legameDoc.getCondiviso().getType() == LegameDocTypeCondivisoType.N_TYPE){
					datiLegame.setFlagCondiviso(false);
				} else {
					datiLegame.setFlagCondiviso(true);
				}
			}

		} else if (legameTitAccesso != null) {
			// LEGAME A TITOLO ACCESSO
			datiLegame = new DatiLegame();
			datiLegame.setNotaLegame(legameTitAccesso.getNoteLegame());
			datiLegame.setSequenzaMusica(legameTitAccesso.getSequenzaMusica());
			if (legameTitAccesso.getSottoTipoLegame() != null) {
				datiLegame.setSottoTipoLegame(legameTitAccesso
						.getSottoTipoLegame().toString());
			}
			if (legameTitAccesso.getTipoLegame() != null) {
				datiLegame.setTipoLegame(legameTitAccesso.getTipoLegame()
						.toString());
			}
			if (legameTitAccesso.getCondiviso() == null){
				datiLegame.setFlagCondiviso(true);
			} else {
				if (legameTitAccesso.getCondiviso().getType() == LegameTitAccessoTypeCondivisoType.N_TYPE){
					datiLegame.setFlagCondiviso(false);
				} else {
					datiLegame.setFlagCondiviso(true);
				}
			}
		}

		return datiLegame;
	}


	public boolean getInfoLocalizzazioni(String codiceBib, String[] infoLocBib) {

		for (int i = 0; i < infoLocBib.length; i++) {
			if (infoLocBib[i].equals(codiceBib) ) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}


}
