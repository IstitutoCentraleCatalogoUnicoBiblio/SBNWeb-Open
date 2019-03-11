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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.FormatoSoggetto;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A600;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe CreaSoggetto
 * Factoring:
 * Crea un soggetto come richiesto dal messaggio xml, dopo aver controllato la
 * validità.
 * Cerca i descrittori e se non esistono li crea, crea i legami
 * soggetto-descrittore
 *
 * Tabelle coinvolte:
 * - Tb_soggetto; Tb_descrittore; Tr_sog_des
 * OggettiCoinvolti:
 * - Soggetto
 * - Descrittore
 * - SoggettoDescrittore
 *
 * Passi da eseguire:
 * Valida il soggetto
 * se non ok ritorna il msg response di diagnostica
 * Se ok inserisce il soggetto e i descrittori nel database; inserisce i legami
 * nel database; prepara il msg di response di output
 *
 * @author
 * @version 24-03-2003
 */
public class CreaSoggetto extends CreaElementoAutFactoring{

	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private BigDecimal 		_schemaVersion;
	private CreaTypeChoice 	datiTypeChoice;
	private String 			_cd_livello;
	private A250 				_t250;
	private String 			_t001;
	private Tb_soggetto 		_soggettoResponse;

	private SbnSimile 			_tipoControllo;

    private String  _condiviso;
    private A600    _t600;
    private String  _t601;
    private String  _t602;

    //almaviva5_20111122 evolutive CFI
    private SbnEdizioneSoggettario _edizione;


	private List<Tb_soggetto> _vettoreDiSimili = new ArrayList<Tb_soggetto>();
	private int _idErroreSimile;

