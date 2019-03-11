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
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import org.apache.log4j.Category;


public abstract class ModificaElementoAutFactoring extends ModificaFactoring {

    static Category log = Category.getInstance("iccu.sbnmarcserver.ModificaElementoAutFactoring");
    protected ElementAutType      elementoAut ;
    protected DatiElementoType    datiElementoAut ;


	public ModificaElementoAutFactoring(SBNMarc input_root_object){
   		super(input_root_object) ;
        elementoAut = factoring_object.getElementoAut();
        datiElementoAut  = elementoAut.getDatiElementoAut();

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
    static public Factoring getFactoring(SBNMarc sbnmarcObj) throws EccezioneFactoring {
        Factoring current_factoring = null;
		DatiElementoType elementoAut = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getElementoAut().getDatiElementoAut();

        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il factoring (valutando input)
        if (elementoAut.getTipoAuthority().getType() == SbnAuthority.LU_TYPE){
            current_factoring = new ModificaLuogo(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
            current_factoring = new ModificaMarca(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
            current_factoring = new ModificaAutore(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
            current_factoring = new ModificaClasse(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
            current_factoring = new ModificaTitoloUniforme(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
            current_factoring = new ModificaTitoloUniformeMusica(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
            current_factoring = new ModificaSoggetto(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.AB_TYPE) {
            current_factoring = new ModificaAbstract(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
            current_factoring = new ModificaTermineThesauro(sbnmarcObj);
        }
        else if (elementoAut.getTipoAuthority().getType() == SbnAuthority.DE_TYPE) {
            current_factoring = new ModificaDescrittore(sbnmarcObj);
        } else
            throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");


        return current_factoring;
    }

	public String getIdAttivita() {
		// almaviva5_20140217 evolutive google3
		if (_cattura)
			return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;

		return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
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
