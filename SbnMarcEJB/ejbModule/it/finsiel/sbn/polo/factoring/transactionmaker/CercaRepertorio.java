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
import it.finsiel.sbn.polo.factoring.base.FormatoRepertorio;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioAutore;
import it.finsiel.sbn.polo.oggetti.RepertorioMarca;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_aut;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

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
public class CercaRepertorio extends CercaElementoAutFactoring {

    private static Category log = Category.getInstance("iccu.serversbnmarc.CercaRepertorio");
    private CercaElementoAutType _elementoAut;
    private CercaDatiAutType _datiElementoAut;

    private CercaSoggettoDescrittoreClassiReperType _cercaRepertorio;
    private String _T001;
    private String stringaLike;
    private SbnRangeDate _T005_Range;
    private String _livelloAutA;
    private String _livelloAutDA;
    private String[] paroleAut;
    private List _TableDao_response;
    private boolean ricercaUnivoca;
    private Tb_repertorio _tb_repertorio = new Tb_repertorio();
    private boolean ricercaPuntuale = false;
    private String stringaEsatta;

    private LegameElementoAutType _legameElementoAut;

    private SbnAuthority _tipoAuthority;

    public CercaRepertorio(SBNMarc input_root_object) {
        super(input_root_object);
        CercaType _cerca;
        StringaCercaType stringaCerca;
        _cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
        _elementoAut = _cerca.getCercaElementoAut();
        _datiElementoAut = _elementoAut.getCercaDatiAut();
        //		CercaSoggettoDescrittoreClassiReperType cerca = new CercaSoggettoDescrittoreClassiReperType();
        if (_datiElementoAut instanceof CercaSoggettoDescrittoreClassiReperType)
            _cercaRepertorio = (CercaSoggettoDescrittoreClassiReperType) _datiElementoAut;
        if (_datiElementoAut != null) {
            if (_datiElementoAut.getCanaliCercaDatiAut() != null) {
                _T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
                stringaCerca = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca();
                if (stringaCerca != null)
                    stringaLike = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
                if (stringaCerca != null)
                    stringaEsatta = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
            }
            _T005_Range = _datiElementoAut.getT005_Range();
            if (_datiElementoAut.getLivelloAut_A() != null)
                _livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
            if (_datiElementoAut.getLivelloAut_Da() != null)
                _livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();
        }
        if (_cercaRepertorio != null)
            paroleAut = _cercaRepertorio.getParoleAut();
        if (_elementoAut.getArrivoLegame() != null) {
            _legameElementoAut = _elementoAut.getArrivoLegame().getLegameElementoAut();
            _tipoAuthority = _legameElementoAut.getTipoAuthority();
        }

    }

