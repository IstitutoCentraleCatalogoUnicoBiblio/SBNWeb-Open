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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.Titset2;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_2;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A152;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A431;
import it.iccu.sbn.ejb.model.unimarcmodel.A801;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C231;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.types.Date;

/**
 * Classe FormatoTitoloUniforme
 * <p>
 * Si occupa della formattazione di un titolo uniforme.
 * </p>
 *
 * @author
 * @author
 *
 * @version 26-feb-03
 */
public class FormatoTitoloUniforme extends FormatoElementAut {

    public FormatoTitoloUniforme() {
    }

    /** Da verificare
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public Object formattaTitolo(Tb_titolo titolo, BigDecimal versione) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elAut = new ElementAutType();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo, schema_version);
            elAut.setLegamiElementoAut(lega);
            TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
            LegamiType leg = titoloCerca.formattaLegamiDocDoc(titolo);
            //Se ci sono legami li aggiungo? Si, non di altri documenti.
            if (leg.getArrivoLegameCount() > 0)
                elAut.addLegamiElementoAut(leg);
        } else {
            elAut.setDatiElementoAut(formattaDocumentoPerListaSintetica(titolo));
            elAut.setLegamiElementoAut(formattaLegamiPerListaSintetica(titolo));

            //formattalegame con autore di responsabilità 1
        }
        return elAut;
    }

    /**
      * Formatta i legami per un esame analitico, cercando nel DB
      * Quei documenti che hanno dei legami diversi devono ridefinire questo metodo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
      */
//    public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo)
//        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
//        LegamiType[] legami = null;
//
//        Titolo titDB = new Titolo();
//        //legami con documenti -> viene fatto dall'esterno, credo
//        //TitoloCerca titCerca = new TitoloCerca(tipoOut, tipoOrd, this);
//        //titCerca.formattaLegamiDocDoc(titolo);
//        List legamiVec = new ArrayList();
//        List vettore; // = titDB.cercaLegamiDocumento(titolo, tipoOrd);
//        //Legami con repertori
//        RepertorioTitoloUniforme repAut = new RepertorioTitoloUniforme();
//        vettore = repAut.cercaCitazioniInRepertori(titolo.getBID(), tipoOrd);
//        if (vettore.size() > 0) {
//            LegamiType legamiType = new LegamiType();
//            legamiType.setIdPartenza(titolo.getBID());
//            for (int i = 0; i < vettore.size(); i++) {
//                //Da sostituire con la vista Vl_repertorio_aut
//                Tr_rep_tit rep_tit = (Tr_rep_tit) vettore.get(i);
//                legamiType.addArrivoLegame(formattaLegameRepertorio(rep_tit));
//            }
//            if (legamiType.getArrivoLegameCount() > 0)
//                legamiVec.add(legamiType);
//        }
//
//        //Legami con autori
//        Autore autDB = new Autore();
//        vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
//        if (vettore.size() > 0) {
//            LegamiType legamiType = new LegamiType();
//            legamiType.setIdPartenza(titolo.getBID());
//            for (int i = 0; i < vettore.size(); i++) {
//                Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
//                if (filtraLegameAutore(aut_tit)) {
//                    legamiType.addArrivoLegame(formattaLegameAutoreEsameAnalitico(titolo, aut_tit));
//                }
//            }
//            if (legamiType.getArrivoLegameCount() > 0)
//                legamiVec.add(legamiType);
//        }
//        legami = new LegamiType[legamiVec.size()];
//        for (int i = 0; i < legami.length; i++) {
//            legami[i] = (LegamiType) legamiVec.get(i);
//        }
//        return legami;
//    }

