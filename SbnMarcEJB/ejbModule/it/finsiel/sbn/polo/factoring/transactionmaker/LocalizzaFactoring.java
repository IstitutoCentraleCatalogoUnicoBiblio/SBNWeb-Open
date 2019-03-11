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
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreGestisceLocalizza;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe ModificaFactoring
 *
 * Classe "Dispatcher" - attiva i Factoring di tipo "Localizza"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 *
 * Riceve in input un vettore di elementi di tipo LocalizzaInfo.
 * Per ogni elemento esegue i seguenti passi:
 * Se tipoOperazione='Localizza':
 * se c'e tipoMateriale, oppure tipoAuthority='TU' o 'UM'  chiama localizzaTitolo
 * di TitoloGestisceLocalizza
 * se c'è tipoAuthority='AU' chiama localizzaAutore di AutoreGestisceLocalizza
 * se c'è tipoAuthority='MA' chiama localizzaMarca di MarcaGestisceLocalizza
 *
 * Se tipoOperazione='Delocalizza' chiama il metodo delocalizza secondo il tipo
 * oggetto indicato dalle stesse regole del caso 'Localizza'
 *
 * Se tipoOperazione='Allineato' chiama il metodo allineato secondo il tipo
 * oggetto indicato dalle stesse regole del caso 'Localizza'
 *
 * Se tipoOperazione='Correggi' occorre verificare che tipoInfo sia 'Gestione', e
 * che sia presente tipoMateriale (altrimenti si ritorna un diagnostico: dati
 * incongruenti). chiama il metodo modificaLocalizzaTitolo di
 * TitoloGestisceLocalizza
 *
 * Se il metodo chiamato restituisce un errore si annullano tutte le operazioni
 * (rollback) e si restituisce il diagnostico indicando anche nel testo del
 * diagnostico SbnIDLoc su cui si è verificato l'errore
 */
public class LocalizzaFactoring extends Factoring {

	static Category log = Category.getInstance("iccu.serversbnmarc.LocalizzaFactoring");

	private LocalizzaType 			localizzaType;
	private int					localizzaCount;
	private LocalizzaInfoType[]	TableDaoLocalizza;

	private SbnUserType 			sbnUser;

	private String 				_codiceBiblioteca;
	private String 				_codicePolo;
	private String 				_biblioteca;

	private TableDao					_TableDao_response;

	private TableDao 				_tavola_response = null;
	private String					sbnIdLoc;
    private BigDecimal 			_schemaVersion = null;
	private String					bibliotecaUtente = null;

	private SbnTipoLocalizza 		_tipoLocalizza;

	private String _c2_899;

	public LocalizzaFactoring(SBNMarc input_root_object) {
		// Assegno radice e classi XML principali
		super (input_root_object) ;
    	localizzaType = input_root_object.getSbnMessage().getSbnRequest().getLocalizza();
		localizzaCount = localizzaType.getLocalizzaInfoCount();
		TableDaoLocalizza = localizzaType.getLocalizzaInfo();
		sbnUser = input_root_object.getSbnUser();
		_biblioteca = input_root_object.getSbnUser().getBiblioteca();
		_codicePolo = _biblioteca.substring(0,3);
		_codiceBiblioteca = _biblioteca.substring(3,6);
   		_schemaVersion = input_root_object.getSchemaVersion();
   		bibliotecaUtente = _biblioteca;
	}

	public LocalizzaFactoring(
	LocalizzaInfoType localizza,
	SbnUserType user, String biblio, String c2_899) {

//		localizzaType = localizza;
//		localizzaCount = localizzaType.getLocalizzaInfoCount();
		_c2_899 = c2_899;
		sbnUser = user;
        setCdUtente(user);
		_biblioteca = biblio;
		_tipoLocalizza = localizza.getTipoInfo();
		_codicePolo = _biblioteca.substring(0,3);
		_codiceBiblioteca = _biblioteca.substring(3,6);

	}

	public LocalizzaFactoring(
	LocalizzaType localizza,
	SbnUserType user, String biblio) {

		localizzaType = localizza;
		localizzaCount = localizzaType.getLocalizzaInfoCount();
		sbnUser = user;
        setCdUtente(user);
		_biblioteca = biblio;
		_codicePolo = _biblioteca.substring(0,3);
		_codiceBiblioteca = _biblioteca.substring(3,6);
}


