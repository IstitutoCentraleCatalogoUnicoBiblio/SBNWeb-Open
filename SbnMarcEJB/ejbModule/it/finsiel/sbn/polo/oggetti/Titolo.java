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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_bibResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_titResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_bResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.NormalizzaSequenza;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.viste.V_daticomuni;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltriDatiComuniCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author
 * @version 23/12/2002
 */
public class Titolo extends Tb_titolo {

	private static final long serialVersionUID = 1852377077081184369L;
	protected Connection db_conn = null;
    boolean filtriValorizzati = false;
    boolean esporta = false;
    private static Logger log = Logger.getLogger("iccu.serversbnmarc.Titolo");

	private static final Comparator<Vl_titolo_tit_c> ORDINAMENTO_SEQUENZA = new Comparator<Vl_titolo_tit_c>() {
		@Override
		public int compare(Vl_titolo_tit_c n1, Vl_titolo_tit_c n2) {
			//ORDER BY this_.TP_LEGAME, this_.CD_NATURA_BASE DESC, this_.SEQUENZA
			int cmp	= n1.getTP_LEGAME().compareTo(n2.getTP_LEGAME());
			cmp = cmp != 0 ? cmp : -(n1.getCD_NATURA_BASE().compareTo(n2.getCD_NATURA_BASE())); // desc
			if (cmp == 0) {
				final String s1 = NormalizzaSequenza.normalizza(n1.getSEQUENZA());
				final String s2 = NormalizzaSequenza.normalizza(n2.getSEQUENZA());
				cmp = s1.compareTo(s2);
			}
			return cmp;
		}
	};

    /** Costruttore, la connessione al db serve per tutti gli accessi al DB */
    public Titolo() {
    }


    public void setEsporta(boolean export) {
        esporta = export;
    }

