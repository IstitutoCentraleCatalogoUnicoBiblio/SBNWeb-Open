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
package it.iccu.sbn.SbnMarcFactory.util.semantica;

import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A600;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.semantica.SemanticaDAO;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.util.semantica.SemanticaUtil;

public class DettaglioOggettoSemantico {

	private final String ticket;
	private final SemanticaDAO dao;

	public DettaglioOggettoSemantico(String ticket) {
		super();
		this.ticket = ticket;

		this.dao = new SemanticaDAO();
	}

	public DettaglioSoggettoVO getDettaglioSoggetto(
			boolean livelloPolo, SoggettoType soggettoType, boolean completo) throws DaoManagerException {

		DettaglioSoggettoVO dettaglioSogg = new DettaglioSoggettoVO();

		A250 a250 = soggettoType.getT250();
		// CID
		String cidRadice = soggettoType.getT001();
		int numTitoliPolo = soggettoType.getNum_tit_coll();
		int numTitoliBiblio = soggettoType.getNum_tit_coll_bib();
		boolean hasTit = soggettoType.hasNum_tit_coll();
		boolean hasTitBib = soggettoType.hasNum_tit_coll_bib();

		// STRINGA DI SOGGETTO
		// Composta da a_250 sommato agli x_250, separati da " - ".
		String stringaSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

		dettaglioSogg.setLivelloPolo(livelloPolo);
		dettaglioSogg.setCid(cidRadice);
		dettaglioSogg.setDataIns(SBNMarcUtil.converteDataSBN(soggettoType.getT100().getA_100_0().toString()) );
		dettaglioSogg.setTesto(stringaSoggetto);
		dettaglioSogg.setLivAut(soggettoType.getLivelloAut().toString());
		dettaglioSogg.setT005(soggettoType.getT005());
		dettaglioSogg.setDataAgg(SBNMarcUtil.converteDataVariazione(soggettoType.getT005()));
		dettaglioSogg.setCampoSoggettario(SemanticaUtil.getSoggettarioSBN(a250));

		if (livelloPolo) {
			// categoria e nota
			A600 t600 = soggettoType.getT600();
			dettaglioSogg.setCategoriaSoggetto(t600.getA_601());
			dettaglioSogg.setNote(t600.getA_602());

			dettaglioSogg.setCondiviso(soggettoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE);

			//almaviva5_20111123 evolutive CFI
			dettaglioSogg.setEdizioneSoggettario(a250.getEdizione() != null ? a250.getEdizione().toString() : null);

			dettaglioSogg.setNumTitoliPolo(numTitoliPolo);
			dettaglioSogg.setNumTitoliBiblio(numTitoliBiblio);
			dettaglioSogg.setHas_num_tit_coll(hasTit);
			dettaglioSogg.setHas_num_tit_coll_bib(hasTitBib);
			if (completo)
				dettaglioSogg.setDatiCondivisione(dao.getDatiCondivisioneSoggetto(cidRadice, null, null));


		} else {
			//almaviva5_20111126 evolutive CFI
			//in indice non è prevista edizione per il soggettario
			//va ricavato dal codice soggettario inviato dall'indice
			dettaglioSogg.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(a250));

			if (completo)
				dettaglioSogg.setDatiCondivisione(dao.getDatiCondivisioneSoggetto(null, cidRadice, null));

			dettaglioSogg.setCondiviso(true);	//soggetto di indice
		}

