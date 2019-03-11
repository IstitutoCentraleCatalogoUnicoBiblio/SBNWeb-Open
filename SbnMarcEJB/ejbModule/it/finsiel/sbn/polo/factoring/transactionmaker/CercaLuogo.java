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
import it.finsiel.sbn.polo.factoring.base.FormatoLuogo;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaLuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;



// Adeguamento versione 1.3 indice 27.02.2015


/**
 * Classe CercaLuogo<BR>
 * <p>
 * Contiene le funzionalità di ricerca per l'entità Luogo,
 * restituisce la lista sintetica o analitica.
 * Possibili parametri di ricerca:
 * Identificativo: T001
 * nome luogo per parte iniziale: nome
 * intervallo di data di aggiornamento: T005_Range
 * Filtri di ricerca:
 * forma del nome: formaNome
 * livello di autorità: statusAuthority
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo (ID_Luogo) o sul
 * nome (ky_norm_luogo)
 * tipoOut: tipo di output richiesto: Esame, Lista
 * <BR>
 * <BR>
 * Tabelle coinvolte:<BR>
 * - Tb_luogo; Tr_luo_luo; Tr_tit_luo<BR>
 * <BR>
 * OggettiCoinvolti:<BR>
 * - Luogo<BR>
 * - LuogoLuogo<BR>
 * - TitoloLuogo<BR>
 * <BR>
 * Passi da eseguire:<BR>
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * se non ok ritorna il msg response di diagnostica
 * <BR>
 * </p>
 *
 * @author
 * @version
 */
public class CercaLuogo extends CercaElementoAutFactoring {
    private CercaElementoAutType 	_elementoAut;
    private CercaDatiAutType 		_datiElementoAut;
    private String 				_T001 = null;
    private String 				_nomeLuogoEsatto = null;
    private String 				_nomeLuogoLike = null;
    private SbnRangeDate 			_T005_Range = null;
	private ArrivoLegame 			_arrivoLegame = null;
    private SbnFormaNome 			_formaNome = null;
    private SbnAuthority 			_statusAuthority = null;
	private SbnTipoOrd 			_sbnTipoOrd;
    private String 				_diagnostico  = null;
    private String					_livelloAutA  = null;
    private String					_livelloAutDA = null;

	private CercaType 				_cerca;

	private CercaLuogoType			_cercaLuogo = null;
	private String 				_a260;
    private List _TableDao_response = new ArrayList();
    private boolean ricerca_univoca = false;

	public CercaLuogo(SBNMarc input_root_object) {
		super(input_root_object);
		_cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
		_elementoAut =  _cerca.getCercaElementoAut();
		_datiElementoAut = _elementoAut.getCercaDatiAut();
		_arrivoLegame = _elementoAut.getArrivoLegame();

		_sbnTipoOrd = _cerca.getTipoOrd();
        if (_datiElementoAut instanceof CercaLuogoType)
            _cercaLuogo = (CercaLuogoType) _datiElementoAut;
		if (_cercaLuogo != null)
			_a260 = _cercaLuogo.getA_260();
		if (_datiElementoAut.getCanaliCercaDatiAut() != null) {
		    _T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
    		if (_datiElementoAut.getCanaliCercaDatiAut().getStringaCerca() != null){
    			_nomeLuogoEsatto = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca().getStringaCercaTypeChoice().getStringaEsatta();
    			_nomeLuogoLike = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca().getStringaCercaTypeChoice().getStringaLike();
    		}
        }
		_T005_Range = _datiElementoAut.getT005_Range();
		_formaNome = _datiElementoAut.getFormaNome();
		_arrivoLegame = _elementoAut.getArrivoLegame();
		if (_datiElementoAut.getLivelloAut_A() != null)
			this._livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
		if (_datiElementoAut.getLivelloAut_Da() != null)
			this._livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();

   }


