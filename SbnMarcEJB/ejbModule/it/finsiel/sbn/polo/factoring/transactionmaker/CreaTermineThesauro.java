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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.estensione.TermineValida;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
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
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;


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

public class CreaTermineThesauro extends CreaElementoAutFactoring {

	private SBNMarc 			_sbnmarcObj;
	private SbnUserType 		_sbnUser;
	private BigDecimal 		_schemaVersion;
	private CreaTypeChoice 	_datiTypeChoice;
	private SbnSimile 			_tipoControllo;
	private String 			_t005;
	private String 			_cd_livello;
	private A935 				_t935;
	private String				_t001;
	private String 			_a935;
	private String 			_c2_935;
	private SbnFormaNome 		_tipoForma;
	private String 			_b935;
	private LegamiType[] 		_legamiElementoAut;

	private ElementAutType _elementoAut;
    private String  _condiviso;


	public CreaTermineThesauro(SBNMarc sbnmarcObj){
        super(sbnmarcObj);
		DatiElementoType datiElemento = null;
		_sbnmarcObj = sbnmarcObj;
        _sbnUser = _sbnmarcObj.getSbnUser();
        _schemaVersion = _sbnmarcObj.getSchemaVersion();
        _datiTypeChoice = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
        _tipoControllo = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getTipoControllo();
        datiElemento = _datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t001 = datiElemento.getT001();
        TermineType termineType = new TermineType();
        termineType = (TermineType) _datiTypeChoice.getElementoAut().getDatiElementoAut();
        _t005 = termineType.getT005();
        _t935 = termineType.getT935();
        if (_t935 != null){
        	_a935 = _t935.getA_935();
        	_b935 = _t935.getB_935();
        	_c2_935 = _t935.getC2_935();
        }
		_tipoForma = termineType.getFormaNome();
        _cd_livello = termineType.getLivelloAut().toString();
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

        if ((_t935 == null)||(_a935.equals(""))||(_c2_935.equals("")))
			throw new EccezioneSbnDiagnostico(3049,"dati incompleti");

        TermineValida termineValida = new TermineValida();
        if (_t001.equals(SBNMARC_DEFAULT_ID)){
        	Progressivi prog = new Progressivi();
        	_t001 = prog.getNextIdTermineThesauro();
        }
        termineValida.validaPerCrea(_t001,_cd_livello, getCdUtente(),_t935,_cattura);
        termineValida.validaPerCreaLegame(_elementoAut);
        TermineThesauro termine = new TermineThesauro();
//		if (_tipoForma == null) _tipoForma = SbnFormaNome.A;
		//inserisce il descrittore nel db e successivamente i legami a questo!
  		if (termine.inserisceTermineThesauro(_t001,_a935,_b935,_c2_935,getCdUtente(),_cd_livello,_tipoForma.toString(),_condiviso)){
   			String idArrivo = null;
			String tipoLegame = null;
//			DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
//			for (int i=0;i<_elementoAut.getLegamiElementoAutCount(); i++){
//				descrittoreDes.inserisciInTr_des_des(_t001,_legamiElementoAut[i],getCdUtente());
//			}
   		}


        object_response = formattaOutput();
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
		Tb_termine_thesauro termineInserito = new Tb_termine_thesauro();
		TermineThesauro termine = new TermineThesauro();
		Descrittore descrittore = new Descrittore();
		termineInserito = termine.cercaTerminePerId(_t001);
        DatiElementoType datiResp = new DatiElementoType();
        datiResp.setT001(termineInserito.getDID());
        SbnDatavar data = new SbnDatavar(termineInserito.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(termineInserito.getCD_LIVELLO())));
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
		return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CREA_DESCRITTORE_1284;
    }

}
