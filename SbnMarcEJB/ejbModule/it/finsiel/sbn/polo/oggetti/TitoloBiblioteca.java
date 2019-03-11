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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_bibResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Category;

public class TitoloBiblioteca extends Tr_tit_bib {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8375060193275819789L;
	protected Connection _db_conn;
    SbnUserType user;
    static Category log = Category.getInstance("iccu.serversbnmarc.TitoloBiblioteca");
    public TitoloBiblioteca() {

    }
    /**
    * Method inserisciTr_tit_bib.
    * @param user
    * @param id
    * @param codicePolo
    * @param codiceBiblioteca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    public boolean inserisciTr_tit_bib(
        String user,
        String id,
        String cdNatura,
        String codicePolo,
        String codiceBiblioteca,
        int tipoLocaliza,
        C899 t899)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(id);
        tr_tit_bib.setCD_POLO(codicePolo);
        tr_tit_bib.setCD_BIBLIOTECA(codiceBiblioteca);
        tr_tit_bib.setFL_CANC(" ");
        tr_tit_bib.setFL_ALLINEA(" ");
        tr_tit_bib.setFL_ALLINEA_SBNMARC(" ");
        tr_tit_bib.setFL_ALLINEA_CLA(" ");
        tr_tit_bib.setFL_ALLINEA_LUO(" ");
        tr_tit_bib.setFL_ALLINEA_REP(" ");
        tr_tit_bib.setFL_ALLINEA_SOG(" ");
        tr_tit_bib.setDS_FONDO(t899.getB_899());
        tr_tit_bib.setDS_CONSISTENZA(t899.getZ_899());
        tr_tit_bib.setDS_SEGN(t899.getG_899());
        tr_tit_bib.setDS_ANTICA_SEGN(t899.getS_899());
        tr_tit_bib.setNOTA_TIT_BIB(t899.getN_899());
        tr_tit_bib.setURI_COPIA(t899.getU_899());

        // Inizio Modifica del 11.12.2013 riportata da Indice:
      //almaviva4 evolutiva 22/10/2010
//  	  if (t899.getT_899() != null)
//  		tr_tit_bib.setTp_digitalizz(t899.getT_899().toString());
//        if (t899.getT_899() != null)
//	        tr_tit_bib.setTP_DIGITALIZZ(t899.getT_899().toString());


        if (t899.getE_899() != null)
            tr_tit_bib.setFL_DISP_ELETTR(t899.getE_899().toString());
        else
            tr_tit_bib.setFL_DISP_ELETTR(" ");


  	  if (t899.getT_899() != null) {
  		tr_tit_bib.setTP_DIGITALIZZ(t899.getT_899().toString());
  		  if (t899.getT_899().toString().equalsIgnoreCase("0")) {
  			tr_tit_bib.setFL_DISP_ELETTR("N");
  		  }
  		  if (t899.getT_899().toString().equalsIgnoreCase("1")) {
  			tr_tit_bib.setFL_DISP_ELETTR("S");
  		  }
  		  if (t899.getT_899().toString().equalsIgnoreCase("2")) {
  			tr_tit_bib.setFL_DISP_ELETTR("S");
  		  }
  	  }
  //almaviva4 evolutiva 22/10/2010 fine

  	  // Fine Modifica del 11.12.2013 riportata da Indice:


        if (t899.getQ_899() != null)
            tr_tit_bib.setFL_MUTILO(t899.getQ_899().toString());
        else
            tr_tit_bib.setFL_MUTILO(" ");


        //Modifico come richiesto da Finsiel
        tr_tit_bib.setFL_GESTIONE("N");
        tr_tit_bib.setFL_POSSESSO("N");
        if ((tipoLocaliza == SbnTipoLocalizza.GESTIONE.getType())
            || (tipoLocaliza == SbnTipoLocalizza.TUTTI.getType()))
            tr_tit_bib.setFL_GESTIONE("S");
        if ((tipoLocaliza == SbnTipoLocalizza.POSSESSO.getType())
            || (tipoLocaliza == SbnTipoLocalizza.TUTTI.getType())) {
        	// Bug MANTIS esercizio 5494 : la localizzazione per solo possesso deve essere ammessa non solo per tutti i titoli
        	// di tipo Documento (Natura M, S, e W) e per le Raccolte Fattizie (natura R)
        	// EVOLUTIVE ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata viene
        	// estesa alla N nuova la localizzazione per possesso della madre
//          if (cdNatura.equals("M") || cdNatura.equals("S") || cdNatura.equals("W")) {
//        	if (cdNatura.equals("M") || cdNatura.equals("S") || cdNatura.equals("W") || cdNatura.equals("R")) {
        	if (cdNatura.equals("M") || cdNatura.equals("S") || cdNatura.equals("W") || cdNatura.equals("R")|| cdNatura.equals("N")) {
        		tr_tit_bib.setFL_POSSESSO("S");
          }
        }

        tr_tit_bib.setUTE_VAR(user);
        tr_tit_bib.setUTE_INS(user);
        inserisciTr_tit_bib(tr_tit_bib);
        return true;
    }

    public void inserisciTr_tit_bib(Tr_tit_bib tb) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tb);


        tr_tit_bibResult.executeCustom("selectCancellato");
        if (tr_tit_bibResult.getElencoRisultati().size()>0)
            tr_tit_bibResult.update(tb);
        else
            tr_tit_bibResult.insert(tb);

    }

    /**
     * Method verificaEsistenzaTr_tit_bib.
     * @param user
     * @param id
     * @param codice_polo
     * @param codice_biblioteca
     * verifica esistenza record Tr_tit_bib
     * @throws InfrastructureException
     */
    public Tr_tit_bib verificaEsistenzaTr_tit_bib(
        String id,
        String cd_polo,
        String cd_biblioteca,
        int tipoLocalizza)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(id);
        tr_tit_bib.setCD_POLO(cd_polo);
        tr_tit_bib.setCD_BIBLIOTECA(cd_biblioteca);
        //controllo che se tipoLocalizza ï¿½ gestione

        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        //tr_tit_bibResult.selectPerKey(tr_tit_bib.leggiAllParametro());
        tr_tit_bibResult.valorizzaElencoRisultati(tr_tit_bibResult.selectPerKey(tr_tit_bib.leggiAllParametro()));

        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        log.info("vettore esistenza" + resultTableDao.size());
        if (resultTableDao.size() != 0) {
            if (SbnTipoLocalizza.GESTIONE.getType() == tipoLocalizza) {
                if (((Tr_tit_bib) resultTableDao.get(0)).getFL_GESTIONE().equals("S")) {
                    log.info("gestione " + ((Tr_tit_bib) resultTableDao.get(0)).getFL_GESTIONE());
                } else {
                    throw new EccezioneSbnDiagnostico(3050);
                }
            }
            if (SbnTipoLocalizza.POSSESSO.getType() == tipoLocalizza) {
                if (((Tr_tit_bib) resultTableDao.get(0)).getFL_POSSESSO().equals("S")) {
                    log.info("possesso " + ((Tr_tit_bib) resultTableDao.get(0)).getFL_POSSESSO());
                } else {
                    throw new EccezioneSbnDiagnostico(3047);
                }
            }
            return (Tr_tit_bib) resultTableDao.get(0);
        } else {
            log.info("passa da null ");
            return null;
        }
    }

    /**
     * Method verificaEsistenzaTr_tit_bib con 3 parametri senza codice biblioteca
     *         non viene effettuato il controllo di congruenza sul tipo localizza
     * @param user
     * @param id
     * @param codice_polo
     * verifica esistenza record Tr_tit_bib
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List verificaEsistenzaTr_tit_bibBiblioteca(String id, String cd_polo, int tipoLocalizza, String codiceBiblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(id);
        tr_tit_bib.setCD_POLO(cd_polo);
        tr_tit_bib.setCD_BIBLIOTECA(codiceBiblioteca);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);

        tr_tit_bibResult.executeCustom("selectPerPolo");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        return resultTableDao;
    }

    /**
     * Method aggiornaTr_tit_bib.
     * @param user
     * @param id
     * @param codice_polo
     * @param codice_biblioteca
     * aggiorna il record in Tr_tit_bib fl_canc = 'S'
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean cancellaTr_tit_bib(String user, Tr_tit_bib tr_tit_bib, int tipoLocalizza)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        boolean esito = false;
        if (tipoLocalizza == SbnTipoLocalizza.TUTTI_TYPE)
            tr_tit_bib.setFL_CANC("S");
        else if (tipoLocalizza == SbnTipoLocalizza.GESTIONE_TYPE)
            tr_tit_bib.setFL_GESTIONE("N");
        else if (tipoLocalizza == SbnTipoLocalizza.POSSESSO_TYPE)
            tr_tit_bib.setFL_POSSESSO("N");
        if (tr_tit_bib.getFL_POSSESSO().equals("N") && tr_tit_bib.getFL_GESTIONE().equals("N"))
            tr_tit_bib.setFL_CANC("S");
        SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
        tr_tit_bib.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        tr_tit_bib.setUTE_VAR(user);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);
        tr_tit_bibResult.executeCustom("updatePerModifica");
        if (tr_tit_bibResult.getRowsUpdate() != 0)
            esito = true;

        return esito;
    }

    /**
     * Method aggiornaTr_tit_bib.
     * @param user
     * @param id
     * @param codice_polo
     * @param codice_biblioteca
     * aggiorna il record in Tr_tit_bib fl_canc = 'S'
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaFlagGesPos(String user, String id, String cd_polo, String cd_biblioteca, int tipoLocaliza,C899 t899)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(id);
        tr_tit_bib.setCD_POLO(cd_polo);
        tr_tit_bib.setCD_BIBLIOTECA(cd_biblioteca);
        tr_tit_bib.setFL_CANC(" ");
        //Modifico come richiesto da Finsiel
        tr_tit_bib.setFL_GESTIONE("N");
        tr_tit_bib.setFL_POSSESSO("N");
        if ((tipoLocaliza == SbnTipoLocalizza.GESTIONE.getType())
            || (tipoLocaliza == SbnTipoLocalizza.TUTTI.getType()))
            tr_tit_bib.setFL_GESTIONE("S");
        if ((tipoLocaliza == SbnTipoLocalizza.POSSESSO.getType())
            || (tipoLocaliza == SbnTipoLocalizza.TUTTI.getType()))
            tr_tit_bib.setFL_POSSESSO("S");
        tr_tit_bib.setUTE_VAR(user);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);
        tr_tit_bibResult.executeCustom("updatePerModifica");
    }

    /**
     * Method aggiornaTr_tit_bib_Consis.
     * @param user
     * @param tr_tit_bib
     * @param segnature
     * aggiorna il record in Tr_tit_bib accodando il campo segnatura in ds_consistenza
     * (per fusione dei titoli musicali)
     * @throws InfrastructureException
     */
    public void aggiornaTr_tit_bib_Consis(Tr_tit_bib tr_tit_bib, String user, String segnatura)
        throws EccezioneDB, InfrastructureException {
        if (segnatura != null) {
            if (tr_tit_bib.getDS_CONSISTENZA() == null) {
                tr_tit_bib.setDS_CONSISTENZA(segnatura.trim());
            } else {
                tr_tit_bib.setDS_CONSISTENZA(tr_tit_bib.getDS_CONSISTENZA().trim() + ";" + segnatura.trim());
            }
        	SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
            tr_tit_bib.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
            tr_tit_bib.setUTE_VAR(user);
	        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


	        tr_tit_bibResult.update(tr_tit_bib);

	    }
    }
    public boolean allineaTr_tit_bib(Tr_tit_bib tit_bib, String user, Tb_titolo titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        boolean esito = false;

        tit_bib.setFL_ALLINEA(" ");
        tit_bib.setFL_ALLINEA_SBNMARC(" ");
        tit_bib.setFL_ALLINEA_CLA(" ");
        tit_bib.setFL_ALLINEA_SOG(" ");
        tit_bib.setFL_ALLINEA_LUO(" ");
        tit_bib.setFL_ALLINEA_REP(" ");
        tit_bib.setUTE_VAR(user);
        if (titolo.getTP_LINK()!=null && titolo.getTP_LINK().equals("F")) {
            tit_bib.setUTE_INS(user);
            tit_bib.setBID(titolo.getBID_LINK());
            inserisciTr_tit_bib(tit_bib);
            tit_bib.setBID(titolo.getBID());
        }
        if (titolo.getFL_CANC().equals("S"))
        {
            tit_bib.setFL_CANC("S");
        }
        // MARCO RANIERI MODIFICA
        // AGGIUNGO SPAZIO SE FL CANC NON ATTIVO
        else
        {
        	 tit_bib.setFL_CANC(" ");
        }

        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tit_bib);
        tr_tit_bibResult.executeCustom("updatePerModificaBiblioteca");

        return esito;
    }

    /**
     * aggiorna il record in tr_tit_bib secondo quanto contenuto in   T899 ad
     * esclusione di a_899, c1_899 e c2_899
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */

    public boolean aggiornaT899(
        String user,
        String Bid,
        String cd_polo,
        String cd_biblioteca,
        C899 t899,
        Tr_tit_bib tr_tit_bib)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        boolean esito = false;
        setBID(Bid);
        setCD_POLO(cd_polo);
        setCD_BIBLIOTECA(cd_biblioteca);
        if (t899.getS_899() != null) {
            setDS_ANTICA_SEGN(t899.getS_899());
        } else
            setDS_ANTICA_SEGN(tr_tit_bib.getDS_ANTICA_SEGN());

        if (t899.getZ_899() != null) {
            setDS_CONSISTENZA(t899.getZ_899());
        } else
            setDS_CONSISTENZA(tr_tit_bib.getDS_CONSISTENZA());

        if (t899.getB_899() != null) {
            setDS_FONDO(t899.getB_899());
        } else
            setDS_FONDO(tr_tit_bib.getDS_FONDO());

        if (t899.getG_899() != null) {
            setDS_SEGN(t899.getG_899());
        } else
            setDS_SEGN(tr_tit_bib.getDS_SEGN());

        if (t899.getQ_899() != null) {
            setFL_MUTILO(t899.getQ_899().toString());
        } else
            setFL_MUTILO(tr_tit_bib.getFL_MUTILO());

        if (t899.getN_899() != null) {
            setNOTA_TIT_BIB(t899.getN_899());
        } else
            setNOTA_TIT_BIB(tr_tit_bib.getNOTA_TIT_BIB());
        if (t899.getE_899() != null)
            setFL_DISP_ELETTR(t899.getE_899().toString());
        else
            setFL_DISP_ELETTR(" ");
        setURI_COPIA(t899.getU_899());



        // Inizio Modifica del 11.12.2013 riportata da Indice:
        //almaviva4 evolutiva 22/10/2010
