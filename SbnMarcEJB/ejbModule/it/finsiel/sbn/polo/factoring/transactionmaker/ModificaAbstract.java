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
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Abstract;
import it.finsiel.sbn.polo.oggetti.estensione.AbstractValida;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A999;
import it.iccu.sbn.ejb.model.unimarcmodel.AbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;


/**
 * @author
 *
 */
public class ModificaAbstract extends ModificaElementoAutFactoring{

	private ElementAutType 	_elementoAut;
	private ModificaType 		_modificaType;
	private DatiElementoType 	_datiElementoAut;
	private StatoRecord 		_statoRecord;
	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private SbnSimile 			_tipoControllo;
	private String 			_t001;
	private String 			_t005;
	private A999 				_t999;
	private String 			_descrizione_abstract;
	private String 			_b931;
	private SbnFormaNome 		_tipoForma;
	private String 			_cd_livello;
	private LegamiType[] 		_legamiElementoAut;

	private LegamiType[] _arrivoLegame;

	private TimestampHash _timeHash;
    private String  _condiviso;

	public ModificaAbstract(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		DatiElementoType datiElemento = null;
		_sbnmarcObj = sbnmarcObj;
        _sbnUser = sbnmarcObj.getSbnUser();
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_elementoAut =  _modificaType.getElementoAut();
		_datiElementoAut = _elementoAut.getDatiElementoAut();
        _tipoControllo = _modificaType.getTipoControllo();
        datiElemento = _modificaType.getElementoAut().getDatiElementoAut();
        _t001 = datiElemento.getT001();
        AbstractType abstractType = new AbstractType();
        abstractType = (AbstractType) _modificaType.getElementoAut().getDatiElementoAut();
        _t005 = abstractType.getT005();
        _t999 = abstractType.getT999();
        if (_t999 != null){
        	_descrizione_abstract = _t999.getA_999();
        }
		_tipoForma = abstractType.getFormaNome();
        _cd_livello = abstractType.getLivelloAut().toString();
        _elementoAut = _modificaType.getElementoAut();
		_statoRecord = abstractType.getStatoRecord();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElemento.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiElemento.getCondiviso() != null && (datiElemento.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiElemento.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }
	}

    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

    	Tb_abstract tb_abstract = new Tb_abstract();
        AbstractValida abstractValida = new AbstractValida();
        _timeHash = new TimestampHash();
        tb_abstract = abstractValida.validaPerModifica(_t001,_cd_livello,_t005,getCdUtente(),_descrizione_abstract,_cattura);
		_timeHash.putTimestamp("Tb_abstract",_t001,new SbnDatavar( tb_abstract.getTS_VAR()));
      	Abstract abs = new Abstract();
        if (_statoRecord != null)  // almaviva INSERITO VERIFICARE PERCHE'
	        if (_statoRecord.getType() == StatoRecord.C_TYPE)
				if ((_descrizione_abstract != null))
		        	tb_abstract.setDS_ABSTRACT(_descrizione_abstract);
        abs.aggiornaAbstract(tb_abstract,getCdUtente());
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
		Tb_abstract abstractModificato = new Tb_abstract();
		Abstract abs = new Abstract();
		abstractModificato = abs.cercaAbstractPerId(_t001);
        DatiElementoType datiResp= new DatiElementoType();
        datiResp.setT001(abstractModificato.getBID());
        SbnDatavar data = new SbnDatavar(abstractModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(elementoAut.getDatiElementoAut().getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.DE);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
	}


}
