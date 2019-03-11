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
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Incipit;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_incipit;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_per_int;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C923;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndIncipit;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
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
public class FormatoDocumentoMusica extends FormatoDocumento {

    public FormatoDocumentoMusica() {
    }

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        MusicaType datiDoc = new MusicaType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        SbnData date = null;

        if (titolo.getAA_PUBB_1() != null){
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(annoDate.getAnnoDate());
        }
        datiDoc.setT100(data);

        if (visualizzaTimestamp())
        {
            SbnDatavar datevar = new SbnDatavar(titolo.getTS_VAR());
            datiDoc.setT005(datevar.getT005Date());
        }

        Isbd isbd = creaIsbd(titolo);
        Musica musicaDB = new Musica();
        String bid = titolo.getBID();

        // INIZIO BUG MANTIS 6152 esercizio: almaviva2 Aprile 2016
		// Siamo nel caso di un documento Audiovisivo ma l'utente non è abilitato a questo materiale ma solo alla musica: in questo caso
        // il software deve  controllare che a prescindere dal DocType (che in questo caso è MUSICA) il tipo Materiale sia Audiovisivo e a
		// questo punto, se il record sulla tabella tb_musica non è stato creato i dati relativi al livello di autorità e all'ISBD vanno
        // presi dal record della tb_titolo;
		Tb_musica musica = musicaDB.cercaPerId(bid);
        if (musica == null) {
        	 if (titolo.getTP_MATERIALE().equals("H")) {
     			datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
     	        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
     	            datiDoc.setT200(isbd.getC200(160));
     	            datiDoc.setT210(isbd.getC210(160));
     	        } else {
     	            datiDoc.setT200(isbd.getC200(80));
     	            datiDoc.setT210(isbd.getC210(80));
     	        }
        	 } else if (titolo.getTP_MATERIALE().equals("U")) {
        		 // Intervento interno del 28.07.2016 su FormatoDocumentoMusica per ripondere
        		 // con una lista sintetica anche in presenza di documenti di tipo Musicale che non
        		 //hanno la relativa occorrenza sulla tabella tb_musica - almaviva2
     			datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
     	        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
     	            datiDoc.setT200(isbd.getC200(160));
     	            datiDoc.setT210(isbd.getC210(160));
     	        } else {
     	            datiDoc.setT200(isbd.getC200(80));
     	            datiDoc.setT210(isbd.getC210(80));
     	        }
        	 } else  {
        		log.error("Legame con musica non trovato");
   	            throw new EccezioneDB(3029, "Legame con musica non trovato per id " + bid);
        	 }
        } else {
	        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(musica.getCD_LIVELLO())));
	        C128 c128 = new C128();
	        if (musica.getTP_ELABORAZIONE() != null && !musica.getTP_ELABORAZIONE().trim().equals(""))
	            c128.setD_128(
	                Decodificatore.getCd_unimarc("Tb_musica", "tp_elaborazione", musica.getTP_ELABORAZIONE()));
	        //tipo elaborazione
	        c128.setB_128(musica.getDS_ORG_SINT()); //organico sintetico dell'elab.
	        if (c128.getD_128() != null || c128.getB_128() != null)
	            datiDoc.setT128(c128);
	        C125 c125 = new C125();
	        // Modifica almaviva2 28/10/2016 ripresa pari pari da INDICE
	        //c125.setB_125(musica.getTP_TESTO_LETTER());
	        datiDoc.setT125(c125);
	        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
	            datiDoc.setT200(isbd.getC200(160));
	            datiDoc.setT210(isbd.getC210(160));
	            if (musica.getCD_PRESENTAZIONE() != null)
	                c125.setA_125_0(
	                    Decodificatore.getCd_unimarc(
	                        "Tb_musica",
	                        "cd_presentazione",
	                        musica.getCD_PRESENTAZIONE()));
	        } else {
	            datiDoc.setT200(isbd.getC200(80));
	            datiDoc.setT210(isbd.getC210(80));
	        }
	        if (musica.getDATAZIONE()!=null) {
	            C923 c923 = new C923();
	            c923.setE_923(musica.getDATAZIONE());
	            datiDoc.setT923(c923);
	        }
        }
		// FINE BUG MANTIS 6152 esercizio: almaviva2 Aprile 2016

        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        MusicaType datiDoc = new MusicaType();
        formattaDocumentoBase(datiDoc, titolo);

        Impronta impr = new Impronta();
        String bid = titolo.getBID();
		List v = impr.cercaPerBid(bid);
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
                    + bid);
        }
        if (titolo.getTP_AA_PUBB() != null)
            data.setA_100_8(Decodificatore.getCd_unimarc("Tb_titolo", "tp_aa_pubb", titolo.getTP_AA_PUBB()));
        if (titolo.getAA_PUBB_1() != null)        {
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_1());
            data.setA_100_9(annoDate.getAnnoDate());
        }
        if (titolo.getAA_PUBB_2() != null){
            AnnoDate annoDate = new AnnoDate(titolo.getAA_PUBB_2());
            data.setA_100_13(annoDate.getAnnoDate());
        }
        datiDoc.setT100(data);
        formattaT101(datiDoc, titolo);
        if (titolo.getCD_PAESE() != null && !titolo.getCD_PAESE().trim().equals("")) {
            C102 t102 = new C102();
            String paese = Decodificatore.getCd_unimarc("PAES", titolo.getCD_PAESE());
            t102.setA_102(paese == null ? titolo.getCD_PAESE() : paese);
            datiDoc.setT102(t102);
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

        // INIZIO BUG MANTIS 6152 esercizio: almaviva2 Aprile 2016
        // Siamo nel caso di un documento Audiovisivo ma l'utente non è abilitato a questo materiale ma solo alla musica: in questo caso
        // il software deve  controllare che a prescindere dal DocType (che in questo caso è MUSICA) il tipo Materiale sia Audiovisivo e a
        // questo punto, se il record sulla tabella tb_musica non è stato creato i dati relativi al livello di autorità e all'ISBD vanno
        // presi dal record della tb_titolo;

	        Musica musicaDB = new Musica();
	        Tb_musica musica = musicaDB.cercaPerId(bid);
	        if (musica == null) {
	        	 if (titolo.getTP_MATERIALE().equals("H")) {
	        		 datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
	        	 } else if (titolo.getTP_MATERIALE().equals("U")) {
	        		// Intervento interno del 28.07.2016 su FormatoDocumentoMusica per ripondere
	        		// con una lista sintetica anche in presenza di documenti di tipo Musicale che non
	        		//hanno la relativa occorrenza sulla tabella tb_musica - almaviva2
	        		 log.error("Legame con musica non trovato");
			            throw new EccezioneDB(3029, "Attenzione: il documento id " + bid + " è disallineato: effettuare Allinea da Indice");
	        	 } else  {
	 	            log.error("Legame con musica non trovato");
		            throw new EccezioneDB(3029, "Legame con musica non trovato per id " + bid);
	        	 }
	        } else  {
	        	datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(musica.getCD_LIVELLO())));
		        C125 c125 = new C125();
		        // Modifica almaviva2 28/10/2016 ripresa pari pari da INDICE
		        //c125.setB_125(musica.getTP_TESTO_LETTER()); //Testo letterario
		        if (musica.getCD_PRESENTAZIONE() != null) {
		            c125.setA_125_0(
		                Decodificatore.getCd_unimarc("Tb_musica", "cd_presentazione", musica.getCD_PRESENTAZIONE()));
		        } else {
		          //NA = non applicabile. Così viene visto come un titolo di musica
		          c125.setA_125_0(
		              Decodificatore.getCd_unimarc("Tb_musica", "cd_presentazione", "NA"));
		        }
		        datiDoc.setT125(c125);
		        C128 c128 = new C128();
		        if (musica.getTP_ELABORAZIONE() != null && !musica.getTP_ELABORAZIONE().trim().equals(""))
		            c128.setD_128(
		                Decodificatore.getCd_unimarc("Tb_musica", "tp_elaborazione", musica.getTP_ELABORAZIONE()));
		        //tipo elaborazione
		        c128.setB_128(musica.getDS_ORG_SINT()); //organico sintetico dell'elab.
		        c128.setC_128(musica.getDS_ORG_ANAL());
		        if (c128.getD_128() != null || c128.getB_128() != null || c128.getC_128() != null)
		            datiDoc.setT128(c128);
		        C923 c923 = new C923();
		        if (musica.getCD_STESURA() != null)
		            c923.setB_923(Decodificatore.getCd_unimarc("Tb_musica", "cd_stesura", musica.getCD_STESURA()));
		        if (!musica.getFL_COMPOSITO().equals(" "))
		            c923.setC_923(SbnIndicatore.valueOf(musica.getFL_COMPOSITO()));
		        if (!musica.getFL_PALINSESTO().equals(" "))
		            c923.setD_923(SbnIndicatore.valueOf(musica.getFL_PALINSESTO()));
		        c923.setE_923(musica.getDATAZIONE());
		        if (musica.getCD_MATERIA() != null)
		            c923.setG_923(Decodificatore.getCd_unimarc("Tb_musica", "cd_materia", musica.getCD_MATERIA()));
		        c923.setH_923(musica.getDS_ILLUSTRAZIONI());
		        c923.setI_923(musica.getNOTAZIONE_MUSICALE());
		        c923.setL_923(musica.getDS_LEGATURA());
		        c923.setM_923(musica.getDS_CONSERVAZIONE());
		        if (c923.getB_923() != null
		            || c923.getC_923() != null
		            || c923.getD_923() != null
		            || c923.getE_923() != null
		            || c923.getG_923() != null
		            || c923.getH_923() != null
		            || c923.getI_923() != null
		            || c923.getL_923() != null
		            || c923.getM_923() != null)
		            datiDoc.setT923(c923);

	        }
		// FINE BUG MANTIS 6152 esercizio: almaviva2 Aprile 2016



        Rappresentazione rappDB = new Rappresentazione();
        Tb_rappresent rapp = rappDB.cercaPerId(bid);
        if (rapp != null) {
            C922 c922 = new C922();
            if (rapp.getTP_GENERE() != null)
                c922.setA_922(
                    Decodificatore.getCd_unimarc("Tb_rappresent", "tp_genere", rapp.getTP_GENERE()));
            if (rapp.getAA_RAPP() != null)
                c922.setP_922(rapp.getAA_RAPP());
            c922.setQ_922(rapp.getDS_PERIODO());
            c922.setR_922(rapp.getDS_TEATRO());
            c922.setS_922(rapp.getDS_LUOGO_RAPP());
            c922.setU_922(rapp.getDS_OCCASIONE());
            c922.setT_922(rapp.getNOTA_RAPP());
            datiDoc.setT922(c922);
        }
        Incipit incipitDB = new Incipit();
        Tb_incipit incipit;
        v = incipitDB.cercaPerBid(bid);
        for (int i = 0; i < v.size(); i++) {
            incipit = (Tb_incipit) v.get(i);
            C926 c926 = new C926();
            if (incipit.getTP_INDICATORE() != null && !incipit.getTP_INDICATORE().equals(" "))
                c926.setA_926(
                    IndIncipit.valueOf(
                        Decodificatore.getCd_unimarc(
                            "Tb_incipit",
                            "tp_indicatore",
                            incipit.getTP_INDICATORE())));
            c926.setB_926(incipit.getNUMERO_COMP());
            c926.setC_926(incipit.getDS_CONTESTO());
            c926.setF_926(incipit.getNUMERO_MOV());
            c926.setG_926(incipit.getNUMERO_P_MOV());
            c926.setH_926(incipit.getREGISTRO_MUS());
            if (incipit.getCD_FORMA() != null)
                c926.setI_926(Decodificatore.getCd_unimarc("Tb_incipit", "cd_forma", incipit.getCD_FORMA()));
            if (incipit.getCD_TONALITA() != null)
                c926.setL_926(
                    Decodificatore.getCd_unimarc("Tb_incipit", "cd_tonalita", incipit.getCD_TONALITA()));
            c926.setM_926(incipit.getCHIAVE_MUS());
            c926.setN_926(incipit.getALTERAZIONE());
            c926.setO_926(incipit.getMISURA());
            c926.setP_926(incipit.getTEMPO_MUS());
            c926.setQ_926(incipit.getNOME_PERSONAGGIO());
            c926.setR_926(incipit.getBID_LETTERARIO());
            datiDoc.addT926(c926);
        }
        Personaggio persDB = new Personaggio();
        Tb_personaggio pers;
        v = persDB.cercaPerBid(bid);
        for (int i = 0; i < v.size(); i++) {
            pers = (Tb_personaggio) v.get(i);
            C927 c927 = new C927();
            c927.setA_927(pers.getNOME_PERSONAGGIO());
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

    /** formatta i legami di un documento */
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
        Autore aut = new Autore();
        List autori = aut.cercaAutorePerTitolo(titolo.getBID());
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
        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType()) {
            legameDoc.setSequenza(titolo.getSEQUENZA());
        }
        //Setto i valori del documento legato
        legameDoc.setDocumentoLegato(formattaDocumentoLegatoListaSintetica(titolo));
        legameDoc.setTipoLegame(
            SbnLegameDoc.valueOf(
                convertiTpLegame(
                    titolo.getTP_LEGAME(),
                    titolo.getCD_NATURA_BASE(),
                    titolo.getCD_NATURA_COLL())));
        if(titolo.getFL_CONDIVISO_LEGAME() != null)
        	legameDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO_LEGAME()));
        arrLegame.setLegameDoc(legameDoc);
        return arrLegame;
    }

    /** Prepara un legame tra due titoli
     * Per ora utilizzo due titoli e un legame, forse servirà una vista e un titolo
     */
    public ArrivoLegame formattaLegameTitUniListaSintetica(Vl_titolo_tit_b tit_arrivo) throws EccezioneDB {
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
        if (tit_arrivo.getTP_MATERIALE().equals("U"))
            legameDoc.setTipoAuthority(SbnAuthority.UM);
        else
            legameDoc.setTipoAuthority(SbnAuthority.TU);
        arrLegame.setLegameElementoAut(legameDoc);
        return arrLegame;
    }

    /**formatta il documento legato */
    public ElementAutType formattaTitUniLegatoListaSintetica(Tb_titolo titolo) throws EccezioneDB {
        ElementAutType doc = new ElementAutType();
        if (titolo.getTP_MATERIALE().equals("U")) {
            TitoloUniformeMusicaType datiDoc = new TitoloUniformeMusicaType();
            datiDoc.setTipoAuthority(SbnAuthority.UM);
            if(titolo.getFL_CONDIVISO() != null)
            	datiDoc.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

            //Decodificatore.get...
            datiDoc.setT001(titolo.getBID());
            A230 a230 = new A230();
            a230.setA_230(titolo.getISBD());
            datiDoc.setT230(a230);
            datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
            doc.setDatiElementoAut(datiDoc);
        } else {
            TitoloUniformeType datiDoc = new TitoloUniformeType();
            datiDoc.setTipoAuthority(SbnAuthority.TU);
            if(titolo.getFL_CONDIVISO() != null)
            	datiDoc.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

            //Decodificatore.get...
            datiDoc.setT001(titolo.getBID());
            A230 a230 = new A230();
            a230.setA_230(titolo.getISBD());
            datiDoc.setT230(a230);
            datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
            doc.setDatiElementoAut(datiDoc);
        }
        return doc;
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
        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        datiDoc.setT001(titolo.getBID());
        datiDoc.setNaturaDoc(SbnNaturaDocumento.valueOf(titolo.getCD_NATURA()));
        Isbd isbd = creaIsbd(titolo);
        datiDoc.setT200(isbd.getC200(80));
        datiDoc.setT210(isbd.getC210());
        DocumentoTypeChoice choice = new DocumentoTypeChoice();
        choice.setDatiDocumento(datiDoc);
        doc.setDocumentoTypeChoice(choice);
        return doc;
    }

    /**
     * Prepara legame tra titolo e autore: viene ridefinito, bisogna vedere se viene invocato.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameAutore(Tb_titolo tit_partenza, Vl_autore_tit relaz)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.valueOf("AU"));
        legameAut.setIdArrivo(relaz.getVID());
        legameAut.setRelatorCode(relaz.getCD_RELAZIONE());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("710"));
        //relaz.getCD_RELAZIONE()));//Tp_responsabilita()));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoAutoreTitolo forAut = new FormatoAutoreTitolo(tipoOut, tipoOrd, user);
        //ElementAutType el = new ElementAutType();
        //el.setDatiElementoAut(forAut.formattaAutorePerLegameTitolo(relaz));
        legameAut.setElementoAutLegato(forAut.formattaAutore(relaz));
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    public Object formattaDocumentoLegatoPerEsame(Tb_titolo titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
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
            doc.addLegamiDocumento(0, lega);
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
        if (tipo.equals("461")) {
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
