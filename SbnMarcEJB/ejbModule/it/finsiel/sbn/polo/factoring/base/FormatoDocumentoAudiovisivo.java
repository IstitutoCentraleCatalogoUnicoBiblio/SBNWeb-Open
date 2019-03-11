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
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Audiovisivo;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Discosonoro;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_audiovideo;
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_per_int;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C115;
import it.iccu.sbn.ejb.model.unimarcmodel.C126;
import it.iccu.sbn.ejb.model.unimarcmodel.C127;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
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
public class FormatoDocumentoAudiovisivo extends FormatoDocumento {

    public FormatoDocumentoAudiovisivo() {
    }

    public DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
    	AudiovisivoType datiDoc = new AudiovisivoType();
        formattaDocumentoBase(datiDoc, titolo);
        C100 data = new C100();
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        SbnData date = null;
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
        Audiovisivo audiovisivo = new Audiovisivo();
        Tb_audiovideo tb_audiovideo = audiovisivo.cercaPerId(titolo.getBID());
        if (tb_audiovideo != null) {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_audiovideo.getCD_LIVELLO())));
          C115 c115 = new C115();
          if (tb_audiovideo.getTP_MATER_AUDIOVIS() != null)
              c115.setA_115_0(Decodificatore.getCd_unimarc("TIVI", tb_audiovideo.getTP_MATER_AUDIOVIS()));
          if (tb_audiovideo.getCD_COLORE() != null)
          	  c115.setA_115_4(Decodificatore.getCd_unimarc("INDC", tb_audiovideo.getCD_COLORE()));
          if (tb_audiovideo.getCD_FORMA_VIDEO() != null)
          	c115.setA_115_8(Decodificatore.getCd_unimarc("FODI", tb_audiovideo.getCD_FORMA_VIDEO()));
          if (tb_audiovideo.getCD_TECNICA() != null)
          	c115.setA_115_9(Decodificatore.getCd_unimarc("TEVI", tb_audiovideo.getCD_TECNICA()));
          if (tb_audiovideo.getCD_FORMA_REGIST() != null)
          	c115.setA_115_15(Decodificatore.getCd_unimarc("PUVI", tb_audiovideo.getCD_FORMA_REGIST()));
          datiDoc.setT115(c115);
        } else {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        }

        Discosonoro discosonoro = new Discosonoro();
        Tb_disco_sonoro tb_disco_sonoro = discosonoro.cercaPerId(titolo.getBID());
		if (tb_disco_sonoro != null) {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_disco_sonoro.getCD_LIVELLO())));
