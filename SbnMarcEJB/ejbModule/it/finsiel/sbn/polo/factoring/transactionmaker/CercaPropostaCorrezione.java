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
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoProposta;
import it.finsiel.sbn.polo.oggetti.Proposta;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;

import java.lang.reflect.InvocationTargetException;
import java.util.List;




/**
 * Classe CercaPropostaCorrezione<BR>
 * <p>
 * Contiene le funzionalità di ricerca per l'entità Proposta di correzione
 * restituisce la lista sintetica o analitica.
 * Possibili canali di ricerca:
 * .Identificativo: idProposta
 * .tipoOggetto e idOggetto
 * .intervallo di data di inserimento: rangeDate
 * .mittenteProposta
 * .destinatarioProposta
 * i canali rangeDate, mittenteProposta, destinatarioProposta possono essere
 * comunicati insieme, la ricerca viene effettuata in AND sulle condizioni, almeno
 * un canale è obbligatorio
 * L'ordinamento è su Data di inserimento
 * L'output è solo analitico: comprensivo di tutte le informazioni di
 * ts_proposta_marc e ts_nota_proposta
 *
 * Tabelle coinvolte:
 * - Ts_Proposta_marc, Ts_note_proposta
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * se non ok ritorna il msg response di diagnostica
 */
public class CercaPropostaCorrezione extends CercaFactoring {

	private List 			_TableDao_response = null;
	private String 			_idProposta;
	private SbnOggetto[] 		_tipoOggetto;
	private String 			_idOggetto;
	private SbnRangeDate 		_rangeDate;
	private boolean 			ricercaPuntuale;
	private CercaPropostaType 	propostaType= new CercaPropostaType();
	private Proposta 			proposta;
	private SbnTipoOrd 		_ord;
	private SbnUserType 		_mittenteProposta;
	private SbnUserType 		_destinatarioProposta;

	public CercaPropostaCorrezione(SBNMarc input_root_object){
		super(input_root_object);
		CercaType cerca;
		cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_ord = cerca.getTipoOrd();

		propostaType =cerca.getCercaPropostaCorrezione();
		_idProposta = propostaType.getIdProposta();
		_tipoOggetto = propostaType.getTipoOggetto();
		_idOggetto = propostaType.getIdOggetto();
		_rangeDate = propostaType.getRangeDate();
		_mittenteProposta = propostaType.getMittenteProposta();
		_destinatarioProposta = propostaType.getDestinatarioProposta();
	}


	public void executeCerca()
	throws IllegalArgumentException, InvocationTargetException, Exception{
		if ((_ord.getType() != SbnTipoOrd.VALUE_0.getType()))
			throw new EccezioneSbnDiagnostico(1231,"errore nell'ordinamento");
		verificaAbilitazioni();
        int counter = 0;
        if (_idProposta  != null)
            counter++;
        if (_idOggetto != null || _tipoOggetto.length >0) {
            counter++;
            if (_idOggetto == null || _tipoOggetto.length == 0) {
                throw new EccezioneSbnDiagnostico(3285,"Comunicare id e tipo oggetto");
            }
        }
        //18.7.2005: la ricerca può essere fatta per data, mittente e destinatario
        if (_rangeDate != null )
            counter++;
        if (_mittenteProposta != null )
            counter++;
        if (_destinatarioProposta != null )
            counter++;
        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039,"comunicare uno e un solo canale di ricerca");
		proposta = new Proposta();
        proposta.valorizzaFiltri(propostaType);

