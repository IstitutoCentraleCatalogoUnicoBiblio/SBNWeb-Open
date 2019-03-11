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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_autoreResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.EccezioneSbnMessage;
import it.finsiel.sbn.polo.factoring.base.TipiAutore;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.factoring.util.ElencoVociAutori;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreBiblioteca;
import it.finsiel.sbn.polo.oggetti.AutoreMarca;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tr_aut_bib;
import it.finsiel.sbn.polo.orm.Ts_stop_list;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.ejb.model.unimarcmodel.A010;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Valida le informazioni relative a un Autore.
 * Input= DatiElementoAut con tipoAuthority = 'Autore'
 * Operazioni di validazione:
 * controllo esistenza con identificativo: T001 = VID
 * controllo versione: T005 = ts_var
 * controllo abilitazione su livello utente/cd_livello
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 27-gen-2003
 */
public class AutoreValida extends Autore {

	private static final long serialVersionUID = 397243171907406011L;
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();
	static String SIGLA_INDICE = validator.getCodicePolo();
    //static String SIGLA_INDICE = ResourceLoader.getPropertyString("SIGLA_INDICE");
    private DatiElementoType _datiElementoAut;
    private String isadn = null;
    private SbnSimile _tipoControllo;
    private String diagnostico;
    private List elencoDiagnostico;

    public AutoreValida(CreaType datiType) {
        super();
        _tipoControllo = datiType.getTipoControllo();
        _datiElementoAut = datiType.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
        estraiIsadn();
    }

    /**
     * Method estraiIsadn.
     */
    private void estraiIsadn() {
    	// Marzo 2016: gestione ISNI (International standard number identifier)
//        A015 t015 = null;
//        if (_datiElementoAut instanceof AutorePersonaleType) {
//            t015 = ((AutorePersonaleType) _datiElementoAut).getT015();
//        } else if (_datiElementoAut instanceof EnteType) {
//            t015 = ((EnteType) _datiElementoAut).getT015();
//        }
//        if (t015 != null)
//            isadn = t015.getA_015().toString();

    	 A010 t010 = null;
         A015 t015 = null;

 		if (_datiElementoAut instanceof AutorePersonaleType) {
             t010 = ((AutorePersonaleType) _datiElementoAut).getT010();
         } else if (_datiElementoAut instanceof EnteType) {
             t010 = ((EnteType) _datiElementoAut).getT010();
         }
         if (t010 != null)
             isadn = t010.getA_010().toString();
    }

    public AutoreValida(ModificaType modificaType) {
        super();
        _tipoControllo = modificaType.getTipoControllo();
        _datiElementoAut = modificaType.getElementoAut().getDatiElementoAut();
        estraiIsadn();
    }

    //questo costruttore serve per il factoring di localizzazione
    public AutoreValida() {
        super();
    }

