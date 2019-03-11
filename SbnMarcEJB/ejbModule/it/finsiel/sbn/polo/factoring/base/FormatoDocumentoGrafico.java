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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.AnnoDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.types.Date;

/**
 * Classe FormatoDocumentoGrafica
 * <p>
 * Si occupa della formattazione di un documento di tipo moderno, che è analoga a quella
 * di un titolo, poichè è la più generica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-dic-02
 */
public class FormatoDocumentoGrafico extends FormatoDocumento {

    public FormatoDocumentoGrafico() {
    }

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        GraficoType datiDoc = new GraficoType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        SbnData date = null;
        String bid = titolo.getBID();
		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            try {
                date = new SbnData(titolo.getTS_INS());
                data.setA_100_0(Date.parseDate(date.getXmlDate()));
                //data.setA_100_0(Date.parseDate(titolo.getBID().getXmlDate()));
            } catch (ParseException e) {
                date = new SbnData(titolo.getTS_INS());
                log.info(
                    "Errore parsing data ts_ins :"
                        + date.getXmlDate()
                        + " relativa al titolo :"
                        + bid);
            }
            datiDoc.setT210(isbd.getC210()); // 80 char
            datiDoc.setT215(isbd.getC215());
        }
        if (titolo.getAA_PUBB_1() != null)        {
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(annoDate.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }


        datiDoc.setT100(data);
        Grafica grafica = new Grafica();
        Tb_grafica tb_grafica = grafica.cercaPerId(bid);
        // Intervento interno Febbraio 2015 (invio diagnostico nel caso di assenza specializzazione sulla tb_grafica
        if (tb_grafica == null) {
            log.error("Legame con grafica non trovato");
            throw new EccezioneDB(3029, "Legame con grafica non trovato per id " + bid);
        }
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_grafica.getCD_LIVELLO())));
        if (tb_grafica.getTP_MATERIALE_GRA() != null) {
            C116 c116 = new C116();
            c116.setA_116_0(Decodificatore.getCd_unimarc("Tb_grafica", "tp_materiale_gra", tb_grafica.getTP_MATERIALE_GRA()));
            datiDoc.setT116(c116);
        }
        datiDoc.setT200(isbd.getC200());
        datiDoc.setT205(isbd.getC205());
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        GraficoType datiDoc = new GraficoType();
        formattaDocumentoBase(datiDoc, titolo);

        // Inizio Segnalazione Carla del 10/03/2015:
        // inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
        Impronta impr = new Impronta();
        List v = impr.cercaPerBid(titolo.getBID());
        if (v.size() > 0) {
            C012[] c012 = new C012[v.size()];
            for (int i = 0; i < c012.length; i++) {
                Tb_impronta tbi = (Tb_impronta) v.get(i);
                c012[i] = new C012();
                c012[i].setA_012_1(tbi.getIMPRONTA_1());
                c012[i].setA_012_2(tbi.getIMPRONTA_2());
                c012[i].setA_012_3(tbi.getIMPRONTA_3());
                c012[i].setNota(tbi.getNOTA_IMPRONTA());
            }
            datiDoc.setT012(c012);
        }
     // Fine Segnalazione Carla del 10/03/2015:

        C100 data = new C100();
        SbnData date = null;
        try {
            date = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(date.getXmlDate()));
            //data.setA_100_0(Date.parseDate(titolo.getBID().getXmlDate()));
        } catch (ParseException e) {
            date = new SbnData(titolo.getTS_INS());
            log.info(
                "Errore parsing data ts_ins :"
                    + date.getXmlDate()
                    + " relativa al titolo :"
                    + titolo.getBID());
        }
        if (titolo.getTP_AA_PUBB() != null)        {
// Correzione BUG 3249 almaviva2 20Ottobre2009 il Decodificatore è applicato su Tb_titolo e non su  Tb_titoloCommonDao
//            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titoloCommonDao", "tp_aa_pubb", titolo.getTP_AA_PUBB().toString()));
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB().toString()));
        }
        if (titolo.getAA_PUBB_1() != null)        {
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(annoDate.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }
        if (titolo.getAA_PUBB_2() != null){
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_2());
            data.setA_100_13(annoDate.getAnnoDate());
            //data.setA_100_13(titolo.getAA_PUBB_2().getAnnoDate());
        }

        datiDoc.setT100(data);
        formattaT101(datiDoc, titolo);
        if (titolo.getCD_PAESE() != null && !titolo.getCD_PAESE().trim().equals("")) {
            C102 t102 = new C102();
            String paese=Decodificatore.getCd_unimarc("PAES",titolo.getCD_PAESE());
            t102.setA_102(paese==null?titolo.getCD_PAESE():paese);
            datiDoc.setT102(t102);
        }
        Grafica grafica = new Grafica();
        Tb_grafica tb_grafica = grafica.cercaPerId(titolo.getBID());
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_grafica.getCD_LIVELLO())));
        C116 c116 = new C116();
        if (tb_grafica.getTP_MATERIALE_GRA() != null)
            c116.setA_116_0(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "tp_materiale_gra",
                    tb_grafica.getTP_MATERIALE_GRA()));
        if (tb_grafica.getCD_SUPPORTO() != null)
            c116.setA_116_1(
                Decodificatore.getCd_unimarc("Tb_grafica", "cd_supporto", tb_grafica.getCD_SUPPORTO()));
        if (tb_grafica.getCD_COLORE() != null)
            c116.setA_116_3(
                Decodificatore.getCd_unimarc("Tb_grafica", "cd_colore", tb_grafica.getCD_COLORE()));
        if (tb_grafica.getCD_TECNICA_DIS_1() != null)
            c116.addA_116_4(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_dis_1",
                    tb_grafica.getCD_TECNICA_DIS_1()));
        if (tb_grafica.getCD_TECNICA_DIS_2() != null)
            c116.addA_116_4(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_dis_2",
                    tb_grafica.getCD_TECNICA_DIS_2()));
        if (tb_grafica.getCD_TECNICA_DIS_3() != null)
            c116.addA_116_4(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_dis_3",
                    tb_grafica.getCD_TECNICA_DIS_3()));
        if (tb_grafica.getCD_TECNICA_STA_1() != null)
            c116.addA_116_10(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_sta_1",
                    tb_grafica.getCD_TECNICA_STA_1()));
        if (tb_grafica.getCD_TECNICA_STA_2() != null)
            c116.addA_116_10(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_sta_2",
                    tb_grafica.getCD_TECNICA_STA_2()));
        if (tb_grafica.getCD_TECNICA_STA_3() != null)
            c116.addA_116_10(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_tecnica_sta_3",
                    tb_grafica.getCD_TECNICA_STA_3()));
        if (tb_grafica.getCD_DESIGN_FUNZ() != null)
            c116.setA_116_16(
                Decodificatore.getCd_unimarc(
                    "Tb_grafica",
                    "cd_design_funz",
                    tb_grafica.getCD_DESIGN_FUNZ()));
        datiDoc.setT116(c116);
        Isbd isbd = creaIsbd(titolo,datiDoc);
        C3XX[] c3 = isbd.getC3xx();
        for (int i = 0; i < c3.length; i++) {
            datiDoc.addT3XX(c3[i]);
        }
     // almaviva2 Evolutiva Dicembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
        formattaDatiComuni1(datiDoc, titolo);

        if (titolo.getCD_AGENZIA() != null && titolo.getCD_AGENZIA().length() >= 2) {
            C801 c801 = new C801();
            c801.setA_801(titolo.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(titolo.getCD_AGENZIA().substring(2));
            datiDoc.setT801(c801);
        }
        return datiDoc;
    }

    /** formatta i legami di un documento per lista sintetica, esegue una ricerca nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public LegamiType[] formattaLegamiPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami;
        Titolo titoloDB = new Titolo();
        //Legami di documenti
        List documenti = titoloDB.cercaLegamiDocumentoNonTitUni(titolo, tipoOrd);
        List legamiVec = new ArrayList();
        if (documenti.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());

            for (int i = 0; i < documenti.size(); i++) {
                Vl_titolo_tit_b tit_tit = (Vl_titolo_tit_b) documenti.get(i);
                if (filtraLegameDocSint(tit_tit)) {
                    legamiType.addArrivoLegame(formattaLegameDocumentoListaSintetica(tit_tit));
                }

            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        //Legami con autori
        Autore autDB = new Autore();
        List autori = autDB.cercaAutorePerTitolo(titolo.getBID());
        if (autori.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < autori.size(); i++) {
                Vl_autore_tit aut_tit = (Vl_autore_tit) autori.get(i);
                if (filtraLegameAutoreSint(aut_tit)) {
                    legamiType.addArrivoLegame(formattaLegameAutoreListaSintetica(titolo, aut_tit));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        legami = new LegamiType[legamiVec.size()];
        for (int i = 0; i < legami.length; i++) {
            legami[i] = (LegamiType) legamiVec.get(i);
        }
        return legami;
    }

    /** Prepara un legame tra due titoli
     * Per ora utilizzo due titoli e un legame, forse servirà una vista e un titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameDocumentoListaSintetica(Vl_titolo_tit_b titolo) throws IllegalArgumentException, InvocationTargetException, Exception {

        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameDocType legameDoc = new LegameDocType();
        //Setto i valori del legame
        legameDoc.setIdArrivo(titolo.getBID());
        legameDoc.setSequenza(titolo.getSEQUENZA());
        legameDoc.setTipoLegame(
            SbnLegameDoc.valueOf(
                convertiTpLegame(
                    titolo.getTP_LEGAME(),
                    titolo.getCD_NATURA_BASE(),
                    titolo.getCD_NATURA_COLL())));
        if(titolo.getFL_CONDIVISO_LEGAME() != null)
        	legameDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO_LEGAME()));

        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            legameDoc.setNoteLegame(titolo.getNOTA_TIT_TIT());
        }
        //Setto i valori del documento legato
        legameDoc.setDocumentoLegato(formattaDocumentoLegatoListaSintetica(titolo));
        arrLegame.setLegameDoc(legameDoc);
        return arrLegame;
    }

    /**formatta il documento legato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public DocumentoType formattaDocumentoLegatoListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DatiDocType datiDoc = new DatiDocType();

        datiDoc.setTipoMateriale(SbnMateriale.valueOf(titolo.getTP_MATERIALE()));
        //Decodificatore.get...
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            datiDoc.setLivelloAutDoc(
                SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        }
        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        formattaGuida(datiDoc, titolo);
        datiDoc.setT001(titolo.getBID());
        datiDoc.setNaturaDoc(SbnNaturaDocumento.valueOf(titolo.getCD_NATURA()));
        Isbd isbd = creaIsbd(titolo);
        datiDoc.setT200(isbd.getC200(80)); //tagliare a 80 caratteri
        DocumentoTypeChoice choice = new DocumentoTypeChoice();
        choice.setDatiDocumento(datiDoc);
        doc.setDocumentoTypeChoice(choice);
        return doc;
    }


    public Object formattaDocumentoLegatoPerEsame(Tb_titolo titolo)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(formattaDocumentoPerEsameAnalitico(titolo));
        TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
        doc.setDocumentoTypeChoice(docChoice);
        LegamiType[] leg = formattaLegamiPerEsameAnalitico(titolo);
        doc.setLegamiDocumento(leg);
        LegamiType lega = titoloCerca.formattaLegamiDocDoc(titolo);
        //Se ci sono legami li aggiungo? Si, non di altri documenti.
        if (lega.enumerateArrivoLegame().hasMoreElements())
            doc.addLegamiDocumento(0,lega);
        return doc;
    }

    /** Filtra legami tra documenti */
    public boolean filtraLegameDocSint(Vl_titolo_tit_b relaz) {
        String tipo =
            convertiTpLegame(relaz.getTP_LEGAME(), relaz.getCD_NATURA_BASE(), relaz.getCD_NATURA_COLL());
        if (tipo == null)
            return false;
        if (tipo.equals("410") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() && legame_461) {
            return true;
        }
        if (tipo.equals("461")) {
            legame_461 = true;
            return true;
        }
        if (tipo.equals("500") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType())
            return true;
        return false;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        SbnLegameAut tipo = convertiTipoLegameAutore(relaz.getTP_NOME_AUT(), relaz.getTP_RESPONSABILITA());
        if (tipo.getType() == SbnLegameAut.valueOf("700").getType() || tipo.getType() == SbnLegameAut.valueOf("710").getType())
            return true; //Autore principale
        return false;
    }

}
