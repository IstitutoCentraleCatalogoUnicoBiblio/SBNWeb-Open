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
import it.finsiel.sbn.polo.factoring.base.AnnoDateTitolo;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNota;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloValidaModifica
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 27-mag-03
 */

/**
Versione 1.26 di TitoloValida Esercizio_sviluppo 24.02.2015
Versione 1.28 di TitoloValida Esercizio_sviluppo 27.02.2015
*/


public class TitoloValidaModifica extends TitoloValida {

	private static final long serialVersionUID = -3133476538688117386L;

	TitoloValidaLegamiModifica validaLegami = null;
    protected SbnLivello livelloAut = null;
    private boolean chiaviModificate = false;




	// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo viene aggiornato secondo la classe corrispondente di Indice
    public final int MODIFICA_NESSUNA = 0;
    public final int MODIFICA_BASE = 1;
    public final int MODIFICA_SPECIFICITA = 2;
    public final int MODIFICA_LEGAMI_SOGGETTO = 4;
    public final int MODIFICA_LEGAMI_CLASSE = 8;

    private int modificaBaseSpecificita;

    public final int ECCEZIONE_NESSUNA = -1;

    private int cdEccezioneTitoloBase = ECCEZIONE_NESSUNA;
    private String descEccezioneTitoloBase=null;
    private int cdEccezioneSpecificita = ECCEZIONE_NESSUNA;
    private String descEccezioneSpecificita=null;
	// Fine modifica BUG MANTIS 3551 - almaviva2 23.02.2010 il controllo viene aggiornato secondo la classe corrispondente di Indice

