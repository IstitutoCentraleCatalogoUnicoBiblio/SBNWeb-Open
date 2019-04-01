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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_luogoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_luo_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.EccezioneSbnMessage;
import it.finsiel.sbn.polo.factoring.base.FormatoLuogo;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.oggetti.RepertorioLuogo;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoLuogoGest;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoValida;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




/**
 * Classe CreaLuogo<BR>
 * <p>
 * Factoring:<BR>
 * Crea un luogo come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Crea gli eventuali legami luogo-luogo
 * <BR>
 * <BR>
 * Tabelle coinvolte:<BR>
 * - Tb_luogo; Tr_luo_luo<BR>
 * <BR>
 * OggettiCoinvolti:<BR>
 * - Luogo<BR>
 * - LuogoLuogo<BR>
 * <BR>
 * Passi da eseguire:<BR>
 * Valida il luogo.
 * se non ok ritorna il msg response di diagnostica
 * se ok e se esistono informazioni di ArrivoLegame:valida legami luogo-luogo; se
 * non ok  ritorna il msg
 * response di diagnostica
 * Se ok inserisce il luogo nel database; inserisce i legami nel database; prepara
 * il msg di response di output
 * <BR>
 * </p>
 * @author
 * @version
 */
public class CreaLuogo extends CreaElementoAutFactoring {

	private String 		livello_response;
	private String 		stato_response;
	private String 		genere1_response;
	private String 		livello_codifica;
	private String 		bid_response;
	private String 		datavar_response;
	private String 		datains_response;
	private SbnUserType 	sbnUser = null;
	private List 		_TableDao_response = new ArrayList();
    private BigDecimal 	_schemaVersion = null;
	private int			_numPrimo;
	private SBNMarc		_sbnmarcObj;
	private CreaType		_creaType;
	private ElementAutType _elementoAut;
	private String 		_polo;
	private SbnSimile 		_tipoControllo;
	private String 		_t001;
	private CreaTypeChoice datiTypeChoice;
	private SbnFormaNome tipoForma;
    private String tipoLegame;
    private String idArrivo;
    private String ds_luogo,cd_livello;
    private String nota = null;
    private String notaCat = null;
    private String cd_paese = null;
    private String user;
    private String biblioteca;
    private String testo;



	public CreaLuogo(SBNMarc sbnmarcObj) throws  EccezioneDB, Exception{
		super(sbnmarcObj);
		_sbnmarcObj = sbnmarcObj;
		_polo = _sbnmarcObj.getSbnUser().getBiblioteca().substring(0,3);
		biblioteca = _sbnmarcObj.getSbnUser().getBiblioteca().substring(3,6);
		_creaType = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea();
        sbnUser = _sbnmarcObj.getSbnUser();
        _elementoAut = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut();
   		_schemaVersion = _sbnmarcObj.getSchemaVersion();
		_tipoControllo = _creaType.getTipoControllo();
		datiTypeChoice=_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
		_t001 = datiTypeChoice.getElementoAut().getDatiElementoAut().getT001();
	}


