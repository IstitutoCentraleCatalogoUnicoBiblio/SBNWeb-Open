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
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe ValidatorContainer
 * <p>
 * Abstract con operazione di merge ABILITAZIONI
 * </p>
 * @author
 *
 */
public abstract class ValidatorContainer implements Serializable {

	private static final long serialVersionUID = 5639860507342504042L;


	/**
	 * Pulisce e compatta l'elenco ATTIVITA (PER GRUPPI & UTENTI)
	 * <br>
	 * La presenza di gruppi e sottogruppi potrebbe avere introdotto più permessi
	 * E' necessario fare il merge
	 * Questa volta devo raggruppare le informazioni per attivita (si/no)
	 * Torno un vettore con  i soli codici attività
	 */
	protected List MergeAttivita(List elenco_attivita_gruppo)
	{

		List nuovo_elenco_attivita = new ArrayList() ;


		int conta = 0 ;
		for (int ind_attivita = 0 ; ind_attivita < elenco_attivita_gruppo.size() ; ind_attivita++)
		{
			Tbf_attivita current = (Tbf_attivita)elenco_attivita_gruppo.get(ind_attivita) ;

			boolean found = false ;
			for (int ind_appoggio = 0 ; ind_appoggio < nuovo_elenco_attivita.size() ; ind_appoggio++)
			{
				Tbf_attivita presente = (Tbf_attivita)nuovo_elenco_attivita.get(ind_appoggio) ;

				if (presente.getCd_attivita().equals(current.getCd_attivita()))
				{
					found = true ;
					break ;
				}
			}

			if (found == false)
				nuovo_elenco_attivita.add(current);
		}
//log.debug("DOPO GRUPPO TOTALE ATTIVITA:"+v_appoggio.size()+":");

		return (nuovo_elenco_attivita) ;
	}









	/**
	 * Pulisce e compatta l'elenco dei PARAMETRI
	 * <br>
	 * La presenza di gruppi e sottogruppi potrebbe avere introdotto più permessi
	 * E' necessario fare il merge
	 * I parametri vanno tutti compattati in un unico RECORD
	 */
	protected List MergeParametri(List elenco_parametri)
	{
		List nuovo_elenco_parametri = new ArrayList() ;

		Tbf_parametro unione = null ;
		for (int ind_dummy =0 ; ind_dummy < elenco_parametri.size() ; ind_dummy++)
		{
			Tbf_parametro curr_dummy = (Tbf_parametro)elenco_parametri.get(ind_dummy) ;

			if (unione == null)
			{
				unione = new Tbf_parametro() ;
				unione.setId_parametro(curr_dummy.getId_parametro()) ;
				//unione.setId_gruppo(curr_dummy.getID_gruppo()) ;
				unione.setCd_livello(curr_dummy.getCd_livello()) ;
				unione.setFl_spogli(curr_dummy.getFl_spogli()) ;
				unione.setTp_ret_doc(curr_dummy.getTp_ret_doc()) ;
				unione.setTp_all_pref(curr_dummy.getTp_all_pref()) ;
				unione.setCd_liv_ade(curr_dummy.getCd_liv_ade()) ;
				unione.setFl_aut_superflui(curr_dummy.getFl_aut_superflui()) ;
			}
			else
			{
				if ((curr_dummy.getCd_livello().compareTo(unione.getCd_livello())) >0)
					unione.setCd_livello(curr_dummy.getCd_livello()) ;

				if ((curr_dummy.getFl_spogli() == 'S') ||
					(curr_dummy.getFl_spogli() == 'y'))
					unione.setFl_spogli(curr_dummy.getFl_spogli()) ;

				if ((curr_dummy.getFl_aut_superflui() == 'S') ||
					(curr_dummy.getFl_aut_superflui() == 'y'))
					unione.setFl_aut_superflui(curr_dummy.getFl_aut_superflui()) ;

				if (String.valueOf(curr_dummy.getCd_liv_ade()).compareTo(String.valueOf(unione.getCd_liv_ade())) <0)
					unione.setCd_liv_ade(curr_dummy.getCd_liv_ade()) ;

				if (curr_dummy.getTp_all_pref().compareTo(unione.getTp_all_pref()) <0)
					unione.setTp_all_pref(curr_dummy.getTp_all_pref()) ;

				if (curr_dummy.getTp_ret_doc().compareTo(unione.getTp_ret_doc()) <0)
					unione.setTp_ret_doc(curr_dummy.getTp_ret_doc()) ;
			}
		}

        if (unione != null)
		nuovo_elenco_parametri.add(unione) ;

		elenco_parametri = nuovo_elenco_parametri ;

		return (elenco_parametri) ;

	}

