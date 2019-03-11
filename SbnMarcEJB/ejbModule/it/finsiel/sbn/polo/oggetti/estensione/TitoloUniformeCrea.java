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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_2;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

/**
 * Classe TitoloUniformeCrea.java
 * <p>
 * Oggetto utile per la creazione dei titoli uniformi.
 * </p>
 *
 * @author
 * @author
 *
 * @version 3-mar-2003
 */
public class TitoloUniformeCrea extends TitoloCrea {

	private static final long serialVersionUID = -3719290302745991862L;

	private TitoloUniformeType titUni = null;

    /** Costruttore */
    public TitoloUniformeCrea(CreaType crea) {
        super(crea);
        DatiElementoType datiEl = crea.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
        if (datiEl instanceof TitoloUniformeType) {
            //Non dovrebbe mai essere null
            titUni = (TitoloUniformeType) datiEl;
        }
    }

    /** Costruttore */
    public TitoloUniformeCrea(ModificaType crea) {
        super(crea);
        DatiElementoType datiEl = crea.getElementoAut().getDatiElementoAut();
        if (datiEl instanceof TitoloUniformeType) {
            //Non dovrebbe mai essere null
            titUni = (TitoloUniformeType) datiEl;
        }
    }

    public Tb_titolo creaDocumento(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        titolo.setCD_NATURA("A");
        titolo.setTP_MATERIALE(" ");

        if (titUni.getT300() != null)
            titolo.setNOTA_INF_TIT(titUni.getT300().getA_300());
        CostruttoreIsbd isbd = new CostruttoreIsbd();
        isbd.definisciISBDtitUni(titolo, titUni);
        if (titUni.getT015() != null && !titUni.getT015().getA_015().equals(""))
            titolo.setISADN(titUni.getT015().getA_015());
        else if (titUni.getLivelloAut().getType() == SbnLivello.valueOf("97").getType()) {
            Progressivi progr = new Progressivi();
            titolo.setISADN(progr.getNextIsadnTitolo());
        }

        //Setto le chiavi del documento cles e clet
        settaChiavi(titolo, titolo.getISBD());
        if (titUni.getT101() != null) {
            String[] lingue = titUni.getT101().getA_101();
            if (lingue.length >= 1)
                titolo.setCD_LINGUA_1(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[0].toUpperCase()));
            if (lingue.length >= 2)
                titolo.setCD_LINGUA_2(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[1].toUpperCase()));
            if (lingue.length >= 3)
                titolo.setCD_LINGUA_3(
                    Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[2].toUpperCase()));
        }


        //14/07/2017 completamento gestione 231 almaviva2 27.07.2017 adeguamento a Indice
        	titolo.setCD_PAESE(null);
	        if (titUni.getT102() != null && titUni.getT102().getA_102()!=null) {
	            titolo.setCD_PAESE(Decodificatore.getCd_tabella(
	            "Tb_titolo",
	            "cd_paese",
	            titUni.getT102().getA_102().toUpperCase()));
	        }



        //MANTIS 0002493
        if (titUni.getT101() == null) {
        	titolo.setCD_LINGUA_1("");
        	titolo.setCD_LINGUA_2("");
        	titolo.setCD_LINGUA_3("");
        }
        //END MANTIS 0002493

        // Bug MANTIS esercizio 5566: La funzione 'Allinea titolo uniforme' non porta le informazioni presenti in indice.
        // Probabilmente in Indice sono state fatte modifiche per cui, mentre prima il campo B_801 conteneva sia IT che ICCU
        // ora vengono spacchettati quindi nel caso in cui la A sia mancante si effettua l'inserimento
        if (titUni.getT801() != null && titUni.getT801().getB_801() != null) {
        	if (titUni.getT801().getA_801() != null) {
        		titolo.setCD_AGENZIA(titUni.getT801().getA_801() + titUni.getT801().getB_801());
        	} else {
        		titolo.setCD_AGENZIA("IT" + titUni.getT801().getB_801());
        	}
        } else {
        	titolo.setCD_AGENZIA("ITICCU");
        }





        if (titUni.getT152() != null)
            titolo.setCD_NORME_CAT(titUni.getT152().getA_152());
        else
            titolo.setCD_NORME_CAT("RICA");
        A830 a830 = titUni.getT830();
        if (a830 != null) {
            titolo.setNOTA_CAT_TIT(a830.getA_830());
        }

        return titolo;
    }

    // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
    // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
    public Tb_titset_2 creaDatiTitset2(Tb_titset_2 titset2) throws EccezioneDB, EccezioneSbnDiagnostico {

        if (titUni.getT231() != null) {
        	//14/07/2017 completamento gestione 231 almaviva2 27.07.2017 adeguamento a Indice
        	if (titUni.getT231().getC_231() != null) {
				String forma = Decodificatore.getCd_tabella("Tb_titset_2", "s231_forma_opera", titUni.getT231().getC_231());
				if (forma == null)
					throw new EccezioneSbnDiagnostico(2900, "Codice forma dell'opera errato");
				titset2.setS231_FORMA_OPERA(forma);
			}
			if (titUni.getT231().getD_231() != null)
				titset2.setS231_DATA_OPERA(titUni.getT231().getD_231());
			if (titUni.getT231().getK_231() != null)
				titset2.setS231_ALTRE_CARATTERISTICHE(titUni.getT231().getK_231());
        }
        return titset2;
    }


}
