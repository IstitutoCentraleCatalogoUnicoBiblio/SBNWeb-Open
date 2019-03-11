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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A930;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.RepertorioType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRepertorio;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 * Classe CreaClasse
 * Factoring:
 * Crea una classe come richiesto dal messaggio xml, dopo aver controllato la
 * validità.
 * T001 non viene considerato in input
 *
 * Se riceve in input T676:
 * il sistema di classificazione è impostato automaticamente a 'D'
 * (tb_classe.cd_sistema)
 * in OUTPUT T001 (SbnID) è composto da 'D'+v_676+a_676
 *
 * Se riceve in input T686:
 * il sistema di classificazione è c2_686 e deve esistere in "tb_codici"
 * l'edizione non è gestita, viene gestita in tb_classe con il valore
 * di default spazio
 * in OUTPUT T001 (SbnID) è composto da c2_686+a_686)
 *
 * Tabelle coinvolte:
 * - Tb_classe
 * OggettiCoinvolti:
 * - Classe
 * Passi da eseguire:
 * Valida la classe.
 * se non ok ritorna il msg response di diagnostica
 * Se ok inserisce la classe nel database; prepara
 * il msg di response di output
 * @author
 * @version
 */
/*
 <SbnRequest>
		<Crea tipoControllo="Conferma" Cattura="true">
			<ElementoAut>
				<DatiElementoAut xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" tipoAuthority="RE" livelloAut="97" statoRecord="c" xsi:type="RepertorioType">
					<T001>A</T001>
					<T930>
						<a_930>Ascarelli, F.  La tipografia cinquecentina italiana. Firenze, Sansoni Antiquariato, 1953.</a_930>
						<b_930>M</b_930>
						<c2_930>A</c2_930>
					</T930>
				</DatiElementoAut>
			</ElementoAut>
		</Crea>
		</SbnRequest>

 */
public class CreaRepertorio extends CreaElementoAutFactoring {

    private SbnUserType 	_sbnUser;
    private BigDecimal 		_schemaVersion;
    private String 			_t001;
    private A930 			_t930;
    private SBNMarc 		_sbnmarcObj;
    private CreaTypeChoice 	datiTypeChoice;
    private String 			_cd_livello;
    private String 			_descrizione = null;
    private TipoRepertorio 	_tipo = null;
    private String 			_sigla = null;
    private static Logger 	log = Logger.getLogger("iccu.sbnmarcserver.CreaRepertorio");
	private List 			_vettoreElementiSimili = new ArrayList();
	private SbnSimile 			_tipoControllo;
    private Tb_repertorio 		tb_repertotioResponse = new Tb_repertorio();
    private String ute_var = null;
    private RepertorioType repertorioType;
    private String  _condiviso;

    public CreaRepertorio(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
       	//almaviva5_20090722 #3086
        ute_var = _sbnUser.getBiblioteca() + _sbnUser.getUserId();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        CreaType crea = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea();
		datiTypeChoice = crea.getCreaTypeChoice();
        _tipoControllo = crea.getTipoControllo();
        repertorioType = (RepertorioType) datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t930 = repertorioType.getT930();
        _descrizione = _t930.getA_930();
        _tipo = _t930.getB_930();
        _sigla = _t930.getC2_930();
        _cd_livello = repertorioType.getLivelloAut().toString();
        _t001 = repertorioType.getT001();

	      // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
	      if (repertorioType.getCondiviso() == null ){
	      	_condiviso = "n";
	      }
	      else if ((repertorioType.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
	      	_condiviso = "s";
	      }
	      else if ((repertorioType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
	      	_condiviso = "n";
	      }
    }
	public Tb_repertorio validaPerCrea(String id,boolean _cattura) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		Repertorio repertorio = new Repertorio();
		Tb_repertorio tb_repertorio = null;
		try {
			//almaviva5_20090722 #3086
			tb_repertorio = repertorio.cercaRepertorioPerCdSigAncheCancellato(id);
		} catch (IllegalArgumentException e) {
			log.error("", e);
		} catch (InvocationTargetException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}
   		return tb_repertorio;
	}
	public boolean valida() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		if( (_t930 == null) || (_descrizione == null) || (_tipo == null) || (_sigla == null))
			return false;
		else
			return true;
	}
    /**
    * Esamina i parametri di ricerca ricevuti via xml
     * @throws InfrastructureException
    */
    protected void executeCrea() throws EccezioneDB,
    	EccezioneSbnDiagnostico, EccezioneFactoring, InfrastructureException {
    	if(!valida()){
    		 throw new EccezioneSbnDiagnostico(4301, "Repertori: Descrizione tipo o sigla mancante");
    	}
    	Tb_repertorio tb_repertorio = validaPerCrea(_t001,_cattura);

		Repertorio repertorio = new Repertorio();
   		if (tb_repertorio == null){
   					Tb_repertorio tb_rep = new Tb_repertorio();
   					tb_rep.setCD_SIG_REPERTORIO(_sigla);
   					tb_rep.setDS_REPERTORIO(_descrizione);
   					tb_rep.setFL_CANC("N");
   					tb_rep.setFL_CONDIVISO(_condiviso);
   					tb_rep.setTP_REPERTORIO(_tipo.toString());
   					tb_rep.setUTE_VAR(ute_var);
   					tb_rep.setUTE_CONDIVISO(ute_var);
   					tb_rep.setUTE_INS(ute_var);
					repertorio.eseguiInsert(tb_rep);
   			} else {
   					tb_repertorio.setCD_SIG_REPERTORIO(_sigla);
   					tb_repertorio.setDS_REPERTORIO(_descrizione);
   					tb_repertorio.setTP_REPERTORIO(_tipo.toString());
   					tb_repertorio.setUTE_VAR(ute_var);
   					//almaviva5_20090722 #3086
   					tb_repertorio.setFL_CANC("N");
					repertorio.eseguiUpdate(tb_repertorio);
   			}
        object_response = formattaOutput();
   		}


