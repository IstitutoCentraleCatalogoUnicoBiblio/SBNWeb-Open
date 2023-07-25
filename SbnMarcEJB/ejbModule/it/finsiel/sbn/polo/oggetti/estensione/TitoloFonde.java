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

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.isFilled;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Audiovisivo;
import it.finsiel.sbn.polo.oggetti.Cartografia;
import it.finsiel.sbn.polo.oggetti.Composizione;
import it.finsiel.sbn.polo.oggetti.Discosonoro;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Incipit;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.Nota;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.oggetti.TitoloTitolo;
import it.finsiel.sbn.polo.orm.Tb_audiovideo;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.polo.orm.Tb_composizione;
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_incipit;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_nota;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * Classe TitoloFonde
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 7-mag-03
 */
public class TitoloFonde extends Titolo {


	private static final long serialVersionUID = 7116404822950005461L;
	// INIZIO Bug 6005 mantis esercizio:OTTOBRE 2015
	protected String tp_materiale1 = null;
	protected String tp_materiale2 = null;



	protected static Logger log = Logger.getLogger(TitoloFonde.class);

    FondeType fonde;
    static int maxSpostamenti = Integer.parseInt(ResourceLoader.getPropertyString("NRO_MAX_SPOSTAMENTO_LEGAMI"));

    public TitoloFonde(FondeType fondeType) {
        super();
        this.fonde = fondeType;
    }

    /**
     * Valida e fonde, perchï¿½ la fusione viene eseguita insieme alla validazione.
     * . i due titoli idPartenza e idArrivo devono esistere in tb_titolo,  devono avere natura uguale,
     * altrimenti si segnala diagnostico ' i bid indicati non sono congruenti'.
     *
     * Se si tratta di spostamento legami (presenza di spostaId):
     * . verifica che il numero di elementi non sia maggiore di un valore massimo gestito in un property
     * di sistema, altrimenti restituisce un diagnostico: 'troppi identificativi comunicati: spezzare la
     *  richiesta in parti'
     * . per ogni spostaId: verifica esistenza tr_tit_tit con bid_base=spostaId e bid_coll=idPartenza, se
     *  non esiste interrompe il servizio e segnala diagnostico 'legame inesistente' comunicando spostaId
     * nel testo diagnostico;
     * verifica esistenza tr_tit_tit con bid_base=spostaId e bid_arrivo=idArrivo, se esiste interrompe il
     *  servizio e segnala diagnostico 'legame giï¿½ esistente' comunicando spostaId nel testo diagnostico.
     *
     * Se spostaId non c'ï¿½ si tratta di fusione: si leggono i legami tr_tit_tit con bid_coll=idPartenza, si
     * verifica che il numero di legami non sia maggiore di un valore massimo gestito in un property di
     * sistema, altrimenti restituisce un diagnostico: 'troppi legami: spezzare la richiesta in parti'
     * pe ogni legame tr_tit_tit: verifica esistenza tr_tit_tit con bid_base=bid e bid_coll=idArrivo, se
     * esiste interrompe il servizio e segnala diagnostico 'legame giï¿½ esistente' comunicando bid nel testo
     * diagnostico.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_titolo eseguiFusione(String user, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit1 = verificaEsistenzaTitolo(fonde.getIdPartenza());
        Tb_titolo tit2 = verificaEsistenzaTitolo(fonde.getIdArrivo());
        if (tit1 == null || tit2 == null)
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        verificaLocalizzazioniCancellazione(tit1, tit2, user);
        TitoloValida tv = new TitoloValida();
        tv.verificaAllineamentoModificaTitolo(tit1.getBID());
        tv.verificaAllineamentoModificaTitolo(tit2.getBID());
        //        if (tit1.getCD_LIVELLO().equals("90")
        //            || tit1.getCD_LIVELLO().equals("97")
        //            || tit1.getCD_LIVELLO().equals("95"))
        //            if (!user.equals(tit1.getUTE_VAR()))
        //                throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
        String cd_natura = tit1.getCD_NATURA();
        String cd_natura2 = tit2.getCD_NATURA();
        int numSp = fonde.getSpostaIDCount();

        // INIZIO Bug 6005 mantis esercizio:OTTOBRE 2015:  a seguito dell'allineamento di un titolo di tipo materiale antico "E"
        // su un titolo di qualsiasi altro materiale si ricevono Protocollo di POLO: 3280 Fusione non
        // consentita tra questi materiali - fusione non consentita tra moderno e antico
        // Viene aggiornato il software con il corrispondente di Indice

        // inizio Parte vecchia ante correzione Bug 6005 che viene asteriscata
        /*if (!((cd_natura.equals("A") || cd_natura.equals("B"))
            && (cd_natura2.equals("A") || cd_natura2.equals("B")))
            && !((cd_natura.equals("M") || cd_natura.equals("W"))
                && (cd_natura2.equals("M") || cd_natura2.equals("W")))
            && !(numSp > 0
                && (cd_natura.equals("M") || cd_natura.equals("C"))
                && (cd_natura2.equals("M") || cd_natura2.equals("C")))
            && !cd_natura.equals(cd_natura2) ) {
            throw new EccezioneSbnDiagnostico(3150, "I bid indicati non sono congruenti");
        }
        // almaviva QUI DOVREBBE SWITCHARE PER NATURA "A" E "B"  SOLO CONTROLLO SOLO SU CLES1  TUTTO PER LE ALTRE NATURE
        // MANTIS 0002491
        if (ValidatorProfiler.getInstance().isPolo(user) && controlloCartaId(tit1, tit2) == false) {
          throw new EccezioneSbnDiagnostico(3142, "Carta identità diversa");
        }


        if (cd_natura.equals("M") || cd_natura.equals("W")) {
        	if (!tit1.getTP_MATERIALE().equals(tit2.getTP_MATERIALE())) {
        		if (tit1.getTP_MATERIALE().equals("E") || tit2.getTP_MATERIALE().equals("E")){
        			throw new EccezioneSbnDiagnostico(3280, "fusione non consentita tra moderno e antico");
        		}
            }
        }
        */
        // fine Parte vecchia ante correzione Bug 6005 che viene asteriscata

        // inizio Parte NUOVA Bug 6005 che viene copiata

