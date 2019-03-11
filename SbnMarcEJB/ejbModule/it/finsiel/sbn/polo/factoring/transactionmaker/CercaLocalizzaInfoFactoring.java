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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreGestisceLocalizza;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceLocalizza;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_biblioteca_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_bib;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoDigitalizzazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;


/**
 * Classe CercaLocalizzaInfoFactoring
 * <p>
 * Classe che esegue l'operazione di CercaLocalizzaInfo richiesta in input via xml.
 * Attiva il factoring di ricerca opportuno secondo tipoOggetto:
 * se c'è il tipo materiale si tratta di documenti: ricerca in tr_tit_bib. Se c'è
 * tipoAuthority titolo uniforme ricerca in tr_tit_bib. Se c'è tipoAuthority
 * Autore ricerca in tr_aut_bib. Se c'è tipoAuthority Marca ricerca in tr_mar_tit.
 * Gli altri tipi authority non sono accettati.
 * Invia l'output preparato al client
 * </p>
 */
public class CercaLocalizzaInfoFactoring extends CercaFactoring {

	private SbnTipoOrd 			_sbnTipoOrd;

    private SbnTipoLocalizza		_tipoLocalizza = null;
	private ElementAutType 		_elementoAut[] = null;
	private LocalizzaInfoType 		_elementoInfoLocalizza;

	private String					_sbnIDLoc;

	private C899[]					_t899;
	private LocalizzaInfoType		_t899_response;
	private String 				c1,c2;
	private List 				_TableDao_response;

	private String 				_c1;
	private String 				_c2;
//	private String 				_idLista;
	private int 					_numPrimo = 1;
	private int 					_tipoAuthority;
	private int 					_tipoMateriale;
	private String 				_biblioteca;
	private String 				_codiceBiblioteca;
	private String 				_codicePolo;
	private int					_infoTipoLocalizza;
	private SbnOggetto 			_tipoOggetto;
	private LocalizzaInfoType 		localizzaInfoType[] = new LocalizzaInfoType[1];
	static Category 				log = Category.getInstance("iccu.serversbnmarc.CercaLocalizzaInfoFactoring");