    /**
     * Perpara il formato del ts_var per i confronti
     * (Utilizzato solo da esporta)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void settaFormatoTsVar () throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult();
        tavola.executeCustom("settaFormatoTsVar");
    }
    /**
     * Metodo per cercare il titolo con numero di identificativo:
     * ricerca su Tb_titolo con ID.
     * @return il titolo trovato oppure null
     * @throws InfrastructureException
     */
    public Tb_titolo estraiTitoloPerID(String id) throws EccezioneDB, InfrastructureException {
        TableDao tavola = cercaTitoloPerID(id);
        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Tb_titolo) v.get(0);
        else
            return null;
    }

    /**
     * Metodo per cercare il titolo con numero di identificativo:
     * ricerca su Tb_titolo con ID.
     * @return il titolo trovato oppure null
     * @throws InfrastructureException
     */
    public TableDao cercaTitoloPerID(String id) throws EccezioneDB, InfrastructureException {
        setBID(id);
        Tb_titoloResult tavola = new Tb_titoloResult(this);

        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        return tavola;
    }

    /** Valorizza i filtri in base al contenuto del CercaAutoreType */
    public Tb_titolo valorizzaFiltri(CercaDatiTitType cerca) throws EccezioneSbnDiagnostico {
        return valorizzaFiltri(this, cerca);
    }

    // Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico e dati comuni (Aea0)
	/** Valorizza i filtri in base al contenuto del CercaAutoreType Modifice per superindex*/
	public boolean valorizzaFiltriPerExport(CercaDatiTitType cerca) throws EccezioneSbnDiagnostico {
		return valorizzaFiltriPerExport(this, cerca);
	}


    /** Valorizza i filtro natura */
    public Tb_titolo valorizzaFiltri(String natura) throws EccezioneSbnDiagnostico {
        filtriValorizzati = true;
        this.settaParametro(TableDao.XXXcd_natura1, natura);
        return this;
    }

	/** Valorizza i filtri di titolo in base al contenuto del CercatitoloType SuperIndex*/

    // FEBBRAIO 2015 ASTERISCATO PERCHE' NON UTILIZZATO DAL POLO - si forza il ritorno valore false
	public boolean valorizzaFiltriPerExport(Tb_titolo titolo, CercaDatiTitType cerca) throws EccezioneSbnDiagnostico {
		boolean ricercaTs_superindex = true;

		filtriValorizzati = true;
		if (cerca == null){
			ricercaTs_superindex = false;
			return ricercaTs_superindex;
		}

		if (cerca.getCercaDatiTitTypeChoice() != null) {
			TitoloCercaType titCerca = cerca.getCercaDatiTitTypeChoice().getTitoloCerca();
			if (titCerca != null) {
				if (titCerca.getEditoreKey() != null){
					ricercaTs_superindex = false;
					return ricercaTs_superindex;
				}
				if (titCerca.getTitoloCLET() != null){
					ricercaTs_superindex = false;
					return ricercaTs_superindex;
				}
				if (titCerca.getStringaCerca().toString() != "") {


					// Inizio Evolutiva febbraio 2015: MODIFICA PER COMPATIBILITA' NUOVO SCHEMA 2.0
					// viene s0stituito titCerca.getStringaCerca() con il nuovo campo stringaCercaTypeChoice
					StringaCercaTypeChoice stringaCercaTypeChoice = titCerca.getStringaCerca().getStringaCercaTypeChoice();

					String stringa = stringaCercaTypeChoice.getStringaEsatta();
					if((stringaCercaTypeChoice.getStringaEsatta() != null) && stringa.length() >0 ){
						ricercaTs_superindex = false;
						return ricercaTs_superindex;
					}
					stringa = stringaCercaTypeChoice.getStringaLike();
					if((stringaCercaTypeChoice.getStringaLike() != null) && stringa.length() >0 ){
						ricercaTs_superindex = false;
						return ricercaTs_superindex;
					}
				}
			}
		}
		GuidaDoc[] guida = cerca.getGuida();
		if (guida != null && guida.length > 0)
			for (int i = 0; i < guida.length; i++) {
				if (guida[i].getTipoRecord() != null) {
					titolo.settaParametro("XXXtp_record_uni" + (i + 1),
						guida[i].getTipoRecord().toString());
				}
			}
		SbnRangeDate t005_range = cerca.getT005_Range();
		if (t005_range != null) {
			ricercaTs_superindex = false;
			return ricercaTs_superindex;
		}
		C100 t100_a = cerca.getT100_A();
		C100 t100_da = cerca.getT100_Da();
		if (t100_a != null && t100_da != null) {
			if (t100_a.getA_100_0() != null && t100_da.getA_100_0() != null) {
				ricercaTs_superindex = false;
				return ricercaTs_superindex;
			}
			if (t100_a.getA_100_8() != null && t100_da.getA_100_8() != null) {
				ricercaTs_superindex = false;
				return ricercaTs_superindex;
			}

		}
		if (cerca.getT101() != null) {
			String[] t101 = cerca.getT101().getA_101();
			if (t101.length > 1){
				ricercaTs_superindex = false;
				return ricercaTs_superindex;
			}
			if (t101.length > 2){
				ricercaTs_superindex = false;
				return ricercaTs_superindex;
			}

		}
		if (cerca.getT102() != null) {
			//titolo.setCd_paese(cerca.getT102().getA_102());
		}

		if (cerca.getLivelloAut_Da() != null){
			ricercaTs_superindex = false;
			return ricercaTs_superindex;
		}

		if (cerca.getLivelloAut_A() != null){
			ricercaTs_superindex = false;
			return ricercaTs_superindex;
		}
		if (cerca.getT102() != null){
			// TRUE
		}

		return ricercaTs_superindex;


	}


    /** Valorizza i filtri di titolo in base al contenuto del CercatitoloType */
    public Tb_titolo valorizzaFiltri(Tb_titolo titolo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        filtriValorizzati = true;
        titolo.leggiAllParametro().putAll(this.leggiAllParametro());
        if (cerca == null)
            return titolo;
        if (cerca.getCercaDatiTitTypeChoice() != null) {
            TitoloCercaType titCerca = cerca.getCercaDatiTitTypeChoice().getTitoloCerca();
            if (titCerca != null) {
                if (titCerca.getEditoreKey() != null)
                    titolo.setKY_EDITORE(Formattazione.formatta(titCerca.getEditoreKey()));
                if (titCerca.getTitoloCLET() != null)
                    titolo.settaParametro(TableDao.XXXstringaClet, Formattazione.formatta(titCerca.getTitoloCLET()));
                if (titCerca.getStringaCerca() != null) {
                    String stringa = titCerca.getStringaCerca().getStringaCercaTypeChoice().getStringaEsatta();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(stringa);
                        if (stringa.length() > 6) {
                            titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa.substring(0, 6));
                            titolo.settaParametro(TableDao.XXXstringaEsatta2, stringa.substring(6));
                        } else {
                            titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa);
                            titolo.settaParametro(TableDao.XXXstringaEsatta2, " ");
                        }
                    }
                    if (titCerca.getStringaCerca().getStringaCercaTypeChoice().getStringaLike() != null) {
                        stringa = Formattazione.formatta(titCerca.getStringaCerca().getStringaCercaTypeChoice().getStringaLike());
                        if (stringa.length() > 6) {
                            titolo.settaParametro(TableDao.XXXstringaLike1, stringa.substring(0, 6));
                            titolo.settaParametro(TableDao.XXXstringaLike2, stringa.substring(6));
                        } else
                            titolo.settaParametro(TableDao.XXXstringaLike1, stringa);
                    }
                    String clet2 = new ChiaviTitolo().estraiClet2(stringa);
                    titolo.settaParametro(TableDao.XXXclet2_ricerca,clet2.trim());
                }
            }
        }
        String[] naturaTit = cerca.getNaturaSbn();
        if (naturaTit != null && naturaTit.length > 0) {
            for (int i = 0; i < naturaTit.length; i++)
                titolo.settaParametro("XXXcd_natura" + (i + 1), naturaTit[i]);
        }
        SbnMateriale tipoMateriale[] = cerca.getTipoMateriale();
        if (tipoMateriale != null) {
            for (int i = 0; i < tipoMateriale.length; i++) {
                if (tipoMateriale[i].toString().equals("E")) {
                    if (naturaTit != null) {
                        for (int c=0;c<naturaTit.length;c++) {
                            if (naturaTit[c].equals("S"))
                                throw new EccezioneSbnDiagnostico(3275);
                        }
                    }
                }
                titolo.settaParametro("XXXtp_materiale" + (i + 1), tipoMateriale[i].toString());
            }
        }
        GuidaDoc[] guida = cerca.getGuida();
        if (guida != null && guida.length > 0)
            for (int i = 0; i < guida.length; i++) {
                if (guida[i].getTipoRecord()!=null) {
                    titolo.settaParametro("XXXtp_record_uni" + (i + 1), guida[i].getTipoRecord().toString());
                }
                // ?? titolo.settaParametro(TableDao.XXX??" + (i + 1), guida[i].getLivelloBibliografico().toString());
                // ?? titolo.settaParametro(TableDao.XXX??" + (i + 1), guida[i].getStatoRecord().toString());
            }
        SbnRangeDate t005_range = cerca.getT005_Range();
        if (t005_range != null) {
            if (!cerca.getT005_Range().hasTipoFiltroDate()
                || cerca.getT005_Range().getTipoFiltroDate() == 1) {
                if (esporta) {
                    titolo.settaParametro(TableDao.XXXesporta_ts_var_da,t005_range.getDataDa().toString() + " 0000000");
                    titolo.settaParametro(TableDao.XXXesporta_ts_var_a, t005_range.getDataA().toString() + " 2359599");
                } else {
                    titolo.settaParametro(TableDao.XXXts_var_da, t005_range.getDataDa().toString());
                    titolo.settaParametro(TableDao.XXXts_var_a, t005_range.getDataA().toString());
                }
            } else {
                // ts_ins c'è già il t100 ?
                //titolo.settaParametro(TableDao.XXXts_ins_da", t005_range.getDataDa().toString());
                //titolo.settaParametro(TableDao.XXXts_ins_a", t005_range.getDataA().toString());
                if (esporta) {
                    titolo.settaParametro(TableDao.XXXesporta_ts_var_e_ts_ins_da,
                        t005_range.getDataDa().toString() + " 0000000");
                    titolo.settaParametro(TableDao.XXXesporta_ts_var_a, t005_range.getDataA().toString() + " 2359599");
                }
            }
        }

        C100 t100_a = cerca.getT100_A();
        C100 t100_da = cerca.getT100_Da();
        if (t100_a != null && t100_da != null) {
            if (t100_a.getA_100_0() != null && t100_da.getA_100_0() != null) {
                titolo.settaParametro(TableDao.XXXts_ins_a, t100_a.getA_100_0().toString());
                titolo.settaParametro(TableDao.XXXts_ins_da, t100_da.getA_100_0().toString());
            }
            if (t100_a.getA_100_8() != null && t100_da.getA_100_8() != null) {
                //se sono diversi i tipi di data si arrabbia
                if (!t100_da.getA_100_8().equals(t100_a.getA_100_8()))
                    throw new EccezioneSbnDiagnostico(3041, "Tipi di date diversi");
                String tp_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100_da.getA_100_8().toString());
                if (tp_data == null || tp_data.length()==0) {
                    throw new EccezioneSbnDiagnostico(3276, "Tipo data non valido");
                }
                char tipoData = tp_data.charAt(0);
                if (tipoData == 'A' || tipoData =='B') {
                    if (naturaTit != null) {
                        for (int i=0;i<naturaTit.length;i++) {
                            if (!naturaTit[i].equals("S") && !naturaTit[i].equals("C"))
                                throw new EccezioneSbnDiagnostico(3191);
                        }
                    }
                } else if (tipoData == 'D' || tipoData =='E' || tipoData =='R' || tipoData =='G') {
                    if (naturaTit != null) {
                        for (int i=0;i<naturaTit.length;i++) {
                            if (!naturaTit[i].equals("M") && !naturaTit[i].equals("W"))
                                throw new EccezioneSbnDiagnostico(3191);
                        }
                    }
                }
                titolo.setTP_AA_PUBB(""+tipoData);
            }
            if (t100_a.getA_100_9() != null && t100_da.getA_100_9() != null) {
                if (t100_a.getA_100_9().length() != t100_da.getA_100_9().length())
                    throw new EccezioneSbnDiagnostico(3042);
                if (t100_a.getA_100_9().length() < 4) {
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_like, t100_da.getA_100_9());
                } else {
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_da, t100_da.getA_100_9());
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_a, t100_a.getA_100_9());
                }
            }
            if (t100_a.getA_100_13() != null && t100_da.getA_100_13() != null) {
                if (t100_a.getA_100_13().length() != t100_da.getA_100_13().length())
                    throw new EccezioneSbnDiagnostico(3042);
                if (t100_a.getA_100_13().length() < 4) {
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_like, t100_da.getA_100_13());
                } else {
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_da, t100_da.getA_100_13());
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_a, t100_a.getA_100_13());
                }
            }
        } else if (t100_a != null || t100_da != null) {
            throw new EccezioneSbnDiagnostico(3041);
        }
        if (cerca.getT101() != null) {
            String[] t101 = cerca.getT101().getA_101();
            if (t101.length > 0)
                titolo.setCD_LINGUA_1(t101[0]);
            if (t101.length > 1)
                titolo.setCD_LINGUA_2(t101[1]);
            if (t101.length > 2)
                titolo.setCD_LINGUA_3(t101[2]);
        }
        if (cerca.getT102() != null) {
            titolo.setCD_PAESE(cerca.getT102().getA_102());
        }
        if (cerca.getT105() != null) {
            String[] t105 = cerca.getT105().getA_105_4();
            if (t105.length > 0)
                titolo.setCD_GENERE_1(t105[0]);
            if (t105.length > 1)
                titolo.setCD_GENERE_2(t105[1]);
            if (t105.length > 2)
                titolo.setCD_GENERE_3(t105[2]);
            if (t105.length > 3)
                titolo.setCD_GENERE_4(t105[3]);
        }

	     // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
	     // e opportunamente salavto sulla tb_titolo campo ky_editore
        if (cerca.getParoleEditore() != null) {
            String[] ped = cerca.getParoleEditore();
            if (ped != null && ped.length > 0) {
                if (naturaTit != null && naturaTit.length>0) {
                    for (int c=0;c<naturaTit.length;c++) {
                        if (!naturaTit[c].equals("C"))
                            throw new EccezioneSbnDiagnostico(2900, "Ricerca con chiave editore consentita solo con filtro natura 'C'");
                    }
                } else
                    throw new EccezioneSbnDiagnostico(2900, "Ricerca con chiave editore consentita solo con filtro natura 'C'");

                for (int i = 0; i < ped.length; i++)
                    //titolo.settaParametro("ky_editore" + (i + 1), ped[i].toUpperCase());
                	titolo.settaParametro(TableDao.XXXky_editore, ped[i].toUpperCase());
            }
        }
        // Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore

        if (cerca.getLivelloAut_Da() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_da, Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_a, cerca.getLivelloAut_A().toString());
        if (cerca.getT102() != null)
            titolo.setCD_PAESE(cerca.getT102().getA_102());
        if (cerca instanceof CercaDocAnticoType) {
            valorizzaFiltriAntico(titolo, (CercaDocAnticoType) cerca);
        }

        //almaviva5_20150303
        if (titolo instanceof V_daticomuni) {
            FiltriDatiComuniCercaType filtri = cerca.getFiltriDatiComuniCerca();
            if (filtri != null) {
                if (filtri.getA_105_11() != null)
                    titolo.settaParametro(TableDao.XXXtestoLetterarioModerno, Decodificatore.getCd_tabella("COLM", filtri.getA_105_11()));
                if (filtri.getA_140_17() != null)
                    titolo.settaParametro(TableDao.XXXtestoLetterarioAntico, Decodificatore.getCd_tabella("COLA", filtri.getA_140_17()));
                if (filtri.getA_181_0() != null)
                    titolo.settaParametro(TableDao.XXXformaContenuto, Decodificatore.getCd_tabella("FOCO", filtri.getA_181_0()));
                if (filtri.getA_182_0() != null)
                    titolo.settaParametro(TableDao.XXXtipoMediazione, Decodificatore.getCd_tabella("MEDI", filtri.getA_182_0()));
            }
		}

        return titolo;
    }

    protected void valorizzaFiltriAntico(Tb_titolo titolo, CercaDocAnticoType cerca) {
        if (cerca.getT140() != null) {

            //Attualmente cd natura non compare nel xml
            titolo.setCD_NATURA("E");
            String[] t140 = cerca.getT140().getA_140_9();
            if (t140.length > 0)
                titolo.setCD_GENERE_1(t140[0]);
            if (t140.length > 1)
                titolo.setCD_GENERE_2(t140[1]);
            if (t140.length > 2)
                titolo.setCD_GENERE_3(t140[2]);
            if (t140.length > 3)
                titolo.setCD_GENERE_4(t140[3]);
        }

    }
    protected void valorizzaFiltriModerno(Tb_titolo titolo, CercaDocMusicaType cerca) {

    }
    /** Per le richieste di tipo musicale si devono settare dei filtri diversi */
    protected Tb_titolo valorizzaFiltriMusica(Tb_titolo titolo, CercaDocMusicaType cerca) {
        //if (cerca.getT125()!=null)
        //titolo.settpTestoLEtterario();
        //        if (cerca.getT128() != null) {
        //            if (cerca.getT128().getB_128Count()>0)
        //            titolo.set();
        //            if (cerca.getT128().getC_128Count()>0)
        //            titolo.set();
        //            if (cerca.getT128().getD_128() != null)
        //            titolo.set();
        //        }
        //923

        return titolo;
    }

    /** Legge da una tavola il valore del COUNT(*)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int conta(TableDao tavola, String nome_statement) throws IllegalArgumentException, InvocationTargetException, Exception {

        tavola.executeCustom(nome_statement);
        return tavola.getCount();
//        int n;
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            n = rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del SELECT COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
//
//        return n;
    }

    public void inserisci(Tb_titolo titolo) throws EccezioneDB, InfrastructureException {
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);
        tavola.insert(titolo);

    }

    /** Esegue una ricerca per i legami tra documenti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaLegamiDocumentoNonTitUni(Tb_titolo titolo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_tit = new Vl_titolo_tit_b();
        tit_tit.setBID_BASE(titolo.getBID());
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_tit);


        tavola.executeCustom("selectDocumentoPerLegame", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }

    /** Esegue una ricerca per i legami tra documenti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaLegamiDocumento(Tb_titolo titolo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola = cercaLegamiDocumento(titolo.getBID(), ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }

    /** Esegue una ricerca per i legami tra documenti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaTuttiLegamiDocumento(Tb_titolo titolo,boolean versoBasso) throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavola = cercaLegamiDocumento(titolo.getBID(), "order_tp_legame");
        List v = tavola.getElencoRisultati();


        // Modifica almaviva2 09.03.2011 BUG MANTIS Collaudo 4294 la prospettazione dei legami a scendere si deve fares sia per
        // le nature M che per le W (esempio per gli spogli)
        //bug mantis 3101 di Indice RIPORTATO su Protocollo di Polo
		//if (versoBasso && titolo.getCD_NATURA().equals("M")) {
        if (versoBasso && ValidazioneDati.in(titolo.getCD_NATURA(), "M", "W")) {
            List v2 = cercaLegamiDocumentoVersoBasso(titolo.getBID());
            // almaviva5_20210305 ordinamento per sequenza
            Collections.sort(v2, ORDINAMENTO_SEQUENZA);
            v.addAll(v2);
        }
        return v;
    }

    public int contaLegamiVersoBasso(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_c tit_tit = new Vl_titolo_tit_c();
        tit_tit.setBID_COLL(id);
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit_tit);
        return conta(tavola, "countPerDocumento");

    }

    /** Esegue una ricerca per i legami tra documenti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaLegamiDocumentoVersoBasso(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_c tit_tit = new Vl_titolo_tit_c();
        tit_tit.setBID_COLL(id);
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit_tit);


        tavola.executeCustom("selectLegamiDiMonografie", "order_tp_legame");
        List v = tavola.getElencoRisultati();

        return v;
    }

    /** Esegue una ricerca per i legami tra documenti
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaLegamiDocumento(String id, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_tit = new Vl_titolo_tit_b();
        tit_tit.setBID_BASE(id);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_tit);


        tavola.executeCustom("selectTitoloPerLegame", ordinamento);
        return tavola;
    }

    /**
    * this.getAutori_no_raffinamento(),
    * "XXXMar123456", //cd utente che ha richiesto l'operazione
    * "001", //tipo di output lista sintetica 001=MAX (SEMPRE)
    * "1", //tipo ordinamento (da 1 a 5)
    * "BIS014", // Tipo report realizzato
    * id_processo+"_SBNMARC", // parte iniziale nome file sequenza da generare
    * "100") ; // Numero massimo di righe per blocco
    */
    /** TODO Antonio
    public void realizzaReport(
        List titoli,
        SbnUserType user,
        String tipoOut,
        String tipoOrd,
        String tipoReport,
        String nomeFile,
        int maxRighe) {
        int elementCounter = 0;
        SbnTipoOutput sbnOut = SbnTipoOutput.valueOf(tipoOut);
        SbnTipoOrd sbnOrd = SbnTipoOrd.valueOf(tipoOrd);
        BigDecimal schemaVersion = new BigDecimal(IndiceServiceLocator.getProperty("SCHEMA_VERSION"));
        ProcessoInDifferita processo_in_differita = new ProcessoInDifferita();
        String xml;
        int numeroFile = 1;
        for (; elementCounter < titoli.size(); elementCounter += maxRighe) {
            StringWriter sw = new StringWriter();
            try {
                FormatoTitolo
                    .formattaLista(
                        titoli,
                        user,
                        sbnOut,
                        sbnOrd,
                        "BIS" + nomeFile.substring(0, nomeFile.indexOf("_")),
                        elementCounter,
                        maxRighe,
                        titoli.size(),
                        schemaVersion,
                        null,
                        null,
                        true)
                    .marshal(sw);
                xml = sw.toString();
            } catch (EccezioneIccu ecc) {
                log.error("Eccezione nella preparazione della lista di autori:" + ecc);
                xml = FormatoErrore.preparaMessaggioErrore(ecc, user);
            } catch (IllegalArgumentException ecc) {
                log.error("Eccezione argomenti:" + ecc);
                xml = FormatoErrore.preparaMessaggioErrore(-1, user);
            } catch (MarshalException e) {
                log.error("Errore marshalling", e);
                xml = FormatoErrore.preparaMessaggioErrore(101, user);
            } catch (ValidationException e) {
                log.error("Errore marshalling", e);
                xml = FormatoErrore.preparaMessaggioErrore(101, user);
            }
            processo_in_differita.putFile(nomeFile + numeroFile + ".xml", xml);
            numeroFile++;
        }
    }
    */
