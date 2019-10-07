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
package it.finsiel.sbn.polo.oggetti.cercatitolo;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.viste.V_daticomuniResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_sog_tit_bibResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_soggetto_titResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_the_tit_bibResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_thesauro_titResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_claResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_impResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_marResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_num_stdResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_sogResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_theResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_bResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_cResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.V_daticomuni;
import it.finsiel.sbn.polo.orm.viste.Vl_sog_tit_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_the_tit_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_imp;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_num_std;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_the;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Classe TitoloCerca.java
 * <p>
 * Esegue la ricerca di titoli sul db secondo diverse modalità.
 * </p>
 *
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class TitoloCerca extends Titolo {
    /**
	 *
	 */
	private static final long serialVersionUID = 4019476939057458L;
	private static Logger log = Logger.getLogger("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca");
    /** Costruttore */
    public TitoloCerca() {
        super();
    }
    /**
     * Metodo per cercare il titolo con stringa isbd
     * ricerca su Tb_titolo con isbd
     * @return elenco di titoli trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaTitoloPerISBD(String isbd) throws IllegalArgumentException, InvocationTargetException, Exception {
        setISBD(isbd);
        Tb_titoloResult tavola = new Tb_titoloResult(this);


        tavola.executeCustom("selectPerISBD");
        List v = tavola.getElencoRisultati();

        return v;
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerClet(String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        // MODIFICA MARCO RANIERI INSERITO IL .trim() pee forzare la ricerca
        if (clet.length()>3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, " ");
        }
        tavola.executeCustom("selectPerClet", ordinamento);
        return tavola;

    }
	/** Esegue la ricerca per nome del titolo con confronto di tipo like
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	 public List cercaTitoloPerClet(String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
		 TableDao tavola = this.cercaTitoloPerClet(clet, null);
         List v = tavola.getElencoRisultati();

		 return v;
	 }

	 public TableDao cercaTitoloPerCletCom(String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
	      V_daticomuniResult tavola = new V_daticomuniResult();

	        if (clet.length()>3) {
	            settaParametro(KeyParameter.XXXstringaClet1, clet.substring(0,3).trim());
	            settaParametro(KeyParameter.XXXstringaClet2, clet.substring(3).trim());
	        } else {
	            settaParametro(KeyParameter.XXXstringaClet1, clet);
	            settaParametro(KeyParameter.XXXstringaClet2, " ");
	        }
	        tavola.executeCustom("selectPerClet", ordinamento);
	        return tavola;

	}
    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLike(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

    // Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico e dati comuni (Aea0)
    public TableDao cercaTitoloPerNomeLikeCom(V_daticomuni com, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_daticomuniResult tavola = new V_daticomuniResult(com);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }

  //almaviva4 12/04/2010
    /** Esegue la ricerca per nome del titolo con confronto di tipo like */
    public TableDao cercaTitoloPerNomeLikeSuperindex(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
  	  Tb_titoloResult tavola = new Tb_titoloResult(this);
  	  tavola.executeCustom("selectPerNomeLikeSuperindex", ordinamento);
  	  return tavola;
    }


    /** Esegue la ricerca per nome del titolo con confronto di tipo like */
    public TableDao cercaTitoloPerNomeLikeSoloBid(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
  	  Tb_titoloResult tavola = new Tb_titoloResult(this);
  	  tavola.executeCustom("selectPerNomeLikeSoloBid", ordinamento);
  	  return tavola;
    }

  //almaviva4 12/04/2010


    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaTitoloPerLikeNome(String nome) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);


        if (nome.length() > 6) {
            settaParametro(TableDao.XXXstringaLike1, nome.substring(0, 6));
            settaParametro(TableDao.XXXstringaLike2, nome.substring(6));
        } else
            settaParametro(TableDao.XXXstringaLike1, nome);
        tavola.executeCustom("selectPerNomeLike", null);
        List v = tavola.getElencoRisultati();

        return v;

    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsatto(String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    public TableDao cercaTitoloPerNomeEsattoCom(V_daticomuni com, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_daticomuniResult tavola = new V_daticomuniResult();
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        return conta(tavola, "countPerNomeLike");
    }

    public int contaTitoloPerNomeLikeCom(V_daticomuni com) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_daticomuniResult tavola = new V_daticomuniResult(com);
        return conta(tavola, "countPerNomeLike");
    }

    //almaviva4 12/04/2010
	public int contaTitoloPerNomeLikeSuperIndex() throws IllegalArgumentException, InvocationTargetException, Exception {
		Tb_titoloResult tavola = new Tb_titoloResult(this);
		return conta(tavola, "countPerNomeLikeSuperindex");
	}
