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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

public abstract class TableDao extends BasicTableDao {

	private static final class NotEmptyPropertySelector implements PropertySelector {

		private static final long serialVersionUID = 2380929617591484126L;

		public boolean include(Object object, String propertyName, Type type) {
			if (object == null)
				return false;
			if ((object instanceof Number)
					&& ((Number) object).longValue() == 0L)
				return false;
			if ((object instanceof Character)
					&& ((Character) object).charValue() == 0)
				return false;
			if ((object instanceof String)
					&& ((String) object).trim().equals(""))
				return false;
			return true;
		}

	}

	protected static final PropertySelector NOT_EMPTY = new NotEmptyPropertySelector();

    public TableDao() {
		super();
	}

    public TableDao(Criteria c) {
		super();
		this.basicCriteria = c;
	}

	public Criteria getCriteria() {
		return this.basicCriteria;
	}

    public Class<? extends OggettoServerSbnMarc> getTarget() {
    	return OggettoServerSbnMarc.class;
    }

    /*
    <opzionale dipende="XXXds_fondo"> AND UPPER(ds_fondo) = UPPER(XXXds_fondo)</opzionale>
    // Manutenzione del 26.06.2012 BUG MANTIS 5039 (collaudo) la select contiene un AND di troppo quindi l'interrogazione per
    // biblioteca/fondo va in errore
    */
    public void setDsFondo(HashMap opzioni)  throws Exception {
        if(opzioni.containsKey(TableDao.XXXds_fondo)){
//            this.basicCriteria.add(Restrictions.sqlRestriction("AND UPPER(ds_fondo) = UPPER(?) ",opzioni.get(TableDao.XXXds_fondo), Hibernate.STRING));
        	this.basicCriteria.add(Restrictions.sqlRestriction(" UPPER(ds_fondo) = UPPER(?) ",opzioni.get(TableDao.XXXds_fondo), Hibernate.STRING));
            opzioni.remove(TableDao.XXXds_fondo);
        }
    }


    /*
     <opzionale dipende="XXXds_segn"> AND UPPER(ds_segn) like UPPER(XXXds_segn)</opzionale>
    */
    public void setDsSegn(HashMap opzioni)  throws Exception {
        if(opzioni.containsKey(TableDao.XXXds_segn)){
            this.basicCriteria.add(Restrictions.sqlRestriction("AND UPPER(ds_segn) like UPPER(?) ",opzioni.get(TableDao.XXXds_segn), Hibernate.STRING));
            opzioni.remove(TableDao.XXXds_segn);
        }
    }

