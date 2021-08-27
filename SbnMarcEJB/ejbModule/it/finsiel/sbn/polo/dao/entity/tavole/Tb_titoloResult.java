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
package it.finsiel.sbn.polo.dao.entity.tavole;


import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.sql.Update;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_titoloResult extends Tb_titoloCommonDao{
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();

	private Tb_titolo tb_titolo= null;
    public Tb_titoloResult(Tb_titolo tb_titolo) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_titolo.leggiAllParametro());
        this.tb_titolo = tb_titolo;
    }
	public Tb_titoloResult() {
        // TODO Auto-generated constructor stub
    }
    /**
	<statement nome="selectEsistenzaId" tipo="select" id="02">
			<fisso>
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectEsistenzaId(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectEsistenzaId");


			filter.setParameter(Tb_titoloCommonDao.XXXbid, opzioni
					.get(Tb_titoloCommonDao.XXXbid));
			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);
			List<Tb_titolo> result = this.basicCriteria
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
    public Session abilitaRicercaLocalizzazioni(Session session){
/*    	
        Filter filter = session.enableFilter("TB_TITOLO_LOCALIZZAZIONI");
        //chiaamo il profiler per leggere il polo
        //TODO: INSERIRE LA CHIAMATA AL PROFILER sigla = validator.getCodicePolo();
        String PoloBib = ResourceLoader.getPropertyString("SIGLA_INDICE");
        filter.setParameter("XXXcd_polo",PoloBib);
        filter.setParameter("XXXcd_biblioteca"," FI");
*/
        return session;
    }
	/**
	<statement nome="selectPerKey" tipo="select" id="02">
			<fisso>
				WHERE
				fl_canc !='S' AND
				  bid = XXXbid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			String bid = (String) opzioni.get(Tb_titoloCommonDao.XXXbid);
			log.debug("select titolo per ID: " + bid);

			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            session = this.abilitaRicercaLocalizzazioni(session);

            Filter filter = session.enableFilter("TB_TITOLO_selectPerKey");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, bid);

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);

			//almaviva5_20091028 #3289
			//commentato per impedire che i filtri impostati da query
			//precedenti modifichino la ricerca per chiave
			//ATTENZIONE: questa modifica disattiva il filtro per bib.

            //this.createCriteria(myOpzioni);

			//almaviva5_20100225 #3583
			setFiltroLocBib(myOpzioni);
			opzioni.remove(Tb_titoloCommonDao.XXXcercaTitLocBib);
			//almaviva5_20120605
			setFiltroBibRaccoltaFattizia(myOpzioni);

			List<Tb_titolo> result = new ArrayList<Tb_titolo>(this.basicCriteria.list());

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
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>
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
			<opzionale dipende="XXXesporta_ts_var_da"> AND ts_var &gt;= XXXesporta_ts_var_da </opzionale>
			<opzionale dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND ts_var &gt;= XXXesporta_ts_var_e_ts_ins_da </opzionale>
			<opzionale dipende="XXXesporta_ts_var_a"> AND ts_var  &lt;= XXXesporta_ts_var_a </opzionale>
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
  public List<Tb_titolo> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      session.enableFilter("TB_TITOLO_selectPerNomeLike");

			this.kycleslike=false;

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
        SELECT COUNT (*) FROM Tb_titolo1
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
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>
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
			<opzionale dipende="XXXesporta_ts_var_da"> AND ts_var &gt;= XXXesporta_ts_var_da </opzionale>
			<opzionale dipende="XXXesporta_ts_var_e_ts_ins_da"> AND ts_ins = ts_var AND ts_var &gt;= XXXesporta_ts_var_e_ts_ins_da </opzionale>
			<opzionale dipende="XXXesporta_ts_var_a"> AND ts_var  &lt;= XXXesporta_ts_var_a </opzionale>
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
            session.enableFilter("TB_TITOLO_countPerNomeLike");

			this.kycleslike=false;

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();

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
	<statement nome="selectPerNomeEsatto" tipo="select" id="05_taymer">
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
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>
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
  public List<Tb_titolo> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
            session = this.abilitaRicercaLocalizzazioni(session);

			Filter filter = session.enableFilter("TB_TITOLO_selectPerNomeEsatto");

//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta1, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta1));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta2, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta2));
//			filter.setParameter(Tb_titoloCommonDao.XXXclet2_ricerca, opzioni.get(Tb_titoloCommonDao.XXXclet2_ricerca));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta1, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta1));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta2, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta2));
//
//			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaEsatta1);
//			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaEsatta2);
//			myOpzioni.remove(Tb_titoloCommonDao.XXXclet2_ricerca);
            this.kycleslike=false;

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

			List<Tb_titolo> result = this.basicCriteria
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
        SELECT COUNT (*) FROM Tb_titolo1
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
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXky_editore"> AND  ky_editore like '%' || XXXky_editore || '%'</opzionale>
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
            Filter filter = session.enableFilter("TB_TITOLO_countPerNomeEsatto");

//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta1, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta1));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta2, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta2));
//			filter.setParameter(Tb_titoloCommonDao.XXXclet2_ricerca,  opzioni.get(Tb_titoloCommonDao.XXXclet2_ricerca));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta1, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta1));
//			filter.setParameter(Tb_titoloCommonDao.XXXstringaEsatta2, opzioni.get(Tb_titoloCommonDao.XXXstringaEsatta2));


//			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaEsatta1);
//			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaEsatta2);
//			myOpzioni.remove(Tb_titoloCommonDao.XXXclet2_ricerca);

            this.kycleslike=false;
            this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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
					ky_cles1_t LIKE XXXstringaClet1 || '%'
					AND ky_clet2_t  = XXXstringaClet2
			</fisso>
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
  public List<Tb_titolo> selectPerClet(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectPerClet");

			filter.setParameter(Tb_titoloCommonDao.XXXstringaClet1, opzioni
					.get(Tb_titoloCommonDao.XXXstringaClet1));
			filter.setParameter(Tb_titoloCommonDao.XXXstringaClet2, opzioni
					.get(Tb_titoloCommonDao.XXXstringaClet2));

			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaClet1);
			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaClet2);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
        SELECT COUNT (*) FROM Tb_titolo1
				WHERE
				fl_canc !='S' AND
					ky_cles1_t LIKE XXXstringaClet1 || '%'
					AND ky_clet2_t  = XXXstringaClet2
			</fisso>
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
      Filter filter = session.enableFilter("TB_TITOLO_countPerClet");

			filter.setParameter(Tb_titoloCommonDao.XXXstringaClet1, opzioni
					.get(Tb_titoloCommonDao.XXXstringaClet1));
			filter.setParameter(Tb_titoloCommonDao.XXXstringaClet2, opzioni
					.get(Tb_titoloCommonDao.XXXstringaClet2));

			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaClet1);
			myOpzioni.remove(Tb_titoloCommonDao.XXXstringaClet2);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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
  public List<Tb_titolo> selectPerISBD(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectPerISBD");

			filter.setParameter(Tb_titoloCommonDao.XXXisbd, opzioni
					.get(Tb_titoloCommonDao.XXXisbd));

			myOpzioni.remove(Tb_titoloCommonDao.XXXisbd);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="selectSimili" tipo="select" id="59_taymer">
			<fisso>
				WHERE fl_canc != 'S'
				AND  ky_cles1_t = XXXky_cles1_t
				AND  ky_cles2_t = XXXky_cles2_t
				AND  ky_clet1_t = XXXky_clet1_t
				AND  ky_clet2_t = XXXky_clet2_t
			</fisso>
			<opzionale dipende="XXXbid">AND bid != XXXbid</opzionale>
			<opzionale dipende="XXXcd_paese">AND cd_paese = XXXcd_paese</opzionale>
			<opzionale dipende="XXXcd_lingua_1">AND (cd_lingua_1 = XXXcd_lingua_1
				OR cd_lingua_2 = XXXcd_lingua_1
				OR cd_lingua_3 = XXXcd_lingua_1)</opzionale>
			<opzionale dipende="XXXnaturaSoC">AND cd_natura in ('S','C')</opzionale>
			<opzionale dipende="XXXnaturaMoW">AND cd_natura in ('M','W')</opzionale>
			<opzionale dipende="XXXnaturaBoA">AND cd_natura in ('B','A')</opzionale>
			<opzionale dipende="XXXcd_natura">AND cd_natura = XXXcd_natura</opzionale>
			<opzionale dipende="XXXaa_pubb_1_s">AND aa_pubb_1 = XXXaa_pubb_1_s</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectSimili(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);
      Filter filter = session.enableFilter("TB_TITOLO_selectSimili");

			filter.setParameter(Tb_titoloCommonDao.XXXky_cles1_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_cles1_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_cles2_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_cles2_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_clet1_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_clet1_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_clet2_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_clet2_t));

			myOpzioni.remove(Tb_titoloCommonDao.XXXky_cles1_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_cles2_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_clet1_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_clet2_t);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="selectSimiliTU" tipo="select" id="59bis_taymer">
			<fisso>
				WHERE fl_canc != 'S'
				AND  ky_cles1_t = XXXky_cles1_t
				AND  ky_cles2_t = XXXky_cles2_t
				AND  ky_clet1_t = XXXky_clet1_t
				AND  ky_clet2_t = XXXky_clet2_t
				AND cd_natura IN ('A','B')
			</fisso>
			<opzionale dipende="XXXbid">AND bid != XXXbid</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectSimiliTU(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectSimiliTU");

			filter.setParameter(Tb_titoloCommonDao.XXXky_cles1_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_cles1_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_cles2_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_cles2_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_clet1_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_clet1_t));
			filter.setParameter(Tb_titoloCommonDao.XXXky_clet2_t, opzioni
					.get(Tb_titoloCommonDao.XXXky_clet2_t));

			myOpzioni.remove(Tb_titoloCommonDao.XXXky_cles1_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_cles2_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_clet1_t);
			myOpzioni.remove(Tb_titoloCommonDao.XXXky_clet2_t);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="selectSimiliAntico" tipo="select" id="78_taymer">
			<fisso>
				WHERE fl_canc != 'S'
				AND (
				ky_cles1_t = XXXcles1_0
		</fisso>
		<opzionale dipende="XXXcles2_0">AND ky_cles2_t = XXXcles2_0</opzionale>
		<opzionale dipende="XXXcles1_1">OR ky_cles1_t = XXXcles1_1</opzionale>
		<opzionale dipende="XXXcles2_1">AND ky_cles2_t = XXXcles2_1</opzionale>
		<opzionale dipende="XXXcles1_2">OR ky_cles1_t = XXXcles1_2</opzionale>
		<opzionale dipende="XXXcles2_2">AND ky_cles2_t = XXXcles2_2</opzionale>
		<opzionale dipende="XXXcles1_3">OR ky_cles1_t = XXXcles1_3</opzionale>
		<opzionale dipende="XXXcles2_3">AND ky_cles2_t = XXXcles2_3</opzionale>
		<opzionale dipende="XXXcles1_4">OR ky_cles1_t = XXXcles1_4</opzionale>
		<opzionale dipende="XXXcles2_4">AND ky_cles2_t = XXXcles2_4</opzionale>
		<successivo>
			<fisso>
			)
			</fisso>
		</successivo>
		<opzionale dipende="XXXbid">AND bid != XXXbid</opzionale>
		<opzionale dipende="XXXcd_paese">AND cd_paese = XXXcd_paese</opzionale>
		<opzionale dipende="XXXcd_lingua_1">AND (cd_lingua_1 = XXXcd_lingua_1
			OR cd_lingua_2 = XXXcd_lingua_1
			OR cd_lingua_3 = XXXcd_lingua_1)</opzionale>
		<opzionale dipende="XXXnaturaSoC">AND cd_natura in ('S','C')</opzionale>
		<opzionale dipende="XXXnaturaMoW">AND cd_natura in ('M','W')</opzionale>
		<opzionale dipende="XXXnaturaBoA">AND cd_natura in ('B','A')</opzionale>
		<opzionale dipende="XXXcd_natura">AND cd_natura = XXXcd_natura</opzionale>
		<opzionale dipende="XXXaa_pubb_1_s">AND aa_pubb_1 = XXXaa_pubb_1_s</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectSimiliAntico(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      session.enableFilter("TB_TITOLO_selectSimiliAntico");

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="selectPerIsadn" tipo="select" id="84_taymer">
			<fisso>
				WHERE
				fl_canc !='S' AND
				isadn = XXXisadn
			</fisso>
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectPerIsadn(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectPerIsadn");

			filter.setParameter(Tb_titoloCommonDao.XXXisadn, opzioni
					.get(Tb_titoloCommonDao.XXXisadn));

			myOpzioni.remove(Tb_titoloCommonDao.XXXisadn);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);

      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="countPerIsadn" tipo="count" id="85_taymer">
			<fisso>
        SELECT COUNT (*) FROM Tb_titolo1
				WHERE
				fl_canc !='S' AND
				isadn = XXXisadn
			</fisso>
			<opzionale dipende="XXXnon_musicale"> AND tp_materiale != 'U'</opzionale>
			<opzionale dipende="XXXcd_natura1"> AND ( cd_natura = XXXcd_natura1</opzionale>
			<opzionale dipende="XXXcd_natura1"> ) </opzionale>
			<opzionale dipende="XXXts_var_da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXts_var_da </opzionale>
			<opzionale dipende="XXXts_var_a"> AND to_char(ts_var,'yyyy-mm-dd')  &lt;= XXXts_var_a </opzionale>
			<opzionale dipende="XXXcd_livello_da"> AND cd_livello &gt;= XXXcd_livello_da </opzionale>
			<opzionale dipende="XXXcd_livello_a"> AND cd_livello &lt;= XXXcd_livello_a </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerIsadn(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      Filter filter = session.enableFilter("TB_TITOLO_countPerIsadn");

			filter.setParameter(Tb_titoloCommonDao.XXXisadn, opzioni
					.get(Tb_titoloCommonDao.XXXisadn));

			myOpzioni.remove(Tb_titoloCommonDao.XXXisadn);

			this.createCriteria(myOpzioni);

			Integer result = (Integer) this.basicCriteria.setProjection(
					Projections.projectionList().add(
							Projections.rowCount())).uniqueResult();
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
	<statement nome="selectPerBid" tipo="select_campi" id="95_william">
			<fisso>
        SELECT isbd  FROM Tb_titolo1
				WHERE  fl_canc != 'S'
     		    and bid =XXXbid
     		</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectPerBid(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      session = this.abilitaRicercaLocalizzazioni(session);

      Filter filter = session.enableFilter("TB_TITOLO_selectPerBid");

			filter.setParameter(Tb_titoloCommonDao.XXXbid, opzioni
					.get(Tb_titoloCommonDao.XXXbid));

			myOpzioni.remove(Tb_titoloCommonDao.XXXbid);
      List<Tb_titolo> result = this.basicCriteria
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
	<statement nome="selectCancellatiPerTsVar" tipo="select" id="137_Taymer">
		<fisso>
			WHERE fl_canc = 'S'
			AND ts_var &gt;= XXXesporta_ts_var_da
			AND ts_var  &lt;= XXXesporta_ts_var_a
		</fisso>
		<opzionale dipende="XXXNOOPAC"> AND cd_natura IN ('M','S','W','N','C')</opzionale>
		<opzionale dipende="XXXOPAC"> AND cd_natura IN ('M','S','W','N')
		AND (tp_link != 'F' OR bid = bid_link)</opzionale>
	    <opzionale dipende="XXXmateriale0">AND tp_materiale IN (</opzionale>
		<opzionale dipende="XXXmateriale" ciclico="S" separatore=",">XXXmateriale</opzionale>
		<opzionale dipende="XXXmateriale0">)</opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
  public List<Tb_titolo> selectCancellatiPerTsVar(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
      this.basicCriteria = session.createCriteria(getTarget());
      Filter filter = session.enableFilter("TB_TITOLO_selectCancellatiPerTsVar");

			filter.setParameter(Tb_titoloCommonDao.XXXesporta_ts_var_da, opzioni
					.get(Tb_titoloCommonDao.XXXesporta_ts_var_da));
			filter.setParameter(Tb_titoloCommonDao.XXXesporta_ts_var_a, opzioni
					.get(Tb_titoloCommonDao.XXXesporta_ts_var_a));

			myOpzioni.remove(Tb_titoloCommonDao.XXXesporta_ts_var_da);
			myOpzioni.remove(Tb_titoloCommonDao.XXXesporta_ts_var_a);

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_titolo",
                    this.basicCriteria, session);
            List<Tb_titolo> result = this.basicCriteria.list();
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




  public void insert(Object opzioni) throws InfrastructureException {

        log.debug("Tb_titolo metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        Timestamp ts = now();
		tb_titolo.setTS_INS(ts);
        tb_titolo.setTS_VAR(ts);
        session.saveOrUpdate(this.tb_titolo);
        this.commitTransaction();
        this.closeSession();
        //aggiorna_tb_reticolo(tb_titolo);
	}


////almaviva NON FUNZIONA ADDWHERE IN QUANTO NON RIESCE A DISCRIMINARE GLI oggetti perchè non univoci
////inserisco un chiamata con HQL
//  public void aggiorna_tb_reticolo( Tb_titolo tb_titolo) throws InfrastructureException {
//      // TODO Auto-generated method stub
//      Session session = this.getSession();
//      this.beginTransaction();
//      Query query = session.createSQLQuery("INSERT INTO tb_reticolo_unimarc " +
//    		  "(bid, fl_da_aggiornare, reticolo,  unimarc, reticolo_xml, ute_ins, ts_ins, ute_var, ts_var )" +
//    		  "VALUES (:BID, :FL_DA_AGGIORNARE, :RETICOLO,  :UNIMARC, :RETICOLO_XML, " +
//    		  ":UTE_INS, :TS_INS, :UTE_VAR, :TS_VAR)");
//
//      query.setParameter("BID",tb_titolo.getBID());
//      query.setParameter("FL_DA_AGGIORNARE","S");
//
//      query.setParameter("RETICOLO"," ");
//      query.setParameter("UNIMARC"," ");
//      query.setParameter("RETICOLO_XML"," ");
//
//      query.setParameter("UTE_INS",tb_titolo.getUTE_INS());
//      query.setParameter("TS_INS",tb_titolo.getTS_INS());
//      query.setParameter("UTE_VAR",tb_titolo.getUTE_VAR());
//      query.setParameter("TS_VAR",tb_titolo.getTS_VAR());
//      query.getQueryString();
//      query.executeUpdate();
//      this.commitTransaction();
//      this.closeSession();
//
//  }
//
//
//
//
//
//
//  public boolean esistenza_bid_tb_reticolo(String Bid) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query query = session
//			.createSQLQuery("SELECT count(*) FROM tb_reticolo_unimarc "
//					+ " where bid=:BID");
//
//		query.setParameter("BID", Bid);
//		query.getQueryString();
//		int x = query.list().size();
//		this.commitTransaction();
//		this.closeSession();
//		if(x>0)
//			return true;
//		else
//			return false;
//
//	}
//
//  public boolean esistenza_reticolo_tb_reticolo(String Striga_cerca) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query query = session
//		.createSQLQuery("SELECT count(*) FROM tb_reticolo_unimarc "
//				+ " where bid=:BID)");
//
//		query.setParameter("STRIGA_CERCA", Striga_cerca);
//		query.getQueryString();
//		int x = query.list().size();
//		this.commitTransaction();
//		this.closeSession();
//		if(x>0)
//			return true;
//		else
//			return false;
//
//	}
//
//
//  public boolean verifica_flag_tb_reticolo(String bid) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query  query = session.createSQLQuery("SELECT count(*) FROM tb_reticolo_unimarc "
//					+ " where bid=:BID and fl_da_aggiornare != 'S'" );
//		query.setParameter("BID", bid);
//		query.getQueryString();
//		query.list().size();
//		this.commitTransaction();
//		this.closeSession();
//		if(query.list().size()>0)
//			return true;
//		else
//			return false;
//
//	}
//
//  public String leggi_reticolo(String bid) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query query = session.createSQLQuery("SELECT bid,reticolo_xml FROM tb_reticolo_unimarc where bid=:BID ");
//		query.setParameter("BID", bid);
//		query.getQueryString();
//		Object [] data = (Object []) query.uniqueResult();
//		log.debug(data[0].toString());
//		log.debug(data[1].toString());
//		this.commitTransaction();
//		this.closeSession();
//		if(query.list().size()>0)
//			return data[1].toString();
//		else
//			return "errore";
//
////		 List insurance = session.createSQLQuery("select  {ins.*}  from insurance ins")
////	      .addEntity("ins", Insurance.class)
////	        .list();
////	      for (Iterator it = insurance.iterator(); it.hasNext();) {
////	        Insurance insuranceObject = (Insurance) it.next();
////	        log.debug("ID: " + insuranceObject.getLngInsuranceId());
////	        log.debug("Name: " + insuranceObject.getInsuranceName());
////	      }
////
//
//
//	}
//
//
//
//  public void aggiorna_tb_reticolo( String Bid,
//		  						    String Fl_da_aggiornare,
//		  						    String Reticolo,
//		  						    String Reticolo_Xml,
//		  						    String Ute_ins,
//		  						    Timestamp Ts_ins,
//		  						    String Ute_var,
//		  						    Timestamp Ts_var
//
//  ) throws InfrastructureException {
//      // TODO Auto-generated method stub
//	  if(esistenza_bid_tb_reticolo(Bid)){
//		  //Aggiorno
//		  update_tb_reticolo(Bid,Fl_da_aggiornare,Reticolo,Reticolo_Xml,Ute_ins,Ts_ins,Ute_var,Ts_var);
//	  }
//	  else{
//		  // inserisco
//		  insert_tb_reticolo(Bid,Fl_da_aggiornare,Reticolo,Reticolo_Xml,Ute_ins,Ts_ins,Ute_var,Ts_var);
//	  }
//
//  }
//
//  public void update_tb_reticolo(String Bid, String Fl_da_aggiornare,
//			String Reticolo, String Reticolo_Xml, String Ute_ins,
//			Timestamp Ts_ins, String Ute_var, Timestamp Ts_var
//
//	) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query query = session
//				.createSQLQuery("UPDATE tb_reticolo_unimarc "
//						+ " SET fl_da_aggiornare = 'N', reticolo= :RETICOLO,  unimarc = :UNIMARC, "
//						+ "      reticolo_xml=:RETICOLO_XML, ute_var=:UTE_VAR, ts_var=:TS_VAR "
//						+ " WHERE bid =:BID" );
//
//		query.setParameter("BID", Bid);
//		//query.setParameter("FL_DA_AGGIORNARE", Fl_da_aggiornare);
//
//		query.setParameter("RETICOLO", Reticolo);
//		query.setParameter("UNIMARC", " ");
//		query.setParameter("RETICOLO_XML", Reticolo_Xml);
//
//		query.setParameter("UTE_VAR", Ute_var);
//		query.setParameter("TS_VAR", Ts_var);
//		query.getQueryString();
//		int x = query.executeUpdate();
//		this.commitTransaction();
//		this.closeSession();
//
//	}
//
//  public void insert_tb_reticolo(String Bid, String Fl_da_aggiornare,
//			String Reticolo, String Reticolo_Xml, String Ute_ins,
//			Timestamp Ts_ins, String Ute_var, Timestamp Ts_var
//
//	) throws InfrastructureException {
//		// TODO Auto-generated method stub
//		Session session = this.getSession();
//		this.beginTransaction();
//		Query query = session
//				.createSQLQuery("INSERT INTO tb_reticolo_unimarc "
//						+ "(bid, fl_da_aggiornare, reticolo,  unimarc, reticolo_xml, ute_ins, ts_ins, ute_var, ts_var )"
//						+ "VALUES (:BID, :FL_DA_AGGIORNARE, :RETICOLO,  :UNIMARC, :RETICOLO_XML, "
//						+ ":UTE_INS, :TS_INS, :UTE_VAR, :TS_VAR)");
//
//		query.setParameter("BID", Bid);
//		query.setParameter("FL_DA_AGGIORNARE", Fl_da_aggiornare);
//
//		query.setParameter("RETICOLO", Reticolo);
//		query.setParameter("UNIMARC", " ");
//		query.setParameter("RETICOLO_XML", Reticolo_Xml);
//
//		query.setParameter("UTE_INS", Ute_ins);
//		query.setParameter("TS_INS", Ts_ins);
//		query.setParameter("UTE_VAR", Ute_var);
//		query.setParameter("TS_VAR", Ts_var);
//		query.getQueryString();
//		int x = query.executeUpdate();
//		this.commitTransaction();
//		this.closeSession();
//
//	}





  /**
   * 	<statement nome="update" tipo="update" id="43_taymer">
			<fisso>
				UPDATE Tb_titolo
				 SET
				  cd_lingua_3 = XXXcd_lingua_3 ,
				  cd_lingua_2 = XXXcd_lingua_2 ,
				  cd_lingua_1 = XXXcd_lingua_1 ,
				  cd_agenzia = XXXcd_agenzia ,
				  ky_clet2_t = XXXky_clet2_t ,
				  nota_cat_tit = XXXnota_cat_tit ,
				  ky_clet1_t = XXXky_clet1_t ,
				  aa_pubb_2 = XXXaa_pubb_2 ,
				  cd_livello = XXXcd_livello ,
				  aa_pubb_1 = XXXaa_pubb_1 ,
				  ky_clet1_ct = XXXky_clet1_ct ,
				  cd_natura = XXXcd_natura ,
				  fl_speciale = XXXfl_speciale ,
				  isadn = XXXisadn ,
				  cd_genere_4 = XXXcd_genere_4 ,
				  cd_genere_3 = XXXcd_genere_3 ,
				  cd_genere_2 = XXXcd_genere_2 ,
				  cd_genere_1 = XXXcd_genere_1 ,
				  ky_cles1_ct = XXXky_cles1_ct ,
				  indice_isbd = XXXindice_isbd ,
				  ute_forza_var = XXXute_forza_var ,
				  ky_clet2_ct = XXXky_clet2_ct ,
				  ky_cles2_t = XXXky_cles2_t ,
				  isbd = XXXisbd ,
				  tp_record_uni = XXXtp_record_uni ,
				  ky_editore = XXXky_editore ,
				  cd_norme_cat = XXXcd_norme_cat ,
				  ky_cles1_t = XXXky_cles1_t ,
				  ts_var = SYSTIMESTAMP ,
				  cd_paese = XXXcd_paese ,
				  tp_materiale = XXXtp_materiale ,
				  tp_aa_pubb = XXXtp_aa_pubb ,
				  ute_var = XXXute_var ,
				  nota_inf_tit = XXXnota_inf_tit ,
				  tp_link = XXXtp_link ,
				  bid_link = XXXbid_link ,
				  ky_cles2_ct = XXXky_cles2_ct
				WHERE
				  bid = XXXbid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

   * @param opzioni
 * @throws InfrastructureException
   */

  public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_titolo metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		// Inizio Modifica del 9.04.2009
		// Per effettuare la correzione di una lingua/genere non per modifica ma per eliminazione
