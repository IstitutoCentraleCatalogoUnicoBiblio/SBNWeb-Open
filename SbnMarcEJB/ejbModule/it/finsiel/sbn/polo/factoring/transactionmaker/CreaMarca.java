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
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoMarca;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.IndiceJMSObjectLocator;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Parola;
import it.finsiel.sbn.polo.oggetti.estensione.MarcaValida;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



/**
 * Classe CreaMarca
 * Factoring:
 * Crea una marca come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Crea gli eventuali marca-repertorio e marca-autore
 * Tabelle coinvolte:
 * - Tb_marca; Tb_parola; Tr_mar_aut; Tr_rep_mar, ts_link_multim
 * OggettiCoinvolti:
 * - Marca
 * - Parola
 * - MarcaAutore
 * - RepertorioMarca
 * - LinkMultimediale
 * Passi da eseguire:
 * Valida la marca.
 * se non ok ritorna il msg response di diagnostica
 * se ok e se esistono informazioni di ArrivoLegame:valida legami repertorio-marca
 * e marca-autore;
 * se c'è T856 registra un record in ts_link_multim con
 * ky_link_multim=MID
 * se non ok  ritorna il msg response di diagnostica
 * Se ok inserisce la marca e le parole nel database,; inserisce i legami nel
 * database; prepara
 * il msg di response di output
 * @author
 * @version
 */

/**
Adeguamento versione Indice 1.3 27.02.2015
 */
public class CreaMarca extends CreaElementoAutFactoring {

	private TableDao 				tavola_response = null;
	private SBNMarc				_sbnmarcObj;
	private CreaType				_creaType;
	private SbnUserType 			_sbnUser = null;
	private ElementAutType 		elementoAut;
    private BigDecimal 			_schemaVersion = null;
	private SbnTipoOrd 			_sbnTipoOrd;
	private List 				_TableDao_response = new ArrayList();
	private List 				_elencoDiagnostico = new ArrayList();
	private int 					_maxRighe;
	private int 					_numPrimo;
    private SbnTipoOutput			_tipoOut = SbnTipoOutput.VALUE_1;
	private int 					numeroRecord;

    private int 					rowCounter = 0;
    private int 					blockCounter = 1;
    private String 				_idLista = null;
	private ElementAutType 		_elementAutType;
	private String 				_polo;
	private String[] 				_b921;
	private LocalizzaType 			_localizzaType;
	private int 					_localizzaCount = 0;
	private String 				sbnIdLoc;
	private LocalizzaInfoType[]	TableDaoLocalizza;
	private String 				_biblioteca;
	private C856[] 				_t856;

	private SbnSimile 				_tipoControllo;

	private String 				_t001;

	private SbnFormaNome 			tipoForma;

	private String 				cd_livello;
    private String  _condiviso;


	public CreaMarca(SBNMarc sbnmarcObj){
		super(sbnmarcObj);
		MarcaType datiElementoAut = null;
		_sbnmarcObj = sbnmarcObj;
		_creaType = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea();
		_elementAutType = _creaType.getCreaTypeChoice().getElementoAut();
        _sbnUser = _sbnmarcObj.getSbnUser();
        _biblioteca = _sbnmarcObj.getSbnUser().getBiblioteca();
   		_schemaVersion = _sbnmarcObj.getSchemaVersion();
		_polo = _sbnUser.getBiblioteca().substring(0,3);
		_tipoControllo = _creaType.getTipoControllo();
		if (_tipoControllo == null)
			_tipoControllo = SbnSimile.SIMILE;
		datiElementoAut = (MarcaType)_creaType.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
		if (datiElementoAut.getT921() != null)
			_b921 = datiElementoAut.getT921().getB_921();
		else _b921 = null;
		elementoAut = _sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice().getElementoAut();
		_t856 = datiElementoAut.getT856();
		_maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC")) ;
		_numPrimo = 1;
		_localizzaType = sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getLocalizza();
		if (_localizzaType != null){
			_localizzaCount = _localizzaType.getLocalizzaInfoCount();
			TableDaoLocalizza = _localizzaType.getLocalizzaInfo();
		}
		CreaTypeChoice datiTypeChoice=_sbnmarcObj.getSbnMessage().getSbnRequest().getCrea().getCreaTypeChoice();
		_t001 = datiTypeChoice.getElementoAut().getDatiElementoAut().getT001();
		tipoForma = datiTypeChoice.getElementoAut().getDatiElementoAut().getFormaNome();
		cd_livello = datiTypeChoice.getElementoAut().getDatiElementoAut().getLivelloAut().toString();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElementoAut.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiElementoAut.getCondiviso() != null && (datiElementoAut.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiElementoAut.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }

	}


