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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sog_desResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.DescrittoreDescrittore;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.Tr_soggettari_biblioteche;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_des;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 */
public class DescrittoreValida extends Descrittore {

	private static final long serialVersionUID = -537304051982714590L;

	private static Category log = Category.getInstance("iccu.sbnmarcserver.DescrittoreValida");



	public DescrittoreValida(){
		super();
	}



	/**
	 * Method validaPerCancella.
	 * controllo di esistenza per identificativo,
	 * @param _idCancellazione
	 * @return Tb_descrittore
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_descrittore validaPerCancella(String utente, String idCancellazione) throws IllegalArgumentException, InvocationTargetException, Exception {
	    Tb_descrittore descrittoreTrovato = null;
		descrittoreTrovato = cercaDescrittorePerId(idCancellazione);
		if (descrittoreTrovato == null)
			throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
		//sel la forma è 'A' allora controllo che non ci siano legami con i soggetti
		if (descrittoreTrovato.getTP_FORMA_DES().equals("A"))
			controllalegami(idCancellazione);
		controllaParametriUtente(utente,descrittoreTrovato.getCD_LIVELLO(),false);
		return descrittoreTrovato;
	}

	/**
	 * Method controllalegami.
	 * @param _idCancellazione
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void controllalegami(String did) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_sog_des tr_sog_des = new Tr_sog_des();
		tr_sog_des.setDID(did);
		Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);


		tavola.executeCustom("selectLegameDescrittore");
        List vec = tavola.getElencoRisultati();

		if (vec.size() >0)
			throw new EccezioneSbnDiagnostico(3130,"esistono legami con alcuni soggetti");

	}


	/**
	 * @param idDescrittore
	 * @param livello
	 * @param vettoreParametriSemantici
	 * questo metodo riceve in input un vettore di parametri semantici (calcolato tramite il Validator)
	 *
	 * l'utente deve essere abilitato alla gestione descrittore
	 * l'utente deve essere abilitato alla gestione soggettario
	 * controllo che non esista già il descrittore con uguale k_norm_descritt
	 *
	 * valida legami descrittori-descrittori:
	 * il descrittore deve esistere nel db
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean validaPerCrea(
	String idDescrittore,
	String livello,
	String utente,
	A931 t931,
    boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
	    String c2_931 = null;
		String a_931;
		Tb_descrittore descrittoreTrovato = null;
		if (!idDescrittore.equals(Factoring.SBNMARC_DEFAULT_ID)){

			descrittoreTrovato = verificaEsistenza(idDescrittore);
			if (descrittoreTrovato != null)
				if(!_cattura)
					throw new EccezioneSbnDiagnostico(3012,"elemento già presente in base dati");
		}
		c2_931 = t931.getC2_931();
		a_931 = t931.getA_931();
		controllaVettoreParametriSemantici(c2_931, utente);
		TableDao tavola;
		int contaSimili=0;
		tavola = cercaDescrittorePerk_norm_descrittore(a_931,c2_931);
		contaSimili = tavola.getElencoRisultati().size();

		if (contaSimili >0)
			throw new EccezioneSbnDiagnostico(3012,"descrittore esistente");
		return true;
	}


	public boolean validaPerCreaLegameOrg(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnFormaNome 					tipoForma = null;
		boolean 						validato = false;
		LegamiType[]					legamiType;
		String 							tp_forma;
		ArrivoLegame[] 					arrivoLegame;
		int 							sizeArrivoLegame;
		String 							idPartenza;
		legamiType 	= elementAutType.getLegamiElementoAut();
		int size 	= elementAutType.getLegamiElementoAutCount();
		tipoForma	= elementAutType.getDatiElementoAut().getFormaNome();
		idPartenza  = elementAutType.getDatiElementoAut().getT001();
		if (tipoForma == null)	tipoForma = SbnFormaNome.A;
		Tb_descrittore tb_descrittore = null;
		int i = 0;
		int j;
		boolean esito = true;
		while ((i < size)&(esito == true)){
			arrivoLegame=legamiType[i].getArrivoLegame();
			j=0;
			while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
	        	tb_descrittore = cercaDescrittorePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
				if (tb_descrittore == null)
					throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");
				j++;
			}
			i++;
		}
        for (i = 0; i < size; i++) {
			arrivoLegame=legamiType[i].getArrivoLegame();
			sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
			String idArrivo;
	        for (j = 0; j < sizeArrivoLegame; j++) {
	        	idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
	        	tb_descrittore = cercaDescrittorePerId(idArrivo);
				if (tb_descrittore!=null){
					controllaTipoLegame(arrivoLegame[j].getLegameElementoAut().getTipoLegame());
		        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("USE")){
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.R_TYPE))){
		        			DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
		        			if (descrittoreDes.cercaDescrittoreDescrittore(idPartenza)){
								validato = true;
		        			}
		        		}
		        	}else if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("UF")){
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("R") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
		        			if (descrittoreDes.cercaDescrittoreDescrittore(idArrivo)){
								validato = true;
		        			}
		        		}
		        	}else {
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			validato = true;
		        		}else throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");
		        	}


				}
				else validato=false;
	        }
        }
    	return validato;
   }

//	SELECT * FROM VL_DESCRITTORE_DES WHERE ( did = 'CSWD000363'
//	    AND TP_FORMA_DES ='R')
//	OR ( did = 'CSWD000359'
//	  and did_1 = 'CSWD000363');

	public boolean validaPerCreaLegame(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnFormaNome 					tipoForma = null;
		boolean 						validato = false;
		LegamiType[]					legamiType;
		String 							tp_forma;
		ArrivoLegame[] 					arrivoLegame;
		int 							sizeArrivoLegame;
		String 							idPartenza;
		legamiType 	= elementAutType.getLegamiElementoAut();
		int size 	= elementAutType.getLegamiElementoAutCount();
		tipoForma	= elementAutType.getDatiElementoAut().getFormaNome();
		idPartenza  = elementAutType.getDatiElementoAut().getT001();
		if (tipoForma == null)	tipoForma = SbnFormaNome.A;
		Tb_descrittore tb_descrittore = null;
		int i = 0;
		int j;
		boolean esito = true;
		while ((i < size)&(esito == true)){
			arrivoLegame=legamiType[i].getArrivoLegame();
			j=0;
			while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
	        	tb_descrittore = cercaDescrittorePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
				if (tb_descrittore == null)
					throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");
				j++;
			}
			i++;
		}
		DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
        for (i = 0; i < size; i++) {
			arrivoLegame=legamiType[i].getArrivoLegame();
			sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
			String idArrivo;
	        for (j = 0; j < sizeArrivoLegame; j++) {
	        	idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
	        	tb_descrittore = cercaDescrittorePerId(idArrivo);
				if (tb_descrittore!=null){
					controllaTipoLegame(arrivoLegame[j].getLegameElementoAut().getTipoLegame());
		        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("USE")){
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.R_TYPE))){


		        			descrittoreDes = new DescrittoreDescrittore();
		        			if (descrittoreDes.cercaDescrittoreDescrittore(idPartenza)){
								validato = true;
		        			}
		        			if (descrittoreDes.countLegamiDescrittoreDescrittore(idPartenza, idArrivo) >=1 ){
		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
		        			}
		        		}



		        	}else if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("UF")){
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("R") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			descrittoreDes = new DescrittoreDescrittore();
		        			if (descrittoreDes.cercaDescrittoreDescrittore(idArrivo)){
								validato = true;
		        			}
		        			descrittoreDes.getDID_BASE();
		        			if (descrittoreDes.countLegamiDescrittoreDescrittore(idArrivo,idPartenza) >=1 ){
		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
		        			}
		        		}
		        	}else {
		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
		        			validato = true;
		        		}else throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");
		        	}


				}
				else validato=false;
	        }
        }
    	return validato;
   }

    public boolean validaPerCreaLegameSoggettoDescrittore(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
        SbnFormaNome                    tipoForma = null;
        boolean                         validato = false;
        LegamiType[]                    legamiType;
        String                          tp_forma;
        ArrivoLegame[]                  arrivoLegame;
        int                             sizeArrivoLegame;
        String                          idPartenza;
        legamiType  = elementAutType.getLegamiElementoAut();
        int size    = elementAutType.getLegamiElementoAutCount();
        tipoForma   = elementAutType.getDatiElementoAut().getFormaNome();
        idPartenza  = elementAutType.getDatiElementoAut().getT001();
        if (tipoForma == null)  tipoForma = SbnFormaNome.A;
        Tb_descrittore tb_descrittore = null;
        int i = 0;
        int j;
        boolean esito = true;
		while ((i < size) && (esito == true)) {
			arrivoLegame = legamiType[i].getArrivoLegame();
			j = 0;
			while ((j < legamiType[i].getArrivoLegameCount()) && (esito == true)) {
                tb_descrittore = cercaDescrittorePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
                if (tb_descrittore == null)
                    throw new EccezioneSbnDiagnostico(3213, "elemento legato inesistente");

                //almaviva5_20100201
                if (!tb_descrittore.getTP_FORMA_DES().equals("A"))	//solo des. in forma accettata!!
                	throw new EccezioneSbnDiagnostico(3033);

				// almaviva5_20120323 evolutive CFI: raccordo edizione
                Soggetto soggetto = new Soggetto();
                Tb_soggetto tb_soggetto = soggetto.cercaSoggettoPerId(idPartenza);
				String edSog = tb_soggetto.getCD_EDIZIONE();
				if (edSog != null) {
					String edDes = tb_descrittore.getCD_EDIZIONE();
					if (!ValidazioneDati.in(edDes, SbnEdizioneSoggettario.E.toString(), edSog) )
						throw new EccezioneSbnDiagnostico(3342);
				}

                j++;
            }
            i++;
        }
		for (i = 0; i < size; i++) {
			arrivoLegame = legamiType[i].getArrivoLegame();
			sizeArrivoLegame = legamiType[i].getArrivoLegameCount();
			for (j = 0; j < sizeArrivoLegame; j++) {
				String idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
				tb_descrittore = cercaDescrittorePerId(idArrivo);
				if (tb_descrittore != null) {
					SbnLegameAut tipoLegame = arrivoLegame[j].getLegameElementoAut().getTipoLegame();
					if (tipoLegame.getType() == SbnLegameAut.valueOf("931").getType() )
						validato = true;
					else
						throw new EccezioneSbnDiagnostico(3033,	"tipo legame non valido");
				} else
					validato = false;
			}
		}
        return validato;
   }
	public boolean validaPerModificaLegame(ElementAutType elementAutType, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnFormaNome 					tipoForma = null;
		boolean 						validato = false;
		LegamiType[]					legamiType;
		String 							tp_forma;
		ArrivoLegame[] 					arrivoLegame;
		int 							sizeArrivoLegame;
		String 							idPartenza;
		legamiType 	= elementAutType.getLegamiElementoAut();
		int size 	= elementAutType.getLegamiElementoAutCount();
		tipoForma	= elementAutType.getDatiElementoAut().getFormaNome();
		idPartenza  = elementAutType.getDatiElementoAut().getT001();
		if (tipoForma == null)
			tipoForma = SbnFormaNome.A;

		int i = 0;
		int j;
		boolean esito = true;
		while ((i < size)&(esito == true)){
			arrivoLegame=legamiType[i].getArrivoLegame();
			j=0;
			while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
				Tb_descrittore tb_descrittore = cercaDescrittorePerId(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
				if (tb_descrittore == null)
					throw new EccezioneSbnDiagnostico(3213);	//elemento legato inesistente
				j++;
			}
			i++;
		}
		DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
		List<Tr_des_des> vettoreDiLegami = descrittoreDes.cercaLegame(idPartenza);
		Tr_des_des legame;
		for (int k = 0; k < vettoreDiLegami.size(); k++) {
			legame = vettoreDiLegami.get(k);
			timeHash.putTimestamp("Tr_des_des",
					legame.getDID_BASE() + legame.getDID_COLL(),
					new SbnDatavar(legame.getTS_VAR()));
		}

        for (i = 0; i < size; i++) {
			SbnTipoOperazione tipoOperazione = null;
			arrivoLegame=legamiType[i].getArrivoLegame();
			sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
			tipoOperazione = legamiType[i].getTipoOperazione();
	        for (j = 0; j < sizeArrivoLegame; j++) {
	        	String idArrivo = arrivoLegame[j].getLegameElementoAut().getIdArrivo();
	        	Tb_descrittore tb_descrittore = cercaDescrittorePerId(idArrivo);
				if (tb_descrittore!=null){
                    if (tipoOperazione.getType() == SbnTipoOperazione.CREA_TYPE){
                        for (int k=0;k<vettoreDiLegami.size();k++){
                            legame = vettoreDiLegami.get(k);
                            if((legame.getDID_COLL().equals(idArrivo)) && (!legame.getFL_CANC().equals("S")))
                                throw new EccezioneSbnDiagnostico(3030,"legame già esistente");
                        }
                        // TEST PER VERIFICA DELLE CONTROLLI PER L'INSERIMENTO
    					controllaTipoLegame(arrivoLegame[j].getLegameElementoAut().getTipoLegame());
    		        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("USE")){
    		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
    		        			(tipoForma.getType() == SbnFormaNome.R_TYPE))){
    		        			descrittoreDes = new DescrittoreDescrittore();
    		        			if (descrittoreDes.cercaDescrittoreDescrittore(idPartenza)){
    								validato = true;
    		        			}
    		        			if (descrittoreDes.countLegamiDescrittoreDescrittore(idPartenza, idArrivo) >=1 ){
    		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
    		        			}
    		        		}
    		        	}else if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("UF")){
    		        		if ((tb_descrittore.getTP_FORMA_DES().equals("R") &&
    		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
    		        			descrittoreDes = new DescrittoreDescrittore();
    		        			if (descrittoreDes.cercaDescrittoreDescrittore(idArrivo)){
    								validato = true;
    		        			}
    		        			descrittoreDes.getDID_BASE();
    		        			if (descrittoreDes.countLegamiDescrittoreDescrittore(idArrivo,idPartenza) >=1 ){
    		        				throw new EccezioneSbnDiagnostico(4545,"Una forma di Rinvio può essere legata ad una sola forma Accettata, ");
    		        			}
    		        		}
    		        	}else {
    		        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
    		        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
    		        			validato = true;
    		        		}else throw new EccezioneSbnDiagnostico(3033,"Legame non ammesso per la forma Accettata");
    		        	}
                        // END
                    }
					if (tipoOperazione.getType() == SbnTipoOperazione.MODIFICA_TYPE){
						controllaTipoLegame(arrivoLegame[j].getLegameElementoAut().getTipoLegame());
			        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("USE")){
			        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
			        			(tipoForma.getType() == SbnFormaNome.R_TYPE))){

			        			if (descrittoreDes.cercaDescrittoreDescrittore(idPartenza)){
									validato = true;
			        			}
			        		} else throw new EccezioneSbnDiagnostico(3033);
			        	}else if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("UF")){
			        		if ((tb_descrittore.getTP_FORMA_DES().equals("R") &&
			        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
			        			if (descrittoreDes.cercaDescrittoreDescrittore(idArrivo)){
									validato = true;
			        			}
			        		} else throw new EccezioneSbnDiagnostico(3033);
			        	}else {
			        		if ((tb_descrittore.getTP_FORMA_DES().equals("A") &&
			        			(tipoForma.getType() == SbnFormaNome.A_TYPE))){
			        			validato = true;
			        		} else throw new EccezioneSbnDiagnostico(3033);
			        	}
					}else if (tipoOperazione.getType() == SbnTipoOperazione.CANCELLA_TYPE){
						if (!descrittoreDes.cercaDescrittoreDescrittore(idPartenza,idArrivo))
							throw new EccezioneSbnDiagnostico(3029,"legame inesistente");
					}

				}else{
					validato=false;
				}
	        }
        }
    	return validato;
   }



	/**
	 * Method controllaTipoLegame.
	 * @param sbnLegameAut
	 */
	private void controllaTipoLegame(SbnLegameAut sbnLegameAut) throws EccezioneSbnDiagnostico {

		//almaviva5_20090220 lettura conf. legami da DB
		if (Decodificatore.getTb_codici("LEDD", sbnLegameAut.toString()) == null)
			throw new EccezioneSbnDiagnostico(3049,"dati incongruenti");
	}


