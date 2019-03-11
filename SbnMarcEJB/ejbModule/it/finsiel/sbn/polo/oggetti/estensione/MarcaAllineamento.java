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
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoMarca;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.AllineaMarcaBiblioteca;
import it.finsiel.sbn.polo.oggetti.AllineamentoMarca;
import it.finsiel.sbn.polo.oggetti.Biblioteca;
import it.finsiel.sbn.polo.oggetti.MarcaBiblioteca;
import it.finsiel.sbn.polo.orm.Tr_mar_bib;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.finsiel.sbn.polo.orm.viste.Vl_allinea_mar_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltraAllineaTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoModifica;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Esegue le operazioni necessarie ad estrarre le marche da allineare per il polo
 * richiedente:
 * Sequenza operativa:
 * Legge il tipo di allineamento scelto dal polo con il validator
 *
 * SE VALE 'flag' (1):
 * Si utilizza la vista VL_Allinea_mar_bib con la condizione su cd_polo, cd_
 * biblioteca in input:
 *
 * Si effettua la count per verificare se il servizio può essere svolto on-line o
 * batch: se la count supera il valore soglia (da gestire nelle properties: lo
 * stesso delle liste analitiche titoli ) si registra l'elaborazione come batch e
 * si restituisce il numero del batch in output, altrimenti si restituisce la
 * lista secondo il tipo output richiesto.
 *
 * Se SbnTipoOutput 001 si restituisce la lista sintetica  in
 * AllineaInfoType impostando tipoModifica come descritto di seguito .
 *
 * Se SbnTipoOutput 000
 * Si utilizzano i metodi del servizio di ricerca per la preparazione della lista
 * sintetica o analitica.
 * Si imposta l'attributo tipoModifica secondo la regola seguente:
 *
 * se fl_allinea o fl_allinea_sbnmarc= C si imposta 'Legami' (VALUE_0)
 * se fl_allinea o fl_allinea_sbnmarc= S si imposta 'Dati' (VALUE_1)
 * se fl_allinea o fl_allinea_sbnmarc= Z si imposta 'Dati e Legami' (VALUE_5)
 * se fl_allinea o fl_allinea_sbnmarc= X si imposta 'Natura' (VALUE_6)
 * se S o Z e tb_titolo.fl_canc='S' si si imposta 'Cancellato' (VALUE_2)
 *
 * Viene passato un oggetto di tipo AllineaInfoType con id, tipoOutput e tipoModifica
 * valorizzato nel metodo 'preparaOutputPerAllinea'
 *
 * SE VALE 'data' (2):
 * la differenza dall'allineamento per flag è la modalità di select dei dati, che
 * dipende dal range  di data selezionato con fl_gestione = 'S'
 *
 * Da valutare se è sempre elaborazione differita oppure se per intervalli di data
 * piccoli può essere effettuata on-line. L'intervallo di giorni non deve superare
 * un valore di soglia da definire nelle properties
 *
 * L'output prodotto è uguale al caso precedente, l'impostazione di
 * tipoModifica è legata al flag_canc per Cancellato
 * altrimenti sono Dati e Legami
 *
 * usa la stessa vista con where su dataInizio e dataFine su ts_var, e su
 * cd_polo/ cd_biblioteca in input, e eventuali filtri in FiltraAllineaTit
 *
 *
 * @author
 * @version marzo 2003
 */


public class MarcaAllineamento extends AllineaMarcaBiblioteca {


	private static final long serialVersionUID = 2027414964830779108L;
	static Category log = Category.getInstance("iccu.serversbnmarc.marcaAllinea");

   	public MarcaAllineamento(){
   		super();
   	}

