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
import it.finsiel.sbn.polo.dao.entity.viste.Vl_autore_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_autore_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_autore_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAutoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe per la gestione delle operazioni complesse sulla tavola Tb_autore.
 */
public class Autore extends Tb_autore {


    /**
	 * 
	 */
	private static final long serialVersionUID = 8288325867550247031L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.Autore");
    boolean filtriValorizzati = false;
    boolean esporta = false;
    Tb_autore tb_autore = new Tb_autore();

    public Autore() {
    }

    public void setEsporta(boolean export) {
        esporta = export;
    }


    /**
     * Esegue una ricerca per gli autori legati ad un documento per il factoring CercaAutore,
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerTitolo(String idArrivo, CercaAutoreType cerca, CercaDatiAutType cercaEl, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_autore_tit tit_aut = new Vl_autore_tit();
        tit_aut.setBID(idArrivo);
        valorizzaFiltri(tit_aut, cerca, cercaEl);
        Vl_autore_titResult tavola = new Vl_autore_titResult(tit_aut);
        tavola.executeCustom("selectAutorePerTitolo", ordinamento);
        //return tavola.getElencoRisultati();
        return tavola;
    }

    public int contaAutorePerTitolo(String idArrivo, CercaAutoreType cerca, CercaDatiAutType cercaEl) throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_autore_tit tit_aut = new Vl_autore_tit();
        tit_aut.setBID(idArrivo);
        valorizzaFiltri(tit_aut, cerca,cercaEl);
        Vl_autore_titResult tavola = new Vl_autore_titResult(tit_aut);
        tavola.executeCustom("countAutorePerTitolo");
        int n = conta(tavola);
        return n;
    }

    /** Esegue una ricerca per gli autori legati ad un documento, per il factoring CercaTitolo
     * @return TableDao Riordinato per tp_responsabilità (in ordine crescente, ma con lo 0 in fondo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaAutorePerTitolo(String id_titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_autore_tit tit_aut = new Vl_autore_tit();
        tit_aut.setBID(id_titolo);
        Vl_autore_titResult tavola = new Vl_autore_titResult(tit_aut);
        tavola.executeCustom("selectAutorePerTitolo", "order_tp_resp");
        List v = tavola.getElencoRisultati();
        int size = v.size();
        //Se il vettore non è vuoto e ha almeno un elemento con tp_resp != 0.
        // originaleif (v.size()>1 && !((Vl_autore_tit)v.lastElement()).getTP_RESPONSABILITA().equals("0")) {
        //if (v.size()>1 && !((Vl_autore_tit)v.get(v.size())).getTP_RESPONSABILITA().equals("0")) {
        if (v.size()>1 && !((Vl_autore_tit)v.get(v.size()-1)).getTP_RESPONSABILITA().equals("0")) {
            //Leggo gli elementi con tp_resp == 0 e li accodo in fondo.
            while(((Vl_autore_tit)v.get(0)).getTP_RESPONSABILITA().equals("0"))
                v.add(v.remove(0));
        }
        return v;
    }

    /**
     * Metodo per cercare gli autori collegati a una marca
     * Input: idArrivo statusAuthority, formaNome
     * ricerca su Tb_autore dove TR_aut_mar.VID = Tb_autore.VID e
     * TR_aut_mar.MID = idArrivo
     * se i filtri sono valorizzati li applica alla select
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerMarca(String idArrivo, CercaAutoreType cerca, CercaDatiAutType cercaEl, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_autore_mar autore = new Vl_autore_mar();
        autore.setMID(idArrivo);
        Vl_autore_marResult tavola = new Vl_autore_marResult(autore);
        valorizzaFiltri(autore, cerca, cercaEl);
        tavola.executeCustom("selectAutorePerMarca", ordinamento);
        return tavola;
    }

    public int contaAutorePerMarca(String idArrivo, CercaAutoreType cerca, CercaDatiAutType cercaEl) throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_autore_mar autore = new Vl_autore_mar();
        autore.setMID(idArrivo);
        Vl_autore_marResult tavola = new Vl_autore_marResult(autore);
        valorizzaFiltri(autore, cerca, cercaEl);
        tavola.executeCustom("countAutorePerMarca");
        int n = conta(tavola);
        return n;
    }

    /**
     * Metodo per leggere i rinvii di un autore
     * Input: VID
     * 1) ricerca su Tb_autore dove TR_aut_aut.vid_base =  VID in input e
     * Tb_autore.VID = TR_aut_aut.vid_coll
     * 2) ricerca su Tb_autore dove TR_aut_aut.vid_coll =  vid in input e
     * Tb_autore.vid = TR_aut_aut.vid_base
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaRinviiAutore(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_autore_aut autore = new Vl_autore_aut();
        autore.setVID_1(vid);
        Vl_autore_autResult tavola = new Vl_autore_autResult(autore);
        tavola.executeCustom("selectAutorePerRinvii", "order_tp_legame");
        List v = tavola.getElencoRisultati();
        return v;
    }

    /**
     * Metodo per cercare gli autori con intervallo di date di variazione
     * Input: T005_Range, statusAuthority, formaNome
     * ricerca su Tb_autore per intervallo su ts_var
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerDatavar(SbnRangeDate t005_range)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXdata_var_da, t005_range.getDataDa().toString());
        settaParametro(TableDao.XXXdata_var_a, t005_range.getDataA().toString());
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("selectDirettaPerDatavar");
        return tavola;
    }

    private void preparaNome (String nome,String nome2) {
        if (nome.length()>50) {
            tb_autore.setKY_CLES1_A(nome.substring(0,50));
            tb_autore.setKY_CLES2_A(nome.substring(50));
        } else {
            tb_autore.setKY_CLES1_A(nome);
            tb_autore.setKY_CLES2_A(null);
            tb_autore.settaParametro(TableDao.XXXky_cles2_anull,"NULL");
        }
        if (nome2 != null) {
            if (nome2.length()>50) {
                tb_autore.settaParametro(TableDao.XXXcles2_1,nome2.substring(0,50));
                tb_autore.settaParametro(TableDao.XXXcles2_2,nome2.substring(50));
            } else {
                tb_autore.settaParametro(TableDao.XXXcles2_1,nome2);
                tb_autore.settaParametro(TableDao.XXXcles2_2,null);
                tb_autore.settaParametro(TableDao.XXXky_cles2_2null,"NULL");
            }
        }
    }
    /**
     * Metodo per cercare l'autore con stringa nome.
     * ricerca su Tb_autore con ky_cles1_a ky_cles2_a
     * Input: nome, statusAuthority, formaNome
     * applica la routine di normalizzazione al nome
     * esegue la select * su Tb_autore
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * Esegue la preparazione dell'output richiesto in tipoOutput.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerNome(String nome,String nome2, String auteur, CercaAutoreType cerca, CercaDatiAutType cercaEl, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        preparaNome(nome, nome2);
        tb_autore.setKY_AUTEUR(auteur);
        if (!filtriValorizzati)
            valorizzaFiltri(tb_autore,cerca,cercaEl);


        tavola.executeCustom("selectPerNome", ordinamento);
        //return tavola.getElencoRisultati();
        return convertiDaVl_autore_tit(tavola);
    }

    /**
     * Method contaAutorePerNome.
     * @param nome
     * @param cercaAutore
     * @param tipoOrd
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaAutorePerNome(String nome,String nome2, String auteur, CercaAutoreType cercaAutore, CercaDatiAutType cercaEl)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tb_autore = new Vl_autore_tit();
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        tb_autore.setKY_AUTEUR(auteur);
        preparaNome(nome, nome2);
        valorizzaFiltri(tb_autore,cercaAutore, cercaEl);
        tavola.executeCustom("countPerNome");
        int n = conta(tavola);

        return n;
    }

    public TableDao cercaAutorePerNomeLike(
        String nome,
        String nome2,
        String auteur,
        CercaAutoreType cerca,
        CercaDatiAutType cercaEl,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        preparaNome(nome, nome2);
        tb_autore.setKY_AUTEUR(auteur);
        if (!filtriValorizzati)
            valorizzaFiltri(tb_autore, cerca,cercaEl);


        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return convertiDaVl_autore_tit(tavola);

    }

    public int contaAutorePerNomeLike(String nome,String nome2, String auteur, CercaAutoreType cercaAutore, CercaDatiAutType cercaEl)
        throws IllegalArgumentException, InvocationTargetException, Exception {
            tb_autore.setKY_AUTEUR(auteur);
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tb_autore = new Vl_autore_tit();
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        preparaNome(nome, nome2);
        valorizzaFiltri(tb_autore, cercaAutore, cercaEl);
        tavola.executeCustom("countPerNomeLike");

        int n = conta(tavola);

        return n;
    }

    /**
     * Method cercaAutorePerParoleNome.
     * @param string
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerParoleNome(
        String[] paroleNome,
        CercaAutoreType cercaAutore,
        CercaDatiAutType cercaEl,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        for (int i = 1; i <= paroleNome.length; i++)
            tb_autore.settaParametro("XXXparola" + i, paroleNome[i - 1]);
        if (!filtriValorizzati)
            valorizzaFiltri(tb_autore,cercaAutore,cercaEl);
        tavola.executeCustom("selectPerParoleNome", ordinamento);

        return convertiDaVl_autore_tit(tavola);
    }
    public int contaAutorePerParoleNome(String[] paroleNome, CercaAutoreType cercaAutore, CercaDatiAutType cercaEl)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        if (hasRelatorCode(cercaEl)) {
            tb_autore = new Vl_autore_tit();
            tavola = new Vl_autore_titResult((Vl_autore_tit)tb_autore);
        } else {
            tavola = new Tb_autoreResult(tb_autore);
        }
        for (int i = 1; i <= paroleNome.length; i++)
            tb_autore.settaParametro("XXXparola" + i, paroleNome[i - 1]);
        valorizzaFiltri(tb_autore, cercaAutore, cercaEl);


        tavola.executeCustom("countPerParoleNome");
        int n = conta(tavola);

        return n;
    }
    private boolean hasRelatorCode(CercaDatiAutType cercaEl) {
        return (cercaEl != null && cercaEl.getRelatorCode() != null);
    }
    /** Valorizza i filtri in base al contenuto del CercaAutoreType */
    public void valorizzaFiltri(CercaAutoreType cerca, CercaDatiAutType cercaEl) {
        valorizzaFiltri(this, cerca, cercaEl);
    }