    public void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (factoring_object.getTipoOrd() != null)
        if ((factoring_object.getTipoOrd().getType() != SbnTipoOrd.VALUE_1.getType()) && (factoring_object.getTipoOrd().getType() != SbnTipoOrd.VALUE_0.getType()))
            throw new EccezioneSbnDiagnostico(1231, "errore nell'ordinamento");
        verificaAbilitazioni();
        int counter = 0;
        if (_T001 != null)
            counter++;
        if ((stringaLike != null) || (stringaEsatta != null))
            counter++;
        if (_legameElementoAut != null)
            counter++;
        if (paroleAut != null && paroleAut.length > 0)
            counter++;
        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039, "comunicare uno e un solo canale di ricerca");
        if (_T001 != null)
            cercaRepertorioPerId();
        else if (stringaLike != null)
            cercaRepertorioPerStringaCerca();
        else if (paroleAut != null)
            cercaRepertorioPerParole();
        else if ((_legameElementoAut != null)) {
            if (_tipoAuthority.getType() == SbnAuthority.AU_TYPE)
                cercaRepertorioPerAutore();
        } else if ((_legameElementoAut != null)) {
            if (_tipoAuthority.getType() == SbnAuthority.TU_TYPE)
                cercaRepertorioPerTitoloUniforme();
        } else if ((_legameElementoAut != null)) {
            if (_tipoAuthority.getType() == SbnAuthority.MA_TYPE)
                cercaRepertorioPerMarca();
        }
        if (tipoOut.getType() == SbnTipoOutput.VALUE_1.getType() || tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            object_response = formattaOutput();
        } else {
            log.error("Tipo output sconosciuto -" + tipoOut);
            throw new EccezioneSbnDiagnostico(3044);
        }
    }

    /**
     * Method cercaRepertorioPerMarca.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaRepertorioPerMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioMarca repertorioMarca = new RepertorioMarca();
        List TableDaoAuxiliar;
        _TableDao_response = new ArrayList();
        repertorioMarca.valorizzaFiltri(_datiElementoAut);
        String idArrivo = _legameElementoAut.getIdArrivo();
        controllaNumeroRecord(repertorioMarca.contaRepertorioPerMarca(idArrivo, _datiElementoAut));
        tavola_response = repertorioMarca.cercaRepertorioPerMarca(idArrivo);
        TableDaoAuxiliar = tavola_response.getElencoRisultati();

        int id;
        for (int i = 0; i < TableDaoAuxiliar.size(); i++) {
            id = (int) ((Tr_rep_mar) TableDaoAuxiliar.get(i)).getID_REPERTORIO();
            Repertorio repertorio = new Repertorio();
            _TableDao_response.add(repertorio.cercaRepertorioId(id));
        }
    }

    /**
     * Method cercaRepertorioPerTitoloUniforme.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaRepertorioPerTitoloUniforme() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioTitoloUniforme repertorioTitoloUniforme = new RepertorioTitoloUniforme();
        List TableDaoAuxiliar;
        _TableDao_response = new ArrayList();
        repertorioTitoloUniforme.valorizzaFiltri(_datiElementoAut);
        String idArrivo = _legameElementoAut.getIdArrivo();
        controllaNumeroRecord(repertorioTitoloUniforme.contaRepertorioPerTitolo(idArrivo, _datiElementoAut));
        tavola_response = repertorioTitoloUniforme.cercaRepertorioPerTitolo(idArrivo);
        TableDaoAuxiliar = tavola_response.getElencoRisultati();

        int id;
        for (int i = 0; i < TableDaoAuxiliar.size(); i++) {
            id = (int) ((Tr_rep_tit) TableDaoAuxiliar.get(i)).getID_REPERTORIO();
            Repertorio repertorio = new Repertorio();
            _TableDao_response.add(repertorio.cercaRepertorioId(id));
        }
    }

    /**
     * Method cercaRepertorioPerAutore.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaRepertorioPerAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        RepertorioAutore repertorioAutore = new RepertorioAutore();
        List TableDaoAuxiliar;
        _TableDao_response = new ArrayList();
        repertorioAutore.valorizzaFiltri(_datiElementoAut);
        String idArrivo = _legameElementoAut.getIdArrivo();
        controllaNumeroRecord(repertorioAutore.contaRepertorioPerAutore(idArrivo, _datiElementoAut));
        tavola_response = repertorioAutore.cercaRepertorioPerAutore(idArrivo);
        TableDaoAuxiliar = tavola_response.getElencoRisultati();

        int id;
        for (int i = 0; i < TableDaoAuxiliar.size(); i++) {
            id = (int) ((Tr_rep_aut) TableDaoAuxiliar.get(i)).getID_REPERTORIO();
            Repertorio repertorio = new Repertorio();
            _TableDao_response.add(repertorio.cercaRepertorioId(id));
        }
    }

    private void cercaRepertorioPerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repertorio = new Repertorio();
        repertorio.valorizzaFiltri(_datiElementoAut);
        controllaNumeroRecord(repertorio.contaRepertorioPerParoleNome(paroleAut));
        tavola_response = repertorio.cercaRepertorioPerParoleNome(paroleAut, tipoOrd);
        _TableDao_response = tavola_response.getElencoRisultati();


    }

    private void cercaRepertorioPerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repertorio = new Repertorio();
        repertorio.valorizzaFiltri(_datiElementoAut);
        if (stringaLike != null) {
            controllaNumeroRecord(repertorio.contaRepertorioPerNomeLike(stringaLike));
            tavola_response = repertorio.cercaRepertorioPerNomeLike(stringaLike, tipoOrd);
        } else if (stringaEsatta != null) {
            controllaNumeroRecord(repertorio.contaRepertorioPerNomeEsatto(stringaEsatta));
            tavola_response = repertorio.cercaRepertorioPerNomeEsatto(stringaEsatta, tipoOrd);
        }
        _TableDao_response = tavola_response.getElencoRisultati();

    }

    private void cercaRepertorioPerId() throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repertorio = new Repertorio();
        _tb_repertorio = repertorio.cercaRepertorioPerCdSig(_T001);
        if (_tb_repertorio == null)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
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
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        SbnUserType user = new SbnUserType();
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_repertorio tb_repertorio;
        int totRighe = 0;
        if (_TableDao_response != null)
            totRighe = _TableDao_response.size();
        FormatoRepertorio formatoRepertorio = new FormatoRepertorio();
        if ((totRighe > 0) || ricercaPuntuale) {
            output.setMaxRighe(maxRighe);
            output.setTotRighe(totRighe);
            output.setNumPrimo(rowCounter+1);
            output.setTipoOrd(factoring_object.getTipoOrd());
            output.setTipoOutput(tipoOut);
            ElementAutType elemento;
            if (!ricercaPuntuale) {
                for (int i = 0; i < totRighe; i++) {
                    elemento = new ElementAutType();
                    tb_repertorio = (Tb_repertorio) _TableDao_response.get(i);
                    elemento.setDatiElementoAut(formatoRepertorio.formattaRepertorioPerEsame(tb_repertorio));
                    output.addElementoAut(elemento);
                }
            } else {
                totRighe = 1;
                elemento = new ElementAutType();
                elemento.setDatiElementoAut(formatoRepertorio.formattaRepertorioPerEsame(_tb_repertorio));
                output.addElementoAut(elemento);
            }
            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
        } else {
            result.setEsito("3001");
            //Esito non positivo: si potrebbe usare un'ecc.
            result.setTestoEsito("Nessun elemento trovato");
        }
        output.setTotRighe(totRighe);
        if (idLista != null)
            output.setIdLista(idLista);
        return sbnmarc;

    }

}