//		buildUpdate.addProperty("cd_lingua_3",this.getParametro().get(TableDao.XXXcd_lingua_3));
//		buildUpdate.addProperty("cd_lingua_2",this.getParametro().get(TableDao.XXXcd_lingua_2));
//		buildUpdate.addProperty("cd_lingua_1",this.getParametro().get(TableDao.XXXcd_lingua_1));

		buildUpdate.addProperty("cd_lingua_3",tb_titolo.getCD_LINGUA_3());
		buildUpdate.addProperty("cd_lingua_2",tb_titolo.getCD_LINGUA_2());
		buildUpdate.addProperty("cd_lingua_1",tb_titolo.getCD_LINGUA_1());
		// Fine Modifica del 9.04.2009





		buildUpdate.addProperty("cd_agenzia",this.getParametro().get(TableDao.XXXcd_agenzia));
		buildUpdate.addProperty("ky_clet2_t",this.getParametro().get(TableDao.XXXky_clet2_t));
		buildUpdate.addProperty("nota_cat_tit",this.getParametro().get(TableDao.XXXnota_cat_tit));
		buildUpdate.addProperty("ky_clet1_t",this.getParametro().get(TableDao.XXXky_clet1_t));


//		buildUpdate.addProperty("aa_pubb_2",this.getParametro().get(TableDao.XXXaa_pubb_2));
		buildUpdate.addProperty("aa_pubb_2",tb_titolo.getAA_PUBB_2());


		buildUpdate.addProperty("cd_livello",this.getParametro().get(TableDao.XXXcd_livello));

