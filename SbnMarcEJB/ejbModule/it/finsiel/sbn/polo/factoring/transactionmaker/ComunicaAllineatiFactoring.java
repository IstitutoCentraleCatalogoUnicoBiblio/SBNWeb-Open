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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreGestisceLocalizza;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ComunicaAllineatiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe ComunicaAllineatiFactoring
 *
 * Classe "Dispatcher" - attiva i Factoring di tipo "ComunicaAllineati"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 *
 * Riceve in input un vettore di elementi di tipo allineati.
 * Per ogni elemento esegue i seguenti passi:
 * se c'e tipoMateriale, oppure tipoAuthority='TU' o 'UM'  chiama allineatoTitolo
 * di TitoloGestisceLocalizza
 * se c'è tipoAuthority='AU' chiama allineatoAutore di AutoreGestisceLocalizza
 * se c'è tipoAuthority='MA' chiama allineatoMarca di MarcaGestisceLocalizza
 *
 * Se il metodo chiamato restituisce un errore si annullano tutte le operazioni
 * (rollback) e si restituisce il diagnostico indicando anche nel testo del
 * diagnostico idAllineato su cui si è verificato l'errore
 */
public class ComunicaAllineatiFactoring extends Factoring {

	static Category log = Category.getInstance("iccu.serversbnmarc.ComunicaAllineatiFactoring");

	private ComunicaAllineatiType 		comunicaAllineatiType;
	private int				allineatoCount;
	private AllineatiType[]			TableDaoAllineato;

	private SbnUserType 			sbnUser;

	private String 				_codiceBiblioteca;
	private String 				_codicePolo;
	private String 				_biblioteca;

	private TableDao				_TableDao_response;

	private TableDao 			_tavola_response = null;
	private String				idAllineato;
	private String				bibliotecaUtente = null;


	public ComunicaAllineatiFactoring(SBNMarc input_root_object) {
		// Assegno radice e classi XML principali
		super (input_root_object) ;
		comunicaAllineatiType = input_root_object.getSbnMessage().getSbnRequest().getComunicaAllineati();
		allineatoCount = comunicaAllineatiType.getAllineatiCount();
		TableDaoAllineato = comunicaAllineatiType.getAllineati();
		sbnUser = input_root_object.getSbnUser();
		_biblioteca = input_root_object.getSbnUser().getBiblioteca();
		_codiceBiblioteca = _biblioteca.substring(3,6);
		_codicePolo = _biblioteca.substring(0,3);
   		bibliotecaUtente = _biblioteca;
	}

