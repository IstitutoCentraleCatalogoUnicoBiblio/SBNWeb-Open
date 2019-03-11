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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Elettronico;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_risorsa_elettr;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C135;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
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
 * Classe FormatoDocumentoMusica
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
public class FormatoDocumentoElettronico extends FormatoDocumento {

    public FormatoDocumentoElettronico() {
    }

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
    	ElettronicoType datiDoc = new ElettronicoType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        SbnData date = null;

        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
		if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) {
            try {
            	date = new SbnData(titolo.getTS_INS());
                data.setA_100_0(Date.parseDate(date.getXmlDate()));
            } catch (ParseException e) {
                log.info(
                    "Errore parsing data ts_ins :"
                        + titolo.getTS_INS()
                        + " relativa al titolo :"
                        + titolo.getBID());
            }
            datiDoc.setT210(isbd.getC210()); // 80 char
            datiDoc.setT215(isbd.getC215());
        }
        if (titolo.getAA_PUBB_1() != null)
            data.setA_100_9(titolo.getAA_PUBB_1());
        datiDoc.setT100(data);
        Elettronico elettronico = new Elettronico();
        Tb_risorsa_elettr tb_risorsa_elettr = elettronico.cercaPerId(titolo.getBID());
        if (tb_risorsa_elettr != null) {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_risorsa_elettr.getCD_LIVELLO())));
          C135 c135 = new C135();
          if (tb_risorsa_elettr.getTP_RISORSA() != null)
              c135.setA_135_0(Decodificatore.getCd_unimarc("RIEL", tb_risorsa_elettr.getTP_RISORSA()));
          if (tb_risorsa_elettr.getCD_DESIGNAZIONE() != null)
          	  c135.setA_135_1(Decodificatore.getCd_unimarc("DESI", tb_risorsa_elettr.getCD_DESIGNAZIONE()));
          if (tb_risorsa_elettr.getCD_COLORE() != null)
          	c135.setA_135_2(Decodificatore.getCd_unimarc("CDCO", tb_risorsa_elettr.getCD_COLORE()));
          if (tb_risorsa_elettr.getCD_DIMENSIONE() != null)
          	c135.setA_135_3(Decodificatore.getCd_unimarc("CDDI", tb_risorsa_elettr.getCD_DIMENSIONE()));
          if (tb_risorsa_elettr.getCD_SUONO() != null)
          	c135.setA_135_4(Decodificatore.getCd_unimarc("SUON", tb_risorsa_elettr.getCD_SUONO()));
          datiDoc.setT135(c135);
        } else {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        }

        datiDoc.setT200(isbd.getC200());
        datiDoc.setT205(isbd.getC205());
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
    	ElettronicoType datiDoc = new ElettronicoType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        SbnData date = null;
        String bid = titolo.getBID();
		try {
        	date = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(date.getXmlDate()));
        } catch (ParseException e) {
            log.info(
                "Errore parsing data ts_ins :"
                    + titolo.getTS_INS()
                    + " relativa al titolo :"
                    + bid);
        }
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()));
        if (titolo.getAA_PUBB_1() != null)
            data.setA_100_9(titolo.getAA_PUBB_1());
        if (titolo.getAA_PUBB_2() != null)
            data.setA_100_13(titolo.getAA_PUBB_2());
        datiDoc.setT100(data);
        formattaT101(datiDoc, titolo);
        if (titolo.getCD_PAESE() != null && !titolo.getCD_PAESE().trim().equals("")) {
            C102 t102 = new C102();
            String paese=Decodificatore.getCd_unimarc("PAES",titolo.getCD_PAESE());
            t102.setA_102(paese==null?titolo.getCD_PAESE():paese);
            datiDoc.setT102(t102);
        }

        if (titolo.getTP_RECORD_UNI().equals("l")) {
            Elettronico elettronico = new Elettronico();
            Tb_risorsa_elettr tb_risorsa_elettr = elettronico.cercaPerId(bid);
    		if (tb_risorsa_elettr == null) {
    			log.error("Legame con elettronico non trovato");
    			throw new EccezioneDB(3029, "Legame con elettronico non trovato per id " + bid);
    		}
            datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_risorsa_elettr.getCD_LIVELLO())));
    		C135 c135 = new C135();
            if (tb_risorsa_elettr.getTP_RISORSA() != null) {
                c135.setA_135_0(Decodificatore.getCd_unimarc("RIEL", tb_risorsa_elettr.getTP_RISORSA()));
            } else {
            	throw new EccezioneDB(3360, "Tipo di video obbligatorio");
            }
            if (tb_risorsa_elettr.getTP_RISORSA() != null)
                c135.setA_135_0(Decodificatore.getCd_unimarc("RIEL", tb_risorsa_elettr.getTP_RISORSA()));
            if (tb_risorsa_elettr.getCD_DESIGNAZIONE() != null)
            	  c135.setA_135_1(Decodificatore.getCd_unimarc("DESI", tb_risorsa_elettr.getCD_DESIGNAZIONE()));
            if (tb_risorsa_elettr.getCD_COLORE() != null)
            	c135.setA_135_2(Decodificatore.getCd_unimarc("CDCO", tb_risorsa_elettr.getCD_COLORE()));
            if (tb_risorsa_elettr.getCD_DIMENSIONE() != null)
            	c135.setA_135_3(Decodificatore.getCd_unimarc("CDDI", tb_risorsa_elettr.getCD_DIMENSIONE()));
            if (tb_risorsa_elettr.getCD_SUONO() != null)
            	c135.setA_135_4(Decodificatore.getCd_unimarc("SUON", tb_risorsa_elettr.getCD_SUONO()));
            datiDoc.setT135(c135);

            formattaDatiComuni1(datiDoc, titolo);

        } //end if tipo record 'l'


        // almaviva2 - RISOLUZIONE PROBLEMI MATERIALE ELETTRONICO - DICEMBRE 2017
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));



        Isbd isbd = creaIsbd(titolo,datiDoc);
        C3XX[] c3 = isbd.getC3xx();
        for (int i = 0; i < c3.length; i++) {
            datiDoc.addT3XX(c3[i]);
        }
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

		if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) {
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
		if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) {
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
        if (tipo.equals("410") && tipoOut.equals(SbnTipoOutput.VALUE_1) && legame_461) {
            return true;
        }
        if (tipo.equals("461")) {
            legame_461 = true;
            return true;
        }
        if (tipo.equals("500") && tipoOut.equals(SbnTipoOutput.VALUE_1))
            return true;
        return false;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        SbnLegameAut tipo = convertiTipoLegameAutore(relaz.getTP_NOME_AUT(), relaz.getTP_RESPONSABILITA());
        if (tipo.equals(SbnLegameAut.valueOf("700")) || tipo.equals(SbnLegameAut.valueOf("710")))
            return true; //Autore principale
        return false;
    }

}