	/**
	 * metodo utilizzato in fase di modifica:
	 * si controlla che il livello in input  sia minore di quello dell'oggetto nel db
	 */
	public boolean verificaLivello(
	Tb_descrittore tb_descrittore,
	String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico {
        if(_cattura)
            return true;
		if (Integer.parseInt(livelloAut)<Integer.parseInt(tb_descrittore.getCD_LIVELLO()))
			throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
		return true;
	}

	private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico{
      Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "DE");
      if(!_cattura){
          if (par == null)
             throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
          String livelloUtente = par.getCd_livello();
          if (par.getTp_abil_auth()!='S')
             throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
          if (Integer.parseInt(livelloAut) > Integer.parseInt(livelloUtente))
             throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
      }
	}


	public boolean controllaVettoreParametriSemantici(String c2_931, String codiceUtente) throws EccezioneSbnDiagnostico{

		ValidatorProfiler validator = ValidatorProfiler.getInstance();
        Tbf_biblioteca_in_polo biblio = validator.getBiblioteca(codiceUtente.substring(0, 3), codiceUtente.substring(3,6));

        Iterator iter = biblio.getTr_soggettari_biblioteche().iterator();

		boolean trovato = false;
		c2_931 = Decodificatore.getCd_tabella("Tb_descrittore", "cd_soggettario", c2_931.toUpperCase());
		if(c2_931 == null)
			throw new EccezioneSbnDiagnostico(3211,"soggettario non abilitato per la biblioteca");

		while(iter.hasNext())
		{
        	Tr_soggettari_biblioteche soggettari = (Tr_soggettari_biblioteche) iter.next();
        	if (c2_931.equals(soggettari.getCD_SOGG().trim()))
        	{
        		//almaviva5_20091030
        		trovato = soggettari.getFL_ATT().equals("1"); //solo se attivo alla gestione
    			break;
        	}
		}

		return trovato;
	}


