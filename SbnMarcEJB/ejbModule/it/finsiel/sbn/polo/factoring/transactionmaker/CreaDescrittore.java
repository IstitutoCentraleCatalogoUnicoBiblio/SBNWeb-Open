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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.DescrittoreDescrittore;
import it.finsiel.sbn.polo.oggetti.estensione.DescrittoreValida;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
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
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;


/**
 * Classe CreaDescrittore<BR>
 * <p>
 * Factoring:<BR>
 * Crea un descrittore come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Crea gli eventuali legami descrittore-descrittore
 * <BR>
 * <BR>
 * Tabelle coinvolte:<BR>
 * - Tb_descrittore; Tr_des_des<BR>
 * <BR>
 * OggettiCoinvolti:<BR>
 * - Descrittore<BR>
 * - DescrittoreDescrittore<BR>
 * <BR>
 * Passi da eseguire:<BR>
 * Valida il descrittore: controllo che l'utente sia abilitato alla gestione
 * descrittore; controllo che sia abilitato alla gestione del soggettario,
 * controllo che non esista già un descrittore con uguale k_norm_descritt.
 * se non ok ritorna il msg response di diagnostica
 * se ok e se esistono informazioni di ArrivoLegame:valida legami
 * descrittore-descrittore:
 * il descrittore deve esistere in db, se tipo legame=USE il descrittore che si
 * sta creando deve avere forma R e quello di arrivo forma A, se tipo legame=UF il
 * descrittore che si sta creando deve avere forma A e quello di arrivo forma R;
 * in tutti gli altri casi i descrittori devono avere entrambi forma 'A'.
 * non ok  ritorna il msg response di diagnostica
 * Se ok inserisce il descrittore nel database; inserisce i legami nel database:
 * si creano i legami reciproci: si crea il legame con il tipo legame in input e
 * il legame invertendo did_base e did_coll e il tipo legame: USE-UF, RT=RT,NT-BT;
 * prepara il msg di response di output
 * <BR>
 * </p>
 * @author
 * @version
 */

public class CreaDescrittore extends CreaElementoAutFactoring {

	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private BigDecimal 		_schemaVersion;
	private CreaTypeChoice 	_datiTypeChoice;
	private SbnSimile 			_tipoControllo;
	private String 			_t005;
	private String 			_cd_livello;
	private A931 				_t931;
	private String				_t001;
	private String 			_a931;
	private String 			_c2_931;
	private SbnFormaNome 		_tipoForma;
	private String 			_b931;
	private String 									_c2_250;
	private LegamiType[] 		_legamiElementoAut;

	private ElementAutType _elementoAut;
    private String  _condiviso;
	private SbnEdizioneSoggettario _edizione;
	private String _cat_termine;


	public CreaDescrittore(SBNMarc sbnmarcObj){
        super(sbnmarcObj);
		DatiElementoType datiElemento = null;
		_sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        _datiTypeChoice = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
        _tipoControllo = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
        datiElemento = _datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t001 = datiElemento.getT001();
        DescrittoreType descrittoreType = new DescrittoreType();
        descrittoreType = (DescrittoreType) _datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t005 = descrittoreType.getT005();
        _t931 = descrittoreType.getT931();
        if (_t931 != null) {
        	_a931 = _t931.getA_931();
        	_b931 = _t931.getB_931();
        	_c2_931 = _t931.getC2_931();
        	_edizione = _t931.getEdizione();
        	_cat_termine = _t931.getCat_termine();
        }
//        else{
//        	throw new EccezioneSbnDiagnostico(9001, " Il campo descrittore non puo essere vuoto");
//
//        }


		_tipoForma = descrittoreType.getFormaNome();
        _cd_livello = descrittoreType.getLivelloAut().toString();
        _elementoAut = _datiTypeChoice.getElementoAut();
		_legamiElementoAut = _elementoAut.getLegamiElementoAut();

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
//            _condiviso = datiElemento.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }

	}

    protected void executeCrea() throws IllegalArgumentException, InvocationTargetException, Exception{

		if (_t931 == null || !ValidazioneDati.isFilled(_a931) || !ValidazioneDati.isFilled(_c2_931))
			throw new EccezioneSbnDiagnostico(3049); //dati incompleti

        DescrittoreValida dv = new DescrittoreValida();
        if (_t001.equals("0000000000")){
        	Progressivi prog = new Progressivi();
        	_t001 = prog.getNextIdDescrittore();
        }
        // Controllo esistenza del descrittore per codice soggettario
        if(!controllaEsistenzaDescrittore(_a931))
        	throw new EccezioneSbnDiagnostico(3050,"Descrittore con codice soggettario gia presente in base dati");
        dv.validaPerCrea(_t001,_cd_livello, getCdUtente(),_t931,_cattura);
        dv.validaPerCreaLegame(_elementoAut);
        Descrittore descrittore = new Descrittore();
		if (_tipoForma == null)
			_tipoForma = SbnFormaNome.A;
		//inserisce il descrittore nel db e successivamente i legami a questo!
		if (descrittore.inserisceDescrittore(_t001, _a931, _b931, _c2_931,
				_edizione, _cat_termine, getCdUtente(), _cd_livello, _tipoForma.toString(), _condiviso)) {

			DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
			for (int i = 0; i < _elementoAut.getLegamiElementoAutCount(); i++) {
				descrittoreDes.inserisciLegame(_t001, _legamiElementoAut[i], getCdUtente());
			}
   		}

        object_response = formattaOutput();
    }

	private boolean controllaEsistenzaDescrittore(String descr)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

        if (!ValidazioneDati.isFilled(descr))
        	throw new EccezioneSbnDiagnostico(3049); //dati incompleti

		Descrittore descrittore = new Descrittore();
		// ANTONIO aggiunta gestione del codice soggettario
		String stringaEsatta = ChiaviSoggetto.normalizzaDescrittore(descr);
		TableDao dao = descrittore.cercaDescrittorePerNomeEsatto(
				stringaEsatta, null, Decodificatore.getCd_tabella(
						"Tb_descrittore", "cd_soggettario", _c2_931
								.toUpperCase().trim()));

		List<Tb_descrittore> risultati = dao.getElencoRisultati();

		if (!ValidazioneDati.isFilled(risultati))
			return true; //tutto ok

		Tb_descrittore tb_descrittore = ValidazioneDati.first(risultati);
		SbnFormaNome forma = SbnFormaNome.valueOf(tb_descrittore.getTP_FORMA_DES());
		switch (forma.getType()) {
		case SbnFormaNome.A_TYPE:
			throw new EccezioneSbnDiagnostico(3346, true, new String[]{tb_descrittore.getDID()});
		case SbnFormaNome.R_TYPE:
			throw new EccezioneSbnDiagnostico(3343, true, new String[]{tb_descrittore.getDID()});
		}

        return true;
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
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
		Tb_descrittore descrittoreInserito = new Tb_descrittore();
		Descrittore descrittore = new Descrittore();
		descrittoreInserito = descrittore.cercaDescrittorePerId(_t001);
        DatiElementoType datiResp = new DatiElementoType();
        datiResp.setT001(descrittoreInserito.getDID());
        SbnDatavar data = new SbnDatavar(descrittoreInserito.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(descrittoreInserito.getCD_LIVELLO())));
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
		return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CREA_DESCRITTORE_1284;
    }

}
