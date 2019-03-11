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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Ts_note_propostaResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Ts_proposta_marcResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.orm.Ts_note_proposta;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 *
 * @author
 * questa classe viene utilizzata per le operazioni sul db e in particolare sulla tabella
 * ts_proposta_marc
 *
 */
public class Proposta extends Ts_proposta_marc{


	private static final long serialVersionUID = -428174522330247451L;
	private boolean 		filtriValorizzati = false;
	private static 		Category log = Category.getInstance("iccu.serversbnmarc.Proposta");

	public Proposta(){

	}

   public Ts_proposta_marc inserisciProposta(
   String idOggetto,
   String user,
   String mittente,
   String messaggio,
   String cd_proposta,
   String tp_messaggio,
   String tipoOggetto) throws EccezioneDB, InfrastructureException {
	   Ts_proposta_marc ts_proposta_marc = new Ts_proposta_marc();
	   ts_proposta_marc.setUTE_INS(user);
	   ts_proposta_marc.setUTE_VAR(user);
	   ts_proposta_marc.setFL_CANC(" ");
	   ts_proposta_marc.setUTE_MITTENTE(mittente);
	   ts_proposta_marc.setID_OGGETTO(idOggetto);
	   ts_proposta_marc.setDS_PROPOSTA(messaggio);
	   ts_proposta_marc.setCD_STATO(cd_proposta);
	   ts_proposta_marc.setTP_MESSAGGIO("P");
	   ts_proposta_marc.setCD_OGGETTO(tipoOggetto);
		Progressivi progress = new Progressivi();
		ts_proposta_marc.setID_PROPOSTA(progress.getNextIdProposta());
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(ts_proposta_marc);


    	tavola.insert(ts_proposta_marc);

    	return this;


   }

	public void inserisciNota(
	int idProposta,
	DestinatarioPropostaType destinatario,
	String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		Ts_note_proposta note = new Ts_note_proposta();
		note.setUTE_INS(user);
		note.setUTE_VAR(user);
		note.setFL_CANC(" ");
		if (destinatario != null) {
            String dest = destinatario.getDestinatarioProposta().getBiblioteca();
            if (destinatario.getDestinatarioProposta().getUserId() != null)
                dest += destinatario.getDestinatarioProposta().getUserId();
            note.setUTE_DESTINATARIO(dest);
        } else
			note.setUTE_DESTINATARIO(user);
		int progressivoRisposta = calcolaProgressivoProposta(idProposta);
		note.setPROGR_RISPOSTA(progressivoRisposta);
		note.setNOTE_PRO(destinatario.getNoteProposta());
		note.setID_PROPOSTA(idProposta);
        Ts_note_propostaResult tavola = new Ts_note_propostaResult(note);


    	tavola.insert(note);

	}