    public boolean verificaVersioneDescrittore(Tb_descrittore tb_descrittore, String t005) throws EccezioneSbnDiagnostico {
        if (t005 != null){
            SbnDatavar datavar = new SbnDatavar(tb_descrittore.getTS_VAR());
            return datavar.getT005Date().equals(t005);
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }


	/**
	 * Method validaPerModifica.
	 * @param t001
	 * @param cd_livello
	 * @param t005
	 * @param codiceUtente
	 * @param t931
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_descrittore validaPerModifica(
		String 	idDescrittore,
		String 	cd_livello,
		String	t005,
		String 	codiceUtente,
		A931 	t931,
        boolean _cattura,
        SbnEdizioneSoggettario _edizione) throws IllegalArgumentException, InvocationTargetException, Exception {
			Tb_descrittore des = new Tb_descrittore();
			des = cercaDescrittorePerId(idDescrittore);
			if (des == null)
				throw new EccezioneSbnDiagnostico(3001,"elemento non trovato");
//			SoggettoDescrittore soggettoDes = new SoggettoDescrittore();
//			if (soggettoDes.cercaSoggettoPerDescrittore(idDescrittore))
//				throw new EccezioneSbnDiagnostico(3220,"descrittore legato ad un soggetto, non modificabile");
			if (!verificaLivello(des,cd_livello,_cattura))
				throw new EccezioneSbnDiagnostico(3007,"livello di authority troppo basso");
			controllaParametriUtente(codiceUtente,cd_livello,_cattura);
			verificaVersioneDescrittore(des,t005);

			//almaviva5_20120622 evolutive CFI
			if (_edizione != null && _edizione.getType() != SbnEdizioneSoggettario.E_TYPE) {
				Soggetto soggetto = new Soggetto();
				TableDao dao = soggetto.cercaSoggettoPerDescrittore(des.getDID(), null, null);
				List<Vl_soggetto_des> soggetti = dao.getElencoRisultati();
				for (Vl_soggetto_des sd : soggetti) {
					//se l'edizione del descrittore non è 'entrambe' va verificato che i soggetti
					//collegati abbiano edizione congruente
					SbnEdizioneSoggettario edSogg = SbnEdizioneSoggettario.valueOf(sd.getCD_EDIZIONE());
					if (!ValidazioneDati.in(edSogg, _edizione, SbnEdizioneSoggettario.E))
						throw new EccezioneSbnDiagnostico(3347, true, new String[] {sd.getCID()});	//edizione non congruente
				}
			}

			return des;
	}


}
