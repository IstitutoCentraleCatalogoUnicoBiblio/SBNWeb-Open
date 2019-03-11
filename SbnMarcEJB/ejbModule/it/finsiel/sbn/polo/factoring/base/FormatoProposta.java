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
package it.finsiel.sbn.polo.factoring.base;


import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.oggetti.Proposta;
import it.finsiel.sbn.polo.orm.Ts_note_proposta;
import it.finsiel.sbn.polo.orm.Ts_proposta_marc;
import it.iccu.sbn.ejb.model.unimarcmodel.DestinatarioPropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.PropostaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnStatoProposta;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Category;
import org.exolab.castor.types.Date;

/**
 * @author
 *
 */
public class FormatoProposta {
    static Category log = Category.getInstance("iccu.box.FormatoProposta");

	public FormatoProposta(){
	}


	public PropostaType formattaProposta(Ts_proposta_marc proposta, SbnTipoOutput tipoOut) throws IllegalArgumentException, InvocationTargetException, Exception{
		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaPropostaPerEsame(proposta);
		else return formattaPropostaPerLista(proposta);
	}

    public PropostaType formattaPropostaPerEsame(Ts_proposta_marc proposta) throws IllegalArgumentException, InvocationTargetException, Exception {
		PropostaType dati = new PropostaType();
        SbnUserType mitt = new SbnUserType();
        mitt.setBiblioteca(proposta.getUTE_MITTENTE().substring(0,6));
        mitt.setUserId(proposta.getUTE_MITTENTE().substring(6));
        dati.setMittenteProposta(mitt);

		dati.setStatoProposta(SbnStatoProposta.valueOf(proposta.getCD_STATO().trim()));
		dati.setTestoProposta(proposta.getDS_PROPOSTA());
		dati.setIdOggetto(proposta.getID_OGGETTO());
		dati.setIdProposta((int) proposta.getID_PROPOSTA());
        try {
            SbnData data = new SbnData(proposta.getDT_INOLTRO());
    		dati.setDataInserimento(new Date(data.getXmlDate()));
        } catch (ParseException e) {
            log.error("Parsing error di una data in dt_inoltro, "+e);
        }
		SbnOggetto sbnOggetto = new SbnOggetto();
        dati.setTipoOggetto(sbnOggetto);
		if (proposta.getCD_OGGETTO().equals("AU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.AU);
		else if (proposta.getCD_OGGETTO().equals("CL"))
			sbnOggetto.setTipoAuthority(SbnAuthority.CL);
		else if (proposta.getCD_OGGETTO().equals("DE"))
			sbnOggetto.setTipoAuthority(SbnAuthority.DE);
		else if (proposta.getCD_OGGETTO().equals("LU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.LU);
		else if (proposta.getCD_OGGETTO().equals("MA"))
			sbnOggetto.setTipoAuthority(SbnAuthority.MA);
		else if (proposta.getCD_OGGETTO().equals("RE"))
			sbnOggetto.setTipoAuthority(SbnAuthority.RE);
		else if (proposta.getCD_OGGETTO().equals("SO"))
			sbnOggetto.setTipoAuthority(SbnAuthority.SO);
		else if (proposta.getCD_OGGETTO().equals("TU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.TU);
		else if (proposta.getCD_OGGETTO().equals("UM"))
			sbnOggetto.setTipoAuthority(SbnAuthority.UM);
		else {
			if ((proposta.getCD_OGGETTO() != null) && (!proposta.getCD_OGGETTO().trim().equals("")))
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(proposta.getCD_OGGETTO().trim()));
			else sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_7);
		}

//cercare le note relative

		Proposta propostaPerNota = new Proposta();
        List vettoreRisultati = propostaPerNota.cercaNotePropostaPerIdProposta((int) proposta.getID_PROPOSTA());
		Ts_note_proposta note_proposta;
		for (int i=0;i<vettoreRisultati.size();i++){
			note_proposta = new Ts_note_proposta();
			note_proposta = (Ts_note_proposta) vettoreRisultati.get(i);
            DestinatarioPropostaType dest = new DestinatarioPropostaType();
            String destin=note_proposta.getUTE_DESTINATARIO();
            SbnUserType duser = new SbnUserType();
            duser.setBiblioteca(destin.substring(0,6));
            duser.setUserId(destin.substring(6));
            dest.setDestinatarioProposta(duser);

            dest.setNoteProposta(note_proposta.getNOTE_PRO());
            try {
                SbnData date = new SbnData(note_proposta.getTS_INS());
                dest.setDataRisposta(new Date(date.getXmlDate()));
                //dest.setDataRisposta(new Date(note_proposta.getTS_INS().getXmlDate()));
            } catch (ParseException e) {
                log.error("Parsing error di una data in ts_note_proposta,"+e);
            }
			dati.addDestinatarioProposta(dest);
		}


        return dati;
    }

//a differenza dell'esame analitico con questo metodo non inoltriamo le note
    public PropostaType formattaPropostaPerLista(Ts_proposta_marc proposta) {
		PropostaType dati = new PropostaType();
        SbnUserType mitt = new SbnUserType();
        mitt.setBiblioteca(proposta.getUTE_MITTENTE().substring(0,6));
        mitt.setUserId(proposta.getUTE_MITTENTE().substring(6));
        dati.setMittenteProposta(mitt);

		dati.setStatoProposta(SbnStatoProposta.valueOf(proposta.getCD_STATO().trim()));
		dati.setTestoProposta(proposta.getDS_PROPOSTA());
		dati.setIdOggetto(proposta.getID_OGGETTO());
		dati.setIdProposta((int) proposta.getID_PROPOSTA());
		SbnOggetto sbnOggetto = new SbnOggetto();
		if (proposta.getCD_OGGETTO().equals("AU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.AU);
		else if (proposta.getCD_OGGETTO().equals("CL"))
			sbnOggetto.setTipoAuthority(SbnAuthority.CL);
		else if (proposta.getCD_OGGETTO().equals("DE"))
			sbnOggetto.setTipoAuthority(SbnAuthority.DE);
		else if (proposta.getCD_OGGETTO().equals("LU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.LU);
		else if (proposta.getCD_OGGETTO().equals("MA"))
			sbnOggetto.setTipoAuthority(SbnAuthority.MA);
		else if (proposta.getCD_OGGETTO().equals("RE"))
			sbnOggetto.setTipoAuthority(SbnAuthority.RE);
		else if (proposta.getCD_OGGETTO().equals("SO"))
			sbnOggetto.setTipoAuthority(SbnAuthority.SO);
		else if (proposta.getCD_OGGETTO().equals("TU"))
			sbnOggetto.setTipoAuthority(SbnAuthority.TU);
		else if (proposta.getCD_OGGETTO().equals("UM"))
			sbnOggetto.setTipoAuthority(SbnAuthority.UM);
		else {
			if ((proposta.getCD_OGGETTO() != null) && (!proposta.getCD_OGGETTO().trim().equals("")))
				sbnOggetto.setTipoMateriale(SbnMateriale.valueOf(proposta.getCD_OGGETTO().trim()));
			else sbnOggetto.setTipoMateriale(SbnMateriale.VALUE_5);
		}
		dati.setTipoOggetto(sbnOggetto);
        return dati;
    }

}
