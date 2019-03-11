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
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoLuogo;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioLuogo;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoLuogoGest;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoValida;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.polo.orm.Tr_rep_luo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A260;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe ModificaLuogo
 * Factoring:
 * Modifica un luogo come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Aggiorna gli eventuali legami luogo-luogo
 *
 * Tabelle coinvolte:
 * - Tb_luogo; Tr_luo_luo
 *
 * OggettiCoinvolti:
 * - Luogo
 * - LuogoLuogo
 *
 * Passi da eseguire:
 * Valida il luogo: controllo di esistenza per identificativo, controllo su
 * timestamp, controllo su livello di autorità dell'utente, controllo sul livello
 * di autorità del luogo.
 * Se viene modificato il nome e tipoControllo <>'Conferma': controllo esistenza
 * luogo con nuovo nome (ricerca per nome): se esiste si comunica diagnostico:
 * luogo esistente.
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok e se esistono informazioni di ArrivoLegame:
 * valida legami luogo-luogo secondo tipoOperazione, che può essere crea,
 * cancella,scambio forma; se crea valgono gli stessi controlli della creazione
 * luogo. La cancellazione imposta flag_canc = 'S' su tr_luo_luo e se si tratta di
 * legame 'vedi' anche sul tb_luogo con forma 'R'.
 * Lo scambio forma può essere effettuato su un legame 'vedi': il luogo in forma
 * accettata va scambiato con il luogo in forma R, mantenendo il LID precedente.
 * non ok  ritorna il msg response di diagnostica
 * Se ok modifica il luogo nel database; modifica i legami nel database; prepara
 * il msg di response di output
 *
 * @author
 * @version
 */
public class ModificaLuogo extends ModificaElementoAutFactoring{
	private SbnTipoOutput _tipoOut;
    private SbnUserType _sbnUser;
    private BigDecimal _schemaVersion = null;
    private ElementAutType _elementoAut;
    private DatiElementoType _datiElementoAut;
    private String _t001 = null;

	private List _TableDao_response = new ArrayList();
    private ModificaType _modificaType;
    private LegamiType[] _arrivoLegame;
    private A260 _t260 = null;
    private String _livelloAut;
    private SbnFormaNome _formaNome;
    private LegamiType[] legami;

	private String _polo;
    private A300 _t300;
    private A830 _t830;
    private StatoRecord _statorecord;
    private boolean esistenzaSimili = false;
    private TimestampHash _timeHash;
 	Tb_luogo luogoModificato;

	/**
	 * Constructor ModificaLuogo.
	 * @param sbnmarcObj
	 */
	public ModificaLuogo(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		//_sbnUser = root_object.getSbnUser();
		// Inizio evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi nota informativa ,
		// nota catalogatore e legame a repertori
		// Si asterisca il preesistente per inserire la nuova gestione presa di Indice
//		_elementoAut =  sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getElementoAut();
//		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
//		_datiElementoAut = _elementoAut.getDatiElementoAut();
//		_sbnUser = sbnmarcObj.getSbnUser();
//		_polo = _sbnUser.getBiblioteca().substring(0,3);
//		_schemaVersion = sbnmarcObj.getSchemaVersion();
//		_t001 = _datiElementoAut.getT001();
//		_t260 = ((LuogoType)_datiElementoAut).getT260();
//		_livelloAut = _datiElementoAut.getLivelloAut().toString();
//		_formaNome = _datiElementoAut.getFormaNome();
//
//
//		_t300 = ((LuogoType)_datiElementoAut).getT300();
//		_arrivoLegame = this._modificaType.getElementoAut().getLegamiElementoAut();
//		_statorecord = ((LuogoType)_datiElementoAut).getStatoRecord();

		_elementoAut =  sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getElementoAut();
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_datiElementoAut = _elementoAut.getDatiElementoAut();
		_sbnUser = sbnmarcObj.getSbnUser();
		_polo = _sbnUser.getBiblioteca().substring(0,3);
		_schemaVersion = sbnmarcObj.getSchemaVersion();
		_t001 = _datiElementoAut.getT001();
		_livelloAut = _datiElementoAut.getLivelloAut().toString();
		_formaNome = _datiElementoAut.getFormaNome();
		_arrivoLegame = this._modificaType.getElementoAut().getLegamiElementoAut();
		_statorecord = _datiElementoAut.getStatoRecord();
        legami = _modificaType.getElementoAut().getLegamiElementoAut();
		if (_modificaType.getElementoAut().getDatiElementoAut() instanceof LuogoType) {
			_statorecord = ((LuogoType)_datiElementoAut).getStatoRecord();
			_t260 = ((LuogoType)_datiElementoAut).getT260();
			_t300 = ((LuogoType)_datiElementoAut).getT300();
			_t830 = ((LuogoType)_datiElementoAut).getT830();
		}

	}


