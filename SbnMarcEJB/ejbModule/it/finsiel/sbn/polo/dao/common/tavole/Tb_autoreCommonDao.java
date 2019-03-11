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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public class Tb_autoreCommonDao extends TableDao {
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

// CAMPI DELLE VISTE ( sono tutti univoci non hanno bisogno di Overwrite
<opzionale dipende="XXXStringaLikeAutore"> AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) LIKE UPPER( XXXStringaLikeAutore )||'%'</opzionale>
<opzionale dipende="XXXStringaEsattaAutore"> AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) = UPPER(XXXStringaEsattaAutore)</opzionale>
<opzionale dipende="XXXStringaLikeAutore1"> AND ky_cles1_a like XXXStringaLikeAutore1 || '%' </opzionale>
<opzionale dipende="XXXStringaLikeAutore2"> AND ky_cles2_a like XXXStringaLikeAutore2 || '%' </opzionale>
<opzionale dipende="XXXStringaEsattaAutore1">   AND ky_cles1_a = XXXStringaEsattaAutore1 </opzionale>
<opzionale dipende="XXXStringaEsattaAutore2"> AND ky_cles2_a = XXXStringaEsattaAutore2 </opzionale>
<opzionale dipende="XXXky_auteur"> AND ky_auteur = XXXky_auteur </opzionale>
<opzionale dipende="XXXky_cautun"> AND ky_cautun = XXXky_cautun </opzionale>
<opzionale dipende="XXXtp_responsabilita"> AND tp_responsabilita = XXXtp_responsabilita </opzionale>
<opzionale dipende="XXXcd_relazione"> AND cd_relazione = XXXcd_relazione </opzionale>

// CAMPI DELLE VISTE ( CON FINALE _F CHE SONO PRESENTI NELLE VISTE
   Ve_cartografia_aut_aut
   Ve_grafica_aut_aut
   Ve_musica_aut_aut
   Ve_titolo_aut_aut

<opzionale dipende="XXXStringaLikeAutore1"> AND ky_cles1_a_f like XXXStringaLikeAutore1 || '%' </opzionale>
<opzionale dipende="XXXStringaLikeAutore2"> AND ky_cles2_a_f like XXXStringaLikeAutore2 || '%' </opzionale>
<opzionale dipende="XXXStringaEsattaAutore1">   AND ky_cles1_a_f = XXXStringaEsattaAutore1 </opzionale>
<opzionale dipende="XXXStringaEsattaAutore2"> AND ky_cles2_a_f = XXXStringaEsattaAutore2 </opzionale>
<opzionale dipende="XXXky_auteur"> AND ky_auteur_f = XXXky_auteur </opzionale>
<opzionale dipende="XXXky_cautun"> AND ky_cautun_f = XXXky_cautun </opzionale>
<opzionale dipende="XXXtp_responsabilita"> AND tp_responsabilita_f = XXXtp_responsabilita </opzionale>
<opzionale dipende="XXXcd_relazione"> AND cd_relazione_f = XXXcd_relazione </opzionale>