	/**
	 * Method inserisciNota.
	 * @param idProposta
	 * @param utente
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void inserisciNota(int idProposta, String utente) throws IllegalArgumentException, InvocationTargetException, Exception {
		inserisciNota(idProposta, null,utente);
	}

    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }

	private int calcolaProgressivoProposta(int idProposta) throws IllegalArgumentException, InvocationTargetException, Exception{
		int progressivoResult =0;
		Ts_note_proposta note_proposta = new Ts_note_proposta();
		note_proposta.setID_PROPOSTA(idProposta);
		Ts_note_propostaResult tavola = new Ts_note_propostaResult(note_proposta);


		tavola.executeCustom("selectLastProgRisposta");
	    progressivoResult = conta(tavola);

		return progressivoResult;
	}

	public TableDao cercaPropostaPerIdProposta(String idProposta,String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        setID_PROPOSTA(Long.parseLong(idProposta));
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);


    	tavola.executeCustom("selectPerKey",ordinamento);
    	return tavola;
	}

	public TableDao cercaPropostaPerKey(int idProposta) throws EccezioneDB, InfrastructureException {
		setID_PROPOSTA(idProposta);
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
    	return tavola;
	}


	public TableDao cercaPropostaPerTipoOggetto(
	String idProposta,
	String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);


    	tavola.executeCustom("selectPerKey",ordinamento);
    	return tavola;
	}

	public TableDao cercaPropostaPerData(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);

    	tavola.executeCustom("selectPropostaPerData",ordinamento);
    	return tavola;
	}


    public void valorizzaFiltri(CercaPropostaType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Ts_proposta_marc valorizzaFiltri(Ts_proposta_marc proposta, CercaPropostaType cerca) {
		filtriValorizzati = true;
        if (cerca == null)
            return proposta;
        if (cerca.getRangeDate() != null) {
            proposta.settaParametro(TableDao.XXXdata_Da,
	                cerca.getRangeDate().getDataDa().toString());
			proposta.settaParametro(TableDao.XXXdata_A,
	                cerca.getRangeDate().getDataA().toString());
        }
        if (cerca.getMittenteProposta() != null)
        	proposta.setUTE_MITTENTE(	cerca.getMittenteProposta().getBiblioteca() +
							        	(cerca.getMittenteProposta().getUserId()==null?"      ":cerca.getMittenteProposta().getUserId()));
        if (cerca.getIdOggetto() != null)
        	proposta.setID_OGGETTO(cerca.getIdOggetto());
        if (cerca.getIdProposta() != null)
        	proposta.settaParametro(TableDao.XXXid_proposta_string,cerca.getIdProposta().toString());
        return proposta;
    }


	/**
	 * Method cercaPropostaPerIdOggetto.
	 * @param _idProposta
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaPropostaPerIdOggetto(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);

    	tavola.executeCustom("selectPropostaPerIdOggetto", ordinamento);
    	return tavola;
	}

	public TableDao cercaPropostaPerMittente(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(this);

    	tavola.executeCustom("selectPropostaPerMittente",ordinamento);
    	return tavola;
	}

	public List cercaPropostaPerDestinatario(String destinatario, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        List vettoreRisultati = new ArrayList();
		//prima seleziono dalla tabella ts_note_proposta
		Ts_note_proposta note_proposta = new Ts_note_proposta();
		note_proposta.setUTE_DESTINATARIO(destinatario);
		Ts_note_propostaResult note_propostaResult = new Ts_note_propostaResult(note_proposta);

		note_propostaResult.executeCustom("selectPropostaPerDestinatario", ordinamento);
        List temp = note_propostaResult.getElencoRisultati();
		Ts_proposta_marc proposta_marc;
		for (int i=0;i<temp.size();i++){
			long progProposta = ((Ts_note_proposta)temp.get(i)).getID_PROPOSTA();
			proposta_marc = new Ts_proposta_marc();
			proposta_marc.setID_PROPOSTA(progProposta);
	        Ts_proposta_marcResult tavola = new Ts_proposta_marcResult(proposta_marc);


            tavola.valorizzaElencoRisultati(tavola.selectPerKey(proposta_marc.leggiAllParametro()));
	    	vettoreRisultati.add(tavola.getElencoRisultati().get(0));

		}
    	return vettoreRisultati;
	}


	public List cercaNotePropostaPerIdProposta(int idProposta) throws IllegalArgumentException, InvocationTargetException, Exception{
		Ts_note_proposta note_proposta = new Ts_note_proposta();
		note_proposta.setID_PROPOSTA(idProposta);
		Ts_note_propostaResult note_propostaResult = new Ts_note_propostaResult(note_proposta);


		note_propostaResult.executeCustom("selectNotaPerProposta");
        List TableDaoResult = note_propostaResult.getElencoRisultati();

		return TableDaoResult;
	}

	/**
	 * Method aggiornaNoteProposta.
	 * @param _utente
	 * @param _idProposta
	 * @param destinatario
	 * @param note
	 * @throws Exception
	 * @throws Exception
	 */
	public void aggiornaNoteProposta(
		String 	utente,
		int 	idProposta,
		String 	destinatario,
		String 	note,
		TimestampHash timeHash) throws Exception {
		TableDao tavola;
		tavola = cercaPropostaNote(idProposta,destinatario);
        List noteTableDao = tavola.getElencoRisultati();

		for (int i=0;i<noteTableDao.size();i++){
			String time = null;
			Ts_note_proposta note_proposta = new Ts_note_proposta();
			note_proposta = (Ts_note_proposta)noteTableDao.get(i);
			time = timeHash.getTimestamp("Ts_note_proposta", String.format("%d-%d", note_proposta.getID_PROPOSTA(), note_proposta.getPROGR_RISPOSTA()));
			//if ((time != null)&&(!note_proposta.getTS_VAR().equals(time)))
			//	throw new EccezioneSbnDiagnostico(3222,"l'elemento è stato aggiornato precedentemente");
			note_proposta.setNOTE_PRO(note);
			note_proposta.setUTE_VAR(utente);
			Ts_note_propostaResult noteResponse = new Ts_note_propostaResult(note_proposta);


			noteResponse.update(note_proposta);

		}
	}



