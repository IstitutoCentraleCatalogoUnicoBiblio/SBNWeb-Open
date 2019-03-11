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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_des;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_sog;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
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
public class FormatoDescrittore {


	private String tipoOrd;
    private SbnUserType user;

    //file di log
    static Category log = Category.getInstance("iccu.box.FormatoDescrittore");

	public FormatoDescrittore (){

	}

	public ElementAutType formattaDescrittore(Tb_descrittore descrittore,SbnTipoOutput tipoOut) throws IllegalArgumentException, InvocationTargetException, Exception {
		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaDescrittorePerEsameAnalitico(descrittore);
		else
			return formattaDescrittorePerLista(descrittore);
	}

	public DescrittoreType formattaDescrittorePerSoggetto(Vl_descrittore_sog des) throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
		ElementAutType elemento = new ElementAutType();
		DescrittoreType datiElemento = new DescrittoreType();
		datiElemento.setTipoAuthority(SbnAuthority.DE);
        if(des.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(des.getFL_CONDIVISO()));

		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(des.getCD_LIVELLO())));
		datiElemento.setFormaNome(SbnFormaNome.valueOf(des.getTP_FORMA_DES()));
        SbnDatavar datevar = new SbnDatavar(des.getTS_VAR());
        datiElemento.setT005(datevar.getT005Date());
		//datiElemento.setT005(descrittore.getTS_VAR().getDate());
		datiElemento.setT001(des.getDID());
		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(des.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: descrittore.ts_ins:"
                    + des.getTS_INS());
        }

        datiElemento.setT100(a100);
		A931 t931 = new A931();
		String unimarc = Decodificatore.getCd_unimarc("Tb_descrittore",	"cd_soggettario", des.getCD_SOGGETTARIO());
		t931.setC2_931(unimarc);
		t931.setA_931(des.getDS_DESCRITTORE());
		t931.setEdizione(ValidazioneDati.isFilled(des.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(des.getCD_EDIZIONE()) : null);
		t931.setCat_termine(ValidazioneDati.isFilled(des.getCAT_TERMINE()) ? des.getCAT_TERMINE() : null);
		//almaviva5_20121205 segnalazione CFI
		t931.setB_931(ValidazioneDati.isFilled(des.getNOTA_DESCRITTORE()) ? des.getNOTA_DESCRITTORE() : null);
		datiElemento.setT931(t931);
		elemento.setDatiElementoAut(datiElemento);
		return datiElemento;
	}




	private ElementAutType formattaDescrittorePerListaOld(Tb_descrittore descrittore) throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaDescrittorePerListaSintetica(descrittore));
		return elemento;
	}

	private ElementAutType formattaDescrittorePerLista(Tb_descrittore descrittore) throws IllegalArgumentException, InvocationTargetException, Exception {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaDescrittorePerEsameAnalitico(descrittore));
		Descrittore cercaDescrittore = new Descrittore();
		List rinvii = cercaDescrittore.cercaRinviiDescrittore(descrittore.getDID(), null);
		LegamiType legamiType = completaLegami (descrittore,rinvii);
		if (legamiType != null)
	        elemento.addLegamiElementoAut(legamiType);
		return elemento;
	}

	public ElementAutType formattaDescrittorePerEsameAnalitico(Tb_descrittore descrittore) throws IllegalArgumentException, InvocationTargetException, Exception {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaDescrittorePerEsameAnalitico(descrittore));
		Descrittore cercaDescrittore = new Descrittore();
		List rinvii = cercaDescrittore.cercaRinviiDescrittore(descrittore.getDID(), null);
		LegamiType legamiType = completaLegami (descrittore,rinvii);
		if (legamiType != null)
	        elemento.addLegamiElementoAut(legamiType);
		return elemento;
	}

	private DescrittoreType creaDescrittorePerListaSintetica(Tb_descrittore des) throws EccezioneFactoring, EccezioneSbnDiagnostico, EccezioneDB {
		DescrittoreType datiElemento = new DescrittoreType();
		datiElemento.setTipoAuthority(SbnAuthority.DE);
        if(des.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(des.getFL_CONDIVISO()));
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(des.getCD_LIVELLO())));
		datiElemento.setFormaNome(SbnFormaNome.valueOf(des.getTP_FORMA_DES()));
		datiElemento.setT001(des.getDID());
		A931 t931 = new A931();
		t931.setA_931(des.getDS_DESCRITTORE());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_soggetto",
			"cd_soggettario",
			des.getCD_SOGGETTARIO().trim());
		t931.setC2_931(unimarc);
		t931.setEdizione(ValidazioneDati.isFilled(des.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(des.getCD_EDIZIONE()) : null);
		t931.setCat_termine(ValidazioneDati.isFilled(des.getCAT_TERMINE()) ? des.getCAT_TERMINE() : null);
		datiElemento.setT931(t931);
		return datiElemento;
	}

	public DescrittoreType creaDescrittorePerEsameAnalitico(Tb_descrittore des) {
		DescrittoreType datiElemento = new DescrittoreType();
		datiElemento.setTipoAuthority(SbnAuthority.DE);
        if(des.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(des.getFL_CONDIVISO()));

		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(des.getCD_LIVELLO())));
		datiElemento.setFormaNome(SbnFormaNome.valueOf(des.getTP_FORMA_DES()));
        SbnDatavar sbnDatavar = new SbnDatavar(des.getTS_VAR());
        datiElemento.setT005(sbnDatavar.getT005Date());
		//datiElemento.setT005(descrittore.getTS_VAR().getDate());
		datiElemento.setT001(des.getDID());
		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(des.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: descrittore.ts_ins:"
                    + des.getTS_INS());
        }

        datiElemento.setT100(a100);
		A931 t931 = new A931();
		t931.setA_931(des.getDS_DESCRITTORE());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_soggetto",
			"cd_soggettario",
			des.getCD_SOGGETTARIO().trim());
		t931.setC2_931(unimarc);
		t931.setEdizione(ValidazioneDati.isFilled(des.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(des.getCD_EDIZIONE()) : null);
		t931.setCat_termine(ValidazioneDati.isFilled(des.getCAT_TERMINE()) ? des.getCAT_TERMINE() : null);
		//almaviva5_20121205 segnalazione CFI
		t931.setB_931(ValidazioneDati.isFilled(des.getNOTA_DESCRITTORE()) ? des.getNOTA_DESCRITTORE() : null);
		datiElemento.setT931(t931);
		return datiElemento;
	}



    public LegamiType completaLegami(Tb_descrittore descrittore, List rinvio)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        LegamiType legame = new LegamiType();
        legame.setIdPartenza(descrittore.getDID());
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[rinvio.size()];
        LegameElementoAutType legElAut = new LegameElementoAutType();
        for (int j = 0; j < rinvio.size(); j++) {
        	legElAut = new LegameElementoAutType();
//            LegameElementoAutType legElAut = new LegameElementoAutType();
            legElAut.setTipoAuthority(SbnAuthority.DE);

            legElAut.setIdArrivo(((Vl_descrittore_des) rinvio.get(j)).getDID());
            ElementAutType elemento = new ElementAutType();
            Descrittore descrittoreLegato = new Descrittore();
            Tb_descrittore tb_descrittore = new Tb_descrittore();
            tb_descrittore =
            	descrittoreLegato.cercaDescrittorePerId(((Vl_descrittore_des) rinvio.get(j)).getDID());
            elemento.setDatiElementoAut(formattaLegameDescrittore(tb_descrittore));
            String tipoLegame = ((Vl_descrittore_des) rinvio.get(j)).getTP_LEGAME().trim().toUpperCase();
            legElAut.setElementoAutLegato(elemento);
			tipoLegame = Decodificatore.getCd_unimarc("Tr_des_des","tp_legame",tipoLegame);
            legElAut.setTipoLegame(SbnLegameAut.valueOf(tipoLegame));
            arrivoLegame[j] = new ArrivoLegame();
            arrivoLegame[j].setLegameElementoAut(legElAut);
        }
        legame.setArrivoLegame(arrivoLegame);
		if (legame.getArrivoLegameCount()==0)
			return null;
        return legame;
    }

    private DatiElementoType formattaLegameDescrittore(Tb_descrittore des) {
        DescrittoreType datiElementoType = new DescrittoreType();
        datiElementoType.setTipoAuthority(SbnAuthority.DE);
        if(des.getFL_CONDIVISO() != null)
        	datiElementoType.setCondiviso(DatiElementoTypeCondivisoType.valueOf(des.getFL_CONDIVISO()));

        datiElementoType.setT001(des.getDID());

        SbnDatavar datevar = new SbnDatavar(des.getTS_VAR());
        datiElementoType.setT005(datevar.getT005Date());

        datiElementoType.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(des.getCD_LIVELLO())));
        datiElementoType.setFormaNome(
            SbnFormaNome.valueOf(des.getTP_FORMA_DES()));
		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(des.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: descrittore.ts_ins:"
                    + des.getTS_INS());
        }
		datiElementoType.setT100(a100);
		A931 t931 = new A931();
		t931.setA_931(des.getDS_DESCRITTORE());
		String unimarc = Decodificatore.getCd_unimarc(
			"Tb_soggetto",
			"cd_soggettario",
			des.getCD_SOGGETTARIO().trim());
		t931.setC2_931(unimarc);
		t931.setEdizione(ValidazioneDati.isFilled(des.getCD_EDIZIONE()) ? SbnEdizioneSoggettario.valueOf(des.getCD_EDIZIONE()) : null);
		t931.setCat_termine(ValidazioneDati.isFilled(des.getCAT_TERMINE()) ? des.getCAT_TERMINE() : null);
		//almaviva5_20121205 segnalazione CFI
		t931.setB_931(ValidazioneDati.isFilled(des.getNOTA_DESCRITTORE()) ? des.getNOTA_DESCRITTORE() : null);
		datiElementoType.setT931(t931);
        return datiElementoType;
    }

}
