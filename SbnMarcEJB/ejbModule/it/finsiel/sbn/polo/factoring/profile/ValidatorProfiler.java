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

import it.finsiel.sbn.polo.ejb.Profiler;
import it.finsiel.sbn.polo.ejb.factory.SbnMarcEJBFactory;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * Classe Validator
 * <p>
 * NOTA BENE:
 * Questa versione è stata appositamente creata per funzionare in una vm esterna
 * a quella di JBoss!
 * <p>
 * La classe Validator è alla base del meccanismo di gestione dei profili di utenza da parte
 * dell'amministratore di indice, che può assegnare e modificare dinamicamente servizi e possibilità
 * a disposizione dei singoli utenti e gruppi di utenza.
 * La classe Validator gestisce quindi la validazione degli utenti, verificando l'accesso dall'esterno
 * e l'estrapolazione dei servizi a disposizione.
 * </p>
 * @author
 */
//public class ValidatorAdminNoCache implements ValidatorAdminAPINoCache {
public class ValidatorProfiler {

    private static Logger log = Logger.getLogger("it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler");
    private Profiler profiler;

    private ValidatorProfiler() throws Exception {
        profiler = SbnMarcEJBFactory.getFactory().getProfiler();
        profiler.init();

    }
    public String getCodicePolo(){
    	try {
			return profiler.getCodicePolo();
		} catch (Exception e) {
			log.error("", e);
		}
    	return null;
    }

