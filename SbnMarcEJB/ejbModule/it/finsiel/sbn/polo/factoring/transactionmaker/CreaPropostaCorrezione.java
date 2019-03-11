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
import it.finsiel.sbn.polo.factoring.base.FormatoProposta;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Proposta;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.OggettoServerSbnMarc;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * Classe CreaPropostaCorrezione
 * Factoring:
 * Crea una proposta di correzione come richiesto dal messaggio xml, dopo aver
 * controllato la
 * validità.
 * Tabelle coinvolte:
 * - Ts_proposta_marc; Ts_note_proposta
 *
 * Passi da eseguire:
 * Valida la proposta di correzione:
 * sono obbligatori tipoOggetto, idOggetto, testoProposta
 * idOggetto deve esistere nel db
 * destinatarioProposta può avere molteplicità superiore a 1
 *
 * L'identificativo proposta (idProposta) viene assegnato dall'Indice. Nel
 * messaggio di creazione inviato dal client deve essere impostato a zeri.
 * Lo stato della proposta è controllato con un simpleType di xml-schema. Viene
 * assegnato il valore 'inserita' (codificato con tb_codici)
 * Se mittenteProposta non è presente si assume uguale a SbnUser che ha inviato il
 * messaggio
 * Se destinatarioProposta non è presente l'Indice assegna l'informazione secondo
 * un algoritmo da definire secondo il tipo di oggetto. (es. utenti delle
 * biblioteche che gestiscono l'oggetto, utenti con livello di authority sul tipo
 * oggetto).
 *
 * se non ok  ritorna il msg response di diagnostica
 * Se ok inserisce la proposta nel database; inserisce un record ts_note_proposta
 * per ogni destinatario individuato; prepara il msg di response di output che
 * corrisponde all'esame analitico completo della proposta
 *
 * @author
 * @version
 */
public class CreaPropostaCorrezione extends CreaFactoring {

	private SBNMarc _sbnmarcObj;
	private SbnUserType _sbnUser;
	private SbnOggetto _tipoOggetto = null;
	private String _idOggetto = null;
	private String _testoProposta = null;
	private SbnAuthority _tipoAuthority;
	private SbnMateriale _tipoMateriale;
	private String _mittenteProposta;
	private SbnSimile _tipoControllo;
	private Ts_proposta_marc _propostaInserita;
	private SbnStatoProposta _statoProposta;
	// da sistemare!!!!!!!!
	private String _tp_messaggio;
	private DestinatarioPropostaType[] _destinatari;

	public CreaPropostaCorrezione(SBNMarc input_root_object){
   		super(input_root_object);
  		_sbnmarcObj = input_root_object;
        _sbnUser = _sbnmarcObj.getSbnUser();
		PropostaType propostaType= new PropostaType();
		propostaType =_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getPropostaCorrezione();
		_tipoControllo =_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
		_tipoOggetto = propostaType.getTipoOggetto();
		_idOggetto = propostaType.getIdOggetto();
		_testoProposta = propostaType.getTestoProposta();
		if (_tipoOggetto != null){
			_tipoAuthority = _tipoOggetto.getTipoAuthority();
			_tipoMateriale = _tipoOggetto.getTipoMateriale();
		}
		if (propostaType.getMittenteProposta() != null) {
            if (propostaType.getMittenteProposta().getUserId()!=null) {
                _mittenteProposta=propostaType.getMittenteProposta().getBiblioteca()+propostaType.getMittenteProposta().getUserId();
            } else {
                _mittenteProposta=propostaType.getMittenteProposta().getBiblioteca()+"      ";
            }
        } else
			_mittenteProposta = getCdUtente();
		_statoProposta = propostaType.getStatoProposta();
		_destinatari = propostaType.getDestinatarioProposta();
	}


