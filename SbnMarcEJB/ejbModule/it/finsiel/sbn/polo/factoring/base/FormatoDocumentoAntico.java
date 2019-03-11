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
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_per_int;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C140;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.types.Date;

/**
 * Classe FormatoDocumentoAntico
 * <p>
 * Si occupa della formattazione di un documento di tipo antico.
 * </p>
 *
 * @author
 * @author
 *
 * @version 15-gen-03
 */
public class FormatoDocumentoAntico extends FormatoDocumento {

    /** Costruttore */
    public FormatoDocumentoAntico() {
    }

    /** formatta documento per lista sintetica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        AnticoType datiDoc = new AnticoType();
        formattaDocumentoBase(datiDoc, titolo);

        C100 data = new C100();
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()));
        if (titolo.getAA_PUBB_1() != null){
            AnnoDate date = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(date.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }
        if (titolo.getAA_PUBB_2() != null){
            AnnoDate date = new AnnoDate(titolo.getAA_PUBB_2());
            data.setA_100_13(date.getAnnoDate());
            //data.setA_100_13(titolo.getAA_PUBB_2().getAnnoDate());
        }
        datiDoc.setT100(data);
        Isbd isbd = creaIsbd(titolo);

        if (tipoOut.getType() == SbnTipoOutput.VALUE_2.getType()) {
            datiDoc.setT200(isbd.getC200(80));
            datiDoc.setT210(isbd.getC210(80));
        } else {
            datiDoc.setT200(isbd.getC200(160)); //160 caratteri
            datiDoc.setT210(isbd.getC210(160));
        }
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        AnticoType datiDoc = new AnticoType();
        formattaDocumentoBase(datiDoc, titolo);

        C100 data = new C100();
        try {
            SbnData date = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(date.getXmlDate()));
            //data.setA_100_0(Date.parseDate(titolo.getTS_INS().getXmlDate()));
        } catch (ParseException e) {
            SbnData date = new SbnData(titolo.getTS_INS());
            log.info(
                "Errore parsing data ts_ins :"
                    + date.getXmlDate()
                    + " relativa al titolo :"
                    + titolo.getBID());
        }
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()));
        if (titolo.getAA_PUBB_1() != null){
            AnnoDate date = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(date.getAnnoDate());
            //data.setA_100_9(titolo.getAA_PUBB_1().getAnnoDate());
        }
        if (titolo.getAA_PUBB_2() != null){
            AnnoDate date = new AnnoDate(titolo.getAA_PUBB_2());
            data.setA_100_13(date.getAnnoDate());
            //data.setA_100_13(titolo.getAA_PUBB_2().getAnnoDate());
        }
        datiDoc.setT100(data);
        formattaT101(datiDoc, titolo);

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

        if (titolo.getCD_PAESE() != null && !titolo.getCD_PAESE().trim().equals("")) {
            C102 t102 = new C102();
            String paese=Decodificatore.getCd_unimarc("PAES",titolo.getCD_PAESE());
            t102.setA_102(paese==null?titolo.getCD_PAESE():paese);
            datiDoc.setT102(t102);
        }
        if (titolo.getCD_GENERE_1()!= null && titolo.getCD_GENERE_1().trim().length()>0) {
            C140 c140 = new C140();
            String temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_1", titolo.getCD_GENERE_1());
          //almaviva5_20150318 il codice genere deve essere lungo max 2 caratteri per l'antico.
            c140.addA_140_9(temp==null?titolo.getCD_GENERE_1().trim():temp);
            if (titolo.getCD_GENERE_2()!= null && titolo.getCD_GENERE_2().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_2", titolo.getCD_GENERE_2());
                c140.addA_140_9(temp==null?titolo.getCD_GENERE_2().trim():temp);
            }
            if (titolo.getCD_GENERE_3()!= null && titolo.getCD_GENERE_3().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_3", titolo.getCD_GENERE_3());
                c140.addA_140_9(temp==null?titolo.getCD_GENERE_3().trim():temp);
            }
            if (titolo.getCD_GENERE_4()!= null && titolo.getCD_GENERE_4().trim().length()>0) {
                temp = Decodificatore.getCd_unimarc("Tb_titolo", "cd_genere_4", titolo.getCD_GENERE_4());
                c140.addA_140_9(temp==null?titolo.getCD_GENERE_4().trim():temp);
            }
            datiDoc.setT140(c140);
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
        v = persDB.cercaPerBid(titolo.getBID());
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
        Titolo titoloDB = new Titolo();
        //Legami di documenti
        List documenti = titoloDB.cercaLegamiDocumento(titolo, tipoOrd);
        List legamiVec = new ArrayList();
        if (documenti.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < documenti.size(); i++) {
                Vl_titolo_tit_b tit_tit = (Vl_titolo_tit_b) documenti.get(i);
                String tipo = filtraLegameDocSint(tit_tit);
                if (tipo != null) {
                    if (tipo.equals("500"))
                        legamiType.addArrivoLegame(formattaLegameTitUniListaSintetica(tit_tit));
                    else
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
    public ArrivoLegame formattaLegameDocumentoListaSintetica(Vl_titolo_tit_b tit_arrivo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameDocType legameDoc = new LegameDocType();
        //Setto i valori del legame
        legameDoc.setIdArrivo(tit_arrivo.getBID());
        if(tit_arrivo.getFL_CONDIVISO_LEGAME() != null)
        	legameDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(tit_arrivo.getFL_CONDIVISO_LEGAME()));
        legameDoc.setTipoLegame(
            SbnLegameDoc.valueOf(
                convertiTpLegame(
                    tit_arrivo.getTP_LEGAME(),
                    tit_arrivo.getCD_NATURA_BASE(),
                    tit_arrivo.getCD_NATURA_COLL())));
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            legameDoc.setSequenza(tit_arrivo.getSEQUENZA());
        }
        //Setto i valori del documento legato
        legameDoc.setDocumentoLegato(formattaDocumentoLegatoListaSintetica(tit_arrivo));
        arrLegame.setLegameDoc(legameDoc);
        return arrLegame;
    }

    /** Prepara un legame tra due titoli
     * Per ora utilizzo due titoli e un legame, forse servirà una vista e un titolo
     */
    public ArrivoLegame formattaLegameTitUniListaSintetica(Vl_titolo_tit_b tit_arrivo)
        throws EccezioneDB {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameElementoAutType legameDoc = new LegameElementoAutType();
        //Setto i valori del legame
        legameDoc.setIdArrivo(tit_arrivo.getBID());
        legameDoc.setTipoLegame(
            SbnLegameAut.valueOf(
                convertiTpLegame(
                    tit_arrivo.getTP_LEGAME(),
                    tit_arrivo.getCD_NATURA_BASE(),
                    tit_arrivo.getCD_NATURA_COLL())));
        //Setto i valori del documento legato
        legameDoc.setElementoAutLegato(formattaTitUniLegatoListaSintetica(tit_arrivo));
        legameDoc.setTipoAuthority(SbnAuthority.TU);
        arrLegame.setLegameElementoAut(legameDoc);
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
        Isbd isbd = creaIsbd(titolo);
        datiDoc.setT200(isbd.getC200(80));
        datiDoc.setT210(isbd.getC210(80));
        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        DocumentoTypeChoice choice = new DocumentoTypeChoice();
        choice.setDatiDocumento(datiDoc);
        doc.setDocumentoTypeChoice(choice);
        return doc;
    }

