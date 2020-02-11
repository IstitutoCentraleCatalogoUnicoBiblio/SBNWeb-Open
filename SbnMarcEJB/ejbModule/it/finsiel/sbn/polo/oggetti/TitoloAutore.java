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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_autoreResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_titolo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_autResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.viste.Vf_titolo_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe TitoloAutore.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 7-gen-03
 */
public class TitoloAutore extends Tr_tit_aut {
    /**
	 * 
	 */
	private static final long serialVersionUID = 74043991997364355L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.TitoloAutore");

    /** Costruttore, la connessione al db serve per tutti gli accessi al DB */
    public TitoloAutore() {
    }

    public TableDao cercaLegamiAutoreTitolo(String idAutore) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(idAutore);
        Tr_tit_autResult tavola = new Tr_tit_autResult(this);
        tavola.executeCustom("selectPerAutore");
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerAutore(Vl_titolo_aut tit) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_autResult tavola = new Vl_titolo_autResult(tit);


        tavola.executeCustom("countPerAutore");
        int n = conta(tavola);

        return n;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaTitoloPerAutore(Vl_titolo_aut tit, String ord) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_autResult tavola = new Vl_titolo_autResult(tit);


        tavola.executeCustom("selectPerAutore", ord);
        return tavola.getElencoRisultati();
    }

    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
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

    /**
     *   nel legame con l'autore il tipoLegame dipende da tr_tit_aut.tp_respons e da
     *   tb_autore.tp_nome:
     *   se tipo nome è A,B,C,D tipoLegame è 700 se tp_respons=1, 701 se
     *   tp_respons=2, 702 se tp_respons=3 o altro
     *   se tipo nome è E,R,G tipoLegame è 710 se tp_respons=1, 711 se tp_respons=2,
     *   712 se tp_respons=3 o altro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisciTitoloAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
            throws IllegalArgumentException, InvocationTargetException, Exception {

    	String _legame_condiviso;
    	if ((leg.getCondiviso() == null || (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = "s";
        }else{
        	_legame_condiviso = "n";
        }

        String tp_resp = calcolaTp_responsabilita(leg);
        Tr_tit_aut ta = new Tr_tit_aut();
        ta.setTP_RESPONSABILITA(tp_resp);
        ta.setVID(leg.getIdArrivo());
        ta.setBID(titolo.getBID());
        ta.setUTE_VAR(titolo.getUTE_VAR());

        // timbri di condivione
        ta.setFL_CONDIVISO(_legame_condiviso);
        ta.setTS_CONDIVISO(TableDao.now());
        ta.setUTE_CONDIVISO(titolo.getUTE_INS());

        ta.setNOTA_TIT_AUT(leg.getNoteLegame());

        // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
        ta.setCD_STRUMENTO_MUS("");
        if (leg.getStrumento()!= null) {
            if (leg.getRelatorCode() != null && (leg.getRelatorCode().equals("590") || leg.getRelatorCode().equals("906")))
               ta.setCD_STRUMENTO_MUS(Decodificatore.getCd_unimarc("ORGA", leg.getStrumento()));
         }

        if (leg.getRelatorCode() == null)
            ta.setCD_RELAZIONE("   ");
        else
            ta.setCD_RELAZIONE(leg.getRelatorCode());
        ta.setFL_CANC(" ");
        if (leg.getSuperfluo() != null)
            ta.setFL_SUPERFLUO(leg.getSuperfluo().toString());
        else
            ta.setFL_SUPERFLUO(" ");
        if (leg.getIncerto() != null)
            ta.setFL_INCERTO(leg.getIncerto().toString());
        else
            ta.setFL_INCERTO(" ");
        Tr_tit_autResult tavola = new Tr_tit_autResult(ta);


        tavola.executeCustom("selectPerKeyCancellato");
        if (tavola.getElencoRisultati().size() > 0)
            tavola.update(ta);
		else {
			//almaviva5_20120517 #4245
			ta.setUTE_INS(titolo.getUTE_VAR());
			tavola.insert(ta);
		}

    }

    public String calcolaTp_responsabilita(LegameElementoAutType leg) {
        if (leg.getTipoRespons()!= null)
            return leg.getTipoRespons().toString();
        if (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("700").getType()
            || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("710").getType())
            return "1";
        else if (
            leg.getTipoLegame().getType() == SbnLegameAut.valueOf("701").getType()
                || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("711").getType())
            return "2";
        
     // almaviva2 Febbraio 2020 - Nuove regole nella gestione del legame titolo-autore
     // Per i seguenti codici di relazione deve essere consentito solo il legame di responsabilità '4':
     // '160 Libraio' - '310 Distributore' - '610 Stampatore' - '620 Stampatore delle tavole' - '650 Editore' - '700 Copista'        
        if (leg.getRelatorCode() != null) {
//            if (leg.getRelatorCode().equals("650")
//                || leg.getRelatorCode().equals("610")
//                || leg.getRelatorCode().equals("160")
//                || leg.getRelatorCode().equals("310")
//                || leg.getRelatorCode().equals("700")
//                || leg.getRelatorCode().equals("750"))
        	if (leg.getRelatorCode().equals("160")
                    || leg.getRelatorCode().equals("310")
                    || leg.getRelatorCode().equals("610")
                    || leg.getRelatorCode().equals("620")
                    || leg.getRelatorCode().equals("650")
                    || leg.getRelatorCode().equals("700"))
                return "4"; //Editore
            else if (
                leg.getRelatorCode().equals("200")
                    || leg.getRelatorCode().equals("820")
                    || leg.getRelatorCode().equals("790")
                    || leg.getRelatorCode().equals("280")
                    || leg.getRelatorCode().equals("290")
                    || leg.getRelatorCode().equals("390")
                    || leg.getRelatorCode().equals("590")
                    || leg.getRelatorCode().equals("040")
                    || leg.getRelatorCode().equals("005")
                    || leg.getRelatorCode().equals("275")
                    || leg.getRelatorCode().equals("580")
                    || leg.getRelatorCode().equals("190")
                    || leg.getRelatorCode().equals("300")
                    || leg.getRelatorCode().equals("250")
//                    || leg.getRelatorCode().equals("310")
                    || leg.getRelatorCode().equals("320")
                    || leg.getRelatorCode().equals("400")
                    || leg.getRelatorCode().equals("110")
//                    || leg.getRelatorCode().equals("160")
                    || leg.getRelatorCode().equals("500")
                    || leg.getRelatorCode().equals("490")
                    || leg.getRelatorCode().equals("420"))
                return "0";
        }
        return "3";

    }

    /** Paro paro a inserisci, tranne per la chiamata all'update e il ts_var
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void modificaTitoloAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }

        String tp_resp = calcolaTp_responsabilita(leg);
        Tr_tit_aut ta = new Tr_tit_aut();
        ta.setTP_RESPONSABILITA(tp_resp);
        ta.setVID(leg.getIdArrivo());
        ta.setBID(titolo.getBID());
        ta.setUTE_INS(titolo.getUTE_INS());
        ta.setUTE_VAR(titolo.getUTE_VAR());
        ta.setNOTA_TIT_AUT(leg.getNoteLegame());
        if (leg.getRelatorCode() == null)
            ta.setCD_RELAZIONE("   ");
        else
            ta.setCD_RELAZIONE(leg.getRelatorCode());
        ta.setFL_CANC(" ");
        if (leg.getSuperfluo() != null)
            ta.setFL_SUPERFLUO(leg.getSuperfluo().toString());
        else
            ta.setFL_SUPERFLUO(" ");
        if (leg.getIncerto() != null)
            ta.setFL_INCERTO(leg.getIncerto().toString());
        else
            ta.setFL_INCERTO(" ");

        // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 4) Occorre aggiungere la specificazione dei relator code 590 (interprete) e 906 (strumentista).
		// Qualora venga valorizzato il relator code 590 o 906, dinamicamente dovrebbe aprirsi il campo con gli strumenti e le voci.
		//  Tabella STMU? Attualmente è registrata, ma risulta vuota (TABELLA CORRETTA E' ORGA)
        if (leg.getStrumento()!= null) {
            if (leg.getRelatorCode() != null && (leg.getRelatorCode().equals("590") || leg.getRelatorCode().equals("906"))) {
            	ta.setCD_STRUMENTO_MUS(Decodificatore.getCd_unimarc("ORGA", leg.getStrumento()));
            }
        }
        // bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
        //ta.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID())));
        ta.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID()+ ta.getCD_RELAZIONE() + ta.getTP_RESPONSABILITA())));

        Tr_tit_aut ta1 = estraiTitoloAutore(titolo.getBID(), leg.getIdArrivo());
		//if(ta1.getFL_CONDIVISO() != _legame_condiviso){
		if(!ta1.getFL_CONDIVISO().equals(_legame_condiviso)){
			ta.setFL_CONDIVISO(_legame_condiviso);
			ta.setTS_CONDIVISO(ta.getTS_VAR());
			ta.setUTE_CONDIVISO(titolo.getUTE_VAR());
		}else{
			ta.setFL_CONDIVISO(ta1.getFL_CONDIVISO());
			ta.setTS_CONDIVISO(ta1.getTS_CONDIVISO());
			ta.setUTE_CONDIVISO(ta1.getUTE_CONDIVISO());
		}


		Tr_tit_autResult tavola = new Tr_tit_autResult(ta);
        tavola.update(ta);

    }
    /**
     *   nel legame con l'autore il tipoLegame dipende da tr_tit_aut.tp_respons e da
     *   tb_autore.tp_nome:
     *   se tipo nome è A,B,C,D tipoLegame è 700 se tp_respons=1, 701 se
     *   tp_respons=2, 702 se tp_respons=3 o altro
     *   se tipo nome è E,R,G tipoLegame è 710 se tp_respons=1, 711 se tp_respons=2,
     *   712 se tp_respons=3 o altro
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void disabilitaTitoloAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_tit_aut ta = new Tr_tit_aut();
        ta.setVID(leg.getIdArrivo());
        ta.setBID(titolo.getBID());
        ta.setUTE_VAR(titolo.getUTE_VAR());
        //ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID())).toString();

        //bug sbnweb 03/04/2017

//        if (leg.getRelatorCode() != null)
//        	ta.setCD_RELAZIONE(leg.getRelatorCode().toString());
        String cd_relazione;
        if (leg.getRelatorCode() != null) {
        	ta.setCD_RELAZIONE(leg.getRelatorCode().toString());
            cd_relazione = leg.getRelatorCode().toString();
        } else {
            cd_relazione = "   ";
        }


        if (leg.getTipoRespons() != null)
        	ta.setTP_RESPONSABILITA(leg.getTipoRespons().toString());
        //ta.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID())));
        ta.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID() + cd_relazione + ta.getTP_RESPONSABILITA())));
        Tr_tit_autResult tavola = new Tr_tit_autResult(ta);
        tavola.executeCustom("updateDisabilita");
    }
    //20080520120513.31
    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(Vf_titolo_aut tit, String clet, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(Vf_titolo_aut tit, String nome, String clet, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaLike, nome);
        if (clet != null)
            tit.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(Vf_titolo_aut tit, String nome, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaEsatta, nome);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;

    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike(Vf_titolo_aut tit, String nome, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaLike, nome);
        if (clet != null)
            tit.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(Vf_titolo_aut tit, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("countPerClet");
        int n = conta(tavola);

        return n;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto(Vf_titolo_aut tit, String nomeEsatto) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);


        tit.settaParametro(TableDao.XXXstringaEsatta, nomeEsatto);
        tavola.executeCustom("countPerNomeEsatto");
        int n = conta(tavola);

        return n;
    }

    public void valorizzaFiltri(Vf_titolo_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        Titolo titolo = new Titolo();
        titolo.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        autore.setCD_RELAZIONE(elAut.getRelatorCode());
        if (elAut.getTipoRespons() != null)
            autore.setTP_RESPONSABILITA(elAut.getTipoRespons().toString());
        //??
        if (elAut.getChiaviAutoreCerca() != null) {
            autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
            autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
        }
        CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
        if (canali != null) {
            autore.setVID(canali.getT001());
            StringaCercaType stringaCerca = canali.getStringaCerca();
            if (stringaCerca != null) {
                autore.settaParametro(TableDao.XXXStringaEsattaAutore, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
                autore.settaParametro(TableDao.XXXStringaLikeAutore, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
            }
        }
    }
    /**
     * Method estraiTitoloAutore.
     * @param string
     * @param string1
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tr_tit_aut estraiTitoloAutore(String bid, String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_aut ta = new Tr_tit_aut();
        ta.setVID(vid);
        ta.setBID(bid);
        Tr_tit_autResult tavola = new Tr_tit_autResult(ta);


        tavola.executeCustom("selectPerBideVid");
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_tit_aut) v.get(0);
        return null;
    }


    //  Inizio manutenzione riportata da Indice Su SbnWEB BUG 2982
	/**Modifica Mantis 1353 Aggiunto metodo
	 * Method estraiTitoloAutore.
	 * @param string
	 * @param string1
	 */
	public Tr_tit_aut estraiTitoloAutore(String bid, String vid, String tp_responsabilita ) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_tit_aut ta = new Tr_tit_aut();
		ta.setVID(vid);
		ta.setBID(bid);
		ta.setTP_RESPONSABILITA(tp_responsabilita);
		Tr_tit_autResult tavola = new Tr_tit_autResult(ta);


		tavola.executeCustom("selectPerKeyResp");
		List v = tavola.getElencoRisultati();

		if (v.size() > 0)
			return (Tr_tit_aut) v.get(0);
		return null;
	}
    /**
     // Fine manutenzione riportata da Indice Su SbnWEB BUG 2982



    /**
     * Method estraiTitoloAutore.
     * @param string
     * @param string1
     * @throws InfrastructureException
     */
    public Tr_tit_aut estraiTitoloAutore(
        String bid,
        String vid,
        String cd_relazione,
        String tp_responsabilita)
        throws EccezioneDB, InfrastructureException {
        Tr_tit_aut ta = new Tr_tit_aut();
        ta.setVID(vid);
        ta.setBID(bid);
        ta.setTP_RESPONSABILITA(tp_responsabilita);
        ta.setCD_RELAZIONE(cd_relazione);
        Tr_tit_autResult tavola = new Tr_tit_autResult(ta);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(ta.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_tit_aut) v.get(0);
        return null;
    }
    /**
     * metodo per estrarre tutti i legami a partire da un determinato bid
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List estraiTitoliAutore(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_aut titoloAutore = new Tr_tit_aut();
        titoloAutore.setBID(bid);
        Tr_tit_autResult tavola = new Tr_tit_autResult(titoloAutore);


        tavola.executeCustom("selectPerTitolo");
        List TableDaoResult = tavola.getElencoRisultati();

        return TableDaoResult;
    }

    /**
     * Per ogni elemento presente nel vettore dei legami:
     * - faccio update mettendo in vid il valore di spostaId
     * - dal bid del legame cerco il titolo e aggiorno anche quello in tb_titolo
     * - aggiorno anche la tr_tit_bib
     * Method updateTitoliAutore.
     * @param vettoreDiLegami
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateTitoliAutore(
            String idPartenza,
            String idArrivo,
            String relazione,
            String responsabilita,
            String user,
            String bid,
            TimestampHash timeHash)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_aut titoloAutore = new Tr_tit_aut();

    	// prima cerco il dati della condivisione sulla tabella autori cercando lo schiacciante
        // MODIFICA RICHIESTA DAL LARA
        // IN FASE DI SPOSTAMENTO LEGAMI DEVO LEGGERE PRIMA I DATI DELLA CONDIVISIONE
        // PER POI PASSARLI ALL'AGGIORNAMENTO
    		Tb_autore tb_autore = new Tb_autore();
    		tb_autore.setVID(idArrivo);
    		Tb_autoreResult tb_res = new Tb_autoreResult(tb_autore);
    		tb_res.executeCustom("selectPerKey");
    		List v = tb_res.getElencoRisultati();
    	       if (v.size() > 0){
    	    	   tb_autore = (Tb_autore) v.get(0);
    	           titoloAutore.setFL_CONDIVISO(tb_autore.getFL_CONDIVISO());
    	           titoloAutore.setUTE_CONDIVISO(tb_autore.getUTE_CONDIVISO());
    	           titoloAutore.setTS_CONDIVISO(tb_autore.getTS_CONDIVISO());
    	       }
    	       else{ // non dovrebbe mai avvenire
    	           titoloAutore.setFL_CONDIVISO(" ");
    	           titoloAutore.setUTE_CONDIVISO(null);
    	           titoloAutore.setTS_CONDIVISO(null);
    	       }
    	// End
    	    // dopo procedo regolamante
            titoloAutore.settaParametro(TableDao.XXXidPartenza, idPartenza);
            titoloAutore.settaParametro(TableDao.XXXidArrivo, idArrivo);
            titoloAutore.setUTE_VAR(user);
            titoloAutore.setBID(bid);

            // bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
            // titoloAutore.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", bid + idPartenza)));
            titoloAutore.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", bid + idPartenza + relazione + responsabilita)));
            Tr_tit_autResult tavola = new Tr_tit_autResult(titoloAutore);


            titoloAutore.setVID(idArrivo);
            tavola.executeCustom("selectEsistenzaPerBideVid");
            if (tavola.getElencoRisultati().size()>0) {
                tavola.executeCustom("deletePerVidEBid");
            }
            tavola.executeCustom("updateLegameAutore");

            Titolo titolo = new Titolo();
            Tb_titolo tb_titolo = titolo.estraiTitoloPerID(titoloAutore.getBID());
            if (tb_titolo != null) {
                tb_titolo.setUTE_VAR(user);
                tb_titolo.setTS_VAR(titoloAutore.getTS_VAR());
                Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);
                tb_titoloResult.executeCustom("updateTitolo");
                AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
                allineamentoTitolo.setTr_tit_aut(true);
                TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
                titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
            }

        }

    public void updateTitoliAutoreOrg(
            String idPartenza,
            String idArrivo,
            String relazione,
            String responsabilita,
            String user,
            String bid,
            TimestampHash timeHash)
            throws IllegalArgumentException, InvocationTargetException, Exception {
            Tr_tit_aut titoloAutore = new Tr_tit_aut();
            titoloAutore.settaParametro(TableDao.XXXidPartenza, idPartenza);
            titoloAutore.settaParametro(TableDao.XXXidArrivo, idArrivo);
            titoloAutore.setUTE_VAR(user);
            titoloAutore.setBID(bid);

            // bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
            // titoloAutore.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", bid + idPartenza)));
            titoloAutore.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_tit_aut", bid + idPartenza + relazione + responsabilita)));
            Tr_tit_autResult tavola = new Tr_tit_autResult(titoloAutore);


            titoloAutore.setVID(idArrivo);
            tavola.executeCustom("selectEsistenzaPerBideVid");
            if (tavola.getElencoRisultati().size()>0) {
                tavola.executeCustom("deletePerVidEBid");
            }
            tavola.executeCustom("updateLegameAutore");

            Titolo titolo = new Titolo();
            Tb_titolo tb_titolo = titolo.estraiTitoloPerID(titoloAutore.getBID());
            if (tb_titolo != null) {
                tb_titolo.setUTE_VAR(user);
                tb_titolo.setTS_VAR(titoloAutore.getTS_VAR());
                Tb_titoloResult tb_titoloResult = new Tb_titoloResult(tb_titolo);
                tb_titoloResult.executeCustom("updateTitolo");
                AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(tb_titolo);
                allineamentoTitolo.setTr_tit_aut(true);
                TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
                titoloG.aggiornaFlagAllineamento(allineamentoTitolo, user);
            }

        }


    public void cancellaPerBid(String bid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_aut tit_luo = new Tr_tit_aut();
        tit_luo.setBID(bid);
        tit_luo.setUTE_VAR(ute_var);
        Tr_tit_autResult tavola = new Tr_tit_autResult(tit_luo);
        tavola.executeCustom("updateCancellaPerBid");
    }

    public void cancellaLegameTitoloAutore(String id_partenza, String bid, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_aut tit_aut = new Tr_tit_aut();
        tit_aut.setBID(bid);
        tit_aut.setVID(id_partenza);
        tit_aut.setUTE_VAR(user);
        Tr_tit_autResult tavola = new Tr_tit_autResult(tit_aut);
        tavola.executeCustom("updateCancellaLegameTitAut");
    }

}
