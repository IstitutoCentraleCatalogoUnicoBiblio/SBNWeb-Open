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
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.DescrittoreDescrittore;
import it.finsiel.sbn.polo.oggetti.estensione.DescrittoreValida;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author
 *
 */
public class ModificaDescrittore extends ModificaElementoAutFactoring {

	private ElementAutType 	_elementoAut;
	private ModificaType 		_modificaType;
	private DatiElementoType 	_datiElementoAut;
	private StatoRecord 		_statoRecord;
	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private SbnSimile 			_tipoControllo;
	private String 			_t001;
	private String 			_t005;
	private A931 				_t931;
	private String 			_a931;
	private String 			_b931;
	private SbnFormaNome 		_tipoForma;
	private String 			_cd_livello;
	private LegamiType[] 		_legamiElementoAut;

	private LegamiType[] _arrivoLegame;

	private TimestampHash _timeHash;
    private String  _condiviso;
	private SbnEdizioneSoggettario _edizione;
	private String _cat_termine;

	public ModificaDescrittore(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		DatiElementoType datiElemento = null;
		_sbnmarcObj = sbnmarcObj;
        _sbnUser = sbnmarcObj.getSbnUser();
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_elementoAut =  _modificaType.getElementoAut();
		_datiElementoAut = _elementoAut.getDatiElementoAut();
        _tipoControllo = _modificaType.getTipoControllo();
        datiElemento = _modificaType.getElementoAut().getDatiElementoAut();
        _t001 = datiElemento.getT001();
        DescrittoreType descrittoreType = (DescrittoreType) _modificaType.getElementoAut().getDatiElementoAut();
        _t005 = descrittoreType.getT005();
        _t931 = descrittoreType.getT931();
        if (_t931 != null) {
        	_a931 = _t931.getA_931();
        	_b931 = _t931.getB_931();
        	_edizione = _t931.getEdizione();
        	_cat_termine = _t931.getCat_termine();
        }
		_tipoForma = descrittoreType.getFormaNome();
        _cd_livello = descrittoreType.getLivelloAut().toString();
        _elementoAut = _modificaType.getElementoAut();
		_legamiElementoAut = _elementoAut.getLegamiElementoAut();
		_arrivoLegame = _elementoAut.getLegamiElementoAut();
		_statoRecord = descrittoreType.getStatoRecord();
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
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiElemento.getCondiviso() != null && (datiElemento.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiElemento.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }
	}

    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