    /** Valorizza i filtri di autore in base al contenuto del CercaAutoreType */
    public Tb_autore valorizzaFiltri(Tb_autore autore, CercaAutoreType cerca, CercaDatiAutType cercaEl) {
        filtriValorizzati = true;
        if (cercaEl != null ) {
            if (cercaEl.getLivelloAut_Da() != null)
                autore.settaParametro(TableDao.XXXlivello_aut_da,Decodificatore.livelloSogliaDa(cercaEl.getLivelloAut_Da().toString()));
            if (cercaEl.getLivelloAut_A() != null)
                autore.settaParametro(TableDao.XXXlivello_aut_a, cercaEl.getLivelloAut_A().toString());
            if (cercaEl.getFormaNome() != null)
                autore.setTP_FORMA_AUT(cercaEl.getFormaNome().toString());
            SbnRangeDate t005_range = cercaEl.getT005_Range();
            if (t005_range != null) {
                if (t005_range.hasTipoFiltroDate() && t005_range.getTipoFiltroDate()==2) {
                    if (esporta) {
                        autore.settaParametro(TableDao.XXXesporta_ts_var_e_ts_ins_da,t005_range.getDataDa().toString());
                        autore.settaParametro(TableDao.XXXesporta_ts_var_a, t005_range.getDataA().toString());
                    } else {
                        autore.settaParametro(TableDao.XXXdata_ins_Da, t005_range.getDataDa().toString());
                        autore.settaParametro(TableDao.XXXdata_ins_A, t005_range.getDataA().toString());
                    }
                } else {
                    if (esporta)  {
                        autore.settaParametro(TableDao.XXXesporta_ts_var_da,t005_range.getDataDa().toString());
                        autore.settaParametro(TableDao.XXXesporta_ts_var_a, t005_range.getDataA().toString());
                    } else {
                        autore.settaParametro(TableDao.XXXdata_var_Da, t005_range.getDataDa().toString());
                        autore.settaParametro(TableDao.XXXdata_var_A, t005_range.getDataA().toString());
                    }
                }
            }
            if (cercaEl.getRelatorCode() != null) {
                autore.settaParametro(TableDao.XXXrelatorCode,Decodificatore.getCd_tabella("Tr_tit_aut","cd_relazione",cercaEl.getRelatorCode().toString()));
            }
        }
        if (cerca != null) {
            if (cerca.getT102() != null)
                autore.setCD_PAESE(cerca.getT102().getA_102());
            SbnTipoNomeAutore[] tipoNome = cerca.getTipoNome();
            if (tipoNome != null && tipoNome.length > 0)
                for (int i = 1; i <= tipoNome.length; i++)
                    autore.settaParametro("XXXtipoNome" + i, tipoNome[i - 1].toString());
            if (cerca.getDataInizio_Da() != null)
                autore.settaParametro(TableDao.XXXdataInizio_Da, cerca.getDataInizio_Da().toString());
            if (cerca.getDataInizio_A() != null)
                autore.settaParametro(TableDao.XXXdataInizio_A, cerca.getDataInizio_A().toString());
            if (cerca.getDataFine_Da() != null)
                autore.settaParametro(TableDao.XXXdataFine_Da, cerca.getDataFine_Da().toString());
            if (cerca.getDataFine_A() != null)
                autore.settaParametro(TableDao.XXXdataFine_A, cerca.getDataFine_A().toString());
        }
        return autore;
    }

