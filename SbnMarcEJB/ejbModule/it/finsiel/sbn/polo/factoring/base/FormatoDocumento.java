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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.Comuni;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.Link_multim;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Nota;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_nota;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_1;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.Ts_link_multim;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_biblioteca_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_classe_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C105bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C110;
import it.iccu.sbn.ejb.model.unimarcmodel.C125bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C140bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C181;
import it.iccu.sbn.ejb.model.unimarcmodel.C182;
import it.iccu.sbn.ejb.model.unimarcmodel.C183;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C321;
import it.iccu.sbn.ejb.model.unimarcmodel.C3XX;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndicatorePubblicato;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LivelloBibliografico;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoNota321;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoSeriale;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
public abstract class FormatoDocumento extends FormatoTitolo {

    //Memorizzo i legami di base, con titoli e autori
    protected boolean legame_461 = false;
    protected boolean contieneAutore1 = false;

    public static FormatoTitolo getInstance(
        SbnTipoOutput tipoOut,
        String tipoOrd,
        SbnUserType user,
        Tb_titolo titolo) {
        FormatoDocumento formato = null;

        //Verifico prima i diritti ?
     // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

        if (specializzaMateriale(titolo.getTP_MATERIALE(), user)) {
            if (titolo.getTP_MATERIALE().equals("G")) { //Materiale grafico
                formato = new FormatoDocumentoGrafico();
            } else if (titolo.getTP_MATERIALE().equals("C")) { //Cartografico
                formato = new FormatoDocumentoCartografico();
            } else if (titolo.getTP_MATERIALE().equals("U")) { //Musica
                formato = new FormatoDocumentoMusica();
            } else if (titolo.getTP_MATERIALE().equals("E")) { //Antico
                formato = new FormatoDocumentoAntico();
            } else if (titolo.getTP_MATERIALE().equals("H")) { //Audiovisivo
                formato = new FormatoDocumentoAudiovisivo();
            } else if (titolo.getTP_MATERIALE().equals("L")) { // Elettronico
                formato = new FormatoDocumentoElettronico();

            } else { ///Moderno è quello di default
                formato = new FormatoDocumentoModerno();
            }
        } else {
        	// intervento settembre 2015: nel caso di materiale H e non si è abilitati si potrebbe essere abilitati
        	// alla Musica quindi si deve effettuare anche questo controllo
        	  if (titolo.getTP_MATERIALE().equals("H")) {
        		  if (specializzaMateriale("U", user)) {
        			  formato = new FormatoDocumentoMusica();
        		  } else {
            		  formato = new FormatoDocumentoModerno();
            	  }
        	  } else {
        		  formato = new FormatoDocumentoModerno();
        	  }

        	  // formato = new FormatoDocumentoModerno();
        }


        formato.tipoOrd = tipoOrd;
        formato.tipoOut = tipoOut;
        return formato;
    }

