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
import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTermineThesauro;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.CountDesDes;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.TerminiTermini;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.viste.Vl_termini_termini;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CountDesDesType;
import it.iccu.sbn.ejb.model.unimarcmodel.DesType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;




/**
 * Contiene le funzionalità di ricerca per l'entità TermineThesauro di Thesauro
 * restituisce la lista sintetica o analitica
 *
 * Possibili parametri di ricerca:
 * Identificativo: T001
 * parole del TermineThesauro: paroleAut
 * TermineThesauro esatto: stringa esatta
 * TermineThesauro parte iniziale: stringaLike
 *
 * Filtri di ricerca:
 * livello di autorità: tipoAuthority
 * intervallo di data di aggiornamento: T005_Range
 * codice soggettario
 *
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo , sulla
 * descrizione, sul timestamp + identificativo
 * tipoOut: analitico o sintetico
 *
 * Tabelle coinvolte:
 * - Tb_termine_thesauro, Tr_termini_termini
 * OggettiCoinvolti:
 * - Termini, TerminiTermini
 *
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * in analitica se il descrittore è di rinvio si prepara l'esame della forma
 * accettata
 * se non ok ritorna il msg response di diagnostica
 * @author
 * @version 13/01/2003
 */
public class CercaTermineThesauro extends CercaElementoAutFactoring {

	private CercaElementoAutType 						_elementoAut;
	private CercaDatiAutType 							_datiElementoAut;
	private CercaSoggettoDescrittoreClassiReperType 	_cercaTermine;
	private String 										_T001;
	private String 										stringaLike;
	private String 										stringaEsatta;
	private SbnRangeDate 								_T005_Range;
	private String 										_livelloAutA;
	private String 										_livelloAutDA;
	private String[] 									paroleAut;
	private String 										_c2_250;
	private Tb_termine_thesauro							_tb_termine = new Tb_termine_thesauro();
	private boolean 									ricercaPuntuale = false;
	//private TableDao 									_TableDao_response;
	private List _TableDao_response = new ArrayList();
	private String[] paroleTerminiPerTermini = null;
	private boolean ricercaTerminiPerTermini = false;
	private ArrivoLegame arrivoLegame = null;

	public CercaTermineThesauro(SBNMarc input_root_object){
		super(input_root_object);
		CercaType 			_cerca;
		StringaCercaType 	stringaCerca;
		_cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_elementoAut =  _cerca.getCercaElementoAut();
		_datiElementoAut = _elementoAut.getCercaDatiAut();
		CercaSoggettoDescrittoreClassiReperType cerca = new CercaSoggettoDescrittoreClassiReperType();
        if (_datiElementoAut instanceof CercaSoggettoDescrittoreClassiReperType)
            _cercaTermine = (CercaSoggettoDescrittoreClassiReperType) _datiElementoAut;

		if (_datiElementoAut.getCanaliCercaDatiAut() != null){
			_T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
			stringaCerca = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca();
			if (stringaCerca != null)
				stringaLike = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
			if (stringaCerca != null)
				stringaEsatta = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
		}
		_T005_Range = _datiElementoAut.getT005_Range();
		if (_datiElementoAut.getLivelloAut_A() != null)
			_livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
		if (_datiElementoAut.getLivelloAut_Da() != null)
			_livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();


		if (_cercaTermine != null){
			if(_cercaTermine.getParoleAut() != null){
				paroleAut = _cercaTermine.getParoleAut();
	            _c2_250 = _cercaTermine.getC2_250();
			}
			if(_cercaTermine.getParoleDescrittoriPerDescrittori() != null){
				paroleTerminiPerTermini = _cercaTermine.getParoleDescrittoriPerDescrittori();
			}

		}
		arrivoLegame = _elementoAut.getArrivoLegame();
   }


   protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
       if (factoring_object.getTipoOrd()!=null)
		if ((factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_3.getType())||(factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_4.getType()))
			throw new EccezioneSbnDiagnostico(1231,"errore nell'ordinamento");
		verificaAbilitazioni();

//		if(_c2_250 != null)
//		{
//
//			DescrittoreValida descrittoreValida = new DescrittoreValida();
//			descrittoreValida.controllaVettoreParametriSemantici(_c2_250, this.cd_utente);
//		}
        int counter = 0;
        if (_T001 != null)
            counter++;
        if ((stringaLike != null) || (stringaEsatta != null))
            counter++;
        if (paroleAut != null && paroleAut.length > 0){
            counter++;
        }else{
        	paroleAut = null;
        }
        if (paroleTerminiPerTermini != null && paroleTerminiPerTermini.length > 0)
            counter++;