	/**
	 * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void eseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception  {
		try {
			verificaAbilitazioni();
			executeComunicaAllineati();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            SBNMarc sbnmarc = new SBNMarc();
            SbnMessageType message = new SbnMessageType();
            SbnResponseType response = new SbnResponseType();
            SbnResultType result = new SbnResultType();
            SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
            SbnOutputType output = new SbnOutputType();
            output.setMaxRighe(1);
            output.setTotRighe(1);
            String errore = "" + ecc.getErrorID();
            int i = 4 - errore.length();
            for (; i > 0; i--)
                errore = "0"+errore;
            result.setEsito(errore);
            result.setTestoEsito("errore");
            sbnmarc.setSbnMessage(message);
            sbnmarc.setSbnUser(sbnUser);
            sbnmarc.setSchemaVersion(schemaVersion);
            message.setSbnResponse(response);
            response.setSbnResult(result);
            response.setSbnResponseTypeChoice(responseChoice);
            responseChoice.setSbnOutput(output);

            object_response = sbnmarc;
        }
	}

   /**
    * esamina il tipo oggetto ricevuto:
    * se è un tipo materiale attiva allineatoTitolo di TitoloGestisceLocalizza con tipoLocalizza Gestione (0)
    * (le informazioni sono su tr_tit_bib)
    * se è un tipoAutority = titolo uniforme o titolo uniforme musica: attiva
    * allineatoTitolo di TitoloGestisceLocalizza con tipoLocalizza Gestione (0) (le informazioni sono su
    * tr_tit_bib)
    * se è un tipoAutority = Autore: attiva allineatoAutore di
    * AutoreGestisceLocalizza (le informazioni sono su tr_tit_aut)
    * se è un tipoAutority = Marca attiva allineatoMarca di
    * MarcaGestisceLocalizza (le informazioni sono su tr_tit_mar)
    * se non è stata fornita la biblioteca viene inserito nel vettore biblioteche il codice del polo associato all'utente
    * che sta lavorando
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public void executeComunicaAllineati() throws IllegalArgumentException, InvocationTargetException, Exception{
		for (int i=0; i<allineatoCount;i++)   	{
			SbnAuthority tipoAuthority = null;
			SbnMateriale tipomateriale = null;
			String[] biblioteca = null;
			idAllineato = TableDaoAllineato[i].getIdAllineato();
			biblioteca = TableDaoAllineato[i].getBiblioteca();
			tipomateriale = TableDaoAllineato[i].getTipoOggetto().getTipoMateriale();
			tipoAuthority = TableDaoAllineato[i].getTipoOggetto().getTipoAuthority();
			List biblio_TableDao = new ArrayList();
			if (biblioteca.length != 0) {
				log.info("inserisco biblio --0-- " + biblioteca.length);
				for (int j=0; j<biblioteca.length; j++){
					biblio_TableDao.add(biblioteca[j]);
					log.info("inserisco biblio --1-- " + biblioteca[j]);
				}
			} else {
					log.info("inserisco biblio --2-- " + _codicePolo);
					biblio_TableDao.add(_codicePolo);
			}
			log.info("biblio " + biblio_TableDao.size());
			try {
				allineato(tipoAuthority,tipomateriale,idAllineato,biblio_TableDao);
			} catch (SQLException e) {
			}
		}
		object_response = formattaOutput();
	}

	private void allineato(
		SbnAuthority tipoAuthority,
		SbnMateriale tipomateriale,
		String idAllineato,
		List biblio_TableDao) throws IllegalArgumentException, InvocationTargetException, Exception{

		if ((tipomateriale != null)){
			TitoloGestisceLocalizza titoloGestisceLocalizza= new TitoloGestisceLocalizza();
			titoloGestisceLocalizza.allineatoTitolo(getCdUtente(),idAllineato,biblio_TableDao,_codicePolo,_codiceBiblioteca,SbnTipoLocalizza.GESTIONE_TYPE);
		}else if (ValidazioneDati.in(tipoAuthority.getType(), SbnAuthority.TU_TYPE, SbnAuthority.UM_TYPE) ) {
			TitoloGestisceLocalizza titoloGestisceLocalizza= new TitoloGestisceLocalizza();
			titoloGestisceLocalizza.allineatoTitolo(getCdUtente(),idAllineato,biblio_TableDao,_codicePolo,_codiceBiblioteca,SbnTipoLocalizza.GESTIONE_TYPE);
		}else if (tipoAuthority.getType() == SbnAuthority.AU_TYPE){
			AutoreGestisceLocalizza autoreGestisceLocalizza= new AutoreGestisceLocalizza();
			autoreGestisceLocalizza.allineatoAutore(getCdUtente(),idAllineato,biblio_TableDao,_codicePolo,_codiceBiblioteca);
	        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
			// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
			// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
			// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
			// INDICAZIONI DI ROSSANA 03/04/2007
//		}else if (tipoAuthority.equals(SbnAuthority.MA)){
//			MarcaGestisceLocalizza marcaGestisceLocalizza= new MarcaGestisceLocalizza();
//			marcaGestisceLocalizza.allineatoMarca(getCdUtente(),idAllineato,biblio_TableDao,_codicePolo,_codiceBiblioteca);
		}else throw new EccezioneSbnDiagnostico(3049, "i dati sono incongruenti");

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
		return CodiciAttivita.getIstance().COMUNICA_ALLINEATI_1035;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().COMUNICA_ALLINEATI_1035;
    }


}