    public Object formattaTitolo(Tb_titolo titolo, BigDecimal versione) throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception  {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            docChoice.setDatiDocumento(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
            TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
            LegamiType leg = titoloCerca.formattaLegamiDocDoc(titolo);
            //Se ci sono legami li aggiungo? Si, non di altri documenti.
            if (leg.getArrivoLegameCount() > 0)
                doc.addLegamiDocumento(0,leg);
            leg = formattaLegamiAutorePerEsameAnalitico(titolo);
            if (leg.getArrivoLegameCount() > 0)
                doc.addLegamiDocumento(0,leg);
        } else {
            docChoice.setDatiDocumento(formattaDocumentoPerListaSintetica(titolo));
            inserisciLegamePerWoN(doc, titolo, versione);
            LegamiType[] lega = formattaLegamiPerListaSintetica(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
        }
        doc.setDocumentoTypeChoice(docChoice);
        return doc;
    }

    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            docChoice.setDatiDocumento(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType leg = formattaLegamiAutorePerEsameAnalitico(titolo);
            if (leg.getArrivoLegameCount() > 0)
                doc.addLegamiDocumento(leg);
        } else {
            docChoice.setDatiDocumento(formattaDocumentoPerListaSintetica(titolo));
            inserisciLegamePerWoN(doc, titolo, versione);
            LegamiType[] lega = formattaLegamiPerListaSintetica(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
        }
        doc.setDocumentoTypeChoice(docChoice);
        return doc;
    }


    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV)
    	throws IllegalArgumentException, InvocationTargetException, Exception {
        DocumentoType doc = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        // ATTENZIONE: QUESTO IF DEVE RIMANERE DIVERSO DALLA VERSIONE DI INDICE ALTRIMNTI NON FUNZIONA
		//if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) { //Analitico
		if ((tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) || (tipoOut.getType() == SbnTipoOutput.VALUE_3.getType()) ) { //Analitico
            docChoice.setDatiDocumento(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType leg = formattaLegamiAutorePerEsameAnalitico(titolo);
            if (leg.getArrivoLegameCount() > 0)
                doc.addLegamiDocumento(leg);
        } else {
            docChoice.setDatiDocumento(formattaDocumentoPerListaSintetica(titolo));
            inserisciLegamePerWoN(doc, titolo, versione);
            LegamiType[] lega = formattaLegamiPerListaSintetica(titolo);
            if (lega.length > 0)
                doc.setLegamiDocumento(lega);
        }
        doc.setDocumentoTypeChoice(docChoice);
        return doc;
    }





    public void aggiungiAltriLegami(Tb_titolo titolo,DocumentoType doc) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
            for(int i = 0;i<lega.length; i++) {
                doc.addLegamiDocumento(lega[i]);
            }
        }
    }

    /** Formatta i legami di lista sintetica, da ridefinire per chi ha legami diversi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public LegamiType[] formattaLegamiPerListaSintetica(Tb_titolo titolo) throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception {
        return new LegamiType[0];
    }
    /** I titoli di natura W o N devono sempre avere il legame con la monografia
     * superiore.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisciLegamePerWoN(DocumentoType doc, Tb_titolo titolo, BigDecimal versione) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (titolo.getCD_NATURA().equals("W") || titolo.getCD_NATURA().equals("N")) {
            Titolo titoloDB = new Titolo();

            LegamiType leg = new LegamiType();
            Vl_titolo_tit_b mon_sup = titoloDB.cercaMonografiaSuperiore(titolo.getBID());
            if (mon_sup == null) {
                log.error("Il titolo "+titolo.getBID()+" di natura "+titolo.getCD_NATURA()+
                " non possiede monografia superiore");
                return;
            }
            ArrivoLegame arrLeg =
                formattaLegameDocumentoListaSintetica(mon_sup);
            leg.addArrivoLegame(arrLeg);
            leg.setIdPartenza(titolo.getBID());
            doc.addLegamiDocumento(leg);
        }
    }

    public abstract DocumentoType formattaDocumentoLegatoListaSintetica(Tb_titolo titolo) throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception;
    public abstract ArrivoLegame formattaLegameDocumentoListaSintetica(Vl_titolo_tit_b titolo)
        throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception;
    public abstract DatiDocType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws EccezioneDB, IllegalArgumentException, InvocationTargetException, Exception;
    public abstract DatiDocType formattaDocumentoPerListaSintetica(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, IllegalArgumentException, InvocationTargetException, Exception;

    /**
     *  Tabella di conversione SBN-Unimarc.
     * Natura SBN  Livello bibliog. Unimarc    Regola di simmetria Note
     * M           m         Indicatore 1 del tag 200 =1     Il titolo è significativo
     * W           m         Indicatore 1 del tag 200 =0     Il titolo non è significativo
     * N           a         conversione stretta     Titolo analitico.
     * S           s         tipo seriale nel tag 110$a <> 'b'    Se manca l’indicazione di tipo
     * seriale = ‘b’ viene assunta natura ‘S’
     * C           s         tipo seriale nel tag 110$a = 'b'
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void formattaGuida(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        GuidaDoc guida = new GuidaDoc();
        String natura = titolo.getCD_NATURA();
     // Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
        if (natura.equals("M")) {
            guida.setLivelloBibliografico(LivelloBibliografico.M);
        } else if (natura.equals("W")) {
            guida.setLivelloBibliografico(LivelloBibliografico.M);
        } else if (natura.equals("N")) {
            guida.setLivelloBibliografico(LivelloBibliografico.A);
        } else if (natura.equals("S")) {
            guida.setLivelloBibliografico(LivelloBibliografico.S);
        } else if (natura.equals("C")) {
            guida.setLivelloBibliografico(LivelloBibliografico.S);
        } else if (natura.equals("R")) {
            guida.setLivelloBibliografico(LivelloBibliografico.S);
        }
        if (titolo.getTP_RECORD_UNI() != null && !titolo.getTP_RECORD_UNI().equals(" ")) {
            String rec = Decodificatore.getCd_unimarc("Tb_titolo","tp_record_uni",titolo.getTP_RECORD_UNI());
            if (rec != null)
                guida.setTipoRecord(TipoRecord.valueOf(rec));
            else {
                log.warn("Titolo: "+titolo.getBID() + " ha tp_record_uni errato :"+titolo.getTP_RECORD_UNI());
            }
        }
        datiDoc.setGuida(guida);

        //almaviva5_20090921 carico infoloc solo per esame analitico
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType() ) //Analitica
        	formattaLocalizzazione(datiDoc, titolo);
    }



    protected void formattaLocalizzazione(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
    	// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
        String natura = titolo.getCD_NATURA();
        if ((natura.equals("M")) || (natura.equals("S")) ||
            (natura.equals("W")) || (natura.equals("C")) ||
            (natura.equals("N")) || (natura.equals("R"))) {
            if (titolo instanceof Tb_titolo) {
                String bid = titolo.getBID();
                String codicePolo = user.getBiblioteca().substring(0,3);
                String codiceBiblioteca = user.getBiblioteca().substring(3,6);
                TitoloGestisceLocalizza gestisceLocalizzaTitolo = new TitoloGestisceLocalizza();
                List infoBib = gestisceLocalizzaTitolo.cercaLocalizzazioniTitolo(bid,codicePolo,codiceBiblioteca);
                int size = infoBib.size();
				for (int i = 0; i < size; i++) {
                    Vl_biblioteca_tit localizzazione =(Vl_biblioteca_tit)infoBib.get(i);
                    log.debug("Trovata localizzazione: " + localizzazione);
                    String localizz = "";
                    // if (localizzazione.getFL_GESTIONE().equalsIgnoreCase("S"))
                    localizz = "-" + localizzazione.getFL_GESTIONE() + "-" + localizzazione.getFL_POSSESSO();
                    datiDoc.addInfoLocBib(i,localizzazione.getCod_polo()+localizzazione.getCd_biblioteca() + localizz);
                }
            }
        }
    }

    protected void formattaLocalizzazioneOld(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        String natura = titolo.getCD_NATURA();
        if ((natura.equals("M")) || (natura.equals("S")) ||
            (natura.equals("W")) || (natura.equals("C")) ||
            (natura.equals("N")) ){
            if(titolo instanceof Tb_titolo){
                titolo.getBID();
                user.getBiblioteca();
                Object[] objLocalizz =  titolo.getTR_TIT_BIB().toArray();
                Iterator iterlocalizz = titolo.getTR_TIT_BIB().iterator();
                int k=0;
                while(iterlocalizz.hasNext()){
                    Tr_tit_bib localizzazione =(Tr_tit_bib)objLocalizz[k];
                    log.debug("Trovata localizzazione: " + localizzazione);
                    datiDoc.addInfoLocBib(k,localizzazione.getCD_POLO()+localizzazione.getCD_BIBLIOTECA());
                    iterlocalizz.next();
                }
            }
        }
    }
    protected void formattaT110(DatiDocType datiDoc, Tb_titolo titolo) {
        if (titolo.getCD_NATURA().equals("C")) {
            C110 c110 = new C110();
            c110.setA_110_0(TipoSeriale.valueOf("b"));
            datiDoc.setT110(c110);
        } else if (titolo.getCD_NATURA().equals("S")) {
            C110 c110 = new C110();
            c110.setA_110_0(TipoSeriale.valueOf("a"));
            datiDoc.setT110(c110);
        }
    }

    protected void formattaT101(DatiDocType datiDoc, Tb_titolo titolo) {
        datiDoc.setT101(formattaT101(titolo));
    }

    protected void formattaDocumentoBase(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {

        if(titolo.getFL_CONDIVISO() != null)
        	datiDoc.setCondiviso(DatiDocTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));
        datiDoc.setT001(titolo.getBID());
        datiDoc.setTipoMateriale(SbnMateriale.valueOf(titolo.getTP_MATERIALE()));
        datiDoc.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        formattaGuida(datiDoc, titolo);
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType() || visualizzaTimestamp()) {
            SbnDatavar datevar = new SbnDatavar(titolo.getTS_VAR());
            datiDoc.setT005(datevar.getT005Date());
        }
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            formattaNote(datiDoc,titolo);
            //Che famo, lo mettiamo qua ?
            formatta856(datiDoc,titolo);
        }
        datiDoc.setNaturaDoc(SbnNaturaDocumento.valueOf(titolo.getCD_NATURA()));
        formattaNumeriStandard(titolo,datiDoc);
    }

    protected void formatta856(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Link_multim lm = new Link_multim();

        List links = lm.cercaLinkMultim(titolo.getBID());
        int size = links.size();
		if (size > 0) {
            C856[] c856 = new C856[size];
            for (int i = 0; i < size; i++) {
                Ts_link_multim link = (Ts_link_multim)links.get(i);
                c856[i] = new C856();
                c856[i].setU_856(link.getURI_MULTIM());
                c856[i].setC9_856_1(lm.estraiImmagine(link));
            }
            datiDoc.setT856(c856);
        }
    }

    /** Crea un oggetto isbd da cui estrarre le informaizoni di interesse */
    public Isbd creaIsbd(Tb_titolo titolo,DatiDocType datiDoc) {
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        datiDoc.setT200(isbd.getC200());
        datiDoc.setT205(isbd.getC205());
        datiDoc.setT206(isbd.getC206());
        datiDoc.setT207(isbd.getC207());
        datiDoc.setT208(isbd.getC208());
        // Manutenzione almaviva2 Aprile 2018 in seguito a Modifica del Protocollo di Indice
        if ((datiDoc.getT210() != null)  && (datiDoc.getT210().length > 0))
        	isbd.getC210()[0].setId2(datiDoc.getT210()[0].getId2());

        datiDoc.setT210(isbd.getC210());
        datiDoc.setT215(isbd.getC215());
        return isbd;
    }

    public Isbd creaIsbd(Tb_titolo titolo) {
        Isbd isbd = new Isbd();
        isbd.ricostruisciISBD(titolo);
        return isbd;
    }

    /** formatta array di numeri standard, forse va in FormatoTitolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void formattaNumeriStandard(Tb_titolo titolo, DatiDocType datiDoc) throws IllegalArgumentException, InvocationTargetException, Exception {
        NumeroStd numeroStd = new NumeroStd();
        List v = numeroStd.cercaNumeroPerBid(titolo.getBID());
        int size = v.size();
		for (int i = 0; i < size; i++) {
            NumStdType num = formattaNumeroStandard((Tb_numero_std) v.get(i));
            // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
            if (num != null &&
//            		(schema_version.compareTo(new BigDecimal(1.09)) > 0 || num.getTipoSTD().getType() != SbnTipoSTD.VALUE_13.getType()))
            		(schema_version.compareTo(new BigDecimal(1.09)) > 0
            				|| !num.getTipoSTD().equals("13")))
                datiDoc.addNumSTD(num);
        }
    }

    /**
     * formatta un numero standard di tipo NumStdType
     * @param tb_numero_std
     * @return NumStdType
     */
    private NumStdType formattaNumeroStandard(Tb_numero_std numero_std) {
        NumStdType numStd = new NumStdType();
        numStd.setNotaSTD(numero_std.getNOTA_NUMERO_STD());
        String tipo = Decodificatore.getCd_unimarc("Tb_numero_std", "tp_numero_std", numero_std.getTP_NUMERO_STD().trim());
        if (tipo == null)
            return null;

        // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
//        numStd.setTipoSTD(SbnTipoSTD.valueOf(tipo));
        numStd.setTipoSTD(tipo);
        if (numero_std.getCD_PAESE() != null && numero_std.getCD_PAESE().trim().length()>0) {
            String paese=Decodificatore.getCd_unimarc("PAES",numero_std.getCD_PAESE());
            numStd.setPaeseSTD(paese==null?numero_std.getCD_PAESE():paese);
        }
        numStd.setNumeroSTD(numero_std.getNUMERO_STD().trim());
        if (numStd.getNumeroSTD() == null) {
            return null;
        }
        return numStd;
    }

    /** formatta titolo di accesso per legame * /
    public DocumentoType formattaTitoloAccessoLegato(Tb_titolo titolo)
    	throws EccezioneDB, EccezioneSbnDiagnostico {
    	//Utilizzo direttamente il formato titolo accessso.
    	FormatoTitoloAccesso formato =
    		new FormatoTitoloAccesso(tipoOut, tipoOrd, titolo);
    	return (DocumentoType) formato.formattaTitolo(titolo);
    }

    /**
     * Prepara legame tra titolo e soggetto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameSoggetto(Vl_soggetto_tit legame) throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.SO);
        legameAut.setIdArrivo(legame.getCID());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("606"));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoSoggetto fs = new FormatoSoggetto();
        ElementAutType el = new ElementAutType();
//        int num_tit_coll_bib = fs.cercaNum_tit_coll_bib(relaz.getCID(),user);
//        int num_tit_coll = fs.cercaNum_tit_coll(relaz.getCID());
//
//        el.setDatiElementoAut(fs.formattaSoggettoPerEsame(relaz,num_tit_coll_bib,num_tit_coll));
        // nel legame della linea titolo non interessa avere i contatori associati
    	Descrittore d = new Descrittore();
    	List dd = d.cercaDescrittorePerSoggetto(legame.getCID());
		el.setDatiElementoAut(fs.formattaSoggettoPerEsame(legame, 0, 0, dd));
        legameAut.setElementoAutLegato(el);
        arrLegame.setLegameElementoAut(legameAut);
        //almaviva5_20090302
        legameAut.setNoteLegame(legame.getNOTA_TIT_SOG_BIB());
        //almaviva5_20121003 evolutive CFI
        legameAut.setRank(Math.max(1, legame.getPOSIZIONE()) );

        return arrLegame;
    }

    public ArrivoLegame formattaLegameThesauro(Vl_thesauro_tit relaz) throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.TH);
        legameAut.setIdArrivo(relaz.getDID());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("606"));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoTermineThesauro fs = new FormatoTermineThesauro();
        ElementAutType el = new ElementAutType();
//        int num_tit_coll_bib = fs.cercaNum_tit_coll_bib(relaz.getCID(),user);
//        int num_tit_coll = fs.cercaNum_tit_coll(relaz.getCID());
//
//        el.setDatiElementoAut(fs.formattaSoggettoPerEsame(relaz,num_tit_coll_bib,num_tit_coll));
        // nel legame della linea titolo non interessa avere i contatori associati
        el.setDatiElementoAut(fs.formattaThesauroPerEsame(relaz,0,0));
        legameAut.setElementoAutLegato(el);
        arrLegame.setLegameElementoAut(legameAut);
        //almaviva5_20090302
        legameAut.setNoteLegame(relaz.getNOTA_TERMINE_TITOLI_BIBLIOTECA());

        fs.formattaLegameThesauro(el);

        return arrLegame;
    }

    /**
     * Prepara legame tra titolo e luogo
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameLuogo(Vl_luogo_tit relaz) throws IllegalArgumentException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.LU);
        legameAut.setIdArrivo(relaz.getLID());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("620"));
        //12.7.2005 inserito il tipo luogo in relatorCode
        if (relaz.getTP_LUOGO().trim().length() > 0)
        	legameAut.setRelatorCode(relaz.getTP_LUOGO());
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoLuogo fl = new FormatoLuogo(tipoOut,tipoOrd);
        fl.formattaLuogo(relaz);
        ElementAutType el = new ElementAutType();
        el.setDatiElementoAut(fl.formattaLuogo(relaz).getDatiElementoAut());
        el = fl.formattaRinviiLuogoPerTitolo(el, relaz);
        legameAut.setElementoAutLegato(el);
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    /**
     * Prepara legame tra titolo e classe
     * @throws EccezioneSbnDiagnostico
     */
    public ArrivoLegame formattaLegameClasse(Vl_classe_tit relaz) throws EccezioneSbnDiagnostico {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.CL);
        arrLegame.setLegameElementoAut(legameAut);

        //almaviva5_20090414
        if (relaz.getCD_SISTEMA().substring(0,1).equalsIgnoreCase("D") &&
        		!relaz.getCD_EDIZIONE().equals("  "))
            legameAut.setTipoLegame(SbnLegameAut.valueOf("676"));
        else
            legameAut.setTipoLegame(SbnLegameAut.valueOf("686"));

        FormatoClasse fc = new FormatoClasse();
        ElementAutType el = new ElementAutType();
        el.setDatiElementoAut(fc.creaClassePerEsameAnalitico(relaz, 0, 0));
        legameAut.setIdArrivo(el.getDatiElementoAut().getT001());
        legameAut.setElementoAutLegato(el);
        arrLegame.setLegameElementoAut(legameAut);

        //almaviva5_20181022 #6749
        legameAut.setNoteLegame(relaz.getNOTA_TIT_CLA());

        return arrLegame;
    }

    /**
     * Prepara legame tra titolo e luogo
     * Da togliere alcune eccezioni, che non vengono generate (rimane solo
     * EccezioneDB)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameMarca(Vl_marca_tit relaz) throws IllegalArgumentException, InvocationTargetException, Exception{
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();

        legameAut.setTipoAuthority(SbnAuthority.MA);
        legameAut.setIdArrivo(relaz.getMID());
        legameAut.setNoteLegame(relaz.getNOTA_TIT_MAR());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("921"));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoMarca formatoMarca = new FormatoMarca(tipoOut, tipoOrd, user);

        legameAut.setElementoAutLegato(formatoMarca.formattaMarca(relaz));

        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
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
        if(relaz.getFL_CONDIVISO_LEGAME() != null)
        	legameAut.setCondiviso(LegameElementoAutTypeCondivisoType.valueOf(relaz.getFL_CONDIVISO_LEGAME()));
        legameAut.setIdArrivo(relaz.getVID());
        if (!relaz.getCD_RELAZIONE().trim().equals(""))
            legameAut.setRelatorCode(relaz.getCD_RELAZIONE());
        legameAut.setNoteLegame(relaz.getNOTA_TIT_AUT());
        if (!relaz.getFL_INCERTO().equals(" "))
            legameAut.setIncerto(SbnIndicatore.valueOf(relaz.getFL_INCERTO()));
        if (!relaz.getFL_SUPERFLUO().equals(" "))
            legameAut.setSuperfluo(SbnIndicatore.valueOf(relaz.getFL_SUPERFLUO()));

        // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
        if (relaz.getCD_STRUMENTO_MUS() != null) {
        	if (!relaz.getCD_STRUMENTO_MUS().trim().equals(""))
        		legameAut.setStrumento(Decodificatore.getCd_unimarc("ORGA", relaz.getCD_STRUMENTO_MUS()));
        }

        String tp_nome = relaz.getTP_NOME_AUT();
        String tp_respons = relaz.getTP_RESPONSABILITA();
        legameAut.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameAut.setTipoRespons(SbnRespons.valueOf(tp_respons));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoAutoreTitolo forAut = new FormatoAutoreTitolo(tipoOut, tipoOrd, user);
        //ElementAutType el = new ElementAutType();
        //el.setDatiElementoAut(forAut.formattaAutore(relaz));
        //el = forAut.formattaRinvioAutorePerTitolo(el, relaz);
        legameAut.setElementoAutLegato(forAut.formattaAutore(relaz));
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    /** formatta legami con titoli accesso
     * Nota: in legameTitAccesso il tipoLegame dipende dalla natura del titolo di
     * accesso:
     *   se tb_titolo=P tipoLegame=510, se D -->517, se T-->423, se B-->454
     *   inoltre devi usare gli elementi del titolo di accesso (nel caso in esame
     * la T454 e non la struttura di datiDocumento
     * * /
    public ArrivoLegame formattaLegameTitoloAccesso(Vl_titolo_tit_b relaz)
    	throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
    	ArrivoLegame arrLegame = new ArrivoLegame();
    	LegameTitAccessoType legameEl = new LegameTitAccessoType();
    	//Setto i valori del legame
    	legameEl.setIdArrivo(relaz.getBID());
    	String cd_natura = relaz.getCD_NATURA();
    	if (cd_natura.equals("P"))
    		legameEl.setTipoLegame(SbnLegameTitAccesso.valueOf("510"));
    	if (cd_natura.equals("D"))
    		legameEl.setTipoLegame(SbnLegameTitAccesso.valueOf("517"));
    	if (cd_natura.equals("T"))
    		legameEl.setTipoLegame(SbnLegameTitAccesso.valueOf("423"));
    	if (cd_natura.equals("B"))
    		legameEl.setTipoLegame(SbnLegameTitAccesso.valueOf("454"));
    	legameEl.setNoteLegame(relaz.getNota_tit_tit());
    	legameEl.setTitAccessoLegato(formattaTitoloAccessoLegato(relaz));
    	//////////////////////// Decod, sotto
    	//Decodificatore.getCDunimarc("tr_tit_tit","tp_legame",relaz.getCD_RELAZIONE());

    	arrLegame.setLegameTitAccesso(legameEl);

    	return arrLegame;
    }

    /**
     * Formatta i legami per un esame analitico, cercando nel DB
     * Quei documenti che hanno dei legami diversi devono ridefinire questo metodo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     */
    public LegamiType formattaLegamiAutorePerEsameAnalitico(Tb_titolo titolo)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType legamiType = new LegamiType();

        //Legami con autori
        Autore autDB = new Autore();
        List vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
        int size = vettore.size();
		if (size > 0) {
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < size; i++) {
                Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
                if (filtraLegameAutore(aut_tit)) {
                    legamiType.addArrivoLegame(formattaLegameAutore(aut_tit));
                }
            }
        }
        return legamiType;
    }

    public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType[] legami;

        //Titolo titDB = new Titolo();
        //legami con documenti -> viene fatto dall'esterno, credo
        //TitoloCerca titCerca = new TitoloCerca(tipoOut, tipoOrd, this);
        //titCerca.formattaLegamiDocDoc(titolo);
        List legamiVec = new ArrayList();
        List vettore; // = titDB.cercaLegamiDocumento(titolo, tipoOrd);
        /*
        //Legami con titoli-accesso
        vettore = titDB.cercaLegamiTitoloAccesso(titolo, tipoOrd);
        if (vettore.size() > 0) {
        	LegamiType legamiType = new LegamiType();
        	legamiType.setIdPartenza(titolo.getBID());
        	for (int i = 0; i < vettore.size(); i++) {
        		Vl_titolo_tit_b tit_tit = (Vl_titolo_tit_b) vettore.get(i);
        		if (filtraLegame(tit_tit.getTP_LEGAME(), "TitoloAccesso")) {
        			legamiType.addArrivoLegame(
        				formattaLegameTitoloAccesso(tit_tit));
        		}
        	}
            if (legamiType.getArrivoLegameCount()>0)
        	legamiVec.add(legamiType);
        }
        */
        //Legami con luoghi
        Luogo luo = new Luogo();
        TableDao tavola = luo.cercaLuogoPerTitolo(titolo.getBID(), null);
        vettore = tavola.getElencoRisultati();

        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_luogo_tit tit_luo = (Vl_luogo_tit) vettore.get(i);
                if (filtraLegame("LU")) {
                    legamiType.addArrivoLegame(formattaLegameLuogo(tit_luo));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        //Legami con marche
        Marca aut = new Marca();
        tavola = aut.cercaMarcaPerTitolo(titolo.getBID(), null);
        vettore = tavola.getElencoRisultati();

        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_marca_tit tit_tit = (Vl_marca_tit) vettore.get(i);
                if (filtraLegame("MA")) {
                    //tit_tit.getCD_RELAZIONE(), "Marca")) {
                    legamiType.addArrivoLegame(formattaLegameMarca(tit_tit));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        //Legami con soggetti
        Soggetto sogg = new Soggetto();
        vettore = sogg.cercaSoggettoPerTitolo(titolo.getBID(), "order_" + SbnTipoOrd.VALUE_0.toString());
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_soggetto_tit tit_tit = (Vl_soggetto_tit) vettore.get(i);
                if (filtraLegame("SO")) {
                    //tit_tit.getTP_LEGAME(),"Soggetto")) { //??
                    legamiType.addArrivoLegame(formattaLegameSoggetto(tit_tit));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        //Legami con Thesauri

        TermineThesauro thes = new TermineThesauro();
        vettore = thes.cercaThesauroPerTitolo(titolo.getBID(), null);
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_thesauro_tit the_tit = (Vl_thesauro_tit) vettore.get(i);
                if (filtraLegame("TH")) {
                    //tit_tit.getTP_LEGAME(),"Soggetto")) { //??
                    legamiType.addArrivoLegame(formattaLegameThesauro(the_tit));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        // Legami con classi
        Classe classe = new Classe();
        tavola = classe.cercaClassePerTitolo(titolo.getBID(), null);
        vettore = tavola.getElencoRisultati();

        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            //Legami con classi
            for (int i = 0; i < vettore.size(); i++) {
                Vl_classe_tit tit_tit = (Vl_classe_tit) vettore.get(i);
                if (filtraLegame("CL")) {
                    //tit_tit.getTP_LEGAME(),"Classe")) { //??
                    legamiType.addArrivoLegame(formattaLegameClasse(tit_tit));
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

    /** Aggiunge le note al DatiDocType
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void formattaNote(DatiDocType datiDoc, Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {

    	// Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
    	// al link dei documenti su Basi Esterne - Link verso base date digital
        Nota notaDB = new Nota();
        List v = notaDB.cercaPerBid(titolo.getBID());
        Tb_nota nota;
        C3XX c3;
        C321 c321;
        for (int i = 0; i < v.size(); i++) {
            nota = (Tb_nota) v.get(i);
            c3 = new C3XX();
            c3.setA_3XX(nota.getDS_NOTA());
            String appoTipoNota = Decodificatore.getCd_unimarc("Tb_nota", "tp_nota", nota.getTP_NOTA());
//            c3.setTipoNota(
//                SbnTipoNota.valueOf(Decodificatore.getCd_unimarc("Tb_nota", "tp_nota", nota.getTP_NOTA())));
            // datiDoc.addT3XX(c3);
            // Bug esercizio 6180 - Maggio 2016 almaviva2
            // viene eliminato il controllo sulla enumeration della SbnTipoNota che è stata cambiata dopo l'inserimento
            // della nota 321 e quindi non coincide più (comportava l'impossibilità di vedere l'analitica titolo dei documenti
            // in cui era inserita la nota al cast

            if (appoTipoNota.equals("321")) {
            //if (c3.getTipoNota().getType() == SbnTipoNota.VALUE_1_TYPE) {

            	c321 = new C321();
            	String [] nota321 = nota.getDS_NOTA().split("##", -1);	// #8199 lettura note con elementi vuoti
//                if (nota321[0].equals("I")) { //nota inserita
	                if (nota321[1].equals("DB")) {
		                String link = Decodificatore.getCd_tabellaDaDescrizione("LINK", nota321[2]);
		                c321.setA_321(link);
		                if (nota321[4].length() > 0)
		                	c321.setC_321(nota321[4]);
		                c321.setU_321(nota321[5]);
		                c321.setTipoNota(TipoNota321.DATABASE);
	                }
	                if (nota321[1].equals("REP")) {
	                	c321.setA_321(nota321[2]);
		                if (nota321[3].length() > 0)
		                	c321.setB_321(nota321[3]);
		                if (nota321[4].length() > 0)
		                	c321.setC_321(nota321[4]);
		                c321.setTipoNota(TipoNota321.REPERTORIO);
	                }
	                datiDoc.addT321(c321);
//                }

            } else {
            	 c3.setTipoNota(
                         SbnTipoNota.valueOf(Decodificatore.getCd_unimarc("Tb_nota", "tp_nota", nota.getTP_NOTA())));
                c3.setA_3XX(nota.getDS_NOTA());
                datiDoc.addT3XX(c3);
            }
        }

    }

    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
    public void formattaDatiComuni1(DatiDocType datiDoc, Tb_titolo titolo) throws EccezioneDB, InfrastructureException {

        Comuni comuni = new Comuni();
        Tb_titset_1 titset = comuni.cercaPerId(titolo.getBID());
        if (titset != null) {
        	// almaviva7 07/07/14
        	// Gestione tipo materiale letterario in base a data (antico o moderno)
			if(titolo.getAA_PUBB_1() != null){
//				String data = titolo.getAA_PUBB_1().getAnnoDate();
				String data = titolo.getAA_PUBB_1();
				data.replace('.', '0'); // Lower limit
				if (data.compareTo("1831") < 0)
				{
					if (titset.getS140_TP_TESTO_LETTERARIO()!= null)
					{
					// antico
					C140bis c140bis = new C140bis();
					c140bis.setA_140_17(Decodificatore.getCd_unimarc("COLA", titset.getS140_TP_TESTO_LETTERARIO()));
			        if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
			        	datiDoc.setT140bis(c140bis);

					}
				}
				else
				{
					if (titset.getS105_TP_TESTO_LETTERARIO() != null)
					{
					// Moderno
					C105bis c105bis = new C105bis();
					c105bis.setA_105_11(Decodificatore.getCd_unimarc("COLM", titset.getS105_TP_TESTO_LETTERARIO()));
			        if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
			        	datiDoc.setT105bis(c105bis);
					}
				}
			}
			 // 07/07/2014 in mancanza di data 1 si testa il tp_materiale se Antico si indirizza in antico
			else if(titolo.getTP_MATERIALE().equals("E")) {
				if (titset.getS140_TP_TESTO_LETTERARIO() != null)
				{
				// Antico
				C140bis c140bis = new C140bis();
				c140bis.setA_140_17(Decodificatore.getCd_unimarc("COLA", titset.getS140_TP_TESTO_LETTERARIO()));
			    if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
			    	datiDoc.setT140bis(c140bis);
				}
			}else{
				// 07/07/2014 Se tipo materiale è moderno si mette moderno altrimenti si mette moderno in tutti glia altri casi( anche se è presente
				// un'impronta che indichi che è antico ( Direttive Franco))

				if (titset.getS105_TP_TESTO_LETTERARIO() != null)
				{
				// Moderno
				C105bis c105bis = new C105bis();
				c105bis.setA_105_11(Decodificatore.getCd_unimarc("COLM", titset.getS105_TP_TESTO_LETTERARIO()));
			    if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
			    	datiDoc.setT105bis(c105bis);
				}
			}
			// End almaviva7 07/07/14


			// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
			// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
			if (titset.getS125_INDICATORE_TESTO() != null) {
				C125bis c125bis = new C125bis();
				c125bis.setB_125(Decodificatore.getCd_unimarc("INDT", titset.getS125_INDICATORE_TESTO()));
				if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
					datiDoc.setT125bis(c125bis);
			}

			 // Inizio Modifica Agosto 2015 per evitare la creazion di una istanza vuota della C182 (ripresa da indice)
			 C181[] c181;
		       if (titset.getS181_TP_FORMA_CONTENUTO_2() == null)
		        	c181 = new C181[1];
		        else
		        	c181 = new C181[2];
	        //C181[] c181 = new C181[2];
		    // Fine Modifica Agosto 2015 per evitare la creazion di una istanza vuota della C182 (ripresa da indice)

		       if (titset.getS181_TP_FORMA_CONTENUTO_1() != null) {
		            c181[0] = new C181();
		        	c181[0].setA_181_0(Decodificatore.getCd_unimarc("FOCO", titset.getS181_TP_FORMA_CONTENUTO_1()));
			        if (titset.getS181_CD_TIPO_CONTENUTO_1() != null)
			        	c181[0].setB_181_0(Decodificatore.getCd_unimarc("TICO", titset.getS181_CD_TIPO_CONTENUTO_1()));
			        if (titset.getS181_CD_MOVIMENTO_1() != null)
			        	c181[0].setB_181_1(Decodificatore.getCd_unimarc("MOVI", titset.getS181_CD_MOVIMENTO_1()));
			        if (titset.getS181_CD_DIMENSIONE_1() != null)
			        	c181[0].setB_181_2(Decodificatore.getCd_unimarc("BIDI", titset.getS181_CD_DIMENSIONE_1()));
			        if (titset.getS181_CD_SENSORIALE_1_1() != null)
			        	c181[0].setB_181_3(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_1_1()));
			        if (titset.getS181_CD_SENSORIALE_2_1() != null)
			        	c181[0].setB_181_4(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_2_1()));
			        if (titset.getS181_CD_SENSORIALE_3_1() != null)
			        	c181[0].setB_181_5(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_3_1()));

			        if (titset.getS181_TP_FORMA_CONTENUTO_2() != null) {
			            c181[1] = new C181();
			        	c181[1].setA_181_0(Decodificatore.getCd_unimarc("FOCO", titset.getS181_TP_FORMA_CONTENUTO_2()));
			        if (titset.getS181_CD_TIPO_CONTENUTO_2() != null)
			        	c181[1].setB_181_0(Decodificatore.getCd_unimarc("TICO", titset.getS181_CD_TIPO_CONTENUTO_2()));
			        if (titset.getS181_CD_MOVIMENTO_2() != null)
			        	c181[1].setB_181_1(Decodificatore.getCd_unimarc("MOVI", titset.getS181_CD_MOVIMENTO_2()));
			        if (titset.getS181_CD_DIMENSIONE_2() != null)
			        	c181[1].setB_181_2(Decodificatore.getCd_unimarc("BIDI", titset.getS181_CD_DIMENSIONE_2()));
			        if (titset.getS181_CD_SENSORIALE_1_2() != null)
			        	c181[1].setB_181_3(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_1_2()));
			        if (titset.getS181_CD_SENSORIALE_2_2() != null)
			        	c181[1].setB_181_4(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_2_2()));
			        if (titset.getS181_CD_SENSORIALE_3_2() != null)
			        	c181[1].setB_181_5(Decodificatore.getCd_unimarc("SENS", titset.getS181_CD_SENSORIALE_3_2()));
			        }
			        if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
			        	datiDoc.setT181(c181);
			        } //end if (titset.getS181_tp_forma_contenuto_1() != null)

	        // Inizio Modifica Agosto 2015 per evitare la creazion di una istanza vuota della C182 (ripresa da indice)
		       C182[] c182;
		        if (titset.getS182_TP_MEDIAZIONE_2() == null)
		        	c182 = new C182[1];
		        else
		        	c182 = new C182[2];
	        //C182[] c182 = new C182[2];
	        // Fine Modifica Agosto 2015 per evitare la creazion di una istanza vuota della C182 (ripresa da indice)

	        if (titset.getS182_TP_MEDIAZIONE_1() != null) {
	            c182[0] = new C182();
	        	c182[0].setA_182_0(Decodificatore.getCd_unimarc("MEDI", titset.getS182_TP_MEDIAZIONE_1()));
		        if (titset.getS182_TP_MEDIAZIONE_2() != null) {
		            c182[1] = new C182();
		        	c182[1].setA_182_0(Decodificatore.getCd_unimarc("MEDI", titset.getS182_TP_MEDIAZIONE_2()));
		        }
		        if (schema_version.compareTo(new BigDecimal(2.00)) > -1)
		        	datiDoc.setT182(c182);

	        } //end if (titset.getS182_tp_mediazione_1() != null)

	     // evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
	        // almaviva 183 Tipo supporto
	        C183[] c183;
	        if (titset.getS183_TP_SUPPORTO_2() == null)
	        	c183 = new C183[1];
	        else
	        	c183 = new C183[2];
	        if (titset.getS183_TP_SUPPORTO_1() != null) {
	            c183[0] = new C183();
	        	c183[0].setA_183_0(Decodificatore.getCd_unimarc("SUPP", titset.getS183_TP_SUPPORTO_1()));
		        if (titset.getS183_TP_SUPPORTO_2() != null) {
		            c183[1] = new C183();
		        	c183[1].setA_183_0(Decodificatore.getCd_unimarc("SUPP", titset.getS183_TP_SUPPORTO_2()));
		        } // almaviva SOLO PER SCHEMA 2.01
		        // almaviva2 Febbraio 2016 - Intervento interno - eliminato il controllo sul numero di versione
		        //if (schema_version.compareTo(new BigDecimal(2.01)) > -1)
		        	datiDoc.setT183(c183);
	        }
//	     // almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
//	     // è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
//	        if (titset.getS210_IND_PUBBLICATO() != null) {
//		        C210[] c210 = new C210[0];
//	        	c210 = datiDoc.getT210();
//	        	c210[0].setId2(IndicatorePubblicato.valueOf("1"));
//	        }
	      //evolutive 2017 solo per schema 2.03

	        C210[] c210 = new C210[1];

	        if ((datiDoc.getT210() != null)  && (datiDoc.getT210().length > 0)){

	        	for(int i=0; i< datiDoc.getT210().length; i++){
		        	c210[i]=datiDoc.getT210()[i];

	        	}
	        } else {
	        	c210[0] = new C210();
	        }
		    if (titset.getS210_IND_PUBBLICATO() != null) {
	        	c210[0].setId2(IndicatorePubblicato.valueOf("1"));
		        datiDoc.setT210(c210);
	        }

        } //end if (titset != null)
    } //end formattaDatiComuni1



}
