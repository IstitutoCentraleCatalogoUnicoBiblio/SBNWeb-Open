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
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloUniformeMusicaValida
 * <p>
 * Esegue i vari controlli per la creazione o la modifica di un titolo uniforme musica
 * e anche dei vari legami che vengono coinvolti.
 * </p>
 *
 * @author
 * @author
 *
 * @version 3-mar-2003
 */
public class TitoloUniformeMusicaValida extends TitoloUniformeValida {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3934837925167750746L;
	private TitoloUniformeMusicaType titUni = null;

    public TitoloUniformeMusicaValida() {
        super();
    }

    public TitoloUniformeMusicaValida(CreaType datiType) throws IllegalArgumentException, InvocationTargetException, Exception {
        super(datiType);
        if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeMusicaType)
            titUni = (TitoloUniformeMusicaType) elementAutType.getDatiElementoAut();
    }

    public TitoloUniformeMusicaValida(ModificaType tipo) throws IllegalArgumentException, InvocationTargetException, Exception {
        super( tipo);
        if (elementAutType.getDatiElementoAut() instanceof TitoloUniformeMusicaType) {
            titUni = (TitoloUniformeMusicaType) elementAutType.getDatiElementoAut();
        }

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
    public boolean validaPerCrea(
        String utente,
        Tb_titolo tb_titolo,
        boolean ignoraId,
        TimestampHash timeHash,
        boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (titUni == null)
            throw new EccezioneSbnDiagnostico(100, "Xml non corretto");
        elencoDiagnostico = null;
        Tb_titolo titolo = null;
        if (!ignoraId) {
            titolo = verificaEsistenzaID(id);
            //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
            if (titolo != null) {
                //ritorno un diagnostico
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Id di titolo già esistente nel DB");
            }
        }
        verificaLivelloCreazione(utente, "UM", _cattura);

        List listaTitoli;
        ChiaviTitolo chiavi = elaboraComposizione(tb_titolo);
        if (tipoControllo.getType() != SbnSimile.CONFERMA_TYPE) {
            listaTitoli = cercaTitoliSimili(chiavi, null);
            //se lista è vuota significa che non esistono titoli simili
            if (listaTitoli.size() != 0) {
                elencoDiagnostico = listaTitoli;
                return false;
            }
        }
        verificaCorrettezza();
        //if (titUni.getT015() != null) {
        //Verifica unicità
        //}

        //Ora valida i legami.
     // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
        validaLegami.validaCreaLegamiTitoloUniforme(utente, tb_titolo.getBID(), "A", timeHash);
        return true;
    }

    protected ChiaviTitolo elaboraComposizione(Tb_titolo tb_titolo) throws EccezioneSbnDiagnostico {
        ChiaviTitolo chiavi = new ChiaviTitolo();
        A928 a928 = titUni.getT928();
        A929 a929 = titUni.getT929();
        if (a928 == null || a929 == null)
            throw new EccezioneSbnDiagnostico(3120, "Manca t928 o t929");
        // Inizio Modifica richiesta per far passare l'organico mancante nelle composizioni; L.almaviva2 15.06.2009
//        if (a928.getB_928() == null)
//            throw new EccezioneSbnDiagnostico(3122, "Manca b928");

        if (a929.getG_929() == null)
            throw new EccezioneSbnDiagnostico(3123, "Manca g929");
        if (a928.getA_928Count() == 0)
            throw new EccezioneSbnDiagnostico(3149, "Codice forma mancante");
        if (a928.getA_928Count() > 0)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_1", a928.getA_928(0)) == null)
                throw new EccezioneSbnDiagnostico(3159, "Codice forma errato");
        if (a928.getA_928Count() > 1)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_2", a928.getA_928(1)) == null)
                throw new EccezioneSbnDiagnostico(3159, "Codice forma errato");
        if (a928.getA_928Count() > 2)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_3", a928.getA_928(2)) == null)
                throw new EccezioneSbnDiagnostico(3159, "Codice forma errato");
        if (a929.getE_929() != null)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", a929.getE_929()) == null)
                throw new EccezioneSbnDiagnostico(3160, "Codice tonalita' errato");
        //A230 a230 = titUni.getT230();
        //if (a230 != null)
        CostruttoreIsbd isbd = new CostruttoreIsbd();
        isbd.definisciISBDtitUniMusicale(tb_titolo, titUni);
        A230 a230 = titUni.getT230();
        chiavi.estraiChiavi(tb_titolo.getISBD());
        tb_titolo.setKY_CLES1_T(chiavi.getKy_cles1_t());
        tb_titolo.setKY_CLES2_T(chiavi.getKy_cles2_t());
        tb_titolo.setKY_CLET1_T(chiavi.getKy_clet1_t());
        tb_titolo.setKY_CLET2_T(chiavi.getKy_clet2_t());
        tb_titolo.setKY_CLES1_CT(chiavi.getKy_cles1_ct());
        tb_titolo.setKY_CLES2_CT(chiavi.getKy_cles2_ct());
        tb_titolo.setKY_CLET1_CT(chiavi.getKy_clet1_ct());
        tb_titolo.setKY_CLET2_CT(chiavi.getKy_clet2_ct());
        return chiavi;

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

    public Tb_titolo validaPerModifica(String utente, boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        tb_titolo = estraiTitoloPerID(id);
        StatoRecord statoRecord = elementAutType.getDatiElementoAut().getStatoRecord();
        if (tb_titolo != null) {
            if (titUni.getT005() != null) {
                if (!verificaVersioneTitolo(tb_titolo)) {
                    throw new EccezioneSbnDiagnostico(3014, "Versione non corretta");
                } else {
                    verificaLivelloModifica(utente, tb_titolo, "UM", _cattura);
                    verificaAllineamentoModificaTitolo(tb_titolo.getBID());
                    verificaLocalizzazioni(tb_titolo, utente);
                    if (statoRecord != null && statoRecord.getType() == StatoRecord.valueOf("c").getType()){
                        ChiaviTitolo chiavi = elaboraComposizione(tb_titolo);
                        if ((tipoControllo == null || tipoControllo.getType() != SbnSimile.CONFERMA_TYPE)) {
                            elencoDiagnostico = cercaTitoliSimili(chiavi, null);
                        }
                        verificaCorrettezza();
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

    public void validaComposizione() throws EccezioneSbnDiagnostico {
        A928 a928 = titUni.getT928();
        A929 a929 = titUni.getT929();
        for (int i = 0; i < a928.getA_928Count(); i++)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_1", a928.getA_928(i)) == null)
                throw new EccezioneSbnDiagnostico(3075, "Tipo forma inesistente");
        if (a929.getE_929() != null)
            if (Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", a929.getE_929()) == null)
                throw new EccezioneSbnDiagnostico(3076, "Codice tonalità inesistente");
    }
    public boolean verificaVersioneTitolo(Tb_titolo tb_titolo) throws EccezioneSbnDiagnostico {
        if (titUni != null && titUni.getT005() != null){
            SbnDatavar data = new SbnDatavar(tb_titolo.getTS_VAR());
            return data.getT005Date().equals(titUni.getT005());
        }

        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }

    /**
     *
     * validaPerCancella titolo uniforme musicale.
     * Valida il titolo:
     * controllo di esistenza per identificativo, se
     * tp_materiale <> 'U' o cd_natura <> 'A' segnala diagnostico 'Il titolo non è
     * uniforme musicale'
     *
     * controllo che non esistano legami con titoli verso il basso(tr_tit_tit con
     * bid=bid_coll), altrimenti segnalo diagnostico 'Esistono legami a titoli'
     * se non ok ritorna il msg response di diagnostica (analogo a creazione)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public Tb_titolo validaPerCancellaTitUniMus(String bid, String utente, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tb_titolo = verificaEsistenzaID(bid);
        if (tb_titolo == null)
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        if (!tb_titolo.getCD_NATURA().equals("A") || !tb_titolo.getTP_MATERIALE().equals("U"))
            throw new EccezioneSbnDiagnostico(3098, "Il bid non individua un titolo uniforme musicale");
        verificaLivelloCancellazione(utente,tb_titolo,"UM");
        verificaLocalizzazioniCancellazione(tb_titolo, utente);
        if (contaLegamiVersoBasso(bid) > 0)
            throw new EccezioneSbnDiagnostico(3095, "Il documento è un livello di raggruppamento");
        //timeHash.putTimestamp("Tb_titolo",tb_titolo.getBID(),tb_titolo.getTS_VAR());
        //[TODO: se si usano i timestamp si devono leggere tutte le tavole collegate.
        return tb_titolo;

    }

}
