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
package it.iccu.sbn.ejb.domain.servizi.esse3.csv;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.vo.xml.binding.esse3.PERSONA;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class Esse3DataPersonaReader extends Esse3DataCsvReaderImpl {
	private List<PERSONA> persone_data_ws;


	private Trl_utenti_biblioteca preparaUtenteModel (PERSONA p) {
		Tbl_utenti utente = new Tbl_utenti();
		//Dati polo
		utente.setCd_bib(biblioteca);
		utente.setCod_bib("   ");
		utente.setCod_polo_bib(cd_polo);

		utente.setCod_fiscale(p.getCODFIS());
		utente.setCod_utente(p.getUSERID());
		utente.setNome(p.getNOME());
		utente.setCognome(p.getCOGNOME());
		
		//Almaviva3 04/04/2019 MANU campo not-null
		utente.setLuogo_nascita(ValidazioneDati.trimOrEmpty(p.getCOMNASCDES()));

		XMLGregorianCalendar data_nascita = p.getDATANASCITA();
		if(data_nascita != null)
			utente.setData_nascita(DateUtil.toDateISO(data_nascita.toString()));
		else //Se vuoto campo not-null imposto a oggi
			utente.setData_nascita(DaoManager.now());
		
		if(ValidazioneDati.isFilled(p.getSESSO()))
			utente.setSesso(p.getSESSO().charAt(0));


		//residenza
		if(ValidazioneDati.isFilled(p.getCAPRES()))
			utente.setCap_res(String.valueOf(p.getCAPRES()));

		if(ValidazioneDati.isFilled(p.getCOMRESDES()))
			utente.setCitta_res(p.getCOMRESDES());

		if(ValidazioneDati.isFilled(p.getINDIRIZZORES()) && ValidazioneDati.isFilled(p.getINDIRIZZORESCIVICO()))
			utente.setIndirizzo_res(p.getINDIRIZZORES().trim() + ", " + p.getINDIRIZZORESCIVICO().trim());

		if(ValidazioneDati.isFilled(p.getTELRES()))
			utente.setTel_res(p.getTELRES());

		if(ValidazioneDati.isFilled(p.getPROVRESSIGLA()))
			utente.setProv_res(p.getPROVRESSIGLA());

		//domicilio
		if(ValidazioneDati.isFilled(p.getCAPDOM()))
			utente.setCap_dom(String.valueOf(p.getCAPDOM()));

		if(ValidazioneDati.isFilled(p.getCOMDOMDES()))
			utente.setCitta_dom(p.getCOMDOMDES());

		if(ValidazioneDati.isFilled(p.getINDIRIZZODOM()) && ValidazioneDati.isFilled(p.getINDIRIZZODOMCIVICO()))
			utente.setIndirizzo_dom(p.getINDIRIZZODOM().trim() + ", " + p.getINDIRIZZODOMCIVICO().trim());

		if(ValidazioneDati.isFilled(p.getPROVDOMSIGLA()))
			utente.setProv_dom(p.getPROVDOMSIGLA().trim());

		if(ValidazioneDati.isFilled(p.getTELDOM()))
			utente.setTel_dom(p.getTELDOM().trim());
		//paese residenza
		if(ValidazioneDati.isFilled(p.getNAZRESCOD())) {
			final String codicePaeseIstat = String.valueOf(p.getNAZRESCOD());
			TB_CODICI codicePaeseDom = decodificaCodPaese(codicePaeseIstat);
			utente.setPaese_res(codicePaeseDom.getCd_tabella());
		} else {
			//Fittizio nullPointer loginAction
			utente.setPaese_res("UN"); //datas[18]
		}
		//Cittadinanza
		if(ValidazioneDati.isFilled(p.getNAZNASCCOD())) {
			final String codicePaeseIstat = String.valueOf(p.getNAZNASCCOD()) ;
			TB_CODICI codicePaeseCitt = decodificaCodPaese(codicePaeseIstat);
			utente.setPaese_citt(codicePaeseCitt.getCd_tabella());
		} else {
			utente.setPaese_citt("UN");

		}

		//Dati mail
		if(ValidazioneDati.isFilled(p.getEMAILATE()))
			utente.setInd_posta_elettr(p.getEMAILATE().trim()); //mail ateneo

		if(ValidazioneDati.isFilled(p.getEMAIL())) //mail personale
			utente.setInd_posta_elettr2(p.getEMAIL().trim());

		//Ateneo
		if(!"".equals(p.getMATRICOLA()))
			utente.setCod_matricola(p.getMATRICOLA());

		Trl_utenti_biblioteca utente_bib = new Trl_utenti_biblioteca();
		utente_bib.setCd_biblioteca(biblioteca);

		utente_bib.setId_utenti(utente);
		//Tipo record
		utente_bib.setCod_tipo_aut(p.getTIPORECORD());

		utente.setCd_tipo_ute(Servizi.Utenti.UTENTE_TIPO_ESTERNO_CHR);
		//Utenza disabilitata
		if(ValidazioneDati.isFilled(p.getDISABLEFLG()) && "1".equals(p.getDISABLEFLG().toString()))
			//disattivazione immediata
			utente_bib.setData_fine_aut(DateUtil.addDay(DaoManager.now(), -1));
		return utente_bib;

	}

	public Esse3DataPersonaReader(String cd_polo, String cd_biblioteca, List<PERSONA> persone) {
		this.inputType = Esse3DataInputType.CSV_FILE_BATCH;
		initPoloBibCodici(cd_polo, cd_biblioteca);
		this.persone_data_ws = persone;
	}
	public boolean read() {
		try {
			for (PERSONA p: persone_data_ws) {
				Trl_utenti_biblioteca utente = preparaUtenteModel(p);
				if (utente != null) {
					users.add(utente);
				}
			}
		} catch (Exception e) {
			errors.add("Errore in conversione Persona Model");
			return false;
		}
		return true;
	}
	public List<UtenteBibliotecaVO> getUtentiBibliotecaVO() {
		List<UtenteBibliotecaVO> users = new ArrayList<UtenteBibliotecaVO>();
		if (users.size() == 0)
			read();
		for (Trl_utenti_biblioteca utente : this.users) {
			users.add(convertUtenteBibliotecaVO(utente));
		}
		return users;
	}
	public List<Trl_utenti_biblioteca> getUtenti() {
		if (users.size() == 0)
			read();
		return users;
	}

	public List<String> getErrors() {
		return errors;
	}

}
