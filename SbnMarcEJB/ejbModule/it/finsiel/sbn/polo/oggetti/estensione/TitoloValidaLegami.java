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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerObject;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;



/**
 * Classe TitoloValidaLegami.java
 * <p>
 * Gestisce la validazione dei legami relativi ai titoli
 * Accede al db.
 * </p>
 *
 * @author
 * @author
 *
 * @version 21-mar-2003
 */
public class TitoloValidaLegami extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4442646412602008276L;
	LegamiType[] legami = null;
    int tipo_autore_1 = 0;
    int tipo_autore_2 = 0;

    public TitoloValidaLegami(String bid, LegamiType[] legami) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        this.legami = legami;
        TitoloAutore titAut = new TitoloAutore();
        List v = titAut.estraiTitoliAutore(bid);
        for (int i = 0; i < v.size(); i++) {
            Tr_tit_aut ta = (Tr_tit_aut) v.get(i);
            if (ta.getTP_RESPONSABILITA().equals("1"))
                tipo_autore_1++;
            if (ta.getTP_RESPONSABILITA().equals("2"))
                tipo_autore_2++;
        }
    }

    /** Deve validare i legami, per ora lo metto qui. Ma forse serve una
     * validazione per ogni tipo e quindi andrebbe in altra posizione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void validaLegamiDocumentoCrea(
        String user,
        TimestampHash timeHash,
        String bid,
        String cd_natura,
        String tp_record,
        SbnMateriale tipoMateriale,
        boolean cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legame.getLegameDoc() != null)
                    validaCreaLegameDocDoc(legame.getLegameDoc(), bid, cd_natura, tp_record, user, timeHash);
                else if (legame.getLegameTitAccesso() != null)
                    validaCreaLegameDocTitAccesso(
                        bid,
                        cd_natura,
                        tp_record,
                        tipoMateriale.toString(),
                        legame.getLegameTitAccesso(),
                        timeHash);
                else if (legame.getLegameElementoAut() != null) {
                    LegameElementoAutType legEl = legame.getLegameElementoAut();
                    verificaPermessiLegame(legEl.getTipoAuthority().toString(), user, cattura);
                    if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE)
                        validaCreaLegamiAutore(
                            bid,
                            user,
                            tipoMateriale.toString(),
                            legEl,
                            new ArrayList(),
                            cd_natura,
                            timeHash);
                    else if (
                        legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                            || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                        validaCreaLegameDocTitUni(bid, cd_natura, legEl, timeHash);
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                        if (!ValidazioneDati.in(cd_natura, "M", "S", "W", "N", "R"))
                            throw new EccezioneSbnDiagnostico(
                                3079,
                                "Legame a classe non consentito per la natura");
                        validaCreaLegameClasse(bid, legEl, timeHash);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                        validaCreaLegameLuogo(bid, legEl, timeHash);
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                        //Può esserci solo se non è un documento moderno.
                        if (tipoMateriale.getType() == SbnMateriale.valueOf("M").getType())
                            throw new EccezioneSbnDiagnostico(3234, "Documento moderno");
                        if (!(cd_natura.equals("M") || cd_natura.equals("W")))
                            throw new EccezioneSbnDiagnostico(
                                3234,
                                "Legame a marca non consentito per la natura");
                        validaCreaLegameMarca(bid, legEl, timeHash);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                    	//almaviva5_20140203
                        //if (tipoMateriale.getType() == SbnMateriale.valueOf("U").getType())
                        //    throw new EccezioneSbnDiagnostico(3079, "Legame a soggetto non consentito");
                        //almaviva5_20130520 #5316 aggiunta natura R
                        if (!ValidazioneDati.in(cd_natura, "M", "S", "W", "N", "R"))
                            throw new EccezioneSbnDiagnostico(
                                3079,
                                "Legame a soggetto non consentito per la natura");
                        validaCreaLegameSoggetto(bid, legEl, timeHash);
                    }
                }
            }
        }
        if (cd_natura.equals("M") || cd_natura.equals("S") || cd_natura.equals("W")) {
            int num_leg_461 = 0;
            for (int i = 0; i < legami.length; i++) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    //Sbntipooperazione serve, è da ignorare o non c'è?
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null)
                        if (legame.getLegameDoc().getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType())
                            num_leg_461++;
                }
            }
            if (cd_natura.equals("W")) {
                if (num_leg_461 != 1)
                    throw new EccezioneSbnDiagnostico(3081, "Legame al titolo superiore obbligatorio");
            } else {
                if (num_leg_461 > 1)
                    throw new EccezioneSbnDiagnostico(3082, "Esiste già un legame 01 per il titolo");
            }

        }
        controlloNumeroAutoriLegati();
    }
    /** Deve validare i legami, per ora lo metto qui. Ma forse serve una
     * validazione per ogni tipo e quindi andrebbe in altra posizione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void validaLegamiTitAccessoCrea(
        String user,
        String bid,
        TimestampHash timeHash,
        String cd_natura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legame.getLegameDoc() != null)
                    validaCreaLegameDocDoc(legame.getLegameDoc(), bid, cd_natura, null, user, timeHash);
                else if (legame.getLegameTitAccesso() != null)
                    //validaCreaLegameDocTitAccesso(cd_natura, ,legame.getLegameTitAccesso(), timeHash);
                    //Non penso che si possa legare più titAccesso.
                    ;
                else if (legame.getLegameElementoAut() != null) {
                    LegameElementoAutType legEl = legame.getLegameElementoAut();
                    if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                        if (cd_natura.equals("B") || cd_natura.equals("T"))
                            validaCreaLegamiAutore(bid, user, null, legEl, new ArrayList(), cd_natura, timeHash);
                        else
                            throw new EccezioneSbnDiagnostico(3079, "Legame non ammesso");

                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
                        if (cd_natura.equals("T"))
                            validaCreaLegameDocTitUni(bid, cd_natura, legEl, timeHash);
                        else
                            throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE)
                        throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                        validaCreaLegameLuogo(bid, legEl, timeHash);
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE)
                        throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE)
                        throw new EccezioneSbnDiagnostico(3079, "Legame errato");
                }
            }
        }
        controlloNumeroAutoriLegati();
    }

    /** Deve validare i legami, per ora lo metto qui. Ma forse serve una
     * validazione per ogni tipo e quindi andrebbe in altra posizione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaCreaLegamiTitoloUniforme(String user, String bid, String natura, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                //Sbntipooperazione serve, è da ignorare o non c'è?
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legame.getLegameDoc() != null)
                    throw new EccezioneSbnDiagnostico(3031, "Legame non consentito");
                else if (legame.getLegameTitAccesso() != null)
                    validaCreaLegameDocTitAccesso(
                        bid,
                        "A",
                        null,
                        " ",
                        legame.getLegameTitAccesso(),
                        timeHash);
                else if (legame.getLegameElementoAut() != null) {
                    LegameElementoAutType legEl = legame.getLegameElementoAut();
                    if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                        validaCreaLegamiAutore(bid, user, null, legEl, new ArrayList(), "A", timeHash);

                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                        validaCreaLegameRepertorio(bid, legEl, timeHash);
                    else if (
                        legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE
                            || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
                    	// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
                        // validaCreaLegameDocTitUni(bid, "A", legEl, timeHash);
                    	validaCreaLegameDocTitUni(bid, natura, legEl, timeHash);
                    else
                        throw new EccezioneSbnDiagnostico(3031, "Legame non consentito");
                }
            }
        }
        controlloNumeroAutoriLegati();
    }

    /** il sottotipo è ammesso solo se il documento di partenza ha tp_materiale U e
     * il titolo di arrivo ha natura D
     * @throws InfrastructureException
     */
    public void validaCreaLegameDocTitAccesso(
        String bid,
        String cd_natura,
        String tp_record,
        String materiale,
        LegameTitAccessoType leg,
        TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        //Tb_titolo tit2 = validaTitAccesso(leg.getTitAccessoLegato());
        //Da estrarre dal DB: verifico esistenza
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo collegato non esistente");
        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());
        String tp_legame = estraiTipoLegame(cd_natura, tit2.getCD_NATURA(), leg.getTipoLegame().toString());
        Tr_tit_tit tt = estraiLegame(bid, tit2.getBID(), tp_legame);
        if (tt != null)
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");

        if (!(materiale.equals("U") && tit2.getCD_NATURA().equals("D")))
            if (leg.getSottoTipoLegame() != null)
                throw new EccezioneSbnDiagnostico(3241, "Sottotipolegame non richiesto");
        if (leg.getSequenzaMusica() != null && tp_record != null)
            if (!(leg.getTipoLegame().getType() == SbnLegameTitAccesso.valueOf("517").getType() && tp_record.equals("d")))
                throw new EccezioneSbnDiagnostico(3134, "Sequenza musica non prevista");
        timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));

    }

    public void validaCreaLegameDocTitUni(
        String bid,
        String natura,
        LegameElementoAutType leg,
        TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo non esistente");
        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());
        // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
       //26/09/2016 per consentire l'individuazione del legame A431V occorre invertire la natura che arriva
       //come partenza V e arrivo A, mentre sulla tb_codici si trova al contrario
      			String tp_legame = null;
      			if (natura.equals("V")) {
      				tp_legame =
      					estraiTipoLegame(tit2.getCD_NATURA(), natura, leg.getTipoLegame().toString());
      			} else {
      				tp_legame =
      					estraiTipoLegame(natura, tit2.getCD_NATURA(), leg.getTipoLegame().toString());
      			}
