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


import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_claResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.KeyParameter;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_classe_the;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseType;
import it.iccu.sbn.ejb.model.unimarcmodel.ClasseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.text.ParseException;

import org.apache.log4j.Logger;


/**
 * @author
 *
 */
public class FormatoClasse {

    static Logger log = Logger.getLogger("iccu.box.FormatoClasse");

	private SbnTipoOutput tipoOut;


	public FormatoClasse(SbnTipoOutput tipoOut, String t001) {
		this.tipoOut = tipoOut;
    }

    public FormatoClasse() {
    	super();
    }


	public ElementAutType formattaClasse(Tb_classe classe, int num_tit, int num_tit_bib, boolean enableRanking) throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {

		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaClassePerEsameAnalitico(classe, num_tit, num_tit_bib, enableRanking);
		else
			return formattaClassePerLista(classe, num_tit, num_tit_bib, enableRanking);
	}

	/**
	 * metodo chiamato dal factoring CreaClasse per formattare l'output
	 */
	public ElementAutType formattaClassePerCreazione(String t001,Tb_classe classe) throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
		return formattaClasseInCreazione(t001,classe);
	}



	/**
	 * Method formattaClasseInCreazione.
	 *
	 * @param classe
	 * @return ElementAutType
	 */
	private ElementAutType formattaClasseInCreazione(String t001, Tb_classe classe) {
		ElementAutType elemento = new ElementAutType();
		ClasseType datiElemento = new ClasseType();
		datiElemento.setTipoAuthority(SbnAuthority.CL);
        if(classe.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(classe.getFL_CONDIVISO()));
		datiElemento.setT001(t001);
        if (classe.getTS_VAR()!= null) {
            SbnDatavar datevar = new SbnDatavar(classe.getTS_VAR());
            datiElemento.setT005(datevar.getT005Date());
        }
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(classe.getCD_LIVELLO())));
		elemento.setDatiElementoAut(datiElemento);
		return elemento;
	}



	private ElementAutType formattaClassePerLista(Tb_classe classe, int num_tit, int num_tit_bib, boolean enableRanking) throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
		ElementAutType elemento = new ElementAutType();
		ClasseType datiAut = creaClassePerListaSintetica(classe, num_tit, num_tit_bib);
		if (enableRanking) {
			int rank = ((Vl_classe_the)classe).getPOSIZIONE();
			datiAut.setRank(rank);
		}
		elemento.setDatiElementoAut(datiAut);
		return elemento;
	}




	private ElementAutType formattaClassePerEsameAnalitico(Tb_classe classe, int numTit, int numTitBib, boolean enableRanking) throws EccezioneDB, EccezioneFactoring , EccezioneSbnDiagnostico {
		ElementAutType elemento = new ElementAutType();
		ClasseType datiAut = creaClassePerEsameAnalitico(classe, numTit, numTitBib);
		elemento.setDatiElementoAut(datiAut);
		if (enableRanking) {
			int rank = ((Vl_classe_the)classe).getPOSIZIONE();
			datiAut.setRank(rank);
		}
		return elemento;
	}

    private ClasseType creaClassePerListaSintetica(Tb_classe classe, int num_tit, int num_tit_bib) throws EccezioneFactoring, EccezioneSbnDiagnostico, EccezioneDB {
        ClasseType datiElemento = new ClasseType();
        datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(classe.getCD_LIVELLO())));
        datiElemento.setTipoAuthority(SbnAuthority.CL);
        if(classe.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(classe.getFL_CONDIVISO()));

        String id = creaID(classe);
		datiElemento.setT001(id);

		ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();
        if (ValidazioneDati.isT001Dewey(id)) {
            A676 t676 = new A676();
            //almaviva5_20140409
            String edizione = Decodificatore.getCd_unimarc("ECLA", classe.getCD_EDIZIONE());
            if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
            t676.setV_676(edizione);
            t676.setC9_676(SbnIndicatore.valueOf(classe.getFL_COSTRUITO()));
            t676.setA_676(classe.getCLASSE());
            t676.setC_676(classe.getDS_CLASSE());

            classeTypeChoice.setT676(t676);
        } else {
            A686 t686 = new A686();
            t686.setA_686(classe.getCLASSE());
            t686.setC_686(classe.getDS_CLASSE());
            t686.setC2_686(classe.getCD_SISTEMA().trim());

            classeTypeChoice.setT686(t686);
        }
        datiElemento.setClasseTypeChoice(classeTypeChoice);

        //almaviva5_20091028
        datiElemento.setNum_tit_coll(num_tit);
        datiElemento.setNum_tit_coll_bib(num_tit_bib);

        return datiElemento;
    }


	public ClasseType creaClassePerEsameAnalitico(Tb_classe classe, int num_tit, int num_tit_bib) throws EccezioneSbnDiagnostico {
		ClasseType datiElemento = new ClasseType();
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(classe.getCD_LIVELLO())));
        datiElemento.setTipoAuthority(SbnAuthority.valueOf("CL"));
        if(classe.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(classe.getFL_CONDIVISO()));

		String id = creaID(classe);
		datiElemento.setT001(id);

		SbnDatavar datevar = new SbnDatavar(classe.getTS_VAR());
        datiElemento.setT005(datevar.getT005Date());

        ClasseTypeChoice classeTypeChoice = new ClasseTypeChoice();
        if (ValidazioneDati.isT001Dewey(id)) {
			A676 t676 = new A676();
			//almaviva5_20140409
            String edizione = Decodificatore.getCd_unimarc("ECLA", classe.getCD_EDIZIONE());
            if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
            t676.setV_676(edizione);
			t676.setC9_676(SbnIndicatore.valueOf(classe.getFL_COSTRUITO()));
			t676.setA_676(classe.getCLASSE());
			t676.setC_676(classe.getDS_CLASSE());

            classeTypeChoice.setT676(t676);
		} else {
			A686 t686 = new A686();
			t686.setA_686(classe.getCLASSE());
			t686.setC_686(classe.getDS_CLASSE());
            t686.setC2_686(classe.getCD_SISTEMA().trim());

            classeTypeChoice.setT686(t686);
		}
        datiElemento.setClasseTypeChoice(classeTypeChoice);

        SbnDatavar datevar1 = new SbnDatavar(classe.getTS_VAR());
        datiElemento.setT005(datevar1.getT005Date());

        //almaviva5_20090401 #2780
        datiElemento.setUltTermine(classe.getULT_TERM());

        //almaviva5_20091028
        datiElemento.setNum_tit_coll(num_tit);
        datiElemento.setNum_tit_coll_bib(num_tit_bib);

		A100 a100 = new A100();
        try {
            SbnData date = new SbnData(classe.getTS_INS());
            a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: autore.ts_ins:"
                    + classe.getTS_INS());
        }
		datiElemento.setT100(a100);
		return datiElemento;
	}

	/**
	 * Method creaID.
	 * @param classe
	 * @return String
	 * @throws EccezioneSbnDiagnostico
	 */
	private String creaID(Tb_classe classe) throws EccezioneSbnDiagnostico {
		StringBuilder id = new StringBuilder(32);
		String simbolo = classe.getCLASSE().trim();
		String edizione = classe.getCD_EDIZIONE().trim();
		String sistema = classe.getCD_SISTEMA();
		//almaviva5_20141114 edizione ridotta
		if (sistema.charAt(0) == 'D' && ValidazioneDati.isFilled(edizione)) {
			//dewey
			edizione = Decodificatore.getCd_unimarc("ECLA", edizione);
			if (!ValidazioneDati.isFilled(edizione))
	           	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente
			id.append('D').append(edizione).append(simbolo);
		}
		else
			//almaviva5_20090414
			id.append(ValidazioneDati.fillRight(sistema, ' ', 3)).append("  ").append(simbolo);

		return id.toString();
	}

	public int contaTitoliCollegatiClasse(String cdSISTEMA, String cdEDIZIONE,
			String classe) throws Exception {
		Tr_tit_cla titcla = new Tr_tit_cla();
		titcla.setCD_SISTEMA(cdSISTEMA);
		titcla.setCD_EDIZIONE(cdEDIZIONE);
		titcla.setCLASSE(classe);
		Tr_tit_claResult tavola = new Tr_tit_claResult(titcla);
		tavola.executeCustom("contaTitoliCollegatiClasse");
		return tavola.getCount();
	}

	public int contaTitoliCollegatiClasseBib(String cdSISTEMA,
			String cdEDIZIONE, String classe, SbnUserType userObject)
			throws Exception {
		Tr_tit_cla titcla = new Tr_tit_cla();
		titcla.setCD_SISTEMA(cdSISTEMA);
		titcla.setCD_EDIZIONE(cdEDIZIONE);
		titcla.setCLASSE(classe);
		titcla.settaParametro(KeyParameter.XXXcd_utente_amm, userObject);
		Tr_tit_claResult tavola = new Tr_tit_claResult(titcla);
		tavola.executeCustom("contaTitoliCollegatiClasseBib");
		return tavola.getCount();
	}



}

