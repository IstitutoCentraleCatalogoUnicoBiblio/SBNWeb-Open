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
import it.finsiel.sbn.polo.factoring.base.FormatoMarca;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Parola;
import it.finsiel.sbn.polo.oggetti.RepertorioMarca;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_par;
import it.finsiel.sbn.polo.orm.viste.Vl_repertorio_mar;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaMarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

	/**
	* Contiene le funzionalità di ricerca per l'entità Marca
	* restituisce la lista sintetica o analitica.
	* Possibili parametri di ricerca:
	* Identificativo: T001
	* intervallo di data di aggiornamento: T005_Range
	* parole della descrizione: paroleAut
	* parole chiave: b_921 in AND
	* repertorio e citazione
	* entità collegata: ArrivoLegame. Può essere: documento (marche di un
	* documento) Autore (marche di un autore)
	*
	* Filtri di ricerca:
	* livello di autorità: tipoAuthority
	*
	* Parametrizzazioni di output:
	* tipoOrd: ordinamento richiesto, può essere su identificativo (mid), sulla
	* descrizione, sul timestamp + descrizione, su repertorio+citazione
	*
	* tipoOut: tipo di output richiesto: Esame, Lista completa (001) lista ridotta
	* (003)
	*
	* Tabelle coinvolte:
	* - Tb_marca, Tb_parole
	* Tr_rep_mar;Tr_tit_mar,Tr_aut_mar,Tr_rep_aut
	*
	* OggettiCoinvolti:
	* - Marca
	* - Parola
	* - AutoreMarca
	* - RepertorioMarca
	* - MarcaValida
	*
	* Passi da eseguire:
	* esegue la ricerca secondo i parametri ricevuti;
	* prepara l'output secondo le indicazioni ricevute
	* se non ok ritorna il msg response di diagnostica
	*
	@author
	@version 13/01/2003
	*/
public class CercaMarca extends CercaElementoAutFactoring{

    static Category 				log = Category.getInstance("iccu.sbnmarcserver.CercaMarca");

    private CercaElementoAutType 	_elementoAut;
    private CercaMarcaType 		_cercaMarca;
    private String 				_T001 = null;
    private String[] 				_paroleMar = null;
    private SbnRangeDate 			_T005_Range = null;
    private ArrivoLegame 			arrivoLegame = null;
    private SbnAuthority 			statusAuthority = null;
	private int 					_citazione = 0;
	private SbnFormaNome 			_formaNome;
	private SbnLivello 			_livelloAutA;
	private SbnLivello 			_livelloAutDa;
	private String[] 				_b_921;
	private String 				_e_921;

	private String 				_repertorio;
	private LegameElementoAutType 	_legameElementoAut;
	private LegameDocType 			_legameDoc;
	private SbnAuthority 			_tipoAuthority;

	private List					_TableDao_response = new ArrayList();



	public CercaMarca(SBNMarc input_root_object){
        super(input_root_object);
        _elementoAut = factoring_object.getCercaElementoAut();
        if (_elementoAut.getArrivoLegame() != null){
			_legameElementoAut = _elementoAut.getArrivoLegame().getLegameElementoAut();
	 		_legameDoc = _elementoAut.getArrivoLegame().getLegameDoc();
        }

        CercaDatiAutType datiElementoAut = _elementoAut.getCercaDatiAut();
        if (datiElementoAut instanceof CercaMarcaType)
            _cercaMarca = (CercaMarcaType) datiElementoAut;
        arrivoLegame = _elementoAut.getArrivoLegame();
        tipoOrd = "order_" + factoring_object.getTipoOrd();
        tipoOut = factoring_object.getTipoOutput();
        CanaliCercaDatiAutType canaliRicerca = datiElementoAut.getCanaliCercaDatiAut();
        if (canaliRicerca != null) {
            _T001 = canaliRicerca.getT001();
		}
        _T005_Range = 		datiElementoAut.getT005_Range();
        if (_cercaMarca!=null){
			_paroleMar = 		_cercaMarca.getParoleAut();
			_citazione = 		_cercaMarca.getCitazione();
			_repertorio=		_cercaMarca.getRepertorio();
			_formaNome =		_cercaMarca.getFormaNome();
			_livelloAutA =		_cercaMarca.getLivelloAut_A();
			_livelloAutDa =		_cercaMarca.getLivelloAut_Da();
			_b_921 =			_cercaMarca.getB_921();
			if (_cercaMarca.getE_921() != null)
				_e_921 =			_cercaMarca.getE_921();
        }

		if (_legameElementoAut != null)
			_tipoAuthority =	_legameElementoAut.getTipoAuthority();
	}