    /*<opzionale dipende="XXXsici"> AND sici like '%' || XXXsici || '%' </opzionale>*/
    public void setSici(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("SICI");
        param.setValueString(opzioni, TableDao.XXXsici);

        if ((value = this.setParameterLikeAnywhere(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXsici);
        }
    }

    /*
    <opzionale dipende="XXXnomeLike"> AND (  UPPER(ky_cles1_t) || UPPER(ky_cles2_t)  LIKE UPPER( XXXnomeLike)||'%' OR   UPPER(ky_cles1_ct) || UPPER(ky_cles2_ct)  LIKE UPPER( XXXnomeLike)||'%'   )  </opzionale>
    */
    private void setNomeLike(HashMap opzioni) {
        if(opzioni.containsKey("XXXnomeLike")){
            this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(ky_cles1_t) || UPPER(ky_cles2_t)  LIKE UPPER(?) ||'%' OR  UPPER(ky_cles1_ct) || UPPER(ky_cles2_ct)  LIKE UPPER(?)||'%'",opzioni.get("XXXnomeLike"), Hibernate.STRING));
        }
    }

    /*
    <opzionale dipende="XXXnomeEsatto"> AND ( UPPER(ky_cles1_t) || UPPER(ky_cles2_t)  = UPPER( XXXnomeEsatto) OR UPPER(ky_cles1_ct) || UPPER(ky_cles2_ct)  = UPPER (XXXnomeEsatto) ) </opzionale>
    */
    private void setNomeEsatto(HashMap opzioni) {
        if(opzioni.containsKey("XXXnomeEsatto")){
            this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(ky_cles1_t) || UPPER(ky_cles2_t)  = UPPER(?) OR UPPER(ky_cles1_ct) || UPPER(ky_cles2_ct)  = UPPER (?)'",opzioni.get("XXXnomeEsatto"),Hibernate.STRING));
        }
    }

    /*
        <opzionale dipende="XXXnumeroLastraDa"> AND numero_lastra &gt;= XXXnumeroLastraDa </opzionale>
        <opzionale dipende="XXXnumeroLastraA"> AND numero_lastra &lt;= XXXnumeroLastraA </opzionale>
     */
    private void setNumeroLastra(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("NUMERO_LASTRA");
        param0.setValueInteger(opzioni, TableDao.XXXnumeroLastraDa);
        param1 = new Parameter();
        param1.setKey("NUMERO_LASTRA");
        param1.setValueInteger(opzioni, TableDao.XXXnumeroLastraA);

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXnumeroLastraDa);
            opzioni.remove(TableDao.XXXnumeroLastraA);
        }
    }

    /*
    <opzionale dipende="XXXcd_natura_base"> AND  cd_natura_base =XXXcd_natura_base </opzionale>
    */
   private void setCdNaturaBase(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("CD_NATURA_BASE");
       param.setValueString(opzioni, TableDao.XXXcd_natura_base);

       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXcd_natura_base);
       }
   }
   /*
   <opzionale dipende="XXXtp_record"> AND  tp_record_uni = XXXtp_record</opzionale>
   */
  private void setTpRecord(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("TP_RECORD_UNI");
      param.setValueString(opzioni, TableDao.XXXtp_record);

      if ((value = this.setParameterEq(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(TableDao.XXXtp_record);
      }
  }
  /*
  <opzionale dipende="XXXnota_numero_std"> AND nota_numero_std =XXXnota_numero_std</opzionale>
  */
 private void setNotaNumeroSTD(HashMap opzioni) throws Exception {
     Criterion value;
     Parameter param = new Parameter();
     param.setKey("NOTA_NUMERO_STD");
     param.setValueString(opzioni, TableDao.XXXnota_numero_std);

     if ((value = this.setParameterEq(param)) != null) {
         this.basicCriteria.add(value);
         opzioni.remove(TableDao.XXXnota_numero_std);
     }
 }
 /*
 <opzionale dipende="XXXaa_pubb_1"> AND aa_pubb_1 = XXXaa_pubb_1</opzionale>
 */
private void setAaPubb1(HashMap opzioni) throws Exception {
    Criterion value;
    Parameter param = new Parameter();
    param.setKey("AA_PUBB_1");
    param.setValueString(opzioni, TableDao.XXXaa_pubb_1);

    if ((value = this.setParameterEq(param)) != null) {
        this.basicCriteria.add(value);
        opzioni.remove(TableDao.XXXaa_pubb_1);
    }
}

/*
<opzionale dipende="XXXky_cles1_t_uguale"> AND  ky_cles1_t = XXXky_cles1_t_uguale </opzionale>
*/
private void setKyCles1TUguale(HashMap opzioni) throws Exception {
   Criterion value;
   Parameter param = new Parameter();
   param.setKey("KY_CLES1_T");
   param.setValueString(opzioni, TableDao.XXXky_cles1_t_uguale);

   if ((value = this.setParameterEq(param)) != null) {
       this.basicCriteria.add(value);
       opzioni.remove(TableDao.XXXky_cles1_t_uguale);
   }
}

/*
<opzionale dipende="XXXky_cles2_t_uguale"> AND  ky_cles2_t = XXXky_cles2_t_uguale </opzionale>
*/
private void setKyCles2TUguale(HashMap opzioni) throws Exception {
   Criterion value;
   Parameter param = new Parameter();
   param.setKey("KY_CLES2_T");
   param.setValueString(opzioni, TableDao.XXXky_cles2_t_uguale);

   if ((value = this.setParameterEq(param)) != null) {
       this.basicCriteria.add(value);
       opzioni.remove(TableDao.XXXky_cles2_t_uguale);
   }
}

/*
<opzionale dipende="XXXky_clet1_t_uguale"> AND  ky_clet1_t = XXXky_clet1_t_uguale </opzionale>
*/
private void setKyClet1TUguale(HashMap opzioni) throws Exception {
   Criterion value;
   Parameter param = new Parameter();
   param.setKey("KY_CLET1_T");
   param.setValueString(opzioni, TableDao.XXXky_clet1_t_uguale);

   if ((value = this.setParameterEq(param)) != null) {
       this.basicCriteria.add(value);
       opzioni.remove(TableDao.XXXky_clet1_t_uguale);
   }
}

/*
<opzionale dipende="XXXky_clet2_t_uguale"> AND  ky_clet2_t = XXXky_clet2_t_uguale </opzionale>
*/
private void setKyClet2TUguale(HashMap opzioni) throws Exception {
    Criterion value;
    Parameter param = new Parameter();
    param.setKey("KY_CLET2_T");
    param.setValueString(opzioni, TableDao.XXXky_clet2_t_uguale);

    if ((value = this.setParameterEq(param)) != null) {
        this.basicCriteria.add(value);
        opzioni.remove(TableDao.XXXky_clet2_t_uguale);
    }
 }





    /*
    <opzionale dipende="XXXfl_canc"> AND fl_canc = XXXfl_canc </opzionale>
    */
   private void setFlCanc(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("FL_CANC");
       param.setValueString(opzioni, TableDao.XXXfl_canc);
       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXfl_canc);
       }
   }


    /*
    <opzionale dipende="XXXfl_allinea_sbnmarc"> AND fl_allinea_sbnmarc = XXXfl_allinea_sbnmarc </opzionale>
    */
   private void setFlAllineaSbnmarc(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("FL_ALLINEA_SBNMARC");
       param.setValueString(opzioni, TableDao.XXXfl_allinea_sbnmarc);

       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXfl_allinea_sbnmarc);
       }
   }

    /*
    <opzionale dipende="XXXlid_base"> AND lid_base = XXXlid_base </opzionale>
    */
   private void setLidBase(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("LID_BASE");
       param.setValueString(opzioni, TableDao.XXXlid_base);

       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXlid_base);
       }
   }

   /*
   <opzionale dipende="XXXlid_coll"> AND lid_coll = XXXlid_coll </opzionale>
   */
   private void setLidColl(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("LID_COLL");
        param.setValueString(opzioni, TableDao.XXXlid_coll);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXlid_coll);
        }
    }

    /*
    <opzionale dipende="XXXdid_base"> AND did_base = XXXdid_base</opzionale>
    */
   private void setDidBase(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("DID_BASE");
       param.setValueString(opzioni, TableDao.XXXdid_base);

       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXdid_base);
       }
   }

   /*
   <opzionale dipende="XXXdid_coll"> AND did_coll = XXXdid_coll</opzionale>
   */
   private void setDidColl(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("DID_COLL");
        param.setValueString(opzioni, TableDao.XXXdid_coll);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdid_coll);
        }
    }

   /*
     * <opzionale dipende="XXXdata_inizio_inserimento"> AND to_char(ts_ins,'dd/mm/yyyy') &gt;= XXXdata_inizio_inserimento </opzionale>
     */
    private void setVidDataInizioInserimento(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TS_INS");
        param.setValueDate(opzioni, TableDao.XXXdata_inizio_inserimento,"dd/mm/yyyy");
        if ((value = this.setParameterGe(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdata_inizio_inserimento);
        }
    }

  /*
  <opzionale dipende="XXXdata_fine_inserimento"> AND to_char(ts_ins,'dd/mm/yyyy') &lt;= XXXdata_fine_inserimento </opzionale>
  */
  private void setVidDataFineInserimento(HashMap opzioni) throws ParseException {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("TS_INS");
      param.setValueDate(opzioni, TableDao.XXXdata_fine_inserimento,"dd/mm/yyyy");
      if ((value = this.setParameterGe(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(TableDao.XXXdata_fine_inserimento);
      }
  }

      /*
      <opzionale dipende="XXXdata_inizio_variazione"> AND to_char(ts_var,'dd/mm/yyyy') &gt;= XXXdata_inizio_variazione </opzionale>
      */
     private void setVidDataInizioVariazione(HashMap opzioni) throws ParseException {
         Criterion value;
         Parameter param = new Parameter();
         param.setKey("TS_VAR");
         param.setValueDate(opzioni, TableDao.XXXdata_inizio_variazione,"dd/mm/yyyy");
         if ((value = this.setParameterGe(param)) != null) {
             this.basicCriteria.add(value);
             opzioni.remove(TableDao.XXXdata_inizio_variazione);
         }
     }

     /*
     <opzionale dipende="XXXdata_fine_variazione"> AND to_char(ts_var,'dd/mm/yyyy') &lt;= XXXdata_fine_variazione </opzionale>
     */
    private void setVidDataFineVariazione(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TS_VAR");
        param.setValueDate(opzioni, TableDao.XXXdata_inizio_variazione,"dd/mm/yyyy");
        if ((value = this.setParameterGe(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdata_fine_variazione);
        }
    }
     /*
     <opzionale dipende="XXXvid_coll"> AND vid_coll = XXXvid_coll </opzionale>
     */
    private void setVidCollegato(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("VID_COLL");
        param.setValueString(opzioni, TableDao.XXXvid_coll);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXvid_coll);
        }
    }

     /*
    <opzionale dipende="XXXvid_base"> AND vid_base = XXXvid_base </opzionale>
    */
   private void setVidBase(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("VID_BASE");
       param.setValueString(opzioni, TableDao.XXXvid_base);

       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXvid_base);
       }
   }

    /*


     AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a
     */
    private void setTsVarDaA(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni, TableDao.XXXts_var_da,"yyyy-mm-dd");
        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni, TableDao.XXXts_var_a,"yyyy-mm-dd");

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXts_var_da);
            opzioni.remove(TableDao.XXXts_var_a);
        }
    }

    /*
     <opzionale dipende="XXXts_var"> AND to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var </opzionale>
    */
    private void setTsVar(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TS_VAR");
        param.setValueDate(opzioni, TableDao.XXXts_var,"yyyymmddhh24miss.FF");
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXts_var);
        }
    }

    /*
     <opzionale dipende="XXXtp_legame"> AND tp_legame = XXXtp_legame </opzionale>
     */
    private void setTpLegame(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_LEGAME");
        param.setValueString(opzioni, TableDao.XXXtp_legame);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXtp_legame);
        }
    }

    /*
    <opzionale dipende="XXXtipo_coll"> AND tipo_coll = XXXtipo_coll </opzionale>
    */
   private void setTipoColl(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("TIPO_COLL");
       param.setValueString(opzioni, TableDao.XXXtipo_coll);
       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(TableDao.XXXtipo_coll);
       }
   }

    /*
    <opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese</opzionale>
     */
    public void setCdPaese(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_PAESE");
        param.setValueString(opzioni, TableDao.XXXcd_paese);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXcd_paese);
        }
    }

    /*
    <opzionale dipende="XXXcd_paese_std"> AND cd_paese_std = XXXcd_paese_std</opzionale>
     */
    public void setCdPaeseStd(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_PAESE_STD");
        param.setValueString(opzioni, TableDao.XXXcd_paese_std);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXcd_paese_std);
        }
    }

    /*
    <opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
    <opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>

     */
    public void setLivelloAut(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("CD_LIVELLO");
        param0.setValueString(opzioni, TableDao.XXXlivello_aut_da);
        param1 = new Parameter();
        param1.setKey("CD_LIVELLO");
        param1.setValueString(opzioni, TableDao.XXXlivello_aut_a);

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXlivello_aut_da);
            opzioni.remove(TableDao.XXXlivello_aut_a);
        }
    }

    /*
    <opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
    <opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
       NUOVA DA VERIFICARE
     <opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
     <opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
     */
    public void setDataVar(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni, TableDao.XXXdata_var_Da,"yyyy-mm-dd");
        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni, TableDao.XXXdata_var_A,"yyyy-mm-dd");

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdata_var_Da);
            opzioni.remove(TableDao.XXXdata_var_A);
        }
    }



    /*
    <opzionale dipende="XXXdataInizioDa"> AND aa_comp_1 &gt;= XXXdataInizioDa </opzionale>
    <opzionale dipende="XXXdataInizioA"> AND aa_comp_1 &lt;= XXXdataInizioA </opzionale>
     */
    public void setDataInizioDaA(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("AA_COMP_1");
        param0.setValueString(opzioni, TableDao.XXXdataInizioDa);
        param1 = new Parameter();
        param1.setKey("AA_COMP_1");
        param1.setValueString(opzioni, TableDao.XXXdataInizioA);

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdataInizioDa);
            opzioni.remove(TableDao.XXXdataInizioA);
        }
    }