    /** Prepara la stringa di output in formato xml
     * @throws InfrastructureException */
//    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico, InfrastructureException {
//        SBNMarc risultato = formattaOutput();
//        rowCounter += maxRighe;
//        return risultato;
//    }

//    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneSbnDiagnostico {
//        SBNMarc sbnmarc = new SBNMarc();
//        SbnMessageType message = new SbnMessageType();
//        SbnResponseType response = new SbnResponseType();
//        SbnResultType result = new SbnResultType();
//        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
//        SbnOutputType output = new SbnOutputType();
//        sbnmarc.setSbnMessage(message);
//        SbnUserType user = new SbnUserType();
//        sbnmarc.setSbnUser(user_object);
//        sbnmarc.setSchemaVersion(schemaVersion);
//        message.setSbnResponse(response);
//        response.setSbnResult(result);
//        response.setSbnResponseTypeChoice(responseChoice);
//        responseChoice.setSbnOutput(output);
//        Tb_repertorio tb_repertorio;
//        int totRighe = 0;
//        if (_TableDao_response != null)
//            totRighe = _TableDao_response.size();
//        FormatoRepertorio formatoRepertorio = new FormatoRepertorio();
//        if ((totRighe > 0) || ricercaPuntuale) {
//            output.setMaxRighe(maxRighe);
//            output.setTotRighe(totRighe);
//            output.setNumPrimo(rowCounter+1);
//            output.setTipoOrd(factoring_object.getTipoOrd());
//            output.setTipoOutput(tipoOut);
//            ElementAutType elemento;
//            if (!ricercaPuntuale) {
//                for (int i = 0; i < totRighe; i++) {
//                    elemento = new ElementAutType();
//                    tb_repertorio = (Tb_repertorio) _TableDao_response.get(i);
//                    elemento.setDatiElementoAut(formatoRepertorio.formattaRepertorioPerEsame(tb_repertorio));
//                    SbnOutputTypeChoiceItem item = new SbnOutputTypeChoiceItem();
//                    SbnOutputTypeChoice choice = new SbnOutputTypeChoice();
//                    choice.setSbnOutputTypeChoiceItem(item);
//                    output.addSbnOutputTypeChoice(choice);
//                    item.setElementoAut(elemento);
//                }
//            } else {
//                totRighe = 1;
//                elemento = new ElementAutType();
//                elemento.setDatiElementoAut(formatoRepertorio.formattaRepertorioPerEsame(_tb_repertorio));
//                SbnOutputTypeChoiceItem item = new SbnOutputTypeChoiceItem();
//                SbnOutputTypeChoice choice = new SbnOutputTypeChoice();
//                choice.setSbnOutputTypeChoiceItem(item);
//                output.addSbnOutputTypeChoice(choice);
//                item.setElementoAut(elemento);
//            }
//            result.setEsito("0000"); //Esito positivo
//            result.setTestoEsito("OK");
//        } else {
//            result.setEsito("3001");
//            //Esito non positivo: si potrebbe usare un'ecc.
//            result.setTestoEsito("Nessun elemento trovato");
//        }
//        output.setTotRighe(totRighe);
//        if (idLista != null)
//            output.setIdLista(idLista);
//        return sbnmarc;
//
//    }
    private  SBNMarc  formattaOutput() throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring, InfrastructureException {
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

        return sbnmarc;
    }
   	public String getIdAttivita(){
		return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CREA_CLASSE_1259;
    }
    protected SBNMarc formattaLista(int numeroBlocco)
    throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        return null;
    }

}