    protected void executeCrea() throws
    IllegalArgumentException, InvocationTargetException, Exception {
    	String ute_var = validaProposta();
		Proposta proposta = new Proposta();
		String tipoOggetto;
		if (_tipoAuthority != null)
			tipoOggetto = _tipoAuthority.toString();
		else
			tipoOggetto = _tipoMateriale.toString();
		_propostaInserita = proposta.inserisciProposta(
								_idOggetto,
								getCdUtente(),
								_mittenteProposta,
								_testoProposta,
								_statoProposta.toString(),
								_tp_messaggio,
								tipoOggetto);

	//viene inserito un record anche in ts_note_proposta
	//controllo che sia presente un destinatario, in caso contrario inserisco nella nota il
	//l'utente che ha effettuato la creazione  come destinatario
		if (_destinatari != null)
	    	for (int i=0; i<_destinatari.length; i++){
	    		proposta.inserisciNota((int)_propostaInserita.getID_PROPOSTA(),
	    							_destinatari[i],
	    							getCdUtente());
	    	}
		else
			proposta.inserisciNota((int)_propostaInserita.getID_PROPOSTA(),ute_var);
    	object_response = formattaOutput();

    }

	/**
	 * Method validaProposta.
	 * @return boolean
	 * @throws InfrastructureException
	 */
	private String validaProposta() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		if ((_idOggetto != null) &&
		(_testoProposta != null) &&
		(_tipoOggetto != null)){
			TableDao tavola = null;
			if (_tipoAuthority != null) {
				if (_tipoAuthority.getType() == SbnAuthority.AU_TYPE){
					Autore autore = new Autore();
					tavola = autore.cercaAutorePerID(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.CL_TYPE){
					Classe classe = new Classe();
					tavola = classe.cercaClassePerIDInTavola(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.DE_TYPE){
					Descrittore descrittore = new Descrittore();
					tavola = descrittore.cercaDescrittorePerIdInTavola(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.LU_TYPE){
					Luogo luogo = new Luogo();
					tavola = luogo.cercaLuogoPerIDInTavola(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.MA_TYPE){
					Marca marca = new Marca();
					tavola = marca.cercaMarcaPerID(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.RE_TYPE){
					Repertorio repertorio = new Repertorio();
					tavola = repertorio.cercaRepertorioPerID(_idOggetto);
				} else if (_tipoAuthority.getType() == SbnAuthority.SO_TYPE){
					Soggetto soggetto = new Soggetto();
					tavola = soggetto.cercaSoggettoPerIdInTavola(_idOggetto);
				} else {
					Titolo titolo = new Titolo();
					tavola = titolo.cercaTitoloPerID(_idOggetto);
				}
			} else {
				Titolo titolo = new Titolo();
				tavola = titolo.cercaTitoloPerID(_idOggetto);
			}
            List v = tavola.getElencoRisultati();

			if (v.size() == 0) {
				throw new EccezioneSbnDiagnostico(3001,"oggetto non trovato nel db");
            }
            OggettoServerSbnMarc ogg = (OggettoServerSbnMarc)v.get(0);
            String ute_var = null;
            try {
            	 Tb_titolo tc = (Tb_titolo) v.get(0);
            	ute_var = tc.getUTE_VAR();
            	// SBAGLIATO DEVE PASSARE UTE_VAR MA BISOGNA MODIFICARE LA FUNZIONE PER PRENDERLO
                //ute_var = ogg.getField("UTE_VAR");
            } catch (Exception e) {
                log.error("Errore reflection in validaProposta (getUte_var)",e);
            }
			return ute_var;
		}
		else throw new EccezioneSbnDiagnostico(3200,"manca un dato obbligatorio");
	}

    private SBNMarc formattaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
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
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        PropostaType elemento[] = new PropostaType[1];
		FormatoProposta formatoProposta = new FormatoProposta();
		elemento[0] = formatoProposta.formattaProposta(_propostaInserita,SbnTipoOutput.VALUE_1);
		output.setPropostaCorrezione(elemento);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
	}


   	public String getIdAttivita() {
		return CodiciAttivita.getIstance().CREA_PROPOSTA_CORREZIONE_1018;
	}
    protected SBNMarc formattaLista(int numeroBlocco)
    throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        return null;
    }

}