    /**
     *
     * validaPerCancella:
     * - controllo l'esistenza per identificativo
     * - se la forma è "A" controllo che non esistano legami con titoli (tr_tit_aut)
     *   altrimenti segnalo diagnostico "Esistono legami a titoli"
     * - controllo che non esistano localizzazioni diverse dal polo dell'utente che
     *   stacancellando (tr_aut_bib) altrimenti segnalo "Autore localizzato in altri
     *   poli, cancellazione impossibile"
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public Tb_autore validaPerCancella(String utente, String idAutore)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        TableDao tavola;
        Tb_autore tb_autore = new Tb_autore();
        Autore autore = new Autore();
        tavola = autore.cercaAutorePerID(idAutore);
        List vettoreAutori = tavola.getElencoRisultati();

        if (vettoreAutori.size() == 0)
            throw new EccezioneSbnDiagnostico(3001, "idAutore non esistente");
        else {
            tb_autore = (Tb_autore) vettoreAutori.get(0);
        }


        // Inizio Intervento almaviva2 BUG MANTIS 4491 esercizio (Documento almaviva1: la cancellazione di un autore è sempre permessa
        // in POLO su Autori condivisi in quanto può essere effettuata solo a fronte di previa cancellazione in Indice: quindi se
        // l'Indice ha effettuato la cancellazione è corretta allinearsi nin tutti i casi.


//        // non ho livelloAut in input dal xml, imposto uguale al livello letto sul db
//    	controllaParametriUtente(utente, tb_autore.getCD_LIVELLO(), tb_autore.getCD_LIVELLO(), false, false);

        if (tb_autore.getFL_CONDIVISO().equals("n")) {
        	controllaParametriUtente(utente, tb_autore.getCD_LIVELLO(), tb_autore.getCD_LIVELLO(), false, false);
        } else {
        	controllaParametriUtente(utente, "01", "01", false, false);
        }
        // Fine Intervento almaviva2 BUG MANTIS 4491 esercizio
        String cd_polo = utente.substring(0, 3);

        // Marzo 2016: gestione ISNI (International standard number identifier)
       	if (tb_autore.getISADN() != null) {
	       throw new EccezioneSbnDiagnostico(3390); //"Autore con numero ISNI non può essere cancellato");
        }


        if (tb_autore.getTP_FORMA_AUT().equals("A")) {
            //controllo che non esistano legami con i titoli
            TitoloAutore titoloAutore = new TitoloAutore();
            tavola = titoloAutore.cercaLegamiAutoreTitolo(idAutore);
            List v = tavola.getElencoRisultati();

            if (v.size() > 0)
                throw new EccezioneSbnDiagnostico(3091, "Esistono legami a titoli");
            AutoreBiblioteca autoreBiblioteca = new AutoreBiblioteca();
            if (ValidatorProfiler.getInstance().isPolo(cd_polo)
                && autoreBiblioteca.verificaLocalizzazioniTr_aut_bib(idAutore, cd_polo))
                throw new EccezioneSbnDiagnostico(
                    3092,
                    "Autore localizzato in altri poli, cancellazione impossibile");
        }
        AutoreMarca autMar = new AutoreMarca();
        if (autMar.cercaLegamiAutore(idAutore).size() > 0)
            throw new EccezioneSbnDiagnostico(3057, "Autore legato a marche, cancellazione impossibile");
        return tb_autore;
    }

    /**
     *
     * validaPerFonde:
     * i due autori idPartenza e idArrivo devono esistere in tb_autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaPerFonde(String utente, String idPartenza, String idArrivo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        Tb_autore tb_autore = new Tb_autore();
        Autore autore = new Autore();
        tavola = autore.cercaAutorePerID(idPartenza);
        List vettoreAutori = tavola.getElencoRisultati();
        if (vettoreAutori.size() == 0)
            throw new EccezioneSbnDiagnostico(3991, "idPartenza non esistente");
        else {
            tb_autore = (Tb_autore) vettoreAutori.get(0);

            if (!tb_autore.getTP_FORMA_AUT().equals("A"))
                throw new EccezioneSbnDiagnostico(3075, "tipo forma errato");
        }

        // Inizio Intervento almaviva2 BUG MANTIS 4537-4533 esercizio (Documento almaviva1: la cancellazione/fusione di un autore
        // è sempre permessa in POLO su Autori condivisi in quanto può essere effettuata solo a fronte di previa cancellazione in Indice:
        // quindi se l'Indice ha effettuato la cancellazione è corretta allinearsi nin tutti i casi.

//        // non ho livelloAut in input dal xml, imposto uguale al livello letto sul db
//        controllaParametriUtente(utente, tb_autore.getCD_LIVELLO(), tb_autore.getCD_LIVELLO(), false,false);

        if (tb_autore.getFL_CONDIVISO().equals("n")) {
        	controllaParametriUtente(utente, tb_autore.getCD_LIVELLO(), tb_autore.getCD_LIVELLO(), false,false);
        } else {
        	controllaParametriUtente(utente, "01", "01", false, false);
        }
         // Fine Intervento almaviva2 BUG MANTIS 4491 esercizio


        tavola = autore.cercaAutorePerID(idArrivo);
        List vettoreAutoriArrivo = tavola.getElencoRisultati();
        if (vettoreAutoriArrivo.size() == 0)
            throw new EccezioneSbnMessage(3991, "idArrivo non esistente");
        else {
            verificaAllineamentoModificaAutore(idPartenza);
            verificaAllineamentoModificaAutore(idArrivo);
            tb_autore = (Tb_autore) tavola.getElencoRisultati().get(0);

            if (!tb_autore.getTP_FORMA_AUT().equals("A"))
                throw new EccezioneSbnDiagnostico(3075, "tipo forma errato");
        }
    }

    /**
     * metodo di validazione per operazione di creazione autore:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean validaPerCrea(String utente, boolean ignoraId, Tb_autore autoreCr, boolean _import, boolean simileImport, boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_autore tb_autore = null;
        if (!ignoraId) {
            tb_autore = verificaEsistenzaID();
            //se tb_autore è stato trovato significa che non posso crearlo nuovamente
            if (tb_autore != null) {
                //ritorno un diagnostico
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Id di autore già esistente nel DB");
            }
            if (!verificaVid(_datiElementoAut.getT001(), utente,_import))
                throw new EccezioneSbnDiagnostico(3902, "Vid non valido");

         }
        if (!simileImport) {
            String livelloAutInput = _datiElementoAut.getLivelloAut().toString();
            // imposto a 05 (livello minimo) il livello letto da db
            controllaParametriUtente(utente, livelloAutInput, "05", true,_cattura);
        }


     // Inizio Marzo 2016: gestione ISNI (International standard number identifier)
        // Questa parte è stata fatta in Indice ma non viene riportata in polo
        estraiIsadn();
//        if (isadn != null && !utente.substring(0,6).equalsIgnoreCase("XXXAMM")) {
//			throw new EccezioneSbnDiagnostico(2900, "Gestione numero ISNI non consentita");
//        }


        List listaAutori;
//        if (isadn != null) {
//            TableDao tavola = cercaAutorePerISADN(isadn);
//            listaAutori = tavola.getElencoRisultati();
//
//            if (listaAutori.size() > 0)
//                throw new EccezioneSbnDiagnostico(3048, "ISADN di autore già esistente nel DB");
//        }

        // Fine Marzo 2016: gestione ISNI (International standard number identifier)


        if (SbnSimile.SIMILE.getType() == _tipoControllo.getType() || simileImport) {
            listaAutori = verificaEsistenzaPerNome(utente, autoreCr);
            //se listaAutori è vuota significa che non esistono autori simili
            if (listaAutori.size() != 0) {
                elencoDiagnostico = listaAutori;
                return false;
            }
        } else {
            listaAutori = verificaEsistenzaConferma(autoreCr);
            // almaviva2 20/aprile/2007 DECIDE CHE A FRONTE DI UN CATTURA DA INDICE IN PRESENZA
            // DI SIMILI O == all'autore di cattura il controllo su i simili non viene effettuato
            if(!_cattura){
                if (listaAutori.size() != 0) {
                    elencoDiagnostico = listaAutori;
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * metodo di validazione per operazione di modifica autore:
     * . verificaEsistenzaID: se non trovato ritorna diagnostico 'Autore inesistente'
     * . verificaVersioneLuogo: se il risultato è negativo ritorna il diagnostico '
     * Versione non aggiornata'
     * . verificaLivelloModifica: se il risultato è falso ritorna il diagnostico al
     * client.
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * autori/autori trovato al client
     * se non esistono autori simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_autore validaPerModifica(String utente,Tb_autore tb_autore, Tb_autore autoreMod,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        String livelloAutInput = _datiElementoAut.getLivelloAut().toString();
        controllaParametriUtente(utente, livelloAutInput, tb_autore.getCD_LIVELLO(), false,_cattura);

        // inizio Marzo 2016: gestione ISNI (International standard number identifier)
        // questa modifica dell'inidce non viene riportata in polo
        estraiIsadn();
////      if (isadn != null && !utente.equals("XXXAMM")) {
//        if (isadn != null && !utente.substring(0,6).equalsIgnoreCase("XXXAMM")) {
//			throw new EccezioneSbnDiagnostico(2900, "Gestione numero ISNI non consentita");
//        }
        // fine Marzo 2016: gestione ISNI (International standard number identifier)
        if (!verificaVersioneAutore(tb_autore)) {
            throw new EccezioneSbnDiagnostico(3014);
        } else {
            if (_datiElementoAut.getStatoRecord() != null
                && _datiElementoAut.getStatoRecord().getType() == StatoRecord.valueOf("c").getType()) {
                if (!(autoreMod.getDS_NOME_AUT().trim().equals(tb_autore.getDS_NOME_AUT().trim())
                    && autoreMod.getTP_NOME_AUT().trim().equals(tb_autore.getTP_NOME_AUT().trim()))) {
                    if (_tipoControllo == null || _tipoControllo.getType() != SbnSimile.CONFERMA_TYPE) {
                        autoreMod.setVID(tb_autore.getVID());
                        elencoDiagnostico = verificaEsistenzaPerNome(utente, autoreMod);
                    } else {
                        //Rimetto questo controllo, dopo che l'evevo dovuto togliere
                        elencoDiagnostico = verificaEsistenzaConferma(autoreMod);
                    }
                }
            }
        }
        autoreMod.setTS_VAR(tb_autore.getTS_VAR());
        return tb_autore;
    }

    /**
     * Metodo che verifica se esistono autori con lo stesso nome già in base dati:
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List verificaEsistenzaPerNome(String utente, Tb_autore tb_autore)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola;
        List v;
        if (new TipiAutore().isPersonale(tb_autore)) {
            tavola = cercaAutoreSimile(tb_autore, null, "selectSimile", null);
            v = tavola.getElencoRisultati();

        } else {
            if (tb_autore.getKY_EL1_A() != null)
                tb_autore.settaParametro(TableDao.XXXky_el1,tb_autore.getKY_EL1_A()
                        + (tb_autore.getKY_EL1_B() == null ? "" : tb_autore.getKY_EL1_B()));
            if (tb_autore.getKY_EL2_A() != null)
                tb_autore.settaParametro(TableDao.XXXky_el2,tb_autore.getKY_EL2_A()
                        + (tb_autore.getKY_EL2_B() == null ? "" : tb_autore.getKY_EL2_B()));
            tavola = cercaAutoreSimile(tb_autore, null, "selectSimileEnti_1", null);
            v = tavola.getElencoRisultati();

            if (v.size() == 0) {
                tavola = cercaAutoreSimile(tb_autore, null, "selectSimileEnti_2", null);
                v = tavola.getElencoRisultati();

            }
        }
        if (v.size() == 0 && !tb_autore.getTP_NOME_AUT().equals("G")) {
            Tbf_par_auth par =
            	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "AU");
            if (par != null && par.equals("000"))
                //select per parole
                v = verificaEsistenzaPerParole(tb_autore);
        }
        return v;
        //se listaAutori è vuota significa che non esistono autori simili
    }

    public List verificaEsistenzaPerParole(Tb_autore tb_autore)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List v = new ArrayList();
        //Devo rimuovere la punteggiatura
        String ds_nome = Formattazione.rimuoviPunteggiatura(tb_autore.getDS_NOME_AUT());
        List parole = new ArrayList();
        StringTokenizer st = new StringTokenizer(ds_nome);
        while (st.hasMoreTokens()) {
            String parola = st.nextToken();
            if (!ElencoParole.getInstance().contiene(parola))
                parole.add(parola);
        }
        if (parole.size() > 0) {
            Tb_autore aut = new Tb_autore();
            for (int i = 1; i <= parole.size(); i++)
                aut.settaParametro("XXXparola" + i, parole.get(i - 1));
            Tb_autoreResult tavola = new Tb_autoreResult(aut);


            tavola.executeCustom("selectPerParoleNome");
            v = tavola.getElencoRisultati();

        }
        return v;
    }

    /**
     * Metodo che verifica se esistono autori con lo stesso nome già in base dati, per i controlli di
     * tipo 'conferma'
     * QUESTO METODO NON è PIù UTILIZZATO: mail di Daniela del 12 Marzo 2004.
     * @throws Exception
     * @throws InvocationTargetException
     */
    public List verificaEsistenzaConferma(Tb_autore tb_autore)
        throws InvocationTargetException, Exception {
        //La forma R non deve essere verificata
        if (tb_autore.getTP_FORMA_AUT().equals("R"))
            return new ArrayList();
        TableDao tavola;
        List v;
        if (tb_autore.getKY_EL1_A() != null)
            tb_autore.settaParametro(TableDao.XXXky_el1,
                tb_autore.getKY_EL1_A() + (tb_autore.getKY_EL1_B() == null ? "" : tb_autore.getKY_EL1_B()));
        else
            tb_autore.settaParametro(TableDao.XXXky_el1null, "OK");

        if (tb_autore.getKY_EL2_A() != null)
            tb_autore.settaParametro(TableDao.XXXky_el2,
                tb_autore.getKY_EL2_A() + (tb_autore.getKY_EL2_B() == null ? "" : tb_autore.getKY_EL2_B()));
        else
            tb_autore.settaParametro(TableDao.XXXky_el2null, "OK");
        if (tb_autore.getKY_EL3() == null)
            tb_autore.settaParametro(TableDao.XXXky_el3null, "OK");
        if (tb_autore.getKY_EL4() == null)
            tb_autore.settaParametro(TableDao.XXXky_el4null, "OK");
        if (tb_autore.getKY_EL5() == null)
            tb_autore.settaParametro(TableDao.XXXky_el5null, "OK");
        if (new TipiAutore().isPersonale(tb_autore)) {
            if (tb_autore.getKY_CLES2_A() == null)
                tb_autore.settaParametro(TableDao.XXXky_cles2_anull, "OK");
            tavola = cercaAutoreSimile(tb_autore, null, "selectSimileConferma", null);
            v = tavola.getElencoRisultati();

        } else {
        	// Inizio Modifica 11.01.2012 almaviva2 per controllo duplicati nella modifica di autori di tipo Ente viene eliminato
        	// la trasformqazione in maiuscolo che in alcuni casi sconvolge l'esito della select
        	// tb_autore.settaParametro(TableDao.XXXds_nome_cerca_simili,tb_autore.getDS_NOME_AUT().trim().toUpperCase());
        	tb_autore.settaParametro(TableDao.XXXds_nome_cerca_simili,tb_autore.getDS_NOME_AUT().trim());
        	// Fine Modifica 11.01.2012 almaviva2
            tb_autore.settaParametro(TableDao.XXXtp_forma_conferma,tb_autore.getTP_FORMA_AUT());
            tavola = cercaAutoreSimile(tb_autore, null, "selectSimileEnti_1", null);
            v = tavola.getElencoRisultati();

        }
        return v;
        //se listaAutori è vuota significa che non esistono autori simili
    }

