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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;

/**
 * Classe TitoloAccessoCrea
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 28-mag-03
 */
public class TitoloAccessoCrea extends TitoloCrea {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5447028753933576348L;
	TitAccessoType titAcc = null;
    C101 c101 = null;
    C105 c105 = null;
    C200 c200 = null;
    C801 c801 = null;
    SbnMateriale materiale = null;

    public TitoloAccessoCrea(CreaType crea) {
        super(crea);
        titAcc = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
        c801 = titAcc.getT801();
        TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
        if (choice.getT423() != null) {
            c200 = choice.getT423().getT200();
            c101 = choice.getT423().getT101();
            c105 = choice.getT423().getT105();
            materiale = choice.getT423().getTipoMateriale();
        }

     // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
        if (choice.getT454A() != null) {
            c200 = choice.getT454A().getT200();
            c101 = choice.getT454A().getT101();
        }
//        if (choice.getT454() != null)
//            c200 = choice.getT454();
        if (choice.getT510() != null)
            c200 = choice.getT510();
        if (choice.getT517() != null)
            c200 = choice.getT517();
    }

    public TitoloAccessoCrea(ModificaType crea) {
        super(crea);
        titAcc = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
        c801 = titAcc.getT801();
        TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
        if (choice.getT423() != null) {
            c200 = choice.getT423().getT200();
            c101 = choice.getT423().getT101();
            c105 = choice.getT423().getT105();
            materiale = choice.getT423().getTipoMateriale();
        }

        // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
        if (choice.getT454A() != null) {
            c200 = choice.getT454A().getT200();
            c101 = choice.getT454A().getT101();
        }
//        if (choice.getT454() != null)
//            c200 = choice.getT454();
        if (choice.getT510() != null)
            c200 = choice.getT510();
        if (choice.getT517() != null)
            c200 = choice.getT517();
    }

    public Tb_titolo creaTitoloAccessso(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico {
        String cd_natura = titolo.getCD_NATURA();
        CostruttoreIsbd isbd = new CostruttoreIsbd();
        isbd.definisciISBD(titolo, c200, null,null,null,null,null,null,null,null,false,null);

        //Setto le chiavi del documento cles e clet
        settaChiavi(titolo,titolo.getISBD());

        if (materiale != null)
            titolo.setTP_MATERIALE(materiale.toString());
        else
            titolo.setTP_MATERIALE(" ");

        //CONTROLLO LINGUA
        // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
        if (cd_natura.equals("B")) {
        	 titolo.setCD_LINGUA_1(null);
             titolo.setCD_LINGUA_2(null);
             titolo.setCD_LINGUA_3(null);
     	     if (c101 != null) {
 	            String[] lingue = c101.getA_101();
 	            if (lingue.length >= 1)
 	                titolo.setCD_LINGUA_1(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[0].toUpperCase()));
 	            if (lingue.length >= 2)
 	            	titolo.setCD_LINGUA_2(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_2", lingue[1].toUpperCase()));
 	            if (lingue.length >= 3)
 	            	titolo.setCD_LINGUA_3(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_3", lingue[2].toUpperCase()));
 	        }
        }

        // BUG Segnalato da Rossana - luglio 2017 - Nature T: la lingua non viene aggiornata ne in variazione ne in allineamento
        if (cd_natura.equals("T")) {
       	 titolo.setCD_LINGUA_1(null);
            titolo.setCD_LINGUA_2(null);
            titolo.setCD_LINGUA_3(null);
    	     if (c101 != null) {
	            String[] lingue = c101.getA_101();
	            if (lingue.length >= 1)
	                titolo.setCD_LINGUA_1(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", lingue[0].toUpperCase()));
	            if (lingue.length >= 2)
	            	titolo.setCD_LINGUA_2(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_2", lingue[1].toUpperCase()));
	            if (lingue.length >= 3)
	            	titolo.setCD_LINGUA_3(Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_3", lingue[2].toUpperCase()));
	        }
       }

        //CONTROLLO Forma contenuto
        if (c105 != null && c105.getA_105_4Count() > 0) {
            String[] generi = c105.getA_105_4();
            if (generi.length >= 1)
                titolo.setCD_GENERE_1(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_genere_1",
                        generi[0],
                        titolo.getTP_MATERIALE()));
            if (generi.length >= 2)
                titolo.setCD_GENERE_2(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_genere_2",
                        generi[1],
                        titolo.getTP_MATERIALE()));
            if (generi.length >= 3)
                titolo.setCD_GENERE_3(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_genere_3",
                        generi[2],
                        titolo.getTP_MATERIALE()));
            if (generi.length >= 4)
                titolo.setCD_GENERE_4(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_genere_4",
                        generi[3],
                        titolo.getTP_MATERIALE()));
        }
        //Fonte del record
        creaFonte(titolo,c801);
        return titolo;

    }

}
