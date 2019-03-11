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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Tb_composizioneCommonDao extends TableDao {


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


        // CAMPI DELLE VISTE (
            <!-- inizio di Tb_composizioneCommonDao -->
            <opzionale dipende="XXXformaComposizione1"> AND cd_forma_1 = XXXformaComposizione1 </opzionale>
            <opzionale dipende="XXXformaComposizione2"> AND cd_forma_2 = XXXformaComposizione2 </opzionale>
            <opzionale dipende="XXXformaComposizione3"> AND cd_forma_3 = XXXformaComposizione3 </opzionale>
            <opzionale dipende="XXXorganicoSinteticoComposizione"> AND ds_org_sint like XXXorganicoSinteticoComposizione </opzionale>
            <opzionale dipende="XXXorganicoAnaliticoComposizione"> AND ds_org_anal like XXXorganicoAnaliticoComposizione </opzionale>
            <opzionale dipende="XXXtonalita"> AND cd_tonalita = XXXtonalita </opzionale>
             DA VERIFICARE <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine = XXXnumeroOrdine </opzionale>
             DA VERIFICARE <opzionale dipende="XXXnumeroOpera"> AND numero_opera = XXXnumeroOpera </opzionale>
                           POSSONO ANCHE ANDARE IN LIKE
                           <opzionale dipende="XXXnumeroOpera"> AND numero_opera like '%' || XXXnumeroOpera || '%' </opzionale>
                           <opzionale dipende="XXXnumeroCatalogo"> AND numero_cat_tem = XXXnumeroCatalogo </opzionale>

            <opzionale dipende="XXXnumeroCatalogo"> AND numero_cat_tem = XXXnumeroCatalogo </opzionale>
            <opzionale dipende="XXXtitoloOrdinamento"> AND ky_ord_ric like XXXtitoloOrdinamento || '%' </opzionale>
            <opzionale dipende="XXXtitoloEstratto"> AND ky_est_ric like XXXtitoloEstratto || '%' </opzionale>
            <opzionale dipende="XXXappellativo"> AND ky_app_ric like XXXappellativo || '%' </opzionale>
            DEFAULT <opzionale dipende="XXXdataInizioDa"> AND aa_comp_1 &gt;= XXXdataInizioDa </opzionale>
            DEFAULT <opzionale dipende="XXXdataInizioA"> AND aa_comp_1 &lt;= XXXdataInizioA </opzionale>
            DEFAULT <opzionale dipende="XXXdataFineDa"> AND aa_comp_2 &gt;= XXXdataFineDa </opzionale>
            DEFAULT <opzionale dipende="XXXdataFineA"> AND aa_comp_2 &lt;= XXXdataFineA </opzionale>

            <opzionale dipende="XXXtitoloOrdinamentoLungo"> AND ky_ord_nor_pre like XXXtitoloOrdinamentoLungo || '%' </opzionale>
            <opzionale dipende="XXXtitoloEstrattoLungo"> AND ky_est_nor_pre like XXXtitoloEstrattoLungo || '%' </opzionale>
            <opzionale dipende="XXXappellativoLungo"> AND ky_app_nor_pre like XXXappellativoLungo || '%' </opzionale>
            <opzionale dipende="XXXdata_composizione">AND  aa_comp_1 &gt;= UPPER(XXXdata_composizione)
                                                      AND  aa_comp_2 &lt; UPPER(XXXdata_composizione) </opzionale>


    */


    protected boolean kycleslike = false;

    public Tb_composizioneCommonDao() {
        super();
    }

    public Tb_composizioneCommonDao(Criteria composizioneCriteria) {
        super();
        this.basicCriteria = composizioneCriteria;
    }


    /*
    <opzionale dipende="XXXdata_composizione">AND  aa_comp_1 &gt;= UPPER(XXXdata_composizione)
    AND  aa_comp_2 &lt; UPPER(XXXdata_composizione) </opzionale>
    */
   private void setDataComposizione(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param0;
       Parameter param1;
       param0 = new Parameter();
       param0.setKey("AA_COMP_1");
       param0.setValueString(opzioni, Tb_composizioneCommonDao.XXXdata_composizione);
       param1 = new Parameter();
       param1.setKey("AA_COMP_2");
       param1.setValueString(opzioni, Tb_composizioneCommonDao.XXXdata_composizione);


       if((value = this.setParameterDaA(param0,param1))!= null){
           this.basicCriteria.add(value);
           opzioni.remove(Tb_composizioneCommonDao.XXXdata_composizione);
       }
   }





    /*
     AND ky_ord_nor_pre like XXXtitoloOrdinamentoLungo || '%'
     */
    private void setTitoloOrdinamentoLungo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_ORD_NOR_PRE");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXtitoloOrdinamentoLungo);

        if((value = this.setParameterLikeEnd(param))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXtitoloOrdinamentoLungo);
        }
    }


    /*
     AND ky_est_nor_pre like XXXtitoloEstrattoLungo || '%'
     */
    private void setTitoloEstrattoLungo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_EST_NOR_PRE");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXtitoloEstrattoLungo);
        if((value = this.setParameterLikeEnd(param))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXtitoloEstrattoLungo);
        }
    }
    /*
      AND ky_app_nor_pre like XXXappellativoLungo || '%'
     */
    private void setAppellativoLungo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_APP_NOR_PRE");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXappellativoLungo);
        if((value = this.setParameterLikeEnd(param))!= null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXappellativoLungo);
        }
    }

    /*
    <opzionale dipende="XXXultima_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF') &lt; XXXultima_variazione</opzionale>
    */
    // TODO Funzione da rivedere Date
    public void setUltimaVariazione(HashMap opzioni) throws ParseException {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("TS_VAR");
       param.setValueDate(opzioni,Tb_composizioneCommonDao.XXXultima_variazione,"yyyymmddhh24miss.FF");

       if((value=setParameterGe(param))!= null){
           this.basicCriteria.add(value);
           this.basicCriteria.add(Restrictions.eqProperty("TS_INS","TS_VAR"));
           opzioni.remove(Tb_composizioneCommonDao.XXXultima_variazione);
       }
    }


    /*
    <opzionale dipende="XXXlettera"> AND ky_ord_ric like XXXlettera || '%' </opzionale>
     */

    public void setLettera(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_ORD_RIC");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXlettera);

        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXlettera);
        }
    }
