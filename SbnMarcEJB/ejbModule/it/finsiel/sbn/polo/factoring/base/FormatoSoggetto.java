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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_sog_tit_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A600;
import it.iccu.sbn.ejb.model.unimarcmodel.A_250;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.X_250;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Category;

/**
 * Classe FormatoSoggetto.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 11-mar-03
 */
public class FormatoSoggetto {

    static Category log = Category.getInstance("iccu.box.FormatoSoggetto");

    private static final String SEPARATORE_TERMINI_SOGGETTO = ResourceLoader.getPropertyString("SEPARATORE_TERMINI_SOGGETTO");
    private static final String SEPARATORE_TERMINI_DEFAULT = " - ";

	private static final Pattern REGEX_SEPARATORI_SOGGETTO = Pattern.compile("(\\s-\\s)|(\\s\\[.*?\\]\\s)|(\\s\\.\\s)");


    public FormatoSoggetto() {
    	super();
    }

    public SoggettoType formattaSoggetto(Tb_soggetto soggetto,
            SbnTipoOutput tipoOut, int tit_bib , int tit, List<Tb_descrittore> dd) {
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaSoggettoPerEsame(soggetto, tit_bib, tit, dd);
        else
			return formattaSoggettoPerLista(soggetto, tit_bib, tit, dd);
    }

