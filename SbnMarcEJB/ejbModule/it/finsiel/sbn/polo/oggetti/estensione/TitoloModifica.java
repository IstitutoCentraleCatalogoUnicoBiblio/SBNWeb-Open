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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_titResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_claResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_luoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_marResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_sog_bibResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_titResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Trs_termini_titoli_bibliotecheResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.GestoreLegami;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Audiovisivo;
import it.finsiel.sbn.polo.oggetti.Cartografia;
import it.finsiel.sbn.polo.oggetti.Comuni;
import it.finsiel.sbn.polo.oggetti.Discosonoro;
import it.finsiel.sbn.polo.oggetti.Elettronico;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Incipit;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloTitolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloUniformeCerca;
import it.finsiel.sbn.polo.orm.Tb_audiovideo;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_incipit;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_risorsa_elettr;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_1;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndicatorePubblicato;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;


/**
 * Classe TitoloModifica.java
 * <p>
 * Modifica i legami in fase di modifica di un titolo.
 * </p>
 *
 * @author
 * @author
 *
 * @version 25-mar-03
 */
public class TitoloModifica extends TitoloCreaDocumento {

	private static final long serialVersionUID = 76551779401606213L;

	public TitoloModifica(ModificaType datiType) {
        super(datiType);
    }

