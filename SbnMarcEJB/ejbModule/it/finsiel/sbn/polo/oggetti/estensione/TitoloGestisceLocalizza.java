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
import it.finsiel.sbn.polo.dao.entity.viste.Vl_biblioteca_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.transactionmaker.LocalizzaFactoring;
import it.finsiel.sbn.polo.oggetti.AutoreAutore;
import it.finsiel.sbn.polo.oggetti.Biblioteca;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.oggetti.TitoloTitB;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_aut_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.viste.Vl_biblioteca_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.util.CodiciAttivita;
import it.finsiel.sbn.util.PoloUtility;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Gestisce le operazioni riguardanti le localizzazioni di titoli (documenti,
 * titoli uniformi, titoli di accesso):
 * Localizza un titolo, delocalizza un titolo, ricerca le localizzazioni di un
 * titolo.
 */
public class TitoloGestisceLocalizza extends TitoloBiblioteca {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4358513367990114020L;
	Connection _db_conn = null;
    static Category log = Category.getInstance("iccu.serversbnmarc.titoloBiblio");
    public TitoloGestisceLocalizza() {
        super();

        //

    }

    /**
     * Ricerca le localizzazioni di un titolo.
     * verifica l'esistenza del titolo (T001) in tb_titolo (chiama verificaEsistenzaID
     * di TitoloValida)
     * se non esiste segnala un diagnostico.
     * se esiste legge in tr_tit_bib secondo tipoLocalizza: se Gestione fl_possesso
     * deve essere = 'S', se 'Possesso' fl_possesso deve essere = 'S'.
     * se c1_899 è compilato si applica una condizione di join con tb_biblioteca, dove
     * cd_ana_codice like c1_899
     * se c2_899 è compilato (in alternativa a c1_899, altrimenti si segnala
     * diagnostico) si applica la condizione di like su cd_polo e cd_ biblioteca di
     * tr_tit_bib
     * Prepara l'output secondo localizzzaInfoType
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */

