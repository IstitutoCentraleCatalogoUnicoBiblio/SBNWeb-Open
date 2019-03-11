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
package it.iccu.sbn.web.integration.servizi.erogazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.AnagrafeUtenteProfessionaleVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AttivitaServizioChecker;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.ControlloFaseIter;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.ControlloFaseIter.Result;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.vo.UserVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class ControlloAttivitaServizio extends SerializableVO {

	static Logger log = Logger.getLogger(ControlloAttivitaServizio.class);

	private static final long serialVersionUID = -8458263447962630152L;
	private static final String CONFIG_XML = "/META-INF/ControlloAttivitaServizio.xml";
	private static final String ATTIVITA_BASE = "00";

	private static final Map<String, String> defaults = new HashMap<String, String>();
	private static final Map<String, AttivitaServizioChecker> instances = new ConcurrentHashMap<String, AttivitaServizioChecker>();
	private static final AtomicBoolean initialized = new AtomicBoolean(false);

	private IterServizioVO passoIter;
	private List<FaseControlloIterVO> controlli;
	private List<AnagrafeUtenteProfessionaleVO> bibliotecari;

	private static final synchronized boolean loadConfigXML() {
		try {
			InputStream input = ControlloAttivitaServizio.class.getResourceAsStream(CONFIG_XML);

			Digester d = new Digester();
			d.setValidating(false);
			d.push(defaults);
			d.addCallMethod("controlli/attivita", "put", 2);
			d.addCallParam("controlli/attivita", 0, "iso");
			d.addCallParam("controlli/attivita", 1, "classe");

			d.parse(input);
			return true;

		} catch (IOException e) {
			return false;
		} catch (SAXException e) {
			return false;
		}
	}

	public static final ControlloAttivitaServizio getControlliBase() {
		IterServizioVO fakeIter = new IterServizioVO();
		fakeIter.setCodAttivita(ATTIVITA_BASE);
		List<FaseControlloIterVO> fakeControlli = new ArrayList<FaseControlloIterVO>();
		List<AnagrafeUtenteProfessionaleVO> fakeBibliotecari = new ArrayList<AnagrafeUtenteProfessionaleVO>();
		return new ControlloAttivitaServizio(fakeIter, fakeControlli, fakeBibliotecari);
	}

	public ControlloAttivitaServizio() {
		super();
		if (!initialized.get() )
			initialized.set(loadConfigXML());

		passoIter = new IterServizioVO();
		controlli = new ArrayList<FaseControlloIterVO>();
		bibliotecari = new ArrayList<AnagrafeUtenteProfessionaleVO>();
	}

	public ControlloAttivitaServizio(IterServizioVO passoIter,
			List<FaseControlloIterVO> controlli,
			List<AnagrafeUtenteProfessionaleVO> bibliotecari) {
		super();
		if (!initialized.get() )
			initialized.set(loadConfigXML());

		this.passoIter = passoIter;
		this.controlli = controlli;
		this.bibliotecari = bibliotecari;
	}

	/**
	 * @param attivitaEjb
	 *            : Istanza di it.iccu.sbn.ejb.vo.servizi.Attivita creata dalla
	 *            funzione dell'EJB che legge i dati relativi ad una attività.
	 *
	 */
	public ControlloAttivitaServizio(AttivitaServizioVO attivita) {
		super();
		if (!initialized.get() )
			initialized.set(loadConfigXML());

		this.passoIter = attivita.getPassoIter();
		this.controlli = attivita.getControlli();
		this.bibliotecari = attivita.getBibliotecari();
	}

	public IterServizioVO getPassoIter() {
		return passoIter;
	}

	public void setPassoIter(IterServizioVO passoIter) {
		this.passoIter = passoIter;
	}

	public void setControlli(List<FaseControlloIterVO> controlli) {
		this.controlli = controlli;
	}

	public void setBibliotecari(List<AnagrafeUtenteProfessionaleVO> bibliotecari) {
		this.bibliotecari = bibliotecari;
	}

	public boolean bibliotecarioAbilitato(UserVO loggedUser) {
		if (!isFilled(bibliotecari) )
			return true;	//tutti autorizzati

		for (AnagrafeUtenteProfessionaleVO aup : bibliotecari)
			if (aup.getIdUtenteProfessionale() == loggedUser.getIdUtenteProfessionale())
				return true;

		return false;
	}

	/**
	 *
	 * it.iccu.sbn.ejb.vo.servizi.erogazione Attivita.java controlloIter boolean
	 *
	 * @param dati
	 * @param msg
	 *            messaggi di diagnostica relativi alla esecuzione dei controlli
	 * @return <ul>
	 *         <li><strong>true</strong>: se l'esecuzione dei controlli è andata
	 *         a buon fine o, se un controllo non è passato, comunque non si
	 *         tratta di un controllo bloccante</li>
	 *         <li><strong>false</strong>: se un controllo bloccante non è
	 *         andato a buon fine</li>
	 *         </ul>
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws Exception
	 *
	 *
	 */
	public boolean controlloIter(DatiControlloVO dati, List<String> messaggi,
			boolean controlloAggiornamento, boolean controlloInoltraPrenotazione) throws Exception {
		boolean ok = true;

		if (messaggi == null)
			messaggi = new ArrayList<String>();

		dati.setInoltroPrenotazione(controlloInoltraPrenotazione);

		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(
				dati.getMovimento().getCodTipoServ(),
				CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		// carico in richiedeSupportoCodici il flag della tabella codici relativo a "richiede supporti"
		for (FaseControlloIterVO controlloIter : this.controlli) {
			String classeControllo = controlloIter.getClasse();

			if (ValidazioneDati.strIsNull(classeControllo))
				return true;

			// verifico che, se impostato a "S" il parametro "controlloAggiornamento"
			// questo sia uguale al flag "controllo aggiornamento" nel controllo
			if (controlloAggiornamento && !controlloIter.isControlloAggiornamento())
				// Il parametro "controlloAggiornamento" è impostato a "S"
				// e nel controllo il flag su "controllo aggiornamento" non è impostato a "S":
				// non effettuo il controllo e vado a quello successivo
				continue;


			// verifico che, se impostato a "S" nel controllo il flag su "richiede supporto",
			// questo sia uguale al flag "richiede supporti" della tabella codici tipo servizio
			// per effettuare il controllo solo se il servizio lo prevede
			if (controlloIter.isRichiedeSupporto() && !ts.richiedeSupporto())
				// il flag nel controllo è impostato a "S"
				// e il flag "richiede supporto" della tabella codici non è impostato a "S":
				// non effettuo il controllo e vado a quello successivo
				continue;


			// verifico che, se impostato a "S" il parametro "controlloInoltraPrenotazione",
			// non sia impostato a "N" il flag "controllo inoltra prenotazione" nel controllo
			if (controlloInoltraPrenotazione && !controlloIter.isControlloInoltraPrenotazione())
				// Il parametro "controlloInoltraPrenotazione" è impostato a "S"
				// e nel controllo il flag su "controllo inoltra prenotazione" è impostato a "N":
				// non effettuo il controllo e vado a quello successivo
				continue;


			Class<?> classe = Thread.currentThread().getContextClassLoader().loadClass(classeControllo);
			Object instance = classe.newInstance();

			if ( !(instance instanceof ControlloFaseIter) )
				return true;

			ControlloFaseIter controllo = (ControlloFaseIter) instance;

			// Eseguo controllo iter
			Result check = controllo.check(controlloIter, dati);
			if (check != Result.OK) {
				if (ValidazioneDati.isFilled(controlloIter.getMessaggio() ) )
					messaggi.add(controlloIter.getMessaggio());
				// Se controllo è bloccante non proseguo
				if (check == Result.FAILED_AND_BLOCKED) {
					ok = false;
					break;
				}
			}
		}

		return ok;
	}

	private AttivitaServizioChecker getController() throws Exception {
		String codAttivita = passoIter.getCodAttivita();
		String classeControllo = defaults.get(codAttivita); //this.attivitaDefault.getClasse();

		if (ValidazioneDati.strIsNull(classeControllo))
			return null;

		// recupero l'istanza già creata
		AttivitaServizioChecker controllo = instances.get(codAttivita);
		if (controllo == null) {

			Class<?> classe = Thread.currentThread().getContextClassLoader().loadClass(classeControllo);
			Object instance = classe.newInstance();

			if (!(instance instanceof AttivitaServizioChecker))
				return null;

			controllo = (AttivitaServizioChecker) instance;
			instances.put(codAttivita, controllo);
		}

		log.debug("istanziato controller attività: " + controllo);
		return controllo;
	}

	/**
	 * Esegue una classe associata ad un passo dell'iter. Tale classe implementa
	 * una serie di operazioni di default da eseguire durante l'avanzamento ad
	 * una data attività.
	 *
	 * it.iccu.sbn.ejb.vo.servizi.erogazione Attivita.java controlloDefaul
	 * boolean
	 *
	 * @param dati
	 * @param messaggi
	 * @return <ul>
	 *         <li><strong>true</strong>: se l'esecuzione del controllo è andata
	 *         a buon fine</li>
	 *         <li><strong>false</strong>:se l'esecuzione del controllo non è
	 *         andata a buon fine</li>
	 *         </ul>
	 * @throws Exception
	 *
	 *
	 */
	public ControlloAttivitaServizioResult controlloDefault(DatiControlloVO dati) throws Exception {

		AttivitaServizioChecker controllo = getController();
		if (controllo == null)
			return ControlloAttivitaServizioResult.OK;

		// Eseguo controllo
		ControlloAttivitaServizioResult check = controllo.check(dati);
		dati.setResult(check);

		return check;
	}

	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {

		AttivitaServizioChecker controllo = getController();
		if (controllo == null)
			return ControlloAttivitaServizioResult.OK;

		// Eseguo controllo
		ControlloAttivitaServizioResult check = controllo.post(dati);
		dati.setResult(check);

		return check;
	}

}
