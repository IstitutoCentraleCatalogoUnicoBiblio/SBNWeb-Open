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
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.TermineClasse;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.TerminiTermini;
import it.finsiel.sbn.polo.oggetti.estensione.TermineValida;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 *
 */
public class ModificaTermineThesauro extends ModificaElementoAutFactoring{

	private ElementAutType 		_elementoAut;
	private ModificaType 		_modificaType;
	private StatoRecord 		_statoRecord;
	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private String 				_t001;
	private String 				_t005;
	private A935 				_t935;
	private String 				_a935;
	private String 				_b935;
	private String 				_c2_935;
	private SbnFormaNome 		_tipoForma;
	private String 				_cd_livello;
	private LegamiType[] 		_legamiElementoAut;

	private LegamiType[] _arrivoLegame;

	private TimestampHash _timeHash;
	private List _vettoreDiSimili = new ArrayList();
    private SbnSimile           _tipoControllo;
    private String  _condiviso;
    private boolean updateTermineEseguita = false;

	public ModificaTermineThesauro(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		DatiElementoType datiElemento = null;
		_sbnmarcObj = sbnmarcObj;
        _sbnUser = sbnmarcObj.getSbnUser();
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_elementoAut =  _modificaType.getElementoAut();
        _tipoControllo = _modificaType.getTipoControllo();
        datiElemento = _modificaType.getElementoAut().getDatiElementoAut();
        _t001 = datiElemento.getT001();
        TermineType termineType = new TermineType();
        termineType = (TermineType) _modificaType.getElementoAut().getDatiElementoAut();
        _t005 = termineType.getT005();
        _t935 = termineType.getT935();
        if (_t935 != null){
        	_a935 = _t935.getA_935();
        	_b935 = _t935.getB_935();
        	_c2_935 = _t935.getC2_935();
        }
		_tipoForma = termineType.getFormaNome();
        _cd_livello = termineType.getLivelloAut().toString();
        _elementoAut = _modificaType.getElementoAut();
		_legamiElementoAut = _elementoAut.getLegamiElementoAut();
		_arrivoLegame = _elementoAut.getLegamiElementoAut();
		_statoRecord = termineType.getStatoRecord();
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElemento.getCondiviso() == null ){
        	_condiviso = "n";
        }
        else if ((datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
	}

    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

    	Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
    	TermineValida termineValida = new TermineValida();
        _timeHash = new TimestampHash();
        tb_termine_thesauro = termineValida.validaPerModifica(_t001,_cd_livello,_t005,getCdUtente(),_t935,_cattura);
		_timeHash.putTimestamp("Tb_termine_thesauro",_t001,new SbnDatavar( tb_termine_thesauro.getTS_VAR()));
		if(_t935 != null)
			termineValida.cercaSimili(_t001,_cd_livello,_t005,getCdUtente(),_t935,_cattura);
		TermineThesauro termineThesauro = new TermineThesauro();
		 if ( (_statoRecord != null) && (_statoRecord.getType() != StatoRecord.C_TYPE) && (_arrivoLegame != null))
             throw new EccezioneSbnDiagnostico(3090,"non è stata inoltrata alcuna richiesta di modifica");
//        if (_statoRecord != null)  // almaviva INSERITO VERIFICARE PERCHE'
//        if (_statoRecord.equals(StatoRecord.C))
        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE) {
        	//significa che devo modificare la nota o la descrizione
			if ((_a935 != null) && (!_a935.equals(""))){
				tb_termine_thesauro.setDS_TERMINE_THESAURO(_a935);
				tb_termine_thesauro.setKY_TERMINE_THESAURO(NormalizzaNomi.normalizzazioneGenerica(_a935));
			}

	        if (_b935 != null)
	        	tb_termine_thesauro.setNOTA_TERMINE_THESAURO(_b935);
        }

        if (!ValidazioneDati.isFilled(_c2_935))
        	_c2_935 = tb_termine_thesauro.getCD_THE();

	    TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
	    timbroCondivisione.modificaTimbroCondivisione(tb_termine_thesauro,getCdUtente(),_condiviso);

	    termineThesauro.aggiornaTermineThesauro(tb_termine_thesauro,getCdUtente());

	    termineValida.validaPerModificaLegame(_elementoAut,_timeHash);
		gestioneLegamiTermineTermine(tb_termine_thesauro, _elementoAut,_timeHash);
		gestioneLegamiTermineClasse(_elementoAut,_timeHash);