	public SBNMarc formattaOutput(String testo, String esito) throws IllegalArgumentException, InvocationTargetException, Exception{
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		result.setEsito(esito);
		result.setTestoEsito(testo);
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(_schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        int size;
        if (_TableDao_response != null)
        	size = _TableDao_response.size();
        else size =0;
        Tb_luogo tb_luogo;
		FormatoLuogo formatoLuogo = new FormatoLuogo();
        for (int i = 0; i < size; i++) {
            tb_luogo = (Tb_luogo) _TableDao_response.get(i);
            output.addElementoAut(formatoLuogo.formattaRinvii(tb_luogo));
        }
        output.setMaxRighe(size);
        output.setNumPrimo(1);
        output.setTotRighe(size);
        output.setTipoOutput(_tipoOut);

        return sbnmarc;
	}




	/**
	 * Method executeCerca.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	protected void executeModifica() throws  IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception {
		// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi nota informativa ,
		// nota catalogatore e legame a repertori
		// Le modifiche vengono riprese dal sofware dell'indice
		String idArrivo,tipoLegame,idPartenza;
		String nota = null;
		String notaCat = null;
		String testo = "OK";
        String codice = "0000";
        _timeHash = new TimestampHash();
		//in validaPerModifica c'è il controllo anche sul tag t260
		LuogoValida luogoValida = new LuogoValida(_modificaType);
		_TableDao_response = luogoValida.validaPerModifica(getCdUtente(),_livelloAut,_timeHash,_cattura);
		if ((_statorecord == null) && (_elementoAut.getLegamiElementoAutCount()==0))
			throw new EccezioneSbnDiagnostico(3090,"nessuna modifica richiesta");
		if (_TableDao_response != null && _TableDao_response.size()>0){
			esistenzaSimili = true;
		 	testo = "sono stati trovati luoghi simili";
            codice = "3004";
		}

		// Inizio evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi nota informativa ,
		// nota catalogatore e legame a repertori
		// Si asterisca il preesistente per inserire la nuova gestione presa di Indice
//		if (!esistenzaSimili){
//			if (_t300 != null)
//				nota = _t300.getA_300();
//			if ((_statorecord != null) && (_statorecord.getType() == StatoRecord.C_TYPE))
//				modificaDati();
//			if (_elementoAut.getLegamiElementoAutCount()>0){
//				LuogoLuogoGest luogoLuogoGest = new LuogoLuogoGest();
//				idPartenza 	= _t001;
//				Tb_luogo luogo = luogoValida.cercaLuogoPerID(idPartenza);
//				for (int i=0;i<_arrivoLegame.length; i++){
//					for (int j=0;j<_arrivoLegame[i].getArrivoLegame().length;j++){
//						idArrivo = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut().getIdArrivo().toString();
//						if(!luogoLuogoGest.validaPerModificaLegame(idPartenza, getCdUtente(),_arrivoLegame[i].getArrivoLegame(j),_arrivoLegame[i].getTipoOperazione(),_timeHash,_cattura))
//						  throw new EccezioneSbnDiagnostico(3034);
//						eseguiModificaLuoLuo(i, j, luogo, luogoLuogoGest);
//					}
//				}
//
//			}
//		}

		if (!esistenzaSimili){
			if (_t300 != null)
				nota = _t300.getA_300();
			if (_t830 != null)
				notaCat = _t830.getA_830();
			if ((_statorecord != null) && (_statorecord.equals(StatoRecord.C)))
				modificaDati();
			if (_elementoAut.getLegamiElementoAutCount()>0){
				LuogoLuogoGest luogoLuogoGest = new LuogoLuogoGest();
				idPartenza 	= _t001;
				Tb_luogo luogo = luogoValida.cercaLuogoPerID(idPartenza);
				for (int i=0;i<_arrivoLegame.length; i++){
					for (int j=0;j<_arrivoLegame[i].getArrivoLegame().length;j++){
//almaviva4 evolutiva luoghi - legame a repertorio
			        LegameElementoAutType legameElementoAutType =
			                        legami[i].getArrivoLegame(j).getLegameElementoAut();
			        if (legameElementoAutType.getTipoAuthority().equals(SbnAuthority.valueOf("RE"))) {
			            RepertorioLuogo repLuo = new RepertorioLuogo();
			            Tr_rep_luo tr_rep_luo = new Tr_rep_luo();
			            tr_rep_luo.setUTE_VAR(getCdUtente());
			            tr_rep_luo.setLID(luogo.getLID());
			            Repertorio rep = new Repertorio();
			            // almaviva INSERITO CONTROLLO SU ESISTENZA REPERTORIO
			            if (rep.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo()) == null){
			                throw new EccezioneSbnDiagnostico(3023, "Repertorio " + legameElementoAutType.getIdArrivo() + " inesistente");
			            }
			            // END
			            Tb_repertorio tb_repertorio = rep.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
			            tr_rep_luo.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
			            //cancellazione, inserimento o modifica
			            if (legami[i].getTipoOperazione().equals(SbnTipoOperazione.CANCELLA)) {
			            	// mdf eliminato perchè non gestito in modalità cancellazione(concorrenza)
//			                tr_rep_luo.setTS_VAR(_timeHash.getTimestamp("Tr_rep_luo",
//			                                    tr_rep_luo.getLID() + tr_rep_luo.getID_REPERTORIO()));
			                            repLuo.cancellaLuogoRepertorio(tr_rep_luo);
			            } else {
                        tr_rep_luo.setNOTA_REP_LUO(legameElementoAutType.getNoteLegame());
                        	if (legami[i].getTipoOperazione().equals(SbnTipoOperazione.CREA)) {
                        		tr_rep_luo.setUTE_INS(getCdUtente());
                        		repLuo.inserisciRepertorioLuogo(tr_rep_luo);
                        	} else if (legami[i].getTipoOperazione().equals(SbnTipoOperazione.MODIFICA)) {
			               repLuo.modificaLuogoRepertorio(tr_rep_luo);
                        	}
			            }
			          } else {

//almaviva4 evolutiva luoghi - legame a repertorio fine

						idArrivo = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut().getIdArrivo().toString();
						if(!luogoLuogoGest.validaPerModificaLegame(idPartenza, getCdUtente(),_arrivoLegame[i].getArrivoLegame(j),_arrivoLegame[i].getTipoOperazione(),_timeHash,_cattura))
						  throw new EccezioneSbnDiagnostico(3034);
						eseguiModificaLuoLuo(i, j, luogo, luogoLuogoGest);
						luogoModificato = luogo;
//				        Luogo luo = new Luogo(db_conn);
//		                legameElementoAutType = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut();
//		                idArrivo = legameElementoAutType.getIdArrivo();
//		                SbnTipoOperazione tipoOperazione = _arrivoLegame[i].getTipoOperazione();
//		                if (tipoOperazione == SbnTipoOperazione.CREA) {
//		                    //controllo che il legame non esista già
//		                    luo.creaLegame(legameElementoAutType, idPartenza, getCdUtente());
//		                } else if (tipoOperazione == SbnTipoOperazione.CANCELLA) {
//		                    //controllo che i legami da cancellare esistano e mi prendo il timehash
//		                    luogoValida.validaPerCancellaLegame(legameElementoAutType, idPartenza, _timeHash);
//		                    luo.cancellaLegame(legameElementoAutType, idPartenza, getCdUtente(), _timeHash);
//		                } else if (tipoOperazione == SbnTipoOperazione.MODIFICA) {
//		                    //controllo che i legami da cancellare esistano e mi prendo il timehash
//		                    luo.modificaLegame(legameElementoAutType, idPartenza, getCdUtente());
//		                } else {
//		                    throw new EccezioneFactoring(3035, "tipo operazione sbagliato");
//		                }
					}
				}

			}
		}
	}
		// Fine evolutive ottobre 2015 almaviva2

		object_response = formattaOutput(testo,codice);
	}

	/**
	 * Method modificaDati.
	 * @throws InfrastructureException
	 */
	private void modificaDati() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
   		Luogo luogo = new Luogo();
   		Tb_luogo tb_luogo;
		tb_luogo = luogo.cercaLuogoPerID(_t001);


		// BUG mantis 5270 collaudo - Maggio 2013 - Il paesa non è obbligatoria nel modifica/crea dell'authority luogo
		// viene eliminata la forzatura e inserita la valorizzazione corretta
		if (_t260 != null) {
			tb_luogo.setCD_PAESE(_t260.getA_260());
	   		tb_luogo.setDS_LUOGO(_t260.getD_260());
	   		tb_luogo.setKY_NORM_LUOGO(NormalizzaNomi.normalizzazioneGenerica(_t260.getD_260()));
		}
	   	if (_t300 !=null)
	   		tb_luogo.setNOTA_LUOGO(_t300.getA_300());

	 // evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di
	 // gestire i campi nota informativa , nota catalogatore e legame a repertori
	   	if (_t830 !=null)
   		    tb_luogo.setNOTA_CATALOGATORE(_t830.getA_830());

      tb_luogo.setCD_LIVELLO(_livelloAut);

   		luogo.updateLuogo(tb_luogo);
   		tb_luogo = luogo.cercaLuogoPerID(_t001);
   		_TableDao_response = new ArrayList();
   		_TableDao_response.add(tb_luogo);
	}

