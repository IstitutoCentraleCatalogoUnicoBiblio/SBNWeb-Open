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
package it.finsiel.sbn.polo.dao.common.tavole;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Tb_classeCommonDao extends TableDao {

/*
    /*
     Criteri di tipo univoco generalizzati
    public static final String XXXcd_paese = "XXXcd_paese";
    public static final String XXXcd_paese_std = "XXXcd_paese_std";
    public static final String XXXlivello_aut_da = "XXXlivello_aut_da";
    public static final String XXXlivello_aut_a = "XXXlivello_aut_a";
    public static final String XXXdata_var_Da = "XXXdata_var_Da";
    public static final String XXXdata_var_A = "XXXdata_var_A";
    public static final String XXXdataInizio_Da = "XXXdataInizio_Da";
    public static final String XXXdataInizio_A = "XXXdataInizio_A";
    public static final String XXXdata_ins_Da = "XXXdata_ins_Da";
    public static final String XXXdata_ins_A = "XXXdata_ins_A";
    public static final String XXXdataInizioDa = "XXXdataInizioDa";
    public static final String XXXdataInizioA = "XXXdataInizioA";
    public static final String XXXdataFine_Da = "XXXdataFine_Da";
    public static final String XXXdataFine_A = "XXXdataFine_A";
    public static final String XXXdataFineDa = "XXXdataFineDa";
    public static final String XXXdataFineA = "XXXdataFineA";
    public static final String XXXaa_morte = "XXXaa_morte";
    public static final String XXXaa_nascita = "XXXaa_nascita";
    public static final String XXXute_var = "XXXute_var";
    public static final String XXXute_ins = "XXXute_ins";
    public static final String XXXlivello = "XXXlivello";
    public static final String XXXlivello_auf = "XXXlivello_auf";
    public static final String XXXlivello_lav = "XXXlivello_lav";
    public static final String XXXlivello_sup = "XXXlivello_sup";
    public static final String XXXlivello_max = "XXXlivello_max";
    public static final String XXXlivello_med = "XXXlivello_med";
    public static final String XXXlivello_min = "XXXlivello_min";
    public static final String XXXlivello_rec = "XXXlivello_rec";


     */






    protected boolean kycleslike = false;

    public Tb_classeCommonDao() {
        super();
    }
    public Tb_classeCommonDao(Criteria classeCriteria) {
        super();
        this.basicCriteria = classeCriteria;
    }

    /*
    <opzionale dipende="XXXparola1"> AND CONTAINS(ds_classe, XXXparola1 ) > 0 </opzionale>
    <opzionale dipende="XXXparola2"> AND CONTAINS(ds_classe, XXXparola2 ) > 0 </opzionale>
    <opzionale dipende="XXXparola3"> AND CONTAINS(ds_classe, XXXparola3 ) > 0 </opzionale>
    <opzionale dipende="XXXparola4"> AND CONTAINS(ds_classe, XXXparola4 ) > 0 </opzionale>

    */
    public void setParola(HashMap opzioni) throws InfrastructureException {

	   	if(isSessionPostgreSQL()){
	   		String appo = null;
			if(opzioni.containsKey(TableDao.XXXparola1)){
		   		appo = "tidx_vector  @@ to_tsquery(";
		   		if (isPostgresVersion83())
		   			appo += "'pg_catalog.italian', ";
		   		else
		   			appo += "'default', ";
	        	appo = appo + " '" + opzioni.get(TableDao.XXXparola1);
	            opzioni.remove(TableDao.XXXparola1);
	        }
	        if(opzioni.containsKey(TableDao.XXXparola2)){
	        	appo = appo + "&" + opzioni.get(TableDao.XXXparola2);
	            opzioni.remove(TableDao.XXXparola2);
	        }
	        if(opzioni.containsKey(TableDao.XXXparola3)){
	        	appo = appo + "&" + opzioni.get(TableDao.XXXparola3);
	            opzioni.remove(TableDao.XXXparola3);
	        }
	        if(opzioni.containsKey(TableDao.XXXparola4)){
	        	appo = appo + "&" + opzioni.get(TableDao.XXXparola4);
	            opzioni.remove(TableDao.XXXparola4);
	        }
	        if(appo != null){
	        	appo = appo + "')";
	        	this.basicCriteria.add(Restrictions.sqlRestriction(appo));
	        }
	   	}else{
	        if(opzioni.containsKey(TableDao.XXXparola1)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_CLASSE, ? ) > 0",opzioni.get(TableDao.XXXparola1), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(TableDao.XXXparola2)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_CLASSE, ? ) > 0",opzioni.get(TableDao.XXXparola2), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(TableDao.XXXparola3)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_CLASSE, ? ) > 0",opzioni.get(TableDao.XXXparola3), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(TableDao.XXXparola4)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_CLASSE, ? ) > 0",opzioni.get(TableDao.XXXparola4), Hibernate.STRING));
	        }
	   	}
    }
    /*
    <opzionale dipende="XXXsistema"> AND cd_sistema = XXXsistema </opzionale>
    */
   public void setCdSistema(HashMap opzioni) throws Exception {

	   String cdSistema = ValidazioneDati.trimOrEmpty((String) opzioni.get(Tb_classeCommonDao.XXXcd_sistema));
	   String cdEdizione = ValidazioneDati.trimOrEmpty((String) opzioni.get(Tb_classeCommonDao.XXXcd_edizione));

	   //almaviva5_20141117
	   if (ValidazioneDati.isFilled(cdEdizione)) {
		   cdEdizione = Decodificatore.convertUnimarcToSbn("ECLA", cdEdizione);
	        if (!ValidazioneDati.isFilled(cdEdizione))
	        	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
		}

	   cdSistema += cdEdizione;

	   if (!ValidazioneDati.isFilled(cdSistema))
		   return;

       Criterion value;
       Parameter param = new Parameter();
       param.setKey("CD_SISTEMA");
       param.setValueString(cdSistema);

       // IL CODICE CD_SISTEMA é DIVENTATO DI TRE QUINDI MODIFICO TUTTE LE CHIAMATE CON UGUALE A LIKE
       // IN QUANTO NON é COMPRESA L'EDIZIONE VERIFICARE IL CORRETTO FUNZIONAMENTO almaviva
       //if ((value = this.setParameterEq(param)) != null) {
		if ((value = this.setParameterLikeEnd(param)) != null) {
			this.basicCriteria.add(value);
			if (ValidazioneDati.equals(cdSistema, "D") )
				//almaviva5_20110829 fix per sistemi non dewey che iniziano per 'D' senza filtro edizione
				this.basicCriteria.add(Restrictions.sqlRestriction("( substring(cd_sistema, 2, 3)=cd_edizione )"));
		}
   }





   /*
   <opzionale dipende="XXXdata_inizio_inserimento"> AND to_char(ts_ins,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_inserimento </opzionale>
   <opzionale dipende="XXXdata_fine_inserimento"> AND to_char(ts_ins,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_inserimento </opzionale>

   DA VERIFICARE
   <opzionale dipende="XXXdata_inizio_inserimento"> AND ts_ins  &gt;= to_timestamp(XXXdata_inizio_inserimento ||'000000.0','dd/mm/yyyyHH24miss.FF')</opzionale>
   <opzionale dipende="XXXdata_fine_inserimento"> AND ts_ins &lt;= to_timestamp(XXXdata_fine_inserimento ||'235959.0','dd/mm/yyyyHH24miss.FF')</opzionale>

    */
   // TODO Funzione da rivedere Date
   public void setDataInizioInserimento(HashMap opzioni) throws ParseException {
       Criterion value;
       Parameter param0;
       Parameter param1;

       param0 = new Parameter();
       param0.setKey("TS_INS");
       param0.setValueDate(opzioni,Tb_classeCommonDao.XXXdata_inizio_inserimento,"dd/mm/yyyy");

       param1 = new Parameter();
       param1.setKey("TS_INS");
       param1.setValueDate(opzioni,Tb_classeCommonDao.XXXdata_fine_inserimento,"dd/mm/yyyy");

       if((value=setParameterDaA(param0,param1))!= null){
           this.basicCriteria.add(value);
       }
   }

   /*
   <opzionale dipende="XXXcd_edizione"> AND cd_edizione = XXXcd_edizione</opzionale>
   */
	public void setCdEdizione(HashMap opzioni) throws Exception {

		// almaviva5_20141117 edizioni ridotte
		HashMap params = (HashMap) opzioni.clone();
		String cdEdizione = ValidazioneDati.trimOrEmpty((String) params.get(Tb_classeCommonDao.XXXcd_edizione));
		if (ValidazioneDati.isFilled(cdEdizione)) {
			cdEdizione = Decodificatore.convertUnimarcToSbn("ECLA", cdEdizione);
			if (!ValidazioneDati.isFilled(cdEdizione))
				throw new EccezioneSbnDiagnostico(7022); // edizione non esistente
			params.put(Tb_classeCommonDao.XXXcd_edizione, cdEdizione);
		}

		Criterion value;
		Parameter param = new Parameter();
		param.setKey("CD_EDIZIONE");
		param.setValueString(params, Tb_classeCommonDao.XXXcd_edizione);

		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}


  /*

  <opzionale dipende="XXXclasse_inizio">  AND  classe &gt;= UPPER(XXXclasse_inizio) AND classe &lt; UPPER(XXXclasse_fine)</opzionale>

   */
  // TODO Funzione a cui inserire l'UPPER
  public void setClasseDaA(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param0;
      Parameter param1;

      param0 = new Parameter();
      param0.setKey("CLASSE");
      param0.setValueString(opzioni,Tb_classeCommonDao.XXXclasse_inizio);

      param1 = new Parameter();
      param1.setKey("CLASSE");
      param1.setValueString(opzioni,Tb_classeCommonDao.XXXclasse_fine);

      if((value=setParameterDaA(param0,param1))!= null){
          this.basicCriteria.add(value);
      }
  }
    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_classeCommonDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
            for(int index =0; index<metodi.length; index++){
                if(metodi[index].getName().startsWith("set")){
                        metodi[index].invoke(this,new Object[] { opzioni});
                    //log.debug(metodi[index].getName());
                }
            }
        super.createCriteria(opzioni);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new InfrastructureException(e);
        }
    }

    public static void main(String[] args) throws Exception{
        Tb_classeCommonDao aut = new Tb_classeCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

}














