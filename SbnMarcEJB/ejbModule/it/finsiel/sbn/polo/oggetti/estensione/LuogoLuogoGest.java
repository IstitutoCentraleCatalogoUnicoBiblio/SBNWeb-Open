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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_luo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_luogo_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */

/**
 * Classe che contiene i metodi per la gestione dei rinvii tra luoghi: Tr_luo_luo
 * Corrisponde a ArrivoLegame.LegameElementoAut con tipoAuthority = 'Luogo' ,
 * tipoLegame = '4XX' corrisponde a tp_legame = 8 (relazione 'vedi')
 * tipoLegame = '5XX' corrisponde a tp_legame = 4 (relazione 'vedi anche')
 */
public class LuogoLuogoGest extends LuogoLuogo{



	private static final long serialVersionUID = -2389440708714740859L;


	public LuogoLuogoGest()  {
		super();
	}



   /**
    * per ogni struttura ArrivoLegame corrispondente al luogo in creazione verifica
    * la validità del legame:
    * . il luogo di arrivo deve esistere: cercaLuogoPerID con idArrivo
    * . se tipoLegame = 5XX tp_forma del luogo letto deve essere 'A' (accettata) e
    * tipoForma del luogo in creazione deve essere 'A', altrimenti segnala
    * diagnostico: 'Forma dei nomi e legame incompatibili'
    * . se tipoLegame = 4XX:
    *   .. se tp_forma del luogo letto è 'R' e tipoForma del luogo in creazione è
    * 'A', il luogo letto non deve essere legato ad altri luoghi (cioè non deve
    * essere presente in nessun record di Tr_luo_luo )
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public boolean validaPerCreaLegame(ElementAutType elementAutType) throws IllegalArgumentException, InvocationTargetException, Exception{
		SbnFormaNome 					tipoForma = null;
		boolean 						validato = false;
		LegamiType[]					legamiType;
		String 							tp_forma;
		ArrivoLegame[] 					arrivoLegame;
		int 							sizeArrivoLegame;

		legamiType 	= elementAutType.getLegamiElementoAut();
		int size 	= elementAutType.getLegamiElementoAutCount();
		tipoForma	= elementAutType.getDatiElementoAut().getFormaNome();
		if (tipoForma == null) tipoForma = SbnFormaNome.A;
		boolean esito;
		esito = controllaConsistenzaLegami(size,legamiType);
		//se i luoghi non sono stati validati non ha senso proseguire
		//vado avanti solo se TUTTI i legami sono risultati validi
		Tb_luogo tb_luogo = null;
		if (esito){
	        for (int i=0;i<size; i++) {
				arrivoLegame=legamiType[i].getArrivoLegame();
				sizeArrivoLegame=legamiType[i].getArrivoLegameCount();
		        for (int j=0;j<sizeArrivoLegame; j++) {
		        	Luogo cercaluogo = new Luogo();
		        	tb_luogo = cercaluogo.cercaLuogoPerID(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
					if (tb_luogo!=null){
			        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("5XX")){
			        		if ((tb_luogo.getTP_FORMA().equals("A")&(tipoForma.getType() == SbnFormaNome.A_TYPE))){
			        			validato = true;
			        		}else throw new EccezioneSbnDiagnostico(3033);
			        	}
			        	if (arrivoLegame[j].getLegameElementoAut().getTipoLegame().toString().equals("4XX")){
			        		if ((tb_luogo.getTP_FORMA().equals("R")&(tipoForma.getType() == SbnFormaNome.A_TYPE))){
			        			if (cercaLuogoLuogo(arrivoLegame[j].getLegameElementoAut().getIdArrivo()) == false){
									validato = true;
			        			}
			        		} else {
			        		    validato = true;
                            }
			        	}
					}
					else{
						validato=false;
					}
		        }
	        }
		}else
			validato=false;
    	return validato;
   }

	//questo metodo serve per validare i luoghi in arrivo
	private boolean controllaConsistenzaLegami(int size, LegamiType[] legamiType) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException{
		Tb_luogo 		tb_luogo;
		ArrivoLegame[] 	arrivoLegame;
		int i = 0;
		int j;
		boolean esito = true;
		while ((i < size)&(esito == true)){
			arrivoLegame = legamiType[i].getArrivoLegame();
			j=0;
			while ((j < legamiType[i].getArrivoLegameCount())&(esito==true)){
	        	Luogo cercaluogo = new Luogo();

	        	tb_luogo = cercaluogo.cercaLuogoPerID(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
				if (tb_luogo == null)
					esito = false;
				j++;
			}
			i++;
		}
		return esito;
	}

	/**
    * per ogni struttura ArrivoLegame corrispondente al luogo in creazione verifica
    * la validità del legame:
    * . il luogo di arrivo deve esistere: cercaLuogoPerID con idArrivo
    * . se tipoLegame = 5XX tp_forma del luogo letto deve essere 'A' (accettata) e
    * tipoForma del luogo in creazione deve essere 'A', altrimenti segnala
    * diagnostico: 'Forma dei nomi e legame incompatibili'
    * . se tipoLegame = 4XX:
    *   .. se tp_forma del luogo letto è 'R' e tipoForma del luogo in creazione è
    * 'A', il luogo letto non deve essere legato ad altri luoghi (cioè non deve
    * essere presente in nessun record di Tr_luo_luo )
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
    public boolean validaPerModificaLegame(
        String lid1,
        String utente,
        ArrivoLegame legame,
        SbnTipoOperazione operazione,
        TimestampHash timeHash,
        boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogo luo1;
        Tb_luogo luo2;
        //prima di tutto valido tutti gli autori di arrivo
        int sizeArrivoLegame;
        LegameElementoAutType elementoLegato = legame.getLegameElementoAut();
        LuogoValida luogo = new LuogoValida();
        luo1 = luogo.cercaLuogoPerID(lid1);

        //Verifico la correttezza del tipo di authority
        if ((elementoLegato == null)
            || (elementoLegato.getTipoAuthority() != null
                && elementoLegato.getTipoAuthority().getType() != SbnAuthority.LU_TYPE)) {
            throw new EccezioneSbnDiagnostico(3083, "Tipo di autority sbagliato");
        }
        //Verifico esistenza luogo
        luo2 = luogo.cercaLuogoPerID(elementoLegato.getIdArrivo());
        //luo2 = luogo.cercaPerID();
        if (luo2 == null) {
            throw new EccezioneSbnDiagnostico(3013, "Luogo non esistente nel DB");
        }
        //memorizzo il timestamp
        timeHash.putTimestamp("tb_luogo", luo2.getLID(), new SbnDatavar( luo2.getTS_VAR()));

        //Controlli specifici a seconda del tipo di operazione
        if (operazione.getType() == SbnTipoOperazione.CREA_TYPE) {
            //CONTROLLI DI CREAZIONE

            //Verifico correttezza dei legami (da controllare)
            if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                if (!(luo2.getTP_FORMA().equals("A") && (luo1.getTP_FORMA().equals("A"))))
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                //Verifico i livelli di entrambi
                if (Integer.parseInt(luo1.getCD_LIVELLO()) < Integer.parseInt(luo2.getCD_LIVELLO()))
                    luogo.controllaParametriUtente(utente, luo2.getCD_LIVELLO(),_cattura);
                if (cercaLuogoLuogo(luo1.getLID(), luo2.getLID()))
                    throw new EccezioneSbnDiagnostico(3030, "Legame tra luoghi già esistente");
            } else if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                if ((luo2.getTP_FORMA().equals("A") && (luo1.getTP_FORMA().equals("A")))) {
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                } else if ((luo2.getTP_FORMA().equals("A") && (luo1.getTP_FORMA().equals("R")))) {
                    List elenco = luogo.cercaRinviiLuogo(luo1.getLID(), null);

                    if (elenco.size() > 0) {
                        // Vedi commenti del metodo
                        throw new EccezioneSbnDiagnostico(3029, "Legame dell'luogo R già esistente");
                    }
                } else { //luo1 tipoForma è A
                    if (cercaLuogoLuogo(luo1.getLID(), legame.getLegameElementoAut().getIdArrivo())) {
                        // Vedi commenti del metodo
                        throw new EccezioneSbnDiagnostico(3030, "Legame tra luoghi già esistente");
                    }
                }
            } else //nè 4XX nè 5XX
                throw new EccezioneSbnDiagnostico(3031, "Tipo di legame non valido");

        } else if (operazione.getType() == SbnTipoOperazione.CANCELLA_TYPE) {
            //CONTROLLI DI CANCELLAZIONE
            if (!cercaLuogoLuogo(elementoLegato.getIdArrivo(), luo1.getLID())) {
                throw new EccezioneSbnDiagnostico(3029, "Legame tra luoghi non esistente");
            }
            settaTimestampPerTR(luo1.getLID(),elementoLegato.getIdArrivo(),timeHash);


            //Verifico correttezza dei legami (da controllare)
            if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                if (!(luo2.getTP_FORMA().equals("A") && (luo1.getTP_FORMA().equals("A"))))
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                //Verifico i livelli di entrambi
                if (Integer.parseInt(luo1.getCD_LIVELLO()) < Integer.parseInt(luo2.getCD_LIVELLO()))
                    luogo.controllaParametriUtente(utente, luo2.getCD_LIVELLO(),_cattura);
            } else if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                if ((luo2.getTP_FORMA().equals("R") && (luo1.getTP_FORMA().equals("A")))
                    || (luo2.getTP_FORMA().equals("A") && (luo1.getTP_FORMA().equals("R")))) {
                } else {
                    throw new EccezioneSbnDiagnostico(3033, "Tipo forma e legami incompatibili");
                }
            } else //nè 4XX nè 5XX
                throw new EccezioneSbnDiagnostico(3031, "Tipo di legame non valido");

        } else if (operazione.getType() == SbnTipoOperazione.SCAMBIOFORMA_TYPE) {
            //CONTROLLI DI SCAMBIOFORMA
            if (legame.getLegameElementoAut().getTipoLegame().getType() != SbnLegameAut.valueOf("4XX").getType()) {
                throw new EccezioneSbnDiagnostico(3036, "Legame di tipo sbagliato");
            }
            validaPerScambioForma(luo1, legame.getLegameElementoAut().getIdArrivo(), timeHash);

        } else if (operazione.getType() == SbnTipoOperazione.MODIFICA_TYPE) {
            //posso modificare solo la nota di un legame.
            String nota = elementoLegato.getNoteLegame();
            if (nota != null) {
                if (cercaLuogoLuogo(legame.getLegameElementoAut().getIdArrivo(), luo1.getLID()) == false) {
                    throw new EccezioneSbnDiagnostico(3029, "Legame tra luoghi non esistente");
                }
                settaTimestampPerTR(luo1.getLID(),elementoLegato.getIdArrivo(),timeHash);
                if (elementoLegato.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                    //Verifico i livelli di entrambi
                    if (Integer.parseInt(luo1.getCD_LIVELLO()) < Integer.parseInt(luo2.getCD_LIVELLO()))
                        luogo.controllaParametriUtente(utente, luo2.getCD_LIVELLO(),_cattura);
                }
            }
        }
        return true;
   }
    protected void settaTimestampPerTR(String base, String coll, TimestampHash timeHash)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    	Tr_luo_luo tr_luo_luo = new Tr_luo_luo();
    	tr_luo_luo.setLID_BASE(base);
    	tr_luo_luo.setLID_COLL(coll);
    	Tr_luo_luoResult tavola = new Tr_luo_luoResult(tr_luo_luo);
    	tavola.executeCustom("selectPerKey");
        List vec = tavola.getElencoRisultati();

    	for (int i=0;i<vec.size();i++){
    		timeHash.putTimestamp(
    				"Tr_luo_luo",
    				base + coll, new SbnDatavar( ((Tr_luo_luo) vec.get(i)).getTS_VAR()));
    	}

    }
   /**
    * metodo per cercare nella vista se esiste almeno un legame con il luogo di arrivo
    * viene restituito tru in caso di esistenza
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
	public boolean cercaLuogoLuogo(String luogoArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean trovato = true;
		Vl_luogo_luo vl_luogo_luo = new  Vl_luogo_luo();
		vl_luogo_luo.setLID_1(luogoArrivo);
		Vl_luogo_luoResult	vl_luogo_luoResult = new Vl_luogo_luoResult(vl_luogo_luo);


		vl_luogo_luoResult.executeCustom("selectLuogoPerRinvii");
        List risultato = new ArrayList();
		risultato = vl_luogo_luoResult.getElencoRisultati();

		if (risultato.size()==0)
			trovato=false;
    	return trovato;
   }



   /**
    * metodo per cercare nella vista se esiste il legame con il luogo di arrivo = luogoArrivo
    * e luogo di partenza = luogoPartenza
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    *
    */
	public boolean cercaLuogoLuogo(String luogoPartenza,String luogoArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
		boolean trovato = true;
		Vl_luogo_luo vl_luogo_luo = new  Vl_luogo_luo();
		vl_luogo_luo.setLID_1(luogoArrivo);
		vl_luogo_luo.setLID(luogoPartenza);
		Vl_luogo_luoResult	vl_luogo_luoResult = new Vl_luogo_luoResult(vl_luogo_luo);


		vl_luogo_luoResult.executeCustom("selectLuogoPerRinvii");
        List risultato = new ArrayList();
		risultato = vl_luogo_luoResult.getElencoRisultati();

		if (risultato.size()==0)
			trovato=false;
    	return trovato;
   }