//		 Inizio Modifica del 20.10.2009 aggiunto anche il controllo su aa_pubbl1
//		buildUpdate.addProperty("aa_pubb_1",this.getParametro().get(TableDao.XXXaa_pubb_1));
		buildUpdate.addProperty("aa_pubb_1",tb_titolo.getAA_PUBB_1());

		buildUpdate.addProperty("ky_clet1_ct",this.getParametro().get(TableDao.XXXky_clet1_ct));
		buildUpdate.addProperty("cd_natura",this.getParametro().get(TableDao.XXXcd_natura));
		buildUpdate.addProperty("fl_speciale",this.getParametro().get(TableDao.XXXfl_speciale));
		buildUpdate.addProperty("isadn",this.getParametro().get(TableDao.XXXisadn));

		// Inizio Modifica del 9.04.2009
		// Per effettuare la correzione di una lingua/genere non per modifica ma per eliminazione
//		buildUpdate.addProperty("cd_genere_4",this.getParametro().get(TableDao.XXXcd_genere_4));
//		buildUpdate.addProperty("cd_genere_3",this.getParametro().get(TableDao.XXXcd_genere_3));
//		buildUpdate.addProperty("cd_genere_2",this.getParametro().get(TableDao.XXXcd_genere_2));
//		buildUpdate.addProperty("cd_genere_1",this.getParametro().get(TableDao.XXXcd_genere_1));

		buildUpdate.addProperty("cd_genere_4",tb_titolo.getCD_GENERE_4());
		buildUpdate.addProperty("cd_genere_3",tb_titolo.getCD_GENERE_3());
		buildUpdate.addProperty("cd_genere_2",tb_titolo.getCD_GENERE_2());
		buildUpdate.addProperty("cd_genere_1",tb_titolo.getCD_GENERE_1());
		// Fine Modifica del 9.04.2009


		buildUpdate.addProperty("ky_cles1_ct",this.getParametro().get(TableDao.XXXky_cles1_ct));
		buildUpdate.addProperty("indice_isbd",this.getParametro().get(TableDao.XXXindice_isbd));
		buildUpdate.addProperty("ute_forza_var",this.getParametro().get(TableDao.XXXute_forza_var));
		buildUpdate.addProperty("ky_clet2_ct",this.getParametro().get(TableDao.XXXky_clet2_ct));
		buildUpdate.addProperty("ky_cles2_t",this.getParametro().get(TableDao.XXXky_cles2_t));
		buildUpdate.addProperty("isbd",this.getParametro().get(TableDao.XXXisbd));
		buildUpdate.addProperty("tp_record_uni",this.getParametro().get(TableDao.XXXtp_record_uni));
		buildUpdate.addProperty("ky_editore",this.getParametro().get(TableDao.XXXky_editore));
		buildUpdate.addProperty("cd_norme_cat",this.getParametro().get(TableDao.XXXcd_norme_cat));
		buildUpdate.addProperty("ky_cles1_t",this.getParametro().get(TableDao.XXXky_cles1_t));
		buildUpdate.addProperty("ts_var",now());
		//almaviva2 27.07.2017 adeguamento a Indice gestione 231
		// buildUpdate.addProperty("cd_paese",this.getParametro().get(TableDao.XXXcd_paese));
		buildUpdate.addProperty("cd_paese",tb_titolo.getCD_PAESE());

		buildUpdate.addProperty("tp_materiale",this.getParametro().get(TableDao.XXXtp_materiale));

		// Inizio Modifica del Gennaio 2015
		// Per effettuare la correzione di una lingua/genere non per modifica ma per eliminazione
		// buildUpdate.addProperty("tp_aa_pubb",this.getParametro().get(TableDao.XXXtp_aa_pubb));
		buildUpdate.addProperty("tp_aa_pubb",tb_titolo.getTP_AA_PUBB());
		// Fine Modifica del Gennaio 2015

