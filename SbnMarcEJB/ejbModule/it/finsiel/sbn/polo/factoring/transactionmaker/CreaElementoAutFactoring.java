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
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.C215;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import org.apache.log4j.Category;


/**
 * Classe CreaElementoAutFactoring
 * <p>
 * Classe che esegue l'operazione di CreaElementoAut, richiesta in input via xml.
 * </p>
 */
public abstract class CreaElementoAutFactoring extends CreaFactoring
{
   private String T001 = null;
   private String T005 = null;
   private String tipoControllo = new String ();

   /**
    * stringa risultato
    */
   private String transactionResult;
   private long currentTime;

   private String livello_response;
   private String stato_response;
   private String genere1_response;
   private String livello_codifica;
   private String bid_response;
   private String datavar_response;
   private String datains_response;

   /**
    * file di log
    */
   static Category log = Category.getInstance("iccu.sbnmarcserver.CreaElementoAutFactoring");

   /**
    * Eventuale Eccezione from DB
    * Utile per generare la risposta
    */
   EccezioneDB exception_archivio = null;

   /**
    * Attributi da XML-input
    */
   private ElementAutType elementoAut;
   private DatiElementoType datiElementoAut;
   private A100 T100 = null;
   private C215 sbn_t215 = null;
   private int tipoAuthority;
   private SBNMarc _input_root_object;
   private String  _condiviso;

   /**
    * Metodo costruttore classe di factoring
    * <p>
    * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
    * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
    * </p>
    * @param  SBNMarc oggetto radice XML in input
    * @param input_root_object
    */
   public CreaElementoAutFactoring(SBNMarc input_root_object)
   {
		super(input_root_object) ;
		_input_root_object=input_root_object;
		elementoAut      = factoring_object.getCreaTypeChoice().getElementoAut() ;
		datiElementoAut  = elementoAut.getDatiElementoAut() ;
		T001 = datiElementoAut.getT001() ;
		T005 = datiElementoAut.getT005() ;
		T100 = datiElementoAut.getT100() ;

		tipoAuthority = datiElementoAut.getTipoAuthority().getType();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElementoAut.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//	    if (datiElementoAut.getCondiviso() != null && (datiElementoAut.getCondiviso().equals(DatiElementoTypeCondivisoType.S))) {
//			_condiviso = datiElementoAut.getCondiviso().toString();
//		} else {
//			_condiviso = "n";
//		}

   }

   /**
    * getFactoring - livello azione
    * <p>
    * Metodo che ritorna se stesso come factoring da utilizzare per effettuare azioni
    * specifiche
    * </p>
    * @param  nessuno
    * @param sbnmarcObj
    * @return void
    */
	static public Factoring getFactoring(SBNMarc sbnmarcObj) throws EccezioneFactoring {
		Factoring current_factoring = null;
		CreaType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getCrea();
		SbnAuthority authority = factoring_object.getCreaTypeChoice().getElementoAut().getDatiElementoAut().getTipoAuthority();
		if (authority.getType() == SbnAuthority.LU_TYPE)  {
			CreaLuogo creaLuogo=null;
				try {
					creaLuogo = new CreaLuogo(sbnmarcObj);
				} catch (EccezioneDB e) {
				} catch (Exception e) {
				}
			current_factoring=creaLuogo;
   		} else
        if (authority.getType() ==  SbnAuthority.valueOf("AU").getType() ){
            current_factoring = new CreaAutore(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("MA").getType() ){
            current_factoring = new CreaMarca(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("TU").getType() ){
            current_factoring = new CreaTitoloUniforme(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("UM").getType() ){
            current_factoring = new CreaTitoloUniformeMusica(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("CL").getType() ){
            current_factoring = new CreaClasse(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("SO").getType() ){
            current_factoring = new CreaSoggetto(sbnmarcObj);
        } else
        if (authority.getType() ==  SbnAuthority.valueOf("DE").getType() ){
            current_factoring = new CreaDescrittore(sbnmarcObj);
        }
		else
        if (authority.getType() ==  SbnAuthority.valueOf("AB").getType() ){
            current_factoring = new CreaAbstract(sbnmarcObj);
        }
		else
	    if (authority.getType() ==  SbnAuthority.valueOf("TH").getType() ){
	        current_factoring = new CreaTermineThesauro(sbnmarcObj);
	    }
	    else
	    if (authority.getType() ==  SbnAuthority.valueOf("RE").getType() ){
	        current_factoring = new CreaRepertorio(sbnmarcObj);
	    }
		else
            throw new EccezioneFactoring(100);
		//log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");


        // Mi faccio tornare l'ennesimo livello, quello che esegue l'azione
        //if (current_factoring != null)
        //    current_factoring = current_factoring.getFactoring(sbnmarcObj) ;

        return current_factoring;

		//return (this);
   }

   public String getIdAttivita() {
	   //almaviva5_20140217 evolutive google3
	   if (_cattura)
		   return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;

	   return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
   }


}
