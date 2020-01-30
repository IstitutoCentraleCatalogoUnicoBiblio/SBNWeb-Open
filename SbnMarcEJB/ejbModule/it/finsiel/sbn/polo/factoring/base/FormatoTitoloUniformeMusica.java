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

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.isFilled;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.trimOrEmpty;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Composizione;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_composizione;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A152;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A801;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
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
 * Classe FormatoDocumentoModerno
 * <p>
 * Si occupa della formattazione di un documento di tipo moderno, che è analoga a quella
 * di un titolo, poichè è la più generica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 9-genn-03
 */
public class FormatoTitoloUniformeMusica extends FormatoElementAut {

    public FormatoTitoloUniformeMusica() {
    }

    /** Copiato da titoloUniforme
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public Object formattaTitolo(Tb_titolo titolo, BigDecimal versione) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elAut = new ElementAutType();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) { //Analitico
            elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));

         	//Inizio intervento almaviva2  - BUG Mantis 4515 collaudo. si inserisce la modifica già fatta su protocollo di Indice
            // per caricare i REPERTORI
        	//Mantis 2223. Aggiunta la classe formattaLegamiPerEsameAnaliticoeRep in sostituzione a formattaLegamiPerEsameAnalitico
        	//poichè la classe precedente non caricava i repertori e nel caso dei titoli uniformi musica questi non venivano
        	//visualizzati.
            // LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo);
            LegamiType[] lega = formattaLegamiPerEsameAnaliticoeRep(titolo, schema_version);
            //Fine intervento almaviva2  - BUG Mantis 4515 collaudo


            elAut.setLegamiElementoAut(lega);
            TitoloCercaRicorsivo titoloCerca = new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
            LegamiType leg = titoloCerca.formattaLegamiDocDoc(titolo);
            //Se ci sono legami li aggiungo? Si, non di altri documenti.
            if (leg.enumerateArrivoLegame().hasMoreElements())
                elAut.addLegamiElementoAut(leg);
        } else {
            elAut.setDatiElementoAut(formattaDocumentoPerListaSintetica(titolo));
            elAut.setLegamiElementoAut(formattaLegamiPerListaSintetica(titolo));
        }
        return elAut;
    }
    public void aggiungiAltriLegami(Tb_titolo titolo,DocumentoType doc) throws EccezioneDB, EccezioneSbnDiagnostico {

    }

    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
    	//return formattaElemento(titolo,versione,CONSTANT.GESTISCI_NATURA_V_SI);
    	return formattaElemento(titolo,versione, true);
    }

    /** Copiato da titoloUniforme
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public Object formattaElemento(Tb_titolo titolo, BigDecimal versione, boolean gestisciNaturaV) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elAut = new ElementAutType();
        // ATTENZIONE: QUESTO IF DEVE RIMANERE DIVERSO DALLA VERSIONE DI INDICE ALTRIMNTI NON FUNZIONA
		//if ((tipoOut.equals(SbnTipoOutput.VALUE_0)) || (tipoOut.equals(SbnTipoOutput.VALUE_3)) ) { //Analitico
		if ((tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) || (tipoOut.getType() == SbnTipoOutput.VALUE_3.getType()) ) { //Analitico
            elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));
            LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo, schema_version);
            elAut.setLegamiElementoAut(lega);
        } else {
            elAut.setDatiElementoAut(formattaDocumentoPerListaSintetica(titolo));
        }
        return elAut;
    }


    /**
     * Formatta il titolo uniforme per la lista sintetica
     */
    protected TitoloUniformeMusicaType formattaDocumentoPerListaSintetica(Tb_titolo titolo) {
        TitoloUniformeMusicaType datiEl = new TitoloUniformeMusicaType();

        datiEl.setTipoAuthority(SbnAuthority.valueOf("UM"));
        if(titolo.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

        datiEl.setT001(titolo.getBID());
        if (visualizzaTimestamp()){
            SbnDatavar data = new SbnDatavar(titolo.getTS_VAR());
            datiEl.setT005(data.getT005Date());
            //datiEl.setT005(titolo.getTS_VAR().getDate());
        }
        A230 a230 = new A230();
        a230.setA_230(titolo.getISBD());
        datiEl.setT230(a230);
        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        return datiEl;
    }

    protected DatiElementoType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws EccezioneDB, InfrastructureException {
        TitoloUniformeMusicaType datiEl = new TitoloUniformeMusicaType();
        datiEl.setTipoAuthority(SbnAuthority.valueOf("UM"));
        if(titolo.getFL_CONDIVISO() != null)
        	datiEl.setCondiviso(DatiElementoTypeCondivisoType.valueOf(titolo.getFL_CONDIVISO()));

        datiEl.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(titolo.getCD_LIVELLO())));
        datiEl.setT001(titolo.getBID());
        SbnDatavar datavar = new SbnDatavar(titolo.getTS_VAR());
        datiEl.setT005(datavar.getT005Date());
        A100 data = new A100();
        try {
            SbnData data2 = new SbnData(titolo.getTS_INS());
            data.setA_100_0(Date.parseDate(data2.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: titolo.ts_ins:" + titolo.getBID());
        }
        datiEl.setT100(data);
        if (titolo.getISADN() != null) {
            A015 a015 = new A015();
            a015.setA_015(titolo.getISADN());
            datiEl.setT015(a015);
        }
        A230 a230 = new A230();
        a230.setA_230(titolo.getISBD());
        datiEl.setT230(a230);
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
        Musica musicaDB = new Musica();
        Tb_musica musica = musicaDB.cercaPerId(titolo.getBID());
        Composizione compDB = new Composizione();
        Tb_composizione comp = compDB.cercaPerId(titolo.getBID());
        if (comp != null) {
            A929 a929 = new A929();
            a929.setA_929(comp.getNUMERO_ORDINE()); //num. ordine
            a929.setB_929(comp.getNUMERO_OPERA()); //num. opera
            a929.setC_929(comp.getNUMERO_CAT_TEM()); //num. catalogo tematico
            //almaviva5_20200128 #7335
            a929.setD_929(trimOrEmpty(comp.getDATAZIONE())); // Datazione
            if (isFilled(comp.getCD_TONALITA()))
                a929.setE_929(
                    Decodificatore.getCd_unimarc("Tb_composizione", "cd_tonalita", comp.getCD_TONALITA()));
            a929.setF_929(comp.getDS_SEZIONI()); //sezioni
            a929.setG_929(comp.getKY_ORD_DEN()); //titolo di ordinamento
            a929.setH_929(comp.getKY_EST_DEN()); //titolo dell'estratto
            a929.setI_929(comp.getKY_APP_DEN()); //Appellativo
            datiEl.setT929(a929);

            A928 a928 = new A928();
            if (comp.getCD_FORMA_1() != null)
                a928.addA_928(
                    Decodificatore.getCd_unimarc("Tb_composizione", "cd_forma_1", comp.getCD_FORMA_1()));
            if (comp.getCD_FORMA_2() != null)
                a928.addA_928(
                    Decodificatore.getCd_unimarc("Tb_composizione", "cd_forma_2", comp.getCD_FORMA_2()));
            if (comp.getCD_FORMA_3() != null)
                a928.addA_928(
                    Decodificatore.getCd_unimarc("Tb_composizione", "cd_forma_3", comp.getCD_FORMA_3()));
            if (musica != null) {
                a928.setB_928(musica.getDS_ORG_SINT()); //organico sintetico
                a928.setC_928(musica.getDS_ORG_ANAL()); //organico analitico
            }
            datiEl.setT928(a928);
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
        legami = new LegamiType[legamiVec.size()];
        for (int i = 0; i < legami.length; i++) {
            legami[i] = (LegamiType) legamiVec.get(i);
        }
        return legami;
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
        legameEl.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        legameEl.setTipoRespons(SbnRespons.valueOf(tp_respons));
        legameEl.setElementoAutLegato(formattaAutoreLegatoSintetica(relaz));
        arrLegame.setLegameElementoAut(legameEl);
        return arrLegame;
    }

    public boolean filtraLegameAutoreSint(Vl_autore_tit relaz) {
        if (relaz.getTP_RESPONSABILITA().equals("1"))
            return true;
        return false;
    }

}
