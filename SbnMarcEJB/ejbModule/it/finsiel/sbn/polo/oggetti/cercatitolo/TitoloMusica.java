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
import it.finsiel.sbn.polo.dao.entity.viste.V_musicaResult;
import it.finsiel.sbn.polo.dao.entity.viste.V_musica_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_claResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_impResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_musica;
import it.finsiel.sbn.polo.orm.viste.V_musica_com;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_imp;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;

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
public class TitoloMusica extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8667611552139227377L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloMusica");

    /** Costruttore pubblico */
    public TitoloMusica() {
        super();
    }

    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(titolo, cerca);
        if (cerca instanceof CercaDocMusicaType) {
            CercaDocMusicaType cercam = (CercaDocMusicaType) cerca;
            C128 c128 = cercam.getT128();
            if (c128 != null) {
                String orgSint = c128.getB_128();
                if (orgSint != null) {
                    if (orgSint.indexOf("*")>=0)
                        orgSint = orgSint.replace('*', '%');
                    else
                        orgSint = orgSint+"%";
                    titolo.settaParametro(TableDao.XXXorganicoSintetico, orgSint);
                }
                String orgAnal = c128.getC_128();
                if (orgAnal != null) {
                    if (orgAnal.indexOf("*") >= 0)
                        orgAnal = orgAnal.replace('*', '%');
                    else
                        orgAnal = orgAnal + "%";
                    titolo.settaParametro(TableDao.XXXorganicoAnalitico, orgAnal);
                }
                if (c128.getD_128() != null)
                    titolo.settaParametro(TableDao.XXXtipoElaborazione,
                        Decodificatore.getCd_tabella("Tb_musica", "tp_elaborazione", c128.getD_128()));
            }
            if (cercam.getT125() != null ) {
                if (cercam.getT125().getB_125() != null)
                    titolo.settaParametro(TableDao.XXXtipoTesto,
                        Decodificatore.getCd_tabella(
                            "Tb_musica",
                            "tp_testo_letter",
                            cercam.getT125().getB_125()));
                if (cercam.getT125().getA_125_0() != null)
                    titolo.settaParametro(TableDao.XXXpresentazione,Decodificatore.getCd_tabella("PRES",cercam.getT125().getA_125_0()));
            }
            //if (cercam.getT923()!=null && cercam.getT923().get()!=null)
            //     titolo.settaParametro(TableDao.XXXpresentazione",Decodificatore.getCd_tabella("PRES",cercam.getT923().get()));
        }
        return titolo;
    }

    /**
     * Cerca i titoli grafici legati ad una classe
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerClasse(Vl_musica_cla tit_cla) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_claResult tavola = new Vl_musica_claResult(tit_cla);
        return conta(tavola, "countPerClasse");
    }

    /**
     * Conta i titoli grafici legati ad una classe
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerClasse(Vl_musica_cla tit_cla, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_claResult tavola = new Vl_musica_claResult(tit_cla);


        tavola.executeCustom("selectPerClasse", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerImpronta(Vl_musica_imp mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_impResult tavola = new Vl_musica_impResult(mus);
        return conta(tavola, "countPerImpronta");
    }

    /**
     * Metodo per cercare il titolo con numero di identificativo:
     * ricerca su Tb_titolo con ID.
     * @return il titolo trovato oppure null
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerImpronta(Vl_musica_imp mus, String ord)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_impResult tavola = new Vl_musica_impResult(mus);


        tavola.executeCustom("selectPerImpronta", ord);
        return tavola;
    }
    /**
     * Cerca i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerMarca(Vl_musica_mar tit_mar, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_marResult tavola = new Vl_musica_marResult(tit_mar);


        tavola.executeCustom("selectPerMarca", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerMarca(Vl_musica_mar tit_mar) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_marResult tavola = new Vl_musica_marResult(tit_mar);
        return conta(tavola,"countPerMarca");
    }

    /**
     * Cerca i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTit(Vl_musica_tit_c tit_tit, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_tit_cResult tavola = new Vl_musica_tit_cResult(tit_tit);


        tavola.executeCustom("selectPerDocumento", ordinamento);
        return tavola;
    }

    /**
     * Conta i titoli grafici legati ad un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTit(Vl_musica_tit_c tit_tit) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_tit_cResult tavola = new Vl_musica_tit_cResult(tit_tit);
        return conta(tavola,"countPerDocumento");
    }


    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(V_musica mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        return conta(tavola, "countPerNomeEsatto");
    }

    public int contaTitoloPerNomeEsattoCom(V_musica_com mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        return conta(tavola, "countPerNomeEsatto");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(V_musica mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeEsattoCom(V_musica_com mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(V_musica mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        return conta(tavola, "countPerNomeLike");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo Like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(V_musica mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    public int contaTitoloPerNomeLikeCom(V_musica_com mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        return conta(tavola, "countPerNomeLike");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo Like */
    public TableDao cercaTitoloPerNomeLikeCom(V_musica_com mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(V_musica mus, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        mus.settaParametro(TableDao.XXXstringaClet, clet);
        return conta(tavola, "countPerClet");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(V_musica mus, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musicaResult tavola = new V_musicaResult(mus);
        mus.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;
    }

    public int contaTitoloPerCletCom(V_musica_com mus, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        mus.settaParametro("stringaClet", clet);
        return conta(tavola, "countPerClet");
    }
    public TableDao cercaTitoloPerCletCom(V_musica_com mus, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_musica_comResult tavola = new V_musica_comResult(mus);
        mus.settaParametro("stringaClet", clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }
}