        DescrittoreValida descrittoreValida = new DescrittoreValida();
        _timeHash = new TimestampHash();
		Tb_descrittore tb_descrittore = descrittoreValida.validaPerModifica(
				_t001, _cd_livello, _t005, getCdUtente(), _t931, _cattura,
				_edizione);
        //almaviva5_20091117
		String cd = Decodificatore.getCd_unimarc("Tb_descrittore", "cd_soggettario", tb_descrittore.getCD_SOGGETTARIO().trim());
		descrittoreValida.controllaVettoreParametriSemantici(cd, this.cd_utente);
        //almaviva5_20090719
        tb_descrittore.setCD_LIVELLO(_cd_livello);
		_timeHash.putTimestamp("Tb_descrittore",_t001, new SbnDatavar( tb_descrittore.getTS_VAR()));
      	Descrittore descrittore = new Descrittore();

        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE) {
			// significa che devo modificare la nota o la descrizione
			if (ValidazioneDati.isFilled(_a931) ) {
				// almaviva5_20090423 controllo se è cambiata la chiave d'ordinamento
				// e se esiste già un descrittore simile in base dati.
				String oldKyDescr = tb_descrittore.getKY_NORM_DESCRITT();
				String newKyDescr = ChiaviSoggetto.normalizzaDescrittore(_a931);

				if (!ValidazioneDati.equals(newKyDescr, oldKyDescr) && !controllaEsistenzaDescrittore(_a931))
					throw new EccezioneSbnDiagnostico(3212,	"Descrittore con codice soggettario gia presente in base dati");

				tb_descrittore = descrittore.gestisceDescrizione(_a931, tb_descrittore);
			}
			if (_b931 != null)
				tb_descrittore.setNOTA_DESCRITTORE(_b931);

			// almaviva5_20111130 evolutive CFI
			if (_edizione != null) {
				boolean edModified = !ValidazioneDati.equals(tb_descrittore.getCD_EDIZIONE(), _edizione.toString());
				if (edModified)
					controllaEdizioneDescrittoriLegati(tb_descrittore.getDID());

				tb_descrittore.setCD_EDIZIONE(_edizione.toString() );
			}
			tb_descrittore.setCAT_TERMINE(_cat_termine);
		}

	    TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
	    timbroCondivisione.modificaTimbroCondivisione(tb_descrittore, getCdUtente(), _condiviso);
		descrittore.aggiornaDescrittore(tb_descrittore, getCdUtente());
		descrittoreValida.validaPerModificaLegame(_elementoAut, _timeHash);
		for (int i = 0; i < _arrivoLegame.length; i++) {
			for (int j = 0; j < _arrivoLegame[i].getArrivoLegame().length; j++) {
				DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
				switch (_arrivoLegame[i].getTipoOperazione().getType()) {
				case SbnTipoOperazione.CREA_TYPE:
					descrittoreDes.inserisciLegame(_t001, _arrivoLegame[i], getCdUtente());
					break;

				case SbnTipoOperazione.CANCELLA_TYPE:
					descrittoreDes.cancellaTr_des_des(_t001, _arrivoLegame[i], getCdUtente(), _timeHash);
					break;

				case SbnTipoOperazione.MODIFICA_TYPE:
					descrittoreDes.modificaInTr_des_des(_t001, _arrivoLegame[i], getCdUtente());
					break;
				}
			}
		}
		object_response = formattaOutput();
    }

    private void controllaEdizioneDescrittoriLegati(String did) throws Exception {
    	Descrittore descrittore = new Descrittore();
    	DescrittoreDescrittore dd = new DescrittoreDescrittore();
    	Set<String> checked = new HashSet<String>();	//legami già controllati
		List<Tr_des_des> legami = dd.cercaLegame(did);
		for (Tr_des_des legame : legami) {
			String idArrivo = ValidazioneDati.equals(legame.getDID_BASE(), did) ? legame.getDID_COLL() : legame.getDID_BASE();
			if (checked.contains(idArrivo))
				continue;

			Tb_descrittore des_legato = descrittore.cercaDescrittorePerId(idArrivo);
			descrittore.controllaEdizioneDescrittore(_edizione, des_legato, getCdUtente() );
			checked.add(idArrivo);
		}

	}

	private boolean controllaEsistenzaDescrittore(String descr)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		Descrittore descrittore = new Descrittore();
		TableDao dao = null;

		if (ValidazioneDati.isFilled(descr)) {
			// ANTONIO aggiunta gestione del codice soggettario
			String stringaEsatta = ChiaviSoggetto.normalizzaDescrittore(descr);
			dao = descrittore.cercaDescrittorePerNomeEsatto(stringaEsatta,
					null, Decodificatore.getCd_tabella("Tb_descrittore",
							"cd_soggettario", _t931.getC2_931().toUpperCase().trim()));
		} else
			throw new EccezioneSbnDiagnostico(3049); //dati incompleti

		return (dao.getElencoRisultati().size() == 0);
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
		Tb_descrittore descrittoreModificato = new Tb_descrittore();
		Descrittore descrittore = new Descrittore();
		descrittoreModificato = descrittore.cercaDescrittorePerId(_t001);
        DatiElementoType datiResp= new DatiElementoType();
        datiResp.setT001(descrittoreModificato.getDID());
        SbnDatavar data = new SbnDatavar(descrittoreModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(elementoAut.getDatiElementoAut().getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.DE);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
	}


}
