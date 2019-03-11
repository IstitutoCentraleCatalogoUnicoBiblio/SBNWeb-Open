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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_autore_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.oggetti.AutoreBiblioteca;
import it.finsiel.sbn.polo.oggetti.Biblioteca;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_aut_bib;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;


/**
 * Gestisce le operazioni riguardanti le localizzazioni di autori:
 * Localizza, delocalizza, ricerca le localizzazioni di un autore
 * La localizzazione può essere solo 'per gestione'
 */
public class AutoreGestisceLocalizza extends AutoreBiblioteca{


	private static final long serialVersionUID = -4038829819859164702L;
	static Category log = Category.getInstance("iccu.serversbnmarc.autoreBiblio");
	public AutoreGestisceLocalizza() {
   		super();
	}

   /**
    * Ricerca le localizzazioni di un autore
    * verifica l'esistenza: (T001) in tb_autore (chiama verificaEsistenzaID di
    * ValidaAutore)
    * se non esiste segnala un diagnostico.
    * se esiste legge in tr_aut_bib
    * se c1_899 è compilato si applica una condizione di join con tb_biblioteca, dove
    * cd_ana_codice like c1_899
    * se c2_899 è compilato (in alternativa a c1_899, altrimenti si segnala
    * diagnostico) si applica la condizione di like su cd_polo e cd_ biblioteca di
    * tr_aut_bib
    * Prepara l'output secondo localizzzaInfoType
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public List cercaLocalizzaAutore(String IDAutore, String c1_899, String c2_899) throws IllegalArgumentException, InvocationTargetException, Exception{
        List TableDaoResult = new ArrayList();
		AutoreValida autoreValida = new AutoreValida();
			if (autoreValida.cercaAutorePerID(IDAutore).getElencoRisultati().size()!=0){
					if (c2_899!=null){
						//guardo nella vista tutti quelli con c2_899
						Vl_autore_bib vl_autore_bib = new Vl_autore_bib();
						vl_autore_bib.setVID(IDAutore);
						vl_autore_bib.setCD_POLO(c2_899.substring(0, 3));
		                if(!c2_899.substring(3, 6).trim().equals(""))
			                vl_autore_bib.setCD_BIBLIOTECA(c2_899.substring(3, 6));

						Vl_autore_bibResult vl_autore_bibResult = new Vl_autore_bibResult(vl_autore_bib);


						vl_autore_bibResult.executeCustom("selectPerPolo");
						TableDaoResult = vl_autore_bibResult.getElencoRisultati();


						//eseguo la query sulla prima vista
					} else if (c1_899!=null){
						Vl_autore_bib vl_autore_bib = new Vl_autore_bib();
						vl_autore_bib.setCD_ANA_BIBLIOTECA(c1_899);
						vl_autore_bib.setVID(IDAutore);
						Vl_autore_bibResult vl_autore_bibResult = new Vl_autore_bibResult(vl_autore_bib);


						vl_autore_bibResult.executeCustom("selectPerAnagrafeLike");

						TableDaoResult = vl_autore_bibResult.getElencoRisultati();


						//eseguo la query sulla seconda select
					} else {
						Vl_autore_bib vl_autore_bib = new Vl_autore_bib();
						vl_autore_bib.setVID(IDAutore);
						Vl_autore_bibResult vl_autore_bibResult = new Vl_autore_bibResult(vl_autore_bib);


						vl_autore_bibResult.executeCustom("selectPerVid");
						TableDaoResult = vl_autore_bibResult.getElencoRisultati();

					}
		}else
			throw new EccezioneSbnDiagnostico(3013);
		return TableDaoResult;
	}


   /**
    * verifica l'esistenza: (T001) in tb_autore (chiama verificaEsistenzaID di
    * ValidaAutore)
    * se non esiste segnala un diagnostico.
    * per ogni elemento T899:
    *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
    *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   Inserisce un record in tr_aut_bib.
    * Prepara l'output con esito positivo
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void localizzaAutore(String user, String IDAutore, List t899, String codicePoloUtente) throws IllegalArgumentException, InvocationTargetException, Exception{
		AutoreValida autoreValida = new AutoreValida();
        List bibliotecaTableDao = new ArrayList();

        TableDao tavola = autoreValida.cercaAutorePerID(IDAutore);
        List v = tavola.getElencoRisultati();
        String codicePolo,codiceBiblioteca;

			if (v.size()!=0){
				for (int i=0;i<t899.size();i++){
					if ((((C899)t899.get(i)).getC1_899()!=null)&(((C899)t899.get(i)).getC2_899()!=null)){
						throw new EccezioneSbnDiagnostico(3018);
					}else{
						//verifico esistenza di tb_biblioteca con c1_899 o c1899
						Biblioteca biblioteca = new Biblioteca();
						bibliotecaTableDao = biblioteca.verificaEsistenza(((C899)t899.get(i)).getC1_899(),((C899)t899.get(i)).getC2_899());
						for (int j=0;j<bibliotecaTableDao.size();j++){
							//verifico le abilitazioni dell'utente per gestire tb_biblioteca

							codicePolo = ((Tbf_biblioteca_in_polo)bibliotecaTableDao.get(j)).getCod_polo();
							codiceBiblioteca = ((Tbf_biblioteca_in_polo)bibliotecaTableDao.get(j)).getCd_biblioteca();
							biblioteca.verificaAbilitazioni(codicePoloUtente,codicePolo);
							inserisciTr_aut_bib(user,IDAutore,codicePolo,codiceBiblioteca);
						}
					}

				}
			}else {
				throw new EccezioneSbnDiagnostico(3013);
			}
	}

   /**
    * verifica l'esistenza: (T001) in tb_autore (chiama verificaEsistenzaID di
    * ValidaAutore)
    * se non esiste segnala un diagnostico.
    * per ogni elemento T899:
    *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
    *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   verifica esistenza record tr_aut_bib
    *   aggiorna il record in tr_aut_bib con fl_canc = 'S'
    * Prepara l'output con esito positivo
 * @throws Exception
 * @throws InvocationTargetException
    */
	public void delocalizzaAutore(String user,String IDAutore,List t899,String poloUtente, String codiceBiblioteca) throws InvocationTargetException, Exception {
		AutoreValida autoreValida = new AutoreValida();
		String codicePolo = null;
        List bibliotecaTableDao = new ArrayList();
        TableDao tavola = autoreValida.cercaAutorePerID(IDAutore);
        List v = tavola.getElencoRisultati();

            if (v.size()!=0){
				for (int i=0;i<t899.size();i++){
					if ((((C899)t899.get(i)).getC1_899()!=null)&(((C899)t899.get(i)).getC2_899()!=null)){
						throw new EccezioneSbnDiagnostico(3018);
					}else{
						//verifico esistenza di tb_biblioteca con c1_899 o c1899
						Biblioteca biblioteca = new Biblioteca();
						bibliotecaTableDao = biblioteca.verificaEsistenza(((C899)t899.get(i)).getC1_899(),((C899)t899.get(i)).getC2_899());
						for (int j=0;j<bibliotecaTableDao.size();j++){
							//verifico le abilitazioni dell'utente per gestire tb_biblioteca
							codicePolo = ((Tbf_biblioteca_in_polo)bibliotecaTableDao.get(j)).getCod_polo();
							codiceBiblioteca = ((Tbf_biblioteca_in_polo)bibliotecaTableDao.get(j)).getCd_biblioteca();
							biblioteca.verificaAbilitazioni(poloUtente,codicePolo);
							// Inizio almaviva2 14 Agosto 2009 non esite in Polo alcuna localizzzazione per gestione degli autori
							// ne di alcuna altra Authority - il controllo viene asteriscato
//							if (verificaEsistenzaTr_aut_bib(IDAutore,codicePolo,codiceBiblioteca))
							// Fine almaviva2 14 Agosto 2009
								cancellaTr_aut_bib(user,IDAutore,codicePolo,codiceBiblioteca);
						}
					}
				}
			}else {
				throw new EccezioneSbnDiagnostico(3013);
			}

	}