NELLE VISTE
<opzionale dipende="XXXtp_responsabilita_or_1"> AND (tp_responsabilita = XXXtp_responsabilita_or_1 </opzionale>
<opzionale dipende="XXXtp_responsabilita_or_2"> OR tp_responsabilita = XXXtp_responsabilita_or_2 ) </opzionale>


     */



	protected boolean kycleslike = false;

	public Tb_autoreCommonDao() {
		super();
	}

    public Tb_autoreCommonDao(Criteria autoreCriteria) {
        super();
        this.basicCriteria = autoreCriteria;
    }

	/*
	<opzionale dipende="XXXtipoNome1"> AND ( tp_nome_aut = XXXtipoNome1</opzionale>
	<opzionale dipende="XXXtipoNome2"> OR  tp_nome_aut = XXXtipoNome2 </opzionale>
	<opzionale dipende="XXXtipoNome3"> OR  tp_nome_aut = XXXtipoNome3 </opzionale>
	<opzionale dipende="XXXtipoNome4"> OR  tp_nome_aut = XXXtipoNome4 </opzionale>
	<opzionale dipende="XXXtipoNome1"> ) </opzionale>
	 */
	public void setTipoNome(HashMap opzioni) {
		Criterion value;
		Parameter param = new Parameter();

		// almaviva2 14 Aprile 2009 Il settaggio della key era mancante ed è stato aggiunto
		param.setKey("TP_NOME_AUT");

        param.setValueCollection(opzioni,Tb_autoreCommonDao.XXXtipoNome,4);
		if((value = this.setParameterIn(param))!= null){
			this.basicCriteria.add(value);
            opzioni.remove(Tb_autoreCommonDao.XXXtipoNome);
		}
	}
	/*
	<opzionale dipende="XXXesporta_ts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXesporta_ts_var_da </opzionale>
	<opzionale dipende="XXXesporta_ts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXesporta_ts_var_a </opzionale>
	 */
	public void setEsportaTsVar(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXesporta_ts_var_da,"yyyy-mm-dd");

        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXesporta_ts_var_a,"yyyy-mm-dd");

		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}



	/*
	<opzionale dipende="XXXtp_forma_aut"> AND tp_forma_aut = XXXtp_forma_aut </opzionale>
	 */
	public void setTpFormaAut(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_FORMA_AUT");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_forma_aut);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
    <opzionale dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins != ts_var AND ts_var &gt;= XXXesporta_ts_var_e_ts_ins_da </opzionale>

	 */
	public void setEsportaTsVarETsInsDa(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TS_VAR");
        param.setValueDate(opzioni,Tb_autoreCommonDao.XXXesporta_ts_var_e_ts_ins_da,"yyyy-mm-dd");
		if((value=setParameterGe(param))!= null){
			this.basicCriteria.add(value);
			this.basicCriteria.add(Restrictions.neProperty("TS_INS","TS_VAR"));
		}
	}
	/*
	<opzionale dipende="XXXdata_inizio_inserimento"> AND to_char(ts_ins,'') &gt;= XXXdata_inizio_inserimento </opzionale>
	<opzionale dipende="XXXdata_fine_inserimento"> AND to_char(ts_ins,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_inserimento </opzionale>
	 */
	public void setDataInizioInserimento(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_INS");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_inizio_inserimento,"yyyymmddhh24miss.FF");

        param1 = new Parameter();
        param1.setKey("TS_INS");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_fine_inserimento,"yyyymmddhh24miss.FF");

		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXdata_inizio_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF') &gt;= XXXdata_inizio_variazione </opzionale>
	<opzionale dipende="XXXdata_fine_variazione"> AND to_char(ts_var,'yyyymmddhh24miss.FF') &lt;= XXXdata_fine_variazione </opzionale>
	 */
	public void setDataInizioVariazione(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_inizio_variazione,"yyyymmddhh24miss.FF");

        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_fine_variazione,"yyyymmddhh24miss.FF");
		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXtp_nome_aut">AND tp_nome_aut = XXXtp_nome_aut </opzionale>
	 */
	public void setTpNomeAut(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_NOME_AUT");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_nome_aut);

		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXtipo_nome_autore"> AND tp_nome_aut = XXXtipo_nome_autore</opzionale>
	 */
	public void setTipoNomeAutore(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_NOME_AUT");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtipo_nome_autore);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXtipo_forma_autore"> AND tp_forma_aut = XXXtipo_forma_autore</opzionale>
	 */
	public void setTipoFormaAutore(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_FORMA_AUT");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtipo_forma_autore);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}


	/*
	<opzionale dipende="XXXcd_polo"> AND UPPER(ute_var) LIKE UPPER(XXXcd_polo)||'%'</opzionale>
	 */
	public void setCdPolo(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("UTE_VAR");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcd_polo);
		if ((value = this.setParameterLikeEnd(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXcd_biblioteca"> AND UPPER(ute_ins) LIKE UPPER(XXXcd_biblioteca)||'%'</opzionale>
	 */
	public void setCdBiblioteca(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("UTE_INS");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcd_biblioteca);
		if ((value = this.setParameterLikeEnd(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXcd_livello">AND XXXcd_livello &gt;= TO_NUMBER(cd_livello)</opzionale>
	 */
	public void setCdLivello(HashMap opzioni) throws InfrastructureException {
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXcd_livello)){

            if(this.isSessionOracle())
                this.basicCriteria.add(Restrictions.sqlRestriction("? >= TO_NUMBER(cd_livello)",opzioni.get(Tb_autoreCommonDao.XXXcd_livello), Hibernate.STRING));
            else
            	this.basicCriteria.add(Restrictions.sqlRestriction("? >= cd_livello",opzioni.get(Tb_autoreCommonDao.XXXcd_livello), Hibernate.STRING));		}
				//this.basicCriteria.add(Restrictions.sqlRestriction("? >= TO_NUMBER(cd_livello,'99')",opzioni.get(Tb_autoreCommonDao.XXXcd_livello), Hibernate.STRING));		}
	}
	/*
	<opzionale dipende="XXXvid"> AND vid != XXXvid </opzionale>
	 */
	public void setVID(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("VID");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXvid);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXautore_parentesi">and ( </opzionale>
	<opzionale dipende="XXXtp_nome_aut_a">tp_nome_aut = 'A' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_b">tp_nome_aut = 'B' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_c">tp_nome_aut = 'C' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_d">tp_nome_aut = 'D' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_e">tp_nome_aut = 'E' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_r">tp_nome_aut = 'R' or</opzionale>
	<opzionale dipende="XXXtp_nome_aut_g">tp_nome_aut = 'G' or</opzionale>
	<opzionale dipende="XXXautore_parentesi"> tp_nome_aut =XXXautore_parentesi ) </opzionale>
	*/
	public void setAutoreParentesi(HashMap opzioni) {
		Criterion criteria;
		if (opzioni.containsKey(Tb_autoreCommonDao.XXXautore_parentesi)) {
			List<String> value = new ArrayList<String>();
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_a)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_a));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_b)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_b));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_c)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_c));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_d)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_d));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_e)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_e));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_r)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_r));
			}
			if (opzioni.containsKey(Tb_autoreCommonDao.XXXtp_nome_aut_g)) {
				value.add((String) opzioni.get(Tb_autoreCommonDao.XXXtp_nome_aut_g));
			}

			Parameter param = new Parameter();
	        param.setKey("TP_NOME_AUT");
	        param.setValueCollection(value);

			if ((criteria = this.setParameterIn(param)) != null) {
				this.basicCriteria.add(criteria);
			}
		}
	}
	/*
	<opzionale dipende="XXXtp_forma_conferma">AND tp_forma_aut = XXXtp_forma_conferma</opzionale>
	 */
	public void setTpFormaConferma(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_FORMA_AUT");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_forma_conferma);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}
	}
	/*
	 * <opzionale dipende="XXXky_cles1_a_inizio"> AND  UPPER(ky_cles1_a) &gt;= UPPER(XXXky_cles1_a_inizio) AND UPPER(ky_cles1_a) &lt; UPPER(XXXky_cles1_a_fine)</opzionale>
	*/
	public void setKyClesInizio(HashMap opzioni) {
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles1_a_inizio)){
			this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(KY_CLES1_A) >=  UPPER(?) ",opzioni.get(Tb_autoreCommonDao.XXXky_cles1_a_inizio), Hibernate.STRING));
			this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(KY_CLES1_A) <  UPPER(?) ",opzioni.get(Tb_autoreCommonDao.XXXky_cles1_a_fine), Hibernate.STRING));
		}
	}
	/*
	 <opzionale dipende="XXXky_cles1_a_puntuale"> AND  ky_cles1_a LIKE UPPER(XXXky_cles1_a_puntuale) </opzionale>
	*/
	public void setKyClesPuntuale(HashMap opzioni) {
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles1_a_puntuale)){
			this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(KY_CLES1_A) LIKE UPPER(?) ",opzioni.get(Tb_autoreCommonDao.XXXky_cles1_a_puntuale), Hibernate.STRING));
		}
	}
	/*
	<opzionale dipende="XXXparola1"> AND CONTAINS(ds_nome_aut, XXXparola1 ) &gt; 0 </opzionale>
	<opzionale dipende="XXXparola2"> AND CONTAINS(ds_nome_aut, XXXparola2 ) &gt; 0 </opzionale>
	<opzionale dipende="XXXparola3"> AND CONTAINS(ds_nome_aut, XXXparola3 ) &gt; 0 </opzionale>
	<opzionale dipende="XXXparola4"> AND CONTAINS(ds_nome_aut, XXXparola4 ) &gt; 0 </opzionale>
	*/
	public void setParola(HashMap opzioni) throws InfrastructureException {
	   	if(isSessionPostgreSQL()){
	   		String appo = null;
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola1)){
		   		appo = "tidx_vector  @@ to_tsquery(";
		   		if (isPostgresVersion83())
		   			appo += "'pg_catalog.italian', ";
		   		else
		   			appo += "'default', ";
	        	appo = appo + " '" + opzioni.get(Tb_autoreCommonDao.XXXparola1);
	            opzioni.remove(Tb_autoreCommonDao.XXXparola1);
	        }
	        if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola2)){
	        	appo = appo + "&" + opzioni.get(Tb_autoreCommonDao.XXXparola2);
	            opzioni.remove(Tb_autoreCommonDao.XXXparola2);
	        }
	        if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola3)){
	        	appo = appo + "&" + opzioni.get(Tb_autoreCommonDao.XXXparola3);
	            opzioni.remove(Tb_autoreCommonDao.XXXparola3);
	        }
	        if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola4)){
	        	appo = appo + "&" + opzioni.get(Tb_autoreCommonDao.XXXparola4);
	            opzioni.remove(Tb_autoreCommonDao.XXXparola4);
	        }
	        if(appo != null){
	        	appo = appo + "')";
	        	this.basicCriteria.add(Restrictions.sqlRestriction(appo));
	        }
	   	}else{
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola1)){
				this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_NOME_AUT, ? ) > 0",opzioni.get(Tb_autoreCommonDao.XXXparola1), Hibernate.STRING));
			}
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola2)){
				this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_NOME_AUT, ? ) > 0",opzioni.get(Tb_autoreCommonDao.XXXparola2), Hibernate.STRING));
			}
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola3)){
				this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_NOME_AUT, ? ) > 0",opzioni.get(Tb_autoreCommonDao.XXXparola3), Hibernate.STRING));
			}
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXparola4)){
				this.basicCriteria.add(Restrictions.sqlRestriction("CONTAINS(DS_NOME_AUT, ? ) > 0",opzioni.get(Tb_autoreCommonDao.XXXparola4), Hibernate.STRING));
			}
	   	}
	}
	/*
	<opzionale dipende="XXXds_nome_cerca_simili">AND TRIM (UPPER (ds_nome_aut)) = XXXds_nome_cerca_simili</opzionale>
	*/
	public void setDsNomeCercaSimili(HashMap opzioni) {
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXds_nome_cerca_simili)){
			this.basicCriteria.add(Restrictions.sqlRestriction("TRIM (UPPER (DS_NOME_AUT)) = ?",opzioni.get(Tb_autoreCommonDao.XXXds_nome_cerca_simili), Hibernate.STRING));
		}
	}
	/*
	<opzionale dipende="XXXky_el1">AND ky_el1_a || ky_el1_b = XXXky_el1</opzionale>
	<opzionale dipende="XXXky_el1null">AND ky_el1_a is null AND ky_el1_b is null</opzionale>

	<opzionale dipende="XXXky_el2">AND ky_el2_a || ky_el2_b = XXXky_el2</opzionale>
	<opzionale dipende="XXXky_el2null">AND ky_el2_a is null AND ky_el2_b is null</opzionale>

	<opzionale dipende="XXXky_el3">AND ky_el3 = XXXky_el3</opzionale>
	<opzionale dipende="XXXky_el3null">AND ky_el3 is null</opzionale>

	<opzionale dipende="XXXky_el4">AND ky_el4 = XXXky_el4</opzionale>
	<opzionale dipende="XXXky_el4null">AND ky_el4 is null</opzionale>

	<opzionale dipende="XXXky_el5">AND ky_el5 = XXXky_el5</opzionale>
	<opzionale dipende="XXXky_el5null">AND ky_el5 is null</opzionale>
	*/
	public void setKyEl(HashMap opzioni) throws Exception {
		Criterion value;
        Parameter param;
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_el1)){
			this.basicCriteria.add(Restrictions.sqlRestriction("KY_EL1_A || KY_EL1_B = ?",opzioni.get(Tb_autoreCommonDao.XXXky_el1), Hibernate.STRING));
		}else if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_el1null)){
			this.basicCriteria.add(Restrictions.isNull("KY_EL1_A"));
			this.basicCriteria.add(Restrictions.isNull("KY_EL1_B"));
		}
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_el2)){
			this.basicCriteria.add(Restrictions.sqlRestriction("KY_EL2_A || KY_EL2_B = ?",opzioni.get(Tb_autoreCommonDao.XXXky_el2), Hibernate.STRING));
		}else if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_el2null)){
			this.basicCriteria.add(Restrictions.isNull("KY_EL2_A"));
			this.basicCriteria.add(Restrictions.isNull("KY_EL2_B"));
		}
        param = new Parameter();
        param.setKey("KY_EL3");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_el3);


		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}else if (opzioni.containsKey(Tb_autoreCommonDao.XXXky_el3null)) {
			this.basicCriteria.add(Restrictions.isNull("KY_EL3"));
		}

        param = new Parameter();
        param.setKey("KY_EL4");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_el4);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}else if (opzioni.containsKey(Tb_autoreCommonDao.XXXky_el4null)) {
			this.basicCriteria.add(Restrictions.isNull("KY_EL4"));
		}
        param = new Parameter();
        param.setKey("KY_EL5");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_el5);
		if ((value = this.setParameterEq(param)) != null) {
			this.basicCriteria.add(value);
		}else if (opzioni.containsKey(Tb_autoreCommonDao.XXXky_el5null)) {
			this.basicCriteria.add(Restrictions.isNull("KY_EL5"));
		}

	}
	/*
	<opzionale dipende="XXXanno_inizio"> AND ts_var  BETWEEN to_date(XXXanno_inizio||'0101','yyyymmdd')</opzionale>
	<opzionale dipende="XXXanno_fine"> AND to_date(XXXanno_fine||'1231','yyyymmdd')</opzionale>
	 */
	public void setAnno(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXanno_inizio,"yyyy");

        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXanno_fine,"yyyy");
		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXdata_anno"> AND ts_var  BETWEEN to_date(XXXdata_anno||'0101','yyyymmdd')</opzionale>
	<opzionale dipende="XXXdata_anno"> AND to_date(XXXdata_anno||'1231','yyyymmdd')</opzionale>
	 */
	public void setDataAnno(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_anno,"yyyy");

        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXdata_anno,"yyyy");
		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}
	/*
	<opzionale dipende="XXXanno_mese_inizio"> AND ts_ins  BETWEEN to_date(XXXanno_mese_inizio,'yyyy-mm')</opzionale>
	<opzionale dipende="XXXanno_mese_fine"> AND to_date(XXXanno_mese_fine,'yyyy-mm')</opzionale>
	 */
	public void setAnnoMese(HashMap opzioni) throws ParseException {
		Criterion value;
        Parameter param0;
        Parameter param1;

        param0 = new Parameter();
        param0.setKey("TS_VAR");
        param0.setValueDate(opzioni,Tb_autoreCommonDao.XXXanno_mese_inizio,"yyyy-mm");

        param1 = new Parameter();
        param1.setKey("TS_VAR");
        param1.setValueDate(opzioni,Tb_autoreCommonDao.XXXanno_mese_fine,"yyyy-mm");
		if((value=setParameterDaA(param0,param1))!= null){
			this.basicCriteria.add(value);
		}
	}



	/*
	 PARTE I
	AND ((ky_cles1_a = XXXky_cles1_a ))
	Oppure
	AND ((ky_cles1_a = XXXky_cles1_a AND ky_cles2_a = XXXky_cles2_a))
	Oppure
	AND ((ky_cles1_a = XXXky_cles1_a AND ky_cles2_a is null))

	 PARTE II
	OR (ky_cles1_a = XXXcles2_1)
	Oppure
	OR (ky_cles1_a = XXXcles2_1 AND ky_cles2_a = XXXcles2_2 )
	Oppure
	OR (ky_cles1_a = XXXcles2_1 AND ky_cles2_a is null )

	*/
	public void setKyCles(HashMap opzioni) throws Exception {
		if(this.kycleslike)
			return;

		boolean addCriteria=false;
		Disjunction disjunction =  Restrictions.disjunction();
		Conjunction conjunction = null;
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles1_a)){
	        Parameter param0 = new Parameter();
	        param0.setKey("KY_CLES1_A");
	        param0.setValueString(opzioni,Tb_autoreCommonDao.XXXky_cles1_a);

			conjunction =  Restrictions.conjunction();
			conjunction.add(this.setParameterEq(param0));
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles2_a)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES2_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_cles2_a);

				conjunction.add(this.setParameterEq(param));
			}else if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles2_anull)){
				conjunction.add(Restrictions.isNull("KY_CLES2_A"));
			}
			disjunction.add(conjunction);
			addCriteria=true;
		}
		if(conjunction != null){
			boolean add=false;
			Conjunction conjunction1 = Restrictions.conjunction();
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXcles2_1)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES1_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcles2_1);

				conjunction1.add(this.setParameterEq(param));
				add = true;
			}
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXcles2_2)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES2_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcles2_2);

				conjunction1.add(this.setParameterEq(param));
				add = true;
			}else if(opzioni.containsKey(Tb_autoreCommonDao.XXXcles2_2null)){
				conjunction1.add(Restrictions.isNull("KY_CLES2_A"));
				add = true;
			}
			if(add)
				disjunction.add(conjunction1);
		}
		if(addCriteria)
			this.basicCriteria.add(disjunction);
	}

	public void setKyClesLike(HashMap opzioni) throws Exception {
		if(!this.kycleslike)
			return;

		boolean addCriteria=false;
		Disjunction disjunction =  Restrictions.disjunction();
		Conjunction conjunction = null;
		if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles1_a)){
			Parameter param0 = new Parameter();
	        param0.setKey("KY_CLES1_A");
	        param0.setValueString(opzioni,Tb_autoreCommonDao.XXXky_cles1_a);
			conjunction =  Restrictions.conjunction();
			conjunction.add(this.setParameterLikeEnd(param0));
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXky_cles2_a)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES2_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_cles2_a);
				conjunction.add(this.setParameterLikeEnd(param));
			}
			disjunction.add(conjunction);
			addCriteria=true;
		}
		if(conjunction != null){
			boolean add=false;
			Conjunction conjunction1 = Restrictions.conjunction();
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXcles2_1)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES1_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcles2_1);

				conjunction1.add(this.setParameterLikeEnd(param));
				add = true;
			}
			if(opzioni.containsKey(Tb_autoreCommonDao.XXXcles2_2)){
		        Parameter param = new Parameter();
		        param.setKey("KY_CLES2_A");
		        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcles2_2);

				conjunction1.add(this.setParameterLikeEnd(param));
				add = true;
			}
			if(add)
				disjunction.add(conjunction1);
		}
		if(addCriteria)
			this.basicCriteria.add(disjunction);
	}




    /*
    <opzionale dipende="XXXStringaEsattaAutore"> AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) = UPPER(XXXStringaEsattaAutore)</opzionale>
     */
    public void setStringaEsattaAutore(HashMap opzioni) {
        if(opzioni.containsKey(Tb_autoreCommonDao.XXXStringaEsattaAutore)){
            this.basicCriteria.add(Restrictions.sqlRestriction("AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) = UPPER(?)",opzioni.get(Tb_autoreCommonDao.XXXStringaEsattaAutore), Hibernate.STRING));
            opzioni.remove(XXXStringaEsattaAutore);
        }
    }

    /*
    <opzionale dipende="XXXStringaLikeAutore"> AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) LIKE UPPER( XXXStringaLikeAutore )||'%'</opzionale>
     */
    public void setStringaLikeAutore(HashMap opzioni) {
        if(opzioni.containsKey(Tb_autoreCommonDao.XXXStringaEsattaAutore)){
            this.basicCriteria.add(Restrictions.sqlRestriction("AND UPPER (ky_cles1_a) || UPPER(ky_cles2_a) LIKE UPPER( ? )||'%'",opzioni.get(Tb_autoreCommonDao.XXXStringaLikeAutore), Hibernate.STRING));
            opzioni.remove(XXXStringaLikeAutore);
        }
    }
    /*
    <opzionale dipende="XXXStringaLikeAutore1"> AND ky_cles1_a like XXXStringaLikeAutore1 || '%' </opzionale>
     */
    private void setStringaLikeAutore1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES1_A");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXStringaLikeAutore1);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaLikeAutore1);
        }
    }
    /*
    <opzionale dipende="XXXStringaLikeAutore2"> AND ky_cles2_a like XXXStringaLikeAutore2 || '%' </opzionale>
     */
    private void setStringaLikeAutore2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES2_A");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXStringaLikeAutore2);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaLikeAutore2);
        }
    }
    /*
     <opzionale dipende="XXXStringaEsattaAutore1">   AND ky_cles1_a = XXXStringaEsattaAutore1 </opzionale>
     */
    private void setStringaEsattaAutore1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES1_A");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXStringaEsattaAutore1);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaEsattaAutore1);
        }
    }
    /*
     <opzionale dipende="XXXStringaEsattaAutore2"> AND ky_cles2_a = XXXStringaEsattaAutore2 </opzionale>
     */
    private void setStringaEsattaAutore2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES2_A");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXStringaEsattaAutore2);
       if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaEsattaAutore2);
        }
    }
    /*
    <opzionale dipende="XXXky_auteur"> AND ky_auteur = XXXky_auteur </opzionale>
     */
    private void setKyAuteur(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_AUTEUR");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_auteur);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXky_auteur);
        }
    }
    /*
    <opzionale dipende="XXXky_cautun"> AND ky_cautun = XXXky_cautun </opzionale>
     */
    private void setKyCautun(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CAUTUN");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXky_cautun);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXky_cautun);
        }
    }

    /*
    <opzionale dipende="XXXtp_responsabilita_or_1"> AND (tp_responsabilita = XXXtp_responsabilita_or_1 </opzionale>
    */
   private void setTpResponsabilita_or_1(HashMap opzioni) throws Exception {
       Criterion value;
       Parameter param = new Parameter();
       param.setKey("TP_RESPONSABILITA");
       param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_responsabilita_or_1);
       if ((value = this.setParameterEq(param)) != null) {
           this.basicCriteria.add(value);
           opzioni.remove(XXXtp_responsabilita_or_1);
       }
   }

   /*
       <opzionale dipende="XXXtp_responsabilita_or_2"> OR tp_responsabilita = XXXtp_responsabilita_or_2 ) </opzionale>
   */
  private void setTpResponsabilita_or_2(HashMap opzioni) throws Exception {
      Criterion value;
      Parameter param = new Parameter();
      param.setKey("TP_RESPONSABILITA");
      param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_responsabilita_or_2);
      if ((value = this.setDisjunctionEq(param)) != null) {
          this.basicCriteria.add(value);
          opzioni.remove(XXXtp_responsabilita_or_2);
      }
  }



    /*
     <opzionale dipende="XXXtp_responsabilita"> AND tp_responsabilita = XXXtp_responsabilita </opzionale>
     */
    private void setTpResponsabilita(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_RESPONSABILITA");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXtp_responsabilita);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXtp_responsabilita);
        }
    }
    /*
     <opzionale dipende="XXXcd_relazione"> AND cd_relazione = XXXcd_relazione </opzionale>
     */
    private void setCdRelazione(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_RELAZIONE");
        param.setValueString(opzioni,Tb_autoreCommonDao.XXXcd_relazione);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXcd_relazione);
        }
    }


	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Tb_autoreCommonDao.class;//this.getClass();
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
		Tb_autoreCommonDao aut = new Tb_autoreCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