	/**
	 * Constructor CercaLocalizzaInfoFactoring.
	 * @param sbnmarcObj
	 */
	public CercaLocalizzaInfoFactoring(SBNMarc input_root_object) {
		super(input_root_object);
		_sbnTipoOrd = factoring_object.getTipoOrd();
		_elementoInfoLocalizza =  factoring_object.getCercaLocalizzaInfo();
		_sbnIDLoc = this._elementoInfoLocalizza.getSbnIDLoc();
		_tipoLocalizza = this._elementoInfoLocalizza.getTipoInfo();
		_t899 = this._elementoInfoLocalizza.getT899();
		if (_t899.length != 0){
			_c1 = this._t899[0].getC1_899();
			_c2 = this._t899[0].getC2_899();
		}
//		_numPrimo = input_root_object.getSbnMessage().getSbnRequest().getCerca().getNumPrimo();
		_tipoOggetto = this._elementoInfoLocalizza.getTipoOggetto();
		if (this._tipoOggetto.getTipoAuthority() != null)
			_tipoAuthority = this._elementoInfoLocalizza.getTipoOggetto().getTipoAuthority().getType();
		if (this._tipoOggetto.getTipoMateriale() != null)
			_tipoAuthority = this._elementoInfoLocalizza.getTipoOggetto().getTipoMateriale().getType();

		_biblioteca = user_object.getBiblioteca();

		_codiceBiblioteca = _biblioteca.substring(0,3);
		_codicePolo = _biblioteca.substring(3,6);
		if (this._tipoLocalizza != null) {
			_infoTipoLocalizza = this._elementoInfoLocalizza.getTipoInfo().getType();
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
	public void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnOggetto tipoOggetto = null;
		tipoOggetto = this._elementoInfoLocalizza.getTipoOggetto();
		if ((tipoOggetto.getTipoAuthority()!=null) && (this._sbnIDLoc!=null)){
			int tipoAutority = tipoOggetto.getTipoAuthority().getType();
			if ((tipoAutority == SbnAuthority.TU.getType())||(tipoAutority == SbnAuthority.UM.getType())){
				TitoloGestisceLocalizza theGestisceLocalizzaTitolo = new TitoloGestisceLocalizza();
				this._TableDao_response = theGestisceLocalizzaTitolo.cercaLocalizzaTitolo(this._sbnIDLoc,this._tipoLocalizza.getType(),this._c1,this._c2);
				if (_TableDao_response.size()==0)
					throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
				this._t899_response = formattaTitolo(this._TableDao_response);
			}else if (tipoAutority == SbnAuthority.AU.getType()){
				AutoreGestisceLocalizza theGestisceLocalizzaAutore = new AutoreGestisceLocalizza();
				this._TableDao_response = theGestisceLocalizzaAutore.cercaLocalizzaAutore(this._sbnIDLoc,this._c1,this._c2);
				if (_TableDao_response.size()==0)
					throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
				this._t899_response = formattaAutore(this._TableDao_response);
		        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
				// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
				// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
				// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
				// INDICAZIONI DI ROSSANA 03/04/2007
			}
//			}else if (tipoAutority == SbnAuthority.MA.getType()){
//				MarcaGestisceLocalizza theGestisceLocalizzaMarca = new MarcaGestisceLocalizza();
//				this._TableDao_response = theGestisceLocalizzaMarca.cercaLocalizzaMarca(this._sbnIDLoc,this._c1,this._c2);
//				if (_TableDao_response.size()==0)
//					throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
//				this._t899_response = formattaMarca(this._TableDao_response);
//			}
		}else if (tipoOggetto.getTipoMateriale()!=null){
			TitoloGestisceLocalizza theGestisceLocalizzaTitolo = new TitoloGestisceLocalizza();
			this._TableDao_response = theGestisceLocalizzaTitolo.cercaLocalizzaTitolo(this._sbnIDLoc,this._tipoLocalizza.getType(),this._c1,this._c2);
            if (_TableDao_response.size()==0)
                throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
			this._t899_response = formattaTitolo(this._TableDao_response);
		}else throw new EccezioneSbnDiagnostico(3041);

	}

	/**
	 * Method formattaMarca.
	 * @param TableDao
	 * @return C899[]
	 */
	private LocalizzaInfoType formattaMarca(List TableDao) {
		LocalizzaInfoType localizza = new LocalizzaInfoType();
		C899 c;
		for (int i=0;i<TableDao.size();i++){
	      	c = new C899();
            Vl_marca_bib vl_marca_bib = (Vl_marca_bib)TableDao.get(i);
            c.setC2_899(vl_marca_bib.getCD_POLO()+vl_marca_bib.getCD_BIBLIOTECA());
            c.setC1_899(vl_marca_bib.getCD_ANA_BIBLIOTECA());
			c.setA_899((vl_marca_bib.getDS_CITTA() == null ? "" : vl_marca_bib.getDS_CITTA() + " - ")
					+ vl_marca_bib.getDS_BIBLIOTECA());
            c.setTipoInfo(SbnTipoLocalizza.GESTIONE);
			localizza.addT899(i,c);
		}
		localizza.setSbnIDLoc(this._sbnIDLoc);
		localizza.setTipoInfo(SbnTipoLocalizza.GESTIONE);
		localizza.setTipoOggetto(this._tipoOggetto);
		localizza.setTipoOperazione(SbnAzioneLocalizza.ESAME);
		localizzaInfoType[0] = localizza;
		return localizza;
	}


	/**
	 * Method formattaAutore.
	 * @param TableDao
	 * @return C899[]
	 */
	private LocalizzaInfoType formattaAutore(List TableDao) {
		LocalizzaInfoType localizza = new LocalizzaInfoType();
		for (int i=0;i<TableDao.size();i++){
	      	C899 c = new C899();
            Vl_autore_bib vl_aut_bib = (Vl_autore_bib)TableDao.get(i);
            c.setC2_899(vl_aut_bib.getCD_POLO()+vl_aut_bib.getCD_BIBLIOTECA());
            c.setC1_899(vl_aut_bib.getCD_ANA_BIBLIOTECA());
			c.setA_899((vl_aut_bib.getDS_CITTA() == null ? "" : vl_aut_bib.getDS_CITTA() + " - ")
					+ vl_aut_bib.getDS_BIBLIOTECA());
			c.setTipoInfo(SbnTipoLocalizza.GESTIONE);
			localizza.addT899(i,c);
		}
		localizza.setSbnIDLoc(this._sbnIDLoc);
		localizza.setTipoInfo(SbnTipoLocalizza.GESTIONE);
		localizza.setTipoOggetto(this._tipoOggetto);
		localizza.setTipoOperazione(SbnAzioneLocalizza.ESAME);

		localizzaInfoType[0] = localizza;
		return localizza;
	}


	/**
	 * Method formattaTitolo.
	 * @param TableDao
	 * @return C899[]
	 */
	private LocalizzaInfoType formattaTitolo(List TableDao) throws EccezioneDB, EccezioneSbnDiagnostico {
		LocalizzaInfoType localizza = new LocalizzaInfoType();
		for (int i=0;i<TableDao.size();i++){
			Vl_biblioteca_tit vl_bib_tit = null;
			vl_bib_tit =((Vl_biblioteca_tit)TableDao.get(i));
	      	C899 c = new C899();
        	c.setC2_899(vl_bib_tit.getCod_polo()+vl_bib_tit.getCd_biblioteca());
        	c.setC1_899(vl_bib_tit.getCd_ana_biblioteca());
			c.setA_899((vl_bib_tit.getDs_citta() == null ? "" : vl_bib_tit.getDs_citta() + " - ")
					+ vl_bib_tit.getDs_biblioteca());
        	c.setB_899(vl_bib_tit.getDS_FONDO());
        	c.setZ_899(vl_bib_tit.getDS_CONSISTENZA());
        	c.setG_899(vl_bib_tit.getDS_SEGN());
        	c.setS_899(vl_bib_tit.getDS_ANTICA_SEGN());
        	c.setN_899(vl_bib_tit.getNOTA_TIT_BIB());
        	if ((vl_bib_tit.getFL_DISP_ELETTR() != null)&&
        		(!(vl_bib_tit.getFL_DISP_ELETTR()).equals(" ")))
	        	c.setE_899(SbnIndicatore.valueOf(vl_bib_tit.getFL_DISP_ELETTR().toString()));
            String fl_mutilo = vl_bib_tit.getFL_MUTILO();
            if ((fl_mutilo != null) && (!fl_mutilo.equals(" "))) {
                if (fl_mutilo.equals("X"))
                    fl_mutilo = "S";
                c.setQ_899(SbnIndicatore.valueOf(fl_mutilo));
            }
        	c.setU_899(vl_bib_tit.getURI_COPIA());
        	if (vl_bib_tit.getTP_DIGITALIZZ() != null && !vl_bib_tit.getTP_DIGITALIZZ().equals(" "))
	        	c.setT_899(SbnTipoDigitalizzazione.valueOf(vl_bib_tit.getTP_DIGITALIZZ()));
        	if (!vl_bib_tit.getFL_POSSESSO().equals("N")&&
        		!vl_bib_tit.getFL_GESTIONE().equals("N"))
        		c.setTipoInfo(SbnTipoLocalizza.TUTTI);
			else if (!vl_bib_tit.getFL_POSSESSO().equals("N")&&
        		vl_bib_tit.getFL_GESTIONE().equals("N"))
        		c.setTipoInfo(SbnTipoLocalizza.POSSESSO);
			else c.setTipoInfo(SbnTipoLocalizza.GESTIONE);
			localizza.addT899(i,c);
		}
		localizza.setSbnIDLoc(this._sbnIDLoc);
		localizza.setTipoInfo(SbnTipoLocalizza.TUTTI);
		//Titolo titolo = new Titolo();
		//Tb_titolo titoloEstratto = titolo.estraiTitoloPerID(_sbnIDLoc);
		//_tipoOggetto.setTipoMateriale(SbnMateriale.valueOf(titoloEstratto.getTP_MATERIALE()));
		localizza.setTipoOggetto(_tipoOggetto);
		localizza.setTipoOperazione(SbnAzioneLocalizza.ESAME);

		localizzaInfoType[0] = localizza;
		return localizza;
	}

    /** Lo rimuovo, poi verifico se funziona.
    public String eseguiTransazione()  {
        try {
				try {
					executeCerca();
				} catch (SQLException e) {
				}
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:"+ecc);
            _object_response = "Errore :"+ecc.getErrorID() + " "+ecc.getMessaggio();
            SBNMarc sbnmarc = new SBNMarc();
            SbnMessageType message = new SbnMessageType();
            SbnResponseType response = new SbnResponseType();
            SbnResultType result = new SbnResultType();
            SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
            SbnOutputType output = new SbnOutputType();
            output.setMaxRighe(0);
            output.setNumPrimo(1);
            output.setTotRighe(0);
            output.setTipoOrd(_sbnTipoOrd);
            output.setTipoOutput(_tipoOut);
            String errore = "" + ecc.getErrorID();
            int i = 4 - errore.length();
            for (; i>0;i--) errore += "0";
            result.setEsito(errore);
            result.setTestoEsito(this._object_response);
            sbnmarc.setSbnMessage(message);
            sbnmarc.setSbnUser(_sbnUser);
            sbnmarc.setSchemaVersion(this._schemaVersion);
            message.setSbnResponse(response);
            response.setSbnResult(result);
            response.setSbnResponseTypeChoice(responseChoice);
            responseChoice.setSbnOutput(output);
            StringWriter sw = new StringWriter();
            try {
                sbnmarc.marshal(sw);
                _object_response = sw.toString();
            } catch (Exception e) {
                log.error("Errore marshalling "+e);
                _object_response = "Errore costruzione xml diagnostico";
            }
        }
        return getResult();
    }
    */


    protected SBNMarc preparaOutput() throws EccezioneDB {
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
        output.setLocalizzaInfo(localizzaInfoType);
        output.setNumPrimo(this._numPrimo);
        output.setTipoOrd(_sbnTipoOrd);
        output.setTotRighe(_TableDao_response.size());
        output.setTipoOutput(tipoOut);
        //log.debug("Risultato: "+ sw.toString());
        return sbnmarc;
	}




    public String getIdAttivita() {
        if (_elementoInfoLocalizza.getTipoInfo().getType() == SbnTipoLocalizza.POSSESSO_TYPE)
            return CodiciAttivita.getIstance().CERCA_LOCALIZZAZIONI_DI_POSSEDUTO_1005;
        return CodiciAttivita.getIstance().CERCA_LOCALIZZAZIONI_PER_GESTIONE_1006;
    }

    public String getIdAttivitaSt() {
        return getIdAttivita();
    }

}