    public void modificaLegami(
        LegamiType[] legami,
        TimestampHash timeHash,
        Tb_titolo titolo,
        AllineamentoTitolo flagAll, SbnUserType sbnUser)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 0; i < legami.length; i++) {
            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null) {
	                    creaLegameDocDoc(titolo, legame.getLegameDoc(), timeHash);
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameTitAccesso() != null) {
	                    creaLegameDocTitAccesso(titolo, legame.getLegameTitAccesso(), timeHash);
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
	                        creaLegameAutore(titolo, legEl, timeHash);
                            flagAll.setTr_tit_aut(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                            creaLegameClasse(titolo, legEl, timeHash);
                            flagAll.setTr_tit_cla(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
                            creaLegameLuogo(titolo, legEl, timeHash);
                            flagAll.setTr_tit_luo(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            creaLegameMarca(titolo, legEl, timeHash);
                            flagAll.setTr_tit_mar(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                            creaLegameSoggetto(titolo, legEl, timeHash, sbnUser);
                            flagAll.setTr_tit_sog(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
                            creaLegameThesauro(titolo, legEl, timeHash, sbnUser);
                            flagAll.setTrs_termini_titoli_biblioteche(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                            creaLegameRepertorio(titolo, legEl, timeHash);
                            flagAll.setTr_rep_tit(true);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE|| legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
		                    creaLegameDocTitUni(titolo, legEl, timeHash);
                            flagAll.setTr_tit_tit(true);
                        }
                    }
                }
            }
            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null) {
	                    modificaLegameDocDoc(titolo, legame.getLegameDoc(), timeHash);
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameTitAccesso() != null) {
	                    modificaLegameDocTitAccesso(titolo, legame.getLegameTitAccesso(), timeHash);
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
	                        modificaLegameAutore(titolo, legEl, timeHash);
                            flagAll.setTr_tit_aut(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                            modificaLegameClasse(titolo, legEl, timeHash);
                            flagAll.setTr_tit_cla(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
                            modificaLegameLuogo(titolo, legEl, timeHash);
                            flagAll.setTr_tit_luo(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            modificaLegameMarca(titolo, legEl, timeHash);
                            flagAll.setTr_tit_mar(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                        	//almaviva5_20120507 evolutive CFI
                            int rankModificato = modificaLegameSoggetto(titolo, legEl, timeHash, sbnUser);
                            if (rankModificato != 0) {
                            	SoggettoTitolo st = new SoggettoTitolo();
                            	st.rankLegameTitoloSoggetto(titolo.getBID(), legEl, sbnUser, legami[i].getTipoOperazione(), rankModificato);
                            }

                            flagAll.setTr_tit_sog(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
                            modificaLegameThesauro(titolo, legEl, timeHash,sbnUser);
                            flagAll.setTrs_termini_titoli_biblioteche(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                            modificaLegameRepertorio(titolo, legEl, timeHash);
                            flagAll.setTr_rep_tit(true);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
		                    modificaLegameDocTitUni(titolo, legEl, timeHash);
                            flagAll.setTr_tit_tit(true);
                        }
                    }
                }
            }
            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null) {
                    	 // Manutenzione almaviva2 23Luglio2009 anche i legami 464 (M464N) sono rovesciati! inserito or su altro tipo legame
                        if (SbnLegameDoc.valueOf("463").getType() == legame.getLegameDoc().getTipoLegame().getType()
                        		|| SbnLegameDoc.valueOf("464").getType() == legame.getLegameDoc().getTipoLegame().getType()) {

                        	// Inizio intervento almaviva2 BUG MANTIS 4494 (esercizio)
                        	// intervento a specchio con l'indice per impedire la cancellazione di una W quando ci sono degli
                        	// spogli agganciati (Vedi commento //almaviva4 14/06/2011)
                            Tb_titolo tf = estraiTitoloPerID(legame.getLegameDoc().getIdArrivo());
                            if (contaLegamiVersoBasso(legame.getLegameDoc().getIdArrivo()) > 0)
                                throw new EccezioneSbnDiagnostico(3095, "Il documento è un livello di raggruppamento");
                          // Fine intervento almaviva2 BUG MANTIS 4494 (esercizio)

                            cancellaLegameTitTit(titolo.getUTE_VAR(), legame.getLegameDoc().getIdArrivo(), titolo.getBID(), timeHash);
                        } else {
                            cancellaLegameTitTit(titolo.getUTE_VAR(), titolo.getBID(), legame.getLegameDoc().getIdArrivo(), timeHash);
                        }
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameTitAccesso() != null) {
                        cancellaLegameTitTit(titolo.getUTE_VAR(), titolo.getBID(), legame.getLegameTitAccesso().getIdArrivo(), timeHash);
                        flagAll.setTr_tit_tit(true);
                    } else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            cancellaLegameAutore(titolo, legEl, timeHash);
                            flagAll.setTr_tit_aut(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                            cancellaLegameClasse(titolo, legEl, timeHash);
                            flagAll.setTr_tit_cla(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
                            cancellaLegameLuogo(titolo, legEl, timeHash);
                            flagAll.setTr_tit_luo(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            cancellaLegameMarca(titolo, legEl, timeHash);
                            flagAll.setTr_tit_mar(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                        	Tb_soggetto _tb_soggetto = new Tb_soggetto();
                        	Soggetto soggetto = new Soggetto();
                        	_tb_soggetto = soggetto.cercaSoggettoPerId(legEl.getIdArrivo());
                        	_tb_soggetto.getCD_SOGGETTARIO().toString();

                            cancellaLegameSoggetto(titolo, legEl, timeHash,sbnUser,_tb_soggetto.getCD_SOGGETTARIO().toString());
                            //almaviva5_20120507 evolutive CFI
                            SoggettoTitolo st = new SoggettoTitolo();
                        	st.rankLegameTitoloSoggetto(titolo.getBID(), legEl, sbnUser, legami[i].getTipoOperazione(), 0);

                            flagAll.setTr_tit_sog(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
                            cancellaLegameThesauro(titolo, legEl, timeHash);
                            flagAll.setTrs_termini_titoli_biblioteche(true);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                            cancellaLegameRepertorio(titolo, legEl, timeHash);
                            flagAll.setTr_rep_tit(true);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
                            cancellaLegameTitTit(titolo.getUTE_VAR(), titolo.getBID(), legEl.getIdArrivo(), timeHash);
                            flagAll.setTr_tit_tit(true);

                            // Febbraio 2018 - adeguamento a manutenzione di Indice per cancellazione rinvio a TitoloUniforme A
                            // almaviva CANCELLAZIONE NATURA V
                            if (legEl.getTipoLegame().toString().equalsIgnoreCase("431")){
                               TitoloUniformeCerca cerca = new TitoloUniformeCerca();
                               Tb_titolo titolo_rinvioV = null;
                               titolo_rinvioV =  cerca.estraiTitoloPerID(legEl.getIdArrivo());
                               cancellaTitolo(titolo_rinvioV);
                            }
                            // END almaviva CANCELLAZIONE NATURA V

                        }
                    }
                }
            }
        }
    }

    public void cancellaLegameTitTit(String ute_var,String bid, String idArrivo, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloTitolo ttDB = new TitoloTitolo();
        ttDB.updateCancella(
            ute_var,
            bid,
            idArrivo,
            timeHash.getTimestamp("Tr_tit_tit", bid + idArrivo));
    }

    public void cancellaLegameAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloAutore ta = new TitoloAutore();
        ta.disabilitaTitoloAutore(titolo, leg, timeHash);
    }

    public void cancellaLegameSoggetto(Tb_titolo titolo, LegameElementoAutType leg,
    		TimestampHash timeHash,SbnUserType sbnUser,String cd_sogg)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        String firma = sbnUser.getBiblioteca() + sbnUser.getUserId();

	    //almaviva5_20091030
	    SoggettoValida soggettoValida = new SoggettoValida();
	    String cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", cd_sogg.trim());
	    soggettoValida.controllaVettoreParametriSemantici(cd, sbnUser.getBiblioteca());
	    soggettoValida.controllaMaxLivelloSoggettazione(cd_sogg.trim(), titolo.getBID(), sbnUser);
	    //

        Tr_tit_sog_bib ts = new Tr_tit_sog_bib();
        ts.setCID(leg.getIdArrivo());

        ts.setCD_SOGG(cd_sogg);
        ts.setBID(titolo.getBID());
        ts.setUTE_VAR(firma);
        ts.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_sog_bib", ts.getBID() + ts.getCID())));
        Tr_tit_sog_bibResult tabella = new Tr_tit_sog_bibResult(ts);
        String cd_polo =sbnUser.getBiblioteca().substring(0,3);
        String cd_biblioteca = sbnUser.getBiblioteca().substring(3,6);


//      if(//fl_autoLoc su tr_soggettari_biblioteche = 1){
//		// cancello senza condizioni l'unico record che ha bid e cid indipendentemente dalla biblioteca
//}
//else{
//	// cancello con chiave cid bid e biblioteca utente che invia la richiesta
//}
        if(tabella.verifica_fl_aut_loc(cd_polo,cd_biblioteca,cd_sogg)){
        	tabella.executeCustom("updateDisabilita");
        }else{
        	ts.setCD_BIBLIOTECA(cd_biblioteca);
        	tabella.executeCustom("updateDisabilitaConBib");
        }


    }
    public void cancellaLegameThesauro(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
	    throws IllegalArgumentException, InvocationTargetException, Exception {
	    Trs_termini_titoli_biblioteche ts = new Trs_termini_titoli_biblioteche();
	    ts.setDID(leg.getIdArrivo());
	    ts.setBID(titolo.getBID());
	    ts.setUTE_VAR(titolo.getUTE_VAR());
	    ts.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Trs_termini_titoli_biblioteche", ts.getBID() + ts.getDID())));
	    Trs_termini_titoli_bibliotecheResult tabella = new Trs_termini_titoli_bibliotecheResult(ts);
	    tabella.executeCustom("updateDisabilita");

	}

    public void cancellaLegameClasse(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_cla tc = new Tr_tit_cla();
        tc.setBID(titolo.getBID());
        String chiave = leg.getIdArrivo();
        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(chiave);
         //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	tc.setCD_SISTEMA(sd.getSistema() + edizione);
        	tc.setCD_EDIZIONE(edizione);
        } else {
        	tc.setCD_SISTEMA(sd.getSistema());
            tc.setCD_EDIZIONE("  ");
        }
    	tc.setCLASSE(sd.getSimbolo());

        tc.setUTE_VAR(titolo.getUTE_VAR());
        tc.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_cla", tc.getBID() + chiave)));
        Tr_tit_claResult tabella = new Tr_tit_claResult(tc);
        tabella.executeCustom("updateDisabilita");
    }

    public void cancellaLegameLuogo(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(titolo.getBID());
        tl.setLID(leg.getIdArrivo());
        tl.setUTE_VAR(titolo.getUTE_VAR());
        tl.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_luo", tl.getBID() + tl.getLID())));
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);
        tabella.executeCustom("updateDisabilita");
    }

    public void cancellaLegameRepertorio(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_tit rt = new Tr_rep_tit();
        rt.setBID(titolo.getBID());
        Repertorio repertorio = new Repertorio();
        Tb_repertorio tb_repertorio = repertorio.cercaRepertorioPerCdSig(leg.getIdArrivo());
        rt.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
        rt.setUTE_VAR(titolo.getUTE_VAR());
        rt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_rep_tit", rt.getBID() + rt.getID_REPERTORIO())));
        Tr_rep_titResult tabella = new Tr_rep_titResult(rt);
        tabella.executeCustom("updateDisabilita");
    }

    /** Solo per antico
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void cancellaLegameMarca(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_mar tm = new Tr_tit_mar();
        tm.setBID(titolo.getBID());
        tm.setMID(leg.getIdArrivo());
        tm.setUTE_VAR(titolo.getUTE_VAR());
        tm.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_mar", tm.getBID() + tm.getMID())));
        Tr_tit_marResult tabella = new Tr_tit_marResult(tm);
        tabella.executeCustom("updateDisabilita");

    }

    public void modificaLegameDocTitAccesso(
        Tb_titolo titolo,
        LegameTitAccessoType leg,
        TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }

        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());

        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(titolo.getBID());
        tt.setBID_COLL(leg.getIdArrivo());
        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
        tt.setFL_CANC(" ");
        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
        String tp_legame = leg.getTipoLegame().toString();
        //Aggiungo le nature dei titoli
        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        //Tolgo il primo e l'ultimo elemento
        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
        tt.setTP_LEGAME(tp_legame);
        if (leg.getSottoTipoLegame() != null)
            tt.setTP_LEGAME_MUSICA(leg.getSottoTipoLegame().toString());
        tt.setUTE_VAR(titolo.getUTE_VAR());

        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("423")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("454")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("510")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("517")));

        tt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL())));

        Tr_tit_tit tt1 = estraiLegame(titolo.getBID(), leg.getIdArrivo());
		//if(tt1.getFL_CONDIVISO() != _legame_condiviso){
		if(!tt1.getFL_CONDIVISO().equals(_legame_condiviso)){
			tt.setFL_CONDIVISO(_legame_condiviso);
			tt.setTS_CONDIVISO(tt.getTS_VAR());
			tt.setUTE_CONDIVISO(titolo.getUTE_VAR());
		}else{
			tt.setFL_CONDIVISO(tt1.getFL_CONDIVISO());
			tt.setTS_CONDIVISO(tt1.getTS_CONDIVISO());
			tt.setUTE_CONDIVISO(tt1.getUTE_CONDIVISO());
		}


        //Da creare il legame tra i due doc
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.update(tt);

    }

    public void modificaLegameDocDoc(Tb_titolo titolo, LegameDocType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //Se non è un legame 463 lo tratto normalmente

    	// Modifica BUG MANTIS 4914 (esercizio) ripresa da medesimo oggetto di Indice
    	// aggiunto if per tipo legame 464 che (M464N) che deve seguire lo stesso percorso del 463 (M463W) cioe dei 51
        if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("463").getType()
        		|| leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("464").getType()) {
            leg.setTipoLegame(SbnLegameDoc.valueOf("461"));
            String idArrivo = leg.getIdArrivo();
            leg.setIdArrivo(titolo.getBID());
            Tb_titolo legato = estraiTitoloPerID(idArrivo);
            modificaLegameTraTitoli(legato, leg, timeHash);
            AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(legato);
            flagAllineamento.setTr_tit_tit(true);
            new TitoloGestisceAllineamento().aggiornaFlagAllineamento(
                flagAllineamento,
                titolo.getUTE_VAR());
        } else {
            modificaLegameTraTitoli(titolo, leg, timeHash);
        }
    }

    protected void modificaLegameTraTitoli(Tb_titolo titolo, LegameDocType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());

        //Controllo tipi legame e cd natura
    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }

        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(titolo.getBID());
        tt.setBID_COLL(leg.getIdArrivo());
        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
        tt.setFL_CANC(" ");
        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
        String sequenza = leg.getSequenza();
        if (sequenza != null) {
            while (sequenza.length() < 10)
                sequenza = " " + sequenza;
            tt.setSEQUENZA(sequenza);
        }
        tt.setSICI(leg.getSici());
        String tp_legame = leg.getTipoLegame().toString();
        //Aggiungo le nature dei titoli
        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        //Tolgo il primo e l'ultimo elemento
        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
        tt.setTP_LEGAME(tp_legame);
        tt.setUTE_VAR(titolo.getUTE_VAR());
        tt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL())));
        Tr_tit_tit tt1 = estraiLegame(titolo.getBID(), leg.getIdArrivo());
        if(!tt1.getFL_CONDIVISO().equals(_legame_condiviso)){
			tt.setFL_CONDIVISO(_legame_condiviso);
			tt.setTS_CONDIVISO(tt.getTS_VAR());
			tt.setUTE_CONDIVISO(titolo.getUTE_VAR());
		}else{
			tt.setFL_CONDIVISO(tt1.getFL_CONDIVISO());
			tt.setTS_CONDIVISO(tt1.getTS_CONDIVISO());
			tt.setUTE_CONDIVISO(tt1.getUTE_CONDIVISO());
		}


        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.update(tt);
    }

    public void modificaLegameDocTitUni(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());
        //Controllo tipi legame e cd natura

    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }

        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(titolo.getBID());
        tt.setBID_COLL(leg.getIdArrivo());
        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
        tt.setFL_CANC(" ");
        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
        String tp_legame = leg.getTipoLegame().toString();
        //Aggiungo le nature dei titoli
        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        //Tolgo il primo e l'ultimo elemento
        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
        tt.setTP_LEGAME(tp_legame);
        tt.setUTE_VAR(titolo.getUTE_VAR());
        tt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL())));


        Tr_tit_tit tt1 = estraiLegame(titolo.getBID(), leg.getIdArrivo());
        if(!tt1.getFL_CONDIVISO().equals(_legame_condiviso)){
			tt.setFL_CONDIVISO(_legame_condiviso);
			tt.setTS_CONDIVISO(tt.getTS_VAR());
			tt.setUTE_CONDIVISO(titolo.getUTE_VAR());
		}else{
			tt.setFL_CONDIVISO(tt1.getFL_CONDIVISO());
			tt.setTS_CONDIVISO(tt1.getTS_CONDIVISO());
			tt.setUTE_CONDIVISO(tt1.getUTE_CONDIVISO());
		}


		Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.update(tt);
    }

    public void modificaLegameAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloAutore ta = new TitoloAutore();
        ta.modificaTitoloAutore(titolo, leg, timeHash);
    }

    public int modificaLegameSoggetto(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash, SbnUserType sbnUser)
    		throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException, Exception {
		// CERCO PRIMA IL SOGGETTO PER TROVARE IL CD_SOGG
		Soggetto soggetto = new Soggetto();
		Tb_soggetto tb_soggetto = new Tb_soggetto();
		tb_soggetto = soggetto.cercaSoggettoPerId(leg.getIdArrivo());

		String firma = sbnUser.getBiblioteca() + sbnUser.getUserId();

		// almaviva5_20091030
		SoggettoValida soggettoValida = new SoggettoValida();
		String cd = Decodificatore.getCd_unimarc("Tb_soggetto",	"cd_soggettario", tb_soggetto.getCD_SOGGETTARIO().trim());
		soggettoValida.controllaVettoreParametriSemantici(cd, sbnUser.getBiblioteca());
		soggettoValida.controllaMaxLivelloSoggettazione(tb_soggetto.getCD_SOGGETTARIO().trim(), titolo.getBID(), sbnUser);
		//

		String cd_polo = sbnUser.getBiblioteca().substring(0, 3);
		String cd_biblioteca = sbnUser.getBiblioteca().substring(3, 6);

		Tr_tit_sog_bib ts = new Tr_tit_sog_bib();
		ts.setCID(leg.getIdArrivo());
		ts.setBID(titolo.getBID());

		Tr_tit_sog_bibResult dao = new Tr_tit_sog_bibResult(ts);
		dao.executeCustom("selectPerKey");
		List<Tr_tit_sog_bib> legami = dao.getElencoRisultati();
		Tr_tit_sog_bib oldLegame = ValidazioneDati.first(legami);

		if (oldLegame == null)
			throw new EccezioneSbnDiagnostico(3029); // legame inesistente

		// almaviva5_20120507 evolutive CFI
		short oldRank = oldLegame.getPOSIZIONE();
		short newRank = GestoreLegami.getPosizioneLegame(leg);
		int direction = oldRank - newRank;

		ts = oldLegame;
		ts.setPOSIZIONE(newRank);

		if (tb_soggetto != null)
			ts.setCD_SOGG(tb_soggetto.getCD_SOGGETTARIO());

		ts.setFL_CANC(" ");
		ts.setUTE_VAR(firma);
		ts.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_sog_bib", ts.getBID() + ts.getCID())));
		ts.setCD_BIBLIOTECA(cd_biblioteca);
		ts.setCD_POLO(cd_polo);
		// almaviva5_20090302
		ts.setNOTA_TIT_SOG_BIB(leg.getNoteLegame());

		dao = new Tr_tit_sog_bibResult(ts);
		dao.update(null);

		return direction;
	}

    public void modificaLegameThesauro(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash,SbnUserType sbnUser)
	    throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
    	TermineThesauro termineThesauro = new TermineThesauro();
	    //Soggetto soggetto = new Soggetto();
		//Tb_soggetto tb_soggetto = new Tb_soggetto();
		Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
		//tb_soggetto =  soggetto.cercaSoggettoPerId(leg.getIdArrivo());
		tb_termine_thesauro = termineThesauro.cercaTerminePerId(leg.getIdArrivo());
    	String cd_polo =sbnUser.getBiblioteca().substring(0,3);
    	String cd_biblioteca = sbnUser.getBiblioteca().substring(3,6);
	    Trs_termini_titoli_biblioteche ts = new Trs_termini_titoli_biblioteche();
	    ts.setDID(leg.getIdArrivo());
	    ts.setBID(titolo.getBID());
	    ts.setFL_CANC(" ");
	    ts.setUTE_VAR(titolo.getUTE_VAR());
	    ts.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Trs_termini_titoli_biblioteche", ts.getBID() + ts.getDID())));
        ts.setCD_BIBLIOTECA(cd_biblioteca);
        ts.setCD_POLO(cd_polo);
        //almaviva5_20090302
        ts.setNOTA_TERMINE_TITOLI_BIBLIOTECA(leg.getNoteLegame());
    	if (tb_termine_thesauro != null){
    		ts.setCD_THE(tb_termine_thesauro.getCD_THE());
    	}

	    Trs_termini_titoli_bibliotecheResult tabella = new Trs_termini_titoli_bibliotecheResult(ts);
	    tabella.update(ts);
	}

    public void modificaLegameClasse(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {

        Tr_tit_cla tc = new Tr_tit_cla();
        tc.setBID(titolo.getBID());
        String chiave = leg.getIdArrivo();
        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(chiave);
         //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	tc.setCD_SISTEMA(sd.getSistema() + edizione);
        	tc.setCD_EDIZIONE(edizione);
        } else {
        	tc.setCD_SISTEMA(sd.getSistema());
            tc.setCD_EDIZIONE("  ");
        }
    	tc.setCLASSE(sd.getSimbolo());

        tc.setUTE_VAR(titolo.getUTE_VAR());
        tc.setFL_CANC(" ");
        tc.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_cla", tc.getBID() + chiave)));
        tc.setNOTA_TIT_CLA(leg.getNoteLegame());
        Tr_tit_claResult tabella = new Tr_tit_claResult(tc);
        tabella.update(tc);
    }

    public void modificaLegameLuogo(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(titolo.getBID());
        tl.setLID(leg.getIdArrivo());
        // almaviva HO INSERITO UN CONTROLLO SUL RELATECODE IN QUANTO POTREBBE RITORNARE NULL
        // IN QUESTO CASO VA IN ERRORE
        if(leg.getRelatorCode() != null){
            if (leg.getRelatorCode().trim().length()>0 )  {
            	tl.setTP_LUOGO(leg.getRelatorCode().trim());
            } else 	tl.setTP_LUOGO("P");
        }else{
            tl.setTP_LUOGO("P");
        }
        tl.setFL_CANC(" ");
        tl.setUTE_VAR(titolo.getUTE_VAR());
        tl.setNOTA_TIT_LUO(leg.getNoteLegame());
        tl.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_luo", tl.getBID() + tl.getLID())));
        Tr_tit_luoResult tabella = new Tr_tit_luoResult(tl);
        tabella.update(tl);

    }

    public void modificaLegameRepertorio(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_tit rt = new Tr_rep_tit();
        rt.setBID(titolo.getBID());
        Repertorio repertorio = new Repertorio();
        Tb_repertorio tb_repertorio = repertorio.cercaRepertorioPerCdSig(leg.getIdArrivo());
        rt.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
        rt.setFL_CANC(" ");
        //tipo di legame se è 810 S / altrimenti N o viceversa
        rt.setFL_TROVATO(" "); //??
        rt.setNOTA_REP_TIT(leg.getNoteLegame());
        rt.setUTE_VAR(titolo.getUTE_VAR());
        rt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_rep_tit", rt.getBID() + rt.getID_REPERTORIO())));
        Tr_rep_titResult tabella = new Tr_rep_titResult(rt);
        tabella.update(rt);

    }

    /** Solo per antico
     * @throws InfrastructureException */
    public void modificaLegameMarca(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {

        Tr_tit_mar tm = new Tr_tit_mar();
        tm.setBID(titolo.getBID());
        tm.setFL_CANC(" ");
        tm.setNOTA_TIT_MAR(leg.getNoteLegame());
        tm.setMID(leg.getIdArrivo());
        tm.setUTE_VAR(titolo.getUTE_VAR());
        tm.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_mar", tm.getBID() + tm.getMID())));
        Tr_tit_marResult tabella = new Tr_tit_marResult(tm);
        tabella.update(tm);


    }

    /**
     * Crea un impronta e la inserisce nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void modificaImpronta(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //CREO IMPRONTA
    	//Segnalazione Carla del 10/03/2015:
    	//inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
        C012[] c012 = null;
        if (datiDoc instanceof MusicaType) {
            MusicaType musica = (MusicaType) datiDoc;
            c012 = musica.getT012();
        } else if (datiDoc instanceof AnticoType) {
            AnticoType antico = (AnticoType) datiDoc;
            c012 = antico.getT012();
        } else if (datiDoc instanceof CartograficoType) {
            CartograficoType cartografico = (CartograficoType) datiDoc;
            c012 = cartografico.getT012();
    	} else if (datiDoc instanceof GraficoType) {
    	    GraficoType grafico = (GraficoType) datiDoc;
    	    c012 = grafico.getT012();
        } else
            return;

        Impronta impronta = new Impronta();
        List elenco = impronta.estraiTuttiPerBid(titolo.getBID());
        Tb_impronta tbi;
        for (int i = 0; i < c012.length; i++) {
            flagAll.setTb_impronta(true);
            tbi = creaImpronta(c012[i], titolo);
            boolean esiste = false;
            Tb_impronta impEstratto;
            for (int j = 0; j < elenco.size(); j++) {
                impEstratto = (Tb_impronta) elenco.get(j);
                if (impEstratto.getIMPRONTA_1().equals(tbi.getIMPRONTA_1())
                    && impEstratto.getIMPRONTA_2().equals(tbi.getIMPRONTA_2())
                    && impEstratto.getIMPRONTA_3().equals(tbi.getIMPRONTA_3())) {
                    esiste = true;
                    elenco.remove(j);
                    break;
                }
            }
            if (esiste)
                impronta.update(tbi);
            else
                impronta.inserisci(tbi);
        }
        for (int i = 0; i < elenco.size(); i++) {
            flagAll.setTb_numero_std(true);
            tbi = (Tb_impronta) elenco.get(i);
            tbi.setUTE_VAR(titolo.getUTE_VAR());
            impronta.cancella(tbi);
        }
    }

    public void modificaGrafico(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (!(datiDoc instanceof GraficoType))
            return;
        GraficoType grafico = (GraficoType) datiDoc;
        Tb_grafica tbg = preparaTb_grafica(titolo, grafico);
        Grafica grafica = new Grafica();
        flagAll.setTb_grafica(true);
        Tb_grafica tbm2 = grafica.cercaPerId(titolo.getBID());
        if (tbm2 == null) {
            tbg.setUTE_INS(tbg.getUTE_VAR());
            grafica.inserisci(tbg);
        } else {
            if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbg.getCD_LIVELLO())) {
            	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
            }
            tbg.setTS_VAR(tbm2.getTS_VAR());
            grafica.update(tbg);
        }
    }

    public void modificaCartografico(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!(datiDoc instanceof CartograficoType))
            return;
        CartograficoType grafico = (CartograficoType) datiDoc;
        Tb_cartografia tbg = preparaTb_cartografia(titolo, grafico);
        Cartografia grafica = new Cartografia();
        flagAll.setTb_cartografia(true);
        Tb_cartografia tbm2 = grafica.cercaPerId(titolo.getBID());
        if (tbm2 == null) {
            tbg.setUTE_INS(tbg.getUTE_VAR());
            grafica.inserisci(tbg);
        } else {
            if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbg.getCD_LIVELLO())) {
                throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
            }
         // almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
			// almaviva2 - Novembre 2018 il campo ProiezioneCarte non è obligatorio quindi, nel caso sia vuoto non viene
			// inviato al protocollo il campo c120.setA_120_7
            // si asterisca la riga sottostante
            // tbg.setTP_PROIEZIONE(tbm2.getTP_PROIEZIONE());


            tbg.setTS_VAR(tbm2.getTS_VAR());
            grafica.update(tbg);
        }
    }

    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
    //almaviva4 evolutiva area0
    public void modificaComuni(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

    Tb_titset_1 tbt = preparaTb_titset_1(titolo, datiDoc);
    if (tbt == null) // 02/03/2015 almaviva7
		return;
    Comuni comuni = new Comuni();
    flagAll.setTb_titolo(true);
    Tb_titset_1 tbm2 = comuni.cercaPerId(titolo.getBID());
    if (tbm2 == null) {
        tbt.setUTE_INS(tbt.getUTE_VAR());
        comuni.inserisci(tbt);
    } else {
//        tbt.setTS_VAR(tbm2.getTS_VAR());

    	// almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
    	// è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice
		tbt.setS210_IND_PUBBLICATO(null);
        C210[] c210 = datiDoc.getT210();

        // Copiato da manutenzione di Indice Gennaio 2018 - almaviva2
     // almaviva Modifica test array 210
        if ((datiDoc.getT210() != null)  && (datiDoc.getT210().length > 0)){
        	if (c210[0].getId2() != null){
        		if (c210[0].getId2().equals(IndicatorePubblicato.VALUE_0)){ //valore '1'
					tbt.setS210_IND_PUBBLICATO("1");
        		}
        	}
        }
//        if (c210[0] != null && c210[0].getId2() != null) {
//			if (c210[0].getId2().equals(IndicatorePubblicato.VALUE_0)) //valore '1'
//				tbt.setS210_IND_PUBBLICATO("1");
//        }

        comuni.update(tbt);
    }
}
    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi



  //almaviva4 23/03/2015
    public void modificaComuniDef(String tipoRecord, Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
    Tb_titset_1 tbt = preparaTb_titset_1_def(tipoRecord, titolo, datiDoc);
	if (tbt == null)
		return;

    Comuni comuni = new Comuni();
    flagAll.setTb_titolo(true);
    Tb_titset_1 tbm2 = comuni.cercaPerId(titolo.getBID());
    if (tbm2 == null) {
        tbt.setUTE_INS(tbt.getUTE_VAR());
        comuni.inserisci(tbt);
    } else
        comuni.update(tbt);
}




    // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
    public void modificaAudiovisivo(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
    					throws IllegalArgumentException, InvocationTargetException, Exception {
    	if (!(datiDoc instanceof AudiovisivoType))
    		return;
    	AudiovisivoType audiovisivoType = (AudiovisivoType) datiDoc;
	    if (titolo.getTP_RECORD_UNI().equals("g")) {
	    	Tb_audiovideo tbg = preparaTb_audiovideo(titolo, audiovisivoType);
	    	Audiovisivo audiovisivo = new Audiovisivo();
	    	flagAll.setTb_audiovideo(true);
	    	Tb_audiovideo tbm2 = audiovisivo.cercaPerId(titolo.getBID());
	    	if (tbm2 == null) {
	    		tbg.setUTE_INS(tbg.getUTE_VAR());
	    		audiovisivo.inserisci(tbg);
	    	} else {
	    		if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbg.getCD_LIVELLO())) {
	    			throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
	    		}
	    		tbg.setTS_VAR(tbm2.getTS_VAR());
	    		audiovisivo.update(tbg);

	    		if (audiovisivoType.getT128() != null)
		        	modificaTb_musica_audio(titolo, audiovisivoType);

	    		if (audiovisivoType.getT922() != null) {
	                modificaTb_rappresent_audio(titolo, audiovisivoType);
	                flagAll.setTb_rappresentazione(true);
	            } else {
	                Rappresentazione rapp = new Rappresentazione();
	                if (rapp.cercaPerId(titolo.getBID()) != null) {
	                    rapp.cancella(titolo.getBID(), titolo.getUTE_VAR());
	                    flagAll.setTb_rappresentazione(true);
	                }
	            }
	            if (audiovisivoType.getT927Count() > 0) {
	                Personaggio pers = new Personaggio();
	                pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	                creaTb_personaggio_audio(titolo, audiovisivoType);
	                flagAll.setTb_personaggio(true);
	            } else {
	                Personaggio pers = new Personaggio();
	                if (pers.cercaPerBid(titolo.getBID()).size() > 0) {
	                    pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	                    flagAll.setTb_personaggio(true);
	                }
	            }
	    	}
	    }

	    if (titolo.getTP_RECORD_UNI().equals("i") || titolo.getTP_RECORD_UNI().equals("j") ) {
		    Tb_disco_sonoro tbd = preparaTb_disco_sonoro(titolo, audiovisivoType);
		    Discosonoro disco = new Discosonoro();
		    flagAll.setTb_disco_sonoro(true);
		    Tb_disco_sonoro tbd2 = disco.cercaPerId(titolo.getBID());
		    if (tbd2 == null) {
		        tbd.setUTE_INS(tbd.getUTE_VAR());
		        disco.inserisci(tbd);
		    } else {
		        if (Integer.parseInt(tbd2.getCD_LIVELLO())>Integer.parseInt(tbd.getCD_LIVELLO())) {
		            throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
		        }
		        tbd.setTS_VAR(tbd2.getTS_VAR());
		        disco.update(tbd);

	    		if (audiovisivoType.getT128() != null)
		        	modificaTb_musica_audio(titolo, audiovisivoType);

		        if (audiovisivoType.getT922() != null) {
	                modificaTb_rappresent_audio(titolo, audiovisivoType);
	                flagAll.setTb_rappresentazione(true);
	            } else {
	                Rappresentazione rapp = new Rappresentazione();
	                if (rapp.cercaPerId(titolo.getBID()) != null) {
	                    rapp.cancella(titolo.getBID(), titolo.getUTE_VAR());
	                    flagAll.setTb_rappresentazione(true);
	                }
	            }
	            if (audiovisivoType.getT927Count() > 0) {
	                Personaggio pers = new Personaggio();
	                pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	                creaTb_personaggio_audio(titolo, audiovisivoType);
	                flagAll.setTb_personaggio(true);
	            } else {
	                Personaggio pers = new Personaggio();
	                if (pers.cercaPerBid(titolo.getBID()).size() > 0) {
	                    pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	                    flagAll.setTb_personaggio(true);
	                }
	            }

		    }
	    }
	}

    public void modificaElettronico(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
    if (!(datiDoc instanceof ElettronicoType))
        return;
	    ElettronicoType elettronicoType = (ElettronicoType) datiDoc;
	    if (titolo.getTP_RECORD_UNI().equals("l")) {
	    	Tb_risorsa_elettr tbg = preparaTb_risorsa_elettr(titolo, elettronicoType);
	    	Elettronico elettronico = new Elettronico();
	    	flagAll.setTb_risorsa_elettr(true);
	    	Tb_risorsa_elettr tbm2 = elettronico.cercaPerId(titolo.getBID());
	    	if (tbm2 == null) {
	    		tbg.setUTE_INS(tbg.getUTE_VAR());
	    		elettronico.inserisci(tbg);
	    	} else {
	    		if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbg.getCD_LIVELLO())) {
	    			throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
	    		}
	    		tbg.setTS_VAR(tbm2.getTS_VAR());
	    		elettronico.update(tbg);
	    	}
	    }
    }

    // Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro




    /**
     * Crea la musica legata ad un documento musicale
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void modificaMusica(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!(datiDoc instanceof MusicaType))
            return;
        MusicaType musicaType = (MusicaType) datiDoc;
        modificaTb_musica(titolo, musicaType);
        flagAll.setTb_musica(true);
        if (musicaType.getT922() != null) {
            modificaTb_rappresent(titolo, musicaType);
            flagAll.setTb_rappresentazione(true);
        } else {
            Rappresentazione rapp = new Rappresentazione();
            if (rapp.cercaPerId(titolo.getBID()) != null) {
                rapp.cancella(titolo.getBID(), titolo.getUTE_VAR());
                flagAll.setTb_rappresentazione(true);
            }
        }
        if (musicaType.getT926Count() > 0) {
        	// Inizio modifica almaviva2 03.05.2010 - BUG MANTIS 3729 la gestione viene adeguata a quella dei personaggi
        	// cioè vengono prima cancellati tutti i legami eistenti e poi reinseriti solo quelli inviati dell'xml
        	// così da gestire le cancellazioni dei sngoli Incipit
//            modificaTb_incipit(titolo, musicaType);
//            flagAll.setTb_incipit(true);
        	Incipit incipit = new Incipit();
        	incipit.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
        	modificaTb_incipit(titolo, musicaType);
        	flagAll.setTb_incipit(true);
        	// Fine modifica almaviva2 03.05.2010 - BUG MANTIS 3729 la gestione viene adeguata a quella dei personaggi
        } else {
            Incipit incipit = new Incipit();
            if (incipit.cercaPerBid(titolo.getBID()) != null) {
                incipit.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
                flagAll.setTb_incipit(true);
            }
        }
        if (musicaType.getT927Count() > 0) {
            Personaggio pers = new Personaggio();
            pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
            creaTb_personaggio(titolo, musicaType);
            flagAll.setTb_personaggio(true);
        } else {
            Personaggio pers = new Personaggio();
            if (pers.cercaPerBid(titolo.getBID()).size() > 0) {
                pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
                flagAll.setTb_personaggio(true);
            }
        }
    }
    /**
     * Crea la musica legata ad un documento musicale
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void modificaTb_musica(Tb_titolo titolo, MusicaType musicaType) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_musica tbm = preparaTb_musica(titolo, musicaType);
        Musica musicaDB = new Musica();
        Tb_musica tbm2 = musicaDB.verificaEsistenza(titolo.getBID());
        if (tbm2 == null) {
            tbm.setUTE_INS(tbm.getUTE_VAR());
            musicaDB.inserisci(tbm);
        } else {
            if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbm.getCD_LIVELLO())) {
                throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
            }
            musicaDB.eseguiUpdate(tbm);
        }
    }

    /**
     * Crea la musica legata ad un documento musicale
     * @throws Exception
     * @throws InfrastructureException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void modificaTb_incipit(Tb_titolo titolo, MusicaType musicaType) throws IllegalArgumentException, InvocationTargetException, InfrastructureException, Exception {
        Incipit incipitDB = new Incipit();
        C926[] c926 = musicaType.getT926();
        for (int i = 0; i < c926.length; i++) {
            Tb_incipit tbi = preparaTb_incipit(titolo, c926[i]);
            if (incipitDB.cercaEsistenza(tbi) != null)
                incipitDB.update(tbi);
            else
                incipitDB.inserisci(tbi);
        }
    }

    protected void modificaTb_rappresent(Tb_titolo titolo, MusicaType musica)
        throws IllegalArgumentException, InvocationTargetException, InfrastructureException, Exception {
        Tb_rappresent tbr = preparaTb_rappresent(titolo, musica);
        Rappresentazione rappresentazione = new Rappresentazione();
        if (rappresentazione.cercaEsistenza(titolo.getBID()) == null)
            rappresentazione.inserisci(tbr);
        else
            rappresentazione.update(tbr);
    }

    // Inizio almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
    protected void modificaTb_musica_audio(Tb_titolo titolo, AudiovisivoType audiovisivoType) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_musica tbm = preparaTb_musica_audio(titolo, audiovisivoType);
        Musica musicaDB = new Musica();
        Tb_musica tbm2 = musicaDB.verificaEsistenza(titolo.getBID());
        if (tbm2 == null) {
            tbm.setUTE_INS(tbm.getUTE_VAR());
            musicaDB.inserisci(tbm);
        } else {
            if (Integer.parseInt(tbm2.getCD_LIVELLO())>Integer.parseInt(tbm.getCD_LIVELLO())) {
                throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore");
            }
            musicaDB.eseguiUpdate(tbm);
        }
    }

    /**
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws InfrastructureException
     */
    protected void modificaTb_rappresent_audio(Tb_titolo titolo, AudiovisivoType grafico)
        throws InfrastructureException, IllegalArgumentException, InvocationTargetException, Exception {
        Tb_rappresent tbr = preparaTb_rappresent_audio(titolo, grafico);
        Rappresentazione rappresentazione = new Rappresentazione();
        if (rappresentazione.cercaEsistenza(titolo.getBID()) == null)
            rappresentazione.inserisci(tbr);
        else
            rappresentazione.update(tbr);
    }

    // Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro





    public void modificaNumeroStandard(NumStdType[] numStd, Tb_titolo titolo,
    									AllineamentoTitolo flagAll,
										BigDecimal versione)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        NumeroStd numeroDB = new NumeroStd();
        Tb_numero_std numero;
        List elenco = numeroDB.estraiTuttiPerBid(titolo.getBID());
        for (int i = 0; i < numStd.length; i++) {
            flagAll.setTb_numero_std(true);
            numero = new Tb_numero_std();
            numero.setNOTA_NUMERO_STD(numStd[i].getNotaSTD());
            numero.setBID(titolo.getBID());
            numero.setCD_PAESE(numStd[i].getPaeseSTD());
            numero.setFL_CANC(" ");
            numero.setUTE_VAR(titolo.getUTE_VAR());
            numero.setUTE_INS(titolo.getUTE_VAR());
            numero.setTP_NUMERO_STD(
                Decodificatore.getCd_tabella(
                    "Tb_numero_std",
                    "tp_numero_std",
                    numStd[i].getTipoSTD().toString()));
            if (numero.getTP_NUMERO_STD().trim().equals("I") || numero.getTP_NUMERO_STD().trim().equals("J"))
                numero.setNUMERO_STD(rimuoviTrattini(numStd[i].getNumeroSTD()));
            else
                numero.setNUMERO_STD(numStd[i].getNumeroSTD());
            if (numero.getTP_NUMERO_STD().trim().equals("L") || numero.getTP_NUMERO_STD().trim().equals("E"))
                numero.setNUMERO_LASTRA(getNumeroLastra(numero.getNUMERO_STD().trim()));
            boolean esiste = false;
            Tb_numero_std numEstratto;
            for (int j = 0; j < elenco.size(); j++) {
                numEstratto = (Tb_numero_std) elenco.get(j);
                if (numEstratto.getTP_NUMERO_STD().trim().equals(numero.getTP_NUMERO_STD().trim())
                    && numEstratto.getNUMERO_STD().trim().equals(numero.getNUMERO_STD().trim())) {
                    esiste = true;
                    elenco.remove(j);
                    break;
                }
            }
            if (esiste)
                numeroDB.update(numero);
            else
                numeroDB.inserisci(numero);
        }
        for (int i = 0; i < elenco.size(); i++) {
            flagAll.setTb_numero_std(true);
            numero = (Tb_numero_std) elenco.get(i);
            if ("U".equals(numero.getTP_NUMERO_STD().trim()) == false ||
            		("U".equals(numero.getTP_NUMERO_STD().trim()) == true
            				&& (versione.compareTo(new BigDecimal(1.09)) > 0 ))) {
                numero.setUTE_VAR(titolo.getUTE_VAR());
                numeroDB.cancella(numero);
            }
        }
    }
    /**
     * Qualora vengano modificate le chiavi del titolo si deve verificare se
     * esistono W collegati al titolo (Accedendo alla Vl_titolo_tit_c con natura base = W)
     * le chiavi nuove vanno modificate anche nei W
     * Quindi si deve impostare il flag di allineamento come se fosse una modifica.
     * @param titolo il titolo modificato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaChiaviLegate(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloTitolo tit_tit = new TitoloTitolo();
        List v = tit_tit.cercaLegamiPerBidCollNaturaColl(titolo.getBID(), "W");
        //Per ogni elemento devo modificare il titolo
        for (int i = 0; i < v.size(); i++) {
            Vl_titolo_tit_c tit_leg = (Vl_titolo_tit_c) v.get(i);
            tit_leg.setKY_CLES1_CT(titolo.getKY_CLES1_CT());
            tit_leg.setKY_CLES2_CT(titolo.getKY_CLES2_CT());
            tit_leg.setKY_CLES1_T(titolo.getKY_CLES1_T());
            tit_leg.setKY_CLES2_T(titolo.getKY_CLES2_T());
            tit_leg.setKY_CLET1_CT(titolo.getKY_CLET1_CT());
            tit_leg.setKY_CLET2_CT(titolo.getKY_CLET2_CT());
            tit_leg.setKY_CLET1_T(titolo.getKY_CLET1_T());
            tit_leg.setKY_CLET2_T(titolo.getKY_CLET2_T());
            tit_leg.setUTE_VAR(titolo.getUTE_VAR());
            //Aggiorno il titolo su db
            updateChiaviEIsbd(tit_leg);
            AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(tit_leg);
            flagAllineamento.setTb_titolo(true);
            //Aggiorno i flag di allineamento
            new TitoloGestisceAllineamento().aggiornaFlagAllineamento(
                flagAllineamento,
                titolo.getUTE_VAR());
        }
    }

    public void modificaNatura(Tb_titolo titolo, AllineamentoTitolo flagAllineamento) throws IllegalArgumentException, InvocationTargetException, Exception {
      Tb_titoloResult tavola = new Tb_titoloResult(titolo);
      tavola.executeCustomUpdate("updateNatura");
      flagAllineamento.setNatura(true);
      flagAllineamento.setTp_legame(true);

      //Quindi deve modificare tutti i tr_tit_tit con bid_coll = bid.
      TitoloTitolo tt= new TitoloTitolo();
      if (titolo.getCD_NATURA().equals("A")) {
        tt.aggiornaLinkModificaNatura(titolo,"09");
      } else {
        tt.aggiornaLinkModificaNatura(titolo,"06");
      }
    }

    // Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
    // viene esteso anche al Materiale Moderno e Antico
    public void modificaPersRapp(Tb_titolo titolo, DatiDocType datiDoc, AllineamentoTitolo flagAll)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    if (!(datiDoc instanceof AnticoType) && !(datiDoc instanceof ModernoType)) {
    	return;
    }
    if (datiDoc instanceof AnticoType) {
	    AnticoType anticoType = (AnticoType) datiDoc;
	    flagAll.setTb_musica(true);
	    if (anticoType.getT922() != null) {
	        modificaTb_rappresentAntico(titolo, anticoType);
	        flagAll.setTb_rappresentazione(true);
	    } else {
	        Rappresentazione rapp = new Rappresentazione();
	        if (rapp.cercaPerId(titolo.getBID()) != null) {
	            rapp.cancella(titolo.getBID(), titolo.getUTE_VAR());
	            flagAll.setTb_rappresentazione(true);
	        }
	    }
	    if (anticoType.getT927Count() > 0) {
	        Personaggio pers = new Personaggio();
	        pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	        creaTb_personaggioAntico(titolo, anticoType);
	        flagAll.setTb_personaggio(true);
	    } else {
	        Personaggio pers = new Personaggio();
	        if (pers.cercaPerBid(titolo.getBID()).size() > 0) {
	            pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	            flagAll.setTb_personaggio(true);
	        }
	    }
	    }
    if (datiDoc instanceof ModernoType) {
    	ModernoType modernoType = (ModernoType) datiDoc;
	    flagAll.setTb_musica(true);
	    if (modernoType.getT922() != null) {
	        modificaTb_rappresentModerno(titolo, modernoType);
	        flagAll.setTb_rappresentazione(true);
	    } else {
	        Rappresentazione rapp = new Rappresentazione();
	        if (rapp.cercaPerId(titolo.getBID()) != null) {
	            rapp.cancella(titolo.getBID(), titolo.getUTE_VAR());
	            flagAll.setTb_rappresentazione(true);
	        }
	    }
	    if (modernoType.getT927Count() > 0) {
	        Personaggio pers = new Personaggio();
	        pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	        creaTb_personaggioModerno(titolo, modernoType);
	        flagAll.setTb_personaggio(true);
	    } else {
	        Personaggio pers = new Personaggio();
	        if (pers.cercaPerBid(titolo.getBID()).size() > 0) {
	            pers.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
	            flagAll.setTb_personaggio(true);
	        }
	    }
	    }
    }

    protected void modificaTb_rappresentAntico(Tb_titolo titolo, AnticoType antico)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    Tb_rappresent tbr = preparaTb_rappresentAntico(titolo, antico);
    Rappresentazione rappresentazione = new Rappresentazione();
    if (rappresentazione.cercaEsistenza(titolo.getBID()) == null)
        rappresentazione.inserisci(tbr);
    else
        rappresentazione.update(tbr);
    }

    protected void modificaTb_rappresentModerno(Tb_titolo titolo, ModernoType moderno)
    throws InfrastructureException, IllegalArgumentException, InvocationTargetException, Exception {
    Tb_rappresent tbr = preparaTb_rappresentModerno(titolo, moderno);
    Rappresentazione rappresentazione = new Rappresentazione();
    if (rappresentazione.cercaEsistenza(titolo.getBID()) == null)
        rappresentazione.inserisci(tbr);
    else
        rappresentazione.update(tbr);
    }

}