/*



     DA VERIFICARE <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine = XXXnumeroOrdine </opzionale>
     DA VERIFICARE <opzionale dipende="XXXnumeroOpera"> AND numero_opera = XXXnumeroOpera </opzionale>
                   POSSONO ANCHE ANDARE IN LIKE Quindi Faccio l'overwrite nelle vistre corrispondenti
                   <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine like '%' || XXXnumeroOrdine || '%' </opzionale>
                   <opzionale dipende="XXXnumeroOpera"> AND numero_opera like '%' || XXXnumeroOpera || '%' </opzionale>




*/
    /*
    <opzionale dipende="XXXnumeroOpera"> AND numero_opera = XXXnumeroOpera </opzionale>
     */
    public void setNumeroOpera(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("NUMERO_OPERA");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXnumeroOpera);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXnumeroOpera);
        }
    }

    /*
    <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine = XXXnumeroOrdine </opzionale>
     */
    public void setNumeroOrdine(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("NUMERO_ORDINE");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXnumeroOrdine);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXnumeroOrdine);
        }
    }

    /*
    <opzionale dipende="XXXappellativo"> AND ky_app_ric like XXXappellativo || '%' </opzionale>
     */
    public void setAppellativo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_APP_RIC");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXappellativo);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXappellativo);
        }
    }

    /*
    <opzionale dipende="XXXtitoloEstratto"> AND ky_est_ric like XXXtitoloEstratto || '%' </opzionale>
     */
    public void setTitoloEstratto(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_EST_RIC");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXtitoloEstratto);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXtitoloEstratto);
        }
    }

    /*
    <opzionale dipende="XXXtitoloOrdinamento"> AND ky_ord_ric like XXXtitoloOrdinamento || '%' </opzionale>
     */
    public void setTitoloOrdinamento(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_ORD_RIC");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXtitoloOrdinamento);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXtitoloOrdinamento);
        }
    }

    /*
    <opzionale dipende="XXXnumeroCatalogo"> AND numero_cat_tem = XXXnumeroCatalogo </opzionale>
     */
    public void setNumeroCatalogo(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("NUMERO_CAT_TEM");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXnumeroCatalogo);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXnumeroCatalogo);
        }
    }

    /*
    <opzionale dipende="XXXtonalita"> AND cd_tonalita = XXXtonalita </opzionale>
     */
    public void setTonalita(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_TONALITA");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXtonalita);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXtonalita);
        }
    }

    /*
    <opzionale dipende="XXXorganicoSinteticoComposizione"> AND ds_org_sint like XXXorganicoSinteticoComposizione </opzionale>
     */
    public void setOrganicoSinteticoComposizione(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("DS_ORG_SINT");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXorganicoSinteticoComposizione);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXorganicoSinteticoComposizione);
        }
    }

    /*
    <opzionale dipende="XXXorganicoAnaliticoComposizione"> AND ds_org_anal like XXXorganicoAnaliticoComposizione </opzionale>
     */
    public void setOrganicoAnaliticoComposizione(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("DS_ORG_ANAL");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXorganicoAnaliticoComposizione);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXorganicoAnaliticoComposizione);
        }
    }


    /*
    <opzionale dipende="XXXformaComposizione1"> AND cd_forma_1 = XXXformaComposizione1 </opzionale>
     */
    public void setFormaComposizione1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_FORMA_1");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXformaComposizione1);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXformaComposizione1);
        }
    }

    /*
    <opzionale dipende="XXXformaComposizione2"> AND cd_forma_2 = XXXformaComposizione2 </opzionale>
     */
    public void setFormaComposizione2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_FORMA_2");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXformaComposizione2);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXformaComposizione2);
        }
    }
    /*
    <opzionale dipende="XXXformaComposizione3"> AND cd_forma_3 = XXXformaComposizione3 </opzionale>
     */
    public void setFormaComposizione3(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_FORMA_3");
        param.setValueString(opzioni,Tb_composizioneCommonDao.XXXformaComposizione3);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Tb_composizioneCommonDao.XXXformaComposizione3);
        }
    }
    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Tb_composizioneCommonDao.class;//this.getClass();
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
        Tb_composizioneCommonDao aut = new Tb_composizioneCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}
