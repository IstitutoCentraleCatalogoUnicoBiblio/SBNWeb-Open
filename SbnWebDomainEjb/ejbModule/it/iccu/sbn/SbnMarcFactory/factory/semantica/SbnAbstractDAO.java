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
import it.iccu.sbn.ejb.model.unimarcmodel.A999;
import it.iccu.sbn.ejb.model.unimarcmodel.AbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.CreaVariaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.SinteticaAbstractPerLegameTitVO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 */
public class SbnAbstractDAO {

	private static Logger log = Logger.getLogger(SbnAbstractDAO.class);
	private FactorySbn polo;
	private SbnUserType user;

	/**
	 * @param indice
	 * @param polo
	 * @param user
	 */
	public SbnAbstractDAO(FactorySbn indice, FactorySbn polo, SbnUserType user) {
		this.user = user;
		this.polo = polo;
		log.debug("Istanzio la classe DAO per l'abstract SbnAbstractDao");
	}

	public SBNMarc ricercaAbstractPerBidCollegato(RicercaAbstractVO datiRicerca) {

		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CercaType cercaType = new CercaType();
			CercaDatiAutType cercaDatiAuType = new CercaDatiAutType();
			CercaElementoAutType cercaElementoAutType = new CercaElementoAutType();

			cercaDatiAuType.setTipoAuthority(SbnAuthority.AB);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_1);
			cercaType.setNumPrimo(1);

