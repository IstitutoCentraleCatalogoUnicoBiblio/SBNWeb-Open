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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_abstractResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Abstract extends Tb_abstract{
   
	private static final long serialVersionUID = -7946378719720280546L;
	private boolean 		filtriValorizzati;
	private static 		Category log = Category.getInstance("iccu.serversbnmarc.Descrittore");


    public Abstract() {
    }

    public Tb_abstract verificaEsistenza(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        setBID(id);
        Tb_abstractResult tavola = new Tb_abstractResult(this);


        //tavola.executeCustom("selectPerEsistenza");
        tavola.executeCustom("selectPerKey");
        List v = tavola.getElencoRisultati();

        Tb_abstract abs = null;
        if (v.size() > 0)
            abs = (Tb_abstract) v.get(0);
        return abs;

    }


    public Tb_abstract cercaAbstractPerId(String id) throws EccezioneDB, InfrastructureException {
        setBID(id);
        Tb_abstractResult tavola = new Tb_abstractResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        Tb_abstract descr = null;
        if (v.size() > 0)
            descr = (Tb_abstract) v.get(0);
        return descr;

    }

	/**
	 * Method estraiTitoliThesauro
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List estraiTitoliAbstract(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
	    setBID(bid);
        Tb_abstractResult tavola = new Tb_abstractResult(this);
        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List TableDaoResult = tavola.getElencoRisultati();
        return TableDaoResult;
 	}



    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
//    public Tb_abstract valorizzaFiltri(Tb_abstract abs, CercaDatiAutType cerca) {
//        CercaAbstractType cercaAbstract = null;
//		filtriValorizzati = true;
//        if (cerca == null)
//            return abs;
//        return abs;
//    }


    /** Valorizza i filtri */
    public Tb_abstract valorizzaFiltri(Tb_abstract abs, CercaDatiAutType cerca) {
    	CercaAbstractType cercaAbstract = null;
		filtriValorizzati = true;
        if (cerca == null)
            return abs;
        if (cerca.getLivelloAut_Da() != null)
        	abs.settaParametro(TableDao.XXXlivello_aut_da,
                Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
        	abs.settaParametro(TableDao.XXXlivello_aut_a, cerca.getLivelloAut_A().toString());
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
        		abs.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
        		abs.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
        		abs.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
        		abs.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }
		if (cerca instanceof CercaAbstractType)
			cercaAbstract = (CercaAbstractType) cerca;
        return abs;
    }


	/**
	 * Method creaAbstract.
	 * @param id
	 * @param descrizioneSoggetto
	 * @param livello
	 * @throws InfrastructureException
	 */
	public boolean creaAbstract(
		String id,
		String livello,
		String descrizioneAbstract,
		String user,
		String _condiviso) throws EccezioneDB,EccezioneSbnDiagnostico, InfrastructureException {
        Tb_abstract tb_abstract = new Tb_abstract();
        tb_abstract.setCD_LIVELLO(livello);
		if (descrizioneAbstract != null)
			tb_abstract.setDS_ABSTRACT(descrizioneAbstract);
		tb_abstract.setBID(id);
		tb_abstract.setFL_CANC(" ");


		tb_abstract.setUTE_INS(user);
		tb_abstract.setUTE_VAR(user);

        Tb_abstractResult tavola = new Tb_abstractResult(tb_abstract);

        tavola.insert(tb_abstract);

		return true;
	}





    public void cancellaAbstract(Tb_abstract abstractDaCancellare, String user, TimestampHash timeHash)
    								throws IllegalArgumentException, InvocationTargetException, Exception {
    abstractDaCancellare.setFL_CANC("S");
    abstractDaCancellare.setUTE_VAR(user);
    String id = new String();
    id = abstractDaCancellare.getBID();
    abstractDaCancellare.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_abstract", id)));
    Tb_abstractResult tavola = new Tb_abstractResult(abstractDaCancellare);
    tavola.executeCustomUpdate("cancellaAbstract");

}

    public void cancellaAbstractPerBid(String bid,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
    	 Tb_abstract tb_abstract = new Tb_abstract();
    	 tb_abstract.setFL_CANC("S");
    	 tb_abstract.setUTE_VAR(ute_var);
    	 tb_abstract.setBID(bid);
    	 Tb_abstractResult tavola = new Tb_abstractResult(tb_abstract);
    	 tavola.cancellaAbstract(tb_abstract);
    }



	/**
	 * Method aggiornaDescrittore.
	 * @param tb_descrittore
	 * @param codiceUtente
	 * @throws InfrastructureException
	 */
	public void aggiornaAbstract(
		Tb_abstract 	tb_abstract,
		String 			codiceUtente) throws EccezioneDB, InfrastructureException {
		tb_abstract.setUTE_VAR(codiceUtente);

		Tb_abstractResult tavola = new Tb_abstractResult(tb_abstract);

		tavola.update(tb_abstract);

	}

}