        if (!((cd_natura.equals("M") || cd_natura.equals("W"))
      		  && (cd_natura2.equals("M") || cd_natura2.equals("W")))
      	  && !(numSp > 0
      		  && (cd_natura.equals("M") || cd_natura.equals("C"))
      		  && (cd_natura2.equals("M") || cd_natura2.equals("C")))
      	  && !cd_natura.equals(cd_natura2) ) {
      	  throw new EccezioneSbnDiagnostico(3150, "I bid indicati non sono congruenti");
        }
      //almaviva4 fine 30/07/2010

        /*
      //almaviva4 30/10/2014 impedire fusione tra materiali diversi
      G -> solo G
      C -> solo C
      H -> solo H
      L -> solo L
      U -> solo U, H
      E -> tutto tranne M
      M -> tutto tranne E
      //almaviva4 evolutiva fusione discoteca 05/2015
      la fusione tra materiali diversi viene consentita (non per tutti i materiali)
      con il trascinamento dei legami di specializzazione dal bid di partenza a quello di arrivo
      */
      //  String tp_materiale1 = tit1.getTp_materiale();
      //  String tp_materiale2 = tit2.getTp_materiale();


		// Settembre 2018 almaviva2 - problema: in fase di variazione descrizione titolo effettuata in INDICE, se si varia la data
        // in modo che cambi automaticamente anche il tipo materiale (da Moderno ad Antico e viceversa) la fusione dei titoli resi
		// uguali in Indice va a buon fine. Al momento dello allineamento però viene bloccata in Polo perchè la fusione
        // (non essendo preceduta dalla variazione dell'oggetto di partenza) trova i tipi materiali , le date ed i tipo record
        // diversi. Si steriscano tutti i controlli così che anche l'allineamento vada a buon fine
        // INIZIO MODIFICA Settembre 2018 almaviva2


//        tp_materiale1 = tit1.getTP_MATERIALE();
//        tp_materiale2 = tit2.getTP_MATERIALE();
//        if (tp_materiale1.equals("M") && tp_materiale2.equals("E")) {
//      	  throw new EccezioneSbnDiagnostico(3150); //"I bid indicati non sono congruenti");
//        }
//        if (tp_materiale1.equals("E") && tp_materiale2.equals("M")) {
//      	  throw new EccezioneSbnDiagnostico(3150); //"I bid indicati non sono congruenti");
//        }

      //controllo congruenza tra tipo record
//        String tp_record1 = tit1.getTP_RECORD_UNI();
//        String tp_record2 = tit2.getTP_RECORD_UNI();
//        // Modifica adeguativa all'Indice per coprire casistica di tp_record = null - 07 gennaio 2016 almaviva2
//        if (tp_record1 != null && tp_record2 != null) {
//      	  if (!tp_record1.equals(tp_record2)) {
//      	  			throw new EccezioneSbnDiagnostico(3150); //"I bid indicati non sono congruenti");
//      	  }
//        }


      //14/09/2015 controllo congruenza tra date pubblicazione
//      	String data1 = tit1.getAA_PUBB_1();
//      	String data2 = tit2.getAA_PUBB_1();
//
//      	// almaviva2 - Marzo 2018 - controllo inserito per assegnare il valore null alle date (per la precisione anno)
//      	// nel caso siano uguali a "0000" o "    " altrimenti la fusione non va a buon fine
//      	// L'intervento in Indice è fatto in maniera diversa ma si deve ottenere lo stesso risultato
//      	if (data1 != null) {
//      		if ((data1.equals("0000") == true) || (data1.equals("    ") == true)) {
//          		data1 = null;
//          	}
//      	}
//
//    	if (data2 != null) {
//    		if ((data2.equals("0000") == true) || (data2.equals("    ") == true)) {
//          		data2 = null;
//          	}
//    	}
//
//
//      	if (data1 != null && data2 != null) {
//      		data1 = data1.replace(".","0");
//      		data2 = data2.replace(".","0");
//      		if (data1.compareTo("1831") < 0 && data2.compareTo("1831") >= 0)
//      	        throw new EccezioneSbnDiagnostico(3280); //Fusione non consentita per incongruenza tra le date dei due titoli
//      		if (data2.compareTo("1831") < 0 && data1.compareTo("1831") >= 0)
//      	        throw new EccezioneSbnDiagnostico(3280); //Fusione non consentita per incongruenza tra le date dei due titoli
//      	}

     // FINE MODIFICA Settembre 2018 almaviva2


      //14/09/2015 controllo congruenza tra date pubblicazione fine

