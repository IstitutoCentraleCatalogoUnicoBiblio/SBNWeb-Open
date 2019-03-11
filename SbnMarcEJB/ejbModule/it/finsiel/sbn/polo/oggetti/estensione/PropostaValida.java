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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Ts_note_propostaResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Proposta;
import it.finsiel.sbn.polo.orm.Ts_note_proposta;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * @author
 *
 */
public class PropostaValida extends Proposta{


	private static final long serialVersionUID = 4152228248425516669L;

	public  PropostaValida(){
		super();
	}
	/**
	 * questo metodo controlla se nella tabella ts_note_proposta è presente un record
	 * con ute_destinatario = destinatario passato in input e idProposta, in caso contrario
	 * viene creato il record opportuno
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Ts_proposta_marc validaPerModifica(PropostaType propostaType, String user, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		String mittente;
		DestinatarioPropostaType[] destinatari;
		int idProposta;
		Ts_note_proposta noteProposta;
        String dest = propostaType.getMittenteProposta().getBiblioteca();
        if (propostaType.getMittenteProposta().getUserId() != null)
            dest += propostaType.getMittenteProposta().getUserId();
        mittente = dest;

		destinatari = propostaType.getDestinatarioProposta();
		idProposta = propostaType.getIdProposta();
        TableDao tav = cercaPropostaPerIdProposta(""+idProposta,null);
        List v = tav.getElencoRisultati();

        Ts_proposta_marc ts_proposta = null;
        if (v.size()>0) {
            ts_proposta = (Ts_proposta_marc)v.get(0);
        } else {
            throw new EccezioneSbnDiagnostico(3013,"Proposta non esistente");
        }
        // Bisogna verificare che il mittente sia quello giusto ?
        if (!mittente.equals(ts_proposta.getUTE_MITTENTE())) {
            throw new EccezioneSbnDiagnostico(3287,"Mittente non corretto");
        }
		aggiornaTimeHash(timeHash,idProposta);
        dest = mittente;
		for (int i=0;i<destinatari.length;i++){
			noteProposta = new Ts_note_proposta();
			noteProposta.setID_PROPOSTA(idProposta);
            dest += destinatari[i].getDestinatarioProposta().getBiblioteca();
            if (destinatari[i].getDestinatarioProposta().getUserId() != null) {
                dest += destinatari[i].getDestinatarioProposta().getUserId();
    			noteProposta.setUTE_DESTINATARIO(destinatari[i].getDestinatarioProposta().getUserId());
            }

			Ts_note_propostaResult tavola = new Ts_note_propostaResult(noteProposta);


			tavola.executeCustom("selectNotaPerPropostaDestinatario");
			List vec = tavola.getElencoRisultati();

			if (vec.size() == 0){
				Proposta proposta = new Proposta();
				proposta.inserisciNota(idProposta,destinatari[i],user);
			}
		}
		if (dest.indexOf(user.substring(0,6))<0){
            if (propostaType.getTestoProposta()!= null) {
                if (!ts_proposta.getDS_PROPOSTA().equals(propostaType.getTestoProposta())) {
                    throw new EccezioneSbnDiagnostico(3202, "Il testo può essere modificato solo dal mittente");
                }
            }

			boolean trovato = false;
			int j=0;
			while (!trovato && j<destinatari.length){
				if (destinatari[j].getDestinatarioProposta().equals(user.substring(0,6)))
					trovato = true;
				j++;
			}
			if (!trovato)
				throw new EccezioneSbnDiagnostico(3202, "il client deve essere il mittente o uno dei destinatari");
		}
		if (propostaType.getStatoProposta().getType() == SbnStatoProposta.I_TYPE &&
			!mittente.equals(user))
			throw new EccezioneSbnDiagnostico(3203, "solo il mittente può modificare il testo");
        return ts_proposta;
	}

	private void aggiornaTimeHash(
	TimestampHash timeHash,
	int idProposta) throws IllegalArgumentException, InvocationTargetException, Exception{
		Proposta proposta = new Proposta();
		List vettoreProposte = proposta.cercaNotePropostaPerIdProposta(idProposta);
		for (int i=0;i<vettoreProposte.size();i++){
			Ts_note_proposta noteProposta = new Ts_note_proposta();
			noteProposta = (Ts_note_proposta) vettoreProposte.get(i);
			timeHash.putTimestamp("Ts_note_proposta",
									Long.toString(noteProposta.getID_PROPOSTA())+ noteProposta.getPROGR_RISPOSTA(),
									new SbnDatavar( noteProposta.getTS_VAR()));
		}
	}

}
