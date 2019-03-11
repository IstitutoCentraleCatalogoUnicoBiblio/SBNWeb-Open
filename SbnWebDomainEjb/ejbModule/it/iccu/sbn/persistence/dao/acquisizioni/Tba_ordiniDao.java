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
package it.iccu.sbn.persistence.dao.acquisizioni;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_parametri_buono_ordine;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_parametri_ordine;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_carrello_spedizione;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordine_inventari;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_ordini_biblioteche;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_numero_std;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.exception.DataException;




public class Tba_ordiniDao extends DaoManager {

	public Tba_ordini getOrdineById(int id_ordine) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Tba_ordini o = (Tba_ordini) session.load(Tba_ordini.class, new Integer(id_ordine));
		return o;
	}

	public Tba_ordini getOrdine(String codPolo, String codBib, int annoOrd, char tipoOrd, int codOrd) throws DaoManagerException {
		try {
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			Session session = getCurrentSession();
			Criteria o = session.createCriteria(Tba_ordini.class);
			o.add(Restrictions.ne("fl_canc", 'S'));
			o.add(Restrictions.eq("cd_bib", bib));
			o.add(Restrictions.eq("anno_ord", new BigDecimal(annoOrd)));
			o.add(Restrictions.eq("cod_tip_ord", tipoOrd));
			o.add(Restrictions.eq("cod_ord", new Integer(codOrd)));

			return (Tba_ordini) o.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public List<Tba_ordini> getRicercaListaOrdiniHib(ListaSuppOrdiniVO ricercaOrdini) throws DaoManagerException {
		List<Tba_ordini> results =null ;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_ordini.class, "father_ord");

			Tbf_polo polo = new Tbf_polo();
			if (ricercaOrdini.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaOrdini.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaOrdini.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaOrdini.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			Tbr_fornitori forn = new Tbr_fornitori();
			//forn.setNom_fornitore(ricercaOrdini.getFornitore().getDescrizione());
			//forn.setCod_fornitore(Integer.parseInt(ricercaOrdini.getFornitore().getCodice()));

			Tbb_capitoli_bilanci capBil = new Tbb_capitoli_bilanci();
			//capBil.setEsercizio(BigDecimal.valueOf(Long.parseLong(ricercaOrdini.getBilancio().getCodice1())));
			//capBil.setCapitolo(BigDecimal.valueOf(Long.parseLong(ricercaOrdini.getBilancio().getCodice2())));

			Tbb_bilanci bil = new Tbb_bilanci();
			//bil.setCod_mat(ricercaOrdini.getBilancio().getCodice3().charAt(0));


			if (ricercaOrdini.getBidList()!=null && ricercaOrdini.getBidList().size()!=0 )
			{
				cr.add(Restrictions.in("bid",  ricercaOrdini.getBidList()));
			}
			if (ricercaOrdini.getIdOrdList()!=null && ricercaOrdini.getIdOrdList().size()!=0 )
			{
				cr.add(Restrictions.in("id_ordine",  ricercaOrdini.getIdOrdList()));
			}

			if (ricercaOrdini.getIDOrd()>0 )
			{
				cr.add(Restrictions.eq("id_ordine", ricercaOrdini.getIDOrd()));
			}
			if (ricercaOrdini.getCodOrdine()!=null && ricercaOrdini.getCodOrdine().length()!=0 )
			{
				cr.add(Restrictions.eq("cod_ord", Integer.parseInt(ricercaOrdini.getCodOrdine())));
			}
			if (ricercaOrdini.getTipoOrdine()!=null && ricercaOrdini.getTipoOrdine().length()!=0 )
			{
				cr.add(Restrictions.eq("cod_tip_ord", ricercaOrdini.getTipoOrdine()));
			}
			if (ricercaOrdini.getAnnoOrdine()!=null && ricercaOrdini.getAnnoOrdine().length()!=0)
			{
				cr.add(Restrictions.eq("anno_ord",  BigDecimal.valueOf(Double.valueOf(ricercaOrdini.getAnnoOrdine()))));
			}
			if (ricercaOrdini.getNaturaOrdine()!=null && ricercaOrdini.getNaturaOrdine().length()!=0 )
			{
				cr.add(Restrictions.eq("natura", ricercaOrdini.getNaturaOrdine()));
			}
			if (ricercaOrdini.getStatoOrdine()!=null && ricercaOrdini.getStatoOrdine().length()!=0 )
			{
				cr.add(Restrictions.eq("stato_ordine", ricercaOrdini.getStatoOrdine()));
			}
			if (ricercaOrdini.getSezioneAcqOrdine()!=null && ricercaOrdini.getSezioneAcqOrdine().length()!=0 )
			{
				//cr.add(Restrictions.eq("id_sez_acquis_bibliografiche.cod_sezione", ricercaOrdini.getSezioneAcqOrdine()).ignoreCase());
				DetachedCriteria childCriteriaSez = DetachedCriteria.forClass(Tba_sez_acquis_bibliografiche.class , "child_sez");
				childCriteriaSez.setProjection(Property.forName("child_sez.id_sez_acquis_bibliografiche"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaSez.add(Restrictions.eq("child_sez.cod_sezione",ricercaOrdini.getSezioneAcqOrdine().trim()).ignoreCase());
				childCriteriaSez.add(Restrictions.eq("child_sez.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaSez));
				cr.add(Property.forName("id_sez_acquis_bibliografiche").in( childCriteriaSez));
			}
			// potrebbe essere così
			if (ricercaOrdini.getStatoOrdineArr()!=null && ricercaOrdini.getStatoOrdineArr().length!=0 )
			{
				cr.add(Restrictions.in("stato_ordine",  ricercaOrdini.getStatoOrdineArr()));
			}
			if (ricercaOrdini.getTipoOrdineArr()!=null && ricercaOrdini.getTipoOrdineArr().length!=0 )
			{
				cr.add(Restrictions.in("cod_tip_ord",  ricercaOrdini.getTipoOrdineArr()));
			}

			if (ricercaOrdini.isAttivatoDaRicerca())
			{
				cr.add(Restrictions.eq("rinnovato", ricercaOrdini.isRinnovato()));
				cr.add(Restrictions.eq("stampato",  ricercaOrdini.isStampato()));
			}
			else
			{
				if ( ricercaOrdini.isRinnovato())
				{
					cr.add(Restrictions.eq("rinnovato", true));
				}

				if ( ricercaOrdini.isStampato())
				{
					cr.add(Restrictions.eq("stampato", true));
				}
			}

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;

			if ((ricercaOrdini.getDataOrdineDa()!=null && ricercaOrdini.getDataOrdineDa().length()!=0) || (ricercaOrdini.getDataOrdineA()!=null && ricercaOrdini.getDataOrdineA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataOrdineDa()),Integer.parseInt(ricercaOrdini.getDataOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaOrdini.getDataOrdineDa());
					dataInizioBool=true;

				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date endDate=new Date();
				try {
					endDate = formato.parse(ricercaOrdini.getDataOrdineA());
					dataFineBool=true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataInizioBool && dataFineBool)
				{
					cr.add(Restrictions.between("data_ord", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_ord", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_ord", new Date(endDate.getTime())));
				}

			}

			dataInizioBool=false;
			dataFineBool=false;
			if ((ricercaOrdini.getDataStampaOrdineDa()!=null && ricercaOrdini.getDataStampaOrdineDa().length()!=0) || (ricercaOrdini.getDataStampaOrdineA()!=null && ricercaOrdini.getDataStampaOrdineA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataStampaOrdineDa()), Integer.parseInt(ricercaOrdini.getDataStampaOrdineA())));
				//cr.add(Restrictions.eq("stampato", true));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaOrdini.getDataStampaOrdineDa());
					dataInizioBool=true;

				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date endDate=new Date();
				try {
					endDate = formato.parse(ricercaOrdini.getDataStampaOrdineA());
					dataFineBool=true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataInizioBool && dataFineBool)
				{
					cr.add(Restrictions.between("data_ord", new Date(startDate.getTime()), new Date(endDate.getTime())));
					cr.add(Restrictions.eq("stampato", true));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_ord", new Date(startDate.getTime())));
					cr.add(Restrictions.eq("stampato", true));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_ord", new Date(endDate.getTime())));
					cr.add(Restrictions.eq("stampato", true));
				}

			}

			dataInizioBool=false;
			dataFineBool=false;
			if ((ricercaOrdini.getDataFineAbbOrdineDa()!=null && ricercaOrdini.getDataFineAbbOrdineDa().length()!=0) || (ricercaOrdini.getDataFineAbbOrdineA()!=null && ricercaOrdini.getDataFineAbbOrdineA().length()!=0) )
			{
				//cr.add(Restrictions.between("data_fine", Integer.parseInt(ricercaOrdini.getDataFineAbbOrdineDa()), Integer.parseInt(ricercaOrdini.getDataFineAbbOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaOrdini.getDataFineAbbOrdineDa());
					dataInizioBool=true;

				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date endDate=new Date();
				try {
					endDate = formato.parse(ricercaOrdini.getDataFineAbbOrdineA());
					dataFineBool=true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataInizioBool && dataFineBool)
				{
					cr.add(Restrictions.between("data_fine", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_fine", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_fine", new Date(endDate.getTime())));
				}

			}

			if (ricercaOrdini.getTipoInvioOrdine()!=null && ricercaOrdini.getTipoInvioOrdine().length()!=0 )
			{
				cr.add(Restrictions.eq("tipo_invio", ricercaOrdini.getTipoInvioOrdine()));
			}
			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("01"))
			{
				cr.add(Restrictions.eq("continuativo", "1"));
			}

			if (ricercaOrdini.getContinuativo()!=null && ricercaOrdini.getContinuativo().length()!=0 && ricercaOrdini.getContinuativo().equals("00"))
			{
				cr.add(Restrictions.ne("continuativo", "1"));
			}

			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getDescrizione()!=null && ricercaOrdini.getFornitore().getDescrizione().length()!=0 )
			{
				DetachedCriteria childCriteriaForn = DetachedCriteria.forClass(Tbr_fornitori.class , "child_forn");
				childCriteriaForn.setProjection(Property.forName("child_forn.cod_fornitore"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaForn.add(Restrictions.eq("child_forn.nom_fornitore",ricercaOrdini.getFornitore().getDescrizione().trim()).ignoreCase());
				childCriteriaForn.add(Restrictions.eq("child_forn.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaForn));
				cr.add(Property.forName("cod_fornitore.cod_fornitore").in( childCriteriaForn));

/*
				forn.setNom_fornitore(ricercaOrdini.getFornitore().getDescrizione());
				//forn.setCod_fornitore(Integer.parseInt(ricercaOrdini.getFornitore().getCodice()));
				cr.add(Restrictions.eq("cod_fornitore", forn.getChiave_for()));
				cr.add(Restrictions.eq("nom_fornitore",ricercaOrdini.getFornitore().getDescrizione() ));*/
			}
			if (ricercaOrdini.getFornitore()!=null  && ricercaOrdini.getFornitore().getCodice()!=null && ricercaOrdini.getFornitore().getCodice().length()!=0 )
			{
				cr.add(Restrictions.eq("cod_fornitore", forn.getChiave_for()));
				//cr.add(Restrictions.eq("cod_fornitore",ricercaOrdini.getFornitore().getCodice() ));
			}
			if ((ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice1()!=null && ricercaOrdini.getBilancio().getCodice1().length()!=0 ) || (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice2()!=null && ricercaOrdini.getBilancio().getCodice2().length()!=0 ))
			{
				DetachedCriteria childCriteriaBil = DetachedCriteria.forClass(Tbb_capitoli_bilanci.class , "child_bil");
				childCriteriaBil.setProjection(Property.forName("child_bil.id_capitoli_bilanci" ) );
				//childCriteriaBil.add(Property.forName("cod_mat.id_capitoli_bilanci.id_capitoli_bilanci").eqProperty("child_bil.id_capitoli_bilanci"));
				if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice1()!=null && ricercaOrdini.getBilancio().getCodice1().length()!=0 )
				{
					childCriteriaBil.add(Restrictions.eq("child_bil.esercizio",BigDecimal.valueOf(Double.valueOf(ricercaOrdini.getBilancio().getCodice1().trim()))));
				}
				if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice2()!=null && ricercaOrdini.getBilancio().getCodice2().length()!=0 )
				{
					childCriteriaBil.add(Restrictions.eq("child_bil.capitolo",BigDecimal.valueOf(Double.valueOf(ricercaOrdini.getBilancio().getCodice2().trim()))));
				}
				childCriteriaBil.add(Restrictions.eq("child_bil.fl_canc", 'N'));
				childCriteriaBil.add(Restrictions.eq("child_bil.cd_bib", bib));
				cr.add(Subqueries.exists(childCriteriaBil));
				cr.add(Property.forName("tbb_bilancicod_mat.id_capitoli_bilanci.id_capitoli_bilanci").in(childCriteriaBil));
			}

/*			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice1()!=null && ricercaOrdini.getBilancio().getCodice1().length()!=0 )
			{
				capBil.setEsercizio(BigDecimal.valueOf(Long.parseLong(ricercaOrdini.getBilancio().getCodice1())));
				cr.add(Restrictions.eq("id_capitoli_bilanci", capBil.getId_capitoli_bilanci()));
			}
			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice2()!=null && ricercaOrdini.getBilancio().getCodice2().length()!=0 )
			{
				capBil.setCapitolo(BigDecimal.valueOf(Long.parseLong(ricercaOrdini.getBilancio().getCodice2())));
				cr.add(Restrictions.eq("id_capitoli_bilanci", capBil.getId_capitoli_bilanci()));
			}*/
			if (ricercaOrdini.getBilancio()!=null && ricercaOrdini.getBilancio().getCodice3()!=null && ricercaOrdini.getBilancio().getCodice3().length()!=0 )
			{
				bil.setCod_mat(ricercaOrdini.getBilancio().getCodice3().charAt(0));
				//cr.add(Restrictions.eq("tbb_bilancicod_mat", bil.getId_capitoli_bilanci()));
				cr.add(Restrictions.eq("tbb_bilancicod_mat.cod_mat", ricercaOrdini.getBilancio().getCodice3().charAt(0)));


			}
			if (ricercaOrdini.getTitolo()!=null && ricercaOrdini.getTitolo().getCodice()!=null && ricercaOrdini.getTitolo().getCodice().length()!=0)
			{
				//Tb_titolo titoloDoc = new Tb_titolo();
				//titoloDoc.setBid(coll.getBidDoc());
				cr.add(Restrictions.eq("bid", ricercaOrdini.getTitolo().getCodice()));
			}
			if (!ricercaOrdini.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}
			if (ricercaOrdini.getCodBibl()!=null && ricercaOrdini.getCodBibl().trim().length()>0 &&  ricercaOrdini.getCodPolo()!=null && ricercaOrdini.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			//	TODO  gestire le biblioteche affiliate
			if (ricercaOrdini.getCodBiblAffil()!=null && ricercaOrdini.getCodBiblAffil().length()!=0 )
			{
				DetachedCriteria childCriteriaOrdBib = DetachedCriteria.forClass(Tra_ordini_biblioteche.class , "child_ord_bib");
				childCriteriaOrdBib.setProjection(Property.forName("child_ord_bib.cod_bib_ord"));
				childCriteriaOrdBib.add( Property.forName("child_ord_bib.anno_ord").eqProperty("father_ord.anno_ord") );
				childCriteriaOrdBib.add( Property.forName("child_ord_bib.cod_tip_ord").eqProperty("father_ord.cod_tip_ord") );
				childCriteriaOrdBib.add( Property.forName("child_ord_bib.cod_ord").eqProperty("father_ord.cod_ord") );
				childCriteriaOrdBib.add(Restrictions.eq("child_ord_bib.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaOrdBib));
				cr.add(Property.forName("cd_bib.cd_biblioteca").in( childCriteriaOrdBib));
			}

			if (ricercaOrdini.getOrdinamento()==null || (ricercaOrdini.getOrdinamento()!=null && ricercaOrdini.getOrdinamento().equals("")))
			{
				//sql=sql + "    order by ord.cd_bib, ord.anno_ord desc, ord.cod_tip_ord, ord.cod_ord, ord.data_ord desc  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("anno_ord"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.asc("cod_ord"));
				cr.addOrder(Order.desc("data_ord"));

			}
			else if (ricercaOrdini.getOrdinamento().equals("1"))
			{
				//sql=sql + "  order by ord.cd_bib, ord.cod_tip_ord, ord.cod_ord ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.asc("cod_ord"));
			}
			else if (ricercaOrdini.getOrdinamento().equals("2"))
			{
				//sql=sql + "  order by ord.cd_bib , ord.anno_ord desc, ord.cod_tip_ord , ord.cod_ord desc";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("anno_ord"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.desc("cod_ord"));
			}

			else if (ricercaOrdini.getOrdinamento().equals("3"))
			{
				//sql=sql + "  order by ord.cd_bib, lower(forn.nom_fornitore) ";
				cr.addOrder(Order.asc("cd_bib"));
				//cr.addOrder(Order.asc("nom_fornitore".toLowerCase()));

			}
			else if (ricercaOrdini.getOrdinamento().equals("4"))
			{
				//sql=sql + "  order by ord.cd_bib, ord.data_ord desc ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("data_ord"));
			}
			else if (ricercaOrdini.getOrdinamento().equals("5"))
			{
				//sql=sql + "  order by ord.cd_bib, ord.stato_ordine ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("stato_ordine"));
			}
			else if (ricercaOrdini.getOrdinamento().equals("6"))
			{
				//sql=sql + "  order by ord.cd_bib, capBil.esercizio, capBil.capitolo ";   // , bil.cod_mat (se aggiunto bisogna cambiare la select distinct per la molteplicita)
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("esercizio"));
				cr.addOrder(Order.asc("capitolo"));
			}
			else if (ricercaOrdini.getOrdinamento().equals("7")) // per le stampe
			{
				//sql=sql + "  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord , ord.cod_ord ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_fornitore"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.asc("cod_ord"));

			}
			else if (ricercaOrdini.getOrdinamento().equals("8")) // per le LISTE DA DOCUMENTO FISICO
			{
				//sql=sql + "  order by ord.cd_bib, ord.bid ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("bid"));
			}
			else if (ricercaOrdini.getOrdinamento().equals("9")) // per le stampe con la gestione dei ristampati
			{
				//sql=sql + "  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord, ord.cod_ord, ord.data_ord ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_fornitore"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.asc("cod_ord"));
				cr.addOrder(Order.asc("data_ord"));

			}
			else if (ricercaOrdini.getOrdinamento().equals("10")) // per le stampe con la gestione dei ristampati
			{
				//sql=sql + "  order by ord.cd_bib, ord.cod_fornitore, ord.cod_tip_ord, ord.cod_ord, ord.data_ord desc";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_fornitore"));
				cr.addOrder(Order.asc("cod_tip_ord"));
				cr.addOrder(Order.asc("cod_ord"));
				cr.addOrder(Order.desc("data_ord"));
			}

			results = cr.list();

			if (results!=null && results.size()>0 && ricercaOrdini.getOrdinamento()!=null && ricercaOrdini.getOrdinamento().equals("3"))
			{
				Comparator comp=null;
				comp =new OrdComparator();
				Collections.sort(results, comp);
			}


			if (results==null || (results!=null && results.size()==0))
			{
				results=null;
			}

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return results;

	}


	public TitoloACQVO getTitoloOrdineTerHib(String bidPassato) throws DaoManagerException {
		List results = null;
		TitoloACQVO tit=null;

		try {
			Session session = this.getCurrentSession();
			String[] arrProva=new String[]{"I","J","M","L","E","G"};

			Criteria cr = session.createCriteria(Tb_titolo.class, "t");

/*			cr.createAlias("t.Tb_numero_std", "n", Criteria.LEFT_JOIN);
			cr.add(Restrictions.and(Restrictions.ne("n.fl_canc",'S'),Restrictions.in("n.tp_numero_std",arrProva)));
			cr.setProjection(Projections.projectionList()
			        .add(Projections.property("t.bid"))
			        .add(Projections.property("t.isbd"))
					.add(Projections.property("t.cd_natura"))
					.add(Projections.property("t.cd_paese"))
					.add(Projections.property("t.cd_lingua_1"))
					.add(Projections.property("t.cd_lingua_2"))
					.add(Projections.property("t.cd_lingua_3"))
					.add(Projections.property("t.indice_isbd"))
					.add(Projections.property("n.tp_numero_std"))
					.add(Projections.property("n.numero_std"))
					//.add(Projections.property("c.ds_tabella"))

			);*/

			if (bidPassato!=null)
			{
				cr.add(Restrictions.eq("t.bid", bidPassato));
			}
			cr.add(Restrictions.ne("t.fl_canc",'S'));

			//cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			//results = (List<Tb_titolo>) cr.uniqueResult();
			results = cr.list();

/*			String sqlString="select t.bid, t.isbd, t.cd_natura,t.cd_paese,t.cd_lingua_1,t.cd_lingua_2,t.cd_lingua_3,t.indice_isbd,n.tp_numero_std,  n.numero_std , tcod.ds_tabella " ;
			sqlString=sqlString + " from Tb_titolo t ";
			sqlString=sqlString + " left join Tb_numero_std  n on  n.bid=t.bid and n.fl_canc<>'S' and n.tp_numero_std in ('I','J','M','L','E','G') "; //ok
			sqlString=sqlString + " left join tb_codici tcod on tcod.tp_tabella='NSTD' and  n.tp_numero_std=tcod.cd_tabella"  ; // ok la property b  è definita nel mapping
			sqlString=sqlString +" where t.fl_canc<>'S'"; //ok
*/




			//bidPassato="BVE0036760"; // RAV0028330 CFI0232619 CFI0117938(ha 3 numeri standard)

			int numRighe=0;
			List arrivo =new ArrayList();
			String[] arrLingua=new String[3];
			List<StrutturaTerna> componiNumStdArr=new ArrayList<StrutturaTerna>() ;
			StrutturaTerna componiNumStd=new StrutturaTerna();

			if (results!=null && results.size()==1)
			{
				//for (int i=0; i <  results.size(); i++)
				//	{
					numRighe=numRighe+1;
					TitoloACQVO arrivoUno=new TitoloACQVO();
					Tb_titolo rec =(Tb_titolo) results.get(0);

					if (rec.getCd_lingua_1()!=null ) {
						arrLingua[0] =rec.getCd_lingua_1().toString();
						if (rec.getCd_lingua_2()!= null)
						{
							arrLingua[1] = rec.getCd_lingua_2().toString();
						}
						if (rec.getCd_lingua_3() != null)
						{
							arrLingua[2] =rec.getCd_lingua_3().toString();
						}
					}
					arrivoUno.setIsbd(rec.getIsbd());
					arrivoUno.setIndiceISBD(rec.getIndice_isbd());
					arrivoUno.setArrCodLingua(arrLingua);
					arrivoUno.setNatura(String.valueOf(rec.getCd_natura()));
					arrivoUno.setCodPaese("");
					if (rec.getCd_paese()!=null)
					{
						arrivoUno.setCodPaese(rec.getCd_paese());
					}
					arrivoUno.setBid(rec.getBid());
					arrivoUno.setNumStandard("");

					Object [] elencoNumStd=rec.getTb_numero_std().toArray();
					// vanno prima adeguati tra_ordine_inventari in StrutturaInventariOrdVO prima del set successivo
					//rec.setRigheInventariRilegatura(elencoAtt);
					List<StrutturaInventariOrdVO> arrayInv = new ArrayList<StrutturaInventariOrdVO >();
					for (int j = 0; j<elencoNumStd.length; j++) {
						Tb_numero_std eleNumStd = (Tb_numero_std) elencoNumStd[j];
						// condizione sui codici
						if (eleNumStd.getTp_numero_std()!=null && (eleNumStd.getTp_numero_std().equals('I') || eleNumStd.getTp_numero_std().equals('J') || eleNumStd.getTp_numero_std().equals('M') || eleNumStd.getTp_numero_std().equals('L') || eleNumStd.getTp_numero_std().equals('E') || eleNumStd.getTp_numero_std().equals('G')));  //('I','J','M','L','E','G'))
						{
							arrivoUno.setNumStandard(eleNumStd.getTp_numero_std()+ eleNumStd.getNumero_std() );
							//compongo il numero standard
							componiNumStd=new StrutturaTerna("","","");
							if (eleNumStd.getTp_numero_std()!=null)
							{
								componiNumStd.setCodice1(eleNumStd.getTp_numero_std().trim());
								// lettura tabella codici per denominazioni di tipo numero standard
/*								Criteria crCod = session.createCriteria(Tb_codici.class, "c");
								crCod.add(Restrictions.eq("c.tp_tabella","NSTD"));
								crCod.add(Restrictions.eq("c.cd_tabella",eleNumStd.getTp_numero_std().trim() ));
								crCod.setProjection(Projections.projectionList()
										.add(Projections.property("c.ds_tabella"))
								);
								List<String> denoresults = (List<String>) crCod.list();
								if (denoresults!=null && denoresults.size()>0)
								{
									componiNumStd.setCodice3(denoresults.get(0).trim());
								}
*/								String denoresults = this.getCodiciDaTbCodici("NSTD",eleNumStd.getTp_numero_std());
								if (denoresults!=null && denoresults.trim().length()>0)
								{
									componiNumStd.setCodice3(denoresults);
								}

							}
							if (eleNumStd.getNumero_std()!=null)
							{
								componiNumStd.setCodice2(eleNumStd.getNumero_std().trim());
							}

							// da prendere su tb_codici
	/*						if (eleNumStd.getDs_tabella()!=null)
							{
								componiNumStd.setCodice3(eleNumStd.getDs_tabella().trim());
							}*/
							componiNumStdArr.add(componiNumStd);
						}
					}
					arrivo.add(arrivoUno);
				//}
			}

			// la molteplicità dei numeri standard va ridotta au un unico elemento con array dei numeristandard
			if (arrivo!=null && arrivo.size()>0)
			{
				List results2=new ArrayList();
/*				if (componiNumStd!=null && componiNumStd.length()>0)
				{
					((TitoloACQVO)results.get(0)).setNumStandard(componiNumStd);
				}
*/				if (componiNumStdArr!=null && componiNumStdArr.size()>0)
				{
					((TitoloACQVO)arrivo.get(0)).setNumStandardArr(componiNumStdArr);
				}
				results2.add(arrivo.get(0)); // considero solo il primo e compongo il numero standard

				results=results2;


			}


			if (results==null || (results!=null && results.size()==0) )
			{
				results=null;
			}
			// inizio innesto
			String tit_isbd="";

			if (results!=null && results.size()>0)
			{
				tit = (TitoloACQVO) results.get(0);
				tit_isbd=tit.getIsbd();
				//recTit.setIsbd(tit.getIsbd());
				if  (!tit_isbd.equals("") &&  tit_isbd.length()>200)
				{
					// gestione composizione titolo
					String indice_isbd=tit.getIndiceISBD();
					String[] isbd_composto=tit_isbd.split("\\. -");
					// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
					String tit_primaParte=isbd_composto[0];
					String tit_secondaParte=isbd_composto[1];
					String[] indice_isbd_composto=indice_isbd.split("\\;");
					String  tit_secondaParte_finale="";
					String  tit_primaParte_finale="";
					String  tit_isbd_finale="";

					if (indice_isbd_composto.length>0 && indice_isbd_composto.length>=1)
					{
						//for (int y = 0; y < indice_isbd_composto.length; y++)
						//{
						if (indice_isbd_composto[0]!=null && indice_isbd_composto[0].length()!=0)
						{
							String primaParte=indice_isbd_composto[0];
							String[] primaParte_composto=primaParte.split("-");
							String pos_primaParte=primaParte_composto[1];

							if (tit_primaParte.length()>100)
							{
								tit_primaParte_finale=tit_primaParte.substring(0,100);
							}
							else
							{
								tit_primaParte_finale=tit_primaParte;
							}
						}
						if (indice_isbd_composto[1]!=null && indice_isbd_composto[1].length()!=0)
						{
							String secondaParte=indice_isbd_composto[1];
							String[] secondaParte_composto=secondaParte.split("\\-");
							String pos_secondaParte=secondaParte_composto[1];
							if (tit_secondaParte.length()>100)
							{
								tit_secondaParte_finale=tit_secondaParte.substring(0,100);
							}
							else
							{
								tit_secondaParte_finale=tit_secondaParte;
							}
						}
						//}
					}
					if (!tit_primaParte_finale.equals("") &&  tit_primaParte_finale.length()>0)
					{
						tit_isbd_finale=tit_primaParte_finale.trim();
						if ( !tit_secondaParte_finale.equals("") &&  tit_secondaParte_finale.length()>0 )
						{
							tit_isbd_finale=tit_isbd_finale + ". - " +  tit_secondaParte_finale.trim();
						}
					}
					if (!tit_isbd_finale.equals("") &&  tit_isbd_finale.length()>0)
					{
						tit.setIsbd(tit_isbd_finale);
					}
				}
			// fine innesto
			}
			} catch (Exception e) {
				e.printStackTrace();
				//log.error(e);
				// TODO Auto-generated catch block
/*				try{
					if (!connectionTer.isClosed())
			      	  {
							connectionTer.close();
			      	  }
				} catch (SQLException se) {
					se.printStackTrace();
				}	*/
			}
			return tit;
	}




	public ConfigurazioneORDVO loadConfigurazioneOrdiniHib(ConfigurazioneORDVO configurazione) throws DataException, ApplicationException , ValidationException
	{
		//this.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	ConfigurazioneORDVO rec = null;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_parametri_ordine.class);

			Tbf_polo polo = new Tbf_polo();
			if (configurazione.getCodPolo()!=null)
			{
				polo.setCd_polo(configurazione.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (configurazione.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(configurazione.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			if (configurazione.getCodBibl()!=null)
			{
				cr.add(Restrictions.eq("cd_biblioteca", bib));
			}
			cr.add(Restrictions.ne("fl_canc",'S'));
			cr.addOrder(Order.asc("cd_biblioteca"));

			List<Tba_parametri_ordine> resultAppo=cr.list();

			// RIEMPIMENTO DI CONFIGURAZIONEORDVO

			// numero di righe del resultset
			int numRighe=0;
			rec = new ConfigurazioneORDVO();

			for (int i=0; i <  resultAppo.size(); i++)
			{
				Tba_parametri_ordine oggConfBO= resultAppo.get(i);
				numRighe=numRighe+1;
				if (numRighe==1) //  solo la prima volta
				{
	    			Tbf_biblioteca_in_polo appoBib = oggConfBO.getCd_biblioteca();
	              	rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
	    			rec.setCodBibl(appoBib.getCd_biblioteca());
					rec.setDenoBibl(appoBib.getId_biblioteca().getNom_biblioteca());
					rec.setDataUpd(oggConfBO.getTs_var());
					rec.setGestioneBilancio(true);
					if ( oggConfBO.getGest_bil()=='N')
					{
						rec.setGestioneBilancio(false);
					}
					rec.setGestioneSezione(true);
					if (oggConfBO.getGest_sez()=='N')
					{
						rec.setGestioneSezione(false);
					}
					rec.setGestioneProfilo(true);
					if (oggConfBO.getGest_prof()=='N')
					{
						rec.setGestioneProfilo(false);
					}

					rec.setCodiceProfilo(String.valueOf(oggConfBO.getCod_prac()));
					rec.setCodiceSezione(oggConfBO.getCod_sezione());
					rec.setChiaveBilancio(new StrutturaTerna("","",""));

					BigDecimal anno_esercizio = oggConfBO.getEsercizio();
					//webVO.setAnnoEdizione(anno_edizione != null ? anno_edizione.toPlainString() : null);

					if (anno_esercizio!=null && anno_esercizio.intValue() > 0)
					{
						rec.getChiaveBilancio().setCodice1(oggConfBO.getEsercizio().toString());
					}
					BigDecimal capitolo = oggConfBO.getCapitolo();

					if (capitolo!=null && capitolo.intValue() > 0)
					{
						rec.getChiaveBilancio().setCodice2(oggConfBO.getCapitolo().toString());
					}
					rec.getChiaveBilancio().setCodice3(oggConfBO.getCod_mat());
					// il costruttore deve avere già creato la struttura e parzialmente riempita con i codici
					String[] codiciForn=new String[6];
					codiciForn[0]="";
					if (oggConfBO.getCod_fornitore_a()!=null &&  oggConfBO.getCod_fornitore_a()>0)
					{
						rec.getFornitoriDefault()[0].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_a()));
						codiciForn[0]=String.valueOf(oggConfBO.getCod_fornitore_a());
					}
					codiciForn[1]="";
					if (oggConfBO.getCod_fornitore_l()!=null &&  oggConfBO.getCod_fornitore_l()>0)
					{
						rec.getFornitoriDefault()[1].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_l()));
						codiciForn[1]=String.valueOf(oggConfBO.getCod_fornitore_l());
					}
					codiciForn[2]="";
					if (oggConfBO.getCod_fornitore_d()!=null &&  oggConfBO.getCod_fornitore_d()>0)
					{
						rec.getFornitoriDefault()[2].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_d()));
						codiciForn[2]=String.valueOf(oggConfBO.getCod_fornitore_d());
					}
					codiciForn[3]="";
					if (oggConfBO.getCod_fornitore_v()!=null &&  oggConfBO.getCod_fornitore_v()>0)
					{
						rec.getFornitoriDefault()[3].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_v()));
						codiciForn[3]=String.valueOf(oggConfBO.getCod_fornitore_v());
					}
					codiciForn[4]="";
					if (oggConfBO.getCod_fornitore_c()!=null  &&  oggConfBO.getCod_fornitore_c()>0)
					{
						rec.getFornitoriDefault()[4].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_c()));
						codiciForn[4]=String.valueOf(oggConfBO.getCod_fornitore_c());
					}
					codiciForn[5]="";
					if (oggConfBO.getCod_fornitore_r()!=null &&  oggConfBO.getCod_fornitore_r()>0)
					{
						rec.getFornitoriDefault()[5].setDescrizione(String.valueOf(oggConfBO.getCod_fornitore_r()));
						codiciForn[5]=String.valueOf(oggConfBO.getCod_fornitore_r());
					}
					rec.setCodiciFornitore(codiciForn);

					rec.setOrdiniAperti(false);
					if (oggConfBO.getOrdini_aperti().equals("Y"))
					{
						rec.setOrdiniAperti(true);
					}
					rec.setOrdiniChiusi(false);
					if  (oggConfBO.getOrdini_chiusi().equals("Y"))
					{
						rec.setOrdiniChiusi(true);
					}
					rec.setOrdiniAnnullati(false);
					if ( oggConfBO.getOrdini_annullati().equals("Y"))
					{
						rec.setOrdiniAnnullati(true);
					}
					rec.setAllineamento(oggConfBO.getAllineamento_prezzo());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return rec;
		}



	public ConfigurazioneBOVO loadConfigurazioneHib(ConfigurazioneBOVO configurazione) throws DataException, ApplicationException , ValidationException
	{
		//this.ValidaBuoniOrdineVO (buonoOrd);
		boolean valRitorno=false;
    	int motivo=0;
    	ConfigurazioneBOVO result = null;
		ConfigurazioneBOVO rec = null;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_parametri_buono_ordine.class);

			Tbf_polo polo = new Tbf_polo();
			if (configurazione.getCodPolo()!=null)
			{
				polo.setCd_polo(configurazione.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (configurazione.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(configurazione.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			if (configurazione.getCodBibl()!=null)
			{
				cr.add(Restrictions.eq("cd_biblioteca", bib));
			}
			cr.add(Restrictions.ne("fl_canc",'S'));


/*				String sql0="select parBO.*, parBO.ts_var as dataUpd, bibliot.nom_biblioteca ";
				sql0=sql0  + " from  tba_parametri_buono_ordine parBO ";
				sql0=sql0  + " join tbf_biblioteca bibliot on bibliot.cd_bib=parBO.cd_biblioteca and bibliot.cd_polo=parBO.cd_polo and bibliot.fl_canc<>'S' ";
				sql0=this.struttura(sql0);
				sql0=sql0 + " parBO.fl_canc='N'";
				sql0=sql0  + " order by  parBO.cd_biblioteca, parBO.progr ";

*/

			cr.addOrder(Order.asc("cd_biblioteca"));
			cr.addOrder(Order.asc("progr"));

			List<Tba_parametri_buono_ordine> resultAppo=cr.list();
			// RIEMPIMENTO DI CONFIGURAZIONEBOVO



			// numero di righe del resultset
				int numRighe=0;
				List<StrutturaTerna> righeRisultato=new ArrayList();
				rec = new ConfigurazioneBOVO();

				//Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
				for (int i=0; i <  resultAppo.size(); i++)
				{
					Tba_parametri_buono_ordine oggConfBO= resultAppo.get(i);
					numRighe=numRighe+1;
					if (numRighe==1) //  solo la prima volta
					{
		    			Tbf_biblioteca_in_polo appoBib = oggConfBO.getCd_biblioteca();
		              	rec.setCodPolo(appoBib.getCd_polo().getCd_polo());
		    			rec.setCodBibl(appoBib.getCd_biblioteca());
						rec.setDenoBibl(appoBib.getId_biblioteca().getNom_biblioteca());
						rec.setDataUpd(oggConfBO.getTs_var());
						if (oggConfBO.getCodice_buono()=='A') // progressivo automatico
						{
							rec.setNumAutomatica(true);
						}
						if (oggConfBO.getCodice_buono()=='M') // progressivo manuale
						{
							rec.setNumAutomatica(false);
						}
						// campi nuovi
						if (oggConfBO.getDescr_formulaintr()!=null)
						{
							rec.leggiStringa(oggConfBO.getDescr_formulaintr(), "I");
						}
						if (oggConfBO.getDescr_oggetto()!=null)
						{
							rec.leggiStringa( oggConfBO.getDescr_oggetto() , "O");
						}
						rec.caricaAree(String.valueOf(oggConfBO.getArea_titolo()), String.valueOf(oggConfBO.getArea_ediz()), String.valueOf(oggConfBO.getArea_num()), String.valueOf(oggConfBO.getArea_pub()));
						if (oggConfBO.isLogo() && oggConfBO.getLogo_img()!=null && oggConfBO.getLogo_img().trim().length()>0)
						{
							rec.setPresenzaLogoImg(true);
							rec.setNomeLogoImg(oggConfBO.getLogo_img().trim().replace("\\\\","\\")); //replace("\\\\","\\")
						}
						else
						{
							rec.setPresenzaLogoImg(false);
							rec.setNomeLogoImg(null);
						}
						if (oggConfBO.isFirmadigit() && oggConfBO.getFirmadigit_img()!=null && oggConfBO.getFirmadigit_img().trim().length()>0)
						{
							rec.setPresenzaFirmaImg(true);
							rec.setNomeFirmaImg(oggConfBO.getFirmadigit_img().trim(). replace("\\\\","\\")); //replace("\\\\","\\")
						}
						else
						{
							rec.setPresenzaFirmaImg(false);
							rec.setNomeFirmaImg(null);
						}
						if (oggConfBO.isPrezzo())
						{
							rec.setPresenzaPrezzo(true);
						}
						else
						{
							rec.setPresenzaPrezzo(false);
						}
						if (oggConfBO.isNprot())
						{
							rec.setEtichettaProtocollo(true);
						}
						else
						{
							rec.setEtichettaProtocollo(false);
						}
						if (oggConfBO.isDataprot())
						{
							rec.setEtichettaDataProtocollo(true);
						}
						else
						{
							rec.setEtichettaDataProtocollo(false);
						}
//						if (oggConfBO.getRinnovo().length()==1)
//						{
							if (oggConfBO.getRinnovo()=='O' || oggConfBO.getRinnovo()=='P' || oggConfBO.getRinnovo()=='N')
							{
								rec.setTipoRinnovo(String.valueOf(oggConfBO.getRinnovo()));
							}
							else
							{
								rec.setTipoRinnovo(null);

							}
//						}
//						else
//						{
//							rec.setTipoRinnovo(null);
//						}

						if (oggConfBO.isRistampa())
						{
							rec.setIndicaRistampa(true);
						}
						else
						{
							rec.setIndicaRistampa(false);
						}
					}
					StrutturaTerna rec2 = new StrutturaTerna("","","");
					rec2.setCodice1(String.valueOf(oggConfBO.getProgr().doubleValue()));
					rec2.setCodice2(oggConfBO.getDescr_test());
					rec2.setCodice3(oggConfBO.getDescr_foot());
					righeRisultato.add(rec2);
				}
				// caricamento int e foot nell VO
				StrutturaTerna [] righeInt=new StrutturaTerna [0];
				StrutturaTerna [] righeFoot=new StrutturaTerna [0];

				if (numRighe >0)
				{
					righeInt=new StrutturaTerna [numRighe];
					righeFoot=new StrutturaTerna [numRighe];
					int dimInt=numRighe;
					int dimFoot=numRighe;
					for (int i=0; i<numRighe; i++)
					{
						// carica int
						if (righeRisultato.get(i)!=null && righeRisultato.get(i).getCodice2()!=null && righeRisultato.get(i).getCodice2().length()>0)
						{
							righeInt[i]=new StrutturaTerna ("","","");
							righeInt[i].setCodice1(righeRisultato.get(i).getCodice1());
							righeInt[i].setCodice2(righeRisultato.get(i).getCodice2());
							righeInt[i].setCodice3("");
						}
						else
						{
							dimInt=dimInt - 1;
						}

						// carica foot
						if (righeRisultato.get(i)!=null && righeRisultato.get(i).getCodice3()!=null && righeRisultato.get(i).getCodice3().length()>0)
						{
							righeFoot[i]=new StrutturaTerna ("","","");
							righeFoot[i].setCodice1(righeRisultato.get(i).getCodice1());
							righeFoot[i].setCodice2(righeRisultato.get(i).getCodice3());
							righeFoot[i].setCodice3("");
						}
						else
						{
							dimFoot=dimFoot -1;
						}
					}
					if (dimFoot< numRighe )
					{
						if (dimFoot>0)
						{
							// CICLO DI RICARICA
							rec.setListaDatiFineStampa(new StrutturaTerna [dimFoot]);
							for (int j=0; j<dimFoot; j++)
							{
								rec.getListaDatiFineStampa()[j]= righeFoot[j];
							}
						}
						else
							{
							rec.setListaDatiFineStampa(new StrutturaTerna [1]);
							rec.getListaDatiFineStampa()[0]=new StrutturaTerna("1","","");
							}
					}
					else
					{
						rec.setListaDatiFineStampa(righeFoot);
					}
					if (dimInt< numRighe)
					{
						if (dimInt>0)
						{
							// CICLO DI RICARICA
							rec.setListaDatiIntestazione(new StrutturaTerna [dimInt]);
							for (int z=0; z<dimInt; z++)
							{
								rec.getListaDatiIntestazione()[z]= righeInt[z];
							}
						}
						else
							{
							rec.setListaDatiIntestazione(new StrutturaTerna [1]);
							rec.getListaDatiIntestazione()[0]=new StrutturaTerna("1","","");
							}

					}
					else
					{
						rec.setListaDatiIntestazione(righeInt);
					}



				}
				else
				{
					rec = null;
				}
				//

/*	}catch (ValidationException e) {
      	  //logger.error("Errore in getBibliteche",e);
      	  throw e;
            //throw new EJBException(e);
*/	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return rec;
	//return result;
	}


	public String  getCodiciDaTbCodici(String tp_tabella, String cd_tabella)
	throws  DaoManagerException{
		String ds_tabella_str ="";
		try{
			Session session = this.getCurrentSession();
			Criteria crCod = session.createCriteria(Tb_codici.class, "c");
			crCod.add(Restrictions.eq("c.tp_tabella",tp_tabella));
			crCod.add(Restrictions.eq("c.cd_tabella",cd_tabella.trim() ));
			crCod.setProjection(Projections.projectionList()
					.add(Projections.property("c.ds_tabella")));
			List<String> denoresults = crCod.list();
			if (denoresults!=null && denoresults.size()>0)
			{
				ds_tabella_str=denoresults.get(0).trim();
			}

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ds_tabella_str;
	}




	public boolean inserisciOrdineHib(OrdiniVO ordine)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(ordine);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public boolean inserisciOrdineBiblHib(Tra_ordini_biblioteche ordineBib)
	throws  DaoManagerException{
		// per i centri sistema
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(ordineBib);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


	public boolean  modificaOrdineHib(OrdiniVO ordine)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(ordine); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaOrdineHib(OrdiniVO ordine)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(ordine); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	private static class OrdComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tba_ordini) o1).getCod_fornitore().getNom_fornitore().toLowerCase();
				String e2 = ((Tba_ordini) o2).getCod_fornitore().getNom_fornitore().toLowerCase();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public Tra_ordine_carrello_spedizione getOrdineCarrelloSpedizione(
			int idOrdine) throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			return (Tra_ordine_carrello_spedizione) session.get(Tra_ordine_carrello_spedizione.class, idOrdine);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void salvaOrdineCarrelloSpedizione(Tra_ordine_carrello_spedizione ocs) throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			ocs.setOrdine((Tba_ordini) session.get(Tba_ordini.class, ocs.getIdOrdine() ));
			if (ocs.getTs_ins() == null) {
				ocs.setTs_ins(now());
				session.save(ocs);
			} else
				session.merge(ocs);

			session.flush();

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tra_ordine_inventari> getListaInventariRilegatura(int idOrdine) throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tra_ordine_inventari.class);
			c.add(Restrictions.eq("id_ordine.id", idOrdine));
			//c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.asc("posizione"));
			c.addOrder(Order.asc("cd_polo"));

			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Object[] countInventariRilegatura(int idOrdine) throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tra_ordine_inventari.class);
			c.add(Restrictions.eq("id_ordine.id", idOrdine));
			//c.add(Restrictions.ne("fl_canc", 'S'));
			c.setProjection(Projections.projectionList()
				.add(Projections.rowCount())
				.add(Projections.max("volume")));

			return (Object[]) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tra_ordine_carrello_spedizione> getSpedizioni(String codPolo, String codBib,
			Date dataSpedizione, short prgSpedizione)  throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tra_ordine_carrello_spedizione.class);
			c.add(Restrictions.eq("dt_spedizione", dataSpedizione));
			if (prgSpedizione > 0)
				c.add(Restrictions.eq("prg_spedizione", prgSpedizione));

			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.gt("prg_spedizione", new Short((short) 0) ));
			c.add(Restrictions.isNotNull("cart_name"));
			c.createCriteria("ordine", "o").add(Restrictions.eq("o.cd_bib", creaIdBib(codPolo, codBib)));

			c.addOrder(Order.asc("prg_spedizione"));
			c.addOrder(Order.asc("cart_name"));
			c.addOrder(Order.asc("o.anno_ord"));
			c.addOrder(Order.asc("o.cod_tip_ord"));
			c.addOrder(Order.asc("o.cod_ord"));

			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tra_ordine_carrello_spedizione getSpedizione(String codPolo, String codBib, String cartName) throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tra_ordine_carrello_spedizione.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.gt("prg_spedizione", new Short((short) 0) ));
			c.add(Restrictions.eq("cart_name", cartName));
			c.createCriteria("ordine", "o").add(Restrictions.eq("o.cd_bib", creaIdBib(codPolo, codBib)));

			return (Tra_ordine_carrello_spedizione) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public BigDecimal importoInventariOrdine(String codPolo, String codBib, String codTipOrd,
			int annoOrd, int codOrd) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			c.add(Restrictions.eq("cd_bib_ord", codBib));
			c.add(Restrictions.eq("cd_tip_ord", codTipOrd));
			c.add(Restrictions.eq("anno_ord", annoOrd));
			c.add(Restrictions.eq("cd_ord", codOrd));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.in("cd_sit", new Character[] {'1', '2'}));
			c.add(Restrictions.eq("cd_serie.cd_polo", bib));
			c.setProjection(Projections.sum("importo"));

			return (BigDecimal) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public int countInventariOrdine(String codPolo, String codBib, String codTipOrd,
			int annoOrd, int codOrd) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			c.add(Restrictions.eq("cd_bib_ord", codBib));
			c.add(Restrictions.eq("cd_tip_ord", codTipOrd));
			c.add(Restrictions.eq("anno_ord", annoOrd));
			c.add(Restrictions.eq("cd_ord", codOrd));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.in("cd_sit", new Character[] {'1', '2'}));
			c.add(Restrictions.eq("cd_serie.cd_polo", bib));
			c.setProjection(Projections.rowCount());
			Number cnt = (Number)c.uniqueResult();

			return cnt.intValue();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tra_ordine_inventari getInventarioRilegatura(Tbc_inventario inv) throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tra_ordine_inventari.class);
			c.add(Restrictions.eq("cd_polo", inv));
			//c.add(Restrictions.ne("fl_canc", 'S'));

			return (Tra_ordine_inventari) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

}
