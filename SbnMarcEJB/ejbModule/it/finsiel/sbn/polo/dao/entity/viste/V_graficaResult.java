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
package it.finsiel.sbn.polo.dao.entity.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.V_grafica;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class V_graficaResult extends it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao {

    public V_graficaResult(V_grafica v_grafica) throws InfrastructureException {
        super();
        this.valorizzaParametro(v_grafica.leggiAllParametro());
    }
	/**
	<statement nome="selectPerNomeLike" tipo="select" id="03_taymer">
			<fisso>
				WHERE
				fl_canc !='S' AND
				(
					( ky_cles1_t  LIKE XXXstringaLike1||'%'
					AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
			</fisso>
			<opzionale dipende="XXXstringaLike2">
				 AND ky_cles2_t like XXXstringaLike2 || '%'
			</opzionale>
			<opzionale dipende="XXXstringaLike1">
				   ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%'
			</opzionale>
			<opzionale dipende="XXXstringaLike2">
				   AND ky_cles2_ct like XXXstringaLike2 || '%'
			</opzionale>
			<opzionale dipende="XXXstringaLike1">
				 	)
				 )
			</opzionale>
			<!-- campi grafici -->
			<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
			<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
			<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
			<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
			<!-- fine campi grafici -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
			<opzionale dipende="XXXstringaClet"> AND UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER( XXXstringaClet)||'%' </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<V_grafica> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("V_GRAFICA_selectPerNomeLike");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "V_grafica",
                    this.basicCriteria, session);

			List<V_grafica> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="countPerNomeLike" tipo="count" id="04_taymer">
			<fisso>
				SELECT COUNT (*) FROM v_grafica
				WHERE
				fl_canc !='S' AND
				(
					( ky_cles1_t  LIKE XXXstringaLike1||'%'
					AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
			</fisso>
			<opzionale dipende="XXXstringaLike2">
				 AND ky_cles2_t like XXXstringaLike2 || '%'
			</opzionale>
			<opzionale dipende="XXXstringaLike1">
				   ) OR ( ky_cles1_ct  LIKE XXXstringaLike1||'%'
			</opzionale>
			<opzionale dipende="XXXstringaLike2">
				   AND ky_cles2_ct like XXXstringaLike2 || '%'
			</opzionale>
			<opzionale dipende="XXXstringaLike1">
				 	)
				 )
			</opzionale>
			<!-- campi grafici -->
			<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
			<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
			<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
			<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
			<!-- fine campi grafici -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
			<opzionale dipende="XXXstringaClet"> AND UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER( XXXstringaClet)||'%' </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("V_GRAFICA_countPerNomeLike");

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="selectPerNomeEsatto" tipo="select" id="05">
			<fisso>
				WHERE
				fl_canc !='S' AND
				(
					( ky_cles1_t = XXXstringaEsatta1
					 AND ky_cles2_t = XXXstringaEsatta2
					AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
				   )
				   OR
				   ( ky_cles1_ct  = XXXstringaEsatta1
				   AND ky_cles2_ct = XXXstringaEsatta2
				 	)
				 )
			</fisso>
			<!-- inizio campi ereditati da Tb_marcaCommonDao -->
			<opzionale dipende="XXXparola1"> AND CONTAINS(ds_marca, XXXparola1 ) > 0 </opzionale>
			<opzionale dipende="XXXparola2"> AND CONTAINS(ds_marca, XXXparola2 ) > 0 </opzionale>
			<opzionale dipende="XXXparola3"> AND CONTAINS(ds_marca, XXXparola3 ) > 0 </opzionale>
			<opzionale dipende="XXXparola4"> AND CONTAINS(ds_marca, XXXparola4 ) > 0 </opzionale>
			<opzionale dipende="XXXparola5"> AND CONTAINS(ds_marca, XXXparola5 ) > 0 </opzionale>
			<opzionale dipende="XXXStringaEsattaMarca"> AND UPPER (DS_MARCA) = UPPER(XXXStringaEsattaMarca)</opzionale>
			<!-- fine campi ereditati da Tb_marcaCommonDao -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<V_grafica> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("V_GRAFICA_selectPerNomeEsatto");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "V_grafica",
                    this.basicCriteria, session);

			List<V_grafica> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="countPerNomeEsatto" tipo="count" id="06_taymer">
			<fisso>
				SELECT COUNT (*) FROM v_grafica
				WHERE
				fl_canc !='S' AND
				(
					( ky_cles1_t = XXXstringaEsatta1
					 AND ky_cles2_t = XXXstringaEsatta2
					AND ky_clet2_t LIKE XXXclet2_ricerca||'%'
				   )
				   OR
				   ( ky_cles1_ct  = XXXstringaEsatta1
				   AND ky_cles2_ct = XXXstringaEsatta2
				 	)
				 )
			</fisso>
			<!-- campi grafici -->
			<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
			<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
			<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
			<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
			<!-- fine campi grafici -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("V_GRAFICA_countPerNomeEsatto");

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="selectPerClet" tipo="select" id="07_taymer">
			<fisso>
				WHERE
				fl_canc !='S' AND
				(
					ky_clet1_t || ky_clet2_t  LIKE UPPER( XXXstringaClet)||'%'
				 )
			</fisso>
			<!-- campi grafici -->
			<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
			<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
			<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
			<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
			<!-- fine campi grafici -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<V_grafica> selectPerClet(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			Filter filter = session.enableFilter("V_GRAFICA_selectPerClet");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet));
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "V_grafica",
                    this.basicCriteria, session);

			List<V_grafica> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="countPerClet" tipo="count" id="08_taymer">
			<fisso>
				SELECT COUNT (*) FROM v_grafica
				WHERE
				fl_canc !='S' AND
				(
					ky_clet1_t || ky_clet2_t  LIKE UPPER( XXXstringaClet)||'%'
				 )
			</fisso>
			<!-- campi grafici -->
			<opzionale dipende="XXXtpMaterialeGra"> AND tp_materiale_gra = XXXtpMaterialeGra </opzionale>
			<opzionale dipende="XXXcdSupporto"> AND cd_supporto like XXXcdSupporto </opzionale>
			<opzionale dipende="XXXcdColore"> AND cd_colore = XXXcdColore </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_1"> AND cd_tecnica_dis_1 like XXXcdTecnicaDis_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_2"> AND cd_tecnica_dis_2 like XXXcdTecnicaDis_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaDis_3"> AND cd_tecnica_dis_3 like XXXcdTecnicaDis_3 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_1"> AND cd_tecnica_sta_1 = XXXcdTecnicaSta_1 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_2"> AND cd_tecnica_sta_2 = XXXcdTecnicaSta_2 </opzionale>
			<opzionale dipende="XXXcdTecnicaSta_3"> AND cd_tecnica_sta_3 = XXXcdTecnicaSta_3 </opzionale>
			<opzionale dipende="XXXcdDesignFunz"> AND cd_design_funz = XXXcdDesignFunz </opzionale>
			<!-- fine campi grafici -->
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>\n
			<opzionale dipende="XXXtp_materiale1"> AND ( tp_materiale = XXXtp_materiale1</opzionale>
			<opzionale dipende="XXXtp_materiale2"> OR  tp_materiale = XXXtp_materiale2 </opzionale>
			<opzionale dipende="XXXtp_materiale3"> OR  tp_materiale = XXXtp_materiale3 </opzionale>
			<opzionale dipende="XXXtp_materiale4"> OR  tp_materiale = XXXtp_materiale4 </opzionale>
			<opzionale dipende="XXXtp_materiale5"> OR  tp_materiale = XXXtp_materiale5 </opzionale>
			<opzionale dipende="XXXtp_materiale1"> ) </opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura2"> OR  cd_natura = XXXcd_natura2 </opzionale>
			<opzionale dipende="XXXcd_natura3"> OR  cd_natura = XXXcd_natura3 </opzionale>
			<opzionale dipende="XXXcd_natura4"> OR  cd_natura = XXXcd_natura4 </opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> AND ( tp_record_uni = XXXtp_record_uni1</opzionale>
			<opzionale dipende="XXXtp_record_uni2"> OR  tp_record_uni = XXXtp_record_uni2 </opzionale>
			<opzionale dipende="XXXtp_record_uni3"> OR  tp_record_uni = XXXtp_record_uni3 </opzionale>
			<opzionale dipende="XXXtp_record_uni4"> OR  tp_record_uni = XXXtp_record_uni4 </opzionale>
			<opzionale dipende="XXXtp_record_uni1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
			<opzionale dipende="XXXts_ins_da"> AND to_char(ts_ins,'yyyy-mm-dd')  &gt;= XXXts_ins_da </opzionale>
			<opzionale dipende="XXXts_ins_a"> AND to_char(ts_ins,'yyyy-mm-dd')  &lt;= XXXts_ins_a </opzionale>
			<!-- Date -->
			<!-- tp_aa_pubb_da = tp_aa_pubb_a, se data incerta va sempre bene -->
			<opzionale dipende="XXXtp_aa_pubb"> AND tp_aa_pubb = XXXtp_aa_pubb </opzionale>
			<!-- Gli anni di pubblicazione possono essere di meno cifre di 4, nel qual caso si usa un confronto di stringhe like -->
			<!-- Per ora faccio cosi': se e' settata aa_pubb_da uso un modo , se e' settata aa_pubb_like uso l'altro -->
			<opzionale dipende="XXXaa_pubb_1_da">AND ( aa_pubb_1 &gt;= XXXaa_pubb_1_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_a"> AND  ( aa_pubb_1 &lt;= XXXaa_pubb_1_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_1_like"> AND ( aa_pubb_1 LIKE XXXaa_pubb_1_like||'%' )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_da"> AND ( aa_pubb_2 &gt;= XXXaa_pubb_2_da )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_a"> AND ( aa_pubb_2 &lt;= XXXaa_pubb_2_a )</opzionale>
			<opzionale dipende="XXXaa_pubb_2_like"> AND ( aa_pubb_2 LIKE XXXaa_pubb_2_like||'%' )</opzionale>
			<!-- fine date-->
			<opzionale dipende="XXXcd_paese"> AND cd_paese = XXXcd_paese </opzionale>
			<opzionale dipende="XXXcd_genere_1"> AND ( cd_genere_1 = XXXcd_genere_1 OR cd_genere_2 = XXXcd_genere_1 OR cd_genere_3 = XXXcd_genere_1 OR cd_genere_4 = XXXcd_genere_1</opzionale>
			<opzionale dipende="XXXcd_genere_2">OR cd_genere_1 = XXXcd_genere_2 OR cd_genere_2 = XXXcd_genere_2 OR cd_genere_3 = XXXcd_genere_2 OR cd_genere_4 = XXXcd_genere_2</opzionale>
			<opzionale dipende="XXXcd_genere_3">OR cd_genere_1 = XXXcd_genere_3 OR cd_genere_2 = XXXcd_genere_3 OR cd_genere_3 = XXXcd_genere_3 OR cd_genere_4 = XXXcd_genere_3</opzionale>
			<opzionale dipende="XXXcd_genere_4">OR cd_genere_1 = XXXcd_genere_4 OR cd_genere_2 = XXXcd_genere_4 OR cd_genere_3 = XXXcd_genere_4 OR cd_genere_4 = XXXcd_genere_4</opzionale>
			<opzionale dipende="XXXcd_genere_1"> ) </opzionale>
			<opzionale dipende="XXXcd_lingua_1"> AND ( cd_lingua_1 = XXXcd_lingua_1 OR cd_lingua_2 = XXXcd_lingua_1 OR cd_lingua_3 = XXXcd_lingua_1</opzionale>
			<opzionale dipende="XXXcd_lingua_2"> OR cd_lingua_1 = XXXcd_lingua_2 OR cd_lingua_2 = XXXcd_lingua_2 OR cd_lingua_3 = XXXcd_lingua_2</opzionale>
			<opzionale dipende="XXXcd_lingua_3"> OR cd_lingua_1 = XXXcd_lingua_3 OR cd_lingua_2 = XXXcd_lingua_3 OR cd_lingua_3 = XXXcd_lingua_3</opzionale>
			<opzionale dipende="XXXcd_lingua_1"> ) </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerClet(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			Filter filter = session.enableFilter("V_GRAFICA_countPerClet");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet));
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXstringaClet);

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.rowCount())).uniqueResult();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	/**
	<statement nome="selectPerISBD" tipo="select" id="09_taymer">
			<fisso>
				WHERE
				fl_canc !='S' AND
				  isbd = XXXisbd
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<V_grafica> selectPerISBD(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			Filter filter = session.enableFilter("V_GRAFICA_selectPerISBD");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXisbd, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXisbd));
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.V_graficaCommonDao.XXXisbd);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "V_grafica",
                    this.basicCriteria, session);

			List<V_grafica> result = this.basicCriteria
					.list();
			this.commitTransaction();
			this.closeSession();
			return result;
		} catch (InfrastructureException ife) {
			throw ife;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InfrastructureException();
		}
	}

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return V_grafica.class;
	}
}
