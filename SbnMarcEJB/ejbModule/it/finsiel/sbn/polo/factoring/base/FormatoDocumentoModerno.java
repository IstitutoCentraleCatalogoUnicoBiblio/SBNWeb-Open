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

import it.finsiel.sbn.polo.factoring.util.AnnoDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_per_int;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
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
 * Classe FormatoDocumentoModerno
 * <p>
 * Si occupa della formattazione di un documento di tipo moderno,
 * è la più generica e quindi può essere utilizzata anche per altri tipi.
 * </p>
 *
 * @author
 * @author
 *
 * @version 14-mag-03
 */
public class FormatoDocumentoModerno extends FormatoDocumento {

    public FormatoDocumentoModerno() {
    }

    //   public abstract DocumentoType formattaDocumentoLegato(Vl_titolo_tit_b titolo);

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        ModernoType datiDoc = new ModernoType();
        formattaDocumentoBase(datiDoc, titolo);

        C100 data = new C100();
        AnnoDate date = null;

        /**
         * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
         * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
         * o uguale al carattere punto '.';
         * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
         */
//        if (titolo.getAA_PUBB_1() != null){
//            date = new AnnoDate(titolo.getAA_PUBB_1());
//            data.setA_100_9(date.getAnnoDate());
//        }
	      if (titolo.getAA_PUBB_1() != null) {
	    	  data.setA_100_9(titolo.getAA_PUBB_1());
	      }
        // almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -


        datiDoc.setT100(data);
        Isbd isbd = creaIsbd(titolo);
        formattaT110(datiDoc, titolo);
        datiDoc.setT210(isbd.getC210());
        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            datiDoc.setT200(isbd.getC200(160)); //160 caratteri

        } else {
            datiDoc.setT200(isbd.getC200(80)); //80 caratteri
        }
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        ModernoType datiDoc = new ModernoType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        SbnData date = null;
        try {
            date = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(date.getXmlDate()));
        } catch (ParseException e) {
            log.info(
                "Errore parsing data ts_ins :"
                    + date.getXmlDate()
                    + " relativa al titolo :"
                    + titolo.getBID());
        }
        //Non funziona:
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()));
        //data.setA_100_8("a");
        //titolo.getTP_AA_PUBB().toLowerCase()));

        /**
         * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
         * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
         * o uguale al carattere punto '.';
         * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
         */