	   /**
	    * metodo per cercare nella vista se esiste almeno un legame con il luogo di arrivo
	    * viene restituito tru in caso di esistenza
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	    */
//	public TableDao cercaLuogoLuogoInTr(String luogoPartenza,String luogoArrivo)
//			throws IllegalArgumentException, InvocationTargetException, Exception {
//			Tr_luo_luo tr_luo_luo = new  Tr_luo_luo();
//			tr_luo_luo.setLID_BASE(luogoPartenza);
//			tr_luo_luo.setLID_COLL(luogoArrivo);
//			Tr_luo_luoResult	tr_luo_luoResult = new Tr_luo_luoResult(tr_luo_luo);
//			TableDao risultato = new TableDao();
//			tr_luo_luoResult.executeCustom("selectLuogoPerRinvii");
//
//			risultato = tr_luo_luoResult.getElencoRisultati();
//	    	return risultato;
//	   }


   /**
    * il metodo cerca il luogo nella tabella "tr_luo_luo" e lo cancella (setta il flag a "S")
    *
    */
/*	public void cancellaLegame(String user,String Lid1,String Lid2) throws EccezioneDB{
		//cerco nella tabella tr_luo_luo, non nella vista
		LuogoLuogo luogoLuogo = new LuogoLuogo();
		luogoLuogo.cancellaLegame(user,Lid1,Lid2);
	}
*/
   /**
    * input: 	Lid1= id del luogo di cui scambiare la forma da A a R
    * 			Lid2= id del luogo legato
    * si controlla l'effettiva esistenza del legame nella tabella
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
    */
/*	public void scambioForma(String Lid1, String Lid2) throws EccezioneDB, EccezioneSbnDiagnostico{
	//controllo che il legame che voglio modificare sia presente nel db!
		Tr_luo_luo tr_luo_luo = new Tr_luo_luo();
		TableDao resultTableDao;
		tr_luo_luo.setLid_coll(Lid1);
		tr_luo_luo.setLid_base(Lid2);
		Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(tr_luo_luo);


		tr_luo_luoResult.selectPerKey();
		resultTableDao = tr_luo_luoResult.getElencoRisultati();

		if (resultTableDao.size()==0){
			tr_luo_luo = new Tr_luo_luo();
			tr_luo_luo.setLid_coll(Lid2);
			tr_luo_luo.setLid_base(Lid1);
			tr_luo_luoResult = new Tr_luo_luoResult(tr_luo_luo);


			tr_luo_luoResult.selectPerKey();
			resultTableDao = tr_luo_luoResult.getElencoRisultati();

		}
		if (resultTableDao.size() == 0)
			throw new EccezioneSbnDiagnostico(3034);
		Tr_luo_luo tr_luo_luo1 = (Tr_luo_luo)resultTableDao.get(0);
		//cambia il tipo di legame
		tr_luo_luo1.setTp_legame("4");
		tr_luo_luoResult = new Tr_luo_luoResult(tr_luo_luo1);


		tr_luo_luoResult.update();

		Luogo luogo = new Luogo();
		Tb_luogo tb_luogo = new Tb_luogo();
		tb_luogo = luogo.cercaLuogoPerID(Lid1);
		tb_luogo.setTp_forma("A");
		Tb_luogoResult tb_luogoResult = new Tb_luogoResult(tb_luogo);


		tb_luogoResult.update();


	}
*/


