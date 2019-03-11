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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoProposta;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.oggetti.Proposta;
import it.finsiel.sbn.polo.oggetti.estensione.PropostaValida;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;

import org.exolab.castor.types.Date;


/**
 * Classe ModificaPropostaCorrezione
 * Factoring:
 * Modifica una proposta di correzione come richiesto dal messaggio xml, dopo aver
 * controllato la validità.
 * E' possibile la modifica di statoProposta, testoProposta, noteProposta. E'
 * gestito l'incremento di destinatarioProposta (cioè è possibile inserire nuovi
 * destinatari ma non cancellare destinatari esistenti).
 * Tabelle coinvolte:
 * - Ts_Proposta_marc; Ts_note_proposta
 *
 * Passi da eseguire:
 * Valida la richiesta:
 * è obbligatorio idProposta e dataVarProposta (time-stamp).
 * se è compilato destinatarioProposta si verifica l'esistenza di ts_note_proposta
 * con ute_destinatario=destinatarioPorposta: se non c'è si crea il record
 *
 * Si verifica che il client sia il mittente, oppure uno dei destinatari.
 * . testoProposta può essere modificato solo dal mittente, e solo se lo stato
 * proposta è 'I' (inserita)
 * se il client è uno dei destinatari (esiste un record ts_nota_proposta con
 * ute_destinatario=SbnUser) si modifica lo stato della proposta e note_pro del
 * ts_note_proposta corrispondente'
 * se è compilato destinatarioProposta si verifica l'esistenza di ts_note_proposta
 * con ute_destinatario=destinatarioPorposta: se non c'è si crea il record
 * si aggiorna ts_var e ute_var.
 *
 * Se ok modifica ila proposta nel database; prepara
 * il msg di response di output che corrisponde all'esame analitico completo della
 * proposta
 * @author
 * @version
 */
public class ModificaPropostaCorrezione extends ModificaFactoring{
	private SBNMarc 						_sbnmarcObj;
	private SbnUserType			 		_sbnUser;
	private Ts_proposta_marc			 	propostaModificata;
	private String 						_testoProposta = null;
	private int 							_idProposta=0;
	private Date 							_dataVarProposta;
	private DestinatarioPropostaType[] 	_destinatari;
	private SbnStatoProposta 				_statoProposta;
	private String 						_mittenteProposta;
	private PropostaType 					_propostaType = new PropostaType();
	private TimestampHash					_timeHash = new TimestampHash();

   public ModificaPropostaCorrezione(SBNMarc input_root_object){
   		super(input_root_object);
		_sbnmarcObj = input_root_object;
        _sbnUser = _sbnmarcObj.getSbnUser();
    	_propostaType =_sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getPropostaCorrezione();
		_testoProposta = _propostaType.getTestoProposta();
		_statoProposta = _propostaType.getStatoProposta();
		_idProposta = _propostaType.getIdProposta();
		_dataVarProposta = _propostaType.getDataInserimento();
		_destinatari = _propostaType.getDestinatarioProposta();
       if (_propostaType.getMittenteProposta() != null) {
           if (_propostaType.getMittenteProposta().getUserId()!=null) {
               _mittenteProposta=_propostaType.getMittenteProposta().getBiblioteca()+_propostaType.getMittenteProposta().getUserId();
           } else {
               _mittenteProposta=_propostaType.getMittenteProposta().getBiblioteca()+"      ";
           }
       } else
           _mittenteProposta = getCdUtente();
   }

    protected void executeModifica() throws
    IllegalArgumentException, InvocationTargetException, Exception {
    	if (_idProposta == 0)
    		throw new EccezioneSbnDiagnostico(3201,"manca idProposta o dataVarProposta");
    	PropostaValida propostaValida = new PropostaValida();
        propostaModificata = propostaValida.validaPerModifica(_propostaType,getCdUtente(), _timeHash);
    	Proposta propostaDiCorrezione = new Proposta();
		if (_destinatari != null){

			if (propostaDiCorrezione.controllaDestinatari(getCdUtente().substring(0,6),_propostaType)){
				for (int i=0;i<_destinatari.length;i++) {
                    String dest = _destinatari[i].getDestinatarioProposta().getBiblioteca();
                    if (_destinatari[i].getDestinatarioProposta().getUserId()!=null) {
                        dest += _destinatari[i].getDestinatarioProposta().getUserId();
                    } else {
                        dest += "      ";
                    }
					propostaDiCorrezione.aggiornaNoteProposta(	getCdUtente(),
																_idProposta,
																dest,
																_destinatari[i].getNoteProposta(),
																_timeHash);
                }
			}
            //[TODO controllare se ci vuole un else a questo punto
			propostaDiCorrezione.verificaEsistenzaDestinatario(_idProposta,getCdUtente(),_destinatari);
		}
        propostaModificata.setUTE_VAR(getCdUtente());
        if (_testoProposta != null)
            propostaModificata.setDS_PROPOSTA(_testoProposta);
        if (_statoProposta != null)
            propostaModificata.setCD_STATO(_statoProposta.toString());
        propostaDiCorrezione.updateProposta(propostaModificata);
        object_response = formattaOutput();
    }

    private SBNMarc formattaOutput() throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
		SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000");
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(_sbnmarcObj.getSchemaVersion());
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        ElementAutType elemento[] = new ElementAutType[1];
		FormatoProposta formatoProposta = new FormatoProposta();
		output.addPropostaCorrezione(formatoProposta.formattaProposta(propostaModificata, SbnTipoOutput.VALUE_1));
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
	}

//da controllare
   	public String getIdAttivita() {
		return CodiciAttivita.getIstance().MODIFICA_1019;
	}

}
