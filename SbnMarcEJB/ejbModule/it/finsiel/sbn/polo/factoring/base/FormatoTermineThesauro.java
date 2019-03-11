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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.TermineThesauro;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.viste.Vl_termini_termini;
import it.finsiel.sbn.polo.orm.viste.Vl_the_tit_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 */
public class FormatoTermineThesauro {


	private String tipoOrd;
    private SbnUserType user;

    //file di log
    static Category log = Category.getInstance("iccu.box.FormatoDescrittore");

	public FormatoTermineThesauro (){

	}

	public ElementAutType formattaTermine(
			Tb_termine_thesauro termine_thesauro,
			SbnTipoOutput tipoOut,
			int tit_bib ,
			int tit)
			throws IllegalArgumentException, InvocationTargetException, Exception {

		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaTerminePerEsameAnalitico(termine_thesauro,tit_bib,tit);
		else
			return formattaTerminePerLista(termine_thesauro,tit_bib,tit);
	}
    public TermineType formattaThesauroPerEsame(Tb_termine_thesauro termine,int tit_bib , int tit) {
    	TermineType dati = new TermineType();
        dati.setNum_tit_coll(tit);
        dati.setNum_tit_coll_bib(tit_bib);
        dati.setLivelloAut(SbnLivello.valueOf(Decodificatore
                .livelloSoglia(termine.getCD_LIVELLO())));
        if(termine.getFL_CONDIVISO() != null)
        	dati.setCondiviso(DatiElementoTypeCondivisoType.valueOf(termine.getFL_CONDIVISO()));

        dati.setT001(termine.getDID());
        dati.setNum_tit_coll(tit);
        dati.setNum_tit_coll_bib(tit_bib);

        dati.setTipoAuthority(SbnAuthority.valueOf("TH"));
        SbnDatavar datevar = new SbnDatavar(termine.getTS_VAR());
        dati.setT005(datevar.getT005Date());

        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(termine.getTS_INS());
            a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: soggetto.ts_ins:"
                    + termine.getTS_INS());
        }

        dati.setT100(a100);
        A935 a935 = new A935();
        String ds_sogg = termine.getDS_TERMINE_THESAURO();
        a935.setA_935(ds_sogg);

        String cd;
        if (termine.getCD_THE() != null
                && (cd = Decodificatore.getCd_unimarc("Tb_termine_thesauro",
                        "cd_the", termine.getCD_THE().trim())) != null) {
            a935.setC2_935(cd);
        } else {
            a935.setC2_935("err.");
        }
        dati.setT935(a935);
        return dati;
    }

//	public TermineType formattaTerminePerSoggetto(Vl_descrittore_sog descrittore) throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
//		ElementAutType elemento = new ElementAutType();
//		TermineType datiElemento = new TermineType();
//		datiElemento.setTipoAuthority(SbnAuthority.TH);
//        if(termine_thesauro.getFL_CONDIVISO() != null)
//        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(termine_thesauro.getFL_CONDIVISO()));
//
//		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(termine_thesauro.getCD_LIVELLO())));
//		datiElemento.setFormaNome(SbnFormaNome.valueOf(termine_thesauro.getTP_FORMA_DES()));
//        SbnDatavar datevar = new SbnDatavar(termine_thesauro.getTS_VAR());
//        datiElemento.setT005(datevar.getOriginalDate());
//		//datiElemento.setT005(termine_thesauro.getTS_VAR().getDate());
//		datiElemento.setT001(termine_thesauro.getDID());
//		A100 a100 = new A100();
//        try {
//            SbnData date = new SbnData(termine_thesauro.getTS_INS());
//            a100.setA_100_0(
//                org.exolab.castor.types.Date.parseDate(
//                    date.getXmlDate()));
//        } catch (ParseException ecc) {
//            log.error(
//                "Formato data non corretto: termine_thesauro.ts_ins:"
//                    + termine_thesauro.getTS_INS());
//        }
//
//        datiElemento.setT100(a100);
//		A931 t931 = new A931();
//		String unimarc = Decodificatore.getCd_unimarc(
//			"Tb_descrittore",
//			"cd_soggettario",
//			termine_thesauro.getCD_SOGGETTARIO());
//		t931.setA_931(termine_thesauro.getDS_DESCRITTORE());
//		t931.setC2_931(unimarc);
//		if (termine_thesauro.getNOTA_DESCRITTORE() != null)
//			t931.setB_931(termine_thesauro.getNOTA_DESCRITTORE());
//		else
//			t931.setB_931("");
//		datiElemento.setT931(t931);
//		elemento.setDatiElementoAut(datiElemento);
//		return datiElemento;
//	}




