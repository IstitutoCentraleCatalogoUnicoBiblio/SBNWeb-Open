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
import it.finsiel.sbn.polo.factoring.base.FormatoClasse;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.estensione.ClasseValida;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

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
public class CreaClasse extends CreaElementoAutFactoring {

	private SbnUserType _sbnUser;
	private BigDecimal _schemaVersion;
	private A676 _t676;
	private A686 _t686;
	private String _t001;
	private SBNMarc _sbnmarcObj;
	private Tb_classe tb_classeResponse = new Tb_classe();
	private CreaTypeChoice datiTypeChoice;
	private String _cd_livello;
	private String _sistema = null;
	private String _edizione = null;
	private String _input_classe = null;
	private String _ds_classe = null;
	private static Logger log = Logger.getLogger("iccu.sbnmarcserver.CreaClasse");
	private List _vettoreElementiSimili = new ArrayList();
	private SbnSimile _tipoControllo;
	private SbnIndicatore c9_676;
	private String _ultTermine = null;

    private ClasseType classeType;
    private String  _condiviso;

    public CreaClasse(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        datiTypeChoice = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
        _tipoControllo = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
        classeType = (ClasseType) datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t686 = classeType.getClasseTypeChoice().getT686();
        _t676 = classeType.getClasseTypeChoice().getT676();
        if (_t676 != null){
        	try {
        		c9_676 = classeType.getClasseTypeChoice().getT676().getC9_676();
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
        	//c9_676 = classeType.getClasseTypeChoice().getT676().getC9_676();
        _cd_livello = classeType.getLivelloAut().toString();

	      // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
	      if (classeType.getCondiviso() == null ){
	      	_condiviso = "n";
	      }
	      else if ((classeType.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
	      	_condiviso = "s";
	      }
	      else if ((classeType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
	      	_condiviso = "n";
	      }

	      //almaviva5_20090401 #2780
	      _ultTermine = classeType.getUltTermine();
    }

    /**
     * La funzione deve restituire la chiave della classificazione.
     * Barbara
     * @return
     */
    public String getChiave() {
        String t001 = null;
        if (_t676 != null)
            t001 = "D" + _t676.getV_676() +_t676.getA_676();
        //almaviva5_20090414 per sistemi non dewey l'edizione è spazio
        if (_t686 != null)
            t001 = ValidazioneDati.fillRight(_t686.getC2_686(), ' ', 3) + "  " + _t686.getA_686();

        return t001;
    }
    /**
    * Esamina i parametri di ricerca ricevuti via xml
     * @throws InfrastructureException
    */
    protected void executeCrea() throws Exception {
        _t001 = this.getChiave();
        valorizzaClassificazione();
        ClasseValida classeValida = new ClasseValida(classeType);

        // DA INSERIRE ANCHE IN MODIFICA CANCELLA FONDE DOPO VERIFICA DI FUNZIONAMENTO
        //classeValida.controllaVettoreParametriSemantici(_sistema, _edizione, this.cd_utente);

        Classe classe = new Classe();
        if (_tipoControllo == null)
        	 _tipoControllo = SbnSimile.SIMILE;
        Tb_classe tb_classe = classeValida.validaPerCrea(_t001, _cattura);
        if (tb_classe != null)
	        _vettoreElementiSimili.add(tb_classe);
        if ((_vettoreElementiSimili.size() == 0) && (_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE)) {
        	//almaviva5_20100722 controllo sistemi abilitati in biblioteca
        	classeValida.controllaVettoreParametriSemantici(_t001, this._sbnUser);
        	//almaviva5_20090401 #2780
			classe.creaClasse(_t001, _cd_livello, _sistema, _edizione,
					_input_classe, _ds_classe, getCdUtente(), _condiviso,
					c9_676, _ultTermine);
	        tb_classeResponse = classe.cercaClassePerID(_t001);
        }
        if ((_vettoreElementiSimili.size() == 0) && (_tipoControllo.getType() == SbnSimile.SIMILEIMPORT_TYPE)) {
        	throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
        }
        object_response = formattaOutput();
    }

    private void valorizzaClassificazione() {
        if (_t676 != null) {
            _sistema = "D";// + _t676.getV_676();
            _edizione = _t676.getV_676();
            _ds_classe = _t676.getC_676();
            _input_classe = _t676.getA_676();
        } else {
            _sistema = ValidazioneDati.fillRight(_t686.getC2_686(), ' ', 3);
            //almaviva5_20090414
            _edizione = "  ";
            _ds_classe = _t686.getC_686();
			_input_classe = _t686.getA_686();
        }
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
        if (_vettoreElementiSimili.size() ==0)
	        result.setTestoEsito("OK");
	    else {
	    	result.setEsito("3004");
	    	result.setTestoEsito("sono stati trovati elementi simili");
	    }
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        int size = _vettoreElementiSimili.size();
        if (size == 0)
        	size = 1;
        Tb_classe tb_classe = new Tb_classe();
        FormatoClasse formatoClasse = new FormatoClasse(SbnTipoOutput.VALUE_1,null);
		if (_vettoreElementiSimili.size() > 0 ){
			for (int i=0; i<_vettoreElementiSimili.size();i++){
            tb_classe = (Tb_classe) _vettoreElementiSimili.get(i);
            output.addElementoAut(formatoClasse.formattaClasse(tb_classe, 0, 0, false));
			}
		}else{
			Classe classe = new Classe();
	        tb_classeResponse = classe.cercaClassePerID(_t001);
	        output.addElementoAut(formatoClasse.formattaClassePerCreazione(_t001, tb_classeResponse));
		}
        output.setMaxRighe(size);
        output.setNumPrimo(1);
        output.setTotRighe(size);

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