    /** formatta i legami di un documento per lista sintetica, esegue una ricerca nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public LegamiType[] formattaLegamiPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami;
        List legamiVec = new ArrayList();
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

        // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
        // Legami con titoli uniformi almaviva7/almaviva4 17/05/2016
        // --------------------------------------------------------
		Vl_titolo_tit_c tit = new Vl_titolo_tit_c();
        tit.setBID_COLL(titolo.getBID());
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit);
        tavola.executeCustom("selectRinviiV" );
        List resp = tavola.getElencoRisultati(1);
        if (!resp.isEmpty())
        {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());

            ArrivoLegame arrLegame = new ArrivoLegame();
            LegameElementoAutType legameEl = new LegameElementoAutType();

            tit = (Vl_titolo_tit_c)resp.get(0);

            legameEl.setTipoAuthority(SbnAuthority.TU);
            legameEl.setTipoLegame(SbnLegameAut.valueOf("431"));
            legameEl.setIdArrivo(tit.getBID());

            ElementAutType elementoAutLegato = new ElementAutType();

//            DatiElementoType datiEl;
            TitoloUniformeType datiEl;

            datiEl = new TitoloUniformeType();
            C231 c231 = new C231();
            c231.setA_231(tit.getISBD());
            datiEl.setT231(c231);


            datiEl.setTipoAuthority(SbnAuthority.valueOf("TU"));
            datiEl.setT001(tit.getBID());
            datiEl.setNaturaTU(tit.getCD_NATURA_BASE());

            datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tit.getCD_LIVELLO())));
            elementoAutLegato.setDatiElementoAut(datiEl);
            legameEl.setElementoAutLegato(elementoAutLegato);
            arrLegame.setLegameElementoAut(legameEl);
            legamiType.addArrivoLegame(arrLegame);

            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
            }



        legami = new LegamiType[legamiVec.size()];
        for (int i = 0; i < legami.length; i++) {
            legami[i] = (LegamiType) legamiVec.get(i);
        }
        return legami;
    }

    /** formatta legami con autori */
    public ArrivoLegame formattaLegameTitoloUniformeListaSintetica(Tb_titolo titolo, Vl_autore_tit relaz) {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameElementoAutType legameEl = new LegameElementoAutType();
        legameEl.setTipoAuthority(SbnAuthority.AU);
        //Setto i valori del legame
        legameEl.setIdArrivo(relaz.getVID());
        String tp_nome = relaz.getTP_NOME_AUT();
        String tp_respons = relaz.getTP_RESPONSABILITA();
        legameEl.setTipoRespons(SbnRespons.valueOf(tp_respons));
//        //evolutiva opera 231
//        if (schema_version.floatValue() > 2.02) {
//            legameEl.setTipoLegame(convertiTipoLegameAutore203(tp_nome, tp_respons));
//        } else {
//            legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
//        }
        legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameEl.setElementoAutLegato(formattaAutoreLegatoSintetica(relaz));

        arrLegame.setLegameElementoAut(legameEl);
        return arrLegame;
    }