   	public String getIdAttivita() {
		if (controllaScambioForma())
			return CodiciAttivita.getIstance().SCAMBIO_FORMA_1029;
		else return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().MODIFICA_LUOGO_1267;
    }

	private boolean controllaScambioForma(){
		boolean trovato = false;
		int i=0;
		while ((!trovato)&&(i<_arrivoLegame.length)){
			for (int j=0;j<_arrivoLegame[i].getArrivoLegame().length;j++){
				if (_arrivoLegame[i].getTipoOperazione() == SbnTipoOperazione.SCAMBIOFORMA){
					trovato = true;
				}
				i++;
			}
		}
		return trovato;
	}


  private void eseguiModificaLuoLuo(
      int i,
      int j,
      Tb_luogo autore,
      LuogoLuogo autAut)
      throws IllegalArgumentException, InvocationTargetException, Exception {
      LegameElementoAutType legameElementoAutType = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut();
      String idArrivo = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut().getIdArrivo();
      String idPartenza = _elementoAut.getLegamiElementoAut(i).getIdPartenza();
      //oppure autore.getVID()
      String tipoLegame = legameElementoAutType.getTipoLegame().toString();
      if (_arrivoLegame[i].getTipoOperazione().getType() == SbnTipoOperazione.SCAMBIOFORMA_TYPE) {
          autAut.scambioFormaLuoghi(idPartenza, idArrivo, getCdUtente());
          //updateLuogoEseguita = true;
      } else {
          if (_arrivoLegame[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
              /* valgono gli stessi controlli della creazione autore */
              Tr_luo_luo aut_aut = new Tr_luo_luo();
              aut_aut.setFL_CANC("N");
              aut_aut.setUTE_INS(getCdUtente());
              aut_aut.setUTE_VAR(getCdUtente());
              if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                  aut_aut.setLID_BASE(autore.getLID());
                  aut_aut.setLID_COLL(idArrivo);
                  aut_aut.setTP_LEGAME("4");
              } else if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                  aut_aut.setTP_LEGAME("8");
                  if (autore.getTP_FORMA().equals("A")) {
                      aut_aut.setLID_BASE(autore.getLID());
                      aut_aut.setLID_COLL(idArrivo);
                  } else {
                      aut_aut.setLID_COLL(autore.getLID());
                      aut_aut.setLID_BASE(idArrivo);
                      //[TODO VERIFICARE: deve aggiornare anche l'autore R.(ts_var e ute_var) e flag
                      Luogo autoreDB = new Luogo();
                      autoreDB.updateVersione(idArrivo, getCdUtente());
                  }
              }
              autAut.inserisciTrLuoLuo(aut_aut);
          } else if (_arrivoLegame[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA.getType()) {
              /* La cancellazione imposta flag_canc = 'S' su tr_aut_aut e se si tratta di
               * legame 'vedi' anche sul tb_autore con forma 'R'.
               */
              autAut.cancellaLegame(getCdUtente(), autore.getLID(), idArrivo, _timeHash);
              Luogo autoreDB = new Luogo();
              if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                  if (autore.getTP_FORMA().equals("R")) {
                      // MODIFICA(per gestire gli autori R legati per errore a più forme A):
                      // se l'autore ha un altro legame ad un A non lo cancello.
                      if (autAut.cercaLegami(autore.getLID(), "8").size() == 0) {
                          autoreDB.cercaRinviiLuogo(autore.getLID(), getCdUtente());
                          autore.setFL_CANC("S");
                      }
                      autoreDB.updateVersione(idArrivo, getCdUtente());
                  } else {
                      // MODIFICA(per gestire gli autori R legati per errore a più forme A):
                      // se l'autore ha un altro legame ad un A non lo cancello.
                      if (autAut.cercaLegami(idArrivo, "8").size() == 0) {
                          Tb_luogo autoreR = autoreDB.cercaLuogoPerID(idArrivo);
                          autoreDB.cancellaLuogo(idArrivo, getCdUtente(), _timeHash);
                          autoreR.setFL_CANC("S");
                      }
                  }
              }

          } else if (_arrivoLegame[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE) {
              //posso modificare solo la nota di un legame.: non avendo nota, non modifico nulla
        // autAut.updateModifica(
        // idPartenza,
        // idArrivo,
        // _timeHash.getTimestamp("Tr_aut_aut", idPartenza + idArrivo),
        //                  getCdUtente());

          } else {
              throw new EccezioneSbnDiagnostico(3035);
          }
          //Aggiorno ts_var e ute_var nel secondo autore.(il primo dovrebbe
          //essere modificato una volta solo)
          Luogo autoreDB = new Luogo();
          autoreDB.updateVersione(idArrivo, autore.getUTE_VAR());
      }
  }

}
