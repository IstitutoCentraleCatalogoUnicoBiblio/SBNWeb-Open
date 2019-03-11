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
import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoAbstract;
import it.finsiel.sbn.polo.oggetti.Abstract;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;




/**
 * Contiene le funzionalità di ricerca per l'entità Abstract
 * restituisce la lista sintetica o analitica
 *
 * Possibili parametri di ricerca:
 * Identificativo: T001
 * Descrizione Abs esatto: stringa esatta
 * Descrizione Abs parte iniziale: stringaLike
 *
 * Filtri di ricerca:
 * livello di autorità: tipoAuthority
 * intervallo di data di aggiornamento: T005_Range
 *
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo , sulla
 * descrizione, sul timestamp + identificativo
 * tipoOut: analitico o sintetico
 *
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * in analitica se il descrittore è di rinvio si prepara l'esame della forma
 * accettata
 * se non ok ritorna il msg response di diagnostica
 * @author
 * @version 13/01/2007
 */
public class CercaAbstract extends CercaElementoAutFactoring {

	private CercaElementoAutType 						_elementoAut;
	private CercaDatiAutType 							_datiElementoAut;
	private CercaAbstract 								_cercaAbstract = null;
	private String 										_T001;
	private String 										stringaLike;
	private String 										stringaEsatta;
	private SbnRangeDate 								_T005_Range;
	private String 										_livelloAutA;
	private String 										_livelloAutDA;
	private Tb_abstract									_tb_abstract = new Tb_abstract();
	private boolean 									ricercaPuntuale = false;
	private TableDao 									_TableDao_response;

	public CercaAbstract(SBNMarc input_root_object){
		super(input_root_object);
		CercaType 			_cerca;
		StringaCercaType 	stringaCerca;
		_cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_elementoAut =  _cerca.getCercaElementoAut();
		_datiElementoAut = _elementoAut.getCercaDatiAut();
		CercaAbstractType cerca = new CercaAbstractType();
        if (_datiElementoAut instanceof CercaAbstractType)
        	cerca = (CercaAbstractType) _datiElementoAut;

		if (_datiElementoAut.getCanaliCercaDatiAut() != null){
			_T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
//		if((_T001 == null) || (_T001.length() !=  10)){
//            throw new EccezioneSbnDiagnostico(6667,"Bid mancante o errato");
//		}

//			stringaCerca = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca();
//			if (stringaCerca != null)
//				stringaLike = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
//			if (stringaCerca != null)
//				stringaEsatta = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
		}
//		_T005_Range = _datiElementoAut.getT005_Range();
//		if (_datiElementoAut.getLivelloAut_A() != null)
//			_livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
//		if (_datiElementoAut.getLivelloAut_Da() != null)
//			_livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();

   }


   protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
       if (factoring_object.getTipoOrd()!=null)
		if ((factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_3.getType())||(factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_4.getType()))
			throw new EccezioneSbnDiagnostico(1231,"errore nell'ordinamento");
		verificaAbilitazioni();
        int counter = 0;
        if (_T001 != null)
            counter++;
//        if ((stringaLike != null) || (stringaEsatta != null))
//            counter++;
        if((_T001 == null) || (_T001.length() !=  10)){
            throw new EccezioneSbnDiagnostico(3039,"Bid mancante o errato");
        }
        if (_T001 != null)
            cercaDescrittorePerId();
// La ricerca viene vatta solo per bid
//        else if ((stringaLike != null)||(stringaEsatta != null))
//            cercaDescrittorePerStringaCerca();

		if (! (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() || tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())) {
			log.error("Tipo output sconosciuto -" + tipoOut);
			throw new EccezioneSbnDiagnostico(3044);
		}

	}



	/**
	 * Method cercaDescrittorePerStringaCerca.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
//	private void cercaDescrittorePerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
//        Abstract abs = new Abstract();
//        abs.valorizzaFiltri(_datiElementoAut);
//        if (stringaLike != null){
//            stringaLike = NormalizzaNomi.normalizzazioneGenerica(stringaLike);
//	        controllaNumeroRecord(abs.contaDescrittorePerNomeLike(stringaLike));
//			tavola_response = abs.cercaDescrittorePerNomeLike(stringaLike, tipoOrd);
//        } else if (stringaEsatta != null) {
//            stringaEsatta = NormalizzaNomi.normalizzazioneGenerica(stringaEsatta);
//        	controllaNumeroRecord(abs.contaDescrittorePerNomeEsatto(stringaEsatta));
//			tavola_response = abs.cercaDescrittorePerNomeEsatto(stringaEsatta, tipoOrd);
//        }
////		_TableDao_response = tavola_response.getElencoRisultati();
//
//	}


	/**
	 * Method cercaDescrittorePerId.
	 * @throws InfrastructureException
	 */
	private void cercaDescrittorePerId() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Abstract abs = new Abstract();
        _tb_abstract =  abs.cercaAbstractPerId(_T001);
        if (_tb_abstract == null)
        	throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
	}


    protected SBNMarc preparaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
        if ((tavola_response == null)&&(!ricercaPuntuale)) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        // Deve utilizzare il numero di richieste che servono
        List response = new ArrayList();
		if (ricercaPuntuale)
			response.add(_tb_abstract);
		else response = tavola_response.getElencoRisultati(maxRighe);
        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

     private SBNMarc formattaOutput(List TableDao_response)
    throws IllegalArgumentException, InvocationTargetException, Exception {
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
        Tb_abstract tb_abstract;
        int totRighe = 0;
        if (ricercaPuntuale)
        	totRighe = 1;
        else
          	totRighe = numeroRecord;
        FormatoAbstract formatoAbstract = new FormatoAbstract();
        if ((totRighe > 0) || ricercaPuntuale) {
            output.setMaxRighe(maxRighe);
            output.setTotRighe(totRighe);
            output.setNumPrimo(rowCounter+1);
            output.setTipoOrd(factoring_object.getTipoOrd());
            output.setTipoOutput(tipoOut);
	  		ElementAutType elemento = new ElementAutType();
            if (!ricercaPuntuale){
	            for (int i = 0; i < TableDao_response.size(); i++) {
	            	tb_abstract = (Tb_abstract) TableDao_response.get(i);
                    output.addElementoAut(formatoAbstract.formattaAbstract(tb_abstract, tipoOut));
	            }
            }
	        else {
	        	output.addElementoAut(formatoAbstract.formattaAbstract(_tb_abstract, tipoOut));

	        }
            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
        } else {
            result.setEsito("3001");
            //Esito non positivo: si potrebbe usare un'ecc.
            result.setTestoEsito("Nessun elemento trovato");
        }

        output.setTotRighe(totRighe);
        if (idLista != null)
            output.setIdLista(idLista);
        return sbnmarc;


    }


    /**
     * Metodo per il controllo delle autorizzazioni
     */
    public void verificaAbilitazioni() throws EccezioneAbilitazioni {
        if (!validator.verificaAttivitaDesc(getCdUtente(),"Cerca"))
            throw new EccezioneAbilitazioni(4000,"Utente non autorizzato");
    }

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CERCA_DESCRITTORE_1283;
    }


}