//        String tp_legame = estraiTipoLegame(natura, tit2.getCD_NATURA(), leg.getTipoLegame().toString());



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

        Tr_tit_tit tt = estraiLegame(bid, leg.getIdArrivo(), tp_legame);
        if (tt != null)
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo già esistente");
        timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));
    }

    /**
     * Modificato per gestire opportunamente il tipo legame 463 (che è a rovescio)
     * @param leg
     * @param bid
     * @param cd_natura_tit1
     * @param utente
     * @param timeHash
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaCreaLegameDocDoc(
        LegameDocType leg,
        String bid,
        String cd_natura_tit1,
        String tp_record1,
        String utente,
        TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tit2 = estraiTitoloPerID(leg.getIdArrivo());
        if (tit2 == null)
            throw new EccezioneSbnDiagnostico(3028, "Titolo legato non esistente");
        if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("463").getType()) {
            Tr_tit_tit tt = estraiLegame(leg.getIdArrivo(), bid, null);
            if (tt != null)
                throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
            //Verifico che non esista già un legame 01 per il titolo
            TableDao t = cercaLegamiDocumento(leg.getIdArrivo(), null);
            List v = t.getElencoRisultati();

            for (int i = 0; i < v.size(); i++) {
                Vl_titolo_tit_b ttb = (Vl_titolo_tit_b) v.get(i);

                // Inizio Modifica almaviva2 BUG MANTIS 4501 esercizio : modificati i controlli esistente
                // per consentire doppio legame M01S  (vedi commento almaviva4 01/08/2011 su software di Indice
                // e commento su vecchia correzione almaviva4 bug mantis 3335)
//                if (ttb.getTP_LEGAME().equals("01") && !ttb.getCD_NATURA_COLL().equals("C")) {
                if (ttb.getTP_LEGAME().equals("01") && ttb.getCD_NATURA_COLL().equals("M")) {
                	throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
                }
            }
        } else {
            String tp_legame =
                estraiTipoLegame(cd_natura_tit1, tit2.getCD_NATURA(), leg.getTipoLegame().toString());

            Tr_tit_tit tt = estraiLegame(bid, leg.getIdArrivo(), tp_legame);
            if (tt != null)
                throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
            if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()) {
                if (cd_natura_tit1.equals("N") && tp_record1.equals("d") && !tit2.getTP_RECORD_UNI().equals("d")) {
                    throw new EccezioneSbnDiagnostico(3277, "Legame errato, titolo superiore non è un manoscritto");
                }
                //Verifico che non esista già un legame 01 per il titolo
                TableDao t = cercaLegamiDocumento(bid, null);
                List v = t.getElencoRisultati();

                for (int i = 0; i < v.size(); i++) {
                    Vl_titolo_tit_b ttb = (Vl_titolo_tit_b) v.get(i);
                    // Inizio Modifica almaviva2 BUG MANTIS 4501 esercizio : modificati i controlli esistente
                    // per consentire doppio legame M01S  (vedi commento almaviva4 01/08/2011 su software di Indice
                    // e commento su vecchia correzione almaviva4 bug mantis 3335)
//                    if (ttb.getTP_LEGAME().equals("01") && !ttb.getCD_NATURA_COLL().equals("C")) {
// OLD VERSIONE
//                    if (ttb.getTP_LEGAME().equals("01")	&& ttb.getCD_NATURA_COLL().equals("M")) {
//                        throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
//                    }
                    // almaviva BUD LEGAME M01S TEST
					if (ttb.getTP_LEGAME().equals("01")	&& ttb.getCD_NATURA_COLL().equals("M")
							&& tit2.getCD_NATURA().equals("M")) {
						   throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
                    }

                }
            }
            // Inizio Intervento interno per Raccolte Fattizie 20.09.2011 almaviva2
            // non è possibile avere più legami M01R (una monografia può appartenere ad una sola raccolta)
            if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("410").getType()) {
                if (cd_natura_tit1.equals("M") && tit2.getCD_NATURA().equals("R")) {
                    //Verifico che non esista già un legame 01 per il titolo
                	TableDao tRaccolte = cercaLegamiDocumento(bid, null);
                	List vRaccolte = tRaccolte.getElencoRisultati();

	                for (int i = 0; i < vRaccolte.size(); i++) {
	                    Vl_titolo_tit_b ttbRaccolte = (Vl_titolo_tit_b) vRaccolte.get(i);
	                    if (ttbRaccolte.getTP_LEGAME().equals("01")	&& ttbRaccolte.getCD_NATURA_COLL().equals("R")) {
	                        throw new EccezioneSbnDiagnostico(3239, "Legame 01 già presente");
	                    }
	                }
	            }
            }
            // Fine Intervento interno per Raccolte Fattizie 20.09.2011 almaviva2



        }
        if (tit2.getCD_NATURA().equals("N"))
            if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S')
                throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");
        if (leg.getSici() != null)
            if (!(cd_natura_tit1.equals("N") && tit2.getCD_NATURA().equals("S") && estraiTipoLegame(cd_natura_tit1, tit2.getCD_NATURA(), leg.getTipoLegame().toString())
                .equals("01")))
                throw new EccezioneSbnDiagnostico(3132, "Sici non ammesso");
        if (leg.getSequenza() != null)
            if (!(leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("410").getType()
                || leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()
                || leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("422").getType()))
                throw new EccezioneSbnDiagnostico(3133, "Sequenza non ammessa");
        timeHash.putTimestamp("Tb_titolo", tit2.getBID(), new SbnDatavar( tit2.getTS_VAR()));
    }

    protected void controlloNumeroAutoriLegati() throws EccezioneSbnDiagnostico {
        if (tipo_autore_1 > 1)
            throw new EccezioneSbnDiagnostico(3045, "tipo legame autore");
        if (tipo_autore_2 > 2 || (tipo_autore_2 > 0 && tipo_autore_1 == 0))
            throw new EccezioneSbnDiagnostico(3045, "tipo legame autore");
    }

    public void validaCreaLegamiAutore(
        String bid,
        String user,
        String tp_materiale,
        LegameElementoAutType leg,
        List legamiCancellati,
        String cd_natura,
        TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Autore autore = new Autore();

        Tb_autore aut = autore.estraiAutorePerID(leg.getIdArrivo());
        if (aut == null)
            throw new EccezioneSbnDiagnostico(3022, "Autore non esistente");
        //Non so se serva il timestamp
        //timeHash.putTimestamp("Tb_autore",autore.getVID(),autore.getTS_VAR());
        if (!"A".equals(aut.getTP_FORMA_AUT()))
            throw new EccezioneSbnDiagnostico(3291, "Forma autore non corretta");
        String cd_relazione = null;
        if (leg.getRelatorCode() == null) {
//        	 Inizio Il controllo viene asteriscato per far passare una marea di allineamenti che vengono bloccati
//            if (tp_materiale != null && (tp_materiale.equals("U") || tp_materiale.equals("G")))
//                throw new EccezioneSbnDiagnostico(3218, "Relator code obbligatorio");
//          Fine Il controllo viene asteriscato per far passare una marea di allineamenti che vengono bloccati
            cd_relazione = "   ";
        } else
            cd_relazione = Decodificatore.getCd_tabella("Tr_tit_aut", "cd_relazione", leg.getRelatorCode());
        if (cd_relazione == null)
            throw new EccezioneSbnDiagnostico(3215, "Relator code inesatto");
        if (cd_relazione.equals("650"))
            if (!aut.getTP_NOME_AUT().equals("E"))
                throw new EccezioneSbnDiagnostico(3217, "Relazione ammessa solo con autori di tipo E");
        if (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("700").getType()
            || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("710").getType()) {
            tipo_autore_1++;
        } else if (
            leg.getTipoLegame().getType() == SbnLegameAut.valueOf("701").getType()
                || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("711").getType()) {
            tipo_autore_2++;
        }
        if (cd_natura.equals("S") && SbnRespons.valueOf("1").getType() == leg.getTipoRespons().getType()) {
            if (!aut.getTP_NOME_AUT().equals("E") && !aut.getTP_NOME_AUT().equals("R") && !aut.getTP_NOME_AUT().equals("G"))
                throw new EccezioneSbnDiagnostico(3292, "L'autore deve essere di tipo ente");
        }
        timeHash.putTimestamp("Tb_autore", aut.getVID(), new SbnDatavar( aut.getTS_VAR()));
        TitoloAutore titAut = new TitoloAutore();
        String tp_resp = titAut.calcolaTp_responsabilita(leg);
        if (!legamiCancellati.contains(leg.getIdArrivo())
            && titAut.estraiTitoloAutore(bid, leg.getIdArrivo(), cd_relazione, tp_resp) != null)
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");

        if (cd_natura.equals("C") && !(tp_resp.equals("3") || tp_resp.equals("4"))) {
            throw new EccezioneSbnDiagnostico(3237, "Per le collane ammessa solo responsabilità 3");
        }
        verificaAutoreSuperfluo(user, leg);
    }



    //  Inizio manutenzione riportata da Indice Su SbnWEB BUG 2982
    //MANTIS 1353 Aggiiunto metodo che gestisca i controlli necessari per la 1353
    //questi controlli non devono essere fatti in fase di modifica del legame ma
    //solo per la creazione
	public void validaCreaLegamiAutoreNew(
		String bid,
		String user,
		String tp_materiale,
		LegameElementoAutType leg,
		List legamiCancellati,
		String cd_natura,
		TimestampHash timeHash)
//		throws EccezioneSbnDiagnostico, EccezioneDB {
	throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException, InvocationTargetException, Exception{
        Autore autore = new Autore();

		Tb_autore aut = autore.estraiAutorePerID(leg.getIdArrivo());
		if (aut == null)
			throw new EccezioneSbnDiagnostico(3022, "Autore non esistente");
		//Non so se serva il timestamp
		//timeHash.putTimestamp("Tb_autore",autore.getVid(),autore.getTs_var());
		if (!"A".equals(aut.getTP_FORMA_AUT()))
			throw new EccezioneSbnDiagnostico(
				3291,
				"Forma autore non corretta");
		String cd_relazione = null;
		if (leg.getRelatorCode() == null) {
			// Inizio Il controllo viene asteriscato per far passare una marea di allineamenti che vengono bloccati
//			if (tp_materiale != null && (tp_materiale.equals("U") || tp_materiale.equals("G")))
//				throw new EccezioneSbnDiagnostico(3218,	"Relator code obbligatorio");
			// Fine Il controllo viene asteriscato per far passare una marea di allineamenti che vengono bloccati
			cd_relazione = "   ";
		} else
			cd_relazione =
				Decodificatore.getCd_tabella(
					"Tr_tit_aut",
					"cd_relazione",
					leg.getRelatorCode());
		if (cd_relazione == null)
			throw new EccezioneSbnDiagnostico(3215, "Relator code inesatto");
		if (cd_relazione.equals("650"))
			// Manutenzione Evolutiva richiesta su Mantis BUG 6606 - almaviva2 luglio2018
			// il legame di tipo 650 è ammesso anche per autori di tipo G
			// if (!aut.getTP_NOME_AUT().equals("E"))
			if (!aut.getTP_NOME_AUT().equals("E") && !aut.getTP_NOME_AUT().equals("G"))
				throw new EccezioneSbnDiagnostico(
					3217,
					"Relazione ammessa solo con autori di tipo E o G");
		if (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("700").getType()
			|| leg.getTipoLegame().getType() == SbnLegameAut.valueOf("710").getType()) {
			tipo_autore_1++;
		} else if (
			leg.getTipoLegame().getType() == SbnLegameAut.valueOf("701").getType()
				|| leg.getTipoLegame().getType() == SbnLegameAut.valueOf("711").getType()) {
			tipo_autore_2++;
		}
		if (cd_natura.equals("S")
			&& SbnRespons.valueOf("1").getType() == leg.getTipoRespons().getType()) {
			if (!aut.getTP_NOME_AUT().equals("E")
				&& !aut.getTP_NOME_AUT().equals("R")
				&& !aut.getTP_NOME_AUT().equals("G"))
				throw new EccezioneSbnDiagnostico(
					3292,
					"L'autore deve essere di tipo ente");
		}
		timeHash.putTimestamp("Tb_autore", aut.getVID(), new SbnDatavar( aut.getTS_VAR()));
		TitoloAutore titAut = new TitoloAutore();
		String tp_resp = titAut.calcolaTp_responsabilita(leg);
		if (!legamiCancellati.contains(leg.getIdArrivo())
			&& titAut.estraiTitoloAutore(
				bid,
				leg.getIdArrivo(),
				cd_relazione,
				tp_resp)
				!= null)
			throw new EccezioneSbnDiagnostico(
				3030,
				"Legame del titolo esistente");
		//MANTIS 1353
		if (cd_relazione.equals("   ")){
			if (titAut.estraiTitoloAutore(bid, leg.getIdArrivo(), tp_resp) != null){
				throw new EccezioneSbnDiagnostico(
								3031,
								"Errore: tipo legame non valido");
			}
		}
		if (!cd_relazione.equals("   ")&& titAut.estraiTitoloAutore(
			bid,
			leg.getIdArrivo(),
			"   ",
			tp_resp)
			!= null) {
				throw new EccezioneSbnDiagnostico(
				7016,
				"Errore: Legame non valido. Esiste già lo stesso legame con campo relazione non impostato. Modificare quello esistente.");
			}
		//fine 1353


		if (cd_natura.equals("C")
			&& !(tp_resp.equals("3") || tp_resp.equals("4"))) {
			throw new EccezioneSbnDiagnostico(
				3237,
				"Per le collane ammessa solo responsabilità 3");
		}
		verificaAutoreSuperfluo(user, leg);
	}
//	 Fine manutenzione riportata da Indice Su SbnWEB BUG 2982







    public void verificaAutoreSuperfluo(String user, LegameElementoAutType leg)
        throws EccezioneSbnDiagnostico {
        ValidatorContainerObject valc = ValidatorProfiler.getInstance().getAbilitazioni(user);
        List v = valc.getElenco_parametri();
        Tbf_parametro par;
        if (leg.getSuperfluo() != null && leg.getSuperfluo().getType() == SbnIndicatore.S_TYPE)
            for (int i = 0; i < v.size(); i++) {
                //[TODO devono essere tutti S ?.
                par = (Tbf_parametro) v.get(i);
                if (par.getFl_aut_superflui()!='S')
                    throw new EccezioneSbnDiagnostico(
                        1111,
                        "Impossibile per l'utente creare un autore superfluo");
            }
    }

    public void validaCreaLegameSoggetto(String bid, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Soggetto soggetto = new Soggetto();
        Tb_soggetto sogg = soggetto.cercaSoggettoPerId(leg.getIdArrivo());
        if (sogg == null)
            throw new EccezioneSbnDiagnostico(3026, "Soggetto non esistente");
        if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("606").getType())
            throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
        timeHash.putTimestamp("Tb_soggetto", sogg.getCID(), new SbnDatavar( sogg.getTS_VAR()));
// MODIFICA SU CONTROLLO LEGAMI almaviva
// IL CONTROLLO SULL'ESISTENZA DI UN LEGAME A SOGGETTO VIENE DEMANDATO ALL CREAZIONE IN QUANTO OPERA SU LA TABELLA
// VL_TIT_SOG_BIB CONTROLLANDO ANCHE IL POLO E LA BIBLIOTECA
//        if (new SoggettoTitolo().estraiTitoloSoggetto(bid, leg.getIdArrivo()) != null)
//            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
    }

    public void validaCreaLegameThesauro(String bid, LegameElementoAutType leg, TimestampHash timeHash)
    throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
    	TermineThesauro termineThesauro = new TermineThesauro();

	    //Soggetto soggetto = new Soggetto();
    	Tb_termine_thesauro thes = termineThesauro.cercaTerminePerId(leg.getIdArrivo());
	    //Tb_soggetto sogg = soggetto.cercaSoggettoPerId(leg.getIdArrivo());
	    if (thes == null)
	        throw new EccezioneSbnDiagnostico(3026, "Thesauro non esistente");
	    if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("606").getType())
	        throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
	    timeHash.putTimestamp("Tb_termine_thesauro", thes.getDID(), new SbnDatavar( thes.getTS_VAR()));
		//MODIFICA SU CONTROLLO LEGAMI almaviva
		//IL CONTROLLO SULL'ESISTENZA DI UN LEGAME A SOGGETTO VIENE DEMANDATO ALL CREAZIONE IN QUANTO OPERA SU LA TABELLA
		//VL_TIT_SOG_BIB CONTROLLANDO ANCHE IL POLO E LA BIBLIOTECA
		//    if (new SoggettoTitolo().estraiTitoloSoggetto(bid, leg.getIdArrivo()) != null)
		//        throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
	}

    public void validaCreaLegameClasse(String bid, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Classe classe = new Classe();
        Tb_classe tb_classe = classe.cercaClassePerID(leg.getIdArrivo());
        if (tb_classe == null)
            throw new EccezioneSbnDiagnostico(3027, "Classe non esistente");
        if (!(leg.getTipoLegame().getType() == SbnLegameAut.valueOf("676").getType()) &&
        	!(leg.getTipoLegame().getType() == SbnLegameAut.valueOf("686").getType()) )
            throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
        timeHash.putTimestamp("Tb_classe", leg.getIdArrivo(), new SbnDatavar( tb_classe.getTS_VAR()));
        if (new TitoloClasse().estraiTitoloClasse(bid, leg.getIdArrivo()) != null)
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
    }

    public void validaCreaLegameRepertorio(String bid, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repertorio = new Repertorio();
        Tb_repertorio tb_repertorio = repertorio.cercaRepertorioPerCdSig(leg.getIdArrivo());
        if (tb_repertorio == null)
            throw new EccezioneSbnDiagnostico(3023, "Repertorio non esistente");
        if (!(leg.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType()
            || leg.getTipoLegame().getType() == SbnLegameAut.valueOf("815").getType()))
            throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
        //Non serve, perchè se esiste viene fatto l'update
        //if (new RepertorioTitoloUniforme().estraiLegame(bid, leg.getIdArrivo()) != null)
        //    throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
        timeHash.putTimestamp(
            "Tb_repertorio",
            "" + tb_repertorio.getID_REPERTORIO(),
            new SbnDatavar( tb_repertorio.getTS_VAR()));
    }

    public void validaCreaLegameLuogo(String bid, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Luogo luogo = new Luogo();
        Tb_luogo tb_luogo = luogo.cercaLuogoPerID(leg.getIdArrivo());
        if (tb_luogo == null)
            throw new EccezioneSbnDiagnostico(3025, "Luogo non esistente");
        if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("620").getType())
            throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
        timeHash.putTimestamp("Tb_luogo", tb_luogo.getLID(), new SbnDatavar( tb_luogo.getTS_VAR()));
        if (new TitoloLuogo().estraiTitoloLuogo(bid, leg.getIdArrivo()) != null) {
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
        }
        // 12.07.2005 gestione codice relazione in tr_tit_luo.tp_luogo
        String cd_relazione = null;
        if (leg.getRelatorCode() == null) {
            cd_relazione = "P";
        } else
            cd_relazione = Decodificatore.getCd_tabella("Tr_tit_luo", "tp_luogo", leg.getRelatorCode());
        if (cd_relazione == null)
            throw new EccezioneSbnDiagnostico(3215, "Relator code inesatto");
        //verifico che non esista già ul legame a luogo con tipo 'P'
        if (cd_relazione.equalsIgnoreCase("P")) {
	        if (new TitoloLuogo().estraiTitoliLuogoTipo(bid,cd_relazione).size() > 0) {
	            throw new EccezioneSbnDiagnostico(3101, "Ammesso un solo legame");
	        }
	    }
    }

    /** Solo per antico e musica
     * @throws InfrastructureException */
    public void validaCreaLegameMarca(String bid, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Marca marca = new Marca();
        Tb_marca tb_marca = marca.estraiMarcaPerID(leg.getIdArrivo());
        if (tb_marca == null)
            throw new EccezioneSbnDiagnostico(3024, "Marca non esistente");
        if (leg.getTipoLegame().getType() != SbnLegameAut.valueOf("921").getType())
            throw new EccezioneSbnDiagnostico(3031, "tipo legame errato");
        timeHash.putTimestamp("Tb_marca", tb_marca.getMID(), new SbnDatavar( tb_marca.getTS_VAR()));
        if (new TitoloMarca().estraiTitoloMarca(leg.getIdArrivo(), bid) != null)
            throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");
    }

    /** Estrae da DB un legame tra due titoli.
     * @throws InfrastructureException */
    public Tr_tit_tit estraiLegame(String bid_1, String bid_2, String tp_legame) throws EccezioneDB, InfrastructureException {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(bid_1);
        tt.setBID_COLL(bid_2);
        tt.setTP_LEGAME(tp_legame);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        //tabella.valorizzaElencoRisultati(tabella.selectPerKey(this.leggiAllParametro()));
        List test = tabella.selectPerKey(tt.leggiAllParametro());
        tabella.valorizzaElencoRisultati(test);
        List v = tabella.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_tit_tit) v.get(0);
        return null;
    }

    /** Cerca il tipo di legame in base alla natura e al tipoLegame(da Xml).
     * Genera eccezione se il legame non esiste */
    public String estraiTipoLegame(String natura1, String natura2, String tipoLegame)
        throws EccezioneSbnDiagnostico {
        //Aggiungo le nature dei titoli
        tipoLegame = natura1 + tipoLegame + natura2;
        //Passo dal decodificatore
        String tipoLegameR = Decodificatore.getCd_tabella("LICR", tipoLegame);
        if (tipoLegameR == null) {
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3031, "Tipo del legame non esistente");
            ecc.appendMessaggio(":" + tipoLegame);
            throw ecc;
        }
        return tipoLegameR.substring(1, tipoLegameR.length() - 1);
    }

    public void verificaPermessiLegame(String tp_authority, String cd_utente, boolean cattura)
        throws EccezioneSbnDiagnostico {
    	if(!cattura){
	        Tbf_par_auth par =
	        	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(cd_utente, tp_authority);
	        if (par == null || par.getFl_abil_legame()=='N')
	            throw new EccezioneSbnDiagnostico(3246, "utente non abilitato alla gestione del legame");
    	}

    }
}
