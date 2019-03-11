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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitoloUniforme;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;


/**
 * Classe TitoloUniformeCerca
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 12-giu-03
 */
public class TitoloUniformeCerca extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7983721604358170254L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloUniformeCerca");
    public TitoloUniformeCerca() {
        super();
    }

    public TableDao cercaTitoloPerAutore(
        String idAutore,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniforme factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_aut tit = new Vl_titolo_aut();
        Vl_titolo_autResult tavola = new Vl_titolo_autResult(tit);
        tit.setVID(idAutore);
        valorizzaFiltri(tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

        tavola.executeCustom("selectPerAutore", ord);
        return tavola;

    }

    public TableDao cercaTitoloPerDocumento(
        String idDoc,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniforme factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_c tit_tit = new Vl_titolo_tit_c();
        tit_tit.setBID_COLL(idDoc);
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit_tit);
        valorizzaFiltri(tit_tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerDocumento"));

        tavola.executeCustom("selectPerDocumento", ord);
        return tavola;

    }

    /** Valorizza i filtri di titolo in base al contenuto del CercatitoloType */
    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaElementoAutType cerca)
        throws EccezioneSbnDiagnostico {
        //Con questo stratagemma posso utilizzare le ricerche di Documenti.
        titolo.settaParametro(TableDao.XXXcd_natura1, "A");
        //Devo settare questo in 6 punti in 3 viste e tabelle
        titolo.settaParametro(TableDao.XXXnon_musicale, "vero");
        if (cerca == null)
            return titolo;
        CercaDatiAutType cercaDati = cerca.getCercaDatiAut();
        if (cercaDati == null)
            return titolo;
        SbnRangeDate t005_range = cercaDati.getT005_Range();
        if (t005_range != null) {
            titolo.settaParametro(TableDao.XXXts_var_da, t005_range.getDataDa().toString());
            titolo.settaParametro(TableDao.XXXts_var_a, t005_range.getDataA().toString());
        }
        if (cercaDati.getLivelloAut_Da() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_da,
                Decodificatore.livelloSogliaDa(cercaDati.getLivelloAut_Da().toString()));
        if (cercaDati.getLivelloAut_A() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_a, cercaDati.getLivelloAut_A().toString());

        CanaliCercaDatiAutType canali = cercaDati.getCanaliCercaDatiAut();
        if (canali != null) {
            //luogo.setVID(canali.getT001());
            StringaCercaType stringaCerca = canali.getStringaCerca();
            if (stringaCerca != null) {
                String stringa = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
                if (stringa != null) {
                    stringa = Formattazione.formatta(stringa);
                    if (stringa.length() > 6) {
                        titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa.substring(0, 6));
                        titolo.settaParametro(TableDao.XXXstringaEsatta2, stringa.substring(6));
                    } else {
                        titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa);
                        titolo.settaParametro(TableDao.XXXstringaEsatta2, " ");
                    }
                }
                stringa = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
                if (stringa != null) {
                    stringa = Formattazione.formatta(stringa);
                    if (stringa.length() > 6) {
                        titolo.settaParametro(TableDao.XXXstringaLike1, stringa.substring(0, 6));
                        titolo.settaParametro(TableDao.XXXstringaLike2, stringa.substring(6));
                    } else
                        titolo.settaParametro(TableDao.XXXstringaLike1, stringa);
                }
                String clet2 = new ChiaviTitolo().estraiClet2(stringa);
                titolo.settaParametro(TableDao.XXXclet2_ricerca,clet2.trim());
            }
        }
        return titolo;
    }

    /**
     * @param num
     * @param tipoOrd
     * @param cercaTitolo
     * @param uniforme
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerIsadn(
        String isadn,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniforme factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit = new Tb_titolo();
        tit.setISADN(isadn);
        Tb_titoloResult tavola = new Tb_titoloResult(tit);
        valorizzaFiltri(tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerIsadn"));

        tavola.executeCustom("selectPerIsadn", ord);
        return tavola;
    }

    /**
     * @param num
     * @param tipoOrd
     * @param cercaTitolo
     * @param uniforme
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerStringaLike(
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniforme factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit = new Tb_titolo();
        Tb_titoloResult tavola = new Tb_titoloResult(tit);
        valorizzaFiltri(tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

        tavola.executeCustom("selectPerNomeLike", ord);
        return tavola;
    }

    /**
     * @param num
     * @param tipoOrd
     * @param cercaTitolo
     * @param uniforme
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerStringaEsatta(
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniforme factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit = new Tb_titolo();
        Tb_titoloResult tavola = new Tb_titoloResult(tit);
        valorizzaFiltri(tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

        tavola.executeCustom("selectPerNomeEsatto", ord);
        return tavola;
    }

}
