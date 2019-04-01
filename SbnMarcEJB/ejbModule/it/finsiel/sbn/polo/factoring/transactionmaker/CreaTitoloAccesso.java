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
package it.finsiel.sbn.polo.factoring.transactionmaker;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloAccessoCrea;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloAccessoValida;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe CreaTitolo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 7-mag-03
 */
public class CreaTitoloAccesso extends CreaFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CreaTitoloAccesso");

    // Attributi da XML-input
    private DocumentoType documento;
    private TitAccessoType titAcc;

    private SbnLivello livelloAut;

    //entità collegata: ArrivoLegame
    private LegamiType[] arrivoLegame = null;

    private SbnSimile tipoControllo = null;
    //Utente che esegue la richiesta
    private SbnUserType sbnUser = null;
    private LocalizzaType localizzaType = null;
    private String  _condiviso;

    private String id_lista = null;

    private final static int maxRighe =
        Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
    //Autore creato dal factoring
    Tb_titolo titoloCreato;

    //vettore utilizzato per i risultati delle ricerche
    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
     * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     * @param  SBNMarc oggetto radice XML in input
     * @return istanza oggetto (default)
     */
    public CreaTitoloAccesso(SBNMarc input_root_object) {
        super(input_root_object);
        //do per scontato che ci siano tutti. Le verifiche sono fatte da chi
        //chiama il factoring

        documento = factoring_object.getCreaTypeChoice().getDocumento();
        titAcc = documento.getDocumentoTypeChoice().getDatiTitAccesso();
        arrivoLegame = documento.getLegamiDocumento();
        tipoControllo = factoring_object.getTipoControllo();
        sbnUser = input_root_object.getSbnUser();
        localizzaType = input_root_object.getSbnMessage().getSbnRequest().getCrea().getLocalizza();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (titAcc.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((titAcc.getCondiviso().getType() == TitAccessoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((titAcc.getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (titAcc.getCondiviso() != null && (titAcc.getCondiviso().equals(DatiDocTypeCondivisoType.S)) ) {
//        	_condiviso = titAcc.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }
    }

    /**
     * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
     * opportuno.
     * gestisce il tipo di risposta richiesto (lista o esame) e produce il
     * file xml di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeCrea()
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //tipoForma=null;
        //controllo la validità del luogo
        TitoloAccessoValida validaTitolo = new TitoloAccessoValida(factoring_object);
        boolean ignoraID = titAcc.getT001().equals(SBNMARC_DEFAULT_ID);
        TimestampHash timeHash = new TimestampHash();
        Tb_titolo tb_titolo = new Tb_titolo();
        String user = getCdUtente();
        if (validaTitolo.validaPerCrea(user, tb_titolo, ignoraID, timeHash,_cattura)) {
            if (tipoControllo.getType() == SbnSimile.SIMILEIMPORT_TYPE)
                throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
            Titolo titoloDB = new Titolo();
            //controllo se esistono informazioni ArrivoLegami
            String cd_livello = titAcc.getLivelloAut().toString();
            TitoloAccessoCrea titoloCrea = new TitoloAccessoCrea(factoring_object);
            if (tipoControllo.getType() == SbnSimile.CONFERMA_TYPE && !scheduled) {
                tb_titolo.setUTE_FORZA_INS(user);
                tb_titolo.setUTE_FORZA_VAR(user);
            }
            tb_titolo.setUTE_INS(user);
            tb_titolo.setUTE_VAR(user);

            //il cd livello potrebbe forse essere spostato.
            tb_titolo.setCD_LIVELLO(cd_livello);

            tb_titolo.setFL_SPECIALE(" ");
            tb_titolo.setFL_CANC(" ");
            //Questo lo metto perchè il bid serve per la lingua.ovviamente può essere000
            tb_titolo.setBID(titAcc.getT001());
            // Timbro Condivisione
            tb_titolo.setFL_CONDIVISO(_condiviso);
            tb_titolo.setTS_CONDIVISO(TableDao.now());
            tb_titolo.setUTE_CONDIVISO(user);

            tb_titolo = titoloCrea.creaTitoloAccessso(tb_titolo);
            if (ignoraID) {
                //Devo settare il vid automaticamente
                Progressivi progress = new Progressivi();
                tb_titolo.setBID(progress.getNextIdTitolo(tb_titolo.getTP_MATERIALE(),tb_titolo.getTP_RECORD_UNI()));
            } else
                tb_titolo.setBID(titAcc.getT001());
            titoloDB.inserisci(tb_titolo);

            titoloCrea.creaLegami(timeHash, tb_titolo, null);
            //Chiamo le creazioni aggiuntive, se non serve non vengono create.
            new TitoloGestisceLocalizza().localizzaTitolo(tb_titolo.getBID(), localizzaType,user_object);

            titoloCreato = titoloDB.estraiTitoloPerID(tb_titolo.getBID());

            object_response = formattaOutput();

        } else {
            //ritorna il messaggio di diagnostica
            elencoDiagnostico = validaTitolo.getElencoDiagnostico();
            settaIdLista();
            if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
                object_response = formattaLista(1);
            } else {
                //Qui non ci dovrebbe mai arrivare: utilizza le eccezioni.
                object_response = null;
            }
        }

    }

    /** Prepara la lista per il diagnostico, qualora esistano altri autori simili
     * E' copiata pari pari da Creatitolo: sarebbe il caso di farne una sola.
     */
    protected SBNMarc formattaLista(int numeroBlocco)
        throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        int last = numeroBlocco * maxRighe;
        if (last > elencoDiagnostico.size())
            last = elencoDiagnostico.size();
        List lista = elencoDiagnostico.subList((numeroBlocco - 1) * maxRighe, last);
        return FormatoTitolo.formattaLista(
            lista,
            sbnUser,
            SbnTipoOutput.valueOf("001"),
            null,
            id_lista,
            (numeroBlocco - 1) * maxRighe,
            maxRighe,
            elencoDiagnostico.size(),
            root_object.getSchemaVersion(),
            "3004",
            "Trovati titoli simili",false);
    }

    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiDocType datiResp = new DatiDocType();
        datiResp.setT001(titoloCreato.getBID());
        SbnDatavar data = new SbnDatavar(titoloCreato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAutDoc(titAcc.getLivelloAut());
        DocumentoType elementoAutResp = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(datiResp);
        elementoAutResp.setDocumentoTypeChoice(docChoice);
        output.addDocumento(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivita() {
    	// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
    	// serve solo il Cerca + Localizza per Gestione
    	if(_cattura){
    		return CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009;
    	} else {
    		return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	}
    	//  	return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	// Fine Intervento google3:
    }

}
