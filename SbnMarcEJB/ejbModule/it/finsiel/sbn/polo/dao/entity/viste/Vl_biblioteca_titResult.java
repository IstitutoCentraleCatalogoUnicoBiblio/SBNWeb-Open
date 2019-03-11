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
import it.finsiel.sbn.polo.dao.common.viste.Vl_biblioteca_titCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.viste.Vl_biblioteca_tit;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;

public class Vl_biblioteca_titResult extends it.finsiel.sbn.polo.dao.common.viste.Vl_biblioteca_titCommonDao{

	public Vl_biblioteca_titResult(Vl_biblioteca_tit Vl_biblioteca_tit) throws InfrastructureException {
		super();
        this.valorizzaParametro(Vl_biblioteca_tit.leggiAllParametro());

	}

    /*
    <statement nome="selectPerPolo" tipo="select" id="01">
        <fisso>
                WHERE
                  bid = XXXbid
                  AND cd_polo = XXXcd_polo
            </fisso>
        <opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca = XXXcd_biblioteca</opzionale>
        <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale>
        <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale>
    </statement>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_biblioteca_tit> selectPerPolo(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_BIBLIOTECA_TIT_selectPerPolo");

            filter.setParameter(Vl_biblioteca_titCommonDao.XXXbid,opzioni.get(Vl_biblioteca_titCommonDao.XXXbid));
            filter.setParameter(Vl_biblioteca_titCommonDao.XXXcd_polo,opzioni.get(Vl_biblioteca_titCommonDao.XXXcd_polo));

            myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXbid);
            myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXcd_polo);

           this.createCriteria(myOpzioni);
           this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_biblioteca_tit",
                   this.basicCriteria, session);

            List<Vl_biblioteca_tit> result = this.basicCriteria.list();
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
   <statement nome="selectPerAnagrafe" tipo="select" id="01">
        <fisso>
                WHERE
                  bid = XXXbid
                  AND cd_ana_biblioteca= XXXcd_ana_biblioteca
            </fisso>
        <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale>
        <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale>
    </statement>

     * @param opzioni
     * @return List
     * @throws InfrastructureException
     */
    public List<Vl_biblioteca_tit> selectPerAnagrafe(HashMap opzioni)
            throws InfrastructureException {
        try {
            HashMap myOpzioni = (HashMap) opzioni.clone();
            Session session = this.getSession();
            this.beginTransaction();
            this.basicCriteria = session.createCriteria(getTarget());
            Filter filter = session.enableFilter("VL_BIBLIOTECA_TIT_selectPerAnagrafe");

            filter.setParameter(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca,opzioni.get(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca));

            myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca);

            this.createCriteria(myOpzioni);
            this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                   "Vl_biblioteca_tit",
                   this.basicCriteria, session);

            List<Vl_biblioteca_tit> result = this.basicCriteria.list();
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
    <statement nome="selectPerAnagrafeLike" tipo="select" id="01">
        <fisso>
                WHERE
                  bid = XXXbid
                  AND upper(trim(cd_ana_biblioteca)) like upper(trim(XXXcd_ana_biblioteca))||'%'
            </fisso>
        <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale>
        <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale>
    </statement>

      * @param opzioni
      * @return List
      * @throws InfrastructureException
      */
     public List<Vl_biblioteca_tit> selectPerAnagrafeLike(HashMap opzioni)
             throws InfrastructureException {
         try {
             HashMap myOpzioni = (HashMap) opzioni.clone();
             Session session = this.getSession();
             this.beginTransaction();
             this.basicCriteria = session.createCriteria(getTarget());
             Filter filter = session.enableFilter("VL_BIBLIOTECA_TIT_selectPerAnagrafeLike");
             filter.setParameter(Vl_biblioteca_titCommonDao.XXXbid,opzioni.get(Vl_biblioteca_titCommonDao.XXXbid));
             filter.setParameter(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca,opzioni.get(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca));

             myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXcd_ana_biblioteca);
             myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXbid);
             this.createCriteria(myOpzioni);
             this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                    "Vl_biblioteca_tit",
                    this.basicCriteria, session);

             List<Vl_biblioteca_tit> result = this.basicCriteria.list();
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
    <statement nome="selectPerBid" tipo="select" id="01">
        <fisso>
                WHERE
                  bid = XXXbid
            </fisso>
        <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale>
        <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale>
    </statement>

       * @param opzioni
       * @return List
       * @throws InfrastructureException
       */
      public List<Vl_biblioteca_tit> selectPerBid(HashMap opzioni)
              throws InfrastructureException {
          try {
              HashMap myOpzioni = (HashMap) opzioni.clone();
              Session session = this.getSession();
              this.beginTransaction();
              this.basicCriteria = session.createCriteria(getTarget());
              Filter filter = session.enableFilter("VL_BIBLIOTECA_TIT_selectPerBid");

              filter.setParameter(Vl_biblioteca_titCommonDao.XXXbid,opzioni.get(Vl_biblioteca_titCommonDao.XXXbid));

              myOpzioni.remove(Vl_biblioteca_titCommonDao.XXXbid);

              this.createCriteria(myOpzioni);
              this.basicCriteria = Parameter.setOrdinamento(myOpzioni,
                     "Vl_biblioteca_tit",
                     this.basicCriteria, session);

              List<Vl_biblioteca_tit> result = this.basicCriteria.list();
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
    <statement nome="selectPerFiltraLocalizza" tipo="select" id="Carlo01">
        <fisso>
                WHERE
                  bid = XXXbid
        </fisso>
        <opzionale dipende="XXXfl_possesso"> AND fl_possesso != 'N'</opzionale>
        <opzionale dipende="XXXfl_gestione"> AND fl_gestione != 'N'</opzionale>
        <opzionale dipende="XXXcd_polo"> AND cd_polo = XXXcd_polo</opzionale>
        <opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca IN (</opzionale>
        <opzionale dipende="XXXcdBiblio" ciclico="S" separatore=",">XXXcdBiblio</opzionale>
        <opzionale dipende="XXXcd_biblioteca">)</opzionale>
    </statement>

 */
      public List<Vl_biblioteca_tit> selectPerFiltraLocalizza(HashMap opzioni){
        return null;

      }

	@Override
	public Class<? extends OggettoServerSbnMarc> getTarget() {
		return Vl_biblioteca_tit.class;
	}

}
