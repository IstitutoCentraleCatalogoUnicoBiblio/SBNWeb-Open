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
import it.iccu.sbn.ejb.model.unimarcmodel.CercaPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DestinatarioProposteDiCorrezione;
import it.iccu.sbn.ejb.vo.gestionebibliografica.SinteticaProposteDiCorrezioneView;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
public class SbnGestioneProposteDiCorrezioneDao {

	private FactorySbn indice;

	private FactorySbn polo;

	private SbnUserType user;

	public SbnGestioneProposteDiCorrezioneDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

	public static String IID_SPAZIO = " ";

	public static final String IID_STRINGAVUOTA = "";

	private Comparator myComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((String) o1).compareTo((String) o2);
		}
	};

	UtilityCastor utilityCastor = new UtilityCastor();


	public AreaDatiPropostaDiCorrezioneVO cercaPropostaDiCorrezione(
			AreaDatiPropostaDiCorrezioneVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo(null);

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
//			cercaType.setTipoOrd(SbnTipoOrd.VALUE_4);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			CercaPropostaType cercaPropostaType = new CercaPropostaType();
			cercaType.setMaxRighe(CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, 4000));


			if (areaDatiPass.getIdProposta() > 0) {
				cercaPropostaType.setIdProposta(String.valueOf(areaDatiPass.getIdProposta()));
			}
			if (areaDatiPass.getIdOggettoProposta() != null && !areaDatiPass.getIdOggettoProposta().equals("")) {
				SbnOggetto[] sbnOggettiLista = new SbnOggetto[1];
				SbnOggetto sbnOggetto = new SbnOggetto();
				cercaPropostaType.setIdOggetto(areaDatiPass.getIdOggettoProposta());

				if (areaDatiPass.getTipoAuthorityProposta() != null && !areaDatiPass.getTipoAuthorityProposta().equals("")) {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getTipoAuthorityProposta()));
				} else if (areaDatiPass.getTipoMaterialeProposta() != null && !areaDatiPass.getTipoMaterialeProposta().equals("")) {
					sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getTipoMaterialeProposta()));
				} else {
					if (areaDatiPass.getIdOggettoProposta().substring(3,4).equals("V")) {
						sbnOggetto.setTipoAuthority(SbnAuthority.AU);
					} else if (areaDatiPass.getIdOggettoProposta().substring(3,4).equals("M")) {
						sbnOggetto.setTipoAuthority(SbnAuthority.MA);
					}  else if (areaDatiPass.getIdOggettoProposta().substring(3,4).equals("L")) {
						sbnOggetto.setTipoAuthority(SbnAuthority.LU);
					} else {
						// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
						// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_0);
						sbnOggetto.setTipoMateriale(SbnMateriale.valueOf("M"));
					}
				}
				sbnOggettiLista[0] = sbnOggetto;
				cercaPropostaType.setTipoOggetto(sbnOggettiLista);
			}
			if ((areaDatiPass.getDataInserimentoPropostaDa() != null && !areaDatiPass.getDataInserimentoPropostaDa().equals(""))
					&& (areaDatiPass.getDataInserimentoPropostaA() != null && !areaDatiPass.getDataInserimentoPropostaA().equals(""))) {

				SbnRangeDate sbnRangeDate = new SbnRangeDate();
 				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
 				sbnRangeDate.setTipoFiltroDate(0);
				sbnRangeDate.setDataDa(new org.exolab.castor.types.Date(df.parse(areaDatiPass.getDataInserimentoPropostaDa())));
				sbnRangeDate.setDataA(new org.exolab.castor.types.Date(df.parse(areaDatiPass.getDataInserimentoPropostaA())));
				cercaPropostaType.setRangeDate(sbnRangeDate);

			}
			if (areaDatiPass.getMittenteProposta() != null && !areaDatiPass.getMittenteProposta().equals("")) {
				cercaPropostaType.setMittenteProposta(user);
			}
			if (areaDatiPass.getDestinatarioProposta() != null && !areaDatiPass.getDestinatarioProposta().equals("")) {
				SbnUserType sbnUserTypeDestinatario = new SbnUserType();
				sbnUserTypeDestinatario.setBiblioteca(areaDatiPass.getDestinatarioProposta());
				cercaPropostaType.setDestinatarioProposta(sbnUserTypeDestinatario);
			}
			if (areaDatiPass.getStatoProposta() != null && !areaDatiPass.getStatoProposta().equals("")) {
				cercaPropostaType.setStatoProposta(SbnStatoProposta.valueOf(areaDatiPass.getStatoProposta()));
			}

			cercaType.setCercaPropostaCorrezione(cercaPropostaType);
			sbnrequest.setCerca(cercaType);

			sbnmessage.setSbnRequest(sbnrequest);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiPass.setCodErr("noServerInd");
				return areaDatiPass;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiPass.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiPass;
			}

			PropostaType propostaTypeElem;
			DestinatarioPropostaType destinatarioPropostaType;
			SinteticaProposteDiCorrezioneView proposteDiCorrezioneView;
			List<SinteticaProposteDiCorrezioneView> listaSinteticaPropCorr = new ArrayList<SinteticaProposteDiCorrezioneView>();
			String elencoDestinatari = "";
			String destinatarioElem = "";

			int tappoRicerca = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getPropostaCorrezioneCount();
			String appoDataIns = "";
			for (int i = 0 ; i < tappoRicerca; i++) {
				propostaTypeElem = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getPropostaCorrezione(i);

				proposteDiCorrezioneView = new SinteticaProposteDiCorrezioneView();
				proposteDiCorrezioneView.setIdProposta(propostaTypeElem.getIdProposta());

				appoDataIns = propostaTypeElem.getDataInserimento().toString();
				proposteDiCorrezioneView.setDataInserimento(appoDataIns.substring(8,10) + "/"
						+ appoDataIns.substring(5,7) + "/"  + appoDataIns.substring(0,4));

				elencoDestinatari = "";

				List<DestinatarioProposteDiCorrezione> listaDestPropCorr = new ArrayList<DestinatarioProposteDiCorrezione>();
				DestinatarioProposteDiCorrezione destinatarioProposteDiCorrezione;

				for (int t = 0 ; t < propostaTypeElem.getDestinatarioProposta().length; t++) {
					destinatarioPropostaType = new DestinatarioPropostaType();
					destinatarioPropostaType = propostaTypeElem.getDestinatarioProposta(t);

					// Inizio intervento BUG MANTIS 4500 collaudo - inserito if su valorizzazione della nota al fine di non prospettare righe vuote
					if (destinatarioPropostaType.getNoteProposta() != null) {
						destinatarioProposteDiCorrezione = new DestinatarioProposteDiCorrezione();

						if (destinatarioPropostaType.getDataRisposta() != null) {
							destinatarioElem = destinatarioPropostaType.getDataRisposta() + " ";
							proposteDiCorrezioneView.setDestinatariData(destinatarioPropostaType.getDataRisposta().toString());
							destinatarioProposteDiCorrezione.setDestinatariData(destinatarioPropostaType.getDataRisposta().toString());
						} else {
							destinatarioElem = "";
						}

						if (destinatarioPropostaType.getDestinatarioProposta() != null) {
							destinatarioElem = destinatarioElem
									+ destinatarioPropostaType.getDestinatarioProposta().getBiblioteca() + " "
									+ destinatarioPropostaType.getDestinatarioProposta().getUserId() + " ";
							proposteDiCorrezioneView.setDestinatariBiblio(destinatarioPropostaType.getDestinatarioProposta().getBiblioteca());
							destinatarioProposteDiCorrezione.setDestinatariBiblio(destinatarioPropostaType.getDestinatarioProposta().getBiblioteca());
						}

						if (destinatarioPropostaType.getNoteProposta() != null) {
							destinatarioElem = destinatarioElem  + "(" + destinatarioPropostaType.getNoteProposta() + ")";
							proposteDiCorrezioneView.setDestinatariNote(destinatarioPropostaType.getNoteProposta());
							destinatarioProposteDiCorrezione.setDestinatariNote(destinatarioPropostaType.getNoteProposta());
						}

						elencoDestinatari = elencoDestinatari + destinatarioElem + "<br />";
						listaDestPropCorr.add(destinatarioProposteDiCorrezione);
					}
					// Fine intervento BUG MANTIS 4500 collaudo

				}
				proposteDiCorrezioneView.setListaDestinatariProp(listaDestPropCorr);

				proposteDiCorrezioneView.setDestinatari(elencoDestinatari);
				proposteDiCorrezioneView.setIdOggetto(propostaTypeElem.getIdOggetto());
				proposteDiCorrezioneView.setKey(String.valueOf(propostaTypeElem.getIdProposta()));
				proposteDiCorrezioneView.setMittenteBiblioteca(propostaTypeElem.getMittenteProposta().getBiblioteca());
				proposteDiCorrezioneView.setMittenteUserId(propostaTypeElem.getMittenteProposta().getUserId());
				proposteDiCorrezioneView.setNumNotizie(tappoRicerca);
				proposteDiCorrezioneView.setProgressivo(i);
				proposteDiCorrezioneView.setStatoProposta(propostaTypeElem.getStatoProposta().toString());
				proposteDiCorrezioneView.setTesto(propostaTypeElem.getTestoProposta());
				proposteDiCorrezioneView.setTipoAuthority("");
				proposteDiCorrezioneView.setTipoMateriale("");
				if (propostaTypeElem.getTipoOggetto().getTipoAuthority() != null) {
					proposteDiCorrezioneView.setTipoAuthority(propostaTypeElem.getTipoOggetto().getTipoAuthority().toString());
				} else if (propostaTypeElem.getTipoOggetto().getTipoMateriale() != null) {
					proposteDiCorrezioneView.setTipoMateriale(propostaTypeElem.getTipoOggetto().getTipoMateriale().toString());
				}
				listaSinteticaPropCorr.add(proposteDiCorrezioneView);
			}

			areaDatiPass.setCodErr("0000");
			areaDatiPass.setListaSintetica(listaSinteticaPropCorr);

			return areaDatiPass;

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

	public AreaDatiVariazionePropostaDiCorrezioneVO inserisciPropostaDiCorrezione(
			AreaDatiVariazionePropostaDiCorrezioneVO areaDatiPass) {

		SBNMarc sbnRisposta = null;
		areaDatiPass.setCodErr("0000");
		areaDatiPass.setTestoProtocollo(null);
		AreaDatiVariazionePropostaDiCorrezioneVO areaDatiVariazionePropostaDiCorrezioneVOReturn = new AreaDatiVariazionePropostaDiCorrezioneVO();

		try {

			CreaType crea = null;
			CreaTypeChoice creaTypeChoice = null;
			ModificaType modifica = null;

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			if (!areaDatiPass.isModifica()) {
				crea = new CreaType();
				crea.setTipoControllo(SbnSimile.CONFERMA);
				creaTypeChoice = new CreaTypeChoice();
			} else {
				modifica = new ModificaType();
				modifica.setTipoControllo(SbnSimile.CONFERMA);

			}

			PropostaType propostaType = new PropostaType();

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			propostaType.setDataInserimento(new org.exolab.castor.types.Date(df.parse(areaDatiPass.getProposteDiCorrezioneView().getDataInserimento())));
			if (!areaDatiPass.getProposteDiCorrezioneView().getDestinatariBiblio().equals("")) {
				DestinatarioPropostaType destinatarioPropostaType = new DestinatarioPropostaType();
				DestinatarioPropostaType[] destinatarioPropostaTypeLista = new DestinatarioPropostaType[1];
				SbnUserType sbnUserTypeDestinatario = new SbnUserType();
				sbnUserTypeDestinatario.setBiblioteca(areaDatiPass.getProposteDiCorrezioneView().getDestinatariBiblio().substring(0,6));
				destinatarioPropostaType.setDestinatarioProposta(sbnUserTypeDestinatario);
				if (areaDatiPass.getProposteDiCorrezioneView().getDestinatariData() != null) {
					destinatarioPropostaType.setDataRisposta(
							new org.exolab.castor.types.Date(df.parse(areaDatiPass.getProposteDiCorrezioneView().getDestinatariData())));
				}
				if (areaDatiPass.getProposteDiCorrezioneView().getDestinatariNote() != null) {
					destinatarioPropostaType.setNoteProposta(areaDatiPass.getProposteDiCorrezioneView().getDestinatariNote());
				}
				destinatarioPropostaTypeLista[0] = destinatarioPropostaType;
				propostaType.setDestinatarioProposta(destinatarioPropostaTypeLista);

			}
			propostaType.setIdOggetto(areaDatiPass.getProposteDiCorrezioneView().getIdOggetto());
			propostaType.setIdProposta(areaDatiPass.getProposteDiCorrezioneView().getIdProposta());
			propostaType.setMittenteProposta(this.user);
			propostaType.setStatoProposta(SbnStatoProposta.valueOf(areaDatiPass.getProposteDiCorrezioneView().getStatoProposta()));
			propostaType.setTestoProposta(areaDatiPass.getProposteDiCorrezioneView().getTesto());
			SbnOggetto sbnOggetto = new SbnOggetto();

			if (areaDatiPass.getProposteDiCorrezioneView().getTipoAuthority() == null
					&& areaDatiPass.getProposteDiCorrezioneView().getTipoMateriale() == null) {
				// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE_5" fissi devono esssere modificati con il "valueOf"
				// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_5);
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(" "));
			} else {
				if (areaDatiPass.getProposteDiCorrezioneView().getTipoAuthority() != null
						&& !areaDatiPass.getProposteDiCorrezioneView().getTipoAuthority().equals("")) {
					sbnOggetto.setTipoAuthority(SbnAuthority.valueOf(areaDatiPass.getProposteDiCorrezioneView().getTipoAuthority()));
				} else if (areaDatiPass.getProposteDiCorrezioneView().getTipoMateriale() != null) {
					if (areaDatiPass.getProposteDiCorrezioneView().getTipoMateriale().equals("")) {
						// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE_5" fissi devono esssere modificati con il "valueOf"
						// sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_5);
						sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(" "));
					} else {
						sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(areaDatiPass.getProposteDiCorrezioneView().getTipoMateriale()));
					}
				}
			}

			propostaType.setTipoOggetto(sbnOggetto);

			if (!areaDatiPass.isModifica()) {
				creaTypeChoice.setPropostaCorrezione(propostaType);
				crea.setCreaTypeChoice(creaTypeChoice);
				sbnrequest.setCrea(crea);
			} else {
				modifica.setPropostaCorrezione(propostaType);
				sbnrequest.setModifica(modifica);
			}

			sbnmessage.setSbnRequest(sbnrequest);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

			if (sbnRisposta == null) {
				areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr("noServerInd");
				return areaDatiVariazionePropostaDiCorrezioneVOReturn;
			}

			if (!sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito().equals("0000")) {
				areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito());
				areaDatiVariazionePropostaDiCorrezioneVOReturn.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
				return areaDatiVariazionePropostaDiCorrezioneVOReturn;
			}

		} catch (SbnMarcException ve) {
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr("9999");
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr("9999");
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr("9999");
			areaDatiVariazionePropostaDiCorrezioneVOReturn.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		areaDatiVariazionePropostaDiCorrezioneVOReturn.setCodErr("0000");
		return areaDatiVariazionePropostaDiCorrezioneVOReturn;
	}

}
