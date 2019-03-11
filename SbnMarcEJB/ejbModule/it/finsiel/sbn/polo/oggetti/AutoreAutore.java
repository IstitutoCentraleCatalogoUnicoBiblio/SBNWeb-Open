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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_autResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.LegamiAutore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_aut_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe per la gestione delle operazioni complesse sulla tavola Tr_aut_aut.
 *
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 27-gen-2003
 */
public class AutoreAutore extends Tr_aut_aut {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3980715485420306104L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.AutoreAutoreDB");

    public AutoreAutore() {

    }

    /**
     * Method validaPerCreaLegame.
     * @param elementoAut
     * @return boolean
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean validaPerCreaLegame(String utente, Tb_autore partenza, LegamiType legame,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tr_aut_aut tr_aut_aut = new Tr_aut_aut();
        Tb_autore tb_aut2;

        LegameElementoAutType legamedoc = legame.getArrivoLegame(0).getLegameElementoAut();

        AutoreValida autore = new AutoreValida();
        TableDao tavola = autore.cercaAutorePerID(legamedoc.getIdArrivo());
        List v = tavola.getElencoRisultati();

        if (v.size() != 1)
            throw new EccezioneSbnDiagnostico(3013, "Autore non esistente nel DB");
        tb_aut2 = (Tb_autore) v.get(0);
        autore.verificaAllineamentoModificaAutore(tb_aut2.getVID());
        tr_aut_aut.setVID_BASE(partenza.getVID());
        tr_aut_aut.setVID_COLL(legamedoc.getIdArrivo());
        if (legamedoc.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
            if (!(partenza.getTP_FORMA_AUT().equals("A") && tb_aut2.getTP_FORMA_AUT().equals("A")))
                throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legame incompatibile");
            if (Integer.parseInt(partenza.getCD_LIVELLO()) < Integer.parseInt(tb_aut2.getCD_LIVELLO()))
                autore.controllaParametriUtente(
                    utente,
                    tb_aut2.getCD_LIVELLO(),
                    tb_aut2.getCD_LIVELLO(),
                    false,_cattura);
            Tr_aut_aut autAut = cercaLegameAutori(partenza.getVID(), legamedoc.getIdArrivo());
            if (autAut != null)
                //Esiste già un legame
                throw new EccezioneSbnDiagnostico(3030, "Legame già esistente");
        } else if (legamedoc.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
            if (!(partenza.getTP_FORMA_AUT().equals("R") && tb_aut2.getTP_FORMA_AUT().equals("A")))
                throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legame incompatibile");
            List vec = autore.cercaRinviiAutore(partenza.getVID());
            if (vec.size() > 0)
                //Esiste già un legame di rinvio
                throw new EccezioneSbnDiagnostico(3030, "Legame di rinvio già esistente");
            //return false;
        }

        return true;
    }

    /**
    * per ogni struttura ArrivoLegame corrispondente al luogo in creazione verifica
    * la validità del legame:
    * . l'autore di arrivo deve esistere: cercaAutorePerID con idArrivo
    * . se tipoLegame = 5XX tp_forma dell'autore letto deve essere 'A' (accettata) e
    * tipoForma dell'autore in modifica deve essere 'A', altrimenti segnala
    * diagnostico: 'Forma dei nomi e legame incompatibili'
    * . se tipoLegame = 4XX:
    *   .. se tp_forma dell'autore letto è 'R' e tipoForma dell'autore in modifica è
    * 'A', l'autore letto non deve essere legato ad altri luoghi (cioè non deve
    * essere presente in nessun record di Tr_aut_aut )
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean validaPerModificaLegame(
        Tb_autore aut1,
        String utente,
        ArrivoLegame legame,
        SbnTipoOperazione operazione,
        TimestampHash timeHash,
        boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tb_autore aut2;
        List elenco;
        //prima di tutto valido tutti gli autori di arrivo
        int sizeArrivoLegame;
        LegameElementoAutType elementoLegato = legame.getLegameElementoAut();
        AutoreValida autore = new AutoreValida();

        //Verifico la correttezza del tipo di authority
        if ((elementoLegato == null)
            || (elementoLegato.getTipoAuthority() != null
                && elementoLegato.getTipoAuthority().getType() != SbnAuthority.AU_TYPE)) {
            throw new EccezioneSbnDiagnostico(3083, "Tipo di autority sbagliato");
        }
        //Verifico esistenza autore
        aut2 = autore.estraiAutorePerID(elementoLegato.getIdArrivo());
        if (aut2 == null) {
            throw new EccezioneSbnDiagnostico(3013, "Autore non esistente nel DB");
        }
        autore.verificaAllineamentoModificaAutore(aut2.getVID());
        //memorizzo il timestamp
        timeHash.putTimestamp("tb_autore", aut2.getVID(), new SbnDatavar( aut2.getTS_VAR()));

        //Controlli specifici a seconda del tipo di operazione
        if (operazione.getType() == SbnTipoOperazione.CREA.getType()) {
            //CONTROLLI DI CREAZIONE

            //Verifico correttezza dei legami (da controllare)
            if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                if (!(aut2.getTP_FORMA_AUT().equals("A") && (aut1.getTP_FORMA_AUT().equals("A"))))
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                //Verifico i livelli di entrambi
                if (Integer.parseInt(aut1.getCD_LIVELLO()) < Integer.parseInt(aut2.getCD_LIVELLO()))
                    autore.controllaParametriUtente(
                        utente,
                        aut2.getCD_LIVELLO(),
                        aut2.getCD_LIVELLO(),
                        false,_cattura);
                if (cercaLegameAutori(aut1.getVID(), aut2.getVID()) != null)
                    throw new EccezioneSbnDiagnostico(3030, "Legame tra autori già esistente");
            } else if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                if ((aut2.getTP_FORMA_AUT().equals("A") && (aut1.getTP_FORMA_AUT().equals("A")))) {
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                } else if ((aut2.getTP_FORMA_AUT().equals("A") && (aut1.getTP_FORMA_AUT().equals("R")))) {
                    elenco = autore.cercaRinviiAutore(aut1.getVID());
                    if (elenco.size() > 0) {
                        // Vedi commenti del metodo
                        throw new EccezioneSbnDiagnostico(3029, "Legame dell'autore R già esistente");
                    }
                } else { //aut1 tipoForma è A
                    Tr_aut_aut autAut =
                        cercaLegameAutori(aut1.getVID(), legame.getLegameElementoAut().getIdArrivo());
                    if (autAut != null) {
                        // Vedi commenti del metodo
                        throw new EccezioneSbnDiagnostico(3030, "Legame tra autori già esistente");
                    }
                }
            } else //nè 4XX nè 5XX
                throw new EccezioneSbnDiagnostico(3031, "Tipo di legame non valido");

        } else if (operazione.getType() == SbnTipoOperazione.CANCELLA_TYPE) {
            //CONTROLLI DI CANCELLAZIONE
            Tr_aut_aut autAut = cercaLegameAutori(elementoLegato.getIdArrivo(), aut1.getVID());
            if (autAut == null) {
                throw new EccezioneSbnDiagnostico(3029, "Legame tra autori non esistente");
            }
            timeHash.putTimestamp(
                "Tr_aut_aut",
                aut1.getVID() + elementoLegato.getIdArrivo(),
                new SbnDatavar( autAut.getTS_VAR()));

            //Verifico correttezza dei legami (da controllare)
            if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                if (!(aut2.getTP_FORMA_AUT().equals("A") && (aut1.getTP_FORMA_AUT().equals("A"))))
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                //Verifico i livelli di entrambi
                if (Integer.parseInt(aut1.getCD_LIVELLO()) < Integer.parseInt(aut2.getCD_LIVELLO()))
                    autore.controllaParametriUtente(
                        utente,
                        aut2.getCD_LIVELLO(),
                        aut2.getCD_LIVELLO(),
                        false, _cattura);
            } else if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                if ((aut2.getTP_FORMA_AUT().equals("R") && (aut1.getTP_FORMA_AUT().equals("A")))
                    || (aut2.getTP_FORMA_AUT().equals("A") && (aut1.getTP_FORMA_AUT().equals("R")))) {
                } else {
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                }
            } else //nè 4XX nè 5XX
                throw new EccezioneSbnDiagnostico(3031, "Tipo di legame non valido");

        } else if (operazione.getType() == SbnTipoOperazione.SCAMBIOFORMA_TYPE) {
            //CONTROLLI DI SCAMBIOFORMA
            if (legame.getLegameElementoAut().getTipoLegame().getType() != SbnLegameAut.valueOf("4XX").getType()) {
                throw new EccezioneSbnDiagnostico(3036, "Legame di tipo sbagliato");
            }
            validaPerScambioForma(aut1, legame.getLegameElementoAut().getIdArrivo(), timeHash);

        } else if (operazione.getType() == SbnTipoOperazione.MODIFICA_TYPE) {
            //posso modificare solo la nota di un legame.
            String nota = elementoLegato.getNoteLegame();

            // Modifica almaviva2 - MANTIS BUG 3587
            // la variazione/update di questo tipo di legame si deve effettuare anche in caso di cattura
            if (nota != null || _cattura) {
                Tr_aut_aut autAut =
                    cercaLegameAutori(legame.getLegameElementoAut().getIdArrivo(), aut1.getVID());
                if (autAut == null) {
                    throw new EccezioneSbnDiagnostico(3029, "Legame tra autori non esistente");
                }
                if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                    //Verifico i livelli di entrambi
                    if (Integer.parseInt(aut1.getCD_LIVELLO()) < Integer.parseInt(aut2.getCD_LIVELLO()))
                        autore.controllaParametriUtente(
                            utente,
                            aut2.getCD_LIVELLO(),
                            aut2.getCD_LIVELLO(),
                            false, _cattura);
                }
                timeHash.putTimestamp(
                    "Tr_aut_aut",
                    aut1.getVID() + elementoLegato.getIdArrivo(),
                    new SbnDatavar( autAut.getTS_VAR()));
            }
        }

        return true;
    }

    /** Cerca ogni tipo di legame tra i due autori
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public Tr_aut_aut cercaLegameAutori(String partenza, String arrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut tr = new Tr_aut_aut();
        Tr_aut_autResult tabella = new Tr_aut_autResult(tr);
        //tr.setVid_base(partenza);
        //tr.setVid_coll(arrivo);
        //tabella.selectPerKey();
        //Faccio in modo che possano essere scambiati vid_base e vid_coll
        tr.settaParametro(TableDao.XXXvid_1, partenza);
        tr.settaParametro(TableDao.XXXvid_2, arrivo);
        tabella.executeCustom("selectPerKeys2");
        List v = tabella.getElencoRisultati();
        if (v.size() > 0) {
            return (Tr_aut_aut) v.get(0);
        }
        return null;
    }

    /** Cerca i legami in cui sia coinvolto un autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaLegamiAutore(String partenza,String tp_legame) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID_BASE(partenza);
        setTP_LEGAME(tp_legame);
        Tr_aut_autResult tabella = new Tr_aut_autResult(this);
        tabella.executeCustom("selectPerAutoreBaseOColl");
        List v = tabella.getElencoRisultati();
        return v;
    }

    public void updateCancellaLegame(String idAutore, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        List vec = cercaLegamiAutore(idAutore,null);
        Tr_aut_aut tr_aut_aut;
        for (int i = 0; i < vec.size(); i++) {
            tr_aut_aut = new Tr_aut_aut();
            tr_aut_aut = (Tr_aut_aut) vec.get(i);
            tr_aut_aut.setUTE_VAR(user);
            cancellaAut_Aut(tr_aut_aut);
            if (tr_aut_aut.getTP_LEGAME().equals("8")) {
                TableDao TableDao = null;
                String idAutoreLegato = null;
                if (tr_aut_aut.getVID_BASE().equals(idAutore))
                    idAutoreLegato = tr_aut_aut.getVID_COLL();
                else
                    idAutoreLegato = tr_aut_aut.getVID_BASE();
                //cancello anche l'autore legato
                Autore autore = new Autore();
                Tb_autore tb_autore = new Tb_autore();
                TableDao = autore.cercaAutorePerID(idAutoreLegato);
                tb_autore = (Tb_autore) TableDao.getElencoRisultati().get(0);

                tb_autore.setUTE_VAR(user);
                Tb_autoreResult tavola = new Tb_autoreResult(autore);
                tavola.executeCustomUpdate("updateCancellaAutore");

            }
        }
    }

    private void cancellaAut_Aut(Tr_aut_aut tr_aut_aut) throws IllegalArgumentException, InvocationTargetException, Exception {
        tr_aut_aut.setFL_CANC("S");
        Tr_aut_autResult tabella = new Tr_aut_autResult(tr_aut_aut);
        tabella.executeCustom("updateCancell");
    }

    /**
     * Esegue l'update di un legame, modificando qualcosa
     * @throws InfrastructureException
     */
    public void updateLegame(Tr_aut_aut legame) throws EccezioneDB, InfrastructureException {
        Tr_aut_autResult tabella = new Tr_aut_autResult(legame);
        tabella.selectPerKey(legame.leggiAllParametro());

        tabella.update(legame);
    }