//almaviva4 12/04/2010 fine




    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerClet(String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        if (clet.length()>3) {
            settaParametro(TableDao.XXXstringaClet1, clet.substring(0,3));
            settaParametro(TableDao.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(TableDao.XXXstringaClet1, clet);
            settaParametro(TableDao.XXXstringaClet2, "");
        }
        return conta(tavola, "countPerClet");
    }

    public int contaTitoloPerCletCom(String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_daticomuniResult tavola = new V_daticomuniResult();
        if (clet.length()>3) {
            settaParametro(KeyParameter.XXXstringaClet1, clet.substring(0,3));
            settaParametro(KeyParameter.XXXstringaClet2, clet.substring(3));
        } else {
            settaParametro(KeyParameter.XXXstringaClet1, clet);
            settaParametro(KeyParameter.XXXstringaClet2, "");
        }
        return conta(tavola, "countPerClet");
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerImpronta(Vl_titolo_imp titolo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_impResult tavola = new Vl_titolo_impResult(titolo);
        return conta(tavola, "countPerImpronta");
    }

    /**
     * Metodo per cercare il titolo con numero di identificativo:
     * ricerca su Tb_titolo con ID.
     * @return il titolo trovato oppure null
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerImpronta(Vl_titolo_imp titolo, String ord)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_impResult tavola = new Vl_titolo_impResult(titolo);


        tavola.executeCustom("selectPerImpronta", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsatto() throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titoloResult tavola = new Tb_titoloResult(this);
        return conta(tavola, "countPerNomeEsatto");
    }

    public int contaTitoloPerNomeEsattoCom(V_daticomuni com) throws IllegalArgumentException, InvocationTargetException, Exception {
        V_daticomuniResult tavola = new V_daticomuniResult();
        return conta(tavola, "countPerNomeEsatto");
    }

    /** Esegue una ricerca per i titoli di accesso legati ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaLegamiTitoloAccesso(Tb_titolo titolo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_tit = new Vl_titolo_tit_b();
        tit_tit.setBID_BASE(titolo.getBID());
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_tit);


        tavola.executeCustom("selectTitoloPerTitoloAccesso", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }
    /**
     * Method cercaTitoloPerNumStd.
     * @param num
     * @param tipoStd
     * @param paeseStd
     * @param nota
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerNumStd(String num, String tipoStd, String paeseStd, String nota)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Vl_titolo_num_std tn = new Vl_titolo_num_std();
        tn.setNUMERO_STD(num);
        tn.setTP_NUMERO_STD(Decodificatore.getCd_tabella("Tb_numero_std", "tp_numero_std", tipoStd));
        tn.setCD_PAESE_STD(paeseStd);
        tn.setNOTA_NUMERO_STD(nota);
        tn.leggiAllParametro().putAll(this.leggiAllParametro());
        Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tn);

        tavola.executeCustom("selectPerNumero");
        
        //Deve essere realizzata: campi fissi sono tipo e numero, gli altri sono opzionali.
        return tavola;
    }
    /**
     * Method cercaTitoloPerBid.
     * @param num
     * @param tipoStd
     * @param paeseStd
     * @param nota
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List  cercaNumeroStandardPerBid(String bid)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_num_std tn = new Vl_titolo_num_std();
        tn.setBID(bid);
        tn.leggiAllParametro().putAll(this.leggiAllParametro());
        Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tn);


        tavola.executeCustom("selectPerBid");
        List lista  = tavola.getElencoRisultati();

        return lista;
    }

	/**
	 * Method cercaTitoloPerNumStd.
	 * @param num
	 * @param tipoStd
	 * @param paeseStd
	 * @param nota
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaTitoloPerListaNumStd(String num, String tipoStd, String paeseStd, String nota)
		throws IllegalArgumentException, InvocationTargetException, Exception {

		Vl_titolo_num_std tn = new Vl_titolo_num_std();
		tn.setNUMERO_STD(num);
		tn.setTP_NUMERO_STD(Decodificatore.getCd_tabella("Tb_numero_std", "tp_numero_std", tipoStd));
		tn.setCD_PAESE_STD(paeseStd);
		tn.setNOTA_NUMERO_STD(nota);
		Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tn);


		tavola.executeCustom("selectPerListaNumeri");
		//Deve essere realizzata: campi fissi sono tipo e numero, gli altri sono opzionali.
		return tavola;
	}

    /**
     * Method contaTitoloPerLuogo.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerLuogo(Vl_titolo_luo tit_luo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_luoResult tavola = new Vl_titolo_luoResult(tit_luo);
        return conta(tavola, "countPerLuogo");
    }

    /**
     * Method contaTitoloPerLuogo
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerLuogo(Vl_titolo_luo tit_luo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_luoResult tavola = new Vl_titolo_luoResult(tit_luo);


        tavola.executeCustom("selectPerLuogo", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerMarca(Vl_titolo_mar tit_mar) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_marResult tavola = new Vl_titolo_marResult(tit_mar);
        return conta(tavola, "countPerMarca");
    }

    /**
     * Method contaTitoloPerLuogo
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerDocumento(String id_doc,String sici, Vl_titolo_tit_c tit_doc, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        tit_doc.setBID_COLL(id_doc);
        tit_doc.setSICI(sici);
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit_doc);


        tavola.executeCustom("selectPerDocumento", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerDocumento(String id_doc, String sici, Vl_titolo_tit_c tit_doc) throws IllegalArgumentException, InvocationTargetException, Exception {
        tit_doc.setBID_COLL(id_doc);
        tit_doc.setSICI(sici);
        Vl_titolo_tit_cResult tavola = new Vl_titolo_tit_cResult(tit_doc);
        return conta(tavola, "countPerDocumento");
    }

    /**
     * Method contaTitoloPerLuogo
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTitUniforme(String id_doc, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
        tit_doc.setBID_BASE(id_doc);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);


        tavola.executeCustom("selectPerTitUniforme", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerTpLegame: conta per un titolo da un legame per sottotipolegame
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTpLegame(Vl_titolo_tit_b tit_doc) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);
        return conta(tavola, "countPerBidPerTpLegame");
    }

    /**
     * Method cercaTitoloPerTpLegame: cerca per un titolo da un legame per sottotipolegame
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTpLegame(Vl_titolo_tit_b tit_doc, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);


        tavola.executeCustom("selectPerDocumentoPerTpLegame", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeEsattoPerTpLegame(Vl_titolo_tit_b mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);
        return conta(tavola, "countPerDocumentoEsattoPerTpLegame");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeEsattoPerTpLegame(Vl_titolo_tit_b mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);


        tavola.executeCustom("selectPerNomeEsattoPerTpLegame", ordinamento);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerNomeLikePerTpLegame(Vl_titolo_tit_b mus) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);
        return conta(tavola, "countPerNomeLikePerTpLegame");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo Like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNomeLikePerTpLegame(Vl_titolo_tit_b mus, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);


        tavola.executeCustom("selectPerNomeLikePerTpLegame", ordinamento);
        return tavola;
    }
    /** Esegue la conta dei record che vengono trovati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaTitoloPerCletPerTpLegame(Vl_titolo_tit_b mus, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);
        mus.settaParametro(TableDao.XXXstringaClet, clet);
        return conta(tavola, "countPerCletPerTpLegame");
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerCletPerTpLegame(Vl_titolo_tit_b mus, String clet, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(mus);


        mus.settaParametro(TableDao.XXXstringaClet, clet);
        tavola.executeCustom("selectPerCletPerTpLegame", ordinamento);
        return tavola;

    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTitUniforme(String id_doc) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
        tit_doc.setBID_BASE(id_doc);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);
        return conta(tavola, "countPerTitUniforme");
    }

    /**
     * Method contaTitoloPerLuogo
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerTitAccesso(String id_doc, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
        tit_doc.setBID_BASE(id_doc);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);


        tavola.executeCustom("selectPerTitAccesso", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerTitAccesso(String id_doc) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
        tit_doc.setBID_BASE(id_doc);
        Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(tit_doc);
        return conta(tavola, "countPerTitAccesso");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerMarca(Vl_titolo_mar tit_mar, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_marResult tavola = new Vl_titolo_marResult(tit_mar);


        tavola.executeCustom("selectPerMarca", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitCollBib(Vl_sog_tit_bib tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_sog_tit_bibResult tavola = new Vl_sog_tit_bibResult(tit_sog);
        return conta(tavola, "countTitCollBib");
    }

    public TableDao contaTitColl(Vl_soggetto_tit tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_soggetto_titResult tavola = new Vl_soggetto_titResult(tit_sog);
    	tavola.executeCustom("cercaTitColl");
        return tavola;
    }

    public int contaTitCollUnique(Vl_soggetto_tit tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_soggetto_titResult tavola = new Vl_soggetto_titResult(tit_sog);
    	return conta(tavola, "contaTitColl");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerSoggetto(Vl_titolo_sog tit_sog) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_sogResult tavola = new Vl_titolo_sogResult(tit_sog);
        return conta(tavola, "countPerSoggetto");
    }


    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerSoggetto(Vl_titolo_sog tit_sog, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_sogResult tavola = new Vl_titolo_sogResult(tit_sog);


        tavola.executeCustom("selectPerSoggetto", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerThesauro(Vl_titolo_the tit_the) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_titolo_theResult tavola = new Vl_titolo_theResult(tit_the);
        return conta(tavola, "countPerThesauro");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerThesauro(Vl_titolo_the tit_the, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_titolo_theResult tavola = new Vl_titolo_theResult(tit_the);


        tavola.executeCustom("selectPerThesauro", ordinamento);
        return tavola;
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public int contaTitoloPerClasse(Vl_titolo_cla tit_cla) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_claResult tavola = new Vl_titolo_claResult(tit_cla);

        return conta(tavola, "countPerClasse");
    }

    /**
     * Method contaTitoloPerRepertorio.
     * @param string
     * @return int
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaTitoloPerClasse(Vl_titolo_cla tit_cla, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_claResult tavola = new Vl_titolo_claResult(tit_cla);


        tavola.executeCustom("selectPerClasse", ordinamento);
        return tavola;
    }

    public int contaTitCollBib(Vl_the_tit_bib tit_the) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_the_tit_bibResult tavola = new Vl_the_tit_bibResult(tit_the);
        return conta(tavola, "countTitCollBib");
    }

    public TableDao contaTitColl(Vl_thesauro_tit tit_the) throws IllegalArgumentException, InvocationTargetException, Exception {
    	Vl_thesauro_titResult tavola = new Vl_thesauro_titResult(tit_the);
    	tavola.executeCustom("cercaTitColl");
        return tavola;
    }

    // Inizio manutenzione correttiva 27.09.2019 Bug MANTIS 7124 almaviva2
    // Vedi anche Protocollo Indice:MANTIS 2108 poich� non fa la count non imposta "numRecord"
    // Il campo ricercaSenzaCount non deve essere impostato altrimenti vengono portati in sintetica solo le prime 10 (maxrighe)
    // richieste perdendo la paginazione sulle successive occorrenze.
    public int contaTitoloPerNumStd(String num, String tipoStd, String paeseStd, String nota)
    		throws IllegalArgumentException, InvocationTargetException, Exception   {
		Vl_titolo_num_std tn = new Vl_titolo_num_std();
		tn.setTP_NUMERO_STD(Decodificatore.getCd_tabella("Tb_numero_std", "tp_numero_std", tipoStd));

		tn.setNUMERO_STD(num);
		tn.setCD_PAESE_STD(paeseStd);
		tn.setNOTA_NUMERO_STD(nota);
		Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tn);
        return conta(tavola, "countPerNumero");
	}


}
