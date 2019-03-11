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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoPoli;
import it.finsiel.sbn.polo.factoring.util.GestoreLegami;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.Audiovisivo;
import it.finsiel.sbn.polo.oggetti.Cartografia;
import it.finsiel.sbn.polo.oggetti.Comuni;
import it.finsiel.sbn.polo.oggetti.Discosonoro;
import it.finsiel.sbn.polo.oggetti.Elettronico;
import it.finsiel.sbn.polo.oggetti.Grafica;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.Incipit;
import it.finsiel.sbn.polo.oggetti.Musica;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Personaggio;
import it.finsiel.sbn.polo.oggetti.Rappresentazione;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.ThesauroTitolo;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.TitoloAutore;
import it.finsiel.sbn.polo.oggetti.TitoloLuogo;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.oggetti.TitoloTitolo;
import it.finsiel.sbn.polo.orm.Tb_audiovideo;
import it.finsiel.sbn.polo.orm.Tb_cartografia;
import it.finsiel.sbn.polo.orm.Tb_disco_sonoro;
import it.finsiel.sbn.polo.orm.Tb_grafica;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_incipit;
import it.finsiel.sbn.polo.orm.Tb_musica;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_personaggio;
import it.finsiel.sbn.polo.orm.Tb_rappresent;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_risorsa_elettr;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tb_titset_1;
import it.finsiel.sbn.polo.orm.Tr_per_int;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.Tr_tit_luo;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.Trs_termini_titoli_biblioteche;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C105bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C115;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C121;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C124;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C125bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C126;
import it.iccu.sbn.ejb.model.unimarcmodel.C127;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C135;
import it.iccu.sbn.ejb.model.unimarcmodel.C140bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C181;
import it.iccu.sbn.ejb.model.unimarcmodel.C182;
import it.iccu.sbn.ejb.model.unimarcmodel.C183;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C210;
import it.iccu.sbn.ejb.model.unimarcmodel.C801;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C923;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.IndicatorePubblicato;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;


/**
 * Classe TitoloCrea.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 25-feb-2003
 */
public class TitoloCrea extends Titolo {

	private static final long serialVersionUID = 2611100834185977768L;

	protected SbnSimile tipoControllo = null;
    protected String diagnostico = null;
    protected TableDao elencoDiagnostico = null;
    protected DocumentoType documentoType = null;
    protected ElementAutType elementAutType = null;
    protected LegamiType[] legami = null;
    protected C200 c200 = null;
    protected String t005 = null;
    protected SbnLivello livelloAut = null;
    String id = null;
    DatiDocType datiDoc = null;

    boolean primo_leg_classe = true;
    boolean primo_leg_soggetto = true;

    //almaviva5_20140217 evolutive google3
    protected boolean _cattura;

    public TitoloCrea(CreaType datiType) {
        super();

        tipoControllo = datiType.getTipoControllo();
        elementAutType = datiType.getCreaTypeChoice().getElementoAut();
        documentoType = datiType.getCreaTypeChoice().getDocumento();
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
                datiDoc = documentoType.getDocumentoTypeChoice().getDatiDocumento();
                id = datiDoc.getT001();
                c200 = datiDoc.getT200();

            } else if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
                id = documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT001();
            }
        } else if (elementAutType != null) {
            id = elementAutType.getDatiElementoAut().getT001();
            legami = elementAutType.getLegamiElementoAut();
        }

        //almaviva5_20140217 evolutive google3
        _cattura = datiType.getCattura();
    }

    public TitoloCrea(DocumentoType datiType) {
        super();
        documentoType = datiType;
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
                datiDoc = documentoType.getDocumentoTypeChoice().getDatiDocumento();
                id = datiDoc.getT001();
                c200 = datiDoc.getT200();

            } else if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
                id = documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT001();
            }
        } else if (elementAutType != null) {
            id = elementAutType.getDatiElementoAut().getT001();
            legami = elementAutType.getLegamiElementoAut();
        }
    }

    public TitoloCrea(ModificaType datiType) {
        super();
        tipoControllo = datiType.getTipoControllo();
        elementAutType = datiType.getElementoAut();
        documentoType = datiType.getDocumento();
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            if (documentoType.getDocumentoTypeChoice().getDatiDocumento() != null) {
                datiDoc = documentoType.getDocumentoTypeChoice().getDatiDocumento();
                id = datiDoc.getT001();
                c200 = datiDoc.getT200();

            } else if (documentoType.getDocumentoTypeChoice().getDatiTitAccesso() != null) {
                id = documentoType.getDocumentoTypeChoice().getDatiTitAccesso().getT001();
            }
        } else if (elementAutType != null) {
            id = elementAutType.getDatiElementoAut().getT001();
            legami = elementAutType.getLegamiElementoAut();
        }

        //almaviva5_20140217 evolutive google3
        _cattura = datiType.getCattura();

    }