// genera errore
//		if(this.getParametro().get(TableDao.XXXtp_aa_pubb).equals("F")){
//			buildUpdate.addProperty("aa_pubb_1",this.getParametro().get(TableDao.XXXaa_pubb_1_s));
//		}
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("nota_inf_tit",this.getParametro().get(TableDao.XXXnota_inf_tit));
		buildUpdate.addProperty("tp_link",this.getParametro().get(TableDao.XXXtp_link));
		buildUpdate.addProperty("bid_link",this.getParametro().get(TableDao.XXXbid_link));
		buildUpdate.addProperty("ky_cles2_ct",this.getParametro().get(TableDao.XXXky_cles2_ct));

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));

		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(TableDao.XXXts_var),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

	}
    public void selectAll(Object opzioni) {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo selectAll invocato Da implementare");

    }


    /**
     * 	<statement nome="updateIndiceIsbd" tipo="update" id="44_taymer">
			<fisso>
				UPDATE Tb_titolo
				 SET
				  indice_isbd = XXXindice_isbd
				WHERE
				  bid = XXXbid
			</fisso>
	</statement>
     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateIndiceIsbd(Object opzioni) throws InfrastructureException {
        log.debug("Tb_titolo metodo updateIndiceIsbd invocato");

        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("indice_isbd",this.getParametro().get(TableDao.XXXindice_isbd));
		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * <statement nome="updateCancellaPerFusione" tipo="update" id="101_taymer">
			<fisso>
				UPDATE Tb_titolo
				 SET
				 fl_canc = 'S' ,
				 tp_link = XXXtp_link ,
				 bid_link = XXXbid_link ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  <!-- AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
				  -->
			</fisso>
	</statement>
     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaPerFusione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo updateCancellaPerFusione invocato");

        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("tp_link",this.getParametro().get(TableDao.XXXtp_link));
		buildUpdate.addProperty("bid_link",this.getParametro().get(TableDao.XXXbid_link));
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();


/*
        UPDATE Tb_titolo
         SET
         fl_canc = 'S' ,
         tp_link = XXXtp_link ,
         bid_link = XXXbid_link ,
         ute_var = XXXute_var ,
         ts_var = SYSTIMESTAMP
        WHERE
          bid = XXXbid
*/
/*
        Session session = this.getSessionUpdate(new HBSbnInterceptor());

        this.beginTransaction();
        Filter filter = session.enableFilter("TB_TITOLO_selectPerISBD");
        filter.setParameter("XXXisbd","12345678");
        tb_titolo.setTS_VAR(now());
        tb_titolo.setFL_CANC("N");
        tb_titolo.setBID(null);
        session.update(this.tb_titolo);
        //this.rollbackTransaction();
//        OracleDialect appo = new OracleDialect();
//        String where = "bid='" + tb_titolo.getBID() +"' and fl_canc='S'";
//        Update update = new Update(appo)
//         .addColumn("fl_canc"  , tb_titolo.getFL_CANC())
//         .addColumn("tp_link"  , tb_titolo.getTP_LINK())
//         .addColumn("bid_link" , tb_titolo.getBID_LINK())
//         .addColumn("ute_var"  , tb_titolo.getUTE_VAR())
//         .addColumn("ts_var"   , tb_titolo.getTS_VAR().toString())
//         .setTableName("tb_titolo");
//       update.setWhere(where);
//       String Sql = update.toStatementString();
//       log.debug(Sql);

        this.rollbackTransaction();
        //this.commitTransaction();
        this.closeSession();
//        Tb_titoloCommonDao Appo = new Tb_titoloCommonDao();
//        generateClearConstraintString("tb_titolo",
//                "where bid='XXXX' and fl_canc='S'");

        */
    }


    /**
     * Generate the SQL UPDATE that updates all the foreign keys to null
     */
    protected String generateClearConstraintString(String tableName,

                                        String sqlWhereString)
    {
        OracleDialect appo = new OracleDialect();
        Update update = new Update(appo)
          .addColumn("PIPPO")
          .setTableName(tableName);
       if (sqlWhereString != null) update.setWhere(sqlWhereString);
       return update.toStatementString();
    }


