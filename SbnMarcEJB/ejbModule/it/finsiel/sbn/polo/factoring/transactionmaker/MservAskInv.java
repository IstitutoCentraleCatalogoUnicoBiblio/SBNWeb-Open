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
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.iccu.sbn.ejb.model.unimarcmodel.AskInv;
import it.iccu.sbn.ejb.model.unimarcmodel.Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;


/**
 * Contiene le funzionalità di ricerca per l'entità Repertorio
 * restituisce la lista (sintetica e analitica coincidono)
 * Possibili parametri di ricerca:
 * Identificativo: T001 (è la sigla  del repertorio)
 * parole della descrizione: paroleAut
 * descrizione esatta: stringa esatta
 * descrizione parte iniziale: stringaLike
 * repertori legati ad autore, a titolo, a marca: ArrivoLegame
 *
 * Filtri di ricerca: nessuno
 *
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo o sulla descrizione
 * tipoOut: solo 000 analitico
 *
 * Tabelle coinvolte:
 * - Tb_repertorio
 * OggettiCoinvolti:
 * - Repertorio
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * se non ok ritorna il msg response di diagnostica
 *
 * @author
 * @version 13/01/2003
 */
public class MservAskInv extends MServDocFactoring {

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.MservAskInv");
    protected int maxRighe;

    private String _Kbibl;
    private String _Kordi;
// opzionali
    private String _kAnno;
    private String _tipoMat;
    private String _tipoCirc;
    private String _precis;
    private String _valore;
    private Boolean _flaColl;
    private String _consisDoc;
    private int 	_annoBill;
    private String _tipoPrezzo;
    private String _prezzo;
    private String _Kinv;
	private String _kserie;

    public MservAskInv(SBNMarc input_root_object) {
        super(input_root_object);
        AskInv _askInv;
        _askInv = input_root_object.getSbnMessage().getMServDoc().getAskinv();
        //		CercaSoggettoDescrittoreClassiReperType cerca = new CercaSoggettoDescrittoreClassiReperType();
        if (_askInv.getKbibl() != null)
        	_Kbibl = _askInv.getKbibl();
        if (_askInv.getKordi() != null)
        	_Kordi = _askInv.getKordi();

        if (_askInv.getKanno() != null)
        	_kAnno = _askInv.getKanno();
        if (_askInv.getTipomat() != null)
        	_tipoMat = _askInv.getTipomat();
        if (_askInv.getTipocirc() != null)
        	_tipoCirc = _askInv.getTipocirc();
        if (_askInv.getPrecis() != null)
        	_precis = _askInv.getPrecis();
        if (_askInv.getValore() != null)
        	_valore = _askInv.getValore();

        if (_askInv.getFlacoll())
        	_flaColl = true;
        else
        	_flaColl = false;

        if (_askInv.getConsisDoc() != null)
        	_consisDoc = _askInv.getConsisDoc();

        if (String.valueOf(_askInv.getAnnoBil()) != null)
        	_annoBill = _askInv.getAnnoBil();
        if (_askInv.getTipoPrezzo() != null)
        	_tipoPrezzo = _askInv.getTipoPrezzo();
        if (_askInv.getPrezzo() != null)
        	_prezzo = _askInv.getPrezzo();

        //almaviva5_20100616 aggiunto cod serie
        if (ValidazioneDati.isFilled(_askInv.getKserie()) )
        	_kserie = _askInv.getKserie();
        else {
        	_kserie = ResourceLoader.getPropertyString("SERIE_SPAZIO");
        	log.warn("kserie non specificato, impostata serie a spazio");
        }
    }

    public void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
    	int counter = 0;

    	if(_Kbibl != null)
    		counter ++;
    	if(_Kordi != null)
    		counter ++;
    	//almaviva5_20100616 aggiunto cod serie
    	if(_kserie != null)
    		counter ++;

    	if (counter != 3)
              throw new EccezioneSbnDiagnostico(3039, "comunicare uno e un solo canale di ricerca");

    	if((_Kbibl != null) && (_Kordi != null))
    		cercaAskInv(_kserie,_kAnno,_tipoMat,_tipoCirc,_precis,_valore,_flaColl,_consisDoc,_annoBill,_tipoPrezzo,_prezzo,_Kinv);

    }

    /**
     * Method cercaRepertorioPerMarca.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaAskInv(
    						String _kSerie,
						    String _kAnno,
						    String _tipoMat,
						    String _tipoCirc,
						    String _precis,
						    String _valore,
						    Boolean _flaColl,
						    String _consisDoc,
						    int 	_annoBill,
						    String _tipoPrezzo,
						    String _prezzo,
						    String _Kinv
    ) 						throws IllegalArgumentException, InvocationTargetException, Exception {







    }


    /** Prepara la stringa di output in formato xml */
    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        SBNMarc risultato = formattaOutput();
        rowCounter += maxRighe;
        return risultato;
    }

    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneSbnDiagnostico {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        MServDoc servDoc = new MServDoc();
        Inventario inventario = new Inventario();
        inventario.setColloc("Colloc");
        inventario.setData("Data");
        inventario.setKbibl("Kbibl");
        inventario.setNinvent("Ninvent");
        inventario.setPrecis("Precis");
        inventario.setSequenza("Sequenza");
        inventario.setSezione("Sezione");
        inventario.setSpecific("Specific");
        inventario.setTipocirc("Tipocirc");
        inventario.setTipomat("TipoMat");
        inventario.setTipoprov("Tipoprov");
        inventario.setValore("Valore");
        servDoc.setAnsinv(inventario);
        SbnUserType user = new SbnUserType();
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setMServDoc(servDoc);
        sbnmarc.setSbnMessage(message);
        int totRighe = 0;
        return sbnmarc;

    }

}