	/**
	 * Pulisce e compatta l'elenco PARAMETRI MATERIALE
	 * <br>
	 * La presenza di gruppi e sottogruppi potrebbe avere introdotto più permessi
	 * E' necessario fare il merge
	 * Questa volta devo raggruppare le informazioni per tipo materiale
	 */
	protected List MergeParametriMateriale(List elenco_parametri_materiale)
	{
		List nuovo_elenco_parametri_materiale = new ArrayList() ;

		// Mi creo un elenco tipi materiale
		// Non li prendo dal decodificatore VOLUTAMENTE - perdonatemi
		List elenco_tipi_materiale = new ArrayList();
		elenco_tipi_materiale.add("E");
		elenco_tipi_materiale.add("M");
		elenco_tipi_materiale.add("C");
		elenco_tipi_materiale.add("G");
		elenco_tipi_materiale.add("U");

		// for sui tipi materiale !!!
		for  (int ind_tipo = 0 ; ind_tipo < elenco_tipi_materiale.size(); ind_tipo++)
		{
			Tbf_par_mat unione = null ;
			//String tipo_materiale = (String) elenco_tipi_materiale.elementAt(ind_tipo);
            String tipo_materiale = (String) elenco_tipi_materiale.get(ind_tipo);
			for (int ind_dummy =0 ; ind_dummy < elenco_parametri_materiale.size() ; ind_dummy++)
			{
				Tbf_par_mat current = (Tbf_par_mat)elenco_parametri_materiale.get(ind_dummy) ;


				if (String.valueOf(current.getCd_par_mat()).equalsIgnoreCase(tipo_materiale)==true)
				{

					if (unione == null)
			  		{
						unione = new Tbf_par_mat() ;
						// Nuovo dato unione dei precedenti
						unione.setId_parametro(current.getId_parametro()) ;
						unione.setCd_par_mat(current.getCd_par_mat()) ;
						unione.setTp_abilitaz(current.getTp_abilitaz()) ;
						unione.setCd_contr_sim(current.getCd_contr_sim()) ;
						unione.setFl_abil_forzat(current.getFl_abil_forzat()) ;
						unione.setCd_livello(current.getCd_livello()) ;
			  		}
			  		else
			  		{
						if ((current.getTp_abilitaz()=='S') ||
							(current.getTp_abilitaz()=='y'))
						   unione.setTp_abilitaz(current.getTp_abilitaz()) ;

						if ((unione.getCd_contr_sim() == null) ||
						    (current.getCd_contr_sim().compareTo(unione.getCd_contr_sim()) >0))
							unione.setCd_contr_sim(current.getCd_contr_sim()) ;

						if ((current.getFl_abil_forzat()=='N') ||
							(current.getFl_abil_forzat()=='n'))
							unione.setFl_abil_forzat(current.getFl_abil_forzat()) ;

						if ((unione.getCd_livello() == null) ||
							(current.getCd_livello().compareTo(unione.getCd_livello()) >0))
							unione.setCd_livello(current.getCd_livello()) ;
					}
		  		}
			}

			// l'unione completa di attributi per tipo materiale va salvato
			if (unione !=null)
				nuovo_elenco_parametri_materiale.add(unione) ;
		}

		elenco_parametri_materiale = nuovo_elenco_parametri_materiale ;

		return elenco_parametri_materiale ;
	}

	/**
	 * Pulisce e compatta l'elenco PARAMETRI SEM
	 * <br>
	 * La presenza di gruppi e sottogruppi potrebbe avere introdotto più permessi
	 * E' necessario fare il merge
	 * Questa volta devo raggruppare le informazioni per tipo sem
	 */
	protected List MergeParametriSem(List elenco_parametri_sem)
	{
		List nuovo_elenco_parametri_sem = new ArrayList() ;
//log.debug("--------->PRIMA PARAM SEM:"+elenco_parametri_sem.size()+":") ;

		for (int ind_elenco = 0 ; ind_elenco < elenco_parametri_sem.size() ; ind_elenco++)
		{

			Tbf_par_sem current = (Tbf_par_sem)elenco_parametri_sem.get(ind_elenco) ;

			// DEVO AGGIUNGERE IL RECORD SE LA COMBINAZIONE E' INEDITA
			boolean found = false ;
			for (int ind_appoggio = 0 ; ind_appoggio < nuovo_elenco_parametri_sem.size() ; ind_appoggio++)
			{
				Tbf_par_sem presente = (Tbf_par_sem)nuovo_elenco_parametri_sem.get(ind_appoggio) ;

				if ((presente.getTp_tabella_codici().equals(current.getTp_tabella_codici())==true) &&
					(presente.getCd_tabella_codici().equals(current.getCd_tabella_codici())==true))
				{
					found = true ;
					break ;
				}
			}

			if (found == false)
				nuovo_elenco_parametri_sem.add(current) ;
		}

//log.debug("--------->DOPO PARAM SEM:"+nuovo_elenco_parametri_sem.size()+":") ;

		return nuovo_elenco_parametri_sem ;
	}


