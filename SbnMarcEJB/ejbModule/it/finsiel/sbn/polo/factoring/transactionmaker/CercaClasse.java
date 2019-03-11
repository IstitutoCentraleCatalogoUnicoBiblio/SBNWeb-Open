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
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CercaClasse extends CercaElementoAutFactoring {

	private CercaElementoAutType _elementoAut;

	private CercaDatiAutType _datiElementoAut;
	private String _T001 = null;
	private SbnRangeDate _T005_Range;
	private String _livelloAutA;
	private String _livelloAutDA;

	private boolean ricercaUnivoca;
	private CercaSoggettoDescrittoreClassiReperType _cercaClasse;
	private String[] paroleAut = null;
	private String stringaLike;
	private String sistemaClassificazione = null;
	private Tb_classe _tb_classe = new Tb_classe();
	private boolean ricercaPuntuale = false;

	private boolean enableRanking = false;

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.CercaClasse");



	public CercaClasse(SBNMarc input_root_object) {
		super(input_root_object);
		CercaType 			_cerca;
		StringaCercaType 	stringaCerca;
		_cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_elementoAut =  _cerca.getCercaElementoAut();
		_datiElementoAut = _elementoAut.getCercaDatiAut();
		CercaSoggettoDescrittoreClassiReperType cerca = new CercaSoggettoDescrittoreClassiReperType();
		if (_datiElementoAut instanceof CercaSoggettoDescrittoreClassiReperType)
            _cercaClasse = (CercaSoggettoDescrittoreClassiReperType) _datiElementoAut;
		if (_datiElementoAut.getCanaliCercaDatiAut() != null){
			_T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
			stringaCerca = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca();
			if (stringaCerca != null)
				stringaLike = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
		}
		_T005_Range = _datiElementoAut.getT005_Range();
		if (_datiElementoAut.getLivelloAut_A() != null)
			_livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
		if (_datiElementoAut.getLivelloAut_Da() != null)
			_livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();
		if (_cercaClasse != null){
			paroleAut = _cercaClasse.getParoleAut();
			sistemaClassificazione = _cercaClasse.getSistemaClassificazione();
		}

   }



   /**
    * metodo attivato da CercaElementoAut.
    * i CANALI di ricerca possono essere:
    * -t001
    * -v_676
    * -stringaLike
    * -paroleAut
    *
    * i FILTRI sono:
    * -livelloAutDa
    * -livelloAutA
    * -t005_range
    * -v_676
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
   protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {

		verificaAbilitazioni();
        int counter = 0;
        if (_T001 != null)
            counter++;
        if (stringaLike != null)
            counter++;
        if (paroleAut != null && paroleAut.length > 0)
            counter++;
        if (elementoAut.getArrivoLegame() != null) {
          counter++;
        }
        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039,"comunicare uno e un solo canale di ricerca");
	//	if (sistemaClassificazione == null)
	//	    throw new EccezioneSbnDiagnostico(3084,"valorizzare sistema di classificazione");
        if (_T001 != null)
            cercaClassePerId();
        else if (stringaLike != null)
            cercaClassePerStringaCerca();
        else if (paroleAut != null && paroleAut.length > 0)
            cercaClassePerParole();


        if (elementoAut.getArrivoLegame() != null)
        	//almaviva5_20111013 evolutive CFI
        	cercaClassePerLegame();
 	}


   private void cercaClassePerLegame() throws IllegalArgumentException, InvocationTargetException, Exception {
		ArrivoLegame legame = _elementoAut.getArrivoLegame();
		if ((legame == null))
			return;
		if (legame.getLegameDoc() != null) {
			cercaClassePerTitolo();
			return;
		}

		//almaviva5_20111013 legame ad authority thesauro
		switch (legame.getLegameElementoAut().getTipoAuthority().getType()) {
		case SbnAuthority.TH_TYPE:
			this.enableRanking = true;
			cercaClassePerThesauro();
			break;
		default:
			throw new EccezioneSbnDiagnostico(501);
		}
  }


	/**
	 * Method cercaClassePerParole.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaClassePerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        Classe classe = new Classe();
        classe.valorizzaFiltri(_datiElementoAut);
        controllaNumeroRecord(classe.contaClassePerParoleNome(paroleAut));
        tavola_response = classe.cercaClassePerParoleNome(paroleAut,tipoOrd);

	}

	private void cercaClassePerTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
      Classe classe = new Classe();
      classe.valorizzaFiltri(_datiElementoAut);
      String idArrivo;
      if (elementoAut.getArrivoLegame().getLegameElementoAut() != null) {
        idArrivo = elementoAut.getArrivoLegame().getLegameElementoAut().getIdArrivo();
      } else {
        idArrivo = elementoAut.getArrivoLegame().getLegameDoc().getIdArrivo();
      }
      //almaviva5_20111114 #4694 l'edizione non è obbligatoria
      if((classe.getCD_SISTEMA() != null) ) {//&& (classe.getCD_EDIZIONE() != null)){
        controllaNumeroRecord(classe.contaClassePerTitoloSistemaEdizione(idArrivo,classe.getCD_SISTEMA().trim(),classe.getCD_EDIZIONE()));
        tavola_response = classe.cercaClassePerTitoloSistemaEdizione(idArrivo,classe.getCD_SISTEMA().trim(),classe.getCD_EDIZIONE(),tipoOrd);
      }
      else{
        controllaNumeroRecord(classe.contaClassePerTitolo(idArrivo));
        tavola_response = classe.cercaClassePerTitolo(idArrivo, tipoOrd);
      }
	}


	private void cercaClassePerThesauro() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		Classe classe = new Classe();
		classe.valorizzaFiltri(_datiElementoAut);
		String idArrivo = elementoAut.getArrivoLegame().getLegameElementoAut().getIdArrivo();
		controllaNumeroRecord(classe.contaClassePerThesauro(idArrivo));
		tavola_response = classe.cercaClassePerThesauro(idArrivo, tipoOrd);

	}

	/**
	 * Method cercaClassePerNome.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaClassePerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Classe classe = new Classe();
        classe.valorizzaFiltri(_datiElementoAut);
        controllaNumeroRecord(classe.contaClassePerNomeLike(stringaLike));
		tavola_response = classe.cercaClassePerNomeLike(stringaLike, tipoOrd);
	}




    private void cercaClassePerId() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Classe classe = new Classe();
        _tb_classe =  classe.cercaClassePerID(_T001);
        if (_tb_classe == null)
        	throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
    }


	public String getIdAttivita(){
		return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;
	}

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CERCA_CLASSI_1247;
    }

    /** Prepara la stringa di output in formato xml */
    protected SBNMarc preparaOutput()
        throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico, Exception {
         if ((tavola_response == null) && (_tb_classe == null)) {
            log.error(
                "Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        // Deve utilizzare il numero di richieste che servono
        List response = null;
        if (tavola_response != null){
	        response = tavola_response.getElencoRisultati(maxRighe);
        } else {
	    	response = new ArrayList();
	    	response.add(_tb_classe);
	    }

        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

    private SBNMarc formattaOutput(List TableDao_response)
    throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico, Exception {
		SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_classe tb_classe;
        int totRighe = 0;
        if (TableDao_response !=null)
        	totRighe =TableDao_response.size();
        FormatoClasse formatoClasse = new FormatoClasse(tipoOut,_T001);
        if ((totRighe > 0) || ricercaPuntuale) {
            output.setMaxRighe(maxRighe);
            output.setNumPrimo(rowCounter+1);
            output.setTipoOrd(factoring_object.getTipoOrd());
            output.setTipoOutput(tipoOut);
			if (!ricercaPuntuale) {
				for (int i = 0; i < totRighe; i++) {
					tb_classe = (Tb_classe) TableDao_response.get(i);
					//almaviva5_20091028
					int num_tit = formatoClasse.contaTitoliCollegatiClasse(
							tb_classe.getCD_SISTEMA(), tb_classe
									.getCD_EDIZIONE(), tb_classe.getCLASSE());
					int num_tit_bib = formatoClasse
							.contaTitoliCollegatiClasseBib(tb_classe
									.getCD_SISTEMA(), tb_classe
									.getCD_EDIZIONE(), tb_classe.getCLASSE(),
									user_object);
					output.addElementoAut(formatoClasse.formattaClasse(tb_classe, num_tit, num_tit_bib, enableRanking));
				}
				output.setTotRighe(numeroRecord);
			}
 	        else {
				//almaviva5_20091028
				int num_tit = formatoClasse.contaTitoliCollegatiClasse(
						_tb_classe.getCD_SISTEMA(), _tb_classe
								.getCD_EDIZIONE(), _tb_classe.getCLASSE());
				int num_tit_bib = formatoClasse
						.contaTitoliCollegatiClasseBib(_tb_classe
								.getCD_SISTEMA(), _tb_classe
								.getCD_EDIZIONE(), _tb_classe.getCLASSE(),
								user_object);
				output.addElementoAut(formatoClasse.formattaClasse(_tb_classe, num_tit, num_tit_bib, enableRanking));
                output.setTotRighe(1);
            }
            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
        } else {
            result.setEsito("3001");
            //Esito non positivo: si potrebbe usare un'ecc.
            result.setTestoEsito("Nessun elemento trovato");
        }
        //output.setTotRighe(totRighe);
        if (idLista != null)
            output.setIdLista(idLista);
        return sbnmarc;


    }
}