    /**
     * Metodo per cercare l'autore con n. identificativo:
     * ricerca su Tb_autore con VID
     * Output possibili:
     * . autore inesistente
     * . autore trovato: tutti gli attributi
     * @throws InfrastructureException
     */
    public TableDao cercaAutorePerID(String id) throws EccezioneDB, InfrastructureException {
        Tb_autore autore = null;
        setVID(id);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        return tavola;
    }
    public Tb_autore estraiAutorePerID(String id) throws EccezioneDB, InfrastructureException {
        Tb_autore autore = null;
        TableDao tavola = cercaAutorePerID(id);
        List v = tavola.getElencoRisultati();
        if (v.size() > 0)
            autore = (Tb_autore) v.get(0);
        return autore;
    }


 // Inizio Marzo 2016: gestione ISNI (International standard number identifier)
//    public TableDao cercaAutorePerISADN(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
//        setISADN(id);
//        Tb_autoreResult tavola = new Tb_autoreResult(this);
//        //if (!filtriValorizzati) valorizzaFiltri(cerca); -> da modificare anche Xml
//        tavola.executeCustom("selectPerIsadn");
//        return tavola;
//    }
    public TableDao estraiAutorePerISNI(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        setISADN(id);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("selectPerIsadn");
        return tavola;
    }