    /**formatta il documento legato */
    public ElementAutType formattaTitUniLegatoListaSintetica(Tb_titolo titolo) throws EccezioneDB {
        ElementAutType doc = new ElementAutType();
        TitoloUniformeType datiDoc = new TitoloUniformeType();

        datiDoc.setTipoAuthority(SbnAuthority.TU);
        //Decodificatore.get...
        if(titolo.getFL_CONDIVISO() != null)
        	datiDoc.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));
        datiDoc.setT001(titolo.getBID());
        A230 a230 = new A230();
        a230.setA_230(titolo.getISBD());
        datiDoc.setT230(a230);
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        doc.setDatiElementoAut(datiDoc);
        return doc;
    }

    /** formatta legami con autori */
    public ArrivoLegame formattaLegameAutoreListaSintetica(Tb_titolo titolo, Vl_autore_tit autore) {
        ArrivoLegame arrLegame = new ArrivoLegame();

        LegameElementoAutType legameEl = new LegameElementoAutType();
        legameEl.setTipoAuthority(SbnAuthority.valueOf("AU"));
        //Setto i valori del legame
        legameEl.setIdArrivo(autore.getVID());
        String tp_nome = autore.getTP_NOME_AUT();
        String tp_respons = autore.getTP_RESPONSABILITA();
        legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameEl.setTipoRespons(SbnRespons.valueOf(tp_respons));

        //Setto autore principale
        legameEl.setElementoAutLegato(formattaAutoreLegatoListaSintetica(autore));

        arrLegame.setLegameElementoAut(legameEl);
        return arrLegame;
    }

    /** formatta autore per legame */
    public ElementAutType formattaAutoreLegatoListaSintetica(Tb_autore autore) {
        ElementAutType eleAut = new ElementAutType();
        DatiElementoType datiEl;
        TipiAutore tipiAutore = new TipiAutore();
        //Verifico se è personale
        if (tipiAutore.isPersonale(autore)) {
            datiEl = new AutorePersonaleType();
            //Creo il nome
             ((AutorePersonaleType) datiEl).setT200(tipiAutore.calcolaT200(autore));
        } else {
            datiEl = new EnteType();
            //Creo il nome
             ((EnteType) datiEl).setT210(tipiAutore.calcolaT210(autore));
        }
        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        datiEl.setTipoAuthority(SbnAuthority.valueOf("AU"));
         //almaviva DA REINSERIRE
        if(autore.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));

        datiEl.setT001(autore.getVID());
        eleAut.setDatiElementoAut(datiEl);
        return eleAut;
    }

    public Object formattaDocumentoLegatoPerEsame(Tb_titolo titolo)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception{
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(formattaDocumentoPerEsameAnalitico(titolo));
        TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
        doc.setDocumentoTypeChoice(docChoice);
        LegamiType[] leg = formattaLegamiPerEsameAnalitico(titolo);
        doc.setLegamiDocumento(leg);
        LegamiType lega = titoloCerca.formattaLegamiDocDoc(titolo);
        //Se ci sono legami li aggiungo? Si, non di altri documenti.
        if (lega.enumerateArrivoLegame().hasMoreElements()) {
            doc.addLegamiDocumento(0,lega);
        }
        return doc;
    }

    /** Filtra legami tra documenti */
    public String filtraLegameDocSint(Vl_titolo_tit_b relaz) {
        String tipo =
            convertiTpLegame(relaz.getTP_LEGAME(), relaz.getCD_NATURA_BASE(), relaz.getCD_NATURA_COLL());
        if (tipo == null)
            return null;
        if (tipo.equals("410") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() && !legame_461) {
            return tipo;
        }
        if (tipo.equals("461") && tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            legame_461 = true;
            return tipo;
        }
        if (tipo.equals("500"))
            return tipo;
        return null;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        SbnLegameAut tipo = convertiTipoLegameAutore(relaz.getTP_NOME_AUT(), relaz.getTP_RESPONSABILITA());
        if (tipo.getType() == SbnLegameAut.valueOf("700").getType() || tipo.getType() == SbnLegameAut.valueOf("710").getType())
            return true; //Autore principale
        return false;
    }



}