    /**
     * Metodo che verifica se l'autore esiste già in base dati:
     * . se T001 è valorizzato invoca   cercaLuogo.cercaLuogoPerID(); se esiste
     * ritorna Tb_autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_autore verificaEsistenzaID() throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_autore tbAutore = null;
        if (_datiElementoAut.getT001() != null) {
            tbAutore = new Tb_autore();
            tbAutore.setVID(_datiElementoAut.getT001());
            Tb_autoreResult tabella = new Tb_autoreResult(tbAutore);
            tabella.executeCustom("selectEsistenzaId");
            List lista = tabella.getElencoRisultati();
            tbAutore = null;
            if (lista.size() != 0)
                tbAutore = (Tb_autore) lista.get(0);
        }
        return tbAutore;
    }

    /**
     * Metodo che verifica se l'autore esiste già in base dati:
     * . se T001 è valorizzato invoca   cercaLuogo.cercaLuogoPerID(); se esiste
     * ritorna Tb_autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_autore verificaEsistenzaID(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_autore tbAutore = new Tb_autore();
        tbAutore.setVID(vid);
        Tb_autoreResult tabella = new Tb_autoreResult(tbAutore);
        tabella.executeCustom("selectEsistenzaId");
        List lista = tabella.getElencoRisultati();

        tbAutore = null;
        if (lista.size() != 0)
            tbAutore = (Tb_autore) lista.get(0);
        return tbAutore;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaVersioneAutore(Tb_autore tb_autore) throws EccezioneSbnDiagnostico {
        if (_datiElementoAut.getT005() != null){
            SbnDatavar datevar = new SbnDatavar(tb_autore.getTS_VAR());
            return datevar.getT005Date().equals(_datiElementoAut.getT005());
            //return tb_autore.getTS_VAR().getOriginalDate().equals(_datiElementoAut.getT005());
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }

    /**
     * metodo che verifica se l'utente ha sufficente livello di autorità per
     * effettuare l'operazione richiesta.
     * livelloAutInput: livello letto dal xml in input
     * livelloAutDb: livello letto dal record sul db
     * forzatura: per modifica e creazione, true valida l'autorizzazione alla forzatura
     */