    public TableDao estraiAutorePerISNIMod(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
    	setISADN(id);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("selectPerIsadnMod");
        return tavola;
    }
    // Fine Marzo 2016: gestione ISNI (International standard number identifier)



    /**
     * Inserisce un autore nel DB
     * @throws InfrastructureException
     */
    public void inserisciAutore(Tb_autore autore) throws EccezioneDB, InfrastructureException {
        Tb_autoreResult tautore = new Tb_autoreResult(autore);
        tautore.insert(autore);
    }

    /**
     * Method cercaAutorePerAuteurOrCautun.
     * @param tb_autore
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutoreSimile(
        Tb_autore autore,
        CercaAutoreType cerca,
        String tipo_ricerca,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_autoreResult tavola = new Tb_autoreResult(autore);
        tavola.executeCustom(tipo_ricerca, ordinamento);
        return tavola;
    }

    public int contaAutoreSimile(Tb_autore autore, CercaAutoreType cerca) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_autoreResult tavola = new Tb_autoreResult(autore);
        tavola.executeCustom("countSimile");
        int n = conta(tavola);
        return n;
    }
    /**
     * Method cercaAutorePerCautun.
     * @param tb_autore
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerCautun(String cautun, CercaAutoreType cerca, CercaDatiAutType cercaEl, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_CAUTUN(cautun);
        if (!filtriValorizzati)
            valorizzaFiltri(cerca, cercaEl);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("selectPerCautun", ordinamento);
        return tavola;
    }

    public int contaAutorePerCautun(String cautun, CercaAutoreType cerca, CercaDatiAutType cercaEl) throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_CAUTUN(cautun);
        valorizzaFiltri(cerca, cercaEl);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("countPerCautun");
        int n = conta(tavola);
        return n;
    }

    /**
     * Method cercaAutorePerAuteur.
     * @param tb_autore
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaAutorePerAuteur(String auteur, CercaAutoreType cerca, CercaDatiAutType cercaEl, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_AUTEUR(auteur);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        if (!filtriValorizzati)
            valorizzaFiltri(cerca, cercaEl);
        tavola.executeCustom("selectPerAuteur", ordinamento);
        return tavola;
    }

    public int contaAutorePerAuteur(String auteur, CercaAutoreType cerca, CercaDatiAutType cercaEl) throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_AUTEUR(auteur);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        valorizzaFiltri(cerca, cercaEl);
        tavola.executeCustom("countPerAuteur");
        int n = conta(tavola);
        return n;
    }

    //E' una forzatura, ma funziona
    private TableDao convertiDaVl_autore_tit(TableDao vl_aut) throws EccezioneDB, InfrastructureException {
        if (vl_aut instanceof Tb_autoreResult)
            return vl_aut;
        Tb_autoreResult tavola = new Tb_autoreResult(null);
        tavola.valorizzaElencoRisultati(vl_aut.getElencoRisultati());
        return tavola;
    }

    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }
    /**
     * this.getAutori_no_raffinamento(),
     * "XXXMar123456", //cd utente che ha richiesto l'operazione
     * "001", //tipo di output lista sintetica 001=MAX (SEMPRE)
     * "1", //tipo ordinamento (da 1 a 5)
     * "BIS014", // Tipo report realizzato
     * id_processo+"_SBNMARC", // parte iniziale nome file sequenza da generare
     * "100") ; // Numero massimo di righe per blocco
     */
//
//    public void realizzaReport(
//        TableDao autori,
//        SbnUserType user,
//        String tipoOut,
//        String tipoOrd,
//        String tipoReport,
//        String nomeFile,
//        int maxRighe) {
//        int elementCounter = 0;
//        BigDecimal schemaVersion = new BigDecimal(IndiceServiceLocator.getProperty("SCHEMA_VERSION"));
//        SbnTipoOutput sbnOut = SbnTipoOutput.valueOf(tipoOut);
//        SbnTipoOrd sbnOrd = SbnTipoOrd.valueOf(tipoOrd);
//        ProcessoInDifferita processo_in_differita = new ProcessoInDifferita();
//        String xml;
//        FormatoAutore formatoAutore = new FormatoAutore(sbnOut, "order_" + tipoOrd, user);
//        int numeroFile = 0;
//        for (; elementCounter < autori.size(); elementCounter += maxRighe) {
//            try {
//                xml =
//                    formatoAutore.formattaLista(
//                        autori,
//                        user,
//                        sbnOut,
//                        sbnOrd,
//                        null,
//                        elementCounter,
//                        maxRighe,
//                        autori.size(),
//                        schemaVersion);
//            } catch (EccezioneIccu ecc) {
//                log.error("Eccezione nella preparazione della lista di autori:" + ecc);
//                xml = FormatoErrore.preparaMessaggioErrore(ecc, user);
//            } catch (IllegalArgumentException ecc) {
//                log.error("Eccezione argomenti:" + ecc);
//                xml =
//                    FormatoErrore.preparaMessaggioErrore(
//                        new EccezioneSbnDiagnostico(-1, "Argomento sbagliato", ecc),
//                        user);
//            }
//            processo_in_differita.putFile(nomeFile + numeroFile + ".xml", xml);
//            numeroFile++;
//        }
//    }
//