	public TableDao aggiornaNoteProposta(
	String user,
	DestinatarioPropostaType destinatario) throws IllegalArgumentException, InvocationTargetException, Exception{
		Ts_note_proposta note_proposta = new Ts_note_proposta();
		note_proposta.setNOTE_PRO(destinatario.getNoteProposta());
		note_proposta.setUTE_VAR(user);
		Ts_note_propostaResult note_propostaResult = new Ts_note_propostaResult(note_proposta);


		note_propostaResult.executeCustom("selectNotaPerProposta");
		return note_propostaResult;
	}

	public TableDao cercaPropostaNote(int idProposta, String sbnUser) throws IllegalArgumentException, InvocationTargetException, Exception{
		Ts_note_proposta noteProposta = new Ts_note_proposta();
		noteProposta.setID_PROPOSTA(idProposta);
		noteProposta.setUTE_DESTINATARIO(sbnUser);
		Ts_note_propostaResult tavola = new Ts_note_propostaResult(noteProposta);


		tavola.executeCustom("selectNotaPerPropostaDestinatario");
		return tavola;
	}


	/**
	 * controllo che il client sia presente fra i destinatari
	 * @param user
	 * @param propostaType
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean controllaDestinatari(String user, PropostaType propostaType) throws IllegalArgumentException, InvocationTargetException, Exception {
		TableDao tavola;
		boolean esito = false;
		tavola = cercaPropostaNote(propostaType.getIdProposta(),user);
		if (tavola.getElencoRisultati().size() > 0)
			esito = true;

		return esito;

	}


	/**
	 * controllo che sia compilato destinatarioProposta e si verifica la presenza di
	 * ts_note_proposta con ute_destinatario = destinatarioProposta
	 * se non c'è si crea il record
	 *
	 * @param utente
	 * @param destinatari
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void verificaEsistenzaDestinatario(
		int idProposta,
		String utente,
		DestinatarioPropostaType[] destinatari) throws IllegalArgumentException, InvocationTargetException, Exception {
		TableDao tavola;
		for (int i=0;i<destinatari.length;i++){
            String dest = destinatari[i].getDestinatarioProposta().getBiblioteca();
            if (destinatari[i].getDestinatarioProposta().getUserId() != null)
                dest += destinatari[i].getDestinatarioProposta().getUserId();
            tavola = cercaPropostaNote(idProposta,dest);

			if (tavola.getElencoRisultati().size() == 0)
				inserisciNota(idProposta,destinatari[i],utente);
		}
	}

	/**
	 * Method updateStatoProposta.
	 * @param idProposta
	 * @param utente
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateStatoProposta(
	int idProposta,
	String utente,
	SbnStatoProposta statoProposta) throws IllegalArgumentException, InvocationTargetException, Exception {
		TableDao tavola;
		tavola = cercaPropostaPerKey(idProposta);
        List proposte = tavola.getElencoRisultati();

		if (proposte.size()>0){
			Ts_proposta_marc propostaMarc = new Ts_proposta_marc();
			propostaMarc = (Ts_proposta_marc)proposte.get(0);
			setCD_STATO(statoProposta.toString());
			setUTE_VAR(utente);
			setTS_VAR(propostaMarc.getTS_VAR());
			Ts_proposta_marcResult proposta_marc = new Ts_proposta_marcResult(this);
			proposta_marc.executeCustom("aggiornaStatoProposta");
		}
	}

    public void updateProposta(Ts_proposta_marc proposta) throws EccezioneDB, InfrastructureException {
        Ts_proposta_marcResult proposta_marc = new Ts_proposta_marcResult(proposta);
        proposta_marc.update(proposta);
    }
}
