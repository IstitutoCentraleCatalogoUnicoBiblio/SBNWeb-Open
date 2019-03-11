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
import it.finsiel.sbn.polo.oggetti.Cartografia;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C121;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C124;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
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
 * Classe FormatoDocumentoCartografico
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
public class FormatoDocumentoCartografico extends FormatoDocumento {

    public FormatoDocumentoCartografico() {
    }

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        CartograficoType datiDoc = new CartograficoType();
        formattaDocumentoBase(datiDoc, titolo);
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            datiDoc.setT210(isbd.getC210(160)); //80 caratteri
            datiDoc.setT200(isbd.getC200(160));
        } else {
            datiDoc.setT210(isbd.getC210(80)); //80 caratteri
            datiDoc.setT200(isbd.getC200(80));
        }
        Cartografia cartografia = new Cartografia();
        String bid = titolo.getBID();
		Tb_cartografia tb_cart = cartografia.cercaPerId(bid);
        // Intervento interno Febbraio 2015 (invio diagnostico nel caso di assenza specializzazione sulla tb_musica
        if (tb_cart == null) {
            log.error("Legame con cartografia non trovato");
            throw new EccezioneDB(3029, "Legame cartografia non trovato per id " + bid);
        }
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_cart.getCD_LIVELLO())));
        C100 data = new C100();
        if (titolo.getAA_PUBB_1() != null)        {
            AnnoDate date = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(date.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }
        datiDoc.setT100(data);
        datiDoc.setT205(isbd.getC205());
        datiDoc.setT206(isbd.getC206());
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        CartograficoType datiDoc = new CartograficoType();
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
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(
                Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()).trim());
        if (titolo.getAA_PUBB_1() != null)        {
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(annoDate.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }


        if (titolo.getAA_PUBB_2() != null)        {
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
        Cartografia cartografia = new Cartografia();
        Tb_cartografia tb_cart = cartografia.cercaPerId(titolo.getBID());
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_cart.getCD_LIVELLO())));
        if (tb_cart.getTP_PUBB_GOV() != null)
            data.setA_100_20(
                Decodificatore.getCd_unimarc("Tb_cartografia", "tp_pubb_gov", tb_cart.getTP_PUBB_GOV()));



        // Modifica almaviva2 BUG MANTIS 4202 09.02.2011
        // Durante i test verificat che è sufficiente che uno dei due campi sia valorizzato per
        // crea l'area C120 che contiene due informazioni e inseriti if di controllo su null
        if (tb_cart.getCD_COLORE() != null || tb_cart.getCD_MERIDIANO() != null) {
//        if (tb_cart.getCD_COLORE() != null && tb_cart.getCD_MERIDIANO() != null) {
            C120 c120 = new C120();

            if (tb_cart.getCD_COLORE() != null) {
                c120.setA_120_0(Decodificatore.getCd_unimarc("Tb_cartografia", "cd_colore", tb_cart.getCD_COLORE()));
            } else {
            	c120.setA_120_0("");
            }

            // almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
            if (tb_cart.getTP_PROIEZIONE() != null) {
            	c120.setA_120_7(Decodificatore.getCd_unimarc("Tb_cartografia", "tp_proiezione", tb_cart.getTP_PROIEZIONE()));
            }

            if (tb_cart.getCD_MERIDIANO() != null) {
                c120.setA_120_9(Decodificatore.getCd_unimarc("Tb_cartografia", "cd_meridiano", tb_cart.getCD_MERIDIANO()));
            } else {
            	c120.setA_120_9("");
            }
            datiDoc.setT120(c120);
        }
        if (tb_cart.getCD_SUPPORTO_FISICO() != null
            || tb_cart.getCD_TECNICA() != null
            || tb_cart.getCD_FORMA_RIPR() != null
            || tb_cart.getCD_FORMA_PUBB() != null
            || tb_cart.getCD_ALTITUDINE() != null) {
            C121 c121 = new C121();
            if (tb_cart.getCD_SUPPORTO_FISICO() != null)
                c121.setA_121_3(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_supporto_fisico",
                        tb_cart.getCD_SUPPORTO_FISICO()));
            if (tb_cart.getCD_TECNICA() != null)
                c121.setA_121_5(
                    Decodificatore.getCd_unimarc("Tb_cartografia", "cd_tecnica", tb_cart.getCD_TECNICA()));
            if (tb_cart.getCD_FORMA_RIPR() != null)
                c121.setA_121_6(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_forma_ripr",
                        tb_cart.getCD_FORMA_RIPR()));
            if (tb_cart.getCD_FORMA_PUBB() != null)
                c121.setA_121_8(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_forma_pubb",
                        tb_cart.getCD_FORMA_PUBB()));
            if (tb_cart.getCD_ALTITUDINE() != null)
                c121.setB_121_0(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_altitudine",
                        tb_cart.getCD_ALTITUDINE()));
            datiDoc.setT121(c121);
        }
        if (tb_cart.getCD_TIPOSCALA() != null) {
            C123 c123 = new C123();
            c123.setId1(
                Indicatore.valueOf(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_tiposcala",
                        tb_cart.getCD_TIPOSCALA())));
            if (tb_cart.getTP_SCALA() != null)
                c123.setA_123(
                    Decodificatore.getCd_unimarc("Tb_cartografia", "tp_scala", tb_cart.getTP_SCALA()));
            c123.setB_123(tb_cart.getSCALA_ORIZ());
            c123.setC_123(tb_cart.getSCALA_VERT());
            c123.setD_123(tb_cart.getLONGITUDINE_OVEST());
            c123.setE_123(tb_cart.getLONGITUDINE_EST());
            c123.setF_123(tb_cart.getLATITUDINE_NORD());
            c123.setG_123(tb_cart.getLATITUDINE_SUD());
            datiDoc.setT123(c123);
        }
        if (tb_cart.getTP_IMMAGINE() != null
            || tb_cart.getCD_FORMA_CART() != null
            || tb_cart.getCD_PIATTAFORMA() != null
            || tb_cart.getCD_CATEG_SATELLITE() != null) {
            C124 c124 = new C124();
            if (tb_cart.getTP_IMMAGINE() != null)
                c124.setA_124(
                    Decodificatore.getCd_unimarc("Tb_cartografia", "tp_immagine", tb_cart.getTP_IMMAGINE()));
            if (tb_cart.getCD_FORMA_CART() != null)
                c124.setB_124(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_forma_cart",
                        tb_cart.getCD_FORMA_CART()));
            if (tb_cart.getCD_PIATTAFORMA() != null)
                c124.setD_124(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "tp_piattaforma",
                        tb_cart.getCD_PIATTAFORMA()));
            if (tb_cart.getCD_CATEG_SATELLITE() != null)
                c124.setE_124(
                    Decodificatore.getCd_unimarc(
                        "Tb_cartografia",
                        "cd_categ_satellite",
                        tb_cart.getCD_CATEG_SATELLITE()));
            datiDoc.setT124(c124);
        }

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
        List vettore = titoloDB.cercaLegamiDocumentoNonTitUni(titolo, tipoOrd);
        List legamiVec = new ArrayList();
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_titolo_tit_b tit_tit = (Vl_titolo_tit_b) vettore.get(i);
                if (filtraLegameDocSint(tit_tit)) {
                    legamiType.addArrivoLegame(formattaLegameDocumentoListaSintetica(tit_tit));
                }

            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        //Legami con autori
        Autore autDB = new Autore();
        vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
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
    public ArrivoLegame formattaLegameDocumentoListaSintetica(Vl_titolo_tit_b tit_arrivo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameDocType legameDoc = new LegameDocType();
        //Setto i valori del legame
        legameDoc.setIdArrivo(tit_arrivo.getBID());
        legameDoc.setSequenza(tit_arrivo.getSEQUENZA());
        legameDoc.setTipoLegame(
            SbnLegameDoc.valueOf(
                convertiTpLegame(
                    tit_arrivo.getTP_LEGAME(),
                    tit_arrivo.getCD_NATURA_BASE(),
                    tit_arrivo.getCD_NATURA_COLL())));
        if(tit_arrivo.getFL_CONDIVISO_LEGAME() != null)
        	legameDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(tit_arrivo.getFL_CONDIVISO_LEGAME()));
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            legameDoc.setNoteLegame(tit_arrivo.getNOTA_TIT_TIT());
        }
        //Setto i valori del documento legato
        legameDoc.setDocumentoLegato(formattaDocumentoLegatoListaSintetica(tit_arrivo));
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
        formattaGuida(datiDoc, titolo);
        datiDoc.setT001(titolo.getBID());
        datiDoc.setNaturaDoc(SbnNaturaDocumento.valueOf(titolo.getCD_NATURA()));
        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        Isbd isbd = creaIsbd(titolo);
        datiDoc.setT200(isbd.getC200(80));
        datiDoc.setT210(isbd.getC210(80));
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
        if (tipo.equals("461") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            legame_461 = true;
            return true;
        }
        return false;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        SbnLegameAut tipo = convertiTipoLegameAutore(relaz.getTP_NOME_AUT(), relaz.getTP_RESPONSABILITA());
        if (tipo.getType() == SbnLegameAut.valueOf("700").getType() || tipo.getType() == SbnLegameAut.valueOf("710").getType())
            return true; //Autore principale
        return false;
    }

}