    protected void executeCrea() throws IllegalArgumentException, InvocationTargetException, Exception {
		String codiceUtente, biblioteca;
		boolean transazioneEseguita=false;
	    String tipoLegame, idArrivo,ds_luogo;
		biblioteca=_sbnmarcObj.getSbnUser().getBiblioteca();
		codiceUtente = getCdUtente();
		boolean ignoraID = _t001.equals(SBNMARC_DEFAULT_ID);


		MarcaValida marcaValida = new MarcaValida();
		_elencoDiagnostico = marcaValida.validaPerCrea(ignoraID,this._creaType, codiceUtente,_cattura);
		if ((_elencoDiagnostico.size() == 0) && (_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE)){
			if (ignoraID) {
				Progressivi progressivi = new Progressivi();
				_t001 = progressivi.getNextIdMarca();
			}

			Marca marca = new Marca();

			marca.inserisciMarca(((MarcaType)elementoAut.getDatiElementoAut()),codiceUtente,cd_livello, _t001, _condiviso);
			for (int i=0;i< _t856.length;i++){
					marca.creaLinkMarcaImmagine(_t001,_t856[i].getU_856(),_t856[i].getC9_856_1(),codiceUtente);
			}
			Parola parola = new Parola();
			for (int i=0;i<_b921.length;i++){
				if (!_b921[i].equals("")){
					String parolaNormalizzata = NormalizzaNomi.normalizzazioneGenerica(_b921[i].trim());
					parola.inserisciParola(_t001,parolaNormalizzata,codiceUtente);
				}
			}
			int size 	= _elementAutType.getLegamiElementoAutCount();
			int i = 0;
			int j;
			ArrivoLegame[] 					arrivoLegame;
			int 							sizeArrivoLegame;
			LegamiType[]					legamiType;
			legamiType 	= _elementAutType.getLegamiElementoAut();

			for (i = 0; i < size; i++) {
				arrivoLegame=legamiType[i].getArrivoLegame();
				sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
		        for (j = 0; j < sizeArrivoLegame; j++) {
		        	marca.creaLegame(arrivoLegame[j].getLegameElementoAut(),_t001,codiceUtente);
				}
			}
			// almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
			// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
			// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
			// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
			// INDICAZIONI DI ROSSANA 03/04/2007
			//eseguiLocalizza();
		}
		if ((_elencoDiagnostico.size() == 0)&(_tipoControllo.getType() == SbnSimile.SIMILEIMPORT_TYPE))
			throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		object_response = formattaOutput();
    }

	private void eseguiLocalizza() throws IllegalArgumentException, InvocationTargetException, Exception{
		if (_localizzaType != null){

			for (int i=0; i<_localizzaCount;i++)   	{
				SbnTipoLocalizza tipoLocalizza = null;
				SbnAuthority tipoAuthority = null;
				SbnMateriale tipomateriale = null;
				SbnAzioneLocalizza tipoOperazione = null;
				C899[] t899 = null;
				sbnIdLoc = TableDaoLocalizza[i].getSbnIDLoc();
				if (sbnIdLoc.equals(SBNMARC_DEFAULT_ID))
					sbnIdLoc = _t001;
				t899 = TableDaoLocalizza[i].getT899();
				tipoOperazione = TableDaoLocalizza[i].getTipoOperazione();
				tipoLocalizza = TableDaoLocalizza[i].getTipoInfo();
				tipomateriale = TableDaoLocalizza[i].getTipoOggetto().getTipoMateriale();
				tipoAuthority = TableDaoLocalizza[i].getTipoOggetto().getTipoAuthority();
                List t899_TableDao = new ArrayList();
				if (t899 != null)
					for (int j=0; j<t899.length; j++){
						t899_TableDao.add(t899[j]);
					}

				if (tipoAuthority != null){
					if (tipoOperazione.getType() == SbnAzioneLocalizza.LOCALIZZA_TYPE){
						LocalizzaFactoring localizzaFactoring = new LocalizzaFactoring(TableDaoLocalizza[i],_sbnUser, _biblioteca, t899[i].getC2_899().toString());
						localizzaFactoring.localizza(tipoAuthority,tipomateriale,sbnIdLoc,t899_TableDao,tipoLocalizza,_biblioteca);
					}else throw new EccezioneSbnDiagnostico(3035,"operazione non valida");
				}

			}
		}
	}

    /** Utilizzato qualora il risultato fosse su più pagine  */
    public SBNMarc eseguiRecupero() {
        try {
            return leggiRecupero();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
        }
        return getSBNMarcResult();
    }

    private SBNMarc leggiRecupero() throws EccezioneFactoring {
        try {
            String nome =  this._idLista + "_" + this._numPrimo;
            log.info("leggo da LDAP con nome file:"+nome);
            Serializable ret = IndiceJMSObjectLocator.lookup(nome);

            return (SBNMarc)ret;
            //return (String)IndiceLDAPObjectLocator.lookup(nome);
        } catch (Exception e) {
            log.error("Eccezione nella lettura del file xml di recupero");
            throw new EccezioneFactoring(102);
        }
    }



