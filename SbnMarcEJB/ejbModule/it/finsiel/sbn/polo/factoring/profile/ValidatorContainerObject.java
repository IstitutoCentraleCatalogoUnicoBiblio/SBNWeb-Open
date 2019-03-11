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
package it.finsiel.sbn.polo.factoring.profile;

import it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_sem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Classe ValidatorContainerObject
 * <p>
 * Raccoglie informazioni tipologie comuni per Polo/Utente
 * utile per memorizzare informazioni su abilitazioni
 * </p>
 * @author
 */
public abstract class ValidatorContainerObject implements Serializable {

	private static final long serialVersionUID = 2698566356392527684L;

	private static Logger log = Logger.getLogger("iccu.amministrazione.validator.ValidatorContainerObject");

	// Elenco parametri da tavola Tbf_parametro
	protected List elenco_parametri = new ArrayList() ;
	// Elenco parametri materiale da tavola Tbf_par_mat
	protected List elenco_parametri_materiale = new ArrayList() ;
    // Elenco parametri authority da tavola Tbf_par_auth
	protected List elenco_parametri_authority = new ArrayList() ;
	// Elenco parametri sem da tavola Tb_par_sem
	protected List elenco_parametri_sem = new ArrayList() ;

//	 Elenco parametri sem da tavola Tb_par_sem
	protected HashMap hash_elenco_parametri_sem = new HashMap() ;

	// Elenco codici attivita abilitate da tavola Tb_tp_attivita
	protected List elenco_attivita  = new ArrayList() ;

	protected HashMap elenco_attivita_figli = new HashMap();
    protected TreeMap map_parametri_materiale = new TreeMap() ;
    protected TreeMap map_parametri_authority = new TreeMap() ;
    protected TreeMap map_attivita = new TreeMap() ;



    /**
	 * Returns the elenco_parametri.
	 * @return List
	 */
	public List getElenco_parametri() {
		return elenco_parametri;
	}
	/**
	 * Sets the elenco_parametri.
	 * @param elenco_parametri The elenco_parametri to set
	 */
	public void setElenco_parametri(List elenco_parametri) {
		this.elenco_parametri = elenco_parametri;
	}
	/**
	 * Returns the elenco_parametri_materiale.
	 * @return List
	 */
	public List getElenco_parametri_materiale() {
		return elenco_parametri_materiale;
	}
	/**
	 * Sets the elenco_parametri_materiale.
	 * @param elenco_parametri_materiale The elenco_parametri_materiale to set
	 */
	public void setElenco_parametri_materiale(List elenco_parametri_materiale) {
		this.elenco_parametri_materiale = elenco_parametri_materiale;
		this.initMap_parametri_materiale();
	}
	/**
	 * Returns the elenco_parametri_authority.
	 * @return List
	 */
	public List getElenco_parametri_authority() {
		return elenco_parametri_authority;
	}
	/**
	 * Sets the elenco_parametri_authority.
	 * @param elenco_parametri_authority The elenco_parametri_authority to set
	 */
	public void setElenco_parametri_authority(List elenco_parametri_authority) {
		this.elenco_parametri_authority = elenco_parametri_authority;
		this.initMap_parametri_authority();
	}
	/**
	 * Returns the elenco_parametri_sem.
	 * @return List
	 */
	public List getElenco_parametri_sem() {
		return elenco_parametri_sem;
	}
	/**
	 * Sets the elenco_parametri_sem.
	 * @param elenco_parametri_sem The elenco_parametri_sem to set
	 */
	public void setElenco_parametri_sem(List elenco_parametri_sem) {
		this.elenco_parametri_sem = elenco_parametri_sem;
		for(int index = 0; index< this.elenco_parametri_sem.size(); index++)
		{
			Tbf_par_sem par_sem = (Tbf_par_sem) this.elenco_parametri_sem.get(index);
			this.hash_elenco_parametri_sem.put(par_sem.getTp_tabella_codici().trim()+par_sem.getCd_tabella_codici().trim(),par_sem);
		}
	}
	public boolean isParametriSemantica(String tp_tabella_codici, String cd_tabella_codici)
	{
		String key = tp_tabella_codici.trim() + cd_tabella_codici.trim();
		if(this.hash_elenco_parametri_sem.containsKey(key))	{
			Tbf_par_sem sem = (Tbf_par_sem) this.hash_elenco_parametri_sem.get(key);
			return (sem.getSololocale() == 'S');
		}
		return false;

		//return !this.hash_elenco_parametri_sem.containsKey(tp_tabella_codici.trim()+cd_tabella_codici.trim());
	}
    /**
	 * Returns the elenco_attivita.
	 * @return List
	 */
	public List getElenco_attivita() {
		return elenco_attivita;
	}
	/**
	 * Sets the elenco_attivita.
	 * @param elenco_attivita The elenco_attivita to set
	 */
	public void setElenco_attivita(List elenco_attivita) {
		this.elenco_attivita = elenco_attivita;
		this.setAttivitaFigli();
		this.initMap_attivita();
	}


