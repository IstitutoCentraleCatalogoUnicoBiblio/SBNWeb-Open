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
package it.iccu.sbn.ejb.domain.amministrazione;


import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.AreaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.DefaultVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.GruppoVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.amministrazione.DefaultDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_config_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo_default;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="AmministrazioneGestioneDefault" description="A session bean named
 *           AmministrazioneGestioneDefault" display-name="AmministrazioneGestioneDefault"
 *           jndi-name="sbnWeb/AmministrazioneGestioneDefault" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */


public abstract class AmministrazioneGestioneDefaultBean implements SessionBean {

	private static final long serialVersionUID = 4283409517664514638L;

	private static Logger log = Logger.getLogger(AmministrazioneGestioneDefaultBean.class);

	public void ejbCreate() {
		return;
	}


	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		return;
	}

	/**
     *
     * <!-- begin-xdoclet-definition -->
     *
     * @throws ResourceNotFoundException
     * @throws ApplicationException
     * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
     * @generated
     *
     */
	public List<GruppoVO> getDefUtente(int idUtente, String idArea, Map attivita, String idBiblioteca, String idPolo) throws EJBException, RemoteException {

		Map<String, GruppoVO> elencoSezioni = new HashMap<String, GruppoVO>();
		try {
			DefaultDao dao = new DefaultDao();
			List<Tbf_bibliotecario_default> defaultUser = null;
			List<Tbf_biblioteca_default> defaultBiblioteca = null;

			if (attivita != null)
				defaultUser = dao.cercaDefaultBibliotecarioPerArea(idUtente, idArea);
			 else
				defaultBiblioteca = dao.cercaTuttiDefaultBiblioteca(idBiblioteca, idPolo);

			List<Tbf_default> defaultPerArea = dao.cercaTuttiDefaultArea(idArea, ValidazioneDati.getMapKeySet(attivita));
//			for (int i=0; i<defaultPerArea.size(); i++) {
//				if (attivita != null && defaultPerArea.get(i).getCodice_attivita() != null && !attivita.containsKey(defaultPerArea.get(i).getCodice_attivita())) {
//					defaultPerArea.remove(i);
//					i--;
//				}
//			}

			//INSERIMENTO DELLA SEZIONE:
			//
			//controllo se la sezione è inserita nell'array elencoSezioni:
			for (Tbf_default defDB : defaultPerArea) {

				Tbf_config_default config = defDB.getTbf_config_default__id_config();
				String nomeGruppo = config.getId_area_sezione().trim();
				if (elencoSezioni.containsKey(nomeGruppo))
					continue;

				//se non ho già inserito la sezione la creo nel VO:
				GruppoVO gruppo = new GruppoVO();
				gruppo.setNome(nomeGruppo);
				gruppo.setBundle("amministrazioneSistemaLabels");
				gruppo.setSeq_ordinamento(config.getSeq_ordinamento());
				elencoSezioni.put(nomeGruppo, gruppo);
			}

			List<DefaultVO> elencoDefSistema  = new ArrayList<DefaultVO>();
			if (attivita != null) {
				//Prendo i defualt di sistema per l'utente. Utilizzo come struttura dati un DefaultVO per la fruibilità dei dati.
				elencoDefSistema = getDefaultSistema(idBiblioteca, idPolo, defaultPerArea);
			}
			else {
				//Prendo i defualt di sistema per la biblioteca. Utilizzo come struttura dati un DefaultVO per la fruibilità dei dati.
				elencoDefSistema = getDefaultSistemaBiblioteca(idBiblioteca, idPolo, defaultPerArea);
			}

			List<Integer> idGruppi = new ArrayList<Integer>();
			int min = 0;

			for (int i = 0; i < defaultPerArea.size(); i++) {

				Tbf_default defDB = defaultPerArea.get(i);
				boolean devoInserire = false;

				DefaultVO def = new DefaultVO();

				//CARICAMENTO DEL DEFAULT ALL'INTERNO DEL VO:
				//**********************************************************************
				//  Caso di default di tipo CAMPO TESTO:
				//**********************************************************************
				if (defDB.getTipo().toUpperCase().equals("STRING")) {
					def.setIdDefault(defDB.getId_default());
					def.setIdGruppoDefault(0);
					def.setTipoDB("STRING");
					def.setSeq_ordinamento(defDB.getSeq_ordinamento());
					def.setNome(defDB.getId_etichetta());
					def.setBundle(defDB.getBundle());
					def.setDimensione(new Integer(defDB.getLunghezza()).toString());
					def.set_default(safeEnumValueOf(defDB.getKey()));
					String value = "";
					if (attivita != null) {
						//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
						//contenente i default del bibliotecario:
						for (int k = 0; k < defaultUser.size(); k++) {
							if (defaultUser.get(k).getId_default().getId_default() == defDB.getId_default()) {
								value = defaultUser.get(k).getValue();
								break;
							}
						}
					}
					else {
						//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
						//contenente i default della biblioteca:
						for (int k = 0; k < defaultBiblioteca.size(); k++) {
							if (defaultBiblioteca.get(k).getId_default().getId_default() == defDB.getId_default()) {
								value = defaultBiblioteca.get(k).getValue();
								break;
							}
						}
					}
					def.setSelezione(value);
					def.setTipo("String");

					//Prendo il valore di sistema e la sua provenienza:
					for (int y=0; y <elencoDefSistema.size(); y++) {
						Integer opt1 = new Integer(defDB.getId_default());
						Integer opt2 = new Integer(elencoDefSistema.get(y).getIdDefault());
						if (opt1.equals(opt2)) {
							def.setDefaultSistema(elencoDefSistema.get(y).getDefaultSistema());
							def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
							break;
						}
					}
					devoInserire = true;
				}

				//**********************************************************************
				//  Caso di default di tipo MENU A TENDINA CON SCELTA SINGOLA:
				//**********************************************************************
				if (defDB.getTipo().toUpperCase().equals("OPZIONE")) {
					//Controllo l'esistenza di un gruppo
					if (defDB.getTbf_gruppi_default() == null) {
						def.setNome(defDB.getId_etichetta());
						def.setBundle(defDB.getBundle());
						def.setIdGruppoDefault(0);
						def.set_default(safeEnumValueOf(defDB.getKey()));
					}
					//Nel caso esiste il gruppo è necessario inserire etichetta e bundle una sola volta:
					else {
						boolean trovoGruppo = false;
						def.setIdGruppoDefault(defDB.getTbf_gruppi_default().getId());
						//controllo quindi se sia stato inserito in un array (idGruppi) che tiene conto dei gruppi inseriti:
						for (int l=0; l < idGruppi.size(); l++) {
							Integer g = new Integer(defDB.getTbf_gruppi_default().getId());
							Integer t = idGruppi.get(l);
							if (g.equals(t)) {
								trovoGruppo = true;
								break;
							}
						}
						if (!trovoGruppo) {
							def.setNome(defDB.getTbf_gruppi_default().getEtichetta());
							def.setBundle(defDB.getTbf_gruppi_default().getBundle());
							idGruppi.add(new Integer(defDB.getTbf_gruppi_default().getId()));
							min = defDB.getSeq_ordinamento();
						}
						else {
							List<DefaultVO> elencoDefParz = elencoSezioni.get(defDB.getTbf_config_default__id_config().getSeq_ordinamento() - 1).getDef();
							boolean minimo = true;
							for (int ind = 0; ind < elencoDefParz.size(); ind++) {
								Integer grp1 = elencoDefParz.get(ind).getIdGruppoDefault();
								Integer grp2 = new Integer(defDB.getTbf_gruppi_default().getId());
								if (grp1.equals(grp2)) {
									DefaultVO defParz = elencoDefParz.get(ind);
									if (defParz.getSeq_ordinamento() > defDB.getSeq_ordinamento()) {
										defParz.setNome("");
										defParz.setBundle("");
									}
									if (defDB.getSeq_ordinamento() > min){
										minimo = false;
									}
									else {
										min = defDB.getSeq_ordinamento();
										minimo = true;
									}
								}
							}
							if (minimo) {
								def.setNome(defDB.getTbf_gruppi_default().getEtichetta());
								def.setBundle(defDB.getTbf_gruppi_default().getBundle());
							}
							else {
								def.setNome("");
								def.setBundle("");
							}
						}
					}
					def.setIdDefault(defDB.getId_default());
					def.setTipoDB("OPZIONI");
					def.setSeq_ordinamento(defDB.getSeq_ordinamento());
					String value = "";
					if (attivita != null) {
						//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
						//contenente i default del bibliotecario:
						for (int k = 0; k < defaultUser.size(); k++) {
							if (defaultUser.get(k).getId_default().getId_default() == defDB.getId_default()) {
								value = defaultUser.get(k).getValue();
								break;
							}
						}
					}
					else {
						//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
						//contenente i default della biblioteca:
						for (int k = 0; k < defaultBiblioteca.size(); k++) {
							if (defaultBiblioteca.get(k).getId_default().getId_default() == defDB.getId_default()) {
								value = defaultBiblioteca.get(k).getValue();
								break;
							}
						}
					}
					def.setSelezione(value);
					def.setTipo("Opzioni");

					CodiciType codice = CodiciType.fromString(defDB.getCodice_tabella_validazione());
					if (codice == null) {
						log.warn(String.format("Attenzione: Tabella codici '%s' non trovata.", defDB.getCodice_tabella_validazione()) );
						continue;
					}
					List<TB_CODICI> elencoCodici = CodiciProvider.getCodici(codice, false);
					def.setListaOpzioni(creaCombo(elencoCodici));

					//Prendo il valore di sistema e la sua provenienza:
					for (int y=0; y <elencoDefSistema.size(); y++) {
						Integer opt1 = new Integer(defDB.getId_default());
						Integer opt2 = new Integer(elencoDefSistema.get(y).getIdDefault());
						if (opt1.equals(opt2)) {
							for (int f = 0; f< elencoCodici.size(); f++) {
								if (elencoCodici.get(f).getCd_tabella().trim().equals(elencoDefSistema.get(y).getDefaultSistema())) {
									def.setDefaultSistema(elencoCodici.get(f).getDs_tabella());
									break;
								}
							}
							def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
							break;
						}
					}
					devoInserire = true;
				}

				//**********************************************************************
				//  Caso di default di tipo MENU CON SCELTA MULTIPA (ES. CHECKBOX):
				//**********************************************************************
				if (defDB.getTipo().toUpperCase().equals("CHECK")) {
					//Controllo l'esistenza di un gruppo
					//Se il gruppo è null è il caso di check unico:
					if (defDB.getTbf_gruppi_default() == null) {
						def.setNome(defDB.getId_etichetta());
						def.setBundle(defDB.getBundle());
						def.setSeq_ordinamento(defDB.getSeq_ordinamento());
						def.setIdGruppoDefault(0);
						def.set_default(safeEnumValueOf(defDB.getKey()));
						List<Tbf_default> opzioni = new ArrayList<Tbf_default>();
						opzioni.add(defDB);
						def.setListaOpzioni(creaComboDaArray(opzioni));
						String[] valore = new String[1];
						String value = "";
						if (attivita != null) {
							//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
							//contenente i default del bibliotecario:
							for (int k = 0; k < defaultUser.size(); k++) {
								if (defaultUser.get(k).getId_default().getId_default() == defDB.getId_default()) {
									value = defaultUser.get(k).getValue();
									break;
								}
							}
						}
						else {
							//Per prendere il valore di quel default faccio la corrispondenza dell'id del default con l'array
							//contenente i default della biblioteca:
							for (int k = 0; k < defaultBiblioteca.size(); k++) {
								if (defaultBiblioteca.get(k).getId_default().getId_default() == defDB.getId_default()) {
									value = defaultBiblioteca.get(k).getValue();
									break;
								}
							}
						}
						//Se il valore di default del bibliotecario è TRUE allora imposto come selezione il contenuto dell'etichetta
						//del default stesso in modo che l'oggetto della JSP visualizzi la giusta selezione:
						if (value.toUpperCase().trim().equals("TRUE")) {
				    		//Prendo il bundle del db e metto l'iniziale maiuscola
							String stringaBundle = defDB.getBundle();
				    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
							ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
							valore[0] = bundle.getString(defDB.getId_etichetta()).trim();
						}
						def.setSelezioneMulti(valore);
						def.setIdDefault(defDB.getId_default());
						def.setTipoDB("CHECK");
						def.setTipo("multiOpzioni");

						//Prendo il valore di sistema e la sua provenienza:
						for (int y=0; y <elencoDefSistema.size(); y++) {
							Integer opt1 = new Integer(defDB.getId_default());
							Integer opt2 = new Integer(elencoDefSistema.get(y).getIdDefault());
							if (opt1.equals(opt2) && elencoDefSistema.get(y).getDefaultSistema().trim().toUpperCase().equals("TRUE")) {
								String stringaBundle = defDB.getBundle();
					    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
					    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
								String[] valoreSys = new String[1];
								valoreSys[0] = bundle.getString(defDB.getId_etichetta()).trim();
					    		def.setDefaultSistemaMulti(valoreSys);
								def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
								break;
							}
						}
						devoInserire = true;
					}
					//Nel caso esiste il gruppo è necessario gestire tutti i check per quel gruppo:
					else {
						boolean trovoGruppo = false;
						//controllo quindi se sia stato inserito in un array (idGruppi) che tiene conto dei gruppi inseriti:
						for (int l=0; l < idGruppi.size(); l++) {
							Integer g = new Integer(defDB.getTbf_gruppi_default().getId());
							Integer t = idGruppi.get(l);
							if (g.equals(t)) {
								trovoGruppo = true;
								break;
							}
						}
						if (!trovoGruppo) {
							idGruppi.add(new Integer(defDB.getTbf_gruppi_default().getId()));
							def.setNome(defDB.getTbf_gruppi_default().getEtichetta());
							def.setBundle(defDB.getTbf_gruppi_default().getBundle());
							def.setSeq_ordinamento(defDB.getSeq_ordinamento());
							def.setIdDefault(defDB.getId_default());
							def.setIdGruppoDefault(defDB.getTbf_gruppi_default().getId());

							//devo catturare tutte le opzioni per quel default dall'array dei default.
							//quindi reitero su defaultPerArea e prendo tutti i default che appartengono allo stesso gruppo
							//di quello attualmente in esame:
							List<Tbf_default> opzioniCK = new ArrayList<Tbf_default>();
							for (int u=i; u < defaultPerArea.size(); u++) {
								Tbf_default defCK = defaultPerArea.get(u);
								Integer grup1 = null;
								Integer grup2 = null;
								if (defCK.getTbf_gruppi_default() != null) {
									grup1 = new Integer(defCK.getTbf_gruppi_default().getId());
								}
								if (defDB.getTbf_gruppi_default() != null) {
									grup2 = new Integer(defDB.getTbf_gruppi_default().getId());
								}
								if ((grup1 != null) && (grup2!= null) && (grup1.equals(grup2))) {
									opzioniCK.add(defaultPerArea.get(u));
								}
							}
							//ordino le opzioni in base alla sequenza ordinamento:
							opzioniCK = ordinaDefault(opzioniCK);
							//Per prendere le opzioni del default mi scorro tutte le opzioniCK e le inserisco nella proprietà
							//ListaOpzioni dell'oggetto DefaultVO:
							def.setListaOpzioni(creaComboDaArray(opzioniCK));
							//Per prendere i valori di tutte le opzioni del default di tipo check devo scorrere
							//la tabella bibliotecari in funzione di come ho ordinato le opzioni:
							List<String> value = new ArrayList<String>();
							List<String> sysValue = new ArrayList<String>();
							for (int k = 0; k < opzioniCK.size(); k++) {
								if (attivita != null) {
									for (int p = 0; p < defaultUser.size(); p++) {
										Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
										Integer opt2 = new Integer(defaultUser.get(p).getId_default().getId_default());
										//Se il valore di default del bibliotecario è TRUE allora imposto come selezione il contenuto dell'etichetta
										//del default stesso in modo che l'oggetto della JSP visualizzi la giusta selezione:
										if ( opt1.equals(opt2) && defaultUser.get(p).getValue().toUpperCase().trim().equals("TRUE")) {
								    		//Prendo il bundle del db e metto l'iniziale maiuscola
											String stringaBundle = opzioniCK.get(k).getBundle();
								    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
								    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
											value.add(bundle.getString(opzioniCK.get(k).getId_etichetta()).trim());
											break;
										}
									}
								}
								else {
									for (int p = 0; p < defaultBiblioteca.size(); p++) {
										Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
										Integer opt2 = new Integer(defaultBiblioteca.get(p).getId_default().getId_default());
										//Se il valore di default del bibliotecario è TRUE allora imposto come selezione il contenuto dell'etichetta
										//del default stesso in modo che l'oggetto della JSP visualizzi la giusta selezione:
										if ( opt1.equals(opt2) && defaultBiblioteca.get(p).getValue().toUpperCase().trim().equals("TRUE")) {
								    		//Prendo il bundle del db e metto l'iniziale maiuscola
											String stringaBundle = opzioniCK.get(k).getBundle();
								    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
								    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
											value.add(bundle.getString(opzioniCK.get(k).getId_etichetta()).trim());
											break;
										}
									}
								}
								//Stessa cosa faccio per prendere il default di sistema e la provenienza:
								for (int y=0; y <elencoDefSistema.size(); y++) {
									Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
									Integer opt2 = new Integer(elencoDefSistema.get(y).getIdDefault());
									//almaviva5_20101108
									if (!ValidazioneDati.isFilled(def.getProvenienza()) )
											def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
									if (!opzioniCK.get(k).getId_etichetta().equals("default.superiore") && opt2 != null && (opt1.equals(opt2)) && (elencoDefSistema.get(y).getDefaultSistema().toUpperCase().trim().equals("TRUE"))) {
										String stringaBundle = opzioniCK.get(k).getBundle();
							    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
							    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
										sysValue.add(bundle.getString(opzioniCK.get(k).getId_etichetta()).trim());
										def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
										break;
									}
								}
							}
							// Nel caso nessuna opzione del gruppo di CHECK sia stata selezionata dal bibliotecario o dall biblioteca
							// imposto l'opzione DEFAULT DI LIVELLO SUPERIORE per la combo.
							// Se tutte le opzioni erano a FALSE, imposto la selezione vuota.
							if (attivita != null) {
								boolean scarta = false;
								boolean sistema = true;
								for (int k = 0; k < opzioniCK.size(); k++) {
									Tbf_default defCK = opzioniCK.get(k);
									for (int p = 0; p < defaultUser.size(); p++) {
										Tbf_bibliotecario_default defUser = defaultUser.get(p);
										if (defUser.getId_default().getId_default() == defCK.getId_default() && defUser.getValue().equals("TRUE")) {
											scarta = true;
											break;
										}
										if (defUser.getId_default().getId_default() == defCK.getId_default() && defUser.getValue().equals("FALSE"))
											sistema = false;
									}
									if (scarta)
										break;
								}
								if (!scarta && sistema) {
						    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
									value.add(bundle.getString("default.superiore"));
								}
								if (!scarta && !sistema)
									value.add("");
							}
							else {
								boolean scarta = false;
								boolean sistema = true;
								for (int k = 0; k < opzioniCK.size(); k++) {
									Tbf_default defCK = opzioniCK.get(k);
									for (int p = 0; p < defaultBiblioteca.size(); p++) {
										Tbf_biblioteca_default defUser = defaultBiblioteca.get(p);
										if (defUser.getId_default().getId_default() == defCK.getId_default() && defUser.getValue().equals("TRUE")) {
											scarta = true;
											break;
										}
										if (defUser.getId_default().getId_default() == defCK.getId_default() && defUser.getValue().equals("FALSE"))
											sistema = false;
									}
									if (scarta)
										break;
								}
								if (!scarta && sistema) {
						    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
									value.add(bundle.getString("default.superiore"));
								}
								if (!scarta && !sistema)
									value.add("");
							}

							//Converto gli List in uno String[] per l'oggetto Menu con multiopzioni:
							String[] valori = new String[value.size()];
							for (int m=0; m<value.size(); m++)
								valori[m] = value.get(m);
							def.setSelezioneMulti(valori);

							valori = new String[sysValue.size()];
							for (int m=0; m<sysValue.size(); m++)
								valori[m] = sysValue.get(m);
							def.setDefaultSistemaMulti(valori);

							def.setTipoDB("CHECK");
							def.setTipo("multiOpzioni");
							devoInserire = true;
						}
						else {
							devoInserire = false;
						}
					}
				}

				//*********************************************************************************
				//  Caso di default di tipo RADIO BUTTON visualizzato con MENU CON SCELTA SINGOLA:
				//*********************************************************************************
				if (defDB.getTipo().toUpperCase().equals("RADIO")) {
					boolean trovoGruppo = false;
					//controllo se sia stato inserito in un array (idGruppi) che tiene conto dei gruppi inseriti:
					for (int l=0; l < idGruppi.size(); l++) {
						Integer g = new Integer(defDB.getTbf_gruppi_default().getId());
						Integer t = idGruppi.get(l);
						if (g.equals(t)) {
							trovoGruppo = true;
							break;
						}
					}
					if (!trovoGruppo) {
						idGruppi.add(new Integer(defDB.getTbf_gruppi_default().getId()));
						def.setNome(defDB.getTbf_gruppi_default().getEtichetta());
						def.setBundle(defDB.getTbf_gruppi_default().getBundle());
						def.setSeq_ordinamento(defDB.getSeq_ordinamento());
						def.setIdDefault(defDB.getId_default());
						def.setIdGruppoDefault(defDB.getTbf_gruppi_default().getId());

						//devo catturare tutte le opzioni per quel default dall'array dei default.
						//quindi reitero su defaultPerArea e prendo tutti i default che appartengono allo stesso gruppo
						//di quello attualmente in esame:
						List<Tbf_default> opzioniCK = new ArrayList<Tbf_default>();
						for (int u=i; u < defaultPerArea.size(); u++) {
							Tbf_default defCK = defaultPerArea.get(u);
							Integer grup1 = null;
							if (defCK.getTbf_gruppi_default() != null) {
								grup1 = defCK.getTbf_gruppi_default().getId();
							}
							Integer grup2 = null;
							if (defDB.getTbf_gruppi_default() != null)
								grup2 = defDB.getTbf_gruppi_default().getId();

							if ((grup1 != null) && (grup2 != null) && (grup1.equals(grup2))) {
								opzioniCK.add(defaultPerArea.get(u));
							}
						}
						//ordino le opzioni in base alla sequenza ordinamento:
						opzioniCK = ordinaDefault(opzioniCK);
						//Per prendere le opzioni del default mi scorro tutte le opzioniCK e le inserisco nella proprietà
						//ListaOpzioni dell'oggetto DefaultVO:
						def.setListaOpzioni(creaComboDaArray(opzioniCK));
						//Per prendere i valori di tutte le opzioni del default di tipo check devo scorrere
						//la tabella bibliotecari in funzione di come ho ordinato le opzioni:
						String value = "";
						for (int k = 0; k < opzioniCK.size(); k++) {
							boolean sysTrovato = false;
							if (attivita != null) {
								for (int p = 0; p < defaultUser.size(); p++) {
									Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
									Integer opt2 = new Integer(defaultUser.get(p).getId_default().getId_default());
									//Se il valore di default del bibliotecario è TRUE allora imposto come selezione il contenuto dell'etichetta
									//del default stesso in modo che l'oggetto della JSP visualizzi la giusta selezione:
									if ( opt1.equals(opt2) && defaultUser.get(p).getValue().toUpperCase().trim().equals("TRUE")) {
							    		//Prendo il bundle del db e metto l'iniziale maiuscola
										String stringaBundle = opzioniCK.get(k).getBundle();
							    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
							    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
										value = bundle.getString(opzioniCK.get(k).getId_etichetta()).trim();
										break;
									}
								}
							}
							else {
								for (int p = 0; p < defaultBiblioteca.size(); p++) {
									Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
									Integer opt2 = new Integer(defaultBiblioteca.get(p).getId_default().getId_default());
									//Se il valore di default del bibliotecario è TRUE allora imposto come selezione il contenuto dell'etichetta
									//del default stesso in modo che l'oggetto della JSP visualizzi la giusta selezione:
									if ( opt1.equals(opt2) && defaultBiblioteca.get(p).getValue().toUpperCase().trim().equals("TRUE")) {
							    		//Prendo il bundle del db e metto l'iniziale maiuscola
										String stringaBundle = opzioniCK.get(k).getBundle();
							    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
							    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
										value = bundle.getString(opzioniCK.get(k).getId_etichetta()).trim();
										break;
									}
								}
							}
							//Stessa cosa faccio per prendere il default di sistema e la sua provenienza:
							if (!sysTrovato) {
								for (int y=0; y <elencoDefSistema.size(); y++) {
									Integer opt1 = new Integer(opzioniCK.get(k).getId_default());
									Integer opt2 = new Integer(elencoDefSistema.get(y).getIdDefault());
									if ( (opt1.equals(opt2)) && (elencoDefSistema.get(y).getDefaultSistema().toUpperCase().trim().equals("TRUE"))) {
										String stringaBundle = opzioniCK.get(k).getBundle();
							    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
							    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
										def.setDefaultSistema(bundle.getString(opzioniCK.get(k).getId_etichetta().trim()));
										def.setProvenienza(elencoDefSistema.get(y).getProvenienza());
										sysTrovato = true;
										break;
									}
								}
							}
						}
						def.setSelezione(value);
						def.setTipoDB("RADIO");
						def.setTipo("Opzioni");
						devoInserire = true;
					}
					else {
						devoInserire = false;
					}
				}

				//**********************************************************************
				//  INSERIMENTO DEL DEFAULT ALL'INTERNO DEL VO DELLE SEZIONI:
				//**********************************************************************
				if (devoInserire)  {
					String nomeGruppo = defDB.getTbf_config_default__id_config().getId_area_sezione().trim();
					GruppoVO gruppoVO = elencoSezioni.get(nomeGruppo);
					if (gruppoVO == null) {
						log.error("Errore caricamento default '" + defDB.getKey() + "'");
						continue;
					}

					List<DefaultVO> elencoDef = gruppoVO.getDef();
					elencoDef.add(def);
					gruppoVO.setDef(ordinaDefaultVO(elencoDef));
				}
			}	// end for

			return ordinaGruppoVO(new ArrayList<GruppoVO>(elencoSezioni.values()));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private ConstantDefault safeEnumValueOf(String key) {
		try {
			return ConstantDefault.valueOf(key);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * Metodo di ordinamento di un List di Tbf_default in base alla sequenza di ordinamento.
	 * L'ordinamento viene effettuato con algoritmo di selection sort.
	 * @param a L'List di Tbf_default che verrà ordinato.
	 * @return L'List di Tbf_default ordinato.
	 */
	private static List<Tbf_default> ordinaDefault(List<Tbf_default> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (a.get(j).getSeq_ordinamento() < a.get(jmin).getSeq_ordinamento())
				jmin = j;
			}
			// scambia gli elementi i e jmin
			Tbf_default aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		return a;
    }

	/**
	 * Metodo di ordinamento di un List di DefaultVO in base alla sequenza ordinamento.
	 * Una volta effettuato l'ordinamento, viene impostato anche l'indice per la visualizzazione nella JSP.
	 * L'ordinamento viene effettuato con algoritmo di selection sort.
	 * @param a L'List di DefaultVO che verrà ordinato.
	 * @return L'List di DefaultVO ordinato.
	 */
	private static List<DefaultVO> ordinaDefaultVO(List<DefaultVO> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (new Integer(a.get(j).getSeq_ordinamento()) < new Integer(a.get(jmin).getSeq_ordinamento()))
				jmin = j;
			}
			// scambia gli elementi i e jmin
			DefaultVO aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		//Impostazione dell'indice di visualizzazione per ogni default:
		for (int j=0; j < a.size(); j++) {
			a.get(j).setIndice("" + j);
		}
		return a;
    }

	/**
	 * Metodo di ordinamento di un List di GruppoVO in base alla sequenza ordinamento.
	 * Una volta effettuato l'ordinamento, viene impostato anche l'indice per la visualizzazione nella JSP.
	 * L'ordinamento viene effettuato con algoritmo di selection sort.
	 * @param a L'List di GruppoVO che verrà ordinato.
	 * @return L'List di GruppoVO ordinato.
	 */
	private static List<GruppoVO> ordinaGruppoVO(List<GruppoVO> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (new Integer(a.get(j).getSeq_ordinamento()) < new Integer(a.get(jmin).getSeq_ordinamento()))
				jmin = j;
			}
			// scambia gli elementi i e jmin
			GruppoVO aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		//Impostazione dell'indice di visualizzazione per ogni default:
		for (int j=0; j < a.size(); j++) {
			a.get(j).setIndice("" + j);
		}
		return a;
    }

	/**
	 * Metodo di ordinamento di un List di AreaVO in base alla sequenza ordinamento.
	 * Una volta effettuato l'ordinamento, viene impostato anche l'indice per la visualizzazione nella JSP.
	 * L'ordinamento viene effettuato con algoritmo di selection sort.
	 * @param a L'List di AreaVO che verrà ordinato.
	 * @return L'List di AreaVO ordinato.
	 */
	private static List<AreaVO> ordinaAreaVO(List<AreaVO> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (new Integer(a.get(j).getSequenzaOrdinamento()) < new Integer(a.get(jmin).getSequenzaOrdinamento()))
				jmin = j;
			}
			// scambia gli elementi i e jmin
			AreaVO aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		return a;
    }

    private List<ComboVO> creaCombo(List<TB_CODICI> opzioni) {
		ComboVO modello = new ComboVO();
		List<ComboVO> lista= new ArrayList<ComboVO>();
    	for ( int j=0;j<opzioni.size();j++){
    		modello=new ComboVO();
    		String codice = opzioni.get(j).getCd_tabella().trim();
    		String descrizione = opzioni.get(j).getDs_tabella().trim();
    		modello.setDescrizione(codice + " " + descrizione);
	    	modello.setCodice(codice);
    		lista.add(modello);
    	}
    	return lista;
    }

    private List<ComboVO> creaComboDaArray(List<Tbf_default> opzioni) {
		ComboVO modello = new ComboVO();
		List<ComboVO> lista= new ArrayList<ComboVO>();
		modello.setDescrizione("");
		modello.setCodice("");
		lista.add(modello);
    	for ( int j=0;j<opzioni.size();j++){
    		modello=new ComboVO();
    		//Prendo il bundle del db e metto l'iniziale maiuscola
    		String stringaBundle = opzioni.get(j).getBundle();
    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
			ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
    		modello.setDescrizione(bundle.getString(opzioni.get(j).getId_etichetta()).trim());
    		modello.setCodice(bundle.getString(opzioni.get(j).getId_etichetta()).trim());
    		lista.add(modello);
    	}
    	return lista;
    }

    public Map<String, String> getTuttiDefaultUtente(String idBiblioteca, String idPolo, Map attivita) throws RemoteException, DaoManagerException{

    	try {
    		Map<String, String> output = new HashMap<String, String>();
	    	DefaultDao defDao = new DefaultDao();
			List<Tbf_default> defaultPerAttivita = defDao.cercaTuttiDefaultAttivita(attivita.keySet());
			List<Tbf_bibliotecario_default> defaultBibliotecario = defDao.cercaTuttiDefaultBibliotecario();
			List<Tbf_biblioteca_default> defaultBiblioteca = defDao.cercaTuttiDefaultBiblioteca(idBiblioteca, idPolo);
			List<Tbf_polo_default> defaultPolo = defDao.cercaTuttiDefaultPolo(idPolo);
			for (int i=0; i<defaultPerAttivita.size(); i++) {
				Integer idDef = new Integer(defaultPerAttivita.get(i).getId_default());
				boolean inserito = false;
				//scorro i default di bibliotecario:
				for (int j=0; j<defaultBibliotecario.size(); j++) {
					if (idDef.equals(new Integer(defaultBibliotecario.get(j).getId_default().getId_default()))) {
						output.put(defaultPerAttivita.get(i).getKey(), defaultBibliotecario.get(j).getValue());
						inserito = true;
						break;
					}
				}
				//se il default non è inserito per il bibliotecario allora si ricerca in quelli della biblioteca:
				if (!inserito) {
					for (int k=0; k<defaultBiblioteca.size(); k++) {
						if (idDef.equals(new Integer(defaultBiblioteca.get(k).getId_default().getId_default()))) {
							output.put(defaultPerAttivita.get(i).getKey(), defaultBiblioteca.get(k).getValue());
							inserito = true;
							break;
						}
					}
				}
				//se il default non è inserito per il bibliotecario allora si ricerca in quelli del polo:
				if (!inserito) {
					for (int l=0; l<defaultPolo.size(); l++) {
						if (idDef.equals(new Integer(defaultPolo.get(l).getId_default().getId_default()))) {
							output.put(defaultPerAttivita.get(i).getKey(), defaultPolo.get(l).getValue());
							inserito = true;
							break;
						}
					}
				}
				//se il default non è inserito per il bibliotecario allora si ricerca in quelli cablati nel codice:
				if (!inserito) {
					String chiave = defaultPerAttivita.get(i).getKey();
					try {
						output.put(chiave, ConstantDefault.valueOf(chiave).getDefault().toString());
						inserito = true;
					}
					catch(Exception e) {
						//e.printStackTrace();
						log.error("Il valore di default del campo '" + chiave + "' non esiste per nessuna istanza!");
						inserito = false;
					}
				}
			}
			return output;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    private List<DefaultVO> getDefaultSistema(String idBiblioteca, String idPolo, List<Tbf_default> defaultPerArea) throws DaoManagerException {
    	List<DefaultVO> output = new ArrayList<DefaultVO>();
    	DefaultDao defDao = new DefaultDao();
		List<Tbf_biblioteca_default> defaultBiblioteca = defDao.cercaTuttiDefaultBiblioteca(idBiblioteca, idPolo);
		List<Tbf_polo_default> defaultPolo = defDao.cercaTuttiDefaultPolo(idPolo);
		for (int i=0; i<defaultPerArea.size(); i++) {
			if (!defaultPerArea.get(i).getId_etichetta().equals("default.superiore")) {
				Integer idDef = new Integer(defaultPerArea.get(i).getId_default());
				boolean inserito = false;
				DefaultVO def = new DefaultVO();
				//scorro i default di biblioteca:
				for (int k=0; k<defaultBiblioteca.size(); k++) {
					if (idDef.equals(new Integer(defaultBiblioteca.get(k).getId_default().getId_default()))) {
						def.setIdDefault(idDef);
						def.setDefaultSistema(defaultBiblioteca.get(k).getValue());
						def.setProvenienza("BIBLIOTECA");
						inserito = true;
						break;
					}
				}
				//se il default non è inserito per il bibliotecario allora si ricerca in quelli del polo:
				if (!inserito) {
					for (int l=0; l<defaultPolo.size(); l++) {
						if (idDef.equals(new Integer(defaultPolo.get(l).getId_default().getId_default()))) {
							def.setIdDefault(idDef);
							def.setDefaultSistema(defaultPolo.get(l).getValue());
							def.setProvenienza("POLO");
							inserito = true;
							break;
						}
					}
				}
				//se il default non è inserito sul database, impostiamo quello di cablato nel codice:
				if (!inserito) {
					String chiave = defaultPerArea.get(i).getKey();
					def.setIdDefault(idDef);
					try {
						def.setDefaultSistema(ConstantDefault.valueOf(chiave).getDefault().toString());
					}
					catch (Exception e) {
						log.error("Il valore di default del campo '" + chiave + "' non esiste per nessuna istanza!");
						//e.printStackTrace();
						inserito = false;
					}
					def.setProvenienza("SISTEMA");
					inserito = true;
				}
				output.add(def);
			}
		}
		return output;
    }

    private List<DefaultVO> getDefaultSistemaBiblioteca(String idBiblioteca, String idPolo, List<Tbf_default> defaultPerArea) throws DaoManagerException {
    	List<DefaultVO> output = new ArrayList<DefaultVO>();
    	DefaultDao defDao = new DefaultDao();
		List<Tbf_polo_default> defaultPolo = defDao.cercaTuttiDefaultPolo(idPolo);
		for (int i=0; i<defaultPerArea.size(); i++) {
			Integer idDef = new Integer(defaultPerArea.get(i).getId_default());
			boolean inserito = false;
			DefaultVO def = new DefaultVO();
			//se il default non è inserito per la biblioteca allora si ricerca in quelli del polo:
			for (int l=0; l<defaultPolo.size(); l++) {
				if (idDef.equals(new Integer(defaultPolo.get(l).getId_default().getId_default()))) {
					def.setIdDefault(idDef);
					def.setDefaultSistema(defaultPolo.get(l).getValue());
					def.setProvenienza("POLO");
					inserito = true;
					break;
				}
			}
			//se il default non è inserito sul database, impostiamo quello di cablato nel codice:
			if (!inserito) {
				String chiave = defaultPerArea.get(i).getKey();
				def.setIdDefault(idDef);
				try {
					def.setDefaultSistema(ConstantDefault.valueOf(chiave).getDefault().toString());
				}
				catch (Exception e) {
					log.error("Il valore di default del campo '" + chiave + "' non esiste per nessuna istanza!");
					//e.printStackTrace();
					inserito = false;
				}
				def.setProvenienza("SISTEMA");
				inserito = true;
			}
			output.add(def);
		}
		return output;
    }

    // Il metodo inserisce i default per l'utente idUtente nel caso i campi idBiblioteca e idPolo siano "" ;
    // Il metodo inserisce i defalut per la biblioteca nel caso i campi idBilioteca e idPolo contengano i codici
    // relativi a biblioteca e polo
    public boolean setDefaultUtente(int idUtente, List<GruppoVO> campi, String idBiblioteca, String idPolo) throws RemoteException {

    	try {
	    	//Struttura dati da inviare al dao con elencati tutti i valori di default:
    		List<DefaultVO> elencoDB = new ArrayList<DefaultVO>();
			DefaultDao defDao = new DefaultDao();

    		for (int i = 0; i<campi.size(); i++) {
	    		List<DefaultVO> elencoDef = campi.get(i).getDef();
	    		for (DefaultVO def : elencoDef) {
//	    			*******************************************************************************
//	    			* Caso di default di tipo CAMPO TESTO (STRING) e                              *
//	    			* caso di default di tipo MENU A TENDINA CON SCELTA SINGOLA (OPZIONE)         *
//	    			*******************************************************************************
	    			if ((def.getTipoDB().equals("STRING")) || (def.getTipoDB().equals("OPZIONI"))) {
	    				elencoDB.add(def);
	    			}
//	    			*******************************************************************************
//	    			* Caso di default di tipo MENU A TENDINA CON SCELTA MULTIPLA (CHECK)          *
//	    			*******************************************************************************
	    			else if (def.getTipoDB().equals("CHECK")) {
	    				//Caso di check unico:
	    				Integer id1 = new Integer(def.getIdGruppoDefault());
	    				final Integer id2 = new Integer(0);
	    				if (id1.equals(id2)) {
	    					elencoDB.add(def);
	    				}
	    				else {
	    					int idGruppoDefault = def.getIdGruppoDefault();
	    					//devo conoscere tutti gli id dei default di quel gruppo
	    					List<Tbf_default> elencoDefDB = defDao.cercaDefaultPerGruppo(idGruppoDefault);
	    					for (int k = 0; k < elencoDefDB.size(); k++) {
	    						DefaultVO defDB = def.copy();
	    						defDB.setIdDefault(elencoDefDB.get(k).getId_default());
								String stringaBundle = elencoDefDB.get(k).getBundle();
					    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
					    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
								String valoreDef = bundle.getString(elencoDefDB.get(k).getId_etichetta()).trim();
								boolean inserito = false;
								for (int l = 0 ; l<def.getSelezioneMulti().length; l++) {
									if (valoreDef.equals(def.getSelezioneMulti()[l])) {
										defDB.setSelezione("TRUE");
										inserito = true;
										break;
									}
								}
								if (!inserito)
									defDB.setSelezione("FALSE");
	    						elencoDB.add(defDB);
	    					}
	    				}
	    			}
//	    			*******************************************************************************
//	    			* Caso di default di tipo MENU A TENDINA CON SCELTA SINGOLA (RADIO)           *
//	    			*******************************************************************************
	    			else if (def.getTipoDB().equals("RADIO")) {
    					int idGruppoDefault = def.getIdGruppoDefault();
    					//devo conoscere tutti gli id dei default di quel gruppo
    					List<Tbf_default> elencoDefDB = defDao.cercaDefaultPerGruppo(idGruppoDefault);
    					for (int k = 0; k < elencoDefDB.size(); k++) {
    						DefaultVO defDB = def.copy();
    						defDB.setIdDefault(elencoDefDB.get(k).getId_default());
							String stringaBundle = elencoDefDB.get(k).getBundle();
				    		stringaBundle = stringaBundle.substring(0,1).toUpperCase() + stringaBundle.substring(1);
				    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources." + stringaBundle);
							String valoreDef = bundle.getString(elencoDefDB.get(k).getId_etichetta()).trim();
							if (valoreDef.equals(def.getSelezione()))
								defDB.setSelezione("TRUE");
							else
								defDB.setSelezione("FALSE");
    						elencoDB.add(defDB);
    					}
    				}

	    		}
	    	}

    		//Invio al Dao l'elenco di default
    		//
    		boolean check = false;
    		if (idPolo.equals("") && idBiblioteca.equals("")) {
				check = defDao.inserisciDefaultUtente(elencoDB, idUtente);

			} else
    			check = defDao.inserisciDefaultBiblioteca(elencoDB, idBiblioteca, idPolo);

    		defDao.clearCache("default");
    		return check;

    	}
    	catch (Exception e) {
    		return false;
    	}
    }

    // Il metodo restituisce l'elenco delle Aree per l'utente nel carico sia riempita la mappa attivita con i permessi utente,
	// altrimenti restituisce tutte le aree.
    public List<AreaVO> getAreeUtente(Map attivita) throws RemoteException {
		List<AreaVO> output = new ArrayList<AreaVO>();
    	try {
    		DefaultDao defDAO = new DefaultDao();
    		List<Tbf_config_default> listaAree = defDAO.cercaAreeUtente(attivita);
    		for (int i = 0; i < listaAree.size(); i++) {
    			Tbf_config_default areaDB = listaAree.get(i);
    			AreaVO area = new AreaVO();
    			area.setIdAreaSezione(areaDB.getId_area_sezione().trim());
    			area.setSequenzaOrdinamento(areaDB.getSeq_ordinamento());
	    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
	    		area.setDescrizione(bundle.getString(areaDB.getId_area_sezione().trim()));
    			output.add(area);
    		}
    		output = ordinaAreaVO(output);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return output;
    }

}
