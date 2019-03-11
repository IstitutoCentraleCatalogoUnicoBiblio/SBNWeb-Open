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
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.oggetti.Link_multim;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Ts_link_multim;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;

/**
 * Classe TitoloCreaDocumento
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 27-mag-03
 */
public class TitoloCreaDocumento extends TitoloCrea {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3179404619616592569L;

	public TitoloCreaDocumento(CreaType crea) {
        super(crea);
    }

    public TitoloCreaDocumento(DocumentoType doc) {
        super(doc);
    }

    public TitoloCreaDocumento(ModificaType modifica) {
        super(modifica);
    }
    /**
     * Method creaDocumento.
     * Crea un oggetto di tipo Tb_documento, invocando i metodi specializzati su ciascun tipo di materiale
     * @param tb_titolo
     * @return Tb_titolo
     * @throws InfrastructureException
     */
    public Tb_titolo creaDocumento(Tb_titolo tb_titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

    	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
    	if (datiDoc.getTipoMateriale() == null
            || datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("M").getType()
            || datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf(" ").getType())
            tb_titolo = creaDocumentoModerno(tb_titolo);
        else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("E").getType()) {
            tb_titolo = creaDocumentoAntico(tb_titolo);
        } else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("U").getType()) {
            tb_titolo = creaDocumentoMusica(tb_titolo);
        } else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("G").getType()) {
            tb_titolo = creaDocumentoGrafica(tb_titolo);
        } else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("C").getType()) {
            tb_titolo = creaDocumentoCartografico(tb_titolo);
        } else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("H").getType()) {
            tb_titolo = creaDocumentoAudiovisivo(tb_titolo);
        } else if (datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf("L").getType()) {
            tb_titolo = creaDocumentoElettronico(tb_titolo);

        } else
            throw new EccezioneSbnDiagnostico(3066, "Tipo doc sconosciuto");
        return tb_titolo;
    }

    public Tb_titolo creaDocumentoModerno(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        String cd_natura = titolo.getCD_NATURA();
        CostruttoreIsbd isbd = new CostruttoreIsbd();
        isbd.definisciISBD(titolo, datiDoc);

        //Setto le chiavi del documento cles e clet
        //In modifica di un documento W non le devo settare. Creo una patch.
        if (!cd_natura.equals("W") || titolo.getKY_CLES1_T() == null)
            settaChiavi(titolo);

        // Inizio modifica almaviva2 15.07.2010 Inserita valorizzazione del campo titolo.getTP_RECORD_UNI secondo le stesse
        // regole applcate in indice per il successivo controllo/Confronto
		if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
			titolo.setTP_RECORD_UNI(Decodificatore.getCd_tabella("Tb_titolo", "tp_record_uni", datiDoc.getGuida().getTipoRecord().toString()));
		}
        // Fine modifica almaviva2 15.07.2010

        if (datiDoc.getTipoMateriale() == null
            || datiDoc.getTipoMateriale().getType() == SbnMateriale.valueOf(" ").getType())
            titolo.setTP_MATERIALE(" ");
        else
            titolo.setTP_MATERIALE(datiDoc.getTipoMateriale().toString());

        // Inizio modifica almaviva2 15.07.2010 Metodo modificato per adeguamenti a software di Indice identificata con commento "almaviva4 ../07/2010"
    	// e non solo quella

        //almaviva4 13/07/2010 controlli tipo record/tipo materiale
        if (!titolo.getTP_MATERIALE().equals(" ") && datiDoc.getGuida().getTipoRecord() != null) {
			if (  (titolo.getTP_MATERIALE().toString().equals("U") && (!titolo.getTP_RECORD_UNI().equals("a")
					  && !titolo.getTP_RECORD_UNI().equals("b") && !titolo.getTP_RECORD_UNI().equals("c")
//Inizio Modifica almaviva225.10.2001 ripresa da almaviva4 evolutiva 26/09/2011 aggiunto tipo record 'g' per tipo materiale 'U'
									  && !titolo.getTP_RECORD_UNI().equals("g")
//FIne Modifica almaviva225.10.2001 ripresa da almaviva4 evolutiva 26/09/2011 fine
					  && !titolo.getTP_RECORD_UNI().equals("d") && !titolo.getTP_RECORD_UNI().equals("j")) )
					||  (titolo.getTP_MATERIALE().toString().equals("C") && !titolo.getTP_RECORD_UNI().equals("e") && !titolo.getTP_RECORD_UNI().equals("f"))
					||  (titolo.getTP_MATERIALE().toString().equals("G") && !titolo.getTP_RECORD_UNI().equals("k")) )
					throw new EccezioneSbnDiagnostico(3271, "Tipo record non compatibile con tipo materiale");
			//18/03/2015 generi speciali eliminati Inizio Adeguamento anche di almaviva2
//			if  (titolo.getTP_MATERIALE().toString().equals("U") && titolo.getTP_RECORD_UNI().toString().equals("a")) {
//				  if (titolo.getCD_GENERE_1() == null || !(titolo.getCD_GENERE_1().charAt(0) == '2')) {
//					  // Inizio modifica BUG 3913 almaviva2 30.09.2010
//					  // in questo caso si forza il valore "2" e non si deve mandare il diagnostico
////					  throw new EccezioneSbnDiagnostico(3272, "Tipo record non compatibile con genere");
//					  titolo.setCD_GENERE_1("2");
//				  // Fine modifica BUG 3913 almaviva2 30.09.2010
//				  }
//			}
			//18/03/2015 generi speciali eliminati Fine Adeguamento anche di almaviva2
        }

        //almaviva4 13/07/2010 fine
        // Fine modifica almaviva2 15.07.2010

        // CONTROLLO DATE
        // Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
        if (cd_natura.equals("M")
            || cd_natura.equals("S")
            || cd_natura.equals("C")
            || cd_natura.equals("W")
            || cd_natura.equals("R")) {
            C100 t100 = datiDoc.getT100();
            String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
            String data = datiDoc.getT100().getA_100_9();
            titolo.setTP_AA_PUBB(tipo_data);
            titolo.setAA_PUBB_1(data);
            data = t100.getA_100_13();
            titolo.setAA_PUBB_2(data);
            if (datiDoc.getT210Count() > 0) {

            }
        }
        // Inizio Modifica almaviva2 Gennaio 2015: inserito controllo sulla natura N per modifica data ripresa da Indice
        if (cd_natura.equals("N")) {
        	if (datiDoc.getT100() == null) {
        		 titolo.setTP_AA_PUBB(null);
                 titolo.setAA_PUBB_1(null);
                 titolo.setAA_PUBB_2(null);
        	} else {
        		 C100 t100 = datiDoc.getT100();
                 String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
                 String data = datiDoc.getT100().getA_100_9();
                 titolo.setTP_AA_PUBB(tipo_data);
                 titolo.setAA_PUBB_1(data);
                 data = t100.getA_100_13();
                 titolo.setAA_PUBB_2(data);
        	}
        }
        // Inizio Modifica almaviva2 Gennaio 2015: inserito controllo sulla natura N per modifica data ripresa da Indice

        if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
            titolo.setTP_RECORD_UNI(
                Decodificatore.getCd_tabella(
                    "Tb_titolo",
                    "tp_record_uni",
                    datiDoc.getGuida().getTipoRecord().toString()));
        }

        //CONTROLLO LINGUA
        titolo.setCD_LINGUA_1(null);
        titolo.setCD_LINGUA_2(null);
        titolo.setCD_LINGUA_3(null);
        if (datiDoc.getT101() != null) {
            String[] lingue = datiDoc.getT101().getA_101();
            if (lingue.length >= 1)
                titolo.setCD_LINGUA_1(lingue[0].toUpperCase());
            if (lingue.length >= 2)
                titolo.setCD_LINGUA_2(lingue[1].toUpperCase());
            if (lingue.length >= 3)
                titolo.setCD_LINGUA_3(lingue[2].toUpperCase());
        }

        //CONTROLLO PAESE
        titolo.setCD_PAESE(null);
        if (datiDoc.getT102() != null && datiDoc.getT102().getA_102()!=null) {
            titolo.setCD_PAESE(Decodificatore.getCd_tabella(
            "Tb_titolo",
            "cd_paese",
            datiDoc.getT102().getA_102()));
        }

        //CONTROLLO Forma contenuto (solo per moderno)
        titolo.setCD_GENERE_1(null);
        titolo.setCD_GENERE_2(null);
        titolo.setCD_GENERE_3(null);
        titolo.setCD_GENERE_4(null);
        if (cd_natura.equals("M")
            || cd_natura.equals("S")
            || cd_natura.equals("C")
            || cd_natura.equals("N")
            || cd_natura.equals("T")
            || cd_natura.equals("W"))
            if (datiDoc instanceof ModernoType) {
                ModernoType moderno = (ModernoType) datiDoc;
                if (moderno.getT105() != null && moderno.getT105().getA_105_4Count() > 0) {
                    String[] generi = moderno.getT105().getA_105_4();
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
            }
        if (cd_natura.equals("C")) {
            //titolo.set ?? ( datiDoc.getT110());
        }
        //Fonte del record
        creaFonte(titolo, datiDoc.getT801());
        String tipoTesto = null;
        if (datiDoc instanceof MusicaType) {
            C125 c125 = ((MusicaType)datiDoc).getT125();
            if (c125 != null) {
                if (c125.getB_125() != null) {
                    tipoTesto = Decodificatore.getCd_tabella("Tb_musica", "tp_testo_letter", c125.getB_125());
                }
            }
        }
		//18/03/2015 generi speciali eliminati Inizio Adeguamento anche di almaviva2
//        if ("k".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("0");
//        } else if ("m".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("4");
//        } else if ("e".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("Y");
//        } else if ("f".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("T");
//        } else if ("g".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("7");
//        } else if ("c".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("9");
//        } else if ("r".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("5");
//        } else if ("j".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("6");
//        } else if ("i".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("1");
//        } else if ("l".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("X");
//        } else if ("d".equals(titolo.getTP_RECORD_UNI())) {
//            titolo.setCD_GENERE_1("8");
//        } else if ("a".equals(titolo.getTP_RECORD_UNI()) && "b".equals(tipoTesto)) {
//            titolo.setCD_GENERE_1("2");
//        } else if ("b".equals(titolo.getTP_RECORD_UNI()) && "b".equals(tipoTesto)) {
//            titolo.setCD_GENERE_1("3");
//        }
		//18/03/2015 generi speciali eliminati Fine Adeguamento anche di almaviva2
        return titolo;

    }


    public void crea856(Tb_titolo titolo) throws EccezioneDB, NumberFormatException, InfrastructureException {
        if (datiDoc.getT856Count()>0) {
            Link_multim lm = new Link_multim();
            Progressivi prog = new Progressivi();
            for (int i = 0; i < datiDoc.getT856Count(); i++) {
                C856 link = datiDoc.getT856(i);
                Ts_link_multim ts = new Ts_link_multim();
                ts.setFL_CANC(" ");
                ts.setID_LINK_MULTIM(Long.parseLong(prog.getNextIdLinkMultim()));
                ts.setKY_LINK_MULTIM(titolo.getBID());
                ts.setURI_MULTIM(link.getU_856());
                //Se sono in modifica uso comunque l'utente var.
                ts.setUTE_INS(titolo.getUTE_VAR());
                ts.setUTE_VAR(titolo.getUTE_VAR());
                lm.insert(ts, link.getC9_856_1());
                link.getU_856();
            }
        }
    }

    public Tb_titolo creaDocumentoGrafica(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        titolo = creaDocumentoModerno(titolo);

        return titolo;

    }

    public Tb_titolo creaDocumentoCartografico(Tb_titolo titolo)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        titolo = creaDocumentoModerno(titolo);

        return titolo;

    }

    public Tb_titolo creaDocumentoElettronico(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        titolo = creaDocumentoModerno(titolo);

        return titolo;

    }

    public Tb_titolo creaDocumentoAudiovisivo(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        titolo = creaDocumentoModerno(titolo);

        return titolo;

    }

    public Tb_titolo creaDocumentoAntico(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        String cd_natura = titolo.getCD_NATURA();

        CostruttoreIsbd isbd = new CostruttoreIsbd();
        isbd.definisciISBD(titolo, datiDoc);

        //Setto le chiavi del documento cles e clet
        ChiaviTitolo chiavi = new ChiaviTitolo();
        settaChiavi(titolo);

        titolo.setTP_MATERIALE(datiDoc.getTipoMateriale().toString());
        // Inizio Modifica almaviva2 15.07.2010 Modificato if su tipo record adeguamenti a software di Indice identificata con commento "almaviva4 ../07/2010"
    	// e aggiunto else con diagnostico
        // almaviva4 12/07/2010 controllo tipo record


        // 18.03.2015 almaviva2 Bug esercizio 5800
        // inseriti i nuovi controlli per inserire tutti i tipi record con Documenti antichi (b,c,d,e,f,k)
        // if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
        if ( (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null)
        		&& datiDoc.getGuida().getTipoRecord().toString().equals("a")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("b")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("c")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("d")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("e")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("f")
        		|| datiDoc.getGuida().getTipoRecord().toString().equals("k")) {

            titolo.setTP_RECORD_UNI(Decodificatore.getCd_tabella("Tb_titolo", "tp_record_uni", datiDoc.getGuida().getTipoRecord().toString()));
        }
		else {
			throw new EccezioneSbnDiagnostico(3271, "Tipo record non compatibile con tipo materiale");
		}
        // Fine Modifica almaviva2 15.07.2010

        //CONTROLLO DATE
        if (cd_natura.equals("M") || cd_natura.equals("C") || cd_natura.equals("W")) {
            C100 t100 = datiDoc.getT100();
            String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
            String data = datiDoc.getT100().getA_100_9();
            titolo.setTP_AA_PUBB(tipo_data);
            titolo.setAA_PUBB_1(data);
            data = t100.getA_100_13();
            titolo.setAA_PUBB_2(data);
        }

        //CONTROLLO LINGUA
        titolo.setCD_LINGUA_1(null);
        titolo.setCD_LINGUA_2(null);
        titolo.setCD_LINGUA_3(null);
        if (cd_natura.equals("M")
            || cd_natura.equals("W")
            || cd_natura.equals("T")
            || cd_natura.equals("N")) {
            String[] lingue = datiDoc.getT101().getA_101();
            if (lingue.length >= 1)
                titolo.setCD_LINGUA_1(lingue[0].toUpperCase());
            if (lingue.length >= 2)
                titolo.setCD_LINGUA_2(lingue[1].toUpperCase());
            if (lingue.length >= 3)
                titolo.setCD_LINGUA_3(lingue[2].toUpperCase());
        }

        //CONTROLLO PAESE
        titolo.setCD_PAESE(null);
        if (cd_natura.equals("M") || cd_natura.equals("C") || cd_natura.equals("W")) {
            if (datiDoc.getT102() != null && datiDoc.getT102().getA_102()!=null) {
                titolo.setCD_PAESE(Decodificatore.getCd_tabella(
                "Tb_titolo",
                "cd_paese",
                datiDoc.getT102().getA_102()));
            }
        }

        titolo.setCD_GENERE_1(null);
        titolo.setCD_GENERE_2(null);
        titolo.setCD_GENERE_3(null);
        titolo.setCD_GENERE_4(null);
        if (datiDoc instanceof AnticoType) {
            AnticoType antico = (AnticoType) datiDoc;
            //CONTROLLO Forma contenuto
            if (cd_natura.equals("M")
                || cd_natura.equals("C")
                || cd_natura.equals("T")
                || cd_natura.equals("W")) {
                if (antico.getT140() != null && antico.getT140().getA_140_9Count() > 0) {
                    String[] generi = antico.getT140().getA_140_9();
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
            }
        }

        //Fonte del record
        creaFonte(titolo, datiDoc.getT801());

        return titolo;

    }

    public Tb_titolo creaDocumentoMusica(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        /*
         *
         String cd_natura = titolo.getCD_NATURA();
        Isbd isbd = new Isbd();
        isbd.definisciISBD(titolo, datiDoc);

        //Setto le chiavi del documento cles e clet
        ChiaviTitolo chiavi = new ChiaviTitolo();
        settaChiavi(titolo);

        titolo.setTP_MATERIALE(datiDoc.getTipoMateriale().toString());

        //DATE
        if (cd_natura.equals("M")
            || cd_natura.equals("S")
            || cd_natura.equals("C")
            || cd_natura.equals("W")) {
            C100 t100 = datiDoc.getT100();
            String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
            String data = datiDoc.getT100().getA_100_9();
            titolo.setTP_AA_PUBB(tipo_data);
            if (data != null)
                titolo.setAA_PUBB_1(data);
            data = t100.getA_100_13();
            if (data != null)
                titolo.setAA_PUBB_2(data);
        }

        //LINGUA
        if (cd_natura.equals("M")
            || cd_natura.equals("S")
            || cd_natura.equals("W")
            || cd_natura.equals("T")
            || cd_natura.equals("N")) {
            String[] lingue = datiDoc.getT101().getA_101();
            if (lingue.length >= 1)
                titolo.setCD_LINGUA_1(lingue[0]);
            if (lingue.length >= 2)
                titolo.setCD_LINGUA_2(lingue[1]);
            if (lingue.length >= 3)
                titolo.setCD_LINGUA_3(lingue[2]);
        }

        //CONTROLLO PAESE
        if (cd_natura.equals("M")
            || cd_natura.equals("S")
            || cd_natura.equals("C")
            || cd_natura.equals("W")) {
            titolo.getCD_PAESE(datiDoc.getT102().getA_102());
        }

        //Fonte del record
        creaFonte(titolo,datiDoc.getT801());
        return titolo;
        */
        return creaDocumentoModerno(titolo);

    }

}