    /**
     * Verifica se il soggetto richiesto è un polo
     * <br>
     * Semplice: Se i primi tre caratteri sono XXX si tratta di un utente fisico
     * creato in fase di amministrazione, altrimenti è un codice polo
     * <br>
     * @param  codice (polo/gruppo/utente)
     * @return boolean (true=polo, false = no polo)
     */
    public boolean isPolo(String codice) {
        boolean is_polo = false;
        try {
            is_polo = profiler.isPolo(codice);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (is_polo);
    }
    /**
     * Restituisce il codice livello di un utente per uno specifico tipo di materiale
     */
    public Tbf_par_mat getParametriUtentePerMateriale(String codice, String tipoMateriale) {
        Tbf_par_mat par_mat = null;
        try {
            par_mat = profiler.getParametriUtentePerMateriale(codice, tipoMateriale);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return par_mat;
    }
    /**
     * Ritorna oggetto parametri utente
     * <br>
     * Torna un oggetto di tipo Tb_parametro
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param codice polo
     * @return oggetto complesso con informazioni
     */
    public Tbf_parametro getTb_parametro(String codice) {
        Tbf_parametro elemento = null;
        try {
            elemento = profiler.getTb_parametro(codice);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (elemento);
    }
    /**
     * Restituisce il codice livello di un utente per uno specifico tipo di authority
     */
    public Tbf_par_auth getParametriUtentePerAuthority(String codice, String tipoAuthority) {
        Tbf_par_auth par_aut = null;
        try {
            par_aut = profiler.getParametriUtentePerAuthority(codice, tipoAuthority);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        // almaviva MODIFICO PER NON AVERE LA PAR_AUT VALORIZZATO
        return par_aut;

        //return null;
    }
    /**
     * Ritorna oggetto con abilitazioni COMPLETE per gruppo/utente
     * <br>
     * Intuisce dalla composizione del codice ricevuto se si tratta di un utente o di un gruppo di tipo (Polo)
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * Se i primi tre caratteri del codice sono XXX si tratta di un utente della struttura
     * (utilizza I.Diretta o Forms amministrazione)
     * Else si tratta di un polo SBNMARC
     * <br>
     * @param codice (polo/gruppo/utente)
     * @return oggetto complesso con informazioni
     */
    public ValidatorContainerObject getAbilitazioni(String codice) {
        ValidatorContainerObject findit = null;
        try {
            findit = profiler.getAbilitazioni(codice);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }

        return (findit);
    }
    /**
     * Ritorna oggetto con abilitazioni per polo
     * <br>
     * ATTENZIONE: per ora carica solo i parametri e non le attività.
     * <br>
     * @param codice (polo/gruppo/utente)
     * @return oggetto complesso con informazioni
     */
    public ValidatorContainerObject getAbilitazioniPolo(String cd_polo) {
        ValidatorContainerObject findit = null;
        try {
            findit = profiler.getAbilitazioniPolo(cd_polo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }

        return (findit);
    }
    /**
     * Informa se l'attività per il gruppo/polo richiesto è abilitata (VERSIONE CODICE ATTIVITA NUMERICO)
     * <br>
     * Intuisce dalla composizione del codice ricevuto se si tratta di un utente o di un gruppo di tipo (Polo)
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * Se i primi tre caratteri del codice sono XXX si tratta di un utente della struttura
     * (utilizza I.Diretta o Forms amministrazione)
     * Else si tratta di un polo SBNMARC
     * <br>
     * @param codice (polo/gruppo/utente)
     * @param codice attivita
     * @return trovato = true/ non trovato = false
     */
    public boolean verificaAttivitaID(String codice, String codice_attivita) throws EccezioneSbnDiagnostico {
        boolean found = false;
        try {
            found = profiler.verificaAttivitaID(codice, codice_attivita);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (found);
    }
    /**
     * Informa se l'attività per il gruppo/polo richiesto è abilitata
     * <br>
     * Intuisce dalla composizione del codice ricevuto se si tratta di un utente o di un gruppo di tipo (Polo)
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * Se i primi tre caratteri del codice sono XXX si tratta di un utente della struttura
     * (utilizza I.Diretta o Forms amministrazione)
     * Else si tratta di un polo SBNMARC
     * <br>
     * @param codice (polo/gruppo/utente)
     * @return trovato = true/ non trovato = false
     */
    public boolean verificaAttivitaDesc(String codice, String descrizione_attivita) {
        boolean found = false;
        try {
            found = profiler.verificaAttivitaDesc(codice, descrizione_attivita);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (found);
    }
    /**
     * Ritorna vettore di sottoAttivita (per codice)
     * <br>
     * Permette di ricostruire le informazioni sulle attivita
     * Ritorna tutte le attività di secondo livello rispetto ad una indicata
     * <br>
     * @param  codice soggetto
     * @param  codice attivita principale
     * @return vettore attivita subordinate
     */
    public List getAttivitaFigli(String codice_soggetto, String codice_attivita) {
        List elenco_attivita_figli = new ArrayList(); // id_tipo_att_base
        try {
            elenco_attivita_figli = profiler.getAttivitaFigli(codice_soggetto,codice_attivita);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (elenco_attivita_figli);
    }

    /**
     * Ritorna vettore con tutte le Biblioteche appartenenti ad un Polo
     * <br>
     * Biblioteche in relazione ad uno specifico Polo
     * <br>
     * @param  codice Polo (3 lettere)
     * @return biblioteca di polo
     */
    public Tbf_biblioteca_in_polo getBiblioteca(String cod_polo, String cod_biblio) {
    	Tbf_biblioteca_in_polo biblioteche_polo = null;
        try {
        	biblioteche_polo = profiler.getBiblioteca(cod_polo,cod_biblio);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return (biblioteche_polo);
    }




    /**
     * Metodo Verifica se l'utente è un amministratore
     * <p>
     * Necessario per gestione form Statistiche
     * </p>
     * @param  codice utente (12)
     * @return boolean true (amministratore) false
     */
//     public boolean isAmministratore(String codice) {
//         boolean esito = false;
//         try {
//             esito = profiler.isAmministratore(codice);
//         } catch (Exception e) {
//             // TODO Auto-generated catch block
//             log.error("", e);
//         }
//         return (esito);
//     }
//   public boolean isInGruppo(String utente, String gruppo) {
//   boolean ritorno = false;
//   try {
//       ritorno = profiler.isInGruppo(utente,gruppo);
//   } catch (Exception e) {
//       // TODO Auto-generated catch block
//       log.error("", e);
//   }
//   return ritorno;
//}
   /**
    * Ritorna vettore parametri semantici
    * <br>
    * torna elenco oggetti costruiti in base all'albero delle abilitazioni
    * <br>
    * @param codice polo
    * @return vettore con i dati richiesti
    */
   public List getParametriSemantica(String codice) {
       List return_List = new ArrayList();
       try {
           return_List = profiler.getParametriSemantica(codice);
       } catch (Exception e) {
           // TODO Auto-generated catch block
           log.error("", e);
       }
       return (return_List);
   }

   /**
    * Ritorna informazioni anagrafiche utente
    * <br>
    * Ritorna le informazioni anagrafiche di base relative all' UTENTE FISICO
    * <br>
    * @param  codice soggetto
    * @return vettore attivita subordinate
    */
   public Tbf_bibliotecario getUtente(String codice_utente) {
	   Tbf_bibliotecario return_utente = null;
       try {
           return_utente = profiler.getUtente(codice_utente);
       } catch (Exception e) {
           // TODO Auto-generated catch block
           log.error("", e);
       }
       return return_utente;
   }

    private static ValidatorProfiler me;

    static synchronized public ValidatorProfiler getInstance() {
        if(me == null)
        {
            try {
                me = new ValidatorProfiler();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return me;
    }





    /**
     * Ritorna oggetto con abilitazioni COMPLETE per gruppo
     * <br>
     * Torna un oggetto di tipo ValidatorContainerObject
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param  id gruppo presente in tabella
     * @return oggetto complesso con informazioni
     */
//    public ValidatorContainerObject getAbilitazioniIdGruppo(long id_gruppo) {
//        try {
//            return (ValidatorContainerGroup) profiler.getAbilitazioniIdGruppo(id_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return null;
//    }

    /**
     * Ritorna oggetto con abilitazioni COMPLETE per gruppo
     * <br>
     * Torna un oggetto di tipo ValidatorContainerObject
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param  descrizione gruppo presente in tabella
     * @return oggetto complesso con informazioni
     */
//    public ValidatorContainerObject getAbilitazioniDsGruppo(String ds_gruppo) {
//        try {
//            return (ValidatorContainerGroup) profiler.getAbilitazioniDsGruppo(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return null;
//    }



    /**
     * Ritorna vettore di sottoAttivita (per descrizione)
     * <br>
     * Permette di ricostruire le informazioni sulle attivita
     * Ritorna tutte le attività di secondo livello rispetto ad una indicata
     * <br>
     * @param  codice soggetto
     * @param  descrizione attivita principale
     * @return vettore attivita subordinate
     */
//    public List getSubAttivita(String codice_soggetto, String descrizione_attivita) {
//        List elenco_attivita_figli = new ArrayList(); // id_tipo_att_base
//        try {
//            elenco_attivita_figli = profiler.getSubAttivita(codice_soggetto,descrizione_attivita);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_attivita_figli);
//    }


//    public boolean isInGruppo(String utente, String gruppo) {
//        boolean ritorno = false;
//        try {
//            ritorno = profiler.isInGruppo(utente,gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return ritorno;
//    }

    /**
     * Ritorna descrizione attivita
     * <br>
     * Lavora sul codice identificativo attivita
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param codice attivia
     * @return stringa descrizione
     */
//    public String getDs_attivita(long codice) {
//        String findit = null;
//        try {
//            findit = profiler.getDs_attivita(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return findit;
//    }

    /**
     * Ritorna vettore attività Utente
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param codice polo
     * @return vettore con i dati richiesti
     */
//    public List getElencoAttivita(String codice) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getElencoAttivita(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//
//
//        return (return_List);
//    }

    /**
     * Ritorna vettore attività Gruppo
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param  id gruppo
     * @return vettore con i dati richiesti
     */
//    public List getElencoAttivita(long id_gruppo) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getElencoAttivita(id_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }

    /**
     * Ritorna vettore attività Gruppo
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param  descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public List getElencoAttivitaDsGruppo(String ds_gruppo) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getElencoAttivitaDsGruppo(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }

    /**
     * Ritorna vettore Gruppi per Utente
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * SOLO PER UTENTE FISICO
     * <br>
     * @param codice polo
     * @return vettore con i dati richiesti
     */
//    public List getElencoGruppiUtente(String codice) {
//        List elenco_gruppi = new ArrayList();
//        try {
//            elenco_gruppi = profiler.getElencoGruppiUtente(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_gruppi);
//    }

    /**
     * Ritorna vettore Gruppi per Gruppo
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * SOLO PER GRUPPO LOGICO
     * <br>
     * @param codice gruppo
     * @return vettore con i dati richiesti
     */
//    public List getElencoGruppiGruppo(String codice) {
//        List elenco_gruppi = new ArrayList();
//        try {
//            elenco_gruppi = profiler.getElencoGruppiGruppo(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_gruppi);
//    }

    /**
     * Ritorna vettore Gruppi per Gruppo
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * SOLO PER GRUPPO LOGICO
     * <br>
     * @param descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public List getElencoGruppiDsGruppo(String ds_gruppo) {
//        List elenco_gruppi = new ArrayList();
//        try {
//            elenco_gruppi = profiler.getElencoGruppiDsGruppo(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_gruppi);
//    }

    /**
     * Ritorna vettore Gruppi per Gruppo
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * SOLO PER GRUPPO LOGICO
     * <br>
     * @param descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public boolean findGruppo(List elenco_gruppi, long id_gruppo) {
//        boolean found_in_elenco = false;
//        try {
//            found_in_elenco = profiler.findGruppo(elenco_gruppi, id_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return found_in_elenco;
//    }
    /**
     * Ritorna elenco email soggetto
     * <br>
     * Ritorna le informazioni anagrafiche di base relative al GRUPPO/POLO
     * <br>
     * @param  codice soggetto/gruppo
     * @return vettore attivita subordinate
     */
//    public String getIndirizziEMail(String codice) {
//        String elenco_email = null;
//        try {
//            elenco_email = profiler.getIndirizziEMail(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return elenco_email;
//    }
    /**
     * Ritorna informazioni anagrafiche gruppo
     * <br>
     * Ritorna le informazioni anagrafiche di base relative al GRUPPO/POLO
     * <br>
     * @param  codice soggetto
     * @return vettore attivita subordinate
     */
//    public Tb_gruppo getGruppo(String codice_polo) {
//        Tb_gruppo return_gruppo = null;
//        try {
//            return_gruppo = profiler.getGruppo(codice_polo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_gruppo);
//    }

    /**
     * Ritorna informazioni anagrafiche gruppo
     * <br>
     * Ritorna le informazioni anagrafiche di base relative al GRUPPO/POLO
     * <br>
     * @param  descrizione gruppo
     * @return vettore attivita subordinate
     */
//    public Tb_gruppo getDsGruppo(String ds_gruppo) {
//        Tb_gruppo risultato = null;
//        try {
//            risultato = profiler.getDsGruppo(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return risultato;
//    }



    /**
     * Ritorna oggetto parametri utente
     * <br>
     * Torna un oggetto di tipo Tb_parametro
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param  descrizione gruppo
     * @return oggetto complesso con informazioni
     */
//    public Tbf_parametro getDsGruppoTb_parametro(String ds_gruppo) {
//        Tbf_parametro elemento = null;
//        try {
//            elemento = profiler.getDsGruppoTb_parametro(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elemento);
//    }
    /**
     * Ritorna vettore parametri authority
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param codice polo
     * @return vettore con i dati richiesti
     */
//    public List getParametriAuthority(String codice) {
//
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getParametriAuthority(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }

    /**
     * Ritorna vettore parametri authority
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param  descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public List getDsGruppoParametriAuthority(String ds_gruppo) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getDsGruppoParametriAuthority(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }

    /**
     * Ritorna vettore parametri materiale
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param codice polo
     * @return vettore con i dati richiesti
     */
//    public List getParametriMateriale(String codice) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getParametriMateriale(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }

    /**
     * Ritorna vettore parametri materiale
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param  descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public List getDsGruppoParametriMateriale(String ds_gruppo) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getDsGruppoParametriMateriale(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }


    /**
     * Ritorna vettore parametri semantici
     * <br>
     * torna elenco oggetti costruiti in base all'albero delle abilitazioni
     * <br>
     * @param  descrizione gruppo
     * @return vettore con i dati richiesti
     */
//    public List getDsGruppoParametriSemantica(String ds_gruppo) {
//        List return_List = new ArrayList();
//        try {
//            return_List = profiler.getDsGruppoParametriSemantica(ds_gruppo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (return_List);
//    }
    /**
     * Ritorna oggetto con elenco abilitazioni Utente distinte per TIPO ATTIVITA
     * <br>
     * Torna un vettore contenente oggetti di tipo Tb_tp_attivita
     * Metodo a disposizione delle FORM per dedurre le voci da inserire nei menu
     *
     * TIPO ATTIVITA PERMESSE IN INPUT:
     * S = Sbnmarc (attività di protocollo)
     * B = Bis (report)
     * A = Amministrazione (form amministrazione)
     * M = Monitoraggio (form monitoraggio)
     * T = sTatistiche (form statistiche)
     * N= sbN (attività old protocollo - forse mai utilizzato)
     * <br>
     * @param codice utente, tipo attivita
     * @return vettore attività permesse
     */
//    public List getElencoAbilitazioniTipoUtente(String cd_utente_amm, String tp_attivita) {
//        List elenco_abilitazioni = new ArrayList();
//        try {
//            elenco_abilitazioni = profiler.getElencoAbilitazioniTipoUtente(cd_utente_amm, tp_attivita);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_abilitazioni);
//    }

    /**
     * Ritorna oggetto con elenco abilitazioni Utente distinte per TIPO ATTIVITA e per LIVELLO
     * <br>
     * Torna un vettore contenente oggetti di tipo Tb_tp_attivita al livello richiesto (0 or 1)
     * Metodo a disposizione delle FORM per dedurre le voci da inserire nei menu
     *
     * <br>
     * @param codice utente, tipo attivita , livello (0/1)
     * @return vettore attività permesse
     */
//    public List getElencoAbilitazioniTipoUtente(String cd_utente_amm, String tp_attivita, long livello) {
//        List elenco_abilitazioni = new ArrayList();
//        try {
//            elenco_abilitazioni = profiler.getElencoAbilitazioniTipoUtente(cd_utente_amm, tp_attivita, livello);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_abilitazioni);
//    }


    /**
     * Informa se l'attività per il gruppo (polo) richiesto è abilitata
     * <br>
     * Confronta la descrizione
     * <br>
     * @param  codice polo
     * @param   stringa descrizione attivita
     * @return trovato = true/ non trovato = false
     */
//    public boolean verificaAttivitaUtente(String cd_utente_amm, String descrizione_attivita) {
//        boolean found = false;
//        try {
//            found = profiler.verificaAttivitaUtente(cd_utente_amm, descrizione_attivita);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return found;
//    }



    /**
     * Informa se l'attività per il gruppo (polo) richiesto è abilitata
     * <br>
     * Confronta la descrizione
     * <br>
     * @param  codice polo
     * @param   codice descrizione attivita
     * @return trovato = true/ non trovato = false
     */
//    public boolean verificaAttivitaPolo(String codice_polo, long codice_attivita) {
//        boolean found = false;
//        try {
//            found = profiler.verificaAttivitaPolo(codice_polo, codice_attivita);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return found;
//    }

    /**
     * Informa se l'attività per il gruppo (polo) richiesto è abilitata
     * <br>
     * Confronta la descrizione
     * <br>
     * @param  codice polo
     * @param   codice descrizione attivita
     * @return trovato = true/ non trovato = false
     */
//    public boolean verificaAttivitaUtente(String cd_utente_amm, long codice_attivita) {
//        boolean found = false;
//        try {
//            found = profiler.verificaAttivitaUtente(cd_utente_amm, codice_attivita);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return found;
//    }

    /**
    * Metodo Verifica se l'utente (o il gruppo) lavora su DB versione VUOTA
    * <p>
    * Necessario per la visualizzazione di dati di IMPORT
    * </p>
    * @param  codice utente (12)
    * @return boolean true (lavora su DB vuoto) false (non lavora su db vuoto)
    */
//    public boolean isDbVuoto(String codice) {
//        boolean ritorno= false;
//        try {
//            ritorno = profiler.isDbVuoto(codice);
//            return ritorno;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return ritorno;
//    }

    /**
    * Metodo Verifica se l'utente (o il gruppo) lavora su DB versione STANDARD
    * <p>
    * Necessario per la visualizzazione di dati di IMPORT
    * </p>
    * @param  codice utente (12)
    * @return boolean true (lavora su DB standard) false (non lavora su db standard)
    */
//    public boolean isDbTest(String codice) {
//        boolean ritorno= false;
//
//        try {
//            ritorno = profiler.isDbTest(codice);
//            return ritorno;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return ritorno;
//    }



    /**
     * Ritorna vettore con tutte le Biblioteche
     * <br>
     * Biblioteche in relazione ai vari Poli
     * <br>
     * @param  nessuno
     * @return vettore biblioteche
     */
//    public List getAllBiblioteche() {
//        List arrayList = new ArrayList();
//        try {
//            arrayList = profiler.getAllBiblioteche();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return arrayList;
//    }

    /**
     * Ritorna vettore con la decodifica dei problemi
     * <br>
     * Elenco tipologie problemi rilevati dai programmi di Monitoraggio
     * <br>
     * @param  nessuno
     * @return vettore elenco problemi
     */
//    public List getAllProblemi() {
//        List arrayList = new ArrayList();
//        try {
//            arrayList = profiler.getAllProblemi();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return arrayList;
//    }

    /**
     * Ritorna vettore con tutte le Biblioteche appartenenti ad un Polo
     * <br>
     * Biblioteche in relazione ad uno specifico Polo
     * <br>
     * @param  codice Polo (3 lettere)
     * @return vettore biblioteche
     */
//    public List getBibliotechePolo(String cd_polo) {
//
//        List elenco_biblioteche_polo = new ArrayList();
//        try {
//            elenco_biblioteche_polo = profiler.getBibliotechePolo(cd_polo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (elenco_biblioteche_polo);
//    }

//    /**
//     * Ritorna vettore con tutte le Biblioteche appartenenti ad un Polo
//     * <br>
//     * Biblioteche in relazione ad uno specifico Polo
//     * <br>
//     * @param  codice Polo (3 lettere)
//     * @return vettore biblioteche
//     */
//    public HashMap getBibliotechePoloHash(String cd_polo) {
//        HashMap hash_biblioteche_polo = new HashMap();
//        try {
//            hash_biblioteche_polo = profiler.getBibliotechePoloHash(cd_polo);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (hash_biblioteche_polo);
//    }

    /**
     * Ritorna vettore con tutte le tipologie di Problemi monitorati
     * <br>
     * Problemi (tipologie)
     * <br>
     * @param
     * @return hash problemi
     */
//    public Map getProblemiHash() {
//        Map hash_problemi = new HashMap();
//        try {
//            hash_problemi = profiler.getProblemiHash();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (hash_problemi);
//    }
//
    /**
     * Ritorna vettore con tutte le tipologie di Problemi monitorati
     * <br>
     * Problemi (tipologie)
     * <br>
     * @param
     * @return hash problemi
     */
//    public SortedHashtable getProblemiSortedHash() {
//        SortedHashtable hash_problemi = new SortedHashtable();
//        try {
//            hash_problemi = profiler.getProblemiSortedHash();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (hash_problemi);
//    }
//
    /**
     * Ritorna Map con tutte le attivita ed i relativi codici
     * <br>
     * Problemi (tipologie)
     * <br>
     * @param
     * @return hash attivita
     */
//    public Map getAttivitaHash() {
//        Map hash_attivita = new HashMap();
//        try {
//            hash_attivita = profiler.getAttivitaHash();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (hash_attivita);
//    }
//
    /**
     * Ritorna SortedMap con tutte le attivita ed i relativi codici
     * <br>
     * Problemi (tipologie)
     * <br>
     * @param
     * @return hash attivita
     */
//    public SortedHashtable getAttivitaSortedHash() {
//        SortedHashtable hash_attivita = new SortedHashtable();
//        try {
//            hash_attivita = profiler.getAttivitaSortedHash();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return (hash_attivita);
//    }
//
    /**
     * Ritorna tutte le descrizioni attivita
     * <br>
     * <br>
     * @return vettore descrizioni
     */
//    public List getAllDs_attivita() {
//        List arrayList = new ArrayList();
//        try {
//            arrayList = profiler.getAllDs_attivita();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return arrayList;
//    }

    /**
     * Ritorna tutte le descrizioni dei Bis
     * <br>
     * <br>
     * @return vettore descrizioni
     */
//    public List getAllDs_Bis() {
//        List attivita = new ArrayList();
//        try {
//            attivita = profiler.getAllDs_Bis();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//        return attivita ;
//    }
    /**
         * Ritorna tutte le descrizioni dei Bis
         * <br>
         * <br>
         * @return vettore descrizioni
         */
    /*
    public List getAllDs_BisXute(String ute) {
        List arrayList = new ArrayList();
        try {
            arrayList = profiler.getAllDs_BisXute(ute);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
        return arrayList;
    }
*/
    /**
     * Ritorna classe da lanciare per  attivita
     * <br>
     * Lavora sul codice identificativo attivita
     * Metodo a disposizione dei FACTORING (Sbnmarc e non)
     * <br>
     * @param codice attivia
     * @return stringa descrizione
     */
//    public String getOggetto(long codice) {
//        String findit = null;
//        try {
//            findit = profiler.getOggetto(codice);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error("", e);
//        }
//
//        return findit;
//    }
//    public static void main(String argv[]){
//       ValidatorAdminNoCache.getInstance();
//    }
}
