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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoClasse;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.estensione.ClasseValida;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;

/**
 * @author
 *
 */
public class ModificaClasse extends ModificaElementoAutFactoring{

	private ElementAutType 		_elementoAut;
	private ModificaType 			_modificaType;
	private ClasseType 		_datiElementoAut;
	private SbnUserType 			_sbnUser;
	private String 				_T001;
	private SbnLivello 			_livelloAut;
	private String 				_t005;
	private A676 					_t676;
	private A686 					_t686;
	private SbnIndicatore 			_fl_costruito;
	private Tb_classe				classe_response;
	private TimestampHash			_timeHash;

    private String  _condiviso;
    private String _ultTermine;



	public ModificaClasse(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_elementoAut =  _modificaType.getElementoAut();
		_datiElementoAut = (ClasseType)_elementoAut.getDatiElementoAut();
		_sbnUser = sbnmarcObj.getSbnUser();
		_T001 = _datiElementoAut.getT001();
		_livelloAut = _datiElementoAut.getLivelloAut();
		_t005 = _datiElementoAut.getT005();
		_t676 = _datiElementoAut.getClasseTypeChoice().getT676();
		_t686 = _datiElementoAut.getClasseTypeChoice().getT686();
		if (_t676 != null)
			_fl_costruito = _t676.getC9_676();
		else
			//almaviva5_20090402 non gestito per classe non dewey
			_fl_costruito = SbnIndicatore.N;

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElementoAut.getCondiviso() == null ){
        	_condiviso = "n";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

        //almaviva5_20090401 #2780
        _ultTermine = _datiElementoAut.getUltTermine();
	}

	protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

		_timeHash = new TimestampHash();
		ClasseValida classeValida = new ClasseValida(_datiElementoAut);
		Tb_classe tb_classe = classeValida.validaPerModifica(_t005,_livelloAut.toString(),_T001,getCdUtente(), _cattura,_fl_costruito.toString());
        classeValida.controllaVettoreParametriSemantici(_T001, this._sbnUser);

		String chiavePerTimeHash = null;
		chiavePerTimeHash = tb_classe.getCD_SISTEMA() +
							tb_classe.getCD_EDIZIONE() +
							tb_classe.getCLASSE().trim();
		_timeHash.putTimestamp("Tb_classe",chiavePerTimeHash,new SbnDatavar((tb_classe.getTS_VAR())));
		if (tb_classe != null){
			Classe classe = new Classe();
			classe.updateClasse(tb_classe,_livelloAut,getCdUtente(),_t676,_t686,_timeHash,_condiviso,_fl_costruito.toString(), _ultTermine);
			classe_response = classe.cercaClassePerID(_T001);
		}
		object_response = formattaOutput();
	}

	public SBNMarc formattaOutput() throws EccezioneFactoring, EccezioneDB, EccezioneSbnDiagnostico{
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
		FormatoClasse formatoClasse = new FormatoClasse();
		output.addElementoAut(formatoClasse.formattaClassePerCreazione(_T001,classe_response));
        output.setTotRighe(1);

        return sbnmarc;
	}

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().MODIFICA_CLASSE_1266;
    }


}