   /**
    * Metodo per cercare il luogo con n. identificativo:
    * ricerca su Tb_luogo con ID_luogo
    * Output possibili:
    * . luogo inesistente
    * . luogo trovato: tutti gli attributi
    * Esegue la preparazione dell'output richiesto in tipoOutput.
    * Input:
    * - LID del luogo
    * - booleano settato a true se sono in fase di ricerca, false se sono in fase di creazione
 * @throws InfrastructureException
    */
   public void cercaLuogoPerID(String ID_Luogo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException   {
		Luogo luogo = new Luogo();
		tavola_response = luogo.cercaLuogoPerIDInTavola(ID_Luogo);
        ricerca_univoca = true;
   }



   /**
    * Metodo per cercare il luogo con stringa nome.
    *
    * ricerca su Tb_luogo con ky_norm_id
    * Input: nome, livelloAut, tipoforma
    * applica la routine di normalizzazione al nome
    * esegue la select * su Tb_luogo
    * se i filtri sono valorizzati li applica alla select
    * Applica il tipo ordinamento richiesto in tipoOrd.
    * Output possibili:
    * . luogo inesistente
    * . luoghi trovati: tutti gli attributi
    * Esegue la preparazione dell'output richiesto in tipoOutput.
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void cercaLuogoPerNome(String nome, String livelloAutA, String livelloAutDa, String tipoforma) throws IllegalArgumentException, InvocationTargetException, Exception {
		TableDao 		listaLuoghiResult 	= null;
		Tb_luogo	tab_luogo 			= new Tb_luogo();
		String		cd_livello			= null;
		//routine di normalizzazione al nome: va sostituito con un metodo più generico!!!
		nome = NormalizzaNomi.normalizzazioneGenerica(nome);
		Luogo luogo = new Luogo();
//questo lo devo mettere perchè non è un filtro!
		if (nome != null)
			luogo.setKY_NORM_LUOGO(nome);
		if (_a260 != null)
			luogo.setCD_PAESE(_a260);
		luogo.valorizzaFiltri(_datiElementoAut);
		if (this._nomeLuogoEsatto != null){
	        controllaNumeroRecord(luogo.contaLuogoPerNome(_datiElementoAut));
			tavola_response = luogo.cercaLuogoPerNome(_datiElementoAut,tipoOrd);
		}
		if (this._nomeLuogoLike != null){
	        controllaNumeroRecord(luogo.contaLuogoPerNomeLike(_datiElementoAut));
			tavola_response = luogo.cercaLuogoPerNomeLike(_datiElementoAut,tipoOrd);
		}
		if (tavola_response == null){
			throw new EccezioneSbnDiagnostico(3015);
		}
   }





   /**
    * Metodo per cercare i luoghi collegati a un titolo
    * Input: idArrivo statusAuthority, formaNome
    * ricerca su Tb_luogo dove TR_tit_luo.Id_luogo = Tb_luogo.Id_luogo e
    * TR_tit_luo.BID = idArrivo
    * se i filtri sono valorizzati li applica alla select
    * Applica il tipo ordinamento richiesto in tipoOrd.
    * Output possibili:
    * . luogo inesistente
    * . luoghi trovati: tutti gli attributi
    * Esegue la preparazione dell'output richiesto in tipoOutput: esame o lista.
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void cercaLuogoPerTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
		if (_arrivoLegame == null) {
			throw new EccezioneSbnDiagnostico(3041);
        } else  {
	        String idArrivo;
          if (_arrivoLegame.getLegameElementoAut() != null) {
            idArrivo = _arrivoLegame.getLegameElementoAut().getIdArrivo();
          } else {
            idArrivo = _arrivoLegame.getLegameDoc().getIdArrivo();
          }
	        Luogo luogo = new Luogo();
	        controllaNumeroRecord(luogo.contaLuogoPerTitolo(idArrivo,_datiElementoAut));
	        tavola_response = luogo.cercaLuogoPerTitolo(idArrivo, tipoOrd);
		}
   }



    /** Prepara la stringa di output in formato xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected SBNMarc preparaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (tavola_response == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        _TableDao_response = tavola_response.getElencoRisultati(maxRighe);
        if(ricerca_univoca)
            numeroRecord=_TableDao_response.size();
        if (_TableDao_response.size()==0)
            throw new EccezioneSbnDiagnostico(3001,"Nessun elemento trovato");
        SBNMarc risultato = formattaOutput();
        rowCounter += _TableDao_response.size();
        return risultato;
    }


   /**
    * Metodo per leggere i rinvii di un luogo
    * Input: ID_luogo
    * 1) ricerca su Tb_luogo dove TR_luo_luo.Id_luogo_base =  ID_luogo in input e
    * Tb_luogo.Id_luogo = TR_luo_luo.id_luogo_coll
    *
    * 2) ricerca su Tb_luogo dove TR_luo_luo.Id_luogo_coll =  ID_luogo in input e
    * Tb_luogo.Id_luogo = TR_luo_luo.id_luogo_base
    *
    * Output possibili:
    * . luogo inesistente
    * . luoghi trovati: tutti gli attributi
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public  List cercaRinviiLuogo(String lid)throws IllegalArgumentException, InvocationTargetException, Exception{
		Luogo luogo = new Luogo();
	    return luogo.cercaRinviiLuogo(lid, tipoOrd);
	}


   /**
    * outputLista()
    * Metodo per compilare la struttura LuogoType con gli attributi dei luoghi letti,
    * con il solo sottoinsieme previsto dalla lista sintetica
    * Se un luogo è in forma di rinvio (tp_forma = 'R') legge il luogo in forma
    * accettata (attiva cercaRinviiLuogo ), compila la struttura LuogoType con il
    * luogo in forma accettata e la struttura ArrivoLegame con il luogo in forma di
    * rinvio
    *
    * outputEsame()
    * Metodo per compilare la struttura LuogoType con gli attributi del luogo letto.
    * Se un luogo è in forma di rinvio (tp_forma = 'R') legge il luogo in forma
    * accettata (attiva cercaRinviiLuogo ), compila la struttura LuogoType con il
    * luogo in forma accettata e la struttura ArrivoLegame con il luogo in forma di
    * rinvio
    *
    * Se un luogo è in forma accettata (tp_forma = 'A')Cerca i rinvii del luogo con
    * il metodo cercaRinviiLuogo
    * Per ogni rinvio trovato compila la struttura ArrivoLegame
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public SBNMarc formattaOutput() throws IllegalArgumentException, InvocationTargetException, Exception{
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
		result.setEsito("0000");
		result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        if (idLista!=null) {
            output.setIdLista(idLista);
        }
		//nella response avrò un insieme di luoghi letti
        Tb_luogo tb_luogo = new Tb_luogo();
        FormatoLuogo formatoLuogo = new FormatoLuogo(tipoOut,tipoOrd);
        for (int i = 0; i < _TableDao_response.size(); i++) {
            tb_luogo = (Tb_luogo) _TableDao_response.get(i);
            output.addElementoAut(formatoLuogo.formattaRinvii(tb_luogo));
        }
        output.setMaxRighe(maxRighe);
        output.setNumPrimo(rowCounter+1);
        output.setTipoOrd(_sbnTipoOrd);
        output.setTotRighe(numeroRecord);
        output.setTipoOutput(tipoOut);
        return sbnmarc;
	}






   /**
    * metodo attivato da CercaElementoAut.
    * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
    * opportuno.
    * gestisce il tipo di risposta richiesto (lista o esame) e produce il
    * file xml di output richiesto
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void executeCerca() throws  IllegalArgumentException, InvocationTargetException, Exception{

		Tb_luogo tb_luogo = new Tb_luogo();
        if (_T001 != null)
			cercaLuogoPerID(_T001);
		else if (_nomeLuogoEsatto != null){
			cercaLuogoPerNome(_nomeLuogoEsatto,_livelloAutA,_livelloAutDA,null);
			}
		else if (_nomeLuogoLike != null){
			cercaLuogoPerNome(_nomeLuogoLike,_livelloAutA,_livelloAutDA,null);
		}
		else if (_arrivoLegame != null ){
			ArrivoLegame legame = _elementoAut.getArrivoLegame();
			cercaLuogoPerTitolo();
    }

	}

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_LUOGHI_1248;
    }

}