//        if (titolo.getAA_PUBB_1() != null){
//            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
//            data.setA_100_9(annoDate.getAnnoDate());
//        }
//        if (titolo.getAA_PUBB_2() != null){
//            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_2());
//            data.setA_100_13(annoDate.getAnnoDate());
//        }
        if (titolo.getAA_PUBB_1() != null)
            data.setA_100_9(titolo.getAA_PUBB_1());
        if (titolo.getAA_PUBB_2() != null)
            data.setA_100_13(titolo.getAA_PUBB_2());


        // almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -


        datiDoc.setT100(data);
        formattaT101(datiDoc, titolo);
        if (titolo.getCD_PAESE() != null && !titolo.getCD_PAESE().trim().equals("")) {
            C102 t102 = new C102();
            String paese=Decodificatore.getCd_unimarc("PAES",titolo.getCD_PAESE());
            t102.setA_102(paese==null?titolo.getCD_PAESE():paese);
            datiDoc.setT102(t102);
        }
        if (titolo.getCD_GENERE_1()!= null && titolo.getCD_GENERE_1().trim().length()>0) {
            C105 c105 = new C105();
            String temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_1", titolo.getCD_GENERE_1());
            //almaviva5_20150318 il codice genere deve essere lungo max 1 carattere.
            c105.addA_105_4(temp==null?titolo.getCD_GENERE_1().trim():temp);
            if (titolo.getCD_GENERE_2()!= null && titolo.getCD_GENERE_2().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_2", titolo.getCD_GENERE_2());
                c105.addA_105_4(temp==null?titolo.getCD_GENERE_2().trim():temp);
            }
            if (titolo.getCD_GENERE_3()!= null && titolo.getCD_GENERE_3().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_3", titolo.getCD_GENERE_3());
                c105.addA_105_4(temp==null?titolo.getCD_GENERE_3().trim():temp);
            }
            if (titolo.getCD_GENERE_4()!= null && titolo.getCD_GENERE_4().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_4", titolo.getCD_GENERE_4());
                c105.addA_105_4(temp==null?titolo.getCD_GENERE_4().trim():temp);
            }
            datiDoc.setT105(c105);
        }
        formattaT110(datiDoc, titolo);
        Isbd isbd = creaIsbd(titolo,datiDoc);
        C3XX[] c3 = isbd.getC3xx();
        for (int i = 0; i < c3.length; i++) {
            datiDoc.addT3XX(c3[i]);
        }

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
        formattaDatiComuni1(datiDoc, titolo);


        if (titolo.getCD_AGENZIA() != null && titolo.getCD_AGENZIA().length() >= 2) {
            C801 c801 = new C801();
            c801.setA_801(titolo.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(titolo.getCD_AGENZIA().substring(2));
            datiDoc.setT801(c801);
        }

        // Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
        // viene esteso anche al Materiale Moderno e Antico
        Rappresentazione rappDB = new Rappresentazione();
        Tb_rappresent rapp = rappDB.cercaPerId(titolo.getBID());
        if (rapp != null) {
            C922 c922 = new C922();
            if (rapp.getTP_GENERE() != null)
                c922.setA_922(
                    Decodificatore.getCd_unimarc("Tb_rappresent", "tp_genere", rapp.getTP_GENERE()));
            if (rapp.getAA_RAPP() != null)
                c922.setP_922(rapp.getAA_RAPP());
			if (rapp.getDS_PERIODO() != null)
            	c922.setQ_922(rapp.getDS_PERIODO().trim());
			if (rapp.getDS_TEATRO() != null)
            	c922.setR_922(rapp.getDS_TEATRO().trim());
			if (rapp.getDS_LUOGO_RAPP() != null)
	            c922.setS_922(rapp.getDS_LUOGO_RAPP().trim());
            c922.setU_922(rapp.getDS_OCCASIONE());
            c922.setT_922(rapp.getNOTA_RAPP());
            datiDoc.setT922(c922);
        }

        Personaggio persDB = new Personaggio();
        Tb_personaggio pers;
        List v = persDB.cercaPerBid(titolo.getBID());
        for (int i = 0; i < v.size(); i++) {
            pers = (Tb_personaggio) v.get(i);
            C927 c927 = new C927();
			if (pers.getNOME_PERSONAGGIO() != null)
	            c927.setA_927(pers.getNOME_PERSONAGGIO().trim());
            if (pers.getCD_TIMBRO_VOCALE() != null)
                c927.setB_927(
                    Decodificatore.getCd_unimarc(
                        "Tb_personaggio",
                        "cd_timbro_vocale",
                        pers.getCD_TIMBRO_VOCALE()));
            Tr_per_int tr_per_int = persDB.cercaInterprete((int) pers.getID_PERSONAGGIO());
            if (tr_per_int != null)
                c927.setC3_927(tr_per_int.getVID());
            datiDoc.addT927(c927);
        }



        return datiDoc;
    }

    /** formatta i legami di un documento per lista sintetica, esegue una ricerca nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public LegamiType[] formattaLegamiPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami;
        legame_461 = false;
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
     * Utilizza una vista,
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameDocumentoListaSintetica(Vl_titolo_tit_b titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameDocType legameDoc = new LegameDocType();
        //Setto i valori del legame
        legameDoc.setIdArrivo(titolo.getBID());

        //Campo obbligatorio
        legameDoc.setTipoLegame(
            SbnLegameDoc.valueOf(
                convertiTpLegame(
                    titolo.getTP_LEGAME(),
                    titolo.getCD_NATURA_BASE(),
                    titolo.getCD_NATURA_COLL())));
        if(titolo.getFL_CONDIVISO_LEGAME() != null)
        	legameDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO_LEGAME()));
        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            legameDoc.setSequenza(titolo.getSEQUENZA());
        }

        //Setto i valori del documento legato: no viene settato da fuori
        legameDoc.setDocumentoLegato(formattaDocumentoLegatoListaSintetica(titolo));
        arrLegame.setLegameDoc(legameDoc);
        return arrLegame;
    }

    /**formatta il documento legato per la lista sintetica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public DocumentoType formattaDocumentoLegatoListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DatiDocType datiDoc = new DatiDocType();
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        datiDoc.setTipoMateriale(SbnMateriale.valueOf(titolo.getTP_MATERIALE()));
        //Decodificatore.get...
        formattaGuida(datiDoc, titolo);
        datiDoc.setT001(titolo.getBID());
        datiDoc.setT200(isbd.getC200(80)); // a 80 caratteri
        //datiDoc.setT210(isbd.getC210());
        datiDoc.setNaturaDoc(SbnNaturaDocumento.valueOf(titolo.getCD_NATURA()));

        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));

        DocumentoTypeChoice choice = new DocumentoTypeChoice();
        choice.setDatiDocumento(datiDoc);
        doc.setDocumentoTypeChoice(choice);
        return doc;
    }


    /** Filtra legami tra documenti */
    public boolean filtraLegameDocSint(Vl_titolo_tit_b relaz) {
        String tipo =
            convertiTpLegame(relaz.getTP_LEGAME(), relaz.getCD_NATURA_BASE(), relaz.getCD_NATURA_COLL());
        if (tipo == null)
            return false;
        if (tipo.equals("410") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() && !legame_461) {
            return true;
        }
        if (tipo.equals("461") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            legame_461 = true;
            return true;
        }

    	// Inizio Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
    	// fra periodici sia verso l'alto che verso il basso;
        if (relaz.getCD_NATURA_BASE().toString().equals("S")
        		&& relaz.getCD_NATURA_COLL().toString().equals("S")) {
        	return true;
        }
       	// Fine Modifica almaviva2 04.08.2010


        return false;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        SbnLegameAut tipo = convertiTipoLegameAutore(relaz.getTP_NOME_AUT(), relaz.getTP_RESPONSABILITA());
        if (relaz.getTP_RESPONSABILITA().equals("1"))
            contieneAutore1 = true;
        if (tipo.getType() == SbnLegameAut.valueOf("700").getType() || tipo.getType() == SbnLegameAut.valueOf("710").getType()) {
            return true; //Autore principale
        }
        if (tipo.getType() == SbnLegameAut.valueOf("702").getType() || tipo.getType() == SbnLegameAut.valueOf("711").getType() || tipo.getType() == SbnLegameAut.valueOf("712").getType())
            if (relaz.getTP_RESPONSABILITA().equals("3") && !contieneAutore1)
                return true; //Altri autori
        return false;
    }

}