//    protected String generateUpdateString(final boolean[] includeProperty,
//            final int j, final Object[] oldFields, final boolean useRowId) {
//        OracleDialect appo = new OracleDialect();
//        Update update = new Update(appo).setTableName("Tb_titolo");
//
//        update.addColumns(getPropertyColumnNames(i),propertyColumnUpdateable[i]);
//
//        // select the correct row by either pk or rowid
//        if (useRowId) {
//            update.setPrimaryKeyColumnNames(new String[] { rowIdName }); // TODO:
//                                                                            // eventually,
//                                                                            // rowIdName[j]
//        } else {
//            update.setPrimaryKeyColumnNames(getKeyColumns(j));
//        }
//
//        boolean hasColumns = false;
//        for (int i = 0; i < entityMetamodel.getPropertySpan(); i++) {
//            if (includeProperty[i] && isPropertyOfTable(i, j)) {
//                // this is a property of the table, which we are updating
//                update.addColumns(getPropertyColumnNames(i),
//                        propertyColumnUpdateable[i]);
//                hasColumns = hasColumns || getPropertyColumnSpan(i) > 0;
//            }
//        }
//
//        if (j == 0
//                && isVersioned()
//                && entityMetamodel.getOptimisticLockMode() == Versioning.OPTIMISTIC_LOCK_VERSION) {
//            // this is the root (versioned) table, and we
//            // are using version-based optimistic locking
//            if (includeProperty[getVersionProperty()]) { // if we are not
//                                                            // updating the
//                                                            // version, also
//                                                            // don't check it!
//                update.setVersionColumnName(getVersionColumnName());
//                hasColumns = true;
//            }
//        } else if (entityMetamodel.getOptimisticLockMode() > Versioning.OPTIMISTIC_LOCK_VERSION
//                && oldFields != null) {
//            // we are using "all" or "dirty" property-based optimistic locking
//
//            boolean[] includeInWhere = entityMetamodel.getOptimisticLockMode() == Versioning.OPTIMISTIC_LOCK_ALL ? getPropertyUpdateability()
//                    : // optimistic-lock="all", include all updatable
//                        // properties
//                    includeProperty; // optimistic-lock="dirty", include all
//                                        // properties we are updating this time
//
//            for (int i = 0; i < entityMetamodel.getPropertySpan(); i++) {
//                boolean[] versionability = getPropertyVersionability();
//                Type[] types = getPropertyTypes();
//                boolean include = includeInWhere[i] && isPropertyOfTable(i, j)
//                        && versionability[i];
//                if (include) {
//                    // this property belongs to the table, and it is not
//                    // specifically
//                    // excluded from optimistic locking by
//                    // optimistic-lock="false"
//                    String[] propertyColumnNames = getPropertyColumnNames(i);
//                    boolean[] propertyNullness = types[i].toColumnNullness(
//                            oldFields[i], getFactory());
//                    for (int k = 0; k < propertyNullness.length; k++) {
//                        if (propertyNullness[k]) {
//                            update.addWhereColumn(propertyColumnNames[k]);
//                        } else {
//                            update.addWhereColumn(propertyColumnNames[k],
//                                    " is null");
//                        }
//                    }
//                }
//            }
//
//        }
//
//        if (getFactory().getSettings().isCommentsEnabled()) {
//            update.setComment("update " + getEntityName());
//        }
//
//        return hasColumns ? update.toStatementString() : null;
//    }

    /**
     * <statement nome="updateTitolo" tipo="update" id="75_Jenny">
			<fisso>
				UPDATE Tb_titolo
				 SET
				  ts_var = SYSTIMESTAMP ,
				  ute_var = XXXute_var

				WHERE
 				  fl_canc !='S' AND
				  bid = XXXbid
			</fisso>
     */
    public void updateTitolo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo updateTitolo invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("tp_link",this.getParametro().get(TableDao.XXXtp_link));
		buildUpdate.addProperty("bid_link",this.getParametro().get(TableDao.XXXbid_link));
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("fl_canc","S","!=");
		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }
    public void settaFormatoTsVar(Object opzioni) {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo settaFormatoTsVar invocato Da implementare");

    }

    /**
	<statement nome="updateChiaviEIsbd" tipo="update" id="110_taymer">
	<fisso>
		UPDATE Tb_titolo
		 SET
		  isbd = XXXisbd,
		  indice_isbd = XXXindice_isbd,
		  ts_var = SYSTIMESTAMP,
		  ky_clet2_t = XXXky_clet2_t ,
		  ky_clet1_t = XXXky_clet1_t ,
		  ky_clet1_ct = XXXky_clet1_ct ,
		  ky_cles1_ct = XXXky_cles1_ct ,
		  ky_clet2_ct = XXXky_clet2_ct ,
		  ky_cles2_t = XXXky_cles2_t ,
		  ky_cles1_t = XXXky_cles1_t ,
		  ky_cles2_ct = XXXky_cles2_ct
		WHERE
		  bid = XXXbid
	</fisso>
<opzionale dipende="XXXute_var">AND  ute_var LIKE UPPER(XXXute_var)||'%'</opzionale>
</statement>
     *
     */
    public void updateChiaviEIsbd(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo updateChiaviEIsbd invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("isbd",this.getParametro().get(TableDao.XXXisbd));
		buildUpdate.addProperty("indice_isbd",this.getParametro().get(TableDao.XXXindice_isbd));

		// Viene riportata la modifica di Indice di bug MANTIS 2239 cambiato da opzionale a fisso il setting del campo UTE_VAR
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));

		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ky_clet2_t",this.getParametro().get(TableDao.XXXky_clet2_t));
		buildUpdate.addProperty("ky_clet1_t",this.getParametro().get(TableDao.XXXky_clet1_t));
		buildUpdate.addProperty("ky_clet1_ct",this.getParametro().get(TableDao.XXXky_clet1_ct));
		buildUpdate.addProperty("ky_cles1_ct",this.getParametro().get(TableDao.XXXky_cles1_ct));
		buildUpdate.addProperty("ky_clet2_ct",this.getParametro().get(TableDao.XXXky_clet2_ct));
		buildUpdate.addProperty("ky_cles2_t",this.getParametro().get(TableDao.XXXky_cles2_t));
		buildUpdate.addProperty("ky_cles1_t",this.getParametro().get(TableDao.XXXky_cles1_t));
		buildUpdate.addProperty("ky_cles2_ct",this.getParametro().get(TableDao.XXXky_cles2_ct));


		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");