        	if (ValidatorProfiler.getInstance().isPolo(user) && controlloCartaId(tit1, tit2) == false) {
                throw new EccezioneSbnDiagnostico(3142, "Carta identità diversa");
              }
              //se un titolo ha natura M o W e il tipo materiale è M o E il tipo materiale dell'altro
              // titolo deve essere uguale al primo
              if (cd_natura.equals("M") || cd_natura.equals("W")) {
                if (!tit1.getTP_MATERIALE().equals(tit2.getTP_MATERIALE())) {
                    if (tit1.getTP_MATERIALE().equals("E") || tit2.getTP_MATERIALE().equals("E")){
//                        throw new EccezioneSbnDiagnostico(3280, "fusione non consentita tra moderno e antico");
                  } else if (tit1.getTP_MATERIALE().equals("M")){
                  } else {
                    if (!ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().MODIFICA_TIPO_MATERIALE_DOCUMENTO_1020)) {
                      throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
                    }
                  }
                }
              }

      // bug mantis 4980 (sbnweb) - evolutva 15/05/2012
      // ai poli è consentito fondere W su M o viceversa solo se entrambi i titoli hanno lo stesso padre,
      // a interfaccia diretta è sempre consentito

        if (ValidatorProfiler.getInstance().isPolo(user)) {

      	  if (
      	  	(cd_natura.equals("M") || cd_natura.equals("W"))
      	  	 &&
      	  	 (cd_natura2.equals("M") || cd_natura2.equals("W"))
      	  	 && !cd_natura.equals(cd_natura2)
      	  	) {
      		Tr_tit_tit tt2;

      		String padreTitPartenza = null;
      		String padreTitArrivo = null;
      		tt2 = null;

      		List legamePadrePartenza = new TitoloTitolo().cercaPadrePerBid(fonde.getIdPartenza());
      		if (!legamePadrePartenza.isEmpty()) {
      			tt2 = (Tr_tit_tit) legamePadrePartenza.get(0);
      			padreTitPartenza = tt2.getBID_COLL();
      		}

      		List legamePadreArrivo = new TitoloTitolo().cercaPadrePerBid(fonde.getIdArrivo());
      		tt2 = null;
      		if (!legamePadreArrivo.isEmpty()) {
      			tt2 = (Tr_tit_tit) legamePadreArrivo.get(0);
      			padreTitArrivo = tt2.getBID_COLL();
      		}

      		if (padreTitPartenza != null && padreTitArrivo != null) {

      			if (padreTitPartenza.equals(padreTitArrivo) == true) {

      				if (cd_natura.equals("M")) {

      					TitoloTitolo ttDB = new TitoloTitolo();
      					List legami = ttDB.cercaLegamiPerBidColl(fonde.getIdPartenza(),null);
      					if (legami.size() > 0)
      						throw new EccezioneSbnDiagnostico(3145, "Fusione non consentita: il titolo da fondere non deve avere figli");
      				}
      			} //end if padri uguali
      			else
      			{
      			  throw new EccezioneSbnDiagnostico(3144, "Fusione non consentita: i due titoli devono avere stesso padre");
      			}
      		} // end if entrambi i padri diversi da null
      		else
      		{
      		  throw new EccezioneSbnDiagnostico(3144, "Fusione non consentita: i due titoli devono avere stesso padre");
      		}
      	  }
        }	//end if si tratta di un polo
      //bug mantis 4980 (sbnweb) - evolutva 15/05/2012 fine

        // fine Parte NUOVA Bug 6005 che viene copiata
        // FINE Bug 6005 mantis esercizio:OTTOBRE 2015



        boolean fondeConLink = false;
        boolean raggruppamento = false;
        if (cd_natura.equals("C")
            || cd_natura.equals("B")
            || cd_natura.equals("P")
            || cd_natura.equals("D")) {
            raggruppamento = true;
        } else if (cd_natura.equals("M") || cd_natura.equals("S") || cd_natura.equals("W")) {
            //verifica legami verso il basso.(count con bid_coll = bid di partenza)
            TitoloTitolo tt = new TitoloTitolo();
            if (tt.contaLegamiPerBidColl(fonde.getIdPartenza()) > 0) {
                raggruppamento = true;
            }
        }

        if (raggruppamento) {
            if (!ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200)) {
                if (ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201))
                    fondeConLink = true;
                else
                    throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
            }
        } else {
            if (!ValidatorProfiler.getInstance().verificaAttivitaID(user, CodiciAttivita.getIstance().FONDE_DOCUMENTI_1024))
                throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
        }

        timeHash.putTimestamp("Tb_titolo", tit1.getBID(), new SbnDatavar( tit1.getTS_VAR()));
        timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));

        if (numSp > 0) {
            //ESEGUO LO SPOSTAMENTO
            if (numSp > maxSpostamenti)
                throw new EccezioneSbnDiagnostico(
                    3140,
                    "Troppi identificativi comunicati: spezzare la richiesta in parti");
            sposta(user, cd_natura2, timeHash);
        } else {
            if (fondeConLink)
                fondeConLink(tit1, tit2, user, timeHash);
            else {
                fonde(tit1, user, timeHash);
            }
        }
        return estraiTitoloPerID(tit2.getBID());
    }
    private boolean confrontaOggetti (Object st1, Object st2) {
      if (st1 == null && st2 == null) {
        return true;
      }
      if (st1 == null || st2 == null) {
        return false;
      }
      return st1.equals(st2);
    }

    protected boolean controlloCartaId(Tb_titolo tit1, Tb_titolo tit2) {
      if (tit1.getKY_CLES1_T().equals(tit2.getKY_CLES1_T()) == false) {
        return false;
      }

      if (confrontaOggetti(tit1.getKY_CLES2_T(), tit2.getKY_CLES2_T()) == false) {
        return false;
      }
      if (confrontaOggetti(tit1.getKY_CLET1_T(), tit2.getKY_CLET1_T()) == false) {
        return false;
      }
      if (confrontaOggetti(tit1.getKY_CLET2_T(), tit2.getKY_CLET2_T()) == false) {
        return false;
      }
      if (confrontaOggetti(tit1.getCD_PAESE(), tit2.getCD_PAESE()) == false) {
        return false;
      }
      if (confrontaOggetti(tit1.getTP_AA_PUBB(), tit2.getTP_AA_PUBB()) == false) {
        return false;
      }
//      if (confrontaOggetti(tit1.getAA_PUBB_1(), tit2.getAA_PUBB_1()) == false) {
//        return false;
//      }
        // MANTIS 0002491 Fusione nature A,B,D,P
        if (tit1.getAA_PUBB_1() != null && tit1.getAA_PUBB_1().toString().equals("0000"))
          tit1.setAA_PUBB_1(null);
        if (tit2.getAA_PUBB_1() != null && tit2.getAA_PUBB_1().toString().equals("0000"))
          tit2.setAA_PUBB_1(null);
        if (confrontaOggetti(tit1.getAA_PUBB_1(), tit2.getAA_PUBB_1()) == false) {
          return false;
        }


      // LE NATURE A NON DEVONO AVERE IL CONTROLLO SULLA CARTA D'IDENTITA SULLA LINGUA
      if(!tit1.getCD_NATURA().equals("A")){
	      if (confrontaOggetti(tit1.getCD_LINGUA_1(), tit2.getCD_LINGUA_1()) == false) {
	        return false;
	      }
      }
      return true;
    }
    /**
     * metodo che verifica se il titolo ï¿½ localizzato presso il polo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void verificaLocalizzazioniCancellazione(
        Tb_titolo tb_titolo1,
        Tb_titolo tb_titolo2,
        String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        if (!titBib.verificaLocalizzazioniPoloUguale(tb_titolo1.getBID(), utente.substring(0, 6)))
            throw new EccezioneSbnDiagnostico(3115, "Titolo non localizzato nel polo");

        //        if (titBib.verificaLocalizzazioniPoloDiverso(tb_titolo1.getBID(), utente))
        //            throw new EccezioneSbnDiagnostico(
        //                3092,
        //                "Titolo localizzato in altri poli, cancellazione impossibile");
        //        if (titBib.verificaLocalizzazioniPoloUguale(tb_titolo2.getBID(), utente.substring(0, 3)))
        //            throw new EccezioneSbnDiagnostico(3115, "Titolo non localizzato nel polo");
        //
        //        if (titBib.verificaLocalizzazioniPoloDiverso(tb_titolo2.getBID(), utente))
        //            throw new EccezioneSbnDiagnostico(
        //                3092,
        //                "Titolo localizzato in altri poli, cancellazione impossibile");
    }

    protected Tb_titolo verificaEsistenzaTitolo(String id) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_titolo tit = estraiTitoloPerID(id);
        if (tit == null) {
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3013, "ID non esistente in base dati");
            ecc.appendMessaggio("- id : " + id);
            throw ecc;
        }
        return tit;
    }

    /**
     * Esegue la fusione.
     * Modifica tr_tit_tit con bid_coll= idArrivo, modifica tb_titolo.ts_var e ute_var
     *  con bid=spostaId, aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_tit_bib con bid=spostaID
     * (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
     *
     *
     * Quando idPartenza non ha piï¿½ legami tr_tit_tit  si spostano i legami tr_tit_aut, tr_tit_tit,
     * tr_tit_luo,tr_tit_sogg,  tr_tit_cla, ecc. con bid_base=idPartenza da idPartenza a idArrivo (update
     * sui legami)
     *
     * Per tutti i legame si controlla che non esista giï¿½ la stessa relazione con idArrivo, nel qual caso
     *  non si esegue nessuna operazione.
     * Al termine dell'elaborazione sui legami si cancella il titolo idPartenza e i suoi legami ad eccezione
     * dei legami tr_tit_bib, in cui si aggiorna fl_allinea (chiama il metodo 'aggiornaFlAllinea' di
     * TitoloBiblioteca con bid=idPartenza).
     *
     *
     * In pratica crea i legami tr_tit_tit e tr_tit_aut
     * quindi cancella il titolo eliminato
     *
     * @param user stringa dell'utente
     * @param legami vettore di legami tr_tit_tit
     * @param timeHash contiene i timestamp degli oggetti letti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void fonde(Tb_titolo tit, String user, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        Tr_tit_tit tt;
        TitoloTitolo ttDB = new TitoloTitolo();
        List legami = new TitoloTitolo().cercaLegamiPerBidColl(fonde.getIdPartenza(),null);
        if (legami.size() > maxSpostamenti)
            throw new EccezioneSbnDiagnostico(
                3141,
                "Troppi identificativi comunicati: spezzare la richiesta in parti");

        for (int i = 0; i < legami.size(); i++) {
            tt = (Tr_tit_tit) legami.get(i);

            ttDB.updateCancella(user, tt.getBID_BASE(), fonde.getIdPartenza());
            //Devo settare i flag di allineamento del titolo con bid tt.getBid_base()
            final Tb_titolo titoloBase = new Titolo().estraiTitoloPerID(tt.getBID_BASE());
            if (titoloBase == null) {
            	throw new EccezioneSbnDiagnostico(3233, "Esiste un legame attivo al titolo cancellato (" + tt.getBID_BASE() + ")");
            }
			AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(titoloBase);
            allineamentoTitolo.setTr_tit_tit(true);
            TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
            titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);

            if (estraiLegame(tt.getBID_BASE(), fonde.getIdArrivo()) != null) {
                // EccezioneSbnDiagnostico ecc = new
                // EccezioneSbnDiagnostico(3030, "Legame
                // gia' esistente");
                // ecc.appendMessaggio(",bid = " + tt.getBid_coll());
                // throw ecc;
            } else {
                // Creo la nuova versione di tr_tit_tit
                tt.setBID_COLL(fonde.getIdArrivo());
                tt.setUTE_VAR(user);
                ttDB.inserisciLegame(tt);
            }
        }
        spostaElementiLegati(tit, user);
        //TitoloCancella titolo = new TitoloCancella();
        //titolo.cancellaElementiLegati(tit); Lo faccio dentro a sposta.
        tit.setUTE_VAR(user);

        TitoloCancella titolo = new TitoloCancella();
        titolo.cancellaElementiLegati(tit);
        tit.setBID_LINK(fonde.getIdArrivo());
        tit.setTP_LINK("F");
        titolo.cancellaPerFusione(tit);
        //Aggiorno la versione
        titolo.updateVersione(fonde.getIdArrivo(), user);

        //Cancello le localizzazioni del bid di partenza
        titBib.cancellaPerBid(fonde.getIdPartenza(), user);

        AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tit);
        allineamentoTitolo.setTb_titolo(true);
        TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
    }

    public boolean spostaElementiLegati(Tb_titolo titolo, String ute_var)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //da usare i verificaEsistenza al posto dei cercaperid. Anzi no: vanno usati entrambi
        boolean ret = false;
        String bid1 = titolo.getBID();
        String bid2 = fonde.getIdArrivo();
        Impronta impronta = new Impronta();
        // 14.7.2005 le impronte non si spostano ma si cancellano
        /*TableDao v = impronta.cercaPerBid(bid);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tb_impronta tbi = (Tb_impronta) v.get(i);
                tbi.setBID(bid2);
                tbi.setUTE_VAR(ute_var);
                if (impronta
                    .verificaEsistenza(bid2, tbi.getIMPRONTA_1(), tbi.getIMPRONTA_2(), tbi.getIMPRONTA_3())
                    == null) {
                    tbi.setUTE_INS(ute_var);
                    impronta.inserisci(tbi);
                } else {
                    impronta.update(tbi);
                }
            }
            impronta.cancellaPerBid(bid, ute_var);
        }*/
        impronta.cancellaPerBid(bid1, ute_var);
        Composizione comp = new Composizione();
        Tb_composizione tbc = comp.cercaPerId(bid1);
        if (tbc != null) {
            tbc.setUTE_VAR(ute_var);
            tbc.setBID(bid2);
            Tb_composizione tbc2 = comp.cercaEsistenza(bid2);
            if (tbc2 == null) {
                tbc.setUTE_INS(ute_var);
                comp.inserisci(tbc);
            } else {
                tbc.setTS_VAR(tbc2.getTS_VAR());
                comp.eseguiUpdate(tbc);
            }
            comp.cancellaPerBid(bid1, ute_var);
        }
        Grafica grafica = new Grafica();
        Tb_grafica tbg = grafica.cercaPerId(bid1);
        if (tbg != null) {
            tbg.setUTE_VAR(ute_var);
            tbg.setBID(bid2);
            if (grafica.cercaPerId(bid2) == null) {
                tbg.setUTE_INS(ute_var);
                grafica.inserisci(tbg);
            } else {
                grafica.update(tbg);
            }

            // Inizio BUG MANTIS 3648  almaviva2 adeguato comportamento a quello di Cartografia
//            grafica.cancellaPerBid(bid, ute_var);
            grafica.cancellaPerBid(bid1, ute_var, new SbnDatavar(tbg.getTS_VAR()));
            // Fine BUG MANTIS 3648  almaviva2 adeguato comportamento a quello di Cartografia
        }
        Cartografia cart = new Cartografia();
        Tb_cartografia tbcar = cart.cercaPerId(bid1);
        if (tbcar != null) {
            tbcar.setUTE_VAR(ute_var);
            tbcar.setBID(bid2);
            if (cart.cercaPerId(bid2) != null) {
                tbcar.setUTE_INS(ute_var);
                cart.inserisci(tbcar);
            } else {
                cart.update(tbcar);
            }
            cart.cancellaPerBid(bid1, ute_var, new SbnDatavar(tbcar.getTS_VAR()));
        }
        NumeroStd numero = new NumeroStd();
        List v = numero.cercaNumeroPerBid(bid1);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tb_numero_std tbn = (Tb_numero_std) v.get(i);
                tbn.setBID(bid2);
                tbn.setUTE_VAR(ute_var);
                if (numero.verificaEsistenza(bid2, tbn.getTP_NUMERO_STD().trim(), tbn.getNUMERO_STD().trim()) == null) {
                    tbn.setUTE_INS(ute_var);
                    numero.inserisci(tbn);
                }
            }
            numero.cancellaPerBid(bid1, ute_var);
        }


        TitoloAutore titAut = new TitoloAutore();
        v = titAut.estraiTitoliAutore(bid1);
        if (v.size() > 0) {
            titAut.cancellaPerBid(bid1, ute_var);
            //            Tr_tit_aut classe;
            //            Autore classeDB = new Autore();
            //            for (int i = 0; i < v.size(); i++) {
            //                classe = (Tr_tit_aut) v.get(i);
            //                classeDB.updateVariazione(classe.getVID(), ute_var);
            //            }
        }
        TitoloLuogo titLuo = new TitoloLuogo();
        // 14.7.2005 i legami a luogo si cancellano e non si spostano
        /*v = titLuo.estraiTitoliLuogo(bid);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tr_tit_luo trl = (Tr_tit_luo) v.get(i);
                trl.setBID(bid2);
                trl.setUTE_VAR(ute_var);
                if (titLuo.estraiTitoloLuogo(bid2, trl.getLID(),null) == null) {
                    trl.setUTE_INS(ute_var);
                    titLuo.inserisci(trl);
                } else {
                    titLuo.update(trl);
                }
            }
            titLuo.cancellaPerBid(bid, ute_var);
       }*/
        titLuo.cancellaPerBid(bid1, ute_var);
        TitoloMarca titMar = new TitoloMarca();
        // 14.7.2005 i legami a marca si cancellano e non si spostano
        /*v = titMar.estraiTitoliMarca(bid);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tr_tit_mar trl = (Tr_tit_mar) v.get(i);
                trl.setBID(bid2);
                trl.setUTE_VAR(ute_var);
                if (titMar.estraiTitoloMarca(trl.getMID(), bid2) == null) {
                    trl.setUTE_INS(ute_var);
                    titMar.inserisci(trl);
                } else
                    titMar.update(trl);
            }
            titMar.cancellaPerBid(bid, ute_var);
        }*/
        titMar.cancellaPerBid(bid1, ute_var);

        spostaLegamiTitoloClasse(bid1, bid2, ute_var);
        spostaLegamiTitoloSoggetto(bid1, bid2, ute_var);

        Nota nota = new Nota();
        v = nota.cercaPerBid(bid1);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tb_nota tn = (Tb_nota) v.get(i);
                tn.setBID(bid2);
                tn.setUTE_VAR(ute_var);
                if (nota.cercaPerChiave(tn.getTP_NOTA(), (int) tn.getPROGR_NOTA(), bid2) == null) {
                    tn.setUTE_INS(ute_var);
                    nota.inserisci(tn);
                } else {
                    nota.update(tn);
                }
            }
            //almaviva5_20090918 #3068
            nota.cancella(bid1, ute_var);
        }


        // Inizio modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo

        List legamiRaccolteFattizie = new TitoloTitolo().cercaLegamiPerBidBaseNaturaColl(bid1,"R");

        if (legamiRaccolteFattizie.size() > 0) {
        	TitoloTitolo ttDB = new TitoloTitolo();
        	Tr_tit_tit tt;
            for (int i = 0; i < legamiRaccolteFattizie.size(); i++) {
            	tt = (Tr_tit_tit) legamiRaccolteFattizie.get(i);

            	ttDB.updateCancella(ute_var, bid2,  bid1);
                if (estraiLegame(bid2, bid1) != null) {
                } else {
                    // Creo la nuova versione di tr_tit_tit
                    tt.setBID_BASE(bid2);
                    tt.setUTE_VAR(ute_var);
                    ttDB.inserisciLegame(tt);
                }
            }
        }

        // Fine modifica almaviva2 04.07.2012 - i legami a raccolta fattizia vanno spostati dal titolo fuso a quello di arrivo

        elaboraTitBib(bid1, bid2, ute_var);
        aggiornaEstensioniMusicali(titolo, bid2);
        aggiornaEstensioni(titolo, bid2);
        return ret;
    }

	private void spostaLegamiTitoloClasse(String bid1, String bid2,
			String ute_var) throws Exception {
		TitoloClasse titoloClasse = new TitoloClasse();
        // 14.7.2005 i legami a classe si cancellano
        // Inizio Modifica almaviva2 10.03.2010 prima di effettuare la cancellazione del legame tit-cla si effettua il controllo
        // di esistenza; Si ripristina il controllo che già veniva effettuato e che era stato asteriscato per altre ragioni
        // e si asterisca la chiamata secca alla cancellazione; Intervento interno
        // Inizio modifica almaviva2 22.06.2012 viene ripristinato il metodo per lo spostamento delle classi
        List<Tr_tit_cla> classi = titoloClasse.estraiTitoliClasse(bid1);
        if (!isFilled(classi))
        	return;

		// almaviva2 15.10.2012 Bug Mantis esercizio 5140/5144:Errrore nello spostamento legami classi se partenza e arrivo fusione sono
        // legati alla stessa classe: la funzione titoloClasse.estraiTitoloClasse deve essere salvata su un oggetto così da poter
        // utilizzarne il ts_var nella fuzione di update che altrimenti nella clausola di where utilizzava la ts_var dell'oggetto
        // arrivo di fusione e non riusciva ad effettuare l'update

        for (Tr_tit_cla tc1 : classi) {
            Tr_tit_cla tc2 = titoloClasse.estraiTitoloClasse(bid2, tc1.getCD_SISTEMA(), tc1.getCD_EDIZIONE(), tc1.getCLASSE());
			if (tc2 == null) {
				tc1.setBID(bid2);
                tc1.setUTE_INS(ute_var);
                tc1.setUTE_VAR(ute_var);
                titoloClasse.inserisci(tc1);
            } else {
                tc2.setUTE_VAR(ute_var);
                titoloClasse.update(tc2);
            }
        }
        titoloClasse.cancellaPerBid(bid1, ute_var);

     // Fine Modifica almaviva2 10.03.2010
     // Fine modifica almaviva2 22.06.2012 viene ripristinato il metodo per lo spostamento delle classi
	}


	private void spostaLegamiTitoloSoggetto(String bid1, String bid2, String ute_var) throws Exception {
		SoggettoTitolo ts = new SoggettoTitolo();
        SoggettoValida sv = new SoggettoValida();
        // 14.7.2005 i legami a soggetto si cancellano
        // Inizio modifica almaviva2 22.06.2012 i soggetti vengono spostati dal bid di partenza a quello di arrivo
        List<Tr_tit_sog_bib> soggetti = ts.estraiTitoliSoggetto(bid1);
        if (!isFilled(soggetti) )
        	return;

    	Map<String, Integer> livelli = new HashMap<String, Integer>();
        for (Tr_tit_sog_bib tsb1 : soggetti) {
            // Per ogni legame tit-sog verifico che esista una soggettazione per quel soggettario e per quale livello
            String cdSogg = tsb1.getCD_SOGG();
            Integer livelloSoggettazioneArrivo = livelli.get(cdSogg);
            if (livelloSoggettazioneArrivo == null) {
            	livelloSoggettazioneArrivo = sv.controllaLivelloSoggettazioneBid(cdSogg, bid2);
            	livelli.put(cdSogg, livelloSoggettazioneArrivo);
            }

        	int livelloSoggettazionePartenza = Math.max(sv.controllaLivelloSoggettazioneBid(cdSogg, bid1), 05);
        	if (livelloSoggettazioneArrivo <= livelloSoggettazionePartenza) {
        		// E' possibile spostare i soggetti perchè il livello autorità legame lo consente
        		// almaviva2 15.10.2012 Bug Mantis esercizio 5140/5144: Vedi stesso problema in legame Titolo-Classe
                Tr_tit_sog_bib tsb2 = ts.estraiTitoloSoggetto(bid2, tsb1.getCID());
				if (tsb2 == null) {	//non trovato
					tsb1.setBID(bid2);
                    tsb1.setUTE_INS(ute_var);
                    tsb1.setUTE_VAR(ute_var);
                    ts.inserisci(tsb1);
                } else {	//legame già esistente
                	tsb2.setUTE_VAR(ute_var);
                    ts.update(tsb2);
                }
        	} else
				if (log.isEnabledFor(Priority.WARN)) {
					//soggettazione non spostabile
					StringBuilder buf = new StringBuilder(128);
					buf.append("Impossibile spostare soggettazione da '").append(bid1);
					buf.append("' a '").append(bid2);
					buf.append("' per soggettario '").append(cdSogg).append("'");
					log.warn(buf.toString());
				}
        }

        ts.cancellaPerBid(bid1, ute_var);
	}

    public void aggiornaEstensioniMusicali(Tb_titolo titolo, String bid2) throws IllegalArgumentException, InvocationTargetException, Exception {
        String mat = estraiTitoloPerID(bid2).getTP_MATERIALE();
        if (!((mat.equals("M") || mat.equals("A")) && titolo.getTP_MATERIALE().equals("U")))
            return;
        String bid = titolo.getBID();
        String ute_var = titolo.getUTE_VAR();
        Musica musica = new Musica();
        Tb_musica tbm = musica.cercaPerId(bid);
        if (tbm != null) {
            tbm.setBID(bid2);
            tbm.setUTE_VAR(ute_var);
            Tb_musica tbm2 = musica.verificaEsistenza(bid2);
            if (tbm2 == null) {
                tbm.setUTE_INS(ute_var);
                musica.inserisci(tbm);
            } else {
                tbm.setTS_VAR(tbm2.getTS_VAR());
                musica.eseguiUpdate(tbm);
            }
            musica.cancellaPerBid(bid, ute_var);
        }

        Incipit incipit = new Incipit();
        List v = incipit.cercaPerBid(bid);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tb_incipit tbi = (Tb_incipit) v.get(i);
                tbi.setBID(bid2);
                tbi.setUTE_VAR(ute_var);
                if (incipit.cercaEsistenza(tbi) == null) {
                    tbi.setUTE_INS(ute_var);
                    incipit.inserisci(tbi);
                } else {
                    incipit.update(tbi);
                }
            }
            incipit.cancellaPerBid(bid, ute_var);
        }
        Personaggio pers = new Personaggio();
        v = pers.cercaPerBid(bid);
        if (v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                Tb_personaggio tbi = (Tb_personaggio) v.get(i);
                tbi.setBID(bid2);
                tbi.setUTE_VAR(ute_var);
                tbi.setUTE_INS(ute_var);
                tbi.setID_PERSONAGGIO(new Progressivi().getNextIdPersonaggio());
                pers.inserisci(tbi);
            }
            pers.cancellaPerBid(bid, ute_var);
        }
        Rappresentazione rapp = new Rappresentazione();
        Tb_rappresent tbr = rapp.cercaPerId(bid);
        if (tbr != null) {
            tbr.setBID(bid2);
            tbr.setUTE_VAR(ute_var);
            if (rapp.cercaEsistenza(bid2) == null) {
                tbr.setUTE_INS(ute_var);
                rapp.inserisci(tbr);
            } else {
                rapp.update(tbr);
            }
            rapp.cancella(bid, ute_var);
        }
        updateLegamiMusica(bid, bid2, ute_var);
    }

    public void updateLegamiMusica(String bid, String bid2, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        //Update dei record con tp_legame_musica != null.
        TitoloTitolo tt = new TitoloTitolo();
        List v = tt.cercaLegamiMusica(bid);
        Tr_tit_tit tr;
        for (int i = 0; i < v.size(); i++) {
            tr = (Tr_tit_tit) v.get(i);
            tr.setUTE_VAR(ute_var);
            tt.updateCancella(tr);
            tr.setUTE_INS(ute_var);
            tr.setBID_BASE(bid2);
            tt.inserisciLegame(tr);
        }
        v = tt.cercaRelTitUniMus(bid);
        for (int i = 0; i < v.size(); i++) {
            Vl_titolo_tit_b vl = (Vl_titolo_tit_b) v.get(i);
            tt.updateCancella(ute_var, vl.getBID_BASE(), vl.getBID());
            vl.setUTE_VAR(ute_var);
            vl.setUTE_INS(ute_var);
            vl.setBID_BASE(bid2);
            tt.inserisciLegame(vl);
        }

    }

	public void aggiornaEstensioni(Tb_titolo titolo, String bid2) throws IllegalArgumentException, InvocationTargetException, Exception {
		String mat2 = estraiTitoloPerID(bid2).getTP_MATERIALE();
		String mat1 = titolo.getTP_MATERIALE();
		if (!mat2.equals("M") && mat2.equals("A"))
			return;
		if (!ValidazioneDati.in(mat1, "G", "C", "H"))
			return;
		String bid = titolo.getBID();
		String ute_var = titolo.getUTE_VAR();
		if (mat1.equals("C")) {
			// Cartografia
			Cartografia cart = new Cartografia();
			Tb_cartografia tbc = cart.cercaPerId(bid);
			if (tbc != null) {
				cart.cancellaPerBid(bid, ute_var, new SbnDatavar(tbc.getTS_VAR()));
				tbc.setUTE_INS(ute_var);
				tbc.setUTE_VAR(ute_var);
				tbc.setBID(bid2);
				cart.inserisci(tbc);
			}
		} else if (mat1.equals("G")) {
			// Grafica
			Grafica gra = new Grafica();
			Tb_grafica tbg = gra.cercaPerId(bid);
			if (tbg != null) {
				// Inizio BUG MANTIS 3648 almaviva2 adeguato comportamento a quello di
				// Cartografia
				// gra.cancellaPerBid(bid, ute_var);
				gra.cancellaPerBid(bid, ute_var, new SbnDatavar(tbg.getTS_VAR()));
				// Fine BUG MANTIS 3648 almaviva2 adeguato comportamento a quello di Cartografia
				tbg.setUTE_INS(ute_var);
				tbg.setUTE_VAR(ute_var);
				tbg.setBID(bid2);
				gra.inserisci(tbg);
			}
		} else if (mat1.equals("H")) {
			// Audiovisivo
			Audiovisivo vis = new Audiovisivo();
			Tb_audiovideo aud = vis.cercaPerId(bid);
			if (aud != null) {
				vis.cancellaPerBid(bid, ute_var, new SbnDatavar(aud.getTS_VAR()));
				aud.setUTE_INS(ute_var);
				aud.setUTE_VAR(ute_var);
				aud.setBID(bid2);
				if (vis.cercaPerId(bid2) == null) {
					vis.inserisci(aud);
				} else {
					vis.update(aud);
				}
			} // end if tb_audiovideo
			Discosonoro dis = new Discosonoro();
			Tb_disco_sonoro son = dis.cercaPerId(bid);
			if (son != null) {
				dis.cancellaPerBid(bid, ute_var, new SbnDatavar(son.getTS_VAR()));
				son.setUTE_INS(ute_var);
				son.setUTE_VAR(ute_var);
				son.setBID(bid2);
				if (dis.cercaPerId(bid2) == null) {
					dis.inserisci(son);
				} else {
					dis.update(son);
				}
			} // end if tb_disco_sonoro
		}
	}

    public void elaboraTitBib(String bidPartenza, String bidArrivo, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        List localizzazioni = titBib.cercaLocalizzazioni(bidPartenza);
        boolean polo = ValidatorProfiler.getInstance().isPolo(ute_var);
        if (isFilled(localizzazioni) ) {
            for (int i = 0; i < localizzazioni.size(); i++) {
                Tr_tit_bib tb = (Tr_tit_bib) localizzazioni.get(i);
                tb.setBID(bidArrivo);
                tb.setUTE_VAR(ute_var);
               	Tr_tit_bib titBibArrivo = titBib.cercaPerChiave(tb);
               	if (titBibArrivo != null) {
               		// INIZIO almaviva2 - intervento Mantis bug 7324
               		// in presenza dell'URI sul bid fuso querste inormazioni vanno aggiornate anche sul bid arrivi di fusione
               		// (tb.getFL_DISP_ELETTR,tb.getTP_DIGITALIZZ, tb.getURI_COPIA)
               		if (isFilled(tb.getURI_COPIA()) ) {
               			titBib.aggiornaTr_tit_bib_Consis_URI(titBibArrivo, ute_var, tb.getDS_SEGN(),
                   				tb.getFL_DISP_ELETTR(),tb.getTP_DIGITALIZZ(), tb.getURI_COPIA());
               		} else {
               			titBib.aggiornaTr_tit_bib_Consis(titBibArrivo, ute_var, tb.getDS_SEGN());
               		}
               	// FINE almaviva2 - intervento Mantis bug 7324
               	} else {
               		// INIZIO Bug esercizio 7434 - Almaviva2 - maggio 2020
               		// in caso di assenza localizzazione l'inserimento deve sempre essere fatto senza ulteriori controlli
               		// e deve essere impostato anche tb.setUTE_INS aggiornato (viene asteriscato tutto il controllo)
               		// if ((polo && tb.getCD_POLO().equals(ute_var.substring(0, 3)))
                    // || (tb.getFL_POSSESSO().equals("S") && !tb.getFL_GESTIONE().equals("S"))) {
                    // se esiste gia' la localizzazione sul titolo di arrivo per la biblioteca,
                	// devo aggiornare la consistenza
                    //    titBib.inserisciTr_tit_bib(tb);
               		//}
               		tb.setUTE_INS(ute_var);
               		titBib.inserisciTr_tit_bib(tb);
               	// FINE Bug esercizio 7434 - Almaviva2 - maggio 2020
               	}
            }
            if (!polo) {
                titBib.aggiornaCancellaFlAllinea(ute_var, bidPartenza, null, null);
            } else {
                titBib.aggiornaCancellaFlAllinea(
                    ute_var,
                    bidPartenza,
                    ute_var.substring(0, 3),
                    ute_var.substring(3, 6));
            }
        }

         //Modifica del 6/12/04
        //Rielaboro anche le informazioni sul bid di arrivo.
        //va messo in allineamento anche il bid di arrivo:
        //    il fl_allinea sarï¿½ impostato a 'S' quando ï¿½ spazio AND il fl_canc != 'S' (diverso da 'S') AND il fl_gestione != 'N' (diverso da 'N') AND il bid=bid-accorpante
        //    il fl_allinea sarï¿½ impostato a 'Z' quando ï¿½ 'C' AND il fl_canc != 'S' (diverso da 'S') AND il fl_gestione != 'N' (diverso da 'N') AND il bid=bid-accorpante

        localizzazioni = titBib.cercaLocalizzazioni(bidArrivo);
        if (isFilled(localizzazioni) ) {
            for (int i = 0; i < localizzazioni.size(); i++) {
                Tr_tit_bib tb = (Tr_tit_bib) localizzazioni.get(i);

                // MARCO RANIERI MODIFICA:
                // PROSEGUO SOLO SE FLAG GESTIONE DIVERSO DA N
                // localizzato per gestione
                if (tb.getFL_GESTIONE().equalsIgnoreCase("N")==false)
                {
                    tb.setUTE_VAR(ute_var);
                    boolean aggiorna = false;
                    if (" ".equals(tb.getFL_ALLINEA())) {
                        tb.setFL_ALLINEA("S");
                        aggiorna = true;
                    } else if ("C".equals(tb.getFL_ALLINEA())) {
                        tb.setFL_ALLINEA("Z");
                        aggiorna = true;
                    }
                    if (aggiorna) {
                        titBib.aggiornaTuttiFlAllinea(tb);
                    }
                }
            }
        }
    }

    /**
     * Modifica minima: modifica titolo con idPArtenza e gli copia i dati di idArrivo. Imposta tp
     * link a D e idArrivo in bid_link.
     * @param tit
     * @param user
     * @param legami
     * @param timeHash
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void fondeConLink(Tb_titolo tit, Tb_titolo tit2, String user, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        TitoloTitolo ttDB = new TitoloTitolo();
        tit2.setUTE_VAR(user);
        tit2.setTP_LINK("D");
        tit2.setBID_LINK(tit2.getBID());
        tit2.setBID(tit.getBID());
        tit2.setTS_VAR(tit.getTS_VAR());
        eseguiUpdate(tit2);
        AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tit);
        allineamentoTitolo.setTb_titolo(true);
        TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
    }
    /**
     *
     * Modifica tr_tit_tit con bid_coll= idArrivo, modifica tb_titolo.ts_var e ute_var
     *  con bid=spostaId, aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_tit_bib con bid=spostaID
     * (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
     *
     *
     * @param timeHash
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void sposta(String user, String neo_cd_natura, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        int numSp = fonde.getSpostaIDCount();
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        Tr_tit_tit tt;
        TitoloTitolo titTitDB = new TitoloTitolo();
        Tb_titolo titSposta;
        for (int i = 0; i < numSp; i++) {
            //cerco il legame.
            titSposta = estraiTitoloPerID(fonde.getSpostaID(i));
            if (titSposta == null) {
                EccezioneSbnDiagnostico ecc =
                    new EccezioneSbnDiagnostico(3013, "ID non esistente in base dati");
                ecc.appendMessaggio("- id : " + fonde.getSpostaID(i));
                throw ecc;
            }
            tt = estraiLegame(fonde.getSpostaID(i), fonde.getIdArrivo());
            if (tt != null) {
                EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3030, "Legame gia' esistente");
                ecc.appendMessaggio(",bid sposta = " + fonde.getSpostaID(i));
                throw ecc;
            }
            tt = estraiLegame(fonde.getSpostaID(i), fonde.getIdPartenza());
            if (tt == null) {
                EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3029, "Legame non esistente");
                ecc.appendMessaggio(",bid sposta = " + fonde.getSpostaID(i));
                throw ecc;
            }

            titTitDB.updateCancella(user, fonde.getSpostaID(i), fonde.getIdPartenza());

            //Creo la nuova versione di tr_tit_tit
            tt.setBID_COLL(fonde.getIdArrivo());
            tt.setCD_NATURA_COLL(neo_cd_natura);
            if (neo_cd_natura.equals("A"))
                tt.setTP_LEGAME("09");
            else if (neo_cd_natura.equals("B"))
                tt.setTP_LEGAME("06");
            tt.setUTE_VAR(user);
            titTitDB.inserisciLegame(tt);

            updateVersione(fonde.getSpostaID(i), user);
            titBib.aggiornaFlAllinea(user, fonde.getSpostaID(i), user.substring(0, 3), user.substring(3, 6));
        }
    }

}