	public CreaSoggetto(SBNMarc sbnmarcObj) {
   		super(sbnmarcObj);
  		_sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
   		_schemaVersion = _sbnmarcObj.getSchemaVersion();
		datiTypeChoice=_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
		_tipoControllo = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
		SoggettoType soggettoType= new SoggettoType();
		soggettoType = (SoggettoType) datiTypeChoice.getElementoAut().getDatiElementoAut();
		_t001 = soggettoType.getT001();
		_t250 = soggettoType.getT250();
		_cd_livello = soggettoType.getLivelloAut().toString();

        //almaviva5_20111122 evolutive CFI
        _edizione = _t250.getEdizione();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (soggettoType.getCondiviso() == null ) {
        	_condiviso = "n";
        }
        else if ((soggettoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((soggettoType.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
		// gestione campi aggiuntivi Nota soggetto e catt_sogg
        // se il campo è vuoto verifico che non sono in cattura
        // se sono in cattura setto di default 1 su catt_sogg altrimanti mando
        // in errore perchè il campo è obbligatorio

		if (soggettoType.getT600() != null){
			_t600 = soggettoType.getT600();
			if (soggettoType.getT600().getA_601() != null){
				_t601 = soggettoType.getT600().getA_601();
			}
			if (soggettoType.getT600().getA_602() != null){
				_t602 = soggettoType.getT600().getA_602();
			}
		}
		if ((_t601 == null) && (_cattura))
			_t601 = "1";

   }

   protected void executeCrea() throws
    IllegalArgumentException, InvocationTargetException, Exception {

		SoggettoValida sv = new SoggettoValida(_cd_livello);
		sv.validaCatt_sogg_Nota(_t601, _t602, _cattura);
		sv.controllaVettoreParametriSemantici(_t250.getC2_250(), this.cd_utente);
		//almaviva5_20101015 errore migrazione ART: il protocollo ricopre i CID esistenti se la
		//sequence sul DB (seq_tb_soggetto) non é correttamente inizializzata
		if (_t001.equals("0000000000")) {
			Progressivi prog = new Progressivi();
			_t001 = prog.getNextIdSoggetto();
		}
    	if (SbnSimile.SIMILEIMPORT.getType() == _tipoControllo.getType() || sv.validaPerCrea(_t001, getCdUtente(), _cattura)) {
    		String[] vettoreDescrittori = SoggettoValida.preparaDescrittori(_t250);
    		Soggetto soggetto = new Soggetto();
			//in cattura il controllo di duplicazione viene disattivato
			if (!_cattura) {
				if (SbnSimile.CONFERMA.getType() == _tipoControllo.getType())
					_vettoreDiSimili = sv.verificaSimiliConferma(null, _t250);
				else
					_vettoreDiSimili = soggetto.cercaSoggettiSimili(null, _t250, vettoreDescrittori);
			}
			int similiTrovati = ValidazioneDati.size(_vettoreDiSimili);
			if (SbnSimile.SIMILEIMPORT.getType() != _tipoControllo.getType()) {
                if (similiTrovati == 0) {
    			    //non ci sono simili, posso creare
					soggetto.creaSoggetto(_t001, _t250, _t601, _t602, getCdUtente(), _cd_livello, _condiviso);
    				//spacchetta i descrittori dal soggetto
    				//verifica l'esistenza dei descrittori per stringa esatta
					sv.controllaDescrittori(_t001, _t250, _t601, _condiviso, vettoreDescrittori, getCdUtente());
    				_soggettoResponse = soggetto.cercaSoggettoPerId(_t001);

               }
                //trovato un simile con edizione diversa
                if (similiTrovati > 0) {
                	String edizione = _edizione != null ? _edizione.toString() : null;
                	Tb_soggetto simile = ValidazioneDati.first(_vettoreDiSimili);
                	if (!ValidazioneDati.equals(edizione, simile.getCD_EDIZIONE()) )
                		//edizione diversa
                		_idErroreSimile = 3344;
                	else
                		//trovati simili
                		_idErroreSimile = 3004;
                }

			} else
				if (similiTrovati == 0)
					throw new EccezioneSbnDiagnostico(3001);

			object_response = formattaOutput();
    	}
    }

	private SBNMarc formattaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		result.setEsito("0000");
		if (ValidazioneDati.isFilled(_vettoreDiSimili))
			result = FormatoErrore.buildSbnResult(_idErroreSimile);
		else
			result.setTestoEsito("OK");

		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(_sbnUser);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);
		int size = 1;
		if (_vettoreDiSimili.size() > 1)
			size = _vettoreDiSimili.size();
		FormatoSoggetto formatoSoggetto = new FormatoSoggetto();
		LegamiType[] legamiType;
		output.setNumPrimo(1);
		output.setTotRighe(size);
		if (_vettoreDiSimili.size() > 0) {
			for (int i = 0; i < size; i++) {
				Tb_soggetto tb_soggetto = new Tb_soggetto();
				tb_soggetto = _vettoreDiSimili.get(i);
				ElementAutType elemento = new ElementAutType();
				int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(tb_soggetto.getCID(), user_object);
				int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(tb_soggetto.getCID());
		    	Descrittore d = new Descrittore();
		    	List dd = d.cercaDescrittorePerSoggetto(tb_soggetto.getCID());
				SoggettoType soggetto = formatoSoggetto.formattaSoggettoPerLista(tb_soggetto, num_tit_coll_bib, num_tit_coll, dd);
				elemento.setDatiElementoAut(soggetto);
				output.addElementoAut(elemento);
			}
		} else if (_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE) {
			SbnTipoOutput _tipoOut = SbnTipoOutput.VALUE_0;
			ElementAutType elemento = new ElementAutType();
			int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(_soggettoResponse.getCID(), user_object);
			int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(_soggettoResponse.getCID());
	    	Descrittore d = new Descrittore();
	    	List dd = d.cercaDescrittorePerSoggetto(_soggettoResponse.getCID());
			elemento.setDatiElementoAut(formatoSoggetto.formattaSoggetto(_soggettoResponse, _tipoOut, num_tit_coll_bib, num_tit_coll, dd));
			output.addElementoAut(elemento);
			if (_tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
				legamiType = formatoSoggetto.formattaSoggettoConLegamiDescrittore(_soggettoResponse);
				if (legamiType != null)
					elemento.setLegamiElementoAut(legamiType);
			}
		}

		output.setTipoOutput(SbnTipoOutput.VALUE_0);
		return sbnmarc;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CREA_SOGGETTO_1258;
    }

}
