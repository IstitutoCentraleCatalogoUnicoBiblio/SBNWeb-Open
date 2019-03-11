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
import it.finsiel.sbn.polo.dao.common.viste.V_elettronicoResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_elettronico;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriElettronicoCercaType;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;


/**
 * Classe TitoloElettronico.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloElettronico extends Titolo {

	private static final long serialVersionUID = 251926535861772761L;

	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloElettronico");

    /** Costruttore pubblico */
    public TitoloElettronico() {
        super();
    }

    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(titolo, cerca);
        if (cerca instanceof CercaDocElettronicoType) {
        	CercaDocElettronicoType cercam = (CercaDocElettronicoType) cerca;
            FiltriElettronicoCercaType filtri = cercam.getFiltriElettronicoCerca();
         // almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
         // Gestione nuovi campi specifici per etichetta 135 (filtri per la specificità in interrogazione; campi e liste nelle mappe di visualizzazione, variazione e inserimento)
            if (filtri != null) {
                if (filtri.getA1350() != null)
                    titolo.settaParametro(KeyParameter.XXXtp_risorsa, Decodificatore.getCd_tabella("RIEL",cercam.getFiltriElettronicoCerca().getA1350()));
                if (filtri.getA1351() != null)
                    titolo.settaParametro(KeyParameter.XXXcd_designazione, Decodificatore.getCd_tabella("DESI",cercam.getFiltriElettronicoCerca().getA1351()));
            }
        }
        return titolo;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(V_elettronico ele) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_elettronicoResult tavola = new V_elettronicoResult(ele);
        return conta(tavola, "countPerNomeEsatto");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(V_elettronico ele, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_elettronicoResult tavola = new V_elettronicoResult(ele);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(V_elettronico ele) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_elettronicoResult tavola = new V_elettronicoResult(ele);
        return conta(tavola, "countPerNomeLike");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo Like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(V_elettronico ele, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_elettronicoResult tavola = new V_elettronicoResult(ele);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(V_elettronico ele, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_elettronicoResult tavola = new V_elettronicoResult(ele);
        ele.settaParametro("stringaClet", clet);
        return conta(tavola, "countPerClet");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(V_elettronico ele, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
    	V_elettronicoResult tavola = new V_elettronicoResult(ele);
        ele.settaParametro("stringaClet", clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }
}
