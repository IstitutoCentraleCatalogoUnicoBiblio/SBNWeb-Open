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
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


public class Tb_descrittoreCommonDao extends TableDao {
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

    /*
        <opzionale dipende="XXXparola1"> AND CONTAINS(ds_descrittore, XXXparola1 ) > 0 </opzionale>
        <opzionale dipende="XXXparola2"> AND CONTAINS(ds_descrittore, XXXparola2 ) > 0 </opzionale>
        <opzionale dipende="XXXparola3"> AND CONTAINS(ds_descrittore, XXXparola3 ) > 0 </opzionale>
        <opzionale dipende="XXXparola4"> AND CONTAINS(ds_descrittore, XXXparola4 ) > 0 </opzionale>
        <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
        <opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
        <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
        <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
        <opzionale dipende="XXXdata_ins_Da"> AND ts_ins &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
        <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>
        <opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
    */





    protected boolean kycleslike = false;

    public Tb_descrittoreCommonDao() {
        super();
    }
    public Tb_descrittoreCommonDao(Criteria descrittoreCriteria) {
        super();
        this.basicCriteria = descrittoreCriteria;
    }

    /*
    <opzionale dipende="XXXparola1"> AND CONTAINS(ds_descrittore, XXXparola1 ) > 0 </opzionale>
    <opzionale dipende="XXXparola2"> AND CONTAINS(ds_descrittore, XXXparola2 ) > 0 </opzionale>
    <opzionale dipende="XXXparola3"> AND CONTAINS(ds_descrittore, XXXparola3 ) > 0 </opzionale>
    <opzionale dipende="XXXparola4"> AND CONTAINS(ds_descrittore, XXXparola4 ) > 0 </opzionale>

    */
    public void setParola(HashMap opzioni) throws InfrastructureException {
	   	if(isSessionPostgreSQL()){
	   		String appo = null;
			if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola1)){
		   		appo = "tidx_vector  @@ to_tsquery(";
		   		if (isPostgresVersion83())
		   			appo += "'pg_catalog.italian', ";
		   		else
		   			appo += "'default', ";
	        	appo = appo + " '" + opzioni.get(Tb_descrittoreCommonDao.XXXparola1);
	            opzioni.remove(Tb_descrittoreCommonDao.XXXparola1);
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola2)){
	        	appo = appo + "&" + opzioni.get(Tb_descrittoreCommonDao.XXXparola2);
	            opzioni.remove(Tb_descrittoreCommonDao.XXXparola2);
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola3)){
	        	appo = appo + "&" + opzioni.get(Tb_descrittoreCommonDao.XXXparola3);
	            opzioni.remove(Tb_descrittoreCommonDao.XXXparola3);
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola4)){
	        	appo = appo + "&" + opzioni.get(Tb_descrittoreCommonDao.XXXparola4);
	            opzioni.remove(Tb_descrittoreCommonDao.XXXparola4);
	        }
	        if(appo != null){
	        	appo = appo + "')";
	        	this.basicCriteria.add(Restrictions.sqlRestriction(appo));
	        }
	   	}else{

	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola1)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_DESCRITTORE, ? ) > 0",opzioni.get(Tb_descrittoreCommonDao.XXXparola1), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola2)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_DESCRITTORE, ? ) > 0",opzioni.get(Tb_descrittoreCommonDao.XXXparola2), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola3)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_DESCRITTORE, ? ) > 0",opzioni.get(Tb_descrittoreCommonDao.XXXparola3), Hibernate.STRING));
	        }
	        if(opzioni.containsKey(Tb_descrittoreCommonDao.XXXparola4)){
	            this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_DESCRITTORE, ? ) > 0",opzioni.get(Tb_descrittoreCommonDao.XXXparola4), Hibernate.STRING));
	        }
	   	}
    }









   /*
   <opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
   */
  public void setCdSoggetto(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("CD_SOGGETTARIO");
      param.setValueString(opzioni,Tb_descrittoreCommonDao.XXXcd_soggettario);
      if ((value = this.setParameterEq(param)) != null) {
          this.basicCriteria.add(value);
      }
  }


  public void setCdEdizioneInClause(HashMap opzioni) throws Exception {
  	//almaviva5_20111128 evolutive CFI
  	SbnEdizioneSoggettario cd_edizione = (SbnEdizioneSoggettario) opzioni.get(Tb_soggettoCommonDao.XXXcd_edizione_IN);
  	if (cd_edizione == null)
  		return;

  	this.basicCriteria.add(Restrictions.in("CD_EDIZIONE", SoggettoValida.getEdizioniRicerca(cd_edizione)));

  }


	public void setCdEdizione(HashMap opzioni) throws Exception {
		// almaviva5_20111130 evolutive CFI
		SbnEdizioneSoggettario cd_edizione = (SbnEdizioneSoggettario) opzioni.get(Tb_soggettoCommonDao.XXXcd_edizione_IN);
    	if (cd_edizione != null)
    		return;

		Criterion value;
		Parameter param = new Parameter();
		param.setKey("CD_EDIZIONE");
		param.setValueString(opzioni, Tb_descrittoreCommonDao.XXXcd_edizione);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}

	public void setCatTermine(HashMap opzioni) throws Exception {
		// almaviva5_20120512 evolutive CFI
		Criterion value;
		Parameter param = new Parameter();
		param.setKey("CAT_TERMINE");
		param.setValueString(opzioni, KeyParameter.XXXcat_termine);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}


	public void setFiltroSoggInBib(HashMap opzioni) throws InfrastructureException {

		//almaviva5_20091117 filtro sui soli sogg. definiti in biblioteca
		SbnUserType user = (SbnUserType) opzioni.get(XXXcercaPerFiltroSoggInBib);
		if (user == null)
			return;

		String codPolo = user.getBiblioteca().substring(0, 3);
		String codBib  = user.getBiblioteca().substring(3);

		StringBuilder sql = new StringBuilder(512);
		sql.append("{alias}.CD_SOGGETTARIO in (");
		sql.append(" select sogg.cd_sogg from tr_soggettari_biblioteche sogg");
		sql.append(" where sogg.fl_canc<>'S'");
		sql.append(" and sogg.cd_polo='").append(codPolo).append("' and sogg.cd_biblioteca='").append(codBib).append("'");
		sql.append(" and sogg.cd_sogg={alias}.CD_SOGGETTARIO");
		sql.append(")");

		basicCriteria.add(Restrictions.sqlRestriction(sql.toString()));

		opzioni.remove(XXXcercaPerFiltroSoggInBib);
	}

    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_descrittoreCommonDao.class;//this.getClass();
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
        Tb_descrittoreCommonDao aut = new Tb_descrittoreCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }

}