	public TableDao allineatoMarca1(
	    String flagAllineamento,
		String utente,
		SbnTipoLocalizza tipoInfo,
		List biblioV,
		SbnDatavar dataInizio,
		SbnDatavar dataFine,
		FiltraAllineaTitType filtraAllinea,
		boolean differita) throws IllegalArgumentException, InvocationTargetException, Exception {

        List bibliotecaTableDao = new ArrayList();
        List codiceBiblioteca = new ArrayList();

		String codicePolo = null;
		log.info("VERIFICA Marca "+ biblioV.size());
		for (int i=0;i<biblioV.size();i++){
		//verifico esistenza di tb_biblioteca
			Biblioteca biblioteca = new Biblioteca();

			bibliotecaTableDao = biblioteca.verificaEsistenza(new String(),(String)biblioV.get(i));
			if (bibliotecaTableDao != null){
				//verifico le abilitazioni dell'utente per gestire tb_biblioteca
				if (i > 0) {
				} else {
					log.info("STO PER VERIFICARE abilitazioni polo");
					codicePolo = ((String)biblioV.get(i)).substring(0,3);
					biblioteca.verificaAbilitazioni(codicePolo);
					log.info("VERIFICATO abilitazioni polo "+ codicePolo);
				}
                if ((((String)biblioV.get(i)).trim()).length() > 3) {
                	codiceBiblioteca.add(((String)biblioV.get(i)).substring(3,6));
                } else {
	               	codiceBiblioteca = null;
                }
			}else {
				throw new EccezioneSbnDiagnostico(3101);
			}
		}

		log.info("caricato polo "+ codicePolo);
		log.info("caricato biblioteca "+ codiceBiblioteca);

		return verificaEsistenzaVl_allinea_mar_bib(flagAllineamento,codicePolo,codiceBiblioteca,
											tipoInfo,dataInizio,dataFine,filtraAllinea,differita);
        }
        public List allineatoMarca2(
                String flagAllineamento,
                String utente,
                List vlMarBib,
                SbnTipoOutput tipoOutput) throws IllegalArgumentException, InvocationTargetException, Exception {
            List TableDao_response = new ArrayList();
        if (vlMarBib != null) {
			log.info("esistenza vl_allinea_mar_bib ");
			for (int j=0; j<vlMarBib.size(); j++){
		        Vl_allinea_mar_bib  vl_mar_bib = (Vl_allinea_mar_bib)vlMarBib.get(j);
		        log.info("mid                 >>"+j+"<<"+vl_mar_bib.getMID());
				log.info("fl_allinea"+"_sbnmarc >>"+"<<"+ vl_mar_bib.getFL_ALLINEA_SBNMARC());
				log.info("fl_allinea"+"         >>"+"<<"+ vl_mar_bib.getFL_ALLINEA());
				log.info("fl_canc   "+"         >>"+"<<"+ vl_mar_bib.getFL_CANC());
                OggettoVariatoType outputV = new OggettoVariatoType();
                if (flagAllineamento.equals("1")) {
	                if ((vl_mar_bib.getFL_ALLINEA()).equals("C") ||
	                	(vl_mar_bib.getFL_ALLINEA_SBNMARC()).equals("C")) {
	                	outputV.setTipoModifica(SbnTipoModifica.VALUE_0);
	                }
	                if ((vl_mar_bib.getFL_ALLINEA()).equals("S") ||
	                	(vl_mar_bib.getFL_ALLINEA_SBNMARC()).equals("S")) {
	                    if ((vl_mar_bib.getFL_CANC()).equals("S"))  {
	                		outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
	                	} else {
	                    }
	                }
	                if ((vl_mar_bib.getFL_ALLINEA()).equals("Z") ||
	                	(vl_mar_bib.getFL_ALLINEA_SBNMARC()).equals("Z")) {
	                    if ((vl_mar_bib.getFL_CANC()).equals("S"))  {
	                		outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
	                	} else {
	                    }
	                }
	                if ((vl_mar_bib.getFL_ALLINEA()).equals("X") ||
	                	(vl_mar_bib.getFL_ALLINEA_SBNMARC()).equals("X")) {
	                	outputV.setTipoModifica(SbnTipoModifica.VALUE_6);
	                }
	                AllineaInfoType output = new AllineaInfoType();
		    	        output.setT001(vl_mar_bib.getMID());
						output.setOggettoVariato(outputV);
	                    if (SbnTipoModifica.VALUE_2.getType() == outputV.getTipoModifica().getType()) {
	                    	TableDao_response.add(output);
	                    } else {
 								output = preparaOutputPerAllinea(output, tipoOutput, utente);
								TableDao_response.add(output);
	                    }
                } else {
           //  flag allineamento = 2 per data
		                AllineaInfoType output = new AllineaInfoType();
		    	        output.setT001(vl_mar_bib.getMID());
	                    outputV.setTipoModifica(SbnTipoModifica.VALUE_5);
	                    if ((vl_mar_bib.getFL_CANC()).equals("S")) {
	                    		outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
	                    }
						output.setOggettoVariato(outputV);
	                    if (SbnTipoModifica.VALUE_2.getType() == outputV.getTipoModifica().getType()) {
	                    	TableDao_response.add(output);
	                    } else {
								output = preparaOutputPerAllinea(output, tipoOutput, utente);
								TableDao_response.add(output);
	                    }

                }
			}
		} else {
			log.info("non esiste su vl_allinea_mar_bib ");
				throw new EccezioneSbnDiagnostico(3001);
		}
		return TableDao_response;
	}