//		 Viene ateriscato quest'IF perchè non se ne capisce il senso (Vedi anche sopra)
//		if(this.getParametro().containsKey(TableDao.XXXute_var))
//			buildUpdate.addWhereLikeEND("ute_var","UPPER (" +this.getParametro().get(TableDao.XXXute_var)+")");
//		buildUpdate.addWhere("ute_var","("+this.getParametro().get(TableDao.XXXute_var)+")||'%'","LIKE UPPER");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();


    }
    /**
     * 	<statement nome="updateCancellaTitolo" tipo="update" id="76_taymer">
			<fisso>
				UPDATE Tb_titolo
				 SET
				 fl_canc = 'S' ,
				 ute_var = XXXute_var ,
				 ts_var = SYSTIMESTAMP
				WHERE
				  bid = XXXbid
				  AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateCancellaTitolo(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_titolo metodo updateCancellaTitolo invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("fl_canc","S");
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("ts_var",now());


		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(TableDao.XXXts_var),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

    /**
     * 	<statement nome="updateNatura" tipo="update" id="75_2">
			<fisso>
				UPDATE Tb_titolo
				 SET
				  ts_var = SYSTIMESTAMP ,
				  ute_var = XXXute_var ,
				  cd_natura = XXXcd_natura

				WHERE
 				  fl_canc !='S' AND
				  bid = XXXbid AND
				  to_char(ts_var,'yyyymmddhh24miss.FF') = XXXts_var
			</fisso>
	</statement>
     * @throws InfrastructureException

     *
     */
    public void updateNatura(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_titolo",Tb_titolo.class);

		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("ute_var",this.getParametro().get(TableDao.XXXute_var));
		buildUpdate.addProperty("cd_natura",this.getParametro().get(TableDao.XXXcd_natura));


		buildUpdate.addWhere("fl_canc","S","!=");
		buildUpdate.addWhere("bid",this.getParametro().get(TableDao.XXXbid),"=");
		buildUpdate.addWhere("ts_var",this.getParametro().get(TableDao.XXXts_var),"=");
		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }


    public void selectPerTsVarOpac(Object opzioni) {
        // TODO Auto-generated method stub

    }

    public void selectFusiPerTsVar(Object opzioni) {
        // TODO Auto-generated method stub

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_titolo.class;
	}

}