    public List cercaLocalizzaTitolo(String IDtitolo, int tipoLocalizza, String c1_899, String c2_899)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List TableDaoResult = new ArrayList();
        TitoloValida titoloValida = new TitoloValida();
        Tb_titolo tb_titolo = null;
        tb_titolo = titoloValida.verificaEsistenzaID(IDtitolo);
        if (tb_titolo != null) {
            Vl_biblioteca_tit vl_biblioteca_tit = new Vl_biblioteca_tit();
            if (tipoLocalizza == SbnTipoLocalizza.GESTIONE.getType())
                vl_biblioteca_tit.setFL_GESTIONE("S");
            if (tipoLocalizza == SbnTipoLocalizza.POSSESSO.getType())
                vl_biblioteca_tit.setFL_POSSESSO("S");
            if ((c1_899 != null) && (c2_899 != null))
                throw new EccezioneSbnDiagnostico(3018);
            else if (c2_899 != null) {
                //guardo nella vista tutti quelli con c2_899
                vl_biblioteca_tit.setBID(IDtitolo);
                vl_biblioteca_tit.setCod_polo(c2_899.substring(0, 3));
                if (!c2_899.substring(3, 6).trim().equals(""))
                    vl_biblioteca_tit.setCd_biblioteca(c2_899.substring(3, 6));
                Vl_biblioteca_titResult vl_biblioteca_titResult =
                    new Vl_biblioteca_titResult(vl_biblioteca_tit);


                vl_biblioteca_titResult.executeCustom("selectPerPolo");
                TableDaoResult = vl_biblioteca_titResult.getElencoRisultati();

                //eseguo la query sulla prima vista
            } else if (c1_899 != null) {
                //					Vl_biblioteca_tit Vl_biblioteca_tit = new Vl_biblioteca_tit();
                vl_biblioteca_tit.setCd_ana_biblioteca(c1_899);
                vl_biblioteca_tit.setBID(IDtitolo);
                Vl_biblioteca_titResult vl_biblioteca_titResult =
                    new Vl_biblioteca_titResult(vl_biblioteca_tit);


                vl_biblioteca_titResult.executeCustom("selectPerAnagrafeLike");
                TableDaoResult = vl_biblioteca_titResult.getElencoRisultati();

                //eseguo la query sulla seconda select
            } else {
                vl_biblioteca_tit.setBID(IDtitolo);
                Vl_biblioteca_titResult vl_biblioteca_titResult =
                    new Vl_biblioteca_titResult(vl_biblioteca_tit);


                vl_biblioteca_titResult.executeCustom("selectPerBid");
                TableDaoResult = vl_biblioteca_titResult.getElencoRisultati();

            }
        } else
            throw new EccezioneSbnDiagnostico(3013);
        return TableDaoResult;
    }

    // Funzione per la gestione del localizzazioni nelle liste e nei reticoli
    public List cercaLocalizzazioniTitolo(String IDtitolo, String polo, String biblioteca)
    throws IllegalArgumentException, InvocationTargetException, Exception {
        List TableDaoResult = new ArrayList();

        Vl_biblioteca_tit vl_biblioteca_tit = new Vl_biblioteca_tit();

        if ((polo == null) && (biblioteca == null))
            throw new EccezioneSbnDiagnostico(3018);
        else if ((biblioteca != null) || (!biblioteca.trim().equals(""))){
            //Selezione per polo + bibliteca
            vl_biblioteca_tit.setBID(IDtitolo);
            vl_biblioteca_tit.setCod_polo(polo);
            vl_biblioteca_tit.setCd_biblioteca(biblioteca);
            Vl_biblioteca_titResult vl_biblioteca_titResult = new Vl_biblioteca_titResult(vl_biblioteca_tit);
            vl_biblioteca_titResult.executeCustom("selectPerPolo");
            TableDaoResult = vl_biblioteca_titResult.getElencoRisultati();
        } else { // seleziono tutto
            vl_biblioteca_tit.setBID(IDtitolo);
            Vl_biblioteca_titResult vl_biblioteca_titResult = new Vl_biblioteca_titResult(vl_biblioteca_tit);
            vl_biblioteca_titResult.executeCustom("selectPerBid");
            TableDaoResult = vl_biblioteca_titResult.getElencoRisultati();
        }
    return TableDaoResult;
}
    /**
     * Method controlloUnicum.
     * @param tb_titolo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void controlloUnicum(Tb_titolo tb_titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        String natura = tb_titolo.getCD_NATURA();
        if (natura.equals("M") || natura.equals("S") || natura.equals("W")) {
            String tpRecord = null;
            tpRecord = tb_titolo.getTP_RECORD_UNI();
            boolean verificaLocalizzazione = false;
            if (tpRecord.equals("b") || tpRecord.equals("d") || tpRecord.equals("f")) {
                verificaLocalizzazione = true;
            }
            if (tb_titolo.getTP_MATERIALE().equals("G")) {
                //			leggo Tb_grafica
                Grafica grafica = new Grafica();
                Tb_grafica tb_grafica = null;
                tb_grafica = grafica.cercaPerId(tb_titolo.getBID());
                if (tb_grafica != null) {
                    String tp_materiale_gra = tb_grafica.getTP_MATERIALE_GRA();
                    if (tp_materiale_gra.equals("b") || tp_materiale_gra.equals("c")) {
                        verificaLocalizzazione = true;
                    }
                }
            }
            if (verificaLocalizzazione) {
                TitoloBiblioteca titoloBiblioteca = new TitoloBiblioteca();
                int numLocalizzazioni = titoloBiblioteca.contaPerBid(tb_titolo.getBID());
                if (numLocalizzazioni > 0)
                    throw new EccezioneSbnDiagnostico(3252, "esemplare unico già localizzato");
            }
        }
    }

    /**
     * Method estraiT899.
     * @param TableDao
     * input: vettore con i legami fra titoli e biblioteche
     */
    private List estraiT899(List vettoreLocalizzati) {
        //questo è da sistemare quando le viste sono complete
        List TableDaoResult = new ArrayList();
        for (int i = 0; i < vettoreLocalizzati.size(); i++) {
            //TableDaoResult.add(((Vl_tit_bib)vettoreLocalizzati.get(i)).getT899());
        }
        return TableDaoResult;
    }

    private boolean controlla_possesso(String bid, String fl_possesso) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_biblioteca_tit Vl_biblioteca_tit = new Vl_biblioteca_tit();
        Vl_biblioteca_tit.setBID(bid);
        Vl_biblioteca_titResult Vl_biblioteca_titResult = new Vl_biblioteca_titResult(Vl_biblioteca_tit);


        Vl_biblioteca_titResult.executeCustom("selectPerBid");
        List risultati = Vl_biblioteca_titResult.getElencoRisultati();

        if (risultati.size() != 0)
            if (((Vl_biblioteca_tit) risultati.get(0)).getFL_POSSESSO() == fl_possesso) {
                return true;
            }
        return false;
    }

    /**
     * verifica l'esistenza: (T001) in tb_titolo (chiama verificaEsistenzaID di
     * ValidaTitolo)
     * se non esiste segnala un diagnostico.
     * per ogni elemento T899:
     *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
     *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
     * caratteri di sbnuser.biblioteca)
     *   Inserisce un record in tr_tit_bib impostando fl_possesso e/o fl_gestione
     * secondo tipoLocalizza.
     * Prepara l'output con esito positivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void localizzaTitolo(
        String user,
        String id,
        List t899,
        String codiceBibliotecaUtente,
        int tipoLocalizza)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloValida titoloValida = new TitoloValida();
        List bibliotecaTableDao = new ArrayList();
        String codicePolo, codiceBiblioteca;
        Tb_titolo tb_titolo = titoloValida.verificaEsistenzaID(id);
        if (tb_titolo != null) {
            controlloUnicum(tb_titolo);
            for (int i = 0; i < t899.size(); i++) {
                if ((((C899) t899.get(i)).getC1_899() != null)
                    && (((C899) t899.get(i)).getC2_899() != null)) {
                    throw new EccezioneSbnDiagnostico(3018);
                } else {
                    //verifico esistenza di tb_biblioteca con c1_899 o c1899
                    Biblioteca biblioteca = new Biblioteca();
                    bibliotecaTableDao =
                        biblioteca.verificaEsistenza(
                            ((C899) t899.get(i)).getC1_899(),
                            ((C899) t899.get(i)).getC2_899());
                    for (int j = 0; j < bibliotecaTableDao.size(); j++) {
                        //verifico le abilitazioni dell'utente per gestire tb_biblioteca
                        codicePolo = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCod_polo();
                        codiceBiblioteca = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCd_biblioteca();
                        verificaAbilita(
                            user,
                            (C899) t899.get(i),
                            codicePolo,
							tipoLocalizza);
                        if (verificaEsistenzaLocalizzazione(codicePolo, codiceBiblioteca, id)) {
                            inserisciTr_tit_bib(
                                user,
                                id,
                                tb_titolo.getCD_NATURA(),
                                codicePolo,
                                codiceBiblioteca,
                                tipoLocalizza,
                                (C899) t899.get(i));
                        } else {
                            //Devo aggiornare i flag gestione e possesso
                            aggiornaFlagGesPos(user,id,codicePolo,codiceBiblioteca,tipoLocalizza,(C899) t899.get(i));
                        }
                    }
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013);
        }
    }

    private boolean verificaAbilita(
        String user,
        C899 c899,
        String codicePolo,
        int tipoLocalizza)
        throws EccezioneSbnDiagnostico {
    	ValidatorProfiler validator = ValidatorProfiler.getInstance();
        String cd_polo = null;
        if (validator.isPolo(user)) {
            cd_polo = user.substring(0,3);
        } else {
        	Tbf_bibliotecario utente = validator.getUtente(user);
            if (utente != null) {
//                if (utente.getDS_POLOBIB()!=null)
//                if ((cd_polo = PoloUtility.getCodPolo(utente))=null)
                    cd_polo = (new PoloUtility()).getCodPolo(utente);
            } else  {
                throw new EccezioneSbnDiagnostico(4000);
            }
        }
        if (SbnTipoLocalizza.GESTIONE.getType() == tipoLocalizza) {
            //localizza per gestione
            if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009))
                throw new EccezioneSbnDiagnostico(4000);
            else if (!codicePolo.equals(cd_polo) && cd_polo != null)
                throw new EccezioneSbnDiagnostico(4000);
        } else if (SbnTipoLocalizza.POSSESSO.getType() == tipoLocalizza) {
            if (validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1010))
                return true;
            if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008))
                throw new EccezioneSbnDiagnostico(4000);
            else if (!codicePolo.equals(cd_polo))
                throw new EccezioneSbnDiagnostico(4000);
        } else if (SbnTipoLocalizza.TUTTI.getType() == tipoLocalizza) {
            if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009))
                throw new EccezioneSbnDiagnostico(4000);
            else if (!codicePolo.equals(cd_polo) && cd_polo != null)
                throw new EccezioneSbnDiagnostico(4000);
            else if ((!codicePolo.equals(cd_polo)) && !validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1010))
                throw new EccezioneSbnDiagnostico(4000);

        }
        return true;
    }

    private boolean verificaAbilitaDelocalizza(
        String user,
        C899 c899,
        String codicePolo,
        int tipoLocalizza)
        throws EccezioneSbnDiagnostico {
    	ValidatorProfiler validator = ValidatorProfiler.getInstance();
        String cd_polo = null;
        if (validator.isPolo(user)) {
            cd_polo = user.substring(0,3);
        } else {
        	Tbf_bibliotecario utente = validator.getUtente(user);
            if (utente != null) {
//                if (utente.getCd_polo().getCd_polo().getCd_polo()!=null)
                	cd_polo = (new PoloUtility()).getCodPolo(utente);
            } else  {
                throw new EccezioneSbnDiagnostico(4000);
            }
        }
        if (SbnTipoLocalizza.GESTIONE.getType() == tipoLocalizza) {
            //delocalizza per gestione
        	/*
        	//almaviva5_20140523 evolutive google3
            if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_GESTIONE_1014))
                throw new EccezioneSbnDiagnostico(4000);
            else if ((!codicePolo.equals(cd_polo)))
                throw new EccezioneSbnDiagnostico(4000);
            */
        } else if (SbnTipoLocalizza.POSSESSO.getType() == tipoLocalizza) {
            if (validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1013))
                return true;
            if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_POSSEDUTO_1012))
                throw new EccezioneSbnDiagnostico(4000);
            else if (!codicePolo.equals(cd_polo))
                throw new EccezioneSbnDiagnostico(4000);
        } else if (SbnTipoLocalizza.TUTTI.getType() == tipoLocalizza) {
        	//almaviva5_20140523 evolutive google3
            //if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_GESTIONE_1014))
           	if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_POSSEDUTO_1012))
                throw new EccezioneSbnDiagnostico(4000);
            else if ((!codicePolo.equals(cd_polo)) && !validator.verificaAttivitaID(user, CodiciAttivita.getIstance().DELOCALIZZA_PER_POSSEDUTO_PER_ALTRI_POLI_1013))
                throw new EccezioneSbnDiagnostico(4000);

        }
        return true;
    }
    private boolean verificaAbilitaCorreggi(
        String user,
        C899 c899,
        String codicePolo,
        int tipoLocalizza)
        throws EccezioneSbnDiagnostico {
    	ValidatorProfiler validator = ValidatorProfiler.getInstance();
        String cd_polo = null;
        if (validator.isPolo(user)) {
            cd_polo = user.substring(0,3);
        } else {
        	Tbf_bibliotecario utente = validator.getUtente(user);
            if (utente != null) {
//                if (utente.getCd_polo().getCd_polo().getCd_polo()!=null)
            	cd_polo = (new PoloUtility()).getCodPolo(utente);
            } else  {
                throw new EccezioneSbnDiagnostico(4000);
            }
        }
        if (validator.verificaAttivitaID(user, CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_PER_ALTRI_POLI_1031))
            return true;
        //almaviva5_20140521 evolutive google3
        //if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().MODIFICA_DATI_DI_LOCALIZZAZIONE_PER_POSSEDUTO_1030))
        if (!validator.verificaAttivitaID(user, CodiciAttivita.getIstance().LOCALIZZA_PER_POSSEDUTO_1008))
            throw new EccezioneSbnDiagnostico(4000);
        else if (!codicePolo.equals(cd_polo))
            throw new EccezioneSbnDiagnostico(4000);
        return true;
    }
    private boolean verificaEsistenzaLocalizzazione(String codicePolo, String codiceBiblioteca, String bid)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
        SbnTipoLocalizza tipoLocalizza = SbnTipoLocalizza.TUTTI;
        tr_tit_bib = verificaEsistenzaTr_tit_bib(bid, codicePolo, codiceBiblioteca, tipoLocalizza.getType());
        if (tr_tit_bib != null)
            //throw new EccezioneSbnDiagnostico(3243, "Titolo già localizzato nella biblioteca");
            return false;
        return true;
    }
    /**
     * verifica l'esistenza: (T001) in tb_titolo (chiama verificaEsistenzaID di
     * ValidaTitolo)
     * se non esiste segnala un diagnostico.
     * per ogni elemento T899:
     *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
     *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
     * caratteri di sbnuser.biblioteca)
     *   verifica esistenza record tr_tit_bib
     *   aggiorna il record in tr_tit_bib secondo quanto indicato   in tipoLocalizza.
     * se fl_possesso <> S e fl_gestione <> S   aggiorna fl_canc = 'S'
     * Prepara l'output con esito positivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void delocalizzaTitolo(
        String user,
        String id,
        List t899,
        String codiceBibliotecaUtente,
        String codicePoloUtente,
        int tipoLocalizza)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List bibliotecaTableDao = new ArrayList();
        TitoloValida titoloValida = new TitoloValida();
        if (titoloValida.verificaEsistenzaID(id) != null) {
            for (int i = 0; i < t899.size(); i++) {
                if ((((C899) t899.get(i)).getC1_899() != null)
                    && (((C899) t899.get(i)).getC2_899() != null)) {
                    throw new EccezioneSbnDiagnostico(3018);
                } else {
                    //verifico esistenza di tb_biblioteca con c1_899 o c1899
                    Biblioteca biblioteca = new Biblioteca();
                    bibliotecaTableDao =
                        biblioteca.verificaEsistenza(
                            ((C899) t899.get(i)).getC1_899(),
                            ((C899) t899.get(i)).getC2_899());
                    for (int j = 0; j < bibliotecaTableDao.size(); j++) {
                        //verifico le abilitazioni dell'utente per gestire tb_biblioteca
                        String codiceBiblioteca = null;
                        String codicePolo = null;
                        codicePolo = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCod_polo();
                        codiceBiblioteca = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCd_biblioteca();
                        verificaAbilitaDelocalizza(
                            user,
                            (C899) t899.get(i),
                            codicePolo,
                            tipoLocalizza);
                        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
                        tr_tit_bib =
                            verificaEsistenzaTr_tit_bib(id, codicePolo, codiceBiblioteca, tipoLocalizza);
                        if (tr_tit_bib != null) {
                            cancellaTr_tit_bib(user, tr_tit_bib, tipoLocalizza);
                        }
                    }
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013);
        }

    }

    /**
     * Il metodo azzera il flag di allineamento su comunicazione dell'utente. viene
     * attivato con il factoring ComunicaAllineati
     *
     * verifica l'esistenza: (T001) in tb_titolo (chiama verificaEsistenzaID di
     * ValidaTitolo
     * se non esiste segnala un diagnostico.
     * per ogni elemento biblioteca in biblioV:
     *   se sono compilati solo i primi tre caratteri del record  (allineamento di
     * polo) deve esaminare tutte le tb_biblioteca  con cd_polo uguale, altrimenti
     * verifica esistenza di ogni record in tb_biblioteca
     *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
     * caratteri di sbnuser.biblioteca)
     *   verifica esistenza record tr_tit_bib
     *   aggiorna il record in tr_tit_bib con fl_allinea = ' '
     * Prepara l'output con esito positivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void allineatoTitolo(
        String user,
        String IDTitolo,
        List biblioV,
        String codicePolo,
        String codiceBiblioteca,
        int tipoLocalizza)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloValida titoloValida = new TitoloValida();
        List bibliotecaTableDao = new ArrayList();
        Tb_titolo titolo = titoloValida.verificaEsistenzaID(IDTitolo);
        if (titolo != null) {
            log.info("VERIFICATO TITOLO " + biblioV.size());
            for (int i = 0; i < biblioV.size(); i++) {
                //verifico esistenza di tb_biblioteca
                Biblioteca biblioteca = new Biblioteca();
                bibliotecaTableDao = biblioteca.verificaEsistenza(new String(), (String) biblioV.get(i));
                if (bibliotecaTableDao.size() != 0) {
                    //verifico le abilitazioni dell'utente per gestire tb_biblioteca
                    log.info("STO PER VERIFICARE abilitazioni");
                    codicePolo = ((String) biblioV.get(i)).substring(0, 3);
                    biblioteca.verificaAbilitazioni(codicePolo);
                    log.info("VERIFICATO abilitazioni polo " + codicePolo);
                    if ((((String) biblioV.get(i)).trim()).length() > 3) {
                        codiceBiblioteca = ((String) biblioV.get(i)).substring(3, 6);
                    } else {
                        codiceBiblioteca = null;
                    }
                    log.info("STO PER VERIFICARE abilitazioni biblioteca " + codiceBiblioteca);
                    Tr_tit_bib tr_tit_bib;
                    List v = verificaEsistenzaTr_tit_bibBiblioteca(IDTitolo, codicePolo, tipoLocalizza,codiceBiblioteca);
                    for (int j = 0;j<v.size();j++) {
                        tr_tit_bib = (Tr_tit_bib)v.get(j);
                        allineaTr_tit_bib(tr_tit_bib, user, titolo);
                    }
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3101);
        }
    }

    /**
     * verifica l'esistenza: (T001) in tb_titolo (chiama verificaEsistenzaID di
     * ValidaTitolo)
     * se non esiste segnala un diagnostico.
     * per ogni elemento T899:
     *   verifica esistenza di tb_biblioteca con c1_899 o c2_899
     *   verifica abilitazione utente a gestire tb_biblioteca (cd_polo =   primi tre
     * caratteri di sbnuser.biblioteca)
     *   verifica esistenza record tr_tit_bib
     *   aggiorna il record in tr_tit_bib secondo quanto contenuto in   T899 ad
     * esclusione di a_899, c1_899 e c2_899
     * Prepara l'output con esito positivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws InfrastructureException
     * @throws IllegalArgumentException
     */
    public void modificaLocalizzaTitolo(
        String user,
        String IDTitolo,
        List t899,
        String codicePoloUtente,
        String codiceBiblioteca,
        int tipoLocalizza)
        throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception {
        TitoloValida titoloValida = new TitoloValida();
        List bibliotecaTableDao = new ArrayList();
        String c1, c2;
        if (titoloValida.estraiTitoloPerID(IDTitolo) != null) {
            for (int i = 0; i < t899.size(); i++) {
                c1 = ((C899) t899.get(i)).getC1_899();
                c2 = ((C899) t899.get(i)).getC2_899();
                if ((c1 == null) && (c2 == null)) {
                    throw new EccezioneSbnDiagnostico(3052, "mancano i valori di c1_899 e c2_899");
                } else {
                    //verifico esistenza di tb_biblioteca con c1_899 o c1899
                    Biblioteca biblioteca = new Biblioteca();
                    bibliotecaTableDao = biblioteca.verificaEsistenza(c1, c2);
                    for (int j = 0; j < bibliotecaTableDao.size(); j++) {
                        //verifico le abilitazioni dell'utente per gestire tb_biblioteca
                        //							biblioteca.verificaAbilitazioni(codiceBiblioteca);
                        String codicePolo = null;
                        codicePolo = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCod_polo();
                        codiceBiblioteca = ((Tbf_biblioteca_in_polo) bibliotecaTableDao.get(j)).getCd_biblioteca();
                       // biblioteca.verificaAbilitazioni(codicePoloUtente, codicePolo);
                        verificaAbilitaCorreggi(
                                user,
                                (C899) t899.get(i),
                                codicePolo,
								tipoLocalizza);
                        Tr_tit_bib tr_tit_bib = new Tr_tit_bib();
                        tr_tit_bib =
                            verificaEsistenzaTr_tit_bib(
                                IDTitolo,
                                codicePolo,
                                codiceBiblioteca,
                                tipoLocalizza);
                        //controllo cd_natura
                        if (tr_tit_bib == null) {
                          throw new EccezioneSbnDiagnostico(3001, "elemento non trovato");
                        }
                        Titolo titolo = new Titolo();
                        Tb_titolo tb_titolo = new Tb_titolo();
                        tb_titolo = titolo.estraiTitoloPerID(tr_tit_bib.getBID());
                        if (tb_titolo == null)  {
                          throw new EccezioneSbnDiagnostico(3001, "elemento non trovato");
                        }
                     // Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
                     // EVOLUTIVE ottobre 2015 almaviva2 - a seguito di creazione/copia di uno spoglio su madre già collocata viene
                    // estesa alla N nuova la localizzazione per possesso della madre
                        if (tb_titolo.getCD_NATURA().equals("M")
                            || tb_titolo.getCD_NATURA().equals("W")
                            || tb_titolo.getCD_NATURA().equals("C")
                            || tb_titolo.getCD_NATURA().equals("S")
                            || tb_titolo.getCD_NATURA().equals("R")
                            || tb_titolo.getCD_NATURA().equals("N")) {
                            aggiornaT899(
                                user,
                                IDTitolo,
                                codicePolo,
                                codiceBiblioteca,
                                (C899) t899.get(i),
                                tr_tit_bib);
                        } else
                            throw new EccezioneSbnDiagnostico(3069, "Natura non valida");
                    }
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3101);
        }

    }
    /**
     * Localizza elementi legati ad un titolo dato.
     * Da un bid estraggo tutti i Tr_tit_tit_b ricorsivamente localizzo e conservo tutti i bid collegati
     * su tutti i bid localizzo le marche legate (tr_tit_mar)
     * su tutti i bid estraggo gli autori(Tr_tit_aut) localizzo ogni autore e ne estraggo
     * i vid collegati ricorsivamente(tr_aut_aut) localizzando ogni vid
     *
     * @param bid
     * @param localizza localizza type in cui aggiungere i vari valori
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void localizzaReticolo(String user, String bid, List t899, String codice_polo, int tipoLocalizza) throws IllegalArgumentException, InvocationTargetException, Exception {
        List titolilegati = trovaBidCollegati(bid);
        List marchelegate = new ArrayList();
        List autorilegati = new ArrayList();
        for (int i=0;i<titolilegati.size();i++) {
            String bidlegato = (String)titolilegati.get(i);
            trovaMidCollegati(bidlegato,marchelegate);
            trovaVidCollegati(bidlegato,autorilegati);
        }
        //localizzo i titoli
        //Parto da 1: non considero il bid da cui parte (viene già localizzato)
        int i = 1;
        for (;i<titolilegati.size();i++) {
            //localizzo con questi dati.
            localizzaTitolo(user,(String)titolilegati.get(i),t899,codice_polo,tipoLocalizza);
        }
        //espando i vid in maniera ricorsiva. e quindi li localizzo.
        trovatuttiVid(autorilegati);
        AutoreGestisceLocalizza autoreGestisceLocalizza= new AutoreGestisceLocalizza();
        for (i=0;i<autorilegati.size();i++) {
            //localizzo con questi dati.
            autoreGestisceLocalizza.localizzaAutore(user,(String)autorilegati.get(i),t899,codice_polo);

        }
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007

        //Localizzo le marche
//        MarcaGestisceLocalizza marcaGestisceLocalizza= new MarcaGestisceLocalizza();
//        for (i=0;i<marchelegate.size();i++) {
//            //localizzo con questi dati.
//            marcaGestisceLocalizza.localizzaMarca(user,(String)marchelegate.get(i),t899,codice_polo);
//        }
    }
    /**
     * Localizza per import
     * Localizza elementi legati ad un titolo dato.
     * Da un bid estraggo tutti i Tr_tit_tit_b ricorsivamente localizzo e conservo tutti i bid collegati
     * su tutti i bid localizzo le marche legate (tr_tit_mar)
     * su tutti i bid estraggo gli autori(Tr_tit_aut) localizzo ogni autore e ne estraggo
     * i vid collegati ricorsivamente(tr_aut_aut) localizzando ogni vid
     *
     * @param bid
     * @param localizza localizza type in cui aggiungere i vari valori
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public LocalizzaType localizzaPerImport(String bid, boolean considerabidpartenza, LocalizzaType localizzatype, LocalizzaInfoType info) throws IllegalArgumentException, InvocationTargetException, Exception {
        LocalizzaInfoType localizza;
        List titolilegati = trovaBidCollegati(bid);
        List marchelegate = new ArrayList();
        List autorilegati = new ArrayList();
        for (int i=0;i<titolilegati.size();i++) {
            String bidlegato = (String)titolilegati.get(i);
            trovaMidCollegati(bidlegato,marchelegate);
            trovaVidCollegati(bidlegato,autorilegati);
        }
        //localizzo i titoli
        int i = 1;
        if (considerabidpartenza)
            i=0;
        for (;i<titolilegati.size();i++) {
            localizza = new LocalizzaInfoType();
            SbnOggetto ogg = new SbnOggetto();
            ogg.setTipoMateriale(SbnMateriale.valueOf(" "));
            localizza.setTipoOggetto(ogg);
            localizza.setTipoInfo(info.getTipoInfo());
            localizza.setT899(info.getT899());
            localizza.setSbnIDLoc((String)titolilegati.get(i));
            localizza.setTipoOperazione(info.getTipoOperazione());
            localizzatype.addLocalizzaInfo(localizza);
        }
        //espando i vid in maniera ricorsiva. e quindi li localizzo.
        trovatuttiVid(autorilegati);
        for (i=0;i<autorilegati.size();i++) {
            localizza = new LocalizzaInfoType();
            SbnOggetto ogg = new SbnOggetto();
            ogg.setTipoAuthority(SbnAuthority.AU);
            localizza.setTipoOggetto(ogg);
            localizza.setTipoInfo(info.getTipoInfo());
            localizza.setT899(info.getT899());
            localizza.setSbnIDLoc((String)autorilegati.get(i));
            localizza.setTipoOperazione(info.getTipoOperazione());
            localizzatype.addLocalizzaInfo(localizza);
        }
        //Localizzo le marche
        for (i=0;i<marchelegate.size();i++) {
            localizza = new LocalizzaInfoType();
            SbnOggetto ogg = new SbnOggetto();
            ogg.setTipoAuthority(SbnAuthority.MA);
            localizza.setTipoOggetto(ogg);
            localizza.setTipoInfo(info.getTipoInfo());
            localizza.setT899(info.getT899());
            localizza.setSbnIDLoc((String)marchelegate.get(i));
            localizza.setTipoOperazione(info.getTipoOperazione());
            localizzatype.addLocalizzaInfo(localizza);
        }
        return localizzatype;
    }

    /**
     * Estrae tutti i vid legati a quelli presenti nel vettore elencovid.
     * @param elencovid
     * @return l'elencovid modificato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private List trovatuttiVid(List elencovid) throws IllegalArgumentException, InvocationTargetException, Exception {
        AutoreAutore autaut = new AutoreAutore();
       for (int el=0;el<elencovid.size(); el++) {
           String vidvisitante = (String)elencovid.get(el);
           List temp = autaut.cercaLegamiAutore(vidvisitante,null);
           for (int i = 0; i<temp.size(); i++)  {
               String ogg = ((Tr_aut_aut)temp.get(i)).getVID_BASE(); //Serve il bid_base ??
               if (!elencovid.contains(ogg)) {
                   elencovid.add(ogg);
               }
           }
       }
       return elencovid;
    }

    private void trovaVidCollegati(String bid, List elencoVid) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloAutore titA = new TitoloAutore();
        List temp = titA.estraiTitoliAutore(bid);
        for (int i=0; i< temp.size(); i++) {
            Tr_tit_aut ta = (Tr_tit_aut)temp.get(i);
            if (!elencoVid.contains(ta.getVID())) {
                elencoVid.add(ta.getVID());
                List autori = new AutoreAutore().cercaLegamiAutore(ta.getVID(), "8");
                for (int j = 0; j < autori.size(); j++) {
                  Tr_aut_aut element = (Tr_aut_aut) autori.get(j);
                  if (!elencoVid.contains(element.getVID_COLL())) {
                    elencoVid.add(element.getVID_COLL());
                  }
                }
            }
        }
    }

    private void trovaMidCollegati(String bid, List elencoMid) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloMarca titMarca = new TitoloMarca();
        List temp = titMarca.estraiTitoliMarca(bid);
        for (int i=0; i< temp.size(); i++) {
            Tr_tit_mar tm = (Tr_tit_mar)temp.get(i);
            if (!elencoMid.contains(tm.getMID()))
                elencoMid.add(tm.getMID());
        }
    }

    /**
     * Trova tutti i bid legati ad un bid.
     * @param bid
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private List trovaBidCollegati (String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloTitB titolo_tit = new TitoloTitB();
        //Vettore contenente i bid legati a quello originale.
        List titolilegati = new ArrayList();
        titolilegati.add(bid);
        for (int el = 0; el<titolilegati.size();el++) {
            String bidvisitante = (String)titolilegati.get(el);
            List temp = titolo_tit.cercaTuttiLegami(bidvisitante);
            for (int i = 0; i<temp.size(); i++)  {
                String ogg = ((Vl_titolo_tit_b)temp.get(i)).getBID(); //Serve il bid_base ??
                if (!titolilegati.contains(ogg)) {
                    titolilegati.add(ogg);
                }
            }
        }
        return titolilegati;
    }

    /**
     * Gestisce la localizzazione del titolo che viene dopo la creazione dello stesso.
     * @param localizzaType
     * @param sbnUser
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void localizzaTitolo(String id, LocalizzaType localizzaType, SbnUserType sbnUser)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (localizzaType != null) {
            LocalizzaInfoType[] TableDaoLocalizza = null;
            int localizzaCount = 0;
            localizzaCount = localizzaType.getLocalizzaInfoCount();
            TableDaoLocalizza = localizzaType.getLocalizzaInfo();
            for (int i = 0; i < localizzaCount; i++) {
                String sbnIdLoc = null;
                SbnTipoLocalizza tipoLocalizza = null;
                SbnAuthority tipoAuthority = null;
                SbnMateriale tipomateriale = null;
                SbnAzioneLocalizza tipoOperazione = null;
                C899[] t899 = null;
                sbnIdLoc = TableDaoLocalizza[i].getSbnIDLoc();
                if (sbnIdLoc == null || sbnIdLoc.equals(Factoring.SBNMARC_DEFAULT_ID))
                    sbnIdLoc = id;
                t899 = TableDaoLocalizza[i].getT899();
                tipoOperazione = TableDaoLocalizza[i].getTipoOperazione();
                tipoLocalizza = TableDaoLocalizza[i].getTipoInfo();
                tipomateriale = TableDaoLocalizza[i].getTipoOggetto().getTipoMateriale();
                tipoAuthority = TableDaoLocalizza[i].getTipoOggetto().getTipoAuthority();
                List t899_TableDao = new ArrayList();
                if (t899 != null)
                    for (int j = 0; j < t899.length; j++) {
                        t899_TableDao.add(t899[j]);
                    }
                if (tipomateriale != null || tipoAuthority != null) {
                    if (tipoOperazione.getType() == SbnAzioneLocalizza.LOCALIZZA_TYPE) {
                            LocalizzaFactoring localizzaFactoring =
                                new LocalizzaFactoring(
                                    localizzaType,
                                    sbnUser,
                                    sbnUser.getBiblioteca());
                            localizzaFactoring.localizza(
                                tipoAuthority,
                                tipomateriale,
                                sbnIdLoc,
                                t899_TableDao,
                                tipoLocalizza,
                                sbnUser.getBiblioteca());
                    } else
                        throw new EccezioneSbnDiagnostico(3035, "operazione non valida");
                }
            }
        }
    }
}