// LE CHIAVI AA_COMP_1 E 2 SONO UTILIzzATE SU TB_COMPOSIIONE V_COMPOSIZIONE
//    public void setDataInizioDaA(HashMap opzioni) throws Exception {
//        Criterion value;
//        Parameter param0;
//        Parameter param1;
//        param0 = new Parameter();
//        param0.setKey("AA_COMP_1");
//        param0.setValueString(opzioni, TableDao.XXXdataInizioDa);
//        param1 = new Parameter();
//        param1.setKey("AA_COMP_1");
//        param1.setValueString(opzioni, TableDao.XXXdataInizioA);
//
//        if((value=setParameterDaA(param0,param1))!= null){
//            this.basicCriteria.add(value);
//            opzioni.remove(TableDao.XXXdataInizioDa);
//            opzioni.remove(TableDao.XXXdataInizioA);
//        }
//    }


    /*
    <opzionale dipende="XXXdataFineDa"> AND aa_comp_2 &gt;= XXXdataFineDa </opzionale>
    <opzionale dipende="XXXdataFineA"> AND aa_comp_2 &lt;= XXXdataFineA </opzionale>

     */
    public void setDataFineDaA(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("AA_COMP_2");
        param0.setValueString(opzioni, TableDao.XXXdataFineDa);
        param1 = new Parameter();
        param1.setKey("AA_COMP_2");
        param1.setValueString(opzioni, TableDao.XXXdataFineA);

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdataFineDa);
            opzioni.remove(TableDao.XXXdataFineA);
        }
    }


    /*
    <opzionale dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da </opzionale>
    <opzionale dipende="XXXdataInizio_A"> AND aa_nascita  &lt;= XXXdataInizio_A </opzionale>
     */
    public void setDataInizio_Da_A(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("AA_NASCITA");
        param0.setValueString(opzioni, TableDao.XXXdataInizio_Da);
        param1 = new Parameter();
        param1.setKey("AA_NASCITA");
        param1.setValueString(opzioni, TableDao.XXXdataInizio_A);

        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdataInizio_Da);
            opzioni.remove(TableDao.XXXdataInizio_A);
        }
    }

    /*

    <opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'dd/mm/yyyy') &gt;= XXXdata_ins_Da </opzionale>
    <opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>

    <opzionale dipende="XXXdata_ins_Da"> AND ts_ins  &gt;= to_date(XXXdata_ins_Da , 'yyyy-mm-dd') </opzionale>
    <opzionale dipende="XXXdata_ins_A"> AND ts_ins &lt;= to_date(XXXdata_ins_A , 'yyyy-mm-dd') </opzionale>

    <opzionale dipende="XXXdata_ins_Da"> AND ts_ins  &gt;= to_timestamp(XXXdata_ins_Da ||'000000.0','dd/mm/yyyyHH24miss.FF')</opzionale>
    <opzionale dipende="XXXdata_ins_A" > AND ts_ins &lt;= to_timestamp(XXXdata_ins_A ||'235959.0','dd/mm/yyyyHH24miss.FF') </opzionale>


     */
    public void setDataIns(HashMap opzioni) throws ParseException {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("TS_INS");
        param0.setValueDate(opzioni, TableDao.XXXdata_ins_Da,"dd/mm/yyyy");
        param1 = new Parameter();
        param1.setKey("TS_INS");
        param1.setValueDate(opzioni, TableDao.XXXdata_ins_A,"dd/mm/yyyy");
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdata_ins_Da);
            opzioni.remove(TableDao.XXXdata_ins_A);
        }
    }


    /*
    <opzionale dipende="XXXdataFine_Da"> AND aa_morte &gt;= XXXdataFine_Da </opzionale>
    <opzionale dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
     */
    public void setDataFine_Da_A(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param0;
        Parameter param1;
        param0 = new Parameter();
        param0.setKey("AA_MORTE");
        param0.setValueString(opzioni, TableDao.XXXdataFine_Da);
        param1 = new Parameter();
        param1.setKey("AA_MORTE");
        param1.setValueString(opzioni, TableDao.XXXdataFine_A);
        if((value=setParameterDaA(param0,param1))!= null){
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXdataFine_Da);
            opzioni.remove(TableDao.XXXdataFine_A);
        }
    }
    /*
    <opzionale dipende="XXXaa_nascita"> AND aa_nascita = XXXaa_nascita </opzionale>
     */
    public void setAANascita(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("AA_NASCITA");
        param.setValueString(opzioni, TableDao.XXXaa_nascita);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXaa_nascita);
        }
    }
    /*
    <opzionale dipende="XXXaa_morte"> AND aa_morte = XXXaa_morte </opzionale>
     */
    public void setAAMorte(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("AA_MORTE");
        param.setValueString(opzioni, TableDao.XXXaa_morte);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXaa_morte);
        }
    }
    /*
    <opzionale dipende="XXXute_ins">AND  UPPER(ute_ins) LIKE UPPER(XXXute_ins)||'%'</opzionale>
    */
    public void setUteIns(HashMap opzioni) {
        if(opzioni.containsKey(TableDao.XXXute_ins)){
            this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(UTE_INS) LIKE UPPER(?)||'%'",opzioni.get(TableDao.XXXute_ins), Hibernate.STRING));
            opzioni.remove(TableDao.XXXute_ins);
        }
    }
    /*
    <opzionale dipende="XXXute_var">AND  UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale>
    */
    public void setUteVar(HashMap opzioni) {
        if(opzioni.containsKey(TableDao.XXXute_var)){
            this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(UTE_VAR) LIKE UPPER(?)||'%'",opzioni.get(TableDao.XXXute_var), Hibernate.STRING));
            opzioni.remove(TableDao.XXXute_var);
        }
    }

    /*
    <opzionale dipende="XXXlivello"> AND cd_livello = XXXlivello</opzionale>
    <opzionale dipende="XXXlivello_rec"> AND cd_livello ='05'</opzionale>
    <opzionale dipende="XXXlivello_min"> AND ( cd_livello &gt;= '06' AND cd_livello &lt;='51')</opzionale>
    <opzionale dipende="XXXlivello_med"> AND ( cd_livello &gt;= '52' AND cd_livello &lt;='71')</opzionale>
    <opzionale dipende="XXXlivello_max"> AND ( cd_livello &gt;= '72' AND cd_livello &lt;='90')</opzionale>
    <opzionale dipende="XXXlivello_sup"> AND ( cd_livello &gt;= '91' AND cd_livello &lt;='95')</opzionale>
    <opzionale dipende="XXXlivello_lav"> AND cd_livello ='96'</opzionale>
    <opzionale dipende="XXXlivello_auf"> AND cd_livello ='97'</opzionale>
     */
    public void setLivello(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param;
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello);

        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_rec,"05");
        if((value = this.setParameterEq(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_min,"06");
        if((value = this.setParameterGe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_min,"51");
        if((value = this.setParameterLe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_med,"52");
        if((value = this.setParameterGe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_med,"71");
        if((value = this.setParameterLe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_max,"72");
        if((value = this.setParameterGe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_max,"90");
        if((value = this.setParameterLe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_sup,"91");
        if((value = this.setParameterGe(param)) != null){
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_sup,"95");
        if((value = this.setParameterLe(param)) != null){
            this.basicCriteria.add(value);
        }

        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_lav,"96");
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
        param = new Parameter();
        param.setKey("CD_LIVELLO");
        param.setValueString(opzioni, TableDao.XXXlivello_auf,"97");
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
        }
        opzioni.remove(TableDao.XXXlivello);
        opzioni.remove(TableDao.XXXlivello_rec);
        opzioni.remove(TableDao.XXXlivello_min);
        opzioni.remove(TableDao.XXXlivello_med);
        opzioni.remove(TableDao.XXXlivello_max);
        opzioni.remove(TableDao.XXXlivello_sup);
        opzioni.remove(TableDao.XXXlivello_lav);
        opzioni.remove(TableDao.XXXlivello_auf);


    }


    /* <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale> */
    public void setFlPossesso(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("FL_POSSESSO");
        param.setValueString(opzioni, TableDao.XXXfl_possesso,"N");

        if ((value = this.setParameterNotEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXfl_possesso);
        }
    }

    /* <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale> */
    public void setFlGestione(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("fl_gestione");
        param.setValueString(opzioni, TableDao.XXXfl_gestione,"N");

        if ((value = this.setParameterNotEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXfl_gestione);
        }
    }

    /* <opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale> */
    public void setNonMusicale(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_MATERIALE");
        param.setValueString(opzioni, TableDao.XXXnon_musicale,"U");

        if ((value = this.setParameterNotEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXnon_musicale);
        }
    }

    /*<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>*/
    public void setKyEditore(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_EDITORE");
        param.setValueString(opzioni, TableDao.XXXky_editore);

        if ((value = this.setParameterLikeAnywhere(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(TableDao.XXXky_editore);
        }
    }
    /*
     <opzionale dipende="XXXimpronta1"> AND impronta_1 like XXXimpronta1 || '%' </opzionale>
     <opzionale dipende="XXXimpronta2"> AND impronta_2 like XXXimpronta2 || '%' </opzionale>
     <opzionale dipende="XXXimpronta3"> AND impronta_3 like XXXimpronta3 || '%' </opzionale>
    */
   private void setKyImpronta(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param;

       param = new Parameter();
       param.setKey("IMPRONTA_1");
       param.setValueString(opzioni, TableDao.XXXimpronta1);
       if ((value = this.setParameterLikeEnd(param)) != null) {
           this.basicCriteria.add(value);
       }

       param = new Parameter();
       param.setKey("IMPRONTA_2");
       param.setValueString(opzioni, TableDao.XXXimpronta2);
       if ((value = this.setParameterLikeEnd(param)) != null) {
           this.basicCriteria.add(value);
       }

       param = new Parameter();
       param.setKey("IMPRONTA_3");
       param.setValueString(opzioni, TableDao.XXXimpronta3);
       if ((value = this.setParameterLikeEnd(param)) != null) {
           this.basicCriteria.add(value);
       }
       opzioni.remove(TableDao.XXXimpronta1);
       opzioni.remove(TableDao.XXXimpronta2);
       opzioni.remove(TableDao.XXXimpronta3);


   }











    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = TableDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
            for(int index =0; index<metodi.length; index++){
                if(metodi[index].getName().startsWith("set")){
                    //log.debug("Tabella:" + cl.getName().toString() + "metodo criteria:" + metodi[index].getName().toString());
                    metodi[index].invoke(this,new Object[] { opzioni});
                    //log.debug(metodi[index].getName());
                }
            }

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
    /**
     * Estrae dal resultset tutti i record e ne costruisce un vettore di AttributiTavole.
     * Attenzione: dopo chiude il result set e lo setta a null.
     * Se il result set è null restituisce l'elenco già elaborato precedentemente;
     * conviene comunque non invocare questo metodo più di una volta.
     */
    protected HashMap filtri = new HashMap();

	public void valorizzaParametro(HashMap filtri) {
		this.filtri = filtri;
	}

	public void mergeParametro(HashMap params) {
		for (Object key : params.keySet())
			this.filtri.put(key, params.get(key) );
	}

	public void valorizzaParametro(String key, String value) {
		this.filtri.put(key, value);
	}

	public HashMap getParametro() {
		return this.filtri;
	}

	public String getParametro(String key) {
		if (this.filtri.containsKey(key))
			return (String) this.filtri.get(key);
		return null;
	}

	protected void pulisciParametro(String key, String value) {
		this.filtri.clear();
	}

/*
 * FUNZIONI PRE L'ESECUZIONI DI QUEY
 */
    private List response;
    private Integer count = null;
    private Integer rowsUpdate = null;

    public int getCount() throws EccezioneDB {
		return count == null ? 0 : count;
	}

	public List getResponse() throws IllegalArgumentException, Exception,
			InvocationTargetException {
		return this.response;
	}

	public List getElencoRisultati() throws EccezioneDB {
		return this.response;
	}

	public void valorizzaElencoRisultati(List response) throws EccezioneDB {
		this.response = response;
		this.count = ValidazioneDati.size(response);
	}

    private int indice = 0;

	public List getElencoRisultati(int numero) throws EccezioneDB {
		List elencoRisultati = new ArrayList();
		int resultSize = getElencoRisultati().size();
		if (resultSize == 0)
			return elencoRisultati;

		int size = indice + numero;
		if (size > resultSize)
			size = resultSize;

		// int size =
		// (getElencoRisultati().size()-indice)<=numero?(getElencoRisultati().size()):numero;

		int i = 0;
		for (i = indice; i < size; i++)
			elencoRisultati.add(getElencoRisultati().get(i));

		this.indice = i;

		return elencoRisultati;
	}

    public int getRowsUpdate() {
		return rowsUpdate == null ? 0 : rowsUpdate;
	}




    //TEST PER LANCIO ROUTINE
    /*
    public void executeCustom(TableDao tavola,String funzione,String ord) throws IllegalArgumentException, Exception, InvocationTargetException {
        tavola.valorizzaParametro(this.getParametro());
        this.test = tavola.executeCustom(funzione,ord);
    }
    */
    public boolean executeCustom(String funzione, String ord) throws IllegalArgumentException, Exception, InvocationTargetException {
		this.valorizzaParametro("ORDER", ord);
		return this.executeCustom(funzione);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean executeCustom(String funzione) throws Exception {
		Class<?> cl = this.getClass();
		Method[] metodi = cl.getMethods();
		for (Method m : metodi) {
			String methodName = m.getName();
			if (methodName.equals(funzione)) {
				log.debug(String.format("LANCIO: '%s' su vista: '%s'", m.getName(), getTarget().getSimpleName() ));
				Object response = m.invoke(this, new Object[] { this.getParametro() });
				if (response instanceof Number)
					this.count = ((Number)response).intValue();
				else if (response instanceof List) {
					List list = new ArrayList((List)response);
					this.response = list;
					this.count = ValidazioneDati.size(list);
				}
			}
		}
		return true;
	}

    public boolean executeCustomUpdate(String nomeStatement)  throws IllegalArgumentException, Exception, InvocationTargetException {
        this.valorizzaParametro(this.getParametro());
        return this.executeCustom(nomeStatement);
    }











    /** Legge da una tavola il valore del COUNT(*)
     * @throws Exception
     * @throws InvocationTargetException
     *
     * @throws IllegalArgumentException */
    /*
    public int conta(List tavola, String nome_statement) throws IllegalArgumentException, InvocationTargetException, Exception {
        return this.executeCustom(nome_statement);
    }
    public int conta(TableDao tavola, String string) throws IllegalArgumentException, InvocationTargetException, Exception {
        tavola.valorizzaParametro(this.getParametro());
        return tavola.executeCustom(string);
    }
    public int conta(List tavola) throws EccezioneDB {
        try {
            return tavola.size();
        } catch (Exception ecc) {
            log.debug("Errore nella lettura del COUNT (*) dal resultset");
            throw new EccezioneDB(1203);
        }
    }

    public boolean executeCustom(String nomeStatement, String nomeEstensione, String order) {
        return false;
    }
 */


	public static final String buildINClause(String target, String[] values) {
		if (!ValidazioneDati.isFilled(values))
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append(target).append(" in (");
		Iterator<String> i = Arrays.asList(values).iterator();
		for (;;) {
			String value = i.next();
			sql.append("'").append(value).append("'");
			if (i.hasNext())
				sql.append(", ");
			else
				break;
		}

		sql.append(")");

		return sql.toString();
	}

    public static String getXXXaa_morte() {
        return XXXaa_morte;
    }

    public static Timestamp now() {
    	return new Timestamp(System.currentTimeMillis());
    }

}
