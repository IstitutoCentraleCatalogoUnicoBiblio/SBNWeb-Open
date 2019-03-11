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
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.base.FormatoDescrittore;
import it.finsiel.sbn.polo.factoring.util.CountDesDes;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.DescrittoreDescrittore;
import it.finsiel.sbn.polo.oggetti.estensione.DescrittoreValida;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CountDesDesType;
import it.iccu.sbn.ejb.model.unimarcmodel.DesType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;




/**
 * Contiene le funzionalità di ricerca per l'entità Descrittore
 * restituisce la lista sintetica o analitica
 *
 * Possibili parametri di ricerca:
 * Identificativo: T001
 * parole del descrittore: paroleAut
 * descriittore esatto: stringa esatta
 * descriittore parte iniziale: stringaLike
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
 * - Tb_descrittore, Tr_des_des
 * OggettiCoinvolti:
 * - Descrittore, DescrittoreDescrittore
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
public class CercaDescrittore extends CercaElementoAutFactoring {

	private CercaElementoAutType 						_elementoAut;
	private CercaDatiAutType 							_datiElementoAut;
	private CercaSoggettoDescrittoreClassiReperType 	_cercaSoggetto;
	private String 									_T001;
	private String 									stringaLike;
	private String 									stringaEsatta;
	private SbnRangeDate 								_T005_Range;
	private String 									_livelloAutA;
	private String 									_livelloAutDA;
	private String[] 									paroleAut;
	private String 									_c2_250;
	private Tb_descrittore								_tb_descrittore = new Tb_descrittore();
	private boolean 									ricercaPuntuale = false;
	//private TableDao 									_TableDao_response;
	private List _TableDao_response = new ArrayList();
	private String[] paroleDescrittoriPerDescrittori = null;
	private boolean ricercaDescrittoriPerDescrittori = false;
	private boolean filtroGestione;
	private ArrivoLegame arrivoLegame = null;
	private SbnEdizioneSoggettario _edizione;

	public CercaDescrittore(SBNMarc input_root_object){
		super(input_root_object);
		CercaType 			_cerca;
		StringaCercaType 	stringaCerca;
		_cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_elementoAut =  _cerca.getCercaElementoAut();
		_datiElementoAut = _elementoAut.getCercaDatiAut();

		//almaviva5_20091117 obbligatorio in polo
		filtroGestione = true;
		//almaviva5_20101221 #4068
		arrivoLegame = _elementoAut.getArrivoLegame();

        if (_datiElementoAut instanceof CercaSoggettoDescrittoreClassiReperType)
            _cercaSoggetto = (CercaSoggettoDescrittoreClassiReperType) _datiElementoAut;

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


		if (_cercaSoggetto != null){
			if(_cercaSoggetto.getParoleAut() != null){
				paroleAut = _cercaSoggetto.getParoleAut();
	            _c2_250 = _cercaSoggetto.getC2_250();
	            _edizione = _cercaSoggetto.getEdizione();
			}
			if(_cercaSoggetto.getParoleDescrittoriPerDescrittori() != null){
				paroleDescrittoriPerDescrittori = _cercaSoggetto.getParoleDescrittoriPerDescrittori();
			}

		}

   }


   protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
       if (factoring_object.getTipoOrd()!=null)
		if ((factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_3.getType())||(factoring_object.getTipoOrd().getType() == SbnTipoOrd.VALUE_4.getType()))
			throw new EccezioneSbnDiagnostico(1231,"errore nell'ordinamento");
		verificaAbilitazioni();

		if(_c2_250 != null)
		{
			DescrittoreValida descrittoreValida = new DescrittoreValida();
			descrittoreValida.controllaVettoreParametriSemantici(_c2_250, this.cd_utente);
		}

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

        //almaviva5_20101221 #4068
        if (arrivoLegame != null)
        	counter++;

        if (paroleDescrittoriPerDescrittori != null && paroleDescrittoriPerDescrittori.length > 0)
            counter++;
        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039,"comunicare uno e un solo canale di ricerca");
        if (_T001 != null)
            cercaDescrittorePerId();
        else if ((stringaLike != null)||(stringaEsatta != null))
            cercaDescrittorePerStringaCerca();
        else if (paroleAut != null)
            cercaDescrittorePerParole();
        else if (paroleDescrittoriPerDescrittori != null && paroleDescrittoriPerDescrittori.length > 0)
            cercaDescrittoriPerParoleDescrittori();
        else if (arrivoLegame != null)
        	cercaDescrittoriPerLegame();

		if (! (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() || tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())) {
			log.error("Tipo output sconosciuto -" + tipoOut);
			throw new EccezioneSbnDiagnostico(3044);
		}

	}



	private void cercaDescrittoriPerLegame() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		if (arrivoLegame == null) {
			log.error("Nessun descrittore specificato nella ricerca");
			throw new EccezioneSbnDiagnostico(3041);
		}
		String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
		DescrittoreDescrittore desDes = new DescrittoreDescrittore();
		//controllaNumeroRecord(desDes.contaDescrittorePerLegamiDescrittore(idArrivo));
		tavola_response = desDes.cercaDescrittorePerLegamiDescrittore(idArrivo);
		List tempTableDao = tavola_response.getElencoRisultati();
		controllaNumeroRecord(ValidazioneDati.size(tempTableDao));
		if (!ValidazioneDati.isFilled(tempTableDao) )
			throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");

	}


   private void cercaDescrittoriPerParoleDescrittori() throws IllegalArgumentException, InvocationTargetException, Exception {
       Descrittore descrittore = new Descrittore();
       //almaviva5_20120510 evolutive CFI
       descrittore.settaParametro(TableDao.XXXcd_edizione_IN, _edizione);
		// almaviva5_20091117 solo soggettari visibili dalla biblioteca
		if (filtroGestione)
			descrittore.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib,	user_object);
       descrittore.valorizzaFiltri(_datiElementoAut);
       List v = new ArrayList();
       if(paroleDescrittoriPerDescrittori.length == 1){
       	//cercaDidMultpli();
       	throw new EccezioneSbnDiagnostico(6666, "Il numero dei descrittori deve essere compreso tra 2 e 4 ");
       }
       for (int i = 0; i < paroleDescrittoriPerDescrittori.length; i++) {
           //é NECESSARIO VERIFICARE SE SONO IN STOP LIST ????????
           if (paroleDescrittoriPerDescrittori[i].length() > 0 && !ElencoParole.getInstance().contiene(paroleDescrittoriPerDescrittori[i]))
               v.add(paroleDescrittoriPerDescrittori[i]);
       }
       String[][] s = new String[v.size()+1][2];
       // DOVE pos 1 = Parola Descrittore
       // DOVE pos 2 = Numero di descrittori contenente la parola


       paroleDescrittoriPerDescrittori = new String[v.size()];
       int i = 0;
       String appoStringaTot = "";
       int conta =0;

       for (i = 0; i < v.size(); i++){
    	   paroleDescrittoriPerDescrittori[i] = (String) v.get(i);
    	   conta = descrittore.contaDescrittorePerParolaEsatta(paroleDescrittoriPerDescrittori[i]);
           s[i][0] = paroleDescrittoriPerDescrittori[i].toString();
           s[i][1] =  String.valueOf(conta);
           appoStringaTot = appoStringaTot + " " + paroleDescrittoriPerDescrittori[i].toString();
           log.debug("Trovati per descrittore=\"" + paroleDescrittoriPerDescrittori[i].toString()+ "\"  descrittori n°=" + conta);
       }
       String[] did_m = new String[4];
       did_m[0]="";
       did_m[1]="";
       did_m[2]="";
       did_m[3]="";
       for (int k = 0; k < paroleDescrittoriPerDescrittori.length; k++) {
    	   paroleDescrittoriPerDescrittori[k] = s[k][0];
       }
       //dovrebbe essere
       int contatot  =descrittore.contaDescrittorePerParoleNome(paroleDescrittoriPerDescrittori);
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

       ricercaDescrittoriPerDescrittori = true;

   }

	/**
	 * Method cercaDescrittorePerParole.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaDescrittorePerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        Descrittore descrittore = new Descrittore();
        //almaviva5_20120510 evolutive CFI
        descrittore.settaParametro(TableDao.XXXcd_edizione_IN, _edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	descrittore.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        descrittore.valorizzaFiltri(_datiElementoAut);

        //almaviva5_20090605
        paroleAut = normalizzaParolePerRicerca(paroleAut, ElencoParole.getInstance());

        controllaNumeroRecord(descrittore.contaDescrittorePerParoleNome(paroleAut));
        tavola_response = descrittore.cercaDescrittorePerParoleNome(paroleAut, tipoOrd);
//		_TableDao_response = tavola_response.getElencoRisultati();

	}


	/**
	 * Method cercaDescrittorePerStringaCerca.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cercaDescrittorePerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Descrittore descrittore = new Descrittore();
        //almaviva5_20120510 evolutive CFI
        descrittore.settaParametro(TableDao.XXXcd_edizione_IN, _edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	descrittore.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        descrittore.valorizzaFiltri(_datiElementoAut);
        if (stringaLike != null){
            stringaLike = ChiaviSoggetto.normalizzaDescrittore(stringaLike);
	        controllaNumeroRecord(descrittore.contaDescrittorePerNomeLike(stringaLike));
			tavola_response = descrittore.cercaDescrittorePerNomeLike(stringaLike, tipoOrd);
        } else if (stringaEsatta != null) {
            stringaEsatta = ChiaviSoggetto.normalizzaDescrittore(stringaEsatta);
        	//ANTONIO aggiunta gestione del codice soggettario
            if(_c2_250 != null){
            	controllaNumeroRecord(descrittore.contaDescrittorePerNomeEsatto(stringaEsatta,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", _c2_250.toUpperCase().trim())));
            	tavola_response = descrittore.cercaDescrittorePerNomeEsatto(stringaEsatta, tipoOrd,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", _c2_250.toUpperCase().trim()));
            }else{
            	controllaNumeroRecord(descrittore.contaDescrittorePerNomeEsatto(stringaEsatta,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", null)));
            	tavola_response = descrittore.cercaDescrittorePerNomeEsatto(stringaEsatta, tipoOrd,Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", null));
            }
        }
//		_TableDao_response = tavola_response.getElencoRisultati();

	}


	/**
	 * Method cercaDescrittorePerId.
	 * @throws InfrastructureException
	 */
	private void cercaDescrittorePerId() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Descrittore descrittore = new Descrittore();
        //almaviva5_20120510 evolutive CFI
        descrittore.settaParametro(TableDao.XXXcd_edizione_IN, _edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	descrittore.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        _tb_descrittore =  descrittore.cercaDescrittorePerId(_T001);
        if (_tb_descrittore == null)
        	throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
	}


    protected SBNMarc preparaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
		if ((tavola_response == null) && (_TableDao_response == null)
				&& (!ricercaPuntuale)) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
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
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
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
		Tb_descrittore tb_descrittore;
		int totRighe = 0;
		if (ricercaPuntuale)
			totRighe = 1;
		else
			totRighe = numeroRecord;

		if (TableDao_response.size() > 0)
			totRighe = TableDao_response.size();
		FormatoDescrittore formatoDescrittore = new FormatoDescrittore();

		if ((totRighe > 0) && ricercaDescrittoriPerDescrittori) {
			output.setTotRighe(numeroRecord);
			CountDesDesType countDesDesType = new CountDesDesType();
			CountDesDes countDesDes = new CountDesDes();
			for (int i = 0; i < TableDao_response.size(); i++) {
				countDesDes = (CountDesDes) TableDao_response.get(i);
				DesType desType = new DesType();
				desType.setParolaDes(countDesDes.getParole_des());
				desType.setDesCount(countDesDes.getDes_count());
				countDesDesType.addDes(desType);
			}
			totRighe = 0;
			ricercaPuntuale = false;

			result.setEsito("0000"); // Esito positivo
			result.setTestoEsito("OK");

			output.setCountDesDes(countDesDesType);

		} else if ((totRighe > 0) || ricercaPuntuale) {
			output.setMaxRighe(maxRighe);
			output.setTotRighe(Math.max(numeroRecord, totRighe) );
			// output.setTotRighe(totRighe);
			output.setNumPrimo(rowCounter + 1);
			output.setTipoOrd(factoring_object.getTipoOrd());
			output.setTipoOutput(tipoOut);
			if (!ricercaPuntuale) {
				for (int i = 0; i < TableDao_response.size(); i++) {
					tb_descrittore = (Tb_descrittore) TableDao_response.get(i);
					output.addElementoAut(formatoDescrittore.formattaDescrittore(tb_descrittore, tipoOut));
				}
			} else {
				output.addElementoAut(formatoDescrittore.formattaDescrittore(_tb_descrittore, tipoOut));

			}
			result.setEsito("0000"); // Esito positivo
			result.setTestoEsito("OK");


		} else {
			result.setEsito("3001");
			// Esito non positivo: si potrebbe usare un'ecc.
			result.setTestoEsito("Nessun elemento trovato");
		}

		// output.setTotRighe(totRighe);
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
