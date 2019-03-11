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
import it.finsiel.sbn.polo.dao.common.viste.V_audiovisivoResult;
import it.finsiel.sbn.polo.dao.common.viste.V_audiovisivo_comResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo_com;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriAudiovisivoCercaType;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;


/**
 * Classe TitoloAudiovisivo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloAudiovisivo extends Titolo {

	private static final long serialVersionUID = -965484923044404875L;

	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAudiovisivo");

    /** Costruttore pubblico */
    public TitoloAudiovisivo() {
        super();
    }

    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(titolo, cerca);
        if (cerca instanceof CercaDocAudiovisivoType) {
            CercaDocAudiovisivoType cercam = (CercaDocAudiovisivoType) cerca;
//            C115 c115 = cercam.getT115();
            FiltriAudiovisivoCercaType filtri = cercam.getFiltriAudiovisivoCerca();

            if (filtri != null) {
                if (filtri.getA1150() != null)
                    titolo.settaParametro(KeyParameter.XXXtipoVideo, Decodificatore.getCd_tabella("TIVI", filtri.getA1150()));
                if (filtri.getA1158() != null)
                    titolo.settaParametro(KeyParameter.XXXformaPubblicazioneDistribuzione, Decodificatore.getCd_tabella("FODI", filtri.getA1158()));
                if (filtri.getA1159() != null)
                    titolo.settaParametro(KeyParameter.XXXtecnicaVideoregistrazione, Decodificatore.getCd_tabella("TEVI", filtri.getA1159()));
                if (filtri.getA1260() != null)
                    titolo.settaParametro(KeyParameter.XXXformaPubblicazione, Decodificatore.getCd_tabella("FPUB", filtri.getA1260()));
                if (filtri.getA1261() != null)
                    titolo.settaParametro(KeyParameter.XXXvelocita, Decodificatore.getCd_tabella("VELO", filtri.getA1261()));
            }
        }
        return titolo;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(V_audiovisivo aud) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
        return conta(tavola, "countPerNomeEsatto");
    }

    public int contaTitoloPerNomeEsattoCom(V_audiovisivo_com aud) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
        return conta(tavola, "countPerNomeEsatto");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(V_audiovisivo aud, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeEsattoCom(V_audiovisivo_com aud, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(V_audiovisivo aud) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
        return conta(tavola, "countPerNomeLike");
    }

    public int contaTitoloPerNomeLikeCom(V_audiovisivo_com aud) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
        return conta(tavola, "countPerNomeLike");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo Like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(V_audiovisivo aud, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeLikeCom(V_audiovisivo_com aud, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(V_audiovisivo aud, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
		if (clet.length() > 3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, " ");
        }
        return conta(tavola, "countPerClet");
    }

    public int contaTitoloPerCletCom(V_audiovisivo_com aud, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
		if (clet.length() > 3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, " ");
        }
        return conta(tavola, "countPerClet");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(V_audiovisivo aud, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivoResult tavola = new V_audiovisivoResult(aud);
		if (clet.length() > 3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, " ");
        }
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }

    public TableDao cercaTitoloPerCletCom(V_audiovisivo_com aud, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_audiovisivo_comResult tavola = new V_audiovisivo_comResult(aud);
		if (clet.length() > 3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, " ");
        }
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }


}
