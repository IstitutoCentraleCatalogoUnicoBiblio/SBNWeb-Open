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
package it.finsiel.sbn.polo.oggetti.cercatitolo;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.viste.V_cartografiaResult;
import it.finsiel.sbn.polo.dao.entity.viste.V_cartografia_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_cartografia_claResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_cartografia_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_cartografia_sogResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_cartografia_theResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_cartografia_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_cartografia;
import it.finsiel.sbn.polo.orm.viste.V_cartografia_com;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_the;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocCartograficoType;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;


/**
 * Classe TitoloMusica.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloCartografia extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2125739196887232767L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCartografia");

    /** Costruttore pubblico */
    public TitoloCartografia() {
        super();
    }

    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(titolo, cerca);
        if (cerca instanceof CercaDocCartograficoType) {
            CercaDocCartograficoType cercac = (CercaDocCartograficoType) cerca;
            C123 c123 = cercac.getT123();
            if (c123 != null) {
                String temp = c123.getA_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXtipoScala, Decodificatore.getCd_tabella("Tb_cartografia","tp_scala", temp));
                }
                temp = c123.getB_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXscalaOrizzontale, temp);
                }
                temp = c123.getC_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXscalaVerticale, temp);
                }
                temp = c123.getD_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXcoordOvest, temp);
                }
                temp = c123.getE_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXcoordEst, temp);
                }
                temp = c123.getF_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXcoordNord, temp);
                }
                temp = c123.getG_123();
                if (temp != null) {
                    titolo.settaParametro(TableDao.XXXcoordSud, temp);
                }
            }
            C120 c120 = cercac.getT120();
            if (c120 != null) {
                if (c120.getA_120_9() != null) {
                    titolo.settaParametro(TableDao.XXXmeridiano, Decodificatore.getCd_tabella("Tb_cartografia","cd_meridiano", c120.getA_120_9()));
                }
            }
        }
        return titolo;
    }

    /**
     * Cerca i titoli cartografici legati ad un soggetto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerSoggetto(Vl_cartografia_sog tit_sog, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_sogResult tavola = new Vl_cartografia_sogResult(tit_sog);
        tavola.executeCustom("selectPerSoggetto", ordinamento);
        return tavola;
    }

    /**
     * Cerca i titoli cartografici legati ad un thesauro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerThesauro(Vl_cartografia_the tit_the, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_theResult tavola = new Vl_cartografia_theResult(tit_the);
        tavola.executeCustom("selectPerThesauro", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli cartografici legati ad un soggetto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerSoggetto(Vl_cartografia_sog tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_sogResult tavola = new Vl_cartografia_sogResult(tit_sog);
        return conta(tavola,"countPerSoggetto");
    }

    /**
     * Conta i titoli cartografici legati ad un thesauro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerThesauro(Vl_cartografia_the tit_the) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_theResult tavola = new Vl_cartografia_theResult(tit_the);
        return conta(tavola,"countPerThesauro");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerClasse(Vl_cartografia_cla tit_cla) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_claResult tavola = new Vl_cartografia_claResult(tit_cla);
        return conta(tavola,"countPerClasse");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerClasse(Vl_cartografia_cla tit_cla, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_claResult tavola = new Vl_cartografia_claResult(tit_cla);


        tavola.executeCustom("selectPerClasse", ordinamento);
        return tavola;
    }

    /**
     * Cerca i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerMarca(Vl_cartografia_mar tit_mar, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_marResult tavola = new Vl_cartografia_marResult(tit_mar);


        tavola.executeCustom("selectPerMarca", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerMarca(Vl_cartografia_mar tit_mar) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_marResult tavola = new Vl_cartografia_marResult(tit_mar);
        return conta(tavola,"countPerMarca");
    }

    /**
     * Cerca i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTit(Vl_cartografia_tit_c tit_tit, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_tit_cResult tavola = new Vl_cartografia_tit_cResult(tit_tit);


        tavola.executeCustom("selectPerTitolo", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTit(Vl_cartografia_tit_c tit_tit) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_cartografia_tit_cResult tavola = new Vl_cartografia_tit_cResult(tit_tit);
        return conta(tavola,"countPerTitolo");
    }


    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(V_cartografia gra) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        return conta(tavola, "countPerNomeEsatto");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(V_cartografia gra, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    public int contaTitoloPerNomeEsattoCom(V_cartografia_com gra) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        return conta(tavola, "countPerNomeEsatto");
    }

    public TableDao cercaTitoloPerNomeEsattoCom(V_cartografia_com gra, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }



    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(V_cartografia gra) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        return conta(tavola, "countPerNomeLike");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(V_cartografia gra, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    public int contaTitoloPerNomeLikeCom(V_cartografia_com gra) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        return conta(tavola, "countPerNomeLike");
    }

    public TableDao cercaTitoloPerNomeLikeCom(V_cartografia_com gra, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }



    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(V_cartografia gra, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        gra.settaParametro(TableDao.XXXstringaClet, clet);
        return conta(tavola, "countPerClet");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(V_cartografia gra, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        gra.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;
    }

    public int contaTitoloPerCletCom(V_cartografia_com gra, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        gra.settaParametro("stringaClet", clet);
        return conta(tavola, "countPerClet");
    }

    public TableDao cercaTitoloPerCletCom(V_cartografia_com gra, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografia_comResult tavola = new V_cartografia_comResult(gra);
        gra.settaParametro("stringaClet", clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;
    }



    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerCoordinate(V_cartografia gra) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        return conta(tavola, "countPerCoordinate");
    }
    /** Esegue la ricerca per coordinate
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerCoordinate(V_cartografia gra, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_cartografiaResult tavola = new V_cartografiaResult(gra);
        tavola.executeCustom("selectPerCoordinate", ordinamento);
        return tavola;
    }

}
