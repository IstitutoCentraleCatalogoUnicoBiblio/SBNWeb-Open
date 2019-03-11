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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloUniformeValida.java
 * <p>
 * Esegue i vari controlli per la creazione o la modifica di un titolo uniforme
 * e anche dei vari legami che vengono coinvolti.
 * </p>
 *
 * @author
 * @author
 *
 * @version 3-mar-2003
 */
public class TitoloUniformeValida extends Titolo {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5491081150554292121L;
	protected SbnSimile tipoControllo = null;
    protected List elencoDiagnostico = null;
    protected ElementAutType elementAutType = null;
    private TitoloUniformeType titUni = null;
    protected DatiElementoType datiEl = null;
    protected SbnLivello livelloAut = null;
    String id = null;
    protected TitoloValidaLegami validaLegami = null;

    public TitoloUniformeValida() {
        super();
    }

    public TitoloUniformeValida(CreaType datiType) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        tipoControllo = datiType.getTipoControllo();
        elementAutType = datiType.getCreaTypeChoice().getElementoAut();
        LegamiType[] legami = null;
        legami = elementAutType.getLegamiElementoAut();
        id = elementAutType.getDatiElementoAut().getT001();
        livelloAut = elementAutType.getDatiElementoAut().getLivelloAut();
        if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeType)
            titUni = (TitoloUniformeType) elementAutType.getDatiElementoAut();
        validaLegami = new TitoloValidaLegami(id, legami);
    }

    public TitoloUniformeValida(ModificaType tipo) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        this.tipoControllo = tipo.getTipoControllo();
        elementAutType = tipo.getElementoAut();
        //Esempio di stringa id da prendere
        id = elementAutType.getDatiElementoAut().getT001();
        livelloAut = elementAutType.getDatiElementoAut().getLivelloAut();
        LegamiType[] legami = null;
        legami = elementAutType.getLegamiElementoAut();
        if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeType)
            titUni = (TitoloUniformeType) elementAutType.getDatiElementoAut();
        validaLegami = new TitoloValidaLegami(id, legami);

    }

    /**
     * Verifica se un titolo che si intende modificare esiste tramite ricerca per ID
     * @return Nel caso esista restituisce il relativo oggetto Tb_titolo.
     * @throws InfrastructureException
     */
    public Tb_titolo verificaEsistenzaID(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        Tb_titolo tb_titolo = null;
        tb_titolo = estraiTitoloPerID(id);
        //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
        return tb_titolo;
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
    public boolean validaPerCrea(String utente, boolean ignoraId, TimestampHash timeHash,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (titUni == null)
            throw new EccezioneSbnDiagnostico(100, "Xml non corretto");
        String natura = "A";
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        if (!ignoraId) {
            tb_titolo = verificaEsistenzaID(id);
            //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
            if (tb_titolo != null) {
                //ritorno un diagnostico
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Id di titolo già esistente nel DB");
            }
        }
        verificaLivelloCreazione(utente, "TU",_cattura);

        List listaTitoli;
        ChiaviTitolo chiavi = new ChiaviTitolo();

        // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
        if (titUni.getT231() != null) {
    		if (titUni.getT231().getA_231() != null) { //si gestisce opera 231
    			chiavi.estraiChiavi(titUni.getT231().getA_231());
    		} else
    			throw new EccezioneSbnDiagnostico(2900, "Errore nel campo T231");
    	} else {
            if (titUni.getT431() != null) {
        		if (titUni.getT431().getA_431() != null) { //si gestisce variante 431
        			chiavi.estraiChiavi(titUni.getT431().getA_431());
        			// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
        			natura = "V";
        		} else
        			throw new EccezioneSbnDiagnostico(2900, "Errore nel campo T431");
    		}
        }
//        A230 a230 = titUni.getT230();
//        if (a230 != null)
//            chiavi.estraiChiavi(a230.getA_230()); //??
//        else
//            throw new EccezioneSbnDiagnostico(3221, "Manca t230");





        verificaCorrettezza();
        if (tipoControllo.getType() != SbnSimile.CONFERMA_TYPE) {
            listaTitoli = cercaTitoliSimili(chiavi, null);
            //se lista è vuota significa che non esistono titoli simili
            if (listaTitoli.size() != 0) {
                elencoDiagnostico = listaTitoli;
                return false;
            }
        }

        if (titUni.getT015() != null) {
            //Verifica unicità
        }

        //Ora valida i legami.
     // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
        validaLegami.validaCreaLegamiTitoloUniforme(utente, id, natura, timeHash);
        return true;
    }

    private void verificaCorrettezza () throws EccezioneSbnDiagnostico {
        if (titUni.getT101() != null)
            for (int i = 0; i < titUni.getT101().getA_101Count(); i++)
                if (Decodificatore
                    .getCd_tabella("Tb_titolo", "cd_lingua_1", titUni.getT101().getA_101(i).toUpperCase())
                    == null)
                    throw new EccezioneSbnDiagnostico(3204, "Codice lingua errato");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected List cercaTitoliSimili(ChiaviTitolo chiavi, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        //settaParametro(TableDao.XXXstringaEsatta", nome);
        setKY_CLES1_T(chiavi.getKy_cles1_t());
        setKY_CLES2_T(chiavi.getKy_cles2_t());
        setKY_CLET1_T(chiavi.getKy_clet1_t());
        setKY_CLET2_T(chiavi.getKy_clet2_t());
        tavola.executeCustom("selectSimiliTU", ordinamento);
        List v = tavola.getElencoRisultati();
        return v;
    }

    public Tb_titolo validaPerModifica(String utente,boolean _cattura) throws EccezioneDB, EccezioneSbnDiagnostico, IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        tb_titolo = estraiTitoloPerID(id);
        StatoRecord statoRecord = elementAutType.getDatiElementoAut().getStatoRecord();
        if (tb_titolo != null) {
            if (titUni.getT005() != null) {
                if (!verificaVersioneTitolo(tb_titolo)) {
                    throw new EccezioneSbnDiagnostico(3014, "Versione non corretta");
                } else {
                    verificaLivelloModifica(utente, tb_titolo, "TU", _cattura);
                    verificaAllineamentoModificaTitolo(tb_titolo.getBID());
                    verificaLocalizzazioni(tb_titolo, utente);
                    if (tb_titolo.getTP_MATERIALE().equals("U"))
                        throw new EccezioneSbnDiagnostico(3105, "Materiale del titolo non corretto");
                    if (statoRecord != null && statoRecord.getType() == StatoRecord.valueOf("c").getType()) {
                        verificaCorrettezza();
                            if (tb_titolo.getCD_NATURA().equals("B")) {
                                if (!ValidatorProfiler.getInstance().verificaAttivitaID(utente, CodiciAttivita.getIstance().MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022))
                                    throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
                            //} else if (!tb_titolo.getCD_NATURA().equals("A"))
                            	} else if (!tb_titolo.getCD_NATURA().equals("A") && !tb_titolo.getCD_NATURA().equals("V"))
                            		// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                                    // con tipo legame 431 (A08V)
                                throw new EccezioneSbnDiagnostico(3069, "Natura del titolo non corretta");
                        if ((tipoControllo == null || tipoControllo.getType() != SbnSimile.CONFERMA_TYPE)) {
                            ChiaviTitolo chiavi = new ChiaviTitolo();
                            // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
                            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
                            if (titUni.getT231() != null)
                        		chiavi.estraiChiavi(titUni.getT231().getA_231());
                        	else {
                        		if (titUni.getT431() != null) {
                            		chiavi.estraiChiavi(titUni.getT431().getA_431());
                        		} else {
                                    throw new EccezioneSbnDiagnostico(2900, "Errore: Manca il campo T231");
                        		}
                            }
                            // chiavi.estraiChiavi(titUni.getT230().getA_230());

                            elencoDiagnostico = cercaTitoliSimili(chiavi, null);
                        }
                    }
                }
            } else {
                throw new EccezioneSbnDiagnostico(3017);
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        }
        return tb_titolo;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaVersioneTitolo(Tb_titolo tb_titolo) throws EccezioneSbnDiagnostico {
        if (titUni != null && titUni.getT005() != null){
            SbnDatavar data = new SbnDatavar(tb_titolo.getTS_VAR());
            return data.getT005Date().equals(titUni.getT005());
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }

    /**
     * metodo che verifica se l'utente ha sufficente livello di autorità per
     * effettuare l'operazione di modifica richiesta.
     * decodifica livelloAut in tb_codici
     * se livelloAut > cd_livello letto sul db predispone in diagnostico: 'Livello di
     * autorità utente non consente l'operazione'.
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public void verificaLivelloModifica(String utente, Tb_titolo tb_titolo, String tipoUniforme,boolean _cattura)
        throws EccezioneSbnDiagnostico {
        if(!_cattura){
            if (Integer.parseInt(livelloAut.toString()) < Integer.parseInt(tb_titolo.getCD_LIVELLO())) {
            	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
            }
            Tbf_par_auth par =
            	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, tipoUniforme);
            if (par == null || par.getTp_abil_auth()!='S')
                throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
            String livelloUtente = par.getCd_livello();
            if (livelloUtente == null
                || Integer.parseInt(livelloAut.toString()) > Integer.parseInt(livelloUtente))
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");

            if (!livelloUtente.equals("97")) {
                if (tb_titolo.getCD_LIVELLO().equals("90") || tb_titolo.getCD_LIVELLO().equals("97"))
                    if (tb_titolo.getCD_LIVELLO().equals(livelloAut.toString()))


           	   // Inizio bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
//                  if (!utente.equals(tb_titolo.getUTE_VAR()))
                    if (!utente.startsWith(tb_titolo.getUTE_VAR().substring(0,3)))
                    	throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
         	   //Fine bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
            }




            if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
                if (par.getFl_abil_forzat()!='S')
                    throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
        }
    }

    public void verificaLivelloCreazione(String utente, String materiale,boolean _cattura) throws EccezioneSbnDiagnostico {
        Tbf_par_auth par =
        	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, materiale);
        if(!_cattura){
    		if (par == null)
    		   throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");

            String livelloUtente = par.getCd_livello();

            if (livelloUtente == null
                || Integer.parseInt(livelloAut.toString()) > Integer.parseInt(livelloUtente))
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
            if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
                if (par.getFl_abil_forzat()!='S')
                    throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
        }
    }

    public void verificaLivelloCancellazione(String utente, Tb_titolo tb_titolo, String materiale)
        throws EccezioneSbnDiagnostico {
        Tbf_par_auth par =
        	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, materiale);
        if (par == null || par.getTp_abil_auth()!='S')
            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
        String livelloUtente = par.getCd_livello();

        // Inizio Intervento almaviva2 BUG MANTIS 5412 esercizio:la cancellazione di un tit Uniforme è sempre permessa
        // in POLO su oggetti condivisi in quanto può essere effettuata solo a fronte di previa cancellazione in Indice: quindi se
        // l'Indice ha effettuato la cancellazione è corretto allinearsi in tutti i casi.
        if (livelloUtente == null || Integer.parseInt(tb_titolo.getCD_LIVELLO()) > Integer.parseInt(livelloUtente)) {
            if (tb_titolo.getFL_CONDIVISO().equals("n")) {
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
            }
        // throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        // Fine Intervento almaviva2 BUG MANTIS 4491 esercizio
        }

        // BUG esercizio 6362 almaviva2 26.01.2017
        // qualunque livello abbia la notizia in Polo, la cncellazione puo essere richiesta solo a seguito di una cancellazione
        // già effettuata dall'Indice per cui deve essere sempre effettuata: i controlli devono essere eseguiti solo per i
        // titoli locali e questo coltrollo è fatto sopra quindi la parte sottostante viene asteriscata;
//        if (!livelloUtente.equals("97")) {
//            if (tb_titolo.getCD_LIVELLO().equals("90") || tb_titolo.getCD_LIVELLO().equals("97"))
//                if (!utente.equals(tb_titolo.getUTE_VAR()))
//                    throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
//        }
    }

    /** Restituisce l'elenco diagnostico contenente i documenti simili */
    public List getElencoDiagnostico() {
        return elencoDiagnostico;
    }
    /**
     * metodo che verifica se il titolo è localizzato presso il polo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void verificaLocalizzazioni(Tb_titolo tb_titolo, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        if (!titBib.verificaLocalizzazioniPoloUguale(tb_titolo.getBID(), utente.substring(0, 6)))
            throw new EccezioneSbnDiagnostico(3115, "Titolo non localizzato nel polo");
    }

    /**
     *
     * validaPerCancella titolo uniforme.
     * Valida il titolo:
     * controllo di esistenza per identificativo, se tp_materiale <> ' ' o cd_natura
     * <> 'A' segnala diagnostico 'il bid non individua un titolo uniforme '.
     * controllo che non esistano legami con titoli verso il basso(tr_tit_tit con
     * bid=bid_coll), altrimenti segnalo diagnostico 'Esistono legami a titoli'
     * se non ok ritorna il msg response di diagnostica (analogo a creazione)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public Tb_titolo validaPerCancellaTitUni(String bid, String utente, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tb_titolo = verificaEsistenzaID(bid);
        if (tb_titolo == null)
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        // almaviva2- febbraio 2018 - evolutiva per cancellazione forma di rinvio dei titoli A
        // protocollo 2.03
        // if (!tb_titolo.getCD_NATURA().equals("A"))
        if (!tb_titolo.getCD_NATURA().equals("A") && !tb_titolo.getCD_NATURA().equals("V"))
            throw new EccezioneSbnDiagnostico(3097, "Il bid non individua un titolo uniforme");
        verificaLivelloCancellazione(utente, tb_titolo, "TU");
        TitoloBiblioteca titoloBib = new TitoloBiblioteca();
        verificaLocalizzazioniCancellazione(tb_titolo, utente);
        if (contaLegamiVersoBasso(bid) > 0)
            throw new EccezioneSbnDiagnostico(3095, "Il documento è un livello di raggruppamento");
        //timeHash.putTimestamp("Tb_titolo",tb_titolo.getBID(),tb_titolo.getTS_VAR());
        //[TODO: se si usano i timestamp si devono leggere tutte le tavole collegate.
        return tb_titolo;
    }

    /**
     * metodo che verifica se il titolo è localizzato presso il polo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void verificaLocalizzazioniCancellazione(Tb_titolo tb_titolo, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }

        TitoloBiblioteca titBib = new TitoloBiblioteca();
        if (!titBib.verificaLocalizzazioniPoloUguale(tb_titolo.getBID(), utente.substring(0, 6)))
            throw new EccezioneSbnDiagnostico(3115, "Titolo non localizzato nel polo");

        if (titBib.verificaLocalizzazioniPoloDiverso(tb_titolo.getBID(), utente))
            throw new EccezioneSbnDiagnostico(
                3092,
                "Titolo localizzato in altri poli, cancellazione impossibile");
    }

}
