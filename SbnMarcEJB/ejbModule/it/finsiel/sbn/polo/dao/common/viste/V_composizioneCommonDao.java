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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/*

<!-- inizio di Tb_composizioneCommonDao -->
<opzionale dipende="XXXformaComposizione1"> AND cd_forma_1 = XXXformaComposizione1 </opzionale>
<opzionale dipende="XXXformaComposizione2"> AND cd_forma_2 = XXXformaComposizione2 </opzionale>
<opzionale dipende="XXXformaComposizione3"> AND cd_forma_3 = XXXformaComposizione3 </opzionale>
<opzionale dipende="XXXorganicoSinteticoComposizione"> AND ds_org_sint like XXXorganicoSinteticoComposizione </opzionale>
<opzionale dipende="XXXorganicoAnaliticoComposizione"> AND ds_org_anal like XXXorganicoAnaliticoComposizione </opzionale>
<opzionale dipende="XXXtonalita"> AND cd_tonalita = XXXtonalita </opzionale>
<opzionale dipende="XXXnumeroOrdine"> AND numero_ordine = XXXnumeroOrdine </opzionale>
<opzionale dipende="XXXnumeroOpera"> AND numero_opera = XXXnumeroOpera </opzionale>
<opzionale dipende="XXXnumeroCatalogo"> AND numero_cat_tem = XXXnumeroCatalogo </opzionale>
<opzionale dipende="XXXtitoloOrdinamento"> AND ky_ord_ric like XXXtitoloOrdinamento || '%' </opzionale>
<opzionale dipende="XXXtitoloEstratto"> AND ky_est_ric like XXXtitoloEstratto || '%' </opzionale>
<opzionale dipende="XXXappellativo"> AND ky_app_ric like XXXappellativo || '%' </opzionale>
<opzionale dipende="XXXdataInizioDa"> AND aa_comp_1 &gt;= XXXdataInizioDa </opzionale>
<opzionale dipende="XXXdataInizioA"> AND aa_comp_1 &lt;= XXXdataInizioA </opzionale>
<opzionale dipende="XXXdataFineDa"> AND aa_comp_2 &gt;= XXXdataFineDa </opzionale>
<opzionale dipende="XXXdataFineA"> AND aa_comp_2 &lt;= XXXdataFineA </opzionale>
<!-- fine di Tb_composizioneCommonDao -->

<!-- campi musicali -->
<opzionale dipende="XXXtipoTesto"> AND tp_testo_letter = XXXtipoTesto </opzionale>
<opzionale dipende="XXXorganicoSintetico"> AND ds_org_sint like XXXorganicoSintetico </opzionale>
<opzionale dipende="XXXorganicoAnalitico"> AND ds_org_anal like XXXorganicoAnalitico </opzionale>
<opzionale dipende="XXXtipoElaborazione"> AND tp_elaborazione = XXXtipoElaborazione </opzionale>
<opzionale dipende="XXXpresentazione"> AND cd_presentazione = XXXpresentazione </opzionale>
<!-- fine campi musicali -->

 */

public class V_composizioneCommonDao extends Tb_titoloCommonDao{

    protected boolean kycleslike = false;
    Tb_composizioneCommonDao composizione;
    Tb_musicaCommonDao musica;
    //Tb_titoloCommonDao titolo;

    public V_composizioneCommonDao() {
        super();
    }


    public void createCriteria(HashMap opzioni) throws InfrastructureException
    {
        try {
            Class cl = V_composizioneCommonDao.class;//this.getClass();
            Method[] metodi = cl.getDeclaredMethods();
            for(int index =0; index<metodi.length; index++){
                if(metodi[index].getName().startsWith("set")){
                        metodi[index].invoke(this,new Object[] { opzioni});
                    //log.debug(metodi[index].getName());
                }
            }
            this.composizione = new Tb_composizioneCommonDao(this.basicCriteria);
            this.musica = new Tb_musicaCommonDao(this.basicCriteria);
            //this.titolo = new Tb_titoloCommonDao(this.basicCriteria);
            this.Kycleslike(this.kycleslike);
            super.createCriteria(opzioni);
            this.composizione.createCriteria(opzioni);
            this.musica.createCriteria(opzioni);
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
        V_composizioneCommonDao aut = new V_composizioneCommonDao();
        aut.createCriteria(new HashMap());
        System.exit(0);
    }
}
