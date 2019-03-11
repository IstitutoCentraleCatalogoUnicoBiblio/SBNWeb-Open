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
import it.finsiel.sbn.polo.orm.viste.Vf_titolo_mar;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class Vf_titolo_marResult extends
		it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao {

    public Vf_titolo_marResult(Vf_titolo_mar vf_titolo_mar) throws InfrastructureException {
        super();
        this.valorizzaParametro(vf_titolo_mar.leggiAllParametro());
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
			<opzionale dipende="XXXstringaClet"> AND UPPER(ky_clet1_t) || UPPER(ky_clet2_t)  LIKE UPPER( XXXstringaClet)||'%' </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Vf_titolo_mar> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("VF_TITOLO_MAR_selectPerNomeLike");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vf_titolo_mar",
                    this.basicCriteria, session);

			List<Vf_titolo_mar> result = this.basicCriteria
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
	<statement nome="countPerNomeLike" tipo="count" id="04">
			<fisso>
				SELECT COUNT (distinct (bid) ) FROM vf_titolo_mar
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
			session.enableFilter("VF_TITOLO_MAR_countPerNomeLike");

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.countDistinct("BID"))).uniqueResult();
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
	public List<Vf_titolo_mar> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			session.enableFilter("VF_TITOLO_MAR_selectPerNomeEsatto");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vf_titolo_mar",
                    this.basicCriteria, session);

			List<Vf_titolo_mar> result = this.basicCriteria
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
	<statement nome="countPerNomeEsatto" tipo="count" id="06">
			<fisso>
				SELECT COUNT (distinct (bid) ) FROM vf_titolo_mar
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
			session.enableFilter("VF_TITOLO_MAR_countPerNomeEsatto");

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.countDistinct("BID"))).uniqueResult();
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
	<statement nome="selectPerClet" tipo="select" id="07">
			<fisso>
				WHERE
				fl_canc !='S' AND
				(
					ky_clet1_t || ky_clet2_t  LIKE UPPER( XXXstringaClet)||'%'
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
	public List<Vf_titolo_mar> selectPerClet(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			this.kycleslike=false;
			Filter filter = session.enableFilter("VF_TITOLO_MAR_selectPerClet");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet));
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vf_titolo_mar",
                    this.basicCriteria, session);

			List<Vf_titolo_mar> result = this.basicCriteria
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
	<statement nome="countPerClet" tipo="count" id="08">
			<fisso>
				SELECT COUNT (distinct (bid) ) FROM vf_titolo_mar
				WHERE
				fl_canc !='S' AND
				(
					ky_clet1_t || ky_clet2_t  LIKE UPPER( XXXstringaClet)||'%'
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
			Filter filter = session.enableFilter("VF_TITOLO_MAR_countPerClet");
			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet));
			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vf_titolo_marCommonDao.XXXstringaClet);

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList()
				      .add( Projections.countDistinct("BID"))).uniqueResult();
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
		return Vf_titolo_mar.class;
	}

}