    /** Prepara la stringa di output in formato xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected SBNMarc preparaOutput()
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if ((tavola_response == null) && (_TableDao_response == null)) {
            log.error(
                "Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        // Deve utilizzare il numero di richieste che servono
        List response = null;
        if (tavola_response != null){
	        response = tavola_response.getElencoRisultati(maxRighe);
        }
	    else {
            response = _TableDao_response;
            numeroRecord = response.size();
        }

        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

    /**
     * Prepara l'xml di risposta utilizzando il vettore TableDao_response
     * @return Stringa contenente l'xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private SBNMarc formattaOutput(List TableDao_response) throws IllegalArgumentException, InvocationTargetException, Exception {
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
        FormatoMarca forMar = new FormatoMarca(tipoOut, tipoOrd,user_object);
        Tb_marca tb_marca;
        int numElementi = TableDao_response.size();
        for (int i = 0; i < numElementi; i++) {
            tb_marca = (Tb_marca) TableDao_response.get(i);
            output.addElementoAut(forMar.formattaMarca(tb_marca));
        }
        output.setTotRighe(numElementi);
        result.setEsito("0000"); //Esito positivo
        if (numElementi >0){
	        result.setTestoEsito("OK");
        }else {
	        result.setEsito("3001");
	        result.setTestoEsito("nessun elemento trovato");
        }
        if (idLista != null)
            output.setIdLista(idLista);
        output.setMaxRighe(maxRighe);
        output.setTotRighe(numeroRecord);
        output.setNumPrimo(rowCounter+1);
        output.setTipoOrd(factoring_object.getTipoOrd());
        output.setTipoOutput(factoring_object.getTipoOutput());
        return sbnmarc;
    }

    protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
    	verificaAbilitazioni();
        if (_T001 != null)
            cercaMarcaPerID();
//        else if (_T005_Range != null)
//            cercaMarcaPerDatavar();
        else if (_cercaMarca!=null){
	        if (_paroleMar.length != 0)
	            cercaMarcaPerParole();
	        else if (_b_921.length != 0)
	        	cercaMarcaPer_b921();
	        else if (_e_921 != null)
	        	cercaMarcaPer_e921();
			//se c'è la citazione è obbligatorio anche il repertorio
			else if (_cercaMarca.hasCitazione() && (_repertorio == null))
					throw new EccezioneSbnDiagnostico(3016);
			else if (_cercaMarca.hasCitazione() && (_repertorio != null))
				cercaMarcaPerCitazioneRepertorio();
			else if (_repertorio != null)
				cercaMarcaPerRepertorio();
        }
		else if ((_legameElementoAut != null)){
			if (_tipoAuthority.getType() == SbnAuthority.AU_TYPE)
				cercaMarcaPerLegameElementoAut();
		}
		else if (_legameDoc != null)
			cercaMarcaPerLegameDoc();
		else if (esporta) {
				if (_T001 == null && _cercaMarca == null && _legameElementoAut == null
					&& _legameDoc == null) {
					if (_paroleMar == null) {
						_paroleMar = new String[1];
					}
					_paroleMar[0] = " ";
					cercaMarcaPerParole();
				}
			}
		else throw new EccezioneSbnDiagnostico(3039);
    }

	/**
	 * Method cercaMarcaPerLegameDoc.
	 * questo metodo legge il bid del titolo e ricerca le marche collegate
	 * attraverso la vista vl_marca_tit
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPerLegameDoc() throws IllegalArgumentException, InvocationTargetException, Exception {
		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		tavola_response = marca.cercaMarcaPerTitolo(_legameDoc.getIdArrivo(),tipoOrd);
		numeroRecord = tavola_response.getElencoRisultati().size();
	}


	/**
	 * Method cercaMarcaPerLegameElementoAut.
	 * questo metodo legge il vid dell'autore e ricerca le marche collegate
	 * attraverso la vista vl_marca_aut
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPerLegameElementoAut() throws IllegalArgumentException, InvocationTargetException, Exception {
		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		controllaNumeroRecord(marca.contaMarcaPerAutore(_legameElementoAut.getIdArrivo()));
		tavola_response = marca.cercaMarcaPerAutore(_legameElementoAut.getIdArrivo(),tipoOrd);
	}


	/**
	 * Method cercaMarcaPerCitazioneRepertorio.
	 * INPUT: citazione e repertorio
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPerCitazioneRepertorio() throws IllegalArgumentException, InvocationTargetException, Exception {
		RepertorioMarca repertorioMarca = new RepertorioMarca();
		List vettoreLegami = new ArrayList();
		vettoreLegami = repertorioMarca.cercaMarcaPerCitazioneRepertorio(_citazione,_repertorio);
		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		for (int i=0; i<vettoreLegami.size();i++){
			marca = new Marca();
			Tb_marca tb_marca = marca.estraiMarcaPerID(((Vl_repertorio_mar)vettoreLegami.get(i)).getMID());
			_TableDao_response.add(tb_marca);
		}
	}


	/**
	 * Method cercaMarcaPerRepertorio.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPerRepertorio() throws IllegalArgumentException, InvocationTargetException, Exception {
		RepertorioMarca repertorioMarca = new RepertorioMarca();
		TableDao appoggio;
        List vettoreLegami = new ArrayList();
		appoggio = repertorioMarca.cercaMarcaPerRepertorio(_repertorio);
		vettoreLegami = appoggio.getElencoRisultati();

		//dal vettore dei legami con la marca ...
		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		for (int i=0; i<vettoreLegami.size();i++){
			marca = new Marca();
			Tb_marca tb_marca = marca.estraiMarcaPerID(((Tr_rep_mar)vettoreLegami.get(i)).getMID());
			_TableDao_response.add(tb_marca);
		}
	}


	/**
	 * Method cercaMarcaPer_b921.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPer_b921() throws IllegalArgumentException, InvocationTargetException, Exception {
		Parola parola = new Parola();
        TableDao appoggio;
        List vettoreVl_marca_par = new ArrayList();
		controllaNumeroRecord(parola.contaMarcaPerParola(_b_921));
		appoggio = parola.cercaMarcaPerParola(_b_921,tipoOrd);
		vettoreVl_marca_par = appoggio.getElencoRisultati();

		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		for (int i=0; i<vettoreVl_marca_par.size();i++){
			marca = new Marca();
			Tb_marca tb_marca = marca.estraiMarcaPerID(((Vl_marca_par)vettoreVl_marca_par.get(i)).getMID());
			_TableDao_response.add(tb_marca);
		}
	}


	/**
	 * Method cercaMarcaPer_e921.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPer_e921() throws IllegalArgumentException, InvocationTargetException, Exception {
        Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
        controllaNumeroRecord(marca.contaMarcaPerMotto(_e_921,_cercaMarca));
        tavola_response = marca.cercaMarcaPerMotto(_e_921, _cercaMarca, tipoOrd);
	}



	/**
	 * Method cercaMarcaPerParole.
	 * qualora fosse valorizzato il campo paroleAut facciamo una ricerca per parole
	 * chiave all'interno della descrizione della marca
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaMarcaPerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        Marca marca = new Marca();
        marca.valorizzaFiltri(_cercaMarca);
        //almaviva5_20120326 #4715
        _paroleMar = normalizzaParolePerRicerca(_paroleMar, ElencoParole.getInstance() );

        controllaNumeroRecord(marca.contaMarcaPerParole(_paroleMar,_cercaMarca));
        tavola_response = marca.cercaMarcaPerParole(_paroleMar, _cercaMarca, tipoOrd);
	}


	/**
	 * Method cercaMarcaPerID.
	 * @throws InfrastructureException
	 */
	private void cercaMarcaPerID() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		Marca marca = new Marca();
		marca.valorizzaFiltri(_cercaMarca);
		Tb_marca tmarca = marca.estraiMarcaPerID(_T001);
    if (tmarca != null) {
      _TableDao_response.add(tmarca);
    }
	}


    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_MARCHE_1252;
    }

}