		object_response = formattaOutput();
    }

	private void gestioneLegamiTermineClasse(ElementAutType _elementoAut,
			TimestampHash _timeHash) throws EccezioneDB, InfrastructureException,
			InvocationTargetException, Exception {

		TermineClasse tc = new TermineClasse();

		for (LegamiType legami : _arrivoLegame) {
			for (ArrivoLegame legame : legami.getArrivoLegame()) {
				LegameElementoAutType autLegato = legame.getLegameElementoAut();
				//almaviva5_20111014 classe o thesauro??
				if (autLegato.getTipoAuthority().getType() != SbnAuthority.CL_TYPE)
					continue;

				SbnTipoOperazione tipoOperazione = legami.getTipoOperazione();
				switch (tipoOperazione.getType()) {
				case SbnTipoOperazione.CREA_TYPE:
					tc.inserisciLegameTermineClasse(_t001, _c2_935, autLegato, getCdUtente(), _timeHash);
					break;

				case SbnTipoOperazione.CANCELLA_TYPE:
					tc.cancellaLegameTermineClasse(_t001, _c2_935, autLegato, getCdUtente(), _timeHash);
					tc.rankLegameTermineClasse(_t001, _c2_935, autLegato, getCdUtente(), tipoOperazione, 0);
					break;

				case SbnTipoOperazione.MODIFICA_TYPE:
					int rankModificato = tc.modificaLegameTermineClasse(_t001, _c2_935, autLegato, getCdUtente(), _timeHash);
					if (rankModificato != 0)
						tc.rankLegameTermineClasse(_t001, _c2_935, autLegato, getCdUtente(), tipoOperazione, rankModificato);
					break;

				default:
					throw new EccezioneSbnDiagnostico(3035); //op. non prevista
				}
			}
		}
	}


	private void gestioneLegamiTermineTermine(Tb_termine_thesauro tb_termine_thesauro, ElementAutType elementAutType, TimestampHash timeHash)
			throws EccezioneDB, InfrastructureException,
			InvocationTargetException, Exception {
		TerminiTermini tt = new TerminiTermini();

		for (int i = 0; i < _arrivoLegame.length; i++) {

			for (int j = 0; j < _arrivoLegame[i].getArrivoLegame().length; j++) {
				LegameElementoAutType autLegato = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut();
				//almaviva5_20111014 classe o thesauro??
				if (autLegato.getTipoAuthority().getType() != SbnAuthority.TH_TYPE)
					continue;

				String idArrivo = autLegato.getIdArrivo();

				if (_arrivoLegame[i].getTipoOperazione() == SbnTipoOperazione.CREA) {
					tt.inserisciInTr_termini_termini(_t001,
							_arrivoLegame[i], getCdUtente());
				} else if (_arrivoLegame[i].getTipoOperazione() == SbnTipoOperazione.CANCELLA) {
					tt.cancellaTr_termini_termini(_t001,
							_arrivoLegame[i], getCdUtente(), _timeHash);
				} else if (_arrivoLegame[i].getTipoOperazione() == SbnTipoOperazione.MODIFICA) {
					tt.modificaInTr_termini_termini(_t001,
							_arrivoLegame[i], getCdUtente());
					// // Scambio forma non previsto per i descrittori
					// 14/02/2008
				} else if (_arrivoLegame[i].getTipoOperazione() == SbnTipoOperazione.SCAMBIOFORMA) {
					tt.scambioForma(tb_termine_thesauro, idArrivo,
							getCdUtente(), _timeHash);
					updateTermineEseguita = true;
					// flagAll.setTb_autore_scambio(true);
					// terminiTermini.scambioForma(_t001,,getCdUtente(),_timeHash);
				}
			}
		}
	}


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
	    result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(_sbnmarcObj.getSchemaVersion());
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_termine_thesauro tb_termine_thesauroModificato = new Tb_termine_thesauro();
        TermineThesauro termineThesauro = new TermineThesauro();
		tb_termine_thesauroModificato = termineThesauro.cercaTerminePerId(_t001);
        DatiElementoType datiResp= new DatiElementoType();
        datiResp.setT001(tb_termine_thesauroModificato.getDID());
        SbnDatavar data = new SbnDatavar(tb_termine_thesauroModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(elementoAut.getDatiElementoAut().getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.TH);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().MODIFICA_THESAURO_1301;
	}


}