        if (_idProposta != null)
            tavola_response = cercaPropostaPerId();
		else if (_tipoOggetto.length > 0)
			tavola_response = cercaPropostaPerOggetto();
		else if (_rangeDate != null)
			tavola_response = cercaPropostaPerData();
		else if (_mittenteProposta != null)
			tavola_response = cercaPropostaPerMittente();
        else if (_destinatarioProposta != null)
            _TableDao_response = cercaPropostaPerDestinatario();
	}

    private TableDao cercaPropostaPerId() throws IllegalArgumentException, InvocationTargetException, Exception {
        return proposta.cercaPropostaPerIdProposta(_idProposta,tipoOrd);
    }

    private TableDao cercaPropostaPerOggetto() throws IllegalArgumentException, InvocationTargetException, Exception {
        return proposta.cercaPropostaPerIdOggetto(tipoOrd);
    }

    private TableDao cercaPropostaPerData() throws IllegalArgumentException, InvocationTargetException, Exception {
        return proposta.cercaPropostaPerData(tipoOrd);
    }

    private TableDao cercaPropostaPerMittente() throws IllegalArgumentException, InvocationTargetException, Exception {
        return proposta.cercaPropostaPerMittente(tipoOrd);
    }

    private List cercaPropostaPerDestinatario() throws IllegalArgumentException, InvocationTargetException, Exception {
        return proposta.cercaPropostaPerDestinatario(""+_destinatarioProposta.getBiblioteca(), tipoOrd);
    }

    /**
	public String eseguiTransazione() throws EccezioneFactoring {
        try {
            executeCerca();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            if (tavola_response != null) {
                try {

                } catch (EccezioneDB ex) {
                    log.error("Errore chiusura statement",ex);
                }
                tavola_response = null;
            }
            object_response =
                new FormatoErrore().preparaMessaggioErrore(ecc, _sbnUser, schemaVersion);
            throw new EccezioneFactoring(56, "Errore di factoring, si necessita il rollback", ecc);
        }
        return getResult();
    }
    */

    /**
     * Metodo utilizzato per preparare gli output alle fasi successive
     * /
    public void proseguiTransazione() {
        if (tavola_response == null) {
            return;
        }
        try {
            while (rowCounter < numeroRecord) {
                blockCounter++;
                String nomeBlocco = _idLista +"_" + blockCounter;
                String stringaBlocco = preparaOutput();
                log.info("Scrivo in LDAP con nome file:"+nomeBlocco);
                long tempo = System.currentTimeMillis();
                IndiceLDAPObjectLocator.bind(nomeBlocco, stringaBlocco);
                tempo -= System.currentTimeMillis();
                log.info("Accesso ad LDAP; tempo impiegato in millisecondi :"+tempo);
            }
        } catch (ServiceLocatorException e) {
            log.error("Problemi in scrittura del file di recupero");
            e.printStackTrace();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
        }
    }



    /** Prepara la stringa di output in formato xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    protected SBNMarc preparaOutput() throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        if (_TableDao_response ==null ) {
            if (tavola_response == null) {
                log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
                throw new EccezioneSbnDiagnostico(201);
            }
            _TableDao_response = tavola_response.getElencoRisultati(maxRighe);
        }
        if (_TableDao_response.size() == 0)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        SBNMarc  out = formattaOutput(_TableDao_response);
        rowCounter += maxRighe;
        return out;
    }

	public SBNMarc formattaOutput(List TableDao_response) throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception{
		SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000");
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        int size=1;
        size = TableDao_response.size();
		FormatoProposta formatoProposta = new FormatoProposta();
		Ts_proposta_marc note_proposta;
		for (int i=0;i<size;i++){
			note_proposta = new Ts_proposta_marc();
			note_proposta = (Ts_proposta_marc) TableDao_response.get(i);
            output.addPropostaCorrezione(formatoProposta.formattaProposta(note_proposta,tipoOut));

		}
        output.setMaxRighe(size);
        output.setNumPrimo(rowCounter+1);
        output.setTotRighe(size);
        return sbnmarc;
	}


   	public String getIdAttivita() {
		return CodiciAttivita.getIstance().CERCA_PROPOSTE_DI_CORREZIONE_1004;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_PROPOSTE_DI_CORREZIONE_1004;
    }

}