		return dettaglioSogg;
	}// end getDettaglioSoggetto

	public DettaglioDescrittoreVO getDettaglioDescrittore(
			boolean livelloPolo, DescrittoreType descrittore, boolean completo) {

		DettaglioDescrittoreVO dd = new DettaglioDescrittoreVO();
		A931 a931 = new A931();
		a931 = descrittore.getT931();

		// DID
		String didRadice = descrittore.getT001();

		// Nota del Did
		String nota = descrittore.getT931().getB_931();

		// STRINGA DI Descrittore
		// Composta da a_931
		String stringaDescrittore = a931.getA_931();

		dd.setLivelloPolo(livelloPolo);
		dd.setCampoSoggettario(SemanticaUtil.getSoggettarioSBN(a931));
		dd.setDid(didRadice);
		dd.setT005(descrittore.getT005());
		dd.setDataIns(SBNMarcUtil.converteDataSBN(descrittore.getT100().getA_100_0().toString()) );
		dd.setDataAgg(SBNMarcUtil.converteDataVariazione(descrittore.getT005()));
		dd.setLivAut(descrittore.getLivelloAut().toString());
		dd.setTesto(stringaDescrittore);
		dd.setNote(nota);
		dd.setFormaNome(descrittore.getFormaNome().toString());

		if (livelloPolo) {
			dd.setCondiviso(descrittore.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE);
			//almaviva5_20111126 evolutive CFI
			dd.setEdizioneSoggettario(a931.getEdizione() != null ? a931.getEdizione().toString() : null);
			dd.setCategoriaTermine(a931.getCat_termine());

			try {
				if (completo) {
					dd.setSoggettiCollegati(dao.contaSoggettiCollegatiDid(didRadice));
					dd.setSoggettiUtilizzati(dao.contaSoggettiUtilizzatiCollegatiDid(SemanticaDAO
									.codPoloFromTicket(ticket), SemanticaDAO.codBibFromTicket(ticket), didRadice));
				}
			} catch (DaoManagerException e) {
				e.printStackTrace();
			}
		} else {
			dd.setEdizioneSoggettario(SemanticaUtil.getEdizioneSoggettarioIndice(a931));
		}

		return dd;
	}// end getDettaglioDescrittore

	public CreaVariaDescrittoreVO fillCreaVariaDescrittoreVO(SBNMarc risposta) {

		CreaVariaDescrittoreVO newDid = new CreaVariaDescrittoreVO();
		if (risposta == null)
			return null;

		SbnMessageType sbnMessage = risposta.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();
		newDid.setTotRighe(totRighe);
		newDid.setEsito(esito);
		newDid.setTestoEsito(testoEsito);

		if (sbnOutPut.getElementoAutCount() > 0) {
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

			newDid.setDid(datiElemento.getT001());
			if (esito.equals("0000")) {
				String dataIns = SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005());
				newDid.setDataInserimento(dataIns);
				newDid.setDataVariazione(dataIns);
				newDid.setT005(datiElemento.getT005());
				newDid.setLivelloAutorita(datiElemento.getLivelloAut()
						.toString());
				if (datiElemento instanceof DescrittoreType) {
					newDid.setFormaNome(datiElemento.getFormaNome().toString());
					A931 a931 = ((DescrittoreType)datiElemento).getT931();
					newDid.setTesto(a931.getA_931());
					newDid.setNote(a931.getB_931());
					newDid.setCodiceSoggettario(a931.getC2_931());
					newDid.setCategoriaTermine(a931.getCat_termine());
				}
			}
		}

		return newDid;
	}

	public CreaVariaTermineVO fillCreaVariaTermineVO(SBNMarc risposta) {

		CreaVariaTermineVO newDid = new CreaVariaTermineVO();
		if (risposta == null)
			return null;

		SbnMessageType sbnMessage = risposta.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();
		String testoEsito = sbnResult.getTestoEsito();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		int totRighe = sbnOutPut.getTotRighe();
		newDid.setTotRighe(totRighe);
		newDid.setEsito(esito);
		newDid.setTestoEsito(testoEsito);

		if (sbnOutPut.getElementoAutCount() > 0) {
			ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
			DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

			newDid.setDid(datiElemento.getT001());
			if (esito.equals("0000")) {
				String dataIns = SBNMarcUtil
						.converteDataVariazione(datiElemento.getT005());
				newDid.setDataInserimento(dataIns);
				newDid.setDataVariazione(dataIns);
				newDid.setT005(datiElemento.getT005());
				newDid.setLivelloAutorita(datiElemento.getLivelloAut()
						.toString());
				if (datiElemento instanceof TermineType) {
					newDid.setFormaNome(datiElemento.getFormaNome().toString());
					A935 a935 = ((TermineType)datiElemento).getT935();
					newDid.setTesto(a935.getA_935());
					newDid.setNote(a935.getB_935());
					newDid.setCodThesauro(a935.getC2_935());
				}
			}
		}

		return newDid;
	}


	public DettaglioTermineThesauroVO getDettaglioTermine(boolean livelloPolo,
			TermineType termine) {
		DettaglioTermineThesauroVO dettaglioTermine = new DettaglioTermineThesauroVO();

		// DID
		String didRadice = termine.getT001();
		A935 a935 = termine.getT935();
		// Nota del Did
		String nota = a935.getB_935();
		String testo = a935.getA_935();

		dettaglioTermine.setLivelloPolo(livelloPolo);
		dettaglioTermine.setCodThesauro(a935.getC2_935());
		dettaglioTermine.setDid(didRadice);
		dettaglioTermine.setT005(termine.getT005());
		dettaglioTermine.setDataIns(SBNMarcUtil.converteDataSBN(termine.getT100().getA_100_0().toString()));
		dettaglioTermine.setDataAgg(SBNMarcUtil.converteDataVariazione(termine.getT005()));
		dettaglioTermine.setLivAut(termine.getLivelloAut().toString());
		dettaglioTermine.setTesto(testo);
		dettaglioTermine.setNote(nota);
		dettaglioTermine.setFormaNome(termine.getFormaNome().toString());

		if (livelloPolo) {
			dettaglioTermine.setCondiviso(termine.getCondiviso()
					.getType() == DatiElementoTypeCondivisoType.S_TYPE);
			dettaglioTermine.setNumTitoliBiblio(termine.getNum_tit_coll_bib());
			dettaglioTermine.setNumTitoliPolo(termine.getNum_tit_coll());
		}

		return dettaglioTermine;
	}// end getDettaglioTermine

}
