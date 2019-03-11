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
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoSbnUser;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerObject;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.util.List;


/**
 * @author
 * L'input è cercaSbnProfile con un codice utente
 * Il factoring deve semplicemente chiamare il validator
 * public ValidatorContainerObject getAbilitazioni(String codice)
 * con il codice utente di compilare SbnUserProfile.
 */
public class CercaSbnProfile extends Factoring{

	private SbnUserType 	sbnUser;
	private String 		biblioteca;
	private TableDao 		_TableDao_response;
	private String 		bibliotecaRequest;
	private SbnUserType 	sbnUserType;
	private CercaType 		cercaType;

	private SbnTipoOrd 	tipoOrd;

	private SbnTipoOutput 	tipoOut;
	private List			_elencoAttivita;
	private List			_elencoParametri;
	private List			_elencoParametriAuthority;
	private List			_elencoParametriMateriale;

	private ValidatorProfiler validator;




	public CercaSbnProfile(SBNMarc sbnmarcObj){
        // Assegno radice e classi XML principali
        super(sbnmarcObj);

		sbnUser = sbnmarcObj.getSbnUser();
		biblioteca = sbnUser.getBiblioteca();
		cercaType = sbnmarcObj.getSbnMessage().getSbnRequest().getCerca();
		sbnUserType = cercaType.getCercaSbnProfile();
		bibliotecaRequest = sbnUserType.getBiblioteca();
		tipoOrd = cercaType.getTipoOrd();
		tipoOut = cercaType.getTipoOutput();
	}


	private void executeCerca() throws EccezioneSbnDiagnostico, EccezioneFactoring, EccezioneDB{
//		try {
		validator = ValidatorProfiler.getInstance();
		String user_profile =
			sbnUserType.getBiblioteca()	+ (sbnUserType.getUserId() == null ?
				"      " :
				sbnUserType.getUserId());

			ValidatorContainerObject  curr_user =  validator.getAbilitazioni(user_profile);
			//Tb_utente dati_utente = new Tb_utente();
			if (curr_user != null){
//				dati_utente = (Tb_utente) curr_user.getTb_utente() ;
				_elencoAttivita = curr_user.getElenco_attivita();

				if(ValidazioneDati.isFilled(_elencoAttivita) ) {
					_elencoParametri = curr_user.getElenco_parametri();
					_elencoParametriAuthority = curr_user.getElenco_parametri_authority();
					_elencoParametriMateriale = curr_user.getElenco_parametri_materiale();
				}else{
						throw new EccezioneSbnDiagnostico(3761,"Utente non profilato");
				}
			} else
					throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
			object_response = formattaOutput();
//		} catch (Exception eccezione_validator)
//		{
//			eccezione_validator.printStackTrace() ;
//			log.debug("Errore: "+eccezione_validator.getMessage());
//		}
	}


   public void eseguiTransazione()  {
        try {
            executeCerca();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            SBNMarc sbnmarc = new SBNMarc();
            SbnMessageType message = new SbnMessageType();
            SbnResponseType response = new SbnResponseType();
            SbnResultType result = new SbnResultType();
            SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
            SbnOutputType output = new SbnOutputType();
            String errore = "" + ecc.getErrorID();
            result.setEsito(errore);
            //result.setEsito("3001");
            //result.setTestoEsito("elemento non trovato");
            result.setTestoEsito(ecc.getMessaggio());
            sbnmarc.setSbnMessage(message);
            sbnmarc.setSbnUser(sbnUser);
            sbnmarc.setSchemaVersion(schemaVersion);
            message.setSbnResponse(response);
            response.setSbnResult(result);
			response.setSbnResponseTypeChoice(responseChoice);

			object_response = sbnmarc;
        }
    }


	public SBNMarc formattaOutput() throws  EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico{
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
		result.setEsito("0000");
		result.setTestoEsito("OK");

        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);

		FormatoSbnUser formatoUser = new FormatoSbnUser();
		SbnProfileType profile = formatoUser.formattaSbnProfile(
									validator,
									getCdUtente(),
									sbnUserType,
									_elencoAttivita,
									_elencoParametri,
									_elencoParametriMateriale,
									_elencoParametriAuthority);
		responseChoice.setSbnUserProfile(profile);
        response.setSbnResponseTypeChoice(responseChoice);

        return sbnmarc;
	}

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().CERCA_PROPOSTE_DI_CORREZIONE_1004;
    }

}
