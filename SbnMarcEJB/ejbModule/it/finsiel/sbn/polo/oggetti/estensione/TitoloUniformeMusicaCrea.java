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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.oggetti.Composizione;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.orm.Tb_composizione;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe TitoloUniformeCrea.java
 * <p>
 * Oggetto utile per la creazione dei titoli uniformi.
 * </p>
 *
 * @author
 * @author
 *
 * @version 3-mar-2003
 */
public class TitoloUniformeMusicaCrea extends TitoloCrea {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8189163440276943300L;
	private TitoloUniformeMusicaType titUni = null;

    /** Costruttore */
    public TitoloUniformeMusicaCrea(CreaType crea) {
        super(crea);
        DatiElementoType datiEl = crea.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
        if (datiEl instanceof TitoloUniformeMusicaType) {
            //Non dovrebbe mai essere null
            titUni = (TitoloUniformeMusicaType) datiEl;
        }
    }

    public TitoloUniformeMusicaCrea(ModificaType crea) {
        super(crea);
        DatiElementoType datiEl = crea.getElementoAut().getDatiElementoAut();
        if (datiEl instanceof TitoloUniformeMusicaType) {
            //Non dovrebbe mai essere null
            titUni = (TitoloUniformeMusicaType) datiEl;
        }
    }