// FUNZIONI PARAMETRIZZATE
///*
//<opzionale dipende="XXXdataInizio_Da"> AND aa_nascita &gt;= XXXdataInizio_Da </opzionale>
//<opzionale dipende="XXXdataInizio_A"> AND aa_nascita &lt;= XXXdataInizio_A </opzionale>
//*/
//public void setDataInizio(HashMap opzioni) {
//  Criterion value;
//  if((value=setParameterDaA(opzioni,Tb_autoreCommonDao.XXXdataInizio_Da,Tb_autoreCommonDao.XXXdataInizio_A,"AA_NASCITA"))!= null){
//      this.basicCriteria.add(value);
//  }
//}
/*
//<opzionale dipende="XXXdataFine_Da"> AND aa_morte &gt;= XXXdataFine_Da </opzionale>
//<opzionale dipende="XXXdataFine_A"> AND aa_morte &lt;= XXXdataFine_A </opzionale>
//*/
//public void setDataFine(HashMap opzioni) {
//  Criterion value;
//  if((value=setParameterDaA(opzioni,Tb_autoreCommonDao.XXXdataFine_Da,Tb_autoreCommonDao.XXXdataFine_A,"AA_MORTE"))!= null){
//      this.basicCriteria.add(value);
//  }
//}
///*
//<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
//<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
// NUOVA DA VERIFICARE
//<opzionale dipende="XXXdata_var_Da"> AND ts_var &gt;= to_date(XXXdata_var_Da , 'yyyy-mm-dd') </opzionale>
//<opzionale dipende="XXXdata_var_A"> AND ts_var &lt;= to_date(XXXdata_var_A , 'yyyy-mm-dd') </opzionale>
//*/
//public void setDataVar(HashMap opzioni) {
//  Criterion value;
//  if((value=setParameterDaA(opzioni,Tb_autoreCommonDao.XXXdata_var_Da,Tb_autoreCommonDao.XXXdata_var_A,"TS_VAR"))!= null){
//      this.basicCriteria.add(value);
//  }
//}
///*
//<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
//<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
//*/
//public void setLivelloAut(HashMap opzioni) {
//  Criterion value;
//  if((value=setParameterDaA(opzioni,Tb_autoreCommonDao.XXXlivello_aut_da,Tb_autoreCommonDao.XXXlivello_aut_a,"CD_LIVELLO"))!= null){
//      this.basicCriteria.add(value);
//  }
//}
/*
//<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese</opzionale>
//*/
//public void setCdPaese(HashMap opzioni) {
//  Criterion value;
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXcd_paese,
//          "CD_PAESE")) != null) {
//      this.basicCriteria.add(value);
//  }
//}
///*
//<opzionale dipende="XXXute_ins">AND  UPPER(ute_ins) LIKE UPPER(XXXute_ins)||'%'</opzionale>
//*/
//public void setUteIns(HashMap opzioni) {
//  if(opzioni.containsKey(Tb_autoreCommonDao.XXXute_ins)){
//      this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(UTE_INS) LIKE UPPER(?)||'%'",opzioni.get(Tb_autoreCommonDao.XXXute_ins), Hibernate.STRING));
//  }
//}
///*
//<opzionale dipende="XXXute_var">AND  UPPER(ute_var) LIKE UPPER(XXXute_var)||'%'</opzionale>
//*/
//public void setUteVar(HashMap opzioni) {
//  if(opzioni.containsKey(Tb_autoreCommonDao.XXXute_var)){
//      this.basicCriteria.add(Restrictions.sqlRestriction("UPPER(UTE_INS) LIKE UPPER(?)||'%'",opzioni.get(Tb_autoreCommonDao.XXXute_var), Hibernate.STRING));
//  }
//}
///*
//<opzionale dipende="XXXaa_nascita"> AND aa_nascita = XXXaa_nascita </opzionale>
// */
//public void setAANascita(HashMap opzioni) {
//  Criterion value;
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXaa_nascita,
//          "AA_NASCITA")) != null) {
//      this.basicCriteria.add(value);
//  }
//}
///*
//<opzionale dipende="XXXaa_morte"> AND aa_morte = XXXaa_morte </opzionale>
// */
//public void setAAMorte(HashMap opzioni) {
//  Criterion value;
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXaa_morte,
//          "AA_MORTE")) != null) {
//      this.basicCriteria.add(value);
//  }
//}
///*
//<opzionale dipende="XXXlivello"> AND cd_livello = XXXlivello</opzionale>
//<opzionale dipende="XXXlivello_rec"> AND cd_livello ='05'</opzionale>
//<opzionale dipende="XXXlivello_min"> AND ( cd_livello &gt;= '06' AND cd_livello &lt;='51')</opzionale>
//<opzionale dipende="XXXlivello_med"> AND ( cd_livello &gt;= '52' AND cd_livello &lt;='71')</opzionale>
//<opzionale dipende="XXXlivello_max"> AND ( cd_livello &gt;= '72' AND cd_livello &lt;='90')</opzionale>
//<opzionale dipende="XXXlivello_sup"> AND ( cd_livello &gt;= '91' AND cd_livello &lt;='95')</opzionale>
//<opzionale dipende="XXXlivello_lav"> AND cd_livello ='96'</opzionale>
//<opzionale dipende="XXXlivello_auf"> AND cd_livello ='97'</opzionale>
// */
//public void setLivello(HashMap opzioni) {
//  Criterion value;
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXlivello,
//          "CD_LIVELLO")) != null) {
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXlivello_rec,"CD_LIVELLO","05")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterGe(opzioni, Tb_autoreCommonDao.XXXlivello_min,"CD_LIVELLO","06")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterLe(opzioni, Tb_autoreCommonDao.XXXlivello_min,"CD_LIVELLO","51")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterGe(opzioni, Tb_autoreCommonDao.XXXlivello_med,"CD_LIVELLO","52")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterLe(opzioni, Tb_autoreCommonDao.XXXlivello_med,"CD_LIVELLO","71")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterGe(opzioni, Tb_autoreCommonDao.XXXlivello_max,"CD_LIVELLO","72")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterLe(opzioni, Tb_autoreCommonDao.XXXlivello_max,"CD_LIVELLO","90")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterGe(opzioni, Tb_autoreCommonDao.XXXlivello_sup,"CD_LIVELLO","91")) != null){
//      this.basicCriteria.add(value);
//  }
//  if((value = this.setParameterLe(opzioni, Tb_autoreCommonDao.XXXlivello_sup,"CD_LIVELLO","95")) != null){
//      this.basicCriteria.add(value);
//  }
//
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXlivello_lav,"CD_LIVELLO","96")) != null) {
//      this.basicCriteria.add(value);
//  }
//  if ((value = this.setParameterEq(opzioni, Tb_autoreCommonDao.XXXlivello_auf,"CD_LIVELLO","97")) != null) {
//      this.basicCriteria.add(value);
//  }
//}



































/*
 * TODO vecchio codice
 */
/*
public void setTpFormaAut(HashMap opzioni) {
    Criterion value;
    if ((value = this.setParameterEq(opzioni, "XXXtp_forma_aut",
            "TP_FORMA_AUT")) != null) {
        this.basicCriteria.add(value);
    }else if ((value = this.setParameterEq(opzioni, "XXXtp_forma_conferma",
            "TP_FORMA_AUT")) != null) {
        this.basicCriteria.add(value);
    }
    log.debug("setTpFormaAut");
}
public void setLivelloAut(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXlivello_aut_da","XXXlivello_aut_a","CD_LIVELLO"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setLivelloAut");
}
public void setTipoNome(HashMap opzioni) {
    Criterion value;
    if((value = this.parserCollection(opzioni,"XXXtipoNome","TP_NOME_AUT",4))!= null)
    {
        this.basicCriteria.add(value);
    }
    log.debug("setTipoNome");
}
public void setCdPaese(HashMap opzioni) {
    Criterion value;
    if ((value = this.setParameterEq(opzioni, "XXXcd_paese",
            "CD_PAESE")) != null) {
        this.basicCriteria.add(value);
    }
    log.debug("setCdPaese");
}
public void setDataInizio(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXdataInizio_Da","XXXdataInizio_A","AA_NASCITA"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setDataInizio");
}
public void setDataFine(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXdataFine_Da","XXXdataFine_A","AA_MORTE"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setDataFine");
}
public void setTpResponsabilita(HashMap opzioni) {
    Criterion value;
    if((value = this.parserCollection(opzioni,"XXXtp_responsabilita_or_","TP_RESPONSABILITA",2))!= null)
    {
        this.basicCriteria.add(value);
    }
    log.debug("setTpResponsabilita");
}
public void setDataIns(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXdata_ins_Da","XXXdata_ins_A","TS_INS"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setDataFine");
}
public void setDataVar(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXdata_var_Da","XXXdata_var_A","TS_VAR"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setDataFine");
}
*/



/*
public void setEsportaTsVar(HashMap opzioni) {
    Criterion value;
    if((value=setParameterDaA(opzioni,"XXXesporta_ts_var_da","XXXesporta_ts_var_a","TS_VAR"))!= null){
        this.basicCriteria.add(value);
    }
    log.debug("setEsportaTsVar");
}
public void setEsportaTsVarETsInsDa(HashMap opzioni) {
    Criterion value;
    if((value=setParameterGe(opzioni,"XXXesporta_ts_var_e_ts_ins_da","TS_VAR"))!= null){
        this.basicCriteria.add(value);
        this.basicCriteria.add(Restrictions.eqProperty("TS_INS","TS_VAR"));
    }
    log.debug("setEsportaTsVarETsInsDa");
}


public void setKyEl(HashMap opzioni) {
    Criterion value;
    if(opzioni.containsKey("XXXky_el1")){
        this.basicCriteria.add(Restrictions.sqlRestriction("KY_EL1_A || KY_EL1_B = ?",opzioni.get("XXXky_el1"), Hibernate.STRING));
    }else if(opzioni.containsKey("XXXky_el1null")){
        this.basicCriteria.add(Restrictions.isNull("KY_EL1_A"));
        this.basicCriteria.add(Restrictions.isNull("KY_EL1_B"));
    }
    if(opzioni.containsKey("XXXky_el2")){
        this.basicCriteria.add(Restrictions.sqlRestriction("KY_EL2_A || KY_EL2_B = ?",opzioni.get("XXXky_el2"), Hibernate.STRING));
    }else if(opzioni.containsKey("XXXky_el2null")){
        this.basicCriteria.add(Restrictions.isNull("KY_EL2_A"));
        this.basicCriteria.add(Restrictions.isNull("KY_EL2_B"));
    }
    if ((value = this.setParameterEq(opzioni, "XXXky_el3","KY_EL3")) != null) {
        this.basicCriteria.add(value);
    }else if (opzioni.containsKey("XXXky_el3null")) {
        this.basicCriteria.add(Restrictions.isNull("KY_EL3"));
    }

    if ((value = this.setParameterEq(opzioni, "XXXky_el4","KY_EL4")) != null) {
        this.basicCriteria.add(value);
    }else if (opzioni.containsKey("XXXky_el4null")) {
        this.basicCriteria.add(Restrictions.isNull("KY_EL4"));
    }
    if ((value = this.setParameterEq(opzioni, "XXXky_el5","KY_EL5")) != null) {
        this.basicCriteria.add(value);
    }else if (opzioni.containsKey("XXXky_el5null")) {
        this.basicCriteria.add(Restrictions.isNull("KY_EL5"));
    }

}
public void setDsNomeCercaSimili(HashMap opzioni) {
    if (opzioni.containsKey("XXXds_nome_cerca_simili")) {
        this.basicCriteria.add(Restrictions.sqlRestriction("AND TRIM (UPPER (DS_NOME_AUT)) = ?",opzioni.get("XXXds_nome_cerca_simili"), Hibernate.STRING));
    }
    log.debug("setDsNomeCercaSimili");
}
*/

