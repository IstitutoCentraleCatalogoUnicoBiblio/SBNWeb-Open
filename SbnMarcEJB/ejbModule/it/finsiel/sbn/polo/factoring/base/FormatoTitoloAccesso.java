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
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C423;
import it.iccu.sbn.ejb.model.unimarcmodel.C454A;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe FormatoDocumentoModerno
 * <p>
 * Si occupa della formattazione di un documento di tipo moderno, che è analoga a quella
 * di un titolo, poichè è la più generica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-gen-03
 */
public class FormatoTitoloAccesso extends FormatoTitolo {

    /**
     * Constructor FormatoTitoloAccesso.
     * @param tipoOut
     * @param tipoOrd
     * @param conn
     * @param titolo
     */
    public FormatoTitoloAccesso(SbnTipoOutput tipoOut, String tipoOrd, Tb_titolo titolo) {

        this.tipoOrd = tipoOrd;
        this.tipoOut = tipoOut;
    }

    /**
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException    */
    public Object formattaTitolo(Tb_titolo titolo, BigDecimal versione) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            docChoice.setDatiTitAccesso(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
            TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
            //Un titolo di accesso ha i legami    ??????????
            LegamiType leg = titoloCerca.formattaLegamiDocDoc(titolo);
            if (leg.getArrivoLegameCount()>0)
                doc.addLegamiDocumento(leg);
        } else {
            // docChoice.setDatiDocumento(
            //formattaTitAccessoPerListaSintetica(titolo));
            return formattaTitAccessoPerListaSintetica(titolo);
        }
        doc.setDocumentoTypeChoice(docChoice);
        return doc;
    }
    public void aggiungiAltriLegami(Tb_titolo titolo,DocumentoType doc) throws EccezioneDB, EccezioneSbnDiagnostico {

    }

    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione) throws IllegalArgumentException, InvocationTargetException, Exception {
    	//return formattaElemento(titolo, versione, CONSTANT.GESTISCI_NATURA_V_SI);
    	return formattaElemento(titolo, versione, true);
    }

    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV) throws IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        // ATTENZIONE: QUESTO IF DEVE RIMANERE DIVERSO DALLA VERSIONE DI INDICE ALTRIMNTI NON FUNZIONA
		//if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) { //Analitico
		if ((tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) || (tipoOut.getType() == SbnTipoOutput.VALUE_3.getType()) ) { //Analitico
            docChoice.setDatiTitAccesso(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
        } else {
            // docChoice.setDatiDocumento(
            //formattaTitAccessoPerListaSintetica(titolo));
            return formattaTitAccessoPerListaSintetica(titolo);
        }
        doc.setDocumentoTypeChoice(docChoice);
        return doc;
    }



    /**
     * Method formattaDocumentoPerEsameAnalitico.
     * @param titolo
     * @return DatiDocType
     */
    private TitAccessoType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) {

        TitAccessoType datiDoc = new TitAccessoType();
        datiDoc.setT001(titolo.getBID());
        if(titolo.getFL_CONDIVISO() != null)
        	datiDoc.setCondiviso(TitAccessoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        SbnDatavar datevar = null;
        datevar = new SbnDatavar(titolo.getTS_VAR());
        datiDoc.setT005(datevar.getT005Date());
        //datiDoc.setT005(titolo.getTS_VAR().getDate());
        String natura = titolo.getCD_NATURA();
        TitAccessoTypeChoice choice = new TitAccessoTypeChoice();
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        if (natura.equals("B")) {

        // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
        	 C454A c454A = new C454A();
             c454A.setT200(isbd.getC200());
             c454A.setT101(formattaT101(titolo));
             if (c454A.getT101() == null) {
             	c454A.setT101(new C101());
             }
             choice.setT454A(c454A);
             // choice.setT454(isbd.getC200());

        } else if (natura.equals("P")) {
            choice.setT510(isbd.getC200());
        } else if (natura.equals("T")) {
            C423 c423 = new C423();
            //Altri codici
            //c423.set
            c423.setT200(isbd.getC200());
            c423.setT101(formattaT101(titolo));
            if (c423.getT101() == null)
                c423.setT101(new C101());
            choice.setT423(c423);
        } else if (natura.equals("D")) {
            choice.setT517(isbd.getC200());
        }
        datiDoc.setTitAccessoTypeChoice(choice);

        datiDoc.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(natura));
        if (titolo.getCD_AGENZIA() != null && titolo.getCD_AGENZIA().length() >= 2) {
            C801 c801 = new C801();
            c801.setA_801(titolo.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(titolo.getCD_AGENZIA().substring(2));
            datiDoc.setT801(c801);
        }
        return datiDoc;

    }

    /**
     * Formatta i legami per un esame analitico, cercando nel DB
     * Quei documenti che hanno dei legami diversi devono ridefinire questo metodo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami = null;

        Titolo titDB = new Titolo();
        //legami con documenti -> viene fatto dall'esterno, credo
        //TitoloCerca titCerca = new TitoloCerca(tipoOut, tipoOrd, this);
        //titCerca.formattaLegamiDocDoc(titolo);
        List legamiVec = new ArrayList();
        //Legami con autori
        Autore autDB = new Autore();
        List vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
                //if (filtraLegame(aut_tit.getCD_RELAZIONE(), "Autore"))
                legamiType.addArrivoLegame(formattaLegameAutore(aut_tit));

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

    /**
     * Prepara legame tra titolo e autore
     * nota sul tipo legame:
     *   nel legame con l'autore il tipoLegame dipende da tr_tit_aut.tp_respons e da
     *   tb_autore.tp_nome:
     *   se tipo nome è A,B,C,D tipoLegame è 700 se tp_respons=1, 701 se
     *   tp_respons=2, 702 se tp_respons=3 o altro
     *   se tipo nome è E,R,G tipoLegame è 710 se tp_respons=1, 711 se tp_respons=2,
     *   712 se tp_respons=3 o altro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public ArrivoLegame formattaLegameAutore(Vl_autore_tit relaz)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.valueOf("AU"));
        legameAut.setIdArrivo(relaz.getVID());
        legameAut.setRelatorCode(relaz.getCD_RELAZIONE());
        String tp_nome = relaz.getTP_NOME_AUT();
        String tp_respons = relaz.getTP_RESPONSABILITA();
        legameAut.setTipoRespons(SbnRespons.valueOf(tp_respons));
        if (tp_nome.equals("A") || tp_nome.equals("B") || tp_nome.equals("C") || tp_nome.equals("D")) {
            if (tp_respons.equals("1"))
                legameAut.setTipoLegame(SbnLegameAut.valueOf("700"));
            else if (tp_respons.equals("2"))
                legameAut.setTipoLegame(SbnLegameAut.valueOf("701"));
            else
                legameAut.setTipoLegame(SbnLegameAut.valueOf("702"));
        } else {
            if (tp_respons.equals("1"))
                legameAut.setTipoLegame(SbnLegameAut.valueOf("710"));
            else if (tp_respons.equals("2"))
                legameAut.setTipoLegame(SbnLegameAut.valueOf("711"));
            else
                legameAut.setTipoLegame(SbnLegameAut.valueOf("712"));
        }
        //relaz.getCD_RELAZIONE()));//Tp_responsabilita()));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoAutoreTitolo forAut = new FormatoAutoreTitolo(tipoOut, tipoOrd, user);
        legameAut.setElementoAutLegato(forAut.formattaAutore(relaz));
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }
    protected DocumentoType formattaTitAccessoPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoise = new DocumentoTypeChoice();
        //Copiato dall'analitico
        TitAccessoType datiDoc = new TitAccessoType();
        datiDoc.setT001(titolo.getBID());
        if(titolo.getFL_CONDIVISO() != null)
        	datiDoc.setCondiviso(TitAccessoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));
        datiDoc.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        if (visualizzaTimestamp()){
            SbnDatavar datevar = null;
            datevar = new SbnDatavar(titolo.getTS_VAR());
            datiDoc.setT005(datevar.getT005Date());

        }
        String natura = titolo.getCD_NATURA();
        TitAccessoTypeChoice choice = new TitAccessoTypeChoice();
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        if (natura.equals("B")) {
        	// Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
            C454A c454A = new C454A();
            c454A.setT200(isbd.getC200());
            c454A.setT101(formattaT101(titolo));
            if (c454A.getT101() == null) {
               	c454A.setT101(new C101());
            }
  	        choice.setT454A(c454A);
            // choice.setT454(isbd.getC200());

        } else if (natura.equals("P")) {
            choice.setT510(isbd.getC200());
        } else if (natura.equals("T")) {
            C423 c423 = new C423();
            //Altri codici
            //c423.set
            c423.setT200(isbd.getC200());
            c423.setT101(formattaT101(titolo));
            if (c423.getT101() == null)
                c423.setT101(new C101());
            choice.setT423(c423);
        } else if (natura.equals("D")) {
            choice.setT517(isbd.getC200());
        }
        datiDoc.setTitAccessoTypeChoice(choice);

        datiDoc.setNaturaTitAccesso(SbnNaturaTitAccesso.valueOf(natura));
        if (titolo.getCD_AGENZIA() != null && titolo.getCD_AGENZIA().length() >= 2) {
            C801 c801 = new C801();
            c801.setA_801(titolo.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(titolo.getCD_AGENZIA().substring(2));
            datiDoc.setT801(c801);
        } else {
            log.error("Campo obbligatorio mancante c801, in titolo : " + titolo.getBID());
        }
        docChoise.setDatiTitAccesso(datiDoc);
        doc.setDocumentoTypeChoice(docChoise);
        //settaLegamiDocumento(doc, titolo); //Devo settare i legami
        LegamiType[] lega = formattaLegamiPerListaSintetica(titolo);
        if (lega.length > 0)
            doc.setLegamiDocumento(lega);
        return doc;
        /*DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoise = new DocumentoTypeChoice();
        TitAccessoType titAccesso = new TitAccessoType();

        if (tipoOut.equals(tipoOut.VALUE_1)) {
            //DA riempire
        }
        titAccesso.setT001(titolo.getBID());
            if (visualizzaTimestamp())
        titAccesso.setT005(titolo.getTS_VAR().getDate());
        //titAccesso.setTipoMateriale(SbnMateriale.valueOf(titolo.getTP_MATERIALE()));
        titAccesso.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        C801 c801 = new C801();
        c801.setA_801(titolo.getCD_PAESE());
        c801.setB_801(titolo.getCD_AGENZIA());
        titAccesso.setT801(c801);

        docChoise.setDatiTitAccesso(titAccesso);
        doc.setDocumentoTypeChoice(docChoise);
        //settaLegamiDocumento(doc, titolo); Devo settare i legami
        return doc;*/
    }
    /** formatta i legami di un documento per lista sintetica, esegue una ricerca nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public LegamiType[] formattaLegamiPerListaSintetica(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami;
        Titolo titoloDB = new Titolo();
        List legamiVec = new ArrayList();
        //Legami di documenti
        /*TableDao documenti = titoloDB.cercaLegamiDocumentoNonTitUni(titolo, tipoOrd);
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
        }*/
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
    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        if (relaz.getTP_RESPONSABILITA().equals("1"))
                return true; //Altri autori
        return false;
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
        legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameEl.setTipoRespons(SbnRespons.valueOf(tp_respons));

        legameEl.setElementoAutLegato(formattaAutoreLegatoSintetica(relaz));

        arrLegame.setLegameElementoAut(legameEl);
        return arrLegame;
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

        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        datiEl.setT001(autore.getVID());
        eleAut.setDatiElementoAut(datiEl);
        return eleAut;
    }

}