			// tipoOrd
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_2);

			// IMPOSTAZIONE CANALE PRIMARIO
			// CANALE : BID
			if (ValidazioneDati.isFilled(datiRicerca.getBid())) {
				CanaliCercaDatiAutType canaliCercaDatiAutType = new CanaliCercaDatiAutType();
				canaliCercaDatiAutType.setT001(datiRicerca.getBid());
				cercaDatiAuType.setCanaliCercaDatiAut(canaliCercaDatiAutType);
			} else
				return null;

			cercaElementoAutType.setCercaDatiAut(cercaDatiAuType);
			cercaType.setCercaElementoAut(cercaElementoAutType);
			sbnrequest.setCerca(cercaType);
			sbnmessage.setSbnRequest(sbnrequest);

			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
		} catch (Exception e) {
			log.error("", e);
		}
		return sbnRisposta;
	}

	public List<SinteticaAbstractPerLegameTitVO> creaListaSinteticaAbstract(SBNMarc response) {
		List<SinteticaAbstractPerLegameTitVO> listaSintentica = null;
		SinteticaAbstractPerLegameTitVO abstracto = new SinteticaAbstractPerLegameTitVO();

		// Reperimento dei dati della lista: classe SbnOutputType
		SbnMessageType sbnMessage = response.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		int numeroElementi = sbnOutPut.getElementoAutCount();
		if (numeroElementi < 1) {
			return new ArrayList<SinteticaAbstractPerLegameTitVO>();
		}

		int progressivo = sbnOutPut.getNumPrimo() - 1; // 0;
		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		if (SBNMarcUtil.eqAuthority(datiElemento.getTipoAuthority(), SbnAuthority.AB) ) {
			listaSintentica = new ArrayList<SinteticaAbstractPerLegameTitVO>();
			for (int i = 0; i < numeroElementi; i++) {
				abstracto = creaElementoListaAbstract(sbnOutPut, i, ++progressivo);
				listaSintentica.add(abstracto);
			}
		}

		return listaSintentica;
	}

	public SinteticaAbstractPerLegameTitVO creaElementoListaAbstract(
			SbnOutputType sbnOutPut, int elementIndex, int progressivo) {
		SinteticaAbstractPerLegameTitVO abstracto = new SinteticaAbstractPerLegameTitVO();

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
		AbstractType abstractType = (AbstractType) datiElemento;

		abstracto.setProgr(progressivo);
		String descr = abstractType.getT999().getA_999();

		abstracto.setT005(abstractType.getT005());
		abstracto.setLivelloAutorita(abstractType.getLivelloAut().toString());
		abstracto.setDescrizione(descr);
		String dataVar = SBNMarcUtil.converteDataVariazione(datiElemento
				.getT005());
		abstracto.setDataVariazione(dataVar);
		if (datiElemento.getT100() != null) {
			String dataIns = SBNMarcUtil.converteDataSBN(datiElemento.getT100()
					.getA_100_0().toString());
			abstracto.setDataInserimento(dataIns);
		} else {
			abstracto.setDataInserimento(dataVar);
		}

		return abstracto;

	}// end creaElementoListaAbstract

	public SBNMarc creaLegameTitoloAbstract(CreaVariaAbstractVO abstracto) {

		AbstractType abstractType = new AbstractType();
		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CreaType crea = new CreaType();
			CreaTypeChoice creaTypeChoice = new CreaTypeChoice();

			// TIPO CONTROLLO (Conferma, Simile, SimileImport)
			crea.setTipoControllo(SbnSimile.CONFERMA);

			ElementAutType elementAutType = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();
			// TIPO AUTHORITY
			datiElemento.setTipoAuthority(SbnAuthority.AB);
			abstractType.setTipoAuthority(SbnAuthority.AB);
			// Livello di Autorità
			String livello = abstracto.getLivello();
			datiElemento.setLivelloAut(SbnLivello.valueOf(livello));
			abstractType.setLivelloAut(SbnLivello.valueOf(livello));

			// DATI DELL'ABSTRACT
			A999 a999 = new A999();

			// //////////// PRENDE I DATI INSERITI /////
			// BID
			abstractType.setT001(abstracto.getT001());

			// Descrizione
			a999.setA_999(abstracto.getDescrizione());

			abstractType.setT999(a999);

			datiElemento = abstractType;
			elementAutType.setDatiElementoAut(datiElemento);

			creaTypeChoice.setElementoAut(elementAutType);
			crea.setCreaTypeChoice(creaTypeChoice);
			sbnrequest.setCrea(crea);
			sbnmessage.setSbnRequest(sbnrequest);

			// VALIDAZIONE DEL FILE XML
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
			System.out.println("ERROR creaLegameTitoloAbstract: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			System.out.println("ERROR creaLegameTitoloAbstract: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end creaLegameTitoloAbstract

	public SBNMarc variaLegameTitoloAbstract(CreaVariaAbstractVO abstracto) {

		AbstractType abstractType = new AbstractType();
		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			ModificaType modifica = null;

			modifica = new ModificaType();
			modifica.setTipoControllo(SbnSimile.CONFERMA);

			ElementAutType elementAutType = new ElementAutType();
			DatiElementoType datiElemento = new DatiElementoType();

			// TIPO AUTHORITY
			datiElemento.setTipoAuthority(SbnAuthority.AB);

			abstractType.setTipoAuthority(SbnAuthority.AB);

			// Livello di Autorità
			String livello = abstracto.getLivello();
			datiElemento.setLivelloAut(SbnLivello.valueOf(livello));
			abstractType.setLivelloAut(SbnLivello.valueOf(livello));

			// DATI DELL'ABSTRACT
			A999 a999 = new A999();

			// Descrizione
			a999.setA_999(abstracto.getDescrizione());

			abstractType.setT999(a999);

			// Bid
			abstractType.setT001(abstracto.getT001());

			datiElemento = abstractType;

			// Data di variazione
			datiElemento.setT005(abstracto.getT005());

			// Stato Record (C)
			datiElemento.setStatoRecord(StatoRecord.C);

			elementAutType.setDatiElementoAut(datiElemento);

			modifica.setElementoAut(elementAutType);
			// sbnRichiesta.setSbnMessage(sbnmessage);

			sbnrequest.setModifica(modifica);
			sbnmessage.setSbnRequest(sbnrequest);
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
			System.out.println("ERROR variaLegameTitoloAbstract: "
					+ ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
			System.out.println("ERROR variaLegameTitoloAbstract: "
					+ e.getMessage() + e.toString());
		}

		return sbnRisposta;
	} // end variaLegameTitoloAbstract

	/**
	 * Questo metodo fa una richiesta di cancellazione Soggetto al protocollo
	 *
	 * @param id
	 *            cid del soggetto.
	 * @param frame
	 *            frame padre
	 * @return Oggetto Castor con la risposta
	 */
	public SBNMarc cancellaLegameTitoloAbstract(String bid) {

		SBNMarc sbnRisposta = null;

		try {
			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();
			CancellaType cancellaType = new CancellaType();
			SbnOggetto sbnOggetto = new SbnOggetto();
			sbnOggetto.setTipoAuthority(SbnAuthority.AB);

			cancellaType.setTipoOggetto(sbnOggetto);
			cancellaType.setIdCancella(bid);

			// sbnRichiesta.setSbnMessage(sbnmessage);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCancella(cancellaType);
			this.polo.setMessage(sbnmessage, this.user);
			sbnRisposta = this.polo.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			log.error("", ve);
			System.out.println("ERROR >>" + ve.getMessage() + ve.toString());
		} catch (Exception e) {
			log.error("", e);
		}

		return sbnRisposta;
	} // end cancellaLegameTitoloAbstract

}