   /**
    * Il metodo azzera il flag di allineamento su comunicazione dell'utente. viene
    * attivato con il factoring ComunicaAllineati
    *
    * verifica l'esistenza: (T001) in tb_autore (chiama verificaEsistenzaID di
    * ValidaAutore)
    * se non esiste segnala un diagnostico.
    * per ogni elemento biblioteca in biblioV:
    *   se sono compilati solo i primi tre caratteri del record  (allineamento di
    * polo) deve esaminare tutte le tb_biblioteca  con cd_polo uguale, altrimenti
    * verifica esistenza di ogni record in tb_biblioteca
    *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   verifica esistenza record tr_aut_bib
    *   aggiorna il record in tr_aut_bib con fl_allinea = ' '
    * Prepara l'output con esito positivo
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void allineatoAutore(String user,String IDAutore,List biblioV,String codicePolo, String codiceBiblioteca) throws IllegalArgumentException, InvocationTargetException, Exception {
		AutoreValida autoreValida = new AutoreValida();
        List bibliotecaTableDao = new ArrayList();
        Tb_autore autore = autoreValida.verificaEsistenzaID(IDAutore);
		if (autore != null){
			log.info("VERIFICATO AUTORE "+ biblioV.size());
     /*   TableDao tavola = autoreValida.cercaAutorePerID(IDAutore);
        TableDao v = tavola.getElencoRisultati();

            if (v.size()!=0){
	*/			for (int i=0;i<biblioV.size();i++){
						//verifico esistenza di tb_biblioteca
						Biblioteca biblioteca = new Biblioteca();
						bibliotecaTableDao = biblioteca.verificaEsistenza(new String(),(String)biblioV.get(i));
						//verifico le abilitazioni dell'utente per gestire tb_biblioteca
						if (bibliotecaTableDao.size() != 0){
							//verifico le abilitazioni dell'utente per gestire tb_biblioteca
							log.info("STO PER VERIFICARE abilitazioni");
							codicePolo = ((String)biblioV.get(i)).substring(0,3);
							biblioteca.verificaAbilitazioni(codicePolo);
							log.info("VERIFICATO abilitazioni polo "+ codicePolo);
                            if ((((String)biblioV.get(i)).trim()).length() > 3) {
                            	codiceBiblioteca = ((String)biblioV.get(i)).substring(3,6);
                            } else {
                            	codiceBiblioteca = null;
                            }
							log.info("STO PER VERIFICARE abilitazioni biblioteca "+ codiceBiblioteca);
							Tr_aut_bib  tr_aut_bib = new Tr_aut_bib();
							boolean esito = false;
							if (codiceBiblioteca == null) esito = verificaEsistenzaTr_aut_bib(IDAutore,codicePolo);
							if (codiceBiblioteca != null) esito = verificaEsistenzaTr_aut_bib(IDAutore,codicePolo,codiceBiblioteca);
							if (esito) {
								log.info("esistenza tr_aut_bib ");
								esito = allineaTr_aut_bib(user,autore,codicePolo,codiceBiblioteca);
							} else {
								log.info("non esiste su tr_aut_bib ");
								throw new EccezioneSbnDiagnostico(3001);
							}
						}
				}
			} else {
				throw new EccezioneSbnDiagnostico(3013);
			}
	}


}