//	private ElementAutType formattaTerminePerListaOld(Tb_termine_thesauro termine_thesauro) throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
//		ElementAutType elemento = new ElementAutType();
//		elemento.setDatiElementoAut(creaTerminePerListaSintetica(termine_thesauro));
//		return elemento;
//	}

	private ElementAutType formattaTerminePerLista(Tb_termine_thesauro termine_thesauro,int tit_bib , int tit) throws IllegalArgumentException, InvocationTargetException, Exception {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaTerminePerListaSintetica(termine_thesauro,tit_bib,tit));
//		TermineThesauro cercatermine_thesauro = new TermineThesauro();
//
//		List rinvii = cercatermine_thesauro.cercaRinviiDescrittore(termine_thesauro.getDID(), null);
//		LegamiType legamiType = completaLegami (termine_thesauro,rinvii);
//
//		if (legamiType != null)
//	        elemento.addLegamiElementoAut(legamiType);
		return elemento;
	}

	public ElementAutType formattaTerminePerEsameAnalitico(Tb_termine_thesauro termine_thesauro,int tit_bib , int tit) throws IllegalArgumentException, InvocationTargetException, Exception {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaTerminePerEsameAnalitico(termine_thesauro,tit_bib,tit));
		TermineThesauro cercaTermineThesauro = new TermineThesauro();
		List rinvii = cercaTermineThesauro.cercaRinviiTermini(termine_thesauro.getDID(), null);
		LegamiType legamiType = completaLegami(termine_thesauro,rinvii);
		if (legamiType != null)
	        elemento.addLegamiElementoAut(legamiType);