        if (arrivoLegame != null)
            counter++;

        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039,"comunicare uno e un solo canale di ricerca");
        if (_T001 != null)
            cercaTerminePerId();
        else if ((stringaLike != null)||(stringaEsatta != null))
        	cercaTerminePerStringaCerca();
        else if (paroleAut != null)
            cercaTerminePerParole();
        else if (paroleTerminiPerTermini != null && paroleTerminiPerTermini.length > 0)
            cercaTerminePerParoleTermine();
        else if (arrivoLegame != null)
            cercaThesauroPerLegame();

        if (! (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() || tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())) {
			log.error("Tipo output sconosciuto -" + tipoOut);
			throw new EccezioneSbnDiagnostico(3044);
		}

	}


   private void cercaThesauroPerLegame() throws IllegalArgumentException, InvocationTargetException, Exception {
		ArrivoLegame legame = _elementoAut.getArrivoLegame();
		if ((legame == null))
			return;
		if (legame.getLegameDoc() != null)
			cercaThesauroPerTitolo();
		else if (legame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE)
			cercaThesauroPerTermine();
	}



   private void cercaThesauroPerTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
       if (arrivoLegame == null) {
           log.error("Nessun titolo specificato nella ricerca");
           throw new EccezioneSbnDiagnostico(3041);
       }
       String idArrivo = null;
       if (arrivoLegame.getLegameDoc() != null)
           idArrivo = arrivoLegame.getLegameDoc().getIdArrivo();
       TermineThesauro termineThesauro = new TermineThesauro();
       //ANTONIO
       termineThesauro.valorizzaFiltri(_elementoAut.getCercaDatiAut());
       controllaNumeroRecord(termineThesauro.contaThesauroPerTitolo(idArrivo));
       List tempTableDao = null;
       tempTableDao = termineThesauro.cercaThesauroPerTitolo(idArrivo, tipoOrd);
       if (tempTableDao.size() == 0)
           throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
       for (int i = 0; i < tempTableDao.size(); i++)
           _TableDao_response.add(
        		   termineThesauro.cercaTerminePerId(((Vl_thesauro_tit) tempTableDao.get(i)).getDID()));
   }



   private void cercaThesauroPerTermine() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		if (arrivoLegame == null) {
			log.error("Nessun descrittore specificato nella ricerca");
			throw new EccezioneSbnDiagnostico(3041);
		}
		String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
		TerminiTermini terminiTermini = new TerminiTermini();
		// controllaNumeroRecord(termineThesauro.contaSoggettoPerDescrittore(idArrivo));
		List tempTableDao = null;
		tempTableDao = terminiTermini.cercaTermineTermine(idArrivo);
		TermineThesauro termineThesauro = new TermineThesauro();
		if (tempTableDao.size() == 0)
			throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
		 for (int i = 0; i < tempTableDao.size(); i++)
			 _TableDao_response.add(termineThesauro.cercaTerminePerId(((Vl_termini_termini) tempTableDao.get(i)).getDID()));

	}

   private void cercaTerminePerParoleTermine() throws IllegalArgumentException, InvocationTargetException, Exception {
	   TermineThesauro termine = new TermineThesauro();
	   termine.valorizzaFiltri(_datiElementoAut);
       List v = new ArrayList();
       if(paroleTerminiPerTermini.length == 1){
       	//cercaDidMultpli();
       	throw new EccezioneSbnDiagnostico(6666, "Il numero dei termini deve essere compreso tra 2 e 4 ");
       }
       for (int i = 0; i < paroleTerminiPerTermini.length; i++) {
           //é NECESSARIO VERIFICARE SE SONO IN STOP LIST ????????
           if (paroleTerminiPerTermini[i].length() > 0 && !ElencoParole.getInstance().contiene(paroleTerminiPerTermini[i]))
               v.add(paroleTerminiPerTermini[i]);
       }
       String[][] s = new String[v.size()+1][2];
       // DOVE pos 1 = Parola Descrittore
       // DOVE pos 2 = Numero di descrittori contenente la parola


       paroleTerminiPerTermini = new String[v.size()];
       int i = 0;
       String appoStringaTot = "";
       int conta =0;

       for (i = 0; i < v.size(); i++){
    	   paroleTerminiPerTermini[i] = (String) v.get(i);
    	   conta = termine.contaTerminePerParolaEsatta(paroleTerminiPerTermini[i]);
           s[i][0] = paroleTerminiPerTermini[i].toString();
           s[i][1] =  String.valueOf(conta);
           appoStringaTot = appoStringaTot + " " + paroleTerminiPerTermini[i].toString();
           log.debug("Trovati per descrittore=\"" + paroleTerminiPerTermini[i].toString()+ "\"  termini n°=" + conta);
       }
       String[] did_m = new String[4];
       did_m[0]="";
       did_m[1]="";
       did_m[2]="";
       did_m[3]="";
       for (int k = 0; k < paroleTerminiPerTermini.length; k++) {
    	   paroleTerminiPerTermini[k] = s[k][0];
       }
       //dovrebbe essere
       int contatot  =termine.contaTerminePerParoleNome(paroleTerminiPerTermini);
       //int contatot  =descrittore.contaDidMultpli(did_m[0],did_m[1],did_m[2],did_m[3],paroleDescrittoriPerDescrittori.length);

       log.debug("Trovati per descrittore=\"" + appoStringaTot + "\"  sogg n°=" + contatot);
       s[v.size()][0] = appoStringaTot;
       s[v.size()][1] =  String.valueOf(contatot);


       CountDesDes al = null;
       for (int j=0;j<=v.size();j++) {
       	al = new CountDesDes();
       	al.setParole_des(s[j][0]);
        al.setDes_count(Integer.parseInt(s[j][1]));
           _TableDao_response.add(j,al);
       }
       ricercaTerminiPerTermini = true;

   }

	/**
	 * Method cercaDescrittorePerParole.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaTerminePerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        TermineThesauro termine = new TermineThesauro();
        termine.valorizzaFiltri(_datiElementoAut);
        //almaviva5_20120209 #4854
        paroleAut = normalizzaParolePerRicerca(paroleAut, ElencoParole.getInstance());
        controllaNumeroRecord(termine.contaTerminePerParoleNome(paroleAut));
        tavola_response = termine.cercaTerminePerParoleNome(paroleAut, tipoOrd);
//		_TableDao_response = tavola_response.getElencoRisultati();

	}


	/**
	 * Method cercaDescrittorePerStringaCerca.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaTerminePerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        TermineThesauro termine = new TermineThesauro();
        termine.valorizzaFiltri(_datiElementoAut);
        if (stringaLike != null){
            stringaLike = NormalizzaNomi.normalizzazioneGenerica(stringaLike);
        	//ANTONIO aggiunta gestione del codice soggettario
            if(_c2_250 != null){
            	if (Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim()) == null){
            		throw new EccezioneSbnDiagnostico(3999, "Codice Thesauro errato");
            	}
            }
            if(_c2_250 != null){
            	controllaNumeroRecord(termine.contaTerminePerNomeLike(stringaLike,Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim())));
            	tavola_response = termine.cercaTerminePerNomeLike(stringaLike, tipoOrd,Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim()));
            }else{
    	        controllaNumeroRecord(termine.contaTerminePerNomeLike(stringaLike));
    			tavola_response = termine.cercaTerminePerNomeLike(stringaLike, tipoOrd);
            }

        } else if (stringaEsatta != null) {
            stringaEsatta = NormalizzaNomi.normalizzazioneGenerica(stringaEsatta);
        	//ANTONIO aggiunta gestione del codice soggettario
            if(_c2_250 != null){
            	if (Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim()) == null){
            		throw new EccezioneSbnDiagnostico(3999, "Codice Thesauro errato");
            	}
            }
            if(_c2_250 != null){
            	controllaNumeroRecord(termine.contaTerminePerNomeEsatto(stringaEsatta,Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim())));
            	tavola_response = termine.cercaTerminePerNomeEsatto(stringaEsatta, tipoOrd,Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", _c2_250.toUpperCase().trim()));
            }else{
            	controllaNumeroRecord(termine.contaTerminePerNomeEsatto(stringaEsatta));
            	tavola_response = termine.cercaTerminePerNomeEsatto(stringaEsatta, tipoOrd);
            }
//        	controllaNumeroRecord(termine.contaTerminePerNomeEsatto(stringaEsatta,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", _c2_250.toUpperCase().trim())));
//			tavola_response = termine.cercaTerminePerNomeEsatto(stringaEsatta, tipoOrd,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", _c2_250.toUpperCase().trim()));
        }
//		_TableDao_response = tavola_response.getElencoRisultati();

	}






	/**
	 * Method cercaDescrittorePerId.
	 * @throws InfrastructureException
	 */
	private void cercaTerminePerId() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
		TermineThesauro termine = new TermineThesauro();
        _tb_termine =  termine.cercaTerminePerId(_T001);
        if (_tb_termine == null)
        	throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
        _TableDao_response.add(_tb_termine);
	}


    protected SBNMarc preparaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
        if ((tavola_response == null) && (_TableDao_response == null)) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneSbnDiagnostico(201);
        }
        // Deve utilizzare il numero di richieste che servono
        List response = new ArrayList();
        if (tavola_response != null)
            response = tavola_response.getElencoRisultati(maxRighe);
        else
            response.addAll(_TableDao_response);
        if (response.size() == 0)
            response.addAll(_TableDao_response);

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
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_termine_thesauro tb_termine_thesauro;
        int totRighe = 0;
        if (ricercaPuntuale)
        	totRighe = 1;
        else
          	totRighe = numeroRecord;

        if (TableDao_response.size() > 0)
            totRighe = TableDao_response.size();
        FormatoTermineThesauro formatoTermine = new FormatoTermineThesauro();

	    if ((totRighe > 0) && ricercaTerminiPerTermini ) {
            //output.setTotRighe(TableDao_response.size());
            output.setTotRighe(numeroRecord);
	    	CountDesDesType countDesDesType = new CountDesDesType();
    		CountDesDes countDesDes = new CountDesDes();
	    	for(int i=0; i< TableDao_response.size(); i++){
	    		countDesDes = (CountDesDes) TableDao_response.get(i);
	    		DesType desType = new DesType();
	    		desType.setParolaDes(countDesDes.getParole_des());
	    		desType.setDesCount(countDesDes.getDes_count());
	    		countDesDesType.addDes(desType);
	    	}
	    	output.setCountDesDes(countDesDesType);
	    	totRighe=0;
	    	ricercaPuntuale =false;

            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
    	}else if ((totRighe > 0) || ricercaPuntuale) {
            output.setMaxRighe(maxRighe);
            //output.setTotRighe(totRighe);
            output.setTotRighe(numeroRecord);
            output.setNumPrimo(rowCounter+1);
            output.setTipoOrd(factoring_object.getTipoOrd());
            output.setTipoOutput(tipoOut);
	  		ElementAutType elemento = new ElementAutType();
            if (!ricercaPuntuale){
	            for (int i = 0; i < TableDao_response.size(); i++) {
//	            	elemento = new ElementAutType();
	            	tb_termine_thesauro = (Tb_termine_thesauro) TableDao_response.get(i);
                    int num_tit_coll_bib = formatoTermine.cercaNum_tit_coll_bib(tb_termine_thesauro.getDID(),user_object);
                    int num_tit_coll = formatoTermine.cercaNum_tit_coll(tb_termine_thesauro.getDID());
                    output.addElementoAut(formatoTermine.formattaTermine(tb_termine_thesauro, tipoOut,num_tit_coll_bib,num_tit_coll));

	            }
            }
	        else {
	        	elemento = new ElementAutType();
            	tb_termine_thesauro = (Tb_termine_thesauro) TableDao_response.get(0);
	        	int num_tit_coll_bib = formatoTermine.cercaNum_tit_coll_bib(tb_termine_thesauro.getDID(),user_object);
                int num_tit_coll = formatoTermine.cercaNum_tit_coll(tb_termine_thesauro.getDID());
                output.addElementoAut(formatoTermine.formattaTermine(_tb_termine, tipoOut,num_tit_coll_bib,num_tit_coll));

	        }
            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
        } else {
            result.setEsito("3001");
            //Esito non positivo: si potrebbe usare un'ecc.
            result.setTestoEsito("Nessun elemento trovato");
        }

//        output.setTotRighe(totRighe);
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