	/**
	 * Pulisce e compatta l'elenco PARAMETRI AUTHORITY
	 * <br>
	 * La presenza di gruppi e sottogruppi potrebbe avere introdotto più permessi
	 * E' necessario fare il merge
	 * Questa volta devo raggruppare le informazioni per tipo autohority
	 */
	protected List MergeParametriAuthority(List elenco_parametri_authority)
	{
		List nuovo_elenco_parametri_authority = new ArrayList() ;

		// Mi creo un elenco tipi authority
		// Non li prendo dal decodificatore VOLUTAMENTE - perdonatemi
		List elenco_tipi_authority = new ArrayList();
		elenco_tipi_authority.add("AU");
		elenco_tipi_authority.add("TU");
		elenco_tipi_authority.add("SO");
		elenco_tipi_authority.add("RE");
		elenco_tipi_authority.add("MA");
		elenco_tipi_authority.add("CL");
		elenco_tipi_authority.add("LU");
		elenco_tipi_authority.add("UM");
		elenco_tipi_authority.add("DE");

		// for sui tipi authoriy!!!
		for  (int ind_tipo = 0 ; ind_tipo < elenco_tipi_authority.size(); ind_tipo++)
		{
			Tbf_par_auth unione = null ;
			String tipo_authority = (String) elenco_tipi_authority.get(ind_tipo);

			for (int ind_dummy =0 ; ind_dummy < elenco_parametri_authority.size() ; ind_dummy++)
			{
				Tbf_par_auth current = (Tbf_par_auth)elenco_parametri_authority.get(ind_dummy) ;

				if (current.getCd_par_auth().equals(tipo_authority)==true)
				{
					if (unione == null)
					{
						unione = new Tbf_par_auth() ;
						// Nuovo dato unione dei precedenti
						unione.setId_parametro(current.getId_parametro()) ;
						unione.setCd_par_auth(current.getCd_par_auth()) ;
						unione.setTp_abil_auth(current.getTp_abil_auth()) ;
						unione.setFl_abil_legame(current.getFl_abil_legame()) ;
						unione.setFl_leg_auth(current.getFl_leg_auth());
						unione.setCd_livello(current.getCd_livello()) ;
						unione.setCd_contr_sim(current.getCd_contr_sim()) ;
						unione.setFl_abil_forzat(current.getFl_abil_forzat()) ;
					}
					else
					{
						if ((current.getTp_abil_auth()=='S') ||
							(current.getTp_abil_auth()=='y'))
							unione.setTp_abil_auth(current.getTp_abil_auth()) ;

						if ((current.getFl_abil_legame()=='S') ||
							(current.getFl_abil_legame()=='y'))
							unione.setFl_abil_legame(current.getFl_abil_legame()) ;

						if ((unione.getCd_livello() == null) ||
							(current.getCd_livello().compareTo(unione.getCd_livello()) >0))
							unione.setCd_livello(current.getCd_livello()) ;

						if ((unione.getCd_contr_sim() == null) ||
							(current.getCd_contr_sim().compareTo(unione.getCd_contr_sim()) >0))
							unione.setCd_contr_sim(current.getCd_contr_sim()) ;

						if ((current.getFl_abil_forzat()=='N') ||
							(current.getFl_abil_forzat()=='n'))
							unione.setFl_abil_forzat(current.getFl_abil_forzat()) ;

						if ((current.getFl_leg_auth()=='S') ||
							(current.getFl_leg_auth()=='y'))
							unione.setFl_leg_auth(current.getFl_leg_auth()) ;
					}
				}
			}

			// l'unione completa di attributi per tipo materiale va salvato
			if (unione !=null)
			nuovo_elenco_parametri_authority.add(unione) ;
		}

		elenco_parametri_authority = nuovo_elenco_parametri_authority ;

		return elenco_parametri_authority ;

	}


	/**
	 * Compone l'elenco delle relazioni fra le attivita (Tr_att_att)
	 * <br>
	 * La presenza di questo metodo è dovuta al legame
	 * tra attività logiche (livello 0) e attivita concrete (livello 1)
	 *
	 * @param   Oggetto contenitore informazioni anagrafiche di base (validator_init)
	 * @param   Ricevo un vettore di attivita (già selezionate per soggetto)
	 * @return  Restituisco un vettore con  le sole relazioni tra le attivita
	 */

	protected List ComposeRelazioniAttivita(List  elenco_tutti_att_att, List elenco_attivita)
	{
		// Vettore con relazioni da considerare
		List new_elenco_relazioni_attivita = new ArrayList() ;

//log.debug("+++++++++++++++++++++++++++++++++++++++++++++++") ;
//log.debug("CONTROLLO RELAZIONI SU ELENCO DI:"+elenco_attivita.size()+":") ;

		int conta = 0 ;
		for (int ind_attivita = 0 ; ind_attivita  < elenco_attivita.size() ; ind_attivita++)
		{
			Tbf_attivita current = (Tbf_attivita)elenco_attivita.get(ind_attivita) ;

			for (int ind_relaz = 0 ; ind_relaz < elenco_tutti_att_att.size() ; ind_relaz++)
			{
				Tbf_attivita relazione = (Tbf_attivita)elenco_tutti_att_att.get(ind_relaz) ;

				if (relazione.getCd_funzione_parent()!= null && current.getCd_attivita().equals(relazione.getCd_funzione_parent().trim()))
				{
						new_elenco_relazioni_attivita.add(relazione) ;
				}
			}

		}

		return (new_elenco_relazioni_attivita) ;
	}
}