//		Descrittore cercaDescrittore = new Descrittore();
//		List rinvii = cercatermine_thesauro.cercaRinviiDescrittore(termine_thesauro.getDID(), null);
//		LegamiType legamiType = completaLegami (termine_thesauro,rinvii);
//		if (legamiType != null)
//	        elemento.addLegamiElementoAut(legamiType);
		return elemento;
	}

	private TermineType creaTerminePerListaSintetica(Tb_termine_thesauro termine_thesauro,int tit_bib , int tit) throws EccezioneFactoring, EccezioneSbnDiagnostico, EccezioneDB {
		TermineType datiElemento = new TermineType();
		datiElemento.setTipoAuthority(SbnAuthority.TH);
        if(termine_thesauro.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(termine_thesauro.getFL_CONDIVISO()));
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(termine_thesauro.getCD_LIVELLO())));
		datiElemento.setFormaNome(SbnFormaNome.valueOf(termine_thesauro.getTP_FORMA_THE()));
		datiElemento.setT001(termine_thesauro.getDID());
		datiElemento.setNum_tit_coll(tit);
		datiElemento.setNum_tit_coll_bib(tit_bib);

		A935 t935 = new A935();
		t935.setA_935(termine_thesauro.getDS_TERMINE_THESAURO());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_termine_thesauro",
			"cd_the",
			termine_thesauro.getCD_THE().trim());
		t935.setC2_935(unimarc);
		datiElemento.setT935(t935);
		return datiElemento;
	}

	public TermineType creaTerminePerEsameAnalitico(Tb_termine_thesauro termine_thesauro,int tit_bib , int tit) {
		TermineType datiElemento = new TermineType();
		datiElemento.setTipoAuthority(SbnAuthority.TH);
        if(termine_thesauro.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(termine_thesauro.getFL_CONDIVISO()));

		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(termine_thesauro.getCD_LIVELLO())));
		datiElemento.setFormaNome(SbnFormaNome.valueOf(termine_thesauro.getTP_FORMA_THE()));
        SbnDatavar sbnDatavar = new SbnDatavar(termine_thesauro.getTS_VAR());
        datiElemento.setT005(sbnDatavar.getT005Date());
		//datiElemento.setT005(termine_thesauro.getTS_VAR().getDate());
		datiElemento.setT001(termine_thesauro.getDID());
		datiElemento.setNum_tit_coll(tit);
		datiElemento.setNum_tit_coll_bib(tit_bib);
		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(termine_thesauro.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: termine_thesauro.ts_ins:"
                    + termine_thesauro.getTS_INS());
        }

        datiElemento.setT100(a100);
		A935 t935 = new A935();
		t935.setA_935(termine_thesauro.getDS_TERMINE_THESAURO());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_termine_thesauro",
			"cd_the",
			termine_thesauro.getCD_THE().trim());
		t935.setC2_935(unimarc);
		if (termine_thesauro.getNOTA_TERMINE_THESAURO() != null)
			t935.setB_935(termine_thesauro.getNOTA_TERMINE_THESAURO());
		else
			t935.setB_935("");
		datiElemento.setT935(t935);
		return datiElemento;
	}



    public LegamiType completaLegami(Tb_termine_thesauro termine_thesauro, List rinvio)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        LegamiType legame = new LegamiType();
        legame.setIdPartenza(termine_thesauro.getDID());
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[rinvio.size()];
        LegameElementoAutType legElAut = new LegameElementoAutType();
        for (int j = 0; j < rinvio.size(); j++) {
        	legElAut = new LegameElementoAutType();
//            LegameElementoAutType legElAut = new LegameElementoAutType();
            legElAut.setTipoAuthority(SbnAuthority.TH);

            legElAut.setIdArrivo(((Vl_termini_termini) rinvio.get(j)).getDID());
            ElementAutType elemento = new ElementAutType();
            TermineThesauro termineLegato = new TermineThesauro();
            Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
            tb_termine_thesauro =
            	termineLegato.cercaTerminePerId(((Vl_termini_termini) rinvio.get(j)).getDID());
            elemento.setDatiElementoAut(formattaLegameTermine(tb_termine_thesauro));
            String tipoLegame = ((Vl_termini_termini) rinvio.get(j)).getTIPO_COLL().trim().toUpperCase();
            legElAut.setElementoAutLegato(elemento);
			tipoLegame = Decodificatore.getCd_unimarc("Tr_termini_termini","tipo_coll",tipoLegame);
            legElAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegame));
            arrivoLegame[j] = new ArrivoLegame();
            arrivoLegame[j].setLegameElementoAut(legElAut);
        }
        legame.setArrivoLegame(arrivoLegame);
		if (legame.getArrivoLegameCount()==0)
			return null;
        return legame;
    }

    private TermineType formattaLegameTermine(Tb_termine_thesauro termine_thesauro) {
        TermineType datiElementoType = new TermineType();
        datiElementoType.setTipoAuthority(SbnAuthority.TH);
        if(termine_thesauro.getFL_CONDIVISO() != null)
        	datiElementoType.setCondiviso(DatiElementoTypeCondivisoType.valueOf(termine_thesauro.getFL_CONDIVISO()));

        datiElementoType.setT001(termine_thesauro.getDID());

        SbnDatavar datevar = new SbnDatavar(termine_thesauro.getTS_VAR());
        datiElementoType.setT005(datevar.getT005Date());

        datiElementoType.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(termine_thesauro.getCD_LIVELLO())));
        datiElementoType.setFormaNome(
            SbnFormaNome.valueOf(termine_thesauro.getTP_FORMA_THE()));
		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(termine_thesauro.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: termine_thesauro.ts_ins:"
                    + termine_thesauro.getTS_INS());
        }
		datiElementoType.setT100(a100);
		A935 t935 = new A935();
		t935.setA_935(termine_thesauro.getDS_TERMINE_THESAURO());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_termine_thesauro",
			"cd_the",
			termine_thesauro.getCD_THE().trim());
		t935.setC2_935(unimarc);
		if (termine_thesauro.getNOTA_TERMINE_THESAURO() != null)
			t935.setB_935(termine_thesauro.getNOTA_TERMINE_THESAURO());
		else
			t935.setB_935("");
		datiElementoType.setT935(t935);
        return datiElementoType;
    }
    public void formattaLegameThesauro(ElementAutType el) throws EccezioneDB {
        // Da implementare
        // el.addLegamiElementoAut();

    }

    public int cercaNum_tit_coll_bib(String did, SbnUserType user) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_the_tit_bib tit = new Vl_the_tit_bib();
        tit.setDID(did);
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

    public int cercaNum_tit_coll(String did) throws IllegalArgumentException, InvocationTargetException, Exception{
        TitoloCerca titolo = new TitoloCerca();
        Vl_thesauro_tit tit = new Vl_thesauro_tit();
        tit.setDID(did);
        TableDao v = titolo.contaTitColl(tit);
        int ret = v.getElencoRisultati().size();
        return ret;
        //if(tavola.getElencoRisultati().size() != 0){


    }


}