    public void aggiungiAltriLegami(Tb_titolo titolo,DocumentoType doc) throws EccezioneDB, EccezioneSbnDiagnostico {

    }
    /** Non mette i legami.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
//    public Object formattaElemento(Tb_titolo titolo) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
//        ElementAutType elAut = new ElementAutType();
//        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
//            elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));
//            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
//            elAut.setLegamiElementoAut(lega);
//        } else {
//            elAut.setDatiElementoAut(formattaDocumentoPerListaSintetica(titolo));
//        }
//        return elAut;
//    }

    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione) throws Exception
    {
    	//return formattaElemento(titolo, versione, CONSTANT.GESTISCI_NATURA_V_SI);
    	return formattaElemento(titolo, versione, true);
    }


    /** Non mette i legami.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV) throws IllegalArgumentException, InvocationTargetException, Exception
    	 {
        ElementAutType elAut = new ElementAutType();
        // ATTENZIONE: QUESTO IF DEVE RIMANERE DIVERSO DALLA VERSIONE DI INDICE ALTRIMNTI NON FUNZIONA
		//if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) { //Analitico
		if ((tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) || (tipoOut.getType() == SbnTipoOutput.VALUE_3.getType()) ) { //Analitico
            elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo, schema_version, gestisciNaturaV);
            elAut.setLegamiElementoAut(lega);
        } else {
            elAut.setDatiElementoAut(formattaDocumentoPerListaSintetica(titolo));
        }
        return elAut;
    }



    /**
     * Formatta il titolo uniforme per la lista sintetica
     */
    protected TitoloUniformeType formattaDocumentoPerListaSintetica(Tb_titolo titolo) {
        TitoloUniformeType datiEl = new TitoloUniformeType();
        datiEl.setTipoAuthority(SbnAuthority.valueOf("TU"));
        if(titolo.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

        datiEl.setT001(titolo.getBID());
        if (visualizzaTimestamp())
        {
            SbnDatavar datevar = null;
            datevar = new SbnDatavar(titolo.getTS_VAR());
            datiEl.setT005(datevar.getT005Date());
        }


        A230 a230 = new A230();
        C231 c231 = new C231();
        A431 a431 = new A431();
        // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
		/*dalla versione 2.03 si opera in questo modo:
		se il polo tratta versione 2.03 in sintetica viene inviato l'isbd (presente nella tb_titolo) nel sottocampo $w della 231
		se il polo tratta versioni < 2.03 in sintetica viene inviato l'isbd (presente nella tb_titolo) nella 230
		*/
       if (schema_version.floatValue() > 2.02) {
        	if (titolo.getCD_NATURA().equals("V")) {
            	a431.setA_431(titolo.getISBD());  //variante opera natura 'V'
            	datiEl.setNaturaTU(titolo.getCD_NATURA());
            	datiEl.setT431(a431);
        	} else {
            	c231.setA_231(titolo.getISBD());
            	datiEl.setNaturaTU(titolo.getCD_NATURA());
            	datiEl.setT231(c231);
        	}
        } else {
        	a230.setA_230(titolo.getISBD());
        	datiEl.setT230(a230);
        }
        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        return datiEl;

    }

    protected DatiElementoType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws EccezioneDB, InfrastructureException {
        TitoloUniformeType datiEl = new TitoloUniformeType();
        SbnDatavar datevar = null;
        datiEl.setTipoAuthority(SbnAuthority.valueOf("TU"));
        if(titolo.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

        datiEl.setT001(titolo.getBID());
        datevar = new SbnDatavar(titolo.getTS_VAR());
        datiEl.setT005(datevar.getT005Date());
        A100 data = new A100();
        try {
            SbnData date = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: titolo.aa_pubb_1:" + titolo.getAA_PUBB_1());
        }
        datiEl.setT100(data);
        if (titolo.getISADN() != null) {
            A015 a015 = new A015();
            a015.setA_015(titolo.getISADN());
            datiEl.setT015(a015);
        }
        if (titolo.getCD_LINGUA_1() != null && titolo.getCD_LINGUA_1().trim().length()>0) {
            C101 c101 = new C101();
            String lingua = Decodificatore.getCd_unimarc("LING", titolo.getCD_LINGUA_1());
            c101.addA_101(lingua == null ? titolo.getCD_LINGUA_1() : lingua);
            if (titolo.getCD_LINGUA_2() != null && !titolo.getCD_LINGUA_2().trim().equals("")) {
                lingua = Decodificatore.getCd_unimarc("LING", titolo.getCD_LINGUA_2());
                c101.addA_101(lingua == null ? titolo.getCD_LINGUA_2() : lingua);
            }
            if (titolo.getCD_LINGUA_3() != null && !titolo.getCD_LINGUA_3().trim().equals("")) {
                lingua = Decodificatore.getCd_unimarc("LING", titolo.getCD_LINGUA_3());
                c101.addA_101(lingua == null ? titolo.getCD_LINGUA_3() : lingua);
            }
            datiEl.setT101(c101);
        }

        //14/07/2017 completamento gestione 231 almaviva2 27.07.2017 adeguamento a Indice
   	   if (titolo.getCD_NATURA().equals("A")) { //si gestisce paese per opera 231
 	        if (titolo.getCD_PAESE() != null && titolo.getCD_PAESE().trim().length()>0) {
 	            C102 c102 = new C102();
 	            String paese = Decodificatore.getCd_unimarc("PAES", titolo.getCD_PAESE());
 	            c102.setA_102(paese == null ? titolo.getCD_PAESE() : paese);
 	            datiEl.setT102(c102);
 	        }
         }

        // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
        A230 a230 = new A230();
        C231 c231 = new C231();
        A431 a431 = new A431();

		/*dalla versione 2.03 si opera in questo modo:
		se il polo tratta versione 2.03 in analitica vengono inviati i campi della tb_titset_2 nel tag 231
		se il polo tratta versioni < 2.03 in analitica viene inviato l'isbd (presente nella tb_titolo) nella 230
		*/
       if (schema_version.floatValue() > 2.02) {
    	   if (titolo.getCD_NATURA().equals("A")) { //si gestisce opera 231
    		   datiEl.setNaturaTU("A");
    		   Titset2 titset2 = new Titset2();
    		   Tb_titset_2 tbt2 = titset2.cercaPerId(titolo.getBID());
    		   if (tbt2 != null) {
    			   c231.setA_231(titolo.getISBD());
    			   //14/07/2017 completamento gestione 231 almaviva2 27.07.2017 adeguamento all'indice
    		       if (ValidazioneDati.isFilled(tbt2.getS231_FORMA_OPERA()) ) {
    		            String forma = Decodificatore.getCd_unimarc("FOOP", tbt2.getS231_FORMA_OPERA());
    		            c231.setC_231(forma == null ? tbt2.getS231_FORMA_OPERA() : forma);
    		       }
    			   c231.setD_231(tbt2.getS231_DATA_OPERA());
    			   c231.setK_231(tbt2.getS231_ALTRE_CARATTERISTICHE());
    			   datiEl.setT231(c231);
    		   } else {
    			   c231.setA_231(titolo.getISBD());
    			   datiEl.setT231(c231);
    		   }
            } else { //si gestisce variante opera 431
            	datiEl.setNaturaTU("V");
            	a431.setA_431(titolo.getISBD());
		        datiEl.setT431(a431);
            }
        } else { //si gestisce titolo uniforme 230
        	a230.setA_230(titolo.getISBD());
        	datiEl.setT230(a230);
        }
//        a230.setA_230(titolo.getIsbd());
//        datiEl.setT230(a230);
        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        if (titolo.getCD_NORME_CAT() != null) {
            A152 a152 = new A152();
            a152.setA_152(titolo.getCD_NORME_CAT());
            datiEl.setT152(a152);
        }
        if (titolo.getNOTA_INF_TIT() != null) {
            A300 a300 = new A300();
            a300.setA_300(titolo.getNOTA_INF_TIT());
            datiEl.setT300(a300);
        }
        if (titolo.getCD_AGENZIA() != null && titolo.getCD_AGENZIA().length() >= 2) {
            A801 a801 = new A801();
            a801.setA_801(titolo.getCD_AGENZIA().substring(0, 2));
            a801.setB_801(titolo.getCD_AGENZIA().substring(2));
            datiEl.setT801(a801);
        }
        if (titolo.getNOTA_CAT_TIT() != null) {
            A830 a830 = new A830();
            a830.setA_830(titolo.getNOTA_CAT_TIT());
            datiEl.setT830(a830);
        }
        return datiEl;
    }

    /** formatta autore per legame */
    public ElementAutType formattaAutoreLegatoSintetica(Tb_autore autore) {
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
        datiEl.setTipoAuthority(SbnAuthority.valueOf("AU"));
        if(autore.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));

        datiEl.setT001(autore.getVID());
        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        eleAut.setDatiElementoAut(datiEl);
        return eleAut;
    }

    /** formatta legami con autori */
    public ArrivoLegame formattaLegameAutoreListaSintetica(Tb_titolo titolo, Vl_autore_tit relaz) {
        ArrivoLegame arrLegame = new ArrivoLegame();
        LegameElementoAutType legameEl = new LegameElementoAutType();
        legameEl.setTipoAuthority(SbnAuthority.AU);
        //Setto i valori del legame
        legameEl.setIdArrivo(relaz.getVID());
        String tp_nome = relaz.getTP_NOME_AUT();
        String tp_respons = relaz.getTP_RESPONSABILITA();
        legameEl.setTipoRespons(SbnRespons.valueOf(tp_respons));
        legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameEl.setElementoAutLegato(formattaAutoreLegatoSintetica(relaz));

        arrLegame.setLegameElementoAut(legameEl);
        return arrLegame;
    }

    /**
     * Formatta un legame tra repertorio e titolo
     * @throws InfrastructureException
     */
    protected ArrivoLegame formattaLegameRepertorio(Tr_rep_tit rep_tit) throws EccezioneDB, InfrastructureException {
        ArrivoLegame arrLegame = new ArrivoLegame();

        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.RE);
        Repertorio repertorio = new Repertorio();
        arrLegame.setLegameElementoAut(legameAut);
        Tb_repertorio rep = repertorio.cercaRepertorioId((int) rep_tit.getID_REPERTORIO());
        if (rep_tit.getFL_TROVATO().equals("S"))
            legameAut.setTipoLegame(SbnLegameAut.valueOf("810"));
        else
            legameAut.setTipoLegame(SbnLegameAut.valueOf("815"));
        //Setto i valori del documento legato
        legameAut.setNoteLegame(rep_tit.getNOTA_REP_TIT());

        if (rep != null) {
            legameAut.setIdArrivo(rep.getCD_SIG_REPERTORIO());
            ElementAutType el = new ElementAutType();
            FormatoRepertorio fr = new FormatoRepertorio();
            el.setDatiElementoAut(fr.formattaRepertorioPerEsame(rep));
            legameAut.setElementoAutLegato(el);
        } else
            legameAut.setIdArrivo("");
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        if (relaz.getTP_RESPONSABILITA().equals("1"))
            return true;
        return false;
    }