    public TitoloValidaModifica(ModificaType tipo) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        tipoControllo = tipo.getTipoControllo();
        documentoType = tipo.getDocumento();
        elementAutType = tipo.getElementoAut();
        LegamiType[] legami = null;
        //Esempio di stringa id da prendere
        if (documentoType != null) {
            datiDoc = documentoType.getDocumentoTypeChoice().getDatiDocumento();
            id = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT001();
            c200 = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT200();
            t005 = documentoType.getDocumentoTypeChoice().getDatiDocumento().getT005();
            livelloAut = documentoType.getDocumentoTypeChoice().getDatiDocumento().getLivelloAutDoc();
            legami = documentoType.getLegamiDocumento();
            statoRecord = documentoType.getStatoRecord();
        } else if (elementAutType != null) {
            id = elementAutType.getDatiElementoAut().getT001();
            t005 = elementAutType.getDatiElementoAut().getT005();
            livelloAut = elementAutType.getDatiElementoAut().getLivelloAut();
            legami = elementAutType.getLegamiElementoAut();
            statoRecord = elementAutType.getDatiElementoAut().getStatoRecord();
        }
        validaLegami = new TitoloValidaLegamiModifica(id, legami);

    }
    /**
    * metodo di validazione per operazione di modifica autore:
    * . verificaEsistenzaID: se non trovato ritorna diagnostico 'Autore inesistente'
    * . verificaVersioneLuogo: se il risultato è negativo ritorna il diagnostico '
    * Versione non aggiornata'
    * . verificaLivelloModifica: se il risultato è falso ritorna il diagnostico al
    * client.
    * se tipoControllo <> 'Conferma'
    * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
    * autori/autori trovato al client
    * se non esistono autori simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */

    public Tb_titolo validaPerModifica(
    		TimestampHash timeHash,
    		String utente,
    		boolean modificato,
    		boolean modVariante,
    		boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;

        tb_titolo = estraiTitoloPerID(id);
        if (tb_titolo != null) {

        	// almaviva2 18.03.2015 Inizio Adeguamento a indice
        	// almaviva7 17/07/14 (update 25/09/14)
        	// Controllo cambio antico/moderno-moderno/antico non ammesso (almaviva)
        	// eccetto che per i periodici e per le collane
        	// sostituito il tipo AnnoDateTitolo con tipo String
        	String cd_natura = "";

			try
			{
        	cd_natura = estraiNatura(datiDoc);

        	//String cd_natura = estraiNatura(datiDoc);
        	if (!cd_natura.equals("S") && !cd_natura.equals("C"))
        	{
			//AnnoDateTitolo aa_pubb_1_DB = tb_titolo.getAA_PUBB_1();
        	String aa_pubb_1_DB = tb_titolo.getAA_PUBB_1();
			if (aa_pubb_1_DB != null)
			{
			String aa_pubb_1_POLO = null;
			C100 t100 = datiDoc.getT100();
			if (t100 != null)
				aa_pubb_1_POLO = datiDoc.getT100().getA_100_9();

			if (aa_pubb_1_DB != null && aa_pubb_1_POLO != null && aa_pubb_1_DB != null)
				{
//        			AnnoDateTitolo.verifyDateFormat(aa_pubb_1_POLO, "aa_pubb_1 DB");  // 10/12/14 Controllo punteggiatura (19.0 NO good)
					AnnoDateTitolo.verifyYear(aa_pubb_1_POLO, "aa_pubb_1 DB");  // 10/12/14 Controllo punteggiatura e validita' data (19.0 NO good)

					try
					{

						AnnoDateTitolo.verifyYearAntico(aa_pubb_1_DB, "aa_pubb_1 DB");
						// Anno in DB e' antico. Vediamo se e' cambiato sul polo
						try
						{
							AnnoDateTitolo.verifyYearAntico(aa_pubb_1_POLO, "aa_pubb_1 POLO");
							// INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
							// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
							// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
							 SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
	                         if (tipoMateriale == null || tipoMateriale.toString().equals(" "));
	                         else {
	                             if (tipoMateriale.equals(SbnMateriale.valueOf("E"))
	                                     || tipoMateriale.equals(SbnMateriale.valueOf("M")))
	                                 datiDoc.setTipoMateriale(SbnMateriale.VALUE_1);
	                         }
							// FINE Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
							// OK data antica anche su polo Polo
						}
						catch (EccezioneSbnDiagnostico esd)
        						{
							// INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
							// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
							// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
	                        //assegnare tipo materiale Moderno
	                        SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
	                        if (tipoMateriale == null || tipoMateriale.toString().equals(" "));
	                        else {
	                            if (tipoMateriale.equals(SbnMateriale.valueOf("E"))
	                                    || tipoMateriale.equals(SbnMateriale.valueOf("M")))
	                                datiDoc.setTipoMateriale(SbnMateriale.VALUE_0);
	                        }
//	                        throw new EccezioneSbnDiagnostico(3269, " Anno indice = antico - Anno polo = moderno"); // , "Cambio data da antico a moderno non consentito"
	                        //cambio data da antico a moderno
	                        //controllare presenza tb_impronta
	                     // INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
	                     // Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
	                     // il cambio materiale (M-->E oppure E-->M) NEL CASO DI CATTURA/ALLINEAMENTO CI PORTIAMO DIETRO IL _cattura
	                     // PERCHE' IN CASO POSITIVO IL LEGAME, SE PRESENTE DEVE ESSERE CANCELLATO LOGICAMENTE COSI' DA CONCLUDERE
	                     // CORRETTAMENTE L'OPERAZIONE RICHIESTA.
	                        verificaImpronte(tb_titolo, utente, _cattura);

	                        //controllare presenza tr_tit_mar
	                        verificaLegameMarca(tb_titolo);

	                        //controllare presenza legame 4 a tipografo/editore
	                        // Intervento BUG mantis 6263: almaviva2 Settembre 2016
	                        // Nel caso di cattura/allineamento non deve essete effettuato il controllo sulla presenza
	                        // di legami a tipografo/Editore in quanto, anche se presente, è comunque ammesso anche per
	                        // i documenti moderni e non solo per quelli antichi (prima non era cosi' quindi EVOLUTIVA
	                        // inserito controllo su cattura/allineamento
	                        if (!_cattura) {
	                        	verificaLegameAutore4(tb_titolo);
	                        }

	                        //cancellare genere a_140_9 su tb_titolo
	                        tb_titolo.setCD_GENERE_1(null);
	                        tb_titolo.setCD_GENERE_2(null);
	                        tb_titolo.setCD_GENERE_3(null);
	                        tb_titolo.setCD_GENERE_4(null);
	                        //cancellare genere di input
	                        if (datiDoc instanceof AnticoType) {
	                            AnticoType antico = (AnticoType) datiDoc;
	                            antico.setT140(null);
	                        }

	                        //cambiare tipo materiale da Antico a Moderno
//	                        SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
	                        if (tipoMateriale == null || tipoMateriale.toString().equals(" "));
	                        else {
	                            if (tipoMateriale.equals(SbnMateriale.valueOf("E")))
	                                datiDoc.setTipoMateriale(SbnMateriale.VALUE_0);
	                        }

							// FINE Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:

        						}
        					}
        					catch (EccezioneSbnDiagnostico esd)
        					{
//        	                    if (esd.getErrorID() == 3269)
//        	                    {
//        	                        // Rilanciamo l'eccezione
//        	                        throw new EccezioneSbnDiagnostico(3269, esd);
//        	                    }
        	                    if (esd.getErrorID() == 3394)
        	                    {
        	                        // Rilanciamo l'eccezione
        	                        throw new EccezioneSbnDiagnostico(3394, esd);
        	                    }
        	                    if (esd.getErrorID() == 3395)
        	                    {
        	                        // Rilanciamo l'eccezione
        	                        throw new EccezioneSbnDiagnostico(3395, esd);
        	                    }
        	                    if (esd.getErrorID() == 3396)
        	                    {
        	                        // Rilanciamo l'eccezione
        	                        throw new EccezioneSbnDiagnostico(3396, esd);
        	                    }

        	                    // Anno in DB moderno
        	                    try
        	                    {
        	                        AnnoDateTitolo.verifyYearModerno(aa_pubb_1_POLO, "aa_pubb_1 POLO");
        	                        // Ok anno moderno anche su polo
        	                    }
        	                    catch (EccezioneSbnDiagnostico ex)
        	                    {
//        	                        throw new EccezioneSbnDiagnostico(3269, " Anno indice = moderno - anno polo = antico"); // , "Cambio data da moderno ad antico non consentito"
        	                        //cambio data da MODERNO a ANTICO
        	                        //controllare presenza tb_numero_std
        		                     // INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
        		                     // Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
        		                     // il cambio materiale (M-->E oppure E-->M) NEL CASO DI CATTURA/ALLINEAMENTO CI PORTIAMO DIETRO IL _cattura
        		                     // PERCHE' IN CASO POSITIVO IL LEGAME, SE PRESENTE DEVE EDDERE CANCELLATO LOGICAMENTE COSI' DA CONCLUDERE
        		                     // CORRETTAMENTE L'OPERAZIONE RICHIESTA.
        	                        verificaNumeriStandard(tb_titolo, utente, _cattura);

        	                        SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();

        	                        //cancellare genere a_105_4 su tb_titolo
        	                        tb_titolo.setCD_GENERE_1(null);
        	                        tb_titolo.setCD_GENERE_2(null);
        	                        tb_titolo.setCD_GENERE_3(null);
        	                        tb_titolo.setCD_GENERE_4(null);
        	                        //cancellare genere di input
        	                        if (datiDoc instanceof ModernoType) {
        	                            ModernoType moderno = (ModernoType) datiDoc;
        	                            moderno.setT105(null);
        	                        }

        	                        //cambiare tipo materiale da Moderno a Antico
        	                        if (tipoMateriale == null || tipoMateriale.toString().equals(" "));
        	                        else {
        	                            if (tipoMateriale.equals(SbnMateriale.valueOf("M")))
        	                                datiDoc.setTipoMateriale(SbnMateriale.VALUE_1);
        	                        }
        	                    }
        	                }
        					} // end if dates !=  null
        				} // End aa_pubb_1_DB != null
        	        	} // End if natura non Periodico


			}
        	catch (EccezioneSbnDiagnostico ex)
			{
                // Nessun test sulle date in caso di mancata natura // cd_natura = estraiNatura(datiDoc);
//              if (ex.getErrorID() == 3269) // 01/04/2015
//                  // Rilanciamo l'eccezione
//                  throw new EccezioneSbnDiagnostico(3269, ex);
              if (ex.getErrorID() == 3394)
                  // Rilanciamo l'eccezione
                  throw new EccezioneSbnDiagnostico(3394, ex);
              if (ex.getErrorID() == 3395)
                  // Rilanciamo l'eccezione
                  throw new EccezioneSbnDiagnostico(3395, ex);
              if (ex.getErrorID() == 3396)
                  // Rilanciamo l'eccezione
                  throw new EccezioneSbnDiagnostico(3396, ex);
              if (ex.getErrorID() == 3397)
                  // Rilanciamo l'eccezione
                  throw new EccezioneSbnDiagnostico(3397, ex);
          }


        	// End almaviva7 17/07/14 (update 25/09/14)
        	// almaviva2 18.03.2015 Fine Adeguamento a indice

            if (!verificaVersioneTitolo(tb_titolo)) {
                throw new EccezioneSbnDiagnostico(3014);
            } else {
                verificaAllineamentoModificaTitolo(id);
                if (!modVariante){
                    verificaLivelloModifica(tb_titolo, utente, _cattura);
                }
                verificaLocalizzazioni(tb_titolo, utente);
                if (modificato) {
                    // Inizio modifica almaviva2 15.07.2010 Metodo modificato per adeguamenti a software di Indice identificata con commento "almaviva4 09/07/2010"
                	// e non solo quella

                	//almaviva4 9/11/2009 controllo cambio materiale
                	// almaviva2 18.03.2015 Inizio Adeguamento a indice
//  				String cd_natura = "";
					cd_natura = estraiNatura(datiDoc);
					// almaviva2 18.03.2015 Fine Adeguamento a indice
					SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
					GuidaDoc guida = datiDoc.getGuida();
					TipoRecord tipoRecord = datiDoc.getGuida().getTipoRecord();

					if (guida == null);
					else
						tipoRecord = datiDoc.getGuida().getTipoRecord();

					// almaviva CONTROLLI PER LA MODIFICA DELLE NATURE
	   				if (!tb_titolo.getCD_NATURA().equals(cd_natura)) {
					   if (!(cd_natura.equals("W") && tb_titolo.getCD_NATURA().equals("M"))
						   || !(cd_natura.equals("S") && tb_titolo.getCD_NATURA().equals("C"))
						   || !(cd_natura.equals("C") && tb_titolo.getCD_NATURA().equals("S")))
						   throw new EccezioneSbnDiagnostico(3208, "Cambio natura non consentito");
					   if (cd_natura.equals("A") && tb_titolo.getCD_NATURA().equals("B") && tipoMateriale.equals("U"))
						   throw new EccezioneSbnDiagnostico(3208, "Cambio natura non consentito");
	   				}
	   				// END 	almaviva CONTROLLI PER LA MODIFICA DELLE NATURE

	   				// Modifica del 23.09.2010 almaviva2 il tipo Materiale delle collane è uguale a spazio
	   				// e non deve rientrare nei controlli di congruenza così come il tipo record (modificato if)
//	   				if (tipoMateriale == null);
	   				if (tipoMateriale == null || tipoMateriale.toString().equals(" "));

	   				else {
	   					// Inizio Modifiche Febbraio 2015: nuovi controlli di Indice per CAMBIO TIPO MATERIALE in VARIAZIONE

//						if (tb_titolo.getBID().charAt(3) == 'E' && tipoMateriale.getType() != SbnMateriale.valueOf("E").getType())
//							throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
//						if (tb_titolo.getBID().charAt(3) != 'E' && tipoMateriale != null && tipoMateriale.getType() == SbnMateriale.valueOf("E").getType())
//							throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
	   				//almaviva4 04/10/2010 controllo cambio materiale per specificita'

	   					String tm = tipoMateriale.toString();
	   					String titTm = tb_titolo.getTP_MATERIALE() == null ? "" : tb_titolo.getTP_MATERIALE();
	   					if (!tm.equals(titTm)) // Controllo se sto cambiando il tipo materiale
	   						{  // CAMBIO MATERIALE. Quindi controlli
					        if (specializzaMateriale(tipoMateriale.toString(), utente, _cattura)) { //contollo abilitazione al materiale
								if (    tipoMateriale.equals(SbnMateriale.valueOf("U"))
										&& (!tb_titolo.getTP_MATERIALE().equals("U"))
										&& (!tb_titolo.getTP_MATERIALE().equals("M"))
										&& (!tb_titolo.getTP_MATERIALE().equals("E")) // almaviva7 14/05/2014
										)
									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
								if (    tipoMateriale.equals(SbnMateriale.valueOf("G"))
										&& (!tb_titolo.getTP_MATERIALE().equals("G"))
										&& (!tb_titolo.getTP_MATERIALE().equals("M"))
										&& (!tb_titolo.getTP_MATERIALE().equals("E")) // almaviva7 14/05/2014
										)
									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
								if (    tipoMateriale.equals(SbnMateriale.valueOf("C"))
										&& (!tb_titolo.getTP_MATERIALE().equals("C"))
										&& (!tb_titolo.getTP_MATERIALE().equals("M"))
										&& (!tb_titolo.getTP_MATERIALE().equals("E")) // almaviva7 14/05/2014
										)
									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
								// INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
								// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
								// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
//								if (    tipoMateriale.equals(SbnMateriale.valueOf("M"))
//										&& (!tb_titolo.getTP_MATERIALE().equals("M"))
//										)
//									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
//								if (    tipoMateriale.equals(SbnMateriale.valueOf("E")) // almaviva7 25/09/2014
//										&& (!tb_titolo.getTP_MATERIALE().equals("E"))
//										)
//									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
								// FINE Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
								if (    tipoMateriale.equals(SbnMateriale.valueOf("H"))
										&& (!tb_titolo.getTP_MATERIALE().equals("H"))
										&& (!tb_titolo.getTP_MATERIALE().equals("M"))
										//&& (!tb_titolo.getTP_MATERIALE().equals("E")) // almaviva4 30/12/2014
										&& (!tb_titolo.getTP_MATERIALE().equals("U")) // almaviva7 25/09/2014
										)
									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
								if (    tipoMateriale.equals(SbnMateriale.valueOf("L"))
										&& (!tb_titolo.getTP_MATERIALE().equals("L"))
										&& (!tb_titolo.getTP_MATERIALE().equals("M")) // almaviva7 14/05/2014
										&& (!tb_titolo.getTP_MATERIALE().equals("E")) // almaviva7 25/09/2014
										)
									throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");

					        } else //end contollo abilitazione al materiale
								throw new EccezioneSbnDiagnostico(3128, "Cambio tipo materiale non consentito");
					} // End CAMBIO MATERIALE
//almaviva4 04/10/2010 controlli cambio tipo record/tipo materiale insieme
						// Fine Modifiche Febbraio 2015: nuovi controlli di Indice per CAMBIO TIPO MATERIALE in VARIAZIONE

				        // Inizio Modifiche Febbraio 2015: nuovi controlli di Indice per CAMBIO TIPO MATERIALE in VARIAZIONE (1111)
				        // prese totalmente dall'omonimo di Indice:

				        if (!tb_titolo.getTP_RECORD_UNI().equals(tipoRecord.toString())
								&& !tb_titolo.getTP_MATERIALE().equals(tipoMateriale.toString()) ) {


							if (tipoRecord == null);
								else {

								  if (!tb_titolo.getTP_RECORD_UNI().equals(tipoRecord.toString())) {
										if (    (tipoRecord.toString().equals("a") && (!tipoMateriale.toString().equals("M")
										&& !tipoMateriale.toString().equals("U") && !tipoMateriale.toString().equals("E")))
											  ||  (tipoRecord.toString().equals("b") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("U")))
											  ||  (tipoRecord.toString().equals("c") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("U")))
											  ||  (tipoRecord.toString().equals("d") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("U")))
											  ||  (tipoRecord.toString().equals("e") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("C")))
											  ||  (tipoRecord.toString().equals("f") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("C")))
											  ||  (tipoRecord.toString().equals("g") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("H") && !tipoMateriale.toString().equals("U")))
											  ||  (tipoRecord.toString().equals("i") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("H")))
											  ||  (tipoRecord.toString().equals("j") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("H") && !tipoMateriale.toString().equals("U")))
											  ||  (tipoRecord.toString().equals("k") && (!tipoMateriale.toString().equals("M")
											  && !tipoMateriale.toString().equals("G")))
											  ||  (tipoRecord.toString().equals("l") && !tipoMateriale.toString().equals("M"))
											  ||  (tipoRecord.toString().equals("m") && !tipoMateriale.toString().equals("M"))
											  ||  (tipoRecord.toString().equals("r") && !tipoMateriale.toString().equals("M"))
					                          ||  (tipoRecord.toString().equals("g") && (!tipoMateriale.toString().equals("M")
												   && !tipoMateriale.toString().equals("U") && !tipoMateriale.toString().equals("H")))
								              ||  (tipoRecord.toString().equals("j") && (!tipoMateriale.toString().equals("M")
												   && !tipoMateriale.toString().equals("U") && !tipoMateriale.toString().equals("H")))
											  ||  (tipoRecord.toString().equals("i") && (!tipoMateriale.toString().equals("M")
												   && !tipoMateriale.toString().equals("H")))
											  ||  (tipoRecord.toString().equals("l") && (!tipoMateriale.toString().equals("M")
												   && !tipoMateriale.toString().equals("L")))
											)
											  throw new EccezioneSbnDiagnostico(3271); // ,"Tipo record non compatibile con tipo materiale"
  										  String tipoTesto = "";
										  if (datiDoc instanceof MusicaType) {
											  if (datiDoc != null) {
												  if (((MusicaType)datiDoc).getT125() != null)
													tipoTesto = ((MusicaType)datiDoc).getT125().getB_125();
											  }
										  }
								  }
								}


							if (!tb_titolo.getTP_MATERIALE().equals(tipoMateriale.toString())) {
								if (    (tipoMateriale.toString().equals("U") && (!tipoRecord.toString().equals("a")
										&& !tipoRecord.toString().equals("b") && !tipoRecord.toString().equals("c")
										&& !tipoRecord.toString().equals("d") && !tipoRecord.toString().equals("g")
										&& !tipoRecord.toString().equals("j")) )
										||  (tipoMateriale.toString().equals("E") && !tipoRecord.toString().equals("a")
												&& !tipoRecord.toString().equals("b") && !tipoRecord.toString().equals("c")
												&& !tipoRecord.toString().equals("d") && !tipoRecord.toString().equals("e")
												&& !tipoRecord.toString().equals("f") && !tipoRecord.toString().equals("k"))
										||  (tipoMateriale.toString().equals("C") && !tipoRecord.toString().equals("e")
										&& !tipoRecord.toString().equals("f"))
										||  (tipoMateriale.toString().equals("H") && !tipoRecord.toString().equals("g")
										&& !tipoRecord.toString().equals("i") && !tipoRecord.toString().equals("j"))
										||  (tipoMateriale.toString().equals("L") && !tipoRecord.toString().equals("l"))
										||  (tipoMateriale.toString().equals("G") && !tipoRecord.toString().equals("k")) )
										throw new EccezioneSbnDiagnostico(3271); // , "Tipo record non compatibile con tipo materiale"

								  String tipoTesto = "";
								  if (datiDoc instanceof MusicaType) {
									  if (datiDoc != null) {
										  if (((MusicaType)datiDoc).getT125() != null)
											tipoTesto = ((MusicaType)datiDoc).getT125().getB_125();
									  }
								  }

								// INIZIO BUG 6422 mantis esercizio; viene rimosso il controllo di coerenza incrociata in coerenza
								// con quanto fatto su Indice
//								if  (tipoMateriale.toString().equals("U") && tipoRecord.toString().equals("a")) {
//									if ( !ValidazioneDati.isFilled(tb_titolo.getCD_GENERE_1())
//									|| !(tb_titolo.getCD_GENERE_1().charAt(0) == '2')
//									|| !(tipoTesto.toString().equals("b")) )
//										throw new EccezioneSbnDiagnostico(3272, "Tipo record non compatibile con genere");
//								}
//								if  (tipoMateriale.toString().equals("U") && tipoRecord.toString().equals("b")) {
//									if ( !ValidazioneDati.isFilled(tb_titolo.getCD_GENERE_1())
//									|| !(tb_titolo.getCD_GENERE_1().charAt(0) == '3')
//									|| !(tipoTesto.toString().equals("b")) )
//										throw new EccezioneSbnDiagnostico(3272, "Tipo record non compatibile con genere");
//								}
								// FINE BUG 6422 mantis esercizio;
							}
				        } //end if cambio tipo record/tipo materiale insieme

						else  {

								if (tipoRecord == null);
									else {

									  if (!tb_titolo.getTP_RECORD_UNI().equals(tipoRecord.toString())) {
											if (    (tipoRecord.toString().equals("a") && (!tb_titolo.getTP_MATERIALE().equals("M")
											&& !tb_titolo.getTP_MATERIALE().equals("U") && !tb_titolo.getTP_MATERIALE().equals("E")))
												  ||  (tipoRecord.toString().equals("b") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("U")))
												  ||  (tipoRecord.toString().equals("c") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("U")))
												  ||  (tipoRecord.toString().equals("d") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("U")))
												  ||  (tipoRecord.toString().equals("e") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("C")))
												  ||  (tipoRecord.toString().equals("f") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("C")))
												  ||  (tipoRecord.toString().equals("g") && (!tb_titolo.getTP_MATERIALE().equals("M")
														&& !tb_titolo.getTP_MATERIALE().equals("U") && !tb_titolo.getTP_MATERIALE().equals("H")))
												  ||  (tipoRecord.toString().equals("i") && !tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("H"))
												  ||  (tipoRecord.toString().equals("j") && (!tb_titolo.getTP_MATERIALE().equals("M")
												  && !tb_titolo.getTP_MATERIALE().equals("U") && !tb_titolo.getTP_MATERIALE().equals("H")))
												  ||  (tipoRecord.toString().equals("k") && (!tb_titolo.getTP_MATERIALE().equals("M")
														  && !tb_titolo.getTP_MATERIALE().equals("E") && !tb_titolo.getTP_MATERIALE().equals("G")))
												  ||  (tipoRecord.toString().equals("l") && !tb_titolo.getTP_MATERIALE().equals("M"))
												  ||  (tipoRecord.toString().equals("m") && !tb_titolo.getTP_MATERIALE().equals("M"))
												  ||  (tipoRecord.toString().equals("r") && !tb_titolo.getTP_MATERIALE().equals("M"))     )
												  throw new EccezioneSbnDiagnostico(3271); // , "Tipo record non compatibile con tipo materiale"

													String tipoTesto = "";
													if (datiDoc instanceof MusicaType) {
														if (datiDoc != null) {
															if (((MusicaType)datiDoc).getT125() != null)
															  tipoTesto = ((MusicaType)datiDoc).getT125().getB_125();
														}
													}
									  } //end if tipo record modificato
									}  //end else tipo record null

								if (!tb_titolo.getTP_MATERIALE().equals(tipoMateriale.toString())) {
									if (    (tipoMateriale.toString().equals("U") && (!tb_titolo.getTP_RECORD_UNI().equals("a")
									&& !tb_titolo.getTP_RECORD_UNI().equals("b") && !tb_titolo.getTP_RECORD_UNI().equals("c")
									&& !tb_titolo.getTP_RECORD_UNI().equals("d") && !tipoRecord.toString().equals("g")
									&& !tb_titolo.getTP_RECORD_UNI().equals("j")) )
											||  (tipoMateriale.toString().equals("E") && !tb_titolo.getTP_RECORD_UNI().equals("a")
													&& !tipoRecord.toString().equals("b") && !tipoRecord.toString().equals("c")
													&& !tipoRecord.toString().equals("d") && !tipoRecord.toString().equals("e")
													&& !tipoRecord.toString().equals("f") && !tipoRecord.toString().equals("k"))
											||  (tipoMateriale.toString().equals("C") && !tb_titolo.getTP_RECORD_UNI().equals("e")
											&& !tb_titolo.getTP_RECORD_UNI().equals("f"))
											||  (tipoMateriale.toString().equals("G") && !tb_titolo.getTP_RECORD_UNI().equals("k")) )
											throw new EccezioneSbnDiagnostico(3271); // , "Tipo record non compatibile con tipo materiale"

									String tipoTesto = "";
									if (datiDoc instanceof MusicaType) {
											if (datiDoc != null) {
													if (((MusicaType)datiDoc).getT125() != null)
														  tipoTesto = ((MusicaType)datiDoc).getT125().getB_125();
											}
									}

								} //end if tipo materiale modificato

						} //end else cambio tipo record/tipo materiale insieme

					} //end else tipo materiale = null