    /**
     * Esegue la rimozione di un legame settando fl_canc a S.
     * Le due chiavi sono interscambiabili: esegue una modifica di entrambi.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void rimuoviLegame(
        String ute_var,
        String partenza,
        String arrivo,
        String tipo_legame,
        TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut legame = new Tr_aut_aut();
        legame.settaParametro(TableDao.XXXvid_1, partenza);
        legame.settaParametro(TableDao.XXXvid_2, arrivo);
        legame.setUTE_VAR(ute_var);
        if (timeHash!=null)
            legame.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("tr_aut_aut", partenza + arrivo)));
        	//legame.setTS_VAR(new Date(Long.parseLong(timeHash.getTimestamp("tr_aut_aut", partenza + arrivo))));
        legame.setTP_LEGAME(tipo_legame);
        Tr_aut_autResult tabella = new Tr_aut_autResult(legame);
        tabella.executeCustomUpdate("updateDisabilitaDoppio");
    }

    /**
     * Verifica che sia possibile eseguire uno scambio forma tra due autori.
     * Controlla che i due autori esistano, e abbiano un legame adatto.
     * input:    vid1= id dell'autore di cui scambiare la forma da A a R
     *           vid2= id dell'autore da R ad A
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected boolean validaPerScambioForma(Tb_autore aut1, String vid2, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //controllo che il legame che voglio modificare sia presente nel db!
        Tr_aut_aut tr_aut_aut = new Tr_aut_aut();
        List resultTableDao;
        tr_aut_aut.settaParametro(TableDao.XXXvid_1, aut1.getVID());
        tr_aut_aut.settaParametro(TableDao.XXXvid_2, vid2);
        tr_aut_aut.setTP_LEGAME("8");
        Tr_aut_autResult tr_aut_autResult = new Tr_aut_autResult(tr_aut_aut);
        tr_aut_autResult.executeCustom("selectPerKeys2");
        resultTableDao = tr_aut_autResult.getElencoRisultati();
        if (resultTableDao.size() != 1)
            throw new EccezioneSbnDiagnostico(3034);

        tr_aut_aut = (Tr_aut_aut) resultTableDao.get(0);
        if (tr_aut_aut.getTP_LEGAME().equals("4"))
            throw new EccezioneSbnDiagnostico(3034, "Legame di tipo sbagliato");

        timeHash.putTimestamp("Tr_aut_aut", aut1.getVID() + vid2, new SbnDatavar( tr_aut_aut.getTS_VAR()));
        //Leggo da DB i due autori coinvolti
        AutoreValida autore = new AutoreValida();
        //resultTableDao = autore.cercaDueAutoriPerID(vid1, vid2);
        TableDao tavola = autore.cercaAutorePerID(vid2);
        resultTableDao = tavola.getElencoRisultati();

        if (resultTableDao.size() != 1)
            throw new EccezioneSbnDiagnostico(3013, "Autore non presente");
        autore.verificaAllineamentoModificaAutore(vid2);
        Tb_autore tb_autore2 = (Tb_autore) resultTableDao.get(0);
        //Il legame deve essere di tipo giusto
        if (!aut1.getTP_FORMA_AUT().equals("A"))
            throw new EccezioneSbnDiagnostico(3094, "Partire dalla forma accettata");
        if (!(tb_autore2.getTP_FORMA_AUT().equals("R"))) {
            throw new EccezioneSbnDiagnostico(3034, "Legame di tipo sbagliato");
        }
        timeHash.putTimestamp("Tb_autore", aut1.getVID(), new SbnDatavar( aut1.getTS_VAR()));
        timeHash.putTimestamp("Tb_autore", tb_autore2.getVID(), new SbnDatavar( tb_autore2.getTS_VAR()));
        return true;
    }

    /**
     * input:    vid1= id dell'autore di cui scambiare la forma da A a R
     *           vid2= id dell'autore legato
     * Non si eseguono controlli !!!!!!!!!!!!!!
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void scambioForma(Tb_autore aut1, String vid2, String ute_var, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //'SCAMBIO FORMA' occorre verificare che non ci
        //siano tr_aut_bib con fl_allinea diverso da spazio
        AutoreBiblioteca autBib = new AutoreBiblioteca();
        if (autBib.cercaFlagAllineaDiverso(aut1.getVID(), " ").size()>0) {
            throw new EccezioneSbnDiagnostico(3312);
        }
        if (autBib.cercaFlagAllineaDiverso(vid2, " ").size()>0) {
            throw new EccezioneSbnDiagnostico(3312);
        }
        //aggiorna ute_var e timestamp_var nella relazione
        Tr_aut_aut tr_aut_aut = new Tr_aut_aut();
        List resultTableDao;
        tr_aut_aut.settaParametro(TableDao.XXXvid_1, aut1.getVID());
        tr_aut_aut.settaParametro(TableDao.XXXvid_2, vid2);
        tr_aut_aut.setUTE_VAR(ute_var);
        tr_aut_aut.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_aut_aut", aut1.getVID() + vid2)));
        Tr_aut_autResult tr_aut_autResult = new Tr_aut_autResult(tr_aut_aut);
        tr_aut_autResult.executeCustomUpdate("updateModifica");

        //DEVO ESEGUIRE LO SCAMBIO EFFETTIVO: modificare i due autori.
        //rifaccio la select (se no diventa un casino l'update).
        Autore autore = new Autore();
        //resultTableDao = autore.cercaDueAutoriPerID(vid1, vid2);
        TableDao tavola = autore.cercaAutorePerID(vid2);
        resultTableDao = tavola.getElencoRisultati();

        if (resultTableDao.size() != 1)
            throw new EccezioneSbnDiagnostico(3013, "Autore non presente");

        Tb_autore tb_autore2 = (Tb_autore) resultTableDao.get(0);
        //String tipo_aut = aut1.getTp_forma_aut();
        String vid = aut1.getVID();
        //aut1.setTp_forma_aut(tb_autore2.getTp_forma_aut());
        aut1.setVID(tb_autore2.getVID());
        aut1.setVID_LINK(vid);
        aut1.setUTE_VAR(ute_var);
        aut1.setTS_VAR(tb_autore2.getTS_VAR());
        //tb_autore2.setTp_forma_aut(tipo_aut);
        tb_autore2.setVID(vid);
        tb_autore2.setVID_LINK(aut1.getVID());
        tb_autore2.setUTE_VAR(ute_var);
        tb_autore2.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_Autore", vid)));
        autore.eseguiUpdateScambioForma(aut1, tb_autore2);
        AllineamentoAutore allineaAut=new AllineamentoAutore(aut1);
        allineaAut.setTb_autore_scambio(true);
        new AutoreAllineamento().aggiornaFlagAllineamento(allineaAut, ute_var);
        ///Devo risettare per non incasinare l'allineamento del primo autore
        aut1.setVID(vid);
    }

    /**
     * Inserisce un autore nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean inserisciAutoreAutore(Tr_aut_aut autaut) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_autResult t_autaut = new Tr_aut_autResult(autaut);
        t_autaut.executeCustom("selectEsistenza");
        int size = t_autaut.getElencoRisultati().size();
        if (size>0) {
            t_autaut.executeCustom("updateInsert");
        } else {
            t_autaut.insert(autaut);
        }
        if (autaut.getTP_LEGAME().equals("4")) {
            LegamiAutore aut = new LegamiAutore(autaut.getUTE_INS(), autaut.getUTE_VAR(), "4");
            aut.elabora(autaut.getVID_BASE(), autaut.getVID_COLL());
        }
        return true;
    }

    /**
     * Method updateModifica.
     * @param idPartenza
     * @param idArrivo
     * @param _userID
     * @param nota
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateModifica(
        String idPartenza,
        String idArrivo,
        String timestamp,
        String ute_var,
        String nota)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut legame = new Tr_aut_aut();
        legame.settaParametro(TableDao.XXXvid_1, idPartenza);
        legame.settaParametro(TableDao.XXXvid_2, idArrivo);
        legame.setUTE_VAR(ute_var);
        legame.setNOTA_AUT_AUT(nota);
        if(timestamp == null){
            SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
            legame.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        }
        else
            legame.setTS_VAR(ConverterDate.SbnDataVarToDate(timestamp));
        Tr_aut_autResult tabella = new Tr_aut_autResult(legame);
        // Qui non serve perche non effetua nessun controllo e non preleva nessun dato
        // verificare se non crea nessun problema su altre chiamate
        tabella.selectPerKeys2(legame.leggiAllParametro());
        tabella.executeCustomUpdate("updateModifica");
    }

    /**
     * in questo metodo si cercano tutti i legami di tipo tr_aut_aut e per ognuno di
     * questi viene fatto l'update
     * Method spostaLegami.
     * @param idPartenza
    	 * @param idArrivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void spostaLegami(String idPartenza, String idArrivo, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut legame = new Tr_aut_aut();
        legame.setVID_BASE(idPartenza);
        Tr_aut_autResult tabella = new Tr_aut_autResult(legame);
        tabella.executeCustom("selectLegami");
        List legamiDiArrivo = new Autore().cercaRinviiAutore(idArrivo);
        List vettoreDiLegami = tabella.getElencoRisultati();
        for (int i = 0; i < vettoreDiLegami.size(); i++) {
            legame = (Tr_aut_aut) vettoreDiLegami.get(i);
            Tr_aut_aut legameEsistente = verificaEsistenza(idArrivo,legame.getVID_COLL());
            if (legameEsistente == null) {
                legameEsistente = verificaEsistenzaNome(legame.getVID_COLL(),legamiDiArrivo);
            }
            if (legameEsistente == null) {
                legame.settaParametro(TableDao.XXXvid_base_new,idArrivo);
                legame.setUTE_VAR(user);
                tabella = new Tr_aut_autResult(legame);
                tabella.executeCustom("spostaLegamiBase");
            }
        }
        legame = new Tr_aut_aut();
        legame.setVID_COLL(idPartenza);
        tabella = new Tr_aut_autResult(legame);
        tabella.executeCustom("selectLegami");
        vettoreDiLegami = tabella.getElencoRisultati();
        for (int i = 0; i < vettoreDiLegami.size(); i++) {
            legame = (Tr_aut_aut) vettoreDiLegami.get(i);
            Tr_aut_aut legameEsistente = verificaEsistenza(legame.getVID_BASE(),idArrivo);
            if (legameEsistente == null) {
                legame.settaParametro(TableDao.XXXvid_coll_new,idArrivo);
                legame.setUTE_VAR(user);
                tabella = new Tr_aut_autResult(legame);
                tabella.executeCustom("spostaLegamiColl");

            }
        }

    }

    public Tr_aut_aut verificaEsistenza(String vid_base,String vid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_aut tr = new Tr_aut_aut();
        Tr_aut_autResult tabella = new Tr_aut_autResult(tr);
        tr.setVID_BASE(vid_base);
        tr.setVID_COLL(vid_coll);
        tabella.executeCustom("selectEsistenza");
        List v = tabella.getElencoRisultati();
        if (v.size() > 0) {
            return (Tr_aut_aut) v.get(0);
        }
        return null;
    }

    /** Restituisce null o un oggetto vuoto
     * @throws InfrastructureException */
    public Tr_aut_aut verificaEsistenzaNome(String vid_coll,List legami) throws EccezioneDB, InfrastructureException {
        Tb_autore aut = new Autore().estraiAutorePerID(vid_coll);
        if (aut != null) {
            String nome = aut.getDS_NOME_AUT();
            for (int i=0;i<legami.size();i++) {
                Tb_autore a = (Tb_autore)legami.get(i);
                if (nome.equals(a.getDS_NOME_AUT())) {
                    Tr_aut_aut aa = new Tr_aut_aut();
                    return aa;
                }
            }

        }
        return null;
    }