    /**
     * Formatta i legami per un esame analitico, cercando nel DB
     * Quei documenti che hanno dei legami diversi devono ridefinire questo metodo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
   public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV)
       throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
       LegamiType[] legami = null;

       Titolo titDB = new Titolo();
       //legami con documenti -> viene fatto dall'esterno, credo
       //TitoloCerca titCerca = new TitoloCerca(tipoOut, tipoOrd, conn, this);
       //titCerca.formattaLegamiDocDoc(titolo);
       List legamiVec = new ArrayList();
       List vettore; // = titDB.cercaLegamiDocumento(titolo, tipoOrd);
       //Legami con repertori
       RepertorioTitoloUniforme repAut = new RepertorioTitoloUniforme();
       vettore = repAut.cercaCitazioniInRepertori(titolo.getBID(), tipoOrd);
       if (vettore.size() > 0) {
           LegamiType legamiType = new LegamiType();
           legamiType.setIdPartenza(titolo.getBID());
           for (int i = 0; i < vettore.size(); i++) {
               //Da sostituire con la vista Vl_repertorio_aut
               Tr_rep_tit rep_tit = (Tr_rep_tit) vettore.get(i);
               legamiType.addArrivoLegame(formattaLegameRepertorio(rep_tit));
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
               if (filtraLegameAutore(aut_tit)) {
                   legamiType.addArrivoLegame(formattaLegameAutoreEsameAnalitico(titolo, aut_tit, versione));
               }
           }
           if (legamiType.getArrivoLegameCount() > 0)
               legamiVec.add(legamiType);
       }
       legami = new LegamiType[legamiVec.size()];
       for (int i = 0; i < legami.length; i++) {
           legami[i] = (LegamiType) legamiVec.get(i);
       }
//
       if (gestisciNaturaV == true) // 27/05/16
       {

       // Legami con titoli uniformi almaviva7/almaviva4 23/05/2016
       // --------------------------------------------------------
    	   Vl_titolo_tit_c tit = new Vl_titolo_tit_c();
       tit.setBID_COLL(titolo.getBID());
       Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit);
       tavola.executeCustom("selectRinviiV" );
       List resp = tavola.getElencoRisultati(1);
       if (!resp.isEmpty())
       {
           LegamiType legamiType = new LegamiType();
           legamiType.setIdPartenza(titolo.getBID());

           ArrivoLegame arrLegame = new ArrivoLegame();
           LegameElementoAutType legameEl = new LegameElementoAutType();

           tit = (Vl_titolo_tit_c)resp.get(0);

           legameEl.setTipoAuthority(SbnAuthority.TU);
           legameEl.setTipoLegame(SbnLegameAut.valueOf("431"));
           legameEl.setIdArrivo(tit.getBID());

           ElementAutType elementoAutLegato = new ElementAutType();

           TitoloUniformeType datiEl;

           datiEl = new TitoloUniformeType();
           C231 c231 = new C231();
           c231.setA_231(tit.getISBD());
           datiEl.setT231(c231);

           datiEl.setTipoAuthority(SbnAuthority.valueOf("TU"));
           datiEl.setT001(tit.getBID());
           datiEl.setNaturaTU(tit.getCD_NATURA_BASE());

           datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tit.getCD_LIVELLO())));
           elementoAutLegato.setDatiElementoAut(datiEl);
           legameEl.setElementoAutLegato(elementoAutLegato);
           arrLegame.setLegameElementoAut(legameEl);
           legamiType.addArrivoLegame(arrLegame);

           if (legamiType.getArrivoLegameCount() > 0)
               legamiVec.add(legamiType);
           }
       } // end if gestisciNaturaV


       legami = new LegamiType[legamiVec.size()];
       for (int i = 0; i < legami.length; i++) {
           legami[i] = (LegamiType) legamiVec.get(i);
       }
//
       return legami;
   }


   public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo, BigDecimal versione)
   throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {

   	//return formattaLegamiPerEsameAnalitico(titolo, versione, CONSTANT.GESTISCI_NATURA_V_SI);
	   return formattaLegamiPerEsameAnalitico(titolo, versione, true);
   }

}