//almaviva4 04/10/2010 controlli cambio tipo record/tipo materiale insieme fine


					if (datiDoc != null) {
						String notaCast = null;
						for (int i = 0; i < datiDoc.getT3XXCount(); i++) {
							SbnTipoNota tipoNota = datiDoc.getT3XX(i).getTipoNota();
							if (tipoNota != null && SbnTipoNota.valueOf("323").equals(tipoNota)) {
								notaCast = datiDoc.getT3XX(i).getA_3XX();
							}
						} // fine for
						if ((notaCast != null) && (!tipoRecord.toString().equals("g")
										&& !tipoRecord.toString().equals("i") && !tipoRecord.toString().equals("j"))) {
							throw new EccezioneSbnDiagnostico(3273, "Nota al cast non compatibile con genere");
						}
					} //fine if per nota al cast


			        // Fine Modifiche Febbraio 2015: nuovi controlli di Indice per CAMBIO TIPO MATERIALE in VARIAZIONE (1111)
					// prese totalmente dall'omonimo di Indice:



                    validaElemento(utente, timeHash,_cattura);
                    int tipoMod = tipoModifica(tb_titolo);
                    if (tipoMod > 0) {
                        chiaviModificate = true;
                        //mantis 2466
                        // if (!tb_titolo.getCd_natura().equals("W") && statoRecord != null && statoRecord.equals(StatoRecord.valueOf("c")))
 					    if (statoRecord != null && statoRecord.equals(StatoRecord.valueOf("c")))
 					    	if (tipoControllo == null || tipoControllo.getType() != SbnSimile.CONFERMA_TYPE) {
                                elencoDiagnostico = cercaTitoliSimili(tb_titolo, tb_titolo.getCD_NATURA(), tipoMod);
 					    	 } else {
                                //elencoDiagnostico = cercaTitoliSimiliConferma(null, id, tb_titolo.getCD_NATURA());
 					    	 }
                    }
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        }
        validaLegami.validaLegamiDocumentoModifica(utente, timeHash, tb_titolo, _cattura);

        //almaviva5_20160907 #6266 forzatura utente variazione (per successivo aggiornamento legami)
        tb_titolo.setUTE_VAR(utente);

        return tb_titolo;
    }

    /**
     * @param tb_titolo
     * @return
     * 1 -> modificate solo le chiavi
     * 2 -> modificato solo il resto (numero std o impronta)
     * 3 -> modificati entrambi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected int tipoModifica(Tb_titolo tb_titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        int ret = 0;
        ChiaviTitolo chiavi = new ChiaviTitolo();
        chiavi.estraiChiavi(c200);
        String aa_pubb = null;
        String paese = null;
        String lingua[]= null;
        if (datiDoc != null) {
            if(datiDoc.getT100() != null)
                aa_pubb = datiDoc.getT100().getA_100_9();
            if(datiDoc.getT102() != null)
                paese = datiDoc.getT102().getA_102();
            if(datiDoc.getT101() != null)
                lingua = datiDoc.getT101().getA_101();
        }
        if (tb_titolo.getCD_NATURA().equals("W")) {
            if (!(aa_pubb == null && tb_titolo.getAA_PUBB_1() == null)
                    || (aa_pubb != null
                        && tb_titolo.getAA_PUBB_1() != null
                        && aa_pubb.equals(tb_titolo.getAA_PUBB_1()))) {
//                && aa_pubb.equals(tb_titolo.getAA_PUBB_1().getAnnoDate()))) {
                ret = 1;
            }
            NumStdType[] num = datiDoc.getNumSTD();
            NumeroStd numero = new NumeroStd();
            List v = numero.cercaNumeroPerBid(tb_titolo.getBID());
            if (numStdModificati(v, num))
                ret += 2;
        } else {
            if (!(chiavi.getKy_cles1_t().trim().equals(tb_titolo.getKY_CLES1_T().trim())
                && chiavi.getKy_cles2_t().trim().equals(tb_titolo.getKY_CLES2_T().trim())
                && chiavi.getKy_clet1_t().trim().equals(tb_titolo.getKY_CLET1_T().trim())
                && chiavi.getKy_clet2_t().trim().equals(tb_titolo.getKY_CLET2_T().trim())
                && ((paese == null && tb_titolo.getCD_PAESE() == null)
                    || (paese != null && paese.equals(tb_titolo.getCD_PAESE())))
                && (!lingueModificate(tb_titolo,datiDoc.getT101()))
                && ((aa_pubb == null && tb_titolo.getAA_PUBB_1() == null)
                    || (aa_pubb != null
                        && tb_titolo.getAA_PUBB_1() != null
                        && aa_pubb.equals(tb_titolo.getAA_PUBB_1()))))) {
///                && aa_pubb.equals(tb_titolo.getAA_PUBB_1().getAnnoDate()))))) {
                ret = 1;
            }
            if (datiDoc instanceof AnticoType) {
                C012[] c012 = ((AnticoType) datiDoc).getT012();
                Impronta imp = new Impronta();
                List v = imp.cercaPerBid(tb_titolo.getBID());
                if (impronteModificate(v, c012))
                    ret += 2;
            } else {
                NumStdType[] num = datiDoc.getNumSTD();
                NumeroStd numero = new NumeroStd();
                List v = numero.cercaNumeroPerBid(tb_titolo.getBID());
                if (numStdModificati(v, num))
                    ret += 2;
            }
        }
        return ret;
    }
    /**
     * @param tb_titolo
     * @param c101
     * @return
     */
    protected boolean lingueModificate(Tb_titolo tb_titolo, C101 c101) {
        int n=0;
        if (tb_titolo.getCD_LINGUA_3() != null && tb_titolo.getCD_LINGUA_3().trim().length()>0) {
            n=3;
            if (c101 == null || n != c101.getA_101Count() || !tb_titolo.getCD_LINGUA_3().equals(c101.getA_101(2))) {
                return true;
            }
        }
        if (tb_titolo.getCD_LINGUA_2() != null && tb_titolo.getCD_LINGUA_2().trim().length()>0) {
            if (n==0) {
                n=2;
            }
            if (c101 == null || n != c101.getA_101Count() || !tb_titolo.getCD_LINGUA_2().equals(c101.getA_101(1))) {
                return true;
            }
        }
        if (tb_titolo.getCD_LINGUA_1() != null && tb_titolo.getCD_LINGUA_1().trim().length()>0) {
            if (n==0) {
                n=1;
            }
            if (c101 == null || n != c101.getA_101Count() || !tb_titolo.getCD_LINGUA_1().equals(c101.getA_101(0))) {
                return true;
            }
        }
        return false;
    }

    public boolean isChiaviModificate() {
        return chiaviModificate;
    }

    private boolean impronteModificate(List v, C012[] c012) {
        if (v.size() != c012.length)
            return true;
        //Per ogni elemento devo verificare se esistono dall'altra parte.
        for (int i = 0; i < v.size(); i++) {
            Tb_impronta imp = (Tb_impronta) v.get(i);
            boolean trovato = false;
            for (int c = 0; c < c012.length; c++) {
                if (imp.getIMPRONTA_1().equals(c012[c].getA_012_1())
                    && imp.getIMPRONTA_2().equals(c012[c].getA_012_2())
                    && imp.getIMPRONTA_3().equals(c012[c].getA_012_3())) {
                    trovato = true;
                    break;
                }
            }
            if (!trovato)
                return true;
        }
        return false;
    }

    private boolean numStdModificati(List v, NumStdType[] std) {
        if (v.size() != std.length)
            return true;
        //Per ogni elemento devo verificare se esistono dall'altra parte.
        for (int i = 0; i < v.size(); i++) {
            Tb_numero_std imp = (Tb_numero_std) v.get(i);
            boolean trovato = false;
            for (int c = 0; c < std.length; c++) {
                if (imp.getNUMERO_STD().trim().equals(std[c].getNumeroSTD().trim())
                    && imp.getTP_NUMERO_STD().trim().equals(
                        Decodificatore.getCd_tabella(
                            "Tb_numero_std",
                            "tp_numero_std",
                            std[c].getTipoSTD().toString()))) {
                    trovato = true;
                    break;
                }
            }
            if (!trovato)
                return true;
        }
        return false;
    }

    /**
     * metodo di validazione per operazione di creazione autore:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @throws InfrastructureException
     */
    public boolean validaElemento(String utente, TimestampHash timeHash, boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

    	// modifica BUG MANTIS 3551 - almaviva2 23.02.2010 (come Indice negli if si tiene conto delle eccezioni)
        String cd_natura = "";
        if (documentoType != null) {
            if (datiDoc != null) {
                cd_natura = estraiNatura(datiDoc);
                SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
                if (tipoMateriale == null || tipoMateriale.getType() == SbnMateriale.valueOf(" ").getType())
                    tipoMateriale = SbnMateriale.valueOf("M");

                // Inizio Intervento per Google3: l’interrogazione effettuata da un Polo non Abilitato ad uno specifico Materiale
                // non propone le specificità ma invia il DocType del moderno mantenendo il tipo Materiale corretto, quindi si deve
                // gestire l’inserimento nel DB di polo di documenti con tipo materiale ‘U’, ‘C’ o ‘G’ senza specificità
                // Inserito il Primo if per verificare le casistiche in oggetto


             // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
                if (datiDoc instanceof ModernoType &&
                    	tipoMateriale.getType() != SbnMateriale.valueOf("M").getType()) {
                    	   validaPerCrea(datiDoc, cd_natura,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("M").getType()) {
                    validaPerCrea(datiDoc, cd_natura,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("E").getType()) {
                    validaPerCreaAntico(datiDoc, cd_natura,_cattura);
//                } else if (tipoMateriale.equals(SbnMateriale.valueOf("U"))) {
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("U").getType() && cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                    validaPerCreaMusica(datiDoc, cd_natura,utente,_cattura);
//                } else if (tipoMateriale.equals(SbnMateriale.valueOf("G"))) {
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("G").getType() && cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                    validaPerCreaGrafica(datiDoc, cd_natura,utente,_cattura);
//                } else if (tipoMateriale.equals(SbnMateriale.valueOf("C"))) {
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("C").getType() && cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                    validaPerCreaCartografia(datiDoc, cd_natura,utente,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("H").getType() && cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                	// Intervento settembre 2015 : quando il Polo è abilitatao alla sola Musica Indice manda Tipo Materiale H
                	// ma type Musica: si inserisce il controllo
                	 if (datiDoc instanceof AudiovisivoType) {
                		 validaPerCreaAudiovisivo(datiDoc, cd_natura,utente,_cattura);
                	 } else  if (datiDoc instanceof MusicaType) {
                		 validaPerCreaMusica(datiDoc, cd_natura,utente,_cattura);
                	 } else {
                		 validaPerCrea(datiDoc, cd_natura,_cattura);
                	 }
                	// validaPerCreaAudiovisivo(datiDoc, cd_natura,utente,_cattura);

                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("L").getType() && cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                	validaPerCreaElettronico(datiDoc, cd_natura,utente,_cattura);

                } else {
                    validaPerCrea(datiDoc, cd_natura,_cattura);
                }
            }
        } else {
            throw new EccezioneSbnDiagnostico(3103, "Dati non consistenti");
        }
        return true;
    }

    /**
     * metodo che verifica se l'utente ha sufficente livello di autorità per
     * effettuare l'operazione di modifica richiesta.
     * decodifica livelloAut in tb_codici
     * se livelloAut > cd_livello letto sul db predispone in diagnostico: 'Livello di
     * autorità utente non consente l'operazione'.
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    protected void verificaLivelloModifica(Tb_titolo tb_titolo, String utente, boolean _cattura)
        throws EccezioneSbnDiagnostico {
    	// Inizio modifica BUG MANTIS 3551 - almaviva2 23.02.2010 (come Indice)

    	modificaBaseSpecificita = 0;
    	String livello = null;

        if(!_cattura) {
            if (Integer.parseInt(livelloAut.toString()) < Integer.parseInt(tb_titolo.getCD_LIVELLO())) {
            	cdEccezioneTitoloBase = 3010;
				descEccezioneTitoloBase = "Livello di autorità in base dati superiore a quello comunicato";
//            	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
            }
            String tp_materiale;
            if (tb_titolo.getCD_NATURA().equals("C") && tb_titolo.getTP_MATERIALE().equals(" ")) {
                tp_materiale = "M";
            } else {
                tp_materiale = tb_titolo.getTP_MATERIALE();
            }

			// il livello autorità del titolo base può essere moderno o antico
            final Tbf_par_mat parTitBase = (datiDoc instanceof AnticoType) ?
            	ValidatorProfiler.getInstance().getParametriUtentePerMateriale(utente, "E") :
				ValidatorProfiler.getInstance().getParametriUtentePerMateriale(utente, "M");

            // Modifica almaviva2 Giugno 2015 per consentire controlli e modifiche sui nuovi materiali
            // (ElettronicoType e AudiovisivoType) presa da Indice
            if (datiDoc instanceof MusicaType || datiDoc instanceof GraficoType || datiDoc instanceof ElettronicoType
            		|| datiDoc instanceof CartograficoType || datiDoc instanceof AudiovisivoType) {
//                String livello = null;
                if (datiDoc instanceof MusicaType)
                    livello = ((MusicaType)datiDoc).getLivelloAut().toString();
                if (datiDoc instanceof GraficoType)
                    livello = ((GraficoType)datiDoc).getLivelloAut().toString();
                if (datiDoc instanceof CartograficoType)
                    livello = ((CartograficoType)datiDoc).getLivelloAut().toString();
                if (datiDoc instanceof AudiovisivoType)
                    livello = ((AudiovisivoType)datiDoc).getLivelloAut().toString();
                if (datiDoc instanceof ElettronicoType)
                    livello = ((ElettronicoType)datiDoc).getLivelloAut().toString();
                Tbf_par_mat par =
                	ValidatorProfiler.getInstance().getParametriUtentePerMateriale(utente, tp_materiale);
                if (par == null || par.getTp_abilitaz()!='S') {

                	// Intervento Settembre 2015: nel caso di mancata abilitazione al materiale H si deve verificare se esiste
                	// almeno l'abilitazione alla Musica per aggiornare la parte suddeta
                	if (tp_materiale.equals("H")) {
                		 Tbf_par_mat parMusica =
                         	ValidatorProfiler.getInstance().getParametriUtentePerMateriale(utente, "U");
                		 if (parMusica == null || parMusica.getTp_abilitaz()!='S') {
                			 cdEccezioneSpecificita = 3245;
             				descEccezioneSpecificita = "Utente non abilitato a modificare il materiale";
//                           throw new EccezioneSbnDiagnostico(3245, "Utente non abilitato a modificare il materiale");
                		 }
                	} else {
                		cdEccezioneSpecificita = 3245;
        				descEccezioneSpecificita = "Utente non abilitato a modificare il materiale";
//                      throw new EccezioneSbnDiagnostico(3245, "Utente non abilitato a modificare il materiale");
                	}

                }

                if (cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                    String livelloUtente = par.getCd_livello();
                    if (livelloUtente == null || Integer.parseInt(livello) > Integer.parseInt(livelloUtente)) {
                    	cdEccezioneSpecificita = 3007;
        				descEccezioneSpecificita = "Livello di autorità utente non consente l'operazione";
//                      throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
                    }
                    if (cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
                        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE) {
                            if (par.getFl_abil_forzat()!='S') {
                            	cdEccezioneSpecificita = 3008;
                				descEccezioneSpecificita = "Utente non abilitato per la forzatura";
//                              throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
                            }
                        }
                    }
                }

                if (cdEccezioneSpecificita == ECCEZIONE_NESSUNA) {
        			modificaBaseSpecificita |= MODIFICA_SPECIFICITA;
                }

                // fine specificità
            } else {
                if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE) {
                    if (parTitBase.getFl_abil_forzat()!='S') {

    					if (modificaBaseSpecificita == MODIFICA_NESSUNA) {
    						throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
    					} else {
    						if (cdEccezioneTitoloBase == ECCEZIONE_NESSUNA)	{
    							cdEccezioneTitoloBase = 3008;
    							descEccezioneTitoloBase = "Utente non abilitato per la forzatura";
    						}
    					}
//                        throw new EccezioneSbnDiagnostico(3008, "Utente non abilitato per la forzatura");
                    }
                }
            }
            String livelloUtente = parTitBase.getCd_livello();

//            if (livelloUtente == null || Integer.parseInt(livelloAut.toString()) > Integer.parseInt(livelloUtente))
//                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        	if (livelloUtente == null || Integer.parseInt(livelloAut.toString()) > Integer.parseInt(livelloUtente))	{
        		if (modificaBaseSpecificita == MODIFICA_NESSUNA) {
        			throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        		} else {
        			if (cdEccezioneTitoloBase == ECCEZIONE_NESSUNA) {
        				cdEccezioneTitoloBase = 3007;
        				descEccezioneTitoloBase = "Livello di autorità utente non consente l'operazione";
        			}
        		}
        	} else {
        		if (cdEccezioneTitoloBase == ECCEZIONE_NESSUNA) {
        			modificaBaseSpecificita |= MODIFICA_BASE;
        		}
        	}



        	if (cdEccezioneTitoloBase != ECCEZIONE_NESSUNA) {
        		return;
        	}

            if (!livelloUtente.equals("97")) {
                if (tb_titolo.getCD_LIVELLO().equals("90")
                    || tb_titolo.getCD_LIVELLO().equals("95")
                    || tb_titolo.getCD_LIVELLO().equals("97"))
                    if (tb_titolo.getCD_LIVELLO().equals(livelloAut.toString()))


//              Inizio bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
//                        if (!utente.startsWith(tb_titolo.getUTE_VAR().substring(0,6)))
                        if (!utente.startsWith(tb_titolo.getUTE_VAR().substring(0,3))) {
							if (modificaBaseSpecificita == MODIFICA_BASE) {
								throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
							} else {
							cdEccezioneTitoloBase = 3116;
							descEccezioneTitoloBase = "Titolo portato a max da altro utente";
							}
//                            throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
                        }
//              Fine bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
           }





            if (tb_titolo.getCD_NATURA().equals("N")) {
//                if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S') {
//                	throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");
//                }
    			if (modificaBaseSpecificita == MODIFICA_BASE) {
    	            if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S') {
    	                throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");
    	            }
   	            } else {
    				if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S') {
    					cdEccezioneTitoloBase = 3244;
    					descEccezioneTitoloBase = "Impossibile modificare natura N";
    				}
    			}
            }
        }// end cattura
        else {
        	// Inizio modifica almaviva2 2010.11.17 BUG MANTIS 3990 - in caso di cattura non si esce senza effettuare
        	// operazioni ma impostando i valori come se i controlli sui livelli fossero tutti positivi altrimenti viene
        	// eseguito solo update della versione e non quello di tutto l'oggetto

        	// Bug MANTIS 5908 almaviva2
        	// In questo oggetto si effettuano SOLO i controlli sulle autorizzazione/livelli Autorità:
        	// il valore del campo modificaBaseSpecificita deve contenere sia il valore di MODIFICA_BASE che quello di MODIFICA_SPECIFICITA
        	// che prima era mancante altrimenti nella cattura/allineamento non vengono aggiornate/create le tabelle delle specificità
        	modificaBaseSpecificita |= MODIFICA_BASE;
        	modificaBaseSpecificita |= MODIFICA_SPECIFICITA;
        	// Fine modifica almaviva2 2010.11.17 BUG MANTIS 3990
        }
    }

    /**
     * metodo che verifica se il titolo è localizzato presso il polo, se l'utente è un
     * utente di polo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void verificaLocalizzazioni(Tb_titolo tb_titolo, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }
        TitoloBiblioteca titBib = new TitoloBiblioteca();
        //verifica che il titolo sia localizzato per gestione nel polo dell'utente, oppure che
        //non ci siano altre localizzazioni e l'utente sia l'utente che ha inserito
        if (titBib.verificaLocalizzazioniPoloUguale(tb_titolo.getBID(), utente.substring(0, 6)))
            return;
        if (!(utente.substring(0, 3).equals(tb_titolo.getUTE_INS().substring(0, 3))))
            throw new EccezioneSbnDiagnostico(3283, "Titolo non localizzato e inserito da altro utente");
        if (titBib.verificaLocalizzazioniPoloDiverso(tb_titolo.getBID(), utente.substring(0, 3)))
            throw new EccezioneSbnDiagnostico(3092, "Titolo localizzato in altri poli");

    }

 // Inizio almaviva2 03.08.2010 - Modifiche riportate dal software di Indice
    public int getModificaBaseSpecificita() {
    	return modificaBaseSpecificita;
    }

    /**
     * @param i
     */
    public void setModificaBaseSpecificita(int i) {
    	modificaBaseSpecificita = i;
    }

    /**
     * @return
     */
    public int getCdEccezioneTitoloBase() {
    	return cdEccezioneTitoloBase;
    }

    /**
     * @return
     */
    public String getDescEccezioneTitoloBase() {
    	return descEccezioneTitoloBase;
    }


    /**
     * @return
     */
    public int getCdEccezioneSpecificita() {
    	return cdEccezioneSpecificita;
    }

    /**
     * @return
     */
    public String getDescEccezioneSpecificita() {
    	return descEccezioneSpecificita;
    }

    // Fine almaviva2 03.08.2010 - Modifiche riportate dal software di Indice


    /** Verifica se un tipo di materiale deve essere specializzato oppure no invocando il Validator */
    protected static boolean specializzaMateriale(String tpMateriale, String utente, boolean _cattura) {

    	// INTERVENTO SETTEMBRE 2015; se siamo in cattura siamo abilitati a tuti i materiali
    	 if(_cattura) {
    		 return true;
    	 }

        ValidatorProfiler valc = ValidatorProfiler.getInstance();
        if (valc == null)
            return false;
        Tbf_par_mat par_mat = valc.getParametriUtentePerMateriale(utente, tpMateriale);
        if (par_mat != null)
            if (par_mat.getTp_abilitaz() == 'S')
                return true;
            else
                return false;
        return false;
    }


    // INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
	// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
	// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
    // Nuovi metodi per effettuare i controlli di congruenza
    /**
     * metodo che verifica se il titolo antico possiede impronte
     * @throws Exception
     */
    protected void verificaImpronte(Tb_titolo tb_titolo, String utente, boolean _cattura)
        throws Exception {
        TitoloValida titoloValida = new TitoloValida();
        Tb_impronta impronta = titoloValida.estraiImprontePerBid(tb_titolo.getBID());
        if (impronta != null) {
        	 if (_cattura) {
        		// INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
        		// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
        		// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
        		// NEL CASO DI CATTURA/ALLINEAMENTO IL LEGAME, SE PRESENTE DEVE EDDERE CANCELLATO LOGICAMENTE COSI' DA CONCLUDERE
        		 // CORRETTAMENTE L'OPERAZIONE RICHIESTA.
        		 Impronta classeImpronta = new Impronta();
        		 classeImpronta.cancellaPerBid(impronta.getBID(), utente);
        	 } else {
        		 throw new EccezioneSbnDiagnostico(3394); //"Operazione cambio data non consentita, presenza di impronte"
        	 }
        }
    }

    /**
     * metodo che verifica se il titolo antico possiede legami a marche
     * @throws Exception
     */
    protected void verificaLegameMarca(Tb_titolo tb_titolo)
        throws Exception {
        TitoloValida titoloValida = new TitoloValida();
        Tr_tit_mar titMar = titoloValida.estraiLegameMarca(tb_titolo.getBID());
        if (titMar!=null) {
            throw new EccezioneSbnDiagnostico(3395); //"Operazione cambio data non consentita, presenza di legame a marca"
        }
    }

    /**
     * metodo che verifica se il titolo antico possiede legami 4 con tipografo o editore
     * @throws Exception
     */
    protected void verificaLegameAutore4(Tb_titolo tb_titolo)
        throws Exception {
        TitoloValida titoloValida = new TitoloValida();
        Tr_tit_aut titAut = titoloValida.estraiLegameAutore4(tb_titolo.getBID());
        if (titAut!=null) {
            throw new EccezioneSbnDiagnostico(3396); //"Operazione cambio data non consentita, presenza di legame a tipografo o editore"
        }
    }

    /**
     * metodo che verifica se il titolo moderno possiede numeri standard
     * @throws Exception
     */
    protected void verificaNumeriStandard(Tb_titolo tb_titolo, String utente, boolean _cattura)
        throws Exception {
        TitoloValida titoloValida = new TitoloValida();
        Tb_numero_std numStd = titoloValida.estraiNumeriStandardPerBid(tb_titolo.getBID());
    	String titTm = tb_titolo.getTP_MATERIALE() == null ? "" : tb_titolo.getTP_MATERIALE();
    	if (numStd!=null) { //presenza numeri standard
    		if (tb_titolo.getTP_RECORD_UNI() == null || tb_titolo.getTP_RECORD_UNI().equals(" ")) {
    			//diagn numeri standard non consentiti
    	        throw new EccezioneSbnDiagnostico(3397); //"Operazione cambio data non consentita, presenza di numeri standard"
    		} else {
    			//per tipo record musicale e materiale moderno se esistono solo num_std 'Y' o 'L' vanno salvati
    			//e non emesso il diagnostico
    			if ( (tb_titolo.getTP_RECORD_UNI().equals("c") || tb_titolo.getTP_RECORD_UNI().equals("d"))
    					&& titTm.equals("M") ) {
    				int numeroStd = titoloValida.estraiNumeriStandardPerBidMus(tb_titolo.getBID());
    				if (numeroStd > 0) {
    				    throw new EccezioneSbnDiagnostico(3397); //"Operazione cambio data non consentita, presenza di numeri standard"
    				} else { //esitono solo numeri 'Y' e 'L' quindi non occorre fare nulla
    				}
    			} else {
    				 if (_cattura) {
    		        		// INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
    		        		// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
    		        		// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
    		        		// NEL CASO DI CATTURA/ALLINEAMENTO IL LEGAME, SE PRESENTE DEVE EDDERE CANCELLATO LOGICAMENTE COSI' DA CONCLUDERE
    		        		 // CORRETTAMENTE L'OPERAZIONE RICHIESTA.
    		        		 NumeroStd numeroStd = new NumeroStd();
    		        		 numeroStd.cancellaPerBid(numStd.getBID(), utente);
    		        	 } else {
    		        		 throw new EccezioneSbnDiagnostico(3397); //"Operazione cambio data non consentita, presenza di numeri standard"
    		        	 }
    			}
    		}
        } //end if numStd not null
    } //end metodo

    // FINE Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:

}
