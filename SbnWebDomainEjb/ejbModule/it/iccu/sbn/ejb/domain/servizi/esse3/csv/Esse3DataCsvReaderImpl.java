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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants.Servizi;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

/**
 * Classe di conversione da CSV dei dati ESSE3 (x LUMSA)
 *
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 17/07/2018
 */
public class Esse3DataCsvReaderImpl implements Esse3DataCsvReader {
	protected Esse3DataInputType inputType = Esse3DataInputType.CSV_FILE_BATCH;
	private Logger log = Logger.getLogger(Esse3DataCsvReaderImpl.class);
	private String separator = "\\|";
	protected String cd_polo;

	private String csv_file;
	protected List<Trl_utenti_biblioteca> users = new ArrayList<Trl_utenti_biblioteca>();
	private List<String> csv_data_ws;
	protected Tbf_biblioteca_in_polo biblioteca;
	protected List<String> errors = new ArrayList<String>();
	private List<TB_CODICI> codici = new ArrayList<TB_CODICI>();

	private Trl_utenti_biblioteca preparaUtenteModel(String line) {

		try {
			//TODO:  STA_STU_COD, DATA_INI_ATT, DATA_FIN_ATT
			String[] datas = line.split(separator, -1);
			Tbl_utenti utente = new Tbl_utenti();
			//Dati polo
			utente.setCd_bib(biblioteca);
			utente.setCod_bib("   ");
			utente.setCod_polo_bib(cd_polo);
			//utente.setCdP
			//anagrafica

			utente.setCod_fiscale(datas[8]);
			utente.setCod_utente(datas[1]);
			utente.setNome(datas[5]);
			utente.setCognome(datas[4]);
			utente.setLuogo_nascita(datas[16]);
			utente.setData_nascita(DateUtil.toDateISO(datas[7]));

			if(ValidazioneDati.isFilled(datas[6]))
				utente.setSesso(datas[6].charAt(0));


			//resisdenza
			if(ValidazioneDati.isFilled(datas[29]))
				utente.setCap_res(datas[29].trim());

			if(ValidazioneDati.isFilled(datas[25]))
				utente.setCitta_res(datas[25].trim());

			if(ValidazioneDati.isFilled(datas[27]) && ValidazioneDati.isFilled(datas[28]))
				utente.setIndirizzo_res(datas[27].trim() + ", " + datas[28].trim());

			if(ValidazioneDati.isFilled(datas[30]))
				utente.setTel_res(datas[30].trim());

			if(ValidazioneDati.isFilled(datas[20]))
				utente.setProv_res(datas[20].trim());

			//domicilio
			if(ValidazioneDati.isFilled(datas[42]))
				utente.setCap_dom(datas[42].trim());

			if(ValidazioneDati.isFilled(datas[38]))
				utente.setCitta_dom(datas[38].trim());

			if(ValidazioneDati.isFilled(datas[40]) && ValidazioneDati.isFilled(datas[41]))
				utente.setIndirizzo_dom(datas[40].trim() + ", " + datas[41].trim());

			if(ValidazioneDati.isFilled(datas[33]))
				utente.setProv_dom(datas[33].trim());

			if(ValidazioneDati.isFilled(datas[43]))
				utente.setTel_dom(datas[43].trim());

			//NAzionalita di residenza

			if(ValidazioneDati.isFilled(datas[18])) {
				final String codicePaeseIstat = datas[18];
				TB_CODICI codicePaeseDom = decodificaCodPaese(codicePaeseIstat);
				utente.setPaese_res(codicePaeseDom.getCd_tabella());
			} else {
				//Fittizio nullPointer loginAction
				utente.setPaese_res("UN"); //datas[18]
			}
			//Cittadinanza
			if(ValidazioneDati.isFilled(datas[12])) {
				final String codicePaeseIstat = datas[12];
				TB_CODICI codicePaeseCitt = decodificaCodPaese(codicePaeseIstat);
				utente.setPaese_citt(codicePaeseCitt.getCd_tabella());
			} else {
				utente.setPaese_citt("UN");

			}


			//Dati mail
			if(ValidazioneDati.isFilled(datas[10]))
				utente.setInd_posta_elettr(datas[10].trim()); //mail ateneo

			if(ValidazioneDati.isFilled(datas[9])) //mail personale
				utente.setInd_posta_elettr2(datas[9].trim());

			//Ateneo
			if(!"".equals(datas[46]))
				utente.setCod_matricola(datas[46]);

			Trl_utenti_biblioteca utente_bib = new Trl_utenti_biblioteca();
			utente_bib.setCd_biblioteca(biblioteca);

			utente_bib.setId_utenti(utente);
			//Tipo record
			utente_bib.setCod_tipo_aut(datas[0]);

			utente.setCd_tipo_ute(Servizi.Utenti.UTENTE_TIPO_ESTERNO_CHR);
			//Utenza disabilitata
			if(ValidazioneDati.isFilled(datas[2]) && "1".equals(datas[2])) {
				//disattivazione immediata
				utente_bib.setData_fine_aut(DateUtil.addDay(DaoManager.now(), -1));

			}

			return utente_bib;
		} catch (Exception e) {
			log.error("Errore CSV linea: " + line);
			errors.add("Conversione CSV linea: " + line +" Errore: " + e.getMessage());
			log.error(e);
			return null;
		}

	}

