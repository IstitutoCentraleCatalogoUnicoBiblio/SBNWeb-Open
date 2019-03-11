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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_parolaResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_marca_parResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.orm.Tb_parola;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_par;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * @author
 * Rappresenta le parole chiavi della marca
 * l'oggetto Parola implementa i metodi di ricerca sul db
 * gestisce operazioni complesse sull'oggetto tb_parole
 */
public class Parola extends Tb_parola
{



	private static final long serialVersionUID = 869458293350783772L;


	public Parola(){

	}

   /**
    * Metodo per la ricerca per parola chiave della marca editoriale antica:
    * max 5 parole in OR
    * dato un vettore di parole cerco le marche alle quali corrispondono
    * INPUT: vettore di parole
    * OUTPUT: vettore di oggetti di tipo tb_marca
    * /

	public TableDao cercaMarcaPerParola(String parole[],String tipoOrd) throws EccezioneDB, EccezioneSbnDiagnostico {
	   	TableDao resultTableDao = new TableDao();
	   	TableDao TableDao = new TableDao();
	   	if (parole.length>5)
	   		throw new EccezioneSbnDiagnostico(3021);
        for (int i = 0; i < parole.length; i++){
	   			parole[i] = parole[i].toUpperCase().trim();
        }
		TableDao TableDaoTemp;
       	Vl_marca_par vl_marca_par = new Vl_marca_par();
       	vl_marca_par.setParola(parole[0]);
		Vl_marca_parResult vl_marca_parResult = new Vl_marca_parResult(vl_marca_par);


		vl_marca_parResult.executeCustom("selectPerParola");
		TableDaoTemp = vl_marca_parResult.getElencoRisultati();
		int i=0;
		while ((i <parole.length)&(TableDaoTemp.size()>0)){
			if (!parole[i].equals("")){

				vl_marca_parResult = (Vl_marca_parResult) cercaMarcaParola(TableDaoTemp,parole[i]);
				TableDaoTemp = vl_marca_parResult.getElencoRisultati();
			}
			i++;
		}
		return vl_marca_parResult;
	}

    /**
     * Metodo per la ricerca per parola chiave della marca editoriale antica:
     * max 5 parole in OR
     * dato un vettore di parole cerco le marche alle quali corrispondono
     * INPUT: vettore di parole
     * OUTPUT: vettore di oggetti di tipo tb_marca
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
     */

     public TableDao cercaMarcaPerParola(String parole[],String tipoOrd) throws IllegalArgumentException, InvocationTargetException, Exception {
         if (parole.length>5)
             throw new EccezioneSbnDiagnostico(3021);
         Vl_marca_par vl_marca_par = new Vl_marca_par();
         vl_marca_par.setPAROLA(parole[0].trim().toUpperCase());
         for (int i = 1; i < parole.length; i++){
             vl_marca_par.settaParametro("XXXparola"+i,parole[i].trim().toUpperCase());
         }
         Vl_marca_parResult vl_marca_parResult = new Vl_marca_parResult(vl_marca_par);


         vl_marca_parResult.executeCustom("selectPerParole", tipoOrd);
         return vl_marca_parResult;
     }



    /**
     * Method contaMarcaPerParola.
     * @param _b_921
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaMarcaPerParola(String[] parole) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (parole.length>5)
            throw new EccezioneSbnDiagnostico(3021);
        Vl_marca_par vl_marca_par = new Vl_marca_par();
        vl_marca_par.setPAROLA(parole[0].trim().toUpperCase());
        for (int i = 1; i < parole.length; i++){
            vl_marca_par.settaParametro("XXXparola"+i,parole[i].trim().toUpperCase());
        }
        Vl_marca_parResult vl_marca_parResult = new Vl_marca_parResult(vl_marca_par);


        vl_marca_parResult.executeCustom("countPerParole");
        return conta(vl_marca_parResult);
    }


	/**
	 * Method contaMarcaPerParola.
	 * @param _b_921
	 * @return int
	 * /
	public int contaMarcaPerParola(String[] _b_921) throws EccezioneSbnDiagnostico, EccezioneDB {
		TableDao tavola;
		tavola = cercaMarcaPerParola(_b_921,null);
		int size = tavola.getElencoRisultati().size();

		return size;
	}


    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            throw new EccezioneDB(1203,"Errore nella lettura dal DB");
//        }
    }



/**
 * TableDao è un vettore di vl_marca_par con le parole in and fino a questo punto
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
 */
	private TableDao cercaMarcaParola(List TableDao,String parola) throws IllegalArgumentException, InvocationTargetException, Exception{
		Vl_marca_par vl_marca_par;
		Vl_marca_parResult vl_marca_parResult = null;
        for (int i = 0; i < TableDao.size(); i++){
        	vl_marca_par = new Vl_marca_par();
        	vl_marca_par.setMID(((Vl_marca_par)TableDao.get(i)).getMID());
        	vl_marca_par.setPAROLA(parola);
            if (vl_marca_parResult != null)

			vl_marca_parResult = new Vl_marca_parResult(vl_marca_par);


			vl_marca_parResult.executeCustom("selectPerParola");
			if (vl_marca_parResult.getElencoRisultati().size()==0 )
				TableDao.remove(i);
        }
		return vl_marca_parResult;
	}



	public List cercaParoleDiMarca(String mid) throws IllegalArgumentException, InvocationTargetException, Exception{
		setMID(mid);
	    Tb_parolaResult tavola = new Tb_parolaResult(this);


	    tavola.executeCustom("selectPerMarca");
        List v = tavola.getElencoRisultati();

	    return v;
   }

	/**
	 * metodo che restituisce un true se esiste un record nella tabella avente
	 * mid e parola
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean verificaParoleMarca(String mid, String parola) throws IllegalArgumentException, InvocationTargetException, Exception{
        setMID(mid);
        setPAROLA(parola.toUpperCase().trim());
        Tb_parolaResult tavola = new Tb_parolaResult(this);


    	tavola.executeCustom("selectParolaMarca");
        List v  = tavola.getElencoRisultati();

        return v.size()>0;
	}

   /** metodo per inserire un vettore di parole nella tabella tb_parola
 * @throws InfrastructureException
    *
   **/
   public void inserisciParola(String mid, String parola,String user) throws EccezioneDB, InfrastructureException {
       Tb_parola tb = new Tb_parola();



       tb.setMID(mid);
       tb.setPAROLA(parola);
       tb.setUTE_INS(user);
       tb.setUTE_VAR(user);
       tb.setFL_CANC(" ");
		Progressivi progress = new Progressivi();
        tb.setID_PAROLA(Long.parseLong(progress.getNextIdParola()));
		Tb_parolaResult tb_parolaResult = new Tb_parolaResult(tb);
		tb_parolaResult = new Tb_parolaResult(tb);
	    tb_parolaResult.insert(this);


   }

	public void eliminaParole(String mid, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		List vettoreDiParole = null;
		vettoreDiParole = cercaParoleDiMarca(mid);
		Tb_parola parola;
		for (int i=0;i<vettoreDiParole.size();i++){
			parola = (Tb_parola) vettoreDiParole.get(i);
			Tb_parolaResult tavola = new Tb_parolaResult(parola);
			tavola.executeCustom("delete");
			//cancellaParola(parola,user);
		}
	}


	private void cancellaParola(Tb_parola parola, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		parola.setFL_CANC("S");
		parola.setUTE_VAR(user);
        Tb_parolaResult tb_parolaResult = new Tb_parolaResult(parola);
    	tb_parolaResult.executeCustom("updatePerCancella");
	}



}