	public AllineaInfoType preparaOutputPerAllinea(AllineaInfoType output, SbnTipoOutput tipo, String utente) throws IllegalArgumentException, InvocationTargetException, Exception {
        SbnUserType user = new SbnUserType();
        user.setBiblioteca(utente.substring(0,6));
        user.setUserId(utente.substring(6));
        return FormatoMarca.formattaElementoPerAllinea(output,user,tipo);
	}

    /**
     * Provvede ad aggiornare i flag di allineamento di un titolo modificato.
     * @param flagAllineamento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaFlagAllineamento(AllineamentoMarca flagAllineamento, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String polo = utente.substring(0, 3);
        MarcaBiblioteca titBib = new MarcaBiblioteca();
        List v = titBib.cercaPerAllineamento(flagAllineamento.getMarca().getMID(), polo);
        String prec_polo = "";
        boolean prec_polo_allinea = false;

        for (int i = 0; i < v.size(); i++) {
            Tr_mar_bib tb = (Tr_mar_bib) v.get(i);
            String curr_polo = tb.getCD_POLO();
            if (curr_polo.equals(prec_polo)) {
                if (prec_polo_allinea)
                    aggiornaFlagPolo(curr_polo, flagAllineamento, utente, tb);
            } else {
                prec_polo = curr_polo;
                if (verificaTipoAllineamento(curr_polo)) {
                    prec_polo_allinea = true;
                    aggiornaFlagPolo(curr_polo, flagAllineamento, utente, tb);
                } else {
                    prec_polo_allinea = true;
                }
            }
        }
    }

    /**
     * Verifica se il tipo di allineamento relativo al polo è uguale a 1.
     * Se non si tratta di polo dovrebbe ritornare false.
     * @param polo
     * @return true se verificato
     */
    public boolean verificaTipoAllineamento(String polo) {
        Tbf_parametro par = ValidatorProfiler.getInstance().getTb_parametro(polo);
        if (par != null)
            return par.getTp_all_pref().trim().equals("1");
        else
            return false;
    }

    /**
     * Aggiorna un tr_aut_bib settando tutti i flag di allineamento nella maniera opportuna.
     * @param polo Il codice polo
     * @param flagAll oggetto allineamentoAutore contenente le informazioni di modifica
     * @param utente Codice utente da inserire in ute_var
     * @param tb oggetto tr_aut_bib da modificare
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaFlagPolo(String polo, AllineamentoMarca flagAll, String utente, Tr_mar_bib tb)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        MarcaBiblioteca autBib = new MarcaBiblioteca();
        tb.setCD_POLO(polo);
        tb.setUTE_VAR(utente);
        tb.setFL_ALLINEA(flagAll.getFlagAllinea());
        tb.setFL_ALLINEA_SBNMARC(flagAll.getFlagAllineaSbnmarc());
        autBib.aggiornaTuttiFlAllinea(tb);
    }


}