	protected TB_CODICI decodificaCodPaese(final String codicePaeseIstat) {
		TB_CODICI codicePaese = Stream.of(codici).filter(new Predicate<TB_CODICI>() {
				public boolean test(TB_CODICI arg0) {
					return codicePaeseIstat.equals(arg0.getCd_flg2());
				}
			}).findFirst().orElse(new TB_CODICI("UN",""));
		return codicePaese;
	}

	/**
	 * Costruttore per lettura da file CSV
	 *
	 * @param String  cd_polo Codice del polo
	 * @param String  cd_biblioteca Codice biblioteca
	 * @param String  csv_file Path dove è sistuato il file csv
	 */
	public Esse3DataCsvReaderImpl(String cd_polo, String cd_biblioteca, String csv_file) {
		super();
		this.csv_file = csv_file;
		this.inputType = Esse3DataInputType.CSV_FILE_BATCH;
		initPoloBibCodici(cd_polo, cd_biblioteca);
	}
	/**
	 * Costruttore per preparazione dati da WebService
	 *
	 * @param String  cd_polo Codice del polo
	 * @param String  cd_biblioteca Codice biblioteca
	 * @param List<String>  csvWsData lista di stringhe CSV con i dati
	 */
	public Esse3DataCsvReaderImpl(String cd_polo, String cd_biblioteca, List<String> csvWsData) {
		super();
		this.csv_file = null;
		this.inputType = Esse3DataInputType.WEBSERVICE;
		this.csv_data_ws = csvWsData;
		initPoloBibCodici(cd_polo, cd_biblioteca);
	}
	public Esse3DataCsvReaderImpl() {

	}

	protected void initPoloBibCodici(String cd_polo, String cd_biblioteca) {
		this.cd_polo = cd_polo;
		this.biblioteca = DaoManager.creaIdBib(this.cd_polo, cd_biblioteca);
		try {
			codici = CodiciProvider.getCodici(CodiciType.CODICE_PAESE);
		} catch (Exception e) {
			log.error("Errore Codici Provider", e);
			errors.add("Errore Codici Provider: " + e.getMessage());
		}
	}

	/**
	 * Esegue la lettura e la conversione del csv <br>
	 * Ritorna se la conversione è andata a buon fine oppure no.<br>
	 * In caso di errore vedere i log
	 *
	 * @return boolean
	 */
	private boolean readFromCSVFile() {
		if (!ValidazioneDati.isFilled(csv_file) )
			return false;
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(csv_file+""));
			while ((line = br.readLine()) != null) {

				Trl_utenti_biblioteca utente = preparaUtenteModel(line);
				if (utente != null) {
					users.add(utente);
				}

			}
		} catch (IOException e) {
			log.error("Errore lettura CSV:", e);
		}

		return true;
	}
	private boolean readFromWsData() {
		if(!ValidazioneDati.isFilled(this.csv_data_ws) || this.csv_data_ws.isEmpty()) {
			String errorMessage = "Errore lettura dati webservice lista dati CSV non piena";
			log.error(errorMessage);
			errors.add(errorMessage);
			return false;
		}
		for(String line: csv_data_ws) {
			Trl_utenti_biblioteca utente = preparaUtenteModel(line);
			if (utente != null) {
				users.add(utente);
			}
		}
		return true;
	}

	/**
	 * Imposta un sparatore custom, default: '|' <br>
	 * Ricordati di impostarlo prima di eseguire read()
	 *
	 * @param String separator stringa separatrice
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Prepara un utente convertito dal CSV in UtenteBaseVO
	 *
	 * @see ServiziConversioneVO
	 *
	 * @param Tbl_utenti utente
	 *
	 * @return UtenteBibliotecaVO
	 */
	protected UtenteBibliotecaVO convertUtenteBibliotecaVO(Trl_utenti_biblioteca utente) {
		try {
			return ServiziConversioneVO.daHibernateAWebUteDettaglio(utente, null, null, null, null);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}


	public boolean read() {
		switch (this.inputType) {
		case CSV_FILE_BATCH:
			boolean hasReadedFromCSVFile = readFromCSVFile();
			logUsersSize();

			return hasReadedFromCSVFile;

		case WEBSERVICE:
			boolean hasReadedFromWsData = readFromWsData();
			logUsersSize();
			return hasReadedFromWsData;
		default:
			log.error("Tipo di import dei dati non definito, nulla sarà importato");
			return false;
		}
	}

	private void logUsersSize() {
		log.debug("Letti dati CSV: " + this.users.size());
	}

	/**
	 * Ritorna la lista di utenti convertiti <br>
	 *
	 * @return List<UtenteBaseVO>
	 */
	public List<UtenteBibliotecaVO> getUtentiBibliotecaVO() {
		List<UtenteBibliotecaVO> users = new ArrayList<UtenteBibliotecaVO>();
		if (users.size() == 0)
			read();
		for (Trl_utenti_biblioteca utente : this.users) {
			users.add(convertUtenteBibliotecaVO(utente));
		}
		return users;
	}
	/**
	 * Ritorna la lista di utenti convertiti <br>
	 *
	 * @return List<Tbl_utenti>
	 */
	public List<Trl_utenti_biblioteca> getUtenti() {
		if (users.size() == 0)
			read();
		return users;
	}

	public List<String> getErrors() {
		return errors;
	}

}