//			datiDoc.setLivelloAut(SbnLivello.valueOf("51"));
			C126 c126 = new C126();
	        if (tb_disco_sonoro.getCD_FORMA() != null)
	        	c126.setA_126_0(Decodificatore.getCd_unimarc("FPUB", tb_disco_sonoro.getCD_FORMA()));
	        if (tb_disco_sonoro.getCD_VELOCITA() != null)
	        	c126.setA_126_1(Decodificatore.getCd_unimarc("VELO", tb_disco_sonoro.getCD_VELOCITA()));
	          datiDoc.setT126(c126);
		} else {
          datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        }

        datiDoc.setT200(isbd.getC200());
        datiDoc.setT205(isbd.getC205());
        return datiDoc;
    }

    public DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
    	AudiovisivoType datiDoc = new AudiovisivoType();
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

        if (titolo.getTP_RECORD_UNI().equals("g")) {
            Audiovisivo audiovisivo = new Audiovisivo();
            Tb_audiovideo tb_audiovideo = audiovisivo.cercaPerId(bid);
    		if (tb_audiovideo == null) {
    			log.error("Legame con audiovideo non trovato");
    			throw new EccezioneDB(3029, "Legame audiovideo non trovato per id " + bid);
    		}
            datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_audiovideo.getCD_LIVELLO())));
    		C115 c115 = new C115();
            if (tb_audiovideo.getTP_MATER_AUDIOVIS() != null) {
                c115.setA_115_0(Decodificatore.getCd_unimarc("TIVI", tb_audiovideo.getTP_MATER_AUDIOVIS())); }
            else {
            	throw new EccezioneDB(3360, "Tipo di video obbligatorio");
            }
            if (ValidazioneDati.isFilled(tb_audiovideo.getLUNGHEZZA()) )
            	c115.setA_115_1(Integer.toString(tb_audiovideo.getLUNGHEZZA()));
            if (tb_audiovideo.getCD_COLORE() != null)
            	c115.setA_115_4(Decodificatore.getCd_unimarc("INDC", tb_audiovideo.getCD_COLORE()));
            if (tb_audiovideo.getCD_SUONO() != null)
            	c115.setA_115_5(Decodificatore.getCd_unimarc("INDS", tb_audiovideo.getCD_SUONO()));
            if (tb_audiovideo.getTP_MEDIA_SUONO() != null)
            	c115.setA_115_6(Decodificatore.getCd_unimarc("SUSU", tb_audiovideo.getTP_MEDIA_SUONO()));
            if (tb_audiovideo.getCD_DIMENSIONE() != null)
            	c115.setA_115_7(Decodificatore.getCd_unimarc("LARG", tb_audiovideo.getCD_DIMENSIONE()));
            if (tb_audiovideo.getCD_FORMA_VIDEO() != null)
            	c115.setA_115_8(Decodificatore.getCd_unimarc("FODI", tb_audiovideo.getCD_FORMA_VIDEO()));
            if (tb_audiovideo.getCD_TECNICA() != null)
            	c115.setA_115_9(Decodificatore.getCd_unimarc("TEVI", tb_audiovideo.getCD_TECNICA()));
            if (tb_audiovideo.getTP_FORMATO_FILM() != null)
            	c115.setA_115_10(Decodificatore.getCd_unimarc("FPIM", tb_audiovideo.getTP_FORMATO_FILM()));
            String appo[] = new String[4];
            if (tb_audiovideo.getCD_MAT_ACCOMP_1() != null)
            	appo[0]=Decodificatore.getCd_unimarc("MACC", tb_audiovideo.getCD_MAT_ACCOMP_1());
            if (tb_audiovideo.getCD_MAT_ACCOMP_2() != null)
            	appo[1]=Decodificatore.getCd_unimarc("MACC", tb_audiovideo.getCD_MAT_ACCOMP_2());
            if (tb_audiovideo.getCD_MAT_ACCOMP_3() != null)
            	appo[2]=Decodificatore.getCd_unimarc("MACC", tb_audiovideo.getCD_MAT_ACCOMP_3());
            if (tb_audiovideo.getCD_MAT_ACCOMP_4() != null)
            	appo[3]=Decodificatore.getCd_unimarc("MACC", tb_audiovideo.getCD_MAT_ACCOMP_4());
            c115.setA_115_11(appo);
            if (tb_audiovideo.getCD_FORMA_REGIST() != null)
            	c115.setA_115_15(Decodificatore.getCd_unimarc("PUVI", tb_audiovideo.getCD_FORMA_REGIST()));
            if (tb_audiovideo.getTP_FORMATO_VIDEO() != null)
            	c115.setA_115_16(Decodificatore.getCd_unimarc("PRVI", tb_audiovideo.getTP_FORMATO_VIDEO()));
            if (tb_audiovideo.getCD_MATERIALE_BASE() != null)
            	c115.setA_115_17(Decodificatore.getCd_unimarc("EMUL", tb_audiovideo.getCD_MATERIALE_BASE()));
            if (tb_audiovideo.getCD_SUPPORTO_SECOND() != null)
            	c115.setA_115_18(Decodificatore.getCd_unimarc("MASU", tb_audiovideo.getCD_SUPPORTO_SECOND()));
            if (tb_audiovideo.getCD_BROADCAST() != null)
            	c115.setA_115_19(Decodificatore.getCd_unimarc("STRA", tb_audiovideo.getCD_BROADCAST()));
            if (tb_audiovideo.getTP_GENERAZIONE() != null)
            	c115.setB_115_0(Decodificatore.getCd_unimarc("VERS", tb_audiovideo.getTP_GENERAZIONE()));
            if (tb_audiovideo.getCD_ELEMENTI() != null)
            	c115.setB_115_1(Decodificatore.getCd_unimarc("ELEP", tb_audiovideo.getCD_ELEMENTI()));
            if (tb_audiovideo.getCD_CATEG_COLORE() != null)
            	c115.setB_115_2(Decodificatore.getCd_unimarc("SCCF", tb_audiovideo.getCD_CATEG_COLORE()));
            if (tb_audiovideo.getCD_POLARITA() != null)
            	c115.setB_115_3(Decodificatore.getCd_unimarc("EMUP", tb_audiovideo.getCD_POLARITA()));
            if (tb_audiovideo.getCD_PELLICOLA() != null)
            	c115.setB_115_4(Decodificatore.getCd_unimarc("COPE", tb_audiovideo.getCD_PELLICOLA()));
            if (tb_audiovideo.getTP_SUONO() != null)
            	c115.setB_115_5(Decodificatore.getCd_unimarc("TISI", tb_audiovideo.getTP_SUONO()));
            if (tb_audiovideo.getTP_STAMPA_FILM() != null)
            	c115.setB_115_6(Decodificatore.getCd_unimarc("TIPE", tb_audiovideo.getTP_STAMPA_FILM()));