    //
//    public void realizzaReport(
//        VectorFileAutore autori,
//        String utente,
//        String tipoOut,
//        String tipoOrd,
//        String tipoReport,
//        String nomeFile,
//        int maxRighe) {
//        int elementCounter = 0;
//        SbnUserType user = new SbnUserType();
//        user.setBiblioteca(utente.substring(0, 6));
//        BigDecimal schemaVersion = new BigDecimal(IndiceServiceLocator.getProperty("SCHEMA_VERSION"));
//        user.setUserId(utente.substring(6));
//        SbnTipoOutput sbnOut = SbnTipoOutput.valueOf(tipoOut);
//        SbnTipoOrd sbnOrd = SbnTipoOrd.valueOf(tipoOrd);
//        ProcessoInDifferita processo_in_differita = new ProcessoInDifferita();
//        String xml;
//        FormatoAutore formatoAutore = new FormatoAutore(sbnOut, "order_" + tipoOrd, user);
//        int numeroFile = 0;
//        for (; elementCounter < autori.size(); elementCounter += maxRighe) {
//            StringWriter sw = new StringWriter();
//            try {
//                    formatoAutore.formattaVectorFile(
//                        autori,
//                        user,
//                        sbnOut,
//                        sbnOrd,
//                        nomeFile.substring(0, nomeFile.indexOf("_")),
//                        elementCounter,
//                        maxRighe,
//                        autori.size(),
//                        schemaVersion)
//                    .marshal(sw);
//                xml = sw.toString();
//            } catch (EccezioneIccu ecc) {
//                log.error("Eccezione nella preparazione della lista di autori:" + ecc);
//                xml = FormatoErrore.preparaMessaggioErrore(ecc, user);
//            } catch (IllegalArgumentException ecc) {
//                log.error("Eccezione argomenti:" + ecc);
//                xml =
//                    FormatoErrore.preparaMessaggioErrore(
//                        new EccezioneSbnDiagnostico(-1, "Argomento sbagliato", ecc),
//                        user);
//            } catch (MarshalException e) {
//                log.error("Errore marshalling", e);
//                xml = FormatoErrore.preparaMessaggioErrore(101, user);
//            } catch (ValidationException e) {
//                log.error("Errore marshalling", e);
//                xml = FormatoErrore.preparaMessaggioErrore(101, user);
//            }
//            processo_in_differita.putFile(nomeFile + numeroFile + ".xml", xml);
//            numeroFile++;
//        }
//    }
//
    /**
     * Method eseguiUpdate per lo scambio forma.
     * @param autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void eseguiUpdateScambioForma(Tb_autore autore1, Tb_autore autore2) throws IllegalArgumentException, InvocationTargetException, Exception {


        //Devo annullare l'ISADN dell'autore2 prima di copiarlo.
        if (autore1.getISADN() != null) {
            Tb_autoreResult tavola = new Tb_autoreResult(autore2);
            tavola.executeCustom("annullaIsadn");
        }
        Tb_autoreResult tavola = new Tb_autoreResult(autore1);
        tavola.executeCustomUpdate("updateScambioForma");

        tavola = new Tb_autoreResult(autore2);
        tavola.executeCustomUpdate("updateScambioForma");

    }


	public void cancellaAutore(Tb_autore autore, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		//cancella legami con marche e repertori
		AutoreMarca autoreMarca = new AutoreMarca();
		autoreMarca.cancellaLegamiMarche(autore.getVID(),user);
		RepertorioAutore repertorioAutore = new RepertorioAutore();
		repertorioAutore.cancellaLegamiRepertori(autore.getVID(),user);
		autore.setUTE_VAR(user);
        Tb_autoreResult tavola = new Tb_autoreResult(autore);
        autore.setVID_LINK(autore.getVID());
        tavola.executeCustomUpdate("updateCancellaAutore");

	}

    public void cancellaAutoreFuso(Tb_autore autore, String vid_link, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
        //cancella legami con marche e repertori
        AutoreMarca autoreMarca = new AutoreMarca();
        autoreMarca.cancellaLegamiMarche(autore.getVID(),user);
        RepertorioAutore repertorioAutore = new RepertorioAutore();
        repertorioAutore.cancellaLegamiRepertori(autore.getVID(),user);
        autore.setUTE_VAR(user);
        Tb_autoreResult tavola = new Tb_autoreResult(autore);
        autore.setVID_LINK(vid_link);
        tavola.executeCustomUpdate("updateCancellaAutore");

    }


    /**
     * Method eseguiUpdate.
     * @param autore
     * @throws InfrastructureException
     */
    public void eseguiUpdate(Tb_autore autore) throws EccezioneDB, InfrastructureException {
        Tb_autoreResult tavola = new Tb_autoreResult(autore);
        tavola.update(autore);
    }

    /**
     * Method cercaDueAutoriPerID.
     * @param vid1
     * @param vid2
     * /
    public TableDao cercaDueAutoriPerID(String vid1, String vid2) throws EccezioneDB{
        settaParametro(TableDao.XXXvid1",vid1);
        settaParametro(TableDao.XXXvid2",vid2);
        Tb_autoreResult tavola = new Tb_autoreResult(this);


        tavola.executeCustom("selectDueAutoriPerVid");
        return tavola.getElencoRisultati();
    }

    /**
     * Method rimuoviAutoreRinviato.
     * @param string
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void rimuoviAutoreRinviato(String vid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(vid);
        setVID_LINK(vid);
        setUTE_VAR(ute_var);
        Tb_autoreResult tavola = new Tb_autoreResult(this);
        tavola.executeCustom("updateCancellaAutore");
    }

    /**
     * @param aut2
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateVariazione(String vid, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
			setVID(vid);
			setUTE_VAR(ute_var);
			Tb_autoreResult tavola = new Tb_autoreResult(this);
			tavola.executeCustom("updateVariazione");
		}
}