//    	  if (t899.getT_899() != null)
//    		tr_tit_bib.setTp_digitalizz(t899.getT_899().toString());
//          if (t899.getT_899() != null)
//  	        tr_tit_bib.setTP_DIGITALIZZ(t899.getT_899().toString());

    	  if (t899.getT_899() != null) {
    		tr_tit_bib.setTP_DIGITALIZZ(t899.getT_899().toString());
    		setTP_DIGITALIZZ(t899.getT_899().toString());
    		  if (t899.getT_899().toString().equalsIgnoreCase("0")) {
    			tr_tit_bib.setFL_DISP_ELETTR("N");
    			setFL_DISP_ELETTR("N");
    		  }
    		  if (t899.getT_899().toString().equalsIgnoreCase("1")) {
    			tr_tit_bib.setFL_DISP_ELETTR("S");
    			setFL_DISP_ELETTR("S");
    		  }
    		  if (t899.getT_899().toString().equalsIgnoreCase("2")) {
    			tr_tit_bib.setFL_DISP_ELETTR("S");
    			setFL_DISP_ELETTR("S");
    		  }
    	  }
    //almaviva4 evolutiva 22/10/2010 fine

    	  // Fine Modifica del 11.12.2013 riportata da Indice:


        setFL_CANC(" ");
        SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
        setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        setUTE_VAR(user);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(this);
        tr_tit_bibResult.executeCustom("updateT899");
        if (tr_tit_bibResult.getElencoRisultati() != null)
            esito = true;

        return esito;
    }

    /*	public boolean aggiornaFlAllinea(
    		String user,
    		String biblioteca,
    		String Bid) throws EccezioneDB{
    		boolean esito = false;
    		setBid(Bid);
    		setFL_ALLINEA("S");
    		setUTE_VAR(user);
    		Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(this);


    		tr_tit_bibResult.executeCustom("aggiornaFlAllinea");
    		if (tr_tit_bibResult.getRowsUpdated() != 0) esito = true;

    		return esito;
    	}
    */

    public void aggiornaTuttiFlAllinea(Tr_tit_bib tb) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tb);

        tr_tit_bibResult.executeCustomUpdate("updateTuttiFlag");

    }

    public void aggiornaFlAllinea(String user, String bid, String cd_polo, String cd_biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setFL_ALLINEA("S");
        setCD_POLO(cd_polo);
        setCD_BIBLIOTECA(cd_biblioteca);
        setUTE_VAR(user);
        setFL_CANC(" ");
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(this);
        tr_tit_bibResult.executeCustom("updatePerModificaPoloDiverso");
    }


    public void aggiornaCancellaFlAllinea(String user, String bid, String cd_polo, String cd_biblioteca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setFL_ALLINEA("S");
        setCD_POLO(cd_polo);
        setCD_BIBLIOTECA(cd_biblioteca);
        setUTE_VAR(user);
        setFL_CANC(" ");
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(this);

        tr_tit_bibResult.executeCustom("updatePerModificaPoloDiverso");

    }

    public void aggiornaFlAllinea(String user, String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(bid);
        setFL_ALLINEA("S");
        setUTE_VAR(user);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(this);

        tr_tit_bibResult.executeCustomUpdate("updateAllineaPerBid");

    }

    /**
     * metodo utilizzato per la verifica della non esistenza di localizzazioni
     * diverse dal polo dell'utente
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean verificaLocalizzazioniPoloDiverso(String bid, String utente) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (utente.length() > 3)
            utente = utente.substring(0, 3);
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(bid);
        tr_tit_bib.setCD_POLO(utente);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        tr_tit_bibResult.executeCustom("selectPerPoloDiverso");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo utilizzato per la verifica della esistenza di localizzazioni
     * nel polo dell'utente con fl_gestione != 'N'
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean verificaLocalizzazioniPoloUguale(String bid, String polo_bib) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(bid);
        tr_tit_bib.setCD_POLO(polo_bib.substring(0,3));
        tr_tit_bib.setCD_BIBLIOTECA(polo_bib.substring(3));
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        //tr_tit_bibResult.executeCustom("selectPerPoloBibliotecaUguale");
        tr_tit_bibResult.executeCustom("selectPerPoloUguale");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }


//    <filter name="TR_TIT_BIB_verificaLocalizzazioniPerBiblioteca"
//        condition="bid = :XXXbid
//                   AND cd_biblioteca = :XXXcd_biblioteca
//                   AND fl_canc != 'S'
//                   AND fl_possesso = 'S'"/>
// CHIAMATA ATTUALMENTE NON GESTITA VIENE UTILIZZATO FILTRO AGGIUNTIVO SU TR_TIT_BIB
    public boolean verificaLocalizzazioniPerBiblioteca(String bid, SbnUserType user) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(bid);
        tr_tit_bib.setCD_BIBLIOTECA(user.getBiblioteca());
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        //tr_tit_bibResult.executeCustom("selectPerPoloBibliotecaUguale");
        tr_tit_bibResult.executeCustom("verificaLocalizzazioniPerBiblioteca");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        if (resultTableDao.size() != 0) {
            return true;
        } else {
            return false;
        }
    }



    public List cercaLocalizzazioni(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(bid);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        tr_tit_bibResult.executeCustom("selectPerBid");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        return resultTableDao;

    }

    public TableDao cercaLocDaCanc(String ts_start,String ts_end) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_bib titolo = new Vl_titolo_bib();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");
        Vl_titolo_bibResult tavola = new Vl_titolo_bibResult(titolo);

        tavola.executeCustom("selectRidottoOpac");
        return tavola;
    }

    public TableDao cercaVariazioniLoc(String ts_start,String ts_end, SbnMateriale[] materiale) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_bib titolo = new Vl_titolo_bib();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");
        if (materiale != null) {
            for (int i=0;i<materiale.length;i++) {
                titolo.settaParametro("XXXmateriale"+i, ""+materiale[i]);
            }
        }
        Vl_titolo_bibResult tavola = new Vl_titolo_bibResult(titolo);


        tavola.executeCustom("selectRidottoOpac");
        return tavola;
    }

    public List cercaPerAllineamento(String bid, String polo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        tr_tit_bib.setBID(bid);
        tr_tit_bib.setCD_POLO(polo);
        Tr_tit_bibResult tr_tit_bibResult = new Tr_tit_bibResult(tr_tit_bib);


        tr_tit_bibResult.executeCustom("selectPerAllineamento");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        return resultTableDao;

    }
    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tit_luo = new Tr_tit_bib();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_tit_bibResult tavola = new Tr_tit_bibResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");

    }

    public Tr_tit_bib cercaPerChiave(Tr_tit_bib tb) throws EccezioneDB, InfrastructureException {
        Tr_tit_bibResult tavola = new Tr_tit_bibResult(tb);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(tb.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_tit_bib) v.get(0);
        return null;
    }

    public int contaPerBid(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_bib tit_bib = new Tr_tit_bib();
        tit_bib.setBID(bid);
        Tr_tit_bibResult tavola = new Tr_tit_bibResult(tit_bib);


        tavola.executeCustom("countPerBid");
        int n = conta(tavola);

        return n;
    }

    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del SELECT COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }

}