	/**
	 * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void eseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception  {
		try {
			executeLocalizza();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            //almaviva5_20140218 segnalazione CSA: messaggio errore errato
            object_response = FormatoErrore.buildMessaggioErrore(ecc, user_object);
        }
	}




   /**
    * esamina il tipo oggetto ricevuto:
    * se è un tipo materiale attiva cercaLocalizzaTitolo di TitoloGestisceLocalizza
    * (le informazioni sono su tr_tit_bib)
    * se è un tipoAutority = titolo uniforme o titolo uniforme musica: attiva
    * cercaLocalizzaTitolo di GestisceLocalizzaTitolo (le informazioni sono su
    * tr_tit_bib)
    * se è un tipoAutority = Autore: attiva cercaLocalizzaAutore di
    * GestisceLocalizzaAutore (le informazioni sono su tr_tit_aut)
    * se è un tipoAutority = Marca attiva cercaLocalizzaMarca di
    * GestisceLocalizzaMarca (le informazioni sono su tr_tit_mar)
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void executeLocalizza() throws IllegalArgumentException, InvocationTargetException, Exception{

		for (int i=0; i<localizzaCount;i++)   	{
			SbnTipoLocalizza tipoLocalizza = null;
			SbnAuthority tipoAuthority = null;
			SbnMateriale tipomateriale = null;
			SbnAzioneLocalizza tipoOperazione = null;
			C899[] t899 = null;
//			String sbnIdLoc = null;
			sbnIdLoc = TableDaoLocalizza[i].getSbnIDLoc();
			t899 = TableDaoLocalizza[i].getT899();
			tipoOperazione = TableDaoLocalizza[i].getTipoOperazione();
			tipoLocalizza = TableDaoLocalizza[i].getTipoInfo();
			tipomateriale = TableDaoLocalizza[i].getTipoOggetto().getTipoMateriale();
			tipoAuthority = TableDaoLocalizza[i].getTipoOggetto().getTipoAuthority();
			List t899_TableDao = new ArrayList();
			if (t899.length>0){
				for (int j=0; j<t899.length; j++){
					t899_TableDao.add(t899[j]);
				}
			}else throw new EccezioneSbnDiagnostico(3073,"almeno una biblioteca è obbligatoria");
			if (tipoOperazione.getType() == SbnAzioneLocalizza.LOCALIZZA_TYPE)
					localizza(tipoAuthority,tipomateriale,sbnIdLoc,t899_TableDao,tipoLocalizza, bibliotecaUtente);
			else if (tipoOperazione.getType() == SbnAzioneLocalizza.DELOCALIZZA_TYPE)
					delocalizza(tipoAuthority,tipomateriale,sbnIdLoc,t899_TableDao,tipoLocalizza);
			else if (tipoOperazione.getType() == SbnAzioneLocalizza.ALLINEATO_TYPE)
					allineato(tipoAuthority,tipomateriale,sbnIdLoc,t899_TableDao,tipoLocalizza);
			else if (tipoOperazione.getType() == SbnAzioneLocalizza.CORREGGI_TYPE){
				if ((tipoLocalizza.getType() == SbnTipoLocalizza.POSSESSO_TYPE || tipoLocalizza.getType() == SbnTipoLocalizza.TUTTI_TYPE) == false
                        || tipomateriale == null) {
                    throw new EccezioneSbnDiagnostico(3049,
                            "i dati sono incongruenti");
                }
                    correggi(tipoAuthority, tipomateriale, sbnIdLoc,
                            t899_TableDao, tipoLocalizza);
			}
		}
		object_response = formattaOutput();
	}

	public void localizza(
		SbnAuthority tipoAuthority,
		SbnMateriale tipomateriale,
		String sbnIdLoc,
        List t899_TableDao,
		SbnTipoLocalizza tipoLocalizza,
		String bibliotecaUtente) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (tipomateriale == null && tipoLocalizza.getType() == SbnTipoLocalizza.POSSESSO_TYPE)
            throw new EccezioneSbnDiagnostico(3049, "i dati sono incongruenti");
		if (((tipoAuthority != null) && (ValidazioneDati.in(tipoAuthority.getType(), SbnAuthority.TU_TYPE, SbnAuthority.UM_TYPE))) || (tipomateriale != null)){
			TitoloGestisceLocalizza titoloGestisceLocalizza= new TitoloGestisceLocalizza();
			titoloGestisceLocalizza.localizzaTitolo(getCdUtente(),sbnIdLoc,t899_TableDao,_codicePolo,tipoLocalizza.getType());
			// almaviva2 21 LUGLIO 2009 La localizzazione in Polo deve essere fatta solo per l'oggetto dichiarato
			// NON DEVE ESSERE PROPAGATA !!!!
//            titoloGestisceLocalizza.localizzaReticolo(getCdUtente(),sbnIdLoc,t899_TableDao,_codicePolo,tipoLocalizza.getType());
			// almaviva2 21 LUGLIO 2009
		}else if (tipoAuthority.getType() == SbnAuthority.AU_TYPE){
			AutoreGestisceLocalizza autoreGestisceLocalizza= new AutoreGestisceLocalizza();
			autoreGestisceLocalizza.localizzaAutore(getCdUtente(),sbnIdLoc,t899_TableDao,_codicePolo);
	        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
			// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
			// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
			// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
			// INDICAZIONI DI ROSSANA 03/04/2007
//		}else if (tipoAuthority.equals(SbnAuthority.MA)){
//			MarcaGestisceLocalizza marcaGestisceLocalizza= new MarcaGestisceLocalizza();
//			marcaGestisceLocalizza.localizzaMarca(getCdUtente(),sbnIdLoc,t899_TableDao,_codicePolo);
		}else throw new EccezioneSbnDiagnostico(3049, "i dati sono incongruenti");
	}

	private void delocalizza(
		SbnAuthority tipoAuthority,
		SbnMateriale tipomateriale,
		String sbnIdLoc,
		List t899_TableDao,
		SbnTipoLocalizza tipoLocalizza) throws IllegalArgumentException, InvocationTargetException, Exception{
		if(((tipoAuthority != null) && (ValidazioneDati.in(tipoAuthority.getType(), SbnAuthority.TU_TYPE, SbnAuthority.UM_TYPE))) || (tipomateriale != null)){
			TitoloGestisceLocalizza titoloGestisceLocalizza = new TitoloGestisceLocalizza();
			titoloGestisceLocalizza.delocalizzaTitolo(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo,tipoLocalizza.getType());
		}else if (tipoAuthority.getType() == SbnAuthority.AU_TYPE){
			AutoreGestisceLocalizza autoreGestisceLocalizza = new AutoreGestisceLocalizza();
			autoreGestisceLocalizza.delocalizzaAutore(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo);
	        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
			// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
			// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
			// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
			// INDICAZIONI DI ROSSANA 03/04/2007
//		}else if (tipoAuthority.equals(SbnAuthority.MA)){
//			MarcaGestisceLocalizza marcaGestisceLocalizza= new MarcaGestisceLocalizza();
//			marcaGestisceLocalizza.delocalizzaMarca(getCdUtente(),sbnIdLoc,t899_TableDao,_codicePolo);
		}else throw new EccezioneSbnDiagnostico(3049, "i dati sono incongruenti");

	}

	private void allineato(
		SbnAuthority tipoAuthority,
		SbnMateriale tipomateriale,
		String sbnIdLoc,
		List t899_TableDao,
		SbnTipoLocalizza tipoLocalizza) throws IllegalArgumentException, InvocationTargetException, Exception{

		if (ValidazioneDati.in(tipoAuthority.getType(), SbnAuthority.TU_TYPE, SbnAuthority.UM_TYPE) || (tipomateriale != null)){
			TitoloGestisceLocalizza titoloGestisceLocalizza= new TitoloGestisceLocalizza();
			titoloGestisceLocalizza.allineatoTitolo(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo,tipoLocalizza.getType());
		}else if (tipoAuthority.getType() == SbnAuthority.AU_TYPE){
			AutoreGestisceLocalizza autoreGestisceLocalizza= new AutoreGestisceLocalizza();
			autoreGestisceLocalizza.allineatoAutore(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo);
	        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
			// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
			// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
			// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
			// INDICAZIONI DI ROSSANA 03/04/2007
//		}else if (tipoAuthority.equals(SbnAuthority.MA)){
//			MarcaGestisceLocalizza marcaGestisceLocalizza= new MarcaGestisceLocalizza();
//			marcaGestisceLocalizza.allineatoMarca(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo);
		}else throw new EccezioneSbnDiagnostico(3049, "i dati sono incongruenti");

	}

	private void correggi(
		SbnAuthority tipoAuthority,
		SbnMateriale tipomateriale,
		String sbnIdLoc,
		List t899_TableDao,
		SbnTipoLocalizza tipoLocalizza) throws IllegalArgumentException, InfrastructureException, InvocationTargetException, Exception{

		TitoloGestisceLocalizza titoloGestisceLocalizza= new TitoloGestisceLocalizza();

		titoloGestisceLocalizza.modificaLocalizzaTitolo(getCdUtente(),sbnIdLoc,t899_TableDao,_codiceBiblioteca,_codicePolo,tipoLocalizza.getType());

	}


	private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
		SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);

        output.setTotRighe(1);
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");

        return sbnmarc;
    }


	public String getIdAttivita(){
		return CodiciAttivita.getIstance().LOCALIZZA_1007;
	}


}
