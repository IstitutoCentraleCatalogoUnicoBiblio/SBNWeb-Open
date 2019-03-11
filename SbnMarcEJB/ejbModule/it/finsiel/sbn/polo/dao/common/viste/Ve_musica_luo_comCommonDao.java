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
package it.finsiel.sbn.polo.dao.common.viste;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_composizioneCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_musicaCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Criterion;

public class Ve_musica_luo_comCommonDao extends Tb_titoloCommonDao {



/*
            <!-- inizio di Tb_composizioneCommonDao -->
            EREDITA' Tb_composizione <opzionale dipende="XXXformaComposizione1"> AND cd_forma_1 = XXXformaComposizione1 </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXformaComposizione2"> AND cd_forma_2 = XXXformaComposizione2 </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXformaComposizione3"> AND cd_forma_3 = XXXformaComposizione3 </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXorganicoSinteticoComposizione"> AND ds_org_sint like XXXorganicoSinteticoComposizione </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXorganicoAnaliticoComposizione"> AND ds_org_anal like XXXorganicoAnaliticoComposizione </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXtonalita"> AND cd_tonalita = XXXtonalita </opzionale>

            SONO IN  LIKE
                   <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine like '%' || XXXnumeroOrdine || '%' </opzionale>
                   <opzionale dipende="XXXnumeroOpera"> AND numero_opera like '%' || XXXnumeroOpera || '%' </opzionale>

            EREDITA' Tb_composizione <opzionale dipende="XXXnumeroCatalogo"> AND numero_cat_tem = XXXnumeroCatalogo </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXtitoloOrdinamento"> AND ky_ord_ric like XXXtitoloOrdinamento || '%' </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXtitoloEstratto"> AND ky_est_ric like XXXtitoloEstratto || '%' </opzionale>
            EREDITA' Tb_composizione <opzionale dipende="XXXappellativo"> AND ky_app_ric like XXXappellativo || '%' </opzionale>
            DEFAULT <opzionale dipende="XXXdataInizioDa"> AND aa_comp_1 &gt;= XXXdataInizioDa </opzionale>
            DEFAULT <opzionale dipende="XXXdataInizioA"> AND aa_comp_1 &lt;= XXXdataInizioA </opzionale>
            DEFAULT <opzionale dipende="XXXdataFineDa"> AND aa_comp_2 &gt;= XXXdataFineDa </opzionale>
            DEFAULT <opzionale dipende="XXXdataFineA"> AND aa_comp_2 &lt;= XXXdataFineA </opzionale>

    */


    protected boolean kycleslike = false;
    Tb_musicaCommonDao musica;
    Tb_composizioneCommonDao composizione;
    //Tb_titoloCommonDao titolo;


    public Ve_musica_luo_comCommonDao() {
        super();
    }



/*
     DA VERIFICARE <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine = XXXnumeroOrdine </opzionale>
     DA VERIFICARE <opzionale dipende="XXXnumeroOpera"> AND numero_opera = XXXnumeroOpera </opzionale>
                   POSSONO ANCHE ANDARE IN LIKE Quindi Faccio l'overwrite nelle vitre corrispondenti
                   <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine like '%' || XXXnumeroOrdine || '%' </opzionale>
                   <opzionale dipende="XXXnumeroOpera"> AND numero_opera like '%' || XXXnumeroOpera || '%' </opzionale>
*/

    /*
    <opzionale dipende="XXXnumeroOpera"> AND numero_opera like '%' || XXXnumeroOpera || '%' </opzionale>
     */
    public void setNumeroOpera(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("NUMERO_OPERA");
        param.setValueString(opzioni,Ve_musica_luo_comCommonDao.XXXnumeroOpera);

        if ((value = this.setParameterLikeAnywhere(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Ve_musica_luo_comCommonDao.XXXnumeroOpera);
        }
    }

    /*
    <opzionale dipende="XXXnumeroOrdine"> AND numero_ordine like '%' || XXXnumeroOrdine || '%' </opzionale>
     */
    public void setNumeroOrdine(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("NUMERO_ORDINE");
        param.setValueString(opzioni,Ve_musica_luo_comCommonDao.XXXnumeroOrdine);
        if ((value = this.setParameterLikeAnywhere(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(Ve_musica_luo_comCommonDao.XXXnumeroOrdine);
        }
    }




    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = Ve_musica_luo_comCommonDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
            for(int index =0; index<metodi.length; index++){
                if(metodi[index].getName().startsWith("set")){
                        metodi[index].invoke(this,new Object[] { opzioni});
                    //log.debug(metodi[index].getName());
                }
            }
            this.musica = new Tb_musicaCommonDao(this.basicCriteria);
            this.composizione = new Tb_composizioneCommonDao(this.basicCriteria);
            //this.titolo = new Tb_titoloCommonDao(this.basicCriteria);
            super.createCriteria(opzioni);
            this.musica.createCriteria(opzioni);
            this.composizione.createCriteria(opzioni);
            //this.titolo.createCriteria(opzioni);
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
        Ve_musica_luo_comCommonDao aut = new Ve_musica_luo_comCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}
