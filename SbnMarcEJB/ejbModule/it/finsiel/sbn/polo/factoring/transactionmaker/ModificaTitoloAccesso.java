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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloAccessoCrea;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloAccessoValida;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloModifica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
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
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe ModificaTitoloAccesso
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 18-giu-03
 */
public class ModificaTitoloAccesso extends ModificaFactoring {
    private TimestampHash timeHash = new TimestampHash();
    private String T001 = null;
    TitAccessoType datiDoc = null;

    DocumentoType documento = null;
    private ModificaType modificaType;
    private LegamiType[] legami;

    private SbnLivello livelloAut;
    private SbnSimile tipoControllo;
    private String id_lista = null;
    private Tb_titolo titoloModificato = null;
    private StatoRecord statoRecord;
    private String  _condiviso;

    int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));

    /**
     * Constructor ModificaTitolo.
     * @param sbnmarcObj
     */
    public ModificaTitoloAccesso(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
        documento = modificaType.getDocumento();
        datiDoc = documento.getDocumentoTypeChoice().getDatiTitAccesso();
        T001 = datiDoc.getT001();
        livelloAut = datiDoc.getLivelloAut();
        statoRecord = documento.getStatoRecord();
        //controllare i legami!!!!
        legami = documento.getLegamiDocumento();
        tipoControllo = modificaType.getTipoControllo();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiDoc.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == TitAccessoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiDoc.getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiDoc.getCondiviso() != null && (datiDoc.getCondiviso().equals(DatiDocTypeCondivisoType.S)) ) {
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
        datiResp.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titoloModificato.getCD_LIVELLO())));
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
            if (modificaType.getDocumento().getLegamiDocumentoCount() == 0)
                throw new EccezioneSbnDiagnostico(3090, "Nessuna modifica da apportare");
        }
        TitoloAccessoValida titoloValida = new TitoloAccessoValida(modificaType);
        TimestampHash timeHash = new TimestampHash();
        Tb_titolo titolo = titoloValida.validaPerModifica(getCdUtente(), timeHash,_cattura);
        TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
        timbroCondivisione.modificaTimbroCondivisione(titolo,user_object.getBiblioteca() + user_object.getUserId(),_condiviso);

        timeHash.putTimestamp("Tb_titolo", titolo.getBID(), new SbnDatavar( titolo.getTS_VAR()));
        AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(titolo);
        elencoDiagnostico = titoloValida.getElencoDiagnostico();
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            settaIdLista();
            object_response = formattaLista(1);
            return;
        }
        String cd_natura = titolo.getCD_NATURA();
        String nuovaNatura = titoloValida.estraiNatura(datiDoc);
        boolean naturaCambiata = !cd_natura.equals(nuovaNatura);

        TitoloModifica titoloModifica = new TitoloModifica(modificaType);
        //validaLegami(titolo);
        if (modificato) {
          if (naturaCambiata) {
            titolo.setCD_NATURA(nuovaNatura);
            titoloModifica.modificaNatura(titolo, flagAllineamento);
          } else {
            titolo = eseguiModificaTitolo(titolo, modificaType, flagAllineamento);
            flagAllineamento.setTb_titolo(true);
          }
        }

        if (naturaCambiata == false) {
          if (modificato) {
              titoloModifica.eseguiUpdate(titolo);
          } else {
              titoloModifica.updateVersione(titolo.getBID(), getCdUtente());
          }
          titoloModifica.modificaLegami(
              modificaType.getDocumento().getLegamiDocumento(),
              timeHash,
              titolo,
              flagAllineamento, null);
        }
        new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
        titoloModificato = titoloValida.estraiTitoloPerID(titolo.getBID());
        object_response = formattaOutput();
    }

    /**
     * Esegue la modifica di un titolo
     */
    private Tb_titolo eseguiModificaTitolo(
        Tb_titolo tb_titolo,
        ModificaType modifica,
        AllineamentoTitolo flagAll)
        throws EccezioneDB, EccezioneSbnDiagnostico {
        String cd_livello = datiDoc.getLivelloAut().toString();
        String cd_natura = datiDoc.getNaturaTitAccesso().toString();
        TitoloAccessoCrea titoloModifica = new TitoloAccessoCrea(modifica);
        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
            tb_titolo.setUTE_FORZA_VAR(getCdUtente());
        tb_titolo.setUTE_VAR(getCdUtente());

        //il cd livello potrebbe forse essere spostato.
        tb_titolo.setCD_LIVELLO(cd_livello);
        if (!tb_titolo.getCD_NATURA().equals(cd_natura))
            flagAll.setNatura(true);
        tb_titolo.setCD_NATURA(cd_natura);
        String nota = "";

        // GESTIONE DELLE NOTE AGGIUNTIVE 3204
        // Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
	    // CODICE ORIGINALE DA ripristinare in caso di deploy in esercizio
		// ( vale solo se non viene deployata l'ID con le nuove modifiche per la gestione delle note)
		// In caso di deploy di ID in esercizio togliere questo commento
//        if (!nota.equals(""))
//            tb_titolo.setNOTA_CAT_TIT(nota);
		if (!nota.equals("")){
			if (nota.length() > 319){
				nota = nota.substring(0,319);
				tb_titolo.setNOTA_CAT_TIT(nota);
			} else {
				tb_titolo.setNOTA_CAT_TIT(nota);
			}
		}
        // Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274
		 // END GESTIONE DELLE NOTE AGGIUNTIVE 3204

        String materiale = tb_titolo.getTP_MATERIALE();
        tb_titolo = titoloModifica.creaTitoloAccessso(tb_titolo);
        if (!tb_titolo.getTP_MATERIALE().equals(materiale))
            flagAll.setMateriale(true);

        return tb_titolo;

    }

    /** Prepara la lista per il diagnostico, qualora esistano altri autori simili
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

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023;
    }

}
