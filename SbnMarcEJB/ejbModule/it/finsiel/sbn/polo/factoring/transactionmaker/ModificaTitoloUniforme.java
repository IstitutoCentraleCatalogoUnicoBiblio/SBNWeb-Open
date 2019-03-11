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
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Titset2;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloModifica;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeCrea;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeValida;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloValidaLegamiModifica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_2;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe ModificaTitoloUniforme
 * <p>
 * Modifica un titolo uniforme e i relativi legami.
 * </p>
 *
 * @author
 * @author
 *
 * @version 03-giu-2003
 */
public class ModificaTitoloUniforme extends ModificaElementoAutFactoring {

    private TimestampHash timeHash = new TimestampHash();
    private String T001 = null;
    private TitoloUniformeType datiDoc = null;
    //ElementAutType elementoAut;
    private ModificaType modificaType;
    private LegamiType[] legami;

    private SbnLivello livelloAut;
    private SbnSimile tipoControllo;
    private String id_lista = null;
    private Tb_titolo titoloModificato = null;
    private StatoRecord statoRecord;
    private String userID;
    private String  _condiviso;

    int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));

    /**
     * Constructor ModificaTitoloUniforme.
     * @param sbnmarcObj
     */
    public ModificaTitoloUniforme(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
        if (datiElementoAut instanceof TitoloUniformeType)
            datiDoc = (TitoloUniformeType) datiElementoAut;
        T001 = datiElementoAut.getT001();
        livelloAut = datiElementoAut.getLivelloAut();
        statoRecord = datiElementoAut.getStatoRecord();
        //controllare i legami!!!!
        legami = elementoAut.getLegamiElementoAut();
        userID = getCdUtente();
        tipoControllo = modificaType.getTipoControllo();
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiDoc.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiDoc.getCondiviso() != null && (datiDoc.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiDoc.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }


    }

    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(root_object.getSchemaVersion());
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiDocType datiResp = new DatiDocType();
        datiResp.setT001(titoloModificato.getBID());
        SbnDatavar data = new SbnDatavar(titoloModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAutDoc(datiElementoAut.getLivelloAut());
        DocumentoType elementoAutResp = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(datiResp);
        elementoAutResp.setDocumentoTypeChoice(docChoice);
        output.addDocumento(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    /**
     * Method executeModifica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

        boolean modificato = false;
        if (statoRecord != null && statoRecord.getType() == StatoRecord.C_TYPE) {
            modificato = true;
        } else {
            //Se non è modificato devono esistere dei legami.
            if (modificaType.getElementoAut().getLegamiElementoAutCount() == 0)
                throw new EccezioneSbnDiagnostico(3090, "Nessuna modifica da apportare");
        }
        TitoloUniformeValida titoloValida = new TitoloUniformeValida(modificaType);
        TimestampHash timeHash = new TimestampHash();
        Tb_titolo titolo = titoloValida.validaPerModifica(getCdUtente(),_cattura);
        AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(titolo);
        timeHash.putTimestamp("Tb_titolo", titolo.getBID(), new SbnDatavar( titolo.getTS_VAR()));
        TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
        timbroCondivisione.modificaTimbroCondivisione(titolo,user_object.getBiblioteca() + user_object.getUserId(),_condiviso);

        elencoDiagnostico = titoloValida.getElencoDiagnostico();
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            settaIdLista();
            object_response = formattaLista(1);
            return;
        }
;
        String cd_natura = titolo.getCD_NATURA();
        boolean naturaCambiata = !cd_natura.equals("A");
     // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
        boolean naturaVno = !cd_natura.equals("V");

        //validaLegami(titolo);
        TitoloModifica titoloModifica = new TitoloModifica(modificaType);
        if (modificato) {
            // if (naturaCambiata) {
        	  if (naturaCambiata && naturaVno) {
              titolo.setCD_NATURA("A");
              titoloModifica.modificaNatura(titolo, flagAllineamento);
            } else {
              titolo = eseguiModificaTitolo(titolo, modificaType, flagAllineamento);
              flagAllineamento.setTb_titolo(true);

              // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
              // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
              if (datiDoc.getT231() != null &&
              		((datiDoc.getNaturaTU() == null) || datiDoc.getNaturaTU().equals("A") )) { //si gestisce opera 231
        		  TitoloUniformeCrea titoloCrea = new TitoloUniformeCrea(factoring_object);
        		  Tb_titset_2 tb_titset_2 = new Tb_titset_2();
        		  tb_titset_2 = titoloCrea.creaDatiTitset2(tb_titset_2);
//        		  if (tb_titset_2.getS231_lingua_originale() == null)
//        			  throw new EccezioneSbnDiagnostico(2900, "lingua originale opera obbligatoria");
        		  tb_titset_2.setUTE_VAR(getCdUtente());
        		  tb_titset_2.setFL_CANC("N");
        		  tb_titset_2.setBID(titolo.getBID());
        		  Titset2 titset2 = new Titset2();
        		  Tb_titset_2 tbt2 = titset2.cercaPerId(titolo.getBID());
        		  if (tbt2 != null) {
        			  titset2.update(tb_titset_2);
        		  } else {
        			  tb_titset_2.setUTE_INS(getCdUtente());
		                  titset2.inserisci(tb_titset_2);
        		  }
        	  }
            }
        }
        // if (naturaCambiata == false) {
        if (naturaCambiata == false || naturaVno == false) {
          TitoloValidaLegamiModifica titValLeg =
              new TitoloValidaLegamiModifica(titolo.getBID(),modificaType.getElementoAut().getLegamiElementoAut());
          titValLeg.validaLegamiDocumentoModifica(userID, timeHash, titolo,_cattura);
          if (modificato) {
              titoloModifica.eseguiUpdate(titolo);
          } else {
              titoloModifica.updateVersione(titolo.getBID(), userID);
          }
          titoloModifica.modificaLegami(
              modificaType.getElementoAut().getLegamiElementoAut(),
              timeHash,
              titolo,
              flagAllineamento, null);
        }

        new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
        //Rileggo per avere il timestamp ed i dati corretti.
        titoloModificato = titoloValida.estraiTitoloPerID(titolo.getBID());
        object_response = formattaOutput();
    }

    /**
     * Esegue la modifica di un titolo
     * Due possibilità: una è quella di creare un autore con tutti i campi null tranne quelli
     * modificati,
     * l'altra è quella di modificare i campi dell'autore estratto dalla select e quindi
     * fare l'update di tutti i campi.
     * Per ora uso una soluzione ibrida.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private Tb_titolo eseguiModificaTitolo(
        Tb_titolo tb_titolo,
        ModificaType modifica,
        AllineamentoTitolo flagAll)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String cd_livello = datiElementoAut.getLivelloAut().toString();
        TitoloUniformeValida validaTitolo = new TitoloUniformeValida(modifica);
        //validaTitolo.validaPerModifica(getCdUtente());
        TitoloUniformeCrea titoloCrea = new TitoloUniformeCrea(modifica);
        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
            tb_titolo.setUTE_FORZA_VAR(userID);
        tb_titolo.setUTE_VAR(userID);

        //il cd livello potrebbe forse essere spostato.
        tb_titolo.setCD_LIVELLO(cd_livello);
        tb_titolo = titoloCrea.creaDocumento(tb_titolo);
        return tb_titolo;

    }

    /**
     * Prepara la lista per il diagnostico, qualora esistano altri autori simili
     * E' copiata pari pari da Creatitolo: sarebbe il caso di farne una sola.
     */
    protected SBNMarc formattaLista(int numeroBlocco)
        throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        int last = numeroBlocco * maxRighe;
        if (last > elencoDiagnostico.size())
            last = elencoDiagnostico.size();
        List lista = elencoDiagnostico.subList((numeroBlocco - 1) * maxRighe, last);
        return FormatoTitolo.formattaLista(
            lista,
            user_object,
            SbnTipoOutput.valueOf("001"),
            null,
            id_lista,
            (numeroBlocco - 1) * maxRighe,
            maxRighe,
            elencoDiagnostico.size(),
            root_object.getSchemaVersion(),
            "3004",
            "Trovati titoli simili",false);
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_1261;
    }
}
