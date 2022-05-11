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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_claResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.ThesauroTitolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;



/**
 * Classe TitoloValidaLegamiModifica
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 28-mag-03
 */
public class TitoloValidaLegamiModifica extends TitoloValidaLegami {
    /**
	 *
	 */
	private static final long serialVersionUID = 3232046651260390278L;
	boolean modificaLegamiAutore = false;
    public TitoloValidaLegamiModifica(String id, LegamiType[] legami) throws IllegalArgumentException, InvocationTargetException, Exception {
        super(id, legami);
    }

    /** Valida per creazione, cancellazione o modifica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void validaLegamiDocumentoModifica(String utente, TimestampHash timeHash, Tb_titolo titolo, boolean cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String cd_natura = titolo.getCD_NATURA();
        String tp_materiale = titolo.getTP_MATERIALE();
        List<String> legamiAutoreCancellati = new ArrayList<String>();


        // Inizio manutenzione riportata da Indice Su SbnWEB BUG 2982
        //mantis 1353
        // Questa funzione mi permette di verificare se sto
        //modificando un legame autore con tipolegame = ""
        //Nel vettore VettorelegamiAutoreDaCancellare carico tutti i legami in cancellazione
        //con tipolegame =""
        boolean RespMod = false;
        LegameElementoAutType legamiAutoreDaCancellare = new LegameElementoAutType();
        List<LegameElementoAutType> VettorelegamiAutoreDaCancellare = new ArrayList<LegameElementoAutType>();
		for (int x = 0; x < legami.length; x++) {
			if (legami[x].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE){
				for (int t = 0; t < legami[x].getArrivoLegameCount(); t++) {
					ArrivoLegame legameCanc = legami[x].getArrivoLegame(t);
					if (legameCanc.getLegameElementoAut() != null) {
						legamiAutoreDaCancellare = legameCanc.getLegameElementoAut();
						VettorelegamiAutoreDaCancellare.add(legamiAutoreDaCancellare);
					}
				}
			}
		}
        //fine 1353
        // Fine manutenzione riportata da Indice Su SbnWEB BUG 2982



        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                boolean modifica = legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE;
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                    if (legame.getLegameDoc() != null)
                        validaCreaLegameDocDoc(
                            legame.getLegameDoc(),
                            titolo.getBID(),
                            titolo.getCD_NATURA(),
                            titolo.getTP_RECORD_UNI(),
                            utente,
                            timeHash);
                    else if (legame.getLegameTitAccesso() != null)
                        validaCreaLegameDocTitAccesso(
                            titolo.getBID(),
                            cd_natura,
                            titolo.getTP_RECORD_UNI(),
                            tp_materiale,
                            legame.getLegameTitAccesso(),
                            timeHash);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        verificaPermessiLegame(legEl.getTipoAuthority().toString(), utente, cattura);
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;


                            // Inizio manutenzione riportata da Indice Su SbnWEB BUG 2982
							//Mantis 1353 se non è in modifica deve effettuare il controllo
                            //impostato per la 1353. Verifici se ho in cancellazione per lo
                            //stesso id una responsabilità = ""
							for (int x = 0; x < VettorelegamiAutoreDaCancellare.size() ; x++) {
								legamiAutoreDaCancellare = VettorelegamiAutoreDaCancellare.get(x);
								if (legEl.getIdArrivo().equals(legamiAutoreDaCancellare.getIdArrivo())){
									if (legEl.getRelatorCode()!=null) {
										if(!legEl.getRelatorCode().equals(legamiAutoreDaCancellare.getRelatorCode())){
											RespMod = true;
										}
									}else{
										if(legamiAutoreDaCancellare.getRelatorCode() != null){
											if(!legamiAutoreDaCancellare.getRelatorCode().equals(legEl.getRelatorCode())){
												RespMod = true;
											}
										}
									}
								}
							}
                            if (RespMod || modifica)  {
                            	//prima faceva solo questo
								validaCreaLegamiAutore(
										titolo.getBID(),
										utente,
										tp_materiale,
										legEl,
										legamiAutoreCancellati,
										cd_natura,
										timeHash);
                            }else {
								validaCreaLegamiAutoreNew(
									titolo.getBID(),
									utente,
									tp_materiale,
									legEl,
									legamiAutoreCancellati,
									cd_natura,
									timeHash);
							}
                            //fine mantis


//                            validaCreaLegamiAutore(
//                                titolo.getBID(),
//                                utente,
//                                tp_materiale,
//                                legEl,
//                                legamiAutoreCancellati,
//                                cd_natura,
//                                timeHash);
                            // Fine manutenzione riportata da Indice Su SbnWEB BUG 2982


                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                            validaCreaLegameDocTitUni(
                                titolo.getBID(),
                                titolo.getCD_NATURA(),
                                legEl,
                                timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                        	//almaviva5_20130520 #5316 aggiunta natura R
                            if (!ValidazioneDati.in(cd_natura, "M", "S", "W", "N", "R"))
                                throw new EccezioneSbnDiagnostico(
                                    3079,
                                    "Legame a classe non consentito per la natura");
                            validaCreaLegameClasse(titolo.getBID(), legEl, timeHash);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                            validaCreaLegameLuogo(titolo.getBID(), legEl, timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                            validaCreaLegameRepertorio(titolo.getBID(), legEl, timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            //Può esserci solo se non è un documento moderno.
                            if (tp_materiale.equals("M"))
                                throw new EccezioneSbnDiagnostico(3234, "Documento moderno");
                            if (!(cd_natura.equals("M") || cd_natura.equals("W")))
                                throw new EccezioneSbnDiagnostico(
                                    3234,
                                    "Legame a marca non consentito per la natura");
                            validaCreaLegameMarca(titolo.getBID(), legEl, timeHash);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                        	//almaviva5_20140203
                            //if (tp_materiale.equals("U"))
                            //    throw new EccezioneSbnDiagnostico(3079, "Legame a soggetto non consentito");
                            //almaviva5_20130520 #5316 aggiunta natura R
                            if (!ValidazioneDati.in(cd_natura, "M", "S", "W", "N", "R"))
                                throw new EccezioneSbnDiagnostico(
                                    3079,
                                    "Legame a soggetto non consentito per la natura");
                            validaCreaLegameSoggetto(titolo.getBID(), legEl, timeHash);
	                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
	                    	//almaviva5_20140203
	                        //if (tp_materiale.equals("U"))
	                        //    throw new EccezioneSbnDiagnostico(3079, "Legame a thesauro non consentito");
	                        //almaviva5_20130520 #5316 aggiunta natura R
	                        if (!ValidazioneDati.in(cd_natura, "M", "S", "W", "N", "R"))
	                            throw new EccezioneSbnDiagnostico(
	                                3079,
	                                "Legame a thesauro non consentito per la natura");
	                        validaCreaLegameThesauro(titolo.getBID(), legEl, timeHash);
	                    }
                    }
                } else {
                    if (legame.getLegameDoc() != null)
                        validaModificaLegameDocDoc(utente, legame.getLegameDoc(), titolo, timeHash, modifica);
                    else if (legame.getLegameTitAccesso() != null)
                        validaModificaLegameDocTitAccesso(
                            titolo,
                            legame.getLegameTitAccesso(),
                            timeHash,
                            modifica);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;
                            if (!modifica)
                                legamiAutoreCancellati.add(legEl.getIdArrivo());
                            validaModificaLegamiAutore(utente, titolo, legEl, timeHash, modifica);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                            validaModificaLegameDocTitUni(titolo, legEl, timeHash, modifica);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE)
                            validaModificaLegameClasse(titolo, legEl, timeHash, modifica);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                            validaModificaLegameRepertorio(titolo, legEl, timeHash, modifica);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                            validaModificaLegameLuogo(titolo, legEl, timeHash, modifica);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            //Può esserci solo se è un documento antico o musicale
  							//Luglio 2013 - Segnalazione Pisa da almaviva Modificato per Mantis 1774
                        	// Modifica riportata dal software di Indice
//                            if (!(tp_materiale.equals("E") || !tp_materiale.equals("U")))
      						  if (!(tp_materiale.equals("E") || tp_materiale.equals("U"))) {
      							throw new EccezioneSbnDiagnostico(3223, "Documento non antico");
      						  }
                            validaModificaLegameMarca(titolo, legEl, timeHash, modifica);
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE)
                            validaModificaLegameSoggetto(titolo, legEl, timeHash, modifica);
                          else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE)
                              validaModificaLegameThesauro(titolo, legEl, timeHash, modifica);
                    }
                }
            }
        }
        if (ValidazioneDati.in(cd_natura, "M", "S", "W")) {
	        int num_leg_461 = 0;
            for (int i = 0; i < legami.length; i++) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    //Sbntipooperazione serve, è da ignorare o non c'è?
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null)
                        if (legame.getLegameDoc().getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()) {
                            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE)
                                num_leg_461--;
                            else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE)
                                num_leg_461++;
                        }
                }
            }
			final List<Vl_titolo_tit_b> titoliSup = this.cercaTitoloSuperiore(titolo.getBID());
	        long naturaLegS = Stream.of(titoliSup).filter(filtraNaturaColl("S")).count();
	        long naturaLegM = Stream.of(titoliSup).filter(filtraNaturaColl("M")).count();
            if (naturaLegM > 0) {
                num_leg_461++;
			}
            if (cd_natura.equals("W")) {
	            if (naturaLegM < 1)
                    throw new EccezioneSbnDiagnostico(3081, "Legame al titolo superiore obbligatorio");
            } else {
                if (num_leg_461 > 1) {
	                if (naturaLegS > 1 || naturaLegM > 1)
		                throw new EccezioneSbnDiagnostico(3082, "Legame al titolo superiore esistente");
                }
            }

        }
        if (modificaLegamiAutore)
            controlloNumeroAutoriLegati();
    }

    private Predicate<? super Vl_titolo_tit_b> filtraNaturaColl(final String natura) {
		return new Predicate<Vl_titolo_tit_b>() {
			public boolean test(Vl_titolo_tit_b tt) {
				return natura.equals(tt.getCD_NATURA_COLL());
			}
		};
	}

	/** Valida per creazione, cancellazione o modifica di legami di un titolo di accesso
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void validaLegamiTitAccessoModifica(String utente, TimestampHash timeHash, Tb_titolo titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String cd_natura = titolo.getCD_NATURA();
        String tp_materiale = titolo.getTP_MATERIALE();
        List<String> legamiAutoreCancellati = new ArrayList<String>();
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                boolean modifica = legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE;
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                    if (legame.getLegameDoc() != null)
                        validaCreaLegameDocDoc(
                            legame.getLegameDoc(),
                            titolo.getBID(),
                            titolo.getCD_NATURA(),
                            titolo.getTP_RECORD_UNI(),
                            utente,
                            timeHash);
                    else if (legame.getLegameTitAccesso() != null)
                        validaCreaLegameDocTitAccesso(
                            titolo.getBID(),
                            cd_natura,
                            titolo.getTP_RECORD_UNI(),
                            tp_materiale,
                            legame.getLegameTitAccesso(),
                            timeHash);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;
                            if (cd_natura.equals("B") || cd_natura.equals("T"))
                                validaCreaLegamiAutore(
                                    titolo.getBID(),
                                    utente,
                                    null,
                                    legEl,
                                    legamiAutoreCancellati,
                                    cd_natura,
                                    timeHash);
                            else
                                throw new EccezioneSbnDiagnostico(3079, "Legame non ammesso");

                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
                            if (cd_natura.equals("T"))
                                validaCreaLegameDocTitUni(titolo.getBID(), cd_natura, legEl, timeHash);
                            else
                                throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                            validaCreaLegameLuogo(titolo.getBID(), legEl, timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                    }
                } else {
                    if (legame.getLegameDoc() != null)
                        validaModificaLegameDocDoc(utente, legame.getLegameDoc(), titolo, timeHash, modifica);
                    else if (legame.getLegameTitAccesso() != null)
                        validaModificaLegameDocTitAccesso(
                            titolo,
                            legame.getLegameTitAccesso(),
                            timeHash,
                            modifica);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;
                            if (!modifica)
                                legamiAutoreCancellati.add(legEl.getIdArrivo());
                            validaModificaLegamiAutore(utente, titolo, legEl, timeHash, modifica);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
                            if (cd_natura.equals("T"))
// Inizio modifica almaviva2 10.11.2009 - per segnalazione interna - non si riesce ad allineare i legami T --> UM
// Errore nella chiamata alla classe di controllo
                            	validaModificaLegameDocTitUni(titolo, legEl, timeHash, modifica);
//                                validaCreaLegameDocTitUni(titolo.getBID(), cd_natura, legEl, timeHash);
//                              Fine modifica almaviva2 10.11.2009 - per segnalazione interna - non si riesce ad allineare i legami T --> UM
                            else
                                throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                            validaCreaLegameLuogo(titolo.getBID(), legEl, timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE)
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");

                    }
                }
            }
            if (modificaLegamiAutore)
                controlloNumeroAutoriLegati();
        }
        if (cd_natura.equals("M") || cd_natura.equals("S") || cd_natura.equals("W")) {
            int num_leg_461 = 0;
            for (int i = 0; i < legami.length; i++) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    //Sbntipooperazione serve, è da ignorare o non c'è?
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null)
                        if (legame.getLegameDoc().getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()) {
                            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE)
                                num_leg_461--;
                            else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE)
                                num_leg_461++;
                        }
                }
            }
            if (cercaMonografiaSuperiore(titolo.getBID()) != null)
                num_leg_461++;
            if (cd_natura.equals("W")) {
                if (num_leg_461 != 1)
                    throw new EccezioneSbnDiagnostico(3081, "Legame al titolo superiore obbligatorio");
            } else {
                if (num_leg_461 > 1)
                    throw new EccezioneSbnDiagnostico(3082, "Legame al titolo superiore esistente");
            }

        }
    }

    /** Valida per creazione, cancellazione o modifica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public void validaLegamiTitoloUniformeModifica(String utente, TimestampHash timeHash, Tb_titolo titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String cd_natura = titolo.getCD_NATURA();
        String tp_materiale = titolo.getTP_MATERIALE();
        List<String> legamiAutoreCancellati = new ArrayList<String>();
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                boolean modifica = legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE;
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                    if (legame.getLegameDoc() != null)
                        throw new EccezioneSbnDiagnostico(3031, "Legame non consentito");
                    else if (legame.getLegameTitAccesso() != null)
                        validaCreaLegameDocTitAccesso(
                            titolo.getBID(),
                            cd_natura,
                            titolo.getTP_RECORD_UNI(),
                            tp_materiale,
                            legame.getLegameTitAccesso(),
                            timeHash);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;
                            validaCreaLegamiAutore(
                                titolo.getBID(),
                                utente,
                                tp_materiale,
                                legEl,
                                legamiAutoreCancellati,
                                cd_natura,
                                timeHash);
                       } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                            validaCreaLegameDocTitUni(
                                titolo.getBID(),
                                titolo.getCD_NATURA(),
                                legEl,
                                timeHash);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                            validaCreaLegameRepertorio(titolo.getBID(), legEl, timeHash);
                    }
                } else {
                    if (legame.getLegameDoc() != null)
                        validaModificaLegameDocDoc(utente, legame.getLegameDoc(), titolo, timeHash, modifica);
                    else if (legame.getLegameTitAccesso() != null)
                        validaModificaLegameDocTitAccesso(
                            titolo,
                            legame.getLegameTitAccesso(),
                            timeHash,
                            modifica);
                    else if (legame.getLegameElementoAut() != null) {
                        LegameElementoAutType legEl = legame.getLegameElementoAut();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                            modificaLegamiAutore = true;
                            if (!modifica)
                                legamiAutoreCancellati.add(legEl.getIdArrivo());
                            validaModificaLegamiAutore(utente, titolo, legEl, timeHash, modifica);
                        } else if (
                            legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                                || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                            validaModificaLegameDocTitUni(titolo, legEl, timeHash, modifica);
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                            validaModificaLegameRepertorio(titolo, legEl, timeHash, modifica);
                    }
                }
            }
        }
        if (modificaLegamiAutore)
            controlloNumeroAutoriLegati();
    }

    public void validaModificaLegameDocTitAccesso(
        Tb_titolo titolo,
        LegameTitAccessoType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo collegato non esistente");
        //String tp_legame =
        //    estraiTipoLegame(titolo.getCD_NATURA(), tit2.getCD_NATURA(), leg.getTipoLegame().toString());
        Tr_tit_tit tt = estraiLegame(titolo.getBID(), leg.getIdArrivo(), null);
        if (tt == null)
            throw new EccezioneSbnDiagnostico(3093, "Legame con titolo di accesso non esistente");
        timeHash.putTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL(), new SbnDatavar( tt.getTS_VAR()));
        if (!(titolo.getTP_MATERIALE().equals("U") && tit2.getCD_NATURA().equals("D")))
            if (leg.getSottoTipoLegame() != null)
                throw new EccezioneSbnDiagnostico(3241, "Sottotipolegame non richiesto");
        if (modifica) {
            //tipo deve essere non so.
            if (leg.getTipoLegame().getType() == SbnLegameTitAccesso.valueOf("454").getType());
            if (leg.getTipoLegame().getType() == SbnLegameTitAccesso.valueOf("510").getType());
            if (leg.getTipoLegame().getType() == SbnLegameTitAccesso.valueOf("517").getType());
            timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));
        }
    }

    public void validaModificaLegameDocTitUni(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo collegato non esistente");
        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());
        //String tp_legame =
        //    estraiTipoLegame(titolo.getCD_NATURA(), tit2.getCD_NATURA(), leg.getTipoLegame().toString());
        // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
        // con tipo legame 431 (A08V)
        //if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("500").getType())
        if (!leg.getTipoLegame().equals(SbnLegameAut.valueOf("500")) &&
				!leg.getTipoLegame().equals(SbnLegameAut.valueOf("431")) &&
				!leg.getTipoLegame().equals(SbnLegameAut.valueOf("531e")) &&
				!leg.getTipoLegame().equals(SbnLegameAut.valueOf("531f")) &&
				!leg.getTipoLegame().equals(SbnLegameAut.valueOf("531h")) &&
				!leg.getTipoLegame().equals(SbnLegameAut.valueOf("531g")) )
            throw new EccezioneSbnDiagnostico(3031, "Tipo del legame non corretto");
        Tr_tit_tit tt = estraiLegame(titolo.getBID(), leg.getIdArrivo(), null);
        if (tt == null)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo non esistente");
        timeHash.putTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL(), new SbnDatavar( tt.getTS_VAR()));
        if (modifica) {
            timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));
        }
    }

    public void validaModificaLegameDocDoc(
        String utente,
        LegameDocType leg,
        Tb_titolo titolo,
        TimestampHash timeHash,
        boolean modifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo collegato non esistente");

        // Manutenzione almaviva2 23Luglio2009 anche i legami 464 (M464N) sono rovesciati! inserito or su altro tipo legame
        if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("463").getType()
        		|| leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("464").getType()) {
            Tr_tit_tit tt = estraiLegame(leg.getIdArrivo(), titolo.getBID(), null);
            if (tt == null)
                throw new EccezioneSbnDiagnostico(3029, "Legame del titolo non esistente");
            timeHash.putTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL(), new SbnDatavar( tt.getTS_VAR()));
            if (tit2.getCD_NATURA().equals("N"))
                if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S')
                    throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");

            //Verifico che non esista già un legame 01 per il titolo
            TableDao t = cercaLegamiDocumento(leg.getIdArrivo(), null);
            List<Vl_titolo_tit_b> v = t.getElencoRisultati();

            for (int i = 0; i < v.size(); i++) {
                Vl_titolo_tit_b ttb = v.get(i);
                if (!ttb.getBID().equals(titolo.getBID())
                    && ttb.getTP_LEGAME().equals("01")
                    && ttb.getCD_NATURA_COLL().equals("M")) {
                    throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
                }
            }
        } else {
            //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());
            //String tp_legame =
            //    estraiTipoLegame(titolo.getCD_NATURA(), tit2.getCD_NATURA(), leg.getTipoLegame().toString());
            //Controllo tipi legame e cd natura
            Tr_tit_tit tt = estraiLegame(titolo.getBID(), leg.getIdArrivo(), null);
            if (tt == null)
                throw new EccezioneSbnDiagnostico(3029, "Legame del titolo non esistente");
            timeHash.putTimestamp("Tr_tit_tit", tt.getBID_BASE() + tt.getBID_COLL(), new SbnDatavar( tt.getTS_VAR()));
            if (tit2.getCD_NATURA().equals("N"))
                if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S')
                    throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");

            if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()) {
                //Verifico che non esista già un legame 01 per il titolo
                TableDao t = cercaLegamiDocumento(titolo.getBID(), null);
                List<Vl_titolo_tit_b> v = t.getElencoRisultati();
                // almaviva bug per gestione legame multiplo di tipo M
                // 28/02/12 inserito contatore per la verifica dei legami di tipo M trovati
                int num_tipo_m =0;
                for (int i = 0; i < v.size(); i++) {
                    Vl_titolo_tit_b ttb = v.get(i);
                    if (!ttb.getBID().equals(leg.getIdArrivo())
    						&& ttb.getTP_LEGAME().equals("01")
    						&& ttb.getCD_NATURA_COLL().equals("M")) {
                        num_tipo_m ++;
                    }
                    // almaviva CONTROLLO gli effettivi legami trovati
                    	if(num_tipo_m >1){
                    		throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
                    }
                }
            }
        }
        if (modifica) {
            timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));
        }
    }

    public void validaModificaLegamiAutore(
        String user,
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        TitoloAutore titAut = new TitoloAutore();
        String cd_relazione;
        if (leg.getRelatorCode() == null)
            cd_relazione = "   ";
        else
            cd_relazione = Decodificatore.getCd_tabella("Tr_tit_aut", "cd_relazione", leg.getRelatorCode());
        if (cd_relazione == null) {
            //throw new EccezioneSbnDiagnostico(3215, "Relator code inesatto");
            cd_relazione = leg.getRelatorCode();
        }
        String tp_resp = titAut.calcolaTp_responsabilita(leg);

        Tr_tit_aut ta = titAut.estraiTitoloAutore(titolo.getBID(), leg.getIdArrivo(), cd_relazione, tp_resp);
        if (ta == null)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo non esistente");
      //bug esercizio 0006391 sbnweb 03/04/2017 almaviva2 ripreso da Indice
        // timeHash.putTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID(), new SbnDatavar( ta.getTS_VAR()));
        timeHash.putTimestamp("Tr_tit_aut", ta.getBID() + ta.getVID() + ta.getCD_RELAZIONE() + ta.getTP_RESPONSABILITA(), new SbnDatavar(ta.getTS_VAR()));
        Autore autore = new Autore();
        Tb_autore aut = autore.estraiAutorePerID(leg.getIdArrivo());
        if (aut == null)
            throw new EccezioneSbnDiagnostico(3022, "Autore non esistente");
        //Questa roba qua non ha senso in cancellazione
        //if (cd_relazione.equals("650"))
        //    if (!aut.getTP_NOME_AUT().equals("E"))
        //        throw new EccezioneSbnDiagnostico(3217, "Relazione ammessa solo con autori di tipo E");
        //Non so se serva il timestamp
        //timeHash.putTimestamp("Tb_autore",autore.getVID(),autore.getTS_VAR());


        // Modificato if inserendo le opportune parentesi a delimitare l'OR fra i tipi di legame
        // con l'AND sul TP_RESPONSABILITA'  almaviva2 28.05.2009
        if (modifica) {
            if (!ta.getTP_RESPONSABILITA().equals("1")
                && (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("700").getType()
                || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("710").getType()))
                tipo_autore_1++;
            else if (!ta.getTP_RESPONSABILITA().equals("2")
                    && (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("701").getType()
                    || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("711").getType()))
                tipo_autore_2++;
        } else { //CANCELLA
            if (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("700").getType()
                || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("710").getType())
                tipo_autore_1--;
            else if (
                leg.getTipoLegame().getType() == SbnLegameAut.valueOf("701").getType()
                    || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("711").getType())
                tipo_autore_2--;
        }
        timeHash.putTimestamp("Tb_autore", aut.getVID(), new SbnDatavar( aut.getTS_VAR()));
        if (modifica) {
            verificaAutoreSuperfluo(user, leg);
        }
    }

    public void validaModificaLegameSoggetto(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tr_tit_sog_bib ts = new SoggettoTitolo().estraiTitoloSoggetto(titolo.getBID(), leg.getIdArrivo());
        if (ts == null)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con soggetto non esistente");
        timeHash.putTimestamp("Tr_tit_sog_bib", ts.getBID() + ts.getCID(), new SbnDatavar( ts.getTS_VAR()));
        if (modifica) {
            Soggetto soggetto = new Soggetto();
            Tb_soggetto sogg = soggetto.cercaSoggettoPerId(leg.getIdArrivo());
            if (sogg == null)
                throw new EccezioneSbnDiagnostico(3026, "Soggetto non esistente");
            if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("606").getType())
                throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
            timeHash.putTimestamp("Tb_soggetto", sogg.getCID(), new SbnDatavar( sogg.getTS_VAR()));
        }
    }

    public void validaModificaLegameThesauro(
            Tb_titolo titolo,
            LegameElementoAutType leg,
            TimestampHash timeHash,
            boolean modifica)
            throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
    	    Trs_termini_titoli_biblioteche ts = new ThesauroTitolo().estraiTitoloThesauro(titolo.getBID(), leg.getIdArrivo());
            if (ts == null)
                throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con thesauro non esistente");
            timeHash.putTimestamp("Trs_termini_titoli_biblioteche", ts.getBID() + ts.getDID(), new SbnDatavar( ts.getTS_VAR()));
            if (modifica) {
            	TermineThesauro termineThesauro = new TermineThesauro();
            	Tb_termine_thesauro thes = termineThesauro.cercaTerminePerId(leg.getIdArrivo());
                if (thes == null)
                    throw new EccezioneSbnDiagnostico(3026, "Thesauro non esistente");
                if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("606").getType())
                    throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
                timeHash.putTimestamp("Tb_termine_thesauro", thes.getDID(), new SbnDatavar( thes.getTS_VAR()));
            }
        }

    public void validaModificaLegameClasse(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {


        Tr_tit_cla tc = new Tr_tit_cla();
        tc.setBID(titolo.getBID());
        String idArrivo = leg.getIdArrivo();
        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(idArrivo);
         //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	tc.setCD_SISTEMA(sd.getSistema() + edizione);
        	tc.setCD_EDIZIONE(edizione);
        } else {
        	tc.setCD_SISTEMA(sd.getSistema());
            tc.setCD_EDIZIONE("  ");
        }
    	tc.setCLASSE(sd.getSimbolo());

        Tr_tit_claResult tabella = new Tr_tit_claResult(tc);

        tabella.valorizzaElencoRisultati(tabella.selectPerKey(tc.leggiAllParametro()));
        List v = tabella.getElencoRisultati();

        if (v.size() == 0)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con classe non esistente");
        tc = (Tr_tit_cla) v.get(0);
        timeHash.putTimestamp("Tr_tit_cla", tc.getBID() + idArrivo, new SbnDatavar( tc.getTS_VAR()));
        if (modifica) {
            Classe classe = new Classe();
            Tb_classe tb_classe = classe.cercaClassePerID(idArrivo);
            if (tb_classe == null)
                throw new EccezioneSbnDiagnostico(3027, "Classe non esistente");
            if (!ValidazioneDati.in(leg.getTipoLegame().getType(),
            		SbnLegameAut.valueOf("676").getType(),
            		SbnLegameAut.valueOf("686").getType()) )
            	throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
            timeHash.putTimestamp("Tb_classe", idArrivo, new SbnDatavar( tb_classe.getTS_VAR()));
        }
    }

    public void validaModificaLegameRepertorio(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repertorio = new Repertorio();
        Tb_repertorio tb_repertorio = repertorio.cercaRepertorioPerCdSig(leg.getIdArrivo());
        if (tb_repertorio == null)
            throw new EccezioneSbnDiagnostico(3023, "Repertorio non esistente");
        Tr_rep_tit tc =
            new RepertorioTitoloUniforme().estraiLegame(
                titolo.getBID(),
                (int) tb_repertorio.getID_REPERTORIO());
        if (tc == null)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con repertorio non esistente");
        timeHash.putTimestamp("Tr_rep_tit", tc.getBID() + tc.getID_REPERTORIO(), new SbnDatavar( tc.getTS_VAR()));
        if (modifica) {
            if (!(leg.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType()
                || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("815").getType()))
                throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
            timeHash.putTimestamp(
                "Tb_repertorio",
                "" + tb_repertorio.getID_REPERTORIO(),
                new SbnDatavar( tb_repertorio.getTS_VAR()));
        }
    }
    public void validaModificaLegameLuogo(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloLuogo titLuo = new TitoloLuogo();
        Tr_tit_luo tl = titLuo.estraiTitoloLuogo(titolo.getBID(), leg.getIdArrivo());
        if (tl == null)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con luogo non esistente");
        timeHash.putTimestamp("Tr_tit_luo", tl.getBID() + tl.getLID(), new SbnDatavar( tl.getTS_VAR()));
        if (modifica) {
            Luogo luogo = new Luogo();
            Tb_luogo tb_luogo = luogo.cercaLuogoPerID(leg.getIdArrivo());
            if (tb_luogo == null)
                throw new EccezioneSbnDiagnostico(3025, "Luogo non esistente");
            if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("620").getType())
                throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
            timeHash.putTimestamp("Tb_luogo", tb_luogo.getLID(), new SbnDatavar( tb_luogo.getTS_VAR()));
        }
    }
    /** Solo per antico
     * @throws InfrastructureException */
    public void validaModificaLegameMarca(
        Tb_titolo titolo,
        LegameElementoAutType leg,
        TimestampHash timeHash,
        boolean modifica)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {


        Tr_tit_mar ts = new Tr_tit_mar();
        ts.setBID(titolo.getBID());
        ts.setMID(leg.getIdArrivo());
        Tr_tit_marResult tabella = new Tr_tit_marResult(ts);
//        List test = tabella.selectPerKey(ts.leggiAllParametro());
//        tabella.valorizzaElencoRisultati(test);
        tabella.valorizzaElencoRisultati(tabella.selectPerKey(ts.leggiAllParametro()));
        //tabella.selectPerKey(ts.leggiAllParametro());
        List v = tabella.getElencoRisultati();

        if (v.size() == 0)
            throw new EccezioneSbnDiagnostico(3029, "Legame del titolo con marca non esistente");
        ts = (Tr_tit_mar) v.get(0);
        timeHash.putTimestamp("Tr_tit_mar", ts.getBID() + ts.getMID(), new SbnDatavar( ts.getTS_VAR()));
        if (modifica) {
            Marca marca = new Marca();
            Tb_marca tb_marca = marca.estraiMarcaPerID(leg.getIdArrivo());
            if (tb_marca == null)
                throw new EccezioneSbnDiagnostico(3024, "Marca non esistente");
            if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("921").getType())
                throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
            timeHash.putTimestamp("Tb_marca", tb_marca.getMID(), new SbnDatavar( tb_marca.getTS_VAR()));
        }
    }

}
