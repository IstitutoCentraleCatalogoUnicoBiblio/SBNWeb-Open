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

import it.finsiel.sbn.exception.InfrastructureException;
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
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeMusicaCrea;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeMusicaValida;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe CreaTitoloUniforme.java
 * <p>
 * Crea un oggetto di tipo titolo uniforme, inserendo nella tabella tb_titolo il relativo
 * record, in più crea i legami così come richiesto da file xml in input.
 * </p>
 *
 * @author
 * @author
 *
 * @version 3-mar-2003
 */
public class CreaTitoloUniformeMusica extends CreaElementoAutFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CreaTitoloUniforme");

    // Attributi da XML-input
    private ElementAutType elAut;

    private DatiElementoType datiEl;
    private TitoloUniformeMusicaType titUni;

    private SbnLivello livelloAut;

    //entità collegata: ArrivoLegame
    private LegamiType[] arrivoLegame = null;

    private SbnSimile tipoControllo = null;
    //Utente che esegue la richiesta
    private SbnUserType sbnUser = null;

    private String id_lista = null;
    private String  _condiviso;

    private final static int maxRighe =
        Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
    //Autore creato dal factoring
    Tb_titolo titoloCreato;
    LocalizzaType localizzaType = null;

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
    public CreaTitoloUniformeMusica(SBNMarc input_root_object) {
        super(input_root_object);
        //do per scontato che ci siano tutti. Le verifiche sono fatte da chi
        //chiama il factoring
        elAut = factoring_object.getCreaTypeChoice().getElementoAut();
        datiEl = elAut.getDatiElementoAut();
        if (datiEl instanceof TitoloUniformeMusicaType) {
            titUni = (TitoloUniformeMusicaType) datiEl;
        }
        arrivoLegame = elAut.getLegamiElementoAut();
        tipoControllo = factoring_object.getTipoControllo();
        sbnUser = input_root_object.getSbnUser();
        localizzaType = input_root_object.getSbnMessage().getSbnRequest().getCrea().getLocalizza();


        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiEl.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiEl.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiEl.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiEl.getCondiviso() != null && (datiEl.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiEl.getCondiviso().toString();
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
     * @throws InfrastructureException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeCrea()
        throws IllegalArgumentException, InvocationTargetException, InfrastructureException, Exception {
        //tipoForma=null;
        //controllo la validità del luogo
        TitoloUniformeMusicaValida validaTitolo = new TitoloUniformeMusicaValida(factoring_object);
        boolean ignoraID = datiEl.getT001().equals("0000000000");
        TimestampHash timeHash = new TimestampHash();
        Tb_titolo tb_titolo = new Tb_titolo();
        if (validaTitolo.validaPerCrea(getCdUtente(), tb_titolo, ignoraID, timeHash,_cattura)) {
            if (tipoControllo.getType() == SbnSimile.SIMILEIMPORT_TYPE)
                throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
            String user = getCdUtente();
            Titolo titoloDB = new Titolo();
            //controllo se esistono informazioni ArrivoLegami
            String cd_livello = datiEl.getLivelloAut().toString();
            TitoloUniformeMusicaCrea titoloCrea = new TitoloUniformeMusicaCrea(factoring_object);
            tb_titolo = titoloCrea.creaDocumento(tb_titolo);
            if (ignoraID) {
                //Devo settare il vid automaticamente
                Progressivi progress = new Progressivi();
                // LA FUNZIONE VIENE SOSTITUITA CON getNextIdTitolo passando valori fissi
                //tb_titolo.setBID(progress.getNextIdTitoloCMP());
                tb_titolo.setBID(progress.getNextIdTitolo("U",""));
            } else
                tb_titolo.setBID(datiEl.getT001());
            if (tipoControllo.getType() == SbnSimile.CONFERMA_TYPE && !scheduled) {
                tb_titolo.setUTE_FORZA_INS(user);
                tb_titolo.setUTE_FORZA_VAR(user);
            }
            tb_titolo.setUTE_INS(user);
            tb_titolo.setUTE_VAR(user);
            tb_titolo.setBID_LINK(""); //??

            //il cd livello potrebbe forse essere spostato.
            tb_titolo.setCD_LIVELLO(cd_livello);

            tb_titolo.setFL_SPECIALE(" "); //?
            tb_titolo.setFL_CANC("N");
            // Timbro Condivisione
            tb_titolo.setFL_CONDIVISO(_condiviso);
            tb_titolo.setTS_CONDIVISO(TableDao.now());
            tb_titolo.setUTE_CONDIVISO(user);

            titoloDB.inserisci(tb_titolo);

            titoloCrea.creaComposizione(tb_titolo);
            titoloCrea.creaLegami(null, tb_titolo, null);

            new TitoloGestisceLocalizza().localizzaTitolo(tb_titolo.getBID(), localizzaType,user_object);

            TableDao tavola = titoloDB.cercaTitoloPerID(tb_titolo.getBID());

            titoloCreato = (Tb_titolo) tavola.getElencoRisultati().get(0);

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
        TitoloUniformeMusicaType datiResp = new TitoloUniformeMusicaType();
        datiResp.setT001(titoloCreato.getBID());
        SbnDatavar data = new SbnDatavar(titoloCreato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        A230 a230 = new A230();
        a230.setA_230(titoloCreato.getISBD());
        datiResp.setT230(a230);
        datiResp.setLivelloAut(datiEl.getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.UM);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    public String getIdAttivita() {
		if (_cattura) {// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA
						// ABILITATO A CREARE UN DOCUMENTO
			return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
		} else {
			return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
		}
	}

	public String getIdAttivitaSt() {
		if (_cattura) {// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA
						// ABILITATO A CREARE UN DOCUMENTO
			return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
		} else {
			return CodiciAttivita.getIstance().CREA_TITOLO_UNIFORME_MUSICA_1255;
		}
	}

}