    public Tb_titolo creaDocumento(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        titolo.setCD_NATURA("A");
        titolo.setTP_MATERIALE("U");

        //      Viene fatta nella validazione, utilizzata per la ricerca di simili
        //        isbd.definisciISBDtitUniMusicale(titolo,titUni);
        if (titUni.getT300() != null)
            titolo.setNOTA_INF_TIT(titUni.getT300().getA_300());
        if (titUni.getT015() != null)
            titolo.setISADN(titUni.getT015().getA_015());
        else if (titUni.getLivelloAut().getType() == SbnLivello.valueOf("97").getType()) {
            Progressivi progr = new Progressivi();
            titolo.setISADN(progr.getNextIsadnTitolo());
        }

        //Setto le chiavi del documento cles e clet
        if (titUni.getT101() != null) {
            String[] lingue = titUni.getT101().getA_101();
            if (lingue.length >= 1)
                titolo.setCD_LINGUA_1(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[0].toUpperCase()));
            if (lingue.length >= 2)
                titolo.setCD_LINGUA_2(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[1].toUpperCase()));
            if (lingue.length >= 3)
                titolo.setCD_LINGUA_3(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[2].toUpperCase()));
        }
        if (titUni.getT801() != null && titUni.getT801().getB_801() != null)
            titolo.setCD_AGENZIA(titUni.getT801().getB_801());
        else
            titolo.setCD_AGENZIA("ITICCU");
        if (titUni.getT152() != null)
            titolo.setCD_NORME_CAT(titUni.getT152().getA_152());
        else
            titolo.setCD_NORME_CAT("RICA");
        A830 a830 = titUni.getT830();
        if (a830 != null) {
            titolo.setNOTA_CAT_TIT(a830.getA_830());
        }

        //A230 a230 = titUni.getT230();
        //settaChiavi(titolo, a230.getA_230());

        return titolo;
    }

    public void creaComposizione(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        A928 a928 = titUni.getT928();
        A929 a929 = titUni.getT929();
        if (a928 != null) {
            Tb_musica musica = new Tb_musica();
            musica.setBID(titolo.getBID());
            musica.setDS_ORG_SINT(a928.getB_928());
            musica.setDS_ORG_ANAL(a928.getC_928());
            musica.setUTE_INS(titolo.getUTE_INS());
            musica.setUTE_VAR(titolo.getUTE_VAR());
            musica.setCD_LIVELLO(titolo.getCD_LIVELLO());
            musica.setFL_COMPOSITO(" ");
            musica.setFL_PALINSESTO(" ");
            Musica musicaDB = new Musica();
            musicaDB.inserisci(musica);
            if (a929 != null) {

                Composizione compDB = new Composizione();
                compDB.inserisci(preparaComposizione(titolo, a928, a929));
            }
        }
    }

    protected Tb_composizione preparaComposizione(Tb_titolo titolo, A928 a928, A929 a929)
        throws EccezioneSbnDiagnostico {
        Tb_composizione comp = new Tb_composizione();
        comp.setBID(titolo.getBID());
        comp.setUTE_INS(titolo.getUTE_INS());
        comp.setUTE_VAR(titolo.getUTE_VAR());
        if (a928.getA_928Count() > 0)
            comp.setCD_FORMA_1(
                Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_1", a928.getA_928(0)));
        if (a928.getA_928Count() > 1)
            comp.setCD_FORMA_2(
                Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_2", a928.getA_928(1)));
        if (a928.getA_928Count() > 2)
            comp.setCD_FORMA_3(
                Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_3", a928.getA_928(2)));
        comp.setNUMERO_ORDINE(a929.getA_929());
        comp.setNUMERO_OPERA(a929.getB_929());
        comp.setNUMERO_CAT_TEM(a929.getC_929());
        comp.setDATAZIONE(a929.getD_929());
        comp.setCD_TONALITA(Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", a929.getE_929()));
        comp.setDS_SEZIONI(a929.getF_929());

        if (a929.getI_929() != null) {
            comp.setKY_APP_DEN(a929.getI_929()); //Denormalizzata (in teoria senza pretitolo)
            comp.setKY_APP_NOR_PRE(NormalizzaNomi.normalizzazioneGenerica(a929.getI_929()));
            //comp.setKy_app_clet(a929.get());
            //comp.setKy_app_pre(a929.getI_929()); //pretitolo
            if (comp.getKY_APP_NOR_PRE().length()>10)
                comp.setKY_APP_RIC(comp.getKY_APP_NOR_PRE().substring(0,10));
            else
                comp.setKY_APP_RIC(comp.getKY_APP_NOR_PRE());
        }
        if (a929.getH_929() != null) {
            //comp.setKy_est_clet(a929.geth());
            //comp.setKy_est_pre(a929.geth());
            comp.setKY_EST_DEN(a929.getH_929());
            comp.setKY_EST_NOR_PRE(NormalizzaNomi.normalizzazioneGenerica(a929.getH_929()));
            if (comp.getKY_EST_NOR_PRE().length()>10)
                comp.setKY_EST_RIC(comp.getKY_EST_NOR_PRE().substring(0,10));
            else
                comp.setKY_EST_RIC(comp.getKY_EST_NOR_PRE());
        }
        if (a929.getG_929() != null) {
            comp.setKY_ORD_DEN(a929.getG_929());
            comp.setKY_ORD_NOR_PRE(NormalizzaNomi.normalizzazioneGenerica(a929.getG_929()));
            //comp.setKy_ord_clet(a929.getG_929());
            //comp.setKy_ord_pre(a929.getG_929());
            if (comp.getKY_ORD_NOR_PRE().length()>10)
                comp.setKY_ORD_RIC(comp.getKY_ORD_NOR_PRE().substring(0,10));
            else
                comp.setKY_ORD_RIC(comp.getKY_ORD_NOR_PRE());
        }
        return comp;
    }

    public void modificaComposizione(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        A928 a928 = titUni.getT928();
        A929 a929 = titUni.getT929();
        Musica musicaDB = new Musica();

        Tb_musica musica = musicaDB.cercaPerId(titolo.getBID());
        boolean esistente = true;
        if (musica == null) {
            esistente = false;
            musica = new Tb_musica();
            musica.setUTE_INS(titolo.getUTE_VAR());
        }
        musica.setDS_ORG_SINT(a928.getB_928());
        musica.setDS_ORG_ANAL(a928.getC_928());
        musica.setUTE_VAR(titolo.getUTE_VAR());

        // Inizio modifica almaviva2 BUG MANTIS 4472 (esercizio)
        // viene gestito il caso di assenza del record di tb_musica nel caso di variazione di Titolo Uniforme
        // da TU semplice a UM (uniforme Musicale) modifica a specchio con protocollo di Indice (Vedi commento: almaviva4 13/06/2011)
//        if (esistente)
//            musicaDB.eseguiUpdate(musica);
//        else musicaDB.eseguiUpdate(musica);

        if (esistente) {
            musicaDB.eseguiUpdate(musica);
        } else {
        	musica.setBID(titolo.getBID());
            musica.setDS_ORG_SINT(a928.getB_928());
            musica.setDS_ORG_ANAL(a928.getC_928());
            musica.setUTE_INS(titolo.getUTE_INS());
            musica.setUTE_VAR(titolo.getUTE_VAR());
            musica.setCD_LIVELLO(titolo.getCD_LIVELLO());
            musica.setFL_COMPOSITO(" ");
            musica.setFL_PALINSESTO(" ");
            musicaDB.inserisci(musica);
        }

        // Fine modifica almaviva2 BUG MANTIS 4472 (esercizio)


        Composizione compDB = new Composizione();
        Tb_composizione comp1 = compDB.cercaPerId(titolo.getBID());
        Tb_composizione comp2 = preparaComposizione(titolo, a928, a929);
        if (comp1 == null) {
            compDB.inserisci(comp2);
        } else {
            comp2.setTS_VAR(comp1.getTS_VAR());
             compDB.eseguiUpdate(comp2);
        }
    }
}
