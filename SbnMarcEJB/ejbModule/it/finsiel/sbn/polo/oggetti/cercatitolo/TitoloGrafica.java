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
import it.finsiel.sbn.polo.dao.entity.viste.V_graficaResult;
import it.finsiel.sbn.polo.dao.entity.viste.V_grafica_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_grafica_claResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_grafica_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_grafica_sogResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_grafica_theResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_grafica_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_grafica;
import it.finsiel.sbn.polo.orm.viste.V_grafica_com;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_the;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocGraficaType;

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
public class TitoloGrafica extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6367539634747274590L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloGrafica");

    /** Costruttore pubblico */
    public TitoloGrafica() {
        super();
    }

    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(titolo, cerca);
        if (cerca instanceof CercaDocGraficaType) {
            CercaDocGraficaType cercam = (CercaDocGraficaType) cerca;
            C116 c116 = cercam.getT116();
            if (c116 != null) {
                if (c116.getA_116_0() != null)
                    titolo.settaParametro(TableDao.XXXtpMaterialeGra,
                        Decodificatore.getCd_tabella("Tb_grafica", "tp_materiale_gra", c116.getA_116_0()));
                if (c116.getA_116_1() != null)
                    titolo.settaParametro(TableDao.XXXcdSupporto,
                        Decodificatore.getCd_tabella("Tb_grafica", "cd_supporto", c116.getA_116_1()));
                if (c116.getA_116_3() != null)
                    titolo.settaParametro(TableDao.XXXcdColore,
                        Decodificatore.getCd_tabella("Tb_grafica", "cd_colore", c116.getA_116_3()));
                for (int i = 0; i < c116.getA_116_4Count(); i++)
                    titolo.settaParametro("XXXcdTecnicaDis_" + (i + 1),
                        Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_1", c116.getA_116_4(i)));
                for (int i = 0; i < c116.getA_116_10Count(); i++)
                    titolo.settaParametro("XXXcdTecnicaSta_" + (i + 1),
                        Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_1", c116.getA_116_10(i)));
                if (c116.getA_116_16() != null)
                    titolo.settaParametro(TableDao.XXXcdDesignFunz,
                        Decodificatore.getCd_tabella("Tb_grafica", "cd_design_funz", c116.getA_116_16()));
            }
        }
        return titolo;
    }
    /**
     * Cerca i titoli grafici legati ad un soggetto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerSoggetto(Vl_grafica_sog tit_sog, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_sogResult tavola = new Vl_grafica_sogResult(tit_sog);


        tavola.executeCustom("selectPerSoggetto", ordinamento);
        return tavola;
    }

    /**
     * Cerca i titoli grafici legati ad un thesauro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerThesauro(Vl_grafica_the tit_the, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_theResult tavola = new Vl_grafica_theResult(tit_the);
        tavola.executeCustom("selectPerThesauro", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un soggetto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerSoggetto(Vl_grafica_sog tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_sogResult tavola = new Vl_grafica_sogResult(tit_sog);
        return conta(tavola,"countPerSoggetto");
    }

    /**
     * Conta i titoli grafici legati ad un thesauro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerThesauro(Vl_grafica_the tit_the) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_grafica_theResult tavola = new Vl_grafica_theResult(tit_the);
        return conta(tavola,"countPerThesauro");
    }

    /**
     * Cerca i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerMarca(Vl_grafica_mar tit_mar, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_marResult tavola = new Vl_grafica_marResult(tit_mar);


        tavola.executeCustom("selectPerMarca", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerMarca(Vl_grafica_mar tit_mar) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_marResult tavola = new Vl_grafica_marResult(tit_mar);
        return conta(tavola,"countPerMarca");
    }

    /**
     * Cerca i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTit(Vl_grafica_tit_c tit_tit, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_tit_cResult tavola = new Vl_grafica_tit_cResult(tit_tit);


        tavola.executeCustom("selectPerTitolo", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTit(Vl_grafica_tit_c tit_tit) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_tit_cResult tavola = new Vl_grafica_tit_cResult(tit_tit);
        return conta(tavola,"countPerTitolo");
    }


    /**
     * Cerca i titoli grafici legati ad una classe
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerClasse(Vl_grafica_cla tit_cla) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_claResult tavola = new Vl_grafica_claResult(tit_cla);
        return conta(tavola,"countPerClasse");
    }

    /**
     * Conta i titoli grafici legati ad una classe
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerClasse(Vl_grafica_cla tit_cla, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_grafica_claResult tavola = new Vl_grafica_claResult(tit_cla);


        tavola.executeCustom("selectPerClasse", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(V_grafica mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(mus);
        return conta(tavola, "countPerNomeEsatto");
    }

    public int contaTitoloPerNomeEsattoCom(V_grafica_com com) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(com);
        return conta(tavola, "countPerNomeEsatto");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(V_grafica mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(mus);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeEsattoCom(V_grafica_com com, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(com);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(V_grafica mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(mus);
        return conta(tavola, "countPerNomeLike");
    }

    public int contaTitoloPerNomeLikeCom(V_grafica_com com) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(com);
        return conta(tavola, "countPerNomeLike");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo Like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(V_grafica mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(mus);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeLikeCom(V_grafica_com com, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(com);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(V_grafica gra, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(gra);
        gra.settaParametro(TableDao.XXXstringaClet, clet);
        return conta(tavola, "countPerClet");
    }

    public int contaTitoloPerCletCom(V_grafica_com gra, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(gra);
        gra.settaParametro("stringaClet", clet);
        return conta(tavola, "countPerClet");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(V_grafica gra, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_graficaResult tavola = new V_graficaResult(gra);
        gra.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerCletCom(V_grafica_com gra, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_grafica_comResult tavola = new V_grafica_comResult(gra);
        gra.settaParametro("stringaClet", clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;
    }
}