//            if (tb_audiovideo.getCd_deteriore() != null)
//            	c115.setB_115_7(Decodificatore.getCd_unimarc("SDET", tb_audiovideo.getCd_deteriore()));
//            if (tb_audiovideo.getCd_completo() != null)
//            	c115.setB_115_8(Decodificatore.getCd_unimarc("COMP", tb_audiovideo.getCd_completo()));
//            c115.setB_115_9(tb_audiovideo.getDt_ispezione());
            datiDoc.setT115(c115);

            Musica musicaDB = new Musica();
            Tb_musica musica = musicaDB.cercaPerId(bid);
            if (musica != null) {
	            C128 c128 = new C128();
	            if (musica.getTP_ELABORAZIONE() != null && !musica.getTP_ELABORAZIONE().trim().equals(""))
	                c128.setD_128(Decodificatore.getCd_unimarc("Tb_musica", "tp_elaborazione", musica.getTP_ELABORAZIONE()));
	            c128.setB_128(musica.getDS_ORG_SINT());
	            c128.setC_128(musica.getDS_ORG_ANAL());
	            if (c128.getD_128() != null || c128.getB_128() != null || c128.getC_128() != null)
	                datiDoc.setT128(c128);
            }

            formattaDatiComuni1(datiDoc, titolo);

            Rappresentazione rappDB = new Rappresentazione();
            Tb_rappresent rapp = rappDB.cercaPerId(bid);
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
            List v = persDB.cercaPerBid(bid);
            for (int i = 0; i < v.size(); i++) {
                pers = (Tb_personaggio) v.get(i);
                C927 c927 = new C927();
    			if (pers.getNOME_PERSONAGGIO() != null)
    	            c927.setA_927(pers.getNOME_PERSONAGGIO().trim());
                if (pers.getCD_TIMBRO_VOCALE() != null)
                    c927.setB_927(
                        Decodificatore.getCd_unimarc("Tb_personaggio", "cd_timbro_vocale", pers.getCD_TIMBRO_VOCALE()));
                Tr_per_int tr_per_int = persDB.cercaInterprete((int) pers.getID_PERSONAGGIO());
                if (tr_per_int != null)
                    c927.setC3_927(tr_per_int.getVID());
                datiDoc.addT927(c927);
            }

        } //end if tipo record 'g'

        if (titolo.getTP_RECORD_UNI().equals("i") || titolo.getTP_RECORD_UNI().equals("j")) {
	        Discosonoro discosonoro = new Discosonoro();
	        Tb_disco_sonoro tb_disco_sonoro = discosonoro.cercaPerId(bid);
			if (tb_disco_sonoro == null) {
				log.error("Legame con disco sonoro non trovato");
				throw new EccezioneDB(3029, "Legame con disco non trovato per id " + bid);
			}
            datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_disco_sonoro.getCD_LIVELLO())));
	        C126 c126 = new C126();
	        if (tb_disco_sonoro.getCD_FORMA() != null) {
	        	c126.setA_126_0(Decodificatore.getCd_unimarc("FPUB", tb_disco_sonoro.getCD_FORMA())); }
            else {
            	throw new EccezioneDB(3361, "Forma di pubblicazione obbligatoria");
            }
	        if (tb_disco_sonoro.getCD_VELOCITA() != null)
	        	c126.setA_126_1(Decodificatore.getCd_unimarc("VELO", tb_disco_sonoro.getCD_VELOCITA()));
	        if (tb_disco_sonoro.getTP_SUONO() != null)
	        	c126.setA_126_2(Decodificatore.getCd_unimarc("TISU", tb_disco_sonoro.getTP_SUONO()));
	        if (tb_disco_sonoro.getCD_PISTA() != null)
	        	c126.setA_126_3(Decodificatore.getCd_unimarc("LASC", tb_disco_sonoro.getCD_PISTA()));
	        if (tb_disco_sonoro.getCD_DIMENSIONE() != null)
	        	c126.setA_126_4(Decodificatore.getCd_unimarc("DINA", tb_disco_sonoro.getCD_DIMENSIONE()));
	        if (tb_disco_sonoro.getCD_LARG_NASTRO() != null)
	        	c126.setA_126_5(Decodificatore.getCd_unimarc("LANA", tb_disco_sonoro.getCD_LARG_NASTRO()));
	        if (tb_disco_sonoro.getCD_CONFIGURAZIONE() != null)
	        	c126.setA_126_6(Decodificatore.getCd_unimarc("CONA", tb_disco_sonoro.getCD_CONFIGURAZIONE()));
	        String appoDisco[] = new String[6];
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_1() != null)
	        	appoDisco[0]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_1());
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_2() != null)
	        	appoDisco[1]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_2());
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_3() != null)
	        	appoDisco[2]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_3());
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_4() != null)
	        	appoDisco[3]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_4());
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_5() != null)
	        	appoDisco[4]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_5());
	        if (tb_disco_sonoro.getCD_MATER_ACCOMP_6() != null)
	        	appoDisco[5]=Decodificatore.getCd_unimarc("MATA", tb_disco_sonoro.getCD_MATER_ACCOMP_6());
	        c126.setA_126_7(appoDisco);
	        if (tb_disco_sonoro.getCD_TECNICA_REGIS() != null)
	        	c126.setA_126_13(Decodificatore.getCd_unimarc("TERE", tb_disco_sonoro.getCD_TECNICA_REGIS()));
	        if (tb_disco_sonoro.getCD_RIPRODUZIONE() != null)
	        	c126.setA_126_14(Decodificatore.getCd_unimarc("SCAR", tb_disco_sonoro.getCD_RIPRODUZIONE()));
	        if (tb_disco_sonoro.getTP_DISCO() != null)
	        	c126.setB_126_0(Decodificatore.getCd_unimarc("TDIS", tb_disco_sonoro.getTP_DISCO()));
	        if (tb_disco_sonoro.getTP_MATERIALE() != null)
	            c126.setB_126_1(Decodificatore.getCd_unimarc("TIMA", tb_disco_sonoro.getTP_MATERIALE()));
	        if (tb_disco_sonoro.getTP_TAGLIO() != null)
	        	c126.setB_126_2(Decodificatore.getCd_unimarc("TITA", tb_disco_sonoro.getTP_TAGLIO()));
        	datiDoc.setT126(c126);
	        if (tb_disco_sonoro.getDURATA() != null)
	        {
	        	C127 c127 = new C127();
	        	c127.setA_127_a(tb_disco_sonoro.getDURATA());
		        datiDoc.setT127(c127);
	        }

            Musica musicaDB = new Musica();
            Tb_musica musica = musicaDB.cercaPerId(bid);
            if (musica != null) {
	            C128 c128 = new C128();
	            if (musica.getTP_ELABORAZIONE() != null && !musica.getTP_ELABORAZIONE().trim().equals(""))
	                c128.setD_128(Decodificatore.getCd_unimarc("Tb_musica", "tp_elaborazione", musica.getTP_ELABORAZIONE()));
	            c128.setB_128(musica.getDS_ORG_SINT());
	            c128.setC_128(musica.getDS_ORG_ANAL());
	            if (c128.getD_128() != null || c128.getB_128() != null || c128.getC_128() != null)
	                datiDoc.setT128(c128);
            }

            formattaDatiComuni1(datiDoc, titolo);

            Rappresentazione rappDB = new Rappresentazione();
            Tb_rappresent rapp = rappDB.cercaPerId(bid);
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
            List v = persDB.cercaPerBid(bid);
            for (int i = 0; i < v.size(); i++) {
                pers = (Tb_personaggio) v.get(i);
                C927 c927 = new C927();
    			if (pers.getNOME_PERSONAGGIO() != null)
    	            c927.setA_927(pers.getNOME_PERSONAGGIO().trim());
                if (pers.getCD_TIMBRO_VOCALE() != null)
                    c927.setB_927(
                        Decodificatore.getCd_unimarc("Tb_personaggio", "cd_timbro_vocale", pers.getCD_TIMBRO_VOCALE()));
                Tr_per_int tr_per_int = persDB.cercaInterprete((int)pers.getID_PERSONAGGIO());
                if (tr_per_int != null)
                    c927.setC3_927(tr_per_int.getVID());
                datiDoc.addT927(c927);
            }

        } //end if tipo record 'i' 'j'

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
