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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_marca_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.oggetti.Biblioteca;
import it.finsiel.sbn.polo.oggetti.MarcaBiblioteca;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tr_mar_bib;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_bib;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;


/**
 * Gestisce le operazioni riguardanti le localizzazioni di marche editoriali:
 * Localizza, delocalizza, ricerca le localizzazioni di una marca.
 * La localizzazioni può essere solo 'per gestione'
 */
public class MarcaGestisceLocalizza extends MarcaBiblioteca{


	private static final long serialVersionUID = -6183447202737892300L;
	static Category log = Category.getInstance("iccu.serversbnmarc.marcaBiblio");

	public MarcaGestisceLocalizza() {
		//super();

	}

   /**
    * Ricerca le localizzazioni di una marca
    * verifica l'esistenza: (T001) in tb_marca (chiama verificaEsistenzaID di
    * MarcaValida)
    * se non esiste segnala un diagnostico.
    * se esiste legge in tr_mar_bib
    * se c1_899 è compilato si applica una condizione di join con tb_biblioteca, dove
    * cd_ana_codice like c1_899
    * se c2_899 è compilato (in alternativa a c1_899, altrimenti si segnala
    * diagnostico) si applica la condizione di like su cd_polo e cd_ biblioteca di
    * tr_mar_bib
    * Prepara l'output secondo localizzzaInfoType
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public List cercaLocalizzaMarca(String IDmarca, String c1_899, String c2_899) throws IllegalArgumentException, InvocationTargetException, Exception {
        List TableDaoResult = new ArrayList();
		MarcaValida marcaValida = new MarcaValida();
		if (marcaValida.verificaEsistenzaID(IDmarca)){
				if (c2_899!=null){
					//guardo nella vista tutti quelli con c2_899
					Vl_marca_bib vl_marca_bib = new Vl_marca_bib();
					vl_marca_bib.setMID(IDmarca);
					vl_marca_bib.setCD_POLO(c2_899.substring(0, 3));
	                if(!c2_899.substring(3, 6).trim().equals(""))
			            vl_marca_bib.setCD_BIBLIOTECA(c2_899.substring(3, 6));

					Vl_marca_bibResult vl_marca_bibResult = new Vl_marca_bibResult(vl_marca_bib);


					vl_marca_bibResult.executeCustom("selectPerPolo");
					TableDaoResult = vl_marca_bibResult.getElencoRisultati();


					//eseguo la query sulla prima vista
				} else if (c1_899!=null){
					Vl_marca_bib vl_marca_bib = new Vl_marca_bib();
					vl_marca_bib.setCD_ANA_BIBLIOTECA(c1_899);
					vl_marca_bib.setMID(IDmarca);
					Vl_marca_bibResult vl_marca_bibResult = new Vl_marca_bibResult(vl_marca_bib);


					vl_marca_bibResult.executeCustom("selectPerAnagrafeLike");
					TableDaoResult = vl_marca_bibResult.getElencoRisultati();


					//eseguo la query sulla seconda select
				} else {
					Vl_marca_bib vl_marca_bib = new Vl_marca_bib();
					vl_marca_bib.setMID(IDmarca);
					Vl_marca_bibResult vl_marca_bibResult = new Vl_marca_bibResult(vl_marca_bib);


					vl_marca_bibResult.executeCustom("selectPerMid");
					TableDaoResult = vl_marca_bibResult.getElencoRisultati();

				}
		}else
			throw new EccezioneSbnDiagnostico(3013);
		return TableDaoResult;

	}

	private boolean c1_899Completo(String c1_899) {

		String input = new String();
		if (c1_899.length()==3) return false;
		for (int i=3;i<6;i++){
			input = input + c1_899.charAt(i);
		}
		if (input.equals("   ")) return false;
		else return true;
	}



   /**
    * verifica l'esistenza: (T001) in tb_marca (chiama verificaEsistenzaID di
    * ValidaMarca)
    * se non esiste segnala un diagnostico.
    * per ogni elemento T899:
    *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
    *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   Inserisce un record in tr_mar_bib.
    * Prepara l'output con esito positivo
    *
    * codiceBibliotecaUtente è il codice della biblioteca dell'utente
 * @throws Exception
 * @throws InvocationTargetException
 * @throws InfrastructureException
 * @throws IllegalArgumentException
    */
	public void localizzaMarca(String user,String id, List t899, String codicePoloUtente) throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception {
		String codicePolo, codiceBiblioteca;
        List bibliotecaTableDao = new ArrayList();
		MarcaValida marcaValida = new MarcaValida();
		if (marcaValida.verificaEsistenzaID(id)){
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
//						biblioteca.verificaAbilitazioni(codicePoloUtente,codicePolo);
						verificaAbilita(user,(C899)t899.get(i),codicePolo,((C899)t899.get(i)).getTipoInfo());
						MarcaBiblioteca marcaBiblioteca = new  MarcaBiblioteca ();
						marcaBiblioteca.inserisciTr_mar_bib(user,id,codicePolo,codiceBiblioteca);
					}
				}

			}
		}else {
			throw new EccezioneSbnDiagnostico(3013);
		}

   }

	private boolean verificaAbilita(String user, C899 c899, String codicePolo, SbnTipoLocalizza tipoLocalizza) throws EccezioneSbnDiagnostico{
		ValidatorProfiler validator = ValidatorProfiler.getInstance();
		if (SbnTipoLocalizza.POSSESSO.getType() == tipoLocalizza.getType() ||
				SbnTipoLocalizza.TUTTI.getType() == tipoLocalizza.getType()) {
			if (validator.verificaAttivitaID(user,CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_PER_ALTRI_POLI_1031))
				return true;
			//almaviva5_20140521 evolutive google3
	        //if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030))
	        if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008))
				throw new EccezioneSbnDiagnostico(4000);
			else if (!codicePolo.equals(user.substring(0,3)))
				throw new EccezioneSbnDiagnostico(4000);
			}
		return true;

	}


   /**
    * verifica l'esistenza: (T001) in tb_marca (chiama verificaEsistenzaID di
    * ValidaMarca)
    * se non esiste segnala un diagnostico.
    * per ogni elemento T899:
    *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
    *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   verifica esistenza record tr_mar_bib
    *   aggiorna il record in tr_mar_bib con fl_canc = 'S'
    * Prepara l'output con esito positivo
 * @throws Exception
 * @throws InvocationTargetException
 * @throws InfrastructureException
 * @throws IllegalArgumentException
    */
	public void delocalizzaMarca(String user,String id, List t899,String codicePoloUtente) throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception{
        List bibliotecaTableDao = new ArrayList();
		String codicePolo, codiceBiblioteca;
		MarcaValida marcaValida = new MarcaValida();
		if (marcaValida.verificaEsistenzaID(id)){
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
						MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
						if (marcaBiblioteca.verificaEsistenzaTr_mar_bib(id,codicePolo,codiceBiblioteca) != null)
							marcaBiblioteca.aggiornaTr_mar_bib(user,id,codicePolo,codiceBiblioteca);
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
    * verifica l'esistenza: (T001) in tb_marca(chiama verificaEsistenzaID di
    * ValidaMarca
    * se non esiste segnala un diagnostico.
    * per ogni elemento biblioteca in biblioV:
    *   se sono compilati solo i primi tre caratteri del record  (allineamento di
    * polo) deve esaminare tutte le tb_biblioteca  con cd_polo uguale, altrimenti
    * verifica esistenza di ogni record in tb_biblioteca
    * verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
    * caratteri di sbnuser.biblioteca)
    *   verifica esistenza record tr_mar_bib
    *   aggiorna il record in tr_mar_bib con fl_allinea = ' '
    * Prepara l'output con esito positivo
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void allineatoMarca(String user,String IDMArca,List biblioV,String codicePolo, String codiceBiblioteca) throws IllegalArgumentException, InvocationTargetException, Exception{
		MarcaValida marcaValida = new MarcaValida();
        List bibliotecaTableDao = new ArrayList();
        Tb_marca marca = marcaValida.estraiMarcaPerID(IDMArca);
			if (marca != null){
				log.info("VERIFICATO MARCA "+ biblioV.size());
				for (int i=0;i<biblioV.size();i++){
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
							log.info("STO PER VERIFICARE marca biblioteca "+ codiceBiblioteca);
						    MarcaBiblioteca mB = new MarcaBiblioteca();
							Tr_mar_bib  tr_mar_bib = new Tr_mar_bib();
                            List esito = new ArrayList();

							if (codiceBiblioteca == null) esito = mB.verificaEsistenzaTr_mar_bib(IDMArca,codicePolo);
							if (codiceBiblioteca != null) esito = mB.verificaEsistenzaTr_mar_bib(IDMArca,codicePolo,codiceBiblioteca);
							if (esito.size() != 0) {
								log.info("esistenza tr_mar_bib ");
								mB.allineaTr_mar_bib(user,marca,codicePolo,codiceBiblioteca);
							} else {
								log.info("non esiste su tr_mar_bib ");
								throw new EccezioneSbnDiagnostico(3001);
							}
						}
				}
			} else {
				throw new EccezioneSbnDiagnostico(3013);
			}
	}
}