    /**
     * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
     * opportuno.
     * gestisce il tipo di risposta richiesto (lista o esame) e produce il
     * file xml di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    protected void executeCrea() throws IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception {
		int contaLegami;
		boolean transazioneEseguita=false;
		boolean ignoraID;
		boolean reper = false;

        // almaviva qui controllo se la forma di rinvio che si vuole inserire in cattura già non sia presete
        // nel db in caso positivo tolto i flag di cancellazione e invio un messaggio di avvenuto ripristino
        if(formaRinvioEsistente()){
            throw new EccezioneSbnMessage(3336);
        }




		LuogoType luogoType= new LuogoType();
		testo="ok";

		tipoForma = datiTypeChoice.getElementoAut().getDatiElementoAut().getFormaNome();
		cd_livello = datiTypeChoice.getElementoAut().getDatiElementoAut().getLivelloAut().toString();
		luogoType = (LuogoType)datiTypeChoice.getElementoAut().getDatiElementoAut();
		ds_luogo = null;
		if (luogoType.getT260() != null){
			ds_luogo = luogoType.getT260().getD_260();
			// MODIFICA RICHIESTA DA ROSSANA IN CASO DI IMPORT DA INDICE E IN CASO IL CODICE PAESE NON VALORIZZATO
			// IL CLIENT INVIA SPAZIO. TESTO LA CONDIZIONE E INSERISCO CODICE UN (PAESE INDETERMINATO)
            if (luogoType.getT260().getA_260() != null){
            		cd_paese = Decodificatore.getCd_tabella("Tb_luogo","cd_paese",luogoType.getT260().getA_260().toUpperCase());
            	if (cd_paese == null){
            		cd_paese = "UN";
            	}
            }
		}
		// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
		// nota informativa , nota catalogatore e legame a repertori
		if (luogoType.getT300() != null)
			nota = luogoType.getT300().getA_300();
		if (luogoType.getT830() != null)
			notaCat = luogoType.getT830().getA_830();

		contaLegami=_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut().getLegamiElementoAutCount();
		//se non esiste formaNome assummiamo che di default sia = a "A"
		if (tipoForma == null)
			tipoForma = SbnFormaNome.A;
		//se formaNome = a R allora controllo che esistano legami nel file xml altrimenti è inutile continuare

		if (tipoForma.getType() == SbnFormaNome.R_TYPE && (contaLegami==0)){
			throw new EccezioneSbnDiagnostico(3025);
		}
    	//controllo la validità del luogo
   		LuogoValida validaLuogo = new LuogoValida(_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea());
		if (SbnSimile.SIMILEIMPORT.getType() == _tipoControllo.getType()){
			ignoraID = true;
			_TableDao_response = validaLuogo.validaPerCrea(getCdUtente(),ignoraID,_cattura);
			if (_TableDao_response.size() == 0)
				throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		}else{
			ignoraID = _t001.equals(SBNMARC_DEFAULT_ID);
			_TableDao_response = validaLuogo.validaPerCrea(getCdUtente(),ignoraID,_cattura);
		}
		//controllo che non ci siano luoghi simili (se tipocontrollo<>conferma) e che non esiste ID
    	if ((_TableDao_response.size() == 0) &&
	    	(_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE)){
			if (ignoraID) {
				Progressivi progress = new Progressivi();
				_t001 = progress.getNextIdLuogo();
			}

			// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
			// nota informativa , nota catalogatore e legame a repertori
			//controllo se esistono Legami con repertorio
			if (_elementoAut.getLegamiElementoAutCount()>0) {
				int size 	= _elementoAut.getLegamiElementoAutCount();
				int i = 0;
				int j;
				ArrivoLegame[] 					arrivoLegame;
				int 							sizeArrivoLegame;
				LegamiType[]					legamiType;
				legamiType 	= _elementoAut.getLegamiElementoAut();
				for (i = 0; i < size; i++) {
					arrivoLegame=legamiType[i].getArrivoLegame();
					sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
			        for (j = 0; j < sizeArrivoLegame; j++) {
			        	if (arrivoLegame[j].getLegameElementoAut().getTipoAuthority().equals(SbnAuthority.RE))
			        		reper = true;
			        }
				}
			}
	   		//controllo se esistono informazioni ArrivoLegami
    		//if (_elementoAut.getLegamiElementoAutCount()>0){
			// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
			// nota informativa , nota catalogatore e legame a repertor
			if (_elementoAut.getLegamiElementoAutCount()>0 && !reper){
    			aggiornaLegami(_t001,tipoForma, cd_paese,cd_livello, ds_luogo,nota, datiTypeChoice ,notaCat);
    		}
			else {

				// Ottobre 2018 - almaviva2 - prima di inserire un luogo si
				// deve verificare che questo
				// non sia già presente anche se cancellato logicamente; nel
				// qual caso si deve aggiornare
				// il record esistente
				Luogo luogo = new Luogo();

				Tb_luogo tb_luogo2 = validaLuogo.verificaEsistenzaID();
				if (tb_luogo2 != null && tb_luogo2.getFL_CANC().equals("S")) {
					// Luogo cancellato logicamente in polo e condiviso con indice
					tb_luogo2.setFL_CANC(" ");
					tb_luogo2.setUTE_VAR(user);
					tb_luogo2.setDS_LUOGO(luogoType.getT260().getD_260());
					luogo.updateLuogo(tb_luogo2);
				} else {
					ds_luogo = luogoType.getT260().getD_260();
					luogo.inserisceLuogo(_t001, _polo, getCdUtente(), tipoForma, cd_paese, cd_livello, ds_luogo, nota, notaCat);
				}

				// evolutive ottobre 2015 almaviva2 - Nella gestione dei
				// luoghi viene data la possibilità di gestire i campi
				// nota informativa , nota catalogatore e legame a repertori
				// almaviva4 evolutiva luoghi - legame a repertorio 04/05/2015
				// Luogo luogo = new Luogo(db_conn);
				int size = _elementoAut.getLegamiElementoAutCount();
				int i = 0;
				int j;
				ArrivoLegame[] arrivoLegame;
				int sizeArrivoLegame;
				LegamiType[] legamiType;
				legamiType = _elementoAut.getLegamiElementoAut();
				for (i = 0; i < size; i++) {
					arrivoLegame = legamiType[i].getArrivoLegame();
					sizeArrivoLegame = legamiType[i].getArrivoLegameCount();
					for (j = 0; j < sizeArrivoLegame; j++) {
						RepertorioLuogo repLuo = new RepertorioLuogo();
						repLuo.validaPerCreaLegame(_t001, arrivoLegame[j], null);
						luogo.creaLegame(arrivoLegame[j].getLegameElementoAut(), _t001, getCdUtente());
					}
				}
				// almaviva4 evolutiva luoghi - legame a repertorio fine 04/05/2015
			}
    	}
		object_response = formattaOutput();
    }

	private Tb_luogo leggiLuogoInserito() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException{
		Luogo luogo = new Luogo();
   		return luogo.cercaLuogoPerID(_t001);
	}

	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
	// nota informativa , nota catalogatore e legame a repertor
    private void aggiornaLegami(
    String t001,
    SbnFormaNome tipoForma,
    String cd_paese,
    String cd_livello,
    String ds_luogo,
    String nota,
    CreaTypeChoice datiTypeChoice,
    String notaCat) throws IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception{
   		LuogoLuogoGest gestioneLuogoLuogo= new LuogoLuogoGest();
   		//valido tutti i legami
   		ElementAutType elementoAutType;
   		elementoAutType = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut();
   		if (gestioneLuogoLuogo.validaPerCreaLegame(elementoAutType)){
   			Luogo luogo = new Luogo();
   			if (tipoForma == null) tipoForma = SbnFormaNome.A;
   			luogo.inserisceLuogo(t001,_polo,getCdUtente(),tipoForma,cd_paese,cd_livello,ds_luogo,nota, notaCat);
			String idArrivo = null;
			String tipoLegame = null;
			LuogoLuogo luogoLuogo = new LuogoLuogo();
            for (int i =0;i<datiTypeChoice.getElementoAut().getLegamiElementoAutCount();i++) {
                for (int j = 0;j<datiTypeChoice.getElementoAut().getLegamiElementoAut(i).getArrivoLegameCount();j++){
    				tipoLegame = datiTypeChoice.getElementoAut().getLegamiElementoAut(i).getArrivoLegame(j).getLegameElementoAut().getTipoLegame().toString();
    				idArrivo = 	datiTypeChoice.getElementoAut().getLegamiElementoAut(i).getArrivoLegame(j).getLegameElementoAut().getIdArrivo().toString();
       				luogoLuogo.inserisceInTr_luo_luo(getCdUtente(),t001,tipoForma ,tipoLegame,idArrivo);
                }
            }
   		}
   		else
   			throw new EccezioneSbnDiagnostico(3034);
    }


   /**
    * metodo che compone il messaggio di risposta alla creazione di un luogo.
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
 * @throws IndexOutOfBoundsException
    */
       private SBNMarc formattaOutput() throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000");
        int size = _TableDao_response.size();
        if (size >0){
        	result.setTestoEsito("sono stati trovato luoghi simili");
   	        result.setEsito("3004");
        }else result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        if (size == 0)
        	size = 1;
        Tb_luogo tb_luogo = new Tb_luogo();
        FormatoLuogo formatoLuogo = new FormatoLuogo();
		if (_TableDao_response.size() > 0 ){
			for (int i=0; i<_TableDao_response.size();i++){
	            tb_luogo = (Tb_luogo) _TableDao_response.get(i);
	            output.addElementoAut(formatoLuogo.formattaLuogoOutput(tb_luogo));
			}
		}else{
			tb_luogo = leggiLuogoInserito();
	        DatiElementoType datiResp = new DatiElementoType();
	        datiResp.setT001(tb_luogo.getLID());
            SbnDatavar data = new SbnDatavar(tb_luogo.getTS_VAR());
    	    datiResp.setT005(data.getT005Date());
    	    datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_luogo.getCD_LIVELLO())));
			datiResp.setTipoAuthority(SbnAuthority.LU);
			ElementAutType elementoAutResp = new ElementAutType();
			elementoAutResp.setDatiElementoAut(datiResp);
			output.addElementoAut(elementoAutResp);
