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
import it.finsiel.sbn.polo.oggetti.RepertorioMarca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.iccu.sbn.ejb.model.unimarcmodel.AskDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Abbonamento;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Contiene le funzionalità di ricerca per l'entità Repertorio
 * restituisce la lista (sintetica e analitica coincidono)
 * Possibili parametri di ricerca:
 * Identificativo: T001 (è la sigla  del repertorio)
 * parole della descrizione: paroleAut
 * descrizione esatta: stringa esatta
 * descrizione parte iniziale: stringaLike
 * repertori legati ad autore, a titolo, a marca: ArrivoLegame
 *
 * Filtri di ricerca: nessuno
 *
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo o sulla descrizione
 * tipoOut: solo 000 analitico
 *
 * Tabelle coinvolte:
 * - Tb_repertorio
 * OggettiCoinvolti:
 * - Repertorio
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * se non ok ritorna il msg response di diagnostica
 *
 * @author
 * @version 13/01/2003
 */
public class MservAskDoc extends MServDocFactoring {

    private static Logger log = Logger.getLogger("iccu.serversbnmarc.MservAskDoc");
    RicTit4Inventario ricTit4Inventario;
    RicTit4Abbonamento ricTit4Abbonamento;
    private String _xdoc;
    private RicTit4Inventario _xinv;
    private RicTit4Abbonamento _xabb;
    private String _Kbibl;
    private String _Kinv;
    private String _Kordi;
    private String _Kanno;

    public MservAskDoc(SBNMarc input_root_object) {
        super(input_root_object);
        AskDoc _askDoc;
        _askDoc = input_root_object.getSbnMessage().getMServDoc().getAskdoc();
        //		CercaSoggettoDescrittoreClassiReperType cerca = new CercaSoggettoDescrittoreClassiReperType();
        if (_askDoc.getXdoc() != null)
            _xdoc = _askDoc.getXdoc();

        if (_askDoc.getXinv() != null){
        	if(_askDoc.getXinv().getKinv() != null)
        		_Kinv = _askDoc.getXinv().getKinv();
        	if(_askDoc.getXinv().getKbibl() != null)
        		_Kbibl = _askDoc.getXinv().getKbibl();
        }
        if(_askDoc.getXabb() != null){
        	if(_askDoc.getXabb().getKbibl() != null)
        		_Kbibl = _askDoc.getXabb().getKbibl();
        	if(_askDoc.getXabb().getKordi() != null)
        		_Kordi = _askDoc.getXabb().getKordi();
        	if(_askDoc.getXabb().getKanno() != null)
        		_Kanno = _askDoc.getXabb().getKanno();
        }

    }

    public void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
    	int counter = 0;
    	if(_xdoc != null)
    		counter ++;
    	if((_Kinv != null) && (_Kbibl != null))
    		counter ++;
    	if((_Kbibl != null) && (_Kordi != null) && (_Kanno != null))
    		counter ++;
    	if (counter != 1)
              throw new EccezioneSbnDiagnostico(3039, "comunicare uno e un solo canale di ricerca");
    	if(_xdoc != null)
    		cercaXdoc();
    	if((_Kinv != null) && (_Kbibl != null))
    		cercaXinv();
    	if((_Kbibl != null) && (_Kordi != null) && (_Kanno != null))
    		cercaXabb();


    }

    /**
     * Method cercaRepertorioPerMarca.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaXdoc() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioMarca repertorioMarca = new RepertorioMarca();
        List TableDaoAuxiliar;

    }
    private void cercaXinv() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioMarca repertorioMarca = new RepertorioMarca();
        List TableDaoAuxiliar;

    }
    private void cercaXabb() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioMarca repertorioMarca = new RepertorioMarca();
        List TableDaoAuxiliar;

    }



    /** Prepara la stringa di output in formato xml */
    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        SBNMarc risultato = formattaOutput();
        //rowCounter += maxRighe;
        return risultato;
    }

    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneSbnDiagnostico {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        SbnUserType user = new SbnUserType();
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_repertorio tb_repertorio;
        int totRighe = 0;
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
        output.setTotRighe(totRighe);
        if (idLista != null)
            output.setIdLista(idLista);
        return sbnmarc;

    }

}
