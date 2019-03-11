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
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;


/**
 * Classe FondeElementoAutFactoring
 *
 * Gestisce la fusione o lo spostamento legame tra due elementi di authority
 *
 * Riceve in input la richiesta di fusione
 * Valida la richiesta secondo l'utente, tramite lo scheduler:
 * . abilitazione all'attività (da verificare la necessità di parcellizzare le
 * attività sul tipo elemento di authority)
 *
 * Attiva la classe opportuna secondo il tipo di authority richiesto.
 */
public abstract class FondeElementoAutFoctoring extends FondeFactoring {

   public FondeElementoAutFoctoring(SBNMarc input_root_object) {
        super(input_root_object) ;
   }

    static public Factoring getFactoring(SBNMarc sbnmarcObj)
        throws EccezioneFactoring
    {
		FondeType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getFonde();
        Factoring current_factoring = null;
        if (factoring_object.getTipoOggetto().getTipoMateriale()!= null) {
        	current_factoring = new FondeDocumento(sbnmarcObj);
        } else {
			SbnAuthority authority = factoring_object.getTipoOggetto().getTipoAuthority();
			if (authority.getType() == SbnAuthority.AU_TYPE){
			    current_factoring = new FondeAutore(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.MA_TYPE){
			    current_factoring = new FondeMarca(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.SO_TYPE){
			    current_factoring = new FondeSoggetto(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.TH_TYPE){
			    current_factoring = new FondeThesauro(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.LU_TYPE){
			    current_factoring = new FondeLuogo(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.CL_TYPE){
			    current_factoring = new FondeClasse(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.TU_TYPE){
			    current_factoring = new FondeTitoloUniforme(sbnmarcObj);
			}else if (authority.getType() == SbnAuthority.UM_TYPE){
			    current_factoring = new FondeTitoloUniformeMusica(sbnmarcObj);
			}else throw new EccezioneFactoring(100);
		}


        return current_factoring;
    }

	public String getIdAttivita() {
		return CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_1027;
	}

    /**
     * Metodo per il controllo delle autorizzazioni
     */
    public void verificaAbilitazioni() throws EccezioneAbilitazioni, EccezioneSbnDiagnostico {
        //if (!validator.verificaAttivita(getCdUtente(), getIdAttivita())) {
            if (!validator.verificaAttivitaID(getCdUtente(), getIdAttivitaSt())) {
                throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
            }
        //}
    }

}
