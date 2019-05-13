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

import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.periodici.ejb.Picos;
import it.iccu.sbn.periodici.ejb.PicosHome;
import it.iccu.sbn.periodici.vo.SchedaInventarioVO;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Classe FondeFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Fonde"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 * </p>
 * Attiva il factoring opportuno secondo la richiesta ricevuta: se c'è
 * tipoMateriale attiva FondeDocumento, altrimenti FondeElementoAutFactoring
 */
public class MServDocFactoring extends Factoring {

	protected MServDoc factoring_object = null;

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.MServDocFactoring");

	/**
	 * Metodo costruttore classe di factoring
	 * <p>
	 * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
	 * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
	 * </p>
	 * @param  SBNMarc oggetto radice XML in input
	 * @return istanza oggetto (default)
	 */
	//costruttore
	public MServDocFactoring(SBNMarc input_root_object) {
		// Assegno radice e classi XML principali
		super(input_root_object);

		// Assegno classi specifiche per questo factoring
		factoring_object = servDoc;
	}

	/**
	 * getFactoring - ritorna il Factoring opportuno
	 * <p>
	 * Metodo che lancia il Factoring verificando le informazioni ricevute in input
	 * La Request verificata è di tipo SBnRequest (XML)
	 * </p>
	 * @param  nessuno
	 * @return void
	 */
	public static Factoring getFactoring(SBNMarc sbnmarcObj)
			throws EccezioneFactoring {
		Factoring current_factoring = null;
		log.debug("STO PER VERIFICARE COSA LANCIARE");
		// Creo il secondo livello di factoring (valutando input)

		MServDoc factoring_object = sbnmarcObj.getSbnMessage().getMServDoc();
		if (factoring_object.getAskdoc() != null) {
			current_factoring = new CercaMservDoc(sbnmarcObj);
		} else if (factoring_object.getAskinv() != null) {
			current_factoring = new CercaMservInv(sbnmarcObj);
		} else if (factoring_object.getAskfrn() != null) {
			current_factoring = new CercaMservFrn(sbnmarcObj);
		} else
			throw new EccezioneFactoring(100);
		//log.info("HO CREATO IL CURRENT FACTORING:" + current_factoring + ":");

		return current_factoring;
	}

	/**
	 * Verifica le abilitazioni specifiche per la fusione. L'utente deve essere abilitato
	 * ad una delle funzioni 1024, 1200, 1201, 1202.
	 */
	public void verificaAbilitazioni() throws EccezioneAbilitazioni,
			EccezioneSbnDiagnostico {
		if (!validator.verificaAttivitaID(getCdUtente(), CodiciAttivita
				.getIstance().FONDE_DOCUMENTI_1024)
				&& !!validator.verificaAttivitaID(getCdUtente(), CodiciAttivita
						.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200)
				&& !validator
						.verificaAttivitaID(
								getCdUtente(),
								CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201)
				&& !!validator
						.verificaAttivitaID(
								getCdUtente(),
								CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202))
			throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
	}

	final public void proseguiTransazione() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		preparaOutput();
	}

	protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring,
			EccezioneSbnDiagnostico, IllegalArgumentException,
			InvocationTargetException, Exception {
		log.error("PREPARA OUTPUT NON IMPLEMENTATO");
		throw new EccezioneSbnDiagnostico(1, "Codice non implementato");
	}

	protected void executeCerca() throws EccezioneAbilitazioni, EccezioneDB,
			EccezioneSbnDiagnostico, IllegalArgumentException,
			InvocationTargetException, Exception {
		throw new EccezioneSbnDiagnostico(1, "Codice non implementato");
	}

	protected Inventario formattaInventario(SchedaInventarioVO inventarioVo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
				Inventario inventario = new Inventario();
				inventario.setColloc(inventarioVo.getColloc());
				inventario.setData(inventarioVo.getData());
				inventario.setKbibl(inventarioVo.getKbibl());
				//almaviva5_20100616
				String kserie = inventarioVo.getKserie();
				//almaviva5_20101022 se non impostata valorizzata a serie spazio
				inventario.setKserie(ValidazioneDati.isFilled(kserie) ? kserie : ResourceLoader.getPropertyString("SERIE_SPAZIO"));

				inventario.setNinvent(inventarioVo.getNinvent());
				inventario.setPrecis(inventarioVo.getPrecis());
				inventario.setSequenza(inventarioVo.getSequenza());
				inventario.setSezione(inventarioVo.getSezione());
				inventario.setSpecific(inventarioVo.getSpecific());
				inventario.setTipocirc(inventarioVo.getTipocirc());
				inventario.setTipomat(inventarioVo.getTipomat());
				inventario.setTipoprov(inventarioVo.getTipoprov());
				inventario.setValore(inventarioVo.getValore());

				return inventario;
			}

	protected Picos getPeriodici() throws NamingException, RemoteException, CreateException {
		InitialContext ctx = new InitialContext();
		PicosHome home = (PicosHome) ctx.lookup(PicosHome.JNDI_NAME);
		return home.create();
	}

}