    public void controllaParametriUtente(
        String utente,
        String livelloAutInput,
        String livelloAutDb,
        boolean forzatura,
        boolean _cattura)
        throws EccezioneSbnDiagnostico {
        if(!_cattura){
            // TEST PER DISABILITARE AUTORITY
        	Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "AU");
        	//Tbf_par_auth par = null;
            if (par == null)
                throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
            String livelloUtente = par.getCd_livello();
            if (par.getTp_abil_auth()!='S')
                throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
            // livello ricevuto in input > di livello abilitato all'utente
            if (Integer.parseInt(livelloAutInput) > Integer.parseInt(livelloUtente))
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
            // livello letto dal db maggiore del livello abilitato all'utente
            if (Integer.parseInt(livelloAutDb) > Integer.parseInt(livelloUtente))
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
            if (forzatura && (_tipoControllo != null && _tipoControllo.getType() == SbnSimile.CONFERMA_TYPE))
                if (par.getFl_abil_forzat()!='S')
                    throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
        }

    }

    public void validaDescrizioneEnti(String nome) throws EccezioneSbnDiagnostico, EccezioneDB {
        Ts_stop_list ts = ElencoVociAutori.verificaEsistenzaVoce(nome);
        if (ts != null) {
            EccezioneSbnDiagnostico ecc =
                new EccezioneSbnDiagnostico(3100, "Voce errata; " + ts.getNOTA_STOP_LIST());
            ecc.appendMessaggio("Voce errata: " + nome + " , " + ts.getNOTA_STOP_LIST());
            throw ecc;
        }
    }

    public SBNMarc getDiagnostico() {
        return null;
    }

    public List getElencoDiagnostico() {
        return elencoDiagnostico;
    }

    /**
    	  * Method verificaVid.
    	  * Verifica se il polo presente nei primi 3 caratteri del VID, esiste.
       * La lunghezza deve essere di 10 caratteri
    	  *
    	  * @param bid
    	  * @return boolean
    	  */
    public static boolean verificaVid(String vid, String user, boolean _import) {
        boolean b = false;
        if (vid.length() != 10) {
            return false;
        }

        char[] c_vid = vid.toCharArray();
        //In import posso importare autori con sigla di altri poli.
        if (_import == false) {
            if (!vid.startsWith(SIGLA_INDICE)) {
                // Ometto il controllo sul POLO che invia la richiesta el il polo contenuto
                // nel ID dell'oggetto
                // Si ipotizza vedi (almaviva2 ) che questo controllo non serva in polo in quanto
                // in fase di cattura devo importare anche id diversi dal polo stesso
                // VERIFICARE SE QUESTO CONTROLLO EFFETTIVAMENTE NON SERVA
                //if (user != null && !user.substring(0,3).equals(vid.substring(0, 3)))
                //    return false;

            	// LA CHAIMATA AL CONTROLLO E' STATO TOLTO SU INDICAZIONE DI almaviva2 12/12/2007
//                if (Decodificatore.getCd_tabella("POLO", vid.substring(0, 3)) == null)
//                    return false;
            }
        }
        if (c_vid[3]=='V') {
            boolean c = true;
            for (int i = 4; i < c_vid.length && b; i++) {
                c = Character.isDigit(c_vid[i]);
            }
            b = c;
        }
        return b;
    }

    public void verificaAllineamentoModificaAutore(String vid)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_aut_bib tb = new Tr_aut_bib();
        tb.setVID(vid);
        tb.setFL_ALLINEA("X");
        Tr_aut_bibResult res = new Tr_aut_bibResult(tb);
        res.executeCustom("selectPerFlagAllinea");
        if (res.getElencoRisultati().size() > 0) {
            throw new EccezioneSbnDiagnostico(3310, "In allineamento");
        }
    }

}