    public void cancellaLegami(String idCancellazione, String user, String tp_forma_aut) throws IllegalArgumentException, InvocationTargetException, Exception {
        //Se ci sono degli autori collegati:
        //Se l'autore è R cancella i legami e aggiorna il flag del A
        //Se l'autore è A cancella i legami agli altri A, i legami e gli autori R.
        List v = cercaLegamiAutore(idCancellazione,null);
        Autore autore = new Autore();
        AutoreAllineamento allineamento =new AutoreAllineamento();
        for (int i = 0; i < v.size(); i++) {
            Tr_aut_aut leg = (Tr_aut_aut) v.get(i);
            //Prendo il vid dell'autore legato
            String idArrivo;
            if (leg.getVID_BASE().equals(idCancellazione))
                idArrivo = leg.getVID_COLL();
            else
                idArrivo = leg.getVID_BASE();
            //Il legame va comunque rimosso
            rimuoviLegame(user, idCancellazione, idArrivo, leg.getTP_LEGAME(), null);
            if (leg.getTP_LEGAME().equals("8")) {
                AllineamentoAutore all = new AllineamentoAutore(autore.estraiAutorePerID(idArrivo));
                if (tp_forma_aut.equals("R")) {
                    all.setTr_aut_aut(true);
                    autore.updateVariazione(idArrivo, user);
                    allineamento.aggiornaFlagAllineamento(all, user);
                } else {
                    // MODIFICA(per gestire gli autori R legati per errore a più forme A):
                    // se l'autore ha un altro legame ad un A non lo cancello.
                    if (cercaLegamiAutore(idArrivo, "8").size() == 0) {
                        autore.rimuoviAutoreRinviato(idArrivo, user);
                        all.setTb_autore(true);
                        allineamento.aggiornaFlagAllineamento(all, user);
                        new AutoreBiblioteca().aggiornaFlagCancCancellazione(idArrivo,user);
                    } else {
                        all.setTr_aut_aut(true);
                        allineamento.aggiornaFlagAllineamento(all, user);
                    }
                }
                //Aggiorno i flag di allineamento
            }
        }

    }
}
