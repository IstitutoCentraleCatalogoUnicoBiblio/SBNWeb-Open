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
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SequenceType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.log4j.Category;
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
public class CreaSequence extends CreaFactoring {

    private SbnUserType 		_sbnUser;
    private BigDecimal 		_schemaVersion;
//    private A676 				_t676;
//    private A686 				_t686;
    private SBNMarc 			_sbnmarcObj;
    //protected String object_response = null;
//    private Tb_classe 			tb_classeResponse = new Tb_classe();
//    private CreaTypeChoice 	datiTypeChoice;
//    private String 			_cd_livello;
//    private String 			_sistema = null;
//    private String 			_edizione = null;
//    private String 			_input_classe = null;
//    private String 			_ds_classe = null;
//    private static Category 	log = Category.getInstance("iccu.sbnmarcserver.CreaClasse");
//	private List 			_vettoreElementiSimili = new ArrayList();
//	private SbnSimile 			_tipoControllo;
    /**
     * file di log
     */
    static Category log = Category.getInstance("iccu.sbnmarcserver.CreaElementoAutFactoring");

    private String          _t001;
    private SbnAuthority                    _tipoAuthority;
    private SbnMateriale                    _tipoMateriale;
    private TipoRecord                      _tipoRecord;
    ClasseType classeType;

    public CreaSequence(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        SequenceType sequenceType= new SequenceType();
        sequenceType = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getSequence();
        _t001 = sequenceType.getIdSequence();
        _tipoAuthority = sequenceType.getTipoAuthority();
        _tipoMateriale = sequenceType.getTipoMateriale();
        _tipoRecord =sequenceType.getTipoRecord();
    }

    /**
    * Esamina i parametri di ricerca ricevuti via xml
     * @throws InfrastructureException
     * @throws SQLException
    */
    protected void executeCrea() throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring, InfrastructureException, SQLException {

        if ((_tipoAuthority == null ) && (_tipoMateriale != null) && (_tipoRecord == null) )
        	throw new EccezioneSbnDiagnostico(3999); //Parametri per la Creazione Progressivo errati

        _t001 = null;
        Progressivi progressivi = new Progressivi();

        if ((_tipoAuthority == null ) && (_tipoMateriale != null) && (_tipoRecord != null) ){
            _t001 = progressivi.getNextIdTitolo(_tipoMateriale.toString(),_tipoRecord.toString());
        }
        else if (_tipoAuthority.getType() == SbnAuthority.AU_TYPE){
            _t001 = progressivi.getNextIdAutore();
            _tipoMateriale = null;
            _tipoRecord = null;
        }
        else if (_tipoAuthority.getType() == SbnAuthority.SO_TYPE){
            _t001 = progressivi.getNextIdSoggetto();
            _tipoMateriale = null;
            _tipoRecord = null;
        }
        else if (_tipoAuthority.getType() == SbnAuthority.DE_TYPE){
            _t001 = progressivi.getNextIdDescrittore();
            _tipoMateriale = null;
            _tipoRecord = null;
        }
        else if (_tipoAuthority.getType() == SbnAuthority.LU_TYPE){
            _t001 = progressivi.getNextIdLuogo();
            _tipoMateriale = null;
            _tipoRecord = null;
        }
        else if (_tipoAuthority.getType() == SbnAuthority.CL_TYPE){
            _tipoMateriale = null;
            _tipoRecord = null;
            //_t001 = progressivi.getNextId();
        }
        else if (_tipoAuthority.getType() == SbnAuthority.MA_TYPE){
            _tipoMateriale = null;
            _tipoRecord = null;
            _t001 = progressivi.getNextIdMarca();
        }
        // DEVO VERIFICARE COSA DEVO FARE PER I TITOLI
        else if (_tipoAuthority.getType() == SbnAuthority.TU_TYPE){
            //_tipoMateriale = null;
            //_tipoRecord = null;
            _t001 = progressivi.getNextIdTitolo("" ,null);
        }
        else if (_tipoAuthority.getType() == SbnAuthority.UM_TYPE){
            _tipoMateriale = null;
            _tipoRecord = null;
//          LA FUNZIONE VIENE SOSTITUITA CON getNextIdTitolo passando valori fissi
// 			almaviva2 01/10/2007
            _t001 = progressivi.getNextIdTitolo("U",null);
        }
        // END
//        else if ((_tipoAuthority == null ) && (_tipoMateriale != null) && (_tipoRecord != null) ){
//            _t001 = progressivi.getNextIdTitolo(_tipoMateriale.toString(),_tipoRecord.toString());
//        }
        else{
            throw new EccezioneSbnDiagnostico(3999,"Parametri per la Creazione Progessivo errati");
        }

//        tipologia di authority:
//        TU = Titolo Uniforme,
//        UM = Titolo Uniforme Musica,
//
//        AU = autore,
//        SO = Soggetto,
//        DE = Descrittore,
//        LU = Luogo,
//        CL = Classe,
//        MA = Marca,
//        RE = Repertorio

        //_t001 = this.getChiave ();
        //valorizzaSequence();
//        ClasseValida classeValida = new ClasseValida(classeType);
//        Classe classe = new Classe();
//        if (_tipoControllo == null)
//        	 _tipoControllo = SbnSimile.SIMILE;
//        Tb_classe tb_classe = classeValida.validaPerCrea(_t001);
//        if (tb_classe != null)
//	        _vettoreElementiSimili.add(tb_classe);
//        if ((_vettoreElementiSimili.size() == 0) && (!_tipoControllo.equals(SbnSimile.SIMILEIMPORT))) {
//            classe.creaClasse(_t001, _cd_livello, _sistema, _edizione, _input_classe, _ds_classe, getCdUtente());
//	        tb_classeResponse = classe.cercaClassePerID(_t001);
//        }
//        if ((_vettoreElementiSimili.size() == 0) && (_tipoControllo.equals(SbnSimile.SIMILEIMPORT))) {
//        	throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
//        }
        object_response = formattaOutput();
    }
//    private void valorizzaSequence() {
//            _sistema = "D";
//            _edizione = _t676.getV_676().toString();
//            _ds_classe = _t676.getC_676();
//            _input_classe = _t676.getA_676();
//    }

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

        if (_t001 != null)
	        result.setTestoEsito("OK");
	    else {
	    	result.setEsito("3004");
	    	result.setTestoEsito("Creazione Progessivo errato");
	    }


        SequenceType sequenceType = new SequenceType();
        sequenceType.setIdSequence(_t001);
        sequenceType.setTipoAuthority(_tipoAuthority);
        if(_tipoMateriale != null){
            sequenceType.setTipoMateriale(_tipoMateriale);
            sequenceType.setTipoRecord(_tipoRecord);
        }
        output.setSequence(sequenceType);

        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        output.setMaxRighe(0);
        output.setNumPrimo(1);
        output.setTotRighe(0);

        return sbnmarc;
    }

   	public String getIdAttivita(){
		//return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
   		return CodiciAttivita.getIstance().CREA_1015;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    }
    protected SBNMarc formattaLista(int numeroBlocco)
    throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        return null;
    }

}
