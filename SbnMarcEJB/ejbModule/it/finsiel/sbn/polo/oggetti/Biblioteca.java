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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tbf_biblioteca_in_poloResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 *
 * classe  utile per la verifica dell'autenticazione di un utente rispetto una biblioteca
 *
 */
public class Biblioteca extends Tbf_biblioteca_in_polo{



	private static final long serialVersionUID = -1022684401727500272L;

	public Biblioteca() {
	}



	/**
	 * Method verificaEsistenza.
	 *
	 * @param c2_899 polo + sigla
	 * @param c1_899 cd_ana_biblioteca
	 * controlla che il parametro passato in input c2_899
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List verificaEsistenza(String c1, String c2) throws IllegalArgumentException, InvocationTargetException, Exception {
        List resultTableDao = new ArrayList();

		if (c2 != null){
			//controllo se sono solo 3 caratteri
			if (c2.trim().length()<5){
				resultTableDao = verificaEsistenzaPerPoloLike(c2.substring(0,3));
			}else{
				resultTableDao = verificaEsistenzaPerPoloEsatto(c2);
			}
		}else if(c1 != null){
			resultTableDao = verificaEsistenzaPerAnagrafe(c1);
		}
		if (resultTableDao.size() == 0){
			throw new EccezioneSbnDiagnostico(3053);
		}
		return resultTableDao;
	}

	/**
	 * Method estraiPolo.
	 * @param c2
	 * @return String
	 * ritorna il i primi 3 caratteri del polo dal campo c2_899
	 */
/*	private String estraiPolo(String c2) {
		String input = new String();
		for (int i=0;i<3;i++){
			input = input + c2.charAt(i);
		}
		return input;
	}
*/

	/**
	 * Method verificaEsistenzaPerPoloLike.
	 * @param c2
	 * @return TableDao
	 * ritorna un vettore di oggetti di tipo tb_biblioteca_in_polo corrispondenti alla stringa di 3
	 * caratteri passata in input
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private List verificaEsistenzaPerPoloLike(String c2) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tbf_biblioteca_in_polo biblioteca = new Tbf_biblioteca_in_polo();
		biblioteca.setCod_polo(c2);

		Tbf_biblioteca_in_poloResult tb_bibliotecaResult = new Tbf_biblioteca_in_poloResult(biblioteca);


		tb_bibliotecaResult.executeCustom("selectPerPoloLike");
        List v =tb_bibliotecaResult.getElencoRisultati();

		return v;
	}


	public List verificaEsistenzaPerPoloEsatto(String polo) throws EccezioneDB, InfrastructureException{
		Tbf_biblioteca_in_polo biblioteca = new Tbf_biblioteca_in_polo();
		biblioteca.setCod_polo(polo.substring(0,3).trim().toUpperCase());
		biblioteca.setCd_biblioteca(polo.substring(3,6).toUpperCase());
		Tbf_biblioteca_in_poloResult tb_bibliotecaResult = new Tbf_biblioteca_in_poloResult(biblioteca);

        tb_bibliotecaResult.valorizzaElencoRisultati(tb_bibliotecaResult.selectPerKey(biblioteca.leggiAllParametro()));
		//tb_bibliotecaResult.selectPerKey(biblioteca.leggiAllParametro());
        List v = tb_bibliotecaResult.getElencoRisultati();


    	return v;
	}

	/* cerco nella tabella tbf_biblioteca_in_polo con cd_ana_biblioteca = anagrafe
	 */
	private List verificaEsistenzaPerAnagrafe(String anagrafe) throws IllegalArgumentException, InvocationTargetException, Exception{
		Tbf_biblioteca_in_polo biblioteca = new Tbf_biblioteca_in_polo();
		biblioteca.setCd_ana_biblioteca(anagrafe)		;
		Tbf_biblioteca_in_poloResult tb_bibliotecaResult = new Tbf_biblioteca_in_poloResult(biblioteca);


		tb_bibliotecaResult.executeCustom("selectPerAnagrafe");
        List v = tb_bibliotecaResult.getElencoRisultati();

		return v;
	}

	/**
	 * Method verificaAbilitazioni.
	 * @param bibliotecaUtente
	 * in input riceve la stringa (i primi 3 caratteri) della biblioteca contenuta nel
	 * file xml in sbnUser.Biblioteca
	 * cerco all'interno di tbf_biblioteca_in_polo se ci sono biblioteche che hanno per cd_polo
	 * questi tre caratteri
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	//?????????????ricontrollare
	public boolean verificaAbilitazioni(String bibliotecaUtente) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tbf_biblioteca_in_polo tb_biblioteca = new Tbf_biblioteca_in_polo();
		tb_biblioteca.setCod_polo(bibliotecaUtente);
		Tbf_biblioteca_in_poloResult tb_bibliotecaResult = new Tbf_biblioteca_in_poloResult(tb_biblioteca);


		tb_bibliotecaResult.executeCustom("selectPerCd_Polo");
        List v = tb_bibliotecaResult.getElencoRisultati();

		if (v.size() != 0){
			return true;
		}else{
			throw new EccezioneSbnDiagnostico(3009);
		}
	}

	/**
	 * verifica che il polo dell'utente sia il polo della biblioteca
	 */
	public void verificaAbilitazioni(String poloUtente, String codicePolo) throws EccezioneDB, EccezioneSbnDiagnostico {
		ValidatorProfiler validator = ValidatorProfiler.getInstance();
		if (validator.isPolo(poloUtente)) {
		    if (poloUtente.equals(codicePolo) == false) {
                throw new EccezioneSbnDiagnostico(4000);
            }
        }
	}


}
