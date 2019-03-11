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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C105;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloAccessoValida
 * <p>
 * Validazione per creazione, modifica e cancellazione di TitoloAccesso
 * </p>
 * @author
 * @author
 *
 * @version 27-mag-03
 */
public class TitoloAccessoValida extends TitoloValida {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6821916119296203571L;
	TitAccessoType titAcc = null;
    TitoloValidaLegami validaLegami;
    TitoloValidaLegamiModifica validaLegamiModifica;

    public TitoloAccessoValida(CreaType datiType) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        tipoControllo = datiType.getTipoControllo();
        documentoType = datiType.getCreaTypeChoice().getDocumento();
        LegamiType[] legami = null;
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            statoRecord = documentoType.getStatoRecord();
            titAcc = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
            if (titAcc != null) {
                id = titAcc.getT001();
                TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
                if(choice != null){
                    if (choice.getT423() != null) {
                        c200 = choice.getT423().getT200();
                    }
                    if (choice.getT454() != null)
                        c200 = choice.getT454();
                    if (choice.getT510() != null)
                        c200 = choice.getT510();
                    if (choice.getT517() != null)
                        c200 = choice.getT517();
                }
            }
        }

        validaLegami = new TitoloValidaLegami(id, legami);
    }

    public TitoloAccessoValida(ModificaType datiType) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        tipoControllo = datiType.getTipoControllo();
        documentoType = datiType.getDocumento();
        LegamiType[] legami = null;
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            statoRecord = documentoType.getStatoRecord();
            titAcc = documentoType.getDocumentoTypeChoice().getDatiTitAccesso();
            if (titAcc != null) {
                id = titAcc.getT001();
                TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
                if(choice != null){
                    if (choice.getT423() != null) {
                        c200 = choice.getT423().getT200();
                    }
                 // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
//                    if (choice.getT454() != null)
//                        c200 = choice.getT454();
                    if (choice.getT454A() != null)
                        c200 = choice.getT454A().getT200();


                    if (choice.getT510() != null)
                        c200 = choice.getT510();
                    if (choice.getT517() != null)
                        c200 = choice.getT517();
                }
            }
        }
        validaLegamiModifica = new TitoloValidaLegamiModifica(id, legami);
    }

    /**
     * metodo di validazione per operazione di creazione autore:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @param titolo: viene settato il campo cd_natura
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean validaPerCrea(String utente, Tb_titolo titolo, boolean ignoraId, TimestampHash timeHash,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        if (!ignoraId && tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE) {
            tb_titolo = verificaEsistenzaID(id);
            //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
            if (tb_titolo != null) {
                //ritorno un diagnostico
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Id di titolo già esistente nel DB");
            }
        }

        String cd_natura = estraiNatura(titAcc);
        if (c200 != null && c200.getA_200Count() > 0) {
            List listaTitoli;
            if (tipoControllo == null || tipoControllo.getType() != SbnSimile.CONFERMA_TYPE) {
                listaTitoli = cercaTitoliSimili( null, cd_natura, 0);
                if (listaTitoli.size() != 0) {
                    //se lista è vuota significa che non esistono autori simili
                    elencoDiagnostico = listaTitoli;
                    return false;
                }
            }else {
                listaTitoli = cercaTitoliSimiliConferma(null, null, cd_natura);
                if (listaTitoli.size() != 0) {
                    //se lista è vuota significa che non esistono autori simili
                    elencoDiagnostico = listaTitoli;
                    return false;
                }
            }
        }
        String materiale = null;

        if(titAcc.getTitAccessoTypeChoice() != null){
            TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
            if (choice.getT423() != null) {
                C101 a101 = choice.getT423().getT101();
                if (a101.getA_101Count() == 0)
                    throw new EccezioneSbnDiagnostico(3195, "Almeno una lingua è obbligatoria");
                for (int i = 0; i < a101.getA_101Count(); i++)
                    if (Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", a101.getA_101(i).toUpperCase())
                        == null)
                        throw new EccezioneSbnDiagnostico(3204, "Codice lingua errato");
                C105 c105 = choice.getT423().getT105();
                if (c105 != null) {
                    if (choice.getT423().getTipoMateriale() == null) {
                        //Di default metto moderno.
                        choice.getT423().setTipoMateriale(SbnMateriale.VALUE_0);
                    } else {
                        materiale = choice.getT423().getTipoMateriale().toString();
                    }
                    String[] generi = c105.getA_105_4();
                    for (int i = 0; i < generi.length; i++)
                        if (Decodificatore
                            .getCd_tabella(
                                "Tb_titolo",
                                "cd_genere_1",
                                generi[i],
                                choice.getT423().getTipoMateriale().toString())
                            == null)
                            throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
                }
            }

         // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
            if (choice.getT454A() != null) {
              	C101 a101 = choice.getT454A().getT101();
               	if (a101 == null || a101.getA_101Count() == 0)
               		throw new EccezioneSbnDiagnostico(3195, "Almeno una lingua è obbligatoria");
                  	for (int i = 0; i < a101.getA_101Count(); i++)
                  		if (Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", a101.getA_101(i).toUpperCase()) == null)
                    throw new EccezioneSbnDiagnostico(3204, "Codice lingua errrato");
            }
        }
        titolo.setCD_NATURA(cd_natura);
        if (materiale == null)
            materiale = "M";
        verificaLivelloCreazione(utente,materiale,cd_natura, titAcc.getLivelloAut().toString(),_cattura);
        //Ora valida i legami.
        validaLegami.validaLegamiTitAccessoCrea(utente, titolo.getBID(), timeHash, cd_natura);
        return true;
    }
    /**
     * metodo di validazione per operazione di modifica:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @param titolo: viene settato il campo cd_natura
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_titolo validaPerModifica(String utente, TimestampHash timeHash,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        tb_titolo = estraiTitoloPerID(id);
        //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
        if (tb_titolo == null) {
            //ritorno un diagnostico
            throw new EccezioneSbnDiagnostico(3013, "Id di titolo non esistente nel DB");
        }
        verificaAllineamentoModificaTitolo(tb_titolo.getBID());
        String cd_natura = estraiNatura(titAcc);
        if (c200 != null && c200.getA_200Count() > 0) {
            ChiaviTitolo chiavi = new ChiaviTitolo();
            chiavi.estraiChiavi(c200);
            String aa_pubb = null;
            if (datiDoc != null && datiDoc.getT100() != null)
                aa_pubb = datiDoc.getT100().getA_100_9();
            SbnData data = new SbnData(tb_titolo.getAA_PUBB_1());
            if (!(chiavi.getKy_cles1_t().trim().equals(tb_titolo.getKY_CLES1_T().trim())
                && chiavi.getKy_cles2_t().trim().equals(tb_titolo.getKY_CLES2_T().trim())
                && chiavi.getKy_clet1_t().trim().equals(tb_titolo.getKY_CLET1_T().trim())
                && chiavi.getKy_clet2_t().trim().equals(tb_titolo.getKY_CLET2_T().trim())
                && ((aa_pubb == null && tb_titolo.getAA_PUBB_1() == null)
                    || (aa_pubb != null
                        && tb_titolo.getAA_PUBB_1() != null
                        && aa_pubb.equals(data.getAnnoDate()))))) {
                if (tipoControllo.getType() != SbnSimile.CONFERMA_TYPE)
                //[TODO ancora da implementare
                    elencoDiagnostico = cercaTitoliSimili(tb_titolo, cd_natura, 1);
                else {
                    //elencoDiagnostico = cercaTitoliSimiliConferma(null, id, cd_natura);
                }
            }
        }
        if(titAcc.getTitAccessoTypeChoice() != null){
            TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
            if (choice.getT423() != null) {
                C101 a101 = choice.getT423().getT101();
                if (a101.getA_101Count() == 0)
                    throw new EccezioneSbnDiagnostico(3195, "Almeno una lingua è obbligatoria");
                for (int i = 0; i < a101.getA_101Count(); i++)
                    if (Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", a101.getA_101(i).toUpperCase())
                        == null)
                        throw new EccezioneSbnDiagnostico(3204, "Codice lingua errato");
                C105 c105 = choice.getT423().getT105();
                //String[] generi = c105.getA_105_4();
                if (c105 != null)
                for (int i = 0; i < c105.getA_105_4Count(); i++)
                    if (Decodificatore
                        .getCd_tabella(
                            "Tb_titolo",
                            "cd_genere_1",
                            c105.getA_105_4(i),
                            choice.getT423().getTipoMateriale().toString())
                        == null)
                        throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
            }

            // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
            if (choice.getT454A() != null) {
            	C101 a101 = choice.getT454A().getT101();
	            if (a101 == null || a101.getA_101Count() == 0)
	                throw new EccezioneSbnDiagnostico(3195, "Almeno una lingua è obbligatoria");
	            for (int i = 0; i < a101.getA_101Count(); i++)
	                if (Decodificatore.getCd_tabella("Tb_titolo", "cd_lingua_1", a101.getA_101(i).toUpperCase())
	                    == null)
	                    throw new EccezioneSbnDiagnostico(3204, "Codice lingua errrato");
            }

        }
        if (statoRecord != null && statoRecord.getType() == StatoRecord.valueOf("c").getType()) {
            if ((tipoControllo == null || tipoControllo.getType() != SbnSimile.CONFERMA_TYPE)) {
                if (tb_titolo.getCD_NATURA().equals("A")) {
                	//20.7.2005 inserito controllo sul tipo materiale
                	if (tb_titolo.getTP_MATERIALE() != null && tb_titolo.getTP_MATERIALE().equals("U"))
                        throw new EccezioneSbnDiagnostico(3208, "cambio natura non consentito in questo caso");
                    if (!ValidatorProfiler.getInstance().verificaAttivitaID(utente, CodiciAttivita.getIstance().MODIFICA_NATURA_TITOLO_DI_ACCESSO_TITOLO_UNIFORME_1022))
                        throw new EccezioneSbnDiagnostico(4000, "Utente non autorizzato");
                }
            }
        }
        String natura_prec = tb_titolo.getCD_NATURA();
        //Agiorno la natura che mi serve per la validazione legami.
        tb_titolo.setCD_NATURA(cd_natura);
        //Ora valida i legami.
        validaLegamiModifica.validaLegamiTitAccessoModifica(utente, timeHash, tb_titolo);
        //Rimetto la natura di prima, per vedere se è modificata
        tb_titolo.setCD_NATURA(natura_prec);
        return tb_titolo;
    }
    public String estraiNatura(TitAccessoType titAcc) {
        //Attributo required
        if (titAcc.getNaturaTitAccesso() != null)
            return titAcc.getNaturaTitAccesso().toString();
        TitAccessoTypeChoice choice = titAcc.getTitAccessoTypeChoice();
        if (choice != null) {
            if (choice.getT423() != null)
                return "T";
            if (choice.getT454() != null)
                return "B";
            if (choice.getT510() != null)
                return "P";
            if (choice.getT517() != null)
                return "D";
        }
        return null;
        //throws new EccezioneSbnDiagnostico("Impossibile determinare la natura del titolo");
    }
}
