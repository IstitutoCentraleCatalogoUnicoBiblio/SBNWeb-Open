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
import it.finsiel.sbn.polo.orm.viste.Vf_cartografia_luo;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class Vf_cartografia_luoResult extends it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_luoCommonDao{

    public Vf_cartografia_luoResult(Vf_cartografia_luo vf_cartografia_luo) throws InfrastructureException {
        super();
        this.valorizzaParametro(vf_cartografia_luo.leggiAllParametro());
    }
	/**
	<statement nome="selectPerCoordinate" tipo="select" id="03_taymer">
			<fisso>
				WHERE
				tp_scala = XXXtipoScala
			</fisso>
			<!-- campi cartografici: sono canali di ricerca, ma non da utilizzarsi in mutua esclusione -->
			<opzionale dipende="XXXcoordOvest"> AND  longitudine_ovest like XXXcoordOvest || '%'</opzionale>
			<opzionale dipende="XXXcoordEst"> AND  longitudine_est like XXXcoordEst || '%'</opzionale>
			<opzionale dipende="XXXcoordNord"> AND  latitudine_nord like XXXcoordNord || '%'</opzionale>
			<opzionale dipende="XXXcoordSud"> AND  latitudine_sud like XXXcoordSud || '%'</opzionale>
			<!-- filtri cartografici -->
			<opzionale dipende="XXXtipoScala"> AND  tp_scala = XXXtipoScala</opzionale>
			<opzionale dipende="XXXmeridiano"> AND  cd_meridiano = XXXmeridiano</opzionale>
			<opzionale dipende="XXXscalaOrizzontale"> AND  scala_oriz like XXXscalaOrizzontale || '%'</opzionale>
			<opzionale dipende="XXXscalaVerticale"> AND  scala_vert like XXXscalaVerticale || '%'</opzionale>

			<!-- fine campi cartografici -->

			<!-- inizio campi ereditati da Tb_luogoCommonDao -->
			<opzionale dipende="XXXStringaLikeLuogo"> AND UPPER(ky_norm_luogo) like '%'||UPPER(XXXStringaLikeLuogo)||'%' </opzionale>
			<opzionale dipende="XXXStringaEsattaLuogo"> AND UPPER(ky_norm_luogo)=UPPER(XXXStringaEsattaLuogo) </opzionale>
			<!-- fine campi ereditati da Tb_luogoCommonDao -->
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
	public List<Vf_cartografia_luo> selectPerCoordinate(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vf_cartografia_luo.class);
			Filter filter = session.enableFilter("VF_CARTOGRAFIA_LUO_selectPerCoordinate");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_luoCommonDao.XXXtipoScala, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_luoCommonDao.XXXtipoScala));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_luoCommonDao.XXXtipoScala);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vf_cartografia_luo",
                    this.basicCriteria, session);

			List<Vf_cartografia_luo> result = this.basicCriteria
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
	<statement nome="countPerCoordinate" tipo="count" id="04_taymer">
			<fisso>
				SELECT COUNT (distinct (bid) ) FROM vf_cartografia_luo
				WHERE
				tp_scala = XXXtipoScala
			</fisso>
			<!-- campi cartografici: sono canali di ricerca, ma non da utilizzarsi in mutua esclusione -->
			<opzionale dipende="XXXcoordOvest"> AND  longitudine_ovest like XXXcoordOvest || '%'</opzionale>
			<opzionale dipende="XXXcoordEst"> AND  longitudine_est like XXXcoordEst || '%'</opzionale>
			<opzionale dipende="XXXcoordNord"> AND  latitudine_nord like XXXcoordNord || '%'</opzionale>
			<opzionale dipende="XXXcoordSud"> AND  latitudine_sud like XXXcoordSud || '%'</opzionale>
			<!-- filtri cartografici -->
			<opzionale dipende="XXXtipoScala"> AND  tp_scala = XXXtipoScala</opzionale>
			<opzionale dipende="XXXmeridiano"> AND  cd_meridiano = XXXmeridiano</opzionale>
			<opzionale dipende="XXXscalaOrizzontale"> AND  scala_oriz like XXXscalaOrizzontale || '%'</opzionale>
			<opzionale dipende="XXXscalaVerticale"> AND  scala_vert like XXXscalaVerticale || '%'</opzionale>

			<!-- fine campi cartografici -->

			<!-- inizio campi ereditati da Tb_luogoCommonDao -->
			<opzionale dipende="XXXStringaLikeLuogo"> AND UPPER(ky_norm_luogo) like '%'||UPPER(XXXStringaLikeLuogo)||'%' </opzionale>
			<opzionale dipende="XXXStringaEsattaLuogo"> AND UPPER(ky_norm_luogo)=UPPER(XXXStringaEsattaLuogo) </opzionale>
			<!-- fine campi ereditati da Tb_luogoCommonDao -->
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
	public Integer countPerCoordinate(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(Vf_cartografia_luo.class);
			Filter filter = session.enableFilter("VF_CARTOGRAFIA_LUO_countPerCoordinate");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_autCommonDao.XXXtipoScala, opzioni
					.get(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_autCommonDao.XXXtipoScala));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.viste.Vf_cartografia_autCommonDao.XXXtipoScala);

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.countDistinct("BID"))).uniqueResult();
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
}