    protected boolean validaPerScambioForma(Tb_luogo aut1, String vid2, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //controllo che il legame che voglio modificare sia presente nel db!
        Tr_luo_luo tr_aut_aut = new Tr_luo_luo();
        List resultTableDao;
        tr_aut_aut.settaParametro(TableDao.XXXlid_1, aut1.getLID());
        tr_aut_aut.settaParametro(TableDao.XXXlid_2, vid2);
        tr_aut_aut.setTP_LEGAME("8");
        Tr_luo_luoResult tr_aut_autResult = new Tr_luo_luoResult(tr_aut_aut);


        tr_aut_autResult.executeCustom("selectPerKeys2");
        resultTableDao = tr_aut_autResult.getElencoRisultati();

        if (resultTableDao.size() != 1)
            throw new EccezioneSbnDiagnostico(3034);

        tr_aut_aut = (Tr_luo_luo) resultTableDao.get(0);
        if (tr_aut_aut.getTP_LEGAME().equals("4"))
            throw new EccezioneSbnDiagnostico(3034, "Legame di tipo sbagliato");

        timeHash.putTimestamp("Tr_luo_luo", aut1.getLID() + vid2, new SbnDatavar( tr_aut_aut.getTS_VAR()));
        //Leggo da DB i due autori coinvolti
        LuogoValida autore = new LuogoValida();
        //resultTableDao = autore.cercaDueAutoriPerID(vid1, vid2);
        Tb_luogo luo2 = autore.cercaLuogoPerID(vid2);
        if (luo2 == null)
            throw new EccezioneSbnDiagnostico(3013, "Luogo non presente");
        //Il legame deve essere di tipo giusto
        if (!aut1.getTP_FORMA().equals("A"))
            throw new EccezioneSbnDiagnostico(3094, "Partire dalla forma accettata");
        if (!(luo2.getTP_FORMA().equals("R"))) {
            throw new EccezioneSbnDiagnostico(3034, "Legame di tipo sbagliato");
        }
        timeHash.putTimestamp("Tr_luo_luo", aut1.getLID(), new SbnDatavar( aut1.getTS_VAR()));
        timeHash.putTimestamp("Tr_luo_luo", luo2.getLID(), new SbnDatavar( luo2.getTS_VAR()));
        return true;
    }


}