    public SoggettoType formattaSoggettoPerEsame(Tb_soggetto soggetto,int tit_bib , int tit, List<Tb_descrittore> dd) {
        SoggettoType dati = new SoggettoType();
        dati.setNum_tit_coll(tit);
        dati.setNum_tit_coll_bib(tit_bib);
        dati.setLivelloAut(SbnLivello.valueOf(Decodificatore
                .livelloSoglia(soggetto.getCD_LIVELLO())));
        if(soggetto.getFL_CONDIVISO() != null)
        	dati.setCondiviso(DatiElementoTypeCondivisoType.valueOf(soggetto.getFL_CONDIVISO()));

        dati.setT001(soggetto.getCID());
        dati.setTipoAuthority(SbnAuthority.valueOf("SO"));
        SbnDatavar datevar = new SbnDatavar(soggetto.getTS_VAR());
        dati.setT005(datevar.getT005Date());

        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(soggetto.getTS_INS());
            a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: soggetto.ts_ins:"
                    + soggetto.getTS_INS());
        }

        dati.setT100(a100);
        //almaviva5_20120322 evolutive CFI
        A250 a250 = scomponiSoggetto(soggetto.getDS_SOGGETTO(), dd);
        A600 a600 = new A600();

        String cd;
        if (soggetto.getCD_SOGGETTARIO() != null
                && (cd = Decodificatore.getCd_unimarc("Tb_soggetto",
                        "cd_soggettario", soggetto.getCD_SOGGETTARIO().trim())) != null) {
            a250.setC2_250(cd);
        } else {
            a250.setC2_250("err.");
        }

        //almaviva5_20111122 evolutive CFI
        a250.setEdizione(ValidazioneDati.isFilled(soggetto.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(soggetto.getCD_EDIZIONE()) : null);

        dati.setT250(a250);
        // CATEGORIA SOGGETTO E NOTA
        if (soggetto.getCAT_SOGG() != null
                && (cd = Decodificatore.getCd_unimarc("Tb_soggetto",
                        "cat_sogg", soggetto.getCAT_SOGG().trim())) != null) {
        	a600.setA_601(cd);
        } else {
        	a600.setA_601("1");
        }
        if (ValidazioneDati.isFilled(soggetto.getNOTA_SOGGETTO()) )
        	a600.setA_602(soggetto.getNOTA_SOGGETTO());

        dati.setT600(a600);

        return dati;
    }

	public static final A250 scomponiSoggetto(final String ds_sogg, List<Tb_descrittore> dd) {

		List<String> descrittori = new ArrayList<String>();
		boolean filled = ValidazioneDati.isFilled(dd);

		String tmp = new String(ds_sogg);
		Matcher m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);

		String lastSep = null;
		boolean found = m.find();
		while (found) {
			String d = formattaStringaDescrittore(lastSep, tmp.substring(0, m.start()) );
			descrittori.add(d);
			lastSep = m.group();

			tmp = tmp.substring(m.end());
			m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);
			found = m.find();
		}
		if (ValidazioneDati.isFilled(tmp))
			descrittori.add(formattaStringaDescrittore(lastSep, tmp) );

		A250 a250 = new A250();
		A_250 a_250 = new A_250();
		String primoDescrittore = ValidazioneDati.isFilled(descrittori) ? descrittori.get(0) : "";
		a_250.setContent(primoDescrittore);
		if (filled)
			a_250.setCat_termine(dd.get(0).getCAT_TERMINE());
		a250.setA_250(a_250);

		int size = descrittori.size();
		filled = size == dd.size();	//fix soggetti pre-migrazione
		for (int idx = 1; idx < size; idx++) {
			X_250 x_250 = new X_250();
			x_250.setContent(descrittori.get(idx));
			if (filled)
				x_250.setCat_termine(dd.get(idx).getCAT_TERMINE());
			a250.addX_250(x_250);
		}

		return a250;
	}

	private static final String formattaStringaDescrittore(String separatore, String testo) {
		if (!ValidazioneDati.isFilled(separatore) || separatore.equals(SEPARATORE_TERMINI_DEFAULT))
			return ValidazioneDati.trimOrEmpty(testo);

		return ValidazioneDati.ltrim(separatore) + ValidazioneDati.trimOrEmpty(testo);
	}

	/**
	 * Method componiSoggetto.
	 * @param t250
	 * @return String
	 */
	public static final String componiSoggetto(A250 t250) {
		return componiSoggetto(t250, SEPARATORE_TERMINI_SOGGETTO);
	}

	public static final String componiSoggetto(A250 t250, String sep) {
		StringBuilder buf = new StringBuilder(240);
		buf.append(t250.getA_250().getContent());
		if (t250.getX_250() != null)
			for (int i = 0; i < t250.getX_250().length; i++) {
				String d = t250.getX_250(i).getContent();
				if (ValidazioneDati.isFilled(d))
					buf.append(sep).append(d);
			}

		return buf.toString();
	}

	public SoggettoType formattaSoggettoPerLista(Tb_soggetto soggetto,int tit_bib , int tit, List<Tb_descrittore> dd) {
        SoggettoType dati = new SoggettoType();
        dati.setNum_tit_coll(tit);
        dati.setNum_tit_coll_bib(tit_bib);
        dati.setTipoAuthority(SbnAuthority.SO);
        dati.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(soggetto.getCD_LIVELLO())));
        if(soggetto.getFL_CONDIVISO() != null)
        	dati.setCondiviso(DatiElementoTypeCondivisoType.valueOf(soggetto.getFL_CONDIVISO()));

        dati.setT001(soggetto.getCID());

        A250 a250 = scomponiSoggetto(soggetto.getDS_SOGGETTO(), dd);
        A600 a600 = new A600();

        String cd;
        if (soggetto.getCD_SOGGETTARIO() != null
                && (cd = Decodificatore.getCd_unimarc("Tb_soggetto",
                        "cd_soggettario", soggetto.getCD_SOGGETTARIO().trim())) != null) {
            a250.setC2_250(cd);
        } else {
            a250.setC2_250("err.");
        }

        //almaviva5_20111122 evolutive CFI
        a250.setEdizione(ValidazioneDati.isFilled(soggetto.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(soggetto.getCD_EDIZIONE()) : null);

        dati.setT250(a250);
        // CATEGORIA SOGGETTO E NOTA
        if (soggetto.getCAT_SOGG() != null
                && (cd = Decodificatore.getCd_unimarc("Tb_soggetto",
                        "cat_sogg", soggetto.getCAT_SOGG().trim())) != null) {
        	a600.setA_601(cd);
        } else {
        	a600.setA_601("1");
        }
        if (ValidazioneDati.isFilled(soggetto.getNOTA_SOGGETTO()) )
        	a600.setA_602(soggetto.getNOTA_SOGGETTO());

        dati.setT600(a600);

        return dati;
    }

    private ArrivoLegame formattaLegameDescrittore(Vl_descrittore_sog ds)
            throws IllegalArgumentException, InvocationTargetException, Exception  {
        ArrivoLegame arrLegame = new ArrivoLegame();
        // Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setIdArrivo(ds.getDID());
        legameAut.setTipoAuthority(SbnAuthority.DE);
        legameAut.setTipoLegame(SbnLegameAut.valueOf("931"));
        arrLegame.setLegameElementoAut(legameAut);
        // Setto i valori del documento legato
        ElementAutType el = new ElementAutType();
        FormatoDescrittore fd = new FormatoDescrittore();
        Descrittore cercaDescrittore = new Descrittore();
        List rinvii = cercaDescrittore.cercaRinviiDescrittore(ds.getDID(), null);
        Tb_descrittore desc = new Tb_descrittore();
        desc = cercaDescrittore.cercaDescrittorePerId(ds.getDID());
        DescrittoreType descrittoreType = fd.creaDescrittorePerEsameAnalitico(desc);
        //almaviva5_20120516 evolutive CFI
        descrittoreType.setRank(Math.max(1, ds.getFL_POSIZIONE()) );

		el.setDatiElementoAut(descrittoreType);
        LegamiType legamiType = fd.completaLegami(desc, rinvii);
        if (legamiType != null)
            el.addLegamiElementoAut(legamiType);

        legameAut.setElementoAutLegato(el);
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    public LegamiType[] formattaSoggettoConLegamiDescrittore(
            Tb_soggetto soggetto) throws IllegalArgumentException, InvocationTargetException, Exception {
        Descrittore descrittore = new Descrittore();
        List vettore;
        LegamiType[] legami = null;
        List legamiVec = new ArrayList();
        vettore = descrittore.cercaDescrittorePerSoggetto(soggetto.getCID());
        LegamiType legamiType = new LegamiType();
        if (vettore.size() > 0) {
            legamiType = new LegamiType();
            legamiType.setIdPartenza(soggetto.getCID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_descrittore_sog desc_sog = (Vl_descrittore_sog) vettore.get(i);
                legamiType.addArrivoLegame(formattaLegameDescrittore(desc_sog));

            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }
        legami = new LegamiType[legamiVec.size()];
        for (int i = 0; i < legami.length; i++) {
            legami[i] = (LegamiType) legamiVec.get(i);
        }
        return legami;
    }

    public ElementAutType formattaSoggettoPerCreazione(Tb_soggetto soggetto)
            throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        ElementAutType elemento = new ElementAutType();
        return formattaSoggettoInCreazione(soggetto);
    }

    /**
     * Method formattaSoggettoInCreazione.
     *
     * @param soggetto
     * @return ElementAutType
     */
    private ElementAutType formattaSoggettoInCreazione(Tb_soggetto soggetto) {
        ElementAutType elemento = new ElementAutType();
        SoggettoType datiElemento = new SoggettoType();
        datiElemento.setTipoAuthority(SbnAuthority.SO);
        if(soggetto.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(soggetto.getFL_CONDIVISO()));

        datiElemento.setT001(soggetto.getCID());
        SbnDatavar datevar = new SbnDatavar(soggetto.getTS_VAR());
        datiElemento.setT005(datevar.getT005Date());
        datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore
                .livelloSoglia(soggetto.getCD_LIVELLO())));
        elemento.setDatiElementoAut(datiElemento);
        return elemento;
    }
    /**
     * Formatta i campi in output per il timbro di condivisione
     */
