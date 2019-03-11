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
import it.finsiel.sbn.polo.dao.entity.viste.V_composizioneResult;
import it.finsiel.sbn.polo.dao.entity.viste.V_musicaResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_musica_tit_c_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_composizione_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitoloUniformeMusica;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_composizione;
import it.finsiel.sbn.polo.orm.viste.V_musica;
import it.finsiel.sbn.polo.orm.viste.Ve_musica_tit_c_com;
import it.finsiel.sbn.polo.orm.viste.Vl_composizione_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloUniformeMusicaType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.log4j.Category;


/**
 * Classe TitoloUniformeMusicaCerca
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 13-giu-03
 */
public class TitoloUniformeMusicaCerca extends TitoloUniformeCerca {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2165718529069746582L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloUniformeMusicaCerca");

    public TitoloUniformeMusicaCerca() {
        super();
    }

    public TableDao cercaTitoloPerAutore(
        boolean composizione,
        String idAutore,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniformeMusica factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (composizione) {
            Vl_composizione_aut tit = new Vl_composizione_aut();

            Vl_composizione_autResult tavola = new Vl_composizione_autResult(tit);
            tit.setVID(idAutore);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

            tavola.executeCustom("selectPerAutore", ord);
            return tavola;
        } else {
            Vl_composizione_aut tit = new Vl_composizione_aut();
            Vl_composizione_autResult tavola = new Vl_composizione_autResult(tit);
            tit.setVID(idAutore);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

            tavola.executeCustom("selectPerAutore", ord);
            return tavola;
        }

    }