//			elementoAut[0] = formatoLuogo.formattaLuogoOutput(tb_luogo);
		}
        output.setMaxRighe(size);
        output.setNumPrimo(1);
        output.setTipoOrd(SbnTipoOrd.VALUE_0);
        output.setTotRighe(size);
        output.setTipoOutput(SbnTipoOutput.VALUE_1);

        return sbnmarc;
    }



   public String getIdAttivita() {
		if (_cattura) {// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA ABILITATO A CREARE UN DOCUMENTO
    		// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
        	// serve solo il Cerca + Localizza per Gestione
    		return CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009;
    		// return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    		// Fine Intervento google3
		} else {
			return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
		}
	}

	public String getIdAttivitaSt() {
		if (_cattura) {// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA
						// ABILITATO A CREARE UN DOCUMENTO
			return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
		} else {
			return CodiciAttivita.getIstance().CREA_LUOGO_1260;
		}
	}



    public boolean formaRinvioEsistente() throws IllegalArgumentException, InvocationTargetException, Exception{
        boolean trovato= false;

		cd_livello = datiTypeChoice.getElementoAut().getDatiElementoAut().getLivelloAut().toString();

        if( (_cattura) && ( datiTypeChoice.getElementoAut().getDatiElementoAut().getFormaNome().toString().equals("R")) ){
            if (factoring_object.getCreaTypeChoice().getElementoAut().getLegamiElementoAutCount() > 0) {
                for (int i = 0; i < factoring_object.getCreaTypeChoice().getElementoAut().getLegamiElementoAut().length; i++) {
                    LegamiType legame = factoring_object.getCreaTypeChoice().getElementoAut().getLegamiElementoAut()[i];
                    for (int j = 0; j < legame.getArrivoLegameCount(); j++) {
                        LegameElementoAutType legEl = legame.getArrivoLegame(j).getLegameElementoAut();
                        if(legEl.getTipoLegame().toString().equals("4XX")){
                            if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE){
                                LuogoValida luogoValida = new LuogoValida(factoring_object);
                                Tb_luogo tb_luogo = costruisciLuogo(_t001,_polo,biblioteca,tipoForma,cd_paese,cd_livello,ds_luogo,nota);
                                tb_luogo = luogoValida.verificaEsistenzaID();
                                if ((tb_luogo != null) && (tb_luogo.getFL_CANC().equals("S"))) {
                                	tb_luogo.setFL_CANC(" ");
                                    Tb_luogoResult tb_luogoResult = new Tb_luogoResult(tb_luogo);
                                    tb_luogoResult.update(tb_luogo);
                                    //ritorno un diagnostico
                                    log.debug("Autore in forma rinvio già esiste è necessario abilitarlo");
                                    trovato = true;
                                }
                                LuogoLuogo luogoLuogo = new LuogoLuogo();
                                Tr_luo_luo luoLuo = luogoLuogo.verificaEsistenza(legEl.getIdArrivo().toString(),_t001);
                                if ((luoLuo != null) && (luoLuo.getFL_CANC().equals("S"))) {
                                	luoLuo.setFL_CANC(" ");
                                    Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(luoLuo);
                                    tr_luo_luoResult.UpdateAbilitaFormaRinvioEsistente(luoLuo);
                                    log.debug("legame luo_luogo già esiste è necessario abilitarlo");
                                    trovato = true;
                                }
                                //trovato = true;
                                    //ritorno un diagnostico
                            }
                        }
                    }
                }
            }
        }
        // NON è una forma di rinvio
        return trovato;
    }
    public Tb_luogo costruisciLuogo(String id_luogo, String polo, String user,
			SbnFormaNome tipoForma, String cd_paese, String cd_livello,
			String ds_luogo, String nota) throws EccezioneDB,
			EccezioneSbnDiagnostico, InfrastructureException {
		Tb_luogo tb_luogo = new Tb_luogo();
		tb_luogo.setCD_LIVELLO(cd_livello);
		if (ds_luogo != null) {
			tb_luogo.setDS_LUOGO(ds_luogo);
			tb_luogo.setKY_NORM_LUOGO(NormalizzaNomi
					.normalizzazioneGenerica(ds_luogo));
		}
		tb_luogo.setLID(id_luogo);
		tb_luogo.setCD_PAESE(cd_paese);
		tb_luogo.setKY_LUOGO(" ");
		tb_luogo.setNOTA_LUOGO(nota);
		if (tipoForma == null)
			tb_luogo.setTP_FORMA(" ");
		else
			tb_luogo.setTP_FORMA(tipoForma.toString());
		tb_luogo.setUTE_INS(user);
		tb_luogo.setUTE_VAR(user);
		tb_luogo.setFL_CANC(" ");
		return tb_luogo;

}


}