    private ElementAutType formattaMarca(Tb_marca marca) throws EccezioneDB, EccezioneFactoring {
        ElementAutType elemento = new ElementAutType();
        //elemento.setLocalizzaInfo(new LocalizzaInfoType[0]);
        elemento.setDatiElementoAut(creaMarca(marca));
        return elemento;

    }


    private DatiElementoType creaMarca(Tb_marca marca) throws EccezioneDB, EccezioneFactoring {
		MarcaType datiElementoType = new MarcaType();
		datiElementoType.setT001(marca.getMID());
        SbnDatavar data = new SbnDatavar(marca.getTS_VAR());
		datiElementoType.setT005(data.getT005Date());
        datiElementoType.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(marca.getCD_LIVELLO())));

		A921 a921 = new A921();
		a921.setA_921(marca.getDS_MARCA());
		a921.setE_921(marca.getDS_MOTTO());
		a921.setD_921(marca.getNOTA_MARCA());
		datiElementoType.setT921(a921);
        return datiElementoType;
    }

	public SBNMarc formattaOutput() throws  IllegalArgumentException, InvocationTargetException, Exception{
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
		result.setEsito("0000");
		if (_elencoDiagnostico.size()>0)
				result.setTestoEsito("sono stati trovati elementi simili");
		else result.setTestoEsito("OK");

        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);

/*
        ElementAutType elementoAut[] = new ElementAutType[_TableDao_response.size()];
		output.setMaxRighe(_TableDao_response.size());
		output.setNumPrimo(1);
		output.setTotRighe(_TableDao_response.size());
        output.setElementoAut(formattaLista(elementoAut));
        output.setTipoOrd(_sbnTipoOrd);
        output.setTotRighe(this._TableDao_response.size());
*/
        int size = _TableDao_response.size();
        if (size == 0)
        	size = 1;
        Tb_marca tb_marca = new Tb_marca();
        FormatoMarca formatoMarca = new FormatoMarca(_tipoOut,null,_sbnUser);
		if (_elencoDiagnostico.size() > 0 ){
			for (int i=0; i<_elencoDiagnostico.size();i++){
	            tb_marca = (Tb_marca) _elencoDiagnostico.get(i);
	            output.addElementoAut(formatoMarca.formattaMarca(tb_marca));
			}
		}else if (_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE){
			tb_marca = leggiMarcaInserita();
	        DatiElementoType datiResp = new DatiElementoType();
	        datiResp.setT001(tb_marca.getMID());
            SbnDatavar data = new SbnDatavar(tb_marca.getTS_VAR());
	        datiResp.setT005(data.getT005Date());
	        datiResp.setLivelloAut(SbnLivello.valueOf(tb_marca.getCD_LIVELLO()));
	        datiResp.setTipoAuthority(SbnAuthority.MA);
	        ElementAutType elementoAutResp = new ElementAutType();
	        elementoAutResp.setDatiElementoAut(datiResp);
	        output.addElementoAut(elementoAutResp);
//			elementoAut[0] = formatoMarca.formattaMarca(tb_marca);
		}
        output.setMaxRighe(size);
        output.setTipoOutput(_tipoOut);

        return sbnmarc;
	}

	/**
	 * Method leggiMarcaInserita.
	 * @return Tb_marca
	 * @throws InfrastructureException
	 */
	private Tb_marca leggiMarcaInserita() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		Marca marca = new Marca();
		Tb_marca marcaCreata = new Tb_marca();
		TableDao tavola;
   		tavola = marca.cercaMarcaPerID(_t001);
   		List temp = tavola.getElencoRisultati();

   		return (Tb_marca) temp.get(0);
	}


	private void formattaLista(SbnOutputType output){

		//nella response avrò un insieme di luoghi letti
        Tb_marca tb_marca = new Tb_marca();
        for (int i = 0; i < _TableDao_response.size(); i++) {
            tb_marca = (Tb_marca) _TableDao_response.get(i);
            try {
            	output.addElementoAut(formattaMarca(tb_marca));
			} catch (EccezioneDB e) {
			} catch (EccezioneFactoring e) {
			}
        }

	}

    public String getIdAttivita(){
    	if(_cattura){// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA ABILITATO A CREARE UN DOCUMENTO
    		// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
        	// serve solo il Cerca + Localizza per Gestione
    		return CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009;
    		// return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    		// Fine Intervento google3
    	}
    	else{
    		return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
    	}
    }
    public String getIdAttivitaSt(){
    	if(_cattura){// SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA ABILITATO A CREARE UN DOCUMENTO
    		return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	}
    	else{
    		return CodiciAttivita.getIstance().CREA_MARCA_1257;
    	}
    }
}