    public TableDao cercaTitoloPerDocumento(
        boolean composizione,
        String idDoc,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniformeMusica factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (composizione) {
            Ve_musica_tit_c_com tit_tit = new Ve_musica_tit_c_com();
            tit_tit.setBID_COLL(idDoc);
            Ve_musica_tit_c_comResult tavola = new Ve_musica_tit_c_comResult(tit_tit);
            valorizzaFiltri(tit_tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerDocumento"));

            tavola.executeCustom("selectPerDocumento", ord);
            return tavola;
        } else {
            Vl_musica_tit_c tit_tit = new Vl_musica_tit_c();
            tit_tit.setBID_COLL(idDoc);
            Vl_musica_tit_cResult tavola = new Vl_musica_tit_cResult(tit_tit);
            valorizzaFiltri(tit_tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerDocumento"));

            tavola.executeCustom("selectPerDocumento", ord);
            return tavola;
        }
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
    public TableDao cercaPerComposizione(
        String titoloOrd,
        String titoloEst,
        String titoloApp,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniformeMusica factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        V_composizione tit = new V_composizione();
        String stringa = null;
        if (titoloOrd!=null) {
        // utilizza le cles della tb_titolo della vista
                stringa = Formattazione.formatta(titoloOrd);
                if (stringa.length() > 6) {
                    tit.settaParametro(TableDao.XXXstringaLike1, stringa.substring(0, 6));
                    tit.settaParametro(TableDao.XXXstringaLike2, stringa.substring(6));
                } else {
                    tit.settaParametro(TableDao.XXXstringaLike1, stringa);
                }
            String clet2 = new ChiaviTitolo().estraiClet2(stringa);
            tit.settaParametro(TableDao.XXXclet2_ricerca,clet2.trim());
        }

//            titoloOrd = NormalizzaNomi.normalizzazioneGenerica(titoloOrd);
//            if (titoloOrd.length()>10) {
//                tit.settaParametro(TableDao.XXXtitoloOrdinamento", titoloOrd.substring(0,10));
//                tit.settaParametro(TableDao.XXXtitoloOrdinamentoLungo", titoloOrd);
//            } else
//                tit.settaParametro(TableDao.XXXtitoloOrdinamento", titoloOrd);
//        }
        if (titoloEst!=null) {
            titoloEst = NormalizzaNomi.normalizzazioneGenerica(titoloEst);
            if (titoloEst.length()>10) {
                tit.settaParametro(TableDao.XXXtitoloEstratto, titoloEst.substring(0,10));
                tit.settaParametro(TableDao.XXXtitoloEstrattoLungo, titoloEst);
            } else
                tit.settaParametro(TableDao.XXXtitoloEstratto, titoloEst);
        }
        if (titoloApp!=null) {
            titoloApp = NormalizzaNomi.normalizzazioneGenerica(titoloApp);
            if (titoloApp.length()>10) {
                tit.settaParametro(TableDao.XXXappellativo, titoloApp.substring(0,10));
                tit.settaParametro(TableDao.XXXappellativoLungo, titoloApp);
            } else
                tit.settaParametro(TableDao.XXXappellativo, titoloApp);
        }
        V_composizioneResult tavola = new V_composizioneResult(tit);
        valorizzaFiltri(tit, cerca);

        factoring.controllaNumeroRecord(conta(tavola, "countPerComposizione"));

        tavola.executeCustom("selectPerComposizione", ord);
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
        boolean composizione,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniformeMusica factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (composizione) {
            V_composizione tit = new V_composizione();
            V_composizioneResult tavola = new V_composizioneResult(tit);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

            tavola.executeCustom("selectPerNomeLike", ord);
            return tavola;
        } else {
            V_musica tit = new V_musica();
            V_musicaResult tavola = new V_musicaResult(tit);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

            tavola.executeCustom("selectPerNomeLike", ord);
            return tavola;
        }
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
        boolean composizione,
        String ord,
        CercaElementoAutType cerca,
        CercaTitoloUniformeMusica factoring)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (composizione) {
            V_composizione tit = new V_composizione();
            V_composizioneResult tavola = new V_composizioneResult(tit);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

            tavola.executeCustom("selectPerNomeEsatto", ord);
            return tavola;
        } else {
            V_musica tit = new V_musica();
            V_musicaResult tavola = new V_musicaResult(tit);
            valorizzaFiltri(tit, cerca);

            factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

            tavola.executeCustom("selectPerNomeEsatto", ord);
            return tavola;
        }
    }

    /** Valorizza i filtri di titolo in base al contenuto del CercatitoloType */
    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaElementoAutType cerca)
        throws EccezioneSbnDiagnostico {
        if (cerca == null)
            return titolo;
        CercaDatiAutType cercaDati = cerca.getCercaDatiAut();
        if (cercaDati == null)
            return titolo;
        super.valorizzaFiltri(titolo, cerca);
        //Setto "false" il non musicale dopo aver valorizzato i filtri col metodo del padre
        //titolo.settaParametro(TableDao.XXXnon_musicale, null);
        // almaviva MODIFICO TOGLIENDO IL PARAMETRO ALTRIMENTI VA IN ERRORE
        HashMap prova = titolo.leggiAllParametro();
        prova.remove(TableDao.XXXnon_musicale);
        //Questo non serve : va sempre su viste musicali
        //titolo.settaParametro(TableDao.XXXfiltroMusicale", "U");
        if (cercaDati instanceof CercaTitoloUniformeMusicaType) {
            //Parametro per cercare solo titoli uniformi musicali.
            CercaTitoloUniformeMusicaType cercam = (CercaTitoloUniformeMusicaType) cercaDati;
            A928 a928 = cercam.getT928();
            if (a928 != null) {
                String orgSint = a928.getB_928();
                if (orgSint != null) {
                    if (orgSint.indexOf("*")>=0)
                        orgSint = orgSint.replace('*', '%');
                    else
                        orgSint = orgSint+"%";
                    titolo.settaParametro(TableDao.XXXorganicoSinteticoComposizione, orgSint);
                }
                String orgAnal = a928.getC_928();
                if (orgAnal != null) {
                    if (orgAnal.indexOf("*") >= 0)
                        orgAnal = orgAnal.replace('*', '%');
                    else
                        orgAnal = orgAnal + "%";
                    titolo.settaParametro(TableDao.XXXorganicoAnaliticoComposizione, orgAnal);
                }
                for (int i = 0; i < a928.getA_928Count(); i++)
                    titolo.settaParametro("XXXformaComposizione" + (i + 1),
                        Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_1", a928.getA_928(i)));
            }
            if (cercam.getT929() != null) {
                A929 a929 = cercam.getT929();
                titolo.settaParametro(TableDao.XXXnumeroOrdine, a929.getA_929());
                titolo.settaParametro(TableDao.XXXnumeroOpera, a929.getB_929());
                titolo.settaParametro(TableDao.XXXnumeroCatalogo, a929.getC_929());
                if (a929.getE_929() != null)
                    titolo.settaParametro(TableDao.XXXtonalita,
                        Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", a929.getE_929()));
            }
            titolo.settaParametro(TableDao.XXXdataInizioDa, cercam.getDataInizio_Da());
            titolo.settaParametro(TableDao.XXXdataInizioA, cercam.getDataInizio_A());
            titolo.settaParametro(TableDao.XXXdataFineDa, cercam.getDataFine_Da());
            titolo.settaParametro(TableDao.XXXdataFineA, cercam.getDataFine_A());
            //if (cercam.getT923()!=null && cercam.getT923().get()!=null)
            //     titolo.settaParametro(TableDao.XXXpresentazione",Decodificatore.getCd_tabella("PRES",cercam.getT923().get()));
        }
        return titolo;
    }

}
