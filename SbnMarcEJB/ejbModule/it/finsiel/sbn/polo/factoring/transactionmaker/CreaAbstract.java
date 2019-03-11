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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Abstract;
import it.finsiel.sbn.polo.oggetti.estensione.AbstractValida;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A999;
import it.iccu.sbn.ejb.model.unimarcmodel.AbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
/**
 * Classe CreaAbstract
 * Factoring:
 * @author
 * @version
 */
public class CreaAbstract extends CreaElementoAutFactoring {

    private SbnUserType 		_sbnUser;
    private BigDecimal 			_schemaVersion;
    private SBNMarc 			_sbnmarcObj;
    private A999 				_t999;
    private String 				_t001;
	private String 				_t005;
    private Tb_abstract 		tb_abstractResponse = new Tb_abstract();
    private CreaTypeChoice 		datiTypeChoice;
    private String 				_cd_livello;
    private String 				_ds_abstact = null;
    private static Category 	log = Category.getInstance("iccu.sbnmarcserver.CreaAbstract");
	private List 			_vettoreElementiSimili = new ArrayList();
	private SbnSimile 			_tipoControllo;
    private AbstractType 				abstractType;
    private String  _condiviso;

    public CreaAbstract(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        datiTypeChoice = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
        _tipoControllo = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
        abstractType = (AbstractType) datiTypeChoice.getElementoAut().getDatiElementoAut();
        _cd_livello = abstractType.getLivelloAut().toString();
        abstractType.getStatoRecord();
        _t001 = abstractType.getT001();
        _t005 = abstractType.getT005();
        abstractType.getT100();
        _ds_abstact = abstractType.getT999().getA_999().toString();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (abstractType.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((abstractType.getCondiviso().getType() == DatiDocTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((abstractType.getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (abstractType.getCondiviso() != null && (abstractType.getCondiviso().equals(DatiDocTypeCondivisoType.S)) ) {
//        	_condiviso = abstractType.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }
    }

    /**
    * Esamina i parametri di ricerca ricevuti via xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    protected void executeCrea() throws IllegalArgumentException, InvocationTargetException, Exception {
        AbstractValida abstractValida = new AbstractValida();
        Abstract abs = new Abstract();
        abstractValida.validaPerCrea(_t001,_cd_livello, getCdUtente(),_cattura);
        abs.creaAbstract(_t001, _cd_livello, _ds_abstact, getCdUtente(),_condiviso);
        object_response = formattaOutput();
    }

    /**
     * metodo che compone il messaggio di risposta alla creazione di una classe
     * @throws InfrastructureException
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring, InfrastructureException {
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
		Tb_abstract abstractInserito = new Tb_abstract();
		Abstract abs = new Abstract();
		abstractInserito = abs.cercaAbstractPerId(_t001);
        DatiElementoType datiResp = new DatiElementoType();
        datiResp.setT001(abstractInserito.getBID());
        SbnDatavar data = new SbnDatavar(abstractInserito.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(abstractInserito.getCD_LIVELLO())));
        datiResp.setTipoAuthority(SbnAuthority.AB);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

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