//    public void settasbnUser(SbnUserType sbnUser){
//
//    }
    /**
     * Deve creare i legami
     * @param sbnUser TODO
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void creaLegami(TimestampHash timeHash, Tb_titolo titolo, SbnUserType sbnUser)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 0; i < legami.length; i++) {
            for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                ArrivoLegame legame = legami[i].getArrivoLegame(c);
                if (legame.getLegameDoc() != null){
	                creaLegameDocDoc(titolo, legame.getLegameDoc(), timeHash);
                }
                else if (legame.getLegameTitAccesso() != null){
                	creaLegameDocTitAccesso(titolo, legame.getLegameTitAccesso(), timeHash);
                }
                else if (legame.getLegameElementoAut() != null) {
                    LegameElementoAutType legEl = legame.getLegameElementoAut();
                    if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
		                 creaLegameAutore(titolo, legEl, timeHash);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.CL_TYPE)
                        creaLegameClasse(titolo, legEl, timeHash);
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.TU_TYPE || legEl.getTipoAuthority().getType() == SbnAuthority.UM_TYPE){
		                creaLegameDocTitUni(titolo, legEl, timeHash);
                    }
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.LU_TYPE)
                        creaLegameLuogo(titolo, legEl, timeHash);
                    else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                        creaLegameMarca(titolo, legEl, timeHash);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                    	creaLegameSoggetto(titolo, legEl, timeHash, sbnUser);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
                    	creaLegameThesauro(titolo, legEl, timeHash, sbnUser);
                    } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE)
                        creaLegameRepertorio(titolo, legEl, timeHash);
                }
            }
        }
    }

    protected void settaChiavi(Tb_titolo titolo, String stringa) throws EccezioneSbnDiagnostico {
        ChiaviTitolo chiavi = new ChiaviTitolo();
        chiavi.estraiChiavi(stringa);
        titolo.setKY_CLES1_T(chiavi.getKy_cles1_t());
        titolo.setKY_CLES2_T(chiavi.getKy_cles2_t());
        titolo.setKY_CLET1_T(chiavi.getKy_clet1_t());
        titolo.setKY_CLET2_T(chiavi.getKy_clet2_t());
        titolo.setKY_CLES1_CT(chiavi.getKy_cles1_ct());
        titolo.setKY_CLES2_CT(chiavi.getKy_cles2_ct());
        titolo.setKY_CLET1_CT(chiavi.getKy_clet1_ct());
        titolo.setKY_CLET2_CT(chiavi.getKy_clet2_ct());
    }

    protected void settaChiavi(Tb_titolo titolo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        String cd_natura = titolo.getCD_NATURA();
        if (!cd_natura.equals("W")) {
            C200 c200 = datiDoc.getT200();
            settaChiavi(titolo, titolo.getISBD());
        } else {
            Tb_titolo titLegato = null;
            for (int i = 0; i < legami.length; i++) {
                for (int c = 0; c < legami[i].getArrivoLegameCount(); c++) {
                    //Sbntipooperazione serve, è da ignorare o non c'è?
                    ArrivoLegame legame = legami[i].getArrivoLegame(c);
                    if (legame.getLegameDoc() != null)
                        if (legame.getLegameDoc().getTipoLegame().getType() == SbnLegameDoc.valueOf("461").getType()) {
                            titLegato = estraiTitoloPerID(legame.getLegameDoc().getIdArrivo());
                            break;
                        }
                }
            }
            if (titLegato!=null) {
                titolo.setKY_CLES1_T(titLegato.getKY_CLES1_T());
                titolo.setKY_CLES2_T(titLegato.getKY_CLES2_T());
                titolo.setKY_CLET1_T(titLegato.getKY_CLET1_T());
                titolo.setKY_CLET2_T(titLegato.getKY_CLET2_T());
                titolo.setKY_CLES1_CT(titLegato.getKY_CLES1_CT());
                titolo.setKY_CLES2_CT(titLegato.getKY_CLES2_CT());
                titolo.setKY_CLET1_CT(titLegato.getKY_CLET1_CT());
                titolo.setKY_CLET2_CT(titLegato.getKY_CLET2_CT());
            }
        }

     // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
     // e opportunamente salvato sulla tb_titolo campo ky_editore
//        if (cd_natura.equals("C")) {
//            //ky_editore
//            if (datiDoc.getT210Count() > 0)
//                titolo.setKY_EDITORE(new ChiaviTitolo().estraiChiaveEditore(datiDoc.getT210(), true));
//
//        }
        if (cd_natura.equals("C")) {
            //ky_editore
//            if (datiDoc.getT210Count() > 0) {
                titolo.setKY_EDITORE(new ChiaviTitolo().estraiChiaveEditoreCollana(datiDoc, true));
            }
//        }
        // Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore
    }

    /**
     * Crea la musica legata ad un documento musicale
     * @throws InfrastructureException
     */
    public void creaMusica(Tb_titolo titolo, DatiDocType datiDoc)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (!(datiDoc instanceof MusicaType))
            return;
        MusicaType musicaType = (MusicaType) datiDoc;
        creaTb_musica(titolo, musicaType);
        if (musicaType.getT922() != null)
            creaTb_rappresent(titolo, musicaType);
        if (musicaType.getT926Count() > 0)
            creaTb_incipit(titolo, musicaType);
        if (musicaType.getT927Count() > 0)
            creaTb_personaggio(titolo, musicaType);

    }
    /**
     * Crea la musica legata ad un documento musicale
     * @throws InfrastructureException
     */
    protected void creaTb_musica(Tb_titolo titolo, MusicaType musicaType) throws EccezioneDB, InfrastructureException {

        Tb_musica tbm = preparaTb_musica(titolo, musicaType);
        tbm.setUTE_INS(titolo.getUTE_INS());
        Musica musicaDB = new Musica();
        musicaDB.inserisci(tbm);
    }

    protected Tb_musica preparaTb_musica(Tb_titolo titolo, MusicaType musicaType) {
        Tb_musica tbm = new Tb_musica();
        tbm.setUTE_VAR(titolo.getUTE_VAR());
        tbm.setFL_CANC(" ");
        tbm.setBID(titolo.getBID());
        if (musicaType.getLivelloAut()!=null) {
            tbm.setCD_LIVELLO(musicaType.getLivelloAut().toString());
        }
        //Questi sono valori di "default"
        tbm.setFL_COMPOSITO(" ");
        tbm.setFL_PALINSESTO(" ");

        C125 c125 = musicaType.getT125();
        if (c125 != null) {
            if (c125.getB_125() != null)
                tbm.setTP_TESTO_LETTER(
                    Decodificatore.getCd_tabella("Tb_musica", "tp_testo_letter", c125.getB_125()));
            if (c125.getA_125_0() != null)
                tbm.setCD_PRESENTAZIONE(
                    Decodificatore.getCd_tabella("Tb_musica", "cd_presentazione", c125.getA_125_0()));
        }
        C128 c128 = musicaType.getT128();
        if (c128 != null) {
            tbm.setDS_ORG_SINT(c128.getB_128());
            tbm.setDS_ORG_ANAL(c128.getC_128());
            if (c128.getD_128() != null)
                tbm.setTP_ELABORAZIONE(
                    Decodificatore.getCd_tabella("Tb_musica", "tp_elaborazione", c128.getD_128()));
        }
        C923 c923 = musicaType.getT923();
        if (c923 != null) {
            //tbm.setDs_traduzione(c923.getA_923());
            if (c923.getB_923() != null)
                tbm.setCD_STESURA(Decodificatore.getCd_tabella("Tb_musica", "cd_stesura", c923.getB_923()));
            if (c923.getC_923() != null)
                tbm.setFL_COMPOSITO(c923.getC_923().toString());
            if (c923.getD_923() != null)
                tbm.setFL_PALINSESTO(c923.getD_923().toString());
            tbm.setDATAZIONE(c923.getE_923());
            if (c923.getG_923() != null)
                tbm.setCD_MATERIA(Decodificatore.getCd_tabella("Tb_musica", "cd_materia", c923.getG_923()));
            tbm.setDS_ILLUSTRAZIONI(c923.getH_923());
            tbm.setNOTAZIONE_MUSICALE(c923.getI_923());
            tbm.setDS_LEGATURA(c923.getL_923());
            tbm.setDS_CONSERVAZIONE(c923.getM_923());
        }
        return tbm;
    }

    /**
     * Crea la musica legata ad un documento musicale
     * @throws InfrastructureException
     */
    public void creaTb_incipit(Tb_titolo titolo, MusicaType musicaType) throws EccezioneDB, InfrastructureException {
        Incipit incipitDB = new Incipit();
        C926[] c926 = musicaType.getT926();
        for (int i = 0; i < c926.length; i++) {
            Tb_incipit tbi = preparaTb_incipit(titolo, c926[i]);
            tbi.setUTE_INS(titolo.getUTE_INS());
            incipitDB.inserisci(tbi);
        }
    }

    protected Tb_incipit preparaTb_incipit(Tb_titolo titolo, C926 c926) {
        Tb_incipit tbi = new Tb_incipit();
        tbi.setUTE_INS(titolo.getUTE_INS());
        tbi.setUTE_VAR(titolo.getUTE_VAR());
        tbi.setFL_CANC(" ");
        tbi.setBID(titolo.getBID());
        if (c926.getA_926() != null)
            tbi.setTP_INDICATORE(
                Decodificatore.getCd_tabella("Tb_incipit", "tp_indicatore", c926.getA_926().toString()));
        tbi.setNUMERO_COMP(c926.getB_926());
        if (c926.getF_926() == null)
            tbi.setNUMERO_MOV("  ");
        else
            tbi.setNUMERO_MOV(c926.getF_926());
        if (c926.getG_926() == null)
            tbi.setNUMERO_P_MOV("  ");
        else
            tbi.setNUMERO_P_MOV(c926.getG_926());
        tbi.setREGISTRO_MUS(c926.getH_926());
        tbi.setNOME_PERSONAGGIO(c926.getQ_926());
        tbi.setBID_LETTERARIO(c926.getR_926());
        tbi.setTEMPO_MUS(c926.getP_926());
        if (c926.getI_926() != null)
            tbi.setCD_FORMA(Decodificatore.getCd_tabella("Tb_incipit", "cd_forma", c926.getI_926()));
        if (c926.getL_926() != null)
            tbi.setCD_TONALITA(Decodificatore.getCd_tabella("Tb_incipit", "cd_tonalita", c926.getL_926()));
        tbi.setCHIAVE_MUS(c926.getM_926());
        tbi.setALTERAZIONE(c926.getN_926());
        tbi.setMISURA(c926.getO_926());
        tbi.setDS_CONTESTO(c926.getC_926());
        return tbi;
    }

    protected void creaTb_personaggio(Tb_titolo titolo, MusicaType musica) throws EccezioneDB, InfrastructureException {
        Tb_personaggio tbp;
        Personaggio personaggioDB = new Personaggio();
        C927[] c927 = musica.getT927();
        Progressivi progressivi = new Progressivi();
        for (int i = 0; i < c927.length; i++) {
            tbp = preparaTb_personaggio(titolo, c927[i], progressivi.getNextIdPersonaggio());
            tbp.setUTE_INS(titolo.getUTE_INS());
            personaggioDB.inserisci(tbp);
            if (c927[i].getC3_927() != null) {
                Tr_per_int rel = new Tr_per_int();
                rel.setVID(c927[i].getC3_927());
                rel.setUTE_INS(titolo.getUTE_INS());
                rel.setUTE_VAR(titolo.getUTE_VAR());
                rel.setFL_CANC(" ");
                rel.setID_PERSONAGGIO(tbp.getID_PERSONAGGIO());
                personaggioDB.inserisciRelazione(rel);
            }
        }
    }

    protected Tb_personaggio preparaTb_personaggio(Tb_titolo titolo, C927 c927, int id) {
        Tb_personaggio tbp = new Tb_personaggio();
        tbp.setUTE_VAR(titolo.getUTE_VAR());
        tbp.setFL_CANC(" ");
        tbp.setBID(titolo.getBID());
        tbp.setNOME_PERSONAGGIO(c927.getA_927());
        tbp.setID_PERSONAGGIO(id);
        if (c927.getB_927() != null)
            tbp.setCD_TIMBRO_VOCALE(
                Decodificatore.getCd_tabella("Tb_personaggio", "cd_timbro_vocale", c927.getB_927()));
        return tbp;
    }

    protected void creaTb_rappresent(Tb_titolo titolo, MusicaType musica)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_rappresent tbr = preparaTb_rappresent(titolo, musica);
        tbr.setUTE_INS(titolo.getUTE_INS());
        Rappresentazione rappresentazione = new Rappresentazione();
        rappresentazione.inserisci(tbr);
    }

    protected Tb_rappresent preparaTb_rappresent(Tb_titolo titolo, MusicaType musica)
        throws EccezioneSbnDiagnostico {
        Tb_rappresent tbr = new Tb_rappresent();
        C922 c922 = musica.getT922();
        tbr.setBID(titolo.getBID());
        tbr.setUTE_VAR(titolo.getUTE_VAR());
        tbr.setUTE_INS(titolo.getUTE_VAR());
        tbr.setFL_CANC(" ");
        tbr.setBID(titolo.getBID());
        if (c922.getA_922() != null)
            tbr.setTP_GENERE(Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()));
        String errore = null;
        if (c922.getP_922() != null && c922.getP_922().length() > 5)
            errore = "P_922";
        if (c922.getR_922() != null && c922.getR_922().length() > 30)
            errore = "R_922";
        if (c922.getS_922() != null && c922.getS_922().length() > 30)
            errore = "S_922";
        if (c922.getT_922() != null && c922.getT_922().length() > 138)
            errore = "T_922";
        if (c922.getQ_922() != null && c922.getQ_922().length() > 15)
            errore = "Q_922";
        if (errore != null) {
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3219, "Lunghezza eccessiva");
            ecc.appendMessaggio("Campo: " + errore);
            throw ecc;
        }
        tbr.setAA_RAPP(c922.getP_922());
        tbr.setDS_PERIODO(c922.getQ_922());
        tbr.setDS_TEATRO(c922.getR_922());
        tbr.setDS_LUOGO_RAPP(c922.getS_922());
        tbr.setDS_OCCASIONE(c922.getU_922());
        tbr.setNOTA_RAPP(c922.getT_922());
        return tbr;
    }

    /**
     * Crea un impronta e la inserisce nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void inserisciImpronta(Tb_titolo titolo, DatiDocType datiDoc) throws IllegalArgumentException, InvocationTargetException, Exception {
        //CREO IMPRONTA
    	// Segnalazione Carla del 10/03/2015:
    	// inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
        C012[] c012 = null;
        if (datiDoc instanceof MusicaType) {
            MusicaType musica = (MusicaType) datiDoc;
            c012 = musica.getT012();
        } else if (datiDoc instanceof AnticoType) {
            AnticoType antico = (AnticoType) datiDoc;
            c012 = antico.getT012();
        } else if (datiDoc instanceof CartograficoType) {
            CartograficoType cartografico = (CartograficoType) datiDoc;
            c012 = cartografico.getT012();
    	} else if (datiDoc instanceof GraficoType) {
    	    GraficoType grafico = (GraficoType) datiDoc;
    	    c012 = grafico.getT012();
        } else
            return;
        Impronta impronta = new Impronta();
        for (int i = 0; i < c012.length; i++) {
            Tb_impronta tbi = creaImpronta(c012[i], titolo);
            impronta.inserisci(tbi);
        }
    }

    public Tb_impronta creaImpronta(C012 c012, Tb_titolo titolo) {
        Tb_impronta tbi = new Tb_impronta();
        tbi.setBID(titolo.getBID());
        tbi.setFL_CANC(" ");
        tbi.setIMPRONTA_1(c012.getA_012_1());
        tbi.setIMPRONTA_2(c012.getA_012_2());
        tbi.setIMPRONTA_3(c012.getA_012_3());
        tbi.setNOTA_IMPRONTA(c012.getNota());
        tbi.setUTE_INS(titolo.getUTE_INS());
        tbi.setUTE_VAR(titolo.getUTE_VAR());
        return tbi;
    }

    public void creaNumeroStandard(NumStdType[] numStd, Tb_titolo titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        NumeroStd numeroDB = new NumeroStd();
        Tb_numero_std numero;
        for (int i = 0; i < numStd.length; i++) {
            numero = new Tb_numero_std();
            numero.setNOTA_NUMERO_STD(numStd[i].getNotaSTD());
            numero.setBID(titolo.getBID());
            if (numStd[i].getPaeseSTD() != null)
                numero.setCD_PAESE(
                    Decodificatore.getCd_tabella("Tb_numero_std", "cd_paese", numStd[i].getPaeseSTD()));
            numero.setFL_CANC(" ");
            numero.setUTE_INS(titolo.getUTE_INS());
            numero.setUTE_VAR(titolo.getUTE_VAR());
            numero.setTP_NUMERO_STD(
                Decodificatore.getCd_tabella(
                    "Tb_numero_std",
                    "tp_numero_std",
                    numStd[i].getTipoSTD().toString()));
            if (numero.getTP_NUMERO_STD().trim().equals("I") || numero.getTP_NUMERO_STD().trim().equals("J"))
                numero.setNUMERO_STD(rimuoviTrattini(numStd[i].getNumeroSTD()));
            else
                numero.setNUMERO_STD(numStd[i].getNumeroSTD());
            if (numero.getTP_NUMERO_STD().trim().equals("L") || numero.getTP_NUMERO_STD().trim().equals("E"))
                numero.setNUMERO_LASTRA(getNumeroLastra(numero.getNUMERO_STD().trim()));
            numeroDB.inserisci(numero);
        }
    }
    /** Estrae il numero dal numStd. */
    //almaviva5_20150107 fix numero lastra > 10 cifre.
    protected BigInteger getNumeroLastra(String numStd) {
        int n;
        int fine = numStd.length() - 1;
        while (fine >= 0 && !Character.isDigit(numStd.charAt(fine)))
            fine--;
        n = fine;
        while (n >= 0 && Character.isDigit(numStd.charAt(n)))
            n--;
        if (n == fine)
            return BigInteger.ZERO;
        return new BigInteger(numStd.substring(n + 1, fine + 1));
    }

    public void creaLegameDocTitAccesso(Tb_titolo titolo, LegameTitAccessoType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }



        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());

        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(titolo.getBID());
        tt.setBID_COLL(leg.getIdArrivo());
        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
        tt.setFL_CANC(" ");
        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
        String tp_legame = leg.getTipoLegame().toString();
        //Aggiungo le nature dei titoli
        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        //Tolgo il primo e l'ultimo elemento
        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
        tt.setTP_LEGAME(tp_legame);
        tt.setSEQUENZA_MUSICA(leg.getSequenzaMusica());
        if (leg.getSottoTipoLegame() != null)
            tt.setTP_LEGAME_MUSICA(leg.getSottoTipoLegame().toString());
        tt.setUTE_VAR(titolo.getUTE_VAR());
        tt.setUTE_INS(titolo.getUTE_VAR());

        // timbri di condivione
        tt.setFL_CONDIVISO(_legame_condiviso);
        tt.setTS_CONDIVISO(TableDao.now());
        tt.setUTE_CONDIVISO(titolo.getUTE_INS());
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("423")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("454")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("510")));
        //if (leg.getTipoLegame().equals(SbnLegameTitAccesso.valueOf("517")));

        //Da creare il legame tra i due doc
        new TitoloTitolo().inserisciLegame(tt);
    }

    protected void creaLegameTraTitoli(Tb_titolo titolo, LegameDocType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }


        //Se è un legame 463 lo ignoro, perchè viene gestito dopo.
        if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("463").getType())
            return;
        //timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());

        //Controllo tipi legame e cd natura

        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(titolo.getBID());
        tt.setBID_COLL(leg.getIdArrivo());
        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
        if (legato == null)
        	throw new EccezioneDB(3028); // titolo da legare inesistente
        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
        tt.setFL_CANC(" ");
        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
        String sequenza = leg.getSequenza();
        if (sequenza != null) {
            if (sequenza.length() > 10) {
                sequenza = sequenza.substring(0, 10);
            } else
                while (sequenza.length() < 10)
                    sequenza = " " + sequenza;
            tt.setSEQUENZA(sequenza);
        }
        tt.setSICI(leg.getSici());
        String tp_legame = leg.getTipoLegame().toString();
        //Aggiungo le nature dei titoli
        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        //Passo dal decodificatore
        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
        //Tolgo il primo e l'ultimo elemento
        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
        tt.setTP_LEGAME(tp_legame);
        tt.setUTE_INS(titolo.getUTE_VAR());
        tt.setUTE_VAR(titolo.getUTE_VAR());
        // timbri di condivione
        tt.setFL_CONDIVISO(_legame_condiviso);
        tt.setTS_CONDIVISO(TableDao.now());
        tt.setUTE_CONDIVISO(titolo.getUTE_INS());
        new TitoloTitolo().inserisciLegame(tt);
    }

    /**
     * Modificato per gestire opportunamente il tipo legame 463 (che è a rovescio)
     * @param titolo
     * @param leg
     * @param timeHash
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void creaLegameDocDoc(Tb_titolo titolo, LegameDocType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        //Se non è un legame 463 lo tratto normalmente
        if (leg.getTipoLegame().getType() == SbnLegameDoc.valueOf("463").getType()) {
            leg.setTipoLegame(SbnLegameDoc.valueOf("461"));
            String idArrivo = leg.getIdArrivo();
            leg.setIdArrivo(titolo.getBID());
            Tb_titolo legato = estraiTitoloPerID(idArrivo);
            creaLegameTraTitoli(legato, leg, timeHash);
            AllineamentoTitolo flagAllineamento = new AllineamentoTitolo(legato);
            flagAllineamento.setTr_tit_tit(true);
            new TitoloGestisceAllineamento().aggiornaFlagAllineamento(
                flagAllineamento,
                titolo.getUTE_VAR());
        } else {
            creaLegameTraTitoli(titolo, leg, timeHash);
        }
    }

    public void creaLegameDocTitUni(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
    	String _legame_condiviso;
    	if ((leg.getCondiviso() != null && (leg.getCondiviso().toString().equals("s")) )) {
    		_legame_condiviso = leg.getCondiviso().toString();
        }else{
        	_legame_condiviso = "n";
        }

    	//timeHash.putTimestamp("Tb_titolo", tit2.getBID(), tit2.getTS_VAR());

        //Controllo tipi legame e cd natura

        Tr_tit_tit tt = new Tr_tit_tit();

     // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
//        tt.setBID_BASE(titolo.getBID());
//        tt.setBID_COLL(leg.getIdArrivo());
//        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
//        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
//        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
//        tt.setFL_CANC(" ");
//        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
//        String tp_legame = leg.getTipoLegame().toString();
//        //Aggiungo le nature dei titoli
//        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
        if (titolo.getCD_NATURA().equals("V")) {
	        tt.setBID_COLL(titolo.getBID());
	        tt.setBID_BASE(leg.getIdArrivo());
	        tt.setCD_NATURA_COLL(titolo.getCD_NATURA());
	        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
	        tt.setCD_NATURA_BASE(legato.getCD_NATURA());
	        tt.setFL_CANC(" ");
	        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
	        String tp_legame = leg.getTipoLegame().toString();
			tp_legame = legato.getCD_NATURA() + tp_legame + titolo.getCD_NATURA();
	        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
	        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
	        tt.setTP_LEGAME(tp_legame);
	        tt.setUTE_INS(titolo.getUTE_INS());
	        tt.setUTE_VAR(titolo.getUTE_INS());
	        // timbri di condivione
	        tt.setFL_CONDIVISO(_legame_condiviso);
	        tt.setTS_CONDIVISO(TableDao.now());
	        tt.setUTE_CONDIVISO(titolo.getUTE_INS());
		} else {
	        tt.setBID_BASE(titolo.getBID());
	        tt.setBID_COLL(leg.getIdArrivo());
	        tt.setCD_NATURA_BASE(titolo.getCD_NATURA());
	        Tb_titolo legato = estraiTitoloPerID(leg.getIdArrivo());
	        tt.setCD_NATURA_COLL(legato.getCD_NATURA());
	        tt.setFL_CANC(" ");
	        tt.setNOTA_TIT_TIT(leg.getNoteLegame());
	        String tp_legame = leg.getTipoLegame().toString();
	        tp_legame = titolo.getCD_NATURA() + tp_legame + legato.getCD_NATURA();
	        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
	        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
	        tt.setTP_LEGAME(tp_legame);
	        tt.setUTE_INS(titolo.getUTE_INS());
	        tt.setUTE_VAR(titolo.getUTE_INS());
	        // timbri di condivione
	        tt.setFL_CONDIVISO(_legame_condiviso);
	        tt.setTS_CONDIVISO(TableDao.now());
	        tt.setUTE_CONDIVISO(titolo.getUTE_INS());
		}

//        //Passo dal decodificatore
//        tp_legame = Decodificatore.getCd_tabella("LICR", tp_legame);
//        //Tolgo il primo e l'ultimo elemento
//        tp_legame = tp_legame.substring(1, tp_legame.length() - 1);
//        tt.setTP_LEGAME(tp_legame);
//        tt.setUTE_INS(titolo.getUTE_VAR());
//        tt.setUTE_VAR(titolo.getUTE_VAR());
//        // timbri di condivione
//        tt.setFL_CONDIVISO(_legame_condiviso);
//        tt.setTS_CONDIVISO(TableDao.now());
//        tt.setUTE_CONDIVISO(titolo.getUTE_INS());
        new TitoloTitolo().inserisciLegame(tt);

    }


    public void creaLegameAutore(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloAutore ta = new TitoloAutore();
        ta.inserisciTitoloAutore(titolo, leg, timeHash);
    }


    //  inserimento:
    // se presente sulla vista non inserire
    // (select from vl_sog_tit_bib where bid=BID cid=cid cd_polo=cd_polo cd_biblioteca=cd_biblioteca
    // altrimenti
    // inserisco con bib e polo operante

    public void creaLegameSoggetto(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash, SbnUserType sbnUser)
    	throws IllegalArgumentException, InvocationTargetException, Exception {

    	SoggettoTitolo st = new SoggettoTitolo();

    	String bid = titolo.getBID();
    	String cid = leg.getIdArrivo();

    	String firma = sbnUser.getBiblioteca() + sbnUser.getUserId();

    	String cd_polo = sbnUser.getBiblioteca().substring(0,3);
    	String cd_biblioteca = sbnUser.getBiblioteca().substring(3,6);

		boolean esiste = st.verificaEsistenzaLegame(bid, cid);
	    if (esiste)
	    	throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");

    	Soggetto soggetto = new Soggetto();
    	Tb_soggetto tb_soggetto = new Tb_soggetto();
    	tb_soggetto =  soggetto.cercaSoggettoPerId(leg.getIdArrivo());
		if (tb_soggetto != null) {

		    //almaviva5_20091030
		    SoggettoValida soggettoValida = new SoggettoValida();
		    String cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", tb_soggetto.getCD_SOGGETTARIO().trim());
		    soggettoValida.controllaVettoreParametriSemantici(cd, sbnUser.getBiblioteca());
		    soggettoValida.controllaMaxLivelloSoggettazione(tb_soggetto.getCD_SOGGETTARIO().trim(), titolo.getBID(), sbnUser);
		    //

	        Tr_tit_sog_bib ts = new Tr_tit_sog_bib();
	        ts.setCID(leg.getIdArrivo());
	        ts.setBID(titolo.getBID());
	        ts.setFL_CANC(" ");

	        ts.setUTE_INS(firma);
	        ts.setUTE_VAR(firma);

	        ts.setCD_POLO(cd_polo);
	        ts.setCD_BIBLIOTECA(cd_biblioteca);
	        ts.setCD_SOGG(tb_soggetto.getCD_SOGGETTARIO());
	        ts.setNOTA_TIT_SOG_BIB(leg.getNoteLegame());

	        //almaviva5_20120507 evolutive CFI
	        ts.setPOSIZIONE(GestoreLegami.getPosizioneLegame(leg) );

	        st.inserisci(ts);
		}
    }


    protected void verificaPrioritaPoloSoggetto(String utente, List v)
    	throws EccezioneSbnDiagnostico, EccezioneDB {
        if (!ElencoPoli.getInstance().contiene(utente.substring(0, 3)))
            throw new EccezioneSbnDiagnostico(3117, "Titolo già legato a soggetto");
        for (int i = 0; i < v.size(); i++) {
            Tr_tit_sog_bib ts = (Tr_tit_sog_bib) v.get(i);
            if (ElencoPoli.getInstance().contiene(ts.getUTE_VAR().substring(0, 3)))
                if (ElencoPoli.getPrioritaSoggetto(ts.getUTE_VAR().substring(0, 3))
                    > ElencoPoli.getPrioritaSoggetto(utente.substring(0, 3)))
                    throw new EccezioneSbnDiagnostico(3117, "Titolo già legato a soggetto");

        }
    }

    protected void verificaPrioritaPoloSoggettoORG(String utente, List v)
        throws EccezioneSbnDiagnostico, EccezioneDB {
    // almaviva questo tipoi di controllo non necessita a livello di polo
//        if (ValidatorAdminNoCache.getInstance().isPolo(utente)) {
//            if (!ElencoPoli.contiene(utente.substring(0, 3)))
//                throw new EccezioneSbnDiagnostico(3117, "Titolo già legato a soggetto");
//            for (int i = 0; i < v.size(); i++) {
//                Tr_tit_sog_bib ts = (Tr_tit_sog_bib) v.get(i);
//                if (ElencoPoli.contiene(ts.getUTE_VAR().substring(0, 3)))
//                    if (ElencoPoli.getPrioritaSoggetto(ts.getUTE_VAR().substring(0, 3))
//                        > ElencoPoli.getPrioritaSoggetto(utente.substring(0, 3)))
//                        throw new EccezioneSbnDiagnostico(3117, "Titolo già legato a soggetto");
//
//            }
//        }
    }

    protected void verificaPrioritaPoloClasse(String utente, List v)
        throws EccezioneSbnDiagnostico, EccezioneDB {
//      almaviva questo tipoi di controllo non necessita a livello di polo
//        if (ValidatorAdminNoCache.getInstance().isPolo(utente)) {
//            if (!ElencoPoli.contiene(utente.substring(0, 3)))
//                throw new EccezioneSbnDiagnostico(3118, "Titolo già legato a classe");
//            for (int i = 0; i < v.size(); i++) {
//                Tr_tit_cla ts = (Tr_tit_cla) v.get(i);
//                if (ElencoPoli.contiene(ts.getUTE_VAR().substring(0, 3)))
//                    if (ElencoPoli.getPrioritaClasse(ts.getUTE_VAR().substring(0, 3))
//                        > ElencoPoli.getPrioritaClasse(utente.substring(0, 3)))
//                        throw new EccezioneSbnDiagnostico(3118, "Titolo già legato a classe");
//
//            }
//        }
    }

    public void creaLegameClasse(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloClasse titC = new TitoloClasse();
        if (primo_leg_classe) {
            primo_leg_classe = false;
            List v = titC.estraiTitoliClasse(titolo.getBID());
            if (v.size() > 0) {
                // almaviva QUI VIENE RIVOLUZIONATA LA MODALITA' DI INSERIMENTO TRA TITOLO E CLASSE
                // IN QUANTO NON DEVONO ESSERE FATTI CONTROLLI SU POLI E VIENE CONSENTITO DI INSERIRE
                // PIU' DI UN SOGGETTO ALL'INTERNO DEL TITOLO
//                verificaPrioritaPoloClasse(titolo.getUTE_VAR(), v);
//                titC.cancellaPerBid(titolo.getBID(), titolo.getUTE_VAR());
            }
        }
//        tc.setCD_SISTEMA(chiave.substring(0, 1));
//        tc.setCD_EDIZIONE(Decodificatore.getCd_tabella("Tb_classe", "cd_edizione", chiave.substring(1, 3)));

        Tr_tit_cla tc = new Tr_tit_cla();
        tc.setBID(titolo.getBID());
        String chiave = leg.getIdArrivo();

        //almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(chiave);
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

        //almaviva5_20120517 #4245
		tc.setUTE_INS(titolo.getUTE_VAR());
        tc.setUTE_VAR(titolo.getUTE_VAR());
        tc.setFL_CANC(" ");

        //almaviva5_20181022 #6794
        tc.setNOTA_TIT_CLA(leg.getNoteLegame());

        titC.inserisci(tc);

    }

    public void creaLegameLuogo(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_luo tl = new Tr_tit_luo();
        tl.setBID(titolo.getBID());
        tl.setLID(leg.getIdArrivo());
        tl.setFL_CANC(" ");
        tl.setUTE_INS(titolo.getUTE_VAR());
        tl.setUTE_VAR(titolo.getUTE_VAR());
        if (leg.getRelatorCode() != null  && leg.getRelatorCode().trim().length()>0 )  {
        	tl.setTP_LUOGO(leg.getRelatorCode().trim());
        } else 	tl.setTP_LUOGO("P");
        tl.setNOTA_TIT_LUO(leg.getNoteLegame());
        new TitoloLuogo().inserisci(tl);
    }

    public void creaLegameRepertorio(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_tit rt = new Tr_rep_tit();
        rt.setBID(titolo.getBID());
        Repertorio repertorio = new Repertorio();
        Tb_repertorio tb_repertorio = repertorio.cercaRepertorioPerCdSig(leg.getIdArrivo());
        rt.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
        rt.setFL_CANC(" ");
        //tipo di legame se è 810 S / altrimenti N
        if (leg.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType())
            rt.setFL_TROVATO("S");
        else
            rt.setFL_TROVATO("N");
        rt.setNOTA_REP_TIT(leg.getNoteLegame());
        rt.setUTE_INS(titolo.getUTE_VAR());
        rt.setUTE_VAR(titolo.getUTE_VAR());
        Tr_rep_titResult tabella = new Tr_rep_titResult(rt);


        RepertorioTitoloUniforme repertorioTitolo = new RepertorioTitoloUniforme();
        if (!repertorioTitolo.controllaEsistenzaLegame(rt, timeHash)) {
            tabella.insert(rt);
        } else {
            rt.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_rep_tit", rt.getBID() + rt.getID_REPERTORIO())));
            tabella.update(rt);
        }


    }

    public void creaGrafico(Tb_titolo titolo, DatiDocType datiDoc) throws EccezioneDB, InfrastructureException {
        if (!(datiDoc instanceof GraficoType))
            return;
        GraficoType grafico = (GraficoType) datiDoc;
        Tb_grafica tbg = preparaTb_grafica(titolo, grafico);
        tbg.setUTE_INS(titolo.getUTE_INS());
        Grafica grafica = new Grafica();
        grafica.inserisci(tbg);
    }

    public Tb_grafica preparaTb_grafica(Tb_titolo titolo, GraficoType grafico) {
        C116 c116 = grafico.getT116();
        Tb_grafica tbg = new Tb_grafica();
        tbg.setBID(titolo.getBID());
        tbg.setUTE_VAR(titolo.getUTE_VAR());
        tbg.setCD_LIVELLO(grafico.getLivelloAut().toString());
        if (c116.getA_116_0() != null)
            tbg.setTP_MATERIALE_GRA(
                Decodificatore.getCd_tabella("Tb_grafica", "tp_materiale_gra", c116.getA_116_0()));
        if (c116.getA_116_1() != null)
            tbg.setCD_SUPPORTO(Decodificatore.getCd_tabella("Tb_grafica", "cd_supporto", c116.getA_116_1()));
        if (c116.getA_116_3() != null)
            tbg.setCD_COLORE(Decodificatore.getCd_tabella("Tb_grafica", "cd_colore", c116.getA_116_3()));
        if (c116.getA_116_4Count() > 0)
            tbg.setCD_TECNICA_DIS_1(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_1", c116.getA_116_4(0)));
        if (c116.getA_116_4Count() > 1)
            tbg.setCD_TECNICA_DIS_2(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_2", c116.getA_116_4(1)));
        if (c116.getA_116_4Count() > 2)
            tbg.setCD_TECNICA_DIS_3(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_3", c116.getA_116_4(2)));
        if (c116.getA_116_10Count() > 0)
            tbg.setCD_TECNICA_STA_1(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_1", c116.getA_116_10(0)));
        if (c116.getA_116_10Count() > 1)
            tbg.setCD_TECNICA_STA_2(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_2", c116.getA_116_10(1)));
        if (c116.getA_116_10Count() > 2)
            tbg.setCD_TECNICA_STA_3(
                Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_3", c116.getA_116_10(2)));
        tbg.setCD_DESIGN_FUNZ(
            Decodificatore.getCd_tabella("Tb_grafica", "cd_design_funz", c116.getA_116_16()));
        return tbg;
    }

    public void creaCartografico(Tb_titolo titolo, DatiDocType datiDoc) throws EccezioneDB, InfrastructureException {
        if (!(datiDoc instanceof CartograficoType))
            return;
        CartograficoType grafico = (CartograficoType) datiDoc;
        Tb_cartografia tbg = preparaTb_cartografia(titolo, grafico);
        tbg.setUTE_INS(titolo.getUTE_INS());
        Cartografia grafica = new Cartografia();
        grafica.inserisci(tbg);
    }

    public Tb_cartografia preparaTb_cartografia(Tb_titolo titolo, CartograficoType grafico) {
        Tb_cartografia tbg = new Tb_cartografia();
        tbg.setBID(titolo.getBID());
        tbg.setUTE_INS(titolo.getUTE_INS());
        tbg.setUTE_VAR(titolo.getUTE_VAR());
        tbg.setCD_LIVELLO(grafico.getLivelloAut().toString());
        C100 c100 = grafico.getT100();

        // Inizio intervento per bug Liguria 4912  almaviva2 Riportato da Indice con commento:
		//Controllo per data null MANTIS 0001699
        if(c100!=null){
            if (c100.getA_100_20() != null)
                tbg.setTP_PUBB_GOV(
                    Decodificatore.getCd_tabella("Tb_cartografia", "tp_pubb_gov", c100.getA_100_20()));
        }

    	// Fine intervento per bug Liguria 4912

        C120 c120 = grafico.getT120(); //obbligatorio
        if (c120.getA_120_0() != null)
            tbg.setCD_COLORE(Decodificatore.getCd_tabella("Tb_cartografia", "cd_colore", c120.getA_120_0()));

     // almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
        if (c120.getA_120_7() != null)
    		tbg.setTP_PROIEZIONE(
    				Decodificatore.getCd_tabella("Tb_cartografia", "tp_proiezione", c120.getA_120_7()));

        if (c120.getA_120_9() != null)
            tbg.setCD_MERIDIANO(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_meridiano", c120.getA_120_9()));
        C121 c121 = grafico.getT121(); //Obbligatorio
        if (c121.getA_121_3() != null)
            tbg.setCD_SUPPORTO_FISICO(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_supporto_fisico", c121.getA_121_3()));
        if (c121.getA_121_5() != null)
            tbg.setCD_TECNICA(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_tecnica", c121.getA_121_5()));
        if (c121.getA_121_6() != null)
            tbg.setCD_FORMA_RIPR(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_ripr", c121.getA_121_6()));
        if (c121.getA_121_8() != null)
            tbg.setCD_FORMA_PUBB(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_pubb", c121.getA_121_8()));
        if (c121.getB_121_0() != null)
            tbg.setCD_ALTITUDINE(
                Decodificatore.getCd_tabella("Tb_cartografia", "cd_altitudine", c121.getB_121_0()));
        C123 c123 = grafico.getT123(); //Obbligatorio
        tbg.setCD_TIPOSCALA(
            Decodificatore.getCd_tabella("Tb_cartografia", "cd_tiposcala", c123.getId1().toString()));
        if (c123.getA_123() != null)
            tbg.setTP_SCALA(Decodificatore.getCd_tabella("Tb_cartografia", "tp_scala", c123.getA_123()));
        tbg.setSCALA_ORIZ(c123.getB_123());
        tbg.setSCALA_VERT(c123.getC_123());
        tbg.setLONGITUDINE_OVEST(c123.getD_123());
        tbg.setLONGITUDINE_EST(c123.getE_123());
        tbg.setLATITUDINE_NORD(c123.getF_123());
        tbg.setLATITUDINE_SUD(c123.getG_123());
        C124 c124 = grafico.getT124(); //non obbligatorio
        if (c124 != null) {
            if (c124.getA_124() != null)
                tbg.setTP_IMMAGINE(
                    Decodificatore.getCd_tabella("Tb_cartografia", "tp_immagine", c124.getA_124()));
            if (c124.getB_124() != null)
                tbg.setCD_FORMA_CART(
                    Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_cart", c124.getB_124()));
            if (c124.getD_124() != null)
                tbg.setCD_PIATTAFORMA(
                    Decodificatore.getCd_tabella("Tb_cartografia", "tp_piattaforma", c124.getD_124()));
            if (c124.getE_124() != null)
                tbg.setCD_CATEG_SATELLITE(
                    Decodificatore.getCd_tabella("Tb_cartografia", "cd_categ_satellite", c124.getE_124()));
        }
        return tbg;
    }
    /** Solo per antico
     * @throws InfrastructureException */
    public void creaLegameMarca(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tr_tit_mar tm = new Tr_tit_mar();
        tm.setBID(titolo.getBID());
        tm.setFL_CANC(" ");
        tm.setNOTA_TIT_MAR(leg.getNoteLegame());
        tm.setMID(leg.getIdArrivo());
        tm.setUTE_INS(titolo.getUTE_VAR());
        tm.setUTE_VAR(titolo.getUTE_VAR());
        new TitoloMarca().inserisci(tm);
    }

    public void creaFonte(Tb_titolo titolo, C801 c801) {
        if (c801 != null) {
            titolo.setCD_AGENZIA(c801.getA_801() + c801.getB_801());
            titolo.setCD_NORME_CAT(c801.getG_801());
        } else {
            titolo.setCD_AGENZIA("ITICCU");
        }
    }

    // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

    public void creaComuni(Tb_titolo titolo, DatiDocType datiDoc)
		throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

    	DatiDocType ddtComuni = datiDoc;
    	Tb_titset_1 tbt = preparaTb_titset_1(titolo, ddtComuni);
    	if (tbt == null) // 02/03/2015 almaviva7
    		return;
        tbt.setUTE_INS(titolo.getUTE_INS());
        Comuni comuni = new Comuni();
        comuni.inserisci(tbt);
	}

    public Tb_titset_1 preparaTb_titset_1(Tb_titolo titolo, DatiDocType ddt)
    throws EccezioneSbnDiagnostico {

    	// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015

        int natura = ddt.getNaturaDoc().getType(); // 02/03/2015 almaviva7
        if (natura == SbnNaturaDocumento.C_TYPE)
        	return null;

    	Tb_titset_1 tbt = new Tb_titset_1();
        tbt.setBID(titolo.getBID());
        tbt.setUTE_INS(titolo.getUTE_INS());
        tbt.setUTE_VAR(titolo.getUTE_VAR());
        tbt.setFL_CANC(" ");

// almaviva7 07/07/2014
		C105bis c105bis = ddt.getT105bis();
        if (c105bis != null)  {
			tbt.setS105_TP_TESTO_LETTERARIO(Decodificatore.getCd_unimarc("COLM", c105bis.getA_105_11()));
			if (tbt.getS105_TP_TESTO_LETTERARIO() == null)
                throw new EccezioneSbnDiagnostico(2900, "Codice tipo di testo letterario errato");
        }
		C140bis c140bis = ddt.getT140bis();
        if (c140bis != null) {
			tbt.setS140_TP_TESTO_LETTERARIO(Decodificatore.getCd_unimarc("COLA", c140bis.getA_140_17()));
			if (tbt.getS140_TP_TESTO_LETTERARIO() == null)
                throw new EccezioneSbnDiagnostico(2900, "Codice tipo di testo letterario errato");
        }
// End almaviva7 07/07/2014



    	// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
        C125bis c125bis = ddt.getT125bis();
        if (c125bis != null)  {
     	   tbt.setS125_INDICATORE_TESTO(Decodificatore.getCd_unimarc("INDT", c125bis.getB_125()));
     	  if (tbt.getS125_INDICATORE_TESTO() == null)
              throw new EccezioneSbnDiagnostico(2900, "Codice tipo di testo registrazione sonora errato");
        }


        C181[] c181 = ddt.getT181();

        if (c181.length == 1) {
    		if (c181[0].getA_181_0() != null) {
    			tbt.setS181_TP_FORMA_CONTENUTO_1(Decodificatore.getCd_unimarc("FOCO", c181[0].getA_181_0()));
    			if (tbt.getS181_TP_FORMA_CONTENUTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_0() != null) {
    			tbt.setS181_CD_TIPO_CONTENUTO_1(Decodificatore.getCd_unimarc("TICO", c181[0].getB_181_0()));
    			if (tbt.getS181_CD_TIPO_CONTENUTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_1() != null) {
    			tbt.setS181_CD_MOVIMENTO_1(Decodificatore.getCd_unimarc("MOVI", c181[0].getB_181_1()));
    			if (tbt.getS181_CD_MOVIMENTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_2() != null) {
    			tbt.setS181_CD_DIMENSIONE_1(Decodificatore.getCd_unimarc("BIDI", c181[0].getB_181_2()));
    			if (tbt.getS181_CD_DIMENSIONE_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_3() != null) {
    			tbt.setS181_CD_SENSORIALE_1_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_3()));
    			if (tbt.getS181_CD_SENSORIALE_1_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_4() != null) {
    			tbt.setS181_CD_SENSORIALE_2_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_4()));
    			if (tbt.getS181_CD_SENSORIALE_2_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_5() != null) {
    			tbt.setS181_CD_SENSORIALE_3_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_5()));
    			if (tbt.getS181_CD_SENSORIALE_3_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
        }

        if (c181.length > 1) {
        	if (c181[0].getA_181_0() != null) {
    			tbt.setS181_TP_FORMA_CONTENUTO_1(Decodificatore.getCd_unimarc("FOCO", c181[0].getA_181_0()));
    			if (tbt.getS181_TP_FORMA_CONTENUTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_0() != null) {
    			tbt.setS181_CD_TIPO_CONTENUTO_1(Decodificatore.getCd_unimarc("TICO", c181[0].getB_181_0()));
    			if (tbt.getS181_CD_TIPO_CONTENUTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_1() != null) {
    			tbt.setS181_CD_MOVIMENTO_1(Decodificatore.getCd_unimarc("MOVI", c181[0].getB_181_1()));
    			if (tbt.getS181_CD_MOVIMENTO_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_2() != null) {
    			tbt.setS181_CD_DIMENSIONE_1(Decodificatore.getCd_unimarc("BIDI", c181[0].getB_181_2()));
    			if (tbt.getS181_CD_DIMENSIONE_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_3() != null) {
    			tbt.setS181_CD_SENSORIALE_1_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_3()));
    			if (tbt.getS181_CD_SENSORIALE_1_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_4() != null) {
    			tbt.setS181_CD_SENSORIALE_2_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_4()));
    			if (tbt.getS181_CD_SENSORIALE_2_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[0].getB_181_5() != null) {
    			tbt.setS181_CD_SENSORIALE_3_1(Decodificatore.getCd_unimarc("SENS", c181[0].getB_181_5()));
    			if (tbt.getS181_CD_SENSORIALE_3_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getA_181_0() != null) {
    			tbt.setS181_TP_FORMA_CONTENUTO_2(Decodificatore.getCd_unimarc("FOCO", c181[1].getA_181_0()));
    			if (tbt.getS181_TP_FORMA_CONTENUTO_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_0() != null) {
    			tbt.setS181_CD_TIPO_CONTENUTO_2(Decodificatore.getCd_unimarc("TICO", c181[1].getB_181_0()));
    			if (tbt.getS181_CD_TIPO_CONTENUTO_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_1() != null) {
    			tbt.setS181_CD_MOVIMENTO_2(Decodificatore.getCd_unimarc("MOVI", c181[1].getB_181_1()));
    			if (tbt.getS181_CD_MOVIMENTO_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_2() != null) {
    			tbt.setS181_CD_DIMENSIONE_2(Decodificatore.getCd_unimarc("BIDI", c181[1].getB_181_2()));
    			if (tbt.getS181_CD_DIMENSIONE_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_3() != null) {
    			tbt.setS181_CD_SENSORIALE_1_2(Decodificatore.getCd_unimarc("SENS", c181[1].getB_181_3()));
    			if (tbt.getS181_CD_SENSORIALE_1_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_4() != null) {
    			tbt.setS181_CD_SENSORIALE_2_2(Decodificatore.getCd_unimarc("SENS", c181[1].getB_181_4()));
    			if (tbt.getS181_CD_SENSORIALE_2_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
    		if (c181[1].getB_181_5() != null) {
    			tbt.setS181_CD_SENSORIALE_3_2(Decodificatore.getCd_unimarc("SENS", c181[1].getB_181_5()));
    			if (tbt.getS181_CD_SENSORIALE_3_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
        	}
        }

        C182[] c182 = ddt.getT182();

        if (c182.length >= 1) {
        	if (c182[0].getA_182_0() != null) {
	            tbt.setS182_TP_MEDIAZIONE_1(Decodificatore.getCd_unimarc("MEDI", c182[0].getA_182_0()));
	            if (tbt.getS182_TP_MEDIAZIONE_1() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
	        }
        }
        if (c182.length == 2) {
        	if (c182[1].getA_182_0() != null) {
	            tbt.setS182_TP_MEDIAZIONE_2(Decodificatore.getCd_unimarc("MEDI", c182[1].getA_182_0()));
	            if (tbt.getS182_TP_MEDIAZIONE_2() == null)
                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
	        }
        }

        // evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
        // In questo caso il controllo sul valore null della 183 viene effettuato ma se siamo in cattura si assume come
        // corretto anche il null (nel caso in cui l'Indice non lo invii)
        // SETTO LA 183 solo per schema versione = 2.01
        //if (versione.compareTo(new BigDecimal(2.01)) > -1 ){
	        C183[] c183 = ddt.getT183();

	        if (c183.length >= 1) {
	        	if (c183[0].getA_183_0() != null) {
		            tbt.setS183_TP_SUPPORTO_1(Decodificatore.getCd_unimarc("SUPP", c183[0].getA_183_0()));
	    			if (tbt.getS183_TP_SUPPORTO_1() == null && !_cattura)
	                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
		        }
	        }
	        if (c183.length == 2) {
	        	if (c183[1].getA_183_0() != null) {
		            tbt.setS183_TP_SUPPORTO_2(Decodificatore.getCd_unimarc("SUPP", c183[1].getA_183_0()));
	    			if (tbt.getS183_TP_SUPPORTO_2() == null && !_cattura)
	                    throw new EccezioneSbnDiagnostico(3389); //Codice errato in area0
	        	}
	        }
        //}

	     // Marzo 2018 manutenzione per gsetire il flag di poubblicazione anche quando l'area di pubblicazione non sia valorizzato
	     //evolutive 2017
        C210[] c210 = ddt.getT210();
        //if (c210[0] != null && c210[0].getId2() != null) {
        //almaviva 11/08/2017 MODIFICATO IL TEST DI ACCESSO  SU PRIMO ARRAY C210
        if ((ddt.getT210() != null)  && (ddt.getT210().length > 0)){
        	if (c210[0].getId2() != null){
				if (c210[0].getId2().equals(IndicatorePubblicato.VALUE_0)) //valore '1'
					tbt.setS210_IND_PUBBLICATO("1");
        	}
        }

        return tbt;
    } // End Tb_titset_1 preparaTb_titset_1
    // Fine almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi



    // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

    public void creaElettronico(Tb_titolo titolo, DatiDocType datiDoc)
		throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
	    if (!(datiDoc instanceof ElettronicoType))
	        return;
	    if (titolo.getTP_RECORD_UNI().equals("l")) {
	    	ElettronicoType elettronicoType = (ElettronicoType) datiDoc;
	        Tb_risorsa_elettr tbe = preparaTb_risorsa_elettr(titolo, elettronicoType);
	        tbe.setUTE_INS(titolo.getUTE_INS());
	        Elettronico elettronico = new Elettronico();
	        elettronico.inserisci(tbe);
	    }
	}

    public void creaAudiovisivo(Tb_titolo titolo, DatiDocType datiDoc)
    	throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (!(datiDoc instanceof AudiovisivoType))
            return;
        if (titolo.getTP_RECORD_UNI().equals("g")) {
	        AudiovisivoType audiovisivoType = (AudiovisivoType) datiDoc;
	        Tb_audiovideo tba = preparaTb_audiovideo(titolo, audiovisivoType);
	        tba.setUTE_INS(titolo.getUTE_INS());
	        Audiovisivo audiovisivo = new Audiovisivo();
	        audiovisivo.inserisci(tba);
//VERIFICARE
	        if (audiovisivoType.getT128() != null)
	        	creaTb_musica_audio(titolo, audiovisivoType);
	        if (audiovisivoType.getT922() != null)
	            creaTb_rappresent_audio(titolo, audiovisivoType);
	        if (audiovisivoType.getT927Count() > 0)
	            creaTb_personaggio_audio(titolo, audiovisivoType);
        }
    }
    public void creaDiscosonoro(Tb_titolo titolo, DatiDocType datiDoc)
    	throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (!(datiDoc instanceof AudiovisivoType))
            return;
        if (titolo.getTP_RECORD_UNI().equals("i") || titolo.getTP_RECORD_UNI().equals("j") ) {
	        AudiovisivoType disco = (AudiovisivoType) datiDoc;
	        Tb_disco_sonoro tbd = preparaTb_disco_sonoro(titolo, disco);
	        tbd.setUTE_INS(titolo.getUTE_INS());
	        Discosonoro discoSonoro = new Discosonoro();
	        discoSonoro.inserisci(tbd);
//VERIFICARE (per 'j' crea sempre un record su tb_musica, anche vuoto a meno del livello)
	        if (titolo.getTP_RECORD_UNI().equals("j"))
	        	creaTb_musica_audio(titolo, disco);
	        if (disco.getT922() != null)
	            creaTb_rappresent_audio(titolo, disco);
	        if (disco.getT927Count() > 0)
	            creaTb_personaggio_audio(titolo, disco);
        }
    }

    protected void creaTb_musica_audio(Tb_titolo titolo, AudiovisivoType audiovisivoType) throws EccezioneDB, InfrastructureException {

        Tb_musica tbm = preparaTb_musica_audio(titolo, audiovisivoType);
        tbm.setUTE_INS(titolo.getUTE_INS());
        Musica musicaDB = new Musica();
        musicaDB.inserisci(tbm);
    }

    protected void creaTb_rappresent_audio(Tb_titolo titolo, AudiovisivoType audiovisivo)
	    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
	    Tb_rappresent tbr = preparaTb_rappresent_audio(titolo, audiovisivo);
	    tbr.setUTE_INS(titolo.getUTE_INS());
	    Rappresentazione rappresentazione = new Rappresentazione();
	    rappresentazione.inserisci(tbr);
	}



  //almaviva4 23/03/2015
    public Tb_titset_1 preparaTb_titset_1_def(String tipoRecord_db, Tb_titolo titolo, DatiDocType ddt)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        int natura = ddt.getNaturaDoc().getType();
        if (natura == SbnNaturaDocumento.C_TYPE)
        	return null;
        Comuni comuni = new Comuni();
        Tb_titset_1 tbt = comuni.cercaPerId(titolo.getBID());
        if (tbt != null) {

	        tbt.setUTE_INS(titolo.getUTE_INS());
	        tbt.setUTE_VAR(titolo.getUTE_VAR());
	        tbt.setFL_CANC(" ");

	        String tipoRecord_in = ddt.getGuida().getTipoRecord().toString(); //controllare se guida null ecc..
	        if ((tipoRecord_db.equals("a") || tipoRecord_db.equals("b")) &&
	        		(!tipoRecord_in.equals("a") && !tipoRecord_in.equals("b"))) {
				tbt.setS105_TP_TESTO_LETTERARIO(null);
				tbt.setS140_TP_TESTO_LETTERARIO(null);
		        return tbt;
	        }

	        if (tipoRecord_db.equals("i") && !tipoRecord_in.equals("i")) {
	     	    tbt.setS125_INDICATORE_TESTO(null);
	            return tbt;
	        }
        } //end tbm2 != null
        return null;
    } // End Tb_titset_1 preparaTb_titset_1_def



    public Tb_risorsa_elettr preparaTb_risorsa_elettr(Tb_titolo titolo, ElettronicoType grafico) {
    	Tb_risorsa_elettr tbe = new Tb_risorsa_elettr();
        tbe.setBID(titolo.getBID());
        tbe.setUTE_INS(titolo.getUTE_INS());
        tbe.setUTE_VAR(titolo.getUTE_VAR());
        tbe.setFL_CANC(" ");
        tbe.setCD_LIVELLO(grafico.getLivelloAut().toString());
        C135 c135 = grafico.getT135();
        if (c135.getA_135_0() != null)
            tbe.setTP_RISORSA(Decodificatore.getCd_unimarc("RIEL", c135.getA_135_0()));
        if (c135.getA_135_1() != null)
            tbe.setCD_DESIGNAZIONE(Decodificatore.getCd_unimarc("DESI", c135.getA_135_1()));
        if (c135.getA_135_2() != null)
            tbe.setCD_COLORE(Decodificatore.getCd_unimarc("CDCO", c135.getA_135_2()));
        if (c135.getA_135_3() != null)
            tbe.setCD_DIMENSIONE(Decodificatore.getCd_unimarc("CDDI", c135.getA_135_3()));
        if (c135.getA_135_4() != null)
            tbe.setCD_SUONO(Decodificatore.getCd_unimarc("SUON", c135.getA_135_4()));
        return tbe;
    }

    public Tb_audiovideo preparaTb_audiovideo(Tb_titolo titolo, AudiovisivoType grafico)
		throws EccezioneDB, EccezioneSbnDiagnostico {
    	Tb_audiovideo tba = new Tb_audiovideo();
        tba.setBID(titolo.getBID());
        tba.setUTE_INS(titolo.getUTE_INS());
        tba.setUTE_VAR(titolo.getUTE_VAR());
        tba.setFL_CANC(" ");
        tba.setCD_LIVELLO(grafico.getLivelloAut().toString());
        C115 c115 = grafico.getT115(); //obbligatorio
        if (c115.getA_115_0() != null)
            tba.setTP_MATER_AUDIOVIS(Decodificatore.getCd_unimarc("TIVI", c115.getA_115_0()));
        if (c115.getA_115_1() != null) {
        	for (int indice = 0; indice < c115.getA_115_1().length(); indice++)
            	if (!Character.isDigit(c115.getA_115_1().charAt(indice))) {
            		throw new EccezioneSbnDiagnostico(3357,  "Campo lunghezza deve essere numerico");
            	}
            	tba.setLUNGHEZZA(Integer.valueOf(c115.getA_115_1()));
        }
        if (c115.getA_115_4() != null)
            tba.setCD_COLORE(Decodificatore.getCd_unimarc("INDC", c115.getA_115_4()));
        if (c115.getA_115_5() != null)
            tba.setCD_SUONO(Decodificatore.getCd_unimarc("INDS", c115.getA_115_5()));
        if (c115.getA_115_6() != null)
            tba.setTP_MEDIA_SUONO(Decodificatore.getCd_unimarc("SUSU", c115.getA_115_6()));
        if (c115.getA_115_7() != null)
            tba.setCD_DIMENSIONE(Decodificatore.getCd_unimarc("LARG", c115.getA_115_7()));
        if (c115.getA_115_8() != null)
            tba.setCD_FORMA_VIDEO(Decodificatore.getCd_unimarc("FODI", c115.getA_115_8()));
        if (c115.getA_115_9() != null)
            tba.setCD_TECNICA(Decodificatore.getCd_unimarc("TEVI", c115.getA_115_9()));
        if (c115.getA_115_10() != null)
            tba.setTP_FORMATO_FILM(Decodificatore.getCd_unimarc("FPIM", c115.getA_115_10()));

        int materialeCount = c115.getA_115_11Count();
        if (materialeCount > 0) {
              String[] materiale = new String[materialeCount];
              for (int i=0; i < materialeCount; i++) {
                    if (c115.getA_115_11()[i] != null){
                    	materiale[i] = c115.getA_115_11()[i];
                    }
              }
      		if (materialeCount == 1){
      			tba.setCD_MAT_ACCOMP_1(Decodificatore.getCd_unimarc("MACC", materiale[0]));
      		}
      		if (materialeCount == 2){
      			tba.setCD_MAT_ACCOMP_1(Decodificatore.getCd_unimarc("MACC", materiale[0]));
      			tba.setCD_MAT_ACCOMP_2(Decodificatore.getCd_unimarc("MACC", materiale[1]));
      		}
      		if (materialeCount == 3){
      			tba.setCD_MAT_ACCOMP_1(Decodificatore.getCd_unimarc("MACC", materiale[0]));
      			tba.setCD_MAT_ACCOMP_2(Decodificatore.getCd_unimarc("MACC", materiale[1]));
      			tba.setCD_MAT_ACCOMP_3(Decodificatore.getCd_unimarc("MACC", materiale[2]));
      		}
      		if (materialeCount == 4){
      			tba.setCD_MAT_ACCOMP_1(Decodificatore.getCd_unimarc("MACC", materiale[0]));
      			tba.setCD_MAT_ACCOMP_2(Decodificatore.getCd_unimarc("MACC", materiale[1]));
      			tba.setCD_MAT_ACCOMP_3(Decodificatore.getCd_unimarc("MACC", materiale[2]));
      			tba.setCD_MAT_ACCOMP_4(Decodificatore.getCd_unimarc("MACC", materiale[3]));
      		}
      	} // end if materialeCount

        if (c115.getA_115_15() != null)
            tba.setCD_FORMA_REGIST(Decodificatore.getCd_unimarc("PUVI", c115.getA_115_15()));
        if (c115.getA_115_16() != null)
            tba.setTP_FORMATO_VIDEO(Decodificatore.getCd_unimarc("PRVI", c115.getA_115_16()));
        if (c115.getA_115_17() != null)
            tba.setCD_MATERIALE_BASE(Decodificatore.getCd_unimarc("EMUL", c115.getA_115_17()));
        if (c115.getA_115_18() != null)
            tba.setCD_SUPPORTO_SECOND(Decodificatore.getCd_unimarc("MASU", c115.getA_115_18()));
        if (c115.getA_115_19() != null)
            tba.setCD_BROADCAST(Decodificatore.getCd_unimarc("STRA", c115.getA_115_19()));
        if (c115.getB_115_0() != null)
            tba.setTP_GENERAZIONE(Decodificatore.getCd_unimarc("VERS", c115.getB_115_0()));
        if (c115.getB_115_1() != null)
            tba.setCD_ELEMENTI(Decodificatore.getCd_unimarc("ELEP", c115.getB_115_1()));
        if (c115.getB_115_2() != null)
            tba.setCD_CATEG_COLORE(Decodificatore.getCd_unimarc("SCCF", c115.getB_115_2()));
        if (c115.getB_115_3() != null)
            tba.setCD_POLARITA(Decodificatore.getCd_unimarc("EMUP", c115.getB_115_3()));
        if (c115.getB_115_4() != null)
            tba.setCD_PELLICOLA(Decodificatore.getCd_unimarc("COPE", c115.getB_115_4()));
        if (c115.getB_115_5() != null)
            tba.setTP_SUONO(Decodificatore.getCd_unimarc("TISI", c115.getB_115_5()));
        if (c115.getB_115_6() != null)
            tba.setTP_STAMPA_FILM(Decodificatore.getCd_unimarc("TIPE", c115.getB_115_6()));
//        if (c115.getB_115_7() != null)
//            tba.setCd_deteriore(Decodificatore.getCd_unimarc("SDET", c115.getB_115_7()));
//        if (c115.getB_115_8() != null)
//            tba.setCd_completo(Decodificatore.getCd_unimarc("COMP", c115.getB_115_8()));
//        if (c115.getB_115_9() > 0)
//        	tba.setDt_ispezione(c115.getB_115_9());
        return tba;
    }
    public Tb_disco_sonoro preparaTb_disco_sonoro(Tb_titolo titolo, AudiovisivoType grafico) {
    	Tb_disco_sonoro tbd = new Tb_disco_sonoro();
        tbd.setBID(titolo.getBID());
        tbd.setUTE_INS(titolo.getUTE_INS());
        tbd.setUTE_VAR(titolo.getUTE_VAR());
        tbd.setFL_CANC(" ");
        tbd.setCD_LIVELLO(grafico.getLivelloAut().toString());
        C126 c126 = grafico.getT126(); //obbligatorio
        if (c126 != null) {
        if (c126.getA_126_0() != null)
            tbd.setCD_FORMA(Decodificatore.getCd_unimarc("FPUB", c126.getA_126_0()));
        if (c126.getA_126_1() != null)
            tbd.setCD_VELOCITA(Decodificatore.getCd_unimarc("VELO", c126.getA_126_1()));
        if (c126.getA_126_2() != null)
            tbd.setTP_SUONO(Decodificatore.getCd_unimarc("TISU", c126.getA_126_2()));
        if (c126.getA_126_3() != null)
            tbd.setCD_PISTA(Decodificatore.getCd_unimarc("LASC", c126.getA_126_3()));
        if (c126.getA_126_4() != null)
            tbd.setCD_DIMENSIONE(Decodificatore.getCd_unimarc("DINA", c126.getA_126_4()));
        if (c126.getA_126_5() != null)
            tbd.setCD_LARG_NASTRO(Decodificatore.getCd_unimarc("LANA", c126.getA_126_5()));
        if (c126.getA_126_6() != null)
            tbd.setCD_CONFIGURAZIONE(Decodificatore.getCd_unimarc("CONA", c126.getA_126_6()));

        int materialeCount = c126.getA_126_7Count();
        if (materialeCount > 0) {
              String[] materiale = new String[materialeCount];
              for (int i=0; i < materialeCount; i++) {
                    if (c126.getA_126_7()[i] != null){
                    	materiale[i] = c126.getA_126_7()[i];
                    }
              }
      		if (materialeCount == 1){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      		}
      		if (materialeCount == 2){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      			tbd.setCD_MATER_ACCOMP_2(Decodificatore.getCd_unimarc("MATA", materiale[1]));
      		}
      		if (materialeCount == 3){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      			tbd.setCD_MATER_ACCOMP_2(Decodificatore.getCd_unimarc("MATA", materiale[1]));
      			tbd.setCD_MATER_ACCOMP_3(Decodificatore.getCd_unimarc("MATA", materiale[2]));
      		}
      		if (materialeCount == 4){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      			tbd.setCD_MATER_ACCOMP_2(Decodificatore.getCd_unimarc("MATA", materiale[1]));
      			tbd.setCD_MATER_ACCOMP_3(Decodificatore.getCd_unimarc("MATA", materiale[2]));
      			tbd.setCD_MATER_ACCOMP_4(Decodificatore.getCd_unimarc("MATA", materiale[3]));
      		}
      		if (materialeCount == 5){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      			tbd.setCD_MATER_ACCOMP_2(Decodificatore.getCd_unimarc("MATA", materiale[1]));
      			tbd.setCD_MATER_ACCOMP_3(Decodificatore.getCd_unimarc("MATA", materiale[2]));
      			tbd.setCD_MATER_ACCOMP_4(Decodificatore.getCd_unimarc("MATA", materiale[3]));
      			tbd.setCD_MATER_ACCOMP_5(Decodificatore.getCd_unimarc("MATA", materiale[4]));
      		}
      		if (materialeCount == 6){
      			tbd.setCD_MATER_ACCOMP_1(Decodificatore.getCd_unimarc("MATA", materiale[0]));
      			tbd.setCD_MATER_ACCOMP_2(Decodificatore.getCd_unimarc("MATA", materiale[1]));
      			tbd.setCD_MATER_ACCOMP_3(Decodificatore.getCd_unimarc("MATA", materiale[2]));
      			tbd.setCD_MATER_ACCOMP_4(Decodificatore.getCd_unimarc("MATA", materiale[3]));
      			tbd.setCD_MATER_ACCOMP_5(Decodificatore.getCd_unimarc("MATA", materiale[4]));
      			tbd.setCD_MATER_ACCOMP_6(Decodificatore.getCd_unimarc("MATA", materiale[5]));
      		}
      	} // end if materialeCount

        if (c126.getA_126_13() != null)
            tbd.setCD_TECNICA_REGIS(Decodificatore.getCd_unimarc("TERE", c126.getA_126_13()));
        if (c126.getA_126_14() != null)
            tbd.setCD_RIPRODUZIONE(Decodificatore.getCd_unimarc("SCAR", c126.getA_126_14()));
        if (c126.getB_126_0() != null)
            tbd.setTP_DISCO(Decodificatore.getCd_unimarc("TDIS", c126.getB_126_0()));
        if (c126.getB_126_1() != null)
            tbd.setTP_MATERIALE(Decodificatore.getCd_unimarc("TIMA", c126.getB_126_1()));
        if (c126.getB_126_2() != null)
            tbd.setTP_TAGLIO(Decodificatore.getCd_unimarc("TITA", c126.getB_126_2()));
        }
        C127 c127 = grafico.getT127(); //non obbligatorio
//        if (c127.getA_127_a() != null)
      if (c127 != null)
        	tbd.setDURATA(c127.getA_127_a());
        return tbd;
    }


    // Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
    // viene esteso anche al Materiale Moderno e Antico
    public void inserisciPersRapp(Tb_titolo titolo, DatiDocType datiDoc) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

      if (datiDoc instanceof ModernoType) {
          ModernoType moderno = (ModernoType) datiDoc;
      	C922 c922 = moderno.getT922();
      	if (c922 != null)
				creaTb_rappresentModerno(titolo, moderno);
      	C927[] c927 = moderno.getT927();
      	if (c927 != null)
      		creaTb_personaggioModerno(titolo, moderno);
      } else if (datiDoc instanceof AnticoType) {
          AnticoType antico = (AnticoType) datiDoc;
      	C922 c922 = antico.getT922();
      	if (c922 != null)
              creaTb_rappresentAntico(titolo, antico);
      	C927[] c927 = antico.getT927();
      	if (c927 != null)
      		creaTb_personaggioAntico(titolo, antico);
	    } else
          return;

  }

    protected void creaTb_rappresentModerno(Tb_titolo titolo, ModernoType moderno)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
	    Tb_rappresent tbr = preparaTb_rappresentModerno(titolo, moderno);
	    tbr.setUTE_INS(titolo.getUTE_INS());
	    Rappresentazione rappresentazione = new Rappresentazione();
	    rappresentazione.inserisci(tbr);
    }
    protected Tb_rappresent preparaTb_rappresentModerno(Tb_titolo titolo, ModernoType moderno)
    throws EccezioneSbnDiagnostico {
	    Tb_rappresent tbr = new Tb_rappresent();
	    C922 c922 = moderno.getT922();
	    tbr.setBID(titolo.getBID());
	    tbr.setUTE_VAR(titolo.getUTE_VAR());
	    tbr.setUTE_INS(titolo.getUTE_VAR());
	    tbr.setFL_CANC(" ");
	    tbr.setBID(titolo.getBID());
	    if (c922.getA_922() != null)
	        tbr.setTP_GENERE(Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()));
	    String errore = null;
	    if (c922.getP_922() != null && c922.getP_922().length() > 5)
	        errore = "P_922";
	    if (c922.getR_922() != null && c922.getR_922().length() > 30)
	        errore = "R_922";
	    if (c922.getS_922() != null && c922.getS_922().length() > 30)
	        errore = "S_922";
	    if (c922.getT_922() != null && c922.getT_922().length() > 138)
	        errore = "T_922";
	    if (c922.getQ_922() != null && c922.getQ_922().length() > 15)
	        errore = "Q_922";
	    if (errore != null) {
	        EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3219, "Lunghezza eccessiva");
	        ecc.appendMessaggio("Campo: " + errore);
	        throw ecc;
	    }
	    tbr.setAA_RAPP(c922.getP_922());
	    tbr.setDS_PERIODO(c922.getQ_922());
	    tbr.setDS_TEATRO(c922.getR_922());
	    tbr.setDS_LUOGO_RAPP(c922.getS_922());
	    tbr.setDS_OCCASIONE(c922.getU_922());
	    tbr.setNOTA_RAPP(c922.getT_922());
	    return tbr;
    }

    protected void creaTb_personaggioModerno(Tb_titolo titolo, ModernoType moderno) throws EccezioneDB, InfrastructureException {
        Tb_personaggio tbp;
        Personaggio personaggioDB = new Personaggio();
        C927[] c927 = moderno.getT927();
        Progressivi progressivi = new Progressivi();
        for (int i = 0; i < c927.length; i++) {
            tbp = preparaTb_personaggio(titolo, c927[i], progressivi.getNextIdPersonaggio());
            tbp.setUTE_INS(titolo.getUTE_INS());
            personaggioDB.inserisci(tbp);
            if (c927[i].getC3_927() != null) {
                Tr_per_int rel = new Tr_per_int();
                rel.setVID(c927[i].getC3_927());
                rel.setUTE_INS(titolo.getUTE_INS());
                rel.setUTE_VAR(titolo.getUTE_VAR());
                rel.setFL_CANC(" ");
                rel.setID_PERSONAGGIO(tbp.getID_PERSONAGGIO());
                personaggioDB.inserisciRelazione(rel);
            }
        }
    }

    protected void creaTb_rappresentAntico(Tb_titolo titolo, AnticoType antico)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
	    Tb_rappresent tbr = preparaTb_rappresentAntico(titolo, antico);
	    tbr.setUTE_INS(titolo.getUTE_INS());
	    Rappresentazione rappresentazione = new Rappresentazione();
	    rappresentazione.inserisci(tbr);
    }

    protected Tb_rappresent preparaTb_rappresentAntico(Tb_titolo titolo, AnticoType antico)
    throws EccezioneSbnDiagnostico {
	    Tb_rappresent tbr = new Tb_rappresent();
	    C922 c922 = antico.getT922();
	    tbr.setBID(titolo.getBID());
	    tbr.setUTE_VAR(titolo.getUTE_VAR());
	    tbr.setUTE_INS(titolo.getUTE_VAR());
	    tbr.setFL_CANC(" ");
	    tbr.setBID(titolo.getBID());
	    if (c922.getA_922() != null)
	        tbr.setTP_GENERE(Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()));
	    String errore = null;
	    if (c922.getP_922() != null && c922.getP_922().length() > 5)
	        errore = "P_922";
	    if (c922.getR_922() != null && c922.getR_922().length() > 30)
	        errore = "R_922";
	    if (c922.getS_922() != null && c922.getS_922().length() > 30)
	        errore = "S_922";
	    if (c922.getT_922() != null && c922.getT_922().length() > 138)
	        errore = "T_922";
	    if (c922.getQ_922() != null && c922.getQ_922().length() > 15)
	        errore = "Q_922";
	    if (errore != null) {
	        EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3219, "Lunghezza eccessiva");
	        ecc.appendMessaggio("Campo: " + errore);
	        throw ecc;
	    }
	    tbr.setAA_RAPP(c922.getP_922());
	    tbr.setDS_PERIODO(c922.getQ_922());
	    tbr.setDS_TEATRO(c922.getR_922());
	    tbr.setDS_LUOGO_RAPP(c922.getS_922());
	    tbr.setDS_OCCASIONE(c922.getU_922());
	    tbr.setNOTA_RAPP(c922.getT_922());
	    return tbr;
    }

    protected void creaTb_personaggioAntico(Tb_titolo titolo, AnticoType antico) throws EccezioneDB, InfrastructureException {
        Tb_personaggio tbp;
        Personaggio personaggioDB = new Personaggio();
        C927[] c927 = antico.getT927();
        Progressivi progressivi = new Progressivi();
        for (int i = 0; i < c927.length; i++) {
            tbp = preparaTb_personaggio(titolo, c927[i], progressivi.getNextIdPersonaggio());
            tbp.setUTE_INS(titolo.getUTE_INS());
            personaggioDB.inserisci(tbp);
            if (c927[i].getC3_927() != null) {
                Tr_per_int rel = new Tr_per_int();
                rel.setVID(c927[i].getC3_927());
                rel.setUTE_INS(titolo.getUTE_INS());
                rel.setUTE_VAR(titolo.getUTE_VAR());
                rel.setFL_CANC(" ");
                rel.setID_PERSONAGGIO(tbp.getID_PERSONAGGIO());
                personaggioDB.inserisciRelazione(rel);
            }
        }
    }

    protected Tb_musica preparaTb_musica_audio(Tb_titolo titolo, AudiovisivoType audiovisivoType) {
        Tb_musica tbm = new Tb_musica();
        tbm.setUTE_VAR(titolo.getUTE_VAR());
        tbm.setFL_CANC(" ");
        tbm.setBID(titolo.getBID());
        if (audiovisivoType.getLivelloAut()!=null) {
            tbm.setCD_LIVELLO(audiovisivoType.getLivelloAut().toString());
        }
        //Questi sono valori di "default"
        tbm.setFL_COMPOSITO(" ");
        tbm.setFL_PALINSESTO(" ");

        C128 c128 = audiovisivoType.getT128();
        if (c128 != null) {
            tbm.setDS_ORG_SINT(c128.getB_128());
            tbm.setDS_ORG_ANAL(c128.getC_128());
            if (c128.getD_128() != null)
                tbm.setTP_ELABORAZIONE(Decodificatore.getCd_tabella("Tb_musica", "tp_elaborazione", c128.getD_128()));
        }
        return tbm;
    }

    protected Tb_rappresent preparaTb_rappresent_audio(Tb_titolo titolo, AudiovisivoType audiovisivo)
    		throws EccezioneSbnDiagnostico {
	    Tb_rappresent tbr = new Tb_rappresent();
	    C922 c922 = audiovisivo.getT922();
	    tbr.setBID(titolo.getBID());
	    tbr.setUTE_VAR(titolo.getUTE_VAR());
	    tbr.setUTE_INS(titolo.getUTE_VAR());
	    tbr.setFL_CANC(" ");
	    tbr.setBID(titolo.getBID());
	    if (c922.getA_922() != null)
	        tbr.setTP_GENERE(Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()));
	    String errore = null;
	    if (c922.getP_922() != null && c922.getP_922().length() > 5)
	        errore = "P_922";
	    if (c922.getR_922() != null && c922.getR_922().length() > 30)
	        errore = "R_922";
	    if (c922.getS_922() != null && c922.getS_922().length() > 30)
	        errore = "S_922";
	    if (c922.getT_922() != null && c922.getT_922().length() > 138)
	        errore = "T_922";
	    if (c922.getQ_922() != null && c922.getQ_922().length() > 15)
	        errore = "Q_922";
	    if (errore != null) {
	        EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3219, "Lunghezza eccessiva");
	        ecc.appendMessaggio("Campo: " + errore);
	        throw ecc;
	    }
	    tbr.setAA_RAPP(c922.getP_922());
	    tbr.setDS_PERIODO(c922.getQ_922());
	    tbr.setDS_TEATRO(c922.getR_922());
	    tbr.setDS_LUOGO_RAPP(c922.getS_922());
	    tbr.setDS_OCCASIONE(c922.getU_922());
	    tbr.setNOTA_RAPP(c922.getT_922());
	    return tbr;
	}

    protected void creaTb_personaggio_audio(Tb_titolo titolo, AudiovisivoType audiovisivo) throws EccezioneDB, InfrastructureException {
        Tb_personaggio tbp;
        Personaggio personaggioDB = new Personaggio();
        C927[] c927 = audiovisivo.getT927();
        Progressivi progressivi = new Progressivi();
        for (int i = 0; i < c927.length; i++) {
            tbp = preparaTb_personaggio(titolo, c927[i], progressivi.getNextIdPersonaggio());
            tbp.setUTE_INS(titolo.getUTE_INS());
            personaggioDB.inserisci(tbp);
            if (c927[i].getC3_927() != null) {
                Tr_per_int rel = new Tr_per_int();
                rel.setVID(c927[i].getC3_927());
                rel.setUTE_INS(titolo.getUTE_INS());
                rel.setUTE_VAR(titolo.getUTE_VAR());
                rel.setFL_CANC(" ");
                rel.setID_PERSONAGGIO(tbp.getID_PERSONAGGIO());
                personaggioDB.inserisciRelazione(rel);
            }
        }
    }


    // Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro


    //  inserimento:
    // se presente sulla vista non inserire
    // (select from vl_sog_tit_bib where bid=BID cid=cid cd_polo=cd_polo cd_biblioteca=cd_biblioteca
    // altrimenti
    // inserisco con bib e polo operante

    public void creaLegameThesauro(Tb_titolo titolo, LegameElementoAutType leg, TimestampHash timeHash, SbnUserType sbnUser)
    throws IllegalArgumentException, InvocationTargetException, Exception {
	    ThesauroTitolo st = new ThesauroTitolo();

	    String bid = titolo.getBID();
	    String did = leg.getIdArrivo();
	    // Errore leggere polo e biblioteca dell'utente che invia i dati

	    String cd_polo = sbnUser.getBiblioteca().substring(0,3);
	    String cd_biblioteca = sbnUser.getBiblioteca().substring(3,6);

	    List vl = st.verificaEsistenzainVista(bid,did,cd_polo,cd_biblioteca);
	    if (vl.size() > 0) {
	    	throw new EccezioneSbnDiagnostico(3030, "Legame del titolo esistente");

	    }
	    else {
	    	TermineThesauro termineThesauro = new TermineThesauro();
	    	//Soggetto soggetto = new Soggetto();
	    	Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
	    	//Tb_soggetto tb_soggetto = new Tb_soggetto();

	    	tb_termine_thesauro =  termineThesauro.cercaTerminePerId(leg.getIdArrivo());
			if(tb_termine_thesauro != null) {
				Trs_termini_titoli_biblioteche ts = new Trs_termini_titoli_biblioteche();
		        ts.setDID(leg.getIdArrivo());
		        ts.setBID(titolo.getBID());
		        ts.setFL_CANC(" ");

		        //almaviva5_20120517 #4245
				ts.setUTE_INS(titolo.getUTE_VAR());
		        ts.setUTE_VAR(titolo.getUTE_VAR());
		        // ts.setUTE_INS(sbnUser.getUserId());
		        // ts.setUTE_VAR(sbnUser.getUserId());
		        ts.setCD_POLO(cd_polo);
		        ts.setCD_BIBLIOTECA(cd_biblioteca);
		        ts.setCD_THE(tb_termine_thesauro.getCD_THE());
		        ts.setNOTA_TERMINE_TITOLI_BIBLIOTECA(leg.getNoteLegame());
		        st.inserisci(ts);
			}

	    }
    }

}