//	public boolean formattaTimbroCondivisione(Tb_soggetto tabella, boolean legame) {
//		if(legame){
//			log.debug("TIMBRO CONDIVISIONE ---------- LEGAME-dati da tabella" + tabella.getClass().getName());
//		}else{
//			log.debug("TIMBRO CONDIVISIONE ---------- ELEMENTO-dati da tabella" + tabella.getClass().getName());
//		}
//		if (tabella.getFL_CONDIVISO() == null) {
//			System.out
//					.println("ERRORE"
//							+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//							+ tabella.getClass().getName());
//			return true;
//		} else {
//			if (tabella.getFL_CONDIVISO().equals("S"))
//				return true;
//			else
//				return false;
//		}
//	}
	// select count from vl_sog_tit_bib where cid='CSWC000030' and polo='XXX' and cd_biblioteca='YYY'

    public int cercaNum_tit_coll_bib(String cid, SbnUserType user) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_sog_tit_bib tit = new Vl_sog_tit_bib();
        tit.setCID(cid);
        String poloBib = user.getBiblioteca();
        String cd_polo = poloBib.substring(0,3);
        String cd_biblioteca = poloBib.substring(3,6);
        tit.setCD_POLO(cd_polo);
        tit.setCD_BIBLIOTECA(cd_biblioteca);
        return titolo.contaTitCollBib(tit);
    }




    //select count(*) from vl_soggetto_tit a
    //	where a.cid = 'CSWC000030' and a.bid in
    //	(select distinct(b.bid) from vl_soggetto_tit b
    //	  where b.cid = 'CSWC000030' )

    public int cercaNum_tit_coll(String cid) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_soggetto_tit tit = new Vl_soggetto_tit();
        tit.setCID(cid);
        TableDao v = titolo.contaTitColl(tit);
        int ret = v.getElencoRisultati().size();
        return ret;
    }

    public int contaNum_tit_coll(String cid) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_soggetto_tit tit = new Vl_soggetto_tit();
        tit.setCID(cid);
        TableDao v = titolo.contaTitColl(tit);
        int ret = v.getElencoRisultati().size();
        return ret;
    }

    public int cercaNum_tit_coll_unique(String cid) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_soggetto_tit tit = new Vl_soggetto_tit();
        tit.setCID(cid);
        int ret = titolo.contaTitCollUnique(tit);
        return ret;
    }

    public static void main(String[] args) {
    	String ds_sogg = "Barmecias . Opere - Trattati [di] filosofia - 1937-1955";
    	ds_sogg="filosofia - arte - storia [della] filosofia - Pascal . Opere";
		A250 t250 = scomponiSoggetto(ds_sogg, null);
		StringBuilder buf = new StringBuilder(240);
		buf.append(t250.getA_250().getContent());
		if (t250.getX_250() != null)
			for (int i = 0; i < t250.getX_250().length; i++)
				if (ValidazioneDati.isFilled(t250.getX_250(i).getContent()))
					buf.append("\n").append(t250.getX_250(i).getContent());

		System.out.println(buf.toString());
    }

}