	protected void setAttivitaFigli()
	{
		// Vettore con relazioni da considerare
		List new_elenco_relazioni_attivita = new ArrayList() ;



		int conta = 0 ;
		for (int ind_attivita = 0 ; ind_attivita  < elenco_attivita.size() ; ind_attivita++)
		{
			Tbf_attivita padre = (Tbf_attivita)elenco_attivita.get(ind_attivita) ;

			if(padre.getCd_funzione_parent()== null)
			{
				for (int ind_relaz = 0 ; ind_relaz < elenco_attivita.size() ; ind_relaz++)
				{
					Tbf_attivita figlio = (Tbf_attivita)elenco_attivita.get(ind_relaz) ;
					if (figlio.getCd_funzione_parent()!= null && figlio.getCd_funzione_parent().trim().equals(padre.getCd_attivita().trim()))
					{
							new_elenco_relazioni_attivita.add(figlio) ;
					}
				}
				elenco_attivita_figli.put(padre.getCd_attivita().trim(), new_elenco_relazioni_attivita);
				new_elenco_relazioni_attivita = new ArrayList();
			}
		}
	}

	public List getAttivitaFigli(String codice_attivita)
	{
//		List elenco_attivita_figli = new ArrayList();
		return (List)elenco_attivita_figli.get(codice_attivita) ;
	}

	private void initMap_parametri_materiale() {
		map_parametri_materiale = new TreeMap();
		for (int i = 0; i < elenco_parametri_materiale.size(); i++) {
			Tbf_par_mat par = (Tbf_par_mat) elenco_parametri_materiale.get(i);
			map_parametri_materiale.put(String.valueOf(par.getCd_par_mat()),par);
		}
	}

	public Tbf_par_mat get_parametri_materiale(String cd_par_mat)
	{
		return (Tbf_par_mat)map_parametri_materiale.get(cd_par_mat);
	}

	public Tbf_par_auth get_parametri_authority(String cd_par_auth)
	{
		return (Tbf_par_auth)map_parametri_authority.get(cd_par_auth);
	}

	private void initMap_parametri_authority() {
		map_parametri_authority = new TreeMap();
		for (int i = 0; i < elenco_parametri_authority.size(); i++) {
			Tbf_par_auth par = (Tbf_par_auth) elenco_parametri_authority.get(i);
			map_parametri_authority.put(par.getCd_par_auth(), par);
		}
	}

	public Tbf_attivita get_attivita(String cd_attivita) {
		return (Tbf_attivita)map_attivita.get(""+cd_attivita);
	}

	private void initMap_attivita() {
		map_attivita = new TreeMap();
		for (int i = 0; i < elenco_attivita.size(); i++) {
			Tbf_attivita att = (Tbf_attivita) elenco_attivita.get(i);
			map_attivita.put("" + att.getCd_attivita(), att);
		}
	}


    // TreeMap parametri materiale ordinati per tipo_materiale
    // TreeMap parametri autority ordinati per tipo_autority
    /** TreeMap attivita ordinate per id_tipo_attivita */
//
	// Elenco codici attività in relazione fra di loro
//	protected List elenco_relazioni_attivita = new ArrayList() ;
	// Elenco totale biblioteche
//	protected List elenco_biblioteche = new ArrayList() ;

	/**
	 * Pulisce e compatta tutti gli elenchi dati
	 * <br>
	 * Permette il merge complessivo per le abilitazioni
	 *
	 * @param Riceve elenco informazioni (utile per ricostruire le relazioni)
	 *
	 */
//	public void mergeAll(List elenco_tutti_att_att)
//	{
//		elenco_parametri = MergeParametri(elenco_parametri) ;
//
//		elenco_parametri_materiale = MergeParametriMateriale(elenco_parametri_materiale) ;
//        initMap_parametri_materiale();
//
//		elenco_parametri_authority = MergeParametriAuthority(elenco_parametri_authority) ;
//        initMap_parametri_authority();
//
//		elenco_parametri_sem = MergeParametriSem(elenco_parametri_sem) ;
//
//		elenco_attivita = MergeAttivita(elenco_attivita) ;
//        initMap_attivita();
//
//		elenco_relazioni_attivita = ComposeRelazioniAttivita(elenco_tutti_att_att,elenco_attivita) ;
//
//
//	}
	/**
	 * Returns the elenco_relazioni_attivita.
	 * @return List
	 */
//	public List getElenco_relazioni_attivita() {
//		return elenco_relazioni_attivita;
//	}
	/**
	 * Sets the elenco_relazioni_attivita.
	 * @param elenco_relazioni_attivita The elenco_relazioni_attivita to set
	 */
//	public void setElenco_relazioni_attivita(List elenco_relazioni_attivita) {
//		this.elenco_relazioni_attivita = elenco_relazioni_attivita;
//	}

	/**
	 * @return
	 */
//	public List getElenco_biblioteche() {
//		return elenco_biblioteche;
//	}
	/**
	 * @param List
	 */
//	public void setElenco_biblioteche(List List) {
//		elenco_biblioteche = List;
//	}

}
