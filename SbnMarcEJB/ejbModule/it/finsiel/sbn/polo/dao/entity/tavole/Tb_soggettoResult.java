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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_soggettoCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.util.BuilderUpdate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Tb_soggettoResult extends Tb_soggettoCommonDao {

    private Tb_soggetto tb_soggetto;

    public Tb_soggettoResult(Tb_soggetto tb_soggetto) throws InfrastructureException {
        super();
        this.valorizzaParametro(tb_soggetto.leggiAllParametro());
        this.tb_soggetto = tb_soggetto;
    }

	/**
	<statement nome="selectPerKey" tipo="select" id="01">
			<fisso>
				WHERE
				  cid = XXXcid
				  AND fl_canc != 'S'
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectPerKey");

			filter.setParameter(Tb_soggettoCommonDao.XXXcid, opzioni.get(Tb_soggettoCommonDao.XXXcid));
			myOpzioni.remove(Tb_soggettoCommonDao.XXXcid);

			//almaviva5_20091125
			createCriteria(opzioni);

			List<Tb_soggetto> result = this.basicCriteria.list();
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
	<statement nome="selectPerNomeLike" tipo="select" id="Jenny_06">
			<fisso>
				WHERE
					fl_canc !='S'
					AND ky_cles1_s LIKE XXXstringaLike||'%'

			</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
			<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectPerNomeLike");

			filter.setParameter(Tb_soggettoCommonDao.XXXstringaLike, opzioni
					.get(Tb_soggettoCommonDao.XXXstringaLike));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXstringaLike);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_soggetto",
                    this.basicCriteria, session);

			List<Tb_soggetto> result = this.basicCriteria
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
	<statement nome="selectPerParoleNome" tipo="select" id="Jenny_08">
			<fisso>
				WHERE
				fl_canc !='S' AND
				  	CONTAINS(ds_soggetto, XXXparola1 ) > 0
		  	</fisso>
			<opzionale dipende="XXXparola2"> AND CONTAINS(ds_soggetto, XXXparola2 ) > 0 </opzionale>
			<opzionale dipende="XXXparola3"> AND CONTAINS(ds_soggetto, XXXparola3 ) > 0 </opzionale>
			<opzionale dipende="XXXparola4"> AND CONTAINS(ds_soggetto, XXXparola4 ) > 0 </opzionale>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
			<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_SOGGETTO_selectPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_SOGGETTO_selectPerParoleNome");
				filter.setParameter(Tb_soggettoCommonDao.XXXparola1, opzioni
						.get(Tb_soggettoCommonDao.XXXparola1));
				myOpzioni.remove(Tb_soggettoCommonDao.XXXparola1);
			}

			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_soggetto",
                    this.basicCriteria, session);

			List<Tb_soggetto> result = this.basicCriteria
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
	<statement nome="selectPerNomeEsatto" tipo="select" id="Jenny_10">
			<fisso>
				WHERE
					fl_canc !='S'
					AND ky_cles1_s = XXXstringaEsatta
			</fisso>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectPerNomeEsatto(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectPerNomeEsatto");

			filter.setParameter(Tb_soggettoCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_soggettoCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXstringaEsatta);
			this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_soggetto",
                    this.basicCriteria, session);

			List<Tb_soggetto> result = this.basicCriteria
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
	<statement nome="countPerNomeLike" tipo="count" id="Jenny_07">
			<fisso>
				SELECT COUNT (*) FROM tb_soggetto
				WHERE
					fl_canc !='S'
					AND ky_cles1_s LIKE XXXstringaLike||'%'
			</fisso>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
			<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public Integer countPerNomeLike(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_countPerNomeLike");

			filter.setParameter(Tb_soggettoCommonDao.XXXstringaLike, opzioni
					.get(Tb_soggettoCommonDao.XXXstringaLike));

//			myOpzioni.remove(Tb_soggettoCommonDao.XXXstringaLike);
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
	<statement nome="countPerParoleNome" tipo="count" id="Jenny_09">
			<fisso>
				SELECT COUNT (*) FROM tb_soggetto
				WHERE
				fl_canc !='S' AND
				  	CONTAINS(ds_soggetto, XXXparola1 ) > 0
		  	</fisso>
			<opzionale dipende="XXXparola2"> AND CONTAINS(ds_soggetto, XXXparola2 ) > 0 </opzionale>
			<opzionale dipende="XXXparola3"> AND CONTAINS(ds_soggetto, XXXparola3 ) > 0 </opzionale>
			<opzionale dipende="XXXparola4"> AND CONTAINS(ds_soggetto, XXXparola4 ) > 0 </opzionale>
			<opzionale dipende="XXXlivello_aut_da"> AND cd_livello &gt;= XXXlivello_aut_da </opzionale>
			<opzionale dipende="XXXlivello_aut_a"> AND cd_livello &lt;= XXXlivello_aut_a </opzionale>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
			<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return Integer
	 * @throws InfrastructureException
	 */
	public Integer countPerParoleNome(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = null;
			if (isSessionPostgreSQL(session)) {
				filter = session.enableFilter("TB_SOGGETTO_countPerParoleNome_postgress");
			}else{
				filter = session.enableFilter("TB_SOGGETTO_countPerParoleNome");
				filter.setParameter(Tb_soggettoCommonDao.XXXparola1, opzioni
						.get(Tb_soggettoCommonDao.XXXparola1));
				myOpzioni.remove(Tb_soggettoCommonDao.XXXparola1);
			}

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
	<statement nome="countPerNomeEsatto" tipo="count" id="Jenny_11">
			<fisso>
				SELECT COUNT (*) FROM tb_soggetto
				WHERE
					fl_canc !='S'
					AND ky_cles1_s = XXXstringaEsatta
			</fisso>
			<opzionale dipende="XXXdata_var_Da"> AND to_char(ts_var,'yyyy-mm-dd') &gt;= XXXdata_var_Da </opzionale>
			<opzionale dipende="XXXdata_var_A"> AND to_char(ts_var,'yyyy-mm-dd') &lt;= XXXdata_var_A </opzionale>
			<opzionale dipende="XXXdata_ins_Da"> AND to_char(ts_ins,'yyyy-mm-dd') &gt;= XXXdata_ins_Da </opzionale>
			<opzionale dipende="XXXdata_ins_A"> AND to_char(ts_ins,'yyyy-mm-dd') &lt;= XXXdata_ins_A </opzionale>
			<opzionale dipende="XXXds_soggetto"> AND cd_soggettario = XXXds_soggetto </opzionale>
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
			Filter filter = session.enableFilter("TB_SOGGETTO_countPerNomeEsatto");

			filter.setParameter(Tb_soggettoCommonDao.XXXstringaEsatta, opzioni
					.get(Tb_soggettoCommonDao.XXXstringaEsatta));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXstringaEsatta);
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
	<statement nome="selectEsistenzaId" tipo="select" id="Jenny_12">
			<fisso>
				WHERE
				  cid = XXXcid
			</fisso>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectEsistenzaId(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectEsistenzaId");

			filter.setParameter(Tb_soggettoCommonDao.XXXcid, opzioni
					.get(Tb_soggettoCommonDao.XXXcid));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXcid);
			List<Tb_soggetto> result = this.basicCriteria
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
	<statement nome="selectSimili" tipo="select" id="Jenny_15">
			<fisso>
				WHERE
				  fl_canc != 'S'
				  AND ky_cles1_s = XXXky_cles1_s
			</fisso>
			<opzionale dipende="XXXky_cles2_s"> AND ky_cles2_s = XXXky_cles2_s </opzionale>
			<opzionale dipende="XXXky_cles2_sNULL"> AND ky_cles2_s is null </opzionale>
			<opzionale dipende="XXXcid"> AND cid != XXXcid </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectSimili(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
            // Modifico la modalità di passaggio dei dati per la preparazione dei filtri opzionali
            // in quanto alcuni parametri non necessitano ma essendo settati creano SQL non congruenti in più
            // personalizzo il filtro cid in quanto e settato a != inveche che ==
            HashMap prova = new HashMap();
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXky_cles2_s))
                prova.put(Tb_soggettoCommonDao.XXXky_cles2_s,opzioni.get(Tb_soggettoCommonDao.XXXky_cles2_s));
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXky_cles2_sNULL))
                prova.put(Tb_soggettoCommonDao.XXXky_cles2_sNULL,opzioni.get(Tb_soggettoCommonDao.XXXky_cles2_sNULL));
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXcid))
                prova.put(Tb_soggettoCommonDao.XXXcid_Diverso,opzioni.get(Tb_soggettoCommonDao.XXXcid));

            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXcd_soggettario))
                prova.put(Tb_soggettoCommonDao.XXXcd_soggettario,opzioni.get(Tb_soggettoCommonDao.XXXcd_soggettario));

			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectSimili");

			filter.setParameter(Tb_soggettoCommonDao.XXXky_cles1_s, opzioni
					.get(Tb_soggettoCommonDao.XXXky_cles1_s));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXky_cles1_s);
			this.createCriteria(prova);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_soggetto",
                    this.basicCriteria, session);

			List<Tb_soggetto> result = this.basicCriteria
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
	<statement nome="selectSimiliConferma" tipo="select" id="20_Taymer">
			<fisso>
				WHERE
				  fl_canc != 'S'
				  AND ky_cles1_s = XXXky_cles1_s
				  AND UPPER( TRIM(ds_soggetto) ) = XXXds_soggetto
			</fisso>
			<opzionale dipende="XXXky_cles2_s"> AND ky_cles2_s = XXXky_cles2_s </opzionale>
			<opzionale dipende="XXXky_cles2_sNULL"> AND ky_cles2_s is null </opzionale>
			<opzionale dipende="XXXcid"> AND cid != XXXcid </opzionale>
	</statement>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Tb_soggetto> selectSimiliConferma(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
            // Modifico la modalità di passaggio dei dati per la preparazione dei filtri opzionali
            // in quanto alcuni parametri non necessitano ma essendo settati creano SQL non congruenti in più
            // personalizzo il filtro cid in quanto e settato a != inveche che ==

            HashMap prova = new HashMap();
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXky_cles2_s))
                prova.put(Tb_soggettoCommonDao.XXXky_cles2_s,opzioni.get(Tb_soggettoCommonDao.XXXky_cles2_s));
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXky_cles2_sNULL))
                prova.put(Tb_soggettoCommonDao.XXXky_cles2_sNULL,opzioni.get(Tb_soggettoCommonDao.XXXky_cles2_sNULL));
            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXcid))
                prova.put(Tb_soggettoCommonDao.XXXcid_Diverso,opzioni.get(Tb_soggettoCommonDao.XXXcid));

            if(opzioni.containsKey(Tb_soggettoCommonDao.XXXcd_soggettario))
                prova.put(Tb_soggettoCommonDao.XXXcd_soggettario,opzioni.get(Tb_soggettoCommonDao.XXXcd_soggettario));

			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TB_SOGGETTO_selectSimiliConferma");

			filter.setParameter(Tb_soggettoCommonDao.XXXky_cles1_s, opzioni
					.get(Tb_soggettoCommonDao.XXXky_cles1_s));
			filter.setParameter(Tb_soggettoCommonDao.XXXds_soggetto, opzioni
					.get(Tb_soggettoCommonDao.XXXds_soggetto));

			myOpzioni.remove(Tb_soggettoCommonDao.XXXky_cles1_s);
			myOpzioni.remove(Tb_soggettoCommonDao.XXXds_soggetto);
			this.createCriteria(prova);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Tb_soggetto",
                    this.basicCriteria, session);

			List<Tb_soggetto> result = this.basicCriteria
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

	public void insert(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_soggetto metodo insert invocato");
        Session session = this.getSession();
        this.beginTransaction();
        tb_soggetto.setFL_CANC("N");
        Timestamp now = now();
		tb_soggetto.setTS_INS(now);
        tb_soggetto.setTS_VAR(now);
        session.saveOrUpdate(this.tb_soggetto);
        this.commitTransaction();
        this.closeSession();


	}

	/**
	 * 	<statement nome="update" tipo="update" id="04">
			<fisso>
				UPDATE Tb_soggetto
				 SET
				  ky_cles2_s = XXXky_cles2_s ,
				  cd_livello = XXXcd_livello ,
				  ds_soggetto = XXXds_soggetto ,
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP  ,
				  cd_soggettario = XXXcd_soggettario ,
				  ky_cles1_s = XXXky_cles1_s ,
				  fl_canc = XXXfl_canc ,
				  fl_speciale = XXXfl_speciale
				WHERE
				  cid = XXXcid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
	public void update(Object opzioni) throws InfrastructureException {
		// TODO Auto-generated method stub
        log.debug("Tb_soggetto metodo update invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_soggetto",Tb_soggetto.class);

		buildUpdate.addProperty("ky_cles2_s",this.getParametro().get(KeyParameter.XXXky_cles2_s));
		buildUpdate.addProperty("cd_livello",this.getParametro().get(KeyParameter.XXXcd_livello));
		buildUpdate.addProperty("ds_soggetto",this.getParametro().get(KeyParameter.XXXds_soggetto));
		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("cd_soggettario",this.getParametro().get(KeyParameter.XXXcd_soggettario));
		buildUpdate.addProperty("ky_cles1_s",this.getParametro().get(KeyParameter.XXXky_cles1_s));
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));
		buildUpdate.addProperty("fl_speciale",this.getParametro().get(KeyParameter.XXXfl_speciale));

		buildUpdate.addProperty("cat_sogg",this.getParametro().get(KeyParameter.XXXcat_sogg));
		buildUpdate.addProperty("nota_soggetto",this.getParametro().get(KeyParameter.XXXnota_soggetto));

		buildUpdate.addProperty("fl_condiviso",this.getParametro().get(KeyParameter.XXXfl_condiviso));
		buildUpdate.addProperty("ts_condiviso",this.getParametro().get(KeyParameter.XXXts_condiviso));
		buildUpdate.addProperty("ute_condiviso",this.getParametro().get(KeyParameter.XXXute_condiviso));

		//almaviva5_20120322 evolutive CFI
		buildUpdate.addProperty("cd_edizione",this.getParametro().get(KeyParameter.XXXcd_edizione));

		buildUpdate.addWhere("cid",this.getParametro().get(KeyParameter.XXXcid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();
	}

	/**
	 * 	<statement nome="cancellaSoggetto" tipo="update" id="Jenny_15">
			<fisso>
				UPDATE Tb_soggetto
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP,
				  fl_canc = XXXfl_canc
				WHERE
				  cid = XXXcid
			</fisso>
	</statement>

	 * @param opzioni
	 * @throws InfrastructureException
	 */
    public void cancellaSoggetto(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_soggetto metodo cancellaSoggetto invocato Da implementare");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_soggetto",Tb_soggetto.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());
		buildUpdate.addProperty("fl_canc",this.getParametro().get(KeyParameter.XXXfl_canc));

		buildUpdate.addWhere("cid",this.getParametro().get(KeyParameter.XXXcid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();


    }

    /**
     * 	<statement nome="updateVersione" tipo="update" id="16_taymer">
			<fisso>
				UPDATE Tb_soggetto
				 SET
				  ute_var = XXXute_var ,
				  ts_var = SYSTIMESTAMP
				WHERE
				  cid = XXXcid
			</fisso>
	</statement>

     * @param opzioni
     * @throws InfrastructureException
     */
    public void updateVersione(Object opzioni) throws InfrastructureException {
        // TODO Auto-generated method stub
        log.debug("Tb_soggetto metodo updateVersione invocato");
        Session session = this.getSession();
        this.beginTransaction();
		BuilderUpdate buildUpdate = new BuilderUpdate(session,"Tb_soggetto",Tb_soggetto.class);

		buildUpdate.addProperty("ute_var",this.getParametro().get(KeyParameter.XXXute_var));
		buildUpdate.addProperty("ts_var",now());

		buildUpdate.addWhere("cid",this.getParametro().get(KeyParameter.XXXcid),"=");

		int query = buildUpdate.executeUpdate();
		this.commitTransaction();
		this.closeSession();

    }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Tb_soggetto.class;
	}

}
