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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_titResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Abstract;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Cartografia;
import it.finsiel.sbn.polo.oggetti.Composizione;
import it.finsiel.sbn.polo.oggetti.Comuni;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Link_multim;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.Nota;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.ThesauroTitolo;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_1;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloCancella
 * <p>
 * Cancella i documenti o i legami.
 * </p>
 * @author
 * @author
 *
 * @version 5-mag-03
 */
public class TitoloCancella extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3346236199878320527L;

	/** Costruttore
     * @param conn Connessione al DB.
     */
    public TitoloCancella() {
        super();
    }

    /**
     * Imposta fl_canc='S',ts_var e ute_var sulle tabelle
     * collegate alla tb_titolo: tb_musica, tb_grafica, tb_cartografia, tb_impronta, tb_numero_std,
     * tb_composizione,
     * e in tutte le tr_tit_*
     * @param titolo, il titolo da cancellare, con già settato ute_var
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws EccezioneSbnDiagnostico
     */
    public void cancellaElementiLegati(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        String bid = titolo.getBID();
        String ute_var = titolo.getUTE_VAR();
        Musica musica = new Musica();
        if (musica.cercaPerId(bid) != null)
            musica.cancellaPerBid(bid, ute_var);
        Impronta impronta = new Impronta();
        if (impronta.cercaPerBid(bid).size() > 0)
            impronta.cancellaPerBid(bid, ute_var);
        Composizione comp = new Composizione();
        if (comp.cercaPerId(bid) != null)
            comp.cancellaPerBid(bid, ute_var);
        Grafica grafica = new Grafica();
        Tb_grafica gra = grafica.cercaPerId(bid);


        // Inizio BUG MANTIS 3648  almaviva2 adeguato comportamento a quello di Cartografia
//        if (gra != null)
//            grafica.cancellaPerBid(bid, ute_var);
        if (gra != null)
            grafica.cancellaPerBid(bid, ute_var, new SbnDatavar(gra.getTS_VAR()));
        // Fine BUG MANTIS 3648  almaviva2 adeguato comportamento a quello di Cartografia



        Cartografia cart = new Cartografia();
        Tb_cartografia tcart = cart.cercaPerId(bid);
        if (tcart != null)
            cart.cancellaPerBid(bid, ute_var,new SbnDatavar(tcart.getTS_VAR()));

        // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
//        Audiovisivo audi = new Audiovisivo(db_conn);
//        Tb_audiovideo taudi = audi.cercaPerId(bid);
//        if (taudi != null)
//            audi.cancellaPerBid(bid, ute_var,taudi.getTs_var());
//        Discosonoro disc = new Discosonoro(db_conn);
//        Tb_disco_sonoro tdisc = disc.cercaPerId(bid);
//        if (tdisc != null)
//            disc.cancellaPerBid(bid, ute_var,tdisc.getTs_var());
        // Finealmaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro



        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
        Comuni tits = new Comuni();
        Tb_titset_1 ttits = tits.cercaPerId(bid);
        if (ttits != null)
            tits.cancellaPerBid(bid, ute_var,new SbnDatavar(ttits.getTS_VAR()));
        // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi


        NumeroStd numero = new NumeroStd();
        if (numero.cercaNumeroPerBid(bid).size() > 0)
            numero.cancellaPerBid(bid, ute_var);
        Link_multim lm = new Link_multim();
        if (lm.cercaLinkMultim(bid).size()>0) {
            lm.cancellaPerKy(bid,ute_var);
        }
        TitoloClasse titoloClasse = new TitoloClasse();
        List v;
        v = titoloClasse.estraiTitoliClasse(bid);
        if (v.size() > 0) {
            titoloClasse.cancellaPerBid(bid, ute_var);
        }
        TitoloAutore titAut = new TitoloAutore();
        v = titAut.estraiTitoliAutore(bid);
        if (v.size() > 0) {
            titAut.cancellaPerBid(bid, ute_var);
            //Non devo aggiornare gli autori
            //Tr_tit_aut classe;
            //Autore classeDB = new Autore();
            //for (int i = 0; i < v.size(); i++) {
            //    classe = (Tr_tit_aut) v.get(i);
            //    classeDB.updateVariazione(classe.getVID(), ute_var);
            //}
        }
        TitoloLuogo titLuo = new TitoloLuogo();
        v = titLuo.estraiTitoliLuogo(bid);
        if (v.size() > 0) {
            titLuo.cancellaPerBid(bid, ute_var);
        }
        TitoloMarca titMar = new TitoloMarca();
        v = titMar.estraiTitoliMarca(bid);
        if (v.size() > 0) {
            titMar.cancellaPerBid(bid, ute_var);
        }
        ThesauroTitolo titThe = new ThesauroTitolo();
        v = titThe.estraiTitoliThesauro(bid);
        if (v.size() > 0) {
            titThe.cancellaPerBid(bid, ute_var);
        }
        Abstract abs = new Abstract();
        v = abs.estraiTitoliAbstract(bid);
        if (v.size() > 0) {
        	abs.cancellaAbstractPerBid(bid, ute_var);
        }

        SoggettoTitolo titSog = new SoggettoTitolo();
        v = titSog.estraiTitoliSoggetto(bid);
        if (v.size() > 0) {
            titSog.cancellaPerBid(bid, ute_var);
        }
        Nota nota = new Nota();
        //almaviva5_20090918 #3068
        nota.cancella(bid, ute_var);
        //Tolgo perchè altrimenti non aggiorna i flag di allineamento
        //TitoloBiblioteca titBib = new TitoloBiblioteca();
        //v = titBib.cercaLocalizzazioni(bid);
        //if (v.size() > 0) {
        //    titBib.cancellaPerBid(bid, ute_var);
        //}

        //Vengono cancellati altrove.
        cancellaLegamiConTitoli(titolo);
    }

    /** Cancella tutti i legami, anche con i titoli uniformi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void cancellaLegamiConTitoli(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        List v = cercaLegamiDocumento(titolo, null);
        if (v.size() > 0) {
            Tr_tit_tit tt = new Tr_tit_tit();
            tt.setBID_BASE(titolo.getBID());
            tt.setUTE_VAR(titolo.getUTE_VAR());
            Tr_tit_titResult tavola = new Tr_tit_titResult(tt);
            tavola.executeCustom("updateCancellaPerBidBase");

            //Non devo aggiornare i titoli collegati
            //for (int i = 0; i < v.size(); i++) {
            //    updateVersione(((Vl_titolo_tit_b) v.get(i)).getBID(), titolo.getUTE_VAR());
            //}
        }

    }
    /**
     * Imposta fl_canc='S',ts_var e ute_var su tb_titolo; vengono cancellati i legami
     * con autori tr_tit_aut e repertori tr_rep_tit, e i legami con titoli verso
     * l'alto (tr_tit_tit con bid=bid_base)
     *
     * Aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_tit_bib (chiama il
     * metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
     *
     * @param titolo, il titolo da cancellare, con già settato ute_var
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws EccezioneSbnDiagnostico
     */
    public void cancellaElementiLegatiTitUni(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        String bid = titolo.getBID();
        String ute_var = titolo.getUTE_VAR();
        List v;
        TitoloAutore titAut = new TitoloAutore();
        v = titAut.estraiTitoliAutore(bid);
        if (v.size() > 0) {
            titAut.cancellaPerBid(bid, ute_var);
            Tr_tit_aut classe;
            Autore classeDB = new Autore();
            for (int i = 0; i < v.size(); i++) {
                classe = (Tr_tit_aut) v.get(i);
                classeDB.updateVariazione(classe.getVID(), ute_var);
            }
        }
        //RepertorioTitoloUniforme Solo per i titoli uniformi.
        RepertorioTitoloUniforme repTit = new RepertorioTitoloUniforme();
        v = repTit.cercaCitazioniInRepertori(bid, null);
        if (v.size() > 0) {
            repTit.cancellaPerBid(bid, ute_var);
            Tr_rep_tit classe;
            Repertorio classeDB = new Repertorio();
            for (int i = 0; i < v.size(); i++) {
                classe = (Tr_rep_tit) v.get(i);
                classeDB.updateVersione((int) classe.getID_REPERTORIO(), ute_var);
            }
        }
        //Devo cancellare i legami con altri titoli verso l'alto.
        cancellaLegamiConTitoli(titolo);
    }

    /**
     * Imposta fl_canc='S',ts_var e ute_var su
     * tb_titolo,tb_musica,tb_composizione; vengono cancellati i legami con autori
     * tr_tit_aut e repertori tr_rep_tit, e i legami con titoli verso l'alto
     * (tr_tit_tit con bid=bid_base)
     *
     * Aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_tit_bib (chiama il
     * metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
     *
     * @param titolo, il titolo da cancellare, con già settato ute_var
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws EccezioneSbnDiagnostico
     */
    public void cancellaElementiLegatiTitUniMus(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        String bid = titolo.getBID();
        String ute_var = titolo.getUTE_VAR();
        Musica musica = new Musica();
        if (musica.cercaPerId(bid) != null)
            musica.cancellaPerBid(bid, ute_var);
        Composizione comp = new Composizione();
        if (comp.cercaPerId(bid) != null)
            comp.cancellaPerBid(bid, ute_var);
        List v;
        TitoloAutore titAut = new TitoloAutore();
        v = titAut.estraiTitoliAutore(bid);
        if (v.size() > 0) {
            titAut.cancellaPerBid(bid, ute_var);
            Tr_tit_aut classe;
            Autore classeDB = new Autore();
            for (int i = 0; i < v.size(); i++) {
                classe = (Tr_tit_aut) v.get(i);
                classeDB.updateVariazione(classe.getVID(), ute_var);
            }
        }
        //RepertorioTitoloUniforme Solo per i titoli uniformi.
        RepertorioTitoloUniforme repTit = new RepertorioTitoloUniforme();
        v = repTit.cercaCitazioniInRepertori(bid, null);
        if (v.size() > 0) {
            repTit.cancellaPerBid(bid, ute_var);
            Tr_rep_tit classe;
            Repertorio classeDB = new Repertorio();
            for (int i = 0; i < v.size(); i++) {
                classe = (Tr_rep_tit) v.get(i);
                classeDB.updateVersione((int) classe.getID_REPERTORIO(), ute_var);
            }
        }
        //Devo cancellare i legami con altri titoli verso l'alto.
        cancellaLegamiConTitoli(titolo);
    }

    public void cancellaPerFusione(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);
        tavola.executeCustom("updateCancellaPerFusione");
    }


}
