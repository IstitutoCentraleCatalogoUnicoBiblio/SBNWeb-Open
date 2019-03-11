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
import it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Ts_stop_list;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * TODO Da Testare
 * @author Antonio
 *
 */
public class Ts_stop_listResult extends Ts_stop_listCommonDao{
    private Ts_stop_list ts_stop_list;

    public Ts_stop_listResult(Ts_stop_list ts_stop_list) throws InfrastructureException {
        super();
        this.valorizzaParametro(ts_stop_list.leggiAllParametro());
    }
	/**
	       <filter name="TS_STOP_LIST_selectPerKey"
                condition="id_stop_list = :XXXid_stop_list "/>
	 *
	 * @param opzioni
	 * @return List
	 * @throws InfrastructureException
	 */
	public List<Ts_stop_list> selectPerKey(HashMap opzioni)
			throws InfrastructureException {
		try {
			HashMap myOpzioni = (HashMap) opzioni.clone();
			Session session = this.getSession();
			this.beginTransaction();
			this.basicCriteria = session.createCriteria(getTarget());
			Filter filter = session.enableFilter("TS_STOP_LIST_selectPerPolo");

			filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXid_stop_list, opzioni
					.get(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXid_stop_list));

			myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXid_stop_list);

			List<Ts_stop_list> result = this.basicCriteria
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
	 /*
     *         <filter name="TS_STOP_LIST_verificaNomeEnte"
                condition="UPPER tp_stop_list = 'V' AND parola LIKE UPPER(:XXXtp_stop_list)||'%' "/>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Ts_stop_list> verificaNomeEnte(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("TS_STOP_LIST_verificaNomeEnte");

            filter.setParameter(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXtp_stop_list, opzioni
                    .get(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXtp_stop_list));

            myOpzioni.remove(it.finsiel.sbn.polo.dao.common.tavole.Ts_stop_listCommonDao.XXXtp_stop_list);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Ts_stop_list",
                    this.basicCriteria, session);

            List<Ts_stop_list> result = this.basicCriteria
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
    /*
     *         <filter name="TS_STOP_LIST_selectElencoArticoli"
                condition="tp_stop_list = 'A' ORDER BY cd_lingua "/>

    * @param opzioni
    * @return List
    * @throws InfrastructureException
    */
   public List<Ts_stop_list> selectElencoArticoli(HashMap opzioni)
           throws InfrastructureException {
       try {
           HashMap myOpzioni = (HashMap) opzioni.clone();
           Session session = this.getSession();
           this.beginTransaction();
           this.basicCriteria = session.createCriteria(getTarget());
           Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoArticoli");

           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Ts_stop_list",
                   this.basicCriteria, session);

           List<Ts_stop_list> result = this.basicCriteria
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
   /*
        <filter name="TS_STOP_LIST_selectElencoParoleTipiEeD"
                condition="tp_stop_list in ('E','D') "/>
   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoParoleTipiEeD(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoParoleTipiEeD");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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
  /*
        <filter name="TS_STOP_LIST_selectElencoForme"
                condition="tp_stop_list = 'F' ORDER BY cd_lingua "/>
   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoForme(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoForme");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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

  /*
        <filter name="TS_STOP_LIST_selectElencoFormeU"
                condition="tp_stop_list = 'U' ORDER BY cd_lingua "/>
   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoFormeU(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoFormeU");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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

  /*
        <filter name="TS_STOP_LIST_selectElencoParole"
                condition="tp_stop_list = 'P' ORDER BY cd_lingua "/>
   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoParole(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoParole");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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

  /*
        <filter name="TS_STOP_LIST_selectElencoS"
                condition="tp_stop_list = 'S' ORDER BY cd_lingua "/>
   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoS(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoS");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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

  /*
        <filter name="TS_STOP_LIST_selectElencoV"
                condition="tp_stop_list = 'S' ORDER BY cd_lingua "/>

   * @param opzioni
   * @return List
   * @throws InfrastructureException
   */
  public List<Ts_stop_list> selectElencoV(HashMap opzioni)
          throws InfrastructureException {
      try {
          HashMap myOpzioni = (HashMap) opzioni.clone();
          Session session = this.getSession();
          this.beginTransaction();
          this.basicCriteria = session.createCriteria(getTarget());
          Filter filter = session.enableFilter("TS_STOP_LIST_selectElencoV");

          this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                  "Ts_stop_list",
                  this.basicCriteria, session);

          List<Ts_stop_list> result = this.basicCriteria
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
		return Ts_stop_list.class;
	}


}
