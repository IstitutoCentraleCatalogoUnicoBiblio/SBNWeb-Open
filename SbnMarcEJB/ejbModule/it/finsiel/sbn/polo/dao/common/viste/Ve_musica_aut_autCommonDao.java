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
import it.finsiel.sbn.polo.dao.common.tavole.Tb_autoreCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_musicaCommonDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_titoloCommonDao;
import it.finsiel.sbn.polo.dao.vo.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.criterion.Criterion;

public class Ve_musica_aut_autCommonDao extends Tb_titoloCommonDao {

/*
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

*/




	protected boolean kycleslike = false;
    Tb_musicaCommonDao musica;
    Tb_autoreCommonDao autore;
    //Tb_titoloCommonDao titolo;

	public Ve_musica_aut_autCommonDao() {
		super();
	}



    /*
    <opzionale dipende="XXXStringaLikeAutore1"> AND ky_cles1_a_f like XXXStringaLikeAutore1 || '%' </opzionale>

     */
    private void setStringaLikeAutore1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES1_A_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXStringaLikeAutore1);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaLikeAutore1);
        }
    }
    /*

    <opzionale dipende="XXXStringaLikeAutore2"> AND ky_cles2_a_f like XXXStringaLikeAutore2 || '%' </opzionale>
     */
    private void setStringaLikeAutore2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES2_A_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXStringaLikeAutore2);
        if ((value = this.setParameterLikeEnd(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaLikeAutore2);
        }
    }
    /*
     <opzionale dipende="XXXStringaEsattaAutore1">   AND ky_cles1_a_f = XXXStringaEsattaAutore1 </opzionale>
     */
    private void setStringaEsattaAutore1(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES1_A_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXStringaEsattaAutore1);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaEsattaAutore1);
        }
    }
    /*
     <opzionale dipende="XXXStringaEsattaAutore2"> AND ky_cles2_a_f = XXXStringaEsattaAutore2 </opzionale>
     */
    private void setStringaEsattaAutore2(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CLES2_A_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXStringaEsattaAutore2);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXStringaEsattaAutore2);
        }
    }
    /*
    <opzionale dipende="XXXky_auteur"> AND ky_auteur_f = XXXky_auteur </opzionale>
     */
    private void setKyAuteur(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_AUTEUR_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXky_auteur);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXky_auteur);
        }
    }
    /*
    <opzionale dipende="XXXky_cautun"> AND ky_cautun_f = XXXky_cautun </opzionale>
     */
    private void setKyCautun(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("KY_CAUTUN_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXky_cautun);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXky_cautun);
        }
    }
    /*
     <opzionale dipende="XXXtp_responsabilita"> AND tp_responsabilita_f = XXXtp_responsabilita </opzionale>
     */
    private void setTpResponsabilita(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("TP_RESPONSABILITA_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXtp_responsabilita);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXtp_responsabilita);
        }
    }
    /*
     <opzionale dipende="XXXcd_relazione"> AND cd_relazione_f = XXXcd_relazione </opzionale>     */
    private void setCdRelazione(HashMap opzioni) throws Exception {
        Criterion value;
        Parameter param = new Parameter();
        param.setKey("CD_RELAZIONE_F");
        param.setValueString(opzioni,Ve_musica_aut_autCommonDao.XXXcd_relazione);
        if ((value = this.setParameterEq(param)) != null) {
            this.basicCriteria.add(value);
            opzioni.remove(XXXcd_relazione);
        }
    }


	public void createCriteria(HashMap opzioni) throws InfrastructureException
	{
		try {
			Class cl = Ve_musica_aut_autCommonDao.class;//this.getClass();
			Method[] metodi = cl.getDeclaredMethods();
			for(int index =0; index<metodi.length; index++){
				if(metodi[index].getName().startsWith("set")){
						metodi[index].invoke(this,new Object[] { opzioni});
					//log.debug(metodi[index].getName());
				}
			}

            this.musica = new Tb_musicaCommonDao(this.basicCriteria);
            this.autore = new Tb_autoreCommonDao(this.basicCriteria);
            //this.titolo = new Tb_titoloCommonDao(this.basicCriteria);
            super.createCriteria(opzioni);
            this.musica.createCriteria(opzioni);
            this.autore.createCriteria(opzioni);
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
		Ve_musica_aut_autCommonDao aut = new Ve_musica_aut_autCommonDao();
		aut.createCriteria(new HashMap());
		System.exit(0);
	}
}