//TODO almaviva eliminata
//    public void realizzaReport(
//        VectorFileTitolo titoli,
//        String utente,
//        String tipoOut,
//        String tipoOrd,
//        String tipoReport,
//        String nomeFile,
//        int maxRighe) throws EccezioneIccu{
//        SbnUserType user = new SbnUserType();
//        user.setBiblioteca(utente.substring(0, 6));
//        user.setUserId(utente.substring(6));
//        SbnTipoOutput sbnOut = SbnTipoOutput.valueOf(tipoOut);
//        SbnTipoOrd sbnOrd = SbnTipoOrd.valueOf(tipoOrd);
//        BigDecimal schemaVersion = new BigDecimal(IndiceServiceLocator.getProperty("SCHEMA_VERSION"));
//        ProcessoInDifferita processo_in_differita = new ProcessoInDifferita();
//        String xml;
//        int numeroFile = 0;
//        for (int elementCounter = 0; elementCounter < titoli.size(); elementCounter += maxRighe) {
//            StringWriter sw = new StringWriter();
//            try {
//                FormatoTitolo
//                    .formattaVectorFile(
//                        db_conn,
//                        titoli,
//                        user,
//                        sbnOut,
//                        sbnOrd,
//                        nomeFile.substring(0, nomeFile.indexOf("_")),
//                        elementCounter,
//                        maxRighe,
//                        titoli.size(),
//                        schemaVersion,
//                        null,
//                        null)
//                    .marshal(sw);
//                xml = sw.toString();
//            } catch (EccezioneIccu ecc) {
//                log.error("Eccezione nella preparazione della lista di titoli:" + ecc);
//                if (ecc.getErrorID() == -1) {
//                	throw ecc;
//                } else {
//                	xml = FormatoErrore.preparaMessaggioErrore(ecc, user);
//                }
//            } catch (IllegalArgumentException ecc) {
//                log.error("Eccezione argomenti:" + ecc);
//                xml = FormatoErrore.preparaMessaggioErrore(-1, user);
//            } catch (MarshalException e) {
//                log.error("Errore marshalling", e);
//                xml = FormatoErrore.preparaMessaggioErrore(101, user);
//            } catch (ValidationException e) {
//                log.error("Errore marshalling", e);
//                xml = FormatoErrore.preparaMessaggioErrore(101, user);
//            }
//            processo_in_differita.putFile(nomeFile + numeroFile + ".xml", xml);
//            numeroFile++;
//        }
//    }

    /**
     * Method cercaMonografiaSuperiore.
     * @param string
     * @return Vl_titolo_tit_b
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Vl_titolo_tit_b cercaMonografiaSuperiore(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_tit = new Vl_titolo_tit_b();
        tit_tit.setBID_BASE(bid);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_tit);

        // Inizio Modifica almaviva2 BUG MANTIS 4501 esercizio : inserita nuova select VL_TITOLO_TIT_B_selectMonografiaSuperioreBis
		// per consentire doppio legame M01S  (vedi commento almaviva4 01/08/2011 su software di Indice)
        // tavola.executeCustom("selectMonografiaSuperiore");
        tavola.executeCustom("selectMonografiaSuperioreBis");

        List v = tavola.getElencoRisultati();

        if (v.size() > 0)
            return (Vl_titolo_tit_b) v.get(0);
        else
            return null;

    }

	public List<Vl_titolo_tit_b> cercaTitoloSuperiore(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_titolo_tit_b tit_tit = new Vl_titolo_tit_b();
		tit_tit.setBID_BASE(bid);
		Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_tit);

		return tavola.cercaTitoloSuperiore(bid);
	}
    /**
     * Method eseguiUpdate.
     * @param autore
     * @throws InfrastructureException
     */
    public void eseguiUpdate(Tb_titolo titolo) throws EccezioneDB, InfrastructureException {
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);


        tavola.update(titolo);

    }

    /**
     * Method eseguiUpdate.
     * @param autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void updateChiaviEIsbd(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);
        tavola.executeCustom("updateChiaviEIsbd");
    }

    public void cancellaTitolo(Tb_titolo titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);
        tavola.executeCustom("updateCancellaTitolo");
    }

    public void updateVersione(String id, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(id);
        setUTE_VAR(ute_var);
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        tavola.executeCustom("updateTitolo");
    }

    /** Estrae da DB un legame tra due titoli.
     * ATTENZIONE Anche tp_legame sarebbe una chiave.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * */
    public Tr_tit_tit estraiLegame(String bid_base, String bid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {


        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_BASE(bid_base);
        tt.setBID_COLL(bid_coll);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);
        tabella.executeCustom("selectPerBidBaseEColl");
        List v = tabella.getElencoRisultati();

        if (v.size() > 0)
            return (Tr_tit_tit) v.get(0);
        return null;
    }

    /*  legge su tr_tit_tit per ricavare il livello gerarchico
     * prima count sul bid_coll
     * se 0 ---> codice 0 = bid senza legami
     * altrimenti
     * count su bid_base
     * se 0 ---> codice 1 = bid padre
     * se > 0 ---> codice 2 = presenza legami superiori e/o inferiori */
    public String contaBidPerGerarchia(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_tit_tit tt = new Tr_tit_tit();
        tt.setBID_COLL(bid);
        Tr_tit_titResult tabella = new Tr_tit_titResult(tt);


        tabella.executeCustom("countPerLivelloColl");
        int n1 = conta(tabella);

        tt = new Tr_tit_tit();
        tt.setBID_BASE(bid);
        tabella = new Tr_tit_titResult(tt);


        tabella.executeCustom("countPerLivelloBase");
        int n2 = conta(tabella);

        // senza livelli
        if (n1 == 0 && n2 == 0)
            return "0";
        // livello intermedio
        if (n1 > 0 && n2 > 0)
            return "3";
        // livello più alto
        if (n1 > 0 && n2 == 0)
            return "1";
        // livello base: n1 == 0 e n2 > 0
        return "2";
    }
    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }

    public TableDao estraiVariatiOpac(String ts_start,String ts_end, SbnNaturaDocumento[] cdNatura, SbnMateriale[] materiale) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo titolo = new Tb_titolo();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,
            ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");
        if (cdNatura != null) {
            for (int i=0;i<cdNatura.length;i++) {
                titolo.settaParametro("XXXnatura"+i, ""+cdNatura[i]);
            }
        }
        if (materiale != null) {
            for (int i=0;i<materiale.length;i++) {
                titolo.settaParametro("XXXmateriale"+i, ""+materiale[i]);
            }
        }
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);

        tavola.executeCustom("selectPerTsVarOpac");
        return tavola;
    }

    public TableDao estraiInseritiOpac(String ts_start,String ts_end,SbnNaturaDocumento[] cdNatura, SbnMateriale[] materiale) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo titolo = new Tb_titolo();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,
            ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");

        // MARCO RANIERI MODIFICA:
        // INTRODUCO PARAMETRI:codice Natura + materiale
        if (cdNatura != null) {
            for (int i=0;i<cdNatura.length;i++) {
                titolo.settaParametro("XXXnatura"+i, ""+cdNatura[i]);
            }
        }
        if (materiale != null) {
            for (int i=0;i<materiale.length;i++) {
                titolo.settaParametro("XXXmateriale"+i, ""+materiale[i]);
            }
        }

        Tb_titoloResult tavola = new Tb_titoloResult(titolo);


        tavola.executeCustom("selectPerTsInsOpac");
        return tavola;
    }

    public TableDao estraiCancellatiTavola(String ts_start,String ts_end, boolean opac,SbnMateriale[] materiale) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo titolo = new Tb_titolo();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,
            ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");

        // MARCO RANIERI MODIFICA:
        // INTRODUCO PARAMETRO materiale
        if (materiale != null) {
            for (int i=0;i<materiale.length;i++) {
                titolo.settaParametro("XXXmateriale"+i, ""+materiale[i]);
            }
        }

        if (opac)
            titolo.settaParametro(TableDao.XXXOPAC,"OK");
        else
            titolo.settaParametro(TableDao.XXXNOOPAC,"OK");
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);


        tavola.executeCustom("selectCancellatiPerTsVar");
        return tavola;
    }

    public TableDao estraiFusiTavola(String ts_start,String ts_end, SbnMateriale[] materiale) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo titolo = new Tb_titolo();
        titolo.settaParametro(TableDao.XXXesporta_ts_var_da,
            ts_start + " 0000000");
        titolo.settaParametro(TableDao.XXXesporta_ts_var_a, ts_end + " 2359599");
        if (materiale != null) {
            for (int i=0;i<materiale.length;i++) {
                titolo.settaParametro("XXXmateriale"+i, ""+materiale[i]);
            }
        }
        Tb_titoloResult tavola = new Tb_titoloResult(titolo);

        tavola.executeCustom("selectFusiPerTsVar");
        return tavola;
    }

    public List estraiCancellati(String ts_start,String ts_end, boolean opac) throws IllegalArgumentException, InvocationTargetException, Exception {
        try {
        TableDao tavola = estraiCancellatiTavola(ts_start,ts_end, opac, null);
        List v = tavola.getElencoRisultati();

        return v;
        } catch (EccezioneDB ecc) {
            log.error("Errore",ecc);
            return null;
        }
    }
    public void verificaAllineamentoModificaTitolo(String bid)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    Tr_tit_bib tb = new Tr_tit_bib();
    tb.setBID(bid);
    tb.setFL_ALLINEA("X");
    Tr_tit_bibResult res = new Tr_tit_bibResult(tb);


    res.executeCustom("selectPerFlagAllinea");
    if (res.getElencoRisultati().size()>0) {
        throw new EccezioneSbnDiagnostico(3311,"In allineamento");
    }
}

    /**
     * Rimuovi i trattini da una stringa.
     * @param stringa
     * @return
     */
    public String rimuoviTrattini(String stringa) {
        int n;
        while ((n = stringa.indexOf("-")) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        //Rimuvo anche gli spazi perchè nell'import ci sono dei file sporchi
        while ((n = stringa.indexOf(" ")) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        return stringa;
    }

 // Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico e dati comuni (Aea0)
    public V_daticomuni valorizzaFiltriCom(V_daticomuni titolo, CercaDatiTitType cerca)
    	throws EccezioneSbnDiagnostico {
        filtriValorizzati = true;
        if (cerca == null)
            return titolo;
        if (cerca.getCercaDatiTitTypeChoice() != null) {
            TitoloCercaType titCerca = cerca.getCercaDatiTitTypeChoice().getTitoloCerca();
            if (titCerca != null) {
                if (titCerca.getEditoreKey() != null)
                    titolo.setKY_EDITORE(Formattazione.formatta(titCerca.getEditoreKey()));
                if (titCerca.getTitoloCLET() != null)
                    titolo.settaParametro(TableDao.XXXstringaClet, Formattazione.formatta(titCerca.getTitoloCLET()));
                if (titCerca.getStringaCerca() != null) {

                	// Inizio Evolutiva febbraio 2015: MODIFICA PER COMPATIBILITA' NUOVO SCHEMA 2.0
                	StringaCercaTypeChoice stringaCercaTypeChoice = titCerca.getStringaCerca().getStringaCercaTypeChoice();
//                    String stringa = titCerca.getStringaCerca().getStringaEsatta();
                	String stringa = stringaCercaTypeChoice.getStringaEsatta();
                	// Fine Evolutiva febbraio 2015: MODIFICA PER COMPATIBILITA' NUOVO SCHEMA 2.0


                    if (stringa != null) {
                        stringa = Formattazione.formatta(stringa);
                        if (stringa.length() > 6) {
                            titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa.substring(0, 6));
                            titolo.settaParametro(TableDao.XXXstringaEsatta2, stringa.substring(6));
                        } else {
                            titolo.settaParametro(TableDao.XXXstringaEsatta1, stringa);
                            titolo.settaParametro(TableDao.XXXstringaEsatta2, " ");
                        }
                    }
                    // Inizio Evolutiva febbraio 2015: MODIFICA PER COMPATIBILITA' NUOVO SCHEMA 2.0
                    // viene s0stituito titCerca.getStringaCerca() con il nuovo campo stringaCercaTypeChoice
                    if (stringaCercaTypeChoice.getStringaLike() != null) {
                        stringa = Formattazione.formatta(stringaCercaTypeChoice.getStringaLike());
                        if (stringa.length() > 6) {
                            titolo.settaParametro(TableDao.XXXstringaLike1, stringa.substring(0, 6));
                            titolo.settaParametro(TableDao.XXXstringaLike2, stringa.substring(6));
                        } else {
    						titolo.settaParametro(TableDao.XXXstringaLike1, stringa);
    						titolo.settaParametro(TableDao.XXXstringaLike2, "");
                        }
                    }
                    String clet2 = new ChiaviTitolo().estraiClet2(stringa);
                    titolo.settaParametro(TableDao.XXXclet2_ricerca, clet2.trim());
                }
            }
        }
        String[] naturaTit = cerca.getNaturaSbn();
        if (naturaTit != null && naturaTit.length > 0) {
            for (int i = 0; i < naturaTit.length; i++)
                titolo.settaParametro("XXXcd_natura" + (i + 1), naturaTit[i]);
        }
        SbnMateriale tipoMateriale[] = cerca.getTipoMateriale();
        if (tipoMateriale != null) {
            for (int i = 0; i < tipoMateriale.length; i++) {
                if (tipoMateriale[i].toString().equals("E")) {
                    if (naturaTit != null) {
                        for (int c=0;c<naturaTit.length;c++) {
                            if (naturaTit[c].equals("S"))
                                throw new EccezioneSbnDiagnostico(3275);
                        }
                    }
                }
                titolo.settaParametro("XXXtp_materiale" + (i + 1), tipoMateriale[i].toString());
            }
        }
        GuidaDoc[] guida = cerca.getGuida();
        if (guida != null && guida.length > 0)
            for (int i = 0; i < guida.length; i++) {
                if (guida[i].getTipoRecord()!=null) {
                    titolo.settaParametro("XXXtp_record_uni" + (i + 1), guida[i].getTipoRecord().toString());
                }
            }
        SbnRangeDate t005_range = cerca.getT005_Range();
        if (t005_range != null) {
            if (!cerca.getT005_Range().hasTipoFiltroDate() || cerca.getT005_Range().getTipoFiltroDate() == 1) {
                if (esporta) {
//    				titolo.settaParametro(TableDao."export_misto_ts_var_da", t005_range.getDataDa().toString() + " 0000000");
//    				titolo.settaParametro(TableDao."export_misto_ts_var_a",  t005_range.getDataA().toString()  + " 2359599");
//    				titolo.settaParametro(TableDao."export_misto_ts_ins_da", t005_range.getDataDa().toString() + " 0000000");
//    				titolo.settaParametro(TableDao."export_misto_ts_ins_a",  t005_range.getDataA().toString()  + " 2359599");
                } else {
                    titolo.settaParametro(TableDao.XXXts_var_da, t005_range.getDataDa().toString());
                    titolo.settaParametro(TableDao.XXXts_var_a,  t005_range.getDataA().toString());
                }
            }
            else if (cerca.getT005_Range().getTipoFiltroDate() == 2){
//                if (esporta) {
//                  titolo.settaParametro(TableDao."export_ts_ins_da", t005_range.getDataDa().toString() + " 0000000");
//                  titolo.settaParametro(TableDao."export_ts_ins_a",  t005_range.getDataA().toString()  + " 2359599");
//                }
            }
            else {
//                if (esporta) {
//    				titolo.settaParametro(TableDao."export_ts_var_da", t005_range.getDataDa().toString() + " 0000000");
//    					titolo.settaParametro(TableDao."export_ts_var_a",  t005_range.getDataA().toString()  + " 2359599");
//    				}
            }
        }
        C100 t100_a = cerca.getT100_A();
        C100 t100_da = cerca.getT100_Da();
        if (t100_a != null && t100_da != null) {
            if (t100_a.getA_100_0() != null && t100_da.getA_100_0() != null) {
                titolo.settaParametro(TableDao.XXXts_ins_a, t100_a.getA_100_0().toString());
                titolo.settaParametro(TableDao.XXXts_ins_da, t100_da.getA_100_0().toString());
            }
            if (t100_a.getA_100_8() != null && t100_da.getA_100_8() != null) {
                //se sono diversi i tipi di data si arrabbia
                if (!t100_da.getA_100_8().equals(t100_a.getA_100_8()))
                    throw new EccezioneSbnDiagnostico(3041, "Tipi di date diversi");
                String tp_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100_da.getA_100_8().toString());
                if (tp_data == null || tp_data.length()==0) {
                    throw new EccezioneSbnDiagnostico(3276, "Tipo data non valido");
                }
                char tipoData = tp_data.charAt(0);
                if (tipoData == 'A' || tipoData =='B') {
                    if (naturaTit != null) {
                        for (int i=0;i<naturaTit.length;i++) {
                            if (!naturaTit[i].equals("S") && !naturaTit[i].equals("C"))
                                throw new EccezioneSbnDiagnostico(3191);
                        }
                    }
                } else if (tipoData == 'D' || tipoData =='E' || tipoData =='R' || tipoData =='G') {
                    if (naturaTit != null) {
                        for (int i=0;i<naturaTit.length;i++) {
                            if (!naturaTit[i].equals("M") && !naturaTit[i].equals("W"))
                                throw new EccezioneSbnDiagnostico(3191);
                        }
                    }
                }
                titolo.setTP_AA_PUBB(""+tipoData);
            }
            if (t100_a.getA_100_9() != null && t100_da.getA_100_9() != null) {
                if (t100_a.getA_100_9().length() != t100_da.getA_100_9().length())
                    throw new EccezioneSbnDiagnostico(3042);
                if (t100_a.getA_100_9().length() < 4) {
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_like, t100_da.getA_100_9());
                } else {
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_da, t100_da.getA_100_9());
                    titolo.settaParametro(TableDao.XXXaa_pubb_1_a, t100_a.getA_100_9());
                }
            }
            if (t100_a.getA_100_13() != null && t100_da.getA_100_13() != null) {
                if (t100_a.getA_100_13().length() != t100_da.getA_100_13().length())
                    throw new EccezioneSbnDiagnostico(3042);
                if (t100_a.getA_100_13().length() < 4) {
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_like, t100_da.getA_100_13());
                } else {
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_da, t100_da.getA_100_13());
                    titolo.settaParametro(TableDao.XXXaa_pubb_2_a, t100_a.getA_100_13());
                }
            }
        } else if (t100_a != null || t100_da != null) {
            throw new EccezioneSbnDiagnostico(3041);
        }
        if (cerca.getT101() != null) {
            String[] t101 = cerca.getT101().getA_101();
            if (t101.length > 0)
                titolo.setCD_LINGUA_1(t101[0]);
            if (t101.length > 1)
                titolo.setCD_LINGUA_2(t101[1]);
            if (t101.length > 2)
                titolo.setCD_LINGUA_3(t101[2]);
        }
        if (cerca.getT102() != null) {
            titolo.setCD_PAESE(cerca.getT102().getA_102());
        }
        if (cerca.getT105() != null) {
            String[] t105 = cerca.getT105().getA_105_4();
            if (t105.length > 0)
                titolo.setCD_GENERE_1(t105[0]);
            if (t105.length > 1)
                titolo.setCD_GENERE_2(t105[1]);
            if (t105.length > 2)
                titolo.setCD_GENERE_3(t105[2]);
            if (t105.length > 3)
                titolo.setCD_GENERE_4(t105[3]);
        }
        if (cerca.getLivelloAut_Da() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_da, Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
            titolo.settaParametro(TableDao.XXXcd_livello_a, cerca.getLivelloAut_A().toString());
        if (cerca.getT102() != null)
            titolo.setCD_PAESE(cerca.getT102().getA_102());
        if (cerca instanceof CercaDocAnticoType) {
            valorizzaFiltriAntico(titolo, (CercaDocAnticoType) cerca);
        }
        FiltriDatiComuniCercaType filtri = cerca.getFiltriDatiComuniCerca();
        if (filtri != null) {
            if (filtri.getA_105_11() != null)
                titolo.settaParametro(TableDao.XXXtestoLetterarioModerno, Decodificatore.getCd_tabella("COLM", filtri.getA_105_11()));
            if (filtri.getA_140_17() != null)
                titolo.settaParametro(TableDao.XXXtestoLetterarioAntico, Decodificatore.getCd_tabella("COLA", filtri.getA_140_17()));
            if (filtri.getA_181_0() != null)
                titolo.settaParametro(TableDao.XXXformaContenuto, Decodificatore.getCd_tabella("FOCO", filtri.getA_181_0()));
            if (filtri.getA_182_0() != null)
                titolo.settaParametro(TableDao.XXXtipoMediazione, Decodificatore.getCd_tabella("MEDI", filtri.getA_182_0()));
        }
        return titolo;


    }
}
