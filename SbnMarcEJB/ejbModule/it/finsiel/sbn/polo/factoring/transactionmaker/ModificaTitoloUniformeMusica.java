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
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloModifica;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeMusicaCrea;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloUniformeMusicaValida;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloValidaLegamiModifica;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A230;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Classe ModificaTitoloUniformeMusica
 * <p>
 * Modifica un titolo uniforme musica e i relativi legami.
 * </p>
 *
 * @author
 * @author
 *
 * @version 31-mar-2003
 */
public class ModificaTitoloUniformeMusica extends ModificaElementoAutFactoring {

	private TimestampHash timeHash = new TimestampHash();
	private String T001 = null;
	private TitoloUniformeMusicaType datiDoc = null;
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
	 * Constructor ModificaTitolo.
	 * @param sbnmarcObj
	 */
	public ModificaTitoloUniformeMusica(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		datiDoc = (TitoloUniformeMusicaType) datiElementoAut;
		T001 =  datiDoc.getT001();
        setTp_materiale("U");
		livelloAut = datiDoc.getLivelloAut();
		statoRecord = datiDoc.getStatoRecord();
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
        TitoloUniformeMusicaType datiResp = new TitoloUniformeMusicaType();
        datiResp.setT001(titoloModificato.getBID());
        SbnDatavar data = new SbnDatavar(titoloModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        A230 a230 = new A230();
        a230.setA_230(titoloModificato.getISBD());
        datiResp.setT230(a230);
        datiResp.setLivelloAut(datiElementoAut.getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.UM);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
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
        TitoloUniformeMusicaValida titoloValida = new TitoloUniformeMusicaValida(modificaType);
		TimestampHash timeHash = new TimestampHash();
		Tb_titolo titolo = titoloValida.validaPerModifica(getCdUtente(),_cattura);
		timeHash.putTimestamp("Tb_titolo",titolo.getBID(),new SbnDatavar( titolo.getTS_VAR()));
        AllineamentoTitolo flagAll = new AllineamentoTitolo(titolo);
        TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
        timbroCondivisione.modificaTimbroCondivisione(titolo,user_object.getBiblioteca() + user_object.getUserId(),_condiviso);

		elencoDiagnostico = titoloValida.getElencoDiagnostico();
		if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
		    settaIdLista();
            object_response = formattaLista(1);
			return;
		}

		//validaLegami(titolo);
		if (modificato) {
			titolo = eseguiModificaTitolo(titolo, modificaType, flagAll);
            flagAll.setTb_titolo(true);
		}

		TitoloValidaLegamiModifica titValLeg = new TitoloValidaLegamiModifica(titolo.getBID(),modificaType.getElementoAut().getLegamiElementoAut());
		titValLeg.validaLegamiDocumentoModifica(userID,timeHash,titolo, _cattura);
		TitoloModifica titoloModifica = new TitoloModifica(modificaType);
		if (modificato) {
			titoloModifica.eseguiUpdate(titolo);
            new TitoloUniformeMusicaCrea(modificaType).modificaComposizione(titolo);
		} else {
            titoloModifica.updateVersione(titolo.getBID(),userID);
        }
		titoloModifica.modificaLegami(
				modificaType.getElementoAut().getLegamiElementoAut(),
				timeHash,
				titolo,
				flagAll, null);

        new TitoloGestisceAllineamento().aggiornaFlagAllineamento(flagAll , getCdUtente());
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
	private Tb_titolo eseguiModificaTitolo(Tb_titolo tb_titolo, ModificaType modifica , AllineamentoTitolo flagAll)
		throws IllegalArgumentException, InvocationTargetException, Exception {
		String cd_livello = datiDoc.getLivelloAut().toString();
		TitoloUniformeMusicaValida validaTitolo = new TitoloUniformeMusicaValida(modifica);
		//validaTitolo.validaPerModifica(getCdUtente());
		String cd_natura = "A";
		TitoloUniformeMusicaCrea titoloCrea = new TitoloUniformeMusicaCrea(modifica);
		if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
			tb_titolo.setUTE_FORZA_VAR(userID);
		tb_titolo.setUTE_VAR(userID);

		//il cd livello potrebbe forse essere spostato.
		tb_titolo.setCD_LIVELLO(cd_livello);

		tb_titolo.setCD_NATURA(cd_natura);
		tb_titolo = titoloCrea.creaDocumento(tb_titolo);
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
		List lista =
			elencoDiagnostico.subList((numeroBlocco - 1) * maxRighe, last);
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
        return CodiciAttivita.getIstance().MODIFICA_TITOLO_UNIFORME_MUSICA_1262;
    }

}
